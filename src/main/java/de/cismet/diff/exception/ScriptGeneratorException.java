/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * ScriptGeneratorException.java
 *
 * Created on 27. Februar 2007, 13:29
 */
package de.cismet.diff.exception;

/**
 * If this exception is thrown it indicates that something went wrong during the generation of the statements. It shall
 * just be thrown by instances of the <code>ScriptGenerator</code> class and shall wrap any other exception that occurs
 * during statement generation. Everytime a new instance of this exception is created there shall be as many information
 * provided as possible because it is the most common way to find out about what went wrong while generating statements.
 *
 * @author   Martin Scholl
 * @version  1.0 2007-03-08
 */
public class ScriptGeneratorException extends java.lang.Exception {

    //~ Instance fields --------------------------------------------------------

    private final transient String table;
    private final transient String column;
    private final transient String type;

    //~ Constructors -----------------------------------------------------------

    /**
     * Constructs an instance of <code>ScriptGeneratorException</code> with the specified detail message. <code>
     * null</code> is passed for all other values the most powerful constructor of this class has.
     *
     * @param  msg  the detail message.
     */
    public ScriptGeneratorException(final String msg) {
        this(msg, null, null, null, null);
    }

    /**
     * Constructs an instance of <code>ScriptGeneratorException</code> with the specified detail message and the
     * specified exception as cause. <code>null</code> is passed for all other values the most powerful constructor of
     * this class has.
     *
     * @param  msg    the detail message.
     * @param  cause  the causing exception
     */
    public ScriptGeneratorException(final String msg, final Throwable cause) {
        this(msg, null, null, null, cause);
    }

    /**
     * Constructs an instance of <code>ScriptGeneratorException</code> with the specified detail message, the specified
     * exception as cause and the name of the involved table for more detailed information. <code>null</code> is passed
     * for all other values the most powerful constructor of this class has.
     *
     * @param  msg    the detail message.
     * @param  table  the name of the table involved
     * @param  cause  the causing exception
     */
    public ScriptGeneratorException(final String msg, final String table, final Throwable cause) {
        this(msg, table, null, null, cause);
    }

    /**
     * Constructs an instance of <code>ScriptGeneratorException</code> with the specified detail message, the specified
     * exception as cause, the name of the involved table and the name of the involved column for more detailed
     * information. <code>null</code> is passed for all other values the most powerful constructor of this class has.
     *
     * @param  msg     the detail message.
     * @param  table   the name of the table involved
     * @param  column  the name of the column involved
     * @param  cause   the causing exception
     */
    public ScriptGeneratorException(final String msg, final String table, final String column, final Throwable cause) {
        this(msg, table, column, null, cause);
    }

    /**
     * Constructs an instance of <code>ScriptGeneratorException</code> with the specified detail message, the specified
     * exception as cause, the name of the involved table, the name of the involved column and the name of the involved
     * type for more detailed information.
     *
     * @param  msg     the detail message.
     * @param  table   the name of the table involved
     * @param  column  the name of the column involved
     * @param  type    the name of the type involved
     * @param  cause   the causing exception
     */
    public ScriptGeneratorException(
            final String msg,
            final String table,
            final String column,
            final String type,
            final Throwable cause) {
        super(msg, cause);
        this.table = table;
        this.column = column;
        this.type = type;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the name of the table involved or <code>null</code> if no table has been involved
     */
    public String getTable() {
        return table;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the name of the column involved or <code>null</code> if no column has been involved
     */
    public String getColumn() {
        return column;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the name of the type involved or <code>null</code> if no type has been involved
     */
    public String getType() {
        return type;
    }
}
