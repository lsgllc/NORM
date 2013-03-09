package com.txdot.isd.rts.server.systemcontrolbatch;

import java.util.Vector;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.BatchLog;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;

/*
 *
 * Purge.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/09/2002	Added Internet purging
 * Ray Rowehl	08/20/2002	Add purging of Admin tables.
 *							defect 4601
 * Ray Rowehl	08/20/2002	Use systemproperty to determine number of 
 * 							days to keep.
 *							defect 4619
 * Ray Rowehl	11/04/2002	Print more status messages
 * K Harrell  	12/11/2002  Rename deleteSubstation11days 
 *                          to purgeCloseOutHstry11Days; edited call;
 * 							defect 5161
 * K Harrell 	01/21/2003  Use deleteDays vs. RTSDate for 
 * 							deleteAdminTables
 * 							defect 5285 
 *                         	Rename the methods for consistency.
 * 							defect 5286 
 * K Harrell  	01/24/2003  No commit on Trans_Hdr
 * 							defect 5302 
 * K Harrell  	01/27/2003  Modify to use format YYYYMMDDDate() vs. 
 * 							TRANSAMDATE
 * 							defect 5306 	
 * K Harrell  	04/08/2003  Added comments for Internet Addr Change 
 *                          and Internet Trans parameters.
 * 							defect 5928 
 * K Harrell  	05/13/2003  Loop through all eligible 
 *                          transamdates for transaction purge 
 * 							standardize comments
 * 							add qryEligibleTransAMDates()
 * 							modify beginPurge()
 * 							defect 6119   
 * K Harrell  	07/24/2003  Add purge for InternetSummary table. 
 *                          modify beginPurge()
 * 							defect 6272
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add delete11DaysReprint(), delete13Months()
 * 							Ver 5.2.0
 * K Harrell	07/18/2004	Use parameters vs. hard code for purge of
 *							RSPS_PRNT, RTS_REPRNT_STKR
 *							add deleteRemotePrint(),deleteReprintSticker()
 *							deprecate delete11DaysReprint(),
 *							 delete13Months()
 *							deleted deleteLogonFunctionTransction()
 *							added deleteLogonFunctionTransaction()
 *							defect 7340  Ver 5.2.1
 * K Harrell	10/10/2004	Modified to reference RSPSPrint vs. 
 *							RemotePrint
 *							renamed deleteRemotePrint() to 
 *							deleteRSPSPrint()
 *							modify beginPurge()
 *							defect 7608  Ver 5.2.1
 * K Harrell	11/30/2004	Modified to include purge of RTS_INV_PROFILE
 * 							and RTS_CRDT_CARD_FEE
 *							modify deleteAdminTables()
 *							defect 7689  Ver 5.2.2 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7897 Ver 5.2.3
 * K Harrell	11/09/2005	Do not run purge after 7:00 a.m.
 *  						modify beginPurge() 
 *	 						defect 8386 
 * K Harrell	11/13/2005	Java 1.4 cleanup; Significant refactoring 
 * 							add caDBAccess, caPurgeDate,cbPurgeSuccessful,
 * 							 ciPurgeDays(), ciPurgeItrntTransDays, 
 * 							 ciPurgeItrntDataDays, csPurgeType  
 * 							add getPurgeAMDate(),handleRollback(),
 * 							 setPurgeDateAndWritePurgeDataToBatchLog(),
 * 							 writePurgeMonthsToBatchLog(),
 * 							 writeSeparator(),
 * 							 writeStatusToBatchLog() 
 * 							 + constants 
 * 							delete delete13Months(), delete11Days()
 * 							rename all deleteXXX to purgeXXX 
 * 							defect 8423 Ver 5.2.3  
 * K Harrell	11/13/2005  Streamline calls for Internet Purge   
 * 							add purgeIRenewItrntDataItrntTrans(), 
 * 							purgeItrntIAddrIRnrTrans(),
 * 							purgeItrntSummary()
 * 	  						modify beginPurge()
 * 							defect 8385 Ver 5.2.3
 * K Harrell	11/29/2005	moved initialization of DBAccess after 
 * 							7:00 a.m. check.
 * 							modify beginPurge() 
 * 							defect 8385 Ver 5.2.3
 * Ray Rowehl	09/11/2006	Use Batch DB UserId and Password while 
 * 							in DB Restricted mode.
 * 							add csDbPassword, csDbUserId
 * 							add Purge(String, String)
 * 							modify Purge(), beginPurge(), 
 * 								purgeAdminTables(), purgeCloseOutHstry(),
 * 								purgeInventoryHistory(), 
 * 								purgeItrntIAddrIRnrTrans(),
 * 								purgeIRenewItrntDataItrntTrans(),
 * 								purgeItrntSummary(),
 * 								purgeLogonFunctionTransaction(),
 * 								purgeReprintSticker(),
 * 								purgeRSPSPrint(), purgeSecurityLog(),
 * 								purgeSubstationSummary(), 
 * 								purgeTransactions()
 * 							defect 8923 Ver 5.2.5  
 * Ray Rowehl	10/03/2006	Migrate back to main project.
 * 							defect 8959 Ver FallAdminTables 
 * K Harrell	11/03/2006	Fixed problem with a class level variable
 * Jeff S.					redefined in a local method.  This caused
 * 							the date calculation to not be updated in 
 * 							the class level variable.
 * 							modify 
 * 							setPurgeDateAndWritePurgeDataToBatchLog()
 * 							defect 9004 Ver Fall_Admin_Tables 
 * 							defect 8959 Ver FallAdminTables
 * K Harrell	10/22/2006	Add programming for handling RTS_EXMPT_AUDIT
 * 							add EXMPT_AUDIT							
 * 							add	purgeExemptAudit()
 * 							modify beginPurge()
 * 							defect 8900 Ver Exempts  
 * Bob Brown	11/03/2006	Purge rts_itrnt_trans recs after 11 days if
 * 							cntystatuscd = 1 (unpaid) and 
 * 							itrntpymntstatuscd is null.
 * 							modify purgeIRenewItrntDataItrntTrans()
 * 							defect 9005 Ver Exempts
 * K Harrell	11/09/2006	Synchronize comments, purge dates to 
 * 							reflect maximum date to purge.
 * 							Remove assignments of DatabaseAccess
 * 							modify qryEligibleTransAMDates().
 * 							 setPurgeDateAndWritePurgeDataToBatchLog(),
 * 							 purgeLogonFunctionTransaction()
 * 							defect 9011 Ver Exempts  
 * K Harrell	02/06/2007	Add code for purge of RTS_SR_FUNC_TRANS,
 * 							RTS_SPCL_PLT_TRANS_HSTRY, RTS_INV_VIRTUAL,
 * 							RTS_SPCL_PLT_REJ_LOG 
 * 							add purgeSpclPltTransHstry(),
 * 								purgeSpclPltRejLog()
 * 							modify purgeTransactions() 
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	02/29/2007	Add purge handling for Virtual Inventory.
 * 							This process also handles releasing the 
 * 							System Holds that have expired. 
 * 							add purgeVirtualInventory()
 * 							modify beginPurge()
 * 							defect 9116 Ver Special Plates
 * Jeff S.		06/29/2007	Added Special Plate Internet Purge.
 * 							add ITRNT_SP_TRANS_INCOMP, 
 * 								ITRNT_SP_TRANS_COMP
 * 							add purgeCompSpappTransAndFees(), 
 * 								purgeIncompSpappTransAndFees()
 * 							modify beginPurge(),
 * 								private purgeItrntIAddrIRnrTrans(),
 * 								private purgeItrntSummary(),
 * 								private qryEligibleTransAMDates(),
 * 							defect 9121 Ver Special Plates
 * Ray Rowehl	07/18/2007	Make comments on VI purge more clear.
 * 							modify purgeVirtualInventory()
 * 							defect 9116 Ver Special Plates
 * K Harrell	07/17/2007	add purgeOrphanSPAPPTransFees(),
 * 							  purgeCompSPAPPTrans()
 * 							  purgeIncompSPAPPTrans() 
 * 							delete purgeIncompSPAPPTransAndFees(),
 * 							  purgeCompSPAPPTransAndFees()
 * 							modify beginPurge()
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	11/08/2007	Need to pass the shared dba to VI purge 
 * 							routines so they can run with purge id. 
 * 							modify purgeVirtualInventory()
 * 							defect 9431 Ver Special Plates
 * Ray Rowehl	11/19/2007	Using the wrong number for purge amdate
 * 							on VI purge.
 * 							Also removed a log write from transaction 
 * 							purge since we do actually purge VI there.
 * 							modify purgeTransactions(),
 * 								purgeVirtualInventory()
 * 							defect 9431 Ver Special Plates
 * K Harrell	10/27/2008	Add processing to update in process 
 * 							Disabled Placard Customer records.
 * 							add DSABLD_PLCRD_CUST, RESETINPROCESS 	
 * 							add resetInProcsDsabldPlcrd() 
 *							defect 9831 Ver Defect_POS_B
 * K Harrell	01/19/2009	Write number of rows purged to Batchlog
 * 							add NUM_ROWS_LENGTH, TABLE_NAME_LENGTH, 
 * 							  ROW, ROWS 
 * 							add ciNumRows 
 * 							add constructNumRows(), 
 * 							 writeSubTableToBatchLog() 
 * 							add writeStatusToBatchLog(String,boolean,boolean)
 * 							modify writeStatusToBatchLog(String,boolean),
 * 							 beginPurge(), purge*() 
 * 							defect 9825 Ver Defect_POS_D 
 * K Harrell	01/21/2009	Purge Web Services Tracking Tables
 * 							add SRVC_HSTRY, V21_REQUEST 
 * 							add purgeV21Request(), purgeSrvcHstry() 
 * 							modify beginPurge() 
 * 							defect 9803 Ver Defect_POS_D
 * K Harrell	01/27/2009	Purge for Disabled Placard Cust
 * 							add DSABLD_PLCRD_CUST_AND_IN_PROCS,
 * 							  DP_CUST_SET_DELETEINDI
 * 							add purgeDsabldPlcrdCust(), 
 * 							  purgeSetDelIndiDsabldPlcrdCust()
 * 							modify DSABLD_PLCRD_CUST   
 * 							modify beginPurge()
 * 							defect 9889 Ver Defect_POS_D
 * K Harrell	02/06/2009	Corrected purge method for Srvc_Hstry  
 * 							 (purge Srvc_Hstry vs. V21Request)
 * 							modify purgeSrvcHstry() 
 * 							defect 9803 Ver Defect_POS_D
 * K Harrell	06/09/2009	Purge for Internet Deposit Recon &  
 * 							Internet Deposit Recon Hstry. 
 * 							add ITRNT_DEPOSIT_RECON, 
 * 							 ITRNT_DEPOSIT_RECON_HSTRY
 * 							add purgeItrntDepositRecon(), 
 * 							   purgeItrntDepositReconHstry()
 * 							modify beginPurge() 
 * 							defect 9955 Ver Defect_POS_F
 * K Harrell	07/03/2009	Use Dealer, Lienholder vs. Dealers, 
 * 							 Lienholders
 * 							modify purgeAdminTables()
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	03/10/2010	Add Purge for ETtlHstry
 * 							add purgeETtlHstry(), ETTL_HSTRY  
 * 							modify beginPurge() 
 * 							defect 10231 Ver POS_640 
 * K Harrell	04/01/2010	Add Purge for WSStatusLog 
 * 							add purgeWorkstationStatusLog(), 
 * 							  WS_STATUS_LOG
 * 							modify beginPurge() 
 * 							defect 8087 Ver POS_640 
 * K Harrell	04/03/2010	Add Purge for ItrntTransDelLog  
 * 							add purgeItrntTransDelLog(), 
 * 							 ITRNT_TRANS_DEL_LOG
 * 							modify beginPurge() 
 * 							defect 10421 Ver POS_640 
 * K Harrell	05/25/2010	Add Purge for PrmtTrans
 * 							add PRMT_TRANS  
 * 							modify purgeTransactions() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/31/2010	Add Purge for MFReq, MFReqHstry 
 * 							add MF_REQ, MF_REQ_HSTRY
 * 							add purgeMFReq(), purgeMFReqHstry()
 * 							modify beginPurge()
 * 							defect 10462 Ver 6.5.0  
 * K Harrell	12/27/2010	Add Purge for SpclPltPrmt 
 * 							add SPCLPLTPRMT
 * 							add purgeSpclPltPrmt() 
 * 							modify beginPurge() 
 * 							defect 10700 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * Run the various purges needed on the database.
 * 
 * @version	6.7.0 				12/27/2010 
 * @author  Kathy Harrell
 * @author 	Michael Abernethy
 * <br>Creation Date:			09/06/2001 
 */

public class Purge
{
	// Object
	private DatabaseAccess caDBAccess = null;
	private RTSDate caPurgeDate = new RTSDate();

	// boolean 
	private boolean cbPurgeSuccessful = true;

	// int
	private int ciNumRows = 0;
	private int ciPurgeDays = 0;
	private int ciPurgeItrntDataDays = SystemProperty.getPurgeTrans();
	private int ciPurgeItrntTransDays =
		SystemProperty.getPurgeInetRenewal();

	// String 
	private String csDbPassword;
	private String csDbUserId;
	private String csPurgeType = "";

	// Constants 
	private static final String ADMIN_LOG = "Admin Log ";
	private static final String ADMIN_TABLE = "Admin Table ";
	private static final String APPROVED_ITRNT_RCD =
		"Approved Itrnt Record ";
	private static final String ASSGND_WS_IDS = "AssgndWorkstationIds ";
	private static final String BEGIN = "begin ";
	private static final String CASH_WS_IDS = "CashWorkstationsIds ";
	private static final String CLOSE_OUT_HSTRY = "CloseOutHstry ";
	private static final String CRDT_CARD_FEE = "Credit Card Fee ";
	private static final String DAYS = " days ";
	private static final String DEALERS = "Dealers ";
	private static final String DECLINED_REFUND_APPROVED_ITRNT =
		"Declined Refund Apprvd Itrnt Rcd ";
	private static final String DP_CUST_SET_DELETEINDI =
		"DPCust Set DeleteIndi ";
	private static final String DSABLD_PLCRD_CUST =
		"Disabled Placard Customer ";
	private static final String DSABLD_PLCRD_CUST_AND_IN_PROCS =
		"DP Cust & Cust In Procs ";
	private static final String EXMPT_AUDIT = "Exempt Audit ";
	// defect 10231 
	private static final String ETTL_HSTRY = "ETtlHstry ";
	// end defect 10231 
	private static final String FAILED = "failed ";
	private static final String FOR = "for ";
	private static final String FUND_FUNC_TRANS = "FundFuncTrans ";
	private static final String INV_FUNC_TRANS = "InvFuncTrans ";
	private static final String INV_HSTRY = "Inventory History ";
	private static final String INV_PROFILE = "Inventory Profile ";
	private static final String INV_VIRTUAL = "InvVirtual ";
	private static final String ITRNT_ADDR_CHG_TRANS =
		"Itrnt Addr Chng Transaction ";
	private static final String ITRNT_DATA = "Itrnt Data ";

	// defect 9955 
	private static final String ITRNT_DEPOSIT_RECON =
		"Itrnt Deposit Recon ";
	private static final String ITRNT_DEPOSIT_RECON_HSTRY =
		"Itrnt Deposit Recon Hstry ";
	// end defect 9955

	private static final String ITRNT_RENEWAL_TRANS =
		"Itrnt Renewal Transaction ";
	private static final String ITRNT_SP_TRANS_COMP =
		"Itrnt Spcl Plt Trans Complete ";
	private static final String ITRNT_SP_TRANS_FEES_ORPHAN =
		"Itrnt Spcl Plt Appl Fees ";
	private static final String ITRNT_SP_TRANS_INCOMP =
		"Itrnt Spcl Plt Trans Incomplete ";
	private static final String ITRNT_SUMMARY = "Itrnt Summary ";
	private static final String ITRNT_TRANS = "Itrnt Trans ";
	private static final String LIENHOLDERS = "Lienholders ";
	private static final String LOG_FUNC_TRANS = "LogFunc Trans ";
	private static final String MONTHS = " months ";

	// defect 10462 
	private static final String MF_REQ = "MFReq ";
	private static final String MF_REQ_HSTRY = "MFReqHstry ";
	// end defect 10462 

	private static final String MV_FUNC_TRANS = "MVFuncTrans ";

	// defect 10491 
	private static final String PRMT_TRANS = "PrmtTrans ";
	// end defect 10491 

	private static int NUM_ROWS_LENGTH = 7;
	private static final String PURGE = "Purge ";
	private static final String PURGE_DATE_IS = "Purge Date is ";
	private static final String PURGE_FAILED_AFTER_7 =
		"Purge failed - start request after 7:00 am";
	private static boolean PURGE_FAILURE = false;
	private static boolean PURGE_SUCCESS = true;
	private static final String PURGE_YYYYMM_IS_LT =
		"Purge where YYYYMM is less than ";
	private static final String PYMNT_ACCT = "Payment Account ";
	private static final String REPRNT_STKR = "Reprint Sticker ";
	private static final String RESETINPROCESS = "Reset InProcs ";
	private static final String ROW = "row";
	private static final String ROWS = "rows";
	private static final String RSPS_PRNT = "RSPS Print ";
	private static final String SEC_LOG = "Security Log ";
	private static final String SECURITY = "Security ";
	private static final String SEPARATOR =
		"---------------------------";
	private static final String SPACE = " ";
	private static final String SPCLPLTREJLOG = "SpclPltRejLog ";
	// defect 10700 
	private static final String SPCLPLTPRMT = "SpclPltPrmt ";
	// end defect 10700 
	private static final String SPCLPLTTRANSHSTRY =
		"SpclPltTransHstry ";
	private static final String SR_FUNC_TRANS = "SRFuncTrans ";
	private static final String SRVC_HSTRY = "Srvc Hstry ";
	private static final String SUBCON = "Subcontractor ";
	private static final String SUBSTA = "Substa ";
	private static final String SUBSTA_SUMMARY = "Substation Summary ";
	private static final String SUCCESSFUL = "successful";
	private static int TABLE_NAME_LENGTH = 50;
	private static final String TR_FDS_DETAIL = "TrFdsDetail ";
	private static final String TR_INV_DETAIL = "TrInvDetail ";
	private static final String TRANS = "Trans ";
	private static final String TRANS_HDR = "TransHeader ";
	private static final String TRANS_PAYMENT = "TransPayment ";
	private static final String TRANSACTION = "Transaction ";
	private static final String TRANSAMDATES_SEL_FOR_PURGE =
		" TransAMDate(s) selected for Purge";
	private static final String UNPAID_ITRNT_RCD =
		"Unpaid Itrnt Record ";
	private static final String V21_REQUEST = "V21 Request ";

	// defect 8087 
	private static final String WKS_STATUS_LOG = "WksStatusLog";
	// end defect 8087 

	// defect 10421 
	private static final String ITRNT_TRANS_DEL_LOG =
		"ItrntTransDelLog";
	// end defect 10421 

	/**
	 * Purge.java Constructor 
	 */
	public Purge()
	{
		// defect 8923
		this(
			SystemProperty.getDBUser(),
			SystemProperty.getDBPassword());
		// end defect 8923
	}

	/**
	 * Purge.java Constructor
	 * 
	 * @param asUser
	 * @param asPassword
	 */
	public Purge(String asUser, String asPassword)
	{
		super();
		csDbUserId = asUser;
		csDbPassword = asPassword;
	}

	/**
	 * Runs the purge process.
	 * 
	 * @return boolean
	 */
	public boolean beginPurge()
	{
		// do not run purge after 7:00
		int liCurrentTime = new RTSDate().get24HrTime();
		if (liCurrentTime > 70000)
		{
			BatchLog.write(PURGE_FAILED_AFTER_7);
			return PURGE_FAILURE;
		}

		// defect 8923
		caDBAccess = new DatabaseAccess(csDbUserId, csDbPassword);
		// end defect 8923

		// Initialize status of Purge, i.e. Successful 
		boolean lbSuccessful = PURGE_SUCCESS;
		writeSeparator();
		// Purge Transaction Purge
		csPurgeType = TRANSACTION + PURGE;
		BatchLog.write(csPurgeType + BEGIN);

		ciPurgeDays = SystemProperty.getPurgeTrans();
		setPurgeDateAndWritePurgeDataToBatchLog();

		// Determine eligible purge dates 
		Vector lvTransAMDates = (Vector) qryEligibleTransAMDates();
		writeSeparator();

		// For every eligible purge date 
		if (lvTransAMDates != null && lvTransAMDates.size() != 0)
		{
			for (int i = 0;
				(i < lvTransAMDates.size() && cbPurgeSuccessful);
				i++)
			{
				// Vector of Integers returned w/ TransAMDates
				int liPurgeAMDate =
					((Integer) lvTransAMDates.get(i)).intValue();

				RTSDate laRTSPurgeDate =
					new RTSDate(RTSDate.AMDATE, liPurgeAMDate);
				BatchLog.write(
					csPurgeType
						+ FOR
						+ liPurgeAMDate
						+ SPACE
						+ "("
						+ laRTSPurgeDate.toString()
						+ ")"
						+ SPACE
						+ BEGIN);

				lbSuccessful = purgeTransactions(liPurgeAMDate);

				writeStatusToBatchLog(
					csPurgeType + FOR + liPurgeAMDate + SPACE,
					lbSuccessful,
					false);
				writeSeparator();
			}
		}

		// Purge Security Log
		csPurgeType = SEC_LOG + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeSecurityLog();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge LogFuncTrans Posted
		csPurgeType = LOG_FUNC_TRANS + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeLogonFunctionTransaction();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		//	Purge ExmptAudit
		csPurgeType = EXMPT_AUDIT + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeExemptAudit();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// defect 9085
		// Purge Special Plates Trans History
		csPurgeType = SPCLPLTTRANSHSTRY + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeSpclPltTransHstry();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge Special Plates Rejection Log
		csPurgeType = SPCLPLTREJLOG + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeSpclPltRejLog();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 9085 

		// defect 10700 
		csPurgeType = SPCLPLTPRMT + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeSpclPltPrmt();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 10700 

		// defect 9116
		// purge of Virtual Inventory.
		csPurgeType = INV_VIRTUAL + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeVirtualInventory();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 9116

		// defect 9803
		// purge of V21 Tracking Table
		csPurgeType = V21_REQUEST + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeV21Request();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// purge of Srvc_Hstry  (Web Services)
		csPurgeType = SRVC_HSTRY + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeSrvcHstry();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 9803

		// Purge Inventory History
		csPurgeType = INV_HSTRY + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeInventoryHistory();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge Substation Summary
		csPurgeType = SUBSTA_SUMMARY + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeSubstationSummary();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge Admin Log
		csPurgeType = ADMIN_LOG + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeAdministrationLog();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge Admin tables where deleteindi is set
		csPurgeType = ADMIN_TABLE + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeAdminTables();
		writeStatusToBatchLog(csPurgeType, lbSuccessful, false);
		writeSeparator();

		// Purge Reprint Sticker
		csPurgeType = REPRNT_STKR + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeReprintSticker();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge RSPS Print 
		csPurgeType = RSPS_PRNT + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeRSPSPrint();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge CloseOutHstry
		csPurgeType = CLOSE_OUT_HSTRY + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeCloseOutHstry();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// defect 9955
		// Purge Internet Deposit Recon 
		csPurgeType = ITRNT_DEPOSIT_RECON + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeItrntDepositRecon();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge Internet Deposit Recon Hstry 
		csPurgeType = ITRNT_DEPOSIT_RECON_HSTRY + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeItrntDepositReconHstry();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 9955 

		// Purge Internet Address Change 
		csPurgeType = ITRNT_ADDR_CHG_TRANS + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeItrntIAddrIRnrTrans();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge Internet Renewal Transactions 
		csPurgeType = ITRNT_RENEWAL_TRANS + PURGE;
		BatchLog.write(csPurgeType + BEGIN);

		// ITRNT_TRANS will be purged after SystemProperty.getPurgeInetRenewal()
		csPurgeType = ITRNT_TRANS + PURGE;
		ciPurgeDays = ciPurgeItrntTransDays;
		setPurgeDateAndWritePurgeDataToBatchLog();

		// ITRNT_DATA will be purged after SystemProperty.getPurgeTrans()
		csPurgeType = ITRNT_DATA + PURGE;
		ciPurgeDays = ciPurgeItrntDataDays;
		setPurgeDateAndWritePurgeDataToBatchLog();

		writeSeparator();

		// UnPaid Internet Record Purge 	
		csPurgeType = UNPAID_ITRNT_RCD + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful =
			purgeIRenewItrntDataItrntTrans(CommonConstants.UNPAID);
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Approved Internet Record Purge
		csPurgeType = APPROVED_ITRNT_RCD + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful =
			purgeIRenewItrntDataItrntTrans(CommonConstants.APPROVED);
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Declined Refund Approved Internet Record Purge 
		csPurgeType = DECLINED_REFUND_APPROVED_ITRNT + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful =
			purgeIRenewItrntDataItrntTrans(
				CommonConstants.DECLINED_REFUND_APPROVED);
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// defect 10421 
		// Purge Internet Trans Del Log Rows 
		csPurgeType = ITRNT_TRANS_DEL_LOG + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeItrntTransDelLog();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 10421  

		// Internet Summary Purge 
		csPurgeType = ITRNT_SUMMARY + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeItrntSummary();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// defect 9121 / 9085 
		// Complete Internet Special Plate Trans Purge 
		csPurgeType = ITRNT_SP_TRANS_COMP + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		//lbSuccessful = purgeCompSPAPPTransAndFees();
		lbSuccessful = purgeCompSPAPPTrans();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Incomplete Internet Special Plate Trans Purge 
		csPurgeType = ITRNT_SP_TRANS_INCOMP + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		//lbSuccessful = purgeIncompSPAPPTransAndFees();
		lbSuccessful = purgeIncompSPAPPTrans();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Orphan Fees 
		csPurgeType = ITRNT_SP_TRANS_FEES_ORPHAN + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeOrphanSPAPPTransFees();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 9121 / 9085 

		// defect 9831 
		// In Process Disabled Placard
		// defect 9889 
		// Use DSABLD_PLCRD_CUST_AND_IN_PROCS vs. DSABLD_PLCRD_CUST   
		csPurgeType = RESETINPROCESS + DSABLD_PLCRD_CUST_AND_IN_PROCS;
		// end defect 9889 
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = resetInProcsDsabldPlcrd();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 9831

		// defect 9889
		// Set DeleteIndi for Disabled Placard Customer
		csPurgeType = DP_CUST_SET_DELETEINDI + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeSetDelIndiDsabldPlcrdCust();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge Disabled Placard 
		csPurgeType = DSABLD_PLCRD_CUST + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeDsabldPlcrdCust();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 9889 

		// defect 10231 
		// Purge ETtlHstry Rows 
		csPurgeType = ETTL_HSTRY + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeETtlHstry();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 10231 

		// defect 8087 
		// Purge Workstation Status Log Rows 
		csPurgeType = WKS_STATUS_LOG + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeWorkstationStatusLog();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 8087  

		// defect 10462 
		// Purge Mainframe Request History Rows 
		csPurgeType = MF_REQ_HSTRY + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeMFReqHstry();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();

		// Purge Mainframe Request Rows
		//  1) Copy rows from prior day to MF_REQ_HSTRY
		//  2) Delete prior day from MF_REQ
		csPurgeType = MF_REQ + PURGE;
		BatchLog.write(csPurgeType + BEGIN);
		lbSuccessful = purgeMFReq();
		writeStatusToBatchLog(csPurgeType, lbSuccessful);
		writeSeparator();
		// end defect 10462 

		caDBAccess = null;
		return cbPurgeSuccessful;
	}

	/**
	 * Return string with number of rows  return 
	 */
	private String constructNumRows()
	{
		String lsRows = ciNumRows == 1 ? ROW : ROWS;

		return UtilityMethods.addPadding(
			"" + ciNumRows,
			NUM_ROWS_LENGTH,
			SPACE)
			+ SPACE
			+ lsRows;
	}
	/**
	 * Issue the rollback for the unit of work. 
	 * Assign false to cbPurgeSuccess.  
	 */
	private void handleRollback()
	{
		try
		{
			caDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
		}
		catch (RTSException aeRTSEx)
		{
			// empty code block
		}
		cbPurgeSuccessful = PURGE_FAILURE;
	}

	/**
	 * Purge old AdminLog Rows.
	 * 
	 * @return boolean
	 */
	private boolean purgeAdministrationLog()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeAdminLog();
			setPurgeDateAndWritePurgeDataToBatchLog();

			AdministrationLog laAdminLog =
				new AdministrationLog(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laAdminLog.purgeAdministrationLog(
					caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge old Admin Tables rows.
	 */
	private boolean purgeAdminTables()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeAdminTables();
			setPurgeDateAndWritePurgeDataToBatchLog();

			caDBAccess.beginTransaction();

			// purge Assgnd_Ws_ids
			AssignedWorkstationIds laAssgndwsids =
				new AssignedWorkstationIds(caDBAccess);
			ciNumRows =
				laAssgndwsids.purgeAssignedWorkstationIds(ciPurgeDays);
			writeSubTableToBatchLog(ASSGND_WS_IDS);

			// purge Cash_Ws_Ids
			CashWorkstationIds laCashwsids =
				new CashWorkstationIds(caDBAccess);
			ciNumRows =
				laCashwsids.purgeCashWorkstationIds(ciPurgeDays);
			writeSubTableToBatchLog(CASH_WS_IDS);

			// purge Crdt_Card_Fee
			CreditCardFee laCrdtCardFee = new CreditCardFee(caDBAccess);
			ciNumRows = laCrdtCardFee.purgeCreditCardFee(ciPurgeDays);
			writeSubTableToBatchLog(CRDT_CARD_FEE);

			// purge Dealer
			// defect 10112
			Dealer laDealer = new Dealer(caDBAccess);
			ciNumRows = laDealer.purgeDealer(ciPurgeDays);
			// end defect 10112
			writeSubTableToBatchLog(DEALERS);

			// purge Inv_Profile 
			InventoryProfile laInvProfile =
				new InventoryProfile(caDBAccess);
			ciNumRows = laInvProfile.purgeInventoryProfile(ciPurgeDays);
			writeSubTableToBatchLog(INV_PROFILE);

			// purge LienHolders
			// defect 10112 
			Lienholder laLienholders = new Lienholder(caDBAccess);
			ciNumRows = laLienholders.purgeLienholder(ciPurgeDays);
			// end defect 10112 
			writeSubTableToBatchLog(LIENHOLDERS);

			// purge Pymnt_Acct
			PaymentAccount laPaymentaccount =
				new PaymentAccount(caDBAccess);
			ciNumRows =
				laPaymentaccount.purgePaymentAccount(ciPurgeDays);
			writeSubTableToBatchLog(PYMNT_ACCT);

			// purge Security
			Security laSecurity = new Security(caDBAccess);
			ciNumRows = laSecurity.purgeSecurity(ciPurgeDays);
			writeSubTableToBatchLog(SECURITY);

			// purge Substa
			Substation laSubstation = new Substation(caDBAccess);
			ciNumRows = laSubstation.purgeSubstation(ciPurgeDays);
			writeSubTableToBatchLog(SUBSTA);

			// purge Subcon
			Subcontractor laSubcontrator =
				new Subcontractor(caDBAccess);
			ciNumRows = laSubcontrator.purgeSubcontractor(ciPurgeDays);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(SUBCON);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge old CloseoutHistory rows.
	 * 
	 * @return boolean
	 */
	private boolean purgeCloseOutHstry()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeTrans();
			setPurgeDateAndWritePurgeDataToBatchLog();

			CloseOutHistory laCloseOut =
				new CloseOutHistory(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laCloseOut.purgeCloseOutHistory(
					caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	//	/**
	//	 * Purges all Special Plate transactions that have been 
	//	 * completed and are past the expiration period.  The fees table 
	//	 * for the matching transaction will also be purged.
	//	 * 
	//	 * TRANSSTATUSCD = 'C'
	//	 * AND
	//	 * INTEGER(SUBSTRING(AUDITTRAILTRANSID,7,5)) LESSTHAN 
	//	 * (AMDATE_TODAY - PURGEDAYS)
	//	 * 
	//	 * @return boolean
	//	 */
	//	private boolean purgeCompSPAPPTransAndFees()
	//	{
	//		ciPurgeDays = SystemProperty.getPurgeInetSPAPPComp();
	//		setPurgeDateAndWritePurgeDataToBatchLog();
	//
	//		try
	//		{
	//			InternetSpecialPlateTransaction laSPItrntTrans =
	//				new InternetSpecialPlateTransaction(caDBAccess);
	//			InternetSpecialPlateTransactionFees laSPItrntTransFees =
	//				new InternetSpecialPlateTransactionFees(caDBAccess);
	//			caDBAccess.beginTransaction();
	//			// Must purge fees first !!!!!!
	//			laSPItrntTransFees.purgeCompleteTransFees(ciPurgeDays);
	//			// Must purge fees first then trans !!!!!!
	//			laSPItrntTrans.purgeCompleteTrans(ciPurgeDays);
	//			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
	//			
	//			return PURGE_SUCCESS;
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			handleRollback();
	//			return PURGE_FAILURE;
	//		}
	//	}
	/**
	 * Purges all Special Plate transactions that have been 
	 * completed and are past the expiration period.  
	 * 
	 * TRANSSTATUSCD = 'C'
	 * AND
	 * INTEGER(SUBSTRING(AUDITTRAILTRANSID,7,5)) LESSTHAN 
	 * (AMDATE_TODAY - PURGEDAYS)
	 * 
	 * @return boolean
	 */
	private boolean purgeCompSPAPPTrans()
	{
		ciPurgeDays = SystemProperty.getPurgeInetSPAPPComp();
		setPurgeDateAndWritePurgeDataToBatchLog();

		try
		{
			InternetSpecialPlateTransaction laSPItrntTrans =
				new InternetSpecialPlateTransaction(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows = laSPItrntTrans.purgeCompleteTrans(ciPurgeDays);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge Disabled Placard Customer 
	 * 
	 * @return boolean
	 */
	private boolean purgeDsabldPlcrdCust()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeDsabldPlcrdCust();
			setPurgeDateAndWritePurgeDataToBatchLog();

			DisabledPlacardCustomer laDsabldPlcrdCust =
				new DisabledPlacardCustomer(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laDsabldPlcrdCust.purgeDisabledPlacardCustomer(
					caPurgeDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}
	/**
	 * Purge old Exempt Audit Rows.
	 * 
	 * @return boolean
	 */
	private boolean purgeExemptAudit()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeExmptAudit();
			setPurgeDateAndWritePurgeDataToBatchLog();

			ExemptAudit laExAudit = new ExemptAudit(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows =
				laExAudit.purgeExemptAudit(caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge old ETtlHstry Rows
	 * 
	 * @return boolean
	 */
	private boolean purgeETtlHstry()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeETtlHstry();
			setPurgeDateAndWritePurgeDataToBatchLog();

			ElectronicTitleHistory laETtlHstry =
				new ElectronicTitleHistory(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows =
				laETtlHstry.purgeElectronicTitleHistory(
					caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	//	/**
	//	 * Purges all complete Special Plate transactions that are 
	//	 * incomplete and are past the expiration period.  The fees table 
	//	 * for the matching transaction will also be purged.
	//	 * 
	//	 * TRANSSTATUSCD = 'A' OR 'I'
	//	 * AND
	//	 * INITREQTIMESTMP LESSTHAN (NOW - PURGEDAYS)
	//	 * AND
	//	 * NO MATCHING TRANSSTATUSCD OF 'P' OR 'C'
	//	 * 
	//	 * @return boolean
	//	 */
	//	private boolean purgeIncompSPAPPTransAndFees()
	//	{
	//		ciPurgeDays = SystemProperty.getPurgeInetSPAPPIncomp();
	//		setPurgeDateAndWritePurgeDataToBatchLog();
	//
	//		try
	//		{
	//			InternetSpecialPlateTransaction laSPItrntTrans =
	//				new InternetSpecialPlateTransaction(caDBAccess);
	//			InternetSpecialPlateTransactionFees laSPItrntTransFees =
	//				new InternetSpecialPlateTransactionFees(caDBAccess);
	//			caDBAccess.beginTransaction();
	//			// Must purge fees first !!!!!!
	//			laSPItrntTransFees.purgeIncompleteTransFees(ciPurgeDays);
	//			// Must purge fees first then trans !!!!!!
	//			laSPItrntTrans.purgeIncompleteTrans(ciPurgeDays);
	//			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
	//			
	//			return PURGE_SUCCESS;
	//		}
	//		catch (RTSException aeRTSEx)
	//		{
	//			handleRollback();
	//			return PURGE_FAILURE;
	//		}
	//	}
	/**
	 * Purges all complete Special Plate transactions that are 
	 * incomplete and are past the expiration period.  
	 * 
	 * TRANSSTATUSCD = 'A' OR 'I'
	 * AND
	 * INITREQTIMESTMP LESSTHAN (NOW - PURGEDAYS)
	 * AND
	 * NO MATCHING TRANSSTATUSCD OF 'P' OR 'C'
	 * 
	 * @return boolean
	 */
	private boolean purgeIncompSPAPPTrans()
	{
		ciPurgeDays = SystemProperty.getPurgeInetSPAPPIncomp();
		setPurgeDateAndWritePurgeDataToBatchLog();

		try
		{
			InternetSpecialPlateTransaction laSPItrntTrans =
				new InternetSpecialPlateTransaction(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows =
				laSPItrntTrans.purgeIncompleteTrans(ciPurgeDays);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge old InvHistory Rows.
	 * 
	 * @return boolean
	 */
	private boolean purgeInventoryHistory()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeInvHist();
			setPurgeDateAndWritePurgeDataToBatchLog();

			InventoryHistory laInvHis =
				new InventoryHistory(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laInvHis.purgeInventoryHistory(caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge Internet Data and Internet Trans for Renewals  
	 * 
	 * @param aiCntyStatusCd int
	 * @return boolean
	 */
	public boolean purgeIRenewItrntDataItrntTrans(int aiCntyStatusCd)
	{
		try
		{
			InternetTransaction laItrntTrans =
				new InternetTransaction(caDBAccess);
			InternetData laItrntData = new InternetData(caDBAccess);

			caDBAccess.beginTransaction();
			// The sequence is IMPORTANT,
			// purgeData must be called before purgeTrans.

			// The data table has no timestamp, it is
			// from the trans table.
			int liNumRows =
				laItrntData.purgeItrntData(
					aiCntyStatusCd,
					ciPurgeItrntDataDays);
			// defect 9005
			// In method purgeItrntIRenewTrans called below, we are 
			// only purging unpaid records if itrntpymntstatuscd is null
			// so the 11 days (using ciPurgeItrntDataDays) is good in
			// this case
			int liNumRows2 = 0;
			if (aiCntyStatusCd == CommonConstants.UNPAID)
			{
				liNumRows2 =
					laItrntTrans.purgeItrntIRenewTrans(
						aiCntyStatusCd,
						ciPurgeItrntDataDays);
			}
			else
			{
				liNumRows2 =
					laItrntTrans.purgeItrntIRenewTrans(
						aiCntyStatusCd,
						ciPurgeItrntTransDays);
			}
			ciNumRows = liNumRows + liNumRows2;
			// end defect 9005

			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge Internet Deposit Recon 
	 * 
	 * @return boolean 
	 */
	private boolean purgeItrntDepositRecon()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeInetDepositRecon();
			setPurgeDateAndWritePurgeDataToBatchLog();

			InternetDepositRecon laItrntDepositRecon =
				new InternetDepositRecon(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laItrntDepositRecon.purgeInternetDepositRecon(
					caPurgeDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge Internet Deposit Recon History 
	 * 
	 * @return boolean 
	 */
	private boolean purgeItrntDepositReconHstry()
	{
		try
		{
			ciPurgeDays =
				SystemProperty.getPurgeInetDepositReconHstry();
			setPurgeDateAndWritePurgeDataToBatchLog();

			InternetDepositReconHstry laItrntDepositReconHstry =
				new InternetDepositReconHstry(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laItrntDepositReconHstry
					.purgeInternetDepositReconHstry(
					caPurgeDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}
	/**
	 * Used to purge Internet Address change transactions.
	 * 
	 * @return boolean
	 */
	private boolean purgeItrntIAddrIRnrTrans()
	{
		ciPurgeDays = SystemProperty.getPurgeInetAddrChng();
		setPurgeDateAndWritePurgeDataToBatchLog();

		try
		{
			InternetTransaction laItrntTrans =
				new InternetTransaction(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows = laItrntTrans.purgeIAddrIRnrTrans(ciPurgeDays);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;

		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge Internet Summary
	 * 
	 * @param aiMonths int
	 * @return boolean
	 */
	private boolean purgeItrntSummary()
	{
		try
		{
			int liPurgeMonths = SystemProperty.getPurgeInetSummary();
			writePurgeMonthsToBatchLog(liPurgeMonths);

			InternetSummary laItrntSummary =
				new InternetSummary(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows = laItrntSummary.purgeItrntSummary(liPurgeMonths);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge old ItrntTransDelLog  Rows
	 * 
	 * @return boolean
	 */
	private boolean purgeItrntTransDelLog()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeItrntTransDelLog();
			setPurgeDateAndWritePurgeDataToBatchLog();

			InternetTransactionDeleteLog laItrntTransDelLog =
				new InternetTransactionDeleteLog(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows =
				laItrntTransDelLog.purgeItrntTransDelLog(ciPurgeDays);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge the LogFuncTrans  
	 */
	private boolean purgeLogonFunctionTransaction()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeTrans();
			setPurgeDateAndWritePurgeDataToBatchLog();

			LogonFunctionTransaction laLogFunc =
				new LogonFunctionTransaction(caDBAccess);

			caDBAccess.beginTransaction();

			// defect 9011 
			// use caPurgeDate vs. local variable
			ciNumRows =
				laLogFunc.purgeLogonFunctionTransaction(
					caPurgeDate.getYYYYMMDDDate());
			// end defect 9011 

			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/** 
	 * Purges RTS_MF_REQ
	 * 
	 * @return boolean 
	 */
	private boolean purgeMFReq()
	{
		try
		{
			MFRequest laMFReq = new MFRequest(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows = laMFReq.purgeMFReq();
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/** 
	 * Purges Mainframe Request History 
	 * 
	 * @return boolean 
	 */
	private boolean purgeMFReqHstry()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeMFReqHstry();
			setPurgeDateAndWritePurgeDataToBatchLog();

			MFRequestHistory laMFReqHstry =
				new MFRequestHistory(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows = laMFReqHstry.purgeMFReqHstry(ciPurgeDays);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purges Orphan Itrnt SPAPP Fees 
	 * 
	 * @return boolean
	 */
	private boolean purgeOrphanSPAPPTransFees()
	{
		try
		{
			InternetSpecialPlateTransactionFees laSPItrntTransFees =
				new InternetSpecialPlateTransactionFees(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows = laSPItrntTransFees.purgeOrphanTransFees();
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge old Reprint Sticker Rows.
	 */
	private boolean purgeReprintSticker()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeReprntStkr();
			setPurgeDateAndWritePurgeDataToBatchLog();

			ReprintSticker laRprStkr = new ReprintSticker(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laRprStkr.purgeReprintSticker(caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge old RPSP Print Rows.
	 */
	private boolean purgeRSPSPrint()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeTrans();
			setPurgeDateAndWritePurgeDataToBatchLog();

			RSPSPrint laRSPSPrint = new RSPSPrint(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laRSPSPrint.purgeRSPSPrint(caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge Security Log rows.
	 */
	private boolean purgeSecurityLog()
	{

		try
		{
			ciPurgeDays = SystemProperty.getPurgeSecLog();
			setPurgeDateAndWritePurgeDataToBatchLog();

			SecurityLog laSecLog = new SecurityLog(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laSecLog.purgeSecurityLog(caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Set DeleteIndi for Disabled Placard Customer records  
	 * 
	 * @return boolean
	 */
	private boolean purgeSetDelIndiDsabldPlcrdCust()
	{
		try
		{
			ciPurgeDays =
				SystemProperty.getPurgeSetDelIndiDsabldPlcrdCust();
			setPurgeDateAndWritePurgeDataToBatchLog();

			DisabledPlacardCustomer laDsabldPlcrdCust =
				new DisabledPlacardCustomer(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laDsabldPlcrdCust
					.purgeSetDelIndiDisabledPlacardCustomer(
					caPurgeDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}
	/**
	 * Purge old SpclPltRejLog Rows.
	 * 
	 * @return boolean
	 */
	private boolean purgeSpclPltRejLog()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeSpclPltRejLog();
			setPurgeDateAndWritePurgeDataToBatchLog();

			SpecialPlateRejectionLog laSpclPltRejLog =
				new SpecialPlateRejectionLog(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laSpclPltRejLog.purgeSpecialPlateRejectionLog(
					caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}
	/**
	 * Purge old SpclPltTransHstry Rows.
	 * 
	 * @return boolean
	 */
	private boolean purgeSpclPltTransHstry()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeSpclPltTransHstry();
			setPurgeDateAndWritePurgeDataToBatchLog();

			SpecialPlateTransactionHistory laSpclPltTransHstry =
				new SpecialPlateTransactionHistory(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laSpclPltTransHstry
					.purgeSpecialPlateTransactionHistory(
					caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}
	/**
	 * Purge old SpclPltPrmt Rows
	 * 
	 * @return boolean
	 */
	private boolean purgeSpclPltPrmt()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeSpclPltPrmt();
			setPurgeDateAndWritePurgeDataToBatchLog();

			SpecialPlatePermit laSpclPltPrmt =
				new SpecialPlatePermit(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laSpclPltPrmt.purgeSpecialPlatePermit(
					caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge Srvc Hstry
	 * 
	 * @return boolean
	 */
	private boolean purgeSrvcHstry()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeWebServices();
			setPurgeDateAndWritePurgeDataToBatchLog();

			WebServicesServiceHistorySql laWebSrvcHstry =
				new WebServicesServiceHistorySql(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laWebSrvcHstry.purgeWebServicesServiceHistory(
					caPurgeDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Remove Substation Summary rows on the specified number of days.
	 * 
	 * @return boolean
	 */
	private boolean purgeSubstationSummary()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeTrans();
			setPurgeDateAndWritePurgeDataToBatchLog();

			SubstationSummary laSubSum =
				new SubstationSummary(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laSubSum.purgeSubstationSummary(
					caPurgeDate.getAMDate());
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Delete Transaction Data.
	 * 
	 * @param int aiPurgeAMDate
	 * @return boolean 
	 */
	private boolean purgeTransactions(int aiPurgeAMDate)
	{
		// RTS_TRANS_HDR (#1)
		try
		{
			TransactionHeader laTransHeader =
				new TransactionHeader(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laTransHeader.purgeTransactionHeader(aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(TRANS_HDR);
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}

		// RTS_TRANS (#2)
		try
		{
			com.txdot.isd.rts.server.db.Transaction laTrans =
				new com.txdot.isd.rts.server.db.Transaction(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows = laTrans.purgeTransaction(aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(TRANS);
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}

		// RTS_TRANS_PAYMENT (#3)
		try
		{
			TransactionPayment laTransPay =
				new TransactionPayment(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laTransPay.purgeTransactionPayment(aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(TRANS_PAYMENT);
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}

		// RTS_MV_FUNC_TRANS (#4) 
		try
		{

			MotorVehicleFunctionTransaction laMvTrans =
				new MotorVehicleFunctionTransaction(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laMvTrans.purgeMotorVehicleFunctionTransaction(
					aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(MV_FUNC_TRANS);
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}

		// RTS_INV_FUNC_TRANS (#5) 
		try
		{
			InventoryFunctionTransaction laInvTrans =
				new InventoryFunctionTransaction(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laInvTrans.purgeInventoryFunctionTransaction(
					aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(INV_FUNC_TRANS);
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}

		// RTS_TR_INV_DETAIL (#6) 
		try
		{
			TransactionInventoryDetail laTransInv =
				new TransactionInventoryDetail(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laTransInv.purgeTransactionInventoryDetail(
					aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(TR_INV_DETAIL);
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}

		// RTS_FUND_FUNC_TRNS   (#7) 
		try
		{
			FundFunctionTransaction laFundFunc =
				new FundFunctionTransaction(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laFundFunc.purgeFundFunctionTransaction(aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(FUND_FUNC_TRANS);
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}

		// RTS_TR_FDS_DETAIL (#8) 
		try
		{
			TransactionFundsDetail laTransFunds =
				new TransactionFundsDetail(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laTransFunds.purgeTransactionFundsDetail(aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(TR_FDS_DETAIL);
		}

		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
		// RTS_SR_FUNC_TRANS (#9) 
		try
		{
			SpecialRegistrationFunctionTransaction laSRFuncTrans =
				new SpecialRegistrationFunctionTransaction(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laSRFuncTrans
					.purgeSpecialRegistrationFunctionTransaction(
					aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(SR_FUNC_TRANS);
		}

		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}

		//	RTS_PRMT_TRANS (#10) 
		try
		{
			PermitTransaction laPrmtTrans =
				new PermitTransaction(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows =
				laPrmtTrans.purgePermitTransaction(aiPurgeAMDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);
			writeSubTableToBatchLog(PRMT_TRANS);
		}

		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}

		return PURGE_SUCCESS;
	}

	/**
	 * Purge V21 Request
	 * 
	 * @return boolean
	 */
	private boolean purgeV21Request()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeInvHist();
			setPurgeDateAndWritePurgeDataToBatchLog();

			V21Request laV21Req = new V21Request(caDBAccess);

			caDBAccess.beginTransaction();
			ciNumRows = laV21Req.purgeV21Request(caPurgeDate);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Purge old Virtual Inventory Rows.
	 * 
	 * <p>Also release any System Holds that have not expired.
	 * 
	 * @return boolean
	 */
	private boolean purgeVirtualInventory()
	{
		InventoryServerBusiness laISB = null;
		try
		{
			ciPurgeDays = SystemProperty.getPurgeTrans();
			setPurgeDateAndWritePurgeDataToBatchLog();

			laISB = new InventoryServerBusiness(PURGE);

			// defect 9431
			// set the purge date correctly using caPurgeDate
			Integer laPurgeDate = new Integer(caPurgeDate.getAMDate());

			// setup the vector
			Vector lvParameters = new Vector(2);
			lvParameters.add(caDBAccess);
			lvParameters.add(laPurgeDate);

			// purge completes from Virtual Inventory
			// Pass the Vector with the parameters
			ciNumRows =
				((Integer) laISB
					.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant.INV_VI_PURGE,
						lvParameters))
					.intValue();

			// setup the new purge date to be Today
			Integer laReleaseDate =
				new Integer(caPurgeDate.getAMDate());

			// re-set up the vector.
			lvParameters = new Vector(2);
			lvParameters.add(caDBAccess);
			lvParameters.add(laReleaseDate);

			// Release all holds
			// date is actually meaningless at this time.
			// Pass the Vector with the parameters
			laISB.processData(
				GeneralConstant.INVENTORY,
				InventoryConstant.INV_VI_RELEASE_ALL_SYSTEM_HOLDS,
				lvParameters);
			// end defect 9431

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
		finally
		{
			laISB = null;
		}
	}

	/**
	 * Purge old WorkstationStatusLog Rows
	 * 
	 * @return boolean
	 */
	private boolean purgeWorkstationStatusLog()
	{
		try
		{
			ciPurgeDays = SystemProperty.getPurgeWSStatusLog();
			setPurgeDateAndWritePurgeDataToBatchLog();

			WorkstationStatusLog laWksStatusLog =
				new WorkstationStatusLog(caDBAccess);
			caDBAccess.beginTransaction();
			ciNumRows =
				laWksStatusLog.purgeWorkstationStatusLog(ciPurgeDays);
			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * New purge method to select the eligible dates to purge
	 * to prevent DB Transaction Log full 
	 *  
	 * @return Vector
	 */
	private Vector qryEligibleTransAMDates()
	{
		Vector lvTransAMDates = null;

		int liNumTransAMDates = 0;

		try
		{
			caDBAccess.beginTransaction();
			com.txdot.isd.rts.server.db.Transaction laTrans =
				new com.txdot.isd.rts.server.db.Transaction(caDBAccess);

			// defect 9011
			// use caPurgeDate vs. local variable 
			lvTransAMDates =
				laTrans.qryTransactionDatesForPurge(
					caPurgeDate.getAMDate());
			// end defect 9011 

			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			if (lvTransAMDates != null && lvTransAMDates.size() != 0)
			{
				liNumTransAMDates = lvTransAMDates.size();
			}
			BatchLog.write(
				liNumTransAMDates + TRANSAMDATES_SEL_FOR_PURGE);
			return (lvTransAMDates);
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return null;
		}

	}

	/**
	 * Method to update Disabled Cust if In Process.
	 *  
	 * @return boolean 
	 */
	private boolean resetInProcsDsabldPlcrd()
	{
		try
		{
			caDBAccess.beginTransaction();

			DisabledPlacardCustomer laDPCust =
				new DisabledPlacardCustomer(caDBAccess);

			ciNumRows =
				laDPCust.resetAllInProcsDisabledPlacardCustomer();

			caDBAccess.endTransaction(DatabaseAccess.COMMIT);

			return PURGE_SUCCESS;
		}
		catch (RTSException aeRTSEx)
		{
			handleRollback();
			return PURGE_FAILURE;
		}
	}

	/**
	 * Write Purge Days to BatchLog 
	 */
	private void setPurgeDateAndWritePurgeDataToBatchLog()
	{
		// defect 9011
		// Subtract 1 day to reflect the maximum date purged.  
		caPurgeDate =
			RTSDate.getCurrentDate().add(
				RTSDate.DATE,
				-ciPurgeDays - 1);
		// end defect 9011 
		BatchLog.write(ciPurgeDays + DAYS + FOR + csPurgeType);
		BatchLog.write(
			PURGE_DATE_IS
				+ caPurgeDate.getAMDate()
				+ SPACE
				+ "("
				+ caPurgeDate.toString()
				+ ")");
	}

	/**
	 * Write Purge Months to BatchLog 
	 */
	private void writePurgeMonthsToBatchLog(int aiPurgeMonths)
	{
		BatchLog.write(aiPurgeMonths + MONTHS + FOR + csPurgeType);

		int liCurrentYr = RTSDate.getCurrentDate().getYear();
		int liCurrentMo = RTSDate.getCurrentDate().getMonth();
		int liCurrentMonths = liCurrentYr * 12 + liCurrentMo;

		int liDelYr = (int) (liCurrentMonths - aiPurgeMonths) / 12;
		int liDelMo = (liCurrentMonths - aiPurgeMonths) % 12;
		int liDelYrMo = liDelYr * 100 + liDelMo;
		BatchLog.write(PURGE_YYYYMM_IS_LT + liDelYrMo);
	}

	/**
	 * 
	 * Write separator constant to BatchLog 
	 *
	 */
	private void writeSeparator()
	{
		BatchLog.write(SEPARATOR);
	}

	/**
	 * Write msg to BatchLog 
	 */
	private void writeStatusToBatchLog(
		String asMsg,
		boolean abSuccessful)
	{
		writeStatusToBatchLog(asMsg, abSuccessful, true);
	}

	/**
	 * Write msg to BatchLog 
	 */
	private void writeStatusToBatchLog(
		String asMsg,
		boolean abSuccessful,
		boolean abRows)
	{
		if (abSuccessful)
		{
			BatchLog.write(
				UtilityMethods.addPaddingRight(
					asMsg + SUCCESSFUL,
					50,
					" ")
					+ (abRows ? constructNumRows() : ""));
		}
		else
		{
			BatchLog.write(asMsg + FAILED);
		}
	}

	/**
	 * Write number of Rows Deleted to BatchLog
	 * 
	 */
	private void writeSubTableToBatchLog(String asMsg)
	{
		BatchLog.write(
			UtilityMethods.addPaddingRight(
				"  " + asMsg,
				TABLE_NAME_LENGTH,
				" ")
				+ constructNumRows());
	}
}