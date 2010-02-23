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
    // became obsolete in favor of getEntity(Class<T>)
    //public List<URL> getAllURLs();
    
    // became obsolete in favor of getEntity(Class<T>, int)
    //public URL getURL(final int id);
    
    public URL getURL(final String url) throws NoResultException;

    public List<URL> getURLsLikeURL(final URL url);

    public URL storeURL(final URL url);

    public List<URL> storeURLs(final List<URL> urls);

    public void deleteURL(final URL url);

    public void deleteURLs(final List<URL> urls);

    public void deleteURLBaseIfUnused(final URLBase urlbase);

    public void deleteURLBasesIfUnused(final List<URLBase> urlbases);
}