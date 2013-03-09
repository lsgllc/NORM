package com.txdot.isd.rts.services.data;

import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * RenewalBarCodeData.java
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name				Date			Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 *							add csCntyNo, cdRenwlPrice 
 *							and get/set methods
 * 							add details for method descriptions
 * 									Ver 5.2.0	
 * J Rue		06/08/2004 	Add Barcode Type and Version
 *							getter/setter for Barcode Utility
 *							add getVersion(), setVersion
 *							getType(), setType()
 *							defect 7108,  ver 5.2.1
 * B Hargrove	07/29/2004 	Add getter/setters for acct codes and
 *							fees 8 - 15.									
 *							add fields (ie: csAcctItmCd8, caItmPrice8)
 *							add getters, setters
 *							defect 7348  Ver 5.2.1
 * K Harrell	04/21/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	04/12/2007	add csOrgNo, ciAddlSetIndi
 * 								plus get/set methods
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/23/2007	add csNewPltsReqdCd, get/set methods
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/22/2007	add treatAsSpclPlt()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/31/2007	Modify treatAsSpclPlt() to disregard 
 * 							"K" if no SpecialPlateFees
 * 							modify treatAsSpclPlt()
 * 							defect 9085 Ver Special Plates   
 * T Pederson	01/05/2010 	Add fields and getter/setters for barcode
 *							version 06.
 *							delete SPCLPLT_FEES_NOT_INCLUDED,
 *							INIT_SPCLPLT_BARCODE_VERSION									
 *							add fields csRegNextExpMo, csRegNextExpYr,
 *							csPltValidityTerm, csPltExpMo, csPltExpYr,
 *							csPltNextExpMo and csPltNextExpYr.
 *							add getters, setters
 * 							modify treatAsSpclPlt()
 *							defect 10303  Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */
/**
 * This Data class contains attributes and get set methods for 
 * RenewalBarCodeData
 * 
 * 
 * @version	Defect_POS_F	01/05/2010
 * @author	Administrator
 * <br>Creation Date:		09/05/2001
 */

public class RenewalBarCodeData implements java.io.Serializable
{
	// int 
	private int ciRegClassCd;
	// defect 9085 
	private int ciAddlSetIndi;
	// end defect 9085 

	// String 
	private String csAuditTrailTransId;
	private String csCntyNo;
	private String csDocNo;
	private String csRegPltCd;
	private String csRegStkrCd;
	private String csRegPltNo;
	private String csRegExpMo;
	private String csRegExpYr;
	private String csAcctItmCd1;
	private String csAcctItmCd2;
	private String csAcctItmCd3;
	private String csAcctItmCd4;
	private String csAcctItmCd5;
	private String csAcctItmCd6;
	private String csAcctItmCd7;
	private String csAcctItmCd8;
	private String csAcctItmCd9;
	private String csAcctItmCd10;
	private String csAcctItmCd11;
	private String csAcctItmCd12;
	private String csAcctItmCd13;
	private String csAcctItmCd14;
	private String csAcctItmCd15;
	// defect 7108
	// Retain Type and Version for Barcode Scanner
	private String csType;
	private String csVersion;
	// end defect 7108
	private String csVin;
	// defect 9085
	private String csOrgNo;
	private String csNewPltsReqdCd;
	// end defect 9085 

	// defect 10303
	private String csRegNextExpMo;
	private String csRegNextExpYr;
	private String csPltValidityTerm;
	private String csPltExpMo;
	private String csPltExpYr;
	private String csPltNextExpMo;
	private String csPltNextExpYr;
	// end defect 10303

	// Object 
	private Dollar caItmPrice1;
	private Dollar caItmPrice2;
	private Dollar caItmPrice3;
	private Dollar caItmPrice4;
	private Dollar caItmPrice5;
	private Dollar caItmPrice6;
	private Dollar caItmPrice7;
	private Dollar caItmPrice8;
	private Dollar caItmPrice9;
	private Dollar caItmPrice10;
	private Dollar caItmPrice11;
	private Dollar caItmPrice12;
	private Dollar caItmPrice13;
	private Dollar caItmPrice14;
	private Dollar caItmPrice15;
	private Dollar caRenwlPrice; //calculated by Subcon

	// defect 10303
	//private final static String SPCLPLT_FEES_NOT_INCLUDED = "K";
	//private final static int INIT_SPCLPLT_BARCODE_VERSION = 5;
	// end defect 10303
	private final static long serialVersionUID = -4831136149268653653L;

	/**
	 * RenewalBarCodeData constructor comment.
	 */
	public RenewalBarCodeData()
	{
		super();
	}
	/**
	 * Returns the value of AcctItmCd1
	 * 
	 * @return String
	 */
	public String getAcctItmCd1()
	{
		return csAcctItmCd1;
	}
	/**
	 * Returns the value of AcctItmCd10
	 * 
	 * @return String
	 */
	public String getAcctItmCd10()
	{
		return csAcctItmCd10;
	}
	/**
	 * Returns the value of AcctItmCd11
	 * 
	 * @return String
	 */
	public String getAcctItmCd11()
	{
		return csAcctItmCd11;
	}
	/**
	 * Returns the value of AcctItmCd12
	 * 
	 * @return String
	 */
	public String getAcctItmCd12()
	{
		return csAcctItmCd12;
	}
	/**
	 * Returns the value of AcctItmCd13
	 *
	 * @return String
	 */
	public String getAcctItmCd13()
	{
		return csAcctItmCd13;
	}
	/**
	 * Returns the value of AcctItmCd14
	 * 
	 * @return String
	 */
	public String getAcctItmCd14()
	{
		return csAcctItmCd14;
	}
	/**
	 * Returns the value of AcctItmCd15
	 * 
	 * @return String
	 */
	public String getAcctItmCd15()
	{
		return csAcctItmCd15;
	}
	/**
	 * Returns the value of AcctItmCd2
	 * 
	 * @return String
	 */
	public String getAcctItmCd2()
	{
		return csAcctItmCd2;
	}
	/**
	 * Returns the value of AcctItmCd3
	 * 
	 * @return String
	 */
	public String getAcctItmCd3()
	{
		return csAcctItmCd3;
	}
	/**
	 * Returns the value of AcctItmCd4
	 * 
	 * @return String
	 */
	public String getAcctItmCd4()
	{
		return csAcctItmCd4;
	}
	/**
	 * Returns the value of AcctImtCd5 
	 * 
	 * @return String
	 */
	public String getAcctItmCd5()
	{
		return csAcctItmCd5;
	}
	/**
	 * Returns the value of AcctItmCd6 
	 * 
	 * @return String
	 */
	public String getAcctItmCd6()
	{
		return csAcctItmCd6;
	}
	/**
	 * Returns the value of AcctItmCd7
	 * 
	 * @return String
	 */
	public String getAcctItmCd7()
	{
		return csAcctItmCd7;
	}
	/**
	 * Returns the value of AcctItmCd8
	 * 
	 * @return String
	 */
	public String getAcctItmCd8()
	{
		return csAcctItmCd8;
	}
	/**
	 * Returns the value of AcctItmCd9
	 *
	 * @return String
	 */
	public String getAcctItmCd9()
	{
		return csAcctItmCd9;
	}
	/**
	 * Return AddlSetIndi
	 * 
	 * @return int
	 */
	public int getAddlSetIndi()
	{
		return ciAddlSetIndi;
	}
	/**
	 * Returns the value of AuditTrailTransId
	 * 
	 * @return String
	 */
	public String getAuditTrailTransId()
	{
		return csAuditTrailTransId;
	}
	/**
	 * Returns the value of CntyNo
	 * 
	 * @return String
	 */
	public String getCntyNo()
	{
		return csCntyNo;
	}
	/**
	 * Returns the value of DocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}
	/**
	 * Returns the value of ItmPrice1
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice1()
	{
		return caItmPrice1;
	}
	/**
	 * Returns the value of ItmPrice10
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice10()
	{
		return caItmPrice10;
	}
	/**
	 * Returns the value of ItmPrice11
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice11()
	{
		return caItmPrice11;
	}
	/**
	 * Returns the value of ItmPrice12
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice12()
	{
		return caItmPrice12;
	}
	/**
	 * Returns the value of ItmPrice13
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice13()
	{
		return caItmPrice13;
	}
	/**
	 * Returns the value of ItmPrice14
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice14()
	{
		return caItmPrice14;
	}
	/**
	 * Returns the value of ItmPrice15
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice15()
	{
		return caItmPrice15;
	}
	/**
	 * Returns the value of ItmPrice2
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice2()
	{
		return caItmPrice2;
	}
	/**
	 * Returns the value of ItmPrice3
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice3()
	{
		return caItmPrice3;
	}
	/**
	 * Returns the value of ItmPrice4
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice4()
	{
		return caItmPrice4;
	}
	/**
	 * Returns the value of ItmPrice5
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice5()
	{
		return caItmPrice5;
	}
	/**
	 * Returns the value of ItmPrice6
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice6()
	{
		return caItmPrice6;
	}
	/**
	 * Returns the value of ItmPrice7
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice7()
	{
		return caItmPrice7;
	}
	/**
	 * Returns the value of ItmPrice8
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice8()
	{
		return caItmPrice8;
	}
	/**
	 * Returns the value of ItmPrice9
	 * 
	 * @return Dollar
	 */
	public Dollar getItmPrice9()
	{
		return caItmPrice9;
	}
	/**
	 * Returns the value of csNewPltsReqdCd
	 * 
	 * @return String
	 */
	public String getNewPltsReqdCd()
	{
		return csNewPltsReqdCd;
	}
	/**
	 * Return value of OrgNo
	 * 
	 * @return String 
	 */
	public String getOrgNo()
	{
		return csOrgNo;
	}
	/**
	 * Returns the value of csPltExpMo
	 * 
	 * @return
	 */
	public String getPltExpMo()
	{
		return csPltExpMo;
	}
	/**
	 * Returns the value of csPltExpYr
	 * 
	 * @return
	 */
	public String getPltExpYr()
	{
		return csPltExpYr;
	}
	/**
	 * Returns the value of csPltNextExpMo
	 * 
	 * @return
	 */
	public String getPltNextExpMo()
	{
		return csPltNextExpMo;
	}
	/**
	 * Returns the value of csPltNextExpYr
	 * 
	 * @return
	 */
	public String getPltNextExpYr()
	{
		return csPltNextExpYr;
	}
	/**
	 * Returns the value of csPltValidityTerm
	 * 
	 * @return
	 */
	public String getPltValidityTerm()
	{
		return csPltValidityTerm;
	}
	/**
	 * Returns the value of RegClassCd
	 * 
	 * @return int
	 */
	public int getRegClassCd()
	{
		return ciRegClassCd;
	}
	/**
	 * Returns the value of RegExpMo
	 * 
	 * @return String
	 */
	public String getRegExpMo()
	{
		return csRegExpMo;
	}
	/**
	 * Returns the value of RegExpYr
	 * 
	 * @return String
	 */
	public String getRegExpYr()
	{
		return csRegExpYr;
	}
	/**
	 * Returns the value of csRegNextExpMo
	 * 
	 * @return
	 */
	public String getRegNextExpMo()
	{
		return csRegNextExpMo;
	}
	/**
	 * Returns the value of csRegNextExpYr
	 * 
	 * @return
	 */
	public String getRegNextExpYr()
	{
		return csRegNextExpYr;
	}
	/**
	 * Returns the value of RegPltCd 
	 * 
	 * @return String
	 */
	public String getRegPltCd()
	{
		return csRegPltCd;
	}
	/**
	 * Returns the value of RegPltNo 
	 * 
	 * @return String
	 */
	public String getRegPltNo()
	{
		return csRegPltNo;
	}
	/**
	 * Returns the value of RegStkrCd 
	 * 
	 * @return String
	 */
	public String getRegStkrCd()
	{
		return csRegStkrCd;
	}
	/**
	 * Returns the value of RenwlPrice.
	 *
	 * @return Dollar
	 */

	public Dollar getRenwlPrice()
	{
		return caRenwlPrice;
	}
	/**
	 * Barcode Type.
	 * 
	 * @return String
	 */
	public String getType()
	{
		return csType;
	}
	/**
	 * Barcode Version.
	 * 
	 * @return String
	 */
	public String getVersion()
	{
		return csVersion;
	}
	/**
	 * Returns the value of Vin 
	 * 
	 * @return String
	 */
	public String getVin()
	{
		return csVin;
	}
	/**
	 * 
	 * Includes Special Plate Fees
	 * 
	 * @return boolean 
	 */
	public boolean treatAsSpclPlt()
	{
		boolean lbTreatAsSpclPlt = false;

		try
		{
			// defect 10303
			// no longer need to check barcode version or NewPltsReqdCd
			// Numeric AbstractValue of BarCode Version
			// Special Plates Fees included starting @ Version 05
			//int liVersion = Integer.parseInt(csVersion);

			//lbTreatAsSpclPlt =
			//	liVersion >= INIT_SPCLPLT_BARCODE_VERSION
			//		&& csRegPltCd != null
			//		&& PlateTypeCache.isSpclPlate(csRegPltCd)
			//		&& csNewPltsReqdCd != null
			//		&& !csNewPltsReqdCd.equals(
			//			SpecialPlatesConstant
			//				.SPCLPLT_FEES_NOT_INCLUDED);
			lbTreatAsSpclPlt =
					csRegPltCd != null
					&& PlateTypeCache.isSpclPlate(csRegPltCd);
			// end defect 10303
		}
		catch (NumberFormatException aeNFE)
		{
		}
		if (!lbTreatAsSpclPlt)
		{
			System.out.println(
				csRegPltNo + "...DO NOT TREAT AS SPECIAL");
		}

		return lbTreatAsSpclPlt;
	}
	/**
	 * Sets the value of AcctItmCd1
	 * 
	 * @param asActItmCd1 String
	 */
	public void setAcctItmCd1(String asActItmCd1)
	{
		csAcctItmCd1 = asActItmCd1;
	}
	/**
	 * Sets the value of AcctItmCd10
	 * 
	 * @param asActItmCd10 String
	 */
	public void setAcctItmCd10(String asActItmCd10)
	{
		csAcctItmCd10 = asActItmCd10;
	}
	/**
	 * Sets the value of AcctItmCd11
	 * 
	 * @param asActItmC11 String
	 */
	public void setAcctItmCd11(String asActItmCd11)
	{
		csAcctItmCd11 = asActItmCd11;
	}
	/**
	 * Sets the value of AcctItmCd12
	 * 
	 * @param asActItmCd12 String
	 */
	public void setAcctItmCd12(String asActItmCd12)
	{
		csAcctItmCd12 = asActItmCd12;
	}
	/**
	 * Sets the value of AcctItmCd13
	 *
	 * @param asActItmCd13 String
	 */
	public void setAcctItmCd13(String asActItmCd13)
	{
		csAcctItmCd13 = asActItmCd13;
	}
	/**
	 * Sets the value of AcctItmCd14
	 * 
	 * @param asActItmCd14 String
	 */
	public void setAcctItmCd14(String asActItmCd14)
	{
		csAcctItmCd14 = asActItmCd14;
	}
	/**
	 * Sets the value of AcctItmCd15
	 * 
	 * @param asActItmCd15 String
	 */
	public void setAcctItmCd15(String asActItmCd15)
	{
		csAcctItmCd15 = asActItmCd15;
	}
	/**
	 * Sets the value of AcctItmCd2
	 * 
	 * @param asActItmCd2 String
	 */
	public void setAcctItmCd2(String asActItmCd2)
	{
		csAcctItmCd2 = asActItmCd2;
	}
	/**
	 * Sets the value of AcctItmCd3
	 * 
	 * @param asActItmCd3 String
	 */
	public void setAcctItmCd3(String asActItmCd3)
	{
		csAcctItmCd3 = asActItmCd3;
	}
	/**
	 * Sets the value of AcctItmCd4
	 * 
	 * @param asActItmCd4 String
	 */
	public void setAcctItmCd4(String asActItmCd4)
	{
		csAcctItmCd4 = asActItmCd4;
	}
	/**
	 * Sets the value of AcctItmCd5
	 * 
	 * @param asActItmCd5 String
	 */
	public void setAcctItmCd5(String asActItmCd5)
	{
		csAcctItmCd5 = asActItmCd5;
	}
	/**
	 * Sets the value of AcctItmCd6
	 * 
	 * @param asActItmCd6 String
	 */
	public void setAcctItmCd6(String asActItmCd6)
	{
		csAcctItmCd6 = asActItmCd6;
	}
	/**
	 * Sets the value of AcctItmCd7
	 * 
	 * @param asActItmCd7 String
	 */
	public void setAcctItmCd7(String asActItmCd7)
	{
		csAcctItmCd7 = asActItmCd7;
	}
	/**
	 * Sets the value of AcctItmCd8
	 * 
	 * @param asActItmCd8 String
	 */
	public void setAcctItmCd8(String asActItmCd8)
	{
		csAcctItmCd8 = asActItmCd8;
	}
	/**
	 * Sets the value of AcctItmCd9
	 *
	 * @param asActItmCd9 String
	 */
	public void setAcctItmCd9(String asActItmCd9)
	{
		csAcctItmCd9 = asActItmCd9;
	}
	/**
	 * Set value of AddlSetIndi
	 * 
	 * @param aiAddlSetIndi int
	 */
	public void setAddlSetIndi(int aiAddlSetIndi)
	{
		ciAddlSetIndi = aiAddlSetIndi;
	}
	/**
	 * Sets the value of AuditTrailTransId 
	 * 
	 * @param asAuditTrailTransId String
	 */
	public void setAuditTrailTransId(String asAuditTrailTransId)
	{
		csAuditTrailTransId = asAuditTrailTransId;
	}
	/**
	 * Sets the value of CntyNo
	 * 
	 * @param asCntyNo String
	 */
	public void setCntyNo(String asCntyNo)
	{
		csCntyNo = asCntyNo;
	}
	/**
	 * Sets the value of DocNo
	 * 
	 * @param asDocNo String
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}
	/**
	 * Sets the value of ItmPrice1
	 * 
	 * @param aaItmPrice1 Dollar
	 */
	public void setItmPrice1(Dollar aaItmPrice1)
	{
		caItmPrice1 = aaItmPrice1;
	}
	/**
	 * Sets the value of ItmPrice10
	 * 
	 * @param aaItmPrice10 Dollar
	 */
	public void setItmPrice10(Dollar aaItmPrice10)
	{
		caItmPrice10 = aaItmPrice10;
	}
	/**
	 * Sets the value of ItmPrice11
	 * 
	 * @param aaItmPrice11 Dollar
	 */
	public void setItmPrice11(Dollar aaItmPrice11)
	{
		caItmPrice11 = aaItmPrice11;
	}
	/**
	 * Sets the value of ItmPrice12
	 * 
	 * @param aaItmPrice12 Dollar
	 */
	public void setItmPrice12(Dollar aaItmPrice12)
	{
		caItmPrice12 = aaItmPrice12;
	}
	/**
	 * Sets the value of ItmPrice13
	 * 
	 * @param aaItmPrice13 Dollar
	 */
	public void setItmPrice13(Dollar aaItmPrice13)
	{
		caItmPrice13 = aaItmPrice13;
	}
	/**
	 * Sets the value of ItmPrice14
	 * 
	 * @param aaItmPrice14 Dollar
	 */
	public void setItmPrice14(Dollar aaItmPrice14)
	{
		caItmPrice14 = aaItmPrice14;
	}
	/**
	 * Sets the value of ItmPrice15
	 * 
	 * @param aaItmPrice15 Dollar
	 */
	public void setItmPrice15(Dollar aaItmPrice15)
	{
		caItmPrice15 = aaItmPrice15;
	}
	/**
	 * Sets the value of ItmPrice2
	 * 
	 * @param aaItmPrice2 Dollar
	 */
	public void setItmPrice2(Dollar aaItmPrice2)
	{
		caItmPrice2 = aaItmPrice2;
	}
	/**
	 * Sets the value of ItmPrice3
	 * 
	 * @param aaItmPrice3 Dollar
	 */
	public void setItmPrice3(Dollar aaItmPrice3)
	{
		caItmPrice3 = aaItmPrice3;
	}
	/**
	 * Sets the value of ItmPrice4
	 * 
	 * @param aaItmPrice4 Dollar
	 */
	public void setItmPrice4(Dollar aaItmPrice4)
	{
		caItmPrice4 = aaItmPrice4;
	}
	/**
	 * Sets the value of ItmPrice5
	 * 
	 * @param aaItmPrice5 Dollar
	 */
	public void setItmPrice5(Dollar aaItmPrice5)
	{
		caItmPrice5 = aaItmPrice5;
	}
	/**
	 * Sets the value of ItmPrice6
	 * 
	 * @param aaItmPrice6 Dollar
	 */
	public void setItmPrice6(Dollar aaItmPrice6)
	{
		caItmPrice6 = aaItmPrice6;
	}
	/**
	 * Sets the value of ItmPrice7
	 * 
	 * @param aaItmPrice7 Dollar
	 */
	public void setItmPrice7(Dollar aaItmPrice7)
	{
		caItmPrice7 = aaItmPrice7;
	}
	/**
	 * Sets the value of ItmPrice8
	 * 
	 * @param aaItmPrice8 Dollar
	 */
	public void setItmPrice8(Dollar aaItmPrice8)
	{
		caItmPrice8 = aaItmPrice8;
	}
	/**
	 * Sets the value of ItmPrice9
	 * 
	 * @param aaItmPrice9 Dollar
	 */
	public void setItmPrice9(Dollar aaItmPrice9)
	{
		caItmPrice9 = aaItmPrice9;
	}
	/**
	 * Set value of csNewPltsReqdCd
	 * 
	 * @param asNewPltsReqdCd String
	 */
	public void setNewPltsReqdCd(String asNewPltsReqdCd)
	{
		csNewPltsReqdCd = asNewPltsReqdCd;
	}
	/**
	 * Set value of OrgNo
	 * 
	 * @param asOrgNo String
	 */
	public void setOrgNo(String asOrgNo)
	{
		csOrgNo = asOrgNo;
	}
	/**
	 * Set value of csPltExpMo
	 * 
	 * @param asPltExpMo String
	 */
	public void setPltExpMo(String asPltExpMo)
	{
		csPltExpMo = asPltExpMo;
	}
	/**
	 * Set value of csPltExpYr
	 * 
	 * @param asPltExpYr String
	 */
	public void setPltExpYr(String asPltExpYr)
	{
		csPltExpYr = asPltExpYr;
	}
	/**
	 * Set value of csPltNextExpMo
	 * 
	 * @param asPltNextExpMo String
	 */
	public void setPltNextExpMo(String asPltNextExpMo)
	{
		csPltNextExpMo = asPltNextExpMo;
	}
	/**
	 * Set value of csPltNextExpYr
	 * 
	 * @param asPltNextExpYr String
	 */
	public void setPltNextExpYr(String asPltNextExpYr)
	{
		csPltNextExpYr = asPltNextExpYr;
	}
	/**
	 * Set value of csPltValidityTerm
	 * 
	 * @param asPltValidityTerm String
	 */
	public void setPltValidityTerm(String asPltValidityTerm)
	{
		csPltValidityTerm = asPltValidityTerm;
	}
	/**
	 * Sets the value of RegClassCd
	 * 
	 * @param aiRegClassCd int
	 */
	public void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}
	/**
	 * Sets the value of RegExpMo
	 * 
	 * @param asRegExpMo String
	 */
	public void setRegExpMo(String asRegExpMo)
	{
		csRegExpMo = asRegExpMo;
	}
	/**
	 * Sets the value of RegExpMo
	 * 
	 * @param asRegExpYr String
	 */
	public void setRegExpYr(String asRegExpYr)
	{
		csRegExpYr = asRegExpYr;
	}
	/**
	 * Set value of csRegNextExpMo
	 * 
	 * @param asRegNextExpMo String
	 */
	public void setRegNextExpMo(String asRegNextExpMo)
	{
		csRegNextExpMo = asRegNextExpMo;
	}
	/**
	 * Set value of csRegNextExpYr
	 * 
	 * @param asRegNextExpYr String
	 */
	public void setRegNextExpYr(String asRegNextExpYr)
	{
		csRegNextExpYr = asRegNextExpYr;
	}
	/**
	 * Sets the value of RegPltCd
	 * 
	 * @param aRegPltCd String
	 */
	public void setRegPltCd(String aRegPltCd)
	{
		csRegPltCd = aRegPltCd.trim();
	}
	/**
	 * Sets the value of RegPltNo
	 * 
	 * @param aRegPltNo String
	 */
	public void setRegPltNo(String aRegPltNo)
	{
		csRegPltNo = aRegPltNo.trim();
	}
	/**
	 * Sets the value of RegStkrCd
	 * 
	 * @param aRegStkrCd String
	 */
	public void setRegStkrCd(String aRegStkrCd)
	{
		csRegStkrCd = aRegStkrCd.trim();
	}
	/**
	 * Sets the value of cdRenwlPrice
	 * 
	 * @param adRenwlPrice Dollar
	 */

	public void setRenwlPrice(Dollar aaRenwlPrice)
	{
		caRenwlPrice = aaRenwlPrice;

	}
	/**
	 * Barcode Type
	 * 
	 * @param asType String
	 */
	public void setType(String asType)
	{
		csType = asType;
	}
	/**
	 * Barcode version
	 * 
	 * @param asVersion String
	 */
	public void setVersion(String asVersion)
	{
		csVersion = asVersion;
	}
	/**
	 * Sets the value of Vin
	 * 
	 * @param asVin String
	 */
	public void setVin(String asVin)
	{
		csVin = asVin;
	}
}
