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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity()
@Table(name = "cs_class_attr")
public class ClassAttribute extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_class_attr_sequence",
            sequenceName = "cs_class_attr_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_class_attr_sequence")
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "attr_key")
    private String attrKey;
    
    @Column(name = "attr_value")
    private String attrValue;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private CidsClass cidsClass;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Type type;
    
    public ClassAttribute()
    {
        setAttrKey(null);
        setAttrValue(null);
        setCidsClass(null);
        setType(null);
    }

    public String getAttrKey()
    {
        return attrKey;
    }

    public void setAttrKey(final String attrKey)
    {
        this.attrKey = attrKey;
    }

    public String getAttrValue()
    {
        return attrValue;
    }

    public void setAttrValue(final String attrValue)
    {
        this.attrValue = attrValue;
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
    
    public String toString()
    {
        return getAttrKey() + "(" + getId() + ")" + getAttrValue();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}