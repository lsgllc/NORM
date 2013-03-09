package com.txdot.isd.rts.services.data;

import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CertifiedLienholderCache;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;
/*
 *
 * TitleData.java  
 *
 * (c) Texas Department of Transportation 2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Arredondo	09/08/2003	Added getter/setter indicator 
 * 							for Vehicle AbstractValue
 * 							defect 6448 Ver 5.1.5
 * K Harrell	06/20/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3	
 * J Rue		08/07/2007	Add getter/setter for LemonLawIndi
 * 							ChildSupportArrearsIndi will replace 
 * 							LemonLawIndi.
 * 							add ciLemonLawIndi 
 * 							add getLemonLawIndi(), setLemonLawIndi()
 * 							defect 9225 Ver Broadcast Messaging
 * J Rue		08/23/2007	Add parm to setLemonLawIndi()
 * 							modify setLemonLawIndi()
 * 							defect 9225 Ver Broadcast Messaging	 	
 * J Rue		03/27/2008	Add new attributes for V21, MF version V
 * 							add getChildSupportIndi(),
 * 								setChildSupportIndi()
 * 								getTtlSignDate(), setTtlSignDate()
 * 								getEtTLCd(), setEtTLCd()
 * 							defect 9581 Ver Defect_POS_A
 * J Rue		05/06/2008	Create Reset method to reset and Title data
 * 							Reset RegPltNo if RmvPltCd > 0
 * 							add ResetFields()
 * 							defect 9630 Ver Defect_POS_A
 * J Rue		05/29/2008	Add author's name
 * 							defect 9581 Ver Defect_POS_A
 * J Rue		06/04/2008	Set Ver to Defect_POS_A
 * 							defect 9581 Ver Defect_POS_A
 * J Rue		11/06/2008	Add PermLienHldrId and LienRlseDate
 * 							add getters/setters
 * 							defect 9833 Ver ELT_MfAccess
 * K Harrell	02/25/2009	add ciUTVMislblIndi,
 * 							 csVTRTtlEmrgCd1, csVTRTtlEmrgCd1, 
 * 							 get/set methods 
 * 							add isETitle()
 * 							defect 9969 Ver Defect_POS_E
 * J Rue		03/05/2009	Update @parm from int to String
 * 							modify setPermLienHldrId1(),
 * 								setPermLienHldrId2(),
 * 								setPermLienHldrId3()
 * 							defect 9961 Ver Defect_POS_E
 * K Harrell	03/10/2009	add hasLien(), clearLien1Data(), 
 * 							  clearLien2Data(), clearLien3Data()
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	03/18/2009	add setupCertfdLienInfo(),
 * 							getPermLienholderData(), 
 * 							isValidPermLienhldrId()
 * 							defect 9987 Ver Defect_POS_E
 * K Harrell	03/25/2009	add setPermLienhldrId()
 * 							defect 9979 Ver Defect_POS_E
 * K Harrell	04/01/2009	add isETitleNoLien()
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	07/03/2009	modify TitleData() 
 * 							delete caLienHolder1, caLienHolder2, 
 * 							 caLienHolder3, get/set methods 
 * 							delete ciLienHolder1Date, ciLienHolder2Date,
 * 							 ciLienHolder3Date, get/set methods
 * 							add chtLienHolderData, getLienholderData(), 
 * 							 setLienholderData() 
 * 							modify clearLien1Data(), clearLien2Data(),
 *							 clearLien3Data(), TitleData(), getTtlVehAddr()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	02/09/2010 	add csPvtLawEnfVehCd, csNonTtlGolfCartCd,
 * 							 csVTRTtlEmrgCd3, csVTRTtlEmrgCd4, get/set
 * 							 methods. 
 * 							delete csVTRTtlEmrgCd1, csVTRTtlEmrgCd2, 
 * 							 get/set methods. 
 * 							modify TitleData() 
 * 							defect 10366 Ver POS_640
 * K Harrell	10/13/2011	add ciETtlPrntDate, get/set methods
 * 							defect 10841 Ver 6.9.0 
 * K Harrell	01/10/2012	add csSurvShpRightsName1, 
 * 							 csSurvShpRightsName2,
 * 							 ciAddlSurvivorIndi, ciExportIndi, 
 * 							 ciSalvIndi, get/set methods 
 * 							defect 11231 Ver 6.10.0   
 * K Harrell	01/16/2012	add resetSurvivorData() 
 * 							defect 10827 Ver 6.10.0   
 * B Woodson	01/31/2012	remove ciSalvIndi, get/set methods 
 * 							defect 11251 Ver 6.10.0
 * K Harrell	02/02/2012	Javadoc cleanup
 * 							defect 11231 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Define Vehicle Title information. 
  *  
 * @version	6.10.0 		02/02/2012
 * @author	N Ting
 * <br>Creation Date:	  
 */

public class TitleData implements java.io.Serializable
{
	// int 
	private int ciAddlLienRecrdIndi;
	private int ciAgncyLoandIndi;
	private int ciCcoIssueDate;
	private int ciChildSupportIndi;
	private int ciCompCntyNo;
	private int ciDocTypeCd;
	private int ciETtlCd;
	// defect 10841 
	private int ciETtlPrntDate; 
	// end defect 10841 
	private int ciGovtOwndIndi;
	private int ciInspectnWaivedIndi;
	private int ciLemonLawIndi;
	private int ciLienRlseDate1;
	private int ciLienRlseDate2;
	private int ciLienRlseDate3;
	private int ciMultRegIndi;
	private int ciMustChangePltIndi;
	private int ciOwnrShpEvidCd;
	private int ciPriorCCOIssueIndi;
	private int ciSurrTtlDate;
	private int ciSurvshpRightsIndi;
	private int ciTtlApplDate;
	private int ciTtlExmnIndi;
	private int ciTtlHotCkIndi;
	private int ciTtlIssueDate;
	private int ciTtlNoMf;
	private int ciTtlRejctnDate;
	private int ciTtlRejctnOfc;
	private int ciTtlRevkdIndi;
	private int ciTtlSignDate;
	private int ciTtlTypeIndi;
	private int ciUTVMislblIndi;
	private int ciVehSoldDate;
	
	// String 
	private String csBatchNo;
	private String csBndedTtlCd;
	private String csDlrGdn;
	private String csDocNo;
	private String csLegalRestrntNo;
	private String csOldDocNo;
	private String csOthrStateCntry;
	private String csPermLienHldrId1;
	private String csPermLienHldrId2;
	private String csPermLienHldrId3;
	private String csPrevOwnrCity;
	private String csPrevOwnrName;
	private String csPrevOwnrState;
	private String csSalvStateCntry;
	private String csTtlProcsCd;
	private String csVehUnitNo;
	
	// Object 		
	private AddressData caTtlVehAddr;
	
	// Dollar 
	private Dollar caImcNo;
	private Dollar caSalesTaxPdAmt;
	private Dollar caVehicleValue;
	private Dollar caVehSalesPrice;
	private Dollar caVehTradeinAllownce;
	
	// Hashtable 
	private Hashtable chtLienholderData;
	
	// defect 10366
	private String csPvtLawEnfVehCd;
	private String csNonTtlGolfCartCd;
	private String csVTRTtlEmrgCd3;
	private String csVTRTtlEmrgCd4;
	// end defect 10366
	
	// defect 11231 
	private String csSurvShpRightsName1;
	private String csSurvShpRightsName2;
	private int ciAddlSurvivorIndi;
	private int ciExportIndi;
	// end defect 11231
	
	private final static long serialVersionUID = -3326107470102003221L;
	
	/**
	 * Is Valid PermLienholderId
	 * 
	 * @param asPermLienhldrId
	 * @return boolean
	 */
	public static boolean isValidPermLienhldrId(String asPermLienId)
	{
		return !UtilityMethods.isEmpty(asPermLienId)
			&& !UtilityMethods.isAllZeros(asPermLienId);
	}
	/**
	 * TitleData constructor comment.
	 */
	public TitleData()
	{
		super();

		caVehSalesPrice = new Dollar(0.0);
		caVehTradeinAllownce = new Dollar(0.0);
		caImcNo = new Dollar(0.0);
		caSalesTaxPdAmt = new Dollar(0.0);
		caTtlVehAddr = new AddressData();
		caVehicleValue = new Dollar(0.0);
		chtLienholderData = new Hashtable();

		chtLienholderData.put(
			TitleConstant.LIENHLDR1,
			new LienholderData());
		chtLienholderData.put(
			TitleConstant.LIENHLDR2,
			new LienholderData());
		chtLienholderData.put(
			TitleConstant.LIENHLDR3,
			new LienholderData());

		csBatchNo = CommonConstant.STR_SPACE_EMPTY;
		csBndedTtlCd = CommonConstant.STR_SPACE_EMPTY;
		csDlrGdn = CommonConstant.STR_SPACE_EMPTY;
		csDocNo = CommonConstant.STR_SPACE_EMPTY;
		csLegalRestrntNo = CommonConstant.STR_SPACE_EMPTY;
		csOldDocNo = CommonConstant.STR_SPACE_EMPTY;
		csOthrStateCntry = CommonConstant.STR_SPACE_EMPTY;
		csPermLienHldrId1 = CommonConstant.STR_SPACE_EMPTY;
		csPermLienHldrId2 = CommonConstant.STR_SPACE_EMPTY;
		csPermLienHldrId3 = CommonConstant.STR_SPACE_EMPTY;
		csPrevOwnrCity = CommonConstant.STR_SPACE_EMPTY;
		csPrevOwnrName = CommonConstant.STR_SPACE_EMPTY;
		csPrevOwnrState = CommonConstant.STR_SPACE_EMPTY;
		csSalvStateCntry = CommonConstant.STR_SPACE_EMPTY;
		csTtlProcsCd = CommonConstant.STR_SPACE_EMPTY;
		csVehUnitNo = CommonConstant.STR_SPACE_EMPTY;
		// defect 10366 
		csPvtLawEnfVehCd = CommonConstant.STR_SPACE_EMPTY;
		csNonTtlGolfCartCd = CommonConstant.STR_SPACE_EMPTY;
		csVTRTtlEmrgCd3 = CommonConstant.STR_SPACE_EMPTY;
		csVTRTtlEmrgCd4 = CommonConstant.STR_SPACE_EMPTY;
		// end defect 10366 
		caTtlVehAddr = new AddressData();
	}

	/** 
	 * Clear data associated w/ 1st Lienholder  
	 */
	public void clearLien1Data()
	{
		//setLienHolder1Date(0);

		// defect 10112 
		setLienholderData(
			TitleConstant.LIENHLDR1,
			new LienholderData());
		// end defct 10112 

		setLienRlseDate1(0);
		setPermLienHldrId1(CommonConstant.STR_SPACE_EMPTY);
	}

	/** 
	 * Clear data associated w/ 2nd Lienholder  
	 */
	public void clearLien2Data()
	{
		//setLienHolder2Date(0);

		// defect 10112 
		setLienholderData(
			TitleConstant.LIENHLDR2,
			new LienholderData());
		// end defect 10112

		setLienRlseDate2(0);
		setPermLienHldrId2(CommonConstant.STR_SPACE_EMPTY);
	}

	/** 
	 * Clear data associated w/ 3rd Lienholder  
	 */
	public void clearLien3Data()
	{
		//setLienHolder3Date(0);

		// defect 10112 
		setLienholderData(
			TitleConstant.LIENHLDR3,
			new LienholderData());
		// end defect 10112 

		setLienRlseDate3(0);
		setPermLienHldrId3(CommonConstant.STR_SPACE_EMPTY);
	}

	/**
	 * Return value of AddlLienRecrdIndi
	 * 
	 * @return int
	 */
	public int getAddlLienRecrdIndi()
	{
		return ciAddlLienRecrdIndi;
	}

	/**
	 * Return value of AddlSurvivorIndi
	 * 
	 * @return ciAddlSurvivorIndi
	 */
	public int getAddlSurvivorIndi()
	{
		return ciAddlSurvivorIndi;
	}

	/**
	 * Return value of AgncyLoandIndi
	 * 
	 * @return int
	 */
	public int getAgncyLoandIndi()
	{
		return ciAgncyLoandIndi;
	}

	/**
	 * Return value of BatchNo
	 * 
	 * @return String
	 */
	public String getBatchNo()
	{
		return csBatchNo;
	}

	/**
	 * Return value of BndedTtlCd
	 * 
	 * @return String
	 */
	public String getBndedTtlCd()
	{
		return csBndedTtlCd;
	}

	/**
	 * Return value of CcoIssueDate
	 * 
	 * @return int
	 */
	public int getCcoIssueDate()
	{
		return ciCcoIssueDate;
	}

	/**
	 * get ChildsupportIndi
	 * 
	 * @return int
	 */
	public int getChildSupportIndi()
	{
		return ciChildSupportIndi;
	}

	/**
	 * Return value of CompCntyNo
	 * 
	 * @return int
	 */
	public int getCompCntyNo()
	{
		return ciCompCntyNo;
	}

	/**
	 * Return value of DlrGdn
	 * 
	 * @return String
	 */
	public String getDlrGdn()
	{
		return csDlrGdn;
	}

	/**
	 * Return value of DocNo
	 * 
	 * @return String
	 */
	public String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Return value of DocTypeCd
	 * 
	 * @return int
	 */
	public int getDocTypeCd()
	{
		return ciDocTypeCd;
	}

	/**
	 * get ELienCd
	 * 
	 * @return int
	 */
	public int getETtlCd()
	{
		return ciETtlCd;
	}

	/**
	 * @return int
	 */
	public int getETtlPrntDate()
	{
		return ciETtlPrntDate;
	}

	/**
	 * Return value of ExportIndi
	 * 
	 * @return int 
	 */
	public int getExportIndi()
	{
		return ciExportIndi;
	}

	/**
	 * Return value of GovtOwndIndi
	 * 
	 * @return int
	 */
	public int getGovtOwndIndi()
	{
		return ciGovtOwndIndi;
	}

	/**
	 * Return value of ImcNo
	 * 
	 * @return Dollar
	 */
	public Dollar getImcNo()
	{
		return caImcNo;
	}

	/**
	 * Return value of InspectnWaivedIndi
	 * 
	 * @return int
	 */
	public int getInspectnWaivedIndi()
	{
		return ciInspectnWaivedIndi;
	}

	/**
	 * Return value of LegalRestrntNo
	 * 
	 * @return int
	 */
	public String getLegalRestrntNo()
	{
		return csLegalRestrntNo;
	}

	/**
	 * Return value of LemonLawIndi
	 * 
	 * @return int
	 */
	public int getLemonLawIndi()
	{
		return ciLemonLawIndi;
	}
	//
	//	/**
	//	 * Return value of LienHolder1Date
	//	 * 
	//	 * @return int
	//	 */
	//	public int getLienHolder1Date()
	//	{
	//		return ciLienHolder1Date;
	//	}
	//
	//	/**
	//	 * Return value of LienHolder2
	//	 * 
	//	 * @return int
	//	 */
	//	public int getLienHolder2Date()
	//	{
	//		return ciLienHolder2Date;
	//	}
	//
	//	/**
	//	 * Return value of LienHolder3Date
	//	 * 
	//	 * @return int
	//	 */
	//	public int getLienHolder3Date()
	//	{
	//		return ciLienHolder3Date;
	//	}

	/**
	 * Return LienholderData for provided Integer
	 * 
	 * @return LienholderData
	 */
	public LienholderData getLienholderData(Integer aiId)
	{
		// For transition w/ IRENEW 
		chtLienholderData =
			chtLienholderData == null
				? new Hashtable()
				: chtLienholderData;

		LienholderData laLienhldrData =
			(LienholderData) chtLienholderData.get(aiId);

		if (laLienhldrData == null)
		{
			laLienhldrData = new LienholderData();
			chtLienholderData.put(laLienhldrData, aiId);
		}
		return laLienhldrData;
	}

	// LienRlseDate
	/**
	 * Get value of LienRlseDate1
	 * 
	 * @return int
	 */
	public int getLienRlseDate1()
	{
		return ciLienRlseDate1;
	}

	/**
	 * Get value of LienRlseDate2
	 * 
	 * @return int
	 */
	public int getLienRlseDate2()
	{
		return ciLienRlseDate2;
	}

	/**
	 * Get value of LienRlseDate3
	 * 
	 * @return int
	 */
	public int getLienRlseDate3()
	{
		return ciLienRlseDate3;
	}

	/**
	 * Return value of MultRegIndi
	 * 
	 * @return int
	 */
	public int getMultRegIndi()
	{
		return ciMultRegIndi;
	}

	/**
	 * Return value of MustChangePltIndi
	 * 
	 * @return int
	 */
	public int getMustChangePltIndi()
	{
		return ciMustChangePltIndi;
	}

	/**
	 * Get value of csNonTtlGolfCartCd
	 * 
	 * @return String
	 */
	public String getNonTtlGolfCartCd()
	{
		return csNonTtlGolfCartCd;
	}

	/**
	 * Return value of OldDocNo
	 * 
	 * @return String
	 */
	public String getOldDocNo()
	{
		return csOldDocNo;
	}

	//	/**
	//	 * Set value of LienHolder1
	//	 * 
	//	 * @param aaLienHolder1LienholderData
	//	 */
	//	public void setLienHolder1(LienholderData aaLienHolder1)
	//	{
	//		caLienHolder1 = aaLienHolder1;
	//	}

	/**
	 * Return value of OthrStateCntry
	 * 
	 * @return String
	 */
	public String getOthrStateCntry()
	{
		return csOthrStateCntry;
	}

	/**
	 * Return value of OwnrShpEvidCd
	 * 
	 * @return String
	 */
	public int getOwnrShpEvidCd()
	{
		return ciOwnrShpEvidCd;
	}

	// defect 9883
	//	Add PERMLIENHLDRID and LIENRLSEDATE
	// PermLienHldrId
	/**
	 * Get value of PermLienHldrId1
	 * 
	 * @return String
	 */
	public String getPermLienHldrId1()
	{
		return csPermLienHldrId1 == null
			? CommonConstant.STR_SPACE_EMPTY
			: csPermLienHldrId1.trim();
	}

	/**
	 * Get value of PermLienHldrId2
	 * 
	 * @return String
	 */
	public String getPermLienHldrId2()
	{
		return csPermLienHldrId2 == null
			? CommonConstant.STR_SPACE_EMPTY
			: csPermLienHldrId2.trim();
	}

	/**
	 * Get value of PermLienHldrId3
	 * 
	 * @return String
	 */
	public String getPermLienHldrId3()
	{
		return csPermLienHldrId3 == null
			? CommonConstant.STR_SPACE_EMPTY
			: csPermLienHldrId3.trim();
	}

	/** 
	 * Assign PermLienholderData to Title LieholderData Objects
	 * 
	 * @param aaTitleData
	 * @return LienholderData 
	 */
	private LienholderData getPermLienholderData(String lsPermLienholderId)
	{
		LienholderData laLienData = null;
		CertifiedLienholderData laData =
			CertifiedLienholderCache.getLatestCertfdLienhldr(
				lsPermLienholderId);
		if (laData != null)
		{
			laLienData = (LienholderData) laData.getLienholderData();
		}
		else
		{
			laLienData = new LienholderData();
		}
		return laLienData;
	}

	/**
	 * Return value of PrevOwnrCity
	 * 
	 * @return String
	 */
	public String getPrevOwnrCity()
	{
		return csPrevOwnrCity;
	}

	/**
	 * Return value of PrevOwnrName
	 * 
	 * @return String
	 */
	public String getPrevOwnrName()
	{
		return csPrevOwnrName;
	}

	/**
	 * Return value of PrevOwnrState
	 * 
	 * @return String
	 */
	public String getPrevOwnrState()
	{
		return csPrevOwnrState;
	}

	/**
	 * Return value of PriorCCOIssueIndi
	 * 
	 * @return int
	 */
	public int getPriorCCOIssueIndi()
	{
		return ciPriorCCOIssueIndi;
	}

	/**
	 * Get value of csPvtLawEnfVehCd
	 * 
	 * @return String
	 */
	public String getPvtLawEnfVehCd()
	{
		return csPvtLawEnfVehCd;
	}

	/**
	 * Return value of SalesTaxPdAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getSalesTaxPdAmt()
	{
		return caSalesTaxPdAmt;
	}

	/**
	 * Return value of SalvStateCntry
	 * 
	 * @return String
	 */
	public String getSalvStateCntry()
	{
		return csSalvStateCntry;
	}

	/**
	 * Return value of SurrTtlDate
	 * 
	 * @return int
	 */
	public int getSurrTtlDate()
	{
		return ciSurrTtlDate;
	}

	/**
	 * Return value of SurvshpRightsIndi
	 * 
	 * @return int
	 */
	public int getSurvshpRightsIndi()
	{
		return ciSurvshpRightsIndi;
	}

	/**
	 * Return value of SurvShpRightsName1
	 * 
	 * @return String 
	 */
	public String getSurvShpRightsName1()
	{
		return csSurvShpRightsName1;
	}

	/**
	 * Return value of SurvShpRightsName2
	 * 
	 * @return String
	 */
	public String getSurvShpRightsName2()
	{
		return csSurvShpRightsName2;
	}

	/**
	 * Return value of TtlApplDate
	 * 
	 * @return int
	 */
	public int getTtlApplDate()
	{
		return ciTtlApplDate;
	}

	/**
	 * Return value of TtlExmnIndi
	 * 
	 * @return int
	 */
	public int getTtlExmnIndi()
	{
		return ciTtlExmnIndi;
	}

	/**
	 * Return value of TtlHotCkIndi
	 * 
	 * @return int
	 */
	public int getTtlHotCkIndi()
	{
		return ciTtlHotCkIndi;
	}

	/**
	 * Return value of TtlIssueDate
	 * 
	 * @return int
	 */
	public int getTtlIssueDate()
	{
		return ciTtlIssueDate;
	}

	/**
	 * Return value of TtlNoMf
	 * 
	 * @return int
	 */
	public int getTtlNoMf()
	{
		return ciTtlNoMf;
	}

	/**
	 * Return value of TtlProcsCd
	 * 
	 * @return String
	 */
	public String getTtlProcsCd()
	{
		return csTtlProcsCd;
	}

	/**
	 * Return value of TtlRejctnDate
	 * 
	 * @return int
	 */
	public int getTtlRejctnDate()
	{
		return ciTtlRejctnDate;
	}

	/**
	 * Return value of TtlRejctnOfc
	 * 
	 * @return int
	 */
	public int getTtlRejctnOfc()
	{
		return ciTtlRejctnOfc;
	}

	/**
	 * Return value of TtlRevkdIndi
	 * 
	 * @return int
	 */
	public int getTtlRevkdIndi()
	{
		return ciTtlRevkdIndi;
	}

	/**
	 * get TtlSignDate
	 * 
	 * @return int
	 */
	public int getTtlSignDate()
	{
		return ciTtlSignDate;
	}

	/**
	 * Return value of TtlTypeIndi
	 * 
	 * @return int
	 */
	public int getTtlTypeIndi()
	{
		return ciTtlTypeIndi;
	}

	/**
	 * Return value of TtlVehAddr
	 * 
	 * @return AddressData
	 */
	public AddressData getTtlVehAddr()
	{
		// defect 10112 
		caTtlVehAddr =
			caTtlVehAddr == null ? new AddressData() : caTtlVehAddr;
		// end defect 10112  
		return caTtlVehAddr;
	}

	/**
	 * Get value of UTVMislblIndi
	 * 
	 * @return int
	 */
	public int getUTVMislblIndi()
	{
		return ciUTVMislblIndi;
	}

	/**
	 * Return value of VehicleValue
	 * 
	 * @return Dollar
	 */
	public Dollar getVehicleValue()
	{
		return caVehicleValue;
	}

	/**
	 * Return value of VehSalesPrice 
	 * 
	 * @return Dollar
	 */
	public Dollar getVehSalesPrice()
	{
		return caVehSalesPrice;
	}

	/**
	 * Return value of VehSoldDate
	 * 
	 * @return int
	 */
	public int getVehSoldDate()
	{
		return ciVehSoldDate;
	}

	/**
	 * Return value of VehTradeinAllownce
	 * 
	 * @return Dollar
	 */
	public Dollar getVehTradeinAllownce()
	{
		return caVehTradeinAllownce;
	}

	/**
	 * Return value of VehUnitNo
	 * 
	 * @return String
	 */
	public String getVehUnitNo()
	{
		return csVehUnitNo;
	}

	/**
	* Gets value of csVTRTtlEmrgCd3
	* 
	* @return String 
	*/
	public String getVTRTtlEmrgCd3()
	{
		return csVTRTtlEmrgCd3;
	}

	/**
	 * Gets value of csVTRTtlEmrgCd4
	 * 
	 * @return String 
	 */
	public String getVTRTtlEmrgCd4()
	{
		return csVTRTtlEmrgCd4;
	}

	/**
	 * Has Lien
	 * 
	 * @return boolean 
	 */
	public boolean hasLien()
	{
		boolean lbLienValid = false;
		for (int i = 1; i <= 3; i++)
		{
			LienholderData laLienData =
				getLienholderData(new Integer(i));
			lbLienValid =
				laLienData != null && laLienData.isPopulated();

			if (lbLienValid)
			{
				break;
			}
		}
		return lbLienValid;
	}

	/**
	 * Is ETitle
	 * 
	 * @return boolean
	 */
	public boolean isETitle()
	{
		return ciETtlCd == TitleConstant.ELECTRONIC_ETTLCD;
	}

	/**
	 * Is ETitle && No Lien
	 * 
	 * @return boolean
	 */
	public boolean isETitleNoLien()
	{
		return isETitle() && UtilityMethods.isEmpty(csPermLienHldrId1);
	}

	/**
	 * Reset select fields, Multiple Titles (CICS R04)
	 * 
	 * @param avPartialData
	 * @return RegistrationData 
	 */
	public Vector resetFields(Vector avPartialData)
	{
		Vector lvData = avPartialData;
		Vector lvRtnData = new Vector();

		for (int liIndex = 0; liIndex < lvData.size(); ++liIndex)
		{
			MFPartialData laPartialData =
				(MFPartialData) lvData.get(liIndex);
			// defect 9630
			//	Set RegPltNo = NOPLATE if PltRmvdCd > 0
			if (laPartialData.getPltRmvCd() > 0)
			{
				laPartialData.setRegPltNo(RegistrationConstant.NOPLATE);
			}
			// end defect 9630

			// Add Regis record to return vector
			lvRtnData.add(liIndex, laPartialData);
		}

		return lvRtnData;
	}
	/**
	 * Reset Survivor fields
	 * 
	 */
	public void resetSurvivorData()
	{
		setAddlSurvivorIndi(0);
		setSurvShpRightsName1(new String()); 
		setSurvShpRightsName2(new String());
	}

	/**
	 * Set value of AddlLienRecrdIndi
	 * 
	 * @param aiAddlLienRecrdIndi int
	 */
	public void setAddlLienRecrdIndi(int aiAddlLienRecrdIndi)
	{
		ciAddlLienRecrdIndi = aiAddlLienRecrdIndi;
	}

	/**
	 * Set value of AddlSurvivorIndi 
	 * 
	 * @param aiAddlSurvivorIndi 
	 */
	public void setAddlSurvivorIndi(int aiAddlSurvivorIndi)
	{
		ciAddlSurvivorIndi = aiAddlSurvivorIndi;
	}

	/**
	 * Set value of AgncyLoandIndi
	 * 
	 * @param aiAgncyLoandIndi int
	 */
	public void setAgncyLoandIndi(int aiAgncyLoandIndi)
	{
		ciAgncyLoandIndi = aiAgncyLoandIndi;
	}

	/**
	 * Set value of BatchNo 
	 * 
	 * @param asBatchNo String
	 */
	public void setBatchNo(String asBatchNo)
	{
		csBatchNo = asBatchNo;
	}

	/**
	 * Set value of BndedTtlCd
	 * 
	 * @param asBndedTtlCd String
	 */
	public void setBndedTtlCd(String asBndedTtlCd)
	{
		csBndedTtlCd = asBndedTtlCd;
	}

	/**
	 * Set value of CcoIssueDate
	 * 
	 * @param aiCcoIssueDate int
	 */
	public void setCcoIssueDate(int aiCcoIssueDate)
	{
		ciCcoIssueDate = aiCcoIssueDate;
	}

	/**
	 * set ChildSupportIndi
	 * 
	 * @param aiChildSupportIndi
	 */
	public void setChildSupportIndi(int aiChildSupportIndi)
	{
		ciChildSupportIndi = aiChildSupportIndi;
	}
	
	/**
	 * Set value of CompCntyNo
	 * 
	 * @param aiCompCntyNo int
	 */
	public void setCompCntyNo(int aiCompCntyNo)
	{
		ciCompCntyNo = aiCompCntyNo;
	}

	/**
	 * Set value of DlrGdn
	 * 
	 * @param asDlrGdn String
	 */
	public void setDlrGdn(String asDlrGdn)
	{
		csDlrGdn = asDlrGdn;
	}

	/**
	 * Set value of DocNo
	 * 
	 * @param asDocNo String
	 */
	public void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * Set value of DocTypeCd
	 * 
	 * @param aiDocTypeCd int
	 */
	public void setDocTypeCd(int aiDocTypeCd)
	{
		ciDocTypeCd = aiDocTypeCd;
	}

	/**
	 * set ETtlCd
	 * 
	 * @param aiETtlCd 
	 */
	public void setETtlCd(int aiETtlCd)
	{
		ciETtlCd = aiETtlCd;
	}

	//	/**
	//	 * Set value of LienHolder1Date
	//	 * 
	//	 * @param aiLienHolder1Date int
	//	 */
	//	public void setLienHolder1Date(int aiLienHolder1Date)
	//	{
	//		ciLienHolder1Date = aiLienHolder1Date;
	//	}

	//	/**
	//	 * Set value of LienHolder2
	//	 * 
	//	 * @param aaLienHolder2LienholderData
	//	 */
	//	public void setLienHolder2(LienholderData aaLienHolder2)
	//	{
	//		caLienHolder2 = aaLienHolder2;
	//	}

	//	/**
	//	 * Set value of LienHolder2Date
	//	 * 
	//	 * @param aaLienHolder2Date int
	//	 */
	//	public void setLienHolder2Date(int aaLienHolder2Date)
	//	{
	//		ciLienHolder2Date = aaLienHolder2Date;
	//	}
	//
	//	/**
	//	 * Set value of LienHolder3
	//	 * 
	//	 * @param aaLienHolder3LienholderData
	//	 */
	//	public void setLienHolder3(LienholderData aaLienHolder3)
	//	{
	//		caLienHolder3 = aaLienHolder3;
	//	}

	//	/**
	//	 * Set value of LienHolder3Date
	//	 * 
	//	 * @param aiLienHolder3Date int
	//	 */
	//	public void setLienHolder3Date(int aiLienHolder3Date)
	//	{
	//		ciLienHolder3Date = aiLienHolder3Date;
	//	}

	/**
	 * set ETtlPrntDate 
	 * 
	 * @param aiETtlPrntDate
	 */
	public void setETtlPrntDate(int aiETtlPrntDate)
	{
		ciETtlPrntDate = aiETtlPrntDate;
	}

	/**
	 * Set value of ExportIndi 
	 * 
	 * @param aiExportIndi 
	 */
	public void setExportIndi(int aiExportIndi)
	{
		ciExportIndi = aiExportIndi;
	}

	/**
	 * Set value of GovtOwndIndi
	 * 
	 * @param aiGovtOwndIndi int
	 */
	public void setGovtOwndIndi(int aiGovtOwndIndi)
	{
		ciGovtOwndIndi = aiGovtOwndIndi;
	}

	/**
	 * Set value of ImcNo
	 * 
	 * @param aaImcNo Dollar
	 */
	public void setImcNo(Dollar aaImcNo)
	{
		caImcNo = aaImcNo;
	}

	/**
	 * Set value of InspectnWaivedIndi
	 * 
	 * @param aiInspectnWaivedIndi int
	 */
	public void setInspectnWaivedIndi(int aiInspectnWaivedIndi)
	{
		ciInspectnWaivedIndi = aiInspectnWaivedIndi;
	}

	/**
	 * Set value of LegalRestrntNo
	 * 
	 * @param asLegalRestrntNo int
	 */
	public void setLegalRestrntNo(String asLegalRestrntNo)
	{
		csLegalRestrntNo = asLegalRestrntNo;
	}

	/**
	* Return value of LemonLawIndi
	* 
	* @return int
	*/
	public void setLemonLawIndi(int aiLemonLaw)
	{
		ciLemonLawIndi = aiLemonLaw;
	}

	/**
	 * Set LienholderData for provided Integer
	 * 
	 * @param data
	 */
	public void setLienholderData(
		Integer aiId,
		LienholderData aaLienhldrData)
	{
		chtLienholderData.put(aiId, aaLienhldrData);
	}

	/**
	 * Set LienholderData for provided Integer
	 * 
	 * @param data
	 */
	public void setLienholderData(
		Integer aiId,
		LienholderData aaLienhldrData,
		int aiLienDate)
	{
		aaLienhldrData.setLienDate(aiLienDate);
		chtLienholderData.put(aiId, aaLienhldrData);
	}

	/**
	 * Set value of LienRlseDate1
	 * 
	 * @param aiLienRlseDate1 int
	 */
	public void setLienRlseDate1(int aiLienRlseDate1)
	{
		ciLienRlseDate1 = aiLienRlseDate1;
	}

	/**
	 * Set value of LienRlseDate2
	 * 
	 * @param aiLienRlseDate2 int
	 */
	public void setLienRlseDate2(int aiLienRlseDate2)
	{
		ciLienRlseDate2 = aiLienRlseDate2;
	}
	/**
	 * Set value of LienRlseDate3
	 * 
	 * @param aiLienRlseDate3 int
	 */
	public void setLienRlseDate3(int aiLienRlseDate3)
	{
		ciLienRlseDate3 = aiLienRlseDate3;
	}

	/**
	 * Set value of MultRegIndi
	 * 
	 * @param aiMultRegIndi int
	 */
	public void setMultRegIndi(int aiMultRegIndi)
	{
		ciMultRegIndi = aiMultRegIndi;
	}

	/**
	 * Set value of MustChangePltIndi
	 * 
	 * @param aiMustChangePltIndi int
	 */
	public void setMustChangePltIndi(int aiMustChangePltIndi)
	{
		ciMustChangePltIndi = aiMustChangePltIndi;
	}

	/**
	 * Set value of csNonTtlGolfCartCd
	 * 
	 * @param asNonTtlGolfCartCd
	 */
	public void setNonTtlGolfCartCd(String asNonTtlGolfCartCd)
	{
		csNonTtlGolfCartCd = asNonTtlGolfCartCd;
	}

	/**
	 * Set value of OldDocNo
	 * 
	 * @param asOldDocNo String
	 */
	public void setOldDocNo(String asOldDocNo)
	{
		csOldDocNo = asOldDocNo;
	}

	/**
	 * Set value of OthrStateCntry
	 * 
	 * @param asOthrStateCntry String
	 */
	public void setOthrStateCntry(String asOthrStateCntry)
	{
		csOthrStateCntry = asOthrStateCntry;
	}

	/**
	 * Set value of OwnrShpEvidCd
	 * 
	 * @param aiOwnrShpEvidCd String
	 */
	public void setOwnrShpEvidCd(int aiOwnrShpEvidCd)
	{
		ciOwnrShpEvidCd = aiOwnrShpEvidCd;
	}
	/**
	 * 
	 * Set PermLienHldrId 
	 * 
	 * @param aiIdNo
	 */
	public void setPermLienHldrId(int aiNo, String asValue)
	{
		switch (aiNo)
		{
			case 1 :
				{
					setPermLienHldrId1(asValue);
					break;
				}
			case 2 :
				{
					setPermLienHldrId2(asValue);
					break;
				}
			case 3 :
				{
					setPermLienHldrId3(asValue);
					break;
				}
		}

	}

	/**
	 * Set value of PermLienHldrId1
	 * 
	 * @param aiPermLienHldrId1 String
	 */
	public void setPermLienHldrId1(String asPermLienHldrId1)
	{
		csPermLienHldrId1 = asPermLienHldrId1;
	}

	/**
	 * Set value of PermLienHldr2Id
	 * 
	 * @param asPermLienHldrId2 String
	 */
	public void setPermLienHldrId2(String asPermLienHldrId2)
	{
		csPermLienHldrId2 = asPermLienHldrId2;
	}
	/**
	 * Set value of PermLienHldrId3
	 * 
	 * @param asPermLienHldrId3 String
	 */
	public void setPermLienHldrId3(String asPermLienHldrId3)
	{
		csPermLienHldrId3 = asPermLienHldrId3;
	}

	/**
	 * Set value of PrevOwnrCity
	 * 
	 * @param asPrevOwnrCity String
	 */
	public void setPrevOwnrCity(String asPrevOwnrCity)
	{
		csPrevOwnrCity = asPrevOwnrCity;
	}

	/**
	 * Set value of PrevOwnrName
	 * 
	 * @param asPrevOwnrName String
	 */
	public void setPrevOwnrName(String asPrevOwnrName)
	{
		csPrevOwnrName = asPrevOwnrName;
	}

	/**
	 * Set value of PrevOwnrState
	 * 
	 * @param asPrevOwnrState String
	 */
	public void setPrevOwnrState(String asPrevOwnrState)
	{
		csPrevOwnrState = asPrevOwnrState;
	}

	/**
	 * Set value of PriorCCOIssueIndi
	 * 
	 * @param aiPriorCCOIssueIndi int
	 */
	public void setPriorCCOIssueIndi(int aiPriorCCOIssueIndi)
	{
		ciPriorCCOIssueIndi = aiPriorCCOIssueIndi;
	}

	/**
	 * Set value of VTRTTLEmrgCd1
	 * 
	 * @param asVTRTTLEmrgCd1
	 */
	public void setPvtLawEnfVehCd(String asVTRTTLEmrgCd1)
	{
		csPvtLawEnfVehCd = asVTRTTLEmrgCd1;
	}

	/**
	 * Set value of SalesTaxPdAmt
	 * 
	 * @param aaSalesTaxPdAmt Dollar
	 */
	public void setSalesTaxPdAmt(Dollar aaSalesTaxPdAmt)
	{
		caSalesTaxPdAmt = aaSalesTaxPdAmt;
	}

	/**
	 * Set value of SalvStateCntry
	 * 
	 * @param asSalvStateCntry String
	 */
	public void setSalvStateCntry(String asSalvStateCntry)
	{
		csSalvStateCntry = asSalvStateCntry;
	}

	/**
	 * Set value of SurrTtlDate
	 * 
	 * @param aiSurrTtlDate int
	 */
	public void setSurrTtlDate(int aiSurrTtlDate)
	{
		ciSurrTtlDate = aiSurrTtlDate;
	}

	/**
	 * Set value of SurvshpRightsIndi
	 * 
	 * @param aiSurvshpRightsIndi int
	 */
	public void setSurvshpRightsIndi(int aiSurvshpRightsIndi)
	{
		ciSurvshpRightsIndi = aiSurvshpRightsIndi;
	}

	/**
	 * Set value of SurvShpRightsName1
	 * 
	 * @param asSurvShpRightsName1 
	 */
	public void setSurvShpRightsName1(String asSurvShpRightsName1)
	{
		csSurvShpRightsName1 = asSurvShpRightsName1;
	}

	/**
	 * Set value of SurvShpRightsName2
	 * 
	 * @param asSurvShpRightsName2 
	 */
	public void setSurvShpRightsName2(String asSurvShpRightsName2)
	{
		csSurvShpRightsName2 = asSurvShpRightsName2;
	}

	/**
	 * Set value of TtlApplDate
	 * 
	 * @param aiTtlApplDate int
	 */
	public void setTtlApplDate(int aiTtlApplDate)
	{
		ciTtlApplDate = aiTtlApplDate;
	}

	/**
	 * Set value of TtlExmnIndi
	 * 
	 * @param aiTtlExmnIndi int
	 */
	public void setTtlExmnIndi(int aiTtlExmnIndi)
	{
		ciTtlExmnIndi = aiTtlExmnIndi;
	}

	/**
	 * Set value of TtlHotCkIndi
	 * 
	 * @param aiTtlHotCkIndi int
	 */
	public void setTtlHotCkIndi(int aiTtlHotCkIndi)
	{
		ciTtlHotCkIndi = aiTtlHotCkIndi;
	}

	/**
	 * Set value of TtlIssueDate
	 * 
	 * @param aiTtlIssueDate int
	 */
	public void setTtlIssueDate(int aiTtlIssueDate)
	{
		ciTtlIssueDate = aiTtlIssueDate;
	}
	/**
	 * Set value of TtlNoMf
	 * 
	 * @param aiTtlNoMf int
	 */
	public void setTtlNoMf(int aiTtlNoMf)
	{
		ciTtlNoMf = aiTtlNoMf;
	}

	/**
	 * Set value of TtlProcsCd 
	 * 
	 * @param asTtlProcsCd String
	 */
	public void setTtlProcsCd(String asTtlProcsCd)
	{
		csTtlProcsCd = asTtlProcsCd;
	}

	/**
	 * Set value of TtlRejctnDate
	 * 
	 * @param aiTtlRejctnDate int
	 */
	public void setTtlRejctnDate(int aiTtlRejctnDate)
	{
		ciTtlRejctnDate = aiTtlRejctnDate;
	}

	/**
	 * Set value of TtlRejctnOfc
	 * 
	 * @param aiTtlRejctnOfc int
	 */
	public void setTtlRejctnOfc(int aiTtlRejctnOfc)
	{
		ciTtlRejctnOfc = aiTtlRejctnOfc;
	}

	/**
	 * Set value of TtlRevkdIndi
	 * 
	 * @param aiTtlRevkdIndi int
	 */
	public void setTtlRevkdIndi(int aiTtlRevkdIndi)
	{
		ciTtlRevkdIndi = aiTtlRevkdIndi;
	}

	/**
	 * set TtlSignDate
	 * 
	 * @param aiTtlSignDate
	 */
	public void setTtlSignDate(int aiTtlSignDate)
	{
		ciTtlSignDate = aiTtlSignDate;
	}

	/**
	 * Set value of TtlTypeIndi
	 * 
	 * @param aiTtlTypeIndi int
	 */
	public void setTtlTypeIndi(int aiTtlTypeIndi)
	{
		ciTtlTypeIndi = aiTtlTypeIndi;
	}

	/**
	 * Set value of TtlVehAddr
	 * 
	 * @param aaTtlVehAddr AddressData
	 */
	public void setTtlVehAddr(AddressData aaTtlVehAddr)
	{
		caTtlVehAddr = aaTtlVehAddr;
	}

	/**
	 * Setup Certified Lienholder Info  
	 */
	public void setupCertfdLienInfo()
	{
		// FIRST LIEN
		LienholderData laLienData =
			getLienholderData(TitleConstant.LIENHLDR1);

		if (laLienData != null
			&& UtilityMethods.isEmpty(laLienData.getName1())
			&& isValidPermLienhldrId(csPermLienHldrId1))
		{
			setLienholderData(
				TitleConstant.LIENHLDR1,
				getPermLienholderData(csPermLienHldrId1),
				laLienData.getLienDate());
		}

		// SECOND LIEN
		laLienData = getLienholderData(TitleConstant.LIENHLDR2);
		if (laLienData != null
			&& UtilityMethods.isEmpty(laLienData.getName1())
			&& isValidPermLienhldrId(csPermLienHldrId2))
		{
			setLienholderData(
				TitleConstant.LIENHLDR2,
				getPermLienholderData(csPermLienHldrId2),
				laLienData.getLienDate());
		}

		// THIRD LIEN 
		laLienData = getLienholderData(TitleConstant.LIENHLDR3);
		if (laLienData != null
			&& UtilityMethods.isEmpty(laLienData.getName1())
			&& isValidPermLienhldrId(csPermLienHldrId3))
		{
			setLienholderData(
				TitleConstant.LIENHLDR3,
				getPermLienholderData(csPermLienHldrId3),
				laLienData.getLienDate());
		}
	}

	/**
	 * Set value of UTVMislblIndi
	 * 
	 * @param aiUTVMislblIndi
	 */
	public void setUTVMislblIndi(int aiUTVMislblIndi)
	{
		ciUTVMislblIndi = aiUTVMislblIndi;
	}

	/**
	 * Set value of VehicleValue 
	 * 
	 * @param aaVehicleValue Dollar
	 */
	public void setVehicleValue(Dollar aaVehicleValue)
	{
		caVehicleValue = aaVehicleValue;
	}

	/**
	 * Set value of VehSalesPrice
	 * 
	 * @param aaVehSalesPrice Dollar
	 */
	public void setVehSalesPrice(Dollar aaVehSalesPrice)
	{
		caVehSalesPrice = aaVehSalesPrice;
	}

	/**
	 * Set value of VehSoldDate
	 * 
	 * @param aiVehSoldDate int
	 */
	public void setVehSoldDate(int aiVehSoldDate)
	{
		ciVehSoldDate = aiVehSoldDate;
	}

	/**
	 * Set value of VehTradeinAllownce
	 * 
	 * @param aaVehTradeinAllownce Dollar
	 */
	public void setVehTradeinAllownce(Dollar aaVehTradeinAllownce)
	{
		caVehTradeinAllownce = aaVehTradeinAllownce;
	}

	/**
	 * Set value of VehUnitNo
	 * 
	 * @param asVehUnitNo String
	 */
	public void setVehUnitNo(String asVehUnitNo)
	{
		csVehUnitNo = asVehUnitNo;

	}

	/**
	 * Sets value of csVTRTtlEmrgCd3
	 * 
	 * @param asVTRTtlEmrgCd3
	 */
	public void setVTRTtlEmrgCd3(String asVTRTtlEmrgCd3)
	{
		csVTRTtlEmrgCd3 = asVTRTtlEmrgCd3;
	}

	/**
	 * Sets value of csVTRTtlEmrgCd4
	 * 
	 * @param asVTRTtlEmrgCd4
	 */
	public void setVTRTtlEmrgCd4(String asVTRTtlEmrgCd4)
	{
		csVTRTtlEmrgCd4 = asVTRTtlEmrgCd4;
	}

}
