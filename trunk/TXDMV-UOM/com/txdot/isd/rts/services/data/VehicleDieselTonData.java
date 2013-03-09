package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * VehicleDieselTonData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							Moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * VehicleDieselTonData
 *
 * @version	5.2.3		06/19/2005
 * @author	Kathy Harrell 
 * <br>Creation Date: 	08/30/2001
 */

public class VehicleDieselTonData implements Serializable
{
	// int
	protected int ciDieselFeePrcnt;
	protected int ciRegClassCd;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;

	// Object 
	protected Dollar caBegDieselVehTon;
	protected Dollar caEndDieselVehTon;

	private final static long serialVersionUID = -7976200876731375694L;
	/**
	 * Returns the value of BegDieselVehTon
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getBegDieselVehTon()
	{
		return caBegDieselVehTon;
	}
	/**
	 * Returns the value of DieselFeePrcnt
	 * 
	 * @return  int  
	 */
	public final int getDieselFeePrcnt()
	{
		return ciDieselFeePrcnt;
	}
	/**
	 * Returns the value of EndDieselVehTon
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getEndDieselVehTon()
	{
		return caEndDieselVehTon;
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
	 * This method sets the value of BegDieselVehTon.
	 * 
	 * @param aaBegDieselVehTon   Dollar  
	 */
	public final void setBegDieselVehTon(Dollar aaBegDieselVehTon)
	{
		caBegDieselVehTon = aaBegDieselVehTon;
	}
	/**
	 * This method sets the value of DieselFeePrcnt.
	 * 
	 * @param aaDieselFeePrcnt   int  
	 */
	public final void setDieselFeePrcnt(int aaDieselFeePrcnt)
	{
		ciDieselFeePrcnt = aaDieselFeePrcnt;
	}
	/**
	 * This method sets the value of EndDieselVehTon.
	 * 
	 * @param aaEndDieselVehTon   Dollar  
	 */
	public final void setEndDieselVehTon(Dollar aaEndDieselVehTon)
	{
		caEndDieselVehTon = aaEndDieselVehTon;
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
