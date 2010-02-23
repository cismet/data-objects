/*
 * UserBackend.java
 *
 * Created on 4. Juli 2006, 09:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

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
 *
 * @author $Author: mscholl $
 * @version $Revision: 1.6 $
 * tag $Name:  $
 * date $Date: 2009/10/29 14:24:52 $
 */
public class ClassBackend implements ClassService
{
    private static final transient Logger LOG = Logger.getLogger(
            ClassBackend.class);
    
    private final transient PersistenceProvider provider;
    
    public ClassBackend(final PersistenceProvider provider)
    {
        this.provider = provider;
    }
     
    public boolean contains(final JavaClass jc)
    {
        try
        {
            return getJavaClass(jc.getQualifier()) != null;
        }catch(final NoResultException ex)
        {
            return false;
        }
    }

    public JavaClass getJavaClass(final String qualifier)
    {
        final EntityManager entityManager = provider.getEntityManager();
        final Query q = entityManager.createQuery("FROM JavaClass jc " +
                "WHERE jc.qualifier = :qualifier").
                setParameter("qualifier", qualifier);
        return (JavaClass)q.getSingleResult();
    }

    public void deleteJavaClass(final JavaClass jc)
    {
        final EntityManager em = provider.getEntityManager();
        int updated = em.createQuery("UPDATE CidsClass cc SET cc.toString = " +
                "null WHERE cc.toString = :jc").
                setParameter("jc", jc).
                executeUpdate();
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updated + " rows from cs_class: toString = " +
                    "null");
        updated = em.createQuery("UPDATE CidsClass cc SET cc.editor = null " +
                "WHERE cc.editor = :jc").
                setParameter("jc", jc).
                executeUpdate();
        
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updated + " rows from cs_class: editor = nu" +
                    "ll");
        updated = em.createQuery("UPDATE CidsClass cc SET cc.renderer = null " +
                "WHERE cc.renderer = :jc").
                setParameter("jc", jc).
                executeUpdate();
        
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updated + " rows from cs_class: renderer = " +
                    "null");
        updated = em.createQuery("UPDATE Attribute a SET a.toString = null " +
                "WHERE a.toString = :jc").
                setParameter("jc", jc).
                executeUpdate();
        
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updated + " rows from cs_attr: toString = n" +
                    "ull");
        updated = em.createQuery("UPDATE Attribute a SET a.editor = null " +
                "WHERE a.editor = :jc").
                setParameter("jc", jc).
                executeUpdate();
        
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updated + " rows from cs_attr: editor = nul" +
                    "l");
        updated = em.createQuery("UPDATE Attribute a SET a.complexEditor = " +
                "null WHERE a.complexEditor = :jc").
                setParameter("jc", jc).
                executeUpdate();
        
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updated + " rows from cs_attr: complexEdito" +
                    "r = " +
                "null");
        updated = em.createQuery("UPDATE Type t SET t.editor = null WHERE " +
                "t.editor = :jc").
                setParameter("jc", jc).
                executeUpdate();
        
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updated + " rows from cs_type: editor = nul" +
                    "l");
        updated = em.createQuery("UPDATE Type t SET t.renderer = null WHERE " +
                "t.renderer = :jc").
                setParameter("jc", jc).
                executeUpdate();
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updated + " rows from cs_type: renderer = n" +
                    "ull");
        provider.delete(jc);
    }
    
    public URL getURL(final String url)
    {
        throw new UnsupportedOperationException();
//        final String[] prefixAndRest = url.split("//");
//        if(prefixAndRest.length != 2)
//            throw new IllegalArgumentException("given string format not " +
//                    "supported: " + url);
//        final String prefix = prefixAndRest[0] + "//";
//        int indexOfSlash = prefixAndRest[1].indexOf('/');
//        if(indexOfSlash < 1)
//            throw new IllegalArgumentException("given string format not " +
//                    "supported: " + url);
//        final String server = prefixAndRest[1].substring(0, indexOfSlash);
    }

    public List<URL> getURLsLikeURL(final URL url)
    {
        final EntityManager em = provider.getEntityManager();
        final URLBase urlBase = url.getUrlbase();
        final String protocol = urlBase.getProtocolPrefix() == null
                ? "" : urlBase.getProtocolPrefix();
        final String server = urlBase.getServer() == null
                ? "" : urlBase.getServer();
        final String path = urlBase.getPath() == null
                ? "" : urlBase.getPath();
        final String object = url.getObjectName() == null
                ? "" : url.getObjectName();
        final Query q = em.createQuery(
                "FROM URL url "
                + "WHERE url.urlbase.protocolPrefix LIKE '%" + protocol + "%' "
                + "AND url.urlbase.server LIKE '%" + server + "%' "
                + "AND url.urlbase.path LIKE '%" + path + "%' "
                + "AND url.objectName LIKE '%" + object + "%'");
        return q.getResultList();
    }

    public URL storeURL(final URL url)
    {
        final EntityManager em = provider.getEntityManager();
        final URLBase urlBase = url.getUrlbase();
        String protocol = urlBase.getProtocolPrefix();
        if(protocol == null)
        {
            protocol = "";
            urlBase.setProtocolPrefix(protocol);
        }
        String server = urlBase.getServer();
        if(server == null)
        {
            server = "";
            urlBase.setServer(server);
        }
        String path = urlBase.getPath();
        if(path == null)
        {
            path = "";
            urlBase.setPath(path);
        }
        final Query q = em.createQuery("FROM URLBase ub "
                + "WHERE ub.protocolPrefix LIKE '" + protocol + "' "
                + "AND ub.server LIKE '" + server + "' "
                + "AND ub.path LIKE '" + path + "'");
        try
        {
            url.setUrlbase((URLBase)q.getSingleResult());
            if(LOG.isDebugEnabled())
                LOG.debug("received urlbase, using it");
        }catch(final NoResultException e)
        {
            // urlbase not existing, store it and set it
            if(LOG.isDebugEnabled())
                LOG.debug("urlbase not present, store it first", e);
            url.setUrlbase(provider.store(urlBase));
        }
        return provider.store(url);
    }

    public List<URL> storeURLs(final List<URL> urls)
    {
        for(int i = 0; i < urls.size(); ++i)
        {
            final URL url = storeURL(urls.get(i));
            urls.set(i, url);
        }
        return urls;
    }

    public void deleteURL(final URL url)
    {
        final URLBase urlbase = url.getUrlbase();
        provider.delete(url);
        deleteURLBaseIfUnused(urlbase);
    }

    public void deleteURLs(final List<URL> urls)
    {
        for(final URL url : urls)
            deleteURL(url);
    }

    public void deleteURLBaseIfUnused(final URLBase urlbase)
    {
        final EntityManager em = provider.getEntityManager();
        final Query q = em.createQuery("FROM URLBase ub "
                + "WHERE ub.id = " + urlbase.getId() + " "
                + "AND ub NOT IN "
                + "(SELECT urlbase FROM URL)");
        try
        {
            if(q.getSingleResult() != null)
            {
                if(LOG.isDebugEnabled())
                    LOG.debug("the urlbase is not referenced anymore: "
                            + urlbase);
                provider.delete(urlbase);
            }
        }catch(final NoResultException e)
        {
            // the urlbase is still referenced
            if(LOG.isDebugEnabled())
                LOG.debug("the urlbase is still referenced: " + urlbase, e);
        }
    }

    public void deleteURLBasesIfUnused(final List<URLBase> urlbases)
    {
        for(final URLBase base : urlbases)
            deleteURLBaseIfUnused(base);
    }
    
    public List getSortedTypes()
    {
        final EntityManager entityManager = provider.getEntityManager();
        final Query q = entityManager.createQuery("SELECT count(" +
                "a.type.id), a.type.id FROM Attribute a GROUP BY a.type");
        return q.getResultList();
    }

    public void deleteIcon(final Icon i)
    {
        final EntityManager em = provider.getEntityManager();
        if(LOG.isInfoEnabled())
            LOG.info("updating CidsClasses referecing icon '" + i + "' settin" +
                    "g reference to null");
        int updatedRows = em.createQuery("UPDATE CidsClass cc SET cc." +
                "classIcon = null WHERE cc.classIcon = :icon").
                setParameter("icon", i).
                executeUpdate();
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updatedRows + " CidsClasses with '" + i +
                    "' as class_icon");
        updatedRows = em.createQuery("UPDATE CidsClass cc SET cc.classIcon = " +
                "null WHERE cc.objectIcon = :icon").
                setParameter("icon", i).
                executeUpdate();
        if(LOG.isInfoEnabled())
            LOG.info("updated " + updatedRows + " CidsClasses with '" + i +
                    "' as object_icon");
        provider.delete(i);
    }
    
    public boolean stillReferenced(final Type t)
    {
        final EntityManager em = provider.getEntityManager();
        List l = em.createQuery("FROM Attribute a WHERE a.type = :type").
                setParameter("type", t).
                getResultList();
        if(l.size() > 0)
            return true;
        l = em.createQuery("FROM ClassAttribute ca WHERE ca.type = :type").
                setParameter("type", t).
                getResultList();
        return l.size() > 0;
    }

    public boolean stillReferenced(final Icon icon)
    {
        final EntityManager em = provider.getEntityManager();
        final List l = em.createQuery("FROM CidsClass c WHERE c.classIcon = " +
                ":icon OR c.objectIcon = :icon").
                setParameter("icon", icon).
                getResultList();
        return l.size() > 0;
    }
}