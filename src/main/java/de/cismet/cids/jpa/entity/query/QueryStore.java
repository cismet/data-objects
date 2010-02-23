/*
 * QueryStore.java
 *
 * Created on 12. September 2007, 14:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.entity.query;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.permission.QueryStorePermission;
import de.cismet.cids.jpa.entity.user.User;
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
import javax.persistence.ManyToOne;
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
@Table(name = "cs_query_store")
public class QueryStore extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_query_store_sequence",
            sequenceName = "cs_query_store_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_query_store_sequence")
    @Column(name = "id")
    private Integer id;
 
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private User user;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "file_name")
    private String filename;
    
    @OneToMany(cascade = CascadeType.ALL, 
            fetch = FetchType.EAGER, 
            mappedBy = "queryStore")
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<QueryStorePermission> queryStorePermissions;
    
    /** Creates a new instance of QueryStore */
    public QueryStore()
    {
        this.user = null;
        this.name = null;
        this.filename = null;
        this.queryStorePermissions = new HashSet<QueryStorePermission>();
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(final User user)
    {
        this.user = user;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(final String filename)
    {
        this.filename = filename;
    }

    public Set<QueryStorePermission> getQueryStorePermissions()
    {
        return queryStorePermissions;
    }

    public void setQueryStorePermissions(final Set<QueryStorePermission> 
            queryStorePermissions)
    {
        this.queryStorePermissions = queryStorePermissions;
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