/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.meta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.MessageFormat;

import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import de.cismet.diff.db.DatabaseConnection;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public class CsLocksConnection {

    //~ Instance fields --------------------------------------------------------

    private final Connection con;
    private final Properties runtime;
    private final ResourceBundle bundle;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CsLocksConnection object.
     *
     * @param   runtime  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public CsLocksConnection(final Properties runtime) throws SQLException {
        this.runtime = runtime;

        this.con = DatabaseConnection.getConnection(runtime);
        this.con.setAutoCommit(false);

        final String dialect = this.runtime.getProperty("internalDialect", "postgres_9");  // NOI18N
        this.bundle = ResourceBundle.getBundle("de.cismet.cids.meta.cs_locks_" + dialect); // NOI18N
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void begin() {
        // noop
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public void lockTable() throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(bundle.getString("lock_table"));
        } finally {
            DatabaseConnection.closeStatement(stmt);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public void rollback() throws SQLException {
        con.rollback();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public void commit() throws SQLException {
        con.commit();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   lockPrefix  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public LockEntry getLock(final String lockPrefix) throws SQLException {
        Statement stmt = null;
        ResultSet set = null;
        try {
            stmt = con.createStatement();
            set = stmt.executeQuery(MessageFormat.format(bundle.getString("get_lock"), lockPrefix));

            final LockEntry lockEntry;
            if (set.next()) {
                final String when = set.getString("user_string").replace(lockPrefix, ""); // NOI18N
                final Date date = new Date(Long.valueOf(when));
                final String who = set.getString("additional_info");                      // NOI18N

                lockEntry = new LockEntry(date, who);
            } else {
                lockEntry = null;
            }

            return lockEntry;
        } finally {
            DatabaseConnection.closeResultSet(set);
            DatabaseConnection.closeStatement(stmt);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   lockPrefix  DOCUMENT ME!
     * @param   who         DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public String setLock(final String lockPrefix, final String who) throws SQLException {
        final String nonce = lockPrefix + "_" + System.currentTimeMillis();
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(MessageFormat.format(bundle.getString("set_lock"), nonce, who)); // NOI18N

            return nonce;
        } finally {
            DatabaseConnection.closeStatement(stmt);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   nonce  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public void releaseLock(final String nonce) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(MessageFormat.format(bundle.getString("release_lock"), nonce)); // NOI18N
        } finally {
            DatabaseConnection.closeStatement(stmt);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   lockPrefix  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public void releaseAllLocks(final String lockPrefix) throws SQLException {
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            stmt.executeUpdate(MessageFormat.format(bundle.getString("release_all_locks"), lockPrefix)); // NOI18N
        } finally {
            DatabaseConnection.closeStatement(stmt);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        try {
            con.close();
        } catch (final SQLException ex) {
            // noop
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class LockEntry {

        //~ Instance fields ----------------------------------------------------

        public Date when;
        public String who;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new LockEntry object.
         */
        public LockEntry() {
        }

        /**
         * Creates a new LockEntry object.
         *
         * @param  when  DOCUMENT ME!
         * @param  who   DOCUMENT ME!
         */
        public LockEntry(final Date when, final String who) {
            this.when = when;
            this.who = who;
        }
    }
}
