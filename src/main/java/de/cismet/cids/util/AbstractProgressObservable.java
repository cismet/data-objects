/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.cids.util;

import java.util.HashSet;

/**
 *
 * @author mscholl
 */
public abstract class AbstractProgressObservable implements ProgressObservable
{
    private final HashSet<ProgressListener> listeners;
    private ProgressListener.ProgressState state;

    public AbstractProgressObservable()
    {
        this.listeners = new HashSet<ProgressListener>();
        this.state = new ProgressListener.ProgressState(null, 0);
    }

    public void addProgressListener(final ProgressListener pl)
    {
        if(pl == null)
            return;
        synchronized(listeners)
        {
            listeners.add(pl);
        }
    }

    public void removeProgressListener(final ProgressListener pl)
    {
        if(pl == null)
            return;
        if(listeners.contains(pl))
        {
            synchronized(listeners)
            {
                listeners.remove(pl);
            }
        }
    }

    protected void fireStateChanged(final ProgressListener.ProgressState state)
    {
        if(state == null)
            return;
        this.state = state;
        final Thread notifier = new Thread(new Runnable()
        {
            public void run()
            {
                synchronized(listeners)
                {
                    for(final ProgressListener pl : listeners)
                        pl.processingStateChanged(state);
                }
            }
        });
        notifier.start();
    }

    protected void fireProgressed(final int steps)
    {
        // suppresses changes until state is not indeterminate anymore
        if(steps < 0 || state.isIndeterminate())
            return;
        final Thread notifier = new Thread(new Runnable()
        {
            public void run()
            {
                synchronized(listeners)
                {
                    for(final ProgressListener pl : listeners)
                        pl.progress(steps);
                }
            }
        });
        notifier.start();
    }
}