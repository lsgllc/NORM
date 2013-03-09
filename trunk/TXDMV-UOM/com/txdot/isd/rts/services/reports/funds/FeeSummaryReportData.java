package com.txdot.isd.rts.services.reports.funds;

import com.txdot.isd.rts.services.data.FeeSummaryData;

/*
 * FeeSummaryReportData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * S Johnston	03/14/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	06/30/2005	Funds/SQL class review
 * 							moved FeeSummaryData from services.db
 * 							to services.data
 * 							defect 8163 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This class contains mathods that deal with Fee Summary Report Data
 *  
 * @version	5.2.3		06/30/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:	09/19/2001 16:26:04  
 */
public class FeeSummaryReportData extends FeeSummaryData
{
	// int 
	protected int ciAcctItmCrdtIndi;
	protected int ciAcctItmGrpCd;
	protected int ciPayableTypeCd;

	// String 
	protected String csAcctItmCdDesc;
	protected String csPayableTypeCdDesc;

	/**
	* Returns the value of AcctItmCrdtIndi
	* 
	* @return int 
	*/
	public final int getAcctItmCrdtIndi()
	{
		return ciAcctItmCrdtIndi;
	}
	/**
	 * Returns the value of AcctItmGrpCd
	 * 
	 * @return int 
	 */
	public final int getAcctItmGrpCd()
	{
		return ciAcctItmGrpCd;
	}
	/**
	 * This method sets the value of AcctItmCrdtIndi.
	 * 
	 * @param aiAcctItmCrdtIndi int 
	 */
	public final void setAcctItmCrdtIndi(int aiAcctItmCrdtIndi)
	{
		ciAcctItmCrdtIndi = aiAcctItmCrdtIndi;
	}
	/**
	 * This method sets the value of AcctItmGrpCd.
	 * 
	 * @param aiAcctItmGrpCd int 
	 */
	public final void setAcctItmGrpCd(int aiAcctItmGrpCd)
	{
		ciAcctItmGrpCd = aiAcctItmGrpCd;
	}
	/**
	 * This method sets the value of PayableTypeCd
	 * 
	 * @return int
	 */
	public int getPayableTypeCd()
	{
		return ciPayableTypeCd;
	}
	/**
	 * Get Payable Type Cd Description
	 * 
	 * @return String
	 */
	public String getPayableTypeCdDesc()
	{
		return csPayableTypeCdDesc;
	}
	/**
	 * This method sets the value of PayableTypeCd
	 * 
	 * @param aiPayableTypeCd int
	 */
	public void setPayableTypeCd(int aiPayableTypeCd)
	{
		ciPayableTypeCd = aiPayableTypeCd;
	}
	/**
	 * This method sets the value of PayableTypeCdDesc
	 * 
	 * @param asPayableTypeCdDesc String
	 */
	public void setPayableTypeCdDesc(String asPayableTypeCdDesc)
	{
		csPayableTypeCdDesc = asPayableTypeCdDesc;
	}
	/**
	 * This method gets the value of AcctItmCdDesc
	 * 
	 * @return String
	 */
	public String getAcctItmCdDesc()
	{
		return csAcctItmCdDesc;
	}
	/**
	 * Set Account Item Cd Description
	 * 
	 * @param asNewAcctItmCdDesc String
	 */
	public void setAcctItmCdDesc(String asNewAcctItmCdDesc)
	{
		csAcctItmCdDesc = asNewAcctItmCdDesc;
	}
}