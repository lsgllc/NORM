package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * PaymentSummaryData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7899 Ver 5.2.3
 * K Harrell	06/30/2005	Java 1.4 Work	
 * 							Moved from server.db
 * 							defect 7899 Ver 5.2.3  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * PaymentSummaryData
 * 
 * @version	5.2.3		06/30/2005
 * @author 	Kathy Harrell
 * <br> Creation Date:	09/19/2001  
 */

public class PaymentSummaryData implements Serializable
{
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciSummaryEffDate;
	protected int ciFeeSourceCd;
	protected int ciPymntTypeCd;
	protected int ciPymntTypeQty;
	protected Dollar caTotalPymntTypeAmt;
	
	private final static long serialVersionUID = 4012326418447465659L;
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
	 * Returns the value of PymntTypeCd
	 * 
	 * @return  int 
	 */
	public final int getPymntTypeCd()
	{
		return ciPymntTypeCd;
	}
	/**
	 * Returns the value of PymntTypeQty
	 * 
	 * @return  int 
	 */
	public final int getPymntTypeQty()
	{
		return ciPymntTypeQty;
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
	 * Returns the value of TotalPymntTypeAmt
	 * 
	 * @return  Dollar 
	 */
	public final Dollar getTotalPymntTypeAmt()
	{
		return caTotalPymntTypeAmt;
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
	 * This method sets the value of PymntTypeCd.
	 * 
	 * @param aiPymntTypeCd  int  
	 */
	public final void setPymntTypeCd(int aiPymntTypeCd)
	{
		ciPymntTypeCd = aiPymntTypeCd;
	}
	/**
	 * This method sets the value of PymntTypeQty.
	 * 
	 * @param aiPymntTypeQty  int  
	 */
	public final void setPymntTypeQty(int aiPymntTypeQty)
	{
		ciPymntTypeQty = aiPymntTypeQty;
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
	 * @param aiSummaryEffDate int   
	 */
	public final void setSummaryEffDate(int aiSummaryEffDate)
	{
		ciSummaryEffDate = aiSummaryEffDate;
	}
	/**
	 * This method sets the value of TotalPymntTypeAmt.
	 * 
	 * @param aaTotalPymntTypeAmt Dollar 
	 */
	public final void setTotalPymntTypeAmt(Dollar aaTotalPymntTypeAmt)
	{
		caTotalPymntTypeAmt = aaTotalPymntTypeAmt;
	}
}
