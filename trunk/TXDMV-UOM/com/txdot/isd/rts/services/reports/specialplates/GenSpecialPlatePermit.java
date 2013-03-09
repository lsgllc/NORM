package com.txdot.isd.rts.services.reports.specialplates;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.util.PCLUtilities;
import com.txdot.isd.rts.services.util.Print;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.StickerPrintingConstant;

/*
 * GenSpecialPlatePermit.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/17/2010	Created
 * 							defect 10700 Ver 6.7.0 
 * K Harrell	04/28/2011	New assessment for Motorcycle Plate 
 * 							modify generatePermit() 
 * 							defect 10804 Ver 6.7.0   
 * K Harrell	08/22/2011	Implement PlateTypeData.isSmallPlt() 
 * 							modify generatePermit()
 * 							defect 10804 Ver 6.8.1 
 * ---------------------------------------------------------------------
 */

/**
 * Class to print Permit for Special Plates 
 *
 * @version	6.8.1 		08/22/2011
 * @author	Kathy Harrell
 * @since				12/17/2010 16:12:17
 */
public class GenSpecialPlatePermit extends ReceiptTemplate
{
	private final static String BORDER_TEXT =
		"____________________________________________________________________________";
	private final static String BOTTOM_BORDER = "BOTTOM_BORDER";
	private final static String MC_BOTTOM_BORDER = "MC_BOTTOM_BORDER";
	private final static String MC_PLATE_NO = "MC_PLATE_NO";
	private final static String MC_PRMT_BOTTOM_MSG =
		"MC_PRMT_BOTTOM_MSG";
	private final static String MC_PRMT_EFF_DT = "MC_PRMT_EFF_DT";
	private final static String MC_PRMT_EXP_DT = "MC_PRMT_EXP_DT";
	private final static String MC_PRMT_EXP_TM = "MC_PRMT_EXP_TM";
	private final static String MC_PRMT_EXPIRES_TXT = "MC_PRMT_EXP_TXT";
	private final static String MC_PRMT_ISS_OFC = "MC_PRMT_ISS_OFC";
	private final static String MC_PRMT_TOP_MSG = "MC_PRMT_TOP_MSG";
	private final static String MC_PRMT_TYPE = "MC_PRMT_TYPE";
	private final static String MC_PRMT_VEHYRMAKE = "MC_PRMT_VEHYRMAKE";
	private final static String MC_PRMT_VIN = "MC_PRMT_VIN";
	private final static String MC_TOP_BORDER = "MC_TOP_BORDER";
	private final static String NO_VIN = " *** NO VIN *** ";
	private final static String PLATE_NO = "PLATE_NO";
	private final static String PRMT_BOTTOM_MSG = "PRMT_BOTTOM_MSG";
	private final static String PRMT_BOTTOM_MSG_TEXT =
		"THIS TEMPORARY INSIGNIA MUST BE REMOVED UPON RECEIPT OF YOUR SPECIAL PLATES.";
	private final static String PRMT_EFF_DT = "PRMT_EFF_DT";
	private final static String PRMT_EFF_DT_TEXT = "Effective Date ";
	private final static String PRMT_EXP_DT = "PRMT_EXP_DT";
	private final static String PRMT_EXPIRES_TEXT = "EXPIRES ";
	private final static String PRMT_EXPIRES_TXT = "PRMT_EXP_TXT";
	private final static String PRMT_ISS_OFC = "PRMT_ISS_OFC";
	private final static String PRMT_ISS_OFC_TEXT = "ISSUED BY: ";
	private final static String PRMT_TOP_MSG = "PRMT_TOP_MSG";
	private final static String PRMT_TOP_MSG_TEXT =
		"VALID FOR 60 CALENDAR DAYS ONLY.  ANY ALTERATION VOIDS THIS INSIGNIA.";
	private final static String PRMT_TYPE = "PRMT_TYPE";
	private final static String PRMT_TYPE_HDR =
		"TEXAS TEMPORARY INSIGNIA";
	private final static String PRMT_VEHYRMAKE = "PRMT_VEHYRMAKE";
	private final static String PRMT_VIN = "PRMT_VIN";
	private final static String PRMT_VIN_TEXT = "VIN: ";
	private final static String TOP_BORDER = "TOP_BORDER";
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

		// PERMIT TOP MSG 
		laPermitFormatData =
			new PermitFormatData(
				PRMT_TOP_MSG,
				2050,
				1550,
				9,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// PLATE NO  
		laPermitFormatData =
			new PermitFormatData(
				PLATE_NO,
				3840,
				2750,
				130,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// PERMIT EFFECTIVE DATE
		laPermitFormatData =
			new PermitFormatData(
				PRMT_EFF_DT,
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
				1800,
				3550,
				28,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// PERMIT EXPIRE DATE 
		laPermitFormatData =
			new PermitFormatData(
				PRMT_EXP_DT,
				3100,
				3550,
				50,
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

		// PERMIT BOTTOM MESSAGE      
		laPermitFormatData =
			new PermitFormatData(
				PRMT_BOTTOM_MSG,
				1300,
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

		// MOTORCYCLE PERMIT TOP MESSAGE 
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_TOP_MSG,
				2220,
				1850,
				8,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE PLATE NO 
		laPermitFormatData =
			new PermitFormatData(
				MC_PLATE_NO,
				3840,
				2700,
				97,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.CENTER);

		// MOTORCYCLE PERMIT EFFECTIVE DATE
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_EFF_DT,
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
				2800,
				3215,
				16,
				StickerPrintingConstant.ARIAL,
				StickerPrintingConstant.BOLD,
				StickerPrintingConstant.LEFT);

		// MOTORCYCLE PERMIT EXPIRE DATE 
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_EXP_DT,
				3550,
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

		// MOTORCYCLE BOTTOM MESSAGE      
		laPermitFormatData =
			new PermitFormatData(
				MC_PRMT_BOTTOM_MSG,
				2000,
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
	private CompleteTransactionData caCTData;

	/**
	 * GenSpecialPlatePermit.java Constructor
	 * 
	 */
	public GenSpecialPlatePermit()
	{

		caRcptProps.setPageHeight(77);
		caRcptProps.setPageWidth(132);
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

		String lsPageProps =
			Print.getPERMIT_TOP_MARGIN() + Print.getPRINT_TRAY_2();

		lsPageProps =
			lsPageProps.substring(0, 2)
				+ Print.getPRINT_LANDSCAPE()
				+ lsPageProps.substring(2);

		this.caRpt.printAttributesNoReturn(lsPageProps);

		generatePermit();
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
		SpecialPlatePermitData laPrmtData =
			caCTData.getSpclPltPrmtData();

		String lsRegPltNo = laPrmtData.getRegPltNo();

		// Eff Date 
		if (laPrmtData.getEffDate() == 0)
		{
			laPrmtData.setEffDate(new RTSDate().getYYYYMMDDDate());
		}

		RTSDate laEffDate =
			new RTSDate(RTSDate.YYYYMMDD, laPrmtData.getEffDate());

		String lsEffDate = laEffDate.getMMDDYYYY("-");

		// Exp Date 
		RTSDate laExpDate =
			new RTSDate(RTSDate.YYYYMMDD, laPrmtData.getExpDate());
		String lsExpDate = laExpDate.getMMDDYYYY("-");

		String lsVehModlYr = "" + laPrmtData.getVehModlYr();
		String lsVIN = laPrmtData.getVIN();
		if (UtilityMethods.isEmpty(lsVIN))
		{
			lsVIN = NO_VIN;
		}
		String lsVehMk = laPrmtData.getVehMk();
		int liOfcIssuanceNo = laPrmtData.getOfcIssuanceNo();
		OfficeIdsData laOfcData =
			OfficeIdsCache.getOfcId(liOfcIssuanceNo);
		String lsOfcName = new String();

		if (laOfcData != null)
		{
			lsOfcName = laOfcData.getOfcName();

			if (laOfcData.isCounty())
			{
				lsOfcName = lsOfcName + CommonConstant.COUNTY_ABBR;
			}
			else if (laOfcData.isRegion())
			{
				int liPos =
					lsOfcName.indexOf(CommonConstant.REGIONAL_OFFICE);
				lsOfcName = lsOfcName.substring(0, liPos).trim();
				lsOfcName = lsOfcName + CommonConstant.REGION_ABBR;
			}
		}

			// defect 10804 
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(laPrmtData.getRegPltCd());

			boolean lbMC = laPltTypeData != null && laPltTypeData.isSmallPlt();
			
//			boolean lbMC = 
//				laPltTypeData != null
//					&& (laPltTypeData.getRegPltCd().trim().endsWith("MC")
//					|| laPltTypeData.getRegPltCd().trim().endsWith("MCF")
//						|| ((!UtilityMethods
//							.isEmpty(laPltTypeData.getRegPltDesign()))
//							&& laPltTypeData
//								.getRegPltDesign()
//								.trim()
//								.endsWith(
//								"MC")));
			// end defect 10804 

		String lsBorder =
			getPrintData(
				lbMC ? MC_TOP_BORDER : TOP_BORDER,
				BORDER_TEXT);

		caRpt.print(lsBorder, 0, lsBorder.length());

		// Permit Type 
		String lsPrmtType = PRMT_TYPE_HDR;
		String lsPermit =
			getPrintData(lbMC ? MC_PRMT_TYPE : PRMT_TYPE, lsPrmtType);
		caRpt.print(lsPermit, 0, lsPermit.length());

		// Plate Number 
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_TOP_MSG : PRMT_TOP_MSG,
				PRMT_TOP_MSG_TEXT);
		caRpt.print(lsPermit, 0, lsPermit.length());

		lsPermit =
			getPrintData(lbMC ? MC_PLATE_NO : PLATE_NO, lsRegPltNo);
		caRpt.print(lsPermit, 0, lsPermit.length());

		// Effective Date 
		lsEffDate = PRMT_EFF_DT_TEXT + " " + lsEffDate;
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_EFF_DT : PRMT_EFF_DT,
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

		lsVehMk = lsVehModlYr + "  " + lsVehMk;

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

		// ISSUING OFFICE 
		lsOfcName = PRMT_ISS_OFC_TEXT + lsOfcName;
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_ISS_OFC : PRMT_ISS_OFC,
				lsOfcName);
		caRpt.print(lsPermit, 0, lsPermit.length());

		// PRMT MESSAGE 
		lsPermit =
			getPrintData(
				lbMC ? MC_PRMT_BOTTOM_MSG : PRMT_BOTTOM_MSG,
				PRMT_BOTTOM_MSG_TEXT);
		caRpt.print(lsPermit, 0, lsPermit.length());

		lsBorder =
			getPrintData(
				lbMC ? MC_BOTTOM_BORDER : BOTTOM_BORDER,
				BORDER_TEXT);
		caRpt.print(lsBorder, 0, lsBorder.length());
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
}
