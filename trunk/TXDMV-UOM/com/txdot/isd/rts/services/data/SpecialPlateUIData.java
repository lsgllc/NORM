package com.txdot.isd.rts.services.data;

/*
 * SpecialPlateUIData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/17/2007	Created
 * 							defect 9085 Ver Special Plates  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * SpecialPlateUIData
 *
 * @version	Special Plates	02/17/2007
 * @author	Kathy Harrell
 * <br>Creation Date:		02/17/2007  17:14:00
 */
public class SpecialPlateUIData extends ExemptAuditUIData
{
	private boolean cbPersonalizedOnly;

	static final long serialVersionUID = 7124201106823970483L;

	/**
	 * Returns value of cbPersonalizedOnly
	 * 
	 * @return
	 */
	public boolean isPersonalizedOnly()
	{
		return cbPersonalizedOnly;
	}

	/**
	 * Sets value of cbPersonalizedOnly
	 * 
	 * @param abPersonalized
	 */
	public void setPersonalizedOnly(boolean abPersonalized)
	{
		cbPersonalizedOnly = abPersonalized;
	}

}
