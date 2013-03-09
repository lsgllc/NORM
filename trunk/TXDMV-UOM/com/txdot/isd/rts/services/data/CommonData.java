package com.txdot.isd.rts.services.data;

/**
 *
 * CommonData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/18/2005	Move to services.data
 * 							defect 7705 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * CommonData object used in the Common Module
 *
 * @version	5.2.3			02/18/2005
 * @author	Joseph Peters
 * <br>Creation Date:		08/30/2001 15:01:56
 * @deprecated
 */

public class CommonData implements java.io.Serializable
{
	private java.lang.String csPlateItemCodeDescription;
	private java.lang.String csCurrentPltNo;
	private int ciExpMo;
	private java.lang.String csRegClass;
	private float cfFee;
	private com.txdot.isd.rts.services.util.RTSDate cdIssueDate;
	private java.lang.String csPltNo;
	private java.lang.String csStkrNo;
	private final static long serialVersionUID = -8467826425118852713L;
	/**
	 * CommonData constructor comment.
	 */
	public CommonData()
	{
		super();
	}
	/**
	 * Get current plate number
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCurrentPltNo()
	{
		return csCurrentPltNo;
	}
	/**
	 * Get expiration month.
	 * 
	 * @return int
	 */
	public int getExpMo()
	{
		return ciExpMo;
	}
	/**
	 * Get fee
	 * 
	 * @return float
	 */
	public float getFee()
	{
		return cfFee;
	}
	/**
	 * Get issue date
	 * 
	 * @return com.txdot.isd.rts.services.util.RTSDate
	 */
	public com.txdot.isd.rts.services.util.RTSDate getIssueDate()
	{
		return cdIssueDate;
	}
	/**
	 * Get plate item code description
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPlateItemCodeDescription()
	{
		return csPlateItemCodeDescription;
	}
	/**
	 * Get plate number
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPltNo()
	{
		return csPltNo;
	}
	/**
	 * Get Reg class
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRegClass()
	{
		return csRegClass;
	}
	/**
	 * Get Sticker number
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getStkrNo()
	{
		return csStkrNo;
	}
	/**
	 * Set current plate number
	 * 
	 * @param newCurrentPltNo java.lang.String
	 */
	public void setCurrentPltNo(java.lang.String newCurrentPltNo)
	{
		csCurrentPltNo = newCurrentPltNo;
	}
	/**
	 * Set expiration month
	 * 
	 * @param newExpMo int
	 */
	public void setExpMo(int newExpMo)
	{
		ciExpMo = newExpMo;
	}
	/**
	 * Set fee
	 * 
	 * @param newFee float
	 */
	public void setFee(float newFee)
	{
		cfFee = newFee;
	}
	/**
	 * Set issue date
	 * 
	 * @param newIssueDate com.txdot.isd.rts.services.util.RTSDate
	 */
	public void setIssueDate(
		com.txdot.isd.rts.services.util.RTSDate newIssueDate)
	{
		cdIssueDate = newIssueDate;
	}
	/**
	 * Set plate item code description
	 * 
	 * @param newPlateItemCodeDescription java.lang.String
	 */
	public void setPlateItemCodeDescription(
		java.lang.String newPlateItemCodeDescription)
	{
		csPlateItemCodeDescription = newPlateItemCodeDescription;
	}
	/**
	 * Set Plate number
	 * 
	 * @param newPltNo java.lang.String
	 */
	public void setPltNo(java.lang.String newPltNo)
	{
		csPltNo = newPltNo;
	}
	/**
	 * Set Reg class
	 * 
	 * @param newRegClass java.lang.String
	 */
	public void setRegClass(java.lang.String newRegClass)
	{
		csRegClass = newRegClass;
	}
	/**
	 * Set sticker number
	 * 
	 * @param newStkrNo java.lang.String
	 */
	public void setStkrNo(java.lang.String newStkrNo)
	{
		csStkrNo = newStkrNo;
	}
}
