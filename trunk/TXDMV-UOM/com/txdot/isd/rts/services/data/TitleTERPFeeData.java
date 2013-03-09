package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.Dollar;

/*
 * TitleTERPData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * TitleTERPData
 *
 * @version	5.2.3		06/19/2005
 * @author	Kathy Harrell 
 * <br>Creation Date: 	07/30/2003 22:03:06  
 */

public class TitleTERPFeeData implements Serializable
{
	// int
	protected int ciRTSEffDate;
	protected int ciRTSEffEndDate;

	// Object 
	protected Dollar ciTtlTERPFlatFee;

	// String 
	protected String csCntyTERPStatusCd;
	protected String csTERPAcctItmCd;

	private final static long serialVersionUID = 5623365780451491282L;

	/**
	 * Return the value of CntyTERPStatusCd
	 * 
	 * @return String
	 */
	public final String getCntyTERPStatusCd()
	{
		return csCntyTERPStatusCd;
	}
	/**
	 * Return the value of RTSEffDate
	 * 
	 * @return int
	 */
	public final int getRTSEffDate()
	{
		return ciRTSEffDate;
	}
	/**
	 * Return the value of RTSEffEndDate
	 * 
	 * @return int
	 */
	public final int getRTSEffEndDate()
	{
		return ciRTSEffEndDate;
	}
	/**
	 * Return the value of TERPAcctItmCd
	 * 
	 * @return String
	 */
	public String getTERPAcctItmCd()
	{
		return csTERPAcctItmCd;
	}
	/**
	 * Return the value of TtlTERPFlatFee
	 * @return Dollar
	 */
	public final Dollar getTtlTERPFlatFee()
	{
		return ciTtlTERPFlatFee;
	}
	/**
	 * Set the value of CntyTERPStatusCd
	 * 
	 * @param asCntyTERPStatusCd String
	 */
	public final void setCntyTERPStatusCd(String asCntyTERPStatusCd)
	{
		csCntyTERPStatusCd = asCntyTERPStatusCd;
	}
	/**
	 * Set the value of RTSEffDate
	 * 
	 * @param aiRTSEffDate int
	 */
	public final void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}
	/**
	 * Set the value of RTSEffEndDate
	 * 
	 * @param aiRTSEffEndDate int
	 */
	public final void setRTSEffEndDate(int aiRTSEffEndDate)
	{
		ciRTSEffEndDate = aiRTSEffEndDate;
	}
	/**
	 * Set the value of TERPAcctItmCd
	 * 
	 * @param asTERPAcctItmCd String
	 */
	public void setTERPAcctItmCd(String asTERPAcctItmCd)
	{
		csTERPAcctItmCd = asTERPAcctItmCd;
	}
	/**
	 * Set the value of TtlTERPFlatFee
	 * 
	 * @param aaTtlTERPFlatFee Dollar
	 */
	public final void setTtlTERPFlatFee(Dollar aaTtlTERPFlatFee)
	{
		ciTtlTERPFlatFee = aaTtlTERPFlatFee;
	}
}
