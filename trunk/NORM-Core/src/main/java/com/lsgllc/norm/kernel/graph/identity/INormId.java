package com.lsgllc.norm.kernel.graph.identity;

import com.lsgllc.norm.kernel.graph.typing.INormType;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 12/31/12
 * Time: 11:30 AM
 * <p/>
 * <p/>
 * (c) Texas Department of Motor Vehicles  2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name		    Date		Description
 * ------------	-----------	--------------------------------------------
 *
 * @author
 * @description
 * @date
 */
public interface INormId<T> {
    UUID getUUID();
    void setUUID(UUID id);
    INormType<T> getObjType();
    void setObjType(INormType<T> type);
}
