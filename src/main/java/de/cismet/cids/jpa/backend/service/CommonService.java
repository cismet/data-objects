/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service;

import com.mchange.v1.util.ClosableResource;

import java.util.List;

import javax.persistence.NoResultException;

import de.cismet.cids.jpa.entity.common.CommonEntity;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  $Revision$, $Date$
 */
public interface CommonService extends ClosableResource {

    //~ Methods ----------------------------------------------------------------

    /**
     * Stores a subtype of {@link CommonEntity}.
     *
     * @param   <T>     a subtype of <code>CommonEntity</code>
     * @param   entity  the subtype of <code>CommonEntity</code> to store
     *
     * @return  the now stored subtype of <code>CommonEntity</code>
     */
    <T extends CommonEntity> T store(final T entity);

    /**
     * Deletes a {@link CommonEntity}.
     *
     * @param  ce  the <code>CommonEntity</code> to delete
     */
    void delete(final CommonEntity ce);

    /**
     * Deletes all {@link CommonEntity}s in the list's order.
     *
     * @param  entities  the <code>CommonEntity</code>s to delete
     */
    void delete(final List<CommonEntity> entities);

    /**
     * Retrieves the desired subtype of {@link CommonEntity} with the given <code>id</code>. If there is no entity of
     * the desired type and with the given <code>id</code> a {@link NoResultException} shall be thrown.
     *
     * @param   <T>     a subtype of <code>CommonEntity</code>
     * @param   entity  the subtype of <code>CommonEntity</code> to store
     * @param   id      the <code>id</code> of the entity
     *
     * @return  the desired entity of type <code>T</code> with the given <code>id</code>
     *
     * @throws  NoResultException  if no entity of the desired type and with the given <code>id</code> is present
     */
    <T extends CommonEntity> T getEntity(final Class<T> entity, final int id) throws NoResultException;

    /**
     * Retrieves all entities of the desired subtype of {@link CommonEntity}. If there exist no entities of the desired
     * type an empty {@link List} shall be retured.
     *
     * @param   <T>     a subtype of <code>CommonEntity</code>
     * @param   entity  the subtype of <code>CommonEntity</code> that shall be retrieved
     *
     * @return  a <code>List</code> containing all entities of type <code>T</code>, never null.
     */
    <T extends CommonEntity> List<T> getAllEntities(final Class<T> entity);

    /**
     * Retrieves the desired subtype of {@link CommonEntity} with the given <code>name</code>. If there is no entity of
     * the desired type and with the given <code>name</code> a {@link NoResultException} shall be thrown.
     *
     * @param   <T>     a subtype of <code>CommonEntity</code>
     * @param   entity  the subtype of <code>CommonEntity</code> that shall be retrieved
     * @param   name    the <code>name</code> of the entity
     *
     * @return  the desired entity of type <code>T</code> with the given <code>name</code>
     *
     * @throws  NoResultException  if no entity of the desired type and with the given <code>name</code> can be found
     */
    <T extends CommonEntity> T getEntity(final Class<T> entity, final String name) throws NoResultException;

    /**
     * Checks whether an entity of type <code>T</code> with the given <code>name</code> exists or not.
     *
     * @param   <T>     a subtype of <code>CommonEntity</code>
     * @param   entity  the subtype of <code>CommonEntity</code> that shall be retrieved
     * @param   name    the <code>name</code> of the entity
     *
     * @return  true if an entity of type <code>T</code> with the given <code>name</code> exists, false otherwise
     */
    <T extends CommonEntity> boolean contains(final Class<T> entity, final String name);
}
