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
import javax.persistence.OneToOne;
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
@Table(name = "cs_type")
public class Type extends CommonEntity implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1006299738110761595L;

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_type_sequence",
        sequenceName = "cs_type_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_type_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    // not everything is complex, so nullable = true
    @OneToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "class_id",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    private CidsClass cidsClass;

    @Column(name = "complex_type")
    private Boolean complexType;

    @Column(name = "descr")
    private String description;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "editor",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    private JavaClass editor;

    @ManyToOne(
        optional = true,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "renderer",
        nullable = true
    )
    @Fetch(FetchMode.SELECT)
    private JavaClass renderer;

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
    public Boolean isComplexType() {
        return complexType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  complexType  DOCUMENT ME!
     */
    public void setComplexType(final Boolean complexType) {
        this.complexType = complexType;
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
    public JavaClass getEditor() {
        return editor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  editor  DOCUMENT ME!
     */
    public void setEditor(final JavaClass editor) {
        this.editor = editor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JavaClass getRenderer() {
        return renderer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  renderer  DOCUMENT ME!
     */
    public void setRenderer(final JavaClass renderer) {
        this.renderer = renderer;
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
}
