/*
 * PSQLStatementGroup.java
 *
 * Created on 16. März 2007, 11:21
 */

package de.cismet.diff.container;

import de.cismet.diff.exception.IllegalCodeException;

/**
 * @author Martin Scholl
 * @version 1.0
 */
public class PSQLStatementGroup extends StatementGroup
{
  private PSQLStatement[] psqlStatements;
  
  public PSQLStatementGroup(StatementGroup group) throws
      IllegalCodeException
  {
    this(group.getStatements(), group.isTransaction());
    this.description = group.getDescription();
    this.tableName = group.getTableName();
    this.columnName = group.getColumnName();
    this.warning = group.getWarning();
  }
  
  /**
   * Creates a new instance of <code>PSQLStatementGroup</code>
   */
  public PSQLStatementGroup(Statement[] statements, boolean transaction) throws
      IllegalCodeException
  {
    super(statements, transaction);
    applyStatements(statements);
    if(transaction)
    {
      PSQLStatement[] tmp = new PSQLStatement[statements.length + 2];
      tmp[0] = new PSQLStatement(
        "BEGIN WORK;", null, null, false);
      tmp[tmp.length - 1] = new PSQLStatement(
        "COMMIT WORK;", null, null, false);
      for(int i = 0; i < psqlStatements.length; i++)
        tmp[i+1] = psqlStatements[i];
      psqlStatements = tmp;
    }
  }
  
  private void applyStatements(Statement[] statements) throws 
      IllegalCodeException
  {
    psqlStatements = new PSQLStatement[statements.length];
    for(int i = 0; i < statements.length; i++)
    {
      if(statements[i] instanceof CodedStatement)
        psqlStatements[i] = new PSQLStatement((CodedStatement)statements[i]);
      else if(statements[i] instanceof PSQLStatement)
        psqlStatements[i] = (PSQLStatement)statements[i];
      else
        throw new IllegalArgumentException("Instance not known");
    }
  }
  
  public PSQLStatement[] getPSQLStatements()
  {
    return psqlStatements;
  }
}
