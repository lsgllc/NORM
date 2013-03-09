package com.txdot.isd.rts.webservices.veh.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;
import com.txdot.isd.rts.webservices.common.data.WebServicesActionsConstants;

import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * RtsVehPublicRequest.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/13/2010	Created
 * 							defect 10684 Ver 6.7.0 
 * R Pilon		02/02/2012	Add missing setter to prevent web service validation
 * 							  error.
 * 							add setValidateRequest()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Request data for Public Web Service for Vehicle Requests
 *
 * @version	6.10.0			02/02/2012
 * @author	Kathy Harrell
 * <br>Creation Date:		12/13/2010 11:39:17
 */
public class RtsVehPublicRequest extends RtsAbstractRequest
{

	private String csVIN;

	/**
	 * Return value of csVIN
	 * 
	 * @return String 
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Set value of csVIN
	 * 
	 * @param asVIN
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

	/** 
	 * 
	 * Return boolean to denote if Valid Request
	 * 
	 * @return boolean 
	 */
	public boolean isValidateRequest()
	{
		return getCaller() != null
			&& getCaller().length() > 0
			&& getCaller().length() <= 30
			&& getSessionId() != null
			&& getSessionId().length() > 0
			&& getSessionId().length() <= 30
			&& csVIN != null
			&& csVIN.length() > 0
			&& csVIN.length() <= CommonConstant.LENGTH_VIN_MAX
			&& getAction() == WebServicesActionsConstants.RTS_VEH_PUBLIC
			&& getVersionNo() == 0;
	}

	/**
	 * Null setter method required to prevent web services validation 
	 * error.
	 * 
	 * @param abValidRequest
	 */
	public void setValidateRequest(boolean abValidRequest)
	{
		// null setter
	}
}

