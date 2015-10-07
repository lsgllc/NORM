package com.lsgllc.norm.kernel.graph.things.impl;

import com.lsgllc.norm.kernel.graph.model.meta.identity.impl.OntologySegmentId;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormOntologySegment;
import com.lsgllc.norm.kernel.graph.things.INormThing;

/**
 * Created By: sameloyiv
 * Date: 12/6/12
 * Time: 1:39 PM
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
public  class AbstractOntologySegment<K extends OntologySegmentId,V extends INormThing<K,?>> extends AbstractNormThing<K,V> implements INormOntologySegment<K,V> {
    protected  String uri;
    protected V entities = null;


    public AbstractOntologySegment(K id) {
        super(id);
    }

    protected AbstractOntologySegment(K id, V thing) {
        super(id, thing);
    }

    public AbstractOntologySegment(V thing) {
        super((K) new OntologySegmentId(), thing);
    }
}
