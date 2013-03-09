package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * AdminNameAddressData.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/24/2009	Created
 * 							defect 10112 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * Insert the class's description here. (do not leave this)
 *
 * @version	Defect_POS_F	06/24/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		06/24/2009 16:45:00
 */
public class AdminNameAddressData
	extends NameAddressData
	implements Serializable
{
	// int 
	private int ciDeleteIndi;
	private int ciId;
	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	
	// RTSDate 
	private RTSDate caChngTimestmp;

	static final long serialVersionUID = -8452678708295298647L;

	/**
	 * AdminNameAddressData.java Constructor
	 * 
	 */
	public AdminNameAddressData()
	{
		super();
		
		ciId = Integer.MIN_VALUE;
		ciOfcIssuanceNo = Integer.MIN_VALUE;
		ciSubstaId = Integer.MIN_VALUE;
		ciDeleteIndi = 0; 
	}

	/**
	 * Return caChngTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}

	/**
	 * Return ciDeleteIndi 
	 * 
	 * @return int
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}

	/**
	 * Return ciId
	 * 
	 * @return int 
	 */
	public int getId()
	{
		return ciId;
	}

	/**
	 * Return ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Return ciSubstaId
	 * 
	 * @return int 
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Set value of caChngTimestmp
	 * 
	 * @param date
	 */
	public void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}

	/**
	 * Set value of ciDeleteIndi
	 * 
	 * @param i
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}

	/**
	 * Set value of ciId
	 * 
	 * @param aiId
	 */
	public void setId(int aiId)
	{
		ciId = aiId;
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
	 * Set value of ciSubstaId 
	 * 
	 * @param aiSubstaId 
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
}
