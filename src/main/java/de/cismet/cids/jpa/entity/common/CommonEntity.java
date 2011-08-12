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

    /**
     * Tests if one of the object's is <code>null</code> and the other is not <code>null</code>.
     *
     * @param   o1  object one
     * @param   o2  object two
     *
     * @return  <code>true</code> if object one is <code>null</code> and object two is not <code>null</code> and vice
     *          versa, <code>false</code> if both objects are <code>null</code> or both objects are not <code>
     *          null</code>.
     */
    protected boolean xorNull(final Object o1, final Object o2) {
        return ((o1 == null) && (o2 != null)) || ((o1 != null) && (o2 == null));
    }

    /**
     * Tests if both objects are equal using the {@link #equals(java.lang.Object)} operation whereas two null objects
     * are considered equal.
     *
     * @param   o1  object one
     * @param   o2  object two
     *
     * @return  <code>true</code> if both objects are <code>null</code> or if o1.equals(o2), false otherwise.
     */
    protected boolean equals(final Object o1, final Object o2) {
        if (xorNull(o1, o2)) {
            return false;
        } else {
            return isNull(o1, o2) || o1.equals(o2);
        }
    }

    /**
     * Tests if both objects are <code>null</code>.
     *
     * @param   o1  object one
     * @param   o2  object two
     *
     * @return  <code>true</code> if both objects are <code>null</code>, <code>false</code> otherwise.
     */
    protected boolean isNull(final Object o1, final Object o2) {
        return (o1 == null) && (o2 == null);
    }
}
