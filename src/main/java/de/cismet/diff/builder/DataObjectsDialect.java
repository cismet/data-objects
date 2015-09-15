/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.diff.builder;

import java.util.List;
import java.util.Map;

import de.cismet.cids.jpa.entity.cidsclass.Attribute;
import de.cismet.cids.jpa.entity.cidsclass.CidsClass;

import de.cismet.diff.container.Statement;
import de.cismet.diff.container.StatementGroup;
import de.cismet.diff.container.Table;
import de.cismet.diff.container.TableColumn;

import de.cismet.diff.exception.IllegalCodeException;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public interface DataObjectsDialect {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   column        DOCUMENT ME!
     * @param   typename      DOCUMENT ME!
     * @param   precision     DOCUMENT ME!
     * @param   scale         DOCUMENT ME!
     * @param   defaultValue  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isDefaultValueValid(
            String column,
            String typename,
            Integer precision,
            Integer scale,
            String defaultValue);

    /**
     * DOCUMENT ME!
     *
     * @param   seqName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean sequenceExists(String seqName);

    /**
     * DOCUMENT ME!
     *
     * @param   tableName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isTableEmpty(String tableName);

    /**
     * DOCUMENT ME!
     *
     * @param   tables     DOCUMENT ME!
     * @param   tableName  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Table findTable(Table[] tables, String tableName);

    /**
     * DOCUMENT ME!
     *
     * @param   group  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalCodeException  DOCUMENT ME!
     */
    StatementGroup createDialectAwareStatementGroup(final StatementGroup group) throws IllegalCodeException;

    /**
     * DOCUMENT ME!
     *
     * @param   targetCreateTable  DOCUMENT ME!
     * @param   attrName           DOCUMENT ME!
     * @param   attrTypeName       DOCUMENT ME!
     * @param   optional           DOCUMENT ME!
     * @param   precision          DOCUMENT ME!
     * @param   scale              DOCUMENT ME!
     * @param   defaultValue       DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    StringBuilder appendCreateTableExpression(StringBuilder targetCreateTable,
            String attrName,
            String attrTypeName,
            boolean optional,
            Integer precision,
            Integer scale,
            String defaultValue);

    /**
     * DOCUMENT ME!
     *
     * @param  targetStatements   DOCUMENT ME!
     * @param  targetCreateTable  DOCUMENT ME!
     * @param  attrName           DOCUMENT ME!
     * @param  tableName          DOCUMENT ME!
     */
    void handlePKCreation(List<Statement> targetStatements,
            StringBuilder targetCreateTable,
            String attrName,
            String tableName);

    /**
     * DOCUMENT ME!
     *
     * @param  targetStatements  DOCUMENT ME!
     * @param  table             DOCUMENT ME!
     * @param  cidsClass         DOCUMENT ME!
     */
    void handlePKDiff(List<Statement> targetStatements, Table table, CidsClass cidsClass);

    /**
     * DOCUMENT ME!
     *
     * @param  target     DOCUMENT ME!
     * @param  column     DOCUMENT ME!
     * @param  attrName   DOCUMENT ME!
     * @param  tableName  DOCUMENT ME!
     */
    void handlePKDefault(List<StatementGroup> target, TableColumn column, String attrName, String tableName);

    /**
     * DOCUMENT ME!
     *
     * @param   column  DOCUMENT ME!
     * @param   attr    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean typeMismatch(TableColumn column, Attribute attr);

    /**
     * DOCUMENT ME!
     *
     * @param   directionTo  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Map<String, String> getTypeMap(boolean directionTo);

    /**
     * DOCUMENT ME!
     *
     * @param   table  DOCUMENT ME!
     * @param   attr   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isIntegerType(Table table, String attr);

    /**
     * DOCUMENT ME!
     *
     * @param   table  DOCUMENT ME!
     * @param   attr   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Statement[] allowNull(String table, String attr);

    /**
     * DOCUMENT ME!
     *
     * @param   table  DOCUMENT ME!
     * @param   attr   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Statement[] removeDefault(String table, String attr);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getMaxIdentifierChars();
}
