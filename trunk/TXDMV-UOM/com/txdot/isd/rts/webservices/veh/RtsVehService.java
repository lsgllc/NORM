package com.txdot.isd.rts.webservices.veh;

import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;
import com.txdot.isd.rts.webservices.veh.data.RtsVehRequest;
import com.txdot.isd.rts.webservices.veh.data.RtsVehResponse;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * RtsVehService.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	05/03/2010	Created class.
 * 							defect 10402 Ver 6.4.0
 * B Hargrove	05/13/2010	Add Plate Validity Term.
 * 							defect 10402 Ver 6.4.0
 * ---------------------------------------------------------------------
 */

/**
 * This is the Web Service for Vehicle Requests.
 *
 * @version	Ver 6.4.0			05/13/2010
 * @author	William Hargrove
 * <br>Creation Date:			03/08/2010 13:45:00
 */
public class RtsVehService
{

	/**
	 * Copy data from vehicle record to response object.
	 * 
	 * @param aaResponse RtsVehResponse
	 * @param aaVID VehicleInquiryData
	 */
	private void extractData(
		RtsVehResponse aaResponse,
		VehicleInquiryData aaVID)
	{
		if (aaVID.getMfVehicleData() != null)
		{
			// Verify RegData is not null 
			if (aaVID.getMfVehicleData().getSpclPltRegisData() != null)
			{
				try
				{
					SpecialPlatesRegisData laSpclPlt = 
						aaVID.getMfVehicleData().getSpclPltRegisData();
					OwnerData laOwnerData = 
						aaVID.getMfVehicleData().getSpclPltRegisData().
							getOwnrData();
						
					// Extract the TransId Data and set to 
					// Tracking Object 
					aaResponse.setVersionNo(0);
					aaResponse.setPlateNo(laSpclPlt.
						getRegPltNo());
					aaResponse.setManufacturingPltNo(laSpclPlt.
						getMfgPltNo());
					aaResponse.setSRId(laSpclPlt.
						getSpclRegId());
					aaResponse.setPlateCode(laSpclPlt.
						getRegPltCd());
					aaResponse.setIsaIndi(laSpclPlt.
						getISAIndi());
					aaResponse.setOrgNo(laSpclPlt.
						getOrgNo());
					aaResponse.setPlateBirthDate(laSpclPlt.
						getPltBirthDate());
					aaResponse.setManufacturingDate(laSpclPlt.
						getMFGDate());
					aaResponse.setPlateEffectiveDate(laSpclPlt.
						getRegEffDate());
					aaResponse.setPlateExpirationYear(laSpclPlt.
						getPltExpYr());
					aaResponse.setPlateExpirationMonth(laSpclPlt.
						getPltExpMo());
					aaResponse.setPltValidityTerm(laSpclPlt.
						getPltValidityTerm());
					aaResponse.setManufacturingStatusCd(laSpclPlt.
						getMFGStatusCd());
					aaResponse.setAddlSetIndi(laSpclPlt.
						getAddlSetIndi());
					aaResponse.setPltSetNo(laSpclPlt.
						getPltSetNo());
					aaResponse.setResCompCntyNo(laSpclPlt.
						getResComptCntyNo());
					aaResponse.setPlateOwnerId(laOwnerData.
						getOwnrId());
					aaResponse.setPlateOwnerNameLine1(laOwnerData.
						getName1());
					aaResponse.setPlateOwnerNameLine2(laOwnerData.
						getName2());
					aaResponse.setPlateOwnerStreetLine1(laOwnerData.
						getAddressData().getSt1());
					aaResponse.setPlateOwnerStreetLine2(laOwnerData.
						getAddressData().getSt2());
					aaResponse.setPlateOwnerCity(laOwnerData.
						getAddressData().getCity());
					aaResponse.setPlateOwnerState(laOwnerData.
						getAddressData().getState());
					aaResponse.setPlateOwnerZipCode(laOwnerData.
						getAddressData().getZpcd());
					aaResponse.setPlateOwnerZipCodeP4(laOwnerData.
						getAddressData().getZpcdp4());
					aaResponse.setPlateOwnerCountry(laOwnerData.
						getAddressData().getCntry());
					aaResponse.setPlateOwnerPhone(laSpclPlt.
						getPltOwnrPhoneNo());
					aaResponse.setPlateOwnerEMail(laSpclPlt.
						getPltOwnrEMail());
					aaResponse.setPlateOwnerDlrGdn(laSpclPlt.
						getPltOwnrDlrGDN());
					aaResponse.setPlateOwnerPhone(laSpclPlt.
						getPltOwnrPhoneNo());
					aaResponse.setDocNo(laSpclPlt.
						getSpclDocNo());
					aaResponse.setAuditTrailTrans(laSpclPlt.
						getSAuditTrailTransId());
					aaResponse.setTransEmpId(laSpclPlt.
						getTransEmpId());
				}
				catch (Exception aeEx)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Got an Exception while extracting data");
					aeEx.printStackTrace();
					aaResponse.setResult(
						String.valueOf(
							ErrorsConstant
								.ERR_NUM_V21_INVALID_REQUEST));
					aaResponse.setErrMsgNo(
						ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
				}
			}
		}
	}


	/**
	 * Formulate the GeneralSearchData object used for searching.
	 * 
	 * @param aaReq WsPLDV21ReqData
	 * @return GeneralSearchData
	 */
	private GeneralSearchData formGeneralSearchData(RtsVehRequest aaReq)
	{
		// set up the search data
		GeneralSearchData laGSD = new GeneralSearchData();
		//Initialize Variables
		laGSD.setIntKey1(0);
		laGSD.setIntKey2(0);
		laGSD.setKey1(null);
		laGSD.setKey2(null);

		// Convert I and O on RegPltNo w/in SPClientBusiness.getVeh() 
		// Set Type of search
		laGSD.setKey1(CommonConstant.REG_PLATE_NO);
		// Set Search Key
		laGSD.setKey2(aaReq.getPlateNo().toUpperCase());
		// Set TransCd
		laGSD.setKey3(TransCdConstant.SPREV);
		// Set Search SP only
		laGSD.setIntKey2(CommonConstant.SEARCH_SPECIAL_PLATES);
		return laGSD;

	}


	/**
	 * Log the request out to the rtsapp.log.
	 * 
	 * @param aaRequest
	 */
	private void logRequest(RtsVehRequest aaRequest)
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
		Log.write(
			Log.SQL_EXCP,
			this,
			"Plate No " + aaRequest.getPlateNo());
	}

	/**
	 * Log the request out to the rtsapp.log.
	 * 
	 * @param aaRequest
	 */
	private void logResponse(
		RtsVehRequest aaRequest,
		RtsVehResponse aaResponse)
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
	
	/**
	 * Receive transaction requests and stores them for future action.
	 * 
	 * @param aarrRequest
	 * @return RtsVehResponse[]
	 */
	public RtsVehResponse[] processData(RtsVehRequest[] aarrRequest)
	{
		RtsVehResponse[] larrResponse =
			new RtsVehResponse[aarrRequest.length];
		DatabaseAccess laDBA = null;

		for (int i = 0; i < aarrRequest.length; i++)
		{
			RtsServicePerformanceTracking laSPTTemp =
				new RtsServicePerformanceTracking(laDBA);
			int liSAVId =
				laSPTTemp.lookupSAVId(
					"RtsVehService",
					aarrRequest[i].getAction(),
					aarrRequest[i].getVersionNo());

			WebServiceHistoryData laRtsSrvcHstryData =
				new WebServiceHistoryData(
					liSAVId,
					aarrRequest[i].getCaller(),
					aarrRequest[i].getSessionId());

			logRequest(aarrRequest[i]);

			try
			{
				laDBA = new DatabaseAccess();
				laDBA.beginTransaction();

				// log the request
				RtsServicePerformanceTracking laSPT =
					new RtsServicePerformanceTracking(laDBA);
				laSPT.logInsert(laRtsSrvcHstryData);
				laDBA.endTransaction(DatabaseAccess.COMMIT);
				laDBA.beginTransaction();
				
				if (liSAVId > 0)
				{
					// if we did not find a valid SAVId
					// do not process the request.

					// do basic data check.
					aarrRequest[i].validateVehRequest();

					switch (aarrRequest[i].getAction())
					{
						case WebServicesActionsConstants
							.RTS_VEH_SPEC_PLT_REGIS :
							{
								RtsVehResponse laNewResponse =
									new RtsVehResponse();
								processGetVehSpclPltReg(
									aarrRequest[i],
								laNewResponse);

								larrResponse[i] = laNewResponse;

								laRtsSrvcHstryData.setErrMsgNo(
									larrResponse[i].getErrMsgNo());
									laRtsSrvcHstryData.setSuccessfulIndi(true);
								break;
							}
						default :
							{
								larrResponse[i] = null;
								break;
							}
					}
				}
				else
				{
					larrResponse[i] = null;
				}
			}
			
			catch (RTSException aeRTSEx)
			{
				System.out.println("RtsVehService had an RTSEx");
				RtsVehResponse laNewResponse = new RtsVehResponse();
				if (aeRTSEx.getCode() != 0)
				{
					laNewResponse.setErrMsgNo(aeRTSEx.getCode());
				}
				else
				{
					laNewResponse.setErrMsgNo(
						ErrorsConstant.ERR_NUM_VI_ITEM_NOT_AVAILABLE);
				}
				
 				if (aeRTSEx.getDetailMsg() != null
				  && aeRTSEx.getDetailMsg().length() > 0)
 				{
					// limit the amount of info being sent back
					if (aeRTSEx.getDetailMsg().length() > 30)
					{
					laNewResponse.setErrMsgDesc(
						aeRTSEx.getDetailMsg().substring(0, 30));
					}
					else
					{
						laNewResponse.setErrMsgDesc(
							aeRTSEx.getDetailMsg());
					}
 				}
					  
				laNewResponse.setPlateNo("");
				larrResponse[i] = laNewResponse;

				laRtsSrvcHstryData.setSuccessfulIndi(false);
				laRtsSrvcHstryData.setErrMsgNo(
					laNewResponse.getErrMsgNo());
			}
			
			catch (Exception aeEx)
			{
				System.out.println(
					"RtsVehService had an exception during request");
			}

			logResponse(aarrRequest[i], larrResponse[i]);

			try
			{
				if (laDBA != null)
				{
					if (!laDBA.isConnected())
					{
						laDBA.beginTransaction();
					}

					// write out the update to the tracking row.
					RtsServicePerformanceTracking laSPT =
						new RtsServicePerformanceTracking(laDBA);
					laSPT.logUpdate(laRtsSrvcHstryData);

					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
			}
			catch (RTSException aeRTSEx)
			{
				System.out.println(
					"RtsVehService had a problem closing the db");
			}
		}
		return larrResponse;
	}

	/**
	 * Process Get Vehicle request.
	 * 
	 * @param aaRequest
	 * @param aaResponse
	 */
	private void processGetVehSpclPltReg(
		RtsVehRequest aaRequest,
		RtsVehResponse aaResponse)
	{
		// default to not found
		aaResponse.setErrMsgNo(
			ErrorsConstant.ERR_NUM_V21_NOT_FOUND);

		GeneralSearchData laSearchData = formGeneralSearchData(aaRequest);
		CommonServerBusiness laCSB = new CommonServerBusiness();
		try
		{
			Object laVehicle =
				laCSB.processData(
					GeneralConstant.COMMON,
					CommonConstant.GET_VEH,
					laSearchData);

			if (laVehicle instanceof VehicleInquiryData)
			{
				VehicleInquiryData laVID =
					(VehicleInquiryData) laVehicle;

				// Remove reference to partials as is populated even
				// when latest RegData is selected.
				if (laVID.getNoMFRecs() == 1)
				{
					aaResponse.setErrMsgNo(0);
					// We have just one record to work with
					// We have just one record to work with
					// and it matches RegExpMo\Yr						
					extractData(
						aaResponse,
						laVID);
				}
				else if (laVID.getNoMFRecs() < 1)
				{
					// There was no record found
					aaResponse.setResult(
						String.valueOf(
							ErrorsConstant.ERR_NUM_V21_NOT_FOUND));
					aaResponse.setErrMsgNo(
						ErrorsConstant.ERR_NUM_V21_NOT_FOUND);
				}
				else
				{
					// TODO Not ready for multiple recs yet
					aaResponse.setResult(
						String.valueOf(
							ErrorsConstant.ERR_NUM_V21_MULTIPLE_RECS));
					aaResponse.setErrMsgNo(
						ErrorsConstant.ERR_NUM_V21_MULTIPLE_RECS);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Got an exception on vehicle lookup");
			aeRTSEx.printStackTrace();

			// send back invalid request
			aaResponse.setResult(
				String.valueOf(
					ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST));
			aaResponse.setErrMsgNo(
				ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
		}
	}

}
