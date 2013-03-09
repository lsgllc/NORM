package com.lsgllc.norm.util.client;

import com.lsgllc.norm.client.persistence.INormPersistable;

/**
 * Created By: sameloyiv
 * Date: 1/3/13
 * Time: 1:36 PM
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
public interface INormDataMart<K> extends INormIdentifyable<K>{
//     EstablishHostConn(String hostInformation);
    INormConnection getConnection(INormCreds creds);
}

