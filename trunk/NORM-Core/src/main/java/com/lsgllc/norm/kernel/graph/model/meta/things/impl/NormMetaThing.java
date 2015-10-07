package com.lsgllc.norm.kernel.graph.model.meta.things.impl;

import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.PropertyId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.ValueId;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormPredicate;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormTriple;
import com.lsgllc.norm.kernel.graph.model.meta.things.INormMetaThing;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormThing;

import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 2/4/13
 * Time: 10:20 AM
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
public class NormMetaThing<K extends ValueId,V extends INormThing<K,PropertyId>> extends AbstractNormThing<K,V> implements INormMetaThing{
    public NormMetaThing(K id) {
        super(id);
    }

    protected NormMetaThing(K id, V thing) {
        super(id, thing);
    }

    @Override
    public Set<INormPredicate> getPredicates() {
        return null;
    }

    @Override
    public Set<INormTriple> getTriples() {
        return null;
    }

    @Override
    public INormPredicate addPredicate(INormPredicate predicate) {
        return null;
    }

    @Override
    public INormTriple addTriple(INormTriple triple) {
        return null;
    }
}
