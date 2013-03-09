package com.txdot.isd.rts.webservices.bat.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractRequest;

/*
 * RtsBatchDetailRequest.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/18/2011	Initial load.
 * 							defect 10673 Ver 6.7.0
 * K McKee		01/09/2012  add csRegPltNo and gets/sets
 * 							add ciOfcIssuanceNo and gets/sets
 * 							defect 11239 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Request for Batch Details for Web Agent.
 *
 * @version	6.10.0			01/09/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		01/18/2011 14:05:24
 */

public class RtsBatchDetailRequest extends RtsAbstractRequest
{
	private int ciAgencyBatchIdntyNo;
	private int ciAgencyIdntyNo;
	private int ciTransWsId;
	private int ciSavReqId;
	private String csRegPltNo = "";
	private int ciOfcIssuanceNo;
	
	/**
	 * Set the Batch Identity Number.
	 * 
	 * @return int
	 */
	public int getAgencyBatchIdntyNo()
	{
		return ciAgencyBatchIdntyNo;
	}

	/**
	 * Get the Agency Identity Number.
	 * 
	 * @return int
	 */
	public int getAgencyIdntyNo()
	{
		return ciAgencyIdntyNo;
	}

	/**
	 * Get the SavReqId identifying the detail record.
	 * 
	 * @return int
	 */
	public int getSavReqId()
	{
		return ciSavReqId;
	}

	/**
	 * Get the Transaction Workstation Id to be used for County Void.
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}
	
	/**
	 * Get the registered plate number
	 * 
	 * @return the csRegPltNo
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * @return the ciOfcIssuanceNo
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * @param ciOfcIssuanceNo the ciOfcIssuanceNo to set
	 */
	public void setOfcIssuanceNo(int ciOfcIssuanceNo)
	{
		this.ciOfcIssuanceNo = ciOfcIssuanceNo;
	}

	/**
	 * Set the Batch Identity Number.
	 * 
	 * @param aiAgencyBatchIdntyNo
	 */
	public void setAgencyBatchIdntyNo(int aiAgencyBatchIdntyNo)
	{
		ciAgencyBatchIdntyNo = aiAgencyBatchIdntyNo;
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
	 * Set the SavReqId identifying the detail record.
	 * 
	 * @param aiSavReqId
	 */
	public void setSavReqId(int aiSavReqId)
	{
		ciSavReqId = aiSavReqId;
	}

	/**
	 * Set the Transaction Workstation Id to be used for County Void.
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * Set the registered plate number
	 * 
	 * @param csRegPltNo the csRegPltNo to set
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		this.csRegPltNo = asRegPltNo;
	}

}
