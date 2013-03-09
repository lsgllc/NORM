package com.txdot.isd.rts.services.data;

/*
 * InventoryDetailReportData.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * Ray Rowehl	09/30/2005	Moved to services.data since this is a 
 * 							data class.
 * 							defect 7890 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
 
/**
 * Class holds all data for the Inventory Detail Report
 *
 * @version	5.2.3			09/30/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/20/2001 10:50:18
 */

public class InventoryDetailReportData
{
	protected int ciInvCdDesc;
	protected int ciInvItmYr;
	protected int ciOfcIssuanceNo;
	protected int ciReUseIndi;
	protected int ciSoldIndi;
	protected int ciTransWsId;
	protected int ciTransAMDate;
	protected int ciTransTime;
	protected int ciVoidIndi;
	protected String csInvItmNo;
	protected String csItmCdDesc;

	/**
	 * Returns the value of InvCdDesc
	 * 
	 * @return int 
	 */
	public final int getInvCdDesc()
	{
		return ciInvCdDesc;
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
	 * Returns the value of ItmCdDesc
	 * 
	 * @return String 
	 */
	public final String getItmCdDesc()
	{
		return csItmCdDesc;
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
	 * Returns the value of ReUseIndi
	 * 
	 * @return int 
	 */
	public final int getReUseIndi()
	{
		return ciReUseIndi;
	}

	/**
	 * Returns the value of SoldIndi
	 * 
	 * @return int 
	 */
	public final int getSoldIndi()
	{
		return ciSoldIndi;
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
	 * Returns the value of VoidIndi
	 * 
	 * @return int 
	 */
	public final int getVoidIndi()
	{
		return ciVoidIndi;
	}

	/**
	 * This method sets the value of InvCdDesc.
	 * 
	 * @param aiInvCdDesc int 
	 */
	public final void setInvCdDesc(int aiInvCdDesc)
	{
		ciInvCdDesc = aiInvCdDesc;
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
	 * This method sets the value of ItmCdDesc.
	 * 
	 * @param asItmCdDesc String 
	 */
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
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
	 * This method sets the value of ReUseIndi.
	 * 
	 * @param aiReUseIndi int 
	 */
	public final void setReUseIndi(int aiReUseIndi)
	{
		ciReUseIndi = aiReUseIndi;
	}

	/**
	 * This method sets the value of SoldIndi.
	 * 
	 * @param aiSoldIndi int 
	 */
	public final void setSoldIndi(int aiSoldIndi)
	{
		ciSoldIndi = aiSoldIndi;
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
	 * This method sets the value of VoidIndi.
	 * 
	 * @param aiVoidIndi int 
	 */
	public final void setVoidIndi(int aiVoidIndi)
	{
		ciVoidIndi = aiVoidIndi;
	}
}