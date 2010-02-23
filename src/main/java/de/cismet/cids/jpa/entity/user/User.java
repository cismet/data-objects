package de.cismet.cids.jpa.entity.user;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "cs_usr")
public class User extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_usr_sequence",
            sequenceName = "cs_usr_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_usr_sequence")
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "login_name")
    private String loginname;
    
    @Column(name = "administrator")
    private boolean admin;
    
    @Column(name = "password")
    private String password;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_pwd_change")
    private Date lastPwdChange;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "cs_ug_membership",
        joinColumns = {@JoinColumn(name = "usr_id")},
        inverseJoinColumns = {@JoinColumn(name = "ug_id")})
    @Fetch(FetchMode.SUBSELECT)
    private Set<UserGroup> userGroups;
    
    public User()
    {
        loginname = null;
        admin = false;
        password = null;
        lastPwdChange = null;
        userGroups = new HashSet<UserGroup>();
    }
    
    public String getLoginname()
    {
        return loginname;
    }
    
    public void setLoginname(final String loginname)
    {
        this.loginname = loginname;
    }
    
    public boolean isAdmin()
    {
        return admin;
    }
    
    public void setAdmin(final boolean admin)
    {
        this.admin = admin;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(final String password)
    {
        this.password = password;
        this.setLastPwdChange(new Date(System.currentTimeMillis()));
    }
    
    public Date getLastPwdChange()
    {
        return lastPwdChange;
    }
    
    public void setLastPwdChange(final Date lastPwdChange)
    {
        this.lastPwdChange = lastPwdChange;
    }

    public Set<UserGroup> getUserGroups()
    {
        return userGroups;
    }

    public void setUserGroups(final Set<UserGroup> userGroups)
    {
        this.userGroups = userGroups;
    }
    
    public String toString()
    {
        return getLoginname() + "(" + getId() + ")";
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