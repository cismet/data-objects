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
public class UnsupportedActionException extends java.lang.Exception {

    //~ Instance fields --------------------------------------------------------

    private final transient String desiredAction;

    //~ Constructors -----------------------------------------------------------

    /**
     * Constructs an instance of <code>UnsupportedActionException</code> with the specified detail message and the name
     * of the desired action.
     *
     * @param  msg            the detail message.
     * @param  desiredAction  the action that was desired to be performed
     */
    public UnsupportedActionException(final String msg, final String desiredAction) {
        super(msg);
        this.desiredAction = desiredAction;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the name of the action that was desired to be performed
     */
    public String getDesiredAction() {
        return desiredAction;
    }
}
