/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.cids.jpa.entity.common;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * DOCUMENT ME!
 *
 * @author   mscholl
 * @version  1.3
 */
@MappedSuperclass
public abstract class CommonEntity implements Serializable {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract Integer getId();

    /**
     * DOCUMENT ME!
     *
     * @param  id  DOCUMENT ME!
     */
    public abstract void setId(final Integer id);

    /**
     * {@inheritDoc}<br/>
     * <br/>
     * Additional comments: the equals may be too general for some usecases. Please reimplement if necessary.
     */
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof CommonEntity)) {
            return false;
        }
        final CommonEntity theOther = (CommonEntity)o;
        if ((getId() == null) && (theOther.getId() == null)) {
            return this == theOther;
        }
        if ((getId() == null) || (theOther.getId() == null)) {
            return false;
        }
        if (getId().equals(theOther.getId())) {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        }

        return getId().hashCode();
    }
}
