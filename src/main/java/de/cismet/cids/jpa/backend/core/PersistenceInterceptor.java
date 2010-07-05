/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.core;

import com.mchange.v1.util.ClosableResource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class PersistenceInterceptor implements MethodInterceptor {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(PersistenceInterceptor.class);

    //~ Instance fields --------------------------------------------------------

    private final transient PersistenceProvider provider;
    private final transient ThreadLocal<Method> methodHolder;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of PersistenceInterceptor.
     *
     * @param  provider  DOCUMENT ME!
     */
    public PersistenceInterceptor(final PersistenceProvider provider) {
        this.provider = provider;
        this.methodHolder = new ThreadLocal<Method>();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Object invoke(final MethodInvocation mi) throws Throwable {
        final Method method = mi.getMethod();
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug(
                    "invokation started: " // NOI18N
                            + "method="
                            + method.getDeclaringClass().getCanonicalName()
                            + "."
                            + method.getName()
                            + " | "        // NOI18N
                            + "args="
                            + argsToString(mi.getArguments())); // NOI18N
            }
            if (!method.getDeclaringClass().isAssignableFrom(ClosableResource.class)) {
                injectManager(method);
            }
            final Object ret = method.invoke(mi.getThis(), mi.getArguments());
            cleanup(method, null);
            if (LOG.isDebugEnabled()) {
                LOG.debug(
                    "invokation succeeded: " // NOI18N
                            + "method="
                            + method.getDeclaringClass().getCanonicalName()
                            + "."
                            + method.getName()
                            + " | "        // NOI18N
                            + "args="
                            + argsToString(mi.getArguments())); // NOI18N
            }
            return ret;
        } catch (final Throwable t) {
            // retrieve wrapped exception
            final Throwable toThrow = (t.getCause() == null) ? t : t.getCause();
            // TODO: maybe do not log with level ERROR if t is instance of
            // NoResultException
            try {
                cleanup(method, toThrow);
            } catch (final Throwable ex) {
                // do nothing since cleanup already handles logging
                LOG.warn("An error occured during cleanup operation", ex);
            }
            if (toThrow instanceof NoResultException) {
                LOG.warn(
                    "invokation failed: " // NOI18N
                            + "method="
                            + method.getDeclaringClass().getCanonicalName()
                            + "."
                            + method.getName()
                            + " | "       // NOI18N
                            + "args="
                            + argsToString(mi.getArguments()),
                    toThrow);
            } else {
                LOG.error(
                    "invokation failed: " // NOI18N
                            + "method="
                            + method.getDeclaringClass().getCanonicalName()
                            + "."
                            + method.getName()
                            + " | "       // NOI18N
                            + "args="
                            + argsToString(mi.getArguments()), // NOI18N
                    toThrow);
            }
            throw toThrow;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String argsToString(final Object[] args) {
        if ((args == null) || (args.length == 0)) {
            return ""; // NOI18N
        }

        final StringBuffer sb = new StringBuffer();
        sb.append(args[0]);
        for (int i = 1; i < args.length; ++i) {
            sb.append(", ").append(args[i]); // NOI18N
        }

        return sb.toString();
    }

    /**
     * This impl could be error-prone because some vm impls may return an empty stacktrace instead of an accurate one.
     * It MUST be called from the same thread, that is going to call cleanup afterwards!
     *
     * @param  m  DOCUMENT ME!
     */
    private void injectManager(final Method m) {
        if (methodHolder.get() != null) {
            return;
        }
        final EntityManager em = provider.emf.createEntityManager();
        em.getTransaction().begin();
        provider.em.set(em);
        methodHolder.set(m);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   m  DOCUMENT ME!
     * @param   t  DOCUMENT ME!
     *
     * @throws  Throwable  DOCUMENT ME!
     */
    private void cleanup(final Method m, final Throwable t) throws Throwable {
        final Method associatedMethod = methodHolder.get();
        // if no method is associated with the calling thread or if method to
        // cleanup for is not the same as the associated method do nothing
        if ((associatedMethod == null) || !associatedMethod.equals(m)) {
            return;
        }
        final EntityManager em = provider.em.get();
        // cleanup was already called, do not try again
        if ((em == null) || !em.isOpen()) {
            return;
        }
        provider.em.set(null);
        methodHolder.set(null);
        try {
            if (t != null) {
                em.getTransaction().rollback();
            } else {
                em.getTransaction().commit();
            }
        } catch (final Throwable tw) {
            LOG.error("cleanup failed: method=" + m.getDeclaringClass().getCanonicalName() + "." + m.getName(), tw); // NOI18N
            throw tw;
        } finally {
            em.clear();
            em.close();
        }
    }
}
