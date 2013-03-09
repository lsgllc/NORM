package com.txdot.isd.rts.webservices.agnt.data;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;
import com
	.txdot
	.isd
	.rts
	.webservices
	.common
	.data
	.WebServicesActionsConstants;

/*
 * RtsWebAgntRequest.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/20/2011	Initial load.
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	02/02/2011	Change to use RtsWebAgntWs
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	05/25/2011	Add validation routine.
 * 							add validateAgntRequest()
 * 							defect 10718 Ver 6.8.0
 * ---------------------------------------------------------------------
 */

/**
 * Request data for Agent web service.
 *
 * @version	6.8.0			05/25/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/20/2011  7:27:46
 */

public class RtsWebAgntRequest extends RtsAbstractRequest
{
	private RtsWebAgntWS[] carrAgnt;
	private int ciProfileCount;

	/**
	 * Get the Agent array.
	 * 
	 * @return RtsWebAgntWS[]
	 */
	public RtsWebAgntWS[] getAgnt()
	{
		return carrAgnt;
	}

	/**
	 * Get an element of Agent array.
	 * 
	 * @param i element of the carrAgnt array to get
	 * @return RtsWebAgntWS
	 */
	public RtsWebAgntWS getAgnt(int i)
	{
		return carrAgnt[i];
	}

	/**
	 * Get the Profile Count.
	 * 
	 * <p>This count gives an indication of how many Agencies 
	 * the Agent is setup for.
	 * If deleting a profile, if there is only one, the Agent also gets 
	 * deleted.
	 * 
	 * @return int
	 */
	public int getProfileCount()
	{
		return ciProfileCount;
	}

	/**
	 * Set the Agent Data.
	 * 
	 * @param aarrAgnt
	 */
	public void setAgnt(RtsWebAgntWS[] aarrAgnt)
	{
		carrAgnt = aarrAgnt;
	}

	/**
	 * Set an element of the Agent array.
	 * 
	 * @param i element of the aarrAgnt array to set
	 * @param aaAgnt the element to set in the aarrAgnt array
	 */
	public void setAgnt(int i, RtsWebAgntWS aaAgnt)
	{
		carrAgnt[i] = aaAgnt;
	}

	/**
	 * Set the Profile Count.
	 * 
	 * @param aiProfileCount
	 */
	public void setProfileCount(int aiProfileCount)
	{
		ciProfileCount = aiProfileCount;
	}

	/**
	 * Make sure the request meets at least basic edit checks.
	 * 
	 * @throws RTSException
	 */
	public void validateAgntRequest() throws RTSException
	{
		// these methods will throw exceptions if there are problems.
		validateAbstractRequest();

		// there has to be agent data to process an insert, delete, 
		// or update.
		if (getAgnt() == null
			&& (getAction()
				== WebServicesActionsConstants.RTS_AGENT_INSERT_UPDATE
				|| getAction()
					== WebServicesActionsConstants.RTS_AGENT_DELETE))
		{
			throw new RTSException(150);
		}

		// check for any problems with the input data.
		if (getAgnt() != null
			&& getAgnt().length > 0
			&& getAction()
				== WebServicesActionsConstants.RTS_AGENT_INSERT_UPDATE)
		{
			for (int i = 0; i < getAgnt().length; i++)
			{
				getAgnt()[i].validateWebAgntWS();
			}
		}
	}

}
