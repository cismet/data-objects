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
@Table(name = "cs_icon")
public class Icon extends CommonEntity implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1327324025915272180L;

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_icon_sequence",
        sequenceName = "cs_icon_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_icon_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "file_name")
    private String fileName;

    //~ Methods ----------------------------------------------------------------

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
    public String getFileName() {
        return fileName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fileName  DOCUMENT ME!
     */
    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getName() + "(" + getId() + ")"; // NOI18N
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Icon)) {
            return false;
        }
        final Icon i = (Icon)o;
        return this.getId().equals(i.getId())
                    && this.getName().equals(i.getName())
                    && this.getFileName().equals(i.getFileName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = (29 * hash) + ((this.id != null) ? this.id.hashCode() : 0);
        hash = (29 * hash) + ((this.name != null) ? this.name.hashCode() : 0);
        hash = (29 * hash) + ((this.fileName != null) ? this.fileName.hashCode() : 0);
        return hash;
    }
}
