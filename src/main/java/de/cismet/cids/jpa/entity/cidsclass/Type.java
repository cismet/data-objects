package de.cismet.cids.jpa.entity.cidsclass;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity()
@Table(name = "cs_type")
public class Type extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_type_sequence",
            sequenceName = "cs_type_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_type_sequence")
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    // not everything is complex, so nullable = true
    @OneToOne(optional = true, 
            fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = true)
    @Fetch(FetchMode.SELECT)
    private CidsClass cidsClass;
    
    @Column(name = "complex_type")
    private Boolean complexType;
    
    @Column(name = "descr")
    private String description;
    
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
    
    public Type()
    { 
        name = null;
        cidsClass = null;
        complexType = null;
        description = null;
        editor = null;
        renderer = null;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public CidsClass getCidsClass()
    {
        return cidsClass;
    }

    public void setCidsClass(final CidsClass cidsClass)
    {
        this.cidsClass = cidsClass;
    }

    public Boolean isComplexType()
    {
        return complexType;
    }

    public void setComplexType(final Boolean complexType)
    {
        this.complexType = complexType;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
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
    
    public String toString()
    {
        return getName() + "(" + getId() + ")";
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(final Integer id)
    {
        this.id = id;
    }
}