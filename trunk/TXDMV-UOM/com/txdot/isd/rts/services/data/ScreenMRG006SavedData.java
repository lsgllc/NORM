package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * ScreenMRG006SavedData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/01/2010	Created
 * 							defect 10592 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * Data object used to restore data after Cancel/Enter from MRG006
 *
 * @version	6.6.0			10/01/2010
 * @author	Kathy Harrell 
 * <br>Creation Date:		10/01/2010	14:36:17
 */
public class ScreenMRG006SavedData implements Serializable
{
	/**
	 * ScreenMRG006SavedData.java Constructor
	 */
	public ScreenMRG006SavedData()
	{
		super();
	}

	private PermitData caPrmtData;

	static final long serialVersionUID = 2286949507537667304L;

	/**
	 * Get value of caPrmtData
	 * 
	 * @return PermitData
	 */
	public PermitData getPrmtData()
	{
		return caPrmtData;
	}

	/**
	 * Set value of caPrmtData
	 * 
	 * @param aaPrmtData
	 */
	public void setPrmtData(PermitData aaPrmtData)
	{
		caPrmtData = aaPrmtData;
	}
}
