/*
 * AllAttrMapping.java.java
 *
 * Created on 23. November 2007, 11:59
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
@Table(name = "cs_all_attr_mapping")
public class AllAttrMapping extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_all_attr_mapping_sequence",
            sequenceName = "cs_all_attr_mapping_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "cs_all_attr_mapping_sequence")
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "class_id")
    private Integer classId;
    
    @Column(name = "object_id")
    private Integer objectId;
    
    @Column(name = "attr_class_id")
    private Integer attrClassId;
    
    @Column(name = "attr_object_id")
    private Integer attrObjectId;
    
    /** Creates a new instance of AllAttrMapping */
    public AllAttrMapping()
    {
        classId = objectId = attrClassId = attrObjectId = null;
    }
    
    public Integer getClassId()
    {
        return classId;
    }
    
    public void setClassId(final Integer classId)
    {
        this.classId = classId;
    }
    
    public Integer getObjectId()
    {
        return objectId;
    }
    
    public void setObjectId(final Integer objectId)
    {
        this.objectId = objectId;
    }
    
    public Integer getAttrClassId()
    {
        return attrClassId;
    }
    
    public void setAttrClassId(final Integer attrClassId)
    {
        this.attrClassId = attrClassId;
    }
    
    public Integer getAttrObjectId()
    {
        return attrObjectId;
    }
    
    public void setAttrObjectId(final Integer attrObjectId)
    {
        this.attrObjectId = attrObjectId;
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