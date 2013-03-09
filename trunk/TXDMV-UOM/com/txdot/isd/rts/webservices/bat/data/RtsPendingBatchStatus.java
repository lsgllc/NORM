package com.txdot.isd.rts.webservices.bat.data;
/*
 * RtsPendingBatchStatus.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/11/2011	Initial load.
 * 							defect 10670 Ver 670
 * ---------------------------------------------------------------------
 */

/**
 * The shows the Pending Batch Status for a selected Agency Auth.
 *
 * @version	6.7.0			01/11/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/11/2011 12:59:11
 */

public class RtsPendingBatchStatus
{
	private boolean cbBatchMaxDaysMet;
	private boolean cbBatchMaxRequestsMet;
	private int ciAgncyBatchIndtyNo;
	private int ciAgncyIndntyNo;
	private int ciNumberOfDays;
	private int ciNumberOfRequests;
	private int ciOfcIssuanceNo;
	private String csOfcName;

	/**
	 * Get the Batch Identity Number.
	 * 
	 * @return int
	 */
	public int getAgncyBatchIndtyNo()
	{
		return ciAgncyBatchIndtyNo;
	}

	/**
	 * Get the Agency Identity Number.
	 * 
	 * @return int
	 */
	public int getAgncyIndntyNo()
	{
		return ciAgncyIndntyNo;
	}

	/**
	 * Get the Number of Days in this Batch.
	 * 
	 * @return int
	 */
	public int getNumberOfDays()
	{
		return ciNumberOfDays;
	}

	/**
	 * Get the Number of Requests in this Batch.
	 * 
	 * @return int
	 */
	public int getNumberOfRequests()
	{
		return ciNumberOfRequests;
	}

	/**
	 * Get the Office Issuance Number.
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
	 * Get the Batch Max Days Met Indi.
	 * 
	 * @return boolean
	 */
	public boolean isBatchMaxDaysMet()
	{
		return cbBatchMaxDaysMet;
	}

	/**
	 * Get the Batch Max Requests Met Indi.
	 * 
	 * @return boolean
	 */
	public boolean isBatchMaxRequestsMet()
	{
		return cbBatchMaxRequestsMet;
	}

	/**
	 * Set the Batch Identity Number.
	 * 
	 * @param aiAgncyBatchIndtyNo
	 */
	public void setAgncyBatchIndtyNo(int aiAgncyBatchIndtyNo)
	{
		ciAgncyBatchIndtyNo = aiAgncyBatchIndtyNo;
	}

	/**
	 * Set the Agency Identity Number.
	 * 
	 * @param aiAgncyIndntyNo
	 */
	public void setAgncyIndntyNo(int aiAgncyIndntyNo)
	{
		ciAgncyIndntyNo = aiAgncyIndntyNo;
	}

	/**
	 * Set the Batch Max Days Met Indi.
	 * 
	 * @param abBatchMaxDaysMet
	 */
	public void setBatchMaxDaysMet(boolean abBatchMaxDaysMet)
	{
		cbBatchMaxDaysMet = abBatchMaxDaysMet;
	}

	/**
	 * Set the Batch Max Requests Met Indi.
	 * 
	 * @param abBatchMaxRequestsMet
	 */
	public void setBatchMaxRequestsMet(boolean abBatchMaxRequestsMet)
	{
		cbBatchMaxRequestsMet = abBatchMaxRequestsMet;
	}

	/**
	 * Set the Number of Days in this Batch.
	 * 
	 * @param aiNumberOfDays
	 */
	public void setNumberOfDays(int aiNumberOfDays)
	{
		ciNumberOfDays = aiNumberOfDays;
	}

	/**
	 * Set the Number of Requests in this Batch.
	 * 
	 * @param aiNumberOfRequests
	 */
	public void setNumberOfRequests(int aiNumberOfRequests)
	{
		ciNumberOfRequests = aiNumberOfRequests;
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

}
