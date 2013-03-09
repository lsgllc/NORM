package com.txdot.isd.rts.server.v21.admin.data;
/*
 * WsIndiStopCodesData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	01/14/2008	New class
 * 							defect 9502 Ver 3_Amigos_PH_A
 * ---------------------------------------------------------------------
 */

/**
 * WebServices data object for Stop Code Indicators.
 *
 * @version	3_Amigos_PH_A	01/14/2008
 * @author	B Hargrove
 * <br>Creation Date:		01/14/2008 11:06
 */
public class WsIndiStopCodesData
{

	private String csIndiFieldValue = "";
	private String csIndiName = "";
	private String csIndiStopCode = "";
	private String csIndiTransCode = "";
	
	/**
	 * Returns Stop Code Indicator Field AbstractValue.
	 * 
	 * @return String
	 */
	public String getIndiFieldValue()
	{
		return csIndiFieldValue;
	}
	
	/**
	 * Returns Stop Code Indicator Name.
	 * 
	 * @return String
	 */
	public String getIndiName()
	{
		return csIndiName;
	}
	
	/**
	 * Returns Stop Code Indicator Stop Code.
	 * 
	 * @return String
	 */
	public String getIndiStopCode()
	{
		return csIndiStopCode;
	}
	
	/**
	 * Returns Stop Code Indicator Transaction Code.
	 * 
	 * @return String
	 */
	public String getIndiTransCode()
	{
		return csIndiTransCode;
	}
	
	/**
	 * Sets Stop Code Indicator Field AbstractValue.
	 * 
	 * @param String aiIndiFieldValue
	 */
	public void setIndiFieldValue(String asIndiFieldValue)
	{
		csIndiFieldValue = asIndiFieldValue;
	}
	
	/**
	 * Sets Stop Code Indicator Name.
	 * 
	 * @param String aiIndiName
	 */
	public void setIndiName(String asIndiName)
	{
		csIndiName = asIndiName;
	}
	
	/**
	 * Sets Stop Code Indicator Stop Code.
	 * 
	 * @param String aiIndiStopCode
	 */
	public void setIndiStopCode(String asIndiStopCode)
	{
		csIndiStopCode = asIndiStopCode;
	}
	
	/**
	 * Sets Stop Code Indicator Transaction Code.
	 * 
	 * @param String aiIndiStopCode
	 */
	public void setIndiTransCode(String asIndiTransCode)
	{
		csIndiTransCode = asIndiTransCode;
	}

}
