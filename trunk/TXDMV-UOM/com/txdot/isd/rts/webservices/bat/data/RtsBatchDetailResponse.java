package com.txdot.isd.rts.webservices.bat.data;

import com.txdot.isd.rts.webservices.common.data.RtsAbstractResponse;
import com.txdot.isd.rts.webservices.ren.data.RtsVehicleInfo;

/*
 * RtsBatchDetailResponse.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/18/2011	Initial load.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	02/09/2011	Switch to using RtsVehicleInfo.
 * 							defect 10673 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Response to Batch Details Request for Web Agent.
 *
 * @version	6.7.0			02/09/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/18/2011 15:12:13
 */

public class RtsBatchDetailResponse extends RtsAbstractResponse
{
	private int ciAgencyBatchIdntyNo;
	private int ciOfcIssuanceNo;
	private String csOfcIssuanceName;
	private int ciAgencyIdntyNo;
	private int ciDetailCount;
	private RtsVehicleInfo[] carrBatchDetailLines;
	
	/**
	 * Get the batch detail lines.
	 * 
	 * @return RtsVehicleInfo
	 */
	public RtsVehicleInfo[] getBatchDetailLines()
	{
		return carrBatchDetailLines;
	}

	/**
	 * Get the batch identity number.
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
	 * Get the number of detail records.
	 * 
	 * @return int
	 */
	public int getDetailCount()
	{
		return ciDetailCount;
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
	 * Get the Office Issuance Name.
	 * 
	 * @return String
	 */
	public String getOfcIssuanceName()
	{
		return csOfcIssuanceName;
	}

	/**
	 * Set the Batch Detail Lines.
	 * 
	 * @param aarrRtsRenewalLines
	 */
	public void setBatchDetailLines(RtsVehicleInfo[] aarrRtsRenewalLines)
	{
		carrBatchDetailLines = aarrRtsRenewalLines;
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
	 * Set the Detail Lines Count.
	 * 
	 * @param aiDetailCount
	 */
	public void setDetailCount(int aiDetailCount)
	{
		ciDetailCount = aiDetailCount;
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
	 * @param asOfcIssuanceName
	 */
	public void setOfcIssuanceName(String asOfcIssuanceName)
	{
		csOfcIssuanceName = asOfcIssuanceName;
	}

}
