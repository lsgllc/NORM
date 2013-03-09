package com.txdot.isd.rts.server.v21.admin;

import java.util.Vector;

import com.txdot.isd.rts.server.v21.admin.data.WsClassToPlateData;
import com.txdot.isd.rts.server.v21.admin.data.WsClassToPlateDataVerB;
import com.txdot.isd.rts.server.v21.admin.data.WsClassToPlateRes;
import com.txdot.isd.rts.server.v21.admin.data.WsClassToPlateResVerB;
import com.txdot.isd.rts.server.v21.request.tracking.V21RequestTracking;
import com.txdot.isd.rts.services.cache.ClassToPlateCache;
import com.txdot.isd.rts.services.data.ClassToPlateData;
import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.V21Constant;

/*
 * AdminClassToPlateList.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	01/16/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/13/2008	Add Request Tracking.
 * 							modify getClassToPlateList()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * K Harrell	07/28/2010	add getClassToPlateListVerA()
 * 							defect 10482 Ver 6.5.0 
 * Ray Rowehl	01/13/2012	Add Version B handling so we can return
 * 								VTPEligibleIndi.
 * 							This ends up being a significant re-write.
 * 								The function of A has not change.  Just 
 * 								refactored to use common methods.
 * 							Remove the oldest version of 
 * 								getClassToPlateList since it is no 
 * 								longer required.
 * 							Delete main since we have to be running
 * 								on the server to make db calls.
 * 							add SA_WS_ADMIN_CLASS_TO_PLT, SC_VERSION_A,
 * 								SC_VERSION_B, 
 * 								SP_PROBLEM_WITH_REQUEST_TRACKING
 * 							add getClassToPlateListVerB(int),
 * 								loadData(String), logCallBegin(), 
 * 								logCallComplete() 
 * 							delete main()
 * 							modify getClassToPlateListVerA(int)
 * 							deprecate getClassToPlateListVerA(int)
 * 							defect 11224 Ver 6.10.0
 * Ray Rowehl	01/30/2012	Add a log write entry to help track when 
 * 							version A is called.
 * 							add SC_VER_A_CALLED
 * 							modify getClassToPlateListVerA()
 * 							defect 11224 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Return the list of all DocumentTypes to the requestor.
 * 
 * @version 6.10.0 		01/30/2012
 * @author B Hargrove
 * @author Ray Rowehl 
 * <br>Creation Date: 	01/14/2008 14:04
 */

public class AdminClassToPlateList
{
	private static final String SA_WS_ADMIN_CLASS_TO_PLT = "ClassToPlt";

	private static final String SC_VER_A_CALLED = 
		"AdminClassToPlateList.getClassToPlateListVerA called";

	private static final String SC_VERSION_A = "A";

	private static final String SC_VERSION_B = "B";

	private static final String SP_PROBLEM_WITH_REQUEST_TRACKING = 
		"Problem with Request Tracking ";

	/**
	 * Returns WsClassToPlateRes Object which contains:
	 * <ul>
	 * <li>array of all Class to Plate records
	 * <li>V21UniqueId
	 * <li>V21ReqId <eul>
	 * 
	 * <p>
	 * Version A.
	 * 
	 * @param aiV21UniqueId
	 * @return WsClassToPlateRes
	 * @deprecated
	 */
	public WsClassToPlateRes getClassToPlateListVerA(int aiV21UniqueId)
	{
		// log entry to ClassToPlate
		V21RequestData laRequestTrackingData = new V21RequestData();

		logCallBegin(aiV21UniqueId, laRequestTrackingData);

		WsClassToPlateData[] larrClassToPlateList = loadDataVerA(SC_VERSION_A);

		WsClassToPlateRes laWsClassToPlateRes = new WsClassToPlateRes();
		laWsClassToPlateRes
				.setClassToPlate((WsClassToPlateData[]) larrClassToPlateList);
		laWsClassToPlateRes.setV21UniqueId(aiV21UniqueId);
		laWsClassToPlateRes.setV21ReqId(laRequestTrackingData
				.getV21ReqId());

		logCallComplete(aiV21UniqueId, laRequestTrackingData);
		
		// write visit to log in case we are trying to figure out
		// if they switch to Version B yet.
		// All calls for AdminClassToPlateList minus these log 
		// entries gives the total of VerB calls.
		Log.write(Log.SQL_EXCP, laWsClassToPlateRes, SC_VER_A_CALLED);

		return laWsClassToPlateRes;
	}

	/**
	 * Returns WsClassToPlateResVerB Object which contains:
	 * <ul>
	 * <li>array of all Class to Plate records
	 * <li>V21UniqueId
	 * <li>V21ReqId <eul>
	 * 
	 * <p>
	 * Version B.
	 * 
	 * @param aiV21UniqueId
	 * @return WsClassToPlateResVerB
	 */
	public WsClassToPlateResVerB getClassToPlateListVerB(
			int aiV21UniqueId)
	{
		V21RequestData laRequestTrackingData = new V21RequestData();

		logCallBegin(aiV21UniqueId, laRequestTrackingData);

		WsClassToPlateDataVerB[] larrClassToPlateList = loadData(SC_VERSION_B);

		WsClassToPlateResVerB laWsClassToPlateResVerB = new WsClassToPlateResVerB();
		laWsClassToPlateResVerB
				.setClassToPlate((WsClassToPlateDataVerB[]) larrClassToPlateList);
		laWsClassToPlateResVerB.setV21UniqueId(aiV21UniqueId);
		laWsClassToPlateResVerB.setV21ReqId(laRequestTrackingData
				.getV21ReqId());

		logCallComplete(aiV21UniqueId, laRequestTrackingData);

		return laWsClassToPlateResVerB;
	}

	/**
	 * Load the Class to Plate data.
	 * 
	 * @param asType
	 * @return WsClassToPlateDataVerB[]
	 */
	private WsClassToPlateDataVerB[] loadData(String asType)
	{
		Vector lvClassToPlateCache = ClassToPlateCache
				.getClassToPlateVec();

		Object[] larrTemp = lvClassToPlateCache.toArray();

		WsClassToPlateDataVerB[] larrClassToPlateList = new WsClassToPlateDataVerB[larrTemp.length];

		// loop through and create the new passable objects
		for (int i = 0; i < larrTemp.length; i++)
		{
			ClassToPlateData laOldData = (ClassToPlateData) larrTemp[i];

			WsClassToPlateDataVerB laNewData = new WsClassToPlateDataVerB();

			laNewData.setRegistrationClassCode(laOldData
					.getRegClassCd());
			laNewData.setRegistrationPlateCode(laOldData.getRegPltCd());
			laNewData.setReplacementPlateCode(laOldData.getReplPltCd());
			laNewData.setPTOEligibleIndi(laOldData.getPTOElgbleIndi());
			laNewData.setVTPEligibleIndi(laOldData.getVTPElgbleIndi());

			larrClassToPlateList[i] = laNewData;
		}
		return larrClassToPlateList;
	}
	
	/**
	 * Load the Class to Plate data for Ver A.
	 * 
	 * <p>This actually acts as a conversion filter on top of loadData().
	 * 
	 * @param asType
	 * @return WsClassToPlateData[]
	 */
	private WsClassToPlateData[] loadDataVerA(String asType)
	{
		WsClassToPlateDataVerB[] larrTemp = loadData(asType);
		WsClassToPlateData[] larrClassToPlateList = new WsClassToPlateData[larrTemp.length];
		
		for (int i = 0; i < larrTemp.length; i++)
		{
			WsClassToPlateData laElement = (WsClassToPlateData) larrTemp[i];
			WsClassToPlateData laNewElement = new WsClassToPlateData();
			laNewElement.setRegistrationClassCode(laElement.getRegistrationClassCode());
			laNewElement.setRegistrationPlateCode(laElement.getRegistrationPlateCode());
			laNewElement.setReplacementPlateCode(laElement.getReplacementPlateCode());
			laNewElement.setPTOEligibleIndi(laElement.getPTOEligibleIndi());
			larrClassToPlateList[i] = laNewElement;
		}
		
		return larrClassToPlateList;
	}

	/**
	 * Log the call start.
	 * 
	 * @param aiV21UniqueId
	 * @param aaRequestTrackingData
	 */
	private void logCallBegin(int aiV21UniqueId,
			V21RequestData aaRequestTrackingData)
	{
		// log entry to ClassToPlate

		aaRequestTrackingData.setRTSTblName(SA_WS_ADMIN_CLASS_TO_PLT);
		aaRequestTrackingData.setV21IntrfcLogId(aiV21UniqueId);

		V21RequestTracking laRequestTrackingWrite = new V21RequestTracking();

		try
		{
			laRequestTrackingWrite.processData(
					GeneralConstant.VISION21,
					V21Constant.V21_GET_ADMIN_TBL_REQ,
					aaRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println(SP_PROBLEM_WITH_REQUEST_TRACKING
					+ SA_WS_ADMIN_CLASS_TO_PLT);
			aeRTSEx.printStackTrace();
		}
	}

	/**
	 * Log that the call has completed.
	 * 
	 * @param aiV21UniqueId
	 * @param aaRequestTrackingData
	 */
	private void logCallComplete(int aiV21UniqueId,
			V21RequestData aaRequestTrackingData)
	{
		V21RequestTracking laRequestTrackingWrite = new V21RequestTracking();

		try
		{
			aaRequestTrackingData.setSuccessfulIndi(1);
			laRequestTrackingWrite.processData(
					GeneralConstant.VISION21,
					V21Constant.V21_COMPL_REQ, aaRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking"
					+ SA_WS_ADMIN_CLASS_TO_PLT);
			aeRTSEx.printStackTrace();
		}
	}
}
