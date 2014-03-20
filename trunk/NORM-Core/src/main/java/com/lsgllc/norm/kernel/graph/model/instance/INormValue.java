package com.lsgllc.norm.kernel.graph.model.instance;

/**
 * Created By: sameloyiv
 * Date: 12/6/12
 * Time: 2:21 PM
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
public interface INormValue<V, T> {
    void setValue(V value);
    V getValue();
    void setValueType(T type);
    T getValueType();
}
