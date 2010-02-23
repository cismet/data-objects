package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.Type;
import java.util.List;

/**
 * encapsulates methods related to user management
 */

/** 
 */
public interface TypeService
{
    // became obsolete in favor of store(T extends CommonEntity)
    //public Type storeType(Type t);
    
    // became obsolete in favor of getEntity(Class<T>, String)
    //public Type getType(String typeName);
    
    // became obsolete in favor of getEntity(Class<T>, int)
    //public Type getType(int id);
    
    // became obsolete in favor of getEntity(Class<T>)
    //public List<Type> getAllTypes();
    
    // became obsolete in favor of delete(CommonEntity)
    //public void deleteType(Type t);
    
    // became obsolete due to mapping
    //public CidsClass getCidsClass4Type(Type t);
    
    // became obsolete in favor of contains(Class<T>, String)
    //public boolean contains(Type t);
    
    public List getSortedTypes();
    
    public boolean stillReferenced(final Type t);
}