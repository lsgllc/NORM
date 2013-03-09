package com.txdot.isd.rts.client.registration.business;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.ClassToPlateCache;
import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * RegistrationSpecialExemptions.java
 * 
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * J Kwik		09/19/2001	Modify per first code review.
 * MAbs			05/14/2002	Checked for Null Pointers 
 * 							defect 3910
 * J Kwik		05/23/2002	handle plate with no sticker 
 * 							code in testReplSpclExmpt().
 * 							defect 4075  
 * S Govindappa 07/26/2002	Added a null pointer 
 * 							check in testReplSpclExmpt method for 
 * 							RegistrationRenewalsData object
 * 							defect 4993 
 * B Brown     	11/07/2002	Change testReplSpclExmpt
 * 							method to do an OR check, instead of and for
 *                          registration expired, OR Annual plate ind 
 * 							!= 0, OR regclasscd = EXEMPT.
 * 							defect 4993 
 * B Brown     	01/15/2003	Fixed defect 4993 again Changed 
 * 							testReplSpclExmpt method  to do an OR check,
 * 							inside of the if (lRegValidData.
 * 							getRegistrationExpired() == 1) reg expired
 * 							check. OR check: if getAnnualPltIndi() != 0,
 * 							OR getRegClassCd() == EXEMPT.
 * K Harrell   	04/23/2003  defect 5974 - Process Exempt Plates in 
 *                          Replacement method testReplSpclExmpt 
 * K Harrell   	04/23/2003  defect 5978 - testExchSpclExmpt - do not 
 * 							check if HQ
 *                         	modify testExchSpclExmpt()
 * B Hargrove	03/09/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							defect 7894 Ver 5.2.3
 * B Hargrove	04/28/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7894 Ver 5.2.3
 * K Harrell	06/14/2005	ClassToPlate, PlateToSticker Implementation
 * 							modify testReplSpclExmpt()
 * 							defect 8218 Ver 5.2.3  
 * B Hargrove	06/15/2005	Comment out unused variables.
 * 							defect 7894 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                 
 * B Hargrove	07/15/2005	Refactor\Move
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * K Harrell	12/12/2005	Use aaVehInquiry.getRTSEffDate vs. 
 * 							laRegData.getRegEffDate for Class To Plt 
 * 							cache inquiry. 
 * 							modify testReplSpclExmpt()  
 * 							defect 8218 Ver 5.2.3 
 * B Hargrove 	10/07/2006 	Remove check to verify RegClassCd because
 * 	K Harrell				counties now handle Exempts. Do not allow 
 * 							Renew when Reg Period Length = 0.
 * 							Use CommonFeesCache.isStandardExempt() 
 * 							modify testRenwlSpclExmpt, testSpclExmpts()
 * 							delete EXEMPT constant
 * 							defect 8900  Ver 5.3.0
 * K Harrell	02/05/2007	Use PlateTypeCache vs. 
 *  							RegistrationRenewalsCache
 * 							modify testReplSpclExmpt()
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/27/2007	Use PlateTypeCache vs. ItemCodesData to 
 * 							determine if plate out of scope.  
 * 							modify verifyPltTypeScope()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/04/2007	Do not allow expired Annual Special Plate
 * 							to be replaced. Note: The Special Plate can
 * 							be expired even when the registration is 
 * 							current.
 * 							modify testReplSpclExmpt()
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/09/2007	Implemented SystemProperty.isHQ(),
 * 							SystemProperty.getOfficeIssuanceCd()
 * 							modify testExchSpclExmpt(),
 * 							verifyPltTypeScope()	
 * 							defect 9085 Ver Special Plates
 * K Harrell	03/29/2007	If Registration is Invalid or invalid
 * 							class/plate/sticker combo, set
 * 							SpecialPlateRegisData
 * 							modify testRenwlSpclExmpt()
 * 						   	defect 9085 Ver Special Plates
 * K Harrell	05/29/2007	made all calls to RegistrationVerify 
 * 							methods static. Add call to 
 * 							verifyValidSpclPlt  
 * 							modify testRenwlSpclExmpt(), testReplSpclExmpt()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/11/2007	Do not allow replacement of Expired 
 * 							Special Plate where the ReplPltCd = 
 * 							RegPltcd.
 * 							modify testReplSpclExmpt()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/04/2007	Add check for unlinked Special Plate for  
 * 							Address Change, PAWT
 * 							add testAddrSpclExmpt() 
 * 							modify testPawtSpclExmpt()
 * 							defect 9085 Ver Special Plates
 * K Harrell	10/31/2007	Corrected transcd, from PAWT to ADDR 
 * 							modify testAddrSpclExmpt()
 * 							defect 9400 Ver Special Plates  
 * ---------------------------------------------------------------------
 */
/** 
 * RegistrationSpecialExemptions class is responsible for management of 
 * Special Exemptions for various Registration events.
 * 
 * @version	Special Plates	10/31/2007
 * @author	Joseph Kwik
 * <br>Creation Date:		09/10/2001 20:26:00 
*/

public class RegistrationSpecialExemptions
{
	// Constants
	// APPORTIONED TRUCK PLT 
	private final static String APPRTK = "APPRTK";
	// APPORTIONED TRL PLT
	private final static String APPRTR = "APPRTR";
	private final static int ANTIQUE = 4;
	private final static int ANTIQUE_MOTORCYCLE = 5;

	/**
	 * RegistrationSpecialExemptions constructor comment.
	 */
	public RegistrationSpecialExemptions()
	{
		super();
	}

	/**
	 * Assign RTS Effective Date based on the TransCode.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @param asTransCode String 
	 * @throws RTSException  
	 */
	private void setRTSEffectiveDate(
		VehicleInquiryData aaVehInqData,
		String asTransCode)
		throws RTSException
	{
		if (asTransCode.equals(TransCdConstant.RENEW))
		{
			int liRegDate =
				aaVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegEffDt();
			if (liRegDate > 0)
			{
				RTSDate laRegEffDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						aaVehInqData
							.getMfVehicleData()
							.getRegData()
							.getRegEffDt());
				if ((laRegEffDate.compareTo(RTSDate.getCurrentDate()))
					>= 0)
				{
					RTSDate laNewRegEffDt =
						laRegEffDate.add(RTSDate.DATE, 1);
					aaVehInqData.setRTSEffDt(
						laNewRegEffDt.getYYYYMMDDDate());
				}
				else
				{
					aaVehInqData.setRTSEffDt(
						RTSDate.getCurrentDate().getYYYYMMDDDate());
				}
			}
			else
			{
				aaVehInqData.setRTSEffDt(
					RTSDate.getCurrentDate().getYYYYMMDDDate());
			}
		}
		else
		{
			aaVehInqData.setRTSEffDt(
				RTSDate.getCurrentDate().getYYYYMMDDDate());
		}

	}

	/**
	 * Performs special exemptions testing of correction event.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @throws RTSException 
	 */
	private void testCorrtnSpclExmpt(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		// Verify valid regis and vehicle class combination is valid
		int liRetVal =
			RegistrationVerify.verifyRegVehClassCombination(
				aaVehInqData);

		if (liRetVal == -1)
		{
			throw new RTSException(730);
		}
	}

	/**
	 * Performs special exemptions testing of duplicate receipt event.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @throws RTSException 
	 */
	private void testDuplSpclExmpt(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();

		// Determine duplicate receipt special exemptions
		if (laRegData.getRegPltCd().equals(APPRTK)
			|| laRegData.getRegPltCd().equals(APPRTR))
		{
			throw new RTSException(390);
		}
		else if (laRegValidData.getRegistrationExpired() == 1)
		{
			throw new RTSException(58);
		}
	}

	/**
	 * Performs special exemptions testing of Exchange event.
	 * 
	 * @param aaVehicleInqData  VehicleInquiryData
	 * @throws RTSException 
	 */
	private void testExchSpclExmpt(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();

		// If workstation is not located at HEADQUARTERS 
		// defect 5978
		// Perform no tests if HQ. Just omit test for expired regis
		// defect 9085 
		//if (!(UtilityMethods.isHeadquarters()))
		if (!SystemProperty.isHQ())
		{
			if (laRegValidData.getRegistrationExpired() == 1)
			{
				throw new RTSException(58);
			}
			// Test if registration class is Antique
			if (laRegData.getRegClassCd() == ANTIQUE
				|| laRegData.getRegClassCd() == ANTIQUE_MOTORCYCLE)
			{
				laRegData.setUnregisterVehIndi(1);
			}
			else
			{
				laRegData.setUnregisterVehIndi(0);
			}
			int liRetVal = verifyPltTypeScope(laRegData);
			if (liRetVal == -1)
			{
				throw new RTSException(741);
			}
		}
		// end defect 5978 
		// end defect 9085
	}
	/**
	 * 
	 * Performs special exemptions testing of Address Change
	 * 
	 * @param aaVehInqData
	 * @throws RTSException
	 */
	private void testAddrSpclExmpt(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		// defect 9400 
		// Wrong transcd! 
		RegistrationVerify.verifyValidSpclPlt(
			aaVehInqData,
			TransCdConstant.ADDR);
		// end defect 9400 
	}

	/**
	 * Performs special exemptions testing of permanent additional
	 * weight event.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @throws RTSException 
	 */
	private void testPawtSpclExmpt(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		VehicleData laVehData =
			aaVehInqData.getMfVehicleData().getVehicleData();
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();

		// defect 9085 
		RegistrationVerify.verifyValidSpclPlt(
			aaVehInqData,
			TransCdConstant.PAWT);
		// end defect 9085 	

		// Determine if registration is current
		if (laRegValidData.getRegistrationExpired() == 1)
		{
			throw new RTSException(58);
		}
		// Do not allow to modify if fixed weight vehicle
		if (laVehData.getFxdWtIndi() != 0)
		{
			throw new RTSException(52);
		}
		// Do not allow to modify weight if vehicle class is apportioned
		if (laRegData.getRegPltCd().equals(APPRTK)
			|| laRegData.getRegPltCd().equals(APPRTR))
		{
			throw new RTSException(138);
		}
		// Determine if reg class allows weight to change
		RegistrationClassData laRegClassData =
			RegistrationClientBusiness.getRegClassDataObject(
				aaVehInqData);
		if (laRegClassData.getCaryngCapReqd() == 0)
		{
			throw new RTSException(52);
		}
	}

	/**
	 * Performs special exemptions testing of Renewal event.
	 * 
	 * @return Vector 
	 * @param aaVehInqData VehicleInquiryData
	 * @throws RTSException
	 */
	private Vector testRenwlSpclExmpt(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();
		String lsTransCode = laRegValidData.getTransCode();

		// defect 8900
		// Stop Renew for StandardExempt
		if (CommonFeesCache
			.isStandardExempt(laRegData.getRegClassCd()))
		{
			throw new RTSException(471);
		}
		// end defect 8900		

		// Set TNRCC charge fee indicator - not implemented.
		// Verify if current date is within range for renewal
		verifyRenwlDateRange(laRegData, laRegValidData);

		int liRetVal;
		// Determine if registration is invalid
		if (laRegData.getRegInvldIndi() == 1)
		{
			//Set new plate indicator to yes
			laRegValidData.setNewPltReplIndi(1);
			laRegData.setRegExpMo(0);
			laRegData.setRegExpYr(0);
		}
		else
		{
			// Verify if plate type is out of scope of RTS
			liRetVal = verifyPltTypeScope(laRegData);
			if (liRetVal == -1)
			{
				throw new RTSException(742);
			}
		}

		Vector lvExceptionVector = new Vector();
		// starting here we need to collect all errors
		// into a vector return it for later display.
		// Verify valid regis and vehicle class combination is valid
		liRetVal =
			RegistrationVerify.verifyRegVehClassCombination(
				aaVehInqData);
		if (liRetVal == -1)
		{
			laRegValidData.setInvalidClassPltStkrIndi(1);
			laRegValidData.setVehClassOK(0);
			//lvExceptionVector.addElement(new RTSException(411));
		}
		else
		{
			// Verify regis class/plate type/sticker type combination
			liRetVal =
				RegistrationVerify.verifyClassPltStkrCombination(
					aaVehInqData);
			if (liRetVal == -1)
			{
				laRegValidData.setInvalidClassPltStkrIndi(1);
				laRegValidData.setVehClassOK(0);
				//lvExceptionVector.addElement(new RTSException(411));
			}
		}
		// defect 9085 
		if (liRetVal == 0)
		{
			try
			{
				RegistrationVerify.verifyValidSpclPlt(
					aaVehInqData,
					TransCdConstant.RENEW);
			}
			catch (RTSException aeRTSEx)
			{
				lvExceptionVector.addElement(aeRTSEx);
			}
		}
		// end defect 9085 
		// Verify vehicle minimum gross weight for registration class
		// and tire type, if necessary for weight-based vehicles.
		liRetVal =
			RegistrationVerify.verifyVehMinGrossWt(
				aaVehInqData,
				lsTransCode,
				lvExceptionVector);
		if (liRetVal > 0)
		{
			laRegValidData.setInvalidMinGrossWtIndi(1);
			laRegValidData.setVehWtStatusOK(false);
		}
		// modified per Issues Logged during Phase 1 requirements 
		// document check for registration invalid indicator
		int liInvldIndi = laRegData.getRegInvldIndi();
		if (liInvldIndi != 0
			|| laRegValidData.getInvalidClassPltStkrIndi() == 1)
		{
			// defect 9085 
			aaVehInqData.getMfVehicleData().setSpclPltRegisData(null);
			// end defect 9085 
			lvExceptionVector.addElement(new RTSException(411));
		}
		return lvExceptionVector;
	}

	/**
	 * Performs special exemptions testing of replacement event.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @throws RTSException 
	 */
	private void testReplSpclExmpt(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();

		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();

		// Test if vehicle is eligible for replacement
		//   If registration is expired and not multi-year and reg class 
		//    code is not exempt the vehicle is not eligible

		// defect 9085 
		// PlateTypeCache replaces RegistrationRenewalsCache
		//RegistrationRenewalsData laRegRenwlData =
		//	RegistrationRenewalsCache.getRegRenwl(
		//		laRegData.getRegPltCd());

		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(laRegData.getRegPltCd());

		// defect 5974 
		// Process Exempt Plates in Replacement
		// Replaced 4993 code with following 
		// defect 8900
		// Change check for Standard Exempt 

		//if ((laRegValidData.getRegistrationExpired() == 1)
		//	&& (laRegRenwlData != null
		//		&& laRegRenwlData.getAnnualPltIndi() != 0)
		//	&& (laRegData.getRegClassCd() != EXEMPT))
		if ((laRegValidData.getRegistrationExpired() == 1)
			&& (laPlateTypeData != null
				&& laPlateTypeData.getAnnualPltIndi() != 0)
			&& !(CommonFeesCache
				.isStandardExempt(laRegData.getRegClassCd())))
		{
			throw new RTSException(58);
		}
		// end defect 8900			
		// end defect 5974

		boolean lbSpclPlt =
			PlateTypeCache.isSpclPlate(laRegData.getRegPltCd());

		boolean lbExpSpclPlt = false;

		if (lbSpclPlt)
		{
			lbExpSpclPlt =
				RegistrationClientUtilityMethods.isExpiredSpclPlt(
					aaVehInqData);
		}
		// defect 9085 
		RegistrationVerify.verifyValidSpclPlt(
			aaVehInqData,
			TransCdConstant.REPL);
		// end defect 9085 

		// Do not allow expired Special Annual Plate to be replaced 
		// This can happen even when the registration is current. 
		if (laPlateTypeData != null
			&& laPlateTypeData.getAnnualPltIndi() == 1
			&& laPlateTypeData.getPltOwnrshpCd().equals(
				SpecialPlatesConstant.OWNER)
			&& lbExpSpclPlt)
		{
			throw new RTSException(58);
		}
		// end defect 9085 

		// Determine if replacement plate code is present in cache
		// using current date (aaVehInqData.getRTSEffDt()) 
		// defect 8218 
		Vector lvClassToPlateData =
			ClassToPlateCache.getClassToPlate(
				laRegData.getRegClassCd(),
				laRegData.getRegPltCd(),
				aaVehInqData.getRTSEffDt());

		if (lvClassToPlateData != null
			&& !lvClassToPlateData.isEmpty())
		{
			// Save ReplPltCd for use when handling plate inventory
			ClassToPlateData laData =
				(ClassToPlateData) lvClassToPlateData.get(0);

			if (laData.getReplPltCd().length() > 0)
			{
				// defect 9085 
				// Do not allow Replacement of Expired Special Plate when 
				// only replacement = same plate.  Distinct RegClassCd 
				// for these. 
				if (lbExpSpclPlt
					&& laData.getReplPltCd().equals(
						laRegData.getRegPltCd()))
				{
					throw new RTSException(58);
				}
				else
				{
					laRegValidData.setReplPltCd(laData.getReplPltCd());
				}
				// end defect 9085 
			}
			else
			{
				// Display replacement cannot be completed message
				throw new RTSException(48);
			}
		}
		// This error is handled here per Issues&Changes document.
		//	RTS1 handled this error on the Enter of REG003.
		// end defect 8218 
		else
		{
			// Display replacement cannot be completed message
			throw new RTSException(48);
		}
	}

	/**
	 * Main entry method for RegistrationSpecialExemptions class.
	 * 
	 * @return Vector 
	 * @param  aaVehInqData VehicleInquiryData
	 * @throws RTSException
	 */
	public Vector testSpclExmpts(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		RegistrationValidationData laRegValidData =
			(RegistrationValidationData) aaVehInqData
				.getValidationObject();
		String lsTransCode = laRegValidData.getTransCode();
		// Assign RTS Effective Date based upon TransCode
		setRTSEffectiveDate(aaVehInqData, lsTransCode);

		// defect 8900
		// Allow county to do Exempt (RegClassCd = 39)
		// Do not need to verify RegClassCd.
		// verifyRegClass() deprecated in RegistrationVerify
		//caRegVerify.verifyRegClass(laRegData, lsTransCode);
		// end defect 8900

		// Verify document type
		RegistrationVerify.verifyDocType(aaVehInqData);
		// Verify registration invalid indicator
		RegistrationVerify.verifyRegInvldIndi(laRegData, lsTransCode);
		// execute test special exemptions based upon trans code
		Vector lvException = new Vector();
		if (lsTransCode.equals(TransCdConstant.RENEW))
		{
			return testRenwlSpclExmpt(aaVehInqData);
		}
		else if (lsTransCode.equals(TransCdConstant.DUPL))
		{
			testDuplSpclExmpt(aaVehInqData);
		}
		else if (lsTransCode.equals(TransCdConstant.EXCH))
		{
			testExchSpclExmpt(aaVehInqData);
		}
		else if (lsTransCode.equals(TransCdConstant.REPL))
		{
			testReplSpclExmpt(aaVehInqData);
		}
		else if (
			lsTransCode.equals(TransCdConstant.PAWT)
				|| (lsTransCode.equals(TransCdConstant.CORREG)
					&& laRegValidData.getRegModify()
						== RegistrationConstant.REG_MODIFY_APPREHENDED))
		{
			testPawtSpclExmpt(aaVehInqData);
		}
		else if (
			lsTransCode.equals(TransCdConstant.CORREG)
				&& (laRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_REG))
		{
			testCorrtnSpclExmpt(aaVehInqData);
		}
		// defect 9085 
		// Do not allow Address Change for unlinked special plate 
		else if (lsTransCode.equals(TransCdConstant.ADDR))
		{
			testAddrSpclExmpt(aaVehInqData);
		}
		// end defect 9085 
		return lvException;
	}

	/**
	 * Verify if plate type is out of scope for RTS.
	 * Return -1 if out of scope else return 0.
	 * 
	 * @param  aaRegData RegistrationData
	 * @return int
	 */
	public static int verifyPltTypeScope(RegistrationData aaRegData)
	{
		// defect 9085 
		// Get inventory prcs code with current plate code from
		// ItemCodesCache
		//		ItemCodesData laItmCdData =
		//			ItemCodesCache.getItmCd(aaRegData.getRegPltCd());
		//		int liInvPrcCd = 0;
		//		if (laItmCdData != null)
		//		{
		//			liInvPrcCd = laItmCdData.getInvProcsngCd();
		//		}
		//		if (liInvPrcCd == 3)
		//		{
		//			// out of scope for RTS
		//			return -1;
		//		}
		//		else
		//		{
		//			return 0;
		//		}
		boolean lbOutOfScope =
			PlateTypeCache.isOutOfScopePlate(aaRegData.getRegPltCd());

		return lbOutOfScope ? -1 : 0;
		// end defect 9085 
	}

	/**
	 * Verifies if current date is within range for renewal..
	 * 
	 * @param aaRegData RegistrationData
	 * @param aaRegValidData RegistrationValidationData
	 * @throws RTSException 
	
	 */
	private void verifyRenwlDateRange(
		RegistrationData aaRegData,
		RegistrationValidationData aaRegValidData)
		throws RTSException
	{
		// Determine if registration is invalid
		if (aaRegData.getRegInvldIndi() == 1)
		{
			//Set new plate indicator to yes
			aaRegValidData.setNewPltReplIndi(1);
			aaRegData.setRegExpMo(0);
			aaRegData.setRegExpYr(0);
		}
		else if (aaRegValidData.getRegistrationExpired() != 1)
			// Test if registration is not expired
		{
			//Verify if plate type is within scope of RTS
			int liOutOfScope = verifyPltTypeScope(aaRegData);
			if (liOutOfScope != -1) // not out of scope.
			{
				//if current date is within 3 months before renewal date
				RTSDate laRTSDateCurrent = RTSDate.getCurrentDate();
				if (3
					<= 12
						* (aaRegData.getRegExpYr()
							- laRTSDateCurrent.getYear())
						+ (aaRegData.getRegExpMo()
							- laRTSDateCurrent.getMonth()))
				{
					throw new RTSException(50);
				}
				// 3>12*(MfVeh{Row}.RegExpYr-Year(TransAMDate))+
				//  (MfVeh{Row}.RegExpMo-Month(TransAMDate))
			}
		}
	}
}
