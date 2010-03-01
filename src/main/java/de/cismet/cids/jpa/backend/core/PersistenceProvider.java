/*
 * PersistenceProvider.java
 *
 * Created on 6. Februar 2008, 14:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.backend.core;

import de.cismet.cids.jpa.backend.service.CommonService;
import de.cismet.cids.jpa.entity.common.CommonEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.apache.log4j.Logger;

/**
 *
 * @author mscholl
 */
public final class PersistenceProvider implements CommonService
{
    private static final transient Logger LOG = Logger.getLogger(
                                                    PersistenceProvider.class);
    
    final transient EntityManagerFactory emf;
    final transient ThreadLocal<EntityManager> em;
    private final transient Properties runtimeProperties;
    private transient boolean closed;
    
    /** Creates a new instance of PersistenceProvider */
    public PersistenceProvider(final Properties runtimeProperties) throws 
                                                        IllegalArgumentException
    {
        closed = false;
        this.runtimeProperties = runtimeProperties;
        this.em = new ThreadLocal<EntityManager>();
        emf = Persistence.createEntityManagerFactory("cidsDataObjectsPU", 
                                             getPropertyMap(runtimeProperties));
    }

    // cannot be reopened
    public void close()
    {
        closed = true;
        emf.close();
        
        if(LOG.isInfoEnabled())
        {
            LOG.info("PersistenceProvider closed: " + this);
        }
    }
    
    public Properties getRuntimeProperties()
    {
        return runtimeProperties;
    }

    public EntityManager getEntityManager()
    {
        if(closed)
        {
            throw new IllegalStateException("Provider already closed");
        }
        
        return em.get();
    }
    
    private Map getPropertyMap(final Properties p)
    {
        LOG.debug("building property map: start");
        try
        {
            final Map map = new HashMap();
            String currentProp = p.getProperty("dialect");
            if(currentProp == null || currentProp.trim().equals(""))
            {
                throw new IllegalArgumentException(
                                    "dialect is not set in runtime properties");
            }

            map.put("hibernate.dialect", currentProp);
            currentProp = p.getProperty("connection.driver_class");
            if(currentProp == null || currentProp.trim().equals(""))
            {
                throw new IllegalArgumentException(
                               "driver_class is not set in runtime properties");
            }

            map.put("hibernate.connection.driver_class", currentProp);
            currentProp = p.getProperty("connection.username");
            if(currentProp == null || currentProp.trim().equals(""))
            {
                throw new IllegalArgumentException(
                                   "username is not set in runtime properties");
            }

            map.put("hibernate.connection.username", currentProp);
            currentProp = p.getProperty("connection.password");
            if(currentProp == null || currentProp.trim().equals(""))
            {
                throw new IllegalArgumentException(
                                   "password is not set in runtime properties");
            }

            map.put("hibernate.connection.password", currentProp);
            currentProp = p.getProperty("connection.url");
            if(currentProp == null || currentProp.trim().equals(""))
            {
                throw new IllegalArgumentException(
                                        "url is not set in runtime properties");
            }

            map.put("hibernate.connection.url", currentProp);
            // TODO: move to runtime.properties
            map.put("hibernate.connection.provider_class", "org.hibernate." +
                    "connection.C3P0ConnectionProvider");
            map.put("c3p0.initialPoolSize", "10");
            map.put("c3p0.minPoolSize", "5");
            map.put("c3p0.maxPoolSize", "50");
            map.put("c3p0.checkoutTimeout", "1000");
            map.put("c3p0.maxStatements", "500");
            map.put("c3p0.maxIdleTimeExcessConnections", "30");
            map.put("c3p0.idleConnectionTestPeriod", "300");
            map.put("c3p0.acquireIncrement", "5");
            map.put("c3p0.numHelperThreads", "5");
            map.put("hibernate.show_sql", "false");
            LOG.debug("building property map: end");
            return map;
        }catch(final Exception t)
        {
            LOG.error("could not set property hibernate.connection.url, " +
                    "will use environment settings", t);
            throw new IllegalArgumentException("connection properties could " +
                    "not be set, probably erroneous runtime properties", t);
        }
    }

    @Override
    public <T extends CommonEntity> T store(final T entity)
    {
        final EntityManager e = getEntityManager();
        T ret = null;
        if(e.contains(entity))
        {
            e.persist(entity);
        } else
        {
            ret = e.merge(entity);
        }

        return ret == null ? entity : ret;
    }

    @Override
    public void delete(final CommonEntity ce)
    {
        final EntityManager e = getEntityManager();
        final Object toDelete;
        if(e.contains(ce))
        {
            toDelete = ce;
        } else
        {
            toDelete = e.merge(ce);
        }
        e.remove(toDelete);
    }

    @Override
    public <T extends CommonEntity> T getEntity(final Class<T> entity, 
                                                final int id)
                                                throws NoResultException
    {
        final EntityManager entityManager = getEntityManager();
        final T e = entityManager.find(entity, id);
        // throw exception to be consistent with em.getSingleResult
        if(e == null)
        {
            throw new NoResultException("'" + entity.getSimpleName() + 
                                       "' with id '" + id + "' does not exist");
        }
        
        return e;
    }

    @Override
    public <T extends CommonEntity> List<T> getAllEntities(
                                                          final Class<T> entity)
    {
        final EntityManager entityManager = getEntityManager();
        final Query q = entityManager.createQuery("FROM " +
                                                  entity.getSimpleName());
        return  q.getResultList();
    }
    
    // assumes entity has a mapped member named 'name'
    @Override
    public <T extends CommonEntity> T getEntity(final Class<T> entity, 
                                                final String   name)
                                               throws NoResultException
    {
        final EntityManager entityManager = getEntityManager();
        final Query q = entityManager.createQuery("FROM " + 
                                                  entity.getSimpleName() +
                                                  " WHERE name = :name");
        q.setParameter("name", name);
        
        return (T)q.getSingleResult();
    }
    
    // assumes entity has a mapped member named 'name'
    @Override
    public <T extends CommonEntity> boolean contains(final Class<T> entity, 
                                                     final String name)
    {
        try
        {
            getEntity(entity, name);
            return true;
        }catch(final NoResultException ex)
        {
            return false;
        }
    }
}