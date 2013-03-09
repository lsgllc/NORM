package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

/*
 * InventoryInquiryReportData.java
 * 
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ ----------- --------------------------------------------
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896
 * Ray Rowehl	09/30/2005	Moved to services.data since this is a 
 * 							data class.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	10/17/2005	Use addPaddingRight(String,int,String) vs.
 *    	 					addPaddingRight(String[],int[],String)
 * 							defect 7899 Ver 5.2.3 	
 * ---------------------------------------------------------------------
 */

/**
 * Data Object to receive data for Inventory Inquiry Report
 *
 * @version	5.2.3 			10/17/2005
 * @author	Kathy Harrell
 * <br>Creation Date:		09/19/2001 16:30:32
 */

public class InventoryInquiryReportData
	extends InventoryAllocationData
	implements Comparable
{
	private static final int LENGTH_INVID = 7;
	private static final int LENGTH_INVITMYR = 4;
	private static final int LENGTH_ITMCD = 8;
	private static final int LENGTH_PATRNSEQNO =
		String.valueOf(Integer.MAX_VALUE).length();

	protected String csItmCdDesc;
	// This is used in the compareTo method to determine how to sort the
	// data returned from the db for the Inquiry Current Balance Report.
	protected String csInvInqBy;

	/**
	 * Compares this object with the specified object for order.  
	 * Returns a negative integer, zero, or a positive integer as this
	 * object is less than, equal to, or greater than the specified 
	 * object.
	 * 
	 * @param aaObj Object 
	 * @return int
	 */
	public int compareTo(Object aaObj)
	{
		InventoryInquiryReportData laInvInqReportData =
			(InventoryInquiryReportData) aaObj;

		// Keys for the argument object
		String lsArgumentKey1 = laInvInqReportData.getInvId();
		String lsArgumentKey2 = laInvInqReportData.getItmCd();
		String lsArgumentKey3 =
			String.valueOf(laInvInqReportData.getInvItmYr());
		String lsArgumentKey4 =
			String.valueOf(laInvInqReportData.getPatrnSeqNo());
		// Keys for this object
		String lsThisKey1 = getInvId();
		String lsThisKey2 = getItmCd();
		String lsThisKey3 = String.valueOf(getInvItmYr());
		String lsThisKey4 = String.valueOf(getPatrnSeqNo());

		// Pad the itmcodes with zeros to the right of the item code.
		// Need to do this b/c if pad to the left 
		// (like constructPrimaryKey) they are not in alphabetical order
		lsArgumentKey2 =
			UtilityMethods.addPaddingRight(
				lsArgumentKey2,
				LENGTH_ITMCD,
				CommonConstant.STR_ZERO);

		lsThisKey2 =
			UtilityMethods.addPaddingRight(
				lsThisKey2,
				LENGTH_ITMCD,
				CommonConstant.STR_ZERO);

		String lsPrimaryKey = null;
		String lsThisPrimaryKey = null;

		try
		{
			if (laInvInqReportData
				.getInvInqBy()
				.equals(InventoryConstant.EMP)
				|| laInvInqReportData.getInvInqBy().equals(
					InventoryConstant.WS)
				|| laInvInqReportData.getInvInqBy().equals(
					InventoryConstant.DLR)
				|| laInvInqReportData.getInvInqBy().equals(
					InventoryConstant.SUBCON))
			{
				lsPrimaryKey =
					UtilityMethods.constructPrimaryKey(
						new String[] {
							lsArgumentKey1,
							lsArgumentKey2,
							lsArgumentKey3,
							lsArgumentKey4 },
						new int[] {
							LENGTH_INVID,
							LENGTH_ITMCD,
							LENGTH_INVITMYR,
							LENGTH_PATRNSEQNO });

				lsThisPrimaryKey =
					UtilityMethods.constructPrimaryKey(
						new String[] {
							lsThisKey1,
							lsThisKey2,
							lsThisKey3,
							lsThisKey4 },
						new int[] {
							LENGTH_INVID,
							LENGTH_ITMCD,
							LENGTH_INVITMYR,
							LENGTH_PATRNSEQNO });
			}
			else
			{
				lsPrimaryKey =
					UtilityMethods.constructPrimaryKey(
						new String[] {
							lsArgumentKey2,
							lsArgumentKey3,
							lsArgumentKey4 },
						new int[] {
							LENGTH_ITMCD,
							LENGTH_INVITMYR,
							LENGTH_PATRNSEQNO });

				lsThisPrimaryKey =
					UtilityMethods.constructPrimaryKey(
						new String[] {
							lsThisKey2,
							lsThisKey3,
							lsThisKey4 },
						new int[] {
							LENGTH_ITMCD,
							LENGTH_INVITMYR,
							LENGTH_PATRNSEQNO });
			}
		}
		catch (RTSException aeRTSEx)
		{
			// empty code block
		}
		return lsThisPrimaryKey.compareTo(lsPrimaryKey);
	}

	/**
	 * Get Inventory Inquiry By
	 * 
	 * @return String
	 */
	public String getInvInqBy()
	{
		return csInvInqBy;
	}

	/**
	 * Returns the value of ItmCdDesc
	 * 
	 * @return String
	 */
	public final String getItmCdDesc()
	{
		return csItmCdDesc;
	}

	/**
	 * Set Inventory Inquiry By
	 * 
	 * @param asInvInqBy String
	 */
	public void setInvInqBy(String asInvInqBy)
	{
		csInvInqBy = asInvInqBy;
	}

	/**
	* Sets the value of ItmCdDesc
	* 
	* @param asItmCdDesc String
	*/
	public final void setItmCdDesc(String asItmCdDesc)
	{
		csItmCdDesc = asItmCdDesc;
	}
}
