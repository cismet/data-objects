/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.catalog;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import org.openide.util.Exceptions;

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

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.common.Domain;

/**
 * DOCUMENT ME!
 *
 * @author   Martin Scholl
 * @version  1.0
 */
@Entity
@Table(name = "cs_cat_link")
public class CatLink extends CommonEntity implements Serializable, Cloneable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_cat_link_sequence",
        sequenceName = "cs_cat_link_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_cat_link_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_to")
    private Integer idTo;

    @Column(name = "id_from")
    private Integer idFrom;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "domain_to",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    private Domain domainTo;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CatLink object.
     */
    public CatLink() {
        idTo = null;
        idFrom = null;
        domainTo = null;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Domain getDomainTo() {
        return domainTo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  domainTo  DOCUMENT ME!
     */
    public void setDomainTo(final Domain domainTo) {
        this.domainTo = domainTo;
    }

    @Override
    public String toString() {
        return "ID: " + getId() + " :: Link from '" + getIdFrom() + "' to '" + getIdTo(); // NOI18N
    }

    @Override
    public CatLink clone() {
        final CatLink link;
        try {
            link = (CatLink)super.clone();
        } catch (final CloneNotSupportedException ex) {
            throw new IllegalStateException("could not clone catlink", ex); // NOI18N
        }
        link.idFrom = getIdFrom();
        link.idTo = getIdTo();
        link.domainTo = getDomainTo();
        return link;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Integer getIdTo() {
        return idTo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  idTo  DOCUMENT ME!
     */
    public void setIdTo(final Integer idTo) {
        this.idTo = idTo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Integer getIdFrom() {
        return idFrom;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  idFrom  DOCUMENT ME!
     */
    public void setIdFrom(final Integer idFrom) {
        this.idFrom = idFrom;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }
}
