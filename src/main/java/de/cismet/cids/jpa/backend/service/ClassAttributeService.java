package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.cidsclass.ClassAttribute;
import java.util.List;

/**
 * encapsulates methods related to user management
 * @deprecated
 */
public interface ClassAttributeService
{
    // became obsolete in favor of store(T extends CommonEntity)
    //public ClassAttribute storeClassAttribute(final ClassAttribute a);
    
    // became obsolete in favor of getEntity(Class<T>, int)
    //public ClassAttribute getClassAttribute(final int id);
    
    // became obsolete due to mapping
    //public List<ClassAttribute> getClassAttributes(final int classId);
    
    // became obsolete in favor of getEntity(Class<T>)
    //public List<ClassAttribute> getAllClassAttributes();
    
    // became obsolete in favor of delete(CommonEntity)
    //public void deleteClassAttribute(final ClassAttribute ca);
}