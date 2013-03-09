package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * SpecialPlateTransactionHistoryData.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/06/2007	Created
 *							defect 9805 Ver Special Plates
 * K Harrell	05/23/2007	Added csItrntTraceNo, get/set methods
 * 							defect 9085 Ver Special Plates 
 * K Harrell	01/07/2009	Use PltExpMo/Yr vs. RegExpMo/Yr	
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
 * J Zwiener	08/25/2009	Add new fields to Spcl_Plt_Trans_Hstry
 * 							add ciPriorPltExpMo, ciPriorPltExpYr,
 * 							 cdPltFee, cdPLPFee, get/set methods
 * 							defect 10097 Ver Defect_POS_F
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							add SpecialPlateTransactionHistoryData() 
 * 							modify getAttributes()
 * 							defect 10191 Ver Defect_POS_G 
 * K Harrell	02/16/2010	add csResrvReasnCd, ciMktngAllowdIndi, 
 * 							 ciAuctnPltIndi, caAuctnPdAmt, 
 * 							 ciPltValidityTerm, get/set methods.
 * 							add isMktngAllowd(), isAuctnPlt(), isISA(),
 * 							 isVoidedTrans(), isTransComplete() 
 * 							defect 10366 Ver POS_640
 * K Harrell 	06/14/2010 	add csTransId, get/set methods
 * 							defect 10505 Ver 6.5.0
 * K Harrell	01/24/2011 	add SpecialPlateTransactionHistoryData(TransactionHeaderData)
 * 							defect 10734 Ver 6.7.0   
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get and set methods for 
 * SpecialPlateTransactionHistoryData
 *
 * @version	6.7.0			01/24/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		02/06/2007	17:12:00
 */

public class SpecialPlateTransactionHistoryData
	implements Serializable, Displayable
{
	private int ciCacheTransAMDate; // SendCache
	private int ciCacheTransTime; // SendCache
	private int ciChrgSpclPltFeeIndi;
	private int ciCustSeqNo;
	private int ciISAIndi;
	private int ciOfcIssuanceNo;
	private int ciPltExpMo;
	private int ciPltExpYr;
	private int ciPriorPltExpMo;
	private int ciPriorPltExpYr;
	private int ciResComptCntyNo;
	private int ciSubstaId;
	private int ciTransAMDate;
	private int ciTransCompleteIndi;
	private int ciTransTime;
	private int ciTransWsId;
	private int ciVoidedTransIndi;

	private String csItrntTraceNo;
	private String csMfgPltNo;
	private String csOrgNo;
	private String csPltOwnrEMail;
	private String csPltOwnrPhone;
	private String csRegPltCd;
	private String csRegPltNo;
	private String csTransCd;
	private String csTransEmpId;

	private Dollar cdPLPFee;
	private Dollar cdPltFee;

	// defect 10112 
	private OwnerData caPltOwnerData;
	// end defect 10112

	// defect 10366
	private int ciAuctnPltIndi;
	private int ciMktngAllowdIndi;
	private int ciPltValidityTerm;
	private String csResrvReasnCd;
	private Dollar caAuctnPdAmt;
	// end defect 10366 

	// defect 10505 
	private String csTransId;
	// end defect 10505

	static final long serialVersionUID = -308610791546962957L;

	/**
	 * Constructor
	 */
	public SpecialPlateTransactionHistoryData()
	{
		super();
		caPltOwnerData = new OwnerData();
	}

	/**
	 * Constructor
	 */
	public SpecialPlateTransactionHistoryData(TransactionHeaderData aaTransHdrData)
	{
		super();
		ciOfcIssuanceNo = aaTransHdrData.getOfcIssuanceNo();
		ciSubstaId = aaTransHdrData.getSubstaId();  
		ciTransAMDate = aaTransHdrData.getTransAMDate();
		ciCustSeqNo = aaTransHdrData.getCustSeqNo(); 
		ciTransWsId = aaTransHdrData.getTransWsId(); 
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
	 * Return value of ciChrgSpclPltFeeIndi
	 * 
	 * @return int
	 */
	public int getChrgSpclPltFeeIndi()
	{
		return ciChrgSpclPltFeeIndi;
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
	 * Return value of ciISAIndi
	 * 
	 * @return int
	 */
	public int getISAIndi()
	{
		return ciISAIndi;
	}

	/**
	 * Return value of csItrntTraceNo
	 * 
	 * @return String 
	 */
	public String getItrntTraceNo()
	{
		return csItrntTraceNo;
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
	 * Get value of ciMktngAllowdIndi
	 * 
	 * @return int
	 */
	public int getMktngAllowdIndi()
	{
		return ciMktngAllowdIndi;
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
	 * Return value of cdPLPFee
	 * 
	 * @return dollar
	 */
	public Dollar getPLPFee()
	{
		return cdPLPFee;
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
	 * Return value of cdPltFee
	 * 
	 * @return dollar
	 */
	public Dollar getPltFee()
	{
		return cdPltFee;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public OwnerData getPltOwnerData()
	{
		if (caPltOwnerData == null)
		{
			caPltOwnerData = new OwnerData();
		}
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
	 * Return value of csPltOwnrEMail
	 * 
	 * @return String
	 */
	public String getPltOwnrEMail()
	{
		return csPltOwnrEMail;
	}
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
	 * Return value of csPltOwnrPhone
	 * 
	 * @return String
	 */
	public String getPltOwnrPhone()
	{
		return csPltOwnrPhone;
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
	 * Return value of ciPriorPltExpMo
	 * 
	 * @return int
	 */
	public int getPriorPltExpMo()
	{
		return ciPriorPltExpMo;
	}

	/**
	 * Return value of ciPriorPltExpYr
	 * 
	 * @return int
	 */
	public int getPriorPltExpYr()
	{
		return ciPriorPltExpYr;
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
	//
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
	//	 * Return value of csPltOwnrZpCd4
	//	 * 
	//	 * @return String
	//	 */
	//	public String getPltOwnrZpCd4()
	//	{
	//		return csPltOwnrZpCd4;
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
	 * Set value of ciResComptCntyNo
	 * 
	 * @return int
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
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
	 * Return value of ciTransCompleteIndi
	 * 
	 * @return int
	 */
	public int getTransCompleteIndi()
	{
		return ciTransCompleteIndi;
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

	/**
	 * Return value of ciVoidedTransIndi 
	 * 
	 * @return int
	 */
	public int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}

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
	 * Return boolean to denote if ciTransCompleteIndi = 1
	 * 
	 * @return boolean
	 */
	public boolean isTransComplete()
	{
		return ciTransCompleteIndi == 1;
	}

	/**
	 * Return boolean to denote if ciVoidedTransIndi = 1
	 * 
	 * @return boolean
	 */
	public boolean isVoidedTrans()
	{
		return ciVoidedTransIndi == 1;
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
	 * Set value of ciChrgSpclPltFeeIndi
	 * 
	 * @param aiChrgSpclPltFeeIndi
	 */
	public void setChrgSpclPltFeeIndi(int aiChrgSpclPltFeeIndi)
	{
		ciChrgSpclPltFeeIndi = aiChrgSpclPltFeeIndi;
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
	 * Set value of csMfgPltNo
	 * 
	 * @param asMfgPltNo
	 */
	public void setMfgPltNo(String asMfgPltNo)
	{
		csMfgPltNo = asMfgPltNo;
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
	 * Set value of ciOfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo
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
	 * Set value of cdPLPFee
	 * 
	 * @param adPLPFee
	 */
	public void setPLPFee(Dollar adPLPFee)
	{
		cdPLPFee = adPLPFee;
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
	 * Set value of cdPltFee
	 * 
	 * @param adPLPFee
	 */
	public void setPltFee(Dollar adPLPFee)
	{
		cdPltFee = adPLPFee;
	}

	/**
	 * Method description
	 * 
	 * @param data
	 */
	public void setPltOwnerData(OwnerData data)
	{
		caPltOwnerData = data;
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
	 * Set value of csPltOwnrEMail
	 * 
	 * @param asPltOwnrEMail
	 */
	public void setPltOwnrEMail(String asPltOwnrEMail)
	{
		csPltOwnrEMail = asPltOwnrEMail;
	}

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
	 * Set value of csPltOwnrPhone
	 * 
	 * @param asPltOwnrPhone
	 */
	public void setPltOwnrPhone(String asPltOwnrPhone)
	{
		csPltOwnrPhone = asPltOwnrPhone;
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
	 * Set value of ciPriorPltExpMo
	 * 
	 * @param aiPriorPltExpMo
	 */
	public void setPriorPltExpMo(int aiPriorPltExpMo)
	{
		ciPriorPltExpMo = aiPriorPltExpMo;
	}

	/**
	 * Set value of ciPriorPltExpYr
	 * 
	 * @param ciPriorPltExpYr
	 */
	public void setPriorPltExpYr(int aiPriorPltExpYr)
	{
		ciPriorPltExpYr = aiPriorPltExpYr;
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
	//	 * Set value of csPltOwnrZpCd4
	//	 * 
	//	 * @param asPltOwnrZpCd4
	//	 */
	//	public void setPltOwnrZpCd4(String asPltOwnrZpCd4)
	//	{
	//		csPltOwnrZpCd4 = asPltOwnrZpCd4;
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
	 * Set value of ciResComptCntyNo
	 * 
	 * @param aiResComptCntyNo
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
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
	 * Set value of ciTransCompleteIndi  
	 * 
	 * @param aiTransCompleteIndi
	 */
	public void setTransCompleteIndi(int aiTransCompleteIndi)
	{
		ciTransCompleteIndi = aiTransCompleteIndi;
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

	/**
	 * Set value of ciVoidedTransIndi
	 * 
	 * @param aiVoidedTransIndi
	 */
	public void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}
}
