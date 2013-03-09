package com.txdot.isd.rts.webservices.common.data;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * RtsTrackingData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/30/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * Ray Rowehl	03/19/2010	Modify constructor to ensure data
 * 							has some value.
 * 							modify RtsTrackingData(int, String, String)
 * 							defect 10401 Ver 6.4.0
 * ---------------------------------------------------------------------
 */

/**
 * This is the data for tracking performance of the web services.
 *
 * @version	6.4.0			03/19/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		05/30/2008 17:18:55
 */
public class RtsTrackingData
{
	private RTSDate caReqTimeStmp;
	private RTSDate caRespTimeStmp;
	private boolean cbSuccessful = false;
	private int ciErrMsgNo = -1;
	private int ciSAVId;
	private String csCallerId;
	private String csSessionId;

	/**
	 * RtsTrackingData.java Constructor.
	 * 
	 * <p>Populate object with the basic first load.
	 * 
	 * @param aiSAVId
	 * @param asCallerId
	 * @param asSessionId
	 */
	public RtsTrackingData(
		int aiSAVId,
		String asCallerId,
		String asSessionId)
	{
		super();
		ciSAVId = aiSAVId;
		// defect 10401
		if (asCallerId != null && asCallerId.length() > 0)
		{
			csCallerId = asCallerId;
		}
		else 
		{
			csCallerId = "Empty " + ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
		}
		if (asSessionId != null && asSessionId.length() > 0)
		{
			csSessionId = asSessionId;
		}
		else 
		{
			csSessionId = "Empty " + ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
		}
		// end defect 10401
	}

	/**
	 * Get the Caller Id.
	 * 
	 * @return String
	 */
	public String getCallerId()
	{
		return csCallerId;
	}

	/**
	 * Get the Error Message Number.
	 * 
	 * @return int
	 */
	public int getErrMsgNo()
	{
		return ciErrMsgNo;
	}

	/**
	 * Get the Request TimeStamp.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getReqTimeStmp()
	{
		return caReqTimeStmp;
	}

	/**
	 * Get the Response Timestamp.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getRespTimeStmp()
	{
		return caRespTimeStmp;
	}

	/**
	 * Get the ServiceActionVerion Id.
	 * 
	 * @return int
	 */
	public int getSAVId()
	{
		return ciSAVId;
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
	 * Method description
	 * 
	 * @return boolean
	 */
	public boolean isSuccessful()
	{
		return cbSuccessful;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setCallerId(String string)
	{
		csCallerId = string;
	}

	/**
	 * Method description
	 * 
	 * @param i
	 */
	public void setErrMsgNo(int i)
	{
		ciErrMsgNo = i;
	}

	/**
	 * Method description
	 * 
	 * @param date
	 */
	public void setReqTimeStmp(RTSDate date)
	{
		caReqTimeStmp = date;
	}

	/**
	 * Method description
	 * 
	 * @param date
	 */
	public void setRespTimeStmp(RTSDate date)
	{
		caRespTimeStmp = date;
	}

	/**
	 * Method description
	 * 
	 * @param i
	 */
	public void setSAVId(int i)
	{
		ciSAVId = i;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setSessionId(String string)
	{
		csSessionId = string;
	}

	/**
	 * Method description
	 * 
	 * @param b
	 */
	public void setSuccessful(boolean b)
	{
		cbSuccessful = b;
	}

}
