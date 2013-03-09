package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * VehMiscData.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	10/02/2006	for TERP
 * 							add caEmissionSalesTax 
 * 							add getEmissionSalesTax(),
 * 							setEmissionSalesTax()
 * 							modify VehMiscData() 
 * 							defect 8903 Ver Exempts 
 * K Harrell	05/21/2008	add ciV21VTNId, csVTNSource, 
 * 							 csTtlTrnsfrEntCd, get/set methods
 * 							defect 9582, 9583 Ver Defect POS A 
 * K Harrell	05/25/2008	add caTtlTrnsfrPnltyFee, csTtlTrnsfrEntCd,
 * 							 get/set methods
 * 							defect 9584 Ver Defect POS A
 * K Harrell	06/20/2008	add csTtlTrnsfrPnltyExmptCd,
 * 							 get/set methods
 * 							defect 9584 Ver Defect POS A 
 * ---------------------------------------------------------------------
 */

/**
 * Data class for Miscellaneous Vehicle Data
 *
 * @version	Defect POS A	05/25/2008  
 * @author	Marx Rajangam
 * <br>Creation Date:		10/03/2001 17:36:15
 */

public class VehMiscData implements java.io.Serializable
{

	private final static long serialVersionUID = 1268201557385795409L;

	// Object
	// defect 8903
	private Dollar caEmissionSalesTax;
	// end defect 8903 
	private Dollar caTaxPdOthrState;
	private Dollar caTotalRebateAmt;
	private Dollar caVehTaxAmt;

	// int
	private int ciAddlTradeInIndi;
	private int ciBatchCount;
	private int ciComptCntyNo;
	private int ciMfDwnCd;
	private int ciNoChrgSalTaxEmiFeeIndi;
	private int ciOwnrSuppliedExpYr;
	private int ciSalesTaxDate;
	private int ciSalesTaxExmptCd;
	private int ciSalesTaxPnltyPer;
	private int ciSpclPltProgIndi;
	private int ciSubconID;
	private int ciTradeInVehYr;
	private int ciTtlApplDate;

	// long 
	private long clIMCNo; //Tax Permit Number

	// String
	private String csAuthCd;
	private String csDlrGDN;
	private String csOwnrSuppliedPltNo;
	private String csOwnrSuppliedStkrNo;
	private String csSalesTaxCat;
	private String csSupvOvride;
	private String csSupvOvrideReason;
	private String csTradeInVehMk;
	private String csTradeInVehVIN;
	private String csTtlTrnsfrPnltyExmptCd;

	// defect 9582 
	private int ciV21VTNId;
	private String csVTNSource;
	// end defect 9582

	// defect 9583 
	private Dollar caTtlTrnsfrPnltyFee;
	private String csTtlTrnsfrEntCd;
	// end defect 9583 

	/**
	 * VehMiscData constructor comment.
	 */
	public VehMiscData()
	{
		super();
		caTaxPdOthrState = new Dollar(0.0);
		caTotalRebateAmt = new Dollar(0.0);
		caVehTaxAmt = new Dollar(0.0);
		// defect 8903
		caEmissionSalesTax = new Dollar(0.0);
		// end defect 8903 
		ciNoChrgSalTaxEmiFeeIndi = 1;
	}

	/**
	 * Return the value of AddlTradeInIndi
	 * 
	 * @return int
	 */
	public int getAddlTradeInIndi()
	{
		return ciAddlTradeInIndi;
	}

	/**
	 * Return the value of AuthCd
	 * 
	 * @return String
	 */
	public String getAuthCd()
	{
		return csAuthCd;
	}

	/**
	 * Return the value of BatchCount
	 * 
	 * @return int
	 */
	public int getBatchCount()
	{
		return ciBatchCount;
	}

	/**
	 * Return the value of ComptCntyNo
	 * 
	 * @return int
	 */
	public int getComptCntyNo()
	{
		return ciComptCntyNo;
	}

	/**
	 * Return the value of DlrGDN
	 * 
	 * @return String
	 */
	public String getDlrGDN()
	{
		return csDlrGDN;
	}

	/**
	 * Return the value of EmissionSalesTax
	 * 
	 * @return Dollar
	 */
	public Dollar getEmissionSalesTax()
	{
		return caEmissionSalesTax;
	}

	/**
	 * Return the value of IMCNo
	 * 
	 * @return long
	 */
	public long getIMCNo()
	{
		return clIMCNo;
	}

	/**
	 * Return the value of MfDwnCd
	 * 
	 * @return int
	 */
	public int getMfDwnCd()
	{
		return ciMfDwnCd;
	}

	/**
	 * Return the value of NoChrgSalTaxEmiFeeIndi
	 * 
	 * @return int
	 */
	public int getNoChrgSalTaxEmiFeeIndi()
	{
		return ciNoChrgSalTaxEmiFeeIndi;
	}

	/**
	 * Return the value of OwnrSuppliedExpYr
	 * 
	 * @return int
	 */
	public int getOwnrSuppliedExpYr()
	{
		return ciOwnrSuppliedExpYr;
	}

	/**
	 * Return the value of OwnrSuppliedPltNo
	 * 
	 * @return String
	 */
	public String getOwnrSuppliedPltNo()
	{
		return csOwnrSuppliedPltNo;
	}

	/**
	 * Return the value of OwnrSuppliedStkrNo
	 * 
	 * @return String
	 */
	public String getOwnrSuppliedStkrNo()
	{
		return csOwnrSuppliedStkrNo;
	}

	/**
	 * Return the value of SalesTaxCat 
	 * 
	 * @return String
	 */
	public String getSalesTaxCat()
	{
		return csSalesTaxCat;
	}

	/**
	 * Return the value of SalesTaxDate
	 * 
	 * @return int
	 */
	public int getSalesTaxDate()
	{
		return ciSalesTaxDate;
	}

	/**
	 * Return the value of SalesTaxExmptCd
	 * 
	 * @return int
	 */
	public int getSalesTaxExmptCd()
	{
		return ciSalesTaxExmptCd;
	}

	/**
	 * Return the value of SalesTaxPnltyPer
	 * 
	 * @return int
	 */
	public int getSalesTaxPnltyPer()
	{
		return ciSalesTaxPnltyPer;
	}

	/**
	 * Return the value of SpclPltProgIndi 
	 * 
	 * @return int
	 */
	public int getSpclPltProgIndi()
	{
		return ciSpclPltProgIndi;
	}

	/**
	 * Return the value of SubconID
	 * 
	 * @return int
	 */
	public int getSubconID()
	{
		return ciSubconID;
	}

	/**
	 * Return the value of SupvOvride
	 * 
	 * @return String
	 */
	public String getSupvOvride()
	{
		return csSupvOvride;
	}

	/**
	 * Return the value of SupvOvrideReason
	 * 
	 * @return String
	 */
	public String getSupvOvrideReason()
	{
		return csSupvOvrideReason;
	}

	/**
	 * Return the value of TaxPdOthrState
	 * 
	 * @return Dollar
	 */
	public Dollar getTaxPdOthrState()
	{
		return caTaxPdOthrState;
	}

	/**
	 * Return the value of TotalRebateAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getTotalRebateAmt()
	{
		return caTotalRebateAmt;
	}

	/**
	 * Return the value of TradeInVehMk
	 * 
	 * @return String
	 */
	public String getTradeInVehMk()
	{
		return csTradeInVehMk;
	}

	/**
	 * Return the value of TradeInVehVIN
	 * 
	 * @return String
	 */
	public String getTradeInVehVIN()
	{
		return csTradeInVehVIN;
	}

	/**
	 * Return the value of TradeInVehYr
	 * 
	 * @return int
	 */
	public int getTradeInVehYr()
	{
		return ciTradeInVehYr;
	}

	/**
	 * Return the value of TtlApplDate
	 * 
	 * @return int
	 */
	public int getTtlApplDate()
	{
		return ciTtlApplDate;
	}

	/**
	 * set value of csTtlTrnsfrEntCd
	 * 
	 * @return String
	 */
	public String getTtlTrnsfrEntCd()
	{
		return csTtlTrnsfrEntCd;
	}

	/**
	 * Return the value of VehTaxAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getVehTaxAmt()
	{
		return caVehTaxAmt;
	}

	/**
	 * Return AbstractValue of csVTNSource
	 * 
	 * @return String
	 */
	public String getVTNSource()
	{
		return csVTNSource;
	}

	/**
	 * Return AbstractValue of ciV21VTNId
	 * 
	 * @return int
	 */
	public int getV21VTNId()
	{
		return ciV21VTNId;
	}

	/**
	 * Set the  value of AddlTradeInIndi
	 * 
	 * @param aiAddlTradeInIndi int
	 */
	public void setAddlTradeInIndi(int aiAddlTradeInIndi)
	{
		ciAddlTradeInIndi = aiAddlTradeInIndi;
	}

	/**
	 * Set the  value of AuthCd
	 * 
	 * @param asAuthCd String
	 */
	public void setAuthCd(String asAuthCd)
	{
		csAuthCd = asAuthCd;
	}

	/**
	 * Set the  value of BatchCount
	 * 
	 * @param aiBatchCount int
	 */
	public void setBatchCount(int aiBatchCount)
	{
		ciBatchCount = aiBatchCount;
	}

	/**
	 * Set the  value of 		ciComptCntyNo
	 * 
	 * @param aiComptCntyNo int
	 */
	public void setComptCntyNo(int aiComptCntyNo)
	{
		ciComptCntyNo = aiComptCntyNo;
	}

	/**
	 * Set the  value of DlrGDN
	 * 
	 * @param asDlrGDN String
	 */
	public void setDlrGDN(String asDlrGDN)
	{
		csDlrGDN = asDlrGDN;
	}

	/**
	 * Set the value of EmissionSalesTax
	 * 
	 * @param aaEmissionSalesTax Dollar 
	 */
	public void setEmissionSalesTax(Dollar aaEmissionSalesTax)
	{
		caEmissionSalesTax = aaEmissionSalesTax;
	}

	/**
	 * Set the  value of IMCNo
	 * 
	 * @param alIMCNo long
	 */
	public void setIMCNo(long alIMCNo)
	{
		clIMCNo = alIMCNo;
	}

	/**
	 * Set the  value of MfDwnCd
	 * 
	 * @param aiMfDwnCd int
	 */
	public void setMfDwnCd(int aiMfDwnCd)
	{
		ciMfDwnCd = aiMfDwnCd;
	}

	/**
	 * Set the  value of NoChrgSalTaxEmiFeeIndi
	 * 
	 * @param aiChrgSalTaxEmiFee int
	 */
	public void setNoChrgSalTaxEmiFeeIndi(int aiNoChrgSalTaxEmiFee)
	{
		ciNoChrgSalTaxEmiFeeIndi = aiNoChrgSalTaxEmiFee;
	}

	/**
	 * Set the  value of OwnrSuppliedExpYr
	 * 
	 * @param aiOwnrSuppliedExpYr int
	 */
	public void setOwnrSuppliedExpYr(int aiOwnrSuppliedExpYr)
	{
		ciOwnrSuppliedExpYr = aiOwnrSuppliedExpYr;
	}

	/**
	 * Set the  value of OwnrSuppliedPltNo 
	 * 
	 * @param asOwnrSuppliedPltNo String
	 */
	public void setOwnrSuppliedPltNo(String asOwnrSuppliedPltNo)
	{
		csOwnrSuppliedPltNo = asOwnrSuppliedPltNo;
	}
	/**
	 * Set the  value of OwnrSuppliedStkrNo
	 * 
	 * @param asOwnrSuppliedStkrNo String
	 */
	public void setOwnrSuppliedStkrNo(String asOwnrSuppliedStkrNo)
	{
		csOwnrSuppliedStkrNo = asOwnrSuppliedStkrNo;
	}

	/**
	 * Set the  value of SalesTaxCat 
	 * 
	 * @param asSalesTaxCat String
	 */
	public void setSalesTaxCat(String asSalesTaxCat)
	{
		csSalesTaxCat = asSalesTaxCat;
	}

	/**
	 * Set the  value of SalesTaxDate
	 * 
	 * @param aiSalesTaxDate int
	 */
	public void setSalesTaxDate(int aiSalesTaxDate)
	{
		ciSalesTaxDate = aiSalesTaxDate;
	}
	/**
	 * Set the  value of SalesTaxExmptCd
	 * 
	 * @param aiSalesTaxExmptCd int
	 */
	public void setSalesTaxExmptCd(int aiSalesTaxExmptCd)
	{
		ciSalesTaxExmptCd = aiSalesTaxExmptCd;
	}

	/**
	 * Set the  value of SalesTaxPnltyPer
	 * 
	 * @param aiSalesTaxPnltyPer int
	 */
	public void setSalesTaxPnltyPer(int aiSalesTaxPnltyPer)
	{
		ciSalesTaxPnltyPer = aiSalesTaxPnltyPer;
	}

	/**
	 * Set the  value of SpclPltProgIndi
	 * 
	 * @param aiSpclPltProgIndi int
	 */
	public void setSpclPltProgIndi(int aiSpclPltProgIndi)
	{
		ciSpclPltProgIndi = aiSpclPltProgIndi;
	}

	/**
	 * Set the  value of SubconID
	 * 
	 * @param aiSubconID int
	 */
	public void setSubconID(int aiSubconID)
	{
		ciSubconID = aiSubconID;
	}

	/**
	 * Set the  value of SupvOvride
	 * 
	 * @param asSupvOvride String
	 */
	public void setSupvOvride(String asSupvOvride)
	{
		csSupvOvride = asSupvOvride;
	}

	/**
	 * Set the  value of SupvOvrideReason
	 * 
	 * @param asSupvOvrideReason String
	 */
	public void setSupvOvrideReason(String asSupvOvrideReason)
	{
		csSupvOvrideReason = asSupvOvrideReason;
	}

	/**
	 * Set the  value of TaxPdOthrState
	 * 
	 * @param aaTaxPdOthrState Dollar
	 */
	public void setTaxPdOthrState(Dollar aaTaxPdOthrState)
	{
		caTaxPdOthrState = aaTaxPdOthrState;
	}

	/**
	 * Set the  value of TotalRebateAmt
	 * 
	 * @param aaTotalRebateAmt Dollar
	 */
	public void setTotalRebateAmt(Dollar aaTotalRebateAmt)
	{
		caTotalRebateAmt = aaTotalRebateAmt;
	}

	/**
	 * Set the  value of TradeInVehMk
	 * 
	 * @param asTradeInVehMk String
	 */
	public void setTradeInVehMk(String asTradeInVehMk)
	{
		csTradeInVehMk = asTradeInVehMk;
	}

	/**
	 * Set the  value of TradeInVehVIN
	 * 
	 * @param asTradeInVehVIN String
	 */
	public void setTradeInVehVIN(String asTradeInVehVIN)
	{
		csTradeInVehVIN = asTradeInVehVIN;
	}

	/**
	 * Set the  value of TradeInVehYr
	 * 
	 * @param aiTradeInVehYr int
	 */
	public void setTradeInVehYr(int aiTradeInVehYr)
	{
		ciTradeInVehYr = aiTradeInVehYr;
	}

	/**
	 * Set the  value of TtlApplDate
	 * 
	 * @param aiTtlApplDate int
	 */
	public void setTtlApplDate(int aiTtlApplDate)
	{
		ciTtlApplDate = aiTtlApplDate;
	}

	/**
	 * Set the  value of VehTaxAmt
	 * 
	 * @param aaVehTaxAmt Dollar
	 */
	public void setVehTaxAmt(Dollar aaVehTaxAmt)
	{
		caVehTaxAmt = aaVehTaxAmt;
	}

	/**
	 * Set value of csVTNSource
	 * 
	 * @param asVTNSource
	 */
	public void setVTNSource(String asVTNSource)
	{
		csVTNSource = asVTNSource;
	}

	/**
	 * Set value of ciV21VTNId 
	 * 
	 * @param aiV21VTNId
	 */
	public void setV21VTNId(int aiV21VTNId)
	{
		ciV21VTNId = aiV21VTNId;
	}

	/**
	 * Set value of csTtlTrnsfrEntCd
	 * 
	 * @param asTtlTrnsfrEntCd
	 */
	public void setTtlTrnsfrEntCd(String asTtlTrnsfrEntCd)
	{
		csTtlTrnsfrEntCd = asTtlTrnsfrEntCd;
	}

	/**
	 * Get value of caTtlTrnsfrPnltyFee
	 * 
	 * @return Dollar
	 */
	public Dollar getTtlTrnsfrPnltyFee()
	{
		return caTtlTrnsfrPnltyFee;
	}

	/**
	 * Set value of caTtlTrnsfrPnltyFee
	 * 
	 * @param aaTtlTrnsfrPnltyFee
	 */
	public void setTtlTrnsfrPnltyFee(Dollar aaTtlTrnsfrPnltyFee)
	{
		caTtlTrnsfrPnltyFee = aaTtlTrnsfrPnltyFee;
	}

	/**
	 * Get value of csTtlTrnsfrPnltyExmptCd
	 * 
	 * @return String
	 */
	public String getTtlTrnsfrPnltyExmptCd()
	{
		return csTtlTrnsfrPnltyExmptCd;
	}

	/**
	 * Set value of csTtlTrnsfrPnltyExmptCd
	 * 
	 * @param asTtlTrnsfrPnltyExmptCd
	 */
	public void setTtlTrnsfrPnltyExmptCd(String asTtlTrnsfrPnltyExmptCd)
	{
		csTtlTrnsfrPnltyExmptCd = asTtlTrnsfrPnltyExmptCd;
	}

}
