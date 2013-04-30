package com.lsgllc.norm.kernel.graph.model.meta.owl.impl;

import com.lsgllc.norm.kernel.core.util.identity.INormId;
import com.lsgllc.norm.kernel.core.util.containment.impl.AbstractNormContainer;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormTriple;
import com.lsgllc.norm.util.client.INormIdentifyable;

import java.util.Set;
import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 1/19/13
 * Time: 3:50 PM
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
public class NormTriple<V extends Set<INormId<UUID>>> extends AbstractNormContainer<INormId<UUID>, V> implements INormTriple<V>,INormIdentifyable {
    public NormTriple(INormId id) {
        super();
    }

    @Override
    public INormId<UUID> getId() {
        return null;
    }

    @Override
    public void setId(Object id) {

    }


    @Override
    public Set<V> getAllContainedBy() {
        return null;
    }

    @Override
    public Set<V> getAllContainedIn() {
        return null;
    }

    @Override
    public Set<V> getAllReferencedBy() {
        return null;
    }

    @Override
    public V put(INormId<UUID> uuidiNormId, V iNormIds) {
        return null;
    }

}
