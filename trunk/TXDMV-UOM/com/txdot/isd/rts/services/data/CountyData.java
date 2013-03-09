package com.txdot.isd.rts.services.data;

import java.io.Serializable;


/*
 *
 * CountyData.java  
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/05/2005	Java 1.4 Work
 * 							moved from services.data.webapps
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * CountyData
 * 
 * @version	5.2.3			10/05/2005 
 * @author	Administrator
 * <br>Creation Date:		10/04/2001 14:56:08 
 */

public class CountyData implements Serializable
{
	//int 
	private int ciInternetIndi;

	// Object 
	private AddressData caAddress;

	//String 
	private String csName;
	private String csTACName;
	private String csPhoneNo;
	private String csEmail;
	private String csHoursOfOperation;
	private String csDaysOfOperation;

	private final static long serialVersionUID = 4502832117354521825L;

	/**
	 * CountyData constructor comment.
	 */
	public CountyData()
	{
		super();
	}
	/**
	 * Return value of Address
	 * 
	 * @return AddressData
	 */
	public AddressData getAddress()
	{
		return caAddress;
	}
	/**
	 * Return value of DaysOfOperation
	 * 
	 * @return String
	 */
	public String getDaysOfOperation()
	{
		return csDaysOfOperation;
	}
	/**
	 * Return value of Email
	 * 
	 * @return String
	 */
	public String getEmail()
	{
		return csEmail;
	}
	/**
	 * Return value of HoursOfOperation
	 * 
	 * @return String
	 */
	public String getHoursOfOperation()
	{
		return csHoursOfOperation;
	}
	/**
	 * Return value of InternetIndi
	 * 
	 * @return int
	 */
	public int getInternetIndi()
	{
		return ciInternetIndi;
	}
	/**
	 * Return value of Name
	 * 
	 * @return String
	 */
	public String getName()
	{
		return csName;
	}
	/**
	 * Return value of PhoneNo
	 * 
	 * @return String
	 */
	public String getPhoneNo()
	{
		return csPhoneNo;
	}
	/**
	 * Return value of TACName
	 * 
	 * @return String
	 */
	public String getTACName()
	{
		return csTACName;
	}
	/**
	 * Set value of  of Address
	 *
	 * @param aaAddress AddressData
	 */
	public void setAddress(AddressData aaAddress)
	{
		caAddress = aaAddress;
	}
	/**
	 * Set value of  of DaysOfOperation
	 * 
	 * @param asDaysOfOperation String
	 */
	public void setDaysOfOperation(String asDaysOfOperation)
	{
		csDaysOfOperation = asDaysOfOperation;
	}
	/**
	 * Set value of  of Email
	 * 
	 * @param asEmail String
	 */
	public void setEmail(String asEmail)
	{
		csEmail = asEmail;
	}
	/**
	 * Set value of  of HoursOfOperation
	 * 
	 * @param asHoursOfOperation String
	 */
	public void setHoursOfOperation(String asHoursOfOperation)
	{
		csHoursOfOperation = asHoursOfOperation;
	}
	/**
	 * Set value of  of InternetIndi
	 * 
	 * @param aiInternetIndi int
	 */
	public void setInternetIndi(int aiInternetIndi)
	{
		ciInternetIndi = aiInternetIndi;
	}
	/**
	 * Set value of  of Name
	 * 
	 * @param asName String
	 */
	public void setName(String asName)
	{
		csName = asName;
	}
	/**
	 * Set value of  of PhoneNo
	 * 
	 * @param asPhoneNo String
	 */
	public void setPhoneNo(String asPhoneNo)
	{
		csPhoneNo = asPhoneNo;
	}
	/**
	 * Set value of  of TACName
	 * 
	 * @param asTACName String
	 */
	public void setTACName(String asTACName)
	{
		csTACName = asTACName;
	}
}
