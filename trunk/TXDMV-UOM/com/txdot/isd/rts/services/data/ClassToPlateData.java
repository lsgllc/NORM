package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * ClassToPlateData.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/14/2005	Created
 * 							defect 8218 Ver 5.2.3
 * K Harrell	01/17/2008	Add column PTOElgbleIndi 
 * 							add ciPTOElgbleIndi, get/set methods 
 * 							defect 9524 Ver 3 Amigos Prep 
 * B Hargrove	09/23/2010	Add column DfltPltCdIndi 
 * 							add ciDfltPltCdIndi, get/set method
 * 							defect 10600 Ver 6.6.0
 * K Harrell	01/10/2012	add ciVTPElgbleIndi, get/set methods
 * 							defect 11231 Ver 6.10.0  
 * K Harrell	02/02/2012	Javadoc Cleanup 
 * 							defect 11231 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * ClassToPlateData
 *
 * @version	6.10.0			02/02/2012
 * @author	Kathy Harrell
 * <br>Creation Date:		06/14/2005	16:10:00
 */
public class ClassToPlateData implements Serializable
{
	
	//	int
	protected int ciRegClassCd;
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	
	// defect 9524 
	protected int ciPTOElgbleIndi;
	// end defect 9524
	
	// defect 10600
	protected int ciDfltPltCdIndi;
	// end defect 10600

	// defect 11231 
	protected int ciVTPElgbleIndi; 
	// end defect 11231
	
	// String
	protected String csRegPltCd;
	protected String csReplPltCd;

	private final static long serialVersionUID = -2341836980688300921L;
	
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
	 * This method returns the value of PTOElgbleIndi
	 * 
	 * @return ciPTOElgbleIndi
	 */
	public int getPTOElgbleIndi()
	{
		return ciPTOElgbleIndi;
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
	 * Returns the value of ReplPltCd
	 * 
	 * @return  String 
	 */
	public final String getReplPltCd()
	{
		return csReplPltCd;
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
	 * Returns the value of ciVTPElgbleIndi
	 * 
	 * @return int
	 */
	public int getVTPElgbleIndi()
	{
		return ciVTPElgbleIndi;
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
	 * This method sets the value of PTOElgbleIndi
	 * 
	 * @param aiPTOElgbleIndi
	 */
	public void setPTOElgbleIndi(int aiPTOElgbleIndi)
	{
		ciPTOElgbleIndi = aiPTOElgbleIndi;
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
	 * This method sets the value of ReplPltCd.
	 * 
	 * @param asReplPltCd   String 
	 */
	public final void setReplPltCd(String asReplPltCd)
	{
		csReplPltCd = asReplPltCd;
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
	/**
	 * This method sets the value of ciVTPElgbleIndi.
	 * 
	 * @param aiVTPElgbleIndi
	 */
	public void setVTPElgbleIndi(int aiVTPElgbleIndi)
	{
		ciVTPElgbleIndi = aiVTPElgbleIndi;
	}
}
