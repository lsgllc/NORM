package com.txdot.isd.rts.server.v21.transaction.data;
/*
 * WsVehicleSoldV21ResDataVerB.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/23/2010	Created
 * 							defect 10482 Ver 6.5.0 
 * K Harrell	07/26/2010	add ciV21UniqueId, get/set methods 
 * 							defect 10482 Ver 6.5.0
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Transaction - Vehicle Sold Result.
 * 
 *   *** Ver B *** 
 *
 * @version	6.5.0 			07/26/2010	
 * @author	Kathy Harrell
 * <br>Creation Date:		07/23/2010	14:00:17
 */
public class WsVehicleSoldV21ResDataVerB
	extends WsVehicleSoldV21ResData
{
	private int ciV21ReqId;
	private int ciV21UniqueId;

	/**
	 * Return value of V21ReqId
	 * 
	 * @return int 
	 */
	public int getV21ReqId()
	{
		return ciV21ReqId;
	}

	/**
	 * Return value of V21UniqueId
	 * 
	 * @return int 
	 */
	public int getV21UniqueId()
	{
		return ciV21UniqueId;
	}

	/**
	 * Set value of V21ReqId
	 * 
	 * @param aiV21ReqId
	 */
	public void setV21ReqId(int aiV21ReqId)
	{
		ciV21ReqId = aiV21ReqId;
	}

	/**
	 * Set value of V21UniqueId
	 * 
	 * @param aiV21UniqueId
	 */
	public void setV21UniqueId(int aiV21UniqueId)
	{
		ciV21UniqueId = aiV21UniqueId;
	}
}
