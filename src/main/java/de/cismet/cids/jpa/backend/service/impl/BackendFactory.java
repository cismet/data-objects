/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import java.util.List;
import java.util.Properties;

import de.cismet.cids.jpa.backend.service.Backend;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;

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

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
        final Properties p = new Properties();
        p.load(new BufferedReader(
                new FileReader(
                    new File(
                        "/Users/mscholl/gitwork/cismet/uba/cids-custom-udm2020-di/src/udm2020-diDist/server/udm2020-di/runtime.properties"))));
        final Backend b = getInstance().getBackend(p);
        final List l = b.getAllEntities(CidsClass.class);
        System.out.println(l);
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
