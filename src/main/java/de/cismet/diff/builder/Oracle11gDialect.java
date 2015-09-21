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

import de.cismet.diff.container.CodedStatement;
import de.cismet.diff.container.NativeStatementGroup;
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
public class Oracle11gDialect extends AbstractDialect {

    //~ Instance fields --------------------------------------------------------

    private final transient Map<String, String> typemapCidsToDBMS;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Oracle11gDialect object.
     *
     * @param  runtime  DOCUMENT ME!
     */
    public Oracle11gDialect(final Properties runtime) {
        super(runtime);
        this.typemapCidsToDBMS = getTypeMap(true);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean sequenceExists(final String seqName) {
        ResultSet rs = null;
        try {
            final String stmt = "SELECT * FROM user_sequences WHERE lower(sequence_name) = '" + seqName.toLowerCase()
                        + "'"; // NOI18N
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
            return !DatabaseConnection.execSQL(
                        runtime,
                        "SELECT 1 FROM "
                                + tableName
                                + " WHERE rownum <= 1 ",
                        this.hashCode())
                        .next();
        } catch (final SQLException ex) {
            return true;
        }
    }

    @Override
    public Table findTable(final Table[] tables, final String tableName) {
        final String schema = runtime.getProperty("connection.username"); // NOI18N
        for (final Table t : tables) {
            // default behaviour
            if (t.getTableName().equalsIgnoreCase(tableName)) {
                return t;
            }

            if (t.getTableName().toLowerCase().replaceFirst("^" + schema.toLowerCase() + ".", "").equalsIgnoreCase(
                            tableName)) {
                return t;
            }
        }

        return null;
    }

    @Override
    public StatementGroup createDialectAwareStatementGroup(final StatementGroup group) throws IllegalCodeException {
        // oracle does not support DDL transactions
        final StatementGroup copy = new StatementGroup(group.getStatements(), false);
        copy.setColumnName(group.getColumnName());
        copy.setDescription(group.getDescription());
        copy.setTableName(group.getTableName());
        copy.setWarning(group.getWarning());
        return new NativeStatementGroup(copy, "de.cismet.diff.resource.oracle11gTemplate");
    }

    @Override
    public void handlePKCreation(final List<Statement> targetStatements,
            final StringBuilder targetCreateTable,
            final String attrName,
            final String tableName) {
        targetCreateTable.append(attrName).append(" INTEGER PRIMARY KEY"); // NOI18N

        if (!sequenceExists(tableName + ScriptGenerator.SEQ_SUFFIX)) {
            targetStatements.add(
                0,
                new CodedStatement(
                    CodedStatement.CODE_CREATE_SEQUENCE,
                    null,
                    false,
                    tableName.toLowerCase()
                            + ScriptGenerator.SEQ_SUFFIX));
        }

        if (!triggerExists(tableName, attrName)) {
            targetStatements.add(
                new CodedStatement(
                    CodedStatement.CODE_CREATE_SEQ_TRIGGER,
                    null,
                    false,
                    tableName.toLowerCase()
                            + "_"
                            + attrName.toLowerCase()
                            + "_trigger", // NOI18N
                    tableName.toLowerCase(),
                    attrName,
                    tableName.toLowerCase()
                            + ScriptGenerator.SEQ_SUFFIX));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   tableName  DOCUMENT ME!
     * @param   attrName   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean triggerExists(final String tableName, final String attrName) {
        ResultSet rs = null;
        try {
            final String pureTableName;
            final int dotIndex = tableName.indexOf('.');
            if (dotIndex > 0) {
                pureTableName = tableName.substring(dotIndex + 1);
            } else {
                pureTableName = tableName;
            }

            final String stmt = "SELECT * FROM user_triggers WHERE lower(trigger_name) = '"
                        + pureTableName.toLowerCase()
                        + "_" + attrName.toLowerCase()
                        + "_trigger'"; // NOI18N
            rs = DatabaseConnection.execSQL(runtime, stmt, this.hashCode());

            return rs.next();
        } catch (final SQLException ex) {
            return false;
        } finally {
            DatabaseConnection.closeResultSet(rs);
        }
    }

    @Override
    public String getTypeMapBundle() {
        return "de.cismet.diff.resource.typemap_oracle_11g";
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
                    CodedStatement.CODE_ALTER_DROP_PRIMARY,
                    CodedStatement.WARNING_DROP_PRIMARY_KEY,
                    false,
                    table.getTableName()));
            targetStatements.add(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_ADD_PRIMARY,
                    CodedStatement.WARNING_NEW_PRIMARY_KEY,
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
                    CodedStatement.CODE_ALTER_DROP_PRIMARY,
                    CodedStatement.WARNING_DROP_PRIMARY_KEY,
                    false,
                    table.getTableName())); // NOI18N
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
        if (!triggerExists(tableName, attrName)) {
            target.add(new StatementGroup(
                    new Statement[] {
                        new CodedStatement(
                            CodedStatement.CODE_CREATE_SEQ_TRIGGER,
                            null,
                            false,
                            tableName.toLowerCase()
                                    + "_"
                                    + attrName.toLowerCase()
                                    + "_trigger", // NOI18N
                            tableName.toLowerCase(),
                            attrName,
                            tableName.toLowerCase()
                                    + ScriptGenerator.SEQ_SUFFIX)
                    },
                    false));
        }

        if (column.getDefaultValue() != null) {
            final Statement[] s = {
                    new CodedStatement(
                        CodedStatement.CODE_ALTER_COLUMN_DROP,
                        null,
                        false,
                        tableName,
                        attrName.toLowerCase(),
                        "DEFAULT") // NOI18N
                };
            target.add(new StatementGroup(s, false));
        }
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
            // plain match did not help, so go into detail
            if (attrTypeName.startsWith("int") && isIntegerType(column)) {
                // all good
            } else {
                mismatch = true;
            }

            // TODO: find out about the different types
        }

        return mismatch;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   tc  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isIntegerType(final TableColumn tc) {
        return tc.getTypeName().equalsIgnoreCase("NUMBER") && (tc.getPrecision() == 38) && (tc.getScale() == 0);
    }

    @Override
    public boolean isIntegerType(final Table table, final String attr) {
        return isIntegerType(table.getTableColumn(attr));
    }

    @Override
    public Statement[] allowNull(final String table, final String attr) {
        return new Statement[] {
                new CodedStatement(
                    CodedStatement.CODE_ALTER_COLUMN_SET,
                    null,
                    false,
                    table,
                    attr.toLowerCase(),
                    ScriptGenerator.NULL)
            };
    }

    @Override
    public Statement[] removeDefault(final String table, final String attr) {
        return new Statement[] {
                new CodedStatement(
                    CodedStatement.CODE_ALTER_COLUMN_SET,
                    null,
                    false,
                    table,
                    attr.toLowerCase(),
                    "DEFAULT "
                            + ScriptGenerator.NULL)
            };
    }

    @Override
    public int getMaxIdentifierChars() {
        return 19;
    }
}
