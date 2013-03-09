package com.txdot.isd.rts.services.reports.funds;

import com.txdot.isd.rts.services.data.CloseOutHistoryData;

/*
 * CloseOutStatisticsReportData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * S Johnston	03/11/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	04/28/2005	Funds/SQL class review
 * 							defect 8163 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Closeout Statistics Report Data.
 *
 * @version	5.2.3 			04/28/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 15:45:43 
 */
public class CloseOutStatisticsReportData extends CloseOutHistoryData
{
	// String 
	protected String csTransSinceCloseOut;

	/**
	 * Returns the value of TransSinceCloseOut
	 * 
	 * @return  String 
	 */
	public final String getTransSinceCloseOut()
	{
		return csTransSinceCloseOut;
	}
	/**
	 * This method sets the value of TransSinceCloseOut.
	 * 
	 * @param asTransSinceCloseOut String 
	 */
	public final void setTransSinceCloseOut(String asTransSinceCloseOut)
	{
		csTransSinceCloseOut = asTransSinceCloseOut;
	}
}
