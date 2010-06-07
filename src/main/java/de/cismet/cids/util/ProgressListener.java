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
public interface ProgressListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  steps  DOCUMENT ME!
     */
    void progress(final int steps);

    /**
     * DOCUMENT ME!
     *
     * @param  state  DOCUMENT ME!
     */
    void processingStateChanged(final ProgressState state);

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    final class ProgressState {

        //~ Instance fields ----------------------------------------------------

        private final String state;
        private final int maxSteps;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ProgressState object.
         *
         * @param  state     DOCUMENT ME!
         * @param  maxSteps  DOCUMENT ME!
         */
        public ProgressState(final String state, final int maxSteps) {
            this.state = state;
            this.maxSteps = maxSteps;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getState() {
            return state;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public int getMaxSteps() {
            return maxSteps;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isIndeterminate() {
            return maxSteps < 1;
        }
    }
}
