package com.txdot.isd.rts.server.v21.admin;

import java.util.Vector;

import com.txdot.isd.rts.server.v21.admin.data.WsIndiDescsData;
import com.txdot.isd.rts.server.v21.admin.data.WsIndiDescsRes;
import com.txdot.isd.rts.server.v21.request.tracking.V21RequestTracking;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.IndicatorDescriptionsCache;
import com.txdot.isd.rts.services.data.IndicatorDescriptionsData;
import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.V21Constant;

/*
 * AdminIndiDescsList.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	02/08/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/14/2008	Add Request Tracking
 * 							modify getIndiDescsList()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * K Harrell	07/28/2010	add getIndiDescsListVerA()
 * 							defect 10482 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */

/**
 * Return the list of all Indicator Descriptions to the requestor.
 *
 * @version	6.5.0			07/28/2010
 * @author	B Hargrove
 * <br>Creation Date:		01/10/2008 09:20
 */
public class AdminIndiDescsList
{
	// Flag to return all Indicator Descriptions.
	public final static int GET_ALL = 0;

	// Before running main, run the RTS application first to build cache
	public static void main(String[] args)
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

		AdminIndiDescsList laClass = new AdminIndiDescsList();
		WsIndiDescsData[] larrIndiDescs = laClass.getIndiDescsList();

		for (int i = 0; i < larrIndiDescs.length; i++)
		{
			WsIndiDescsData laObject =
				(WsIndiDescsData) larrIndiDescs[i];
			System.out.println(
				laObject.getIndiName()
					+ " "
					+ laObject.getIndiFieldValue()
					+ " "
					+ laObject.getIndiDesc()
					+ " "
					+ laObject.getIndiScrnPriority());
		}
	}

	/**
	 * Returns an array of all Indicator Descriptions.
	 * 
	 * @return WsIndiDescsData[]
	 */
	public WsIndiDescsData[] getIndiDescsList()
	{
		// log entry to IndiDescs
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRTSTblName("IndiDescs");
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

		Vector lvIndiDescsList = new Vector();
		WsIndiDescsData[] larrIndiDescsList = new WsIndiDescsData[0];

		try
		{
			lvIndiDescsList =
				IndicatorDescriptionsCache.getIndiDescs(GET_ALL);

			Object[] larrTemp = lvIndiDescsList.toArray();
			larrIndiDescsList = new WsIndiDescsData[larrTemp.length];

			// loop through and create the new passable objects
			for (int i = 0; i < larrTemp.length; i++)
			{
				IndicatorDescriptionsData laOldData =
					(IndicatorDescriptionsData) larrTemp[i];
				WsIndiDescsData laNewData = new WsIndiDescsData();
				laNewData.setIndiName(laOldData.getIndiName());
				laNewData.setIndiFieldValue(
					laOldData.getIndiFieldValue());
				laNewData.setIndiDesc(laOldData.getIndiDesc());
				laNewData.setIndiScrnPriority(
					laOldData.getIndiScrnPriority());
				larrIndiDescsList[i] = laNewData;
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
		}
		catch (RTSException aeEx)
		{
			aeEx.printStackTrace();
		}

		return larrIndiDescsList;
	}

	/**
	 * Returns WsIndiDescsRes Object which contains: 
	 *   - array of all IndiDescs
	 *   - V21UniqueId 
	 *   - V21ReqId 
	 * 
	 * @param aiV21UniqueId 
	 * @return WsIndiDescsRes
	 */
	public WsIndiDescsRes getIndiDescsListVerA(int aiV21UniqueId)
	{
		// log entry to IndiDescs
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRTSTblName("IndiDescs");
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

		Vector lvIndiDescsList = new Vector();
		WsIndiDescsData[] larrIndiDescsList = new WsIndiDescsData[0];

		try
		{
			lvIndiDescsList =
				IndicatorDescriptionsCache.getIndiDescs(GET_ALL);

			Object[] larrTemp = lvIndiDescsList.toArray();
			larrIndiDescsList = new WsIndiDescsData[larrTemp.length];

			// loop through and create the new passable objects
			for (int i = 0; i < larrTemp.length; i++)
			{
				IndicatorDescriptionsData laOldData =
					(IndicatorDescriptionsData) larrTemp[i];
				WsIndiDescsData laNewData = new WsIndiDescsData();
				laNewData.setIndiName(laOldData.getIndiName());
				laNewData.setIndiFieldValue(
					laOldData.getIndiFieldValue());
				laNewData.setIndiDesc(laOldData.getIndiDesc());
				laNewData.setIndiScrnPriority(
					laOldData.getIndiScrnPriority());
				larrIndiDescsList[i] = laNewData;
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
		}
		catch (RTSException aeEx)
		{
			aeEx.printStackTrace();
		}

		WsIndiDescsRes laWsIndiDescsRes = new WsIndiDescsRes();
		laWsIndiDescsRes.setIndiDescs(larrIndiDescsList);
		laWsIndiDescsRes.setV21UniqueId(aiV21UniqueId);
		laWsIndiDescsRes.setV21ReqId(
			laRequestTrackingData.getV21ReqId());

		return laWsIndiDescsRes;
	}
}
