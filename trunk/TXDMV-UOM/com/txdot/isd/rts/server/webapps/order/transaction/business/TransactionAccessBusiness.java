package com.txdot.isd.rts.server.webapps.order.transaction.business;

import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.services.webapps.util.EmailSpecialPlateReceipt;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.ServiceConstants;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetSpecialPlateTransaction;
import com.txdot.isd.rts.server.db.InternetSpecialPlateTransactionFees;
import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;
import com.txdot.isd.rts.server.webapps.order.common.business.AbstractBusiness;
import com.txdot.isd.rts.server.webapps.order.common.business.CommonBusiness;
import com.txdot.isd.rts.server.webapps.order.common.data.Address;
import com.txdot.isd.rts.server.webapps.order.common.data.DefaultResponse;
import com.txdot.isd.rts.server.webapps.order.common.data.Errors;
import com.txdot.isd.rts.server.webapps.order.common.data.Fees;
import com.txdot.isd.rts.server.webapps.order.transaction.data.TransactionRequest;

/*
 * TransactionAccessBusiness.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/06/2007	Created Class.
 * 							defect 9120 Ver Special Plates
 * Jeff S.		06/26/2007	Created a new method used to confirm the 
 * 							inventory.  Also added error handling so 
 * 							that we can log the RTSException at TXO.
 * 							defect 9121 Ver Special Plates
 * K Harrell	07/12/2009	Implement new OwnerData 
 * 							modify convertToSPTransData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	02/11/2010	Implement insSPItrntTransFees
 * 							modify insertTransaction()
 * 							defect 10366 Ver POS_640 
 * B Brown		04/22/2010	add a 1 second delay in the update, which
 * 							inserts into the trans tables, to avoid
 * 							the duplicate row DB2 error.
 * 							Also, generate the email based on good
 * 							payment alone.
 * 							modify processData()
 * 							defect 10391 Ver POS_640
 * ---------------------------------------------------------------------
 */

/**
 * Business class used to handle the Special Plate Transactions.
 *
 * @version	POS_640			04/22/2010
 * @author	Jeff Seifert
 * <br>Creation Date:		03/06/2007 13:40:00
 */
public class TransactionAccessBusiness extends AbstractBusiness
{
	private Vector cvTransDataObjs = new Vector();

	/**
	 * Converts the TransactionRequest to a SpecialPlateItrntTransData
	 * Data Object.
	 * 
	 * @param aaTransReq TransactionRequest
	 * @return SpecialPlateItrntTransData
	 */
	public SpecialPlateItrntTransData convertToSPTransData(TransactionRequest aaTransReq)
	{
		SpecialPlateItrntTransData laSPTransData =
			new SpecialPlateItrntTransData();
		laSPTransData.setAddlSetIndi(aaTransReq.getAddlSetIndi());

		if (aaTransReq.getEpayRcveTimeStamp() != null)
		{
			laSPTransData.setEpayRcveTimeStmp(
				new RTSDate(aaTransReq.getEpayRcveTimeStamp()));
		}

		if (aaTransReq.getEpaySendTimeStamp() != null)
		{
			laSPTransData.setEpaySendTimeStmp(
				new RTSDate(aaTransReq.getEpaySendTimeStamp()));
		}

		laSPTransData.setISAIndi(aaTransReq.getIsaIndi());
		laSPTransData.setPLPIndi(aaTransReq.getPlpIndi());
		laSPTransData.setItrntPymntStatusCd(
			aaTransReq.getItrntPymntStatusCd());
		laSPTransData.setItrntTraceNo(aaTransReq.getItrntTraceNo());
		laSPTransData.setMfgPltNo(aaTransReq.getMfgPltNo());
		laSPTransData.setOrgNo(aaTransReq.getOrgNo());
		OwnerData laOwnerData = new OwnerData();
		// defect 10112 
		laOwnerData.setName1(aaTransReq.getPltOwnrName1());
		laOwnerData.setName2(aaTransReq.getPltOwnrName2());
		// end defect 10112 
		
		AddressData laAddressData = new AddressData();
		laAddressData.setSt1(aaTransReq.getAddress().getStreet1());
		laAddressData.setSt2(aaTransReq.getAddress().getStreet2());
		laAddressData.setCity(aaTransReq.getAddress().getCity());
		laAddressData.setState(aaTransReq.getAddress().getState());
		laAddressData.setZpcd(aaTransReq.getAddress().getZipCd());
		laAddressData.setZpcdp4(aaTransReq.getAddress().getZipCd4());
		// defect 10112 
		laOwnerData.setAddressData(laAddressData);
		// end defect 10112 
		
		laSPTransData.setOwnerData(laOwnerData);
		laSPTransData.setPltOwnrEmail(aaTransReq.getPltOwnrEmail());
		laSPTransData.setPltOwnrPhone(aaTransReq.getPhoneNo());
		laSPTransData.setPymntAmt(aaTransReq.getPymntAmt());
		laSPTransData.setPymntOrderId(aaTransReq.getPymntOrderID());
		laSPTransData.setRegPltCd(aaTransReq.getRegPltCd());
		laSPTransData.setRegPltNo(aaTransReq.getRegPltNo());
		laSPTransData.setReqIPAddr(aaTransReq.getReqIPAddr());
		laSPTransData.setResComptCntyNo(aaTransReq.getResComptCntyNo());
		// Add the fees.
		Fees[] larrFees = aaTransReq.getFees();
		if (larrFees != null)
		{
			for (int i = 0; i < larrFees.length; i++)
			{
				if (larrFees[i] != null)
				{
					if (larrFees[i].getItemPrice() == 0.00)
					{
						continue;
					}
					FeesData laSPFeesData = new FeesData();
					laSPFeesData.setItmQty(larrFees[i].getItmQty());
					laSPFeesData.setAcctItmCd(larrFees[i].getAcctItmCd());
					laSPFeesData.setItemPrice(
						new Dollar(larrFees[i].getItemPrice()));
					laSPFeesData.setDesc(larrFees[i].getDesc());
					laSPTransData.addFee(laSPFeesData);
				}
			}
		}

		return laSPTransData;
	}

	/**
	 * Inserts the Transaction into the RTS_ITRNT_SPAPP_TRANS Table.
	 * 
	 * @param request
	 * @return
	 */
	private DefaultResponse insertTransaction(TransactionRequest aaTransReq)
	{
		DefaultResponse laDefResp = new DefaultResponse();
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();

		try
		{
			System.out.println(
				"******************************************");
			System.out.println(
				"****************** INSERT TRANS **********");
			System.out.println(
				"******************************************");
			printTransData(aaTransReq);

			// Generate the Transaction Data Object
			SpecialPlateItrntTransData laSPTransData =
				convertToSPTransData(aaTransReq);

			// Get the Data Access Objects
			InternetSpecialPlateTransaction laIntrtTransAccess =
				new InternetSpecialPlateTransaction(laDatabaseAccess);
			InternetSpecialPlateTransactionFees laIntrtTransFeesAccess =
				new InternetSpecialPlateTransactionFees(laDatabaseAccess);

			// Begin the Unit of Work
			laDatabaseAccess.beginTransaction();
			// Update all of the Active Transaction Records to Inactive
			laIntrtTransAccess.updTransStatusCd(
				laSPTransData.getRegPltNo(),
				CommonConstants.TRANS_STATUS_ACTIVE,
				CommonConstants.TRANS_STATUS_INACTIVE);
			// Insert the Transaction Record
			
			// defect 10366
			// Use common method (between VPAPPx & IAPPL)
			// add 3 sets below for SPAPPL
			//laIntrtTransAccess.insActiveSPItrntTrans(laSPTransData);
			laSPTransData.setInitReqTimeStmp(new RTSDate());
			laSPTransData.setTransStatusCd(
				CommonConstants.TRANS_STATUS_ACTIVE);
			laSPTransData.setPltValidityTerm(1);
			laIntrtTransAccess.insSPItrntTrans(laSPTransData);
			// end defect 10366 
			
			// If there are fees.
			if (laSPTransData.getFeesData() != null
				&& laSPTransData.getFeesData().size() > 0)
			{
				// Get the Req Id
				int liReqPltNoReqId =
					laIntrtTransAccess.qryMaxReqPltNoReqId(
						laSPTransData.getRegPltNo());
				// Insert the Fees
				laIntrtTransFeesAccess.insSPItrntTransFees(
					liReqPltNoReqId,
					laSPTransData);
			}
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// End the Unit of Work

			// Mark as sucessfull.
			laDefResp.setAck(ServiceConstants.AR_ACK_SUCCESS);
		}
		catch (RTSException aeEx)
		{
			aeEx.printStackTrace();
			// Mark as unsucessfull.
			setError(laDefResp, aeEx);
			laDefResp.setAck(ServiceConstants.AR_ACK_FAILURE);
			try
			{
				laDatabaseAccess.endTransaction(
					DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeEx2)
			{
				aeEx2.printStackTrace();
			}
		}

		return laDefResp;
	}

	/**
	 * Prints the Trans data out to the console.
	 * 
	 * @param aaTransReq TransactionRequest
	 */
	private void printTransData(TransactionRequest aaTransReq)
	{
		System.out.println("Reg Plt No = " + aaTransReq.getRegPltNo());
		System.out.println("Reg Plt Cd = " + aaTransReq.getRegPltCd());
		System.out.println("Org No = " + aaTransReq.getOrgNo());
		System.out.println(
			"Res Comp County = " + aaTransReq.getResComptCntyNo());
		System.out.println("Name 1 = " + aaTransReq.getPltOwnrName1());
		System.out.println("Name 2 = " + aaTransReq.getPltOwnrName2());
		Address laAdd = aaTransReq.getAddress();
		if (laAdd != null)
		{
			System.out.println("Street1 = " + laAdd.getStreet1());
			System.out.println("Street2 = " + laAdd.getStreet2());
			String lsCitySateZip =
				laAdd.getCity()
					+ ", "
					+ laAdd.getState()
					+ " "
					+ laAdd.getZipCd();
			if (laAdd.getZipCd4() != null
				&& laAdd.getZipCd4().length() > 0)
			{
				lsCitySateZip += "-" + laAdd.getZipCd4();
			}
			System.out.println("CITY/STATE/ZIP = " + lsCitySateZip);
		}
		System.out.println("Phone No = " + aaTransReq.getPhoneNo());
		System.out.println("Email = " + aaTransReq.getPltOwnrEmail());
		System.out.println("MF Plt No = " + aaTransReq.getMfgPltNo());
		System.out.println(
			"ISA Indicator = " + (aaTransReq.getIsaIndi() != 0));
		System.out.println(
			"Additional Set Indicator = "
				+ (aaTransReq.getAddlSetIndi() != 0));
		System.out.println(
			"PLP Indicator = " + (aaTransReq.getPlpIndi() != 0));
		System.out.println(
			"Payment Amt. = " + aaTransReq.getPymntAmt());
		if (aaTransReq.getFees() != null)
		{
			System.out.println("Fees.:");
			for (int i = 0; i < aaTransReq.getFees().length; i++)
			{
				System.out.println(
					"Fee Desc. "
						+ (i + 1)
						+ " = "
						+ aaTransReq.getFees(i).getDesc());
				System.out.println(
					"Fee AcctItmCd"
						+ (i + 1)
						+ " = "
						+ aaTransReq.getFees(i).getAcctItmCd());
				System.out.println(
					"Fee Amount"
						+ (i + 1)
						+ " = "
						+ aaTransReq.getFees(i).getItemPrice());
			}
		}
		System.out.println(
			"Internet Trace No. = " + aaTransReq.getItrntTraceNo());
		System.out.println(
			"Payment Status Cd = "
				+ aaTransReq.getItrntPymntStatusCd());
		System.out.println(
			"Payment Order Id = " + aaTransReq.getPymntOrderID());
		System.out.println("IP Address = " + aaTransReq.getReqIPAddr());
		System.out.println(
			"Epay Send Timestamp = "
				+ aaTransReq.getEpaySendTimeStamp());
		System.out.println(
			"Epay Receive Timestamp = "
				+ aaTransReq.getEpayRcveTimeStamp());
		System.out.println(
			"**************************************************************");
	}
	/**
	 * Inherited method used to handle all of the functions within this
	 * module.
	 */
	public Object processData(Object aaObject)
	{
		// Reset the vector of trans objects
		cvTransDataObjs = new Vector();

		TransactionRequest[] larrTAReq =
			(TransactionRequest[]) aaObject;
		DefaultResponse[] larrDefResp =
			new DefaultResponse[larrTAReq.length];
		for (int i = 0; i < larrTAReq.length; i++)
		{
			larrDefResp[i] = new DefaultResponse();
			switch (larrTAReq[i].getAction())
			{
				case ServiceConstants.TA_ACTION_INSERT_TRANS :
					{
						larrDefResp[i] =
							insertTransaction(larrTAReq[i]);
						break;
					}
				case ServiceConstants.TA_ACTION_UPDATE_TRANS :
					{
						// defect 10391
						// sleep for 1 second between insert tries to 
						// avoid the 803 (duplicate row db error).
//						larrDefResp[i] =
//							updateTransaction(larrTAReq[i]);
						// updateTransaction(larrTAReq[i]) performs 2
						// units of work:
						// 1) confirms the plate inventory, then
						//    update rts_itrnt_spapp_trans.transstatuscd
						//    to pending if Epay payment was good
						// 2) postTrans (inserts into RTS trans tables, 
						//    complete inventory, and update 
						//    rts_itrnt_spapp_trans.transstatuscd
						//    to complete, audittrailtransid to current
						//    time     
						for (int x = 0; x < 30; x++)
						{
							try
							{
								Thread.sleep(1000);
							}
							catch (InterruptedException e)
							{
							}
							larrDefResp[i] =
								updateTransaction(larrTAReq[i]);

							if (larrDefResp[i].getAck()
								== ServiceConstants.AR_ACK_SUCCESS)
							{
								break;
							}
						}						
						// If this is the last one and the payment was
						// sucessfull 
						// CommonBusiness laComBus = new CommonBusiness();
						// move the email receipt below the for loop								
//						if (i == (larrTAReq.length - 1)
//							&& larrTAReq[i].getItrntPymntStatusCd()
//								== CommonConstants.PAYMENT_CAPTURE_SUCCESS
//							&& laComBus.isAllSuccessfull(larrDefResp))
						if (i == (larrTAReq.length - 1)
							&& larrTAReq[i].getItrntPymntStatusCd()
								== CommonConstants.PAYMENT_CAPTURE_SUCCESS)		
						{
						// end defect 10391
							EmailSpecialPlateReceipt laEmail =
								new EmailSpecialPlateReceipt();
							//laEmail.	
							if (!laEmail.emailReceipt(larrTAReq))
							{
								System.out.println(
									" email problem - msg not sent");
								//TODO Code error message here		
							}
						}
						break;
					}
				default :
					{
						larrDefResp[i].setAck(
							ServiceConstants
								.AR_ACK_FAILURE_WITH_WARNING);
						Errors laError = new Errors();
						laError.setErrorCode(99);
						larrDefResp[i].setErrors(laError);
						break;
					}
			}
		}
		return larrDefResp;
	}

	/**
	 * Confrims the inventory, updates the Internet Special Plate 
	 * Transaction Table and might create the POS transaction.
	 * 
	 * This could be used by either the webservice or MQ.
	 * 
	 * @param aaSPItrntTransData SpecialPlateItrntTransData
	 * @param abFromMQ boolean
	 * @param abCreatePOSTrans boolean
	 * @throws RTSException
	 */
	public void updateTransaction(
		SpecialPlateItrntTransData aaSPItrntTransData,
		boolean abFromMQ,
		boolean abCreatePOSTrans)
		throws Exception
	{
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();

		try
		{
			InternetSpecialPlateTransaction laIntrtTransAccess =
				new InternetSpecialPlateTransaction(laDatabaseAccess);

			// Begin the Unit of Work
			laDatabaseAccess.beginTransaction();
				
			// Set the update date and time to right now so that
			// we can use that for both inventory and transaction.
			aaSPItrntTransData.setUpdtTimeStmp(new RTSDate());
			
			// Good Payment
			if (aaSPItrntTransData.getItrntPymntStatusCd()
				== CommonConstants.PAYMENT_CAPTURE_SUCCESS)
			{
				// Confirm Inventory
				confirmInventory(laDatabaseAccess, aaSPItrntTransData);
				// Update the Trans table to Pending
				laIntrtTransAccess.updActiveSPItrntTrans(
					aaSPItrntTransData,
					CommonConstants.TRANS_STATUS_PENDING,
					abFromMQ);
			}
			// Bad Payment
			else
			{
				// Update the Trans table to Inactive
				laIntrtTransAccess.updActiveSPItrntTrans(
					aaSPItrntTransData,
					CommonConstants.TRANS_STATUS_INACTIVE,
					abFromMQ);
			}
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// End the Unit of Work
			
			// Good Payment and auto create POS trans.
			if (aaSPItrntTransData.getItrntPymntStatusCd()
				== CommonConstants.PAYMENT_CAPTURE_SUCCESS
				&& abCreatePOSTrans)
			{
				CommonServerBusiness laCommServerBuss =
					new CommonServerBusiness();
				// This will update the SP Internet trans table to 
				// completed and finalize inventory.
				laCommServerBuss.processData(
					GeneralConstant.COMMON,
					CommonConstant.PROC_IAPPL,
					aaSPItrntTransData);
			}
		}
		catch (RTSException aeEx)
		{
			try
			{
				laDatabaseAccess.endTransaction(
					DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx2)
			{
				aeRTSEx2.printStackTrace();
			}

			throw aeEx;
		}
	}
	
	/**
	 * When we get a good payment we need to mark the inventory so that
	 * no one else can get that inventory.  When the POS transaction is
	 * created the inventory will be finalized when end transaction
	 * is run.
	 * 
	 * @param aaDatabaseAccess DatabaseAccess
	 * @param aaSPItrntTransData SpecialPlateItrntTransData
	 * @throws RTSException
	 */
	private void confirmInventory(
		DatabaseAccess aaDatabaseAccess,
		SpecialPlateItrntTransData aaSPItrntTransData)
		throws RTSException
	{
		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();

		laInvAllocData.setItmCd(aaSPItrntTransData.getRegPltCd());
		laInvAllocData.setInvItmNo(aaSPItrntTransData.getRegPltNo());
		laInvAllocData.setInvQty(1);
		laInvAllocData.setInvItmEndNo(aaSPItrntTransData.getRegPltNo());
		laInvAllocData.setInvItmYr(0);
		laInvAllocData.setISA(
			aaSPItrntTransData.isISAIndi() == 1 ? true : false);
		laInvAllocData.setUserPltNo(
			aaSPItrntTransData.isPLPIndi() == 1 ? true : false);
		laInvAllocData.setMfgPltNo(aaSPItrntTransData.getMfgPltNo());
		laInvAllocData.setItrntReq(true);
		laInvAllocData.setOfcIssuanceNo(291);
		laInvAllocData.setTransWsId(999);
		laInvAllocData.setTransEmpId(TransCdConstant.IAPPL);
		laInvAllocData.setRequestorIpAddress(
			aaSPItrntTransData.getReqIPAddr());
		laInvAllocData.setRequestorRegPltNo(
			CommonConstant.STR_SPACE_EMPTY);
		laInvAllocData.setTransTime(
			aaSPItrntTransData.getUpdtTimeStmp().get24HrTime());
		InventoryServerBusiness laInvServBus =
			new InventoryServerBusiness();
		Vector lvInvBussData = new Vector();
		lvInvBussData.add(CommonConstant.ELEMENT_0, aaDatabaseAccess);
		lvInvBussData.add(CommonConstant.ELEMENT_1, laInvAllocData);
		laInvAllocData =
			(InventoryAllocationData) laInvServBus.processData(
				GeneralConstant.COMMON,
				InventoryConstant.INV_VI_UPDATE_TRANSTIME,
				lvInvBussData);

		if (laInvAllocData.getErrorCode() != 0)
		{
			throw new RTSException(laInvAllocData.getErrorCode());
		}
	}

	/**
	 * This is used by the webservice to process updates to the 
	 * Internet Special Plate Transaction Table.
	 * 
	 * @param aaTransReq TransactionRequest
	 * @return DefaultResponse
	 */
	private DefaultResponse updateTransaction(TransactionRequest aaTransReq)
	{
		DefaultResponse laDefResp = new DefaultResponse();
		try
		{
			System.out.println(
				"******************************************");
			System.out.println(
				"****************** UPDATE TRANS **********");
			System.out.println(
				"******************************************");
			printTransData(aaTransReq);

			// Actual update method.  Also used by MQ.  We will create a
			// POS transaction if the request says to.
			SpecialPlateItrntTransData laTransData =
				convertToSPTransData(aaTransReq);
			// Save the data object used to email the receipt.
			cvTransDataObjs.add(laTransData);
			// Actual Update.
			updateTransaction(
				laTransData,
				false,
				((aaTransReq.getCreatePOSTransIndi() == 1)
					? true
					: false));

			// Mark as sucessfull.
			laDefResp.setAck(ServiceConstants.AR_ACK_SUCCESS);
		}
		catch (Exception aeEx)
		{
			// Mark as unsucessfull.
			aeEx.printStackTrace();
			setError(laDefResp, aeEx);
			laDefResp.setAck(ServiceConstants.AR_ACK_FAILURE);
		}

		return laDefResp;
	}

}
