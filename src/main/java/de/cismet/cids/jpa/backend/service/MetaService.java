/*
 * MetaService.java
 *
 * Created on 15. Februar 2008, 15:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.backend.service;

import com.mchange.v1.util.ClosableResource;
import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.cidsclass.Type;
import de.cismet.cids.util.Cancelable;
import de.cismet.cids.util.ProgressObservable;
import java.sql.SQLException;

/**
 *
 * @author mscholl
 */
public interface MetaService extends
                                    ClosableResource,
                                    ProgressObservable,
                                    Cancelable
{
    public static final String CS_ATTR_MAP_TABLE = "cs_all_attr_mapping";
    public static final String CS_ATTR_STRING_TABLE = "cs_attr_string";
    public static final String CS_CLASS_TABLE = "cs_class";
    public static final String CS_ATTR_TABLE = "cs_attr";
    public static final String CS_TYPE_TABLE = "cs_type";

    void adjustTypeClassId(final Type type) throws SQLException;
    void adjustAttrForeignKey(final Attribute attr) throws SQLException;
    void refreshIndex(final CidsClass cidsClass) throws SQLException;
}