package com.txdot.isd.rts.services.data;

/*
 *
 * EmailData.java   
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/10/2005	Java 1.4 Work
 * 							moved from services.data.webapps
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * EmailData
 * 
 * @version	5.2.3			10/10/2005 
 * @author	Administrator
 * <br>Creation Date:		10/02/2001 10:39:25
 */

public class EmailData
{
	// int 
	private int ciCntyStatusCd;

	// String
	private String csEmail;
	private String csDocNo;
	private String csPlateNo;
	private String csVin;
	private String csRenewalDt;

	/**
	 * EmailData constructor comment.
	 */
	public EmailData()
	{
		super();
	}
	/**
	 * Return value of CntyStatusCd
	 * 
	 * @return int
	 */
	public int getCntyStatusCd()
	{
		return ciCntyStatusCd;
	}
	/**
	 * 
	 * Return value of DocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}
	/**
	 * 
	 * Return value of Email
	 * 
	 * @return String
	 */
	public String getEmail()
	{
		return csEmail;
	}
	/**
	 * 
	 * Return value of PlateNo
	 * 
	 * @return String
	 */
	public String getPlateNo()
	{
		return csPlateNo;
	}
	/**
	 * 
	 * Return value of RenewalDt
	 * 
	 * @return String
	 */
	public String getRenwalDt()
	{
		return csRenewalDt;
	}
	/**
	 * 
	 * Return value of Vin
	 * 
	 * @return String
	 */
	public String getVin()
	{
		return csVin;
	}
	/**
	 * Set value of CntyStatusCd
	 * 
	 * @param aiCntyStatusCd int
	 */
	public void setCntyStatusCd(int aiCntyStatusCd)
	{
		ciCntyStatusCd = aiCntyStatusCd;
	}
	/**
	 * 
	 * Set value of DocNo
	 * 
	 * @param asDocNo String
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}
	/**
	 * 
	 * Set value of Email
	 * 
	 * @param asEmail String
	 */
	public void setEmail(String asEmail)
	{
		csEmail = asEmail;
	}
	/**
	 * 
	 * Set value of PlateNo
	 * 
	 * @param asPlateNo String
	 */
	public void setPlateNo(String asPlateNo)
	{
		csPlateNo = asPlateNo;
	}
	/**
	 * 
	 * Set value of RenewalDt
	 * 
	 * @param asRenewalDt String
	 */
	public void setRenewalDt(String asRenewalDt)
	{
		csRenewalDt = asRenewalDt;
	}
	/**
	 * 
	 * Set value of Vin
	 * 
	 * @param asVin String
	 */
	public void setVin(String asVin)
	{
		csVin = asVin;
	}
}
