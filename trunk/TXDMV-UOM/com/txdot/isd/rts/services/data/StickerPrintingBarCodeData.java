package com.txdot.isd.rts.services.data;

/*
 *
 * StickerPrintingBarCodeData.java
 *
 * (c) Texas Department of Transportation 2004
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		06/08/2004	New data object for FrmBarcodeReader
 *							defect 7108 Ver 5.2.1
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3  
 * ---------------------------------------------------------------
 */

/**
 * This Data class contains attributes and get/set methods for 
 * utility program Sticker Printing Barcode Reader
 * 
 *
 * @version	5.2.3 		04/21/2005
 * @author: Jeff Rue
 * <br>Creation Date:	06/08/2004
 * 
 */

public class StickerPrintingBarCodeData
{
	// String
	private String csData = "";
	private String csDocNo = "";
	private String csPrntCntyNo = "";
	private String csRegClassCd = "";
	private String csRegExpDate = "";
	private String csRegisPltCd = "";
	private String csRegisStkrCd = "";
	private String csRegPltNo = "";
	private String csResCompCntyNo = "";
	private String csStkrPrntDate = "";
	private String csType = "";
	private String csVersion = "";
	private String csVIN = "";
	private String csWorkStatId = "";
	/**
	 * Return the value of Data
	 * 
	 * @return String
	 */
	public String getData()
	{
		return csData;
	}
	/**
	 * Return the value of DocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}
	/**
	 * Return the value of PrntCntyNo
	 * 
	 * @return String
	 */
	public String getPrntCntyNo()
	{
		return csPrntCntyNo;
	}
	/**
	 * Return the value of RegClassCd
	 * 
	 * @return String
	 */
	public String getRegClassCd()
	{
		return csRegClassCd;
	}
	/**
	 * Return the value of RegExpDate
	 * 
	 * @return String
	 */
	public String getRegExpDate()
	{
		return csRegExpDate;
	}
	/**
	 * Return the value of RegisPltCd
	 * 
	 * @return String
	 */
	public String getRegisPltCd()
	{
		return csRegisPltCd;
	}
	/**
	 * Return the value of RegisStkrCd
	 * 
	 * @return String
	 */
	public String getRegisStkrCd()
	{
		return csRegisStkrCd;
	}
	/**
	 * Return the value of RegPltNo
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}
	/**
	 * Return the value of ResCompCntyNo
	 * 
	 * @return String
	 */
	public String getResCompCntyNo()
	{
		return csResCompCntyNo;
	}
	/**
	 * Return the value of StkrPrntDate
	 * 
	 * @return String
	 */
	public String getStkrPrntDate()
	{
		return csStkrPrntDate;
	}
	/**
	 * Return the value of Type
	 * 
	 * @return String
	 */
	public String getType()
	{
		return csType;
	}
	/**
	 * Return the value of Version
	 * 
	 * @return String
	 */
	public String getVersion()
	{
		return csVersion;
	}
	/**
	 * Return the value of VIN 
	 * 
	 * @return String
	 */
	public String getVIN()
	{
		return csVIN;
	}
	/**
	 * Return the value of WorkStatId
	 *
	 * @return String
	 */
	public String getWorkStatId()
	{
		return csWorkStatId;
	}
	/**
	 * Set the value of Data
	 * 
	 * @param asData String
	 */
	public void setData(String asData)
	{
		csData = asData;
	}
	/**
	 * Set the value of DocNo
	 * 
	 * @param asDocNo String
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}
	/**
	 * Set the value of PrntCntyNo 
	 * 
	 * @param asPrntCntyNo String
	 */
	public void setPrntCntyNo(String asPrntCntyNo)
	{
		csPrntCntyNo = asPrntCntyNo;
	}
	/**
	 * Set the value of RegClassCd
	 * 
	 * @param asRegClassCd String
	 */
	public void setRegClassCd(String asRegClassCd)
	{
		csRegClassCd = asRegClassCd;
	}
	/**
	 * Set the value of RegExpDate
	 * 
	 * @param asRegExpDate String
	 */
	public void setRegExpDate(String asRegExpDate)
	{
		csRegExpDate = asRegExpDate;
	}
	/**
	 * Set the value of RegisPltCd
	 * 
	 * @param asRegisPltCd String
	 */
	public void setRegisPltCd(String asRegisPltCd)
	{
		csRegisPltCd = asRegisPltCd;
	}
	/**
	 * Set the value of RegisStkrCd
	 * 
	 * @param asRegisStkrCd String
	 */
	public void setRegisStkrCd(String asRegisStkrCd)
	{
		csRegisStkrCd = asRegisStkrCd;
	}
	/**
	 * Set the value of RegPltNo
	 * 
	 * @param asRegPltNo String
	 */
	public void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}
	/**
	 * Set the value of ResCompCntyNo 
	 * 
	 * @param asResCompCntyNo String
	 */
	public void setResCompCntyNo(String asResCompCntyNo)
	{
		csResCompCntyNo = asResCompCntyNo;
	}
	/**
	 * Set the value of StkrPrntDate
	 * 
	 * @param asStkrPrntDate String
	 */
	public void setStkrPrntDate(String asStkrPrntDate)
	{
		csStkrPrntDate = asStkrPrntDate;
	}
	/**
	 * Set the value of Type
	 * 
	 * @param asType String
	 */
	public void setType(String asType)
	{
		csType = asType;
	}
	/**
	 * Set the value of Version
	 * 
	 * @param asVersion String
	 */
	public void setVersion(String asVersion)
	{
		csVersion = asVersion;
	}
	/**
	 * Set the value ofcsVIN 
	 * 
	 * @param asVIN int
	 */
	public void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}
	/**
	 * Set the value of WorkStatId
	 * 
	 * @param asWorkStatId String
	 */
	public void setWorkStatId(String asWorkStatId)
	{
		csWorkStatId = asWorkStatId;
	}
}
