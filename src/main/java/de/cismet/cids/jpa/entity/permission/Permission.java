/*
 * Permission.java
 *
 * Created on 10. Januar 2007, 14:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.entity.permission;

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
 * @author schlob
 */
@Entity()
@Table(name = "cs_permission")
public class Permission extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_permission_sequence",
            sequenceName = "cs_permission_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_permission_sequence")
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "key")
    private String key;
    
    @Column(name = "description")
    private String description;

    public static final Permission NO_PERMISSION;

    static
    {
        NO_PERMISSION = new Permission();
        NO_PERMISSION.setKey("<kein Recht>");
    }
    
    /** Creates a new instance of Permission */
    public Permission()
    {
        key = null;
        description = null;
    }
   
    public String getKey()
    {
        return key;
    }
    
    public void setKey(final String key)
    {
        this.key = key;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(final String description)
    {
        this.description = description;
    }
    
    public String toString()
    {
        return key;// + "(" + getId() + ")";
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