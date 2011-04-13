/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.core;

import org.apache.log4j.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.persistence.NoResultException;

import de.cismet.cids.jpa.backend.service.Backend;
import de.cismet.cids.jpa.backend.service.impl.BackendFactory;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.Icon;
import de.cismet.cids.jpa.entity.cidsclass.Type;

import de.cismet.remotetesthelper.ws.rest.RemoteTestHelperClient;

import de.cismet.tools.ScriptRunner;
import org.junit.Ignore;

import static org.junit.Assert.*;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public class PersistenceCacheTest {

    //~ Static fields/initializers ---------------------------------------------

    private static final String TEST_DB_NAME = PersistenceCacheTest.class.getSimpleName() + "_test_db";
    private static final RemoteTestHelperClient CLIENT = new RemoteTestHelperClient();

    private static final transient Logger LOG = Logger.getLogger(PersistenceCacheTest.class);

    //~ Instance fields --------------------------------------------------------

    private transient Backend backend;

    private transient Connection con;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PersistenceCacheTest object.
     */
    public PersistenceCacheTest() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception              DOCUMENT ME!
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        final Properties p = new Properties();
        p.put("log4j.appender.Remote", "org.apache.log4j.net.SocketAppender");
        p.put("log4j.appender.Remote.remoteHost", "localhost");
        p.put("log4j.appender.Remote.port", "4445");
        p.put("log4j.appender.Remote.locationInfo", "true");
        p.put("log4j.rootLogger", "ALL,Remote");
        org.apache.log4j.PropertyConfigurator.configure(p);

        if (!Boolean.valueOf(CLIENT.initCidsSystem(TEST_DB_NAME))) {
            throw new IllegalStateException("cannot initilise test db");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception              DOCUMENT ME!
     * @throws  IllegalStateException  DOCUMENT ME!
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        if (!Boolean.valueOf(CLIENT.dropDatabase(TEST_DB_NAME))) {
            throw new IllegalStateException("could not drop test db");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Before
    public void setUp() throws Exception {
        try {
            con = CLIENT.getConnection(TEST_DB_NAME);
            backend = BackendFactory.getInstance().getBackend(createRuntimeProperties(con), true);
            final ScriptRunner runner = new ScriptRunner(con, true, false);
            runner.runScript(new BufferedReader(
                    new InputStreamReader(
                        PersistenceCacheTest.class.getResourceAsStream("persistence_cache_test.sql"))));
        } catch (Exception e) {
            con.close();
            throw e;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   con  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private Properties createRuntimeProperties(final Connection con) throws Exception {
        final Properties props = new Properties();

        final DatabaseMetaData meta = con.getMetaData();
        props.setProperty("connection.url", meta.getURL());
        props.setProperty("connection.username", meta.getUserName());
        props.setProperty("connection.password", "x");
        props.setProperty("connection.driver_class", "org.postgresql.Driver");
        props.setProperty("dialect", "org.hibernate.dialect.PostgreSQLDialect");

        return props;
    }

    /**
     * DOCUMENT ME!
     */
    @After
    public void tearDown() {
        try {
            backend.close();
        } catch (final Exception e) {
            LOG.warn("could not close backend", e);
        }

        try {
            con.close();
        } catch (final SQLException ex) {
            LOG.warn("could not close connection", ex); // NOI18N
        }
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
     *
     * @throws  Exception  DOCUMENT ME!
     */
    // ig
    @Ignore
    @Test
    public void testCache() throws Exception {
        // get tests
        final List<CidsClass> allClasses = new ArrayList<CidsClass>(backend.getAllEntities(CidsClass.class));
        List<CidsClass> allClassesFromCache = backend.getAllEntities(CidsClass.class);

        ResultSet set = con.createStatement().executeQuery("select count(id) from cs_class");
        set.next();

        assertTrue("should have found " + set.getInt(1) + " classes", allClassesFromCache.size() == set.getInt(1));
        assertEquals(allClasses.toArray(), allClassesFromCache.toArray()); // should always succeed since the first
                                                                           // call initialises the cache and
                                                                           // retrieves the data from it afterwards

        final List<Type> allTypes = new ArrayList<Type>(backend.getAllEntities(Type.class));
        List<Type> allTypesFromCache = backend.getAllEntities(Type.class);

        set = con.createStatement().executeQuery("select count(id) from cs_type");
        set.next();

        assertTrue("should have found " + set.getInt(1) + " types", allTypesFromCache.size() == set.getInt(1));
        assertEquals(allTypes.toArray(), allTypesFromCache.toArray()); // same as for classes

        final List<Icon> allIcons = new ArrayList<Icon>(backend.getAllEntities(Icon.class));
        List<Icon> allIconsFromCache = backend.getAllEntities(Icon.class);

        set = con.createStatement().executeQuery("select count(id) from cs_icon");
        set.next();

        assertTrue("should have found " + set.getInt(1) + " icons", allIconsFromCache.size() == set.getInt(1));
        assertEquals(allIcons.toArray(), allIconsFromCache.toArray()); // same as for classes

        // insert tests

        Icon storedIcon = new Icon();
        storedIcon.setName("a");
        storedIcon.setFileName("a");

        storedIcon = backend.store(storedIcon);

        set = con.createStatement().executeQuery("select count(id) from cs_icon");
        set.next();

        allIconsFromCache = backend.getAllEntities(Icon.class);
        assertTrue("should have found " + set.getInt(1) + " icons",
            (allIconsFromCache.size() == set.getInt(1))
                    && (allIconsFromCache.size() == (allIcons.size() + 1)));

        set = con.createStatement().executeQuery("select max(id) from cs_icon");
        set.next();

        Icon storedIconFromCacheById = null;
        Icon storedIconFromCacheByName = null;
        try {
            storedIconFromCacheById = backend.getEntity(Icon.class, set.getInt(1));
            storedIconFromCacheByName = backend.getEntity(Icon.class, "a");
        } catch (final NoResultException noResultException) {
            fail("should have found the new icon, cache inconsistent: " + noResultException);
        }

        assertEquals("cache inconsistent", storedIcon, storedIconFromCacheById);
        assertEquals("cache inconsistent", storedIcon, storedIconFromCacheByName);
        assertTrue("cache inconsistent", backend.contains(Icon.class, "a"));

        final CidsClass newClass = new CidsClass();
        newClass.setName("test7");
        newClass.setTableName("test7");
        newClass.setPrimaryKeyField("ID");
        newClass.setArrayLink(Boolean.FALSE);
        newClass.setIndexed(Boolean.FALSE);
        newClass.setClassIcon(storedIcon);
        newClass.setObjectIcon(storedIcon);

        final CidsClass storedClass = backend.store(newClass);

        allClassesFromCache = backend.getAllEntities(CidsClass.class);

        set = con.createStatement().executeQuery("select count(id) from cs_class");
        set.next();

        assertTrue("should have found " + set.getInt(1) + " classes",
            (allClassesFromCache.size() == set.getInt(1))
                    && (allClassesFromCache.size()
                        == (allClasses.size() + 1)));

        set = con.createStatement().executeQuery("select max(id) from cs_class");
        set.next();

        CidsClass storedClassFromCacheById = null;
        CidsClass storedClassFromCacheByName = null;
        try {
            storedClassFromCacheById = backend.getEntity(CidsClass.class, set.getInt(1));
            storedClassFromCacheByName = backend.getEntity(CidsClass.class, "test7");
        } catch (final NoResultException noResultException) {
            fail("should have found the new class, cache inconsistent: " + noResultException);
        }

        assertEquals("cache inconsistent", storedClass, storedClassFromCacheById);
        assertEquals("cache inconsistent", storedClass, storedClassFromCacheByName);
        assertTrue("cache inconsistent", backend.contains(CidsClass.class, "test7"));

        final Type newClassType = new Type();
        newClassType.setName("test7");
        newClassType.setCidsClass(storedClass);
        newClassType.setComplexType(Boolean.TRUE);

        final Type storedType = backend.store(newClassType);

        set = con.createStatement().executeQuery("select count(id) from cs_type");
        set.next();

        allTypesFromCache = backend.getAllEntities(Type.class);

        assertTrue("should have found " + set.getInt(1) + " types",
            (allTypesFromCache.size() == set.getInt(1))
                    && (allTypesFromCache.size()
                        == (allTypes.size() + 1)));

        set = con.createStatement().executeQuery("select max(id) from cs_type");
        set.next();

        Type storedTypeFromCacheById = null;
        Type storedTypeFromCacheByName = null;
        try {
            storedTypeFromCacheById = backend.getEntity(Type.class, set.getInt(1));
            storedTypeFromCacheByName = backend.getEntity(Type.class, "test7");
        } catch (final NoResultException e) {
            fail("should have found the new types, cache inconsistent: " + e);
        }

        assertEquals("cache inconsistent", storedType, storedTypeFromCacheById);
        assertEquals("cache inconsistent", storedType, storedTypeFromCacheByName);
        assertTrue("cache inconsistent", backend.contains(Type.class, "test7"));

        // modify tests

        // delete tests

        set = con.createStatement().executeQuery("select max(id) from cs_class");
        set.next();
        storedClass.setType(storedType);
        backend.delete(storedClass);
        try {
            storedClassFromCacheById = backend.getEntity(CidsClass.class, set.getInt(1));
            fail("shouldn't have found the instance since it is deleted, cache inconsistent: "
                        + storedClassFromCacheById);
        } catch (final NoResultException e) {
            // it's all right
        }
        try {
            storedClassFromCacheByName = backend.getEntity(CidsClass.class, "test7");
            fail("shouldn't have found the instance since it is deleted, cache inconsistent: "
                        + storedClassFromCacheByName);
        } catch (final NoResultException e) {
            // it's all right
        }
        assertFalse("cache inconsistent", backend.contains(CidsClass.class, "test7"));

        // the type should also be deleted from the cache as it is cascaded



        backend.delete((List)allIconsFromCache);
        set = con.createStatement().executeQuery("select count(id) from cs_icon");
        set.next();
        allIconsFromCache = backend.getAllEntities(Icon.class);

        assertTrue("not all icons deleted, remaining: " + set.getInt(1),
            allIconsFromCache.isEmpty()
                    && (set.getInt(1) == 0));
    }
}
