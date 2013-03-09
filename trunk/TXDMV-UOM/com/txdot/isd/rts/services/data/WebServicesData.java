package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * WebServicesData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/24/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * Data class for RTS_SVC table data.
 *
 * @version	MyPlates_POS	06/24/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		06/24/2008 10:33:55
 */
public class WebServicesData  implements Serializable
{
	private int ciSrvId;
	private String csSrvcName;
	private String csSrvcDesc;
	
	/**
	 * Get the Service Id.
	 * 
	 * @return int
	 */
	public int getSrvId()
	{
		return ciSrvId;
	}

	/**
	 * Get the Service Description.
	 * 
	 * @return String
	 */
	public String getSrvcDesc()
	{
		return csSrvcDesc;
	}

	/**
	 * Get the Service Name.
	 * 
	 * @return String
	 */
	public String getSrvcName()
	{
		return csSrvcName;
	}

	/**
	 * Set the Service Id.
	 * 
	 * @param 
	 */
	public void setSrvId(int aiSrvId)
	{
		ciSrvId = aiSrvId;
	}

	/**
	 * Get the Service Description.
	 * 
	 * @param asSrvcDesc
	 */
	public void setSrvcDesc(String asSrvcDesc)
	{
		csSrvcDesc = asSrvcDesc;
	}

	/**
	 * Set the Service Name.
	 * 
	 * @param asSrvcName
	 */
	public void setSrvcName(String asSrvcName)
	{
		csSrvcName = asSrvcName;
	}
}
