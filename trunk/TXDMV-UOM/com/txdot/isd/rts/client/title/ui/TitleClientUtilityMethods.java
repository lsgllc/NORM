package com.txdot.isd.rts.client.title.ui;

import java.util.Vector;

import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientBusiness;

import com.txdot.isd.rts.services.cache.CommercialVehicleWeightsCache;
import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.OdometerBrands;
import com.txdot.isd.rts.services.util.constants.TrailerTypes;

/*
 *
 * TitleClientUtilityMethods.java 
 *
 * (c) Texas Department of Transportation  2001
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	-------------------------------------------- 
 * J Rue		08/23/2002	correct logic for DTA-Record 
 *							notApplicable. 
 *							add new method createDlrEmptyVehInqObj to 
 *							retain DTA information.
 *							defect 4648 
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * J Rue		07/22/2003	Check for null before 
 *							accessing Lienholder Data
 *							method createDlrEmptyVehInqObj()
 *							defect 5941, 6044 
 * J Rue		08/12/2003	Remove tonnage, mileage, empty weight when 
 *							DTA and Record Not Applicable
 *							method createDlrEmptyVehInqObj()
 *							defect 5941, Ver 5.1.4
 * J Rue		08/25/2004	DealerTitleData.NewRegExpMo() and 
 *							DealerTitleData.NewRegExpYr() were converted 
 *							to integer.
 *							methods createDlrEmptyVehInqObj()
 *							defect 7496 Ver 5.2.1
 * J Rue		11/18/2004	Retain DTA Lien data for Record Not 
 *							Applicable
 *							modify createDlrEmptyVehInqObj()
 *							defect 6797 Ver 5.2.2
 * K Harrell	12/16/2004	Change reference from setNewNewStkrNo() to
 *							setNewStkrNo(); 
 *							Formatting/JavaDoc/Variable Name Cleanup 
 *							modify createDlrEmptyVehInqObj()
 *							defect 7736 Ver 5.2.2 
 * J Rue		03/22/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * K Harrell	05/03/2005	Remove reference to deprecated 
 * 							RegistrationMiscData as	well as other 
 * 							variables flagged by WSAD as not referenced. 
 * 							modify createDlrEmptyVehInqObj(),
 * 							createEmptyVehInqObj()
 * 							defect 8188  Ver 5.2.3 
 * J Rue		05/06/2005	Clean up code
 * 							Mark deprecated methods
 *							deprecated isValidOdometerReading() 								
 * 							defect 7898, Ver 5.2.3
 * K Harrell	05/17/2005	rename RegistrationClassData.getDiesleReqd()
 * 							to getDieselReqd()
 * 							defect 7786  Ver 5.2.3 
 * J Rue		08/23/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/07/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Replace magic nums with meaningful verbage.
 * 							defect 7898 Ver 5.2.3    
 * B Hargrove	12/19/2005	For new Seasonal Ag RegClassCds (70 - 76),
 *							we have to add to the ugly hard-coded 
 *							RegClassCds in the weight validation methods. 
 *							Also found that RegClassCd 62
 *							(Farm TrkTrac > 1 ton) was left out of 
 *							original list, so we added it. 
 *							modify isGrossWtInvalidForCombVeh(),
 *							isVehTonInvalidForTruckGrt1orComb(),
 *							isVehTonInvalidForTruckLsEqOne()
 *							defect 8481 Ver 5.2.2 Fix 7 
 * B Hargrove	02/13/2006	Non-titled trailers with a gross wt > 4000 
 * 							needs to be titled (current edit is > 4001).
 *							modify isNonTtlTrlrWtValid()
 *							defect 7898 Ver 5.2.3 
 * J Rue		09/16/2008	Correct setting LienDate2 and LienDate3
 * 							modify addTitleDataToDlrTitleData()
 * 							defect 8603 Ver Defect_POS_B
 * J Rue		09/23/2008	Remove Carrying Capacity
 * 							modify createDlrEmptyVehInqObj()
 * 							defect 8783 Ver Defect_POS_B
 * B Hargrove	12/31/2008	Add check for Vehicle Class when checking
 * 							Trk<=1 (Apportioned (RegClassCd 6) does not
 * 							have 'Appr<=1' type of RegClassCd, so must 
 * 							check VehClassCd = 'Trk <=1').
 * 							modify isVehTonInvalidForTruckLsEqOne()
 * 							defect 8703 Ver Defect_POS_D
 * J Rue		01/09/2009	Move method createDlrEmptyVehInqOb()
 * 							to VehicleInquiryData class
 * 							delete createDlrEmptyVehInqObj()
 * 							defect 8631 Ver Defect_POS_D
 * B Hargrove	01/10/2009	Add check for Vehicle Class when checking
 * 							Trk>1 in isVehTonInvalidForTruckGrt1orComb().
 * 							Remove hard-coded lists of RegClassCds in 
 * 							both '<=1' and '>1' methods and use VehClass
 * 							and isWeightBasedFee() instead.
 * 							modify isVehTonInvalidForTruckGrt1orComb(),
 * 							isVehTonInvalidForTruckLsEqOne()
 * 							defect 9910 Ver Defect_POS_D
 * J Rue		01/22/2009	Clean up javadoc comments for 01/09/2009
 * 							Remove comments for 12/31/2008 and 01/07/2009
 * 							for defect 8631. Those comments were no 
 * 							longer valid for defect 8631
 * 							defect 8631 Ver Defect_POS_D
 * B Hargrove	03/05/2009	(Added in from POS_D)
 * 							Defect 9129 changed how we determine 'weight
 * 							based fee. Now need to add check for 'or
 * 							Apportioned' (RegClassCd 6).
 * 							modify isVehTonInvalidForTruckLsEqOne(),
 * 							isVehTonInvalidForTruckGrt1orComb()
 * 							defect 8703 Ver Defect_POS_D
 * K Harrell	03/05/2009	Add methods for ELT; add'l cleanup. 
 * 							add isValidPermLienhldrId(), 
 * 							 getPermLienhldrLabel()
 * 							delete isIdZero(), isValidOdometerReading(int),
 * 							 createEmptyRegData(), getRegClass(),
 * 							 isCarCapInvalid()  
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	07/02/2009	Implement new TitleData
 * 							modify createEmptyVehInqObj()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	11/07/2011	add validateGrossWtForRegClassCd() 
 * 							delete EMPTY_WT_18000, REGCLASSCD_10, 
 * 							  EMPTY_WT_6000, MAX_WT
 * 							delete isGrossWtInvalidForCombVeh()
 * 							  isCarrCapBlank(), 
 * 							  isEmptyWtInvalidForRegClassGrt6000(), 
 * 							  isEmptyWtInvalidForRegClassLsEq6000(), 
 * 							  isInvalidEmptyWt(), getRegClassCdDesc() 
 * 							modify isCarrCapInvalid()
 * 							defect 10959 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * Utility class for the Title Event
 *
 * @version	6.9.0 		11/07/2011
 * @author	Ashish Mahajan
 * @since 				10/22/01 18:30:30 
 */

public class TitleClientUtilityMethods
{
	// Constants
	private static final int LOWER_LIMIT = 1;
	private static final int UPPER_LIMIT = 999999;
	
	// defect 10959 
	// private static final int MAX_WT = 80000;
	// private static final int EMPTY_WT_6000 = 6000;
	// private static final int EMPTY_WT_18000 = 18000;
	// private static final int REGCLASSCD_10 = 10;
	// end defect 10959 

	private static final String MISC = "MISC";
	private static final String TRLR = "TRLR";
	private static final String SEMI = "SEMI";
	private static final String FULL = "FULL";
	private static final String DPP = "DPP";
	private static final String DVP = "DVP";
	private static final String DVPF = "DVPF";
	// defect 9910	
	private static final String TRK_GTR_ONE = "TRK>1";
	// end defect 9910

	// defect 9971 
	private static final String ID_COLON_SPACE = "Id: ";

	// defect 8703	
	private static final String TRK_LESS_ONE = "TRK<=1";
	private static final int APPORTIONED = 6;
	// end defect 8703


	/**
	 * TitleClientUtilityMethods constructor comment.
	 */
	public TitleClientUtilityMethods()
	{
		super();
	}

	/**
	 * Create Empty Vehicle Inquiry Object
	 *
	 * @return VehicleInquiryData
	 */
	public static VehicleInquiryData createEmptyVehInqObj()
	{
		VehicleInquiryData laVehInqData = new VehicleInquiryData();
		MFVehicleData laMfVehData = new MFVehicleData();
		TitleData laTtlData = new TitleData();
		VehicleData laVehData = new VehicleData();
		RegistrationData laRegData = new RegistrationData();
		OwnerData laOwnrData = new OwnerData();
		SalvageData laSlvgData = new SalvageData();
		AddressData laAddrDataTtl = new AddressData();
		laTtlData.setTtlVehAddr(laAddrDataTtl);

		AddressData laAddrDataReg = new AddressData();
		laRegData.setRenwlMailAddr(laAddrDataReg);

		laVehInqData.setMfVehicleData(laMfVehData);
		laMfVehData.setOwnerData(laOwnrData);
		laMfVehData.setRegData(laRegData);
		laMfVehData.setVctSalvage(new Vector());
		laMfVehData.getVctSalvage().add(laSlvgData);
		laMfVehData.setTitleData(laTtlData);
		laMfVehData.setVehicleData(laVehData);

		return laVehInqData;
	}

	/**
	 * Return vector of OdometerBrands
	 * 
	 * @return Vector
	 */
	public static Vector getOdometerBrands()
	{
		Vector lvVC = new Vector(3);
		lvVC.add(OdometerBrands.ACTUAL);
		lvVC.add(OdometerBrands.NOTACT);
		lvVC.add(OdometerBrands.EXCEED);
		return lvVC;
	}

//	/**
//	 * Return value of Plate Type Description
//	 * 
//	 * @param asItmCd String
//	 * @return String
//	 */
//	public static String getPlateTypeDesc(String asItmCd)
//	{
//		if (asItmCd != null)
//		{
//			ItemCodesData laItmData = ItemCodesCache.getItmCd(asItmCd);
//			if (laItmData != null)
//			{
//				return laItmData.getItmCdDesc();
//			}
//		}
//		return null;
//	}

	/**
	 * Return label for Permanent Lienholder Id 
	 * 
	 * @param asPermLienId
	 * @return String
	 */
	public static String getPermLienhldrLabel(String asPermLienId)
	{
		String lsPermId = CommonConstant.STR_SPACE_EMPTY;

		if (isValidPermLienhldrId(asPermLienId))
		{
			lsPermId = ID_COLON_SPACE + asPermLienId;
		}
		return lsPermId;
	}

//	/**
//	 * Return value of Registration Class Description
//	 * 
//	 * @param aiCd int
//	 * @param aiDt int
//	 * @return String
//	 */
//	public static String getRegClassCdDesc(int aiCd, int aiDt)
//	{
//		CommonFeesData laFeeData =
//			CommonFeesCache.getCommonFee(aiCd, aiDt);
//		if (laFeeData != null)
//		{
//			return laFeeData.getRegClassCdDesc();
//		}
//		else
//		{
//			return null;
//		}
//
//	}

	/**
	 * Return vector of Trailer Types
	 * 
	 * @return Vector
	 */
	public static Object getTrailerTypes()
	{
		Vector lvVC = new Vector(2);
		lvVC.add(TrailerTypes.SEMI);
		lvVC.add(TrailerTypes.FULL);
		return lvVC;
	}

//	/**
//	 * Return boolean based on wheter Carrying Capacity blank
//	 * 
//	 * @param asCarrCap String
//	 * @return boolean
//	 */
//	public static boolean isCarrCapBlank(String asCarrCap)
//	{
//		boolean lbRet = false;
//		if (asCarrCap != null)
//		{
//			asCarrCap = asCarrCap.trim();
//			if (asCarrCap.length() == 0)
//			{
//				return true;
//			}
//			try
//			{
//				int liCarrCap = Integer.parseInt(asCarrCap);
//				if (!(liCarrCap > 0))
//				{
//					lbRet = true;
//				}
//			}
//			catch (NumberFormatException aeNFEx)
//			{
//				lbRet = true;
//			}
//		}
//		return lbRet;
//	}

	/**
	 * Return boolean to denote validity of Carrying Capacity
	 * 
	 * @param aaRegData RegistrationClassData
	 * @param asGrossWt String
	 * @param asCarrCap String
	 * @param asTon String
	 * @param aiFxdWtIndi int
	 * @return boolean
	 */
	public static boolean isCarrCapInvalid(
		RegistrationClassData aaRegData,
		String asGrossWt,
		String asCarrCap,
		String asTon,
		int aiFxdWtIndi)
	{
		boolean lbRet = false;
		if (aaRegData != null
			&& aaRegData.getCaryngCapValidReqd() == 1)
		{
			// defect 10959 
			//			if (asGrossWt.length() > 0)
			//			{
			//				int liGrossWt = Integer.parseInt(asGrossWt);
			//				if (liGrossWt != MAX_WT)
			//				{
			// end defect 10959 
			if (asCarrCap.length() > 0)
			{
				long llCarrCap = 0;

				try
				{
					llCarrCap = Integer.parseInt(asCarrCap);
				}
				catch (NumberFormatException aeNFEx)
				{
					llCarrCap = 0;
				}

				Dollar laTon = null;
				try
				{
					laTon = new Dollar(asTon);
				}
				catch (Exception aeEx)
				{
					laTon =
						new Dollar(
								CommonConstant.STR_ZERO_DOLLAR);
				}
				CommercialVehicleWeightsData laComData =
					CommercialVehicleWeightsCache
					.getCommVehWts(
							laTon);

				if (aiFxdWtIndi == 0
						&& aaRegData.getTonReqd() == 1
						&& laComData != null
						&& llCarrCap < laComData.getMinCaryngCap())
				{
					lbRet = true;
				}
			}
		}
		// defect 10959 
//			}
//		}
		// end defect 10959 
		return lbRet;
	}
	
//	/**
//	 * Return boolean to denote validity of EmptyWeight
//	 * 
//	 * @param aiRegCd int
//	 * @param asEmptyWt String
//	 * @return boolean
//	 */
//	public static boolean isEmptyWtInvalidForRegClassGrt6000(
//		int aiRegCd,
//		String asEmptyWt)
//	{
//		boolean lbRet = false;
//		if (aiRegCd == 26
//			|| aiRegCd == 40
//			|| aiRegCd == 41
//			|| aiRegCd == 50
//			|| aiRegCd == 53
//			|| aiRegCd == 56
//			|| aiRegCd == 59)
//		{
//			if (asEmptyWt.length() > 0)
//			{
//				int liEmptWt = Integer.parseInt(asEmptyWt);
//				if (liEmptWt <= EMPTY_WT_6000)
//				{
//					lbRet = true;
//				}
//			}
//		}
//
//		return lbRet;
//	}

//	/**
//	 * Return boolean to denote validity of EmptyWeight
//	 * 
//	 * @param aiRegCd int
//	 * @param asEmptyWt String
//	 * @return boolean
//	 */
//	public static boolean isEmptyWtInvalidForRegClassLsEq6000(
//		int aiRegCd,
//		String asEmptyWt)
//	{
//		boolean lbRet = false;
//		if (aiRegCd == 8
//			|| aiRegCd == 25
//			|| aiRegCd == 28
//			|| aiRegCd == 48
//			|| aiRegCd == 52
//			|| aiRegCd == 55
//			|| aiRegCd == 58)
//		{
//			if (asEmptyWt.length() > 0)
//			{
//				int liEmptWt = Integer.parseInt(asEmptyWt);
//				if (liEmptWt > EMPTY_WT_6000)
//				{
//					lbRet = true;
//				}
//			}
//		}
//
//		return lbRet;
//	}

	/**
	 * Return boolean to denote validity of Fixed Weight
	 * 
	 * @param aaRegdata RegistrationClassData
	 * @param asTon String
	 * @param asGrossWt String
	 * @return boolean
	 */
	public static boolean isFixedWtIndi(
		RegistrationClassData aaRegData,
		String asTon,
		String asGrossWt)
	{
		boolean lbRet = false;

		//if(getchkFixedWeight().isSelected())
		if (asTon != null && asTon.length() > 0)
		{

			//String strTon = gettxtVehicleTonnage().getText().trim();
			Dollar laTon = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
			try
			{
				laTon = new Dollar(asTon);
			}
			catch (Exception aeEx)
			{
				laTon = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
			}

			CommercialVehicleWeightsData laComData =
				CommercialVehicleWeightsCache.getCommVehWts(laTon);

			if (aaRegData != null && laComData != null)
			{
				if (asGrossWt != null && asGrossWt.length() > 0)
				{
					int liGrossWt = Integer.parseInt(asGrossWt);
					if ((aaRegData.getTonReqd() == 1)
						&& (liGrossWt
							< laComData.getMinGrossWtAllowble()))
					{
						lbRet = true;
					}
				}
			}
		}

		return lbRet;
	}

	/**
	 * Return boolean to determine whether:
	 *  <ol>
	 *  <li> Gross Weight less than Min Farm Trailer Wt
	 *  <li> Gross Weight greater than Max Farm Trailer Wt
	 *  <eol> 
	 * 
	 * @param aaRegisClassData RegistrationClassData
	 * @param asGrossWt String
	 * @return boolean
	 */
	public static boolean isFrmTrlMinWt(
		RegistrationClassData aaRegisClassData,
		String asGrossWt)
	{
		boolean lbRet = false;
		if (aaRegisClassData != null && aaRegisClassData.getFarmTrlrMinWt() != 0)
		{
			if (asGrossWt.length() > 0)
			{
				int liGrossWt = Integer.parseInt(asGrossWt);
				if (liGrossWt < aaRegisClassData.getFarmTrlrMinWt()
					|| liGrossWt > aaRegisClassData.getFarmTrlrMaxWt())
				{
					lbRet = true;
				}
			}
		}
		return lbRet;
	}

//	/**
//	 * Return boolean to denote validity of Gross Wt for Comb Veh
//	 * 
//	 * @param aiRegCd int
//	 * @param asGrossWt String
//	 * @return boolean
//	 */
//	public static boolean isGrossWtInvalidForCombVeh(
//		int aiRegCd,
//		String asGrossWt)
//	{
//		boolean lbRet = false;
//		// defect 8481
//		// add Seasonal Ag RegClassCd
//		//if(aiRegCd == REGCLASSCD_10)
//		if (aiRegCd == REGCLASSCD_10 || aiRegCd == 76)
//		{
//			// end defect 8481
//			if (asGrossWt.length() > 0)
//			{
//				int liGrossWt = Integer.parseInt(asGrossWt);
//				if (liGrossWt < EMPTY_WT_18000)
//				{
//					lbRet = true;
//				}
//			}
//		}
//
//		return lbRet;
//	}

//	/**
//	 * Return boolean to denote if Invalid Empty Weight
//	 * 
//	 * @param asWt String
//	 * @return boolean
//	 */
//	public static boolean isInvalidEmptyWt(String asWt)
//	{
//		boolean lbRet = false;
//		if (asWt != null)
//		{
//			asWt = asWt.trim();
//			if (asWt.length() == 0)
//			{
//				return true;
//			}
//			try
//			{
//				int liEmptyWt = Integer.parseInt(asWt);
//				if (!(liEmptyWt > 0))
//				{
//					lbRet = true;
//				}
//			}
//			catch (NumberFormatException aeNFEx)
//			{
//				lbRet = true;
//			}
//		}
//		return lbRet;
//	}

	/**
	 * Return boolean to denote if Odometer Reading invalid 
	 * 
	 * @param asVehMkYr String
	 * @param asVehTon String
	 * @param asVehGrossWt String
	 * @param asOdmtRdng String
	 * @return boolean
	 */
	public static boolean isInvalidOdometerReadingBasedOnTIMA(
		String asVehMkYr,
		String asVehTon,
		String asVehGrossWt,
		String asOdmtRdng)
	{
		boolean lbRet = true;
		int liMkYr = 0;
		double ldTon = 0.0;
		int liGrossWt = 0;
		try
		{
			liMkYr = Integer.parseInt(asVehMkYr);
		}
		catch (NumberFormatException aeNFEx)
		{
			liMkYr = 0;

		}

		try
		{
			ldTon = Double.parseDouble(asVehTon);
		}
		catch (NumberFormatException aeNFEx)
		{
			ldTon = 0.0;

		}

		try
		{
			liGrossWt = Integer.parseInt(asVehGrossWt);
		}
		catch (NumberFormatException aeNFWEx)
		{
			liGrossWt = 0;

		}

		RTSDate laCurrDate = RTSDate.getCurrentDate();
		int liCurrYr = laCurrDate.getYear();
		int liMkrPlus10 = liMkYr + 10;
		int liVehAge = liCurrYr - liMkrPlus10;

		if ((liVehAge >= 0 || ldTon > 2.0 || liGrossWt > 16000)
			&& asOdmtRdng.equals(CommonConstant.STR_EXEMPT))
		{
			lbRet = false;
		}

		if ((liVehAge < 0 && ldTon <= 2.0 && liGrossWt <= 16000)
			&& !asOdmtRdng.equals(CommonConstant.STR_EXEMPT))
		{
			lbRet = false;
		}

		return lbRet;
	}

	/**
	 * Return boolean to denote if length is invalid for Park Trailer
	 * 
	 * @param asTrlrLen
	 * @return boolean
	 */
	public static boolean isLengthInvalidForParkTlr(String asTrlLen)
	{
		boolean lbRet = false;

		try
		{
			int liLen = Integer.parseInt(asTrlLen);
			if (liLen <= 45)
			{
				lbRet = true;
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			lbRet = true;
		}

		return lbRet;
	}

	/**
	 * Return boolean to denote if length is invalid for Travel Trailer
	 * 
	 * @param asTrlrLen
	 * @return boolean
	 */
	public static boolean isLengthInvalidForTrvTrlr(String asTrlLen)
	{
		boolean lbRet = false;

		try
		{
			int liLen = Integer.parseInt(asTrlLen);
			if (liLen < 1 || liLen > 39)
			{
				lbRet = true;
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			lbRet = true;
		}

		return lbRet;
	}

	/**
	 * Return boolean to determine if Weight valid for NonTitled Trailer
	 * 
	 * @param aaRegData RegistrationClassData
	 * @param asVehClass String
	 * @param asTrlrType String
	 * @param asEmptyWt String
	 * @return boolean
	 */
	public static boolean isNonTtlTrlrWtValid(
		RegistrationClassData aaRegData,
		String asVehClass,
		String asTrlType,
		String asGrossWt,
		String asEmptyWt)
	{
		boolean lbRet = true;

		if (aaRegData != null
			&& aaRegData.getTrlrWtValidReqd() == 1
			&& asVehClass.equals(MISC)
			&& asGrossWt.length() > 0
			&& asEmptyWt.length() > 0
			&& asTrlType != null)
		{
			int liEmptyWt = Integer.parseInt(asEmptyWt);
			int liGrossWt = Integer.parseInt(asGrossWt);
			// defect 7898
			// this edit is supposed to be > 4000
			//if ((asTrlType.equals(SEMI) && liGrossWt > 4001)
			//	|| (asTrlType.equals(FULL) && liEmptyWt > 4001))
			if ((asTrlType.equals(SEMI) && liGrossWt > 4000)
				|| (asTrlType.equals(FULL) && liEmptyWt > 4000))
			{
				lbRet = false;
			}
			// end defect 7898
		}
		return lbRet;
	}

	/**
	 * Return boolean to denote if Trailer Weight is invalid
	 * 
	 * @param asVehClass String
	 * @param aiRegCd int
	 * @param asTrlrType String
	 * @param asGrossWt String
	 * @param asEmptyWt String
	 * @return boolean
	 */
	public static boolean isTrailerWtInvalid(
		String asVehClass,
		int aiRegCd,
		String asTrlrType,
		String asGrossWt,
		String asEmptyWt)
	{
		boolean lbRet = false;
		if (asVehClass.equals(TRLR) && aiRegCd == 37)
		{
			if (asGrossWt.length() > 0 && asEmptyWt.length() > 0)
			{
				int liGrossWt = Integer.parseInt(asGrossWt);
				int liEmptyWt = Integer.parseInt(asEmptyWt);

				if (asTrlrType.equals(SEMI) && liGrossWt <= 4000)
				{
					lbRet = true;
				}

				if (asTrlrType.equals(FULL) && liEmptyWt <= 4000)
				{
					lbRet = true;
				}
			}
		}
		return lbRet;
	}

	/**
	 * Return boolean to denote if Trailer Carrying Capacity is invalid
	 * 
	 * @param aaRegData RegistrationClassData
	 * @param asEmptyWt String
	 * @param asCarrCap String
	 * @return boolean
	 */
	public static boolean isTrlCarrCapInvalid(
		RegistrationClassData aaRegData,
		String asEmptyWt,
		String asCarrCap)
	{
		boolean lbRet = false;
		if (aaRegData != null && aaRegData.getTrlrCapValidReqd() == 1)
		{
			if (asEmptyWt.length() > 0 && asCarrCap.length() > 0)
			{
				long llCarrCap = Integer.parseInt(asCarrCap);
				int liEmptyWt = Integer.parseInt(asEmptyWt);

				long llThirdEmptyWt = liEmptyWt / 3;

				if (llCarrCap < llThirdEmptyWt)
				{
					lbRet = true;
				}
			}
		}
		return lbRet;
	}

	/**
	 * 
	 * Is Valid PermLienholderId
	 * 
	 * @param asPermLienhldrId
	 * @return boolean
	 */
	public static boolean isValidPermLienhldrId(String asPermLienId)
	{
		return !UtilityMethods.isEmpty(asPermLienId)
			&& !UtilityMethods.isAllZeros(asPermLienId.trim());
	}

	/**
	 * Return boolean to denote validity of Odometer Reading 
	 * 
	 * @param asOdoRdng String
	 * @return boolean
	 */
	public static boolean isValidOdometerReading(String asOdoRdng)
	{
		boolean lbRet = false;
		try
		{
			int liRdng = Integer.parseInt(asOdoRdng);
			if (liRdng >= LOWER_LIMIT && liRdng <= UPPER_LIMIT)
			{
				lbRet = true;
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			if (asOdoRdng.length() == 0
				|| asOdoRdng.equalsIgnoreCase(CommonConstant.STR_EXEMPT))
			{
				lbRet = true;
			}
		}
		return lbRet;
	}

	/**
	 * Return boolean to denote if Veh Ton is Invalid
	 * 
	 * @param asVehTon String
	 * @return boolean
	 */
	public static boolean isVehTonInvalid(String asVehTon)
	{
		boolean lbRet = false;

		try
		{
			// defect 7898
			//	This process determine if parameter can be cast
			Double.parseDouble(asVehTon);
			Dollar laDlrVehTon = new Dollar(asVehTon);
			CommercialVehicleWeightsData laComData =
				CommercialVehicleWeightsCache.getCommVehWts(
					laDlrVehTon);

			if (laComData == null)
			{
				lbRet = true;
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			lbRet = true;
		}
		return lbRet;
	}

	/**
	 * Return boolean to denote if Veh Ton Invalid for Disabled Plates
	 * 
	 * @param asRegPltCd String
	 * @param asVenTon String
	 * @return boolean
	 */
	public static boolean isVehTonInvalidForDisabledPlates(
		String asRegPltCd,
		String asVehTon)
	{
		boolean lbRet = false;

		if (asRegPltCd.equals(DPP)
			|| asRegPltCd.equals(DVP)
			|| asRegPltCd.equals(DVPF))
		{
			if (asVehTon.length() > 0)
			{
				double ldTon = Double.parseDouble(asVehTon);
				if (ldTon > 2.00)
				{
					lbRet = true;
				}
			}
		}
		return lbRet;
	}

	/**
	 * Return boolean to denote if Veh Ton Invalid for Truck > 1Ton 
	 * or Combo
	 * 
	 * @param aiRegCd int
	 * @param asVehClass String
	 * @param asVehTon String
	 * @return boolean
	 */
	public static boolean isVehTonInvalidForTruckGrt1orComb(
		int aiRegCd,
		String asVehClass,
		String asVehTon)
	{
		boolean lbRet = false;

		// defect 9910
		// Add 'asVehClass' Vehicle Class parameter
		// Use 'isWeightBasedFee() instead of hard-coded list
		// defect 8703
		// Also need to check for Apportioned RegClassCd since APPOR is
		// NOT a weight-based fee RegClass.
		// defect 8481
		// add Seasonal Ag RegClassCds
		// added RegClassCd 62 (Farm TrkTrac > 1 ton) because it was
		// left out earlier by mistake.
		//if(aiRegCd == 10 || aiRegCd == 36 || aiRegCd == 43 ||
		//   aiRegCd == 47 || aiRegCd == 61 )
		//if(aiRegCd == 10 || aiRegCd == 36 || aiRegCd == 43 ||
		//   aiRegCd == 47 || aiRegCd == 61 || aiRegCd == 62 || 
		//   aiRegCd == 71 || aiRegCd == 73 || aiRegCd == 74 || 
		//   aiRegCd == 75 || aiRegCd == 76 ||
		//	asVehClass.equals(TRK_GTR_ONE))
		if ((MiscellaneousRegClientBusiness.isWeightBasedFee(aiRegCd)
			|| aiRegCd == APPORTIONED)
			&& asVehClass.equals(TRK_GTR_ONE))
		{
			// end defect 8703
			// end defect 8481
			// end defect 9910
			if (asVehTon.length() > 0)
			{
				double ldTon = Double.parseDouble(asVehTon);
				if (ldTon <= 1.00)
				{
					lbRet = true;
				}
			}
		}
		return lbRet;

	}

	/**
	 * Return boolean to denote if Veh Ton Invalid for Truck <= 1Ton 
	 * or Combo
	 * 
	 * @param aiRegCd int
	 * @param asVehClass String
	 * @param asVehTon String
	 * @return boolean
	 */
	public static boolean isVehTonInvalidForTruckLsEqOne(
		int aiRegCd,
		String asVehClass,
		String asVehTon)
	{
		boolean lbRet = false;

		// defect 9910
		// Use 'isWeightBasedFee() instead of hard-coded list
		// defect 8481
		// add Seasonal Ag RegClassCds
		//if(aiRegCd == 15 || aiRegCd == 35 || aiRegCd == 46 || 
		//   aiRegCd == 49 || aiRegCd == 54 || aiRegCd == 57 || 
		//   aiRegCd == 60)
		// defect 8703
		// Add 'asVehClass' Vehicle Class parameter
		// Add check for vehicle class (ie: Apportioned does not have 
		// specific '<=1 ton' or '> 1 ton' Reg classes)
		// Also need to check for Apportioned RegClassCd since APPOR is
		// NOT a weight-based fee RegClass.
		//if(aiRegCd == 15 || aiRegCd == 35 || aiRegCd == 46 || 
		//   aiRegCd == 49 || aiRegCd == 54 || aiRegCd == 57 || 
		//   aiRegCd == 60 || aiRegCd == 70 || aiRegCd == 72 ||
		//	asVehClass.equals(TRK_LESS_ONE))
		if ((MiscellaneousRegClientBusiness.isWeightBasedFee(aiRegCd)
			|| aiRegCd == APPORTIONED)
			&& asVehClass.equals(TRK_LESS_ONE))
		{
			// end defect 8481
			// end defect 9910
			if (asVehTon.length() > 0)
			{
				double ldTon = Double.parseDouble(asVehTon);
				if (ldTon > 1.00)
				{
					lbRet = true;
				}
			}
		}
		return lbRet;

	}

	/**
	 * Return boolean to denote if Width Valid for Park Trailer 
	 * 
	 * @param asWidth String
	 * @return boolean
	 */
	public static boolean isWidthValidForParkTlr(String asWidth)
	{
		try
		{
			double ldFeet =
				Double.parseDouble(
					asWidth.substring(
						0,
						asWidth.indexOf(CommonConstant.STR_PERIOD)));
			double ldInches =
				Double.parseDouble(
					asWidth.substring(
						asWidth.indexOf(CommonConstant.STR_PERIOD)
							+ 1));
			double ldWidth = (ldFeet * 12) + ldInches;
			return (ldWidth > 102.0);
		}
		catch (NumberFormatException aeNFEx)
		{
			return false;
		}
	}
	/**
	 * Return boolean to denote if Width Valid for Park Trailer 
	 * 
	 * @param asWidth String
	 * @return boolean
	 */
	public static boolean isWidthValidForParkTlr(int aiWidth)
	{
		return aiWidth > 102; 
	}
	
	/**
	 * Validate Gross Weight for RegClassCd 
	 * 
	 * @param aiRegCd
	 * @param aiGrossWt
	 * @throws RTSException
	 */
	public static void  validateGrossWtForRegClassCd(
			int aiRegCd,
			int aiGrossWt)
			throws RTSException 
	{
		CommonFeesData laCommFeesData = CommonFeesCache.getCommonFee(aiRegCd,new RTSDate().getYYYYMMDDDate());

		if (laCommFeesData != null)
		{
			int liRegClassMinWt = laCommFeesData.getRegClassMinWt();
			int liRegClassMaxWt = laCommFeesData.getRegClassMaxWt();

			if (aiGrossWt < liRegClassMinWt || aiGrossWt > liRegClassMaxWt)
			{
				String lsAppend = "";
				if (liRegClassMinWt == 1)
				{
					lsAppend = "LESS THAN OR EQUAL TO "+ liRegClassMaxWt;  
				}
				else
				{
					lsAppend = "BETWEEN "+ liRegClassMinWt 
					+ " AND " + liRegClassMaxWt; 	
				}

				throw 
					new RTSException(
						ErrorsConstant.ERR_NUM_INVALID_WEIGHT_RANGE,
						new String[] { lsAppend +"." });
			}

		}
	}
}
