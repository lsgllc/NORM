package com.txdot.isd.rts.client.title.business;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.title.ui.TitleValidObj;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com
	.txdot
	.isd
	.rts
	.services
	.reports
	.title
	.GenDealerTitlePreliminaryReport;
import com.txdot.isd.rts.services.reports.title.GenStickerReceiptReport;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * TitleClientBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/27/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Modified references to ReprintData 
 * 							methods to match renaming according to 
 * 							standards, e.g. getItmCd.
 * 							Modified references to Cache Static 
 * 							Methods 
 * 							Ver 5.2.0
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							modify printReports(Object)
 *							defect 6848, 6898 Ver 5.1.6
 * K Harrell	03/19/2004	Modified to include 5.1.6 changes.
 *							add DTA_FROM_DISKETTE, FILE_TO,BACK_SLASH
 *							add import statements
 * 							modify printReports(),genPreDealerReport,
 *							getDataFromDisk(),processData()
 * 							Ver 5.2.0	
 * Jeff S.		05/26/2004	getDTAPageSettings() was doing the same 
 * 							thing that getDefaultPageProps() was.  
 * 							Only need one method for ease of maintenance
 *							modify printReports(Object)
 *							defect 7078 Ver 5.2.0
 * J Rue		07/20/2004	Incorporate prior 5.1.6 DTA defects to 5.2.1
 *							defect 7350 Ver 5.2.1
 * J Rue		07/27/2004	Write to diskette 1 time after POS/RSPS 
 * 							processing has been completed.
 *							modify getDataFromDisk()
 *							defect 7240 VER 5.2.1
 * J Rue		08/13/2004	Comment call to Sticker Receipt Report
 *							Sticker/Receipt Report call is made from 
 *							FrmDealerMediaContentsDTA007.
 *							actionPerformed()
 * 							modify genPreDealerReport()
 *							defect 7429 Ver 5.2.1
 * J Rue		08/25/2004	DealerTitleData.NewRegExpYr() were converted 
 *							to integer.
 *							method getDataFromDisk()
 *							defect 7496 Ver 5.2.1
 * K Harrell	10/11/2004	Modify for ReprintData.setVIN()
 *							modify getDataFromDisk()
 *							defect      Ver 5.2.1
 * J Rue		12/01/2004	Do not write back to the diskette if disk is
 *							a NON-RSPS.
 *							modify getDataFromDisk()
 *							defect 7738 Ver 5.2.2
 * J Rue		12/02/2004	Add RegPltNo & RSPSId for RSPS_PRNT insert.
 *							modify getDataFromDisk()
 *							defect 7637 Ver 5.2.2
 * J Rue		12/10/2004	Add method that appends RSPS data to the
 *							original Dealer Title Data object
 *							add appendRSPSDataToOrigDTA(), 
 *							getDataFromDisk()
 *							defect 7738 Ver 5.2.2
 * J Rue		12/17/2004	Update comments. Change param from 
 *							DealerTitleData to Object
 *							modify appendRSPSDataToOrigDTA()
 *							defect 7738 Ver 5.2.2
 * K Harrell	01/25/2005	Correct calculation of PrntQty,ReprntQty
 *							modify getDataFromDisk()
 *							defect 7926 Ver 5.2.2
 * Ray Rowehl	02/08/2005	Change package for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							Hungarian notation to standards.
 * 							defect 7898 Ver 5.2.3 
 * J Rue		06/03/2005	Comment out unused variables
 * 							Deprecate unused local methods
 * 							deprecate getNumDocRecords(), 
 * 							getPlateTypeDesc(String), 
 * 							getRegClass(int, int)
 * 							defect 7898 Ver 5.2.3
 * J Rue		06/15/2005	Match RSPS getters/setters set in 
 * 							DealerTitleData to better define their 
 * 							meaning
 * 							modify appendRSPSDataToOrigDTA()
 * 							modify getDataFromDisk()
 * 							defect 8217 Ver 5.2.3
 * J Rue		06/21/2005	Throw exception if DLRTITLE.DAT exist but 
 *							empty. Display DTA005
 *							modify getDataFromDisk()
 *							defect 8220 Ver 5.2.3
 * J Rue		06/23/2005	Add final static variable TEMP_FILE
 * 							Add class level variable
 * 							defect 8227 Ver 5.2.3
 * J Rue		06/28/2005	Remove deprecated methods
 * 							deprecate genCCOdocument()
 * 							deprecate genCOAdocument
 * 							defect 7898 Ver 5.2.3
 * K Harrell	07/05/2005	Use cache for Dealer Data, Lienholder Data
 * 							plus add'l Java 1.4 Cleanup
 * 							modify getDataFromDisk(), getKeyBoardData(),
 * 							getLienHldrData()
 * 							defect 8283 Ver 5.2.3 
 * J Rue		07/13/2005	Add option to get file from any location.
 * 							Move appendRSPSDataToOrigDTA() from 
 * 							pending Transaction panel.
 * 							Use RSPS_DTA.DAT for diskette confirmation.
 * 							modify getDataFromDisk(), 
 * 							modify appendRSPSDataToOrigDTA()
 * 							modify verifyOrigMatchPrcsDTA()
 * 							defect 8227 Ver 5.2.3
 * J Rue		07/20/2005	Add defect 8283 back to getDataFromDisk()
 * 							modify getDataFromDisk()
 * 							defect 8227 Ver 5.2.3
 * J Rue		07/22/2005	Add DLRTITLE_ORG constant is designate
 * 							the dealer original file.
 * 							defect 8227 Ver 5.2.3
 * J Rue		08/25/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		08/29/2005	Log all exceptions for diskette processing.
 * 							modify getDataFromDisk()
 * 							defect 8304 Ver 5.2.2
 * J Rue		09/08/2005	Update comments
 * 							modify verifyOrigMatchPrcsDTA()
 * 							defect 8227 Ver 5.2.3
 * J Rue		12/16/2005	Replace "Confirmation Screen  CTL001 with 
 * 							ErrorsConstant.ERR_TITLE_CONFIRMATION_SCR
 * 							modify ensureDiskInADrive()
 * 							modify verifyOrigMatchPrcsDTA()
 * K Harrell	01/10/2006	Replaced  "System.getProperty(
 *							CommonConstant.SYSTEM_LINE_SEPARATOR)" with
 *							CommonConstant.SYSTEM_LINE_SEPARATOR
 *							modify printReports()
 *							defect 7078, 7898 Ver 5.2.3 
 * Jeff S.		06/26/2006	Used screen constant for CTL001 Title.
 * 							modify verifyOrigMatchPrcsDTA(),
 * 								ensureDiskInADrive()
 * 							defect 8756 Ver 5.2.3
 * T Pederson	09/08/2006	Added case for presumptive value to 
 * 							processData()
 *							modify processData()
 * 							defect 8926 Ver 5.2.5
 * K Harrell	02/21/2007	Changes due to refactoring ciInvProcsngCd
 * 							in TitleValidObj for analysis 
 * 							modify setPlateTypeDesc() 
 *							defect 9085 Ver Special Plates
 * K Harrell	02/27/2007	Removed reference to TitleValidObj
 * 							ciInvProcsngCd
 * 							modify setPlateTypeDesc() 
 *							defect 9085 Ver Special Plates 
 * K Harrell	04/02/2008	Check for null TtlProcsCd 
 * 							modify isCorrectTitleRejectionAllowed()
 * 							defect 6073 Ver Defect POS A
 * K Harrell	06/09/2008	Remove deprecated classes, including 
 * 							references to COA 
 * 							rename salvage() to addSalvageTrans()  
 * 							modify processData() 
 * 							defect 9642 Ver Defect POS a 	
 * J Rue		09/23/2008	Update error message INPUT_MEDIA_FAILURE_ERRMSG
 * 							modify INPUT_MEDIA_FAILURE_ERRMSG
 * 							defect 8812 Ver Defect_POS_B
 * J Rue		01/15/2009	Check RSPSId and RSPSDiskNo for consistency.
 * 							Return RTSException if disk records are not
 * 							consistent.
 * 							Add new error message CORRUPTDISK, 
 * 							MEDIA_ID_ERRMSG
 * 							add checkRSPSIdRSPSDskNoConsistency()	
 * 							modify getDataFromDisk()
 * 							defect 8912 Ver Defect_POS_D
 * J Rue		01/29/2009	ErrorsConstant.CORRUPTDISK was deleted.
 * 							modify getDataFromDisk()
 * 							defect 8912 Ver Defect_POS_D	
 * J Rue		02/02/2009	Increment counter by 1 and test for maximum 
 * 							number of iterations. Throw RTSException if 
 * 							meet. Delete STR_SPACE_EMPTY
 * 							add MAX_RSPS_NOT_MATCHD_CNT
 * 							modify verifyOrigMatchPrcsDTA()	
 * 							delete SPACE_EMPTY	  
 * 							defect 8912 Ver Defect_POS_D
 * K Harrell	03/03/2009	Removed arguments where not required.
 * 							modify getKeyBoardData(), getLienHldrData(), 
 * 							  processData()
 * 							defect 9969 Ver Defect_POS_E	
 * B Hargrove	06/03/2009  Add Flashdrive option to DTA. 
 *                   		modify processData(), getDataFromDisk()
 * 							(add parameter aiFunctionID, rename to
 * 							 getDataFromMedia()),
 * 							verifyOrigMatchPrcsDTA()
 * 							delete FILE_FROM
 * 							defect 10075 Ver Defect_POS_F
 * K Harrell	06/25/2009	Implement new DealerData, LienholderData 
 * 							defect 10112 Ver Defect_POS_F
 * J Rue		07/24/2009	Rename DealersData() to DealerData()
 * 							modify getDataFromMedia()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/18/2009	Implement UtilityMethods.addPCLandSaveReport(),  
 * 							 ReportProperties.initClientInfo(),
 * 							 DocTypeConstant
 * 							delete COA_NOREG, COA_DOCTYPE,
 *							 NON_TITLED
 * 							delete getDlrTtlDataFromHardDrive(),
 * 							 isVehAllowedToBeTitled(Object)
 * 							modify printReports(), genPreDealerReport(),
 * 							 genStickerReceiptReport(), processData(),
 * 							 isCorrectTitleRejectionAllowed(), 
 * 							 isVehAllowedToBeTitled(int), 
 * 							 checkRSPSIdRSPSDskNoConsistency(),
 * 							 getDataFromMedia() 
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	10/13/2009	Correct Report Number for Sticker Report. 
 * 							Should be 2161. Implement ReportConstant.
 * 							delete DLR_PRELIMINARY_RPT, STKR_RCPT_RPT,
 * 							 DLRPRELM, STKRRCP, RPT_NUM   
 * 							modify genPreDealerReport(), 
 * 							 genStickerReceiptReport(), processData(), 
 * 							 printReports() 
 * 							defect 10251 Ver Defect_POS_G
 * K Harrell	12/16/2009	DTA Cleanup
 * 							add updateRSPSDAtaOnOrigDTARecord(), 
 * 							 genDealerTitlePreliminaryReport(), 
 * 							 getDTADataFromMedia(), 
 * 							 getDTAKeyBoardData() 
 * 							delete appendRSPSDataToOrigDTA(), 
 * 							 genPreDealerReport(),  
 * 							 getDataFromMedia(), getKeyBoardData()
 * 							modify verifyOrigMatchPrcsDTA(), 
 * 							 checkRSPSIdDskNoConsistency()  
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	10/04/2010	modify getTitleInProcess() 
 * 							defect 10598 Ver 6.6.0 
 * K Harrell	05/29/2011	add manageFraudCd() 
 * 							modify processData() 
 * 							defect 10865 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * The class contains title client business methods.
 * 
 * @version	6.8.0			05/29/2011	
 * @author	Ashish Mahajan
 * <br>Creation Date:		09/04/2001
 */

public class TitleClientBusiness
{
	// Constants
	public static final String BACK_SLASH = "\\";
	public static final String DLRTITLE_FILE = "DLRTITLE.DAT";
	public static final String DLRTITLE_ORG = "DLRTITLE.ORG";
	public static final String DLRTITLEDAT =
		SystemProperty.getRTSAppDirectory() + DLRTITLE_FILE;

	private final static int MAX_RSPS_NOT_MATCHD_CNT = 3;

	private final static String FILE_EMPTY = "file empty";

	private final static String FILE_TO =
		SystemProperty.getTitleDirectory();

	public final static String RSPS_DTA_FILE = "RSPS_DTA.DAT";
	public final static String RSPS_DTA =
		SystemProperty.getRTSAppDirectory() + RSPS_DTA_FILE;

	/**
	 * Get Dealer Data from D:\RTS\RTSAPPL\RSPS_DTA.DAT
	 * <ol>
	 * <li> The data on the hard drive is a copy of the original dealer 
	 * <li> title records.
	 * <li> (Note: RSPS_DTA.DAT data on the hard drive may be in a 
	 * <li>        different sort order (Form31No) and the fields may 
	 * <li>        contain updated information compared to DLRTITLE.DAT)
	 * <li> Select the dealer record determine by the parm aiIndex
	 * <li> Append RSPS to the Original dealer data or 
	 * <li>	if aaObj is null then return the original dealer data.
	 * <eol>
	 * 
	 * @param aaObj Object
	 * @param asFileName String
	 * @param aiIndex int 
	 * @return DealerTitleData Object
	 */
	public static DealerTitleData updateRSPSDataOnOrigDTARecord(
		Object aaObj,
		String asFileName,
		int aiIndex)
	{
		DealerTitleData laOrigDlrTtlData = new DealerTitleData();
		DealerTitleData laUpdtDlrTtlData = new DealerTitleData();
		Vector lvOrigDTADlrTtlData = new Vector();

		try
		{
			// defect 8227
			// Use RSPS_DTA.DAT on the hard drive instead of media to 
			//	reduce the possiblity of errrors  
			// Read D:\\RTS\\RTSAPPL\\RSPS_DTA.DAT
			//
			lvOrigDTADlrTtlData =
				MediaParser.parse(
					new File(asFileName),
					(Parseable) new DealerTitleData(),
					BACK_SLASH);
			// end defect 8227

			// Get record
			laOrigDlrTtlData =
				(DealerTitleData) lvOrigDTADlrTtlData.get(aiIndex);

			// If parm object is DealerTitleData then copy current 
			//	POS/RSPS data to the original dealer title records. This
			//	will ensure the original dealer data will be preserved 
			//	and the POS/RSPS data will be updated.
			//	Case in point, writing to RSPS_DTA.DAT. Retain the
			//	original dealer data and updated POS/RSPS information.
			if (aaObj instanceof DealerTitleData)
			{
				// Update RSPS data on Original DealerTitleData record.
				laUpdtDlrTtlData = (DealerTitleData) aaObj;

				laOrigDlrTtlData.setPOSProcsIndi(
					laUpdtDlrTtlData.isPOSProcsIndi());

				laOrigDlrTtlData.setUpdtPOSRSPSPrntIndi(
					laUpdtDlrTtlData.isUpdtPOSRSPSPrntIndi());

				// TODO (KPH) 	
				// The following updates should not be required

				// POS cannot print 
				laOrigDlrTtlData.setPosPrntInvQty(
					laUpdtDlrTtlData.getPosPrntInvQty());

				// Voided is no longer valid for RSPS for DTA  
				laOrigDlrTtlData.setVoided(
					laUpdtDlrTtlData.getVoided());

				// These do not change   
				laOrigDlrTtlData.setRSPSPrntInvQty(
					laUpdtDlrTtlData.getRSPSPrntInvQty());

				laOrigDlrTtlData.setRSPSDiskSeqNo(
					laUpdtDlrTtlData.getRSPSDiskSeqNo());

				laOrigDlrTtlData.setRSPSId(
					laUpdtDlrTtlData.getRSPSId());

				laOrigDlrTtlData.setRSPSOrigPrntDate(
					laUpdtDlrTtlData.getRSPSOrigPrntDate());
			}

		}
		catch (Exception aeEx)
		{
			// defect 8304
			// Log all exceptions
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR);
			// end defect 8304
		}

		return laOrigDlrTtlData;
	}

	/**
	 * Ensure diskette is in the A:\ drive and DLRTITLE.DAT is on the 
	 * diskette before continuing.
	 * Return false after 3 tries.
	 * 
	 * @param asTransCode String - DTAORD, DTANTD
	 * @return boolean
	 */
	public static boolean ensureDiskInADrive(String asTransCode)
	{
		// Counter for number of times error message is displayed
		int liCntr = 1;
		boolean lbFileExist = true;

		while (true)
		{
			//	Ensure diskette and file exist, A:\DLRTITLE.DAT exist.
			lbFileExist = FileUtil.checkADrvForDisk(asTransCode);

			if (!lbFileExist && liCntr <= 3)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.INSERT_MEDIA_MSG,
						ScreenConstant.CTL001_FRM_TITLE);

				leRTSEx.setBeep(RTSException.BEEP);
				leRTSEx.displayError(RTSException.getRTSDesktop());
				liCntr++;
			}
			else if (liCntr > 3)
			{
				// Break and return to main menu if diskette/file 
				//	access fails.
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.INPUT_MEDIA_FAILURE_ERRMSG,
						ScreenConstant.CTL001_FRM_TITLE);
				leRTSEx.setBeep(RTSException.BEEP);
				leRTSEx.displayError(RTSException.getRTSDesktop());
				break;
			}
			else
			{
				break;
			}
		}
		return lbFileExist;
	}

	/**
	 * getDocType
	 * 
	 * @param int aiDocType
	 * @return Object
	 */
	private static Object getDocType(int aiDocType)
	{
		return DocumentTypesCache.getDocType(aiDocType);
	}

	/**
	 * getNumDocRecords
	 * 
	 * @param aiModule int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private static Object getNumDocRecords(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		return Comm.sendToServer(aiModule, aiFunctionId, aaData);
	}

	/**
	 * getOdometerBrand
	 * 
	 * @return Object
	 */
	private static Object getOdometerBrand()
	{
		Vector lvVC = new Vector(3);
		lvVC.add(OdometerBrands.ACTUAL);
		lvVC.add(OdometerBrands.NOTACT);
		lvVC.add(OdometerBrands.EXCEED);
		return lvVC;
	}

	/**
	 * getRegClass
	 * 
	 * @param String asVehCd
	 * @param int aiRegCd
	 * @param int aiEffDt
	 * @return Object
	 */
	private static Object getRegClass(
		String asVehCd,
		int aiRegCd,
		int aiEffDt)
	{
		RegistrationClassData laRegData =
			(
				RegistrationClassData) RegistrationClassCache
					.getRegisClass(
				asVehCd,
				aiRegCd,
				aiEffDt);
		return laRegData;
	}

	/**
	 * getRegClassDesc
	 * 
	 * @param int aiRegType
	 * @param int aiEffDate
	 * @return String
	 */
	private static String getRegClassDesc(int aiRegType, int aiEffDate)
	{
		CommonFeesData laFeeData =
			CommonFeesCache.getCommonFee(aiRegType, aiEffDate);
		if (laFeeData != null)
		{
			return laFeeData.getRegClassCdDesc();
		}
		else
		{
			return null;
		}
	}

	/**
	 * getTrailerTypes
	 * 
	 * @return Object
	 */
	private static Object getTrailerTypes()
	{
		Vector lvVector = new Vector(2);
		lvVector.add(TrailerTypes.SEMI);
		lvVector.add(TrailerTypes.FULL);
		return lvVector;
	}

	/**
	 * Determines if title process code for title record is marked 
	 * as rejected.
	 * 
	 * @param aiDocType int
	 * @param asTtlProcsCd String 
	 * @return boolean
	 */
	public static boolean isCorrectTitleRejectionAllowed(
		int aiDocTypeCd,
		String asTtlProcsCd)
	{
		// defect 8628 
		//		boolean lbVal = false;
		//		// defect 6073 
		//		// Validate that asTtlProcsCd is not null 
		//		if (aiDocTypeCd != NON_TITLED
		//			&& asTtlProcsCd != null
		//			&& asTtlProcsCd.equals("R"))
		//		{
		//			lbVal = true;
		//		}
		//		// end defect 6073 
		//		return lbVal;
		return aiDocTypeCd != DocTypeConstant.NON_TITLED_VEHICLE
			&& asTtlProcsCd != null
			&& asTtlProcsCd.equals(TitleConstant.TTLPROCSCD_REJECTED);
		// end defect 8628 
	}

	/**
	 * Determines if the document type is a Certificate of Authority.
	 *
	 * @param aiDocType int
	 * @return boolean
	 */
	public static boolean isVehAllowedToBeTitled(int aiDocTypeCd)
	{
		// defect 8628 
		//		boolean lbVal = false;
		//		if (aiDocTypeCd > COA_DOCTYPE || aiDocTypeCd < COA_NOREG)
		//		{
		//			lbVal = true;
		//		}
		//		return lbVal;
		// defect 8628 
		return aiDocTypeCd != DocTypeConstant.CERTIFICATE_OF_AUTHORITY
			&& aiDocTypeCd
				!= DocTypeConstant.CERTIFICATE_OF_AUTHORITY_NO_REGIS;
		// end defect 8628  
	}

	/**
	 * setPlateTypeDesc
	 * 
	 * @param aaObjData Object
	 * @param asItmCd String 
	 */
	private static void setPlateTypeDesc(
		Object aaObjData,
		String asItmCd)
	{
		TitleValidObj laVObj = (TitleValidObj) aaObjData;

		if (asItmCd != null)
		{
			ItemCodesData laItmData = ItemCodesCache.getItmCd(asItmCd);

			if (laItmData != null)
			{
				laVObj.setPlateType(laItmData.getItmCdDesc());
				// defect 9085 
				// laVObj.setInvProcsngCd(laItmData.getInvProcsngCd());
				// end defect 9085 
			}
		}
	}

	/**
	 * Determine if the first record RSPSId in 
	 * 	D:\RTS\RTSAPPL\RSPS_DTA.DAT matches the same RSPSId on the 
	 * 	diskette.
	 * Continue to check until a match is found.
	 * 
	 * (This process was moved from Pend Trans Panel)
	 * 
	 * @param aaDlrTtlData Object 
	 */
	public static void verifyOrigMatchPrcsDTA(Object aaDlrTtlData)
		throws RTSException
	{
		// defect 10290 
		// refactored 
		//    laDealerData   to laDlrTtlData
		//     laDiskDlrData to laFirstDiskDlrTtlData   

		DealerTitleData laDlrTtlData = (DealerTitleData) aaDlrTtlData;

		// defect 8912
		//	Throw Exception if fail on 3rd time.
		int liTestCnt = 1;
		// end defect 8912 

		boolean lbDone = false;
		while (!lbDone)
		{
			// Get the first original dealer record from
			//	A:\\DLRTITLE.DAT
			//	This record will be used to compare against the 
			//	processed data.

			//	 DOES NOT UPDATE RSPS DATA, just returns 1st record
			//	 from external media
			DealerTitleData laFirstDiskDlrTtlData =
				TitleClientBusiness.updateRSPSDataOnOrigDTARecord(
					null,
					SystemProperty.getExternalMedia(),
					0);

			// Compare the original and processed Dealer Title Data
			// RSPS Id && RSPS Disk Seq Number 
			// All done if the same 
			if ((laDlrTtlData
				.getRSPSId()
				.equals(laFirstDiskDlrTtlData.getRSPSId()))
				&& (laDlrTtlData.getRSPSDiskSeqNo()
					== laFirstDiskDlrTtlData.getRSPSDiskSeqNo()))
			{
				lbDone = true;
			}
			else
			{
				// It has been determined the original media is not
				// available
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.RSPSNOTMATCHDTA1
							+ laDlrTtlData.getRSPSDiskSeqNo()
							+ ErrorsConstant.RSPSNOTMATCHDTA2
							+ laDlrTtlData.getRSPSId(),
						ScreenConstant.CTL001_FRM_TITLE);

				leRTSEx.setBeep(RTSException.BEEP);
				leRTSEx.displayError(RTSException.getRTSDesktop());

				// defect 8912
				//	Increment counter by 1 and test for maximum number 
				//	of iterations. Throw RTSException if meet. 
				if (liTestCnt++ >= MAX_RSPS_NOT_MATCHD_CNT)
				{
					throw new RTSException();
				}
				// end defect 8912
			}
		}
		// end defect 10290 
	}

	/**
	 * TitleClientBusiness constructor comment.
	 */
	public TitleClientBusiness()
	{
		super();
	}

	/**
	 * Add Salvage Transaction
	 * 
	 * @param aaObj Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object addSalvageTrans(Object aaObj) throws RTSException
	{
		CommonClientBusiness laClientBusiness =
			new CommonClientBusiness();
		laClientBusiness.processData(
			GeneralConstant.COMMON,
			CommonConstant.ADD_TRANS,
			aaObj);
		return null;
	}

	/**
	 * Check RSPSId and RSPSDiskNo consistency  
	 * If a record is found to be inconsistent, an RTSException is 
	 * thrown.
	 * 
	 * @param avDlrTtlData 
	 * @throws RTSException 
	 */
	private void checkRSPSIdRSPSDskNoConsistency(Vector avDlrTtlData)
		throws RTSException
	{
		String lsPrevRSPSId = CommonConstant.STR_SPACE_EMPTY;
		String lsRSPSId = CommonConstant.STR_SPACE_EMPTY;

		int lsPrevRSPSDskNo = 0;

		int lsRSPSDskNo = 0;

		// Get record
		for (int liIndex = 0; liIndex < avDlrTtlData.size(); liIndex++)
		{
			// defect 10290 
			// Refactored  laDlrData to laDlrTtlData 
			// Get DealerTitleData record from vector
			DealerTitleData laDlrTtlData =
				(DealerTitleData) avDlrTtlData.get(liIndex);

			// Copy the RSPSId and RSPSDiskNo
			lsRSPSId = laDlrTtlData.getRSPSId();
			lsRSPSDskNo = laDlrTtlData.getRSPSDiskSeqNo();
			// end defect 10290 

			// RSPSId and RSPSDiskNo must match the previous to continue
			// Skip the first record
			if (liIndex != 0
				&& (!lsPrevRSPSId.equals(lsRSPSId)
					|| lsPrevRSPSDskNo != lsRSPSDskNo))
			{
				// defect 8628
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						ErrorsConstant.MEDIA_ID_ERRMSG
							+ MediaParser.REFER_TO_REC
							+ (liIndex + 1),
						ErrorsConstant.CONFIRMATION_SCREEN,
						true);
				throw leRTSEx;
				// end defect 8628
			}
			// Copy the RSPSId and RSPSDiskNo to 
			//	PrevRSPSId and PrevRSPSDiskNo
			lsPrevRSPSId = lsRSPSId;
			lsPrevRSPSDskNo = lsRSPSDskNo;
		}
	}

	/**
	 * genDealerTitlePreliminaryReport
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object genDealerTitlePreliminaryReport(Object aaData)
		throws RTSException
	{
		// Implement ReportConstant 
		ReportProperties laRptProps =
			new ReportProperties(ReportConstant.DLR_PRELIMINARY_RPT_ID);

		laRptProps.initClientInfo();

		GenDealerTitlePreliminaryReport laGpr =
			new GenDealerTitlePreliminaryReport(
				ReportConstant.DLR_PRELIMINARY_RPT_TITLE,
				laRptProps);

		Vector lvData =
			UtilityMethods.getNewDTADlrTtlDataVectorWithSkip(
				(Vector) aaData);

		laGpr.formatReport(lvData);

		return new ReportSearchData(
			laGpr.getFormattedReport().toString(),
			ReportConstant.DLR_PRELIMINARY_RPT_FILENAME,
			ReportConstant.DLR_PRELIMINARY_RPT_TITLE,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
	}

	/**
	 * genStickerReceiptReport
	 * 
	 * @param aaData Object
	 * @return Object
	 */
	private Object genStickerReceiptReport(Object aaData)
	{
		ReportProperties laRptProps =
			new ReportProperties(ReportConstant.DLR_STKR_RCPT_RPT_ID);
		laRptProps.initClientInfo();

		Vector lvDlrTtlData = (Vector) aaData;

		GenStickerReceiptReport laGPR =
			new GenStickerReceiptReport(
				ReportConstant.DLR_STKR_RCPT_RPT_TITLE,
				laRptProps);

		laGPR.formatReport(lvDlrTtlData);

		return new ReportSearchData(
			laGPR.getFormattedReport().toString(),
			ReportConstant.DLR_STKR_RCPT_RPT_FILENAME,
			ReportConstant.DLR_STKR_RCPT_RPT_TITLE,
			ReportConstant.RPT_1_COPY,
			ReportConstant.PORTRAIT);
	}

	/**
	 * Get data from diskette or flashdrive for DTA processing
	 * Updates the Transaction.updateReprintSticker() db
	 * Throw RTSException if data is compromised
	 * 
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector getDTADataFromMedia() throws RTSException
	{
		Vector lvUpdtVct = new Vector();

		try
		{
			// defect 10075
			// Use new variable indicating either diskette or flashdrive
			FileUtil.copyFile(
				SystemProperty.getExternalMedia(),
				FILE_TO);
			// end defect 10075

			FileUtil.copyFile(FILE_TO, RSPS_DTA);
		}
		catch (Exception aeExc)
		{
			throw new RTSException(RTSException.SYSTEM_ERROR, aeExc);
		}

		try
		{
			// defect 8227
			//	Use RSPS_DTA.DAT for processing
			Vector lvDlrTtlData =
				MediaParser.parse(
					new File(RSPS_DTA),
					(Parseable) new DealerTitleData(),
					BACK_SLASH);
			// end defect 8227	

			// defect 8220
			// Throw Exception if File Exists, no Data. 
			// Caught in VCEntryPreferencesDTA001()
			if (lvDlrTtlData.size() == 0)
			{
				RTSException leRTSEx = new RTSException();
				leRTSEx.setMessage(FILE_EMPTY);
				throw leRTSEx;
			}
			// end defect 8220 

			// Throw Exception if RSPSId, RSPSDiskNo inconsistent
			// TODO Loop Consolidation 
			checkRSPSIdRSPSDskNoConsistency(lvDlrTtlData);

			// Sort by Form31 
			UtilityMethods.sort(lvDlrTtlData);

			// Read each record
			// Process DTA record, append RSPS data to records
			for (int liIndex = 0;
				liIndex < lvDlrTtlData.size();
				liIndex++)
			{
				DealerTitleData laDlrTtlData =
					(DealerTitleData) lvDlrTtlData.get(liIndex);

				laDlrTtlData.setTransNo(liIndex);
				laDlrTtlData.setKeyBoardEntry(false);

				// defect 10290 
				// Removed validation of TransDate, VehBdyType
				// Handled in setField				
				// end defect 10290 

				// Append RSPS data format to NON-RSPS records
				// defect 7738
				//  If diskette was process at RSPS WS to conditional
				//	getRSPSId().length must be > 0 for all 
				//	RSPS records or getRSPSId().length = 0 
				//	for all NON-RSPS records
				if (laDlrTtlData.getRSPSId().length() > 0
					&& !laDlrTtlData.isUpdtPOSRSPSPrntIndi())
					//&& !laDlrTtlData.isInvalidRecord()))
				{
					// end defect 7738
					ReprintData laReprintData = new ReprintData();
					laReprintData.setOfcIssuanceNo(
						SystemProperty.getOfficeIssuanceNo());
					laReprintData.setReprntDate(
						RTSDate.getCurrentDate());
					laReprintData.setOrigin(CommonConstant.RSPS_DTA);
					laReprintData.setOriginId(
						laDlrTtlData.getDealerId());
					String lsStickerCd =
						laDlrTtlData
							.getMFVehicleData()
							.getRegData()
							.getRegStkrCd();
					laReprintData.setItmCd(lsStickerCd);
					int liYear = 0;
					liYear = laDlrTtlData.getNewRegExpYr();
					if (liYear == 0)
					{
						liYear =
							laDlrTtlData
								.getMFVehicleData()
								.getRegData()
								.getRegExpYr();
					}
					laReprintData.setItmYr(liYear);
					// defect 7637
					// Add NewPltNo or RegPltNo for DB update
					if (laDlrTtlData.getNewPltNo() != null
						&& laDlrTtlData.getNewPltNo().length() != 0)
					{
						laReprintData.setRegPltNo(
							laDlrTtlData.getNewPltNo());
					}
					else
					{
						laReprintData.setRegPltNo(
							laDlrTtlData
								.getMFVehicleData()
								.getRegData()
								.getRegPltNo());
					}
					laReprintData.setVIN(
						laDlrTtlData
							.getMFVehicleData()
							.getVehicleData()
							.getVin());
					// Add Print/Reprint Quantity for DB update
					int liRSPSPrintNum =
						laDlrTtlData.getRSPSPrntInvQty();

					// defect 7926
					//	 Set Prnt to 1. set ReprntQty to TotalQty - 1
					if (liRSPSPrintNum > 0)
					{
						laReprintData.setPrntQty(1);
						laReprintData.setReprntQty(liRSPSPrintNum - 1);
					}
					else
					{
						laReprintData.setPrntQty(0);
						laReprintData.setReprntQty(0);
					}
					// end defect 7926 
					// end defect 7637
					laReprintData.setVoided(laDlrTtlData.getVoided());
					laReprintData.setScannerId(
						CommonConstant.STR_SPACE_EMPTY
							+ laDlrTtlData.getRSPSId());
					laReprintData.setDiskNum(
						laDlrTtlData.getRSPSDiskSeqNo());
					laReprintData.setSubstaId(
						SystemProperty.getSubStationId());
					laReprintData.setWsId(
						SystemProperty.getWorkStationId());

					laDlrTtlData.setUpdtPOSRSPSPrntIndi(true);

					//	Update RTS_REPRNT_STKR
					//	(Note: Cache will only update the RSPS_PRNT and 
					//			REPRNT_STKR once per day.)
					if (laReprintData.isValidForDB2Insert()
						&& (liRSPSPrintNum > 0
							|| laDlrTtlData.getVoided() > 0))
					{
						Transaction.updateReprintSticker(laReprintData);
					}
				}

				// For RSPS transactions, build a vector, first by
				// converting the delimited data records in the dealer  
				// file to strings. 
				if (laDlrTtlData.getRSPSId() != null
					&& laDlrTtlData.getRSPSId().length() > 0)
				{
					lvUpdtVct.add(
						liIndex,
						MediaParser.convertParseableToString(
							(Parseable) laDlrTtlData,
							BACK_SLASH));
				}
			}
			// TODO The data is not necessarily in the same order. 
			//      See UtilityMethods.sort() above. 
			// defect 7240 
			// Write back to the RSPS_DTA.DAT so that this file has 
			//	the same data in the same order as the application
			// defect 7738
			//	Do not write back to RSPS_DTA.DAT if trans are NON-RSPS
			//	(Note: In updateRecord(), if lvUpdtVct is empty 
			//		- no update.)
			if (lvDlrTtlData.size() > 0
				&& (((DealerTitleData) lvDlrTtlData.get(0)).getRSPSId()
					!= null
					&& ((DealerTitleData) lvDlrTtlData.get(0))
						.getRSPSId()
						.length()
						> 0))
			{
				MediaParser.updateRecord(new File(RSPS_DTA), lvUpdtVct);
				// defect 8227
				//	Copy RSPS_DTA.DAT to A:\\DLRTITLE.DAT
				// defect 10075
				// Use new variable indicating either diskette or flashdrive
				//FileUtil.copyFile(RSPS_DTA, FROM_FIILE);
				FileUtil.copyFile(
					RSPS_DTA,
					SystemProperty.getExternalMedia());
				// end defect 10075				
				// end defect 8227
			}
			// end defect 7738
			// end defect 7240

			// Get and set Dealer Id information
			DealerTitleData laDlrTtlData =
				(DealerTitleData) lvDlrTtlData.get(0);

			DealerData laDealerData =
				DealersCache.getDlr(
					SystemProperty.getOfficeIssuanceNo(),
					SystemProperty.getSubStationId(),
					laDlrTtlData.getDealerId());

			Transaction.setDTADealerData(
				(DealerData) UtilityMethods.copy(laDealerData));

			Transaction.setDTADealerId(laDlrTtlData.getDealerId());

			return lvDlrTtlData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		catch (IOException aeIOEx)
		{
			throw new RTSException(RTSException.JAVA_ERROR, aeIOEx);
		}
	}

	/**
	 * getDTAKeyBoardData
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getDTAKeyBoardData(Object aaData)
		throws RTSException
	{
		DealerTitleData laDlrTtlData = (DealerTitleData) aaData;

		DealerData laDealerData =
			DealersCache.getDlr(
				SystemProperty.getOfficeIssuanceNo(),
				SystemProperty.getSubStationId(),
				laDlrTtlData.getDealerId());

		Transaction.setDTADealerData(
			(DealerData) UtilityMethods.copy(laDealerData));

		Transaction.setDTADealerId(laDlrTtlData.getDealerId());

		laDlrTtlData.setKeyBoardEntry(true);

		Vector lvVector = new Vector();
		lvVector.add(laDlrTtlData);
		return lvVector;
	}

	/**
	 * Makes call to LienholderCache which will get from DB if 
	 * available.
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getLienHldrData(Object aaData) throws RTSException
	{
		VehicleInquiryData laVehInqData = (VehicleInquiryData) aaData;

		TitleValidObj laValidObj =
			(TitleValidObj) laVehInqData.getValidationObject();

		if (laValidObj != null)
		{
			LienholderData laLienData =
				(LienholderData) laValidObj.getLienData();

			if (laLienData != null)
			{
				// defect 8283
				laLienData =
					LienholdersCache.getLienhldr(
						SystemProperty.getOfficeIssuanceNo(),
						SystemProperty.getSubStationId(),
						laLienData.getId());

				laValidObj.setLienData(laLienData);
				// end defect 8283 
			}
		}
		return laVehInqData;
	}

	/**
	 * getOwnrEvidCds
	 * 
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getOwnrEvidCds(Object aaData) throws RTSException
	{
		VehicleInquiryData laVehData = (VehicleInquiryData) aaData;
		TitleValidObj laValidObj =
			(TitleValidObj) laVehData.getValidationObject();

		// Get Ownership Evidence Codes sorted depending on whether 
		//	additional evidence has been surrendered
		int liEvidSortNo =
			laVehData
				.getMfVehicleData()
				.getTitleData()
				.getAddlLienRecrdIndi();

		Object laObjData = null;
		try
		{
			laObjData =
				OwnershipEvidenceCodesCache.getOwnrEvidCdsByEvidSurrCd(
					liEvidSortNo);
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		finally
		{
			laValidObj.setOwnrEvidCds(laObjData);
		}
		return laVehData;
	}

	/**
	 * getTitleInProcess
	 * 
	 * @param aiModuleName int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object getTitleInProcess(
		int aiModuleName,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		GeneralSearchData laGSD = new GeneralSearchData();

		// defect 10598 
		laGSD.setKey1(CommonConstant.DOC_NO);
		laGSD.setKey2((String) aaData);
		// end defect 10598 

		return Comm.sendToServer(aiModuleName, aiFunctionId, laGSD);
	}

	//	/**
	//	 * isVehAllowedToBeTitled
	//	 * 
	//	 * @param aaData
	//	 * @return Boolean
	//	 */
	//	private Boolean isVehAllowedToBeTitled(Object aaData)
	//	{
	//		VehicleInquiryData laVehData = (VehicleInquiryData) aaData;
	//		int liDocTypeCd =
	//			laVehData.getMfVehicleData().getTitleData().getDocTypeCd();
	//		boolean lbVal = false;
	//		if (liDocTypeCd > COA_DOCTYPE || liDocTypeCd < COA_NOREG)
	//		{
	//			lbVal = true;
	//		}
	//		return new Boolean(lbVal);
	//	}

	/**
	 * isVehicleExpired
	 * 
	 * @param aiMth int
	 * @param aiYr int
	 * @param aiTransactionDate int
	 * @return boolean
	 */
	private boolean isVehicleExpired(
		int aiMth,
		int aiYr,
		int aiTransactionDate)
	{
		return CommonValidations.isRegistrationExpired(
			aiMth,
			aiYr,
			aiTransactionDate);
	}

	/**
	 * printReports
	 *
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	private Object printReports(Object aaData) throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;

		// defect 8628
		// defect 7078
		String lsPageProps = Print.getDefaultPageProps();
		String lsRpt =
			lsPageProps
				+ CommonConstant.SYSTEM_LINE_SEPARATOR
				+ laRptSearchData.getKey1();
		// end defect 7078

		String lsFileName =
			UtilityMethods.saveReport(
				lsRpt,
				laRptSearchData.getKey2(),
				laRptSearchData.getIntKey1());

		if ((lsFileName != null)
			&& !(lsFileName.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			Print laPrint = new Print();

			// defect 10251
			laPrint.sendToPrinter(
				lsFileName,
				ReportConstant.DLR_PRELIMINARY_RPT_FILENAME);
			// end defect 10251  
		}
		// end defect 8628
		return laRptSearchData;
	}

	/**
	 * processData
	 *
	 * @param aiModule int
	 * @param aiFunctionId int
	 * @param aaData Object 
	 * @return Object
	 * @throws RTSException 
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		//Pass the call to the communication layer
		switch (aiFunctionId)
		{
			// defect 8628
			// No used 
			//case TitleConstant.GET_DLR_TTL_DATA_FROM_HARDDRIVE :
			//	{
			//		return getDlrTtlDataFromHardDrive();
			//	}
			//case TitleConstant.IS_VEH_ALLOWED_TO_BE_TITLED :
			//	{
			//		return isVehAllowedToBeTitled(aaData);
			//	}

			// defect 10290 
			case TitleConstant.GENERATE_STICKER_RECEIPT_REPORT :
				{
					printReports(genStickerReceiptReport(aaData));
					return null;
				}
				// end defect 10290
			case TitleConstant.VALIDATION002 :
				{
					return returnDataWithDesc(aaData);
				}
			case TitleConstant.LIENHOLDER_DATA :
				{
					return getLienHldrData(aaData);
				}
			case TitleConstant.GET_OWNR_EVID_CDS :
				{
					return getOwnrEvidCds(aaData);
				}
				// defect 10075
			case TitleConstant.DATA_FROM_DISK :
				{
					// return getDataFromDisk();
					// defect 10290 
					return getDTADataFromMedia();
					// end defect 10290 
				}
				// end defect 10075
			case TitleConstant.GET_NUM_DOC_RECORD :
				{
					return getNumDocRecords(
						aiModule,
						aiFunctionId,
						aaData);
				}
			case TitleConstant.GENERATE_PRELIMINARY_DEALER_REPORT :
				{
					// defect 10290 
					printReports(
						genDealerTitlePreliminaryReport(aaData));
					// end defect 10290 
					return aaData;
				}
			case TitleConstant.DELETE_TITLE_IN_PROCESS :
				{
					return getTitleInProcess(
						aiModule,
						aiFunctionId,
						aaData);
				}
			case TitleConstant.PROCS_KEYBRD :
				{
					// defect 10290 
					// return getKeyBoardData(aaData);
					return getDTAKeyBoardData(aaData);
					// end defect 10290 
				}
			case TitleConstant.SALVAGE :
				{
					return addSalvageTrans(aaData);
				}
				// defect 10865
			case TitleConstant.FRAUDCD_MGMT :
				{
					return manageFraudCd(aaData);
				}
				// end defect 10865
				// defect 8926
			case TitleConstant.GET_PRIVATE_PARTY_VALUE :
				{
					return Comm.sendToServer(
						aiModule,
						aiFunctionId,
						aaData);
				}
				// end defect 8926
			default :
				return null;
		}
	}

	/**
	 * returnDataWithDesc
	 *
	 * @param aaData Object
	 * @return Object 
	 */
	private Object returnDataWithDesc(Object aaData)
	{
		VehicleInquiryData laVehData = (VehicleInquiryData) aaData;
		com.txdot.isd.rts.client.title.ui.TitleValidObj laVObj = null;
		if (laVehData.getValidationObject() == null)
		{
			laVObj =
				new com.txdot.isd.rts.client.title.ui.TitleValidObj();
		}
		else
		{
			laVObj =
				(com
					.txdot
					.isd
					.rts
					.client
					.title
					.ui
					.TitleValidObj) laVehData
					.getValidationObject();
		}
		//Get DocType Description
		int liDocType =
			laVehData.getMfVehicleData().getTitleData().getDocTypeCd();
		DocumentTypesData laDocTypeData =
			(DocumentTypesData) getDocType(liDocType);
		if (laDocTypeData != null)
		{
			laVObj.setDocTypeCdDesc(laDocTypeData.getDocTypeCdDesc());
		}
		//Get RegClass Description
		int liRegCode =
			laVehData.getMfVehicleData().getRegData().getRegClassCd();
		int liDate = RTSDate.getCurrentDate().getYYYYMMDDDate();
		String lsRegClassDesc =
			(String) getRegClassDesc(liRegCode, liDate);
		laVObj.setRegClassDesc(lsRegClassDesc);
		//Set PlateType
		String lsItemCode =
			laVehData.getMfVehicleData().getRegData().getRegPltCd();
		if (lsItemCode == null)
		{
			lsItemCode = CommonConstant.STR_SPACE_EMPTY;
		}
		setPlateTypeDesc(laVObj, lsItemCode);
		//Set vehicle registration data
		String lsVehClsCd =
			laVehData
				.getMfVehicleData()
				.getVehicleData()
				.getVehClassCd();
		RegistrationClassData laRegData =
			(RegistrationClassData) getRegClass(lsVehClsCd,
				liRegCode,
				liDate);
		laVObj.setRegData(laRegData);
		//Set trailer types
		Vector lvTrlTyp =
			(Vector) com
				.txdot
				.isd
				.rts
				.client
				.title
				.business
				.TitleClientBusiness
				.getTrailerTypes();
		laVObj.setTrlTyp(lvTrlTyp);
		//Set odometer brand
		Vector lvOdBrn =
			(Vector) com
				.txdot
				.isd
				.rts
				.client
				.title
				.business
				.TitleClientBusiness
				.getOdometerBrand();
		laVObj.setOdBrn(lvOdBrn);
		//Set reg expired flag
		int liMnth =
			laVehData.getMfVehicleData().getRegData().getRegExpMo();
		int liYr =
			laVehData.getMfVehicleData().getRegData().getRegExpYr();
		laVObj.setRegExpired(
			isVehicleExpired(liMnth, liYr, laVehData.getRTSEffDt()));
		laVehData.setValidationObject(laVObj);
		return laVehData;
	}

	/**
	 * Manage Fraud Cd 
	 * 
	 * @param aaData
	 * @return Object 
	 * @throws RTSException 
	 */
	private Object manageFraudCd(Object aaData) throws RTSException
	{
		Vector lvFraudVector = (Vector) aaData;
		RTSDate laRTSDateTrans = new RTSDate();
		String lsHour = String.valueOf(laRTSDateTrans.getHour());
		String lsMinute = String.valueOf(laRTSDateTrans.getMinute());
		String lsSecond = String.valueOf(laRTSDateTrans.getSecond());

		int liTransTime =
			Integer.parseInt(
				UtilityMethods.addPadding(
					new String[] { lsHour, lsMinute, lsSecond },
					new int[] { 2, 2, 2 },
					CommonConstant.STR_ZERO));

		for (int i = 0; i < lvFraudVector.size(); i++)
		{
			FraudLogData laFraudLogData =
				(FraudLogData) lvFraudVector.elementAt(i);
			laFraudLogData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laFraudLogData.setSubstaId(
				SystemProperty.getSubStationId());
			laFraudLogData.setTransWsId(
				SystemProperty.getWorkStationId());
			laFraudLogData.setTransEmpId(
				SystemProperty.getCurrentEmpId());
			laFraudLogData.setTransAMDate(laRTSDateTrans.getAMDate());
			laFraudLogData.setTransTime(liTransTime);

			TransactionCacheData laTransactionCacheData =
				new TransactionCacheData();
			laTransactionCacheData.setObj(laFraudLogData);
			laTransactionCacheData.setProcName(
				TransactionCacheData.INSERT);

			Vector lvTrans = new Vector();
			lvTrans.addElement(laTransactionCacheData);

			try
			{
				Object laResult = Transaction.postTrans(lvTrans);

				if (laResult != null
					&& laResult instanceof Boolean
					&& !((Boolean) laResult).booleanValue())
				{
					Transaction.writeToCache(lvTrans);
				}
			}
			catch (RTSException aeRTSEx)
			{
				throw aeRTSEx;
			}
		}
		return null;
	}
}
