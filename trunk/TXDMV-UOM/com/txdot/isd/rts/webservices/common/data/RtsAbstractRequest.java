package com.txdot.isd.rts.webservices.common.data;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * RtsAbstractRequest.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/27/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS 
 * Ray Rowehl	05/25/2011	Add a method to do basic validation of 
 * 							request data.
 * 							add MAX_LNGTH_CALLER, MAX_LNGTH_SESSIONID
 * 							add validateAbstractRequest()
 * 							defect 10718 Ver 6.8.0
 * ---------------------------------------------------------------------
 */

/**
 * This is the required base data for all RTS Web Service Requests.
 *
 * @version	6.8.0			05/25/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		05/27/2008 17:31:00
 */

public abstract class RtsAbstractRequest
{
	private static final int MAX_LNGTH_CALLER = 30;
	private static final int MAX_LNGTH_SESSIONID = 30;
	private int ciAction = 0;
	private int ciVersionNo = 0;
	private String csCaller = "";
	private String csSessionId = "";
	
	/**
	 * Get the Action requested.
	 * 
	 * @return int
	 */
	public int getAction()
	{
		return ciAction;
	}

	/**
	 * Get the Caller provided in the request.
	 * 
	 * @return String
	 */
	public String getCaller()
	{
		return csCaller;
	}
	
	/**
	 * Get the Session Id.
	 * 
	 * @return String
	 */
	public String getSessionId()
	{
		return csSessionId;
	}
	
	/**
	 * Get the Version requeested.
	 * 
	 * @return int
	 */
	public int getVersionNo()
	{
		return ciVersionNo;
	}


	/**
	 * Set the Action requested.
	 * 
	 * @param aiAction
	 */
	public void setAction(int aiAction)
	{
		ciAction = aiAction;
	}
	
	/**
	 * Set the Caller provided in the request.
	 * 
	 * @param asCaller
	 */
	public void setCaller(String asCaller)
	{
		csCaller = asCaller;
	}

	/**
	 * Set the Session Id.
	 * 
	 * @param asSessionId
	 */
	public void setSessionId(String asSessionId)
	{
		csSessionId = asSessionId;
	}
	
	/**
	 * Set the Version requeested.
	 * 
	 * @param aiVersionNo
	 */
	public void setVersionNo(int aiVersionNo)
	{
		ciVersionNo = aiVersionNo;
	}
	
	/**
	 * Validates the Abstract Request Data.
	 * 
	 * @throws RTSException
	 */
	public void validateAbstractRequest() throws RTSException
	{
		RTSException leRTSEx = null;

		// Make sure there is a Caller
		if (getCaller() == null || getCaller().length() < 1)
		{
			leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		else if (getCaller().length() > MAX_LNGTH_CALLER)
		{
			leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}

		// Make sure there is a SessionId
		if (getSessionId() == null || getSessionId().length() < 1)
		{
			leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}
		else if (getSessionId().length() > MAX_LNGTH_SESSIONID)
		{
			leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}

		// if there is an exception, throw it.
		if (leRTSEx != null)
		{
			throw leRTSEx;
		}
	}
}
