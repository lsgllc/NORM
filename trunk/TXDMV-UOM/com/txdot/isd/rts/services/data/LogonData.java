package com.txdot.isd.rts.services.data;

/*
 * LogonData.java
 * 
 * (c) Texas Department of Transportation  2001 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * RHicks      	06/13/2002  Retrieve time from WAS rather than Mainframe
 *							defect 3972
 * Ray Rowehl	09/25/2003	Change "name" to be UserId to reflect data 
 *							field.
 *							add getUserName, setUserName
 *							delete getName, setName
 *							defect 6445 Ver  5.1.6
 * Ray Rowehl	01/21/2004	Add get and set for name back in for 5.1.5 
 *							Fix2
 *							add getName(), setName()
 *							defect 6445 Ver 5.1.6
 * Ray Rowehl	02/17/2004	Reformat to match updated Java Standards
 * 							defect 6445 Ver 5.1.6
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							delete cbNew
 * 							delete setNew(), isNew()
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This is the data class to handle logon requests.
 * It includes a copy of Security Data.
 *
 * @version 5.2.3  			06/19/2005
 * @author 	Michael Abernethy
 * <br>Creation date 		09/18/2001 15:46:43
 */

public class LogonData implements java.io.Serializable
{
	// int 
	private int ciOfcIssuanceNo;
	private int ciSubstaId;
	private int ciReturnCode;
	private int ciWsId;

	//	Object 
	private SecurityData caSecurityData;

	// String  
	private String csMainframeTime = null;
	private String csMainframeDate = null;
	private String csNewPass;
	private String csPassword;
	private String csTimeZone;
	private String csUserName;

	private final static long serialVersionUID = 503154469184638448L;
	
	/**
	 * LogonData constructor comment.
	 */
	public LogonData()
	{
		super();
	}
	/**
	 * Return the value of MainframeDate
	 * 
	 * @return String
	 */
	public String getMainframeDate()
	{
		return csMainframeDate;
	}
	/**
	 * Return the value of MainframeTime
	 * 
	 * @return String
	 */
	public String getMainframeTime()
	{
		return csMainframeTime;
	}
	/**
	 * This method returns the UserName stored.
	 * 
	 * @return String
	 */
	public String getName()
	{
		return csUserName;
	}
	/**
	 * Return the value of NewPass
	 * 
	 * @return String
	 */
	public String getNewPass()
	{
		return csNewPass;
	}
	/**
	 * Return the value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return the value of Password
	 * 
	 * @return String
	 */
	public String getPassword()
	{
		return csPassword;
	}
	/**
	 * Return the value of ReturnCode
	 * 
	 * @return int
	 */
	public int getReturnCode()
	{
		return ciReturnCode;
	}
	/**
	 * Return the value of SecurityData
	 * 
	 * @return SecurityData
	 */
	public SecurityData getSecurityData()
	{
		return caSecurityData;
	}
	/**
	 * Return the value of SubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Return the value of TimeZone
	 * 
	 * @return String
	 */
	public String getTimeZone()
	{
		return csTimeZone;
	}
	/**
	 * This method returns the UserName stored.
	 *
	 * @return String
	 */
	public String getUserName()
	{
		return csUserName;
	}
	/**
	 * Return the value of WsId
	 * 
	 * @return int
	 */
	public int getWsId()
	{
		return ciWsId;
	}
	/**
	 * Set the value of MainframeDate
	 * 
	 * @param asMainframeDate String
	 */
	public void setMainframeDate(String asMainframeDate)
	{
		csMainframeDate = asMainframeDate;
	}
	/**
	 * Set the value of MainframeTime
	 * 
	 * @param asMainframeTime String
	 */
	public void setMainframeTime(String asMainframeTime)
	{
		while (asMainframeTime.length() < 6)
		{
			asMainframeTime = "0" + asMainframeTime;
		}
		csMainframeTime = asMainframeTime;
	}
	/**
	 * Set the UserName field.
	 * 
	 * @param asName String
	 */
	public void setName(String asName)
	{
		csUserName = asName;
	}
	/**
	 * Set the value of NewPass
	 * 
	 * @param asNewPass String
	 */
	public void setNewPass(String asNewPass)
	{
		csNewPass = asNewPass;
	}
	/**
	 * Set the value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Set the value of Password
	 * 
	 * @param asPassword String
	 */
	public void setPassword(String asPassword)
	{
		csPassword = asPassword;
	}
	/**
	 * Set the value of ReturnCode
	 * 
	 * @param aiReturnCode int
	 */
	public void setReturnCode(int aiReturnCode)
	{
		ciReturnCode = aiReturnCode;
	}
	/**
	 * Set the value of SecurityData
	 * 
	 * @param aaSecurityData SecurityData
	 */
	public void setSecurityData(SecurityData aaSecurityData)
	{
		caSecurityData = aaSecurityData;
	}
	/**
	 * Set the value of SubstaId
	 * 
	 * @param aiSubstaId int
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * Set the value of TimeZone 
	 * 
	 * @param asTimeZone String
	 */
	public void setTimeZone(String asTimeZone)
	{
		csTimeZone = asTimeZone;
	}
	/**
	 * Set the UserName field.
	 * 
	 * @param asName String
	 */
	public void setUserName(String asName)
	{
		csUserName = asName;
	}
	/**
	 * Set the value of WsId
	 * 
	 * @param aiWsId int
	 */
	public void setWsId(int aiWsId)
	{
		ciWsId = aiWsId;
	}
}
