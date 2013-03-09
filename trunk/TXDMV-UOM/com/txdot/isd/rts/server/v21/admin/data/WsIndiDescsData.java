package com.txdot.isd.rts.server.v21.admin.data;
/*
 * WsIndiDescsData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	02/08/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Indicator Descriptions.
 *
 * @version	3_Amigos_PH_A	02/08/2008
 * @author	B Hargrove
 * <br>Creation Date:		01/10/2008 08:44
 */
public class WsIndiDescsData
{
	private String csIndiDesc = "";
	private String csIndiFieldValue = "";
	private String csIndiName = "";
	
	private int ciIndiScrnPriority = 0;
	
	/**
	 * Returns Indicator Description.
	 * 
	 * @return String
	 */
	public String getIndiDesc()
	{
		return csIndiDesc;
	}
	
	/**
	 * Returns Indicator Field AbstractValue.
	 * 
	 * @return String
	 */
	public String getIndiFieldValue()
	{
		return csIndiFieldValue;
	}
	
	/**
	 * Returns Indicator Name.
	 * 
	 * @return String
	 */
	public String getIndiName()
	{
		return csIndiName;
	}
	
	/**
	 * Returns Indicator Screen Priority.
	 * 
	 * @return int
	 */
	public int getIndiScrnPriority()
	{
		return ciIndiScrnPriority;
	}
	
	/**
	 * Sets Indicator Description.
	 * 
	 * @param String asIndiDesc
	 */
	public void setIndiDesc(String asIndiDesc)
	{
		csIndiDesc = asIndiDesc;
	}
	
	/**
	 * Sets Indicator Field AbstractValue.
	 * 
	 * @param String asIndiFieldValue
	 */
	public void setIndiFieldValue(String asIndiFieldValue)
	{
		csIndiFieldValue = asIndiFieldValue;
	}
	
	/**
	 * Sets Indicator Name.
	 * 
	 * @param String asIndiName
	 */
	public void setIndiName(String asIndiName)
	{
		csIndiName = asIndiName;
	}
	
	/**
	 * Sets Indicator Screen Priority.
	 * 
	 * @param int aiIndiScrnPriority
	 */
	public void setIndiScrnPriority(int aiIndiScrnPriority)
	{
		ciIndiScrnPriority = aiIndiScrnPriority;
	}

}
