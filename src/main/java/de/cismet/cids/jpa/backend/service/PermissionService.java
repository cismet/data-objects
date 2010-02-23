/*
 * PermissonService.java
 *
 * Created on 24. Janurary 2008, 15:14
 */

package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.permission.AttributePermission;
import de.cismet.cids.jpa.entity.permission.ClassPermission;
import de.cismet.cids.jpa.entity.permission.Permission;

/**
 *
 * @author mscholl
 * @version 1.0
 * @deprecated
 */
public interface PermissionService
{
    // became obsolete in favor of store(T extends CommonEntity)
    //public Permission storePermission(final Permission p);
    
    // became obsolete in favor of store(T extends CommonEntity)
    //public ClassPermission storeClassPermission(final ClassPermission p);
    
    // became obsolete in favor of store(T extends CommonEntity)
    //public AttributePermission storeAttributePermission(final 
            //AttributePermission p);
}