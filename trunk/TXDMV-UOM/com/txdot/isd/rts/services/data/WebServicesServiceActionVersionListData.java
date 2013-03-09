package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * WebServicesServiceActionVersionListData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	07/03/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * Data class for WebServiceServiceActionVersionList cache data.
 *
 * @version	MyPlates_POS	07/03/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		07/03/2008 08:36:55
 */
public class WebServicesServiceActionVersionListData
	extends WebServicesServiceActionVersionData
	implements Serializable
{
	private String csSrvcName;
	
	/**
	 * Return the Service Name.
	 * 
	 * @return String
	 */
	public String getSrvcName()
	{
		return csSrvcName;
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
