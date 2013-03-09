package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.webservices.trans.data.RtsTransRequestV1;

/*
 * WebServicesTransactionHistoryData.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/25/2010	Created
 * 							defect 10366 Ver POS_640
 * Ray Rowehl	04/12/2010	Add constructor for request data 
 * 							add WebServicesTransactionHistoryData(
 * 								int, RtsTransRequestV1)
 * 							defect 10401 Ver 6.4.0
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get and set methods for 
 * WebServicesTransactionHistoryData
 *
 * @version	6.4.0			04/12/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		03/25/2010 14:47:17
 */
public class WebServicesTransactionHistoryData
{
	private OwnerData caPltOwnrData;
	private Dollar caPymntAmt;
	private int ciAddlSetIndi;
	private int ciAuctnPltIndi;
	private int ciISAIndi;
	private int ciItrntPymntStatusCd;
	private int ciMktngAllowdIndi;
	private int ciPLPIndi;
	private int ciPltExpMo;
	private int ciPltExpYr;
	private int ciPltValidityTerm;
	private int ciResComptCntyNo;
	private int ciSavReqId;
	private long clSpclRegId;
	private String csItrntTraceNo;
	private String csMfgPltNo;
	private String csOrgNo;
	private String csPltOwnrEMail;
	private String csPltOwnrPhone;
	private String csPymntOrderId;
	private String csRegPltCd;
	private String csRegPltNo;
	private String csResrvReasnCd;
	private String csTransCd;

	/**
	 * WebServicesTransactionHistoryData Constructor
	 * 
	 */
	public WebServicesTransactionHistoryData()
	{
		super();
		caPltOwnrData = new OwnerData();
		caPltOwnrData.setAddressData(new AddressData());
		caPymntAmt = new Dollar(0); 
	}
	
	/**
	 * WebServicesTransactionHistoryData Constructor
	 * 
	 */
	public WebServicesTransactionHistoryData(int aiSavReqId, RtsTransRequestV1 aaTransReqV1)
	{
		this();
		caPltOwnrData.setOwnrId("");
		caPltOwnrData.setName1(aaTransReqV1.getPltOwnrName1());
		caPltOwnrData.setName2(aaTransReqV1.getPltOwnrName2());
		caPltOwnrData.getAddressData().setSt1(aaTransReqV1.getPltOwnrAddr().getStreetLine1());
		caPltOwnrData.getAddressData().setSt2(aaTransReqV1.getPltOwnrAddr().getStreetLine2());
		caPltOwnrData.getAddressData().setCity(aaTransReqV1.getPltOwnrAddr().getCity());
		caPltOwnrData.getAddressData().setState(aaTransReqV1.getPltOwnrAddr().getState());
		caPltOwnrData.getAddressData().setZpcd(aaTransReqV1.getPltOwnrAddr().getZip());
		caPltOwnrData.getAddressData().setZpcdp4(aaTransReqV1.getPltOwnrAddr().getZipP4());
		caPymntAmt = new Dollar(aaTransReqV1.getPymntAmt().doubleValue());
		ciAddlSetIndi = aaTransReqV1.getAddlSetIndi();
		ciAuctnPltIndi = (aaTransReqV1.isAuctnPltIndi() ? 1 : 0);
		ciISAIndi = (aaTransReqV1.isIsa() ? 1 : 0);
		ciItrntPymntStatusCd = aaTransReqV1.getItrntPymntStatusCd();
		ciMktngAllowdIndi = (aaTransReqV1.isMktngAllowdIndi() ? 1 : 0);
		ciPLPIndi = (aaTransReqV1.isPlp() ? 1 : 0);
		ciPltExpMo = aaTransReqV1.getPltExpMo();
		ciPltExpYr = aaTransReqV1.getPltExpYr();
		ciPltValidityTerm = aaTransReqV1.getPltValidityTerm();
		ciResComptCntyNo = aaTransReqV1.getResComptCntyNo();
		ciSavReqId = aiSavReqId;
		clSpclRegId = aaTransReqV1.getSpclRegId();
		csItrntTraceNo = aaTransReqV1.getItrntTraceNo();
		csMfgPltNo = aaTransReqV1.getMfgPltNo();
		csOrgNo = aaTransReqV1.getOrgNo();
		csPltOwnrEMail = aaTransReqV1.getPltOwnrEmailAddr();
		csPltOwnrPhone = aaTransReqV1.getPltOwnrPhone();
		csPymntOrderId = aaTransReqV1.getPymntOrderId();
		csRegPltCd = aaTransReqV1.getPltCd();
		csRegPltNo = aaTransReqV1.getPltNo();
		csResrvReasnCd = aaTransReqV1.getReservReasnCd();
		csTransCd = aaTransReqV1.getTransCode();
	}
	

	/**
	 * Get AbstractValue of ciAddlSetIndi
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}

	/**
	 * Get AbstractValue of ciAuctnPltIndi
	 * 
	 * @return int
	 */
	public int getAuctnPltIndi()
	{
		return ciAuctnPltIndi;
	}

	/**
	 * Get AbstractValue of ciISAIndi
	 * 
	 * @return int
	 */
	public int getISAIndi()
	{
		return ciISAIndi;
	}

	/**
	 * Get AbstractValue of ciItrntPymntStatusCd
	 * 
	 * @return int
	 */
	public int getItrntPymntStatusCd()
	{
		return ciItrntPymntStatusCd;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public String getItrntTraceNo()
	{
		return csItrntTraceNo;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public String getMfgPltNo()
	{
		return csMfgPltNo;
	}

	/**
	 * Get AbstractValue of ciMktngAllowdIndi
	 * 
	 * @return int
	 */
	public int getMktngAllowdIndi()
	{
		return ciMktngAllowdIndi;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}

	/**
	 * Get AbstractValue of ciPLPIndi
	 * 
	 * @return int
	 */
	public int getPLPIndi()
	{
		return ciPLPIndi;
	}

	/**
	 * Get AbstractValue of ciPltExpMo
	 * 
	 * @return int
	 */
	public int getPltExpMo()
	{
		return ciPltExpMo;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public int getPltExpYr()
	{
		return ciPltExpYr;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public OwnerData getPltOwnrData()
	{
		return caPltOwnrData;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public String getPltOwnrEMail()
	{
		return csPltOwnrEMail;
	}

	/**
	 * Get AbstractValue of csPltOwnrPhone
	 * 
	 * @return String 
	 */
	public String getPltOwnrPhone()
	{
		return csPltOwnrPhone;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public int getPltValidityTerm()
	{
		return ciPltValidityTerm;
	}
	
	/**
	 * Get AbstractValue of caPymntAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getPymntAmt()
	{
		return caPymntAmt;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public String getPymntOrderId()
	{
		return csPymntOrderId;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public String getResrvReasnCd()
	{
		return csResrvReasnCd;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public int getSavReqId()
	{
		return ciSavReqId;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public long getSpclRegId()
	{
		return clSpclRegId;
	}

	/**
	 * Get AbstractValue of
	 * 
	 * @return 
	 */
	public String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Get AbstractValue of ciAddlSetIndi
	 * 
	 * @param aiAddlSetIndi
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}

	/**
	 * Get AbstractValue of ciAuctnPltIndi
	 * 
	 * @param aiAuctnPltIndi
	 */
	public void setAuctnPltIndi(int aiAuctnPltIndi)
	{
		ciAuctnPltIndi = aiAuctnPltIndi;
	}

	/**
	 * Get AbstractValue of ciISAIndi
	 * 
	 * @param aiISAIndi
	 */
	public void setISAIndi(int aiISAIndi)
	{
		ciISAIndi = aiISAIndi;
	}

	/**
	 * Get AbstractValue of ciItrntPymntStatusCd
	 * 
	 * @param aiItrntPymntStatusCd
	 */
	public void setItrntPymntStatusCd(int aiItrntPymntStatusCd)
	{
		ciItrntPymntStatusCd = aiItrntPymntStatusCd;
	}

	/**
	 * Get AbstractValue of csItrntTraceNo
	 * 
	 * @param asItrntTraceNo
	 */
	public void setItrntTraceNo(String asItrntTraceNo)
	{
		csItrntTraceNo = asItrntTraceNo;
	}

	/**
	 * Get AbstractValue of csMfgPltNo
	 * 
	 * @param asMfgPltNo
	 */
	public void setMfgPltNo(String asMfgPltNo)
	{
		csMfgPltNo = asMfgPltNo;
	}

	/**
	 * Get AbstractValue of aiMktngAllowdIndi
	 * 
	 * @param ciMktngAllowdIndi
	 */
	public void setMktngAllowdIndi(int aiMktngAllowdIndi)
	{
		ciMktngAllowdIndi = aiMktngAllowdIndi;
	}

	/**
	 * Get AbstractValue of csOrgNo
	 * 
	 * @param asOrgNo
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}

	/**
	 * Get AbstractValue of ciPLPIndi
	 * 
	 * @param aiPLPIndi
	 */
	public void setPLPIndi(int aiPLPIndi)
	{
		ciPLPIndi = aiPLPIndi;
	}

	/**
	 * Get AbstractValue of ciPltExpMo
	 * 
	 * @param aiPltExpMo
	 */
	public void setPltExpMo(int aiPltExpMo)
	{
		ciPltExpMo = aiPltExpMo;
	}

	/**
	 * Get AbstractValue of ciPltExpYr
	 * 
	 * @param aiPltExpYr
	 */
	public void setPltExpYr(int aiPltExpYr)
	{
		ciPltExpYr = aiPltExpYr;
	}

	/**
	 * Get AbstractValue of caPltOwnrData
	 * 
	 * @param aaPltOwnrData
	 */
	public void setPltOwnrData(OwnerData aaPltOwnrData)
	{
		caPltOwnrData = aaPltOwnrData;
	}

	/**
	 * Get AbstractValue of csPltOwnrEMail
	 * 
	 * @param asPltOwnrEMail
	 */
	public void setPltOwnrEMail(String asPltOwnrEMail)
	{
		csPltOwnrEMail = asPltOwnrEMail;
	}

	/**
	 * Get AbstractValue of csPltOwnrPhone
	 * 
	 * @param asPltOwnrPhone
	 */
	public void setPltOwnrPhone(String asPltOwnrPhone)
	{
		csPltOwnrPhone = asPltOwnrPhone;
	}

	/**
	 * Get AbstractValue of ciPltValidityTerm
	 * 
	 * @param aiPltValidityTerm
	 */
	public void setPltValidityTerm(int aiPltValidityTerm)
	{
		ciPltValidityTerm = aiPltValidityTerm;
	}

	/**
	 * Get AbstractValue of caPymntAmt
	 * 
	 * @param aaPymntAmt
	 */
	public void setPymntAmt(Dollar aaPymntAmt)
	{
		caPymntAmt = aaPymntAmt;
	}

	/**
	 * Get AbstractValue of csPymntOrderId
	 * 
	 * @param asPymntOrderId
	 */
	public void setPymntOrderId(String asPymntOrderId)
	{
		csPymntOrderId = asPymntOrderId;
	}

	/**
	 * Get AbstractValue of csRegPltCd
	 * 
	 * @param asRegPltCd
	 */
	public void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * Get AbstractValue of csRegPltNo
	 * 
	 * @param asRegPltNo
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * Get AbstractValue of ciResComptCntyNo
	 * 
	 * @param aiResComptCntyNo
	 */
	public void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * Get AbstractValue of csResrvReasnCd
	 * 
	 * @param asResrvReasnCd
	 */
	public void setResrvReasnCd(String asResrvReasnCd)
	{
		csResrvReasnCd = asResrvReasnCd;
	}

	/**
	 * Get AbstractValue of ciSavReqId
	 * 
	 * @param aiSavReqId
	 */
	public void setSavReqId(int aiSavReqId)
	{
		ciSavReqId = aiSavReqId;
	}

	/**
	 * Get AbstractValue of clSpclRegId
	 * 
	 * @param alSpclRegId
	 */
	public void setSpclRegId(long alSpclRegId)
	{
		clSpclRegId = alSpclRegId;
	}

	/**
	 * Get AbstractValue of csTransCd
	 * 
	 * @param asTransCd
	 */
	public void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

}
