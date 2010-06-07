/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import java.util.List;

import de.cismet.cids.jpa.entity.cidsclass.Type;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public interface TypeService {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List getSortedTypes();

    /**
     * DOCUMENT ME!
     *
     * @param   t  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean stillReferenced(final Type t);
}
