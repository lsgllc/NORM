package com.lsgllc.norm.kernel.graph.model.meta.identity;

import com.lsgllc.norm.kernel.graph.identity.impl.AbstractNormId;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 2/2/13
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
public class OntologySegmentId extends AbstractNormId<ELEMENT_TYPES> {
    public OntologySegmentId(UUID id) {
        super(id);
    }

    public OntologySegmentId() {
        super(ELEMENT_TYPES.ONT_SEGMENT);
    }

}
