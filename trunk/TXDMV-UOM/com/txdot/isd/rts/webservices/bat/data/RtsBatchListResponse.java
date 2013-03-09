package com.txdot.isd.rts.webservices.bat.data;

import java.util.Calendar;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;

/*
 * RtsBatchListResponse.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	11/23/2010	Initial load.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	11/29/2010	Add CashWsId, OfcIssuanceCd, and SubstaName.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	01/10/2011	Revamp after re-flow discussion.
 * 							defect 10673 Ver 6.7.0
 * R Pilon		02/02/2012	Add missing setter method to prevent web service 
 * 							  validation error.
 * 							add setTransWsId()
 * 							delete setCashWsId()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Batch List Response for WebSub.
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		11/23/2010 17:27:50
 */

public class RtsBatchListResponse extends RtsAbstractResponse
{
	
	/**
	 * List of Batches with summary information about each one.
	 */
	private RtsBatchListSummaryLine[] carrBatchSummaryLine;
	/**
	 * End Date when doing a restricted Batch List.
	 */
	private Calendar caSearchEndDate;
	
	/**
	 * Start Date when doing a restricted Batch List.
	 */
	private Calendar caSearchStartDate;
	
	/**
	 * Used when Approving a Batch from the County.
	 */
	private int ciTransWsId;
	
	/**
	 * Batch Status Code when doing a restricted Batch List.
	 */
	private String csSearchBatchStatusCd;

	/**
	 * Get the array of Batch Summary Lines.
	 * 
	 * @return RtsBatchListSummaryLine[]
	 */
	public RtsBatchListSummaryLine[] getBatchSummaryLine()
	{
		return carrBatchSummaryLine;
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
	 * Get the Transaction Workstation Id.
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Set the array of Batch Summary Lines.
	 * 
	 * @param aarrBatchSummaryLine
	 */
	public void setBatchSummaryLine(RtsBatchListSummaryLine[] aarrBatchSummaryLine)
	{
		carrBatchSummaryLine = aarrBatchSummaryLine;
	}

	/**
	 * Set the Transaction Workstation Id.
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
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
	 * Set the Search End Date.
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

}
