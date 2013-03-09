package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * SubstationSubscriptionData.java
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
 * This Data class contains attributes and get set methods for 
 * SubstationSubscriptionData
 *
 * @version	5.2.3		06/19/2005
 * @author	Kathy Harrell 
 * <br>Creation Date: 	09/05/2001  13:36:05
 */

public class SubstationSubscriptionData implements Serializable
{
	// int 
	protected int ciOfcIssuanceNo = Integer.MIN_VALUE;
	protected int ciSubstaId = Integer.MIN_VALUE;
	protected int ciTblSubstaId;
	protected int ciTblUpdtIndi;

	// Object 
	protected RTSDate caChngTimestmp;

	// String 
	protected String csTblName;

	private final static long serialVersionUID = -3302143238929738123L;
	/**
	 * Returns the value of ChngTimestmp
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
	 * Returns the value of SubstaId
	 * 
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}
	/**
	 * Returns the value of TblName
	 * 
	 * @return  String 
	 */
	public final String getTblName()
	{
		return csTblName;
	}
	/**
	 * Returns the value of TblSubstaId
	 * 
	 * @return  int 
	 */
	public final int getTblSubstaId()
	{
		return ciTblSubstaId;
	}
	/**
	 * Returns the value of TblUpdtIndi
	 * 
	 * @return  int 
	 */
	public final int getTblUpdtIndi()
	{
		return ciTblUpdtIndi;
	}
	/**
	 * This method sets the value of ChngTimestmp
	 * 
	 * @param aaChngTimestmp   RTSDate 
	 */
	public final void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}
	/**
	 * This method sets the value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * This method sets the value of SubstaId
	 * 
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}
	/**
	 * This method sets the value of TblName
	 * 
	 * @param asTblName   String 
	 */
	public final void setTblName(String asTblName)
	{
		csTblName = asTblName;
	}
	/**
	 * This method sets the value of TblSubstaId
	 * 
	 * @param aiTblSubstaId   int 
	 */
	public final void setTblSubstaId(int aiTblSubstaId)
	{
		ciTblSubstaId = aiTblSubstaId;
	}
	/**
	 * This method sets the value of TblUpdtIndi
	 * 
	 * @param aiTblUpdtIndi   int 
	 */
	public final void setTblUpdtIndi(int aiTblUpdtIndi)
	{
		ciTblUpdtIndi = aiTblUpdtIndi;
	}
}
