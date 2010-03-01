package de.cismet.cids.jpa.backend.service;

/**
 * encapsulates methods related to user management
 */

/**  */
public interface ClassService extends 
                                    URLService,
                                    TypeService,
                                    JavaClassService,
                                    IconService
{
    // became obsolete in favor of store(T extends CommonEntity)
    //public CidsClass storeClass(final CidsClass c);
    
    // became obsolete in favor of getEntity(Class<T>, int)
    //public CidsClass getClass(final int id);
    
    //public CidsClass getClass(final String name);
    
    // became obsolete in favor of getEntity(Class<T>)
    //public List<CidsClass> getAllClasses();
    
    // became obsolete in favor of delete(CommonEntity)
    //public void deleteClass(final CidsClass c);
    
    // became obsolete in favor of contains(Class<T>, String)
    //public boolean contains(final CidsClass c);
}