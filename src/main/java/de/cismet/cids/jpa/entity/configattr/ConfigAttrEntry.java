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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import de.cismet.cids.jpa.entity.common.Domain;
import de.cismet.cids.jpa.entity.user.User;
import de.cismet.cids.jpa.entity.user.UserGroup;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "cs_config_attr_jt")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ConfigAttrEntry extends CommonEntity implements Serializable {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_config_attr_jt_sequence",
        sequenceName = "cs_config_attr_jt_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_config_attr_jt_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usr_id")
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ug_id")
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private UserGroup usergroup;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "dom_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Domain domain;

    @ManyToOne(
        cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH },
        optional = false
    )
    @JoinColumn(
        name = "key_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private ConfigAttrKey key;

    @ManyToOne(
        cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH },
        optional = false
    )
    @JoinColumn(
        name = "val_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private ConfigAttrValue value;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "type_id",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private ConfigAttrType type;

    //~ Methods ----------------------------------------------------------------

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(final Integer id) {
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  domain  DOCUMENT ME!
     */
    public void setDomain(final Domain domain) {
        this.domain = domain;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ConfigAttrKey getKey() {
        return key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  key  DOCUMENT ME!
     */
    public void setKey(final ConfigAttrKey key) {
        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public User getUser() {
        return user;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  user  DOCUMENT ME!
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public UserGroup getUsergroup() {
        return usergroup;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  usergroup  DOCUMENT ME!
     */
    public void setUsergroup(final UserGroup usergroup) {
        this.usergroup = usergroup;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ConfigAttrValue getValue() {
        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  value  DOCUMENT ME!
     */
    public void setValue(final ConfigAttrValue value) {
        this.value = value;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ConfigAttrType getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type  DOCUMENT ME!
     */
    public void setType(final ConfigAttrType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "[" + super.toString() + "] (" + getId() + ") key: " + getKey() + " || value: " + getValue();
    }
}
