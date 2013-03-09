package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * OfficeTimeZoneData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/03/2010	Created
 * 							defect 10427 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * OfficeTimeZoneData
 *
 * @version	POS_640			04/03/2010 
 * @author	Kathy Harrell
 * <br>Creation Date:		04/03/2010 14:52:17
 */
public class OfficeTimeZoneData implements Serializable
{

	private int ciOfcIssuanceNo;
	private String csTimeZone;

	static final long serialVersionUID = -914255323192225571L;

	/**
	 * OfficeTimeZoneData.java Constructor
	 * 
	 */
	public OfficeTimeZoneData()
	{
		super();
	}

	/**
	 * Gets value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Gets value of csTimeZone
	 * 
	 * @return String
	 */
	public String getTimeZone()
	{
		return csTimeZone;
	}

	/**
	 * Sets value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Sets value of csTimeZone
	 * 
	 * @param asTimeZone
	 */
	public void setTimeZone(String asTimeZone)
	{
		csTimeZone = asTimeZone;
	}

}
