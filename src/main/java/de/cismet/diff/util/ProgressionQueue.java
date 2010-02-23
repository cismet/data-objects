/*
 * ProgressionQueue.java
 *
 * Created on 1. März 2007, 11:19
 */

package de.cismet.diff.util;

import de.cismet.diff.container.Action;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Used as event queue where to put <code>Action</code> instance representing
 * the actions a user has performed that affect the "cs_class" and/or "cs_attr" 
 * entries.
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-09
 */
public class ProgressionQueue implements Cloneable
{
  private LinkedList<Action> actionList;
  
  /**
   * Creates a new instance of <code>ProgressionQueue</code>
   */
  public ProgressionQueue()
  {
    actionList = new LinkedList<Action>();
  }
  
  public void putAction(Action a)
  {
    actionList.addLast(a);
  }
  
  public Action removeAction(Action a)
  {
    return actionList.remove(a) ? a : null;
  }
  
  public LinkedList<Action> getActionList()
  {
    return actionList;
  }
  
  public Action[] getActionArray()
  {
    Action[] actions = new Action[actionList.size()];
    return actionList.toArray(actions);
  }
  
  public void clearActions()
  {
    actionList.clear();
  }
  
  public Iterator<Action> getIterator()
  {
    return actionList.listIterator();
  }
  
  public LinkedList<Action> getActionList(String actionDescription)
  {
    LinkedList<Action> ret = new LinkedList<Action>();
    for(Action a : actionList)
    {
      if(a.getAction().equals(actionDescription))
      {
        ret.addLast(a);
      }
    }
    return ret.size() == 0 ? null : ret;
  }
  
  public Action[] getActionArray(String actionDescription)
  {
    LinkedList<Action> actions = getActionList(actionDescription);
    if(actions == null)
      return null;
    Action[] ret = new Action[actions.size()];
    return actions.toArray(ret);
  }
  
  public ProgressionQueue clone()
  {
    ProgressionQueue clone = new ProgressionQueue();
    for(Action a : actionList)
      clone.putAction(a);
    return clone;
  }
}
