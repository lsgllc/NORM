package com.txdot.isd.rts.server.v21.admin.data;

/*
 * WsClassToPlateRes.java
 *
 * (c) Texas Department of Motor Vehicles 2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/13/2012	Copied from WsClassToPlateRes.
 * 							Changed carrClassToPlate to use 
 * 								WsClassToPlateDataVerB.
 * 							defect 11224 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Web Services Response Object for Class to Plate Ver B
 *
 * @version	6.10.0			01/13/2012
 * @author	Kathy Harrell
 * @author  Ray Rowehl
 * <br>Creation Date:		01/13/2012 12:25:17 
 */
public class WsClassToPlateResVerB
{
	private int ciV21ReqId;
	private int ciV21UniqueId;
	private WsClassToPlateDataVerB[] carrClassToPlate;

	/**
	 * WsClassToPlateResVerB.java Constructor
	 * 
	 */
	public WsClassToPlateResVerB()
	{
		super();
	}

	/**
	 * Return value of carrClassToPlate
	 * 
	 * @return WsClassToPlateDataVerB[]
	 */
	public WsClassToPlateDataVerB[] getClassToPlate()
	{
		return carrClassToPlate;
	}

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
	 * Set value of carrClassToPlate 
	 * 
	 * @param aarrClassToPlate
	 */
	public void setClassToPlate(WsClassToPlateDataVerB[] aarrClassToPlate)
	{
		carrClassToPlate = aarrClassToPlate;
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
