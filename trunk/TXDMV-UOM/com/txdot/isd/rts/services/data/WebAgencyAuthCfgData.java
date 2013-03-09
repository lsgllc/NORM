package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 * WebAgencyAuthCfgData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0 
 * K Harrell	01/05/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0 
 * K Harrell	01/10/2011	add PilotIndi, get/set methods 
 * 							defect 10708 Ver 6.7.0
 * K Harrell	03/18/2011	add ciAgncyIdntyNo, ciOfcIssuanceNo, get/set 
 * 							methods
 * 							defect 10768 Ver 6.7.1  
 * K Harrell	03/22/2011	add csSubconId, get/set methods
 * 							defect 10768 Ver 6.7.1
 * K McKee      11/04/2011  add csOfcName, get/set methods
 * 							defect 11151 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgencyAuthCfgData 
 *
 * @version	6.9.0			11/04/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:33:17
 */
public class WebAgencyAuthCfgData implements Serializable
{
	private int ciAgncyAuthIdntyNo;
	private int ciAgncyIdntyNo; 
	private int ciDeleteIndi; 
	private int ciExpProcsngMos;
	private int ciIssueInvIndi;
	private int ciMaxSubmitCount;
	private int ciMaxSubmitDays;
	private int ciOfcIssuanceNo; 
	private int ciPilotIndi;
	private int ciSubconId; 
	private int ciUpdtngAgntIdntyNo;
	
	private String csExpProcsngCd;
	private String csKeyEntryCd;
	private String csOfcName;
	
	private RTSDate caChngTimestmp;
	
	static final long serialVersionUID = 4628761879574095103L;
	
	/**
	 * WebAgencyAuthCfgData.java Constructor
	 * 
	 */
	public WebAgencyAuthCfgData()
	{
		super();
	}

	/**
	 * Get value of ciAgncyAuthIdntyNo
	 * 
	 * @return int
	 */
	public int getAgncyAuthIdntyNo()
	{
		return ciAgncyAuthIdntyNo;
	}

	/**
	 * Get value of ciAgncyIdntyNo
	 * 
	 * @return int 
	 */
	public int getAgncyIdntyNo()
	{
		return ciAgncyIdntyNo;
	}

	/**
	 * Get value of caChngTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}
	/**
	 * Get value of ciDeleteIndi
	 * 
	 * @return int 
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}

	/**
	 * Get value of csExpProcsngCd
	 * 
	 * @return String
	 */
	public String getExpProcsngCd()
	{
		return csExpProcsngCd;
	}

	/**
	 * Get value of ciExpProcsngMos
	 * 
	 * @return  int
	 */
	public int getExpProcsngMos()
	{
		return ciExpProcsngMos;
	}

	/**
	 * Get value of ciIssueInvIndi
	 * 
	 * @return  int
	 */
	public int getIssueInvIndi()
	{
		return ciIssueInvIndi;
	}

	/**
	 * Get value of csKeyEntryCd
	 * 
	 * @return String 
	 */
	public String getKeyEntryCd()
	{
		return csKeyEntryCd;
	}

	/**
	 * Get value of ciMaxSubmitCount
	 * 
	 * @return  int
	 */
	public int getMaxSubmitCount()
	{
		return ciMaxSubmitCount;
	}

	/**
	 * Get value of ciMaxSubmitDays
	 * 
	 * @return  int
	 */
	public int getMaxSubmitDays()
	{
		return ciMaxSubmitDays;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @return int 
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
 
	/**
	 * @return the csOfcName
	 */
	public String getOfcName()
	{
		return csOfcName;
	}
 
	/**
	 * Get value of ciPilotIndi
	 * 
	 * @return int
	 */
	public int getPilotIndi()
	{
		return ciPilotIndi;
	}

	/**
	 * Get value of SubconId
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}
	/**
	 * @return the ciUpdtngAgntIdntyNo
	 */
	public int getUpdtngAgntIdntyNo()
	{
		return ciUpdtngAgntIdntyNo;
	}
	/**
	 * Set value of ciAgncyAuthIdntyNo
	 * 
	 * @param aiAgncyAuthIdntyNo
	 */
	public void setAgncyAuthIdntyNo(int aiAgncyAuthIdntyNo)
	{
		ciAgncyAuthIdntyNo = aiAgncyAuthIdntyNo;
	}

	/**
	 * Set value of ciAgncyIdntyNo
	 * 
	 * @param aiAgncyIdntyNo
	 */
	public void setAgncyIdntyNo(int aiAgncyIdntyNo)
	{
		ciAgncyIdntyNo = aiAgncyIdntyNo;
	}

	/**
	 * Set value of caChngTimestmp
	 * 
	 * @param aaChngTimestmp
	 */
	public void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}

	/**
	 * Set value of ciDeleteIndi
	 * 
	 * @param aiDeleteIndi
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}

	/**
	 * Set value of csExpProcsngCd
	 * 
	 * @param asExpProcsngCd
	 */
	public void setExpProcsngCd(String asExpProcsngCd)
	{
		csExpProcsngCd = asExpProcsngCd;
	}

	/**
	 * Set value of ciExpProcsngMos
	 * 
	 * @param aiExpProcsngMos
	 */
	public void setExpProcsngMos(int aiExpProcsngMos)
	{
		ciExpProcsngMos = aiExpProcsngMos;
	}

	/**
	 * Set value of ciIssueInvIndi
	 * 
	 * @param aiIssueInvIndi
	 */
	public void setIssueInvIndi(int aiIssueInvIndi)
	{
		ciIssueInvIndi = aiIssueInvIndi;
	}

	/**
	 * Set value of csKeyEntryCd
	 * 
	 * @param asKeyEntryCd
	 */
	public void setKeyEntryCd(String asKeyEntryCd)
	{
		csKeyEntryCd = asKeyEntryCd;
	}

	/**
	 * Set value of ciMaxSubmitCount
	 * 
	 * @param aiMaxSubmitCount
	 */
	public void setMaxSubmitCount(int aiMaxSubmitCount)
	{
		ciMaxSubmitCount = aiMaxSubmitCount;
	}

	/**
	 * Set value of ciMaxSubmitDays
	 * 
	 * @param aiMaxSubmitDays
	 */
	public void setMaxSubmitDays(int aiMaxSubmitDays)
	{
		ciMaxSubmitDays = aiMaxSubmitDays;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * @param asOfcName the csOfcName to set
	 */
	public void setOfcName(String asOfcName)
	{
		this.csOfcName = asOfcName;
	}
	
	/**
	 * Set value of ciPilotIndi
	 * 
	 * @param aiPilotIndi
	 */
	public void setPilotIndi(int aiPilotIndi)
	{
		ciPilotIndi = aiPilotIndi;
	}

	/**
	 * Set value of SubconId 
	 * 
	 * @param aiSubconId
	 */
	public void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}

	/**
	 * @param ciUpdtngAgntIdntyNo the ciUpdtngAgntIdntyNo to set
	 */
	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)
	{
		this.ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;
	}

	
}
