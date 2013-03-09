package com.txdot.isd.rts.server.v21.vehicleinfo.data;
/*
 * WsVehicleInfoV21DataRes.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/31/2008	New class
 * 							Version A adds the Indicator data array.
 * 							defect 9502 Ver 3_Amigos_PH_A
 * Ray Rowehl	02/05/2008	Add Title Issue Date.
 * 							add ciTtlIssueDate
 * 							add getTtlIssueDate(), setTtlIssueDate()
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * Contains the Vehicle Info in response to a V21 Vehicle Info Request.
 * 
 * <p>This is Version A.
 *
 * @version	3_Amigos_PH_A	02/05/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		01/31/2008 17:27:36
 */
public class WsVehicleInfoV21DataResVerA extends WsVehicleInfoV21DataRes
{
	/**
	 * Vehicle Indicators data
	 */
	private WsVehicleInfoV21Indicator[] carrIndicators;
	
	/**
	 * Title Issue Date.
	 */
	private int ciTtlIssueDate;

	/**
	 * Return the Indicators array.
	 * 
	 * @return WsVehicleInfoV21Indicator[]
	 */
	public WsVehicleInfoV21Indicator[] getIndicators()
	{
		return carrIndicators;
	}
	
	/**
	 * Return the Title Issue Date.
	 * 
	 * @return int
	 */
	public int getTtlIssueDate()
	{
		return ciTtlIssueDate;
	}

	/**
	 * Set the Indicators array.
	 * 
	 * @param aarrIndicators
	 */
	public void setIndicators(WsVehicleInfoV21Indicator[] aarrIndicators)
	{
		carrIndicators = aarrIndicators;
	}

	public void setResult(int aiErrorCode)
	{
		setResult(String.valueOf(aiErrorCode));
	}

	/**
	 * Set the Title Issue Date
	 * 
	 * @param aiTtlIssueDate
	 */
	public void setTtlIssueDate(int aiTtlIssueDate)
	{
		ciTtlIssueDate = aiTtlIssueDate;
	}

}
