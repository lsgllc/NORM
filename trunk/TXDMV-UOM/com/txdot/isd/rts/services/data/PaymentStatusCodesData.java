package com.txdot.isd.rts.services.data;

/*
 *
 * PaymentStatusCodesData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
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
 * This Data class contains attributes and get/set methods for 
 * PaymentStatusCodesData
 * 
 * @version	5.2.3		06/19/2005 
 * @author	Administrator
 * <br>Creation Date: 	08/30/2001 
 */

import java.io.Serializable;

public class PaymentStatusCodesData implements Serializable
{
	// String 
	protected String csPymntStatusCd;
	protected String csPymntStatusDesc;

	private final static long serialVersionUID = -433681369289772955L;
	
	/**
	 * Returns the value of PymntStatusCd
	 * 
	 * @return  String 
	 */
	public final String getPymntStatusCd()
	{
		return csPymntStatusCd;
	}
	/**
	 * Returns the value of PymntStatusDesc
	 * 
	 * @return  String 
	 */
	public final String getPymntStatusDesc()
	{
		return csPymntStatusDesc;
	}
	/**
	 * This method sets the value of PymntStatusCd.
	 * 
	 * @param asPymntStatusCd   String 
	 */
	public final void setPymntStatusCd(String asPymntStatusCd)
	{
		csPymntStatusCd = asPymntStatusCd;
	}
	/**
	 * This method sets the value of PymntStatusDesc.
	 * 
	 * @param asPymntStatusDesc   String 
	 */
	public final void setPymntStatusDesc(String asPymntStatusDesc)
	{
		csPymntStatusDesc = asPymntStatusDesc;
	}
}
