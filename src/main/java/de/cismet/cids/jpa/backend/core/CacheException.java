/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.core;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class CacheException extends Exception {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>CacheException</code> without detail message.
     */
    public CacheException() {
    }

    /**
     * Constructs an instance of <code>CacheException</code> with the specified detail message.
     *
     * @param  msg  the detail message.
     */
    public CacheException(final String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>CacheException</code> with the specified detail message and the specified cause.
     *
     * @param  msg    the detail message.
     * @param  cause  the exception cause
     */
    public CacheException(final String msg, final Throwable cause) {
        super(msg, cause);
    }
}
