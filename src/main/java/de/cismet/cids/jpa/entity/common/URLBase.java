/*
 * URLBase.java
 *
 * Created on 24. January 2008, 15:46
 */

package de.cismet.cids.jpa.entity.common;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * @author mscholl
 * @version 1.0
 */
@Entity()
@Table(name = "url_base")
public class URLBase extends CommonEntity implements Serializable
{
    @Id
    @SequenceGenerator(name = "url_base_seq",
            sequenceName = "url_base_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, 
            generator = "url_base_seq")
    @Column(name = "id")
    private Integer id;
 
    @Column(name = "prot_prefix")
    private String protocolPrefix;
    
    @Column(name = "server")
    private String server;

    @Column(name = "path")
    private String path;
    
    public URLBase()
    {
        protocolPrefix = null;
        server = null;
        path = null;
    }

    public String getProtocolPrefix()
    {
        return protocolPrefix;
    }

    public void setProtocolPrefix(final String protocolPrefix)
    {
        this.protocolPrefix = protocolPrefix;
    }

    public String getServer()
    {
        return server;
    }

    public void setServer(final String server)
    {
        this.server = server;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(final String path)
    {
        this.path = path;
    }
    
    @Override
    public String toString()
    {
        return protocolPrefix + server + path;
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