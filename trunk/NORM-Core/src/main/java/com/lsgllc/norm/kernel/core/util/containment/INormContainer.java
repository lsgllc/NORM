package com.lsgllc.norm.kernel.core.util.containment;

import com.lsgllc.norm.kernel.graph.identity.INormContainerMarker;

import java.util.Map;

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
public interface INormContainer<K, V > extends Map<K,V>, INormContainerMarker<K,V> {
    V getEntityURI();

}
