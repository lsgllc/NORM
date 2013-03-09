package com.txdot.isd.rts.services.data;import java.io.Serializable;import com.txdot.isd.rts.services.util.RTSDate;import com.txdot.isd.rts.services.util.constants.RegistrationConstant;import com.txdot.isd.rts.webservices.agnt.data.RtsWebAgntSecurityWS;/* * WebAgentSecrty.java * * (c) Texas Department of Transportation 2010 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	12/28/2010	Created * 							defect 10708 Ver 6.7.0   * K Harrell	01/05/2011	Renamings per standards  * 							defect 10708 Ver 6.7.0 * K Harrell	01/10/2011	add ciSubmitBatchAccs, get/set methods * 							defect 10708 Ver 6.7.0   * K Harrell	03/30/2011	add ciDMVUserIndi, csAgncyTypeCd,  * 							 get/is/set methods  * 							defect 10785 Ver 6.7.1 * K Harrell	04/06/2011	delete ciDMVUser, get/set method  * 							modify isDMVUser()  * 							defect 10785 Ver 6.7.1       * Ray Rowehl	05/09/2011	Rename UserAuthAccs to AgntAuthAccs to match * 							the database name. * 							add ciAgntAuthAccs * 							delete ciUserAuthAccs * 							add getAgntAuthAccs(), setAgntAuthAccs() * 							delete getUserAuthAccs(), setUserAuthAccs() * 							defect 10718 Ver 6.7.1 * K McKee      09/01/2011  added ciUpdtngAgntIdntyNo; *                          defect 10729 Ver 6.8.1 * K McKee      11/03/2011  added caAgncyNameAddress.  getters/setters * 							defect 11146 Ver 6.9.0 * --------------------------------------------------------------------- *//** * This Data class contains attributes and get and set methods for  * WebAgentSecrtyData  *  * @version	6.9.0 			11/03/2011	 * @author	Kathy Harrell * <br>Creation Date:		12/28/2010 18:33:17 *//* &WebAgentSecurityData& */public class WebAgentSecurityData implements Serializable{/* &WebAgentSecurityData'ciAgncyAuthAccs& */	private int ciAgncyAuthAccs;/* &WebAgentSecurityData'ciAgncyIdntyNo& */	private int ciAgncyIdntyNo;/* &WebAgentSecurityData'ciAgncyInfoAccs& */	private int ciAgncyInfoAccs;/* &WebAgentSecurityData'ciAgntIdntyNo& */	private int ciAgntIdntyNo;/* &WebAgentSecurityData'ciAgntSecrtyIdntyNo& */	private int ciAgntSecrtyIdntyNo;/* &WebAgentSecurityData'ciAprvBatchAccs& */	private int ciAprvBatchAccs;/* &WebAgentSecurityData'ciBatchAccs& */	private int ciBatchAccs;/* &WebAgentSecurityData'ciDashAccs& */	private int ciDashAccs;/* &WebAgentSecurityData'ciDeleteIndi& */	private int ciDeleteIndi;/* &WebAgentSecurityData'ciRenwlAccs& */	private int ciRenwlAccs;/* &WebAgentSecurityData'ciReprntAccs& */	private int ciReprntAccs;/* &WebAgentSecurityData'ciRptAccs& */	private int ciRptAccs;/* &WebAgentSecurityData'ciSubmitBatchAccs& */	private int ciSubmitBatchAccs;/* &WebAgentSecurityData'ciUpdtngAgntIdntyNo& */	private int ciUpdtngAgntIdntyNo;	// defect 10718	//private int ciUserAuthAccs;/* &WebAgentSecurityData'ciAgntAuthAccs& */	private int ciAgntAuthAccs;	// end defect 10718/* &WebAgentSecurityData'ciVoidAccs& */	private int ciVoidAccs;/* &WebAgentSecurityData'csAgncyTypeCd& */	private String csAgncyTypeCd;		// defect 11146/* &WebAgentSecurityData'caAgncyNameAddress& */	private NameAddressData caAgncyNameAddress;	// end defect 11146/* &WebAgentSecurityData'caChngTimestmp& */	private RTSDate caChngTimestmp;	/* &WebAgentSecurityData'serialVersionUID& */	static final long serialVersionUID = -7946194815789046049L;	/**	 * WebAgentSecurityData Constructor	 * 	 *//* &WebAgentSecurityData.WebAgentSecurityData& */	public WebAgentSecurityData()	{		super();	}/* &WebAgentSecurityData.WebAgentSecurityData$1& */	public WebAgentSecurityData(RtsWebAgntSecurityWS aaObject)	{		super();		setAgncyAuthAccs(aaObject.isAgncyAuthAccs() ? 1 : 0);		setAgncyInfoAccs(aaObject.isAgncyInfoAccs() ? 1 : 0);		setAgntAuthAccs(aaObject.isAgntAuthAccs() ? 1 : 0);		setAprvBatchAccs(aaObject.isAprvBatchAccs() ? 1 : 0);		setBatchAccs(aaObject.isBatchAccs() ? 1 : 0);		setRenwlAccs(aaObject.isRenwlAccs() ? 1 : 0);		setReprntAccs(aaObject.isRePrntAccs() ? 1 : 0);		setRptAccs(aaObject.isRptAccs() ? 1 : 0);		setSubmitBatchAccs(aaObject.isSubmitBatchAccs() ? 1 : 0);		setVoidAccs(aaObject.isVoidAccs() ? 1 : 0);		setAgncyIdntyNo(aaObject.getAgncyIdntyNo());		setAgntIdntyNo(aaObject.getAgntIdntyNo());		setAgntSecrtyIdntyNo(aaObject.getAgntSecrtyIdntyNo());		setUpdtngAgntIdntyNo(aaObject.getUpdtngAgntIdntyNo());	}	/**	 * Get value of ciAgncyAuthAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getAgncyAuthAccs& */	public int getAgncyAuthAccs()	{		return ciAgncyAuthAccs;	}	/**	 * Get value of ciAgncyIdntyNo	 * 	 * @return int	 *//* &WebAgentSecurityData.getAgncyIdntyNo& */	public int getAgncyIdntyNo()	{		return ciAgncyIdntyNo;	}	/**	 * Get value of ciAgncyInfoAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getAgncyInfoAccs& */	public int getAgncyInfoAccs()	{		return ciAgncyInfoAccs;	}	/**	 * Get value of csAgncyTypeCd	 * 	 * @return String 	 *//* &WebAgentSecurityData.getAgncyTypeCd& */	public String getAgncyTypeCd()	{		return csAgncyTypeCd;	}	/**	 * Get value of ciAgntIdntyNo	 * 	 * @return int	 *//* &WebAgentSecurityData.getAgntIdntyNo& */	public int getAgntIdntyNo()	{		return ciAgntIdntyNo;	}	/**	 * Get value of ciAgntSecrtyIdntyNo	 * 	 * @return int	 *//* &WebAgentSecurityData.getAgntSecrtyIdntyNo& */	public int getAgntSecrtyIdntyNo()	{		return ciAgntSecrtyIdntyNo;	}	/**	 * Get value of ciAprvBatchAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getAprvBatchAccs& */	public int getAprvBatchAccs()	{		return ciAprvBatchAccs;	}	/**	 * Get value of ciBatchAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getBatchAccs& */	public int getBatchAccs()	{		return ciBatchAccs;	}	/**	 * Get value of caChngTimestmp	 * 	 * @return RTSDate	 *//* &WebAgentSecurityData.getChngTimestmp& */	public RTSDate getChngTimestmp()	{		return caChngTimestmp;	}	/**	 * Get value of ciDashAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getDashAccs& */	public int getDashAccs()	{		return ciDashAccs;	}	/**	 * Get value of ciDeleteIndi	 * 	 * @return int	 *//* &WebAgentSecurityData.getDeleteIndi& */	public int getDeleteIndi()	{		return ciDeleteIndi;	}	/**	 * Get value of ciRenwlAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getRenwlAccs& */	public int getRenwlAccs()	{		return ciRenwlAccs;	}	/**	 * Get value of ciReprntAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getReprntAccs& */	public int getReprntAccs()	{		return ciReprntAccs;	}	/**	 * Get value of ciRptAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getRptAccs& */	public int getRptAccs()	{		return ciRptAccs;	}	/**	 * Get value of ciSubmitBatchAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getSubmitBatchAccs& */	public int getSubmitBatchAccs()	{		return ciSubmitBatchAccs;	}	/**	 * Get value of ciAgntAuthAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getAgntAuthAccs& */	public int getAgntAuthAccs()	{		return ciAgntAuthAccs;	}	/**	 * Get value of ciVoidAccs	 * 	 * @return int	 *//* &WebAgentSecurityData.getVoidAccs& */	public int getVoidAccs()	{		return ciVoidAccs;	}		/**	 * Get the value of caAgncyNameAddress	 * 	 * @return the caAgncyNameAddress	 *//* &WebAgentSecurityData.getAgncyNameAddress& */	public NameAddressData getAgncyNameAddress()	{		if (caAgncyNameAddress == null)		{			caAgncyNameAddress = new NameAddressData();		}		return caAgncyNameAddress;	}		/**	 * Get the updating agent identity no	 * 	 * @return the ciUpdtngAgntIdntyNo	 *//* &WebAgentSecurityData.getUpdtngAgntIdntyNo& */	public int getUpdtngAgntIdntyNo()	{		return ciUpdtngAgntIdntyNo;	}		/**	 * Return if DMV User	 * 	 * @return boolean 	 *//* &WebAgentSecurityData.isDMVUser& */	public boolean isDMVUser()	{		return csAgncyTypeCd != null			&& (csAgncyTypeCd.equalsIgnoreCase(RegistrationConstant.TXDMVHQ_AGNCYTYPECD)			|| csAgncyTypeCd.equalsIgnoreCase(RegistrationConstant.REGIONALSC_AGNCYTYPECD) 			|| csAgncyTypeCd.equalsIgnoreCase(RegistrationConstant.COUNTY_AGNCYTYPECD));	}	/**	 * Set value of ciAgncyAuthAccs	 * 	 * @param aiAgncyAuthAccs	 *//* &WebAgentSecurityData.setAgncyAuthAccs& */	public void setAgncyAuthAccs(int aiAgncyAuthAccs)	{		ciAgncyAuthAccs = aiAgncyAuthAccs;	}	/**	 * Set value of ciAgncyIdntyNo	 * 	 * @param aiAgncyIdntyNo	 *//* &WebAgentSecurityData.setAgncyIdntyNo& */	public void setAgncyIdntyNo(int aiAgncyIdntyNo)	{		ciAgncyIdntyNo = aiAgncyIdntyNo;	}	/**	 * Set value of ciAgncyInfoAccs	 * 	 * @param aiAgncyInfoAccs	 *//* &WebAgentSecurityData.setAgncyInfoAccs& */	public void setAgncyInfoAccs(int aiAgncyInfoAccs)	{		ciAgncyInfoAccs = aiAgncyInfoAccs;	}	/**	 * Set value of csAgncyTypeCd	 * 	 * @param asAgncyTypeCd	 *//* &WebAgentSecurityData.setAgncyTypeCd& */	public void setAgncyTypeCd(String asAgncyTypeCd)	{		csAgncyTypeCd = asAgncyTypeCd;	}	/**	 * Set value of ciAgntIdntyNo	 * 	 * @param aiAgntIdntyNo	 *//* &WebAgentSecurityData.setAgntIdntyNo& */	public void setAgntIdntyNo(int aiAgntIdntyNo)	{		ciAgntIdntyNo = aiAgntIdntyNo;	}	/**	 * Set value of ciAgntSecrtyIdntyNo	 * 	 * @param aiAgntSecrtyIdntyNo	 *//* &WebAgentSecurityData.setAgntSecrtyIdntyNo& */	public void setAgntSecrtyIdntyNo(int aiAgntSecrtyIdntyNo)	{		ciAgntSecrtyIdntyNo = aiAgntSecrtyIdntyNo;	}	/**	 * Set value of ciAprvBatchAccs	 * 	 * @param aiAprvBatchAccs	 *//* &WebAgentSecurityData.setAprvBatchAccs& */	public void setAprvBatchAccs(int aiAprvBatchAccs)	{		ciAprvBatchAccs = aiAprvBatchAccs;	}	/**	 * Set value of ciBatchAccs	 * 	 * @param aiBatchAccs	 *//* &WebAgentSecurityData.setBatchAccs& */	public void setBatchAccs(int aiBatchAccs)	{		ciBatchAccs = aiBatchAccs;	}	/**	 * Set value of caChngTimestmp	 * 	 * @param aaChngTimestmp	 *//* &WebAgentSecurityData.setChngTimestmp& */	public void setChngTimestmp(RTSDate aaChngTimestmp)	{		caChngTimestmp = aaChngTimestmp;	}	/**	 * Set value of ciDashAccs	 * 	 * @param aiDashAccs	 *//* &WebAgentSecurityData.setDashAccs& */	public void setDashAccs(int aiDashAccs)	{		ciDashAccs = aiDashAccs;	}	/**	 * Set value of ciDeleteIndi	 * 	 * @param aiDeleteIndi	 *//* &WebAgentSecurityData.setDeleteIndi& */	public void setDeleteIndi(int aiDeleteIndi)	{		ciDeleteIndi = aiDeleteIndi;	}	/**	 * Set value of ciRenwlAccs	 * 	 * @param aiRenwlAccs	 *//* &WebAgentSecurityData.setRenwlAccs& */	public void setRenwlAccs(int aiRenwlAccs)	{		ciRenwlAccs = aiRenwlAccs;	}	/**	 * Set value of ciReprntAccs	 * 	 * @param aiReprntAccs	 *//* &WebAgentSecurityData.setReprntAccs& */	public void setReprntAccs(int aiReprntAccs)	{		ciReprntAccs = aiReprntAccs;	}	/**	 * Set value of ciRptAccs	 * 	 * @param aiRptAccs	 *//* &WebAgentSecurityData.setRptAccs& */	public void setRptAccs(int aiRptAccs)	{		ciRptAccs = aiRptAccs;	}	/**	 * Set value of ciSubmitBatchAccs	 * 	 * @param aiSubmitBatchAccs	 *//* &WebAgentSecurityData.setSubmitBatchAccs& */	public void setSubmitBatchAccs(int aiSubmitBatchAccs)	{		ciSubmitBatchAccs = aiSubmitBatchAccs;	}	/**	 * Set value of ciAgntAuthAccs	 * 	 * @param aiAgntAuthAccs	 *//* &WebAgentSecurityData.setAgntAuthAccs& */	public void setAgntAuthAccs(int aiAgntAuthAccs)	{		ciAgntAuthAccs = aiAgntAuthAccs;	}	/**	 * Set value of ciVoidAccs	 * 	 * @param aiVoidAccs	 *//* &WebAgentSecurityData.setVoidAccs& */	public void setVoidAccs(int aiVoidAccs)	{		ciVoidAccs = aiVoidAccs;	}	/**	 * Set the agent identity no of the updating agent.	 * 	 * @param aiUpdtngAgntIdntyNo the ciUpdtngAgntIdntyNo to set	 *//* &WebAgentSecurityData.setUpdtngAgntIdntyNo& */	public void setUpdtngAgntIdntyNo(int aiUpdtngAgntIdntyNo)	{		this.ciUpdtngAgntIdntyNo = aiUpdtngAgntIdntyNo;	}	/**	 * Set the AbstractValue of AgncyNameAddress	 * 	 * @param aaAgncyNameAddress the AgncyNameAddress to set	 *//* &WebAgentSecurityData.setAgncyNameAddress& */	public void setAgncyNameAddress(NameAddressData aaAgncyNameAddress)	{		this.caAgncyNameAddress = aaAgncyNameAddress;	}	}/* #WebAgentSecurityData# */