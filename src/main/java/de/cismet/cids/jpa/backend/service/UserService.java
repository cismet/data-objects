/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import java.util.List;

import de.cismet.cids.jpa.entity.permission.AbstractPermission;
import de.cismet.cids.jpa.entity.user.User;
import de.cismet.cids.jpa.entity.user.UserGroup;

/**
 * defines methods related to user management.
 *
 * @version  1.5
 */
public interface UserService {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  ug  DOCUMENT ME!
     */
    void delete(final UserGroup ug);

    /**
     * Atomically delete a user and all the relationships.
     *
     * @param  user  DOCUMENT ME!
     */
    void delete(final User user);

    /**
     * Removes the membership of the user from the given usergroup.
     *
     * @param  user  the user whose membership shall be revoked from the group
     * @param  ug    the usergroup the user's membership shall be removed from
     */
    void removeMembership(final User user, final UserGroup ug);

    /**
     * DOCUMENT ME!
     *
     * @param   original  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    UserGroup copy(final UserGroup original);

    /**
     * DOCUMENT ME!
     *
     * @param   original  DOCUMENT ME!
     * @param   newGroup  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    UserGroup copy(final UserGroup original, final UserGroup newGroup);

    /**
     * DOCUMENT ME!
     *
     * @param   userName  DOCUMENT ME!
     * @param   password  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    User getUser(final String userName, final String password);

    /**
     * DOCUMENT ME!
     *
     * @param   <T>       DOCUMENT ME!
     * @param   permType  DOCUMENT ME!
     * @param   ug        DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    <T extends AbstractPermission> List<T> getPermissions(final Class<T> permType, final UserGroup ug);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Integer getLowestUGPrio();
}
