package com.lsgllc.norm.kernel.graph.model.instance.identity.impl;

import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 2/3/13
 * Time: 4:05 PM
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
public class FloatId extends AbstractNormInstanceId<INSTANCE_TYPE> {
    public FloatId(UUID id) {
        super(id);
    }

    public FloatId() {
        super(INSTANCE_TYPE.FLOAT);
    }
}
