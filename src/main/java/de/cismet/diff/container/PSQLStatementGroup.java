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
public class PSQLStatementGroup extends StatementGroup {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PSQLStatementGroup object.
     *
     * @param   group  DOCUMENT ME!
     *
     * @throws  IllegalCodeException  DOCUMENT ME!
     */
    public PSQLStatementGroup(final StatementGroup group) throws IllegalCodeException {
        this(group.getStatements(), group.isTransaction());
        this.description = group.getDescription();
        this.tableName = group.getTableName();
        this.columnName = group.getColumnName();
        this.warning = group.getWarning();
    }

    /**
     * Creates a new instance of <code>PSQLStatementGroup.</code>
     *
     * @param   statements   DOCUMENT ME!
     * @param   transaction  DOCUMENT ME!
     *
     * @throws  IllegalCodeException  DOCUMENT ME!
     */
    public PSQLStatementGroup(final Statement[] statements, final boolean transaction) throws IllegalCodeException {
        super(statements, transaction);
        applyStatements(statements);
        if (transaction) {
            final Statement[] tmp = new Statement[statements.length + 2];
            tmp[0] = new PSQLStatement(
                    "BEGIN WORK;", // NOI18N
                    null,
                    null,
                    false);
            tmp[tmp.length - 1] = new PSQLStatement(
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
     * @param   statements  DOCUMENT ME!
     *
     * @throws  IllegalCodeException      DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    private void applyStatements(final Statement[] statements) throws IllegalCodeException {
        final PSQLStatement[] psqlStatements = new PSQLStatement[statements.length];
        for (int i = 0; i < statements.length; i++) {
            if (statements[i] instanceof CodedStatement) {
                psqlStatements[i] = new PSQLStatement((CodedStatement)statements[i]);
            } else if (statements[i] instanceof PSQLStatement) {
                psqlStatements[i] = (PSQLStatement)statements[i];
            } else {
                throw new IllegalArgumentException("Instance not known"); // NOI18N
            }
        }
        setStatements(psqlStatements);
    }

    @Override
    public String toString() {
        return Arrays.toString(statements);
    }
}
