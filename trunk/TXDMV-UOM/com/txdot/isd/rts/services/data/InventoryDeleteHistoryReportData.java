package com.txdot.isd.rts.services.data;

/*
 * InventoryDeleteHistoryReportData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * K Harrell	06/30/2005	Moved InventoryHistoryData from server.db
 * 							to services.data
 * 							defect 7899 Ver 5.2.3 
 * Ray Rowehl	09/20/2005	Moved to services.data since this is a 
 * 							data class.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
 
/**
 * Class holds all data for the Inventory Delete History Report
 *
 * @version	5.2.3			09/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 10:19:27
 */

public class InventoryDeleteHistoryReportData
	extends InventoryHistoryData
{
	protected String csItmCdDesc;
	protected String csDelInvReasn;

	/**
	 * Returns the value of DelInvReasn
	 * 
	 * @return String
	 */
	public final String getDelInvReasn()
	{
		return csDelInvReasn;
	}
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
	 * This method sets the value of DelInvReasn
	 * 
	 * @param asDelInvReasn String
	 */
	public final void setDelInvReasn(String asDelInvReasn)
	{
		csDelInvReasn = asDelInvReasn;
	}

	/**
	 * This method sets the value of ItmCdDesc
	 * 
	 * @param asItmCdDesc String
	 */
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
}