/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service.impl;

import java.util.Properties;

import de.cismet.cids.jpa.backend.service.Backend;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class BackendFactory {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BackendFactory object.
     */
    private BackendFactory() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   runtimeProperties  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Backend getBackend(final Properties runtimeProperties) {
        return getBackend(runtimeProperties, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   runtimeProperties  DOCUMENT ME!
     * @param   caching            DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Backend getBackend(final Properties runtimeProperties, final boolean caching) {
        return new BackendImpl(runtimeProperties, caching);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   backend  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    public boolean isCaching(final Backend backend) {
        if (backend instanceof BackendImpl) {
            return ((BackendImpl)backend).isCaching();
        }

        throw new UnsupportedOperationException("unknown backend implementation: " + backend); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param   backend  DOCUMENT ME!
     *
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    public void flushCache(final Backend backend) {
        if (backend instanceof BackendImpl) {
            ((BackendImpl)backend).flushCache();
        }

        throw new UnsupportedOperationException("unknown backend implementation: " + backend); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param   backend  DOCUMENT ME!
     *
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    public void revalidate(final Backend backend) {
        throw new UnsupportedOperationException("backend validation not available yet");
    }

    /**
     * DOCUMENT ME!
     *
     * @param   backend  DOCUMENT ME!
     * @param   caching  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    public Backend getBackend(final Backend backend, final boolean caching) {
        throw new UnsupportedOperationException("backend transformation not available yet");
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static BackendFactory getInstance() {
        return LazyInitialiser.INSTANCE;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class LazyInitialiser {

        //~ Static fields/initializers -----------------------------------------

        @SuppressWarnings("PMD.AccessorClassGeneration")
        private static final BackendFactory INSTANCE = new BackendFactory();

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LazyInitialiser object.
         */
        private LazyInitialiser() {
        }
    }
}
