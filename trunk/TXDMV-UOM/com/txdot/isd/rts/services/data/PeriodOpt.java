package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * PeriodOpt.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	02/18/2005	Move to services.data and deprecate.
 *							defect 7705 Ver 5.2.3
 * B Hargrove	03/31/2005	Un-deprecate. This class is used in
 *							miscreg.ui.FrmTempAddlWeightOptionsMRG011
 *							defect 7893 Ver 5.2.3
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * This class is used for calculating Misc Registration Fees.
 * 
 * @version	5.2.3			04/21/2005 
 * @author	Sunil Govindappa
 * <br>Creation Date:		11/05/2001 13:45:06
 */
public class PeriodOpt implements java.io.Serializable
{

	// Object
	private Dollar caItmPrice;
	private RTSDate caPrmtExpDate;

	//	String 
	private String csItmCd;
	private String csItmDesc;
	private String csPrmtValdityPeriod;

	private final static long serialVersionUID = -6970848477848712928L;

	/**
	 * PeriodOpt constructor comment.
	 */
	public PeriodOpt()
	{
		super();
	}
	/**
	 * Return value of ItmCd
	 * 
	 * @return String
	 */
	public String getItmCd()
	{
		return csItmCd;
	}
	/**
	 * Return value of ItmDesc
	 * 
	 * @return String
	 */
	public String getItmDesc()
	{
		return csItmDesc;
	}
	/**
	 * Return value of ItmPrice
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice()
	{
		return caItmPrice;
	}
	/**
	 * Return value of PrmtExpDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getPrmtExpDate()
	{
		return caPrmtExpDate;
	}
	/**
	 * Return value of PrmtValdityPeriod
	 * 
	 * @return String
	 */
	public String getPrmtValdityPeriod()
	{
		return csPrmtValdityPeriod;
	}
	/**
	 * Set value of ItmCd
	 * 
	 * @param asItmCd String
	 */
	public void setItmCd(String asItmCd)
	{
		csItmCd = asItmCd;
	}
	/**
	 * Set value of ItmDesc
	 * 
	 * @param asItmDesc String
	 */
	public void setItmDesc(String asItmDesc)
	{
		csItmDesc = asItmDesc;
	}
	/**
	 * Set value of ItmPrice
	 * 
	 * @param aaItmPrice Dollar
	 */
	public void setItmPrice(Dollar aaItmPrice)
	{
		caItmPrice = aaItmPrice;
	}
	/**
	 * Set value of PrmtExpDate
	 * 
	 * @param aaPrmtExpDate RTSDate
	 */
	public void setPrmtExpDate(RTSDate aaPrmtExpDate)
	{
		caPrmtExpDate = aaPrmtExpDate;
	}
	/**
	 * Set value of PrmtValdityPeriod
	 * 
	 * @param asPrmtValdityPeriod String
	 */
	public void setPrmtValdityPeriod(String asPrmtValdityPeriod)
	{
		csPrmtValdityPeriod = asPrmtValdityPeriod;
	}
}
