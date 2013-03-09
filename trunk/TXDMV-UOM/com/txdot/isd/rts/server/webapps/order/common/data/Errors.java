package com.txdot.isd.rts.server.webapps.order.common.data;
/*
 * Errors.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/02/2007	Created class.
 * 							defect 9121 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * Error code and messages for debugging a response.
 *
 * @version	Special Plates	03/02/2007
 * @author	Jeff Seifert
 * <br>Creation Date:		03/02/2007 14:30:00
 */
public class Errors
{
	private int errorCode = 0;
	private String longMessage = "";
	private String severityCode = "";
	private String shortMessage = "";
	
	/**
	 * Token defining the error code number
	 * 
	 * @return int
	 */
	public int getErrorCode()
	{
		return errorCode;
	}

	/**
	 * String describing the error message.
	 * 
	 * @return String
	 */
	public String getLongMessage()
	{
		return longMessage;
	}

	/**
	 * Either Error or Wanring.
	 * 
	 * @return String
	 */
	public String getSeverityCode()
	{
		return severityCode;
	}

	/**
	 * String containing the error message
	 * 
	 * @return String
	 */
	public String getShortMessage()
	{
		return shortMessage;
	}

	/**
	 * Sets the token defining the error code number
	 * 
	 * @param aiErrorCode int
	 */
	public void setErrorCode(int aiErrorCode)
	{
		errorCode = aiErrorCode;
	}

	/**
	 * Sets the string describing the error message.
	 * 
	 * @param asLongMessage String
	 */
	public void setLongMessage(String asLongMessage)
	{
		longMessage = asLongMessage;
	}

	/**
	 * Sets either Error or Wanring.
	 * 
	 * @param asSeverityCode String
	 */
	public void setSeverityCode(String asSeverityCode)
	{
		severityCode = asSeverityCode;
	}

	/**
	 * Sets the string containing the error message
	 * 
	 * @param asShortMessage String
	 */
	public void setShortMessage(String asShortMessage)
	{
		shortMessage = asShortMessage;
	}

}
