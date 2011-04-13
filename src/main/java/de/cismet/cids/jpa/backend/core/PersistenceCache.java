/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.core;

import com.mchange.v1.util.ClosableResource;

import com.rits.cloning.Cloner;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.persistence.NoResultException;

import de.cismet.cids.jpa.backend.service.CommonService;
import de.cismet.cids.jpa.entity.common.CommonEntity;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class PersistenceCache implements MethodInterceptor, ClosableResource {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(PersistenceCache.class);

    //~ Instance fields --------------------------------------------------------

    private final transient Map<Class, List<CommonEntity>> entityCache;
    private final transient ReentrantReadWriteLock lock;
    private final transient CommonService cs;
    private final transient Cloner cloner;
    private transient volatile boolean closed;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PersistenceCache object.
     *
     * @param  cs  DOCUMENT ME!
     */
    public PersistenceCache(final CommonService cs) {
        this.entityCache = new HashMap<Class, List<CommonEntity>>(50);
        this.lock = new ReentrantReadWriteLock();
        this.cs = cs;
        this.cloner = new Cloner();
        closed = false;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Object invoke(final MethodInvocation mi) throws Throwable {
        if (LOG.isDebugEnabled()) {
            LOG.debug("cache interceptor: " + mi.getMethod()); // NOI18N
        }

        lock.readLock().lock();
        try {
            if (closed) {
                throw new IllegalStateException("cache already closed"); // NOI18N
            }
        } finally {
            lock.readLock().unlock();
        }

        final Cached cached = mi.getMethod().getAnnotation(Cached.class);

        assert cached != null : "erroneous interception, cached is null"; // NOI18N

        // prepare the cache
        prepare(mi);

        switch (cached.type()) {
            case GET: {
                return get(mi);
            }
            case PUT: {
                return store(mi);
            }
            case DEL: {
                return delete(mi);
            }
        }

        throw new IllegalStateException("illegal cache configuration, check your cache annotations and cache types"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     */
    // calls from outside are always ok, but calls from inside cannot be done if readlock is aquired -> deadlock
    public void invalidate() {
        lock.writeLock().lock();
        try {
            entityCache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mi  DOCUMENT ME!
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    private void prepare(final MethodInvocation mi) {
        Lock current = lock.readLock();
        current.lock();

        try {
            if (closed) {
                throw new IllegalStateException("cache already closed"); // NOI18N
            }

            final Class[] entityClasses = getEntityClass(mi.getArguments());
            for (final Class entityClass : entityClasses) {
                if (!entityCache.keySet().contains(entityClass)) {
                    // upgrade the lock to safely write to the cache
                    current.unlock();
                    current = lock.writeLock();
                    current.lock();

                    if (!entityCache.keySet().contains(entityClass)) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("preparing cache: " + entityClass); // NOI18N
                        }

                        final List<CommonEntity> entities = cs.getAllEntities(entityClass);
                        entityCache.put(entityClass, entities);
                    }

                    // downgrade the lock again
                    final ReentrantReadWriteLock.ReadLock rl = lock.readLock();
                    rl.lock();
                    current.unlock();
                    current = rl;
                }
            }
        } finally {
            current.unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mi  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Throwable              DOCUMENT ME!
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    private Object store(final MethodInvocation mi) throws Throwable {
        lock.writeLock().lock();

        try {
            if (closed) {
                throw new IllegalStateException("cache already closed"); // NOI18N
            }

            final Object ret;
            try {
                ret = mi.proceed();
            } catch (final Exception e) {
                LOG.error("error during store call, invalidating cache", e); // NOI18N
                invalidate();
                throw e;
            }

            try {
                final CommonEntity before = getCEArg(mi.getArguments());

                if (ret instanceof CommonEntity) {
                    final CommonEntity after = (CommonEntity)ret;
                    final List<CommonEntity> entities = entityCache.get(before.getClass());

                    assert entities != null : "cache not properly prepared"; // NOI18N

                    if (before.getId() == null) {
                        // new object, only add to the cache
                        entities.add(after);
                    } else {
                        // exchange cached object
                        int exchangeIndex = -1;
                        for (int i = 0; i < entities.size(); ++i) {
                            if (entities.get(i).equals(before)) {
                                exchangeIndex = i;
                                break;
                            }
                        }

                        if (exchangeIndex < 0) {
                            LOG.error(
                                "stored object not found in cache, invalidating cache for CommonEntity: " // NOI18N
                                        + before);
                            entityCache.remove(before.getClass());
                        } else {
                            entities.set(exchangeIndex, after);
                        }
                    }
                } else {
                    LOG.error("erroneous store call, invalidating cache for CommonEntity: " + before);    // NOI18N
                    entityCache.remove(before.getClass());
                }
            } catch (final CacheException e) {
                LOG.error("unsupported cache call, invalidating cache: " + mi.getMethod());               // NOI18N
                invalidate();
            }

            return cloner.deepClone(ret);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CacheException  DOCUMENT ME!
     */
    private CommonEntity getCEArg(final Object[] args) throws CacheException {
        if ((args.length == 1) && (args[0] instanceof CommonEntity)) {
            return (CommonEntity)args[0];
        } else {
            final String message = "cannot find CommonEntity argument"; // NOI18N
            LOG.error(message);
            throw new CacheException(message);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CacheException  DOCUMENT ME!
     */
    private List<CommonEntity> getCEArgs(final Object[] args) throws CacheException {
        if ((args.length == 1) && (args[0] instanceof List)) {
            return (List)args[0];
        } else {
            final String message = "cannot find CommonEntity argument"; // NOI18N
            LOG.error(message);
            throw new CacheException(message);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mi  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CacheException            DOCUMENT ME!
     * @throws  IllegalStateException     DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     * @throws  NoResultException         DOCUMENT ME!
     */
    private Object get(final MethodInvocation mi) throws CacheException {
        lock.readLock().lock();

        try {
            if (closed) {
                throw new IllegalStateException("backend already closed"); // NOI18N
            }

            final Method method = mi.getMethod();
            final Class returnType = method.getReturnType();
            final Class[] entityClasses = getEntityClass(mi.getArguments());

            if (entityClasses.length != 1) {
                throw new CacheException("get does not support multiple entity classes: " // NOI18N
                            + Arrays.toString(entityClasses));
            }

            final List<CommonEntity> entities = entityCache.get(entityClasses[0]);

            assert entities != null : "cache not correctly initialised, class not present: " + entityClasses[0]; // NOI18N

            final Object ret;
            if (boolean.class.isAssignableFrom(returnType)) {
                // contains request

                // we assume that the second argument is present and it is a String
                final String toFind = (String)mi.getArguments()[1];
                if (toFind == null) {
                    throw new IllegalArgumentException("name must not be null"); // NOI18N
                }

                ret = get(entities, "name", toFind) != null; // NOI18N
            } else if (Collection.class.isAssignableFrom(returnType)) {
                // getAll request
                ret = entityCache.get(entityClasses[0]);
            } else {
                // getby name or id

                final Object toFind = mi.getArguments()[1];
                if (toFind instanceof String) {
                    // get by name
                    ret = get(entities, "name", toFind); // NOI18N
                } else {
                    // get by id
                    ret = get(entities, "id", toFind); // NOI18N
                }

                // throw exception to be consistent with CommonService
                if (ret == null) {
                    throw new NoResultException("'" + entityClasses[0].getSimpleName()
                                + "' with property value '" // NOI18N
                                + toFind + "' does not exist"); // NOI18N
                }
            }

            if (LOG.isDebugEnabled()) {
                LOG.debug("found return value: " + ret); // NOI18N
            }

            return cloner.deepClone(ret);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   entities  DOCUMENT ME!
     * @param   prop      name DOCUMENT ME!
     * @param   toFind    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CacheException  DOCUMENT ME!
     */
    private Object get(final List<CommonEntity> entities, final String prop, final Object toFind)
            throws CacheException {
        for (final CommonEntity ce : entities) {
            final Object current;
            try {
                current = PropertyUtils.getProperty(ce, prop);
            } catch (final Exception ex) {
                final String message = "error accessing bean, does probably not have a property '" + prop + "': " + ce; // NOI18N
                LOG.error(message, ex);
                throw new CacheException(message, ex);
            }

            if (toFind.equals(current)) {
                return ce;
            }
        }

        // we did not find any entity with that name
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   mi  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Throwable              DOCUMENT ME!
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    private Object delete(final MethodInvocation mi) throws Throwable {
        lock.writeLock().lock();

        try {
            if (closed) {
                throw new IllegalStateException("backend already closed"); // NOI18N
            }

            final Object ret;
            try {
                ret = mi.proceed();
            } catch (final Exception e) {
                LOG.error("error during store call, invalidating cache", e); // NOI18N
                invalidate();
                throw e;
            }

            try {
                final Object[] args = mi.getArguments();
                if (args.length != 1) {
                    throw new CacheException("invalid number of arguments: " + args.length); // NOI18N
                }

                if (args[0] instanceof CommonEntity) {
                    final CommonEntity before = (CommonEntity)args[0];
                    final List<CommonEntity> entities = entityCache.get(before.getClass());

                    assert entities != null : "cache not properly prepared"; // NOI18N

                    if (!entities.remove(before)) {
                        LOG.error("deleted object not found in cache, invalidating cache for CommonEntity: " + before); // NOI18N
                        entityCache.remove(before.getClass());
                    }
                } else if (args[0] instanceof List) {
                    final List<CommonEntity> before = (List)args[0];
                    if (before.isEmpty()) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("empty delete request parameter: " + before);                                     // NOI18N
                        }
                    } else {
                        final CommonEntity ce = before.get(0);
                        final List<CommonEntity> entities = entityCache.get(ce.getClass());

                        assert entities != null : "cache not properly prepared"; // NOI18N

                        for (final CommonEntity entity : before) {
                            if (!entities.remove(entity)) {
                                LOG.error(
                                    "deleted object not found in cache, invalidating cache for CommonEntity: " // NOI18N
                                            + entity.getClass());
                                entityCache.remove(entity.getClass());
                                // we do not end here since it is legal to delete entities of different classes at once
                            }
                        }
                    }
                } else {
                    throw new CacheException("unsupported argument type: " + args[0]);                         // NOI18N
                }
            } catch (final CacheException e) {
                LOG.error("unsupported cache call, invalidating cache: " + mi.getMethod(), e);                 // NOI18N
                invalidate();
            }

            return ret;
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    private Class[] getEntityClass(final Object... args) {
        for (final Object arg : args) {
            if (arg instanceof Class) {
                return new Class[] { (Class)arg };
            } else if (arg instanceof CommonEntity) {
                return new Class[] { arg.getClass() };
            } else if (arg instanceof List) {
                final List list = (List)arg;
                if (list.isEmpty()) {
                    LOG.warn("cannot determine entity class since the given list arg is empty"); // NOI18N

                    return new Class[] {};
                } else {
                    final Set<Class> classes = new HashSet<Class>();
                    for (final Object o : list) {
                        classes.add(o.getClass());
                    }

                    return classes.toArray(new Class[classes.size()]);
                }
            }
        }

        throw new IllegalStateException("cache cannot find entity class, check your configuration"); // NOI18N
    }

    @Override
    public void close() throws Exception {
        lock.writeLock().lock();

        try {
            entityCache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
