package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * CashWorkstationIdsData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data 
 *							defect 7899 Ver 5.2.3 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * CashWorkstationIdsData
 *
 * @version	5.2.3		06/19/2005
 * @author	Administrator
 * <br>Creation Date:	09/04/2001 
 */

public class CashWorkstationIdsData implements Serializable
{
	// int
	protected int ciCashWsId = Integer.MIN_VALUE;
	protected int ciCloseOutReqIndi;
	protected int ciCloseOutReqWsId;
	protected int ciDeleteIndi;
	protected int ciOfcIssuanceNo = Integer.MIN_VALUE;
	protected int ciSubstaId = Integer.MIN_VALUE;

	// Object
	protected RTSDate caChngTimestmp;
	protected RTSDate caCurrStatTimestmp;

	private final static long serialVersionUID = 8940091871665983978L;

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
	 * Returns the value of ChngTimestmp
	 * 
	 * @return  RTSDate 
	 */
	public final RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}
	/**
	 * Returns the value of CloseOutReqIndi
	 * 
	 * @return  int 
	 */
	public final int getCloseOutReqIndi()
	{
		return ciCloseOutReqIndi;
	}
	/**
	 * Returns the value of CloseOutReqWsId
	 * 
	 * @return  int 
	 */
	public final int getCloseOutReqWsId()
	{
		return ciCloseOutReqWsId;
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
	 * Returns the value of DeleteIndi
	 * 
	 * @return  int 
	 */
	public final int getDeleteIndi()
	{
		return ciDeleteIndi;
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
	 * This method sets the value of CashWsId.
	 * 
	 * @param aiCashWsId   int 
	 */
	public final void setCashWsId(int aiCashWsId)
	{
		ciCashWsId = aiCashWsId;
	}
	/**
	 * This method sets the value of ChngTimestmp.
	 * @param aaChngTimestmp   RTSDate 
	 */
	public final void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}
	/**
	 * This method sets the value of CloseOutReqIndi.
	 * @param aiCloseOutReqIndi   int 
	 */
	public final void setCloseOutReqIndi(int aiCloseOutReqIndi)
	{
		ciCloseOutReqIndi = aiCloseOutReqIndi;
	}
	/**
	 * This method sets the value of CloseOutReqWsId.
	 * 
	 * @param aiCloseOutReqWsId   int 
	 */
	public final void setCloseOutReqWsId(int aiCloseOutReqWsId)
	{
		ciCloseOutReqWsId = aiCloseOutReqWsId;
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
	 * This method sets the value of DeleteIndi.
	 * 
	 * @param aiDeleteIndi   int 
	 */
	public final void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
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
}
