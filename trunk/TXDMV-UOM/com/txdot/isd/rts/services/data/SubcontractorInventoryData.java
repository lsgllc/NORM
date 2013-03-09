package com.txdot.isd.rts.services.data;

import java.util.Vector;


/*
 * SubcontractorInventoryData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3			  
 * ---------------------------------------------------------------------
 */
/**
 * Subcontractor Inventory Data
 * 
 * @version 5.2.3		06/19/2005 
 * @author 	Nancy Ting
 * <br>Creation Date:	10/18/2001 10:50:35
 */

public class SubcontractorInventoryData implements java.io.Serializable
{
	// int 
	private int ciIssueDate;

	// Object 
	private SubcontractorData caSubconInfo;

	// Vector
	private Vector cvInvItems;

	private final static long serialVersionUID = -7664253718567581838L;
	/**
	 * SubcontractorInventoryData constructor comment.
	 */
	public SubcontractorInventoryData()
	{
		super();
	}
	/**
	 * Return value of inventory items.
	 * 
	 * @return Vector
	 */
	public Vector getInvItems()
	{
		return cvInvItems;
	}
	/**
	 * Return value of issue date.
	 * 
	 * @return int
	 */
	public int getIssueDate()
	{
		return ciIssueDate;
	}
	/**
	 * Return value of SubconInfo 
	 * 
	 * @return SubcontractorData
	 */
	public SubcontractorData getSubconInfo()
	{
		return caSubconInfo;
	}
	/**
	 * Set value of inventory items
	 * 
	 * @param avInvItems Vector
	 */
	public void setInvItems(Vector avInvItems)
	{
		cvInvItems = avInvItems;
	}
	/**
	 * Set value of issue date
	 * 
	 * @param aiIssueDate int
	 */
	public void setIssueDate(int aiIssueDate)
	{
		ciIssueDate = aiIssueDate;
	}
	/**
	 * Set value of subcon info
	 * 
	 * @param aaSubconInfo SubcontractorData
	 */
	public void setSubconInfo(SubcontractorData aaSubconInfo)
	{
		caSubconInfo = aaSubconInfo;
	}
}
