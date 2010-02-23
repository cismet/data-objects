/*
 * ThreadUtil.java
 *
 * Created on 15. Februar 2008, 10:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.util;

import java.util.Arrays;

/**
 *
 * @author mscholl
 */
public final class ThreadUtil
{
    /** Creates a new instance of ThreadUtil */
    private ThreadUtil()
    {
    }
    
    public static boolean equals(final StackTraceElement[] ste1, final 
            StackTraceElement[] ste2, final boolean lineNumbers)
    {
        if(ste1 == null || ste2 == null)
            throw new NullPointerException("equality check only allowed for " +
                    "non-null objects: ste1=" + ste1 + "||" + "ste2=" + ste2);
        if(ste1.length != ste2.length)
            return false;
        // though stacktrace "begins" at the last element of the array, this
        // approuch will be way faster than beginning at the "root" element
        for(int i = 0; i < ste1.length; ++i)
            if(!equals(ste1[i], ste2[i], lineNumbers))
                return false;
        return true;
    }
    
    public static boolean equals(final StackTraceElement ste1, final 
            StackTraceElement ste2, final boolean lineNumbers)
    {
        if(ste1 == null || ste2 == null)
            throw new NullPointerException("equality check only allowed for " +
                    "non-null objects: ste1=" + ste1 + "||" + "ste2=" + ste2);
        if(lineNumbers)
            return ste1.equals(ste2);
        if(ste1.getMethodName().equals(ste2.getMethodName()) &&
                ste1.getClassName().equals(ste2.getClassName()))
        {
            final String fn1 = ste1.getFileName();
            final String fn2 = ste2.getFileName();
            if((fn1 == null && fn2 != null) || (fn1 != null && fn2 == null))
                return false;
            if((fn1 == null && fn2 == null) || fn1.equals(fn2))
                return true;
        }
        return false;
    }
    
    public static StackTraceElement[] truncateCommon(final StackTraceElement[] 
            ste)
    {
        if(ste.length < 2)
            return null;
        final StackTraceElement[] truncated = new StackTraceElement[ste.length -
                2];
        for(int i = ste.length - 1; i > 1; --i)
            truncated[i - 2] = ste[i];
        return truncated;
    }
    
    public static StackTraceElement[] getCallerTrace(final StackTraceElement[]
            ste)
    {
        if(ste.length < 3)
            return null;
        final StackTraceElement[] truncated = new StackTraceElement[ste.length -
                3];
        for(int i = ste.length - 1; i > 2; --i)
            truncated[i - 3] = ste[i];
        return truncated;
    }
}
