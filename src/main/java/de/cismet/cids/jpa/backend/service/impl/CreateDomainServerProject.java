/*
 * createDomainServerProject.java
 *
 * Created on 30. November 2007, 12:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.backend.service.impl;




import de.cismet.tools.PasswordEncrypter;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.zip.ZipInputStream;



/**
 *
 * @author cschmidt
 */
public class CreateDomainServerProject {
    
    
    private final String JDBC_DRIVER = "org.postgresql.Driver";
    private final transient Logger log = Logger.getLogger(this.getClass());    
    private String projectName;
    private String dbConnection;
    private String dbName;
    private String dbUser;
    private String dbPassword;  
    private static final int BUFFER = 2048; 
    private InputStream inStream;   
    private File dir;
   
    
    
    /** Creates a new instance of createDomainServerProject */
    public CreateDomainServerProject(String dbConnection, String dbName,
            String dbUser, String dbPassword, String projectName, 
            File directory)     
    { 
       inStream = this.getClass().getResourceAsStream(
               "/de/cismet/cids/meta/basicCidsDomainServer.zip");
       setDbConnection(dbConnection);
       setDbName(dbName);
       setDbUser(dbUser);
       setDbPassword(dbPassword);
       setProjectName(projectName);
       setDir(directory);
    }
    
    public boolean createProject()
    {            
//       log.debug("Zipfile:"+zipfile.getAbsolutePath()+" exists ? :"+zipfile.exists());
        try 
        {
            extractArchive(inStream, new File(getDir().getPath()+File.separator+getProjectName()));
        } 
        catch (FileNotFoundException ex) 
        {
            log.error("File not found exception", ex);
            ex.printStackTrace();
            return false;
        } 
       catch (final IOException ex) 
       {
           log.error("IOException", ex);
           ex.printStackTrace();
           EventQueue.invokeLater(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null,
                        "Die Sprache pgsql konnte nicht installiert werden.\n "+
                        "\n\n\n"+ex.getStackTrace(),"Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            });       
           return false;
       }
       catch (Exception ex)
       {
           return false;
       }
       
       return true;
       
    }     

    
    public boolean createDatabase()
    {
        Connection con = null;
        try 
        {            
            Class.forName(JDBC_DRIVER);                       
            con = DriverManager.getConnection(getDbConnection(),getDbUser(),
                    getDbPassword()); 
            
            Statement stmntCreateDb = con.createStatement();
            String createdb ="CREATE DATABASE "+getDbName();
//            String createdb ="CREATE DATABASE "+getDbName()+
//                    " ENCODING 'Latin9'";      
            stmntCreateDb.executeUpdate(createdb);
            stmntCreateDb.close();
            con.close();                 
        } 
        catch (final SQLException ex) 
        {
            log.error("Could not create Database", ex);
            ex.printStackTrace();
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null,
                        "Datenbank konnte nicht erstellt werden.\n "+
                        "\nMögliche Ursachen:\n"+
                        "\n-Ein Fehler im Connectionstring (falscher Host oder Port)."+
                        "\n-Der Datenbankserver nimmt keine TCP/IP-Verbindungen an."+
                        "\n-Es existiert bereits eine Datenbank mit dem Namen. "+
                        dbName+" "+
                        "\n-Der eingegebene Benutzername existiert nicht\n\t bzw "+
                        "verfügt nicht über die benötigten Rechte\n"+
                        "\nCreate Metasystem wird abgebrochen!"+
                            "\n\n\n\n"+ex.getStackTrace(),"Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            });             
            return false;
            
        }
        catch (ClassNotFoundException ex)
        {
            log.error("DriverClass could not be found", ex);
            ex.printStackTrace();
            return false;
        }
        finally
        {
            try 
            {   if(con != null)
                    con.close();
            } 
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        
        return true;
    }
    
    public void dropDatabase(){
        Connection con = null;
        try 
        {            
            Class.forName(JDBC_DRIVER);                       
            con = DriverManager.getConnection(getDbConnection(),getDbUser(),
                    getDbPassword()); 
            
            Statement stmntCreateDb = con.createStatement();
            String dropdb ="DROP DATABASE "+getDbName();      
            stmntCreateDb.executeUpdate(dropdb);
            stmntCreateDb.close();
            con.close();                 
        }
        catch (ClassNotFoundException ex)
        {
            log.error("DriverClass could not be found", ex);           
        }
        catch(SQLException ex)
        {
            log.error("Could not drop Database", ex);
        }
        
    }
    public void dropProject()
    {
        File project = new File(dir,projectName);
        if(project.exists())
        {
            project.delete();
        }
    }
    
    public boolean createLanguage()
    {
        Connection con = null;
        try {
        
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(getDbConnection()+"/"+getDbName(),
                    getDbUser(),getDbPassword());
            
            String existsLanguage =
                    "Select count (*) as exists from pg_language where lanname like 'plpgsql'";
            Statement stmntExistsLanguage = con.createStatement();
            ResultSet rs = stmntExistsLanguage.executeQuery(existsLanguage);
            rs.next();
            int exists = rs.getInt("exists");
//            log.fatal("Wert von exists: "+exists);
            if( exists == 0)
            {
                String createHandler = 
                        "CREATE OR REPLACE FUNCTION plpgsql_call_handler() "+
                        "RETURNS language_handler AS '$libdir/plpgsql' LANGUAGE C;";            

                Statement stmntCreateHandler = con.createStatement();
                stmntCreateHandler.executeUpdate(createHandler);
                stmntCreateHandler.close();

                Statement stmntCreateLang = con.createStatement();
                String createLang ="CREATE TRUSTED PROCEDURAL LANGUAGE 'plpgsql' "+
                        "HANDLER plpgsql_call_handler ;";
                stmntCreateLang.executeUpdate(createLang);
                stmntCreateLang.close();
                log.debug("Create Language 'plpqsql' auf der Datenbank: "+getDbName());    

                con.close();            
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            log.error("Could not find JDBC-Driver Class", ex);  
            
            return false;
        } catch (final SQLException ex) {
            ex.printStackTrace();
            log.error("Could not create Language + ErrorCode: "+
                    ex.getErrorCode(), ex);
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null,
                        "Die Sprache pgsql konnte nicht installiert werden.\n "+
                        "\n\n\n"+ex.getStackTrace(),"Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            });             
            return false;
        }
        finally
        {
            try 
            {   if(con != null)
                    con.close();
            } 
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return true;       
    }
    
    
    /*
     *
     *  Enthält Workaround: Die spatial_ref_sys - scriptDatein können sehr groß werden,
     *  das kann zu Problemen führen beim Versuch diese als ein Skript mit 
     *  Statement.execute(Skript) auszuführen. Daher wird diese Datei als Batch 
     *  geparst und per addbatch und executeBatch ausgeführt. Das parsen wird
     *  anhand des Semikolons als Query-End-Zeichen durchgeführt. Alle anderen Skriptdatein
     *  werden per Statement.execute ausgeführt, da hier das Semikolon nicht immer 
     *  das Ende einer Query anzeigt. z.B innerhalb der Entwertungszeichen ' ' (Hochkomma)
     *
     */
    public boolean executeSqlScriptFiles(String dbCon, String dbName, String uName,
            String pwd)
    { 
        Connection con = null;
        try{
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(dbCon+"/"+dbName,
                    uName, pwd);
            StringBuffer script;
            String s;
            InputStream postgisSql = null;
            InputStream spatialRefSysSql = null;
            
            if(con.getMetaData().getDatabaseMajorVersion() == 7)
            {
                postgisSql = this.getClass().getResourceAsStream(
                    "/de/cismet/cids/meta/postgis7_4.sql");
                spatialRefSysSql =this.getClass().getResourceAsStream(
                    "/de/cismet/cids/meta/spatial_ref_sys7_4.sql");
            }
            else if(con.getMetaData().getDatabaseMajorVersion() == 8)
            {
                postgisSql = this.getClass().getResourceAsStream(
                    "/de/cismet/cids/meta/lwpostgis8.sql");
                spatialRefSysSql =this.getClass().getResourceAsStream(
                    "/de/cismet/cids/meta/spatial_ref_sys8.sql");
            }
            InputStream postgresCreate = this.getClass().getResourceAsStream(
                    "/de/cismet/cids/meta/"+
                    "postgresql_create_new_cids_table_set.jdbc_sql.sql");

            
            ArrayList<InputStream> streamList = new ArrayList<InputStream>();
            streamList.add(postgisSql);
            streamList.add(spatialRefSysSql);
            streamList.add(postgresCreate);
            ListIterator<InputStream> itStreams = streamList.listIterator();        
            int zaehler = 0;
            while(itStreams.hasNext())
            {
                log.debug("Welches Skript wird gerade bearbeitet? Nummer: "+ ++zaehler);
                InputStream curFile = itStreams.next();
                s = new String();
                script = new StringBuffer();
                BufferedReader in1 = new BufferedReader(new InputStreamReader(curFile)); 
                Statement stmntExecuteScript1 = con.createStatement();  
                if(zaehler == 2)
                {
                    while ((s = in1.readLine()) != null)
                    {                     
                        if(!s.startsWith("--"))
                        {
                            if(s.contains("--"))
                            {
                                s = s.substring(0,s.indexOf("--"));                            
                            }
                            script.append(s+"\r\n");
                            if(s.contains(";"))
                            {                            
                                stmntExecuteScript1.addBatch(script.toString());
                                script.setLength(0);                            
                            }
                        }                                       
                    }
                    stmntExecuteScript1.executeBatch();
                }
                else
                {
                    while ((s = in1.readLine()) != null)
                    {                     
                        if(!s.startsWith("--"))
                        {
                            if(s.contains("--"))
                            {
                                s = s.substring(0,s.indexOf("--"));                            
                            }
                            script.append(s+"\r\n");                            
                        }                                       
                    }
                    stmntExecuteScript1.execute(script.toString(),
                        Statement.NO_GENERATED_KEYS);
                }
                stmntExecuteScript1.close();        
                in1.close();
            }      
        }
        catch (FileNotFoundException ex) 
        {
            log.error("Could not find file", ex);
            ex.printStackTrace();
            return false;
        }
        catch (IOException ex)
        {
            log.error("Could not open File", ex);
            ex.printStackTrace();
            return false;
        }
        catch (final SQLException ex) 
        {
            log.error("Could not execute Scriptfile", ex);
            log.error("Aufruf get nextException(): ",ex.getNextException());
            ex.printStackTrace();
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    JOptionPane.showMessageDialog(null,
                        "Die Die Skriptdatein konnten nicht ausgeführt werden.\n "+
                        "\n\n\n"+ex.getStackTrace(),"Fehler",
                            JOptionPane.ERROR_MESSAGE);
                }
            });  
            return false;
        }
        catch(Exception ex)
        {
            log.error("Error ", ex);
            return false;
        }
        finally
        {
            try 
            {
                if(con != null)
                    con.close();
            } 
            catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        log.debug("Skriptfiles abgearbeitet");
        return true;        
    }
    
    public boolean editProperties(File propDirectory, String connection,
            String dbName, String userName, String pwd, String projName,
            String webServerPort, String rmiServerPort,  String  mode)
    {
        try 
        {

            final File f = new File(propDirectory, "runtime.properties");
//            log.fatal("runtime.properties pfad: "+f.getPath());
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(f)));
            String nextLine;
            StringBuffer buf = new StringBuffer();
            while(br.ready())
            {
                nextLine = br.readLine();
//                log.fatal("Inhalt nextLine : "+nextLine);
                if(nextLine.startsWith("connection.url="))
                {
                    nextLine = "connection.url="+connection+"/"+dbName;
                }
                else if(nextLine.startsWith("connection.username="))
                {
                    nextLine = "connection.username="+userName;
                }
                else if(nextLine.startsWith("connection.password="))
                {
                    nextLine = "connection.password="+PasswordEncrypter.encryptString(pwd);
                }
                else if(nextLine.startsWith("serverTitle="))
                {
                    nextLine = "serverTitle="+"cids Domainserver ("+projName+")";
                }
                else if(nextLine.startsWith("webserverPort="))
                {
                    nextLine = "webserverPort=" + webServerPort;
                }
                else if(nextLine.startsWith("serverPort="))
                {
                    nextLine = "serverPort="+ rmiServerPort;
                }
                else if(nextLine.startsWith("startMode="))
                {
                    nextLine = "startMode=" + mode;
                }
                else if (nextLine.startsWith("serverName="))
                {
                    nextLine = "serverName=" + projName;
                }
                buf.append(nextLine+"\n");                            
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(buf.toString());
            bw.flush();
            bw.close();
            return true;
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
            return false;
        }
        catch (Exception ex)
        {
            log.error("Unexpected Exception : ", ex);
            return false;
        }
    }

    public Logger getLog() {
        return log;
    }  

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDbConnection() {
        return dbConnection;
    }

    public void setDbConnection(String dbConnection) {
        this.dbConnection = dbConnection;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }   

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    } 
    
    
   public void extractArchive(InputStream inStream, File destDir) throws Exception {
        ZipInputStream zipIS = new ZipInputStream(inStream);
        ZipEntry entry = zipIS.getNextEntry();
        File newFile;
        if(!destDir.exists())
            destDir.mkdirs();
        while (entry != null) {
            newFile = new File(destDir, entry.getName());
            if (entry.isDirectory()) {                      //wird bei ZipInputStream nicht immer korrekt erkannt !!!!!
//                log.fatal("Make dirs : "+entry.getName());
                newFile.mkdirs();
            } else {                                    //daher erneute prüfung notwendig
                if(!newFile.getParentFile().exists())   //falls der Pfad noch nicht existiert
                    newFile.getParentFile().mkdirs();   // leg ihn an!
                log.debug("newFile.getPath : "+ newFile.getPath());
                newFile.createNewFile();                
                OutputStream out = new FileOutputStream(newFile);
                writeOutFile(out, zipIS);
                out.close();
            }
            entry = zipIS.getNextEntry();
        }
        zipIS.close();        
    }


    /**
     * Writes the contents of the given InputStream to the given OutStream.
     * Will write bytes until <code>in</code> returns EOF.  Neither
     * stream is closed by this method.
     *
     * @param out Stream to write data to
     * @param in  Stream to read data from
     * @throws IOException If unable to read from <code>in</code> or write to
<code>out</code>
     */
    public static void writeOutFile(OutputStream out, InputStream in) throws
                IOException {
        byte buf[] = new byte[1024];
        int read;
        while (true) {
            read = in.read(buf);
            if (read == -1) {
                break;
            }
            out.write(buf, 0, read);
        }
    }
     
    
   





    
    
    
}