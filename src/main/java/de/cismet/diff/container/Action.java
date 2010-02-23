/*
 * Action.java
 *
 * Created on 1. März 2007, 11:20
 */

package de.cismet.diff.container;

import de.cismet.diff.DiffAccessor;
import de.cismet.diff.exception.UnsupportedActionException;
import java.util.ResourceBundle;

/**
 * Provides a simple container to hold actions that has been performed and the
 * arguments that are involved. The supported action descriptions are provided
 * by public constants, that shall be used to instantiate this class.
 * Currently there is only one action supported.
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-09
 */
public class Action
{
  public static final String DROP_ACTION = "drop";
  
  private String action;
  private String[] args;
  
  /** 
   * Creates a new instance of <code>Action</code><br/>
   * <br/>
   * ACTION      ->   Arguments<br/>
   * DROP_ACTION ->   tablename
   */
  public Action(String actionDescription, String ... args) throws
      UnsupportedActionException
  {
    ResourceBundle exceptionBundle = ResourceBundle.getBundle(
        DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
    if(!actionDescription.equals(DROP_ACTION))
      throw new UnsupportedActionException(exceptionBundle.getString(
          DiffAccessor.UNSUPPORTED_ACTION_EXCEPTION_ACTION_NOT_SUPPORTED),
          actionDescription);
    this.action = actionDescription;
    this.args = args;
  }

  public String getAction()
  {
    return action;
  }

  public String[] getArgs()
  {
    return args;
  }
}
