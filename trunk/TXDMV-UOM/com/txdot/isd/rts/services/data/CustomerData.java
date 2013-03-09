package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * CustomerData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * CustomerData
 *
 * @version	6.5.0		05/24/2010
 * @author	Kathy Harrell
 * <br>Creation Date:	05/24/2010	12:25:17
 */
public class CustomerData implements Serializable
{
	// String 
	private String csEMail = new String();
	private String csPhoneNo = new String();
	
	// Object 
	private AddressData caAddressData;
	private CustomerNameData caCustNameData;
	
	static final long serialVersionUID = -7977277289513680403L;

	/**
	 * CustomerData.java Constructor
	 * 
	 */
	public CustomerData()
	{
		super();
		caAddressData = new AddressData();
		caCustNameData = new CustomerNameData();
	}

	/**
	 * Sets value of caAddressData
	 * 
	 * @return AddressData
	 */
	public AddressData getAddressData()
	{
		if (caAddressData == null)
		{
			caAddressData = new AddressData();
		}
		return caAddressData;
	}

	/**
	 * Sets value of caCustNameData
	 * 
	 * @return CustomerNameData
	 */
	public CustomerNameData getCustNameData()
	{
		if (caCustNameData == null) 
		{
			caCustNameData = new CustomerNameData(); 	
		}
		return caCustNameData;
	}

	/**
	 * Sets value of csEMail
	 * 
	 * @return String
	 */
	public String getEMail()
	{
		return csEMail;
	}

	/**
	 * Sets value of csPhoneNo
	 * 
	 * @return String
	 */
	public String getPhoneNo()
	{
		return csPhoneNo;
	}

	/**
	 * Sets value of caAddressData
	 * 
	 * @param aaAddressData
	 */
	public void setAddressData(AddressData aaAddressData)
	{
		caAddressData = aaAddressData;
	}

	/**
	 * Sets value of caCustNameData
	 * 
	 * @param aaCustNameData
	 */
	public void setCustNameData(CustomerNameData aaCustNameData)
	{
		caCustNameData = aaCustNameData;
	}

	/**
	 * Sets value of csEMail
	 * 
	 * @param asEMail
	 */
	public void setEMail(String asEMail)
	{
		csEMail = asEMail;
	}

	/**
	 * Sets value of csPhoneNo
	 * 
	 * @param asPhoneNo
	 */
	public void setPhoneNo(String asPhoneNo)
	{
		csPhoneNo = asPhoneNo;
	}
}
