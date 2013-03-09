package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * VehicleUserData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/05/2005	Java 1.4 Work
 * 							Moved to services.data	
 * 							defect 7889 Ver 5.2.3
 * K Harrell	06/17/2010	add ciEMailRenwlReqCd, get/set methods 
 * 							defect 10508 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get and set methods for 
 * VehicleUserData
 *
 * @version	6.5.0		06/17/2010
 * @author	Administrator 
 * <br>Creation Date: 	10/02/2001 10:44:22
 */

public class VehicleUserData implements Serializable
{

	// defect 10508 
	// int 
	private int ciEMailRenwlReqCd;
	// end defect 10508  

	// String
	private String csEmail;
	private String csFirstName;
	private String csLastName;
	private String csMiddleName;
	private String csPhoneNumber;
	private String csRecipientName;
	private String csTraceNumber;

	// Object 
	private AddressData caAddress;
	private RTSDate caRenewalDateTime;

	private final static long serialVersionUID = -4045243633359772943L;

	/**
	 * VehicleUserData constructor comment.
	 */
	public VehicleUserData()
	{

		super();

		csFirstName = null;
		csLastName = null;
		caAddress = new com.txdot.isd.rts.services.data.AddressData();
		csTraceNumber = null;
		csEmail = null;
		caRenewalDateTime = null;
		csPhoneNumber = null;
		csRecipientName = null;
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
	 * Return value of Email
	 *
	 * @return String
	 */
	public String getEmail()
	{
		return csEmail;
	}
	/**
	 * Get value of ciEMailRenwlReqCd
	 * 
	 * @return int 
	 */
	public int getEMailRenwlReqCd()
	{
		return ciEMailRenwlReqCd;
	}
	/**
	 * Return value of FirstName
	 * 
	 * @return String
	 */
	public String getFirstName()
	{
		return csFirstName;
	}
	/**
	 * Return value of LastName
	 * 
	 * @return String
	 */
	public String getLastName()
	{
		return csLastName;
	}
	/**
	 * Return value of MiddleName
	 * 
	 * @return String
	 */
	public String getMiddleName()
	{
		return csMiddleName;
	}
	/**
	 * Return value of PhoneNumber
	 * 
	 * @return String
	 */
	public String getPhoneNumber()
	{
		return csPhoneNumber;
	}
	/**
	 * Return value of RecipientName
	 * 
	 * @return String
	 */
	public String getRecipientName()
	{
		return csRecipientName;
	}
	/**
	 * Return value of RenewalDateTime
	 *
	 * @return RTSDate
	 */
	public RTSDate getRenewalDateTime()
	{
		return caRenewalDateTime;
	}
	/**
	 * Return value of TraceNumber
	 * 
	 * @return String
	 */
	public String getTraceNumber()
	{
		return csTraceNumber;
	}
	/**
	 * Set value of Address
	 * 
	 * @param aaAddress AddressData
	 */
	public void setAddress(AddressData aaAddress)
	{
		caAddress = aaAddress;
	}
	/**
	 * Set value of Email
	 * 
	 * @param asEmail String
	 */
	public void setEmail(String asEmail)
	{
		csEmail = asEmail;
	}

	/**
	 * Set value of ciEMailRenwlReqCd
	 * 
	 * @param aiEMailRenwlReqCd
	 */
	public void setEMailRenwlReqCd(int aiEMailRenwlReqCd)
	{
		ciEMailRenwlReqCd = aiEMailRenwlReqCd;
	}
	/**
	 * Set value of FirstName
	 * 
	 * @param asFirstName String
	 */
	public void setFirstName(String asFirstName)
	{
		csFirstName = asFirstName;
	}
	/**
	 * Set value of LastName
	 * 
	 * @param asLastName String
	 */
	public void setLastName(String asLastName)
	{
		csLastName = asLastName;
	}
	/**
	 * Set value of MiddleName 
	 * 
	 * @param asMiddleName  String
	 */
	public void setMiddleName(String asMiddleName)
	{
		csMiddleName = asMiddleName;
	}
	/**
	 * Set value of PhoneNumber
	 * 
	 * @param csPhoneNumber String
	 */
	public void setPhoneNumber(String asPhoneNumber)
	{
		csPhoneNumber = asPhoneNumber;
	}
	/**
	 * Set value of RecipientName
	 *
	 * @param asRecipientName String
	 */
	public void setRecipientName(String asRecipientName)
	{
		csRecipientName = asRecipientName;
	}
	/**
	 * Set value of RenewalDateTime
	 * 
	 * @param aaRenewalDateTime RTSDate 
	 */
	public void setRenewalDateTime(RTSDate aaRenewalDateTime)
	{
		caRenewalDateTime = aaRenewalDateTime;
	}
	/**
	 * Set value of TraceNumber
	 * 
	 * @param asTraceNumber String
	 */
	public void setTraceNumber(String asTraceNumber)
	{
		csTraceNumber = asTraceNumber;
	}

}
