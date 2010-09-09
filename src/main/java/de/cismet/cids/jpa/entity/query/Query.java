/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.query;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.cismet.cids.jpa.entity.cidsclass.CidsClass;
import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.permission.QueryPermission;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "cs_query")
public class Query extends CommonEntity implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_query_sequence",
        sequenceName = "cs_query_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_query_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "descr")
    private String description;

    @Column(name = "statement")
    private String statement;

    @Column(name = "result")
    private Integer result;

    @Column(name = "is_update")
    private Boolean isUpdate;

    @Column(name = "is_union")
    private Boolean isUnion;

    @Column(name = "is_root")
    private Boolean isRoot;

    @Column(name = "is_batch")
    private Boolean isBatch;

    @Column(name = "conjunction")
    private Boolean isConjunction;

    @Column(name = "is_search")
    private Boolean isSearch;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "query"
    )
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<QueryParameter> queryParameters;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "query"
    )
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<QueryPermission> queryPermissions;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "cs_query_class_assoc",
        joinColumns = { @JoinColumn(name = "class_id") },
        inverseJoinColumns = { @JoinColumn(name = "query_id") }
    )
    @Fetch(FetchMode.SUBSELECT)
    private Set<CidsClass> cidsClasses;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of Query.
     */
    public Query() {
        // init with false to avoid auto-unboxing NPE
        this.isUpdate = false;
        this.isUnion = false;
        this.isRoot = false;
        this.isBatch = false;
        this.isConjunction = false;
        this.isSearch = false;
        this.queryParameters = new HashSet<QueryParameter>();
        this.queryPermissions = new HashSet<QueryPermission>();
        this.cidsClasses = new HashSet<CidsClass>();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name  DOCUMENT ME!
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  description  DOCUMENT ME!
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getStatement() {
        return statement;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  statement  DOCUMENT ME!
     */
    public void setStatement(final String statement) {
        this.statement = statement;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Integer getResult() {
        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  result  DOCUMENT ME!
     */
    public void setResult(final Integer result) {
        this.result = result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isUpdate  DOCUMENT ME!
     */
    public void setIsUpdate(final Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getIsUnion() {
        return isUnion;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isUnion  DOCUMENT ME!
     */
    public void setIsUnion(final Boolean isUnion) {
        this.isUnion = isUnion;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getIsRoot() {
        return isRoot;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isRoot  DOCUMENT ME!
     */
    public void setIsRoot(final Boolean isRoot) {
        this.isRoot = isRoot;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getIsBatch() {
        return isBatch;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isBatch  DOCUMENT ME!
     */
    public void setIsBatch(final Boolean isBatch) {
        this.isBatch = isBatch;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getIsConjunction() {
        return isConjunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isConjunction  DOCUMENT ME!
     */
    public void setIsConjunction(final Boolean isConjunction) {
        this.isConjunction = isConjunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getIsSearch() {
        return isSearch;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  isSearch  DOCUMENT ME!
     */
    public void setIsSearch(final Boolean isSearch) {
        this.isSearch = isSearch;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<QueryParameter> getQueryParameters() {
        return queryParameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  queryParameters  DOCUMENT ME!
     */
    public void setQueryParameters(final Set<QueryParameter> queryParameters) {
        this.queryParameters = queryParameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<QueryPermission> getQueryPermissions() {
        return queryPermissions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  queryPermissions  DOCUMENT ME!
     */
    public void setQueryPermissions(final Set<QueryPermission> queryPermissions) {
        this.queryPermissions = queryPermissions;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<CidsClass> getCidsClasses() {
        return cidsClasses;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsClasses  DOCUMENT ME!
     */
    public void setCidsClasses(final Set<CidsClass> cidsClasses) {
        this.cidsClasses = cidsClasses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName() + "(" + getId() + ")"; // NOI18N
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setId(final Integer id) {
        this.id = id;
    }
}
