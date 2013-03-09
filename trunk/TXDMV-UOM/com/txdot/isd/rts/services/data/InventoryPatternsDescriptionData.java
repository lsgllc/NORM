package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * InventoryPatternsDescriptionData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/13/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * Min Wang     08/01/2005  Compares object with the specified object 
 * 							for order.
 * 							add compareTo()
 * 							implement Comparable
 * 							defect 8269 Ver 5.2.2 Fix 6
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * InventoryPatternsDescriptionData 
 * 
 * @version	5.2.3		08/01/2005 
 * @author	Kathy Harrell
 * <br>Creation Date:	11/15/2001 18:51:55 
 */

public class InventoryPatternsDescriptionData implements Serializable, 
	// defect 8269
	Comparable
	// end defect 8269
{
	// int
	protected int ciInvItmYr;

	// String   
	protected String csItmCd;
	protected String csItmCdDesc;

	private final static long serialVersionUID = -2661341772604808103L;
	/**
	 * Returns the value of InvItmYr
	 * 
	 * @return  int  
	 */
	public final int getInvItmYr()
	{
		return ciInvItmYr;
	}
	/**
	 * Returns the value of comparing item disc
	 * 
	 * @return  int  
	 */
	public final int compareTo(Object aaObj)
	{
		String lsLocalObjKey = getItmCdDesc() + getInvItmYr();
		InventoryPatternsDescriptionData laPassedObj =
			(InventoryPatternsDescriptionData) aaObj;
		String lsPassedObjKey =
			 laPassedObj.getItmCdDesc() + laPassedObj.getInvItmYr();

		return lsLocalObjKey.compareTo(lsPassedObjKey);
	}
	/**
	 * Returns the value of ItmCd
	 * 
	 * @return  String 
	 */
	public final String getItmCd()
	{
		return csItmCd;
	}
	/**
	 * Returns the value of ItmCdDesc
	 * 
	 * @return  String 
	 */
	public final String getItmCdDesc()
	{

		return csItmCdDesc;
	}
	/**
	 * This method sets the value of InvItmYr.
	 * 
	 * @param aiInvItmYr   int  
	 */
	public final void setInvItmYr(int aiInvItmYr)
	{
		ciInvItmYr = aiInvItmYr;
	}
	/**
	 * This method sets the value of ItmCd.
	 * 
	 * @param asItmCd   String 
	 */
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	/**
	 * This method sets the value of ItmCdDesc
	 * 
	 * @param asItmCdDesc String 
	 */
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
}
