/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.user.User;

/**
 * encapsulates methods related to user management.
 *
 * @version  $Revision$, $Date$
 */
public interface UserService {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   userName  DOCUMENT ME!
     * @param   password  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    User getUser(final String userName, final String password);
}
