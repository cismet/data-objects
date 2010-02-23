/*
 * CommonEntity.java
 *
 * Created on 23. Januar 2008, 16:48
 *
 */

package de.cismet.cids.jpa.entity.common;

import java.io.Serializable;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author $Author: mscholl $
 * @version $Revision: 1.3 $
 * tag $Name:  $
 * date $Date: 2008/04/23 10:24:26 $
 */
@MappedSuperclass
public abstract class CommonEntity implements Serializable
{
    public abstract Integer getId();
    
    public abstract void setId(final Integer id);

    @Override
    public boolean equals(final Object o)
    {
        if(!(o instanceof CommonEntity))
            return false;
        final CommonEntity theOther = (CommonEntity)o;
        if(getId() == null && theOther.getId() == null)
            return this == theOther;
        if(getId() == null || theOther.getId() == null)
            return false;
        if(getId().equals(theOther.getId()))
            return true;
        return false;
    }

    @Override
    public int hashCode()
    {
        if(getId() == null)
            return super.hashCode();
        return getId().hashCode();
    }
}