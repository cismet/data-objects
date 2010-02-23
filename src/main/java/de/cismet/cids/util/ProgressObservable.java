package de.cismet.cids.util;

/**
 *
 * @author mscholl
 */
public interface ProgressObservable
{
    public void addProgressListener(final ProgressListener pl);
    public void removeProgressListener(final ProgressListener pl);
}
