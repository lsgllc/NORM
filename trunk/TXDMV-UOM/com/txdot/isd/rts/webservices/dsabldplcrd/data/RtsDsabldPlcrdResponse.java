package com.txdot.isd.rts.webservices.dsabldplcrd.data;

import java.io.Serializable;

/*
 * RtsDsabldPlcrdResponse.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/24/2010	Created
 * 							defect 10607 Ver 6.6.0 
 * K Harrell	10/03/2010	add new Constructor() 
 * 							add RtsDsabldPlcrdResponse(asInvItmNo, aiErrMsgNo)
 * 							defect 10607 Ver 6.6.0 
 * R Pilon		02/09/2012	Change the setter methods to match correpsonding 
 * 							  getter methods to prevent web service validation 
 * 							  errors.  Also, modified the properties to match
 * 							  the getter/setter names as appopriate.
 * 							add csOfcName, ciRTSExpMo, ciRTSExpYr, csZpCd,
 * 							  setOfcName(), setRTSEffDate(), setRTSExpMo(), 
 * 							  setRTSExpYr(), setZpCd()
 * 							delete csCountyName, ciExpMo, ciExpYr, csZip, 
 * 							  setCountyName(), setEffectiveDate(), setExpMo(), 
 * 							  setExpYr(), setZip()
 * 							modify getOfcName(), getRTSExpMo(), getRTSExpYr(),
 * 							  getZpCd()
 * 							defect 11135 Ver 6.10.0
 * ---------------------------------------------------------------------
 */

/**
 * Request data for Disabled Placard Service
 *
 * @version	6.10.0		02/09/2012
 * @author	Kathy Harrell
 * <br>Creation Date:	09/24/20101	12:27:17
 */
public class RtsDsabldPlcrdResponse implements Serializable
{
	private int ciCustIdTypeCd;
	private int ciDeleteIndi;
	private int ciErrMsgNo;
	// defect 11135
	private int ciRTSExpMo;
	private int ciRTSExpYr;
	// end defect 11135
	private int ciInstIndi;
	private int ciResComptCntyNo;
	private int ciRTSEffDate;
	private String csAcctItmCd;
	private String csAcctItmCdDesc;
	private String csCity;
	// defect 11135
	private String csOfcName;
	// end defect 11135
	private String csCustId;
	private String csCustIdTypeDesc;
	private String csDelReasnDesc;
	private String csFrstNameLastName;
	private String csInvItmNo;
	private String csSt1;
	private String csSt2;
	private String csStateCntry;
	// defect 11135
	private String csZpCd;
	// end defect 11135

	static final long serialVersionUID = 4973214699605557917L;

	/**
	 * RtsDsabldPlcrdResponse.java Constructor
	 * 
	 */
	public RtsDsabldPlcrdResponse()
	{
		super();
	}

	/**
	 * RtsDsabldPlcrdResponse.java Constructor
	 * 
	 * @param asInvItmNo
	 * @param aiErrMsgNo 
	 */
	public RtsDsabldPlcrdResponse(String asInvItmNo, int aiErrMsgNo)
	{
		super();
		csInvItmNo = asInvItmNo;
		ciErrMsgNo = aiErrMsgNo;
	}

	/**
	 * Get value of csAcctItmCd
	 * 
	 * @return String
	 */
	public String getAcctItmCd()
	{
		return csAcctItmCd;
	}

	/**
	 * Get value of csAcctItmCdDesc
	 * 
	 * @return String
	 */
	public String getAcctItmCdDesc()
	{
		return csAcctItmCdDesc;
	}

	/**
	 * Get value of csCity
	 * 
	 * @return String
	 */
	public String getCity()
	{
		return csCity;
	}

	/**
	 * Get value of csCustId
	 * 
	 * @return String
	 */
	public String getCustId()
	{
		return csCustId;
	}

	/**
	 * Get value of ciCustIdTypeCd
	 * 
	 * @return int
	 */
	public int getCustIdTypeCd()
	{
		return ciCustIdTypeCd;
	}

	/**
	 * Get value of csCustIdTypeDesc
	 * 
	 * @return String
	 */
	public String getCustIdTypeDesc()
	{
		return csCustIdTypeDesc;
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
	 * Get value of csDelReasnDesc
	 * 
	 * @return csDelReasnDesc
	 */
	public String getDelReasnDesc()
	{
		return csDelReasnDesc;
	}

	/**
	 * Get value of ciErrMsgNo
	 * 
	 * @return int 
	 */
	public int getErrMsgNo()
	{
		return ciErrMsgNo;
	}

	/**
	 * Get value of csFrstNameLastName
	 * 
	 * @return String
	 */
	public String getFrstNameLastName()
	{
		return csFrstNameLastName;
	}

	/**
	 * Get value of ciInstIndi
	 * 
	 * @return int 
	 */
	public int getInstIndi()
	{
		return ciInstIndi;
	}

	/**
	 * Get value of csInvItmNo
	 * 
	 * @return String
	 */
	public String getInvItmNo()
	{
		return csInvItmNo;
	}

	/**
	 * Get value of csOfcName
	 * 
	 * @return String
	 */
	public String getOfcName()
	{
		// defect 11135
		return csOfcName;
		// end defect 11135
	}

	/**
	 * Get value of ciResComptCntyNo
	 * 
	 * @return int 
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}

	/**
	 * Get value of ciRTSEffDate
	 * 
	 * @return int
	 */
	public int getRTSEffDate()
	{
		return ciRTSEffDate;
	}

	/**
	 * Get value of ciRTSExpMo
	 * 
	 * @return int
	 */
	public int getRTSExpMo()
	{
		// defect 11135
		return ciRTSExpMo;
		// end defect 11135
	}

	/**
	 * Get value of ciRTSExpYr
	 * 
	 * @return int
	 */
	public int getRTSExpYr()
	{
		// defect 11135
		return ciRTSExpYr;
		// end defect 11135
	}

	/**
	 * Set value of 
	 * 
	 * @return
	 */
	public String getSt1()
	{
		return csSt1;
	}

	/**
	 * Set value of 
	 * 
	 * @return
	 */
	public String getSt2()
	{
		return csSt2;
	}

	/**
	 * Set value of 
	 * 
	 * @return
	 */
	public String getStateCntry()
	{
		return csStateCntry;
	}

	/**
	 * Return value of csZpCd
	 * 
	 * @return
	 */
	public String getZpCd()
	{
		// defect 11135
		return csZpCd;
		// end defect 11135
	}

	/**
	 * Set value of csAcctItmCd
	 * 
	 * @param asAcctItmCd
	 */
	public void setAcctItmCd(String asAcctItmCd)
	{
		csAcctItmCd = asAcctItmCd;
	}

	/**
	 * Set value of csAcctItmCdDesc
	 * 
	 * @param string
	 */
	public void setAcctItmCdDesc(String string)
	{
		csAcctItmCdDesc = string;
	}

	/**
	 * Set value of csCity
	 * 
	 * @param asCity
	 */
	public void setCity(String asCity)
	{
		csCity = asCity;
	}

	/**
	 * Set value of csOfcName
	 * 
	 * @param asOfcName
	 */
	public void setOfcName(String asOfcName)
	{
		csOfcName = asOfcName;
	}

	/**
	 * Set value of csCustId
	 * 
	 * @param asCustId
	 */
	public void setCustId(String asCustId)
	{
		csCustId = asCustId;
	}

	/**
	 * Set value of ciCustIdTypeCd
	 * 
	 * @param aiCustIdTypeCd
	 */
	public void setCustIdTypeCd(int aiCustIdTypeCd)
	{
		ciCustIdTypeCd = aiCustIdTypeCd;
	}

	/**
	 * Set value of csCustIdTypeDesc
	 * 
	 * @param asCustIdTypeDesc
	 */
	public void setCustIdTypeDesc(String asCustIdTypeDesc)
	{
		csCustIdTypeDesc = asCustIdTypeDesc;
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
	 * Set value of csDelReasnDesc
	 * 
	 * @param asDelReasnDesc
	 */
	public void setDelReasnDesc(String asDelReasnDesc)
	{
		csDelReasnDesc = asDelReasnDesc;
	}

	/**
	 * Set value of ciRTSEffDate 
	 * 
	 * @param aiRTSEffDate
	 */
	public void setRTSEffDate(int aiRTSEffDate)
	{
		ciRTSEffDate = aiRTSEffDate;
	}

	/**
	 * Set value of ciErrMsgNo
	 * 
	 * @param aiErrMsgNo
	 */
	public void setErrMsgNo(int aiErrMsgNo)
	{
		ciErrMsgNo = aiErrMsgNo;
	}

	/**
	 * Set value of ciRTSExpMo 
	 * 
	 * @param aiRTSExpMo
	 */
	public void setRTSExpMo(int aiRTSExpMo)
	{
		ciRTSExpMo = aiRTSExpMo;
	}

	/**
	 * Set value of ciRTSExpYr 
	 * 
	 * @param aiRTSExpYr
	 */
	public void setRTSExpYr(int aiRTSExpYr)
	{
		ciRTSExpYr = aiRTSExpYr;
	}

	/**
	 * Set value of csApplicantName
	 * 
	 * @param asApplicantName
	 */
	public void setFrstNameLastName(String asApplicantName)
	{
		csFrstNameLastName = asApplicantName;
	}

	/**
	 * Set value of ciInstIndi
	 * 
	 * @param aiInstIndi
	 */
	public void setInstIndi(int aiInstIndi)
	{
		ciInstIndi = aiInstIndi;
	}

	/**
	 * Set value of csInvItmNo 
	 * 
	 * @param asInvItmNo
	 */
	public void setInvItmNo(String asInvItmNo)
	{
		csInvItmNo = asInvItmNo;
	}

	/**
	 * Set value of ciResComptCntyNo
	 * 
	 * @param aiResComptCntyNo
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * Set value of csSt1
	 * 
	 * @param asSt1
	 */
	public void setSt1(String asSt1)
	{
		csSt1 = asSt1;
	}

	/**
	 * Set value of csSt2
	 * 
	 * @param asSt2
	 */
	public void setSt2(String asSt2)
	{
		csSt2 = asSt2;
	}

	/**
	 * Set value of csStateCntry
	 * 
	 * @param asStateCntry
	 */
	public void setStateCntry(String asStateCntry)
	{
		csStateCntry = asStateCntry;
	}

	/**
	 * Set value of csZpCd
	 * 
	 * @param asZpCd
	 */
	public void setZpCd(String asZpCd)
	{
		csZpCd = asZpCd;
	}

}
