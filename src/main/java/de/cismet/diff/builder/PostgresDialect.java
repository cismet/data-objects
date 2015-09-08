/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.builder;

import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;

import de.cismet.diff.container.CodedStatement;
import de.cismet.diff.container.PSQLStatementGroup;
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
public class PostgresDialect implements ScriptGeneratorDialect {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(PostgresDialect.class);

    //~ Instance fields --------------------------------------------------------

    private final Properties runtime;

    private final transient Map<String, String> typemapDBMStoCids;
    private final transient Map<String, String> typemapCidsToDBMS;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PostgresDialect object.
     *
     * @param  runtime  DOCUMENT ME!
     */
    public PostgresDialect(final Properties runtime) {
        this.runtime = runtime;
        this.typemapDBMStoCids = getTypeMap(false);
        this.typemapCidsToDBMS = getTypeMap(true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   directionTo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public final Map<String, String> getTypeMap(final boolean directionTo) {
        final ResourceBundle bundle = ResourceBundle.getBundle("de.cismet.diff.resource.typemap"); // NOI18N

        final Map<String, String> map = new HashMap<String, String>();
        final Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            // for bidirectional mapping
            if (directionTo) {
                map.put(key, bundle.getString(key));
            } else {
                map.put(bundle.getString(key), key);
            }
        }

        return map;
    }

    @Override
    public boolean isDefaultValueValid(final String column,
            final String typename,
            final Integer precision,
            final Integer scale,
            final String defaultValue) {
        try {
            String csTypename = typename;
            // map typename back from postgres internal to cids type name if necessary
            if (typemapDBMStoCids.containsKey(typename)) {
                csTypename = typemapDBMStoCids.get(typename);
            }
            DatabaseConnection.updateSQL(runtime, "BEGIN WORK", this.hashCode()); // NOI18N
            final StringBuffer sb = new StringBuffer(50);
            // build a new temporary table creation string using the given values
            sb.append("CREATE TEMP TABLE cs_tmptable (").append(column).append(' ').append(csTypename); // NOI18N
            // typesize != null indicates that a type is parameterized
            if (precision != null) {
                sb.append('(').append(precision);                    // NOI18N
                if (scale != null) {
                    sb.append(", ").append(scale);                   // NOI18N
                }
                sb.append(')');                                      // NOI18N
            }
            sb.append(" DEFAULT ").append(defaultValue).append(")"); // NOI18N
            // try to create table from creation string, if failes due to exception it
            // indicates that the default value is not valid
            DatabaseConnection.updateSQL(runtime, sb.toString(), this.hashCode());
            // try to insert the default values into the table, if fails due to
            // exception it indicates that the default value has correct type but does
            // not fit in the reserved space for this column and so is not valid
            DatabaseConnection.updateSQL(runtime, "INSERT INTO cs_tmptable DEFAULT VALUES", this.hashCode()); // NOI18N
            // everyting was fine
            return true;
        } catch (final SQLException ex) {
            // an exception indicated that the value is not valid
            return false;
        } finally {
            try {
                // rollback to delete the temporary table
                DatabaseConnection.updateSQL(runtime, "ROLLBACK", this.hashCode()); // NOI18N
            } catch (SQLException ex) {
                // do nothing, table will be deleted when session ends
                LOG.error("temp table could not be deleted", ex); // NOI18N
            }
        }
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
        try {
            return !DatabaseConnection.execSQL(runtime, "SELECT 1 FROM " + tableName + " LIMIT 1", this.hashCode())
                        .next();
        } catch (final SQLException ex) {
            return true;
        }
    }

    @Override
    public Table findTable(final Table[] tables, final String tableName) {
        for (final Table t : tables) {
            // default behaviour
            if (t.getTableName().equalsIgnoreCase(tableName)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public StatementGroup createDialectAwareStatementGroup(final StatementGroup group) throws IllegalCodeException {
        return new PSQLStatementGroup(group);
    }

    @Override
    public StringBuilder appendCreateTableExpression(final StringBuilder targetCreateTable,
            final String attrName,
            final String attrTypeName,
            final boolean optional,
            final Integer precision,
            final Integer scale,
            final String defaultValue) {
        // probably we have to do type conversion if a cids type is not a valid dbms type
        targetCreateTable.append(attrName).append(' ').append(attrTypeName.toUpperCase()); // NOI18N
        if (precision != null) {
            targetCreateTable.append('(').append(precision);                               // NOI18N
            if (scale != null) {
                targetCreateTable.append(", ").append(scale);                              // NOI18N
            }
            targetCreateTable.append(')');                                                 // NOI18N
        }
        if (!optional) {
            targetCreateTable.append(" NOT");                                              // NOI18N
        }
        targetCreateTable.append(" NULL");                                                 // NOI18N

        if (defaultValue != null) {
            targetCreateTable.append(" DEFAULT ").append(defaultValue); // NOI18N
        }

        return targetCreateTable;
    }

    @Override
    public void handlePKCreation(final List<Statement> targetStatements,
            final StringBuilder targetCreateTable,
            final String attrName,
            final String tableName) {
        targetCreateTable.append(attrName).append(" INTEGER PRIMARY KEY DEFAULT nextval('") // NOI18N
        .append(tableName.toLowerCase()).append("_seq')");                                  // NOI18N

        if (!sequenceExists(tableName + ScriptGenerator.SEQ_SUFFIX)) {
            targetStatements.add(
                0,
                new CodedStatement(
                    CodedStatement.CODE_CREATE_SEQUENCE,
                    null,
                    false,
                    tableName.toLowerCase()
                            + ScriptGenerator.SEQ_SUFFIX,
                    "1")); // NOI18N
        }
    }

    @Override
    public void handlePKDiff(final List<Statement> targetStatements, final Table table, final CidsClass cidsClass) {
        if (!sequenceExists(cidsClass.getTableName() + ScriptGenerator.SEQ_SUFFIX)) {
            if (!isTableEmpty(cidsClass.getTableName())) {
                targetStatements.add(
                    0,
                    new CodedStatement(
                        CodedStatement.CODE_SELECT_SETVAL_MAX,
                        null,
                        false,
                        cidsClass.getTableName().toLowerCase(),
                        cidsClass.getPrimaryKeyField().toLowerCase(),
                        cidsClass.getTableName().toLowerCase()
                                + ScriptGenerator.SEQ_SUFFIX));
            }

            targetStatements.add(
                0,
                new CodedStatement(
                    CodedStatement.CODE_CREATE_SEQUENCE,
                    null,
                    false,
                    cidsClass.getTableName().toLowerCase()
                            + ScriptGenerator.SEQ_SUFFIX,
                    "1")); // NOI18N
        }

        // composite primary key, drop it and create n
        if (table.getPrimaryKeyColumnNames().length > 1) {
            targetStatements.add(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_DROP_CONSTRAINT,
                    null,
                    false,
                    table.getTableName(),
                    table.getTableName()
                            + "_pkey")); // NOI18N
            targetStatements.add(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_ADD_PRIMARY,
                    null,
                    false,
                    table.getTableName().toLowerCase(),
                    cidsClass.getPrimaryKeyField().toLowerCase()));
        }
        // no primary key, create new
        else if (table.getPrimaryKeyColumnNames().length < 1) {
            targetStatements.add(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_ADD_PRIMARY,
                    null,
                    false,
                    table.getTableName().toLowerCase(),
                    cidsClass.getPrimaryKeyField().toLowerCase()));
        }
        // if key not equals, drop it create new
        else if (!table.getPrimaryKeyColumnNames()[0].equalsIgnoreCase(
                        cidsClass.getPrimaryKeyField())) {
            targetStatements.add(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_DROP_CONSTRAINT,
                    CodedStatement.WARNING_DROP_PRIMARY_KEY,
                    false,
                    table.getTableName(),
                    table.getTableName()
                            + "_pkey")); // NOI18N
            targetStatements.add(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_ADD_PRIMARY,
                    CodedStatement.WARNING_NEW_PRIMARY_KEY,
                    false,
                    table.getTableName().toLowerCase(),
                    cidsClass.getPrimaryKeyField().toLowerCase()));
        }
    }

    @Override
    public void handlePKDefault(final List<StatementGroup> target,
            final TableColumn column,
            final String attrName,
            final String tableName) {
        //J-
        final String defVal = column == null ? null : column.getDefaultValue();
        if ((defVal == null)
                    || !(
                        // this string represents postgres jdbc 7 drivers
                        defVal.equalsIgnoreCase("nextval('" + tableName + "_seq'::text)") // NOI18N
                        ||
                        // this string represents postgres jdbc 8 drivers
                        defVal.equalsIgnoreCase("nextval('" + tableName + "_seq'::regclass)"))) { // NOI18N

            final Statement[] s = {
                    new CodedStatement(
                        CodedStatement.CODE_ALTER_COLUMN_SET,
                        null,
                        false,
                        tableName,
                        attrName.toLowerCase(),
                        "DEFAULT nextval('" + tableName + "_seq')") // NOI18N
                };
            target.add(new StatementGroup(s, false));
        }
        //J+
    }

    @Override
    public boolean isIntegerType(final Table table, final String attr) {
        // TODO: add BIGINT support
        return table.getColumnTypeName(attr).equalsIgnoreCase("int4"); // NOI18N
    }

    @Override
    public boolean typeMismatch(final TableColumn column, final Attribute attr) {
        boolean mismatch = false;
        String attrTypeName = attr.getType().getName().toLowerCase();
        if (typemapCidsToDBMS.containsKey(attrTypeName)) {
            attrTypeName = typemapCidsToDBMS.get(attrTypeName);
        }
        if ((!attrTypeName.equalsIgnoreCase(column.getTypeName()))
                    || ((attr.getPrecision() != null)
                        && (attr.getPrecision() != column.getPrecision()))
                    || ((attr.getScale() != null)
                        && (attr.getScale() != column.getScale()))) {
            // this check has to be added since postgres jdbc driver 8 and later
            // handles columns, that are of type int or bigint and have not null
            // constraint as well as a sequence as their default value, as
            // serial types. serial types basically are integers
            if ((attrTypeName.equalsIgnoreCase("int4")                // NOI18N
                            || attrTypeName.equalsIgnoreCase("int8")) // NOI18N


                        // it is already interpreted
                        // as
                        // serial if there is a
                        // sequence
                        // present, maybe I
                        // misunderstood the
                        // description..... -.-
                        // column.getNullable() ==
                        // DatabaseMetaData.attributeNoNulls
                        // &&
                        && (column.getDefaultValue() != null)
                        && column.getDefaultValue().startsWith("nextval")) {   // NOI18N
                if (attrTypeName.equalsIgnoreCase("int4")) {                   // NOI18N
                    if (!column.getTypeName().equalsIgnoreCase("serial")) {    // NOI18N
                        mismatch = true;
                    }
                } else {
                    if (!column.getTypeName().equalsIgnoreCase("bigserial")) { // NOI18N
                        mismatch = true;
                    }
                }
            } else {
                mismatch = true;
            }
        }

        return mismatch;
    }
}
