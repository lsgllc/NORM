package com.txdot.isd.rts.webservices.veh;import java.util.Vector;import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;import com.txdot.isd.rts.webservices.veh.data.RtsVehIndicator;import com.txdot.isd.rts.webservices.veh.data.RtsVehPublicRequest;import com.txdot.isd.rts.webservices.veh.data.RtsVehPublicResponse;import com.txdot.isd.rts.services.data.*;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.CommonValidations;import com.txdot.isd.rts.services.util.Log;import com.txdot.isd.rts.services.util.constants.CommonConstant;import com.txdot.isd.rts.services.util.constants.ErrorsConstant;import com.txdot.isd.rts.services.util.constants.GeneralConstant;import com.txdot.isd.rts.services.util.constants.TransCdConstant;import com.txdot.isd.rts.server.common.business.CommonServerBusiness;import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;/* * RtsVehPublicService.java * * (c) Texas Department of Transportation 2010 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	12/13/2010	Created  * 							defect 10684 Ver 6.7.0  * --------------------------------------------------------------------- *//** * This is the Public Web Service for Vehicle Requests. * * @version	6.7.0			12/13/2010	 * @author	Kathy Harrell * <br>Creation Date:		12/13/2010	11:35:17 *//* &RtsVehPublicService& */public class RtsVehPublicService{	/**	 * RtsVehPublicService.java Constructor	 * 	 *//* &RtsVehPublicService.RtsVehPublicService& */	public RtsVehPublicService()	{		super();	}	/**	 * Copy data from vehicle record to response object.	 * 	 * @param aaResponse RtsVehPublicResponse	 * @param aaVID VehicleInquiryData	 *//* &RtsVehPublicService.extractData& */	private void extractData(		RtsVehPublicResponse aaResponse,		VehicleInquiryData aaVID)	{		if (aaVID.getMfVehicleData() != null)		{			getIndicatorArray(aaResponse, aaVID);		}		try		{			TitleData laTtlData =				aaVID.getMfVehicleData().getTitleData();			if (laTtlData != null)			{				aaResponse.setTtlIssueDate(laTtlData.getTtlIssueDate());				aaResponse.setDocNo(laTtlData.getDocNo());			}			VehicleData laVehData =				aaVID.getMfVehicleData().getVehicleData();			if (laVehData != null)			{				aaResponse.setVIN(laVehData.getVin());				aaResponse.setVehMk(laVehData.getVehMk());				aaResponse.setVehModl(laVehData.getVehModl());				aaResponse.setVehModlYr(laVehData.getVehModlYr());			}		}		catch (Exception aeEx)		{			Log.write(				Log.SQL_EXCP,				this,				"Got an Exception while extracting data");			aeEx.printStackTrace();			aaResponse.setErrMsgNo(				ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);		}	}	/**	 * Formulate the GeneralSearchData object used for searching.	 * 	 * @param aaReq RtsVehPublicRequest	 * @return GeneralSearchData	 *//* &RtsVehPublicService.formGeneralSearchData& */	private GeneralSearchData formGeneralSearchData(RtsVehPublicRequest aaReq)	{		// set up the search data		GeneralSearchData laGSD = new GeneralSearchData();		//Initialize Variables		laGSD.setIntKey1(0);		laGSD.setIntKey2(0);		laGSD.setKey1(null);		laGSD.setKey2(null);		// Set Type of search		laGSD.setKey1(CommonConstant.VIN);		// Set Search Key				String lsVIN = aaReq.getVIN();		lsVIN = CommonValidations.convert_i_and_o_to_1_and_0(lsVIN);		laGSD.setKey2(lsVIN.toUpperCase());		// Set TransCd		laGSD.setKey3(TransCdConstant.V21VTN);		return laGSD;	}	/**	 * Log Request	 *	 * @param aaRtsSrvcHstryData	 * @param aaDBA 	 * @return boolean 	 *//* &RtsVehPublicService.logRequest& */	private boolean logRequest(		WebServiceHistoryData aaRtsSrvcHstryData,		DatabaseAccess aaDBA)	{		boolean lbSuccessful = false;		try		{			aaDBA.beginTransaction();			// Log the request to RTS_SRVC_HSTRY			RtsServicePerformanceTracking laSPT =				new RtsServicePerformanceTracking(aaDBA);			laSPT.logInsert(aaRtsSrvcHstryData);			aaDBA.endTransaction(DatabaseAccess.COMMIT);			lbSuccessful = true;		}		catch (RTSException aeRTSEx)		{			try			{				aaDBA.endTransaction(DatabaseAccess.ROLLBACK);			}			catch (Exception aeEx)			{			}		}		return lbSuccessful;	}	/**	 * Log Result	 * 	 * @param aaDBA 	 *//* &RtsVehPublicService.logResult& */	private void logResult(		WebServiceHistoryData aaRtsSrvcHstryData,		DatabaseAccess aaDBA)	{		try		{			aaDBA.beginTransaction();			RtsServicePerformanceTracking laSPT =				new RtsServicePerformanceTracking(aaDBA);			laSPT.logUpdate(aaRtsSrvcHstryData);			aaDBA.endTransaction(DatabaseAccess.COMMIT);		}		catch (RTSException aeRTSEx)		{			System.out.println(				"RtsVehPublicService had a problem processing the request");			try			{				aaDBA.endTransaction(DatabaseAccess.ROLLBACK);			}			catch (RTSException aeRTSEx1)			{			}		}	}	/**	 * Receive Vehicle Requests (for Public) and responds 	 * 	 * @param aarrRequest	 * @return RtsVehPublicResponse[]	 *//* &RtsVehPublicService.processData$2& */	public RtsVehPublicResponse[] processData(RtsVehPublicRequest[] aarrRequest)	{		RtsVehPublicResponse[] larrResponse =			new RtsVehPublicResponse[aarrRequest.length];		DatabaseAccess laDBA = null;		for (int i = 0; i < aarrRequest.length; i++)		{			int liErrMsgNo = ErrorsConstant.ERR_NUM_V21_NOT_AVAILABLE;			RtsVehPublicRequest laRequest = aarrRequest[i];			if (laRequest.isValidateRequest())			{				laDBA = new DatabaseAccess();				RtsServicePerformanceTracking laSPTTrack =					new RtsServicePerformanceTracking(laDBA);				int liSAVId =					laSPTTrack.lookupSAVId(						"RtsVehService",						aarrRequest[i].getAction(),						aarrRequest[i].getVersionNo());				WebServiceHistoryData laRtsSrvcHstryData =					new WebServiceHistoryData(						liSAVId,						aarrRequest[i].getCaller(),						aarrRequest[i].getSessionId());				boolean lbDBLog = logRequest(laRtsSrvcHstryData, laDBA);				if (liSAVId > 0)				{					switch (aarrRequest[i].getAction())					{						case WebServicesActionsConstants							.RTS_VEH_PUBLIC :							{								RtsVehPublicResponse laNewResponse =									new RtsVehPublicResponse();								processGetVehPublic(									aarrRequest[i],									laNewResponse);								larrResponse[i] = laNewResponse;								liErrMsgNo =									larrResponse[i].getErrMsgNo();								laRtsSrvcHstryData.setSuccessfulIndi(									true);								break;							}						default :							{								larrResponse[i] = null;								break;							}					}				}				else				{					larrResponse[i] = null;				}				if (lbDBLog)				{					laRtsSrvcHstryData.setSuccessfulIndi(						liErrMsgNo == 0);					laRtsSrvcHstryData.setErrMsgNo(liErrMsgNo);					logResult(laRtsSrvcHstryData, laDBA);				}			}			else			{				RtsVehPublicResponse laNewResponse =					new RtsVehPublicResponse();				laNewResponse.setErrMsgNo(					ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);				larrResponse[i] = laNewResponse;			}		}		return larrResponse;	}	/**	 * Lookup the applicable Indicators and add them to the response.	 * 	 * @param aaRes	 * @param aaVID	 *//* &RtsVehPublicService.getIndicatorArray& */	private void getIndicatorArray(		RtsVehPublicResponse aaRes,		VehicleInquiryData aaVID)	{		IndicatorLookup laIndiLookup = new IndicatorLookup();		Vector lvIndis =			laIndiLookup.getIndicatorsV21(				aaVID.getMfVehicleData(),				TransCdConstant.V21VTN,				IndicatorLookup.V21);		RtsVehIndicator[] larrIndis =			new RtsVehIndicator[lvIndis.size()];		for (int i = 0; i < lvIndis.size(); i++)		{			IndicatorData laID = (IndicatorData) lvIndis.elementAt(i);			RtsVehIndicator laVI21Indi = new RtsVehIndicator();			laVI21Indi.setIndiName(laID.getIndiName());			laVI21Indi.setIndiValue(laID.getDesc());			larrIndis[i] = laVI21Indi;		}		aaRes.setIndicators(larrIndis);	}	/**	 * Process Get Vehicle request.	 * 	 * @param aaRequest	 * @param aaResponse	 *//* &RtsVehPublicService.processGetVehPublic& */	private void processGetVehPublic(		RtsVehPublicRequest aaRequest,		RtsVehPublicResponse aaResponse)	{		// default to not found		aaResponse.setErrMsgNo(ErrorsConstant.ERR_NUM_V21_NOT_FOUND);		GeneralSearchData laSearchData =			formGeneralSearchData(aaRequest);		CommonServerBusiness laCSB = new CommonServerBusiness();		try		{			Object laVehicle =				laCSB.processData(					GeneralConstant.COMMON,					CommonConstant.GET_VEH,					laSearchData);			if (laVehicle instanceof VehicleInquiryData)			{				VehicleInquiryData laVID =					(VehicleInquiryData) laVehicle;				if (laVID.isMFUp())				{					if (laVID.getNoMFRecs() < 1)					{						// check to see if there is an archive record  						laSearchData.setIntKey2(							CommonConstant.SEARCH_ARCHIVE);						laSearchData.setIntKey4(1);						// search archive						laVehicle =							laCSB.processData(								GeneralConstant.COMMON,								CommonConstant.GET_VEH,								laSearchData);						laVID = (VehicleInquiryData) laVehicle;					}					if (laVID.getNoMFRecs() == 1)					{						aaResponse.setErrMsgNo(0);						extractData(aaResponse, laVID);					}					else if (laVID.getNoMFRecs() > 1)					{						aaResponse.setErrMsgNo(							ErrorsConstant.ERR_NUM_V21_MULTIPLE_RECS);					}				}				else				{					aaResponse.setErrMsgNo(						ErrorsConstant.ERR_NUM_V21_NOT_AVAILABLE);				}			}		}		catch (RTSException aeRTSEx)		{			System.err.println("Got an exception on vehicle lookup");			aeRTSEx.printStackTrace();			aaResponse.setErrMsgNo(				ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);		}	}}/* #RtsVehPublicService# */