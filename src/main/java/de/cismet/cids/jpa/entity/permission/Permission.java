/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.permission;

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
@Table(name = "cs_permission")
public class Permission extends CommonEntity implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = -133598201451198769L;

    public static final Permission NO_PERMISSION;

    static {
        NO_PERMISSION = new Permission();
        NO_PERMISSION.setKey("<kein Recht>"); // TODO: I18N
    }

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_permission_sequence",
        sequenceName = "cs_permission_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_permission_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "key")
    private String key;

    @Column(name = "description")
    private String description;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getKey() {
        return key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  key  DOCUMENT ME!
     */
    public void setKey(final String key) {
        this.key = key;
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return key;
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
