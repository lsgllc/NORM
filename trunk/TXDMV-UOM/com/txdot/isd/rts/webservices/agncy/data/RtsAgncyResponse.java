package com.txdot.isd.rts.webservices.agncy.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RtsAgncyResponse.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/05/2011	Initial load.
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	01/12/2011	Modify to use new Data Classes
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	01/24/2011	Restructure to use a combined object.
 * 							defect 10718 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Response Data output from RtsAgencyService.
 *
 * @version	6.7.0			01/24/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/05/2011 09:53:31
 */

public class RtsAgncyResponse extends RtsAbstractResponse
{
	private RtsWebAgncy[] carrRtsWebAgncyOut;

	/**
	 * Get the Web Agency data object.
	 * 
	 * @return RtsWebAgncy[]
	 */
	public RtsWebAgncy[] getRtsWebAgncyOut()
	{
		return carrRtsWebAgncyOut;
	}

	/**
	 * Set the Web Agency data object.
	 * 
	 * @param aarrRtsWebAgncyOut
	 */
	public void setRtsWebAgncyOut(RtsWebAgncy[] aarrRtsWebAgncyOut)
	{
		carrRtsWebAgncyOut = aarrRtsWebAgncyOut;
	}

}
