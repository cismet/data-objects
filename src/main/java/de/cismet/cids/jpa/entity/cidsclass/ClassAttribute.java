/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.cidsclass;

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

import de.cismet.cids.jpa.entity.common.CommonEntity;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "cs_class_attr")
public class ClassAttribute extends CommonEntity implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 7059910660267569751L;

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_class_attr_sequence",
        sequenceName = "cs_class_attr_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_class_attr_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "attr_key")
    private String attrKey;

    @Column(name = "attr_value")
    private String attrValue;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "class_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    private CidsClass cidsClass;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "type_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    private Type type;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ClassAttribute object.
     */
    public ClassAttribute() {
        setAttrKey(null);
        setAttrValue(null);
        setCidsClass(null);
        setType(null);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getAttrKey() {
        return attrKey;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attrKey  DOCUMENT ME!
     */
    public void setAttrKey(final String attrKey) {
        this.attrKey = attrKey;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getAttrValue() {
        return attrValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attrValue  DOCUMENT ME!
     */
    public void setAttrValue(final String attrValue) {
        this.attrValue = attrValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public CidsClass getCidsClass() {
        return cidsClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cidsClass  DOCUMENT ME!
     */
    public void setCidsClass(final CidsClass cidsClass) {
        this.cidsClass = cidsClass;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Type getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type  DOCUMENT ME!
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getAttrKey() + "(" + getId() + ")" + getAttrValue(); // NOI18N
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
