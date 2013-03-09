package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * VehicleRegFeesData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/05/2005	Java 1.4 Work
 * 							Moved to services.data	
 * 							defect 7889 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * VehicleRegFeesData
 *
 * @version	5.2.3		10/05/2005
 * @author	Administrator 
 * <br>Creation Date: 	10/04/2001 16:06:17 
 */
public class VehicleRegFeesData implements Serializable
{
	private String csAcctItemDesc;
	private float cfItemPrice;
	private String csAcctItemCd;

	private final static long serialVersionUID = -8571902640206390547L;

	/**
	 * VehicleRegFeesData constructor comment.
	 */
	public VehicleRegFeesData()
	{
		super();
		csAcctItemDesc = null;
		cfItemPrice = 0;
		csAcctItemCd = null;
	}
	/**
	 * Return value of AcctItemCd
	 * 
	 * @return int
	 */
	public String getAcctItemCd()
	{
		return csAcctItemCd;
	}
	/**
	 * Return value of AcctItemDesc
	 * 
	 * @return String
	 */
	public String getAcctItemDesc()
	{
		return csAcctItemDesc;
	}
	/**
	 * Return value of ItemPrice
	 * 
	 * @return float
	 */
	public float getItemPrice()
	{
		return cfItemPrice;
	}
	/**
	 * Set value of AcctItemCd
	 * 
	 * @param asAcctItemCd int
	 */
	public void setAcctItemCd(String asAcctItemCd)
	{
		csAcctItemCd = asAcctItemCd;
	}
	/**
	 * Set value of AcctItemDesc
	 * 
	 * @param asAcctItemDesc String
	 */
	public void setAcctItemDesc(String asAcctItemDesc)
	{
		csAcctItemDesc = asAcctItemDesc;
	}
	/**
	 * Return value of ItemPrice
	 * 
	 * @param afItemPrice float
	 */
	public void setItemPrice(float afItemPrice)
	{
		cfItemPrice = afItemPrice;
	}
}
