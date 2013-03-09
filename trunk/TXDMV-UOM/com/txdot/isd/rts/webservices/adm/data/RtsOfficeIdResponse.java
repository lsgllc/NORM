package com.txdot.isd.rts.webservices.adm.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RtsOfficeIdResponse.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/29/2008	Created class.
 * 							defect 9677 Ver MyPlates_POS
 * R Pilon		02/02/2012	Add missing setter method to prevent web service 
 * 							  validation error.
 * 							add setOfficeData()
 * 							delete setCarrOfficeData()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Response to caller.
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		05/29/2008 18:05:06
 */
public class RtsOfficeIdResponse extends RtsAbstractResponse
{
	private RtsOfficeIdsData[] carrOfficeData;
	
	/**
	 * Get the Office Ids array.
	 * 
	 * @return RtsOfficeIdsData[]
	 */
	public RtsOfficeIdsData[] getOfficeData()
	{
		return carrOfficeData;
	}

	/**
	 * Set the Office Ids array.
	 * 
	 * @param aarrOfficeData
	 */
	public void setOfficeData(RtsOfficeIdsData[] aarrOfficeData)
	{
		carrOfficeData = aarrOfficeData;
	}
}
