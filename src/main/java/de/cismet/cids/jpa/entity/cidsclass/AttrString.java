/*
 * AttrString.java
 *
 * Created on 23. November 2007, 12:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.entity.cidsclass;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author cschmidt
 */
@Entity()
@Table(name = "cs_attr_string")
public class AttrString extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_attr_string_sequence",
            sequenceName = "cs_attr_string_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_attr_string_sequence")
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "class_id")
    private Integer classId;
    
    @Column(name = "attr_id")
    private Integer attrId;
    
    @Column(name = "object_id")
    private Integer objectId;
    
    @Column(name = "string_val")
    private String stringVal;
    
    /** Creates a new instance of AttrString */
    public AttrString()
    {
        classId = attrId = objectId = null;
        stringVal = null;
    }

    public Integer getClassId()
    {
        return classId;
    }

    public void setClassId(final Integer classId)
    {
        this.classId = classId;
    }

    public Integer getAttrId()
    {
        return attrId;
    }

    public void setAttrId(final Integer attrId)
    {
        this.attrId = attrId;
    }

    public Integer getObjectId()
    {
        return objectId;
    }

    public void setObjectId(final Integer objectId)
    {
        this.objectId = objectId;
    }

    public String getStringVal()
    {
        return stringVal;
    }

    public void setStringVal(final String stringVal)
    {
        this.stringVal = stringVal;
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