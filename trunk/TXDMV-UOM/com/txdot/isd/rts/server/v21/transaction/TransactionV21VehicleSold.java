package com.txdot.isd.rts.server.v21.transaction;

import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.*;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.v21.request.tracking.V21RequestTracking;
import com.txdot.isd.rts.server.v21.transaction.data.WsVehicleSoldV21ReqData;
import com.txdot.isd.rts.server.v21.transaction.data.WsVehicleSoldV21ReqDataVerA;
import com.txdot.isd.rts.server.v21.transaction.data.WsVehicleSoldV21ResData;
import com.txdot.isd.rts.server.v21.transaction.data.WsVehicleSoldV21ResDataVerB;

/*
 * TransactionV21VehicleSold.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	02/07/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/25/2008	Add Ver A processing.
 * 							Add Request Tracking.
 * 							add createVehicleTransferNotificationVerA()
 * 							modify createVehicleTransferNotification(),
 * 								extractData()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * K Harrell	04/07/2008	Add V21UniqueId, VTNSource processing 
 * 							modify extractData()
 * 							defect 9582 Ver Defect POS A
 * K Harrell	06/26/2008	Add search for Archive; Correct assignment
 * 							of SuccessfulIndi  
 * 							modify getVehicleRecord(), extractData()
 * 							defect 9700, 9709 Ver Defect POS A
 * K Harrell	08/01/2008	Remove reference to partials if record 
 * 							count = 1. 
 * 							modify getVehicleRecord()
 * 							defect 9785 Ver MyPlates_POS 
 * K Harrell	02/18/2009	Restored from earlier version 
 * 							defect 9785 Ver Defect_POS_D 
 * K Harrell	07/23/2010	added createVehicleTransferNotificationVerB()
 * 							defect 10482 Ver 6.5.0 
 * K Harrell	07/26/2010	add V21UniqueId to response
 * 							modify createVehicleTransferNotificationVerB()
 * 							defect 10482 Ver 6.5.0
 * K Harrell	08/04/2010	add retry on Transaction Insert
 * 							add INT_MAX_RETRY, MSG_RETRY,
 *  						  MSG_RETRY_EXCEEDED,
 * 							  MSG_RTSEX_ON_TRANS_CREATE,
 * 							  MSG_SLEEP_INTERRUPTED
 * 							add postV21VTNTrans()
 * 							modify extractData() 
 * 							defect 10569 Ver 6.5.0   
 * K Harrell	05/09/2011	Check for MFDown 
 * 							modify getVehicleRecord() 
 * 							defect 10810 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Transaction - Vehicle Sold.
 *
 * @version	6.7.1 			05/09/2011
 * @author	B Hargrove
 * <br>Creation Date:		01/15/2008 11:02
 */

public class TransactionV21VehicleSold
{
	// defect 10569 
	private final static int INT_MAX_RETRY = 5;
	private static final String MSG_RETRY =
		"Retrying V21VTN transaction request ";
	private static final String MSG_RETRY_EXCEEDED =
		"Maximum Retry for V21VTN transaction exceeded";
	private static final String MSG_RTSEX_ON_TRANS_CREATE =
		"RTSException processing V21VTN trans request";
	private static final String MSG_SLEEP_INTERRUPTED =
		"Sleep interrupted ";
	// end defect 10569 

	/**
	 * Do the requested service.
	 * Get MF Vehicle for Vehicle Transfer Notification Transaction and 
	 * update the Vehicle Sold Date.
	 * 
	 * @param String asPlateNumber
	 * @param String asVTNSource
	 * @param int aiRegExpYear
	 * @param int aiRegExpMonth
	 * @param String asDocumentNumber
	 * @param int aiSoldDate
	 * @param long alSpecialRegID
	 * @return array WsVehicleSoldV21ResData[]
	 */
	public WsVehicleSoldV21ResData[] createVehicleTransferNotification(
		String asPlateNumber,
		String asVTNSource,
		int aiRegExpYear,
		int aiRegExpMonth,
		String asDocumentNumber,
		int aiSoldDate,
		long alSpecialRegID)
	{
		System.out.println("Entered Vehicle Sold web service");
		// Formulate the Request Tracking Object
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRegPltNo(asPlateNumber);
		laRequestTrackingData.setVTNSource(asVTNSource);
		laRequestTrackingData.setRegExpYr(aiRegExpYear);
		laRequestTrackingData.setRegExpMo(aiRegExpMonth);
		laRequestTrackingData.setDocNo(asDocumentNumber);
		laRequestTrackingData.setVehSoldDate(aiSoldDate);
		laRequestTrackingData.setSpclRegId(alSpecialRegID);
		V21RequestTracking laRequestTrackingWrite =
			new V21RequestTracking();

		try
		{
			// log entry to get vehicle
			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_VEH_SOLD_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking");
			aeRTSEx.printStackTrace();
		}

		// Formulate request
		WsVehicleSoldV21ReqData laRequest =
			new WsVehicleSoldV21ReqData();
		laRequest.setV21PlateNumberReq(asPlateNumber);
		laRequest.setV21VTNSourceReq(asVTNSource);
		laRequest.setV21RegExpYearReq(aiRegExpYear);
		laRequest.setV21RegExpMonthReq(aiRegExpMonth);
		laRequest.setV21DocumentNumberReq(asDocumentNumber);
		laRequest.setV21SoldDateReq(aiSoldDate);
		laRequest.setV21SpecialRegIDReq(alSpecialRegID);

		// Create base response data object
		WsVehicleSoldV21ResData laResponse =
			new WsVehicleSoldV21ResData();

		// Create the final response array
		WsVehicleSoldV21ResData[] larrFinalResponse =
			new WsVehicleSoldV21ResData[1];

		// TODO
		//Validate the date
		//		RTSDate laRTSDate = new RTSDate(RTSDate.YYYYMMDD,aiSoldDate);

		//		if (laRTSDate.compareTo(RTSDate.getCurrentDate())<=0)
		//		{
		writeV21VehicleSoldToLog(laRequest);

		// Get the best guess vehicle record
		getVehicleRecord(laRequest, laResponse, laRequestTrackingData);

		larrFinalResponse[0] = laResponse;
		//		}
		//		else
		// Invalid date. Do not store RTS transaction. Respond with error.
		//		{
		//			laResponse.setResult(String.
		//				valueOf(ErrorsConstant.
		//					ERR_NUM_V21_INVALID_REQUEST);
		//			laResponse.setV21VehicleSoldErrMsgRes("FAILED");
		//			aaRequesTrackingData.setErrMsgNo(
		//				ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
		//		}

		System.out.println("Finished Vehicle Sold web service");
		System.out.println("Result " + laResponse.getResult());

		// log the completion
		try
		{
			// defect 9700 
			// Set Indi Conditionally
			laRequestTrackingData.setSuccessfulIndi(
				laRequestTrackingData.getErrMsgNo()
					== ErrorsConstant.ERR_NUM_V21_SUCCESS
					? 1
					: 0);
			// end defect 9700  

			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_COMPL_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking");
			aeRTSEx.printStackTrace();
		}

		// Return the response array
		return larrFinalResponse;

	}

	/**
	 * Do the requested service.
	 * Get MF Vehicle for Vehicle Transfer Notification Transaction and 
	 * update the Vehicle Sold Date.
	 * 
	 * <p>This is Version A of the Interface.
	 * 
	 * @param String asPlateNumber
	 * @param String asVTNSource
	 * @param int aiRegExpYear
	 * @param int aiRegExpMonth
	 * @param String asDocumentNumber
	 * @param int aiSoldDate
	 * @param long alSpecialRegID
	 * @param int aiV21UniqueId
	 * @return array WsVehicleSoldV21ResData[]
	 */
	public WsVehicleSoldV21ResData[] createVehicleTransferNotificationVerA(
		String asPlateNumber,
		String asVTNSource,
		int aiRegExpYear,
		int aiRegExpMonth,
		String asDocumentNumber,
		int aiSoldDate,
		long alSpecialRegID,
		int aiV21UniqueId)
	{
		System.out.println("Entered Vehicle Sold web service VerA");

		// Formulate the Request Tracking Object
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRegPltNo(asPlateNumber);
		laRequestTrackingData.setVTNSource(asVTNSource);
		laRequestTrackingData.setRegExpYr(aiRegExpYear);
		laRequestTrackingData.setRegExpMo(aiRegExpMonth);
		laRequestTrackingData.setDocNo(asDocumentNumber);
		laRequestTrackingData.setVehSoldDate(aiSoldDate);
		laRequestTrackingData.setSpclRegId(alSpecialRegID);
		laRequestTrackingData.setV21IntrfcLogId(aiV21UniqueId);
		V21RequestTracking laRequestTrackingWrite =
			new V21RequestTracking();

		try
		{
			// log entry to get vehicle
			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_VEH_SOLD_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking");
			aeRTSEx.printStackTrace();
		}

		// Formulate request
		WsVehicleSoldV21ReqDataVerA laRequest =
			new WsVehicleSoldV21ReqDataVerA();
		laRequest.setV21PlateNumberReq(asPlateNumber);
		laRequest.setV21VTNSourceReq(asVTNSource);
		laRequest.setV21RegExpYearReq(aiRegExpYear);
		laRequest.setV21RegExpMonthReq(aiRegExpMonth);
		laRequest.setV21DocumentNumberReq(asDocumentNumber);
		laRequest.setV21SoldDateReq(aiSoldDate);
		laRequest.setV21SpecialRegIDReq(alSpecialRegID);
		laRequest.setV21UniqueId(aiV21UniqueId);

		// Create base response data object
		WsVehicleSoldV21ResData laResponse =
			new WsVehicleSoldV21ResData();

		// Create the final response array
		WsVehicleSoldV21ResData[] larrFinalResponse =
			new WsVehicleSoldV21ResData[1];

		// TODO
		//Validate the date
		//		RTSDate laRTSDate = new RTSDate(RTSDate.YYYYMMDD,aiSoldDate);

		//		if (laRTSDate.compareTo(RTSDate.getCurrentDate())<=0)
		//		{
		writeV21VehicleSoldToLog(laRequest);

		// Get the best guess vehicle record
		getVehicleRecord(laRequest, laResponse, laRequestTrackingData);

		larrFinalResponse[0] = laResponse;
		//		}
		//		else
		// Invalid date. Do not store RTS transaction. Respond with error.
		//		{
		//			laResponse.setResult(String.
		//				valueOf(ErrorsConstant.
		//					ERR_NUM_V21_INVALID_REQUEST);
		//			laResponse.setV21VehicleSoldErrMsgRes("FAILED");
		//			aaRequesTrackingData.setErrMsgNo(
		//				ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
		//		}

		System.out.println("Finished Vehicle Sold web service");
		System.out.println("Result " + laResponse.getResult());

		// log the completion
		try
		{
			laRequestTrackingData.setSuccessfulIndi(1);
			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_COMPL_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking");
			aeRTSEx.printStackTrace();
		}

		// Return the response array
		return larrFinalResponse;

	}

	/**
	 * Do the requested service.
	 * Get MF Vehicle for Vehicle Transfer Notification Transaction and 
	 * update the Vehicle Sold Date.
	 * 
	 * <p>This is Version B of the Interface.
	 * 
	 * @param String asPlateNumber
	 * @param String asVTNSource
	 * @param int aiRegExpYear
	 * @param int aiRegExpMonth
	 * @param String asDocumentNumber
	 * @param int aiSoldDate
	 * @param long alSpecialRegID
	 * @param int aiV21UniqueId
	 * @return array WsVehicleSoldV21ResDataVerB[]
	 */
	public WsVehicleSoldV21ResDataVerB[] createVehicleTransferNotificationVerB(
		String asPlateNumber,
		String asVTNSource,
		int aiRegExpYear,
		int aiRegExpMonth,
		String asDocumentNumber,
		int aiSoldDate,
		long alSpecialRegID,
		int aiV21UniqueId)
	{
		System.out.println("Entered Vehicle Sold web service VerB");

		// Formulate the Request Tracking Object
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRegPltNo(asPlateNumber);
		laRequestTrackingData.setVTNSource(asVTNSource);
		laRequestTrackingData.setRegExpYr(aiRegExpYear);
		laRequestTrackingData.setRegExpMo(aiRegExpMonth);
		laRequestTrackingData.setDocNo(asDocumentNumber);
		laRequestTrackingData.setVehSoldDate(aiSoldDate);
		laRequestTrackingData.setSpclRegId(alSpecialRegID);
		laRequestTrackingData.setV21IntrfcLogId(aiV21UniqueId);
		V21RequestTracking laRequestTrackingWrite =
			new V21RequestTracking();

		try
		{
			// log entry to get vehicle
			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_VEH_SOLD_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking");
			aeRTSEx.printStackTrace();
		}

		// TODO  Should this be VerB 
		// Formulate request
		WsVehicleSoldV21ReqDataVerA laRequest =
			new WsVehicleSoldV21ReqDataVerA();
		laRequest.setV21PlateNumberReq(asPlateNumber);
		laRequest.setV21VTNSourceReq(asVTNSource);
		laRequest.setV21RegExpYearReq(aiRegExpYear);
		laRequest.setV21RegExpMonthReq(aiRegExpMonth);
		laRequest.setV21DocumentNumberReq(asDocumentNumber);
		laRequest.setV21SoldDateReq(aiSoldDate);
		laRequest.setV21SpecialRegIDReq(alSpecialRegID);
		laRequest.setV21UniqueId(aiV21UniqueId);

		// Create base response data object
		WsVehicleSoldV21ResDataVerB laResponse =
			new WsVehicleSoldV21ResDataVerB();

		laResponse.setV21ReqId(laRequestTrackingData.getV21ReqId());
		laResponse.setV21UniqueId(aiV21UniqueId);

		// Create the final response array
		WsVehicleSoldV21ResDataVerB[] larrFinalResponse =
			new WsVehicleSoldV21ResDataVerB[1];

		// TODO
		//Validate the date
		//		RTSDate laRTSDate = new RTSDate(RTSDate.YYYYMMDD,aiSoldDate);

		//		if (laRTSDate.compareTo(RTSDate.getCurrentDate())<=0)
		//		{
		writeV21VehicleSoldToLog(laRequest);

		// Get the best guess vehicle record
		getVehicleRecord(laRequest, laResponse, laRequestTrackingData);

		larrFinalResponse[0] = laResponse;
		//		}
		//		else
		// Invalid date. Do not store RTS transaction. Respond with error.
		//		{
		//			laResponse.setResult(String.
		//				valueOf(ErrorsConstant.
		//					ERR_NUM_V21_INVALID_REQUEST);
		//			laResponse.setV21VehicleSoldErrMsgRes("FAILED");
		//			aaRequesTrackingData.setErrMsgNo(
		//				ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
		//		}

		System.out.println("Finished Vehicle Sold web service");
		System.out.println("Result " + laResponse.getResult());

		// log the completion
		try
		{
			laRequestTrackingData.setSuccessfulIndi(1);
			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_COMPL_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking");
			aeRTSEx.printStackTrace();
		}

		// Return the response array
		return larrFinalResponse;

	}

	/**
	 * Copy data from vehicle record to response object.
	 * 
	 * @param aaReq
	 * @param aaRes
	 * @param aaVID
	 * @param aaRequesTrackingData
	 */
	private void extractData(
		WsVehicleSoldV21ReqData aaReq,
		WsVehicleSoldV21ResData aaRes,
		VehicleInquiryData aaVID,
		V21RequestData aaRequesTrackingData)
	{
		if (aaVID.getMfVehicleData() != null)
		{
			// see if we can get Title Data
			if (aaVID.getMfVehicleData().getTitleData() != null)
			{
				try
				{
					// From found vehicle, update the Vehicle Sold Date
					aaVID
						.getMfVehicleData()
						.getTitleData()
						.setVehSoldDate(
						aaReq.getV21SoldDateReq());

					// defect 9582
					if (aaVID.getVehMiscData() == null)
					{
						aaVID.setVehMiscData(new VehMiscData());
					}

					aaVID.getVehMiscData().setV21VTNId(
						aaReq.getV21UniqueId());

					aaVID.getVehMiscData().setVTNSource(
						aaReq.getV21VTNSourceReq());
					// end defect 9582

					Vector lvTransSend = new Vector(2);
					lvTransSend.add(aaVID);

					if (aaReq.getV21VTNSourceReq().equals("G360"))
					{
						// Send the Global 360 trans
						lvTransSend.add(TransCdConstant.G36VTN);
					}
					else if (aaReq.getV21VTNSourceReq().equals("V21"))
					{

						lvTransSend.add(TransCdConstant.V21VTN);
					}
					else
					{
						aaRes.setResult(
							String.valueOf(
								ErrorsConstant
									.ERR_NUM_V21_INVALID_REQUEST));

						aaRequesTrackingData.setErrMsgNo(
							ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);

						Log.write(
							Log.SQL_EXCP,
							this,
							"Invalid Source for Transaction selection");
						throw new RTSException(
							ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
					}

					// defect 10569  
					// Call method to post V21VTN Transaction
					Object laReturn =
						(TransactionHeaderData) postV21VTNTrans(aaRes,
							lvTransSend,
							aaRequesTrackingData);
					// end defect 10569

					aaRes.setResult(
						String.valueOf(
							ErrorsConstant.ERR_NUM_V21_SUCCESS));

					// Extract the TransId Data and set to 
					// Tracking Object 
					TransactionHeaderData laTransHdrData =
						(TransactionHeaderData) laReturn;
					TransactionKey laTransKey = new TransactionKey();
					laTransKey.setOfcIssuanceNo(
						laTransHdrData.getOfcIssuanceNo());
					laTransKey.setTransWsId(
						laTransHdrData.getTransWsId());
					laTransKey.setTransAMDate(
						laTransHdrData.getTransAMDate());
					laTransKey.setTransTime(
						laTransHdrData.getTransTime());
					aaRequesTrackingData.setTransRequest(true);
					aaRequesTrackingData.setTransactionKey(laTransKey);
					aaRequesTrackingData.setErrMsgNo(
						ErrorsConstant.ERR_NUM_V21_SUCCESS);
				}
				catch (RTSException aeRTSEx)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Got an RTSException while extracting data");
					if (aeRTSEx.getCode() > 0)
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							"Error number " + aeRTSEx.getCode());
					}
					aeRTSEx.printStackTrace();

					// defect 10569 
					// Only assign if not previously assigned 
					if (aaRequesTrackingData.getErrMsgNo()
						<= ErrorsConstant.ERR_NUM_V21_SUCCESS)
					{
						aaRes.setResult(
							String.valueOf(
								ErrorsConstant
									.ERR_NUM_V21_INVALID_REQUEST));

						aaRequesTrackingData.setErrMsgNo(
							ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
					}
					// end defect 10569
				}
				catch (Exception aeEx)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Got an Exception while extracting data");
					aeEx.printStackTrace();
					aaRes.setResult(
						String.valueOf(
							ErrorsConstant
								.ERR_NUM_V21_INVALID_REQUEST));
					aaRequesTrackingData.setErrMsgNo(
						ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
				}
			}
		}
	}

	/**
	 * Formulate the GeneralSearchData object used for searching.
	 * 
	 * @param aaReq WsVehicleSoldV21ReqData
	 * @return GeneralSearchData
	 */
	private GeneralSearchData formGeneralSearchData(WsVehicleSoldV21ReqData aaReq)
	{
		// set up the search data
		GeneralSearchData laTempSearchData = new GeneralSearchData();

		// always search Active / Inactive first.
		laTempSearchData.setIntKey2(
			CommonConstant.SEARCH_ACTIVE_INACTIVE);

		// try using TITLE at first
		// this should force VINA lookup
		laTempSearchData.setKey3(TransCdConstant.TITLE);

		laTempSearchData.setKey5("V21");

		// populate search data according to data provided
		if (aaReq.getV21DocumentNumberReq() != null
			&& aaReq.getV21DocumentNumberReq().length() > 0)
		{
			laTempSearchData.setKey1(CommonConstant.DOC_NO);
			laTempSearchData.setKey2(aaReq.getV21DocumentNumberReq());
		}
		else if (
			aaReq.getV21PlateNumberReq() != null
				&& aaReq.getV21PlateNumberReq().length() > 0)
		{
			laTempSearchData.setKey1(CommonConstant.REG_PLATE_NO);
			laTempSearchData.setKey2(aaReq.getV21PlateNumberReq());
		}

		return laTempSearchData;
	}

	/**
	 * Look up the requested vehicle record and determine 
	 * if it is the one we are looking for.
	 * 
	 * @param aaReq WsVehicleInfoV21DataReq
	 * @param aaRes WsVehicleInfoV21DataRes
	 * @param aaRequesTrackingData V21RequestData
	 */
	private void getVehicleRecord(
		WsVehicleSoldV21ReqData aaReq,
		WsVehicleSoldV21ResData aaRes,
		V21RequestData aaRequesTrackingData)
	{
		// default to not found
		aaRequesTrackingData.setErrMsgNo(
			ErrorsConstant.ERR_NUM_V21_NOT_FOUND);

		GeneralSearchData laSearchData = formGeneralSearchData(aaReq);
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

				// defect 10810 	
				if (laVID.isMFDown())
				{
					aaRes.setResult(
						String.valueOf(
							ErrorsConstant.ERR_NUM_V21_NOT_AVAILABLE));
					aaRequesTrackingData.setErrMsgNo(
						ErrorsConstant.ERR_NUM_V21_NOT_AVAILABLE);
					laVID.setNoMFRecs(0);
				}
				else
				{
					// end defect 10811 

					// defect 9709 
					if (laVID.getNoMFRecs() < 1)
					{
						laSearchData.setIntKey2(
							CommonConstant.SEARCH_ARCHIVE);
						laSearchData.setIntKey4(1);

						// search archive
						laVehicle =
							laCSB.processData(
								GeneralConstant.COMMON,
								CommonConstant.GET_VEH,
								laSearchData);
						laVID = (VehicleInquiryData) laVehicle;
					}

					// defect 9785
					// Remove reference to partials as is populated even
					// when latest RegData is selected.
					if (laVID.getNoMFRecs() == 1)
						//	&& (laVID.getPartialDataList() == null
						//		|| laVID.getPartialDataList().size() == 0))
						// end defect 9785 
					{
						// We have just one record to work with 

						// Test if RegExpMo\Yr match
						if (laVID.getMfVehicleData() != null
							&& laVID.getMfVehicleData().getRegData()
								!= null
							&& (laVID
								.getMfVehicleData()
								.getRegData()
								.getRegExpMo()
								!= aaReq.getV21RegExpMonthReq()
								|| laVID
									.getMfVehicleData()
									.getRegData()
									.getRegExpYr()
									!= aaReq.getV21RegExpYearReq()))
						{
							// RegExpMo\Yr did not match the vehicle returned
							System.out.println(
								"Fail on "
									+ aaReq.getV21PlateNumberReq()
									+ " "
									+ aaReq.getV21RegExpMonthReq()
									+ " "
									+ aaReq.getV21RegExpYearReq()
									+ " Reg Exp Month or Year not matching");
							aaRes.setResult(
								String.valueOf(
									ErrorsConstant
										.ERR_NUM_V21_REGEXPYRMO_DO_NOT_MATCH));
							aaRequesTrackingData.setErrMsgNo(
								ErrorsConstant
									.ERR_NUM_V21_REGEXPYRMO_DO_NOT_MATCH);
						}
						else
						{
							// We have just one record to work with
							// and it matches RegExpMo\Yr						

							extractData(
								aaReq,
								aaRes,
								laVID,
								aaRequesTrackingData);

						}
					}
					// end defect 9709

					else if (laVID.getNoMFRecs() < 1)
					{
						// There was no record found
						aaRes.setResult(
							String.valueOf(
								ErrorsConstant.ERR_NUM_V21_NOT_FOUND));
						aaRequesTrackingData.setErrMsgNo(
							ErrorsConstant.ERR_NUM_V21_NOT_FOUND);
					}
					else
					{
						// TODO Not ready for multiple recs yet
						aaRes.setResult(
							String.valueOf(
								ErrorsConstant
									.ERR_NUM_V21_MULTIPLE_RECS));
						aaRequesTrackingData.setErrMsgNo(
							ErrorsConstant.ERR_NUM_V21_MULTIPLE_RECS);
					}
				}
				// defect 10810 
			}
			// end defect 10810 
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Got an exception on vehicle lookup");
			aeRTSEx.printStackTrace();

			// send back invalid request to v21
			aaRes.setResult(
				String.valueOf(
					ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST));
			aaRequesTrackingData.setErrMsgNo(
				ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
		}
	}

	/**
	 * Post V21VTN transaction
	 * 
	 * @param aaRes
	 * @param avTransSend
	 * @param aaRequesTrackingData
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object postV21VTNTrans(
		WsVehicleSoldV21ResData aaRes,
		Vector avTransSend,
		V21RequestData aaRequesTrackingData)
		throws RTSException
	{
		Object laReturn = new Object();

		for (int i = 0; i < INT_MAX_RETRY; i++)
		{
			try
			{
				CommonServerBusiness laCommServerBuss =
					new CommonServerBusiness();

				laReturn =
					laCommServerBuss.processData(
						GeneralConstant.COMMON,
						CommonConstant.PROC_V21VTN,
						avTransSend);
				break;
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					MSG_RTSEX_ON_TRANS_CREATE);

				// Check to see if this is a duplicate key.
				if (aeRTSEx.getDetailMsg() != null
					&& aeRTSEx.getDetailMsg().indexOf(
						CommonConstant.DUPLICATE_KEY_EXCEPTION)
						> -1)
				{
					if (avTransSend instanceof Vector
						&& avTransSend != null
						&& avTransSend.size() == 2
						&& avTransSend.elementAt(0)
							instanceof VehicleInquiryData)
					{
						VehicleInquiryData laVID =
							(VehicleInquiryData) avTransSend.elementAt(
								0);

						String lsVin =
							laVID
								.getMfVehicleData()
								.getVehicleData()
								.getVin();

						String lsRegPltNo =
							laVID
								.getMfVehicleData()
								.getRegData()
								.getRegPltNo();

						// If transaction will be retried 
						if (i < INT_MAX_RETRY - 1)
						{
							Log.write(
								Log.SQL_EXCP,
								this,
								MSG_RETRY
									+ i
									+ " "
									+ " VIN: "
									+ lsVin
									+ " PlateNo: "
									+ lsRegPltNo);

							try
							{
								Thread.sleep(1000);
							}
							catch (InterruptedException aeIEx)
							{
								Log.write(
									Log.SQL_EXCP,
									this,
									MSG_SLEEP_INTERRUPTED + i);
							}
						}
						else
						{
							Log.write(
								Log.SQL_EXCP,
								this,
								MSG_RETRY_EXCEEDED
									+ " "
									+ " VIN: "
									+ lsVin
									+ " PlateNo: "
									+ lsRegPltNo);

							aaRequesTrackingData.setErrMsgNo(
								ErrorsConstant
									.ERR_NUM_V21_NOT_AVAILABLE);

							aaRes.setResult(
								String.valueOf(
									ErrorsConstant
										.ERR_NUM_V21_NOT_AVAILABLE));
							throw aeRTSEx;
						}
					}
					else
					{
						throw aeRTSEx;
					}
				}
				else
				{
					throw aeRTSEx;
				}
			}
		}
		return laReturn;
	}

	/**
	 * Write results of Vehicle Sold Transaction to log
	 * 
	 * @param WsVehicleSoldV21ReqData aaRequest
	 */
	private void writeV21VehicleSoldToLog(WsVehicleSoldV21ReqData aaRequest)
	{
		System.out.println("**V21 Vehicle Sold Request:");
		System.out.println("Plate " + aaRequest.getV21PlateNumberReq());
		System.out.println(
			"VTN Source " + aaRequest.getV21VTNSourceReq());
		System.out.println(
			"RegExpYr " + aaRequest.getV21RegExpYearReq());
		System.out.println(
			"RegExpMo " + aaRequest.getV21RegExpMonthReq());
		System.out.println(
			"DocNo " + aaRequest.getV21DocumentNumberReq());
		System.out.println("SoldDate " + aaRequest.getV21SoldDateReq());
		System.out.println(
			"SpclRegID " + aaRequest.getV21SpecialRegIDReq());
		System.out.println("V21UniqueId " + aaRequest.getV21UniqueId());
	}

}
