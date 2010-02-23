/*
 * UnsupportedActionException.java
 *
 * Created on 1. März 2007, 11:26
 */

package de.cismet.diff.exception;

/**
 *
 * @author Martin Scholl
 * @version 1.0  2007-03-08
 */
public class UnsupportedActionException extends java.lang.Exception
{
  private String desiredAction;
  
  /**
   * Constructs an instance of <code>UnsupportedActionException</code> with the
   * specified detail message and the name of the desired action.
   *
   * @param msg the detail message.
   * @param desiredAction the action that was desired to be performed
   */
  public UnsupportedActionException(String msg, String desiredAction)
  {
    super(msg);
    this.desiredAction = desiredAction;
  }
  
  /**
   * @return the name of the action that was desired to be performed
   */
  public String getDesiredAction()
  {
    return desiredAction;
  }
}