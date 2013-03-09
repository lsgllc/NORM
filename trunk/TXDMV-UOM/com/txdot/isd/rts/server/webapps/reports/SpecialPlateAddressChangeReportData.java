package com.txdot.isd.rts.server.webapps.reports;

import com.txdot.isd.rts.services.data.AddressData;

/*
 * SpecialPlateAddressChangeReportData.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown     	08/20/2003  CQU100004885  Added This data class for the 
 * 							Special plate
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 * K Harrell	02/06/2009	deprecated 
 * 							defect 9941 Ver Defect_POS_D 
 *----------------------------------------------------------------------
 */

/**
 * Hold the data fields needed for special plate address change report.
 *  
 * @version	Defect_POS_D	02/06/2009
 * @author	Bob Brown 
 * @deprecated 
 * <br>Creation Date:		03/25/2003 15:05:50
 */
public class SpecialPlateAddressChangeReportData
{
	private String csPltNo;
	private String csVin;
	private String csOwnerName;
	private AddressData caAddress;
	private String csCountyName;
	public String csDocNo;
	
	/**
	 * Get Address
	 * 
	 * @return com.txdot.isd.rts.services.data.AddressData
	 */
	public com.txdot.isd.rts.services.data.AddressData getAddress()
	{
		return caAddress;
	}
	
	/**
	 * Get County Name
	 * @return java.lang.String
	 */
	public java.lang.String getCountyName()
	{
		return csCountyName;
	}
	
	/**
	 * Get DocNo
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDocNo()
	{
		return csDocNo;
	}
	
	/**
	 * Get Owner Name
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getOwnerName()
	{
		return csOwnerName;
	}
	
	/**
	 * Get Plate Number
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPltNo()
	{
		return csPltNo;
	}
	
	/**
	 * Get VIN
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVin()
	{
		return csVin;
	}
	
	/**
	 * Set Address
	 * 
	 * @param AddressData asNewAddress
	 */
	public void setAddress(AddressData asNewAddress)
	{
		caAddress = asNewAddress;
	}
	
	/**
	 * Set County Name
	 * 
	 * @param asNewCountyName java.lang.String
	 */
	public void setCountyName(String asNewCountyName)
	{
		csCountyName = asNewCountyName;
	}
	
	/**
	 * Set DocNo
	 * 
	 * @param asNewCsDocNo java.lang.String
	 */
	public void setDocNo(java.lang.String asNewCsDocNo)
	{
		csDocNo = asNewCsDocNo;
	}
	
	/**
	 * Set Owner Name
	 * 
	 * @param asNewOwnerName java.lang.String
	 */
	public void setOwnerName(String asNewOwnerName)
	{
		csOwnerName = asNewOwnerName;
	}
	
	/**
	 * Set Plate Number
	 * 
	 * @param asNewPltNo java.lang.String
	 */
	public void setPltNo(String asNewPltNo)
	{
		csPltNo = asNewPltNo;
	}
	
	/**
	 * Set VIN
	 * 
	 * @param asNewVin java.lang.String
	 */
	public void setVin(String asNewVin)
	{
		csVin = asNewVin;
	}
}
