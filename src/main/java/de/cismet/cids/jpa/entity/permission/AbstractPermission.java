/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  1.3
 */
@MappedSuperclass
public abstract class AbstractPermission extends CommonEntity implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "ug_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    protected UserGroup userGroup;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "permission",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    protected Permission permission;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public UserGroup getUserGroup() {
        return userGroup;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  userGroup  DOCUMENT ME!
     */
    public void setUserGroup(final UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  permission  DOCUMENT ME!
     */
    public void setPermission(final Permission permission) {
        this.permission = permission;
    }
}
