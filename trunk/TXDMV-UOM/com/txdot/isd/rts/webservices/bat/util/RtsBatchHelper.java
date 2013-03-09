package com.txdot.isd.rts.webservices.bat.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.registration.GenRegistrationReceipts;
import com.txdot.isd.rts.services.util.JetPclProcess;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.webservices.agncy.util.RtsAgncyHelper;
import com.txdot.isd.rts.webservices.bat.data.RtsBatchDetailRequest;
import com.txdot.isd.rts.webservices.bat.data.RtsBatchDetailResponse;
import com.txdot.isd.rts.webservices.bat.data.RtsBatchListRequest;
import com.txdot.isd.rts.webservices.bat.data.RtsBatchListSummaryLine;
import com.txdot.isd.rts.webservices.ren.data.RTSFees;
import com.txdot.isd.rts.webservices.ren.data.RtsVehicleInfo;

/*
 * RtsBatchHelper.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/28/2011	Initial load.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	03/22/2011	Working on Security settings.
 * 							defect 10673 Ver 6.7.1
 * K Harrell	03/25/2011	...working 
 * 							defect 10785 Ver 6.7.1
 * Ray Rowehl	03/28/2011	Refactor AgntSecrtyIdntyNo for BLSL.
 * 							modify getBatchList()
 * 							defect 10673 Ver 6.7.1
 * K Harrell	03/31/2011	add approveBatch() 
 * 							defect 10785 Ver 6.7.1 
 * K Harrell	04/05/2011	add Search Date validation/assignment 
 * 							modify getBatchList() 
 * 							defect 10768 Ver 6.7.1
 * K Harrell	05/03/2011	... working 
 * 							defect 10768 Ver 6.7.1 	
 * K McKee      09/13/2011  changed code to qry RTS.RTS_LOG_FUNC_TRANS
 *                          for empId instead of RS_SECURITY for batch approval
 *                          modify approveBatch()
 *                          defect 10729
 * K McKee      10/06/2011  Added logging for Error 3400
 *                          defect 10729  Ver 6.9.0
 * K Harrell	11/08/2011	modify for receipt address
 * 						 	modify reprintBatchItem() 
 * 							defect 11137 Ver 6.9.0 
 * K McKee      12/30/2011  added plate serach
 * 							add getBatchDetailForPlate()
 * 							defect 11239  Ver 6.10.0
 * K McKee      02/16/2012  added log comment and aex
 * 							defect 11239  Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Helper class that provides common batch routines.
 *
 * @version	6.10.0 			12/30/2011
 * @author	Ray Rowehl
 * @author  Kathy Harrell 
 * <br>Creation Date:		01/28/2011 13:44:13
 */

public class RtsBatchHelper
{
	private DatabaseAccess caDBA;
	private RtsBatchDetailRequest caDetailRequest;
	private RtsBatchListRequest caListRequest;
	private WebAgencyBatchData caWABatchData;
	private WebAgentSecurityData caWASecData;

	/**
	 * RtsBatchHelper constructor
	 */
	public RtsBatchHelper(
		RtsBatchListRequest aaListRequest,
		RtsBatchDetailRequest aaDetailRequest,
		DatabaseAccess aaDBA)
	{
		super();
		caListRequest = aaListRequest;
		caDetailRequest = aaDetailRequest;
		caDBA = aaDBA;
	}

	/**
	 * Process Batch Approval 
	 *   - Validate 
	 *        - TransWsId
	 *        - RTS Security
	 *   - Update BatchStatusCd to "I"  (In Process)
	 *   - End Trans/Begin Trans   
	 *   - Call CommonServerBusiness to
	 *        - Create Transactions 
	 *        - Update to BatchStatusCd "A"  
	 *
	 * @throws RTSException
	 */
	private void approveBatch() throws RTSException
	{
		// TransWsId not passed / WsId not Found: 2330		
		if (caListRequest.getTransWsId() < 0)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_INVALID_OFC_WSID_DATA);
		}


	 	// Begin defect 10729 	
		LogonFunctionTransaction laLogonFunctionTransaction = new LogonFunctionTransaction(caDBA);
		
		Calendar laCalendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		 
		// Read the Logon Function Transaction table for the office issuance id,
		// transwsID, and current date to retreive the Employee id.
		
		 
		String lsEmpId = laLogonFunctionTransaction.qryLogonFunctionTransactionEmpID(
				caListRequest.getOfcIssuanceNo(), caListRequest.getTransWsId(), 
				Integer.parseInt(sdf.format(laCalendar.getTime())));
		// RTS Security not found:  2331  
		if (lsEmpId == null || lsEmpId.trim().equals(""))
		{
			throw new RTSException(
				ErrorsConstant.LOGIN_INFO_NOT_FOUND_IN_LOG_FUNCTION_TRANS);
		}
	 	caListRequest.setOfcIssuanceNo(
		 		caWABatchData.getOfcIssuanceNo());
		// qryWebAgent was called to get the user name for reading the RTS_SECURITY table
		// Since we are no longer reading the RTS_SECURITY table, we do not
		// have to read the RTS_WEB_AGNT table.

/*		// RTS_WEB_AGNT
		WebAgent laWASQL = new WebAgent(caDBA);
		Vector lvWAData =
			laWASQL.qryWebAgent(caWASecData.getAgntIdntyNo());

		// Agent Not Found: 2329 
		if (lvWAData == null || lvWAData.size() != 1)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AGENT_NOT_FOUND);
		}
		WebAgentData laWAgntData = (WebAgentData) lvWAData.elementAt(0);*/

		// RTS_WEB_AGNCY 
		WebAgencyData laWAgncyData = new WebAgencyData();
		laWAgncyData.setAgncyIdntyNo(caWABatchData.getAgncyIdntyNo());
		WebAgency laWAgncySQL = new WebAgency(caDBA);

		Vector lvWAgncyData = laWAgncySQL.qryWebAgency(laWAgncyData);
		if (lvWAgncyData == null || lvWAgncyData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AGENCY_NOT_FOUND);
		}
		laWAgncyData = (WebAgencyData) lvWAgncyData.elementAt(0);

		// RTS_ASSGND_WS_IDS 
		AssignedWorkstationIds laAssgndWksIdsSQL =
			new AssignedWorkstationIds(caDBA);
		AssignedWorkstationIdsData laAssgndWksIdsData =
			laAssgndWksIdsSQL.qryAssignedWorkstationIds(
				caWABatchData.getOfcIssuanceNo(),
				caListRequest.getTransWsId());

		// WsId not Found: 2330 
		if (laAssgndWksIdsData == null)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_INVALID_OFC_WSID_DATA);
		}

	
		/*  commented out per defect 10729	
	 	// RTS_SECURITY 
		SecurityData laSecData = new SecurityData();
		laSecData.setOfcIssuanceNo(caWABatchData.getOfcIssuanceNo());
		laSecData.setSubstaId(laAssgndWksIdsData.getSubstaId());
		laSecData.setUserName(laWAgntData.getUserName());
		Security laSecSQL = new Security(caDBA);
		laSecData.setSubstaId(laSecSQL.qrySecuritySubstaId(laSecData));
		Vector lvSecData = laSecSQL.qrySecurity(laSecData);

		// RTS Security not found:  2331  
		if (lvSecData == null || lvSecData.size() != 1)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_RTS_SECURITY_NOT_FOUND);
		}

		laSecData = (SecurityData) lvSecData.elementAt(0);*/
		
		// end defect 10729 
		if (caWABatchData
			.getBatchStatusCd()
			.equals(RegistrationConstant.INPROCESS_BATCHSTATUSCD))
		{
			if (!(caWABatchData.getTransWsId()
				== caListRequest.getTransWsId()
				&& caWABatchData.getTransAMDate()
					== new RTSDate().getAMDate()
				&& caWABatchData.getTransEmpId().equals(lsEmpId)))
			{
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_INVALID_TRANS_HDR);
			}
		}
		else
		{
			caWABatchData.setBatchStatusCd(
				RegistrationConstant.INPROCESS_BATCHSTATUSCD);
			caWABatchData.setTransAMDate(new RTSDate().getAMDate());
			caWABatchData.setTransEmpId(lsEmpId);
			caWABatchData.setTransWsId(caListRequest.getTransWsId());
			caWABatchData.setCustSeqNo(0);
			caWABatchData.setBatchApprvTimestmp(new RTSDate());
			updtBatchStatus();
		}
		caDBA.endTransaction(DatabaseAccess.COMMIT);
		// UOW #2 END

		Hashtable laHashApprvBatch = new Hashtable();
		laHashApprvBatch.put("BATCHDATA", caWABatchData);
		laHashApprvBatch.put("ASSGNDWSDATA", laAssgndWksIdsData);
		laHashApprvBatch.put("WEBAGNCYDATA", laWAgncyData);

		// UOW managed w/in CommonServerBusiness  
		CommonServerBusiness laCommSrvrBus = new CommonServerBusiness();
		Boolean lbSuccess =
			(Boolean) laCommSrvrBus.processData(
				GeneralConstant.COMMON,
				CommonConstant.PROC_WEB_SUB,
				laHashApprvBatch);

		if (!lbSuccess.booleanValue())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_APPROVAL_NOT_SUCCESSFUL);
		}
		caDBA.beginTransaction();
		caWABatchData.setBatchCompleteTimestmp(new RTSDate());
		updtBatchStatus();
		caDBA.endTransaction(DatabaseAccess.COMMIT);
		caDBA.beginTransaction();

	}

	/** 
	 * Batch Request Validation  
	 * 
	 * @param asNewStatusCd 
	 * @throws RTSException
	 */
	private void batchRequestValidation(String asNewStatusCd)
		throws RTSException
	{
		// Verify Record Found 
		caWABatchData = new WebAgencyBatchData();
		caWABatchData.setAgncyBatchIdntyNo(
			caListRequest.getAgencyBatchIdntyNo());

		WebAgencyBatch laWABatchSQL = new WebAgencyBatch(caDBA);

		Vector lvBatchSummary =
			laWABatchSQL.qryWebAgencyBatch(caWABatchData);

		// No Batch or More Than One: 2326  
		if (lvBatchSummary == null || lvBatchSummary.size() != 1)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AGENCY_BATCH_NOT_FOUND);
		}
		caWABatchData =
			(WebAgencyBatchData) lvBatchSummary.elementAt(0);

		// Batch Not Available For Request:  2328  
		if ((asNewStatusCd
			.equals(RegistrationConstant.SUBMIT_BATCHSTATUSCD)
			&& !caWABatchData.isAvailableForSubmit())
			|| (asNewStatusCd
				.equals(RegistrationConstant.APPROVED_BATCHSTATUSCD)
				&& !caWABatchData.isAvailableForApprove()))
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENCY_BATCH_NOT_AVAIL_FOR_REQUEST);
		}
		caWASecData = new WebAgentSecurityData();
		try
		{
			caWASecData.setAgntSecrtyIdntyNo(
				Integer.parseInt(caListRequest.getCaller()));
		}
		catch (Exception aeEx)
		{
		}
		WebAgentSecurity laWASecSQL = new WebAgentSecurity(caDBA);
		Vector lvWASecData =
			laWASecSQL.qryWebAgentSecurity(
				caWASecData.getAgntSecrtyIdntyNo());

		// Agent Security Record Not Found:  2324 
		if (lvWASecData == null || lvWASecData.size() != 1)
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND);
		}
		caWASecData = (WebAgentSecurityData) lvWASecData.elementAt(0);

		// User Not Authorized for Request:  2317  
		if ((asNewStatusCd
			.equals(RegistrationConstant.SUBMIT_BATCHSTATUSCD)
			&& caWASecData.getSubmitBatchAccs() != 1)
			|| (asNewStatusCd
				.equals(RegistrationConstant.APPROVED_BATCHSTATUSCD)
				&& caWASecData.getAprvBatchAccs() != 1))
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);

	}

	/**
	 * Return Details for Batch
	 * 
	 * @param aaResponse
	 * @throws RTSException
	 */
	public void getBatchDetail(RtsBatchDetailResponse aaResponse)
		throws RTSException
	{
		// Query RTS_WEB_AGNT_SECURITY 
		WebAgentSecurityData laWASecData = new WebAgentSecurityData();
		try
		{
			laWASecData.setAgntSecrtyIdntyNo(
				Integer.parseInt(caDetailRequest.getCaller()));
		}
		catch (Exception Ex)
		{
		}

		WebAgentSecurity laWASecSQL = new WebAgentSecurity(caDBA);
		Vector lvWASecData =
			laWASecSQL.qryWebAgentSecurity(
				laWASecData.getAgntSecrtyIdntyNo());

		// Agent Security Not Found: 2324
		if (lvWASecData == null || lvWASecData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND);
		}

		laWASecData = (WebAgentSecurityData) lvWASecData.elementAt(0);

		// Agent Does Not have (Batch) Authorization: 2317 
		if (laWASecData.getBatchAccs() != 1)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);
		}

		WebAgencyBatchData laWABData = new WebAgencyBatchData();
		laWABData.setAgncyBatchIdntyNo(
			caDetailRequest.getAgencyBatchIdntyNo());

		// Query RTS_WEB_AGNCY_BATCH			
		WebAgencyBatch laWABSQL = new WebAgencyBatch(caDBA);
		Vector lvBatchSummary = laWABSQL.qryWebAgencyBatch(laWABData);

		// Agency Batch Not Found: 2326 
		if (lvBatchSummary == null || lvBatchSummary.size() != 1)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AGENCY_BATCH_NOT_FOUND);
		}

		laWABData = (WebAgencyBatchData) lvBatchSummary.elementAt(0);
		aaResponse.setAgencyIdntyNo(laWABData.getAgncyIdntyNo());
		aaResponse.setAgencyBatchIdntyNo(
			laWABData.getAgncyBatchIdntyNo());
		aaResponse.setOfcIssuanceNo(laWABData.getOfcIssuanceNo());
		aaResponse.setOfcIssuanceName(
			OfficeIdsCache.getOfcName(laWABData.getOfcIssuanceNo()));

		int liTransCount = 0;

		WebAgencyTransactionData laWATransData =
			new WebAgencyTransactionData();

		laWATransData.setAgncyBatchIdntyNo(
			laWABData.getAgncyBatchIdntyNo());

		// Query RTS_WEB_AGNCY_TRANS for given batch 
		WebAgencyTransaction laWATransSQL =
			new WebAgencyTransaction(caDBA);

		Vector lvBatchDetail =
			laWATransSQL.qryWebAgencyTransaction(
				laWATransData,
				true,
				true);

		// Agency Batch Not Found: 2327
		if (lvBatchDetail == null || lvBatchDetail.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENCY_BATCH_HAS_NO_TRANS);
		}

		RtsVehicleInfo[] larrVehInfo =
			new RtsVehicleInfo[lvBatchDetail.size()];

		// For all Trans 
		for (Iterator laIter = lvBatchDetail.iterator();
			laIter.hasNext();
			)
		{
			laWATransData = (WebAgencyTransactionData) laIter.next();

			RtsVehicleInfo laVehInfo =
				new RtsVehicleInfo(laWATransData);

			laVehInfo.setReprtAccs(
				laWATransData.getReprtAccs(laWABData, laWASecData));

			laVehInfo.setVoidAccs(
				laWATransData.getVoidAccs(laWABData, laWASecData));

			laVehInfo.getRegistrationData().setResComptCntyName(
				OfficeIdsCache.getOfcName(
					laVehInfo
						.getRegistrationData()
						.getResComptCntyNo()));

			// Query RTS_WEB_AGNCY_TRANS_FEE 
			WebAgencyTransactionFee laSqlFees =
				new WebAgencyTransactionFee(caDBA);

			Vector lvFeesData =
				laSqlFees.qryWebAgencyTransactionFee(
					laWATransData.getSavReqId());

			RTSFees[] larrRtsFees = new RTSFees[lvFeesData.size()];

			int liFeeCount = 0;

			// For all Fees 
			for (Iterator laIterFees = lvFeesData.iterator();
				laIterFees.hasNext();
				)
			{
				WebAgencyTransactionFeeData laWATransFeeData =
					(WebAgencyTransactionFeeData) laIterFees.next();

				RTSFees laRtsFeeData = new RTSFees();

				AccountCodesData laAcctData =
					AccountCodesCache.getAcctCd(
						laWATransFeeData.getAcctItmCd(),
						new RTSDate().getYYYYMMDDDate());

				if (laAcctData != null)
				{
					laRtsFeeData.setFeesAcctCdDesc(
						laAcctData.getAcctItmCdDesc());
				}
				else
				{
					laRtsFeeData.setFeesAcctCdDesc(
						laWATransFeeData.getAcctItmCd());
				}
				laRtsFeeData.setItemAmt(
					new Double(
						laWATransFeeData.getItmPrice().getValue())
						.doubleValue());

				larrRtsFees[liFeeCount] = laRtsFeeData;

				laVehInfo.setTotalFees(
					laVehInfo.getTotalFees()
						+ laRtsFeeData.getItemAmt());

				liFeeCount = liFeeCount + 1;
			}

			laVehInfo.setFees(larrRtsFees);
			larrVehInfo[liTransCount] = laVehInfo;
			liTransCount = liTransCount + 1;
		}

		aaResponse.setBatchDetailLines(larrVehInfo);
		aaResponse.setDetailCount(liTransCount);
	}

	/**
	 * Run and get results from query.
	 * 
	 * @return Hashtable
	 * @throws RTSException
	 */
	public Hashtable getBatchList() throws RTSException
	{
		// Query RTS_WEB_AGNT_SECURITY 
		WebAgentSecurityData laWASecData = new WebAgentSecurityData();
		try
		{
			laWASecData.setAgntSecrtyIdntyNo(
				Integer.parseInt(caListRequest.getCaller()));
		}
		catch (Exception Ex)
		{
		}
		WebAgentSecurity laWASecSQL = new WebAgentSecurity(caDBA);
		Vector lvSec =
			laWASecSQL.qryWebAgentSecurity(
				laWASecData.getAgntSecrtyIdntyNo());
		laWASecData = (WebAgentSecurityData) lvSec.elementAt(0);

		WebAgencyBatchData laWABatchData = new WebAgencyBatchData();

		boolean lbDMVUser = false;
		if (laWASecData.isDMVUser())
		{
			lbDMVUser = true;
			laWABatchData.setOfcIssuanceNo(
				caListRequest.getOfcIssuanceNo());
		}
		else
		{
			laWABatchData.setAgncyIdntyNo(
				caListRequest.getAgencyIdntyNo());
		}
		// Default to last 30 days 
		int liEndAMDate = new RTSDate().getAMDate();
		int liStartAMDate =
			liEndAMDate
				- RegistrationConstant.WEB_AGNT_MAX_SEARCH_RANGE_DIFF;

		RTSDate laSearchStartDate =
			new RTSDate(RTSDate.AMDATE, liStartAMDate);

		RTSDate laSearchEndDate =
			new RTSDate(RTSDate.AMDATE, liEndAMDate);

		if (caListRequest.getSearchEndDate() != null)
			try
			{
				laSearchEndDate.setCalendar(
					caListRequest.getSearchEndDate());

				// If End Date and no Start Date, use 
				//  predefined maximum date range  (30) 
				if (caListRequest.getSearchStartDate() == null)
				{
					laSearchStartDate =
						laSearchEndDate.add(
							RTSDate.DATE,
							-RegistrationConstant
								.WEB_AGNT_MAX_SEARCH_RANGE_DIFF);
				}
			}
			catch (Exception aeEx)
			{

			}

		if (caListRequest.getSearchStartDate() != null)
		{
			try
			{
				laSearchStartDate.setCalendar(
					caListRequest.getSearchStartDate());
			}
			catch (Exception aeEx)
			{

			}
		}
		if (caListRequest.getSearchEndDate() != null)
			try
			{
				laSearchEndDate.setCalendar(
					caListRequest.getSearchEndDate());
			}
			catch (Exception aeEx)
			{

			}
		if (caListRequest.getSearchStartDate() != null &&
					caListRequest.getSearchEndDate() != null){
			if (laSearchEndDate.getAMDate() - laSearchStartDate.getAMDate()
					> 30)
			{
				throw new RTSException(
						ErrorsConstant
						.ERR_NUM_WEBAGNT_BATCH_START_END_TOO_FAR_APART);
			}	

			laWABatchData.setSearchStartDate(laSearchStartDate);
			laWABatchData.setSearchEndDate(laSearchEndDate);
		}
	 
		laWABatchData.setBatchStatusCd(
			caListRequest.getSearchBatchStatusCd());
		// defect  xxxxx -  
		if (caListRequest.getAgencyBatchIdntyNo() > 0){
			laWABatchData.setAgncyBatchIdntyNo(caListRequest.getAgencyBatchIdntyNo());
		}
		// end defect
		WebAgencyBatch laWABatchSQL = new WebAgencyBatch(caDBA);

		Vector lvBatchSummary =
			laWABatchSQL.qryWebAgencyBatch(laWABatchData);

		Vector lvReturnBatchSummary = new Vector();

		// Restrict returned records to Maximum Defined (200)  
		boolean lbMaxReached =
			lvBatchSummary.size()
				> RegistrationConstant.WEB_AGNT_MAX_BATCH_ROWS;
		int liCount =
			Math.min(
				lvBatchSummary.size(),
				RegistrationConstant.WEB_AGNT_MAX_BATCH_ROWS);

		// Get Agency Name if greater than 0
		RtsAgncyHelper laAgncyHlpr = new RtsAgncyHelper();

		String lsAgencyName = "";
		
		if (!lbDMVUser && caListRequest.getAgencyIdntyNo() > 0)
		{
			lsAgencyName =
				laAgncyHlpr.getAgencyName(
					caListRequest.getAgencyIdntyNo(),
					caDBA);
		}

		for (int i = 0; i < liCount; i++)
		{
			laWABatchData =
				(WebAgencyBatchData) lvBatchSummary.elementAt(i);

			RtsBatchListSummaryLine laBLSL =
				new RtsBatchListSummaryLine(laWABatchData, laWASecData);

			laBLSL.setOfcName(
				OfficeIdsCache.getOfcName(laBLSL.getOfcIssuanceNo()));

			if (lbDMVUser || caListRequest.getAgencyIdntyNo() == 0)
			{
				lsAgencyName =
					laAgncyHlpr.getAgencyName(
						laWABatchData.getAgncyIdntyNo(),
						caDBA);
			}
			laBLSL.setAgncyName1(lsAgencyName);

			// Query RTS_WEB_AGNCY_TRANS for number of 
			//    Trans, Reprint, Void 
			WebAgencyTransaction laWATransSQL =
				new WebAgencyTransaction(caDBA);

			laWATransSQL.qryWebAgencyTransactionSum(laBLSL);

			// Query RTS_WEB_AGNCY_TRANS_FEE for Total Fees 
			WebAgencyTransactionFee laWATransFeeSQL =
				new WebAgencyTransactionFee(caDBA);

			laBLSL.setTotalDollars(
				(double) laWATransFeeSQL.qryWebAgencyTransactionFeeTot(
					laBLSL.getAgncyBatchIdntyNo()));

			lvReturnBatchSummary.addElement(laBLSL);
		}
		Hashtable lhtHashReturn = new Hashtable();
		lhtHashReturn.put(
			RegistrationConstant.BATCHES_ENTRY,
			lvReturnBatchSummary);
		lhtHashReturn.put(
			RegistrationConstant.MAXREACHED_ENTRY,
			new Boolean(lbMaxReached));
		return lhtHashReturn;
	}

	/** 
	 * Process Status Change Request 
	 * 
	 * @param asNewStatusCd
	 * @throws RTSException
	 */
	public void processStatusChange(String asNewStatusCd)
		throws RTSException
	{
		// Perform Validation for Status Cahnge Request 
		batchRequestValidation(asNewStatusCd);

		if (asNewStatusCd
			.equals(RegistrationConstant.APPROVED_BATCHSTATUSCD))
		{
			approveBatch();
		}
		else if (
			asNewStatusCd.equals(
				RegistrationConstant.SUBMIT_BATCHSTATUSCD))
		{
			caWABatchData = new WebAgencyBatchData();
			caWABatchData.setAgncyBatchIdntyNo(
				caListRequest.getAgencyBatchIdntyNo());
			caWABatchData.setSubmitAgntSecrtyIdntyNo(
				caWASecData.getAgntSecrtyIdntyNo());
			caWABatchData.setBatchSubmitTimestmp(new RTSDate());
			caWABatchData.setBatchStatusCd(
				RegistrationConstant.SUBMIT_BATCHSTATUSCD);

			updtBatchStatus();
		}
		else
		{   
			Log.write(Log.APPLICATION, this,"processStatusChange - " + " Error number 2300" + " - Invalid Batch status");
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
		}
	}

	/**
	 * Reprint the Batch Detail
	 *
	 * @throws RTSException
	 */
	public void reprintBatchItem() throws RTSException
	{
		WebAgencyTransactionData laWATransData =
			new WebAgencyTransactionData();

		laWATransData.setSavReqId(caDetailRequest.getSavReqId());

		// Web Agent Transaction 
		WebAgencyTransaction laWATransSQL =
			new WebAgencyTransaction(caDBA);

		Vector lvWATransData =
			laWATransSQL.qryWebAgencyTransaction(laWATransData, true);

		// Web Agent Trans Exists? 
		if (lvWATransData == null || lvWATransData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_TRANS_NOT_FOUND);
		}
		laWATransData =
			(WebAgencyTransactionData) lvWATransData.elementAt(0);

		// Caller Web Agent Security 
		WebAgentSecurityData laWASecData = new WebAgentSecurityData();
		try
		{
			laWASecData.setAgntSecrtyIdntyNo(
				Integer.parseInt(caDetailRequest.getCaller()));
		}
		catch (Exception Ex)
		{
		}
		WebAgentSecurity laWASecSQL = new WebAgentSecurity(caDBA);

		Vector lvSec =
			laWASecSQL.qryWebAgentSecurity(
				laWASecData.getAgntSecrtyIdntyNo());

		// Caller Web Agent Security Exists?			
		if (lvSec == null || lvSec.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND);
		}

		laWASecData = (WebAgentSecurityData) lvSec.elementAt(0);

		// Verify Caller has Reprint Access 
		if (laWASecData.getReprntAccs() == 1)
		{
			if (!PlateTypeCache
				.isAnnualPlt(laWATransData.getRegPltCd()))
			{
				String lsUpdtStmt = " PrntQty = PrntQty + 1 ";

				int liNumRows =
					laWATransSQL.voidReprintWebAgencyTransaction(
						laWATransData.getSavReqId(),
						lsUpdtStmt);

				if (liNumRows != 1)
				{
					throw new RTSException(
						ErrorsConstant.ERR_NUM_WEBAGNT_TRANS_NOT_FOUND);
				}
			}
		}
		else
		{
			// Caller Not Authorized to Reprint 
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);
		}

		// Web Agent Security for Transaction
		lvSec =
			laWASecSQL.qryWebAgentSecurity(
				laWATransData.getAgntSecrtyIdntyNo());

		// Transaction Agent Security Exists?	
		if (lvSec == null || lvSec.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND);
		}

		laWASecData = (WebAgentSecurityData) lvSec.elementAt(0);

		// Web Agenct Transaction Check 
		WebAgentData laWebAgntData = new WebAgentData();
		laWebAgntData.setAgntIdntyNo(laWASecData.getAgntIdntyNo());
		WebAgent laWAgntSQL = new WebAgent(caDBA);

		Vector lvWAgntData =
			laWAgntSQL.qryWebAgent(laWASecData.getAgntIdntyNo());

		if (lvWAgntData == null || lvWAgntData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AGENT_NOT_FOUND);
		}

		WebAgentData laWAgntData =
			(WebAgentData) lvWAgntData.elementAt(0);
		String lsUserName = laWAgntData.getUserName();

		// Web Agency Check
		WebAgency laWAgncySQL = new WebAgency(caDBA);
		WebAgencyData laWAgncyData = new WebAgencyData();
		laWAgncyData.setAgncyIdntyNo(laWASecData.getAgncyIdntyNo());

		Vector lvWAgncyData = laWAgncySQL.qryWebAgency(laWAgncyData);

		if (lvWAgncyData == null || lvWAgncyData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AGENCY_NOT_FOUND);
		}

		laWAgncyData = (WebAgencyData) lvWAgncyData.elementAt(0);

		// Create Subcontractor Data from Web Agency 
		SubcontractorData laSubconData = new SubcontractorData();
		laSubconData.setAddressData(laWAgncyData.getAddressData());
		laSubconData.setName1(laWAgncyData.getName1().toUpperCase());
		laSubconData.setName2(laWAgncyData.getName2().toUpperCase());

		// Collect Fees 
		WebAgencyTransactionFee laWATransFeeSQL =
			new WebAgencyTransactionFee(caDBA);

		Vector lvWATransFeeData =
			laWATransFeeSQL.qryWebAgencyTransactionFee(
				laWATransData.getSavReqId());
		
		// defect 11137 
		WebAgencyTransactionAddress laWATransAddrSQL =
			new WebAgencyTransactionAddress(caDBA);
		
		WebAgencyTransactionAddressData laWATransOwnrAddrData =  
			laWATransAddrSQL.qryWebAgencyTransactionAddr(
				laWATransData.getSavReqId(),RegistrationConstant.OWNR_ADDR_TYPE_CD);

		WebAgencyTransactionAddressData laWATransRcpntAddrData =  
			laWATransAddrSQL.qryWebAgencyTransactionAddr(
				laWATransData.getSavReqId(),RegistrationConstant.RCPNT_ADDR_TYPE_CD);
		// end defect 11137 

		// Initialize Subcontractor Renewal Data for Receipt 
		SubcontractorRenewalData laSubconRenwlData =
			new SubcontractorRenewalData(
				laWATransData,
				lvWATransFeeData);

		// Initialize Subcontractor Renewal Cache Data for Receipt 
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

		// Initialize CompleteTransactionData
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

		laCTData.setWebAgntUserName(lsUserName);

		Vector lvCTD = new Vector();
		lvCTD.add(laCTData);

		// Generate Receipt 
		GenRegistrationReceipts laGenRpt =
			new GenRegistrationReceipts();

		laGenRpt.formatReceipt(lvCTD);

		String lsReceipt =
			laGenRpt.getReceipt().toString() + ReportConstant.FF;

		JetPclProcess laJetPclProcess = new JetPclProcess();

		if (!laJetPclProcess
			.convertFile(
				lsReceipt,
				Integer.toString(laWATransData.getSavReqId())))
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_RENEWAL_RECEIPT_ERROR);
		}
	}

	/**
	 * Update Batch Status
	 * 
	 * @throws RTSException
	 */
	private void updtBatchStatus() throws RTSException
	{
		WebAgencyBatch laWABatchSQL = new WebAgencyBatch(caDBA);

		int liRows = laWABatchSQL.updWebAgencyBatch(caWABatchData);

		if (liRows != 1)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AGENCY_BATCH_NOT_FOUND);
		}
	}

	/**
	 * Void batch request.
	 *
	 * @throws RTSException
	 */
	public void voidBatchItem() throws RTSException
	{
		// Verify Record Found 
		WebAgencyTransactionData laWATransData =
			new WebAgencyTransactionData();
		laWATransData.setSavReqId(caDetailRequest.getSavReqId());
		WebAgencyTransaction laWATransSQL =
			new WebAgencyTransaction(caDBA);

		Vector lvWATransData =
			laWATransSQL.qryWebAgencyTransaction(laWATransData, true);

		if (lvWATransData == null || lvWATransData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_TRANS_NOT_FOUND);
		}
		laWATransData =
			(WebAgencyTransactionData) lvWATransData.elementAt(0);

		WebAgentSecurityData laWASecData = new WebAgentSecurityData();
		try
		{
			laWASecData.setAgntSecrtyIdntyNo(
				Integer.parseInt(caDetailRequest.getCaller()));
		}
		catch (Exception Ex)
		{
		}
		WebAgentSecurity laWASecSQL = new WebAgentSecurity(caDBA);

		Vector lvSec =
			laWASecSQL.qryWebAgentSecurity(
				laWASecData.getAgntSecrtyIdntyNo());

		// Caller Web Agent Security Exists?			
		if (lvSec == null || lvSec.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND);
		}

		laWASecData = (WebAgentSecurityData) lvSec.elementAt(0);

		// Not Voided && BatchStatusCd in (0,C,S,A)
		if (laWATransData.isAvailableForVoid())
		{
			if (laWASecData.getVoidAccs() == 1)
			{
				String lsUpdtStmt = " AgncyVoidIndi = 1 ";

				if (laWASecData.isDMVUser()
					&& laWATransData.getBatchStatusCd().equals(
						RegistrationConstant.APPROVED_BATCHSTATUSCD))
				{
					lsUpdtStmt = " CntyVoidIndi = 1 ";
				}

				lsUpdtStmt =
					lsUpdtStmt
						+ ",VoidSavReqId = "
						+ caDetailRequest.getSavReqId();

				int liNumRows =
					laWATransSQL.voidReprintWebAgencyTransaction(
						laWATransData.getSavReqId(),
						lsUpdtStmt);

				if (liNumRows != 1)
				{
					throw new RTSException(
						ErrorsConstant.ERR_NUM_WEBAGNT_TRANS_NOT_FOUND);
				}
			}
			else
			{
				// User Not Authorized to Void 
				throw new RTSException(
					ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);
			}
		}
		else
		{
			// Transaction Not Available for Void 
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_TRANS_NOT_AVAIL_TO_VOID);
		}
	}
	/**
	 * Return Details for Batch containing a specific plate and county
	 * 
	 * @param RtsBatchDetailResponse aaResponse
	 * @throws RTSException
	 */
	public void getBatchDetailForPlate(RtsBatchDetailResponse aaResponse)
		throws RTSException
	{
		// Query RTS_WEB_AGNT_SECURITY 
		WebAgentSecurityData laWASecData = new WebAgentSecurityData();
		try
		{
			laWASecData.setAgntSecrtyIdntyNo(
				Integer.parseInt(caDetailRequest.getCaller()));
		}
		catch (Exception aeEx)
		{
			// defect 11239
			Log.write(Log.APPLICATION, this, "getBatchDetailForPlate -  caller id is not an integer");
			// end defect 11239
		}

		WebAgentSecurity laWASecSQL = new WebAgentSecurity(caDBA);
		Vector lvWASecData =
			laWASecSQL.qryWebAgentSecurity(
				laWASecData.getAgntSecrtyIdntyNo());

		// Agent Security Not Found: 2324
		if (lvWASecData == null || lvWASecData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND);
		}

		laWASecData = (WebAgentSecurityData) lvWASecData.elementAt(0);

		// Agent Does Not have (Batch) Authorization: 2317 
		if (laWASecData.getBatchAccs() != 1)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);
		}

		WebAgencyTransactionData laWATransData =
			new WebAgencyTransactionData();
		laWATransData.setRegPltNo(caDetailRequest.getRegPltNo().toUpperCase());
		laWATransData.setResComptCntyNo(caDetailRequest.getOfcIssuanceNo());
		
		// Query RTS_WEB_AGNCY_TRANS for given batch 
		WebAgencyTransaction laWATransSQL =
			new WebAgencyTransaction(caDBA);

		Vector lvBatchDetail =
			laWATransSQL.qryWebAgencyTransactionForPlate(
				laWATransData);

		// No plate found for batch   -- err 2351
		if (lvBatchDetail == null || lvBatchDetail.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_NO_PLATE_FOUND_FOR_COUNTY);
		}
		RtsVehicleInfo[] larrVehInfo =
			new RtsVehicleInfo[lvBatchDetail.size()];
		
		int liTransCount = 0;

		// For all Trans 
		for (Iterator laIter = lvBatchDetail.iterator();
			laIter.hasNext();
			)
		{
			laWATransData = (WebAgencyTransactionData) laIter.next();

			RtsVehicleInfo laVehInfo =
				new RtsVehicleInfo(laWATransData);

			laVehInfo.getRegistrationData().setResComptCntyName(
				OfficeIdsCache.getOfcName(
					laVehInfo
						.getRegistrationData()
						.getResComptCntyNo()));
			
			larrVehInfo[liTransCount] = laVehInfo;
			liTransCount = liTransCount + 1;
	
			WebAgencyTransactionFee laSqlFees =
				new WebAgencyTransactionFee(caDBA);

			Vector lvFeesData =
				laSqlFees.qryWebAgencyTransactionFee(
					laWATransData.getSavReqId());

			RTSFees[] larrRtsFees = new RTSFees[lvFeesData.size()];

			int liFeeCount = 0;

			// Get the fees associated with the return
			for (Iterator laIterFees = lvFeesData.iterator();
					laIterFees.hasNext();
					)
			{
				WebAgencyTransactionFeeData laWATransFeeData =
					(WebAgencyTransactionFeeData) laIterFees.next();
	
				RTSFees laRtsFeeData = new RTSFees();
					AccountCodesData laAcctData =
						AccountCodesCache.getAcctCd(
							laWATransFeeData.getAcctItmCd(),
							new RTSDate().getYYYYMMDDDate());
	
				if (laAcctData != null)
				{
					laRtsFeeData.setFeesAcctCdDesc(
						laAcctData.getAcctItmCdDesc());
				}
				else
				{
					laRtsFeeData.setFeesAcctCdDesc(
						laWATransFeeData.getAcctItmCd());
				}
				laRtsFeeData.setItemAmt(
					new Double(
						laWATransFeeData.getItmPrice().getValue())
						.doubleValue());
				larrRtsFees[liFeeCount] = laRtsFeeData;
	
				laVehInfo.setTotalFees(
						laVehInfo.getTotalFees()
							+ laRtsFeeData.getItemAmt());
	
				liFeeCount = liFeeCount + 1;
			 
				laVehInfo.setFees(larrRtsFees);
			}
			laVehInfo.setFees(larrRtsFees);
		}
		aaResponse.setBatchDetailLines(larrVehInfo);
	}
}
