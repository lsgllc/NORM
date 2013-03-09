package com.txdot.isd.rts.services.data;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Vector;

import com.txdot.isd.rts.services.util.Displayable;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.TitleConstant;
/*
 *
 * MFVehicleData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/18/2001	Added columns/join for SendTrans
 *                         	(OfcIssuanceCd,TransEmpId)
 * K Harrell	05/19/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * K Harrell	09/28/2006	New Columns for Exempts Project
 * 					 		add csHoopsRegPltNo,ciPltBirthDate,
 * 							    caVehValue,caEmissionSalesTax,
 * 							    ciLemonLawIndi
 * 							add getHoopsRegPltNo(),setHoopsRegPltNo()
 * 							    getPltBirthDate(),setPltBirthDate(),
 * 								getVehValue(), setVehValue()
 * 								getEmissionSalesTax(),setEmissionSalesTax()
 * 								getLemonLawIndi(), setLemonLawIndi()
 * 							defect 8901,8902,8903 Ver Exempts 
 * J Rue		04/07/2008	New attributes for V21, MF V version
 * 							add getTtlTrnsfrPntlyExmptCd(),
 * 								setTtlTrnsfrPntlyExmptCd()
 * 								getTtlTrnsfrEntCd(), setTtlTrnsfrEntCd()
 * 								getDissociateCd(), setDissociateCd()
 * 								getChildSupportIndi, setChildSupportIndi()
 * 								getETtlCd, setETtlCd
 * 								getPrismLvlCd(), setPrismlvlCd()
 * 								getTtlSignDate(), setTtlSighDate()
 * 								getV21PltId, setV21PltId()
 * 								getV21VTNId(), setV21VTNId()
 * 								getVTNSource(), setVTNSource()
 * 							defect 9581 Ver 3_AMIGOS_PH_B
 * B Hargrove	04/16/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							delete caCustBaseRegFee, caCustDieselFee,
 * 							getCustBaseRegFee(), getCustDieselFee(),
 * 							setCustBaseRegFee(), setCustDieselFee()
 * 							defect 9631 Ver Defect POS A
 * K Harrell	05/21/2008	removed csTtlTrnsfrPntlyExmptCd, get/set
 * 							methods 
 * 							defect 9581 Ver Defect POS A
 * K Harrell	06/22/2008	restore csTtlTrnsfrPntlyExmptCd, get/set
 * 							methods 
 * 							defect 9724 Ver Defect POS A
 * J Rue		10/15/2008	Add getters and setters for PERMLIENHLDRID 
 * 							and LIENRLSEDATE not in Defect_POC_D
 * 							add setLien1RlseDate(), setLien2RlseDate()
 * 								setLien3RlseDate(), getLien1RlseDate(),
 * 								getLien2RlseDate(), getLien3RlseDate()
 * 								setPermLien1HldrID(), setPermLien2HldrID()
 * 								setPermLien3HldrID(), getPermLien1HldrID()
 * 								getPermLien2HldrID(), getPermLien1HldrID()
 * 							defect 9833 Ver ELT_MfAccess
 * J Rue		11/07/2008	Reset getter/setters to upper/lower case
 * 							modify setLien1RlseDate(), setLien2RlseDate()
 * 								setLien3RlseDate(), getLien1RlseDate(),
 * 								getLien2RlseDate(), getLien3RlseDate()
 * 								setPermLien1HldrID(), setPermLien2HldrID()
 * 								setPermLien3HldrID(), getPermLien1HldrID()
 * 								getPermLien2HldrID(), getPermLien1HldrID()
 * 							defect 9833 Ver Defect_POS_B
 * J Rue		12/31/2008	The getters and setters for PERMLIENHLDRID 
 * 							and LIENRLSEDATE were not in Defect_POC_D
 * 							Add these getters and setters 
 * 							add setLien1RlseDate(), setLien2RlseDate()
 * 								setLien3RlseDate(), getLien1RlseDate(),
 * 								getLien2RlseDate(), getLien3RlseDate()
 * 								setPermLien1HldrID(), setPermLien2HldrID()
 * 								setPermLien3HldrID(), getPermLien1HldrID()
 * 								getPermLien2HldrID(), getPermLien1HldrID()
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		01/07/2009	Update comments for 12/31 and 10/15
 * 							defect 9655 Ver Defect_POS_D
 * J Rue		02/25/2009	Add csAAMVAMsgId, csUTVMislblIndi, 
 * 							 csVTRRegEmrgCd1, csVTRRegEmrgCd2,
 * 							 csVTRTtlEmrgCd1, csVTRTtlEmrgCd2 
 * 							 get/set methods 
 * 							defect 9961 Ver Defect_POS_E
 * K Harrell	02/25/2009	PermLienxHldrId needs to be String 
 * 				 			delete ciPermLienxHldrId, get/set methods
 * 							delete csAAMVAMsgId, get/set methods 
 * 							add csPermLienHldrId1-3, get/set methods
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	04/08/2009	delete csAAMVAMsgId, get/set methods 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/02/2009	Implement Hashmap of LienholderData ,new 
 * 							 OwnerData, VehLocAddrData, RenewalAddrData 
 * 							add cmLienholderData
 * 							add getLienholderData(), 
 * 								setLienholderData(), getRenewalAddrData(),
 * 							    setRenewalAddrData(), getVehLocAddrData(),
 * 							    setVehLocAddr()
 * 							delete ciLien1Date, ciLien2Date, ciLien3Date, 
 * 							 get/set methods. 
 * 							delete csLienxxx, get/set methods
 * 							modify MotorVehicleFunctionTransactionData() 
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	07/03/2009	Allow creation of MFVehicleData 
 * 							add getMFVehicleData(), getRegistrationData(),
 * 							 getTitleData(), getSalvageData(), 
 * 							 getVehicleData()
 * K Harrell	10/14/2009	delete ciPrivacyOptCd, get/set methods
 * 							defect 10246 Ver Defect_POS_G 
 * K Harrell	10/16/2009	Modify for ShowCache
 * 							modify getAttributes()
 * 							defect 10191 Ver Defect_POS_G
 * K Harrell	03/23/2010	add csPvtLawEnfVehCd, csNonTtlGolfCartCd,
 * 							 csVTRTtlEmrgCd3, csVTRTtlEmrgCd4,
 *  						 ciSOReelectionIndi, csRecpntEMail 	
 * 							 get/set methods. 
 * 							delete csVTRTtlEmrgCd1, csVTRTtlEmrgCd2, 
 * 							 get/set methods. 
 *   						defect 10366 Ver POS_640 
 * K Harrell 	06/14/2010 	add csTransId, get/set methods
 * 							defect 10505 Ver 6.5.0
 * K Harrell	06/15/2010	delete ciSOReelectionIndi, get/set methods
 * 							add ciEMailRenwlReqCd, get/set methods
 * 							defect 10508 Ver 6.5.0  
 * K Harrell	09/20/2010	add ciTtlExmnIndi, get/set methods 
 * 							defect 10013 Ver 6.6.0 
 * K Harrell	01/05/2011	add csVehMjrColorCd, csVehMnrColorCd, 
 * 							  get/set methods
 * 							defect 10712 Ver 6.7.0 
 * K Harrell	10/25/2011	add ciETtlPrntDate, get/set methods 
 * 							defect 10841 Ver 6.9.0 
 * K Harrell	01/10/2012	add csSurvShpRightsName1, 
 * 							 csSurvShpRightsName2,
 * 							 ciAddlSurvivorIndi, ciExportIndi, 
 * 							 ciSalvIndi, get/set methods 
 * 							defect 11231 Ver 6.10.0  
 * K Harrell	02/02/2012	Javadoc cleanup
 * 							defect 11231 Ver 6.10.0  
 * ---------------------------------------------------------------------
 */

/** 
 * This Data class contains attributes and get set methods for 
 * MotorVehicleFunctionTransactionData 
 * 
 * @version	6.10.0			02/02/2012
 * @author	Kathy Harrell 
 * @simce					09/18/2001  
 */

public class MotorVehicleFunctionTransactionData
	implements Serializable, Displayable
{

	//	Dollar 		
	private Dollar caCustActulRegFee;
	private Dollar caEmissionSalesTax;
	private Dollar caIMCNo;
	private Dollar caRegRefAmt;
	private Dollar caSalesTaxPdAmt;
	private Dollar caTaxPdOthrState;
	private Dollar caTotalRebateAmt;
	private Dollar caVehSalesPrice;
	private Dollar caVehTon;
	private Dollar caVehTradeInAllownce;
	private Dollar caVehValue;
	
	// Object
	private OwnerData caOwnerData;
	private AddressData caVehLocAddrData;
	private AddressData caRenewalAddrData;

	// Hashtable 
	private Hashtable chtLienholderData;
	
	// int 
	private int ciAddlLienRecrdIndi;
	private int ciAddlTradeInIndi;
	private int ciAgncyLoandIndi;
	private int ciCacheTransAMDate;
	private int ciCacheTransTime;
	private int ciCCOIssueDate;
	private int ciChildSupportIndi;
	private int ciClaimComptCntyNo;
	private int ciComptCntyNo;
	private int ciCustSeqNo;
	private int ciDieselIndi;
	private int ciDissociateCd;
	private int ciDocTypeCd;
	private int ciDOTStndrdsIndi;
	private int ciDPSSaftySuspnIndi;
	private int ciDPSStlnIndi;
	private int ciETtlCd;
	// defect 10841 
	private int ciETtlPrntDate;
	// end defect 10841
	private int ciExmptIndi;
	private int ciFileTierCd;
	private int ciFloodDmgeIndi;
	private int ciFxdWtIndi;
	private int ciGovtOwndIndi;
	private int ciHvyVehUseTaxIndi;
	private int ciInspectnWaivedIndi;
	private int ciJnkCd;
	private int ciJnkDate;
	private int ciLemonLawIndi;
	private int ciLien2NotRlsedIndi;
	private int ciLien3NotRlsedIndi;
	private int ciLienNotRlsedIndi;
	private int ciLienRlseDate1;
	private int ciLienRlseDate2;
	private int ciLienRlseDate3;
	private int ciMfDwnCd;
	private int ciOfcIssuanceCd;
	private int ciOfcIssuanceNo;
	private int ciOwnrshpEvidCd;
	private int ciOwnrSuppliedExpYr;
	// defect 10246
	//private int ciPrivacyOptCd;
	// end defect 10246 
	private int ciPltBirthDate;
	private int ciPltsSeizdIndi;
	private int ciPriorCCOIssueIndi;
	private int ciPrmtReqrdIndi;
	private int ciRecondCd;
	private int ciRecontIndi;
	private int ciRegClassCd;
	private int ciRegEffDate;
	private int ciRegExpMo;
	private int ciRegExpYr;
	private int ciRegHotCkIndi;
	private int ciRegInvldIndi;
	private int ciRegPltAge;
	private int ciRenwlMailRtrnIndi;
	private int ciRenwlYrMsmtchIndi;
	private int ciReplicaVehModlYr;
	private int ciResComptCntyNo;
	private int ciSalesTaxDate;
	private int ciSalesTaxExmptCd;
	private int ciSpclPltProgIndi;
	private int ciStkrSeizdIndi;
	private int ciSubconId;
	private int ciSubconIssueDate;
	private int ciSubstaId;
	private int ciSurrTtlDate;
	private int ciSurvshpRightsIndi;
	private int ciTradeInVehYr;
	private int ciTransAMDate;
	private int ciTransTime;
	private int ciTransWsId;
	private int ciTtlApplDate;
	// defect 10013 
	private int ciTtlExmnIndi;
	// end defect 10013 
	private int ciTtlHotCkIndi;
	private int ciTtlIssueDate;
	private int ciTtlRejctnDate;
	private int ciTtlRejctnOfc;
	private int ciTtlRevkdIndi;
	private int ciTtlSignDate;
	private int ciUTVMislblIndi;
	private int ciV21PltId;
	private int ciV21VTNId;
	private int ciVehCaryngCap;
	private int ciVehEmptyWt;
	private int ciVehGrossWt;
	private int ciVehLngth;
	private int ciVehModlYr;
	private int ciVehSoldDate;
	private int ciVINErrIndi;
	private int ciVoidedTransIndi;
	
	// defect 11231 
	private String csSurvShpRightsName1;
	private String csSurvShpRightsName2;
	private int ciAddlSurvivorIndi;
	private int ciExportIndi;
	private int ciSalvIndi;  
	// end defect 11231
	
	// String 
	private String csAuditTrailTransId;
	private String csAuthCd;
	private String csBatchNo;
	private String csBndedTtlCd;
	private String csDlrGDN;
	private String csDocNo;
	private String csEmissionSourceCd;
	private String csHoopsRegPltNo;
	private String csLegalRestrntNo;
	private String csNotfyngCity;
	private String csOldDocNo;
	private String csOthrGovtTtlNo;
	private String csOthrStateCntry;
	// These are never used by POS
	// Requirement enforced on MF  
	private String csOwnrFstName;
	private String csOwnrLstName;
	private String csOwnrMI;
	// end never used 
	private String csOwnrSuppliedPltNo;
	private String csOwnrSuppliedStkrNo;
	private String csPermLienHldrId1;
	private String csPermLienHldrId2;
	private String csPermLienHldrId3;
	private String csPrevOwnrCity;
	private String csPrevOwnrName;
	private String csPrevOwnrState;
	private String csPrismLvlCd;
	private String csRecpntFstName;
	private String csRecpntLstName;
	private String csRecpntMI;
	private String csRecpntName;
	private String csRegPltCd;
	private String csRegPltNo;
	private String csRegPltOwnrName;
	private String csRegStkrCd;
	private String csReplicaVehMk;
	private String csSalesTaxCat;
	private String csSalvStateCntry;
	private String csSalvYardNo;
	private String csTireTypeCd;
	private String csTradeInVehMk;
	private String csTradeInVehVin;
	private String csTransCd;
	private String csTransEmpId;
	private String csTrlrType;
	private String csTtlNoMf;
	private String csTtlProcsCd;
	private String csTtlTrnsfrEntCd;
	private String csTtlTrnsfrPnltyExmptCd;
	private String csVehBdyType;
	private String csVehBdyVin;
	private String csVehClassCd;
	private String csVehMk;
	private String csVehModl;
	private String csVehOdmtrBrnd;
	private String csVehOdmtrReadng;
	private String csVehUnitNo;
	private String csVIN;
	private String csVTNSource;
	private String csVTRRegEmrgCd1;

	private String csVTRRegEmrgCd2;

	//	defect 10505 
	protected String csTransId;
	// end defect 10505
	
	// defect 10336 
	private String csPvtLawEnfVehCd;
	private String csNonTtlGolfCartCd;
	private String csVTRTtlEmrgCd3;
	private String csVTRTtlEmrgCd4;
	private String csRecpntEMail;
	// end defect 10336
	
	// defect 10508 
	//private int ciSOReelectionIndi;
	private int ciEMailRenwlReqCd;
	// end defect 10508
	
	// defect 10712 
	private String csVehMjrColorCd;
	private String csVehMnrColorCd;
	// defect 10712
	
	private final static long serialVersionUID = -4691974447700451485L;

	/**
	 * Constructor
	 */
	public MotorVehicleFunctionTransactionData()
	{
		super();
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
		caOwnerData = new OwnerData();
		caVehLocAddrData = new AddressData();
		caRenewalAddrData = new AddressData();
	}

	/**
	 * Returns the value of AddlLienRecrdIndi
	 * @return  int 
	 */
	public final int getAddlLienRecrdIndi()
	{
		return ciAddlLienRecrdIndi;
	}

	/**
	 * Returns the value of AddlSurvivorIndi
	 * 
	 * @return int
	 */
	public int getAddlSurvivorIndi()
	{
		return ciAddlSurvivorIndi;
	}

	/**
	 * Returns the value of AddlTradeInIndi
	 * @return  int 
	 */
	public final int getAddlTradeInIndi()
	{
		return ciAddlTradeInIndi;
	}

	/**
	 * Returns the value of AgncyLoandIndi
	 * @return  int 
	 */
	public final int getAgncyLoandIndi()
	{
		return ciAgncyLoandIndi;
	}

	/**
	 * Get attributes for Object fields
	 * 
	 * @return java.util.Map
	 */
	public java.util.Map getAttributes()
	{
		java.util.HashMap lhmHash = new java.util.HashMap();
		Field[] larrFields = this.getClass().getDeclaredFields();

		for (int i = 0; i < larrFields.length; i++)
		{
			try
			{
				// defect 10191
				String lsFieldName = larrFields[i].getName();

				if (lsFieldName.equals("caOwnerData"))
				{
					UtilityMethods.addAttributesForObject(
						lsFieldName,
						getOwnerData().getAttributes(),
						lhmHash);
				}
				else if (lsFieldName.equals("caVehLocAddrData"))
				{
					UtilityMethods.addAttributesForObject(
						lsFieldName,
						getVehLocAddrData().getAttributes(),
						lhmHash);
				}
				else if (lsFieldName.equals("caRenewalAddrData"))
				{
					UtilityMethods.addAttributesForObject(
						lsFieldName,
						getRenewalAddrData().getAttributes(),
						lhmHash);
				}
				else if (
					lsFieldName.equals("chtLienholderData")
						&& chtLienholderData != null)
				{
					for (int l = TitleConstant.LIENHLDR1.intValue();
						l <= TitleConstant.LIENHLDR3.intValue();
						l++)
					{
						UtilityMethods.addAttributesForObject(
							"xxLienHolderData" + l,
							(getLienholderData(new Integer(l)))
								.getAttributes(),
							lhmHash);
					}
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
	 * Returns the value of AuditTrailTransId
	 * @return  String 
	 */
	public final String getAuditTrailTransId()
	{
		return csAuditTrailTransId;
	}

	/**
	 * Returns the value of AuthCd
	 * @return  String 
	 */
	public final String getAuthCd()
	{
		return csAuthCd;
	}

	/**
	 * Returns the value of BatchNo
	 * @return  String 
	 */
	public final String getBatchNo()
	{
		return csBatchNo;
	}

	/**
	 * Returns the value of BndedTtlCd
	 * @return  String 
	 */
	public final String getBndedTtlCd()
	{
		return csBndedTtlCd;
	}

	/**
	 * Returns the value of CacheTransAMDate
	 * 
	 * @return int
	 */
	public int getCacheTransAMDate()
	{
		return ciCacheTransAMDate;
	}

	/**
	 * Returns the value of CacheTransTime
	 * 
	 * @return int
	 */
	public int getCacheTransTime()
	{
		return ciCacheTransTime;
	}
	/**
	 * Returns the value of CCOIssueDate
	 * @return  int 
	 */
	public final int getCCOIssueDate()
	{
		return ciCCOIssueDate;
	}

	/**
	 * get ChildSupportIndi
	 * 
	 * @return
	 */
	public int getChildSupportIndi()
	{
		return ciChildSupportIndi;
	}

	/**
	 * Returns the value of ClaimComptCntyNo
	 * @return  int 
	 */
	public final int getClaimComptCntyNo()
	{
		return ciClaimComptCntyNo;
	}

	/**
	 * Returns the value of ComptCntyNo
	 * @return  int 
	 */
	public final int getComptCntyNo()
	{
		return ciComptCntyNo;
	}

	// defect 9631
	//	/**
	//	 * Returns the value of CustBaseRegFee
	//	 * @deprecated
	//	 * @return  Dollar 
	//	 */
	//	public final Dollar getCustBaseRegFee()
	//	{
	//		return caCustBaseRegFee;
	//	}

	//	/**
	//	 * Returns the value of CustDieselFee
	//	 * @deprecated
	//	 * @return  Dollar 
	//	 */
	//	public final Dollar getCustDieselFee()
	//	{
	//		return caCustDieselFee;
	//	}
	// end defect 9631

	/**
	 * Returns the value of CustActulRegFee
	 * @return  Dollar 
	 */
	public final Dollar getCustActulRegFee()
	{
		return caCustActulRegFee;
	}

	/**
	 * Returns the value of CustSeqNo
	 * @return  int 
	 */
	public final int getCustSeqNo()
	{
		return ciCustSeqNo;
	}

	/**
	 * Returns the value of DieselIndi
	 * @return  int 
	 */
	public final int getDieselIndi()
	{
		return ciDieselIndi;
	}

	/**
	 * get DissociatedCd
	 * 
	 * @return int
	 */
	public int getDissociateCd()
	{
		return ciDissociateCd;
	}

	/**
	 * Returns the value of DlrGDN
	 * @return  String 
	 */
	public final String getDlrGDN()
	{
		return csDlrGDN;
	}

	/**
	 * Returns the value of DocNo
	 * @return  String 
	 */
	public final String getDocNo()
	{
		return csDocNo;
	}

	/**
	 * Returns the value of DocTypeCd
	 * @return  int 
	 */
	public final int getDocTypeCd()
	{
		return ciDocTypeCd;
	}

	/**
	 * Returns the value of DOTStndrdsIndi
	 * @return  int 
	 */
	public final int getDOTStndrdsIndi()
	{
		return ciDOTStndrdsIndi;
	}

	/**
	 * Returns the value of DPSSaftySuspnIndi
	 * @return  int 
	 */
	public final int getDPSSaftySuspnIndi()
	{
		return ciDPSSaftySuspnIndi;
	}

	/**
	 * Returns the value of DPSStlnIndi
	 * @return  int 
	 */
	public final int getDPSStlnIndi()
	{
		return ciDPSStlnIndi;
	}

	/**
	 * Gets value of EMailRenwlReqCd
	 * 
	 * @return int
	 */
	public int getEMailRenwlReqCd()
	{
		return ciEMailRenwlReqCd;
	}

	/**
	 * Returns the value of EmissionSalesTax
	 * @return  Dollar 
	 */
	public final Dollar getEmissionSalesTax()
	{
		return caEmissionSalesTax;
	}

	/**
	 * Returns the value of EmissionSourceCd
	 * @return  String 
	 */
	public final String getEmissionSourceCd()
	{
		return csEmissionSourceCd;
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
	 * Returns the value of ExmptIndi
	 * @return  int 
	 */
	public final int getExmptIndi()
	{
		return ciExmptIndi;
	}

	/**
	 * Returns the value of ExportIndi
	 * 
	 * @return int
	 */
	public int getExportIndi()
	{
		return ciExportIndi;
	}

	/**
	 * Returns the value of FileTierCd
	 * @return  int 
	 */
	public final int getFileTierCd()
	{
		return ciFileTierCd;
	}

	/**
	 * Returns the value of FloodDmgeIndi
	 * @return  int 
	 */
	public final int getFloodDmgeIndi()
	{
		return ciFloodDmgeIndi;
	}

	/**
	 * Returns the value of FxdWtIndi
	 * @return  int 
	 */
	public final int getFxdWtIndi()
	{
		return ciFxdWtIndi;
	}

	/**
	 * Returns the value of GovtOwndIndi
	 * @return  int 
	 */
	public final int getGovtOwndIndi()
	{
		return ciGovtOwndIndi;
	}

	/**
	 * Returns the value of HoopsRegPltNo
	 * @return  String 
	 */
	public final String getHoopsRegPltNo()
	{
		return csHoopsRegPltNo;
	}

	/**
	 * Returns the value of HvyVehUseTaxIndi
	 * @return  int 
	 */
	public final int getHvyVehUseTaxIndi()
	{
		return ciHvyVehUseTaxIndi;
	}

	/**
	 * Returns the value of IMCNo
	 * @return  Dollar 
	 */
	public final Dollar getIMCNo()
	{
		return caIMCNo;
	}

	/**
	 * Returns the value of InspectnWaivedIndi
	 * @return  int 
	 */
	public final int getInspectnWaivedIndi()
	{
		return ciInspectnWaivedIndi;
	}

	/**
	 * Returns the value of JnkCd
	 * @return  int 
	 */
	public final int getJnkCd()
	{
		return ciJnkCd;
	}
	/**
	 * Returns the value of JnkDate
	 * @return  int 
	 */
	public final int getJnkDate()
	{
		return ciJnkDate;
	}

	//	/**
	//	 * Returns the value of Lien1Date
	//	 * @return  int 
	//	 */
	//	public final int getLien1Date()
	//	{
	//		return ciLien1Date;
	//	}
	//
	//	/**
	//	 * Returns the value of Lien2Date
	//	 * @return  int 
	//	 */
	//	public final int getLien2Date()
	//	{
	//		return ciLien2Date;
	//	}

	/**
	 * Returns the value of LegalRestrntNo
	 * @return  String 
	 */
	public final String getLegalRestrntNo()
	{
		return csLegalRestrntNo;
	}

	//	/**
	//	 * Returns the value of Lien3Date
	//	 * @return  int 
	//	 */
	//	public final int getLien3Date()
	//	{
	//		return ciLien3Date;
	//	}

	/**
	 * This method gets the value of LemonLawIndi
	 * 
	 * @return int 
	 */
	public int getLemonLawIndi()
	{
		return ciLemonLawIndi;
	}

	/**
	 * Returns the value of Lien2NotRlsedIndi
	 * @return  int 
	 */
	public final int getLien2NotRlsedIndi()
	{
		return ciLien2NotRlsedIndi;
	}

	//	/**
	//	 * Returns the value of LienHldr1City
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr1City()
	//	{
	//		return csLienHldr1City;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr1Cntry
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr1Cntry()
	//	{
	//		return csLienHldr1Cntry;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr1Name1
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr1Name1()
	//	{
	//		return csLienHldr1Name1;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr1Name2
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr1Name2()
	//	{
	//		return csLienHldr1Name2;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr1St1
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr1St1()
	//	{
	//		return csLienHldr1St1;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr1St2
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr1St2()
	//	{
	//		return csLienHldr1St2;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr1State
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr1State()
	//	{
	//		return csLienHldr1State;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr1ZpCd
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr1ZpCd()
	//	{
	//		return csLienHldr1ZpCd;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr1ZpCdP4
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr1ZpCdP4()
	//	{
	//		return csLienHldr1ZpCdP4;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr2City
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr2City()
	//	{
	//		return csLienHldr2City;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr2Cntry
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr2Cntry()
	//	{
	//		return csLienHldr2Cntry;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr2Name1
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr2Name1()
	//	{
	//		return csLienHldr2Name1;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr2Name2
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr2Name2()
	//	{
	//		return csLienHldr2Name2;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr2St1
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr2St1()
	//	{
	//		return csLienHldr2St1;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr2St2
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr2St2()
	//	{
	//		return csLienHldr2St2;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr2State
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr2State()
	//	{
	//		return csLienHldr2State;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr2ZpCd
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr2ZpCd()
	//	{
	//		return csLienHldr2ZpCd;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr2ZpCdP4
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr2ZpCdP4()
	//	{
	//		return csLienHldr2ZpCdP4;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr3City
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr3City()
	//	{
	//		return csLienHldr3City;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr3Cntry
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr3Cntry()
	//	{
	//		return csLienHldr3Cntry;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr3Name1
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr3Name1()
	//	{
	//		return csLienHldr3Name1;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr3Name2
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr3Name2()
	//	{
	//		return csLienHldr3Name2;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr3St1
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr3St1()
	//	{
	//		return csLienHldr3St1;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr3St2
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr3St2()
	//	{
	//		return csLienHldr3St2;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr3State
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr3State()
	//	{
	//		return csLienHldr3State;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr3ZpCd
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr3ZpCd()
	//	{
	//		return csLienHldr3ZpCd;
	//	}
	//
	//	/**
	//	 * Returns the value of LienHldr3ZpCdP4
	//	 * @return  String 
	//	 */
	//	public final String getLienHldr3ZpCdP4()
	//	{
	//		return csLienHldr3ZpCdP4;
	//	}

	/**
	 * Returns the value of Lien3NotRlsedIndi
	 * @return  int 
	 */
	public final int getLien3NotRlsedIndi()
	{
		return ciLien3NotRlsedIndi;
	}
	// end defect 9961

	/**
	 * Return LienholderData for provided Integer
	 * 
	 * @return LienholderData
	 */
	public LienholderData getLienholderData(Integer aiId)
	{
		LienholderData laLienhldrData =
			(LienholderData) chtLienholderData.get(aiId);

		if (laLienhldrData == null)
		{
			laLienhldrData = new LienholderData();
			chtLienholderData.put(aiId, laLienhldrData);
		}
		return laLienhldrData;
	}

	/**
	 * Returns the value of LienNotRlsedIndi
	 * @return  int 
	 */
	public final int getLienNotRlsedIndi()
	{
		return ciLienNotRlsedIndi;
	}

	//	New getters/setters for LienRlseDate 
	/**
	 * Get LIENRLSEDATE for Lien 1
	 * 
	 * @return int
	 */
	public int getLienRlseDate1()
	{
		return ciLienRlseDate1;
	}

	/**
	 * Get LIENRLSEDATE for Lien 2
	 * 
	 * @return int
	 */
	public int getLienRlseDate2()
	{
		return ciLienRlseDate2;
	}

	/**
	 * Get LIENRLSEDATE for Lien 3
	 * 
	 * @return int
	 */
	public int getLienRlseDate3()
	{
		return ciLienRlseDate3;
	}

	/**
	 * Returns the value of MfDwnCd
	 * @return  int 
	 */
	public final int getMfDwnCd()
	{
		return ciMfDwnCd;
	}

	/** 
	 * 
	 * Method description
	 * 
	 * @return
	 */
	public final MFVehicleData getMFVehicleData()
	{
		MFVehicleData laMFVehData = new MFVehicleData();
		laMFVehData.setTitleData(getTitleData());
		laMFVehData.setRegData(getRegistrationData());
		laMFVehData.setOwnerData(
			(OwnerData) UtilityMethods.copy(caOwnerData));
		laMFVehData.setVehicleData(getVehicleData());
		Vector lvSalvage = new Vector();
		lvSalvage.add(getSalvageData());
		laMFVehData.setVctSalvage(lvSalvage);

		return laMFVehData;
	}

	/**
	 * Get NonTtlGolfCartCd
	 * 
	 * @return String
	 */
	public String getNonTtlGolfCartCd()
	{
		return csNonTtlGolfCartCd;
	}

	/**
	 * Returns the value of NotfyngCity
	 * @return  String 
	 */
	public final String getNotfyngCity()
	{
		return csNotfyngCity;
	}

	/**
	 * Returns the value of OfcIssuanceCd
	 * @return  int 
	 */
	public final int getOfcIssuanceCd()
	{
		return ciOfcIssuanceCd;
	}

	/**
	 * Returns the value of OfcIssuanceNo
	 * @return  int 
	 */
	public final int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Returns the value of OldDocNo
	 * @return  String 
	 */
	public final String getOldDocNo()
	{
		return csOldDocNo;
	}

	//	/**
	//	 * Returns the value of OwnrCity
	//	 * @return  String 
	//	 */
	//	public final String getOwnrCity()
	//	{
	//		return csOwnrCity;
	//	}
	//
	//	/**
	//	 * Returns the value of OwnrCntry
	//	 * @return  String 
	//	 */
	//	public final String getOwnrCntry()
	//	{
	//		return csOwnrCntry;
	//	}

	/**
	 * Returns the value of OthrGovtTtlNo
	 * @return  String 
	 */
	public final String getOthrGovtTtlNo()
	{
		return csOthrGovtTtlNo;
	}

	//	/**
	//	 * Returns the value of OwnrId
	//	 * @return  String 
	//	 */
	//	public final String getOwnrId()
	//	{
	//		return csOwnrId;
	//	}

	/**
	 * Returns the value of OthrStateCntry
	 * @return  String 
	 */
	public final String getOthrStateCntry()
	{
		return csOthrStateCntry;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public OwnerData getOwnerData()
	{
		if (caOwnerData == null)
		{
			caOwnerData = new OwnerData();
		}
		return caOwnerData;
	}

	/**
	 * Returns the value of OwnrFstName
	 * @return  String 
	 */
	public final String getOwnrFstName()
	{
		return csOwnrFstName;
	}

	//	/**
	//	 * Returns the value of OwnrSt1
	//	 * @return  String 
	//	 */
	//	public final String getOwnrSt1()
	//	{
	//		return csOwnrSt1;
	//	}
	//
	//	/**
	//	 * Returns the value of OwnrSt2
	//	 * @return  String 
	//	 */
	//	public final String getOwnrSt2()
	//	{
	//		return csOwnrSt2;
	//	}
	//
	//	/**
	//	 * Returns the value of OwnrState
	//	 * @return  String 
	//	 */
	//	public final String getOwnrState()
	//	{
	//		return csOwnrState;
	//	}

	/**
	 * Returns the value of OwnrLstName
	 * @return  String 
	 */
	public final String getOwnrLstName()
	{
		return csOwnrLstName;
	}

	/**
	 * Returns the value of OwnrMI
	 * @return  String 
	 */
	public final String getOwnrMI()
	{
		return csOwnrMI;
	}

	/**
	 * Returns the value of OwnrshpEvidCd
	 * @return  int 
	 */
	public final int getOwnrshpEvidCd()
	{
		return ciOwnrshpEvidCd;
	}

	//	/**
	//	 * Returns the value of OwnrTtlName1
	//	 * @return  String 
	//	 */
	//	public final String getOwnrTtlName1()
	//	{
	//		return csOwnrTtlName1;
	//	}
	//
	//	/**
	//	 * Returns the value of OwnrTtlName2
	//	 * @return  String 
	//	 */
	//	public final String getOwnrTtlName2()
	//	{
	//		return csOwnrTtlName2;
	//	}
	//
	//	/**
	//	 * Returns the value of OwnrZpCd
	//	 * @return  String 
	//	 */
	//	public final String getOwnrZpCd()
	//	{
	//		return csOwnrZpCd;
	//	}
	//
	//	/**
	//	 * Returns the value of OwnrZpCdP4
	//	 * @return  String 
	//	 */
	//	public final String getOwnrZpCdP4()
	//	{
	//		return csOwnrZpCdP4;
	//	}

	/**
	 * Returns the value of OwnrSuppliedExpYr
	 * @return  int 
	 */
	public final int getOwnrSuppliedExpYr()
	{
		return ciOwnrSuppliedExpYr;
	}
	/**
	 * Returns the value of OwnrSuppliedPltNo
	 * @return  String 
	 */
	public final String getOwnrSuppliedPltNo()
	{
		return csOwnrSuppliedPltNo;
	}
	/**
	 * Returns the value of OwnrSuppliedStkrNo
	 * @return  String 
	 */
	public final String getOwnrSuppliedStkrNo()
	{
		return csOwnrSuppliedStkrNo;
	}

	/**
	 * Get PERMLIENHLDRID for Lien 1
	 * 
	 * @return String
	 */
	public String getPermLienHldrId1()
	{
		return csPermLienHldrId1;
	}

	/**
	 * Get PERMLIENHLDRID for Lien 2
	 * 
	 * @return String
	 */
	public String getPermLienHldrId2()
	{
		return csPermLienHldrId2;
	}

	/**
	 * Get PERMLIENHLDRID for Lien 3
	 * 
	 * @return String
	 */
	public String getPermLienHldrId3()
	{
		return csPermLienHldrId3;
	}

	/**
	 * Returns the value of PltBirthDate
	 * @return  int 
	 */
	public final int getPltBirthDate()
	{
		return ciPltBirthDate;
	}

	/**
	 * Returns the value of PltsSeizdIndi
	 * @return  int 
	 */
	public final int getPltsSeizdIndi()
	{
		return ciPltsSeizdIndi;
	}

	/**
	 * Returns the value of PrevOwnrCity
	 * @return  String 
	 */
	public final String getPrevOwnrCity()
	{
		return csPrevOwnrCity;
	}

	/**
	 * Returns the value of PrevOwnrName
	 * @return  String 
	 */
	public final String getPrevOwnrName()
	{
		return csPrevOwnrName;
	}

	//	/**
	//	 * Returns the value of PrivacyOptCd
	//	 * @return  int 
	//	 */
	//	public final int getPrivacyOptCd()
	//	{
	//		return ciPrivacyOptCd;
	//	}

	/**
	 * Returns the value of PrevOwnrState
	 * @return  String 
	 */
	public final String getPrevOwnrState()
	{
		return csPrevOwnrState;
	}

	/**
	 * Returns the value of PriorCCOIssueIndi
	 * @return  int 
	 */
	public final int getPriorCCOIssueIndi()
	{
		return ciPriorCCOIssueIndi;
	}

	/**
	 * get PrsmLvlCd
	 * 
	 * @return String
	 */
	public String getPrismLvlCd()
	{
		return csPrismLvlCd;
	}

	/**
	 * Returns the value of PrmtReqrdIndi
	 * @return  int 
	 */
	public final int getPrmtReqrdIndi()
	{
		return ciPrmtReqrdIndi;
	}

	/**
	 * Get PvtLawEnfVehCd
	 * 
	 * @return String
	 */
	public String getPvtLawEnfVehCd()
	{
		return csPvtLawEnfVehCd;
	}

	/**
	 * Returns the value of RecondCd
	 * @return  int 
	 */
	public final int getRecondCd()
	{
		return ciRecondCd;
	}

	/**
	 * Returns the value of RecontIndi
	 * @return  int 
	 */
	public final int getRecontIndi()
	{
		return ciRecontIndi;
	}

	/**
	 * Gets value of csRecpntEMail
	 * 
	 * @return String 
	 */
	public String getRecpntEMail()
	{
		return csRecpntEMail;
	}

	/**
	 * Returns the value of RecpntFstName
	 * @return  String 
	 */
	public final String getRecpntFstName()
	{
		return csRecpntFstName;
	}

	/**
	 * Returns the value of RecpntLstName
	 * @return  String 
	 */
	public final String getRecpntLstName()
	{
		return csRecpntLstName;
	}

	/**
	 * Returns the value of RecpntMI
	 * @return  String 
	 */
	public final String getRecpntMI()
	{
		return csRecpntMI;
	}

	/**
	 * Returns the value of RecpntName
	 * @return  String 
	 */
	public final String getRecpntName()
	{
		return csRecpntName;
	}

	/**
	 * Returns the value of RegClassCd
	 * @return  int 
	 */
	public final int getRegClassCd()
	{
		return ciRegClassCd;
	}

	/**
	 * Returns the value of RegEffDate
	 * @return  int 
	 */
	public final int getRegEffDate()
	{
		return ciRegEffDate;
	}

	/**
	 * Returns the value of RegExpMo
	 * @return  int 
	 */
	public final int getRegExpMo()
	{
		return ciRegExpMo;
	}

	/**
	 * Returns the value of RegExpYr
	 * @return  int 
	 */
	public final int getRegExpYr()
	{
		return ciRegExpYr;
	}

	/**
	 * Returns the value of RegHotCkIndi
	 * @return  int 
	 */
	public final int getRegHotCkIndi()
	{
		return ciRegHotCkIndi;
	}

	/**
	 * Returns the value of RegInvldIndi
	 * @return  int 
	 */
	public final int getRegInvldIndi()
	{
		return ciRegInvldIndi;
	}

	/**
	 * 
	 * Return RegistrationData 
	 * 
	 * @return RegistrationData
	 */
	public final RegistrationData getRegistrationData()
	{
		RegistrationData laRegistrationData = new RegistrationData();
		laRegistrationData.setClaimComptCntyNo(getClaimComptCntyNo());
		laRegistrationData.setCustActlRegFee(getCustActulRegFee());
		laRegistrationData.setDpsSaftySuspnIndi(getDPSSaftySuspnIndi());
		laRegistrationData.setEmissionSourceCd(getEmissionSourceCd());
		laRegistrationData.setExmptIndi(getExmptIndi());
		laRegistrationData.setFileTierCd(getFileTierCd());
		laRegistrationData.setHvyVehUseTaxIndi(getHvyVehUseTaxIndi());
		laRegistrationData.setNotfyngCity(getNotfyngCity());
		laRegistrationData.setOwnrSuppliedPltNo(getOwnrSuppliedPltNo());
		laRegistrationData.setOwnrSuppliedExpYr(getOwnrSuppliedExpYr());
		laRegistrationData.setPltSeizedIndi(getPltsSeizdIndi());
		laRegistrationData.setRecpntName(getRecpntName());
		laRegistrationData.setRegClassCd(getRegClassCd());
		laRegistrationData.setRegEffDt(getRegEffDate());
		laRegistrationData.setRegExpMo(getRegExpMo());
		laRegistrationData.setRegExpYr(getRegExpYr());
		laRegistrationData.setRegHotCkIndi(getRegHotCkIndi());
		laRegistrationData.setRegInvldIndi(getRegInvldIndi());
		laRegistrationData.setRegPltAge(getRegPltAge());
		laRegistrationData.setRegPltCd(getRegPltCd());
		laRegistrationData.setRegPltNo(getRegPltNo());
		laRegistrationData.setRegPltOwnrName(getRegPltOwnrName());
		laRegistrationData.setRegRefAmt(getRegRefAmt());
		laRegistrationData.setRegStkrCd(getRegStkrCd());
		laRegistrationData.setRenwlMailAddr(
			(AddressData) UtilityMethods.copy(getRenewalAddrData()));
		laRegistrationData.setRenwlMailRtrnIndi(getRenwlMailRtrnIndi());
		laRegistrationData.setRenwlYrMismatchIndi(
			getRenwlYrMsmtchIndi());
		laRegistrationData.setResComptCntyNo(getResComptCntyNo());
		laRegistrationData.setStkrSeizdIndi(getStkrSeizdIndi());
		laRegistrationData.setSubconId(getSubconId());
		laRegistrationData.setSubconIssueDt(getSubconIssueDate());
		laRegistrationData.setTireTypeCd(getTireTypeCd());
		laRegistrationData.setVehCaryngCap(getVehCaryngCap());
		laRegistrationData.setVehGrossWt(getVehGrossWt());
		return laRegistrationData;

	}

	/**
	 * Returns the value of RegPltAge
	 * @return  int 
	 */
	public final int getRegPltAge()
	{
		return ciRegPltAge;
	}

	/**
	 * Returns the value of RegPltCd
	 * @return  String 
	 */
	public final String getRegPltCd()
	{
		return csRegPltCd;
	}

	/**
	 * Returns the value of RegPltNo
	 * @return  String 
	 */
	public final String getRegPltNo()
	{
		return csRegPltNo;
	}

	//	/**
	//	 * Returns the value of RenwlMailngCity
	//	 * @return  String 
	//	 */
	//	public final String getRenwlMailngCity()
	//	{
	//		return csRenwlMailngCity;
	//	}
	//
	//	/**
	//	 * Returns the value of RenwlMailngSt1
	//	 * @return  String 
	//	 */
	//	public final String getRenwlMailngSt1()
	//	{
	//		return csRenwlMailngSt1;
	//	}
	//
	//	/**
	//	 * Returns the value of RenwlMailngSt2
	//	 * @return  String 
	//	 */
	//	public final String getRenwlMailngSt2()
	//	{
	//		return csRenwlMailngSt2;
	//	}
	//
	//	/**
	//	 * Returns the value of RenwlMailngState
	//	 * @return  String 
	//	 */
	//	public final String getRenwlMailngState()
	//	{
	//		return csRenwlMailngState;
	//	}
	//
	//	/**
	//	 * Returns the value of RenwlMailngZPCd
	//	 * @return  String 
	//	 */
	//	public final String getRenwlMailngZPCd()
	//	{
	//		return csRenwlMailngZPCd;
	//	}
	//
	//	/**
	//	 * Returns the value of RenwlMailngZPCdP4
	//	 * @return  String 
	//	 */
	//	public final String getRenwlMailngZPCdP4()
	//	{
	//		return csRenwlMailngZPCdP4;
	//	}

	/**
	 * Returns the value of RegPltOwnrName
	 * @return  String 
	 */
	public final String getRegPltOwnrName()
	{
		return csRegPltOwnrName;
	}

	/**
	 * Returns the value of RegRefAmt
	 * @return  Dollar 
	 */
	public final Dollar getRegRefAmt()
	{
		return caRegRefAmt;
	}

	/**
	 * Returns the value of RegStkrCd
	 * @return  String 
	 */
	public final String getRegStkrCd()
	{
		return csRegStkrCd;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public AddressData getRenewalAddrData()
	{
		if (caRenewalAddrData == null)
		{
			caRenewalAddrData = new AddressData();
		}
		return caRenewalAddrData;
	}

	/**
	 * Returns the value of RenwlMailRtrnIndi
	 * @return  int 
	 */
	public final int getRenwlMailRtrnIndi()
	{
		return ciRenwlMailRtrnIndi;
	}

	/**
	 * Returns the value of RenwlYrMsmtchIndi
	 * @return  int 
	 */
	public final int getRenwlYrMsmtchIndi()
	{
		return ciRenwlYrMsmtchIndi;
	}

	/**
	 * Returns the value of ReplicaVehMk
	 * @return  String 
	 */
	public final String getReplicaVehMk()
	{
		return csReplicaVehMk;
	}

	/**
	 * Returns the value of ReplicaVehModlYr
	 * @return  int 
	 */
	public final int getReplicaVehModlYr()
	{
		return ciReplicaVehModlYr;
	}

	/**
	 * Returns the value of ResComptCntyNo
	 * @return  int 
	 */
	public final int getResComptCntyNo()
	{
		return ciResComptCntyNo;
	}

	/**
	 * Returns the value of SalesTaxCat
	 * @return  String 
	 */
	public final String getSalesTaxCat()
	{
		return csSalesTaxCat;
	}

	/**
	 * Returns the value of SalesTaxDate
	 * @return  int 
	 */
	public final int getSalesTaxDate()
	{
		return ciSalesTaxDate;
	}

	/**
	 * Returns the value of SalesTaxExmptCd
	 * @return  int 
	 */
	public final int getSalesTaxExmptCd()
	{
		return ciSalesTaxExmptCd;
	}

	/**
	 * Returns the value of SalesTaxPdAmt
	 * @return  Dollar 
	 */
	public final Dollar getSalesTaxPdAmt()
	{
		return caSalesTaxPdAmt;
	}

	/**
	 * Returns Salvage Data to return to complete Transaction
	 * 
	 * @return SalvageData 
	 */
	private SalvageData getSalvageData()
	{
		SalvageData laSalvageData = new SalvageData();
		laSalvageData.setSlvgCd(getJnkCd());
		laSalvageData.setOthrGovtTtlNo(getOthrGovtTtlNo());
		laSalvageData.setOthrStateCntry(getOthrStateCntry());
		laSalvageData.setSalvYardNo(getSalvYardNo());
		laSalvageData.setLienNotRlsedIndi(getLienNotRlsedIndi());
		laSalvageData.setLienNotRlsedIndi2(getLien2NotRlsedIndi());
		laSalvageData.setLienNotRlsedIndi3(getLien3NotRlsedIndi());
		laSalvageData.setOwnrEvdncCd(getOwnrshpEvidCd());
		return laSalvageData;
	}

	/**
	 * Returns the value of SalvIndi
	 * 
	 * @return int
	 */
	public int getSalvIndi()
	{
		return ciSalvIndi;
	}
	/**
	 * Returns the value of SalvStateCntry
	 * @return  String 
	 */
	public final String getSalvStateCntry()
	{
		return csSalvStateCntry;
	}

	/**
	 * Returns the value of SalvYardNo
	 * @return  String 
	 */
	public final String getSalvYardNo()
	{
		return csSalvYardNo;
	}

	/**
	 * Returns the value of SpclPltProgIndi
	 * @return  int 
	 */
	public final int getSpclPltProgIndi()
	{
		return ciSpclPltProgIndi;
	}

	/**
	 * Returns the value of StkrSeizdIndi
	 * @return  int 
	 */
	public final int getStkrSeizdIndi()
	{
		return ciStkrSeizdIndi;
	}

	/**
	 * Returns the value of SubconId
	 * @return  int 
	 */
	public final int getSubconId()
	{
		return ciSubconId;
	}

	/**
	
	 * Returns the value of SubconIssueDate
	 * @return  int 
	 */
	public final int getSubconIssueDate()
	{
		return ciSubconIssueDate;
	}
	/**
	 * Returns the value of SubstaId
	 * @return  int 
	 */
	public final int getSubstaId()
	{
		return ciSubstaId;
	}

	/**
	 * Returns the value of SurrTtlDate
	 * @return  int 
	 */
	public final int getSurrTtlDate()
	{
		return ciSurrTtlDate;
	}

	/**
	 * Returns the value of SurvshpRightsIndi
	 * @return  int 
	 */
	public final int getSurvshpRightsIndi()
	{
		return ciSurvshpRightsIndi;
	}

	/**
	 * Returns the value of SurvShpRightsName1
	 * 
	 * @return String
	 */
	public String getSurvShpRightsName1()
	{
		return csSurvShpRightsName1;
	}

	/**
	 * Returns the value of SurvShpRightsName2
	 * 
	 * @return String
	 */
	public String getSurvShpRightsName2()
	{
		return csSurvShpRightsName2;
	}

	/**
	 * Returns the value of TaxPdOthrState
	 * @return  Dollar 
	 */
	public final Dollar getTaxPdOthrState()
	{
		return caTaxPdOthrState;
	}

	/**
	 * Returns the value of TireTypeCd
	 * @return  String 
	 */
	public final String getTireTypeCd()
	{
		return csTireTypeCd;
	}
	/**
	 * Return Title Data 
	 * 
	 * @return TitleData
	 */
	public final TitleData getTitleData()
	{
		TitleData laTitleData = new TitleData();
		AddressData laAddressData = new AddressData();

		laTitleData.setAddlLienRecrdIndi(getAddlLienRecrdIndi());
		laTitleData.setAgncyLoandIndi(getAgncyLoandIndi());
		laTitleData.setBatchNo(getBatchNo());
		laTitleData.setBndedTtlCd(getBndedTtlCd());
		laTitleData.setCcoIssueDate(getCCOIssueDate());
		laTitleData.setCompCntyNo(getComptCntyNo());
		laTitleData.setDlrGdn(getDlrGDN());
		laTitleData.setDocNo(getDocNo());
		laTitleData.setDocTypeCd(getDocTypeCd());
		laTitleData.setGovtOwndIndi(getGovtOwndIndi());
		laTitleData.setImcNo(getIMCNo());
		laTitleData.setInspectnWaivedIndi(getInspectnWaivedIndi());

		for (int i = 1; i <= 3; i++)
		{
			LienholderData laLienholderData =
				getLienholderData(new Integer(i));

			laLienholderData =
				laLienholderData == null
					? new LienholderData()
					: laLienholderData;

			laTitleData.setLienholderData(
				new Integer(i),
				(LienholderData) UtilityMethods.copy(laLienholderData));
		}

		laTitleData.setOldDocNo(getOldDocNo());
		laTitleData.setOwnrShpEvidCd(getOwnrshpEvidCd());
		laTitleData.setPrevOwnrName(getPrevOwnrName());
		laTitleData.setPrevOwnrCity(getPrevOwnrCity());
		laTitleData.setPrevOwnrState(getPrevOwnrState());
		laTitleData.setPriorCCOIssueIndi(getPriorCCOIssueIndi());
		laTitleData.setSalesTaxPdAmt(getSalesTaxPdAmt());
		laTitleData.setSalvStateCntry(getSalvStateCntry());
		laTitleData.setSurrTtlDate(getSurrTtlDate());
		laTitleData.setSurvshpRightsIndi(getSurvshpRightsIndi());
		laTitleData.setTtlApplDate(getTtlApplDate());
		laTitleData.setTtlHotCkIndi(getTtlHotCkIndi());
		laTitleData.setTtlIssueDate(getTtlIssueDate());
		laTitleData.setTtlProcsCd(getTtlProcsCd());
		laTitleData.setTtlRejctnDate(getTtlRejctnDate());
		laTitleData.setTtlRejctnOfc(getTtlRejctnOfc());
		laTitleData.setTtlRevkdIndi(getTtlRevkdIndi());
		laAddressData =
			(AddressData) UtilityMethods.copy(getVehLocAddrData());
		laTitleData.setTtlVehAddr(laAddressData);
		laTitleData.setVehSoldDate(getVehSoldDate());
		laTitleData.setVehUnitNo(getVehUnitNo());
		laTitleData.setVehSalesPrice(getVehSalesPrice());
		laTitleData.setVehTradeinAllownce(getVehTradeInAllownce());
		return laTitleData;

	}

	/**
	 * Returns the value of TotalRebateAmt
	 * @return  Dollar 
	 */
	public final Dollar getTotalRebateAmt()
	{
		return caTotalRebateAmt;
	}

	/**
	 * Returns the value of TradeInVehMk
	 * @return  String 
	 */
	public final String getTradeInVehMk()
	{
		return csTradeInVehMk;
	}
	/**
	 * Returns the value of TradeInVehVin
	 * @return  String 
	 */
	public final String getTradeInVehVin()
	{
		return csTradeInVehVin;
	}

	/**
	 * Returns the value of TradeInVehYr
	 * @return  int 
	 */
	public final int getTradeInVehYr()
	{
		return ciTradeInVehYr;
	}

	/**
	 * Returns the value of TransAMDate
	 * @return  int 
	 */
	public final int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Returns the value of TransCd
	 * @return  String 
	 */
	public final String getTransCd()
	{
		return csTransCd;
	}

	/**
	 * Returns the value of TransCd
	 * @return  String 
	 */
	public final String getTransEmpId()
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
	 * Returns the value of TransTime
	 * @return  int 
	 */
	public final int getTransTime()
	{

		return ciTransTime;
	}

	/**
	 * Returns the value of TransWsId
	 * @return  int 
	 */
	public final int getTransWsId()
	{
		return ciTransWsId;
	}

	/**
	 * Returns the value of TrlrType
	 * @return  String 
	 */
	public final String getTrlrType()
	{
		return csTrlrType;
	}

	/**
	 * Returns the value of TtlApplDate
	 * @return  int 
	 */
	public final int getTtlApplDate()
	{
		return ciTtlApplDate;
	}
	/**
	 * Return value of ciTtlExmnIndi
	 * 
	 * @return int
	 */
	public int getTtlExmnIndi()
	{
		return ciTtlExmnIndi;
	}

	/**
	 * Returns the value of TtlHotCkIndi
	 * @return  int 
	 */
	public final int getTtlHotCkIndi()
	{
		return ciTtlHotCkIndi;
	}

	/**
	 * Returns the value of TtlIssueDate
	 * @return  int 
	 */
	public final int getTtlIssueDate()
	{
		return ciTtlIssueDate;
	}

	/**
	 * Returns the value of TtlNoMf
	 * @return  String 
	 */
	public final String getTtlNoMf()
	{
		return csTtlNoMf;
	}

	// defect 9969 
	// defect 9961
	//	getter/setter for ELT
	//	/**
	//	 * Get AAMVAMsgId
	//	 * 
	//	 * @return String
	//	 */
	//	public String getAAMVAMsgId()
	//	{
	//		return csAAMVAMsgId;
	//	}
	// end defect 9969 

	/**
	 * Returns the value of TtlProcsCd
	 * @return  String 
	 */
	public final String getTtlProcsCd()
	{
		return csTtlProcsCd;
	}

	/**
	 * Returns the value of TtlRejctnDate
	 * @return  int 
	 */
	public final int getTtlRejctnDate()
	{
		return ciTtlRejctnDate;
	}

	/**
	 * Returns the value of TtlRejctnOfc
	 * @return  int 
	 */
	public final int getTtlRejctnOfc()
	{
		return ciTtlRejctnOfc;
	}

	/**
	 * Returns the value of TtlRevkdIndi
	 * @return  int 
	 */
	public final int getTtlRevkdIndi()
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
	 * get TtlTrnsfrEntCd
	 * 
	 * @return String
	 */
	public String getTtlTrnsfrEntCd()
	{
		return csTtlTrnsfrEntCd;
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
	//
	//	/**
	//	 * Returns the value of TtlVehLocCity
	//	 * @return  String 
	//	 */
	//	public final String getTtlVehLocCity()
	//	{
	//		return csTtlVehLocCity;
	//	}
	//
	//	/**
	//	 * Returns the value of TtlVehLocSt1
	//	 * @return  String 
	//	 */
	//	public final String getTtlVehLocSt1()
	//	{
	//		return csTtlVehLocSt1;
	//	}
	//
	//	/**
	//	 * Returns the value of TtlVehLocSt2
	//	 * @return  String 
	//	 */
	//	public final String getTtlVehLocSt2()
	//	{
	//		return csTtlVehLocSt2;
	//	}
	//
	//	/**
	//	 * Returns the value of TtlVehLocState
	//	 * @return  String 
	//	 */
	//	public final String getTtlVehLocState()
	//	{
	//		return csTtlVehLocState;
	//	}
	//
	//	/**
	//	 * Returns the value of TtlVehLocZpCd
	//	 * @return  String 
	//	 */
	//	public final String getTtlVehLocZpCd()
	//	{
	//		return csTtlVehLocZpCd;
	//	}
	//
	//	/**
	//	 * Returns the value of TtlVehLocZpCdP4
	//	 * @return  String 
	//	 */
	//	public final String getTtlVehLocZpCdP4()
	//	{
	//		return csTtlVehLocZpCdP4;
	//	}
	// end defect 9655

	/**
	 * Get UTVMislblIndi
	 * 
	 * @return int
	 */
	public int getUTVMislblIndi()
	{
		return ciUTVMislblIndi;
	}

	/**
	 * get V21PltId
	 * 
	 * @return int
	 */
	public int getV21PltId()
	{
		return ciV21PltId;
	}

	/**
	 * get V21VTNId
	 * 
	 * @return int
	 */
	public int getV21VTNId()
	{
		return ciV21VTNId;
	}

	/**
	 * Returns the value of VehBdyType
	 * @return  String 
	 */
	public final String getVehBdyType()
	{
		return csVehBdyType;
	}

	/**
	 * Returns the value of VehBdyVin
	 * @return  String 
	 */
	public final String getVehBdyVin()
	{
		return csVehBdyVin;
	}

	/**
	 * Returns the value of VehCaryngCap
	 * @return  int 
	 */
	public final int getVehCaryngCap()
	{
		return ciVehCaryngCap;
	}

	/**
	 * Returns the value of VehClassCd
	 * @return  String 
	 */
	public final String getVehClassCd()
	{
		return csVehClassCd;
	}

	/**
	 * Returns the value of VehEmptyWt
	 * @return  int 
	 */
	public final int getVehEmptyWt()
	{
		return ciVehEmptyWt;
	}

	/**
	 * Returns the value of VehGrossWt
	 * @return  int 
	 */
	public final int getVehGrossWt()
	{
		return ciVehGrossWt;
	}

	/**
	 * Returns Vehicle Data Object 
	 * 
	 * @return VehicleData 
	 */
	public final VehicleData getVehicleData()
	{
		VehicleData laVehicleData = new VehicleData();
		laVehicleData.setAuditTrailTransId(getAuditTrailTransId());
		laVehicleData.setDieselIndi(getDieselIndi());
		laVehicleData.setDotStndrdsIndi(getDOTStndrdsIndi());
		laVehicleData.setDpsStlnIndi(getDPSStlnIndi());
		laVehicleData.setFloodDmgeIndi(getFloodDmgeIndi());
		laVehicleData.setFxdWtIndi(getFxdWtIndi());
		laVehicleData.setPrmtReqrdIndi(getPrmtReqrdIndi());
		laVehicleData.setReContIndi(getRecontIndi());
		laVehicleData.setReplicaVehMk(getReplicaVehMk());
		laVehicleData.setReplicaVehModlYr(getReplicaVehModlYr());
		laVehicleData.setTrlrType(getTrlrType());
		laVehicleData.setVehBdyType(getVehBdyType());
		laVehicleData.setVehBdyVin(getVehBdyType());
		laVehicleData.setVehClassCd(getVehClassCd());
		laVehicleData.setVehEmptyWt(getVehEmptyWt());
		laVehicleData.setVehLngth(getVehLngth());
		laVehicleData.setVehMk(getVehMk());
		laVehicleData.setVehModl(getVehModl());
		laVehicleData.setVehModlYr(getVehModlYr());
		laVehicleData.setVehOdmtrBrnd(getVehOdmtrBrnd());
		laVehicleData.setVehOdmtrReadng(getVehOdmtrReadng());
		laVehicleData.setVehTon(getVehTon());
		laVehicleData.setVin(getVIN());
		laVehicleData.setVinErrIndi(getVINErrIndi());
		laVehicleData.setRenwlYrMismatchIndi(getRenwlYrMsmtchIndi());
		return laVehicleData;
	}

	/**
	 * Returns the value of VehLngth
	 * @return  int 
	 */
	public final int getVehLngth()
	{
		return ciVehLngth;
	}

	/**
	 * Method description
	 * 
	 * @return
	 */
	public AddressData getVehLocAddrData()
	{
		if (caVehLocAddrData == null)
		{
			caVehLocAddrData = new AddressData();
		}
		return caVehLocAddrData;
	}

	/**
	 * Get AbstractValue of csVehMjrColorCd
	 * 
	 * @return String
	 */
	public String getVehMjrColorCd()
	{
		return csVehMjrColorCd;
	}

	/**
	 * Returns the value of VehMk
	 * @return  String 
	 */
	public final String getVehMk()
	{
		return csVehMk;
	}

	/**
	 * Get AbstractValue of csVehMnrColorCd
	 * 
	 * @return String 
	 */
	public String getVehMnrColorCd()
	{
		return csVehMnrColorCd;
	}

	/**
	 * Returns the value of VehModl
	 * @return  String 
	 */
	public final String getVehModl()
	{
		return csVehModl;
	}

	/**
	 * Returns the value of VehModlYr
	 * @return  int 
	 */
	public final int getVehModlYr()
	{
		return ciVehModlYr;
	}

	/**
	 * Returns the value of VehOdmtrBrnd
	 * @return  String 
	 */
	public final String getVehOdmtrBrnd()
	{
		return csVehOdmtrBrnd;
	}

	/**
	 * Returns the value of VehOdmtrReadng
	 * @return  String 
	 */
	public final String getVehOdmtrReadng()
	{
		return csVehOdmtrReadng;
	}

	/**
	 * Returns the value of VehSalesPrice
	 * @return  Dollar 
	 */
	public final Dollar getVehSalesPrice()
	{
		return caVehSalesPrice;
	}

	/**
	 * Returns the value of VehSoldDate
	 * @return  int 
	 */
	public final int getVehSoldDate()
	{
		return ciVehSoldDate;
	}

	/**
	 * Returns the value of VehTon
	 * @return  Dollar 
	 */
	public final Dollar getVehTon()
	{
		return caVehTon;
	}

	/**
	 * Returns the value of VehTradeInAllownce
	 * @return  Dollar 
	 */
	public final Dollar getVehTradeInAllownce()
	{
		return caVehTradeInAllownce;
	}

	/**
	 * Returns the value of VehUnitNo
	 * @return  String 
	 */
	public final String getVehUnitNo()
	{
		return csVehUnitNo;
	}

	/**
	 * Returns the value of VehValue
	 * @return  Dollar 
	 */
	public final Dollar getVehValue()
	{
		return caVehValue;
	}

	/**
	 * Returns the value of VIN
	 * @return  String 
	 */
	public final String getVIN()
	{
		return csVIN;
	}

	/**
	 * Returns the value of VINErrIndi
	 * @return  int 
	 */
	public final int getVINErrIndi()
	{
		return ciVINErrIndi;
	}

	/**
	 * Returns the value of VoidedTransIndi
	 * @return  int 
	 */
	public final int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}

	/**
	 * get VTNSource
	 * 
	 * @return String
	 */
	public String getVTNSource()
	{
		return csVTNSource;
	}

	/**
	 * Get VTRRegEmrgCd1
	 * 
	 * @return String
	 */
	public String getVTRRegEmrgCd1()
	{
		return csVTRRegEmrgCd1;
	}

	/**
	 * Get VTRRegEmrgCd2
	 * 
	 * @return String
	 */
	public String getVTRRegEmrgCd2()
	{
		return csVTRRegEmrgCd2;
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
	 * This method sets the value of AddlLienRecrdIndi.
	 * @param aiAddlLienRecrdIndi   int 
	 */
	public final void setAddlLienRecrdIndi(int aiAddlLienRecrdIndi)
	{
		ciAddlLienRecrdIndi = aiAddlLienRecrdIndi;
	}

	/**
	 * This method sets the value of AddlSurvivorIndi.
	 * 
	 * @param ciAddlSurvivorIndi 
	 */
	public void setAddlSurvivorIndi(int aiAddlSurvivorIndi)
	{
		ciAddlSurvivorIndi = aiAddlSurvivorIndi;
	}

	/**
	 * This method sets the value of AddlTradeInIndi.
	 * @param aiAddlTradeInIndi   int 
	 */
	public final void setAddlTradeInIndi(int aiAddlTradeInIndi)
	{
		ciAddlTradeInIndi = aiAddlTradeInIndi;
	}

	/**
	 * This method sets the value of AgncyLoandIndi.
	 * @param aiAgncyLoandIndi   int 
	 */
	public final void setAgncyLoandIndi(int aiAgncyLoandIndi)
	{
		ciAgncyLoandIndi = aiAgncyLoandIndi;
	}

	/**
	 * This method sets the value of AuditTrailTransId.
	 * @param asAuditTrailTransId   String 
	 */
	public final void setAuditTrailTransId(String asAuditTrailTransId)
	{
		csAuditTrailTransId = asAuditTrailTransId;
	}

	/**
	 * This method sets the value of AuthCd.
	 * @param asAuthCd   String 
	 */
	public final void setAuthCd(String asAuthCd)
	{
		csAuthCd = asAuthCd;
	}

	/**
	 * This method sets the value of BatchNo.
	 * @param asBatchNo   String 
	 */
	public final void setBatchNo(String asBatchNo)
	{
		csBatchNo = asBatchNo;
	}

	// defect 9631
	//	/**
	//	 * This method sets the value of CustBaseRegFee.
	//	 * @deprecated
	//	 * @param aaCustBaseRegFee   Dollar 
	//	 */
	//	public final void setCustBaseRegFee(Dollar aaCustBaseRegFee)
	//	{
	//		caCustBaseRegFee = aaCustBaseRegFee;
	//	}

	//	/**
	//	 * This method sets the value of CustDieselFee.
	//	 * @deprecated
	//	 * @param aaCustDieselFee   Dollar 
	//	 */
	//	public final void setCustDieselFee(Dollar aaCustDieselFee)
	//	{
	//		caCustDieselFee = aaCustDieselFee;
	//	}
	// end defect 9631

	/**
	 * This method sets the value of BndedTtlCd.
	 * @param asBndedTtlCd   String 
	 */
	public final void setBndedTtlCd(String asBndedTtlCd)
	{
		csBndedTtlCd = asBndedTtlCd;
	}

	/**
	 * This method sets the value of CacheTransAMDate
	 * 
	 * @param aiCacheTransAMDate int
	 */
	public void setCacheTransAMDate(int aiCacheTransAMDate)
	{
		ciCacheTransAMDate = aiCacheTransAMDate;
	}

	/**
	 * This method sets the value of CacheTransTime
	 * 
	 * @param aiCacheTransTime int
	 */
	public void setCacheTransTime(int aiCacheTransTime)
	{
		ciCacheTransTime = aiCacheTransTime;
	}

	/**
	 * This method sets the value of CCOIssueDate.
	 * 
	 * @param aiCCOIssueDate   int 
	 */
	public final void setCCOIssueDate(int aiCCOIssueDate)
	{
		ciCCOIssueDate = aiCCOIssueDate;
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
	 * This method sets the value of ClaimComptCntyNo.
	 * @param aiClaimComptCntyNo   int 
	 */
	public final void setClaimComptCntyNo(int aiClaimComptCntyNo)
	{
		ciClaimComptCntyNo = aiClaimComptCntyNo;
	}

	/**
	 * This method sets the value of ComptCntyNo.
	 * @param aiComptCntyNo   int 
	 */
	public final void setComptCntyNo(int aiComptCntyNo)
	{
		ciComptCntyNo = aiComptCntyNo;
	}

	/**
	 * This method sets the value of CustActulRegFee.
	 * @param aaCustActulRegFee   Dollar 
	 */
	public final void setCustActulRegFee(Dollar aaCustActulRegFee)
	{
		caCustActulRegFee = aaCustActulRegFee;
	}

	/**
	 * This method sets the value of CustSeqNo.
	 * @param aiCustSeqNo   int 
	 */
	public final void setCustSeqNo(int aiCustSeqNo)
	{
		ciCustSeqNo = aiCustSeqNo;
	}

	/**
	 * This method sets the value of DieselIndi.
	 * @param aiDieselIndi   int 
	 */
	public final void setDieselIndi(int aiDieselIndi)
	{
		ciDieselIndi = aiDieselIndi;
	}

	/**
	 * set DissociateCd
	 * 
	 * @param aiDissociateCd
	 */
	public void setDissociateCd(int aiDissociateCd)
	{
		ciDissociateCd = aiDissociateCd;
	}

	/**
	 * This method sets the value of DlrGDN.
	 * @param asDlrGDN   String 
	 */
	public final void setDlrGDN(String asDlrGDN)
	{
		csDlrGDN = asDlrGDN;
	}

	/**
	 * This method sets the value of DocNo.
	 * @param asDocNo   String 
	 */
	public final void setDocNo(String asDocNo)
	{
		csDocNo = asDocNo;
	}

	/**
	 * This method sets the value of DocTypeCd.
	 * @param aiDocTypeCd   int 
	 */
	public final void setDocTypeCd(int aiDocTypeCd)
	{
		ciDocTypeCd = aiDocTypeCd;
	}

	/**
	 * This method sets the value of DOTStndrdsIndi.
	 * @param aiDOTStndrdsIndi   int 
	 */
	public final void setDOTStndrdsIndi(int aiDOTStndrdsIndi)
	{
		ciDOTStndrdsIndi = aiDOTStndrdsIndi;
	}

	/**
	 * This method sets the value of DPSSaftySuspnIndi.
	 * @param aiDPSSaftySuspnIndi   int 
	 */
	public final void setDPSSaftySuspnIndi(int aiDPSSaftySuspnIndi)
	{
		ciDPSSaftySuspnIndi = aiDPSSaftySuspnIndi;
	}

	/**
	 * This method sets the value of DPSStlnIndi.
	 * @param aiDPSStlnIndi   int 
	 */
	public final void setDPSStlnIndi(int aiDPSStlnIndi)
	{
		ciDPSStlnIndi = aiDPSStlnIndi;
	}

	/**
	 * Sets value of ciEMailRenwlReqCd
	 * 
	 * @param aiEMailRenwlReqCd
	 */
	public void setEMailRenwlReqCd(int aiEMailRenwlReqCd)
	{
		ciEMailRenwlReqCd = aiEMailRenwlReqCd;
	}

	/**
	 * This method sets the value of EmissionSalesTax.
	 * @param aaEmissionSalesTax   Dollar 
	 */
	public final void setEmissionSalesTax(Dollar aaEmissionSalesTax)
	{
		caEmissionSalesTax = aaEmissionSalesTax;
	}

	/**
	 * This method sets the value of EmissionSourceCd.
	 * @param asEmissionSourceCd   String 
	 */
	public final void setEmissionSourceCd(String asEmissionSourceCd)
	{
		csEmissionSourceCd = asEmissionSourceCd;
	}

	/**
	 * set ciETtlCd
	 * 
	 * @param aiETtlCd
	 */
	public void setETtlCd(int aiETtlCd)
	{
		ciETtlCd = aiETtlCd;
	}

	/**
	 * @param aiETtlPrntDate
	 */
	public void setETtlPrntDate(int aiETtlPrntDate)
	{
		ciETtlPrntDate = aiETtlPrntDate;
	}

	/**
	 * This method sets the value of ExmptIndi.
	 * @param aiExmptIndi   int 
	 */
	public final void setExmptIndi(int aiExmptIndi)
	{
		ciExmptIndi = aiExmptIndi;
	}

	/**
	 * @param aiExportIndi
	 */
	public void setExportIndi(int aiExportIndi)
	{
		ciExportIndi = aiExportIndi;
	}

	/**
	 * This method sets the value of FileTierCd.
	 * @param aiFileTierCd   int 
	 */
	public final void setFileTierCd(int aiFileTierCd)
	{
		ciFileTierCd = aiFileTierCd;
	}

	/**
	 * This method sets the value of FloodDmgeIndi.
	 * @param aiFloodDmgeIndi   int 
	 */
	public final void setFloodDmgeIndi(int aiFloodDmgeIndi)
	{
		ciFloodDmgeIndi = aiFloodDmgeIndi;
	}
	/**
	 * This method sets the value of FxdWtIndi.
	 * @param aiFxdWtIndi   int 
	 */
	public final void setFxdWtIndi(int aiFxdWtIndi)
	{
		ciFxdWtIndi = aiFxdWtIndi;
	}

	//	/**
	//	 * This method sets the value of Lien1Date.
	//	 * @param aiLien1Date   int 
	//	 */
	//	public final void setLien1Date(int aiLien1Date)
	//	{
	//		ciLien1Date = aiLien1Date;
	//	}
	//
	//	/**
	//	 * This method sets the value of Lien2Date.
	//	 * @param aiLien2Date   int 
	//	 */
	//	public final void setLien2Date(int aiLien2Date)
	//	{
	//		ciLien2Date = aiLien2Date;
	//	}

	/**
	 * This method sets the value of GovtOwndIndi.
	 * @param aiGovtOwndIndi   int 
	 */
	public final void setGovtOwndIndi(int aiGovtOwndIndi)
	{
		ciGovtOwndIndi = aiGovtOwndIndi;
	}

	//	/**
	//	 * This method sets the value of Lien3Date.
	//	 * @param aiLien3Date   int 
	//	 */
	//	public final void setLien3Date(int aiLien3Date)
	//	{
	//		ciLien3Date = aiLien3Date;
	//	}

	/**
	 * This method sets the value of HoopsRegPltNo.
	 * @param asHoopsRegPltNo   String 
	 */
	public final void setHoopsRegPltNo(String asHoopsRegPltNo)
	{
		csHoopsRegPltNo = asHoopsRegPltNo;
	}

	/**
	 * This method sets the value of HvyVehUseTaxIndi.
	 * @param aiHvyVehUseTaxIndi   int 
	 */
	public final void setHvyVehUseTaxIndi(int aiHvyVehUseTaxIndi)
	{
		ciHvyVehUseTaxIndi = aiHvyVehUseTaxIndi;
	}

	//	/**
	//	 * This method sets the value of LienHldr1City.
	//	 * @param asLienHldr1City   String 
	//	 */
	//	public final void setLienHldr1City(String asLienHldr1City)
	//	{
	//		csLienHldr1City = asLienHldr1City;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr1Cntry.
	//	 * @param asLienHldr1Cntry   String 
	//	 */
	//	public final void setLienHldr1Cntry(String asLienHldr1Cntry)
	//	{
	//		csLienHldr1Cntry = asLienHldr1Cntry;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr1Name1.
	//	 * @param asLienHldr1Name1   String 
	//	 */
	//	public final void setLienHldr1Name1(String asLienHldr1Name1)
	//	{
	//		csLienHldr1Name1 = asLienHldr1Name1;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr1Name2.
	//	 * @param asLienHldr1Name2   String 
	//	 */
	//	public final void setLienHldr1Name2(String asLienHldr1Name2)
	//	{
	//		csLienHldr1Name2 = asLienHldr1Name2;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr1St1.
	//	 * @param asLienHldr1St1   String 
	//	 */
	//	public final void setLienHldr1St1(String asLienHldr1St1)
	//	{
	//		csLienHldr1St1 = asLienHldr1St1;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr1St2.
	//	 * @param asLienHldr1St2   String 
	//	 */
	//	public final void setLienHldr1St2(String asLienHldr1St2)
	//	{
	//		csLienHldr1St2 = asLienHldr1St2;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr1State.
	//	 * @param asLienHldr1State   String 
	//	 */
	//	public final void setLienHldr1State(String asLienHldr1State)
	//	{
	//		csLienHldr1State = asLienHldr1State;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr1ZpCd.
	//	 * @param asLienHldr1ZpCd   String 
	//	 */
	//	public final void setLienHldr1ZpCd(String asLienHldr1ZpCd)
	//	{
	//		csLienHldr1ZpCd = asLienHldr1ZpCd;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr1ZpCdP4.
	//	 * @param asLienHldr1ZpCdP4   String 
	//	 */
	//	public final void setLienHldr1ZpCdP4(String asLienHldr1ZpCdP4)
	//	{
	//		csLienHldr1ZpCdP4 = asLienHldr1ZpCdP4;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr2City.
	//	 * @param asLienHldr2City   String 
	//	 */
	//	public final void setLienHldr2City(String asLienHldr2City)
	//	{
	//		csLienHldr2City = asLienHldr2City;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr2Cntry.
	//	 * @param asLienHldr2Cntry   String 
	//	 */
	//	public final void setLienHldr2Cntry(String asLienHldr2Cntry)
	//	{
	//		csLienHldr2Cntry = asLienHldr2Cntry;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr2Name1.
	//	 * @param asLienHldr2Name1   String 
	//	 */
	//	public final void setLienHldr2Name1(String asLienHldr2Name1)
	//	{
	//		csLienHldr2Name1 = asLienHldr2Name1;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr2Name2.
	//	 * @param asLienHldr2Name2   String 
	//	 */
	//	public final void setLienHldr2Name2(String asLienHldr2Name2)
	//	{
	//		csLienHldr2Name2 = asLienHldr2Name2;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr2St1.
	//	 * @param asLienHldr2St1   String 
	//	 */
	//	public final void setLienHldr2St1(String asLienHldr2St1)
	//	{
	//		csLienHldr2St1 = asLienHldr2St1;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr2St2.
	//	 * @param asLienHldr2St2   String 
	//	 */
	//	public final void setLienHldr2St2(String asLienHldr2St2)
	//	{
	//		csLienHldr2St2 = asLienHldr2St2;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr2State.
	//	 * @param asLienHldr2State   String 
	//	 */
	//	public final void setLienHldr2State(String asLienHldr2State)
	//	{
	//		csLienHldr2State = asLienHldr2State;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr2ZpCd.
	//	 * @param asLienHldr2ZpCd   String 
	//	 */
	//	public final void setLienHldr2ZpCd(String asLienHldr2ZpCd)
	//	{
	//		csLienHldr2ZpCd = asLienHldr2ZpCd;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr2ZpCdP4.
	//	 * @param asLienHldr2ZpCdP4   String 
	//	 */
	//	public final void setLienHldr2ZpCdP4(String asLienHldr2ZpCdP4)
	//	{
	//		csLienHldr2ZpCdP4 = asLienHldr2ZpCdP4;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr3City.
	//	 * @param asLienHldr3City   String 
	//	 */
	//	public final void setLienHldr3City(String asLienHldr3City)
	//	{
	//		csLienHldr3City = asLienHldr3City;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr3Cntry.
	//	 * @param assLienHldr3Cntry   String 
	//	 */
	//	public final void setLienHldr3Cntry(String asLienHldr3Cntry)
	//	{
	//		csLienHldr3Cntry = asLienHldr3Cntry;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr3Name1.
	//	 * @param asLienHldr3Name1   String 
	//	 */
	//	public final void setLienHldr3Name1(String asLienHldr3Name1)
	//	{
	//		csLienHldr3Name1 = asLienHldr3Name1;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr3Name2.
	//	 * @param asLienHldr3Name2   String 
	//	 */
	//	public final void setLienHldr3Name2(String asLienHldr3Name2)
	//	{
	//		csLienHldr3Name2 = asLienHldr3Name2;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr3St1.
	//	 * @param asLienHldr3St1   String 
	//	 */
	//	public final void setLienHldr3St1(String asLienHldr3St1)
	//	{
	//		csLienHldr3St1 = asLienHldr3St1;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr3St2.
	//	 * @param asLienHldr3St2   String 
	//	 */
	//	public final void setLienHldr3St2(String asLienHldr3St2)
	//	{
	//		csLienHldr3St2 = asLienHldr3St2;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr3State.
	//	 * @param asLienHldr3State   String 
	//	 */
	//	public final void setLienHldr3State(String asLienHldr3State)
	//	{
	//		csLienHldr3State = asLienHldr3State;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr3ZpCd.
	//	 * @param asLienHldr3ZpCd   String 
	//	 */
	//	public final void setLienHldr3ZpCd(String asLienHldr3ZpCd)
	//	{
	//		csLienHldr3ZpCd = asLienHldr3ZpCd;
	//	}
	//
	//	/**
	//	 * This method sets the value of LienHldr3ZpCdP4.
	//	 * @param asLienHldr3ZpCdP4   String 
	//	 */
	//	public final void setLienHldr3ZpCdP4(String asLienHldr3ZpCdP4)
	//	{
	//		csLienHldr3ZpCdP4 = asLienHldr3ZpCdP4;
	//	}

	/**
	 * This method sets the value of IMCNo.
	 * @param aaIMCNo   Dollar 
	 */
	public final void setIMCNo(Dollar aaIMCNo)
	{
		caIMCNo = aaIMCNo;
	}

	/**
	 * This method sets the value of InspectnWaivedIndi.
	 * @param aiInspectnWaivedIndi   int 
	 */
	public final void setInspectnWaivedIndi(int aiInspectnWaivedIndi)
	{
		ciInspectnWaivedIndi = aiInspectnWaivedIndi;
	}

	/**
	 * This method sets the value of JnkCd.
	 * @param aiJnkCd   int 
	 */
	public final void setJnkCd(int aiJnkCd)
	{
		ciJnkCd = aiJnkCd;
	}

	/**
	 * This method sets the value of JnkDate.
	 * @param aiJnkDate   int 
	 */
	public final void setJnkDate(int aiJnkDate)
	{
		ciJnkDate = aiJnkDate;
	}

	/**
	 * This method sets the value of LegalRestrntNo.
	 * @param asLegalRestrntNo   String 
	 */
	public final void setLegalRestrntNo(String asLegalRestrntNo)
	{
		csLegalRestrntNo = asLegalRestrntNo;
	}

	/**
	 * This method sets the value of LemonLawIndi
	 * 
	 * @param aiLemonLawIndi   int 
	 */
	public final void setLemonLawIndi(int aiLemonLawIndi)
	{
		ciLemonLawIndi = aiLemonLawIndi;
	}

	/**
	 * This method sets the value of Lien2NotRlsedIndi.
	 * @param aiLien2NotRlsedIndi   int 
	 */
	public final void setLien2NotRlsedIndi(int aiLien2NotRlsedIndi)
	{
		ciLien2NotRlsedIndi = aiLien2NotRlsedIndi;
	}

	/**
	 * This method sets the value of Lien3NotRlsedIndi.
	 * @param aiLien3NotRlsedIndi   int 
	 */
	public final void setLien3NotRlsedIndi(int aiLien3NotRlsedIndi)
	{
		ciLien3NotRlsedIndi = aiLien3NotRlsedIndi;
	}

	/**
	 * Set LienholderData for provided Integer
	 * 
	 * @param aaLienhldrData
	 */
	public void setLienholderData(
		Integer aiId,
		LienholderData aaLienhldrData)
	{
		aaLienhldrData =
			aaLienhldrData == null
				? new LienholderData()
				: aaLienhldrData;
		chtLienholderData.put(aiId, aaLienhldrData);
	}

	/**
	 * This method sets the value of LienNotRlsedIndi.
	 * @param aiLienNotRlsedIndi   int 
	 */
	public final void setLienNotRlsedIndi(int aiLienNotRlsedIndi)
	{
		ciLienNotRlsedIndi = aiLienNotRlsedIndi;
	}

	/**
	 * Set LIENRLSEDATE for Lien 1
	 * 
	 * @param aiLienRlseDate1
	 */
	public void setLienRlseDate1(int aiLienRlseDate1)
	{
		ciLienRlseDate1 = aiLienRlseDate1;
	}

	/**
	 * Set LIENRLSEDATE for Lien 2
	 * 
	 * @param aiLienRlseDate2
	 */
	public void setLienRlseDate2(int aiLienRlseDate2)
	{
		ciLienRlseDate2 = aiLienRlseDate2;
	}

	/**
	 * Set LIENRLSEDATE for Lien 3
	 * 
	 * @param aiLienRlseDate3
	 */
	public void setLienRlseDate3(int aiLienRlseDate3)
	{
		ciLienRlseDate3 = aiLienRlseDate3;
	}

	//	/**
	//	 * This method sets the value of OwnrCity.
	//	 * @param asOwnrCity   String 
	//	 */
	//	public final void setOwnrCity(String asOwnrCity)
	//	{
	//		csOwnrCity = asOwnrCity;
	//	}
	//
	//	/**
	//	 * This method sets the value of OwnrCntry.
	//	 * @param asOwnrCntry   String 
	//	 */
	//	public final void setOwnrCntry(String asOwnrCntry)
	//	{
	//		csOwnrCntry = asOwnrCntry;
	//	}

	/**
	 * This method sets the value of MfDwnCd.
	 * @param aiMfDwnCd   int 
	 */
	public final void setMfDwnCd(int aiMfDwnCd)
	{
		ciMfDwnCd = aiMfDwnCd;
	}

	//	/**
	//	 * This method sets the value of OwnrId.
	//	 * @param aOwnrId   String 
	//	 */
	//	public final void setOwnrId(String aOwnrId)
	//	{
	//		csOwnrId = aOwnrId;
	//	}

	/**
	 * Set NonTtlGolfCartCd
	 * 
	 * @param asNonTtlGolfCartCd	String
	 */
	public void setNonTtlGolfCartCd(String asNonTtlGolfCartCd)
	{
		csNonTtlGolfCartCd = asNonTtlGolfCartCd;
	}

	/**
	 * This method sets the value of NotfyngCity.
	 * @param asNotfyngCity   String 
	 */
	public final void setNotfyngCity(String asNotfyngCity)
	{
		csNotfyngCity = asNotfyngCity;
	}

	/**
	 * This method sets the value of OfcIssuanceCd.
	 * @param aiOfcIssuanceCd   int 
	 */
	public final void setOfcIssuanceCd(int aiOfcIssuanceCd)
	{
		ciOfcIssuanceCd = aiOfcIssuanceCd;
	}

	//	/**
	//	 * This method sets the value of OwnrSt1.
	//	 * @param asOwnrSt1   String 
	//	 */
	//	public final void setOwnrSt1(String asOwnrSt1)
	//	{
	//		csOwnrSt1 = asOwnrSt1;
	//	}
	//
	//	/**
	//	 * This method sets the value of OwnrSt2.
	//	 * @param asOwnrSt2   String 
	//	 */
	//	public final void setOwnrSt2(String asOwnrSt2)
	//	{
	//		csOwnrSt2 = asOwnrSt2;
	//	}
	//
	//	/**
	//	 * This method sets the value of OwnrState.
	//	 * @param asOwnrState   String 
	//	 */
	//	public final void setOwnrState(String asOwnrState)
	//	{
	//		csOwnrState = asOwnrState;
	//	}

	/**
	 * This method sets the value of OfcIssuanceNo.
	 * @param aiOfcIssuanceNo   int 
	 */
	public final void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}

	/**
	 * This method sets the value of OldDocNo.
	 * @param asOldDocNo   String 
	 */
	public final void setOldDocNo(String asOldDocNo)
	{
		csOldDocNo = asOldDocNo;
	}

	/**
	 * This method sets the value of OthrGovtTtlNo.
	 * @param asOthrGovtTtlNo   String 
	 */
	public final void setOthrGovtTtlNo(String asOthrGovtTtlNo)
	{
		csOthrGovtTtlNo = asOthrGovtTtlNo;
	}

	//	/**
	//	 * This method sets the value of OwnrTtlName1.
	//	 * @param asOwnrTtlName1   String 
	//	 */
	//	public final void setOwnrTtlName1(String asOwnrTtlName1)
	//	{
	//		csOwnrTtlName1 = asOwnrTtlName1;
	//	}
	//
	//	/**
	//	 * This method sets the value of OwnrTtlName2.
	//	 * @param asOwnrTtlName2   String 
	//	 */
	//	public final void setOwnrTtlName2(String asOwnrTtlName2)
	//	{
	//		csOwnrTtlName2 = asOwnrTtlName2;
	//	}
	//
	//	/**
	//	 * This method sets the value of OwnrZpCd.
	//	 * @param asOwnrZpCd   String 
	//	 */
	//	public final void setOwnrZpCd(String asOwnrZpCd)
	//	{
	//		csOwnrZpCd = asOwnrZpCd;
	//	}
	//
	//	/**
	//	 * This method sets the value of OwnrZpCdP4.
	//	 * @param asOwnrZpCdP4   String 
	//	 */
	//	public final void setOwnrZpCdP4(String asOwnrZpCdP4)
	//	{
	//		csOwnrZpCdP4 = asOwnrZpCdP4;
	//	}

	/**
	 * This method sets the value of OthrStateCntry.
	 * @param asOthrStateCntry   String 
	 */
	public final void setOthrStateCntry(String asOthrStateCntry)
	{
		csOthrStateCntry = asOthrStateCntry;
	}

	/**
	 * Method description
	 * 
	 * @param data
	 */
	public void setOwnerData(OwnerData data)
	{
		caOwnerData = (data == null ? new OwnerData() : data);
	}

	/**
	 * This method sets the value of OwnrFstName.
	 * @param asOwnrFstName   String 
	 */
	public final void setOwnrFstName(String asOwnrFstName)
	{
		csOwnrFstName = asOwnrFstName;
	}

	/**
	 * This method sets the value of OwnrLstName.
	 * @param asOwnrLstName   String 
	 */
	public final void setOwnrLstName(String asOwnrLstName)
	{
		csOwnrLstName = asOwnrLstName;
	}

	/**
	 * This method sets the value of OwnrMI.
	 * @param asOwnrMI   String 
	 */
	public final void setOwnrMI(String asOwnrMI)
	{
		csOwnrMI = asOwnrMI;
	}

	/**
	 * This method sets the value of OwnrshpEvidCd.
	 * @param aiOwnrshpEvidCd   int 
	 */
	public final void setOwnrshpEvidCd(int aiOwnrshpEvidCd)
	{
		ciOwnrshpEvidCd = aiOwnrshpEvidCd;
	}

	/**
	 * This method sets the value of OwnrSuppliedExpYr.
	 * @param aiOwnrSuppliedExpYr   int 
	 */
	public final void setOwnrSuppliedExpYr(int aiOwnrSuppliedExpYr)
	{
		ciOwnrSuppliedExpYr = aiOwnrSuppliedExpYr;
	}

	/**
	 * This method sets the value of OwnrSuppliedPltNo.
	 * @param asOwnrSuppliedPltNo   String 
	 */
	public final void setOwnrSuppliedPltNo(String asOwnrSuppliedPltNo)
	{
		csOwnrSuppliedPltNo = asOwnrSuppliedPltNo;
	}

	/**
	 * This method sets the value of OwnrSuppliedStkrNo.
	 * @param asOwnrSuppliedStkrNo   String 
	 */
	public final void setOwnrSuppliedStkrNo(String asOwnrSuppliedStkrNo)
	{
		csOwnrSuppliedStkrNo = asOwnrSuppliedStkrNo;
	}

	/**
	 * Set PERMLIENHLDRID for Lien 1
	 * 
	 * @param asPermLienHldrId1
	 */
	public void setPermLienHldrId1(String asPermLienHldrId1)
	{
		csPermLienHldrId1 = asPermLienHldrId1;
	}

	/**
	 * Set PERMLIENHLDRID for Lien 2
	 * 
	 * @param asPermLienHldrID
	 */
	public void setPermLienHldrId2(String asPermLienHldrId2)
	{
		csPermLienHldrId2 = asPermLienHldrId2;
	}

	/**
	 * Set PERMLIENHLDRID for Lien 3
	 * 
	 * @param asPermLienHldrID
	 */
	public void setPermLienHldrId3(String asPermLienHldrId3)
	{
		csPermLienHldrId3 = asPermLienHldrId3;
	}

	/**
	 * This method sets the value of PltBirthDate.
	 * @param aiPltBirthDate   int 
	 */
	public final void setPltBirthDate(int aiPltBirthDate)
	{
		ciPltBirthDate = aiPltBirthDate;
	}

	/**
	 * This method sets the value of PltsSeizdIndi.
	 * @param aiPltsSeizdIndi   int 
	 */
	public final void setPltsSeizdIndi(int aiPltsSeizdIndi)
	{
		ciPltsSeizdIndi = aiPltsSeizdIndi;
	}

	/**
	 * This method sets the value of PrevOwnrCity.
	 * @param asPrevOwnrCity   String 
	 */
	public final void setPrevOwnrCity(String asPrevOwnrCity)
	{
		csPrevOwnrCity = asPrevOwnrCity;
	}

	/**
	 * This method sets the value of PrevOwnrName.
	 * @param asPrevOwnrName   String 
	 */
	public final void setPrevOwnrName(String asPrevOwnrName)
	{
		csPrevOwnrName = asPrevOwnrName;
	}

	/**
	 * This method sets the value of PrevOwnrState.
	 * @param asPrevOwnrState   String 
	 */
	public final void setPrevOwnrState(String asPrevOwnrState)
	{
		csPrevOwnrState = asPrevOwnrState;
	}

	/**
	 * This method sets the value of PriorCCOIssueIndi.
	 * @param aiPriorCCOIssueIndi   int 
	 */
	public final void setPriorCCOIssueIndi(int aiPriorCCOIssueIndi)
	{
		ciPriorCCOIssueIndi = aiPriorCCOIssueIndi;
	}

	/**
	 * set PrsmLvlCd
	 * 
	 * @param asPrsmLvlCd
	 */
	public void setPrismLvlCd(String asPrismLvlCd)
	{
		csPrismLvlCd = asPrismLvlCd;
	}
	//
	///**
	// * This method sets the value of PrivacyOptCd.
	// * @param aiPrivacyOptCd   int 
	// */
	//public final void setPrivacyOptCd(int aiPrivacyOptCd)
	//{
	//	ciPrivacyOptCd = aiPrivacyOptCd;
	//}

	/**
	 * This method sets the value of PrmtReqrdIndi.
	 * @param aiPrmtReqrdIndi   int 
	 */
	public final void setPrmtReqrdIndi(int aiPrmtReqrdIndi)
	{
		ciPrmtReqrdIndi = aiPrmtReqrdIndi;
	}

	/**
	 * Set PvtLawEnfVehCd
	 * 
	 * @param asPvtLawEnfVehCd	String
	 */
	public void setPvtLawEnfVehCd(String asPvtLawEnfVehCd)
	{
		csPvtLawEnfVehCd = asPvtLawEnfVehCd;
	}

	/**
	 * This method sets the value of RecondCd.
	 * @param aiRecondCd   int 
	 */
	public final void setRecondCd(int aiRecondCd)
	{
		ciRecondCd = aiRecondCd;
	}

	/**
	 * This method sets the value of RecontIndi.
	 * @param aiRecontIndi   int 
	 */
	public final void setRecontIndi(int aiRecontIndi)
	{
		ciRecontIndi = aiRecontIndi;
	}

	/**
	 * Sets value of csRecpntEMail
	 * 
	 * @param asRecpntEMail
	 */
	public void setRecpntEMail(String asRecpntEMail)
	{
		csRecpntEMail = asRecpntEMail;
	}

	/**
	 * This method sets the value of RecpntFstName.
	 * @param asRecpntFstName   String 
	 */
	public final void setRecpntFstName(String asRecpntFstName)
	{
		csRecpntFstName = asRecpntFstName;
	}

	/**
	 * This method sets the value of RecpntLstName.
	 * @param asRecpntLstName   String 
	 */
	public final void setRecpntLstName(String asRecpntLstName)
	{
		csRecpntLstName = asRecpntLstName;
	}

	/**
	 * This method sets the value of RecpntMI.
	 * @param asRecpntMI   String 
	 */
	public final void setRecpntMI(String asRecpntMI)
	{
		csRecpntMI = asRecpntMI;
	}

	/**
	 * This method sets the value of RecpntName.
	 * @param asRecpntName   String 
	 */
	public final void setRecpntName(String asRecpntName)
	{
		csRecpntName = asRecpntName;
	}

	/**
	 * This method sets the value of RegClassCd.
	 * @param aiRegClassCd   int 
	 */
	public final void setRegClassCd(int aiRegClassCd)
	{
		ciRegClassCd = aiRegClassCd;
	}

	/**
	 * This method sets the value of RegEffDate.
	 * @param aiRegEffDate   int 
	 */
	public final void setRegEffDate(int aiRegEffDate)
	{
		ciRegEffDate = aiRegEffDate;
	}

	/**
	 * This method sets the value of RegExpMo.
	 * @param aiRegExpMo   int 
	 */
	public final void setRegExpMo(int aiRegExpMo)
	{
		ciRegExpMo = aiRegExpMo;
	}

	/**
	 * This method sets the value of RegExpYr.
	 * @param aiRegExpYr   int 
	 */
	public final void setRegExpYr(int aiRegExpYr)
	{
		ciRegExpYr = aiRegExpYr;
	}

	//	/**
	//	 * This method sets the value of RenwlMailngCity.
	//	 * @param asRenwlMailngCity   String 
	//	 */
	//	public final void setRenwlMailngCity(String asRenwlMailngCity)
	//	{
	//		csRenwlMailngCity = asRenwlMailngCity;
	//	}
	//
	//	/**
	//	 * This method sets the value of RenwlMailngSt1.
	//	 * @param asRenwlMailngSt1   String 
	//	 */
	//	public final void setRenwlMailngSt1(String asRenwlMailngSt1)
	//	{
	//		csRenwlMailngSt1 = asRenwlMailngSt1;
	//	}
	//
	//	/**
	//	 * This method sets the value of RenwlMailngSt2.
	//	 * @param asRenwlMailngSt2   String 
	//	 */
	//	public final void setRenwlMailngSt2(String asRenwlMailngSt2)
	//	{
	//		csRenwlMailngSt2 = asRenwlMailngSt2;
	//	}

	//	/**
	//	 * This method sets the value of RenwlMailngState.
	//	 * @param asRenwlMailngState   String 
	//	 */
	//	public final void setRenwlMailngState(String asRenwlMailngState)
	//	{
	//		csRenwlMailngState = asRenwlMailngState;
	//	}
	//
	//	/**
	//	 * This method sets the value of RenwlMailngZPCd.
	//	 * @param asRenwlMailngZPCd   String 
	//	 */
	//	public final void setRenwlMailngZPCd(String asRenwlMailngZPCd)
	//	{
	//		csRenwlMailngZPCd = asRenwlMailngZPCd;
	//	}
	//
	//	/**
	//	 * This method sets the value of RenwlMailngZPCdP4.
	//	 * @param asRenwlMailngZPCdP4   String 
	//	 */
	//	public final void setRenwlMailngZPCdP4(String asRenwlMailngZPCdP4)
	//	{
	//		csRenwlMailngZPCdP4 = asRenwlMailngZPCdP4;
	//	}

	/**
	 * This method sets the value of RegHotCkIndi.
	 * @param aiRegHotCkIndi   int 
	 */
	public final void setRegHotCkIndi(int aiRegHotCkIndi)
	{
		ciRegHotCkIndi = aiRegHotCkIndi;
	}

	/**
	 * This method sets the value of RegInvldIndi.
	 * @param aiRegInvldIndi   int 
	 */
	public final void setRegInvldIndi(int aiRegInvldIndi)
	{
		ciRegInvldIndi = aiRegInvldIndi;
	}

	/**
	 * This method sets the value of RegPltAge.
	 * @param aiRegPltAge   int 
	 */
	public final void setRegPltAge(int aiRegPltAge)
	{
		ciRegPltAge = aiRegPltAge;
	}

	/**
	 * This method sets the value of RegPltCd.
	 * @param asRegPltCd   String 
	 */
	public final void setRegPltCd(String asRegPltCd)
	{
		csRegPltCd = asRegPltCd;
	}

	/**
	 * This method sets the value of RegPltNo.
	 * @param assRegPltNo   String 
	 */
	public final void setRegPltNo(String asRegPltNo)
	{
		csRegPltNo = asRegPltNo;
	}

	/**
	 * This method sets the value of RegPltOwnrName.
	 * @param asRegPltOwnrName   String 
	 */
	public final void setRegPltOwnrName(String asRegPltOwnrName)
	{
		csRegPltOwnrName = asRegPltOwnrName;
	}

	/**
	 * This method sets the value of RegRefAmt.
	 * @param aaRegRefAmt   Dollar 
	 */
	public final void setRegRefAmt(Dollar aaRegRefAmt)
	{
		caRegRefAmt = aaRegRefAmt;
	}

	/**
	 * This method sets the value of RegStkrCd.
	 * @param asRegStkrCd   String 
	 */
	public final void setRegStkrCd(String asRegStkrCd)
	{
		csRegStkrCd = asRegStkrCd;
	}

	/**
	 * Sets Renewal Address Data 
	 * 
	 * @param aaRenwlAddrData
	 */
	public void setRenewalAddrData(AddressData aaRenwlAddrData)
	{
		caRenewalAddrData =
			(aaRenwlAddrData == null
				? new AddressData()
				: aaRenwlAddrData);
	}

	/**
	 * This method sets the value of RenwlMailRtrnIndi.
	 * @param aiRenwlMailRtrnIndi   int 
	 */
	public final void setRenwlMailRtrnIndi(int aiRenwlMailRtrnIndi)
	{
		ciRenwlMailRtrnIndi = aiRenwlMailRtrnIndi;
	}

	/**
	 * This method sets the value of RenwlYrMsmtchIndi.
	 * @param aiRenwlYrMsmtchIndi   int 
	 */
	public final void setRenwlYrMsmtchIndi(int aiRenwlYrMsmtchIndi)
	{
		ciRenwlYrMsmtchIndi = aiRenwlYrMsmtchIndi;
	}
	/**
	 * This method sets the value of ReplicaVehMk.
	 * @param asReplicaVehMk   String 
	 */
	public final void setReplicaVehMk(String asReplicaVehMk)
	{
		csReplicaVehMk = asReplicaVehMk;
	}

	/**
	 * This method sets the value of ReplicaVehModlYr.
	 * @param aiReplicaVehModlYr   int 
	 */
	public final void setReplicaVehModlYr(int aiReplicaVehModlYr)
	{
		ciReplicaVehModlYr = aiReplicaVehModlYr;
	}

	/**
	 * This method sets the value of ResComptCntyNo.
	 * @param aiResComptCntyNo   int 
	 */
	public final void setResComptCntyNo(int aiResComptCntyNo)
	{
		ciResComptCntyNo = aiResComptCntyNo;
	}

	/**
	 * This method sets the value of SalesTaxCat.
	 * @param asSalesTaxCat   String 
	 */
	public final void setSalesTaxCat(String asSalesTaxCat)
	{
		csSalesTaxCat = asSalesTaxCat;
	}

	/**
	 * This method sets the value of SalesTaxDate.
	 * @param aiSalesTaxDate   int 
	 */
	public final void setSalesTaxDate(int aiSalesTaxDate)
	{
		ciSalesTaxDate = aiSalesTaxDate;
	}

	/**
	 * This method sets the value of SalesTaxExmptCd.
	 * @param aiSalesTaxExmptCd   int 
	 */
	public final void setSalesTaxExmptCd(int aiSalesTaxExmptCd)
	{
		ciSalesTaxExmptCd = aiSalesTaxExmptCd;
	}

	/**
	 * This method sets the value of SalesTaxPdAmt.
	 * @param aaSalesTaxPdAmt   Dollar 
	 */
	public final void setSalesTaxPdAmt(Dollar aaSalesTaxPdAmt)
	{
		caSalesTaxPdAmt = aaSalesTaxPdAmt;
	}

	/**
	 * This method sets the value of SalvIndi
	 * 
	 * @param aiSalvIndi 
	 */
	public void setSalvIndi(int aiSalvIndi)
	{
		ciSalvIndi = aiSalvIndi;
	}

	/**
	 * This method sets the value of SalvStateCntry.
	 * @param asSalvStateCntry   String 
	 */
	public final void setSalvStateCntry(String asSalvStateCntry)
	{
		csSalvStateCntry = asSalvStateCntry;
	}

	/**
	 * This method sets the value of SalvYardNo.
	 * @param s   String 
	 */
	public final void setSalvYardNo(String asSalvYardNo)
	{
		csSalvYardNo = asSalvYardNo;
	}

	/**
	 * This method sets the value of SpclPltProgIndi.
	 * @param aiSpclPltProgIndi   int 
	 */
	public final void setSpclPltProgIndi(int aiSpclPltProgIndi)
	{
		ciSpclPltProgIndi = aiSpclPltProgIndi;
	}

	/**
	 * This method sets the value of StkrSeizdIndi.
	 * @param aiStkrSeizdIndi   int 
	 */
	public final void setStkrSeizdIndi(int aiStkrSeizdIndi)
	{
		ciStkrSeizdIndi = aiStkrSeizdIndi;
	}

	/**
	 * This method sets the value of SubconId.
	 * @param aiSubconId   int 
	 */
	public final void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}

	/**
	 * This method sets the value of SubconIssueDate.
	 * @param aiSubconIssueDate   int 
	 */
	public final void setSubconIssueDate(int aiSubconIssueDate)
	{
		ciSubconIssueDate = aiSubconIssueDate;
	}

	/**
	 * This method sets the value of SubstaId.
	 * @param aiSubstaId   int 
	 */
	public final void setSubstaId(int aiSubstaId)
	{
		ciSubstaId = aiSubstaId;
	}

	/**
	 * This method sets the value of SurrTtlDate.
	 * @param aiSurrTtlDate   int 
	 */
	public final void setSurrTtlDate(int aiSurrTtlDate)
	{
		ciSurrTtlDate = aiSurrTtlDate;
	}

	/**
	 * This method sets the value of SurvshpRightsIndi.
	 * @param aiSurvshpRightsIndi   int 
	 */
	public final void setSurvshpRightsIndi(int aiSurvshpRightsIndi)
	{
		ciSurvshpRightsIndi = aiSurvshpRightsIndi;
	}

	/**
	 * This method sets the value of SurvShpRightsName1
	 * 
	 * @param asSurvShpRightsName1 
	 */
	public void setSurvShpRightsName1(String asSurvShpRightsName1)
	{
		csSurvShpRightsName1 = asSurvShpRightsName1;
	}

	/**
	 * This method sets the value of asSurvShpRightsName2
	 * 
	 * @param asSurvShpRightsName2 
	 */
	public void setSurvShpRightsName2(String asSurvShpRightsName2)
	{
		csSurvShpRightsName2 = asSurvShpRightsName2;
	}

	/**
	 * This method sets the value of TaxPdOthrState.
	 * @param aaTaxPdOthrState   Dollar 
	 */
	public final void setTaxPdOthrState(Dollar aaTaxPdOthrState)
	{
		caTaxPdOthrState = aaTaxPdOthrState;
	}

	/**
	 * This method sets the value of TireTypeCd.
	 * @param asTireTypeCd   String 
	 */
	public final void setTireTypeCd(String asTireTypeCd)
	{
		csTireTypeCd = asTireTypeCd;
	}

	/**
	 * This method sets the value of TotalRebateAmt.
	 * @param aaTotalRebateAmt   Dollar 
	 */
	public final void setTotalRebateAmt(Dollar aaTotalRebateAmt)
	{
		caTotalRebateAmt = aaTotalRebateAmt;
	}

	/**
	 * This method sets the value of TradeInVehMk.
	 * @param asTradeInVehMk   String 
	 */
	public final void setTradeInVehMk(String asTradeInVehMk)
	{
		csTradeInVehMk = asTradeInVehMk;
	}

	/**
	 * This method sets the value of TradeInVehVin.
	 * @param asTradeInVehVin   String 
	 */
	public final void setTradeInVehVin(String asTradeInVehVin)
	{
		csTradeInVehVin = asTradeInVehVin;
	}

	/**
	 * This method sets the value of TradeInVehYr.
	 * @param aiTradeInVehYr   int 
	 */
	public final void setTradeInVehYr(int aiTradeInVehYr)
	{
		ciTradeInVehYr = aiTradeInVehYr;
	}

	/**
	 * This method sets the value of TransAMDate.
	 * @param aiTransAMDate   int 
	 */
	public final void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}

	/**
	 * This method sets the value of TransCd.
	 * @param asTransCd   String 
	 */
	public final void setTransCd(String asTransCd)
	{
		csTransCd = asTransCd;
	}

	/**
	 * This method sets the value of TransEmpId.
	 * @param asTransEmpId   String 
	 */
	public final void setTransEmpId(String asTransEmpId)
	{
		csTransEmpId = asTransEmpId;
	}

	/**
	 * Sets the value of TransId
	 * @param asTransid  
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}

	/**
	 * This method sets the value of TransTime.
	 * @param aiTransTime   int 
	 */
	public final void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * This method sets the value of TransWsId.
	 * @param aiTransWsId   int 
	 */
	public final void setTransWsId(int aiTransWsId)
	{
		ciTransWsId = aiTransWsId;
	}

	/**
	 * This method sets the value of TrlrType.
	 * @param asTrlrType   String 
	 */
	public final void setTrlrType(String asTrlrType)
	{
		csTrlrType = asTrlrType;
	}

	//	/**
	//	 * This method sets the value of TtlVehLocCity.
	//	 * @param asTtlVehLocCity   String 
	//	 */
	//	public final void setTtlVehLocCity(String asTtlVehLocCity)
	//	{
	//		csTtlVehLocCity = asTtlVehLocCity;
	//	}
	//
	//	/**
	//	 * This method sets the value of TtlVehLocSt1.
	//	 * @param asTtlVehLocSt1   String 
	//	 */
	//	public final void setTtlVehLocSt1(String asTtlVehLocSt1)
	//	{
	//		csTtlVehLocSt1 = asTtlVehLocSt1;
	//	}
	//
	//	/**
	//	 * This method sets the value of TtlVehLocSt2.
	//	 * @param asTtlVehLocSt2   String 
	//	 */
	//	public final void setTtlVehLocSt2(String asTtlVehLocSt2)
	//	{
	//		csTtlVehLocSt2 = asTtlVehLocSt2;
	//	}
	//
	//	/**
	//	 * This method sets the value of TtlVehLocState.
	//	 * @param asTtlVehLocState   String 
	//	 */
	//	public final void setTtlVehLocState(String asTtlVehLocState)
	//	{
	//		csTtlVehLocState = asTtlVehLocState;
	//	}
	//
	//	/**
	//	 * This method sets the value of TtlVehLocZpCd.
	//	 * @param asTtlVehLocZpCd   String 
	//	 */
	//	public final void setTtlVehLocZpCd(String asTtlVehLocZpCd)
	//	{
	//		csTtlVehLocZpCd = asTtlVehLocZpCd;
	//	}
	//
	//	/**
	//	 * This method sets the value of TtlVehLocZpCdP4.
	//	 * @param asTtlVehLocZpCdP4   String 
	//	 */
	//	public final void setTtlVehLocZpCdP4(String asTtlVehLocZpCdP4)
	//	{
	//		csTtlVehLocZpCdP4 = asTtlVehLocZpCdP4;
	//	}

	// defect 9969 
	//	/**
	//	 * Set AAMVAMsgId
	//	 * 
	//	 * @param aaVTRRegEmrgCd1	String
	//	 */
	//	public void setAAMVAMsgId(String aaAAMVAMsgId)
	//	{
	//		csAAMVAMsgId = aaAAMVAMsgId;
	//	}
	// end defect 9969 

	/**
	 * This method sets the value of TtlApplDate.
	 * @param aiTtlApplDate   int 
	 */
	public final void setTtlApplDate(int aiTtlApplDate)
	{
		ciTtlApplDate = aiTtlApplDate;
	}

	/**
	 * Set value of ciTtlExmnIndi
	 * 
	 * @param aiTtlExmnIndi
	 */
	public void setTtlExmnIndi(int aiTtlExmnIndi)
	{
		ciTtlExmnIndi = aiTtlExmnIndi;
	}

	/**
	 * This method sets the value of TtlHotCkIndi.
	 * @param aiTtlHotCkIndi   int 
	 */
	public final void setTtlHotCkIndi(int aiTtlHotCkIndi)
	{
		ciTtlHotCkIndi = aiTtlHotCkIndi;
	}

	/**
	 * This method sets the value of TtlIssueDate.
	 * @param aiTtlIssueDate   int 
	 */
	public final void setTtlIssueDate(int aiTtlIssueDate)
	{
		ciTtlIssueDate = aiTtlIssueDate;
	}

	/**
	 * This method sets the value of TtlNoMf.
	 * @param asTtlNoMf   String 
	 */
	public final void setTtlNoMf(String asTtlNoMf)
	{
		csTtlNoMf = asTtlNoMf;
	}

	/**
	 * This method sets the value of TtlProcsCd.
	 * @param asTtlProcsCd   String 
	 */
	public final void setTtlProcsCd(String asTtlProcsCd)
	{
		csTtlProcsCd = asTtlProcsCd;
	}

	/**
	 * This method sets the value of TtlRejctnDate.
	 * @param aiTtlRejctnDate   int 
	 */
	public final void setTtlRejctnDate(int aiTtlRejctnDate)
	{
		ciTtlRejctnDate = aiTtlRejctnDate;
	}

	/**
	 * This method sets the value of TtlRejctnOfc.
	 * @param aiTtlRejctnOfc   int 
	 */
	public final void setTtlRejctnOfc(int aiTtlRejctnOfc)
	{
		ciTtlRejctnOfc = aiTtlRejctnOfc;
	}

	/**
	 * This method sets the value of TtlRevkdIndi.
	 * @param aiTtlRevkdIndi   int 
	 */
	public final void setTtlRevkdIndi(int aiTtlRevkdIndi)
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
	 * set TtlTrnsfrEntCd
	 * 
	 * @param string
	 */
	public void setTtlTrnsfrEntCd(String asTtlTrnsfrEntCd)
	{
		csTtlTrnsfrEntCd = asTtlTrnsfrEntCd;
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

	/**
	 * Set VTRRegEmrgCd1
	 * 
	 * @param aaVTRRegEmrgCd1	String
	 */
	public void setUTVMislblIndi(int aiUTVMislblIndi)
	{
		ciUTVMislblIndi = aiUTVMislblIndi;
	}

	/**
	 * set V21PltId
	 * 
	 * @param aiV21PltId
	 */
	public void setV21PltId(int aiV21PltId)
	{
		ciV21PltId = aiV21PltId;
	}

	/**
	 * set V21VTNId
	 * 
	 * @param aiV21VTNId
	 */
	public void setV21VTNId(int aiV21VTNId)
	{
		ciV21VTNId = aiV21VTNId;
	}

	/**
	 * This method sets the value of VehBdyType.
	 * @param asVehBdyType   String 
	 */
	public final void setVehBdyType(String asVehBdyType)
	{
		csVehBdyType = asVehBdyType;
	}

	/**
	 * This method sets the value of VehBdyVin.
	 * @param asVehBdyVin   String 
	 */
	public final void setVehBdyVin(String asVehBdyVin)
	{
		csVehBdyVin = asVehBdyVin;
	}

	/**
	 * This method sets the value of VehCaryngCap.
	 * @param aiVehCaryngCap   int 
	 */
	public final void setVehCaryngCap(int aiVehCaryngCap)
	{
		ciVehCaryngCap = aiVehCaryngCap;
	}

	/**
	 * This method sets the value of VehClassCd.
	 * @param asVehClassCd   String 
	 */
	public final void setVehClassCd(String asVehClassCd)
	{
		csVehClassCd = asVehClassCd;
	}

	/**
	 * This method sets the value of VehEmptyWt.
	 * @param aiVehEmptyWt   int 
	 */
	public final void setVehEmptyWt(int aiVehEmptyWt)
	{
		ciVehEmptyWt = aiVehEmptyWt;
	}

	/**
	 * This method sets the value of VehGrossWt.
	 * @param aiVehGrossWt   int 
	 */
	public final void setVehGrossWt(int aiVehGrossWt)
	{
		ciVehGrossWt = aiVehGrossWt;
	}

	/**
	 * This method sets the value of VehLngth.
	 * @param aiVehLngth   int 
	 */
	public final void setVehLngth(int aiVehLngth)
	{
		ciVehLngth = aiVehLngth;
	}

	/**
	 * Method description
	 * 
	 * @param data
	 */
	public void setVehLocAddrData(AddressData data)
	{
		caVehLocAddrData = (data == null ? new AddressData() : data);
	}

	/**
	 * Set AbstractValue of csVehMjrColorCd
	 * 
	 * @param asVehMjrColorCd
	 */
	public void setVehMjrColorCd(String asVehMjrColorCd)
	{
		csVehMjrColorCd = asVehMjrColorCd;
	}

	/**
	 * This method sets the value of VehMk.
	 * @param saVehMk   String 
	 */
	public final void setVehMk(String asVehMk)
	{
		csVehMk = asVehMk;
	}

	/**
	 * Set AbstractValue of csVehMnrColorCd
	 * 
	 * @param asVehMnrColorCd
	 */
	public void setVehMnrColorCd(String asVehMnrColorCd)
	{
		csVehMnrColorCd = asVehMnrColorCd;
	}

	/**
	 * This method sets the value of VehModl.
	 * @param asVehModl   String 
	 */
	public final void setVehModl(String asVehModl)
	{
		csVehModl = asVehModl;
	}

	/**
	 * This method sets the value of VehModlYr.
	 * @param aVehModlYr   int 
	 */
	public final void setVehModlYr(int aVehModlYr)
	{
		ciVehModlYr = aVehModlYr;
	}

	/**
	 * This method sets the value of VehOdmtrBrnd.
	 * @param asVehOdmtrBrnd   String 
	 */
	public final void setVehOdmtrBrnd(String asVehOdmtrBrnd)
	{
		csVehOdmtrBrnd = asVehOdmtrBrnd;
	}

	/**
	 * This method sets the value of VehOdmtrReadng.
	 * @param asVehOdmtrReadng   String 
	 */
	public final void setVehOdmtrReadng(String asVehOdmtrReadng)
	{
		csVehOdmtrReadng = asVehOdmtrReadng;
	}

	/**
	 * This method sets the value of VehSalesPrice.
	 * @param aaVehSalesPrice   Dollar 
	 */
	public final void setVehSalesPrice(Dollar aaVehSalesPrice)
	{
		caVehSalesPrice = aaVehSalesPrice;
	}

	/**
	 * This method sets the value of VehSoldDate.
	 * @param aiVehSoldDate   int 
	 */
	public final void setVehSoldDate(int aiVehSoldDate)
	{
		ciVehSoldDate = aiVehSoldDate;
	}

	/**
	 * This method sets the value of VehTon.
	 * @param aaVehTon   Dollar 
	 */
	public final void setVehTon(Dollar aaVehTon)
	{
		caVehTon = aaVehTon;
	}

	/**
	 * This method sets the value of VehTradeInAllownce.
	 * @param aaVehTradeInAllownce   Dollar 
	 */
	public final void setVehTradeInAllownce(Dollar aaVehTradeInAllownce)
	{
		caVehTradeInAllownce = aaVehTradeInAllownce;
	}

	/**
	 * This method sets the value of VehUnitNo.
	 * @param aaVehUnitNo   String 
	 */
	public final void setVehUnitNo(String aaVehUnitNo)
	{
		csVehUnitNo = aaVehUnitNo;
	}

	/**
	 * This method sets the value of VehValue
	 * @param aaVehValue Dollar 
	 */
	public final void setVehValue(Dollar aaVehValue)
	{
		caVehValue = aaVehValue;
	}

	/**
	 * This method sets the value of VIN.
	 * @param asVIN   String 
	 */
	public final void setVIN(String asVIN)
	{
		csVIN = asVIN;
	}

	/**
	 * This method sets the value of VINErrIndi.
	 * @param aiVINErrIndi   int 
	 */
	public final void setVINErrIndi(int aiVINErrIndi)
	{
		ciVINErrIndi = aiVINErrIndi;
	}

	/**
	 * This method sets the value of VoidedTransIndi.
	 * @param aiVoidedTransIndi   int 
	 */
	public final void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;

	}

	/**
	 * set VTNSource
	 * 
	 * @param asVTNSource
	 */
	public void setVTNSource(String asVTNSource)
	{
		csVTNSource = asVTNSource;
	}

	/**
	 * Set VTRRegEmrgCd1
	 * 
	 * @param aaVTRRegEmrgCd1	String
	 */
	public void setVTRRegEmrgCd1(String aaVTRRegEmrgCd1)
	{
		csVTRRegEmrgCd1 = aaVTRRegEmrgCd1;
	}

	/**
	 * Set VTRRegEmrgCd2
	 * 
	 * @param aaVTRRegEmrgCd2	String
	 */
	public void setVTRRegEmrgCd2(String aaVTRRegEmrgCd2)
	{
		csVTRRegEmrgCd2 = aaVTRRegEmrgCd2;
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
