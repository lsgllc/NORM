package com.txdot.isd.rts.webservices.agnt.data;

import java.util.Calendar;
import java.util.Date;

import com.txdot.isd.rts.services.exception.RTSException;

/*
 * RtsWebAgntWS.java
 *
 * (c) Texas Department of Motor Vehicles 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	01/28/2011	Initial load.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/02/2011	Add check DMV Indi.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	02/03/2011	Fold in RtsWebAgnt definition.
 * 							It is not translating into web service 
 * 							definition from just extending for some 
 * 							reason.
 * 							defect 10670 Ver 6.7.0
 * Ray Rowehl	04/28/2011	Rename setEmpPhone to setPhone.
 * 							add setPhone()
 * 							delete setEmpPhone()
 * 							defect 10718 Ver 6.7.1
 * Ray Rowehl	06/15/2011	Add RtsWebAgntSecurityWS array.
 * 							add carrAgntSecurity, cbDataChanged
 * 							add getAgntSecurity(), setAgntSecurity(),
 * 								isDataChanged(), setDataChanged()
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	06/20/2011	Remove all the fields that went to 
 * 							RtsWebAgntSecurityWS.
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	06/20/2011	Bring back some of the critical getters for 
 * 							WebAgent login function.
 * 							defect 10718 Ver 6.8.0
 * Ray Rowehl	06/30/2011	Add a creater for a WebAgentData object.
 * 							add getWebAgentData()
 * 							defect 10718 Ver 6.8.0
 * K McKEE      09/01/2011  Added setUpdtngAgntIdntyNo
 * 							modify getWebAgentData()
 * 							defect 10729 Ver 6.8.1
 * R Pilon		02/02/2012	Changed method setChngTimeStmpDate() to accept a
 * 							  Date object instead of a Calendar object to 
 * 							  prevent web service validation error.
 * 							delete setChngTimeStmpDate(Calendar aaChngTimeStmp)
 * 							add setChngTimeStmpDate(Date aaChngTimeStmpDate)
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Combined Agent and Security View for web services.
 *
 * @version	6.10.0			02/02/2012
 * @author	Ray Rowehl
 * <br>Creation Date:		01/28/2011 19:07:11
 */

public class RtsWebAgntWS
{
	private Calendar caChngTimeStmp;
	private RtsWebAgntSecurityWS[] carrAgntSecurity;
	private boolean cbCheckDMVIndi;
	private boolean cbDataChanged;
	private boolean cbDmvUserIndi;
	private int ciAgntIdntyNo;
	private int ciInitOfcNo;
	private String csEMail;
	private String csFstName;
	private String csLstName;
	private String csMiName;
	private String csPhone;
	private String csUserName;
	private int ciUpdtngAgntIdntyNo;

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
	 * Return the Agent Security Identity Number.
	 * 
	 * @return int
	 */
	public int getAgntSecrtyIdntyNo()
	{
		int liIdntyNo = 0;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			liIdntyNo = getAgntSecurity()[0].getAgntSecrtyIdntyNo();
		}
		return liIdntyNo;
	}

	/**
	 * Get the Agent Security array.
	 * 
	 * @return RtsWebAgntSecurityWS[]
	 */
	public RtsWebAgntSecurityWS[] getAgntSecurity()
	{
		return carrAgntSecurity;
	}

	/**
	 * Get an element of Agent Security array.
	 * 
	 * @param i element of the carrAgntSecurity array to get
	 * @return RtsWebAgntSecurityWS
	 */
	public RtsWebAgntSecurityWS getAgntSecurity(int i)
	{
		return carrAgntSecurity[i];
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
	 * Get the Agent's EMail Address.
	 * 
	 * @return String
	 */
	public String getEMail()
	{
		return csEMail;
	}

	/**
	 * Get the Agent's First Name.
	 *  
	 * @return String
	 */
	public String getFstName()
	{
		return csFstName;
	}

	/**
	 * Get the Agent's Initial Office Number
	 * 
	 * @return int
	 */
	public int getInitOfcNo()
	{
		return ciInitOfcNo;
	}

	/**
	 * Get the Agent's Last Name
	 * 
	 * @return String
	 */
	public String getLstName()
	{
		return csLstName;
	}

	/**
	 * Get the Agent's Middle Initial.
	 * 
	 * @return String
	 */
	public String getMiName()
	{
		return csMiName;
	}

	/**
	 * Get the Agent's Phone Number.
	 * 
	 * @return String
	 */
	public String getPhone()
	{
		return csPhone;
	}

	/**
	 * Get the Agent's User Name.
	 * 
	 * @return String
	 */
	public String getUserName()
	{
		return csUserName;
	}
	
	/**
	 * Return Agency Auth Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isAgncyAuthAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isAgncyAuthAccs();
		}
		return lbAccs;
	}
	
	/**
	 * Return Agency Info Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isAgncyInfoAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isAgncyInfoAccs();
		}
		return lbAccs;
	}
	
	/**
	 * Return Agent Auth Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isAgntAuthAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isAgntAuthAccs();
		}
		return lbAccs;
	}
	
	/**
	 * Return Approve Batch Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isAprvBatchAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isAprvBatchAccs();
		}
		return lbAccs;
	}
	
	/**
	 * Return Batch Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isBatchAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isBatchAccs();
		}
		return lbAccs;
	}

	/**
	 * Get the Check DMV Indi
	 * 
	 * <p>This indicates if we should use the DMV User Indi 
	 * in SQL.
	 * 
	 * @return boolean
	 */
	public boolean isCheckDMVIndi()
	{
		return cbCheckDMVIndi;
	}

	/**
	 * Has the data changed?
	 * 
	 * @return boolean
	 */
	public boolean isDataChanged()
	{
		return cbDataChanged;
	}
	
	/**
	 * Get DMV User Indi.
	 * 
	 * @return boolean
	 */
	public boolean isDmvUserIndi()
	{
		return cbDmvUserIndi;
	}

	/**
	 * Return Renewal Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isRenwlAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isRenwlAccs();
		}
		return lbAccs;
	}

	/**
	 * Return RePrint Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isRePrntAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isRePrntAccs();
		}
		return lbAccs;
	}
	
	/**
	 * Return Report Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isRptAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isRptAccs();
		}
		return lbAccs;
	}
	
	/**
	 * Return Submit Batch Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isSubmitBatchAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isSubmitBatchAccs();
		}
		return lbAccs;
	}
	
	/**
	 * Return Void Access for logged in Agent.
	 * 
	 * @return boolean
	 */
	public boolean isVoidAccs()
	{
		boolean lbAccs = false;
		
		if (getAgntSecurity() != null
		&& getAgntSecurity().length > 0)
		{
			lbAccs = getAgntSecurity()[0].isVoidAccs();
		}
		return lbAccs;
	}
	
	/**
	 * Dummy setter for AgncyAuthAccs.
	 */
	public void setAgncyAuthAccs(boolean abJunk)
	{
		// dummy setter.  no action
	}
	
	/**
	 * Dummy setter for AgncyInfoAccs.
	 */
	public void setAgncyInfoAccs(boolean abJunk)
	{
		// dummy setter.  no action
	}
	
	/**
	 * Dummy setter for AgntAuthAccs.
	 */
	public void setAgntAuthAccs(boolean abJunk)
	{
		// dummy setter.  no action
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
	 * Dummy set of Agent Security Identity Number.
	 */
	public void setAgntSecrtyIdntyNo(int aiAgntSecrtyIdntyNo)
	{
		// Do not actually do anything!
	}

	/**
	 * Set the Agent Security array.
	 * 
	 * @param aarrAgntSecurity
	 */
	public void setAgntSecurity(RtsWebAgntSecurityWS[] aarrAgntSecurity)
	{
		carrAgntSecurity = aarrAgntSecurity;
	}
	
	/**
	 * Set an element of the Agent Security array.
	 * 
	 * @param i element of the carrAgntSecurity array to set
	 * @param aaAgntSecurity the element to set in the carrAgntSecurity array
	 */
	public void setAgntSecurity(int i, RtsWebAgntSecurityWS aaAgntSecurity)
	{
		carrAgntSecurity[i] = aaAgntSecurity;
	}
	
	/**
	 * Dummy setter for AprvBatchAccs.
	 */
	public void setAprvBatchAccs(boolean abJunk)
	{
		// dummy setter.  no action
	}
	
	/**
	 * Dummy setter for BatchAccs.
	 */
	public void setBatchAccs(boolean abJunk)
	{
		// dummy setter.  no action
	}

	/**
	 * Set the Check DMV Indi
	 * 
	 * <p>This indicates if we should use the DMV User Indi 
	 * in SQL.
	 * 
	 * @param abCheckDMVIndi
	 */
	public void setCheckDMVIndi(boolean abCheckDMVIndi)
	{
		cbCheckDMVIndi = abCheckDMVIndi;
	}

	/**
	 * Set the TimeStamp of the last change.
	 * 
	 * @param aaChngTimeStmp
	 */
	public void setChngTimeStmp(Calendar aaChngTimeStmp)
	{
		caChngTimeStmp = aaChngTimeStmp;
	}
	
	/**
	 * Set the TimeStamp of the last change.
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
	 * Set DMV User Indi.
	 * 
	 * @param abDmvUserIndi
	 */
	public void setDmvUserIndi(boolean abDmvUserIndi)
	{
		cbDmvUserIndi = abDmvUserIndi;
	}

	/**
	 * Set the Agent's EMail Address.
	 * 
	 * @param asEMail
	 */
	public void setEMail(String asEMail)
	{
		csEMail = asEMail;
	}

	/**
	 * Set the Agent's First Name.
	 * 
	 * @param asFstName
	 */
	public void setFstName(String asFstName)
	{
		csFstName = asFstName;
	}

	/**
	 * Set the Agent's Initial Office.
	 * 
	 * @param aiInitOfcNo
	 */
	public void setInitOfcNo(int aiInitOfcNo)
	{
		ciInitOfcNo = aiInitOfcNo;
	}

	/**
	 * Set the Agent's Last Name.
	 * 
	 * @param asLstName
	 */
	public void setLstName(String asLstName)
	{
		csLstName = asLstName;
	}

	/**
	 * Set the Agent's Middle Initial.
	 * 
	 * @param asMiName
	 */
	public void setMiName(String asMiName)
	{
		csMiName = asMiName;
	}

	/**
	 * Set the Agent's Phone Number.
	 * 
	 * @param asPhone
	 */
	public void setPhone(String asPhone)
	{
		csPhone = asPhone;
	}
	
	/**
	 * Dummy setter for RenwlAccs.
	 */
	public void setRenwlAccs(boolean abJunk)
	{
		// dummy setter.  no action
	}
	
	/**
	 * Dummy setter for RePrntAccs.
	 */
	public void setRePrntAccs(boolean abJunk)
	{
		// dummy setter.  no action
	}
	
	/**
	 * Dummy setter for RptAccs.
	 */
	public void setRptAccs(boolean abJunk)
	{
		// dummy setter.  no action
	}
	
	/**
	 * Dummy setter for SubmitBatchAccs.
	 */
	public void setSubmitBatchAccs(boolean abJunk)
	{
		// dummy setter.  no action
	}

	/**
	 * Set the Agent's UserName.
	 * 
	 * @param asUserName
	 */
	public void setUserName(String asUserName)
	{
		csUserName = asUserName;
	}
	
	/**
	 * Dummy setter for VoidAccs.
	 */
	public void setVoidAccs(boolean abJunk)
	{
		// dummy setter.  no action
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
	 * Set the agent identity no of the updating agent.
	 * @param ciUpdtngAgntIdntyNo the ciUpdtngAgntIdntyNo to set
	 */
	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)
	{
		ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;
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
