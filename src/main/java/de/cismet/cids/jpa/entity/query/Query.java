/*
 * Query.java
 *
 * Created on 30. August 2007, 11:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.entity.query;

import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.permission.QueryPermission;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author mscholl
 */
@Entity()
@Table(name = "cs_query")
public class Query extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_query_sequence",
            sequenceName = "cs_query_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_query_sequence")
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "name")
    private String name;
    
    @Column(name = "descr")
    private String description;
    
    @Column(name = "statement")
    private String statement;
    
    @Column(name = "result")
    private Integer result;
    
    @Column(name = "is_update")
    private Boolean isUpdate;
    
    @Column(name = "is_union")
    private Boolean isUnion;
    
    @Column(name = "is_root")
    private Boolean isRoot;
    
    @Column(name = "is_batch")
    private Boolean isBatch;
    
    @Column(name = "conjunction")
    private Boolean isConjunction;
    
    @Column(name = "is_search")
    private Boolean isSearch;
    
    @OneToMany(cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, 
            mappedBy = "query")
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<QueryParameter> queryParameters;
    
    @OneToMany(cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, 
            mappedBy = "query")
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<QueryPermission> queryPermissions;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cs_query_class_assoc",
        joinColumns = {@JoinColumn(name = "class_id")},
        inverseJoinColumns = {@JoinColumn(name = "query_id")})
    @Fetch(FetchMode.SUBSELECT)
    private Set<CidsClass> cidsClasses;
    
    /** Creates a new instance of Query */
    public Query()
    {
        this.name = null;
        this.description = null;
        this.statement = null;
        this.result = null;
        // init with false to avoid auto-unboxing NPE
        this.isUpdate = false;
        this.isUnion = false;
        this.isRoot = false;
        this.isBatch = false;
        this.isConjunction = false;
        this.isSearch = false;
        this.queryParameters = new HashSet<QueryParameter>();
        this.queryPermissions = new HashSet<QueryPermission>();
        this.cidsClasses = new HashSet<CidsClass>();
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public String getStatement()
    {
        return statement;
    }

    public void setStatement(final String statement)
    {
        this.statement = statement;
    }

    public Integer getResult()
    {
        return result;
    }

    public void setResult(final Integer result)
    {
        this.result = result;
    }

    public Boolean getIsUpdate()
    {
        return isUpdate;
    }

    public void setIsUpdate(final Boolean isUpdate)
    {
        this.isUpdate = isUpdate;
    }

    public Boolean getIsUnion()
    {
        return isUnion;
    }

    public void setIsUnion(final Boolean isUnion)
    {
        this.isUnion = isUnion;
    }

    public Boolean getIsRoot()
    {
        return isRoot;
    }

    public void setIsRoot(final Boolean isRoot)
    {
        this.isRoot = isRoot;
    }

    public Boolean getIsBatch()
    {
        return isBatch;
    }

    public void setIsBatch(final Boolean isBatch)
    {
        this.isBatch = isBatch;
    }

    public Boolean getIsConjunction()
    {
        return isConjunction;
    }

    public void setIsConjunction(final Boolean isConjunction)
    {
        this.isConjunction = isConjunction;
    }

    public Boolean getIsSearch()
    {
        return isSearch;
    }

    public void setIsSearch(final Boolean isSearch)
    {
        this.isSearch = isSearch;
    }

    public Set<QueryParameter> getQueryParameters()
    {
        return queryParameters;
    }

    public void setQueryParameters(final Set<QueryParameter> queryParameters)
    {
        this.queryParameters = queryParameters;
    }

    public Set<QueryPermission> getQueryPermissions()
    {
        return queryPermissions;
    }

    public void setQueryPermissions(final Set<QueryPermission> queryPermissions)
    {
        this.queryPermissions = queryPermissions;
    }

    public Set<CidsClass> getCidsClasses()
    {
        return cidsClasses;
    }

    public void setCidsClasses(final Set<CidsClass> cidsClasses)
    {
        this.cidsClasses = cidsClasses;
    }
    
    public String toString()
    {
        return getName() + "(" + getId() + ")";
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