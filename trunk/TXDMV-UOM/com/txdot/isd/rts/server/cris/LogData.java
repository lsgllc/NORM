package com.txdot.isd.rts.server.cris;

/*
 *
 * LogData.java
 *
 * (c) Texas Department of Transportation 2004
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7885 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * Log Data
 *
 * @version	5.2.3			05/02/2005
 * @author	Jeff Seifert
 * <p>Creation Date:		03/31/2004 11:30:05
 */

public class LogData
{
	private java.lang.String csLogDate;
	private java.lang.String csUserId;
	private java.lang.String csLogType;
	private java.lang.String csLogMessage;
	/**
	 * LogData constructor comment.
	 */

	public LogData()
	{
		super();
	}
	/**
	 * Get Log Date
	 * 
	 * @return java.lang.String
	 */

	public java.lang.String getLogDate()
	{
		return csLogDate;
	}
	/**
	 * Get Log Message
	 * 
	 * @return java.lang.String
	 */

	public java.lang.String getLogMessage()
	{
		return csLogMessage;
	}
	/**
	 * Get Log Type
	 *  
	 * @return java.lang.String
	 */
	public java.lang.String getLogType()
	{
		return csLogType;
	}
	/**
	 * Get User ID
	 *
	 * @return java.lang.String
	 */

	public java.lang.String getUserId()
	{
		return csUserId;
	}
	/**
	 * Set Log Date
	 *
	 * @param asNewLogDate java.lang.String
	 */

	public void setLogDate(java.lang.String asNewLogDate)
	{
		csLogDate = asNewLogDate;
	}
	/**
	 * Set Log Message
	 * 
	 * @param asNewLogMessage java.lang.String
	 */

	public void setLogMessage(java.lang.String asNewLogMessage)
	{
		csLogMessage = asNewLogMessage;
	}
	/**
	 * Set Log Type
	 *  
	 * @param asNewCsLogType java.lang.String
	 */
	public void setLogType(java.lang.String asNewLogType)
	{
		csLogType = asNewLogType;
	}
	/**
	 * Set User ID
	 *
	 * @param asNewUserId java.lang.String
	 */

	public void setUserId(java.lang.String asNewUserId)
	{
		csUserId = asNewUserId;
	}
}
