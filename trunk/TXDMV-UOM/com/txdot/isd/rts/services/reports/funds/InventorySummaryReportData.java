package com.txdot.isd.rts.services.reports.funds;

import com.txdot.isd.rts.services.data.InventorySummaryData;

/*
 * InventorySummaryReportData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	06/30/2005	Moved InventorySummaryData from services.db
 * 							to services.data
 * 							defect 8163 Ver 5.2.3 	
 * ---------------------------------------------------------------------
 */
/**
 * This class contains data for the Inventory Summary Report
 * 
 * @version	5.2.3			06/30/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:		09/19/2001 16:55:47  
 */
public class InventorySummaryReportData extends InventorySummaryData
{
	protected String csItmCdDesc;
	/**
	 * Returns the value of ItmCdDesc
	 * 
	 * @return String 
	 */
	public final String getItmCdDesc()
	{
		return csItmCdDesc;
	}
	/**
	 * This method sets the value of ItmCdDesc.
	 * 
	 * @param asItmCdDesc String 
	 */
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
}