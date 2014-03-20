package com.lsgllc.norm.kernel.graph.things;

import com.lsgllc.norm.kernel.core.util.containment.INormContainer;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created By: sameloyiv
 * Date: 12/6/12
 * Time: 2:39 PM
 * <p/>
 * <p/>
 * (c) Loy Services Group, LLC. 2008-2014
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public interface INormObjectStore<K,V> extends INormContainer<K, ConcurrentSkipListSet<V>>{
}
