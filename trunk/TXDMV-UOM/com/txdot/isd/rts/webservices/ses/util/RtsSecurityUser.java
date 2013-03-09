/**
 * 
 */
package com.txdot.isd.rts.webservices.ses.util;

/*
 * RtsSecurityUser.java
 * 
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History: 
 * Name 		Date 		Description 
 * ------------ ----------- -------------------------------------------- 
 * R Pilon 		08/02/2011 	Class Created. 
 * 							defect 10718 Ver 6.8.1
 * R Pilon		08/23/2011	Verified csPhoneNbr was not null before
 * 							formatting.
 * 							modify getPhoneNbrNoHyphens()
 * 							add getPhoneNbrDigitsOnly(), removeChar()
 * 							defect 10980 Ver 6.8.1                     
 * ---------------------------------------------------------------------
 */

/**
 * Represents a user's security information from eDirectory and/or
 * Active Directory.
 * 
 * @version 6.8.1 08/02/2011
 * @author RPILON-C
 * @Creation Date: 08/02/2011 13:13
 */
public class RtsSecurityUser
{
	private String csCommonName;
	private String csEmailAddr;
	private String csFirstName;
	private String csLastName;
	private String csPhoneNbr;
	private String csUserId;

	/**
	 * @return the csCommonName
	 */
	public String getCommonName()
	{
		return csCommonName;
	}

	/**
	 * @param asCommonName
	 *            the asCommonName to set
	 */
	public void setCommonName(String asCommonName)
	{
		this.csCommonName = asCommonName;
	}

	/**
	 * @return the csEmailAddr
	 */
	public String getEmailAddr()
	{
		return csEmailAddr;
	}

	/**
	 * @param asEmailAddr
	 *            the asEmailAddr to set
	 */
	public void setEmailAddr(String asEmailAddr)
	{
		this.csEmailAddr = asEmailAddr;
	}

	/**
	 * @return the csFirstName
	 */
	public String getFirstName()
	{
		return csFirstName;
	}

	/**
	 * @param asFirstName
	 *            the asFirstName to set
	 */
	public void setFirstName(String asFirstName)
	{
		this.csFirstName = asFirstName;
	}

	/**
	 * @return the csLastName
	 */
	public String getLastName()
	{
		return csLastName;
	}

	/**
	 * @param asLastName
	 *            the asLastName to set
	 */
	public void setLastName(String asLastName)
	{
		this.csLastName = asLastName;
	}

	/**
	 * @return the csPhoneNbr
	 */
	public String getPhoneNbr()
	{
		return csPhoneNbr;
	}
	
	// defect 10980
	/**
     * @return the csPhoneNbr as digits only
     */
	public String getPhoneNbrDigitsOnly()
	{
		String lsFormattedPhone = null;

		if (csPhoneNbr != null)
		{
			lsFormattedPhone = csPhoneNbr;
			lsFormattedPhone = this.removeChar(lsFormattedPhone, "(");
			lsFormattedPhone = this.removeChar(lsFormattedPhone, ")");
			lsFormattedPhone = this.removeChar(lsFormattedPhone, "-");
			lsFormattedPhone = this.removeChar(lsFormattedPhone, " ");
		}

		return lsFormattedPhone;
	}

	// end defect 10980

	/**
     * @return the csPhoneNbr with no hyphens
     */
	public String getPhoneNbrNoHyphens()
	{
		// defect 10980
		String lsFormattedPhone = null;

		if (csPhoneNbr != null)
		{
			lsFormattedPhone = this.removeChar(lsFormattedPhone, "-");
		}

		return lsFormattedPhone;
		// end defect 10980
	}

	/**
	 * @param asPhoneNbr
	 *            the asPhoneNbr to set
	 */
	public void setPhoneNbr(String asPhoneNbr)
	{
		this.csPhoneNbr = asPhoneNbr;
	}

	/**
	 * @return the csUserId
	 */
	public String getUserId()
	{
		return csUserId;
	}

	/**
	 * @param asUserId
	 *            the asUserId to set
	 */
	public void setUserId(String asUserId)
	{
		this.csUserId = asUserId;
	}
	
	// defect 10980
	/**
     * Remove the character from the String.
     * 
     * @param asStringToFormat
     * @param asCharToRemove
     * @return Original String without character
     */
	private String removeChar(String asStringToFormat, String asCharToRemove)
	{
		StringBuffer laFormattedString = new StringBuffer(asStringToFormat);

		while (laFormattedString.indexOf(asCharToRemove) > -1)
		{
			laFormattedString.deleteCharAt(laFormattedString
				.indexOf(asCharToRemove));
		}

		return laFormattedString.toString();
	}
	// end defect 10980
}
