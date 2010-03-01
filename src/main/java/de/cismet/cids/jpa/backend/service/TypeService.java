package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.cidsclass.Type;
import java.util.List;

/**
 * encapsulates methods related to user management
 */

/** 
 */
public interface TypeService
{
    List getSortedTypes();
    
    boolean stillReferenced(final Type t);
}