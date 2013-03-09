package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * FeeSummaryData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/02/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work	
 * 							Moved from server.db
 * 							defect 7899 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for
 * FeeSummaryData
 *
 * @version	5.2.3		06/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date:	09/17/2001 
 */
public class FeeSummaryData implements Serializable
{
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciSummaryEffDate;
	protected int ciFeeSourceCd;
	protected String csAcctItmCd;
	protected int ciCashDrawerIndi;
	protected int ciTotalAcctItmQty;
	protected Dollar caTotalAcctItmAmt;
	
	private final static long serialVersionUID = 6120002346729880501L;
	/**
	 * Returns the value of AcctItmCd
	 *
	 * @return  String 
	 */
	public final String getAcctItmCd()
	{
		return csAcctItmCd;
	}
	/**
	 * Returns the value of CashDrawerIndi
	 *
	 * @return  int 
	 */
	public final int getCashDrawerIndi()
	{
		return ciCashDrawerIndi;
	}
	/**
	 * Returns the value of FeeSourceCd
	 *
	 * @return  int 
	 */
	public final int getFeeSourceCd()
	{
		return ciFeeSourceCd;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 *
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of SubstaId
	 *
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Returns the value of SummaryEffDate
	 *
	 * @return  int 
	 */
	public final int getSummaryEffDate()
	{
		return ciSummaryEffDate;
	}
	/**
	 * Returns the value of TotalAcctItmAmt
	 *
	 * @return  Dollar 
	 */
	public final Dollar getTotalAcctItmAmt()
	{
		return caTotalAcctItmAmt;
	}
	/**
	 * Returns the value of TotalAcctItmQty
	 *
	 * @return  int 
	 */
	public final int getTotalAcctItmQty()
	{
		return ciTotalAcctItmQty;
	}
	/**
	 * This method sets the value of AcctItmCd.
	 *
	 * @param asAcctItmCd  String  
	 */
	public final void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}
	/**
	 * This method sets the value of CashDrawerIndi.
	 *
	 * @param aiCashDrawerIndi  int 
	 */
	public final void setCashDrawerIndi(int aiCashDrawerIndi)
	{
		ciCashDrawerIndi = aiCashDrawerIndi;
	}
	/**
	 * This method sets the value of FeeSourceCd.
	 *
	 * @param aiFeeSourceCd  int  
	 */
	public final void setFeeSourceCd(int aiFeeSourceCd)
	{
		ciFeeSourceCd = aiFeeSourceCd;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 *
	 * @param aiOfcIssuanceNo  int   
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of SubstaId.
	 *
	 * @param aiSubstaId  int  
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * This method sets the value of SummaryEffDate.
	 *
	 * @param aiSummaryEffDate  int 
	 */
	public final void setSummaryEffDate(int aiSummaryEffDate)
	{
		ciSummaryEffDate = aiSummaryEffDate;
	}
	/**
	 * This method sets the value of TotalAcctItmAmt.
	 * 
	 * @param adTotalAcctItmAmt  Dollar 
	 */
	public final void setTotalAcctItmAmt(Dollar adTotalAcctItmAmt)
	{
		caTotalAcctItmAmt = adTotalAcctItmAmt;
	}
	/**
	 * This method sets the value of TotalAcctItmQty.
	 * 
	 * @param aiTotalAcctItmQty  int  
	 */
	public final void setTotalAcctItmQty(int aiTotalAcctItmQty)
	{
		ciTotalAcctItmQty = aiTotalAcctItmQty;
	}
}
