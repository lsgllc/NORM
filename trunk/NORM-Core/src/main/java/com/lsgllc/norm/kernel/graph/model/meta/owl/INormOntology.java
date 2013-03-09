package com.lsgllc.norm.kernel.graph.model.meta.owl;

import com.lsgllc.norm.kernel.core.util.containment.INormContainer;

import java.io.Serializable;
import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 1/3/13
 * Time: 10:26 AM
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
public interface INormOntology<K,V> extends INormContainer<K, Set<V>>,Serializable {
}
