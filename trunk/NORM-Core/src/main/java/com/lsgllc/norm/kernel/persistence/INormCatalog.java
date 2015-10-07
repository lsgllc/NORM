package com.lsgllc.norm.kernel.persistence;

import com.lsgllc.norm.kernel.core.util.containment.INormContainer;
import com.lsgllc.norm.kernel.graph.model.meta.identity.IOntologyId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.OntologyId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.OntologySegmentId;
import com.lsgllc.norm.kernel.graph.things.INormObject;
import com.lsgllc.norm.kernel.graph.things.INormThing;

/**
 * Created By: sameloyiv
 * Date: 10/10/14
 * Time: 12:39 PM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2014
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public interface INormCatalog <K extends IOntologyId<INormObject>,V extends INormThing<K,V>> extends INormContainer<K, V> {
}
