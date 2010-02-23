/*
 * CatLink.java
 *
 * Created on 21. Juni 2007, 15:40
 */

package de.cismet.cids.jpa.entity.catalog;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.common.Domain;
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
 * @author Martin Scholl
 * @version 1.0
 */
@Entity()
@Table(name = "cs_cat_link")
public class CatLink extends CommonEntity implements Serializable, Cloneable
{
    @Id
    @SequenceGenerator(name = "cs_cat_link_sequence",
            sequenceName = "cs_cat_link_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_cat_link_sequence")
    @Column(name = "id")
    private Integer id;
    
    // maybe there is no need to map this
//    @ManyToOne(optional = false, fetch = FetchType.EAGER)
//    @JoinColumn(name = "id_from", nullable = false)
//    private CatNode fromNode;
//    
//    // maybe there is no need to map this
//    @ManyToOne(optional = false, fetch = FetchType.EAGER)
//    @JoinColumn(name = "id_to", nullable = false)
//    private CatNode toNode;
    
    @Column(name = "id_to")
    private Integer idTo;
    
    @Column(name = "id_from")
    private Integer idFrom;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "domain_to", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Domain domainTo;
    
    /**
     * Creates a new instance of <code>CatLink</code>
     */
    public CatLink()
    {
//        fromNode = null;
//        toNode = null;
        idTo = null;
        idFrom = null;
        domainTo = null;
    }

//    public CatNode getFromNode()
//    {
//        return fromNode;
//    }
//
//    public void setFromNode(final CatNode fromNode)
//    {
//        this.fromNode = fromNode;
//    }
//
//    public CatNode getToNode()
//    {
//        return toNode;
//    }
//
//    public void setToNode(final CatNode toNode)
//    {
//        this.toNode = toNode;
//    }

    public Domain getDomainTo()
    {
        return domainTo;
    }

    public void setDomainTo(final Domain domainTo)
    {
        this.domainTo = domainTo;
    }
    
    public String toString()
    {
        return "ID: " + getId() + " :: Link from '" + getIdFrom() + "' to '" +
                getIdTo();
    }
    
    public CatLink clone()
    {
        final CatLink link = new CatLink();
        link.idFrom = getIdFrom();
        link.idTo = getIdTo();
        link.domainTo = getDomainTo();
        return link;
    }

    public Integer getIdTo()
    {
        return idTo;
    }

    public void setIdTo(Integer idTo)
    {
        this.idTo = idTo;
    }

    public Integer getIdFrom()
    {
        return idFrom;
    }

    public void setIdFrom(Integer idFrom)
    {
        this.idFrom = idFrom;
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