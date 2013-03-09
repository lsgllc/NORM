package com.txdot.isd.rts.server.v21.request.tracking;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.V21Constant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;

/*
 * V21RequestTracking.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/04/2008	Created
 * 							defect 9546 Ver 3 Amigos PH A
 * ---------------------------------------------------------------------
 */

/**
 * This class provides access to methods used to track the V21 Requests
 * and responses. 
 *
 * @version	3 Amigos PH A	02/04/2008
 * @author	Kathy Harrell 
 * <br>Creation Date:		02/04/2007  12:50:00 
 */
public class V21RequestTracking
{
	/**
	 * V21RequestTracking constructor
	 */
	public V21RequestTracking()
	{
		super();
	}

	/**
	 * Used to call the private methods of the V21RequestTracking server business.
	 *
	 * @param aiModule int 
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException 
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			case V21Constant.V21_GET_VEH_REQ :
				{
					return insV21GetVehReq(aaData);
				}
			case V21Constant.V21_GET_ADMIN_TBL_REQ :
				{
					return insV21GetAdminTblReq(aaData);
				}
			case V21Constant.V21_VEH_SOLD_REQ :
				{
					return insV21VehSoldReq(aaData);
				}
			case V21Constant.V21_PLT_DISP_REQ :
				{
					return insV21PltDispReq(aaData);
				}
			case V21Constant.V21_COMPL_REQ :
				{
					return complV21Req(aaData);
				}
		}
		return null;
	}

	/**
	 * Insert into RTS_V21_REQUEST & RTS_V21_GET_VEH_REQ
	 *
	 * @param Object
	 * @throws RTSException 
	 */
	private Object insV21GetVehReq(Object aaData) throws RTSException
	{
		V21RequestData aaV21ReqData = (V21RequestData) aaData;

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// Insert into Parent V21_REQUEST 
			aaV21ReqData.setV21ReqTypeCd(V21Constant.V21_GET_VEH);
			V21Request laV21Req = new V21Request(laDBAccess);
			laDBAccess.beginTransaction();
			aaV21ReqData = laV21Req.insV21Request(aaV21ReqData);

			// Insert into Request Table V21_GET_VEH_REQ 
			V21GetVehicleRequest laV21GetVehReq =
				new V21GetVehicleRequest(laDBAccess);

			laV21GetVehReq.insV21GetVehicleRequest(aaV21ReqData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return aaV21ReqData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Insert into RTS_V21_REQUEST & RTS_V21_GET_ADMIN_TBL_REQ 
	 *
	 * @param Object
	 * @throws RTSException 
	 */
	private Object insV21GetAdminTblReq(Object aaData)
		throws RTSException
	{

		V21RequestData laV21ReqData = (V21RequestData) aaData;
		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// Insert into Parent V21_REQUEST  
			laV21ReqData.setV21ReqTypeCd(V21Constant.V21_GET_ADMIN_TBL);
			V21Request laV21Req = new V21Request(laDBAccess);
			laDBAccess.beginTransaction();
			laV21ReqData = laV21Req.insV21Request(laV21ReqData);

			// Insert into V21_GET_ADMIN_TBL_REQ 
			V21GetAdminTableRequest laV21GetAdmTblReq =
				new V21GetAdminTableRequest(laDBAccess);
			laV21GetAdmTblReq.insV21GetAdminTableRequest(laV21ReqData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return laV21ReqData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Insert into RTS_V21_REQUEST & RTS_V21_VEH_SOLD_REQ 
	 *
	 * @param Object
	 * @throws RTSException 
	 */
	private Object insV21VehSoldReq(Object aaData) throws RTSException
	{
		V21RequestData aaV21ReqData = (V21RequestData) aaData;

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// Insert into Parent V21_REQUEST  
			aaV21ReqData.setV21ReqTypeCd(V21Constant.V21_VEH_SOLD);
			V21Request laV21Req = new V21Request(laDBAccess);
			laDBAccess.beginTransaction();
			aaV21ReqData = laV21Req.insV21Request(aaV21ReqData);

			// Insert into RTS_V21_VEH_SOLD_REQ
			V21VehicleSoldRequest laV21VehSoldReq =
				new V21VehicleSoldRequest(laDBAccess);
			laV21VehSoldReq.insV21VehicleSoldRequest(aaV21ReqData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return aaV21ReqData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Insert into RTS_V21_REQUEST & RTS_V21_PLT_DISP_REQ 
	 *
	 * @param Object
	 * @throws RTSException 
	 */
	private Object insV21PltDispReq(Object aaData) throws RTSException
	{
		V21RequestData laV21ReqData = (V21RequestData) aaData;

		DatabaseAccess laDBAccess = new DatabaseAccess();
		try
		{
			// Insert into Parent V21_REQUEST  
			laV21ReqData.setV21ReqTypeCd(V21Constant.V21_PLT_DISP);
			V21Request laV21Req = new V21Request(laDBAccess);
			laDBAccess.beginTransaction();
			laV21ReqData = laV21Req.insV21Request(laV21ReqData);

			// Insert into RTS_V21_PLT_DISP_REQ
			V21PlateDispositionRequest laV21PltDispReq =
				new V21PlateDispositionRequest(laDBAccess);
			laV21PltDispReq.insV21PlateDispositionRequest(laV21ReqData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return laV21ReqData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Update the V21_REQUEST, V21REQID_RTSTRANSID 
	 *
	 * @param Object
	 * @throws RTSException 
	 */
	private Object complV21Req(Object aaData) throws RTSException
	{

		V21RequestData laV21ReqData = (V21RequestData) aaData;
		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			// Update Request as complete   
			V21Request laV21Req = new V21Request(laDBAccess);
			laDBAccess.beginTransaction();
			laV21Req.updV21Request(laV21ReqData);

			// Insert record of RTS Transaction If Successful  
			if (laV21ReqData.isSuccessful()
				&& laV21ReqData.isTransRequest())
			{
				V21RequestIdToRTSTransId laV21ToRTS =
					new V21RequestIdToRTSTransId(laDBAccess);
				laV21ToRTS.insV21RequestIdToRTSTransId(laV21ReqData);
			}
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return laV21ReqData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}
}
