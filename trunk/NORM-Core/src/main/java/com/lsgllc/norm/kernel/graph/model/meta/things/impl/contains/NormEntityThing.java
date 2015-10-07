package com.lsgllc.norm.kernel.graph.model.meta.things.impl.contains;

import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.AttributeId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.EntityId;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormEntityThing;

/**
 * Created By: sameloyiv
 * Date: 2/4/13
 * Time: 10:12 AM
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
public class NormEntityThing<K extends EntityId,V extends INormThing<K,AttributeId>> extends AbstractNormEntityThing<K,V> {
    public NormEntityThing(K id) {
        super(id);
    }

    protected NormEntityThing(K id, V thing) {
        super(id, thing);
    }

    public NormEntityThing(V thing) {
        super(thing);
    }
}
