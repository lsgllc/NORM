package com.txdot.isd.rts.services.data;

/*
 * InventoryReceiveHistoryReportData.java
 * 
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896
 * Ray Rowehl	09/30/2005	Moved to services.data since this is a 
 * 							data class.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
 
/**
 * Data Object to receive data for Inventory Receive History Report
 *
 * @version	5.2.3 			09/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 10:15:00
 */

public class InventoryReceiveHistoryReportData
	extends InventoryHistoryData
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
	 * This method sets the value of ItmCdDesc
	 * 
	 * @param asItmCdDesc String
	 */
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
}