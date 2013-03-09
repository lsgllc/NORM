package com.lsgllc.norm.kernel.graph.model.meta.identity;

import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.graph.identity.impl.*;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;
import com.lsgllc.norm.kernel.graph.typing.INormType;

import java.io.Serializable;
import java.util.UUID;

public class AbstractNormMetaId<T extends ELEMENT_TYPES> extends AbstractNormId<T> implements Serializable {

    public AbstractNormMetaId(UUID id) {
        super(id);
    }

    private AbstractNormMetaId() {
        this(UUID.randomUUID());
    }

    public AbstractNormMetaId(INormType<T> type) {
        this();
        this.type = type;
    }

    public static INormId<ELEMENT_TYPES> createId(ELEMENT_TYPES type) {

        INormId<ELEMENT_TYPES> id = null;
        switch (type) {
            case ONTOLOGY:
                id = new OntologyId();
                break;
            case ONT_SEGMENT:
                id = new OntologySegmentId();
                break;
            case OBJECT_STORE:
                id = new ObjectStoreId();
                break;
            case OBJECT:
                id = new ObjectId();
                break;
            case ENTITY:
                id = new EntityId();
                break;
            case ATTRIBUTE:
                id = new AttributeId();
                break;
            case PROPERTY:
                id = new PropertyId();
                break;
            case VALUE:
                id = new ValueId();
                break;
        }
        return id;

    }
}