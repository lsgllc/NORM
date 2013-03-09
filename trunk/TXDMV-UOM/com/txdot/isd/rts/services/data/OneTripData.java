package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * OneTripData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	05/24/2010	add isPopulated()
 * 							defect 10491 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * OneTripData 
 * 
 * @version	6.5.0 		05/24/2010
 * @author	Joseph Kwik
 * <br>Creation Date:	11/15/2001 17:15:35  
 */

public class OneTripData implements java.io.Serializable
{
	// String 
	private String csDestPnt = "";	
	private String csIntrmdtePnt1 = "";
	private String csIntrmdtePnt2 = "";
	private String csIntrmdtePnt3 = "";
	private String csOrigtnPnt = "";

	private final static long serialVersionUID = -1186028874119776259L;
	
	/**
	 * OneTripData constructor comment.
	 */
	public OneTripData()
	{
		super();
	}
	
	/**
	 * Return value of DestPnt
	 * 
	 * @return String
	 */
	public String getDestPnt()
	{
		return csDestPnt;
	}
	
	/**
	 * Return value of IntrmdtePnt1
	 * 
	 * @return String
	 */
	public String getIntrmdtePnt1()
	{
		return csIntrmdtePnt1;
	}
	
	/**
	 * Return value of IntrmdtePnt2
	 * 
	 * @return String
	 */
	public String getIntrmdtePnt2()
	{
		return csIntrmdtePnt2;
	}
	
	/**
	 * Return value of IntrmdtePnt3
	 * 
	 * @return String
	 */
	public String getIntrmdtePnt3()
	{
		return csIntrmdtePnt3;
	}
	
	/**
	 * Return value of OrigtnPnt
	 * 
	 * @return String
	 */
	public String getOrigtnPnt()
	{
		return csOrigtnPnt;
	}

	
	/**
	 * Return boolean to denote if OneTripData is Populated
	 * 
	 * @return boolean 
	 */
	public boolean isPopulated()
	{
		return (
			!(UtilityMethods.isEmpty(csDestPnt)
				&& UtilityMethods.isEmpty(csIntrmdtePnt1)
				&& UtilityMethods.isEmpty(csIntrmdtePnt2)
				&& UtilityMethods.isEmpty(csIntrmdtePnt3)
				&& UtilityMethods.isEmpty(csOrigtnPnt))); 
	}

	/**
	 * Set value of DestPnt
	 * 
	 * @param asDestPnt String
	 */
	public void setDestPnt(String asDestPnt)
	{
		csDestPnt = asDestPnt;
	}
	
	/**
	 * Set value of IntrmdtePnt1
	 * 
	 * @param asIntrmdtePnt1 String
	 */
	public void setIntrmdtePnt1(String asIntrmdtePnt1)
	{
		csIntrmdtePnt1 = asIntrmdtePnt1;
	}
	
	/**
	 * Set value of IntrmdtePnt2
	 * 
	 * @param asIntrmdtePnt2 String
	 */
	public void setIntrmdtePnt2(String asIntrmdtePnt2)
	{
		csIntrmdtePnt2 = asIntrmdtePnt2;
	}
	
	/**
	 * Set value of IntrmdtePnt3
	 * 
	 * @param asIntrmdtePnt3 String
	 */
	public void setIntrmdtePnt3(String asIntrmdtePnt3)
	{
		csIntrmdtePnt3 = asIntrmdtePnt3;
	}
	
	/**
	 * Set value of OrigtnPnt
	 * 
	 * @param asOrigtnPnt String
	 */
	public void setOrigtnPnt(String asOrigtnPnt)
	{
		csOrigtnPnt = asOrigtnPnt;
	}
}
