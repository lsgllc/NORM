package com.lsgllc.norm.kernel.graph.things.impl;

import com.lsgllc.norm.kernel.graph.model.meta.identity.ObjectStoreId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.OntologySegmentId;
import com.lsgllc.norm.kernel.graph.model.meta.owl.INormOntologySegment;
import com.lsgllc.norm.kernel.graph.things.INormThing;

/**
 * Created By: sameloyiv
 * Date: 12/6/12
 * Time: 1:39 PM
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractOntologySegment)) return false;
        if (!super.equals(o)) return false;

        AbstractOntologySegment that = (AbstractOntologySegment) o;

        if (!entities.equals(that.entities)) return false;
        if (!uri.equals(that.uri)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + uri.hashCode();
        result = 31 * result + entities.hashCode();
        return result;
    }
}
