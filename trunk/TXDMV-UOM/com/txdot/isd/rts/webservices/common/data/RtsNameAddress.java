package com.txdot.isd.rts.webservices.common.data;

import com.txdot.isd.rts.services.data.NameAddressData;

/*
 * RtsNameAddress.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * D Hamilton	11/17/2011	Created class.
 * 							defect 10729 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * This lays out the data contained in an Address.
 *
 * @version	6.9.0			11/17/2011
 * @author	Dan Hamilton
 * <br>Creation Date:		11/17/2011 11:27:06
 */
public class RtsNameAddress {
	protected String csName1;
	protected String csName2;
	protected RtsAddress caRtsAddress;

	/**
	 * 
	 */
	public RtsNameAddress() {
		super();
		setRtsAddress(new RtsAddress());
	}

	/**
	 * 
	 */
	public RtsNameAddress(NameAddressData nameAddressData) {
		super();
		if (nameAddressData.getAddressData() != null) {
			setRtsAddress(new RtsAddress(nameAddressData.getAddressData()));
		}
		setName1(nameAddressData.getName1());
		setName2(nameAddressData.getName2());
	}

	/**
	 * @return
	 */
	public RtsAddress getRtsAddress() {
		return caRtsAddress;
	}

	/**
	 * @return
	 */
	public String getName1() {
		return csName1;
	}

	/**
	 * @return
	 */
	public String getName2() {
		return csName2;
	}

	/**
	 * @param address
	 */
	public void setRtsAddress(RtsAddress address) {
		caRtsAddress = address;
	}

	/**
	 * @param string
	 */
	public void setName1(String string) {
		csName1 = string;
	}

	/**
	 * @param string
	 */
	public void setName2(String string) {
		csName2 = string;
	}

}
