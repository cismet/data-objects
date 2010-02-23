package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import java.util.List;

/**
 * encapsulates methods related to user management
 * @deprecated
 */

public interface AttributeService
{
    // TODO: became obsolete in favor of store(T extends CommonEntity)
    //public Attribute storeAttribute(final Attribute a);
    
    // TODO: became obsolete in favor of getEntity(Class<T>, int)
    //public Attribute getAttribute(final int id);
    
    // became obsolete due to mapping
    //public List<Attribute> getAttributes(final int classId);
   
    // TODO: became obsolete in favor of getEntity(Class<T>)
    //public List<Attribute> getAllAttributes();
    
    // TODO: became obsolete in favor of delete(CommonEntity)
    //public void deleteAttribute(final Attribute attr);
    
    // TODO: became obsolete in favor of contains(Class<T>, String)
    //public boolean contains(final Attribute a);
}