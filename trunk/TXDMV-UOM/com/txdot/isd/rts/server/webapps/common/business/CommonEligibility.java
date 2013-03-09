package com.txdot.isd.rts.server.webapps.common.business;

import java.util.Calendar;
import java.util.Vector;

import com.txdot.isd.rts.client.registration.business.RegistrationVerify;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.DocTypeConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;
import com.txdot.isd.rts.services.webapps.util.constants.MessageConstants;

/*
 * CommonEligibility.java 
 *
 * (c) Department of Transportation  2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford		03/25/2003	PCR24,Modify item codes in Address
 *                      	Modify plateTypeEligible(). 
 *							defect 4885.
 * B Brown		08/20/2003	Modify eligibleForAddress().
 *                      	Modify itemCdEligible()
 *                      	Removed isSpecialPlate().
 *							defect 4885.
 * B Brown		12/04/2003	Modify itemCdEligible() 
 *                      	defect 6686.Version 5.1.5 fix 2.
 * B Brown		11/30/2004	Disallow internet Tow Truck registration:
 *							Add method isRegClassCodeEligible().
 *							Modify method eligibleForRenewal().
 *							Add 2 instances constants for tow truck reg
 *                      	class codes.
 *                      	defect 7356. Ver 5.2.2.
 * Jeff S.		02/23/2005	Get code to standard. Changed a non-static
 * 							call to a static call.
 * 							modify itemCdEligible(),plateTypeEligible(), 
 * 							registrationTooFarInFuture()
 *							defect 7889 Ver 5.2.3
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7888 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * K Harrell	10/25/2005	Comment out unused methods, renamed 
 * 							renamed/refactored eligibleForAddressChange()
 * 							to isEligibleForAddressChange(),
 * 							eligibleForNewRenewalNotice() to 
 * 							isEligibleForNewRenewalNotice(),
 * 							docTypeEligible() to isDocTypeEligible(),
 * 							plateTypeEligible() to isPlateTypeEligible(),
 * 							itemCdEligible() to isItemCdEligible()
 * 							refactored isRegClassCodeEligible()
 * 							registrationTooFarInFuture() to 
 * 							isRegistrationTooFarInFuture()  
 * 							defect 7889 Ver 5.2.3   
 * B. Brown		11/21/2005  Do not allow Seasonal Ag vehicles to renew
 * 							over the internet.
 * 							modify isRegClassCodeEligible()
 * 							defect 8434 Ver 5.2.2.7
 * B. Brown		02/13/2007  Comment out references to 
 * 							isPlateTypeEligible()
 * 							deprecate isPlateTypeEligible()
 * 							modify isEligibleForAddressChange(),
 * 								isEligibleForNewRenewalNotice(),
 * 								isEligibleForRenewal()
 * 							defect 9119 Ver Special Plates  
 * K Harrell	02/26/2007	Comment out isPlateTypeEligible() as
 * 							have removed InvProcsngCd
 *							defect 9085 Ver Special Plates 
 * B Brown		07/18/2007  Added Special Plate checks
 * 							modify isEligibleForRenewal() 
 * 							defect 9119 Ver Special Plates
 * B Brown		07/24/2007  Add another Special Plate check 							
 * 							add isPlateEligible()   
 * 							defect 9119 Ver Special Plates    
 * B Brown		07/27/2007	Don't allow disaster relief vehicle or
 * 							ATV to renwew on the internet.
 * 							Add DISASTER_RELIEF_11
 *								DISASTER_RELIEF_TRL_77 = 77
 *								ATV_7 = 7	             
 *							Modify isRegClassCodeEligible
 *							defect 9133 Ver Special Plates
 * B Brown		08/18/2008	Disallow invprcsdcd's = 3 in renewal,
 * 							address change, and renewal notice.
 * 							Reinstated method isPlateTypeEligible().
 * 							modify isEligibleForNewRenewalNotice(),
 * 								isEligibleForNewRenewal(),
 * 								isEligibleForAddressChange()
 * 							defect 9658 ver 5.8.2
 * K Harrell	07/20/2009	Sorted members; Used DocTypeCd constants
 * 							modify isDocTypeCdEligible()  
 * 							defect 10128 Ver Defect_POS_F 
 * B Brown		01/07/2011	Add common fees cache check for regclasscd
 * 							internet renewal eligibility check.
 * 							modify isRegClassCodeEligible()
 * 							defect 10714 Ver POS_670 
 * K Harrell	03/16/2011	Modify for WebAgent Lookup; Make 
 * 							 selected methods public.  
 * 							modify isDocTypeCdEligible(), 
 * 							 isRegClassCodeEligible(), 
 * 							 isRegistrationTooFarInFuture(), 
 * 							 getCutoffDay(), isPlateTypeEligible()    
 * 							defect 10768 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/** 
 * This class is responsible for determining internet registration
 * and address change eligibility, and if a renewal notice should be 
 * generated.
 *
 * @version 6.7.1			03/16/2011
 * @author  CCHEN2
 * <br>Creation Date:		10/08/2003 16:38:56
 */

public class CommonEligibility
{

	// Object 
	private MFVehicleData caMfVehicleData;
	private VehicleInquiryData caVehInquiryData;

	// String 
	private String csMessage;

	// Vector
	private Vector cvSalvageData;
	// end defect 9133

	/**
	 * CommonEligibility constructor
	 * 
	 * @param aaVehInquiryData VehicleInquiryData
	 */
	public CommonEligibility(VehicleInquiryData aaVehInquiryData)
	{
		super();
		this.caVehInquiryData = aaVehInquiryData;
		this.caMfVehicleData = caVehInquiryData.getMfVehicleData();
		this.cvSalvageData = caMfVehicleData.getVctSalvage();
	}

	/**
	 * Get the cut off date for the renewal.
	 * 
	 * @param aaCutoffDate Calendar
	 * @return int
	 */
	public int getCutoffDay(Calendar aaCutoffDate)
	{
		int liCutoffDay = 5;
		switch (aaCutoffDate.get(Calendar.DAY_OF_WEEK))
		{
			case Calendar.TUESDAY :
				{
				}
			case Calendar.SUNDAY :
				{
					liCutoffDay += 1;
					break;
				}
			case Calendar.WEDNESDAY :
				{
				}
			case Calendar.THURSDAY :
				{
				}
			case Calendar.FRIDAY :
				{
				}
			case Calendar.SATURDAY :
				{
					liCutoffDay += 2;
					break;
				}
		}
		return liCutoffDay;
	}

	/**
	 * If the transaction has Hard Stop.
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	private boolean hasHardStop(String asTransCd)
	{
		// load up the indicators for this transCd into a vector
		Vector lvIndi =
			IndicatorLookup.getIndicators(
				caMfVehicleData,
				asTransCd,
				IndicatorLookup.SCREEN);
		// check to see if there is a hard stop for each indicator
		return IndicatorLookup.hasHardStop(lvIndi);
	}

	/**
	 * Is this doc type eligible.
	 * 
	 *  Cd Desc  
	 *   1 REGULAR TITLE
	 *   2 OFF-HIGHWAY USE ONLY
	 *   3 SALVAGE CERTIFICATE - NO REGIS
	 *   4 SALVAGE CERTIFICATE
	 *   5 REGISTRATION PURPOSES ONLY
	 *   6 CERT. OF AUTH. - NO REGIS
	 *   7 CERTIFICATE OF AUTHORITY
	 *   8 LEGAL RESTRAINT
	 *   9 NON-TITLED-VEHICLE
	 *  10 O/S REGISTERED APPORTIONED VEH
	 *  11 INSURANCE - NO REGIS - TITLE
	 *  12 SALV TITLE DAMAGED
	 *  13 SALV TITLE DAMAGED-NO REG
	 *  14 NONREPAIR 95% PLUS LOSS
	 *  15 NONREPAIR 95% PLUS LOSS-NOREG
	 * 
	 * @return boolean
	 */
	public boolean isDocTypeCdEligible()
	{
		boolean lbReturn = true;

		int liDocTypeCd = caMfVehicleData.getTitleData().getDocTypeCd();

		// defect 10128 
		//		if (liDocTypeCd == 2
		//		  || liDocTypeCd == 3
		//		  || liDocTypeCd == 4
		//		  || liDocTypeCd == 6
		//		  || liDocTypeCd == 7
		//		  || liDocTypeCd == 8
		//		  || liDocTypeCd == 11
		//		  || liDocTypeCd == 12
		//		  || liDocTypeCd == 13
		//		  || liDocTypeCd == 14
		//		  || liDocTypeCd == 15)

		//  TODO  or   liDocTypeCd in (1,5,9,10)  
		//        or   add a column to DocTypeCd 
		// 

		if (liDocTypeCd == DocTypeConstant.OFF_HIGHWAY_USE_ONLY
			|| liDocTypeCd == DocTypeConstant.SALVAGE_CERTIFICATE_NO_REGIS
			|| liDocTypeCd == DocTypeConstant.SALVAGE_CERTIFICATE
			|| liDocTypeCd == DocTypeConstant.CERTIFICATE_OF_AUTHORITY
			|| liDocTypeCd
				== DocTypeConstant.CERTIFICATE_OF_AUTHORITY_NO_REGIS
			|| liDocTypeCd == DocTypeConstant.LEGAL_RESTRAINT
			|| liDocTypeCd == DocTypeConstant.INSURANCE_NO_REGIS_TITLE
			|| liDocTypeCd == DocTypeConstant.SALV_TITLE_DAMAGED
			|| liDocTypeCd == DocTypeConstant.SALV_TITLE_DAMAGED_NO_REG
			|| liDocTypeCd == DocTypeConstant.NONREPAIR_95_PLUS_LOSS
			|| liDocTypeCd
				== DocTypeConstant.NONREPAIR_95_PLUS_LOSS_NO_REG)
		{
			// end defect 10128 
			lbReturn = false;
		}

		return lbReturn;
	}

	/**
	 * Eligible for Address Change Online.
	 * 
	 * @return boolean
	 */
	public boolean isEligibleForAddressChange()
	{
		// Not eligible if ANY of the following:  
		//  1)  IADDR Hard Stop  
		//  2)  DocTypeCd not eligible  
		//  3)  PlateType not eligible && ItemCd not eligible
		//        i.e. InvProcsngCd in (2,3) && ItmCd not  in list

		boolean lbReturn = true;

		if (hasHardStop(TransCdConstant.IADDR)
			|| !isDocTypeCdEligible() 
			// defect 9119
			// defect 9658
			|| (!isPlateTypeEligible()
				&& !isItemCdEligible()) // end defect 9658
			|| !isItemCdEligible()
			|| !isPlateEligible(TransCdConstant.IADDR))
			// end defect 9119
		{
			lbReturn = false;
		}

		return lbReturn;
	}
	
	/**
	 * Eligible for new renewal notice.
	 * 
	 * @return boolean
	 */
	public boolean isEligibleForNewRenewalNotice()
	{
		// Not eligible if ANY of the following:  
		//  1) IRNR Hard Stop  
		//  2) DocTypeCd not eligible 
		//  3) PlateType not eligible  
		//  4) Registration Too Far In Future  
		//  5) Expired Registration

		boolean lbReturn = true;

		if (hasHardStop(TransCdConstant.IRNR)
			|| !isDocTypeCdEligible() // defect 9119
		// defect 9658 
			|| !isPlateTypeEligible() // end defect 9658
		// end defect defect 9119
			|| isRegistrationTooFarInFuture()
			|| isExpiredRegistration())
		{
			lbReturn = false;
		}
		return lbReturn;
	}
	/**
	 * Eligible for renewal.
	 *
	 * @return boolean
	 */
	public boolean isEligibleForRenewal()
	{
		// Not eligible if ANY of the following:  
		//  1) IRENEW Hard Stop               
		//  2) DocTypeCd not eligible        
		//  3) PlateType not eligible         
		//  4) Registration Too Far In Future
		//  5) Expired Registration            
		//  6) RegClassCd not eligible

		boolean lbReturn = true;

		if (hasHardStop(TransCdConstant.IRENEW)
			|| !isDocTypeCdEligible() // defect 9119 
		// defect 9658	 
			|| !isPlateTypeEligible() // end defect 9658
			|| isRegistrationTooFarInFuture()
			|| isExpiredRegistration()
			|| !isRegClassCodeEligible()
			|| !isPlateEligible(TransCdConstant.IRENEW))
		{
			lbReturn = false;
		}
		//		try
		//		{
		//			RegistrationVerify.verifyValidSpclPlt(
		//				caVehInquiryData,
		//				TransCdConstant.IRENEW);
		//		}
		//
		//		catch (RTSException leRTSEx)
		//		{
		//			lbReturn = false;
		//		}

		// (aaVehInqData, TransCd) method here??

		return lbReturn;
	}

	/**
	 * Cheack to see if vehicle has expired registration.
	 * 
	 * @return boolean
	 */
	public boolean isExpiredRegistration()
	{
		// independently checked for address change
		// and registration renewal, not within commonEligible
		// so it is public
		// Verify that cutoff date has not been reached.
		// Cutoff date is the 5 business days after the last day of 
		// the EXP_MONTH_YEAR
		Calendar laCurrentDate = Calendar.getInstance();
		Calendar laCutoffDate = Calendar.getInstance();
		int liMonth = caMfVehicleData.getRegData().getRegExpMo() - 1;
		int liYear = caMfVehicleData.getRegData().getRegExpYr();
		laCutoffDate.set(liYear, liMonth, 1);
		//get the first day of the exp month
		laCutoffDate.add(Calendar.MONTH, 1);
		//roll the month forward
		//set the cutoff day (5 business days after the last day 
		// of the exp month)
		laCutoffDate.set(
			Calendar.DAY_OF_MONTH,
			getCutoffDay(laCutoffDate));

		boolean lbReturn = false;
		if (laCurrentDate.after(laCutoffDate))
		{
			csMessage = MessageConstants.MSG_REG_EXPIRED;
			lbReturn = true;
		}
		return lbReturn;
	}

	/**
	 * Chage if item codes is eligible for Internet address change.
	 * 
	 * @return boolean
	 */
	private boolean isItemCdEligible()
	{
		MFVehicleData laMfVehData = caVehInquiryData.getMfVehicleData();
		RegistrationData laRegData = laMfVehData.getRegData();
		String lsPlateCd = laRegData.getRegPltCd();

		boolean lbReturn = false;

		if (lsPlateCd != null)
		{
			ItemCodesData laItemCodesData =
				ItemCodesCache.getItmCd(lsPlateCd);
			// defect 6686  
			// add ADMJUDG - FED ADMNSTRATV LAW JUDGES
			if (!(laItemCodesData.getItmCd().equals("ADMJUDG")
				|| laItemCodesData.getItmCd().equals("ANPTPLT")
				|| laItemCodesData.getItmCd().equals("ANPTSTKR")
				|| laItemCodesData.getItmCd().equals("APPRTK")
				|| laItemCodesData.getItmCd().equals("APPRTR")
				|| laItemCodesData.getItmCd().equals("ASGBODY")
				|| laItemCodesData.getItmCd().equals("ASGEQUIP")
				|| laItemCodesData.getItmCd().equals("ASGFRAME")
				|| laItemCodesData.getItmCd().equals("ASGHTTRL")
				|| laItemCodesData.getItmCd().equals("ASGMOTC")
				|| laItemCodesData.getItmCd().equals("ASGTMOT")
				|| laItemCodesData.getItmCd().equals("ASGTRANS")
				|| laItemCodesData.getItmCd().equals("ASGTRLR")
				|| laItemCodesData.getItmCd().equals("ASGTXMC")
				|| laItemCodesData.getItmCd().equals("ASGTXMV")
				|| laItemCodesData.getItmCd().equals("CASHRCPT")
				|| laItemCodesData.getItmCd().equals("CCOTITLE")
				|| laItemCodesData.getItmCd().equals("CNTYJUDG")
				|| laItemCodesData.getItmCd().equals("CRDTVOUH")
				|| laItemCodesData.getItmCd().equals("EXPDBL")
				|| laItemCodesData.getItmCd().equals("EXPFO")
				|| laItemCodesData.getItmCd().equals("EXPMCP")
				|| laItemCodesData.getItmCd().equals("EXPSGL")
				|| laItemCodesData.getItmCd().equals("FOREST")
				|| laItemCodesData.getItmCd().equals("ORIGTTL")
				|| laItemCodesData.getItmCd().equals("PARP")
				|| laItemCodesData.getItmCd().equals("REASGVIN")
				|| laItemCodesData.getItmCd().equals("RTP")
				|| laItemCodesData.getItmCd().equals("SALVCERT")
				|| laItemCodesData.getItmCd().equals("SEASAGPT")
				|| laItemCodesData.getItmCd().equals("SJP")
				|| laItemCodesData.getItmCd().equals("SOP")
				|| laItemCodesData.getItmCd().equals("TOPAUTH")
				|| laItemCodesData.getItmCd().equals("UHP")
				|| laItemCodesData.getItmCd().equals("UJP")
				|| laItemCodesData.getItmCd().equals("USP")
				|| laItemCodesData.getItmCd().equals("144RBULK")
				|| laItemCodesData.getItmCd().equals("5APPRTR")
				|| laItemCodesData.getItmCd().equals("5TTP")
				|| laItemCodesData.getItmCd().equals(
					"72RBULK"))) //	end defect 6686
			{
				lbReturn = true;
			}
		}
		return lbReturn;
	}

	/**
	 * Is the special plate available and valid for IRENEW or IADDR.
	 * 
	 * @param asTransCd String
	 * @return boolean
	 */
	public boolean isPlateEligible(String asTransCd)
	{
		boolean lbPlateIsAvailable = false;

		if (caMfVehicleData
			.isAvailableForProcessing(
				CommonConstant.INET_OFC_CD,
				asTransCd))
		{
			lbPlateIsAvailable = true;
			try
			{
				RegistrationVerify.verifyValidSpclPlt(
					caVehInquiryData,
					TransCdConstant.IRENEW);
			}

			catch (RTSException leRTSEx)
			{
				lbPlateIsAvailable = false;
			}
		}
		else
		{
			lbPlateIsAvailable = false;
		}

		return lbPlateIsAvailable;
	}

	/**
	 * Determine if the plate is eligible for registration renewal
	 * notice generation or not, hence elibible for renewal receipient 
	 * address change or not, i.e. if plate type out of scope.
	 * @return boolean
	 */
	public boolean isPlateTypeEligible()
	{

		MFVehicleData laMfVehData = caVehInquiryData.getMfVehicleData();
		RegistrationData laRegData = laMfVehData.getRegData();
		String lsPlateCd = laRegData.getRegPltCd();

		boolean lbReturn = true;
		if (lsPlateCd != null)
		{
			ItemCodesData laItemCodesData =
				ItemCodesCache.getItmCd(lsPlateCd);

			int liInvProcsngCd = laItemCodesData.getInvProcsngCd();

			// defect 9658
			// if (liInvProcsngCd == 2 || liInvProcsngCd == 3)
			if (liInvProcsngCd == 3)
				// end defect 9658
			{
				lbReturn = false;
			}
		}
		return lbReturn;
	}

	/**
	 * This method looks at Reg class codes 46 and 47 (tow trucks)
	 * to disallow them to be registered over the internet.
	 *
	 * @return boolean
	 */
	public boolean isRegClassCodeEligible()
	{
		MFVehicleData laMfVehData = caVehInquiryData.getMfVehicleData();
		RegistrationData laRegData = laMfVehData.getRegData();
		int liRegClassCd = laRegData.getRegClassCd();
		
		boolean lbRegClassCodeEligible = false;
		
		//		defect 10714
		//		switch (liRegClassCd)
		//		{
		//			case TOW_TRUCK_REGCLASS_46 :
		//				{
		//					break;
		//				}
		//			case TOW_TRUCK_REGCLASS_47 :
		//				{
		//					break;
		//				}
		//			case SEAS_AG_TRK_LESS_OR_EQ_1TON_70 :
		//				{
		//					break;
		//				}
		//			case SEAS_AG_TRK_GREATER_THAN_1TON_71 :
		//				{
		//					break;
		//				}
		//			case SEAS_AG_FARM_TRK_LESS_OR_EQ_1TON_72 :
		//				{
		//					break;
		//				}
		//			case SEAS_AG_FARM_TRK_GREATER_THAN_1TON_73 :
		//				{
		//					break;
		//				}
		//			case SEAS_AG_FRM_TRKTRAC_GREATER_THAN_1TON_74 :
		//				{
		//					break;
		//				}
		//			case SEAS_AG_COTTON_TRK_GREATER_THAN_1TON_75 :
		//				{
		//					break;
		//				}
		//			case SEAS_AG_COMBINATION_76 :
		//				{
		//					break;
		//				}
		//				// defect 9133
		//			case DISASTER_RELIEF_11 :
		//				{
		//					break;
		//				}
		//			case DISASTER_RELIEF_TRL_77 :
		//				{
		//					break;
		//				}
		//			case ATV_7 :
		//				{
		//					break;
		//				}
		//				// end defect 9133	
		//			default :
		//				{
		//					lbRegClassCodeEligible = true;
		//				}
		//		}
		//		return lbRegClassCodeEligible;
		
		int liEffectiveExpDatePlusOne = 0;
		
		if (laRegData.getRegExpYr() != 0
			&& laRegData.getRegExpMo() != 0)
		{
			if (laRegData.getRegExpMo() != 12)
			{
				liEffectiveExpDatePlusOne =
					(laRegData.getRegExpYr() * 10000)
						+ ((laRegData.getRegExpMo() + 1) * 100)
						+ 1;
			}
			else
			{
				liEffectiveExpDatePlusOne =
					((laRegData.getRegExpYr() + 1) * 10000) + 100 + 1;
			}
		}
		
		CommonFeesData laCommonFeesData =
			CommonFeesCache.getCommonFee(
				liRegClassCd,
				liEffectiveExpDatePlusOne);
				
		if (laCommonFeesData.getIVTRSRegNotAllowdIndi() == 0)
		{
			lbRegClassCodeEligible = true;
		}
		return lbRegClassCodeEligible;
		// end defect 10714
	}

	/**
	 * Return if the registration is too far in the future.
	 *  
	 * RNR is available only if within window, 
	 *  i.e. RegExp MoYr - Current Date is less than or equal 3 months     
	 * 
	 * @return boolean
	 */
	public boolean isRegistrationTooFarInFuture()
	{
		RTSDate lRTSDateCurrent = RTSDate.getCurrentDate();

		boolean lbReturn = false;
		if (3
			<= 12
				* (caMfVehicleData.getRegData().getRegExpYr()
					- lRTSDateCurrent.getYear())
				+ (caMfVehicleData.getRegData().getRegExpMo()
					- lRTSDateCurrent.getMonth()))
		{
			lbReturn = true;
		}

		return lbReturn;
	}

	//		/**
	//		 * Get the message.
	//		 * 
	//		 * @return String
	//		 */
	//		//	public String getMessage()
	//		//	{
	//		//		return csMessage;
	//		//	}	

	//	/**
	//	 * Salvage Certificate.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean salvageCertificate()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData.getTitleData().getDocTypeCd() == 3
	//	//				|| caMfVehicleData.getTitleData().getDocTypeCd() == 4);
	//	//	}
	//	/**
	//	 * SCOT
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean salvageCertificateOfTitle()
	//	//	{
	//	//		return (
	//	//			((SalvageData) cvSalvageData.elementAt(0)).getSlvgCd()
	//	//				== 8);
	//	//	}
	//	/**
	//	 * Set the current message.
	//	 * 
	//	 * @param asMessage String
	//	 */
	//	//	public void setMessage(String asMessage)
	//	//	{
	//	//		csMessage = asMessage;
	//	//	}
	//	/**
	//	 * Return if the vehicle is stolen or not.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean stolen()
	//	//	{
	//	//		return (caMfVehicleData.getVehicleData().getDpsStlnIndi() != 0);
	//	//	}
	//	/**
	//	 * Title Application awaiting release.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean titleAppAwaitingRelease()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData
	//	//				.getTitleData()
	//	//				.getTtlProcsCd()
	//	//				.equalsIgnoreCase(
	//	//				"H"));
	//	//	}
	//	/**
	//	 * Title Application Rejected.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean titleAppRejected()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData
	//	//				.getTitleData()
	//	//				.getTtlProcsCd()
	//	//				.equalsIgnoreCase(
	//	//				"R"));
	//	//	}
	//	/**
	//	 * Title Application Held Awaiting DPSOK.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean titleHeldAwaitingDPSOK()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData
	//	//				.getTitleData()
	//	//				.getTtlProcsCd()
	//	//				.equalsIgnoreCase(
	//	//				"D"));
	//	//	}
	//	/**
	//	 * Title Revoked.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean titleRevoked()
	//	//	{
	//	//		return (caMfVehicleData.getTitleData().getTtlRevkdIndi() != 0);
	//	//	}
	//	/**
	//	 * Title Superceded.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean titleSuperceded()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData
	//	//				.getTitleData()
	//	//				.getTtlProcsCd()
	//	//				.equalsIgnoreCase(
	//	//				"S"));
	//	//	}
	//	/**
	//	 * Title Surrendered.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean titleSurrendered()
	//	//	{
	//	//		return (caMfVehicleData.getTitleData().getSurrTtlDate() != 0);
	//	//	}
	//	/**
	//	 * Vehicle Sold.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean vehicleSold()
	//	//	{
	//	//		return (caMfVehicleData.getTitleData().getVehSoldDate() != 0);
	//	//	}
	//	/**
	//	 * Vehicle Transferred.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean vehicleTransferred()
	//	//	{
	//	//		return vehicleSold();
	//	//	}
	//	/**
	//		 * Return if the vehicle was abandoned or not.
	//		 * 
	//		 * @return boolean
	//		 */
	//	//	private boolean abandonedMotorVehicle()
	//	//	{
	//	//		return (
	//	//			((SalvageData) cvSalvageData.elementAt(0)).getSlvgCd()
	//	//				== 7);
	//	//	}
	//	/**
	//	 * Return if the vehicle was agency loaned or not.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean agencyLoaned()
	//	//	{
	//	//		return (caMfVehicleData.getTitleData().getAgncyLoandIndi() != 0);
	//	//	}
	//	/**
	//	 * Return if the vehicle was bonded title or not.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean bondedTitle()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData
	//	//				.getTitleData()
	//	//				.getBndedTtlCd()
	//	//				.equalsIgnoreCase(
	//	//				"S"));
	//	//	}
	//	/**
	//	 * Return if the vehicle has Certificate of Authority or not.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean certificateOfAuthority()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData.getTitleData().getDocTypeCd() == 6
	//	//				|| caMfVehicleData.getTitleData().getDocTypeCd() == 7);
	//	//	}
	//	/**
	//	 * Used to check eligibility for:
	//	 * 1) Registration Renewal
	//	 * 2) Address Change
	//	 * 3) Address Change Renewal Notice
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean commonEligible1()
	//	//	{
	//	//
	//	//		// plate type out ot scope or not	
	//	//		if (!plateTypeEligible())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (invalidRegistration())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (exempt())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (stolen())
	//	//		{
	//	//			return false;
	//	//		}
	//	//
	//	//		int liDocTypeCd = caMfVehicleData.getTitleData().getDocTypeCd();
	//	//		if (liDocTypeCd == 2
	//	//			|| liDocTypeCd == 3 // OFF HIGHWAY USE ONLY
	//	//			|| liDocTypeCd == 4 // SALVAGE CERTIFICATE NO REGIS & 
	//	//								// DUPLICATE " " " "
	//	//			|| liDocTypeCd == 6 // SALVAGE CERTIFICATE & DUPLICATE " "
	//	//			|| liDocTypeCd == 7 // CERTIFICATE OF AUTHORITY - NO REGIS
	//	//			|| liDocTypeCd == 8 // CERTIFICATE OF AUTHORITY
	//	//			|| liDocTypeCd == 11 // LEGAL RESTRAINT - CONTACT TXDOT
	//	//			|| liDocTypeCd == 12 // INSURANCE - NO REGIS
	//	//			|| liDocTypeCd == 13 // SALVAGE CERTIFICATE OF TITLE 75-94% 
	//	//								 // LOSS & DUPLICATE " " " " " "
	//	//			|| liDocTypeCd == 14 // SALVAGE CERTIFICATE OF TITLE 75-94% 
	//	//								 // LOSS-NOREG & DUPLICATE " " " " " "
	//	//			|| liDocTypeCd == 15 // NONREPAIRABLE CERTIFICATE OF TITLE 
	//	//								 // 95% LOSS & DUPLICATE " " " " " "
	//	//			)
	//	//			// NONREPAIRABLE CERTIFICATE OF TITLE 95% LOSS-NOREG & 
	//	//			// DUPLICATE " " " " " "
	//	//		{
	//	//			return false;
	//	//		}
	//	//		return true;
	//	//	}
	//	/**
	//	 * Used to check eligibility for:
	//	 * 1) Registration Renewal
	//	 * 2) Address Change Renewal Notice
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean commonEligible2()
	//	//	{
	//	//		if (agencyLoaned())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (bondedTitle())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (DOTStandards())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (DPSSafetySuspension())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (junkedVehicle())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (evidenceSurrenderedBySalvageYard())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (evidenceSurrenderedByOwner())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (creditVoucherIssued())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (abandonedMotorVehicle())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (refundPending())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (mailReturn())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (titleSurrendered())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (hotCheckRegistrationFees())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (hotCheckTitleFees())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (titleAppAwaitingRelease())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (titleAppRejected())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (titleSuperceded())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (titleRevoked())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (titleHeldAwaitingDPSOK())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (vehicleTransferred())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (registrationTooFarInFuture())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		if (expiredRegistration())
	//	//		{
	//	//			return false;
	//	//		}
	//	//		return true;
	//	//	}
	//	/**
	//	 * Was a credit voucher issued.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean creditVoucherIssued()
	//	//	{
	//	//		return (
	//	//			((SalvageData) cvSalvageData.elementAt(0)).getSlvgCd()
	//	//				== 6);
	//	//	}
	//	/**
	//	 * DOT statndards
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean DOTStandards()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData.getVehicleData().getDotStndrdsIndi() != 0);
	//	//	}
	//	/**
	//	 * DPS Safety Suspension.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean DPSSafetySuspension()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData.getRegData().getDpsSaftySuspnIndi() != 0);
	//	//	}
	//	/**
	//	 * Duplicate NRCOT
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean duplicateNonrepairableCertificateOfTitle()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData.getTitleData().getDocTypeCd() == 14
	//	//				|| caMfVehicleData.getTitleData().getDocTypeCd() == 15);
	//	//	}
	//	/**
	//	 * Duplicate Salvage Certificate.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean duplicateSalvageCertificate()
	//	//	{
	//	//		// From Todd, they are the same
	//	//		return salvageCertificate();
	//	//	}
	//	/**
	//	 * Duplicate SCOT.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean duplicateSalvageCertificateOfTitle()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData.getTitleData().getDocTypeCd() == 12
	//	//				|| caMfVehicleData.getTitleData().getDocTypeCd() == 13);
	//	//	}		
	//	/**
	//	 * Evidence Surrendered By Owner.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean evidenceSurrenderedByOwner()
	//	//	{
	//	//		return (
	//	//			((SalvageData) cvSalvageData.elementAt(0)).getSlvgCd()
	//	//				== 5);
	//	//	}
	//	/**
	//	 * Evidence Surrendered By Salvage Yard.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean evidenceSurrenderedBySalvageYard()
	//	//	{
	//	//		return (
	//	//			((SalvageData) cvSalvageData.elementAt(0)).getSlvgCd()
	//	//				== 4);
	//	//	}
	//	/**
	//	 * Exempt.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean exempt()
	//	//	{
	//	//		return (caMfVehicleData.getRegData().getExmptIndi() != 0);
	//	//	}	
	//	/**
	//	 * Return if the vehicle has hot check Reg Fee's.
	//	 * 		 
	//	 *  
	//	 * @return boolean
	//	 */
	//	//	private boolean hotCheckRegistrationFees()
	//	//	{
	//	//		return regHotChecked();
	//	//	}
	//	/**
	//	 * Return if the vehicle has hot check Title Fee's.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean hotCheckTitleFees()
	//	//	{
	//	//		return (caMfVehicleData.getTitleData().getTtlHotCkIndi() != 0);
	//	//	}
	//	/**
	//	 * Return if the vehicle has an invalid Registration.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean invalidRegistration()
	//	//	{
	//	//		return (caMfVehicleData.getRegData().getRegInvldIndi() != 0);
	//	//	}	
	//	/**
	//		 * Refund Pending
	//		 * 
	//		 * @return boolean
	//		 */
	//	//	private boolean refundPending()
	//	//	{
	//	//		return registrationRefund();
	//	//	}
	//	/**
	//	 * Registration Hot Checked.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean regHotChecked()
	//	//	{
	//	//		return (caMfVehicleData.getRegData().getRegHotCkIndi() != 0);
	//	//	}
	//	/**
	//	 * Return if the trans is a Reg Refund.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean registrationRefund()
	//	//	{
	//	//		// if Registration Refund amount is not zero,
	//	//		// it is registration refund
	//	//		return (
	//	//			!caMfVehicleData.getRegData().getRegRefAmt().equals(
	//	//				new Dollar("0.00")));
	//	//	}	
	//	/**
	//	 * Return if the vehicle is junked or not.
	//	 * 
	//	 * @return boolean
	//	 */
	//	//	private boolean junkedVehicle()
	//	//	{
	//	//		// 0 is not junked, others are junked
	//	//		// From Michael
	//	//		return (
	//	//			((SalvageData) cvSalvageData.elementAt(0)).getSlvgCd()
	//	//				!= 0);
	//	//	}
	//	/**
	//	 * Mail Returned.
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean mailReturn()
	//	//	{
	//	//		return (
	//	//			caMfVehicleData.getRegData().getRenwlMailRtrnIndi() != 0);
	//	//	}
	//	/**
	//	 * NRCOT
	//	 * 
	//	 * @return boolean
	//	 */ //	private boolean nonrepairableCertificateOfTitle()
	//	//	{
	//	//		return (
	//	//			((SalvageData) cvSalvageData.elementAt(0)).getSlvgCd()
	//	//				== 8);
	//	//	}		

}
