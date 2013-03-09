package com.txdot.isd.rts.webservices.bat.data;

import java.util.Calendar;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;

/*
 * RtsBatchListRequest.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	11/22/2010	Initial load.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	01/04/2011	Add the required fields to make this work.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	01/10/2011	Re-flow required fields to match up to 
 * 							new processing requirements.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	03/28/2011	Remove Plate Number.
 * 							delete csSearchPlateNo
 * 							delete getSearchPlateNo(), setSearchPlateNo()
 * 							defect 10673 Ver 6.7.1
 * Ray Rowehl	03/29/2011	Add Office Issuance Number as a selection 
 * 							criteria.
 * 							add csOfcIssuanceNo
 * 							add getOfcIssuanceNo(), setOfcIssuanceNo()
 * 							defect 10673 Ver 6.7.1
 * ---------------------------------------------------------------------
 */

/**
 * Initial Batch List Request for WebSub.
 *
 * @version	6.7.1			03/29/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		11/22/2010 10:38:38
 */

public class RtsBatchListRequest extends RtsAbstractRequest
{
	
	/**
	 * End Date when doing a restricted Batch List.
	 */
	private Calendar caSearchEndDate;
	
	/**
	 * Start Date when doing a restricted Batch List.
	 */
	private Calendar caSearchStartDate;
	
	/**
	 * Used when processing a Batch.
	 */
	private int ciAgencyBatchIdntyNo;
	
	/**
	 * Agency to pull a Batch list for.
	 */
	private int ciAgencyIdntyNo;
	
	/**
	 * Used as an input to search.
	 */
	private int ciOfcIssuanceNo;
	
	/**
	 * Used when Approving a Batch from the County.
	 */
	private int ciTransWsId;
	
	/**
	 * Batch Status Code when doing a restricted Batch List.
	 */
	private String csSearchBatchStatusCd;
	
	/**
	 * Get the Indentity Number of the Batch to work on.
	 * 
	 * @return int
	 */
	public int getAgencyBatchIdntyNo()
	{
		return ciAgencyBatchIdntyNo;
	}
	
	/**
	 * Get the Agency Identity Number to retrieve batches for.
	 * 
	 * @return int
	 */
	public int getAgencyIdntyNo()
	{
		return ciAgencyIdntyNo;
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
	 * Get the Search Batch Status Code.
	 * 
	 * @return String
	 */
	public String getSearchBatchStatusCd()
	{
		return csSearchBatchStatusCd;
	}

	/**
	 * Get the Search End Date.
	 * 
	 * @return Calendar
	 */
	public Calendar getSearchEndDate()
	{
		return caSearchEndDate;
	}

	/**
	 * Get the Search Start Date.
	 * 
	 * @return Calendar
	 */
	public Calendar getSearchStartDate()
	{
		return caSearchStartDate;
	}

	/**
	 * Get the Transaction Work Station Id.
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Set the Indentity Number of the Batch to work on.
	 * 
	 * @param aiAgencyBatchIdntyNo
	 */
	public void setAgencyBatchIdntyNo(int aiAgencyBatchIdntyNo)
	{
		ciAgencyBatchIdntyNo = aiAgencyBatchIdntyNo;
	}

	/**
	 * Set the Agency Identity Number to retrieve batches for.
	 * 
	 * @param aiAgencyIdntyNo
	 */
	public void setAgencyIdntyNo(int aiAgencyIdntyNo)
	{
		ciAgencyIdntyNo = aiAgencyIdntyNo;
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
	 * Set the Search Batch Status Code.
	 * 
	 * @param asSearchBatchStatusCd
	 */
	public void setSearchBatchStatusCd(String asSearchBatchStatusCd)
	{
		csSearchBatchStatusCd = asSearchBatchStatusCd;
	}

	/**
	 * Set the End Search Date.
	 * 
	 * @param aaSearchEndDate
	 */
	public void setSearchEndDate(Calendar aaSearchEndDate)
	{
		caSearchEndDate = aaSearchEndDate;
	}

	/**
	 * Set the Search Start Date.
	 * 
	 * @param aaSearchStartDate
	 */
	public void setSearchStartDate(Calendar aaSearchStartDate)
	{
		caSearchStartDate = aaSearchStartDate;
	}

	/**
	 * Set the Transaction Work Station Id for County Processing.
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

}
