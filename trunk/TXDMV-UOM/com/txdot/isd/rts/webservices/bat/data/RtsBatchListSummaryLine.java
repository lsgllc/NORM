package com.txdot.isd.rts.webservices.bat.data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.txdot.isd.rts.services.data.WebAgencyBatchData;
import com.txdot.isd.rts.services.data.WebAgentSecurityData;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 * RtsBatchListSummaryLine.java
 *
 * (c) Texas Department of Motor Vehicles 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	11/22/2010	Initial load.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	01/04/2011	Add the needed fields.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	01/10/2011	Re-flow after definition of what is needed.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	01/27/2011	Add the booleans indicating what access the
 * 							logged in user has on the batch.
 * 							Also added some logic for breaking out the 
 * 							date data in a more useful format for Web 
 * 							Agent.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	02/09/2011	Add Reprint Count.
 * 							defect 10673 Ver 6.7.0
 * Ray Rowehl	03/28/2011	Rename a few fields per requirements.
 * 							add ciSubmitAgntSecrtyIdntyNo,
 * 								ciReprintCnt
 * 							delete ciAgntSecrtyIdntyNo,
 * 								ciReprintCount, ciNumberOfDays,
 * 								cbBatchMaxDaysMet, cbBatchMaxRequestsMet
 * 							add getSubmitAgntSecrtyIdntyNo, 
 * 								setSubmitAgntSecrtyIdntyNo()
 * 							delete getAgntSecrtyIdntyNo(), 
 * 								setAgntSecrtyIdntyNo(), 
 * 								getReprintCount(), setReprintCount(),
 * 								getBatchCompltDateString(),
 * 								getBatchInitDateString(),
 * 								getNumberOfDays(), setNumberOfDays(),
 * 								isBatchMaxDaysMet(), 
 * 								isBatchMaxRequestsMet(),
 * 								setBatchMaxDaysMet(), 
 * 								setBatchMaxRequestsMet()
 * 							modify RtsBatchListSummaryLine()
 * 							defect 10673 Ver 6.7.1
 * K Harrell	04/27/2011	modify constructor to use WebAgnecyBatchData. 
 * 							getSubmitAgntSecrtyIdntyNo() 
 * 							modify RtsBatchListSummaryLine(WebAgencyBatchData)
 * 							defect 10785 Ver 6.7.1
 * K Harrell	04/28/2011	add getBatchStatusDesc() 
 * 							defect 10785 Ver 6.7.1  
 * K McKee  	11/10/2011  Added null setter for WSDL generation
 * 							setBatchComptDate()
 * 							setBatchComptTime()
 * 							setBatchInitDate()
 * 							setBatchInitTime()
 * 							rename setAgencyIdntyNo() to setAgncyIdntyNo() to match getter
 * 							defect 10729 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * Batch Summary data line.
 *
 * @version	6.9.0			11/10/2011
 * @author	Ray Rowehl
 * <br>Creation Date:		11/22/2010 10:44:11
 */

public class RtsBatchListSummaryLine
{
	/**
	 * When the batch closed.
	 */
	private Calendar caBatchCompltTimeStamp;

	/**
	 * When the batch started.
	 */
	private Calendar caBatchInitTimeStamp;
	private boolean cbApproveAccs;

	private boolean cbSubmitAccs;

	/**
	 * Dollar Amount in this Batch.
	 */
	private double cdTotalDollars;

	/**
	 * Batch Identity Number
	 */
	private int ciAgncyBatchIdntyNo;

	/**
	 * Agency Identity Number for this Batch.
	 */
	private int ciAgncyIdntyNo;

	/**
	 * Agent Security Identity Number who Submitted this Batch.
	 */
	private int ciSubmitAgntSecrtyIdntyNo;

	/**
	 * Office Issuance Number on this Batch.
	 */
	private int ciOfcIssuanceNo;

	/**
	 * Count of number of Renewal Requests in this batch.
	 */
	private int ciRenewalRequestCnt;
	private int ciReprintCnt;

	/**
	 * Total Number of Voids in this Batch.
	 */
	private int ciVoidCnt;

	/**
	 * Agency Name 1 for this Batch.
	 */
	private String csAgncyName1;

	/**
	 * The Agent User Name on this Batch.
	 */
	private String csAgntUserName;

	/**
	 * The Status Code of this Batch.
	 */
	private String csBatchStatusCd;

	/**
	 * Office Name to display.
	 */
	private String csOfcName;

	/**
	 * RtsBatchListSummaryLine.java Constructor
	 */
	public RtsBatchListSummaryLine()
	{
		super();
	}

	/**
	 * RtsBatchListSummaryLine.java Constructor
	 */
	public RtsBatchListSummaryLine(
		WebAgencyBatchData aaWABD,
		WebAgentSecurityData aaWASD)
	{
		super();
		setAgncyBatchIdntyNo(aaWABD.getAgncyBatchIdntyNo());
		setAgncyIdntyNo(aaWABD.getAgncyIdntyNo());
		setOfcIssuanceNo(aaWABD.getOfcIssuanceNo());
		setSubmitAgntSecrtyIdntyNo(aaWABD.getSubmitAgntSecrtyIdntyNo());
		setBatchStatusCd(aaWABD.getBatchStatusCd());
		setBatchInitTimeStamp(
			aaWABD.getBatchInitTimestmp().getCalendar());
		if (aaWABD.getBatchCompleteTimestmp() != null)
		{
			setBatchCompltTimeStamp(
				aaWABD.getBatchCompleteTimestmp().getCalendar());
		}
		setSubmitAccs(
			aaWASD.getSubmitBatchAccs() == 1
				&& aaWABD.isAvailableForSubmit()
				&& aaWABD.getAgncyIdntyNo() == aaWASD.getAgncyIdntyNo());

		setApproveAccs(
			aaWASD.getAprvBatchAccs() == 1
				&& aaWABD.isAvailableForApprove());
	}

	/**
	 * Get the Identity Key for the Batch.
	 * 
	 * @return int
	 */
	public int getAgncyBatchIdntyNo()
	{
		return ciAgncyBatchIdntyNo;
	}

	/**
	 * Get the Agncy Identity Number.
	 * 
	 * @return int
	 */
	public int getAgncyIdntyNo()
	{
		return ciAgncyIdntyNo;
	}

	/**
	 * Get the Agency Name 1 for this Batch.
	 * 
	 * @return String
	 */
	public String getAgncyName1()
	{
		return csAgncyName1;
	}

	/**
	 * Get the Agent Security Identity Number that took Submitted this 
	 * Batch.
	 * 
	 * @return int
	 */
	public int getSubmitAgntSecrtyIdntyNo()
	{
		return ciSubmitAgntSecrtyIdntyNo;
	}

	/**
	 * Get UserName of the User Acting on the Batch.
	 * 
	 * @return String
	 */
	public String getAgntUserName()
	{
		return csAgntUserName;
	}

	/**
	 * Get the Batch Complete Date.
	 * 
	 * @return Date
	 */
	public Date getBatchCompltDate()
	{
		if (caBatchCompltTimeStamp != null)
		{
			return caBatchCompltTimeStamp.getTime();
		}
		else
		{
			return caBatchInitTimeStamp.getTime();
		}
	}
	
	/**
	 * Null Setter for WSDL generation
	 * @param aaBatchCompltDate
	 */	
	public void setBatchCompltDate(Date aaBatchCompltDate)
	{
		// null setter
	}

	/**
	 * Get the Batch Complete Time.
	 * 
	 * @return String
	 */
	public String getBatchCompltTime()
	{
		SimpleDateFormat laTimeFormat =
			new SimpleDateFormat("hh:mm:ss");
		if (caBatchCompltTimeStamp != null)
		{
			return laTimeFormat.format(
				caBatchCompltTimeStamp.getTime());
		}
		else
		{
			return "";
		}
	}
	/**
	 * Null Setter for WSDL generation
	 * @param aaBatchCompltTime
	 */
	public void setBatchCompltTime(String aaBatchCompltTime)
	{
		// null setter
	}
	/**
	 * Get the Batch Complete Time Stamp.
	 * 
	 * @return Calendar
	 */
	public Calendar getBatchCompltTimeStamp()
	{
		return caBatchCompltTimeStamp;
	}

	/**
	 * Get the Batch Initiation Date.
	 * 
	 * @return Date
	 */
	public Date getBatchInitDate()
	{
		return caBatchInitTimeStamp.getTime();
	}
	
	/**
	 * Null Setter for WSDL generation
	 * @param aaBatchInitDate
	 */
	public void setBatchInitDate(Date aaBatchInitDate)
	{
		// null setter
	}
	
	/**
	 * Get the Batch Initiation Time.
	 * 
	 * @return String
	 */
	public String getBatchInitTime()
	{
		SimpleDateFormat laTimeFormat =
			new SimpleDateFormat("hh:mm:ss");
		return laTimeFormat.format(caBatchInitTimeStamp.getTime());
	}
	
	/**
	 * Null Setter for WSDL generation
	 * @param aaBatchInitTime
	 */
	public void setBatchInitTime(String aaBatchInitTime)
	{
		// null setter
	}
	
	/**
	 * Get the Batch Initiation Time Stamp.
	 * 
	 * @return Calendar
	 */
	public Calendar getBatchInitTimeStamp()
	{
		return caBatchInitTimeStamp;
	}

	/**
	 * Get the Batch Status Code.
	 * 
	 * @return String
	 */
	public String getBatchStatusCd()
	{
		return csBatchStatusCd;
	}

	/**
	 * Get the Batch Status Code Description
	 * 
	 * @return String
	 */
	public String getBatchStatusDesc()
	{
		String lsBatchStatusDesc = "";

		if (csBatchStatusCd != null)
		{
			if (csBatchStatusCd
				.equals(RegistrationConstant.OPEN_BATCHSTATUSCD))
			{
				lsBatchStatusDesc = "Open";
			}
			else if (
				csBatchStatusCd.equals(
					RegistrationConstant.CLOSE_BATCHSTATUSCD))
			{
				lsBatchStatusDesc = "Closed";
			}
			else if (
				csBatchStatusCd.equals(
					RegistrationConstant.INPROCESS_BATCHSTATUSCD))
			{
				lsBatchStatusDesc = "In Process";
			}
			else if (
				csBatchStatusCd.equals(
					RegistrationConstant.SUBMIT_BATCHSTATUSCD))
			{
				lsBatchStatusDesc = "Submitted";
			}
			else if (
				csBatchStatusCd.equals(
					RegistrationConstant.APPROVED_BATCHSTATUSCD))
			{
				lsBatchStatusDesc = "Approved";
			}
		}
		return lsBatchStatusDesc;
	}
	
	/**
	 * Null Setter for WSDL generation
	 * @param aaBatchStatusDesc
	 */
	public void setBatchStatusDesc(String aaBatchStatusDesc)
	{
		// null setter
	}
	
	/**
	 * Get the Office Issuance Number for the batch.
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Get the Office Name to display for Batch.
	 * 
	 * @return String
	 */
	public String getOfcName()
	{
		return csOfcName;
	}

	/**
	 * Get Renewal Request Count for this Batch.
	 * 
	 * @return int
	 */
	public int getRenewalRequestCnt()
	{
		return ciRenewalRequestCnt;
	}

	/**
	 * Get the Reprint Count.
	 * 
	 * @return int
	 */
	public int getReprintCnt()
	{
		return ciReprintCnt;
	}

	/**
	 * Get the Total Dollar Amount for this Batch.
	 * 
	 * @return double
	 */
	public double getTotalDollars()
	{
		return cdTotalDollars;
	}

	/**
	 * Get the Void Count for this Batch.
	 * 
	 * @return int
	 */
	public int getVoidCnt()
	{
		return ciVoidCnt;
	}

	/**
	 * Get the Approve Access for the user on this batch.
	 * 
	 * @return boolean
	 */
	public boolean isApproveAccs()
	{
		return cbApproveAccs;
	}

	/**
	 * Get the Submit Access for the user on this batch.
	 * 
	 * @return boolean
	 */
	public boolean isSubmitAccs()
	{
		return cbSubmitAccs;
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
	 * Set the Identity Key for the Batch.
	 * 
	 * @param aiAgncyBatchIdntyNo
	 */
	public void setAgncyBatchIdntyNo(int aiAgncyBatchIdntyNo)
	{
		ciAgncyBatchIdntyNo = aiAgncyBatchIdntyNo;
	}

	/**
	 * Set the Agency Name 1 for this Batch.
	 * 
	 * @param asAgncyName1
	 */
	public void setAgncyName1(String asAgncyName1)
	{
		csAgncyName1 = asAgncyName1;
	}

	/**
	 * Set the Agent Security Identity Number that took action on the 
	 * Batch.
	 * 
	 * @param aiSubmitAgntSecrtyIdntyNo
	 */
	public void setSubmitAgntSecrtyIdntyNo(int aiSubmitAgntSecrtyIdntyNo)
	{
		ciSubmitAgntSecrtyIdntyNo = aiSubmitAgntSecrtyIdntyNo;
	}

	/**
	 * Set UserName of the User Acting on the Batch.
	 * 
	 * @param asAgntUserName
	 */
	public void setAgntUserName(String asAgntUserName)
	{
		csAgntUserName = asAgntUserName;
	}

	/**
	 * Set the Approve Access for the user on this batch.
	 * 
	 * @param abApproveAccs
	 */
	public void setApproveAccs(boolean abApproveAccs)
	{
		cbApproveAccs = abApproveAccs;
	}

	/**
	 * Set the Batch Complete Time Stamp.
	 * 
	 * @param aaBatchCompltTimeStamp
	 */
	public void setBatchCompltTimeStamp(Calendar aaBatchCompltTimeStamp)
	{
		caBatchCompltTimeStamp = aaBatchCompltTimeStamp;
	}

	/**
	 * Set the Batch Initiation Time Stamp.
	 * 
	 * @param aaBatchInitTimeStamp
	 */
	public void setBatchInitTimeStamp(Calendar aaBatchInitTimeStamp)
	{
		caBatchInitTimeStamp = aaBatchInitTimeStamp;
	}

	/**
	 * Set the Batch Status Code.
	 * 
	 * @param asBatchStatusCd
	 */
	public void setBatchStatusCd(String asBatchStatusCd)
	{
		csBatchStatusCd = asBatchStatusCd;
	}

	/**
	 * Set the Office Issuance Number for the Batch.
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set the Office Name to display for Batch.
	 * 
	 * @param asOfcName
	 */
	public void setOfcName(String asOfcName)
	{
		csOfcName = asOfcName;
	}

	/**
	 * Set Renewal Request Count for this Batch.
	 * 
	 * @param aiRenewalRequestCnt
	 */
	public void setRenewalRequestCnt(int aiRenewalRequestCnt)
	{
		ciRenewalRequestCnt = aiRenewalRequestCnt;
	}

	/**
	 * Set the Reprint Count.
	 * 
	 * @param aiReprintCnt
	 */
	public void setReprintCnt(int aiReprintCnt)
	{
		ciReprintCnt = aiReprintCnt;
	}

	/**
	 * Set the Submit Access for the user on this batch.
	 * 
	 * @param abSubmitAccs
	 */
	public void setSubmitAccs(boolean abSubmitAccs)
	{
		cbSubmitAccs = abSubmitAccs;
	}

	/**
	 * Set the Total Dollar Amount for this Batch.
	 * 
	 * @param adTotalDollars
	 */
	public void setTotalDollars(double adTotalDollars)
	{
		cdTotalDollars = adTotalDollars;
	}

	/**
	 * Set the Void Count for this Batch.
	 * 
	 * @param aiVoidCnt
	 */
	public void setVoidCnt(int aiVoidCnt)
	{
		ciVoidCnt = aiVoidCnt;
	}

}
