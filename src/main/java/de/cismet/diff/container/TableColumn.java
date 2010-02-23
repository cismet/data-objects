/*
 * TableColumn.java
 *
 * Created on 14. Februar 2007, 11:05
 */

package de.cismet.diff.container;

import de.cismet.diff.DiffAccessor;
import java.util.ResourceBundle;

/**
 * Simple container class to provide information about a column of a table.
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-08
 */
public class TableColumn
{
  private String columnName;
  private String typeName;
  private int precision;
  private int scale;
  private String defaultValue;
  private short nullable;
  private boolean isPrimKey;
  
  /**
   * 
   * Creates a new instance of <code>TableColumn</code>
   * 
   * 
   * @param columnName may not be null or empty string
   * @param typeName may not be null or empty string
   * @param precision
   * @param defaultValue
   * @param nullable implies whether this column is nullable,
   *    0 = true, 1 = false, 2 = unknown
   * @param isPrimKey
   * @throws IllegalArgumentException if columnName, typeName or nullable do not
   *    contain proper values
   */
  public TableColumn(String columnName, String typeName, int typeSize,
      int decimalDigits, String defaultValue, short nullable, boolean isPrimKey)
  {
    ResourceBundle exceptionBundle = ResourceBundle.getBundle(
      DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
    if(columnName == null || columnName.equals(""))
      throw new IllegalArgumentException(exceptionBundle.getString(
          DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_COLNAME_NULL_OR_EMPTY));
    if(typeName == null || typeName.equals(""))
      throw new IllegalArgumentException(exceptionBundle.getString(
          DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_TYPENAME_NULL_OR_EMPTY));
    if(nullable < 0 || nullable > 2)
      throw new IllegalArgumentException(exceptionBundle.getString(
          DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_NULLABLE_OUT_OF_BOUNDS));
    this.columnName = columnName;
    this.typeName = typeName;
    this.precision = typeSize;
    this.scale = decimalDigits;
    this.defaultValue = defaultValue;
    this.nullable = nullable;
    this.isPrimKey = isPrimKey;
  }
  
  public String getColumnName()
  {
    return columnName;
  }
  
  public String getTypeName()
  {
    return typeName;
  }

  public int getPrecision()
  {
    return precision;
  }

  public int getScale()
  {
    return scale;
  }
  
  /**
   * @return the default value of this column or <code>null</code> if no default
   *    value is assigned
   */
  public String getDefaultValue()
  {
    return defaultValue;
  }
  
  public short getNullable()
  {
    return nullable;
  }
  
  public boolean isPrimaryKey()
  {
    return isPrimKey;
  }
  
  public String toString()
  {
    if(isPrimKey)
      return new String(columnName   + " : " +
                        typeName     + " : " +
                        defaultValue + " : " +
                        nullable     + " : " +
                        "primary key");
    else
      return new String(columnName   + " : " +
                        typeName     + " : " +
                        defaultValue + " : " +
                        nullable);
  }
}
