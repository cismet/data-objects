/*
 * QueryService.java
 *
 * Created on 30. August 2007, 16:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package de.cismet.cids.jpa.backend.service;

import de.cismet.cids.jpa.entity.query.Query;
import de.cismet.cids.jpa.entity.query.QueryStore;
import java.util.List;

/**
 *
 * @author mscholl
 */
public interface QueryService
{
    // became obsolete in favor of getEntity(Class<T>)
    //public List<Query> getAllQueries();
    
    // became obsolete in favor of store(CommonEntity)
    //public Query storeQuery(final Query q);
    
    // became obsolete in favor of delete(CommonEntity)
    //public void deleteQuery(final Query q);
    
    // became obsolete in favor of getEntity(Class<T>, int)
    //public Query getQuery(final int id);
    
    // became obsolete in favor of getEntity(Class<T>, String)
    //public Query getQuery(final String name);
    
    // became obsolete in favor of getEntity(Class<T>)
    //public List<QueryStore> getAllQueryStores();
    
    // became obsolete in favor of store(T extends CommonEntity)
    //public QueryStore storeQueryStore(final QueryStore q);
    
    // became obsolete in favor of delete(CommonEntity)
    //public void deleteQueryStore(final QueryStore q);
    
    // became obsolete in favor of getEntity(Class<T>, int)
    //public QueryStore getQueryStore(int id);
}