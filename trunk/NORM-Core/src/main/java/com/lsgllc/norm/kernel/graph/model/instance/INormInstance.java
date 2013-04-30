package com.lsgllc.norm.kernel.graph.model.instance;

import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.util.client.INormIdentifyable;

import java.io.Serializable;

/**
 * Created By: sameloyiv
 * Date: 2/3/13
 * Time: 2:19 PM
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
public interface INormInstance<K,V,T>  extends INormThing<K,V>,INormIdentifyable<K>,Serializable {
    void setValue(T value);
    T getValue();
}
