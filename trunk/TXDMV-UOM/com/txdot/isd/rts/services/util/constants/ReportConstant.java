package com.txdot.isd.rts.services.util.constants;

/*
  * ReportConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Add REPRINT_STICKER_REPORT,START_DATE,
 * 							END_DATE
 * 							Ver 5.2.0			 
 * B Hargrove	05/11/2005	Remove reference to Quick Counter.
 *  						delete class variables 
 *							defect 7955 Ver 5.2.3
 * Ray Rowehl	07/06/2005	Reorganize Inventory Reports Constants.
 * 							defect 7890 Ver 5.2.3
 * J Rue		08/16/2005	Add constants SLSTX_UC
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	09/29/2005	Add constant for Profile Report title
 * 							add RPT_3051_INVENTORY_PROFILE_REPORT_TITLE
 * 							defect 7890 Ver 5.2.3
 * K Harrell	10/17/2005  Add RPT_9021_COMPL_SETASIDE_TITLE
 * 							defect 7890 Ver 5.2.3
 * K Harrell	11/08/2005	Use Closeout vs. Close Out when referencing
 * 							a report.  Countywide vs. County Wide or
 * 							County-Wide. Set-Aside vs. Set Aside.
 * 							Add "SUMMARY" to Substation Summary report
 * 							titles. 
 * 							comment unused constants
 * 							add COMPL_SETASIDE_REPORT_TITLE,
 * 							 COMPL_SETASIDE_REPORT_ID
 * 							deleted SETASIDE_REPORT_ID 
 * 							defect 8379 Ver 5.2.3
 * J Ralph		01/27/2006	FormFeed constant cleanup
 * 							add FF
 * 							defect 8524 Ver 5.2.3
 * J Ralph		10/18/2006	Added Exempt Audit Export/Report constants
 * 							add GENERATE_EXEMPT_AUDIT_EXPORT,
 * 							GENERATE_EXEMPT_AUDIT_REPORT
 * 							defect 8900 Ver Exempts
 * J Ralph		10/20/2006  Added additional Exempt Audit constants
 * 							add NUM_LINES_1, NUM_LINES_2,
 * 								RPT_5051_FILENAME, RPT_5051_REPORT_ID,
 * 								RPT_5051_REPORT_TITLE, RPT_5051_RPT_ERR,
 * 								RPT_5051_SUMMARY, WORKSTATION_ID,
 * 								REQUESTED_BY, AUDIT_DATE_FROM, STR_TO
 * 							defect 8900 Ver Exempts
 * J Ralph		11/15/2006	add REC_RET_UNAVAIL, STR_VOIDED, STR_ASTERISK
 * 							defect 9017 Ver Exempts
 * K Harrell	02/17/2007	added constants for Special Plate Reports
 * 							RPT_6001_FILENAME, RPT_6001_REPORT_ID,
 * 							RPT_6001_ONLN_REPORT_TITLE, RPT_6001_RPT_ERR,
 * 							RPT_6001_SUMMARY 
 * 							defect 9085 Ver Special Plates
 * Min Wang		02/26/2007 	added constant for Virtual Inventory
 * 							Inquiry Report.
 *  						RPT_3031_INVENTORY_INQUIRY_REPORT_TITLE
 * 							defect 9117 Ver Special Plates
 * Min Wang		03/19/2007  added constant for Virtual Inventory
 * 							Rejection Report.
 * 							RPT_3073_FILENAME
 * 							RPT_3073_VI_REJ_REPORT_ID
 *  						RPT_3073_VI_REJ_REPORT_TITLE
 * 							defect 9117 Ver Special Plates
 * T Pederson	04/10/2007	Changed value for RPT_6001_FILENAME 
 * 							defect 9123 Ver Special Plates
 * K Harrell	05/09/2007  More work on Special Plates report constants
 * 							defect 9085 Ver Special Plates 	
 * Min Wang		05/14/2007	Changed report title for
 * 							Rejection Report.
 * 							modify RPT_3073_VI_REJ_REPORT_TITLE
 * 							defect 9117 Ver Special Plates
 * K Harrell	08/07/2007	Removed "ONLINE" from Special Plates 
 * 							Online Report.
 * 							modify RPT_6001_ONLN_REPORT_TITLE  
 * 							defect 9202 Ver Special Plates
 * K Harrell	10/27/2008	Added Report constants for Disabled Placard 
 * 							Report. 
 * 							add RPT_8001_ONLN_FILENAME,
 * 							  RPT_8001_REPORT_ID,
 * 							  RPT_8001_REPORT_TITLE,
 * 							  RPT_8001_RPT_ERR,
 * 							  RPT_8001_SUMMARY
 * 							defect 9831 Ver Defect_POS_B  
 * K Harrell	02/09/2009	Add Report constants for Internet Deposit 
 * 							Reconciliation Report 
 * 							add RPT_2004_INTERNET_DEPOSIT_RECON_ID,
 *							  RPT_2004_INTERNET_DEPOSIT_RECON_TITLE,
 *							  RPT_2004_INTERNET_DEPOSIT_RECON_FILENAME
 * 							defect 9935 Ver Defect_POS_D 
 * J Zwiener	02/26/2009	Add Report constants for Certfd Lienhldr Rpt 
 * 							add CERTFD_LIENHLDR_REPORT_ID,
 * 							  CERTFD_LIENHLDR_REPORT_TITLE, 
 * 							  CERTFD_LIENHLDR_REPORT_FILENAME
 * 							defect 9968 Ver Defect_POS_E
 * K Harrell	03/20/2009	Add Report constants for ELT Report 
 * 							add GENERATE_ETITLE_EXPORT, 
 * 							  GENERATE_ETITLE_REPORT, 
 * 							  RPT_5071_ETITLE_REPORT_ID, 
 * 							  RPT_5071_ETITLE_REPORT_TITLE
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	03/30/2009	add LANDSCAPE_REPORT_LIST
 * 							defect 9972 Ver Defect_POS_E  
 * K Harrell	06/15/2009	add FEES_REPORT_ID, FEES_REPORT_TITLE,
 * 							 INVENTORY_DETAIL_REPORT_ID,
 * 							 INVENTORY_DETAIL_REPORT_TITLE,
 * 							 RPR002_COL_RPT_DESC,
 * 							 RPR002_COL_RPT_NO,
 * 							 RPR002_COL_RPT_WSID,
 * 							 RPR002_COL_RPT_DATE,
 * 							 RPR002_COL_RPT_TIME,
 * 							 RPR002_COL_SAME_OBJECT,
 * 							 LINES_PER_PAGE_PORTRAIT,
 * 							 LINES_PER_PAGE_LANDSCAPE, 
 * 							 PRINT_ALL, PRINT_CURRENT,
 * 							 PRINT_SELECTED, EXTENSION_CERTS,
 * 							 NO_PRINT_OPTION_SELECTED, 
 * 							 ESC_CHAR 
 * 							delete csEscChar 
 * 							defect 10086 Ver Defect_POS_F
 * K Harrell	06/18/2009	add RPR000_NEXT_SCREEN_PREVIOUS,
 * 							 RPR000_NEXT_SCREEN_FINAL, 
 * 							 RPR000_NEXT_SCREEN_CANCEL 
 * 							defect 10011 Ver Defect_POS_F 
 * K Harrell	06/22/2009	add RPT_2001_ADDR_CHNG_TITLE,
 * 							 RPT_2001_ADDR_CHNG_ID,
 * 							 RPT_2001_ADDR_CHG_FILENAME,
 * 							 RPT_2002_ITRNT_TRANS_RECON_TITLE,
 * 							 RPT_2002_ITRNT_TRANS_RECON_ID,
 * 							 RPT_2002_ITRNT_TRANS_RECON_FILENAME,
 * 							 RPT_2003_VENDOR_PAYMENT_TITLE,
 * 							 RPT_2003_VENDOR_PAYMENT_ID,
 * 							 RPT_2003_VENDOR_PAYMENT_BATCH_FILENAME,
 * 							 RPT_2003_VENDOR_PAYMENT_ONLINE_FILENAME,
 * 							 VENDOR_RPT_CUTOFF_TIME,
 * 							 ITRANS_RPT_CUTOFF_TIME,
 * 							 RPT_2004_INTERNET_DEPOSIT_RECON_BATCH_FILENAME,
 * 							 RPT_2004_INTERNET_DEPOSIT_RECON_ONLINE_FILENAME, 
 * 							 RPT_9001_FILENAME, RPT_9001_REPORT_ID,
 * 							 RPT_9001_REPORT_TITLE
 * 							defect 10023 Ver Defect_POS_F
 * K Harrell	07/22/2009	add SALES_TAX_ALLOCATION_REPORT_TITLE,
 * 							 SALES_TAX_ALLOCATION_REPORT_FILENAME,
 * 							 BATCH_TITLE_PACKAGE_REPORT_TITLE,
 * 							 BATCH_TITLE_PACKAGE_REPORT_FILENAME,
 * 							 TITLE_PACKAGE_REPORT_TITLE,
 * 							 TITLE_PACKAGE_REPORT_ONLINE_FILENAME,
 * 							 REPRINT_STICKER_REPORT_TITLE,
 * 							 REPRINT_STICKER_REPORT_FILENAME,
 * 							 COMPL_SETASIDE_REPORT_FILENAME
 * 							 defect 10023 Ver Defect_POS_F
 * K Harrell	08/17/2009	Correct Report Id for InternetTransReconRpt
 * 							(changed from 6/22/2009) 
 * 							modify RPT_2002_ITRNT_TRANS_RECON_ID
 * 							 defect 10023 Ver Defect_POS_F
 * K Harrell	08/22/2009	add BEGIN_DATE, END_DATE, ADMIN_LOG
 * 							defect 8628 Ver Defect_POS_F  
 * K Harrell	08/26/2009	add RPT_COPIES_FUNDS_FILENAME_BY_WSID
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	10/12/2009	add RPT_3071_RPT_ERR, RPT_3072_RPT_ERR
 * 							defect 10207 Ver Defect_POS_G
  * K Harrell	10/13/2009	add DLR_PRELIMINARY_RPT_TITLE,
 * 							 DLR_PRELIMINARY_RPT_FILENAME,
 * 							 DLR_PRELIMINARY_RPT_ID,
 * 							 DLR_STKR_RCPT_RPT_TITLE,
 * 							 DLR_STKR_RCPT_RPT_FILENAME,
 * 							 DLR_STKR_RCPT_RPT_ID,
 * 							 DLR_COMPLETED_RPT_FILENAME,
 * 							 DLR_COMPLETED_RPT_TITLE,
 * 							 DLR_COMPLETED_RPT_ID
 * 							defect 10251 Ver Defect_POS_G 
 * K Harrell	10/20/2009	add SORT_BY_ID,  SORT_BY_ID_INDI,  
 * 							 SORT_BY_NAME_INDI, SORT_ORDER_HDG, 
 * 							 SORT_ORDER_ID, SORT_ORDER_NAME,
 * 							 ADMIN_LOG_SORT_ORDER_ID, 
 * 							 ADMIN_LOG_SORT_ORDER_NAME 
 * 							defect 10250 Ver Defect_POS_G 
 * B Hargrove	10/28/2009	Changed report constant for Title Package
 * 							batch. 
 * 							defect 10261 Ver Defect_POS_G
 * K Harrell	09/14/2010	add PREVIEW_RCPT_NAME
 * 							defect 10590 Ver 6.6.0 
 * K Harrell	01/18/2011	add RPT_EXTENSION
 * 							defect 10701 Ver 6.7.0 
 * K Harrell	06/03/2011	add GENERATE_FRAUD_EXPORT,
 * 							 GENERATE_FRAUD_REPORT,
 * 							 FRAUD_REPORT_ID, FRAUD_REPORT_TITLE,
 * 							 FRAUD_REPORT_FILENAME, 
 * 							 FRAUD_REPORT_ERR
 * 							defect 10900 Ver 6.8.0   
 * ---------------------------------------------------------------------
 */

/**
 * Constants for Report Module, including Function Ids, Report Titles, 
 * Report Ids, Report File Names, etc. 
 * 
 * @version	6.8.0 			06/03/2011
 * @author	Rakesh Duggirila
 * <br>Creation Date:		07/17/2001 
 */

public class ReportConstant
{
	//General
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;
	public final static int GENERATE_SALES_TAX_REPORT = 1;
	public final static int GENERATE_REPRINT_REPORT = 2;
	public final static int PRINT_REPRINT_REPORT = 200;

	/**
	 * Do not keep any back versions of the report.
	 */
	public final static int RPT_1_COPY = 1;
	/**
	 * Keep 7 levels of the report.
	 */
	public final static int RPT_7_COPIES = 7;

	public final static int GENERATE_TITLE_PACKAGE_REPORT = 4;
	public final static int REPRINT_STICKER_REPORT = 5;
	// defect 8900
	// Add constants for Exempt Audit Export/Report
	public final static int GENERATE_EXEMPT_AUDIT_EXPORT = 6;
	public final static int GENERATE_EXEMPT_AUDIT_REPORT = 7;
	// end defect 8900

	// defect 9972 
	public final static int GENERATE_ETITLE_EXPORT = 8;
	public final static int GENERATE_ETITLE_REPORT = 9;
	// end defect 9972 
	
	// defect 10900
	public final static int GENERATE_FRAUD_EXPORT = 10;
	public final static int GENERATE_FRAUD_REPORT = 11;
	// end defect 10900  


	public final static String SUBSTATION = "SUBSTATION";
	public final static String EMPLOYEE = "EMPLOYEE";

	// set up constants for subtitles and total lines
	public final static String CASH_DRAWER = "CASH DRAWER";
	// Used in setTypeOfReportOperationText
	public final static String CASH_DRAWER_OPER =
		"CASH DRAWER OPERATIONS: ";
	public final static String TOTAL_CASH_DRAWER_OPER =
		"TOTAL CASH DRAWER OPERATIONS: ";
	public final static String NON_CASH_DRAWER_OPER =
		"NON-CASH DRAWER OPERATIONS: ";
	public final static String TOTAL_NON_CASH_DRAWER_OPER =
		"TOTAL NON-CASH DRAWER OPERATIONS:";

	// Used in SetSummaryForText
	public final static String SUMMARY_FOR_CASH_DRAWER =
		"SUMMARY FOR CASH DRAWER ";
	public final static String TOTAL_FOR_CASH_DRAWER =
		"TOTAL FOR CASH DRAWER ";
	public final static String SUMMARY_FOR_EMPLOYEE =
		"SUMMARY FOR EMPLOYEE ";
	public final static String TOTAL_FOR_EMPLOYEE =
		"TOTAL FOR EMPLOYEE ";
	public final static String SUMMARY_FOR_SUBSTA =
		"SUMMARY FOR SUBSTATION ";
	public final static String TOTAL_FOR_SUBSTA =
		"TOTAL FOR SUBSTATION ";

	// fields for summary report page
	public final static String REPORT_TOTAL_TEXT = "REPORT TOTAL:";
	public final static String SUMMARY_FOR_REPORT_TEXT =
		"SUMMARY FOR REPORT:";

	public final static int CENTER = 1;
	public final static int REPORT_FOOTER_LINES = 2;
	public final static int PORTRAIT = 0;
	public final static int LANDSCAPE = 1;

	// defect 8900
	public final static int NUM_LINES_1 = 1;
	public final static int NUM_LINES_2 = 2;
	// end defect 8900

	// defect 10087 
	// defect 6181
	// Create an ESC constant
	public static final String ESC_CHAR = "\u001b";
	// end defect 6181
	// end defect 10087 

	// defect 8628 
	public final static String BEGIN_DATE = "BEGIN_DATE";
	public final static String END_DATE = "END_DATE";
	public final static String ADMIN_LOG = "ADMIN_LOG";
	// end defect 8628 

	// defect 7898
	public final static String SLSTX_UC = "SLSTX";
	// end defect 7898

	//  Constants added to support help links from Preview Report screen
	// defect 10142
	public final static String SALES_TAX_ALLOCATION_REPORT_TITLE =
		"SALES TAX ALLOCATION REPORT";
	public final static String SALES_TAX_ALLOCATION_REPORT_FILENAME =
		"SLSTX";

	public final static String BATCH_TITLE_PACKAGE_REPORT_TITLE =
		"BATCH TITLE PACKAGE REPORT";
	// defect 10261
	//public final static String BATCH_TITLE_PACKAGE_REPORT_FILENAME =
	//	"TTLBAT";
	public final static String BATCH_TITLE_PACKAGE_REPORT_FILENAME =
		"TTLPBAT";
	// end defect 10261
	public final static String TITLE_PACKAGE_REPORT_TITLE =
		"TITLE PACKAGE REPORT";
	public final static String TITLE_PACKAGE_REPORT_ONLINE_FILENAME =
		"TTLPONLN";

	public final static String REPRINT_STICKER_REPORT_TITLE =
		"REPRINT STICKER REPORT";

	public final static String REPRINT_STICKER_REPORT_FILENAME =
		"REPSTI";
	// end defect 10142 

	// defect 10251 
	// Dealer Preliminary Report 
	public final static String DLR_PRELIMINARY_RPT_TITLE =
		"DEALER PRELIMINARY REPORT";
	public final static String DLR_PRELIMINARY_RPT_FILENAME =
		"DLRPRELM";
	public final static String DLR_PRELIMINARY_RPT_ID = "RTS.POS.2151";

	// Dealer Sticker Receipt Report
	public final static String DLR_STKR_RCPT_RPT_TITLE =
		"STICKER/RECEIPT REPORT";
	public final static String DLR_STKR_RCPT_RPT_FILENAME = "STKRRCP";
	public final static String DLR_STKR_RCPT_RPT_ID = "RTS.POS.2161";

	// Dealer Completed Report 
	public final static String DLR_COMPLETED_RPT_FILENAME = "DLRCOMPL";
	public final static String DLR_COMPLETED_RPT_TITLE =
		"DEALER COMPLETED REPORT";
	public static final String DLR_COMPLETED_RPT_ID = "RTS.POS.2171";
	// end defect 10251 

	public final static String SUBCONTRACTOR_RENEWAL_REPORT =
		"SUBCONTRACTOR RENEWAL REPORT";

	//Local Options Reports
	public final static String DEALER_REPORT_ID = "RTS.POS.4013";
	public final static String DEALER_REPORT = "DEALER REPORT";

	public final static String LIENHOLDER_REPORT_ID = "RTS.POS.4011";
	public final static String LIENHOLDER_REPORT = "LIENHOLDER REPORT";

	public final static String SUBCON_REPORT_ID = "RTS.POS.4012";
	public final static String SUBCONTRACTOR_REPORT =
		"SUBCONTRACTOR REPORT";
		
	// defect 10900 
	public final static String FRAUD_REPORT_ID =
		"RTS.POS.5912";
	public final static String FRAUD_REPORT_TITLE =
		"SUSPECTED FRAUD REPORT";
	public final static String FRAUD_REPORT_FILENAME =
		"FRDONLN";
	public final static String FRAUD_REPORT_ERR =
			"Exception occurred in formatReport() of "
				+ "com.txdot.isd.rts.services.reports.reports."
				+ "GenFraudReport";
	// end defect 10900 

	// defect 9968 
	public final static String CERTFD_LIENHLDR_REPORT_ID =
		"RTS.POS.4014";
	public final static String CERTFD_LIENHLDR_REPORT_TITLE =
		"CERTIFIED LIENHOLDER REPORT";
	public final static String CERTFD_LIENHLDR_REPORT_FILENAME =
		"CERTLIEN";
	// end defect 9968 

	public final static String EVENT_SECURITY_REPORT_ID =
		"RTS.POS.4002";
	public final static String EVENT_SECURITY_REPORT =
		"EVENT SECURITY REPORT";

	public final static String EMPSEC_REPORT_ID = "RTS.POS.4001";
	public final static String EMPLOYEE_SECURITY_REPORT =
		"EMPLOYEE SECURITY REPORT";

	public final static String SECCHG_REPORT_ID = "RTS.POS.4003";
	public final static String SECURITY_CHANGE_REPORT =
		"SECURITY CHANGE REPORT";

	// defect 8379
	public final static String PUBLISHING_REPORT_ID = "RTS.POS.4021";
	public final static String PUBLISHING_REPORT_TITLE =
		"PUBLISHING REPORT";

	public final static String COMPL_SETASIDE_REPORT_ID =
		"RTS.POS.9021";
	public final static String COMPL_SETASIDE_REPORT_TITLE =
		"COMPLETED SET-ASIDE TRANSACTION REPORT";

	// defect 10142 
	public final static String COMPL_SETASIDE_REPORT_FILENAME =
		"INCTRN";
	// end defect 10142 

	// Funds Reports 

	// Substation Summary Report Info 		
	public final static String SUBSTATION_REPORT_ID = "RTS.POS.5921";
	public final static String SUBSTATION_PYMNT_REPORT_ID =
		"RTS.POS.5921";
	public final static String SUBSTATION_PYMNT_REPORT_TITLE =
		"SUBSTATION SUMMARY PAYMENT TYPE REPORT";

	public final static String SUBSTATION_FEES_REPORT_ID =
		"RTS.POS.5922";
	public final static String SUBSTATION_FEES_REPORT_TITLE =
		"SUBSTATION SUMMARY FEES REPORT";

	public final static String SUBSTATION_INV_REPORT_ID =
		"RTS.POS.5923";
	public final static String SUBSTATION_INV_REPORT_TITLE =
		"SUBSTATION SUMMARY INVENTORY REPORT";

	// CountyWide Report Info
	public final static String COUNTYWIDE_REPORT_ID = "RTS.POS.5901";
	public final static String COUNTYWIDE_PYMNT_REPORT_ID =
		"RTS.POS.5901";
	public final static String COUNTYWIDE_PYMNT_REPORT_TITLE =
		"COUNTYWIDE PAYMENT TYPE REPORT";

	public final static String COUNTYWIDE_FEES_REPORT_ID =
		"RTS.POS.5902";
	public final static String COUNTYWIDE_FEES_REPORT_TITLE =
		"COUNTYWIDE FEES REPORT";

	public final static String COUNTYWIDE_INV_REPORT_ID =
		"RTS.POS.5903";
	public final static String COUNTYWIDE_INV_REPORT_TITLE =
		"COUNTYWIDE INVENTORY REPORT";

	public final static String COUNTYWIDE_EXCEPTION_REPORT_ID =
		"RTS.POS.5904";
	public final static String COUNTYWIDE_EXCEPTION_REPORT_TITLE =
		"COUNTYWIDE EXCEPTION REPORT";
	// end defect 8379 	

	public final static String PAYMENT_TYPE_REPORT_ID = "RTS.POS.5211";
	public final static String CURRENT_STATUS_REPORT_ID =
		"RTS.POS.5212";
	public final static String CLOSEOUT_REPORT_ID = "RTS.POS.5213";
	public final static String CLOSEOUT_STATISTICS_REPORT_ID =
		"RTS.POS.5201";
	public final static String CLOSEOUT_STATISTICS_REPORT_TITLE =
		"CLOSEOUT STATISTICS REPORT";

	public final static String TRANSACTION_RECON_REPORT_ID =
		"RTS.POS.5231";

	public final static String RPT_5221_INVENTORY_SUMMARY_REPORT_ID =
		"RTS.POS.5221";

	// defect 10023
	public final static String RPT_2001_ADDR_CHNG_TITLE =
		"RENEWAL RECIPIENT ADDRESS CHANGE REPORT";
	public final static String RPT_2001_ADDR_CHNG_ID = "RTS.WBA.2001";
	public final static String RPT_2001_ADDR_CHG_FILENAME = "ADDRCHG";

	public final static String RPT_2002_ITRNT_TRANS_RECON_TITLE =
		"INTERNET TRANSACTION RECONCILIATION DETAIL REPORT";
	public final static String RPT_2002_ITRNT_TRANS_RECON_ID =
		"RTS.WBA.2002";
	public final static String RPT_2002_ITRNT_TRANS_RECON_FILENAME =
		"ITRANS";

	public final static String RPT_2003_VENDOR_PAYMENT_TITLE =
		"VENDOR PAYMENT REPORT";
	public final static String RPT_2003_VENDOR_PAYMENT_ID =
		"RTS.WBG.2003";

	public final static String RPT_2003_VENDOR_PAYMENT_BATCH_FILENAME =
		"VPRBAT";
	public final static String RPT_2003_VENDOR_PAYMENT_ONLINE_FILENAME =
		"VPRONLN";

	//	Vendor report time ==> eastern time
	public final static String VENDOR_RPT_CUTOFF_TIME = "02:00:00";
	// TxDOT server time  ==> central time
	public final static String ITRANS_RPT_CUTOFF_TIME = "01:00:00";

	public final static String RPT_2004_INTERNET_DEPOSIT_RECON_BATCH_FILENAME =
		"IDEPBAT";
	public final static String RPT_2004_INTERNET_DEPOSIT_RECON_ONLINE_FILENAME =
		"IDEPONLN";
	// end defect 10023

	// defect 9935 
	public final static String RPT_2004_INTERNET_DEPOSIT_RECON_ID =
		"RTS.POS.2004";
	public final static String RPT_2004_INTERNET_DEPOSIT_RECON_TITLE =
		"INTERNET DEPOSIT RECONCILIATION REPORT";
	// end defect 9935 

	// Inventory Reports
	public final static String RPT_3001_INVENTORY_DELETED_REPORT_ID =
		"RTS.POS.3001";
	public final static String RPT_3001_INVENTORY_DELETE_REPORT_TITLE =
		"INVENTORY DELETED REPORT";
	public final static String RPT_3011_INVENTORY_ALLOCATION_REPORT_ID =
		"RTS.POS.3011";
	public final static String RPT_3011_INVENTORY_ALLOCATE_REPORT_TITLE =
		"INVENTORY ALLOCATE REPORT";
	public final static String RPT_3021_INVENTORY_RECEIVE_REPORT_ID =
		"RTS.POS.3021";
	public final static String RPT_3021_INVENTORY_RECEIVE_REPORT_TITLE =
		"INVENTORY RECEIVED REPORT";
	public final static String RPT_3031_INVENTORY_INQUIRY_REPORT_ID =
		"RTS.POS.3031";
	public final static String RPT_3031_INVENTORY_INQUIRY_REPORT_TITLE =
		"INVENTORY INQUIRY REPORT";
	//defect 9117
	public final static String RPT_3031_VIRTUAL_INVENTORY_INQUIRY_REPORT_TITLE =
		"VIRTUAL INVENTORY INQUIRY REPORT";
	public final static String RPT_3073_VI_REJ_REPORT_TITLE =
		"PLP Request Rejection Report";
	public final static String RPT_3073_FILENAME = "VIREJ";
	public final static String RPT_3073_VI_REJ_REPORT_ID =
		"RTS.POS.3073";
	// end defect 9117
	public final static String RPT_3041_INVENTORY_HOLD_REPORT_ID =
		"RTS.POS.3041";
	public final static String RPT_3041_INVENTORY_HOLD_REPORT_TITLE =
		"INVENTORY ONHOLD REPORT";
	public final static String RPT_3051_INVENTORY_PROFILE_REPORT_ID =
		"RTS.POS.3051";
	public final static String RPT_3051_INVENTORY_PROFILE_REPORT_TITLE =
		"INVENTORY PROFILE REPORT";
	public final static String RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_FILENAME =
		"INDELR";
	public final static String RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_ID =
		"RTS.POS.3071";

	// defect 10207 	
	public final static String RPT_3071_RPT_ERR =
		"Exception occurred in formatReport() of "
			+ "com.txdot.isd.rts.services.reports.reports."
			+ "GenDeleteInventoryHistoryReport";

	public final static String RPT_3072_RPT_ERR =
		"Exception occurred in formatReport() of "
			+ "com.txdot.isd.rts.services.reports.reports."
			+ "GenReceiveInventoryHistoryReport";
	// end defect 10207 

	public final static String RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_TITLE =
		"DELETE INVENTORY HISTORY REPORT";
	public final static String RPT_3072_RECEIVE_INVENTORY_HISTORY_REPORT_ID =
		"RTS.POS.3072";
	public final static String RPT_3072_RECEIVE_INVENTORY_HISTORY_REPORT_TITLE =
		"RECEIVE INVENTORY HISTORY REPORT";

	public final static String BIAR_REPORT_ID = "RTS.POS.9901";
	public final static String BIAR_REPORT =
		"BATCH INVENTORY ACTION REPORT";

	public final static String RPT_9901_IAR_FILENAME = "IARONLN";
	public final static String RPT_9901_IAR_REPORT_ID = "RTS.POS.9901";
	public final static String RPT_9901_IAR_REPORT_TITLE =
		"INVENTORY ACTION REPORT";

	// defect 8900
	// Exempt Report constants 
	public final static String RPT_5051_FILENAME = "EXMPTAUD";
	public final static String RPT_5051_REPORT_ID = "RTS.POS.5051";
	public final static String RPT_5051_REPORT_TITLE =
		"EXEMPT AUDIT REPORT";
	public final static String RPT_5051_RPT_ERR =
		"Exception occurred in formatReport() of "
			+ "com.txdot.isd.rts.services.reports.reports."
			+ "GenExemptAuditReport";
	public final static String RPT_5051_SUMMARY = "SUMMARY";
	public final static String WORKSTATION_ID = "WORKSTATION ID";
	public final static String REQUESTED_BY = "REQUESTED BY";
	public final static String AUDIT_DATE_FROM = "AUDIT DATE FROM";
	public final static String STR_TO = "TO";
	// end defect 8900

	// defect 9972 
	public final static String RPT_5071_ETITLE_REPORT_ID =
		"RTS.POS.5071";
	public final static String RPT_5071_ETITLE_REPORT_FILENAME =
		"ETTLONLN";
	public final static String RPT_5071_ETITLE_REPORT_TITLE =
		"ELECTRONIC TITLE REPORT";
	public final static String RPT_5071_SUMMARY = "SUMMARY";
	public final static String LANDSCAPE_REPORT_LIST =
		"4014 5071 6001 8001";
	// end defect 9972 
	// end defect 9972 

	// defect 9017
	public final static String REC_RET_UNAVAIL =
		"* Record Retrieval Unavailable";
	public final static String STR_VOIDED = "***** VOIDED ***** ";
	public final static String STR_ASTERISK = "*";
	// end defect 9017

	// defect 9085 
	public final static String RPT_6001_BATCH_FILENAME = "SAPPBAT";
	public final static String RPT_6001_ONLN_FILENAME = "SAPPONLN";
	public final static String RPT_6001_REPORT_ID = "RTS.POS.6001";
	public final static String RPT_6001_BATCH_REPORT_TITLE =
		"SPECIAL PLATE APPLICATION BATCH REPORT";
	// defect 9202 
	// Remove "ONLINE" for clarity
	public final static String RPT_6001_ONLN_REPORT_TITLE =
		"SPECIAL PLATE APPLICATION REPORT";
	// end defect 9202 
	public final static String RPT_6001_RPT_ERR =
		"Exception occurred in formatReport() of "
			+ "com.txdot.isd.rts.services.reports.reports."
			+ "GenSpclPltApplReport";
	public final static String RPT_6001_SUMMARY = "SUMMARY";
	// end defect 9085 

	// defect 9831 
	public final static String RPT_8001_ONLN_FILENAME = "DPONLN";
	public final static String RPT_8001_REPORT_ID = "RTS.POS.8001";
	public final static String RPT_8001_REPORT_TITLE =
		"DISABLED PLACARD REPORT";
	public final static String RPT_8001_RPT_ERR =
		"Exception occurred in formatReport() of "
			+ "com.txdot.isd.rts.services.reports.reports."
			+ "GenDsabldPlcrdReport";
	public final static String RPT_8001_SUMMARY = "SUMMARY";
	// end defect 9831 

	// defect 10023 
	public final static String RPT_9001_FILENAME = "VOID";
	public final static String RPT_9001_REPORT_ID = "RTS.POS.9001";
	public final static String RPT_9001_REPORT_TITLE = "VOID REPORT";
	// end defect 10023 

	// defect 8524
	// Report and Receipt FormFeed
	public final static char FF = (char) 12;
	// end defect 8524

	// defect 10011 	
	public final static String RPR000_NEXT_SCREEN_PREVIOUS = "PREVIOUS";
	public final static String RPR000_NEXT_SCREEN_FINAL = "FINAL";
	public final static String RPR000_NEXT_SCREEN_CANCEL = "CANCEL";
	// end defect 10011 

	// defect 10086 
	public final static String FEES_REPORT_ID = "RTS.POS.5241";
	public final static String FEES_REPORT_TITLE = "FEES REPORT";

	public final static String INVENTORY_DETAIL_REPORT_ID =
		"RTS.POS.5161";
	public final static String INVENTORY_DETAIL_REPORT_TITLE =
		"INVENTORY DETAIL REPORT";

	public final static int RPR002_COL_RPT_DESC = 0;
	public final static int RPR002_COL_RPT_NO = 1;
	public final static int RPR002_COL_RPT_WSID = 2;
	public final static int RPR002_COL_RPT_DATE = 3;
	public final static int RPR002_COL_RPT_TIME = 4;
	public final static int RPR002_COL_SAME_OBJECT = 99;

	public final static int LINES_PER_PAGE_PORTRAIT = 77;
	public final static int LINES_PER_PAGE_LANDSCAPE = 52;

	public final static int NO_PRINT_OPTION_SELECTED = 0;
	public final static int PRINT_ALL = 1;
	public final static int PRINT_CURRENT = 2;
	public final static int PRINT_SELECTED = 3;
	public final static String EXTENSION_CERTS = ".CRT";
	// end defect 10086 
	// defect 8628 
	public final static boolean CLIENT = true;
	public final static boolean SERVER = false;
	public final static boolean BATCH = true;
	public final static boolean ONLINE = false;
	public final static String SYSTEM_EMPID = "SYSTEM";
	// This "flag" is used in UtilityMethods.saveReport() to determine 
	//  if WsId in front of file name. 
	public final static int RPT_COPIES_FUNDS_FILENAME_BY_WSID = 0;
	// end defect 8628

	// defect 10250
	public final static boolean SORT_BY_ID = true;
	public final static int SORT_BY_ID_INDI = 0;
	public final static int SORT_BY_NAME_INDI = 1;
	public final static String SORT_ORDER_HDG = "SORT ORDER";
	public final static String SORT_ORDER_ID = "ID";
	public final static String SORT_ORDER_NAME = "NAME";
	public final static String ADMIN_LOG_SORT_ORDER_ID = "Id";
	public final static String ADMIN_LOG_SORT_ORDER_NAME = "Name";
	// end defect 10250
	
	// defect 10590 
	public final static String PREVIEW_RCPT_NAME = "D:\\RTS\\RTSAPPL\\PREVIEW.RCP";
	// end defect 10590
	
	// defect 10701   
    public final static String RPT_EXTENSION = ".RPT"; 
    // end defect 10701 
}
