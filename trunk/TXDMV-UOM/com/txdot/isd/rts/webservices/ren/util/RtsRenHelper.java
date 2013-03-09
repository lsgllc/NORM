package com.txdot.isd.rts.webservices.ren.util;

import java.util.Vector;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;
import com.txdot.isd.rts.server.webapps.common.business.CommonEligibility;
import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.common.Fees;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.registration.GenRegistrationReceipts;
import com.txdot.isd.rts.services.util.JetPclProcess;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.webservices.ren.data.RTSFees;
import com.txdot.isd.rts.webservices.ren.data.RtsRegistrationData;
import com.txdot.isd.rts.webservices.ren.data.RtsRenewalRequest;
import com.txdot.isd.rts.webservices.ren.data.RtsRenewalResponse;

/*
 * RtsRenHelper.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/15/2010	Initial load.
 * 							Formulate methods to handle vehicle lookup
 * 							and verification.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	12/31/2010	Add Fees Component.  Dummy it in for now.
 * 							defect 10670 Ver 6.7.0
 * K Harrell	02/02/2011	add getWebAgncyTransReceipt() 
 * 							defect 10745 Ver 6.7.0 
 * Ray Rowehl	02/09/2011	Enhance extract data to use new vehicle 
 * 							info objects.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	03/09/2011	Removed receipt generation code from vehicle
 * 							look up section.  This really needs to go
 * 							somewhere else.
 * 							modify extractData()
 * 							defect 10670 Ver 6.7.1
 * Bob Brown	03/15/2011	Add code to return PDF receipt boolean
 * 							modify processDocNoRcpt()
 * 							defect 10670 Ver 6.7.1
 * K Harrell	04/22/2011	modify processGetWebAgncyTransReceipt()
 * 							defect 10769 Ver 6.7.1
 * K Harrell	04/24/2011	added constructors 
 * 							defect 10769 Ver 6.7.1  
 * K Harrell	04/28/2011	modify processGetWebAgncyTransReceipt() 
 * 							defect 10769 Ver 6.7.1 
 * K Harrell	05/10/2011	modify validateRecord() 
 * 							defect 10769 Ver 6.7.1 
 * K Harrell	05/11/2011	Check for MF Down
 * 							modify processGetVehReq()
 * 							defect 10768 Ver 6.7.1  
 * K Harrell	06/12/2011	Allow processing of No Vin Vehicles
 * 							modify validateRecord()
 * 							defect 10887 Ver 6.8.0 
 * K McKee      10/06/2011  Added logging for Generic Error 2300
 *                          defect 10729 Ver 6.9.0
 * K Harrell	11/05/2011	modify insertTrans(), 
 * 							 processGetWebAgncyTransReceipt() 
 * 							defect 11137 Ver 6.9.0 
 * K Harrell	11/08/2011	modify processGetWebAgncyTransReceipt()
 * 							defect 11149 Ver 6.9.0 
 * K Harrell	12/13/2011	Check any substaid where inventory is 
 * 							allocated to SubconId 
 * 							modify processGetWebAgncyTransReceipt()
 * 							defect 11178 Ver 6.9.0 
 * D Hamilton	12/22/2011	Include account item code in fee list.
 *                          modify assignFeesAndInsertWebAgncyTransFee()
 * 							defect 11199 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Helper class to provide utility interfaces for calling other classes
 * for WebAgent Renewal.
 *
 * @version	6.10.0 			12/22/2011
 * @author	Ray Rowehl
 * @author	Kathy Harrell 
 * <br>Creation Date:		12/15/2010 13:13:27
 */

public class RtsRenHelper
{
	private final static int COMBINATION_REGCLASSCD = 10;

	private CompleteTransactionData caCTData =
		new CompleteTransactionData();
	private DatabaseAccess caDBA;
	private RtsRenewalRequest caRenRequest;
	private RtsRenewalResponse caRenResponse;
	private VehicleInquiryData caVID;
	private WebAgencyAuthCfgData caWAAuthCfgData;
	private Vector cvWAAuthCfgData;

	/**
	 * RtsBatchHelper constructor
	 */
	public RtsRenHelper(
		RtsRenewalRequest aaRenRequest,
		DatabaseAccess aaDBA)
	{
		super();
		caRenRequest = aaRenRequest;
		caDBA = aaDBA;
	}

	/**
	 * RtsBatchHelper constructor
	 */
	public RtsRenHelper(
		RtsRenewalRequest aaRenRequest,
		RtsRenewalResponse aaRenResponse,
		Vector avWebAgencyAuthData,
		DatabaseAccess aaDBA)
	{
		super();
		caRenRequest = aaRenRequest;
		caRenResponse = aaRenResponse;
		cvWAAuthCfgData = avWebAgencyAuthData;
		caDBA = aaDBA;
	}

	/**
	 * Assign Fees to Response & Insert into RTS_WEB_AGNCY_TRANS_FEE 
	 * 
	 * @throws RTSException 
	 */
	private void assignFeesAndInsertWebAgncyTransFee()
		throws RTSException
	{
		WebAgencyTransactionFee laFeesSQL =
			new WebAgencyTransactionFee(caDBA);

		Vector lvRegFeesData = caCTData.getRegFeesData().getVectFees();
		RTSFees[] larrFees =
			new RTSFees[caCTData.getRegFeesData().getVectFees().size()];

		for (int i = 0; i < lvRegFeesData.size(); ++i)
		{
			FeesData laFeesData = (FeesData) lvRegFeesData.elementAt(i);

			// Insert into RTS_WEB_AGNCY_TRANS_FEE
			laFeesSQL.insWebAgencyTransactionFee(
				laFeesData,
				caRenResponse
					.getRtsVehInfo()
					.getTransData()
					.getSAVReqId());

			// Assign Account Code & Description.
			AccountCodesData laAcctCdData =
				AccountCodesCache.getAcctCd(
					laFeesData.getAcctItmCd(),
					RTSDate.getCurrentDate().getYYYYMMDDDate());

			larrFees[i] = new RTSFees();

			if (laAcctCdData != null)
			{
				// defect 11199
				larrFees[i].setFeesAcctCd(
					laAcctCdData.getAcctItmCd());
				// end defect 11199 
				larrFees[i].setFeesAcctCdDesc(
						laAcctCdData.getAcctItmCdDesc());
			}
			else
			{
				// defect 11199
				larrFees[i].setFeesAcctCd(
						laFeesData.getAcctItmCd());
				// end defect 11199 
				larrFees[i].setFeesAcctCdDesc(laFeesData.getDesc());
			}

			// Set Item Amount
			larrFees[i].setItemAmt(
				Double.parseDouble(
					laFeesData.getItemPrice().getValue()));

			// Add to Total Fees 
			caRenResponse.getRtsVehInfo().setTotalFees(
				caRenResponse.getRtsVehInfo().getTotalFees()
					+ larrFees[i].getItemAmt());

		}
		// Assign to Fees Array 
		caRenResponse.getRtsVehInfo().setFees(larrFees);
	}

	/**
	 * Update Response Reg/Plt Exp Mo/Yr per Fee Calc 
	 *
	 */
	private void assignNewExpMoYr()
	{
		RegFeesData laRegFeesData = caCTData.getRegFeesData();

		RtsRegistrationData laRegData =
			caRenResponse.getRtsVehInfo().getRegistrationData();

		laRegData.setNewRegExpMo(laRegFeesData.getToMonthDflt());
		laRegData.setNewRegExpYr(laRegFeesData.getToYearDflt());

		String lsRegPltCd = laRegData.getRegPltCd();

		if (!UtilityMethods.isEmpty(lsRegPltCd))
		{
			int liPltExpMo = 0;
			int liPltExpYr = 0;

			if (PlateTypeCache.isSpclPlate(lsRegPltCd))
			{
				if (PlateTypeCache.isVendorPlate(lsRegPltCd))
				{
					liPltExpMo =
						caCTData
							.getVehicleInfo()
							.getSpclPltRegisData()
							.getPltExpMo();

					liPltExpYr =
						caCTData
							.getVehicleInfo()
							.getSpclPltRegisData()
							.getPltExpYr();
				}
				else
				{
					liPltExpMo = laRegFeesData.getToMonthDflt();
					liPltExpYr = laRegFeesData.getToYearDflt();
				}
			}
			// This is set even if NOT Special Plates 
			if (caRenResponse.getRtsVehInfo().getSpecialPlts() != null)
			{
				caRenResponse
					.getRtsVehInfo()
					.getSpecialPlts()
					.setNewPltExpMo(
					liPltExpMo);

				caRenResponse
					.getRtsVehInfo()
					.getSpecialPlts()
					.setNewPltExpYr(
					liPltExpYr);
			}
		}
	}

	/**
	 * This method handles calling the Fees class to compute fees for 
	 * display on the client side.
	 * 
	 * @throws RTSException 
	 */
	private void computeFees() throws RTSException
	{
		caCTData.setOfcIssuanceNo(
			caRenResponse
				.getRtsVehInfo()
				.getRegistrationData()
				.getResComptCntyNo());

		// Vehicle Data retrieved from MF 
		MFVehicleData laOrigMFVehicleData = caVID.getMfVehicleData();

		// If Special Plate, set Charge Special Plate Fee
		if (laOrigMFVehicleData.isSpclPlt())
		{
			laOrigMFVehicleData
				.getSpclPltRegisData()
				.setSpclPltChrgFeeIndi(
				1);
		}

		// Assign Original MFVehicleData to CompleteTransactionData
		caCTData.setOrgVehicleInfo(laOrigMFVehicleData);

		// Assign New MFVehicleData to CompleteTransactionData
		caCTData.setVehicleInfo(
			(MFVehicleData) UtilityMethods.copy(laOrigMFVehicleData));

		caCTData.setNoMFRecs(caVID.getNoMFRecs());
		caCTData.getRegTtlAddlInfoData().setChrgFeeIndi(1);
		caCTData.getRegTtlAddlInfoData().setNoChrgRegEmiFeeIndi(
			laOrigMFVehicleData.getRegData().getRegClassCd()
				== COMBINATION_REGCLASSCD
				? 0
				: 1);

		// Data in this vector is a flag to charge Reg vs. RegCor Fees 
		prepInv();

		// Calculate Fees 			
		caCTData =
			new Fees().calcFees(TransCdConstant.WRENEW, caCTData);
	}

	/**
	 * <ul>
	 * <li> Extract and Verify Data.  
	 * <li> Calculate Fees
	 * <li> Assign New Exp Mo/Yr 
	 * <li> Manage Batch && Insert Transaction.
	 * </ul> 
	 * 
	 * @throws RTSException 
	 */
	private void extractDataInsertTrans() throws RTSException
	{
		// Must precede validateRecord()
		caRenResponse.setupFromVehRecord(caVID, caRenRequest);

		validateRecord();

		computeFees();

		assignNewExpMoYr();

		insertTrans();
	}

	/**
	 * Formulate the GeneralSearchData object used for searching.
	 * 
	 * @return GeneralSearchData
	 */
	private GeneralSearchData formGeneralSearchData()
	{
		GeneralSearchData laGSD = new GeneralSearchData();

		// Build according to KeyTypeCd 
		if (caRenRequest
			.getKeyTypeCd()
			.equals(RegistrationConstant.SCAN_KEYTYPECD))
		{
			laGSD.setKey1(CommonConstant.DOC_NO);
			laGSD.setKey2(caRenRequest.getDocNo());
		}
		else
		{
			laGSD.setKey1(CommonConstant.REG_PLATE_NO);
			laGSD.setKey2(caRenRequest.getRegPltNo());
			laGSD.setKey4(caRenRequest.getLast4OfVin());
		}
		laGSD.setKey3(TransCdConstant.WRENEW);
		laGSD.setIntKey2(CommonConstant.SEARCH_ACTIVE_INACTIVE);
		return laGSD;
	}

	/** 
	 * Insert into RTS_WEB_AGNCY_TRANS, RTS_WEB_AGNCY_TRANS_FEE
	 * 
	 * @throws RTSException
	 */
	private void insertTrans() throws RTSException
	{
		WebAgencyTransactionData laWATransData =
			new WebAgencyTransactionData(caRenResponse.getRtsVehInfo());
		laWATransData.setKeyTypeCd(caRenRequest.getKeyTypeCd());
		laWATransData.setWebSessionId(caRenRequest.getSessionId());
		laWATransData.setAgncyBatchIdntyNo(manageAgncyBatch());
		
		// defect 11137  
		OwnerData laOwnrData = caVID.getMfVehicleData().getOwnerData(); 
		laWATransData.setOwnrTtlName1(laOwnrData.getName1()); 
		laWATransData.setOwnrTtlName2(laOwnrData.getName2());
		RegistrationData laRegData = caVID.getMfVehicleData().getRegData();
		laWATransData.setRecpntName(laRegData.getRecpntName());
		// end defect 11137 
		
		//	Insert RTS_WEB_AGNCY_TRANS
		WebAgencyTransaction laSQL = new WebAgencyTransaction(caDBA);
		laSQL.insWebAgencyTransaction(laWATransData);
		
		
		// defect 11137 
		// Owner Address 
		WebAgencyTransactionAddressData laWATransOwnrAddrData = 
			new WebAgencyTransactionAddressData(laWATransData.getSavReqId(), 
					RegistrationConstant.OWNR_ADDR_TYPE_CD, 
					laOwnrData.getAddressData());
		
		WebAgencyTransactionAddress laWATASQL = new WebAgencyTransactionAddress(caDBA); 
		laWATASQL.insWebAgencyTransactionAddress(laWATransOwnrAddrData);
		
		// Renewal Mail Address  
		if (laRegData.getRenwlMailAddr().isPopulated())
		{
			WebAgencyTransactionAddressData laWATransRenwlAddrData = 
				new WebAgencyTransactionAddressData(laWATransData.getSavReqId(), 
						RegistrationConstant.RCPNT_ADDR_TYPE_CD, 
						laRegData.getRenwlMailAddr());
			laWATASQL.insWebAgencyTransactionAddress(laWATransRenwlAddrData);
		}
		// end defect 11137 


		// Assign Fees Vector to Response Object and Insert to 
		//    RTS_WEB_AGNCY_TRANS_FEE
		assignFeesAndInsertWebAgncyTransFee();
	}

	/**
	 * Close/Insert, Update Batch as Required.  
	 * Return AgncyBatchIdntyNo for this Transaction 
	 * 
	 * @return int 
	 * @throws RTSException 
	 */
	private int manageAgncyBatch() throws RTSException
	{
		int liAgncyBatchIdntyNo = Integer.MAX_VALUE;
		boolean lbInsert = false;
		boolean lbClose = false;

		WebAgencyBatchData laWABatchData = new WebAgencyBatchData();
		WebAgencyBatchData laWABatchSelectedData =
			new WebAgencyBatchData();

		laWABatchData.setAgncyIdntyNo(
			caWAAuthCfgData.getAgncyIdntyNo());
		laWABatchData.setOfcIssuanceNo(
			caWAAuthCfgData.getOfcIssuanceNo());
		laWABatchData.setBatchStatusCd(
			RegistrationConstant.OPEN_BATCHSTATUSCD);

		WebAgencyBatch laWABatchSQL = new WebAgencyBatch(caDBA);

		Vector lvWABatch =
			laWABatchSQL.qryWebAgencyBatch(laWABatchData, false);

		if (lvWABatch == null || lvWABatch.size() == 0)
		{
			lbInsert = true;
		}
		else
		{
			for (int i = 0; i < lvWABatch.size(); i++)
			{
				laWABatchData =
					(WebAgencyBatchData) lvWABatch.elementAt(i);

				if (laWABatchData.getAgncyBatchIdntyNo()
					< liAgncyBatchIdntyNo)
				{
					liAgncyBatchIdntyNo =
						laWABatchData.getAgncyBatchIdntyNo();
					laWABatchSelectedData = laWABatchData;
				}
			}
			laWABatchSQL.updWebAgencyBatchForLogicalLock(
				laWABatchSelectedData);

			// Test against Number Days
			int liNumDays =
				new RTSDate().getAMDate()
					- laWABatchSelectedData
						.getBatchInitTimestmp()
						.getAMDate();

			if (liNumDays > caWAAuthCfgData.getMaxSubmitDays())
			{
				lbClose = true;
				lbInsert = true;
			}
			else
			{
				WebAgencyTransaction laWATransSQL =
					new WebAgencyTransaction(caDBA);

				int liNumTrans =
					laWATransSQL.qryWebAgencyTransactionCnt(
						liAgncyBatchIdntyNo);

				if (liNumTrans >= caWAAuthCfgData.getMaxSubmitCount())
				{
					lbClose = true;
					lbInsert = true;
				}
			}
		}

		if (lbClose)
		{
			laWABatchData.setBatchStatusCd(
				RegistrationConstant.CLOSE_BATCHSTATUSCD);
			laWABatchData.setBatchCloseTimestmp(new RTSDate());
			laWABatchSQL.updWebAgencyBatch(laWABatchData);
		}

		if (lbInsert)
		{
			laWABatchData.setBatchStatusCd(
				RegistrationConstant.OPEN_BATCHSTATUSCD);
			laWABatchData.setBatchCloseTimestmp(null);
			laWABatchData =
				laWABatchSQL.insWebAgencyBatch(laWABatchData);
			liAgncyBatchIdntyNo = laWABatchData.getAgncyBatchIdntyNo();
		}
		return liAgncyBatchIdntyNo;
	}

	/**
	 * Prepare a vector of ProcessInventoryData for fees calculation.
	 * This is required so that the appropriate acctitmcd is issued. 
	 * If no fees, charges "Correction Fees" 
	 */
	private void prepInv()
	{
		Vector lvInvData = new Vector();
		RegistrationData laRegData =
			caVID.getMfVehicleData().getRegData();
		ProcessInventoryData laInvData = new ProcessInventoryData();
		laInvData.setOfcIssuanceNo(laRegData.getResComptCntyNo());
		laInvData.setItmCd(laRegData.getRegPltCd());
		laInvData.setInvQty(1);
		lvInvData.add(laInvData);
		caCTData.setInvItms(lvInvData);
		caCTData.setInvItemCount(lvInvData.size());
	}

	/**
	 * Process Get Vehicle request.
	 * 
	 * @throws RTSException  
	 */
	public void processGetVehReq() throws RTSException
	{
		Object laVehicle =
			new CommonServerBusiness().processData(
				GeneralConstant.COMMON,
				CommonConstant.GET_VEH,
				formGeneralSearchData());

		if (laVehicle != null
			&& laVehicle instanceof VehicleInquiryData)
		{
			caVID = (VehicleInquiryData) laVehicle;

			if (caVID.isMFDown())
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_V21_NOT_AVAILABLE);
			}

			// Initial Validation of Returned Object 

			// Incomplete Data   (Shouldn't Happen!) 
			if (caVID.getNoMFRecs() == 1
				&& (caVID.getMfVehicleData() == null
					|| caVID.getMfVehicleData().getRegData() == null))
			{
				Log.write(Log.APPLICATION, this,"processGetVehReq - *** Error 2300 *** - Incomplete renewal data");
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
			}

			// No Record Found / Canceled Record : 2301 
			if (caVID.getNoMFRecs() == 0
				|| (caVID.getNoMFRecs() == 1
					&& caVID
						.getMfVehicleData()
						.getRegData()
						.getCancPltIndi()
						== 1))
			{
				throw new RTSException(
					ErrorsConstant
						.ERR_NUM_WEBAGNT_LOOKUP_NO_RECORD_FOUND);
			}

			// Multiple Records: 2302  (Shouldn't Happen!) 
			if (caVID.getNoMFRecs() > 1)
			{
				throw new RTSException(
					ErrorsConstant
						.ERR_NUM_WEBAGNT_LOOKUP_MULT_RECORDS_FOUND);
			}

			extractDataInsertTrans();
		}
		else
		{
			// Incorrect Object Returned 
			Log.write(Log.APPLICATION, this,"processGetVehReq - *** Error 2300 *** - Incorrect Vehicle object returned");
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
		}
	}

	/**
	 * This return Receipt Data 
	 * 
	 * @throws RTSException 
	 */
	public void processGetWebAgncyTransReceipt() throws RTSException
	{
		int liStkrPrntQty = 1;
		WebAgencyTransaction laWATransSQL =
			new WebAgencyTransaction(caDBA);
		WebAgencyTransactionData laWATransData =
			new WebAgencyTransactionData();
		laWATransData.setSavReqId(caRenRequest.getRequestIdntyNo());
		laWATransData.setAccptVehIndi(1);

		/**
		 *  Confirm WEB_AGNCY_TRANS data exists 
		 */
		Vector lvData =
			laWATransSQL.qryWebAgencyTransaction(laWATransData, false);

		if (lvData == null || lvData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_TRANS_NOT_FOUND);
		}

		laWATransData = (WebAgencyTransactionData) lvData.elementAt(0);

		if (!UtilityMethods.isEmpty(caRenRequest.getNewInvItmNo()))
		{
			caRenRequest.setNewInvItmNo(
				caRenRequest.getNewInvItmNo().trim().toUpperCase());
		}
		laWATransData.setInvItmNo(caRenRequest.getNewInvItmNo());

		/**
		 *  Confirm WEB_AGNCY_SECURITY data exists 
		 */
		WebAgentSecurity laWASecrtySQL = new WebAgentSecurity(caDBA);

		Vector lvWASecrtyData =
			laWASecrtySQL.qryWebAgentSecurity(
				laWATransData.getAgntSecrtyIdntyNo());

		if (lvWASecrtyData == null || lvWASecrtyData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND);
		}

		WebAgentSecurityData laWASecrtyData =
			(WebAgentSecurityData) lvWASecrtyData.elementAt(0);

		/**
		 *  Confirm WEB_AGNT data exists 
		 */
		WebAgentData laWebAgntData = new WebAgentData();
		laWebAgntData.setAgntIdntyNo(laWASecrtyData.getAgntIdntyNo());
		WebAgent laWAgntSQL = new WebAgent(caDBA);

		Vector lvWAgntData =
			laWAgntSQL.qryWebAgent(laWASecrtyData.getAgntIdntyNo());

		if (lvWAgntData == null || lvWAgntData.isEmpty())
		{
			Log.write(Log.APPLICATION, this, "processGetWebAgncyTransReceipt - *** Error 2300 *** - Agent does not exist");
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
		}
		WebAgentData laWAgntData =
			(WebAgentData) lvWAgntData.elementAt(0);

		/**
		 *  Confirm WEB_AGNCY data exists 
		 */
		WebAgency laWAgncySQL = new WebAgency(caDBA);
		WebAgencyData laWAgncyData = new WebAgencyData();
		laWAgncyData.setAgncyIdntyNo(laWASecrtyData.getAgncyIdntyNo());

		Vector lvWAgncyData = laWAgncySQL.qryWebAgency(laWAgncyData);

		if (lvWAgncyData == null || lvWAgncyData.isEmpty())
		{
			Log.write(Log.APPLICATION, this, "processGetWebAgncyTransReceipt - *** Error 2300 *** - Agency does not exist");
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
		}

		laWAgncyData = (WebAgencyData) lvWAgncyData.elementAt(0);
		
		/**
		 *  RTS_WEB_AGNCY_TRANS_FEE
		 *  
		 *  1st - Insert Optional Fees 
		 */
		WebAgencyTransactionFee laWATransFeeSQL =
			new WebAgencyTransactionFee(caDBA);
		
		// defect 11149 
		// Insert Optional Fees
		RTSFees[] larrOptionalFees =  caRenRequest.getOptionalFees();
		
		if (larrOptionalFees != null) 
		{
			for (int i = 0; i< larrOptionalFees.length; i++)
			{
				RTSFees laRTSFees = (RTSFees) larrOptionalFees[i]; 
				laWATransFeeSQL.insWebAgencyTransactionFee(
						new FeesData(laRTSFees),caRenRequest.getRequestIdntyNo()); 
			}
		}
		// end defect 11149 

		/**
		 *  Query RTS_WEB_AGNCY_TRANS_FEE  
		 */
		Vector lvWATransFeeData =
			laWATransFeeSQL.qryWebAgencyTransactionFee(
				laWATransData.getSavReqId());
		
		// defect 11137 
		/**
		 *  Query RTS_WEB_AGNCY_TRANS_ADDR
		 *    Owner & Recipient   
		 */
		WebAgencyTransactionAddress laWATransAddrSQL =
			new WebAgencyTransactionAddress(caDBA);
		
		WebAgencyTransactionAddressData laWATransOwnrAddrData =  
			laWATransAddrSQL.qryWebAgencyTransactionAddr(
				laWATransData.getSavReqId(),RegistrationConstant.OWNR_ADDR_TYPE_CD);

		WebAgencyTransactionAddressData laWATransRcpntAddrData =  
			laWATransAddrSQL.qryWebAgencyTransactionAddr(
				laWATransData.getSavReqId(),RegistrationConstant.RCPNT_ADDR_TYPE_CD);
		// end defect 11137 
		
		/**
		 *  Confirm Valid Plate Type   
		 */
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(laWATransData.getRegPltCd());

		if (laPltTypeData == null)
		{
			Log.write(Log.APPLICATION, this, "processGetWebAgncyTransReceipt - *** Error 2300 *** - Plate Type does not exist");
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
		}

		/**
		 *  Confirm Inventory Request is Valid, Available    
		 */
		if (laWATransData.getMustReplPltIndi() == 1
			&& !laPltTypeData.isSpclPlt())
		{
			String lsInvItmNo = caRenRequest.getNewInvItmNo();

			if (UtilityMethods.isEmpty(lsInvItmNo))
			{
				Log.write(Log.APPLICATION, this, "processGetWebAgncyTransReceipt - *** Error 2300 *** - Inventory does not exist");
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
			}
			int liInvItmYr = 0;

			if (laPltTypeData.isAnnualPlt())
			{
				liInvItmYr = laWATransData.getRegExpYr() + 1;
				liStkrPrntQty = 0;
			}
			
			
			ValidateInventoryPattern laValidateInventoryPattern =
				new ValidateInventoryPattern();

			ProcessInventoryData laProcessInventoryData =
				new ProcessInventoryData();
			laProcessInventoryData.setItmCd(
				laWATransData.getRegPltCd());
			laProcessInventoryData.setInvQty(1);
			laProcessInventoryData.setInvItmNo(lsInvItmNo);
			laProcessInventoryData.setInvItmYr(liInvItmYr);
			laProcessInventoryData.setOfcIssuanceNo(
				laWATransData.getResComptCntyNo());
			laProcessInventoryData.setInvLocIdCd("S");
			laProcessInventoryData.setInvId(
				"" + laWATransData.getSubconId());
			laProcessInventoryData.setVerifyInvOwner(true);

			// Will Throw RTSException if Invalid InvItmNo 
			laValidateInventoryPattern.validateItmNoInput(
				laProcessInventoryData.convertToInvAlloctnUIData(
					laProcessInventoryData));

			// Plate Number Valid; Delete from Inventory
			
			// defect 11178 
			InventoryAllocation laInvAllSQL = new InventoryAllocation(caDBA);
			
			Vector lvSubstaId = laInvAllSQL.qryInventoryAllocationSubstaIdForSubcon(
					laWATransData.getResComptCntyNo(), laWATransData.getSubconId(), laWATransData.getRegPltCd());
		
			if (lvSubstaId != null && lvSubstaId.size() >0)
			{
				InventoryServerBusiness laInvSrvBus =
					new InventoryServerBusiness();
				
				for (int i = 0; i<lvSubstaId.size(); i++)
				{
					int liSubstaId = ((Integer) lvSubstaId.elementAt(i)).intValue(); 
					laProcessInventoryData.setSubstaId(liSubstaId);
					Vector lvInvData = new Vector();
					lvInvData.add(caDBA);
					lvInvData.add(laProcessInventoryData);

					try
					{
						// Throws RTSException if Not Found or Not Allocated
						laInvSrvBus.processData(
								GeneralConstant.INVENTORY,
								InventoryConstant.DELETE_FOR_ISSUE_INVENTORY,
								lvInvData);
						
						break; 
					}
					catch (RTSException aeRTSEx)
					{
						if (i == lvSubstaId.size() -1)
						{
							throw aeRTSEx; 
						}
					}
				}
			}
			else
			{
				throw new RTSException(ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB); 
			}
			// end defect 11178 
		}

		/**
		 *  Setup for Receipt / Transaction
		 *   (using Subcontractor Logic for WRENEW)     
		 */
		SubcontractorData laSubconData = new SubcontractorData();
		laSubconData.setAddressData(laWAgncyData.getAddressData());
		laSubconData.setName1(laWAgncyData.getName1().toUpperCase());
		laSubconData.setName2(laWAgncyData.getName2().toUpperCase());

		SubcontractorRenewalData laSubconRenwlData =
			new SubcontractorRenewalData(
				laWATransData,
				lvWATransFeeData);

		SubcontractorRenewalCacheData laSubcontractorRenewalCacheData =
			new SubcontractorRenewalCacheData();
		laSubcontractorRenewalCacheData.setSubconInfo(laSubconData);

		laSubcontractorRenewalCacheData.setTempSubconRenewalData(
			laSubconRenwlData);

		SubcontractorHdrData laSubconHdrData =
			new SubcontractorHdrData();

		laSubconHdrData.setSubconId(laWATransData.getSubconId());

		laSubcontractorRenewalCacheData.setSubcontractorHdrData(
			laSubconHdrData);
		
		// defect 11137 
		CompleteTransactionData laCTData =
			new CompleteTransactionData(
					laWATransData,
					lvWATransFeeData,
					laWATransOwnrAddrData,
					laWATransRcpntAddrData);
		// end defect 11137 
		
		laCTData.assignDataForSubconReceipt(
			laSubcontractorRenewalCacheData,
			laSubconRenwlData,
			TransCdConstant.WRENEW,
			laWATransData);

		laCTData.setWebAgntUserName(laWAgntData.getUserName());

		/**
		 *  Create Receipt; Convert to PDF via JetPCL 
		 */
		Vector lvCTD = new Vector();
		lvCTD.add(laCTData);

		GenRegistrationReceipts laGenRpt =
			new GenRegistrationReceipts();

		laGenRpt.formatReceipt(lvCTD);

		String lsReceipt =
			laGenRpt.getReceipt().toString() + ReportConstant.FF;

		JetPclProcess laJetPclProcess = new JetPclProcess();

		if (!laJetPclProcess
			.convertFile(
				lsReceipt,
				Integer.toString(caRenRequest.getRequestIdntyNo())))
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_RENEWAL_RECEIPT_ERROR);
		}

		/**
		 *  Create WRENEW Transaction, Update RTS_WEB_AGNCY_TRANS 
		 */
		if (laWATransSQL
			.accptWebAgencyTransaction(
				laWATransData.getSavReqId(),
				liStkrPrntQty,
				laWATransData.getInvItmNo(),
				laCTData
					.getSubconRenwlData()
					.getSubconProcessDateTime())
			!= 1)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_NO_TRANS_AVAIL_TO_ACCPT);
		}
	}

	/**
	 * Validate Record 
	 *
	 * @throws RTSException 
	 */
	private void validateRecord() throws RTSException
	{
		// Last4VIN does not match: 2303 	
		String lsLast4 = caRenRequest.getLast4OfVin();
		String lsVin =
			caVID.getMfVehicleData().getVehicleData().getVin();

		if (caRenRequest
			.getKeyTypeCd()
			.equals(RegistrationConstant.KEYED_KEYTYPECD))
		{
			// defect 10887
			// Allow processing of No Vin Vehicles 
			int liLast4Length =
				lsLast4 == null ? 0 : lsLast4.trim().length();

			int liVinLength = lsVin == null ? 0 : lsVin.trim().length();
			
			if (liLast4Length == 0 || liVinLength == 0)
			{
				if (!(liLast4Length == 0 && liVinLength == 0))
				{
					throw new RTSException(
						ErrorsConstant
							.ERR_NUM_WEBAGNT_LOOKUP_NO_MATCH_LAST_4_VIN);
				}
			}
			else
			{
				if (liVinLength >= liLast4Length)
				{
					String lsCompare =
						lsVin.substring(liVinLength - liLast4Length);

					if (!lsCompare.equals(lsLast4))
					{
						throw new RTSException(
							ErrorsConstant
								.ERR_NUM_WEBAGNT_LOOKUP_NO_MATCH_LAST_4_VIN);
					}
				}
				else
				{
					throw new RTSException(
						ErrorsConstant
							.ERR_NUM_WEBAGNT_LOOKUP_NO_MATCH_LAST_4_VIN);
				}
			}
			// end defect 10887
		}
		// Unsupported County: 2304 
		int liResComptCntyNo =
			caVID.getMfVehicleData().getRegData().getResComptCntyNo();

		boolean lbFound = false;

		WebAgencyAuthCfgData laWAAuthCfgData =
			new WebAgencyAuthCfgData();

		for (int i = 0; i < cvWAAuthCfgData.size(); i++)
		{
			laWAAuthCfgData =
				(WebAgencyAuthCfgData) cvWAAuthCfgData.elementAt(i);

			if (laWAAuthCfgData.getOfcIssuanceNo() == liResComptCntyNo)
			{
				lbFound = true;
				caWAAuthCfgData = laWAAuthCfgData;
				break;
			}
		}
		if (!lbFound)
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_LOOKUP_AGNT_NOT_AUTHORIZED_FOR_COUNTY);
		}

		// Agent Must Scan:  2305 
		if (caRenRequest
			.getKeyTypeCd()
			.equals(RegistrationConstant.KEYED_KEYTYPECD)
			&& caWAAuthCfgData.getKeyEntryCd().equals(
				RegistrationConstant.SCAN_KEYTYPECD))
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_LOOKUP_AGNT_MUST_SCAN);
		}

		// Agent Must Keyboard:  2319 
		if (caRenRequest
			.getKeyTypeCd()
			.equals(RegistrationConstant.SCAN_KEYTYPECD)
			&& caWAAuthCfgData.getKeyEntryCd().equals(
				RegistrationConstant.KEYED_KEYTYPECD))
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_LOOKUP_AGNT_MUST_KEYBOARD);
		}

		//  Registration "Too Expired" : 2307  
		RegistrationData laRegData =
			caVID.getMfVehicleData().getRegData();

		if (laRegData.isExpired())
		{
			boolean lbThrowException = false;

			if (caWAAuthCfgData
				.getExpProcsngCd()
				.equals(RegistrationConstant.NONE_EXPPROCSNGCD))
			{
				lbThrowException = true;
			}
			else if (
				laWAAuthCfgData.getExpProcsngCd().equals(
					RegistrationConstant.GRACE_EXPPROCSNGCD))
			{
				CommonEligibility laCommElig =
					new CommonEligibility(caVID);

				// This check actually verifies that 
				// not within the Grace Period for IVTRS 
				lbThrowException = laCommElig.isExpiredRegistration();
			}
			else if (
				caWAAuthCfgData.getExpProcsngCd().equals(
					RegistrationConstant.EXPIRED_EXPPROCSNGCD))
			{
				int liCurrMos =
					RTSDate.getCurrentDate().getYear() * 12
						+ RTSDate.getCurrentDate().getMonth();

				int liRegMos =
					laRegData.getRegExpYr() * 12
						+ laRegData.getRegExpMo();

				lbThrowException =
					(liCurrMos - liRegMos)
						> laWAAuthCfgData.getExpProcsngMos();
			}
			if (lbThrowException)
			{
				throw new RTSException(
					ErrorsConstant
						.ERR_NUM_WEBAGNT_LOOKUP_REG_EXPIRED_BEYOND_AUTH);
			}
		}

		// Validation continued via VehicleInquiryData method 
		caVID.validateForWRENEW();

		if (caWAAuthCfgData.getIssueInvIndi() == 0
			&& caRenResponse
				.getRtsVehInfo()
				.getRegistrationData()
				.isMustReplPltIndi()
			&& caRenResponse.getRtsVehInfo().getSpecialPlts() == null)
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_LOOKUP_LOC_NOT_AUTHORIZED_FOR_INV);
		}

		caRenResponse.getRtsVehInfo().getTransData().setSubconId(
			caWAAuthCfgData.getSubconId());
	}
}
