package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * WebServicesServiceActionVersionData.java
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
 * Data class for RTS_SRVC_ACTN_VERSION table data.
 *
 * @version	MyPlates_POS	07/02/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		07/02/2008 13:47:55
 */

public class WebServicesServiceActionVersionData
	implements Serializable
{
	private int ciActnId;
	private int ciDeleteIndi;
	private int ciSavId;
	private int ciSrvcId;
	private int ciVersion;
	private String csSavDesc;
	
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
	 * Return the Delete Indicator.
	 * 
	 * @return int
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}

	/**
	 * Return the Service-Action-Version Description.
	 * 
	 * @return String
	 */
	public String getSavDesc()
	{
		return csSavDesc;
	}

	/**
	 * Return the Service-Action-Version Id.
	 * 
	 * @return int
	 */
	public int getSavId()
	{
		return ciSavId;
	}

	/**
	 * Return the Service Id.
	 * 
	 * @return int
	 */
	public int getSrvcId()
	{
		return ciSrvcId;
	}

	/**
	 * Return the Version.
	 * 
	 * @return int
	 */
	public int getVersion()
	{
		return ciVersion;
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

	/**
	 * Set the Delete Indicator.
	 * 
	 * @param aiDeleteIndi
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}

	/**
	 * Set the Service-Action-Version Id.
	 * 
	 * @param asSavDesc
	 */
	public void setSavDesc(String asSavDesc)
	{
		csSavDesc = asSavDesc;
	}

	/**
	 * Set the Service-Action-Version Id.
	 * 
	 * @param aiSavId
	 */
	public void setSavId(int aiSavId)
	{
		ciSavId = aiSavId;
	}

	/**
	 * Set the Service Id.
	 * 
	 * @param aiSrvcId
	 */
	public void setSrvcId(int aiSrvcId)
	{
		ciSrvcId = aiSrvcId;
	}

	/**
	 * Set the Version.
	 * 
	 * @param aiVersion
	 */
	public void setVersion(int aiVersion)
	{
		ciVersion = aiVersion;
	}

}
