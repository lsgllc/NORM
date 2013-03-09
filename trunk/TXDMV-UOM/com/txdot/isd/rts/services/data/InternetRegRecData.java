package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * InternetRegRecData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford 	09/03/2002	DB down, added address change indicator 
 * 							defect 3700
 * K Harrell	10/25/2005	Java 1.4 Cleanup 
 * 							defect 7889 Ver 5.2.3   
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * InternetRegRecData
 * 
 * @version 5.2.3		10/25/2005 
 * @author 	Clifford Chen
 * <br>Creation Date:	10/04/2001 16:14:22
 */

public class InternetRegRecData implements Serializable
{
	// boolean
	private boolean cbAddressChanged;

	// int
	private int ciOfcIssuanceNo;
	private int ciSendEmail;

	// String 
	private String csEmployeeId;
	private String csReasonCd;
	private String csReasonComments;
	private String csStatus;
	private String csSubstationId;
	private String csTransactionId;

	// Object 
	private CompleteRegRenData caCompleteRegRenData;
	private RTSDate caCountyProcessedDt;

	private final static long serialVersionUID = 3791376133469743804L;
	/**
	 * InternetRegRecData constructor comment.
	 */
	public InternetRegRecData()
	{
		super();

		csStatus = null;
		csReasonCd = null;
		csReasonComments = null;
		ciSendEmail = 0;
		csEmployeeId = null;
		csSubstationId = null;
		caCountyProcessedDt = null;
		csTransactionId = null;
		cbAddressChanged = false;
		caCompleteRegRenData = new CompleteRegRenData();
	}
	/**
	 * Return value of CompleteRegRenData
	 * 
	 * @return CompleteRegRenData
	 */
	public CompleteRegRenData getCompleteRegRenData()
	{
		return caCompleteRegRenData;
	}
	/**
	 * Return value of CountyProcessedDt
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getCountyProcessedDt()
	{
		return caCountyProcessedDt;
	}
	/**
	 * Return value of EmployeeId
	 *  
	 * @return String
	 */
	public String getEmployeeId()
	{
		return csEmployeeId;
	}
	/**
	 * Return value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}
	/**
	 * Return value of ReasonCd
	 * 
	 * @return String
	 */
	public String getReasonCd()
	{
		return csReasonCd;
	}
	/**
	 * Return value of ReasonComments
	 *  
	 * @return String
	 */
	public String getReasonComments()
	{
		return csReasonComments;
	}
	/**
	 * Return value of Status
	 *  
	 * @return String
	 */
	public String getStatus()
	{
		return csStatus;
	}
	/**
	 * Return value of SubstationId
	 *  
	 * @return String
	 */
	public String getSubstationId()
	{
		return csSubstationId;
	}
	/**
	 * Return value of Transactionid
	 *  
	 * @return String
	 */
	public String getTransactionId()
	{
		return csTransactionId;
	}
	/**
	 * Return value of AddressChanged
	 *  
	 * @return boolean 
	 */
	public boolean isAddressChanged()
	{
		return cbAddressChanged;
	}
	/**
	 * Return value of SendEmail
	 * 
	 * @return int 
	 */
	public int getSendEmail()
	{
		return ciSendEmail;
	}
	/**
	 * Set value of AddressChanged
	 * 
	 * @param abAddressChanged boolean 
	 */
	public void setAddressChanged(boolean abAddressChanged)
	{
		cbAddressChanged = abAddressChanged;
	}
	/**
	 * Set value of CompleteRegRenData
	 * 
	 * @param aaCompleteRegRenData CompleteRegRenData
	 */
	public void setCompleteRegRenData(CompleteRegRenData aaCompleteRegRenData)
	{
		caCompleteRegRenData = aaCompleteRegRenData;
	}
	/**
	 * Set value of CountyProcessedDt
	 * 
	 * @param aaCountyProcessedDt RTSDate
	 */
	public void setCountyProcessedDt(RTSDate aaCountyprocessDt)
	{
		caCountyProcessedDt = aaCountyprocessDt;
	}
	/**
	 * Set value of EmployeeId
	 * 
	 * @param asEmployeeId String
	 */
	public void setEmployeeId(String asEmployeeId)
	{
		csEmployeeId = asEmployeeId;
	}
	/**
	 * Set value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Set value of ReasonCd 
	 * 
	 * @param asReasonCd String
	 */
	public void setReasonCd(String asReasonCd)
	{
		csReasonCd = asReasonCd;
	}
	/**
	 * Set value of ReasonComments
	 * 
	 * @param asReasonComments String
	 */
	public void setReasonComments(String asReasonComments)
	{
		csReasonComments = asReasonComments;
	}
	/**
	 * Set value of SendEmail
	 * 
	 * @param aiSendEmail int
	 */
	public void setSendEmail(int aiSendEmail)
	{
		ciSendEmail = aiSendEmail;
	}
	/**
	 * Set value of Status
	 * 
	 * @param asStatus String
	 */
	public void setStatus(String asStatus)
	{
		csStatus = asStatus;
	}
	/**
	 * Set value of SubstationId
	 * 
	 * @param asSubstationId String
	 */
	public void setSubstationId(String asSubstationId)
	{
		csSubstationId = asSubstationId;
	}
	/**
	 * Set value of TransactionId 
	 *  
	 * @param asTransactionId String
	 */
	public void setTransactionId(String asTransactionId)
	{
		csTransactionId = asTransactionId;
	}
}
