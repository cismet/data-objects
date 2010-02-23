/*
 * Permission.java
 *
 * Created on 10. Januar 2007, 14:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.entity.common;

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
@Table(name = "cs_domain")
public class Domain extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_domain_sequence",
            sequenceName = "cs_domain_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_domain_sequence")
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "name")
    private String name;
    
    /** Creates a new instance of Permission */
    public Domain()
    {
        name = null;
    }
       
    public String getName()
    {
        return name;
    }
    
    public void setName(final String name)
    {
        this.name = name;
    }
    
    public String toString()
    {
        return name;
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