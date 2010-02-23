/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.entity.permission;

import de.cismet.cids.jpa.entity.common.CommonEntity;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 *
 * @author mscholl
 */
@Entity()
@Table(name = "cs_policy_rule")
public class PolicyRule extends CommonEntity
{
    @Id
    @SequenceGenerator(name = "cs_policy_rule_sequence",
            sequenceName = "cs_policy_rule_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "cs_policy_rule_sequence")
    @Column(name = "id")
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "policy", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Policy policy;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "permission", nullable = false)
    @Fetch(FetchMode.SELECT)
    private Permission permission;

    @Column(name = "default_value")
    private Boolean defaultValue;

    public PolicyRule()
    {
        id = null;
        policy = null;
        permission = null;
        defaultValue = null;
    }

    public Boolean getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(final Boolean defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public Permission getPermission()
    {
        return permission;
    }

    public void setPermission(final Permission permission)
    {
        this.permission = permission;
    }

    public Policy getPolicy()
    {
        return policy;
    }

    public void setPolicy(final Policy policy)
    {
        this.policy = policy;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(final Integer id)
    {
        this.id = id;
    }
}