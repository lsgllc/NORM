package com.txdot.isd.rts.webservices.agnt.data;

import java.util.Calendar;
import java.util.Date;

import com.txdot.isd.rts.services.data.WebAgentSecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.webservices.common.data.RtsNameAddress;

/*
 * RtsWebAgntSecurityWS.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	06/15/2011	Initial load.
 * 							defect 10718 Ver 6.8.0
 * K McKee      09/01/2011  setUpdtngAgntIdntyNo
 * 							modify RtsWebAgntSecurityWS(WebAgentSecurityData aaObject)
 * 							defect 10729 Ver 6.8.1
 * K McKee      11/03/2011  added caAgncyNameAddress - getter/setters
 * 							modify RtsWebAgntSecurityWS(WebAgentSecurityData aaObject)
 * 							defect 11146 Ver 6.9.0
 * D Hamilton   11/17/2011  replace NameAddressData with RtsNameAddress 
 * 							defect 11146 Ver 6.9.0
 * R Pilon		02/02/2012	Changed method setChngTimeStmpDate() to accept a
 * 							  Date object instead of a Calendar object to 
 * 							  prevent web service validation error.
 * 							delete setChngTimeStmpDate(Calendar aaChngTimeStmp)
 * 							add setChngTimeStmpDate(Date aaChngTimeStmpDate)
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Agent Security object for web services.
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		06/15/2011 11:11:55
 */

public class RtsWebAgntSecurityWS
{
	private Calendar caChngTimeStmp;
	
	private boolean cbAgncyAuthAccs;
	private boolean cbAgncyInfoAccs;
	private boolean cbAgntAuthAccs;
	private boolean cbAprvBatchAccs;
	private boolean cbBatchAccs;
	private boolean cbDataChanged;
	private boolean cbRenwlAccs;
	private boolean cbRePrntAccs;
	private boolean cbRptAccs;
	private boolean cbSubmitBatchAccs;
	private boolean cbVoidAccs;
	
	private int ciAgncyIdntyNo;
	private int ciAgntIdntyNo;
	private int ciAgntSecrtyIdntyNo;
	private int ciUpdtngAgntIdntyNo;

	// defect 11146
	private RtsNameAddress caAgncyNameAddress;
	// end defect 11146

	/**
	 * RtsWebAgntSecurityWS.java Constructor
	 */
	public RtsWebAgntSecurityWS()
	{
		super();
	}
	
	/**
	 * RtsWebAgntSecurityWS.java Constructor
	 */
	public RtsWebAgntSecurityWS(WebAgentSecurityData aaObject)
	{
		super();
		setChngTimeStmp(aaObject.getChngTimestmp().getCalendar());
		setAgncyAuthAccs(aaObject.getAgncyAuthAccs() == 1);
		setAgncyInfoAccs(aaObject.getAgncyInfoAccs() == 1);
		setAgntAuthAccs(aaObject.getAgntAuthAccs() == 1);
		setAprvBatchAccs(aaObject.getAprvBatchAccs() == 1);
		setBatchAccs(aaObject.getBatchAccs() == 1);
		setDataChanged(false);
		setRenwlAccs(aaObject.getRenwlAccs() == 1);
		setRePrntAccs(aaObject.getReprntAccs() == 1);
		setRptAccs(aaObject.getRptAccs() == 1);
		setSubmitBatchAccs(aaObject.getSubmitBatchAccs() == 1);
		setVoidAccs(aaObject.getVoidAccs() == 1);
		setAgncyIdntyNo(aaObject.getAgncyIdntyNo());
		setAgntIdntyNo(aaObject.getAgntIdntyNo());
		setAgntSecrtyIdntyNo(aaObject.getAgntSecrtyIdntyNo());
		setUpdtngAgntIdntyNo(aaObject.getUpdtngAgntIdntyNo());

		//		 defect 11146 begin
		setAgncyNameAddress(new RtsNameAddress(aaObject.getAgncyNameAddress()));

		// defect 11146 end
	}

	/**
	 * Get the Agency Identity Number.
	 * 
	 * @return int
	 */
	public int getAgncyIdntyNo()
	{
		return ciAgncyIdntyNo;
	}
	
	/**
	 * Get the Agency name and location
	 * 
	 * @return the caAgncyNameAddress
	 */
	public RtsNameAddress getAgncyNameAddress()
	{
		if (caAgncyNameAddress == null)
		{
			caAgncyNameAddress = new RtsNameAddress();
		}
		return caAgncyNameAddress;
	}
	
	/**
	 * Get the Agent Identity Number.
	 * 
	 * @return int
	 */
	public int getAgntIdntyNo()
	{
		return ciAgntIdntyNo;
	}

	/**
	 * Get the Agent Security Identity Number.
	 * 
	 * @return int
	 */
	public int getAgntSecrtyIdntyNo()
	{
		return ciAgntSecrtyIdntyNo;
	}
	
	/**
	 * Get the updating agent identity no
	 * @return the ciUpdtngAgntIdntyNo
	 */
	public int getUpdtngAgntIdntyNo()
	{
		return ciUpdtngAgntIdntyNo;
	}
	
	
	/**
	 * Get the Last Change Time Stamp
	 * 
	 * @return Calendar
	 */
	public Calendar getChngTimeStmp()
	{
		return caChngTimeStmp;
	}
	
	/**
	 * Get the Last Change Date
	 * 
	 * @return Date
	 */
	public Date getChngTimeStmpDate()
	{
		if (caChngTimeStmp != null)
		{
			return caChngTimeStmp.getTime();
		}
		else 
		{
			return Calendar.getInstance().getTime();
		}
	}
	
	/**
	 * Get the Agency Authority Access.
	 * 
	 * @return boolean
	 */
	public boolean isAgncyAuthAccs()
	{
		return cbAgncyAuthAccs;
	}

	/**
	 * Get the Agency Information Access.
	 * 
	 * @return boolean
	 */
	public boolean isAgncyInfoAccs()
	{
		return cbAgncyInfoAccs;
	}

	/**
	 * Get the Agent Authority Access.
	 * 
	 * @return boolean
	 */
	public boolean isAgntAuthAccs()
	{
		return cbAgntAuthAccs;
	}

	/**
	 * Get the Approve Batch Access.
	 * 
	 * @return boolean
	 */
	public boolean isAprvBatchAccs()
	{
		return cbAprvBatchAccs;
	}

	/**
	 * Get the Batch Access.  Can view a batch.
	 * 
	 * @return boolean
	 */
	public boolean isBatchAccs()
	{
		return cbBatchAccs;
	}

	/**
	 * Has the data been changed.
	 * 
	 * @return boolean
	 */
	public boolean isDataChanged()
	{
		return cbDataChanged;
	}

	/**
	 * Get the Renewal Access.
	 * 
	 * @return boolean
	 */
	public boolean isRenwlAccs()
	{
		return cbRenwlAccs;
	}

	/**
	 * Get the Reprint Access.
	 * 
	 * @return boolean
	 */
	public boolean isRePrntAccs()
	{
		return cbRePrntAccs;
	}

	/**
	 * Get Report Access.
	 * 
	 * @return boolean
	 */
	public boolean isRptAccs()
	{
		return cbRptAccs;
	}

	/**
	 * Get the Submit Batch Access.
	 * 
	 * @return boolean
	 */
	public boolean isSubmitBatchAccs()
	{
		return cbSubmitBatchAccs;
	}

	/**
	 * Get the Void Access.
	 * 
	 * @return boolean
	 */
	public boolean isVoidAccs()
	{
		return cbVoidAccs;
	}

	/**
	 * Set the Agency Authority Access.
	 * 
	 * @param abAgncyAuthAccs
	 */
	public void setAgncyAuthAccs(boolean abAgncyAuthAccs)
	{
		cbAgncyAuthAccs = abAgncyAuthAccs;
	}

	/**
	 * Set the agency name and location
	 * 
	 * @param aaAgncyNameAddress the AgncyNameAddress to set
	 */
	public void setAgncyNameAddress(RtsNameAddress aaAgncyNameAddress)
	{
		this.caAgncyNameAddress = aaAgncyNameAddress;
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
	 * Set the Agency Information Access.
	 * 
	 * @param abAgncyInfoAccs
	 */
	public void setAgncyInfoAccs(boolean abAgncyInfoAccs)
	{
		cbAgncyInfoAccs = abAgncyInfoAccs;
	}

	/**
	 * Set the Agent Authority Access.
	 * 
	 * @param abAgntAuthAccs
	 */
	public void setAgntAuthAccs(boolean abAgntAuthAccs)
	{
		cbAgntAuthAccs = abAgntAuthAccs;
	}

	/**
	 * Set the Agent Identity Number.
	 * 
	 * @param aiAgntIdntyNo
	 */
	public void setAgntIdntyNo(int aiAgntIdntyNo)
	{
		ciAgntIdntyNo = aiAgntIdntyNo;
	}

	/**
	 * Set the Agent Security Identity Number.
	 * 
	 * @param aiAgntSecrtyIdntyNo
	 */
	public void setAgntSecrtyIdntyNo(int aiAgntSecrtyIdntyNo)
	{
		ciAgntSecrtyIdntyNo = aiAgntSecrtyIdntyNo;
	}

	/**
	 * Set the Approve Batch Access.
	 * 
	 * @param abAprvBatchAccs
	 */
	public void setAprvBatchAccs(boolean abAprvBatchAccs)
	{
		cbAprvBatchAccs = abAprvBatchAccs;
	}

	/**
	 * Set the Batch Access.  Can view a batch.
	 * 
	 * @param abBatchAccs
	 */
	public void setBatchAccs(boolean abBatchAccs)
	{
		cbBatchAccs = abBatchAccs;
	}

	/**
	 * Get the TimeStamp of the last change.
	 * 
	 * @param aaChngTimeStmp
	 */
	public void setChngTimeStmp(Calendar aaChngTimeStmp)
	{
		caChngTimeStmp = aaChngTimeStmp;
	}
	
	/**
	 * Set the Date of the last change.
	 * 
	 * @param aaChngTimeStmpDate
	 */
	public void setChngTimeStmpDate(Date aaChngTimeStmpDate)
	{
		if (aaChngTimeStmpDate != null)
		{
			caChngTimeStmp.setTime(aaChngTimeStmpDate);
		}
	}

	/**
	 * Set Data Changed indicator.
	 * 
	 * @param abDataChanged
	 */
	public void setDataChanged(boolean abDataChanged)
	{
		cbDataChanged = abDataChanged;
	}

	/**
	 * Set the Renewal Access.
	 * 
	 * @param abRenwlAccs
	 */
	public void setRenwlAccs(boolean abRenwlAccs)
	{
		cbRenwlAccs = abRenwlAccs;
	}

	/**
	 * Set the Reprint Access.
	 * 
	 * @param abRePrntAccs
	 */
	public void setRePrntAccs(boolean abRePrntAccs)
	{
		cbRePrntAccs = abRePrntAccs;
	}

	/**
	 * Set Report Access.
	 * 
	 * @param abRptAccs
	 */
	public void setRptAccs(boolean abRptAccs)
	{
		cbRptAccs = abRptAccs;
	}

	/**
	 * Set the Submit Batch Access.
	 * 
	 * @param abSubmitBatchAccs
	 */
	public void setSubmitBatchAccs(boolean abSubmitBatchAccs)
	{
		cbSubmitBatchAccs = abSubmitBatchAccs;
	}


	/**
	 * Set the Void Access.
	 * 
	 * @param abVoidAccs
	 */
	public void setVoidAccs(boolean abVoidAccs)
	{
		cbVoidAccs = abVoidAccs;
	}

	/**
	 * Set the agent identity no of the updating agent.
	 * 
	 * @param aiUpdtngAgntIdntyNo the UpdtngAgntIdntyNo to set
	 */
	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)
	{
		this.ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;
	}
	
	/**
	 * Make sure the meets at least basic edit checks.
	 * 
	 * @throws RTSException
	 */
	public void validateWebAgntWS() throws RTSException
	{
		RTSException leRTSEx = null;
		// if there is an exception, throw it.
		if (leRTSEx != null)
		{
			throw leRTSEx;
		}
	}

}
