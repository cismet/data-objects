/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.container;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.ResourceBundle;

import de.cismet.diff.DiffAccessor;

/**
 * Simple container class for information about a table in a database.
 *
 * @author   Martin Scholl
 * @version  1.0 2007-03-08
 */
public class Table {

    //~ Instance fields --------------------------------------------------------

    private final transient String tableName;
    private final transient TableColumn[] columns;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>Table.</code>
     *
     * @param   tableName  may not be null or the empty string
     * @param   columns    may not be null or zero length
     *
     * @throws  IllegalArgumentException  if any argument is not valid
     */
    public Table(final String tableName, final TableColumn[] columns) {
        final ResourceBundle exceptionBundle = ResourceBundle.getBundle(DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
        if ((tableName == null) || tableName.isEmpty()) {
            throw new IllegalArgumentException(
                exceptionBundle.getString(DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_TABNAME_NULL_OR_EMPTY));
        }
        if ((columns == null) || (columns.length == 0)) {
            throw new IllegalArgumentException(
                exceptionBundle.getString(DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_COLUMNS_NULL_OR_EMPTY));
        }
        this.tableName = tableName;
        this.columns = Arrays.copyOf(columns, columns.length);
    }

    //~ Methods ----------------------------------------------------------------

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
     * @return  DOCUMENT ME!
     */
    public TableColumn[] getColumns() {
        return Arrays.copyOf(columns, columns.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String[] getColumnNames() {
        final String[] columnNames = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            columnNames[i] = columns[i].getColumnName();
        }
        return columnNames;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   columnName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getColumnTypeName(final String columnName) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
                return columns[i].getTypeName();
            }
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   columnName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDefaultValue(final String columnName) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
                return columns[i].getDefaultValue();
            }
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   columnName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getNullable(final String columnName) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getColumnName().equalsIgnoreCase(columnName)) {
                return columns[i].getNullable();
            }
        }
        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String[] getPrimaryKeyColumnNames() {
        final LinkedList<String> keys = new LinkedList<String>();
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].isPrimaryKey()) {
                keys.add(columns[i].getColumnName());
            }
        }
        return keys.toArray(new String[keys.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   columnName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean contains(final String columnName) {
        return getTableColumn(columnName) != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   columnName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public TableColumn getTableColumn(final String columnName) {
        for (final TableColumn tc : columns) {
            if (tc.getColumnName().equalsIgnoreCase(columnName)) {
                return tc;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (final TableColumn t : columns) {
            sb.append(tableName).append(" :: ").append(t).append(System.getProperty("line.separator")); // NOI18N
        }
        return sb.toString();
    }
}
