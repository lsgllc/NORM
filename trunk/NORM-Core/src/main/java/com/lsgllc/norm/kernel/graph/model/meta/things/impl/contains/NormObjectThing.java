package com.lsgllc.norm.kernel.graph.model.meta.things.impl.contains;

import com.lsgllc.norm.kernel.graph.model.meta.identity.EntityId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.ObjectId;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormObjectThing;

/**
 * Created By: sameloyiv
 * Date: 2/4/13
 * Time: 10:11 AM
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
public class NormObjectThing<K extends ObjectId,V extends INormThing<K,EntityId>> extends AbstractNormObjectThing<K,V> {
    public NormObjectThing(K id) {
        super(id);
    }

    protected NormObjectThing(K id, V thing) {
        super(id, thing);
    }

    public NormObjectThing(V thing) {
        super(thing);
    }
}
