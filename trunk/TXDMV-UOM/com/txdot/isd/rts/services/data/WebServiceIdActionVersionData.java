package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * WebServiceIdActionVersionData.java
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
 * Data class for RTS_SRVC_ACTN_VERSION table data.
 *
 * @version	MyPlates_POS	06/24/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		06/24/2008 10:43:55
 */
public class WebServiceIdActionVersionData implements Serializable
{
	private int ciActnId;
	private int ciDeleteIndi;
	private int ciSAVId;
	private int ciSrvId;
	private int ciVersion;
	private String csSAVDesc;
	
	/**
	 * Return Action Id.
	 * 
	 * @return int
	 */
	public int getActnId()
	{
		return ciActnId;
	}

	/**
	 * Return Delete Indicator.
	 * 
	 * @return int
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}

	/**
	 * Return the Service - Action - Version Description.
	 * 
	 * @return String
	 */
	public String getSAVDesc()
	{
		return csSAVDesc;
	}

	/**
	 * Return the Service - Action - Version Id.
	 *  
	 * @return int
	 */
	public int getSAVId()
	{
		return ciSAVId;
	}

	/**
	 * Return the Service Id.
	 * 
	 * @return int
	 */
	public int getSrvId()
	{
		return ciSrvId;
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
	public void setCiDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}

	/**
	 * Set the Service - Action - Version desc.
	 * 
	 * @param asSAVDesc
	 */
	public void setSAVDesc(String asSAVDesc)
	{
		csSAVDesc = asSAVDesc;
	}

	/**
	 * Set the Service - Action - Version Id.
	 * 
	 * @param aiSAVId
	 */
	public void setSAVId(int aiSAVId)
	{
		ciSAVId = aiSAVId;
	}

	/**
	 * Set the Service Id.
	 * 
	 * @param aiSrvId
	 */
	public void setSrvId(int aiSrvId)
	{
		ciSrvId = aiSrvId;
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
