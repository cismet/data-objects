/*
 * Table.java
 *
 * Created on 14. Februar 2007, 15:22
 */

package de.cismet.diff.container;

import de.cismet.diff.DiffAccessor;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * Simple container class for information about a table in a database
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-08
 */
public class Table
{
  private String tableName;
  private TableColumn[] columns;
  
  /** 
   * Creates a new instance of <code>Table</code>
   *
   * @param tableName may not be null or the empty string
   * @param columns may not be null or zero length
   * @throws IllegalArgumentException if any argument is not valid
   */
  public Table(String tableName, TableColumn[] columns)
  {
    ResourceBundle exceptionBundle = ResourceBundle.getBundle(
      DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
    if(tableName == null || tableName.equals(""))
      throw new IllegalArgumentException(exceptionBundle.getString(
          DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_TABNAME_NULL_OR_EMPTY));
    if(columns == null || columns.length == 0)
      throw new IllegalArgumentException(exceptionBundle.getString(
          DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_COLUMNS_NULL_OR_EMPTY));
    this.tableName = tableName;
    this.columns = columns;
  }
  
  public String getTableName()
  {
    return tableName;
  }
  
  public TableColumn[] getColumns()
  {
    return columns;
  }
  
  public String[] getColumnNames()
  {
    String[] columnNames = new String[columns.length];
    for(int i = 0; i < columns.length; i++)
      columnNames[i] = columns[i].getColumnName();
    return columnNames;
  }
  
  public String getColumnTypeName(String columnName)
  {
    for(int i = 0; i < columns.length; i++)
      if(columns[i].getColumnName().equalsIgnoreCase(columnName))
        return columns[i].getTypeName();
    return null;
  }
  
  public String getDefaultValue(String columnName)
  {
    for(int i = 0; i < columns.length; i++)
      if(columns[i].getColumnName().equalsIgnoreCase(columnName))
        return columns[i].getDefaultValue();
    return null;
  }
  
  public int getNullable(String columnName)
  {
    for(int i = 0; i < columns.length; i++)
      if(columns[i].getColumnName().equalsIgnoreCase(columnName))
        return columns[i].getNullable();
    return -1;
  }
  
  public String[] getPrimaryKeyColumnNames()
  {
    LinkedList<String> keys = new LinkedList<String>();
    for(int i = 0; i < columns.length; i++)
      if(columns[i].isPrimaryKey())
        keys.add(columns[i].getColumnName());
    String[] ret = new String[keys.size()];
    return keys.toArray(ret);
  }
  
  public boolean contains(String columnName)
  {
    return getTableColumn(columnName) != null;
  }
  
  public TableColumn getTableColumn(String columnName)
  {
    for(TableColumn tc : columns)
      if(tc.getColumnName().equalsIgnoreCase(columnName))
        return tc;
    return null;
  }
  
  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    for(TableColumn t : columns)
      sb.append(tableName + " :: " + t + System.getProperty("line.separator"));
    return sb.toString();
  }
}
