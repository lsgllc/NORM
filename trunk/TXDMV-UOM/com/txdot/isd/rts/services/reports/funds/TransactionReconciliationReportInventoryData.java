package com.txdot.isd.rts.services.reports.funds;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * TransactionReconciliationReportInventoryData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * ---------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for
 * TransactionReconciliationReportData
 * 
 * @version	5.2.3			05/31/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:		10/30/2001 12:11:00 
 */
public class TransactionReconciliationReportInventoryData
{
	protected Dollar caItmPrice;
    protected int ciCustSeqNo;
    protected int ciCashWsId;
	protected int ciInvItmYr;
	protected int ciOfcIssuanceNo;
    protected int ciTransAMDate;
    protected int ciTransTime;
	protected int ciTransWsId;
    protected String csTransEmpId;
    protected String csItmCd;
    protected String csItmCdDesc;
    protected String csInvItmNo;
    
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
     * Returns the value of InvItmNo
     * 
     * @return String 
     */
    public final String getInvItmNo()
    {
        return csInvItmNo;
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
     * Returns the value of ItmCd
     * 
     * @return String 
     */
    public final String getItmCd()
    {
        return csItmCd;
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
     * This method sets the value of ItmCdDesc.
     * 
     * @param asItmCdDesc String 
     */
    public final void setAcctItmCdDesc(String asItmCdDesc)
    {
        csItmCdDesc = asItmCdDesc;
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
     * This method sets the value of ItmCd
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
}
