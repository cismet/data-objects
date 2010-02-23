/*
 * DefaultInspectionResult.java
 *
 * Created on 13. September 2007, 16:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.maintenance.util;

import de.cismet.cids.maintenance.InspectionResult;
import de.cismet.cids.maintenance.container.Row;
import de.cismet.diff.container.Table;
import java.util.Vector;

/**
 *
 * @author mscholl
 */
public class DefaultInspectionResult implements InspectionResult
{
    private Table table;
    private String message;
    private Vector<Row> rows;
    private int code;
    private int erroneousColumnCount;
    
    /** Creates a new instance of DefaultInspectionResult */
    public DefaultInspectionResult()
    {
    }

    public Table getTable()
    {
        return table;
    }

    public void setTable(Table table)
    {
        this.table = table;
    }

    public String getResultMessage()
    {
        return message;
    }

    public void setResultMessage(String message)
    {
        this.message = message;
    }

    public Vector<Row> getErroneousRows()
    {
        return rows;
    }

    public void setErroneousRows(Vector<Row> rows)
    {
        this.rows = rows;
    }

    public int getMessageCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }
    
    public int getErroneousColumnCount()
    {
        return erroneousColumnCount;
    }

    public void setErroneousColumnCount(int erroneousColumnCount)
    {
        this.erroneousColumnCount = erroneousColumnCount;
    }

    public int getErroneousRowCount()
    {
        return rows == null ? 0 : rows.size();
    }
}