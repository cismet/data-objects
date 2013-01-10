/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import de.cismet.cids.jpa.backend.core.PersistenceProvider;
import de.cismet.cids.jpa.backend.service.UserService;
import de.cismet.cids.jpa.entity.permission.ClassPermission;
import de.cismet.cids.jpa.entity.user.User;
import de.cismet.cids.jpa.entity.user.UserGroup;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class UserBackend implements UserService {

    //~ Instance fields --------------------------------------------------------

    private final transient PersistenceProvider provider;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new UserBackend object.
     *
     * @param  provider  DOCUMENT ME!
     */
    public UserBackend(final PersistenceProvider provider) {
        this.provider = provider;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public User getUser(final String userName, final String password) {
        final EntityManager em = provider.getEntityManager();
        final Query q = em.createQuery("FROM User WHERE login_name = :userName AND password =:password"); // NOI18N
        q.setParameter("userName", userName);                                                             // NOI18N
        q.setParameter("password", password);                                                             // NOI18N
        return (User)q.getSingleResult();
    }

    @Override
    public List<ClassPermission> getClassPermissions(final UserGroup ug) {
        final EntityManager em = provider.getEntityManager();
        final Query q = em.createQuery("FROM ClassPermission WHERE userGroup = :ug"); // NOI18N
        q.setParameter("ug", ug);                                                     // NOI18N

        return q.getResultList();
    }

    @Override
    public Integer getLowestUGPrio() {
        final EntityManager em = provider.getEntityManager();
        final TypedQuery<Integer> q = em.createQuery("SELECT MAX(priority) FROM UserGroup", Integer.class); // NOI18N

        return q.getSingleResult() + 1;
    }
}
