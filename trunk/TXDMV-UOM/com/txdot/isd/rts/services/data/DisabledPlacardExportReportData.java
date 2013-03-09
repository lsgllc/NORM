package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.util.UtilityMethods;

/*
 * DisabledPlacardExportReportData
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/27/2008	Created 
 * 							defect 9831 Ver POS_Defect_B
 * K Harrell	11/03/2008	add csDelReasnDesc, get/set methods.
 * 							defect 9831 Ver POS_Defect_B 
 * ---------------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get set methods for 
 * DisabledPlacardData.
 *
 * @version	POS_Defect_B	11/03/2008
 * @author	Kathy Harrell
 * <br>Creation Date:		10/27/2008
 */
public class DisabledPlacardExportReportData
	extends DisabledPlacardCustomerData
{
	private int ciDeleteIndi;
	private int ciDsabldPlcrdIdntyNo;
	private int ciRTSEffDate;
	private int ciRTSExpMo;
	private int ciRTSExpYr;
	private int ciTransIdntyNo;
	private int ciTransTypeCd;
	private int ciVoidedIndi;
	private String csAcctItmCd;
	private String csAcctItmCdDesc;
	private String csCustIdTypeDesc;
	private String csDelReasnDesc;
	private String csInvItmNo;
	private String csOfcName;
	private String csTransId;

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
	 * Get value of ciDsabldPlcrdIdntyNo
	 * 
	 * @return int
	 */
	public int getDsabldPlcrdIdntyNo()
	{
		return ciDsabldPlcrdIdntyNo;
	}

	/** 
	 * 
	 * Build String of Data for MVDI Export
	 * 
	 * @return String 
	 */
	public String getExportData()
	{
		String lsData =
			UtilityMethods.addPadding("" + getCustIdntyNo(), 10, "0")
				+ UtilityMethods.addPaddingRight(getCustId(), 15, " ")
				+ UtilityMethods.addPadding(
					"" + getCustIdTypeCd(),
					6,
					"0")
				+ UtilityMethods.addPaddingRight(
					getCustIdTypeDesc(),
					45,
					" ")
				+ getInstIndi()
				+ UtilityMethods.addPaddingRight(
					getOwnerName(false),
					61,
					" ")
				+ UtilityMethods.addPaddingRight(
					getAddressData().getSt1(),
					30,
					" ")
				+ UtilityMethods.addPaddingRight(
					getAddressData().getSt2() == null
						? " "
						: getAddressData().getSt2(),
					30,
					" ")
				+ UtilityMethods.addPaddingRight(
					getAddressData().getCity(),
					19,
					" ")
				+ getStateCntryForExport()
				+ getZipForExport()
				+ UtilityMethods.addPadding(
					"" + getResComptCntyNo(),
					3,
					"0")
				+ UtilityMethods.addPaddingRight(getOfcName(), 45, " ")
				+ UtilityMethods.addPaddingRight(getInvItmNo(), 10, " ")
				+ UtilityMethods.addPaddingRight(getAcctItmCd(), 8, " ")
				+ UtilityMethods.addPaddingRight(
					getAcctItmCdDesc(),
					28,
					" ")
				+ getRTSEffDate()
				+ UtilityMethods.addPadding("" + getRTSExpMo(), 2, "0")
				+ getRTSExpYr()
				+ getDeleteIndi()
				+ getVoidedIndi()
				+ UtilityMethods.addPadding(
					"" + getDsabldPlcrdIdntyNo(),
					10,
					"0")
				+ UtilityMethods.addPaddingRight(getDelReasnDesc(), 30, " ");
		return lsData;
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
		return csOfcName;
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
		return ciRTSExpMo;
	}

	/**
	 * Get value of ciRTSExpYr
	 * 
	 * @return int
	 */
	public int getRTSExpYr()
	{
		return ciRTSExpYr;
	}

	/**
	 * Return State or Cntry value as appropriate  
	 */
	public String getStateCntryForExport()
	{

		return UtilityMethods.addPaddingRight(
			(getAddressData().isUSA()
				? getAddressData().getState()
				: getAddressData().getCntry()),
			4,
			" ");
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
	 * Get value of ciTransIdntyNo
	 * 
	 * @return int
	 */
	public int getTransIdntyNo()
	{
		return ciTransIdntyNo;
	}

	/**
	 * Get value of ciTransTypeCd
	 * 
	 * @return int
	 */
	public int getTransTypeCd()
	{
		return ciTransTypeCd;
	}

	/**
	 * Get value of ciVoidedIndi
	 * 
	 * @return int
	 */
	public int getVoidedIndi()
	{
		return ciVoidedIndi;
	}

	/**
	 * Return State or Cntry value as appropriate  
	 */
	public String getZipForExport()
	{
		String lsZp = getAddressData().getZpcd().trim();
		String lsZpP4 =
			getAddressData().getZpcdp4() == null
				? ""
				: getAddressData().getZpcdp4().trim();

		if (getAddressData().isUSA())
		{
			lsZpP4 = lsZpP4.length() == 0 ? "" : "-" + lsZpP4;
		}
		return UtilityMethods.addPaddingRight((lsZp + lsZpP4), 10, " ");
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
	 * Set value of ciDsabldPlcrdIdntyNo
	 * 
	 * @param aiDsabldPlcrdIdntyNo
	 */
	public void setDsabldPlcrdIdntyNo(int aiDsabldPlcrdIdntyNo)
	{
		ciDsabldPlcrdIdntyNo = aiDsabldPlcrdIdntyNo;
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
	 * Set value of csOfcName
	 * 
	 * @param asOfcName
	 */
	public void setOfcName(String asOfcName)
	{
		csOfcName = asOfcName;
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
	 * Set value of csTransId
	 * 
	 * @param asTransId
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * Set value of ciTransIdntyNo
	 * 
	 * @param aiTransIdntyNo
	 */
	public void setTransIdntyNo(int aiTransIdntyNo)
	{
		ciTransIdntyNo = aiTransIdntyNo;
	}

	/**
	 * Set value of ciTransTypeCd
	 * 
	 * @param aiTransTypeCd
	 */
	public void setTransTypeCd(int aiTransTypeCd)
	{
		ciTransTypeCd = aiTransTypeCd;
	}

	/**
	 * Set value of ciVoidedIndi 
	 * 
	 * @param aiVoidedIndi
	 */
	public void setVoidedIndi(int aiVoidedIndi)
	{
		ciVoidedIndi = aiVoidedIndi;
	}

}
