/*
 * Backend.java
 *
 * Created on 9. Januar 2007, 15:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.backend.service.impl;

import de.cismet.cids.jpa.backend.core.PersistenceInterceptor;
import de.cismet.cids.jpa.backend.core.PersistenceProvider;
import de.cismet.cids.jpa.backend.service.CatalogService;
import de.cismet.cids.jpa.backend.service.ClassService;
import de.cismet.cids.jpa.backend.service.CommonService;
import de.cismet.cids.jpa.backend.service.MetaService;
import de.cismet.cids.jpa.backend.service.UserService;
import de.cismet.cids.jpa.entity.catalog.CatNode;
import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.Icon;
import de.cismet.cids.jpa.entity.cidsclass.JavaClass;
import de.cismet.cids.jpa.entity.cidsclass.Type;
import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.common.Domain;
import de.cismet.cids.jpa.entity.common.URL;
import de.cismet.cids.jpa.entity.common.URLBase;
import de.cismet.cids.jpa.entity.user.User;
import de.cismet.cids.util.ProgressListener;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import net.sf.tie.ProxyInjector;
import net.sf.tie.ext.InterceptionBuilder;

/**
 *
 * @author $Author: mscholl $
 * @version $Revision: 1.13 $
 * tag $Name:  $
 * date $Date: 2009/10/29 14:24:52 $
 */
public final class Backend implements 
        ClassService,
        UserService,
        CatalogService,
        MetaService, 
        CommonService
{
    final ClassService cb;
    final UserService ub;
    final CatalogService catBackend;
    final MetaService metaBackend;
    final PersistenceProvider provider;
    final CommonService commonBackend;
    
    // <editor-fold defaultstate="collapsed" desc=" Part: Constructors ">
    /** Creates a new instance of Backend */
    public Backend(final Properties properties)
    {
        provider = new PersistenceProvider(properties);
        final ClassBackend classB = new ClassBackend(provider);
        final UserBackend userB = new UserBackend(provider);
        final CatalogBackend catB = new CatalogBackend(provider);
        final MetaBackend metaB = new MetaBackend(properties);
        final InterceptionBuilder builder = new InterceptionBuilder();
        builder.always(new PersistenceInterceptor(provider));
        final ProxyInjector injector = new ProxyInjector(builder.done());
        cb = injector.wrapObject(ClassService.class, classB);
        ub = injector.wrapObject(UserService.class, userB);
        catBackend = injector.wrapObject(CatalogService.class, catB);
        metaBackend = injector.wrapObject(MetaService.class, metaB);
        commonBackend = injector.wrapObject(CommonService.class, provider);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Part: Backend operations ">
    public void close() throws Exception
    {
        // it should be enough to close the persistence provider
        provider.close();
        metaBackend.close();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Part: CommonService">
    public <T extends CommonEntity> T store(final T entity)
    {
        return commonBackend.store(entity);
    }

    public void delete(final CommonEntity ce)
    {
        commonBackend.delete(ce);
    }

    public <T extends CommonEntity> T getEntity(final Class<T> entity, final int
            id)
    {
        return commonBackend.getEntity(entity, id);
    }

    public <T extends CommonEntity> List<T> getAllEntities(final Class<T> 
            entity)
    {
        return commonBackend.getAllEntities(entity);
    }

    /**
     * assumes entity has a mapped member named 'name'
     */
    public <T extends CommonEntity> T getEntity(final Class<T> entity, final 
            String name)
    {
        return commonBackend.getEntity(entity, name);
    }

    /**
     * assumes entity has a mapped member named 'name'
     */
    public <T extends CommonEntity> boolean contains(final Class<T> entity, 
            final String name)
    {
        return commonBackend.contains(entity, name);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Part: ClassBackend ">
    public URL getURL(final String url)
    {
        return cb.getURL(url);
    }

    public List<URL> getURLsLikeURL(final URL url)
    {
        return cb.getURLsLikeURL(url);
    }

    public URL storeURL(final URL url)
    {
        return cb.storeURL(url);
    }

    public List<URL> storeURLs(final List<URL> urls)
    {
        return cb.storeURLs(urls);
    }

    public void deleteURL(final URL url)
    {
        cb.deleteURL(url);
    }

    public void deleteURLs(final List<URL> urls)
    {
        cb.deleteURLs(urls);
    }

    public void deleteURLBaseIfUnused(final URLBase urlbase)
    {
        cb.deleteURLBaseIfUnused(urlbase);
    }

    public void deleteURLBasesIfUnused(final List<URLBase> urlbases)
    {
        cb.deleteURLBasesIfUnused(urlbases);
    }
    
    public boolean contains(final JavaClass jc)
    {
        return cb.contains(jc);
    }
    
    public JavaClass getJavaClass(final String qualifier)
    {
        return cb.getJavaClass(qualifier);
    }
    
    public void deleteJavaClass(final JavaClass jc)
    {
        cb.deleteJavaClass(jc);
    }
    
    public List getSortedTypes()
    {
        return cb.getSortedTypes();
    }
    
    public boolean stillReferenced(final Type t)
    {
        return cb.stillReferenced(t);
    }
    
    public boolean stillReferenced(final Icon icon)
    {
        return cb.stillReferenced(icon);
    }
    
    public void deleteIcon(final Icon i)
    {
        cb.deleteIcon(i);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Part: UserBackend ">
    public User getUser(final String userName, final String password)
    {
        return ub.getUser(userName, password);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Part: CatalogBackend  ">
    public HashMap<String, String> getSimpleObjectInformation(final CatNode 
            node)
    {
        return catBackend.getSimpleObjectInformation(node);
    }

    public List<CatNode> getNodeParents(final CatNode node)
    {
        return catBackend.getNodeParents(node);
    }

    public List<CatNode> getNodeChildren(final CatNode node)
    {
        return catBackend.getNodeChildren(node);
    }

    public List<CatNode> getRootNodes(final CatNode.Type type)
    {
        return catBackend.getRootNodes(type);
    }
    
    /**
     * 
     * @returns true if the node has been deleted from the database, false if
     *          only the link has been deleted
     */
    public boolean deleteNode(final CatNode parent, final CatNode node)
    {
        return catBackend.deleteNode(parent, node);
    }
    
    public void deleteRootNode(final CatNode node)
    {
        catBackend.deleteRootNode(node);
    }

    public void moveNode(final CatNode oldParent, final CatNode newParent, final 
            CatNode node)
    {
        catBackend.moveNode(oldParent, newParent, node);
    }

    public void copyNode(final CatNode oldParent, final CatNode newParent, final 
            CatNode node)
    {
        catBackend.copyNode(oldParent, newParent, node);
    }

    public void linkNode(final CatNode oldParent, final CatNode newParent, final
            CatNode node)
    {
        catBackend.linkNode(oldParent, newParent, node);
    }

    public void moveChildren(final CatNode oldParent, final CatNode newParent)
    {
        catBackend.moveChildren(oldParent, newParent);
    }
    
    public CatNode addNode(final CatNode parent, final CatNode newNode, final 
            Domain domainTo)
    {
        return catBackend.addNode(parent, newNode, domainTo);
    }

    public boolean isLeaf(final CatNode node, final boolean useCache)
    {
        return catBackend.isLeaf(node, useCache);
    }

    public void reloadNonLeafNodeCache()
    {
        catBackend.reloadNonLeafNodeCache();
    }

    public List<CatNode> getRootNodes()
    {
        return catBackend.getRootNodes();
    }

    public Domain getLinkDomain(final CatNode from, final CatNode to)
    {
        return catBackend.getLinkDomain(from, to);
    }

    public void setLinkDomain(final CatNode from, final CatNode to, final Domain 
            domainTo)
    {
        catBackend.setLinkDomain(from, to, domainTo);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Part: MetaBackend ">
    
    public void refreshIndex(final CidsClass cidsClass) throws SQLException
    {
        metaBackend.refreshIndex(cidsClass);
    }

    public void adjustTypeClassId(final Type type) throws SQLException
    {
        metaBackend.adjustTypeClassId(type);
    }

    public void adjustAttrForeignKey(final Attribute attr) throws SQLException
    {
        metaBackend.adjustAttrForeignKey(attr);
    }

    public void addProgressListener(final ProgressListener pl)
    {
        metaBackend.addProgressListener(pl);
    }

    public void removeProgressListener(final ProgressListener pl)
    {
        metaBackend.removeProgressListener(pl);
    }

    public void cancel()
    {
        metaBackend.cancel();
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Part: testing purposes ">
    public static void main(String[] args)
    {
        Properties p=new Properties();
        p.put("log4j.appender.Remote", "org.apache.log4j.net.SocketAppender");
        p.put("log4j.appender.Remote.remoteHost","localhost");
        p.put("log4j.appender.Remote.port", "4445");
        p.put("log4j.appender.Remote.locationInfo", "true");
        p.put("log4j.rootLogger", "DEBUG,Remote");
        // hibernate core, maybe the following must then not be set anymore
        p.put("log4j.logger.org.hibernate", "WARN,Remote");
        // hql parser activity
//        p.put("log4j.logger.org.hibernate.hql.ast.AST", "WARN,Remote");
//        // sql
//        p.put("log4j.logger.org.hibernate.SQL", "WARN,Remote");
//        // jdbc bind parameters
//        p.put("log4j.logger.org.hibernate.type", "WARN,Remote");
//        // schema export/update
//        p.put("log4j.logger.org.hibernate.tool.hbm2ddl", "WARN,Remote");
//        // hql parse trees
//        p.put("log4j.logger.org.hibernate.hql", "WARN,Remote");
//        // cache activity
//        p.put("log4j.logger.org.hibernate.cache", "WARN,Remote");
//        // transaction activity
//        p.put("log4j.logger.org.hibernate.transaction", "WARN,Remote");
//        //jdbc resource acquisition
//        p.put("log4j.logger.org.hibernate.jdbc", "WARN,Remote");
        // c3p0 connection pool logging
        p.put("log4j.logger.com.mchange.v2.c3p0", "WARN,Remote");
        org.apache.log4j.PropertyConfigurator.configure(p);
        Backend backend = null;
        try
        {
            p = new Properties();
            p.load(new File("/Users/mscholl/cvswork6/testauslieferung/cidsDistribution/abf_dev_20090320/runtime.properties").toURL().openStream());
            backend = new Backend(p);
            System.out.println("========================================================");
            long begin = System.currentTimeMillis();
            URL url1 = new URL();
            URL url2 = new URL();
            URL url3 = new URL();
            URLBase urlbase = new URLBase();
            urlbase.setServer("s10221");
            url1.setUrlbase(urlbase);
            url1.setObjectName("a1");
            url2.setUrlbase(urlbase);
            url2.setObjectName("b2");
            url3.setUrlbase(urlbase);
            url3.setObjectName("c3");
            //url.setObjectName("ufermauer_51");
            //final List<URL> urls = backend.getURLsLikeURL(url);
            //url1 = backend.store(url1);
            //url2 = backend.store(url2);
            //url3 = backend.store(url3);
            List<URL> urls = Arrays.asList(url1, url2, url3);
            urls = backend.storeURLs(urls);
            backend.deleteURLs(urls);
//            backend.deleteURL(urls.get(0));
//            backend.deleteURL(urls.get(1));
//            backend.deleteURL(urls.get(2));
//            url1 = backend.storeURL(url1);
//            url2 = backend.storeURL(url2);
//            url3 = backend.storeURL(url3);
//            backend.delete(url1);
//            backend.delete(url2);
//            backend.delete(url3);
            //final User u = backend.getUser("GiessE102", "GiessE102");
//            System.out.println("loaded in: " + (System.currentTimeMillis() - begin));
//            System.out.println("--------");
//            for(final URL u : urls)
//                System.out.println(u);
        }catch(Throwable t)
        {
            t.printStackTrace();
        }
    }// </editor-fold>
}