package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * AddlWtTableData.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	06/20/2005	Modify for move to Java1.4 \ WSAD from VAJ.
 *							modify for Java 1.4
 *							defect 7893 Ver 5.2.3
 * B Hargrove	06/28/2005	Refactor\Move 
 * 							AddlWtTableData class from
 *							com.txdot.isd.rts.client.miscreg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7893 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * AddlWtTableData is a simple data object that contains the data used 
 * by the tables in MRG011.
 *
 * @version	5.2.3			06/8/2005
 * @author	Joseph Kwik
 * <br>Creation Date:		12/06/01 15:22:02
 */

public class AddlWtTableData implements java.io.Serializable
{
	private java.lang.String csPeriod;
	private RTSDate caExpirationDate;
	private Dollar caAmount;
	// defect 7893
	// remove unused variable
	//private final static long clSerialVersionUID = 5245358390885704385L;
	//
	
	/**
	 * Create a RefundTableData.
	 */
	public AddlWtTableData()
	{
		super();
	}
	
	/**
	 * Return the caAmount.
	 * 
	 * @return Dollar
	 */
	public Dollar getAmount()
	{
		return caAmount;
	}
	
	/**
	 * Return the expiration date.
	 * 
	 * @return RTSDate
	 */
	public RTSDate getExpirationDate()
	{
		return caExpirationDate;
	}
	
	/**
	 * Return the csPeriod.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPeriod()
	{
		return csPeriod;
	}
	
	/**
	 * Set the caAmount.
	 *
	 * @param aaNewAmount Dollar the caAmount
	 */
	public void setAmount(Dollar aaNewAmount)
	{
		caAmount = aaNewAmount;
	}
	
	/**
	 * Set the expiration date.
	 * 
	 * @param aaNewDate RTSDate the expiration date
	 */
	public void setExpirationDate(RTSDate aaNewDate)
	{
		caExpirationDate = aaNewDate;
	}
	
	/**
	 * Set the csPeriod.
	 * 
	 * @param aaNewPeriod java.lang.String the csPeriod
	 */
	public void setPeriod(java.lang.String aaNewPeriod)
	{
		csPeriod = aaNewPeriod;
	}
}
