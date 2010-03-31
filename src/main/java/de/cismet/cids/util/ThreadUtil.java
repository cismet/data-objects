/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.util;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public final class ThreadUtil {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ThreadUtil.
     */
    private ThreadUtil() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   ste1         DOCUMENT ME!
     * @param   ste2         DOCUMENT ME!
     * @param   lineNumbers  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public static boolean equals(
            final StackTraceElement[] ste1,
            final StackTraceElement[] ste2,
            final boolean lineNumbers) {
        if ((ste1 == null) || (ste2 == null)) {
            throw new IllegalArgumentException(
                "equality check only allowed for non-null objects: ste1=" + ste1 + "||" + "ste2=" + ste2);
        }
        if (ste1.length != ste2.length) {
            return false;
        }
        // though stacktrace "begins" at the last element of the array, this
        // approuch will be way faster than beginning at the "root" element
        for (int i = 0; i < ste1.length; ++i) {
            if (!equals(ste1[i], ste2[i], lineNumbers)) {
                return false;
            }
        }
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ste1         DOCUMENT ME!
     * @param   ste2         DOCUMENT ME!
     * @param   lineNumbers  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  NullPointerException  DOCUMENT ME!
     */
    public static boolean equals(
            final StackTraceElement ste1,
            final StackTraceElement ste2,
            final boolean lineNumbers) {
        if ((ste1 == null) || (ste2 == null)) {
            throw new IllegalArgumentException(
                "equality check only allowed for non-null objects: ste1=" + ste1 + "||" + "ste2=" + ste2);
        }
        if (lineNumbers) {
            return ste1.equals(ste2);
        }
        if (ste1.getMethodName().equals(ste2.getMethodName())
                    && ste1.getClassName().equals(ste2.getClassName())) {
            final String fn1 = ste1.getFileName();
            final String fn2 = ste2.getFileName();
            if (((fn1 == null) && (fn2 != null)) || ((fn1 != null) && (fn2 == null))) {
                return false;
            }
            if (((fn1 == null) && (fn2 == null)) || fn1.equals(fn2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ste  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static StackTraceElement[] truncateCommon(final StackTraceElement[] ste) {
        if (ste.length < 2) {
            return null;
        }
        final StackTraceElement[] truncated = new StackTraceElement[ste.length - 2];
        for (int i = ste.length - 1; i > 1; --i) {
            truncated[i - 2] = ste[i];
        }
        return truncated;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   ste  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static StackTraceElement[] getCallerTrace(final StackTraceElement[] ste) {
        if (ste.length < 3) {
            return null;
        }
        final StackTraceElement[] truncated = new StackTraceElement[ste.length - 3];
        for (int i = ste.length - 1; i > 2; --i) {
            truncated[i - 3] = ste[i];
        }
        return truncated;
    }
}
