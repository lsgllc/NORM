package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * CloseOutHistoryData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell   	10/16/2001  Added variables for CloseOut
 * K Harrell   	10/18/2001  Added CloseOutReqWsId
 * B Tulsiani	10/28/2001  Added RptMsg
 * B Tulsiani  	12/18/2001  Added BsnDateTotalAmt
 * K Harrell	04/22/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * CloseOutHistoryData. 
 *
 * @version	5.2.3			04/22/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/24/2001 16:17:17 
 */

public class CloseOutHistoryData implements Serializable
{
	// int 
	protected int ciCashWsId;
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciSummaryEffDate;

	// Object 
	protected RTSDate caCloseOutBegTstmp;
	protected RTSDate caCloseOutEndTstmp;

	private final static long serialVersionUID = -7684319976305135379L;
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
	 * Returns the value of CloseOutBegTstmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getCloseOutBegTstmp()
	{
		return caCloseOutBegTstmp;
	}
	/**
	 * Returns the value of CloseOutEndTstmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getCloseOutEndTstmp()
	{
		return caCloseOutEndTstmp;
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
	 * This method sets the value of CashWsId.
	 * 
	 * @param aiCashWsId   int 
	 */
	public final void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	/**
	 * This method sets the value of CloseOutBegTstmp.
	 * 
	 * @param aaCloseOutBegTstmp   RTSDate 
	 */
	public final void setCloseOutBegTstmp(RTSDate aaCloseOutBegTstmp)
	{
		caCloseOutBegTstmp = aaCloseOutBegTstmp;
	}
	/**
	 * This method sets the value of CloseOutEndTstmp.
	 * 
	 * @param aaCloseOutEndTstmp   RTSDate 
	 */
	public final void setCloseOutEndTstmp(RTSDate aaCloseOutEndTstmp)
	{
		caCloseOutEndTstmp = aaCloseOutEndTstmp;
	}
	/**
	 * This method sets the value of OfcIssuanceNo.
	 * 
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of SubstaId.
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * This method sets the value of SummaryEffDate.
	 * 
	 * @param aiSummaryEffDate   int 
	 */
	public final void setSummaryEffDate(int aiSummaryEffDate)
	{
		ciSummaryEffDate = aiSummaryEffDate;
	}
}
