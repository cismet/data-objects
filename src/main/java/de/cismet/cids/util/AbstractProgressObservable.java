/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public abstract class AbstractProgressObservable implements ProgressObservable {

    //~ Instance fields --------------------------------------------------------

    private final transient Set<ProgressListener> listeners;
    private transient ProgressListener.ProgressState state;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractProgressObservable object.
     */
    public AbstractProgressObservable() {
        this.listeners = new HashSet<ProgressListener>();
        this.state = new ProgressListener.ProgressState(null, 0);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void addProgressListener(final ProgressListener pl) {
        if (pl == null) {
            return;
        }
        synchronized (listeners) {
            listeners.add(pl);
        }
    }

    @Override
    public void removeProgressListener(final ProgressListener pl) {
        if (pl == null) {
            return;
        }
        if (listeners.contains(pl)) {
            synchronized (listeners) {
                listeners.remove(pl);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  state  DOCUMENT ME!
     */
    protected void fireStateChanged(final ProgressListener.ProgressState state) {
        if (state == null) {
            return;
        }
        this.state = state;
        final Iterator<ProgressListener> it;
        synchronized (listeners) {
            it = new HashSet<ProgressListener>(listeners).iterator();
        }
        while (it.hasNext()) {
            it.next().processingStateChanged(state);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  steps  DOCUMENT ME!
     */
    protected void fireProgressed(final int steps) {
        // suppresses changes until state is not indeterminate anymore
        if ((steps < 0) || state.isIndeterminate()) {
            return;
        }
        final Iterator<ProgressListener> it;
        synchronized (listeners) {
            it = new HashSet<ProgressListener>(listeners).iterator();
        }
        while (it.hasNext()) {
            it.next().progress(steps);
        }
    }
}
