/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.container;

import java.util.Arrays;
import java.util.ResourceBundle;

import de.cismet.diff.DiffAccessor;

import de.cismet.diff.exception.UnsupportedActionException;

/**
 * Provides a simple container to hold actions that has been performed and the arguments that are involved. The
 * supported action descriptions are provided by public constants, that shall be used to instantiate this class.
 * Currently there is only one action supported.
 *
 * @author   Martin Scholl
 * @version  1.0 2007-03-09
 */
public class Action {

    //~ Static fields/initializers ---------------------------------------------

    public static final String DROP_ACTION = "drop"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private final transient String[] args;
    private final transient String action;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>Action</code><br/>
     * <br/>
     * ACTION -> Arguments<br/>
     * DROP_ACTION -> tablename.
     *
     * @param   actionDescription  DOCUMENT ME!
     * @param   args               DOCUMENT ME!
     *
     * @throws  UnsupportedActionException  DOCUMENT ME!
     */
    public Action(final String actionDescription, final String... args) throws UnsupportedActionException {
        final ResourceBundle exceptionBundle = ResourceBundle.getBundle(DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
        if (!actionDescription.equals(DROP_ACTION)) {
            throw new UnsupportedActionException(
                exceptionBundle.getString(
                    DiffAccessor.UNSUPPORTED_ACTION_EXCEPTION_ACTION_NOT_SUPPORTED),
                actionDescription);
        }
        this.action = actionDescription;
        this.args = args;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getAction() {
        return action;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String[] getArgs() {
        return Arrays.copyOf(args, args.length);
    }
}
