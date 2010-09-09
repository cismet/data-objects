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
@Table(name = "cs_all_attr_mapping")
public class AllAttrMapping extends CommonEntity implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_all_attr_mapping_sequence",
        sequenceName = "cs_all_attr_mapping_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_all_attr_mapping_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "class_id")
    private Integer classId;

    @Column(name = "object_id")
    private Integer objectId;

    @Column(name = "attr_class_id")
    private Integer attrClassId;

    @Column(name = "attr_object_id")
    private Integer attrObjectId;

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
    public Integer getAttrClassId() {
        return attrClassId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attrClassId  DOCUMENT ME!
     */
    public void setAttrClassId(final Integer attrClassId) {
        this.attrClassId = attrClassId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Integer getAttrObjectId() {
        return attrObjectId;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  attrObjectId  DOCUMENT ME!
     */
    public void setAttrObjectId(final Integer attrObjectId) {
        this.attrObjectId = attrObjectId;
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
