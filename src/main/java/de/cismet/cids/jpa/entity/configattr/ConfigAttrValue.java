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
@Table(name = "cs_config_attr_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfigAttrValue extends CommonEntity implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_config_attr_value_sequence",
        sequenceName = "cs_config_attr_value_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_config_attr_value_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "value")
    private String value;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  value  DOCUMENT ME!
     */
    public void setValue(final String value) {
        this.value = value;
    }

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

    @Override
    public String toString() {
        return "[" + super.toString() + "] (" + getId() + ") value: " + getValue();
    }
}
