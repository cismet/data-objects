/*
 * CommonService.java
 *
 * Created on 11. Februar 2008, 10:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.common.CommonEntity;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author mscholl
 */
public interface CommonService
{
    public <T extends CommonEntity> T store(final T entity);
    
    public void delete(final CommonEntity ce);
    
    public <T extends CommonEntity> T getEntity(final Class<T> entity, final int
            id) throws NoResultException;
    
    public <T extends CommonEntity> List<T> getAllEntities(final Class<T> 
            entity);
    
    // assumes entity has a mapped member named 'name'
    public <T extends CommonEntity> T getEntity(final Class<T> entity, final 
            String name) throws NoResultException;
    
    // assumes entity has a mapped member named 'name'
    public <T extends CommonEntity> boolean contains(final Class<T> entity, 
            final String name);
}
