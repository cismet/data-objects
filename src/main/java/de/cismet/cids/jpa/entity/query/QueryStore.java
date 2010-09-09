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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.permission.QueryStorePermission;
import de.cismet.cids.jpa.entity.user.User;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "cs_query_store")
public class QueryStore extends CommonEntity implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_query_store_sequence",
        sequenceName = "cs_query_store_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_query_store_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    private User user;

    @Column(name = "name")
    private String name;

    @Column(name = "file_name")
    private String filename;

    @OneToMany(
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        mappedBy = "queryStore"
    )
    @Fetch(FetchMode.SUBSELECT)
    @Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Set<QueryStorePermission> queryStorePermissions;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new QueryStore object.
     */
    public QueryStore() {
        this.queryStorePermissions = new HashSet<QueryStorePermission>();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public User getUser() {
        return user;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  user  DOCUMENT ME!
     */
    public void setUser(final User user) {
        this.user = user;
    }

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
    public String getFilename() {
        return filename;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  filename  DOCUMENT ME!
     */
    public void setFilename(final String filename) {
        this.filename = filename;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Set<QueryStorePermission> getQueryStorePermissions() {
        return queryStorePermissions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  queryStorePermissions  DOCUMENT ME!
     */
    public void setQueryStorePermissions(final Set<QueryStorePermission> queryStorePermissions) {
        this.queryStorePermissions = queryStorePermissions;
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
