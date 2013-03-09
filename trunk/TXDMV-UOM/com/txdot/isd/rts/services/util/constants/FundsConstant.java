package com.txdot.isd.rts.services.util.constants;

/*
 *
 * FundsConstant.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/13/2004	Added constants for Fee Source Cds
 *							defect 7620  Ver 5.2.1
 * K Harrell	06/12/2005	deleted unused constants
 * 							modified value for RTS_MIN_DATE 
 * 										Ver 5.2.3
 * K Harrell	11/08/2005	Use Closeout vs. Close Out when referencing
 * 							a report. Set-Aside vs. Set Aside.
 * 							defect 8379 Ver 5.2.3    
 * K Harrell	06/08/2009	add REPORT_GENERATED, NO_TRANSACTIONS,
 * 							FUN001_CASH_DRAWER, FUN001_LAST_CLOSEOUT,
 * 							FUN001_LAST_CURRENT_STATUS,
 * 							FUN007B_ID, FUN007B_LAST_CLOSEOUT,
 * 							FUN007B_LAST_CURRENT_STATUS, FUN002_ID,
 * 							FUN002_LAST_CLOSEOUT,FUN008_CASH_DRAWER,
 * 							FUN008_REQUESTED_WORKSTATION, 
 * 							FUN010_CASH_DRAWER,
 * 							FUN010_CLOSEOUT_END_DATE_TIME, FUN014_ID,
 * 							FUN013B_CASH_DRAWER, FUN013B_REPORT_STATUS, 
 *							FUN013C_EMPLOYEE_ID, FUN013C_REPORT_STATUS,
 * 							FUN014_LAST_CLOSEOUT_REQ_TIME, 
 * 							FUN011_EMPLOYEE_ID, FUN011_LAST_NAME, 
 * 							FUN011_FIRST_NAME, FEE_SOURCE_DO_NOT_SHOW
 * 							defect 9943 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * Constants for Funds Module
 *
 * @version	Defect_POS_F	06/08/2009 
 * @author	Bobby Tulsiani
 * <br>Creation Date:		08/27/2001
 */

public class FundsConstant
{
	public final static int NO_DATA_TO_BUSINESS =
		GeneralConstant.NO_DATA_TO_BUSINESS;
	public final static int GET_ALL_CASH_DRAWERS = 12;
	public final static int GET_CASH_DRAWER = 13;
	public final static int GET_EMPLOYEE_DATA = 14;
	public final static int GET_CASH_DRAWERS_RESET = 15;
	public final static int RESET_DRAWERS = 16;
	public final static int GET_CASH_DRAWERS_SUBSTATION = 17;
	public final static int UPD_CURRENT_STAT = 18;
	public final static int CLEAR_DESKTOP = 19;

	public static final String RTS_MIN_DATE =
		"2000-01-01 00:00:00.000000";

	public final static int GENERATE_PAYMENT_REPORT = 21;
	public final static int GENERATE_CLOSEOUT_STATISTICS_REPORT = 27;
	public final static int GENERATE_COUNTYWIDE_REPORT = 28;
	public final static int GENERATE_SUBSTATION_SUMMARY_REPORT = 29;

	public final static int DISPLAY_REPORTS = 200;
	public final static int PRINT_REPORTS = 300;

	// FundsReportData Constants 
	public final static int CASH_DRAWER = 50;
	public final static int EMPLOYEE = 51;
	public final static int SUBSTATION = 52;
	public final static int NONE = 53;

	public final static int LAST_CLOSE = 54;
	public final static int AFTER_SUBSTATION = 55;
	public final static int SINCE_CURRENT = 56;
	public final static int SINCE_CLOSE = 57;
	public final static int DATE_RANGE = 58;

	public final static int CLOSE_OUT_FOR_DAY = 110;
	public final static int CURRENT_STATUS = 120;

	public final static int COUNTYWIDE = 59;

	// Entity + primary split constants
	public final static int CASH_DRAWER_NONE = 0;
	public final static int CASH_DRAWER_EMP = 1;
	public final static int EMP_NONE = 2;
	public final static int EMP_CASH_DRAWER = 3;

	// Funds Report Names
	public final static String PAYMENT_REPORT = "PAYMENT REPORT";
	public final static String TRANSACTION_REPORT =
		"TRANSACTION RECONCILIATION REPORT";
	public final static String FEES_REPORT = "FEES REPORT";
	public final static String INVENTORYD_REPORT =
		"INVENTORY DETAIL REPORT";
	public final static String INVENTORYS_REPORT =
		"INVENTORY SUMMARY REPORT";

	// Closeout Status Remarks  
	public final static String CLOSE_COMPLETE = "Closeout Complete.";
	public final static String NO_COMPL_TRANSACTIONS =
		"No Complete Transactions Since Last Closeout.";
	public final static String SET_ASIDE =
		"A Set-Aside Transaction(s) Exists.";
	public final static String INDI_SET =
		"Closeout Indicator Already Set.";

	// defect 9943	
	public final static String REPORT_GENERATED = "Report Generated";
	public final static String NO_TRANSACTIONS = "No Transactions";
	// defect 9943 

	//Transaction Constants
	public final static int GET_FUND_FUNC_TRANS_AND_TR_FDS_DETAIL = 80;

	//Fee source constants
	// defect 9943 
	public final static int FEE_SOURCE_DO_NOT_SHOW = 0;
	// end defect 9943 
	//defect 7620		
	public final static int FEE_SOURCE_CUST = 1;
	public final static int FEE_SOURCE_SUBCON = 2;
	public final static int FEE_SOURCE_DEALER = 3;
	public final static int FEE_SOURCE_INTERNET = 5;
	//end defect 7620 

	// Cash Drawer Indicators 
	public final static int CASH_DRAWER_INDI = 1;
	public final static int NON_CASH_DRAWER_INDI = 0;

	//Constants for Table Models
	// defect 9943 
	public final static int FUN001_CASH_DRAWER = 0;
	public final static int FUN001_LAST_CLOSEOUT = 1;
	public final static int FUN001_LAST_CURRENT_STATUS = 2;

	public final static int FUN002_ID = 0;
	public final static int FUN002_LAST_CLOSEOUT = 1;
	
	public final static int FUN007B_ID = 0;
	public final static int FUN007B_LAST_CLOSEOUT = 1;
	public final static int FUN007B_LAST_CURRENT_STATUS = 2;

	public final static int FUN008_CASH_DRAWER = 0;
	public final static int FUN008_REQUESTED_WORKSTATION = 1;

	public final static int FUN010_CASH_DRAWER = 0;
	public final static int FUN010_CLOSEOUT_END_DATE_TIME = 1;

	public final static int FUN011_EMPLOYEE_ID = 0; 
	public final static int FUN011_LAST_NAME = 1;
	public final static int FUN011_FIRST_NAME = 2;	
	
	public final static int FUN013B_CASH_DRAWER = 0; 
	public final static int FUN013B_REPORT_STATUS = 1; 

	public final static int FUN013C_EMPLOYEE_ID = 0; 
	public final static int FUN013C_REPORT_STATUS = 1; 
	
	public final static int FUN014_ID = 0;
	public final static int FUN014_LAST_CLOSEOUT_REQ_TIME = 1;
	
	public final static int FUN015_CASH_DRAWER = 0;
	public final static int FUN015_STATUS = 1;
	// end defect 9943 
}
