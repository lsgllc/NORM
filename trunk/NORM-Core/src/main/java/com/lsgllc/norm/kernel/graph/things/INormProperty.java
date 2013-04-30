package com.lsgllc.norm.kernel.graph.things;

import com.lsgllc.norm.kernel.core.util.containment.INormContainer;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created By: sameloyiv
 * Date: 12/6/12
 * Time: 2:21 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public interface INormProperty<K,V> extends INormContainer<K, ConcurrentSkipListSet<V>> {
}
