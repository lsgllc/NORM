package com.lsgllc.norm.kernel.graph.things.impl;

import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.core.util.identity.INormId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.ObjectStoreId;
import com.lsgllc.norm.kernel.graph.things.INormObjectStore;

/**
 * Created By: sameloyiv
 * Date: 1/7/13
 * Time: 4:21 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2013
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public class AbstractNormObjectStoreThing< K extends INormId<?>,V extends INormThing<K,?>> extends AbstractNormThing<K,V> implements INormObjectStore<K,V> {


    public AbstractNormObjectStoreThing(K id) {
        super(id);
    }

    protected AbstractNormObjectStoreThing(K id, V thing) {
        super(id, thing);
    }

    public AbstractNormObjectStoreThing(V thing) {
        super((K) new ObjectStoreId(), thing);
    }
}
