package com.txdot.isd.rts.client.specialplates.business;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * SpecialPlatesClientUtilityMethods.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/26/2007	Created
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/08/2007	added calcFees(), determineMfgPltNo()
 * 							defect 9085 Ver Special Plates  
 * J Rue		03/13/2007	Add calcFees() using 1 parameter VehInqData
 * 							Add TransCd check for applIndi setting = 1
 * 							add calcFees()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/23/2007	Move saveData() from 
 * 							SpecialPlatesClientBusiness
 * 							add saveSPData()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/26/2006	Add check for SpclPltsRegisData = null
 * 							modify saveSPData()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/30/2007	Create an VehInqData object with all the
 * 							associated objects
 * 							add createVehInqDataObj()
 * 							defect 9086 Ver Special Plates
 * K Harrell/	04/19/2007	Working....
 * J Rue					Change FAILURE_VERIFICATION to 
 * 							FAILURE_MESSAGE
 * 							modify verifyRecordApplicable(),
 * 							modify verifyMfgStatusCd()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/15/2007	add verifyAvailableSunsetPlate()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/17/2007	working on messages
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/18/2007	Throw RTSException where ResComptCntyNo != 
 * 							requesting office if County
 * 							add verifyResComptCntyNoOnSPAPPLReserved()
 * 							modify verifyRecordApplicable()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/31/2007	Save SAuditTrailTransId from Reserved
 * 							modify saveSPData() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/01/2007	Use UtilityMethods.isSPAPPL()
 * 							add convertTransCdForDisplay()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/04/2007	LP not available for SPRNW in HQ
 * 							modify verifyRecordApplicable()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/12/2007	Save SpecRegId for Reserved 
 * 							modify saveSPData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/13/2007	Catch the specification of a new 
 * 							plate in Title/Reg where sunsetted.
 * 							modify verifyRecordApplicable()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/19/2007	Undo 05/17/2007 addition
 * 							delete verifyResComptCntyNoOnSPAPPLReserved()
 * 							modify verifyRecordApplicable()  
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/19/2007	add reflectorization fee for DLRPLP, DLRPLPMC
 * 							modify calcFees 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/21/2007	Additional work on Customer Supplied
 * 							modify saveSPData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/01/2007	New requirement for PLPDLR, PLPDLRMC 
 * 							plates: PlateSticker (US) fee of $0.00.
 * 							Moved Inventory Processing here for PLPDLRxx
 * 							add addlDlrPltProcessing()
 * 							modify calcFees()
 * 							defect 9085 Ver Special Plates
 * K Harrell    07/08/2007	Do not grant HQ authorization to every plate
 * 							Use "Registration" authorization for SPRNW
 * 							modify verifyPlateTypeDefinedAndAvailable()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/10/2007	Always prorate PLP fees, i.e. acctitmcd 
 * 							starts with "SPL0090" 
 * 							modify calcFees() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/17/2007	Reject Expired Annual Plate on SPL002 if 
 * 							expired. 
 * 							modify verifyRecordApplicable()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/18/2007	New fee rules
 * 							modify calcFees()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/25/2007	Reverse 7/17 changes
 * 							modify verifyRecordApplicable()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/08/2007	Use Trans Code Description vs. TransCd in 
 * 							Err Msgs 
 * 							add getTransCdDesc()
 * 							modify verifyAvailableSunsetPlate(),
 * 							  verifyMfgStatusCd(), verifyRecordApplicable()
 * 							defect 9236 Ver Special Plates
 * K Harrell	08/08/2007	Special Plate LP Handling  
 * 							Verify not same year iff same plate type
 * 							modify verifyRecordApplicable()
 * 							defect 9238 Ver Special Plates
 * K Harrell	08/13/2007	Set laRegFeesData.setToYearDflt to correctly
 * 							display InvItmYr for Dlr Plate Stkr   
 * 							delete convertTransCdForDisplay()
 * 							modify calcFees()
 * 							defect 9247 Ver Special Plates
 * K Harrell	08/15/2007	Add Sticker on Remanufacture of PLPDLRxx
 * 							add addDlrStkrInv()
 * 							modify addlDlrPltProcessing(),
 * 								calcFees()  
 * 							defect 9251 Ver Special Plates
 * K Harrell	08/28/2007	Changes per walkthru. Cleanup JavaDoc.	
 * 							Throw exception when transcd not found
 * 							modify getTransCdDesc()
 * 							defect 9236 Ver Special Plates
 * K Harrell	10/14/2007	Allow for exchange to current sunsetted
 * 							plate. 
 * 							modify verifyAvailableSunsetPlate()
 * 							defect 9351 Ver Special Plates 2
 * K Harrell	10/22/2007	Do not present INV001 for Issue Inventory
 * 							 even if not found.  
 * 							modify calcFees(), setupInvAlloc()   	
 * 							defect 9386 Ver Special Plates 2
 * K Harrell	10/28/2007	Remove check for Customer Supplied as no 
 * 							longer call method for SPAPPC. Validate 
 * 							year on Plate Owner change.  
 * 							modify saveSPData(), verifyRecordApplicable() 
 * 							defect 9396 Ver Special Plates 2 
 * K Harrell	01/21/2008	No InvItmYr on PLPDLRxx plates in SPREV
 * 							modify calcFees()
 * 							defect 9482 Ver Tres Amigos Prep
 * K Harrell	06/04/2008	Do not throw exception when in the SPREV 
 * 							when invalid OrgNo
 * 							modify verifyOrgNoValid()
 * 							defect 9699 Ver Defect POS A 
 * B Hargrove	07/15/2008	Cannot Renew (SPRNW) a Vendor Plate. Cannot
 * 							Title or Renew with expired Vendor Plate.
 * 							modify verifyRecordApplicable()
 * 							defect 9689 Ver MyPlates_POS
 * K Harrell	01/07/2009 Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify calcFees(), verifyAvailableSunsetPlate(),
 * 							verifyRecordApplicable() 
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	12/20/2009	add isValidPltExpYr()
 * 							defect 10295 Ver Defect_POS_H
 * B Hargrove	03/08/2010  Vendor Plate uses Plate Validity Term. Modify 
 *        					look-up to Plate Surcharge. Allow expired VP
 * 							to be titled or renewed.
 *        					modify calcFees(), verifyRecordApplicable() 
 *        					defect 10357 Ver 6.4.0
 * K Harrell	04/09/2010  Default RegPltCd to PLP for SPRSRV, SPUNAC
 * 							modify createVehInqData() 
 * 							defect 10441 Ver POS_640 
 * K Harrell	03/25/2011	Do not add Reflectorization to Fees if 0
 * 							modify addDlrPltProcessing() 
 * 							defect 10776 Ver 6.7.1  
 * B Hargrove	09/30/2011  We need to use 'new RegEffDt' in table lookups.  
 *        					modify calcFees() 
 *        					defect 10948 Ver 6.9.0
 * --------------------------------------------------------------------- 
 */

/**
 * Special Plates Utility Methods
 *
 * @version	6.9.0  			09/30/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		02/26/2007 12:23:00
 */
public class SpecialPlatesClientUtilityMethods
{

	/**
	 * SpecialPlatesClientUtilityMethods constructor.
	 */
	public SpecialPlatesClientUtilityMethods()
	{
		super();
	}

	/**
	 * Return TransCd Description without "RECEIPT" 
	 * 
	 * @param asTransCd
	 * @return String 
	 * @throws RTSException 
	 */
	private static String getTransCdDesc(String asTransCd)
		throws RTSException
	{
		TransactionCodesData laTransCdData =
			TransactionCodesCache.getTransCd(asTransCd);
		String lsTransCdDesc = laTransCdData.getTransCdDesc();
		int liReceiptPos = lsTransCdDesc.indexOf("RECEIPT");
		if (liReceiptPos > 0)
		{
			lsTransCdDesc = lsTransCdDesc.substring(0, liReceiptPos);
		}
		return lsTransCdDesc.trim();
	}

	/**
	 * Add Dealer Sticker Inventory  (PLPDLRxx)
	 * 
	 * @param aaCTData
	 * @param asRegStkrCd 
	 */
	private static void addDlrStkrInv(
		CompleteTransactionData aaCTData,
		String asRegStkrCd)
	{
		Vector lvStkrInv = new Vector();
		ProcessInventoryData laProcInvData = new ProcessInventoryData();
		if (asRegStkrCd != null && asRegStkrCd.length() != 0)
		{
			laProcInvData.setItmCd(asRegStkrCd);
			lvStkrInv.add(laProcInvData);
			aaCTData.setStickers(lvStkrInv);
			aaCTData.setInvItms(
				(Vector) UtilityMethods.copy(lvStkrInv));
			aaCTData.setInvItemCount(1);
		}
	}

	/**
	 * Calculate Additional DlrPlt Fees
	 *   - Reflectorization 
	 *   - Plate Sticker of 0.00 
	 * 
	 * @param aaSpclPltRegisData
	 * @param avFees
	 * @param aiDate 
	 */
	private static void addlDlrPltProcessing(
		CompleteTransactionData aaCTData,
		Vector avFees,
		int aiDate)
	{
		SpecialPlatesRegisData laSpclPltRegisData =
			aaCTData.getVehicleInfo().getSpclPltRegisData();

		// Reflectorization Fees for DLR Plates
		int liDLRPLPRegClassCd =
			ClassToPlateCache.getPLPDLRRegClassCd(
				laSpclPltRegisData.getRegPltCd());

		// Reflectorization Fee
		CommonFeesData laCommonFeesData =
			CommonFeesCache.getCommonFee(liDLRPLPRegClassCd, aiDate);

		// defect 10776 
		// Do not show Reflectorization Fee if 0.00 in CommonFees
		if (!laCommonFeesData.getReflectnFee().equals(new Dollar(0.0)))
		{
			// end defect 10776 
			
			FeesData laFeesData = new FeesData();
			String lsAcctItmCd = "REFLECT";
			AccountCodesData laAcctCdData =
				AccountCodesCache.getAcctCd(lsAcctItmCd, aiDate);
			laFeesData.setAcctItmCd(lsAcctItmCd);
			laFeesData.setCrdtAllowedIndi(
				laAcctCdData.getCrdtAllowdIndi());
			laFeesData.setDesc(laAcctCdData.getAcctItmCdDesc());
			if (laSpclPltRegisData.getSpclPltChrgFeeIndi() == 1)
			{
				laFeesData.setItemPrice(laCommonFeesData.getReflectnFee());
			}
			else
			{
				laFeesData.setItemPrice(new Dollar(0.0));
			}
			laFeesData.setItmQty(1);
			avFees.add(laFeesData);
			
			// defect 10776
		}
		// end defect 10776

		// AcctItmCd (Plate Sticker) - Fee of $0.00 
		Vector lvPltToStkrData =
			PlateToStickerCache.getPltToStkrs(
				laSpclPltRegisData.getRegPltCd(),
				aiDate);
		if (lvPltToStkrData != null && lvPltToStkrData.size() != 0)
		{
			PlateToStickerData laPltToStkrData =
				(PlateToStickerData) lvPltToStkrData.elementAt(0);
			FeesData laFeesData2 = new FeesData();
			String lsAcctItmCd = laPltToStkrData.getAcctItmCd();
			if (lsAcctItmCd != null)
			{
				AccountCodesData laAcctCdData =
					AccountCodesCache.getAcctCd(lsAcctItmCd, aiDate);
				if (laAcctCdData != null)
				{
					laFeesData2.setAcctItmCd(lsAcctItmCd);
					laFeesData2.setCrdtAllowedIndi(
						laAcctCdData.getCrdtAllowdIndi());
					laFeesData2.setDesc(
						laAcctCdData.getAcctItmCdDesc());
					laFeesData2.setItemPrice(new Dollar(0.0));
					laFeesData2.setItmQty(1);
					avFees.add(laFeesData2);
				}
			}
			// defect 9251 
			// Moved logic to addDlrStkrInv() 
			// Add Sticker to Sticker Inventory
			addDlrStkrInv(aaCTData, laPltToStkrData.getRegStkrCd());
			// end defect 9251 
		}
	}

	/**
	 * Calculate Fees for Special Plates 
	 * 	- Application
	 *  - Renew Plate Only 
	 *  
	 * @param aaMFVehData	MFVehicleData
	 * @param asTransCd 	String 
	 * @return CompleteTransactionData
	 */
	public static CompleteTransactionData calcFees(
		MFVehicleData aaMFVehData,
		String asTransCd)
	{
		CompleteTransactionData laCTData =
			new CompleteTransactionData();

		laCTData.setVehicleInfo(aaMFVehData);
		SpecialPlatesRegisData laSpclPltRegisData =
			aaMFVehData.getSpclPltRegisData();
		boolean lbDlrPlt =
			ClassToPlateCache.getPLPDLRRegClassCd(
				laSpclPltRegisData.getRegPltCd())
				!= 0;
		int liToday = new RTSDate().getYYYYMMDDDate();
		String lsRegPltCd = laSpclPltRegisData.getRegPltCd();
		String lsOrgNo = laSpclPltRegisData.getOrgNo();
		String lsRequestType = laSpclPltRegisData.getRequestType();

		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  

		laCTData.setExpMo(laSpclPltRegisData.getPltExpMo());
		laCTData.setExpYr(laSpclPltRegisData.getPltExpYr());
		laCTData.setInvItemCount(0);
		Vector lvInvItms = null;

		// defect 10948
		// For Spcl Plt Renew, set RTSEffDate to PltExpDt + 1
		// Use this for all table look-ups.
		
		int liRTSEffDT = liToday;
		if (asTransCd.equals(TransCdConstant.SPRNW))
		{
			if (laSpclPltRegisData.getOrigPltExpMo() != 12)
			{
				liRTSEffDT =
				(laSpclPltRegisData.getOrigPltExpYr() * 10000)
				+ ((laSpclPltRegisData.getOrigPltExpMo() + 1) * 100)
				 + 1;
			}
			else
			{
				liRTSEffDT =
					((laSpclPltRegisData.getOrigPltExpYr() + 1) * 10000)
					+ 100
					+ 1;
				}
			}
			// end hijack

		// defect 9386/9482 
		// Moved to beginning so that valid for SPREV/SPRNW as well
		RegFeesData laRegFeesData = new RegFeesData();
		laCTData.setRegFeesData(laRegFeesData);
		laRegFeesData.setToYearDflt(laSpclPltRegisData.getPltExpYr());
		laRegFeesData.setToMonthDflt(laSpclPltRegisData.getPltExpMo());
		// end defect 9386/9482

		// end defect 9864 

		if (lsRequestType
			.equals(SpecialPlatesConstant.ISSUE_FROM_INVENTORY))
		{
			lvInvItms = new Vector();
			ProcessInventoryData laInvData = new ProcessInventoryData();
			laInvData.setTransEmpId(SystemProperty.getCurrentEmpId());
			laInvData.setSubstaId(SystemProperty.getSubStationId());
			laInvData.setTransWsId(
				Integer.toString(SystemProperty.getWorkStationId()));
			laInvData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laInvData.setItmCd(laSpclPltRegisData.getRegPltCd());
			laInvData.setInvItmYr(laSpclPltRegisData.getInvItmYr());

			// defect 9386
			// Set data for server to return even if not found  
			laInvData.setInvLocIdCd("U");
			// end defect 9386
			lvInvItms.add(laInvData);
			laCTData.setInvItms(lvInvItms);
			laCTData.setInvItemCount(lvInvItms.size());
		}
		if (!asTransCd.equals(TransCdConstant.SPREV)
			&& !asTransCd.equals(TransCdConstant.SPRNR))
		{
			int liAddlSetIndi = laSpclPltRegisData.getAddlSetIndi();
			int liApplIndi =
				asTransCd.equals(TransCdConstant.SPRNW) ? 0 : 1;
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(lsRegPltCd);
			// defect 9482 
			// Moved earlier in method  
			// RegFeesData laRegFeesData = new RegFeesData();
			// defect 9386 
			// Set ToMonthDflt so that shows up on INV007
			//laRegFeesData.setToMonthDflt(
			//	laSpclPltRegisData.getRegExpMo());
			// end defect 9386  
			// end defect 9482 
			Vector lvFees = new Vector();
			Vector lvPltSurcharge = new Vector();
			// defect 10357
			// If Vendor Plate, look-up with Plate Validity Term
			int liPltValidityTerm = 1;
			if (PlateTypeCache.isVendorPlate(lsRegPltCd))
			{
				liPltValidityTerm =
					laSpclPltRegisData.getPltValidityTerm();
			}
			lvPltSurcharge =
				PlateSurchargeCache.getPltSurcharge(
					lsRegPltCd,
					lsOrgNo,
					liAddlSetIndi,
					liApplIndi,
					// defect 10948
					//liToday,
					liRTSEffDT,
					// end defect 10948
					liPltValidityTerm);
			// end defect 10357					
			for (int i = 0; i < lvPltSurcharge.size(); i++)
			{
				PlateSurchargeData laPltSurchrgData =
					(PlateSurchargeData) lvPltSurcharge.elementAt(i);
				FeesData laFeesData = new FeesData();
				String lsAcctItmCd = laPltSurchrgData.getAcctItmCd();
				AccountCodesData laAcctCdData =
					// defect 10948
					//AccountCodesCache.getAcctCd(lsAcctItmCd, liToday);
					AccountCodesCache.getAcctCd(lsAcctItmCd, liRTSEffDT);
					// end defect 10948
				laFeesData.setAcctItmCd(lsAcctItmCd);
				laFeesData.setCrdtAllowedIndi(
					laAcctCdData.getCrdtAllowdIndi());
				laFeesData.setDesc(laAcctCdData.getAcctItmCdDesc());

				int liNoMonthsToCharge =
					laSpclPltRegisData.getNoMonthsToCharge();

				// defect 10357
				// Do not change months to charge for Vendor Plt
				if (!PlateTypeCache.isVendorPlate(lsRegPltCd))
				{
					if (lbDlrPlt)
					{
						liNoMonthsToCharge = 12;
					}
					else if (
						laPltTypeData.getSpclPrortnIncrmnt() == 12
							&& !lsAcctItmCd.startsWith(
								SpecialPlatesConstant
									.PLP_ACCTITMCD_PREFIX))
					{
						if (liNoMonthsToCharge <= 12)
						{
							liNoMonthsToCharge = 12;
						}
						else
						{
							liNoMonthsToCharge = 24;
						}
					}
				}
				// end defect 10357
				if (laSpclPltRegisData.getSpclPltChrgFeeIndi() == 1)
				{
					laFeesData.setItemPrice(
						(laPltSurchrgData
							.getPltSurchargeFee()
							.multiplyNoRound(
								new Dollar(liNoMonthsToCharge)))
							.divideNoRound(new Dollar(12.0))
							.round());
				}
				else
				{
					laFeesData.setItemPrice(new Dollar(0.0));
				}
				laFeesData.setItmQty(1);
				lvFees.add(laFeesData);
			}
			if (lbDlrPlt)
			{
				// defect 10948
				//addlDlrPltProcessing(laCTData, lvFees, liToday);
				addlDlrPltProcessing(laCTData, lvFees, liRTSEffDT);
				// defect 10948
			}
			laRegFeesData.setVectFees(lvFees);
			// defect 9482 
			// Moved earlier in method 
			// defect 9247 
			// setToYearDflt to correctly assign year for printing
			//  Sticker info on Receipt 
			//laRegFeesData.setToYearDflt(
			//laSpclPltRegisData.getRegExpYr());
			// end defect 9247 
			//laCTData.setRegFeesData(laRegFeesData);
			// defect 9482 
		}
		// defect 9251 
		// Print Sticker on Remanufacture of Dealer Plt 
		// SPREV, SPRNR 
		else if (
			lbDlrPlt
				&& laSpclPltRegisData.getRequestType().equals(
					SpecialPlatesConstant.MANUFACTURE))
		{
			Vector lvPltToStkrData =
				PlateToStickerCache.getPltToStkrs(
					laSpclPltRegisData.getRegPltCd(),
					// defect 10948
					//liToday);
					liRTSEffDT);
					// end defect 10948
			if (lvPltToStkrData != null && lvPltToStkrData.size() != 0)
			{
				PlateToStickerData laPltToStkrData =
					(PlateToStickerData) lvPltToStkrData.elementAt(0);
				addDlrStkrInv(laCTData, laPltToStkrData.getRegStkrCd());
			}
		}
		// end defect 9251 
		laCTData.setTransCode(asTransCd);
		return laCTData;
	}

	/**
	 * Determine if Plt Exp Yr Invalid
	 *  
	 * @param abVendor
	 * @param asPltYr
	 * @return boolean
	 */
	public static boolean isValidPltExpYr(
		boolean abVendor,
		int aiPltYr)
	{
		int liMaxPlus =
			abVendor
				? SpecialPlatesConstant.MAX_PLUS_CURRENT_YR_VENDOR
				: SpecialPlatesConstant.MAX_PLUS_CURRENT_YR;

		return aiPltYr
			>= SpecialPlatesConstant.FIRST_YEAR_SPECIALTY_PLATES
			&& aiPltYr <= (RTSDate.getCurrentDate().getYear() + liMaxPlus);
	}

	/**
	 * Save MF SpecialPlatesRegisData to the original VehicleInquiryData
	 * object
	 * 
	 * @param aaOrigVehInqData	VehicleInquiryData
	 * @param aaVehInqData		VehicleInquiryData
	 * @return VehicleInquiryData
	 * @throws RTSException 
	 */
	public static VehicleInquiryData saveSPData(
		VehicleInquiryData aaOrigVehInqData,
		VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		String lsRequestType = new String();
		if (aaOrigVehInqData.getMfVehicleData().getSpclPltRegisData()
			== null
			|| (aaOrigVehInqData
				.getMfVehicleData()
				.getSpclPltRegisData()
				.getRequestType()
				== null
				|| aaOrigVehInqData
					.getMfVehicleData()
					.getSpclPltRegisData()
					.getRequestType()
					.length()
					== 0))
		{
			//	Move SP MF data to original data object
			aaOrigVehInqData.getMfVehicleData().setSpclPltRegisData(
				aaVehInqData.getMfVehicleData().getSpclPltRegisData());
		}
		else
		{
			SpecialPlatesRegisData laOrigSpclPlt =
				aaOrigVehInqData
					.getMfVehicleData()
					.getSpclPltRegisData();
			SpecialPlatesRegisData laNewSpclPlt =
				aaVehInqData.getMfVehicleData().getSpclPltRegisData();

			lsRequestType =
				aaOrigVehInqData
					.getMfVehicleData()
					.getSpclPltRegisData()
					.getRequestType();

			//	Reserved save Owner and MfgStatusCd and ResComptCntyNo
			if (lsRequestType
				.equals(SpecialPlatesConstant.FROM_RESERVE))
			{
				// Save OwnerData & MfgStatusCd  					
				laOrigSpclPlt.setOwnrData(laNewSpclPlt.getOwnrData());
				laOrigSpclPlt.setMFGStatusCd(
					laNewSpclPlt.getMFGStatusCd());
				laOrigSpclPlt.setResComptCntyNo(
					laNewSpclPlt.getResComptCntyNo());
				laOrigSpclPlt.setSAuditTrailTransId(
					laNewSpclPlt.getSAuditTrailTransId());
				laOrigSpclPlt.setSpclRegId(laNewSpclPlt.getSpclRegId());
			}
			//	Else, save all the data
			// defect 9396
			// No longer check for Customer Supplied 
			else
			{
				// Move SP MF data to original data object
				aaOrigVehInqData
					.getMfVehicleData()
					.setSpclPltRegisData(
					laNewSpclPlt);
			}
			// end defect 9396 
			// Save original request Type
			aaOrigVehInqData
				.getMfVehicleData()
				.getSpclPltRegisData()
				.setRequestType(lsRequestType);
		}
		return aaOrigVehInqData;
	}

	/**
	 * Create a VehInqData object to include SpecialPlateRegisData
	 * 
	 * @param String asTransCd
	 * @param String asOrigPlateNo 
	 * @return VehicleInquiryData
	 */
	public static VehicleInquiryData createVehInqData(
		String asTransCd,
		String asOrigPlateNo)
	{
		VehicleInquiryData laVehInqData = new VehicleInquiryData();
		MFVehicleData laMFVehicleData = new MFVehicleData();
		laVehInqData.setMfVehicleData(laMFVehicleData);
		SpecialPlatesRegisData laSpclPltRegisData =
			new SpecialPlatesRegisData();
		laSpclPltRegisData.initWhereNull();
		laSpclPltRegisData.setRegPltNo(asOrigPlateNo);
		if (asTransCd.equals(TransCdConstant.SPUNAC))
		{
			laSpclPltRegisData.setMFGStatusCd(
				SpecialPlatesConstant.UNACCEPTABLE_MFGSTATUSCD);
		}
		else if (asTransCd.equals(TransCdConstant.SPRSRV))
		{
			laSpclPltRegisData.setMFGStatusCd(
				SpecialPlatesConstant.RESERVE_MFGSTATUSCD);
		}

		// defect 10441 
		laSpclPltRegisData.setRegPltCd(
			SpecialPlatesConstant.PLP_REGPLTCD);
		// end defect 10441 

		laMFVehicleData.setSpclPltRegisData(laSpclPltRegisData);
		laVehInqData.setNoMFRecs(1);
		laVehInqData.setMfVehicleData(laMFVehicleData);
		return laVehInqData;
	}

	/**
	 * Set up General Search Data
	 * 
	 * @param asRegPltNo
	 * @param asTransCd
	 * @return GeneralSearchData
	 */
	public static GeneralSearchData buildSpclPltNoSearchGSD(
		String asRegPltNo,
		String asTransCd)
	{
		GeneralSearchData laGSD = new GeneralSearchData();
		//Initialize Variables
		laGSD.setIntKey1(0);
		laGSD.setIntKey2(0);
		laGSD.setKey1(null);
		laGSD.setKey2(null);

		// Convert I and O on RegPltNo w/in SPClientBusiness.getVeh() 
		// Set Type of search
		laGSD.setKey1(CommonConstant.REG_PLATE_NO);
		// Set Search Key
		laGSD.setKey2(asRegPltNo.toUpperCase());
		// Set TransCd
		laGSD.setKey3(asTransCd);
		// Set Search SP only
		laGSD.setIntKey2(CommonConstant.SEARCH_SPECIAL_PLATES);
		return laGSD;
	}

	/**
	 * Setup the Inventory Allocation Data Object
	 * 
	 * @param aaSpclPltRegisData
	 * @param asOrigRegPltNo
	 * @return InventoryAllocationData 
	 * 
	 * @throws RTSException 
	 */
	public static InventoryAllocationData setupInvAlloc(
		SpecialPlatesRegisData aaSpclPltRegisData,
		String asOrigRegPltNo)
		throws RTSException
	{
		InventoryAllocationData laInvAllocData =
			new InventoryAllocationData();
		laInvAllocData.setItmCd(aaSpclPltRegisData.getRegPltCd());
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(
				aaSpclPltRegisData.getRegPltCd());
		boolean lbPersonalizedIndi =
			laPltTypeData.getUserPltNoIndi() == 1;
		int liYear = aaSpclPltRegisData.getInvItmYr();
		// defect 9386 
		// add ISSUE_FROM_INVENTORY
		if (lbPersonalizedIndi
			|| (aaSpclPltRegisData.getRequestType() != null
				&& (aaSpclPltRegisData
					.getRequestType()
					.equals(SpecialPlatesConstant.FROM_RESERVE)
					|| aaSpclPltRegisData.getRequestType().equals(
						SpecialPlatesConstant.UNACCEPTABLE)
					|| aaSpclPltRegisData.getRequestType().equals(
						SpecialPlatesConstant.CUSTOMER_SUPPLIED)
					|| aaSpclPltRegisData.getRequestType().equals(
						SpecialPlatesConstant.ISSUE_FROM_INVENTORY))))
		{
			laInvAllocData.setInvItmNo(
				aaSpclPltRegisData.getRegPltNo());
			laInvAllocData.setInvItmEndNo(
				aaSpclPltRegisData.getRegPltNo());
			laInvAllocData.setInvStatusCd(
				InventoryConstant.HOLD_INV_SYSTEM);
		}
		// end defect 9386 
		laInvAllocData.setInvItmYr(liYear);
		laInvAllocData.setISA(aaSpclPltRegisData.getISAIndi() == 1);
		laInvAllocData.setUserPltNo(lbPersonalizedIndi);
		laInvAllocData.setItrntReq(false);
		laInvAllocData.setMfgPltNo(aaSpclPltRegisData.getMfgPltNo());
		laInvAllocData.setInvQty(1);
		laInvAllocData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laInvAllocData.setRequestorRegPltNo(asOrigRegPltNo);
		laInvAllocData.setTransWsId(SystemProperty.getWorkStationId());
		laInvAllocData.setTransAmDate(new RTSDate().getAMDate());
		laInvAllocData.setTransEmpId(SystemProperty.getCurrentEmpId());
		String lsHostName = new String();
		try
		{
			lsHostName =
				java.net.InetAddress.getLocalHost().getHostName();
		}

		catch (java.net.UnknownHostException aeUHEx)
		{

		}
		laInvAllocData.setRequestorIpAddress(lsHostName);
		return laInvAllocData;
	}

	/**
	 * Verify Available if Sunset Plate  (PlateAge LT PltMandPltReplAge) 
	 * 
	 * @param aaSpclPltRegisData
	 * @param asTransCd 
	 * @throws RTSException
	 */
	private static void verifyAvailableSunsetPlate(
		SpecialPlatesRegisData aaSpclPltRegisData,
		String asTransCd)
		throws RTSException
	{
		if (aaSpclPltRegisData.getRegPltCd() != null
			&& aaSpclPltRegisData.getOrgNo() != null)
		{
			String lsRegPltCd = aaSpclPltRegisData.getRegPltCd().trim();
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(lsRegPltCd);
			String lsOrgNo = aaSpclPltRegisData.getOrgNo().trim();
			if (OrganizationNumberCache
				.isSunsetted(lsRegPltCd, lsOrgNo))
			{
				// defect 9351 
				// Allow selection of record if current 
				boolean lbRenewal =
					asTransCd.equals(TransCdConstant.SPRNW);
				// || asTransCd.equals(TransCdConstant.RENEW);

				int liPltAge =
					aaSpclPltRegisData.getRegPltAge(lbRenewal);

				if (liPltAge >= laPltTypeData.getMandPltReplAge())
				{
					// defect 9864 
					// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
					boolean lbRegExpired =
						CommonValidations.isRegistrationExpired(
							aaSpclPltRegisData.getPltExpMo(),
							aaSpclPltRegisData.getPltExpYr());
					// end defect 9864 

					if (lbRenewal || lbRegExpired)
					{
						// defect 9236
						// Use Trans Code Description vs. TransCd in Err Msgs 
						throw new RTSException(
							RTSException.FAILURE_MESSAGE,
							"This plate is not available for "
								+ getTransCdDesc(asTransCd)
								+ ". Given the plate age, "
								+ "the plate must be replaced, but the organization, "
								+ OrganizationNumberCache.getOrgName(
									lsRegPltCd,
									lsOrgNo)
								+ ", is no longer eligible for manufacture.",
							"ERROR");
						// end defect 9236
					}
				}
				// end defect 9351
			}
		}
	}

	/**
	 * Verify the actual MfgStatusCd the record against the expected
	 * 
	 * @param asTransCd
	 * @param asMfgStatusCd
	 * @param asReqMfgStatusCd
	 * @throws RTSException
	 */
	private static void verifyMfgStatusCd(
		String asTransCd,
		String asMfgStatusCd,
		String asReqMfgStatusCd)
		throws RTSException
	{
		if (!asMfgStatusCd.trim().equals(asReqMfgStatusCd.trim()))
		{
			// Interpret the current MsgStatusCd 
			String lsInterpretMfgStatusCd =
				(String) SpecialPlatesConstant.INTERPRET_STATUSCDS.get(
					asMfgStatusCd.trim());
			if (lsInterpretMfgStatusCd == null)
			{
				lsInterpretMfgStatusCd = "UNDEFINED";
			}
			String lsMfgMsg = lsInterpretMfgStatusCd;
			if (!asMfgStatusCd.trim().equals(""))
			{
				lsMfgMsg = asMfgStatusCd + ", " + lsMfgMsg;
			}

			// Build ErrMsg String
			// defect 9236
			// Use Trans Code Description vs. TransCd in Err Msgs 
			String lsErrMsg =
				"The selected record has a status of "
					+ lsMfgMsg
					+ ", and is not available to satisfy a request in "
					+ getTransCdDesc(asTransCd)
					+ ".";

			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				lsErrMsg,
				"ERROR");
			// end defect 9236
		}
	}

	/**
	 * Verify that plate type is defined and available 
	 * 
	 * @param asRegPltCd
	 * @return PlateTypeData 
	 * @throws RTSException
	 */
	private static void verifyPltTypeDefinedAndAvailable(
		String asRegPltCd,
		String asTransCd)
		throws RTSException
	{
		if (asRegPltCd == null || asRegPltCd.length() == 0)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"The selected record does not have an associated plate type.",
				"ERROR");
		}
		// Verify plate type via cache
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(asRegPltCd);
		if (laPltTypeData == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"The selected record has an invalid plate type",
				"ERROR");
		}

		// Check for Registration Events  (default)
		boolean lbAuth = !PlateTypeCache.isOutOfScopePlate(asRegPltCd);

		// Check for Application Event
		boolean lbApplAuth =
			!PlateTypeCache.isOutOfScopePlate(
				asRegPltCd,
				SystemProperty.getOfficeIssuanceCd(),
				SpecialPlatesConstant.ORDER_TYPE_EVENTS);

		// Either Authorization will do for Revise
		if (asTransCd.equals(TransCdConstant.SPREV))
		{
			lbAuth = lbApplAuth || lbAuth;
		}

		// Throw exception if not authorized  
		if (!lbAuth)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"The selected record has a plate type of "
					+ asRegPltCd
					+ " and is out of scope for this office.",
				"ERROR");
		}
	}

	/**
	 * Verify valid OrgNo
	 * 
	 * @param asRegPltCd
	 * @param asOrgNo
	 * @throws RTSException
	 */
	private static void verifyOrgNoValid(
		String asRegPltCd,
		String asOrgNo,
		String asTransCd)
		throws RTSException
	{
		String lsOrganizationName =
			OrganizationNumberCache.getOrgName(asRegPltCd, asOrgNo);
		// defect 9699 
		// Do not throw exception when in the SPREV event.  
		if (lsOrganizationName.trim().length() == 0)
		{
			RTSException laRTSEx =
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					"The organization number on the record is not valid"
						+ " and must be corrected via the Revise event.",
					"ERROR");
			if (asTransCd.equals(TransCdConstant.SPREV))
			{
				laRTSEx.displayError();
			}
			else
			{
				throw laRTSEx;
			}
		}
		// end defect 9699 
	}

	// Removed 6-19-07  (KPH)
	//	/**
	//	 * 
	//	 * Verify Same RescomptCntyNo on Reserved Request
	//	 * 
	//	 * @param aaSpclPltRegisData
	//	 * @throws RTSException
	//	 */
	//	private static void verifyResComptCntyNoOnSPAPPLReserved(SpecialPlatesRegisData aaSpclPltRegisData)
	//		throws RTSException
	//	{
	//		if (SystemProperty.isCounty()
	//			&& aaSpclPltRegisData.getResComptCntyNo()
	//				!= SystemProperty.getOfficeIssuanceNo())
	//		{
	//			throw new RTSException(
	//				RTSException.FAILURE_MESSAGE,
	//				"The County Number associated with this Reserved Plate "
	//					+ " does not match the requesting office. ",
	//				"ERROR");
	//		}
	//	}

	/**
	 * Verify Plate No
	 * 
	 * @param aaSpclPltRegisData
	 * @throws RTSException
	 */
	public static void verifyValidPltNo(SpecialPlatesRegisData aaSpclPltRegisData)
		throws RTSException
	{
		ValidateInventoryPattern laValidateInventoryPattern =
			new ValidateInventoryPattern();
		ProcessInventoryData laProcessInventoryData =
			new ProcessInventoryData();
		String lsPlateNo = aaSpclPltRegisData.getRegPltNo().trim();
		laProcessInventoryData.setItmCd(
			aaSpclPltRegisData.getRegPltCd());
		laProcessInventoryData.setInvQty(1);
		laProcessInventoryData.setInvItmNo(lsPlateNo);
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(
				aaSpclPltRegisData.getRegPltCd());
		if (laPltTypeData != null)
		{
			if (laPltTypeData.getUserPltNoIndi() == 1
				&& lsPlateNo.length() > laPltTypeData.getMaxByteCount())
			{
				throw new RTSException(2014);
			}
			// If Not Annual, will validate prior to Screen Presentation
			//  (SPL004) 
			if (laPltTypeData.getAnnualPltIndi() != 1)
			{
				laProcessInventoryData.setInvItmYr(0);
				laValidateInventoryPattern.validateItmNoInput(
					laProcessInventoryData.convertToInvAlloctnUIData(
						laProcessInventoryData));
			}
		}
		else
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Invalid Plate Type. ",
				"ERROR");
		}
	}

	/**
	 * Verify Record Available for Processing  
	 * 
	 * @param aaOrigVehInqData
	 * @param aaVehInqData
	 * @param asTransCd
	 * @throws RTSException
	 */
	public static void verifyRecordApplicable(
		VehicleInquiryData aaOrigVehInqData,
		VehicleInquiryData aaVehInqData,
		String asTransCd)
		throws RTSException
	{
		try
		{
			// defect 9236
			// Use Trans Code Description vs. TransCd in Err Msgs
			String lsTransCdDesc = getTransCdDesc(asTransCd);
			SpecialPlatesRegisData laNewSpclPltData =
				aaVehInqData.getMfVehicleData().getSpclPltRegisData();
			laNewSpclPltData.initWhereNull();
			String lsMfgStatusCd = laNewSpclPltData.getMFGStatusCd();
			// If !Special Plates || (!HQ && (SPREV || SPRNW))
			if (!UtilityMethods.isSpecialPlates(asTransCd)
				|| (!SystemProperty.isHQ()
					&& asTransCd.equals(TransCdConstant.SPREV))
				|| asTransCd.equals(TransCdConstant.SPRNW))
			{
				// defect 9689
				// Do not allow expired Vendor Plate for Title or Renew
				// defect 10357
				// Vendor Plate can be renewed in Title or Renew
				//				if (PlateTypeCache
				//					.isVendorPlate(laNewSpclPltData.getRegPltCd())
				//					&& (asTransCd.equals(TransCdConstant.TITLE)
				//						|| asTransCd.equals(TransCdConstant.NONTTL)
				//						|| asTransCd.equals(TransCdConstant.REJCOR)
				//						|| asTransCd.equals(TransCdConstant.RENEW)))
				//				{
				//					// defect 9864 
				//					// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
				//					// Also refactored lsPltExpMo/Yr to liPltExpMo/Yr 
				//					int liPltExpMo = laNewSpclPltData.getPltExpMo();
				//					int liPltExpYr = laNewSpclPltData.getPltExpYr();
				//
				//					if (CommonValidations
				//						.isPlateExpired(
				//							liPltExpMo,
				//							liPltExpYr,
				//							new RTSDate().getYYYYMMDDDate()))
				//					{
				//
				//						throw new RTSException(
				//							RTSException.FAILURE_MESSAGE,
				//							"The Vendor Plate is expired.",
				//							"ERROR");
				//					}
				//					// end defect 9864
				//				}
				// end defect 10357
				// end defect 9689
				// Verify Assign MfgStatusCd 
				verifyMfgStatusCd(
					asTransCd,
					lsMfgStatusCd,
					SpecialPlatesConstant.ASSIGN_MFGSTATUSCD);
				// Verify Plate Type Defined & Valid 
				verifyPltTypeDefinedAndAvailable(
					laNewSpclPltData.getRegPltCd(),
					asTransCd);
				// Verify valid OrgNo
				verifyOrgNoValid(
					laNewSpclPltData.getRegPltCd(),
					laNewSpclPltData.getOrgNo(),
					asTransCd);

				// defect 9864 
				// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
				boolean lbExpired =
					CommonValidations.isRegistrationExpired(
						laNewSpclPltData.getPltExpMo(),
						laNewSpclPltData.getPltExpYr());
				boolean lbRenwlWindow =
					CommonValidations.isInRenewalWindow(
						laNewSpclPltData.getPltExpMo(),
						laNewSpclPltData.getPltExpYr());
				// end defect 9864 

				PlateTypeData laPltTypeData =
					PlateTypeCache.getPlateType(
						laNewSpclPltData.getRegPltCd());
				boolean lbLP =
					laPltTypeData.getNeedsProgramCd().equals(
						SpecialPlatesConstant.LP_PLATE);
				boolean lbAnnualPlt =
					laPltTypeData.getAnnualPltIndi() == 1;
				// If Annual && LP
				if (lbAnnualPlt && lbLP)
				{
					if (lbExpired
						&& (asTransCd.equals(TransCdConstant.EXCH)
							|| asTransCd.equals(TransCdConstant.RENEW)
							|| UtilityMethods.getEventType(
								asTransCd).equals(
								TransCdConstant.TTL_EVENT_TYPE)))
					{
						throw new RTSException(
							RTSException.FAILURE_MESSAGE,
							"The selected record is not available for "
								+ lsTransCdDesc
								+ " as the registration has expired. ",
							"ERROR");
					}
					if (asTransCd.equals(TransCdConstant.RENEW))
					{
						MFVehicleData laMFVehData =
							aaOrigVehInqData.getMfVehicleData();

						if (laMFVehData != null
							&& laMFVehData.getRegData() != null)
						{
							int liExpYr =
								laMFVehData.getRegData().getRegExpYr();

							// defect 9238 
							// Only validate different year if 
							// same Special Plate 
							boolean lbSameSpclPlt =
								laMFVehData.getSpclPltRegisData()
									!= null
									&& laMFVehData
										.getSpclPltRegisData()
										.getRegPltCd()
										!= null
									&& laMFVehData
										.getSpclPltRegisData()
										.getRegPltCd()
										.equals(
										laNewSpclPltData.getRegPltCd());
							// defect 9864 
							// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
							if (lbSameSpclPlt
								&& laNewSpclPltData.getPltExpYr()
									== liExpYr)
							{
								// end defect 9864 
								throw new RTSException(
									RTSException.FAILURE_MESSAGE,
									"The expiration year of the selected record must be "
										+ " greater than the current vehicle registration year. ",
									"ERROR");
							}
							// end defect 9238 
						}
					}
				}
				if (asTransCd.equals(TransCdConstant.SPRNW))
				{
					// Cannot SPRNW "LP" plates 
					if (lbLP)
					{
						throw new RTSException(
							RTSException.FAILURE_MESSAGE,
							"The selected record is an LP plate and is not available for "
								+ lsTransCdDesc
								+ ". "
								+ " Please use the Special Plates Application Event to reapply.",
							"ERROR");
					}
					// defect 9689
					// Cannot SPRNW a Vendor Plate
					else if (
						PlateTypeCache.isVendorPlate(
							laNewSpclPltData.getRegPltCd()))
					{
						throw new RTSException(
							RTSException.FAILURE_MESSAGE,
							"The selected record is a Vendor plate and is not available for "
								+ lsTransCdDesc
								+ ". ",
							"ERROR");
					}
					// end defect 9689
					else if (!lbExpired && !lbRenwlWindow)
					{
						throw new RTSException(50);
					}
				}
				if (asTransCd.equals(TransCdConstant.SPRNW)
					|| UtilityMethods.getEventType(asTransCd).equals(
						TransCdConstant.REG_EVENT_TYPE)
					|| UtilityMethods.getEventType(asTransCd).equals(
						TransCdConstant.TTL_EVENT_TYPE))
				{
					// Verify Sunsetted Plate is not past Mandatory
					// replacement age
					verifyAvailableSunsetPlate(
						laNewSpclPltData,
						asTransCd);
				}
				// Not Special Plates Event  
				if (!UtilityMethods.isSpecialPlates(asTransCd))
				{
					// Plate Type of selected does not match Original
					if (!aaOrigVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegPltCd()
						.equals(laNewSpclPltData.getRegPltCd()))
					{
						ItemCodesData laItemCodes =
							ItemCodesCache.getItmCd(
								laNewSpclPltData.getRegPltCd());
						String lsPltTypeDesc = "UNDEFINED";
						if (laItemCodes != null)
						{
							lsPltTypeDesc = laItemCodes.getItmCdDesc();
						}
						throw new RTSException(
							RTSException.FAILURE_MESSAGE,
							"The selected record has a plate type of "
								+ lsPltTypeDesc
								+ " which is not consistent with the current"
								+ " selection.",
							"ERROR");
					}
				}
			}
			else
			{
				// SPRSRV, SPUNAC 
				if (asTransCd.equals(TransCdConstant.SPRSRV)
					|| asTransCd.equals(TransCdConstant.SPUNAC))
				{
					String lsInterpretMfgStatusCd =
						(
							String) SpecialPlatesConstant
								.INTERPRET_STATUSCDS
								.get(
							lsMfgStatusCd);
					if (lsInterpretMfgStatusCd == null)
					{
						lsInterpretMfgStatusCd = "UNDEFINED";
					}
					String lsMfgMsg = lsInterpretMfgStatusCd;
					if (!lsMfgStatusCd.equals(""))
					{
						lsMfgMsg = lsMfgStatusCd + ", " + lsMfgMsg;
					}
					lsMfgMsg =
						"A record with the specified plate number currently exists with the status of "
							+ lsMfgMsg
							+ ".  ";
					// Do not prompt user if Unacceptable || Reserve. 
					// HQ cannot modify those statuses.
					boolean lbDoNotAddEndingMsg =
						lsMfgStatusCd.equals(
							SpecialPlatesConstant
								.UNACCEPTABLE_MFGSTATUSCD)
							|| lsMfgStatusCd.equals(
								SpecialPlatesConstant
									.RESERVE_MFGSTATUSCD);
					if (!lbDoNotAddEndingMsg)
					{
						lsMfgMsg =
							lsMfgMsg
								+ "The status can only be modified by "
								+ "Headquarters in "
								+ getTransCdDesc("SPREV")
								+ ".";
					}
					throw new RTSException(
						RTSException.FAILURE_MESSAGE,
						lsMfgMsg,
						"ERROR");
				}
				// SPAPPL, etc.  
				if (UtilityMethods.isSPAPPL(asTransCd))
				{
					SpecialPlatesRegisData laOrigSpclPltData =
						aaOrigVehInqData
							.getMfVehicleData()
							.getSpclPltRegisData();
					laOrigSpclPltData.initWhereNull();
					String lsRequestType =
						laOrigSpclPltData.getRequestType();
					if (lsRequestType
						.equals(
							SpecialPlatesConstant.PLATE_OWNER_CHANGE)
						|| lsRequestType.equals(
							SpecialPlatesConstant.FROM_RESERVE))
					{
						String lsReqdMfgStatusCd =
							lsRequestType.equals(
								SpecialPlatesConstant
									.PLATE_OWNER_CHANGE)
								? SpecialPlatesConstant
									.ASSIGN_MFGSTATUSCD
								: SpecialPlatesConstant
									.RESERVE_MFGSTATUSCD;
						// Verify MfgStatusCd = "" (Assigned) 
						verifyMfgStatusCd(
							asTransCd,
							laNewSpclPltData.getMFGStatusCd(),
							lsReqdMfgStatusCd);
						if (lsRequestType
							.equals(
								SpecialPlatesConstant
									.PLATE_OWNER_CHANGE))
						{
							// Verify same Plate Type as that selected
							String lsRegPltCd =
								laNewSpclPltData.getRegPltCd();
							String lsOrgNo =
								laNewSpclPltData.getOrgNo();
							verifyPltTypeDefinedAndAvailable(
								laNewSpclPltData.getRegPltCd(),
								asTransCd);
							if (!aaOrigVehInqData
								.getMfVehicleData()
								.getSpclPltRegisData()
								.getRegPltCd()
								.equals(lsRegPltCd))
							{
								ItemCodesData laItemCodes =
									ItemCodesCache.getItmCd(lsRegPltCd);
								String lsPltTypeDesc =
									laItemCodes.getItmCdDesc();
								throw new RTSException(
									RTSException.FAILURE_MESSAGE,
									"The selected record has a plate type of "
										+ lsPltTypeDesc
										+ " which is not consistent with the current selection.",
									"ERROR");
							}
							// Verify same Organization as that selected
							if (!aaOrigVehInqData
								.getMfVehicleData()
								.getSpclPltRegisData()
								.getOrgNo()
								.trim()
								.equals(lsOrgNo))
							{
								String lsOrganizationName =
									OrganizationNumberCache.getOrgName(
										lsRegPltCd,
										lsOrgNo);
								throw new RTSException(
									RTSException.FAILURE_MESSAGE,
									"The organization on the selected record, "
										+ lsOrganizationName
										+ ", is not "
										+ " consistent with the current selection.",
									"ERROR");
							}
							// defect 9864 
							// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
							// Verify correct InvItmYr if Annual 
							// defect 9396 
							if (aaOrigVehInqData
								.getMfVehicleData()
								.getSpclPltRegisData()
								.getInvItmYr()
								!= 0
								&& aaOrigVehInqData
									.getMfVehicleData()
									.getSpclPltRegisData()
									.getInvItmYr()
									!= laNewSpclPltData.getPltExpYr())
							{
								throw new RTSException(
									RTSException.FAILURE_MESSAGE,
									"A "
										+ laNewSpclPltData.getPltExpYr()
										+ " record exists "
										+ " with the same plate number but does not "
										+ " match the year specified.",
									"ERROR");
							}
							// end defect 9396 

							// Verify Current  
							if (CommonValidations
								.isRegistrationExpired(
									laNewSpclPltData.getPltExpMo(),
									laNewSpclPltData.getPltExpYr()))
							{
								throw new RTSException(
									RTSException.FAILURE_MESSAGE,
									"The selected record is expired and not available for "
										+ " ownership change.",
									"ERROR");
							}
							// end defect 9864 
						}
					}
				}
			}
			// end defect 9236 	
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.setBeep(1);
			throw aeRTSEx;
		}
	}
}