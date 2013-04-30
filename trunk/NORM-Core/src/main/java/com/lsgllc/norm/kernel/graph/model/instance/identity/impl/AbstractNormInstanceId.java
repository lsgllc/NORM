package com.lsgllc.norm.kernel.graph.model.instance.identity.impl;

import com.lsgllc.norm.kernel.core.util.identity.impl.AbstractNormId;
import com.lsgllc.norm.kernel.graph.model.instance.identity.INormInstanceId;
import com.lsgllc.norm.kernel.graph.model.instance.types.impl.AbstractNormInstanceType;
import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;
import com.lsgllc.norm.kernel.graph.model.meta.types.impl.AbstractNormMetaType;
import com.lsgllc.norm.kernel.graph.typing.ELEMENT_TYPES;

import java.io.Serializable;
import java.util.UUID;

public class AbstractNormInstanceId<T> extends AbstractNormId<T> implements INormInstanceId<T>,Serializable {

    public AbstractNormInstanceId(T type) {
        this(type, UUID.randomUUID());
    }
    public AbstractNormInstanceId(T type,UUID id) {
        super(id);
        this.type =  (type instanceof INSTANCE_TYPE)?
            AbstractNormInstanceType.createType((INSTANCE_TYPE) type):
                AbstractNormMetaType.createType((ELEMENT_TYPES) type);
    }

    public static INormInstanceId<?> createId(INSTANCE_TYPE type) {

        INormInstanceId<?> id = null;
        switch (type) {

            case BOOLEAN:
                id = new BooleanId();
                break;
            case CHAR:
                id = new CharId();
                break;
            case FLOAT:
                id = new FloatId();
                break;
            case DOUBLE:
                id = new DoubleId();
                break;
            case BYTE:
                id = new ByteId();
                break;
            case SHORT:
                id = new ShortId();
                break;
            case INTEGER:
                id = new IntegerId();
                break;
            case LONG:
                id = new LongId();
                break;
            case STRING:
                id = new StringId();
                break;
            case OBJECT:
                id = new ObjectInstanceId();
                break;
        }
        return id;
    }
}