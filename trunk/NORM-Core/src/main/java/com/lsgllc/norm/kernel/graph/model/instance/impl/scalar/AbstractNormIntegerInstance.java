package com.lsgllc.norm.kernel.graph.model.instance.impl.scalar;

import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.IntegerId;
import com.lsgllc.norm.kernel.graph.model.instance.impl.NormScalarValue;
import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;
import com.lsgllc.norm.kernel.graph.things.INormThing;

/**
 * Created By: sameloyiv
 * Date: 2/7/13
 * Time: 12:09 AM
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
public class AbstractNormIntegerInstance<K extends IntegerId,V extends INormThing<?,?>,T> extends AbstractNormInstance<K,V,T> {
    public AbstractNormIntegerInstance(Integer value) {
        super((K) new IntegerId(), new NormScalarValue(value,INSTANCE_TYPE.BOOLEAN));
    }
}
