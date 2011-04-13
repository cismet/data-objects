/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.permission;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
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

import de.cismet.cids.jpa.entity.cidsclass.Attribute;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  1.0
 */
@Entity
@Table(name = "cs_ug_attr_perm")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AttributePermission extends AbstractPermission implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_ug_attr_perm_sequence",
        sequenceName = "cs_ug_attr_perm_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_ug_attr_perm_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "attr_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Attribute attribute;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attribute  DOCUMENT ME!
     */
    public void setAttribute(final Attribute attribute) {
        this.attribute = attribute;
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
