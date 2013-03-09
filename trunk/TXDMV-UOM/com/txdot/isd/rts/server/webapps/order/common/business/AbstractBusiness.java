package com.txdot.isd.rts.server.webapps.order.common.business;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.txdot.isd.rts.server.webapps.order.common.data.AbstractResponse;
import com.txdot.isd.rts.server.webapps.order.common.data.Errors;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.webapps.util.constants.ServiceConstants;

/*
 * AbstractBusiness.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		03/06/2007	Created Class.
 * 							defect 9120 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Abstract Class used to force all of the business classes
 * to inherit the needed methods.
 *
 * @version	Special Plates	03/06/2007
 * @author	Jeff Seifert
 * <br>Creation Date:		03/06/2007 13:50:00
 */
public abstract class AbstractBusiness
{
	/**
	 * Inherited method used to handle all of the functions within this
	 * module.
	 * 
	 * @param aaObject Object
	 * @return Object
	 * @throws RTSException
	 */
	public abstract Object processData(Object aaObject)
		throws RTSException;
		
	/**
	 * Sets the error message for the response.
	 * 
	 * @param aaAbResponse AbstractResponse
	 * @param aeRTSEx Exception
	 */
	public void setError(AbstractResponse aaAbResponse, Exception aeRTSEx)
	{
		aaAbResponse.setAck(ServiceConstants.AR_ACK_FAILURE_WITH_WARNING);
		StringWriter laStringWriter = new StringWriter();
		PrintWriter laPrintWriter = new PrintWriter(laStringWriter);
		aeRTSEx.printStackTrace(laPrintWriter);
		Errors laError = new Errors();
		laError.setLongMessage(laStringWriter.toString());
		laError.setShortMessage(aeRTSEx.getMessage());
		aaAbResponse.setErrors(laError);
	}
}
