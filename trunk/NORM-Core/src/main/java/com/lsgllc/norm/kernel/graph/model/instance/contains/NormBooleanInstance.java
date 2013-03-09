package com.lsgllc.norm.kernel.graph.model.instance.contains;

import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.BooleanId;
import com.lsgllc.norm.kernel.graph.model.instance.impl.scalar.AbstractNormBooleanInstance;
import com.lsgllc.norm.kernel.graph.model.instance.types.impl.BooleanType;
import com.lsgllc.norm.kernel.graph.things.INormThing;

/**
 * Created By: sameloyiv
 * Date: 2/5/13
 * Time: 6:11 AM
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
public class NormBooleanInstance
        extends AbstractNormBooleanInstance<BooleanId,INormThing<?,?>,INormInstance<BooleanId,INormThing<?,?>,BooleanType>> {
    public NormBooleanInstance( Boolean value) {
        super(value);
    }
}
