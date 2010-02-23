/*
 * AbstractPermission.java
 *
 * Created on 10. Januar 2007, 14:33
 *
 */

package de.cismet.cids.jpa.entity.permission;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.user.UserGroup;
import java.io.Serializable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author $Author: mscholl $
 * @version $Revision: 1.3 $
 * tag $Name:  $
 * date $Date: 2008/04/23 10:24:26 $
 */
@MappedSuperclass
public abstract class AbstractPermission extends CommonEntity implements 
        Serializable
{
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "ug_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    protected UserGroup userGroup;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "permission", nullable = false)
    @Fetch(FetchMode.SELECT)
    protected Permission permission;
    
    public AbstractPermission()
    {
        userGroup = null;
        permission = null;
    }

    public UserGroup getUserGroup()
    {
        return userGroup;
    }
    
    public void setUserGroup(final UserGroup userGroup)
    {
        this.userGroup = userGroup;
    }
    
    public Permission getPermission()
    {
        return permission;
    }
   
    public void setPermission(final Permission permission)
    {
        this.permission = permission;
    }
}