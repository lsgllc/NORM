package com.txdot.isd.rts.webservices.common.data;
/*
 * RtsAbstractResponse.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/28/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS 
 * ---------------------------------------------------------------------
 */

/**
 * This is the required base data for all RTS Web Service Responses.
 *
 * @version	MyPlates_POS	05/28/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		05/28/2008 09:21:55
 */
public abstract class RtsAbstractResponse
{
	private int ciErrMsgNo = -1;
	private String csErrMsgDesc;
	private int ciVersionNo = 0;

	/**
	 * Returns the Error Message Description.
	 * 
	 * @return String
	 */
	public String getErrMsgDesc()
	{
		return csErrMsgDesc;
	}
	
	/**
	 * Returns the Error Message Number.
	 * 
	 * @return int
	 */
	public int getErrMsgNo()
	{
		return ciErrMsgNo;
	}

	/**
	 * Returns the Version Number being returned.
	 * 
	 * @return int
	 */
	public int getVersionNo()
	{
		return ciVersionNo;
	}

	/**
	 * Sets the Error Message Description.
	 * 
	 * @param asErrMsgDesc
	 */
	public void setErrMsgDesc(String asErrMsgDesc)
	{
		csErrMsgDesc = asErrMsgDesc;
	}
	
	/**
	 * Sets the Error Message Number.
	 * 
	 * @param aiErrMsgNo
	 */
	public void setErrMsgNo(int aiErrMsgNo)
	{
		ciErrMsgNo = aiErrMsgNo;
	}

	/**
	 * Set the Version Number being returned.
	 * 
	 * @param aiVersionNo
	 */
	public void setVersionNo(int aiVersionNo)
	{
		ciVersionNo = aiVersionNo;
	}
}
