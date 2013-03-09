package com.txdot.isd.rts.services.data;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * CompleteTransactionData.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		09/17/2001	Change class to serializable
 * N Ting		09/17/2001	Change ciRegRnltyChrgIndi to 
 * 							ciRegPnltyChrgIndi
 * Ray Rowehl	08/21/2002	Add Internet Transaction Data Object
 *							defect 3700
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add variables cvStickers, cSubconRenwlData
 * 							and associated get/set methods.
 * 							Ver 5.2.0
 * K Harrell	04/19/2004	Add variables for Reprint Sticker
 *							ciRprStkOfcIssuanceNo,
 *							ciRprStkTransWsId, ciRprStkTransAMDate,
 *							ciRprStkTransTime & associated get/set
 *							methods 
 *							defect 7018 Ver 5.2.0  
 * K Harrell	04/22/2005	Java 1.4 Work
 *							defect 7899 Ver 5.2.3
 * B. Brown		08/18/2005  Regain removed fields needed due to Complete
 * 							TransactionData being stored in table. 
 * 							rts_itrnt_data.
 * 							add cOrgVehicleInfo,cVehicleInfo,
 * 							    cRegFeesData,cdollarCustSubtotal,
 * 							    cRegTtlAddlInfoData 
 * 							add getCOrgVehicleInfo(),getCVehicleInfo(),
 * 							    getCRegFeesData(),
 * 								getCdollarCustSubtotal()
 * 							    getCRegTtlAddlInfoData()
 * 							modify getOrgVehicleInfo(),getVehicleInfo(),
 * 								   getRegFeesData(),getCustSubtotal(),
 * 								   getRegTtlAddlInfoData() 			
 *							defect 7899 Ver 5.2.3
 * K Harrell	09/30/2005	Refactoring services.data.webapps 
 * 							moved and renamed InternetTransactionData
 * 							defect 7889 Ver 5.2.3 	 
 * B Hargrove	11/02/2005	Add getter/setter for CommonFees Reg 
 * 							Period Length (used in date edit on REG029). 
 * 							add ciRegPeriodLngth, getRegPeriodLngth(),
 * 							setRegPeriodLngth()
 * 							defect 8404 Ver 5.2.2 Fix 7
 * K Harrell	10/24/2006	add method to build Exempt Audit Data 
 * 							add getExemptAuditData(),getInvItmNo(),
 * 							  getPltNo(), initExemptAuditData()
 * 							add FIRST_RECORD,SECOND_RECORD,THIRD_RECORD,
 *							 PLATE_TRCKNG_TYPE
 * 							defect 8900 Ver Exempts
 * K Harrell	11/05/2006	Ensure correct assignment of Expiration Date
 * 							when creating Exempt Audit Data Object
 * 							add buildExpirationDt()
 * 							modify getExemptAuditData()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/13/2006	Add 'VOIDNC' to transaction processing for 
 * 							ExemptAuditData
 * 							modify getExemptAuditData()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/17/2006	Add population of ExemptAuditData.MfDwnCd
 * 							Do not write PriorExemptIndi as 1 for SBRNW 
 * 							modify initExemptAuditData()
 * 							defect 9017 Ver Exempts
 * B Hargrove 	02/12/2007 	For Special Plates Full implementation, we
 * 							do not check InvProcsngCd to check for  
 * 							Special Plate. Remove indicator used between
 * 							Fees and REG029 that was based on InvProcsngCd.
 * 							Add getters\setters for SpclPlate From Month,
 * 							SpclPlate From Year, SpclPlate Number of 
 * 							Months to Charge.
 * 							add ciSpclPlateFromMo, ciSpclPlateFromYr,
 *							ciSpclPlateNoMoCharge, getSpclPlateFromMo(),
 *							getSpclPlateFromYr(), getSpclPlateNoMoCharge(),
 *							setSpclPlateFromMo(), setSpclPlateFromYr(), 
 *							setSpclPlateNoMoCharge()
 * 							delete ciTtlInWinInvPrCdNotEqTwoIndi, 
 * 							getTtlInWinInvPrCdNotEqTwoIndi(),
 * 							setTtlInWinInvPrCdNotEqTwoIndi() 
 * 							defect 9126 Ver Special Plates
 * B Hargrove 	02/23/2007 	Add indicator to charge either the 
 * 							Replacement Fee or the higher Remake Fee
 * 							when replacing a Personalized Plate.
 * 							See:  FrmPlateSelectionREG011
 * 							add csSpclPlateReplFeeCd,
 * 							getSpclPlateReplFeeCd(),
 * 							setSpclPlateReplFeeCd()
 * 							defect 9126 Ver Special Plates
 * K Harrell	03/03/2007	Removed the transition variables / methods
 * 							implemented for 5.2.3 transition
 * 							(see B Brown 7899 defect implemented 
 * 							 08/18/2005) 
 *							delete cOrgVehicleInfo,cVehicleInfo,
 * 							   cRegFeesData,cdollarCustSubtotal,
 * 							   cRegTtlAddlInfoData 
 * 							delete getCOrgVehicleInfo(),getCVehicleInfo(),
 * 							   getCRegFeesData(),
 * 							   getCdollarCustSubtotal()
 * 							   getCRegTtlAddlInfoData()
 * 							modify getOrgVehicleInfo(),getVehicleInfo(),
 * 							   getRegFeesData(),getCustSubtotal(),
 * 							   getRegTtlAddlInfoData()
 * K Harrell	03/03/2007	removed csSpclPlateReplFeeCd, get/set methods
 * 							Replaced by indicator in SpecialPlateRegisData
 * 							defect 9085 Ver Special Plates 
 * B Hargrove	04/03/2007	Add csTxtSpclPltMoSold, 
 * 							getTxtSpclPltMoSold(), setTxtSpclPltMoSold()
 * 							defect 9126 Ver Special Plates
 * K Harrell	05/24/2007	add setupForIAPPL()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/29/2007	Remove ciSpclPltProgIndi, get/set methods()
 * 							modify constructor.
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/11/2007	Use current date for calculating IAPPL 
 * 							MfgDate, Exp Mo/Yr 
 * 							modify setupForIAPPL()
 * 							defect 9359 Ver Special Plates   
 * B Brown		01/25/2008	Switch the statement order for calculating
 * 							IAPPL special plate exp mo and exp yr, 
 * 							so when a Special Plate is purchased online
 * 							in January (exp mo is 1),
 * 							the exp yr is the current year, 
 * 							not current year + 1  
 * 							modify setupForIAPPL()
 * 							defect 9508 Ver Tres Amigos prep
 * B Hargrove	04/16/2008	Checking references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe. (see: defect 9557).
 * 							No changes needed here. 
 * 							defect 9631 Ver Defect POS A   
 * B Brown		06/17/2008	Add processing for MyPlates
 * 							transcd's VPAPPL and VPAPPR.
 * 							add setupForVENDOR()
 * 							defect 9711 Ver MyPlates_POS. 
 * B Brown		07/14/2008	Add setting transEmpId in SpecialPlateRegis
 * 							Data object
 * 							add setupForVENDOR()
 * 							defect 9711 Ver MyPlates_POS.
 * K Harrell	01/07/2009  Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify setupForIAPPL(),setupForVENDOR()   
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	03/20/2009	add getETtlHstryData()
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	04/28/2009	add assignment of SubstaId 
 * 							modify getETtlHstryData()
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	07/03/2009	delete ciOwnrSuppliedStkrAge, get/set methods
 * 							modify caMailingAddrData, get/set methods
 * 							modify getETtlHstryData(), 
 * 							 getExemptAuditData()
 * 							defect 10112 Ver Defect_POS_F	
 * J Zwiener	08/28/2009  add getSpclPlateFee(), getSplcPlatePLPFee()
 * 							defect 10097 Ver Defect_POS_F
 * K Harrell	12/15/2009	add caDlrTtlData, get/set methods
 * 							delete cvDlrTtlDataObjs, get/set methods 
 * 							defect 10290 Ver Defect_POS_H 
 * K Harrell	12/18/2009	ciPltTerm removed from SpecialPlateRegisData
 * 							Use PltValidityTerm.
 * 							modify setupForVENDOR()
 * 							defect 10311 Ver Defect_POS_H
 * K Harrell	03/22/2010	Use PltValidityTerm vs. PltTerm in 
 * 							 SpecialPlateItrntTransData
 * 							add assignment of MktngAllowdIndi, AuctnPltIndi, 
 * 							 ResrvReasnCd, SpclRegId 
 * 							modify setupForVENDOR() 
 * 							defect 10366 Ver POS_640
 * Ray Rowehl	03/29/2010	Use the passed trans code instead of 
 * 							computing it.  
 * 							modify setupForVENDOR()
 * 							defect 10401 Ver 6.4.0
 * K Harrell	04/30/2010	Add calculation of AuctnPdAmt for 
 * 							SpecialPlateItrntTransData
 * 							modify setupForVENDOR()
 * 							defect 10366 Ver POS_640 
 * Ray Rowehl	05/04/2010	Need to setup the mfgstatuscd and associated
 * 							fields based on transcode.
 * 							modify setupForVENDOR()
 * 							defect 10401 Ver 6.4.0
 * K Harrell	06/14/2010	add assignment of TransId for ETtlHstryData
 * 							and ExemptAuditData
 * 							modify getETtlHstryData(), getExemptAuditData()
 * 							defect 10505 Ver 6.5.0 
 * K Harrell	07/14/2010	add assignAllocInvItmsForTimedPermit(), 
 * 							 deleteAllocInvItmsForTimedPermit()
 * 							defect 10491 Ver 6.5.0
 * K Harrell	09/13/2010	add cbPreviewReceipt, isPreviewReceipt(),
 * 							 setPreviewReceipt()
 * 							defect 10590 Ver 6.6.0
 * K Harrell	09/28/2010	modify getTransId(), getBatchCount()  
 * 							defect 10590 Ver 6.6.0  
 * K Harrell	12/23/2010	add caSpclPltPrmtData, get/set methods 
 * 							defect 10700 Ver 6.7.0
 * K Harrell	01/21/2011	add CompleteTransactionData(WebAgencyTransactionData,
 * 							 Vector) 
 * 							modify getSpclPlateFee(), getSpclPlatePLPFee() 
 * 							defect 10734 Ver 6.7.0
 * K Harrell	02/04/2011	add assignDataForSubconReceipt() 
 * 							defect 10745 Ver 6.7.0   
 * K Harrell	04/22/2011	add assignDataForSubconReceipt(SubcontractorRenewalCacheData,
 * 							SubcontractorRenewalData, String,
 * 							WebAgencyTransactionData) 
 * 							defect 10745 Ver 6.7.0 
 * K Harrell	04/26/2011  modify assignDataForSubconReceipt()
 * 							defect 10745 Ver 6.7.1 
 * K Harrell	11/06/2011  add CompleteTransactionData(
 * 							 WebAgencyTransaction, Vector,
 * 							 WebAgencyTransactionAddressData, 
 *  						 WebAgencyTransactionAddressData)
 *  						add cbWRENEWSubconAddr, get/set methods 
 *  						delete CompleteTransactionData(WebAgencyTransaction,
 *  							Vector)
 * 							modify assignDataForSubconReceipt(
 * 							  SubcontractorRenewalCacheData,
 * 							  SubcontractorRenewalData, String)
 * 							defect 11137 Ver 6.9.0 
 * B Brown		11/30/2011	Set PltValidityTerm to a 1 for IAPPL's
 * 							modify setupForIAPPL()
 * 							defect 10957 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/** 
 * This class contains all the data required for the completion of the 
 * transaction.
 * 
 * @version	6.9.0  				11/30/2011
 * @author	Sunil Govindappa
 * @since 						09/07/2001
 * 
 */
public class CompleteTransactionData implements java.io.Serializable
{
	// defect 10590 
	// boolean 
	private boolean cbPreviewReceipt = false;
	// end defect 10590 
	
	// defect 11137 
	private boolean cbWRENEWSubconAddr = false; 
	// end defect 11137 
	
	// Dollar
	// Annual Diesel Fee
	private Dollar caAnnualDieselFee;
	// Annual Registration Fee
	private Dollar caAnnualRegFee;
	// Credit Remaining
	private Dollar caCrdtRemaining;

	// Objects
	private CreditCardFeeData caCreditCardFeeData;
	// Customer Actual Registration Fee
	private Dollar caCustActualRegFee;
	// Customer Base Registration Fee
	private Dollar caCustBaseRegFee;
	// Diesel Fee
	private Dollar caDieselFee;
	// CLSOUT
	private Dollar caDollarBsnDateTotalAmt;
	private Dollar caDollarCustSubtotal = new Dollar("0.00");
	private FundsUpdateData caFundsUpdate;
	// InternetTransactionData Object
	private InternetTransactionData caInternetTransactionData;
	private InventoryFunctionTransactionData caInvFuncTrans;
	// For CCO 
	// defect 10112 
	private NameAddressData caMailingAddrData;
	// end defect 10112 
	// Original MFVehicleData object extracted from the mainframe
	private MFVehicleData caOrgVehicleInfo;
	// Registration Fees object which holds the fees and valid exp dates for a transaction
	private RegFeesData caRegFeesData;
	// Registration Penalty Fee
	private Dollar caRegisPenaltyFee;
	// Registration Items Data 
	private RegistrationItemsData caRegItemsData;
	// Registrational/Title Additional Info screens information
	private RegTtlAddlInfoData caRegTtlAddlInfoData;
	private SubcontractorRenewalData caSubconRenwlData;
	// Total fee entered for each transaction during Subcon Renewal
	private Dollar caSubConRenwlTotalFee;
	private SubcontractorRenewalCacheData caSubcontractorRenewalCacheData;
	private TimedPermitData caTimedPermitData = null;

	// defect 10700 
	private SpecialPlatePermitData caSpclPltPrmtData = null;
	// end defect 10700   

	// Modified MFVehicleData object after passing through events
	private MFVehicleData caVehicleInfo;
	// Vehicle Misc Data object
	private VehMiscData caVehMisc;
	// Vehicle Sales Tax amount
	private Dollar caVehSalesTaxAmt;
	// Vehicle Tax amount
	private Dollar caVehTaxAmt;
	// Vehicle Tax Penalty
	private Dollar caVehTaxPnlty;
	// Vehicle Total Sales Tax paid
	private Dollar caVehTotalSalesTaxPd;
	private Dollar caVoidPymntAmt;
	// Used for VOID transaction
	private TransactionHeaderData caVoidTransactionHeaderData;

	// int 
	private int ciCashWsid;
	// Current Transaction Number
	private int ciCurrTransNo;
	// Dealer ID
	private int ciDealerId;
	// Disable Additional Fee Indicator
	private int ciDisableAddlFees;
	// Disable County Fee Indicator
	private int ciDisableCtyFees;
	// Expiration Month set in REG029 screen for fees recalculation
	private int ciExpMo = 0;
	// Expiration Year set in REG029 screen for fees recalculation
	private int ciExpYr = 0;
	// Fiscal Year
	private int ciFsclYr;
	// Used in REG029 screen to check the validity of Expiration date 
	// entered by user
	private int ciFxdExpMo = 0;
	// Inventory Item Count
	private int ciInvItemCount;
	// Indicator for charging mail fee
	private int ciMailFeeApplies;
	// Do not charge Fee indicator
	private int ciNoChrgIndi;
	private int ciNoMFRecs;
	private int ciNoMoChrg;
	private int ciNoMoCredit;
	private int ciOfcIssuanceNo;

	// defect 10112
	//private int ciOwnrSuppliedStkrAge;
	// end defect 10112 

	// No of rows of temp additional weight options to be shown on 
	// MRG011 screen during Misc Registration for Temp Addl weight 
	private int ciPeriodOptRowsread;
	private int ciPrintOptions;
	// Fee calculation - temporary additional weight
	private int ciPrmtEffDate;
	// Indicator set in Reg029 screen for fees recalculation
	private int ciReCalcIndi;
	// Registration Penalty Charge Indicator
	// defect 8404
	private int ciRegPeriodLngth;
	// end defect 8404
	private int ciRegPnltyChrgIndi;
	// defect 7018
	// Add columns for Reprint Sticker Transaction
	private int ciRprStkOfcIssuanceNo;
	private int ciRprStkTransAMDate;
	private int ciRprStkTransTime;
	private int ciRprStkTransWsId;
	// end defect 7018
	// Corresponds to the same field in VehicleInquiryData
	private int ciSavedInvItmCount;
	// defect 9126 / 9085
	// Special Plate progress indicator
	//private int ciSpclPlateProgIndi;
	private int ciSpclPlateFromMo = 0;
	private int ciSpclPlateFromYr = 0;
	private int ciSpclPlateNoMoCharge = 0;
	// end defect 9126 / 9085

	// Subcontractor ID
	private int ciSubconId;
	// Indicator denoting scanned Subcon renewal
	private int ciSubConRenwlBarCdIndi;
	// Temporary vehicle gross weight entered during misc registration
	private int ciTempVehGrossWt;
	private int ciTransactionDate;
	// For SendCache and internet Transaction
	private int ciTransAMDate;
	private int ciTransGregorianDt;
	private int ciTransTime;
	private int ciVoidAllIndi;
	private int ciVoidCustSeqNo;
	private int ciVoidedTransIndi;
	private int ciVoidOfcIssuanceNo;
	private int ciVoidSubstaId;
	private int ciVoidTransAMDate;
	private int ciVoidTransTime;
	private int ciVoidTransWsId;

	// String
	private String csBatchCount;
	private String csCustName;
	// This variable holds the Error Message for Date Validation in DTA 
	private String csDTAErrorMsg = null;
	private String csInternetScreen;
	// Owner supplied plate number which is set in REG029 screen
	private String csOwnrSuppliedPltNo;
	// Owner supplied sticker number which is in REG029 screen
	private String csOwnrSuppliedStkrNo;
	// Used in Transaction Processing
	private String csSavedTransCd;
	// Transaction Code
	private String csTransCode;
	// Used for receipts
	private String csTransId;
	// Number of months of registration & credit message 
	private String csTxtRegMoSold;
	// defect 9126
	// Number of months of Special Plate Fee sold
	private String csTxtSpclPltMoSold;
	// end defect 9126
	private String csVoidTransCd;
	private String csVoidTransName;

	// defect 10745 
	private String csWebAgntUserName;
	// end defect 10745 

	// Vector 
	// Vector of ProcessInventoryData w/ allocated inventory. 
	private Vector cvAllocInvItms;

	// defect 10290 
	private DealerTitleData caDlrTtlData;
	//private Vector cvDlrTtlDataObjs = null;
	// end defect 10290 

	// Vector of ProcessInventoryData w/ issued inventory
	private Vector cvInvItms = null;
	private Vector cvInvItmsAfterFees;
	// Vector of PeriodOpt objects holding Temp Add'l Wt Options
	private Vector cvPeriodOptVec;
	private Vector cvSavedInvItms;
	private Vector cvStickers;
	private Vector cvTransInvDetail;

	private static final int FIRST_RECORD = 0;
	private static final String PLATE_TRCKNG_TYPE = "P";
	private static final int SECOND_RECORD = 1;
	private final static long serialVersionUID = 4270307392803124584L;
	private static final String STICKER_TRCKNG_TYPE = "S";
	private static final int THIRD_RECORD = 2;

	/**
	 * getCacheItmTrkngType
	 * 
	 * @param  asItmCd String
	 * @return String
	 */
	public static String getCacheItmTrkngType(String asItmCd)
	{
		ItemCodesData laItmTrkngType = ItemCodesCache.getItmCd(asItmCd);
		if (laItmTrkngType != null)
		{
			return laItmTrkngType.getItmTrckngType();
		}
		else
		{
			return "";
		}
	}

	/**
	 * CompleteTransactionData constructor comment.
	 */
	public CompleteTransactionData()
	{
		super();
		ciCurrTransNo = -1;
		ciDisableAddlFees = 0;
		ciDisableCtyFees = 0;
		ciFsclYr = 0;
		ciInvItemCount = 0;
		ciNoChrgIndi = 0;
		ciNoMoChrg = 0;
		ciRegPnltyChrgIndi = 0;
		ciTransGregorianDt = 0;

		caAnnualDieselFee = null;
		caAnnualRegFee = null;
		caCustActualRegFee = null;
		caCustBaseRegFee = null;
		caDieselFee = null;
		caRegFeesData = new RegFeesData();
		caRegItemsData = new RegistrationItemsData();
		caRegTtlAddlInfoData = new RegTtlAddlInfoData();
		caVehicleInfo = null;
		caVehSalesTaxAmt = null;
		caVehTaxPnlty = null;
		caVehTotalSalesTaxPd = null;

	}
	/**
	 * CompleteTransactionData constructor comment.
	 */
	public CompleteTransactionData(
		WebAgencyTransactionData aaWATransData,
		Vector avWATransFeeData,
		WebAgencyTransactionAddressData aaWATransOwnrAddrData, 
		WebAgencyTransactionAddressData aaWATransRcpntAddrData)
	{
		super();
		setTransCode(TransCdConstant.WRENEW);
		setOfcIssuanceNo(aaWATransData.getResComptCntyNo());
		SubcontractorRenewalData laSubconData =
			new SubcontractorRenewalData(
				aaWATransData,
				avWATransFeeData);
		caSubcontractorRenewalCacheData =
			new SubcontractorRenewalCacheData();
		caSubcontractorRenewalCacheData.setTempSubconRenewalData(
			laSubconData);
		SubcontractorHdrData laSubconHdrData =
			new SubcontractorHdrData();
		laSubconHdrData.setSubconId(aaWATransData.getSubconId());
		caSubcontractorRenewalCacheData.setSubcontractorHdrData(
			laSubconHdrData);
		
		// defect 11137 
		if (aaWATransOwnrAddrData != null)
		{
			MFVehicleData laMfVehData = new MFVehicleData();
			RegistrationData laRegData = new RegistrationData();
			OwnerData laOwnrData = new OwnerData(); 
			laOwnrData.setName1(aaWATransData.getOwnrTtlName1()); 
			laOwnrData.setName2(aaWATransData.getOwnrTtlName2());
			laOwnrData.setAddressData(aaWATransOwnrAddrData.getAddrData()); 
			laMfVehData.setOwnerData(laOwnrData);
			laRegData.setRecpntName(aaWATransData.getRecpntName()); 
			if (aaWATransRcpntAddrData != null)
			{
				laRegData.setRenwlMailAddr(aaWATransRcpntAddrData.getAddrData());
			}
			laMfVehData.setRegData(laRegData); 
			setVehicleInfo(laMfVehData); 
		}
		// end defect 11137  
	}

	/**
	 * Delete Inventory For Timed Permit for Transaction Processing
	 *
	 * (not currently used - on standby if necessary ) 
	 */
	public void deleteAllocInvItmsForTimedPermit()
	{
		if (UtilityMethods.printsPermit(getTransCode()))
		{
			setAllocInvItms(null);
		}
	}
	/**
	 * Create CompleteTransactionData Object with Sticker/Plate 
	 * Objects for Receipt
	 * 
	 * @param aaSRCD SubcontractorRenewalCacheData
	 * @param aaSubconRenewData SubcontractorRenewalData
	 * @param asTransCd
	 * @param aaWATransData 
	 */
	public void assignDataForSubconReceipt(
		SubcontractorRenewalCacheData aaSRCD,
		SubcontractorRenewalData aaSubconRenewData,
		String asTransCd,
		WebAgencyTransactionData aaWATransData)
	{
		assignDataForSubconReceipt(
			aaSRCD,
			aaSubconRenewData,
			asTransCd);
		
		setSubconRenwlData(aaSubconRenewData); 
			
		setTransId("" + aaWATransData.getSavReqId());

		// If Special Plate and must 
		if (PlateTypeCache.isSpclPlate(aaWATransData.getRegPltCd())
			&& aaWATransData.getMustReplPltIndi() == 1)
		{
			caVehicleInfo.getSpclPltRegisData().setMFGStatusCd(
				SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD);
			caVehicleInfo.getSpclPltRegisData().setMfgSpclPltIndi(1);
			caVehicleInfo.getSpclPltRegisData().setMFGDate(
				TransCdConstant.WRENEW);
		}

	}

	/**
	 * Create CompleteTransactionData Object with Sticker/Plate 
	 * Objects for Receipt
	 * 
	 * @param aaSRCD SubcontractorRenewalCacheData
	 * @param aaSubconRenewData SubcontractorRenewalData
	 * @return CompleteTransactionData 
	 */
	public void assignDataForSubconReceipt(
		SubcontractorRenewalCacheData aaSRCD,
		SubcontractorRenewalData aaSubconRenewData,
		String asTransCd)
	{
		setTransCode(asTransCd);
		ProcessInventoryData laProcInvData = null;
		Vector lvInventory = new Vector();

		// Data for Receipt
		// Owner Info: Using Subcon Info as owner data
		//             If SBRWN only 
		
		// defect 11137 
		MFVehicleData laMFVehicle = getVehicleInfo(); 
		if (laMFVehicle == null)
		{
			laMFVehicle = new MFVehicleData();
			cbWRENEWSubconAddr = true;
		}
		OwnerData laOwnerData = laMFVehicle.getOwnerData(); 
		
		if (!csTransCode.equals(TransCdConstant.WRENEW) || cbWRENEWSubconAddr)
		{
			SubcontractorData laSubconData = aaSRCD.getSubconInfo();
	
			laOwnerData.setName1(laSubconData.getName1());
			laOwnerData.setName2(laSubconData.getName2());
			AddressData laAddressData =
				(AddressData) UtilityMethods.copy(
					laSubconData.getAddressData());
			laOwnerData.setAddressData(laAddressData);
			laMFVehicle.setOwnerData(laOwnerData);
		}
		// end defect 11137 
		
		// Title Info 
		TitleData laTitleData = new TitleData();
		laTitleData.setDocNo(aaSubconRenewData.getDocNo());
		laMFVehicle.setTitleData(laTitleData);

		// Reg Data 
		// defect 11137 
		RegistrationData laRegData = laMFVehicle.getRegData(); 
		if (laRegData == null)
		{
			laRegData = new RegistrationData(); 
		}
		// end defect 11137 
		
		laRegData.setRegClassCd(
			Integer.parseInt(aaSubconRenewData.getRegClassCd()));
		laRegData.setVehGrossWt(aaSubconRenewData.getVehGrossWt());
		laRegData.setRegExpMo(aaSubconRenewData.getRegExpMo());
		laRegData.setRegEffDt(aaSubconRenewData.getSubconIssueDate());
		laRegData.setRegPltCd(
			aaSubconRenewData.getPltItmCd() == null
				? ""
				: aaSubconRenewData.getPltItmCd().trim());
		laRegData.setRegStkrCd(
			aaSubconRenewData.getStkrItmCd() == null
				? ""
				: aaSubconRenewData.getStkrItmCd().trim());

		// Set RegPltCd, OrgNo in RegData
		if (aaSubconRenewData.getSpclPltIndi() == 1)
		{
			laRegData.setRegPltCd(
				aaSubconRenewData.getSpclPltRegPltCd());
		}

		if (PlateTypeCache.isSpclPlate(laRegData.getRegPltCd()))
		{
			SpecialPlatesRegisData laSpclPltRegisData =
				new SpecialPlatesRegisData();
			laSpclPltRegisData.setPltValidityTerm(
				aaSubconRenewData.getPltVldtyTerm());
			laSpclPltRegisData.setRegPltNo(
				aaSubconRenewData.getNewPltNo());
			laSpclPltRegisData.setRegPltCd(laRegData.getRegPltCd());
			laSpclPltRegisData.setPltExpMo(
				aaSubconRenewData.getPltNextExpMo());
			laSpclPltRegisData.setPltExpYr(
				aaSubconRenewData.getPltNextExpYr());
			laSpclPltRegisData.setOrgNo(aaSubconRenewData.getOrgNo());
			laMFVehicle.setSpclPltRegisData(laSpclPltRegisData);
		}

		// Inventory 
		// Sticker Issued
		int liRecordType = aaSubconRenewData.getRecordType();
		if (liRecordType == SubcontractorRenewalData.STKR
			|| liRecordType == SubcontractorRenewalData.PLT_STKR)
		{
			laProcInvData = new ProcessInventoryData();
			laProcInvData.setItmCd(aaSubconRenewData.getStkrItmCd());
			//set expiration year
			laProcInvData.setInvItmYr(aaSubconRenewData.getNewExpYr());
			lvInventory.add(laProcInvData);

			Vector lvStkrInv = new Vector();
			lvStkrInv.addElement(UtilityMethods.copy(laProcInvData));
			setStickers(lvStkrInv);
		}

		// Plate Issued
		if (liRecordType == SubcontractorRenewalData.PLT_STKR
			|| liRecordType == SubcontractorRenewalData.PLT)
		{
			laProcInvData = new ProcessInventoryData();
			laProcInvData.setInvItmNo(aaSubconRenewData.getNewPltNo());
			laRegData.setRegPltNo(aaSubconRenewData.getNewPltNo());
			laRegData.setPrevPltNo(aaSubconRenewData.getRegPltNo());
			laProcInvData.setItmCd(aaSubconRenewData.getPltItmCd());
			if (liRecordType == SubcontractorRenewalData.PLT)
			{
				laProcInvData.setInvItmYr(
					aaSubconRenewData.getNewExpYr());
			}
			else
			{
				laProcInvData.setInvItmYr(0);
			}
			lvInventory.add(laProcInvData);
		}
		else
		{
			laRegData.setRegPltNo(aaSubconRenewData.getRegPltNo());
			laRegData.setPrevPltNo(aaSubconRenewData.getRegPltNo());
		}
		setAllocInvItms(lvInventory);
		laMFVehicle.setRegData(laRegData);

		// Vehicle Info
		VehicleData laVehData = new VehicleData();
		laVehData.setVin(aaSubconRenewData.getVIN());
		laVehData.setVehMk(aaSubconRenewData.getVehMk());
		laVehData.setVehModl(aaSubconRenewData.getVehModl());
		laVehData.setVehModlYr(aaSubconRenewData.getVehModlYr());

		laMFVehicle.setVehicleData(laVehData);
		setVehicleInfo(laMFVehicle);
		// TransId 
		setTransId(aaSubconRenewData.getTransID());
		// Fees
		RegFeesData laRegFeesData = new RegFeesData();
		laRegFeesData.setVectFees(
			aaSubconRenewData.getFeesDataTrFunds());
		setRegFeesData(laRegFeesData);
		setExpMo(aaSubconRenewData.getRegExpMo());
		setExpYr(aaSubconRenewData.getNewExpYr());
		setOfcIssuanceNo(aaSubconRenewData.getCntyNo());
	}

	/**
	 * Assign Inventory For Timed Permit for Transaction Processing
	 *
	 */
	public void assignAllocInvItmsForTimedPermit()
	{
		PermitData laPrmtData = (PermitData) getTimedPermitData();
		ProcessInventoryData laInvAllocData =
			new ProcessInventoryData();
		laInvAllocData.setItmCd(laPrmtData.getItmCd());
		laInvAllocData.setInvItmNo(laPrmtData.getPrmtNo());
		laInvAllocData.setInvItmEndNo(laPrmtData.getPrmtNo());
		Vector lvInv = new Vector();
		lvInv.add(laInvAllocData);
		setAllocInvItms(lvInv);
	}

	/**
	 * Set which registration expiration year to by indi
	 *				IN = Inventory Issued
	 *				OS = Ownered Supplied.
	 *				MF = MFVeh
	 *  All code replaced in this method for PCR 34	
	 * 
	 * @param  aaTransData Object
	 * @return String
	 */
	private void buildExpirationDt()
	{
		String lsItmTrckngType = "";
		ProcessInventoryData laInvItms = new ProcessInventoryData();
		boolean lbExpMoYrSet = false;

		try
		{
			if (CommonFeesCache
				.isStandardExempt(
					caVehicleInfo.getRegData().getRegClassCd()))
			{
				ciExpMo = 0;
				ciExpYr = 0;
			}
			else
			{
				ciExpMo = caVehicleInfo.getRegData().getRegExpMo();

				// Assign Year where inventory is issued 
				if (ciExpYr == 0)
				{
					// Iterate through all inventory items to find a year.
					if (cvAllocInvItms != null
						&& cvAllocInvItms.size() > 0)
					{
						for (int i = 0; i < cvAllocInvItms.size(); i++)
						{
							laInvItms =
								(
									ProcessInventoryData) cvAllocInvItms
										.elementAt(
									i);
							if (laInvItms.getInvItmYr() > 0)
							{
								lsItmTrckngType =
									getCacheItmTrkngType(
										laInvItms.getItmCd());
								if ((lsItmTrckngType
									.equals(STICKER_TRCKNG_TYPE)
									|| lsItmTrckngType.equals(
										PLATE_TRCKNG_TYPE)))
								{
									ciExpYr = laInvItms.getInvItmYr();
									lbExpMoYrSet = true;
									break;
								}
							}
						} // end for
					} // end if

					// Assign Year where Owner Supplied Inventory 
					if (!lbExpMoYrSet)
					{
						// Check for ownersupplied for Reg Correction since it 
						// doesn't use REG029 or Inv screens to set exp yr.
						if (caVehicleInfo
							.getRegData()
							.getOwnrSuppliedExpYr()
							> 0)
						{
							ciExpYr =
								caVehicleInfo
									.getRegData()
									.getOwnrSuppliedExpYr();
						}
						// Assign Year where no new registration 
						else if (
							caVehicleInfo.getRegData().getRegExpYr()
								>= 0)
						{
							if ((caVehicleInfo
								.getRegData()
								.getRegPltNo()
								!= null
								&& !caVehicleInfo
									.getRegData()
									.getRegPltNo()
									.equals(
									"")))
							{
								ciExpYr =
									caVehicleInfo
										.getRegData()
										.getRegExpYr();
							}
						}
					}
				}
			}
		} // end try box
		catch (NullPointerException aeNPEx)
		{
			System.out.println(
				aeNPEx
					+ " error on CompleteTransactionData.buildExpirationDt()");
		}
	}
	/**
	 * Returns the value of AllocInvItms
	 * 
	 * @return Vector
	 */
	public Vector getAllocInvItms()
	{
		return cvAllocInvItms;
	}
	/**
	 * Returns the value of AnnualDieselFee
	 * 
	 * @return Dollar
	 */
	public Dollar getAnnualDieselFee()
	{
		return caAnnualDieselFee;
	}
	/**
	 * Returns the value of AnnualRegFee
	 * 
	 * @return Dollar
	 */
	public Dollar getAnnualRegFee()
	{
		return caAnnualRegFee;
	}
	/**
	 * Returns the value of BatchCount
	 * 
	 * @return String
	 */
	public String getBatchCount()
	{
		// defect 10590 
		return csBatchCount == null ? new String() : csBatchCount;
		// end defect 10590 
	}
	/**
	 * Returns the value of BsnDateTotalAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getBsnDateTotalAmt()
	{
		return caDollarBsnDateTotalAmt;
	}
	/**
	 * Returns the value of CashWsid
	 * 
	 * @return int
	 */
	public int getCashWsid()
	{
		return ciCashWsid;
	}
	/**
	 * Returns the value of CrdtRemaining
	 * 
	 * @return Dollar
	 */
	public Dollar getCrdtRemaining()
	{
		return caCrdtRemaining;
	}
	/**
	 * Returns the value of CreditCardFeeData
	 * 
	 * @return CreditCardFeeData
	 */
	public CreditCardFeeData getCreditCardFeeData()
	{
		return caCreditCardFeeData;
	}

	/**
	 * Returns the value of CurrTransNo
	 * 
	 * @return int
	 */
	public int getCurrTransNo()
	{
		return ciCurrTransNo;
	}
	/**
	 * Returns the value of CustActualRegFee
	 * 
	 * @return Dollar
	 */
	public Dollar getCustActualRegFee()
	{
		return caCustActualRegFee;
	}
	/**
	 * Returns the value of CustBaseRegFee
	 * 
	 * @return Dollar
	 */
	public Dollar getCustBaseRegFee()
	{
		return caCustBaseRegFee;
	}
	/**
	 * Returns the value of CustName
	 * 
	 * @return String
	 */
	public String getCustName()
	{
		return csCustName;
	}
	/**
	 * Returns the value of CustSubtotal
	 * 
	 * @return Dollar
	 */
	public Dollar getCustSubtotal()
	{
		return caDollarCustSubtotal;
	}
	/**
	 * Returns the value of DealerId
	 * 
	 * @return int
	 */
	public int getDealerId()
	{
		return ciDealerId;
	}
	/**
	 * Returns the value of DieselFee
	 * 
	 * @return Dollar
	 */
	public Dollar getDieselFee()
	{
		return caDieselFee;
	}
	/**
	 * Returns the value of DisableAddlFees
	 * 
	 * @return int
	 */
	public int getDisableAddlFees()
	{
		return ciDisableAddlFees;
	}
	/**
	 * Returns the value of DisableCtyFees
	 * 
	 * @return int
	 */
	public int getDisableCtyFees()
	{
		return ciDisableCtyFees;
	}

	/** 
	 * Returns the value of caDlrTtlData
	 * 
	 * @return DealerTitleData
	 */
	public DealerTitleData getDlrTtlData()
	{
		return caDlrTtlData;
	}

	/**
	 * Returns the value of DTAErrorMsg
	 * 
	 * @return String
	 */
	public String getDTAErrorMsg()
	{
		return csDTAErrorMsg;
	}

	/** 
	 * Return Populated ElectronicTitleHstry Data Object
	 * 
	 * @param aaTransHdrData
	 * @return ElectronicTitleHistoryData
	 */
	public ElectronicTitleHistoryData getETtlHstryData(
		TransactionHeaderData aaTransHdrData,
		int aiTransTime)
	{
		ElectronicTitleHistoryData laETtlHstryData = null;

		if (csTransCode.equals(TransCdConstant.VOID)
			|| csTransCode.equals(TransCdConstant.VOIDNC))
		{
			laETtlHstryData = new ElectronicTitleHistoryData();
			laETtlHstryData.setOfcIssuanceNo(ciVoidOfcIssuanceNo);
			laETtlHstryData.setSubstaId(aaTransHdrData.getSubstaId());
			laETtlHstryData.setTransWsId(ciVoidTransWsId);
			laETtlHstryData.setTransAMDate(ciVoidTransAMDate);
			laETtlHstryData.setTransTime(ciVoidTransTime);
			laETtlHstryData.setCacheTransAMDate(
				aaTransHdrData.getTransAMDate());
			laETtlHstryData.setCacheTransTime(ciTransTime);
		}
		else if (TransactionCodesCache.isETitleTransCd(csTransCode))
		{
			if (caVehicleInfo != null)
			{
				TitleData laTitleData = caVehicleInfo.getTitleData();

				if (laTitleData != null
					&& laTitleData.isETitle()
					&& laTitleData.hasLien())
				{
					laETtlHstryData = new ElectronicTitleHistoryData();
					laETtlHstryData.setOfcIssuanceNo(ciOfcIssuanceNo);
					laETtlHstryData.setSubstaId(
						aaTransHdrData.getSubstaId());
					laETtlHstryData.setTransWsId(
						aaTransHdrData.getTransWsId());
					laETtlHstryData.setTransAMDate(
						aaTransHdrData.getTransAMDate());
					laETtlHstryData.setTransTime(aiTransTime);
					// defect 10505
					laETtlHstryData.setTransId(getTransId());
					// end defect 10505 
					laETtlHstryData.setTransEmpId(
						aaTransHdrData.getTransEmpId());
					laETtlHstryData.setCustSeqNo(
						aaTransHdrData.getCustSeqNo());
					laETtlHstryData.setCacheTransAMDate(
						aaTransHdrData.getTransAMDate());
					laETtlHstryData.setCacheTransTime(
						aaTransHdrData.getTransTime());
					laETtlHstryData.setTransCd(csTransCode);

					// defect 10112 
					laETtlHstryData.setOwnrTtlName1(
						caVehicleInfo.getOwnerData().getName1());
					laETtlHstryData.setOwnrTtlName2(
						caVehicleInfo.getOwnerData().getName2());
					// end defect 10112 

					laETtlHstryData.setPermLienhldrId(
						laTitleData.getPermLienHldrId1());
					laETtlHstryData.setVIN(
						caVehicleInfo.getVehicleData().getVin());
					if (caVehicleInfo.getRegData() != null)
					{
						laETtlHstryData.setRegPltNo(getPltNo());
					}
				}
			}
		}
		return laETtlHstryData;
	}

	/**
	 * Return Populated ExemptAuditData if appropriate
	 *
	 * @return ExemptAuditData
	 */
	public ExemptAuditData getExemptAuditData(
		TransactionHeaderData aaTransHdrData,
		int aiTransTime)
	{
		ExemptAuditData laExemptAuditData = null;

		if (csTransCode.equals(TransCdConstant.VOID)
			|| csTransCode.equals(TransCdConstant.VOIDNC))
		{
			laExemptAuditData = new ExemptAuditData();
			laExemptAuditData.setOfcIssuanceNo(ciVoidOfcIssuanceNo);
			laExemptAuditData.setTransWsId(ciVoidTransWsId);
			laExemptAuditData.setTransAMDate(ciVoidTransAMDate);
			laExemptAuditData.setTransTime(ciVoidTransTime);
			laExemptAuditData.setCacheTransAMDate(
				aaTransHdrData.getTransAMDate());
			laExemptAuditData.setCacheTransTime(ciTransTime);
		}
		else if (csTransCode.equals(TransCdConstant.SBRNW))
		{
			SubcontractorRenewalData laSBRNWData =
				caSubcontractorRenewalCacheData
					.getTempSubconRenewalData();

			if (laSBRNWData
				.getRenwlTotalFees()
				.compareTo(CommonConstant.ZERO_DOLLAR)
				== 0)
			{
				laExemptAuditData =
					initExemptAuditData(aaTransHdrData, aiTransTime);

				// defect 10505 
				laExemptAuditData.setTransId(getTransId());
				// end defect 10505

				laExemptAuditData.setDocNo(laSBRNWData.getDocNo());
				laExemptAuditData.setVIN(laSBRNWData.getVIN());
				laExemptAuditData.setRegExpMo(
					laSBRNWData.getRegExpMo());
				laExemptAuditData.setRegExpYr(
					laSBRNWData.getNewExpYr());

				// RegClassCd 
				int liRegClassCd =
					Integer.parseInt(laSBRNWData.getRegClassCd());
				laExemptAuditData.setRegClassCd(liRegClassCd);

				// RegPltCd 
				String lsRegPltCd = laSBRNWData.getPltItmCd();
				laExemptAuditData.setRegPltCd(lsRegPltCd);

				// PltNo 
				laExemptAuditData.setRegPltNo(getPltNo());
				laExemptAuditData.setPriorRegPltNo(
					laSBRNWData.getRegPltNo());

				laExemptAuditData.setExmptIndi(1);

				// defect 9017 
				laExemptAuditData.setPriorExmptIndi(0);
				// end defect 9017 
			}
		}
		// Not Inventory Transactions; Not Inventory Void 
		else if (!(csTransCode.startsWith("INV")))
		{
			if ((caOrgVehicleInfo != null
				&& caOrgVehicleInfo.getRegData() != null
				&& caOrgVehicleInfo.getRegData().getExmptIndi() == 1)
				|| (caVehicleInfo != null
					&& caVehicleInfo.getRegData() != null
					&& caVehicleInfo.getRegData().getExmptIndi() == 1))
			{

				laExemptAuditData =
					initExemptAuditData(aaTransHdrData, aiTransTime);

				//	Set Prior Reg Info 
				if (caOrgVehicleInfo != null
					&& caOrgVehicleInfo.getRegData() != null)
				{
					laExemptAuditData.setPriorRegClassCd(
						caOrgVehicleInfo.getRegData().getRegClassCd());
					laExemptAuditData.setPriorRegPltCd(
						caOrgVehicleInfo.getRegData().getRegPltCd());
					laExemptAuditData.setPriorRegPltNo(
						caOrgVehicleInfo.getRegData().getRegPltNo());
					laExemptAuditData.setPriorExmptIndi(
						caOrgVehicleInfo.getRegData().getExmptIndi());
				}

				// Set Current Reg Info 
				if (caVehicleInfo != null)
				{
					if (caVehicleInfo.getRegData() != null)
					{
						laExemptAuditData.setRegClassCd(
							caVehicleInfo.getRegData().getRegClassCd());

						laExemptAuditData.setRegPltNo(getPltNo());

						laExemptAuditData.setRegPltCd(
							caVehicleInfo.getRegData().getRegPltCd());

						laExemptAuditData.setExmptIndi(
							caVehicleInfo.getRegData().getExmptIndi());

						buildExpirationDt();

						laExemptAuditData.setRegExpMo(ciExpMo);

						laExemptAuditData.setRegExpYr(ciExpYr);
					}

					// Vehicle Data
					VehicleData laNewVehicleData =
						caVehicleInfo.getVehicleData();

					if (laNewVehicleData != null)
					{
						laExemptAuditData.setVehClassCd(
							laNewVehicleData.getVehClassCd());
						laExemptAuditData.setVehMk(
							laNewVehicleData.getVehMk());
						laExemptAuditData.setVehModlYr(
							laNewVehicleData.getVehModlYr());
						laExemptAuditData.setVIN(
							laNewVehicleData.getVin());
					}

					// Title Data
					TitleData laNewTitleData =
						caVehicleInfo.getTitleData();

					if (laNewTitleData != null)
					{
						// Doc No 
						laExemptAuditData.setDocNo(
							laNewTitleData.getDocNo());

						// defect 10112 
						// LienHldr1Name 
						//if (laNewTitleData.getLienHolder1() != null)
						LienholderData laLienData =
							laNewTitleData.getLienholderData(
								TitleConstant.LIENHLDR1);

						if (laLienData.isPopulated())
						{
							laExemptAuditData.setLienHldr1Name1(
								laLienData.getName1());
						}
					}
					OwnerData laNewOwnerData =
						caVehicleInfo.getOwnerData();

					if (laNewOwnerData != null)
					{
						laExemptAuditData.setOwnrTtlName1(
							laNewOwnerData.getName1());
						laExemptAuditData.setOwnrTtlName2(
							laNewOwnerData.getName2());
					}
					// end defect 10112 
				}

				if (caRegTtlAddlInfoData != null)
				{
					laExemptAuditData.setTtlFeeIndi(
						caRegTtlAddlInfoData.getChrgTtlFeeIndi());
				}

			}
		}
		return laExemptAuditData;
	}

	/**
	 * Returns the value of ExpMo
	 * 
	 * @return int
	 */
	public int getExpMo()
	{
		return ciExpMo;
	}
	/**
	 * Returns the value of ExpYr
	 * 
	 * @return int
	 */
	public int getExpYr()
	{
		return ciExpYr;
	}
	/**
	 * Returns the value of FsclYr
	 * 
	 * @return int
	 */
	public int getFsclYr()
	{
		return ciFsclYr;
	}
	/**
	 * Returns the value of FundsUpdate
	 * 
	 * @return FundsUpdateData
	 */
	public FundsUpdateData getFundsUpdate()
	{
		return caFundsUpdate;
	}
	/**
	 * Returns the value of FxdExpMo
	 * 
	 * @return int
	 */
	public int getFxdExpMo()
	{
		return ciFxdExpMo;
	}
	/**
	 * Returns the value of InternetScreen
	 * 
	 * @return String
	 */
	public String getInternetScreen()
	{
		return csInternetScreen;
	}
	/**
	 * Return the value of InternetTransactionData 
	 * 
	 * @return InternetTransactionData
	 */
	public InternetTransactionData getInternetTransactionData()
	{
		return caInternetTransactionData;
	}
	/**
	 * Returns the value of InvFuncTrans
	 * 
	 * @return InventoryFunctionTransactionData  
	 */
	public InventoryFunctionTransactionData getInvFuncTrans()
	{
		return caInvFuncTrans;
	}
	/**
	 * Returns the value of InvItemCount
	 * 
	 * @return int
	 */
	public int getInvItemCount()
	{
		return ciInvItemCount;
	}
	/**
	 * Return the correct sticker number.
	 * pass args:Complete Transaction Data Object
	 * InvtrackingType P =Plate,	S =	Sticker Record number
	 * Note:This method gets the Registration Data base on Tracking Type.
	 * If new inventory was issued,this methods get it from
	 * processInventoryData.
	 * 
	 * @param asTrckngType String
	 * @param aiIndex int
	 * @return String
	 */
	protected String getInvItmNo(String asTrckngType, int aiIndex)
	{
		Vector lvInvItm = new Vector();
		String lsInvItmNo = "";
		String lsItmTrckngType = "";
		ProcessInventoryData laInvItms = new ProcessInventoryData();
		lvInvItm = getAllocInvItms();
		// Vector of Inventory Items
		try
		{
			if (!(lvInvItm == null || lvInvItm.size() == 0)
				&& lvInvItm.size() > aiIndex)
			{
				laInvItms =
					(ProcessInventoryData) lvInvItm.elementAt(aiIndex);
				lsItmTrckngType =
					getCacheItmTrkngType(laInvItms.getItmCd());
				if (lsItmTrckngType.equals(asTrckngType))
				{
					lsInvItmNo = laInvItms.getInvItmNo();
				}
			}
		}
		catch (NullPointerException aeNPEx)
		{
			System.out.println(
				aeNPEx
					+ " error on CompleteTransactionData.getInvItmNo()");
		}
		return lsInvItmNo;
	}
	/**
	 * Returns the value of InvItms
	 * 
	 * @return Vector
	 */
	public Vector getInvItms()
	{
		return cvInvItms;
	}
	/**
	 * Returns the value of InvItmsAfterFees
	 * 
	 * @return Vector
	 */
	public Vector getInvItmsAfterFees()
	{
		return cvInvItmsAfterFees;
	}
	/**
	 * Returns the value of MailFeeApplies
	 * 
	 * @return int
	 */
	public int getMailFeeApplies()
	{
		return ciMailFeeApplies;
	}
	/**
	 * Returns the value of MailingAddrData
	 * 
	 * @return NameAddressData
	 */
	public NameAddressData getMailingAddrData()
	{
		return caMailingAddrData;
	}
	/**
	 * Returns the value of NoChrgIndi
	 * 
	 * @return int
	 */
	public int getNoChrgIndi()
	{
		return ciNoChrgIndi;
	}
	/**
	 * Returns the value of NoMFRecs
	 * 
	 * @return int
	 */
	public int getNoMFRecs()
	{
		return ciNoMFRecs;
	}
	/**
	 * Returns the value of NoMoChrg
	 * 
	 * @return int
	 */
	public int getNoMoChrg()
	{
		return ciNoMoChrg;
	}
	/**
	 * Returns the value of NoMoCredit
	 * 
	 * @return int
	 */
	public int getNoMoCredit()
	{
		return ciNoMoCredit;
	}
	/**
	 * Returns the value of OfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getOfcIssuanceNo()
	{
		return ciOfcIssuanceNo;
	}

	/**
	 * Returns the value of OrgVehicleInfo
	 * 
	 * @return MFVehicleData
	 */
	public MFVehicleData getOrgVehicleInfo()
	{
		return caOrgVehicleInfo;
	}
	/**
	 * Returns the value of OwnrSuppliedPltNo
	 * 
	 * @return String
	 */
	public String getOwnrSuppliedPltNo()
	{
		return csOwnrSuppliedPltNo;
	}
	//	/**
	//	 * Returns the value of OwnrSuppliedStkrAge
	//	 * 
	//	 * @return int
	//	 */
	//	public int getOwnrSuppliedStkrAge()
	//	{
	//		return ciOwnrSuppliedStkrAge;
	//	}

	/**
	 * Returns the value of OwnrSuppliedStkrNo
	 * 
	 * @return String
	 */
	public String getOwnrSuppliedStkrNo()
	{
		return csOwnrSuppliedStkrNo;
	}

	/**
	 * Returns the value of PeriodOptRowsread
	 * 
	 * @return int
	 */
	public int getPeriodOptRowsread()
	{
		return ciPeriodOptRowsread;
	}

	/**
	 * Returns the value of cvPeriodOptVec
	 * 
	 * @return Vector
	 */
	public Vector getPeriodOptVec()
	{
		return cvPeriodOptVec;
	}
	/**
	 * Determine the correct plate to print.
	 * 
	 * @return String
	 */
	protected String getPltNo()
	{
		String lsRegPltNo = "";

		if (csTransCode.equals(TransCdConstant.SBRNW))
		{
			lsRegPltNo =
				caSubcontractorRenewalCacheData
					.getTempSubconRenewalData()
					.getNewPltNo();
			if (lsRegPltNo == null || lsRegPltNo.length() == 0)
			{
				lsRegPltNo =
					caSubcontractorRenewalCacheData
						.getTempSubconRenewalData()
						.getRegPltNo();
			}
		}
		else
		{
			// get the first inventory item in the inventory object
			lsRegPltNo = getInvItmNo(PLATE_TRCKNG_TYPE, FIRST_RECORD);
			// Is the first item in the Inventory object a plate number, 
			//   returns a blank if not found
			if (lsRegPltNo != null && !lsRegPltNo.equals(""))
			{
				return lsRegPltNo;
			}

			// get the second inventory item in the inventory object
			lsRegPltNo = getInvItmNo(PLATE_TRCKNG_TYPE, SECOND_RECORD);

			// Is the second item in the Inventory table a plate number,
			//   returns a blank if not found
			if (lsRegPltNo != null && !lsRegPltNo.equals(""))
			{
				return lsRegPltNo;
			}
			// get the third inventory item in the inventory object
			lsRegPltNo = getInvItmNo(PLATE_TRCKNG_TYPE, THIRD_RECORD);
			// Is the third item in the Inventory table a plate number,
			//   returns a blank if not found
			if (lsRegPltNo != null && !lsRegPltNo.equals(""))
			{
				return lsRegPltNo;
			}
			// Check for ownersupplied, else return RegPltNo
			if (getOwnrSuppliedPltNo() != null
				&& !getOwnrSuppliedPltNo().equals(""))
			{
				lsRegPltNo = getOwnrSuppliedPltNo();
			}
			else
			{
				lsRegPltNo =
					getVehicleInfo().getRegData().getRegPltNo();
			}
		}
		return lsRegPltNo == null ? "" : lsRegPltNo;
	}

	/**
	 * Returns the value of PrintOptions
	 * 
	 * @return int
	 */
	public int getPrintOptions()
	{
		return ciPrintOptions;
	}

	/**
	 * Returns the value of PrmtEffDate
	 * 
	 * @return int
	 */
	public int getPrmtEffDate()
	{
		return ciPrmtEffDate;
	}

	/**
	 * Returns the value of ReCalcIndi
	 * 
	 * @return int
	 */
	public int getReCalcIndi()
	{
		return ciReCalcIndi;
	}

	/**
	 * Returns the value of RegFeesData
	 * 
	 * @return RegFeesData
	 */
	public RegFeesData getRegFeesData()
	{
		return caRegFeesData;
	}

	/**
	 * Returns the value of RegisPenaltyFee
	 * 
	 * @return Dollar
	 */
	public Dollar getRegisPenaltyFee()
	{
		return caRegisPenaltyFee;
	}

	/**
	 * Returns the value of RegItemsData
	 * 
	 * @return RegistrationItemsData
	 */
	public RegistrationItemsData getRegItemsData()
	{
		return caRegItemsData;
	}

	/**
	 * Returns the value of RegPeriodLngth
	 * 
	 * @return int
	 */
	public int getRegPeriodLngth()
	{
		return ciRegPeriodLngth;
	}
	// end defect 8404

	/**
	 * Returns the value of RegPnltyChrgIndi
	 * 
	 * @return int
	 */
	public int getRegPnltyChrgIndi()
	{
		return ciRegPnltyChrgIndi;
	}

	/**
	 * Returns the value of RegTtlAddlInfoData
	 * 
	 * @return RegTtlAddlInfoData
	 */
	public RegTtlAddlInfoData getRegTtlAddlInfoData()
	{
		return caRegTtlAddlInfoData;
	}

	/**
	 * Returns the value of RprStkOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getRprStkOfcIssuanceNo()
	{
		return ciRprStkOfcIssuanceNo;
	}

	/**
	 * Returns the value of RprStkTransAMDate
	 * 
	 * @return int
	 */
	public int getRprStkTransAMDate()
	{
		return ciRprStkTransAMDate;
	}

	/**
	 * Returns the value of RprStkTransTime
	 * 
	 * @return int
	 */
	public int getRprStkTransTime()
	{
		return ciRprStkTransTime;
	}

	/**
	 * Returns the value of RprStkTransWsId
	 * 
	 * @return int
	 */
	public int getRprStkTransWsId()
	{
		return ciRprStkTransWsId;
	}

	/**
	 * Returns the value of SavedInvItmCount
	 * 
	 * @return int
	 */
	public int getSavedInvItmCount()
	{
		return ciSavedInvItmCount;
	}

	/**
	 * Returns the value of SavedInvItms
	 * 
	 * @return Vector
	 */
	public Vector getSavedInvItms()
	{
		return cvSavedInvItms;
	}

	/**
	 * Returns the value of SavedTransCd
	 * 
	 * @return String
	 */
	public String getSavedTransCd()
	{
		return csSavedTransCd;
	}

	/**
	 * Returns the value of SpclPlateFromMo
	 * 
	 * @return int
	 */
	public int getSpclPlateFromMo()
	{
		return ciSpclPlateFromMo;
	}

	/**
	 * Returns the value of SpclPlateFromYr
	 * 
	 * @return int
	 */
	public int getSpclPlateFromYr()
	{
		return ciSpclPlateFromYr;
	}

	/**
	 * Returns the value of SpclPlateNoMoCharge
	 * 
	 * @return int
	 */
	public int getSpclPlateNoMoCharge()
	{
		return ciSpclPlateNoMoCharge;
	}

	/**										Defect 10097
	 * Returns the value of ldSpclPlateFee
	 * 
	 * @return Dollar
	 */
	public Dollar getSpclPlateFee()
	{

		Vector lvFeesData = new Vector();

		// defect 10734 
		if (csTransCode.equals(TransCdConstant.SBRNW)
			|| csTransCode.equals(TransCdConstant.WRENEW))
		{
			// end defect 10734 
			SubcontractorRenewalData laSubconRenewalData =
				caSubcontractorRenewalCacheData
					.getTempSubconRenewalData();
			lvFeesData = laSubconRenewalData.getFeesDataTrFunds();
		}
		else
		{
			lvFeesData = caRegFeesData.getVectFees();
		}
		Dollar ldSpclPlateFee = new Dollar(0.0);

		if (lvFeesData != null)
		{
			for (int i = 0; i < lvFeesData.size(); i++)
			{
				String lsAcctCd =
					((FeesData) lvFeesData.elementAt(i)).getAcctItmCd();
				if (lsAcctCd.startsWith("SPL0")
					&& !lsAcctCd.startsWith("SPL0090"))
				{
					Dollar laItmPrice =
						(Dollar) ((FeesData) lvFeesData.elementAt(i))
							.getItemPrice();
					ldSpclPlateFee =
						ldSpclPlateFee.addNoRound(laItmPrice);
				}
			}
		}
		return ldSpclPlateFee;
	}

	/**										
	 * Returns the value of cdSpclPlatePLPFee
	 * 
	 * @return Dollar
	 */
	public Dollar getSpclPlatePLPFee()
	{
		Vector lvFeesData = new Vector();

		// defect 10734 
		if (csTransCode.equals(TransCdConstant.SBRNW)
			|| csTransCode.equals(TransCdConstant.WRENEW))
		{
			// end defect 10734 
			SubcontractorRenewalData laSubconRenewalData =
				caSubcontractorRenewalCacheData
					.getTempSubconRenewalData();
			lvFeesData = laSubconRenewalData.getFeesDataTrFunds();
		}
		else
		{
			lvFeesData = caRegFeesData.getVectFees();
		}
		Dollar ldSpclPlatePLPFee = new Dollar(0.0);
		if (lvFeesData != null)
		{
			for (int i = 0; i < lvFeesData.size(); i++)
			{
				String lsAcctCd =
					((FeesData) lvFeesData.elementAt(i)).getAcctItmCd();
				if (lsAcctCd.startsWith("SPL0090"))
				{
					Dollar laItmPrice =
						(Dollar) ((FeesData) lvFeesData.elementAt(i))
							.getItemPrice();
					ldSpclPlatePLPFee =
						ldSpclPlatePLPFee.addNoRound(laItmPrice);
				}
			}
		}

		return ldSpclPlatePLPFee;
	}
	/**
	 * Returns the value of cvStickers
	 *  
	 * @return Vector
	 */
	public Vector getStickers()
	{
		return cvStickers;
	}

	/**
	 * Returns the value of SubconId
	 * 
	 * @return int
	 */
	public int getSubconId()
	{
		return ciSubconId;
	}

	/**
	 * Returns the value of SubConRenwlBarCdIndi
	 * 
	 * @return int
	 */
	public int getSubConRenwlBarCdIndi()
	{
		return ciSubConRenwlBarCdIndi;
	}

	/**
	 * Returns the value of SubconRenwlData
	 * 
	 * @return SubcontractorRenewalData
	 */
	public SubcontractorRenewalData getSubconRenwlData()
	{
		return caSubconRenwlData;
	}

	/**
	 * Returns the value of SubConRenwlTotalFee
	 * 
	 * @return Dollar
	 */
	public Dollar getSubConRenwlTotalFee()
	{
		return caSubConRenwlTotalFee;
	}

	/**
	 * Returns the value of SubcontractorRenewalCacheData
	 * 
	 * @return SubcontractorRenewalCacheData
	 */
	public SubcontractorRenewalCacheData getSubcontractorRenewalCacheData()
	{
		return caSubcontractorRenewalCacheData;
	}

	/**
	 * Returns the value of TempVehGrossWt
	 * 
	 * @return int
	 */
	public int getTempVehGrossWt()
	{
		return ciTempVehGrossWt;
	}

	/**
	 * Returns the value of TimedPermitData
	 * 
	 * @return TimedPermitData
	 */
	public TimedPermitData getTimedPermitData()
	{
		return caTimedPermitData;
	}

	/**
	 * Returns the value of TransactionDate
	 * 
	 * @return int
	 */
	public int getTransactionDate()
	{
		return ciTransactionDate;
	}

	/**
	 * Returns the value of TransAMDate
	 * 
	 * @return int
	 */
	public int getTransAMDate()
	{
		return ciTransAMDate;
	}

	/**
	 * Returns the value of TransCode
	 * 
	 * @return String
	 */
	public String getTransCode()
	{
		return csTransCode;
	}

	/**
	 * Returns the value of TransGregorianDt
	 * 
	 * @return int
	 */
	public int getTransGregorianDt()
	{
		return ciTransGregorianDt;
	}
	/**
	 * Returns the value of TransId
	 * 
	 * @return String
	 */
	public String getTransId()
	{
		// defect 10590 
		return csTransId == null ? new String() : csTransId;
		// end defect 10590 
	}

	/**
	 * Returns the value of cvTransInvDetail
	 * 
	 * @return Vector
	 */
	public Vector getTransInvDetail()
	{
		return cvTransInvDetail;
	}

	/**
	 * Returns the value of TransTime
	 * 
	 * @return int
	 */
	public int getTransTime()
	{
		return ciTransTime;
	}

	/**
	 * Returns the value of TxtRegMoSold
	 * 
	 * @return String
	 */
	public String getTxtRegMoSold()
	{
		return csTxtRegMoSold;
	}

	/**
	 * Returns the value of TxtSpclPltMoSold
	 * Added defect 9126.
	 * 
	 * @return String
	 */
	public String getTxtSpclPltMoSold()
	{
		return csTxtSpclPltMoSold;
	}

	/**
	 * Returns the value of VehicleInfo
	 * 
	 * @return MFVehicleData
	 */
	public MFVehicleData getVehicleInfo()
	{
		return caVehicleInfo;
	}

	/**
	 * Returns the value of VehMisc
	 * 
	 * @return VehMiscData
	 */
	public VehMiscData getVehMisc()
	{
		return caVehMisc;
	}

	/**
	 * Returns the value of VehSalesTaxAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getVehSalesTaxAmt()
	{
		return caVehSalesTaxAmt;
	}

	/**
	 * Returns the value of VehTaxAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getVehTaxAmt()
	{
		return caVehTaxAmt;
	}
	/**
	 * Returns the value of VehTaxPnlty
	 * 
	 * @return Dollar
	 */
	public Dollar getVehTaxPnlty()
	{
		return caVehTaxPnlty;
	}

	/**
	 * Returns the value of VehTotalSalesTaxPd
	 * 
	 * @return Dollar
	 */
	public Dollar getVehTotalSalesTaxPd()
	{
		return caVehTotalSalesTaxPd;
	}

	/**
	 * Returns the value of VoidAllIndi
	 * 
	 * @return int
	 */
	public int getVoidAllIndi()
	{
		return ciVoidAllIndi;
	}

	/**
	 * Returns the value of VoidCustSeqNo
	 * 
	 * @return int
	 */
	public int getVoidCustSeqNo()
	{
		return ciVoidCustSeqNo;
	}

	/**
	 * Returns the value of VoidedTransIndi
	 * 
	 * @return int
	 */
	public int getVoidedTransIndi()
	{
		return ciVoidedTransIndi;
	}

	/**
	 * Returns the value of VoidOfcIssuanceNo
	 * 
	 * @return int
	 */
	public int getVoidOfcIssuanceNo()
	{
		return ciVoidOfcIssuanceNo;
	}

	/**
	 * Returns the value of VoidPymntAmt
	 * 
	 * @return Dollar
	 */
	public Dollar getVoidPymntAmt()
	{
		return caVoidPymntAmt;
	}

	/**
	 * Returns the value of VoidSubstaId
	 * 
	 * @return int
	 */
	public int getVoidSubstaId()
	{
		return ciVoidSubstaId;
	}

	/**
	 * Returns the value of VoidTransactionHeaderData
	 * 
	 * @return TransactionHeaderData
	 */
	public TransactionHeaderData getVoidTransactionHeaderData()
	{
		return caVoidTransactionHeaderData;
	}

	/**
	 * Returns the value of VoidTransAMDate
	 * 
	 * @return int
	 */
	public int getVoidTransAMDate()
	{
		return ciVoidTransAMDate;
	}

	/**
	 * Returns the value of VoidTransCd
	 * 
	 * @return String
	 */
	public String getVoidTransCd()
	{
		return csVoidTransCd;
	}

	/**
	 * Returns the value of VoidTransName
	 * 
	 * @return String
	 */
	public String getVoidTransName()
	{
		return csVoidTransName;
	}

	/**
	 * Returns the value of VoidTransTime
	 * 
	 * @return int
	 */
	public int getVoidTransTime()
	{
		return ciVoidTransTime;
	}

	/**
	 * Returns the value of VoidTransWsId
	 * 
	 * @return int
	 */
	public int getVoidTransWsId()
	{
		return ciVoidTransWsId;
	}

	/**
	 * Returns the value of WebAgent User Name
	 * 
	 * @return int
	 */
	public String getWebAgntUserName()
	{
		return csWebAgntUserName;
	}
	/**
	 * Initialize ExemptAuditData
	 *
	 * @return boolean
	 */
	private ExemptAuditData initExemptAuditData(
		TransactionHeaderData aaTransHdr,
		int aiTransTime)
	{
		ExemptAuditData laExemptAuditData = new ExemptAuditData();
		laExemptAuditData.setOfcIssuanceNo(
			aaTransHdr.getOfcIssuanceNo());
		laExemptAuditData.setSubstaId(aaTransHdr.getSubstaId());
		laExemptAuditData.setTransAMDate(aaTransHdr.getTransAMDate());
		laExemptAuditData.setTransWsId(aaTransHdr.getTransWsId());
		laExemptAuditData.setTransTime(aiTransTime);
		laExemptAuditData.setTransEmpId(aaTransHdr.getTransEmpId());
		laExemptAuditData.setCustSeqNo(aaTransHdr.getCustSeqNo());
		laExemptAuditData.setTransCd(csTransCode);
		laExemptAuditData.setCacheTransAMDate(
			aaTransHdr.getTransAMDate());
		laExemptAuditData.setCacheTransAMDate(aiTransTime);
		// defect 9017 
		if (caVehicleInfo != null && !caVehicleInfo.isFromMF())
		{
			laExemptAuditData.setMfDwnCd(1);
		}
		// end defect 9017 
		return laExemptAuditData;
	}

	/**
	 * Return boolean to determine if Preview Receipt
	 * 
	 * @return boolean 
	 */
	public boolean isPreviewReceipt()
	{
		return cbPreviewReceipt;
	}
	
	/**
	 * @return the cbWRENEWSubconAddr
	 */
	public boolean isWRENEWSubconAddr()
	{
		return cbWRENEWSubconAddr;
	}

	/**
	 * Sets the value of AllocInvItms
	 * 
	 * @param avAllocInvItms Vector
	 */
	public void setAllocInvItms(Vector avAllocInvItms)
	{
		cvAllocInvItms = avAllocInvItms;
	}
	/**
	 * Sets the value of AnnualDieselFee
	 * 
	 * @param aaAnnualDieselFee Dollar
	 */
	public void setAnnualDieselFee(Dollar aaAnnualDieselFee)
	{
		caAnnualDieselFee = aaAnnualDieselFee;
	}
	/**
	 * Sets the value of AnnualRegFee
	 * 
	 * @param aaAnnualRegFee Dollar
	 */
	public void setAnnualRegFee(Dollar aaAnnualRegFee)
	{
		caAnnualRegFee = aaAnnualRegFee;
	}
	/**
	 * Sets the value of BatchCount
	 * 
	 * @param asBatchCount String
	 */
	public void setBatchCount(String asBatchCount)
	{
		csBatchCount = asBatchCount;
	}
	/**
	 * Sets the value of BsnDateTotalAmt
	 * 
	 * @param aaBsnDateTotalAmt Dollar
	 */
	public void setBsnDateTotalAmt(Dollar aaBsnDateTotalAmt)
	{
		caDollarBsnDateTotalAmt = aaBsnDateTotalAmt;
	}
	/**
	 * Sets the value of CashWsid
	 * 
	 * @param aCashWsid int
	 */
	public void setCashWsid(int aCashWsid)
	{
		ciCashWsid = aCashWsid;
	}
	/**
	 * Sets the value of CrdtRemaining
	 * 
	 * @param aaCrdtRemaining Dollar
	 */
	public void setCrdtRemaining(Dollar aaCrdtRemaining)
	{
		caCrdtRemaining = aaCrdtRemaining;
	}
	/**
	 * Sets the value of CreditCardFeeData
	 * 
	 * @param aaCreditCardFeeData CreditCardFeeData
	 */
	public void setCreditCardFeeData(CreditCardFeeData aaCreditCardFeeData)
	{
		caCreditCardFeeData = aaCreditCardFeeData;
	}

	/**
	 * Sets the value of CurrTransNo
	 * 
	 * @param aiCurrTransNo int
	 */
	public void setCurrTransNo(int aiCurrTransNo)
	{
		ciCurrTransNo = aiCurrTransNo;
	}

	/**
	 * Sets the value of CustActualRegFee
	 * 
	 * @param aaCustActualRegFee Dollar
	 */
	public void setCustActualRegFee(Dollar aaCustActualRegFee)
	{
		caCustActualRegFee = aaCustActualRegFee;
	}
	/**
	 * Sets the value of CustBaseRegFee
	 * 
	 * @param aaCustBaseRegFee Dollar
	 */
	public void setCustBaseRegFee(Dollar aaCustBaseRegFee)
	{
		caCustBaseRegFee = aaCustBaseRegFee;
	}
	/**
	 * Sets the value of CustName
	 * 
	 * @param asCustName String
	 */
	public void setCustName(String asCustName)
	{
		csCustName = asCustName;
	}
	/**
	 * Sets the value of CustSubtotal
	 * 
	 * @param aaCustSubtotal Dollar
	 */
	public void setCustSubtotal(Dollar aaCustSubtotal)
	{
		caDollarCustSubtotal = aaCustSubtotal;
	}
	/**
	 * Sets the value of DealerId
	 * 
	 * @param aiDealerId int
	 */
	public void setDealerId(int aiDealerId)
	{
		ciDealerId = aiDealerId;
	}
	/**
	 * Sets the value of DieselFee
	 * 
	 * @param aaDieselFee Dollar
	 */
	public void setDieselFee(Dollar aaDieselFee)
	{
		caDieselFee = aaDieselFee;
	}
	/**
	 * Sets the value of DisableAddlFees
	 * 
	 * @param aiDisableAddlFees int
	 */
	public void setDisableAddlFees(int aiDisableAddlFees)
	{
		ciDisableAddlFees = aiDisableAddlFees;
	}
	/**
	 * Sets the value of DisableCtyFees
	 * 
	 * @param aiDisableCtyFees int
	 */
	public void setDisableCtyFees(int aiDisableCtyFees)
	{
		ciDisableCtyFees = aiDisableCtyFees;
	}
	//	/**
	//	 * Sets the value of DlrTtlDataObjs
	//	 * 
	//	 * @param avDlrTtlDataObjs Vector
	//	 */
	//	public void setDlrTtlDataObjs(Vector avDlrTtlDataObjs)
	//	{
	//		cvDlrTtlDataObjs = avDlrTtlDataObjs;
	//	}
	/** 
	 * Sets the value of caDlrTtlData
	 * 
	 * @return DealerTitleData
	 */
	public void setDlrTtlData(DealerTitleData aaDlrTtlData)
	{
		caDlrTtlData = aaDlrTtlData;
	}
	/**
	 * Sets the value of DTAErrorMsg
	 * 
	 * @param asDTAErrorMsg String
	 */
	public void setDTAErrorMsg(String asDTAErrorMsg)
	{
		csDTAErrorMsg = asDTAErrorMsg;
	}
	/**
	 * Sets the value of ExpMo
	 * 
	 * @param aiExpMo int
	 */
	public void setExpMo(int aiExpMo)
	{
		ciExpMo = aiExpMo;
	}
	/**
	 * Sets the value of ExpYr
	 * 
	 * @param aiExpYr int
	 */
	public void setExpYr(int aiExpYr)
	{
		ciExpYr = aiExpYr;
	}
	/**
	 * Sets the value of FsclYr
	 * 
	 * @param aiFsclYr int
	 */
	public void setFsclYr(int aiFsclYr)
	{
		ciFsclYr = aiFsclYr;
	}
	/**
	 * Sets the value of FundsUpdate
	 * 
	 * @param aaFundsUpdate FundsUpdateData
	 */
	public void setFundsUpdate(FundsUpdateData aaFundsUpdate)
	{
		caFundsUpdate = aaFundsUpdate;
	}
	/**
	 * Sets the value of FxdExpMo
	 * 
	 * @param aiFxdExpMo int
	 */
	public void setFxdExpMo(int aiFxdExpMo)
	{
		ciFxdExpMo = aiFxdExpMo;
	}
	/**
	 * Sets the value of InternetScreen
	 * 
	 * @param asInternetScreen String
	 */
	public void setInternetScreen(String asInternetScreen)
	{
		csInternetScreen = asInternetScreen;
	}
	/**
	 * Set the InternetTransactionData object.
	 * 
	 * @param aaInternetTransData InternetTransactionData
	 */
	public void setInternetTransData(InternetTransactionData aaInternetTransData)
	{
		caInternetTransactionData = aaInternetTransData;
	}
	/**
	 * Sets the value of InvFuncTrans
	 * 
	 * @param aiInvFuncTrans InventoryFunctionTransactionData
	 */
	public void setInvFuncTrans(InventoryFunctionTransactionData aaInvFuncTrans)
	{
		caInvFuncTrans = aaInvFuncTrans;
	}
	/**
	 * Sets the value of InvItemCount
	 * 
	 * @param aiInvItemCount int
	 */
	public void setInvItemCount(int aiInvItemCount)
	{
		ciInvItemCount = aiInvItemCount;
	}
	/**
	 * Sets the value of InvItms
	 * 
	 * @param avInvItms Vector
	 */
	public void setInvItms(Vector avInvItms)
	{
		cvInvItms = avInvItms;
	}
	/**
	 * Sets the value of InvItmsAfterFees
	 * 
	 * @param avInvItmsAfterFees Vector
	 */
	public void setInvItmsAfterFees(Vector avInvItmsAfterFees)
	{
		cvInvItmsAfterFees = avInvItmsAfterFees;
	}
	/**
	 * Sets the value of MailFeeApplies
	 *
	 * @param aiMailFeeApplies int
	 */
	public void setMailFeeApplies(int aiMailFeeApplies)
	{
		ciMailFeeApplies = aiMailFeeApplies;
	}

	/**
	 * Sets the value of MailingAddrData
	 * 
	 * @param aaMailingAddrData NameAddressData
	 */
	public void setMailingAddrData(NameAddressData aaMailingAddrData)
	{
		caMailingAddrData = aaMailingAddrData;
	}

	/**
	 * Sets the value of NoChrgIndi
	 *  
	 * @param aiNoChrgIndi int
	 */
	public void setNoChrgIndi(int aiNoChrgIndi)
	{
		ciNoChrgIndi = aiNoChrgIndi;
	}

	/**
	 * Sets the value of NoMFRecs
	 * 
	 * @param aiNoMFRecs int
	 */
	public void setNoMFRecs(int aiNoMFRecs)
	{
		ciNoMFRecs = aiNoMFRecs;
	}
	/**
	 * Sets the value of NoMoChrg
	 * 
	 * @param aiNoMoChrg int
	 */
	public void setNoMoChrg(int aiNoMoChrg)
	{
		ciNoMoChrg = aiNoMoChrg;
	}
	/**
	 * Sets the value of NoMoCredit
	 *
	 * @param aiNoMoCredit int
	 */
	public void setNoMoCredit(int aiNoMoCredit)
	{
		ciNoMoCredit = aiNoMoCredit;
	}
	/**
	 * Sets the value of OfcIssuanceNo
	 * 
	 * @param aiOfcIssuanceNo int
	 */
	public void setOfcIssuanceNo(int aiOfcIssuanceNo)
	{
		ciOfcIssuanceNo = aiOfcIssuanceNo;
	}
	/**
	 * Sets the value of OrgVehicleInfo
	 * 
	 * @param aiOldVehicleInfo MFVehicleData
	 */
	public void setOrgVehicleInfo(MFVehicleData aaOrgVehicleInfo)
	{
		caOrgVehicleInfo = aaOrgVehicleInfo;
	}
	/**
	 * Sets the value of OwnrSuppliedPltNo
	 * 
	 * @param asOwnrSuppliedPltNo String
	 */
	public void setOwnrSuppliedPltNo(String asOwnrSuppliedPltNo)
	{
		csOwnrSuppliedPltNo = asOwnrSuppliedPltNo;
	}

	/**
	 * Sets the value of PeriodOptRowsread
	 * 
	 * @param aiPeriodOptRowsread int
	 */
	public void setPeriodOptRowsread(int aiPeriodOptRowsread)
	{
		ciPeriodOptRowsread = aiPeriodOptRowsread;
	}
	/**
	 * Sets the value of PeriodOptVec
	 * 
	 * @param avPeriodOptVec Vector
	 */
	public void setPeriodOptVec(Vector avPeriodOptVec)
	{
		cvPeriodOptVec = avPeriodOptVec;
	}

	/**
	 * Set value of cbPreviewReceipt 
	 * 
	 * @param abPreviewReceipt
	 */
	public void setPreviewReceipt(boolean abPreviewReceipt)
	{
		cbPreviewReceipt = abPreviewReceipt;
	}

	/**
	 * Sets the value of PrintOptions
	 * 
	 * @param aiPrintOptions int
	 */
	public void setPrintOptions(int aiPrintOptions)
	{
		ciPrintOptions = aiPrintOptions;
	}
	/**
	 * Sets the value of PrmtEffDate
	 * 
	 * @param aiPrmtEffDate int
	 */
	public void setPrmtEffDate(int aiPrmtEffDate)
	{
		ciPrmtEffDate = aiPrmtEffDate;
	}
	/**
	 * Sets the value of ReCalcIndi
	 * 
	 * @param aiReCalcIndi int
	 */
	public void setReCalcIndi(int aiReCalcIndi)
	{
		ciReCalcIndi = aiReCalcIndi;
	}
	/**
	 * Sets the value of RegFeesData
	 * 
	 * @param aaRegFeesData RegFeesData
	 */
	public void setRegFeesData(RegFeesData aaRegFeesData)
	{
		caRegFeesData = aaRegFeesData;
	}
	/**
	 * Sets the value of RegisPenaltyFee
	 * 
	 * @param aaRegisPenaltyFee Dollar
	 */
	public void setRegisPenaltyFee(Dollar aaRegisPenaltyFee)
	{
		caRegisPenaltyFee = aaRegisPenaltyFee;
	}
	/**
	 * Sets the value of RegItemsData
	 * 
	 * @param aaRegItemsData RegistrationItemsData
	 */
	public void setRegItemsData(RegistrationItemsData aaRegItemsData)
	{
		caRegItemsData = aaRegItemsData;
	}
	/**
	 * Sets the value of RegPeriodLngth
	 * 
	 * @param aiRegPeriodLngth int
	 */
	public void setRegPeriodLngth(int aiRegPeriodLngth)
	{
		ciRegPeriodLngth = aiRegPeriodLngth;
	}
	/**
	 * Sets the value of RegPnltyChrgIndi
	 * 
	 * @param aiRegPnltyChrgIndi int
	 */
	public void setRegPnltyChrgIndi(int aiRegPnltyChrgIndi)
	{
		ciRegPnltyChrgIndi = aiRegPnltyChrgIndi;
	}
	/**
	 * Sets the value of RegTtlAddlInfoData
	 * 
	 * @param aiRegTtlAddlInfoData RegTtlAddlInfoData
	 */
	public void setRegTtlAddlInfoData(RegTtlAddlInfoData aiRegTtlAddlInfoData)
	{
		caRegTtlAddlInfoData = aiRegTtlAddlInfoData;
	}
	/**
	 * Method to set the value of RprStkOfcIssaunceNo
	 * 
	 * @param aiRprStkOfcIssuanceNo int
	 */
	public void setRprStkOfcIssuanceNo(int aiRprStkOfcIssuanceNo)
	{
		ciRprStkOfcIssuanceNo = aiRprStkOfcIssuanceNo;
	}
	/**
	 * Method to set the value of RprStkTransAMDate
	 * 
	 * @param aiRprStkTransAMDate int
	 */
	public void setRprStkTransAMDate(int aiRprStkTransAMDate)
	{
		ciRprStkTransAMDate = aiRprStkTransAMDate;
	}
	/**
	 * Method to set the value of RprStkTransTime
	 * 
	 * @param aiRprStkTransTime int
	 */
	public void setRprStkTransTime(int aiRprStkTransTime)
	{
		ciRprStkTransTime = aiRprStkTransTime;
	}
	/**
	 * Method to set the value of RprStkTransWsId
	 * 
	 * @param aiRprStkTransWsId int
	 */
	public void setRprStkTransWsId(int aiRprStkTransWsId)
	{
		ciRprStkTransWsId = aiRprStkTransWsId;
	}
	/**
	 * Sets the value of SavedInvItmCount
	 * 
	 * @param aiSavedInvItmCount int
	 */
	public void setSavedInvItmCount(int aiSavedInvItmCount)
	{
		ciSavedInvItmCount = aiSavedInvItmCount;
	}
	/**
	 * Sets the value of SavedInvItms
	 * 
	 * @param avSavedInvItms Vector
	 */
	public void setSavedInvItms(Vector avSavedInvItms)
	{
		cvSavedInvItms = avSavedInvItms;
	}
	/**
	 * Sets the value of SavedTransCd
	 * 
	 * @param asSavedTransCd String
	 */
	public void setSavedTransCd(String asSavedTransCd)
	{
		csSavedTransCd = asSavedTransCd;
	}

	/**
	 * Sets the value of SpclPlateFromMo
	 * 
	 * @param aiSpclPlateFromMo int
	 */
	public void setSpclPlateFromMo(int aiSpclPlateFromMo)
	{
		ciSpclPlateFromMo = aiSpclPlateFromMo;
	}

	/**
	 * Sets the value of SpclPlateFromYr
	 * 
	 * @param aiSpclPlateFromYr int
	 */
	public void setSpclPlateFromYr(int aiSpclPlateFromYr)
	{
		ciSpclPlateFromYr = aiSpclPlateFromYr;
	}

	/**
	 * Sets the value of SpclPlateNoMoCharge
	 * 
	 * @param aiSpclPlateNoMoCharge int
	 */
	public void setSpclPlateNoMoCharge(int aiSpclPlateNoMoCharge)
	{
		ciSpclPlateNoMoCharge = aiSpclPlateNoMoCharge;
	}

	/**
	 * Sets the value of Stickers
	 * 
	 * @param avStickers Vector
	 */
	public void setStickers(Vector avStickers)
	{
		cvStickers = avStickers;
	}
	/**
	 * Sets the value of SubconId
	 * 
	 * @param aiSubconId int
	 */
	public void setSubconId(int aiSubconId)
	{
		ciSubconId = aiSubconId;
	}
	/**
	 * Sets the value of SubConRenwlBarCdIndi
	 * 
	 * @param aiSubConRenwlBarCdIndi int
	 */
	public void setSubConRenwlBarCdIndi(int aiSubConRenwlBarCdIndi)
	{
		ciSubConRenwlBarCdIndi = aiSubConRenwlBarCdIndi;
	}
	/**
	 * Sets the value of SubconRenwlData
	 * 
	 * @param aaSubconRenwlData SubcontractorRenewalData
	 */
	public void setSubconRenwlData(SubcontractorRenewalData aaSubconRenwlData)
	{
		caSubconRenwlData = aaSubconRenwlData;
	}
	/**
	 * Sets the value of SubConRenwlTotalFee
	 * 
	 * @param aaSubConRenwlTotalFee Dollar
	 */
	public void setSubConRenwlTotalFee(Dollar aaSubConRenwlTotalFee)
	{
		caSubConRenwlTotalFee = aaSubConRenwlTotalFee;
	}
	/**
	 * Sets the value of SubcontractorRenewalCacheData
	 * 
	 * @param aaSubcontractorRenewalCacheData SubcontractorReaialCacheData
	 */
	public void setSubcontractorRenewalCacheData(SubcontractorRenewalCacheData aaSubcontractorRenewalCacheData)
	{
		caSubcontractorRenewalCacheData =
			aaSubcontractorRenewalCacheData;
	}
	/**
	 * Sets the value of TempVehGrossWt
	 * 
	 * @param aiTempVehGrossWt int
	 */
	public void setTempVehGrossWt(int aiTempVehGrossWt)
	{
		ciTempVehGrossWt = aiTempVehGrossWt;
	}
	/**
	 * Sets the value of TimedPermitData
	 * 
	 * @param aaTimedPermitData TimedPermitData
	 */
	public void setTimedPermitData(TimedPermitData aaTimedPermitData)
	{
		caTimedPermitData = aaTimedPermitData;
	}
	/**
	 * Sets the value of TransactionDate
	 *  
	 * @param aiTransactionDate int
	 */
	public void setTransactionDate(int aiTransactionDate)
	{
		ciTransactionDate = aiTransactionDate;
	}
	/**
	 * Sets the value of TransAMDate
	 * 
	 * @param aiTransAMDate int
	 */
	public void setTransAMDate(int aiTransAMDate)
	{
		ciTransAMDate = aiTransAMDate;
	}
	/**
	 * Sets the value of TransCode
	 * 
	 * @param aiTransCode String
	 */
	public void setTransCode(String aiTransCode)
	{
		csTransCode = aiTransCode;
	}
	/**
	 * Sets the value of TransGregorianDt
	 * 
	 * @param aiTransGregorianDt int
	 */
	public void setTransGregorianDt(int aiTransGregorianDt)
	{
		ciTransGregorianDt = aiTransGregorianDt;
	}
	/**
	 * Sets the value of TransId
	 * 
	 * @param aiTransId String
	 */
	public void setTransId(String asTransId)
	{
		csTransId = asTransId;
	}
	/**
	 * Sets the value of TransInvDetail
	 * 
	 * @param avTransInvDetail Vector
	 */
	public void setTransInvDetail(Vector avTransInvDetail)
	{
		cvTransInvDetail = avTransInvDetail;
	}
	/**
	 * Sets the value of TransTime
	 *  
	 * @param aiTransTime int
	 */
	public void setTransTime(int aiTransTime)
	{
		ciTransTime = aiTransTime;
	}

	/**
	 * Sets the value of TxtRegMoSold
	 * 
	 * @param aiTxtRegMoSold String
	 */
	public void setTxtRegMoSold(String asTxtRegMoSold)
	{
		csTxtRegMoSold = asTxtRegMoSold;
	}

	/**
	 * Sets the value of TxtSpclPltMoSold
	 * Added defect 9126.
	 * 
	 * @param aiTxtSpclPltMoSold String
	 */
	public void setTxtSpclPltMoSold(String asTxtSpclPltMoSold)
	{
		csTxtSpclPltMoSold = asTxtSpclPltMoSold;
	}
	/**
	 * 
	 * Setup for IAPPL
	 * 
	 * @param aaSpclPltItrntData SpecialPlateItrntTransData
	 */
	public void setupForIAPPL(SpecialPlateItrntTransData aaSpclPltItrntData)
	{
		// Create Special Plates Regis Data Object from 
		//  SpecialPlateItrntTransData 
		SpecialPlatesRegisData laSpclPltRegisData =
			new SpecialPlatesRegisData();

		// PltApplDate
		RTSDate laItrntPltApplDate =
			aaSpclPltItrntData.getUpdtTimeStmp();

		laSpclPltRegisData.setPltApplDate(
			laItrntPltApplDate.getYYYYMMDDDate());

		// RegPltCd 
		laSpclPltRegisData.setRegPltCd(
			aaSpclPltItrntData.getRegPltCd());

		// RegPltNo & MfgPltNo 
		laSpclPltRegisData.setRegPltNo(
			aaSpclPltItrntData.getRegPltNo());
		laSpclPltRegisData.setMfgPltNo(
			aaSpclPltItrntData.getMfgPltNo());

		// OrgNo
		laSpclPltRegisData.setOrgNo(aaSpclPltItrntData.getOrgNo());

		// AddlSetIndi 
		laSpclPltRegisData.setAddlSetIndi(
			aaSpclPltItrntData.isAddlSetIndi());

		// ISAIndi
		laSpclPltRegisData.setISAIndi(aaSpclPltItrntData.isISAIndi());

		// Exp Mo / Yr
		// defect 9359 
		// Calculate Exp Mo/Yr based upon current date 
		int liMonth = new RTSDate().getMonth();
		int liYear = new RTSDate().getYear();

		// defect 9508
		// switch the below statements so when liMonth is 1,
		// the liYear is the current year, not current year + 1  
		liYear = liMonth == 1 ? liYear : liYear + 1;
		liMonth = liMonth == 1 ? 12 : liMonth - 1;
		//liYear = liMonth == 1 ? liYear : liYear +1;
		// end defect 9508

		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
		laSpclPltRegisData.setPltExpMo(liMonth);
		laSpclPltRegisData.setPltExpYr(liYear);
		// end defect 9864 
		// end defect 9359 

		// OwnrData 
		laSpclPltRegisData.setOwnrData(
			aaSpclPltItrntData.getOwnerData());

		// Phone 
		laSpclPltRegisData.setPltOwnrPhoneNo(
			aaSpclPltItrntData.getPltOwnrPhone());

		// EMail 
		laSpclPltRegisData.setPltOwnrEMail(
			aaSpclPltItrntData.getPltOwnrEmail());

		// MfgStatusCd
		laSpclPltRegisData.setMFGStatusCd(
			SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD);

		// RequestType 
		laSpclPltRegisData.setRequestType(
			SpecialPlatesConstant.MANUFACTURE);

		// ItrntTraceNo 
		laSpclPltRegisData.setItrntTraceNo(
			aaSpclPltItrntData.getItrntTraceNo());

		// ResComptCntyNo 
		laSpclPltRegisData.setResComptCntyNo(
			aaSpclPltItrntData.getResComptCntyNo());

		// MfgDate 
		// defect 9359 
		// Use current date for starting date
		// vs. date of application request on Internet   
		laSpclPltRegisData.setMFGDate(TransCdConstant.IAPPL);
		//, laItrntPltApplDate);
		// end defect 9359 

		// RegEffDate 
		laSpclPltRegisData.setRegEffDate(
			(new RTSDate()).getYYYYMMDDDate());

		// SpclPltChrgFeeIndi 
		laSpclPltRegisData.setSpclPltChrgFeeIndi(1);
		// defect 10957
		laSpclPltRegisData.setPltValidityTerm(1);
		// end defect 10957

		// Create/Assign to MFVehicleData 
		MFVehicleData laMFVehicleData = new MFVehicleData();
		laMFVehicleData.setSpclPltRegisData(laSpclPltRegisData);
		laMFVehicleData.setOwnerData(laSpclPltRegisData.getOwnrData());

		// Create/Assign to Registration Data   
		RegistrationData laRegData = new RegistrationData();
		laRegData.setResComptCntyNo(
			laSpclPltRegisData.getResComptCntyNo());
		laRegData.setRegPltNo(laSpclPltRegisData.getRegPltNo());
		laRegData.setRegPltCd(laSpclPltRegisData.getRegPltCd());

		// Assign RegData to MFVehicleData 
		laMFVehicleData.setRegData(laRegData);

		// Assign MFVehicleData for CompleteTransData   
		setVehicleInfo(laMFVehicleData);
		RegFeesData laRegFeesData = new RegFeesData();
		laRegFeesData.setVectFees(aaSpclPltItrntData.getFeesData());
		laRegFeesData.setToYearDflt(laSpclPltRegisData.getInvItmYr());

		// Assign Registration Fees
		setRegFeesData(laRegFeesData);

		// Assign TransCd 
		setTransCode(TransCdConstant.IAPPL);
	}

	/**
	 * Setup for VENDOR
	 * 
	 * @param aaSpclPltItrntData SpecialPlateItrntTransData
	 */
	public void setupForVENDOR(SpecialPlateItrntTransData aaSpclPltItrntData)
	{
		// Create Special Plates Regis Data Object from 
		//  SpecialPlateItrntTransData 
		SpecialPlatesRegisData laSpclPltRegisData =
			new SpecialPlatesRegisData();

		// PltApplDate
		RTSDate laItrntPltApplDate =
			aaSpclPltItrntData.getUpdtTimeStmp();

		// WILL APPLICATION DATE BE TODAY FOR VENDOR PLATES?
		laSpclPltRegisData.setPltApplDate(
			laItrntPltApplDate.getYYYYMMDDDate());

		// RegPltCd 
		laSpclPltRegisData.setRegPltCd(
			aaSpclPltItrntData.getRegPltCd());

		// RegPltNo & MfgPltNo 
		laSpclPltRegisData.setRegPltNo(
			aaSpclPltItrntData.getRegPltNo());
		laSpclPltRegisData.setMfgPltNo(
			aaSpclPltItrntData.getMfgPltNo());

		// OrgNo
		laSpclPltRegisData.setOrgNo(aaSpclPltItrntData.getOrgNo());

		// AddlSetIndi 
		laSpclPltRegisData.setAddlSetIndi(
			aaSpclPltItrntData.isAddlSetIndi());

		// ISAIndi
		laSpclPltRegisData.setISAIndi(aaSpclPltItrntData.isISAIndi());

		// Exp Mo / Yr
		// Expiration Mo/Yr will be passed, along with term (num of 
		// years), reserve indi, and transempid)

		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
		laSpclPltRegisData.setPltExpMo(
			aaSpclPltItrntData.getPltExpMo());
		laSpclPltRegisData.setPltExpYr(
			aaSpclPltItrntData.getPltExpYr());
		// end defect 9864 

		// defect 9711
		laSpclPltRegisData.setTransEmpId(
			aaSpclPltItrntData.getTransEmpID());
		// end defect 9711

		// OwnrData 
		laSpclPltRegisData.setOwnrData(
			aaSpclPltItrntData.getOwnerData());

		// Phone 
		laSpclPltRegisData.setPltOwnrPhoneNo(
			aaSpclPltItrntData.getPltOwnrPhone());

		// EMail 
		laSpclPltRegisData.setPltOwnrEMail(
			aaSpclPltItrntData.getPltOwnrEmail());

		// defect 10401
		// group all manufactur stuff together.
		// Not all trans require a "M"
		if (aaSpclPltItrntData
			.getTransCd()
			.equals(TransCdConstant.VPDEL)
			|| aaSpclPltItrntData.getTransCd().equals(
				TransCdConstant.VPREV))
		{
			// delete and revise do not require a manufacture.  
			// set to blank.
			laSpclPltRegisData.setMFGStatusCd(
				CommonConstant.STR_SPACE_EMPTY);
			laSpclPltRegisData.setRequestType(
				CommonConstant.STR_SPACE_EMPTY);
			laSpclPltRegisData.setMFGDate(0);
		}
		else if (
			aaSpclPltItrntData.getTransCd().equals(
				TransCdConstant.VPUNAC))
		{
			// unacceptable requires a "N"
			laSpclPltRegisData.setMFGStatusCd(
				SpecialPlatesConstant.UNACCEPTABLE_MFGSTATUSCD);
			laSpclPltRegisData.setRequestType(
				SpecialPlatesConstant.UNACCEPTABLE);
			laSpclPltRegisData.setMFGDate(0);
		}
		else if (
			aaSpclPltItrntData.getTransCd().equals(
				TransCdConstant.VPRSRV))
		{
			// Reserve is an "R"
			laSpclPltRegisData.setMFGStatusCd(
				SpecialPlatesConstant.RESERVE_MFGSTATUSCD);
			laSpclPltRegisData.setRequestType(
				SpecialPlatesConstant.FROM_RESERVE);
			laSpclPltRegisData.setMFGDate(0);
		}
		else
		{
			// Otherwise, manufacture.
			// MfgStatusCd
			laSpclPltRegisData.setMFGStatusCd(
				SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD);

			// RequestType 
			laSpclPltRegisData.setRequestType(
				SpecialPlatesConstant.MANUFACTURE);

			// new transcd's VPAPPL and VPAPPR
			if (aaSpclPltItrntData.getTransCd() != null
				&& aaSpclPltItrntData.getTransCd().length() > 0)
			{
				// use the TransCd passed if populated.
				laSpclPltRegisData.setMFGDate(
					aaSpclPltItrntData.getTransCd());
			}
			else if (aaSpclPltItrntData.getFromReserveIndi() == 1)
			{
				laSpclPltRegisData.setMFGDate(TransCdConstant.VPAPPR);
			}
			else
			{
				laSpclPltRegisData.setMFGDate(TransCdConstant.VPAPPL);
			}
		}
		// end defect 10401

		// ItrntTraceNo 
		laSpclPltRegisData.setItrntTraceNo(
			aaSpclPltItrntData.getItrntTraceNo());

		// ResComptCntyNo 
		laSpclPltRegisData.setResComptCntyNo(
			aaSpclPltItrntData.getResComptCntyNo());

		// MfgDate 
		// Use current date for starting date
		// vs. date of application request on Internet  

		// defect 10401
		// move up to only set the date when appropriate.
		//		// new transcd's VPAPPL and VPAPPR
		//		// defect 10401
		//		if (aaSpclPltItrntData.getTransCd() != null
		//			&& aaSpclPltItrntData.getTransCd().length() > 0)
		//		{
		//			// use the TransCd passed if populated.
		//			laSpclPltRegisData.setMFGDate(aaSpclPltItrntData.getTransCd());
		//		}
		//		else if (aaSpclPltItrntData.getFromReserveIndi() == 1)
		//		{
		//			// end defect 10401
		//			laSpclPltRegisData.setMFGDate(TransCdConstant.VPAPPR);
		//		}
		//		else
		//		{
		//			laSpclPltRegisData.setMFGDate(TransCdConstant.VPAPPL);
		//		}
		// end defect 10401

		// RegEffDate 
		laSpclPltRegisData.setRegEffDate(
			(new RTSDate()).getYYYYMMDDDate());

		// SpclPltChrgFeeIndi 
		// TODO need to review for accuracy!
		laSpclPltRegisData.setSpclPltChrgFeeIndi(1);

		// defect 10366 
		// Use PltValidityTerm vs. PltTerm in SpecialPlateItrntTransData 
		//laSpclPltRegisData.setPltValidityTerm(aaSpclPltItrntData.getPltTerm());
		laSpclPltRegisData.setPltValidityTerm(
			aaSpclPltItrntData.getPltValidityTerm());
		laSpclPltRegisData.setMktngAllowdIndi(
			aaSpclPltItrntData.getMktngAllowdIndi());
		laSpclPltRegisData.setAuctnPltIndi(
			aaSpclPltItrntData.getAuctnPltIndi());
		laSpclPltRegisData.setResrvReasnCd(
			aaSpclPltItrntData.getResrvReasnCd());
		laSpclPltRegisData.setSpclRegId(
			aaSpclPltItrntData.getSpclRegId());

		if (aaSpclPltItrntData.getAuctnPltIndi() == 1)
		{
			Vector lvFees = aaSpclPltItrntData.getFeesData();
			Dollar laAuctnAmt = new Dollar(0);
			if (lvFees != null && lvFees.size() > 0)
			{
				for (int i = 0; i < lvFees.size(); i++)
				{
					Object laObj = lvFees.elementAt(i);

					if (laObj instanceof FeesData)
					{
						FeesData laFeesData = (FeesData) laObj;
						laAuctnAmt =
							laAuctnAmt.add(laFeesData.getItemPrice());
					}
				}
			}
			laSpclPltRegisData.setAuctnPdAmt(laAuctnAmt);
		}
		// end defect 10366

		// Create/Assign to MFVehicleData 
		MFVehicleData laMFVehicleData = new MFVehicleData();
		laMFVehicleData.setSpclPltRegisData(laSpclPltRegisData);
		laMFVehicleData.setOwnerData(laSpclPltRegisData.getOwnrData());

		// Create/Assign to Registration Data   
		RegistrationData laRegData = new RegistrationData();
		laRegData.setResComptCntyNo(
			laSpclPltRegisData.getResComptCntyNo());
		laRegData.setRegPltNo(laSpclPltRegisData.getRegPltNo());
		laRegData.setRegPltCd(laSpclPltRegisData.getRegPltCd());

		// Assign RegData to MFVehicleData 
		laMFVehicleData.setRegData(laRegData);

		//Assign MFVehicleData for CompleteTransData   
		setVehicleInfo(laMFVehicleData);

		// FEES may be set differently for the VENDOR

		// fees will need to be faked out til ready!!!

		//		if (aaSpclPltItrntData.getFeesData().size()==0)
		//		{
		//			Vector lvFeesData = new Vector();
		//			FeesData laFees = new FeesData();
		//			laFees.setItmQty(1);
		//			laFees.setItemPrice(new Dollar(300.00));
		//			laFees.setAcctItmCd("SPL0090C");
		//			lvFeesData.add(laFees);
		//			aaSpclPltItrntData.setFeesData(lvFeesData);
		//		}

		RegFeesData laRegFeesData = new RegFeesData();
		laRegFeesData.setVectFees(aaSpclPltItrntData.getFeesData());

		// Assign Registration Fees
		setRegFeesData(laRegFeesData);

		// Assign TransCd
		// defect 10401
		if (aaSpclPltItrntData.getTransCd() != null
			&& aaSpclPltItrntData.getTransCd().length() > 0)
		{
			// use the TransCd passed if populated.
			setTransCode(aaSpclPltItrntData.getTransCd());
		}
		else if (aaSpclPltItrntData.getFromReserveIndi() == 1)
		{
			// end defect 10401
			setTransCode(TransCdConstant.VPAPPR);
		}
		else
		{
			setTransCode(TransCdConstant.VPAPPL);
		}

		// defect 10401
		if (laRegFeesData != null
			&& laRegFeesData.getVectFees() != null
			&& laRegFeesData.getVectFees().size() > 0)
		{
			// TODO
			// The real problem is we are not getting CompleteTransData
			// to Transaction.  That is the cause of the null pointer.
			ciSpclPlateNoMoCharge =
				aaSpclPltItrntData.getPltValidityTerm() * 12;
		}
		// end defect 10401
	}

	/**
	 * Sets the value of VehicleInfo
	 * 
	 * @param aaVehicleInfo MFVehicleData
	 */
	public void setVehicleInfo(MFVehicleData aaVehicleInfo)
	{
		caVehicleInfo = aaVehicleInfo;
	}
	/**
	 * Sets the value of VehMisc
	 * 
	 * @param aaVehMisc VehMiscData
	 */
	public void setVehMisc(VehMiscData aaVehMisc)
	{
		caVehMisc = aaVehMisc;
	}
	/**
	 * Sets the value of VehSalesTaxAmt
	 * 
	 * @param aaVehSalesTaxAmt Dollar
	 */
	public void setVehSalesTaxAmt(Dollar aaVehSalesTaxAmt)
	{
		caVehSalesTaxAmt = aaVehSalesTaxAmt;
	}
	/**
	 * Sets the value of VehTaxAmt
	 * 
	 * @param aaVehTaxAmt Dollar
	 */
	public void setVehTaxAmt(Dollar aaVehTaxAmt)
	{
		caVehTaxAmt = aaVehTaxAmt;
	}
	/**
	 * Sets the value of VehTaxPnlty
	 * 
	 * @param aaVehTaxPnlty Dollar
	 */
	public void setVehTaxPnlty(Dollar aaVehTaxPnlty)
	{
		caVehTaxPnlty = aaVehTaxPnlty;
	}
	/**
	 * Sets the value of VehTotalSalesTaxPd
	 * 
	 * @param aaVehTotalSalesTaxPd Dollar
	 */
	public void setVehTotalSalesTaxPd(Dollar aaVehTotalSalesTaxPd)
	{
		caVehTotalSalesTaxPd = aaVehTotalSalesTaxPd;
	}
	/**
	 * Sets the value of VoidAllIndi
	 * 
	 * @param aiVoidAllIndi int
	 */
	public void setVoidAllIndi(int aiVoidAllIndi)
	{
		ciVoidAllIndi = aiVoidAllIndi;
	}
	/**
	 * Sets the value of VoidCustSeqNo
	 * 
	 * @param aiVoidCustSeqNo int
	 */
	public void setVoidCustSeqNo(int aiVoidCustSeqNo)
	{
		ciVoidCustSeqNo = aiVoidCustSeqNo;
	}
	/**
	 * Sets the value of VoidedTransIndi
	 * 
	 * @param aiVoidedTransIndi int
	 */
	public void setVoidedTransIndi(int aiVoidedTransIndi)
	{
		ciVoidedTransIndi = aiVoidedTransIndi;
	}
	/**
	 * Sets the value of VoidOfcIssuanceNo
	 * 
	 * @param aiVoidOfcIssuanceNo int
	 */
	public void setVoidOfcIssuanceNo(int aiVoidOfcIssuanceNo)
	{
		ciVoidOfcIssuanceNo = aiVoidOfcIssuanceNo;
	}
	/**
	 * Sets the value of VoidPymntAmt
	 * 
	 * @param aaVoidPymntAmt Dollar
	 */
	public void setVoidPymntAmt(Dollar aaVoidPymntAmt)
	{
		caVoidPymntAmt = aaVoidPymntAmt;
	}
	/**
	 * Sets the value of VoidSubstaId
	 * 
	 * @param aiVoidSubstaId int
	 */
	public void setVoidSubstaId(int aiVoidSubstaId)
	{
		ciVoidSubstaId = aiVoidSubstaId;
	}
	/**
	 * Sets the value of VoidTransactionHeaderData
	 * 
	 * @param aiVoidTransactionHeaderData TransactionHeaderData
	 */
	public void setVoidTransactionHeaderData(TransactionHeaderData aiVoidTransactionHeaderData)
	{
		caVoidTransactionHeaderData = aiVoidTransactionHeaderData;
	}
	/**
	 * Sets the value of VoidTransAMDate
	 * 
	 * @param aiVoidTransAMDate int
	 */
	public void setVoidTransAMDate(int aiVoidTransAMDate)
	{
		ciVoidTransAMDate = aiVoidTransAMDate;
	}
	/**
	 * Sets the value of VoidTransCd
	 * 
	 * @param asVoidTransCd String
	 */
	public void setVoidTransCd(String asVoidTransCd)
	{
		csVoidTransCd = asVoidTransCd;
	}
	/**
	 * Sets the value of VoidTransName
	 * 
	 * @param asVoidTransName String
	 */
	public void setVoidTransName(String asVoidTransName)
	{
		csVoidTransName = asVoidTransName;
	}
	/**
	 * Sets the value of VoidTransTime
	 * 
	 * @param aiVoidTransTime int
	 */
	public void setVoidTransTime(int aiVoidTransTime)
	{
		ciVoidTransTime = aiVoidTransTime;
	}
	/**
	 * Sets the value of VoidTransWsId
	 * 
	 * @param aiVoidTransWsId int
	 */
	public void setVoidTransWsId(int aiVoidTransWsId)
	{
		ciVoidTransWsId = aiVoidTransWsId;
	}

	/**
	 * Is Special Plate Permit to be printed
	 * 
	 * @return boolean
	 */
	public boolean isPrntSpclPltPrmt()
	{
		return !UtilityMethods.isSPAPPL(getTransCode())
			&& getVehicleInfo() != null
			&& getVehicleInfo().getSpclPltRegisData() != null
			&& getVehicleInfo().getSpclPltRegisData().isPrntPrmt();
	}

	/**
	 * Return new SpecialPlatePermitData 
	 * 
	 * @param aaTransHdrData
	 */
	public void setSpclPltPrmtData(
		TransactionHeaderData aaTransHdrData,
		int aiTransTime)
	{
		caSpclPltPrmtData = new SpecialPlatePermitData();

		caSpclPltPrmtData.setOfcIssuanceNo(
			aaTransHdrData.getOfcIssuanceNo());
		caSpclPltPrmtData.setSubstaId(aaTransHdrData.getSubstaId());
		caSpclPltPrmtData.setTransAMDate(
			aaTransHdrData.getTransAMDate());
		caSpclPltPrmtData.setTransWsId(aaTransHdrData.getTransWsId());
		caSpclPltPrmtData.setCustSeqNo(aaTransHdrData.getCustSeqNo());
		caSpclPltPrmtData.setTransTime(aiTransTime);

		MFVehicleData laMfVehData = getVehicleInfo();
		SpecialPlatesRegisData laSpclPltRegisData =
			(SpecialPlatesRegisData) laMfVehData.getSpclPltRegisData();

		// REGPLTCD 
		caSpclPltPrmtData.setRegPltCd(laSpclPltRegisData.getRegPltCd());

		// REGPLTNO  			
		caSpclPltPrmtData.setRegPltNo(laSpclPltRegisData.getRegPltNo());

		// EFFDATE 
		int liEffDate = laSpclPltRegisData.getRegEffDate();
		if (liEffDate == 0)
		{
			liEffDate = new RTSDate().getYYYYMMDDDate();
		}
		caSpclPltPrmtData.setEffDate(liEffDate);

		// EXPDATE 
		RTSDate laExpDate = new RTSDate(RTSDate.YYYYMMDD, liEffDate);
		laExpDate = laExpDate.add(RTSDate.DATE, 60);
		int liExpDate = laExpDate.getYYYYMMDDDate();
		caSpclPltPrmtData.setExpDate(liExpDate);

		VehicleData laVeh = laMfVehData.getVehicleData();

		// VIN 
		caSpclPltPrmtData.setVIN(laVeh.getVin());

		// VEHMODLYR
		caSpclPltPrmtData.setVehModlYr(laVeh.getVehModlYr());

		// VEHMK 
		caSpclPltPrmtData.setVehMk(laVeh.getVehMk());

		// TRANSID 
		caSpclPltPrmtData.setTransId(getTransId());
	}

	/**
	 * Get value of caSpclPltPrmtData
	 * 
	 * @return SpecialPlatePermitData
	 */
	public SpecialPlatePermitData getSpclPltPrmtData()
	{
		return caSpclPltPrmtData;
	}

	/**
	 * Set value of caSpclPltPrmtData
	 * 
	 * @param aaSpclPltPrmtData
	 */
	public void setSpclPltPrmtData(SpecialPlatePermitData aaSpclPltPrmtData)
	{
		caSpclPltPrmtData = aaSpclPltPrmtData;
	}

	/**
	 * @param abWRENEWSubconAddr 
	 */
	public void setWRENEWSubconAddr(boolean abWRENEWSubconAddr)
	{
		cbWRENEWSubconAddr = abWRENEWSubconAddr;
	}
	
	/**
	 * Set value of csWebAgntUserName
	 *
	 * @param asWebAgntUserName
	 */
	public void setWebAgntUserName(String asWebAgntUserName)
	{
		csWebAgntUserName = asWebAgntUserName;
	}
}
