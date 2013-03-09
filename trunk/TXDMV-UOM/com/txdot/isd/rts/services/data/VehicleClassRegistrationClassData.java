package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * VehicleClassRegistrationClassData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * B Hargrove	09/23/2010	Add column DfltRegClassCdIndi 
 * 							add ciDfltRegClassCdIndi, get/set method
 * 							defect 10600 Ver 6.6.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * VehicleClassRegistrationClassData
 *
 * @version	Ver 6.6.0		09/23/2010
 * @author	Kathy Harrell 
 * <br>Creation Date: 		09/06/2001 10:43:32 
 */

public class VehicleClassRegistrationClassData
	implements Serializable, java.lang.Comparable
{
	// int 
	// defect 10600
	protected int ciDfltRegClassCdIndi;
	// end defect 10600
	protected int ciRegClassCd;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;

	// String 
	protected String csRegClassCdDesc;
	protected String csVehClassCd;

	private final static long serialVersionUID = -7336745997929168433L;

	/**
	* Sorts the Data Object by Reg Cd Desc
	*
	* @param aaObject Object  
	*/
	public int compareTo(Object aaObject)
	{
		VehicleClassRegistrationClassData laRegClassCacheData =
			(VehicleClassRegistrationClassData) aaObject;

		String lsCurrentString = csRegClassCdDesc;
		String lsCompareToString =
			laRegClassCacheData.getRegClassCdDesc();
		return lsCurrentString.compareTo(lsCompareToString);

	}
	/**
	 * Returns the value of Default Reg Class for a Veh Class indi
	 * 
	 * @return  int 
	 */
	public final int getDfltRegClassCdIndi()
	{
		return ciDfltRegClassCdIndi;
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
	 * Returns the value of RegClassCdDesc
	 * 
	 * @return  String 
	 */
	public final String getRegClassCdDesc()
	{
		return csRegClassCdDesc;
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
	 * Returns the value of VehClassCd
	 * 
	 * @return  String 
	 */
	public final String getVehClassCd()
	{
		return csVehClassCd;
	}
	/**
	 * This method sets the value of Default Reg Class for a Veh Class indi
	 * 
	 * @param aiDfltRegClassCdIndi   int 
	 */
	public final void setDfltRegClassCdIndi(int aiDfltRegClassCdIndi)
	{
		ciDfltRegClassCdIndi = aiDfltRegClassCdIndi;
	}
	/**
	 * This method sets the value of RegClassCd
	 * 
	 * @param aiRegClassCd   int 
	 */
	public final void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}
	/**
	 * This method sets the value of RegClassCdDesc
	 * 
	 * @param asRegClassCdDesc   String 
	 */
	public final void setRegClassCdDesc(String asRegClassCdDesc)
	{
		csRegClassCdDesc = asRegClassCdDesc;
	}
	/**
	 * This method sets the value of RTSEffDate
	 * 
	 * @param aiRTSEffDate   int 
	 */
	public final void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}
	/**
	 * This method sets the value of RTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate   int 
	 */
	public final void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}
	/**
	 * This method sets the value of VehClassCd
	 * 
	 * @param asVehClassCd   String 
	 */
	public final void setVehClassCd(String asVehClassCd)
	{
		csVehClassCd = asVehClassCd;
	}
}
