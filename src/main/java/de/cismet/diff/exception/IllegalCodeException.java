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
public class IllegalCodeException extends Exception {

    //~ Instance fields --------------------------------------------------------

    private final transient String code;

    //~ Constructors -----------------------------------------------------------

    /**
     * Constructs an instance of <code>IllegalCodeException</code> with the specified detail message and the specified
     * illegal code.
     *
     * @param  message  msg the detail message.
     * @param  code     the code that is illegal
     */
    public IllegalCodeException(final String message, final String code) {
        super(message);
        this.code = code;
    }

    /**
     * Creates a new IllegalCodeException object.
     *
     * @param  message  DOCUMENT ME!
     * @param  code     DOCUMENT ME!
     * @param  cause    DOCUMENT ME!
     */
    public IllegalCodeException(final String message, final String code, final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the illegal code that caused the exception
     */
    public String getCode() {
        return code;
    }
}
