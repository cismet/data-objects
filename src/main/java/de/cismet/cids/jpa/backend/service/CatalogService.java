/*
 * CatalogService.java
 *
 * Created on 21. Juni 2007, 15:40
 */

package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.catalog.CatNode;
import de.cismet.cids.jpa.entity.common.Domain;
import java.util.HashMap;
import java.util.List;

/**
 * @author Martin Scholl
 * @version 1.0
 */
public interface CatalogService
{
    public List<CatNode> getNodeChildren(final CatNode node);
    
    public List<CatNode> getNodeParents(final CatNode node);
    
    public List<CatNode> getRootNodes(final CatNode.Type type);
    
    public List<CatNode> getRootNodes();
    
    public Domain getLinkDomain(final CatNode from, final CatNode to);
    
    public void setLinkDomain(final CatNode from, final CatNode to, 
            final Domain domainTo);
    
    // became obsolete due to mapping
    //public List<NodePermission> getNodePermission(final CatNode node);
    
    public HashMap<String, String> getSimpleObjectInformation(final CatNode 
            node);
    
    // became obsolete in favor of store(T extends CommonEntity)
    //public CatNode storeNode(final CatNode node) throws
            //CatalogBackendException;
    
    // became obsolete due to mapping
    //public NodePermission storeNodePermission(final NodePermission permission) 
            //throws
            //CatalogBackendException;
    
    // became obsolete due to mapping
    //public List<NodePermission> storeNodePermissions(final CatNode node, final
            //List<NodePermission> permissions) throws
            //CatalogBackendException;
    
    public boolean deleteNode(final CatNode parent, final CatNode node);
    
    public void deleteRootNode(final CatNode node);
    
    // became obsolete due to mapping
    //public void deleteNodePermission(final NodePermission permission);
    
    // became obsolete due to mapping
    //public void deleteNodePermissions(final List<NodePermission> permissions);
    
    public CatNode addNode(final CatNode parent, final CatNode newNode, final 
            Domain domainTo);
    
    public void moveNode(final CatNode oldParent, final CatNode newParent, final
            CatNode node);
    
    public void copyNode(final CatNode oldParent, final CatNode newParent, final 
            CatNode node);

    public void linkNode(final CatNode oldParent, final CatNode newParent, final
            CatNode node);
    
    public void moveChildren(final CatNode oldParent, final CatNode newParent);
    
    public boolean isLeaf(final CatNode node, final boolean useCache);
    
    public void reloadNonLeafNodeCache();
}