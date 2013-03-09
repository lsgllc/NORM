package com.txdot.isd.rts.server.v21.transaction.data;
/*
 * WsVehicleSoldV21ReqDataVerA.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	02/13/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Transaction - Vehicle Sold Request.
 * 
 * <p>This is Version A.
 *
 * @version	3_Amigos_PH_A	02/13/2008
 * @author	B Hargrove
 * <br>Creation Date:		02/13/2008 10:26
 */
public class WsVehicleSoldV21ReqDataVerA
	extends WsVehicleSoldV21ReqData
{
	private int ciV21UniqueId;
	
	/**
	 * Returns the V21 Unique Id.
	 * 
	 * @return int
	 */
	public int getV21UniqueId()
	{
		return ciV21UniqueId;
	}

	/**
	 * Sets the V21 Unique Id.
	 * 
	 * @param aiV21UniqueId
	 */
	public void setV21UniqueId(int aiV21UniqueId)
	{
		ciV21UniqueId = aiV21UniqueId;
	}
}
