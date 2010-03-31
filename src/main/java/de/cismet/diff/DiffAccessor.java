/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff;

import de.cismet.cids.jpa.backend.service.impl.Backend;

import de.cismet.diff.util.ProgressionQueue;

import de.cismet.diff.builder.ScriptGenerator;
import de.cismet.diff.builder.ScriptGenerator;
import de.cismet.diff.builder.TableLoader;
import de.cismet.diff.builder.TableLoader;

import de.cismet.diff.container.Action;
import de.cismet.diff.container.PSQLStatementGroup;

import de.cismet.diff.db.DatabaseConnection;

import de.cismet.diff.exception.ScriptGeneratorException;
import de.cismet.diff.exception.TableLoaderException;
import de.cismet.diff.exception.UnsupportedActionException;

import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class provides access to all the functionality the <code>de.cismet.diff</code> package provides. To use this
 * package you will never have to instantiate any class within its sub-packages. Just instantiate this one and use the
 * functionality it provides.
 *
 * @author   Martin Scholl
 * @version  1.0 2007-03-09
 */
public class DiffAccessor {

    //~ Static fields/initializers ---------------------------------------------

    // TODO: centralize ResourceBundle or load exc messages directly?!
    public static final String EXCEPTION_RESOURCE_BASE_NAME = "de.cismet.diff.resource.exceptionMessages";               // NOI18N
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_TABLES_NULL_OR_EMPTY =
        "IllegalArgumentException_tablesNullOrEmpty";                                                                    // NOI18N
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_CLASSES_NULL_OR_EMPTY =
        "IllegalArgumentException_classesNullOrEmpty";                                                                   // NOI18N
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_TABNAME_NULL_OR_EMPTY =
        "IllegalArgumentException_tableNameNullOrEmpty";                                                                 // NOI18N
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_COLUMNS_NULL_OR_EMPTY =
        "IllegalArgumentException_columnsNullOrEmpty";                                                                   // NOI18N
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_COLNAME_NULL_OR_EMPTY =
        "IllegalArgumentException_columnNameNullOrEmpty";                                                                // NOI18N
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_TYPENAME_NULL_OR_EMPTY =
        "IllegalArgumentException_typeNameNullOrEmpty";                                                                  // NOI18N
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_NULLABLE_OUT_OF_BOUNDS =
        "IllegalArgumentException_nullableNotWithinBounds";                                                              // NOI18N
    public static final String ILLEGAL_ARGUMENT_EXCEPTION_TYPESIZE_NOT_GT_0 = "IllegalArgumentException_typeSizeNotGt0"; // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_EXC_STATEMENT_CREATE =
        "ScriptGeneratorException_excStatementCreation";                                                                 // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_MISSING_COMP_TYPE =
        "ScriptGeneratorException_missingComplexType";                                                                   // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_DEF_TYPE_MISMATCH =
        "ScriptGeneratorException_defaultValueTypeMismatch";                                                             // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_MISSING_PRIMKEY_FIELD =
        "ScriptGeneratorException_missingPrimaryKeyField";                                                               // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_EMPTY_PRIMKEY_FIELD =
        "ScriptGeneratorException_emptyPrimaryKeyField";                                                                 // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_NOT_NULLABLE =
        "ScriptGeneratorException_primKeyNotNullable";                                                                   // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NOT_FOUND =
        "ScriptGeneratorException_primKeyAttrNotFound";                                                                  // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NO_DROP =
        "ScriptGeneratorException_primKeyAttrNotDroppable";                                                              // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_ATTR_NOT_NULL =
        "ScriptGeneratorException_primKeyAttrNotNullable";                                                               // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_PRIMKEY_NOT_INTEGER =
        "ScriptGeneratorException_primKeyNotInteger";                                                                    // NOI18N
    public static final String SCRIPT_GENERATOR_EXCEPTION_TABLENAME_HAS_SPACES =
        "ScriptGeneratorExcpetion_tableNameHasSpaces";                                                                   // NOI18N
    public static final String TABLE_LOADER_EXCPETION_STDP_CREATION_FAILED = "TableLoaderException_STDPcreationFailed";  // NOI18N
    public static final String TABLE_LOADER_EXCPETION_TABLE_LOAD_FAILED = "TableLoaderException_tableLoadFailed";        // NOI18N
    public static final String ILLEGAL_CODE_EXCEPTION_CODE_NOT_KNOWN = "IllegalCodeException_codeNotKnown";              // NOI18N
    public static final String ILLEGAL_CODE_EXCEPTION_WARNING_NOT_KNOWN = "IllegalCodeException_warningNotKnown";        // NOI18N
    public static final String SQL_EXCEPTION_LOCATE_JDBC_DRIVER_FAILED = "SQLException_locateJDBCdriverFailed";          // NOI18N
    public static final String SQL_EXCEPTION_JDBC_INSTANTIATION_FAILED = "SQLException_JDBCinstantiationFailed";         // NOI18N
    public static final String SQL_EXCEPTION_JDBC_INSTANTIATION_ILLEGAL_ACCESS =
        "SQLException_JDBCinstantiationIllegalAccess";                                                                   // NOI18N
    public static final String UNSUPPORTED_ACTION_EXCEPTION_ACTION_NOT_SUPPORTED =
        "UnsupportedActionException_actionNotSupported";                                                                 // NOI18N

    private static final Logger LOG = Logger.getLogger(DiffAccessor.class);

    //~ Instance fields --------------------------------------------------------

    private final transient Backend backend;
    private final transient Properties runtime;
    private final transient ProgressionQueue storage;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of DiffAccessor by calling the constructor with 'null' as <code>
     * de.cismet.cids.dataobjects.dbbackend.Backend</code> b.
     *
     * @param  runtime  DOCUMENT ME!
     */
    public DiffAccessor(final Properties runtime) {
        this(runtime, null);
    }

    /**
     * Creates a new instance of DiffAccessor. The Backend b shall be an instance of <code>
     * de.cismet.cids.dataobjects.dbbackend.Backend</code>. It is used to load several data to provide the desired
     * functionality. If you pass 'null' as an argument, a new Backend instance will be created, when needed. If you
     * already use a Backend instance, you should provide it, because creating a new one will take several seconds an so
     * decreases speed of some methods.
     *
     * @param   runtime  DOCUMENT ME!
     * @param   b        <code>de.cismet.cids.dataobjects.dbbackend.Backend</code> instance may be null
     *
     * @throws  IllegalArgumentException  NullPointerException DOCUMENT ME!
     */
    public DiffAccessor(final Properties runtime, final Backend b) {
        this.backend = b;
        if (runtime == null) {
            throw new IllegalArgumentException("runtime properties must not be null"); // NOI18N
        }
        this.runtime = runtime;
        storage = new ProgressionQueue();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Puts a drop action to the event queue. This queue may hold user actions made that affect the system tables
     * "cs_*", especially "cs_class" and "cs_attr", and will so affect the corresponding tables of the database.
     *
     * @param  affectedTable  the name of the table that has been removed from "cs_class"
     */
    public void putDropAction(final String affectedTable) {
        try {
            final Action a = new Action(Action.DROP_ACTION, affectedTable);
            storage.putAction(a);
        } catch (final UnsupportedActionException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("drop action not supported", ex); // NOI18N
            }
        }
    }

    /**
     * Creates an array of <code>de.cismet.diff.container.PSQLStatementGroup</code> instances. These instances contain,
     * amongst other things, the PSQL statements, that are necessary to update the tables of the cids system
     * corresponding to the entries in the system tables "cs_class" and "cs_attr". The returned array is sorted and the
     * statements shall be executed in the order of the array to ensure the statements to work well.
     *
     * @return  an array of <code>de.cismet.diff.container.PSQLStatement</code> instances containing the statements
     *          necessary to adjust the system tables entries to the corresponding tables
     *
     * @throws  TableLoaderException      if something went wrong during table load
     * @throws  ScriptGeneratorException  if statements could not be generated for any reason
     */
    public PSQLStatementGroup[] getStatementGroups() throws TableLoaderException, ScriptGeneratorException {
        long time1 = System.currentTimeMillis();
        final TableLoader t = new TableLoader(runtime, backend);
        time1 = System.currentTimeMillis() - time1;
        long time2 = System.currentTimeMillis();
        final ScriptGenerator sg = new ScriptGenerator(runtime, t, storage);
        final PSQLStatementGroup[] s = sg.getStatementGroups();
        time2 = System.currentTimeMillis() - time2;
        if (LOG.isInfoEnabled()) {
            LOG.info("Loaded tables in '" + time1 + "' ms.");        // NOI18N
            LOG.info("Generated statements in '" + time2 + "' ms."); // NOI18N
        }
        return s;
    }

    /**
     * Removes all actions from the event queue.
     */
    public void removeActions() {
        storage.clearActions();
    }

    /**
     * DOCUMENT ME!
     */
    public void freeResources() {
        DatabaseConnection.clear();
    }
}
