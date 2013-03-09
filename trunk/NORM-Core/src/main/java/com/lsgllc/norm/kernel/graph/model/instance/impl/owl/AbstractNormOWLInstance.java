package com.lsgllc.norm.kernel.graph.model.instance.impl.owl;

import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.INormValue;
import com.lsgllc.norm.kernel.graph.model.instance.types.INormInstanceType;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormThing;
import com.lsgllc.norm.kernel.graph.typing.INormType;

/**
 * Created By: sameloyiv
 * Date: 2/7/13
 * Time: 3:46 PM
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
@Deprecated
public class AbstractNormOWLInstance<K,V extends INormThing<?,?>,T>extends AbstractNormThing<K,V> implements INormInstance<K,V,INormValue<?,INormInstanceType<T>>> {
    //    private INormInstanceType<T> valueType;
    private INormValue<?, INormInstanceType<T>> value = null;

    public AbstractNormOWLInstance(K id, INormValue<?, INormInstanceType<T>> value) {
        super(id);
        this.value = value;
//        this.metaType = metaType;
    }


    public INormInstanceType<T> getValueType() {
        return (INormInstanceType<T>) ((INormId)this.getId()).getObjType();
    }

    public void setValueType(INormInstanceType<T> valueType) {
        ((INormId)this.getId()).setObjType((INormType<T>) valueType);
    }

    public INormValue<?, INormInstanceType<T>> getValue() {
        return value;
    }

    public void setValue(INormValue<?, INormInstanceType<T>> value) {
        this.value = value;
    }
}
