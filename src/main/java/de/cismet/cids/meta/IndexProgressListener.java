/*
 * IndexProgressListener.java
 *
 * Created on 27. Februar 2008, 10:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.meta;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author cschmidt
 */
public interface IndexProgressListener {
    
    public void dispose();
    
    public JLabel getLblInsertIntoDb();
    
    public JProgressBar getProgInsertIntoDb();
    
    public JProgressBar getProgBarAttr();
    
    public JProgressBar getProgBarClass();
    
    public JLabel getLblAttribute();
}
