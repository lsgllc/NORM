package com.txdot.isd.rts.client.registration.business;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.constants.DocTypeConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/* 
 * RegistrationVerify.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		11/07/2001	modified verifyRegInvldIndi() to test for 
 * 							reg invalid indi for all events per document
 * 							Issues Logged during Phase 1 requirements
 * MAbs			05/14/2002	Checked for Null Pointers CQU100003910
 * K Harrell	06/12/2005	ClassToPlate, PlateToSticker Implementation
 * 							modify verifyClassPltStkrCombination()
 * 							defect 8218 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                *    
 * B Hargrove	06/30/2005	Cleanup for move to Java 1.4.
 * 							Bring code to standards. 
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove 	10/07/2006 	No longer need to check if Exempt
 * 	K Harrell				deprecate verifyRegClass()
 * 							defect 8900  Ver 5.3.0
 * K Harrell	05/21/2007	add verifyValidSpclPlt()
 * 							modified non-static methods to static	
 * 							delete verifyRegClass()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/14/2007	removed year check for annual special plates
 * 							modify verifyValidSpclPlt()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/05/2007	Add logic for rejecting RNR request if 
 * 							unlinked Special Plate.   
 * 							modify verifyValidSpclPlt()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	09/07/2007	Only verify sunsetted Plate Age on 
 * 							Renew. 
 * 							modify verifyValidSpclPlt()
 * 							defect 9288 Ver Special Plates 
 * K Harrell	10/14/2007	Only verify sunsetted Plate Age on Renew 
 * 							if have not specified new special plate
 * 							modify verifyValidSpclPlt()
 * 							defect 9351 Ver Special Plates 2
 * K Harrell	11/01/2007	Throw message that must respecify only on  
 * 							 RENEW 
 * 							modify verifyValidSpclPlt()  
 * 							defect 9400 Ver Special Plates 2   
 * B Hargrove	07/15/2008 	Skip Special Plate edits if Vendor Plate.
 * 							Edit for cannot continue Replacement event 
 * 							if registration and Vendor Plate are expired.
 * 							modify verifyValidSpclPlt() 
 * 							defect 9689 Ver MyPlates_POS
 * B Hargrove	08/08/2008 	Error in 9689. Supposed to be 'not' vendor 
 * 							plate ('!' was left off by mistake).
 * 							modify verifyValidSpclPlt() 
 * 							defect 9792 Ver MyPlates_POS
 * K Harrell	02/18/2009	Add method to create message if SpecialPlate
 * 							Address is to be changed. 
 * 							add verifySpclPltAddrUpdtType()
 * 							defect 9893 Ver Defect_POS_D
 * K Harrell	06/02/2009	Add method to verify CompleteTransactionData
 * 							for Internet Renewal Processing.  Use Constants
 * 							from DocTypeConstant, ErrorsConstant
 * 							add verifyItrntComplTransData()
 *  						delete verifyVehMinGrossWt(VehicleInquiryData,
 * 							  String)  (not used)
 * 							modify verifyDocType(), verifyRegInvldIndi(),
 * 							 verifyVehMinGrossWt() 
 * 							defect 8749 Ver Defect_POS_F
 * K Harrell 	07/20/2009	Add validation for IRENEW and Sunsetted 
 * 							Special Plate which needs to be replaced. 
 * 							modify verifyValidSpclPlt() 
 * 							defect 10128 Ver Defect_POS_F 
 * B Hargrove	01/08/2010  Allow Vendor Plate to use verifyValidSpclPlt()
 *  						so we don't get nullpointer on unlinked or 
 *  						purged VP. see also: 
 * 							FrmRegistraionREG003 validateVendorPlt()
 * 							modify verifyValidSpclPlt()
 * 							defect 10327 Ver Defect_POS_H 
 * K Harrell	04/21/2010	prevent previously registered ATV/ROV from 
 * 							registration
 * 							add ERR_MSG_2022_APPEND
 * 							modify verifyDocType() 
 * 							defect 10453 Ver POS_640 
 * B Hargrove	10/12/2010  Token Trailer no longer prints a sticker.
 * 							modify verifyClassPltStkrCombination()
 * 							defect 10623 Ver 6.6.0
 * B Hargrove 	10/29/2010  Token Trailer now does not print sticker.
 * 							Comment out all changes. They want to store 
 * 							inv detail, just don't print sticker.
 * 							modify verifyClassPltStkrCombination()
 * 							defect 10623 Ver 6.6.0
 * B Woodson	01/25/2012	modify verifyDocType()	
 * 							defect 11228 Ver 6.10.0
 * B Woodson	02/09/2012	unmodify verifyDocType()	
 * 							defect 11228 Ver 6.10.0
 * ---------------------------------------------------------------------
 */
/**
 * RegistrationVerify class is responsible for management of 
 * verifications for various Registration events.
 * 
 * @version	6.6.0				02/09/2012
 * @author	Joseph Kwik
 * <br>Creation Date:			09/12/2001 22:19:45
 */

public class RegistrationVerify
{
	private final static String ERR_MSG_2022_APPEND =
		"  PLEASE ISSUE NEW OFF-HIGHWAY TITLE.";

	/**
	 * RegistrationVerify constructor comment.
	 */
	public RegistrationVerify()
	{
		super();
	}

	/**
	 * Verifies if registration class, plate and sticker combination is 
	 * valid. Return 0 if combination is valid else return -1;
	 * 
	 * @param  aaVehInqData VehicleInquiryData
	 * @return int 
	 * @throws RTSException
	 */
	public static int verifyClassPltStkrCombination(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		String lsStkrCd = new String();
		if (laRegData.getRegStkrCd() == null)
		{
			lsStkrCd = "";
		}
		else
		{
			lsStkrCd = laRegData.getRegStkrCd();
		}
		// defect 8218 
		// Vector of PlateToStickerData 
		Vector lvPltToStkrData =
			RegistrationPlateStickerCache.getPltStkrs(
				laRegData.getRegClassCd(),
				laRegData.getRegPltCd(),
				aaVehInqData.getRTSEffDt(),
				lsStkrCd);

		// defect 10623
		// Token Trailer no longer has a sticker
		if (lvPltToStkrData != null)
//			|| laRegData.getRegClassCd() ==
//				RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
		{
			// end defect 10623
			return 0;
		}
		return -1;
		// end defect 8218 
	}

	/**
	 * Present Msg if Special Plate Address will be changed. 
	 * 
	 * @param aaMFVehData
	 */
	public static void verifySpclPltAddrUpdtType(MFVehicleData aaMFVehData)
	{
		int liUpdate = aaMFVehData.getSpclPltAddrUpdtType();

		if (liUpdate != SpecialPlatesConstant.SPCL_PLT_KEEP_ADDRESS)
		{
			String lsMsg =
				SpecialPlatesConstant.SPCL_PLT_ADDR_CHG_MSG_PREFIX
					+ SpecialPlatesConstant
						.SPCL_PLT_ADDR_CHG_MSG_SUFFIX
						.get(
						new Integer(liUpdate));

			RTSException leRTSEx =
				new RTSException(
					RTSException.INFORMATION_MESSAGE,
					lsMsg,
					"Information");
			leRTSEx.displayError();
		}
	}

	/**
	 * Verify Special Plate Data Attached
	 * 
	 * @param aaVehInqData
	 * @param asTransCd 
	 * @throws RTSException
	 */
	public static void verifyValidSpclPlt(
		VehicleInquiryData aaVehInqData,
		String asTransCd)
		throws RTSException
	{
		MFVehicleData laMFVehData = aaVehInqData.getMfVehicleData();
		RegistrationData laRegData = laMFVehData.getRegData();
		String lsMsg =
			"The Special Plate linked to this registration is invalid.  ";
		String lsMsgPlus = "Use Change Registration to respecify.  ";
		String lsRNRErrMsg = "A Renewal Notice cannot be printed.";
		boolean lbValid = true;
		try
		{
			if (laRegData != null)
			{
				String lsRegPltCd = laRegData.getRegPltCd();

				if (lsRegPltCd != null && laMFVehData != null)
				{
					SpecialPlatesRegisData laSpclPltRegData =
						laMFVehData.getSpclPltRegisData();

					PlateTypeData laPltTypeData =
						PlateTypeCache.getPlateType(lsRegPltCd);

					//defect 9689 
					// Do not do Special Plate edits if Vendor Plate
					//if (laPltTypeData != null)
					// defect 9792
					// The '!' was left off.
					// defect 10327
					// Allow VP to use this edit					
					if (laPltTypeData != null)
						//if (laPltTypeData != null
						//	 && !PlateTypeCache.isVendorPlate(lsRegPltCd))
						// end defect 10327
					{
						// end defect 9689
						// end defect 9792
						boolean lbRegSpclPlt =
							!laPltTypeData.getPltOwnrshpCd().equals(
								SpecialPlatesConstant.VEHICLE);

						// Do not validate if Special Plate and 
						// Out of Scope and Title event type     		
						if (!(lbRegSpclPlt
							&& PlateTypeCache.isOutOfScopePlate(
								lsRegPltCd)
							&& (asTransCd.equals(TransCdConstant.TITLE)
								|| asTransCd.equals(
									TransCdConstant.NONTTL)
								|| asTransCd.equals(
									TransCdConstant.REJCOR))))
						{
							// If no Special Plate Data and Special Plate  - OR - 
							//       Special Plate data and Not Special Plate on 
							//           Reg Record 
							if ((lbRegSpclPlt
								&& laSpclPltRegData == null)
								|| (!lbRegSpclPlt
									&& laSpclPltRegData != null))
							{
								lbValid = false;
							}
							else if (
								laSpclPltRegData != null
									&& laSpclPltRegData.getRegPltCd()
										!= null
									&& !laSpclPltRegData
										.getRegPltCd()
										.equals(
										laRegData.getRegPltCd()))
							{
								lbValid = false;

							}
							else if (
								laSpclPltRegData != null
									&& !laSpclPltRegData
										.getMFGStatusCd()
										.equals(
										SpecialPlatesConstant
											.ASSIGN_MFGSTATUSCD))
							{
								lbValid = false;

							}
							// defect 9351 
							// Only verify if have not specified a new 
							//  special plate  
							// defect 9288 
							// Only verify sunsetted plate age on
							// Renew ** see defect 10128 

							// defect 10128 
							//  Include IRENEW in check for Sunsetted
							//    Plate which needs to be replaced   
							else if (
								laSpclPltRegData != null
									&& ((asTransCd
										.equals(TransCdConstant.RENEW)
										&& !laSpclPltRegData
											.isEnterOnSPL002())
										|| asTransCd.equals(
											TransCdConstant.IRENEW))
									&& OrganizationNumberCache
										.isSunsetted(
										lsRegPltCd,
										laSpclPltRegData.getOrgNo()))
							{
								// end defect 10128 

								int liMandReplPltAge =
									laPltTypeData.getMandPltReplAge();

								if (laSpclPltRegData.getRegPltAge(true)
									>= liMandReplPltAge)
								{
									lsMsg =
										"The plate attached to this vehicle must be replaced as the "
											+ "organization, "
											+ OrganizationNumberCache
												.getOrgName(
												lsRegPltCd,
												laSpclPltRegData
													.getOrgNo())
											+ ", is no longer eligible for manufacture. ";
									lbValid = false;
								}
							}
							// end defect 9288 
							// end defect 9351 

							if (!lbValid)
							{
								if (asTransCd
									.equals(TransCdConstant.RENEW)
									|| asTransCd.equals(
										TransCdConstant.EXCH)
									|| asTransCd.equals(
										TransCdConstant.TITLE)
									|| asTransCd.equals(
										TransCdConstant.REJCOR))
								{
									lsMsg = lsMsg + lsMsgPlus;
								}
								else if (
									asTransCd.equals(
										TransCdConstant.RNR))
								{
									lsMsg = lsMsg + lsRNRErrMsg;
								}
								RTSException leRTSEx =
									new RTSException(
										RTSException.FAILURE_MESSAGE,
										lsMsg,
										"ERROR");
								throw leRTSEx;
							}
							// defect 9400 
							// Streamline validation on LP Plates 
							if (lbRegSpclPlt
								&& laPltTypeData
									.getNeedsProgramCd()
									.equals(
										SpecialPlatesConstant.LP_PLATE))
						{
								if (asTransCd
									.equals(TransCdConstant.REPL))
								{
									RTSException leRTSEx =
										new RTSException(
											RTSException
												.FAILURE_MESSAGE,
											"The Special Plate linked to this registration is not available for replacement.  ",
											"ERROR");
									throw leRTSEx;

								}
								else if (
									asTransCd.equals(
										TransCdConstant.RENEW)
										&& !laSpclPltRegData
											.isEnterOnSPL002())
								{
									RTSException leRTSEx =
										new RTSException(
											RTSException
												.FAILURE_MESSAGE,
											"The Special Plate linked to this registration must be respecified.  "
												+ lsMsgPlus,
											"ERROR");
									throw leRTSEx;
								}
							}
						}
					}
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.setBeep(RTSException.BEEP);
			throw aeRTSEx;
		}
	}

	/**
	 * If CompleteTransactionData null, displays error w/ passed
	 * Plate No 
	 * 
	 * @param aaCTData CompleteTransactionData
	 * @param asPlateNo String
	 */
	public static void verifyItrntComplTransData(
		CompleteTransactionData aaCTData,
		String asPlateNo)
	{
		if (aaCTData == null)
		{
			RTSException leValidEx =
				new RTSException(
					RTSException.WARNING_MESSAGE,
					ErrorsConstant
						.VEH_DATA_MISSING_EXCEPTION
						.replaceFirst(
						ErrorsConstant
							.VEH_DATA_MISSING_EXCEPTION_TXT_TO_REPLACE,
						asPlateNo),
					ErrorsConstant.VEH_DATA_MISSING_EXCEPTION_TITLE);
			leValidEx.displayError();
		}
	}

	/**
	 * Verify document type.
	 * 
	 * @param  aaVehInqData VehicleInquiryData
	 * @throws RTSException 
	 */
	public static void verifyDocType(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		VehicleInquiryData laVehInqData = aaVehInqData;
		TitleData laTitleData =
			laVehInqData.getMfVehicleData().getTitleData();
		int liMfDown = laVehInqData.getMfDown();
		if (liMfDown == 0)
		{
			int liDocType = laTitleData.getDocTypeCd();

			// defect 10453 
			RegistrationData laRegData =
				laVehInqData.getMfVehicleData().getRegData();
			if (laRegData != null
				&& laRegData.isATV_ROV()
				&& liDocType != DocTypeConstant.OFF_HIGHWAY_USE_ONLY)
			{
				throw new RTSException(
					ErrorsConstant.ERR_MSG_NO_REG_FOR_ATV_ROV,
					new String[] { ERR_MSG_2022_APPEND });
			}
			// end defect 10453 

			// defect 8749 
			//	if ((liDocType != 1)
			//		&& (liDocType != 5)
			//		&& (liDocType != 9))
			if ((liDocType != DocTypeConstant.REGULAR_TITLE)
				&& (liDocType
					!= DocTypeConstant.REGISTRATION_PURPOSES_ONLY)
				&& (liDocType != DocTypeConstant.NON_TITLED_VEHICLE))
			{
				int liRTSEx = 0;
				switch (liDocType)
				{
					//case 2 :
					case DocTypeConstant.OFF_HIGHWAY_USE_ONLY :
						{
							//leRTSEx = 466;
							liRTSEx =
								ErrorsConstant
									.ERR_NUM_ACTION_INVALID_OFF_HWY;
							break;
						}
						//case 3 :
					case DocTypeConstant.SALVAGE_CERTIFICATE_NO_REGIS :
						{ // intentionally falling through
						}
						//case 4 :
					case DocTypeConstant.SALVAGE_CERTIFICATE :
						{
							//leRTSEx = 467;
							liRTSEx =
								ErrorsConstant
									.ERR_NUM_ACTION_INVALID_SALV_CERT;
							break;
						}
						//case 6 :
					case DocTypeConstant.CERTIFICATE_OF_AUTHORITY :
						{ // intentionally falling through
						}
						//case 7 :
					case DocTypeConstant
						.CERTIFICATE_OF_AUTHORITY_NO_REGIS :
						{
							//leRTSEx = 468;
							liRTSEx =
								ErrorsConstant
									.ERR_NUM_ACTION_INVALID_COA;
							break;
						}
						//case 8 :
					case DocTypeConstant.LEGAL_RESTRAINT :
						{
							//leRTSEx = 428;
							liRTSEx =
								ErrorsConstant
									.ERR_NUM_ACTION_INVALID_LGL_REST;
							break;
						}
						//case 10 :
					case DocTypeConstant
						.OS_REGISTERED_APPORTIONED_VEH :
						{
							//leRTSEx = 469;
							liRTSEx =
								ErrorsConstant
									.ERR_NUM_ACTION_INVALID_OS_APP;
							break;
						}
						//case 11 :
					case DocTypeConstant.INSURANCE_NO_REGIS_TITLE :
						{
							//leRTSEx = 470;
							liRTSEx =
								ErrorsConstant
									.ERR_NUM_ACTION_INVALID_STOLEN;
							break;
						}
						//case 12 :
					case DocTypeConstant.SALV_TITLE_DAMAGED :
						{ // intentionally falling through
						}
						//case 13 :
					case DocTypeConstant.SALV_TITLE_DAMAGED_NO_REG :
						{
							//leRTSEx = 655;
							liRTSEx =
								ErrorsConstant
									.ERR_NUM_ACTION_INVALID_SALV_VEH_TITLE;
							break;
						}
						//case 14 :
					case DocTypeConstant.NONREPAIR_95_PLUS_LOSS :
						{ // intentionally falling through
						}
						//case 15 :
					case DocTypeConstant
						.NONREPAIR_95_PLUS_LOSS_NO_REG :
						{
							//leRTSEx = 654;
							liRTSEx =
								ErrorsConstant
									.ERR_NUM_ACTION_INVALID_NON_REPAIR;
							break;
						}
					default :
						{
							//leRTSEx = 429;
							liRTSEx =
								ErrorsConstant.ERR_NUM_INVALID_DOC_TYPE;
							break;
						}
				}
				throw new RTSException(liRTSEx);
			}
			// end defect 8749 
		}
	}

	/**
	 * Verifies that registration is valid.
	 * 
	 * @param  aaRegData RegistrationData
	 * @param  asTransCode String
	 * @throws RTSException The exception description.
	 */
	public static void verifyRegInvldIndi(
		RegistrationData aaRegData,
		String asTransCode)
		throws RTSException
	{
		int liInvldIndi = aaRegData.getRegInvldIndi();
		if (liInvldIndi != 0
			&& !asTransCode.equals(TransCdConstant.RENEW))
		{
			// defect 8749 
			//throw new RTSException(161);
			throw new RTSException(
				ErrorsConstant.ERR_NUM_INVALID_REG_NO_ACTION);
			// end defect 8749  
		}
	}

	/**
	 * Verifies if registration and vehicle class combination is valid.
	 * Return 0 if valid otherwise return -1.
	 * 
	 * @param  aaVehInqData VehicleInquiryData
	 * @return int
	 * @throws RTSException 
	 */
	public static int verifyRegVehClassCombination(VehicleInquiryData aaVehInqData)
		throws RTSException
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();
		VehicleData laVehData =
			aaVehInqData.getMfVehicleData().getVehicleData();
		RegistrationClassData laRegClassData =
			RegistrationClassCache.getRegisClass(
				laVehData.getVehClassCd(),
				laRegData.getRegClassCd(),
				aaVehInqData.getRTSEffDt());
		if (laRegClassData != null)
		{
			return 0;
		}
		else
		{
			return -1;
		}
	}

	//	/**
	//	 * Verifies vehicle minimum gross weight for registration class 
	//	 * and tire type. Return 0 if valid else return error code.
	//	 * 
	//	 * @param aaVehInqData VehicleInquiryData
	//	 * @param asTransCode String
	//	 * @return int 
	//	 */
	//	public static int verifyVehMinGrossWt(
	//		VehicleInquiryData aaVehInqData,
	//		String asTransCode)
	//	{
	//		Vector lvExceptionVector = new Vector();
	//		return verifyVehMinGrossWt(
	//			aaVehInqData,
	//			asTransCode,
	//			lvExceptionVector);
	//	}

	/**
	 * Verifies vehicle minimum gross weight for registration class and 
	 * tire type. Return 0 if valid else return error code.
	 * 
	 * @param aaVehInqData VehicleInquiryData
	 * @param asTransCode String
	 * @param avException Vector
	 * @return int 
	 */
	public static int verifyVehMinGrossWt(
		VehicleInquiryData aaVehInqData,
		String asTransCode,
		Vector avException)
	{
		RegistrationData laRegData =
			aaVehInqData.getMfVehicleData().getRegData();

		// Select fee calc category from common fees
		CommonFeesData laCommonFeeData =
			CommonFeesCache.getCommonFee(
				laRegData.getRegClassCd(),
				aaVehInqData.getRTSEffDt());

		// defect 8749
		// Implement Single Return vs. 4
		// Use Constants from CommonFeesConstant, ErrorsConstant
		int liErrNo = 0;

		if (laCommonFeeData != null
				//&& laCommonFeeData.getFeeCalcCat() == 2)
				&& laCommonFeeData.getFeeCalcCat()
					== CommonFeesConstant.FEE_CALC_CAT_WEIGHT_BASED)
			{
				// Verify minimum gross weight for regis class/tire type
				// Get minimum gross weight based on regis class and tire type
				RegistrationWeightFeesData laRegWtFeesData =
					RegistrationWeightFeesCache.getRegWtFee(
						laRegData.getRegClassCd(),
						laRegData.getTireTypeCd(),
						aaVehInqData.getRTSEffDt(),
						laRegData.getVehGrossWt());

				if (laRegWtFeesData != null)
				{
					if (laRegData.getVehGrossWt()
						< laRegWtFeesData.getBegWtRnge())
					{
						if (asTransCode.equals(TransCdConstant.RENEW))
						{
							// avException.addElement(new RTSException(418));
							// return 418;
							liErrNo =
								ErrorsConstant
									.ERR_NUM_GROSS_WT_INVALID_CHNG_WT;
						}
						else
						{
							//avException.addElement(new RTSException(419));
							//return 419;
							liErrNo =
								ErrorsConstant
									.ERR_NUM_GROSS_WT_INVALID_FOR_REG_TIRE;
						}
						avException.addElement(new RTSException(liErrNo));
						// end defect 8749 
					}
				}
			}
			return liErrNo;
			// end defect 8749 
		}
	}
