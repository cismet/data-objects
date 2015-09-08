/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.builder;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;

import de.cismet.diff.container.Statement;
import de.cismet.diff.container.StatementGroup;
import de.cismet.diff.container.Table;
import de.cismet.diff.container.TableColumn;

import de.cismet.diff.db.DatabaseConnection;

import de.cismet.diff.exception.IllegalCodeException;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public class Oracle11gDialect implements ScriptGeneratorDialect {

    //~ Instance fields --------------------------------------------------------

    private final Properties runtime;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Oracle11gDialect object.
     *
     * @param  runtime  DOCUMENT ME!
     */
    public Oracle11gDialect(final Properties runtime) {
        this.runtime = runtime;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isDefaultValueValid(final String column,
            final String typename,
            final Integer precision,
            final Integer scale,
            final String defaultValue) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public boolean sequenceExists(final String seqName) {
        ResultSet rs = null;
        try {
            final String stmt = "SELECT * FROM " + seqName; // NOI18N
            rs = DatabaseConnection.execSQL(runtime, stmt, this.hashCode());

            return rs.next();
        } catch (final SQLException ex) {
            return false;
        } finally {
            DatabaseConnection.closeResultSet(rs);
        }
    }

    @Override
    public boolean isTableEmpty(final String tableName) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public Table findTable(final Table[] tables, final String tableName) {
        for (final Table t : tables) {
            // default behaviour
            if (t.getTableName().equalsIgnoreCase(tableName)) {
                return t;
            }
            final String internalDialect = runtime.getProperty("internalDialect"); // NOI18N
            if ("oracle_11g".equals(internalDialect)) {                            // NOI18N
                final String schema = runtime.getProperty("connection.username");  // NOI18N
                if (t.getTableName().toLowerCase().startsWith(schema.toLowerCase() + ".")) {
                    return t;
                }
            }
        }
        return null;
    }

    @Override
    public StatementGroup createDialectAwareStatementGroup(final StatementGroup group) throws IllegalCodeException {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public StringBuilder appendCreateTableExpression(final StringBuilder targetCreateTable,
            final String attrName,
            final String attrTypeName,
            final boolean optional,
            final Integer precision,
            final Integer scale,
            final String defaultValue) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public void handlePKCreation(final List<Statement> targetStatements,
            final StringBuilder targetCreateTable,
            final String attrName,
            final String tableName) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
                                                                       // Tools | Templates.
    }

    @Override
    public Map<String, String> getTypeMap(final boolean directionTo) {
        throw new UnsupportedOperationException("Not supported yet.");    // To change body of generated methods, choose
                                                                          // Tools | Templates.
    }

    @Override
    public void handlePKDiff(final List<Statement> targetStatements, final Table table, final CidsClass cidsClass) {
        throw new UnsupportedOperationException("Not supported yet.");    // To change body of generated methods, choose
                                                                          // Tools | Templates.
    }

    @Override
    public void handlePKDefault(final List<StatementGroup> target,
            final TableColumn column,
            final String attrName,
            final String tableName) {
        throw new UnsupportedOperationException("Not supported yet.");    // To change body of generated methods, choose
                                                                          // Tools | Templates.
    }

    @Override
    public boolean typeMismatch(final TableColumn column, final Attribute attr) {
        throw new UnsupportedOperationException("Not supported yet.");    // To change body of generated methods, choose
                                                                          // Tools | Templates.
    }

    @Override
    public boolean isIntegerType(final Table table, final String attr) {
        throw new UnsupportedOperationException("Not supported yet.");    // To change body of generated methods, choose
                                                                          // Tools | Templates.
    }
}
