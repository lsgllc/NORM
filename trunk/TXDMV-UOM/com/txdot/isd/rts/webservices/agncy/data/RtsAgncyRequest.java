package com.txdot.isd.rts.webservices.agncy.data;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
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
 * RtsAgncyRequest.java
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
 * Ray Rowehl	01/24/2011	Reflow for combined object.
 * 							defect 10718 Ver 6.7.0
 * Ray Rowehl	05/20/2011	Add validation.
 * 							add validateAgncyRequest()
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	05/25/2011	Improve validation.
 * 							modify validateAgncyRequest()
 * 							defect 10718 Ver 6.8.0
 * ---------------------------------------------------------------------
 */

/**
 * Request Data input to RtsAgencyService.
 *
 * @version	6.8.0			05/25/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/05/2011 09:45:05
 */

public class RtsAgncyRequest extends RtsAbstractRequest
{
	private RtsWebAgncy caRtsWebAgncyInput;

	/**
	 * Get the Web Agency data object.
	 * 
	 * @return RtsWebAgncy
	 */
	public RtsWebAgncy getRtsWebAgncyInput()
	{
		return caRtsWebAgncyInput;
	}

	/**
	 * Set the Web Agency data object.
	 * 
	 * @param aaRtsWebAgncyInput
	 */
	public void setRtsWebAgncyInput(RtsWebAgncy aaRtsWebAgncyInput)
	{
		caRtsWebAgncyInput = aaRtsWebAgncyInput;
	}

	/**
	 * Make sure the meets at least basic edit checks.
	 * 
	 * @throws RTSException
	 */
	public void validateAgncyRequest() throws RTSException
	{
		validateAbstractRequest();

		// check for any problems with the input data.
		if (getRtsWebAgncyInput() != null
			&& getAction()
				== WebServicesActionsConstants.RTS_AGENCY_INSERT_UPDATE)
		{
			getRtsWebAgncyInput().validateWebAgncy();
		}
	}

}
