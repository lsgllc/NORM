package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * GeneralSearchData.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs/TP		06/05/2002	MultiRecs in Archive 
 * 							defect 4019
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B Brown		10/05/2010	Add csKey6, getter and setter
 * 							defect 10608 Ver POS 6.6.0
 * ---------------------------------------------------------------------
 */

/**
 * Data Object used for passing multiple types of parameters 
 * 
 * @version	POS 6.6.0		10/05/2010
 * @author	Administrator
 * <br>Creation Date:		08/14/2001 18:19:53  
 */

public class GeneralSearchData implements java.io.Serializable
{
	// int
	private int ciIntKey1;
	private int ciIntKey2;
	private int ciIntKey3;
	private int ciIntKey4;

	// Object
	private RTSDate caDate1;
	private RTSDate caDate2;

	// String 
	private String csKey1;
	private String csKey2;
	private String csKey3;
	private String csKey4;
	private String csKey5;
	// defect 10608
	private String csKey6;
	// end defect 10608

	// Constants 
	public final static String CHECK_NO = "FDSCHECKNO";
	public final static String FUNDS = "FDSRPTDATE";
	public final static String PAYMENT = "FDSPMTDATE";
	public final static String TRACE = "FDSTRACENO";

	private final static long serialVersionUID = -7334322981003606744L;

	/**
	 * GeneralSearchData constructor comment.
	 */
	public GeneralSearchData()
	{
		super();
	}
	/**
	 * Return the value of Date1
	 * 
	 * @return RTSDate
	 */
	public RTSDate getDate1()
	{
		return caDate1;
	}
	/**
	 * Return the value of Date2
	 * 
	 * @return RTSDate
	 */
	public RTSDate getDate2()
	{
		return caDate2;
	}
	/**
	 * Return the value of IntKey1
	 * 
	 * @return int
	 */
	public int getIntKey1()
	{
		return ciIntKey1;
	}
	/**
	 * Return the value of IntKey2
	 * 
	 * @return int
	 */
	public int getIntKey2()
	{
		return ciIntKey2;
	}
	/**
	 * Return the value of IntKey3
	 * 
	 * @return int
	 */
	public int getIntKey3()
	{
		return ciIntKey3;
	}
	/**
	 * Return the value of IntKey4
	 * 
	 * @return int
	 */
	public int getIntKey4()
	{
		return ciIntKey4;
	}
	/**
	 * Return the value of Key1
	 * Search Type
	 * 
	 * @return String
	 */
	public String getKey1()
	{
		return csKey1;
	}
	/**
	 * Return the value of Key2
	 * Search key
	 * 
	 * @return String
	 */
	public String getKey2()
	{
		return csKey2;
	}
	/**
	 * Return the value of Key3
	 * TransCd
	 * 
	 * @return String
	 */
	public String getKey3()
	{
		return csKey3;
	}
	/**
	 * Return the value of Key4
	 * OwnerId (4 digits)
	 * 
	 * @return String
	 */
	public String getKey4()
	{
		return csKey4;
	}
	/**
	 * Return the value of Key5
	 * MFLogError 
	 * 
	 * @return String
	 */
	public String getKey5()
	{
		return csKey5;
	}
	/**
	 * Return the value of Key6
	 * IRENEW 
	 * 
	 * @return String
	 */
	public String getKey6() {
		return csKey6;
	}

	/**
	 * Set the value of Date1
	 * 
	 * @param aaDate1 RTSDate
	 */
	public void setDate1(RTSDate aaDate1)
	{
		caDate1 = aaDate1;
	}
	/**
	 * Set the value of Date2
	 * 
	 * @param aaDate2 RTSDate
	 */
	public void setDate2(RTSDate aaDate2)
	{
		caDate2 = aaDate2;
	}
	/**
	 * Set the value of IntKey1
	 * 
	 * @param aiIntKey1 int
	 */
	public void setIntKey1(int aiIntKey1)
	{
		ciIntKey1 = aiIntKey1;
	}
	/**
	 * Set the value of IntKey2
	 * 
	 * @param aiIntKey2 int
	 */
	public void setIntKey2(int aiIntKey2)
	{
		ciIntKey2 = aiIntKey2;
	}
	/**
	 * Set the value of IntKey3
	 * 
	 * @param aiIntKey3 int
	 */
	public void setIntKey3(int aiIntKey3)
	{
		ciIntKey3 = aiIntKey3;
	}
	/**
	 * Set the value of IntKey4
	 * 
	 * @param aiIntKey4 int
	 */
	public void setIntKey4(int aiIntKey4)
	{
		ciIntKey4 = aiIntKey4;
	}
	/**
	 * Set the value of Key1
	 * Search type
	 * 
	 * @param asKey1 String
	 */
	public void setKey1(String asKey1)
	{
		csKey1 = asKey1;
	}
	/**
	 * Set the value of Key2
	 * Search Key
	 * 
	 * @param asKey2 String
	 */
	public void setKey2(String asKey2)
	{
		csKey2 = asKey2;
	}
	/**
	 * Set the value of Key3
	 * TransCd
	 * 
	 * @param asKey3 String
	 */
	public void setKey3(String asKey3)
	{
		csKey3 = asKey3;
	}
	/**
	 * Set the value of Key4
	 * OwnerId (4 digits)
	 * 
	 * @param asKey4 String
	 */
	public void setKey4(String asKey4)
	{
		csKey4 = asKey4;
	}
	/**
	 * Set the value of Key5
	 * MFLogError
	 * 
	 * @param asKey5 String
	 */
	public void setKey5(String asKey5)
	{
		csKey5 = asKey5;
	}
	/**
	 * Set the value of Key6
	 * IRENEW
	 * 
	 * @param asKey5 String
	 */
	public void setKey6(String asKey6) {
		csKey6 = asKey6;
	}

}
