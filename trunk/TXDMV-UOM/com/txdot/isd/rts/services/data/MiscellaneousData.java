package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * MiscellaneousData.java
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
 * MiscellaneousData
 * 
 * @version	5.2.3		06/19/2005 
 * @author	Administrator
 * <br>Creation Date: 	09/04/2001 
 */

public class MiscellaneousData implements Serializable
{
	// int 
	protected int ciOfcIssuanceNo = Integer.MIN_VALUE;
	protected int ciServerPlusIndi;
	protected int ciSubstaId = Integer.MIN_VALUE;

	//	Object 
	protected RTSDate caChngTimestmp;

	// String 
	protected String csSupvOvrideCd;

	private final static long serialVersionUID = 4075721422687405014L;
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
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Returns the value of ServerPlusIndi
	 * 
	 * @return  int 
	 */
	public final int getServerPlusIndi()
	{
		return ciServerPlusIndi;
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
	 * Returns the value of SupvOvrideCd
	 * 
	 * @return  String 
	 */
	public final String getSupvOvrideCd()
	{
		return csSupvOvrideCd;
	}
	/**
	 * This method sets the value of ChngTimestmp.
	 * 
	 * 
	 * @param aaChngTimestmp   RTSDate 
	 */
	public final void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
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
	 * This method sets the value of ServerPlusIndi.
	 * 
	 * @param aiServerPlusIndi   int 
	 */
	public final void setServerPlusIndi(int aiServerPlusIndi)
	{
		ciServerPlusIndi = aiServerPlusIndi;
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
	/**
	 * This method sets the value of SupvOvrideCd.
	 * 
	 * @param asSupvOvrideCd   String 
	 */
	public final void setSupvOvrideCd(String asSupvOvrideCd)
	{
		csSupvOvrideCd = asSupvOvrideCd;
	}
}
