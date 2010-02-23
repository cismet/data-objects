/*
 * InspectionResult.java
 *
 * Created on 13. September 2007, 16:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.maintenance;

import de.cismet.cids.maintenance.container.Row;
import de.cismet.diff.container.Table;
import java.util.Vector;

/**
 *
 * @author mscholl
 */
public interface InspectionResult
{
    public static final int CODE_NO_KEYS = 0;
    public static final int CODE_ONE_KEY = 1;
    public static final int CODE_ONE_KEY_ERROR = 2;
    public static final int CODE_MULTIPLE_KEYS = 3;
    public static final int CODE_MULTIPLE_KEYS_ERROR = 4;
    
    public Table getTable();
    public Vector<Row> getErroneousRows();
    public String getResultMessage();
    public int getMessageCode();
    public int getErroneousColumnCount();
    public int getErroneousRowCount();
}
