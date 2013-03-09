package com.txdot.isd.rts.server.webapps.order.common.data;
/*
 * AbstractRequest.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/02/2007	Created class.
 * 							defect 9120 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * This is the AbstractResponse class, for "shared" webservice
 * requests.
 *
 * @version	Special Plates	03/02/2007
 * @author	Jeff Seifert
 * <br>Creation Date:		03/02/2007 14:30:00
 */
public abstract class AbstractRequest
{
	private int action = 0;
	private String version = "";

	/**
	 * Gets the action for this request.  If there are not more
	 * than one action for a given function then 0 is the default.
	 * 
	 * If there are multiple functions then a function must be
	 * specified.
	 * 
	 * @return int
	 */
	public int getAction()
	{
		return action;
	}
	
	/**
	 * A string representing the specific software 
	 * version that processes the request and generates 
	 * the response.
	 * 
	 * @return String
	 */
	public String getVersion()
	{
		return version;
	}

	/**
	 * Sets the Action for this request.  This value determines
	 * what module is called in the web service function.  This 
	 * value is required for each request that has multiple 
	 * modules.
	 * 
	 * @param aiAction int
	 */
	public void setAction(int aiAction)
	{
		action = aiAction;
	}

	/**
	 * Sets the string representing the specific software 
	 * version that processes the request and generates 
	 * the response.
	 * 
	 * @param asVersion String
	 */
	public void setVersion(String asVersion)
	{
		version = asVersion;
	}

}
