/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.cidsclass.Icon;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  1.3
 */
public interface IconService {

    //~ Methods ----------------------------------------------------------------

    /**
     * special implementation because icon id of owning cidsclasses has to be set to null. as it is not necessary for an
     * icon to know which classes hold a reference to it, this will be solved with the impl of this method instead of
     * using mapping and cascading.
     *
     * @param  i  DOCUMENT ME!
     */
    void deleteIcon(final Icon i);

    /**
     * DOCUMENT ME!
     *
     * @param   icon  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean stillReferenced(final Icon icon);
}
