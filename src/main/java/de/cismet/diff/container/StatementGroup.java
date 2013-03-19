/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.container;

import java.util.Arrays;

import de.cismet.tools.Equals;

/**
 * DOCUMENT ME!
 *
 * @author   Martin Scholl
 * @version  1.0
 */
public class StatementGroup {

    //~ Static fields/initializers ---------------------------------------------

    public static final String GROUP_DESC_UPDATE_AND_NOT_NULL = "group_desc_update_and_notnull";                     // NOI18N
    public static final String GROUP_DESC_NEW_TABLE = "group_desc_new_table";                                        // NOI18N
    public static final String GROUP_DESC_CONVERT_TYPE = "group_desc_convert_type";                                  // NOI18N
    public static final String GROUP_DESC_PRIM_KEY_FIT = "group_desc_prim_key_fit";                                  // NOI18N
    public static final String GROUP_DESC_DEL_TABLE = "group_desc_del_table_and_prim_key_seq";                       // NOI18N
    public static final String WARNING_CONVERT_ERROR_ON_TYPE_MISMATCH = "group_warn_convert_error_on_type_mismatch"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    protected String tableName;
    protected String columnName;
    protected String description;
    protected String warning;
    protected Statement[] statements;
    protected boolean transaction;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>StatementGroup.</code>
     *
     * @param  statements   DOCUMENT ME!
     * @param  transaction  DOCUMENT ME!
     */
    public StatementGroup(final Statement[] statements, final boolean transaction) {
        this.transaction = transaction;
        this.statements = Arrays.copyOf(statements, statements.length);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Statement[] getStatements() {
        return Arrays.copyOf(statements, statements.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  statements  DOCUMENT ME!
     */
    public void setStatements(final Statement[] statements) {
        this.statements = Arrays.copyOf(statements, statements.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  description  DOCUMENT ME!
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getWarning() {
        return warning;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  warning  DOCUMENT ME!
     */
    public void setWarning(final String warning) {
        this.warning = warning;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isTransaction() {
        return transaction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  transaction  DOCUMENT ME!
     */
    public void setTransaction(final boolean transaction) {
        this.transaction = transaction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tableName  DOCUMENT ME!
     */
    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  columnName  DOCUMENT ME!
     */
    public void setColumnName(final String columnName) {
        this.columnName = columnName;
    }

    @Override
    public boolean equals(final Object obj) {
        return Equals.beanDeepEqual(this, obj);
    }

    @Override
    @SuppressWarnings("PMD.ConfusingTernary")
    public int hashCode() {
        int hash = 7;
        hash = (67 * hash) + ((this.tableName != null) ? this.tableName.hashCode() : 0);
        hash = (67 * hash) + ((this.columnName != null) ? this.columnName.hashCode() : 0);
        hash = (67 * hash) + ((this.description != null) ? this.description.hashCode() : 0);
        hash = (67 * hash) + ((this.warning != null) ? this.warning.hashCode() : 0);
        hash = (67 * hash) + Arrays.deepHashCode(this.statements);
        hash = (67 * hash) + (this.transaction ? 1 : 0);

        return hash;
    }
}
