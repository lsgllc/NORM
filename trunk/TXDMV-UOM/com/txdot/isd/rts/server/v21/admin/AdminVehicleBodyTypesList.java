package com.txdot.isd.rts.server.v21.admin;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.VehicleBodyTypesCache;
import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.data.VehicleBodyTypesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.V21Constant;

import com.txdot.isd.rts.server.v21.admin.data.WsVehicleBodyTypesData;
import com.txdot.isd.rts.server.v21.admin.data.WsVehicleBodyTypesRes;
import com.txdot.isd.rts.server.v21.request.tracking.V21RequestTracking;

/*
 * AdminVehicleBodyTypesList.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	01/11/2008	New class 
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/14/2008	Add Request Tracking.
 * 							modify getVehicleBodyTypesList()
 * 							defect 9502 Ver 3_Amigos_PH_A 
 * K Harrell	07/28/2010	add getVehBodyTypesListVerA()
 * 							defect 10482 Ver 6.5.0   
 * ---------------------------------------------------------------------
 */

/**
 * Return the list of all Vehicle Body Types to the requestor.
 *
 * @version	6.5.0   		07/28/2010
 * @author	B Hargrove
 * <br>Creation Date:		01/10/2008 13:34
 */
public class AdminVehicleBodyTypesList
{
	// Before running main, run the RTS application first to build cache
	public static void main(String[] aarrArgs)
	{
		// load cache 
		try
		{
			CacheManager.loadCache();
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
			System.exit(16);
		}

		AdminVehicleBodyTypesList laClass =
			new AdminVehicleBodyTypesList();
		WsVehicleBodyTypesData[] larrVehicleBodyTypes =
			laClass.getVehicleBodyTypesList();

		for (int i = 0; i < larrVehicleBodyTypes.length; i++)
		{
			WsVehicleBodyTypesData laObject =
				(WsVehicleBodyTypesData) larrVehicleBodyTypes[i];
			System.out.println(
				laObject.getVehicleBodyType()
					+ " "
					+ laObject.getVehicleBodyDescription());
		}
	}

	/**
	 * Returns an array of all Vehicle Body Types.
	 * 
	 * @return WsVehicleBodyTypesData[]
	 */
	public WsVehicleBodyTypesData[] getVehicleBodyTypesList()
	{
		// log entry to IndiStopCodes
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRTSTblName("VehicleBodyTypes");
		V21RequestTracking laRequestTrackingWrite =
			new V21RequestTracking();

		try
		{
			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_GET_ADMIN_TBL_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking");
			aeRTSEx.printStackTrace();
		}

		Vector lvVehicleBodyTypesList =
			VehicleBodyTypesCache.getVehBdyTypesVec();

		Object[] larrTemp = lvVehicleBodyTypesList.toArray();
		WsVehicleBodyTypesData[] larrVehicleBodyTypesList =
			new WsVehicleBodyTypesData[larrTemp.length];

		// loop through and create the new passable objects
		for (int i = 0; i < larrTemp.length; i++)
		{
			VehicleBodyTypesData laOldData =
				(VehicleBodyTypesData) larrTemp[i];
			WsVehicleBodyTypesData laNewData =
				new WsVehicleBodyTypesData();
			laNewData.setVehicleBodyType(laOldData.getVehBdyType());
			laNewData.setVehicleBodyDescription(
				laOldData.getVehBdyTypeDesc());
			larrVehicleBodyTypesList[i] = laNewData;
		}

		// log completion
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

		return larrVehicleBodyTypesList;
	}

	/**
	 * Returns WsVehicleBodyTypesRes Object which contains: 
	 *   - array of all VehicleBodyTypes
	 *   - V21UniqueId 
	 *   - V21ReqId 
	 * 
	 * @param aiV21UniqueId 
	 * @return WsVehicleBodyTypesRes
	 */
	public WsVehicleBodyTypesRes getVehicleBodyTypesListVerA(int aiV21UniqueId)
	{
		// log entry to IndiStopCodes
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRTSTblName("VehicleBodyTypes");
		laRequestTrackingData.setV21IntrfcLogId(aiV21UniqueId);

		V21RequestTracking laRequestTrackingWrite =
			new V21RequestTracking();

		try
		{
			laRequestTrackingWrite.processData(
				GeneralConstant.VISION21,
				V21Constant.V21_GET_ADMIN_TBL_REQ,
				laRequestTrackingData);
		}
		catch (RTSException aeRTSEx)
		{
			System.err.println("Problem with Request Tracking");
			aeRTSEx.printStackTrace();
		}

		Vector lvVehicleBodyTypesList =
			VehicleBodyTypesCache.getVehBdyTypesVec();

		Object[] larrTemp = lvVehicleBodyTypesList.toArray();
		WsVehicleBodyTypesData[] larrVehicleBodyTypesList =
			new WsVehicleBodyTypesData[larrTemp.length];

		// loop through and create the new passable objects
		for (int i = 0; i < larrTemp.length; i++)
		{
			VehicleBodyTypesData laOldData =
				(VehicleBodyTypesData) larrTemp[i];
			WsVehicleBodyTypesData laNewData =
				new WsVehicleBodyTypesData();
			laNewData.setVehicleBodyType(laOldData.getVehBdyType());
			laNewData.setVehicleBodyDescription(
				laOldData.getVehBdyTypeDesc());
			larrVehicleBodyTypesList[i] = laNewData;
		}

		// log completion
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

		WsVehicleBodyTypesRes laWsVehicleBodyTypesRes =
			new WsVehicleBodyTypesRes();
		laWsVehicleBodyTypesRes.setVehicleBodyTypes(
			larrVehicleBodyTypesList);
		laWsVehicleBodyTypesRes.setV21UniqueId(aiV21UniqueId);
		laWsVehicleBodyTypesRes.setV21ReqId(
			laRequestTrackingData.getV21ReqId());

		return laWsVehicleBodyTypesRes;
	}

}
