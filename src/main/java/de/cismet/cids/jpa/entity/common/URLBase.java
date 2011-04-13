/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.common;

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

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  1.0
 */
@Entity
@Table(name = "url_base")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class URLBase extends CommonEntity implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "url_base_seq",
        sequenceName = "url_base_seq",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "url_base_seq"
    )
    @Column(name = "id")
    private Integer id;

    @Column(name = "prot_prefix")
    private String protocolPrefix;

    @Column(name = "server")
    private String server;

    @Column(name = "path")
    private String path;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getProtocolPrefix() {
        return protocolPrefix;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  protocolPrefix  DOCUMENT ME!
     */
    public void setProtocolPrefix(final String protocolPrefix) {
        this.protocolPrefix = protocolPrefix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getServer() {
        return server;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  server  DOCUMENT ME!
     */
    public void setServer(final String server) {
        this.server = server;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getPath() {
        return path;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  path  DOCUMENT ME!
     */
    public void setPath(final String path) {
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return protocolPrefix + server + path;
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
