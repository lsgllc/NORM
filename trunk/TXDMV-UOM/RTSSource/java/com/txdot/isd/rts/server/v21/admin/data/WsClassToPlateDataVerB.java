package com.txdot.isd.rts.server.v21.admin.data;/* * WsClassToPlateDataVerB.java * * (c) Texas Department of MotorVehicles 2012 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	01/13/2012	Extend from WsClassToPlateData. * 							Added VTPEligibleIndi. * 							defect 11224 Ver 6.10.0 * --------------------------------------------------------------------- *//** * WebServices data object for Class To Plate Ver B. * * @version	6.10.0			01/13/2012 * @author  Ray Rowehl * <br>Creation Date:		01/13/2012 14:38:53 *//* &WsClassToPlateDataVerB& */public class WsClassToPlateDataVerB extends WsClassToPlateData{/* &WsClassToPlateDataVerB'ciVTPEligibleIndi& */	private int ciVTPEligibleIndi = 0;		/**	 * Get the VTP Eligible Indicator.	 * 	 * @return the ciVTPEligibleIndi	 *//* &WsClassToPlateDataVerB.getVTPEligibleIndi& */	public int getVTPEligibleIndi()	{		return ciVTPEligibleIndi;	}		/**	 * Set the VTP Eligible Indicator.	 * 	 * @param aiVTPEligibleIndi	 *//* &WsClassToPlateDataVerB.setVTPEligibleIndi& */	public void setVTPEligibleIndi(int aiVTPEligibleIndi)	{		ciVTPEligibleIndi = aiVTPEligibleIndi;	}	}/* #WsClassToPlateDataVerB# */