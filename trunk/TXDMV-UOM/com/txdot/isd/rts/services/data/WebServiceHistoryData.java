package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.webservices.common.data.RtsTrackingData;

/*
 * WebServiceHistoryData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/24/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * Ray Rowehl	01/27/2009	Add an Identity field for database indentity
 * 							tracking.
 * 							Sort members.
 * 							add ciSavReqId
 * 							add getSavReqId(), setSavReqId()
 * 							defect 9804 Ver Defect_POS_D
 * ---------------------------------------------------------------------
 */

/**
 * Data class for RTS_SVC_HSTRY table data.
 *
 * @version	MyPlates_POS	01/27/2009
 * @author	Ray Rowehl
 * <br>Creation Date:		06/24/2008 15:55:11
 */
public class WebServiceHistoryData implements Serializable
{
	private RTSDate caReqTimeStmp;
	private RTSDate caRespTimeStmp;
	private int ciErrMsgNo;
	// defect 9804
	private int ciSavReqId;
	// end defect 9804
	private int ciSAVId;
	private int ciSuccessfulIndi;
	private String csCallerId;
	private String csSessionId;

	/**
	 * WebServiceHistoryData.java Constructor
	 * 
	 * @param aiSavId
	 * @param asCaller
	 * @param asSessionId
	 */
	public WebServiceHistoryData(
		int aiSavId,
		String asCaller,
		String asSessionId)
	{
		super();
		this.setSAVId(aiSavId);
		this.setCallerId(asCaller);
		this.setSessionId(asSessionId);
	}
	
	/**
	 * WebServiceHistoryData.java Constructor
	 * 
	 * @param aaRTD
	 */
	public WebServiceHistoryData(RtsTrackingData aaRTD)
	{
		super();
		this.setReqTimeStmp(aaRTD.getReqTimeStmp());
		this.setRespTimeStmp(aaRTD.getRespTimeStmp());
		this.setErrMsgNo(aaRTD.getErrMsgNo());
		this.setCallerId(aaRTD.getCallerId());
		this.setSAVId(aaRTD.getSAVId());
		this.setSessionId(aaRTD.getSessionId());
		this.setSuccessfulIndi(aaRTD.isSuccessful());
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
	 * Get the SavReqId field.
	 * 
	 * @return int
	 */
	public int getSavReqId()
	{
		return ciSavReqId;
	}

	/**
	 * Get the Request Time Stamp.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getReqTimeStmp()
	{
		return caReqTimeStmp;
	}

	/**
	 * Get the Response Time Stamp.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getRespTimeStmp()
	{
		return caRespTimeStmp;
	}

	/**
	 * Get the Service - Action - Version Id.
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
	 * @return string
	 */
	public String getSessionId()
	{
		return csSessionId;
	}

	/**
	 * Get the Successful Indicator.
	 * 
	 * @return int
	 */
	public int getSuccessfulIndi()
	{
		return ciSuccessfulIndi;
	}

	/**
	 * Set the Caller Id.
	 * 
	 * @param asCallerId
	 */
	public void setCallerId(String asCallerId)
	{
		csCallerId = asCallerId;
	}

	/**
	 * Set the Error Message Number.
	 * 
	 * @param aiErrMsgNo
	 */
	public void setErrMsgNo(int aiErrMsgNo)
	{
		ciErrMsgNo = aiErrMsgNo;
	}

	/**
	 * Set the SavReqId field.
	 * 
	 * @param aiSavReqId
	 */
	public void setSavReqId(int aiSavReqId)
	{
		ciSavReqId = aiSavReqId;
	}

	/**
	 * Set the Request Time Stamp.
	 * 
	 * @param aaReqTimeStmp
	 */
	public void setReqTimeStmp(RTSDate aaReqTimeStmp)
	{
		caReqTimeStmp = aaReqTimeStmp;
	}

	/**
	 * Set the Response Time Stamp.
	 * 
	 * @param aaRespTimeStmp
	 */
	public void setRespTimeStmp(RTSDate aaRespTimeStmp)
	{
		caRespTimeStmp = aaRespTimeStmp;
	}

	/**
	 * Set the Service - Action - Version Id.
	 * 
	 * @param aiSAVId
	 */
	public void setSAVId(int aiSAVId)
	{
		ciSAVId = aiSAVId;
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
	 * Set the Successful Indicator from boolean.
	 * 
	 * @param abSuccessfulIndi
	 */
	public void setSuccessfulIndi(boolean abSuccessfulIndi)
	{
		int liSuccessIndi = 0;
		if (abSuccessfulIndi)
		{
			liSuccessIndi = 1;
		}
		setSuccessfulIndi(liSuccessIndi);
	}

	/**
	 * Set the Successful Indicator.
	 * 
	 * @param aiSuccessfulIndi
	 */
	public void setSuccessfulIndi(int aiSuccessfulIndi)
	{
		ciSuccessfulIndi = aiSuccessfulIndi;
	}
}
