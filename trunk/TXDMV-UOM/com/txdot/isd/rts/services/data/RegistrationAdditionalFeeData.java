package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * RegistrationAdditionalFeeData.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/28/2005	Created
 *							defect 8104 Ver 5.2.2 Fix 4
 * K Harrell	06/19/2005	Moved to services.data
 * 							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This data class contains attributes and get set methods for 
 * RegistrationAdditionalFeeData
 *
 * @version	5.2.3 			06/19/2005 
 * @author	K Harrell	
 * <br>Creation Date:		03/28/2005	15:58:15
 */

public class RegistrationAdditionalFeeData implements Serializable
{
	// int
	protected int ciRegClassCd;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	
	// Object 
	protected Dollar caRegAddlFee;
	
	// String 
	protected String csAddlFeeItmCd;
	
	private final static long serialVersionUID = 8405529901295391093L;
	/**
	 * RegistrationAdditionalFeeData constructor comment.
	 */
	public RegistrationAdditionalFeeData()
	{
		super();
	}
	/**
	 * Return value of AddlFeeItmCd
	 * 
	 * @return String
	 */
	public String getAddlFeeItmCd()
	{
		return csAddlFeeItmCd;
	}
	/**
	 * Return value of RegAddlFee
	 * 
	 * @return Dollar
	 */
	public Dollar getRegAddlFee()
	{
		return caRegAddlFee;
	}
	/**
	 * Return value of RegClassCd
	 * 
	 * @return int
	 */
	public int getRegClassCd()
	{
		return ciRegClassCd;
	}
	/**
	 * Return value of RTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}
	/**
	 * Return value of RTSEffEndDate
	 * 
	 * @return int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}
	/**
	 * Assign value of AddlFeeItmCd
	 * 
	 * @param asAddlFeeItmCd String
	 */
	public void setAddlFeeItmCd(String asAddlFeeItmCd)
	{
		csAddlFeeItmCd = asAddlFeeItmCd;
	}
	/**
	 * Assign value of RegAddlFee
	 * 
	 * @param aaRegAddlFee Dollar
	 */
	public void setRegAddlFee(Dollar aaRegAddlFee)
	{
		caRegAddlFee = aaRegAddlFee;
	}
	/**
	 * Assign value of RegClassCd
	 * 
	 * @param aiRegClassCd int
	 */
	public void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}
	/**
	 * Assign value of RTSEffDate
	 * 
	 * @param aiRTSEffDate int
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}
	/**
	 * Assign value of RTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate int
	 */
	public void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}
}
