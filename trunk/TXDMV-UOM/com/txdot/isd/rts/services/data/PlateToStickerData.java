package com.txdot.isd.rts.services.data;

import java.io.Serializable;
/*
 * PlateToStickerData.java
 *
 * (c) Texas Department of Transportation 2005
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/14/2005 	Created
 * 							defect 8218 Ver 5.2.3 
 * B Hargrove	09/23/2010	Add column DfltStkrCdIndi 
 * 							add ciDfltStkrCdIndi, get/set method
 * 							defect 10600 Ver 6.6.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * PlateToStickerData.
 *
 * @version	Ver 6.6.0 		09/23/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		06/14/2005 16:47:00
 */
public class PlateToStickerData implements Serializable
{
	//	int
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;
	// defect 10600
	protected int ciDfltStkrCdIndi;
	// end defect 10600

	// String
	protected String csAcctItmCd;
	protected String csRegPltCd;
	protected String csRegStkrCd;
	protected String csRegStkrCdDesc;

	private final static long serialVersionUID = 631659045897873386L;

	/**
	 * Returns the value of AcctItmCd
	 * 
	 * @return  String 
	 */
	public final String getAcctItmCd()
	{
		return csAcctItmCd;
	}
	
	/**
	 * Returns the value of Default Sticker for a Plate indi
	 * 
	 * @return  int 
	 */
	public final int getDfltStkrCdIndi()
	{
		return ciDfltStkrCdIndi;
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
	 * Returns the value of RegStkrCd
	 * 
	 * @return  String 
	 */
	public final String getRegStkrCd()
	{
		return csRegStkrCd;
	}

	/**
	 * Returns the value of RegStkrCdDesc
	 * 
	 * @return  String 
	 */
	public final String getRegStkrCdDesc()
	{
		return csRegStkrCdDesc;
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
	 * This method sets the value of AcctItmCd.
	 * 
	 * @param asAcctItmCd   String 
	 */
	public final void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}

	/**
	 * This method sets the value of Default Sticker for a Plate indi
	 * 
	 * @param aiDfltStkrCdIndi   int  
	 */
	public final void setDfltStkrCdIndi(int aiDfltStkrCdIndi)
	{
		ciDfltStkrCdIndi = aiDfltStkrCdIndi;
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
	 * This method sets the value of RegStkrCd.
	 * 
	 * @param asRegStkrCd   String 
	 */
	public final void setRegStkrCd(String asRegStkrCd)
	{
		csRegStkrCd = asRegStkrCd;
	}

	/**
	 * This method sets the value of RegStkrCdDesc.
	 * 
	 * @param asRegStkrCdDesc   String 
	 */
	public final void setRegStkrCdDesc(String asRegStkrCdDesc)
	{
		csRegStkrCdDesc = asRegStkrCdDesc;
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
