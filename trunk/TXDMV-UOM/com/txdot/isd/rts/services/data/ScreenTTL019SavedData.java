package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * ScreenTTL019SavedData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/09/2010	Created 
 * 							defect 10665 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Object to save data from TTL019
 *
 * @version	6.7.0			12/09/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/09/2010  13:29:17
 */
public class ScreenTTL019SavedData implements Serializable
{
	NameAddressData caMailingNameAddressData = new NameAddressData();

	/**
	 * ScreenTTL019SavedData.java Constructor
	 */
	public ScreenTTL019SavedData()
	{
		super();
	}

	static final long serialVersionUID = -7646531560508210892L;

	/**
	 * Get value of caMailingNameAddressData
	 * 
	 * @return NameAddressData
	 */
	public NameAddressData getMailingNameAddressData()
	{
		return caMailingNameAddressData;
	}

	/**
	 * Set value of caMailingNameAddressData
	 * 
	 * @param aaMailingNameAddressData
	 */
	public void setMailingNameAddressData(NameAddressData aaMailingNameAddressData)
	{
		caMailingNameAddressData = aaMailingNameAddressData;
	}
}
