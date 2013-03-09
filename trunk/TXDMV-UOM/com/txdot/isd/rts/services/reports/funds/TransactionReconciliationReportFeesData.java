package com.txdot.isd.rts.services.reports.funds;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.AcctCdConstant;

/*
 * TransactionReconciliationReportFeesData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	07/26/2009	add isHB3095Placard() 
 * 							defect 10133 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for
 * TransactionReconciliationReportPaymentData
 * 
 * @version	Defect_POS_F	07/26/2009 
 * @author	Kathy Harrell
 * <br>Creation Date:		10/30/2001 23:32:00
 */
public class TransactionReconciliationReportFeesData
{
	protected Dollar caItmPrice;
	protected int ciOfcIssuanceNo;
	protected int ciTransWsId;
	protected int ciTransAMDate;
	protected int ciTransTime;
	protected int ciCustSeqNo;
	protected int ciCashWsId;
	protected String csTransEmpId;
	protected String csAcctItmCd;
	protected String csAcctItmCdDesc;

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
	* Returns the value of AcctItmCd
	* 
	* @return String 
	*/
	public final String getAcctItmCd()
	{
		return csAcctItmCd;
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
	 * Returns the value of CustSeqNo
	 * 
	 * @return int 
	 */
	public final int getCustSeqNo()
	{
		return ciCustSeqNo;
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
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
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
	 * Returns the value of TransAMDate
	 * 
	 * @return int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
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
	 * Returns the value of TransWsId
	 * 
	 * @return int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * This method sets the value of AcctItmCd
	 * 
	 * @param asAcctItmCd String 
	 */
	public final void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
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
	 * This method returns boolean denoting if Fee for Placard in 
	 *  HB3095   (AcctItmCd = ItmCd) 
	 *
	 * @return boolean 
	 */
	public final boolean isHB3095Placard()
	{
		return csAcctItmCd.equals(AcctCdConstant.TDC)
			|| csAcctItmCd.equals(AcctCdConstant.PDC);
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
	 * This method sets the value of CustSeqNo.
	 * 
	 * @param aiCustSeqNo int 
	 */
	public final void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
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
	 * This method sets the value of TransEmpId.
	 * 
	 * @param asTransEmpId String 
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
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
	 * This method sets the value of TransTime.
	 * 
	 * @param aiTransTime int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}
}