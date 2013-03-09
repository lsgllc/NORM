package com.txdot.isd.rts.server.misc;

import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.dataaccess.MfAccess;
import com.txdot.isd.rts.server.db.*;

/*
 *
 * MiscServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/12/2002	add InvItmEndNo to ProcessInventory
 *							vector in completePendingTransaction()
 *							defect 3465
 * K Harrell  	10/16/2002  Set TransTimestmp = null in qryVoidTransHdr
 *							defect 4901 
 * Min Wang		10/07/2002	Modified ProcessData() and 
 * 							added getTransForVoidIndi() to retrieve 
 * 							transaction objects to apply void indi to.  
 * 							This makes setVoidIndi deprecated.
 *							defect 4746 
 * Ray Rowehl	02/03/2003	Add setup for clientHost to go to MFAccess
 *							defect 4588
 * K Harrell	01/23/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add updateReprintSticker()
 *				 			Ver 5.2.0	
 * K Harrell	04/19/2004	Assign values to CompleteTransactionData for 
 *							original transaction
 *							modify updateReprintSticker()
 *							defect 7076 Ver 5.2.0 
 * K Harrell	04/20/2004	Add call to ReprintSticker.insReprintSticker
 *							modify updateReprintSticker()
 *							defect 7020 Ver 5.2.0
 * K Harrell	05/05/2004	Do not update PrntInvQty when !printableindi
 *							modify updateReprintSticker()
 *							defect 7070 Ver 5.2.0
 * K Harrell	06/30/2004	Issue Rollback on DB error
 *							modify  updateReprintSticker()
 *							defect 7217 Ver 5.2.0
 * K Harrell	07/02/2004	Check for ReprintStickerTransData for
 *							Reprint Sticker request
 *							modify updateReprintSticker()
 *							defect 7284  Ver 5.2.0
 * K Harrell	07/31/2004	Do not generate RPRSTK transaction if
 *							prntinvqty = 0
 *							modify updateReprintSticker()
 *							defect 7410  Ver 5.2.1
 * K Harrell	10/13/2004	Determine if SBRNW via FeeSourceCd
 *							for Pending Transaction
 *							modify getPendingTransactions()
 *							defect 7620  Ver 5.2.1 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7892 Ver 5.2.3
 * K Harrell	06/19/2005	Java 1.4 Work 
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/25/2005	Correct OriginCd for insert on
 *							RTS_REPRNT_STKR for DTA/SBRNW
 *							modify updateReprintSticker() 
 *							defect 8296 Ver 5.2.2 Fix 6
 * K Harrell	08/23/2005	Include HCKITM in TransCds which generate
 * 							VOIDNC.
 * 							modify voidTransaction()  
 * 							defect 8345 Ver 5.2.3
 * K Harrell	02/25/2007	Modify to accommodate Special Plates in 
 * 							void
 * 							add setSpclPltRegisData()
 * 							modify getMVFData()   
 * 							defect 9085 Ver Special Plates
 * B Hargrove	04/04/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify setRegistrationData()
 * 							defect 9631 Ver Defect POS A
 * K Harrell	01/07/2009  Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *							modify setSpclPltRegisData()  
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	03/03/2009	Modify to use Lienholder Address Data. Remove
 * 							use of parameters where not required.  
 * 							modify setTitleData()
 * 							defect 9969 Ver Defect_POS_E 
 * K Harrell	07/03/2009	Implement new OwnerData in MVFuncTrans, 
 * 							 SRFuncTrans
 * 							delete setOwnerData(), setSpclPltRegisData(),
 * 							 setRegistrationData(),setSalvageData(), 
 * 							 setTitleData(), setVehicleData(),   
 * 							 createTransId(),
 * 							modify completePendingTransaction(),  
 * 							 getMVFData(), getVoidTransaction()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	06/14/2010	add getPrmtData()
 * 							modify getVoidRTSTrans(), processData()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/26/2010 	add logic to also retrieve VI for Permit
 * 							modify getPrmtData()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	06/30/2010	VoidedTransIndi now in RTS_PRMT_TRANS; added
 * 							 logic to retrieve/support
 * 							modify getTransForVoidIndi()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/18/2010	Accommodate Reprint Permit;  Cleanup.  
 * 							modify updateReprintSticker() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	01/29/1011	Assign boolean to denote that WebAgent Trans
 * 							modify getPendingTransactions() 
 * 							defect 10734 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */
/**
 * Class will handle all of the server processing for any event in 
 * Miscellaneous
 *
 * @version	6.7.0			01/29/1011
 * @author 	Bobby Tulsiani
 * <br>Creation Date: 		10/22/2001	17:02:06
 */
public class MiscServerBusiness
{
	private String csClientHost = "Unknown";

	/**
	 * MiscServerBusiness constructor comment.
	 */
	public MiscServerBusiness()
	{
		super();
	}

	/**
	 * MiscServerBusiness constructor comment.
	 * 
	 * @param asClientHost String 
	 */
	public MiscServerBusiness(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
	}

	/**
	 * Method checks if transaction has posted to the mainframe, and 
	 * therefore can not be voided
	 *
	 * @param aaData Object 
	 * @return Object
	 * @throws RTSException
	 */
	public Object checkMFTrans(Object aaData) throws RTSException
	{
		GeneralSearchData laGeneralSearchData =
			(GeneralSearchData) aaData;
		MfAccess laMFA = new MfAccess(csClientHost);

		// Return exception if transactions not present on mainframe
		try
		{
			int liTransPresentOnMF =
				laMFA.voidTransactions(laGeneralSearchData);
			if (liTransPresentOnMF == 0)
			{
				throw new RTSException(330);
			}
		}
		//Catch Mainframe down scenario
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getMsgType().equals(RTSException.MF_DOWN))
			{
				throw new RTSException(331);
			}
			throw aeRTSEx;
		}
		return null;
	}

	/**
	 * Method uses transaction header data to query the other transaction 
	 * tables and return a CompleteVehicleTransaction data object to the
	 * pending transaction screen ready to be completed
	 *
	 * @param aaData Object 
	 * @return Object
	 * @throws RTSException
	 */
	public Object completePendingTransaction(Object aaData)
		throws RTSException
	{
		TransactionHeaderData laTransactionHeaderData =
			(TransactionHeaderData) aaData;
		//Create new objects for data
		Vector lvTransactions = new Vector();
		Vector lvFundsDetails = new Vector();
		CompleteVehicleTransactionData laCVTData =
			new CompleteVehicleTransactionData();
		Vector lvRegFeesData = new Vector();
		Vector lvProcessInventoryData = null;
		Vector lvTrInvDetailData = null;
		Vector lvTransIdList = new Vector();

		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();

			// RTS_TRANS 
			Transaction laDbData1 = new Transaction(laDBA);
			TransactionData laQryObj = new TransactionData();
			laQryObj.setOfcIssuanceNo(
				laTransactionHeaderData.getOfcIssuanceNo());
			laQryObj.setSubstaId(laTransactionHeaderData.getSubstaId());
			laQryObj.setTransAMDate(
				laTransactionHeaderData.getTransAMDate());
			laQryObj.setTransWsId(
				laTransactionHeaderData.getTransWsId());
			laQryObj.setCustSeqNo(
				laTransactionHeaderData.getCustSeqNo());
			laQryObj.setTransTime(Integer.MIN_VALUE);
			lvTransactions = laDbData1.qryTransaction(laQryObj);

			//For each transaction returned, retrieve associated data
			for (int i = 0; i < lvTransactions.size(); i++)
			{
				TransactionData laTransactionData =
					(TransactionData) lvTransactions.get(i);

				// get transids info
				// defect 10112 
				lvTransIdList.addElement(
					UtilityMethods.getTransId(
						laTransactionData.getOfcIssuanceNo(),
						laTransactionData.getTransWsId(),
						laTransactionData.getTransAMDate(),
						laTransactionData.getTransTime()));
				// end defect 10112 

				RegFeesData laRegFeesData = new RegFeesData();

				//Query Trans Funds table data
				TransactionFundsDetail laDbData2 =
					new TransactionFundsDetail(laDBA);
				TransactionFundsDetailData laQryObj2 =
					new TransactionFundsDetailData();
				laQryObj2.setOfcIssuanceNo(
					laTransactionData.getOfcIssuanceNo());
				laQryObj2.setSubstaId(laTransactionData.getSubstaId());
				laQryObj2.setTransAMDate(
					laTransactionData.getTransAMDate());
				laQryObj2.setTransWsId(
					laTransactionData.getTransWsId());
				laQryObj2.setCustSeqNo(
					laTransactionData.getCustSeqNo());
				laQryObj2.setTransTime(
					laTransactionData.getTransTime());
				lvFundsDetails =
					laDbData2.qryTransactionFundsDetailForComplete(
						laQryObj2);
				Vector lvFeesData = new java.util.Vector();

				//For each assoicated funds data, set fees data
				for (int j = 0; j < lvFundsDetails.size(); j++)
				{
					TransactionFundsDetailData laTransactionFundsDetailData =
						(
							TransactionFundsDetailData) lvFundsDetails
								.get(
							j);
					FeesData laFees = new FeesData();
					laFees.setAcctItmCd(
						laTransactionFundsDetailData.getAcctItmCd());
					laFees.setItemPrice(
						laTransactionFundsDetailData.getItmPrice());
					laFees.setItmQty(
						laTransactionFundsDetailData.getItmQty());
					laFees.setDesc(
						laTransactionFundsDetailData
							.getAcctItmCdDesc());
					laFees.setCrdtAllowedIndi(
						laTransactionFundsDetailData
							.getCrdtAllowdIndi());
					lvFeesData.addElement(laFees);
				}
				laRegFeesData.setVectFees(lvFeesData);
				laRegFeesData.setTransCd(
					laTransactionData.getTransCd());
				lvRegFeesData.add(laRegFeesData);

				//Set Inventory only once for each transaction
				if (i == 0)
				{
					TransactionInventoryDetail laTransactionInventoryDetail =
						new TransactionInventoryDetail(laDBA);
					TransactionInventoryDetailData laTransactionInventoryDetailData =
						new TransactionInventoryDetailData();
					laTransactionInventoryDetailData.setOfcIssuanceNo(
						laTransactionData.getOfcIssuanceNo());
					laTransactionInventoryDetailData.setSubstaId(
						laTransactionData.getSubstaId());
					laTransactionInventoryDetailData.setTransWsId(
						laTransactionData.getTransWsId());
					laTransactionInventoryDetailData.setTransAMDate(
						laTransactionData.getTransAMDate());
					laTransactionInventoryDetailData.setCustSeqNo(
						laTransactionData.getCustSeqNo());
					//Query the Trans Inv data 
					lvTrInvDetailData =
						laTransactionInventoryDetail
							.qryTransactionInventoryDetailSet(
							laTransactionInventoryDetailData);
				}
			}
			laCVTData.setRegFeesData(lvRegFeesData);
			laCVTData.setTransactionHeader(laTransactionHeaderData);

			//convert laTransactionInventoryDetailData to ProcessInventoryData
			if (lvTrInvDetailData != null
				&& lvTrInvDetailData.size() > 0)
			{
				lvProcessInventoryData = new Vector();
				for (int i = 0; i < lvTrInvDetailData.size(); i++)
				{
					TransactionInventoryDetailData laTransactionInventoryDetailData =
						(
							TransactionInventoryDetailData) lvTrInvDetailData
								.get(
							i);
					ProcessInventoryData laProcessInventoryData =
						new ProcessInventoryData();
					laProcessInventoryData.setOfcIssuanceNo(
						laTransactionInventoryDetailData
							.getOfcIssuanceNo());
					laProcessInventoryData.setSubstaId(
						laTransactionInventoryDetailData.getSubstaId());
					laProcessInventoryData.setItmCd(
						laTransactionInventoryDetailData.getItmCd());
					laProcessInventoryData.setInvItmYr(
						laTransactionInventoryDetailData.getInvItmYr());
					laProcessInventoryData.setInvItmNo(
						laTransactionInventoryDetailData.getInvItmNo());
					laProcessInventoryData.setInvItmEndNo(
						laTransactionInventoryDetailData.getInvItmNo());
					laProcessInventoryData.setInvQty(1);
					lvProcessInventoryData.addElement(
						laProcessInventoryData);
				}
				laCVTData.setProcessInventoryData(
					lvProcessInventoryData);
			}
			//set Multiple Trans indi
			if (lvTransactions != null && lvTransactions.size() > 1)
			{
				laCVTData.setMultipleTrans(true);
			}
			//set Transid List
			laCVTData.setTransIdList(lvTransIdList);
			return laCVTData;
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
		}
	}

	/**
	 * Method retrieves MVFData for Voiding Internet transactions.  Uses set
	 * methods to set the objects within it.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object getMVFData(Object aaData) throws RTSException
	{

		//Extract VoidTransactionData passed to method
		VoidTransactionData laVoidTransData =
			(VoidTransactionData) aaData;

		//Create new objects to store data
		MFVehicleData laMFVData = new MFVehicleData();

		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			MotorVehicleFunctionTransaction laMotorVehicleFunctionTransaction =
				new MotorVehicleFunctionTransaction(laDBA);
			MotorVehicleFunctionTransactionData laMVFuncTransData =
				new MotorVehicleFunctionTransactionData();
			laMVFuncTransData.setOfcIssuanceNo(
				laVoidTransData.getOfcIssuanceNo());
			laMVFuncTransData.setSubstaId(
				laVoidTransData.getSubstaId());
			laMVFuncTransData.setTransAMDate(
				laVoidTransData.getTransAMDate());
			laMVFuncTransData.setTransWsId(
				laVoidTransData.getTransWsId());
			laMVFuncTransData.setCustSeqNo(
				laVoidTransData.getCustSeqNo());
			laMVFuncTransData.setTransTime(
				laVoidTransData.getTransTime());
			laMVFuncTransData.setTransCd(laVoidTransData.getTransCd());

			//Query MVF Transacation Table and store data in vector
			Vector lvMVFuncTransData =
				laMotorVehicleFunctionTransaction
					.qryMotorVehicleFunctionTransaction(
					laMVFuncTransData);

			//Set Data in child objects of MVF Transaction	
			if (lvMVFuncTransData.size() != 0)
			{
				MotorVehicleFunctionTransactionData laMVFTData =
					(
						MotorVehicleFunctionTransactionData) lvMVFuncTransData
							.get(
						0);

				// defect 10112
				laMFVData = laMVFTData.getMFVehicleData();
				// end defect 10112 
			}
			else
			{
				laMFVData = new MFVehicleData(true);
			}

			// defect 9085 
			SpecialRegistrationFunctionTransaction laSRFuncTrans =
				new SpecialRegistrationFunctionTransaction(laDBA);

			SpecialRegistrationFunctionTransactionData laSRFuncTransData =
				new SpecialRegistrationFunctionTransactionData();

			laSRFuncTransData.setOfcIssuanceNo(
				laVoidTransData.getOfcIssuanceNo());
			laSRFuncTransData.setSubstaId(
				laVoidTransData.getSubstaId());
			laSRFuncTransData.setTransAMDate(
				laVoidTransData.getTransAMDate());
			laSRFuncTransData.setTransWsId(
				laVoidTransData.getTransWsId());
			laSRFuncTransData.setCustSeqNo(
				laVoidTransData.getCustSeqNo());
			laSRFuncTransData.setTransTime(
				laVoidTransData.getTransTime());
			laSRFuncTransData.setTransCd(laVoidTransData.getTransCd());

			// Query SR_Func_Trans and store data in object 
			Vector lvSRFuncTransData =
				laSRFuncTrans
					.qrySpecialRegistrationFunctionTransaction(
					laSRFuncTransData);

			if (lvSRFuncTransData.size() != 0)
			{
				laSRFuncTransData =
					(
						SpecialRegistrationFunctionTransactionData) lvSRFuncTransData
							.elementAt(
						0);

				// defect 10112 
				laMFVData.setSpclPltRegisData(
					laSRFuncTransData.getSpclPltRegisData());
				// end defect 10112 
			}
			// end defect 9085 
			//Return MVF Trans Data
			return laMFVData;
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
		}
	}
	/**
	 * Method retrieves PermitData for Voiding Internet transactions. 
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object getPrmtData(Object aaData) throws RTSException
	{
		VoidTransactionData laVoidTransData =
			(VoidTransactionData) aaData;

		PermitData laPrmtData = null;

		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			PermitTransaction laPrmtTrans =
				new PermitTransaction(laDBA);

			PermitTransactionData laPrmtTransData =
				new PermitTransactionData();

			laPrmtTransData.setOfcIssuanceNo(
				laVoidTransData.getOfcIssuanceNo());
			laPrmtTransData.setSubstaId(laVoidTransData.getSubstaId());
			laPrmtTransData.setTransAMDate(
				laVoidTransData.getTransAMDate());
			laPrmtTransData.setTransWsId(
				laVoidTransData.getTransWsId());
			laPrmtTransData.setCustSeqNo(
				laVoidTransData.getCustSeqNo());
			laPrmtTransData.setTransTime(
				laVoidTransData.getTransTime());
			laPrmtTransData.setTransCd(laVoidTransData.getTransCd());

			Vector lvPrmtTransData =
				laPrmtTrans.qryPermitTransaction(laPrmtTransData);

			if (lvPrmtTransData.size() != 0)
			{
				laPrmtTransData =
					(PermitTransactionData) lvPrmtTransData.get(0);

				laPrmtData = laPrmtTransData.getPermitData();

				if (laPrmtData != null)
				{
					TransactionKey laTransKey =
						new TransactionKey(
							laPrmtTransData.getOfcIssuanceNo(),
							laPrmtTransData.getTransWsId(),
							laPrmtTransData.getTransAMDate(),
							laPrmtTransData.getTransTime());

					InventoryVirtual laInvVirt =
						new InventoryVirtual(laDBA);

					InventoryAllocationData laInvAllocData =
						laInvVirt.qryInventoryVirtualForTransId(
							laTransKey);

					if (laInvAllocData != null)
					{
						String lsInvItmNo =
							laInvAllocData.getInvItmNo();

						if (!UtilityMethods.isEmpty(lsInvItmNo)
							&& lsInvItmNo.equals(laPrmtData.getPrmtNo()))
						{
							laPrmtData.setVIAllocData(laInvAllocData);
						}
					}
				}
				laDBA.endTransaction(DatabaseAccess.COMMIT);
			}
			return laPrmtData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBA.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Method queries the the TransactionHeader table to retrieve all 
	 * pending transactions to be posted on CUS001.
	 *
	 * @param aaData Object 
	 * @return Object
	 * @throws RTSException
	 */
	public Object getPendingTransactions(Object aaData)
		throws RTSException
	{
		Vector lvPendingTransactions = new Vector();
		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			
			// defect 10734
			TransactionHeader laTransHdrSQL =
				new TransactionHeader(laDBA);
				
			TransactionHeaderData laTransHdrData =
				(TransactionHeaderData) aaData;
				
			lvPendingTransactions =
				laTransHdrSQL.qryIncompleteTransactionHeader(
					laTransHdrData);

			// For each Pending transaction, set boolean for 
			// SBRNW, WebAgent 
			if (lvPendingTransactions != null && lvPendingTransactions.size() > 0)
			{
				for (int i = 0; i < lvPendingTransactions.size(); i++)
				{
					laTransHdrData =
						(TransactionHeaderData) lvPendingTransactions.get(i);

					// If FeeSourceCd = 2, either SBRWN or WebAgent
					if (laTransHdrData.getFeeSourceCd()
						== FundsConstant.FEE_SOURCE_SUBCON)
					{
						if (laTransHdrData.getCustSeqNo()
							<= CommonConstant.MAX_POS_CUSTSEQNO)
						{
							laTransHdrData.setSubconTrans(true);
						}
						else
						{
							laTransHdrData.setWebAgntTrans(true);
						}
					}
				}
			}
			// end defect 10734 

			return lvPendingTransactions;
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
		}
	}

	/**
	 * Method gets Transaction objects that will have the VoidIndi set.
	 * Based partially on setVoidIndi().
	 * Returns the vector to client for processing.
	 *
	 * @param aaData Object 
	 * @return Object
	 * @throws RTSException
	 */
	public Object getTransForVoidIndi(Object aaData)
		throws RTSException
	{
		Vector lvTransToVoid = new Vector();
		Vector lvTrans = (Vector) aaData;
		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			Transaction laTrans = new Transaction(laDBA);

			//Create objects for each void table
			VoidTransactionData laVoidTransData = null;
			MotorVehicleFunctionTransactionData laMVFuncTransData =
				null;
			InventoryFunctionTransactionData laInvFuncTransData = null;
			FundFunctionTransactionData laFundFuncTransData = null;
			// defect 10491 
			PermitTransactionData laPrmtTransData = null;
			// end defect 10491  

			//For each trans passed, set the void indi
			for (int i = 0; i < lvTrans.size(); i++)
			{ //Get Trans table row to set VoidIndi on.
				laVoidTransData = (VoidTransactionData) lvTrans.get(i);
				TransactionData laTransData = new TransactionData();
				laTransData.setOfcIssuanceNo(
					laVoidTransData.getOfcIssuanceNo());
				laTransData.setSubstaId(laVoidTransData.getSubstaId());
				laTransData.setTransAMDate(
					laVoidTransData.getTransAMDate());
				laTransData.setTransWsId(
					laVoidTransData.getTransWsId());
				laTransData.setCustSeqNo(
					laVoidTransData.getCustSeqNo());
				laTransData.setTransTime(
					laVoidTransData.getTransTime());
				Vector lvTransaction =
					laTrans.qryTransaction(laTransData);
				for (int j1 = 0; j1 < lvTransaction.size(); j1++)
				{
					lvTransToVoid.add(lvTransaction.elementAt(j1));
				}
				// Get MVFTrans table row to set VoidIndi on
				laMVFuncTransData =
					new MotorVehicleFunctionTransactionData();
				laMVFuncTransData.setOfcIssuanceNo(
					laVoidTransData.getOfcIssuanceNo());
				laMVFuncTransData.setSubstaId(
					laVoidTransData.getSubstaId());
				laMVFuncTransData.setTransAMDate(
					laVoidTransData.getTransAMDate());
				laMVFuncTransData.setTransWsId(
					laVoidTransData.getTransWsId());
				laMVFuncTransData.setCustSeqNo(
					laVoidTransData.getCustSeqNo());
				laMVFuncTransData.setTransTime(
					laVoidTransData.getTransTime());
				MotorVehicleFunctionTransaction laMVFuncTrans =
					new MotorVehicleFunctionTransaction(laDBA);
				Vector lvMVFunc =
					laMVFuncTrans.qryMotorVehicleFunctionTransaction(
						laMVFuncTransData);
				if (lvMVFunc != null && lvMVFunc.size() > 0)
				{
					for (int j2 = 0; j2 < lvMVFunc.size(); j2++)
					{
						lvTransToVoid.add(lvMVFunc.elementAt(j2));
					}
				}
				//Set Void Indi for Inv Func Trans table
				laInvFuncTransData =
					new InventoryFunctionTransactionData();
				laInvFuncTransData.setOfcIssuanceNo(
					laVoidTransData.getOfcIssuanceNo());
				laInvFuncTransData.setSubstaId(
					laVoidTransData.getSubstaId());
				laInvFuncTransData.setTransAMDate(
					laVoidTransData.getTransAMDate());
				laInvFuncTransData.setTransWsId(
					laVoidTransData.getTransWsId());
				laInvFuncTransData.setCustSeqNo(
					laVoidTransData.getCustSeqNo());
				laInvFuncTransData.setTransTime(
					laVoidTransData.getTransTime());
				InventoryFunctionTransaction laInvFuncTrans =
					new InventoryFunctionTransaction(laDBA);
				Vector lvIFuncTrans =
					laInvFuncTrans.qryInventoryFunctionTransaction(
						laInvFuncTransData);
				if (lvIFuncTrans != null && lvIFuncTrans.size() > 0)
				{
					for (int j3 = 0; j3 < lvIFuncTrans.size(); j3++)
					{
						lvTransToVoid.add(lvIFuncTrans.elementAt(j3));
					}
				}
				//Set void Indi for Fund Func Trans table
				laFundFuncTransData = new FundFunctionTransactionData();
				laFundFuncTransData.setOfcIssuanceNo(
					laVoidTransData.getOfcIssuanceNo());
				laFundFuncTransData.setSubstaId(
					laVoidTransData.getSubstaId());
				laFundFuncTransData.setTransAMDate(
					laVoidTransData.getTransAMDate());
				laFundFuncTransData.setTransWsId(
					laVoidTransData.getTransWsId());
				laFundFuncTransData.setCustSeqNo(
					laVoidTransData.getCustSeqNo());
				laFundFuncTransData.setTransTime(
					laVoidTransData.getTransTime());
				FundFunctionTransaction laFuncFuncTrans =
					new FundFunctionTransaction(laDBA);
				Vector lvFuncTrans =
					laFuncFuncTrans.qryFundFunctionTransaction(
						laFundFuncTransData);
				if (lvFuncTrans != null && lvFuncTrans.size() > 0)
				{
					for (int j4 = 0; j4 < lvFuncTrans.size(); j4++)
					{
						lvTransToVoid.add(lvFuncTrans.elementAt(j4));
					}
				}
				// defect 10491 
				// Set void Indi for Permit Trans 
				laPrmtTransData = new PermitTransactionData();
				laPrmtTransData.setOfcIssuanceNo(
					laVoidTransData.getOfcIssuanceNo());
				laPrmtTransData.setSubstaId(
					laVoidTransData.getSubstaId());
				laPrmtTransData.setTransAMDate(
					laVoidTransData.getTransAMDate());
				laPrmtTransData.setTransWsId(
					laVoidTransData.getTransWsId());
				laPrmtTransData.setCustSeqNo(
					laVoidTransData.getCustSeqNo());
				laPrmtTransData.setTransTime(
					laVoidTransData.getTransTime());
				PermitTransaction laPrmtTrans =
					new PermitTransaction(laDBA);
				Vector lvPrmtTrans =
					laPrmtTrans.qryPermitTransaction(laPrmtTransData);
				if (lvPrmtTrans != null && lvPrmtTrans.size() > 0)
				{
					for (int j5 = 0; j5 < lvPrmtTrans.size(); j5++)
					{
						lvTransToVoid.add(lvPrmtTrans.elementAt(j5));
					}
				}
				// end defect 10491 
			}
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
		}
		return lvTransToVoid;
	}

	/**
	 * Query is called from Common Business, Transaction class.  The method
	 * returns the transaction table data of the record to be voided.
	 *
	 * @param aaData Object 
	 * @return Object
	 * @throws RTSException
	 */
	public Object getVoidRTSTrans(Object aaData) throws RTSException
	{
		TransactionData laTransData = (TransactionData) aaData;
		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			Transaction laTransaction = new Transaction(laDBA);
			Vector lvTransactionData =
				(Vector) laTransaction.qryTransaction(laTransData);
			if (lvTransactionData == null
				|| lvTransactionData.size() != 1)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					"lvTransactionData in getVoidRTSTrans is wrong",
					"ERROR");
			}
			laTransData = (TransactionData) lvTransactionData.get(0);

			// defect 10491
			if (UtilityMethods
				.isPermitApplication(laTransData.getTransCd()))
			{
				laTransData.setAddPrmtRecIndi(0);
				laTransData.setDelPrmtRecIndi(1);
			}
			// end defect 10491 

			return laTransData;
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
		}
	}

	/**
	 * Method queries the Transaction Header to get the Transactions 
	 * associated with the user specified TransId, and returns a list 
	 * to post on VOI002.
	 *
	 * @param aaData Object 
	 * @return Object
	 * @throws RTSException
	 */
	public Object getVoidTransaction(Object aaData) throws RTSException
	{
		GeneralSearchData laGenSearchData = (GeneralSearchData) aaData;
		String lsTransId = laGenSearchData.getKey1();
		Vector lvCustSeqNums = new Vector();
		Vector lvTransactions = new Vector();

		//Set values based on TransId
		int liOfcIssuanceNo =
			Integer.parseInt(lsTransId.substring(0, 3));
		int liTransWsId = Integer.parseInt(lsTransId.substring(3, 6));
		int liTransAMDate =
			Integer.parseInt(lsTransId.substring(6, 11));
		int liTransTime = Integer.parseInt(lsTransId.substring(11, 17));
		int liSubstaId = laGenSearchData.getIntKey1();
		int liCustSeqNo = 0;
		DatabaseAccess laDBA = null;
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();
			TransactionHeader laTransHdr = new TransactionHeader(laDBA);
			VoidTransactionData laQryObj = new VoidTransactionData();
			laQryObj.setOfcIssuanceNo(liOfcIssuanceNo);
			laQryObj.setTransWsId(liTransWsId);
			laQryObj.setTransAMDate(liTransAMDate);
			laQryObj.setTransTime(liTransTime);
			laQryObj.setSubstaId(liSubstaId);

			// Can not void another County's transactions.
			if (liOfcIssuanceNo != laGenSearchData.getIntKey2())
			{
				return lvTransactions;
			}
			// The number of transactions associated with the entered
			// trans id	
			lvCustSeqNums =
				laTransHdr.qryTransactionHeaderForVoid(laQryObj);

			//If no cust seq numbers returned
			if (lvCustSeqNums.size() == 0)
			{
				return lvTransactions;
			}
			for (int i = 0; i < lvCustSeqNums.size(); i++)
			{

				//Check that trans are on same subsation   
				if (((VoidTransactionData) lvCustSeqNums.get(i))
					.getSubstaId()
					!= laGenSearchData.getIntKey1())
				{
					return lvTransactions;
				}
				else
				{
					liCustSeqNo =
						((VoidTransactionData) lvCustSeqNums.get(i))
							.getCustSeqNo();
					laQryObj.setCustSeqNo(liCustSeqNo);
				}
			}
			//Set transtime to min value if not first trans of the day
			Transaction laTrans = new Transaction(laDBA);

			if (liCustSeqNo == 0)
			{
				laQryObj.setTransTime(liTransTime);
			}
			else
			{
				laQryObj.setTransTime(Integer.MIN_VALUE);
			}
			//Qry the transaction table for the void info	
			lvTransactions = laTrans.qryTransactionForVoid(laQryObj);

			// For each trans returned, create a transid to be displayed
			for (int i = 0; i < lvTransactions.size(); i++)
			{
				// defect 10112 
				VoidTransactionData laVoidData =
					(VoidTransactionData) lvTransactions.get(i);

				liOfcIssuanceNo = laVoidData.getOfcIssuanceNo();
				liTransWsId = laVoidData.getTransWsId();
				liTransAMDate = laVoidData.getTransAMDate();
				liTransTime = laVoidData.getTransTime();
				laVoidData.setTransactionId(
					UtilityMethods.getTransId(
						liOfcIssuanceNo,
						liTransWsId,
						liTransAMDate,
						liTransTime));
				// end defect 10112 

				//Query for inv data in each trans
				TransactionInventoryDetailData laTrInvDetailData =
					new TransactionInventoryDetailData();
				laTrInvDetailData.setOfcIssuanceNo(liOfcIssuanceNo);
				laTrInvDetailData.setTransWsId(liTransWsId);
				laTrInvDetailData.setTransAMDate(liTransAMDate);
				laTrInvDetailData.setTransTime(liTransTime);
				laTrInvDetailData.setSubstaId(
					((VoidTransactionData) lvTransactions.get(i))
						.getSubstaId());
				laTrInvDetailData.setCustSeqNo(
					((VoidTransactionData) lvTransactions.get(i))
						.getCustSeqNo());
				TransactionInventoryDetail laTrInvDetail =
					new TransactionInventoryDetail(laDBA);
				Vector lvHasInv =
					laTrInvDetail.qryTransactionInventoryDetail(
						laTrInvDetailData);
				if (lvHasInv != null && lvHasInv.size() > 0)
				{
					(
						(VoidTransactionData) lvTransactions.get(
							i)).setInventoryIndi(
						1);
				}
			}
			//Return the list of transactions
			return lvTransactions;
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
		}
	}

	/**
	 * Process data directs the server business method calls
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
		switch (aiFunctionId)
		{
			case MiscellaneousConstant.GET_PENDING_TRANSACTIONS :
				return getPendingTransactions(aaData);

			case MiscellaneousConstant.COMPLETE_PENDING_TRANS :
				return completePendingTransaction(aaData);

			case MiscellaneousConstant.GET_VOID_TRANSACTION :
				return getVoidTransaction(aaData);

			case MiscellaneousConstant.VOID_TRANSACTION :
				return voidTransaction(aaData);

			case MiscellaneousConstant.GET_VOID_RTS_TRANS :
				return getVoidRTSTrans(aaData);

			case MiscellaneousConstant.QRY_VOID_TRANS_HDR :
				return qryVoidTransHdr(aaData);

			case MiscellaneousConstant.GET_VOID_MVF_DATA :
				return getMVFData(aaData);

				// defect 10491 
			case MiscellaneousConstant.GET_VOID_PRMT_DATA :
				return getPrmtData(aaData);
				// end defect 10491 

			case MiscellaneousConstant.CHECK_MF_TRANS :
				return checkMFTrans(aaData);

			case MiscellaneousConstant.GET_TRANS_FOR_VOID_INDI :
				return getTransForVoidIndi(aaData);

			case MiscellaneousConstant.UPDATE_REPRINT_STICKER :
				return updateReprintSticker(aaData);

		}
		return null;
	}

	/**
	 * Method queries the transactino header table, and returns information
	 * to Misc.client.business
	 *
	 * @param aaData Object 
	 * @return Object
	 * @throws RTSException
	 */
	public Object qryVoidTransHdr(Object aaData) throws RTSException
	{
		VoidTransactionData laVoidTransactionData =
			(VoidTransactionData) aaData;
		DatabaseAccess laDBA = new DatabaseAccess();
		try
		{
			laDBA.beginTransaction();
			TransactionHeader laTransactionHeader =
				new TransactionHeader(laDBA);
			TransactionHeaderData laTransactionHeaderData =
				new TransactionHeaderData();
			TransactionHeaderData laTransactionHeaderDataReturn =
				new TransactionHeaderData();
			//Set info in data object
			laTransactionHeaderData.setOfcIssuanceNo(
				laVoidTransactionData.getOfcIssuanceNo());
			laTransactionHeaderData.setSubstaId(
				laVoidTransactionData.getSubstaId());
			laTransactionHeaderData.setTransAMDate(
				laVoidTransactionData.getTransAMDate());
			laTransactionHeaderData.setTransWsId(
				laVoidTransactionData.getTransWsId());
			laTransactionHeaderData.setCustSeqNo(
				laVoidTransactionData.getCustSeqNo());
			laTransactionHeaderData.setTransTime(
				laVoidTransactionData.getTransTime());
			Vector lvTransactionHeader =
				(Vector) laTransactionHeader.qryTransactionHeader(
					laTransactionHeaderData);
			//Return data
			if (lvTransactionHeader == null
				|| lvTransactionHeader.size() != 1)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					"lvTransactionData in getVoidRTSTrans is wrong",
					"ERROR");
			}
			laTransactionHeaderDataReturn =
				(TransactionHeaderData) lvTransactionHeader.get(0);
			// defect 4901
			// Set TransTimestmp to null prior to insert. 
			laTransactionHeaderDataReturn.setTransTimestmp(null);
			// end defect 4901 
			return laTransactionHeaderDataReturn;
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
		}
	}

	/**
	 * Update database for sticker reprint
	 *
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object updateReprintSticker(Object aaData)
		throws RTSException
	{
		DatabaseAccess laDBAccess = null;
		// defect 7217
		boolean lbSuccessful = false;
		// end defect 7217
		boolean lbPrinted = false;
		try
		{
			Log.write(
				Log.APPLICATION,
				this,
				"Starting DB call for Update - Reprint Sticker");
			laDBAccess = new DatabaseAccess();
			laDBAccess.beginTransaction();
			// defect 7284
			// Use ReprintStickerTransData vs. HashMap()
			ReprintStickerTransData laRprntStkrTransData =
				(ReprintStickerTransData) aaData;
			Map laMap =
				(Map) laRprntStkrTransData.getReprintStickerHashMap();
			// end defect 7284 
			ReceiptLogData laRcptLogData =
				(ReceiptLogData) laMap.get("DATA");
			int liOfc = ((Integer) laMap.get("OFC")).intValue();
			int liSub = ((Integer) laMap.get("SUB")).intValue();
			// defect 7076
			// insert transaction for RPRSTK 
			CompleteTransactionData laCompleteTransactionData =
				(CompleteTransactionData) laMap.get("CTDATA");
			TransactionHeaderData laTransactionHeaderData =
				(TransactionHeaderData) laMap.get("TRANSHDR");
			// end defect 7076
			TransactionInventoryDetail laTrInvDetail =
				new TransactionInventoryDetail(laDBAccess);
			TransactionInventoryDetailData laTrInvDetailData =
				new TransactionInventoryDetailData();
			Transaction laTrans = new Transaction(laDBAccess);

			// defect 10491 
			// Accommodate Reprint Permit 
			TransactionIdData laTransIdData =
				new TransactionIdData(laRcptLogData.getTransId());
			int liTransWsId = laTransIdData.getTransWsId();
			int liTransAMDate = laTransIdData.getTransAMDate();
			int liTransTime = laTransIdData.getTransTime();
			int liCustSeqNo = ((Integer) laMap.get("CSN")).intValue();

			TransactionData laTransData = new TransactionData();
			laTransData.setOfcIssuanceNo(liOfc);
			laTransData.setSubstaId(liSub);
			laTransData.setTransWsId(liTransWsId);
			laTransData.setTransAMDate(liTransAMDate);
			laTransData.setTransTime(liTransTime);
			laTransData.setCustSeqNo(liCustSeqNo);

			Vector lvTrans = laTrans.qryTransaction(laTransData);
			if (lvTrans == null || lvTrans.size() == 0)
			{
				return laMap;
			}

			laTrInvDetailData.setOfcIssuanceNo(liOfc);
			laTrInvDetailData.setSubstaId(liSub);
			laTrInvDetailData.setTransWsId(liTransWsId);
			laTrInvDetailData.setTransAMDate(liTransAMDate);
			laTrInvDetailData.setTransTime(liTransTime);
			laTrInvDetailData.setCustSeqNo(liCustSeqNo);

			// Query Inventory Details for Receipt 
			Vector lvVector =
				laTrInvDetail.qryTransactionInventoryDetail(
					laTrInvDetailData);
			if (lvVector == null || lvVector.size() == 0)
			{
				return laMap;
			}

			boolean lbPrmtTrans = false;
			// For every inventory item 
			TransactionInventoryDetailData laTrInvData = null;
			// defect 7070
			// do not update prntinvqty if not printable
			ItemCodesData laItemData = null;
			for (int i = 0; i < lvVector.size(); i++)
			{
				laTrInvData =
					(TransactionInventoryDetailData) lvVector.get(i);
				laItemData =
					ItemCodesCache.getItmCd(laTrInvData.getItmCd());
				String lsTransCd = laTrInvData.getTransCd();
				lbPrmtTrans = UtilityMethods.printsPermit(lsTransCd);

				// Can only have one printable item per transaction	
				if (laItemData.isPrintable() || lbPrmtTrans)
				{
					// With DTA/Subcon, can have non-printed items 
					if (laTrInvData.getReprntCount() != 0)
					{
						lbPrinted = true;
					}
					break;
				}
			}
			//if (itemData == null || itemData.isPrintable() == false )
			if (lbPrinted == false)
			{
				return laMap;
			}
			// end defect 7070 
			else
			{
				// defect 7076
				// Assign values to CompleteTransactionData for original Transaction
				if (lbPrmtTrans)
				{
					laCompleteTransactionData.setTransCode(
						TransCdConstant.RPRPRM);
				}
				// end defect 10491 
				laCompleteTransactionData.setRprStkOfcIssuanceNo(liOfc);
				laCompleteTransactionData.setRprStkTransAMDate(
					liTransAMDate);
				laCompleteTransactionData.setRprStkTransWsId(
					liTransWsId);
				laCompleteTransactionData.setRprStkTransTime(
					liTransTime);
				// end defect 7076

				Vector lvIn = new Vector();
				lvIn.addElement(laTransactionHeaderData);
				lvIn.addElement(laCompleteTransactionData);
				lvIn.addElement(laDBAccess);
				/*
				 * Code to wait for 1 second between transactions.
				 * Without this pause, there is a duplicate key error.
				 */
				Thread.sleep(1000);
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
					CommonConstant.PROC_TRANS,
					lvIn);
				int liPrintNum = laTrInvData.getReprntCount();
				laTrInvData.setReprntCount(liPrintNum + 1);
				laTrInvDetail.updTransactionInventoryDetailReprint(
					laTrInvData);

				// defect 10491 
				if (lbPrmtTrans)
				{
					lbSuccessful = true;
					Log.write(
						Log.APPLICATION,
						this,
						"Successful DB call for Update - Reprint Permit");
				}
				else
				{
					// end defect 10491

					//
					// Setup ReprintData for Insert/Update RTS_REPRNT_STKR
					//
					// defect 7020
					// Update RTS_RPRNT_STKR online vs. Batch
					// 
					ReprintData laReprintData = new ReprintData();
					laReprintData.setOfcIssuanceNo(liOfc);
					laReprintData.setReprntDate(
						new RTSDate(RTSDate.AMDATE, liTransAMDate));

					// defect 8296
					// Use POS annotation for DTA/SUB events vs. RSPS 	
					if (laTrInvData.getTransCd().startsWith("DTA"))
					{
						// laReprintData.setOrigin(
						//	CommonConstant.REPORT_RSPS_DTA);
						laReprintData.setOrigin(
							CommonConstant.REPORT_POS_DTA);
					}
					else if (
						laTrInvData.getTransCd().startsWith("SBR"))
					{
						// laReprintData.setOrigin(
						//	CommonConstant.REPORT_RSPS_SUB);
						laReprintData.setOrigin(
							CommonConstant.REPORT_POS_SUB);
					}
					// end defect 8296
					else
					{
						laReprintData.setOrigin(CommonConstant.POS);
					}

					laReprintData.setItmCd(laTrInvData.getItmCd());
					laReprintData.setItmYr(laTrInvData.getInvItmYr());
					laReprintData.setPrntQty(1);
					ReprintSticker laRprSticker =
						new ReprintSticker(laDBAccess);
					Vector lvReprintData = new Vector();
					lvReprintData.add(laReprintData);
					laRprSticker.insReprintSticker(lvReprintData);
					// defect 7217 
					lbSuccessful = true;
					// end defect 7217 
					// end defect 7020 
					Log.write(
						Log.APPLICATION,
						this,
						"Successful DB call for Update - Reprint Sticker");
					// defect 10491 
				}
				// end defect 10491 
				return laMap;
			}
		}
		catch (RTSException aeRTSEx)
		{
			// defect 10491 
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call for Update - Reprint Sticker/Permit");
			// end defect 10491 
			throw aeRTSEx;
		}
		catch (InterruptedException aeIEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeIEx);
			throw leRTSEx;
		}
		finally
		{
			// defect 7217 
			if (lbSuccessful)
			{
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			}
			else
			{
				laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			}
			// end defect 7217 
		}
	}

	/**
	 * Method creates the Complete Transaction data that is returned to 
	 * the void process
	 *
	 * @param aaData Object 
	 * @return Object
	 * @throws RTSException
	 */
	public Object voidTransaction(Object aaData) throws RTSException
	{
		Vector lvTransactions = (Vector) aaData;
		VoidTransactionData laVTData =
			(VoidTransactionData) lvTransactions.get(0);
		com.txdot.isd.rts.services.data.CompleteTransactionData CTData =
			new com
				.txdot
				.isd
				.rts
				.services
				.data
				.CompleteTransactionData();
		DatabaseAccess laDBA = null;
		Dollar laTotalVoidAmt = new Dollar("0.00");
		try
		{
			laDBA = new DatabaseAccess();
			laDBA.beginTransaction();

			// For each transaction to void, qry Funds Details, and 
			// set void amounts	
			for (int i = 0; i < lvTransactions.size(); i++)
			{
				VoidTransactionData laVoidTransData =
					(VoidTransactionData) lvTransactions.get(i);
				TransactionFundsDetailData laTransFundsDetailData =
					new TransactionFundsDetailData();
				TransactionFundsDetail laTransFundsDetail =
					new TransactionFundsDetail(laDBA);
				laTransFundsDetailData.setOfcIssuanceNo(
					laVoidTransData.getOfcIssuanceNo());
				laTransFundsDetailData.setSubstaId(
					laVoidTransData.getSubstaId());
				laTransFundsDetailData.setTransAMDate(
					laVoidTransData.getTransAMDate());
				laTransFundsDetailData.setTransWsId(
					laVoidTransData.getTransWsId());
				laTransFundsDetailData.setCustSeqNo(
					laVoidTransData.getCustSeqNo());
				laTransFundsDetailData.setTransTime(
					laVoidTransData.getTransTime());
				Vector lvTransFundsDetail =
					laTransFundsDetail.qryTransactionFundsDetail(
						laTransFundsDetailData);
				if (lvTransFundsDetail != null
					&& lvTransactions.size() != 0)
				{
					for (int j = 0; j < lvTransFundsDetail.size(); j++)
					{
						TransactionFundsDetailData laTFDData =
							(
								TransactionFundsDetailData) lvTransFundsDetail
									.get(
								j);
						laTotalVoidAmt =
							laTotalVoidAmt.add(laTFDData.getItmPrice());
					}
				}
			}
			//Set void data
			CTData.setVoidOfcIssuanceNo(laVTData.getOfcIssuanceNo());
			CTData.setVoidSubstaId(laVTData.getSubstaId());
			CTData.setVoidTransAMDate(laVTData.getTransAMDate());
			CTData.setVoidTransWsId(laVTData.getTransWsId());
			CTData.setVoidCustSeqNo(laVTData.getCustSeqNo());
			CTData.setVoidTransTime(laVTData.getTransTime());
			CTData.setVoidTransCd(laVTData.getTransCd());

			// defect 8345
			// Include HCKITM for VOIDNC 
			//Set Void Trans Cd to special indicator if criteria met
			if (laVTData.getTransCd().equals(TransCdConstant.HOTCK)
				|| laVTData.getTransCd().equals(TransCdConstant.HOTDED)
				|| laVTData.getTransCd().equals(TransCdConstant.REFUND)
				|| laVTData.getTransCd().equals(TransCdConstant.HCKITM))
			{
				CTData.setTransCode(TransCdConstant.VOIDNC);
			}
			else
			{
				CTData.setTransCode(TransCdConstant.VOID);
			}
			// end defect 8345

			CTData.setVoidPymntAmt(laTotalVoidAmt);
			//Return all gathered info
			return CTData;
		}
		finally
		{
			laDBA.endTransaction(DatabaseAccess.NONE);
		}
	}
}
