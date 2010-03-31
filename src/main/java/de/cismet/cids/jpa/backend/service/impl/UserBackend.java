/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service.impl;

import de.cismet.cids.jpa.backend.core.PersistenceProvider;
import de.cismet.cids.jpa.backend.service.UserService;
import de.cismet.cids.jpa.entity.user.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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
}
