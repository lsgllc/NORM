package com.lsgllc.norm.kernel.graph.model.instance.identity.impl;

import com.lsgllc.norm.kernel.graph.model.instance.identity.impl.AbstractNormInstanceId;
import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 2/3/13
 * Time: 4:04 PM
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
public class IntegerId extends AbstractNormInstanceId<INSTANCE_TYPE> {
    public IntegerId(UUID id) {
        super(id);
    }

    public IntegerId() {
        super(INSTANCE_TYPE.INTEGER);
    }
}
