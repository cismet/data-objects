/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.backend.service.impl;

import org.apache.log4j.Logger;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.cismet.cids.jpa.backend.core.PersistenceProvider;
import de.cismet.cids.jpa.backend.service.ConfigAttrService;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrEntry;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrKey;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrType;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrType.Types;
import de.cismet.cids.jpa.entity.configattr.ConfigAttrValue;

/**
 * DOCUMENT ME!
 *
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class ConfigAttrBackend implements ConfigAttrService {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(ConfigAttrBackend.class);

    //~ Instance fields --------------------------------------------------------

    private final transient PersistenceProvider provider;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ConfigAttrBackend object.
     *
     * @param  provider  DOCUMENT ME!
     */
    public ConfigAttrBackend(final PersistenceProvider provider) {
        this.provider = provider;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public List<ConfigAttrEntry> getEntries(final ConfigAttrKey key) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("get all config attr entries for key: " + key); // NOI18N
        }

        final EntityManager manager = provider.getEntityManager();
        final Query q = manager.createQuery("FROM ConfigAttrEntry cae WHERE cae.key = :key"); // NOI18N
        q.setParameter("key", key);                                                           // NOI18N

        return q.getResultList();
    }

    @Override
    public List<ConfigAttrEntry> getEntries(final Types type) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("get all config attr entries for type: " + type); // NOI18N
        }

        final EntityManager manager = provider.getEntityManager();
        final Query q = manager.createQuery("FROM ConfigAttrEntry cae WHERE cae.type = :type"); // NOI18N
        q.setParameter("type", getType(type));                                                  // NOI18N

        return q.getResultList();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   type  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private ConfigAttrType getType(final Types type) {
        final EntityManager manager = provider.getEntityManager();
        final Query q = manager.createQuery("FROM ConfigAttrType cat WHERE cat.type = :type"); // NOI18N
        q.setParameter("type", ConfigAttrType.getType(type));                                  // NOI18N

        return (ConfigAttrType)q.getSingleResult();
    }

    @Override
    public ConfigAttrEntry storeEntry(final ConfigAttrEntry entry) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("store entry: " + entry); // NOI18N
        }

        final EntityManager manager = provider.getEntityManager();

        ConfigAttrKey key = entry.getKey();
        if (key.getId() == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("found new key: " + key);                                             // NOI18N
            }
            final Query q = manager.createQuery("FROM ConfigAttrKey cak WHERE cak.key = :key"); // NOI18N
            q.setParameter("key", key.getKey());                                                // NOI18N

            try {
                final ConfigAttrKey existing = (ConfigAttrKey)q.getSingleResult();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("key exists, using it: " + existing); // NOI18N
                }

                key = existing;
            } catch (final NoResultException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("key does not exist, using transient instance: " + key, e); // NOI18N
                }
                // skip
            }
        }

        ConfigAttrValue value = entry.getValue();
        if (value.getId() == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("found new value: " + value);                                               // NOI18N
            }
            final Query q = manager.createQuery("FROM ConfigAttrValue cav WHERE cav.value = :value"); // NOI18N
            q.setParameter("value", value.getValue());                                                // NOI18N

            try {
                final ConfigAttrValue existing = (ConfigAttrValue)q.getSingleResult();

                if (LOG.isDebugEnabled()) {
                    LOG.debug("value exists, using it: " + existing); // NOI18N
                }

                value = existing;
            } catch (final NoResultException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("value does not exist, using transient instance: " + value, e); // NOI18N
                }
                // skip
            }
        }

        entry.setKey(key);
        entry.setValue(value);

        return provider.store(entry);
    }

    @Override
    public void cleanAttributeTables() {
        final EntityManager manager = provider.getEntityManager();

        final Query keyQuery = manager.createQuery(
                "FROM ConfigAttrKey cak WHERE cak NOT IN (SELECT cae.key FROM ConfigAttrEntry cae)"); // NOI18N
        final List<ConfigAttrKey> orphanKeys = keyQuery.getResultList();
        for (final ConfigAttrKey orphan : orphanKeys) {
            provider.delete(orphan);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("deleted " + orphanKeys.size() + " ConfigAttrKey orphans");                     // NOI18N
        }

        final Query valueQuery = manager.createQuery(
                "FROM ConfigAttrValue cav WHERE cav NOT IN (SELECT cae.value FROM ConfigAttrEntry cae)"); // NOI18N
        final List<ConfigAttrValue> orphanValues = valueQuery.getResultList();
        for (final ConfigAttrValue orphan : orphanValues) {
            provider.delete(orphan);
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("deleted " + orphanValues.size() + " ConfigAttrValue orphans");                     // NOI18N
        }
    }

    @Override
    public void close() throws Exception {
        cleanAttributeTables();
    }

    @Override
    public List<ConfigAttrEntry> getEntries(final ConfigAttrKey key, final Types type) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("get all config attr entries for key '" + key + "' and type '" + type + "'"); // NOI18N
        }

        final EntityManager manager = provider.getEntityManager();
        final Query q = manager.createQuery("FROM ConfigAttrEntry cae WHERE cae.key = :key AND cae.type = :type"); // NOI18N
        q.setParameter("key", key);                                                                                // NOI18N
        q.setParameter("type", getType(type));                                                                     // NOI18N

        return q.getResultList();
    }

    @Override
    public boolean contains(final ConfigAttrEntry entry) {
        if (entry == null) {
            return false;
        }

        final EntityManager manager = provider.getEntityManager();
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<ConfigAttrEntry> cq = cb.createQuery(ConfigAttrEntry.class);
        final Root<ConfigAttrEntry> root = cq.from(ConfigAttrEntry.class);

        cq.where(cb.and(
                cb.equal(root.get("domain"), entry.getDomain()),                                        // NOI18N
                cb.equal(root.get("key"), entry.getKey()),                                              // NOI18N
                (entry.getUsergroup() == null) ? cb.isNull(root.get("usergroup"))                       // NOI18N
                                               : cb.equal(root.get("usergroup"), entry.getUsergroup()), // NOI18N
                (entry.getUser() == null) ? cb.isNull(root.get("user")) : cb.equal(root.get("user"), entry.getUser()))); // NOI18N

        try {
            manager.createQuery(cq).getSingleResult();

            return true;
        } catch (final NoResultException e) {
            return false;
        }
    }

    @Override
    public boolean contains(final ConfigAttrKey key) {
        if (key == null) {
            return false;
        }

        final EntityManager manager = provider.getEntityManager();
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<ConfigAttrKey> cq = cb.createQuery(ConfigAttrKey.class);
        final Root<ConfigAttrKey> root = cq.from(ConfigAttrKey.class);

        cq.where(cb.equal(root.get("key"), key.getKey()));

        try {
            manager.createQuery(cq).getSingleResult();

            return true;
        } catch (final NoResultException e) {
            return false;
        }
    }
}
