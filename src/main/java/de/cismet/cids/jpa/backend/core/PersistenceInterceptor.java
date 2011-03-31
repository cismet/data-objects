/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.core;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import org.apache.log4j.Logger;

import java.lang.reflect.Method;

import java.sql.SQLException;

import java.text.MessageFormat;

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

    private static final String INV_FAIL_TEMPLATE = "invokation failed: method={0}.{1} | args={2}";     // NOI18N
    private static final String INV_SUCC_TEMPLATE = "invokation succeeded: method={0}.{1} | args={2}";  // NOI18N
    private static final String INV_STARTED_TEMPLATE = "invokation started: method={0}.{1} | args={2}"; // NOI18N

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

    // it is only thrown for tracing purposes
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    @Override
    public Object invoke(final MethodInvocation mi) throws Throwable {
        final Method method = mi.getMethod();
        try {
            if (LOG.isDebugEnabled()) {
                final String message = MessageFormat.format(
                        INV_STARTED_TEMPLATE,
                        method.getDeclaringClass().getCanonicalName(),
                        method.getName(),
                        argsToString(mi.getArguments()));
                LOG.debug(message, new Throwable("trace")); // NOI18N
            }

            injectManager(method);

            final Object ret = method.invoke(mi.getThis(), mi.getArguments());
            cleanup(method, null);
            if (LOG.isDebugEnabled()) {
                final String message = MessageFormat.format(
                        INV_SUCC_TEMPLATE,
                        method.getDeclaringClass().getCanonicalName(),
                        method.getName(),
                        argsToString(mi.getArguments()));
                LOG.debug(message);
            }
            return ret;
        } catch (final Exception e) {
            // retrieve wrapped exception
            final Throwable toThrow = (e.getCause() == null) ? e : e.getCause();
            // TODO: maybe do not log with level ERROR if t is instance of
            // NoResultException
            try {
                cleanup(method, toThrow);
            } catch (final Exception ex) {
                // do nothing since cleanup already handles logging
                LOG.warn("An error occured during cleanup operation", ex); // NOI18N
            }
            final String message = MessageFormat.format(
                    INV_FAIL_TEMPLATE,
                    method.getDeclaringClass().getCanonicalName(),
                    method.getName(),
                    argsToString(mi.getArguments()));
            if (toThrow instanceof NoResultException) {
                LOG.warn(message, toThrow);
            } else {
                LOG.error(message, toThrow);
                Throwable cause = toThrow;
                while (cause != null) {
                    if (cause instanceof SQLException) {
                        final SQLException sqle = (SQLException)cause;
                        LOG.error("next Exception: " + sqle.getNextException(), sqle.getNextException()); // NOI18N
                    }
                    cause = cause.getCause();
                }
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
            if (t == null) {
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
            }
        } catch (final Exception e) {
            LOG.error("cleanup failed: method=" + m.getDeclaringClass().getCanonicalName() + "." + m.getName(), e); // NOI18N
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof SQLException) {
                    final SQLException sqle = (SQLException)cause;
                    LOG.error("next Exception: " + sqle.getNextException(), sqle.getNextException());               // NOI18N
                }
                cause = cause.getCause();
            }
            throw e;
        } finally {
            em.clear();
            em.close();
        }
    }
}
