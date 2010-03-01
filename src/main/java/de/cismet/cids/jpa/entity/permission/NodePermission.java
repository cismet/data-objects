/*
 * NodePermission.java
 *
 * Created on 21. Juni 2007, 15:39
 */

package de.cismet.cids.jpa.entity.permission;

import de.cismet.cids.jpa.entity.catalog.CatNode;
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
@Table(name = "cs_ug_cat_node_perm")
public class NodePermission extends AbstractPermission implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_ug_cat_node_perm_sequence",
            sequenceName = "cs_ug_cat_node_perm_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_ug_cat_node_perm_sequence")
    @Column(name = "id")
    private Integer id;
 
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "cat_node_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private CatNode node;
    
    public NodePermission()
    {
        node = null;
    }
    
    public CatNode getNode()
    {
        return node;
    }

    public void setNode(final CatNode node)
    {
        this.node = node;
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