package com.lsgllc.norm.kernel.graph.typing.impl;

import com.lsgllc.norm.kernel.core.util.identity.impl.AbstractNormId;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 2/3/13
 * Time: 2:50 PM
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
public class NormTypeId extends AbstractNormId<ELEMENT_TYPES> {
    public NormTypeId(UUID id) {
        super(id);
    }

    public NormTypeId() {
        super(ELEMENT_TYPES.TYPE);
    }
}
