/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.common;

import java.util.Set;

import de.cismet.cids.jpa.entity.permission.AbstractPermission;
import de.cismet.cids.jpa.entity.permission.Policy;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public interface PermissionAwareEntity {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Set<? extends AbstractPermission> getPermissions();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Policy getPolicy();
}
