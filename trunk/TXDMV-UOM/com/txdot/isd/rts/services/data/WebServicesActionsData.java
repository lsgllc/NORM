package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * WebServicesActionsData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/02/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * Data class for RTS_ACTN table data.
 *
 * @version	MyPlates_POS	07/02/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		07/02/2008 09:24:35
 */
public class WebServicesActionsData implements Serializable
{
	private int ciActnId;
	private String csActnDesc;

	/**
	 * Return the Action Description.
	 * 
	 * @return String
	 */
	public String getActnDesc()
	{
		return csActnDesc;
	}
	
	/**
	 * Return the Action Id.
	 * 
	 * @return int
	 */
	public int getActnId()
	{
		return ciActnId;
	}

	/**
	 * Set the Action Description.
	 * 
	 * @param asActnDesc
	 */
	public void setActnDesc(String asActnDesc)
	{
		csActnDesc = asActnDesc;
	}

	/**
	 * Set the Action Id.
	 * 
	 * @param aiActnId
	 */
	public void setActnId(int aiActnId)
	{
		ciActnId = aiActnId;
	}

}
