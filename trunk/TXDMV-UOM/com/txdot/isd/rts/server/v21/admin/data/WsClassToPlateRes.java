package com.txdot.isd.rts.server.v21.admin.data;

/*
 * WsClassToPlateRes.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/28/2010	Created
 * 							defect 10482 Ver 6.5.0 
 * Ray Rowehl	01/13/2012	Deprecate
 * 							defectg 11224 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Web Services Response Object for Class to Plate
 *
 * @version	6.10.0			01/13/2012
 * @author	Kathy Harrell
 * <br>Creation Date:		07/28/2010 11:25:17 
 * @deprecated
 */

public class WsClassToPlateRes
{
	private int ciV21ReqId;
	private int ciV21UniqueId;
	private WsClassToPlateData[] carrClassToPlate;

	/**
	 * WsClassToPlateRes.java Constructor
	 * 
	 */
	public WsClassToPlateRes()
	{
		super();
	}

	/**
	 * Return value of carrClassToPlate
	 * 
	 * @return WsClassToPlateData[]
	 */
	public WsClassToPlateData[] getClassToPlate()
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
	public void setClassToPlate(WsClassToPlateData[] aarrClassToPlate)
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
