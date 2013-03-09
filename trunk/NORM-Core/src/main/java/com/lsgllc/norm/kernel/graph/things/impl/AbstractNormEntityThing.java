package com.lsgllc.norm.kernel.graph.things.impl;

import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;
import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.EntityId;
import com.lsgllc.norm.kernel.graph.things.INormEntity;

/**
 * Created By: sameloyiv
 * Date: 2/3/13
 * Time: 12:25 PM
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
public class AbstractNormEntityThing< K extends INormId<?>,V extends INormThing<K,?>> extends AbstractNormThing<K,V> implements INormEntity<K,V> {

    public AbstractNormEntityThing(K id) {
        super(id);
    }

    protected AbstractNormEntityThing(K id, V thing) {
        super(id, thing);
    }

    public AbstractNormEntityThing(V thing) {
        super((K) new EntityId(), thing);
    }
    @Override
    public void addAttributeThing(V attributeThing) {

    }
}
