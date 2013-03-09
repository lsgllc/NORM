package com.txdot.isd.rts.server.webapps.order.common.data;

/*
 * SpecialPlateDesign.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		05/11/2007	Created class.
 * 							defect 9120 Ver Special Plates 
 * ---------------------------------------------------------------------
 */

/**
 * Object used to hold the special plate fees.
 * 
 * Fee could be the special plate fee or the personalization fee.
 *
 * @version	Special Plates	05/11/2007
 * @author	Jeff Seifert
 * <br>Creation Date:		05/11/2007 11:30:00
 */
public class Fees
{
	private String acctItmCd;
	private String desc;
	private double itemPrice;
	private int itmQty;
	
	/**
	 * The Account Item Code for the given fee.
	 * 
	 * @return String
	 */
	public String getAcctItmCd()
	{
		return acctItmCd;
	}

	/**
	 * The Description of the fee.
	 * 
	 * @return String
	 */
	public String getDesc()
	{
		return desc;
	}

	/**
	 * The Item price of the fee.
	 * 
	 * @return double
	 */
	public double getItemPrice()
	{
		return itemPrice;
	}

	/**
	 * The quantity of this fee.
	 * 
	 * @return int
	 */
	public int getItmQty()
	{
		return itmQty;
	}

	/**
	 * Sets the Account Item Code for the given fee.
	 * 
	 * @param asAcctItmCd String
	 */
	public void setAcctItmCd(String asAcctItmCd)
	{
		acctItmCd = asAcctItmCd;
	}

	/**
	 * Sets the Description of the fee.
	 * 
	 * @param asDesc String
	 */
	public void setDesc(String asDesc)
	{
		desc = asDesc;
	}

	/**
	 * Sets the Item price of the fee.
	 * 
	 * @param adItemPrice double
	 */
	public void setItemPrice(double adItemPrice)
	{
		itemPrice = adItemPrice;
	}

	/**
	 * Sets the quantity of this fee.
	 * 
	 * @param aiItmQty int
	 */
	public void setItmQty(int aiItmQty)
	{
		itmQty = aiItmQty;
	}

}
