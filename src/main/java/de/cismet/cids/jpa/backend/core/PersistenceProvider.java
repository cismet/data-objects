/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.core;

import com.mchange.v1.util.ClosableResource;

import java.util.Properties;

import javax.persistence.EntityManager;

import de.cismet.cids.jpa.backend.service.CommonService;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public interface PersistenceProvider extends CommonService {

    //~ Methods ----------------------------------------------------------------

    /**
     * Gets the {@link javax.persistence.EntityManager} that is associated with the current thread.
     *
     * @return  the <code>EntityManager</code> of the current thread.
     */
    EntityManager getEntityManager();

    /**
     * Delivers the runtime properties associated with this <code>PersistenceProviderImpl</code>.
     *
     * @return  the runtime properties
     */
    Properties getRuntimeProperties();

    /**
     * Tests whether this provider is already closed.
     *
     * @return  true if it is closed, false otherwise
     */
    boolean isClosed();

    /**
     * This method shall be used to close resources that depend on the opened state of this provider.
     *
     * @param   resource  the resource to be closed
     *
     * @throws  Exception  any exception that occurs during the close operation of the given resource
     */
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    void monitorClose(final ClosableResource resource) throws Exception;
}
