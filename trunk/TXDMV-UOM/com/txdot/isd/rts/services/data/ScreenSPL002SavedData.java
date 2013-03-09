package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * ScreenSPL002SavedData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/21/2010	Created
 * 							defect 10592 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * Data object used to restore data after Cancel/Enter from SPL002
 *
 * @version	6.6.0			09/21/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/21/2010	09:57:17
 */
public class ScreenSPL002SavedData implements Serializable
{

	/**
	 * ScreenSPL002SavedData.java Constructor
	 * 
	 */
	public ScreenSPL002SavedData()
	{
		super();
	}

	private SpecialPlatesRegisData caSpclPltRegisData = null;

	static final long serialVersionUID = 1492101839703597235L;

	/**
	 * Return caSpclPltRegisData
	 * 
	 * @return caSpclPltRegisData
	 */
	public SpecialPlatesRegisData getSpclPltRegisData()
	{
		return caSpclPltRegisData;
	}

	/**
	 * Sets value of caSpclPltRegisData
	 * 
	 * @param aaSpclPltRegis
	 */
	public void setSpclPltRegisData(SpecialPlatesRegisData aaSpclPltRegis)
	{
		caSpclPltRegisData = aaSpclPltRegis;

	}

}
