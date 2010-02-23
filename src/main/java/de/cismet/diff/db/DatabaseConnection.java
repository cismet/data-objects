/*
 * DatabaseConnection.java
 *
 * Created on 26. März 2007, 17:24
 */

package de.cismet.diff.db;

import de.cismet.diff.DiffAccessor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author Martin Scholl
 * @version 1.0
 */
public class DatabaseConnection
{
  private static HashMap<Integer, Connection> connectionHash_update = 
      new HashMap<Integer, Connection>();
  private static HashMap<Integer, Connection> connectionHash_exec = 
      new HashMap<Integer, Connection>();
  
  /**
   * Creates a new instance of <code>DatabaseConnection</code>
   */
  private DatabaseConnection(){}
  
  /**
   * Connect to the database
   *
   * @return java.sql.Connection to the database
   * @throws java.sql.SQLException if an error occurs during creation of the
   *      connection
   */
  public static Connection getConnection(final Properties runtime) throws
      SQLException
  {
    return getConnection(runtime, 30);
  }

  /**
   * Connect to the database
   *
   * @return java.sql.Connection to the database
   * @throws java.sql.SQLException if an error occurs during creation of the
   *      connection
   */
  public static Connection getConnection(final Properties runtime, final int
      timeOut) throws
      SQLException
  {
    final ResourceBundle exceptionBundle = ResourceBundle.getBundle(
      DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
    final String dbURL = runtime.getProperty("connection.url");
    final String username = runtime.getProperty("connection.username");
    final String password = runtime.getProperty("connection.password");
    final String driver = runtime.getProperty("connection.driver_class");
    try
    {
      Class.forName(driver).newInstance();
    }catch(final ClassNotFoundException cnfe)
    {
      throw new SQLException(exceptionBundle.getString(
          DiffAccessor.SQL_EXCEPTION_LOCATE_JDBC_DRIVER_FAILED));
    }catch(final InstantiationException ie)
    {
      throw new SQLException(exceptionBundle.getString(
          DiffAccessor.SQL_EXCEPTION_JDBC_INSTANTIATION_FAILED));
    }catch(final IllegalAccessException iae)
    {
      throw new SQLException(exceptionBundle.getString(
          DiffAccessor.SQL_EXCEPTION_JDBC_INSTANTIATION_ILLEGAL_ACCESS));
    }
    final int dTO = DriverManager.getLoginTimeout();
    DriverManager.setLoginTimeout(timeOut);
    try
    {
      return DriverManager.getConnection(dbURL, username, password);
    }finally
    {
      DriverManager.setLoginTimeout(dTO);
    }
  }
    
  /**
   * Executes update sql statements such as ALTER, INSERT, UPDATE etc. It uses 
   * just one Connection instance for all method calls to enable transaction
   * functionality. Auto commit is set to false.
   */
  public static int updateSQL(Properties runtime, String sql, 
      int callerHashcode) throws
      SQLException
  {
    if(connectionHash_update.get(callerHashcode) == null)
    {
      Connection con = getConnection(runtime);
      con.setAutoCommit(false);
      connectionHash_update.put(callerHashcode, con);
    }
    return connectionHash_update.get(callerHashcode).createStatement().
        executeUpdate(sql);
  }
  
  /**
   * Executes query sql statements such as SELECT. 
   */
  public static ResultSet execSQL(Properties runtime, String sql,
      int callerHashcode) throws
      SQLException
  {
    if(connectionHash_exec.get(callerHashcode) == null)
      connectionHash_exec.put(callerHashcode, getConnection(runtime));
    return connectionHash_exec.get(callerHashcode).createStatement().
        executeQuery(sql);
  }

  /** Releases all cached connections **/
  public static void clear()
  {
      for(final Connection con : connectionHash_exec.values())
      {
          try
          {
              con.close();
          }catch(final SQLException e)
          {
              // maybe log some day ...
          }
      }
      for(final Connection con : connectionHash_update.values())
      {
          try
          {
              con.close();
          }catch(final SQLException e)
          {
              // maybe log some day ...
          }
      }
      connectionHash_exec.clear();
      connectionHash_update.clear();
  }
}
