package com.lsgllc.norm.kernel.graph.model.instance.impl;

import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.graph.model.instance.identity.INormInstanceId;
import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.AbstractNormInstanceId;
import com.lsgllc.norm.kernel.graph.model.instance.impl.AbstractNormValue;
import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;
import com.lsgllc.norm.kernel.graph.model.instance.types.INormInstanceType;
import com.lsgllc.norm.kernel.graph.model.instance.INormValue;
import com.lsgllc.norm.kernel.graph.model.meta.things.INormMetaThing;
import com.lsgllc.norm.kernel.graph.things.INormThing;
import com.lsgllc.norm.kernel.graph.things.impl.AbstractNormThing;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;
import com.lsgllc.norm.kernel.graph.typing.INormType;
import com.lsgllc.norm.util.client.INormIdentifyable;

/**
 * Created By: sameloyiv
 * Date: 1/7/13
 * Time: 4:09 PM
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
public class NormScalarValue<V, T extends INSTANCE_TYPE> extends AbstractNormValue<V,T>{

//    private INormInstanceType<T> valueType;
    private V value = null;

    public NormScalarValue(V value, T metaType) {
        super(value, (INormId<T>) AbstractNormInstanceId.createId(metaType));
        this.value = value;
//        this.metaType = metaType;
    }


    public INormInstanceType<T> getValueType() {
        return (INormInstanceType<T>) this.getId().getObjType();
    }

    public void setValueType(INormInstanceType<T> valueType) {
        this.getId().setObjType((INormType<T>) valueType);
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
