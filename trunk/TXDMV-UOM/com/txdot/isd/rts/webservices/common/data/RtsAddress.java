package com.txdot.isd.rts.webservices.common.data;

import com.txdot.isd.rts.services.data.AddressData;

/*
 * RtsAddress.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/28/2008	Created class.
 * 							defect 9675 Ver MyPlates_POS
 * ---------------------------------------------------------------------
 */

/**
 * This lays out the data contained in an Address.
 *
 * @version	MyPlates_POS	05/28/2008
 * @author	Ray Rowehl
 * <br>Creation Date:		05/28/2008 11:27:06
 */
public class RtsAddress
{
	private String csStreetLine1;
	private String csStreetLine2;
	private String csCity;
	private String csState;
	private String csZip;
	private String csZipP4;

	/**
	 * 
	 */
	public RtsAddress() {
		super();
	}

	/**
	 * 
	 */
	public RtsAddress(AddressData addressData) {
		super();
		setStreetLine1(addressData.getSt1());
		setStreetLine2(addressData.getSt2());
		setCity(addressData.getCity());
		setState(addressData.getState());
		setZip(addressData.getZpcd());
		setZipP4(addressData.getZpcdp4());
	}

	/**
	 * Return the City of the address.
	 * 
	 * @return String
	 */
	public String getCity()
	{
		return csCity;
	}

	/**
	 * Return the State of the address.
	 * 
	 * @return String
	 */
	public String getState()
	{
		return csState;
	}

	/**
	 * Return the Street Line 1 of the address.
	 * 
	 * @return String
	 */
	public String getStreetLine1()
	{
		return csStreetLine1;
	}

	/**
	 * Return the Street Line 2 of the address.
	 * 
	 * @return String
	 */
	public String getStreetLine2()
	{
		return csStreetLine2;
	}

	/**
	 * Return the Zip Code of the address.
	 * 
	 * @return String
	 */
	public String getZip()
	{
		return csZip;
	}

	/**
	 * Return the Zip Code Plus 4 of the address.
	 * 
	 * @return String
	 */
	public String getZipP4()
	{
		return csZipP4;
	}

	/**
	 * Set the City of the address.
	 * 
	 * @param asCity
	 */
	public void setCity(String asCity)
	{
		csCity = asCity;
	}

	/**
	 * Set the State of the address.
	 * 
	 * @param asState
	 */
	public void setState(String asState)
	{
		csState = asState;
	}

	/**
	 * Set the Street Line 1 of the address.
	 * 
	 * @param asStreetLine1
	 */
	public void setStreetLine1(String asStreetLine1)
	{
		csStreetLine1 = asStreetLine1;
	}

	/**
	 * Set the Street Line 2 of the address.
	 * 
	 * @param asStreetLine2
	 */
	public void setStreetLine2(String asStreetLine2)
	{
		csStreetLine2 = asStreetLine2;
	}

	/**
	 * Set the Zip Code of the address.
	 * 
	 * @param asZip
	 */
	public void setZip(String asZip)
	{
		csZip = asZip;
	}

	/**
	 * Set the Zip Code Plus 4 of the address.
	 * 
	 * @param asZipP4
	 */
	public void setZipP4(String asZipP4)
	{
		csZipP4 = asZipP4;
	}

}
