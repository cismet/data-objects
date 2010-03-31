/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.maintenance;

import de.cismet.cids.maintenance.container.Row;
import de.cismet.cids.maintenance.util.DefaultInspectionResult;

import de.cismet.diff.container.Table;
import de.cismet.diff.container.TableColumn;

import de.cismet.diff.db.DatabaseConnection;
import de.cismet.diff.db.SimpleTablesDataProvider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class MaintenanceBackend {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(MaintenanceBackend.class);

    protected static final String FOREIGN_KEY_PATTERN = ".+_[iI][dD]";                                 // NOI18N
    protected static final String SQL_PATTERN = "SELECT * FROM ? WHERE ? NOT IN ( SELECT id FROM ? )"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private transient Properties properties;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MaintenanceBackend object.
     *
     * @param   properties  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public MaintenanceBackend(final Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException("properties must not be null"); // NOI18N
        }
        this.properties = properties;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   tablename  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public InspectionResult checkTable(final String tablename) {
        if (properties == null) {
            return null;
        }
        Connection con = null;
        ResultSet set = null;
        try {
            con = DatabaseConnection.getConnection(properties);
            final DefaultInspectionResult result = new DefaultInspectionResult();
            final SimpleTablesDataProvider provider = new SimpleTablesDataProvider(properties);
            // first columnname then name of the table the key possibly points at
            final Map<String, String> param = new HashMap<String, String>();
            final List<String> colnames = new ArrayList<String>();
            final Table check = new Table(tablename, provider.getColumns(tablename));
            for (TableColumn t : provider.getColumns(tablename)) {
                if (t.getColumnName().matches(FOREIGN_KEY_PATTERN)) {
                    final String colname = t.getColumnName();
                    final String tname = "cs_" + colname.substring(0, colname.indexOf("_id"));    // NOI18N
                    param.put(colname, tname);
                    colnames.add(colname);
                }
            }
            if (param.isEmpty()) {
                result.setResultMessage(
                    org.openide.util.NbBundle.getMessage(
                        MaintenanceBackend.class,
                        "MaintenanceBackend.resultmessage.noForeignkeysFound"));// NOI18N
                result.setCode(InspectionResult.CODE_NO_KEYS);
                result.setTable(check);
                result.setErroneousRows(null);
                result.setErroneousColumnCount(0);
                return result;
            }
            final List<Row> rows = new ArrayList<Row>();
            int errorColumnCount = 0;
            for (String colname : colnames) {
                final String sql =
                    SQL_PATTERN.replaceFirst("\\?", tablename)                                    // NOI18N
                    .replaceFirst("\\?", colname)                                                 // NOI18N
                    .replaceFirst("\\?", param.get(colname));                                     // NOI18N
                if (LOG.isDebugEnabled()) {
                    LOG.debug("sql string built: " + sql);                                        // NOI18N
                }
                try {
                    set = con.createStatement().executeQuery(sql);
                } catch (SQLException ex) {
                    LOG.warn("could not execute query", ex);                                      // NOI18N
                    continue;
                }
                int countForColumn = 0;
                while (set.next()) {
                    countForColumn++;
                    final List data = new ArrayList(check.getColumnNames().length);
                    for (String cname : check.getColumnNames()) {
                        data.add(set.getObject(cname));
                    }
                    final List errorCol = new ArrayList(1);
                    errorCol.add(colname);
                    rows.add(new Row(check, data, errorCol));
                }
                if (countForColumn != 0) {
                    errorColumnCount++;
                }
            }
            result.setTable(check);
            result.setErroneousRows(rows);
            result.setErroneousColumnCount(errorColumnCount);
            if (colnames.size() == 1) {
                if (errorColumnCount > 0) {
                    result.setCode(InspectionResult.CODE_ONE_KEY_ERROR);
                    result.setResultMessage(
                        org.openide.util.NbBundle.getMessage(
                            MaintenanceBackend.class,
                            "MaintenanceBackend.resultmessage.columnWithFKAndErrorsFound"));      // NOI18N
                } else {
                    result.setCode(InspectionResult.CODE_ONE_KEY);
                    result.setResultMessage(
                        org.openide.util.NbBundle.getMessage(
                            MaintenanceBackend.class,
                            "MaintenanceBackend.resultmessage.columnWithFKWithoutErrorsFound"));  // NOI18N
                }
            } else {
                if (errorColumnCount > 0) {
                    result.setCode(InspectionResult.CODE_MULTIPLE_KEYS_ERROR);
                    result.setResultMessage(
                        org.openide.util.NbBundle.getMessage(
                            MaintenanceBackend.class,
                            "MaintenanceBackend.resultmessage.columnsWithFKAndErrorsFound"));     // NOI18N
                } else {
                    result.setCode(InspectionResult.CODE_MULTIPLE_KEYS);
                    result.setResultMessage(
                        org.openide.util.NbBundle.getMessage(
                            MaintenanceBackend.class,
                            "MaintenanceBackend.resultmessage.columnsWithFKWithoutErrorsFound")); // NOI18N
                }
            }
            return result;
        } catch (final SQLException ex) {
            LOG.error("error while investigating database", ex);                                  // NOI18N
        } finally {
            DatabaseConnection.closeResultSet(set);
            DatabaseConnection.closeConnection(con);
        }
        return null;
    }
}
