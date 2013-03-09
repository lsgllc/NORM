package com.lsgllc.norm.kernel.graph.model.instance.contains;

import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.DoubleId;
import com.lsgllc.norm.kernel.graph.model.instance.impl.scalar.AbstractNormDoubleInstance;
import com.lsgllc.norm.kernel.graph.model.instance.types.impl.DoubleType;
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
public class NormDoubleInstance extends AbstractNormDoubleInstance<DoubleId,INormThing<?,?>,INormInstance<DoubleId,INormThing<?,?>,DoubleType>> {
    public NormDoubleInstance(Double value) {
        super(value);
    }
}
