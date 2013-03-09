package com.txdot.isd.rts.webservices.agnt.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RtsWebAgntResponse.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/20/2011	Initial load.
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	02/02/1011	Change to use RtsWebAgntWS
 * 							defect 10718 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Response data for Agent web service.
 *
 * @version	6.7.0			02/02/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/20/2011  7:38:11
 */

public class RtsWebAgntResponse extends RtsAbstractResponse
{
	private RtsWebAgntWS[] carrRtsWebAgnt;
	
	/**
	 * Get the Rts Web Agent combined object.
	 * 
	 * @return RtsWebAgntWS[]
	 */
	public RtsWebAgntWS[] getRtsWebAgnt()
	{
		return carrRtsWebAgnt;
	}

	/**
	 * Set the Rts Web Agent combined object.
	 * 
	 * @param aarrRtsWebAgnt
	 */
	public void setRtsWebAgnt(RtsWebAgntWS[] aarrRtsWebAgnt)
	{
		carrRtsWebAgnt = aarrRtsWebAgnt;
	}

}
