package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * VehicleClassSpclPltTypeDescriptionData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/10/2007	Created;
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/23/2007	No longer extends PlateTypeData
 * 							defect 9085 Ver Special Plates   
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * PlateTypeData joined to RegisClass and ItemCodes and ClassToPlt
 *
 * @version	Special Plates	02/23/2007
 * @author	Kathy Harrell
 * <br>Creation Date:		02/10/2007 17:37:00
 */

public class VehicleClassSpclPltTypeDescriptionData
	implements Serializable
{
	static final long serialVersionUID = -3840374571236397169L;

	// String
	protected String csVehClassCd;
	protected String csRegPltCd;
	protected String csRegPltCdDesc;

	// int 
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;

	/**
	 * Return value of csVehClassCd
	 * 
	 * @return String
	 */
	public String getVehClassCd()
	{
		return csVehClassCd;
	}

	/**
	 * Set value of csVehClassCd
	 * 
	 * @param asVehClassCd
	 */
	public void setVehClassCd(String asVehClassCd)
	{
		csVehClassCd = asVehClassCd;
	}

	/**
	 * Return value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Return value of ciRTSEffEndDate
	 * 
	 * @return int
	 */
	public int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}

	/**
	 * Return value of csRegPltCd
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Return value of csRegPltCdDesc
	 * 
	 * @return String
	 */
	public String getRegPltCdDesc()
	{
		return csRegPltCdDesc;
	}

	/**
	 * Set value of ciRTSEffDate
	 * 
	 * @param aiRTSEffDate
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * Set value of ciRTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate
	 */
	public void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}

	/**
	 * Set value of csRegPltCd
	 * 
	 * @param asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Set value of csRegPltCdDesc
	 * 
	 * @param asRegPltCdDesc
	 */
	public void setRegPltCdDesc(String asRegPltCdDesc)
	{
		csRegPltCdDesc = asRegPltCdDesc;
	}
}
