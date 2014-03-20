package com.lsgllc.norm.kernel.core.util.containment.impl;

import java.util.HashMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created By: sameloyiv
 * Date: 12/14/12
 * Time: 11:16 AM
 * <p/>
 * <p/>
 * (c) Loy Services Group, LLC. 2008-2014
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public abstract class AbstractNormHashMap<K ,V> extends ConcurrentSkipListMap<K,V> {
    enum RELATIONSHIP {HAS_ONE_OR_MORE, HAS_ZERO_OR_MORE, IS_CONTAINED_BY, REFERENCED_BY}

    private V theThing = null;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractNormHashMap)) return false;
        if (!super.equals(o)) return false;

        AbstractNormHashMap that = (AbstractNormHashMap) o;

        if (!theThing.equals(that.theThing)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + theThing.hashCode();
        return result;
    }
}
