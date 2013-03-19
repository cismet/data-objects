/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.container;

import de.cismet.tools.Equals;

/**
 * DOCUMENT ME!
 *
 * @author   Martin Scholl
 * @version  1.0
 */
// we simply don't want this class to be instantiated, it is the base class for statements
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
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

    @Override
    public boolean equals(final Object obj) {
        return Equals.beanDeepEqual(this, obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = (83 * hash) + (this.pedantic ? 1 : 0);
        hash = (83 * hash) + ((this.warning == null) ? 0 : this.warning.hashCode());

        return hash;
    }
}
