package com.txdot.isd.rts.server.v21.admin;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.VehicleMakesCache;
import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.data.VehicleMakesData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.V21Constant;

import com.txdot.isd.rts.server.v21.admin.data.WsVehicleMakesData;
import com.txdot.isd.rts.server.v21.admin.data.WsVehicleMakesRes;
import com.txdot.isd.rts.server.v21.request.tracking.V21RequestTracking;

/*
 * AdminVehMakesList.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/27/2007	New class
 * 							defect 9502 Ver FRVP 
 * Ray Rowehl	02/14/2008	Add Request Tracking.
 * 							modify getVehMakeList()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * K Harrell	07/28/2010	add getVehMakeListVerA()
 * 							defect 10482 Ver 6.5.0   
 * R Pilon		02/02/2012	Change call to method setVehicleMakeDesc() to 
 * 							  setVehicleMakeDescription() to prevent web 
 * 							  service validation error.
 * 							modify getVehMakeList(), getVehMakeListVerA()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Return the list of all Vehicle Makes to the requestor.
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		12/27/2007 11:11:55
 */
public class AdminVehMakesList
{
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

		AdminVehMakesList laClass = new AdminVehMakesList();
		WsVehicleMakesData[] larrVehMake = laClass.getVehMakeList();

		for (int i = 0; i < larrVehMake.length; i++)
		{
			WsVehicleMakesData laObject =
				(WsVehicleMakesData) larrVehMake[i];
			System.out.println(
				laObject.getVehicleMake()
					+ " "
					+ laObject.getVehicleMakeDescription());
		}
	}

	/**
	 * Returns an array of all Vehicle Makes.
	 * 
	 * @return WsVehicleMakesData[]
	 */
	public WsVehicleMakesData[] getVehMakeList()
	{
		// log entry to IndiStopCodes
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRTSTblName("VehMake");
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

		Vector lvVehMakeList = VehicleMakesCache.getVehMks();

		Object[] larrTemp = lvVehMakeList.toArray();
		WsVehicleMakesData[] larrVehMakeList =
			new WsVehicleMakesData[larrTemp.length];

		// loop through and create the new passable objects
		for (int i = 0; i < larrTemp.length; i++)
		{
			VehicleMakesData laOldData = (VehicleMakesData) larrTemp[i];
			WsVehicleMakesData laNewData = new WsVehicleMakesData();
			laNewData.setVehicleMake(laOldData.getVehMk());
			// defect 11135
			laNewData.setVehicleMakeDescription(laOldData.getVehMkDesc());
			// end defect 11135
			larrVehMakeList[i] = laNewData;
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

		return larrVehMakeList;
	}
	/**
	 * Returns WsVehicleMakesRes Object which contains: 
	 *   - array of all VehicleMakes
	 *   - V21UniqueId 
	 *   - V21ReqId 
	 * 
	 * @param aiV21UniqueId 
	 * @return WsVehicleBodyTypesRes
	 */
	public WsVehicleMakesRes getVehMakeListVerA(int aiV21UniqueId)
	{
		// log entry to IndiStopCodes
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRTSTblName("VehMake");
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

		Vector lvVehMakeList = VehicleMakesCache.getVehMks();

		Object[] larrTemp = lvVehMakeList.toArray();
		WsVehicleMakesData[] larrVehMakeList =
			new WsVehicleMakesData[larrTemp.length];

		// loop through and create the new passable objects
		for (int i = 0; i < larrTemp.length; i++)
		{
			VehicleMakesData laOldData = (VehicleMakesData) larrTemp[i];
			WsVehicleMakesData laNewData = new WsVehicleMakesData();
			laNewData.setVehicleMake(laOldData.getVehMk());
			// defect 11135
			laNewData.setVehicleMakeDescription(laOldData.getVehMkDesc());
			// end defect 11135
			larrVehMakeList[i] = laNewData;
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

		WsVehicleMakesRes laWsVehicleMakesRes = new WsVehicleMakesRes();
		laWsVehicleMakesRes.setVehicleMakes(larrVehMakeList);
		laWsVehicleMakesRes.setV21UniqueId(aiV21UniqueId);
		laWsVehicleMakesRes.setV21ReqId(
			laRequestTrackingData.getV21ReqId());

		return laWsVehicleMakesRes;
	}

}
