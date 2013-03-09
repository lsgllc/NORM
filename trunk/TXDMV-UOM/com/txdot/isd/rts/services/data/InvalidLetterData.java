package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * InvalidLetterData.java  
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
  * K Harrell	06/19/2005	Java 1.4 Work
 * 							moved to services.data	
 * 							defect 7899 Ver 5.2.3 							  
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * InvalidLetterData
 *
 * @version	5.2.3			06/19/2005 
 * @author	Administrator
 * <br>Creation Date:		08/30/2001
 */

public class InvalidLetterData implements Serializable
{
	// String 
	protected String csInvldLtrCombo;
	protected String csItmCd;

	private final static long serialVersionUID = 8034808810139931073L;
	/**
	 * Returns the value of InvldLtrCombo
	 * 
	 * @return  String 
	 */
	public final String getInvldLtrCombo()
	{
		return csInvldLtrCombo;
	}
	/**
	 * Returns the value of ItmCd
	 * 
	 * @return  String 
	 */
	public final String getItmCd()
	{
		return csItmCd;
	}
	/**
	 * This method sets the value of InvldLtrCombo.
	 * 
	 * @param asInvldLtrCombo   String 
	 */
	public final void setInvldLtrCombo(String asInvldLtrCombo)
	{
		csInvldLtrCombo = asInvldLtrCombo;
	}
	/**
	 * This method sets the value of ItmCd.
	 * 
	 * @param asItmCd   String 
	 */
	public final void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
}
