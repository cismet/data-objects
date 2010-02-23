/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.diff;

import de.cismet.cids.jpa.backend.service.impl.Backend;
import de.cismet.diff.container.PSQLStatementGroup;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mscholl
 */
public class DiffAccessorTest
{

    public DiffAccessorTest()
    {
    }

    @BeforeClass
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
        org.apache.log4j.PropertyConfigurator.configure(p);
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    private String getCurrentMethodName()
    {
        return new Throwable().getStackTrace()[1].getMethodName();
    }

    /**
     * Test of putDropAction method, of class DiffAccessor.
     */
    @Ignore
    @Test
    public void testPutDropAction()
    {
        System.out.println("putDropAction");
        String affectedTable = "";
        DiffAccessor instance = null;
        instance.putDropAction(affectedTable);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatementGroups method, of class DiffAccessor.
     */
    @Ignore
    @Test
    public void testGetStatementGroups() throws Exception
    {
        System.out.println("TEST " + getCurrentMethodName());
        try
        {
            final Properties prop = new Properties();
            prop.load(new BufferedInputStream(new FileInputStream(
                    "/Users/mscholl/cvswork6/testauslieferung/" +
                    "cidsDistribution/abf_dev_20090320/runtime.properties")));
            final Backend b = new Backend(prop);
            final DiffAccessor da = new DiffAccessor(prop, b);
            final PSQLStatementGroup[] grp = da.getStatementGroups();
            System.out.println(grp.length);
        }catch(final Exception e)
        {
            e.printStackTrace();
            fail("could not get statements");
        }
        System.out.println("TEST " + getCurrentMethodName() + " SUCCEEDED");
    }

    /**
     * Test of removeActions method, of class DiffAccessor.
     */
    @Ignore
    @Test
    public void testRemoveActions()
    {
        System.out.println("removeActions");
        DiffAccessor instance = null;
        instance.removeActions();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of freeResources method, of class DiffAccessor.
     */
    @Ignore
    @Test
    public void testFreeResources()
    {
        System.out.println("freeResources");
        DiffAccessor instance = null;
        instance.freeResources();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}