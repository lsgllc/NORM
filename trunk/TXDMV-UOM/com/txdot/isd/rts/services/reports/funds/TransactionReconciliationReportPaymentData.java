package com.txdot.isd.rts.services.reports.funds;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * TransactionReconciliationReportPaymentData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
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
 * <br>Creation Date:		10/30/2001 23:32:00
 */
public class TransactionReconciliationReportPaymentData
{
	protected Dollar cdPymntTypeAmt;
    protected int ciOfcIssuanceNo;
    protected int ciTransAMDate;
    protected int ciCustSeqNo;
    protected int ciCashWsId;
    protected int ciTransWsId;
	protected String csTransEmpId;
    protected String csRecordType;
    protected String csPymntNo;
    protected String csPymntTypeCdDesc;

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
     * Returns the value of OfcIssuanceNo
     * 
     * @return int 
     */
    public final int getOfcIssuanceNo()
    {
        return ciOfcIssuanceNo;
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
     * Returns the value of PymntTypeAmt
     * 
     * @return Dollar 
     */
    public final Dollar getPymntTypeAmt()
    {
        return cdPymntTypeAmt;
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
	 * Returns the value of RecordType
	 * 
	 * @return String 
	 */
	public final String getRecordType()
	{
		return csRecordType;
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
     * This method sets the value of OfcIssuanceNo.
     * 
     * @param aiOfcIssuanceNo int 
     */
    public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
    {
        ciOfcIssuanceNo = aiOfcIssuanceNo;
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
    
    /**
     * This method sets the value of PymntTypeAmt.
     * 
     * @param aaPymntTypeAmt Dollar 
     */
    public final void setPymntTypeAmt(Dollar aaPymntTypeAmt)
    {
        cdPymntTypeAmt = aaPymntTypeAmt;
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
     * This method sets the value of RecordType
     * 
     * @param asRecordType String 
     */
    public final void setRecordType(String asRecordType)
    {
        csRecordType = asRecordType;
    }
}