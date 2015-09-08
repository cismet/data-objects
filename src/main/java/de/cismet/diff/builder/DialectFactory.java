/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.builder;

import java.util.Properties;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public class DialectFactory {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DialectFactory object.
     */
    private DialectFactory() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   runtime  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    public static ScriptGeneratorDialect getScriptGeneratorDialect(final Properties runtime) {
        final String internalDialect = runtime.getProperty("internalDialect");                  // NOI18N
        if ((internalDialect == null) || "postgres_9".equals(internalDialect)) {                // NOI18N
            return new PostgresDialect(runtime);
//            stmtBundle = ResourceBundle.getBundle(
//                    "de.cismet.diff.resource.script_gen_stmts");                                // NOI18N
        } else if ("oracle_11g".equals(internalDialect)) {
            return new Oracle11gDialect(runtime);
//            stmtBundle = ResourceBundle.getBundle(
//                    "de.cismet.diff.resource.script_gen_stmts_oracle_11g");                     // NOI18N
        } else {
            throw new UnsupportedOperationException("unsupported dialect: " + internalDialect); // NOI18N
        }
    }
}
