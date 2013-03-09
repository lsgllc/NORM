package com.txdot.isd.rts.webservices.ren;import com.txdot.isd.rts.webservices.common.RtsServicePerformanceTracking;import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;import com.txdot.isd.rts.webservices.ren.data.RtsRenewalRcptResponse;import com.txdot.isd.rts.webservices.ren.data.RtsRenewalRequest;import com.txdot.isd.rts.webservices.ren.util.RtsRenHelper;import com.txdot.isd.rts.services.cache.ErrorMessagesCache;import com.txdot.isd.rts.services.data.WebServiceHistoryData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.Log;import com.txdot.isd.rts.services.util.constants.ErrorsConstant;import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;/* * RtsRenRcptService.java * * (c) Texas Department of Motor Vehicles 2010 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	12/10/2010	Initial load. * 							defect 10670 Ver 670 * Ray Rowehl	12/30/2010	Remove the PCL code for now. * 							defect 10670 Ver 670 * Ray Rowehl	02/07/2011	Reflow to just sent the receipt url now. * 							defect 10670 Ver 670 * Ray Rowehl	03/02/2011	Change over to use "RtsRenRcptService" * 							in the sav lookup. * 							defect 10670 Ver 670 * Min Wang		03/11/2011	Make max message length 100. * 							modify processData() * 							defect 10670 Ver 6.7.1 * Min Wang		03/21/2011	Use right constant for error msg. * 							modify processData() * 							defect 10670 Ver 6.7.1 * K Harrell	04/12/2011	modify processData() * 							defect 10769 Ver 6.7.1    * K Harrell	04/24/2011	Implement new RtsRenHelper constructor.  * 							modify processRcptReq() * 							defect 10768 Ver 6.7.1   * K McKee      10/06/2011  Added loggoing for Error 2300 *                          defect 10729 Ver 6.9.0 * --------------------------------------------------------------------- *//** * The web service to finalize the renewal request and format a  * receipt. * * @version	6.7.1			04/24/2011 * @author	Ray Rowehl * @author  Kathy Harrell  * <br>Creation Date:		12/10/2010 15:43:27 *//* &RtsRenRcptService& */public class RtsRenRcptService{	/**	 * Process request to Accept Vehicle and Generate Receipt	 * 	 * @param  aarrRequest RtsRenewalRequest[]	 * @return RtsRenewalRcptResponse[]	 *//* &RtsRenRcptService.processData$2& */	public RtsRenewalRcptResponse[] processData(RtsRenewalRequest[] aarrRequest)	{		RtsRenewalRcptResponse[] larrResponse =			new RtsRenewalRcptResponse[aarrRequest.length];		DatabaseAccess laDBA = null;		for (int i = 0; i < aarrRequest.length; i++)		{			RtsServicePerformanceTracking laSPTTemp =				new RtsServicePerformanceTracking(laDBA);			int liSAVId =				laSPTTemp.lookupSAVId(					"RtsRenRcptService",					aarrRequest[i].getAction(),					aarrRequest[i].getVersionNo());			WebServiceHistoryData laRtsSrvcHstryData =				new WebServiceHistoryData(					liSAVId,					aarrRequest[i].getCaller(),					aarrRequest[i].getSessionId());			RtsRenewalRcptResponse laNewResponse =				new RtsRenewalRcptResponse();			try			{				laDBA = new DatabaseAccess();				// UOW #1 BEGIN				laDBA.beginTransaction();				RtsServicePerformanceTracking laSPT =					new RtsServicePerformanceTracking(laDBA);				laSPT.logInsert(laRtsSrvcHstryData);				laDBA.endTransaction(DatabaseAccess.COMMIT);				// UOW #1 END 				switch (aarrRequest[i].getAction())				{					case WebServicesActionsConstants						.RTS_REN_PLATE_POST :						{							// intentionally dropping through						}					case WebServicesActionsConstants.RTS_REN_DOCNO :						{							// UOW #2 BEGIN							laDBA.beginTransaction();							processRcptReq(aarrRequest[i], laDBA);							break;						}					default :						{						Log.write(Log.APPLICATION, this, "processData -  ******  ERROR - 2300 ****  Invalid Action Type");							throw new RTSException(								ErrorsConstant									.ERR_NUM_WEBAGNT_GENERAL_ERROR);						}				}				laNewResponse.setErrMsgNo(0);			}			catch (RTSException aeRTSEx)			{				laNewResponse.setErrMsgNo(					aeRTSEx.getCode() == 0						? ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR						: aeRTSEx.getCode());			}			catch (Exception aeEx)			{				Log.write(Log.APPLICATION, this, "processData - Exception   ******  ERROR - 2300 ****  "  + aeEx.toString());				laNewResponse.setErrMsgNo(					ErrorsConstant.ERR_NUM_WEBAGNT_GENERAL_ERROR);			}			if (laNewResponse.getErrMsgNo() != 0)			{				laNewResponse.setErrMsgDesc(					ErrorMessagesCache.getErrMsgDesc(						laNewResponse.getErrMsgNo()));			}			larrResponse[i] = laNewResponse;			laRtsSrvcHstryData.setSuccessfulIndi(				laNewResponse.getErrMsgNo() == 0);			laRtsSrvcHstryData.setErrMsgNo(laNewResponse.getErrMsgNo());			try			{				if (laDBA != null)				{					if (!laDBA.isConnected())					{						laDBA.beginTransaction();					}					else					{						if (laNewResponse.getErrMsgNo() != 0)						{							laDBA.endTransaction(								DatabaseAccess.ROLLBACK);							// UOW #2 END 							// UOW #3 BEGIN  							laDBA.beginTransaction();						}					}					// Update request status in RTS_SRVC_HSTRY 					RtsServicePerformanceTracking laSPT =						new RtsServicePerformanceTracking(laDBA);					laSPT.logUpdate(laRtsSrvcHstryData);					laDBA.endTransaction(DatabaseAccess.COMMIT);					// UOW #2 (OR #3)  END  				}			}			catch (RTSException aeRTSEx)			{				Log.write(Log.APPLICATION, this, "processData -  had a problem closing the dba "); 			}		}		return larrResponse;	}	/**	 * Process Renewal (Receipt) Request 	 *  - Generate Receipt 	 *  - Update RTS_WEB_AGNCY_TRANS 	 * 	 * @param aaRequest   	RtsRenewalRequest	 * @param aaDBA			DatabaseAccess	 * @throws RTSException 	 *//* &RtsRenRcptService.processRcptReq& */	private void processRcptReq(		RtsRenewalRequest aaRequest,		DatabaseAccess aaDBA)		throws RTSException	{		RtsRenHelper laRtsRenHelper = new RtsRenHelper(aaRequest, aaDBA);		laRtsRenHelper.processGetWebAgncyTransReceipt();	}}/* #RtsRenRcptService# */