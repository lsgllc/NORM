package com.txdot.isd.rts.webservices.dsabldplcrd;

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
	.dsabldplcrd
	.data
	.RtsDsabldPlcrdRequest;
import com
	.txdot
	.isd
	.rts
	.webservices
	.dsabldplcrd
	.data
	.RtsDsabldPlcrdResponse;

import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.MiscellaneousRegConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.DisabledPlacard;
import com.txdot.isd.rts.server.db.WebServicesDisabledPlacardHistory;

/*
 * RtsDsabldPlcrdService.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/24/2010	Created 
 * 							defect 10607 Ver 6.6.0 
 * K Harrell	10/03/2010	add logRequest(), logResult() 
 * 							defect 10607 Ver 6.6.0 
 * K Harrell	10/23/2010	Return no more than 5 rows (per Roy) 
 * 							modify getDsabldPlcrd()
 * 							defect 10607 Ver 6.6.0 
 * K Harrell	11/09/2010	The array size should not exceed 5 
 * 							modify getDsabldPlcrd()
 * 							defect 10607 Ver 6.6.0  
 * K Harrell	11/02/2011	Correct typo.  (2012 to 2102) 
 * ---------------------------------------------------------------------
 */

/**
 * This is the Web Service for Disabled Placard Requests
 *
 * @version	6.9.0 		11/02/2011
 * @author	Kathy Harrell
 * <br>Creation Date:	09/24/2010	12:38:17
 */
public class RtsDsabldPlcrdService
{
	/**
	 * RtsDsabldPlcrdService.java Constructor
	 * 
	 */
	public RtsDsabldPlcrdService()
	{
		super();
	}

	public RtsDsabldPlcrdResponse[] getDsabldPlcrdA(
		int aiAction,
		String asCaller,
		String asSessionId,
		int aiVersionNo,
		String asInvItmNo)
	{
		RtsDsabldPlcrdRequest laReqData = new RtsDsabldPlcrdRequest();
		laReqData.setAction(aiAction);
		laReqData.setCaller(asCaller);
		laReqData.setSessionId(asSessionId);
		laReqData.setVersionNo(aiVersionNo);
		laReqData.setInvItmNo(asInvItmNo);
		RtsDsabldPlcrdRequest[] larrRequest =
			new RtsDsabldPlcrdRequest[1];
		larrRequest[0] = laReqData;
		return getDsabldPlcrd(larrRequest);
	}

	/**
	 * Perform the requested service 
	 * 
	 * @param aarrRequest 
	 * @return RtsDsabldPlcrdResponse[]
	 */
	public RtsDsabldPlcrdResponse[] getDsabldPlcrd(RtsDsabldPlcrdRequest[] aarrRequest)
	{
		System.out.println("Entered getDsabldPlcrd()");

		// Initialize to 1 element 
		RtsDsabldPlcrdResponse[] larrResponse =
			new RtsDsabldPlcrdResponse[1];

		// We assume that this is ONE request 
		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsDsabldPlcrdRequest laRqstData = aarrRequest[i];

			// If Invalid Data, throw it back w/ 2101 
			//    w/ No Logging
			int liErrMsgNo = 2101;

			if (laRqstData.isValidRequest())
			{
				DatabaseAccess laDBA = new DatabaseAccess();

				RtsServicePerformanceTracking laSPTTrack =
					new RtsServicePerformanceTracking(laDBA);

				int liSAVId =
					laSPTTrack.lookupSAVId(
						"RtsDsabldPlcrdService",
						laRqstData.getAction(),
						laRqstData.getVersionNo());

				WebServiceHistoryData laRtsSrvcHstryData =
					new WebServiceHistoryData(
						liSAVId,
						laRqstData.getCaller(),
						laRqstData.getSessionId());

				// UOW #1, #2 
				boolean lbDBLog =
					logRequest(laRtsSrvcHstryData, laRqstData, laDBA);
				// END UOW #1, #2 

				Vector lvResult = new Vector();
				String lsInvItmNo = laRqstData.getInvItmNo();
				int liNumRows = 0;

				// UOW #3 
				try
				{
					DisabledPlacard laDsabldPlcrdSQL =
						new DisabledPlacard(laDBA);
					laDBA.beginTransaction();
					lvResult =
						laDsabldPlcrdSQL.qryDisabledPlacardWeb(
							lsInvItmNo);
					laDBA.endTransaction(DatabaseAccess.COMMIT);

					if (lvResult != null && !lvResult.isEmpty())
					{
						liNumRows = lvResult.size();
						liErrMsgNo = 0;
					}
					else
					{
						//liErrMsgNo = 2012;
						liErrMsgNo = 2102;
					}
				}
				catch (RTSException aeRTSEx)
				{
					try
					{
						laDBA.endTransaction(DatabaseAccess.ROLLBACK);
					}
					catch (Exception aeEx)
					{
					}
					System.err.println(
						"Problem with RtsDsabldPlcrdService");
					aeRTSEx.printStackTrace();
					liErrMsgNo = 2101;
				}
				// END UOW #3 

				if (lvResult != null
					&& !lvResult.isEmpty()
					&& liErrMsgNo == 0)
				{
					int liReturnRows =
						Math.min(
							lvResult.size(),
							MiscellaneousRegConstant
								.MAX_DP_ROWS_FOR_CICS);

					larrResponse =
						new RtsDsabldPlcrdResponse[liReturnRows];

					for (int j = 0; j < liReturnRows; j++)
					{
						RtsDsabldPlcrdResponse laData =
							(
								RtsDsabldPlcrdResponse) lvResult
									.elementAt(
								j);

						larrResponse[j] = laData;
					}
				}
				else
				{
					larrResponse[0] =
						new RtsDsabldPlcrdResponse(
							lsInvItmNo,
							liErrMsgNo);
				}

				if (lbDBLog)
				{
					laRtsSrvcHstryData.setSuccessfulIndi(
						liErrMsgNo == 0);

					laRtsSrvcHstryData.setErrMsgNo(liErrMsgNo);

					// UOW #4 
					logResult(laRtsSrvcHstryData, laDBA, liNumRows);
					// END UOW #4 
				}
			}
			else
			{
				larrResponse[0] =
					new RtsDsabldPlcrdResponse(
						laRqstData.getInvItmNo(),
						liErrMsgNo);
			}
		}
		return larrResponse;
	}

	/**
	 * Log Request
	 *
	 * @param aaRtsSrvcHstryData
	 * @param aarrRequest 
	 * @param aaDBA 
	 * @return boolean 
	 */
	private boolean logRequest(
		WebServiceHistoryData aaRtsSrvcHstryData,
		RtsDsabldPlcrdRequest aarrRequest,
		DatabaseAccess aaDBA)
	{
		boolean lbSuccessful = false;
		try
		{
			aaDBA.beginTransaction();

			// Log the request to RTS_SRVC_HSTRY
			// UOW 
			RtsServicePerformanceTracking laSPT =
				new RtsServicePerformanceTracking(aaDBA);
			laSPT.logInsert(aaRtsSrvcHstryData);
			aaDBA.endTransaction(DatabaseAccess.COMMIT);
			// END UOW  

			// Log the InvItmNo to RTS_SRVC_DSABLD_PLCRD_HSTRY
			// UOW  
			aaDBA.beginTransaction();
			WebServicesDisabledPlacardHistory laWebSrvcDsabldPlcrdHist =
				new WebServicesDisabledPlacardHistory(aaDBA);
			laWebSrvcDsabldPlcrdHist
				.insWebServicesDisabledPlacardHistory(
				aaRtsSrvcHstryData.getSavReqId(),
				aarrRequest);
			aaDBA.endTransaction(DatabaseAccess.COMMIT);
			// END UOW   

			lbSuccessful = true;
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				aaDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (Exception aeEx)
			{

			}
		}
		return lbSuccessful;

	}
	/**
	 * Log Result
	 * 
	 * @param aaDBA 
	 */
	private void logResult(
		WebServiceHistoryData aaRtsSrvcHstryData,
		DatabaseAccess aaDBA,
		int aiNumRows)
	{

		try
		{
			aaDBA.beginTransaction();

			// Update RTS_SRVC_HSTRY 
			RtsServicePerformanceTracking laSPT =
				new RtsServicePerformanceTracking(aaDBA);
			laSPT.logUpdate(aaRtsSrvcHstryData);

			// Update RTS_SRVC_DSABLD_PLCRD_HSTRY
			WebServicesDisabledPlacardHistory laWebSrvcDsabldPlcrdHist =
				new WebServicesDisabledPlacardHistory(aaDBA);
			laWebSrvcDsabldPlcrdHist
				.updWebServicesDisabledPlacardHistory(
				aaRtsSrvcHstryData.getSavReqId(),
				aiNumRows);
			aaDBA.endTransaction(DatabaseAccess.COMMIT);
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println(
				"RtsDsabldPlcrdService had a problem closing the dba");
			try
			{
				aaDBA.endTransaction(DatabaseAccess.ROLLBACK);
			}
			catch (RTSException aeRTSEx1)
			{
			}
		}

	}
}