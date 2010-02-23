/*
 * Row.java
 *
 * Created on 13. September 2007, 17:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.maintenance.container;

import de.cismet.diff.container.Table;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 *
 * @author mscholl
 */
public class Row
{
    private Table table;
    private Vector headlessRowdata;
    
    private Vector<ErrorAwareEntry> rowdata;
    
    /** Creates a new instance of Row */
    public Row(Table table, Vector data, Vector<String> erroneousColumnNames)
    {
        this.table = table;
        this.headlessRowdata = data;
        String[] names = table.getColumnNames();
        rowdata = new Vector<ErrorAwareEntry>(names.length);
        while(names.length > headlessRowdata.size())
            headlessRowdata.add("");
        for(int i = 0; i < names.length; i++)
        {
            boolean hasError = erroneousColumnNames.contains(names[i]);
            rowdata.add(new ErrorAwareEntry(names[i], headlessRowdata.get(i),
                    hasError));
        }
    }
    
    public Object getData(String columnName)
    {
        for(Entry e : rowdata)
            if(e.getColumnName().equalsIgnoreCase(columnName))
                return e.getData();
        return null;
    }

    public Vector getHeadlessRowdata()
    {
        return headlessRowdata;
    }
    
    public Vector<ErrorAwareEntry> getRowdata()
    {
        return rowdata;
    }

    public Table getTable()
    {
        return table;
    }
    
    public Vector<ErrorAwareEntry> getErroneousEntries()
    {
        Vector<ErrorAwareEntry> ret = new Vector<ErrorAwareEntry>();
        for(ErrorAwareEntry entry : rowdata)
            if(entry.hasError())
                ret.add(entry);
        return ret;
    }
    
    public static class Entry
    {
        private String columnName;
        private Object data;
        
        public Entry(String columnName, Object data)
        {
            this.columnName = columnName;
            this.data = data;
        }

        public String getColumnName()
        {
            return columnName;
        }

        public Object getData()
        {
            return data;
        }
    }
    
    public static class ErrorAwareEntry extends Entry
    {
        private boolean hasError;
        
        public ErrorAwareEntry(String columnName, Object data, boolean hasError)
        {
            super(columnName, data);
            this.hasError = hasError;
        }

        public boolean hasError()
        {
            return hasError;
        }
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for(Object o : headlessRowdata)
            sb.append(o).append("\t");
        return sb.toString();
    }
}
