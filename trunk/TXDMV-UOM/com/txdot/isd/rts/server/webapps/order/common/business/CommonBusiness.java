package com.txdot.isd.rts.server.webapps.order.common.business;

import com.txdot.isd.rts.server.webapps.order.common.data.AbstractResponse;
import com.txdot.isd.rts.services.webapps.util.constants.ServiceConstants;

/*
 * CommonBusiness.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Jeff S.		05/15/2007	Created Class.
 * ---------------------------------------------------------------------
 */

/**
 * Common business functions.
 *
 * @version	Special Plates	05/15/2007
 * @author	Jeff Seifert
 * <br>Creation Date:		05/15/2007 08:15:00
 */
public class CommonBusiness
{
	/**
	 * Returns if the array of responses where all sucessfull.
	 * 
	 * @param aarrResponse AbstractResponse[]
	 * @return boolean
	 */
	public boolean isAllSuccessfull(AbstractResponse[] aarrResponse)
	{
		boolean lbReturn = true;
		for (int i = 0; i < aarrResponse.length; i++)
		{
			if (aarrResponse[i].getAck()
				!= ServiceConstants.AR_ACK_SUCCESS)
			{
				lbReturn = false;
				break;
			}
		}
		return lbReturn;
	}
}
