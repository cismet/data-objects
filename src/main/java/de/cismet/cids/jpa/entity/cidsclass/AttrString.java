/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.cidsclass;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "cs_attr_string")
public class AttrString extends CommonEntity implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 4625114499652288080L;

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_attr_string_sequence",
        sequenceName = "cs_attr_string_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_attr_string_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "attr_id")
    private Integer attrId;

    @Column(name = "object_id")
    private Integer objectId;

    @Column(name = "string_val")
    private String stringVal;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Integer getClassId() {
        return classId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  classId  DOCUMENT ME!
     */
    public void setClassId(final Integer classId) {
        this.classId = classId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Integer getAttrId() {
        return attrId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attrId  DOCUMENT ME!
     */
    public void setAttrId(final Integer attrId) {
        this.attrId = attrId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Integer getObjectId() {
        return objectId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  objectId  DOCUMENT ME!
     */
    public void setObjectId(final Integer objectId) {
        this.objectId = objectId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getStringVal() {
        return stringVal;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  stringVal  DOCUMENT ME!
     */
    public void setStringVal(final String stringVal) {
        this.stringVal = stringVal;
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
