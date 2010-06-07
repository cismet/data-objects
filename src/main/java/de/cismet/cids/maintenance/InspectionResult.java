/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.maintenance;

import java.util.List;

import de.cismet.cids.maintenance.container.Row;

import de.cismet.diff.container.Table;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public interface InspectionResult {

    //~ Instance fields --------------------------------------------------------

    int CODE_NO_KEYS = 0;
    int CODE_ONE_KEY = 1;
    int CODE_ONE_KEY_ERROR = 2;
    int CODE_MULTIPLE_KEYS = 3;
    int CODE_MULTIPLE_KEYS_ERROR = 4;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Table getTable();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<Row> getErroneousRows();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getResultMessage();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getMessageCode();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getErroneousColumnCount();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getErroneousRowCount();
}
