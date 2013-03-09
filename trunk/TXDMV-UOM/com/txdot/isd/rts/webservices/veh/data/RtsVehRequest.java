package com.txdot.isd.rts.webservices.veh.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * RtsVehRequest.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/29/2010	Created class.
 * 							defect 10402 Ver 6.4.0
 * ---------------------------------------------------------------------
 */

/**
 * Request data for Vehicle Service.
 *
 * @version	Ver 6.4.0			04/29/2010
 * @author	William Hargrove
 * <br>Creation Date:			03/08/2010 13:48:00
 */
public class RtsVehRequest extends RtsAbstractRequest
{
	
	private String csPlateNo;

	/**
	 * Return the value of Plate Number
	 * 
	 * @return String
	 */
	public String getPlateNo()
	{
		return csPlateNo;
	}

	/**
	 * Set the value of Plate Number
	 * 
	 * @param asPlateNo String
	 */
	public void setPlateNo(String asPlateNo)
	{
		csPlateNo = asPlateNo;
	}

	/**
	 * Make sure the meets at least basic edit checks.
	 * 
	 * @throws RTSException
	 */
	public void validateVehRequest() throws RTSException
	{
		// make sure there is a CallerId
		if (getCaller() == null || getCaller().length() < 1)
		{
			throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}

		// Make sure there is a SessionId
		if (getSessionId() == null || getSessionId().length() < 1)
		{
			throw new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		}

		// Edit for Plate length <= 7
		if (getPlateNo() == null || getPlateNo().length() > 7)
		{
			throw new RTSException(ErrorsConstant.ERR_NUM_PLATENO_TOO_LONG);
		}
	}

}
