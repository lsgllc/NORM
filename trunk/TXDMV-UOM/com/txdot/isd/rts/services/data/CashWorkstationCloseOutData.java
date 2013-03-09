package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * CashWorkstationCloseOutData.java
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
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * CashWorkstationCloseOutData
 *
 * @version	5.2.3			05/19/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2001 11:37:45 
 */

public class CashWorkstationCloseOutData implements Serializable
{
	//	boolean 
	protected boolean cbNoMonetaryTransactions;
	protected boolean cbPendingTransactions;
	protected boolean cbSelected;

	// int 
	protected int ciCashWsId;
	protected int ciCloseOutReqWsId;
	protected int ciCloseOutReqIndi;
	protected int ciOfcIssuanceNo;
	protected int ciSubstaId;
	protected int ciTransSinceCloseOut;

	// Object
	protected Dollar caBsnDateTotalAmt;
	protected RTSDate caCloseOutBegTstmp;
	protected RTSDate caCloseOutEndTstmp;
	protected RTSDate caCurrentTimestmp;
	protected RTSDate caCurrStatTimestmp;

	// String 
	protected String csRptStatus = "";

	private final static long serialVersionUID = -177325293525431820L;
	/**
	 * Returns the value of BsnDateTotalAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getBsnDateTotalAmt()
	{
		return caBsnDateTotalAmt;
	}
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
	 * Returns the value of CloseOutReqIndi
	 * 
	 * @return int
	 */
	public int getCloseOutReqIndi()
	{
		return ciCloseOutReqIndi;
	}
	/**
	 * Returns the value of CloseOutReqIndi
	 * 
	 * @return  int
	 */
	public final int getCloseOutReqWsId()
	{
		return ciCloseOutReqWsId;
	}
	/**
	 * Returns the value of CurrentTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getCurrentTimestmp()
	{
		return caCurrentTimestmp;
	}
	/**
	 * Returns the value of CurrStatTimestmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getCurrStatTimestmp()
	{
		return caCurrStatTimestmp;
	}
	/**
	  * Returns the value of NoMonetaryTransactions
	  * 
	  * @return  boolean
	  */
	public final boolean getNoMonetaryTransactions()
	{
		return cbNoMonetaryTransactions;
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
	 * Returns the value of PendingTransactions
	 * 
	 * @return  boolean
	 */
	public final boolean getPendingTransactions()
	{
		return cbPendingTransactions;
	}
	/**
	 * Returns the value of RptStatus
	 * 
	 * @return String
	 */
	public String getRptStatus()
	{
		return csRptStatus;
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
	  * Returns the value of TransSinceCloseOut
	  * 
	  * @return  int
	  */
	public final int getTransSinceCloseOut()
	{
		return ciTransSinceCloseOut;
	}
	/**
	 * Returns the value of Cash Drawer is Selected
	 * 
	 * @return  boolean
	 */
	public boolean isSelected()
	{
		return cbSelected;
	}
	/**
	 * Thismethod sets the value of BsnDateTotalAmt
	 * 
	 * @param aaBsnDateTotalAmt Dollar
	 */
	public void setBsnDateTotalAmt(Dollar aaBsnDateTotalAmt)
	{
		caBsnDateTotalAmt = aaBsnDateTotalAmt;
	}
	/**
	 * This method sets the value of CashWsId.
	 * 
	 * @param aCashWsId   int 
	 */
	public final void setCashWsId(int aCashWsId)
	{
		ciCashWsId = aCashWsId;
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
	 * This method sets the value of CloseOutReqIndi
	 * 
	 * @param aiCloseOutReqIndi int
	 */
	public void setCloseOutReqIndi(int aiCloseOutReqIndi)
	{
		ciCloseOutReqIndi = aiCloseOutReqIndi;
	}
	/**
	 * This method sets the value of CloseOutReqWsId
	 * 
	 * @param aiCloseOutReqWsId int
	 */
	public final void setCloseOutReqWsId(int aiCloseOutReqWsId)
	{
		ciCloseOutReqWsId = aiCloseOutReqWsId;
	}
	/**
	 * This method sets the value of CurrentTimestmp
	 * 
	 * @param aaCurrentTimestmp RTSDate
	 */
	public void setCurrentTimestmp(RTSDate aaCurrentTimestmp)
	{
		caCurrentTimestmp = aaCurrentTimestmp;
	}
	/**
	 * This method sets the value of CurrStatTimestmp.
	 * 
	 * @param aaCurrStatTimestmp   RTSDate 
	 */
	public final void setCurrStatTimestmp(RTSDate aaCurrStatTimestmp)
	{
		caCurrStatTimestmp = aaCurrStatTimestmp;
	}
	/**
	 * This method sets the value of NoMonetaryTransactions
	 * 
	 * @param abNoMonetaryTransactions boolean
	 */
	public final void setNoMonetaryTransactions(boolean abNoMonetaryTransactions)
	{
		cbNoMonetaryTransactions = abNoMonetaryTransactions;
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
	 * This method sets the value of PendingTransactions
	 * 
	 * @param abPendingTransactions boolean
	 */
	public final void setPendingTransactions(boolean abPendingTransactions)
	{
		cbPendingTransactions = abPendingTransactions;
	}
	/**
	 * This method sets the value of RptStatus
	 * 
	 * @param asRptStatus String
	 */
	public void setRptStatus(String asRptStatus)
	{
		csRptStatus = asRptStatus;
	}
	/**
	 * This method sets the value of Selected Cash Drawer
	 * 
	 * @param abSelected boolean
	 */

	public void setSelected(boolean abSelected)
	{
		cbSelected = abSelected;
	}
	/**
	 * This method sets the value of SubstaId.
	 * 
	 * @param aSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * This method sets the value of TransSinceCloseOut
	 * 
	 * @param aiTransSinceCloseOut int
	 */
	public final void setTransSinceCloseOut(int aiTransSinceCloseOut)
	{
		ciTransSinceCloseOut = aiTransSinceCloseOut;
	}
}
