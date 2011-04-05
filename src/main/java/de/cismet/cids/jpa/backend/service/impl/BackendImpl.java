/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service.impl;

import net.sf.tie.ProxyInjector;
import net.sf.tie.ext.InterceptionBuilder;

import java.sql.SQLException;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.cismet.cids.jpa.backend.core.PersistenceInterceptor;
import de.cismet.cids.jpa.backend.core.PersistenceProvider;
import de.cismet.cids.jpa.backend.service.Backend;
import de.cismet.cids.jpa.backend.service.CatalogService;
import de.cismet.cids.jpa.backend.service.ClassService;
import de.cismet.cids.jpa.backend.service.CommonService;
import de.cismet.cids.jpa.backend.service.ConfigAttrService;
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
import de.cismet.cids.jpa.entity.configattr.ConfigAttrEntry;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrKey;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrType;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrType.Types;
import de.cismet.cids.jpa.entity.user.User;

import de.cismet.cids.util.ProgressListener;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  1.13
 */
// The backend shall not be formatted automatically since jalopy messes everything up due to the netbeans editor folds
//J-
// This is an Accessor/Facade class so there simply are many imports
@SuppressWarnings("PMD.ExcessiveImports")
public final class BackendImpl implements Backend {

    //~ Instance fields --------------------------------------------------------

    private final transient ClassService cb;
    private final transient UserService ub;
    private final transient CatalogService catBackend;
    private final transient MetaService metaBackend;
    private final transient PersistenceProvider provider;
    private final transient CommonService commonBackend;
    private final transient ConfigAttrService configAttrService;

    //~ Constructors -----------------------------------------------------------

    // <editor-fold defaultstate="collapsed" desc=" Part: Constructors ">
    /**
     * Creates a new instance of BackendImpl.
     *
     * @param  properties  DOCUMENT ME!
     */
    public BackendImpl(final Properties properties) {
        provider = new PersistenceProvider(properties);
        final ClassBackend classB = new ClassBackend(provider);
        final UserBackend userB = new UserBackend(provider);
        final CatalogBackend catB = new CatalogBackend(provider);
        final MetaBackend metaB = new MetaBackend(properties);
        final ConfigAttrBackend configAttrB = new ConfigAttrBackend(provider);
        final InterceptionBuilder builder = new InterceptionBuilder();
        builder.always(new PersistenceInterceptor(provider));
        final ProxyInjector injector = new ProxyInjector(builder.done());
        cb = injector.wrapObject(ClassService.class, classB);
        ub = injector.wrapObject(UserService.class, userB);
        catBackend = injector.wrapObject(CatalogService.class, catB);
        metaBackend = injector.wrapObject(MetaService.class, metaB);
        commonBackend = injector.wrapObject(CommonService.class, provider);
        configAttrService = injector.wrapObject(ConfigAttrService.class, configAttrB);
    }
    // </editor-fold>

    //~ Methods ----------------------------------------------------------------

    // <editor-fold defaultstate="collapsed" desc=" Part: CommonService">
    @Override
    public <T extends CommonEntity> T store(final T entity) {
        return commonBackend.store(entity);
    }

    @Override
    public void delete(final CommonEntity ce) {
        commonBackend.delete(ce);
    }

    @Override
    public void delete(final List<CommonEntity> entities) {
        commonBackend.delete(entities);
    }

    @Override
    public <T extends CommonEntity> T getEntity(final Class<T> entity, final int id) {
        return commonBackend.getEntity(entity, id);
    }

    @Override
    public <T extends CommonEntity> List<T> getAllEntities(final Class<T> entity) {
        return commonBackend.getAllEntities(entity);
    }

    /**
     * assumes entity has a mapped member named 'name'
     *
     * @param   <T>     DOCUMENT ME!
     * @param   entity  DOCUMENT ME!
     * @param   name    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public <T extends CommonEntity> T getEntity(final Class<T> entity, final String name) {
        return commonBackend.getEntity(entity, name);
    }

    /**
     * assumes entity has a mapped member named 'name'
     *
     * @param   <T>     DOCUMENT ME!
     * @param   entity  DOCUMENT ME!
     * @param   name    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public <T extends CommonEntity> boolean contains(final Class<T> entity, final String name) {
        return commonBackend.contains(entity, name);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Part: CatalogBackend  ">
    @Override
    public Map<String, String> getSimpleObjectInformation(final CatNode node) {
        return catBackend.getSimpleObjectInformation(node);
    }

    @Override
    public List<CatNode> getNodeParents(final CatNode node) {
        return catBackend.getNodeParents(node);
    }

    @Override
    public List<CatNode> getNodeChildren(final CatNode node) {
        return catBackend.getNodeChildren(node);
    }

    @Override
    public List<CatNode> getRootNodes(final CatNode.Type type) {
        return catBackend.getRootNodes(type);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   parent  DOCUMENT ME!
     * @param   node    DOCUMENT ME!
     *
     * @return  true if the node has been deleted from the database, false if only the link has been deleted
     */
    @Override
    public boolean deleteNode(final CatNode parent, final CatNode node) {
        return catBackend.deleteNode(parent, node);
    }

    @Override
    public void deleteRootNode(final CatNode node) {
        catBackend.deleteRootNode(node);
    }

    @Override
    public void moveNode(final CatNode oldParent, final CatNode newParent,
            final CatNode node) {
        catBackend.moveNode(oldParent, newParent, node);
    }

    @Override
    public void copyNode(final CatNode oldParent, final CatNode newParent,
            final CatNode node) {
        catBackend.copyNode(oldParent, newParent, node);
    }

    @Override
    public void linkNode(final CatNode oldParent, final CatNode newParent,
            final CatNode node) {
        catBackend.linkNode(oldParent, newParent, node);
    }

    @Override
    public CatNode addNode(final CatNode parent, final CatNode newNode, final Domain domainTo) {
        return catBackend.addNode(parent, newNode, domainTo);
    }

    @Override
    public boolean isLeaf(final CatNode node, final boolean useCache) {
        return catBackend.isLeaf(node, useCache);
    }

    @Override
    public void reloadNonLeafNodeCache() {
        catBackend.reloadNonLeafNodeCache();
    }

    @Override
    public List<CatNode> getRootNodes() {
        return catBackend.getRootNodes();
    }

    @Override
    public Domain getLinkDomain(final CatNode from, final CatNode to) {
        return catBackend.getLinkDomain(from, to);
    }

    @Override
    public void setLinkDomain(final CatNode from, final CatNode to,
            final Domain domainTo) {
        catBackend.setLinkDomain(from, to, domainTo);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Part: MetaBackend ">
    @Override
    public void refreshIndex(final CidsClass cidsClass) throws SQLException {
        metaBackend.refreshIndex(cidsClass);
    }

    @Override
    public void adjustTypeClassId(final Type type) throws SQLException {
        metaBackend.adjustTypeClassId(type);
    }

    @Override
    public void adjustAttrForeignKey(final Attribute attr) throws SQLException {
        metaBackend.adjustAttrForeignKey(attr);
    }

    @Override
    public void addProgressListener(final ProgressListener pl) {
        metaBackend.addProgressListener(pl);
    }

    @Override
    public void removeProgressListener(final ProgressListener pl) {
        metaBackend.removeProgressListener(pl);
    }

    @Override
    public void cancel() {
        metaBackend.cancel();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Part: BackendImpl operations ">
    @Override
    // there is no choice but to obay the interface specification
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    public void close() throws Exception {
        provider.monitorClose(configAttrService);
        provider.monitorClose(metaBackend);
        // it should be enough to close the persistence provider
        provider.close();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Part: ClassrBackend ">
    @Override
    public List<URL> getURLsLikeURL(final URL url) {
        return cb.getURLsLikeURL(url);
    }

    @Override
    public URL storeURL(final URL url) {
        return cb.storeURL(url);
    }

    @Override
    public List<URL> storeURLs(final List<URL> urls) {
        return cb.storeURLs(urls);
    }

    @Override
    public void deleteURL(final URL url) {
        cb.deleteURL(url);
    }

    @Override
    public void deleteURLs(final List<URL> urls) {
        cb.deleteURLs(urls);
    }

    @Override
    public void deleteURLBaseIfUnused(final URLBase urlbase) {
        cb.deleteURLBaseIfUnused(urlbase);
    }

    @Override
    public void deleteURLBasesIfUnused(final List<URLBase> urlbases) {
        cb.deleteURLBasesIfUnused(urlbases);
    }

    @Override
    public boolean contains(final JavaClass jc) {
        return cb.contains(jc);
    }

    @Override
    public JavaClass getJavaClass(final String qualifier) {
        return cb.getJavaClass(qualifier);
    }

    @Override
    public void deleteJavaClass(final JavaClass jc) {
        cb.deleteJavaClass(jc);
    }

    @Override
    public List getSortedTypes() {
        return cb.getSortedTypes();
    }

    @Override
    public boolean stillReferenced(final Type t) {
        return cb.stillReferenced(t);
    }

    @Override
    public boolean stillReferenced(final Icon icon) {
        return cb.stillReferenced(icon);
    }

    @Override
    public void deleteIcon(final Icon i) {
        cb.deleteIcon(i);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Part: UserBackend ">
    @Override
    public User getUser(final String userName, final String password) {
        return ub.getUser(userName, password);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc=" Part: ConfigAttrBackend ">
    @Override
    public List<ConfigAttrEntry> getEntries(final ConfigAttrKey key) {
        return configAttrService.getEntries(key);
    }

    @Override
    public List<ConfigAttrEntry> getEntries(final ConfigAttrKey key, final Types type){
        return configAttrService.getEntries(key, type);
    }

    @Override
    public List<ConfigAttrEntry> getEntries(final ConfigAttrType.Types type) {
        return configAttrService.getEntries(type);
    }

    @Override
    public ConfigAttrEntry storeEntry(final ConfigAttrEntry entry) {
        return configAttrService.storeEntry(entry);
    }

    @Override
    public void cleanAttributeTables() {
        configAttrService.cleanAttributeTables();
    }

    @Override
    public boolean contains(final ConfigAttrEntry entry) {
        return configAttrService.contains(entry);
    }

    @Override
    public boolean contains(final ConfigAttrKey key) {
        return configAttrService.contains(key);
    }

    // </editor-fold>
}
//J+
