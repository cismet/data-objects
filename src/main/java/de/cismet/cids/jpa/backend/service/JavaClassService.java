package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.cidsclass.JavaClass;
import java.util.List;

/**
 * encapsulates methods related to user management
 */

public interface JavaClassService
{
    // became obsolete in favor of store(T extends CommonEntity)
    //public JavaClass storeJavaClass(final JavaClass c);
    
    // became obsolete in favor of getEntity(Class<T>, int)
    //public JavaClass getJavaClass(final int id);
    
    // became obsolete in favor of getEntity(Class<T>)
    //public List<JavaClass> getAllJavaClasses();
    
    // special impl to remove all references before deleting the javaclass.
    // will not be handled by mapping as a javaclass does not not know about
    // all classes, attributes and types that hold a reference to it. it would
    // be more complicated to "tell" a javaclass all places where it is 
    // referenced and would only result in performance loss.
    public void deleteJavaClass(final JavaClass jc);
    
    public boolean contains(final JavaClass jc);
    
    public JavaClass getJavaClass(final String qualifier);
    
}