/*
 * AttributePermission.java
 *
 * Created on 24. January 2008, 15:45
 */

package de.cismet.cids.jpa.entity.permission;

import de.cismet.cids.jpa.entity.cidsclass.Attribute;
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

/**
 *
 * @author mscholl
 * @version 1.0
 */
@Entity()
@Table(name = "cs_ug_attr_perm")
public class AttributePermission extends AbstractPermission implements 
        Serializable
{
    @Id
    @SequenceGenerator(name = "cs_ug_attr_perm_sequence",
            sequenceName = "cs_ug_attr_perm_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_ug_attr_perm_sequence")
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "attr_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Attribute attribute;
    
    public AttributePermission()
    {
        attribute = null;
    }

    public Attribute getAttribute()
    {
        return attribute;
    }

    public void setAttribute(final Attribute attribute)
    {
        this.attribute = attribute;
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