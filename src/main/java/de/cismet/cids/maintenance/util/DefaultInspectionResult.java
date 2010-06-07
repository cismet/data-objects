/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DefaultInspectionResult.java
 *
 * Created on 13. September 2007, 16:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.cids.maintenance.util;

import java.util.List;

import de.cismet.cids.maintenance.InspectionResult;
import de.cismet.cids.maintenance.container.Row;

import de.cismet.diff.container.Table;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class DefaultInspectionResult implements InspectionResult {

    //~ Instance fields --------------------------------------------------------

    private transient Table table;
    private transient String message;
    private transient List<Row> rows;
    private transient int code;
    private transient int erroneousColumnCount;

    //~ Methods ----------------------------------------------------------------

    @Override
    public Table getTable() {
        return table;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  table  DOCUMENT ME!
     */
    public void setTable(final Table table) {
        this.table = table;
    }

    @Override
    public String getResultMessage() {
        return message;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  message  DOCUMENT ME!
     */
    public void setResultMessage(final String message) {
        this.message = message;
    }

    @Override
    public List<Row> getErroneousRows() {
        return rows;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  rows  DOCUMENT ME!
     */
    public void setErroneousRows(final List<Row> rows) {
        this.rows = rows;
    }

    @Override
    public int getMessageCode() {
        return code;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  code  DOCUMENT ME!
     */
    public void setCode(final int code) {
        this.code = code;
    }

    @Override
    public int getErroneousColumnCount() {
        return erroneousColumnCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  erroneousColumnCount  DOCUMENT ME!
     */
    public void setErroneousColumnCount(final int erroneousColumnCount) {
        this.erroneousColumnCount = erroneousColumnCount;
    }

    @Override
    public int getErroneousRowCount() {
        return (rows == null) ? 0 : rows.size();
    }
}
