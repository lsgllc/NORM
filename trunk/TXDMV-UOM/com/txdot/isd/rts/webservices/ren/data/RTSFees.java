package com.txdot.isd.rts.webservices.ren.data;
/*
 * RTSFees.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	11/19/2010	Initial load.
 * 							Defect 10670 Ver WebSub
 * ---------------------------------------------------------------------
 */

/**
 * Intial Renewal Fees Due for WebSub.
 *
 * @version	WebSub			11/19/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		11/19/2010 07:38:42
 */

public class RTSFees
{
	private String csFeesAcctCdDesc;
	private String csFeesAcctCd;
	private double cdItemAmt;
	
	/**
	 * Get the dollar amount for Fees Due Line Item.
	 * 
	 * @return double
	 */
	public double getItemAmt()
	{
		return cdItemAmt;
	}

	/**
	 * Get the description for Fees Due Line Item.
	 * 
	 * @return String
	 */
	public String getFeesAcctCdDesc()
	{
		return csFeesAcctCdDesc;
	}

	/**
	 * Set the dollar amount for Fees Due Line Item.
	 * 
	 * @param adItemAmt
	 */
	public void setItemAmt(double adItemAmt)
	{
		cdItemAmt = adItemAmt;
	}

	/**
	 * Set the description for Fees Due Line Item.
	 * 
	 * @param asFeesAcctCdDesc
	 */
	public void setFeesAcctCdDesc(String asFeesAcctCdDesc)
	{
		csFeesAcctCdDesc = asFeesAcctCdDesc;
	}

	/**
	 * @return the csFeesAcctCd
	 */
	public String getFeesAcctCd()
	{
		return csFeesAcctCd;
	}

	/**
	 * @param csFeesAcctCd the csFeesAcctCd to set
	 */
	public void setFeesAcctCd(String csFeesAcctCd)
	{
		this.csFeesAcctCd = csFeesAcctCd;
	}

}
