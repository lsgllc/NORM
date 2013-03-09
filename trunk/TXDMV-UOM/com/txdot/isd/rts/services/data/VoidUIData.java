package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * VoidUIData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This contains the data pertaining to the Void User Interface Data
 *
 * @version	5.2.3			04/21/2005
 * @author	Administrator
 * <br>Creation Date:		10/29/2001 14:58:15
 **/

public class VoidUIData implements Serializable
{
	// String 
	private String csTargetEventTransId;
	private String csTargetInventoryTransId;
	private String csTransId;
	private String csVoidDescription;

	private final static long serialVersionUID = 397443400592118901L;
	/**
	 * VoidUIData constructor comment.
	 */
	public VoidUIData()
	{
		super();
	}
	/**
	 * Get the value of TargetEventTransId
	 * 
	 * @return String
	 */
	public String getTargetEventTransId()
	{
		return csTargetEventTransId;
	}
	/**
	 * Get the value of TargetInventoryTransId
	 * 
	 * @return String
	 */
	public String getTargetInventoryTransId()
	{
		return csTargetInventoryTransId;
	}
	/**
	 * Get the value of TransId
	 * 
	 * @return String
	 */
	public String getTransId()
	{
		return csTransId;
	}
	/**
	 * Get the value of VoidDescription 
	 * 
	 * @return String
	 */
	public String getVoidDescription()
	{
		return csVoidDescription;
	}
	/**
	 * Get the value of TargetEventTransId
	 *
	 * @param asTargetEventTransId String
	 */
	public void setTargetEventTransId(String asTargetEventTransId)
	{
		csTargetEventTransId = asTargetEventTransId;
	}
	/**
	 * Get the value of TargetInventoryTransId
	 * 
	 * @param asTargetInventoryTransId String
	 */
	public void setTargetInventoryTransId(String asTargetInventoryTransId)
	{
		csTargetInventoryTransId = asTargetInventoryTransId;
	}
	/**
	 * Get the value of TransId
	 * 
	 * @param asTransid String
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}
	/**
	 * Get the value of VoidDescription 
	 * 
	 * @param asVoidDescription String
	 */
	public void setVoidDescription(String asVoidDescription)
	{
		csVoidDescription = asVoidDescription;
	}
}
