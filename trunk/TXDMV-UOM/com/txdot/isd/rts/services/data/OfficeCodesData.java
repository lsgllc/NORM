package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * OfficeCodesData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		09/06/2001	Convert to Hungarian notation.
 * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 			 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * OfficeCodesData
 * 
 * @version	5.2.3		06/19/2005 
 * @author	Administrator
 * <br>Creation Date: 	09/04/2001 
 */

public class OfficeCodesData implements Serializable
{
	// int
	protected int ciOfcIssuanceCd;

	// String 
	protected String csOfcIssuanceCdDesc;

	private final static long serialVersionUID = -5355248040037296528L;

	/**
	 * Returns the value of OfcIssuanceCd
	 * 
	 * @return  int  
	 */
	public final int getOfcIssuanceCd()
	{
		return ciOfcIssuanceCd;
	}
	/**
	 * Returns the value of OfcIssuanceCdDesc
	 * 
	 * @return  String 
	 */
	public final String getOfcIssuanceCdDesc()
	{
		return csOfcIssuanceCdDesc;
	}
	/**
	 * This method sets the value of OfcIssuanceCd.
	 * 
	 * @param aiOfcIssuanceCd   int  
	 */
	public final void setOfcIssuanceCd(int aiOfcIssuanceCd)
	{
		ciOfcIssuanceCd = aiOfcIssuanceCd;
	}
	/**
	 * This method sets the value of OfcIssuanceCdDesc.
	 * 
	 * @param asOfcIssuanceCdDesc   String 
	 */
	public final void setOfcIssuanceCdDesc(String asOfcIssuanceCdDesc)
	{
		csOfcIssuanceCdDesc = asOfcIssuanceCdDesc;
	}
}
