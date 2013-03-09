package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * WebAgencyBatchStatusData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708  Ver 6.7.0   
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgencyBatchStatusData 
 *
 * @version	6.7.0			12/28/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:33:17
 */
public class WebAgencyBatchStatusData implements Serializable
{
	private String csBatchStatusCd;
	private String csBatchStatusDesc;

	static final long serialVersionUID = 4608523545247526706L;

	/**
	 * WebAgencyBatchStatusData.java Constructor
	 * 
	 */
	public WebAgencyBatchStatusData()
	{
		super();
	}

	/**
	 * Get value of csBatchStatusCd
	 * 
	 * @return String
	 */
	public String getBatchStatusCd()
	{
		return csBatchStatusCd;
	}

	/**
	 * Get value of csBatchStatusDesc
	 * 
	 * @return String 
	 */
	public String getBatchStatusDesc()
	{
		return csBatchStatusDesc;
	}

	/**
	 * Set value of csBatchStatusCd
	 * 
	 * @param asBatchStatusCd
	 */
	public void setBatchStatusCd(String asBatchStatusCd)
	{
		csBatchStatusCd = asBatchStatusCd;
	}

	/**
	 * Set value of csBatchStatusDesc
	 * 
	 * @param asBatchStatusDesc
	 */
	public void setBatchStatusDesc(String asBatchStatusDesc)
	{
		csBatchStatusDesc = asBatchStatusDesc;
	}

}
