/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.util;

import de.cismet.diff.container.Action;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Used as event queue where to put <code>Action</code> instance representing the actions a user has performed that
 * affect the "cs_class" and/or "cs_attr" entries.
 *
 * @author   Martin Scholl
 * @version  1.0 2007-03-09
 */
public class ProgressionQueue implements Cloneable {

    //~ Instance fields --------------------------------------------------------

    private final transient LinkedList<Action> actionList;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of <code>ProgressionQueue.</code>
     */
    public ProgressionQueue() {
        actionList = new LinkedList<Action>();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  action  DOCUMENT ME!
     */
    public void putAction(final Action action) {
        actionList.addLast(action);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   action  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Action removeAction(final Action action) {
        return actionList.remove(action) ? action : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Action> getActionList() {
        return actionList;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Action[] getActionArray() {
        return actionList.toArray(new Action[actionList.size()]);
    }

    /**
     * DOCUMENT ME!
     */
    public void clearActions() {
        actionList.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Iterator<Action> getIterator() {
        return actionList.listIterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   actionDescription  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<Action> getActionList(final String actionDescription) {
        final LinkedList<Action> ret = new LinkedList<Action>();
        for (final Action a : actionList) {
            if (a.getAction().equals(actionDescription)) {
                ret.addLast(a);
            }
        }
        return (ret.isEmpty()) ? null : ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   actionDescription  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Action[] getActionArray(final String actionDescription) {
        final List<Action> actions = getActionList(actionDescription);
        if (actions == null) {
            return null;
        }
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public ProgressionQueue clone() {
        final ProgressionQueue clone = new ProgressionQueue();
        for (final Action action : actionList) {
            clone.putAction(action);
        }
        return clone;
    }
}
