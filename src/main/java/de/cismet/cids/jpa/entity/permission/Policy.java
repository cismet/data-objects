package de.cismet.cids.jpa.entity.permission;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author mscholl
 */
@Entity()
@Table(name = "cs_policy")
public class Policy extends CommonEntity
{
    @Id
    @SequenceGenerator(name = "cs_policy_sequence",
            sequenceName = "cs_policy_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "cs_policy_sequence")
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public static final Policy NO_POLICY;

    static
    {
        final Policy p = new Policy();
        p.name = "<keine Richtlinie>";
        p.id = null;
        NO_POLICY = p;
    }

    public Policy()
    {
        this.id = null;
        this.name = null;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(final Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}