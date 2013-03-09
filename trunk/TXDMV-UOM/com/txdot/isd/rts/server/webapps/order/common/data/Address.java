package com.txdot.isd.rts.server.webapps.order.common.data;

import java.io.Serializable;

/*
 * Address.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown		03/02/2007	Created class.
 * 							defect 9121 Ver Special Plates 
 * B Brown		03/16/2008	To aid in the ability of the MQAccess class  
 * 							on the POS servers to process a 
 * 							SpecialPltOrderCart object this class, which
 * 							is a part of the SpecialPltOrderCart, has to
 * 		`					be serializable.
 * 							defect 9599 Ver Tres Amigos Prep
 * B Brown		03/25/2008	Let WSAD generate serialVersionUID  
 * 							defect 9599 Ver Tres Amigos Prep  
 * ---------------------------------------------------------------------
 */

/**
 * This is the address data class.
 *
 * @version	Tres Amigos Prep	03/25/2008
 * @author	bbrown
 * <br>Creation Date:			03/02/2007 14:30:00
 */
public class Address
		// defect 9599
		implements Serializable
		// end defect 9599
{	
	// defect 9599
	// private final static long serialVersionUID = 0x757bb79f2cbc744dL;
	static final long serialVersionUID = -443900073919352559L;
	// end defect 9599	
	private String city = "";
	private String state = "";
	private String street1 = "";
	private String street2 = "";
	private String zipCd = "";
	private String zipCd4 = "";
	private String county = "";
	/**
	 * gets city
	 *
	 * @return String
	 */
	public String getCity()
	{
		return city;
	}

	/**
	 * gets state
	 *
	 * @return String
	 */
	public String getState()
	{
		return state;
	}

	/**
	 * gets street1
	 *
	 * @return String
	 */
	public String getStreet1()
	{
		return street1;
	}

	/**
	 * gets street2
	 *
	 * @return String
	 */
	public String getStreet2()
	{
		return street2;
	}

	/**
	 * gets zipCd
	 *
	 * @return String
	 */
	public String getZipCd()
	{
		return zipCd;
	}

	/**
	 * gets zipCd4
	 *
	 * @return String
	 */
	public String getZipCd4()
	{
		return zipCd4;
	}

	/**
	 * sets city
	 * 
	 * @param asCity
	 */
	public void setCity(String asCity)
	{
		city = asCity;
	}

	/**
	 * sets state
	 * 
	 * @param asState
	 */
	public void setState(String asState)
	{
		state = asState;
	}

	/**
	 * sets street1
	 * 
	 * @param asString1
	 */
	public void setStreet1(String asStreet1)
	{
		street1 = asStreet1;
	}

	/**
	 * sets street2
	 * 
	 * @param asStreet2
	 */
	public void setStreet2(String asStreet2)
	{
		street2 = asStreet2;
	}

	/**
	 * sets zipCd
	 * 
	 * @param asZipCd
	 */
	public void setZipCd(String asZipCd)
	{
		zipCd = asZipCd;
	}

	/**
	 * sets zipCd4
	 * 
	 * @param asZipCd4
	 */
	public void setZipCd4(String asZipCd4)
	{
		zipCd4 = asZipCd4;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public String getCounty()
	{
		return county;
	}

	/**
	 * Method description
	 * 
	 * @param string
	 */
	public void setCounty(String string)
	{
		county = string;
	}

}
