package com.lsgllc.norm.kernel.graph.typing.impl;

import com.lsgllc.norm.kernel.graph.model.instance.types.INSTANCE_TYPE;
import com.lsgllc.norm.kernel.graph.typing.INormType;

/**
 * Created By: sameloyiv
 * Date: 2/3/13
 * Time: 3:34 PM
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
public class NormTypeFactory {
    public static INormType<INSTANCE_TYPE> createType(INSTANCE_TYPE type) {

        INormType<INSTANCE_TYPE> normType = null;
        switch (type) {

            case BOOLEAN:
                break;
            case CHAR:
                break;
            case FLOAT:
                break;
            case DOUBLE:
                break;
            case BYTE:
                break;
            case SHORT:
                break;
            case INTEGER:
                break;
            case LONG:
                break;
            case STRING:
                break;
            case OBJECT:
                break;
        }
        return normType;

    }
}
