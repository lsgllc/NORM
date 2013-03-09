package com.txdot.isd.rts.webservices.agncy.data;

import java.util.Calendar;

/*
 * AgncyAuthData.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/27/2010	Initial load.
 * 							defect 10670 Ver 670
 * Ray Rowehl	12/29/2010	Update to match table.
 * 							defect 10670 Ver 670
 * Ray Rowehl	01/04/2011	Add cfg table data.
 * 							defect 10670 Ver 670
 * ---------------------------------------------------------------------
 */

/**
 * The data definition of Web_agency_auth for web services.
 * 
 * <p>Defines what counties an Agency works for.
 *
 * @version	6.7.0			01/04/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		12/27/2010 14:03:40
 */

public class AgncyAuthData
{
	private Calendar caChngTimeStmp;
	private boolean cbIssueInvIndi;
	private int ciAgencyAuthIdntyNo;
	private int ciAgencyIdntyNo;
	private int ciExpPrcsngMos;
	private int ciMaxSubmitCount;
	private int ciMaxSubmitDays;
	private int ciOfcIssuanceNo;
	private int ciSubconId;
	private String csExpPrcsngCd;
	private String csKeyEntryCode;
	private String csOfcName;
	
	/**
	 * Get the Agency Auth Indentity Number.
	 * 
	 * @return int
	 */
	public int getAgencyAuthIdntyNo()
	{
		return ciAgencyAuthIdntyNo;
	}
	
	/**
	 * Get Agency Identity Number.
	 * 
	 * @return int
	 */
	public int getAgencyIdntyNo()
	{
		return ciAgencyIdntyNo;
	}

	/**
	 * Get Timestamp of last Change.
	 * 
	 * @return Calendar
	 */
	public Calendar getChngTimeStmp()
	{
		return caChngTimeStmp;
	}

	/**
	 * Get the Expired Processing Code.
	 * 
	 * @return String
	 */
	public String getExpPrcsngCd()
	{
		return csExpPrcsngCd;
	}

	/**
	 * Get the Expired Processing Months.
	 * 
	 * @return int
	 */
	public int getExpPrcsngMos()
	{
		return ciExpPrcsngMos;
	}

	/**
	 * Get the Key Entry Code.
	 * 
	 * @return String
	 */
	public String getKeyEntryCode()
	{
		return csKeyEntryCode;
	}

	/**
	 * Get the Max Submit Count;
	 * 
	 * @return int
	 */
	public int getMaxSubmitCount()
	{
		return ciMaxSubmitCount;
	}

	/**
	 * Get the Max Submit Days
	 * 
	 * @return int
	 */
	public int getMaxSubmitDays()
	{
		return ciMaxSubmitDays;
	}

	/**
	 * Get Office Issuance Number.
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Get the Office Name.
	 * 
	 * @return String
	 */
	public String getOfcName()
	{
		return csOfcName;
	}

	/**
	 * Get the Subcon Id.
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}

	/**
	 * Get the Issue Inventory Indicator.
	 * 
	 * @return boolean
	 */
	public boolean isIssueInvIndi()
	{
		return cbIssueInvIndi;
	}

	/**
	 * Set the Agency Auth Indentity Number.
	 * 
	 * @param aiAgencyAuthIdntyNo
	 */
	public void setAgencyAuthIdntyNo(int aiAgencyAuthIdntyNo)
	{
		ciAgencyAuthIdntyNo = aiAgencyAuthIdntyNo;
	}

	/**
	 * Set the Agency Identity Number.
	 * 
	 * @param aiAgencyIdntyNo
	 */
	public void setAgencyIdntyNo(int aiAgencyIdntyNo)
	{
		ciAgencyIdntyNo = aiAgencyIdntyNo;
	}

	/**
	 * Set the Timestamp for the last change.
	 * 
	 * @param calendar
	 */
	public void setChngTimeStmp(Calendar calendar)
	{
		caChngTimeStmp = calendar;
	}

	/**
	 * Set the Expired Processing Code
	 * 
	 * @param asExpPrcsngCd
	 */
	public void setExpPrcsngCd(String asExpPrcsngCd)
	{
		csExpPrcsngCd = asExpPrcsngCd;
	}

	/**
	 * Set the Expired Processing Months.
	 * 
	 * @param aiExpPrcsngMos
	 */
	public void setExpPrcsngMos(int aiExpPrcsngMos)
	{
		ciExpPrcsngMos = aiExpPrcsngMos;
	}

	/**
	 * Set the Issue Inventory Indicator.
	 * 
	 * @param abIssueInvIndi
	 */
	public void setIssueInvIndi(boolean abIssueInvIndi)
	{
		cbIssueInvIndi = abIssueInvIndi;
	}

	/**
	 * Set the Key Entry Code.
	 * 
	 * @param asKeyEntryCode
	 */
	public void setKeyEntryCode(String asKeyEntryCode)
	{
		csKeyEntryCode = asKeyEntryCode;
	}

	/**
	 * Set the Max Submit Count.
	 * 
	 * @param aiMaxSubmitCount
	 */
	public void setMaxSubmitCount(int aiMaxSubmitCount)
	{
		ciMaxSubmitCount = aiMaxSubmitCount;
	}

	/**
	 * Set the Max Submit Days.
	 * 
	 * @param aiMaxSubmitDays
	 */
	public void setMaxSubmitDays(int aiMaxSubmitDays)
	{
		ciMaxSubmitDays = aiMaxSubmitDays;
	}

	/**
	 * Set the Office Issuance Number.
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set the Office Name.
	 * 
	 * @param asOfcName
	 */
	public void setOfcName(String asOfcName)
	{
		csOfcName = asOfcName;
	}

	/**
	 * Set the Subcon Id to match RTS POS.
	 * 
	 * @param aiSubconId
	 */
	public void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}

}
