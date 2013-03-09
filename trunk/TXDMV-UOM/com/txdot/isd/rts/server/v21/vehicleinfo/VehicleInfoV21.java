package com.txdot.isd.rts.server.v21.vehicleinfo;

import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.v21.request.tracking.V21RequestTracking;
import com.txdot.isd.rts.server.v21.vehicleinfo.data.*;

/*
 * VehicleInfoV21.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/14/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	01/18/2008	Moved the constants to CommonConstant.
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	01/22/2008	Reflowed the public method to accept just 
 * 							base data types (String) and return an array
 * 							of response object (even though it is 1 item).
 * 							modified getVehicleV21()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	03/03/2008	Update search routine to check Archive if no
 * 							record was found on Active / Inactive.
 * 							modify getVehicleRecord() 
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	03/10/2008	Improve reporting of multiple archive 
 * 							records.
 * 							modify getVehicleRecord()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	04/03/2008	Improve handling of Archive.  The partial 
 * 							list comes back as an empty vector.
 * 							modify getVehicleRecord()
 * 							defect 9629 Ver 3_Amigos_PH_A
 * K Harrell	08/01/2008	Check for null VIN in getVehicleRecord() 
 * 							modify getVehicleRecord()
 * 							defect 9786 Ver MyPlates_POS
 * K Harrell	09/02/2008	Use new Indicator method names 
 * 							modify getIndicatorArray()
 * 							defect 9651 Ver Defect_POS_B
 * K Harrell	09/08/2008	Add Last4OfVin to GSD when search by Plate
 * 							modify formGeneralSearchData()
 * 							defect 9818 Ver Defect_POS_B 
 * K Harrell	07/12/2009	Implement new OwnerData
 * 							modify extractData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	11/02/2009	When Search Vina requested, return MF 
 * 							Record w/ GSD on request to search Archive. 
 * 							getVehicleRecord() 
 * 							defect 10264 Defect_POS_G 
 * K Harrell	12/23/2009	Correct Return Code on No Record Found && 
 * 							VinaLookup = false
 * 							modify getVehicleRecord() 
 * 							defect 10280 Ver Defect_POS_H
 * K Harrell	02/18/2010	Track VinaLookupNeeded on V21 Get Vehicle
 * 							requests. 
 * 							modify getVehicleV21VerA()
 * 							defect 10337 Ver POS_640
 * K Harrell	04/23/2010	Try/Catch around Tracking Update
 * 							add V21TRACKINGINSERT, V21TRACKINGUPDATE 
 * 							add logTrackingError() 
 * 							modify getVehicleV21VerA() 
 * 							defect 10467 Ver POS_640 
 * K Harrell	07/23/2010	add getVehicleV21VerB()
 * 							modify logTrackingError()  
 * 							defect 10482 Ver 6.5.0 
 * K Harrell	07/26/2010	add V21UniqueId to response
 * 							modify getVehicleV21VerB()
 * 							defect 10482 Ver 6.5.0 
 * K Harrell	08/10/2010	Throw error on getVehicle if parameters are 
 * 							 of incorrect length. Ignore VinaLookupNeeded
 * 							 if no VIN provided.
 * 							add validateData(), SEPARATOR
 * 							modify getVehicleV21VerB(),logTrackingError()  
 * 							defect 10577 Ver 6.5.0  
 * K Harrell	05/09/2011	Check for MFDown 
 * 							modify getVehicleRecord() 
 * 							defect 10810 Ver 6.7.1
 * ---------------------------------------------------------------------
 */

/**
 * Perform Vehicle lookup for V21 and return appropriate response.
 *
 * @version	6.7.1			05/09/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/14/2008 14:27:56
 */
public class VehicleInfoV21
{
	// defect 10467 
	private final static String V21TRACKINGINSERT = "Insert - ";
	private final static String V21TRACKINGUPDATE = "Update - ";
	// end defect 10467

	// defect 10577
	private final static String SEPARATOR = ", ";
	// end defect 10577 

	/**
	 * Do the requested service.
	 * 
	 * <p>This is the first version (" ").
	 * 
	 * @param asPlateNumber String
	 * @param asLastFourOfVIN String
	 * @param asDocumentNumber String
	 * @param asVIN String
	 * @return WsVehicleInfoV21DataRes[]
	 */
	public WsVehicleInfoV21DataRes[] getVehicleV21(
		String asPlateNumber,
		String asLastFourOfVIN,
		String asDocumentNumber,
		String asVIN)
	{
		System.out.println("Entered getVehicleV21");

		// log entry to get vehicle
		V21RequestData laRequestTrackingData = new V21RequestData();
		V21RequestTracking laRequestTrackingWrite =
			new V21RequestTracking();

		try
		{
			laRequestTrackingData.setRegPltNo(asPlateNumber);
			laRequestTrackingData.setLast4VIN(asLastFourOfVIN);
			laRequestTrackingData.setDocNo(asDocumentNumber);
			laRequestTrackingData.setVIN(asVIN);

			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_GET_VEH_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking");
			aeRTSEx.printStackTrace();
		}

		// formulate request
		WsVehicleInfoV21DataReq laRequest =
			new WsVehicleInfoV21DataReq();
		laRequest.setRegPlate(asPlateNumber);
		laRequest.setLastFourOfVin(asLastFourOfVIN);
		laRequest.setDocumentNumber(asDocumentNumber);
		laRequest.setVIN(asVIN);

		// create base response data object
		WsVehicleInfoV21DataRes laResponse =
			new WsVehicleInfoV21DataRes();

		// Default to Invalid Response.  Change if there is a different
		// result.
		laResponse.setResult(CommonConstant.INVALID_REQ);

		// Get the best guess vehicle record
		getVehicleRecord(laRequest, laResponse, true);

		// create the response object.
		WsVehicleInfoV21DataRes[] larrFinalResponse =
			new WsVehicleInfoV21DataRes[1];
		larrFinalResponse[0] = laResponse;

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

		// return the response 
		return larrFinalResponse;
	}

	/**
	 * Do the requested service.
	 * 
	 * <p>This is the A version.
	 * 
	 * @param asPlateNumber String
	 * @param asLastFourOfVIN String
	 * @param asDocumentNumber String
	 * @param asVIN String
	 * @param aiVinaLookupNeeded int
	 * @return WsVehicleInfoV21DataResVerA[]
	 */
	public WsVehicleInfoV21DataResVerA[] getVehicleV21VerA(
		String asPlateNumber,
		String asLastFourOfVIN,
		String asDocumentNumber,
		String asVIN,
		boolean abVinaLookupNeeded)
	{
		System.out.println("Entered getVehicleV21VerA");

		// create base response data object
		WsVehicleInfoV21DataResVerA laResponse =
			new WsVehicleInfoV21DataResVerA();
		WsVehicleInfoV21DataResVerA[] larrFinalResponse =
			new WsVehicleInfoV21DataResVerA[1];
		larrFinalResponse[0] = laResponse;

		// defect 10467 
		//try
		//{
		// end defect 10467 

		// log entry to get vehicle
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRegPltNo(asPlateNumber);
		laRequestTrackingData.setLast4VIN(asLastFourOfVIN);
		laRequestTrackingData.setDocNo(asDocumentNumber);
		laRequestTrackingData.setVIN(asVIN);
		// defect 10337 
		laRequestTrackingData.setVinaLookupNeeded(abVinaLookupNeeded);
		// end defect 10337 

		boolean lbTrackingUpdate = true;
		V21RequestTracking laRequestTrackingWrite =
			new V21RequestTracking();

		try
		{
			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_GET_VEH_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			// defect 10467
			lbTrackingUpdate = false;
			logTrackingError(laRequestTrackingData, V21TRACKINGINSERT);
			// end defect 10467 
		}

		// formulate request
		WsVehicleInfoV21DataReq laRequest =
			new WsVehicleInfoV21DataReq();
		laRequest.setRegPlate(asPlateNumber);
		laRequest.setLastFourOfVin(asLastFourOfVIN);
		laRequest.setDocumentNumber(asDocumentNumber);
		laRequest.setVIN(asVIN);
		laRequest.setVinaLookupNeeded(abVinaLookupNeeded);

		// Default to Invalid Response.  Change if there is a different
		// result.
		laResponse.setResult(
			ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);

		// Get the best guess vehicle record
		getVehicleRecord(
			laRequest,
			laResponse,
			laRequest.isVinaLookupNeeded());

		// reset up the response object.
		larrFinalResponse = new WsVehicleInfoV21DataResVerA[1];
		larrFinalResponse[0] = laResponse;

		// defect 10467 
		if (lbTrackingUpdate)
		{
			// log return
			laRequestTrackingData.setErrMsgNo(
				Integer.parseInt(laResponse.getResult()));

			if (laRequestTrackingData.getErrMsgNo()
				== ErrorsConstant.ERR_NUM_V21_SUCCESS)
			{
				laRequestTrackingData.setSuccessfulIndi(1);
			}
			else
			{
				laRequestTrackingData.setSuccessfulIndi(0);
			}

			try
			{
				laRequestTrackingWrite.processData(
					GeneralConstant.VISION21,
					V21Constant.V21_COMPL_REQ,
					laRequestTrackingData);
			}
			catch (RTSException aeRTSEx)
			{
				logTrackingError(
					laRequestTrackingData,
					V21TRACKINGUPDATE);
			}
		}
		//}
		//	catch (RTSException aeRTSEx)
		//	{
		//		System.err.println(
		//			"Got an RTSException and I am going to quit.");
		//		laResponse.setResult(
		//			ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
		//	}
		System.out.println("Exiting getVehicleV21VerA");
		//} // defect 10467 

		// return the response 
		return larrFinalResponse;
	}

	/**
	 * Do the requested service.
	 * 
	 * <p>This is the B version.
	 * 
	 * @param asPlateNumber String
	 * @param asLastFourOfVIN String
	 * @param asDocumentNumber String
	 * @param asVIN String
	 * @param abVinaLookupNeeded boolean
	 * @param aiV21IntrfcLogId  int 
	 * @return WsVehicleInfoV21DataResVerB[]
	 */
	public WsVehicleInfoV21DataResVerB[] getVehicleV21VerB(
		String asPlateNumber,
		String asLastFourOfVIN,
		String asDocumentNumber,
		String asVIN,
		boolean abVinaLookupNeeded,
		int aiV21UniqueId)
	{
		System.out.println("Entered getVehicleV21VerB");

		// create base response data object
		WsVehicleInfoV21DataResVerB laResponse =
			new WsVehicleInfoV21DataResVerB();
		WsVehicleInfoV21DataResVerB[] larrFinalResponse =
			new WsVehicleInfoV21DataResVerB[1];
		larrFinalResponse[0] = laResponse;

		// defect 10577 
		if (!validateData(aiV21UniqueId,
			asPlateNumber,
			asLastFourOfVIN,
			asDocumentNumber,
			asVIN,
			abVinaLookupNeeded))
		{
			laResponse.setResult(
				ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
		}
		else
		{
			// end defect 10577 

			// log entry to get vehicle
			// Continue to track VinaLookupNeeded sent by V21 although
			//  may ignore later, i.e. if no VIN 
			V21RequestData laRequestTrackingData = new V21RequestData();
			laRequestTrackingData.setRegPltNo(asPlateNumber);
			laRequestTrackingData.setLast4VIN(asLastFourOfVIN);
			laRequestTrackingData.setDocNo(asDocumentNumber);
			laRequestTrackingData.setVIN(asVIN);
			laRequestTrackingData.setVinaLookupNeeded(
				abVinaLookupNeeded);
			laRequestTrackingData.setV21IntrfcLogId(aiV21UniqueId);

			boolean lbTrackingUpdate = true;

			V21RequestTracking laRequestTrackingWrite =
				new V21RequestTracking();

			try
			{
				laRequestTrackingWrite.processData(
					GeneralConstant.VISION21,
					V21Constant.V21_GET_VEH_REQ,
					laRequestTrackingData);
			}
			catch (RTSException aeRTSEx)
			{
				lbTrackingUpdate = false;
				logTrackingError(
					laRequestTrackingData,
					V21TRACKINGINSERT);
			}

			// formulate request
			WsVehicleInfoV21DataReq laRequest =
				new WsVehicleInfoV21DataReq();
			laRequest.setRegPlate(asPlateNumber);
			laRequest.setLastFourOfVin(asLastFourOfVIN);
			laRequest.setDocumentNumber(asDocumentNumber);
			laRequest.setVIN(asVIN);

			// defect 10557
			// Do not use provided "VinaLookupNeeded" if no VIN
			//laRequest.setVinaLookupNeeded(abVinaLookupNeeded);
			laRequest.setVinaLookupNeeded(
				abVinaLookupNeeded && !UtilityMethods.isEmpty(asVIN));
			// end defect 10557

			laResponse.setV21ReqId(laRequestTrackingData.getV21ReqId());

			// Default to Invalid Response.  Change if there is a different
			// result.
			laResponse.setResult(
				ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);

			// Get the best guess vehicle record
			getVehicleRecord(
				laRequest,
				laResponse,
				laRequest.isVinaLookupNeeded());

			// reset up the response object.
			larrFinalResponse = new WsVehicleInfoV21DataResVerB[1];
			laResponse.setV21ReqId(laRequestTrackingData.getV21ReqId());
			laResponse.setV21UniqueId(aiV21UniqueId);
			larrFinalResponse[0] = laResponse;

			if (lbTrackingUpdate)
			{
				// log return
				laRequestTrackingData.setErrMsgNo(
					Integer.parseInt(laResponse.getResult()));

				if (laRequestTrackingData.getErrMsgNo()
					== ErrorsConstant.ERR_NUM_V21_SUCCESS)
				{
					laRequestTrackingData.setSuccessfulIndi(1);
				}
				else
				{
					laRequestTrackingData.setSuccessfulIndi(0);
				}

				try
				{
					laRequestTrackingWrite.processData(
						GeneralConstant.VISION21,
						V21Constant.V21_COMPL_REQ,
						laRequestTrackingData);
				}
				catch (RTSException aeRTSEx)
				{
					logTrackingError(
						laRequestTrackingData,
						V21TRACKINGUPDATE);
				}
			}
			// defect 10577 
		}
		// end defect 10577

		System.out.println("Exiting getVehicleV21VerB");
		return larrFinalResponse;
	}

	/**
	 * Look up the requested vehicle record and determine 
	 * if it is the one we are looking for.
	 * 
	 * @param aaReq WsVehicleInfoV21DataReq
	 * @param aaRes WsVehicleInfoV21DataRes
	 * @param abUseVina boolean
	 */
	private void getVehicleRecord(
		WsVehicleInfoV21DataReq aaReq,
		WsVehicleInfoV21DataRes aaRes,
		boolean abUseVina)
	{
		try
		{
			GeneralSearchData laSearchData =
				formGeneralSearchData(aaReq);

			CommonServerBusiness laCSB = new CommonServerBusiness();

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
						ErrorsConstant.ERR_NUM_V21_NOT_AVAILABLE);
					laVID.setNoMFRecs(0);
				}
				else
				{
					// end defect 10810 

					if (laVID.getNoMFRecs() == 1)
					{
 						// defect 9786 
						// Check for null VIN (know it is null for Cancelled Plate) 
						// if the requestor passed plate and last 4 of vin,
						// make sure they match the record returned.
						if (laVID.getMfVehicleData() != null
							&& laVID.getMfVehicleData().getVehicleData()
								!= null
							&& aaReq.getRegPlateReq() != null
							&& aaReq.getRegPlateReq().length() > 0
							&& aaReq.getLastFourOfVin() != null
							&& aaReq.getLastFourOfVin().length() > 0
							&& (laVID
								.getMfVehicleData()
								.getVehicleData()
								.getVin()
								== null
								|| laVID
									.getMfVehicleData()
									.getVehicleData()
									.getVin()
									.length()
									< 4
								|| !laVID
									.getMfVehicleData()
									.getVehicleData()
									.getVin()
									.substring(
										laVID
											.getMfVehicleData()
											.getVehicleData()
											.getVin()
											.length()
											- 4)
									.equals(aaReq.getLastFourOfVin())))
						{
							// Oops.  We had an issue with last 4 of vin check
							System.out.println(
								"Fail on "
									+ aaReq.getRegPlateReq()
									+ " "
									+ aaReq.getLastFourOfVin()
									+ " Last 4 of Vin not matching");
							aaRes.setResult(
								ErrorsConstant.ERR_NUM_V21_NOT_FOUND);
							laVID.setNoMFRecs(0);
						}
						else
						{
							// we have just one record to work with
							// and it matches the last 4 of vin check. 
							extractData(aaRes, laVID);
							aaRes.setResult(
								ErrorsConstant.ERR_NUM_V21_SUCCESS);
						}
					}
					else if (laVID.getNoMFRecs() < 1)
					{
						// check to see if there is an archive record that 
						// matches up
						laSearchData.setIntKey2(
							CommonConstant.SEARCH_ARCHIVE);
						laSearchData.setIntKey4(1);

						// defect 10264 
						Object laObj = laSearchData;

						// defect 
						if (abUseVina)
						{
							laObj = new Vector();
							((Vector) laObj).addElement(laVehicle);
							((Vector) laObj).addElement(laSearchData);
						}

						// search archive
						laVehicle =
							laCSB.processData(
								GeneralConstant.COMMON,
								CommonConstant.GET_VEH,
								laObj);
						// end defect 10264 

						laVID = (VehicleInquiryData) laVehicle;

						// there was an archive record

						// defect 10280
						// Implement hasPartialDataList()

						// defect 9629 
						// Include the size of partials in the check 
						if (laVID.getNoMFRecs() == 1
							//&& (laVID.getPartialDataList() == null
							//	|| laVID.getPartialDataList().size() == 0))
							&& !laVID.hasPartialDataList())
						{
							// end defect 10280 

							// defect 9786 
							// Add check for null VIN 
							// if the requestor passed plate and last 4 of vin,
							// make sure they match the record returned.
							if (laVID.getMfVehicleData() != null
								&& laVID
									.getMfVehicleData()
									.getVehicleData()
									!= null
								&& aaReq.getRegPlateReq() != null
								&& aaReq.getRegPlateReq().length() > 0
								&& aaReq.getLastFourOfVin() != null
								&& aaReq.getLastFourOfVin().length() > 0
								&& (laVID
									.getMfVehicleData()
									.getVehicleData()
									.getVin()
									== null
									|| laVID
										.getMfVehicleData()
										.getVehicleData()
										.getVin()
										.length()
										< 4
									|| !laVID
										.getMfVehicleData()
										.getVehicleData()
										.getVin()
										.substring(
											laVID
												.getMfVehicleData()
												.getVehicleData()
												.getVin()
												.length()
												- 4)
										.equals(
											aaReq.getLastFourOfVin())))
							{
								// Oops.  We had an issue with last 4 of vin check
								System.out.println(
									"Fail on "
										+ aaReq.getRegPlateReq()
										+ " "
										+ aaReq.getLastFourOfVin()
										+ " Last 4 of Vin not matching "
										+ "(Archive)");
								aaRes.setResult(
									ErrorsConstant
										.ERR_NUM_V21_NOT_FOUND);
								laVID.setNoMFRecs(0);
							}
							// end defect 9786 
							else
							{
								// we have just one record to work with
								// and it matches the last 4 of vin check. 
								extractData(aaRes, laVID);
								aaRes.setResult(
									ErrorsConstant.ERR_NUM_V21_SUCCESS);
							}
						}
						// defect 10280 
						// Implement hasPartialDataList 
						//  (which includes size of partials in check)
						else if (
							laVID.getNoMFRecs() > 1
							//|| laVID.getPartialDataList() != null)
								|| laVID.hasPartialDataList())
						{
							// end defect 10280 

							// multiple records found
							aaRes.setResult(
								ErrorsConstant
									.ERR_NUM_V21_MULTIPLE_RECS);
						}
						else
						{
							// no records
							aaRes.setResult(
								ErrorsConstant.ERR_NUM_V21_NOT_FOUND);
						}

						// There was no record found
						if (laVID.getNoMFRecs() == 0 && abUseVina)
						{
							// send back the VINA response though.
							extractData(aaRes, laVID);
							// defect 10280
							// No longer Needed  
							//aaRes.setResult(
							//	ErrorsConstant.ERR_NUM_V21_NOT_FOUND
						}
						// end defect 10280 
					}
					else
					{
						// This should not happen.  V21 requests are handled
						// like IRENEW.
						// customer will have to go to county for processing.
						aaRes.setResult(
							ErrorsConstant.ERR_NUM_V21_MULTIPLE_RECS);
					}
					// defect 10810
				}
				// end defect 10810 
			}
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Got an RTSException on vehicle lookup");
			aeRTSEx.printStackTrace();

			// send back invalid request to v21
			aaRes.setResult(ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
		}
		catch (Exception aeEx)
		{
			System.err.println("Got an Exception on vehicle lookup");
			aeEx.printStackTrace();

			// send back invalid request to v21
			aaRes.setResult(ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST);
		}
	}

	/**
	 * Copy data from vehicle record to response object.
	 * 
	 * @param aaRes
	 * @param aaVID
	 */
	private void extractData(
		WsVehicleInfoV21DataRes aaRes,
		VehicleInquiryData aaVID)
	{
		if (aaVID.getMfVehicleData() != null)
		{
			// get the indicator values if this is at least Version A
			if (aaVID.getNoMFRecs() == 1
				&& aaRes instanceof WsVehicleInfoV21DataResVerA)
			{
				// if there was a record returned, get the indicators
				if (aaVID.getNoMFRecs() == 1)
				{
					getIndicatorArray(
						(WsVehicleInfoV21DataResVerA) aaRes,
						aaVID);
				}
				else
				{
					// otherwise, return an empty array.
					(
						(
							WsVehicleInfoV21DataResVerA) aaRes)
								.setIndicators(
						new WsVehicleInfoV21Indicator[0]);
				}
			}

			// see if we can get Vehicle Data
			if (aaVID.getMfVehicleData().getVehicleData() != null)
			{
				aaRes.setMake(
					aaVID
						.getMfVehicleData()
						.getVehicleData()
						.getVehMk());
				aaRes.setModel(
					aaVID
						.getMfVehicleData()
						.getVehicleData()
						.getVehModl());
				aaRes.setModelYear(
					aaVID
						.getMfVehicleData()
						.getVehicleData()
						.getVehModlYr());
				aaRes.setBodyStyle(
					aaVID
						.getMfVehicleData()
						.getVehicleData()
						.getVehBdyType());
				aaRes.setVIN(
					aaVID.getMfVehicleData().getVehicleData().getVin());
			}

			// see if we can get Title Data
			if (aaVID.getMfVehicleData().getTitleData() != null)
			{
				aaRes.setDocumentNumber(
					aaVID.getMfVehicleData().getTitleData().getDocNo());

				// if the response is of type VerA, than set the 
				// Title Issue Date.
				if (aaRes instanceof WsVehicleInfoV21DataResVerA)
				{
					(
						(
							WsVehicleInfoV21DataResVerA) aaRes)
								.setTtlIssueDate(
						aaVID
							.getMfVehicleData()
							.getTitleData()
							.getTtlIssueDate());
				}
			}

			// see if we can get Regis Data
			if (aaVID.getMfVehicleData().getRegData() != null)
			{
				aaRes.setPlateNumber(
					aaVID
						.getMfVehicleData()
						.getRegData()
						.getRegPltNo());
				aaRes.setPlateType(
					aaVID
						.getMfVehicleData()
						.getRegData()
						.getRegPltCd());
				aaRes.setRegClassCode(
					aaVID
						.getMfVehicleData()
						.getRegData()
						.getRegClassCd());
				// TODO need to pick up plate status later.
				aaRes.setPlateStatus("");
				aaRes.setRegExpMonth(
					aaVID
						.getMfVehicleData()
						.getRegData()
						.getRegExpMo());
				aaRes.setRegExpYear(
					aaVID
						.getMfVehicleData()
						.getRegData()
						.getRegExpYr());
				aaRes.setPlateCreatedDate(
					aaVID
						.getMfVehicleData()
						.getRegData()
						.getPltBirthDate());
			}

			// see if we can get Special Regis Data
			if (aaVID.getMfVehicleData().getSpclPltRegisData() != null)
			{
				aaRes.setSpecialRegId(
					aaVID
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getSpclRegId());
			}

			// see if we can get Owner Data
			if (aaVID.getMfVehicleData().getOwnerData() != null)
			{
				// defect 10112 
				aaRes.setNameOnTitleLine1(
					aaVID.getMfVehicleData().getOwnerData().getName1());
				aaRes.setNameOnTitleLine2(
					aaVID.getMfVehicleData().getOwnerData().getName2());
				// end defect 10112 
			}
		}
	}

	/**
	 * Lookup the applicable Indicators and add them to the response.
	 * 
	 * @param aaRes
	 * @param aaVID
	 */
	private void getIndicatorArray(
		WsVehicleInfoV21DataResVerA aaRes,
		VehicleInquiryData aaVID)
	{
		IndicatorLookup laIndiLookup = new IndicatorLookup();
		Vector lvIndis =
			laIndiLookup.getIndicatorsV21(
				aaVID.getMfVehicleData(),
				TransCdConstant.V21VTN,
				IndicatorLookup.V21);
		WsVehicleInfoV21Indicator[] larrIndis =
			new WsVehicleInfoV21Indicator[lvIndis.size()];
		for (int i = 0; i < lvIndis.size(); i++)
		{
			IndicatorData laID = (IndicatorData) lvIndis.elementAt(i);
			WsVehicleInfoV21Indicator laVI21Indi =
				new WsVehicleInfoV21Indicator();

			// defect 9651
			//laVI21Indi.setIndiName(laID.getDesc());
			//laVI21Indi.setIndiValue(laID.getStopCode());
			laVI21Indi.setIndiName(laID.getIndiName());
			laVI21Indi.setIndiValue(laID.getDesc());
			// end defect 9651
			larrIndis[i] = laVI21Indi;
		}
		aaRes.setIndicators(larrIndis);
	}

	/**
	 * Formulate the GeneralSearchData object used for searching.
	 * 
	 * @param aaReq WsVehicleInfoV21DataReq
	 * @return GeneralSearchData
	 */
	private GeneralSearchData formGeneralSearchData(WsVehicleInfoV21DataReq aaReq)
		throws RTSException
	{
		// set up the search data
		GeneralSearchData laTempSearchData = new GeneralSearchData();

		// always search Active / Inactive first.
		laTempSearchData.setIntKey2(
			CommonConstant.SEARCH_ACTIVE_INACTIVE);

		// Use the V21 TransCd Constants.
		if (aaReq.isVinaLookupNeeded())
		{
			laTempSearchData.setKey3(TransCdConstant.V21VTN);
		}
		else
		{
			laTempSearchData.setKey3(TransCdConstant.V21PLD);
		}

		laTempSearchData.setKey5("V21");

		// populate search data according to data provided
		if (aaReq.getDocumentNumberReq() != null
			&& aaReq.getDocumentNumberReq().length() > 0)
		{
			laTempSearchData.setKey1(CommonConstant.DOC_NO);
			laTempSearchData.setKey2(aaReq.getDocumentNumberReq());
			aaReq.setVinaLookupNeeded(false);
		}
		else if (
			aaReq.getVINReq() != null
				&& aaReq.getVINReq().length() > 0)
		{
			laTempSearchData.setKey1(CommonConstant.VIN);
			laTempSearchData.setKey2(aaReq.getVINReq());
		}
		else if (
			aaReq.getRegPlateReq() != null
				&& aaReq.getLastFourOfVin() != null)
		{
			laTempSearchData.setKey1(CommonConstant.REG_PLATE_NO);
			laTempSearchData.setKey2(aaReq.getRegPlateReq());
			// defect 9818 
			// Set GSD Key4 to LastFourOfVin
			laTempSearchData.setKey4(aaReq.getLastFourOfVin());
			// end defect 9818 
			aaReq.setVinaLookupNeeded(false);
		}
		else
		{
			throw (
				new RTSException(
					ErrorsConstant.ERR_NUM_V21_INVALID_REQUEST));
		}

		return laTempSearchData;
	}

	/**
	 * Log Tracking Error
	 * 
	 * @param aaRequestData
	 * @param asAction 
	 */
	private void logTrackingError(
		V21RequestData aaRequestData,
		String asAction)
	{
		// defect 10577 
		// Use SEPARATOR

		// defect 10482 
		// Add IntrfcLogId 
		String lsLog =
			"V21 Tracking Error: "
				+ asAction
				+ "V21ReqId: "
				+ aaRequestData.getV21ReqId()
				+ SEPARATOR
				+ "IntrfcLogId: "
				+ aaRequestData.getV21IntrfcLogId()
				+ SEPARATOR
				+ "DocNo: "
				+ aaRequestData.getDocNo()
				+ SEPARATOR
				+ "VIN: "
				+ aaRequestData.getVIN()
				+ SEPARATOR
				+ "Last4VIN: "
				+ aaRequestData.getLast4VIN()
				+ SEPARATOR
				+ "PlateNo: "
				+ aaRequestData.getRegPltNo()
				+ SEPARATOR
				+ "VINA LookUp: "
				+ (aaRequestData.getVinaLookupIndi() == 1
					? "true"
					: "false");
		// end defect 10482 
		// end defect 10577 

		Log.write(Log.SQL_EXCP, this, lsLog);
		System.err.println(lsLog);
	}

	/**
	 * Validate Data
	 *
	 * @return boolean
	 */
	private boolean validateData(
		int aiV21UniqueId,
		String asPlateNumber,
		String asLastFourOfVIN,
		String asDocumentNumber,
		String asVIN,
		boolean abVinaLookupNeeded)
	{
		boolean lbValid =
			!(UtilityMethods.isEmpty(asPlateNumber)
				&& UtilityMethods.isEmpty(asLastFourOfVIN)
				&& UtilityMethods.isEmpty(asDocumentNumber)
				&& UtilityMethods.isEmpty(asVIN));

		if (lbValid)
		{
			lbValid =
				(asLastFourOfVIN == null
					|| asLastFourOfVIN.length() <= 4)
					&& (asDocumentNumber == null
						|| asDocumentNumber.length()
							<= CommonConstant.LENGTH_DOCNO)
					&& (asVIN == null
						|| asVIN.length()
							<= CommonConstant.LENGTH_VIN_MAX)
					&& (asPlateNumber == null
						|| asPlateNumber.length()
							<= CommonConstant.LENGTH_PLTNO);
		}
		if (!lbValid)
		{
			System.err.println(
				"V21 Get Vehicle Invalid Data: "
					+ "IntrfcLogId: "
					+ aiV21UniqueId
					+ SEPARATOR
					+ "DocNo: "
					+ asDocumentNumber
					+ SEPARATOR
					+ "VIN: "
					+ asVIN
					+ SEPARATOR
					+ "Last4VIN: "
					+ asLastFourOfVIN
					+ SEPARATOR
					+ "PlateNo: "
					+ asPlateNumber
					+ SEPARATOR
					+ "VINA LookUp: "
					+ abVinaLookupNeeded);
		}
		return lbValid;
	}
}
