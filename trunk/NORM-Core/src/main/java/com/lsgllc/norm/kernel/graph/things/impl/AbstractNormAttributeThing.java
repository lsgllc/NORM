package com.lsgllc.norm.kernel.graph.things.impl;

import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;
import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.AttributeId;
import com.lsgllc.norm.kernel.graph.things.INormAttribute;
import com.lsgllc.norm.util.exceptions.NormNotFoundException;

/**
 * Created By: sameloyiv
 * Date: 1/7/13
 * Time: 3:49 PM
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
public class AbstractNormAttributeThing<K extends INormId<?>,V extends INormThing<K,?>> extends AbstractNormThing<K,V> implements INormAttribute<K,V> {
    private V thing = null;
    public AbstractNormAttributeThing(K id) {
        super(id);
    }

    protected AbstractNormAttributeThing(K id, V thing) {
        super(id, thing);
    }

    public AbstractNormAttributeThing(V thing) {
        super((K) new AttributeId(), thing);
    }

    public V getThing() {
        return thing;
    }

    public void setThing(V thing) {
        this.thing = thing;
    }
}
