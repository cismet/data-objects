package de.cismet.cids.jpa.entity.cidsclass;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.permission.ClassPermission;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * CidsClass objects are expensive so query them wisely
 */
@Entity()
@Table(name = "cs_class")
public class CidsClass extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_class_sequence",
            sequenceName = "cs_class_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_class_sequence")
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "descr")
    private String description;
    
    @ManyToOne(optional = true, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "class_icon_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Icon classIcon;
    
    @ManyToOne(optional = true, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "object_icon_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Icon objectIcon;
    
    @Column(name = "table_name")
    private String tableName;
    
    @Column(name = "primary_key_field")
    private String primaryKeyField;
    
    @Column(name = "indexed")
    private Boolean indexed;
    
    @ManyToOne(optional = true, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "tostring", nullable = true)
    @Fetch(FetchMode.SELECT)
    private JavaClass toString;
    
    @ManyToOne(optional = true, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "editor", nullable = true)
    @Fetch(FetchMode.SELECT)
    private JavaClass editor;
    
    @ManyToOne(optional = true, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "renderer", nullable = true)
    @Fetch(FetchMode.SELECT)
    private JavaClass renderer;

    @ManyToOne(optional = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "policy", nullable = true)
    @Fetch(FetchMode.SELECT)
    private Policy policy;

    @ManyToOne(optional = true,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_policy", nullable = true)
    @Fetch(FetchMode.SELECT)
    private Policy attributePolicy;
    
    @Column(name = "array_link")
    private Boolean arrayLink;
    
    @OneToMany(cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, 
            mappedBy = "cidsClass")
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<Attribute> attributes;
    
    @OneToMany(cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, 
            mappedBy = "cidsClass")
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<ClassAttribute> classAttributes;
    
    @OneToOne(cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, 
            mappedBy = "cidsClass")
    @Fetch(FetchMode.SELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Type type;
    
    @OneToMany(cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, 
            mappedBy = "cidsClass")
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<ClassPermission> classPermissions;
    
    public static final CidsClass NO_CLASS;
    
    static
    {
        final CidsClass cc = new CidsClass();
        cc.setName("<keine Klasse>");
        cc.setTableName("<keine Klasse>");
        NO_CLASS = cc;
    }
    
    public CidsClass()
    {
        name = null;
        description = null;
        classIcon = null;
        objectIcon = null;
        tableName = null;
        primaryKeyField = null;
        // avoid npe if not null constraint is not set
        indexed = false;
        toString = null;
        editor = null;
        renderer = null;
        // avoid npe if not null constraint is not set
        arrayLink = false;
        attributes = new HashSet<Attribute>();
        classAttributes = new HashSet<ClassAttribute>();
        type = null;
        classPermissions = new HashSet<ClassPermission>();
        policy = null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public Icon getClassIcon()
    {
        return classIcon;
    }

    public void setClassIcon(final Icon classIcon)
    {
        this.classIcon = classIcon;
    }

    public Icon getObjectIcon()
    {
        return objectIcon;
    }

    public void setObjectIcon(final Icon objectIcon)
    {
        this.objectIcon = objectIcon;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(final String tableName)
    {
        this.tableName = tableName;
    }

    public String getPrimaryKeyField()
    {
        return primaryKeyField;
    }

    public void setPrimaryKeyField(final String primaryKeyField)
    {
        this.primaryKeyField = primaryKeyField;
    }

    public Boolean isIndexed()
    {
        return indexed;
    }

    public void setIndexed(final Boolean indexed)
    {
        this.indexed = indexed;
    }

    public JavaClass getToString()
    {
        return toString;
    }

    public void setToString(final JavaClass toString)
    {
        this.toString = toString;
    }

    public JavaClass getEditor()
    {
        return editor;
    }

    public void setEditor(final JavaClass editor)
    {
        this.editor = editor;
    }

    public JavaClass getRenderer()
    {
        return renderer;
    }

    public void setRenderer(final JavaClass renderer)
    {
        this.renderer = renderer;
    }

    public Boolean isArrayLink()
    {
        return arrayLink;
    }

    public void setArrayLink(final Boolean arrayLink)
    {
        this.arrayLink = arrayLink;
    }

    public Set<ClassAttribute> getClassAttributes()
    {
        return classAttributes;
    }

    public void setClassAttributes(final Set<ClassAttribute> classAttributes)
    {
        this.classAttributes = classAttributes;
    }

    public Set<Attribute> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(final Set<Attribute> attributes)
    {
        this.attributes = attributes;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(final Type type)
    {
        this.type = type;
    }

    public Set<ClassPermission> getClassPermissions()
    {
        return classPermissions;
    }

    public void setClassPermissions(final Set<ClassPermission> classPermissions)
    {
        this.classPermissions = classPermissions;
    }
    
    @Override
    public String toString()
    {
        return getTableName();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public Policy getAttributePolicy()
    {
        return attributePolicy;
    }

    public void setAttributePolicy(final Policy attributePolicy)
    {
        this.attributePolicy = attributePolicy;
    }
}