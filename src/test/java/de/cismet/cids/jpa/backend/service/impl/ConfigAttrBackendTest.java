/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service.impl;

import de.cismet.cids.jpa.backend.service.Backend;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrEntry;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrKey;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Properties;

import de.cismet.diff.db.DatabaseConnection;

import de.cismet.tools.ScriptRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public class ConfigAttrBackendTest {

    //~ Static fields/initializers ---------------------------------------------

    private static Properties runtimeProperties;
    private static Backend backend;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConfigAttrBackendTest object.
     */
    public ConfigAttrBackendTest() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        runtimeProperties = new Properties();
        runtimeProperties.load(ConfigAttrBackendTest.class.getResourceAsStream("runtime.properties")); // NOI18N
        backend = BackendFactory.getInstance().getBackend(runtimeProperties);
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
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Before
    public void setUp() throws Exception {
        final ScriptRunner runner = new ScriptRunner(DatabaseConnection.getConnection(runtimeProperties), false, true);
        final InputStream scriptStream = ConfigAttrBackendTest.class.getResourceAsStream("configAttrTestData.sql"); // NOI18N
        final BufferedReader scriptReader = new BufferedReader(new InputStreamReader(scriptStream));
        try {
            runner.runScript(scriptReader);
        } finally {
            scriptReader.close();
        }
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
     * DOCUMENT ME!
     */
    @Ignore
    @Test
    public void testGetEntries_ConfigAttrKey() {
    }

    /**
     * DOCUMENT ME!
     */
    @Ignore
    @Test
    public void testGetEntries_ConfigAttrTypeTypes() {
    }

    /**
     * DOCUMENT ME!
     */
    @Ignore
    @Test
    public void testStoreEntry() {
    }

    /**
     * DOCUMENT ME!
     */
    @Test
    public void testCleanAttributeTables() {
        System.out.println("TEST " + getCurrentMethodName());

        backend.cleanAttributeTables();

        final List<ConfigAttrKey> keys = backend.getAllEntities(ConfigAttrKey.class);
        final List<ConfigAttrValue> values = backend.getAllEntities(ConfigAttrValue.class);

        assertTrue(keys.size() == 5);
        assertTrue(values.size() == 15);
    }

    @Ignore
    @Test
    public void testContains(){
        System.out.println("TEST " + getCurrentMethodName());
        final List<ConfigAttrEntry> entries = backend.getAllEntities(ConfigAttrEntry.class);
        assertTrue(backend.contains(entries.get(0)));
    }
}
