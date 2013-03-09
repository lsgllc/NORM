package com.lsgllc.norm.kernel.graph.model.instance.impl.owl;

import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.impl.NormMetaValue;
import com.lsgllc.norm.kernel.graph.model.instance.impl.scalar.AbstractNormInstance;
import com.lsgllc.norm.kernel.graph.model.meta.identity.AttributeId;
import com.lsgllc.norm.kernel.graph.things.INormProperty;
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
public class AbstractAttributeInstance<K extends AttributeId,V extends INormInstance<?,?,T>,T> extends AbstractNormInstance<K,V,T> {
    public AbstractAttributeInstance(INormProperty<K, V> value) {
        super((K) new AttributeId(), new NormMetaValue(value, ELEMENT_TYPES.ATTRIBUTE));
    }
}
