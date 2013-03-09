package com.txdot.isd.rts.webservices.agnt.data;

import java.util.Calendar;

/*
 * RtsWebAgntSecurity.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/11/2011	Initial load.
 * 							defect 10670 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Defines the security settings for an Agent within an Agency
 *
 * @version	6.7.0			01/11/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		01/11/2011 14:34:17
 */

public class RtsWebAgntSecurity
{
	private Calendar caChngTimeStmp;
	private boolean cbAgncyAuthAccs;
	private boolean cbAgncyInfoAccs;
	private boolean cbAgntAuthAccs;
	private boolean cbAprvBatchAccs;
	private boolean cbBatchAccs;
	private boolean cbDashAccs;
	private boolean cbRenwlAccs;
	private boolean cbRePrntAccs;
	private boolean cbRptAccs;
	private boolean cbSubmitBatchAccs;
	private boolean cbVoidAccs;
	private int ciAgncyIdntyNo;
	private int ciAgntIdntyNo;
	private int ciAgntSecrtyIdntyNo;

	/**
	 * Get the Identitiy Number for the Agency.
	 * 
	 * @return int
	 */
	public int getAgncyIdntyNo()
	{
		return ciAgncyIdntyNo;
	}

	/**
	 * Get the Identity Number for the Agent.
	 * 
	 * @return int
	 */
	public int getAgntIdntyNo()
	{
		return ciAgntIdntyNo;
	}

	/**
	 * Get the Identity Number for Agent Security.
	 * 
	 * @return int
	 */
	public int getAgntSecrtyIdntyNo()
	{
		return ciAgntSecrtyIdntyNo;
	}
	
	/**
	 * Get the timestamp from the Last Change.
	 * 
	 * @return Calendar
	 */
	public Calendar getChngTimeStmp()
	{
		return caChngTimeStmp;
	}

	/**
	 * Get the indicator for Agency Authorization Access.
	 * 
	 * @return boolean
	 */
	public boolean isAgncyAuthAccs()
	{
		return cbAgncyAuthAccs;
	}

	/**
	 * Get the indicator for Agency Information Access.
	 * 
	 * @return boolean
	 */
	public boolean isAgncyInfoAccs()
	{
		return cbAgncyInfoAccs;
	}

	/**
	 * Get the indicator for using Agent Authority.
	 * 
	 * @return boolean
	 */
	public boolean isAgntAuthAccs()
	{
		return cbAgntAuthAccs;
	}

	/**
	 * Get the indicator for Batch Approval.
	 * 
	 * @return boolean
	 */
	public boolean isAprvBatchAccs()
	{
		return cbAprvBatchAccs;
	}

	/**
	 * Get the indicator for Batch Access.
	 * 
	 * @return boolean
	 */
	public boolean isBatchAccs()
	{
		return cbBatchAccs;
	}

	/**
	 * Get the indicator for DashBoard Access.
	 * 
	 * @return boolean
	 */
	public boolean isDashAccs()
	{
		return cbDashAccs;
	}

	/**
	 * Get the indicator for Renewal.
	 * 
	 * @return boolean
	 */
	public boolean isRenwlAccs()
	{
		return cbRenwlAccs;
	}

	/**
	 * Get the inidicator for Reprint Access.
	 * 
	 * @return boolean
	 */
	public boolean isRePrntAccs()
	{
		return cbRePrntAccs;
	}

	/**
	 * Get the indicator for Report Access.
	 * 
	 * @return boolean
	 */
	public boolean isRptAccs()
	{
		return cbRptAccs;
	}

	/**
	 * Get the indicator for Submitting Batches.
	 * 
	 * @return boolean
	 */
	public boolean isSubmitBatchAccs()
	{
		return cbSubmitBatchAccs;
	}

	/**
	 * Get the indicator for using Void.
	 * 
	 * @return boolean
	 */
	public boolean isVoidAccs()
	{
		return cbVoidAccs;
	}

	/**
	 * Set the Identity Number for Agent Security.
	 * 
	 * @param aiAgntSecrtyIdntyNo
	 */
	public void setAgentSecurityIdntyNo(int aiAgntSecrtyIdntyNo)
	{
		ciAgntSecrtyIdntyNo = aiAgntSecrtyIdntyNo;
	}

	/**
	 * Set the indicator for Agency Authorization Access.
	 * 
	 * @param abAgncyAuthAccs
	 */
	public void setAgncyAuthAccs(boolean abAgncyAuthAccs)
	{
		cbAgncyAuthAccs = abAgncyAuthAccs;
	}

	/**
	 * Set the Identitiy Number for the Agency.
	 * 
	 * @param aiAgncyIdntyNo
	 */
	public void setAgncyIdntyNo(int aiAgncyIdntyNo)
	{
		ciAgncyIdntyNo = aiAgncyIdntyNo;
	}

	/**
	 * Set the indicator for Agency Information Access.
	 * 
	 * @param abAgncyInfoAccs
	 */
	public void setAgncyInfoAccs(boolean abAgncyInfoAccs)
	{
		cbAgncyInfoAccs = abAgncyInfoAccs;
	}

	/**
	 * Set the indicator for using Agent Authority.
	 * 
	 * @param abAgntAuthAccs
	 */
	public void setAgntAuthAccs(boolean abAgntAuthAccs)
	{
		cbAgntAuthAccs = abAgntAuthAccs;
	}

	/**
	 * Set the Identity Number for the Agent.
	 * 
	 * @param aiAgntIdntyNo
	 */
	public void setAgntIdntyNo(int aiAgntIdntyNo)
	{
		ciAgntIdntyNo = aiAgntIdntyNo;
	}

	/**
	 * Set the indicator for Batch Approval.
	 * 
	 * @param abAprvBatchAccs
	 */
	public void setAprvBatchAccs(boolean abAprvBatchAccs)
	{
		cbAprvBatchAccs = abAprvBatchAccs;
	}

	/**
	 * Set the indicator for Batch Access.
	 * 
	 * @param abBatchAccs
	 */
	public void setBatchAccs(boolean abBatchAccs)
	{
		cbBatchAccs = abBatchAccs;
	}

	/**
	 * Set the timestamp from the Last Change.
	 * 
	 * @param aaChngTimeStmp
	 */
	public void setChngTimeStmp(Calendar aaChngTimeStmp)
	{
		caChngTimeStmp = aaChngTimeStmp;
	}

	/**
	 * Set the indicator for DashBoard Access.
	 * 
	 * @param abDashAccs
	 */
	public void setDashAccs(boolean abDashAccs)
	{
		cbDashAccs = abDashAccs;
	}

	/**
	 * Set the indicator for Renewal.
	 * 
	 * @param abRenwlAccs
	 */
	public void setRenwlAccs(boolean abRenwlAccs)
	{
		cbRenwlAccs = abRenwlAccs;
	}

	/**
	 * Set the inidicator for Reprint Access.
	 * 
	 * @param abRePrntAccs
	 */
	public void setRePrntAccs(boolean abRePrntAccs)
	{
		cbRePrntAccs = abRePrntAccs;
	}

	/**
	 * Set the indicator for Report Access.
	 * 
	 * @param abRptAccs
	 */
	public void setRptAccs(boolean abRptAccs)
	{
		cbRptAccs = abRptAccs;
	}

	/**
	 * Set the indicator for Submitting Batches.
	 * 
	 * @param abSubmitBatchAccs
	 */
	public void setSubmitBatchAccs(boolean abSubmitBatchAccs)
	{
		cbSubmitBatchAccs = abSubmitBatchAccs;
	}

	/**
	 * Set the indicator for using Void.
	 * 
	 * @param abVoidAccs
	 */
	public void setVoidAccs(boolean abVoidAccs)
	{
		cbVoidAccs = abVoidAccs;
	}

}
