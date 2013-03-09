package com.txdot.isd.rts.server.registration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InventoryAllocation;
import com.txdot.isd.rts.server.db.ProcessInventorySQL;

import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;
import com.txdot.isd.rts.services.data.InventoryAllocationData;
import com.txdot.isd.rts.services.data.ProcessInventoryData;
import com.txdot.isd.rts.services.data.SubcontractorRenewalCacheData;
import com.txdot.isd.rts.services.data.SubcontractorRenewalData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 *
 * SubcontractorRenewalServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/28/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Imported class.
 * 							Ver 5.2.0
 * K Harrell	08/24/2004	Correct Inventory Prompting
 *							modify procSubconRenwl()
 *							defect 7484   Ver 5.2.1
 * K Harrell	10/04/2004	Remove call to addTrans from procSubconRenwl
 *							deprecate addTrans()
 *							deprecate getNextInventory(int,int,Object) 
 *							modify procSubconRenwl()
 *							defect 7586 Ver 5.2.1 
 * Ray Rowehl	02/08/2005	Change package reference for Transaction
 * 							modify addTrans()
 * 							defect 7705 Ver 5.2.3
 * K Harrell	06/22/2005	Rename INV014 to INV003
 * 							delete addTrans(),
 * 							deprecate cancelSubcon(),
 * 							getNextInventory(Object), 
 * 							subconServerProcess()
 * 							modify processData()
 * 							defect 6966 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/** 
 * The SubcontractorRenewalServerBusiness dispatch the incoming request to
 * the function request on the server side for SubcontractorRenewal events.  
 * It also returns the result back to the caller
 * 
 * @version	5.2.3		06/22/2005
 * @author 	Nancy Ting
 * <br>Creation Date:	10/18/2001
 */
public class SubcontractorRenewalServerBusiness
{
	public static final String FILLER = "filler";
	public static final String SUBCON_LOC_ID_CD = "S";
	public static final String CENTRAL_LOC_CD = "C";
	public static final String WORKSTATION_LOC_CD = "W";
	public static final String DEALER_LOC_ID = "D";
	public static final String EMPLOYEE_LOC_CD = "E";
	private static final String SUBCON_NAME = "Subcontractor";
	private static final String CENTRAL_NAME = "Central";
	private static final String WORKSTATION_NAME = "Workstation";
	private static final String DEALER_NAME = "Dealer";
	private static final String EMPLOYEE_NAME = "Employee";

	/**
	 * SubcontractorRenewalServerBusiness constructor comment.
	 */
	public SubcontractorRenewalServerBusiness()
	{
		super();
	}
	//	/**
	//	 * Releasing all inventory for cancel Subcon operation
	//	 * 
	//	 * @param aaData Object
	//	 * @param abCancelHeldOnly 
	//	 * @deprecated 
	//	 * @throws RTSException
	//	 */
	//	private Object cancelSubcon(
	//		Object aaData,
	//		boolean abCancelHeldOnly)
	//		throws RTSException
	//	{
	//		/*
	//		DatabaseAccess dba = null;
	//		boolean lbCreateDb = false;
	//		SubcontractorRenewalCacheData lSubcontractorRenewalCacheData =
	//			null;
	//		if (data instanceof Vector)
	//		{
	//			Vector lvInput = (Vector) data;
	//			dba = (DatabaseAccess) lvInput.get(0);
	//			lSubcontractorRenewalCacheData =
	//				(SubcontractorRenewalCacheData) lvInput.get(1);
	//		}
	//		else
	//		{
	//			lbCreateDb = true;
	//			dba = new DatabaseAccess();
	//			dba.beginTransaction();
	//			lSubcontractorRenewalCacheData =
	//				(SubcontractorRenewalCacheData) data;
	//		}
	//		SubcontractorRenewalCacheData lCopySubcontractorRenewalCacheData =
	//			(SubcontractorRenewalCacheData) UtilityMethods.copy(
	//				lSubcontractorRenewalCacheData);
	//		Vector lvSubconList =
	//			lSubcontractorRenewalCacheData.getSubconRenewalData();
	//		if (lvSubconList == null || abCancelHeldOnly)
	//		{
	//			lvSubconList = new Vector();
	//		}
	//		//add the held items in the vector as well
	//		SubcontractorRenewalData lSubcontractorRenewalDataHeldItems =
	//			new SubcontractorRenewalData();
	//		lSubcontractorRenewalDataHeldItems.setProcInvPlt(
	//			lSubcontractorRenewalCacheData.getHeldInvPlt());
	//		lSubcontractorRenewalDataHeldItems.setProcInvStkr(
	//			lSubcontractorRenewalCacheData.getHeldInvStkr());
	//		lvSubconList.addElement(lSubcontractorRenewalDataHeldItems);
	//		String lsSubconId =
	//			String.valueOf(
	//				lSubcontractorRenewalCacheData
	//					.getSubcontractorHdrData()
	//					.getSubconId());
	//		boolean lbSuccess = false;
	//		try
	//		{
	//			//loop through all the items to release them
	//			releaseSubconList(
	//				lvSubconList,
	//				lSubcontractorRenewalCacheData,
	//				dba);
	//			lbSuccess = true;
	//		}
	//		finally
	//		{
	//			if (lbCreateDb)
	//			{
	//				if (lbSuccess)
	//				{
	//					dba.endTransaction(dba.COMMIT);
	//				}
	//				else
	//				{
	//					dba.endTransaction(dba.ROLLBACK);
	//				}
	//			}
	//		}
	//		*/
	//		return null;
	//	}

	/**
	 * Check whether the inventory items on the diskette are found in 
	 * the database.  
	 * 
	 * If found  
	 *    Update InvStatusCd
	 * 	  Put to lhtInventoryList w/ Trans Key, ProcessInventoryData 
	 * else  
	 * 	  Put to lhtInventoryList w/ Trans Key, "Filler"
	 * 
	 * @param aaDataObject
	 * @throws RTSException 
	 */
	private Object checkDisketteInventory(Object aaData)
		throws RTSException
	{
		System.out.println("checkDisketteInventory");
		Hashtable lhtInventoryList = (Hashtable) aaData;
		Iterator laIterator = lhtInventoryList.keySet().iterator();

		if (lhtInventoryList.size() > 0)
		{
			boolean lbSuccess = false;
			DatabaseAccess laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			try
			{
				while (laIterator.hasNext())
				{
					//check inventory
					Integer laKey = (Integer) laIterator.next();
					ProcessInventoryData laProcInvDataInput =
						(ProcessInventoryData) lhtInventoryList.get(
							laKey);

					InventoryServerBusiness laInventoryServerBusiness =
						new InventoryServerBusiness();
					Vector lvInput = new Vector();
					lvInput.add(laDBA);
					lvInput.add(laProcInvDataInput);

					Vector lvProcInvDataOutput =
						(Vector) laInventoryServerBusiness.processData(
							GeneralConstant.INVENTORY,
							InventoryConstant
								.VERIFY_INVENTORY_ITEM_IN_DB,
							lvInput);

					ProcessInventoryData laProcessInventoryData = null;

					if (lvProcInvDataOutput != null
						&& lvProcInvDataOutput.size() != 0)
					{
						laProcessInventoryData =
							(
								ProcessInventoryData) lvProcInvDataOutput
									.get(
								0);
					}

					// If already issued, needs to go to INV003
					if (laProcessInventoryData != null
						&& laProcessInventoryData.isAlreadyIssued())
					{
						laProcessInventoryData = null;
					}

					// If on hold, also an error
					if (laProcessInventoryData != null
						&& laProcessInventoryData.getInvStatusCd() != 0)
					{
						laProcessInventoryData = null;
					}

					// If inventory is allocated to different entity
					//     go to INV003
					if (laProcessInventoryData != null
						&& !(laProcessInventoryData
							.getInvId()
							.equals(laProcInvDataInput.getInvId())
							&& laProcessInventoryData
								.getInvLocIdCd()
								.equals(
								laProcInvDataInput.getInvLocIdCd())))
					{
						laProcessInventoryData = null;
					}

					// If inventory is not null, i.e. inventory is 
					// allocated to subcontractor and not on hold,
					// place on hold it
					if (laProcessInventoryData != null)
					{
						laProcessInventoryData.setInvQty(1);
						laProcessInventoryData.setInvItmNo(
							laProcInvDataInput.getInvItmNo());
						laProcessInventoryData.setInvItmEndNo(
							laProcInvDataInput.getInvItmNo());
						laProcessInventoryData.setInvStatusCd(2);
						updateInvStatusCd(
							laProcessInventoryData,
							laDBA);
					}
					// Put inventory data in hashtable
					if (laProcessInventoryData == null)
					{
						lhtInventoryList.put(laKey, "FILLER");
					}
					else
					{
						lhtInventoryList.put(
							laKey,
							laProcessInventoryData);
					}
				}
				lbSuccess = true;
			}
			catch (RTSException aeRTSEx)
			{
				lbSuccess = false;
			}
			finally
			{
				if (lbSuccess)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
				else
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				}
			}
		}
		return lhtInventoryList;
	}
	/**
	 * Delete selected SubcontractorRenewal Transactions
	 * 
	 * @param aaData Object
	 * @throws RTSException
	 */
	private Object deleteSelectedSubconRenwlRecord(Object aaData)
		throws RTSException
	{
		System.out.println("deleteSelectedSubconRenwlRecord");
		DatabaseAccess laDBA = null;
		boolean lbCreateDb = false;
		SubcontractorRenewalCacheData laSubcontractorRenewalCacheData =
			null;
		//method is used in modify as well as delete
		if (aaData instanceof Vector)
		{
			Vector lvInput = (Vector) aaData;
			laDBA = (DatabaseAccess) lvInput.get(0);
			laSubcontractorRenewalCacheData =
				(SubcontractorRenewalCacheData) lvInput.get(1);
		}
		else
		{
			lbCreateDb = true;
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			laSubcontractorRenewalCacheData =
				(SubcontractorRenewalCacheData) aaData;
		}
		//send the list of data to be removed
		Vector lvDeleteTransKeyList =
			laSubcontractorRenewalCacheData.getDeleteTransKeyList();

		Vector lvReleaseInvList =
			laSubcontractorRenewalCacheData.getReleaseInventoryList();
		boolean lbSuccess = false;
		try
		{
			Vector lvIn = new Vector();
			lvIn.addElement(laDBA);
			lvIn.addElement(lvDeleteTransKeyList);
			com
				.txdot
				.isd
				.rts
				.server
				.common
				.business
				.CommonServerBusiness laCommonServerBusiness =
				new com
					.txdot
					.isd
					.rts
					.server
					.common
					.business
					.CommonServerBusiness();

			laCommonServerBusiness.processData(
				GeneralConstant.COMMON,
				CommonConstant.CANCEL_SELECTED_TRANS,
				lvIn);
			//release items
			releaseInvList(lvReleaseInvList, laDBA);
			lbSuccess = true;
		}
		finally
		{
			if (lbCreateDb)
			{
				if (lbSuccess)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
				else
				{
					laDBA.endTransaction(DatabaseAccess.ROLLBACK);
				}
			}
		}
		return laSubcontractorRenewalCacheData;
	}

	/**
	 * Get the next available inventory for prompting in Subcon
	 * 
	 * @param aaProcInvData ProcessInventoryData
	 * @throws RTSException
	 */
	private ProcessInventoryData getNextInventory(ProcessInventoryData aaProcInvData)
		throws RTSException
	{
		System.out.println("getNextInventory");
		DatabaseAccess laDBA = new DatabaseAccess();
		ProcessInventorySQL laProcessInventorySQL =
			new ProcessInventorySQL(laDBA);
		laDBA.beginTransaction();
		try
		{
			if (aaProcInvData != null)
			{
				Log.write(
					Log.APPLICATION,
					this,
					"Starting DB call to ProcessInventorySQL");
				Vector lvPlt =
					laProcessInventorySQL
						.qryNextAvailableInventoryEntity(
						aaProcInvData);
				Log.write(
					Log.APPLICATION,
					this,
					"Successful DB call to ProcessInventorySQL");
				if (lvPlt != null && lvPlt.size() > 0)
				{
					return (ProcessInventoryData) lvPlt.get(0);
				}
				else
				{
					return null;
				}
			}
			else
			{
				return null;
			}
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to ProcessInventorySQL");
			throw aeRTSEx;
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
		}
	}
	/**
	 * Get the allocation inventory for Subcon PCR34
	 * 
	 * @param aaData Object
	 * @throws RTSException 
	 */
	private Object getSubconAllocatedInventory(Object aaData)
		throws RTSException
	{
		System.out.println("getSubconAllocatedInventory");
		Vector lvInvItems = (Vector) aaData;

		InventoryAllocationData laInventoryAllocationData =
			(InventoryAllocationData) lvInvItems.get(0);

		Log.write(
			Log.APPLICATION,
			this,
			"Starting DB call to InventoryAllocation");

		DatabaseAccess laDatabaseAccess = new DatabaseAccess();
		try
		{
			laDatabaseAccess.beginTransaction();

			InventoryAllocation laInventoryAllocation =
				new InventoryAllocation(laDatabaseAccess);
			Vector lvReturn =
				laInventoryAllocation.qryInventoryAllocationForSubcon(
					laInventoryAllocationData);
			Log.write(
				Log.APPLICATION,
				this,
				"Successful DB call to InventoryAllocation");
			return lvReturn;
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to InventoryAllocation");
			throw aeRTSEx;
		}
		finally
		{
			laDatabaseAccess.endTransaction(DatabaseAccess.NONE);
		}
	}
	/**
	 * Dispatches methods according to function ids
	 * 
	 * @param aiModule int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		System.out.println("processData");
		System.out.println("FunctionId=" + aiFunctionId);

		switch (aiFunctionId)
		{
			case RegistrationConstant.GET_SUBCON_ALLOCATED_INV :
				return getSubconAllocatedInventory(aaData);

			case RegistrationConstant.PROCESS_SUBCON_RENWL :
				return procSubconRenwl(aaData);

			case RegistrationConstant.RELEASE_INV003_ITM :
				return releaseINV003Inventory(aaData);

			case RegistrationConstant
				.DEL_SELECTED_SUBCON_RENWL_RECORD :
				return deleteSelectedSubconRenwlRecord(aaData);

				//	case RegistrationConstant.CANCEL_SUBCON :
				//		return cancelSubcon(aaData, false);

				//	case RegistrationConstant.CANCEL_HELD_SUBCON :
				//		return cancelSubcon(aaData, true);

			case RegistrationConstant.CHECK_DISK_INVENTORY :
				return checkDisketteInventory(aaData);
		}
		return null;
	}
	/**
	 * Process the SubconRenwl Request
	 * 
	 * @param aaSubconCache Object
	 * @throws RTSException 
	 */
	private Object procSubconRenwl(Object aaSubconCache)
		throws RTSException
	{
		System.out.println("procSubconRenwl");
		SubcontractorRenewalCacheData laSubconRenewalCacheData =
			(SubcontractorRenewalCacheData) aaSubconCache;

		SubcontractorRenewalData laSubconRenewalData =
			laSubconRenewalCacheData.getTempSubconRenewalData();

		SubcontractorRenewalData laRecordToBeModified =
			laSubconRenewalCacheData.getRecordTobeModified();

		if (laSubconRenewalCacheData.getHeldInvPlt() != null)
		{
			//from INV003
			if (laSubconRenewalCacheData
				.getINV003ProcessInventoryData()
				!= null)
			{
				//when press Enter on INV003, no need to release
				//set it to ProcInvPlt
			}
			else if (
				laSubconRenewalData.getPltItmCd() != null
					&& laSubconRenewalData.getPltItmCd().equals(
						laSubconRenewalCacheData
							.getHeldInvPlt()
							.getItmCd())
					&& laSubconRenewalData.getNewPltNo().equals(
						laSubconRenewalCacheData
							.getHeldInvPlt()
							.getInvItmNo()))
			{
				// If modify and held plate is the same as new plate, 
				// no need to release
				laSubconRenewalData.setProcInvPlt(
					laSubconRenewalCacheData.getHeldInvPlt());
			}
			else
			{
				//release held plate
				laSubconRenewalCacheData
					.getHeldInvPlt()
					.setInvStatusCd(
					0);
				updateInvStatusCd(
					laSubconRenewalCacheData.getHeldInvPlt(),
					null);
				laSubconRenewalCacheData.setHeldInvPlt(null);
			}
		}

		// If performed diskette validation and there is a held plate,
		// and the held plate is different from the plate actually issued
		// in modify release plate
		if (laRecordToBeModified != null
			&& !laRecordToBeModified.isProcessed()
			&& laSubconRenewalCacheData.getDiskHeldPltList().get(
				laRecordToBeModified.getTransKeyNumber())
				!= null
			&& !(laRecordToBeModified.getPltItmCd() != null
				&& laRecordToBeModified.getPltItmCd().equals(
					laSubconRenewalData.getPltItmCd())
				&& laRecordToBeModified.getNewPltNo().equals(
					laSubconRenewalData.getNewPltNo())))
		{
			laRecordToBeModified.getProcInvPlt().setInvStatusCd(0);
			updateInvStatusCd(
				laRecordToBeModified.getProcInvPlt(),
				null);
			laRecordToBeModified.setProcInvPlt(null);
			//remove the entry from disk held plt list
			laSubconRenewalCacheData.getDiskHeldPltList().remove(
				laRecordToBeModified.getTransKeyNumber());
		}
		// if from INV003
		if (laSubconRenewalCacheData.getINV003ProcessInventoryData()
			!= null)
		{
			laSubconRenewalData.setProcInvPlt(
				laSubconRenewalCacheData
					.getINV003ProcessInventoryData());
			laSubconRenewalCacheData.setINV003AllocatedName(null);
			laSubconRenewalCacheData.setINV003ProcessInventoryData(
				null);
			laSubconRenewalData.setValidatePltIndi(
				SubcontractorRenewalData.PLT_VALIDATED);
		}

		//if already validate and is in modify
		if (laRecordToBeModified != null
			&& laSubconRenewalData.getValidatePltIndi()
				== SubcontractorRenewalData.VALIDATE_PLT)
		{
			// Only the ones that are already processed, for disk 
			// renewals that are not processed, still need to validate 
			// plate also for unprocessed but validated inventory,  
			// no need to validate
			if ((laRecordToBeModified.isProcessed()
				|| (!laRecordToBeModified.isProcessed()
					&& laSubconRenewalCacheData.getDiskHeldPltList().get(
						laRecordToBeModified.getTransKeyNumber())
						!= null))
				&& (laRecordToBeModified.getPltItmCd() != null
					&& laRecordToBeModified.getPltItmCd().equals(
						laSubconRenewalData.getPltItmCd())
					&& laRecordToBeModified.getNewPltNo().equals(
						laSubconRenewalData.getNewPltNo())))
			{
				laSubconRenewalData.setProcInvPlt(
					laRecordToBeModified.getProcInvPlt());
				laSubconRenewalData.setValidatePltIndi(
					SubcontractorRenewalData.PLT_VALIDATED);
			}
		}
		//check if required to validate plate
		if (laSubconRenewalData.getValidatePltIndi()
			== SubcontractorRenewalData.VALIDATE_PLT)
		{
			validatePlt(laSubconRenewalCacheData);
			if (laSubconRenewalCacheData
				.getINV003ProcessInventoryData()
				!= null)
			{
				return laSubconRenewalCacheData;
			}
		}

		//get next available inventory if needed (do not get next if modify
		if (laSubconRenewalData.getProcInvPlt() != null
			&& laSubconRenewalCacheData.getRecordTobeModified() == null)
		{
			ProcessInventoryData laNextInvInput =
				(ProcessInventoryData) UtilityMethods.copy(
					laSubconRenewalData.getProcInvPlt());
			laNextInvInput.setInvLocIdCd("S");
			laNextInvInput.setInvId(
				String.valueOf(
					laSubconRenewalCacheData
						.getSubcontractorHdrData()
						.getSubconId()));

			ProcessInventoryData laNextInv =
				getNextInventory(laNextInvInput);

			if (laNextInv != null)
			{
				// defect 7484
				// Override invqty from call; cause inventory split 
				laNextInv.setInvQty(1);
				laNextInv.setInvItmEndNo(laNextInv.getInvItmNo());
				// end defect 7484 
				laNextInv.setInvStatusCd(2);
				updateInvStatusCd(laNextInv, null);
				laSubconRenewalCacheData.setHeldInvPlt(laNextInv);
			}
		}
		return laSubconRenewalCacheData;
	}
	/**
	 * Release INV003 held item
	 * 
	 * @param aiModule int 
	 * @param aiFunctionId int 
	 * @param aaSubconCache Object
	 * @throws RTSException 
	 */
	private Object releaseINV003Inventory(Object aaSubconCache)
		throws RTSException
	{
		System.out.println("releaseINV003Inventory ");
		SubcontractorRenewalCacheData laSubconCacheData =
			(SubcontractorRenewalCacheData) aaSubconCache;

		ProcessInventoryData laReleaseItem =
			laSubconCacheData.getINV003ProcessInventoryData();
			
		System.out.println("Inventory: " + laReleaseItem.getInvItmNo());
		laReleaseItem.setInvStatusCd(0);
		updateInvStatusCd(laReleaseItem, null);
		//clean up data object
		laSubconCacheData.setINV003ProcessInventoryData(null);
		laSubconCacheData.setINV003AllocatedName(null);
		laSubconCacheData.setINV003Voided(false);
		return laSubconCacheData;
	}
	/**
	 * Release the list of Subcon inventory
	 * 
	 * @param avInvList Vector
	 * @param aaDBA DatabaseAccess
	 * @throws RTSException
	 */
	private boolean releaseInvList(
		Vector avInvList,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		System.out.println("releaseInvList");
		InventoryServerBusiness laInventoryServerBusiness =
			new InventoryServerBusiness();

		if (avInvList != null)
		{
			for (int i = 0; i < avInvList.size(); i++)
			{
				ProcessInventoryData laReleaseInv =
					(ProcessInventoryData) avInvList.get(i);

				System.out.println(laReleaseInv.getInvItmNo());

				laReleaseInv.setInvStatusCd(0);
				Vector lvIn = new Vector();
				lvIn.addElement(aaDBA);
				lvIn.addElement(laReleaseInv);
				try
				{
					laInventoryServerBusiness.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant.UPDATE_INVENTORY_STATUS_CD,
						lvIn);
				}
				catch (RTSException aeRTSEx)
				{
					if (aeRTSEx.getCode() != 182)
					{
						throw aeRTSEx;
					}
				}
			}
		}
		return true;
	}
	//	/**
	//	 * Make use of Subcon client business process to process Subcon
	//	 * business methods on server side.
	//	 * 
	//	 * @param aiModule int 
	//	 * @param aiFunctionId int 
	//	 * @param aaSubconCache Object
	//	 * @throws RTSException 
	//	 */
	//	private Object subconServerProcess(
	//		int aiModule,
	//		int aiFunctionId,
	//		Object aaSubconCache)
	//		throws RTSException
	//	{
	//		SubcontractorRenewalClientBusiness laSubcontractorRenewalClientBusiness =
	//			new SubcontractorRenewalClientBusiness();
	//		return laSubcontractorRenewalClientBusiness.processData(
	//			aiModule,
	//			aiFunctionId,
	//			aaSubconCache);
	//	}
	/**
	 * Release plate inventory
	 * 
	 * @param aaProcInvData ProcessInventoryData  
	 * @param aaDBA DatabaseAccess
	 * @throws RTSException
	 */
	private void updateInvStatusCd(
		ProcessInventoryData laProcInvData,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		System.out.println("updateInvStatusCd");
		boolean lbSuccess = false;
		boolean lbContainsDB = false;

		if (aaDBA != null)
		{
			lbContainsDB = true;
		}
		else
		{
			aaDBA = new DatabaseAccess();
			aaDBA.beginTransaction();
		}
		try
		{
			InventoryServerBusiness laInventoryServerBusiness =
				new InventoryServerBusiness();
			Vector lvIn = new Vector();
			lvIn.addElement(aaDBA);
			lvIn.addElement(laProcInvData);
			System.out.println(laProcInvData.getInvItmNo()); 
			System.out.println(laProcInvData.getInvStatusCd()); 
			laInventoryServerBusiness.processData(
				GeneralConstant.INVENTORY,
				InventoryConstant.UPDATE_INVENTORY_STATUS_CD,
				lvIn);
			lbSuccess = true;
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() != 182)
				throw aeRTSEx;
		}
		finally
		{
			if (!lbContainsDB)
			{
				if (lbSuccess)
				{
					aaDBA.endTransaction(DatabaseAccess.COMMIT);
				}
				else
				{
					aaDBA.endTransaction(DatabaseAccess.ROLLBACK);
				}
			}
		}
	}
	/**
	 * Validate plate
	 * 
	 * @param aaSubconCacheData SubcontractorRenewalCacheData
	 * @throws RTSException
	 */
	private void validatePlt(SubcontractorRenewalCacheData aSubconCacheData)
		throws RTSException
	{
		System.out.println("validatePlt");
		SubcontractorRenewalData laSubconRenwlData =
			aSubconCacheData.getTempSubconRenewalData();
		ProcessInventoryData laProcessInventoryData = null;
		//construct processinventorydata
		ProcessInventoryData laProcInvDataInput =
			new ProcessInventoryData();

		laProcInvDataInput.setOfcIssuanceNo(
			aSubconCacheData
				.getSubcontractorHdrData()
				.getOfcIssuanceNo());
		laProcInvDataInput.setSubstaId(
			aSubconCacheData.getSubcontractorHdrData().getSubstaId());
		laProcInvDataInput.setItmCd(laSubconRenwlData.getPltItmCd());

		if (laSubconRenwlData.getRecordType()
			== SubcontractorRenewalData.PLT_STKR)
		{
			laProcInvDataInput.setInvItmYr(0);
		}
		else
		{
			laProcInvDataInput.setInvItmYr(
				laSubconRenwlData.getNewExpYr());
		}
		laProcInvDataInput.setInvItmNo(laSubconRenwlData.getNewPltNo());
		laProcInvDataInput.setInvItmEndNo(
			laProcInvDataInput.getInvItmNo());
		laProcInvDataInput.setInvQty(1);
		//check the database for the inventory
		InventoryServerBusiness laInventoryServerBusiness =
			new InventoryServerBusiness();
		Vector lvProcInvDataOutput =
			(Vector) laInventoryServerBusiness.processData(
				GeneralConstant.INVENTORY,
				InventoryConstant.VERIFY_INVENTORY_ITEM_IN_DB,
				laProcInvDataInput);
		if (lvProcInvDataOutput != null
			&& lvProcInvDataOutput.size() != 0)
		{
			laProcessInventoryData =
				(ProcessInventoryData) lvProcInvDataOutput.get(0);
		}
		//determine if needed to go to INV003
		//if already issued, then it is considered not found in inv_allocation table
		if (laProcessInventoryData != null
			&& laProcessInventoryData.isAlreadyIssued())
		{
			laProcessInventoryData = null;
		}
		boolean lbToINV003 = false;

		if (laProcessInventoryData == null)
		{
			lbToINV003 = true;
		}
		else
		{
			laProcessInventoryData.setInvItmNo(
				laProcInvDataInput.getInvItmNo());
			laProcessInventoryData.setInvItmEndNo(
				laProcInvDataInput.getInvItmNo());
			laProcessInventoryData.setInvQty(1);

			//check whether it is on hold 
			if (laProcessInventoryData.getInvStatusCd() != 0)
			{
				throw new RTSException(594);
			}
			if (laProcessInventoryData
				.getInvId()
				.equals(
					String.valueOf(
						aSubconCacheData
							.getSubcontractorHdrData()
							.getSubconId()))
				&& laProcessInventoryData.getInvLocIdCd().equals("S"))
			{
				//do not go to INV003
			}
			else
			{
				lbToINV003 = true;
				//set labels for INV003
				if (laProcessInventoryData
					.getInvLocIdCd()
					.equals(CENTRAL_LOC_CD))
				{
					aSubconCacheData.setINV003AllocatedName(
						CENTRAL_NAME);
				}
				else if (
					laProcessInventoryData.getInvLocIdCd().equals(
						WORKSTATION_LOC_CD))
				{
					aSubconCacheData.setINV003AllocatedName(
						WORKSTATION_NAME
							+ " "
							+ laProcessInventoryData.getInvId());
				}
				else if (
					laProcessInventoryData.getInvLocIdCd().equals(
						DEALER_LOC_ID))
				{
					aSubconCacheData.setINV003AllocatedName(
						DEALER_NAME
							+ " "
							+ laProcessInventoryData.getInvId());
				}
				else if (
					laProcessInventoryData.getInvLocIdCd().equals(
						EMPLOYEE_LOC_CD))
				{
					aSubconCacheData.setINV003AllocatedName(
						EMPLOYEE_NAME
							+ " "
							+ laProcessInventoryData.getInvId());
				}
				else if (
					laProcessInventoryData.getInvLocIdCd().equals(
						SUBCON_LOC_ID_CD))
				{
					aSubconCacheData.setINV003AllocatedName(
						SUBCON_NAME
							+ " "
							+ laProcessInventoryData.getInvId());
				}
				if (laProcessInventoryData.getInvLocIdCd().equals("V"))
				{
					aSubconCacheData.setINV003Voided(true);
				}
				else
				{
					aSubconCacheData.setINV003Voided(false);
				}
			}
		}
		//hold inventory
		if (laProcessInventoryData != null)
		{
			laProcessInventoryData.setInvStatusCd(2);
			updateInvStatusCd(laProcessInventoryData, null);
		}
		else
		{
			//going to INV003, set dummy held inventory
			laProcessInventoryData = new ProcessInventoryData();
			laProcessInventoryData.setInvId("0");
			laProcessInventoryData.setInvQty(1);
			laProcessInventoryData.setInvLocIdCd("U");
			laProcessInventoryData.setInvItmNo(
				laProcInvDataInput.getInvItmNo());
			laProcessInventoryData.setInvItmEndNo(
				laProcInvDataInput.getInvItmNo());
			laProcessInventoryData.setOfcIssuanceNo(
				laProcInvDataInput.getOfcIssuanceNo());
			laProcessInventoryData.setSubstaId(
				laProcInvDataInput.getSubstaId());
			laProcessInventoryData.setItmCd(
				laProcInvDataInput.getItmCd());
			laProcessInventoryData.setInvItmYr(
				laProcInvDataInput.getInvItmYr());
		}
		if (lbToINV003)
		{
			aSubconCacheData.setINV003ProcessInventoryData(
				laProcessInventoryData);
			return;
		}
		else
		{
			laSubconRenwlData.setProcInvPlt(laProcessInventoryData);
		}
	}
}
