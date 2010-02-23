/*
 * URL.java
 *
 * Created on 24. January 2008, 15:44
 */

package de.cismet.cids.jpa.entity.common;

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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


/**
 * @author mscholl
 * @version 1.0
 */
@Entity()
@Table(name = "url")
public class URL extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "url_seq",
            sequenceName = "url_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "url_seq")
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "object_name")
    private String objectName;
    
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "url_base_id", nullable = false)
    @Fetch(FetchMode.SELECT)
    private URLBase urlbase;
    
    public static final URL NO_DESCRIPTION;
    
    static
    {
        final URL url = new URL();
        final URLBase urlbase = new URLBase();
        urlbase.setPath("");
        urlbase.setProtocolPrefix("");
        urlbase.setServer("");
        url.setUrlbase(urlbase);
        url.setObjectName("<keine Beschreibung>");
        NO_DESCRIPTION = url;
    }
    
    public URL()
    {
        objectName = null;
        urlbase = null;
    }

    public String getObjectName()
    {
        return objectName;
    }

    public void setObjectName(final String objectName)
    {
        this.objectName = objectName;
    }

    public URLBase getUrlbase()
    {
        return urlbase;
    }

    public void setUrlbase(final URLBase urlbase)
    {
        this.urlbase = urlbase;
    }

    @Override
    public String toString()
    {
        return getUrlbase().toString() + objectName;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}