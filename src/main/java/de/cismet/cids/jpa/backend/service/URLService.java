/*
 * URLService.java
 *
 * Created on 24. January 2008, 15:52
 */

package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.common.URL;
import de.cismet.cids.jpa.entity.common.URLBase;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author mscholl
 * @version 1.0
 */
public interface URLService
{    
    URL getURL(final String url) throws NoResultException;

    List<URL> getURLsLikeURL(final URL url);

    URL storeURL(final URL url);

    List<URL> storeURLs(final List<URL> urls);

    void deleteURL(final URL url);

    void deleteURLs(final List<URL> urls);

    void deleteURLBaseIfUnused(final URLBase urlbase);

    void deleteURLBasesIfUnused(final List<URLBase> urlbases);
}