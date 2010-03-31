/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.util;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public interface ProgressObservable {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  pl  DOCUMENT ME!
     */
    void addProgressListener(final ProgressListener pl);
    /**
     * DOCUMENT ME!
     *
     * @param  pl  DOCUMENT ME!
     */
    void removeProgressListener(final ProgressListener pl);
}
