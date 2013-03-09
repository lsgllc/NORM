package com.lsgllc.norm.kernel.graph.model.meta.owl;

import com.lsgllc.norm.kernel.core.util.containment.INormContainer;
import com.lsgllc.norm.kernel.graph.identity.INormId;

import java.util.Set;
import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 1/4/13
 * Time: 1:50 PM
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
 * predicate types: Entity, Attribute, property, value, triple
 */
public interface INormPredicate <V extends Set<INormId<UUID>>> extends INormContainer<INormId<UUID>, V> {
    enum PREDICATES {ALLOWS_NULLS, HAS_AT_LEAST_ONE, HAS_ONLY_ONE, HAS_ZERO_OR_MORE, CREATED_UNDER, REFERENCED_BY}
    boolean op();

}
