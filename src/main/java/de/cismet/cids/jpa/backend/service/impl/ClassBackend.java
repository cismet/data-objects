/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service.impl;

import de.cismet.cids.jpa.backend.core.PersistenceProvider;
import de.cismet.cids.jpa.backend.service.ClassService;
import de.cismet.cids.jpa.entity.cidsclass.Icon;
import de.cismet.cids.jpa.entity.cidsclass.JavaClass;
import de.cismet.cids.jpa.entity.cidsclass.Type;
import de.cismet.cids.jpa.entity.common.URL;
import de.cismet.cids.jpa.entity.common.URLBase;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.apache.log4j.Logger;

/**
 * DOCUMENT ME!
 *
 * @author   $Author: mscholl $
 * @version  $Revision: 1.6 $ tag $Name: $ date $Date: 2009/10/29 14:24:52 $
 */
public class ClassBackend implements ClassService {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(ClassBackend.class);

    //~ Instance fields --------------------------------------------------------

    private final transient PersistenceProvider provider;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ClassBackend object.
     *
     * @param  provider  DOCUMENT ME!
     */
    public ClassBackend(final PersistenceProvider provider) {
        this.provider = provider;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   jc  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean contains(final JavaClass jc) {
        try {
            return getJavaClass(jc.getQualifier()) != null;
        } catch (final NoResultException ex) {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   qualifier  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public JavaClass getJavaClass(final String qualifier) {
        final EntityManager entityManager = provider.getEntityManager();
        final Query q = entityManager.createQuery("FROM JavaClass jc WHERE jc.qualifier = :qualifier"); // NOI18N
        q.setParameter("qualifier", qualifier);                                                         // NOI18N
        return (JavaClass)q.getSingleResult();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  jc  DOCUMENT ME!
     */
    @Override
    public void deleteJavaClass(final JavaClass jc) {
        final EntityManager em = provider.getEntityManager();

        Query q = em.createQuery("UPDATE CidsClass cc SET cc.toString = null WHERE cc.toString = :jc"); // NOI18N
        q.setParameter("jc", jc);                                                                       // NOI18N
        int updated = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updated + " rows from cs_class: toString = null");                    // NOI18N
        }

        q = em.createQuery("UPDATE CidsClass cc SET cc.editor = null WHERE cc.editor = :jc"); // NOI18N
        q.setParameter("jc", jc);                                                             // NOI18N
        updated = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updated + " rows from cs_class: editor = null");            // NOI18N
        }

        q = em.createQuery("UPDATE CidsClass cc SET cc.renderer = null WHERE cc.renderer = :jc"); // NOI18N
        q.setParameter("jc", jc);
        updated = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updated + " rows from cs_class: renderer = null");              // NOI18N
        }

        q = em.createQuery("UPDATE Attribute a SET a.toString = null WHERE a.toString = :jc"); // NOI18N
        q.setParameter("jc", jc);                                                              // NOI18N
        updated = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updated + " rows from cs_attr: toString = null");            // NOI18N
        }

        q = em.createQuery("UPDATE Attribute a SET a.editor = null WHERE a.editor = :jc"); // NOI18N
        q.setParameter("jc", jc);                                                          // NOI18N
        updated = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updated + " rows from cs_attr: editor = null");          // NOI18N
        }

        q = em.createQuery("UPDATE Attribute a SET a.complexEditor = null WHERE a.complexEditor = :jc"); // NOI18N
        q.setParameter("jc", jc);                                                                        // NOI18N
        updated = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updated + " rows from cs_attr: complexEditor = null");                 // NOI18N
        }

        q = em.createQuery("UPDATE Type t SET t.editor = null WHERE t.editor = :jc");     // NOI18N
        q.setParameter("jc", jc);                                                         // NOI18N
        updated = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updated + " rows from cs_type: editor = null");         // NOI18N
        }
        q = em.createQuery("UPDATE Type t SET t.renderer = null WHERE t.renderer = :jc"); // NOI18N
        q.setParameter("jc", jc);                                                         // NOI18N
        updated = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updated + " rows from cs_type: renderer = null");
        }

        provider.delete(jc);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public List<URL> getURLsLikeURL(final URL url) {
        final EntityManager em = provider.getEntityManager();
        final URLBase urlBase = url.getUrlbase();
        final String protocol = (urlBase.getProtocolPrefix() == null) ? "" : urlBase.getProtocolPrefix(); // NOI18N
        final String server = (urlBase.getServer() == null) ? "" : urlBase.getServer();                   // NOI18N
        final String path = (urlBase.getPath() == null) ? "" : urlBase.getPath();                         // NOI18N
        final String object = (url.getObjectName() == null) ? "" : url.getObjectName();                   // NOI18N
        final Query q = em.createQuery(
                "FROM URL url "                                                                           // NOI18N
                + "WHERE url.urlbase.protocolPrefix LIKE '%" + protocol + "%' "                           // NOI18N
                + "AND url.urlbase.server LIKE '%" + server + "%' "                                       // NOI18N
                + "AND url.urlbase.path LIKE '%" + path + "%' "                                           // NOI18N
                + "AND url.objectName LIKE '%" + object + "%'");                                          // NOI18N
        return q.getResultList();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public URL storeURL(final URL url) {
        final EntityManager em = provider.getEntityManager();
        final URLBase urlBase = url.getUrlbase();
        String protocol = urlBase.getProtocolPrefix();
        if (protocol == null) {
            protocol = "";                                           // NOI18N
            urlBase.setProtocolPrefix(protocol);
        }
        String server = urlBase.getServer();
        if (server == null) {
            server = "";                                             // NOI18N
            urlBase.setServer(server);
        }
        String path = urlBase.getPath();
        if (path == null) {
            path = "";                                               // NOI18N
            urlBase.setPath(path);
        }
        final Query q = em.createQuery(
                "FROM URLBase ub "                                   // NOI18N
                + "WHERE ub.protocolPrefix LIKE '" + protocol + "' " // NOI18N
                + "AND ub.server LIKE '" + server + "' "             // NOI18N
                + "AND ub.path LIKE '" + path + "'");                // NOI18N
        try {
            url.setUrlbase((URLBase)q.getSingleResult());
            if (LOG.isDebugEnabled()) {
                LOG.debug("received urlbase, using it");             // NOI18N
            }
        } catch (final NoResultException e) {
            // urlbase not existing, store it and set it
            if (LOG.isDebugEnabled()) {
                LOG.debug("urlbase not present, store it first", e); // NOI18N
            }
            url.setUrlbase(provider.store(urlBase));
        }
        return provider.store(url);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   urls  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public List<URL> storeURLs(final List<URL> urls) {
        for (int i = 0; i < urls.size(); ++i) {
            final URL url = storeURL(urls.get(i));
            urls.set(i, url);
        }
        return urls;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    @Override
    public void deleteURL(final URL url) {
        final URLBase urlbase = url.getUrlbase();
        provider.delete(url);
        deleteURLBaseIfUnused(urlbase);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  urls  DOCUMENT ME!
     */
    @Override
    public void deleteURLs(final List<URL> urls) {
        for (final URL url : urls) {
            deleteURL(url);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  urlbase  DOCUMENT ME!
     */
    @Override
    public void deleteURLBaseIfUnused(final URLBase urlbase) {
        final EntityManager em = provider.getEntityManager();
        final Query q = em.createQuery(                                             // NOI18N
                "FROM URLBase ub "                                                  // NOI18N
                + "WHERE ub.id = " + urlbase.getId() + " "                          // NOI18N
                + "AND ub NOT IN "                                                  // NOI18N
                + "(SELECT urlbase FROM URL)");                                     // NOI18N
        try {
            if (q.getSingleResult() != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("the urlbase is not referenced anymore: " + urlbase); // NOI18N
                }
                provider.delete(urlbase);
            }
        } catch (final NoResultException e) {
            // the urlbase is still referenced
            if (LOG.isDebugEnabled()) {
                LOG.debug("the urlbase is still referenced: " + urlbase, e); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  urlbases  DOCUMENT ME!
     */
    @Override
    public void deleteURLBasesIfUnused(final List<URLBase> urlbases) {
        for (final URLBase base : urlbases) {
            deleteURLBaseIfUnused(base);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public List getSortedTypes() {
        final EntityManager entityManager = provider.getEntityManager();
        final Query q = entityManager.createQuery(
                "SELECT count(a.type.id), a.type.id FROM Attribute a GROUP BY a.type"); // NOI18N
        return q.getResultList();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  i  DOCUMENT ME!
     */
    @Override
    public void deleteIcon(final Icon i) {
        final EntityManager em = provider.getEntityManager();
        if (LOG.isInfoEnabled()) {
            LOG.info("updating CidsClasses referecing icon '" + i + "' setting reference to null"); // NOI18N
        }

        Query q = em.createQuery("UPDATE CidsClass cc SET cc.classIcon = null WHERE cc.classIcon = :icon"); // NOI18N
        q.setParameter("icon", i);                                                                          // NOI18N
        int updatedRows = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updatedRows + " CidsClasses with '" + i + "' as class_icon");             // NOI18N
        }

        q = em.createQuery("UPDATE CidsClass cc SET cc.classIcon = null WHERE cc.objectIcon = :icon"); // NOI18N
        q.setParameter("icon", i);                                                                     // NOI18N
        updatedRows = q.executeUpdate();
        if (LOG.isInfoEnabled()) {
            LOG.info("updated " + updatedRows + " CidsClasses with '" + i + "' as object_icon");       // NOI18N
        }

        provider.delete(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   t  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean stillReferenced(final Type t) {
        final EntityManager em = provider.getEntityManager();
        
        Query q = em.createQuery("FROM Attribute a WHERE a.type = :type"); // NOI18N
        q.setParameter("type", t);                                         // NOI18N
        List l = q.getResultList();
        if (!l.isEmpty()) {
            return true;
        }

        q = em.createQuery("FROM ClassAttribute ca WHERE ca.type = :type"); // NOI18N
        q.setParameter("type", t);                                          // NOI18N
        l = q.getResultList();
        return !l.isEmpty();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   icon  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean stillReferenced(final Icon icon) {
        final EntityManager em = provider.getEntityManager();
        final Query q = em.createQuery("FROM CidsClass c WHERE c.classIcon = :icon OR c.objectIcon = :icon"); // NOI18N
        q.setParameter("icon", icon);                                                                         // NOI18N
        final List l = q.getResultList();
        return !l.isEmpty();
    }
}
