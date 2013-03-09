package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import com.txdot.isd.rts.services.util.*;

/*
 *
 * RegistrationWeightFeesData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Tulsiani	08/28/2001	Added Javadoc comments
 * K Harrell    08/31/2001	Altered RTSDate to int for RTSEffDate, etc.
 *                          Altered Weights to int; 
 * 							import services.util.*; 
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 			 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * RegistrationWeightFeesData
 * 
 * @version	5.2.3		06/19/2005 
 * @author	Bobby Tulsiani
 * <br>Creation Date: 	08/24/2001 16:44:52 
 */

public class RegistrationWeightFeesData implements Serializable
{
	// int
	protected int ciBegWtRnge;
	protected int ciEndWtRnge;
	protected int ciRegClassCd;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;

	// Object 
	protected Dollar caTireTypeFee;

	// String 
	protected String csTireTypeCd;

	private final static long serialVersionUID = -3567335360648803591L;
	/**
	 * RegWtFeesData constructor comment.
	 */
	public RegistrationWeightFeesData()
	{
		super();
	}
	/**
	 * Return the value of BegWtRnge
	 * 
	 * @return int
	 */
	public int getBegWtRnge()
	{
		return ciBegWtRnge;
	}
	/**
	 * Return the value of EndWtRnge
	 * 
	 * @return int
	 */
	public int getEndWtRnge()
	{
		return ciEndWtRnge;
	}
	/**
	 * Return the value of RegClassCd
	 * 
	 * @return int
	 */
	public int getRegClassCd()
	{
		return ciRegClassCd;
	}
	/**
	 * Return the value of RTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}
	/**
	 * Return the value of RTSEffEndDate
	 * 
	 * @return int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}
	/**
	 * Return the value of TireTypeCd
	 * 
	 * @return String
	 */
	public String getTireTypeCd()
	{
		return csTireTypeCd;
	}
	/**
	 * Return the value of TireTypeFee
	 * 
	 * @return Dollar
	 */
	public Dollar getTireTypeFee()
	{
		return caTireTypeFee;
	}
	/**
	 * Return the value of 
	 * 
	 * @param aiBegWtRnge int
	 */
	public void setBegWtRnge(int aiBegWtRnge)
	{
		ciBegWtRnge = aiBegWtRnge;
	}
	/**
	 * Return the value of 
	 * 
	 * @param aiEndWtRnge int
	 */
	public void setEndWtRnge(int aiEndWtRnge)
	{
		ciEndWtRnge = aiEndWtRnge;
	}
	/**
	 * Return the value of 
	 * 
	 * @param aiRegClassCd int
	 */
	public void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}
	/**
	 * Return the value of 
	 * 
	 * @param aiRTSEffDate int
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}
	/**
	 * Return the value of 
	 * 
	 * @param aiRTSEffEndDate int
	 */
	public void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}
	/**
	 * Return the value of 
	 * 
	 * @param asTireTypeCd String
	 */
	public void setTireTypeCd(String asTireTypeCd)
	{
		csTireTypeCd = asTireTypeCd;
	}
	/**
	 * Return the value of 
	 * 
	 * @param aaTireTypeFee Dollar
	 */
	public void setTireTypeFee(Dollar aaTireTypeFee)
	{
		caTireTypeFee = aaTireTypeFee;
	}
}
