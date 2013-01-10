/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import com.mchange.v1.util.ClosableResource;

import java.util.List;

import de.cismet.cids.jpa.entity.common.Domain;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrEntry;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrKey;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrType.Types;
import de.cismet.cids.jpa.entity.user.User;
import de.cismet.cids.jpa.entity.user.UserGroup;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public interface ConfigAttrService extends ClosableResource {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<ConfigAttrEntry> getEntries(final ConfigAttrKey key);

    /**
     * DOCUMENT ME!
     *
     * @param   key   DOCUMENT ME!
     * @param   type  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<ConfigAttrEntry> getEntries(final ConfigAttrKey key, final Types type);

    /**
     * DOCUMENT ME!
     *
     * @param   type  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<ConfigAttrEntry> getEntries(final Types type);

    /**
     * DOCUMENT ME!
     *
     * @param   dom      DOCUMENT ME!
     * @param   ug       DOCUMENT ME!
     * @param   user     DOCUMENT ME!
     * @param   collect  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<ConfigAttrEntry> getEntries(final Domain dom,
            final UserGroup ug,
            final User user,
            final boolean collect);

    /**
     * DOCUMENT ME!
     *
     * @param   dom              DOCUMENT ME!
     * @param   ug               DOCUMENT ME!
     * @param   user             DOCUMENT ME!
     * @param   localDomainName  DOCUMENT ME!
     * @param   collect          DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<ConfigAttrEntry> getEntries(final Domain dom,
            final UserGroup ug,
            final User user,
            final String localDomainName,
            final boolean collect);

    /**
     * DOCUMENT ME!
     *
     * @param   user             DOCUMENT ME!
     * @param   localDomainName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<ConfigAttrEntry> getEntries(final User user, final String localDomainName);

    /**
     * DOCUMENT ME!
     *
     * @param   user              DOCUMENT ME!
     * @param   localDomainName   DOCUMENT ME!
     * @param   newCollect        DOCUMENT ME!
     * @param   includeConflicts  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<ConfigAttrEntry> getEntries(final User user,
            final String localDomainName,
            final boolean newCollect,
            final boolean includeConflicts);

    /**
     * DOCUMENT ME!
     *
     * @param   user              DOCUMENT ME!
     * @param   includeConflicts  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<Object[]> getEntriesNewCollect(final User user, final boolean includeConflicts);

    /**
     * DOCUMENT ME!
     *
     * @param   entry  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    ConfigAttrEntry storeEntry(final ConfigAttrEntry entry);

    /**
     * DOCUMENT ME!
     *
     * @param   entry  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean contains(final ConfigAttrEntry entry);

    /**
     * DOCUMENT ME!
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean contains(final ConfigAttrKey key);

    /**
     * DOCUMENT ME!
     */
    void cleanAttributeTables();
}
