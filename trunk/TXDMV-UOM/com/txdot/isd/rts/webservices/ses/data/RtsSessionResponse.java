package com.txdot.isd.rts.webservices.ses.data;

import java.util.Calendar;

import com.txdot.isd.rts.webservices.agncy.data.RtsWebAgncy;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntWS;
import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RtsSessionResponse.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/27/2010	Initial load.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	01/03/2011	Update with lastest expected data.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	01/25/2011	Update with lastest expected data.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	01/31/2011	Organize RtsWebAgent into a combined object.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	03/30/2011	Remove Batch List data.
 * 							delete carrRtsBatchListSummaryLines
 * 							delete getRtsBatchListSummaryLines(), 
 * 								setRtsBatchListSummaryLines()
 * 							defect 10670 Ver 6.7.1
 * ---------------------------------------------------------------------
 */

/**
 * The web service to do Login for Web Renewal Response.
 *
 * @version	6.7.1			03/30/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		12/27/2010 11:05:46
 */

public class RtsSessionResponse extends RtsAbstractResponse
{
	private Calendar caLastLoginSuccessful;
	private Calendar caLastLoginUnsuccessful;
	//private RtsBatchListSummaryLine[] carrRtsBatchListSummaryLines;
	private RtsWebAgncy[] carrRtsWebAgncy;
	private RtsWebAgntWS[] carrRtsWebAgntWs;

	/**
	 * Get the Date of the last successful login.
	 * 
	 * @return Calendar
	 */
	public Calendar getLastLoginSuccessful()
	{
		return caLastLoginSuccessful;
	}

	/**
	 * Get the Date of the last unsuccessful login.
	 * 
	 * @return Calendar 
	 */
	public Calendar getLastLoginUnsuccessful()
	{
		return caLastLoginUnsuccessful;
	}

//	/**
//	 * Get the Batch Statuses.
//	 * 
//	 * @return RtsBatchListSummaryLine[]
//	 */
//	public RtsBatchListSummaryLine[] getRtsBatchListSummaryLines()
//	{
//		return carrRtsBatchListSummaryLines;
//	}

	/**
	 * Get the Agency Data associated with this login.
	 * 
	 * @return RtsWebAgncy[]
	 */
	public RtsWebAgncy[] getRtsWebAgncy()
	{
		return carrRtsWebAgncy;
	}
	
	/**
	 * Get the Agent Data.
	 * 
	 * @return RtsWebAgntWs
	 */
	public RtsWebAgntWS[] getRtsWebAgntWs()
	{
		return carrRtsWebAgntWs;
	}

	/**
	 * Set the Date for last successful login.
	 * 
	 * @param aaLastLoginSuccessful
	 */
	public void setLastLoginSuccessful(Calendar aaLastLoginSuccessful)
	{
		caLastLoginSuccessful = aaLastLoginSuccessful;
	}

	/**
	 * Set the Date for last unsuccessful login.
	 * 
	 * @param aaLastLoginUnsuccessful
	 */
	public void setLastLoginUnsuccessful(Calendar aaLastLoginUnsuccessful)
	{
		caLastLoginUnsuccessful = aaLastLoginUnsuccessful;
	}

//	/**
//	 * Set the Batch Statuses.
//	 * 
//	 * @param aarrRtsBatchListSummaryLines
//	 */
//	public void setRtsBatchListSummaryLines(RtsBatchListSummaryLine[] aarrRtsBatchListSummaryLines)
//	{
//		carrRtsBatchListSummaryLines = aarrRtsBatchListSummaryLines;
//	}

	/**
	 * Set the Agency Data associated with this login.
	 * 
	 * @param aarrRtsWebAgncy
	 */
	public void setRtsWebAgncy(RtsWebAgncy[] aarrRtsWebAgncy)
	{
		carrRtsWebAgncy = aarrRtsWebAgncy;
	}

	/**
	 * Set the Agent Data.
	 * 
	 * @param aaRtsWebAgntWs
	 */
	public void setRtsWebAgntWs(RtsWebAgntWS[] aarrRtsWebAgntWs)
	{
		carrRtsWebAgntWs = aarrRtsWebAgntWs;
	}

}
