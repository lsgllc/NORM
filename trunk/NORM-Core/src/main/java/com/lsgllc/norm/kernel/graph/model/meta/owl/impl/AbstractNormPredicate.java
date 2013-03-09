package com.lsgllc.norm.kernel.graph.model.meta.owl.impl;

import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.core.util.containment.impl.AbstractNormContainer;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormPredicate;
import com.lsgllc.norm.util.client.INormIdentifyable;

import java.util.Set;
import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 1/7/13
 * Time: 6:22 PM
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
public  class AbstractNormPredicate<V extends Set<INormId<UUID>>> extends AbstractNormContainer<INormId<UUID>, V>  implements INormPredicate<V>,INormIdentifyable {
    private INormId<UUID> id = null;

    protected AbstractNormPredicate(INormId id) {
        super();
    }

    @Override
    public INormId<UUID> getId() {
        return this.id;
    }

    @Override
    public void setId(Object id) {

    }

    @Override
    public boolean op() {
        return false;
    }

//    @Override
//    public abstract boolean op();
}
