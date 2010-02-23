/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.util;

/**
 *
 * @author mscholl
 */
public interface ProgressListener
{
    public void progress(final int steps);
    public void processingStateChanged(final ProgressState state);

    public static final class ProgressState
    {
        private String state;
        private int maxSteps;

        public ProgressState(final String state, final int maxSteps)
        {
            this.state = state;
            this.maxSteps = maxSteps;
        }

        public String getState()
        {
            return state;
        }

        public int getMaxSteps()
        {
            return maxSteps;
        }

        public boolean isIndeterminate()
        {
            return maxSteps < 1;
        }
    }
}
