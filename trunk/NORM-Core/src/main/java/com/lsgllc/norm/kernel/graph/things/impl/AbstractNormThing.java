package com.lsgllc.norm.kernel.graph.things.impl;

import com.lsgllc.norm.kernel.core.util.containment.impl.AbstractNormContainer;
import com.lsgllc.norm.kernel.graph.model.instance.impl.NormScalarValue;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.typing.INormType;
import com.lsgllc.norm.util.client.INormIdentifyable;
import com.lsgllc.norm.util.exceptions.NormNotFoundException;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created By: sameloyiv
 * Date: 1/4/13
 * Time: 11:57 AM
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
public abstract class AbstractNormThing<K ,V extends INormThing<?,?>> extends AbstractNormContainer<K,ConcurrentSkipListSet<V>> implements INormThing<K,V>,INormIdentifyable<K>{

    private K id;
    private V thing;
    private INormType type;


    protected AbstractNormThing(K id) {
        this.id = id;
    }

    public AbstractNormThing(K id, V thing) {
        this(id);
        this.thing = thing;
    }

    @Override
    public V getThing() throws NormNotFoundException {
        return this.thing;
    }

    @Override
    public void setThing(V thing) {
        this.thing = thing;
    }

    @Override
    public K getId() {
        return this.id;
    }

    @Override
    public void setId(K id) {

    }



}
