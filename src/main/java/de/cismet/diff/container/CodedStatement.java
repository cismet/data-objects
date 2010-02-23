/*
 * CodedStatement.java
 *
 * Created on 22. Februar 2007, 10:20
 */

package de.cismet.diff.container;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class provides a container for coded sql statements. The supported codes
 * are provided as public constants that shall be used to create instances of 
 * this class. It also provides constants for warnings that may go along with
 * a statement.
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-09
 */
public class CodedStatement extends Statement
{
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the name of the table that is involved.
   */
  public static final String KEY_TABLENAME              = "0";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the name of the sequence that is involved.
   */
  public static final String KEY_SEQUENCENAME           = "0";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the name type enumeration that is used to specify the argument when trying
   * to create a new table.<br/>
   * name type enumerations are of the form:<br/>
   * columnname columntype [constraints]
   *    [, columnname columntype [constraints]....]
   */
  public static final String KEY_NAME_TYPE_ENUM         = "1";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the name of the column that is involved.
   */
  public static final String KEY_COLUMNNAME             = "1";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the constraint that shall be set.
   */
  public static final String KEY_CONSTRAINT             = "2";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the new default value of a column.
   */
  public static final String KEY_NEW_DEFAULT            = "2";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the old name of the column that is involved.
   */
  public static final String KEY_COLUMNNAME_OLD         = "2";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the constraint that shall be dropped.
   */
  public static final String KEY_CONSTRAINT_DROP        = "1";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the value that shall be set when building 
   * <code>CODE_UPDATE_WHERE_NULL</code> statements.
   */
  public static final String KEY_COLUMN_VALUE           = "2";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the value that is the start value of a new sequence.
   */
  public static final String KEY_START_VALUE            = "1";
  
  /** 
   * Constant for use to build a valid hashmap. This shall be used as key for
   * the sequencename along with the <code>CODE_SELECT_SETVAL_MAX</code>
   * statement.
   */
  public static final String KEY_SEQUENCENAME_SETVAL    = "2";

  /* highest no of all the keys + 1 */
  private static final int KEY_MAX_ARGS                 = 3;
  
  /** 
   * Code indicating a table drop. Arguments, that must be provided:<br/>
   * <br/>
   * <code>KEY_TABLENAME</code>
   */
  public static final String CODE_DROP_STANDARD         = "drop_table";
  
  /** 
   * Code indicating a sequence drop. Arguments, that must be provided:<br/>
   * <br/>
   * <code>KEY_SEQUENCENAME</code>
   */
  public static final String CODE_DROP_SEQUENCE         = "drop_sequence";
  
  /** 
   * Code indicating a create table. Arguments, that must be provided:
   *
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_NAME_TYPE_ENUM</code>
   */
  public static final String CODE_CREATE_STANDARD       = "create_table";
  
  /** 
   * Code indicating a create sequence. Arguments, that must be provided:
   *
   * <code>KEY_SEQUENCENAME</code><br/>
   * <code>KEY_START_VALUE</code>
   */
  public static final String CODE_CREATE_SEQUENCE       = "create_sequence";
  
  /**
   * Code indicating addition of a column. Arguments, that must be provided:
   * 
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_COLUMNNAME</code><br/>
   * <code>KEY_CONSTRAINT</code>
   */
  public static final String CODE_ALTER_ADD_COLUMN      = "alter_add_column";
  
  /** 
   * Code indicating the drop of a column. Arguments, that must be provided:
   *
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_COLUMNNAME</code>
   */
  public static final String CODE_ALTER_DROP_COLUMN     = "alter_drop_column";
  
  /**
   * Code indicating altering constraints of a column. Arguments, that must be 
   * provided:
   * 
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_COLUMNNAME</code><br/>
   * <code>KEY_CONSTRAINT</code>
   */
  public static final String CODE_ALTER_COLUMN_SET      = "alter_column_set";
  
  /**
   * Code indicating the drop of a column constraint. Arguments, that must be 
   * provided:
   * 
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_COLUMNNAME</code><br/>
   * <code>KEY_CONSTRAINT</code>
   */
  public static final String CODE_ALTER_COLUMN_DROP     = "alter_column_drop";
  
  /**
   * Code indicating renaming of a column. Arguments, that must be provided:
   * 
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_COLUMNNAME</code><br/>
   * <code>KEY_COLUMNNAME_OLD</code>
   */
  public static final String CODE_ALTER_RENAME_COLUMN   = "alter_rename_column";
  
  /**
   * Code indicating addition of a primary key constraint. Arguments, that must 
   * be provided:
   * 
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_COLUMNNAME</code>
   */
  public static final String CODE_ALTER_ADD_PRIMARY     = "alter_add_primary";
  
  /**
   * Code indicating the copy of a column. Arguments, that must be provided:
   * 
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_COLUMNNAME</code><br/>
   * <code>KEY_COLUMNNAME_OLD</code>
   */
  public static final String CODE_UPDATE_COPY           = "update_copy";
  
  /**
   * Code indicating an update where column value == <code>null</code>. 
   * Arguments, that must be provided:
   * 
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_COLUMNNAME</code><br/>
   * <code>KEY_COLUMN_VALUE</code>
   */
  public static final String CODE_UPDATE_WHERE_NULL     = "update_where_null";
  
  /**
   * Code indicating the drop of a table constraint. Arguments, that must be 
   * provided:
   * 
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_CONSTRAINT_DROP</code>
   */
  public static final String CODE_ALTER_DROP_CONSTRAINT =
      "alter_drop_constraint";
  
  /**
   * Code indicating the drop of a table constraint. Arguments, that must be 
   * provided:
   * 
   * <code>KEY_TABLENAME</code><br/>
   * <code>KEY_COLUMNNAME</code><br/>
   * <code>KEY_SEQUENCENAME_SETVAL</code>
   */
  public static final String CODE_SELECT_SETVAL_MAX =
      "select_setval_max";
  
  
  public static final String WARNING_TYPE_MISMATCH =
      "warning_alter_type_mismatch";
  public static final String WARNING_CREATE_COMPLEX_TYPES_NOT_FOUND =
      "warning_create_complex_types_not_found";
  public static final String WARNING_ALTER_COMPLEX_TYPE_NOT_FOUND =
      "warning_alter_complex_type_not_found";
  public static final String WARNING_NEW_PRIMARY_KEY =
      "warning_new_primary_key";
  public static final String WARNING_DROP_PRIMARY_KEY =
      "warning_drop_primary_key";
  public static final String WARNING_COMPOSITE_PRIMARY_KEY =
      "warning_composite_primary_key";
  public static final String WARNING_ALTER_COLUMN_TO_NOTNULL =
      "warning_alter_column_to_notnull";
  
  private String code;
  private String[] argsArray;
  
  /** 
   * Creates a new instance of <code>CodedStatement</code>.
   * 
   * @param code one of the codes provided by this class
   * @param warning one of the warnings provided by this class or null if no
   *    warning shall be attached
   * @param args a <code>HashMap<String, String></code> providing the arguments
   *    corresponding to the code of the statement
   */
  public CodedStatement(String code, String warning, boolean pedantic,
      HashMap<String, String> args)
  {
    super(warning, pedantic);
    this.code = code;
    this.argsArray = getArgsArray(args);
  }
  
  /** 
   * Creates a new instance of <code>CodedStatement</code>. Alternate
   * constructor for easier creation of a <code>CodedStatement</code>. Providing
   * the arguments here indicates that you know in which order they shall be
   * given for the <code>CodedStatement</code> instance to be correct for
   * further use. The arguments shall be given in the order described with the
   * codes.
   * 
   * @param code one of the codes provided by this class
   * @param warning one of the warnings provided by this class or null if no
   *    warning shall be attached
   * @param args the arguments corresponding to the statement in the correct
   *    order
   */
  public CodedStatement(String code, String warning, boolean pedantic,
      String ... args)
  {
    super(warning, pedantic);
    this.code = code;
    this.argsArray = args;
  }
  
  public String getCode()
  {
    return code;
  }
  
  public String[] getArgs()
  {
    return argsArray;
  }
  
  private String[] getArgsArray(HashMap<String, String> args)
  {
    LinkedList<String> sort = new LinkedList<String>();
    for(int i = 0; i < KEY_MAX_ARGS; i++)
    {
      String value = args.get(String.valueOf(i));
      if(value != null)
        sort.addLast(value);
    }
    String[] ret = new String[sort.size()];
    return sort.toArray(ret);
  }
}
