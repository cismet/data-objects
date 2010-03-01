/*
 * CatalogService.java
 *
 * Created on 21. Juni 2007, 15:40
 */

package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.catalog.CatNode;
import de.cismet.cids.jpa.entity.common.Domain;
import java.util.*;

/**
 * @author Martin Scholl
 * @version 1.0
 */
public interface CatalogService
{
    List<CatNode> getNodeChildren(final CatNode node);
    
    List<CatNode> getNodeParents(final CatNode node);
    
    List<CatNode> getRootNodes(final CatNode.Type type);
    
    List<CatNode> getRootNodes();
    
    Domain getLinkDomain(final CatNode from, final CatNode to);
    
    void setLinkDomain(final CatNode from, final CatNode to,
                       final Domain domainTo);
    
    Map<String, String> getSimpleObjectInformation(final CatNode node);

    
    boolean deleteNode(final CatNode parent, final CatNode node);
    
    void deleteRootNode(final CatNode node);


    CatNode addNode(final CatNode parent, final CatNode newNode, 
                    final Domain domainTo);
    
    void moveNode(final CatNode oldParent, final CatNode newParent, 
                  final CatNode node);
    
    void copyNode(final CatNode oldParent, final CatNode newParent, 
                  final CatNode node);

    void linkNode(final CatNode oldParent, final CatNode newParent, 
                  final CatNode node);
    
    void moveChildren(final CatNode oldParent, final CatNode newParent);
    
    boolean isLeaf(final CatNode node, final boolean useCache);
    
    void reloadNonLeafNodeCache();
}