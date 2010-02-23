package de.cismet.cids.jpa.entity.user;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.common.Domain;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity()
@Table(name = "cs_ug")
public class UserGroup extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_ug_sequence",
            sequenceName = "cs_ug_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_ug_sequence")
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "name")
    private String name;
    
    @Column(name = "descr")
    private String description;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "domain", nullable = false)
    private Domain domain;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cs_ug_membership",
        joinColumns = {@JoinColumn(name = "ug_id")},
        inverseJoinColumns = {@JoinColumn(name = "usr_id")})
    @Fetch(FetchMode.SUBSELECT)
    private Set<User> users;
    
    public UserGroup()
    {
        name = null;
        description = null;
        domain = null;
        users = new HashSet<User>();
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
    
    public Domain getDomain()
    {
        return domain;
    }
    
    public void setDomain(final Domain domain)
    {
        this.domain = domain;
    }
    
    public String toString()
    {
        return getName() + "(" + getId() + ")";
    }

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers(final Set<User> users)
    {
        this.users = users;
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