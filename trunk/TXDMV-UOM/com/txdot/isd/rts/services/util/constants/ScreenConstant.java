package com.txdot.isd.rts.services.util.constants;

/*
 * ScreenConstant.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Ray Rowehl	05/29/2003	Add logoff and reboot for defect 6445
 * Ray Rowehl	12/01/2003	remove reboot for 6445.
 * Ray Rowehl	02/19/2004	reflow for updated java standards
 *							defect 6445 Ver 5.1.6
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add new class constants 
 * 							- REG050, REG053, RPR004, CUS003, CUS004
 * 							Ver 5.2.0
 * K Harrell	03/28/2004	Modify constant for SEC008
 *							defect 6973 Ver 5.2.0
 * Min Wang     06/28/22004 Add new class constant for RSPS
 *							defect 7135 Ver 5.2.1
 * J Rue		03/23/2005	Change reference class DTA008b to DTA009
 * 							and DTA008a to DTA008
 * 							modify processData()
 * 							defect 6963 Ver 5.2.3 
 * J Rue		03/25/2003	Ensure each line does not exceed 762 chars.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/25/2005	Change Key007 to KEY007
 * 							defect 6967 Ver 5.2.3
 * J Rue		03/30/2005	Apply corrections from walkthrough
 * 							defect 6963 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Rename INV020b to INV019.
 * 							defect 6964 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Rename INV015A to INV028
 * 							delete INV015b
 * 							defect 6965 Ver 5.2.3
 * Ray Rowehl	04/12/2005	Remove INV008
 * 							delete INV008
 * 							defect 7954 Ver 5.2.3
 * Ray Rowehl	04/15/2005	Rename the INV020 constant from INV020a
 * 							defect 6964 Ver 5.2.3
 * J Rue		04/26/2005	VCRejectReleaseMethodTTL022 and
 * 							VCCCOIssueDateTTL044 has been deprecated
 * 							defect 7959,7960 Ver 5.2.3
 * K Harrell	05/02/2005	Rename INV014b to INV0013 
 * 							defect 6966 Ver 5.2.3 
 * Jeff S.		06/17/2005	Renamed County processing frames to have
 * 							the frame number in the class name.
 * 							modify CP001, CP002, CP003, CP004, CP005
 * 								CP006, CP007, CP008, CP009
 * 							defect 7889 Ver 5.2.3
 * Ray Rowehl	07/09/2005	Add new constant to be used with frames.
 * 							add MSG_MAIN_ERR_CATCH
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	07/11/2005	Add a new constant for CTL001 Title.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	07/12/2005	Deleted constants for RTS I Help Screens
 * 							never implemented in RTS II 	
 * 							deleted ERR001,HLP001
 * 							defect 6971 Ver 5.2.3 
 * Ray Rowehl	07/16/2005	Add new constants for screen Uncaught 
 * 							Execptions and desktop size.
 * 							add DESKTOP_DEV_HEIGHT, DESKTOP_DEV_WIDTH,
 * 								MSG_FRAME_EXCEPTION, 
 * 								MSG_UNCAUGHT_EXECPTION
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move Inventory Frame constants over to here.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/15/2005	Reorder the frames by frame number.
 * 							defect 7890 Ver 5.2.3
 * Jeff S.		08/16/2005	Renamed CP### Constants to match the frame
 * 							number. Removed Unused constants.
 * 							remove CP001, CP002, CP003, CP004, CP005
 * 								CP006, CP007, CP008, CP009
 * 							add REG101, REG102, REG103, REG104, REG105, 
 * 								REG106
 * 							defect 7889 Ver 5.2.3
 * J Rue		08/16/2005	Move Title Frame constants over to here
 * 							defect 7898 Ver 5.2.3
 * J Rue		08/23/2005	Move Title Frame constants over to here
 * 							defect 7898 Ver 5.2.3
 * K Harrell	12/14/2005	Capitalize "Tax" for in Sales Tax TTL012
 * 							Removed reference to QuickCounter
 * 							defect 7898 Ver 5.2.3
 * K Harrell	01/12/2006	Renamed DTA009_FRANE_NAME to 
 * 							DTA009_FRAME_NAME
 * 							defect 7898 Ver 5.2.3    
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							Changed Constant to use 4 spaces.
 * 							modify CTL001_FRM_TITLE
 * 							defect 8756 Ver 5.2.3
 * T Pederson	09/08/2006	Added TTL045 constants 
 * 							defect 8926 Ver 5.2.5
 * Jeff S.		10/10/2006	Added Exempt Audit Report. Task 43.
 * 							add RPR005
 * 							defect 8900 Ver Exempts
 * Jeff S.		04/02/2007	Add Constants for MES001.
 * 							add MES001_FRAME_TITLE, MES001,
 * 								MES002_FRAME_TITLE, MES002 
 * 							defect 7768 Ver Broadcast Message
 * J Rue		02/08/2007	Add Special Plates SPL003
 * 							add SPL003
 * 							defect 9086 Ver Special Plates
 * K Harrell	02/12/2007  New Special Plates Screens	
 * 							Added SPL001,SPL002,SPL004,INQ005 
 * 							defect 9085 Ver Special Plates
 * J Rue		02/14/2007	Update screen name for SPL004
 * 							defect 9085 Ver Special Plates
 * B Hargrove	02/23/2007	New Special Plates Screens	
 * 							Add REG011
 * 							defect 9126 Ver Special Plates
 * J Rue		02/23/2007	Change SPL003 to KEY002
 * 							defect 9126 Ver Special Plates
 * M Reyes		02/26/2007	New Security Screen
 * 							Add SEC018
 * 							defect 9124 Ver Special Plates
 * K Harrell	02/27/2007	Renamed SPL004 to SPL003
 * 							defect 9085 Ver Special Plates
 * Min Wang		03/13/2007	add sceen constant for VI rejection Report.
 * 							add INV031, INV031_FRAME_NAME, 
 * 							INV031_FRAME_TITLE	
 * 							defect 9117 Ver Special Plates
 * J Rue		04/13/2007	Add SPL004
 * 							defect 9086 Ver Special Plates	
 * Min Wang		05/14/2007	change VI Rejection Report to
 * 							PLP Request Rejection Report.
 * 							modify INV031_FRAME_TITLE
 * 							defect 9117 Ver Special Plates
 * K Harrell	05/19/2007	VC Rename for INQ005
 * 							modify INQ005 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Rename SPL005 to SPL004 
 * 							defect 9085 Ver Special Plates   		  
 * B Hargrove	04/01/2008	Change all occurrences of 'progress' to   
 * 							'process' (ie: TTL042_FRAME_TITLE = 
 * 							"Delete Title In Progress      TTL042").
 * 							modify constant TTL042_FRAME_TITLE 
 * 							defect 8786 Ver Defect_POS_A
 * K Harrell	09/11/2008	Remove constants associated w/ COA Event
 * 							 (which was incorporated into Salvage)
 * 							delete TTL015, TTL015_FRAME_NAME,
 * 							 TTL015_FRAME_TITLE 
 * 							defect 9810 Ver Defect_POS_B 
 * K Harrell	10/21/2008	Add constants for Disabled Placard 
 * 							MRG020, MRG021, MRG022, MRG023, MRG024, 
 * 							MRG025, MRG026, MRG027
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	02/26/2009	Add constants for Electronic Title Report
 * 							RPR006, RPR007  
 * 							defect 9972 Ver Defect_POS_E
 * B Hargrove	05/26/2009  Add Flashdrive option to DTA. Remove
 * 							references to 'diskette'.  
 *                   		modify DTA007, DTA007_FRAME_NAME, 
 * 							DTA007_FRAME_TITLE
 * 							defect 10075 Ver Defect_POS_F
 * K Harrell	06/02/2009	Constants for Sales Tax Allocation Report
 * 							add KEY003_FRM_TITLE
 * 							defect 10014 Ver Defect_POS_F  
 * K Harrell	06/15/2009	add RPR008
 * 							defect 10086 Ver Defect 10086
 * K Harrell	08/04/2009	modify TTL018, TTL018_FRAME_NAME, TTL019,
 * 							TTL019_FRAME_NAME 
 * 							delete INQ008
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	05/24/2010	add MRG002, MRG007
 * 							delete MRG001, MRG008  
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	09/17/2010	add INQ002 
 * 							defect 10598 Ver 6.6.0 
 * K Harrell	12/26/2010	add SPL005
 * 							defect 10700 Ver 6.7.0 
 * K Harrell	01/14/2011	add OPT008, OPT008_FRAME_TITLE,
 * 							 OPT008_FRAME_NAME 
 * 							defect 10701 Ver 6.7.0 
 * K Harrell	06/03/2011	add RPR009 
 * 							defect 10900 Ver 6.8.0 
 * K Harrell	06/18/2011	add INQ008
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	01/13/2012	add TTL010
 * 							defect 10827 Ver 6.10.0 
 * K Harrell	02/05/2012	add MRG028, MRG029
 * 							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class holds all the screen controller names for RTS.
 * 
 * @version 6.10.0 				02/05/2012
 * @author	Michael Abernethy 
 * <br>Creation Date:			08/23/2001 12:59:51 
 */
public class ScreenConstant
{
	/**
	 * CTL001 Frame Title
	 */
	public final static String CTL001_FRM_TITLE =
		"Confirm Action    CTL001";

	public final static String LOGOFF = "LOGOFF";

	// These screens are sorted alphabetically.
	// review package names to see what module 
	// they belong to.
	public final static String ACC001 =
		"com.txdot.isd.rts.client.accounting.ui."
			+ "VCAdditionalCollectionsACC001";
	public final static String ACC002 =
		"com.txdot.isd.rts.client.accounting.ui."
			+ "VCRegionalCollectionACC002";
	public final static String ACC004 =
		"com.txdot.isd.rts.client.accounting.ui.VCHotCheckACC004";
	public final static String ACC005 =
		"com.txdot.isd.rts.client.accounting.ui."
			+ "VCItemSeizedACC005";
	public final static String ACC006 =
		"com.txdot.isd.rts.client.accounting.ui.VCRefundACC006";
	public final static String ACC017 =
		"com.txdot.isd.rts.client.accounting.ui."
			+ "VCFundsDueSummaryACC017";
	public final static String ACC018 =
		"com.txdot.isd.rts.client.accounting.ui."
			+ "VCFundsRemittanceACC018";
	public final static String ACC019 =
		"com.txdot.isd.rts.client.accounting.ui."
			+ "VCEnterDetailRemittanceACC019";
	public final static String ACC020 =
		"com.txdot.isd.rts.client.accounting.ui."
			+ "VCFundsPaymentACC020";
	public final static String ACC022 =
		"com.txdot.isd.rts.client.accounting.ui.VCPaymentDetailACC022";
	public final static String ACC023 =
		"com.txdot.isd.rts.client.accounting.ui.VCPaymentSummaryACC023";
	public final static String ACC024 =
		"com.txdot.isd.rts.client.accounting.ui."
			+ "VCFundsDetailsACC024";
	public final static String CTL002 =
		"com.txdot.isd.rts.client.common.ui.VCCountyConfirmCTL002";
	public final static String CTL003 =
		"com.txdot.isd.rts.client.common.ui.VCVTRAuthorizationCTL003";
	public final static String CTL004 =
		"com.txdot.isd.rts.client.common.ui.VCSupervisorOverrideCTL004";
	public final static String CTL005 =
		"com.txdot.isd.rts.client.common.ui.VCRecordFoundCTL005";
	public final static String CTL006 =
		"com.txdot.isd.rts.client.common.ui.VCDocumentNoConfirmCTL006";
	public final static String CTL007 =
		"com.txdot.isd.rts.client.common.ui.VCWorkstationLockedCTL007";
	public final static String CTL009 =
		"com.txdot.isd.rts.client.misc.ui.VCPrintDestinationCTL009";
	public final static String CTL010 =
		"com.txdot.isd.rts.client.common.ui."
			+ "VCVTRAuthorizationSSNCTL010";
	public final static String CUS001 =
		"com.txdot.isd.rts.client.misc.ui.VCSelectTransactionCUS001";
	public final static String CUS002 =
		"com.txdot.isd.rts.client.common.ui.VCSetAsideCUS002";
	public final static String CUS003 =
		"com.txdot.isd.rts.client.misc.ui.VCSelectReceiptCUS003";
	public final static String CUS004 =
		"com.txdot.isd.rts.client.misc.ui."
			+ "VCPrintSupervisorOverrideCUS004";
	// DTA - Copy Instructions
	public final static String DTA001 =
		"com.txdot.isd.rts.client.title.ui.VCEntryPreferencesDTA001";
	public final static String DTA001_FRAME_NAME =
		"FrmEntryPreferencesDTA001";
	public final static String DTA001_FRAME_TITLE =
		"Entry Preferences     DTA001";
	// DTA - External Media Copy Failure
	public final static String DTA005 =
		"com.txdot.isd.rts.client.title.ui.VCCopyFailureDTA005";
	public final static String DTA005_FRAME_NAME =
		"FrmCopyFailureDTA005";
	public final static String DTA005_FRAME_TITLE =
		"Copy Failure    DTA005";
	// DTA - Prliminary
	// defect 10075 
	public final static String DTA007 =
		"com.txdot.isd.rts.client.title.ui.VCDealerMediaContentsDTA007";
	public final static String DTA007_FRAME_NAME =
		"FrmDealerMediaContentsDTA007";
	public final static String DTA007_FRAME_TITLE =
		"Dealer External Media Contents     DTA007";
	// end defect 10075

	// DTA - Dealer Supplied New Inventory
	//	defect 6963
	public final static String DTA008 =
		"com.txdot.isd.rts.client.title.ui."
			+ "VCDealerTitleTransactionDTA008";
	public final static String DTA008_FRAME_NAME =
		"FrmDealerTitleTransactionDTA008";
	public final static String DTA008_FRAME_TITLE =
		"Dealer Title Transaction   DTA008";
	// end defect 6963

	// DTA - Manual Entry DlrId, DlrSeqNo
	// defect 6963
	public final static String DTA009 =
		"com.txdot.isd.rts.client.title.ui.VCManualEntryDTA009";
	public final static String DTA009_FRAME_NAME =
		"FrmManualEntryDAT009";
	public final static String DTA009_FRAME_TITLE =
		"Manual Entry   DTA009";
	// end defect 6963
	public final static String FUN001 =
		"com.txdot.isd.rts.client.funds.ui.VCCashDrawerSelectionFUN001";
	public final static String FUN002 =
		"com.txdot.isd.rts.client.funds.ui."
			+ "VCCloseOutConfirmationFUN002";
	public final static String FUN003 =
		"com.txdot.isd.rts.client.funds.ui.VCDateRangeEntriesFUN003";
	public final static String FUN006 =
		"com.txdot.isd.rts.client.funds.ui."
			+ "VCFundsReportSelectionFUN006";
	public final static String FUN007 =
		"com.txdot.isd.rts.client.funds.ui.VCReportSelectionFUN007";
	public final static String FUN008 =
		"com.txdot.isd.rts.client.funds.ui."
			+ "VCResetCloseOutIndicatorFUN008";
	public final static String FUN010 =
		"com.txdot.isd.rts.client.funds.ui.VCSubstationSummaryFUN010";
	public final static String FUN011 =
		"com.txdot.isd.rts.client.funds.ui.VCEmployeeSelectionFUN011";
	public final static String FUN012 =
		"com.txdot.isd.rts.client.funds.ui.VCReportDateSelectionFUN012";
	public final static String FUN013 =
		"com.txdot.isd.rts.client.funds.ui."
			+ "VCReportGenerationStatusFUN013";
	public final static String FUN014 =
		"com.txdot.isd.rts.client.funds.ui.VCCloseOutWarningFUN014";
	public final static String FUN015 =
		"com.txdot.isd.rts.client.funds.ui.VCCloseOutStatusFUN015";
	public final static String FUN016 =
		"com.txdot.isd.rts.client.funds.ui.VCUpdateVerificationFUN016";
	public final static String GENRPT =
		"com.txdot.isd.rts.client.common.ui.VCGeneralReportInput";

	// INQUIRY 
	// defect 10598 
	public final static String INQ002 =
		"com.txdot.isd.rts.client.common.ui.VCInProcessTransactionsINQ002";
	// end defect 10598 

	public final static String INQ003 =
		"com.txdot.isd.rts.client.inquiry.ui."
			+ "VCVehicleInqAddlInfoINQ003";
	public final static String INQ004 =
		"com.txdot.isd.rts.client.common.ui.VCMultipleRecordsINQ004";

	// defect 9085 
	public final static String INQ005 =
		"com.txdot.isd.rts.client.inquiry.ui."
			+ "VCSpecialPlateInquiryInfoINQ005";
	// end defect 9085 

	public final static String INQ006 =
		"com.txdot.isd.rts.client.inquiry.ui."
			+ "VCInquiryOwnerAddressessINQ006";
	public final static String INQ007 =
		"com.txdot.isd.rts.client.accounting.ui.VCCustomerNameINQ007";

	// defect 10844 
	public final static String INQ008 =
		"com.txdot.isd.rts.client.common.ui.VCPriorModifyPermitTransactionsINQ008";
	// end defect 10844

	// INVENTORY 
	public final static String INV001 =
		"com.txdot.isd.rts.client.common.ui."
			+ "VCInventoryItemNumberInputINV001";
	public final static String INV002 =
		"com.txdot.isd.rts.client.inventory.ui.VCModifyInvoiceINV002";
	public static final String INV002_FRAME_NAME =
		"FrmModifyInvoiceINV002";
	public static final String INV002_FRAME_TITLE =
		"Inventory - Modify Invoice     INV002";
	//	defect 6966
	// INV014 renamed to INV003  
	public final static String INV003 =
		"com.txdot.isd.rts.client.common.ui.VCItemNumberNotFoundINV003";
	// end defect 6966 
	public final static String INV004 =
		"com.txdot.isd.rts.client.inventory.ui.VCReceiveInvoiceINV004";
	public static final String INV004_FRAME_NAME =
		"FrmReceiveInvoiceINV004";
	public static final String INV004_FRAME_TITLE =
		"Inventory - Receive Invoice     INV004";
	public final static String INV006 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryDeleteINV006";

	public static final String INV006_FRAME_NAME =
		"FrmInventoryDeleteINV006";
	public static final String INV006_FRAME_TITLE =
		"Inventory - Inventory Delete     INV006";
	public final static String INV005 =
		"com.txdot.isd.rts.client.common.ui."
			+ "VCInventoryOverrideInventoryItemINV005";
	public final static String INV007 =
		"com.txdot.isd.rts.client.common.ui.VCRegistrationItemsINV007";
	public final static String INV009 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCAllocateFromLocationINV009";
	public static final String INV009_FRAME_NAME =
		"FrmAllocateFromLocationINV009";
	public static final String INV009_FRAME_TITLE =
		"Inventory - Allocate From Location     INV009";
	public final static String INV010 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCAllocateToLocationINV010";

	public static final String INV010_FRAME_NAME =
		"FrmAllocateToLocationINV010";
	public static final String INV010_FRAME_TITLE =
		"Inventory - Allocate To Location     INV010";
	public final static String INV011 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCDeleteItemFromInvoiceINV011";
	public static final String INV011_FRAME_TITLE =
		"Inventory - Delete Item From Invoice     INV011";
	public static final String INV011_FRAME_NAME =
		"FrmDeleteItemFromInvoiceINV011";
	public final static String INV012 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCAddItemToInvoiceINV012";
	public static final String INV012_FRAME_NAME =
		"FrmAddItemToInvoiceINV012";
	public static final String INV012_FRAME_TITLE =
		"Inventory - Add Item to Invoice     INV012";
	public final static String INV013 =
		"com.txdot.isd.rts.client.inventory.ui.VCAllocateItemINV013";
	public static final String INV013_FRAME_NAME =
		"FrmAllocateItemINV013";
	public static final String INV013_FRAME_TITLE =
		"Inventory - Allocate Item     INV013";
	public final static String INV014 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCAllocationSummaryINV014";
	public static final String INV014_FRAME_NAME =
		"FrmAllocationSummaryINV014";
	public static final String INV014_FRAME_TITLE =
		"Inventory - Allocation Summary     INV014";
	public final static String INV015 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryHistoryCountyINV015";
	public static final String INV015_FRAME_NAME =
		"FrmInventoryHistoryCountyINV015";
	public static final String INV015_FRAME_TITLE =
		"Inventory History County Selection     INV015";
	public final static String INV016 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryProfileINV016";
	public static final String INV016_FRAME_NAME =
		"FrmInventoryProfileINV016";
	public static final String INV016_FRAME_TITLE =
		"Inventory - Inventory Profile     INV016";
	public final static String INV017 =
		"com.txdot.isd.rts.client.inventory.ui.VCItemsOnHoldINV017";
	public static final String INV017_FRAME_NAME =
		"FrmItemsOnHoldINV017";
	public static final String INV017_FRAME_TITLE =
		"Inventory - Items on Hold     INV017";
	public final static String INV018 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCPlaceItemsOnHoldINV018";
	public static final String INV018_FRAME_NAME =
		"FrmPlaceItemsOnHoldINV018";
	public static final String INV018_FRAME_TITLE =
		"Inventory - Place Items on Hold     INV018";
	//	defect 6964
	public final static String INV019 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryActionReportDateSelectionINV019";

	public static final String INV019_FRAME_NAME =
		"FrmInventoryActionReportDateSelectionINV019";
	public static final String INV019_FRAME_TITLE =
		"Inventory Action Report Date Selection     INV019";
	// end defect 6964
	public final static String INV020 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryInquirySelectionINV020";
	public static final String INV020_FRAME_NAME =
		"FrmInventoryInquirySelectionINV020";
	public static final String INV020_FRAME_TITLE =
		"Inventory Inquiry Selection     INV020";
	public final static String INV021 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryInquiryCriteriaINV021";
	public static final String INV021_FRAME_NAME =
		"FrmInventoryInquiryCriteriaINV021";
	public static final String INV021_FRAME_TITLE =
		"Inventory Inquiry Criteria     INV021";
	public final static String INV022 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryInquiryItemTypeINV022";
	public static final String INV022_FRAME_NAME =
		"FrmInventoryInquiryItemTypeINV022";
	public static final String INV022_FRAME_TITLE =
		"Inventory Inquiry Item Type(s)     INV022";
	public final static String INV023 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryInquiryBySpecificItemNumberINV023";

	public static final String INV023_FRAME_NAME =
		"FrmInventoryInquiryBySpecificItemNumberINV023";
	public static final String INV023_FRAME_TITLE =
		"Inventory Inquiry by Specific Item Number     INV023";
	public final static String INV024 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryHistoryItemSelectionINV024";
	public static final String INV024_FRAME_NAME =
		"FrmInventoryHistoryItemSelectionINV024";
	public static final String INV024_FRAME_TITLE =
		"Inventory History Item Selection     INV024";
	public final static String INV025 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryItemRangeDeleteINV025";
	public static final String INV025_FRAME_NAME =
		"FrmInventoryItemRangeDeleteINV025";
	public static final String INV025_FRAME_TITLE =
		"Inventory Item Range Delete     INV025";
	public final static String INV026 =
		"com.txdot.isd.rts.client.inventory.ui.VCDeletionSummaryINV026";
	public static final String INV026_FRAME_NAME =
		"FrmDeletionSummaryINV026";
	public static final String INV026_FRAME_TITLE =
		"Inventory - Deletion Summary     INV026";
	public final static String INV027 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCInventoryInquirySelectionINV027";
	public static final String INV027_FRAME_NAME =
		"FrmInventoryInquirySelectionINV027";
	public static final String INV027_FRAME_TITLE =
		"Inventory Inquiry Selection     INV027";
	//	defect 6965
	public final static String INV028 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCModifyItemOnInvoiceINV028";
	public static final String INV028_FRAME_NAME =
		"FrmModifyItemOnInvoiceINV028";
	public static final String INV028_FRAME_TITLE =
		"Inventory - Modify Item on Invoice     INV028";
	// end defect 6965
	public final static String INV029 =
		"com.txdot.isd.rts.client.common.ui.VCItemNumberNotFoundINV029";
	public final static String INV030 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCViewInventoryProfileINV030";
	public static final String INV030_FRAME_NAME =
		"FrmViewInventoryProfileINV030";
	public static final String INV030_FRAME_TITLE =
		"Inventory - View Inventory Profile     INV030";
	// defect 9117
	public final static String INV031 =
		"com.txdot.isd.rts.client.inventory.ui."
			+ "VCVIRejectionReportINV031";
	public static final String INV031_FRAME_NAME =
		"FrmVIRejectionReportINV031";
	public static final String INV031_FRAME_TITLE =
		"PLP Request Rejection Report     INV031";
	// end defect 9117
	public final static String KEY001 =
		"com.txdot.isd.rts.client.common.ui."
			+ "VCInquiryKeySelectionKEY001";
	// defect 9086
	public final static String KEY002 =
		"com.txdot.isd.rts.client.common.ui"
			+ ".VCSpecialPlateInquiryKEY002";
	public final static String KEY002_FRM_TITLE =
		"Special Plate Inquiry   KEY002";
	// end defect 9086

	// defect 10014 
	public final static String KEY003_FRM_TITLE =
		"Sales Tax Inquiry     KEY003";
	// end defect 10014 

	public final static String KEY003 =
		"com.txdot.isd.rts.client.reports.ui.VCSalesTaxInquiryKEY003";
	public final static String KEY006 =
		"com.txdot.isd.rts.client.common.ui.VCVinKeySelectionKEY006";
	// Document Number Entry - Delete Title in Process
	public final static String KEY007 =
		"com.txdot.isd.rts.client.title.ui."
			+ "VCDocumentNumberInquiryKEY007";
	public final static String KEY007_FRAME_NAME =
		"FrmDocumentNumberInquiryKEY007";
	public final static String KEY007_FRAME_TITLE =
		"Document Number Inquiry KEY007";
	public final static String KEY008 =
		"com.txdot.isd.rts.client.common.ui.VCPlateKeySelectionKEY008";
	public final static String KEY021 =
		"com.txdot.isd.rts.client.accounting.ui."
			+ "VCFundsKeySelectionKEY021";
	// defect 7768
	public final static String MES001 =
		"com.txdot.isd.rts.client.help.ui.VCMessagesMES001";
	public final static String MES001_FRAME_TITLE =
		"RTS Messages  MES001";
	public final static String MES002 =
		"com.txdot.isd.rts.client.help.ui.VCReplyMES002";
	public final static String MES002_FRAME_TITLE =
		"RTS Message Reply  MES002";
	// end defect 7768

	// defect 10491	
	// public final static String MRG001 =
	//	"com.txdot.isd.rts.client.miscreg.ui.VC30DayPermitTypesMRG001";
	// public final static String MRG008 =
	// com.txdot.isd.rts.client.miscreg.ui.VCOneTripMRG008";

	public final static String MRG002 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCTimedPermitInquiryMRG002";

	public final static String MRG007 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCTimedPermitVinKeySelectionMRG007";
	// end defect 10491 

	public final static String MRG004 =
		"com.txdot.isd.rts.client.miscreg.ui.VCTowTruckMRG004";
	public final static String MRG005 =
		"com.txdot.isd.rts.client.miscreg.ui.VCTimedPermitTypesMRG005";
	public final static String MRG006 =
		"com.txdot.isd.rts.client.miscreg.ui.VCTimedPermitMRG006";

	public final static String MRG010 =
		"com.txdot.isd.rts.client.miscreg.ui.VCTempAddlWeightMRG010";
	public final static String MRG011 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCTempAddlWeightOptionsMRG011";
	public final static String MRG013 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCNonResidentAgricultureMRG013";

	// defect 9831 
	public final static String MRG020 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCDisabledPlacardInquiryMRG020";

	public final static String MRG021 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCDisabledPlacardSearchResultsMRG021";

	public final static String MRG022 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCDisabledPersonInformationMRG022";

	public final static String MRG023 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCAssignedDisabledPlacardsMRG023";

	public final static String MRG024 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCDeleteDisabledPlacardMRG024";

	public final static String MRG025 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCAddNewDisabledPlacardMRG025";

	public final static String MRG026 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCReplaceDisabledPlacardMRG026";

	public final static String MRG027 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCDisabledPlacardReportMRG027";
	// end defect 9831
	
	// defect 11214 
	public final static String MRG028 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCRenewDisabledPlacardMRG028";
	public final static String MRG029 =
		"com.txdot.isd.rts.client.miscreg.ui."
			+ "VCReinstateDisabledPlacardMRG029";
	// end defect 11214 
	
	public final static String OPT001 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCDealerInformationOPT001";
	public final static String OPT002 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCLienHolderInformationOPT002";
	public final static String OPT003 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSubcontractorInformationOPT003";
	public final static String OPT004 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCPaymentAccountUpdateOPT004";
	public final static String OPT005 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCPaymentAccountUpdateOPT005";
	public final static String OPT006 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCMaintainCreditCardFeesOPT006";
	public final static String OPT007 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCMaintainCreditCardFeesOPT007";
	public final static String PENTRN =
		"com.txdot.isd.rts.client.common.ui.VCPendingTransactions";
	public final static String PMT001 =
		"com.txdot.isd.rts.client.common.ui.VCPaymentPMT001";
	public final static String PMT002 =
		"com.txdot.isd.rts.client.common.ui.VCMiscFeePMT002";
	public final static String PMT003 =
		"com.txdot.isd.rts.client.common.ui.VCCreditPMT003";
	public final static String PMT004 =
		"com.txdot.isd.rts.client.common.ui.VCFeesDuePMT004";
	public final static String PUB002 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCPublishingSubstationUpdateAuthorityPUB002";
	public final static String REG001 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCStickerSelectionREG001";
	public final static String REG002 =
		"com.txdot.isd.rts.client.common.ui."
			+ "VCSpecialRegistrationREG002";
	public final static String REG003 =
		"com.txdot.isd.rts.client.registration.ui.VCRegistrationREG003";
	// Registration Refund
	public final static String REG004 =
		"com.txdot.isd.rts.client.title.ui.VCRegistrationRefundREG004";
	public final static String REG004_FRAME__NAME =
		"FrmRegistrationRefundREG004";
	public final static String REG004_FRAME_TITLE =
		"Registration Refund     REG004";
	public final static String REG005 =
		"com.txdot.isd.rts.client.title.ui.VCCancelRegistrationREG005";
	public final static String REG005_FRAME__NAME =
		"FrmCancelRegistrationREG005";
	public final static String REG005_FRAME_TITLE =
		"Cancel Registration     REG005";
	public final static String REG006 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCSubcontractorEntryREG006";
	public final static String REG007 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCRegistrationSubcontractorRenewalREG007";
	public final static String REG008 =
		"com.txdot.isd.rts.client.common.ui."
			+ "VCClassPlateStickerTypeREG008";
	public final static String REG009 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCModifySubcontractorRenewalREG009";
	public final static String REG010 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCVehicleWeightREG010";
	public final static String REG014 =
		"com.txdot.isd.rts.client.common.ui."
			+ "VCMainframeRecordNotAvailableREG014";

	// defect 9126 
	public final static String REG011 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCPlateSelectionREG011";
	// end defect 9126 

	public final static String REG016 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCReplacementChoicesREG016";
	public final static String REG024 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCModifyRegistrationChoicesREG024";
	public final static String REG025 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCRegistrationCorrectionREG025";
	public final static String REG029 =
		"com.txdot.isd.rts.client.common.ui.VCEnterRegistrationREG029";
	public final static String REG032 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCOwnerRegistrationREG032";
	public final static String REG033 =
		"com.txdot.isd.rts.client.registration.ui.VCOwnerAddressREG033";
	public final static String REG039 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCRegistrationAdditionalInfoREG039";
	public final static String REG050 =
		"com.txdot.isd.rts.client.registration.ui."
			+ "VCEntryPreferencesREG050";
	public final static String REG053 =
		"com.txdot.isd.rts.client.registration.ui.VCCopyFailureREG053";
	public final static String RPR000 =
		"com.txdot.isd.rts.client.reports.ui.VCPreviewReportRPR000";
	public final static String RPR001 =
		"com.txdot.isd.rts.client.misc.ui.VCReprintReceiptRPR001";
	public final static String RPR002 =
		"com.txdot.isd.rts.client.reports.ui.VCReprintReportsRPR002";
	public final static String RPR003 =
		"com.txdot.isd.rts.client.reports.ui."
			+ "VCTitlePackageReportRPR003";
	public final static String RPR004 =
		"com.txdot.isd.rts.client.reports.ui.VCReprintStickerRPR004";
	// defect 8900
	// Task 43
	public final static String RPR005 =
		"com.txdot.isd.rts.client.reports.ui.VCExemptAuditReportRPR005";
	// end defect 8900

	// defect 9972 
	public final static String RPR006 =
		"com.txdot.isd.rts.client.reports.ui.VCElectronicTitleReportRPR006";
	public final static String RPR007 =
		"com.txdot.isd.rts.client.reports.ui.VCElectronicTitleReportAddlCriteriaRPR007";
	// end defect 9972 		

	// defect 10086 
	public final static String RPR008 =
		"com.txdot.isd.rts.client.reports.ui.VCPrintRangeRPR008";
	// end defect 10086

	// defect 10900 
	public final static String RPR009 =
		"com.txdot.isd.rts.client.reports.ui.VCSuspectedFraudReportRPR009";
	// end defect 10900

	public final static String RSP001 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCRSPSStatusUpdateRSP001";
	public final static String SEC001 =
		"com.txdot.isd.rts.client.localoptions.ui.VCLogonSEC001";
	public final static String SEC002 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCPasswordChangeSEC002";
	public final static String SEC005 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCEmployeeAccessRightsSEC005";
	public final static String SEC006 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsRegSEC006";
	public final static String SEC007 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsTitleRegSEC007";
	public final static String SEC008 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsStatusChangeSEC008";
	public final static String SEC009 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsInquirySEC009";
	public final static String SEC010 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsMiscRegSEC010";
	public final static String SEC011 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsMiscellaneousSEC011";
	public final static String SEC012 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsReportsSEC012";
	public final static String SEC013 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsLocalOptionsSEC013";
	public final static String SEC014 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityRightsAccountingSEC014";
	public final static String SEC015 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsInventorySEC015";
	public final static String SEC016 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsFundsSEC016";
	public final static String SEC017 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSupervisorOverrideUpdateSEC017";
	// defect 9124
	public final static String SEC018 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCSecurityAccessRightsSpecialPltsSEC018";
	// end defect 9124
	public final static String SEC020 =
		"com.txdot.isd.rts.client.localoptions.ui.VCServerPlusSEC020";

	// defect 9085 
	public final static String SPL001 =
		"com.txdot.isd.rts.client.specialplates.ui."
			+ "VCSpecialPlateApplicationSPL001";
	public final static String SPL002 =
		"com.txdot.isd.rts.client.specialplates.ui."
			+ "VCSpecialPlateInformationSPL002";
	public final static String SPL003 =
		"com.txdot.isd.rts.client.specialplates.ui."
			+ "VCSpecialPlateApplicationReportsSPL003";
	public final static String SPL004 =
		"com.txdot.isd.rts.client.common.ui."
			+ "VCSpecialPlateMFRecordNotAvailableSPL004";
	// end defect 9085 

	// defect 10700 
	public final static String SPL005 =
		"com.txdot.isd.rts.client.specialplates.ui."
			+ "VCSpecialPlateDuplicateInsigniaSPL005";
	// end defect 10700 

	// Lien Salvage Entry 
	public final static String TTL001 =
		"com.txdot.isd.rts.client.title.ui.VCSalvageLienEntryTTL001";
	public final static String TTL001_FRAME_NAME =
		"FrmSalvageLienEntryTTL001";
	public final static String TTL001_FRAME_TITLE =
		"Salvage Lien Entry     TTL001";
	// Title Type
	public final static String TTL002 =
		"com.txdot.isd.rts.client.title.ui.VCTitleTypesTTL002";
	public final static String TTL002_FRAME_NAME =
		"FrmTitleTypesTTL002";
	public final static String TTL002_FRAME_TITLE =
		"Title Types    TTL002";
	// Title Record Found
	public final static String TTL003 =
		"com.txdot.isd.rts.client.title.ui.VCTitleRecordTTL003";
	public final static String TTL003_FRAME_NAME =
		"FrmTitleRecordTTL003";
	public final static String TTL003_FRAME_TITLE =
		"Title Record   TTL003";
	// No Title Record Found
	public final static String TTL004 =
		"com.txdot.isd.rts.client.title.ui.VCNoTitleRecordTTL004";
	public final static String TTL004_FRAME_NAME =
		"FrmNoTitleRecordTTL004";
	public final static String TTL004_FRAME_TITLE =
		"No Title Record   TTL004";
	// Lienholder - Existing Liens
	public final static String TTL005 =
		"com.txdot.isd.rts.client.title.ui.VCLienDisplayTTL005";
	public final static String TTL005_FRANE_NAME =
		"FrmLienDisplayTTL005";
	public final static String TTL005_TITLE = "Lien Display     TTL005";
	// Status Change
	public final static String TTL006 =
		"com.txdot.isd.rts.client.title.ui.VCStatusChangeRecordTTL006";
	public final static String TTL006_FRAME_NAME =
		"FrmStatusChangeRecordTTL006";
	public final static String TTL006_FRAME_TITLE =
		"Status Change Record       TTL006";
	// Owner Info Entry
	public final static String TTL007 =
		"com.txdot.isd.rts.client.title.ui.VCOwnerEntryTTL007";
	public final static String TTL007_FRAME_NAME =
		"FrmOwnerEntryTTL007";
	public final static String TTL007_FRAME_TITLE =
		"Owner Entry    TTL007";
	// Title Additional Info		
	public final static String TTL008 =
		"com.txdot.isd.rts.client.title.ui.VCTitleAdditionalInfoTTL008";
	public final static String TTL008_FRAME_NAME =
		"FrmTitleAdditionalInfoTTL008";
	public final static String TTL008_FRAME_TITLE =
		"Title Additional Info     TTL008";
	public final static String TTL009 =
		"com.txdot.isd.rts.client.common.ui.VCStolenWaivedTTL009";
	
	// defect 10827 
	// Rights of Survivorship Names 
	public final static String TTL010 =
		"com.txdot.isd.rts.client.title.ui.VCRightsofSurvivorshipTTL010";
	// end defect 10827 
	
	// Ownership Evdidence
	public final static String TTL011 =
		"com.txdot.isd.rts.client.title.ui.VCOwnershipEvidenceTTL011";
	public final static String TTL011_FRAME_NAME =
		"FrmOwnershipEvidenceTTL011";
	public final static String TTL011_FRAME_TITLE =
		"Ownership Evidence      TTL011";
	// Sales Tax Info		
	public final static String TTL012 =
		"com.txdot.isd.rts.client.title.ui.VCSalesTaxTTL012";
	public final static String TTL012_FRAME_NAME = "FrmSalesTaxTTL012";
	public final static String TTL012_FRAME_TITLE =
		"Sales Tax     TTL012";
	// Mailing Info - Salvage
	public final static String TTL013 =
		"com.txdot.isd.rts.client.title.ui."
			+ "VCSalvageCCOMailingInfoTTL013";
	public final static String TTL013_FRAME_NAME =
		"FrmSalvageCCOMailingInfoTTL013";
	public final static String TTL013_FRAME_TITLE =
		"Salvage CCO Mailing Info     TTL013";

	// defect 9810 
	// COA			
	//	public final static String TTL015 =
	//		"com.txdot.isd.rts.client.title.ui.VCCOATTL015";
	//	public final static String TTL015_FRAME_NAME = "FrmCOATTL015";
	//	public final static String TTL015_FRAME_TITLE = "COA    TTL015";
	// end defect 9810 

	// Salvage
	public final static String TTL016 =
		"com.txdot.isd.rts.client.title.ui.VCSalvageTTL016";
	public final static String TTL016_FRAME_NAME = "FrmSalvageTTL016";
	public final static String TTL016_FRAME_TITLE = "Salvage    TTL016";
	// CCO
	public final static String TTL018_FRAME_TITLE = "CCO     TTL018";
	public final static String TTL019_FRAME_TITLE =
		"CCO Mailing Info      TTL019";

	// defect 10112 
	public final static String TTL018 =
		"com.txdot.isd.rts.client.title.ui.VCCCOTTL018";
	public final static String TTL018_FRAME_NAME = "FrmCCOTTL018";
	// Mailing Info - CCO
	public final static String TTL019 =
		"com.txdot.isd.rts.client.title.ui.VCCCOMailingInfoTTL019";
	public final static String TTL019_FRAME_NAME =
		"FrmCCOMailingInfoTTL019";
	// end defect 10112 

	// Vehicle Junked
	public final static String TTL028 =
		"com.txdot.isd.rts.client.title.ui.VCVehicleJunkedTTL028";
	public final static String TTL028_FRAME_NAME =
		"FrmVehicleJunkedTTL028";
	public final static String TTL028_FRAME_TITLE =
		"Vehicle Junked   TTL028";
	// Title Surrendard
	public final static String TTL029 =
		"com.txdot.isd.rts.client.title.ui.VCTitleSurrenderedTTL029";
	public final static String TTL029_FRAME_NAME =
		"FrmTitleSurrenderedTTL029";
	public final static String TTL029_FRAME_TITLE =
		"Title Surrendered       TTL029";
	// Miscellaneous
	public final static String TTL030 =
		"com.txdot.isd.rts.client.title.ui."
			+ "VCMiscellaneousRemarksTTL030";
	public final static String TTL030_FRAME_NAME =
		"FrmMiscellaneousRemarksTTL030";
	public final static String TTL030_FRAME_TITLE =
		"Miscellaneous Remarks      TTL030";
	public final static String TTL033 =
		"com.txdot.isd.rts.client.title.ui."
			+ "VCAdditionalSalesTaxScreenTTL033";
	public final static String TTL033_FRAME_NAME =
		"FrmAdditionalSalesTaxScreenTTL033";
	public final static String TTL033_FRAME_TITLE =
		"Additional Sales Tax      TTL033";
	// Lienholder - Data Entry
	public final static String TTL035 =
		"com.txdot.isd.rts.client.title.ui.VCLienEntryTTL035";
	public final static String TTL035_FRAME_NAME = "FrmLienEntryTTL035";
	public final static String TTL035_FRAME_TITLE =
		"Lien Entry      TTL035";
	// Stolen SRS
	public final static String TTL037 =
		"com.txdot.isd.rts.client.title.ui.VCStolenSRSTTL037";
	public final static String TTL037_FRAME_NAME = "FrmStolenSRSTTL037";
	public final static String TTL037_FRAME_TITLE =
		"Stolen/SRS    TTL037";
	// Transfer Other Screen (MF Down)
	public final static String TTL040 =
		"com.txdot.isd.rts.client.title.ui."
			+ "VCTransferOtherScreenTTL040";
	public final static String TTL040_FRAME_NAME =
		"FrmTransferOtherScreenTTL040";
	public final static String TTL040_FRAME_TITLE =
		"Transfer/Other   TTL040";
	// defect 8786
	// Delete Title In Process
	public final static String TTL042 =
		"com.txdot.isd.rts.client.title.ui."
			+ "VCDeleteTitleInProcessTTL042";
	public final static String TTL042_FRAME_NAME =
		"FrmDeleteTitleinProcessTTL042";
	public final static String TTL042_FRAME_TITLE =
		"Delete Title In Process      TTL042";
	// end defect 8786
	// defect 8926
	public final static String TTL045 =
		"com.txdot.isd.rts.client.title.ui."
			+ "VCPresumptiveValueTTL045";
	public final static String TTL045_FRAME_TITLE =
		"Presumptive AbstractValue      TTL045";
	// end defect 8926
	public final static String VOI001 =
		"com.txdot.isd.rts.client.misc.ui.VCTransactionKeyVOI001";
	public final static String VOI002 =
		"com.txdot.isd.rts.client.misc.ui."
			+ "VCTransactionAvailabletovoidVOI002";

	// Phase II constants
	// defect 7889
	// Renamed VC's to have the frame number in the class name
	// Renamed Constants from CP### to REG### and removed duplicates.
	public final static String REG101 =
		"com.txdot.isd.rts.client.webapps.registrationrenewal.ui."
			+ "VCVehicleSearchREG101";
	public final static String REG102 =
		"com.txdot.isd.rts.client.webapps.registrationrenewal.ui."
			+ "VCSearchResultsREG102";
	public final static String REG103 =
		"com.txdot.isd.rts.client.webapps.registrationrenewal.ui."
			+ "VCProcessVehicleREG103";
	public final static String REG104 =
		"com.txdot.isd.rts.client.webapps.registrationrenewal.ui."
			+ "VCInsuranceInfoREG104";
	public final static String REG105 =
		"com.txdot.isd.rts.client.webapps.registrationrenewal.ui."
			+ "VCAddressInfoREG105";
	public final static String REG106 =
		"com.txdot.isd.rts.client.webapps.registrationrenewal.ui."
			+ "VCReportSelectionREG106";

	// 7885
	// Add constants to be used in Frames
	public final static int DESKTOP_DEV_HEIGHT = 480;
	public final static int DESKTOP_DEV_WIDTH = 640;

	public final static String MSG_FRAME_EXCEPTION = "Frame Exception ";

	public final static String MSG_MAIN_ERR_CATCH =
		"Exception occurred in main() of com.txdot.isd.rts.client."
			+ "general.gui.JDialogBox";

	public final static String MSG_UNCAUGHT_EXECPTION =
		"--------- UNCAUGHT EXCEPTION ---------";

	public final static String RTS_TITLE_STRING =
		"Registration and Title System";

	public static final String SEC005_FRAME_TITLE =
		"Employee Access Rights   SEC005";

	public static final String SEC005_FRAME_NAME =
		"FrmEmployeeAccessRightsSEC005";
	// end defect 7885

	// defect 10701
	public final static String OPT008 =
		"com.txdot.isd.rts.client.localoptions.ui."
			+ "VCBatchReportManagementOPT008";

	public static final String OPT008_FRAME_TITLE =
		"Batch Report Management   OPT008";

	public static final String OPT008_FRAME_NAME =
		"FrmBatchReportManagementOPT008";
	// end defect 10701 
}
