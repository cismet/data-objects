/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.container;

import java.util.Arrays;

import de.cismet.diff.exception.IllegalCodeException;

/**
 * DOCUMENT ME!
 *
 * @author   Martin Scholl
 * @version  1.0
 */
public class NativeStatementGroup extends StatementGroup {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Oracle11gStatementGroup object.
     *
     * @param   group              DOCUMENT ME!
     * @param   sqlBundleResource  DOCUMENT ME!
     *
     * @throws  IllegalCodeException  DOCUMENT ME!
     */
    public NativeStatementGroup(final StatementGroup group, final String sqlBundleResource)
            throws IllegalCodeException {
        this(group.getStatements(), group.isTransaction(), sqlBundleResource);
        this.description = group.getDescription();
        this.tableName = group.getTableName();
        this.columnName = group.getColumnName();
        this.warning = group.getWarning();
    }

    /**
     * Creates a new instance of <code>Oracle11gStatementGroup.</code>
     *
     * @param   statements         DOCUMENT ME!
     * @param   transaction        DOCUMENT ME!
     * @param   sqlBundleResource  DOCUMENT ME!
     *
     * @throws  IllegalCodeException  DOCUMENT ME!
     */
    public NativeStatementGroup(final Statement[] statements, final boolean transaction, final String sqlBundleResource)
            throws IllegalCodeException {
        super(statements, transaction);
        applyStatements(statements, sqlBundleResource);
        if (transaction) {
            final Statement[] tmp = new Statement[statements.length + 2];
            tmp[0] = new NativeStatement(
                    "BEGIN WORK;", // NOI18N
                    null,
                    null,
                    false);
            tmp[tmp.length - 1] = new NativeStatement(
                    "COMMIT WORK;", // NOI18N
                    null,
                    null,
                    false);
            System.arraycopy(this.statements, 0, tmp, 1, this.statements.length);
            setStatements(tmp);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   statements         DOCUMENT ME!
     * @param   sqlBundleResource  DOCUMENT ME!
     *
     * @throws  IllegalCodeException      DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    private void applyStatements(final Statement[] statements, final String sqlBundleResource)
            throws IllegalCodeException {
        final NativeStatement[] stmts = new NativeStatement[statements.length];
        for (int i = 0; i < statements.length; i++) {
            if (statements[i] instanceof CodedStatement) {
                stmts[i] = new NativeStatement((CodedStatement)statements[i], sqlBundleResource);
            } else if (statements[i] instanceof NativeStatement) {
                stmts[i] = (NativeStatement)statements[i];
            } else {
                throw new IllegalArgumentException("Instance not known"); // NOI18N
            }
        }
        setStatements(stmts);
    }

    @Override
    public String toString() {
        return Arrays.toString(statements);
    }
}
