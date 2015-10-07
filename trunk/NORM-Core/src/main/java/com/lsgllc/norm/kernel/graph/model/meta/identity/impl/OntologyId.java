package com.lsgllc.norm.kernel.graph.model.meta.identity.impl;

import com.lsgllc.norm.kernel.core.util.identity.impl.AbstractNormId;
import com.lsgllc.norm.kernel.graph.model.meta.identity.IOntologyId;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 2/2/13
 * Time: 4:01 PM
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
public class OntologyId<TT extends ELEMENT_TYPES> extends AbstractNormId<TT> implements IOntologyId<TT> {
    public OntologyId(UUID id) {
        super(id);
    }

    public OntologyId() {
        super((TT) ELEMENT_TYPES.ONTOLOGY);
    }
}
