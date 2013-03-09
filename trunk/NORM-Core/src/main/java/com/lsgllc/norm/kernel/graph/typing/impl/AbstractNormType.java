package com.lsgllc.norm.kernel.graph.typing.impl;

import com.lsgllc.norm.kernel.graph.identity.INormId;
import com.lsgllc.norm.kernel.graph.typing.INormType;
import com.lsgllc.norm.util.client.INormIdentifyable;

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
public  abstract class AbstractNormType<T> implements INormType<T>, INormIdentifyable<INormId<T>>{
    protected INormId<T> id;
    protected T type;

    protected AbstractNormType(T type) {
        this.id = (INormId<T>) new NormTypeId();
        this.type = type;
    }


    @Override
    public  INormType<T> getType() {
        return this.id.getObjType();
    }

    @Override
    public void setType( INormType<T> type) {
        this.id.setObjType(type);

    }


    @Override
    public INormId<T> getId() {
        return id;
    }

    @Override
    public void setId(INormId<T> id) {
         this.id = id;
    }
}
