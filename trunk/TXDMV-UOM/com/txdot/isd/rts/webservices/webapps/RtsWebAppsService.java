package com.txdot.isd.rts.webservices.webapps;

import java.util.Vector;

import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.RtsTrackingData;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;
import com.txdot.isd.rts.webservices.webapps.data.RtsWebAppsRequest;
import com.txdot.isd.rts.webservices.webapps.data.RtsWebAppsResponse;

import com.txdot.isd.rts.services.data.SpecialPlateItrntTransData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.InternetSpecialPlateTransaction;

/*
 * RtsWebAppsService.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		07/03/2008	New Class 
 * 							defect 9676 Ver MyPlates_POS
 * Min Wang		07/22/2008  modify processData()
 * 							defect 9676 Ver MyPlates_POS
 * Ray Rowehl	01/26/2009	Add a check to ensure the connection is 
 * 							still active before attempting to log
 * 							the response.
 * 							modify processData()
 * 							defect 9804 Ver Defect_POS_D
 * K Harrell	07/12/2009	Implement new OwnerData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	02/09/2010 	implement PltValidityTerm vs. PltTerm
 * 							modify processSpAppTransQuery()
 * 							defect 10366 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This is the Web Service for WebApps Requests.
 *
 * @version	POS_640			02/09/2010
 * @author	Min Wang
 * <br>Creation Date:		07/03/2008 02:50:00
 */
public class RtsWebAppsService
{

	/**
	 * Receive transaction requests and stores them for future action.
	 * 
	 * @param aarrRequest
	 * @return RtsTransResponse[]
	 */
	public RtsWebAppsResponse[] processData(RtsWebAppsRequest[] aarrRequest)
	{
		RtsWebAppsResponse[] larrResponse =
			new RtsWebAppsResponse[aarrRequest.length];
		DatabaseAccess laDBA = null;

		for (int i = 0; i < aarrRequest.length; i++)
		{

			RtsServicePerformanceTracking laSPTtemp =
				new RtsServicePerformanceTracking(laDBA);
			int liSAVId =
				laSPTtemp.lookupSAVId(
					"RtsWebAppsService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			RtsTrackingData laRtsTrackingData =
				new RtsTrackingData(
					liSAVId,
					aarrRequest[i].getCaller(),
					aarrRequest[i].getSessionId());

			try
			{

				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();

				// log the request
				RtsServicePerformanceTracking laSPT =
					new RtsServicePerformanceTracking(laDBA);
				laSPT.logInsert(laRtsTrackingData);

				switch (aarrRequest[i].getAction())
				{
					case WebServicesActionsConstants
						.RTS_WEBAPPS_GET_SP_APP_TRANS :
						{
							RtsWebAppsResponse laNewResponse =
								new RtsWebAppsResponse();
							processSpAppTransQuery(
								aarrRequest[i],
								laNewResponse,
								laDBA);

							larrResponse[i] = laNewResponse;

							logRequest(aarrRequest[i], laNewResponse);

							laRtsTrackingData.setErrMsgNo(
								larrResponse[i].getErrMsgNo());
							laRtsTrackingData.setSuccessful(true);
							break;
						}
					default :
						{
							larrResponse[i] = null;
							break;
						}
				}

			}
			catch (RTSException aeRTSEx)
			{
				System.out.println(
					"RtsWebAppsService had a problem closing the dba");
			}

			try
			{
				if (laDBA != null)
				{
					// defect 9804
					if (!laDBA.isConnected())
					{
						laDBA.beginTransaction();
					}
					// end 9804

					// write out the update to the tracking row.
					RtsServicePerformanceTracking laSPT =
						new RtsServicePerformanceTracking(laDBA);
					laSPT.logUpdate(laRtsTrackingData);

					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
			}
			catch (RTSException aeRTSEx)
			{
				System.out.println(
					"RtsWebAppsService had a problem closing the dba");
			}
		}

		return larrResponse;
	}

	/**
	 * Process Special Plate Transaction Query.
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 * @param aaDBA
	 */
	private void processSpAppTransQuery(
		RtsWebAppsRequest aaRequest,
		RtsWebAppsResponse aaResponse,
		DatabaseAccess aaDBA)
	{

		try
		{
			InternetSpecialPlateTransaction laISPT =
				new InternetSpecialPlateTransaction(aaDBA);

			Vector lvSpAppTrans =
				laISPT.qryTransactionRecord(
					aaRequest.getItrntTraceNo());

			if (lvSpAppTrans != null && lvSpAppTrans.size() > 0)
			{
				aaResponse.setErrMsgNo(0);
				SpecialPlateItrntTransData laSPITD =
					(
						SpecialPlateItrntTransData) lvSpAppTrans
							.elementAt(
						0);

				aaResponse.setAuditTrailTransId(
					laSPITD.getAuditTrailTransId());
				aaResponse.setItrntTranceNo(laSPITD.getItrntTraceNo());
				aaResponse.setMfgPltNo(laSPITD.getMfgPltNo());
				aaResponse.setOrgNo(laSPITD.getOrgNo());
				aaResponse.setPltOwnrEmail(laSPITD.getPltOwnrEmail());
				aaResponse.setPltOwnrPhone(laSPITD.getPltOwnrPhone());
				aaResponse.setPymntOrderId(laSPITD.getPymntOrderId());
				aaResponse.setRegPltCd(laSPITD.getRegPltCd());
				aaResponse.setRegPltNo(laSPITD.getRegPltNo());
				aaResponse.setReqIpAddr(laSPITD.getReqIPAddr());
				aaResponse.setTransEmpId(laSPITD.getTransEmpID());
				aaResponse.setTransStatusCd(laSPITD.getTransStatusCd());
				aaResponse.setEpayRcveTimestmp(
					laSPITD.getEpayRcveTimeStmpCal());
				aaResponse.setEpaySendTimestmp(
					laSPITD.getEpaySendTimeStmpCal());
				aaResponse.setInitReqTimestmp(
					laSPITD.getInitReqTimeStmpCal());
				aaResponse.setUpdtTimestmp(
					laSPITD.getUpdtTimeStmpCal());
				aaResponse.setPltExpMo(laSPITD.getPltExpMo());
				aaResponse.setPltExpYr(laSPITD.getPltExpYr());
				aaResponse.setResComptCntyNo(
					laSPITD.getResComptCntyNo());
				// defect 10112 
				aaResponse.setPltOwnrName1(
					laSPITD.getOwnerData().getName1());
				aaResponse.setPltOwnrName2(
					laSPITD.getOwnerData().getName2());
				aaResponse.setPltOwnrSt1(
					laSPITD.getOwnerData().getAddressData().getSt1());
				aaResponse.setPltOwnrSt2(
					laSPITD.getOwnerData().getAddressData().getSt2());
				aaResponse.setPltOwnrCity(
					laSPITD.getOwnerData().getAddressData().getCity());
				aaResponse.setPltOwnrState(
					laSPITD.getOwnerData().getAddressData().getState());
				aaResponse.setPltOwnrZpCd(
					laSPITD.getOwnerData().getAddressData().getZpcd());
				aaResponse.setPltOwnrZpCd4(
					laSPITD
						.getOwnerData()
						.getAddressData()
						.getZpcdp4());
				// end defect 10112 
				aaResponse.setIsaIndi(laSPITD.isISAIndi());
				aaResponse.setAddlSetIndi(laSPITD.isAddlSetIndi());
				aaResponse.setPymntAmt(
					new Dollar(laSPITD.getPymntAmt()));
				aaResponse.setItrntPymntStatusCd(
					laSPITD.getItrntPymntStatusCd());
					
				// defect 10366 
				//aaResponse.setPltTerm(laSPITD.getPltTerm());
				aaResponse.setPltTerm(laSPITD.getPltValidityTerm());
				// end defect 10366 
				
				aaResponse.setReserveIndi(laSPITD.getFromReserveIndi());

			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
			}
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getCode() != 0)
			{
				aaResponse.setErrMsgNo(aeRTSEx.getCode());
			}
			else
			{
				aaResponse.setErrMsgNo(
					ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
			}
			if (aeRTSEx.getDetailMsg() != null)
			{
				aaResponse.setErrMsgDesc(aeRTSEx.getDetailMsg());
			}
		}
	}

	/**
	 * Log the request out to the rtsapp.log.
	 * 
	 * @param aaRequest
	 */
	private void logRequest(
		RtsWebAppsRequest aaRequest,
		RtsWebAppsResponse aaResponse)
	{
		// write the request to the log
		Log.write(
			Log.SQL_EXCP,
			this,
			"=======================================");
		Log.write(
			Log.SQL_EXCP,
			this,
			"Action  " + aaRequest.getAction());
		Log.write(
			Log.SQL_EXCP,
			this,
			"Version " + aaRequest.getVersionNo());
		Log.write(
			Log.SQL_EXCP,
			this,
			"Caller  " + aaRequest.getCaller());
		Log.write(
			Log.SQL_EXCP,
			this,
			"Session " + aaRequest.getSessionId());

		Log.write(Log.SQL_EXCP, this, "-----------------------------");

		if (aaResponse != null)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"MSG NO  " + aaResponse.getErrMsgNo());
			if (aaResponse.getErrMsgDesc() != null
				&& aaResponse.getErrMsgDesc().trim().length() > 0)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"MSG DESC " + aaResponse.getErrMsgDesc());
			}
		}
		else
		{
			Log.write(Log.SQL_EXCP, this, "Response is NULL");
		}
	}

}
