/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.container;

import java.text.MessageFormat;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.cismet.diff.DiffAccessor;

import de.cismet.diff.exception.IllegalCodeException;

/**
 * Container class for native statements. This class is able to build correct statements of correct <code>
 * CodedStatements</code> using templates that can be found in the <code>de.cismet.diff.resource</code> package.<br/>
 * You can also build a <code>NativeStatement</code> manually. In this case there will be no corresponding <code>
 * CodedStatement</code> stored in the class.
 *
 * @author   Martin Scholl
 * @version  1.0 2015-09-09
 */
public final class NativeStatement extends Statement {

    //~ Instance fields --------------------------------------------------------

    private String statement;
    private transient CodedStatement codedStatement;
    private final transient ResourceBundle sqlBundle;
    private final transient ResourceBundle descBundle;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new empty instance of <code>NativeStatement</code>.
     */
    public NativeStatement() {
        this(null, null, null, false);
    }

    /**
     * Creates a new instance of <code>PSQLStatement</code> using a <code>CodedStatement</code>. Simply calls the <code>
     * applyCodedStatement</code> method.
     *
     * @param   statement          the statement which shall be the base of creation of the <code>NativeStatement</code>
     *                             instance
     * @param   sqlBundleResource  DOCUMENT ME!
     *
     * @throws  IllegalCodeException  if the code contained in the <code>CodedStatement</code> instance is not know or
     *                                invalid
     */
    public NativeStatement(final CodedStatement statement, final String sqlBundleResource) throws IllegalCodeException {
        super(statement.getWarning(), statement.isPedantic());
        sqlBundle = ResourceBundle.getBundle(sqlBundleResource);
        descBundle = ResourceBundle.getBundle(sqlBundleResource + "Description"); // NOI18N
        applyCodedStatement(statement);
    }

    /**
     * Lets you manually create a new instance of <code>NativeStatement</code> without using a <code>
     * CodedStatement</code> as base. You may provide your desired statement, the description and the warning.
     *
     * @param  statement    a string represending a SQL statement
     * @param  description  a custom prose description of what the provided statement does
     * @param  warning      a string indicating a warning that can indicate that the action the statement tries to
     *                      perform may fail etc.
     * @param  pedantic     DOCUMENT ME!
     */
    public NativeStatement(
            final String statement,
            final String description,
            final String warning,
            final boolean pedantic) {
        super(warning, pedantic);
        this.statement = statement;
        this.description = description;
        this.sqlBundle = null;
        this.descBundle = null;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * You can use this method to find out whether this instance has been create from a <code>CodedStatement</code> or
     * manually.
     *
     * @return  the <code>CodedStatement</code> this instance of <code>NativeStatement</code> was build of or null if it
     *          has been created manually.
     */
    public CodedStatement getCodedStatement() {
        return codedStatement;
    }

    /**
     * Tries to use the information the given <code>CodedStatement</code> provides to build a native statement with a
     * corresponding description and a warning if it is also provided.
     *
     * @param   codedStatement  the statement providing the information
     *
     * @throws  IllegalCodeException  DOCUMENT ME!
     */
    public void applyCodedStatement(final CodedStatement codedStatement) throws IllegalCodeException {
        final ResourceBundle exceptionBundle = ResourceBundle.getBundle(DiffAccessor.EXCEPTION_RESOURCE_BASE_NAME);
        final String[] args = codedStatement.getArgs();
        try {
            final String code = codedStatement.getCode();
            final MessageFormat sqlform = new MessageFormat(sqlBundle.getString(code));
            final MessageFormat descform = new MessageFormat(descBundle.getString(code));
            this.statement = sqlform.format(args);
            this.description = descform.format(args);
        } catch (final MissingResourceException ex) {
            throw new IllegalCodeException(
                exceptionBundle.getString(DiffAccessor.ILLEGAL_CODE_EXCEPTION_CODE_NOT_KNOWN),
                codedStatement.getCode(),
                ex);
        }
        try {
            MessageFormat warnform = null;
            if (codedStatement.getWarning() != null) {
                warnform = new MessageFormat(descBundle.getString(codedStatement.getWarning()));
            }
            if (warnform == null) {
                this.warning = null;
            } else {
                this.warning = warnform.format(args);
            }
        } catch (final MissingResourceException ex) {
            throw new IllegalCodeException(
                exceptionBundle.getString(DiffAccessor.ILLEGAL_CODE_EXCEPTION_WARNING_NOT_KNOWN),
                codedStatement.getWarning(),
                ex);
        }
        this.codedStatement = codedStatement;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getStatement() {
        return statement;
    }

    /**
     * Sets a new statement. Using this method will cause the <code>CodedStatement</code> held by this <code>
     * PSQLStatement</code> to be set to <code>null</code>.
     *
     * @param  statement  the new statement
     */
    public void setStatement(final String statement) {
        this.codedStatement = null;
        this.pedantic = false;
        this.statement = statement;
    }

    /**
     * Sets a new statement description. Using this method will cause the <code>CodedStatement</code> held by this
     * <code>PSQLStatement</code> to be set to <code>null</code>.
     *
     * @param  description  the new description of the statement
     */
    @Override
    public void setDescription(final String description) {
        this.codedStatement = null;
        this.description = description;
    }

    /**
     * Sets pedantic. Using this method will cause the <code>CodedStatement</code> held by this <code>
     * PSQLStatement</code> to be set to <code>null</code>.
     *
     * @param  pedantic  DOCUMENT ME!
     */
    public void setPedantic(final boolean pedantic) {
        this.codedStatement = null;
        this.pedantic = pedantic;
    }

    @Override
    public String toString() {
        if (warning == null) {
            return statement + " :: " + description;                    // NOI18N
        } else {
            return statement + " :: " + description + " :: " + warning; // NOI18N
        }
    }
}
