package com.txdot.isd.rts.server.v21.admin.data;

/*
 * WsDocTypesRes.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/28/2010	Created
 * 							defect 10482 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * Web Services Response Object for Document Types
 *
 * @version	6.5.0			07/28/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		07/28/2010 11:25:17 
 */
public class WsDocTypesRes
{
	private int ciV21ReqId;
	private int ciV21UniqueId;
	private WsDocTypesData[] carrDocTypes;

	/**
	 * WsDocTypesRes.java Constructor
	 * 
	 */
	public WsDocTypesRes()
	{
		super();
	}

	/**
	 * Return carrDocTypes
	 * 
	 * @return WsDocTypesData[]
	 */
	public WsDocTypesData[] getDocTypes()
	{
		return carrDocTypes;
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
	 * Return value of carrDocTypes 
	 * 
	 * @param aarrDocTypes
	 */
	public void setDocTypes(WsDocTypesData[] aarrDocTypes)
	{
		carrDocTypes = aarrDocTypes;
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