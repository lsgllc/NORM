package com.txdot.isd.rts.services.data;

import java.util.Vector;

/*
 *
 * RegistrationItemsData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get and set methods for 
 * RegistrationItemsData 
 * 
 * @version	5.2.3			05/19/2005 
 * @author	Administrator
 * <br>Creation Date:		08/30/2001 17:26:57  
 */

public class RegistrationItemsData implements java.io.Serializable
{
	// Vector
	private Vector cvInvAllocationData;

	private final static long serialVersionUID = 5410973810621043820L;
	/**
	 * RegistrationItemsData constructor comment.
	 */
	public RegistrationItemsData()
	{
		super();
	}
	/**
	 * Return value of InvAllocationData
	 * 
	 * @return Vector
	 */
	public Vector getInvAllocationData()
	{
		return cvInvAllocationData;
	}
	/**
	 * Set value of InvAllocationData
	 * 
	 * @param avInvAllocationData Vector
	 */
	public void setInvAllocationData(Vector avInvAllocationData)
	{
		cvInvAllocationData = avInvAllocationData;
	}
}
