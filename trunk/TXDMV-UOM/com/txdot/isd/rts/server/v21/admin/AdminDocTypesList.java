package com.txdot.isd.rts.server.v21.admin;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.DocumentTypesCache;
import com.txdot.isd.rts.services.data.DocumentTypesData;
import com.txdot.isd.rts.services.data.V21RequestData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.V21Constant;

import com.txdot.isd.rts.server.v21.admin.data.WsDocTypesData;
import com.txdot.isd.rts.server.v21.admin.data.WsDocTypesRes;
import com.txdot.isd.rts.server.v21.request.tracking.V21RequestTracking;

/*
 * AdminDocTypesList.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	01/10/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/13/2008	Add Request Tracking.
 * 							modify getDocTypesList()
 * 							defect 9502 Ver 3_Amigos_PH_A 
 * K Harrell	07/28/2010	add getDocTypesListVerA()
 * 							defect 10482 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */

/**
 * Return the list of all DocumentTypes to the requestor.
 *
 * @version	6.5.0			07/28/2010
 * @author	B Hargrove
 * <br>Creation Date:		01/10/2008 14:43
 */
public class AdminDocTypesList
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

		AdminDocTypesList laClass = new AdminDocTypesList();
		WsDocTypesData[] larrDocTypes = laClass.getDocTypesList();

		for (int i = 0; i < larrDocTypes.length; i++)
		{
			WsDocTypesData laObject = (WsDocTypesData) larrDocTypes[i];
			System.out.println(
				laObject.getDocTypeCd()
					+ " "
					+ laObject.getDocTypeCdDesc()
					+ " "
					+ laObject.getRegRecIndi());
		}
	}
	
	/**
	 * Returns an array of all Document Types.
	 * 
	 * @return WsDocTypesData[]
	 */
	public WsDocTypesData[] getDocTypesList()
	{
		// log entry to DocTypes
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setRTSTblName("DocTypes");
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

		Vector lvDocTypesList = DocumentTypesCache.getDocTypesVec();

		Object[] larrTemp = lvDocTypesList.toArray();
		WsDocTypesData[] larrDocTypesList =
			new WsDocTypesData[larrTemp.length];

		// loop through and create the new passable objects
		for (int i = 0; i < larrTemp.length; i++)
		{
			DocumentTypesData laOldData =
				(DocumentTypesData) larrTemp[i];
			WsDocTypesData laNewData = new WsDocTypesData();
			laNewData.setDocTypeCd(laOldData.getDocTypeCd());
			laNewData.setDocTypeCdDesc(laOldData.getDocTypeCdDesc());
			laNewData.setRegRecIndi(laOldData.getRegRecIndi());
			larrDocTypesList[i] = laNewData;
		}

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

		return larrDocTypesList;
	}

	/**
	 * Returns WsDocTypesRes Object which contains: 
	 *   - array of all Document Types
	 *   - V21UniqueId 
	 *   - V21ReqId 
	 * 
	 * @param aiV21UniqueId 
	 * @return WsDocTypesRes
	 */
	public WsDocTypesRes getDocTypesListVerA(int aiV21UniqueId)
	{
		// log entry to DocTypes
		V21RequestData laRequestTrackingData = new V21RequestData();
		laRequestTrackingData.setV21IntrfcLogId(aiV21UniqueId);
		laRequestTrackingData.setRTSTblName("DocTypes");
		
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

		Vector lvDocTypesList = DocumentTypesCache.getDocTypesVec();

		Object[] larrTemp = lvDocTypesList.toArray();
		WsDocTypesData[] larrDocTypesList =
			new WsDocTypesData[larrTemp.length];

		// loop through and create the new passable objects
		for (int i = 0; i < larrTemp.length; i++)
		{
			DocumentTypesData laOldData =
				(DocumentTypesData) larrTemp[i];
			WsDocTypesData laNewData = new WsDocTypesData();
			laNewData.setDocTypeCd(laOldData.getDocTypeCd());
			laNewData.setDocTypeCdDesc(laOldData.getDocTypeCdDesc());
			laNewData.setRegRecIndi(laOldData.getRegRecIndi());
			larrDocTypesList[i] = laNewData;
		}

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
		
		WsDocTypesRes laWsDocTypesRes = new WsDocTypesRes();
		laWsDocTypesRes.setDocTypes(larrDocTypesList);
		laWsDocTypesRes.setV21UniqueId(aiV21UniqueId);
		laWsDocTypesRes.setV21ReqId(
			laRequestTrackingData.getV21ReqId());
			
		return laWsDocTypesRes;
	}

}
