/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.permission;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

import de.cismet.cids.jpa.entity.common.CommonEntity;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
@Entity
@Table(name = "cs_policy_rule")
public class PolicyRule extends CommonEntity {

    //~ Instance fields --------------------------------------------------------

    @Id
    @SequenceGenerator(
        name = "cs_policy_rule_sequence",
        sequenceName = "cs_policy_rule_sequence",
        allocationSize = 1
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "cs_policy_rule_sequence"
    )
    @Column(name = "id")
    private Integer id;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "policy",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    private Policy policy;

    @ManyToOne(
        optional = false,
        fetch = FetchType.EAGER
    )
    @JoinColumn(
        name = "permission",
        nullable = false
    )
    @Fetch(FetchMode.SELECT)
    private Permission permission;

    @Column(name = "default_value")
    private Boolean defaultValue;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Boolean getDefaultValue() {
        return defaultValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  defaultValue  DOCUMENT ME!
     */
    public void setDefaultValue(final Boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Permission getPermission() {
        return permission;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  permission  DOCUMENT ME!
     */
    public void setPermission(final Permission permission) {
        this.permission = permission;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Policy getPolicy() {
        return policy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  policy  DOCUMENT ME!
     */
    public void setPolicy(final Policy policy) {
        this.policy = policy;
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
