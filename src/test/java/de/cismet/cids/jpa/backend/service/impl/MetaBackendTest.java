package de.cismet.cids.jpa.backend.service.impl;



import de.cismet.cids.jpa.backend.service.Backend;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.BeforeClass;


/**
 * Test is db dependant and can thus not be activated
 * @author martin.scholl@cismet.de
 */
public class MetaBackendTest
{

    private static Properties runtimeProperties;
    private static Backend backend;

//    @BeforeClass
    public static void setUpClass() throws Exception
    {
        final Properties p = new Properties();
        p.put("log4j.appender.Remote", "org.apache.log4j.net.SocketAppender");
        p.put("log4j.appender.Remote.remoteHost", "localhost");
        p.put("log4j.appender.Remote.port", "4445");
        p.put("log4j.appender.Remote.locationInfo", "true");
        p.put("log4j.rootLogger", "ALL,Remote");
        p.put("log4j.logger.org.hibernate", "WARN,Remote");
        p.put("log4j.logger.com.mchange.v2", "WARN,Remote");
        p.put("log4j.logger.net.sf.ehcache", "WARN,Remote");
        org.apache.log4j.PropertyConfigurator.configure(p);

        runtimeProperties = new Properties();
        runtimeProperties.load(MetaBackendTest.class.getResourceAsStream("runtime.properties")); // NOI18N
        backend = BackendFactory.getInstance().getBackend(runtimeProperties, false);
    }

//    @AfterClass
    public static void tearDownClass() throws Exception
    {
        backend.close();
    }

    private String getCurrentMethodName()
    {
        return new Throwable().getStackTrace()[1].getMethodName();
    }

//    @Test
    public void testReindex() throws SQLException
    {
        System.out.println("TEST " + getCurrentMethodName());
        
        final CidsClass c = new CidsClass();
        c.setId(177);
        c.setName("TestClass");
        
        backend.refreshIndex(c);
    }
}