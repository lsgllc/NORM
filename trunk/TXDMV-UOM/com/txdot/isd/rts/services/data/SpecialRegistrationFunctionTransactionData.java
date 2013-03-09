package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 *
 * SpecialRegistrationFunctionTransactionData.java
 *
 * (c) Texas Department of Transportation 2007
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/02/2007	Created
 *							defect 9805 Ver Special Plates 
 * J Rue		02/26/2007	Change SpclRegId from Dollar to long
 * 							modify getSpclRegId(), setSpclRegId()
 * 							defect 9086 Ver Special Plates
 * J Rue		04/03/2008	New fields for V21
 * 							add ciDissociateCd,ciV21PltId,
 * 								getDissociateCd(), setDissociateCd()
 * 								getV21PltId(), setV21PltId()
 * 							defect 9581 Ver 3_AMIGOS_PH_B
 * K Harrell	05/22/2008	remove ciV21PltId, get/set methods  
 * 						 	defect  9581 Ver 3 Amigos PH B
 * K Harrell	01/07/2009	Refactor ciRegExpMo/Yr, get/set methods.
 * 							refactor ciRegExpMo/Yr to ciPltExpMo/Yr
 * 							refactor RegExpMo/Yr get/set methods to 
 * 								PltExpMo/Yr get/set methods 
 * 							defect 9864 Ver Defect_POS_D
 * K Harrell	07/03/2009	add caPltOwnerData, set/get methods
 * 							delete csPltOwnrName1, csPltOwnrName2,
 * 							 csPltOwnrSt1, csPltOwnrSt2, csPltOwnrCity,
 * 							 csPltOwnrState, csPltOwnrZpCd, 
 * 							 csPltOwnrZpCd4, csPltOwnrCntry, get/set
 * 							 methods
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							modify getAttributes(), getPltOwnerData() 
 * 							defect 10191 Ver Defect_POS_G
 * K Harrell	02/16/2010	add csResrvReasnCd, ciMktngAllowdIndi, 
 * 							 ciAuctnPltIndi, caAuctnPdAmt, 
 * 							 ciPltValidityTerm, csItrntTraceNo,
 * 							 ciPltSoldMos  
 * 							  get/set methods
 * 							add add isMktngAllowd(), isAuctnPlt(), isISA() 
 * 							delete csSpclRegStkrNo, csVRIMSMfgCd 
 * 							 get/set methods 
 * 							defect 10366 Ver POS_640  
 * K Harrell	06/04/2010	assign new fields to SpecialPlateRegisData
 * 							for Void
 * 							modify getSpclPltRegisData()
 * 							defect 10500 Ver POS_640 
 * K Harrell 	06/14/2010 	add csTransId, get/set methods
 * 							defect 10505 Ver 6.5.0
 * K Harrell	06/15/2010	add ciElectionPndngIndi, get/set methods
 * 							modify getSpclPltRegisData()
 * 							defect 10507 Ver 6.5.0 
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * SpecialRegistrationFunctionTransactionData  
 * 
 * @version	6.5.0			06/15/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		02/02/2001 10:37:11
 */

public class SpecialRegistrationFunctionTransactionData
	implements Serializable, Displayable
{
	// int
	private int ciAddlSetIndi;
	private int ciAuctnPltIndi;
	private int ciCacheTransAMDate; // SendCache
	private int ciCacheTransTime; // SendCache 
	private int ciCustSeqNo;
	private int ciDissociateCd;
	private int ciISAIndi;
	private int ciMfgDate;
	private int ciMktngAllowdIndi;
	private int ciOfcIssuanceCd; // SendTrans 
	private int ciOfcIssuanceNo;
	private int ciPltExpMo;
	private int ciPltExpYr;
	private int ciPltSetNo;
	private int ciPltSoldMos;
	private int ciPltValidityTerm;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransTime;
	private int ciTransWsId;

	// defect 10507 
	private int ciElectionPndngIndi;
	// end defect 10507 

	// long 
	private long clSpclRegId;

	//	String
	private String csItrntTraceNo;
	private String csMfgPltNo;
	private String csMfgStatusCd;
	private String csOrgNo;
	private String csPltOwnrDist;
	private String csPltOwnrDlrGDN;
	private String csPltOwnrEMail;
	private String csPltOwnrOfcCd;
	private String csPltOwnrPhone;
	private String csRegPltCd;
	private String csRegPltNo;
	private String csResrvReasnCd;
	private String csSAuditTrailTransId;
	private String csSpclDocNo;
	private String csSpclRemks;
	private String csTransCd;
	private String csTransEmpId; // SendTrans

	//	defect 10505 
	private String csTransId;
	// end defect 10505 

	// Object 	
	private Dollar caAuctnPdAmt;
	private OwnerData caPltOwnerData;

	static final long serialVersionUID = -7629935608624117917L;

	/** 
	 * Constructor
	 */
	public SpecialRegistrationFunctionTransactionData()
	{
		super();
		caPltOwnerData = new OwnerData();
	}

	/**
	 * Return value of ciAddlSetIndi
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	/**
	 * Returns attributes for display in ShowCache
	 * 
	 * @return HashSet
	 */
	public Map getAttributes()
	{
		HashMap lhmHash = new HashMap();

		Field[] larrFields = this.getClass().getDeclaredFields();

		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				// defect 10191
				String lsFieldName = larrFields[i].getName();

				if (lsFieldName.equals("caPltOwnerData"))
				{
					UtilityMethods.addAttributesForObject(
						lsFieldName,
						getPltOwnerData().getAttributes(),
						lhmHash);
				}
				else
				{
					lhmHash.put(lsFieldName, larrFields[i].get(this));
				}
				// end defect 10191
			}
			catch (IllegalAccessException leIllAccEx)
			{
				continue;
			}
		}
		return lhmHash;
	}

	//	/**
	//	 * Set value of csVRIMSMfgCd
	//	 * 
	//	 * @param asVRIMSMfgCd
	//	 */
	//	public void setVRIMSMfgCd(String asVRIMSMfgCd)
	//	{
	//		csVRIMSMfgCd = asVRIMSMfgCd;
	//	}

	/**
	 * Get value of caAuctnPdAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getAuctnPdAmt()
	{
		return caAuctnPdAmt;
	}

	/**
	 * Get value of ciAuctnPltIndi
	 * 
	 * @return int
	 */
	public int getAuctnPltIndi()
	{
		return ciAuctnPltIndi;
	}

	/**
	 * Return value of ciCacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Return value of ciCacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}

	/**
	 * Return value of ciCustSeqNo
	 * 
	 * @return int
	 */
	public int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * get Dissociate Code
	 * 
	 * @return
	 */
	public int getDissociateCd()
	{
		return ciDissociateCd;
	}

	/**
	 * Get value of ciElectionPndngIndi
	 * 
	 * @return int
	 */
	public int getElectionPndngIndi()
	{
		return ciElectionPndngIndi;
	}

	/**
	 * Return value of ciISAIndi
	 * 
	 * @return int
	 */
	public int getISAIndi()
	{
		return ciISAIndi;
	}

	/**
	 * Get value of csItrntTraceNo
	 * 
	 * @return String
	 */
	public String getItrntTraceNo()
	{
		return csItrntTraceNo;
	}

	/**
	 * Return value of ciMfgDate
	 * 
	 * @return int
	 */
	public int getMfgDate()
	{
		return ciMfgDate;
	}

	/**
	 * Return value of csMfgPltNo
	 * 
	 * @return String
	 */
	public String getMfgPltNo()
	{
		return csMfgPltNo;
	}

	/**
	 * Return value of csMfgStatusCd
	 * 
	 * @return String
	 */
	public String getMfgStatusCd()
	{
		return csMfgStatusCd;
	}

	/**
	 * Get value of ciMktngAllowdIndi
	 * 
	 * @return int
	 */
	public int getMktngAllowdIndi()
	{
		return ciMktngAllowdIndi;
	}

	/**
	 * Return value of ciOfcIssuanceCd
	 * 
	 * @return int
	 */
	public int getOfcIssuanceCd()
	{
		return ciOfcIssuanceCd;
	}

	/**
	 * Return value of ciOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Return value of csOrgNo
	 * 
	 * @return String
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Return value of ciPltExpMo
	 * 
	 * @return int
	 */
	public int getPltExpMo()
	{
		return ciPltExpMo;
	}

	/**
	 * Return value of ciPltExpYr
	 * 
	 * @return int
	 */
	public int getPltExpYr()
	{
		return ciPltExpYr;
	}

	/**
	 * return Plate Owner Data 
	 * 
	 * @return OwnerData
	 */
	public OwnerData getPltOwnerData()
	{
		// defect 10191 
		if (caPltOwnerData == null)
		{
			setPltOwnerData(new OwnerData());
		}
		// end defect 10191 
		return caPltOwnerData;
	}
	//
	//	/**
	//	 * Return value of csPltOwnrCity
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrCity()
	//	{
	//		return csPltOwnrCity;
	//	}
	//
	//	/**
	//	 * Return value of csPltOwnrCntry
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrCntry()
	//	{
	//		return csPltOwnrCntry;
	//	}

	/**
	 * Return value of csPltOwnrDist
	 * 
	 * @return String
	 */
	public String getPltOwnrDist()
	{
		return csPltOwnrDist;
	}

	/**
	 * Return value of csPltOwnrDlrGDN
	 * 
	 * @return String
	 */
	public String getPltOwnrDlrGDN()
	{
		return csPltOwnrDlrGDN;
	}

	/**
	 * Return value of csPltOwnrEMail
	 * 
	 * @return String
	 */
	public String getPltOwnrEMail()
	{
		return csPltOwnrEMail;
	}

	//	/**
	//	 * Return value of csPltOwnrId
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrId()
	//	{
	//		return csPltOwnrId;
	//	}
	//
	//	/**
	//	 * Return value of csPltOwnrName1
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrName1()
	//	{
	//		return csPltOwnrName1;
	//	}
	//
	//	/**
	//	 * Return value of csPltOwnrName2
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrName2()
	//	{
	//		return csPltOwnrName2;
	//	}

	/**
	 * Return value of csPltOwnrOfcCd
	 * 
	 * @return String
	 */
	public String getPltOwnrOfcCd()
	{
		return csPltOwnrOfcCd;
	}

	/**
	 * Return value of csPltOwnrPhone
	 * 
	 * @return String
	 */
	public String getPltOwnrPhone()
	{
		return csPltOwnrPhone;
	}

	/**
	 * Return value of ciPltSetNo
	 * 
	 * @return int
	 */
	public int getPltSetNo()
	{
		return ciPltSetNo;
	}

	/**
	 * Return ciPltSoldMos
	 * 
	 * @return
	 */
	public int getPltSoldMos()
	{
		return ciPltSoldMos;
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

	//	/**
	//	 * Return value of getPltOwnrSt1
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrSt1()
	//	{
	//		return csPltOwnrSt1;
	//	}
	//
	//	/**
	//	 * Return value of csPltOwnrState
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrState()
	//	{
	//		return csPltOwnrState;
	//	}

	//	/**
	//	 * Return value of csPltOwnrZpCd
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrZpCd()
	//	{
	//		return csPltOwnrZpCd;
	//	}
	//
	//	/**
	//	 * Return value of csPltOwnrZpCP4
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrZpCdP4()
	//	{
	//		return csPltOwnrZpCdP4;
	//	}

	/**
	 * Return value of csRegPltCd
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Return value of csRegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Get value of csResrvReasnCd
	 * 
	 * @return String 
	 */
	public String getResrvReasnCd()
	{
		return csResrvReasnCd;
	}

	/**
	 * Return value of csSAuditTrailTransId
	 * 
	 * @return String
	 */
	public String getSAuditTrailTransId()
	{
		return csSAuditTrailTransId;
	}

	/**
	 * Return value of csSpclDocNo
	 * 
	 * @return String
	 */
	public String getSpclDocNo()
	{
		return csSpclDocNo;
	}

	/**
	 * Sets Special Plates Regis Data Objet to return to complete 
	 * Transaction
	 * 
	 * @return SpecialPlatesRegisData
	 */
	public SpecialPlatesRegisData getSpclPltRegisData()
	{
		SpecialPlatesRegisData laSpclPltRegisData =
			new SpecialPlatesRegisData();
		laSpclPltRegisData.setRegPltCd(getRegPltCd());
		laSpclPltRegisData.setRegPltNo(getRegPltNo());
		laSpclPltRegisData.setOrgNo(getOrgNo());
		laSpclPltRegisData.setMfgPltNo(getMfgPltNo());
		laSpclPltRegisData.setMFGDate(getMfgDate());
		laSpclPltRegisData.setMFGStatusCd(getMfgStatusCd());

		laSpclPltRegisData.setAddlSetIndi(getAddlSetIndi());
		laSpclPltRegisData.setISAIndi(getISAIndi());

		laSpclPltRegisData.setPltExpMo(getPltExpMo());
		laSpclPltRegisData.setPltExpYr(getPltExpYr());

		laSpclPltRegisData.setPltSetNo(getPltSetNo());
		laSpclPltRegisData.setPltOwnrDlrGDN(getPltOwnrDlrGDN());

		// Owner Data
		laSpclPltRegisData.setOwnrData(
			(OwnerData) UtilityMethods.copy(getPltOwnerData()));
		laSpclPltRegisData.setPltOwnrEMail(getPltOwnrEMail());
		laSpclPltRegisData.setPltOwnrPhoneNo(getPltOwnrPhone());

		// HQ Fields 
		laSpclPltRegisData.setPltOwnrOfcCd(getPltOwnrOfcCd());
		laSpclPltRegisData.setPltOwnrDist(getPltOwnrDist());
		laSpclPltRegisData.setSpclDocNo(getSpclDocNo());
		laSpclPltRegisData.setSpclRemks(getSpclRemks());

		// defect 10366 
		// Other
		//laSpclPltRegisData.setSpclRegStkrNo(getSpclRegStkrNo()); 
		//laSpclPltRegisData.setVRIMSMFGCd(getVRIMSMfgCd());
		// end defect 10366

		// defect 10500 
		laSpclPltRegisData.setSpclRegId(getSpclRegId());
		laSpclPltRegisData.setSAuditTrailTransId(
			getSAuditTrailTransId());
		laSpclPltRegisData.setResrvReasnCd(getResrvReasnCd());
		laSpclPltRegisData.setMktngAllowdIndi(getMktngAllowdIndi());
		laSpclPltRegisData.setAuctnPltIndi(getAuctnPltIndi());
		laSpclPltRegisData.setAuctnPdAmt(getAuctnPdAmt());
		laSpclPltRegisData.setPltValidityTerm(getPltValidityTerm());
		laSpclPltRegisData.setItrntTraceNo(getItrntTraceNo());
		laSpclPltRegisData.setNoMonthsToCharge(getPltSoldMos());
		// end defect 10500
		
		// defect 10507
		laSpclPltRegisData.setElectionPndngIndi(getElectionPndngIndi());
		// end defect 10507 
		return laSpclPltRegisData;
	}

	//	/**
	//	 * Returns value of csPltOwnrSt2
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrSt2()
	//	{
	//		return csPltOwnrSt2;
	//	}
	//
	//	/**
	//	 * Set value of csPltOwnrSt2 
	//	 * 
	//	 * @param asPltOwnrSt2
	//	 */
	//	public void setPltOwnrSt2(String asPltOwnrSt2)
	//	{
	//		csPltOwnrSt2 = asPltOwnrSt2;
	//	}

	/**
	 * Set value of clSpclRegId
	 * 
	 * @return long
	 */
	public long getSpclRegId()
	{
		return clSpclRegId;
	}

	//	/**
	//	 * Return value of csSpclRegStkrNo
	//	 * 
	//	 * @return String
	//	 */
	//	public String getSpclRegStkrNo()
	//	{
	//		return csSpclRegStkrNo;
	//	}

	/**
	 * Return value of csSpclRemks
	 * 
	 * @return String
	 */
	public String getSpclRemks()
	{
		return csSpclRemks;
	}

	/**
	 * Return value of ciSubstaId
	 * 
	 * @return int
	 */
	public int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Return value of ciTransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Return value of csTransCd
	 * 
	 * @return String
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Return value of csTransEmpId
	 * 
	 * @return String
	 */
	public String getTransEmpId()
	{
		return csTransEmpId;
	}

	/**
	* Returns the value of TransId
	* 
	* @return String 
	*/
	public String getTransId()
	{
		if (csTransId == null || csTransId.trim().length() == 0)
		{
			csTransId =
				UtilityMethods.getTransId(
					ciOfcIssuanceNo,
					ciTransWsId,
					ciTransAMDate,
					ciTransTime);
		}
		return csTransId;
	}

	/**
	 * Return value of ciTransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Return value of ciTransWsId
	 * 
	 * @return int
	 */
	public int getTransWsId()
	{
		return ciTransWsId;
	}

	//	/**
	//	 * Return value of csVRIMSMfgCd
	//	 * 
	//	 * @return String
	//	 */
	//	public String getVRIMSMfgCd()
	//	{
	//		return csVRIMSMfgCd;
	//	}

	/**
	 *Return boolean to denote if ciAuctnPltIndi = 1
	 * 
	 * @return boolean
	 */
	public boolean isAuctnPlt()
	{
		return ciAuctnPltIndi == 1;
	}

	/**
	 * Return boolean to denote if ciISAIndi = 1
	 * 
	 * @return boolean
	 */
	public boolean isISA()
	{
		return ciISAIndi == 1;
	}

	/**
	 * Return boolean to denote if ciMktngAllowdIndi = 1
	 * 
	 * @return boolean
	 */
	public boolean isMktngAllowd()
	{
		return ciMktngAllowdIndi == 1;
	}

	/**
	 * Set value of ciAddlSetIndi
	 * 
	 * @param aiAddlSetIndi
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}

	/**
	 * Set value of caAuctnPdAmt
	 * 
	 * @param aaAuctnPdAmt
	 */
	public void setAuctnPdAmt(Dollar aaAuctnPdAmt)
	{
		caAuctnPdAmt = aaAuctnPdAmt;
	}

	/**
	 * Set value of ciAuctnPltIndi
	 * 
	 * @param aiAuctnPltIndi
	 */
	public void setAuctnPltIndi(int aiAuctnPltIndi)
	{
		ciAuctnPltIndi = aiAuctnPltIndi;
	}

	/**
	 * Set value of ciCacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * Set value of ciCacheTransTime
	 * 
	 * @param aiCacheTransTime
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
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
	 * set Dissociate code
	 * 
	 * @param aiDissociatedCd
	 */
	public void setDissociateCd(int aiDissociateCd)
	{
		ciDissociateCd = aiDissociateCd;
	}

	/**
	 * Set value of ciElectionPndngIndi
	 * 
	 * @param aiElectionPndngIndi
	 */
	public void setElectionPndngIndi(int aiElectionPndngIndi)
	{
		ciElectionPndngIndi = aiElectionPndngIndi;
	}

	/**
	 * Set value of ciISAIndi
	 * 
	 * @param aiISAIndi
	 */
	public void setISAIndi(int aiISAIndi)
	{
		ciISAIndi = aiISAIndi;
	}

	/**
	 * Set value of csItrntTraceNo
	 * 
	 * @param asItrntTraceNo
	 */
	public void setItrntTraceNo(String asItrntTraceNo)
	{
		csItrntTraceNo = asItrntTraceNo;
	}

	/**
	 * Set value of ciMfgDate
	 * 
	 * @param aiMfgDate
	 */
	public void setMfgDate(int aiMfgDate)
	{
		ciMfgDate = aiMfgDate;
	}

	/**
	 * Set value of csMfgPltNo
	 * 
	 * @param asMfgPltNo
	 */
	public void setMfgPltNo(String asMfgPltNo)
	{
		csMfgPltNo = asMfgPltNo;
	}

	/**
	 * Set value of csMfgStatusCd
	 * 
	 * @param asMfgStatusCd
	 */
	public void setMfgStatusCd(String asMfgStatusCd)
	{
		csMfgStatusCd = asMfgStatusCd;
	}

	/**
	 * Set value of ciMktngAllowdIndi
	 * 
	 * @param aiMktngAllowdIndi
	 */
	public void setMktngAllowdIndi(int aiMktngAllowdIndi)
	{
		ciMktngAllowdIndi = aiMktngAllowdIndi;
	}

	/**
	 * Set value of ciOfcIssuanceCd
	 * 
	 * @param aiOfcIssuanceCd
	 */
	public void setOfcIssuanceCd(int aiOfcIssuanceCd)
	{
		ciOfcIssuanceCd = aiOfcIssuanceCd;
	}

	/**
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param i
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
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
	 * set Plate Owner Data
	 * 
	 * @param aaPltOwnerData
	 */
	public void setPltOwnerData(OwnerData aaPltOwnerData)
	{
		caPltOwnerData = aaPltOwnerData;
	}

	//	/**
	//	 * Set value of csPltOwnrCity
	//	 * 
	//	 * @param asPltOwnrCity
	//	 */
	//	public void setPltOwnrCity(String asPltOwnrCity)
	//	{
	//		csPltOwnrCity = asPltOwnrCity;
	//	}
	//
	//	/**
	//	 * Set value of csPltOwnrCntry
	//	 * 
	//	 * @param asPltOwnrCntry
	//	 */
	//	public void setPltOwnrCntry(String asPltOwnrCntry)
	//	{
	//		csPltOwnrCntry = asPltOwnrCntry;
	//	}

	/**
	 * Set value of asPltOwnrDist
	 * 
	 * @param asPltOwnrDist
	 */
	public void setPltOwnrDist(String asPltOwnrDist)
	{
		csPltOwnrDist = asPltOwnrDist;
	}

	/**
	 * Set value of csPltOwnrDlrGDN
	 * 
	 * @param asPltOwnrDlrGDN
	 */
	public void setPltOwnrDlrGDN(String asPltOwnrDlrGDN)
	{
		csPltOwnrDlrGDN = asPltOwnrDlrGDN;
	}

	/**
	 * Set value of csPltOwnrEMail
	 * 
	 * @param asPltOwnrEMail
	 */
	public void setPltOwnrEMail(String asPltOwnrEMail)
	{
		csPltOwnrEMail = asPltOwnrEMail;
	}

	//	/**
	//	 * Set value of csPltOwnrId
	//	 * 
	//	 * @param asPltOwnrId
	//	 */
	//	public void setPltOwnrId(String asPltOwnrId)
	//	{
	//		csPltOwnrId = asPltOwnrId;
	//	}
	//
	//	/**
	//	 * Set value of csPltOwnrName1
	//	 * 
	//	 * @param asPltOwnrName1
	//	 */
	//	public void setPltOwnrName1(String asPltOwnrName1)
	//	{
	//		csPltOwnrName1 = asPltOwnrName1;
	//	}
	//
	//	/**
	//	 * Set value of csPltOwnrName2
	//	 * 
	//	 * @param asPltOwnrName2
	//	 */
	//	public void setPltOwnrName2(String asPltOwnrName2)
	//	{
	//		csPltOwnrName2 = asPltOwnrName2;
	//	}

	/**
	 * Set value of 
	 * 
	 * @param asPltOwnrOfcCd
	 */
	public void setPltOwnrOfcCd(String asPltOwnrOfcCd)
	{
		csPltOwnrOfcCd = asPltOwnrOfcCd;
	}

	/**
	 * Set value of csPltOwnrPhone
	 * 
	 * @param asPltOwnrPhone
	 */
	public void setPltOwnrPhone(String asPltOwnrPhone)
	{
		csPltOwnrPhone = asPltOwnrPhone;
	}

	/**
	 * Set value of ciPltSetNo
	 * 
	 * @param aiPltSetNo
	 */
	public void setPltSetNo(int aiPltSetNo)
	{
		ciPltSetNo = aiPltSetNo;
	}

	/**
	 * Set value of ciPltSoldMos
	 * 
	 * @param aiPltSoldMos
	 */
	public void setPltSoldMos(int aiPltSoldMos)
	{
		ciPltSoldMos = aiPltSoldMos;
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

	//	/**
	//	 * Set value of csPltOwnrSt1
	//	 * 
	//	 * @param asPltOwnrSt1
	//	 */
	//	public void setPltOwnrSt1(String asPltOwnrSt1)
	//	{
	//		csPltOwnrSt1 = asPltOwnrSt1;
	//	}
	//
	//	/**
	//	 * Set value of csPltOwnrState
	//	 * 
	//	 * @param asPltOwnrState
	//	 */
	//	public void setPltOwnrState(String asPltOwnrState)
	//	{
	//		csPltOwnrState = asPltOwnrState;
	//	}
	//
	//	/**
	//	 * Set value of csPltOwnrZpCd
	//	 * 
	//	 * @param asPltOwnrZpCd
	//	 */
	//	public void setPltOwnrZpCd(String asPltOwnrZpCd)
	//	{
	//		csPltOwnrZpCd = asPltOwnrZpCd;
	//	}
	//
	//	/**
	//	 * Set value of csPltOwnrZpCdP4
	//	 * 
	//	 * @param asPltOwnrZpCdP4
	//	 */
	//	public void setPltOwnrZpCdP4(String asPltOwnrZpCdP4)
	//	{
	//		csPltOwnrZpCdP4 = asPltOwnrZpCdP4;
	//	}

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
	 * Set value of csResrvReasnCd
	 * 
	 * @param asResrvReasnCd
	 */
	public void setResrvReasnCd(String asResrvReasnCd)
	{
		csResrvReasnCd = asResrvReasnCd;
	}

	/**
	 * Set value of csSAuditTrailTransId
	 * 
	 * @param asSAuditTrailTransId
	 */
	public void setSAuditTrailTransId(String asSAuditTrailTransId)
	{
		csSAuditTrailTransId = asSAuditTrailTransId;
	}

	/**
	 * Set value of csSpclDocNo
	 * 
	 * @param asSpclDocNo
	 */
	public void setSpclDocNo(String asSpclDocNo)
	{
		csSpclDocNo = asSpclDocNo;
	}

	/**
	 * Get value of caSpclRegId
	 * 
	 * @param alSpclRegId
	 */
	public void setSpclRegId(long alSpclRegId)
	{
		clSpclRegId = alSpclRegId;

	}
	//
	//	/**
	//	 * Set value of csSpclRegStkrNo
	//	 * 
	//	 * @param asSpclRegStkrNo
	//	 */
	//	public void setSpclRegStkrNo(String asSpclRegStkrNo)
	//	{
	//		csSpclRegStkrNo = asSpclRegStkrNo;
	//	}

	/**
	 * Set value of csSpclRemks 
	 * 
	 * @param asSpclRemks
	 */
	public void setSpclRemks(String asSpclRemks)
	{
		csSpclRemks = asSpclRemks;
	}

	/**
	 * Set value of ciSubstaId
	 * 
	 * @param aiSubstaId
	 */
	public void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
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
	 * Set value of csTransCd
	 * 
	 * @param asTransCd
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
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
	 * Sets the value of TransId
	 * 
	 * @param asTransid  
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * Set value of ciTransTime
	 * 
	 * @param aiTransTime
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
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
