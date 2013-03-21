/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.maintenance.container;

import java.util.ArrayList;
import java.util.List;

import de.cismet.diff.container.Table;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public class Row {

    //~ Instance fields --------------------------------------------------------

    private final transient Table table;
    private final transient List<Object> headlessRowdata;

    private final transient List<ErrorAwareEntry> rowdata;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of Row.
     *
     * @param  table                 DOCUMENT ME!
     * @param  data                  DOCUMENT ME!
     * @param  erroneousColumnNames  DOCUMENT ME!
     */
    public Row(final Table table, final List<Object> data, final List<String> erroneousColumnNames) {
        this.table = table;
        this.headlessRowdata = data;
        final String[] names = table.getColumnNames();
        rowdata = new ArrayList<ErrorAwareEntry>(names.length);
        while (names.length > headlessRowdata.size()) {
            headlessRowdata.add(""); // NOI18N
        }
        for (int i = 0; i < names.length; i++) {
            final boolean hasError = erroneousColumnNames.contains(names[i]);
            rowdata.add(new ErrorAwareEntry(names[i], headlessRowdata.get(i), hasError));
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   columnName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getData(final String columnName) {
        for (final Entry e : rowdata) {
            if (e.getColumnName().equalsIgnoreCase(columnName)) {
                return e.getData();
            }
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List getHeadlessRowdata() {
        return headlessRowdata;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<ErrorAwareEntry> getRowdata() {
        return rowdata;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Table getTable() {
        return table;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<ErrorAwareEntry> getErroneousEntries() {
        final List<ErrorAwareEntry> ret = new ArrayList<ErrorAwareEntry>();
        for (final ErrorAwareEntry entry : rowdata) {
            if (entry.hasError()) {
                ret.add(entry);
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (final Object o : headlessRowdata) {
            sb.append(o).append('\t'); // NOI18N
        }
        return sb.toString();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class Entry {

        //~ Instance fields ----------------------------------------------------

        private final transient String columnName;
        private final transient Object data;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Entry object.
         *
         * @param  columnName  DOCUMENT ME!
         * @param  data        DOCUMENT ME!
         */
        public Entry(final String columnName, final Object data) {
            this.columnName = columnName;
            this.data = data;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public String getColumnName() {
            return columnName;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public Object getData() {
            return data;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class ErrorAwareEntry extends Entry {

        //~ Instance fields ----------------------------------------------------

        private final transient boolean hasError;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ErrorAwareEntry object.
         *
         * @param  columnName  DOCUMENT ME!
         * @param  data        DOCUMENT ME!
         * @param  hasError    DOCUMENT ME!
         */
        public ErrorAwareEntry(final String columnName, final Object data, final boolean hasError) {
            super(columnName, data);
            this.hasError = hasError;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean hasError() {
            return hasError;
        }
    }
}
