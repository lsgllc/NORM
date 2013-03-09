package com.lsgllc.norm.util.client;


import com.lsgllc.norm.util.exceptions.NormRuntimeSystemException;

import java.util.UUID;

/**
 * Created By: sameloyiv
 * Date: 1/3/13
 * Time: 1:47 PM
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
public interface INormConnection<T>  extends INormIdentifyable<T> {
    INormCreds getCreds(INormConnection connection);
    INormPersistable getNewPersistable(INormPersistable iFace)  throws NormRuntimeSystemException;
}
