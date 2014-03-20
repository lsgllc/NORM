package com.lsgllc.norm.kernel.core.util.identity.impl;

import com.lsgllc.norm.kernel.core.util.brokers.impl.NormIDBroker;
import com.lsgllc.norm.kernel.core.util.identity.INormId;
import com.lsgllc.norm.kernel.graph.typing.IHasNormType;
import com.lsgllc.norm.kernel.graph.typing.INormType;
import com.lsgllc.norm.util.springsupport.AppContext;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 12/31/12
 * Time: 11:35 AM
 * <p/>
 * <p/>
 * (c) Loy Services Group, LLC. 2008-2014
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public class AbstractNormId<TT> implements IHasNormType<TT>,INormId<TT>{
    protected UUID id;
    protected INormType<TT> type = null;
//    private NormIDBroker manager = (NormIDBroker) AppContext.getCtx().getBean("idManager");


    public AbstractNormId(UUID id) {
        this.id = id;
    }

    private AbstractNormId() {
        this( UUID.randomUUID());
    }

    public AbstractNormId(TT type) {
        this();
    }




    @Override
    public int hashCode() {
        return id.hashCode();
    }


    @Override
    public INormType<TT> getType() {
        return this.type;
    }

    @Override
    public void setType( INormType<TT> type) {

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractNormId)) return false;

        INormId normId = (INormId) o;

        if (!id.equals(normId.getUUID())) return false;

        return true;
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public void setUUID(UUID id) {

    }

    @Override
    public INormType<TT> getObjType() {
        return null;
    }

    @Override
    public void setObjType(INormType<TT> type) {

    }
}
