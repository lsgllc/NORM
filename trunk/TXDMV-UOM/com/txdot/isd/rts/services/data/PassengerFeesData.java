package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * PassengerFeesData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 			 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * PassengerFeesData
 * 
 * @version	5.2.3		06/19/2005 
 * @author	Administrator
 * <br>Creation Date: 	08/30/2001 
 */

public class PassengerFeesData implements Serializable
{
	// int
	protected int ciBegModlYr;
	protected int ciEndModlYr;
	protected int ciRegClassCd;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;

	// Object 
	protected Dollar caRegFee;

	private final static long serialVersionUID = -2446718416990791472L;

	/**
	 * Returns the value of BegModlYr
	 * 
	 * @return  int  
	 */
	public final int getBegModlYr()
	{
		return ciBegModlYr;
	}
	/**
	 * Returns the value of EndModlYr
	 * 
	 * @return  int  
	 */
	public final int getEndModlYr()
	{
		return ciEndModlYr;
	}
	/**
	 * Returns the value of RegClassCd
	 * 
	 * @return  int  
	 */
	public final int getRegClassCd()
	{
		return ciRegClassCd;
	}
	/**
	 * Returns the value of RegFee
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getRegFee()
	{
		return caRegFee;
	}
	/**
	 * Returns the value of RTSEffDate
	 * 
	 * @return  int  
	 */
	public final int getRTSEffDate()
	{
		return ciRTSEffDate;
	}
	/**
	 * Returns the value of RTSEffEndDate
	 * 
	 * @return  int  
	 */
	public final int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}
	/**
	 * This method sets the value of BegModlYr.
	 * 
	 * @param aiBegModlYr   int  
	 */
	public final void setBegModlYr(int aiBegModlYr)
	{
		ciBegModlYr = aiBegModlYr;
	}
	/**
	 * This method sets the value of EndModlYr.
	 * 
	 * @param aiEndModlYr   int  
	 */
	public final void setEndModlYr(int aiEndModlYr)
	{
		ciEndModlYr = aiEndModlYr;
	}
	/**
	 * This method sets the value of RegClassCd.
	 * 
	 * @param aiRegClassCd   int  
	 */
	public final void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}
	/**
	 * This method sets the value of RegFee.
	 * 
	 * @param aaRegFee   Dollar  
	 */
	public final void setRegFee(Dollar aaRegFee)
	{
		caRegFee = aaRegFee;
	}
	/**
	 * This method sets the value of RTSEffDate.
	 * 
	 * @param aiRTSEffDate   int  
	 */
	public final void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}
	/**
	 * This method sets the value of RTSEffEndDate.
	 * 
	 * @param aiRTSEffEndDate   int  
	 */
	public final void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}
}
