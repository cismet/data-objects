/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import com.mchange.v1.util.ClosableResource;

/**
 * The accessor interface of the backend.
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public interface Backend extends ClassService,
    UserService,
    CatalogService,
    MetaService,
    CommonService,
    ConfigAttrService,
    ClosableResource {
}
