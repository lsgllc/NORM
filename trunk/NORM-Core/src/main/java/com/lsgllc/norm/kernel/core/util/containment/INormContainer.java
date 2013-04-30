package com.lsgllc.norm.kernel.core.util.containment;

import com.lsgllc.norm.kernel.core.util.identity.INormContainerMarker;

import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Created By: sameloyiv
 * Date: 12/6/12
 * Time: 12:37 PM
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
public interface INormContainer<K, V > extends ConcurrentNavigableMap<K,V>, INormContainerMarker<K,V> {
    V getEntityURI();

}
