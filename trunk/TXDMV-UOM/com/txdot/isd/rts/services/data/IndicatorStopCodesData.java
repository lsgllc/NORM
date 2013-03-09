package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * IndicatorStopCodesData.java 
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
 * IndicatorStopCodesData
 *
 * @version	5.2.3			06/19/2005 
 * @author	Administrator
 * <br>Creation Date:		08/30/2001
 */

public class IndicatorStopCodesData implements Serializable
{
	// String 
	protected String csIndiFieldValue;
	protected String csIndiName;
	protected String csIndiStopCd;
	protected String csIndiTransCd;

	private final static long serialVersionUID = -7809432406359294486L;

	/**
	 * Returns the value of IndiFieldValue
	 * 
	 * @return  String 
	 */
	public final String getIndiFieldValue()
	{
		return csIndiFieldValue;
	}
	/**
	 * Returns the value of IndiName
	 * 
	 * @return  String 
	 */
	public final String getIndiName()
	{
		return csIndiName;
	}
	/**
	 * Returns the value of IndiStopCd
	 * 
	 * @return  String 
	 */
	public final String getIndiStopCd()
	{
		return csIndiStopCd;
	}
	/**
	 * Returns the value of IndiTransCd
	 * 
	 * @return  String 
	 */
	public final String getIndiTransCd()
	{
		return csIndiTransCd;
	}
	/**
	 * This method sets the value of IndiFieldValue.
	 * 
	 * @param asIndiFieldValue   String 
	 */
	public final void setIndiFieldValue(String asIndiFieldValue)
	{
		csIndiFieldValue = asIndiFieldValue;
	}
	/**
	 * This method sets the value of IndiName.
	 * 
	 * @param asIndiName   String 
	 */
	public final void setIndiName(String asIndiName)
	{
		csIndiName = asIndiName;
	}
	/**
	 * This method sets the value of IndiStopCd.
	 * 
	 * @param asIndiStopCd   String 
	 */
	public final void setIndiStopCd(String asIndiStopCd)
	{
		csIndiStopCd = asIndiStopCd;
	}
	/**
	 * This method sets the value of IndiTransCd.
	 * 
	 * @param asIndiTransCd   String 
	 */
	public final void setIndiTransCd(String asIndiTransCd)
	{
		csIndiTransCd = asIndiTransCd;
	}
}
