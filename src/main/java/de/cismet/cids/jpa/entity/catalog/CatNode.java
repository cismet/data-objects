/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.catalog;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.JavaClass;
import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.common.PermissionAwareEntity;
import de.cismet.cids.jpa.entity.common.URL;
import de.cismet.cids.jpa.entity.permission.AbstractPermission;
import de.cismet.cids.jpa.entity.permission.NodePermission;
import de.cismet.cids.jpa.entity.permission.Policy;

/**
 * DOCUMENT ME!
 *
 * @author   $Author: mscholl $
 * @version  $Revision: 1.6 $ tag $Name: $ date $Date: 2009/04/16 16:59:54 $
 */
@Entity
@Table(name = "cs_cat_node")
public class CatNode extends CommonEntity implements Serializable, PermissionAwareEntity {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_cat_node_sequence",
        sequenceName = "cs_cat_node_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_cat_node_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "descr",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    private URL url;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "class_id",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    private CidsClass cidsClass;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "policy",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    private Policy policy;

    @Column(name = "derive_permissions_from_class")
    private Boolean derivePermFromClass;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "iconfactory",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    private JavaClass iconFactory;

    @Column(name = "object_id")
    private Integer objectId;

    @Column(name = "node_type")
    private String nodeType;

    @Column(name = "is_root")
    private Boolean isRoot;

    @Column(name = "sql_sort")
    private Boolean sqlSort;

    @Column(name = "dynamic_children")
    private String dynamicChildren;

    @Column(name = "icon")
    private String icon;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "node"
    )
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<NodePermission> nodePermissions;

    private transient boolean leaf;
    private transient CatNode prospectiveParent;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>CatNode.</code>
     */
    public CatNode() {
        derivePermFromClass = false;
        leaf = true;
        nodePermissions = new HashSet<NodePermission>();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name  DOCUMENT ME!
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public URL getUrl() {
        return url;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    public void setUrl(final URL url) {
        this.url = url;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsClass getCidsClass() {
        return cidsClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsClass  DOCUMENT ME!
     */
    public void setCidsClass(final CidsClass cidsClass) {
        this.cidsClass = cidsClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Integer getObjectId() {
        return objectId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  objectId  DOCUMENT ME!
     */
    public void setObjectId(final Integer objectId) {
        this.objectId = objectId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  nodeType  DOCUMENT ME!
     */
    public void setNodeType(final String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getIsRoot() {
        return isRoot;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isRoot  DOCUMENT ME!
     */
    public void setIsRoot(final Boolean isRoot) {
        this.isRoot = isRoot;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getSqlSort() {
        return sqlSort;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sqlSort  DOCUMENT ME!
     */
    public void setSqlSort(final Boolean sqlSort) {
        this.sqlSort = sqlSort;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDynamicChildren() {
        return dynamicChildren;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  dynamicChildren  DOCUMENT ME!
     */
    public void setDynamicChildren(final String dynamicChildren) {
        this.dynamicChildren = dynamicChildren;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isLeaf() {
        return leaf;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isLeaf  DOCUMENT ME!
     */
    public void setIsLeaf(final boolean isLeaf) {
        this.leaf = isLeaf;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CatNode getProspectiveParent() {
        return prospectiveParent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  prospectiveParent  DOCUMENT ME!
     */
    public void setProspectiveParent(final CatNode prospectiveParent) {
        this.prospectiveParent = prospectiveParent;
    }

    @Override
    public String toString() {
        return getName() + "(" + getId() + ")"; // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<NodePermission> getNodePermissions() {
        return nodePermissions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  nodePermissions  DOCUMENT ME!
     */
    public void setNodePermissions(final Set<NodePermission> nodePermissions) {
        this.nodePermissions = nodePermissions;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Policy getPolicy() {
        return policy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  policy  DOCUMENT ME!
     */
    public void setPolicy(final Policy policy) {
        this.policy = policy;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getDerivePermFromClass() {
        return derivePermFromClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  derivePermFromClass  DOCUMENT ME!
     */
    public void setDerivePermFromClass(final Boolean derivePermFromClass) {
        this.derivePermFromClass = derivePermFromClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JavaClass getIconFactory() {
        return iconFactory;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  iconFactory  DOCUMENT ME!
     */
    public void setIconFactory(final JavaClass iconFactory) {
        this.iconFactory = iconFactory;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getIcon() {
        return icon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  icon  DOCUMENT ME!
     */
    public void setIcon(final String icon) {
        this.icon = icon;
    }

    @Override
    public Set<? extends AbstractPermission> getPermissions() {
        return getNodePermissions();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static final class Type {

        //~ Static fields/initializers -----------------------------------------

        public static final Type CLASS = new Type("C");  // NOI18N
        public static final Type OBJECT = new Type("O"); // NOI18N
        public static final Type ORG = new Type("N");    // NOI18N

        //~ Instance fields ----------------------------------------------------

        private final transient String type;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Type object.
         *
         * @param  type  DOCUMENT ME!
         */
        private Type(final String type) {
            this.type = type;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getType() {
            return type;
        }

        @Override
        public boolean equals(final Object object) {
            if (object instanceof CatNode.Type) {
                return this.type.equals(((CatNode.Type)object).type);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return type.hashCode();
        }
    }
}
