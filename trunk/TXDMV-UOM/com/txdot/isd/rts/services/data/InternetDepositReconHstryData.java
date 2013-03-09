package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * InternetDepositReconHstryData.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/12/2009	Created 
 * 							defect 9935 Ver Defect_POS_D 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * InternetDepositReconHstryData
 *
 * @version	Defect_POS_D	02/12/2009 
 * @author	Kathy Harrell 
 * <br>Creation Date:		02/12/2009
 */
public class InternetDepositReconHstryData implements Serializable
{

	// RTSDate 
	private RTSDate caBankDepositDate;
	private RTSDate caProcsComplTimestmp;
	private RTSDate caProcsInitTimestmp;
	private RTSDate caTmpInsrtTimestmp;
	// int 
	private int ciDepositReconHstryReqId;
	private int ciErrMsgNo;
	private int ciSuccessfulIndi;
	private int ciTransCount;

	/**
	 * Gets value of caBankDepositDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBankDepositDate()
	{
		return caBankDepositDate;
	}

	/**
	 * Gets value of ciDepositReconHstryReqId
	 * 
	 * @return int 
	 */
	public int getDepositReconHstryReqId()
	{
		return ciDepositReconHstryReqId;
	}

	/**
	 * Gets value of ciErrMsgNo
	 * 
	 * @return int
	 */
	public int getErrMsgNo()
	{
		return ciErrMsgNo;
	}

	/**
	 * Gets value of caProcsComplTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getProcsComplTimestmp()
	{
		return caProcsComplTimestmp;
	}

	/**
	 * Gets value of caProcsInitTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getProcsInitTimestmp()
	{
		return caProcsInitTimestmp;
	}

	/**
	 * Gets value of ciSuccessfulIndi
	 * 
	 * @return int
	 */
	public int getSuccessfulIndi()
	{
		return ciSuccessfulIndi;
	}

	/**
	 * Gets value of caTmpInsrtTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getTmpInsrtTimestmp()
	{
		return caTmpInsrtTimestmp;
	}

	/**
	 * Gets value of ciTransCount
	 * 
	 * @return int
	 */
	public int getTransCount()
	{
		return ciTransCount;
	}

	/**
	 * Sets value of caBankDepositDate
	 * 
	 * @param aaBankDepositDate
	 */
	public void setBankDepositDate(RTSDate aaBankDepositDate)
	{
		caBankDepositDate = aaBankDepositDate;
	}

	/**
	 * Sets value of ciDepositReconHstryReqId
	 * 
	 * @param aiDepositReconHstryReqId
	 */
	public void setDepositReconHstryReqId(int aiDepositReconHstryReqId)
	{
		ciDepositReconHstryReqId = aiDepositReconHstryReqId;
	}

	/**
	 * Sets value of ciErrMsgNo
	 * 
	 * @param aiErrMsgNo
	 */
	public void setErrMsgNo(int aiErrMsgNo)
	{
		ciErrMsgNo = aiErrMsgNo;
	}

	/**
	 * Sets value of caProcsComplTimestmp
	 * 
	 * @param aaProcsComplTimestmp
	 */
	public void setProcsComplTimestmp(RTSDate aaProcsComplTimestmp)
	{
		caProcsComplTimestmp = aaProcsComplTimestmp;
	}

	/**
	 * Sets value of caProcsInitTimestmp
	 * 
	 * @param aaProcsInitTimestmp
	 */
	public void setProcsInitTimestmp(RTSDate aaProcsInitTimestmp)
	{
		caProcsInitTimestmp = aaProcsInitTimestmp;
	}

	/**
	 * Sets value of ciSuccessfulIndi
	 * 
	 * @param aiSuccessfulIndi
	 */
	public void setSuccessfulIndi(int aiSuccessfulIndi)
	{
		ciSuccessfulIndi = aiSuccessfulIndi;
	}

	/**
	 * Sets value of caTmpInsrtTimestmp
	 * 
	 * @param aaTmpInsrtTimestmp
	 */
	public void setTmpInsrtTimestmp(RTSDate aaTmpInsrtTimestmp)
	{
		caTmpInsrtTimestmp = aaTmpInsrtTimestmp;
	}

	/**
	 * Sets value of ciTransCount
	 * 
	 * @param aiTransCount
	 */
	public void setTransCount(int aiTransCount)
	{
		ciTransCount = aiTransCount;
	}
}
