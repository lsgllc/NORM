package com.txdot.isd.rts.services.webapps.util.constants;

import com.txdot.isd.rts.services.util.RTSDate;

/*
 *
 * RegRenProcessingConstants.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford		03/31/2003	add constants for special plate address 
 * 							change report
 * 							defect 4885, PCR24 
 * B Brown  	08/20/2003  Changed constants SPCL_PLTADDR_CHG_HDR to 
 * 							"SPECIAL PLATES BRANCH" and S
 * 							PCL_PLTADDR_CHG_TITLE to 
 * 							"SPECIAL PLATE ADDRESS CHANGE REPORT".
 * 							defect 4885, PCR24
 * B Brown		10/31/2005	Get code to standard. 
 *							defect 7889 Ver 5.2.3   
 * K Harrell	02/09/2009	Modify as Special Plates Address Change 
 * 							 Report no longer required. 
 * 							delete GET_SPCL_PLT_ADDR_CHG_RPT,
 * 							 GET_CNTY_NAME,SPCL_PLTADDR_CHG_HDR,
 * 							 SPCL_PLTADDR_CHG_TITLE,SPCL_PLTADDR_CHG_RPT,
 * 							 SPCL_PLTADDR_CUTOFF_TIME,
 * 							 SPCL_PLTADDR_SUBSTATION_NAME,
 * 							 SPCL_PLTADDR_WORKSTATION_ID,
 * 							 SPCL_PLTADDR_REQUESTED_BY
 * 							defect 9941 Ver Defect_POS_D
 * K Harrell	02/09/2009	Add constants for Internet Deposit 
 * 							Reconciliation Report.
 * 							add GET_DEPOSIT_RECON_RPT, VISA_ABBR,
 * 							  AMERICANEXPRESS_ABBR, DISCOVER_ABBR, 
 * 							 MASTERCARD_ABBR
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	02/19/2009	Add constant for minimum date for 
 * 							Internet Deposit Reconciliation Report 
 * 							add MIN_DEPOSIT_RECON_RPT_DATE
 * 							defect 9935 Ver Defect_POS_D 
 * K Harrell	04/08/2009	add MINDATE, MAXDATE
 * 							defect 10027 Ver Defect_POS_E
 * K Harrell	06/19/2009	delete unused constants 950 - 973, 
 * 							 975 - 978, 980 - 982
 * 							delete RPT_VERSIONS, ADDRCHG_HDR, 
 * 							 ADDRCHG_TITLE, ADDR_CHANGE_RPT, ITRANS_HDR,
 * 							 ITRANS_TITLE, ITRANS_RPT,
 *							 VENDOR_TRANS_HDR, VENDOR_TRANS_TITLE,
 *							 VENDOR_TRANS_RPT, VENDOR_RPT_CUTOFF_TIME,
 *							 ITRANS_RPT_CUTOFF_TIME  
 * 							defect 10023 Ver Defect_POS_F
 * K Harrell	02/27/2010 	Removing Internet Address Change Report
 * 							delete GET_ADDR_CHANGE_RPT, 
 * 								TXDOT_CIS_OFCISSUANCENO
 * 							defect 10387 Ver POS_640   
 * K Harrell	03/02/2010	Remove constants duplicated in CommonConstant
 * 							 or no longer used in REG103.  
 * 							delete MAX_ADDR, MAX_CITY, MAX_PHONE,
 * 							 MAX_ZIP,MAX_ZIP_EXT, MIN_FNAME, MIN_LNAME,
 * 							 MIN_ADDR,MIN_CITY, MIN_PHONE, MIN_ZIP,
 * 							 MIN_ZIP_EXT
 * 							defect 10372 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * RegRenProcessingConstants standardizes messages and processes
 * 
 * @version	POS_640			03/02/2010
 * @author	George Donoso
 * <br>Creation Date:		01/09/2002 12:21:22
 */

public class RegRenProcessingConstants
{
	//function ids
	public final static int NO_DATA = 0;
	public final static int GET_PROC_VEHICLE = 1;
	public final static int PROC_REG_RENEWAL = 2;
	public final static int PROC_REG_RENEWAL_ADDR = 3;
	public final static int GET_NEXT_NEW = 4;
	public final static int GET_NEXT_HOLD = 5;
	public final static int VEH_CHECKOUT = 6;
	public final static int GET_NEXT_ANY = 7;

	// defect 10387 
	// public final static int GET_ADDR_CHANGE_RPT = 8;
	// TxDOT Customer Information Service OfcIssuanceno 
	// public final static int TXDOT_CIS_OFCISSUANCENO = 290;
	// end defect 10387 

	public final static int GET_TRANS_RPT = 9;
	public final static int VIEW_ADDR = 10;
	public final static int VIEW_INS = 11;
	public final static int GET_TX = 12;
	public final static int SET_TX_INFO = 14;
	public final static int GET_VENDOR_RPT = 15;
	public final static int VIEW_VENDOR_RPT = 16;
	public final static int GET_RENEWAL_COUNT = 17;
	public final static int PROC_REFUND_RESULT = 18;
	public final static int UNDO_CHECKOUT = 19;
	// defect 9935 
	public final static int GET_DEPOSIT_RECON_RPT = 20;
	public final static int MIN_DEPOSIT_RECON_RPT_DATE = 20090114;
	// end defect 9935 

	// defect 10011 
	public final static int MIN_DEPOSIT_RECON_RPT_AMDATE =
		new RTSDate(
			RTSDate.YYYYMMDD,
			RegRenProcessingConstants.MIN_DEPOSIT_RECON_RPT_DATE)
			.getAMDate();
	// end defect 10011 

	public final static String OBTAIN_NEXT_974 =
		"OBTAIN NEXT RECORD FOR PROCESSING?";
	public final static String ERR_MODIFIED_REC_979 =
		"THIS RECORD HAS BEEN MODIFIED, IGNORE CHANGES?";

	// Reason codes
	public final static String RSN_ADDR = "1";
	public final static String RSN_INS = "2";
	public final static String RSN_CNTY = "3";
	public final static String RSN_OTHER = "10";

	//field lengths
	// (1) Maximum
	public final static int MAX_FNAME = 15;
	public final static int MAX_LNAME = 30;
	public final static int MAX_PLATENO = 7;
	public final static int MAX_COMMENTS = 200;
	public final static int MAX_TRANSID = 17;

	// defect 10372 
	// public final static int MAX_ADDR = 30;
	// public final static int MAX_CITY = 19;
	// public final static int MAX_PHONE = 12;
	// public final static int MAX_ZIP = 5;
	// public final static int MAX_ZIP_EXT = 4;
	// public final static int MIN_FNAME = 1;
	// public final static int MIN_LNAME = 0;
	// public final static int MIN_ADDR = 3;
	// public final static int MIN_CITY = 2;
	// public final static int MIN_PHONE = 7;
	// public final static int MIN_ZIP = 5;
	// public final static int MIN_ZIP_EXT = 3;
	// end defect 10372 

	//Status descriptors
	public final static String UNPAID_LBL = "Unpaid";
	public final static String NEW_LBL = "New";
	public final static String HOLD_LBL = "Hold";
	public final static String DECL_PENDING_LBL =
		"Declined(Charge-back Pending)";
	public final static String APPROVED_LBL = "Approved";
	public final static String DECL_FAILED_LBL =
		"Declined(Charge-back Failed)";
	public final static String DECL_ALL_LBL = "Declined(All)";
	public final static String DECL_SUCCESS_LBL =
		"Declined(Charge-back Successful)";
	public final static String IN_PROC_LBL = "In Process";

	// report type
	public final static int IADDR = 0;
	public final static int ITRANS = 1;
	public final static int VENDOR = 2;

	// defect 10027 
	public final static String MINDATE = "MinDate";
	public final static String MAXDATE = "MaxDate";
	// end defect 10027 

	// defect 9935 
	public final static String AMERICANEXPRESS_ABBR = "AMEX";
	public final static String DISCOVER_ABBR = "DISC";
	public final static String MASTERCARD_ABBR = "MAST";
	public final static String VISA_ABBR = "VISA";
	// end defect 9935  

	// defect 10023 
	// Removed Error Constants 950 - 973, 975 - 978, 980 - 982 
	// (1) Error messages, numbered. 
	//public final static String ERR_SEARCH_KEYS_950 =
	//	"ENTER SEARCH PARAMETERS";
	//		public final static String ERR_PLATE_951 =
	//			"ENTER PLATE NO. IN CORRECT FORMAT";
	//public final static String ERR_UTI_952 =
	//	"ENTER INTERNET TRACE NUMBER IN CORRECT FORMAT";
	//public final static String ERR_BEGIN_DATE_REQD_953 =
	//	"ENTER BEGIN DATE";
	//public final static String ERR_BEGIN_DATE_954 =
	//	"ENTER BEGIN DATE IN CORRECT FORMAT";
	//public final static String ERR_END_DATE_955 =
	//	"ENTER END DATE IN CORRECT FORMAT";
	//public final static String ERR_BEGIN_DATE_TOO_OLD_956 =
	//	"BEGIN DATE MORE THAN 30 DAYS IN THE PAST";
	//public final static String ERR_TRANSID_957 =
	//	"ENTER TRANSACTION ID IN CORRECT FORMAT";
	//public final static String ERR_VENDOR_DATE_RANGE_958 =
	//	"THE RANGE BETWEEN FROM AND TO IS TOO LARGE";
	//public final static String ERR_DATE_RANGE_959 =
	//	"DATE MUST BE WITHIN 30 DAYS OF THE CURRENT DATE";
	//public final static String ERR_NO_RECORD_SELECTION_960 =
	//	"NO RECORD SELECTED";
	//public final static String ERR_EMPL_ID_961 =
	//	"EMPLOYEE ID NOT DEFINED (CHECK PROPERTIES FILE)";
	//public final static String ERR_CHECKED_OUT_962 =
	//	"THIS RECORD IS CURRENTLY CHECKED OUT BY SOMEONE ELSE";
	//public final static String MSG_HOLD_963 =
	//	"YOU MUST ENTER A HOLD REASON BEFORE CONTINUING";
	//	public final static String MSG_DECLINE_964 =
	//		"YOU MUST ENTER A DECLINE REASON BEFORE CONTINUING";
	//	public final static String MSG_COMMENTS_HOLD_965 =
	//		"YOU MUST ENTER A COMMENT FOR THE HOLD BEFORE CONTINUING";
	//	public final static String MSG_COMMENTS_DECLINE_966 =
	//		"YOU MUST ENTER A COMMENT FOR THE DECLINE BEFORE CONTINUING";
	//	public final static String ERR_ACTION_CMD_967 =
	//		"NO ACTION COMMAND SPECIFIED";
	//	public final static String ERR_ACCESS_DENIED_968 =
	//		"ACCESS DENIED - CHECK WITH YOUR SYSTEM ADMINSTRATOR";
	//	public final static String ERR_NO_RPT_SELECTION_969 =
	//		"NO REPORT SELECTED";
	//	public final static String ERR_REC_NOT_FOUND_970 =
	//		"NO RECORD FOUND";
	//	public final static String ERR_TX_NOT_FOUND_971 =
	//		"TRANSACTION NOT FOUND";
	//	public final static String ERR_CONN_972 =
	//		"A CONNECTION FAILURE HAS OCCURRED";
	//	public final static String ERR_EXC_973 = "AN ERROR HAS OCCURRED";
	//	public final static String ERR_NEXT_975 =
	//		"NO TRANSACTIONS AVAILABLE IN QUEUE FOR PROCESSING";
	//	public final static String ERR_NEXT_NEW_976 =
	//		"NO TRANSACTION STATUS NEW IN QUEUE. TRANSACTION STATUS HOLD DISPLAYED FOR PROCESSING";
	//	public final static String ERR_NEXT_HOLD_977 =
	//		"NO TRANSACTION STATUS HOLD IN QUEUE. TRANSACTION STATUS NEW DISPLAYED FOR PROCESSING";
	//	public final static String ERR_DB_SUCCESS_978 =
	//		"THIS RECORD HAS BEEN UPDATED";
	//	public final static String ERR_INVALID_DATE_980 =
	//		"INVALID FROM DATE OR TO DATE";
	//	public final static String ERR_INVALID_DATE_981 =
	//		"INVALID BEGIN DATE OR END DATE";
	//	public final static String ERR_BEGIN_DATE_982 =
	//		"BEGIN DATE MORE THAN 60 DAYS IN THE PAST";

	//  public final static int RPT_VERSIONS = 7;
	//	public final static String ADDRCHG_HDR =
	//		"RENEWAL RECIPIENT ADDRESS CHANGE REPORT";
	//	public final static String ADDRCHG_TITLE = "ADDRESS CHANGE REPORT";
	//	public final static String ADDR_CHANGE_RPT = "RTS.WBA.2001";
	//
	//	public final static String ITRANS_HDR =
	//		"INTERNET TRANSACTION RECONCILIATION DETAIL REPORT";
	//	public final static String ITRANS_TITLE =
	//		"INTERNET TRANSACTION REPORT";
	//	public final static String ITRANS_RPT = "RTS.POS.2002";

	// Moved to Reports Constant 
	//	public final static String VENDOR_TRANS_HDR =
	//		"VENDOR PAYMENT REPORT";
	//	public final static String VENDOR_TRANS_TITLE =
	//		"VENDOR PAYMENT REPORT";
	//	public final static String VENDOR_TRANS_RPT = "RTS.WBG.2003";

	//	// Vendor report time ==> eastern time
	//	public final static String VENDOR_RPT_CUTOFF_TIME = "02:00:00";
	//	// TxDOT server time  ==> central time
	//	public final static String ITRANS_RPT_CUTOFF_TIME = "01:00:00";
	//	end defect 10023
}
