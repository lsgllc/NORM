package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * WebAgencyAuthData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708  Ver 6.7.0  
 * K Harrell	01/05/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0   
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgencyAuthData. 
 *
 * @version	6.7.0			01/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:33:17
 */
public class WebAgencyAuthData implements Serializable
{
	private int ciAgncyAuthIdntyNo; 
	private int ciAgncyIdntyNo; 
	private int ciDeleteIndi; 
	private int ciOfcIssuanceNo; 
	private int ciSubconId;
	
	private RTSDate caChngTimestmp;
	
	static final long serialVersionUID = 5927348921813964246L; 
	
	/**
	 * WebAgencyAuthData.java Constructor
	 */
	public WebAgencyAuthData()
	{
		super();
	}

	/**
	 * Get value of ciAgncyAuthIdntyNo
	 * 
	 * @return int
	 */
	public int getAgncyAuthIdntyNo()
	{
		return ciAgncyAuthIdntyNo;
	}

	/**
	 * Get value of ciAgncyIdntyNo
	 * 
	 * @return int
	 */
	public int getAgncyIdntyNo()
	{
		return ciAgncyIdntyNo;
	}

	/**
	 * Get value of caChngTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}

	/**
	 * Get value of ciDeleteIndi
	 * 
	 * @return int
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Get value of ciSubconId
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}

	/**
	 * Set value of ciAgncyAuthIdntyNo
	 * 
	 * @param aiAgncyAuthIdntyNo
	 */
	public void setAgncyAuthIdntyNo(int aiAgncyAuthIdntyNo)
	{
		ciAgncyAuthIdntyNo = aiAgncyAuthIdntyNo;
	}

	/**
	 * Set value of ciAgncyIdntyNo
	 * 
	 * @param aiAgncyIdntyNo
	 */
	public void setAgncyIdntyNo(int aiAgncyIdntyNo)
	{
		ciAgncyIdntyNo = aiAgncyIdntyNo;
	}

	/**
	 * Set value of caChngTimestmp
	 * 
	 * @param aaChngTimestmp
	 */
	public void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}

	/**
	 * Set value of ciDeleteIndi
	 * 
	 * @param aiDeleteIndi
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set value of ciSubconId
	 * 
	 * @param aiSubconId
	 */
	public void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}

}
