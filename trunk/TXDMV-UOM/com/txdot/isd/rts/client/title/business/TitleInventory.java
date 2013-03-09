package com.txdot.isd.rts.client.title.business;

import java.util.Vector;

import com.txdot.isd.rts.client.title.ui.TitleValidObj;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.TitleTypes;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * TitleInventory.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * T Pederson   05/10/2002  Changed doRejCorrInventory to only setup 
 *							inventory vector when reg class or plate 
 *							type has changed
 * J Rue		10/23/2002	Code added but turned off for future use. 
 *							Set registration registration expired 
 *							boolean. 
 *							modify issueInvRecordFound(),
 *							determineRegisExpired()
 * J Rue		08/13/2004	Correct conditional to check if inventory 
 *							was issued at DTA008 screen
 *							modfify doDlrTtlInventory()
 *							defect 7437 Ver 5.2.1
 * J Rue		08/25/2004	DealerTitleData.NewRegExpMo() and 
 *							DealerTitleData.NewRegExpYr() were 
 *							converted to integer.
 *							modify doDlrTtlInventory(), 
 *							issueNewRegStckrForDlr(),
 *							isValidNewExpMoYr()
 *							defect 7496 Ver 5.2.1
 * K Harrell	12/29/2004	Correct assessment of new plate is issued
 *							Formatting/JavaDoc/Variable Name cleanup
 *							modify isNewPltStkr_IfRegPltChg()
 *							defect 6820 Ver 5.2.2
 * J Rue		12/31/2004	Check for OffHwyUse when issuing new 
 *							plates
 *							modify issueNewPlate()
 *							defect 7538 Ver 5.2.2
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7898 Ver 5.2.3 
 * B Hargrove 	10/11/2006 	Counties can now do Exempts. Modify edits 
 * K Harrell				that were looking for 'Headquarters' 
 * 							modify issueInvRecordFound()
 * 							defect 8900 Ver Exempts 
 * Min Wang		10/12/2006	New Requirement for handling plate age 
 * 							modify issueInvRecordFound(), issueNewPlate()
 *							defect 8901 Ver Exempts
 * K Harrell	11/21/2006	Implementation of NeedsProgramCd 
 * 							Do not consider Plate Age unless 
 * 							NeedsProgramCd = "R"
 * 							add PLT_REPLACEMENT_REQUIRED
 * 							modify issueInvRecordFound(),
 * 							 issueNewPlate() 
 * 							defect 9025 Ver Exempts
 * K Harrell	11/27/2006	Null pointer in issueNewPlate().  Incorrect
 * 							parameter to RegistrationRenewalsCache. 
 * 							modify issueNewPlate()
 * 							defect 9033 Ver Exempts
 * K Harrell	12/05/2006	DTA does not have to specify new plate on 
 * 							DTA008
 * 							modify issueNewPlate()
 * 							defect 9047 Ver Exempts
 * K Harrell	02/05/2007 	Use PlateTypeCache vs. 
 * 									RegistrationRenewalsCache
 * 							issueInvRecordFound(),issueNewPlate()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/27/2007	Eliminate reference to InvProcsngCd
 * 							delete SPECIAL_PLATES
 * 							modify doInventory() 
 * 							defect 9085 Ver Special Plates 
 * K Harrell	04/22/2007	use SystemProperty.isHQ() 		
 * 							modify setSpclPltProgIndi()
 * 							defect 9085 Ver Special Plates
 * B Hargrove	05/07/2007	When issuing Special Plate, we need to 
 * 							capture the Inventory Item No entered on 
 * 							KEY002 so that later on INV007 Inv Display 
 * 							is shown rather than INV001 Inv Entry.
 * 							modify doRejCorrInventory(), 
 * 							issueInvNoRecord(), issueInvRecordFound()
 * 							defect 9126 Ver Special Plates
 * B Hargrove	07/17/2007	Modify to handle out-of-scope for DTA
 * 							(see modification of 02/27/2007).
 * 							For out-of-scope, the 'new reg data' (plate,
 * 							stkr, expmo\yr) is not retained.  
 * 							modify doClearRegDataForReceipt(),
 * 							doDlrTtlInventory() 
 * 							defect 9085 Ver Special Plates 
 * B Hargrove	11/20/2007	Do not set inventory if 'TONLY'
 * 							(Title Only - No Regis).
 * 							doInventory() 
 * 							defect 9337 Ver Special Plates 2
 * K Harrell	01/17/2008	Prior plate no on receipt for 'TONLY'
 * 							Do not check for !TONLY in doInventory()
 * 							modify doInventory()
 * 							defect 9497 Ver 3 Amigos Prep 
 * B Hargrove	04/18/2008	Add check for 'HQ and TONLY' so we do not 
 * 							set inventory for HQ.
 * 							modify doInventory() 
 * 							defect 9632 Ver FRVP
 * J Rue		09/21/2009	NewStkrNo has been deprecated for v6.1.0
 * 							modify doClearRegDataForReceipt(),
 * 								issueNewRegStckrForDlr()
 * 							defect 10232 Ver Defect_POS_F
 * K Harrell	12/20/2009	DTA Cleanup. Unnecessary to again validate
 * 							 New RegExpMo/Yr.  Reorder issue of  
 * 							 Form31/PlateNo. ServerDown if PlateNo   
 * 							 invalid && before Form31.
 * 							add isRegPltCdandStkrUnchanged() 
 * 							delete isValidNewExpMoYr(),
 * 								isRegPltCdAndStckrNotChanged()
 * 							modify doDlrTtlInventory()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	03/31/2010	DTA does not include TR_INV_DETAIL record
 * 							when registration in Window
 * 							delete isRegPltCdandStkrUnchanged()
 * 							modify doDlrTtlInventory() 
 * 							defect 10429 Ver POS_640 
 * B Hargrove 	10/25/2010  Token Trailer now does not print sticker.
 * 							Add check for Token Trailer (33).
 * 							modify issueInvNoRecord(),
 * 							issueInvRecordFound() 
 * 							defect 10623 Ver 6.6.0
 * B Hargrove 	10/29/2010  Token Trailer now does not print sticker.
 * 							Comment out all changes. They want to store 
 * 							inv detail, just don't print sticker.
 * 							modify issueInvNoRecord(),
 * 							issueInvRecordFound()
 * 							defect 10623 Ver 6.6.0
* B Hargrove	09/02/2011	For invalid reg we should issue sticker if 
 * 							not Annual Plt. We need this now because we
 * 							invalidate regis on Title w/ GDN and we need
 * 							to indicate that a sticker is issued.
 * 							(moved up from Ver 6.8.0)
 * 							modify issueInvRecordFound()
 * 							defect 10988 Ver 6.8.1  
 * B Hargrove	11/02/2011	Notice that code was not checking  the 'real
 * 							original' Reg Data when checking for Reg Invalid. 
 * 							modify issueInvRecordFound()
 * 							defect 11136 Ver 6.8.2  
 * B Hargrove 	02/02/2012  We need to issue sticker when 'New Plates 
 * 							Desired' was selected 
 * 							modify issueInvRecordFound()
 * 							(moved up from Rel 6.9.0)
 * 							defect 11264 Ver 6.9.0
 * ---------------------------------------------------------------------
 */

/**
 * This class sets up data needed to issue inventory in all title events.
 * 
 * @version	Ver 6.9.0 			02/02/2012
 * @author	Ashish Mahajan
 * <br>Creation Date:			11/19/2001 16:13:19 
 */

public class TitleInventory
{
	private VehicleInquiryData caVehInqData = null;
	private DealerTitleData caDlrTtlData = null;
	private Vector cvInvItemsData = new Vector();
	private final static String FORM31 = "FORM31";
	private final static String TOWTP = "TOWTP";

	// defect 9025 
	private final static String PLT_REPLACEMENT_REQUIRED = "R";
	// end defect 9025

	/**
	 * CCO Title inventory item code for Inventory allocation
	 */
	private final static String CCO_INV_ITM_CD = "CCOTITLE";

	private String csTransCd;

	/**
	 * TitleInventory constructor comment.
	 */
	public TitleInventory()
	{
		super();
	}

	/**
	 * Set boolean for registration expired.
	 * 
	 * @param aiRegisMo int
	 * @param aiRegisYr int
	 * @param aiTransDate int
	 * @return boolean
	 */
	public boolean determineRegisExpired(
		int aiRegisMo,
		int aiRegisYr,
		int aiTransDate)
	{
		boolean lbRegisExpired = false;
		RTSDate laCurrDate = RTSDate.getCurrentDate();
		RTSDate laTransDate = new RTSDate(1, aiTransDate);
		// defect 4811
		// Determine Current date > RTS Effective Date
		if (laCurrDate.compareTo(laTransDate) == 1)
		{
			lbRegisExpired =
				CommonValidations.isRegistrationExpired(
					aiRegisMo,
					aiRegisYr,
					aiTransDate);
		}
		// Determine Current date = RTS Effective Date
		else
		{
			lbRegisExpired =
				CommonValidations.isRegistrationExpired(
					aiRegisMo,
					aiRegisYr);
		}
		return lbRegisExpired;
	}

	/**
	 * Clears registration data.
	 * 
	 */
	private void doClearRegDataForReceipt()
	{
		RegistrationData laRegData =
			caVehInqData.getMfVehicleData().getRegData();

		VehMiscData laVehMiscData = caVehInqData.getVehMiscData();

		if (!csTransCd.equals(TransCdConstant.CORTTL))
		{
			laRegData.setRegStkrNo("");
			laRegData.setRegPltNo("");
			laRegData.setRegEffDt(0);
			laRegData.setRegExpYr(0);
			if (laVehMiscData.getSpclPltProgIndi() == 1)
			{
				laRegData.setRegExpMo(0);
			}
			// Clear Dealer Title so reg data does not print on receipt
			if (UtilityMethods.isDTA(csTransCd))
			{
				caDlrTtlData.setNewPltNo("");
				caDlrTtlData.setNewRegExpMo(0);
				caDlrTtlData.setNewRegExpYr(0);
			}
		}
	}

	/**
	 * Controls the initial setting of the inventory vector for dealer title.
	 *
	 * @param aaVehInqData
	 * @param aaDlrTtlData
	 * @param asTransCd
	 */
	public void doDlrTtlInventory(
		VehicleInquiryData aaVehInqData,
		DealerTitleData aaDlrTtlData,
		String asTransCd)
	{
		this.caVehInqData = aaVehInqData;
		this.caDlrTtlData = aaDlrTtlData;
		this.csTransCd = asTransCd;

		if (caVehInqData != null)
		{
			MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();
			RegistrationData laRegData = laMFVehData.getRegData();

			if (PlateTypeCache
				.isOutOfScopePlate(laRegData.getRegPltCd()))
			{
				setSpclPltProgIndi();
				doClearRegDataForReceipt();
				issueForm31();
			}
			// defect 10429 
			//			else if (
			//				// defect 10290 
			//			// Remove validation of New Exp Mo/Yr; Validated on DTA008.
			//			 (
			//				// Current, No Change Plt/Stkr
			//			caVehInqData.getNoMFRecs()
			//					> 0)
			//					&& !(CommonValidations
			//						.isRegistrationExpired(
			//							laRegData.getRegExpMo(),
			//							laRegData.getRegExpYr()))
			//				//&& isValidNewExpMoYr()
			//					&& isRegPltCdAndStkrUnchanged())
			//				// end defect 10290 
			//			{
			//				issueForm31();
			//				// defect 10290 
			//				// In correcting isRegPltCdAndStkrNotChanged() must add
			//				//  following:   
			//				if (caVehInqData
			//					.getMfVehicleData()
			//					.getTitleData()
			//					.getMustChangePltIndi()
			//					== 1)
			//				{
			//					issueNewPlate();
			//				}
			//			}
			// end defect 10429
			else
			{
				boolean lbNewReg =
					caDlrTtlData.getNewRegExpMo()
						+ caDlrTtlData.getNewRegExpYr()
						> 0;

				if (lbNewReg)
				{
					issueNewRegStckrForDlr();
				}
				// defect 10290 
				// Reorder so that no DB Server Down if invalid plateno 
				issueForm31();
				issueNewPlate();
				// end defect 10290 
			}
		}
	}

	/**
	 * Controls the initial setting of the inventory vector for title.
	 *
	 * @param aaVehInqData VehicleInquiryData
	 * @param asTransCd    String
	 * @throws RTSException 
	 */
	public void doInventory(
		VehicleInquiryData aaVehInqData,
		String asTransCd)
		throws RTSException
	{
		this.caVehInqData = aaVehInqData;
		this.csTransCd = asTransCd;

		MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();
		TitleData laTtlData = laMFVehData.getTitleData();
		RegistrationData laRegData = laMFVehData.getRegData();

		// defect 9632
		// Add check for 'is HQ and 'TONLY''
		if (PlateTypeCache.isOutOfScopePlate(laRegData.getRegPltCd())
			|| (SystemProperty.isHQ()
				&& laRegData.getRegPltCd().equals(
					CommonConstant.TONLY_REGPLTCD)))
		{
			// end defect 9632
			setSpclPltProgIndi();
			doClearRegDataForReceipt();
		}
		else if (laRegData.getRegWaivedIndi() == 1)
		{
		}
		else if (laRegData.getOffHwyUseIndi() == 1)
		{
		}
		else if (
			laTtlData.getTtlTypeIndi() == TitleTypes.INT_CORRECTED)
		{
		}
		else if (csTransCd.equals(TransCdConstant.REJCOR))
		{
			doRejCorrInventory();
		}
		else if (caVehInqData.getNoMFRecs() == 0)
		{
			issueInvNoRecord();
		}
		else if (caVehInqData.getNoMFRecs() == 1)
		{
			issueInvRecordFound();
		}
	}

	/**
	 * Sets up the inventory vector <code> InvItemsData </code> for correct title rejection.
	 *
	 * 
	 */
	private void doRejCorrInventory()
	{
		MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();

		RegistrationData laRegData = laMFVehData.getRegData();

		TitleValidObj laTtlValidObj =
			(TitleValidObj) caVehInqData.getValidationObject();

		MFVehicleData laMFVehOrigData =
			(MFVehicleData) laTtlValidObj.getMfVehOrig();

		if (laRegData.getRegInvldIndi() == 1)
		{
			laRegData.setRegExpYr(0);
		}

		// Set up inventory vector if reg class or plate has been changed	
		if ((laRegData.getRegClassCd()
			!= laMFVehOrigData.getRegData().getRegClassCd())
			|| (!laRegData
				.getRegPltCd()
				.equals(laMFVehOrigData.getRegData().getRegPltCd())))
		{
			boolean lbNewPlt = true;
			boolean lbNewStkr = false;
			if (!laRegData.getRegStkrCd().equals(""))
			{
				lbNewStkr = true;
			}
			if (lbNewStkr)
			{
				ProcessInventoryData laProcInvData =
					new ProcessInventoryData();
				laProcInvData.setTransEmpId(
					SystemProperty.getCurrentEmpId());
				laProcInvData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laProcInvData.setSubstaId(
					SystemProperty.getSubStationId());
				laProcInvData.setTransWsId(
					Integer.toString(
						SystemProperty.getWorkStationId()));
				laProcInvData.setItmCd(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegStkrCd());
				cvInvItemsData.add(laProcInvData);
			}
			if (lbNewPlt)
			{
				ProcessInventoryData laProcInvData =
					new ProcessInventoryData();
				laProcInvData.setTransEmpId(
					SystemProperty.getCurrentEmpId());
				laProcInvData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				laProcInvData.setSubstaId(
					SystemProperty.getSubStationId());
				laProcInvData.setTransWsId(
					Integer.toString(
						SystemProperty.getWorkStationId()));
				laProcInvData.setItmCd(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegPltCd());
				// defect 9126
				// If Item Number is not null, capture this Special Plate No
				if (caVehInqData
					.getMfVehicleData()
					.getSpclPltRegisData()
					!= null)
				{
					laProcInvData.setInvItmNo(
						caVehInqData
							.getMfVehicleData()
							.getSpclPltRegisData()
							.getRegPltNo());
					laProcInvData.setInvLocIdCd("U");
				}
				// end defect 9126		
				cvInvItemsData.add(laProcInvData);
			}
		}
	}

	/**
	 * Return InvItemsData
	 * 
	 * @return Vector
	 */
	public Vector getInvItems()
	{
		return cvInvItemsData;
	}

	/**
	 * Determines if plate code has changed and sticker code is not blank.
	 *  
	 * @return boolean
	 */
	private boolean isNewPltStkr_IfRegPltChg()
	{
		boolean lbRet = false;

		TitleValidObj laTtlValidObj =
			(TitleValidObj) caVehInqData.getValidationObject();

		MFVehicleData laMFVehOrigData =
			(MFVehicleData) laTtlValidObj.getMfVehOrig();

		String lsOrigPltCd = laMFVehOrigData.getRegData().getRegPltCd();

		String lsPltCd =
			caVehInqData.getMfVehicleData().getRegData().getRegPltCd();

		// defect 6820
		// omit test for Sticker 
		//String lsRegStkrCd =
		//	caVehInqData.getMfVehicleData().getRegData().getRegStkrCd();
		if (!lsPltCd.equals(lsOrigPltCd))
			// 	&& (lsRegStkrCd != null && lsRegStkrCd.equals("")))
			// end defect 6820 
		{
			lbRet = true;
		}
		return lbRet;
	}

	/**
	 * Determines if registration will expire within 3 months.
	 *
	 * @return boolean
	 */
	private boolean isRegExpIn3MonthFrame()
	{
		MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();
		int liRegExpMo = laMFVehData.getRegData().getRegExpMo();
		int liRegExpYr = laMFVehData.getRegData().getRegExpYr();
		RTSDate laRTSEffDate =
			(new RTSDate(RTSDate.YYYYMMDD, caVehInqData.getRTSEffDt()));
		int liCurrMo = laRTSEffDate.getMonth();
		int liCurrYr = laRTSEffDate.getYear();
		if ((liCurrYr == liRegExpYr) && ((liRegExpMo - liCurrMo) <= 2))
		{
			return true;
		}
		else if (
			(liCurrYr != liRegExpYr)
				&& ((liRegExpYr - liCurrYr) == 1)
				&& ((liRegExpMo + 12) - liCurrMo <= 2))
		{
			return true;
		}
		return false;
	}

	// defect 10429 
	//	/**
	//	 * Determines if plate code or sticker code has changed.  
	//	 * Returns <code>false</code> if it has not changed.
	//	 *
	//	 * @return boolean
	//	 */
	//	private boolean isRegPltCdAndStkrUnchanged()
	//	{
	//		boolean lbRet = false;
	//
	//		TitleValidObj laTtlValidObj =
	//			(TitleValidObj) caVehInqData.getValidationObject();
	//
	//		MFVehicleData laMFVehOrigData =
	//			(MFVehicleData) laTtlValidObj.getMfVehOrig();
	//
	//		MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();
	//
	//		if (laMFVehOrigData != null)
	//		{
	//			// defect 10290 
	//			// Use .equals() vs. == for String compare 
	//			//		if (laMFVehOrigData.getRegData().getRegPltCd()
	//			//			== laMFVehData.getRegData().getRegPltCd()
	//			//			&& laMFVehOrigData.getRegData().getRegStkrCd()
	//			//				== laMFVehData.getRegData().getRegStkrCd())
	//			if (laMFVehOrigData
	//				.getRegData()
	//				.getRegPltCd()
	//				.equals(laMFVehData.getRegData().getRegPltCd())
	//				&& laMFVehOrigData.getRegData().getRegStkrCd().equals(
	//					laMFVehData.getRegData().getRegStkrCd()))
	//			{
	//				// end defect 10290 
	//				lbRet = true;
	//			}
	//		}
	//		return lbRet;
	//	}
	// end defect 10429 

	/**
	 * Adds the CCO Title to the inventory vector for CCO.
	 * 
	 */
	public void issueCCOTitle()
	{
		ProcessInventoryData laProcInvData = new ProcessInventoryData();
		laProcInvData.setTransEmpId(SystemProperty.getCurrentEmpId());
		laProcInvData.setSubstaId(SystemProperty.getSubStationId());
		laProcInvData.setTransWsId(
			Integer.toString(SystemProperty.getWorkStationId()));
		laProcInvData.setItmCd(CCO_INV_ITM_CD);
		laProcInvData.setInvItmYr(0);
		laProcInvData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		cvInvItemsData.add(laProcInvData);
	}

	/**
	 * Adds the Form 31 to the inventory vector for dealer title.
	 * 
	 */
	private void issueForm31()
	{
		ProcessInventoryData laProcInvData = new ProcessInventoryData();
		laProcInvData.setTransEmpId(SystemProperty.getCurrentEmpId());
		laProcInvData.setSubstaId(SystemProperty.getSubStationId());
		laProcInvData.setTransWsId(
			Integer.toString(SystemProperty.getWorkStationId()));
		laProcInvData.setItmCd(FORM31);
		laProcInvData.setInvItmNo(caDlrTtlData.getForm31No());
		laProcInvData.setInvItmYr(0);
		laProcInvData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		cvInvItemsData.add(laProcInvData);
	}

	/**
	 * Sets up the inventory vector <code> InvItemsData </code> for title application
	 * when no record exists on the mainframe.
	 *
	 */
	private void issueInvNoRecord()
	{
		MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();
		boolean lbNewStkr = false;
		boolean lbNewPlt = false;
		if (laMFVehData.getRegData().getRegStkrCd().equals(""))
		{
			lbNewPlt = true;
		}
		else
		{
			// defect 10623
			// Add check for Token Trailer. Tow Truck (even 
			// though it is not an annual plt, now has no stkr.
//			if (laMFVehData.getRegData().getRegClassCd() != 
//					RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
//			{
				lbNewStkr = true;
//			}
			lbNewPlt = true;
		}
		if (lbNewStkr)
		{
			// Add sticker to inventory vector
			ProcessInventoryData laProcInvData =
				new ProcessInventoryData();
			laProcInvData.setTransEmpId(
				SystemProperty.getCurrentEmpId());
			laProcInvData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laProcInvData.setSubstaId(SystemProperty.getSubStationId());
			laProcInvData.setTransWsId(
				Integer.toString(SystemProperty.getWorkStationId()));
			laProcInvData.setItmCd(
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegStkrCd());
			cvInvItemsData.add(laProcInvData);
		}
		if (lbNewPlt)
		{
			// Add plate to inventory vector
			ProcessInventoryData laProcInvData =
				new ProcessInventoryData();
			laProcInvData.setTransEmpId(
				SystemProperty.getCurrentEmpId());
			laProcInvData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laProcInvData.setSubstaId(SystemProperty.getSubStationId());
			laProcInvData.setTransWsId(
				Integer.toString(SystemProperty.getWorkStationId()));
			laProcInvData.setItmCd(
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegPltCd());
			// defect 9126
			// If Item Number is not null, capture this Special Plate No
			if (caVehInqData.getMfVehicleData().getSpclPltRegisData()
				!= null)
			{
				laProcInvData.setInvItmNo(
					caVehInqData
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getRegPltNo());
				laProcInvData.setInvLocIdCd("U");
			}
			// end defect 9126		
			cvInvItemsData.add(laProcInvData);
		}
	}

	/**
	 * Sets up the inventory vector <code> InvItemsData </code> for 
	 * title application  when a record exists on the mainframe.
	 * 
	 */
	private void issueInvRecordFound()
	{
		MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();

		TitleValidObj laTtlValidObj =
			((TitleValidObj) caVehInqData.getValidationObject());

		MFVehicleData laMFVehOrigData =
			(MFVehicleData) laTtlValidObj.getMfVehOrig();

		RegTtlAddlInfoData laRegAddlData =
			(RegTtlAddlInfoData) laTtlValidObj.getRegTtlAddData();

		// defect 10988
		// Moved here from below
		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(
				laMFVehData.getRegData().getRegPltCd(),
				new RTSDate().getYYYYMMDDDate());
		// end defect 10988

		int liNewPltIndi = laRegAddlData.getNewPltDesrdIndi();

		int liExpMo = laMFVehData.getRegData().getRegExpMo();
		int liExpYr = laMFVehData.getRegData().getRegExpYr();
		boolean lbNewStkr = false;
		boolean lbNewPlt = false;

		// defect 8900
		boolean lbStandardExempt =
			CommonFeesCache.isStandardExempt(
				laMFVehData.getRegData().getRegClassCd());
		// end defect 8900 

		// Clear registration exp year if registration is invalid
		// defect 11136
		// Use ORIGINAL data object
		//if (!(laMFVehData.getRegData().getRegInvldIndi() == 0))
		if (!(laMFVehOrigData.getRegData().getRegInvldIndi() == 0))

		{
			// end defect 11136
			laMFVehData.getRegData().setRegExpYr(0);
			// defect 10988
			// For invalid reg we should issue sticker if not Annual Plt
			// Need this now because 'Invalidate Regis' on Title w/ GDN
			if (laPlateTypeData != null
				&& laPlateTypeData.getAnnualPltIndi() == 0)
			{
				lbNewStkr = true;
			}
			// end defect 10988
		}
		// Set flag to issue new plate and sticker if plate code is tow truck
		if (laMFVehData.getRegData().getRegPltCd().equals(TOWTP))
		{
			lbNewStkr = true;
			lbNewPlt = true;
		}
		// Check if new plates desired check box was checked
		if (liNewPltIndi == 1)
		{
			lbNewPlt = true;
		}
		// Check if new plate code doesn't match original plate code
		if (!(laMFVehData
			.getRegData()
			.getRegPltCd()
			.equals(laMFVehOrigData.getRegData().getRegPltCd())))
		{
			lbNewPlt = true;

			// Check if a sticker is needed
			if (laMFVehData.getRegData().getRegStkrCd() != null
				&& !(laMFVehData.getRegData().getRegStkrCd().equals("")))
			{
				lbNewStkr = true;
			}
		}

		// Check if plate must be changed
		// defect 11264
		// Also issue sticker if new plates desired
		if (laMFVehData.getTitleData().getMustChangePltIndi() == 1
			|| (laRegAddlData.getNewPltDesrdIndi() == 1 
			&& laMFVehData.isPTOEligible()))
			// end defect 11264			
		{
			lbNewPlt = true;
			// Check if a sticker is needed
			if (laMFVehData.getRegData().getRegStkrCd() != null
				&& !(laMFVehData.getRegData().getRegStkrCd().equals("")))
			{
				lbNewStkr = true;
			}
		}
		// Check if new registration class code doesn't match original registration class code
		if (!(laMFVehData.getRegData().getRegClassCd()
			== laMFVehOrigData.getRegData().getRegClassCd()))
		{
			if (laMFVehData.getRegData().getRegStkrCd() != null
				&& !(laMFVehData.getRegData().getRegStkrCd().equals("")))
			{
				lbNewStkr = true;
			}
		}

		// Check if Registration is expired
		//        Leave code for posible future fix, defect 4811
		//  boolean lbRegisExpired = determineRegisExpired(iExpMo,iExpYr,liRTSEffDate);
		// Check if Registration is expired
		if (CommonValidations.isRegistrationExpired(liExpMo, liExpYr))
		{
			// defect 10988
			// Moved above
			//PlateTypeData laPlateTypeData =
			//	PlateTypeCache.getPlateType(
			//		laMFVehData.getRegData().getRegPltCd());
			// end defect 10988
			// defect 8900
			// Check for Standard Exempt (disregard OfcissuanceCd)
			//if (liOffcIssCd == 1
			//	&& liOffcIssNo == SPECIAL_PLATES
			//	&& laMFVehData.getRegData().getRegClassCd() == 39)
			if (lbStandardExempt)
			{
				lbNewPlt = true;
			}
			//end defect 8900

			// Check if plate age warrants issuing a new plate
			// defect 9025 
			// Consider NeedsProgramCd 
			// defect 8901
			// Use new RegPltAge method 
			if ((laPlateTypeData != null)
				&& (laMFVehData.getRegData().getRegPltAge(false)
					>= laPlateTypeData.getMandPltReplAge())
				&& (laPlateTypeData
					.getNeedsProgramCd()
					.equals(PLT_REPLACEMENT_REQUIRED)))
			{
				lbNewPlt = true;
			}
			// end defect 8901
			// end defect 9025 

			// Set flag to issue new plate if annual plate	
			if (laPlateTypeData != null
				&& laPlateTypeData.getAnnualPltIndi() == 1)
			{
				lbNewPlt = true;
			}
			else
			{
				if (laMFVehData.getRegData().getRegStkrCd() != null
					&& !laMFVehData.getRegData().getRegStkrCd().equals(
						""))
				{
					lbNewStkr = true;
				}
			}
			// end defect 9085 
		}
		else // Registration is current
			{
			String lsOthrSt =
				laMFVehData.getTitleData().getOthrStateCntry();

			// Check if title was surrendered to another state
			if ((laMFVehData.getTitleData().getSurrTtlDate() > 0)
				|| !(lsOthrSt == null || lsOthrSt.equals("")))
			{
				lbNewPlt = true;
				laMFVehData.getRegData().setRegInvldIndi(1);

				if (laMFVehData.getRegData().getRegStkrCd() != null
					&& !(laMFVehData
						.getRegData()
						.getRegStkrCd()
						.equals("")))
				{
					lbNewStkr = true;
				}
			}
			// Check if registration will expire within 3 months
			if (isRegExpIn3MonthFrame())
			{
				// defect 10988
				// Moved above
				//PlateTypeData laPlateTypeData =
				//	PlateTypeCache.getPlateType(
				//		laMFVehData.getRegData().getRegPltCd(),
				//		new RTSDate().getYYYYMMDDDate());
				// end defect 10988

				// defect 8900
				// Check for Standard Exempt (disregard OfcissuanceCd)
				//if (liOffcIssCd == 1
				//	&& liOffcIssNo == SPECIAL_PLATES
				//	&& laMFVehData.getRegData().getRegClassCd() == 39)
				if (lbStandardExempt)
				{
					lbNewPlt = true;
				}
				// end defect 8900

				// defect 9025 
				// Consider NeedsProgramCd 
				// defect 8901
				// Use new RegPltAge method 
				if ((laPlateTypeData != null)
					&& (laMFVehData.getRegData().getRegPltAge(false)
						>= laPlateTypeData.getMandPltReplAge())
					&& (laPlateTypeData
						.getNeedsProgramCd()
						.equals(PLT_REPLACEMENT_REQUIRED)))
				{
					lbNewPlt = true;
				}
				// end defect 8901
				// end defect 9025  

				// Set flag to issue new plate if annual plate	
				if (laPlateTypeData != null
					&& laPlateTypeData.getAnnualPltIndi() == 1)
				{
					lbNewPlt = true;
				}
				else
				{
					if (laMFVehData.getRegData().getRegStkrCd() != null
						&& !laMFVehData
							.getRegData()
							.getRegStkrCd()
							.equals(
							""))
					{
						lbNewStkr = true;
					}
				}
			}
			// end defect 9085 
		}
		// defect 10623
		// Add check for Token Trailer. Tow Truck (even 
		// though it is not an annual plt, now has no stkr.
		if (lbNewStkr)
//		if (lbNewStkr &&
//			laMFVehData.getRegData().getRegClassCd() != 
//				RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
		{
			// end defect 10623
			// Add sticker to inventory vector
			ProcessInventoryData laProcInvData =
				new ProcessInventoryData();
			laProcInvData.setTransEmpId(
				SystemProperty.getCurrentEmpId());
			laProcInvData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laProcInvData.setSubstaId(SystemProperty.getSubStationId());
			laProcInvData.setTransWsId(
				Integer.toString(SystemProperty.getWorkStationId()));
			laProcInvData.setItmCd(
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegStkrCd());
			cvInvItemsData.add(laProcInvData);
		}
		if (lbNewPlt)
		{
			// Add plate to inventory vector
			ProcessInventoryData laProcInvData =
				new ProcessInventoryData();
			laProcInvData.setTransEmpId(
				SystemProperty.getCurrentEmpId());
			laProcInvData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laProcInvData.setSubstaId(SystemProperty.getSubStationId());
			laProcInvData.setTransWsId(
				Integer.toString(SystemProperty.getWorkStationId()));
			laProcInvData.setItmCd(
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegPltCd());
			// defect 9126
			// If Item Number is not null, capture this Special Plate No
			if (caVehInqData.getMfVehicleData().getSpclPltRegisData()
				!= null)
			{
				laProcInvData.setInvItmNo(
					caVehInqData
						.getMfVehicleData()
						.getSpclPltRegisData()
						.getRegPltNo());
				laProcInvData.setInvLocIdCd("U");
			}
			// end defect 9126		
			cvInvItemsData.add(laProcInvData);
		}
	}
	/**
	 * Adds the new plate to the inventory vector for dealer title.
	 * 
	 */
	private void issueNewPlate()
	{
		// defect 8901
		//int liRegPltAge =
		//	caVehInqData.getMfVehicleData().getRegData().getRegPltAge();
		int liRegPltAge =
			caVehInqData.getMfVehicleData().getRegData().getRegPltAge(
				false);
		// end defect 8901

		TitleValidObj laTtlValidObj =
			((TitleValidObj) caVehInqData.getValidationObject());

		RegTtlAddlInfoData caRegAddlData =
			(RegTtlAddlInfoData) laTtlValidObj.getRegTtlAddData();

		int liNewPltIndi = caRegAddlData.getNewPltDesrdIndi();

		// defect 9025
		// Consider NeedsProgramCd
		String lsRegPltCd =
			caVehInqData.getMfVehicleData().getRegData().getRegPltCd();
		String lsNewPlt = caDlrTtlData.getNewPltNo();

		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(lsRegPltCd);

		// defect 9047 
		// Remove check for valid lsNewPlt 
		// defect 7538
		// Issue new plate if: 
		//  - Not OffHwyUse
		//  - And Either
		//        - New Plates Required (Regis_Renewals)
		//        - New Plate specified on DTA008  (lsNewPlt)
		//        - NewPltIndi = 1  (from TTL008, New Plates Desired)
		//        - Plate Changed from prior registration 

		if ((caVehInqData
			.getMfVehicleData()
			.getRegData()
			.getOffHwyUseIndi()
			!= 1)
			&& ((laPlateTypeData
				.getNeedsProgramCd()
				.equals(PLT_REPLACEMENT_REQUIRED)
				&& liRegPltAge >= laPlateTypeData.getMandPltReplAge())
				|| (lsNewPlt != null && !lsNewPlt.equals(""))
				|| liNewPltIndi == 1
				|| isNewPltStkr_IfRegPltChg()))
			// end defect 7538
			// end defect 9085 
		{
			ProcessInventoryData laProcInvData =
				new ProcessInventoryData();
			laProcInvData.setTransEmpId(
				SystemProperty.getCurrentEmpId());
			laProcInvData.setSubstaId(SystemProperty.getSubStationId());
			laProcInvData.setTransWsId(
				Integer.toString(SystemProperty.getWorkStationId()));
			laProcInvData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laProcInvData.setItmCd(lsRegPltCd);
			laProcInvData.setInvItmNo(caDlrTtlData.getNewPltNo());
			cvInvItemsData.add(laProcInvData);
		}
		// end defect 9025
		// end defect 9047  
	}

	/**
	 * Adds the new sticker to the inventory vector for dealer title.
	 * 
	 */
	private void issueNewRegStckrForDlr()
	{
		ProcessInventoryData laProcInvData = new ProcessInventoryData();
		MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();
		laProcInvData.setTransEmpId(SystemProperty.getCurrentEmpId());
		laProcInvData.setSubstaId(SystemProperty.getSubStationId());
		laProcInvData.setTransWsId(
			Integer.toString(SystemProperty.getWorkStationId()));
		laProcInvData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laProcInvData.setItmCd(laMFVehData.getRegData().getRegStkrCd());
		laProcInvData.setInvItmNo(CommonConstant.STR_SPACE_EMPTY);
		int liYr = caDlrTtlData.getNewRegExpYr();
		laProcInvData.setInvItmYr(liYr);
		// only add item if it needs to get populated
		if (laProcInvData.getItmCd() != null
			&& !laProcInvData.getItmCd().equals(""))
		{
			cvInvItemsData.add(laProcInvData);
		}
	}

	//	/**
	//	 * Checks the validity of the new registration expiration month and year
	//	 * entered for dealer title.
	//	 *
	//	 * @return boolean
	//	 */
	//	private boolean isValidNewExpMoYr()
	//	{
	//		boolean lbRet = true;
	//		if (caDlrTtlData != null)
	//		{
	//			// defect 7496
	//			// NewRegExpMo and NewRegExpYr were converted to integer
	//			//String strMo = dlrTtlData.getNewRegExpMo();
	//			//String strYr =  dlrTtlData.getNewRegExpYr();
	//			//if(strMo != null && strYr != null)
	//			//{
	//			//	if(strMo!= null)
	//			//	{
	//			//		try
	//			//		{
	//			//			int iMo = Integer.parseInt(strMo);
	//			//			int iYr = Integer.parseInt(strYr);
	//			//			if(iMo <0 || iMo >12)
	//			//				bRet = false;
	//			//			if(iYr < 1)
	//			//				bRet = false;
	//			//		}
	//			//		catch(NumberFormatException e)
	//			//		{
	//			//			bRet = false;
	//			//		}
	//			//	}
	//			int liMo = caDlrTtlData.getNewRegExpMo();
	//			int liYr = caDlrTtlData.getNewRegExpYr();
	//			if (liMo < 0 || liMo > 12)
	//			{
	//				lbRet = false;
	//			}
	//			if (liYr < 1)
	//			{
	//				lbRet = false;
	//			}
	//		}
	//		return lbRet;
	//	}

	/**
	 * Sets the special plate in progress indicator.
	 *
	 */
	private void setSpclPltProgIndi()
	{
		MFVehicleData laMFVehData = caVehInqData.getMfVehicleData();

		TitleValidObj laTtlValidObj =
			(TitleValidObj) caVehInqData.getValidationObject();

		VehMiscData laVehMiscData = caVehInqData.getVehMiscData();

		String lsRegPltCd = laMFVehData.getRegData().getRegPltCd();

		MFVehicleData laMFVehDataOrig =
			(MFVehicleData) laTtlValidObj.getMfVehOrig();

		String lsRegPltCdOrig =
			laMFVehDataOrig.getRegData().getRegPltCd();

		laVehMiscData.setSpclPltProgIndi(0);

		// defect 10290 		
		if ((!csTransCd.equals(TransCdConstant.REJCOR))
			|| ((csTransCd.equals(TransCdConstant.REJCOR))
				&& (!lsRegPltCd.equals(lsRegPltCdOrig))))
		{
			if (!SystemProperty.isHQ())
			{
				laVehMiscData.setSpclPltProgIndi(1);
			}
		}
		// end defect 10290 
	}
}
