package com.txdot.isd.rts.services.data;

import java.io.Serializable;

/*
 *
 * DMVTRMessageData.java 
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
 * DMVTRMessageData
 *
 * @version	5.2.3			06/19/2005 
 * @author	Administrator
 * <br>Creation Date:		09/04/2001
 */

public class DMVTRMessageData implements Serializable
{
	// String 
	protected String csRTSDMVTRMsg1;
	protected String csRTSDMVTRMsg2;
	protected String csRTSDMVTRMsg3;

	private final static long serialVersionUID = -5535662138042748623L;
	/**
	 * Returns the value of RTSDMVTRMsg1
	 * 
	 * @return  String 
	 */
	public final String getRTSDMVTRMsg1()
	{
		return csRTSDMVTRMsg1;
	}
	/**
	 * Returns the value of RTSDMVTRMsg2
	 * 
	 * @return  String 
	 */
	public final String getRTSDMVTRMsg2()
	{
		return csRTSDMVTRMsg2;
	}
	/**
	 * Returns the value of RTSDMVTRMsg3
	 * 
	 * @return  String 
	 */
	public final String getRTSDMVTRMsg3()
	{
		return csRTSDMVTRMsg3;
	}
	/**
	 * This method sets the value of RTSDMVTRMsg1.
	 * 
	 * @param asRTSDMVTRMsg1   String 
	 */
	public final void setRTSDMVTRMsg1(String asRTSDMVTRMsg1)
	{
		csRTSDMVTRMsg1 = asRTSDMVTRMsg1;
	}
	/**
	 * This method sets the value of RTSDMVTRMsg2.
	 * 
	 * @param asRTSDMVTRMsg2   String 
	 */
	public final void setRTSDMVTRMsg2(String asRTSDMVTRMsg2)
	{
		csRTSDMVTRMsg2 = asRTSDMVTRMsg2;
	}
	/**
	 * This method sets the value of RTSDMVTRMsg3.
	 * 
	 * @param asRTSDMVTRMsg3   String 
	 */
	public final void setRTSDMVTRMsg3(String asRTSDMVTRMsg3)
	{
		csRTSDMVTRMsg3 = asRTSDMVTRMsg3;
	}
}
