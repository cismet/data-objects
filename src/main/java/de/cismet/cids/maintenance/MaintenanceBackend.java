/*
 * MaintenanceBackend.java
 *
 * Created on 13. September 2007, 15:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.maintenance;

import de.cismet.cids.maintenance.container.Row;
import de.cismet.cids.maintenance.util.DefaultInspectionResult;
import de.cismet.diff.container.Table;
import de.cismet.diff.container.TableColumn;
import de.cismet.diff.db.DatabaseConnection;
import de.cismet.diff.db.SimpleTablesDataProvider;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author mscholl
 */
public class MaintenanceBackend
{
    private final transient Logger log = Logger.getLogger(this.getClass());
    
    protected static final String FOREIGN_KEY_PATTERN = ".+_[iI][dD]";
    protected static final String SQL_PATTERN = 
            "SELECT * FROM ? WHERE ? NOT IN ( SELECT id FROM ? )";
    
    private Properties properties;
    
//    static
//    {
//        Properties p = new Properties();
//        p.put("log4j.appender.Remote", "org.apache.log4j.net.SocketAppender");
//        p.put("log4j.appender.Remote.remoteHost","localhost");
//        p.put("log4j.appender.Remote.port", "4445");
//        p.put("log4j.appender.Remote.locationInfo", "true");
//        p.put("log4j.rootLogger", "DEBUG,Remote");
//        org.apache.log4j.PropertyConfigurator.configure(p);
//    }
    
    /** Creates a new instance of MaintenanceBackend */
    public MaintenanceBackend()
    {
        InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(
                "de/cismet/diff/resource/runtime.properties");
        properties = new Properties();
        try
        {
            properties.load(in);
        } catch (IOException ex)
        {
            log.error("could not load properties", ex);
        }
    }
    
    public MaintenanceBackend(Properties properties)
    {
        if(properties == null)
            throw new NullPointerException("properties must not be null");
        this.properties = properties;
    }
    
    public InspectionResult checkTable(String tablename)
    {
        if(properties == null)
            return null;
        Connection con = null;
        try
        {
            con = DatabaseConnection.getConnection(properties);
        } catch (SQLException ex)
        {
            log.error("could not retrieve database connection", ex);
            return null;
        }
        DatabaseMetaData metadata = null;
        try
        {
            metadata = con.getMetaData();
        } catch (SQLException ex)
        {
            log.error("could not retrieve database metadata", ex);
            return null;
        }
        try
        {
            DefaultInspectionResult result = new DefaultInspectionResult();
            SimpleTablesDataProvider provider = new SimpleTablesDataProvider(
                    properties);
            //first columnname then name of the table the key possibly points at
            Map<String, String> param = new HashMap<String, String>();
            Vector<String> colnames = new Vector<String>();
            Table check = new Table(tablename, provider.getColumns(tablename));
            for(TableColumn t : provider.getColumns(tablename))
            {
                if(t.getColumnName().matches(FOREIGN_KEY_PATTERN))
                {
                    String colname = t.getColumnName();
                    String tname = "cs_" + colname.substring(0, colname.indexOf(
                            "_id"));
                    param.put(colname, tname);
                    colnames.add(colname);
                }
            }
            if(param.size() == 0)
            {
                result.setResultMessage("Es wurden keine Fremdschlüssel " +
                        "gefunden");
                result.setCode(InspectionResult.CODE_NO_KEYS);
                result.setTable(check);
                result.setErroneousRows(null);
                result.setErroneousColumnCount(0);
                return result;
            }
            Vector<Row> rows = new Vector<Row>(); 
            int errorColumnCount = 0;
            for(String colname : colnames)
            {
                String sql = SQL_PATTERN.
                        replaceFirst("\\?", tablename).
                        replaceFirst("\\?", colname).
                        replaceFirst("\\?", param.get(colname));
                log.debug("sql string built: " + sql);
                ResultSet set = null;
                try
                {
                    set = con.createStatement().executeQuery(sql);
                } catch (SQLException ex)
                {
                    log.warn("could not execute query", ex);
                    continue;
                }
                int countForColumn = 0;
                while(set.next())
                {
                    countForColumn++;
                    Vector data = new Vector(check.getColumnNames().length);
                    for(String cname : check.getColumnNames())
                        data.add(set.getObject(cname));
                    Vector errorCol = new Vector(1);
                    errorCol.add(colname);
                    rows.add(new Row(check, data, errorCol));
                }
                if(countForColumn != 0)
                    errorColumnCount++;
            }
            result.setTable(check);
            result.setErroneousRows(rows);
            result.setErroneousColumnCount(errorColumnCount);
            if(colnames.size() == 1)
            {
                if(errorColumnCount > 0)
                {
                    result.setCode(InspectionResult.CODE_ONE_KEY_ERROR);
                    result.setResultMessage("Es wurde eine Spalte mit " +
                            "Fremdschlüssel gefunden und Fehler festgestellt");
                }
                else
                {
                    result.setCode(InspectionResult.CODE_ONE_KEY);
                    result.setResultMessage("Es wurde eine Spalte mit " +
                            "Fremdschlüssel gefunden und keine Fehler " +
                            "festgestellt");
                }
            }
            else
            {
                if(errorColumnCount > 0)
                {
                    result.setCode(InspectionResult.CODE_MULTIPLE_KEYS_ERROR);
                    result.setResultMessage("Es wurden mehrere Spalten mit " +
                            "Fremdschlüsseln gefunden und Fehler festgestellt");
                }
                else
                {
                    result.setCode(InspectionResult.CODE_MULTIPLE_KEYS);
                    result.setResultMessage("Es wurden mehrere Spalten mit " +
                            "Fremdschlüsseln gefunden und keine Fehler " +
                            "festgestellt");
                }
                    
            }
            return result;
        } catch (SQLException ex)
        {
            log.error("error while investigating database", ex);
        }
        return null;
    }
    
    public static void main(String[] args)
    {
        MaintenanceBackend b = new MaintenanceBackend();
        InspectionResult result = b.checkTable("cs_attr");
        System.out.println("cs_attr: " + result.getResultMessage());
        if(result.getErroneousRowCount() > 0)
        {
            for(String col : result.getTable().getColumnNames())
                System.out.print(col + "\t");
            System.out.println("");
            for(Row row : result.getErroneousRows())
            {
                System.out.println(row.toString() + " :: " + row.getErroneousEntries().firstElement().getColumnName());
            }
        }
//        System.out.println("-------------------------------------------------------------------------------------");
//        result = b.checkTable("cs_query_class_assoc");
//        System.out.println("cs_query_class_assoc: " + result.getResultMessage());
//        if(result.getErroneousRowCount() > 0)
//        {
//            for(String col : result.getTable().getColumnNames())
//                System.out.print(col + "\t");
//            System.out.println("");
//            for(Row row : result.getErroneousRows())
//            {
//                System.out.println(row.toString());
//            }
//        }
//        System.out.println("-------------------------------------------------------------------------------------");
//        result = b.checkTable("cs_query_link");
//        System.out.println("cs_query_link: " + result.getResultMessage());
//        if(result.getErroneousRowCount() > 0)
//        {
//            for(String col : result.getTable().getColumnNames())
//                System.out.print(col + "\t");
//            System.out.println("");
//            for(Row row : result.getErroneousRows())
//            {
//                System.out.println(row.toString());
//            }
//        }
//        System.out.println("-------------------------------------------------------------------------------------");
//        result = b.checkTable("cs_query_parameter");
//        System.out.println("cs_query_parameter: " + result.getResultMessage());
//        if(result.getErroneousRowCount() > 0)
//        {
//            for(String col : result.getTable().getColumnNames())
//                System.out.print(col + "\t");
//            System.out.println("");
//            for(Row row : result.getErroneousRows())
//            {
//                System.out.println(row.toString());
//            }
//        }
//        System.out.println("-------------------------------------------------------------------------------------");
//        result = b.checkTable("cs_query_store");
//        System.out.println(result.getTable().getTableName() + ": " + result.getResultMessage());
//        if(result.getErroneousRowCount() > 0)
//        {
//            for(String col : result.getTable().getColumnNames())
//                System.out.print(col + "\t");
//            System.out.println("");
//            for(Row row : result.getErroneousRows())
//            {
//                System.out.println(row.toString());
//            }
//        }
//        System.out.println("-------------------------------------------------------------------------------------");
//        result = b.checkTable("cs_query_store_ug_assoc");
//        System.out.println("cs_query_store_ug_assoc: " + result.getResultMessage());
//        if(result.getErroneousRowCount() > 0)
//        {
//            for(String col : result.getTable().getColumnNames())
//                System.out.print(col + "\t");
//            System.out.println("");
//            for(Row row : result.getErroneousRows())
//            {
//                System.out.println(row.toString());
//            }
//        }
//        System.out.println("-------------------------------------------------------------------------------------");
//        result = b.checkTable("cs_query_ug_assoc");
//        System.out.println("cs_query_ug_assoc: " + result.getResultMessage());
//        if(result.getErroneousRowCount() > 0)
//        {
//            for(String col : result.getTable().getColumnNames())
//                System.out.print(col + "\t");
//            System.out.println("");
//            for(Row row : result.getErroneousRows())
//            {
//                System.out.println(row.toString());
//            }
//        }
    }
}