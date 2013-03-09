package com.txdot.isd.rts.webservices.adm.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RTSPlateTypeReponse.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Mark Reyes	06/16/2008	Create class
 * 							defect 9677 Ver MyPlates_POS
 * R Pilon		02/02/2012	Add missing setter method to prevent web service 
 * 							  validation error.
 * 							add setPlateTypeData()
 * 							delete setCarrPlateTypeData()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Response to caller.
 *
 * @version	6.10.0			02/02/2012
 * @author	Mark Reyes
 * <br>Creation Date:		06/16/2008 16:00:00
 */
public class RtsPlateTypeResponse extends RtsAbstractResponse
{
	private RtsPlateTypeData[] carrPlateTypeData;
	
	/**
	 * Get Plate Type array.
	 * 
	 * @return RtsPlateTypeData[]
	 */
	public RtsPlateTypeData[] getPlateTypeData()
	{
		return carrPlateTypeData;
	}
	
	/**
	 * Set Plate Type array.
	 * 
	 * @return RtsPlateTypeData[]
	 */
	public void setPlateTypeData(RtsPlateTypeData[] aarrPlateTypeData)
	{
		carrPlateTypeData = aarrPlateTypeData;
	}
}
