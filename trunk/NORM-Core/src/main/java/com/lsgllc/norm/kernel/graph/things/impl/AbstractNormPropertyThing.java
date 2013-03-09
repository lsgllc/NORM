package com.lsgllc.norm.kernel.graph.things.impl;

import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;
import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.PropertyId;
import com.lsgllc.norm.kernel.graph.things.INormProperty;

/**
 * Created By: sameloyiv
 * Date: 1/7/13
 * Time: 3:56 PM
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
public class AbstractNormPropertyThing< K extends INormId<ELEMENT_TYPES>,V extends INormThing<K,?>> extends AbstractNormThing<K,V> implements INormProperty<K,V> {

    public AbstractNormPropertyThing(K id) {
        super(id);
    }

    protected AbstractNormPropertyThing(K id, V thing) {
        super(id, thing);
    }

    public AbstractNormPropertyThing(V thing) {
        super((K) new PropertyId(), thing);
    }
}
