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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.text.MessageFormat;

import java.util.Iterator;
import java.util.Properties;

import de.cismet.cids.jpa.backend.service.MetaService;
import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.Type;

import de.cismet.cids.util.AbstractProgressObservable;
import de.cismet.cids.util.ProgressListener;
import de.cismet.cids.util.ProgressListener.ProgressState;

import de.cismet.diff.db.DatabaseConnection;

/**
 * DOCUMENT ME!
 *
 * @author   cschmidt
 * @version  $Revision$, $Date$
 */
public class MetaBackend extends AbstractProgressObservable implements MetaService {

    //~ Static fields/initializers ---------------------------------------------

    // TODO: remove all printstacktraces, set everything final what is possible TODO: clean this messy class -.-

    private static final transient Logger LOG = Logger.getLogger(MetaBackend.class);

    //J-
    private static final String ATTR_MAP_TEMP_TABLE = "cs_all_attr_mapping_temp";                   // NOI18N
    private static final String ATTR_STRING_TEMP_TABLE = "cs_attr_string_temp";                     // NOI18N

    private static final String STMT_PREP_TMP_ATTR_MAP =
            "CREATE TEMPORARY TABLE " + ATTR_MAP_TEMP_TABLE + " (" +                                // NOI18N
                    "class_id INTEGER, " +                                                          // NOI18N
                    "object_id INTEGER, " +                                                         // NOI18N
                    "attr_class_id INTEGER, " +                                                     // NOI18N
                    "attr_object_id INTEGER)";                                                      // NOI18N

    private static final String STMT_PREP_TMP_ATTR_STRING =
            "CREATE TEMP TABLE " + ATTR_STRING_TEMP_TABLE + " (" +                                  // NOI18N
                    "class_id INTEGER, " +                                                          // NOI18N
                    "attr_id INTEGER, " +                                                           // NOI18N
                    "object_id INTEGER, " +                                                         // NOI18N
                    "string_val text )";                                                            // NOI18N

    private static final String STMT_DROP_TMP_ATTR_MAP = "DROP TABLE " + ATTR_MAP_TEMP_TABLE;       // NOI18N

    private static final String STMT_DROP_TMP_ATTR_STRING = "DROP TABLE " + ATTR_STRING_TEMP_TABLE; // NOI18N

    private static final String STMT_INSERT_ATTR_MAP =
            "INSERT INTO " + ATTR_MAP_TEMP_TABLE + " ( " +                                          // NOI18N
                    "class_id, " +                                                                  // NOI18N
                    "object_id, " +                                                                 // NOI18N
                    "attr_class_id, " +                                                             // NOI18N
                    "attr_object_id) " +                                                            // NOI18N
                    "VALUES (?, ?, ?, ?)";                                                          // NOI18N

    private static final String STMT_INSERT_ATTR_STRING =
            "INSERT INTO " + ATTR_STRING_TEMP_TABLE + " (" +                                        // NOI18N
                    "class_id, " +                                                                  // NOI18N
                    "attr_id, " +                                                                   // NOI18N
                    "object_id, " +                                                                 // NOI18N
                    "string_val) " +                                                                // NOI18N
                    "VALUES (?, ?, ?, ?)";                                                          // NOI18N

    private static final String STMT_COPY_TO_ATTR_MAP =
            "INSERT INTO " + CS_ATTR_MAP_TABLE + " (" +                                             // NOI18N
                    "class_id, " +                                                                  // NOI18N
                    "object_id, " +                                                                 // NOI18N
                    "attr_class_id, " +                                                             // NOI18N
                    "attr_object_id) " +                                                            // NOI18N
                    "(SELECT " +                                                                    // NOI18N
                            "class_id, " +                                                          // NOI18N
                            "object_id, " +                                                         // NOI18N
                            "attr_class_id, " +                                                     // NOI18N
                            "attr_object_id " +                                                     // NOI18N
                            "FROM " + ATTR_MAP_TEMP_TABLE + ")";                                    // NOI18N

    private static final String STMT_COPY_TO_ATTR_STRING =
            "INSERT INTO " + CS_ATTR_STRING_TABLE + " (" +                                          // NOI18N
                    "class_id, " +                                                                  // NOI18N
                    "attr_id, " +                                                                   // NOI18N
                    "object_id, " +                                                                 // NOI18N
                    "string_val)" +                                                                 // NOI18N
                    "(SELECT " +                                                                    // NOI18N
                            "class_id, " +                                                          // NOI18N
                            "attr_id, " +                                                           // NOI18N
                            "object_id, " +                                                         // NOI18N
                            "string_val " +                                                         // NOI18N
                            "FROM " + ATTR_STRING_TEMP_TABLE + ")";                                 // NOI18N

    private static final String STMT_DELETE_INDEXES_ATTR_MAP =
            "DELETE FROM " + CS_ATTR_MAP_TABLE + " WHERE class_id = ?";                             // NOI18N

    private static final String STMT_DELETE_INDEXES_ATTR_STRING =
            "DELETE FROM " + CS_ATTR_STRING_TABLE + " WHERE class_id = ?";                          // NOI18N

    private static final String STMT_CLASSID_BY_NAME =
            "SELECT id FROM " + CS_CLASS_TABLE + " WHERE name = ?";                                 // NOI18N

    private static final String STMT_UPDATE_TYPE_CLASSID =
            "UPDATE " + CS_TYPE_TABLE + " SET class_id = ? WHERE name = ?";                         // NOI18N

    private static final String STMT_UPDATE_CSATTR_FK =
            "UPDATE " + CS_ATTR_TABLE + " SET foreign_key_references_to = ? WHERE id = ?";          // NOI18N

    private static final String STMT_CLEAR_ATTR_MAP_TMP_TABLE =
            "DELETE FROM " + ATTR_MAP_TEMP_TABLE;                                                   // NOI18N

    private static final String STMT_CLEAR_ATTR_STRING_TMP_TABLE =
            "DELETE FROM " + ATTR_STRING_TEMP_TABLE;                                                // NOI18N

    private static final String NULL_STRING = "NULL";                                               // NOI18N
    //J+

    //~ Instance fields --------------------------------------------------------

    private final transient Properties props;
    private transient Connection con;

    private transient PreparedStatement stmtInsertAttrMap;
    private transient PreparedStatement stmtInsertAttrString;
    private transient PreparedStatement stmtCreateTmpAttrMap;
    private transient PreparedStatement stmtCreateTmpAttrString;
    private transient PreparedStatement stmtDropTmpAttrMap;
    private transient PreparedStatement stmtDropTmpAttrString;
    private transient PreparedStatement stmtDeleteIndexesAttrMap;
    private transient PreparedStatement stmtDeleteIndexesAttrString;
    private transient PreparedStatement stmtCopyToAttrMap;
    private transient PreparedStatement stmtCopyToAttrString;
    private transient PreparedStatement stmtClassIdByName;
    private transient PreparedStatement stmtUpdateTypeClassId;
    private transient PreparedStatement stmtUpdateCsAttrFK;
    private transient PreparedStatement stmtClearAttrMapTmpTable;
    private transient PreparedStatement stmtClearAttrStringTmpTable;

    private transient boolean closed;
    private transient boolean canceled;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of MetaBackend.
     *
     * @param  runtimeProps  DOCUMENT ME!
     */
    public MetaBackend(final Properties runtimeProps) {
        this.props = runtimeProps;
        closed = true;
        canceled = false;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * TODO: test
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Override
    public void close() throws Exception {
        if (closed) {
            return;
        }
        try {
            dropTmpTables();
            for (final Field f : getClass().getDeclaredFields()) {
                if (Statement.class.isAssignableFrom(f.getType()) && f.getName().startsWith("stmt")) {         // NOI18N
                    final Method m = PreparedStatement.class.getMethod("close", new Class[0]);                 // NOI18N
                    m.invoke(f.get(this), new Object[0]);
                }
            }
            con.close();
        } catch (final SQLException ex) {
            LOG.warn("could not close connection", ex);                                                        // NOI18N
            throw ex;
        } finally {
            for (final Field f : getClass().getDeclaredFields()) {
                if (PreparedStatement.class.isAssignableFrom(f.getType()) && f.getName().startsWith("stmt")) { // NOI18N
                    f.set(this, null);
                }
            }
            con = null;
            closed = true;
        }
    }

    /**
     * Finds the cs_class id for a given cs_class name.
     *
     * @param   name  the name of the cs_class
     *
     * @return  -1 if the id could not be found or an error occurs, the id otherwise
     */
    private int getClassIdByName(final String name) {
        ResultSet set = null;
        try {
            stmtClassIdByName.setString(1, name);
            set = stmtClassIdByName.executeQuery();
            if (set.next()) {
                return set.getInt(1);
            }
        } catch (final SQLException e) {
            LOG.error("could not retrieve class id", e);      // NOI18N
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (final SQLException e) {
                    LOG.warn("could not close resultset", e); // NOI18N
                }
            }
        }
        return -1;
    }

    /**
     * Finds the cids class corresponding to the given type and updates the reference accordingly.
     *
     * @param   type  type to be updated
     *
     * @throws  SQLException  if any error occurs during execution
     */
    @Override
    public void adjustTypeClassId(final Type type) throws SQLException {
        if (closed) {
            init();
        }
        if ((type != null) && type.isComplexType()) {
            final int id = getClassIdByName(type.getName());
            try {
                if (id > -1) {
                    stmtUpdateTypeClassId.setInt(1, id);
                    stmtUpdateTypeClassId.setString(2, type.getName());
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("excuting update: " + stmtUpdateTypeClassId);             // NOI18N
                    }
                    stmtUpdateTypeClassId.executeUpdate();
                } else {
                    throw new SQLException("could not find an id for given type: " + type); // NOI18N
                }
            } catch (final SQLException ex) {
                LOG.error("could not adjust type for: " + type.getName(), ex);              // NOI18N
                throw ex;
            }
        }
    }

    /**
     * Finds the cids class corresponding to the given attr and updates the fk reference accordingly.
     *
     * @param   attr  attr to be updated
     *
     * @throws  SQLException  if any error occurs during execution
     */
    @Override
    public void adjustAttrForeignKey(final Attribute attr) throws SQLException {
        if (closed) {
            init();
        }
        if (attr != null) {
            final int id = getClassIdByName(attr.getType().getName());
            try {
                if (id > -1) {
                    stmtUpdateCsAttrFK.setInt(1, id);
                    stmtUpdateCsAttrFK.setInt(2, attr.getId());
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("excuting update: " + stmtUpdateCsAttrFK);                // NOI18N
                    }
                    stmtUpdateCsAttrFK.executeUpdate();
                } else {
                    throw new SQLException("could not find an id for given attr: " + attr); // NOI18N
                }
            } catch (final SQLException ex) {
                LOG.error("could not adjust attr for " + attr, ex);                         // NOI18N
                throw ex;
            }
        }
    }

    /**
     * Refreshes the index of the given {@link CidsClass} by creating a temporary table with the new indexes, then
     * deleting the old ones and finally copying the temporary table to the index table. If an indexed field is a
     * foreign key and no value is set -1 will be indexed instead of the attribut object id. If the indexed field is not
     * a foreign key and no value is set the {@link java.lang.String} "NULL" will be indexed instead of the fields
     * value.
     *
     * <p>This implemenetation makes use of the {@link ProgressListener} to propagate progress state.</p>
     *
     * @param   cidsClass  the class that shall be reindexed
     *
     * @throws  SQLException  if any error occurs during indexing
     */
    @Override
    public void refreshIndex(final CidsClass cidsClass) throws SQLException {
        final long startTime = System.currentTimeMillis();
        if (LOG.isInfoEnabled()) {
            LOG.info("start refreshIndex for class: " + cidsClass);                                                  // NOI18N
        }
        fireStateChanged(
            new ProgressState(
                NbBundle.getMessage(MetaBackend.class, "MetaBackend.progress.prepareIndexing", cidsClass.getName()), // NOI18N
                0));
        if (closed) {
            init();
        }
        try {
            int counter = 0;
            final Iterator<Attribute> it = cidsClass.getAttributes().iterator();
            final String tableName = cidsClass.getTableName().toLowerCase().trim();
            final String classPrimaryKeyField = cidsClass.getPrimaryKeyField().toLowerCase().trim();
            final String selIDAndNameFromTbl = "SELECT " + classPrimaryKeyField + ", {0} FROM " + tableName;         // NOI18N
            while (it.hasNext()) {
                if (canceled) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("canceled");                                                                       // NOI18N
                    }
                    canceled = false;
                    return;
                }
                final Attribute attr = it.next();
                // we won't do anything if the attribute shall not be indexed
                if (!attr.isIndexed()) {
                    continue;
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("reindexing attribute: " + attr.getName());     // NOI18N
                }
                fireStateChanged(
                    new ProgressState(
                        NbBundle.getMessage(
                            MetaBackend.class,
                            "MetaBackend.progress.createIndexForAttrOfClass", // NOI18N
                            attr.getName(),
                            cidsClass.getName()),
                        0));
                final String fieldName = attr.getFieldName();
                final String query = MessageFormat.format(selIDAndNameFromTbl, fieldName);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("executing query: " + query);                   // NOI18N
                }
                Statement stmtSelIDAndNameFromTbl = null;
                ResultSet rs = null;
                try {
                    stmtSelIDAndNameFromTbl = con.createStatement(
                            ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    rs = stmtSelIDAndNameFromTbl.executeQuery(query);
                    // finding out about the rowcount
                    rs.last();
                    final int rowcount = rs.getRow();
                    rs.beforeFirst();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("fetched " + rowcount + " rows");                                   // NOI18N
                    }
                    fireStateChanged(
                        new ProgressState(
                            NbBundle.getMessage(
                                MetaBackend.class,
                                "MetaBackend.progress.createIndexForAttrOfClass",                     // NOI18N
                                attr.getName(),
                                cidsClass.getName()),
                            rowcount));
                    counter = 0;
                    while (rs.next()) {
                        if (canceled) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("canceled");                                                // NOI18N
                            }
                            canceled = false;
                            return;
                        }
                        final int objectId = rs.getInt(1);
                        if (attr.isForeignKey()) {
                            final CidsClass attrClass = attr.getType().getCidsClass();
                            final String attrTabName = attrClass.getTableName();
                            final String attrPKF = attrClass.getPrimaryKeyField();
                            final String attrFieldName = attr.getFieldName();
                            final String selectObjectId = "SELECT " + tableName + "." + attrFieldName // NOI18N
                                        + " FROM " + tableName + ", " + attrTabName                   // NOI18N
                                        + " WHERE " + tableName + "." + classPrimaryKeyField + " = "
                                        + objectId                                                    // NOI18N
                                        + " AND " + tableName + "." + attrFieldName + " = " + attrTabName + "."
                                        + attrPKF;                                                    // NOI18N
                            Statement stmt = null;
                            ResultSet rsObjectID = null;
                            try {
                                stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                rsObjectID = stmt.executeQuery(selectObjectId);
                                final int attrObjectId;
                                // there will be one single result (the attribute object id) or nothing
                                if (rsObjectID.next()) {
                                    attrObjectId = rsObjectID.getInt(attrFieldName);
                                } else {
                                    // if no object is set for this object -1 will be indexed
                                    attrObjectId = -1;
                                }
                                addInsertIntoAllAttrMapping(
                                    cidsClass.getId(),
                                    objectId,
                                    attrClass.getId(),
                                    attrObjectId);
                            } finally {
                                DatabaseConnection.closeResultSet(rsObjectID);
                                DatabaseConnection.closeStatement(stmt);
                            }
                        } else {
                            // if attr is not foreign key we put the content to the string index
                            String refVal = rs.getString(2);
                            if (refVal != null) {
                                refVal = refVal.trim().replaceAll("'", "''"); // NOI18N
                            }
                            addInsertIntoAttrString(cidsClass.getId(), attr.getId(), objectId, refVal);
                        }
                        if (++counter >= 25000) {
                            executeInsertBatch();
                            counter = 0;
                        }
                        fireProgressed(1);
                    }
                } finally {
                    DatabaseConnection.closeResultSet(rs);
                    DatabaseConnection.closeStatement(stmtSelIDAndNameFromTbl);
                }
                fireStateChanged(
                    new ProgressState(
                        NbBundle.getMessage(
                            MetaBackend.class,
                            "MetaBackend.progress.createdIndexForAttrOfClass", // NOI18N
                            attr.getName(),
                            cidsClass.getName()),
                        0));
            }
            executeInsertBatch();
            fireStateChanged(
                new ProgressState(
                    NbBundle.getMessage(
                        MetaBackend.class,
                        "MetaBackend.progress.createdIndexForClass",          // NOI18N
                        cidsClass.getName()),
                    0));
            Statement aStmt = null;
            try {
                con.setAutoCommit(false);
                aStmt = con.createStatement();
                fireStateChanged(
                    new ProgressState(
                        NbBundle.getMessage(
                            MetaBackend.class,
                            "MetaBackend.progress.replaceOldIndexOfClass",    // NOI18N
                            cidsClass.getName()),
                        0));
                aStmt.execute("BEGIN");                                       // NOI18N
                deleteIndexes(cidsClass.getId());
                executeRefresh();
                aStmt.execute("COMMIT");                                      // NOI18N
            } catch (final SQLException e) {
                LOG.error("could not refresh index tables", e);               // NOI18N
                aStmt.execute("ROLLBACK");                                    // NOI18N
                throw e;
            } finally {
                con.setAutoCommit(true);
                DatabaseConnection.closeStatement(aStmt);
            }
        } finally {
            clearTmpTables();
        }
        final long duration = (System.currentTimeMillis() - startTime) / 1000;
        if (LOG.isInfoEnabled()) {
            LOG.info("refresh index for class '" + cidsClass + "' completed in " + duration + " seconds"); // NOI18N
        }
        fireStateChanged(
            new ProgressState(
                NbBundle.getMessage(
                    MetaBackend.class,
                    "MetaBackend.progress.refreshedIndexForClassInSec",       // NOI18N
                    cidsClass.getName(),
                    duration),
                0));
    }

    /**
     * initialises the connection and the prepared statements sets the object state to open (closed == false).
     *
     * @throws  SQLException  if any error occurs during statement preparation
     */
    private void init() throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("init");                           // NOI18N
        }
        try {
            con = DatabaseConnection.getConnection(props, 10);
            stmtCreateTmpAttrMap = con.prepareStatement(STMT_PREP_TMP_ATTR_MAP);
            stmtCreateTmpAttrString = con.prepareStatement(STMT_PREP_TMP_ATTR_STRING);
            stmtInsertAttrString = con.prepareStatement(STMT_INSERT_ATTR_STRING);
            stmtInsertAttrMap = con.prepareStatement(STMT_INSERT_ATTR_MAP);
            stmtDropTmpAttrMap = con.prepareStatement(STMT_DROP_TMP_ATTR_MAP);
            stmtDropTmpAttrString = con.prepareStatement(STMT_DROP_TMP_ATTR_STRING);
            stmtDeleteIndexesAttrMap = con.prepareStatement(STMT_DELETE_INDEXES_ATTR_MAP);
            stmtDeleteIndexesAttrString = con.prepareStatement(STMT_DELETE_INDEXES_ATTR_STRING);
            stmtCopyToAttrMap = con.prepareStatement(STMT_COPY_TO_ATTR_MAP);
            stmtCopyToAttrString = con.prepareStatement(STMT_COPY_TO_ATTR_STRING);
            stmtClassIdByName = con.prepareStatement(STMT_CLASSID_BY_NAME);
            stmtUpdateTypeClassId = con.prepareStatement(STMT_UPDATE_TYPE_CLASSID);
            stmtUpdateCsAttrFK = con.prepareStatement(STMT_UPDATE_CSATTR_FK);
            stmtClearAttrMapTmpTable = con.prepareStatement(STMT_CLEAR_ATTR_MAP_TMP_TABLE);
            stmtClearAttrStringTmpTable = con.prepareStatement(STMT_CLEAR_ATTR_STRING_TMP_TABLE);
            createTmpTables();
            closed = false;
        } catch (final SQLException e) {
            LOG.error("could not init meta backend", e); // NOI18N
            throw e;
        }
    }

    /**
     * creates necessary temporary tables using prepared statements.
     *
     * @throws  SQLException  if any error occurs during execution
     */
    private void createTmpTables() throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("create tmp tables");              // NOI18N
        }
        try {
            stmtCreateTmpAttrMap.execute();
            stmtCreateTmpAttrString.execute();
        } catch (final SQLException e) {
            LOG.error("could not create tmp tables", e); // NOI18N
            throw e;
        }
    }

    /**
     * Deletes any entry in the temporary tables.
     *
     * @throws  SQLException  if any error occurs during execution
     */
    private void clearTmpTables() throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("clear tmp tables"); // NOI18N
        }
        stmtClearAttrMapTmpTable.execute();
        stmtClearAttrStringTmpTable.execute();
    }

    /**
     * drops the previously created temporary tables using prepared statements.
     *
     * @throws  SQLException  if any error occurs during execution
     */
    private void dropTmpTables() throws SQLException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("drop tmp tables");              // NOI18N
        }
        try {
            stmtDropTmpAttrMap.execute();
            stmtDropTmpAttrString.execute();
        } catch (final SQLException e) {
            LOG.error("could not drop tmp tables", e); // NOI18N
            throw e;
        }
    }

    /**
     * deletes indexes from attr mapping and attr string for the given classid. does not care about transactions.
     *
     * @param   classId  indexes of the class with that id shall be removed
     *
     * @throws  SQLException  if something went wrong during deletion
     */
    private void deleteIndexes(final int classId) throws SQLException {
        stmtDeleteIndexesAttrMap.setInt(1, classId);
        int rowCount = stmtDeleteIndexesAttrMap.executeUpdate();
        if (LOG.isDebugEnabled()) {
            LOG.debug("deleted " + rowCount + " rows from attr mapping for class id: " + classId); // NOI18N
        }
        stmtDeleteIndexesAttrString.setInt(1, classId);
        rowCount = stmtDeleteIndexesAttrString.executeUpdate();
        if (LOG.isDebugEnabled()) {
            LOG.debug("deleted " + rowCount + " rows from attr string for class id: " + classId);  // NOI18N
        }
    }

    /**
     * adds an insert statement to the prepared statement that updates cs_all_attr_mapping.
     *
     * @param   classId       the class id of the class to index
     * @param   objectId      the object id of the object corresponding to the class
     * @param   attrId        the attr of the class to be indexed
     * @param   attrObjectId  the object id of the row corresponding to the object
     *
     * @throws  SQLException  if the statement cannot be batched
     */
    private void addInsertIntoAllAttrMapping(
            final int classId,
            final int objectId,
            final int attrId,
            final int attrObjectId) throws SQLException {
        try {
            stmtInsertAttrMap.setInt(1, classId);
            stmtInsertAttrMap.setInt(2, objectId);
            stmtInsertAttrMap.setInt(3, attrId);
            stmtInsertAttrMap.setInt(4, attrObjectId);
            stmtInsertAttrMap.addBatch();
        } catch (final SQLException ex) {
            LOG.error("Could not add batch to allAttrMappingStatement: " + stmtInsertAttrMap.toString(), ex); // NOI18N
            throw ex;
        }
    }

    /**
     * adds an insert statement to the prepared statement that updates cs_attr_string.
     *
     * @param   classId      the class id of the class to index
     * @param   attrId       the attr of the class to be indexed
     * @param   objectId     the object id of the object corresponding to the class
     * @param   stringvalue  the string to be indexed
     *
     * @throws  SQLException  if the statement cannot be batched
     */
    private void addInsertIntoAttrString(
            final int classId,
            final int attrId,
            final int objectId,
            final String stringvalue) throws SQLException {
        try {
            stmtInsertAttrString.setInt(1, classId);
            stmtInsertAttrString.setInt(2, attrId);
            stmtInsertAttrString.setInt(3, objectId);
            stmtInsertAttrString.setString(4, (stringvalue == null) ? NULL_STRING : stringvalue);
            stmtInsertAttrString.addBatch();
        } catch (final SQLException ex) {
            LOG.error("Could not add Batch to attrStringStatement: " + stmtInsertAttrString.toString(), ex); // NOI18N
            throw ex;
        }
    }

    /**
     * executes the prepared statement batches.
     *
     * @throws  SQLException  if the batch could not be executed
     */
    private void executeInsertBatch() throws SQLException {
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("execute insert batch");              // NOI18N
            }
            stmtInsertAttrString.executeBatch();
            stmtInsertAttrString.clearBatch();
            stmtInsertAttrString.clearWarnings();
            stmtInsertAttrMap.executeBatch();
            stmtInsertAttrMap.clearBatch();
            stmtInsertAttrMap.clearWarnings();
        } catch (final SQLException ex) {
            LOG.error("could not execute batch", ex);           // NOI18N
            LOG.error("next exception", ex.getNextException()); // NOI18N
            throw ex;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    private void executeRefresh() throws SQLException {
        int rowCount = stmtCopyToAttrMap.executeUpdate();
        if (LOG.isDebugEnabled()) {
            LOG.debug("copied " + rowCount + " rows from '" + ATTR_MAP_TEMP_TABLE + "' to '" + CS_ATTR_MAP_TABLE + "'"); // NOI18N
        }
        rowCount = stmtCopyToAttrString.executeUpdate();
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                "copied "
                        + rowCount
                        + " rows from '"
                        + ATTR_STRING_TEMP_TABLE
                        + "' to '"
                        + CS_ATTR_STRING_TABLE
                        + "'");                                                                                          // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void cancel() {
        canceled = true;
    }
}
