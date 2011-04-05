/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.diff;

import de.cismet.cids.jpa.backend.service.Backend;
import de.cismet.cids.jpa.backend.service.impl.BackendFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import java.util.Properties;


import de.cismet.diff.container.PSQLStatementGroup;

import static org.junit.Assert.*;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class DiffAccessorTest {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DiffAccessorTest object.
     */
    public DiffAccessorTest() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
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

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * DOCUMENT ME!
     */
    @Before
    public void setUp() {
    }

    /**
     * DOCUMENT ME!
     */
    @After
    public void tearDown() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getCurrentMethodName() {
        return new Throwable().getStackTrace()[1].getMethodName();
    }

    /**
     * Test of putDropAction method, of class DiffAccessor.
     */
    @Ignore
    @Test
    public void testPutDropAction() {
        System.out.println("putDropAction");
        final String affectedTable = "";
        final DiffAccessor instance = null;
        instance.putDropAction(affectedTable);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStatementGroups method, of class DiffAccessor.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Ignore
    @Test
    public void testGetStatementGroups() throws Exception {
        System.out.println("TEST " + getCurrentMethodName());
        try {
            final Properties prop = new Properties();
            prop.load(new BufferedInputStream(
                    new FileInputStream(
                        "/Users/mscholl/cvswork6/testauslieferung/"
                        + "cidsDistribution/abf_dev_20090320/runtime.properties")));
            final Backend b = BackendFactory.getInstance().getBackend(prop);
            final DiffAccessor da = new DiffAccessor(prop, b);
            final PSQLStatementGroup[] grp = da.getStatementGroups();
            System.out.println(grp.length);
        } catch (final Exception e) {
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
    public void testRemoveActions() {
        System.out.println("removeActions");
        final DiffAccessor instance = null;
        instance.removeActions();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of freeResources method, of class DiffAccessor.
     */
    @Ignore
    @Test
    public void testFreeResources() {
        System.out.println("freeResources");
        final DiffAccessor instance = null;
        instance.freeResources();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
