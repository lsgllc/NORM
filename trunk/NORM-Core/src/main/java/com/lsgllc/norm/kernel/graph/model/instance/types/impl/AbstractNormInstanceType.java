package com.lsgllc.norm.kernel.graph.model.instance.types.impl;

import com.lsgllc.norm.kernel.core.util.identity.INormId;
import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;
import com.lsgllc.norm.kernel.graph.model.instance.types.INormInstanceType;
import com.lsgllc.norm.kernel.graph.typing.INormType;
import com.lsgllc.norm.kernel.graph.typing.impl.AbstractNormType;
import org.objectweb.asm.Opcodes;

/**
 * Created By: sameloyiv
 * Date: 1/4/13
 * Time: 2:44 PM
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
public   class AbstractNormInstanceType<T extends INSTANCE_TYPE>  extends AbstractNormType<T> implements INormInstanceType<T>,INormType<T>{
    protected INormId<T> id;

    protected AbstractNormInstanceType(T id) {
        super(id);
    }


    public static INormType createType(INSTANCE_TYPE type) {

        INormType<INSTANCE_TYPE> normType = null;
        switch (type) {
            case BOOLEAN:
                normType = new BooleanType();
                break;
            case CHAR:
                normType = new CharType();
                break;
            case FLOAT:
                normType = new FloatType();
                break;
            case DOUBLE:
                normType = new DoubleType();
                break;
            case BYTE:
                normType = new ByteType();
                break;
            case SHORT:
                normType = new ShortType();
                break;
            case INTEGER:
                normType = new IntegerType();
                break;
            case LONG:
                normType = new LongType();
                break;
            case STRING:
                normType = new StringType();
                break;
            case OBJECT:
                normType = new ObjectType();
                break;
        }
        return normType;

    }

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public Opcodes getASMOpCode() {
        return null;
    }

    @Override
    public Class<T> getClazz() {
        return null;
    }
}
