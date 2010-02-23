/*
 * QueryParameter.java
 *
 * Created on 30. August 2007, 12:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.entity.query;

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

/**
 *
 * @author mscholl
 */
@Entity()
@Table(name = "cs_query_parameter")
public class QueryParameter extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_query_parameter_sequence",
            sequenceName = "cs_query_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_query_parameter_sequence")
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "param_key")
    private String key;
    
    @Column(name = "descr")
    private String description;
    
    @Column(name = "is_query_result")
    private Boolean isQueryResult;
    
    // foreign key??? should be, wont be used
    @Column(name = "type_id")
    private Integer typeID;
    
    @Column(name = "query_position")
    private Integer position;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "query_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Query query;
    
    /** Creates a new instance of QueryParameter */
    public QueryParameter()
    {
        this.key = null;
        this.description = null;
        this.isQueryResult = null;
        this.typeID = null;
        this.position = null;
        this.query = null;
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

    public Boolean getIsQueryResult()
    {
        return isQueryResult;
    }

    public void setIsQueryResult(final Boolean isQueryResult)
    {
        this.isQueryResult = isQueryResult;
    }

    public Integer getTypeID()
    {
        return typeID;
    }

    public void setTypeID(final Integer typeID)
    {
        this.typeID = typeID;
    }

    public Integer getPosition()
    {
        return position;
    }

    public void setPosition(final Integer position)
    {
        this.position = position;
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