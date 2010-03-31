/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.catalog.CatNode;
import de.cismet.cids.jpa.entity.common.Domain;

import java.util.List;
import java.util.Map;

/**
 * DOCUMENT ME!
 *
 * @author   Martin Scholl
 * @version  1.0
 */
public interface CatalogService {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<CatNode> getNodeChildren(final CatNode node);

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<CatNode> getNodeParents(final CatNode node);

    /**
     * DOCUMENT ME!
     *
     * @param   type  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<CatNode> getRootNodes(final CatNode.Type type);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<CatNode> getRootNodes();

    /**
     * DOCUMENT ME!
     *
     * @param   from  DOCUMENT ME!
     * @param   to    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Domain getLinkDomain(final CatNode from, final CatNode to);

    /**
     * DOCUMENT ME!
     *
     * @param  from      DOCUMENT ME!
     * @param  to        DOCUMENT ME!
     * @param  domainTo  DOCUMENT ME!
     */
    void setLinkDomain(final CatNode from, final CatNode to, final Domain domainTo);

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Map<String, String> getSimpleObjectInformation(final CatNode node);

    /**
     * DOCUMENT ME!
     *
     * @param   parent  DOCUMENT ME!
     * @param   node    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean deleteNode(final CatNode parent, final CatNode node);

    /**
     * DOCUMENT ME!
     *
     * @param  node  DOCUMENT ME!
     */
    void deleteRootNode(final CatNode node);

    /**
     * DOCUMENT ME!
     *
     * @param   parent    DOCUMENT ME!
     * @param   newNode   DOCUMENT ME!
     * @param   domainTo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    CatNode addNode(final CatNode parent, final CatNode newNode, final Domain domainTo);

    /**
     * DOCUMENT ME!
     *
     * @param  oldParent  DOCUMENT ME!
     * @param  newParent  DOCUMENT ME!
     * @param  node       DOCUMENT ME!
     */
    void moveNode(final CatNode oldParent, final CatNode newParent, final CatNode node);

    /**
     * DOCUMENT ME!
     *
     * @param  oldParent  DOCUMENT ME!
     * @param  newParent  DOCUMENT ME!
     * @param  node       DOCUMENT ME!
     */
    void copyNode(final CatNode oldParent, final CatNode newParent, final CatNode node);

    /**
     * DOCUMENT ME!
     *
     * @param  oldParent  DOCUMENT ME!
     * @param  newParent  DOCUMENT ME!
     * @param  node       DOCUMENT ME!
     */
    void linkNode(final CatNode oldParent, final CatNode newParent, final CatNode node);

    /**
     * DOCUMENT ME!
     *
     * @param   node      DOCUMENT ME!
     * @param   useCache  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isLeaf(final CatNode node, final boolean useCache);

    /**
     * DOCUMENT ME!
     */
    void reloadNonLeafNodeCache();
}
