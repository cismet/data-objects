/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.container;

import java.util.ResourceBundle;

import de.cismet.diff.DiffAccessor;

/**
 * Simple container class to provide information about a column of a table.
 *
 * @author   Martin Scholl
 * @version  1.0 2007-03-08
 */
public class TableColumn {

    //~ Instance fields --------------------------------------------------------

    private final transient String columnName;
    private final transient String typeName;
    private final transient int precision;
    private final transient int scale;
    private final transient String defaultValue;
    private final transient short nullable;
    private final transient boolean isPrimKey;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>TableColumn.</code>
     *
     * @param   columnName     may not be null or empty string
     * @param   typeName       may not be null or empty string
     * @param   typeSize       precision
     * @param   decimalDigits  DOCUMENT ME!
     * @param   defaultValue   DOCUMENT ME!
     * @param   nullable       implies whether this column is nullable, 0 = true, 1 = false, 2 = unknown
     * @param   isPrimKey      DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  if columnName, typeName or nullable do not contain proper values
     */
    public TableColumn(
            final String columnName,
            final String typeName,
            final int typeSize,
            final int decimalDigits,
            final String defaultValue,
            final short nullable,
            final boolean isPrimKey) {
        final ResourceBundle exceptionBundle = ResourceBundle.getBundle(DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
        if ((columnName == null) || columnName.isEmpty()) {
            throw new IllegalArgumentException(
                exceptionBundle.getString(DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_COLNAME_NULL_OR_EMPTY));
        }
        if ((typeName == null) || typeName.isEmpty()) {
            throw new IllegalArgumentException(
                exceptionBundle.getString(DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_TYPENAME_NULL_OR_EMPTY));
        }
        if ((nullable < 0) || (nullable > 2)) {
            throw new IllegalArgumentException(
                exceptionBundle.getString(DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_NULLABLE_OUT_OF_BOUNDS));
        }
        this.columnName = columnName;
        this.typeName = typeName;
        this.precision = typeSize;
        this.scale = decimalDigits;
        this.defaultValue = defaultValue;
        this.nullable = nullable;
        this.isPrimKey = isPrimKey;
    }

    //~ Methods ----------------------------------------------------------------

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
     * @return  DOCUMENT ME!
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getScale() {
        return scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the default value of this column or <code>null</code> if no default value is assigned
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public short getNullable() {
        return nullable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isPrimaryKey() {
        return isPrimKey;
    }

    @Override
    public String toString() {
        final String colon = " : ";   // NOI18N
        if (isPrimKey) {
            return new String(
                    columnName
                    + colon
                    + typeName
                    + colon
                    + defaultValue
                    + colon
                    + nullable
                    + colon
                    + "primary key"); // NOI18N
        } else {
            return new String(columnName + colon
                    + typeName + colon
                    + defaultValue + colon
                    + nullable);
        }
    }
}
