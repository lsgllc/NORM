package com.txdot.isd.rts.webservices.ren;

import java.util.Vector;

import com
	.txdot
	.isd
	.rts
	.webservices
	.common
	.RtsServicePerformanceTracking;
import com
	.txdot
	.isd
	.rts
	.webservices
	.common
	.data
	.WebServicesActionsConstants;
import com.txdot.isd.rts.webservices.ren.data.RtsRenewalRequest;
import com.txdot.isd.rts.webservices.ren.data.RtsRenewalResponse;
import com.txdot.isd.rts.webservices.ren.util.RtsRenHelper;

import com.txdot.isd.rts.services.cache.ErrorMessagesCache;
import com.txdot.isd.rts.services.data.WebAgentSecurityData;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.WebAgencyAuth;
import com.txdot.isd.rts.server.db.WebAgentSecurity;
import com.txdot.isd.rts.server.db.WebServicesRenewalLookupHistory;

/*
 * RtsRenLookupService.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	11/29/2010	Initial load.
 * 							defect 10670 Ver 6.7.0
 * K Harrell	03/11/2011	Add validation
 * 							add validateRequest(), 
 * 								isvalidateRequestParmLength()
 * 							defect 10768 Ver 6.7.1
 * Min Wang		03/21/2011	using constant ERR_MSG_MAX_LNG_WEB_SERVICES
 * 							for err msg 
 * 							defect 10670 Ver 6.7.1
 * K Harrell	03/22/2011  add SEPARATOR
 * 							add getErrMsgDetail()  
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	04/05/2011	modify processData() 
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	04/11/2011	introduce new error handling, constants 
 * 							modify processData(), validateRequest() 
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	04/24/2011	Implement new RtsRenHelper constructor. 
 * 							modify processGetRenewalRecord()
 * 							defect 10768 Ver 6.7.1  
 * K Harrell	06/27/2011	Allow empty Last4VIN 	
 * 							modify validateRequest() 
 * 							defect 10768 Ver 6.8.0 
 * K McKee 		10/06/2011  Added logging for Error 3400
 *                          defect 10729  Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * The web service to do Record Lookup for Web Renewal Requests.
 *
 * @version	6.8.0			06/27/2011
 * @author	Ray Rowehl
 * @author  Kathy Harrell 
 * <br>Creation Date:		11/29/2010 15:43:27
 */
public class RtsRenLookupService
{
	private final static String SEPARATOR = ", ";

	/**
	 * Get Error Message Detail 
	 * 
	 * @param aaRequest RtsRenewalRequest
	 * @return String 
	 */
	private String getErrMsgDetail(RtsRenewalRequest aaRequest)
	{
		return "WebAgent Renewal Lookup Invalid Data: "
			+ "SavReqId: "
			+ aaRequest.getRequestIdntyNo()
			+ SEPARATOR
			+ "DocNo: "
			+ aaRequest.getDocNo()
			+ SEPARATOR
			+ "Last4VIN: "
			+ aaRequest.getLast4OfVin()
			+ SEPARATOR
			+ "PlateNo: "
			+ aaRequest.getRegPltNo();
	}

	/** 
	 * Is Valid Request Parmeter Length for RTS_SRVC_WEB_REN_LOOKUP_HSTRY
	 * 
	 * @param aaRequest	RtsRenewalRequest
	 * @return boolean 
	 */
	private boolean isValidRequestParmLength(RtsRenewalRequest aaRequest)
	{
		boolean lbValid = true;

		if (aaRequest.getDocNo() != null)
		{
			aaRequest.setDocNo(
				aaRequest.getDocNo().trim().toUpperCase());
		}
		if (aaRequest.getRegPltNo() != null)
		{
			aaRequest.setRegPltNo(
				aaRequest.getRegPltNo().trim().toUpperCase());
		}
		if (aaRequest.getLast4OfVin() != null)
		{
			aaRequest.setLast4OfVin(
				aaRequest.getLast4OfVin().trim().toUpperCase());
		}

		lbValid =
			(aaRequest.getLast4OfVin() == null
				|| aaRequest.getLast4OfVin().length() <= 4)
				&& (aaRequest.getDocNo() == null
					|| aaRequest.getDocNo().length()
						<= CommonConstant.LENGTH_DOCNO)
				&& (aaRequest.getRegPltNo() == null
					|| aaRequest.getRegPltNo().length()
						<= CommonConstant.LENGTH_PLTNO);

		if (!lbValid)
		{
			System.out.println(getErrMsgDetail(aaRequest));
		}

		return lbValid;
	}

	/**
	 * Process Lookup Requests
	 * 
	 * @param aarrRequest RtsRenewalRequest[]
	 * @return RtsRenewalResponse[]
	 */
	public RtsRenewalResponse[] processData(RtsRenewalRequest[] aarrRequest)
	{
		RtsRenewalResponse[] larrResponse =
			new RtsRenewalResponse[aarrRequest.length];

		DatabaseAccess laDBA = null;

		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsServicePerformanceTracking laSPTTemp =
				new RtsServicePerformanceTracking(laDBA);

			int liSAVId =
				laSPTTemp.lookupSAVId(
					"RtsRenLookupService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			WebServiceHistoryData laRtsSrvcHstryData =
				new WebServiceHistoryData(
					liSAVId,
					aarrRequest[i].getCaller(),
					aarrRequest[i].getSessionId());

			RtsRenewalResponse laNewResponse = new RtsRenewalResponse();

			try
			{
				laDBA = new DatabaseAccess();

				// UOW #1 BEGIN 
				laDBA.beginTransaction();

				// Log request to RTS_SRVC_HSTRY 
				RtsServicePerformanceTracking laSPT =
					new RtsServicePerformanceTracking(laDBA);
				laSPT.logInsert(laRtsSrvcHstryData);
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				// UOW #1 END

				// UOW #2 BEGIN 
				laDBA.beginTransaction();

				switch (aarrRequest[i].getAction())
				{
					case WebServicesActionsConstants.RTS_REN_DOCNO :
						{
							// intentionally dropping through
						}
					case WebServicesActionsConstants
						.RTS_REN_PLATE_LOOKUP :
						{
							// Capture the SavReqId for tracking
							if (aarrRequest[i].getRequestIdntyNo() < 1)
							{
								aarrRequest[i].setRequestIdntyNo(
									laRtsSrvcHstryData.getSavReqId());

							}

							// Can not insert if invalid lengths 
							if (
								!isValidRequestParmLength(aarrRequest[i]))
							{
								throw new RTSException(
									ErrorsConstant
										.ERR_NUM_WEBAGNT_INVALID_LOOKUP_REQUEST);
							}

							WebServicesRenewalLookupHistory laSRLH =
								new WebServicesRenewalLookupHistory(laDBA);
							laSRLH.insWebServicesRenewalLookupHistory(
								aarrRequest[i]);
							laDBA.endTransaction(DatabaseAccess.COMMIT);
							// UOW #2 END 

							// UOW #3 BEGIN
							laDBA.beginTransaction();

							processGetRenewalRecord(
								aarrRequest[i],
								laNewResponse,
								laDBA);
							break;
						}
					default :
						{
							Log.write(Log.APPLICATION, this, "processData -  ******  ERROR - 2300 ****  invalid Action Type");
							throw new RTSException(
								ErrorsConstant
									.ERR_NUM_WEBAGNT_GENERAL_ERROR);
						}
				}

				laNewResponse.setErrMsgNo(0);
			}
			catch (RTSException aeRTSEx)
			{
				laNewResponse.setErrMsgNo(
					aeRTSEx.getCode() == 0
						? ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR
						: aeRTSEx.getCode()); 
						
			}
			catch (Exception aeEx)
			{
				laNewResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);
				Log.write(Log.APPLICATION, this, "processData - Exception  ******  ERROR - 2300 ****   " + aeEx.toString());
			}

			if (laNewResponse.getErrMsgNo() != 0)
			{
				laNewResponse.setErrMsgDesc(
					ErrorMessagesCache.getErrMsgDesc(
						laNewResponse.getErrMsgNo()));
			}

			larrResponse[i] = laNewResponse;
			laRtsSrvcHstryData.setSuccessfulIndi(
				laNewResponse.getErrMsgNo() == 0);
			laRtsSrvcHstryData.setErrMsgNo(laNewResponse.getErrMsgNo());

			try
			{
				if (laDBA != null)
				{
					if (!laDBA.isConnected())
					{
						laDBA.beginTransaction();
					}

					// Update request status in RTS_SRVC_HSTRY 
					RtsServicePerformanceTracking laSPT =
						new RtsServicePerformanceTracking(laDBA);
					laSPT.logUpdate(laRtsSrvcHstryData);
					laDBA.endTransaction(DatabaseAccess.COMMIT);
					// UOW #2/3 END  
				}
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(Log.APPLICATION, this, "processData -  had a problem closing the dba " ); 
			}
		}
		return larrResponse;
	}

	/**
	 * Validate Request per Security / Web Agency Authorization 
	 * Call RtsRenHelper.processGetVehReq() to retrieve vehicle and 
	 *  perform further validation.   
	 * Return record to caller.  
	 * 
	 * @param aaRequest		RtsRenewalRequest
	 * @param aaResponse	RtsRenewalResponse
	 * @param aaDBA			DatabaseAccess
	 * @throws RTSException 
	 */
	private void processGetRenewalRecord(
		RtsRenewalRequest aaRequest,
		RtsRenewalResponse aaResponse,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Vector lvWAAuthCfg = validateRequest(aaRequest, aaDBA);

		RtsRenHelper laHelper =
			new RtsRenHelper(aaRequest, aaResponse, lvWAAuthCfg, aaDBA);

		laHelper.processGetVehReq();
	}

	/** 
	 * If valid data, return Vector from RTS_WEB_AGNCY_AUTH 
	 *
	 * @param aaRequest	RtsRenewalRequest
	 * @param aaDBA		RtsRenewalRequest
	 * @throws RTSException 
	 */
	private Vector validateRequest(
		RtsRenewalRequest aaRequest,
		DatabaseAccess aaDBA)
		throws RTSException
	{
		Vector lvWAAuthCfg = new Vector();

		String lsKeyTypeCd = aaRequest.getKeyTypeCd();

		// Validate Required KeyTypeCd and Associated Data 
		// - K && non-empty(RegPltNo) && empty DocNo
		// - S && non-empty DocNo && empty(RegPltNo,Last4Vin)
		if (!((lsKeyTypeCd.equals(RegistrationConstant.KEYED_KEYTYPECD)
			&& !UtilityMethods.isEmpty(aaRequest.getRegPltNo())
			&& UtilityMethods.isEmpty(aaRequest.getDocNo()))
			|| (lsKeyTypeCd.equals(RegistrationConstant.SCAN_KEYTYPECD)
				&& !UtilityMethods.isEmpty(aaRequest.getDocNo())
					& UtilityMethods.isEmpty(aaRequest.getRegPltNo())
				&& UtilityMethods.isEmpty(aaRequest.getLast4OfVin()))))
		{
			// Invalid Lookup Request:  2323 
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_INVALID_LOOKUP_REQUEST);
		}

		// Validate Agent Security
		int liAgntSecrtyIdntyNo = 0;
		try
		{
			liAgntSecrtyIdntyNo =
				Integer.parseInt(aaRequest.getCaller());
		}
		catch (NumberFormatException aeNFEx)
		{
		}
		WebAgentSecurity laWASecuritySQL = new WebAgentSecurity(aaDBA);

		Vector lvWASecData =
			laWASecuritySQL.qryWebAgentSecurity(liAgntSecrtyIdntyNo);

		// Agent Security Not Found: 2324
		if (lvWASecData == null || lvWASecData.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant
					.ERR_NUM_WEBAGNT_AGENT_SECURITY_NOT_FOUND);
		}
		WebAgentSecurityData laWASecurityData =
			(WebAgentSecurityData) lvWASecData.elementAt(0);

		// Agent Does Not have (Renewal) Authorization: 2317 
		if (laWASecurityData.getRenwlAccs() != 1)
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_AUTHORIZATION_ERR);
		}

		WebAgencyAuth laWebAgncyAuthSQL = new WebAgencyAuth(aaDBA);

		lvWAAuthCfg =
			laWebAgncyAuthSQL.qryWebAgencyAuthAndCfg(
				laWASecurityData.getAgncyIdntyNo());

		// No Web Agency Authorization Found: 2325  
		if (lvWAAuthCfg == null || lvWAAuthCfg.isEmpty())
		{
			throw new RTSException(
				ErrorsConstant.ERR_NUM_WEBAGNT_NO_WEB_AGENCY_AUTH);
		}
		return lvWAAuthCfg;
	}
}
