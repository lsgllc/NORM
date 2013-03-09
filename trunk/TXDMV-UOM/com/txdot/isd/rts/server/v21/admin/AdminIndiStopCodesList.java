package com.txdot.isd.rts.server.v21.admin;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.IndicatorStopCodesCache;
import com.txdot.isd.rts.services.data.IndicatorStopCodesData;
import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.V21Constant;

import com.txdot.isd.rts.server.v21.admin.data.WsIndiStopCodesData;
import com.txdot.isd.rts.server.v21.admin.data.WsIndiStopCodesRes;
import com.txdot.isd.rts.server.v21.request.tracking.V21RequestTracking;

/*
 * AdminIndiStopCodesList.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	01/14/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/14/2008	Add Request Tracking
 * 							modify getIndiStopCodesList()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * K Harrell	07/28/2010	add getIndiStopCodesListVerA()
 * 							defect 10482 Ver 6.5.0   
 * ---------------------------------------------------------------------
 */

/**
 * Return the list of all Stop Code Indicators to the requestor.
 *
 * @version	6.5.0  			07/28/2010
 * @author	B Hargrove
 * <br>Creation Date:		01/14/2008 11:21
 */
public class AdminIndiStopCodesList
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

		AdminIndiStopCodesList laClass = new AdminIndiStopCodesList();
		WsIndiStopCodesData[] larrIndiStopCodes =
			laClass.getIndiStopCodesList();

		for (int i = 0; i < larrIndiStopCodes.length; i++)
		{
			WsIndiStopCodesData laObject =
				(WsIndiStopCodesData) larrIndiStopCodes[i];
			System.out.println(
				laObject.getIndiTransCode()
					+ " "
					+ laObject.getIndiName()
					+ " "
					+ laObject.getIndiFieldValue()
					+ " "
					+ laObject.getIndiStopCode());
		}
	}

	/**
	 * Returns an array of all Stop Code Indicators.
	 * 
	 * @return WsIndiStopCodesData[]
	 */
	public WsIndiStopCodesData[] getIndiStopCodesList()
	{
		// log entry to IndiStopCodes
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRTSTblName("IndiStopCodes");
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

		Vector lvIndiStopCodesList =
			IndicatorStopCodesCache.getIndiStopCdVec();

		Object[] larrTemp = lvIndiStopCodesList.toArray();
		WsIndiStopCodesData[] larrIndiStopCodes =
			new WsIndiStopCodesData[larrTemp.length];

		// loop through and create the new passable objects
		for (int i = 0; i < larrTemp.length; i++)
		{
			IndicatorStopCodesData laOldData =
				(IndicatorStopCodesData) larrTemp[i];
			WsIndiStopCodesData laNewData = new WsIndiStopCodesData();
			laNewData.setIndiTransCode(laOldData.getIndiTransCd());
			laNewData.setIndiName(laOldData.getIndiName());
			laNewData.setIndiFieldValue(laOldData.getIndiFieldValue());
			laNewData.setIndiStopCode(laOldData.getIndiStopCd());
			larrIndiStopCodes[i] = laNewData;
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

		return larrIndiStopCodes;
	}

	/**
	 * Returns WsIndiStopCodesRes Object which contains: 
	 *   - array of all IndiStopCodes
	 *   - V21UniqueId 
	 *   - V21ReqId 
	 * 
	 * @param aiV21UniqueId 
	 * @return WsIndiStopCodesRes
	 */
	public WsIndiStopCodesRes getIndiStopCodesListVerA(int aiV21UniqueId)
	{
		// log entry to IndiStopCodes
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRTSTblName("IndiStopCodes");
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

		Vector lvIndiStopCodesList =
			IndicatorStopCodesCache.getIndiStopCdVec();

		Object[] larrTemp = lvIndiStopCodesList.toArray();
		WsIndiStopCodesData[] larrIndiStopCodes =
			new WsIndiStopCodesData[larrTemp.length];

		// loop through and create the new passable objects
		for (int i = 0; i < larrTemp.length; i++)
		{
			IndicatorStopCodesData laOldData =
				(IndicatorStopCodesData) larrTemp[i];
			WsIndiStopCodesData laNewData = new WsIndiStopCodesData();
			laNewData.setIndiTransCode(laOldData.getIndiTransCd());
			laNewData.setIndiName(laOldData.getIndiName());
			laNewData.setIndiFieldValue(laOldData.getIndiFieldValue());
			laNewData.setIndiStopCode(laOldData.getIndiStopCd());
			larrIndiStopCodes[i] = laNewData;
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

		WsIndiStopCodesRes laWsIndiStopCodesRes =
			new WsIndiStopCodesRes();
		laWsIndiStopCodesRes.setIndiStopCodes(larrIndiStopCodes);
		laWsIndiStopCodesRes.setV21UniqueId(aiV21UniqueId);
		laWsIndiStopCodesRes.setV21ReqId(
			laRequestTrackingData.getV21ReqId());

		return laWsIndiStopCodesRes;
	}
}
