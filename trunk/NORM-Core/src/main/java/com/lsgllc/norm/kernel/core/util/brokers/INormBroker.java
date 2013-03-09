package com.lsgllc.norm.kernel.core.util.brokers;

/**
 * Created By: sameloyiv
 * Date: 3/6/13
 * Time: 5:32 PM
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
public interface INormBroker<T> {
    T getId(String key);
}
