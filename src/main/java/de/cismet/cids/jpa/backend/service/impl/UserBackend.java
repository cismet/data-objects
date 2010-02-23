/*
 * UserBackend.java
 *
 * Created on 4. Juli 2006, 09:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.backend.service.impl;

import de.cismet.cids.jpa.backend.core.PersistenceProvider;
import de.cismet.cids.jpa.backend.service.UserService;
import de.cismet.cids.jpa.entity.user.User;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author schlob
 */
public class UserBackend implements UserService
{
    private final transient PersistenceProvider provider;
    
    public UserBackend(final PersistenceProvider provider)
    {
        this.provider = provider;
    }
    
    public User getUser(final String userName, final String password)
    {
        final EntityManager em = provider.getEntityManager();
        final Query q = em.createQuery("FROM User WHERE login_name =" +
                ":userName AND password =:password").
                setParameter("userName", userName).
                setParameter("password", password);
        final User user = (User)q.getSingleResult();
        return user;
    }
}