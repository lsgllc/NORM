package com.lsgllc.norm.kernel.graph.model.instance.identity.impl;

import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 2/3/13
 * Time: 4:03 PM
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
public class BooleanId extends AbstractNormInstanceId<INSTANCE_TYPE>  {
    public BooleanId(String canonicalName) {
        super(INSTANCE_TYPE.BOOLEAN, UUID.fromString(canonicalName));
    }

    public BooleanId() {
        super(INSTANCE_TYPE.BOOLEAN);
    }

}
