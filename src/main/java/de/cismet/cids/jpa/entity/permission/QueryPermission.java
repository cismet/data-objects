/*
 * QueryPermission.java
 *
 * Created on 30. August 2007, 14:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.entity.permission;

import de.cismet.cids.jpa.entity.query.Query;
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
 */
@Entity()
@Table(name = "cs_query_ug_assoc")
public class QueryPermission extends AbstractPermission implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_query_ug_assoc_sequence",
            sequenceName = "cs_query_ug_assoc_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_query_ug_assoc_sequence")
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "query_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Query query;
    
    /** Creates a new instance of QueryPermission */
    public QueryPermission()
    {
        query = null;
    }

    public Query getQuery()
    {
        return query;
    }

    public void setQuery(final Query query)
    {
        this.query = query;
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