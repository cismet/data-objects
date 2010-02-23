/*
 * ClassPermission.java
 *
 * Created on 10. Januar 2007, 14:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.entity.permission;

import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
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
 * @author schlob
 */
@Entity()
@Table(name = "cs_ug_class_perm")
public class ClassPermission extends AbstractPermission implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_ug_class_perm_sequence",
            sequenceName = "cs_ug_class_perm_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_ug_class_perm_sequence")
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "class_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private CidsClass cidsClass;
    
    public ClassPermission()
    {
        cidsClass = null;
    }

    public CidsClass getCidsClass()
    {
        return cidsClass;
    }

    public void setCidsClass(final CidsClass cidsClass)
    {
        this.cidsClass = cidsClass;
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