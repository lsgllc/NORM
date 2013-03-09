package com.txdot.isd.rts.webservices.adm;

import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.OfficeIds;
import com.txdot.isd.rts.server.db.PlateType;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.data.PlateTypeData;
import com.txdot.isd.rts.services.data.WebServiceHistoryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.webservices.adm.data.RtsOfficeIdResponse;
import com.txdot.isd.rts.webservices.adm.data.RtsOfficeIdsData;
import com.txdot.isd.rts.webservices.adm.data.RtsPlateTypeData;
import com.txdot.isd.rts.webservices.adm.data.RtsPlateTypeResponse;
import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;
import com.txdot.isd.rts.webservices.common.data.RtsDefaultRequest;
import com.txdot.isd.rts.webservices.common.data.RtsTrackingData;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;

/*
 * RtsAdmService.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/28/2008	Created class.
 * 							defect 9677 Ver MyPlates_POS
 * Mark Reyes	06/25/2008  Added getPltTypes()
 * 							defect 9677 Ver MyPlates_POS
 * Ray Rowehl	01/26/2009	Add DBA connection check before writing 
 * 							result.
 * 							Refactored / Renamed laDBAD to laDBA in 
 * 							getPltTypes.
 * 							modify getCountyInfo(), getPltTypes
 * 							defect 9804 Ver Defect_POS_D
 * Ray Rowehl	10/05/2010	Remove office 257 specific code.
 * 							Remove unused variables.
 * 							clean up method JavaDocs.
 * 							organize imports.
 * 							modify getCountyInfo(), 
 * 								RtsPlateTypeResponse()
 * 							defect 10618 Ver POS 660
 * R Pilon		02/02/2012	Change call to method setCarrPlateTypeData() to 
 * 							  setPlateTypeData() and setCarrOfficeData() to 
 * 							  setOfficeData().
 * 							modify getCountyInfo(), getPltTypes()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * This is the RTS Web Service for returning Admin Table Data.
 * 
 * <p>Note that each method has to be called individually because
 * they are returning their own form of data.
 * TODO.  Review for future setup once we migrate to RAD!
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		05/28/2008 11:13:56
 */
public class RtsAdmService
{
	/**
	 * Gets the Office Ids data appropriate to the request, formats it,
	 * and returns the data.
	 * 
	 * @param  RtsDefaultRequest
	 * @return RtsOfficeIdResponse
	 */
	public RtsOfficeIdResponse[] getCountyInfo(RtsDefaultRequest[] aarrRequest)
	{
		RtsOfficeIdResponse[] larrResponse = null;
		if (aarrRequest[0].getAction()
			== WebServicesActionsConstants.RTS_ADM_COUNTY_INFO)
		{
			DatabaseAccess laDBA = null;
			RtsServicePerformanceTracking laSPTTemp =
			new RtsServicePerformanceTracking(laDBA);
			int liSAVId =
			laSPTTemp.lookupSAVId(
					"RtsAdmService",
					aarrRequest[0].getAction(),
					aarrRequest[0].getVersionNo());

			WebServiceHistoryData laRtsSrvcHstryData =
				new WebServiceHistoryData(
					liSAVId,
					aarrRequest[0].getCaller(),
					aarrRequest[0].getSessionId());

			larrResponse = new RtsOfficeIdResponse[1];
			RtsOfficeIdsData[] larrCountyInfo = null;
			

			try
			{
				laDBA = new DatabaseAccess();
				// defect 10618
				// Vector lvOfficeIdsData = null;
				// end defect 10618
				laDBA.beginTransaction();

				// log the request
				RtsServicePerformanceTracking laSPT =
					new RtsServicePerformanceTracking(laDBA);
				laSPT.logInsert(laRtsSrvcHstryData);

				OfficeIds laOISql = new OfficeIds(laDBA);

				// query to get all the counties
				Vector lvCountyInfoList = laOISql.qryOfficeIds(3);

				// setup the response row
				RtsOfficeIdResponse laResponseRow =
					new RtsOfficeIdResponse();
				larrResponse[0] = laResponseRow;

				// loop through the vector and convert it to an 
				// array of RtsOfficeIdsData
				// defect 10618
				// array size should equal vector size.
				larrCountyInfo =
					new RtsOfficeIdsData[lvCountyInfoList.size()];
				// end defect 10618
				for (int i = 0; i < lvCountyInfoList.size(); i++)
				{
					OfficeIdsData laOID =
						(OfficeIdsData) lvCountyInfoList.elementAt(i);

					// defect 10618
					// No longer need to bypass POS Test office
					if (laOID != null)
					{
						//	&& laOID.getOfcIssuanceNo() != 257)
						//{
						// end defect 10618
						RtsOfficeIdsData laRtsOID =
							new RtsOfficeIdsData(laOID);
						larrCountyInfo[i] = laRtsOID;
					}
				}

				larrResponse[0].setErrMsgNo(0);
				larrResponse[0].setErrMsgDesc("");
				// defect 11135
				larrResponse[0].setOfficeData(larrCountyInfo);
				// end defect 11135
				laRtsSrvcHstryData.setSuccessfulIndi(true);
				laRtsSrvcHstryData.setErrMsgNo(0);
			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.DEBUG,
					this,
					"Got an RTSException getting CountyInfo");
				if (aeRTSEx.getCode() != 0)
				{
					larrResponse[0].setErrMsgNo(aeRTSEx.getCode());
					laRtsSrvcHstryData.setErrMsgNo(aeRTSEx.getCode());
				}
				if (aeRTSEx.getDetailMsg() != null)
				{
					larrResponse[0].setErrMsgDesc(
						aeRTSEx.getDetailMsg());
				}
				// defect 11135
				larrResponse[0].setOfficeData(null);
				// end defect 11135
			}

			try
			{
				// defect 9804
				if (!laDBA.isConnected())
				{
					laDBA.beginTransaction();
				}
				// end 9804
				
				// write out the update to the tracking row.
				RtsServicePerformanceTracking laSPT =
					new RtsServicePerformanceTracking(laDBA);
				laSPT.logUpdate(laRtsSrvcHstryData);

				if (laDBA != null)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
				}
			}
			catch (RTSException aeRTSEx)
			{
				System.out.println(
					"RtsAdmService had a problem " + "closing the dba");
			}
		}

		return larrResponse;
	}

	/**
	 * Gets the Plate Type data and returns the data.
	 * 
	 * @param  RtsDefaultRequest
	 * @return RtsPlateTypeResponse
	 */
	public RtsPlateTypeResponse[] getPltTypes(RtsDefaultRequest[] aarrRequest)
	{
		RtsPlateTypeResponse[] larrResponse = null;
		if (aarrRequest[0].getAction()
			== WebServicesActionsConstants.RTS_ADM_PLATE_TYPE)
		{
			// defect 9804
			DatabaseAccess laDBA = null;
			RtsServicePerformanceTracking laRSPTTemp = new RtsServicePerformanceTracking(laDBA);
			// end defect 9804
			int liSAVId =
			laRSPTTemp.lookupSAVId(
					"RtsAdmService",
					aarrRequest[0].getAction(),
					aarrRequest[0].getVersionNo());
			RtsTrackingData laRtsTrackingData =
				new RtsTrackingData(
					liSAVId,
					aarrRequest[0].getCaller(),
					aarrRequest[0].getSessionId());
			larrResponse = new RtsPlateTypeResponse[1];
			RtsPlateTypeData[] larrPlateType = null;
			

			try
			{
				// defect 9804
				laDBA = new DatabaseAccess();
				// end defect 9804
				// defect 10618
				// Vector lvPlateTypeData = null;
				// end defect 10618
				// defect 9804
				laDBA.beginTransaction();

				// log the request
				RtsServicePerformanceTracking laSPTD =
					new RtsServicePerformanceTracking(laDBA);
				// end defect 9804
				laSPTD.logInsert(laRtsTrackingData);

				// defect 9804
				PlateType laPTSql = new PlateType(laDBA);
				// end defect 9804

				//query to get plate type information
				Vector lvPlateTypeList = laPTSql.qryPlateType();
				
				// setup the response row
				RtsPlateTypeResponse laResponseRowPl =
						new RtsPlateTypeResponse();
				larrResponse[0] = laResponseRowPl;
				// loop through the vector and convert it to an
				// array of RtsPlateTypeData
				larrPlateType = new 
				RtsPlateTypeData[lvPlateTypeList.size()];
				for(int i = 0; i < lvPlateTypeList.size(); i++)
				{
					PlateTypeData laPTD = (PlateTypeData)
					lvPlateTypeList.elementAt(i);
					if(laPTD != null)
					{
					
					RtsPlateTypeData laRtsPTD = 
							new RtsPlateTypeData(laPTD);
					larrPlateType[i] = laRtsPTD;
					}
					
				}
				larrResponse[0].setErrMsgNo(0);
				larrResponse[0].setErrMsgDesc("");
				// defect 11135
				larrResponse[0].setPlateTypeData(larrPlateType);
				// end defect 11135
				laRtsTrackingData.setSuccessful(true);
				laRtsTrackingData.setErrMsgNo(0);

			}
			catch (RTSException aeRTSEx)
			{
				Log.write(
					Log.DEBUG,
					this,
					"Got an RTSException getting CountyInfo");
				if (aeRTSEx.getCode() != 0)
				{
					larrResponse[0].setErrMsgNo(aeRTSEx.getCode());
					laRtsTrackingData.setErrMsgNo(aeRTSEx.getCode());
				}
				if (aeRTSEx.getDetailMsg() != null)
				{
					larrResponse[0].setErrMsgDesc(
						aeRTSEx.getDetailMsg());
				}
				// defect 11135
				larrResponse[0].setPlateTypeData(null);
				// end defect 11135
			}
			try
			{
				// defect 9804
				if (!laDBA.isConnected())
				{
					laDBA.beginTransaction();
				}
				
				// write out the update to the tracking row.
				RtsServicePerformanceTracking laSPTD = 
					new RtsServicePerformanceTracking(laDBA);
				// end defect 9804
				
				laSPTD.logUpdate(laRtsTrackingData);
				
				// defect 9804
				if (laDBA != null)
				{
					laDBA.endTransaction(DatabaseAccess.COMMIT);
					// end defect 9804
				}
			}
			catch(RTSException aeRTSEx)
			{
				System.out.println("RtsAdmService had a problem" +
				 "closing the dba");
			}
		}

		return larrResponse;
	}
}
