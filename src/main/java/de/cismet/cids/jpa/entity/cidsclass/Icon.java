package de.cismet.cids.jpa.entity.cidsclass;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity()
@Table(name = "cs_icon")
public class Icon extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "cs_icon_sequence",
            sequenceName = "cs_icon_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "cs_icon_sequence")
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "file_name")
    private String fileName;
    
    public Icon()
    {
        name = null;
        fileName = null;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(final String name)
    {
        this.name = name;
    }
    
    public String getFileName()
    {
        return fileName;
    }
    
    public void setFileName(final String fileName)
    {
        this.fileName = fileName;
    }
    
    @Override
    public String toString()
    {
        return getName() + "(" + getId() + ")";
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(final Integer id)
    {
        this.id = id;
    }

    @Override
    public boolean equals(final Object o)
    {
        if(!(o instanceof Icon))
            return false;
        final Icon i = (Icon)o;
        return this.getId().equals(i.getId()) && 
                this.getName().equals(i.getName()) &&
                this.getFileName().equals(i.getFileName());
    }
}