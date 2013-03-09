package com.txdot.isd.rts.client.registration.business;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.RegistrationClassCache;
import com.txdot.isd.rts.services.common.Fees;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * RegistrationClientUtilityMethods.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Kwik&Ting	05/15/2002	Do not setAllocatedInv in 
 * 							certain transaction scenario
 * 							defect 3919
 * K. Harrell 	07/23/2002	Present Replacement msg	for DPP/DPMCP only 
 * 							if replace plate
 * 							defect 4503 
 * Ray Rowehl	02/08/2005	Change import for Fees
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * B Hargrove	06/23/2005	Modify for move to Java 1.4. 
 * 							Format, Hungarian notation for variables. 
 * 							Bring code to standards. Remove unused 
 *  	 					variables, methods. Add If-Else braces.
 * 							delete laVehData 
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	06/27/2005	Refactor\Move 
 * 							RegistrationClientUtilityMethods class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.client.reg.business.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	06/28/2005	Refactor\Move
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * K Harrell	10/20/2006	Exempts work; Created/Moved methods to 
 * 							share between RegClientBusiness & UI
 * 							add TOKEN_TRAILER
 * 							add procsEmissions(),procsTokenTrailer(),
 * 							procsHvyVehUseTaxVerify()
 * 							defect 8900 Ver Exempts
 * K Harrell	10/30/2006	Verify Heavy Use is not required if Exempt
 * 							modify procsHvyVehUseTaxVerify()
 * 							defect 8900 Ver Exempts
 * K Harrell	03/03/2007	No longer prompt for alternate replacements
 * 							for disabled vehicles.  Handled by REG011.
 * 							delete procsDsabldPersnPlt()
 * 							delete DISABLED_PERSON_PLT, 
 * 							 DISABLED_MOTORCYCLE_PLT, MSG_DYWT_REPLACE
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/04/2007	Add method to determine if expired Special
 * 							Plate.
 * 							add isExpiredSpclPlt()
 * 							defect 9085 Ver Special Plates 		 
 * B Hargrove	04/13/2007	Add Plate Number string to addItmCdToInv().
 * 							String will be null except when coming from 
 *  						FrmRegistrationREG003.prepInv() (Special 
 * 							Plate number has already been entered
 * 							on FrmSpecialPlateInquiryKEY002). This will 
 * 							keep the INV007 screen from asking for the
 * 							plate number a second time when plate is not
 * 							customer supplied.
 * 							modify addItmCdToInv()
 * 							defect 9126 Ver Special Plates
 * K Harrell	05/10/2007	Working ...
 * K Harrell	10/14/2007	Assign value for Plate Transfer Fee to 
 * 							RegTtlAddlInfoData from 
 * 							RegistrationValidationData for fee processing
 * 							delete procsDsabldPersnPlt()
 * 							add procsPltTrnsfrFee()  
 * 							modify prepFees()
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	03/11/2008	Do not created TR_INV_DETAIL records w/ RNR 
 * 							modify prepFees()
 * 							defect 7085 Ver Defect POS A 
 * K Harrell	01/07/2009  Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify isExpiredSpclPlt()  
 *        					defect 9864 Ver Defect_POS_D
 * Min Wang		07/28/2010	Add processing verify tow truck mask.
 * 							add procsVerifyTowTruckMask()
 * 							defect 10007 Ver 6.5.0
 * Min Wang		09/14/2010	Fix null point when change the vehicle to 
 * 							the incorrect vehicle class for the truck.
 * 							modify procsVerifyTowTruckMask()
 * 							defect 10591 Ver 6.6.0
 * K Harrell	10/10/2011	add procsPTOTrnsfrEligible()
 * 							delete procsPltTrnsfrFee() 
 * 							modify prepFees() 
 * 							defect 11030 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * Registration Client Utility Methods.
 *
 * @version	6.9.0 			10/10/2011
 * @author	Joseph Kwik
 * <br>Creation Date:		11/01/2001 14:50:39
 */

public class RegistrationClientUtilityMethods
{
	private static final int EIGHTEEN_EIGHTY = 1880;
	private final static int TOKEN_TRAILER = 33;

	/**
	 * RegistrationClientUtilityMethods constructor.
	 */
	public RegistrationClientUtilityMethods()
	{
		super();
	}
	/**
	 * 
	 * Manage adding item code to Inventory items vector.
	 * 
	 * @param aaRegValidData
	 * @param asItmCd
	 */

	public static void addItmCdToInv(
		RegistrationValidationData aaRegValidData,
		String asItmCd)
	{
		addItmCdToInv(aaRegValidData, asItmCd, null);

	}
	/**
	 * Manage adding item code to Inventory items vector.
	 * 
	 * @param aaRegValidData RegistrationValidationData
	 * @param asItmCd String Item Code
	 * @param asInvItmNo String Item Code (added defect 9126)
	 */
	public static void addItmCdToInv(
		RegistrationValidationData aaRegValidData,
		String asItmCd,
		String asInvItmNo)
	{
		Vector lvInvItms = aaRegValidData.getInvItms();

		if (lvInvItms == null)
		{
			lvInvItms = new Vector();
		}

		ProcessInventoryData alInvData = new ProcessInventoryData();
		alInvData.setTransEmpId(SystemProperty.getCurrentEmpId());
		alInvData.setSubstaId(SystemProperty.getSubStationId());
		alInvData.setTransWsId(
			Integer.toString(SystemProperty.getWorkStationId()));
		alInvData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		alInvData.setItmCd(asItmCd);
		// defect 9126
		// If Item Number is not null, capture this Special Plate No
		if (asInvItmNo != null)
		{
			alInvData.setInvItmNo(asInvItmNo);
			alInvData.setInvLocIdCd("U");
		}
		// end defect 9126		
		lvInvItms.add(alInvData);
		aaRegValidData.setInvItms(lvInvItms);
	}
	/**
	 * Determine Emissions
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @return RegistrationValidData
	 */
	public static void procsEmissions(VehicleInquiryData aaVehInqData)
	{
		// defect 8516/8900
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();

		// Do not add for REPL, DUPL, MODIFY (CORREG) 
		if (!(laRegValidData
			.getTransCode()
			.equals(TransCdConstant.REPL))
			&& !(laRegValidData
				.getTransCode()
				.equals(TransCdConstant.DUPL))
			&& !(laRegValidData
				.getTransCode()
				.equals(TransCdConstant.CORREG)
				&& laRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_REG))
		{
			int liRegClassCd =
				aaVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegClassCd();

			// Note Exempt Status as enabled and set indicator
			boolean lbChargeFee = laRegValidData.getChrgFeeIndi() == 1;
			boolean lbExempt = laRegValidData.getExemptIndi() == 1;

			// Determine Emissions	
			int liRTSCurrDate = new RTSDate().getYYYYMMDDDate();
			CommonFeesData laCommFeesData =
				CommonFeesCache.getCommonFee(
					liRegClassCd,
					liRTSCurrDate);
			boolean lbEmissions = false;
			if (laCommFeesData != null)
			{
				lbEmissions =
					(laCommFeesData.getEmissionsPrcnt()).compareTo(
						CommonConstant.ZERO_DOLLAR)
						> 0;
			}

			// Set Mask true if Emissions  
			laRegValidData.setEmissionsFeeMask(lbEmissions);

			// Set Indicator if Emissions && !Exempt
			laRegValidData.setEmissionsFeeIndi(
				(lbEmissions && (!lbExempt || lbChargeFee)) ? 1 : 0);
		}
		// end defect 8516/8900 
	}
	/**
	 * Determine if Reg Exp Yr Invalid
	 *  
	 * @return boolean
	 * @param asYr String Year
	 */
	public static boolean isInvalidRegExpYr(String asYr)
	{
		String lsYearModel = asYr;
		boolean lbRetVal = false;

		try
		{
			int liNum = Integer.parseInt(lsYearModel);
			if (liNum <= EIGHTEEN_EIGHTY
				|| liNum > (RTSDate.getCurrentDate().getYear() + 5))
			{
				lbRetVal = true;
			}
			else
				lbRetVal = false;
			{
				return lbRetVal;
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			return true;
		}
	}
	/**
	 * Determine if Expired Special Plate
	 * 
	 * @return booelan 
	 */
	public static boolean isExpiredSpclPlt(VehicleInquiryData aaVehicleInquiryData)
	{
		boolean lbExpSpclPlt = false;

		SpecialPlatesRegisData laSpclPltRegisData =
			aaVehicleInquiryData
				.getMfVehicleData()
				.getSpclPltRegisData();

		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
		if (laSpclPltRegisData != null)
		{
			lbExpSpclPlt =
				CommonValidations.isRegistrationExpired(
					laSpclPltRegisData.getPltExpMo(),
					laSpclPltRegisData.getPltExpYr());
		}
		// end defect 9864 

		return lbExpSpclPlt;
	}
	/**
	 * Prepare data for fees calculation.
	 *  
	 * @return CompleteTransactionData
	 * @param aaVehInqData VehicleInquiryData Updated VehicleInquiryData
	 * @param aaOrigVehInqData VehicleInquiryData Original 
	 * 	VehicleInquiryData
	 */
	public static CompleteTransactionData prepFees(
		VehicleInquiryData aaVehInqData,
		VehicleInquiryData aaOrigVehInqData)
	{
		VehicleInquiryData laVehInqData = aaVehInqData;
		MFVehicleData laMFVehData = laVehInqData.getMfVehicleData();
		RegistrationData laRegData =
			(RegistrationData) laMFVehData.getRegData();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) laVehInqData
				.getValidationObject();

		RegTtlAddlInfoData laRegTtlAddlInfoData =
			new RegTtlAddlInfoData();
		laRegTtlAddlInfoData.setProcsByMailIndi(
			laRegValidData.getProcsdByMailIndi());
		laRegTtlAddlInfoData.setNewPltDesrdIndi(
			laRegValidData.getNewPltReplIndi());
		laRegTtlAddlInfoData.setChrgFeeIndi(
			laRegValidData.getChrgFeeIndi());
		laRegTtlAddlInfoData.setChrgAddlTknTrlrFeeIndi(
			laRegValidData.getAddlToknFeeIndi());
		aaVehInqData
			.getMfVehicleData()
			.getRegData()
			.setHvyVehUseTaxIndi(
			laRegValidData.getHvyVehUseTaxIndi());

		laRegTtlAddlInfoData.setRegExpiredReason(
			laRegValidData.getRegExpiredReason());
		laRegTtlAddlInfoData.setApprhndFndsCntyNo(
			laRegValidData.getApprndComptCntyNo());
		laRegData.setApprndCntyNo(
			laRegValidData.getApprndComptCntyNo());
		laRegTtlAddlInfoData.setCorrtnEffMo(
			laRegValidData.getCorrtnEffMo());
		laRegTtlAddlInfoData.setCorrtnEffYr(
			laRegValidData.getCorrtnEffYr());
		laRegTtlAddlInfoData.setRegExpiredReason(
			laRegValidData.getRegExpiredReason());
		if (laRegValidData.getEmissionsFeeIndi() == 0)
		{
			laRegTtlAddlInfoData.setNoChrgRegEmiFeeIndi(1);
		}
		else
		{
			laRegTtlAddlInfoData.setNoChrgRegEmiFeeIndi(0);
		}
		// defect 11030 
		
		// defect 9368 
		// Assign ChrgPltTrnsfrFeeIndi from RegValidData as 
		// Fees uses RegTtlAddlInfoData
		// laRegTtlAddlInfoData.setChrgPltTrnsfrFeeIndi(
		//		laRegValidData.getChrgPltTrnsfrFeeIndi());
		// end defect 9368
		laRegTtlAddlInfoData.setPTOTrnsfrIndi(
			laRegValidData.getPTOTrnsfrIndi());
		// end defect 11030 

		CompleteTransactionData laCompTransData =
			new CompleteTransactionData();
		laCompTransData.setRegTtlAddlInfoData(laRegTtlAddlInfoData);
		laCompTransData.setOrgVehicleInfo(
			aaOrigVehInqData.getMfVehicleData());
		laCompTransData.setVehicleInfo(laMFVehData);
		laCompTransData.setInvItms(laRegValidData.getInvItms());
		laCompTransData.setRegPnltyChrgIndi(
			laRegValidData.getRegPnltyChrgIndi());
		laCompTransData.setVehMisc(laVehInqData.getVehMiscData());

		// defect 7085
		// add check for !RNR 
		if (!(laRegValidData
			.getTransCode()
			.equals(TransCdConstant.DUPL)
			|| laRegValidData.getTransCode().equals(TransCdConstant.ADDR)
			|| laRegValidData.getTransCode().equals(TransCdConstant.PAWT)
			|| laRegValidData.getTransCode().equals(TransCdConstant.RNR)
			|| laRegValidData.getTransCode().equals(
				TransCdConstant.CORREG)))
		{
			laCompTransData.setAllocInvItms(
				laRegValidData.getSameVehAllocInvItms());
		}
		// end defect 7085

		laCompTransData.setNoMFRecs(laVehInqData.getNoMFRecs());

		// set inventory item count
		// defect 9085
		if (laRegValidData.getInvItms() == null)
		{
			laCompTransData.setInvItemCount(0);
		}
		else
		{
			laCompTransData.setInvItemCount(
				laRegValidData.getInvItms().size());
		}
		//		if (laRegValidData.getTransCode().equals(TransCdConstant.DUPL)
		//			|| laRegValidData.getTransCode().equals(TransCdConstant.PAWT)
		//			|| laRegValidData.getTransCode().equals(
		//				TransCdConstant.CORREG)
		//			|| laRegValidData.getTransCode().equals(
		//				TransCdConstant.CORREGX))
		//		{
		//			laCompTransData.setInvItemCount(0);
		//		}
		//		else if  
		//		 (
		//			laRegValidData.getTransCode().equals(TransCdConstant.RENEW)
		//				|| laRegValidData.getTransCode().equals(
		//					TransCdConstant.EXCH)
		//				|| laRegValidData.getTransCode().equals(
		//					TransCdConstant.REPL))
		//		{
		//			laCompTransData.setInvItemCount(
		//				laRegValidData.getInvItms().size());
		//		}
		// end defect 9085

		laCompTransData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());

		if (!laRegValidData.getTransCode().equals(TransCdConstant.ADDR)
			&& !laRegValidData.getTransCode().equals(TransCdConstant.RNR))
		{
			Fees laFees = new Fees();
			laCompTransData =
				laFees.calcFees(
					laRegValidData.getTransCode(),
					laCompTransData);
		}
		else
		{
			// ADDR or RNR
			laCompTransData.setTransCode(laRegValidData.getTransCode());
		}

		// If CORREGX then reset to CORREG
		if (laRegValidData
			.getTransCode()
			.equals(TransCdConstant.CORREGX))
		{
			laCompTransData.setTransCode(TransCdConstant.CORREG);
		}
		return laCompTransData;
	}

	/**
	 * Determine if Heavy Vehicle Use Tax Verify required.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 */
	public static void procsHvyVehUseTaxVerify(VehicleInquiryData aaVehInqData)
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();

		RegistrationClassData laRegClassData =
			RegistrationClassCache.getRegisClass(
				aaVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVehClassCd(),
				laRegData.getRegClassCd(),
				aaVehInqData.getRTSEffDt());

		if (laRegClassData == null)
		{
			laRegClassData = new RegistrationClassData();
		}
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();

		if (laRegClassData.getHvyVehUseWt() > 0)
		{
			laRegValidData.setHvyVehUseTaxMask(true);
			// defect 8900
			// Not required if Exempt 
			if ((laRegData.getVehGrossWt()
				>= laRegClassData.getHvyVehUseWt())
				&& laRegData.getExmptIndi() != 1)
			{
				laRegValidData.setHvyVehUseTaxRequired(1);
			}
			else
			{
				laRegValidData.setHvyVehUseTaxRequired(0);
			}
			// end defect 8900 
		}
		else
		{
			laRegValidData.setHvyVehUseTaxRequired(0);
			// defect 8404
			// Reset variables when HvyVehUseTaxRequired == 0 
			laRegValidData.setHvyVehUseTaxIndi(0);
			laRegValidData.setHvyVehUseTaxMask(false);
			// end defect 8404 
		}
	}
	/**
	 * Determine if Tow Truck.  Set mask accordingly.
	 * 
	 * @param aaRegValidData
	 * @param aaRegClassData
     */
		public static void procsVerifyTowTruckMask(RegistrationValidationData aaRegValidData, RegistrationClassData aaRegClassData)
		{
			// defect 10591
			if (aaRegClassData != null
			&& (aaRegClassData.getRegClassCd() == 46
				|| aaRegClassData.getRegClassCd() == 47))
			{
				aaRegValidData.setVerifyTowTruckCertMask(true); 
			}
			else
			{
				aaRegValidData.setVerifyTowTruckCertMask(false); 			
			}
			// end defect 10591
		}
	

	/**
	 * Determine if Token Trailer.  Set mask accordingly.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 */
	public static void procsTokenTrailer(VehicleInquiryData aaVehInqData)
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();

		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();

		// Determine if Additional Token Trailer Fee should be enabled
		laRegValidData.setAddlToknFeeMask(
			laRegData.getRegClassCd() == TOKEN_TRAILER);

		laRegValidData.setAddlToknFeeIndi(0);
	}
	/**
	 * Determine if Plate to Owner Eligible. Set mask accordingly.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 */
	public static void procsPTOTrnsfrEligible(VehicleInquiryData aaVehInqData)
	{
		boolean lbPTOEligible =
			aaVehInqData.getMfVehicleData().isPTOEligible();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();
		laRegValidData.setPTOTrnsfrMask(lbPTOEligible);
		if (!lbPTOEligible)
		{
			laRegValidData.setPTOTrnsfrIndi(0);
		}
	}
	// defect 11030 
	//	/**
	//	* Determine if Plate to Owner Applicable. Set mask accordingly.
	//	* 
	//	* @param aaVehInqData VehicleInquiryData
	//	*/
	//	public static void procsPltTrnsfrFee(VehicleInquiryData aaVehInqData)
	//	{
	//	boolean lbPTOEligible =
	//	aaVehInqData.getMfVehicleData().isPTOEligible();
	//	RegistrationValidationData laRegValidData =
	//	(RegistrationValidationData) aaVehInqData
	//	.getValidationObject();
	//	laRegValidData.setChrgPltTrnsfrFeeMask(lbPTOEligible);
	//	if (!lbPTOEligible)
	//	{
	//	laRegValidData.setChrgPltTrnsfrFeeIndi(0);
	//	}
	//	}
	// end defect 11030 

}
