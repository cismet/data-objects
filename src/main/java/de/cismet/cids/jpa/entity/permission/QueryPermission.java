/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.permission;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.cismet.cids.jpa.entity.query.Query;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "cs_query_ug_assoc")
public class QueryPermission extends AbstractPermission implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_query_ug_assoc_sequence",
        sequenceName = "cs_query_ug_assoc_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_query_ug_assoc_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "query_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    private Query query;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Query getQuery() {
        return query;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  query  DOCUMENT ME!
     */
    public void setQuery(final Query query) {
        this.query = query;
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
