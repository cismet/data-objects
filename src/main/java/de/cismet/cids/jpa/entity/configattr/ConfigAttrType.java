/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.configattr;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "cs_config_attr_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfigAttrType extends CommonEntity implements Serializable {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum Types {

        //~ Enum constants -----------------------------------------------------

        CONFIG_ATTR, ACTION_TAG, XML_ATTR
    }

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_config_attr_type_sequence",
        sequenceName = "cs_config_attr_type_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_config_attr_type_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "descr")
    private String description;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    @Override
    public void setId(final Integer id) {
        this.id = id;
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
    public String getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type  DOCUMENT ME!
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Types getAttrType() {
        if ("C".equalsIgnoreCase(getType())) {
            return Types.CONFIG_ATTR;
        } else if ("A".equalsIgnoreCase(getType())) {
            return Types.ACTION_TAG;
        } else if ("X".equalsIgnoreCase(getType())) {
            return Types.XML_ATTR;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   type  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String getType(final Types type) {
        if (Types.CONFIG_ATTR.equals(type)) {
            return "C";
        } else if (Types.ACTION_TAG.equals(type)) {
            return "A";
        } else if (Types.XML_ATTR.equals(type)) {
            return "X";
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   type  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isType(final Types type) {
        return getAttrType().equals(type);
    }
}
