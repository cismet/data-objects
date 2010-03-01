package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.cidsclass.Icon;

/**
 *
 * @author $Author: mscholl $
 * @version $Revision: 1.3 $
 * tag $Name:  $
 * date $Date: 2008/03/10 15:24:32 $
 *
 * encapsulates methods related to user management
 */
public interface IconService
{ 
    // special implementation because icon id of owning cidsclasses has to be
    // set to null. as it is not necessary for an icon to know which classes
    // hold a reference to it, this will be solved with the impl of this method
    // instead of using mapping and cascading.
    void deleteIcon(final Icon i);
    
    boolean stillReferenced(final Icon icon);
}