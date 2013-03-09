package com.txdot.isd.rts.server.v21.vehicleinfo.data;
/*
 * WsVehicleInfoV21DataReqVerB.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/26/2010	add ciV21UniqueId, get/set methods
 * 							defect 10482 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * Contains Vehicle Information Request Data from V21 to lookup a 
 * Vehicle Record.
 * 
 *  *** Ver B *** 
 *
 * @version	6.5.0			07/26/2010		
 * @author	Kathy Harrell	
 * <br>Creation Date:		07/26/2010	14:02:17
 */
public class WsVehicleInfoV21DataReqVerB
	extends WsVehicleInfoV21DataReq
{

	/**
	 * WsVehicleInfoV21DataReqVerB.java Constructor
	 * 
	 */
	public WsVehicleInfoV21DataReqVerB()
	{
		super();
	}

	private int ciV21UniqueId;

	/**
	 * Return value of ciV21UniqueId
	 * 
	 * @return int 
	 */
	public int getV21UniqueId()
	{
		return ciV21UniqueId;
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
