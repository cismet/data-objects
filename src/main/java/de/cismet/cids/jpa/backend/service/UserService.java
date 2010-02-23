package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.user.User;
import de.cismet.cids.jpa.entity.user.UserGroup;
import java.util.List;

/**
 * encapsulates methods related to user management
 */
public interface UserService
{
    public User getUser(final String userName, final String password);
    
    // became obsolete in favor of store(T extends CommonEntity)
    //public User storeUser(final User user);
    
    // became obsolete in favor of store(T extends CommonEntity)
    //public UserGroup storeUserGroup(final UserGroup userGroup);
    
    //public UserGroup getUserGroup(final String userGroupName);
    
    // became obsolete in favor of getEntity(Class<T>)
    //public List<User> getAllUsers();
    
    // became obsolete in favor of getEntity(Class<T>)
    //public List<UserGroup> getAllUserGroups();
    
    // became obsolete in favor of delete(CommonEntity)
    //public void deleteUser(final User user);
    
    // became obsolete in favor of delete(CommonEntity)
    //public void deleteUserGroup(final UserGroup ug);
}