package com.txdot.isd.rts.services.data;/* * InventoryProfileReportData.java *  * (c) Texas Department of Transporation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------ ----------- -------------------------------------------- * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade * 							defect 7896 * Ray Rowehl	09/30/2005	Move this class to services.data since this  * 							is a data class. * 							defect 7890 Ver 5.2.3 * --------------------------------------------------------------------- */ /** * Data Object to receive data for Inventory Profile Report * * @version	5.2.3 			09/30/2005 * @author	Kathy Harrell * <br>Creation Date:		09/20/2001 10:59:16 *//* &InventoryProfileReportData& */public class InventoryProfileReportData extends InventoryProfileData{/* &InventoryProfileReportData'ciInvQty& */	protected int ciInvQty;/* &InventoryProfileReportData'csInvId& */	protected String csInvId;/* &InventoryProfileReportData'csItmCdDesc& */	protected String csItmCdDesc;	/**	 * Returns the value of InvId	 * 	 * @return String	 *//* &InventoryProfileReportData.getInvId& */	public final String getInvId()	{		return csInvId;	}	/**	 * Returns the value of InvQty	 * 	 * @return int	 *//* &InventoryProfileReportData.getInvQty& */	public final int getInvQty()	{		return ciInvQty;	}	/**	 * Returns the value of ItmCdDesc	 * 	 * @return String	 *//* &InventoryProfileReportData.getItmCdDesc& */	public final String getItmCdDesc()	{		return csItmCdDesc;	}	/**	 * Insert the method's description here.	 * 	 * @param asInvId String	 *//* &InventoryProfileReportData.setInvId& */	public final void setInvId(String asInvId)	{		csInvId = asInvId;	}	/**	 * This method sets the value of InvQty	 * 	 * @param aiInvQty int	 *//* &InventoryProfileReportData.setInvQty& */	public final void setInvQty(int aiInvQty)	{		ciInvQty = aiInvQty;	}	/**	 * This method sets the value of ItmCdDesc	 * 	 * @param asItmCdDesc String	 *//* &InventoryProfileReportData.setItmCdDesc& */	public final void setItmCdDesc(String asItmCdDesc)	{		csItmCdDesc = asItmCdDesc;	}}/* #InventoryProfileReportData# */