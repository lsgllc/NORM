package com.lsgllc.norm.kernel.graph.model.instance.contains;

import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.impl.scalar.AbstractNormObjectInstance;
import com.lsgllc.norm.kernel.graph.model.instance.types.impl.ObjectType;
import com.lsgllc.norm.kernel.graph.model.meta.identity.ObjectId;
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
public class NormObjectInstance extends AbstractNormObjectInstance<ObjectId,INormThing<?,?>,INormInstance<ObjectId,INormThing<?,?>,ObjectType>>{
    public NormObjectInstance(INormThing<?,?> value) {
        super(value);
    }
}
