package de.cismet.cids.jpa.entity.cidsclass;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.permission.AttributePermission;
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

@Entity()
@Table(name = "cs_attr")
/**
 * $Author: mscholl $
 * $State: Exp $
 * $Revision: 1.3 $
 * $Header: /cvs/cidsDataObjects/src/de/cismet/cids/jpa/entity/cidsclass/Attribute.java,v 1.3 2008/03/19 11:40:37 mscholl Exp $
 * $Name:  $
 */
public class Attribute extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_attr_sequence",
            sequenceName = "cs_attr_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_attr_sequence")
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne(optional = false, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private CidsClass cidsClass;
    
    @ManyToOne(optional = false, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Type type;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "field_name")
    private String fieldName;
    
    @Column(name = "foreign_key")
    private Boolean foreignKey;
    
    @Column(name = "substitute")
    private Boolean substitute;
    
    @Column(name = "foreign_key_references_to")
    private Integer foreignKeyClass;
    
    @Column(name = "descr")
    private String description;
    
    @Column(name = "visible")
    private Boolean visible;
    
    @Column(name = "indexed")
    private Boolean indexed;
    
    @Column(name = "isarray")
    private Boolean array;
    
    @Column(name = "array_key")
    private String arrayKey;
    
    @ManyToOne(optional = true, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "editor", nullable = true)
    @Fetch(FetchMode.SELECT)
    private JavaClass editor;
    
    @ManyToOne(optional = true, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "tostring", nullable = true)
    @Fetch(FetchMode.SELECT)
    private JavaClass toString;
    
    @ManyToOne(optional = true, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "complex_editor", nullable = true)
    @Fetch(FetchMode.SELECT)
    private JavaClass complexEditor;
    
    @Column(name = "optional")
    private Boolean optional;
    
    @Column(name = "default_value")
    private String defaultValue;
    
    /** depricated 
    private JavaClass fromString; */
    
    @Column(name = "pos")
    private Integer position;
    
    @Column(name = "precision")
    private Integer precision;
    
    @Column(name = "scale")
    private Integer scale;
    
    @OneToMany(cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, 
            mappedBy = "attribute")
    @Fetch(FetchMode.SELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<AttributePermission> attributePermissions;
    
    public Attribute()
    {
        array = false;
        arrayKey = null;
        cidsClass = null;
        complexEditor = null;
        defaultValue = null;
        description = null;
        editor = null;
        fieldName = null;
        foreignKey = false;
        foreignKeyClass = null;
        indexed = false;
        name = null;
        optional = true;
        position = 0;
        precision = null;
        scale = null;
        substitute = false;
        toString = null;
        type = null;
        visible = true;
        attributePermissions = new HashSet<AttributePermission>();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(final Integer id)
    {
        this.id = id;
    }

    public CidsClass getCidsClass()
    {
        return cidsClass;
    }

    public void setCidsClass(final CidsClass cidsClass)
    {
        this.cidsClass = cidsClass;
    }

    public Type getType()
    {
        return type;
    }

    public void setType(final Type type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(final String fieldName)
    {
        this.fieldName = fieldName;
    }

    public Boolean isForeignKey()
    {
        return foreignKey;
    }

    public void setForeignKey(final Boolean foreignKey)
    {
        this.foreignKey = foreignKey;
    }

    public Boolean isSubstitute()
    {
        return substitute;
    }

    public void setSubstitute(final Boolean substitute)
    {
        this.substitute = substitute;
    }

    public Integer getForeignKeyClass()
    {
        return foreignKeyClass;
    }

    public void setForeignKeyClass(final Integer foreignKeyClass)
    {
        this.foreignKeyClass = foreignKeyClass;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public Boolean isVisible()
    {
        return visible;
    }

    public void setVisible(final Boolean visible)
    {
        this.visible = visible;
    }

    public Boolean isIndexed()
    {
        return indexed;
    }

    public void setIndexed(final Boolean indexed)
    {
        this.indexed = indexed;
    }

    public Boolean isArray()
    {
        return array;
    }

    public void setArray(final Boolean array)
    {
        this.array = array;
    }

    public String getArrayKey()
    {
        return arrayKey;
    }

    public void setArrayKey(final String arrayKey)
    {
        this.arrayKey = arrayKey;
    }

    public JavaClass getEditor()
    {
        return editor;
    }

    public void setEditor(final JavaClass editor)
    {
        this.editor = editor;
    }

    public JavaClass getToString()
    {
        return toString;
    }

    public void setToString(final JavaClass toString)
    {
        this.toString = toString;
    }

    public JavaClass getComplexEditor()
    {
        return complexEditor;
    }

    public void setComplexEditor(final JavaClass complexEditor)
    {
        this.complexEditor = complexEditor;
    }

    public Boolean isOptional()
    {
        return optional;
    }

    public void setOptional(final Boolean optional)
    {
        this.optional = optional;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(final String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public Integer getPosition()
    {
        return position;
    }

    public void setPosition(final Integer position)
    {
        this.position = position;
    }

    public Integer getPrecision()
    {
        return precision;
    }

    public void setPrecision(final Integer precision)
    {
        this.precision = precision;
    }

    public Integer getScale()
    {
        return scale;
    }

    public void setScale(final Integer scale)
    {
        this.scale = scale;
    }

    public Set<AttributePermission> getAttributePermissions()
    {
        return attributePermissions;
    }

    public void setAttributePermissions(final Set<AttributePermission> 
            attributePermission)
    {
        this.attributePermissions = attributePermissions;
    }
    
    public String toString()
    {
        return getName() + "(" + getId() + ")";
    }
}