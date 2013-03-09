package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * RenewalEMailHstryData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	07/12/2010	Created
 * 							defect 10508 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * RenewalEMailHstryData
 *
 * @version	6.5.0 			07/12/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		07/12/2010	17:19:17
 */
public class RenewalEMailHstryData
{
	// RTSDate 
	private RTSDate caBatchDate;
	private RTSDate caProcsComplTimestmp;
	private RTSDate caProcsInitTimestmp;
	private RTSDate caTmpInsrtTimestmp;

	// int 
	private int ciRenwlEMailReqId;
	private int ciErrMsgNo;
	private int ciSuccessfulIndi;
	private int ciRenwlCount;

	/**
	 * RenewalEMailHstryData.java Constructor
	 */
	public RenewalEMailHstryData()
	{
		super();
	}

	/**
	 * Return value of caBatchDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBatchDate()
	{
		return caBatchDate;
	}

	/**
	 * Return value of caProcsComplTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getProcsComplTimestmp()
	{
		return caProcsComplTimestmp;
	}

	/**
	 * Return value of caProcsInitTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getProcsInitTimestmp()
	{
		return caProcsInitTimestmp;
	}

	/**
	 * Return value of caTmpInsrtTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getTmpInsrtTimestmp()
	{
		return caTmpInsrtTimestmp;
	}

	/**
	 * Return value of ciErrMsgNo
	 * 
	 * @return int
	 */
	public int getErrMsgNo()
	{
		return ciErrMsgNo;
	}

	/**
	 * Return value of ciRenwlCount
	 * 
	 * @return int
	 */
	public int getRenwlCount()
	{
		return ciRenwlCount;
	}

	/**
	 * Return value of ciRenwlEMailReqId
	 * 
	 * @return int
	 */
	public int getRenwlEMailReqId()
	{
		return ciRenwlEMailReqId;
	}

	/**
	 * Return value of ciSuccessfulIndi
	 * 
	 * @return int
	 */
	public int getSuccessfulIndi()
	{
		return ciSuccessfulIndi;
	}

	/**
	 * Set value of caBatchDate
	 * 
	 * @param aaBatchDate
	 */
	public void setBatchDate(RTSDate aaBatchDate)
	{
		caBatchDate = aaBatchDate;
	}

	/**
	 * Set value of caProcsComplTimestmp
	 * 
	 * @param aaProcsComplTimestmp
	 */
	public void setProcsComplTimestmp(RTSDate aaProcsComplTimestmp)
	{
		caProcsComplTimestmp = aaProcsComplTimestmp;
	}

	/**
	 * Set value of caProcsInitTimestmp
	 * 
	 * @param aaProcsInitTimestmp
	 */
	public void setProcsInitTimestmp(RTSDate aaProcsInitTimestmp)
	{
		caProcsInitTimestmp = aaProcsInitTimestmp;
	}

	/**
	 * Set value of caTmpInsrtTimestmp
	 * 
	 * @param aaTmpInsrtTimestmp
	 */
	public void setTmpInsrtTimestmp(RTSDate aaTmpInsrtTimestmp)
	{
		caTmpInsrtTimestmp = aaTmpInsrtTimestmp;
	}

	/**
	 * Set value of ciErrMsgNo
	 * 
	 * @param aiErrMsgNo
	 */
	public void setErrMsgNo(int aiErrMsgNo)
	{
		ciErrMsgNo = aiErrMsgNo;
	}

	/**
	 * Set value of ciRenwlCount
	 * 
	 * @param aiRenwlCount
	 */
	public void setRenwlCount(int aiRenwlCount)
	{
		ciRenwlCount = aiRenwlCount;
	}

	/**
	 * Set value of ciRenwlEMailReqId
	 * 
	 * @param aiRenwlEMailReqId
	 */
	public void setRenwlEMailReqId(int aiRenwlEMailReqId)
	{
		ciRenwlEMailReqId = aiRenwlEMailReqId;
	}

	/**
	 * Set value of ciSuccessfulIndi
	 * 
	 * @param aiSuccessfulIndi
	 */
	public void setSuccessfulIndi(int aiSuccessfulIndi)
	{
		ciSuccessfulIndi = aiSuccessfulIndi;
	}

}
