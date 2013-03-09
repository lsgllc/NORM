package com.txdot.isd.rts.services.reports.funds;

/*
 * 
 * TransactionReconciliationReportTransactionData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K. Harrell   08/02/2002  CQU100004541 - Added SQL for VoidTransCd
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for
 * TransactionReconciliationReportPaymentData
 * 
 * @version	5.2.3			05/31/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:		10/30/2001 23:44:00
 */
public class TransactionReconciliationReportTransactionData
{
	protected int ciOfcIssuanceNo;
	protected int ciTransAMDate;
	protected int ciTransWsId;
	protected int ciTransTime;
	protected int ciCustSeqNo;
	protected int ciCashWsId;
	protected String csTransEmpId;
	protected String csTransCdDesc;
	protected String csCustName1;
	protected String csTransCd;
	protected String csVoidTransCd;
	
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
	 * Returns the value of CustName1
	 * 
	 * @return String 
	 */
	public final String getCustName1()
	{
		return csCustName1;
	}
	
	/**
	 * Returns the value of CustSeqNo
	 * 
	 * @return int 
	 */
	public final int getCustSeqNo()
	{
		return ciCustSeqNo;
	}
	
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
	 * Returns the value of TransCdDesc
	 * 
	 * @return String 
	 */
	public final String getTransCdDesc()
	{
		return csTransCdDesc;
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
	 * Returns Void Trans Code
	 * 
	 * @return String
	 */
	public final String getVoidTransCd()
	{
		return csVoidTransCd;
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
	 * This method sets the value of CustName1.
	 * 
	 * @param asCustName1 String 
	 */
	public final void setCustName1(String asCustName1)
	{
		csCustName1 = asCustName1;
	}
	
	/**
	 * This method sets the value of CustSeqNo.
	 * 
	 * @param aiCustSeqNo int 
	 */
	public final void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
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
	 * This method sets the value of TransCdDesc.
	 * 
	 * @param asTransCdDesc String 
	 */
	public final void setTransCdDesc(String asTransCdDesc)
	{
		csTransCdDesc = asTransCdDesc;
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
	 * @param aiTransTime int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}
	
	/**
	 * This method sets the value of TransWsId.
	 * 
	 * @param aiTransWsId int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}
	
	/**
	 * Set Void Trans Code
	 * 
	 * @param asNewVoidTransCd String
	 */
	public final void setVoidTransCd(String asNewVoidTransCd)
	{
		csVoidTransCd = asNewVoidTransCd;
	}
}
