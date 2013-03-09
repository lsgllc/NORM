package com.txdot.isd.rts.services.data;import java.io.Serializable;/* * WebServicesActionsData.java * * (c) Texas Department of Transportation 2008 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	07/02/2008	Created class. * 							defect 9675 Ver MyPlates_POS * --------------------------------------------------------------------- *//** * Data class for RTS_ACTN table data. * * @version	MyPlates_POS	07/02/2008 * @author	Ray Rowehl * <br>Creation Date:		07/02/2008 09:24:35 *//* &WebServicesActionsData& */public class WebServicesActionsData implements Serializable{/* &WebServicesActionsData'ciActnId& */	private int ciActnId;/* &WebServicesActionsData'csActnDesc& */	private String csActnDesc;	/**	 * Return the Action Description.	 * 	 * @return String	 *//* &WebServicesActionsData.getActnDesc& */	public String getActnDesc()	{		return csActnDesc;	}		/**	 * Return the Action Id.	 * 	 * @return int	 *//* &WebServicesActionsData.getActnId& */	public int getActnId()	{		return ciActnId;	}	/**	 * Set the Action Description.	 * 	 * @param asActnDesc	 *//* &WebServicesActionsData.setActnDesc& */	public void setActnDesc(String asActnDesc)	{		csActnDesc = asActnDesc;	}	/**	 * Set the Action Id.	 * 	 * @param aiActnId	 *//* &WebServicesActionsData.setActnId& */	public void setActnId(int aiActnId)	{		ciActnId = aiActnId;	}}/* #WebServicesActionsData# */