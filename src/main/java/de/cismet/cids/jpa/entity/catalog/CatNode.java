/*
 * CatNode.java
 *
 * Created on 21. Juni 2007, 15:40
 */

package de.cismet.cids.jpa.entity.catalog;

import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.JavaClass;
import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.common.URL;
import de.cismet.cids.jpa.entity.permission.NodePermission;
import de.cismet.cids.jpa.entity.permission.Policy;
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
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author $Author: mscholl $
 * @version $Revision: 1.6 $
 * tag $Name:  $
 * date $Date: 2009/04/16 16:59:54 $
 */
@Entity()
@Table(name = "cs_cat_node")
public class CatNode extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_cat_node_sequence",
            sequenceName = "cs_cat_node_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_cat_node_sequence")
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "descr", nullable = true)
    @Fetch(FetchMode.SELECT)
    private URL url;
    
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = true)
    @Fetch(FetchMode.SELECT)
    private CidsClass cidsClass;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "policy", nullable = true)
    @Fetch(FetchMode.SELECT)
    private Policy policy;

    @Column(name = "derive_permissions_from_class")
    private Boolean derivePermFromClass;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "iconfactory", nullable = true)
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
    
    @OneToMany(cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, 
            mappedBy = "node")
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<NodePermission> nodePermissions;
    
    private transient boolean isLeaf;
    private transient CatNode prospectiveParent;
    
    /**
     * Creates a new instance of <code>CatNode</code>
     */
    public CatNode()
    {
        name = null;
        url = null;
        cidsClass = null;
        objectId = null;
        nodeType = null;
        isRoot = null;
        sqlSort = null;
        dynamicChildren = null;
        isLeaf = true;
        icon = null;
        nodePermissions = new HashSet<NodePermission>();
    }
    
    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public URL getUrl()
    {
        return url;
    }

    public void setUrl(final URL url)
    {
        this.url = url;
    }

    public CidsClass getCidsClass()
    {
        return cidsClass;
    }

    public void setCidsClass(final CidsClass cidsClass)
    {
        this.cidsClass = cidsClass;
    }

    public Integer getObjectId()
    {
        return objectId;
    }

    public void setObjectId(final Integer objectId)
    {
        this.objectId = objectId;
    }

    public String getNodeType()
    {
        return nodeType;
    }

    public void setNodeType(final String nodeType)
    {
        this.nodeType = nodeType;
    }

    public Boolean getIsRoot()
    {
        return isRoot;
    }

    public void setIsRoot(final Boolean isRoot)
    {
        this.isRoot = isRoot;
    }

    public Boolean getSqlSort()
    {
        return sqlSort;
    }

    public void setSqlSort(final Boolean sqlSort)
    {
        this.sqlSort = sqlSort;
    }

    public String getDynamicChildren()
    {
        return dynamicChildren;
    }

    public void setDynamicChildren(final String dynamicChildren)
    {
        this.dynamicChildren = dynamicChildren;
    }

    public boolean isLeaf()
    {
        return isLeaf;
    }

    public void setIsLeaf(final boolean isLeaf)
    {
        this.isLeaf = isLeaf;
    }

    public CatNode getProspectiveParent()
    {
        return prospectiveParent;
    }

    public void setProspectiveParent(final CatNode prospectiveParent)
    {
        this.prospectiveParent = prospectiveParent;
    }

    @Override
    public String toString()
    {
        return getName() + "(" + getId() + ")";
    }

    public Set<NodePermission> getNodePermissions()
    {
        return nodePermissions;
    }

    public void setNodePermissions(final Set<NodePermission> nodePermissions)
    {
        this.nodePermissions = nodePermissions;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(final Integer id)
    {
        this.id = id;
    }

    public Policy getPolicy()
    {
        return policy;
    }

    public void setPolicy(final Policy policy)
    {
        this.policy = policy;
    }

    public Boolean getDerivePermFromClass()
    {
        return derivePermFromClass;
    }

    public void setDerivePermFromClass(final Boolean derivePermFromClass)
    {
        this.derivePermFromClass = derivePermFromClass;
    }

    public JavaClass getIconFactory()
    {
        return iconFactory;
    }

    public void setIconFactory(final JavaClass iconFactory)
    {
        this.iconFactory = iconFactory;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(final String icon)
    {
        this.icon = icon;
    }
    
    public static final class Type
    {
        public static final Type CLASS = new Type("C");
        public static final Type OBJECT = new Type("O");
        public static final Type ORG = new Type("N");
        
        private final String type;
        
        private Type(final String type)
        {
            this.type = type;
        }
        
        public String getType()
        {
            return type;
        }

        @Override
        public boolean equals(final Object object)
        {
            if(object instanceof CatNode.Type)
                return this.type.equals(((CatNode.Type)object).type);
            return false;
        }

        @Override
        public int hashCode()
        {
            return type.hashCode();
        }
    }
}