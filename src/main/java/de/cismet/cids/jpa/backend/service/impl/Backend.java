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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
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
    private final transient ClassService cb;
    private final transient UserService ub;
    private final transient CatalogService catBackend;
    private final transient MetaService metaBackend;
    private final transient PersistenceProvider provider;
    private final transient CommonService commonBackend;
    
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
    @Override
    public void close() throws Exception
    {
        // it should be enough to close the persistence provider
        provider.close();
        metaBackend.close();
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Part: CommonService">
    @Override
    public <T extends CommonEntity> T store(final T entity)
    {
        return commonBackend.store(entity);
    }

    @Override
    public void delete(final CommonEntity ce)
    {
        commonBackend.delete(ce);
    }

    @Override
    public <T extends CommonEntity> T getEntity(final Class<T> entity, 
                                               final int id)
    {
        return commonBackend.getEntity(entity, id);
    }

    @Override
    public <T extends CommonEntity> List<T> getAllEntities(
                                                          final Class<T> entity)
    {
        return commonBackend.getAllEntities(entity);
    }

    /**
     * assumes entity has a mapped member named 'name'
     */
    @Override
    public <T extends CommonEntity> T getEntity(final Class<T> entity, 
                                                final String name)
    {
        return commonBackend.getEntity(entity, name);
    }

    /**
     * assumes entity has a mapped member named 'name'
     */
    @Override
    public <T extends CommonEntity> boolean contains(final Class<T> entity, 
                                                     final String name)
    {
        return commonBackend.contains(entity, name);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Part: ClassBackend ">
    @Override
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
    @Override
    public User getUser(final String userName, final String password)
    {
        return ub.getUser(userName, password);
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Part: CatalogBackend  ">
    @Override
    public Map<String, String> getSimpleObjectInformation(final CatNode node)
    {
        return catBackend.getSimpleObjectInformation(node);
    }

    @Override
    public List<CatNode> getNodeParents(final CatNode node)
    {
        return catBackend.getNodeParents(node);
    }

    @Override
    public List<CatNode> getNodeChildren(final CatNode node)
    {
        return catBackend.getNodeChildren(node);
    }

    @Override
    public List<CatNode> getRootNodes(final CatNode.Type type)
    {
        return catBackend.getRootNodes(type);
    }
    
    /**
     * 
     * @returns true if the node has been deleted from the database, false if
     *          only the link has been deleted
     */
    @Override
    public boolean deleteNode(final CatNode parent, final CatNode node)
    {
        return catBackend.deleteNode(parent, node);
    }

    @Override
    public void deleteRootNode(final CatNode node)
    {
        catBackend.deleteRootNode(node);
    }

    @Override
    public void moveNode(final CatNode oldParent, final CatNode newParent, 
                        final  CatNode node)
    {
        catBackend.moveNode(oldParent, newParent, node);
    }

    @Override
    public void copyNode(final CatNode oldParent, final CatNode newParent, 
                         final CatNode node)
    {
        catBackend.copyNode(oldParent, newParent, node);
    }

    @Override
    public void linkNode(final CatNode oldParent, final CatNode newParent, 
                         final CatNode node)
    {
        catBackend.linkNode(oldParent, newParent, node);
    }

    @Override
    public void moveChildren(final CatNode oldParent, final CatNode newParent)
    {
        catBackend.moveChildren(oldParent, newParent);
    }

    @Override
    public CatNode addNode(final CatNode parent, final CatNode newNode, 
                           final Domain domainTo)
    {
        return catBackend.addNode(parent, newNode, domainTo);
    }

    @Override
    public boolean isLeaf(final CatNode node, final boolean useCache)
    {
        return catBackend.isLeaf(node, useCache);
    }

    @Override
    public void reloadNonLeafNodeCache()
    {
        catBackend.reloadNonLeafNodeCache();
    }

    @Override
    public List<CatNode> getRootNodes()
    {
        return catBackend.getRootNodes();
    }

    @Override
    public Domain getLinkDomain(final CatNode from, final CatNode to)
    {
        return catBackend.getLinkDomain(from, to);
    }

    @Override
    public void setLinkDomain(final CatNode from, final CatNode to, 
                              final Domain domainTo)
    {
        catBackend.setLinkDomain(from, to, domainTo);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Part: MetaBackend ">
    @Override
    public void refreshIndex(final CidsClass cidsClass) throws SQLException
    {
        metaBackend.refreshIndex(cidsClass);
    }

    @Override
    public void adjustTypeClassId(final Type type) throws SQLException
    {
        metaBackend.adjustTypeClassId(type);
    }

    @Override
    public void adjustAttrForeignKey(final Attribute attr) throws SQLException
    {
        metaBackend.adjustAttrForeignKey(attr);
    }

    @Override
    public void addProgressListener(final ProgressListener pl)
    {
        metaBackend.addProgressListener(pl);
    }

    @Override
    public void removeProgressListener(final ProgressListener pl)
    {
        metaBackend.removeProgressListener(pl);
    }

    @Override
    public void cancel()
    {
        metaBackend.cancel();
    }

    // </editor-fold>
    
}