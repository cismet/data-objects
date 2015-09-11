/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.builder;

import org.apache.log4j.Logger;

import java.sql.DatabaseMetaData;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.Type;

import de.cismet.diff.DiffAccessor;

import de.cismet.diff.container.Action;
import de.cismet.diff.container.CodedStatement;
import de.cismet.diff.container.Statement;
import de.cismet.diff.container.StatementGroup;
import de.cismet.diff.container.Table;
import de.cismet.diff.container.TableColumn;

import de.cismet.diff.exception.IllegalCodeException;
import de.cismet.diff.exception.ScriptGeneratorException;

import de.cismet.diff.util.ProgressionQueue;

/**
 * This class can generate an array of <code>PSQLStatements</code> that contain the statements required to adjust the
 * state of the tables in a database to the state stored in the "cids" system tables. The state in the "cids" system
 * tables is always decisive.
 *
 * @author   Martin Scholl
 * @version  1.0 2007-03-09
 */
// check god class argument, pmd seems to confuse the container classes (*statement) as 'closable resource', case
// conversion is only done for identifiers which are ASCII only
@SuppressWarnings({ "PMD.GodClass", "PMD.CloseResource", "PMD.UseLocaleWithCaseConversions" })
public class ScriptGenerator {

    //~ Static fields/initializers ---------------------------------------------

    public static final String INTEGER = "INTEGER"; // NOI18N
    private static final String INT = "int";        // NOI18N

    private static final transient Logger LOG = Logger.getLogger(ScriptGenerator.class);
    public static final String NULL = "NULL";
    public static final String NOT_NULL = "NOT " + NULL;
    public static final String SEQ_SUFFIX = "_seq";

    //~ Instance fields --------------------------------------------------------

    private final transient String TMP_COLUMN = "swapper_tmp_" + System.currentTimeMillis(); // NOI18N
    // drop columns if they're not persistent columns defined in the cids metadata tables (cs_attr)
    private final transient boolean dropColumns;

    private transient Table[] allTables;
    private transient List<Table> tables;
    private transient List<CidsClass> classes;
    private transient List<CidsClass> classesDone;

    private transient List<String> callStack;

    private transient List<StatementGroup> statements;
    private transient StatementGroup[] dialectAwareStatementGroups;
    private transient Map<String, String> typemapCidsToDBMS;
    private transient ProgressionQueue queue;

    private final transient ResourceBundle exceptionBundle = ResourceBundle.getBundle(
            DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
    private final transient ResourceBundle descBundle = ResourceBundle.getBundle(
            "de.cismet.diff.resource.psqlTemplateDescription"); // NOI18N

    private final transient ScriptGeneratorDialect dialect;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ScriptGenerator object.
     *
     * @param  runtime  DOCUMENT ME!
     * @param  t        DOCUMENT ME!
     */
    public ScriptGenerator(final Properties runtime, final TableLoader t) {
        this(runtime, t.getTables(), t.getClasses(), null);
    }

    /**
     * Creates a new ScriptGenerator object.
     *
     * @param  runtime  DOCUMENT ME!
     * @param  t        DOCUMENT ME!
     * @param  storage  DOCUMENT ME!
     */
    public ScriptGenerator(final Properties runtime, final TableLoader t, final ProgressionQueue storage) {
        this(runtime, t.getTables(), t.getClasses(), storage);
    }

    /**
     * Creates a new ScriptGenerator object.
     *
     * @param  runtime  DOCUMENT ME!
     * @param  tables   DOCUMENT ME!
     * @param  classes  DOCUMENT ME!
     */
    public ScriptGenerator(final Properties runtime, final Table[] tables, final CidsClass[] classes) {
        this(runtime, tables, classes, null);
    }

    /**
     * Creates a new ScriptGenerator object.
     *
     * @param   runtime  DOCUMENT ME!
     * @param   tables   DOCUMENT ME!
     * @param   classes  DOCUMENT ME!
     * @param   storage  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public ScriptGenerator(
            final Properties runtime,
            final Table[] tables,
            final CidsClass[] classes,
            final ProgressionQueue storage) {
        if ((tables == null) || (tables.length == 0)) {
            throw new IllegalArgumentException(
                exceptionBundle.getString(
                    DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_TABLES_NULL_OR_EMPTY));
        }
        if ((classes == null) || (classes.length == 0)) {
            throw new IllegalArgumentException(
                exceptionBundle.getString(
                    DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_CLASSES_NULL_OR_EMPTY));
        }
        this.allTables = Arrays.copyOf(tables, tables.length);
        this.tables = new ArrayList<Table>(allTables.length);
        for (final Table t : allTables) {
            if (t != null) {
                this.tables.add(t);
            }
        }

        final CidsClass[] intClasses = Arrays.copyOf(classes, classes.length);
        this.classes = new ArrayList<CidsClass>(intClasses.length);
        for (final CidsClass c : intClasses) {
            if (c != null) {
                this.classes.add(c);
            }
        }
        this.dialect = DialectFactory.getScriptGeneratorDialect(runtime);
        this.classesDone = new ArrayList<CidsClass>(this.classes.size());
        this.typemapCidsToDBMS = dialect.getTypeMap(true);
        this.statements = new ArrayList<StatementGroup>();
        this.queue = storage;
        this.callStack = new ArrayList<String>();
        this.dialectAwareStatementGroups = null;

        final String propDropColumnsValue = runtime.getProperty(DiffAccessor.PROP_DROP_COLUMNS);
        // for backwards compatibility the default value is true, so we check if the property is explicitly set to
        // false, we can thus not rely on the default Boolean.valueOf() operation
        dropColumns = !"false".equalsIgnoreCase(propDropColumnsValue); // NOI18N
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ScriptGeneratorException  DOCUMENT ME!
     */
    public StatementGroup[] getStatementGroups() throws ScriptGeneratorException {
        if (dialectAwareStatementGroups != null) {
            return Arrays.copyOf(dialectAwareStatementGroups, dialectAwareStatementGroups.length);
        }
        while (!classes.isEmpty()) {
            callStack.clear();
            final List<StatementGroup> s = createStatements(classes.get(0));
            if (s != null) {
                statements.addAll(s);
            }
        }
        while (!tables.isEmpty()) {
            callStack.clear();
            final StatementGroup s = create_DROP_statement(tables.get(0));
            if (s != null) {
                statements.add(s);
            }
        }
        final LinkedList<StatementGroup> dialectAwareGroups = new LinkedList<StatementGroup>();
        try {
            while (!statements.isEmpty()) {
                final StatementGroup current = statements.remove(0);
                if (current != null) {
                    dialectAwareGroups.addLast(dialect.createDialectAwareStatementGroup(current));
                }
            }
        } catch (final IllegalCodeException ex) {
            LOG.error("error during generation: illegal code: " + ex.getCode(), ex); // NOI18N
            throw new ScriptGeneratorException(
                exceptionBundle.getString(
                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_EXC_STATEMENT_CREATE),
                ex);
        }
        dialectAwareStatementGroups = new StatementGroup[dialectAwareGroups.size()];
        dialectAwareStatementGroups = dialectAwareGroups.toArray(dialectAwareStatementGroups);

        return Arrays.copyOf(dialectAwareStatementGroups, dialectAwareStatementGroups.length);
    }

    /**
     * cids only, no dialect specific stuff.
     *
     * @param   c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ScriptGeneratorException  DOCUMENT ME!
     */
    private List<StatementGroup> createStatements(final CidsClass c) throws ScriptGeneratorException {
        if (c == null) {
            return null;
        }
        final Table t = dialect.findTable(allTables, c.getTableName());
        if (t != null) {
            return create_ALTER_Statements(c, t);
        }
        return create_CREATE_Statement(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     * @param   t  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ScriptGeneratorException  DOCUMENT ME!
     */
    private List<StatementGroup> create_ALTER_Statements(final CidsClass c, final Table t)
            throws ScriptGeneratorException {
        final Iterator<Attribute> it = c.getAttributes().iterator();
        final LinkedList<TableColumn> tableColumns = new LinkedList<TableColumn>();
        final LinkedList<StatementGroup> statementGroups = new LinkedList<StatementGroup>();
        for (final TableColumn tc : t.getColumns()) {
            tableColumns.add(tc);
        }
        // add primary key statements
        final StatementGroup primkeyGroup = createPrimaryKeyStatements(t, c);
        if (primkeyGroup != null) {
            statementGroups.addLast(primkeyGroup);
        }
        while (it.hasNext()) {
            final String warning = null;
            final Attribute current = it.next();
            final String attrName = current.getFieldName();
            final Type attrType = current.getType();
            String attrTypeName = attrType.getName().toLowerCase();
            if (typemapCidsToDBMS.containsKey(attrTypeName)) {
                attrTypeName = typemapCidsToDBMS.get(attrTypeName);
            }

            // extension attributes get special treatment: they will completely be left alone. However, if 'drop
            // columns' is activated, we still don't have to do anything but to go to the next object of the iterator,
            // because the default cleanup facilities will take care. So be careful when cleanup mechanism shall be
            // changed.
            if (Boolean.TRUE.equals(current.isExtensionAttr())) {
                continue;
            }

            // this is only a "virtual" field to indicate the 1:N relationship and thus can savely be ignored. However,
            // just like extension attributes, if 'drop columns' is activated, the cleanup facilities will take care in
            // case of the presence of this table column (column != null). So again, be careful when cleanup mechanism
            // shall be changed
            if ((current.getForeignKeyClass() != null) && (current.getForeignKeyClass() < 0)) {
                continue;
            }

            // column is present?
            final TableColumn column = t.getTableColumn(attrName);

            // column not present, create new column
            if (column == null) {
                // <editor-fold defaultstate="collapsed" desc=" handle complex type ">
                if (attrType.isComplexType()) {
                    // checks whether this type is already present or tries to create it
                    // by calling the createStatements method with the name of the type as
                    // param. the name is also added to a call stack to prevent endless
                    // loops in case of cyclic relation between two or more classes.
                    // type is still not present after creation try, a
                    // ScriptGeneratorException will be thrown.
                    if (!(classesDone.contains(attrType.getCidsClass())
                                    || callStack.contains(attrType.getName()))) {
                        callStack.add(attrType.getName());
                        final List<StatementGroup> s = createStatements(attrType.getCidsClass());
                        if (s != null) {
                            statementGroups.addAll(s);
                        }
                        if (!classesDone.contains(attrType.getCidsClass())) {
                            throw new ScriptGeneratorException(
                                exceptionBundle.getString(
                                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_COMP_TYPE),
                                t.getTableName(),
                                attrName,
                                null);
                        }
                    }

                    // add statement to statementlist
                    final Statement[] st = {
                            new CodedStatement(
                                CodedStatement.CODE_ALTER_ADD_COLUMN,
                                warning,
                                false,
                                t.getTableName(),
                                attrName.toLowerCase(),
                                INTEGER)
                        };

                    // in certain cases recursive createStatement calls lead to an already present statement, thus we
                    // have to check if the statement group is already present
                    final StatementGroup sg = new StatementGroup(st, false);
                    if (!statementGroups.contains(sg)) {
                        statementGroups.addLast(sg);
                    }
                } // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc=" handle normal type ">
                else {
                    // add precision to parameter if present
                    final StringBuilder paramAttrType;

                    // bpchar is just internal and cannot be used to create new columns :(
                    // --> remapping to char
                    if ("bpchar".equals(attrTypeName)) {           // NOI18N
                        paramAttrType = new StringBuilder("char"); // NOI18N
                    } else {
                        paramAttrType = new StringBuilder(attrTypeName);
                    }
                    if (current.getPrecision() != null) {
                        paramAttrType.append('(');
                        // also add scale to parameter if present
                        if (current.getScale() == null) {
                            paramAttrType.append(current.getPrecision()); // NOI18N
                        } else {
                            paramAttrType.append(current.getPrecision());
                            paramAttrType.append(',');
                            paramAttrType.append(current.getScale());
                        }
                        paramAttrType.append(')');
                    }
                    final Statement[] s = {
                            new CodedStatement(
                                CodedStatement.CODE_ALTER_ADD_COLUMN,
                                null,
                                false,
                                t.getTableName(),
                                attrName,
                                paramAttrType.toString())
                        };
                    statementGroups.addLast(new StatementGroup(s, false));
                }                                                         // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc=" add default if present ">
                if (attrName.equalsIgnoreCase(c.getPrimaryKeyField())) {
                    dialect.handlePKDefault(statementGroups, column, attrName, t.getTableName());
                } else if (current.getDefaultValue() != null) {
                    if (
                        !dialect.isDefaultValueValid(
                                    attrName,
                                    attrTypeName,
                                    current.getPrecision(),
                                    current.getScale(),
                                    current.getDefaultValue())) {
                        throw new ScriptGeneratorException(
                            exceptionBundle.getString(
                                DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_DEF_TYPE_MISMATCH),
                            t.getTableName(),
                            attrName,
                            null);
                    }
                    final Statement[] s = {
                            new CodedStatement(
                                CodedStatement.CODE_ALTER_COLUMN_SET,
                                null,
                                false,
                                t.getTableName(),
                                attrName.toLowerCase(),
                                "DEFAULT " // NOI18N
                                        + current.getDefaultValue())
                        };
                    statementGroups.addLast(new StatementGroup(s, false));
                }                      // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc=" set not null if needed ">
                if (!current.isOptional()) {
                    // default value is valid if present due to check above
                    if (current.getDefaultValue() == null) {
                        final Statement[] s = {
                                new CodedStatement(
                                    CodedStatement.CODE_ALTER_COLUMN_SET,
                                    CodedStatement.WARNING_ALTER_COLUMN_TO_NOTNULL,
                                    true,
                                    t.getTableName(),
                                    attrName.toLowerCase(),
                                    NOT_NULL) // NOI18N
                            };
                        statementGroups.addLast(new StatementGroup(s, false));
                    } else {
                        final Statement[] s = {
                                new CodedStatement(
                                    CodedStatement.CODE_UPDATE_WHERE_NULL,
                                    null,
                                    false,
                                    t.getTableName(),
                                    attrName.toLowerCase(),
                                    current.getDefaultValue()),
                                new CodedStatement(
                                    CodedStatement.CODE_ALTER_COLUMN_SET,
                                    null,
                                    false,
                                    t.getTableName(),
                                    attrName.toLowerCase(),
                                    NOT_NULL)
                            };
                        final StatementGroup group = new StatementGroup(s, true);
                        final MessageFormat descform = new MessageFormat(
                                descBundle.getString(
                                    StatementGroup.GROUP_DESC_UPDATE_AND_NOT_NULL));
                        final String[] args = { t.getTableName(), attrName.toLowerCase() };
                        group.setDescription(descform.format(args));
                        group.setTableName(t.getTableName());
                        group.setColumnName(attrName.toLowerCase());
                        statementGroups.addLast(group);
                    }
                }                             // </editor-fold>
            }
            // column present, look for differences
            else {
                // <editor-fold defaultstate="collapsed" desc=" handle complex type ">
                if (attrType.isComplexType()) {
                    // checks whether this type is already present or tries to create it
                    // by calling the createStatements method with the name of the type as
                    // param. the name is also added to a call stack to prevent endless
                    // loops in case of cyclic relation between two or more classes.
                    // type is still not present after creation try, a
                    // ScriptGeneratorException will be thrown.
                    if (!(classesDone.contains(attrType.getCidsClass())
                                    || callStack.contains(attrType.getName()))) {
                        callStack.add(attrType.getName());
                        final List<StatementGroup> s = createStatements(attrType.getCidsClass());
                        if (s != null) {
                            statementGroups.addAll(s);
                        }
                        if (!classesDone.contains(attrType.getCidsClass())) {
                            throw new ScriptGeneratorException(
                                exceptionBundle.getString(
                                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_COMP_TYPE),
                                t.getTableName(),
                                attrName,
                                attrTypeName,
                                null);
                        }

                        if (!dialect.isIntegerType(t, attrName)) {
                            statementGroups.addLast(createTypeConversionStatements(
                                    t,
                                    current,
                                    INTEGER));
                        }
                    }
                } // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc=" handle normal type ">
                else {
                    // type mismatch
                    if (dialect.typeMismatch(column, current)) {
                        statementGroups.addLast(createTypeConversionStatements(
                                t,
                                current,
                                null));
                    }
                } // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc=" handle primary key ">
                if (current.getFieldName().equalsIgnoreCase(c.getPrimaryKeyField())) {
                    dialect.handlePKDefault(statementGroups, column, attrName, t.getTableName());
                } // </editor-fold>
                // <editor-fold defaultstate="collapsed" desc=" handle normal attr ">
                else {
                    // <editor-fold defaultstate="collapsed" desc=" drop default value ">
                    if ((current.getDefaultValue() == null)
                                && ((t.getDefaultValue(attrName) != null)
                                    && !t.getDefaultValue(attrName).equals("NULL"))) {
                        statementGroups.addLast(new StatementGroup(
                                dialect.removeDefault(t.getTableName(), attrName),
                                false));
                    }
                    // </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc=" set default value ">
                    if ((current.getDefaultValue() != null)
                                && !defaultValueEquals(current.getDefaultValue(), t.getDefaultValue(attrName))) {
                        if (
                            !dialect.isDefaultValueValid(
                                        attrName,
                                        attrTypeName,
                                        current.getPrecision(),
                                        current.getScale(),
                                        current.getDefaultValue())) {
                            throw new ScriptGeneratorException(
                                exceptionBundle.getString(
                                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_DEF_TYPE_MISMATCH),
                                t.getTableName(),
                                attrName,
                                null);
                        }
                        final Statement[] s = {
                                new CodedStatement(
                                    CodedStatement.CODE_ALTER_COLUMN_SET,
                                    null,
                                    false,
                                    t.getTableName(),
                                    attrName.toLowerCase(),
                                    "DEFAULT " // NOI18N
                                            + current.getDefaultValue())
                            };
                        statementGroups.addLast(new StatementGroup(s, false));
                    }                      // </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc=" alter column to 'optional' ">
                    if (current.isOptional() && (column.getNullable() != DatabaseMetaData.attributeNullable)) {
                        if (column.getColumnName().equalsIgnoreCase(c.getPrimaryKeyField())) {
                            throw new ScriptGeneratorException(
                                exceptionBundle.getString(
                                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_NOT_NULLABLE),
                                t.getTableName(),
                                attrName,
                                null);
                        }
                        statementGroups.addLast(new StatementGroup(
                                dialect.allowNull(t.getTableName(), attrName),
                                false));
                    } // </editor-fold>
                    // <editor-fold defaultstate="collapsed" desc=" alter column to 'required' ">
                    if (!current.isOptional() && (column.getNullable() != DatabaseMetaData.attributeNoNulls)) {
                        if (current.getDefaultValue() == null) {
                            final Statement[] s = {
                                    new CodedStatement(
                                        CodedStatement.CODE_ALTER_COLUMN_SET,
                                        CodedStatement.WARNING_ALTER_COLUMN_TO_NOTNULL,
                                        true,
                                        t.getTableName(),
                                        attrName.toLowerCase(),
                                        NOT_NULL)
                                };
                            statementGroups.addLast(new StatementGroup(s, false));
                        } else {
                            if (
                                !dialect.isDefaultValueValid(
                                            attrName,
                                            attrTypeName,
                                            current.getPrecision(),
                                            current.getScale(),
                                            current.getDefaultValue())) {
                                throw new ScriptGeneratorException(
                                    exceptionBundle.getString(
                                        DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_DEF_TYPE_MISMATCH),
                                    t.getTableName(),
                                    attrName,
                                    null);
                            }
                            final Statement[] s = {
                                    new CodedStatement(
                                        CodedStatement.CODE_UPDATE_WHERE_NULL,
                                        null,
                                        false,
                                        t.getTableName(),
                                        attrName.toLowerCase(),
                                        current.getDefaultValue()),
                                    new CodedStatement(
                                        CodedStatement.CODE_ALTER_COLUMN_SET,
                                        null,
                                        false,
                                        t.getTableName(),
                                        attrName.toLowerCase(),
                                        NOT_NULL)
                                };
                            final StatementGroup group = new StatementGroup(s, true);
                            final MessageFormat descform = new MessageFormat(
                                    descBundle.getString(
                                        StatementGroup.GROUP_DESC_UPDATE_AND_NOT_NULL));
                            final String[] args = { t.getTableName(), attrName.toLowerCase() };
                            group.setDescription(descform.format(args));
                            group.setTableName(t.getTableName());
                            group.setColumnName(attrName.toLowerCase());
                            statementGroups.addLast(group);
                        }
                    } // </editor-fold>
                }     // </editor-fold>
                tableColumns.remove(column);
            }
        }
        // <editor-fold defaultstate="collapsed" desc=" drop columns that are not in "cs_attr" anymore ">
        // we only drop columns if 'drop columns' is activated
        if (dropColumns) {
            while (!tableColumns.isEmpty()) {
                if (tableColumns.getFirst().getColumnName().equalsIgnoreCase(c.getPrimaryKeyField())) {
                    throw new ScriptGeneratorException(
                        exceptionBundle.getString(
                            DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NO_DROP),
                        t.getTableName(),
                        c.getPrimaryKeyField(),
                        null);
                }
                final Statement[] s = {
                        new CodedStatement(
                            CodedStatement.CODE_ALTER_DROP_COLUMN,
                            null,
                            false,
                            t.getTableName(),
                            tableColumns.removeFirst().getColumnName())
                    };
                statementGroups.addLast(new StatementGroup(s, false));
            }
        } // </editor-fold>
        tables.remove(t);
        classes.remove(c);
        classesDone.add(c);

        return statementGroups;
    }

    /**
     * cids only, no dialect specific stuff, at least atm.
     *
     * @param   cidsDefault  DOCUMENT ME!
     * @param   dbmsDefault  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean defaultValueEquals(final String cidsDefault, final String dbmsDefault) {
        boolean equal = false;

        if (dbmsDefault != null) {
            if (cidsDefault.isEmpty()) {
                equal = dbmsDefault.isEmpty();
            } else if (cidsDefault.length() == 1) {
                equal = (dbmsDefault.length() == 1) && (cidsDefault.charAt(0) == dbmsDefault.charAt(0));
            } else if ((cidsDefault.charAt(0) == '\'') && (cidsDefault.charAt(cidsDefault.length() - 1) == '\'')) {
                // we have a string default value
                // strip the quotes and compare the 'bare' values
                final String cD = cidsDefault.substring(1, cidsDefault.length() - 1);
                // take care to strip the quotes appropriately as there might be additional casts inserted by the dbms
                final String dD = stripSingleQuotes(dbmsDefault);

                equal = cD.equals(dD);
            } else {
                equal = cidsDefault.equals(dbmsDefault);

                if (!equal) {
                    // we have to add legacy behaviour as everything has been considered a string before
                    final String dD = stripSingleQuotes(dbmsDefault);
                    equal = cidsDefault.equals(dD);
                }
            }
        }

        return equal;
    }

    /**
     * cids only, no dialect specific stuff.
     *
     * @param   s  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String stripSingleQuotes(final String s) {
        String ret = s;

        if (s != null) {
            final int start = s.indexOf('\'') + 1;
            final int end = s.lastIndexOf('\'');

            if ((start > 0) && (end > start)) {
                ret = s.substring(start, end);
            }
        }

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ScriptGeneratorException  DOCUMENT ME!
     */
    private List<StatementGroup> create_CREATE_Statement(final CidsClass c) throws ScriptGeneratorException {
        // is name valid
        if (c.getTableName().contains(" ")) { // NOI18N
            throw new ScriptGeneratorException(
                exceptionBundle.getString(
                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_TABLENAME_HAS_SPACES),
                c.getTableName(),
                null);
        }
        // is primary key present and valid
        if (c.getPrimaryKeyField() == null) {
            throw new ScriptGeneratorException(
                exceptionBundle.getString(
                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_PRIMKEY_FIELD),
                c.getTableName(),
                null);
        } else if (c.getPrimaryKeyField().isEmpty()) {
            throw new ScriptGeneratorException(
                exceptionBundle.getString(
                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_EMPTY_PRIMKEY_FIELD),
                c.getTableName(),
                null);
        }
        final LinkedList<Statement> statem = new LinkedList<Statement>();
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put(CodedStatement.KEY_TABLENAME, c.getTableName().toLowerCase());
        final StringBuilder nameTypeEnum = new StringBuilder(20);
        final Iterator<Attribute> it = c.getAttributes().iterator();
        boolean primarykeyFound = false;
        while (it.hasNext()) {
            final Attribute current = it.next();
            // extension attributes and 1:N foreign key fields shall be ignored, for further info, see
            // create_ALTER_Statements operation
            if (!Boolean.TRUE.equals(current.isExtensionAttr())
                        && !((current.getForeignKeyClass() != null) && (current.getForeignKeyClass() < 0))) {
                final String name = current.getFieldName().toLowerCase();
                final Type type = current.getType();
                if (type.isComplexType()) {
                    // checks whether this type is already present or tries to create it
                    // by calling the createStatements method with the name of the type as
                    // param. the name is also added to a call stack to prevent endless
                    // loops in case of cyclic relation between two or more classes.
                    // type is still not present after creation try, a
                    // ScriptGeneratorException will be thrown.
                    if (!(classesDone.contains(type.getCidsClass())
                                    || callStack.contains(type.getName()))) {
                        callStack.add(type.getName());
                        final List<StatementGroup> s = createStatements(type.getCidsClass());
                        if (s != null) {
                            statements.addAll(s);
                        }
                        if (!classesDone.contains(type.getCidsClass())) {
                            throw new ScriptGeneratorException(
                                exceptionBundle.getString(
                                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_COMP_TYPE),
                                c.getTableName().toLowerCase(),
                                name,
                                type.getName(),
                                null);
                        }
                    }

                    // TODO: INTEGER as default fk type? should depend on the pk type of the referenced class
                    dialect.appendCreateTableExpression(
                        nameTypeEnum,
                        name,
                        "INTEGER",
                        current.isOptional(),
                        null,
                        null,
                        null); // NOI18N
                }
                // handle primary key
                // primary key is always integer and has a sequence as default value
                // the sequence will be created and is named: '<tablename>_seq'
                else if (name.equalsIgnoreCase(c.getPrimaryKeyField())) {
                    primarykeyFound = true;
                    if (current.isOptional()) {
                        throw new ScriptGeneratorException(
                            exceptionBundle.getString(
                                DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NOT_NULL),
                            c.getTableName().toLowerCase(),
                            c.getPrimaryKeyField().toLowerCase(),
                            null);
                    }
                    if (!type.getName().equalsIgnoreCase(INTEGER)) { // NOI18N
                        throw new ScriptGeneratorException(
                            exceptionBundle.getString(
                                DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_NOT_INTEGER),
                            c.getTableName().toLowerCase(),
                            name,
                            type.getName(),
                            null);
                    }
                    dialect.handlePKCreation(statem, nameTypeEnum, name, c.getTableName());
                } else {
                    String defaultValue = null;
                    // append default value if exists
                    if ((current.getDefaultValue() != null) && !name.equalsIgnoreCase(c.getPrimaryKeyField())) {
                        if (
                            !dialect.isDefaultValueValid(
                                        name,
                                        type.getName(),
                                        current.getPrecision(),
                                        current.getScale(),
                                        current.getDefaultValue())) {
                            throw new ScriptGeneratorException(
                                exceptionBundle.getString(
                                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_DEF_TYPE_MISMATCH),
                                c.getTableName().toLowerCase(),
                                name,
                                null);
                        }
                        defaultValue = current.getDefaultValue();
                    }

                    dialect.appendCreateTableExpression(
                        nameTypeEnum,
                        name,
                        type.getName(),
                        current.isOptional(),
                        current.getPrecision(),
                        current.getScale(),
                        defaultValue);
                }

                nameTypeEnum.append(", "); // NOI18N
            }
        }
        if (!primarykeyFound) {
            throw new ScriptGeneratorException(
                exceptionBundle.getString(
                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_PRIMKEY_FIELD),
                c.getTableName(),
                null);
        }
        nameTypeEnum.delete(nameTypeEnum.length() - 2, nameTypeEnum.length());
        map.put(CodedStatement.KEY_NAME_TYPE_ENUM, nameTypeEnum.toString());
        classes.remove(c);
        classesDone.add(c);
        for (int i = statem.size() - 1; i >= 0; --i) {
            final Statement s = statem.get(i);
            // trigger statements must be last
            if (!((s instanceof CodedStatement)
                            && ((CodedStatement)s).getCode().equals(CodedStatement.CODE_CREATE_SEQ_TRIGGER))) {
                statem.add(i + 1, (new CodedStatement(
                            CodedStatement.CODE_CREATE_STANDARD,
                            // there has never been a warning assigned thus we simply put null here
                            null,
                            false,
                            map)));
            }
        }
        Statement[] s = new Statement[statem.size()];
        s = statem.toArray(s);
        final StatementGroup group = new StatementGroup(s, true);
        group.setTableName(c.getTableName().toLowerCase());
        final MessageFormat descform = new MessageFormat(descBundle.getString(StatementGroup.GROUP_DESC_NEW_TABLE));
        final String[] args = { c.getTableName().toLowerCase() };
        group.setDescription(descform.format(args));
        final LinkedList<StatementGroup> l = new LinkedList<StatementGroup>();
        l.add(group);

        return l;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   table  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private StatementGroup create_DROP_statement(final Table table) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put(CodedStatement.KEY_TABLENAME, table.getTableName());
        tables.remove(table);
        final LinkedList<Statement> statem = new LinkedList<Statement>();
        // if a queue is provided check first if a table has really been dropped by
        // the user or if the table is just in the database with no relation to the
        // cids system
        if (queue == null) {
            // sequence has to be dropped after table
            if (dialect.sequenceExists(table.getTableName() + SEQ_SUFFIX)) {
                statem.add(
                    new CodedStatement(
                        CodedStatement.CODE_DROP_SEQUENCE,
                        null,
                        false,
                        table.getTableName()
                                + SEQ_SUFFIX)); // NOI18N
            }
            statem.addFirst(new CodedStatement(CodedStatement.CODE_DROP_STANDARD, null, false, map));
            final Statement[] s = statem.toArray(new Statement[statem.size()]);
            final StatementGroup ret = new StatementGroup(s, true);
            ret.setDescription(
                MessageFormat.format(descBundle.getString(StatementGroup.GROUP_DESC_NEW_TABLE), table.getTableName()));
            ret.setTableName(table.getTableName());

            return ret;
        } else {
            final Action[] drops = queue.getActionArray(Action.DROP_ACTION);
            if (drops != null) {
                for (final Action action : drops) {
                    // args[0] shall always be the table name if a drop action is stored
                    if (action.getArgs()[0].equalsIgnoreCase(table.getTableName())) {
                        // sequence has to be dropped after table
                        if (dialect.sequenceExists(table.getTableName() + SEQ_SUFFIX)) {
                            statem.add(
                                new CodedStatement(
                                    CodedStatement.CODE_DROP_SEQUENCE,
                                    null,
                                    false,
                                    table.getTableName()
                                            + SEQ_SUFFIX)); // NOI18N
                        }
                        statem.addFirst(new CodedStatement(
                                CodedStatement.CODE_DROP_STANDARD,
                                null,
                                false,
                                map));
                        final Statement[] s = statem.toArray(new Statement[statem.size()]);
                        final StatementGroup ret = new StatementGroup(s, true);
                        ret.setDescription(
                            MessageFormat.format(
                                descBundle.getString(StatementGroup.GROUP_DESC_NEW_TABLE),
                                table.getTableName()));
                        ret.setTableName(table.getTableName());

                        return ret;
                    }
                }
            }
        }
        return null;
    }

    /**
     * sql 99 compliant and not dialect dependent (with regard to mode of operation).
     *
     * @param   table            DOCUMENT ME!
     * @param   attr             DOCUMENT ME!
     * @param   typeToConvertIn  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private StatementGroup createTypeConversionStatements(
            final Table table,
            final Attribute attr,
            final String typeToConvertIn) {
        final LinkedList<CodedStatement> cstatem = new LinkedList<CodedStatement>();
        final String attrTypeName = attr.getType().getName().toUpperCase();
        final String warning = CodedStatement.WARNING_TYPE_MISMATCH;
        final StringBuilder newTypeName = new StringBuilder(attrTypeName);
        if (typeToConvertIn == null) {
            if (attr.getPrecision() != null) {
                newTypeName.append('(');
                newTypeName.append(attr.getPrecision());
                if (attr.getScale() != null) {
                    newTypeName.append(',');
                    newTypeName.append(attr.getScale());
                }
                newTypeName.append(')');
            }
            cstatem.addLast(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_ADD_COLUMN,
                    warning,
                    false,
                    table.getTableName(),
                    TMP_COLUMN,
                    newTypeName.toString()));
        } else {
            cstatem.addLast(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_ADD_COLUMN,
                    warning,
                    false,
                    table.getTableName(),
                    TMP_COLUMN,
                    typeToConvertIn.toUpperCase()));
            newTypeName.delete(0, newTypeName.length());
            newTypeName.append(typeToConvertIn.toUpperCase());
        }
        if (attr.getDefaultValue() != null) {
            cstatem.addLast(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_COLUMN_SET,
                    warning,
                    false,
                    table.getTableName(),
                    TMP_COLUMN,
                    "DEFAULT " // NOI18N
                            + attr.getDefaultValue()));
        }
        cstatem.addLast(
            new CodedStatement(
                CodedStatement.CODE_UPDATE_COPY,
                warning,
                false,
                table.getTableName(),
                TMP_COLUMN,
                attr.getFieldName()));
        // copy has to be performed first, otherwise the set not null statement
        // will obviously fail as long as there is at least one row in the table
        if (!attr.isOptional()) {
            cstatem.addLast(
                new CodedStatement(
                    CodedStatement.CODE_ALTER_COLUMN_SET,
                    warning,
                    false,
                    table.getTableName(),
                    TMP_COLUMN,
                    NOT_NULL)); // NOI18N
        }
        cstatem.addLast(
            new CodedStatement(
                CodedStatement.CODE_ALTER_DROP_COLUMN,
                warning,
                false,
                table.getTableName(),
                attr.getFieldName()));
        cstatem.addLast(
            new CodedStatement(
                CodedStatement.CODE_ALTER_RENAME_COLUMN,
                warning,
                false,
                table.getTableName(),
                attr.getFieldName(),
                TMP_COLUMN));
        final Statement[] stmt = cstatem.toArray(new Statement[cstatem.size()]);
        final StatementGroup group = new StatementGroup(stmt, true);
        group.setTableName(table.getTableName().toLowerCase());
        group.setColumnName(attr.getFieldName().toLowerCase());
        final MessageFormat descform = new MessageFormat(
                descBundle.getString(
                    StatementGroup.GROUP_DESC_UPDATE_AND_NOT_NULL));
        final String[] args = {
                table.getTableName(),
                attr.getFieldName().toLowerCase(),
                table.getTableColumn(attr.getFieldName()).getTypeName().toUpperCase(),
                newTypeName.toString()
            };
        group.setDescription(descform.format(args));
        group.setWarning(descBundle.getString(StatementGroup.WARNING_CONVERT_ERROR_ON_TYPE_MISMATCH));

        return group;
    }

    /**
     * NOTE: typesize, default_value and optional field will be ignored when checking and creating the primary key. type
     * of the primary key has to be integer otherwise exception
     *
     * @param   table      DOCUMENT ME!
     * @param   cidsClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  ScriptGeneratorException  DOCUMENT ME!
     */
    private StatementGroup createPrimaryKeyStatements(final Table table, final CidsClass cidsClass)
            throws ScriptGeneratorException {
        // check for primary key
        // eventually drop existing and/or create new one
        if (cidsClass.getPrimaryKeyField() == null) {
            throw new ScriptGeneratorException(
                exceptionBundle.getString(
                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_PRIMKEY_FIELD),
                cidsClass.getTableName(),
                null);
        }
        if (!containsPrimkeyAttr(cidsClass)) {
            throw new ScriptGeneratorException(
                exceptionBundle.getString(
                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NOT_FOUND),
                table.getTableName(),
                cidsClass.getPrimaryKeyField().toLowerCase(),
                null);
        }
        if (isPrimkeyAttrOptional(cidsClass)) {
            throw new ScriptGeneratorException(
                exceptionBundle.getString(
                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NOT_NULL),
                table.getTableName(),
                cidsClass.getPrimaryKeyField().toLowerCase(),
                null);
        }
        // TODO: add according error message
        if (!isPrimkeyAttrIntegerType(cidsClass)) {
            throw new ScriptGeneratorException(
                exceptionBundle.getString(
                    DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_NOT_INTEGER),
                table.getTableName(),
                cidsClass.getPrimaryKeyField().toLowerCase(),
                null);
        }

        final LinkedList<Statement> codedStatements = new LinkedList<Statement>();

        dialect.handlePKDiff(codedStatements, table, cidsClass);

        StatementGroup createGroup = null;

        if (!codedStatements.isEmpty()) {
            final Statement[] stmts = codedStatements.toArray(new Statement[codedStatements.size()]);
            createGroup = new StatementGroup(stmts, true);
            createGroup.setDescription(
                MessageFormat.format(
                    descBundle.getString(StatementGroup.GROUP_DESC_PRIM_KEY_FIT),
                    table.getTableName()));
            createGroup.setTableName(table.getTableName());
            createGroup.setColumnName(cidsClass.getPrimaryKeyField().toLowerCase());
        }

        return createGroup;
    }

    /**
     * cids only, no dialect specific stuff.
     *
     * @param   cidsClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean containsPrimkeyAttr(final CidsClass cidsClass) {
        final Iterator<Attribute> it = cidsClass.getAttributes().iterator();
        while (it.hasNext()) {
            if (it.next().getFieldName().equalsIgnoreCase(cidsClass.getPrimaryKeyField())) {
                return true;
            }
        }
        return false;
    }

    /**
     * cids only, no dialect specific stuff.
     *
     * @param   cidsClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isPrimkeyAttrOptional(final CidsClass cidsClass) {
        final Iterator<Attribute> it = cidsClass.getAttributes().iterator();
        while (it.hasNext()) {
            final Attribute current = it.next();
            if (current.getFieldName().equalsIgnoreCase(cidsClass.getPrimaryKeyField())) {
                return current.isOptional();
            }
        }
        return false;
    }

    /**
     * cids only, no dialect specific stuff.
     *
     * @param   cidsClass  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isPrimkeyAttrIntegerType(final CidsClass cidsClass) {
        final Iterator<Attribute> it = cidsClass.getAttributes().iterator();
        while (it.hasNext()) {
            final Attribute current = it.next();
            if (current.getFieldName().equalsIgnoreCase(cidsClass.getPrimaryKeyField())) {
                final String name = current.getType().getName();

                return name.toLowerCase().startsWith(INT);
            }
        }
        return false;
    }
}
