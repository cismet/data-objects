/*
 * ScriptGenerator.java
 *
 * Created on 20. Februar 2007, 12:25
 */

package de.cismet.diff.builder;

import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.Type;
import de.cismet.diff.DiffAccessor;
import de.cismet.diff.container.Action;
import de.cismet.diff.container.CodedStatement;
import de.cismet.diff.container.PSQLStatementGroup;
import de.cismet.diff.container.Statement;
import de.cismet.diff.container.StatementGroup;
import de.cismet.diff.container.Table;
import de.cismet.diff.container.TableColumn;
import de.cismet.diff.db.DatabaseConnection;
import de.cismet.diff.exception.IllegalCodeException;
import de.cismet.diff.exception.ScriptGeneratorException;
import de.cismet.diff.util.ProgressionQueue;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;


/**
 * This class can generate an array of <code>PSQLStatements</code> that contain
 * the statements required to adjust the state of the tables in a database to
 * the state stored in the "cids" system tables. The state in the "cids" system
 * tables is always decisive.
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-09
 */
public class ScriptGenerator
{
  private final String TMP_COLUMN = "swapper_tmp_" + 
      System.currentTimeMillis();
  private final Logger log = Logger.getLogger(this.getClass());
  
  private Table[] allTables;
  private LinkedList<Table> tables;
  private LinkedList<CidsClass> classes;
  private LinkedList<CidsClass> classesDone;
  
  private LinkedList<String> callStack;
  
  private LinkedList<StatementGroup> statements;
  private PSQLStatementGroup[] psqlStatementGroups;
  private HashMap<String, String> typemapCidsToPSQL;
  private HashMap<String, String> typemapPSQLtoCids;
  private ProgressionQueue queue;
  private Properties runtime;
  
  private ResourceBundle exceptionBundle = ResourceBundle.getBundle(
        DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
  private ResourceBundle descBundle = ResourceBundle.getBundle(
      "de.cismet.diff.resource.psqlTemplateDescription");
  
  public ScriptGenerator(Properties runtime, TableLoader t)
  {
    this(runtime, t.getTables(), t.getClasses(), null);
  }
  
  public ScriptGenerator(Properties runtime, TableLoader t, 
      ProgressionQueue storage)
  {
    this(runtime, t.getTables(), t.getClasses(), storage);
  }
  
  public ScriptGenerator(Properties runtime, Table[] tables, 
      CidsClass[] classes)
  {
    this(runtime, tables, classes, null);
  }
  
  public ScriptGenerator(Properties runtime, Table[] tables, 
      CidsClass[] classes, ProgressionQueue storage)
  {
    if(tables == null || tables.length == 0)
      throw new IllegalArgumentException(exceptionBundle.getString(
          DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_TABLES_NULL_OR_EMPTY));
    if(classes == null || classes.length == 0)
      throw new IllegalArgumentException(exceptionBundle.getString(
          DiffAccessor.ILLEGAL_ARGUMENT_EXCEPTION_CLASSES_NULL_OR_EMPTY));
    this.allTables = tables;
    this.tables = new LinkedList<Table>();
    for(Table t : tables)
      if(t != null)
        this.tables.add(t);
    this.classes = new LinkedList<CidsClass>();
    for(CidsClass c : classes)
      if(c != null)
        this.classes.add(c);
    classesDone = new LinkedList<CidsClass>();
    typemapCidsToPSQL = getTypeMap(true);
    typemapPSQLtoCids = getTypeMap(false);
    statements = new LinkedList<StatementGroup>();
    this.queue = storage;
    this.callStack = new LinkedList<String>();
    psqlStatementGroups = null;
    this.runtime = runtime;
  }
  
  private HashMap<String, String> getTypeMap(boolean directionTo)
  {
    ResourceBundle bundle = ResourceBundle.getBundle(
        "de.cismet.diff.resource.typemap");
    HashMap<String, String> map = new HashMap<String, String>();
    Enumeration<String> keys = bundle.getKeys();
    while(keys.hasMoreElements())
    {
      String key = keys.nextElement();
      // for bidirectional mapping
      if(directionTo)
        map.put(key, bundle.getString(key));
      else
        map.put(bundle.getString(key), key);
    }
    return map;
  }
  
  public PSQLStatementGroup[] getStatementGroups() throws
      ScriptGeneratorException
  {
    if(psqlStatementGroups != null)
      return psqlStatementGroups;
    while(classes.size() > 0)
    {
      callStack.clear();
      LinkedList<StatementGroup> s = createStatements(classes.getFirst());
      if(s != null)
        statements.addAll(s);
    }
    while(tables.size() > 0)
    {
      callStack.clear();
      StatementGroup s = create_DROP_statement(tables.getFirst());
      if(s != null)
        statements.addLast(s);
    }
    LinkedList<PSQLStatementGroup> psql = new LinkedList<PSQLStatementGroup>();
    try
    {
      while(statements.size() > 0)
      {
        StatementGroup current = statements.removeFirst();
        if(current != null)
          psql.addLast(new PSQLStatementGroup(current));
      }
    }
    catch(IllegalCodeException ex)
    {
      System.out.println(ex.getCode());
      throw new ScriptGeneratorException(exceptionBundle.getString(
          DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_EXC_STATEMENT_CREATE), ex);
    }
    psqlStatementGroups = new PSQLStatementGroup[psql.size()];
    return psqlStatementGroups = psql.toArray(psqlStatementGroups);
  }
  
  private LinkedList<StatementGroup> createStatements(CidsClass c) throws
      ScriptGeneratorException
  {
    if(c == null)
      return null;
    Table t = getTable(c.getTableName());
    if(t != null)
      return create_ALTER_Statements(c, t);
    return create_CREATE_Statement(c);
  }
  
  private LinkedList<StatementGroup> create_ALTER_Statements(
      CidsClass c, Table t) throws
      ScriptGeneratorException
  {
    Iterator<Attribute> it = c.getAttributes().iterator();
    LinkedList<TableColumn> tableColumns = new LinkedList<TableColumn>();
    LinkedList<StatementGroup> statementGroups = new LinkedList<
        StatementGroup>();
    for(TableColumn tc : t.getColumns())
      tableColumns.add(tc);
    // add primary key statements
    StatementGroup primkeyGroup = createPrimaryKeyStatements(t, c);
    if(primkeyGroup != null)
      statementGroups.addLast(primkeyGroup);
    while(it.hasNext())
    {
      String warning = null;
      Attribute current = it.next();
      String attrName = current.getFieldName();
      Type attrType = current.getType();
      String attrTypeName = attrType.getName().toLowerCase();
      if(typemapCidsToPSQL.containsKey(attrTypeName))
        attrTypeName = typemapCidsToPSQL.get(attrTypeName);
      // column is present?
      TableColumn column = t.getTableColumn(attrName);
      // yes, it is, look for differences
      if(column != null)
      {
        // <editor-fold defaultstate="collapsed" desc=" handle complex type ">
        if(attrType.isComplexType())
        {
          // checks whether this type is already present or tries to create it
          // by calling the createStatements method with the name of the type as
          // param. the name is also added to a call stack to prevent endless
          // loops in case of cyclic relation between two or more classes.
          // type is still not present after creation try, a 
          // ScriptGeneratorException will be thrown.
          if(!(classesDone.contains(attrType.getCidsClass()) ||
              callStack.contains(attrType.getName())))
          {
            callStack.add(attrType.getName());
            LinkedList<StatementGroup> s = createStatements(attrType.
                    getCidsClass());
            if(s != null)
              statementGroups.addAll(s);
            if(!classesDone.contains(attrType.getCidsClass()))
              throw new ScriptGeneratorException(exceptionBundle.getString(
                  DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_COMP_TYPE),
                  t.getTableName(), attrName, attrTypeName,
                  null);
          }
          if(!column.getTypeName().equalsIgnoreCase("int4"))
            statementGroups.addLast(createTypeConversionStatements(
                t, current, "INTEGER"));
        }// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc=" handle normal type ">
        else
        {
          // type mismatch
          if((!attrTypeName.equalsIgnoreCase(column.getTypeName())) || (
              current.getPrecision() != null && 
              current.getPrecision().intValue() != column.getPrecision()) ||
              current.getScale() != null &&
              current.getScale().intValue() != column.getScale())
          {
            // this check has to be added since postgres jdbc driver 8 and later
            // handles columns, that are of type int or bigint and have not null 
            // constraint as well as a sequence as their default value, as 
            // serial types. serial types basically are integers
            if((attrTypeName.equalsIgnoreCase("int4") ||
                attrTypeName.equalsIgnoreCase("int8")) && 
                // it is already interpreted as serial if there is a sequence
                // present, maybe I misunderstood the description..... -.-
                //column.getNullable() == DatabaseMetaData.attributeNoNulls &&
                column.getDefaultValue() != null &&
                column.getDefaultValue().startsWith("nextval"))
            {
              if(attrTypeName.equalsIgnoreCase("int4"))
              {
                if(!column.getTypeName().equalsIgnoreCase("serial"))
                  statementGroups.addLast(createTypeConversionStatements(
                      t, current, null));
              }
              else
              {
                if(!column.getTypeName().equalsIgnoreCase("bigserial"))
                  statementGroups.addLast(createTypeConversionStatements(
                      t, current, null));
              }
            }
            else
            {
              statementGroups.addLast(createTypeConversionStatements(
                  t, current, null));
            }
          }
            
        }// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc=" handle normal attr ">
        if(!current.getFieldName().equalsIgnoreCase(c.getPrimaryKeyField()))
        {
          // <editor-fold defaultstate="collapsed" desc=" drop default value ">
          if(current.getDefaultValue() == null &&
              t.getDefaultValue(attrName) != null)
          {
            Statement[] s = {new CodedStatement(
                CodedStatement.CODE_ALTER_COLUMN_DROP, null, false,
                t.getTableName(), attrName.toLowerCase(), "DEFAULT")};
            statementGroups.addLast(new StatementGroup(s, false));
          }
          // </editor-fold>
          // <editor-fold defaultstate="collapsed" desc=" set default value ">
          String defaultVal = t.getDefaultValue(attrName);
          if(defaultVal != null)
          {
            int i = defaultVal.indexOf("'") + 1;
            int j = defaultVal.lastIndexOf("'");
            if(i > 0 && j > i)
              defaultVal = defaultVal.substring(i, j);
          }
          if(current.getDefaultValue() != null &&
              !current.getDefaultValue().equals(defaultVal))
          {
            if(!isDefaultValueValid(attrName, attrTypeName, current.
                getPrecision(), current.getScale(), current.getDefaultValue()))
              throw new ScriptGeneratorException(exceptionBundle.getString(
                  DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_DEF_TYPE_MISMATCH),
                  t.getTableName(), attrName, null);
            Statement[] s = {new CodedStatement(
                CodedStatement.CODE_ALTER_COLUMN_SET, null, false,
                t.getTableName(), attrName.toLowerCase(), 
                "DEFAULT '" + current.getDefaultValue() + "'")};
            statementGroups.addLast(new StatementGroup(s, false));
          }// </editor-fold>
          // <editor-fold defaultstate="collapsed" desc=" alter column to 'optional' ">
          if(current.isOptional() && !(column.getNullable() ==
              DatabaseMetaData.attributeNullable))
          {
            if(column.getColumnName().equalsIgnoreCase(c.getPrimaryKeyField()))
              throw new ScriptGeneratorException(exceptionBundle.getString(
                  DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_NOT_NULLABLE),
                  t.getTableName(), attrName, null);
            Statement[] s = {new CodedStatement(
                CodedStatement.CODE_ALTER_COLUMN_DROP, null, false,
                t.getTableName(), attrName.toLowerCase(), "NOT NULL")};
            statementGroups.addLast(new StatementGroup(s, false));
          }// </editor-fold>
          // <editor-fold defaultstate="collapsed" desc=" alter column to 'required' ">
          if(!current.isOptional() && !(column.getNullable() ==
              DatabaseMetaData.attributeNoNulls))
          {
            if(current.getDefaultValue() != null)
            {
              if(!isDefaultValueValid(attrName, attrTypeName, current.
                  getPrecision(), current.getScale(),current.getDefaultValue()))
              throw new ScriptGeneratorException(exceptionBundle.getString(
                  DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_DEF_TYPE_MISMATCH),
                  t.getTableName(), attrName, null);
              Statement[] s = {
                  new CodedStatement(
                      CodedStatement.CODE_UPDATE_WHERE_NULL, null, false,
                      t.getTableName(), attrName.toLowerCase(),
                      current.getDefaultValue()), 
                  new CodedStatement(
                      CodedStatement.CODE_ALTER_COLUMN_SET, null, false,
                      t.getTableName(), attrName.toLowerCase(), "NOT NULL")};
              StatementGroup group = new StatementGroup(s, true);
              MessageFormat descform = new MessageFormat(descBundle.getString(
                  StatementGroup.GROUP_DESC_UPDATE_AND_NOT_NULL));
              String[] args = {t.getTableName(), attrName.toLowerCase()};
              group.setDescription(descform.format(args));
              group.setTableName(t.getTableName());
              group.setColumnName(attrName.toLowerCase());
              statementGroups.addLast(group);
            }
            else
            {
              Statement[] s = {new CodedStatement(
                  CodedStatement.CODE_ALTER_COLUMN_SET,
                  CodedStatement.WARNING_ALTER_COLUMN_TO_NOTNULL, true,
                  t.getTableName(), attrName.toLowerCase(), "NOT NULL")};
              statementGroups.addLast(new StatementGroup(s, false));
            }
          }// </editor-fold>
        }// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc=" handle primary key ">
        else
        {
          String defVal = column.getDefaultValue();
          // <editor-fold defaultstate="collapsed" desc=" set default 'nextval' ">
          if(defVal == null || !(
              // this string represents postgres jdbc 7 drivers
              defVal.equalsIgnoreCase("nextval('" + t.getTableName() + 
              "_seq'::text)") || 
              // this string represents postgres jdbc 8 drivers
              defVal.equalsIgnoreCase("nextval('" + t.getTableName() + 
              "_seq'::regclass)") ))
          {
            Statement[] s = {new CodedStatement(
                CodedStatement.CODE_ALTER_COLUMN_SET, null, false,
                t.getTableName(), attrName.toLowerCase(),
                "DEFAULT nextval('" + t.getTableName() + "_seq')")};
            statementGroups.addLast(new StatementGroup(s, false));
          }// </editor-fold>
        }// </editor-fold>
        tableColumns.remove(column);
      }
      // column not present, create new column
      else
      {
        // <editor-fold defaultstate="collapsed" desc=" handle complex type ">
        if(attrType.isComplexType())
        {
          // checks whether this type is already present or tries to create it
          // by calling the createStatements method with the name of the type as
          // param. the name is also added to a call stack to prevent endless
          // loops in case of cyclic relation between two or more classes.
          // type is still not present after creation try, a 
          // ScriptGeneratorException will be thrown.
          if(!(classesDone.contains(attrType.getCidsClass()) ||
              callStack.contains(attrType.getName())))
          {
            callStack.add(attrType.getName());
            LinkedList<StatementGroup> s = createStatements(attrType.
                getCidsClass());
            if(s != null)
              statementGroups.addAll(s);
            if(!classesDone.contains(attrType.getCidsClass()))
              throw new ScriptGeneratorException(exceptionBundle.getString(
                  DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_COMP_TYPE),
                  t.getTableName(), attrName, null);
          }
          // add statement to statementlist
          Statement[] s = {new CodedStatement(
              CodedStatement.CODE_ALTER_ADD_COLUMN, warning, false,
              t.getTableName(), attrName.toLowerCase(), "INTEGER")};
          statementGroups.addLast(new StatementGroup(s, false));
        }// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc=" handle normal type ">
        else
        {
          // add precision to parameter if present
          String paramAttrType = attrTypeName;
          // bpchar is just internal and cannot be used to create new columns :(
          // --> remapping to char
          if(paramAttrType.equals("bpchar"))
              paramAttrType = "char";
          if(current.getPrecision() != null)
          {
            // also add scale to parameter if present
            if(current.getScale() != null)
            {
                paramAttrType = paramAttrType + "(" + current.getPrecision() +
                        ", " + current.getScale() + ")";
            }
            else
            {
                paramAttrType = paramAttrType + "(" + current.getPrecision() + 
                        ")";
            }
          }
          Statement[] s = {new CodedStatement(
              CodedStatement.CODE_ALTER_ADD_COLUMN, null, false,
              t.getTableName(), attrName, paramAttrType)};
          statementGroups.addLast(new StatementGroup(s, false));
        }// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc=" add default if present ">
        if(current.getFieldName().equalsIgnoreCase(c.getPrimaryKeyField()))
        {
          Statement[] s = {new CodedStatement(
              CodedStatement.CODE_ALTER_COLUMN_SET, null, false,
              t.getTableName(), attrName.toLowerCase(),
              "DEFAULT nextval('" + t.getTableName() + "_seq')")};
          statementGroups.addLast(new StatementGroup(s, false));
        }
        else if(current.getDefaultValue() != null)
        {
          if(!isDefaultValueValid(attrName, attrTypeName, current.getPrecision(
              ), current.getScale(), current.getDefaultValue()))
            throw new ScriptGeneratorException(exceptionBundle.getString(
                DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_DEF_TYPE_MISMATCH),
                t.getTableName(), attrName, null);
          Statement[] s = {new CodedStatement(
              CodedStatement.CODE_ALTER_COLUMN_SET, null, false,
              t.getTableName(), attrName.toLowerCase(),
              "DEFAULT '" + current.getDefaultValue() + "'")};
          statementGroups.addLast(new StatementGroup(s, false));
        }// </editor-fold>
        // <editor-fold defaultstate="collapsed" desc=" set not null if needed ">
        if(!current.isOptional())
        {
          // default value is valid if present due to check above
          if(current.getDefaultValue() != null)
          {
            Statement[] s = {
                new CodedStatement(
                    CodedStatement.CODE_UPDATE_WHERE_NULL, null, false,
                    t.getTableName(), attrName.toLowerCase(),
                    current.getDefaultValue()),
                new CodedStatement(
                    CodedStatement.CODE_ALTER_COLUMN_SET, null, false,
                    t.getTableName(), attrName.toLowerCase(), "NOT NULL")};
            StatementGroup group = new StatementGroup(s, true);
            MessageFormat descform = new MessageFormat(descBundle.getString(
                StatementGroup.GROUP_DESC_UPDATE_AND_NOT_NULL));
            String[] args = {t.getTableName(), attrName.toLowerCase()};
            group.setDescription(descform.format(args));
            group.setTableName(t.getTableName());
            group.setColumnName(attrName.toLowerCase());
            statementGroups.addLast(group);
          }
          else
          {
            Statement[] s = {new CodedStatement(
                CodedStatement.CODE_ALTER_COLUMN_SET,
                CodedStatement.WARNING_ALTER_COLUMN_TO_NOTNULL, true,
                t.getTableName(), attrName.toLowerCase(), "NOT NULL")};
            statementGroups.addLast(new StatementGroup(s, false));
          }
        }// </editor-fold>
      }
    }
    // <editor-fold defaultstate="collapsed" desc=" drop columns that are not in "cs_attr" anymore ">
    while(tableColumns.size() > 0)
    {
      if(tableColumns.getFirst().getColumnName().equalsIgnoreCase(
          c.getPrimaryKeyField()))
        throw new ScriptGeneratorException(exceptionBundle.getString(
                DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NO_DROP),
                t.getTableName(), c.getPrimaryKeyField(), null);
      Statement[] s = {new CodedStatement(
          CodedStatement.CODE_ALTER_DROP_COLUMN, null, false,
          t.getTableName(), tableColumns.removeFirst().getColumnName())};
      statementGroups.addLast(new StatementGroup(s, false));
    }// </editor-fold>
    tables.remove(t);
    classes.remove(c);
    classesDone.add(c);
    return statementGroups;
  }
  
  private LinkedList<StatementGroup> create_CREATE_Statement(CidsClass c) throws
      ScriptGeneratorException
  {
    // is name valid
    if(c.getTableName().contains(" "))
      throw new ScriptGeneratorException(exceptionBundle.getString(
          DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_TABLENAME_HAS_SPACES),
          c.getTableName(), null);
    // is primary key present and valid
    try
    {
      if(c.getPrimaryKeyField().equals(""))
        throw new ScriptGeneratorException(exceptionBundle.getString(
            DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_EMPTY_PRIMKEY_FIELD),
            c.getTableName(), null);
    }
    catch(NullPointerException npe)
    {
      throw new ScriptGeneratorException(exceptionBundle.getString(
          DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_PRIMKEY_FIELD),
          c.getTableName(), npe);
    }
    LinkedList<Statement> statem = new LinkedList<Statement>();
    HashMap<String, String> map = new HashMap<String, String>();
    map.put(CodedStatement.KEY_TABLENAME, c.getTableName().toLowerCase());
    StringBuffer nameTypeEnum = new StringBuffer();
    Iterator<Attribute> it = c.getAttributes().iterator();
    String warning = null;
    boolean primarykeyFound = false;
    while(it.hasNext())
    {
      Attribute current = it.next();
      String name = current.getFieldName().toLowerCase();
      Type type = current.getType();
      if(type.isComplexType())
      {
        // checks whether this type is already present or tries to create it
        // by calling the createStatements method with the name of the type as
        // param. the name is also added to a call stack to prevent endless
        // loops in case of cyclic relation between two or more classes.
        // type is still not present after creation try, a 
        // ScriptGeneratorException will be thrown.
        if(!(classesDone.contains(type.getCidsClass()) ||
              callStack.contains(type.getName())))
        {
          callStack.add(type.getName());
          LinkedList<StatementGroup> s = createStatements(type.getCidsClass());
          if(s != null)
            statements.addAll(s);
          if(!classesDone.contains(type.getCidsClass()))
            throw new ScriptGeneratorException(exceptionBundle.getString(
                  DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_COMP_TYPE),
                  c.getTableName().toLowerCase(), name, type.getName(), null);
        }
        if(current.isOptional())
          nameTypeEnum.append(name).append(" INTEGER NULL");
        else
          nameTypeEnum.append(name).append(" INTEGER NOT NULL");
      }
      // handle primary key
      // primary key is always integer and has a sequence as default value
      // the sequence will be created and is named: '<tablename>_seq'
      else if(name.equalsIgnoreCase(c.getPrimaryKeyField()))
      {
        primarykeyFound = true;
        if(current.isOptional())
          throw new ScriptGeneratorException(exceptionBundle.getString(
              DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NOT_NULL),
              c.getTableName().toLowerCase(), c.getPrimaryKeyField().
              toLowerCase(), null);
        if(!type.getName().equalsIgnoreCase("INTEGER"))
          throw new ScriptGeneratorException(exceptionBundle.getString(
                  DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_NOT_INTEGER),
                  c.getTableName().toLowerCase(), name, type.getName(), null);
        nameTypeEnum.append(name).append(" INTEGER");
        nameTypeEnum.append(" PRIMARY KEY DEFAULT nextval('").
            append(c.getTableName().toLowerCase()).append("_seq')");
        if(!sequenceExists(c.getTableName()))
            statem.addFirst(new CodedStatement(
                    CodedStatement.CODE_CREATE_SEQUENCE,
                    null, false, c.getTableName().toLowerCase() + "_seq", "1"));
      }
      else
      {
        nameTypeEnum.append(name).append(" ").append(type.getName().toUpperCase(
            ));
        if(current.getPrecision() != null)
        {
          nameTypeEnum.append("(").append(current.getPrecision());
          if(current.getScale() != null)
            nameTypeEnum.append(", " + current.getScale());
          nameTypeEnum.append(")");
        }
        if(!current.isOptional())
          nameTypeEnum.append(" NOT");
        nameTypeEnum.append(" NULL");
      }
      // append default value if exists
      if(current.getDefaultValue() != null && !name.equalsIgnoreCase(
          c.getPrimaryKeyField()))
      {
        if(!isDefaultValueValid(name, type.getName(), current.getPrecision(),
            current.getScale(), current.getDefaultValue()))
          throw new ScriptGeneratorException(exceptionBundle.getString(
                  DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_DEF_TYPE_MISMATCH),
                  c.getTableName().toLowerCase(), name, null);
        nameTypeEnum.append(" DEFAULT '" + current.getDefaultValue() + "'");
      }
      nameTypeEnum.append(", ");
    }
    if(primarykeyFound == false)
      throw new ScriptGeneratorException(exceptionBundle.getString(
          DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_PRIMKEY_FIELD),
          c.getTableName(), null);
    nameTypeEnum.delete(nameTypeEnum.length() - 2, nameTypeEnum.length());
    map.put(CodedStatement.KEY_NAME_TYPE_ENUM, nameTypeEnum.toString());
    classes.remove(c);
    classesDone.add(c);
    statem.addLast(new CodedStatement(
            CodedStatement.CODE_CREATE_STANDARD, warning, 
            false, map));
    Statement[] s = new Statement[statem.size()];
    s = statem.toArray(s);
    StatementGroup group = new StatementGroup(s, true);
    group.setTableName(c.getTableName().toLowerCase());
    MessageFormat descform = new MessageFormat(descBundle.getString(
        StatementGroup.GROUP_DESC_NEW_TABLE));
    String[] args = {c.getTableName().toLowerCase()};
    group.setDescription(descform.format(args));
    LinkedList<StatementGroup> l = new LinkedList<StatementGroup>();
    l.add(group);
    return l;
  }
  
  private StatementGroup create_DROP_statement(Table t)
  {
    HashMap<String, String> map = new HashMap<String, String>();
    map.put(CodedStatement.KEY_TABLENAME, t.getTableName());
    tables.remove(t);
    LinkedList<Statement> statem = new LinkedList<Statement>();
    // if a queue is provided check first if a table has really been dropped by
    // the user or if the table is just in the database with no relation to the
    // cids system
    if(queue != null)
    {
      Action[] drops = queue.getActionArray(Action.DROP_ACTION);
      if(drops != null)
      {
        for(Action a : drops)
        {
          // args[0] shall always be the table name if a drop action is stored
          if(a.getArgs()[0].equalsIgnoreCase(t.getTableName()))
          {
            // sequence has to be dropped after table
            if(sequenceExists(t.getTableName()))
              statem.add(new CodedStatement(
                  CodedStatement.CODE_DROP_SEQUENCE, null, false,
                  t.getTableName() + "_seq"));
            statem.addFirst(new CodedStatement(
                CodedStatement.CODE_DROP_STANDARD, null, false, map));
            Statement[] s = new Statement[statem.size()];
            s = statem.toArray(s);
            StatementGroup ret = new StatementGroup(s, true);
            // TODO: description to resource bundle
            ret.setDescription("Die Tabelle " + t.getTableName() + 
                " wird gelöscht. Weiterhin wird die dazugehörige Sequenz für " +
                "den Primärschlüssel gelöscht, sofern sie vorhanden ist.");
            ret.setTableName(t.getTableName());
            return ret;
          }
        }
      }
    }
    else
    {
      // sequence has to be dropped after table
      if(sequenceExists(t.getTableName()))
        statem.add(new CodedStatement(
            CodedStatement.CODE_DROP_SEQUENCE, null, false,
            t.getTableName() + "_seq"));
      statem.addFirst(new CodedStatement(CodedStatement.CODE_DROP_STANDARD, 
          null, false, map));
      Statement[] s = new Statement[statem.size()];
      s = statem.toArray(s);
      StatementGroup ret = new StatementGroup(s, true);
      // TODO: description to resource bundle
      ret.setDescription("Die Tabelle " + t.getTableName() + " wird gelöscht." +
          " Weiterhin wird die dazugehörige Sequenz für den Primärschlässel " +
          "gelöscht, sofern sie vorhanden ist.");
      ret.setTableName(t.getTableName());
      return ret;
    }
    return null;
  }
  
  private StatementGroup createTypeConversionStatements(Table t,
      Attribute attr, String typeToConvertIn)
  {
    LinkedList<CodedStatement> cstatem = new LinkedList<CodedStatement>();
    String attrTypeName = attr.getType().getName().toUpperCase();
    String warning = CodedStatement.WARNING_TYPE_MISMATCH;
    String newTypeName = null;
    if(typeToConvertIn == null)
    {
      if(attr.getPrecision() == null)
        newTypeName = attrTypeName;
      else
      {
        newTypeName = attrTypeName + "(" + attr.getPrecision();
        if(attr.getScale() != null)
          newTypeName += ", " + attr.getScale();
        newTypeName += ")";
      }
      cstatem.addLast(new CodedStatement(
          CodedStatement.CODE_ALTER_ADD_COLUMN, warning, false,
          t.getTableName(), TMP_COLUMN, newTypeName));
    }
    else
    {
      cstatem.addLast(new CodedStatement(
          CodedStatement.CODE_ALTER_ADD_COLUMN, warning, false,
          t.getTableName(), TMP_COLUMN, typeToConvertIn.toUpperCase()));
      newTypeName = typeToConvertIn.toUpperCase();
    }
    if(attr.getDefaultValue() != null)
      cstatem.addLast(new CodedStatement(
          CodedStatement.CODE_ALTER_COLUMN_SET, warning, false,
          t.getTableName(), TMP_COLUMN, "DEFAULT '" + attr.getDefaultValue() +
          "'"));
    cstatem.addLast(new CodedStatement(
        CodedStatement.CODE_UPDATE_COPY, warning, false,
        t.getTableName(), TMP_COLUMN, attr.getFieldName()));
    // copy has to be performed first, otherwise the set not null statement
    // will obviously fail as long as there is at least one row in the table
    if(!attr.isOptional())
      cstatem.addLast(new CodedStatement(
          CodedStatement.CODE_ALTER_COLUMN_SET, warning, false,
          t.getTableName(), TMP_COLUMN, "NOT NULL"));
    cstatem.addLast(new CodedStatement(
        CodedStatement.CODE_ALTER_DROP_COLUMN, warning, false,
        t.getTableName(), attr.getFieldName()));
    cstatem.addLast(new CodedStatement(
        CodedStatement.CODE_ALTER_RENAME_COLUMN, warning, false,
        t.getTableName(), attr.getFieldName(), TMP_COLUMN));
    Statement[] s = new Statement[cstatem.size()];
    s = cstatem.toArray(s);
    StatementGroup group = new StatementGroup(s, true);
    group.setTableName(t.getTableName().toLowerCase());
    group.setColumnName(attr.getFieldName().toLowerCase());
    MessageFormat descform = new MessageFormat(descBundle.getString(
        StatementGroup.GROUP_DESC_UPDATE_AND_NOT_NULL));
    String[] args = {t.getTableName(), attr.getFieldName().toLowerCase(), 
        t.getTableColumn(attr.getFieldName()).getTypeName().toUpperCase(),
        newTypeName};
    group.setDescription(descform.format(args));
    group.setWarning("Die Konvertierung kann fehlschlagen, wenn Typen nicht " +
        "kompatibel sind.");
    return group;
  }
  
  /**
   * NOTE: typesize, default_value and optional field will be ignored when
   *       checking and creating the primary key.
   *       type of the primary key has to be integer otherwise exception
   */
  private StatementGroup createPrimaryKeyStatements(
      Table t, CidsClass c) throws 
      ScriptGeneratorException
  {
    LinkedList<CodedStatement> codedStatements = new LinkedList<CodedStatement>(
        );
    StatementGroup createGroup = null;
    // check for primary key
    // eventually drop existing and/or create new one
    try
    {
      if(!containsPrimkeyAttr(c))
        throw new ScriptGeneratorException(exceptionBundle.getString(
              DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NOT_FOUND),
              t.getTableName(), c.getPrimaryKeyField().toLowerCase(), null);
      if(isPrimkeyAttrOptional(c))
        throw new ScriptGeneratorException(exceptionBundle.getString(
              DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NOT_NULL),
              t.getTableName(), c.getPrimaryKeyField().toLowerCase(), null);
      if(!isPrimkeyAttrInteger(c))
        throw new ScriptGeneratorException(exceptionBundle.getString(
              DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_NOT_INTEGER),
              t.getTableName(), c.getPrimaryKeyField().toLowerCase(), null);
      if(!sequenceExists(c.getTableName() + "_seq"))
      {
        if(isTableEmpty(c.getTableName()))
          codedStatements.addFirst(new CodedStatement(
            CodedStatement.CODE_CREATE_SEQUENCE, null, false,
            c.getTableName().toLowerCase() + "_seq", "1"));
        else
        {
          codedStatements.addFirst(new CodedStatement(
              CodedStatement.CODE_SELECT_SETVAL_MAX, null, false,
              c.getTableName().toLowerCase(), c.getPrimaryKeyField().
              toLowerCase(), c.getTableName().toLowerCase() + "_seq"));
          codedStatements.addFirst(new CodedStatement(
              CodedStatement.CODE_CREATE_SEQUENCE, null, false,
              c.getTableName().toLowerCase() + "_seq", "1"));
        }
      }
      // composite primary key, drop it and create new
      if(t.getPrimaryKeyColumnNames().length > 1)
      {
        codedStatements.addLast(new CodedStatement(
            CodedStatement.CODE_ALTER_DROP_CONSTRAINT, null, false,
            t.getTableName(), t.getTableName() + "_pkey"));
        codedStatements.addLast(new CodedStatement(
            CodedStatement.CODE_ALTER_ADD_PRIMARY, null, false,
            t.getTableName().toLowerCase(), c.getPrimaryKeyField().toLowerCase(
            )));
      }
      // no primary key, create new
      else if(t.getPrimaryKeyColumnNames().length < 1)
        codedStatements.addLast(new CodedStatement(
            CodedStatement.CODE_ALTER_ADD_PRIMARY, null, false,
            t.getTableName().toLowerCase(), c.getPrimaryKeyField().toLowerCase(
            )));
      // if key not equals, drop it create new
      else if(!t.getPrimaryKeyColumnNames()[0].equalsIgnoreCase(
          c.getPrimaryKeyField()))
      {
        codedStatements.addLast(new CodedStatement(
            CodedStatement.CODE_ALTER_DROP_CONSTRAINT,
            CodedStatement.WARNING_DROP_PRIMARY_KEY, false,
            t.getTableName(), t.getTableName() + "_pkey"));
        codedStatements.addLast(new CodedStatement(
            CodedStatement.CODE_ALTER_ADD_PRIMARY,
            CodedStatement.WARNING_NEW_PRIMARY_KEY, false,
            t.getTableName().toLowerCase(), c.getPrimaryKeyField().toLowerCase(
            )));
      }
      if(codedStatements.size() > 0)
      {
        Statement[] statements = new Statement[codedStatements.size()];
        statements = codedStatements.toArray(statements);
        createGroup = new StatementGroup(statements, true);
        MessageFormat descform = new MessageFormat(descBundle.getString(
        StatementGroup.GROUP_DESC_PRIM_KEY_FIT));
        String[] args = {t.getTableName()};
        createGroup.setDescription(descform.format(args));
        createGroup.setTableName(t.getTableName());
        createGroup.setColumnName(c.getPrimaryKeyField().toLowerCase());
      }
    }
    // npe is thrown when c.getPrimaryKeyField() returns null and method will be
    // accessed from returning value. So then the primary key is missing
    catch(NullPointerException npe)
    {
      throw new ScriptGeneratorException(exceptionBundle.getString(
          DiffAccessor.SCRIPT_GENERATOR_EXCEPTION_MISSING_PRIMKEY_FIELD),
          c.getTableName(), npe);
    }
    return createGroup;
  }
  
  private Table getTable(String name)
  {
    for(Table t : allTables)
      if(t.getTableName().equalsIgnoreCase(name))
        return t;
    return null;
  }
  
  private boolean containsPrimkeyAttr(final CidsClass c)
  {
    final Iterator<Attribute> it = c.getAttributes().iterator();
    while(it.hasNext())
      if(it.next().getFieldName().equalsIgnoreCase(c.getPrimaryKeyField()))
        return true;
    return false;
  }
  
  private boolean isPrimkeyAttrOptional(final CidsClass c)
  {
    final Iterator<Attribute> it = c.getAttributes().iterator();
    while(it.hasNext())
    {
      final Attribute current = it.next();
      if(current.getFieldName().equalsIgnoreCase(c.getPrimaryKeyField()))
        return current.isOptional();
    }
    return false;
  }
  
  private boolean isPrimkeyAttrInteger(final CidsClass c)
  {
    final Iterator<Attribute> it = c.getAttributes().iterator();
    while(it.hasNext())
    {
      final Attribute current = it.next();
      if(current.getFieldName().equalsIgnoreCase(c.getPrimaryKeyField()))
      {
        return current.getType().getName().equalsIgnoreCase("INTEGER") ?
            true : false;
      }
    }
    return false;
  }
  
  private boolean sequenceExists(String seqName)
  {
    try
    {
      DatabaseConnection.execSQL(runtime, "SELECT * FROM " + seqName, 
          this.hashCode());
      return true;
    } catch (SQLException ex)
    {
      return false;
    }
  }
  
  private boolean isTableEmpty(String tableName)
  {
    try
    {
      return !DatabaseConnection.execSQL(
          runtime, "SELECT * FROM " + tableName, this.hashCode()).next();
    } catch (SQLException ex)
    {
      return true;
    }
  }
  
  private boolean isDefaultValueValid(String column, String typename, 
      Integer precision, Integer scale, String defaultValue)
  {
    try
    {
      String csTypename = typename;
      // map typename back from postgres internal to cids type name if necessary
      if(typemapPSQLtoCids.containsKey(typename))
        csTypename = typemapPSQLtoCids.get(typename);
      DatabaseConnection.updateSQL(runtime, "BEGIN WORK", this.hashCode());
      StringBuffer sb = new StringBuffer();
      // build a new temporary table creation string using the given values
      sb.append("CREATE TEMP TABLE cs_tmptable (").append(column).append(" ").
          append(csTypename);
      // typesize != null indicates that a type is parameterized
      if(precision != null)
      {
        sb.append("(").append(precision);
        if(scale != null)
          sb.append(", ").append(scale);
        sb.append(")");
      }
      sb.append(" DEFAULT '").append(defaultValue).append("')");
      // try to create table from creation string, if failes due to exception it
      // indicates that the default value is not valid
      DatabaseConnection.updateSQL(runtime, sb.toString(), this.hashCode());
      // try to insert the default values into the table, if fails due to
      // exception it indicates that the default value has correct type but does
      // not fit in the reserved space for this column and so is not valid
      DatabaseConnection.updateSQL(runtime, "INSERT INTO cs_tmptable " +
          "DEFAULT VALUES", this.hashCode());
      // everyting was fine
      return true;
    } catch (SQLException ex)
    {
      // an exception indicated that the value is not valid
      return false;
    } finally
    {
      try
      {
        // rollback to delete the temporary table
        DatabaseConnection.updateSQL(runtime, "ROLLBACK", this.hashCode());
      } catch (SQLException ex)
      {
        // do nothing, table will be deleted when session ends
        log.error("temp table could not be deleted", ex);
      }
    }
  }
}
