/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import com.mchange.v1.util.ClosableResource;

import java.sql.SQLException;

import de.cismet.cids.jpa.entity.cidsclass.CidsClass;

import de.cismet.cids.util.Cancelable;

import de.cismet.commons.utils.ProgressObservable;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public interface MetaService extends ClosableResource, ProgressObservable, Cancelable {

    //~ Methods ----------------------------------------------------------------

    /**
     * Refreshes the index of the given {@link CidsClass} by using the new plsql indexing function.
     *
     * <p>This implemenetation makes use of the {@link ProgressListener} to propagate progress state.</p>
     *
     * @param   cidsClass  the class that shall be reindexed
     *
     * @throws  SQLException  if any error occurs during indexing
     */
    void refreshIndex(final CidsClass cidsClass) throws SQLException;
}
