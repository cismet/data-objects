/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.builder;

import org.apache.log4j.Logger;

import java.sql.SQLException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import de.cismet.diff.db.DatabaseConnection;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public abstract class AbstractDialect implements DataObjectsDialect {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(AbstractDialect.class);

    //~ Instance fields --------------------------------------------------------

    protected final Properties runtime;
    protected final transient Map<String, String> typemapDBMStoCids;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractDialect object.
     *
     * @param  runtime  DOCUMENT ME!
     */
    public AbstractDialect(final Properties runtime) {
        this.runtime = runtime;
        this.typemapDBMStoCids = getTypeMap(false);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public boolean isDefaultValueValid(final String column,
            final String typename,
            final Integer precision,
            final Integer scale,
            final String defaultValue) {
        // SQL 99
        try {
            String csTypename = typename;
            // map typename back from postgres internal to cids type name if necessary
            if (typemapDBMStoCids.containsKey(typename)) {
                csTypename = typemapDBMStoCids.get(typename);
            }
            final StringBuffer sb = new StringBuffer(50);
            // build a new temporary table creation string using the given values
            sb.append("CREATE GLOBAL TEMPORARY TABLE cs_tmptable (").append(column).append(' ').append(csTypename); // NOI18N
            // typesize != null indicates that a type is parameterized
            if (precision != null) {
                sb.append('(').append(precision);                    // NOI18N
                if (scale != null) {
                    sb.append(", ").append(scale);                   // NOI18N
                }
                sb.append(')');                                      // NOI18N
            }
            sb.append(" DEFAULT ").append(defaultValue).append(")"); // NOI18N
            // try to create table from creation string, if failes due to exception it
            // indicates that the default value is not valid
            DatabaseConnection.updateSQL(runtime, sb.toString(), this.hashCode());
            // try to insert the default values into the table, if fails due to
            // exception it indicates that the default value has correct type but does
            // not fit in the reserved space for this column and so is not valid
            DatabaseConnection.updateSQL(runtime, "INSERT INTO cs_tmptable VALUES ( DEFAULT )", this.hashCode()); // NOI18N
            // everyting was fine
            return true;
        } catch (final SQLException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("invalid default value", ex);
            }
            // an exception indicated that the value is not valid
            return false;
        } finally {
            try {
                // delete the temporary table
                DatabaseConnection.updateSQL(runtime, "DROP TABLE cs_tmptable", this.hashCode()); // NOI18N
            } catch (SQLException ex) {
                // table has not been created
                if (LOG.isDebugEnabled()) {
                    LOG.debug("temp table could not be deleted", ex); // NOI18N
                }
            }
        }
    }

    @Override
    public StringBuilder appendCreateTableExpression(final StringBuilder targetCreateTable,
            final String attrName,
            final String attrTypeName,
            final boolean optional,
            final Integer precision,
            final Integer scale,
            final String defaultValue) {
        // probably we have to do type conversion if a cids type is not a valid dbms type
        targetCreateTable.append(attrName).append(' ').append(attrTypeName.toUpperCase()); // NOI18N
        if (precision != null) {
            targetCreateTable.append('(').append(precision);                               // NOI18N
            if (scale != null) {
                targetCreateTable.append(", ").append(scale);                              // NOI18N
            }
            targetCreateTable.append(')');                                                 // NOI18N
        }
        if (!optional) {
            targetCreateTable.append(" NOT");                                              // NOI18N
        }
        targetCreateTable.append(" NULL");                                                 // NOI18N

        if (defaultValue != null) {
            targetCreateTable.append(" DEFAULT ").append(defaultValue); // NOI18N
        }

        return targetCreateTable;
    }

    @Override
    public final Map<String, String> getTypeMap(final boolean directionTo) {
        final ResourceBundle bundle = ResourceBundle.getBundle(getTypeMapBundle()); // NOI18N

        final Map<String, String> map = new HashMap<String, String>();
        final Enumeration<String> keys = bundle.getKeys();
        while (keys.hasMoreElements()) {
            final String key = keys.nextElement();
            // for bidirectional mapping
            if (directionTo) {
                map.put(key, bundle.getString(key));
            } else {
                map.put(bundle.getString(key), key);
            }
        }

        return map;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract String getTypeMapBundle();
}
