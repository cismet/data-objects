/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.exception;

/**
 * DOCUMENT ME!
 *
 * @author   Martin Scholl
 * @version  1.0 2007-03-08
 */
public class TableLoaderException extends Exception {

    //~ Constructors -----------------------------------------------------------

    /**
     * Constructs an instance of <code>TableLoaderException</code> with the specified detail message and the specified
     * cause.
     *
     * @param  name   msg the detail message
     * @param  cause  the cause of the exception
     */
    public TableLoaderException(final String name, final Throwable cause) {
        super(name, cause);
    }
}
