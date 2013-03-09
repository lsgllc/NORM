package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 * ScreenTTL035SavedData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/13/2010	Created
 * 							defect 10592 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * Object to save data from TTL035 
 *
 * @version	6.6.0 		09/13/2010
 * @author	Kathy Harrell 
 * <br>Creation Date:	09/13/2010 16:45:00
 */
public class ScreenTTL035SavedData implements Serializable
{
	private TitleData caTitleData;

	static final long serialVersionUID = 167786038800710467L;

	/**
	 * ScreenTTL035SavedData.java Constructor
	 * 
	 */
	public ScreenTTL035SavedData()
	{
		super();
	}

	/**
	 * Get value of caTitleData
	 * 
	 * @return TitleData
	 */
	public TitleData getTitleData()
	{
		return caTitleData;
	}

	/**
	 * Set value of caTitleData
	 * 
	 * @param aaTitleData
	 */
	public void setTitleData(TitleData aaTitleData)
	{
		caTitleData = aaTitleData;
	}
}
