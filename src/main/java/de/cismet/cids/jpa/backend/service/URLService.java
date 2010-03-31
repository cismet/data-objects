/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.common.URL;
import de.cismet.cids.jpa.entity.common.URLBase;

import java.util.List;

import javax.persistence.NoResultException;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  1.0
 */
public interface URLService {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<URL> getURLsLikeURL(final URL url);

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    URL storeURL(final URL url);

    /**
     * DOCUMENT ME!
     *
     * @param   urls  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<URL> storeURLs(final List<URL> urls);

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    void deleteURL(final URL url);

    /**
     * DOCUMENT ME!
     *
     * @param  urls  DOCUMENT ME!
     */
    void deleteURLs(final List<URL> urls);

    /**
     * DOCUMENT ME!
     *
     * @param  urlbase  DOCUMENT ME!
     */
    void deleteURLBaseIfUnused(final URLBase urlbase);

    /**
     * DOCUMENT ME!
     *
     * @param  urlbases  DOCUMENT ME!
     */
    void deleteURLBasesIfUnused(final List<URLBase> urlbases);
}
