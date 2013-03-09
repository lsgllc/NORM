package com.lsgllc.norm.kernel.graph.typing;

import java.io.Serializable;

/**
 * Created By: sameloyiv
 * Date: 2/3/13
 * Time: 3:51 PM
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
public interface IHasNormType<T> extends Serializable {
    INormType<T> getType();

    void setType( INormType<T> type);
}
