package com.lsgllc.norm.kernel.graph.model.meta.things.impl.containedby;

import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.ObjectInstanceId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.ObjectStoreId;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormObjectStoreThing;

/**
 * Created By: sameloyiv
 * Date: 2/4/13
 * Time: 10:09 AM
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
public class NormObjectStoreThing<K extends ObjectStoreId,V extends INormThing<K,ObjectInstanceId>> extends AbstractNormObjectStoreThing<K,V> {
    public NormObjectStoreThing(K id) {
        super(id);
    }

    protected NormObjectStoreThing(K id, V thing) {
        super(id, thing);
    }

    public NormObjectStoreThing(V thing) {
        super(thing);
    }
}
