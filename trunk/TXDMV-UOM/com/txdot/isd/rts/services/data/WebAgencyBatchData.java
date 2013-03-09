package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

/*
 * WebAgencyBatchData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708  Ver 6.7.0
 * K Harrell	01/05/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0
 * K Harrell	01/21/2011	add ciCustSeqNo, ciTransAMDate, 
 * 							 ciTransWsId, csTransEmpId, 
 * 							 get/set methods
 * 							defect 10708 Ver 6.7.0     
 * K Harrell	03/18/2011	add isOpen()
 * 							defect 10768 Ver 6.7.1   
 * K Harrell	03/21/2011	add caBatchCloseTimestmp, caBatchApprvTimestmp, 
 * 							caBatchSubmitTimestmp, get/set methods 
 * K Harrell	03/28/2011	add SubmitAgntSecrtyIdntyNo, get/set methods
 * 							delete AgntSecrtyIdntyNo, get/set methods
 * 							defect 10785 Ver 6.7.1 
 * K Harrell	03/29/2011	add caSearchStartDate, caSearchEndDate, 
 * 							 get/set methods 
 * 							defect 10785 Ver 6.7.1 
 * K Harrell	04/05/2011	implement constants 
 * 							defect 10785 Ver 6.7.1
 * K Harrell	05/05/2011	modify isAvailableForApprove() 
 * 							defect 10785 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgencyBatchData. 
 *
 * @version	6.7.1			05/05/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:33:17
 */
public class WebAgencyBatchData implements Serializable
{
	private int ciAgncyBatchIdntyNo;
	private int ciAgncyIdntyNo;
	private int ciCustSeqNo;
	private int ciOfcIssuanceNo;
	private int ciSubmitAgntSecrtyIdntyNo;
	private int ciTransAMDate;
	private int ciTransWsId;

	private String csBatchStatusCd;
	private String csTransEmpId;

	private RTSDate caBatchApprvTimestmp;
	private RTSDate caBatchCloseTimestmp;
	private RTSDate caBatchCompleteTimestmp;
	private RTSDate caBatchInitTimestmp;
	private RTSDate caBatchSubmitTimestmp;
	private RTSDate caSearchEndDate;
	private RTSDate caSearchStartDate;

	static final long serialVersionUID = -4955311374412132689L;

	/**
	 * WebAgencyBatchData.java Constructor
	 * 
	 */
	public WebAgencyBatchData()
	{
		super();
	}

	/**
	 * Get value of ciAgncyBatchIdntyNo
	 * 
	 * @return int
	 */
	public int getAgncyBatchIdntyNo()
	{
		return ciAgncyBatchIdntyNo;
	}

	/**
	 * Get value of ciAgncyIdntyNo
	 * 
	 * @return int
	 */
	public int getAgncyIdntyNo()
	{
		return ciAgncyIdntyNo;
	}

	/**
	 * Set value of caBatchApprvTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBatchApprvTimestmp()
	{
		return caBatchApprvTimestmp;
	}

	/**
	 * Get value of caBatchCloseTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBatchCloseTimestmp()
	{
		return caBatchCloseTimestmp;
	}

	/**
	 * Get value of caBatchCompleteTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBatchCompleteTimestmp()
	{
		return caBatchCompleteTimestmp;
	}

	/**
	 * Get value of caBatchInitTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBatchInitTimestmp()
	{
		return caBatchInitTimestmp;
	}

	/**
	 * Get value of csBatchStatusCd
	 * 
	 * @return String
	 */
	public String getBatchStatusCd()
	{
		return csBatchStatusCd;
	}

	/**
	 * Get value of caBatchSubmitTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getBatchSubmitTimestmp()
	{
		return caBatchSubmitTimestmp;
	}

	/**
	 * Get value of ciCustSeqNo
	 * 
	 * @return int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * Get value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Get value of caSearchEndDate
	 * 
	 * @return RTSDate
	 */
	public RTSDate getSearchEndDate()
	{
		return caSearchEndDate;
	}

	/**
	 * Get value of caSearchStartDate
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getSearchStartDate()
	{
		return caSearchStartDate;
	}

	/**
	 * Get value of ciSubmitAgntSecrtyIdntyNo
	 * 
	 * @return int
	 */
	public int getSubmitAgntSecrtyIdntyNo()
	{
		return ciSubmitAgntSecrtyIdntyNo;
	}

	/**
	 * Get value of ciTransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Get value of 
	 * 
	 * @return
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}

	/**
	 * Get value of ciTransWsId
	 * 
	 * @return int 
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Return boolean to denote if availabe for Submit 
	 * 
	 * @return boolean
	 */
	public boolean isAvailableForSubmit()
	{
		return getBatchStatusCd() != null
			&& (getBatchStatusCd()
				.trim()
				.equals(RegistrationConstant.OPEN_BATCHSTATUSCD)
				|| getBatchStatusCd().trim().equals(
					RegistrationConstant.CLOSE_BATCHSTATUSCD));
	}

	/**
	 * Return boolean to denote if availabe for Submit 
	 * 
	 * @return boolean
	 */
	public boolean isAvailableForApprove()
	{
		return getBatchStatusCd() != null
			&& (getBatchStatusCd()
				.trim()
				.equals(RegistrationConstant.SUBMIT_BATCHSTATUSCD)
				|| (getBatchStatusCd()
					.trim()
					.equals(
						RegistrationConstant.INPROCESS_BATCHSTATUSCD)
					&& getTransAMDate() == new RTSDate().getAMDate()));
	}

	/**
	 * 
	 * Return boolean to denote if valid for Transaction Processing
	 * 
	 * @return boolean
	 */
	public boolean isValidForTransProcessing()
	{
		return getBatchStatusCd() != null
			&& getBatchStatusCd().trim().equals(
				RegistrationConstant.INPROCESS_BATCHSTATUSCD)
			&& !UtilityMethods.isEmpty(getTransEmpId())
			&& getTransAMDate() == new RTSDate().getAMDate();
	}

	/**
	 * Set value of ciAgncyBatchIdntyNo
	 * 
	 * @param aiAgncyBatchIdntyNo
	 */
	public void setAgncyBatchIdntyNo(int aiAgncyBatchIdntyNo)
	{
		ciAgncyBatchIdntyNo = aiAgncyBatchIdntyNo;
	}

	/**
	 * Set value of ciAgncyIdntyNo
	 * 
	 * @param aiAgncyIdntyNo
	 */
	public void setAgncyIdntyNo(int aiAgncyIdntyNo)
	{
		ciAgncyIdntyNo = aiAgncyIdntyNo;
	}

	/**
	 * Set value of caBatchApprvTimestmp
	 * 
	 * @param aaBatchApprvTimestmp
	 */
	public void setBatchApprvTimestmp(RTSDate aaBatchApprvTimestmp)
	{
		caBatchApprvTimestmp = aaBatchApprvTimestmp;
	}

	/**
	 * Set value of caBatchCloseTimestmp
	 * 
	 * @param aaBatchCloseTimestmp
	 */
	public void setBatchCloseTimestmp(RTSDate aaBatchCloseTimestmp)
	{
		caBatchCloseTimestmp = aaBatchCloseTimestmp;
	}

	/**
	 * Set value of caBatchCompleteTimestmp
	 * 
	 * @param aaBatchCompleteTimestmp
	 */
	public void setBatchCompleteTimestmp(RTSDate aaBatchCompleteTimestmp)
	{
		caBatchCompleteTimestmp = aaBatchCompleteTimestmp;
	}

	/**
	 * Set value of caBatchInitTimestmp
	 * 
	 * @param aaBatchInitTimestmp
	 */
	public void setBatchInitTimestmp(RTSDate aaBatchInitTimestmp)
	{
		caBatchInitTimestmp = aaBatchInitTimestmp;
	}

	/**
	 * Set value of csBatchStatusCd
	 * 
	 * @param asBatchStatusCd
	 */
	public void setBatchStatusCd(String asBatchStatusCd)
	{
		csBatchStatusCd = asBatchStatusCd;
	}

	/**
	 * Set value of caBatchSubmitTimestmp
	 * 
	 * @param aaBatchSubmitTimestmp
	 */
	public void setBatchSubmitTimestmp(RTSDate aaBatchSubmitTimestmp)
	{
		caBatchSubmitTimestmp = aaBatchSubmitTimestmp;
	}

	/**
	 * Set value of ciCustSeqNo
	 * 
	 * @param aiCustSeqNo
	 */
	public void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * Set value of caSearchEndDate
	 * 
	 * @param aaSearchEndDate
	 */
	public void setSearchEndDate(RTSDate aaSearchEndDate)
	{
		caSearchEndDate = aaSearchEndDate;
	}

	/**
	 * Set value of caSearchStartDate
	 * 
	 * @param aaSearchStartDate
	 */
	public void setSearchStartDate(RTSDate aaSearchStartDate)
	{
		caSearchStartDate = aaSearchStartDate;
	}

	/**
	 * Set value of ciSubmitAgntSecrtyIdntyNo
	 * 
	 * @param aiSubmitAgntSecrtyIdntyNo
	 */
	public void setSubmitAgntSecrtyIdntyNo(int aiSubmitAgntSecrtyIdntyNo)
	{
		ciSubmitAgntSecrtyIdntyNo = aiSubmitAgntSecrtyIdntyNo;
	}

	/**
	 * Set value of ciTransAMDate
	 * 
	 * @param aiTransAMDate
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * Set value of csTransEmpId
	 * 
	 * @param asTransEmpId
	 */
	public void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}

	/**
	 * Set value of ciTransWsId
	 * 
	 * @param aiTransWsId
	 */
	public void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

}
