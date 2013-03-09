package com.txdot.isd.rts.services.data;

import java.io.Serializable;

import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;

import com.txdot.isd.rts.webservices.ren.data.RtsVehicleInfo;

/*
 * WebAgencyTransactionData.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/10/2011	Created 
 * 							defect 10708 Ver 6.7.0 
 * K Harrell	01/21/2011	add needsTrans() 
 * 							defect 10734 Ver 6.7.0 
 * K Harrell	02/02/2011 	add ciVehGrossWt, get/set methods
 * 						    defect 10734 Ver 6.7.0
 * K Harrell	03/11/2011	add caInitReqTimestmnp, get/set methods,
 * 							 caAccptTimestmp, get/set methods,
 * 							 ciVoidSavReqId, get/set methods  
 * 							defect 10734 Ver 6.7.1 
 * K Harrell	03/25/2011	add ciPltAge, csWebSessionId, get/set methods
 * 							delete ciCitationIndi, csReqIpAddr, 
 * 								get/set methods   
 * 							defect 10768 Ver 6.7.1 
 * K Harrell	03/31/2011	add getReprtAccs(),getVoidAccs(),
 * 							  getProcsAccs(), isVoided() 
 * 							defect 10785 Ver 6.7.1  
 * K Harrell	04/06/2011	modify getReprtAccs(),getVoidAccs(),
 * 							  getProcsAccs(), isVoided() 
 * 							defect 10785 Ver 6.7.1 
 * K Harrell	11/05/2011	add csOwnrTtlName1, csOwnrTtlName2,
 * 								csRecpntName, get/set methods 
 * 							defect 11137 Ver 6.9.0   
 * 
 * K McKee      11/08/2011  add csUserName,ciAgntIdntyNo get/set methods
 * 							defect 11145 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * Data class for RTS_WEB_AGNCY_TRANS 
 *
 * @version	6.9.0  			11/08/2011
 * @author	Kathy Harrell 
 * @since 					01/10/2011 12:32:17
 */
public class WebAgencyTransactionData implements Serializable
{
	private int ciAccptVehIndi;
	private int ciAddlSetIndi;
	private int ciAgncyBatchIdntyNo;
	private int ciAgncyVoidIndi;
	private int ciAgntSecrtyIdntyNo;
	private int ciCntyVoidIndi;
	private int ciMustReplPltIndi;
	private int ciNewPltExpMo;
	private int ciNewPltExpYr;
	private int ciNewRegExpMo;
	private int ciNewRegExpYr;
	private int ciPltAge;
	private int ciPltBirthDate;
	private int ciPltExpMo;
	private int ciPltExpYr;
	private int ciPltValidityTerm;
	private int ciPrntQty;
	private int ciRegClassCd;
	private int ciRegExpMo;
	private int ciRegExpYr;
	private int ciResComptCntyNo;
	private int ciSavReqId;
	private int ciSubconId;
	private int ciVehGrossWt;
	private int ciVehModlYr;
	private int ciVoidSavReqId;
	
	private String csAuditTrailTransId;
	private String csBarCdVersionNo;
	private String csBatchStatusCd;
	private String csDocNo;
	private String csInsVerfdCd;
	private String csInvItmNo;
	private String csKeyTypeCd;
	private String csOrgNo;
	
	// defect 11137 
	private String csOwnrTtlName1;
	private String csOwnrTtlName2;
	private String csRecpntName;
	// end defect 11137 
	
	private String csRegPltCd;
	private String csRegPltNo;
	private String csStkrItmCd;
	private String csTransId;
	private String csVehMk;
	private String csVehModl;
	private String csVIN;
	private String csWebSessionId;
	
	private RTSDate caAccptTimestmp;
	private RTSDate caInitReqTimestmp;
	private RTSDate caTransTimestmp;
	
	// defect 11145
	
	private int ciAgntIdntyNo;

	private String csUserName;
	// 11145
	
	private static final long serialVersionUID = 5340563781990661779L;
	
	/**
	 * WebAgencyTransactionData.java Constructor
	 * 
	 */
	public WebAgencyTransactionData()
	{
		super();
	}

	/**
	 * WebAgencyTransactionData.java Constructor
	 * 
	 * @param aaRtsVehInfo
	 */
	public WebAgencyTransactionData(RtsVehicleInfo aaRtsVehInfo)
	{
		super();

		// handle registration data
		setInsVerfdCd(
			aaRtsVehInfo.getRegistrationData().getInsVerfdCd());
		setMustReplPltIndi(
			aaRtsVehInfo.getRegistrationData().isMustReplPltIndi()
				? 1
				: 0);
		setNewRegExpMo(
			aaRtsVehInfo.getRegistrationData().getNewRegExpMo());
		setNewRegExpYr(
			aaRtsVehInfo.getRegistrationData().getNewRegExpYr());
		setPltAge(aaRtsVehInfo.getRegistrationData().getPltAge());
		setPltBirthDate(
			aaRtsVehInfo.getRegistrationData().getPltBirthDate());
		setPrntQty(aaRtsVehInfo.getRegistrationData().getPrntQty());
		setRegClassCd(
			aaRtsVehInfo.getRegistrationData().getRegClassCd());
		setRegExpMo(aaRtsVehInfo.getRegistrationData().getRegExpMo());
		setRegExpYr(aaRtsVehInfo.getRegistrationData().getRegExpYr());
		setResComptCntyNo(
			aaRtsVehInfo.getRegistrationData().getResComptCntyNo());
		setVehGrossWt(
			aaRtsVehInfo.getRegistrationData().getVehGrossWt());
		setInvItmNo(aaRtsVehInfo.getRegistrationData().getInvItmNo());
		setRegPltCd(aaRtsVehInfo.getRegistrationData().getRegPltCd());
		setRegPltNo(aaRtsVehInfo.getRegistrationData().getRegPltNo());
		setStkrItmCd(aaRtsVehInfo.getRegistrationData().getStkrItmCd());

		// handle title data
		setDocNo(aaRtsVehInfo.getTitleData().getDocNo());

		// handle vehicle data
		setVehModl(aaRtsVehInfo.getVehicleData().getVehModl());
		setVehModlYr(aaRtsVehInfo.getVehicleData().getVehModlYr());
		setVehMk(aaRtsVehInfo.getVehicleData().getVehMk());
		setVIN(aaRtsVehInfo.getVehicleData().getVIN());

		// handle transaction data
		setAccptVehIndi(
			aaRtsVehInfo.getTransData().isAccptVehIndi() ? 1 : 0);
		setAgncyVoidIndi(
			aaRtsVehInfo.getTransData().isAgncyVoidIndi() ? 1 : 0);
		setCntyVoidIndi(
			aaRtsVehInfo.getTransData().isCntyVoidIndi() ? 1 : 0);
		setAgncyBatchIdntyNo(
			aaRtsVehInfo.getTransData().getAgncyBatchIdntyNo());
		setAgntSecrtyIdntyNo(
			aaRtsVehInfo.getTransData().getAgntSecrtyIdntyNo());
		setSavReqId(aaRtsVehInfo.getTransData().getSAVReqId());
		setSubconId(aaRtsVehInfo.getTransData().getSubconId());
		setAuditTrailTransId(
			aaRtsVehInfo.getTransData().getAuditTrailTransId());
		setBarCdVersionNo(
			aaRtsVehInfo.getTransData().getBarCdVersionNo());
		setKeyTypeCd(aaRtsVehInfo.getTransData().getKeyTypeCd());
		//setWebSessionId(aaRtsVehInfo.getTransData().getWebSessionId());
		setTransId(aaRtsVehInfo.getTransData().getPosTransId());

		// handle special plates data
		if (aaRtsVehInfo.getSpecialPlts() != null)
		{
			setNewPltExpMo(
				aaRtsVehInfo.getSpecialPlts().getNewPltExpMo());
			setNewPltExpYr(
				aaRtsVehInfo.getSpecialPlts().getNewPltExpYr());
			setPltExpMo(aaRtsVehInfo.getSpecialPlts().getPltExpMo());
			setPltExpYr(aaRtsVehInfo.getSpecialPlts().getPltExpYr());
			setOrgNo(aaRtsVehInfo.getSpecialPlts().getOrgNo());
			setAddlSetIndi(
				aaRtsVehInfo.getSpecialPlts().getAddlSetIndi());
			setPltValidityTerm(
				aaRtsVehInfo.getSpecialPlts().getPltValidityTerm());
		}
	}

	/**
	 * Return value of caAccptTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getAccptTimestmp()
	{
		return caAccptTimestmp;
	}

	/**
	 * Get value of ciAccptVehIndi
	 * 
	 * @return int
	 */
	public int getAccptVehIndi()
	{
		return ciAccptVehIndi;
	}

	/**
	 * Get value of ciAddlSetIndi
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	/**
	 * Return value of ciAgncyBatchIdntyNo
	 * 
	 * @return int
	 */
	public int getAgncyBatchIdntyNo()
	{
		return ciAgncyBatchIdntyNo;
	}

	/**
	 * Get value of ciAgncyVoidIndi
	 * 
	 * @return int
	 */
	public int getAgncyVoidIndi()
	{
		return ciAgncyVoidIndi;
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
	 * Get value of csAuditTrailTransId
	 * 
	 * @return String
	 */
	public String getAuditTrailTransId()
	{
		return csAuditTrailTransId;
	}

	/**
	 * Get value of csBarCdVersionNo
	 * 
	 * @return String
	 */
	public String getBarCdVersionNo()
	{
		return csBarCdVersionNo;
	}

	/**
	 * Return value of csBatchStatusCd
	 * 
	 * @return String 
	 */
	public String getBatchStatusCd()
	{
		return csBatchStatusCd;
	}

	/**
	 * Get value of ciCntyVoidIndi
	 * 
	 * @return int
	 */
	public int getCntyVoidIndi()
	{
		return ciCntyVoidIndi;
	}

	/**
	 * Get value of csDocNo
	 * 
	 * @return String 
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Return value of caInitReqTimestmp
	 * 
	 * @return RTSDate
	 */
	public RTSDate getInitReqTimestmp()
	{
		return caInitReqTimestmp;
	}

	/**
	 * Get value of csInsVerfdCd
	 * 
	 * @return  String
	 */
	public String getInsVerfdCd()
	{
		return csInsVerfdCd;
	}

	/**
	 * Get value of csInvItmNo
	 * 
	 * @return  String
	 */
	public String getInvItmNo()
	{
		return csInvItmNo;
	}

	/**
	 * Get value of csKeyTypeCd
	 * 
	 * @return String
	 */
	public String getKeyTypeCd()
	{
		return csKeyTypeCd;
	}

	/**
	 * Get value of ciMustReplPltIndi
	 * 
	 * @return int
	 */
	public int getMustReplPltIndi()
	{
		return ciMustReplPltIndi;
	}

	/**
	 * Get value of ciNewPltExpMo
	 * 
	 * @return int
	 */
	public int getNewPltExpMo()
	{
		return ciNewPltExpMo;
	}

	/**
	 * Get value of ciNewPltExpYr
	 * 
	 * @return int
	 */
	public int getNewPltExpYr()
	{
		return ciNewPltExpYr;
	}

	/**
	 * Get value of ciNewRegExpMo
	 * 
	 * @return int
	 */
	public int getNewRegExpMo()
	{
		return ciNewRegExpMo;
	}

	/**
	 * Get value of ciNewRegExpYr
	 * 
	 * @return int
	 */
	public int getNewRegExpYr()
	{
		return ciNewRegExpYr;
	}

	/**
	 * Get value of csOrgNo
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * @return csOwnrTtlName1
	 */
	public String getOwnrTtlName1()
	{
		return csOwnrTtlName1;
	}

	/**
	 * @return csOwnrTtlName2
	 */
	public String getOwnrTtlName2()
	{
		return csOwnrTtlName2;
	}

	/**
	 * Return value of ciPltAge
	 * 
	 * @return int
	 */
	public int getPltAge()
	{
		return ciPltAge;
	}

	/**
	 * Get value of ciPltBirthDate
	 * 
	 * @return int
	 */
	public int getPltBirthDate()
	{
		return ciPltBirthDate;
	}

	/**
	 * Get value of ciPltExpMo
	 * 
	 * @return int
	 */
	public int getPltExpMo()
	{
		return ciPltExpMo;
	}
	/**
	 * Get value of ciPltExpYr 
	 * 
	 * @return int
	 */
	public int getPltExpYr()
	{
		return ciPltExpYr;
	}

	/**
	 * Get value of ciPltValidityTerm
	 * 
	 * @return int
	 */
	public int getPltValidityTerm()
	{
		return ciPltValidityTerm;
	}

	/**
	 * Return value of ciPrntQty
	 * 
	 * @return int
	 */
	public int getPrntQty()
	{
		return ciPrntQty;
	}

	/**
	 * Return boolean to denote if requested processing should be allowed
	 * 
	 * @param aaWABatchData
	 * @param aaWASecData
	 * @return boolean 
	 */
	private boolean getProcsAccs(
		WebAgencyBatchData aaWABatchData,
		boolean abProcsAccs,
		WebAgentSecurityData aaWASecData)
	{
		boolean lbProcsAccs = false;

		String lsBatchStatusCd = aaWABatchData.getBatchStatusCd();

		if (abProcsAccs
			&& !isVoided()
			&& (lsBatchStatusCd
				.equals(RegistrationConstant.OPEN_BATCHSTATUSCD)
				|| lsBatchStatusCd.equals(
					RegistrationConstant.SUBMIT_BATCHSTATUSCD)
				|| lsBatchStatusCd.equals(
					RegistrationConstant.CLOSE_BATCHSTATUSCD)))
		{

			// If DMV User - AgncyTypeCd = "C" 
			if (aaWASecData.isDMVUser())
			{
				// If DMV Agency 
				if (aaWASecData.getAgncyIdntyNo()
					== aaWABatchData.getAgncyIdntyNo())
				{
					lbProcsAccs = true;
				}
				else
				{
					lbProcsAccs =
						lsBatchStatusCd.equals(
							RegistrationConstant.SUBMIT_BATCHSTATUSCD);
				}
			}
			else
			{
				lbProcsAccs =
					lsBatchStatusCd.equals(
						RegistrationConstant.OPEN_BATCHSTATUSCD)
						|| lsBatchStatusCd.equals(
							RegistrationConstant.CLOSE_BATCHSTATUSCD);
			}
		}
		return lbProcsAccs;

	}

	/**
	 * @return  csRecpntName
	 */
	public String getRecpntName()
	{
		return csRecpntName;
	}

	/**
	 * Get value of ciRegClassCd
	 * 
	 * @return int
	 */
	public int getRegClassCd()
	{
		return ciRegClassCd;
	}

	/**
	 * Get value of ciRegExpMo
	 * 
	 * @return int
	 */
	public int getRegExpMo()
	{
		return ciRegExpMo;
	}

	/**
	 * Get value of ciRegExpYr
	 * 
	 * @return int
	 */
	public int getRegExpYr()
	{
		return ciRegExpYr;
	}

	/**
	 * Get value of csRegPltCd
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Get value of csRegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Return boolean to denote if Reprint should be allowed
	 * 
	 * @param asBatchStatusCd
	 * @param aaWASecData
	 * @return boolean 
	 */
	public boolean getReprtAccs(
		WebAgencyBatchData aaWABatchData,
		WebAgentSecurityData aaWASecData)
	{
		return getProcsAccs(
			aaWABatchData,
			aaWASecData.getReprntAccs() == 1,
			aaWASecData);
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
	 * Get value of ciSavReqId
	 * 
	 * @return int
	 */
	public int getSavReqId()
	{
		return ciSavReqId;
	}

	/**
	 * Get value of csStkrItmCd
	 * 
	 * @return String
	 */
	public String getStkrItmCd()
	{
		return csStkrItmCd;
	}
	/**
	 * Get value of ciSubconId
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}

	/**
	 * Get value of csTransId
	 * 
	 * @return String
	 */
	public String getTransId()
	{
		return csTransId;
	}

	/**
	 * Get value of caTransTimestmp
	 * 
	 * @return RTSDate 
	 */
	public RTSDate getTransTimestmp()
	{
		return caTransTimestmp;
	}

	/**
	 * Get value of ciVehGrossWt
	 * 
	 * @return int
	 */
	public int getVehGrossWt()
	{
		return ciVehGrossWt;
	}

	/**
	 * Get value of csVehMk
	 * 
	 * @return String
	 */
	public String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Get value of csVehModl
	 * 
	 * @return String 
	 */
	public String getVehModl()
	{
		return csVehModl;
	}

	/**
	 * Get value of ciVehModlYr
	 * 
	 * @return int
	 */
	public int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Get value of csVIN
	 * 
	 * @return  String 
	 */
	public String getVIN()
	{
		return csVIN;
	}

	/**
	 * Return boolean to denote if Void should be allowed
	 * 
	 * @param aaWABatchData
	 * @param aaWASecData
	 * @return boolean 
	 */
	public boolean getVoidAccs(
		WebAgencyBatchData aaWABatchData,
		WebAgentSecurityData aaWASecData)
	{
		return getProcsAccs(
			aaWABatchData,
			aaWASecData.getVoidAccs() == 1,
			aaWASecData);
	}

	/**
	 * Return value of ciVoidSavReqId
	 * 
	 * @return int 
	 */
	public int getVoidSavReqId()
	{
		return ciVoidSavReqId;
	}
	/**
	 * Get value of csWebSessionId
	 * 
	 * @return String
	 */
	public String getWebSessionId()
	{
		return csWebSessionId;
	}

	/** 
	 * Return boolean to denote if Accepted
	 * 
	 * @return boolean
	 */
	public boolean isAccepted()
	{
		return ciAccptVehIndi == 1;
	}

	/**
	 * Return boolean to denote that transaction is 
	 * Available for Void
	 * 
	 * @return boolean 
	 */
	public boolean isAvailableForVoid()
	{
		return !isVoided()
			&& isAccepted()
			&& (csBatchStatusCd.equals("O")
				|| csBatchStatusCd.equals("C")
				|| csBatchStatusCd.equals("S"));
	}

	/**
	 * Return boolean to denote that transaction is voided
	 * 
	 * @return boolean 
	 */
	private boolean isVoided()
	{
		return ciAgncyVoidIndi == 1 || ciCntyVoidIndi == 1;
	}

	/**
	 * Needs Transaction
	 * 
	 * @return boolean
	 */
	public boolean needsTrans()
	{
		boolean lbNeedsTrans = true;

		if (!UtilityMethods.isEmpty(getTransId()))
		{
			TransactionIdData laTransIdData =
				new TransactionIdData(getTransId());
			lbNeedsTrans =
				laTransIdData.getTransAMDate()
					!= new RTSDate().getAMDate();
		}
		return lbNeedsTrans;
	}

	/**
	 * Set value of caAccptTimestmp
	 * 
	 * @param aaAccptTimestmp
	 */
	public void setAccptTimestmp(RTSDate aaAccptTimestmp)
	{
		caAccptTimestmp = aaAccptTimestmp;
	}

	/**
	 * Set value of ciAccptVehIndi
	 * 
	 * @param aiAccptVehIndi
	 */
	public void setAccptVehIndi(int aiAccptVehIndi)
	{
		ciAccptVehIndi = aiAccptVehIndi;
	}

	/**
	 * Set value of ciAddlSetInidi
	 * 
	 * @param aiAddlSetInidi
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
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
	 * Set value of ciAgncyVoidIndi
	 * 
	 * @param aiAgncyVoidIndi
	 */
	public void setAgncyVoidIndi(int aiAgncyVoidIndi)
	{
		ciAgncyVoidIndi = aiAgncyVoidIndi;
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
	 * Set value of csAuditTrailTransId
	 * 
	 * @param asAuditTrailTransId
	 */
	public void setAuditTrailTransId(String asAuditTrailTransId)
	{
		csAuditTrailTransId = asAuditTrailTransId;
	}

	/**
	 * Set value of csBarCdVersionNo
	 * 
	 * @param asBarCdVersionNo
	 */
	public void setBarCdVersionNo(String asBarCdVersionNo)
	{
		csBarCdVersionNo = asBarCdVersionNo;
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
	 * Set value of ciCntyVoidIndi
	 * 
	 * @param aiCntyVoidIndi
	 */
	public void setCntyVoidIndi(int aiCntyVoidIndi)
	{
		ciCntyVoidIndi = aiCntyVoidIndi;
	}

	/**
	 * Set value of csDocNo
	 * 
	 * @param asDocNo
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * Set value of caInitReqTimestmp
	 * 
	 * @param aaInitReqTimestmp
	 */
	public void setInitReqTimestmp(RTSDate aaInitReqTimestmp)
	{
		caInitReqTimestmp = aaInitReqTimestmp;
	}

	/**
	 * Set value of csInsVerfdCd
	 * 
	 * @param asInsVerfdCd
	 */
	public void setInsVerfdCd(String asInsVerfdCd)
	{
		csInsVerfdCd = asInsVerfdCd;
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
	 * Set value of csKeyTypeCd
	 * 
	 * @param asKeyTypeCd
	 */
	public void setKeyTypeCd(String asKeyTypeCd)
	{
		csKeyTypeCd = asKeyTypeCd;
	}

	/**
	 * Set value of ciMustReplPltIndi
	 * 
	 * @param aiMustReplPltIndi
	 */
	public void setMustReplPltIndi(int aiMustReplPltIndi)
	{
		ciMustReplPltIndi = aiMustReplPltIndi;
	}

	/**
	 * Set value of ciNewPltExpMo
	 * 
	 * @param aiNewPltExpMo
	 */
	public void setNewPltExpMo(int aiNewPltExpMo)
	{
		ciNewPltExpMo = aiNewPltExpMo;
	}

	/**
	 * Set value of ciNewPltExpYr
	 * 
	 * @param aiNewPltExpYr
	 */
	public void setNewPltExpYr(int aiNewPltExpYr)
	{
		ciNewPltExpYr = aiNewPltExpYr;
	}

	/**
	 * Set value of ciNewRegExpMo
	 * 
	 * @param aiNewRegExpMo
	 */
	public void setNewRegExpMo(int aiNewRegExpMo)
	{
		ciNewRegExpMo = aiNewRegExpMo;
	}

	/**
	 * Set value of ciNewRegExpYr
	 * 
	 * @param aiNewRegExpYr
	 */
	public void setNewRegExpYr(int aiNewRegExpYr)
	{
		ciNewRegExpYr = aiNewRegExpYr;
	}

	/**
	 * Set value of csOrgNo
	 * 
	 * @param asOrgNo
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}

	/**
	 * @param asOwnrTtlName1 
	 */
	public void setOwnrTtlName1(String asOwnrTtlName1)
	{
		csOwnrTtlName1 = asOwnrTtlName1;
	}

	/**
	 * @param csOwnrTtlName2 
	 */
	public void setOwnrTtlName2(String asOwnrTtlName2)
	{
			csOwnrTtlName2 = asOwnrTtlName2;
	}

	/**
	 * Set value of ciPltAge 
	 * 
	 * @param aiPltAge
	 */
	public void setPltAge(int aiPltAge)
	{
		ciPltAge = aiPltAge;
	}

	/**
	 * Set value of ciPltBirthDate
	 * 
	 * @param aiPltBirthDate
	 */
	public void setPltBirthDate(int aiPltBirthDate)
	{
		ciPltBirthDate = aiPltBirthDate;
	}

	/**
	 * Set value of ciPltExpMo
	 * 
	 * @param aiPltExpMo
	 */
	public void setPltExpMo(int aiPltExpMo)
	{
		ciPltExpMo = aiPltExpMo;
	}

	/**
	 * Set value of ciPltExpYr
	 * 
	 * @param aiPltExpYr
	 */
	public void setPltExpYr(int aiPltExpYr)
	{
		ciPltExpYr = aiPltExpYr;
	}

	/**
	 * Set value of ciPltValidityTerm
	 * 
	 * @param aiPltValidityTerm
	 */
	public void setPltValidityTerm(int aiPltValidityTerm)
	{
		ciPltValidityTerm = aiPltValidityTerm;
	}

	/**
	 * Set value of ciPrntQty
	 * 
	 * @param aiPrntQty
	 */
	public void setPrntQty(int aiPrntQty)
	{
		ciPrntQty = aiPrntQty;
	}

	/**
	 * @param asRecpntName 
	 */
	public void setRecpntName(String asRecpntName)
	{
		csRecpntName = asRecpntName;
	}

	/**
	 * Set value of ciRegClassCd
	 * 
	 * @param aiRegClassCd
	 */
	public void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}

	/**
	 * Set value of ciRegExpMo
	 * 
	 * @param aiRegExpMo
	 */
	public void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}

	/**
	 * Set value of ciRegExpYr
	 * 
	 * @param aiRegExpYr
	 */
	public void setRegExpYr(int aiRegExpYr)
	{
		ciRegExpYr = aiRegExpYr;
	}

	/**
	 * Set value of csRegPltCd
	 * 
	 * @param asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Set value of csRegPltNo
	 * 
	 * @param asRegPltNo
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
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
	 * Set value of ciSavReqId
	 * 
	 * @param aiSavReqId
	 */
	public void setSavReqId(int aiSavReqId)
	{
		ciSavReqId = aiSavReqId;
	}

	/**
	 * Set value of csStkrItmCd
	 * 
	 * @param asStkrItmCd
	 */
	public void setStkrItmCd(String asStkrItmCd)
	{
		csStkrItmCd = asStkrItmCd;
	}

	/**
	 * Set value of ciSubconId
	 * 
	 * @param aiSubconId
	 */
	public void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}

	/**
	 * Set value of csTransId
	 * 
	 * @param asTransId
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * Set value of caTransTimestmp
	 * 
	 * @param aaTransTimestmp
	 */
	public void setTransTimestmp(RTSDate aaTransTimestmp)
	{
		caTransTimestmp = aaTransTimestmp;
	}

	/**
	 * Set value of ciVehGrossWt
	 * 
	 * @param aiVehGrossWt
	 */
	public void setVehGrossWt(int aiVehGrossWt)
	{
		ciVehGrossWt = aiVehGrossWt;
	}

	/**
	 * Set value of csVehMk
	 * 
	 * @param asVehMk
	 */
	public void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Set value of csVehModl
	 * 
	 * @param asVehModl
	 */
	public void setVehModl(String asVehModl)
	{
		csVehModl = asVehModl;
	}

	/**
	 * Set value of ciVehModlYr
	 * 
	 * @param aiVehModlYr
	 */
	public void setVehModlYr(int aiVehModlYr)
	{
		ciVehModlYr = aiVehModlYr;
	}

	/**
	 * Set value of csVIN
	 * 
	 * @param asVIN
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

	/**
	 * Set value of ciVoidSavReqId
	 * 
	 * @param aiVoidSavReqId
	 */
	public void setVoidSavReqId(int aiVoidSavReqId)
	{
		ciVoidSavReqId = aiVoidSavReqId;
	}

	/**
	 * Set value of csWebSessionId
	 * 
	 * @param asWebSessionId
	 */
	public void setWebSessionId(String asWebSessionId)
	{
		csWebSessionId = asWebSessionId;
	}

	/**
	 * @return the ciAgntIdntyNo
	 */
	public int getAgntIdntyNo()
	{
		return ciAgntIdntyNo;
	}

	/**
	 * @param aiAgntIdntyNo the AgntIdntyNo to set
	 */
	public void setAgntIdntyNo(int aiAgntIdntyNo)
	{
		this.ciAgntIdntyNo = aiAgntIdntyNo;
	}

	/**
	 * @return the csUserName
	 */
	public String getUserName()
	{
		return csUserName;
	}

	/**
	 * @param asUserName the UserName to set
	 */
	public void setUserName(String asUserName)
	{
		this.csUserName = asUserName;
	}

}
