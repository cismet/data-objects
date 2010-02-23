/*
 * CatalogBackend.java
 *
 * Created on 21. Juni 2007, 15:41
 */

package de.cismet.cids.jpa.backend.service.impl;

import de.cismet.cids.jpa.backend.core.PersistenceProvider;
import de.cismet.cids.jpa.backend.service.CatalogService;
import de.cismet.cids.jpa.entity.catalog.CatLink;
import de.cismet.cids.jpa.entity.catalog.CatNode;
import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.common.Domain;
import de.cismet.cids.jpa.entity.permission.NodePermission;
import de.cismet.diff.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 * @author Martin Scholl
 * @version 1.0
 */
public class CatalogBackend implements CatalogService
{
    private static final transient Logger LOG = Logger.getLogger(
            CatalogBackend.class);
    
    private transient HashSet<Integer> nonLeafCache;
    private final transient PersistenceProvider provider;
    
    /**
     * Creates a new instance of <code>CatalogBackend</code>
     */
    public CatalogBackend(final PersistenceProvider provider)
    {
        this.provider = provider;
        nonLeafCache = getNonLeafNodes();
    }

    public HashMap<String, String> getSimpleObjectInformation(final CatNode 
            node)
    {
        if(!node.getNodeType().equals(CatNode.Type.OBJECT.getType()))
            return null;
        final HashMap<String, String> objInfo = new HashMap<String, String>();
        final HashMap<String, String> tmp = new HashMap<String, String>();
        try
        {
            for(final Iterator<Attribute> it = node.getCidsClass().
                    getAttributes().iterator(); it.hasNext();)
            {
                final Attribute a = it.next();
                tmp.put(a.getName(), a.getFieldName());
            }
        }catch(final Exception e)
        {
            LOG.warn("could not retrieve simple object information", e);
            return null;
        }
        // dynamic bean creation 
        Connection c = null;
        try
        {
            c = DatabaseConnection.getConnection(provider.getRuntimeProperties(
                    ));
            c.setAutoCommit(false);
            final ResultSet set = c.createStatement().executeQuery("select * " +
                    "from " + node.getCidsClass().getTableName() + 
                    " where id = " + node.getObjectId());
            while(set.next())
            {
                for(final Iterator<Entry<String, String>> entries = tmp.
                        entrySet().iterator(); entries.hasNext();)
                {
                    final Entry<String, String> e = entries.next();
                    objInfo.put(e.getKey(), set.getString(e.getValue()));
                }
                break;
            }
            if(set.next())
                throw new SQLException("query shall return just one row");
            set.close();
            c.commit();
            c = null;
            return objInfo;
        } catch (final SQLException ex)
        {
            LOG.error("error during object information fetching", ex);
            if(c != null)
            {
                try
                {
                    c.rollback();
                } catch (final SQLException sqle)
                {
                    LOG.error("could not roll back", sqle);
                }
            }
        }
        return null;
    }

    public List<CatNode> getNodeParents(final CatNode node)
    {
        final EntityManager em = provider.getEntityManager();
        final Query q = em.createQuery("FROM CatNode node WHERE node.id in (" +
                "SELECT idFrom FROM CatLink WHERE idTo = :id)").
                setParameter("id", node.getId());
        final List<CatNode> nodeList = q.getResultList();
        for(final ListIterator<CatNode> it = nodeList.listIterator(); it.
                hasNext();)
        {
            final CatNode n = it.next();
            n.setIsLeaf(false);
        }
        return nodeList;
    }

    public List<CatNode> getNodeChildren(final CatNode node)
    {
        final EntityManager em = provider.getEntityManager();
        final Query q = em.createQuery(
                "FROM CatNode node WHERE node.id in (SELECT idTo FROM " +
                "CatLink WHERE idFrom = :id)").
                setParameter("id", node.getId());
        final List<CatNode> nodeList = q.getResultList();
        for(final ListIterator<CatNode> it = nodeList.listIterator(); 
                it.hasNext();)
        {
            final CatNode n = it.next();
            n.setIsLeaf(isLeaf(n, true));
        }
        return nodeList;
    }
    
    public List<CatNode> getRootNodes(final CatNode.Type type)
    {
        final EntityManager em = provider.getEntityManager();
        final Query q;
        if(type != null)
        {
            q = em.createQuery("FROM CatNode node WHERE node.isRoot = true " +
                    "AND node.nodeType = :type").
                    setParameter("type", type.getType());
        }
        else
        {
            q = em.createQuery("FROM CatNode node WHERE node.isRoot = true ");
        }
        final List<CatNode> nodeList = q.getResultList();
        for(final ListIterator<CatNode> it = nodeList.listIterator(); 
                it.hasNext();)
        {
            final CatNode n = it.next();
            n.setIsLeaf(isLeaf(n, true));
        }
        return nodeList;
    }
    
    public List<CatNode> getRootNodes()
    {
        return getRootNodes(null);
    }
    
    /**
     * 
     * @returns true if the node has been deleted from the database, false if
     *          only the link has been deleted
     */
    public boolean deleteNode(final CatNode parent, final CatNode node)
    {
        final EntityManager em = provider.getEntityManager();
        // if parent == null then link will not exist
        Query q = null;
        if(parent != null)
        {
            q = em.createQuery("FROM CatLink c WHERE c.idTo = " + node.
                    getId() + " AND c.idFrom = " + parent.getId());
            provider.delete((CommonEntity)q.getSingleResult());
            if(isLeaf(parent, false))
            {
                parent.setIsLeaf(true);
                nonLeafCache.remove(parent.getId());
            }
        }
        q = em.createQuery("FROM CatLink c WHERE c.idTo = " + node.getId());
        if(q.getResultList().size() == 0)
        {
            q = em.createQuery("FROM CatLink c WHERE c.idFrom = " + node.
                    getId());
            for(final Iterator<CatLink> lit = q.getResultList().iterator(); 
                    lit.hasNext();)
                provider.delete(lit.next());
            provider.delete(node);
            return true;
        }else
        {
            // revoke root status if node has not been deleted
            if(parent == null)
            {
                node.setIsRoot(false);
                provider.store(node);
            }
            return false;
        }
    }
    
    public void deleteRootNode(final CatNode node)
    {
        deleteNode(null, node);
    }
    
    public void moveNode(final CatNode oldParent, final CatNode newParent, final
            CatNode node)
    {
        final EntityManager em = provider.getEntityManager();
        final CatLink link;
        if(oldParent != null)
        {
            link = (CatLink)em.createQuery("FROM CatLink WHERE idFrom = " + 
                    oldParent.getId() + "AND idTo = " + node.getId()).
                    getSingleResult();
            if(isLeaf(oldParent, false))
            {
                oldParent.setIsLeaf(true);
                nonLeafCache.remove(oldParent.getId());
            }
        }
        else
        {
            final Domain domain = (Domain)em.createQuery(
                    "FROM Domain WHERE name = 'LOCAL'").getSingleResult();
            link = new CatLink();
            link.setIdTo(node.getId());
            link.setDomainTo(domain);
        }
        link.setIdFrom(newParent.getId());
        newParent.setIsLeaf(false);
        nonLeafCache.add(newParent.getId());
        provider.store(link);
    }
    
    public void copyNode(final CatNode oldParent, final CatNode newParent, final
            CatNode node)
    {
        final EntityManager em = provider.getEntityManager();
        final Domain domainTo;
                                // there is no domain for dynamic nodes
        if(oldParent != null && node.getId() != -1)
        {
            domainTo = ((CatLink)em.createQuery("FROM CatLink WHERE idFrom = " +
                    oldParent.getId() + "AND idTo = " + node.getId()).
                    getSingleResult()).getDomainTo();
        }else
        {
            domainTo = (Domain)em.createQuery("FROM Domain WHERE name = " +
                    "'LOCAL'").getSingleResult();
        }
        // TODO: will the child nodes be copied also or linked or just left out
        CatNode newNode = new CatNode();
        newNode.setCidsClass(node.getCidsClass());
        newNode.setDerivePermFromClass(node.getDerivePermFromClass());
        newNode.setDynamicChildren(node.getDynamicChildren());
        newNode.setIcon(node.getIcon());
        newNode.setIconFactory(node.getIconFactory());
        newNode.setIsLeaf(node.isLeaf());
        newNode.setIsRoot(newParent == null);
        newNode.setName(node.getName());
        newNode.setNodePermissions(node.getId() == null ? null : 
                copyPermissions(node.getNodePermissions(), newNode));
        newNode.setNodeType(node.getNodeType());
        newNode.setObjectId(node.getObjectId());
        newNode.setPolicy(node.getPolicy());
        newNode.setSqlSort(node.getSqlSort());
        newNode.setUrl(node.getUrl().getId() == null ? null : node.getUrl());
        newNode = addNode(newParent, newNode, domainTo);
        copyLinks(node, newNode);
    }

    private Set<NodePermission> copyPermissions(final Set<NodePermission> perms,
            final CatNode newNode)
    {
        final Set<NodePermission> ret = new HashSet<NodePermission>();
        for(final NodePermission perm : perms)
        {
            final NodePermission newPerm = new NodePermission();
            newPerm.setNode(newNode);
            newPerm.setPermission(perm.getPermission());
            newPerm.setUserGroup(perm.getUserGroup());
            ret.add(newPerm);
        }
        return ret;
    }

    private void copyLinks(final CatNode oldNode, final CatNode newNode)
    {
        final EntityManager em = provider.getEntityManager();
        final List<CatLink> links = em.createQuery("FROM CatLink WHERE " +
                "idFrom = " + oldNode.getId()).getResultList();
        for(final CatLink link : links)
        {
            final CatLink newLink = new CatLink();
            newLink.setIdFrom(newNode.getId());
            newLink.setIdTo(link.getIdTo());
            newLink.setDomainTo(link.getDomainTo());
            provider.store(newLink);
        }
    }

    public void linkNode(final CatNode oldParent, final CatNode newParent, final
            CatNode node)
    {
        final EntityManager em = provider.getEntityManager();
        final CatLink clone;
        if(oldParent != null)
        {
            final CatLink link = (CatLink)em.createQuery("FROM CatLink " +
                    "WHERE idFrom = " + oldParent.getId() + "AND idTo = " +
                    node.getId()).getSingleResult();
            clone = link.clone();
        }
        else
        {
            final Domain domain = (Domain)em.createQuery("FROM Domain " +
                    "WHERE name = 'LOCAL'").getSingleResult();
            clone = new CatLink();
            clone.setDomainTo(domain);
            clone.setIdTo(node.getId());
        }
        clone.setIdFrom(newParent.getId());
        newParent.setIsLeaf(false);
        nonLeafCache.add(newParent.getId());
        provider.store(clone);
    }
    
    /** @deprecated */
    public void moveChildren(CatNode oldParent, CatNode newParent)
    {
        throw new UnsupportedOperationException("cannot be used anymore");
//        if(LOG_DEBUG)
//            log.debug("CatalogBackend: moveChildren requested: from: " + 
//                    oldParent.toString() + " to: " + newParent.toString());
//        final EntityTransaction t = em.getTransaction();
//        try
//        {
//            t.begin();
//            for(Iterator<CatNode> it = getNodeChildren(oldParent).iterator(); 
//                    it.hasNext();)
//            {
//                CatNode child = it.next();
//                CatLink link = (CatLink)backend.getSession().createQuery(
//                        "FROM CatLink WHERE idFrom = " + oldParent.getId() + 
//                        "AND idTo = " + child.getId()).uniqueResult();
//                detach(link);
//                link.setIdFrom(newParent.getId());
//                backend.store(link);
//            }
//        }catch(Throwable tw)
//        {
//            log.error("CatalogBackend: error while moving children from node " +
//                    oldParent.toString() + " to node " + newParent.toString(),
//                    tw);
//            t.rollback();
//            if(tw instanceof HibernateException)
//                backend.reconnect();
//            throw new CatalogBackendException("CatalogBackend: error while " +
//                    "moving children from node " + oldParent.toString() + 
//                    " to node " + newParent.toString(), tw);
//        }
//        t.commit();
    }
    
    public CatNode addNode(final CatNode parent, final CatNode newNode, final 
            Domain domainTo)
    {
        if(newNode == null)
            throw new IllegalArgumentException("new node must not be null");
        if(domainTo == null)
            throw new IllegalArgumentException("domainTo must not be null");
        if(parent == null && !newNode.getIsRoot())
            throw new IllegalArgumentException("if parent == null new node " +
                    "must be root node");
        final CatNode node = provider.store(newNode);
        if(parent != null)
        {
            final CatLink link = new CatLink();
            link.setIdFrom(parent.getId());
            link.setIdTo(node.getId());
            link.setDomainTo(domainTo);
            provider.store(link);
            parent.setIsLeaf(false);
            nonLeafCache.add(parent.getId());
        }
        return node;
    }

    public boolean isLeaf(final CatNode node, final boolean useCache)
    {
        if(useCache && nonLeafCache != null)
            return !nonLeafCache.contains(node.getId());
        final EntityManager em = provider.getEntityManager();
        final Integer id = node.getId();
        final Query q = em.createQuery("select id from CatLink link " +
                "where link.idFrom = :id").setParameter("id", id).
                setMaxResults(1);
        final boolean isLeaf = q.getResultList().size() == 0;
        return isLeaf;
    }
    
    public void reloadNonLeafNodeCache()
    {
        final Thread t = new Thread(new Runnable() 
        {
            public void run()
            {
                getNonLeafNodes();
            }
        });
        t.start();
    }
    
    private synchronized HashSet<Integer> getNonLeafNodes()
    {
        final ResultSet set;
        try
        {
            final Connection c = DatabaseConnection.getConnection(provider.
                    getRuntimeProperties());
            set = c.createStatement().executeQuery(
                    "SELECT DISTINCT id_from FROM cs_cat_link");
        } catch (final Exception ex)
        {
            LOG.error("could not fetch nonLeafCache", ex);
            return null;
        }
        final HashSet<Integer> ret = new HashSet<Integer>();
        try
        {
            while(set.next())
                ret.add(set.getInt(1));
        } catch (final Exception ex)
        {
            LOG.error("could not build non leaf node id cache", ex);
            return null;
        }
        return nonLeafCache = ret;
    }
    
    public Domain getLinkDomain(final CatNode from, final CatNode to)
    {
        if(from == null || to == null)
            return null;
        final EntityManager em = provider.getEntityManager();
        Query q = em.createQuery("FROM CatLink link WHERE link.idFrom = " +
                ":idFrom AND link.idTo = :idTo").
                setParameter("idFrom", from.getId()).
                setParameter("idTo", to.getId());
        final CatLink link = (CatLink)q.getSingleResult();
        return provider.getEntity(Domain.class, link.getDomainTo().getId());
    }
    
    public void setLinkDomain(final CatNode from, final CatNode to, final Domain 
            domainTo)
    {
        if(from == null)
            throw new IllegalArgumentException("from node must not be null");
        if(to == null)
            throw new IllegalArgumentException("to node must not be null");
        if(domainTo == null)
            throw new IllegalArgumentException("domainTo must not be null");
        final EntityManager em = provider.getEntityManager();
        final Query q = em.createQuery("FROM CatLink link WHERE link." +
                "idFrom = :idFrom AND link.idTo = :idTo").
                setParameter("idFrom", from.getId()).
                setParameter("idTo", to.getId());
        final CatLink link = (CatLink)q.getSingleResult();
        link.setDomainTo(domainTo);
        provider.store(link);
    }
}