package com.txdot.isd.rts.services.data;

import java.lang.reflect.Field;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;

/*
 *
 * ProcessInventoryData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C Walker		10/30/2001	Added PLPFlag field
 *							Added method to convert from a 
 *							InventoryAllocationData object
 * Min Wang 	07/08/2003	Modifed convertFromInvAlloctnData() and 
 * 							convertToInvAlloctnUIData()
 *                          to handle data changes.
 *                          defect 6076.  
 * K Harrell	04/13/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * Min Wang		05/16/2005  Change to use new isCalcInv() instead of 
 * 							old getCalcInv().
 * 							modify convertFromInvAlloctnData(),
 * 							convertToInvAlloctnUIData()
 * 							defect 6370 Ver 5.2.3
 * Ray Rowehl	02/12/2007	Remove methods that are moved to 
 * 							InventoryAllocationData.
 * 							delete ciTransAMDate, ciTransTime, 
 * 								csTransEmpId, csTransWsId 
 * 							delete getTransEmpId(), getTransTime(), 
 * 								getTransWsId(), setTransEmpId(), 
 * 								setTransTime(), setTransWsId()
 * 							modify getTransAMDate(), setTransAMDate()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	02/13/2007	Move RangeCd to InventoryAllocationData 
 * 							as part of creating Virtual Inventory.
 * 							delete csRangeCd
 * 							delete getRangeCd(), setRangeCd()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	02/17/2007	Enhance conversion from InvAllocData.
 * 							Move Already Issued boolean.
 * 							delete cbAlreadyIssued
 * 							delete isAlreadyIssued(), setAlreadyIssued()
 * 							modify convertFromInvAlloctnData()
 * 							defect 9116 Ver Special Plates
 * Ray Rowehl	05/23/2007	Update PID conversion.
 * 							modify convertFromInvAlloctnData()
 * 							defect 9116 Ver Special Plates
 * ---------------------------------------------------------------------
 */
 
/**
 * This Data class contains attributes and get and set methods for 
 * ProcessInventoryData 
 * 
 * @version	Special Plates	05/23/2007 
 * @author	Kathy Harrell
 * <br>Creation Date:		10/09/2001 16:00:46
 */

public class ProcessInventoryData
	extends com.txdot.isd.rts.services.data.InventoryAllocationData
	implements Displayable
{
	// boolean 
	// defect 9116
	//private boolean cbAlreadyIssued = false;
	// end defect 9116
	private boolean cbBarCodeScanned;
	private boolean cbPLPFlag = false;
	private boolean cbPreviouslyVoidedItem = false;
	private boolean cbReleaseHold;

	// int
	private int ciCentralStock;
	private int ciIssueMismatchIndi;
	private int ciIssueStatus;
	private int ciMaxQty;

	// String
	private String csEntity;
	private String csId;
	// defect 9116
	//private String csRangeCd;
	// end defect 9116

	//	Constant
	/**
	 * One of the following constants:
	 * - VALID_HELD
	 * - NOT_EXISTED_ITEM
	 * - NOT_EXISTED_ITEM_REUSED
	 * - NOT_EXISTED_ITEM_REISSUED
	 * - MISMATCHED_ITEM
	 */
	public static final int VALID_HELD = 1;
	public static final int NOT_EXISTED_ITEM = 2;
	public static final int NOT_EXISTED_ITEM_REUSED = 3;
	public static final int NOT_EXISTED_ITEM_REISSUED = 4;
	public static final int MISMATCHED_ITEM = 5;

	private final static long serialVersionUID = -1069667159198740264L;
	
	/**
	 * Takes an InventoryAllocationData object and copies it into a 
	 * ProcessInventoryData object.
	 * 
	 * @param aaInvAlloctnData InventoryAllocationData
	 * @return ProcessInventoryData
	 */
	public ProcessInventoryData convertFromInvAlloctnData(InventoryAllocationData aaInvAlloctnData)
	{
		ProcessInventoryData laProcsInvData =
			new ProcessInventoryData();
		laProcsInvData.setOfcIssuanceNo(
			aaInvAlloctnData.getOfcIssuanceNo());
		laProcsInvData.setSubstaId(aaInvAlloctnData.getSubstaId());
		laProcsInvData.setNewSubstaId(
			aaInvAlloctnData.getNewSubstaId());
		laProcsInvData.setItmCd(aaInvAlloctnData.getItmCd());
		laProcsInvData.setInvItmYr(aaInvAlloctnData.getInvItmYr());
		laProcsInvData.setInvItmNo(aaInvAlloctnData.getInvItmNo());
		laProcsInvData.setInvId(aaInvAlloctnData.getInvId());
		laProcsInvData.setInvLocIdCd(aaInvAlloctnData.getInvLocIdCd());
		laProcsInvData.setInvItmEndNo(
			aaInvAlloctnData.getInvItmEndNo());
		laProcsInvData.setInvQty(aaInvAlloctnData.getInvQty());
		laProcsInvData.setPatrnSeqNo(aaInvAlloctnData.getPatrnSeqNo());
		laProcsInvData.setInvStatusCd(
			aaInvAlloctnData.getInvStatusCd());
		laProcsInvData.setPatrnSeqCd(aaInvAlloctnData.getPatrnSeqCd());
		laProcsInvData.setCalcInv(aaInvAlloctnData.isCalcInv());
		laProcsInvData.setEndPatrnSeqNo(
			aaInvAlloctnData.getEndPatrnSeqNo());
		
		// defect 9116
		laProcsInvData.setProcessInvData(true);
		laProcsInvData.setISA(aaInvAlloctnData.isISA());
		laProcsInvData.setViItmCd(aaInvAlloctnData.getViItmCd());
		laProcsInvData.setCustSupplied(aaInvAlloctnData.isCustSupplied());
		// end defect 9116
		return laProcsInvData;
	}
	
	/**
	 * Create new InventoryAllocationUIData from ProcessInventoryData
	 * 
	 * @param aaProcsInvData ProcessInventoryData
	 * @return InventoryAllocationUIData
	 */
	public InventoryAllocationUIData convertToInvAlloctnUIData(ProcessInventoryData aaProcsInvData)
	{

		InventoryAllocationUIData laInvAlloctnUIData =
			new InventoryAllocationUIData();
		laInvAlloctnUIData.setOfcIssuanceNo(
			aaProcsInvData.getOfcIssuanceNo());
		laInvAlloctnUIData.setSubstaId(aaProcsInvData.getSubstaId());
		laInvAlloctnUIData.setNewSubstaId(
			aaProcsInvData.getNewSubstaId());
		laInvAlloctnUIData.setItmCd(aaProcsInvData.getItmCd());
		laInvAlloctnUIData.setInvItmYr(aaProcsInvData.getInvItmYr());
		laInvAlloctnUIData.setInvItmNo(aaProcsInvData.getInvItmNo());
		laInvAlloctnUIData.setInvId(aaProcsInvData.getInvId());
		laInvAlloctnUIData.setInvLocIdCd(
			aaProcsInvData.getInvLocIdCd());
		laInvAlloctnUIData.setInvItmEndNo(
			aaProcsInvData.getInvItmEndNo());
		laInvAlloctnUIData.setInvQty(aaProcsInvData.getInvQty());
		laInvAlloctnUIData.setPatrnSeqNo(
			aaProcsInvData.getPatrnSeqNo());
		laInvAlloctnUIData.setInvStatusCd(
			aaProcsInvData.getInvStatusCd());
		laInvAlloctnUIData.setPatrnSeqCd(
			aaProcsInvData.getPatrnSeqCd());
		//defect 6076
		//defect 6370
		laInvAlloctnUIData.setCalcInv(aaProcsInvData.isCalcInv());
		//end defect 6370
		laInvAlloctnUIData.setEndPatrnSeqNo(
			aaProcsInvData.getEndPatrnSeqNo());
		//end defect 6076
		return laInvAlloctnUIData;
	}
	
	/**
	 * Get Object field attributes
	 * 
	 * @return Map
	 */
	public Map getAttributes()
	{
		java.util.HashMap lhmHash = new java.util.HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();
		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				lhmHash.put(
					larrFields[i].getName(),
					larrFields[i].get(this));
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}
	
	/**
	 * Returns the value of CentralStock
	 * @return  int
	 */
	public final int getCentralStock()
	{
		return ciCentralStock;
	}
	
	/**
	 * Returns the value of Entity
	 * @return  String
	 */
	public final String getEntity()
	{
		return csEntity;
	}
	
	/**
	* Returns the value of Id
	* @return  String
	*/
	public final String getId()
	{
		return csId;
	}
	
	/**
	 * Returns the value of IssueMismatchIndi
	 * 
	 * @return int
	 */
	public int getIssueMismatchIndi()
	{
		return ciIssueMismatchIndi;
	}
	
	/**
	 * Returns the value of IssueStatus;
	 * 
	 * @return int
	 */
	public int getIssueStatus()
	{
		return ciIssueStatus;
	}
	
	/**
	* Returns the value of MaxQty
	* @return  int 
	*/
	public final int getMaxQty()
	{
		return ciMaxQty;
	}

	/**
	 * Returns the value of TransAMDate 
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return getTransAmDate();
	}
	
	/**
	 * Returns the value of BarCodeScanned
	 * @return boolean
	 */
	public boolean isBarCodeScanned()
	{
		return cbBarCodeScanned;
	}
	
	/**
	 * Returns the value of PLPFlag
	 * 
	 * @return boolean
	 */
	public boolean isPLPFlag()
	{
		return cbPLPFlag;
	}
	
	/**
	 * Returns the value of PreviouslyVoidedItem
	 * @return boolean
	 */
	public boolean isPreviouslyVoidedItem()
	{
		return cbPreviouslyVoidedItem;
	}
	
	/**
	 * Returns the value of ReleaseHold
	 * 
	 * @return boolean
	 */
	public boolean isReleaseHold()
	{
		return cbReleaseHold;
	}

	/**
	* This method sets the value of CentralStock
	* @param aiCentralStock  int 
	*/
	public final void setCentralStock(int aiCentralStock)
	{
		ciCentralStock = aiCentralStock;
	}
	
	/**
	* This method sets the value of Entity
	* @param asEntity   String
	*/
	public final void setEntity(String asEntity)
	{
		csEntity = asEntity;
	}
	
	/**
	* This method sets the value of Id
	* @param aId   String
	*/
	public final void setId(String asId)
	{
		csId = asId;
	}
	
	/**
	 * Set the value of BarCodeScanned 
	 * @param abBarCodeScanned boolean
	 */
	public void setIsBarCodeScanned(boolean abBarCodeScanned)
	{
		cbBarCodeScanned = abBarCodeScanned;
	}
	
	/**
	 * Set the value of IssueMismatchIndi
	 * 
	 * @param aiIssueMismatchIndi int
	 */
	public void setIssueMismatchIndi(int aiIssueMismatchIndi)
	{
		ciIssueMismatchIndi = aiIssueMismatchIndi;
	}
	
	/**
	 * Set the value of IssueStatus 
	 * 
	 * @param aiIssueStatus int
	 */
	public void setIssueStatus(int aiIssueStatus)
	{
		ciIssueStatus = aiIssueStatus;
	}
	
	/**
	* This method sets the value of MaxQty.
	* @param aiMaxQty   int 
	*/
	public final void setMaxQty(int aiMaxQty)
	{
		ciMaxQty = aiMaxQty;
	}
	
	/**
	 * Set the value of PLPFlag
	 * 
	 * @param abPLPFlag boolean
	 */
	public void setPLPFlag(boolean abPLPFlag)
	{
		cbPLPFlag = abPLPFlag;
	}
	
	/**
	 * Set the value of PreviouslyVoidedItem
	 * @param abPreviouslyVoidedItem boolean
	 */
	public void setPreviouslyVoidedItem(boolean abPreviouslyVoidedItem)
	{
		cbPreviouslyVoidedItem = abPreviouslyVoidedItem;
	}

	/**
	 * Set the value of ReleaseHold
	 * 
	 * @param abReleaseHold boolean
	 */
	public void setReleaseHold(boolean abReleaseHold)
	{
		cbReleaseHold = abReleaseHold;
	}
	
	/**
	 * Set the value of TransAMDate
	 * 
	 * @param aiTransAMDate int
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		setTransAmDate(aiTransAMDate);
	}
	
	/**
	 * Set the value of TransWsId
	 * 
	 * @param asTransWsId String
	 */
	public void setTransWsId(String asTransWsId)
	{
		int liNewTransWsId = -1;
		try
		{
			liNewTransWsId = Integer.parseInt(asTransWsId);
		}
		finally
		{
			//take no action.  already set default value
		}
		setTransWsId(liNewTransWsId);
	}
}
