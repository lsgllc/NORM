package com.txdot.isd.rts.webservices.common;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.WebServicesServiceHistorySql;
import com
	.txdot
	.isd
	.rts
	.services
	.cache
	.WebServicesServiceActionVersionCache;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.webservices.common.data.RtsTrackingData;

/*
 * RtsServicePerformanceTracking.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/30/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * Ray Rowehl	01/18/2009	Only set the insert date if it was null.
 * 							modify logUpdate()
 * 							defect 9804 Ver Defect_POS_D
 * Ray Rowehl	01/26/2009	Modify the update to call insert if the 
 * 							request date was null.
 * 							modify logUpdate()
 * 							defect 9804 Ver Defect_POS_D
 * Ray Rowehl	03/26/2010	Remove normal system.outs.  We depend on 
 * 							the tables for tracking.
 * 							modify logInsert(), logUpdate()
 * 							defect 10401 Ver 6.4.0
 * Ray Rowehl	04/29/2010	Modify the insert routine to throw back
 * 							any RTSExceptions that occur.  
 * 							Also add the throws clause.
 * 							If we can not insert into a table, there is 
 * 							no reason to continue.
 * 							modify logInsert() (both versions)
 * 							defect 10401 Ver 6.4.0
 * ---------------------------------------------------------------------
 */

/**
 * Handle the interface to logging the performance of the RTS Web
 * Services.
 * 
 * @version	6.4.0			04/29/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		05/30/2008 17:18:55
 */

public class RtsServicePerformanceTracking
{
	private DatabaseAccess caDBA;

	/**
	 * RtsServicePerformanceTracking.java Constructor
	 * 
	 * @param aaDBA
	 */
	public RtsServicePerformanceTracking(DatabaseAccess aaDBA)
	{
		super();
		caDBA = aaDBA;
	}

	/**
	 * Allow old school to to send to tracking table.
	 * 
	 * @param aaRTD
	 * @return RtsTrackingData
	 */
	public RtsTrackingData logInsert(RtsTrackingData aaRTD) throws RTSException
	{
		WebServiceHistoryData laWSHD = new WebServiceHistoryData(aaRTD);
		logInsert(laWSHD);
		aaRTD.setReqTimeStmp(laWSHD.getReqTimeStmp());

		return aaRTD;
	}

	/**
	 * Insert history data in history table.
	 * 
	 * @param aaWsHstryData
	 * @return WebServiceHistoryData
	 */
	public WebServiceHistoryData logInsert(WebServiceHistoryData aaWsHstryData) throws RTSException
	{
		try
		{
			aaWsHstryData.setReqTimeStmp(new RTSDate());

			// defect 10401
			//// write to stdout to provide another confirmation
			//System.out.println("===============================");
			//System.out.println("Pretend Tracking Insert");
			//System.out.println(
			//	"SAVId        " + aaWsHstryData.getSAVId());
			//System.out.println(
			//	"Caller Id    " + aaWsHstryData.getCallerId());
			//System.out.println(
			//	"Session Id   " + aaWsHstryData.getSessionId());
			//if (aaWsHstryData.getSuccessfulIndi() == 1)
			//{
			//	System.out.println("Success      " + "true");
			//}
			//else
			//{
			//	System.out.println("Success      " + "false");
			//}
			//System.out.println(
			//	"ErrMsgNo     " + aaWsHstryData.getErrMsgNo());
			//System.out.println(
			//	"Req TimeStmp " + aaWsHstryData.getReqTimeStmp());
			// end defect 10401

			WebServicesServiceHistorySql laSQL =
				new WebServicesServiceHistorySql(caDBA);
			laSQL.insWebServicesServiceHistory(aaWsHstryData);
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println(
				"WS History Tracking Insert just got an error!");
			aeRTSEx.printStackTrace();
			// defect 10401
			throw aeRTSEx;
			// end defect 10401
		}
		return aaWsHstryData;
	}

	/**
	 * Update the tracking data from a previous insert.
	 * 
	 * @param aaRTD
	 * @return RtsTrackingData
	 */
	public WebServiceHistoryData logUpdate(WebServiceHistoryData aaWsHstryData)
	{
		try
		{
			// defect 9804
			// We only create a new date if it was previously null
			// If it was null, we need to be doing an insert instead!
			if (aaWsHstryData.getReqTimeStmp() == null)
			{
				// end defect 9804
				aaWsHstryData.setReqTimeStmp(new RTSDate());
				// defect 9804
				// do an insert instead of an update.
				logInsert(aaWsHstryData);
			}
			// end defect 9804
			
			aaWsHstryData.setRespTimeStmp(new RTSDate());

			// defect 10401
			//// write to stdout as a backup
			//System.out.println("===============================");
			//System.out.println("Pretend Tracking Update");
			//System.out.println(
			//	"SAVId        " + aaWsHstryData.getSAVId());
			//System.out.println(
			//	"Caller Id    " + aaWsHstryData.getCallerId());
			//System.out.println(
			//	"Session Id   " + aaWsHstryData.getSessionId());
			//if (aaWsHstryData.getSuccessfulIndi() == 1)
			//{
			//	System.out.println("Success      " + "true");
			//}
			//else
			//{
			//	System.out.println("Success      " + "false");
			//}
			//System.out.println(
			//	"ErrMsgNo     " + aaWsHstryData.getErrMsgNo());
			//System.out.println(
			//	"Req TimeStmp " + aaWsHstryData.getReqTimeStmp());
			//System.out.println(
			//	"Rsp TimeStmp " + aaWsHstryData.getRespTimeStmp());
			// end defect 10401

			WebServicesServiceHistorySql laSQL =
				new WebServicesServiceHistorySql(caDBA);
			laSQL.updWebServicesServiceHistory(aaWsHstryData);
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println(
				"WS History Tracking Update just got an error!");
			aeRTSEx.printStackTrace();
		}
		return aaWsHstryData;
	}

	/**
	 * Handle the update for old school.
	 * 
	 * @param aaRTD
	 * @return RtsTrackingData
	 */
	public RtsTrackingData logUpdate(RtsTrackingData aaRTD)
	{
		WebServiceHistoryData laWSHD = new WebServiceHistoryData(aaRTD);

		logUpdate(laWSHD);

		aaRTD.setReqTimeStmp(laWSHD.getReqTimeStmp());
		aaRTD.setRespTimeStmp(laWSHD.getRespTimeStmp());

		return aaRTD;
	}

	/**
	 * Lookup the Service - Action - Version Id Number.
	 * 
	 * @param asService
	 * @param aiAction
	 * @param aiVersion
	 * @return int
	 */
	public int lookupSAVId(
		String asService,
		int aiAction,
		int aiVersion)
	{
		int liSavId = -1;
		WebServicesServiceActionVersionCache laCache =
			new WebServicesServiceActionVersionCache();
		liSavId = laCache.getSavId(asService, aiAction, aiVersion);
		return liSavId;
	}
}
