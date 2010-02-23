/*
 * StatementGroup.java
 *
 * Created on 16. März 2007, 10:17
 */

package de.cismet.diff.container;

/**
 * @author Martin Scholl
 * @version 1.0
 */
public class StatementGroup
{
  public static final String GROUP_DESC_UPDATE_AND_NOT_NULL = 
      "group_desc_update_and_notnull";
  public static final String GROUP_DESC_NEW_TABLE = 
      "group_desc_new_table";
  public static final String GROUP_DESC_CONVERT_TYPE = 
      "group_desc_convert_type";
  public static final String GROUP_DESC_PRIM_KEY_FIT =
      "group_desc_prim_key_fit";
  
  protected String tableName;
  protected String columnName;
  protected String description;
  protected String warning;
  protected Statement[] statements;
  protected boolean transaction;
  
  /**
   * Creates a new instance of <code>StatementGroup</code>
   */
  public StatementGroup(Statement[] statements, boolean transaction)
  {
    this.transaction = transaction;
    this.statements = statements;
    this.warning = null;
    this.description = null;
    this.tableName = null;
    this.columnName = null;
  }
  
  public Statement[] getStatements()
  {
    return statements;
  }

  public void setStatements(Statement[] statements)
  {
    this.statements = statements;
  }
 
  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getWarning()
  {
    return warning;
  }

  public void setWarning(String warning)
  {
    this.warning = warning;
  }

  public boolean isTransaction()
  {
    return transaction;
  }

  public void setTransaction(boolean transaction)
  {
    this.transaction = transaction;
  }

  public String getTableName()
  {
    return tableName;
  }

  public void setTableName(String tableName)
  {
    this.tableName = tableName;
  }

  public String getColumnName()
  {
    return columnName;
  }

  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }
}
