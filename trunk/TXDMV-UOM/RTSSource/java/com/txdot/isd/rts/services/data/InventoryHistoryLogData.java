package com.txdot.isd.rts.services.data;import java.io.Serializable;import com.txdot.isd.rts.services.util.RTSDate;/* * * InventoryHistoryLogData.java  * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	05/19/2005	Java 1.4 Work *							defect 7899 Ver 5.2.3  * --------------------------------------------------------------------- *//** * This Data class contains attributes and get set methods for  * InventoryHistoryLogData *  * @version	5.2.3		05/19/2005  * @author	Administrator * <br>Creation Date: 	09/17/2001   *//* &InventoryHistoryLogData& */public class InventoryHistoryLogData implements Serializable{	// int/* &InventoryHistoryLogData'ciOfcIssuanceNo& */	protected int ciOfcIssuanceNo;	// Object/* &InventoryHistoryLogData'caLastInsertDate& */	protected RTSDate caLastInsertDate;	// String /* &InventoryHistoryLogData'csOfcName& */	protected String csOfcName;/* &InventoryHistoryLogData'serialVersionUID& */	private final static long serialVersionUID = 8500329921796851593L;	/**	 * Returns the value of LastInsertDate	 * 	 * @return  RTSDate 	 *//* &InventoryHistoryLogData.getLastInsertDate& */	public final RTSDate getLastInsertDate()	{		return caLastInsertDate;	}	/**	 * Returns the value of OfcIssuanceNo	 * 	 * @return  int 	 *//* &InventoryHistoryLogData.getOfcIssuanceNo& */	public final int getOfcIssuanceNo()	{		return ciOfcIssuanceNo;	}	/**	 * Return the value of OfcName	 * 	 * @return String	 *//* &InventoryHistoryLogData.getOfcName& */	public String getOfcName()	{		return csOfcName;	}	/**	 * This method sets the value of LastInsertDate.	 * 	 * @param aaLastInsertDate   RTSDate 	 *//* &InventoryHistoryLogData.setLastInsertDate& */	public final void setLastInsertDate(RTSDate aaLastInsertDate)	{		caLastInsertDate = aaLastInsertDate;	}	/**	 * This method sets the value of OfcIssuanceNo.	 * 	 * @param aiOfcIssuanceNo   int 	 *//* &InventoryHistoryLogData.setOfcIssuanceNo& */	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)	{		ciOfcIssuanceNo = aiOfcIssuanceNo;	}	/**	 * Sets the value of OfcName	 * 	 * @param asOfcName String	 *//* &InventoryHistoryLogData.setOfcName& */	public void setOfcName(String asOfcName)	{		csOfcName = asOfcName;	}}/* #InventoryHistoryLogData# */