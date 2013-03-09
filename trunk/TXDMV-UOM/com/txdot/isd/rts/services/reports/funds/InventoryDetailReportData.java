package com.txdot.isd.rts.services.reports.funds;

import java.io.Serializable;

/*
 * InventoryDetailReportData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/23/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add ciReprntQty and get/set 
 * 							methods. 
 * 							Ver 5.2.0	
 * S Johnston	05/10/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3 	 
 * ---------------------------------------------------------------------
 */
/**
* This Data class contains attributes and get set methods for 
* InventoryDetailReportData
* 
* @version	5.2.3			05/10/2005
* @author	Kathy Harrell
* <br>Creation Date:		09/19/2001
*/
public class InventoryDetailReportData implements Serializable
{
	protected int ciCashWsId;
	protected int ciInvItmYr;
	protected int ciInvQty;
	protected int ciOfcIssuanceNo;
	protected int ciTransAMDate;
	protected int ciTransWsId;
	protected int ciTransTime;
	protected int ciVoidedTransIndi;
	protected String csTransEmpId;
	protected String csTransCd;
	protected String csInvLocIdCd;
	protected String csInvId;
	protected String csItmCd;
	protected String csItmCdDesc;
	protected String csInvItmNo;
	protected String csInvEndNo;
	protected String csDelInvReasn;

	// PCR 34
	protected int ciReprntQty;
	/**
	 * Returns the value of CashWsId
	 * 
	 * @return  int 
	 */
	public final int getCashWsId()
	{
		return ciCashWsId;
	}

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
	 * Returns the value of InvEndNo
	 * 
	 * @return String 
	 */
	public final String getInvEndNo()
	{
		return csInvEndNo;
	}

	/**
	 * Returns the value of InvId
	 * 
	 * @return  String 
	 */
	public final String getInvId()
	{
		return csInvId;
	}

	/**
	 * Returns the value of InvItmNo
	 * 
	 * @return  String 
	 */
	public final String getInvItmNo()
	{
		return csInvItmNo;
	}

	/**
	 * Returns the value of InvItmYr
	 * 
	 * @return  int 
	 */
	public final int getInvItmYr()
	{
		return ciInvItmYr;
	}

	/**
	 * Returns the value of InvLocIdCd
	 * 
	 * @return  String 
	 */
	public final String getInvLocIdCd()
	{
		return csInvLocIdCd;
	}

	/**
	 * Returns the value of InvQty
	 * 
	 * @return  int 
	 */
	public final int getInvQty()
	{
		return ciInvQty;
	}

	/**
	 * Returns the value of ItmCd
	 * 
	 * @return  String 
	 */
	public final String getItmCd()
	{
		return csItmCd;
	}

	/**
	 * Returns the value of ItmCdDesc
	 * 
	 * @return  String 
	 */
	public final String getItmCdDesc()
	{
		return csItmCdDesc;
	}

	// End PCR 34
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * PCR 34
	 * 
	 * @return int
	 */
	public int getReprntQty()
	{
		return ciReprntQty;
	}

	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Returns the value of TransCd
	 * 
	 * @return String 
	 */
	public final String getTransCd()
	{
		return csTransCd;
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
	 * Returns the value of TransTime
	 * 
	 * @return int 
	 */
	public final int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Returns the value of TransWsId
	 * 
	 * @return int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Returns the value of VoidedTransIndi
	 * 
	 * @return int 
	 */
	public final int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
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
	 * This method sets the value of DelInvReasn.
	 * 
	 * @param asDelInvReasn String 
	 */
	public final void setDelInvReasn(String asDelInvReasn)
	{
		csDelInvReasn = asDelInvReasn;
	}

	/**
	 * This method sets the value of InvEndNo.
	 * 
	 * @param asInvEndNo String 
	 */
	public final void setInvEndNo(String asInvEndNo)
	{
		csInvEndNo = asInvEndNo;
	}

	/**
	 * This method sets the value of InvId.
	 * 
	 * @param asInvId String 
	 */
	public final void setInvId(String asInvId)
	{
		csInvId = asInvId;
	}

	/**
	 * This method sets the value of InvItmNo.
	 * 
	 * @param asInvItmNo String 
	 */
	public final void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
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
	 * This method sets the value of InvLocIdCd.
	 * 
	 * @param asInvLocIdCd String 
	 */
	public final void setInvLocIdCd(String asInvLocIdCd)
	{
		csInvLocIdCd = asInvLocIdCd;
	}

	/**
	 * This method sets the value of InvQty.
	 * 
	 * @param aiInvQty int 
	 */
	public final void setInvQty(int aiInvQty)
	{
		ciInvQty = aiInvQty;
	}

	/**
	 * This method sets the value of ItmCd.
	 * 
	 * @param asItmCd String 
	 */
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
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
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * PCR 34
	 * 
	 * @param aiNewReprintQty int
	 */
	public void setReprntQty(int aiNewReprntQty)
	{
		ciReprntQty = aiNewReprntQty;
	}

	/**
	 * This method sets the value of TransAMDate.
	 * 
	 * @param aiTransAMDate int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * This method sets the value of TransCd.
	 * 
	 * @param asTransCd String 
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
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

	/**
	 * This method sets the value of TransTime.
	 * 
	 * @param aTransTime int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * This method sets the value of TransWsId.
	 * 
	 * @param aaTransWsId int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * This method sets the value of VoidedTransIndi.
	 * 
	 * @param aaVoidedTransIndi int 
	 */
	public final void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}
}
