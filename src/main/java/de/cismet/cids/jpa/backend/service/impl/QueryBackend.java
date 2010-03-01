/*
 * QueryBackend.java
 *
 * Created on 30. August 2007, 16:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.backend.service.impl;

import de.cismet.cids.jpa.backend.core.PersistenceProvider;
import de.cismet.cids.jpa.entity.query.Query;
import de.cismet.cids.jpa.entity.query.QueryStore;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;

/**
 *
 * @author mscholl
 * @depricated
 */
public class QueryBackend
{
    private final transient Logger log = Logger.getLogger(this.getClass());
    
    private final transient PersistenceProvider provider;
    
    /** Creates a new instance of QueryBackend */
    public QueryBackend(final PersistenceProvider provider)
    {
        this.provider = provider;
    }
}