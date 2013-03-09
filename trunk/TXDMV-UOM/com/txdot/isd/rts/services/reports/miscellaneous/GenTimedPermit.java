package com.txdot.isd.rts.services.reports.miscellaneous;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.OneTripData;
import com.txdot.isd.rts.services.data.PermitData;
import com.txdot.isd.rts.services.data.PermitFormatData;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.util.PCLUtilities;
import com.txdot.isd.rts.services.util.Print;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.StickerPrintingConstant;

/*
 * GenTimedPermit.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/14/2010	Created
 * 							defect 10491 Ver 6.5.0
 * K Harrell	07/02/2010	Updated for Motorcycle 
 * 							defect 10491 Ver 6.5.0
 * K Harrell	07/08/2010	Use Tray 1
 * 							modify generatePermit()
 * 							defect 10491 Ver 6.5.0
 * K Harrell	07/10/2010	Remove logic for Bolt Holes; Coming back 
 * 							  for Issuing Office logic 
 * 							Retaining code in case changes 
 * 							defect 10491 Ver 6.5.0   
 * K Harrell	07/14/2010	Use Tray 2
 * 							modify PRMT_ISS_OFC_TEXT, COUNTY, REGION, 
 * 							 REGIONAL OFFICE 
 * 							modify generatePermit() 
 * 			  				defect 10491 Ver 6.5.0
 * K Harrell	01/07/2011	Implement new PermitData methods for  
 * 							  Issuing Office Name 
 * 							delete COUNTY, REGION, REGIONAL_OFFICE  
 * 							modify generatePermit() 
 *							defect 10726 Ver 6.7.0   
 * ---------------------------------------------------------------------
 */

/**
 * Class to print Permit 
 *
 * @version	6.7.0 			01/07/2011
 * @author	Kathy Harrell 
 * <br>Creation Date:		06/14/2010 10:04:24
 */
public class GenTimedPermit extends ReceiptTemplate
{
	private final static String TOP_BORDER = "TOP_BORDER";
	private final static String BOTTOM_BORDER = "BOTTOM_BORDER";
	//	private final static String BOLT1 = "BOLT1";
	//	private final static String BOLT2 = "BOLT2";
	//	private final static String BOLT3 = "BOLT3";
	//	private final static String BOLT4 = "BOLT4";
	private final static String PRMT_TYPE = "PRMT_TYPE";
	private final static String PRMT_TMP_REG = "PRMT_TMP_REG";
	private final static String PRMT_NO = "PRMT_NO";
	private final static String PRMT_EFF_DT_TM = "PRMT_EFF_DT_TM";
	private final static String PRMT_EXPIRES_TXT = "PRMT_EXP_TXT";
	private final static String PRMT_EXP_DT = "PRMT_EXP_DT";
	private final static String PRMT_EXP_TM = "PRMT_EXP_TM";
	private final static String PRMT_VEHYRMAKE = "PRMT_VEHYRMAKE";
	private final static String PRMT_VIN = "PRMT_VIN";
	private final static String PRMT_ORGN = "PRMT_ORGN";
	private final static String PRMT_DESTN = "PRMT_DESTN";
	private final static String PRMT_ISS_OFC = "PRMT_ISS_OFC";
	private final static String PRMT_MSG = "PRMT_MSG";

	private final static String MC_TOP_BORDER = "MC_TOP_BORDER";
	private final static String MC_BOTTOM_BORDER = "MC_BOTTOM_BORDER";
	//	private final static String MC_BOLT1 = "MC_BOLT1";
	//	private final static String MC_BOLT2 = "MC_BOLT2";
	//	private final static String MC_BOLT3 = "MC_BOLT3";
	//	private final static String MC_BOLT4 = "MC_BOLT4";
	private final static String MC_PRMT_TYPE = "MC_PRMT_TYPE";
	private final static String MC_PRMT_TMP_REG = "MC_PRMT_TMP_REG";
	private final static String MC_PRMT_NO = "MC_PRMT_NO";
	private final static String MC_PRMT_EFF_DT_TM = "MC_PRMT_EFF_DT_TM";
	private final static String MC_PRMT_EXPIRES_TXT = "MC_PRMT_EXP_TXT";
	private final static String MC_PRMT_EXP_DT = "MC_PRMT_EXP_DT";
	private final static String MC_PRMT_EXP_TM = "MC_PRMT_EXP_TM";
	private final static String MC_PRMT_VEHYRMAKE = "MC_PRMT_VEHYRMAKE";
	private final static String MC_PRMT_VIN = "MC_PRMT_VIN";
	private final static String MC_PRMT_ORGN = "MC_PRMT_ORGN";
	private final static String MC_PRMT_DESTN = "MC_PRMT_DESTN";
	private final static String MC_PRMT_ISS_OFC = "MC_PRMT_ISS_OFC";
	private final static String MC_PRMT_MSG = "MC_PRMT_MSG";

	private final static String PRMT_TMP_REG_TEXT =
		"THIS VEHICLE IS TEMPORARILY REGISTERED WITH PERMIT #";
	private final static String PRMT_EFF_DT_TEXT = "Effective Date ";
	private final static String PRMT_EXPIRES_TEXT = "EXPIRES ";
	private final static String PRMT_VIN_TEXT = "VIN: ";
	private final static String PRMT_ORGN_PT_TEXT =
		"ORIGINATION POINT: ";
	private final static String PRMT_DESTN_TEXT = "DESTINATION POINT: ";
	private final static String PRMT_ISS_OFC_TEXT = "ISSUED BY: ";
	private final static String PRMT_MSG_TEXT =
		"RECEIPT FOR PERMIT MUST BE CARRIED IN THE VEHICLE AT ALL TIMES";
	private final static String BORDER_TEXT =
		"____________________________________________________________________________";
	private final static String TEXAS = "TEXAS ";
	private final static String NO_VIN = " *** NO VIN *** ";
	
	// defect 10726 
	//	private final static String COUNTY = " CTY";
	//	private final static String REGION = " RO";
	//	private final static String REGIONAL_OFFICE = "REGIONAL OFFICE";
	// end defect 10726
	 
	private CompleteTransactionData caCTData;
	
	private static final int TRANSOBJPOSITION = 0;

	static {
		loadPermitFormatValues();
	}

	/**
	 * Loads all of the layout values for the permit elements into a
	 * hashtable for lookup everytime a permit is printed.
	 * 
	 */
	private static void loadPermitFormatValues()
	{
		PermitFormatData laPermitFormatData;
		// TOP BORDER 
		laPermitFormatData =
			new PermitFormatData(
				TOP_BORDER,
				3840,
				660,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.CENTER);

		//		// BOLT1 
		//		laPermitFormatData =
		//			new PermitFormatData(
		//				BOLT1,
		//				1000,
		//				1310,
		//				130,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.LEFT);
		//
		//		// BOLT2 
		//		laPermitFormatData =
		//			new PermitFormatData(
		//				BOLT2,
		//				5750,
		//				1310,
		//				130,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.LEFT);
		//
		//		// BOLT3 
		//		laPermitFormatData =
		//			new PermitFormatData(
		//				BOLT3,
		//				1000,
		//				4350,
		//				130,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.LEFT);
		//
		//		// BOLT4 
		//		laPermitFormatData =
		//			new PermitFormatData(
		//				BOLT4,
		//				5750,
		//				4350,
		//				130,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.LEFT);

		// PERMIT TYPE 
		laPermitFormatData =
			new PermitFormatData(
				PRMT_TYPE,
				3840,
				1410,
				26,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// PERMIT REGISTERED MSG 
		laPermitFormatData =
			new PermitFormatData(
				PRMT_TMP_REG,
				2400,
				1550,
				9,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// PERMIT REGISTERED PERMIT NO 
		laPermitFormatData =
			new PermitFormatData(
				PRMT_NO,
				3840,
				2750,
				130,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// PERMIT EFFECTIVE DATE/TIME
		laPermitFormatData =
			new PermitFormatData(
				PRMT_EFF_DT_TM,
				3850,
				3050,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// PERMIT EXPIRES TEXT 
		laPermitFormatData =
			new PermitFormatData(
				PRMT_EXPIRES_TXT,
				1300,
				3550,
				28,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// PERMIT EXPIRE DATE 
		laPermitFormatData =
			new PermitFormatData(
				PRMT_EXP_DT,
				2600,
				3550,
				50,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// PERMIT EXPIRE TIME  
		laPermitFormatData =
			new PermitFormatData(
				PRMT_EXP_TM,
				5350,
				3550,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// VEH YR MAKE   
		laPermitFormatData =
			new PermitFormatData(
				PRMT_VEHYRMAKE,
				3840,
				3875,
				26,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// VIN   
		laPermitFormatData =
			new PermitFormatData(
				PRMT_VIN,
				1900,
				4150,
				14,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// ISSUING OFFICE    
		laPermitFormatData =
			new PermitFormatData(
				PRMT_ISS_OFC,
				4000,
				4150,
				14,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// ORIGIN     
		laPermitFormatData =
			new PermitFormatData(
				PRMT_ORGN,
				1900,
				4290,
				11,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.LEFT);

		// DESTN      
		laPermitFormatData =
			new PermitFormatData(
				PRMT_DESTN,
				1900,
				4410,
				11,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.LEFT);

		// MESSAGE      
		laPermitFormatData =
			new PermitFormatData(
				PRMT_MSG,
				1800,
				4580,
				11,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// BOTTOM BORDER 
		laPermitFormatData =
			new PermitFormatData(
				BOTTOM_BORDER,
				3840,
				4980,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.CENTER);

		// MOTORCYCLE TOP BORDER 
		laPermitFormatData =
			new PermitFormatData(
				MC_TOP_BORDER,
				3840,
				1310,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.CENTER);

		//		// MOTORCYCLE BOLT1 
		//		laPermitFormatData =
		//			new PermitFormatData(
		//				MC_BOLT1,
		//				1200,
		//				1700,
		//				130,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.LEFT);
		//
		//		// MOTORCYCLE BOLT2 
		//		laPermitFormatData =
		//			new PermitFormatData(
		//				MC_BOLT2,
		//				5350,
		//				1700,
		//				130,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.LEFT);
		//
		//		// MOTORCYCLE BOLT3 
		//		laPermitFormatData =
		//			new PermitFormatData(
		//				MC_BOLT3,
		//				1200,
		//				3700,
		//				130,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.LEFT);
		//
		//		// MOTORCYCLE BOLT4 
		//		laPermitFormatData =
		//			new PermitFormatData(
		//				MC_BOLT4,
		//				5350,
		//				3700,
		//				130,
		//				StickerPrintingConstant.ARIAL,
		//				StickerPrintingConstant.MEDIUM,
		//				StickerPrintingConstant.LEFT);

		// MOTORCYCLE PERMIT TYPE 
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_TYPE,
				3840,
				1710,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// MOTORCYCLE PERMIT REGISTERED MSG 
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_TMP_REG,
				2635,
				1850,
				8,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE PERMIT REGISTERED PERMIT NO 
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_NO,
				3840,
				2700,
				97,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// MOTORCYCLE PERMIT EFFECTIVE DATE/TIME
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_EFF_DT_TM,
				3850,
				2920,
				12,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// MOTORCYCLE PERMIT EXPIRES TEXT 
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_EXPIRES_TXT,
				2400,
				3215,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE PERMIT EXPIRE DATE 
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_EXP_DT,
				3150,
				3215,
				24,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE PERMIT EXPIRE TIME  
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_EXP_TM,
				4500,
				3215,
				14,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE VEH YR MAKE   
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_VEHYRMAKE,
				3840,
				3420,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// MOTORCYCLE VIN   
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_VIN,
				1900,
				3605,
				12,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE ISSUING OFFICE    
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_ISS_OFC,
				3800,
				3605,
				12,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE ORIGIN     
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_ORGN,
				2600,
				3710,
				8,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE DESTN      
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_DESTN,
				2600,
				3810,
				8,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE MESSAGE      
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_MSG,
				2200,
				3910,
				8,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE BOTTOM BORDER 
		laPermitFormatData =
			new PermitFormatData(
				MC_BOTTOM_BORDER,
				3840,
				4200,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.MEDIUM,
				StickerPrintingConstant.CENTER);
	}

	/**
	 * GenTimedPermit.java Constructor
	 * 
	 * 
	 */
	public GenTimedPermit()
	{
		super();
		caRcptProps.setPageHeight(77);
		caRcptProps.setPageWidth(132);
	}

	/**
	 * Retrieve PCL data to write to file 
	 * 
	 * @param asPrmtFrmt
	 * @param asText 
	 * @return String 
	 */
	private String getPrintData(String asPrmtFrmt, String asText)
	{
		String lsPrint = new String();

		PermitFormatData laPrmtFrmtData =
			PermitFormatData.getLayout(asPrmtFrmt);

		if (laPrmtFrmtData != null)
		{
			lsPrint =
				PCLUtilities.genStickerLayoutPCL(
					laPrmtFrmtData.getPRMT_HORI(),
					laPrmtFrmtData.getPRMT_VERT(),
					asText,
					laPrmtFrmtData.getPRMT_JUST(),
					laPrmtFrmtData.getPRMT_FONT(),
					laPrmtFrmtData.getPRMT_FONT_SIZE(),
					laPrmtFrmtData.getPRMT_STROKE());
		}
		return lsPrint;
	}

	/**
	 * Format Receipt  
	 * 
	 * @param avData 
	 */
	public void formatReceipt(Vector avData)
	{
		caCTData =
			(CompleteTransactionData) avData.elementAt(
				TRANSOBJPOSITION);

		if (caCTData.getTimedPermitData() instanceof PermitData)
		{
			String lsPageProps =
				Print.getPERMIT_TOP_MARGIN() + Print.getPRINT_TRAY_2();

			lsPageProps =
				lsPageProps.substring(0, 2)
					+ Print.getPRINT_LANDSCAPE()
					+ lsPageProps.substring(2);

			this.caRpt.printAttributesNoReturn(lsPageProps);

			generatePermit();
		}
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Generate Permit  
	 */
	private void generatePermit()
	{
		PermitData laPrmtData =
			(PermitData) caCTData.getTimedPermitData();

		String lsPrmtNo = laPrmtData.getPrmtNo();
		String lsItmCd = laPrmtData.getItmCd();
		String lsPrmtType = ItemCodesCache.getItmCdDesc(lsItmCd);
		lsPrmtType = TEXAS + lsPrmtType;
		RTSDate laEffDate = laPrmtData.getRTSDateEffDt();
		String lsEffDate = laEffDate.getMMDDYYYY("-");
		String lsEffTime = laEffDate.getTimeSS();
		RTSDate laExpDate = laPrmtData.getRTSDateExpDt();
		String lsExpDate = laExpDate.getMMDDYYYY("-");
		String lsExpTime = laExpDate.getTimeSS();
		String lsVehModlYr = "" + laPrmtData.getVehModlYr();
		String lsVIN = laPrmtData.getVin();
		if (UtilityMethods.isEmpty(lsVIN))
		{
			lsVIN = NO_VIN;
		}
		String lsVehMk = laPrmtData.getVehMk();
		String lsVehMkDesc = laPrmtData.getVehMkDesc();

		// defect 10726 
		String lsIssuingOfcName = laPrmtData.getIssuingOfcName();
		// defect 10726 

		String lsOrgn = new String();
		String lsDestn = new String();
		OneTripData laOTData = laPrmtData.getOneTripData();
		if (laOTData != null)
		{
			lsOrgn = laOTData.getOrigtnPnt();
			lsDestn = laOTData.getDestPnt();
		}
		boolean lbMC =
			laPrmtData.getItmCd().equals(
				MiscellaneousRegConstant.ITMCD_30MCPT)
				|| laPrmtData.getItmCd().equals(
					MiscellaneousRegConstant.ITMCD_OTMCPT);

		String lsBorder =
			getPrintData(
				lbMC ? MC_TOP_BORDER : TOP_BORDER,
				BORDER_TEXT);
		caRpt.print(lsBorder, 0, lsBorder.length());

		//		String lsPermit = getPrintData(lbMC ? MC_BOLT1 : BOLT1, ".");
		//		caRpt.print(lsPermit, 0, lsPermit.length());
		//
		//		lsPermit = getPrintData(lbMC ? MC_BOLT2 : BOLT2, ".");
		//		caRpt.print(lsPermit, 0, lsPermit.length());

		// Permit Type 
		String lsPermit =
			getPrintData(lbMC ? MC_PRMT_TYPE : PRMT_TYPE, lsPrmtType);
		caRpt.print(lsPermit, 0, lsPermit.length());

		// Permit Number 
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_TMP_REG : PRMT_TMP_REG,
				PRMT_TMP_REG_TEXT);
		caRpt.print(lsPermit, 0, lsPermit.length());

		lsPermit = getPrintData(lbMC ? MC_PRMT_NO : PRMT_NO, lsPrmtNo);
		caRpt.print(lsPermit, 0, lsPermit.length());

		// Effective Date 
		lsEffDate =
			PRMT_EFF_DT_TEXT + " " + lsEffDate + "  " + lsEffTime;
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_EFF_DT_TM : PRMT_EFF_DT_TM,
				lsEffDate);
		caRpt.print(lsPermit, 0, lsPermit.length());

		// Expires 
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_EXPIRES_TXT : PRMT_EXPIRES_TXT,
				PRMT_EXPIRES_TEXT);
		caRpt.print(lsPermit, 0, lsPermit.length());

		// Expiration Date 
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_EXP_DT : PRMT_EXP_DT,
				lsExpDate);
		caRpt.print(lsPermit, 0, lsPermit.length());

		// Expiration Time 
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_EXP_TM : PRMT_EXP_TM,
				lsExpTime);
		caRpt.print(lsPermit, 0, lsPermit.length());

		if (UtilityMethods.isEmpty(lsVehMkDesc))
		{
			lsVehMkDesc = lsVehMk;
		}
		lsVehMk = lsVehModlYr + "  " + lsVehMkDesc;

		// Vehicle Year / Make 
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_VEHYRMAKE : PRMT_VEHYRMAKE,
				lsVehMk);
		caRpt.print(lsPermit, 0, lsPermit.length());

		// VIN 
		lsVIN = PRMT_VIN_TEXT + lsVIN;
		lsPermit = getPrintData(lbMC ? MC_PRMT_VIN : PRMT_VIN, lsVIN);
		caRpt.print(lsPermit, 0, lsPermit.length());

		//		// BOLT3
		//		lsPermit = getPrintData(lbMC ? MC_BOLT3 : BOLT3, ".");
		//		caRpt.print(lsPermit, 0, lsPermit.length());
		//
		//		// BOLT4
		//		lsPermit = getPrintData(lbMC ? MC_BOLT4 : BOLT4, ".");
		//		caRpt.print(lsPermit, 0, lsPermit.length());

		// ISSUING OFFICE 
		lsIssuingOfcName = PRMT_ISS_OFC_TEXT + lsIssuingOfcName;
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_ISS_OFC : PRMT_ISS_OFC,
				lsIssuingOfcName);
		caRpt.print(lsPermit, 0, lsPermit.length());

		if (laPrmtData.isOTPT())
		{
			lsOrgn = PRMT_ORGN_PT_TEXT + lsOrgn;
			lsPermit =
				getPrintData(lbMC ? MC_PRMT_ORGN : PRMT_ORGN, lsOrgn);
			caRpt.print(lsPermit, 0, lsPermit.length());
			lsDestn = PRMT_DESTN_TEXT + lsDestn;
			lsPermit =
				getPrintData(
					lbMC ? MC_PRMT_DESTN : PRMT_DESTN,
					lsDestn);
			caRpt.print(lsPermit, 0, lsPermit.length());
		}
		// PRMT MESSAGE 
		lsPermit =
			getPrintData(lbMC ? MC_PRMT_MSG : PRMT_MSG, PRMT_MSG_TEXT);
		caRpt.print(lsPermit, 0, lsPermit.length());

		lsBorder =
			getPrintData(
				lbMC ? MC_BOTTOM_BORDER : BOTTOM_BORDER,
				BORDER_TEXT);
		caRpt.print(lsBorder, 0, lsBorder.length());
	}
}
