/*
 * TableLoaderException.java
 *
 * Created on 20. Februar 2007, 11:26
 */

package de.cismet.diff.exception;

/**
 * @author Martin Scholl
 * @version 1.0  2007-03-08
 */
public class TableLoaderException extends Exception
{
  
  /**
   * Constructs an instance of <code>TableLoaderException</code> with the
   * specified detail message and the specified cause.
   *
   * @param msg the detail message
   * @param cause the cause of the exception
   */
  public TableLoaderException(String name, Throwable cause)
  {
    super(name, cause);
  }
}
