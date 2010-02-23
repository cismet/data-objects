/*
 * IllegalCodeException.java
 *
 * Created on 22. Februar 2007, 16:21
 */

package de.cismet.diff.exception;

/**
 * @author Martin Scholl
 * @version 1.0  2007-03-08
 */
public class IllegalCodeException extends Exception
{
  private String code;
  
  /**
   * Constructs an instance of <code>IllegalCodeException</code> with the
   * specified detail message and the specified illegal code.
   *
   * @param msg the detail message.
   * @param code the code that is illegal
   */
  public IllegalCodeException(String message, String code)
  {
    super(message);
    this.code = code;
  }
  
  /**
   * @return the illegal code that caused the exception
   */
  public String getCode()
  {
    return code;
  }
}
