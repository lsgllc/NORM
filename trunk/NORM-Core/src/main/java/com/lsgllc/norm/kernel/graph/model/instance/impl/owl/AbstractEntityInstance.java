package com.lsgllc.norm.kernel.graph.model.instance.impl.owl;

import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.ObjectInstanceId;
import com.lsgllc.norm.kernel.graph.model.instance.impl.NormObjectValue;
import com.lsgllc.norm.kernel.graph.model.instance.impl.scalar.AbstractNormInstance;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;

/**
 * Created By: sameloyiv
 * Date: 2/7/13
 * Time: 3:21 PM
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
public class AbstractEntityInstance<K extends ObjectInstanceId,V extends INormInstance<?,?,T>,T> extends AbstractNormInstance<K,V,T> {
    public AbstractEntityInstance(String canonicalName, Object value) {
        super((K) new ObjectInstanceId(canonicalName), new NormObjectValue(value, ELEMENT_TYPES.ENTITY));
    }
}
