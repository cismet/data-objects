/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.builder;

import de.cismet.cids.jpa.backend.service.impl.Backend;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;

import de.cismet.diff.DiffAccessor;

import de.cismet.diff.container.Table;

import de.cismet.diff.db.SimpleTablesDataProvider;

import de.cismet.diff.exception.TableLoaderException;

import java.sql.SQLException;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * This class allows you to retrieve <code>Table</code> and <code>CidsClass</code> instances from the database the
 * underlying <code>SimpleTablesDataProvider</code> and <code>Backend</code> use.
 *
 * @author   Martin Scholl
 * @version  1.0 2007-03-13
 */
public class TableLoader {

    //~ Instance fields --------------------------------------------------------

    private transient CidsClass[] classes;
    private transient Table[] tables;

    private transient Backend b;
    private transient Properties runtime;
    private transient SimpleTablesDataProvider data;

    private final transient ResourceBundle exceptionBundle = ResourceBundle.getBundle(
            DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of TableLoader setting the backend to 'null' and tries to load the tables immediately.
     *
     * @param   runtime  the runtime.properties
     *
     * @throws  TableLoaderException  de.cismet.diff.exception.TableLoaderException if an error occurs while loading
     *                                tables
     */
    public TableLoader(final Properties runtime) throws TableLoaderException {
        this(runtime, null);
    }

    /**
     * Creates a new instance of TableLoader using the given backend instance and tries to load the tables immediately.
     *
     * @param   runtime  the runtime.properties
     * @param   b        backend the hibernate session to use
     *
     * @throws  TableLoaderException  de.cismet.diff.exception.TableLoaderException if an error occurs while loading
     *                                tables
     */
    public TableLoader(final Properties runtime, final Backend b) throws TableLoaderException {
        this.b = b;
        this.runtime = runtime;
        try {
            data = new SimpleTablesDataProvider(runtime);
        } catch (SQLException ex) {
            throw new TableLoaderException(
                exceptionBundle.getString(
                    DiffAccessor.TABLE_LOADER_EXCPETION_STDP_CREATION_FAILED),
                ex);
        }
        load();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Table[] getTables() {
        return Arrays.copyOf(tables, tables.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  TableLoaderException  DOCUMENT ME!
     */
    public Table[] getTablesForceReload() throws TableLoaderException {
        loadTables();
        return Arrays.copyOf(tables, tables.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsClass[] getClasses() {
        return Arrays.copyOf(classes, classes.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  TableLoaderException  DOCUMENT ME!
     */
    public CidsClass[] getClassesForceReload() throws TableLoaderException {
        loadClasses();
        return Arrays.copyOf(classes, classes.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  TableLoaderException  DOCUMENT ME!
     */
    private void load() throws TableLoaderException {
        loadTables();
        loadClasses();
    }

    /**
     * Loads the classes entries in the "cs_class" system table of the database specified in the runtime properties. If
     * the backend has not been set while instatiation, a new <code>de.cismet.cids.dataobjects.dbbackend.Backend</code>
     * instance will be created using the "runtime.properties".
     *
     * @throws  TableLoaderException  DOCUMENT ME!
     */
    private void loadClasses() throws TableLoaderException {
        try {
            if (b == null) {
                b = new Backend(runtime);
            }
            final List<CidsClass> l = b.getAllEntities(CidsClass.class);
            classes = new CidsClass[l.size()];
            classes = l.toArray(classes);
        } catch (final Exception ex) {
            classes = null;
            throw new TableLoaderException(
                exceptionBundle.getString(
                    DiffAccessor.TABLE_LOADER_EXCPETION_TABLE_LOAD_FAILED),
                ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  TableLoaderException  DOCUMENT ME!
     */
    private void loadTables() throws TableLoaderException {
        try {
            final String[] tableNames = data.getTableNames();
            tables = new Table[tableNames.length];
            for (int i = 0; i < tableNames.length; i++) {
                tables[i] = new Table(tableNames[i], data.getColumns(tableNames[i]));
            }
        } catch (final SQLException ex) {
            tables = null;
            throw new TableLoaderException(
                exceptionBundle.getString(
                    DiffAccessor.TABLE_LOADER_EXCPETION_TABLE_LOAD_FAILED),
                ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  TableLoaderException  DOCUMENT ME!
     */
    public void reload() throws TableLoaderException {
        load();
    }
}
