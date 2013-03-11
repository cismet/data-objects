/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service.impl;

import org.apache.log4j.Logger;

import org.openide.util.NbBundle;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Properties;

import de.cismet.cids.jpa.backend.service.MetaService;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;

import de.cismet.commons.utils.ProgressEvent;
import de.cismet.commons.utils.ProgressEvent.State;
import de.cismet.commons.utils.ProgressListener;
import de.cismet.commons.utils.ProgressObservable;
import de.cismet.commons.utils.ProgressSupport;

import de.cismet.diff.db.DatabaseConnection;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  1.3
 */
public class MetaBackend implements MetaService, ProgressObservable {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(MetaBackend.class);

    //~ Instance fields --------------------------------------------------------

    private final transient ProgressSupport progressSupport;
    private final transient Properties props;

    private transient boolean closed;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of MetaBackend.
     *
     * @param  runtimeProps  DOCUMENT ME!
     */
    public MetaBackend(final Properties runtimeProps) {
        this.progressSupport = new ProgressSupport();
        this.closed = false;

        this.props = runtimeProps;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    // there is no choice but to obay the interface specification
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @Override
    public void close() throws Exception {
        closed = true;
    }

    @Override
    public void refreshIndex(final CidsClass cidsClass) throws SQLException {
        if (cidsClass == null) {
            throw new IllegalArgumentException("cidsClass must not be null"); // NOI18N
        }

        if (closed) {
            throw new IllegalStateException("backend already closed: " + this); // NOI18N
        }

        final long startTime = System.currentTimeMillis();
        if (LOG.isInfoEnabled()) {
            LOG.info("start refreshIndex for class: " + cidsClass.getName()); // NOI18N
        }

        progressSupport.fireEvent(
            new ProgressEvent(
                this,
                State.STARTED,
                0,
                0,
                NbBundle.getMessage(
                    MetaBackend.class,
                    "MetaBackend.refreshIndex(CidsClass).indexingStarted", // NOI18N
                    cidsClass.getName())));

        Connection con = null;
        try {
            con = DatabaseConnection.getConnection(props);

            con.createStatement().execute("SELECT reindex(" + cidsClass.getId() + ");"); // NOI18N
        } catch (final SQLException e) {
            LOG.error("re-indexing of class '" + cidsClass.getName() + "' failed", e);   // NOI18N

            progressSupport.fireEvent(
                new ProgressEvent(
                    this,
                    State.BROKEN,
                    0,
                    0,
                    NbBundle.getMessage(
                        MetaBackend.class,
                        "MetaBackend.refreshIndex(CidsClass).indexingFailed", // NOI18N
                        cidsClass.getName(),
                        e.getLocalizedMessage())));

            throw e;
        } finally {
            DatabaseConnection.closeConnection(con);
        }

        final long duration = (System.currentTimeMillis() - startTime) / 1000;
        if (LOG.isInfoEnabled()) {
            LOG.info("refresh index for class '" + cidsClass.getName() + "' completed in " + duration + " seconds"); // NOI18N
        }
        progressSupport.fireEvent(
            new ProgressEvent(
                this,
                State.FINISHED,
                0,
                0,
                NbBundle.getMessage(
                    MetaBackend.class,
                    "MetaBackend.refreshIndex(CidsClass).indexingFinished",                                          // NOI18N
                    cidsClass.getName(),
                    duration)));
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void cancel() {
        // currently not supported
    }

    @Override
    public void addProgressListener(final ProgressListener pl) {
        progressSupport.addProgressListener(pl);
    }

    @Override
    public void removeProgressListener(final ProgressListener pl) {
        progressSupport.removeProgressListener(pl);
    }
}
