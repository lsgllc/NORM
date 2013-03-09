package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.util.Vector;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * MailingAddressData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3 
 * K Harrell	03/03/2009	add getMailingNameVector()
 * 							add caAddressData, get/set methods
 * 							delete caMailingAddress, get/set methods
 * 							modify MailingAddressData() 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/01/2009	deprecated. All referencing methods now use 
 * 							new NameAddressData.  
 * 							defect 10112 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/** 
 * Mailing address data used when a seperate mailing address is
 * captured and printed.
 * 
 * @version	Defect_POS_F 	07/01/2009  
 * @author	Todd Pederson
 * @deprecated 
 * <br>Creation Date:		09/07/2001 16:09:32  
 */

public class MailingAddressData implements Serializable
{
	// String
	private String csMailingName;
	private String csMailingName2;
	
	// defect 9969
	// Object	 
	private AddressData caAddressData;
	// end defect 9969	

	/**
	 * MailingAddressData constructor comment.
	 */
	public MailingAddressData()
	{
		super();
		csMailingName = "";
		csMailingName2 = "";
		// defect 9969 
		caAddressData = new AddressData();
		// end defect 9969 
	}

	/**
	 * Return value of MailingAddress
	 * 
	 * @return AddressData
	 */
	public AddressData getAddressData()
	{
		return caAddressData;
	}

	/**
	 * Return value of MailingName
	 * 
	 * @return String
	 */
	public String getMailingName()
	{
		return csMailingName;
	}

	/**
	 * Return value of MailingName2
	 * 
	 * @return String
	 */
	public String getMailingName2()
	{
		return csMailingName2;
	}

	/** 
	 * Return Vector of MailingNames 
	 * 
	 * @return Vector 
	 */
	public final Vector getMailingNameVector()
	{
		Vector lvVector = new Vector();
		lvVector.add(csMailingName);
		if (!UtilityMethods.isEmpty(csMailingName2))
		{
			lvVector.add(csMailingName2);
		}
		return lvVector;
	}

	/**
	 * Set value of AddressData
	 * 
	 * @param aaAddressData AddressData
	 */
	public void setAddressData(AddressData aaAddressData)
	{
		caAddressData = aaAddressData;
	}

	/**
	 * Set value of MailingName
	 * 
	 * @param asMailingName String
	 */
	public void setMailingName(String asMailingName)
	{
		csMailingName = asMailingName;
	}

	/**
	 * Set value of MailingName2
	 * 
	 * @param asMailingName2 String
	 */
	public void setMailingName2(String asMailingName2)
	{
		csMailingName2 = asMailingName2;
	}
}
