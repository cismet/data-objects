/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.db;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import de.cismet.diff.DiffAccessor;

/**
 * DOCUMENT ME!
 *
 * @author   Martin Scholl
 * @version  1.0
 */
public final class DatabaseConnection {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(DatabaseConnection.class);
    private static final Map<Integer, Connection> CON_HASH_UPDATE = new HashMap<Integer, Connection>();
    private static final Map<Integer, Connection> CON_HASH_EXEC = new HashMap<Integer, Connection>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>DatabaseConnection.</code>
     */
    private DatabaseConnection() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Connect to the database.
     *
     * @param   runtime  DOCUMENT ME!
     *
     * @return  java.sql.Connection to the database
     *
     * @throws  SQLException  if an error occurs during creation of the connection
     */
    public static Connection getConnection(final Properties runtime) throws SQLException {
        return getConnection(runtime, 30);
    }

    /**
     * Connect to the database.
     *
     * @param   runtime  DOCUMENT ME!
     * @param   timeOut  DOCUMENT ME!
     *
     * @return  java.sql.Connection to the database
     *
     * @throws  SQLException  if an error occurs during creation of the connection
     */
    public static Connection getConnection(final Properties runtime, final int timeOut) throws SQLException {
        final ResourceBundle exceptionBundle = ResourceBundle.getBundle(
                DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
        final String driver = runtime.getProperty("connection.driver_class");
        try {
            Class.forName(driver).newInstance();
        } catch (final ClassNotFoundException e) {
            throw new SQLException(exceptionBundle.getString(DiffAccessor.SQL_EXCEPTION_LOCATE_JDBC_DRIVER_FAILED), e);
        } catch (final InstantiationException e) {
            throw new SQLException(exceptionBundle.getString(DiffAccessor.SQL_EXCEPTION_JDBC_INSTANTIATION_FAILED), e);
        } catch (final IllegalAccessException e) {
            throw new SQLException(
                exceptionBundle.getString(DiffAccessor.SQL_EXCEPTION_JDBC_INSTANTIATION_ILLEGAL_ACCESS),
                e);
        }
        final int dTO = DriverManager.getLoginTimeout();
        DriverManager.setLoginTimeout(timeOut);

        final String dbURL = runtime.getProperty("connection.url");
        final String username = runtime.getProperty("connection.username");
        final String password = runtime.getProperty("connection.password");
        try {
            return DriverManager.getConnection(dbURL, username, password);
        } finally {
            DriverManager.setLoginTimeout(dTO);
        }
    }

    /**
     * Executes update sql statements such as ALTER, INSERT, UPDATE etc. It uses just one Connection instance for all
     * method calls to enable transaction functionality. Auto commit is set to false.
     *
     * @param   runtime         DOCUMENT ME!
     * @param   sql             DOCUMENT ME!
     * @param   callerHashcode  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    // the connections used here will be cached and reused, clear() closes them
    @SuppressWarnings("PMD.CloseResource")
    public static int updateSQL(final Properties runtime, final String sql, final int callerHashcode)
            throws SQLException {
        // FIXME: missing sync may cause unneccesary connection creation and memory leaks
        if (CON_HASH_UPDATE.get(callerHashcode) == null) {
            final Connection con = getConnection(runtime);
            con.setAutoCommit(false);
            CON_HASH_UPDATE.put(callerHashcode, con);
        }

        return CON_HASH_UPDATE.get(callerHashcode).createStatement().executeUpdate(sql);
    }

    /**
     * Executes query sql statements such as SELECT.
     *
     * @param   runtime         DOCUMENT ME!
     * @param   sql             DOCUMENT ME!
     * @param   callerHashcode  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    public static ResultSet execSQL(final Properties runtime, final String sql, final int callerHashcode)
            throws SQLException {
        if (CON_HASH_EXEC.get(callerHashcode) == null) {
            CON_HASH_EXEC.put(callerHashcode, getConnection(runtime));
        }
        return CON_HASH_EXEC.get(callerHashcode).createStatement().executeQuery(sql);
    }

    /**
     * Releases all cached connections.*
     */
    // don't know why this an issue for the PMD plugin, the whole purpose of this operation is to close methods...
    @SuppressWarnings("PMD.CloseResource")
    public static void clear() {
        for (final Connection con : CON_HASH_EXEC.values()) {
            try {
                con.close();
            } catch (final SQLException e) {
                LOG.warn("could not close connection: " + con, e); // NOI18N
            }
        }
        for (final Connection con : CON_HASH_UPDATE.values()) {
            try {
                con.close();
            } catch (final SQLException e) {
                LOG.warn("could not close connection: " + con, e); // NOI18N
            }
        }
        CON_HASH_EXEC.clear();
        CON_HASH_UPDATE.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cons  DOCUMENT ME!
     */
    // don't know why this an issue for the PMD plugin, the whole purpose of this operation is to close methods...
    @SuppressWarnings("PMD.CloseResource")
    public static void closeConnections(final Connection... cons) {
        for (final Connection con : cons) {
            closeConnection(con);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  con  DOCUMENT ME!
     */
    // don't know why this an issue for the PMD plugin, the whole purpose of this operation is to close methods...
    @SuppressWarnings("PMD.CloseResource")
    public static void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (final SQLException e) {
                LOG.warn("could not close connection", e); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  stmts  DOCUMENT ME!
     */
    // don't know why this an issue for the PMD plugin, the whole purpose of this operation is to close methods...
    @SuppressWarnings("PMD.CloseResource")
    public static void closeStatements(final Statement... stmts) {
        for (final Statement stmt : stmts) {
            closeStatement(stmt);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  stmt  DOCUMENT ME!
     */
    // don't know why this an issue for the PMD plugin, the whole purpose of this operation is to close methods...
    @SuppressWarnings("PMD.CloseResource")
    public static void closeStatement(final Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (final SQLException e) {
                LOG.warn("could not close statement", e); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sets  DOCUMENT ME!
     */
    // don't know why this an issue for the PMD plugin, the whole purpose of this operation is to close methods...
    @SuppressWarnings("PMD.CloseResource")
    public static void closeResultSets(final ResultSet... sets) {
        for (final ResultSet set : sets) {
            closeResultSet(set);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  set  DOCUMENT ME!
     */
    // don't know why this an issue for the PMD plugin, the whole purpose of this operation is to close methods...
    @SuppressWarnings("PMD.CloseResource")
    public static void closeResultSet(final ResultSet set) {
        if (set != null) {
            try {
                set.close();
            } catch (final SQLException e) {
                LOG.warn("could not close resultset", e); // NOI18N
            }
        }
    }
}
