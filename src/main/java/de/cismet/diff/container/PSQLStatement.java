/*
 * PSQLStatement.java
 *
 * Created on 20. Februar 2007, 12:28
 */

package de.cismet.diff.container;

import de.cismet.diff.DiffAccessor;
import de.cismet.diff.exception.IllegalCodeException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Container class for PSQL statements. This class is able to build correct
 * statements of correct <code>CodedStatements</code> using templates that can
 * be found in the <code>de.cismet.diff.resource</code> package.<br/>
 * You can also build a <code>PSQLStatement</code> manually. In this case there
 * will be no corresponding <code>CodedStatement</code> stored in the class.
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-09
 */
public class PSQLStatement extends Statement
{
  private String statement;
  private String description;
  private CodedStatement codedStatement;
  
  private HashMap<String, String> pedanticStatements;
  
  private ResourceBundle sqlBundle = ResourceBundle.getBundle(
      "de.cismet.diff.resource.psqlTemplate");
  private ResourceBundle descBundle = ResourceBundle.getBundle(
      "de.cismet.diff.resource.psqlTemplateDescription");
  /**
   * Creates a new empty instance of <code>PSQLStatement</code>.
   */
  public PSQLStatement()
  {
    this(null, null, null, false);
  }
  
  /** 
   * Creates a new instance of <code>PSQLStatement</code> using a
   * <code>CodedStatement</code>. Simply calls the 
   * <code>applyCodedStatement</code> method.
   *
   * @param statement the statement which shall be the base of creation of the
   *    <code>PSQLStatement</code> instance
   * @throws IllegalCodeException if the code contained in the 
   *    <code>CodedStatement</code> instance is not know or invalid
   */
  public PSQLStatement(CodedStatement statement) throws
      IllegalCodeException
  {
    super(statement.getWarning(), statement.isPedantic());
    applyCodedStatement(statement);
  }
  
  /**
   * Lets you manually create a new instance of <code>PSQLStatement</code>
   * without using a <code>CodedStatement</code> as base. You may provide your
   * desired statement, the description and the warning.
   *
   * @param statement a string represending a SQL statement
   * @param description a custom prose description of what the provided
   *    statement does
   * @param warning a string indicating a warning that can indicate that the
   *    action the statement tries to perform may fail etc.
   */
  public PSQLStatement(String statement, String description, String warning,
      boolean pedantic)
  {
    super(warning, pedantic);
    this.statement = statement;
    this.description = description;
    this.codedStatement = null;
  }
  
  private void initPedanticStatements()
  {
    pedanticStatements = new HashMap<String, String>();
    pedanticStatements.put(CodedStatement.CODE_ALTER_COLUMN_SET, "NOT NULL");
  }
  
  /**
   * You can use this method to find out whether this instance has been create 
   * from a <code>CodedStatement</code> or manually.
   *
   * @return the <code>CodedStatement</code> this instance of 
   *    <code>PSQLStatement</code> was build of or null if it has been created
   *    manually.
   */
  public CodedStatement getCodedStatement()
  {
    return codedStatement;
  }
  
  /**
   * Tries to use the information the given <code>CodedStatement</code> provides
   * to build a PSQL statement with a corresponding description and a warning if
   * it is also provided.
   * 
   * @param codedStatement the statement providing the information
   */
  public void applyCodedStatement(CodedStatement codedStatement) throws
      IllegalCodeException
  {
    ResourceBundle exceptionBundle = ResourceBundle.getBundle(
        DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
    String[] args = codedStatement.getArgs();
    try
    {
      String code = codedStatement.getCode();
      MessageFormat sqlform = new MessageFormat(sqlBundle.getString(
          code));
      MessageFormat descform = new MessageFormat(descBundle.getString(
          code));
      this.statement = sqlform.format(args);
      this.description = descform.format(args);
    }
    catch(MissingResourceException ex)
    {
      throw new IllegalCodeException(exceptionBundle.getString(
          DiffAccessor.ILLEGAL_CODE_EXCEPTION_CODE_NOT_KNOWN),
          codedStatement.getCode());
    }
    try
    {
      MessageFormat warnform = null;
      if(codedStatement.getWarning() != null)
        warnform = new MessageFormat(descBundle.getString(
            codedStatement.getWarning()));
      if(warnform != null)
        this.warning = warnform.format(args);
      else
        this.warning = null;
    }
    catch(MissingResourceException ex)
    {
      throw new IllegalCodeException(exceptionBundle.getString(
          DiffAccessor.ILLEGAL_CODE_EXCEPTION_WARNING_NOT_KNOWN),
          codedStatement.getWarning());
    }
    this.codedStatement = codedStatement;
  }
  
  public String getStatement()
  {
    return statement;
  }
  
  /**
   * Sets a new statement. Using this method will cause the
   * <code>CodedStatement</code> held by this <code>PSQLStatement</code> to be 
   * set to <code>null</code>.
   *
   * @param statement the new statement
   */
  public void setStatement(String statement)
  {
    this.codedStatement = null;
    this.pedantic = false;
    this.statement = statement;
  }
  
  public String getDescription()
  {
    return description;
  }
  
  /**
   * Sets a new statement description. Using this method will cause the 
   * <code>CodedStatement</code> held by this <code>PSQLStatement</code> to be 
   * set to <code>null</code>.
   *
   * @param description the new description of the statement
   */
  public void setDescription(String description)
  {
    this.codedStatement = null;
    this.description = description;
  }
  
  /**
   * Sets pedantic. Using this method will cause the 
   * <code>CodedStatement</code> held by this <code>PSQLStatement</code> to be 
   * set to <code>null</code>.
   *
   * @param pedantic
   */
  public void setPedantic(boolean pedantic)
  {
    this.codedStatement = null;
    this.pedantic = pedantic;
  }
  
  public String toString()
  {
    if(warning != null)
      return statement + " :: " + description + " :: " + warning;
    else
      return statement + " :: " + description;
  }
}
