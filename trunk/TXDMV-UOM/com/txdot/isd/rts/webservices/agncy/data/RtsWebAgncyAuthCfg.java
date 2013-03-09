package com.txdot.isd.rts.webservices.agncy.data;

import java.util.Calendar;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.WebAgencyAuthCfgData;
import com.txdot.isd.rts.services.data.WebAgencyAuthData;

/*
 * RtsWebAgncyAuthCfg.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/11/2011	Initial load.
 * 							defect 10670 Ver 6.7.0
 * K McKee      10/20/2011  add RtsWebAgncyAuthCfg(WebAgencyAuthCfgData)
 * 							defect 11151 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * The data definition of RTS.RTS_WEB_AGNCY_AUTH and 
 * RTS.RTS_WEB_AGNCY_AUTH_CFG for web services.
 * 
 * <p>Defines what counties an Agency works for.
 *
 * @version	6.9.0			10/20/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/11/2011 16:27:56
 */

public class RtsWebAgncyAuthCfg
{
	// Web_agency_auth
	private int ciAgncyIdntyNo;
	private int ciOfcIssuanceNo;
	private int ciSubconId;
	
	private String csOfcName;
	
	// Web_agency_auth_cfg
	
	private Calendar caChngTimeStmp;

	private int ciAgncyAuthIdntyNo;
	private int ciExpPrcsngMos;
	private int ciMaxSubmitCount;
	private int ciMaxSubmitDays;
	private int ciUpdtngAgntIdntyNo;
	
	private String csExpPrcsngCd;
	private String csKeyEntryCode;
	
	private boolean cbPilotIndi;
	private boolean cbIssueInvIndi;
	
	/**
	 * AgncyData Constructor 
	 */
	public RtsWebAgncyAuthCfg()
	{
		super();
	}
	
	/**
	 * Copy the Agency Auth data to the object.
	 * 
	 * @param aaWAAD   WebAgencyAuthData
	 */
	public void setAgencyAuthData(WebAgencyAuthData aaWAAD)
	{
		setAgncyAuthIdntyNo(aaWAAD.getAgncyAuthIdntyNo());
		setAgncyIdntyNo(aaWAAD.getAgncyIdntyNo());
		setOfcIssuanceNo(aaWAAD.getOfcIssuanceNo());
		setOfcName(OfficeIdsCache.getOfcName(aaWAAD.getOfcIssuanceNo()));
		setSubconId(aaWAAD.getSubconId());	
	}
	
	/**
	 * RtsWebAgncyAuthCfg.java Constructor
	 * defect 11151
	 * 
	 * @param   aaObject    WebAgencyAuthCfgData
	 */
	public RtsWebAgncyAuthCfg(WebAgencyAuthCfgData aaObject)
	{
		super();
		setAgncyAuthIdntyNo(aaObject.getAgncyAuthIdntyNo());
		setAgncyIdntyNo(aaObject.getAgncyIdntyNo());
		setOfcIssuanceNo(aaObject.getOfcIssuanceNo());
		setOfcName(OfficeIdsCache.getOfcName(aaObject.getOfcIssuanceNo()));
		setSubconId(aaObject.getSubconId());	
		setChngTimeStmp(aaObject.getChngTimestmp().getCalendar());
		setExpPrcsngCd(aaObject.getExpProcsngCd());
		setExpPrcsngMos(aaObject.getExpProcsngMos());
		setIssueInvIndi(aaObject.getIssueInvIndi() == 1);
		setKeyEntryCode(aaObject.getKeyEntryCd());
		setMaxSubmitCount(aaObject.getMaxSubmitCount());
		setMaxSubmitDays(aaObject.getMaxSubmitDays());
		setPilotIndi(aaObject.getPilotIndi() == 1);
	}
	
	/**
	 * Get the Agency Auth Indentity Number.
	 * 
	 * @return int
	 */
	public int getAgncyAuthIdntyNo()
	{
		return ciAgncyAuthIdntyNo;
	}
	
	/**
	 * Get Agency Identity Number.
	 * 
	 * @return int
	 */
	public int getAgncyIdntyNo()
	{
		return ciAgncyIdntyNo;
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
	 * Get the Updating Agent ID
	 * 
	 * @return the ciUpdtngAgntIdntyNo
	 */
	public int getUpdtngAgntIdntyNo()
	{
		return ciUpdtngAgntIdntyNo;
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
	 * Get the pilot indicator
	 * 
	 * @return
	 */
	public boolean isPilotIndi()
	{
		return cbPilotIndi;
	}
	
	/**
	 * Set the Agency Auth Indentity Number.
	 * 
	 * @param aiAgncyAuthIdntyNo
	 */
	public void setAgncyAuthIdntyNo(int aiAgncyAuthIdntyNo)
	{
		ciAgncyAuthIdntyNo = aiAgncyAuthIdntyNo;
	}

	/**
	 * Set the Agency Identity Number.
	 * 
	 * @param aiAgncyIdntyNo
	 */
	public void setAgncyIdntyNo(int aiAgncyIdntyNo)
	{
		ciAgncyIdntyNo = aiAgncyIdntyNo;
	}

	/**
	 * Set the Timestamp for the last change.
	 * 
	 * @param calendar
	 */
	public void setChngTimeStmp(Calendar aaCalendar)
	{
		caChngTimeStmp = aaCalendar;
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
	 * Set the Pilot Indicator.
	 * 
	 * @param abPilotIndi
	 */
	public void setPilotIndi(boolean abPilotIndi)
	{
		cbPilotIndi = abPilotIndi;
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

	/**
	 * Set the Updating Agent Identity Number
	 * 
	 * @param ciUpdtngAgntIdntyNo the ciUpdtngAgntIdntyNo to set
	 */
	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)
	{
		this.ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;
	}
}
