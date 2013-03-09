package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * ClassToPlateDescription.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/14/2005	Created
 * 							defect 8218  Ver 5.2.3 
 * B Hargrove	09/23/2010	Add column DfltPltCdIndi 
 * 							add ciDfltPltCdIndi, get/set method
 * 							defect 10600 Ver 6.6.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * ClassToPlateDescriptionData
 *
 * @version	 Ver 6.6.0		09/23/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		06/14/2005	15:27:00
 */
public class ClassToPlateDescriptionData
	implements Serializable, Comparable
{
	// int 
	protected int ciRegClassCd;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	// defect 10600
	protected int ciDfltPltCdIndi;
	// end defect 10600

	// String
	protected String csItmCdDesc;
	protected String csRegPltCd;

	private final static long serialVersionUID = 553369346247961078L;

	/**
	* Sorts the Data Object by Plate Cd Desc
	* 
	* @param aaObject Object 
	* @return int 
	*/
	public int compareTo(Object aaObject)
	{
		ClassToPlateDescriptionData laPlateDescData =
			(ClassToPlateDescriptionData) aaObject;

		String lsCurrentString = csItmCdDesc;
		String lsCompareToString = laPlateDescData.getItmCdDesc();

		return lsCurrentString.compareTo(lsCompareToString);

	}
	/**
	 * Returns the value of Default Plate for Reg Class indi
	 * 
	 * @return  int 
	 */
	public final int getDfltPltCdIndi()
	{
		return ciDfltPltCdIndi;
	}
	/**
	 * Returns the value of ItmCdDesc
	 * 
	 * @return  String 
	 */
	public final String getItmCdDesc()
	{
		return csItmCdDesc;
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
	 * Returns the value of RegPltCd
	 * 
	 * @return  String 
	 */
	public final String getRegPltCd()
	{
		return csRegPltCd;
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
	 * This method sets the value of Default Plate for Reg Class indi
	 * 
	 * @param aiDfltPltCdIndi   int 
	 */
	public final void setDfltPltCdIndi(int aiDfltPltCdIndi)
	{
		ciDfltPltCdIndi = aiDfltPltCdIndi;
	}
	/**
	 * This method sets the value of ItmCdDesc.
	 * 
	 * @param asItmCdDesc   String 
	 */
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
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
	 * This method sets the value of RegPltCd.
	 * 
	 * @param asRegPltCd   String 
	 */
	public final void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
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
