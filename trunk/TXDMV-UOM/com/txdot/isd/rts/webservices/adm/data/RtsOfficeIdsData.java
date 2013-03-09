package com.txdot.isd.rts.webservices.adm.data;

import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.webservices.common.data.RtsAddress;

/*
 * RtsOfficeIdsData.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/28/2008	Created class.
 * 							defect 9677 Ver MyPlates_POS
 * Ray Rowehl	07/16/2008	Change the constructor so that we pick 
 * 							up the physical street from the mailing 
 * 							street.
 * 							modify RtsOfficeIdsData(.)
 * 							defect 9677 Ver MyPlates_POS
 * R Pilon		02/02/2012	Added default constructor and missing setter
 * 							methods to prevent web service validation error.
 * 							add RtsOfficeIdsData() constructor, setPhoneNumber(),
 * 							  setTacName()
 * 							delete setCsPhoneNumber(), setCsTacName()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * This is the format for returning an OfficeIds row to the requestor.
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		05/28/2008 11:20:06
 */
public class RtsOfficeIdsData
{
	private String csCountyName;
	private int ciNumber;
	private String csTacName;
	private String csPhoneNumber;
	private String csEmailAddress;
	private RtsAddress caMailingAddress;
	private RtsAddress caPhysicalAddress;

	/**
	 * RtsOfficeIdsData.java Constructor
	 */
	public RtsOfficeIdsData()
	{
		super();
	}

	/**
	 * RtsOfficeIdsData.java Constructor
	 * 
	 * @param aaOID
	 */
	public RtsOfficeIdsData(OfficeIdsData aaOID)
	{
		super();
		ciNumber = aaOID.getOfcIssuanceNo();
		csCountyName = aaOID.getOfcName();
		csTacName = aaOID.getTacName();
		csPhoneNumber = aaOID.getTacPhoneNo();
		csEmailAddress = aaOID.getEMailAddr();
		RtsAddress laMailRAddr = new RtsAddress();
		laMailRAddr.setStreetLine1(aaOID.getOfcSt());
		laMailRAddr.setStreetLine2("");
		laMailRAddr.setCity(aaOID.getOfcCity());
		laMailRAddr.setState("TX");
		laMailRAddr.setZip(String.valueOf(aaOID.getOfcZpCd()));
		laMailRAddr.setZipP4(aaOID.getOfcZpCdP4());
		caMailingAddress = laMailRAddr;

		RtsAddress laPhysRAddr = new RtsAddress();
		
		// if there is no physical street, assume that the mailing 
		// street line is a physical street.
		if (aaOID.getPhysOfcLoc() != null
		&& aaOID.getPhysOfcLoc().trim().length() > 0)
		{
			laPhysRAddr.setStreetLine1(aaOID.getPhysOfcLoc());
		}
		else
		{
			laPhysRAddr.setStreetLine1(aaOID.getOfcSt());
		}
		
		laPhysRAddr.setStreetLine2("");
		laPhysRAddr.setCity(aaOID.getOfcCity());
		laPhysRAddr.setState("TX");
		laPhysRAddr.setZip(String.valueOf(aaOID.getOfcZpCd()));
		laPhysRAddr.setZipP4("");
		caPhysicalAddress = laPhysRAddr;
	}

	/**
	 * Return the Office Number of the County.
	 * 
	 * @return int
	 */
	public int getNumber()
	{
		return ciNumber;
	}

	/**
	 * Return the Name of the County.
	 * 
	 * @return String
	 */
	public String getCountyName()
	{
		return csCountyName;
	}

	/**
	 * Return the Email Address for the County.
	 * 
	 * @return String
	 */
	public String getEmailAddress()
	{
		return csEmailAddress;
	}

	/**
	 * Return the Phone Number of the County.
	 * 
	 * @return String
	 */
	public String getPhoneNumber()
	{
		return csPhoneNumber;
	}

	/**
	 * Return the Tax Assessor Collector name for the County.
	 * 
	 * @return String
	 */
	public String getTacName()
	{
		return csTacName;
	}

	/**
	 * Return the Mailing Address of the County.
	 * 
	 * @return RtsAddress
	 */
	public RtsAddress getMailingAddress()
	{
		return caMailingAddress;
	}

	/**
	 * Return the Physical Address of the County.
	 * 
	 * @return RtsAddress
	 */
	public RtsAddress getPhysicalAddress()
	{
		return caPhysicalAddress;
	}

	/**
	 * Set the Office Number of the County.
	 * 
	 * @param aiNumber
	 */
	public void setNumber(int aiNumber)
	{
		ciNumber = aiNumber;
	}

	/**
	 * Set the Name of the County.
	 * 
	 * @param asCountyName
	 */
	public void setCountyName(String asCountyName)
	{
		csCountyName = asCountyName;
	}

	/**
	 * Set the Email Address for the County.
	 * 
	 * @param string
	 */
	public void setEmailAddress(String string)
	{
		csEmailAddress = string;
	}

	/**
	 * Set the Phone Number of the County.
	 * 
	 * @param asPhoneNumber
	 */
	public void setPhoneNumber(String asPhoneNumber)
	{
		csPhoneNumber = asPhoneNumber;
	}

	/**
	 * Set the Tax Assessor Collector name for the County.
	 * 
	 * @param asTacName
	 */
	public void setTacName(String asTacName)
	{
		csTacName = asTacName;
	}

	/**
	 * Set the Mailing Address of the County.
	 * 
	 * @param aaMailingAddress
	 */
	public void setMailingAddress(RtsAddress aaMailingAddress)
	{
		caMailingAddress = aaMailingAddress;
	}

	/**
	 * Set the Physical Address of the County.
	 * 
	 * @param aaPhysicalAddress
	 */
	public void setPhysicalAddress(RtsAddress aaPhysicalAddress)
	{
		caPhysicalAddress = aaPhysicalAddress;
	}
}
