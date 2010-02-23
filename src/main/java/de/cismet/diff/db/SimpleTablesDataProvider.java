/*
 * TablesDataProvider.java
 *
 * Created on 19. Februar 2007, 15:40
 */

package de.cismet.diff.db;

import de.cismet.diff.container.TableColumn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

/**
 * This class uses a single JDBC connection to claim data from the database. It
 * provides information about the names of the tables in the database and
 * provides information about the columns of tables corresponding to the tables
 * name. Moreover it provides a static method to execute update sql statements.
 * It is essential for this class to have <code>runtime.properties</code> that 
 * contain the url, the username, the password and the driver class as 
 * properties. The package where the <code>runtime.properties</code> can be
 * found and the single connection params has to be altered if they change. They
 * can be altered in the constructor of the class.
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-08
 */
public class SimpleTablesDataProvider
{
  // default type of a table, will probably not have to be altered
  private final String[] TYPES = {"TABLE"};
  
  private Connection con;
  
  /**
   * Creates a new instance of <code>SimpleTablesDataProvider</code> 
   * initializing the runtime parameters from the "runtime.properties" file 
   * and finally connecting to the database.
   * 
   * 
   * @throws java.sql.SQLException if the database runtime could not be
   *      initialized
   */
  public SimpleTablesDataProvider(Properties runtime) throws
      SQLException
  {
    con = DatabaseConnection.getConnection(runtime);
  }
  
  /**
   * Retrieves all names of tables in the database excluding any system table
   * starting with "cs_" and the two PostGIS tables "spatial_ref_sys" and
   * "geometry_columns".
   *
   * @throws java.sql.SQLException if any error occurs when fetching the
   *      results
   * @return String[] containing the names of the tables in the database
   */
  public String[] getTableNames() throws
      SQLException
  {
    ResultSet set = null;
    LinkedList<String> names = new LinkedList<String>();
    try
    {
      set = con.getMetaData().getTables(null, null, null, TYPES);
      while(set.next())
      {
        String table = set.getString(3);
        if(!(table.startsWith("cs_") ||
             table.equals("spatial_ref_sys") ||
             table.equals("geometry_columns")))
        {
          names.add(table);
        }
      }
      if(names.size() == 0)
        return null;
      String[] ret = new String[names.size()];
      return names.toArray(ret);
    }
    finally
    {
      if(set != null)
        set.close();
    }
  }
  
  /**
   * Retrives the name, the type name its size and default value and whether the
   * column is nullable from all columns of the table with the given name.
   *
   * @param tableName the name of the table which columns shall be retrieved
   * @throws java.lang.SQLException if an error occurs during retrieval
   * @return TableColumn[] containing all the information about the columns of
   *      the table
   */
  public TableColumn[] getColumns(String tableName) throws
      SQLException
  {
    ResultSet set = null;
    LinkedList<String> keys = new LinkedList<String>();
    LinkedList<TableColumn> columns = new LinkedList<TableColumn>();
    try
    {
      //set = con.getMetaData().getPrimaryKeys(schema, schemaPattern, tableName);
      set = con.getMetaData().getPrimaryKeys(null, null, tableName);
      // 4 = column name
      while(set.next())
        keys.add(set.getString(4));
      //set = con.getMetaData().getColumns(schema, schemaPattern, tableName,null);
      set = con.getMetaData().getColumns(null, null, tableName, null);
      while(set.next())
      {
        //  4 = column name
        //  6 = type name
        //  7 = column size
        //  9 = decimal digits
        // 11 = nullable
        // 13 = column default value (may be 'null')
        if(keys.contains(set.getString(4)))
          columns.add(new TableColumn(set.getString(4),
                                      set.getString(6),
                                      set.getInt(7),
                                      set.getInt(9),
                                      set.getString(13),
                                      set.getShort(11),
                                      true));
        else
          columns.add(new TableColumn(set.getString(4),
                                      set.getString(6),
                                      set.getInt(7),
                                      set.getInt(9),
                                      set.getString(13),
                                      set.getShort(11),
                                      false));
      }
      if(columns.size() == 0)
        return null;
      TableColumn[] ret = new TableColumn[columns.size()];
      return columns.toArray(ret);
    }
    finally
    {
      if(set != null)
        set.close();
    }
  }
}
