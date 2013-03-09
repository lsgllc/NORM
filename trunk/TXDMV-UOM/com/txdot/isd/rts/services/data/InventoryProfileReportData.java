package com.txdot.isd.rts.services.data;


/*
 * InventoryProfileReportData.java
 * 
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896
 * Ray Rowehl	09/30/2005	Move this class to services.data since this 
 * 							is a data class.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
 
/**
 * Data Object to receive data for Inventory Profile Report
 *
 * @version	5.2.3 			09/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 10:59:16
 */

public class InventoryProfileReportData extends InventoryProfileData
{
	protected int ciInvQty;
	protected String csInvId;
	protected String csItmCdDesc;

	/**
	 * Returns the value of InvId
	 * 
	 * @return String
	 */
	public final String getInvId()
	{
		return csInvId;
	}

	/**
	 * Returns the value of InvQty
	 * 
	 * @return int
	 */
	public final int getInvQty()
	{
		return ciInvQty;
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
	 * Insert the method's description here.
	 * 
	 * @param asInvId String
	 */
	public final void setInvId(String asInvId)
	{
		csInvId = asInvId;
	}

	/**
	 * This method sets the value of InvQty
	 * 
	 * @param aiInvQty int
	 */
	public final void setInvQty(int aiInvQty)
	{
		ciInvQty = aiInvQty;
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
