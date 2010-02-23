/*
 * TableLoader.java
 *
 * Created on 19. Februar 2007, 13:42
 */

package de.cismet.diff.builder;

import de.cismet.cids.jpa.backend.service.impl.Backend;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.diff.DiffAccessor;
import de.cismet.diff.container.Table;
import de.cismet.diff.db.SimpleTablesDataProvider;
import de.cismet.diff.exception.TableLoaderException;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * This class allows you to retrieve <code>Table</code> and 
 * <code>CidsClass</code> instances from the database the underlying 
 * <code>SimpleTablesDataProvider</code> and <code>Backend</code> use.
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-13
 */
public class TableLoader
{
  private CidsClass[] classes;
  private Table[] tables;
  
  private Backend b;
  private Properties runtime;
  private SimpleTablesDataProvider data;
  
  private ResourceBundle exceptionBundle = ResourceBundle.getBundle(
      DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
  
  /**
   * Creates a new instance of TableLoader setting the backend to 'null' and
   * tries to load the tables immediately.
   *
   * @param runtime the runtime.properties
   * @throws de.cismet.diff.exception.TableLoaderException if an error occurs
   *      while loading tables
   */
  public TableLoader(Properties runtime) throws
      TableLoaderException
  {
    this(runtime, null);
  }
  
  /**
   * Creates a new instance of TableLoader using the given backend instance and
   * tries to load the tables immediately.
   *
   * @param runtime the runtime.properties
   * @param backend the hibernate session to use
   * @throws de.cismet.diff.exception.TableLoaderException if an error occurs
   *      while loading tables
   */
  public TableLoader(Properties runtime, Backend b) throws
      TableLoaderException
  {
    classes = null;
    tables = null;
    this.b = b;
    this.runtime = runtime;
    try
    {
      data = new SimpleTablesDataProvider(runtime);
    }
    catch (SQLException ex)
    {
      throw new TableLoaderException(exceptionBundle.getString(
          DiffAccessor.TABLE_LOADER_EXCPETION_STDP_CREATION_FAILED), ex);
    }
    load();
  }
  
  public Table[] getTables()
  {
    return tables;
  }
  
  public Table[] getTablesForceReload() throws
      TableLoaderException
  {
    loadTables();
    return tables;
  }
  
  public CidsClass[] getClasses()
  {
    return classes;
  }
  
  public CidsClass[] getClassesForceReload() throws
      TableLoaderException
  {
    loadClasses();
    return classes;
  }
  
  private void load() throws
      TableLoaderException
  {
    loadTables();
    loadClasses();
  }
  
  /**
   * Loads the classes entries in the "cs_class" system table of the database
   * specified in the runtime properties. If the backend has not been set while
   * instatiation, a new 
   * <code>de.cismet.cids.dataobjects.dbbackend.Backend</code> instance will be
   * created using the "runtime.properties".
   */
  private void loadClasses() throws
      TableLoaderException
  {
    try
    {
      if(b == null)
        b = new Backend(runtime);
      List<CidsClass> l = b.getAllEntities(CidsClass.class);
      classes = new CidsClass[l.size()];
      classes = l.toArray(classes);
    }
    catch (Exception ex)
    {
      classes = null;
      throw new TableLoaderException(exceptionBundle.getString(
          DiffAccessor.TABLE_LOADER_EXCPETION_TABLE_LOAD_FAILED), ex);
    }
  }
  
  private void loadTables() throws
      TableLoaderException
  {
    try
    {
      String[] tableNames = data.getTableNames();
      tables = new Table[tableNames.length];
      for(int i = 0; i < tableNames.length; i++)
        tables[i] = new Table(tableNames[i], data.getColumns(tableNames[i]));
    }
    catch (SQLException ex)
    {
      tables = null;
      throw new TableLoaderException(exceptionBundle.getString(
          DiffAccessor.TABLE_LOADER_EXCPETION_TABLE_LOAD_FAILED), ex);
    }
  }
  
  public void reload() throws
      TableLoaderException
  {
    load();
  }
}
