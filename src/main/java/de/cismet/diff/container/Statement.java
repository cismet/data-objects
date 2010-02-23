/*
 * Statement.java
 *
 * Created on 16. März 2007, 10:17
 */

package de.cismet.diff.container;

/**
 * @author Martin Scholl
 * @version 1.0
 */
public abstract class Statement
{
  protected String warning;
  protected boolean pedantic;

  /**
   * Creates a new instance of <code>Statement</code>
   */
  public Statement(String warning, boolean pedantic)
  {
    this.warning = warning;
    this.pedantic = pedantic;
  }

  public String getWarning()
  {
    return warning;
  }

  public void setWarning(String warning)
  {
    this.warning = warning;
  }
  
  public boolean isPedantic()
  {
    return pedantic;
  }
}
