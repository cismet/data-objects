/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import com.mchange.v1.util.ClosableResource;

import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.Type;

import de.cismet.cids.util.Cancelable;
import de.cismet.cids.util.ProgressObservable;

import java.sql.SQLException;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public interface MetaService extends ClosableResource, ProgressObservable, Cancelable {

    //~ Static fields/initializers ---------------------------------------------

    String CS_ATTR_MAP_TABLE = "cs_all_attr_mapping";
    String CS_ATTR_STRING_TABLE = "cs_attr_string";
    String CS_CLASS_TABLE = "cs_class";
    String CS_ATTR_TABLE = "cs_attr";
    String CS_TYPE_TABLE = "cs_type";

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   type  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    void adjustTypeClassId(final Type type) throws SQLException;
    /**
     * DOCUMENT ME!
     *
     * @param   attr  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    void adjustAttrForeignKey(final Attribute attr) throws SQLException;
    /**
     * DOCUMENT ME!
     *
     * @param   cidsClass  DOCUMENT ME!
     *
     * @throws  SQLException  DOCUMENT ME!
     */
    void refreshIndex(final CidsClass cidsClass) throws SQLException;
}
