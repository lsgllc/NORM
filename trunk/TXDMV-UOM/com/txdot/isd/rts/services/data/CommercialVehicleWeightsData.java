package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * CommercialVehicleWeightsData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * CommercialVehicleWeightsData
 *
 * @version	5.2.3		06/19/2005
 * @author	Administrator
 * <br>Creation Date:	08/30/2001 
 */

public class CommercialVehicleWeightsData implements Serializable
{
	// int
	protected int ciMinCaryngCap;
	protected int ciMinGrossWtAllowble;

	// Object 
	protected Dollar caVehTon;

	// String
	protected String csVehTonDesc;

	private final static long serialVersionUID = -644606155629520523L;

	/**
	 * Returns the value of MinCaryngCap
	 * 
	 * @return  int  
	 */
	public final int getMinCaryngCap()
	{
		return ciMinCaryngCap;
	}
	/**
	 * Returns the value of MinGrossWtAllowble
	 * 
	 * @return  int  
	 */
	public final int getMinGrossWtAllowble()
	{
		return ciMinGrossWtAllowble;
	}
	/**
	 * Returns the value of VehTon
	 * 
	 * @return  Dollar  
	 */
	public final Dollar getVehTon()
	{
		return caVehTon;
	}
	/**
	 * Returns the value of VehTonDesc
	 * 
	 * @return  String 
	 */
	public final String getVehTonDesc()
	{
		return csVehTonDesc;
	}
	/**
	 * This method sets the value of MinCaryngCap.
	 * 
	 * @param aiMinCaryngCap   int  
	 */
	public final void setMinCaryngCap(int aiMinCaryngCap)
	{
		ciMinCaryngCap = aiMinCaryngCap;
	}
	/**
	 * This method sets the value of MinGrossWtAllowble.
	 * 
	 * @param aiMinGrossWtAllowble   int  
	 */
	public final void setMinGrossWtAllowble(int aiMinGrossWtAllowble)
	{
		ciMinGrossWtAllowble = aiMinGrossWtAllowble;
	}
	/**
	 * This method sets the value of VehTon.
	 * 
	 * @param aaVehTon   Dollar  
	 */
	public final void setVehTon(Dollar aaVehTon)
	{
		caVehTon = aaVehTon;
	}
	/**
	 * This method sets the value of VehTonDesc.
	 * 
	 * @param asVehTonDesc   String 
	 */
	public final void setVehTonDesc(String asVehTonDesc)
	{
		csVehTonDesc = asVehTonDesc;
	}
}
