/*
 * MetaBackend.java
 *
 * Created on 23. November 2007, 11:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.cids.jpa.backend.service.impl;

import de.cismet.cids.jpa.backend.service.MetaService;
import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.Type;
import de.cismet.cids.util.AbstractProgressObservable;
import de.cismet.cids.util.ProgressListener.ProgressState;
import de.cismet.diff.db.DatabaseConnection;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
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
import org.apache.log4j.Logger;

/**
 *
 * @author cschmidt
 */
public class MetaBackend extends AbstractProgressObservable implements
        MetaService
{
    // TODO: remove all printstacktraces, set everything final what is possible
    // TODO: clean this messy class -.-

    private static final transient Logger LOG = Logger.getLogger(
            MetaBackend.class);

    private static final String ATTR_MAP_TEMP_TABLE =
            "cs_all_attr_mapping_temp";
    private static final String ATTR_STRING_TEMP_TABLE =
            "cs_attr_string_temp";

    private static final String STMT_PREP_TMP_ATTR_MAP =
            "CREATE TEMPORARY TABLE " + ATTR_MAP_TEMP_TABLE + " (" +
                    "class_id INTEGER, " +
                    "object_id INTEGER, " +
                    "attr_class_id INTEGER, " +
                    "attr_object_id INTEGER)";

    private static final String STMT_PREP_TMP_ATTR_STRING =
            "CREATE TEMP TABLE " + ATTR_STRING_TEMP_TABLE + " (" +
                    "class_id INTEGER, " +
                    "attr_id INTEGER, " +
                    "object_id INTEGER, " +
                    "string_val text )";

    private static final String STMT_DROP_TMP_ATTR_MAP =
            "DROP TABLE " + ATTR_MAP_TEMP_TABLE;

    private static final String STMT_DROP_TMP_ATTR_STRING =
            "DROP TABLE " + ATTR_STRING_TEMP_TABLE;

    private static final String STMT_INSERT_ATTR_MAP =
            "INSERT INTO " + ATTR_MAP_TEMP_TABLE + " ( " +
                    "class_id, " +
                    "object_id, " +
                    "attr_class_id, " +
                    "attr_object_id) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String STMT_INSERT_ATTR_STRING =
            "INSERT INTO " + ATTR_STRING_TEMP_TABLE + " (" +
                    "class_id, " +
                    "attr_id, " +
                    "object_id, " +
                    "string_val) " +
                    "VALUES (?, ?, ?, ?)";

    private static final String STMT_COPY_TO_ATTR_MAP =
            "INSERT INTO " + CS_ATTR_MAP_TABLE + " (" +
                    "class_id, " +
                    "object_id, " +
                    "attr_class_id, " +
                    "attr_object_id) " +
                    "(SELECT " +
                            "class_id, " +
                            "object_id, " +
                            "attr_class_id, " +
                            "attr_object_id " +
                            "FROM " + ATTR_MAP_TEMP_TABLE + ")";

    private static final String STMT_COPY_TO_ATTR_STRING =
            "INSERT INTO " + CS_ATTR_STRING_TABLE + " (" +
                    "class_id, " +
                    "attr_id, " +
                    "object_id, " +
                    "string_val)" +
                    "(SELECT " +
                            "class_id, " +
                            "attr_id, " +
                            "object_id, " +
                            "string_val " +
                            "FROM " + ATTR_STRING_TEMP_TABLE + ")";

    private static final String STMT_DELETE_INDEXES_ATTR_MAP =
            "DELETE FROM " + CS_ATTR_MAP_TABLE + " WHERE class_id = ?";

    private static final String STMT_DELETE_INDEXES_ATTR_STRING =
            "DELETE FROM " + CS_ATTR_STRING_TABLE + " WHERE class_id = ?";

    private static final String STMT_CLASSID_BY_NAME =
            "SELECT id FROM " + CS_CLASS_TABLE + " WHERE name = ?";

    private static final String STMT_UPDATE_TYPE_CLASSID =
            "UPDATE " + CS_TYPE_TABLE + " SET class_id = ? WHERE name = ?";

    private static final String STMT_UPDATE_CSATTR_FK =
            "UPDATE " + CS_ATTR_TABLE + " SET foreign_key_references_to = ? " +
                    "WHERE id = ?";

    private static final String STMT_CLEAR_ATTR_MAP_TMP_TABLE =
            "DELETE FROM " + ATTR_MAP_TEMP_TABLE;

    private static final String STMT_CLEAR_ATTR_STRING_TMP_TABLE =
            "DELETE FROM " + ATTR_STRING_TEMP_TABLE;

    private final Properties props;
    private Connection con;

    private PreparedStatement stmtInsertAttrMap;
    private PreparedStatement stmtInsertAttrString;
    private PreparedStatement stmtCreateTmpAttrMap;
    private PreparedStatement stmtCreateTmpAttrString;
    private PreparedStatement stmtDropTmpAttrMap;
    private PreparedStatement stmtDropTmpAttrString;
    private PreparedStatement stmtDeleteIndexesAttrMap;
    private PreparedStatement stmtDeleteIndexesAttrString;
    private PreparedStatement stmtCopyToAttrMap;
    private PreparedStatement stmtCopyToAttrString;
    private PreparedStatement stmtClassIdByName;
    private PreparedStatement stmtUpdateTypeClassId;
    private PreparedStatement stmtUpdateCsAttrFK;
    private PreparedStatement stmtClearAttrMapTmpTable;
    private PreparedStatement stmtClearAttrStringTmpTable;

    private boolean closed;
    private boolean canceled;

    /** Creates a new instance of MetaBackend */
    public MetaBackend(final Properties runtimeProps)
    {
        this.props = runtimeProps;
        closed = true;
        canceled = false;
    }

    // TODO: test
    public void close() throws Exception
    {
        if(closed)
            return;
        try
        {
            dropTmpTables();
            for(final Field f : getClass().getDeclaredFields())
            {
                if(Statement.class.isAssignableFrom(f.getType()) &&
                        f.getName().startsWith("stmt"))
                {
                    final Method m = PreparedStatement.class.getMethod("close",
                            new Class[0]);
                    m.invoke(f.get(this), new Object[0]);
                }
            }
            con.close();
        }catch(final SQLException ex)
        {
            LOG.warn("could not close connection", ex);
            throw ex;
        }finally
        {
            for(final Field f : getClass().getDeclaredFields())
            {
                if(PreparedStatement.class.isAssignableFrom(f.getType()) &&
                        f.getName().startsWith("stmt"))
                {
                    f.set(this, null);
                }
            }
            con = null;
            closed = true;
        }
    }

    /**
     * Finds the cs_class id for a given cs_class name
     *
     * @param name the name of the cs_class
     * @return -1 if the id could not be found or an error occurs, the id
     *         otherwise
     */
    private int getClassIdByName(final String name)
    {
        ResultSet set = null;
        try
        {
            stmtClassIdByName.setString(1, name);
            set = stmtClassIdByName.executeQuery();
            if(set.next())
                return set.getInt(1);
        }catch(final SQLException e)
        {
            LOG.error("could not retrieve class id", e);
        }finally
        {
            if(set != null)
            {
                try
                {
                    set.close();
                }catch(final SQLException e)
                {
                    LOG.warn("could not close resultset", e);
                }
            }
        }
        return -1;
    }

    /**
     * Finds the cids class corresponding to the given type and updates the
     * reference accordingly
     *
     * @param type type to be updated
     * @throws java.sql.SQLException if any error occurs during execution
     */
    public void adjustTypeClassId(final Type type) throws SQLException
    {
        if(closed)
            init();
        if(type != null && type.isComplexType())
        {
            final int id = getClassIdByName(type.getName());
            try
            {
                if(id != -1)
                {
                    stmtUpdateTypeClassId.setInt(1, id);
                    stmtUpdateTypeClassId.setString(2, type.getName());
                    if(LOG.isDebugEnabled())
                        LOG.debug("excuting update: " + stmtUpdateTypeClassId);
                    stmtUpdateTypeClassId.executeUpdate();
                }else
                    throw new SQLException("could not find an id for given " +
                            "type: " + type);
            }catch(final SQLException ex)
            {
                LOG.error("could not adjust type for: " + type.getName(), ex);
                throw ex;
            }
        }
    }

    /**
     * Finds the cids class corresponding to the given attr and updates the fk
     * reference accordingly
     *
     * @param attr attr to be updated
     * @throws java.sql.SQLException if any error occurs during execution
     */
    public void adjustAttrForeignKey(final Attribute attr) throws SQLException
    {
        if(closed)
            init();
        if(attr != null)
        {
            final int id = getClassIdByName(attr.getType().getName());
            try
            {
                if(id != -1)
                {
                    stmtUpdateCsAttrFK.setInt(1, id);
                    stmtUpdateCsAttrFK.setInt(2, attr.getId());
                    if(LOG.isDebugEnabled())
                        LOG.debug("excuting update: " + stmtUpdateCsAttrFK);
                    stmtUpdateCsAttrFK.executeUpdate();
                }else
                    throw new SQLException("could not find an id for given " +
                            "attr: " + attr);
            }catch(final SQLException ex)
            {
                LOG.error("could not adjust attr for " + attr, ex);
                throw ex;
            }
        }
    }

    public void refreshIndex(final CidsClass cidsClass) throws SQLException
    {
        final long startTime = System.currentTimeMillis();
        if(LOG.isInfoEnabled())
            LOG.info("start refreshIndex for class: " + cidsClass);
        fireStateChanged(new ProgressState("Indizieren von " + cidsClass.
                getName() + " vorbereiten", 0));
        if(closed)
            init();
        try
        {
            int counter = 0;
            final Iterator<Attribute> it = cidsClass.getAttributes().iterator();
            final String tableName = cidsClass.getTableName().toLowerCase().
                    trim();
            final String classPrimaryKeyField = cidsClass.getPrimaryKeyField().
                    toLowerCase().
                    trim();
            final String selIDAndNameFromTbl =
                    "SELECT " + classPrimaryKeyField + ", {0} FROM " +
                    tableName;
            while(it.hasNext())
            {
                if(canceled)
                {
                    if(LOG.isDebugEnabled())
                        LOG.debug("canceled");
                    canceled = false;
                    return;
                }
                final Attribute attr = it.next();
                // we won't do anything if the attribute shall not be indexed
                if(!attr.isIndexed())
                    continue;
                if(LOG.isDebugEnabled())
                    LOG.debug("reindexing attribute: " + attr.getName());
                fireStateChanged(new ProgressState("Index erstellen für " +
                        "Attribut '" + attr.getName() + "' der Klasse '" +
                        cidsClass.getName() + "'", 0));
                final String fieldName = attr.getFieldName();
                final String query = MessageFormat.format(selIDAndNameFromTbl,
                        fieldName);
                if(LOG.isDebugEnabled())
                    LOG.debug("executing query: " + query);
                Statement stmtSelIDAndNameFromTbl = null;
                ResultSet rs = null;
                try
                {
                    stmtSelIDAndNameFromTbl = con.createStatement(ResultSet.
                            TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = stmtSelIDAndNameFromTbl.executeQuery(query);
                    // finding out about the rowcount
                    rs.last();
                    final int rowcount = rs.getRow();
                    rs.beforeFirst();
                    if(LOG.isDebugEnabled())
                        LOG.debug("fetched " + rowcount + " rows");
                    fireStateChanged(new ProgressState("Index erstellen für " +
                            "Attribut '" + attr.getName() + "' der Klasse '" +
                            cidsClass.getName() + "'", rowcount));
                    counter = 0;
                    while(rs.next())
                    {
                        if(canceled)
                        {
                            if(LOG.isDebugEnabled())
                                LOG.debug("canceled");
                            canceled = false;
                            return;
                        }
                        final int objectId = rs.getInt(1);
                        // if attr is not foreign key we put the content to the
                        // string index
                        if(!attr.isForeignKey())
                        {
                            final String refVal = rs.getString(2);
                            if(refVal != null && refVal.trim().length() > 1)
                            {
                                final String s = refVal.trim().replaceAll("'",
                                        "''");
                                addInsertIntoAttrString(cidsClass.getId(), attr.
                                        getId(), objectId, s);
                            }else
                            {
                                if(LOG.isInfoEnabled())
                                    LOG.info("will not insert values in cs_at" +
                                            "tr_string, string to be indexed " +
                                            "== null: classId: " + cidsClass.
                                            getId() + ", attrId: " + attr.getId(
                                            ) + ", objectId: " + objectId);
                            }
                        }else
                        {
                            final CidsClass attrClass = attr.getType().
                                    getCidsClass();
                            final String attrTabName = attrClass.getTableName();
                            final String attrPKF = attrClass.getPrimaryKeyField(
                                    );
                            final String attrFieldName = attr.getFieldName();
                            final String selectObjectId =
                                    "SELECT " + tableName + "." +
                                    attrFieldName + " FROM " +
                                    tableName + ", " + attrTabName + " WHERE " +
                                    tableName + "." + classPrimaryKeyField +
                                    " = " + objectId + " AND " +
                                    tableName + "." + attrFieldName + " = " +
                                    attrTabName + "." + attrPKF;
                            Statement stmt = null;
                            ResultSet rsObjectID = null;
                            try
                            {
                                stmt = con.createStatement(ResultSet.
                                        TYPE_SCROLL_SENSITIVE, ResultSet.
                                        CONCUR_READ_ONLY);
                                rsObjectID = stmt.executeQuery(selectObjectId);
                                while(rsObjectID.next())
                                {
                                    final int attrObjectId = rsObjectID.getInt(
                                            attrFieldName);
                                    addInsertIntoAllAttrMapping(cidsClass.getId(
                                            ), objectId, attrClass.getId(),
                                            attrObjectId);
                                }
                            }finally
                            {
                                if(rsObjectID != null)
                                {
                                    try
                                    {
                                        rsObjectID.close();
                                    }catch(final SQLException e)
                                    {
                                        LOG.warn("could not close resultset",
                                                e);
                                    }
                                }
                                if(stmt != null)
                                {
                                    try
                                    {
                                        stmt.close();
                                    }catch(final SQLException e)
                                    {
                                        LOG.warn("could not close stmt", e);
                                    }
                                }
                            }
                        }
                        if(++counter >= 25000)
                        {
                            executeInsertBatch();
                            counter = 0;
                        }
                        fireProgressed(1);
                    }
                }finally
                {
                    if(rs != null)
                    {
                        try
                        {
                            rs.close();
                        }catch(final SQLException e)
                        {
                            LOG.warn("could not close resultset", e);
                        }
                    }
                    if(stmtSelIDAndNameFromTbl != null)
                    {
                        try
                        {
                            stmtSelIDAndNameFromTbl.close();
                        }catch(final SQLException e)
                        {
                            LOG.warn("could not close stmt", e);
                        }
                    }
                }
                fireStateChanged(new ProgressState("Index erstellt für Attrib" +
                        "ut '" + attr.getName() + "' der Klasse '" + cidsClass.
                        getName() + "'", 0));
            }
            executeInsertBatch();
            fireStateChanged(new ProgressState("Index erstellt für Klasse '" +
                    cidsClass.getName() + "'", 0));
            Statement aStmt = null;
            try
            {
                con.setAutoCommit(false);
                aStmt = con.createStatement();
                fireStateChanged(new ProgressState("Ersetzen des alten Indexe" +
                        "s für Klasse '" + cidsClass.getName() + "'", 0));
                aStmt.execute("BEGIN");
                deleteIndexes(cidsClass.getId());
                executeRefresh();
                aStmt.execute("COMMIT");
            }catch(final SQLException e)
            {
                LOG.error("could not refresh index tables", e);
                aStmt.execute("ROLLBACK");
                throw e;
            }finally
            {
                con.setAutoCommit(true);
                if(aStmt != null)
                    aStmt.close();
            }
        }finally
        {
            clearTmpTables();
        }
        final long duration = (System.currentTimeMillis() - startTime) / 1000;
        if(LOG.isInfoEnabled())
            LOG.info("refresh index for class '" + cidsClass +
                    "' completed in " + duration + " seconds");
        fireStateChanged(new ProgressState("Index für Klasse '" + cidsClass.
                getName() + " erneuert in " + duration + " Sekunden", 0));
    }

    /**
     * initialises the connection and the prepared statements
     * sets the object state to open (closed == false)
     *
     * @throws java.sql.SQLException if any error occurs during statement
     *          preparation
     */
    private void init() throws SQLException
    {
        if(LOG.isDebugEnabled())
            LOG.debug("init");
        try
        {
            con = DatabaseConnection.getConnection(props, 10);
            stmtCreateTmpAttrMap = con.prepareStatement(STMT_PREP_TMP_ATTR_MAP);
            stmtCreateTmpAttrString = con.prepareStatement(
                    STMT_PREP_TMP_ATTR_STRING);
            stmtInsertAttrString = con.prepareStatement(
                    STMT_INSERT_ATTR_STRING);
            stmtInsertAttrMap = con.prepareStatement(STMT_INSERT_ATTR_MAP);
            stmtDropTmpAttrMap = con.prepareStatement(STMT_DROP_TMP_ATTR_MAP);
            stmtDropTmpAttrString = con.prepareStatement(
                    STMT_DROP_TMP_ATTR_STRING);
            stmtDeleteIndexesAttrMap = con.prepareStatement(
                    STMT_DELETE_INDEXES_ATTR_MAP);
            stmtDeleteIndexesAttrString = con.prepareStatement(
                    STMT_DELETE_INDEXES_ATTR_STRING);
            stmtCopyToAttrMap = con.prepareStatement(STMT_COPY_TO_ATTR_MAP);
            stmtCopyToAttrString = con.prepareStatement(
                    STMT_COPY_TO_ATTR_STRING);
            stmtClassIdByName = con.prepareStatement(STMT_CLASSID_BY_NAME);
            stmtUpdateTypeClassId = con.prepareStatement(
                    STMT_UPDATE_TYPE_CLASSID);
            stmtUpdateCsAttrFK = con.prepareStatement(STMT_UPDATE_CSATTR_FK);
            stmtClearAttrMapTmpTable = con.prepareStatement(
                    STMT_CLEAR_ATTR_MAP_TMP_TABLE);
            stmtClearAttrStringTmpTable = con.prepareStatement(
                    STMT_CLEAR_ATTR_STRING_TMP_TABLE);
            createTmpTables();
            closed = false;
        }catch(final SQLException e)
        {
            LOG.error("could not init meta backend", e);
            throw e;
        }
    }

    /**
     * creates necessary temporary tables using prepared statements
     *
     * @throws java.sql.SQLException if any error occurs during execution
     */
    private void createTmpTables() throws SQLException
    {
        if(LOG.isDebugEnabled())
            LOG.debug("create tmp tables");
        try
        {
            stmtCreateTmpAttrMap.execute();
            stmtCreateTmpAttrString.execute();
        }catch(final SQLException e)
        {
            LOG.error("could not create tmp tables", e);
            throw e;
        }
    }

    /**
     * Deletes any entry in the temporary tables
     *
     * @throws java.sql.SQLException if any error occurs during execution
     */
    private void clearTmpTables() throws SQLException
    {
        if(LOG.isDebugEnabled())
            LOG.debug("clear tmp tables");
        stmtClearAttrMapTmpTable.execute();
        stmtClearAttrStringTmpTable.execute();
    }

    /**
     * drops the previously created temporary tables using prepared statements
     *
     * @throws java.sql.SQLException if any error occurs during execution
     */
    private void dropTmpTables() throws SQLException
    {
        if(LOG.isDebugEnabled())
            LOG.debug("drop tmp tables");
        try
        {
            stmtDropTmpAttrMap.execute();
            stmtDropTmpAttrString.execute();
        }catch(final SQLException e)
        {
            LOG.error("could not drop tmp tables", e);
            throw e;
        }
    }

    /**
     * deletes indexes from attr mapping and attr string for the given classid.
     * does not care about transactions.
     *
     * @param classId indexes of the class with that id shall be removed
     * @throws java.sql.SQLException if something went wrong during deletion
     */
    private void deleteIndexes(final int classId) throws SQLException
    {
        stmtDeleteIndexesAttrMap.setInt(1, classId);
        int rowCount = stmtDeleteIndexesAttrMap.executeUpdate();
        if(LOG.isDebugEnabled())
            LOG.debug("deleted " + rowCount + " rows from attr mapping for cl" +
                    "ass id: " + classId);
        stmtDeleteIndexesAttrString.setInt(1, classId);
        rowCount = stmtDeleteIndexesAttrString.executeUpdate();
        if(LOG.isDebugEnabled())
            LOG.debug("deleted " + rowCount + " rows from attr string for cla" +
                    "ss id: " + classId);
    }

    /**
     * adds an insert statement to the prepared statement that updates
     * cs_all_attr_mapping
     *
     * @param classId   the class id of the class to index
     * @param objectId  the object id of the object corresponding to the class
     * @param attrId    the attr of the class to be indexed
     * @param attrObjectId  the object id of the row corresponding to the object
     *
     * @throws java.sql.SQLException if the statement cannot be batched
     */
    private void addInsertIntoAllAttrMapping(final int classId, final int
            objectId, final int attrId, final int attrObjectId) throws
            SQLException
    {
        try
        {
            stmtInsertAttrMap.setInt(1, classId);
            stmtInsertAttrMap.setInt(2, objectId);
            stmtInsertAttrMap.setInt(3, attrId);
            stmtInsertAttrMap.setInt(4, attrObjectId);
            stmtInsertAttrMap.addBatch();
        }catch(final SQLException ex)
        {
            LOG.error("Could not add batch to allAttrMappingStatement: " +
                    stmtInsertAttrMap.toString(), ex);
            throw ex;
        }
    }

    /**
     * adds an insert statement to the prepared statement that updates
     * cs_attr_string
     *
     * @param classId   the class id of the class to index
     * @param objectId  the object id of the object corresponding to the class
     * @param attrId    the attr of the class to be indexed
     * @param stringvalue   the string to be indexed
     *
     * @throws java.sql.SQLException if the statement cannot be batched
     */
    private void addInsertIntoAttrString(final int classId, final int attrId,
            final int objectId, final String stringvalue) throws
            SQLException
    {
        try
        {
            stmtInsertAttrString.setInt(1, classId);
            stmtInsertAttrString.setInt(2, attrId);
            stmtInsertAttrString.setInt(3, objectId);
            stmtInsertAttrString.setString(4, stringvalue);
            stmtInsertAttrString.addBatch();
        }catch(final SQLException ex)
        {
            LOG.error("Could not add Batch to attrStringStatement: " + 
                    stmtInsertAttrString.toString(), ex);
            throw ex;
        }
    }

    /**
     * executes the prepared statement batches
     *
     * @throws java.sql.SQLException if the batch could not be executed
     */
    private void executeInsertBatch() throws SQLException
    {
        try
        {
            if(LOG.isDebugEnabled())
                LOG.debug("execute insert batch");
            stmtInsertAttrString.executeBatch();
            stmtInsertAttrString.clearBatch();
            stmtInsertAttrString.clearWarnings();
            stmtInsertAttrMap.executeBatch();
            stmtInsertAttrMap.clearBatch();
            stmtInsertAttrMap.clearWarnings();
        }catch(final SQLException ex)
        {
            LOG.error("could not execute batch", ex);
            LOG.error("next exception", ex.getNextException());
            throw ex;
        }
    }


    private void executeRefresh() throws SQLException
    {
        int rowCount = stmtCopyToAttrMap.executeUpdate();
        if(LOG.isDebugEnabled())
            LOG.debug("copied " + rowCount + " rows from '" +
                    ATTR_MAP_TEMP_TABLE + "' to '" + CS_ATTR_MAP_TABLE + "'");
        rowCount = stmtCopyToAttrString.executeUpdate();
        if(LOG.isDebugEnabled())
            LOG.debug("copied " + rowCount + " rows from '" +
                    ATTR_STRING_TEMP_TABLE + "' to '" + CS_ATTR_STRING_TABLE +
                    "'");
    }

    public void cancel()
    {
        canceled = true;
    }

    public static void main(String[] args)
    {
        final Properties p = new Properties();
        p.put("log4j.appender.Remote", "org.apache.log4j.net.SocketAppender");
        p.put("log4j.appender.Remote.remoteHost", "localhost");
        p.put("log4j.appender.Remote.port", "4445");
        p.put("log4j.appender.Remote.locationInfo", "true");
        p.put("log4j.rootLogger", "ALL,Remote");
        p.put("log4j.logger.org.hibernate", "WARN,Remote");
        p.put("log4j.logger.com.mchange.v2", "WARN,Remote");
        org.apache.log4j.PropertyConfigurator.configure(p);
        try
        {
            final Properties prop = new Properties();
            prop.load(new BufferedInputStream(new FileInputStream(
                    "/Users/mscholl/cvswork6/testauslieferung/" +
                    "cidsDistribution/abf_dev_20090320/runtime.properties")));
            final Backend b = new Backend(prop);
            final CidsClass c = b.getEntity(CidsClass.class, 48);
            b.refreshIndex(c);
        }catch(final Exception e)
        {
            LOG.fatal("error during refresh", e);
        }
    }
}