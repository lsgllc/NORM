package com.lsgllc.norm.kernel.graph.model.instance.containedby.scalar;

import com.lsgllc.norm.kernel.graph.model.instance.INormInstance;
import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.StringId;
import com.lsgllc.norm.kernel.graph.model.instance.impl.scalar.AbstractNormStringInstance;
import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;
import com.lsgllc.norm.kernel.graph.model.instance.types.impl.StringType;
import com.lsgllc.norm.kernel.graph.things.INormProperty;
import com.lsgllc.norm.kernel.graph.typing.INormType;

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
public class NormStringInstance extends AbstractNormStringInstance<StringId,INormInstance<INormProperty,INormInstance,INormType<INSTANCE_TYPE>>,StringType> {
    public NormStringInstance(String value) {
        super(value);
    }
}
