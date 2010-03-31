/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.cidsclass.JavaClass;

/**
 * encapsulates methods related to user management.
 *
 * @version  $Revision$, $Date$
 */

public interface JavaClassService {

    //~ Methods ----------------------------------------------------------------

    /**
     * special impl to remove all references before deleting the javaclass. will not be handled by mapping as a
     * javaclass does not not know about all classes, attributes and types that hold a reference to it. it would be more
     * complicated to "tell" a javaclass all places where it is referenced and would only result in performance loss.
     *
     * @param  jc  DOCUMENT ME!
     */
    void deleteJavaClass(final JavaClass jc);

    /**
     * DOCUMENT ME!
     *
     * @param   jc  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean contains(final JavaClass jc);

    /**
     * DOCUMENT ME!
     *
     * @param   qualifier  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JavaClass getJavaClass(final String qualifier);
}
