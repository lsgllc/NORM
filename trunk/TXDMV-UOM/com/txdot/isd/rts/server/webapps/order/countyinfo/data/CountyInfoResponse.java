package com.txdot.isd.rts.server.webapps.order.countyinfo.data;
import com.txdot.isd.rts.server.webapps.order.common.data.*;

/*
 * CountyInfoRequest.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B. Brown		03/02/2007	Created class.
 * 							defect 9121 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * This class holds the response from a call to the County Info
 * Function.
 *
 * @version	Special Plates	03/02/2007
 * @author	Bob Brown
 * <br>Creation Date:		03/02/2007 14:30:00
 */
public class CountyInfoResponse extends AbstractResponse
{
	private String countyName = "";
	private String countyNo = "";
	private String emailAddress = "";
	private Address mailingAddress = null;
	private String phone = "";
	private Address physicalAddress = null;
	private String tacName = "";

	/**
	 * gets countyName
	 *
	 * @return String
	 */
	public String getCountyName()
	{
		return countyName;
	}

	/**
	 * gets countyNo
	 *
	 * @return String
	 */
	public String getCountyNo()
	{
		return countyNo;
	}

	/**
	 * Gets the email address of the county office.
	 * 
	 * @return String
	 */
	public String getEmailAddress()
	{
		return emailAddress;
	}

	/**
	 * Gets the countys Mailing Addesss Object.
	 * 
	 * @return Address
	 */
	public Address getMailingAddress()
	{
		return mailingAddress;
	}

	/**
	 * Gets the Phone Number for the county.
	 * 
	 * @return String
	 */
	public String getPhone()
	{
		return phone;
	}

	/**
	 * Gets the countys Physical Addesss Object.
	 * 
	 * @return Address
	 */
	public Address getPhysicalAddress()
	{
		return physicalAddress;
	}

	/**
	 * gets tacName
	 *
	 * @return String
	 */
	public String getTacName()
	{
		return tacName;
	}

	/**
	 * sets countyName
	 * 
	 * @param asCountyName
	 */
	public void setCountyName(String asCountyName)
	{
		countyName = asCountyName;
	}

	/**
	 * sets countyNo
	 * 
	 * @param asCountyNo String
	 */
	public void setCountyNo(String asCountyNo)
	{
		countyNo = asCountyNo;
	}

	/**
	 * Sets the email address of the county office.
	 * 
	 * @param asEmailAddress String
	 */
	public void setEmailAddress(String asEmailAddress)
	{
		emailAddress = asEmailAddress;
	}

	/**
	 * Sets the countys Mailing Addesss Object.
	 * 
	 * @param aaMailingAddress Address
	 */
	public void setMailingAddress(Address aaMailingAddress)
	{
		mailingAddress = aaMailingAddress;
	}

	/**
	 * Sets the Phone number for the County.
	 * 
	 * @param asPhone String
	 */
	public void setPhone(String asPhone)
	{
		phone = asPhone;
	}

	/**
	 * Sets the countys Physical Addesss Object.
	 * 
	 * @param aaPhysicalAddress Address
	 */
	public void setPhysicalAddress(Address aaPhysicalAddress)
	{
		physicalAddress = aaPhysicalAddress;
	}

	/**
	 * sets tacName
	 * 
	 * @param asTacName
	 */
	public void setTacName(String asTacName)
	{
		tacName = asTacName;
	}

}
