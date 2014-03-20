package com.lsgllc.norm.kernel.core.util.containment.impl;

import com.lsgllc.norm.kernel.core.util.containment.INormContainer;

/**
 * Created By: sameloyiv
 * Date: 12/6/12
 * Time: 12:39 PM
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

public  class AbstractNormContainer<K,V > extends AbstractNormHashMap<K,V> implements INormContainer<K, V> {
    private String entityURI;

    protected AbstractNormContainer() {
    }


    @Override
    public V getEntityURI() {
        return null;
    }

}
