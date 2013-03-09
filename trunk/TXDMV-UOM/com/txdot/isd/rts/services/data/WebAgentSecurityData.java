package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntSecurityWS;

/*
 * WebAgentSecrty.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/28/2010	Created
 * 							defect 10708 Ver 6.7.0  
 * K Harrell	01/05/2011	Renamings per standards 
 * 							defect 10708 Ver 6.7.0
 * K Harrell	01/10/2011	add ciSubmitBatchAccs, get/set methods
 * 							defect 10708 Ver 6.7.0  
 * K Harrell	03/30/2011	add ciDMVUserIndi, csAgncyTypeCd, 
 * 							 get/is/set methods 
 * 							defect 10785 Ver 6.7.1
 * K Harrell	04/06/2011	delete ciDMVUser, get/set method 
 * 							modify isDMVUser() 
 * 							defect 10785 Ver 6.7.1      
 * Ray Rowehl	05/09/2011	Rename UserAuthAccs to AgntAuthAccs to match
 * 							the database name.
 * 							add ciAgntAuthAccs
 * 							delete ciUserAuthAccs
 * 							add getAgntAuthAccs(), setAgntAuthAccs()
 * 							delete getUserAuthAccs(), setUserAuthAccs()
 * 							defect 10718 Ver 6.7.1
 * K McKee      09/01/2011  added ciUpdtngAgntIdntyNo;
 *                          defect 10729 Ver 6.8.1
 * K McKee      11/03/2011  added caAgncyNameAddress.  getters/setters
 * 							defect 11146 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebAgentSecrtyData 
 * 
 * @version	6.9.0 			11/03/2011	
 * @author	Kathy Harrell
 * <br>Creation Date:		12/28/2010 18:33:17
 */

public class WebAgentSecurityData implements Serializable
{
	private int ciAgncyAuthAccs;
	private int ciAgncyIdntyNo;
	private int ciAgncyInfoAccs;
	private int ciAgntIdntyNo;
	private int ciAgntSecrtyIdntyNo;
	private int ciAprvBatchAccs;
	private int ciBatchAccs;
	private int ciDashAccs;
	private int ciDeleteIndi;
	private int ciRenwlAccs;
	private int ciReprntAccs;
	private int ciRptAccs;
	private int ciSubmitBatchAccs;
	private int ciUpdtngAgntIdntyNo;
	// defect 10718
	//private int ciUserAuthAccs;
	private int ciAgntAuthAccs;
	// end defect 10718
	private int ciVoidAccs;

	private String csAgncyTypeCd;
	
	// defect 11146
	private NameAddressData caAgncyNameAddress;
	// end defect 11146
	private RTSDate caChngTimestmp;
	

	static final long serialVersionUID = -7946194815789046049L;

	/**
	 * WebAgentSecurityData Constructor
	 * 
	 */
	public WebAgentSecurityData()
	{
		super();
	}

	public WebAgentSecurityData(RtsWebAgntSecurityWS aaObject)
	{
		super();
		setAgncyAuthAccs(aaObject.isAgncyAuthAccs() ? 1 : 0);
		setAgncyInfoAccs(aaObject.isAgncyInfoAccs() ? 1 : 0);
		setAgntAuthAccs(aaObject.isAgntAuthAccs() ? 1 : 0);
		setAprvBatchAccs(aaObject.isAprvBatchAccs() ? 1 : 0);
		setBatchAccs(aaObject.isBatchAccs() ? 1 : 0);
		setRenwlAccs(aaObject.isRenwlAccs() ? 1 : 0);
		setReprntAccs(aaObject.isRePrntAccs() ? 1 : 0);
		setRptAccs(aaObject.isRptAccs() ? 1 : 0);
		setSubmitBatchAccs(aaObject.isSubmitBatchAccs() ? 1 : 0);
		setVoidAccs(aaObject.isVoidAccs() ? 1 : 0);
		setAgncyIdntyNo(aaObject.getAgncyIdntyNo());
		setAgntIdntyNo(aaObject.getAgntIdntyNo());
		setAgntSecrtyIdntyNo(aaObject.getAgntSecrtyIdntyNo());
		setUpdtngAgntIdntyNo(aaObject.getUpdtngAgntIdntyNo());
	}

	/**
	 * Get value of ciAgncyAuthAccs
	 * 
	 * @return int
	 */
	public int getAgncyAuthAccs()
	{
		return ciAgncyAuthAccs;
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
	 * Get value of ciAgncyInfoAccs
	 * 
	 * @return int
	 */
	public int getAgncyInfoAccs()
	{
		return ciAgncyInfoAccs;
	}

	/**
	 * Get value of csAgncyTypeCd
	 * 
	 * @return String 
	 */
	public String getAgncyTypeCd()
	{
		return csAgncyTypeCd;
	}

	/**
	 * Get value of ciAgntIdntyNo
	 * 
	 * @return int
	 */
	public int getAgntIdntyNo()
	{
		return ciAgntIdntyNo;
	}

	/**
	 * Get value of ciAgntSecrtyIdntyNo
	 * 
	 * @return int
	 */
	public int getAgntSecrtyIdntyNo()
	{
		return ciAgntSecrtyIdntyNo;
	}

	/**
	 * Get value of ciAprvBatchAccs
	 * 
	 * @return int
	 */
	public int getAprvBatchAccs()
	{
		return ciAprvBatchAccs;
	}

	/**
	 * Get value of ciBatchAccs
	 * 
	 * @return int
	 */
	public int getBatchAccs()
	{
		return ciBatchAccs;
	}

	/**
	 * Get value of caChngTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getChngTimestmp()
	{
		return caChngTimestmp;
	}

	/**
	 * Get value of ciDashAccs
	 * 
	 * @return int
	 */
	public int getDashAccs()
	{
		return ciDashAccs;
	}

	/**
	 * Get value of ciDeleteIndi
	 * 
	 * @return int
	 */
	public int getDeleteIndi()
	{
		return ciDeleteIndi;
	}

	/**
	 * Get value of ciRenwlAccs
	 * 
	 * @return int
	 */
	public int getRenwlAccs()
	{
		return ciRenwlAccs;
	}

	/**
	 * Get value of ciReprntAccs
	 * 
	 * @return int
	 */
	public int getReprntAccs()
	{
		return ciReprntAccs;
	}

	/**
	 * Get value of ciRptAccs
	 * 
	 * @return int
	 */
	public int getRptAccs()
	{
		return ciRptAccs;
	}

	/**
	 * Get value of ciSubmitBatchAccs
	 * 
	 * @return int
	 */
	public int getSubmitBatchAccs()
	{
		return ciSubmitBatchAccs;
	}

	/**
	 * Get value of ciAgntAuthAccs
	 * 
	 * @return int
	 */
	public int getAgntAuthAccs()
	{
		return ciAgntAuthAccs;
	}

	/**
	 * Get value of ciVoidAccs
	 * 
	 * @return int
	 */
	public int getVoidAccs()
	{
		return ciVoidAccs;
	}
	
	/**
	 * Get the value of caAgncyNameAddress
	 * 
	 * @return the caAgncyNameAddress
	 */

	public NameAddressData getAgncyNameAddress()
	{
		if (caAgncyNameAddress == null)
		{
			caAgncyNameAddress = new NameAddressData();
		}
		return caAgncyNameAddress;
	}
	
	/**
	 * Get the updating agent identity no
	 * 
	 * @return the ciUpdtngAgntIdntyNo
	 */
	public int getUpdtngAgntIdntyNo()
	{
		return ciUpdtngAgntIdntyNo;
	}
	
	/**
	 * Return if DMV User
	 * 
	 * @return boolean 
	 */
	public boolean isDMVUser()
	{
		return csAgncyTypeCd != null
			&& (csAgncyTypeCd.equalsIgnoreCase(RegistrationConstant.TXDMVHQ_AGNCYTYPECD)
			|| csAgncyTypeCd.equalsIgnoreCase(RegistrationConstant.REGIONALSC_AGNCYTYPECD) 
			|| csAgncyTypeCd.equalsIgnoreCase(RegistrationConstant.COUNTY_AGNCYTYPECD));
	}

	/**
	 * Set value of ciAgncyAuthAccs
	 * 
	 * @param aiAgncyAuthAccs
	 */
	public void setAgncyAuthAccs(int aiAgncyAuthAccs)
	{
		ciAgncyAuthAccs = aiAgncyAuthAccs;
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
	 * Set value of ciAgncyInfoAccs
	 * 
	 * @param aiAgncyInfoAccs
	 */
	public void setAgncyInfoAccs(int aiAgncyInfoAccs)
	{
		ciAgncyInfoAccs = aiAgncyInfoAccs;
	}

	/**
	 * Set value of csAgncyTypeCd
	 * 
	 * @param asAgncyTypeCd
	 */
	public void setAgncyTypeCd(String asAgncyTypeCd)
	{
		csAgncyTypeCd = asAgncyTypeCd;
	}

	/**
	 * Set value of ciAgntIdntyNo
	 * 
	 * @param aiAgntIdntyNo
	 */
	public void setAgntIdntyNo(int aiAgntIdntyNo)
	{
		ciAgntIdntyNo = aiAgntIdntyNo;
	}

	/**
	 * Set value of ciAgntSecrtyIdntyNo
	 * 
	 * @param aiAgntSecrtyIdntyNo
	 */
	public void setAgntSecrtyIdntyNo(int aiAgntSecrtyIdntyNo)
	{
		ciAgntSecrtyIdntyNo = aiAgntSecrtyIdntyNo;
	}

	/**
	 * Set value of ciAprvBatchAccs
	 * 
	 * @param aiAprvBatchAccs
	 */
	public void setAprvBatchAccs(int aiAprvBatchAccs)
	{
		ciAprvBatchAccs = aiAprvBatchAccs;
	}

	/**
	 * Set value of ciBatchAccs
	 * 
	 * @param aiBatchAccs
	 */
	public void setBatchAccs(int aiBatchAccs)
	{
		ciBatchAccs = aiBatchAccs;
	}

	/**
	 * Set value of caChngTimestmp
	 * 
	 * @param aaChngTimestmp
	 */
	public void setChngTimestmp(RTSDate aaChngTimestmp)
	{
		caChngTimestmp = aaChngTimestmp;
	}

	/**
	 * Set value of ciDashAccs
	 * 
	 * @param aiDashAccs
	 */
	public void setDashAccs(int aiDashAccs)
	{
		ciDashAccs = aiDashAccs;
	}

	/**
	 * Set value of ciDeleteIndi
	 * 
	 * @param aiDeleteIndi
	 */
	public void setDeleteIndi(int aiDeleteIndi)
	{
		ciDeleteIndi = aiDeleteIndi;
	}

	/**
	 * Set value of ciRenwlAccs
	 * 
	 * @param aiRenwlAccs
	 */
	public void setRenwlAccs(int aiRenwlAccs)
	{
		ciRenwlAccs = aiRenwlAccs;
	}

	/**
	 * Set value of ciReprntAccs
	 * 
	 * @param aiReprntAccs
	 */
	public void setReprntAccs(int aiReprntAccs)
	{
		ciReprntAccs = aiReprntAccs;
	}

	/**
	 * Set value of ciRptAccs
	 * 
	 * @param aiRptAccs
	 */
	public void setRptAccs(int aiRptAccs)
	{
		ciRptAccs = aiRptAccs;
	}

	/**
	 * Set value of ciSubmitBatchAccs
	 * 
	 * @param aiSubmitBatchAccs
	 */
	public void setSubmitBatchAccs(int aiSubmitBatchAccs)
	{
		ciSubmitBatchAccs = aiSubmitBatchAccs;
	}

	/**
	 * Set value of ciAgntAuthAccs
	 * 
	 * @param aiAgntAuthAccs
	 */
	public void setAgntAuthAccs(int aiAgntAuthAccs)
	{
		ciAgntAuthAccs = aiAgntAuthAccs;
	}

	/**
	 * Set value of ciVoidAccs
	 * 
	 * @param aiVoidAccs
	 */
	public void setVoidAccs(int aiVoidAccs)
	{
		ciVoidAccs = aiVoidAccs;
	}

	/**
	 * Set the agent identity no of the updating agent.
	 * 
	 * @param aiUpdtngAgntIdntyNo the ciUpdtngAgntIdntyNo to set
	 */
	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)
	{
		this.ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;
	}

	/**
	 * Set the AbstractValue of AgncyNameAddress
	 * 
	 * @param aaAgncyNameAddress the AgncyNameAddress to set
	 */
	public void setAgncyNameAddress(NameAddressData aaAgncyNameAddress)
	{
		this.caAgncyNameAddress = aaAgncyNameAddress;
	}
	
}
