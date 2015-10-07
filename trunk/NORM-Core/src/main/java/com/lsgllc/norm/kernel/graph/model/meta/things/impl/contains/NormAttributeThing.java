package com.lsgllc.norm.kernel.graph.model.meta.things.impl.contains;

import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.AttributeId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.PropertyId;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormAttributeThing;

/**
 * Created By: sameloyiv
 * Date: 2/4/13
 * Time: 10:00 AM
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
public class NormAttributeThing<K extends AttributeId,V extends INormThing<K,PropertyId>> extends AbstractNormAttributeThing<K,V> {
    public NormAttributeThing(K id) {
        super(id);
    }

    protected NormAttributeThing(K id, V thing) {
        super(id, thing);
    }

    public NormAttributeThing(V thing) {
        super(thing);
    }
}
