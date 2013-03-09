package com.lsgllc.norm.kernel.core.util.containment.impl;

import com.lsgllc.norm.kernel.core.util.containment.INormContainer;

/**
 * Created By: sameloyiv
 * Date: 12/6/12
 * Time: 12:39 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */

public  class AbstractNormContainer<K,V > extends AbstractNormHashMap<K,V> implements INormContainer<K, V> {
    private String entityURI;

    protected AbstractNormContainer() {
    }


    @Override
    public V getEntityURI() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractNormContainer)) return false;
        if (!super.equals(o)) return false;

        AbstractNormContainer that = (AbstractNormContainer) o;

        if (!entityURI.equals(that.entityURI)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + entityURI.hashCode();
        return result;
    }
}
