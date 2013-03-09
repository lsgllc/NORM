package com.txdot.isd.rts.services.reports.funds;

import com.txdot.isd.rts.services.data.PaymentSummaryData;

/*
 * 
 * PaymentSummaryReportData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell 	09/27/2001	Added FeeSourceCdDesc (to map to RTSI SQL)
 * S Johnston	05/12/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	06/30/2005	Moved PaymentSummaryData from services.db
 * 							to services.data
 * 							defect 8163 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */
/**
 * This class contains the data for payment summary report
 *
 * @version 5.2.3		06/32/2005
 * @author	Kathy Harrell
 * <br>Creation Date: 	09/19/2001 16:28:51
 */
public class PaymentSummaryReportData extends PaymentSummaryData
{
	protected String csFeeSourceCdDesc;
	protected String csPymntTypeCdDesc;

	/**
	 * Get Fee Source Code Description
	 * 
	 * @return String
	 */
	public String getFeeSourceCdDesc()
	{
		return csFeeSourceCdDesc;
	}
	
	/**
	 * Get Pymnt Type Code Description
	 * 
	 * @return String
	 */
	public String getPymntTypeCdDesc()
	{
		return csPymntTypeCdDesc;
	}
	
	/**
	 * Set Fee Source Code Description
	 * 
	 * @param asFeeSourceCdDesc String
	 */
	public void setFeeSourceCdDesc(String asFeeSourceCdDesc)
	{
		csFeeSourceCdDesc = asFeeSourceCdDesc;
	}
	
	/**
	 * Set Pymnt Type Code Description
	 * 
	 * @param asPymntTypeCdDesc String
	 */
	public void setPymntTypeCdDesc(String asPymntTypeCdDesc)
	{
		csPymntTypeCdDesc = asPymntTypeCdDesc;
	}
}