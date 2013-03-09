/**
 * 
 */
package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * ScreenTTL010SavedData.java
 *
 * (c) Texas Department of Motor Vehicles 2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/13/2012	Created
 * 							defect 10827 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/** 
 * Object to save data from TTL010
 * 
 * @version	6.10.0		01/13/2012 
 * @author	Kathy Harrell 
 * @since				01/13/2012 
 */
public class ScreenTTL010SavedData implements Serializable
{
	private boolean cbAddlSurvivors;
	
	private String csName1;
	private String csName2;
	
	private static final long serialVersionUID = 1443020591808492488L;
	
	/**
	 * Return boolean to denote if checkbox for AddlSurvivors is set  
	 * 
	 * @return boolean
	 */
	public boolean isAddlSurvivors()
	{
		return cbAddlSurvivors;
	}
	
	/** 
	 * Return boolean to denote if any data entered or selected  
	 * 
	 * @return boolean 
	 */
	public boolean isEmpty() 
	{
		return UtilityMethods.isEmpty(csName1) && UtilityMethods.isEmpty(csName2)
		&& !isAddlSurvivors(); 
	}
	/**
	 * Return value of csName1
	 * 
	 * @return String
	 */
	public String getName1()
	{
		return csName1;
	}
	/**
	 * Return value of csName2
	 * 
	 * @return String
	 */
	public String getName2()
	{
		return csName2;
	}
	/**
	 * Set value of abAddlSurvivors 
	 * 
	 * @param abAddlSurvivors 
	 */
	public void setAddlSurvivors(boolean abAddlSurvivors)
	{
		cbAddlSurvivors = abAddlSurvivors;
	}
	/**
	 * Set value of csName1
	 * 
	 * @param asName1 
	 */
	public void setName1(String asName1)
	{
		csName1 = asName1;
	}
	/**
	 * Set value of csName2
	 * 
	 * @param String
	 */
	public void setName2(String asName2)
	{
		csName2 = asName2;
	} 
}
