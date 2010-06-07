/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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
 * DOCUMENT ME!
 *
 * @author   cschmidt
 * @version  $Revision$, $Date$
 */
public interface IndexProgressListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    void dispose();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JLabel getLblInsertIntoDb();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JProgressBar getProgInsertIntoDb();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JProgressBar getProgBarAttr();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JProgressBar getProgBarClass();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JLabel getLblAttribute();
}
