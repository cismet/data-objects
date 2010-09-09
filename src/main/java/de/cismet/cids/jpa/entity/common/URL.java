/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.common;

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

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  1.0
 */
@Entity
@Table(name = "url")
public class URL extends CommonEntity implements Serializable {

    //~ Static fields/initializers ---------------------------------------------

    public static final URL NO_DESCRIPTION;

    static {
        final URL url = new URL();
        final URLBase urlbase = new URLBase();
        urlbase.setPath("");                       // NOI18N
        urlbase.setProtocolPrefix("");             // NOI18N
        urlbase.setServer("");                     // NOI18N
        url.setUrlbase(urlbase);
        url.setObjectName("<keine Beschreibung>"); // NOI18N
        NO_DESCRIPTION = url;
    }

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "url_seq",
        sequenceName = "url_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "url_seq"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "object_name")
    private String objectName;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "url_base_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    private URLBase urlbase;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  objectName  DOCUMENT ME!
     */
    public void setObjectName(final String objectName) {
        this.objectName = objectName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public URLBase getUrlbase() {
        return urlbase;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  urlbase  DOCUMENT ME!
     */
    public void setUrlbase(final URLBase urlbase) {
        this.urlbase = urlbase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getUrlbase().toString() + getObjectName();
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
