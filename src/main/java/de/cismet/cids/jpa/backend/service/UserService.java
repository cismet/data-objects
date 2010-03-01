package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.user.User;

/**
 * encapsulates methods related to user management
 */
public interface UserService
{
    User getUser(final String userName, final String password);
}