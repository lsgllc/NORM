package com.txdot.isd.rts.services.reports.funds;

import java.io.Serializable;

/*
 *
 * InventorySummaryTypeReportData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell  	10/04/2001  Created new object - no longer extends
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add ciReprntQty and get/set 
 * 							methods.
 * 							Ver 5.2.0
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3 				 
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * InventorySummaryReportData
 * 
 * @version	5.2.3			05/31/2005  
 * @author	Kathy Harrell
 * <br>Creation Date:		09/19/2001
 */
public class InventorySummaryTypeReportData implements Serializable
{
	protected int ciCashWsId;
	protected int ciInvItmYr;
	// PCR 34
	private int ciReprntQty;
	// End PCR 34
	protected String csTransEmpId;
	protected String csInvStatus;
	protected String csItmCdDesc;
	protected String csInvQty;

	/**
	 * Returns the value of CashWsId
	 * 
	 * @return int 
	 */
	public final int getCashWsId()
	{
		return ciCashWsId;
	}
	
	/**
	 * Returns the value of InvItmYr
	 * 
	 * @return int 
	 */
	public final int getInvItmYr()
	{
		return ciInvItmYr;
	}
	
	/**
	 * Returns the value of InvQty
	 * 
	 * @return String 
	 */
	public final String getInvQty()
	{
		return csInvQty;
	}
	
	/**
	 * Returns the value of InvStatus
	 * 
	 * @return String 
	 */
	public final String getInvStatus()
	{
		return csInvStatus;
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
	 * This method sets the value of ReprntQty. PCR 34
	 * 
	 * @return int
	 */
	public int getReprntQty()
	{
		return ciReprntQty;
	}
	
	/**
	 * Returns the value of TransEmpId
	 * 
	 * @return String
	 */
	public final String getTransEmpId()
	{
		return csTransEmpId;
	}
	
	/**
	 * This method sets the value of CashWsId.
	 * 
	 * @param aiCashWsId int 
	 */
	public final void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	
	/**
	 * This method sets the value of InvItmYr.
	 * 
	 * @param aiInvItmYr int 
	 */
	public final void setInvItmYr(int aiInvItmYr)
	{
		ciInvItmYr = aiInvItmYr;
	}
	
	/**
	 * This method sets the value of InvQty.
	 * 
	 * @param asInvQty String 
	 */
	public final void setInvQty(String asInvQty)
	{
		csInvQty = asInvQty;
	}
	
	/**
	 * This method sets the value of InvStatus.
	 * 
	 * @param asInvStatus String 
	 */
	public final void setInvStatus(String asInvStatus)
	{
		csInvStatus = asInvStatus;
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
	
	/**
	 * This method sets the value of ReprntQty PCR 34
	 * 
	 * @param aiReprintQty int
	 */
	public void setReprntQty(int aiReprntQty)
	{
		ciReprntQty = aiReprntQty;
	}
	
	/**
	 * This method sets the value of TransEmpId.
	 * 
	 * @param asTransEmpId String 
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}
}
