package com.lsgllc.norm.kernel.graph.things;

import com.lsgllc.norm.kernel.graph.model.meta.owl.INormPredicate;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormTriple;
import com.lsgllc.norm.util.client.INormIdentifyable;
import com.lsgllc.norm.util.exceptions.NormNotFoundException;

import java.io.Serializable;
import java.util.Set;

/**
 * Created By: sameloyiv
 * Date: 1/4/13
 * Time: 11:47 AM
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
public interface INormThing<K,V> extends Serializable {
    V getThing() throws NormNotFoundException;
    void setThing(V thing);
}
