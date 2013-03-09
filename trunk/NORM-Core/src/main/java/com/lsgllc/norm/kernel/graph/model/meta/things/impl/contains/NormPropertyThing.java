package com.lsgllc.norm.kernel.graph.model.meta.things.impl.contains;

import com.lsgllc.norm.kernel.graph.model.meta.identity.PropertyId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.ValueId;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormPropertyThing;

/**
 * Created By: sameloyiv
 * Date: 2/4/13
 * Time: 10:19 AM
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
public class NormPropertyThing <K extends PropertyId,V extends INormThing<K,ValueId>> extends AbstractNormPropertyThing<K,V> {
    public NormPropertyThing(K id) {
        super(id);
    }

    public NormPropertyThing(K id, V thing) {
        super(id, thing);
    }

    public NormPropertyThing(V thing) {
        super(thing);
    }
}
