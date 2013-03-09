package com.txdot.isd.rts.services.reports.funds;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * TransactionReconciliationReportData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for
 * TransactionReconciliationReportData
 * 
 * @version	5.2.3			05/31/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 16:10:20 
 */
public class TransactionReconciliationReportData
{
	protected Dollar caPymntTypeAmt;
	protected Dollar caChngDueAmt;
	protected Dollar caItmPrice;
	protected int ciCustSeqNo;
	protected int ciCashWsId;
	protected int ciTransAMDate;
	protected int ciTransTime;
	protected int ciTransWsId;
	protected int ciInvItmYr;
	protected int ciOfcIssuanceNo;
	protected String csRecordType;
	protected String csTransEmpId;
	protected String csTransCdDesc;
	protected String csPymntTypeCdDesc;
	protected String csChngDueTypeCdDesc;
	protected String csCustName1;
	protected String csAcctItmCdDesc;
	protected String csInvItmNo;
	protected String csTransCd;
	protected String csPymntNo;
	
	/**
	 * Returns the value of RecordType
	 * 
	 * @return String 
	 */
	public final String getRecordType()
	{
		return csRecordType;
	}
	
	/**
	 * This method sets the value of RecordType.
	 * 
	 * @param asRecordType String 
	 */
	public final void setRecordType(String asRecordType)
	{
		csRecordType = asRecordType;
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
	 * This method sets the value of CustSeqNo.
	 * 
	 * @param aiCustSeqNo int 
	 */
	public final void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}
	
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
	 * This method sets the value of CashWsId.
	 * 
	 * @param aiCashWsId int 
	 */
	public final void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
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
	 * This method sets the value of TransAMDate.
	 * 
	 * @param aiTransAMDate int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
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
	 * This method sets the value of TransTime.
	 * 
	 * @param aiTransTime int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
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
	 * This method sets the value of TransEmpId.
	 * 
	 * @param asTransEmpId String 
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
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
	 * This method sets the value of TransWsId.
	 * 
	 * @param aiTransWsId int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
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
	 * This method sets the value of TransCdDesc.
	 * 
	 * @param asTransCdDesc String 
	 */
	public final void setTransCdDesc(String asTransCdDesc)
	{
		csTransCdDesc = asTransCdDesc;
	}
	
	/**
	 * Returns the value of PymntTypeCdDesc
	 * 
	 * @return String 
	 */
	public final String getPymntTypeCdDesc()
	{
		return csPymntTypeCdDesc;
	}
	
	/**
	 * This method sets the value of PymntTypeCdDesc.
	 * 
	 * @param asPymntTypeCdDesc String 
	 */
	public final void setPymntTypeCdDesc(String asPymntTypeCdDesc)
	{
		csPymntTypeCdDesc = asPymntTypeCdDesc;
	}
	
	/**
	 * Returns the value of PymntTypeAmt
	 * 
	 * @return Dollar 
	 */
	public final Dollar getPymntTypeAmt()
	{
		return caPymntTypeAmt;
	}
	
	/**
	 * This method sets the value of PymntTypeAmt.
	 * 
	 * @param aaPymntTypeAmt Dollar 
	 */
	public final void setPymntTypeAmt(Dollar aaPymntTypeAmt)
	{
		caPymntTypeAmt = aaPymntTypeAmt;
	}
	
	/**
	 * Returns the value of ChngDueTypeCdDesc
	 * 
	 * @return String 
	 */
	public final String getChngDueTypeCdDesc()
	{
		return csChngDueTypeCdDesc;
	}
	
	/**
	 * This method sets the value of ChngDueTypeCdDesc.
	 * 
	 * @param asChngDueTypeCdDesc String 
	 */
	public final void setChngDueTypeCdDesc(String asChngDueTypeCdDesc)
	{
		csChngDueTypeCdDesc = asChngDueTypeCdDesc;
	}
	
	/**
	 * Returns the value of ChngDueAmt
	 * 
	 * @return Dollar 
	 */
	public final Dollar getChngDueAmt()
	{
		return caChngDueAmt;
	}
	
	/**
	 * This method sets the value of ChngDueAmt.
	 * 
	 * @param aaChngDueAmt Dollar 
	 */
	public final void setChngDueAmt(Dollar aaChngDueAmt)
	{
		caChngDueAmt = aaChngDueAmt;
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
	 * This method sets the value of CustName1.
	 * 
	 * @param asCustName1 String 
	 */
	public final void setCustName1(String asCustName1)
	{
		csCustName1 = asCustName1;
	}
	
	/**
	 * Returns the value of AcctItmCdDesc
	 * 
	 * @return String 
	 */
	public final String getAcctItmCdDesc()
	{
		return csAcctItmCdDesc;
	}
	
	/**
	 * This method sets the value of AcctItmCdDesc.
	 * 
	 * @param asAcctItmCdDesc String 
	 */
	public final void setAcctItmCdDesc(String asAcctItmCdDesc)
	{
		csAcctItmCdDesc = asAcctItmCdDesc;
	}
	
	/**
	 * Returns the value of InvItmNo
	 * 
	 * @return String 
	 */
	public final String getInvItmNo()
	{
		return csInvItmNo;
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
	 * Returns the value of InvItmYr
	 * 
	 * @return int 
	 */
	public final int getInvItmYr()
	{
		return ciInvItmYr;
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
	 * Returns the value of ItmPrice
	 * 
	 * @return Dollar 
	 */
	public final Dollar getItmPrice()
	{
		return caItmPrice;
	}
	
	/**
	 * This method sets the value of ItmPrice.
	 * 
	 * @param aaItmPrice Dollar 
	 */
	public final void setItmPrice(Dollar aaItmPrice)
	{
		caItmPrice = aaItmPrice;
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
	 * This method sets the value of TransCd.
	 * 
	 * @param asTransCd String 
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
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
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	
	/**
	 * Returns the value of PymntNo
	 * 
	 * @return String 
	 */
	public final String getPymntNo()
	{
		return csPymntNo;
	}
	
	/**
	 * This method sets the value of PymntNo.
	 * 
	 * @param asPymntNo String 
	 */
	public final void setPymntNo(String asPymntNo)
	{
		csPymntNo = asPymntNo;
	}
}