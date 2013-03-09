package com.lsgllc.norm.kernel.graph.model.instance.contains;

import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.impl.scalar.AbstractNormShortInstance;
import com.lsgllc.norm.kernel.graph.model.instance.types.impl.ShortId;
import com.lsgllc.norm.kernel.graph.model.instance.types.impl.ShortType;
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
public class NormShortInstance extends AbstractNormShortInstance<ShortId,INormThing<?,?>,INormInstance<ShortId,INormThing<?,?>,ShortType>>{
    public NormShortInstance(Short value) {
        super(value);
    }
}
