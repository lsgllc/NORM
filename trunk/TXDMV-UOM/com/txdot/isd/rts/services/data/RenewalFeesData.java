package com.txdot.isd.rts.services.data;

/* 
 * RenewalFeesData.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	06/28/2005	Refactor\Move 
 * 							RenewalFeesData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * ---------------------------------------------------------------------
 */
/**
 * Used for Subcontractor Renewal and Quick Counter Renewal 
 * and pass to Common for calculating fees.
 * 
 * @version	5.2.3		06/28/2005
 * @author	Nancy Ting
 * <br>Creation Date:	10/18/2001 10:36:38
 */
public class RenewalFeesData implements java.io.Serializable
{
	private int ciIssueMismatchIndi;
	private java.lang.String csNewRegPltNo;
	private java.lang.String csNewRegStkrNo;
	private java.lang.String csPltInvId;
	private java.lang.String csPltLocIdCd;
	private java.lang.String csRecordTypeCd;
	private int ciRenwlBarcodeIndi;
	private com.txdot.isd.rts.services.util.Dollar caRenwlTotalFee;
	private java.lang.String csStkrInvId;
	private java.lang.String csStkrLocIdCd;
	private int ciSubconId;
	private int ciSubconIssueDate;
	private com
		.txdot
		.isd
		.rts
		.services
		.data
		.RenewalBarCodeData caRenwlBarcodeData;
	private final static long serialVersionUID = 3299090694075288551L;
	/**
	 * RenewalFeesData constructor comment.
	 */
	public RenewalFeesData()
	{
		super();
	}
	/**
	 * getIssueMismatchIndi
	 * 
	 * @return int
	 */
	public int getIssueMismatchIndi()
	{
		return ciIssueMismatchIndi;
	}
	/**
	 * getNewRegPltNo
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getNewRegPltNo()
	{
		return csNewRegPltNo;
	}
	/**
	 * getNewRegStkrNo
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getNewRegStkrNo()
	{
		return csNewRegStkrNo;
	}
	/**
	 * getPltInvId
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPltInvId()
	{
		return csPltInvId;
	}
	/**
	 * getPltLocIdCd
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPltLocIdCd()
	{
		return csPltLocIdCd;
	}
	/**
	 * getRecordTypeCd
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getRecordTypeCd()
	{
		return csRecordTypeCd;
	}
	/**
	 * getRenwlBarcodeData
	 * 
	 * @return com.txdot.isd.rts.client.common.ui.RenewalBarCodeData
	 */
	public com
		.txdot
		.isd
		.rts
		.services
		.data
		.RenewalBarCodeData getRenwlBarcodeData()
	{
		return caRenwlBarcodeData;
	}
	/**
	 * getRenwlBarcodeIndi
	 * 
	 * @return int
	 */
	public int getRenwlBarcodeIndi()
	{
		return ciRenwlBarcodeIndi;
	}
	/**
	 * getRenwlTotalFee
	 * 
	 * @return com.txdot.isd.rts.services.util.Dollar
	 */
	public com.txdot.isd.rts.services.util.Dollar getRenwlTotalFee()
	{
		return caRenwlTotalFee;
	}
	/**
	 * getStkrInvId
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getStkrInvId()
	{
		return csStkrInvId;
	}
	/**
	 * getStkrLocIdCd
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getStkrLocIdCd()
	{
		return csStkrLocIdCd;
	}
	/**
	 * getSubconId
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}
	/**
	 * getSubconIssueDate
	 * 
	 * @return int
	 */
	public int getSubconIssueDate()
	{
		return ciSubconIssueDate;
	}
	/**
	 * setIssueMismatchIndi
	 * 
	 * @param aiNewIssueMismatchIndi int
	 */
	public void setIssueMismatchIndi(int aiNewIssueMismatchIndi)
	{
		ciIssueMismatchIndi = aiNewIssueMismatchIndi;
	}
	/**
	 * setNewRegPltNo
	 * 
	 * @param asNewNewRegPltNo java.lang.String
	 */
	public void setNewRegPltNo(java.lang.String asNewNewRegPltNo)
	{
		csNewRegPltNo = asNewNewRegPltNo;
	}
	/**
	 * setNewRegStkrNo
	 * 
	 * @param asNewNewRegStkrNo java.lang.String
	 */
	public void setNewRegStkrNo(java.lang.String asNewNewRegStkrNo)
	{
		csNewRegStkrNo = asNewNewRegStkrNo;
	}
	/**
	 * setPltInvId
	 * @param asNewPltInvId java.lang.String
	 */
	public void setPltInvId(java.lang.String asNewPltInvId)
	{
		csPltInvId = asNewPltInvId;
	}
	/**
	 * setPltLocIdCd
	 * 
	 * @param asNewPltLocIdCd java.lang.String
	 */
	public void setPltLocIdCd(java.lang.String asNewPltLocIdCd)
	{
		csPltLocIdCd = asNewPltLocIdCd;
	}
	/**
	 * setRecordTypeCd
	 * 
	 * @param asNewRecordTypeCd java.lang.String
	 */
	public void setRecordTypeCd(java.lang.String asNewRecordTypeCd)
	{
		csRecordTypeCd = asNewRecordTypeCd;
	}
	/**
	 * setRenwlBarcodeData
	 * 
	 * @param aaNewRenwlBarcodeData 
	 * 	com.txdot.isd.rts.client.common.ui.RenewalBarCodeData
	 */
	public void setRenwlBarcodeData(
		com
			.txdot
			.isd
			.rts
			.services
			.data
			.RenewalBarCodeData aaNewRenwlBarcodeData)
	{
		caRenwlBarcodeData = aaNewRenwlBarcodeData;
	}
	/**
	 * setRenwlBarcodeIndi
	 * 
	 * @param aiNewRenwlBarcodeIndi int
	 */
	public void setRenwlBarcodeIndi(int aiNewRenwlBarcodeIndi)
	{
		ciRenwlBarcodeIndi = aiNewRenwlBarcodeIndi;
	}
	/**
	 * setRenwlTotalFee
	 * 
	 * @param aaNewRenwlTotalFee com.txdot.isd.rts.services.util.Dollar
	 */
	public void setRenwlTotalFee(
		com.txdot.isd.rts.services.util.Dollar aaNewRenwlTotalFee)
	{
		caRenwlTotalFee = aaNewRenwlTotalFee;
	}
	/**
	 * setStkrInvId
	 * 
	 * @param asNewStkrInvId java.lang.String
	 */
	public void setStkrInvId(java.lang.String asNewStkrInvId)
	{
		csStkrInvId = asNewStkrInvId;
	}
	/**
	 * setStkrLocIdCd
	 * 
	 * @param asNewStkrLocIdCd java.lang.String
	 */
	public void setStkrLocIdCd(java.lang.String asNewStkrLocIdCd)
	{
		csStkrLocIdCd = asNewStkrLocIdCd;
	}
	/**
	 * setSubconId
	 * 
	 * @param aiNewSubconId int
	 */
	public void setSubconId(int aiNewSubconId)
	{
		ciSubconId = aiNewSubconId;
	}
	/**
	 * setSubconIssueDate
	 * 
	 * @param aiNewSubconIssueDate int
	 */
	public void setSubconIssueDate(int aiNewSubconIssueDate)
	{
		ciSubconIssueDate = aiNewSubconIssueDate;
	}
}
