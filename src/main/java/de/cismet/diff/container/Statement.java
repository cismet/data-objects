/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.container;

/**
 * DOCUMENT ME!
 *
 * @author   Martin Scholl
 * @version  1.0
 */
public abstract class Statement {

    //~ Instance fields --------------------------------------------------------

    protected transient boolean pedantic;
    protected String warning;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>Statement.</code>
     *
     * @param  warning   DOCUMENT ME!
     * @param  pedantic  DOCUMENT ME!
     */
    public Statement(final String warning, final boolean pedantic) {
        this.warning = warning;
        this.pedantic = pedantic;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getWarning() {
        return warning;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  warning  DOCUMENT ME!
     */
    public void setWarning(final String warning) {
        this.warning = warning;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isPedantic() {
        return pedantic;
    }
}
