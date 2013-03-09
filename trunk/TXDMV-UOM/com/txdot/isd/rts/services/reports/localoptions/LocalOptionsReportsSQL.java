package com.txdot.isd.rts.services.reports.localoptions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.UtilityMethods;

import com.txdot.isd.rts.server.dataaccess.DBValue;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * LocalOptionsReportsSQL.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K. Harrell   01/15/2002  Removed/added columns
 * K. Harrell   07/09/2002  Altered SQL to remove 'CCDO'
 * S Govindappa 07/10/2002  Merged PCR25 changes to display credit card 
 * 							fee info
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Min Wang		10/15/2003	Add SysUserId for xp.
 *							modify qryEventSecurityReport()
 *                          Defect 6616. Version 5.1.6.
 * Min Wang		1/22/2004	Change SysUserId to UserName
 *							modify qryEventSecurityReport()
 *							Defect 6616. Version 5.1.6.
 * K Harrell	01/29/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Added call for Reprint Sticker Report Access
 * 							Removed call for Quick Counter Access
 * 							Import from 5.2.0; 	
 *							Ver 5.2.0	
 * K Harrell	03/21/2004	5.1.6 Merge. 
 * 							modify qryEventSecurityReport()
 * 							Ver 5.2.0
 * Min Wang     07/19/2004	Add RSPSUPDTACCS for security event report.
 *							modify qryEventSecurityReport()
 *							defect 7311 Ver 5.2.1
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * M Reyes		10/13/2006	Added references to ExmptAuditRptAccs &
 * 							ExmptAuthAccs
 * 							modify qryEventSecurityReport() 
 * 							defect 8900 Ver Exempts
 * M Reyes		04/24/2007	Added references to SpclPltAccs,
 * 							SpclPltApplAccs, SpclPltRenewPltAccs,
 * 							SpclPltRevisePltAccs, SpclPltResrvPltAccs,
 * 							SpclPltUnAccptblPltAccs, SpclPltDelPltAccs,
 * 							SpclPltRptsAccs
 * 							modify qryEventSecurityReport()
 * 							defect 9124 Ver Special Plates
 * Min Wang		04/09/2008	Change event name "ISSUE DRIVERS ED".
 * 							modify qryEventSecurityReport()
 * 							defect 9083 Ver Defect_POS_A 
 * K Harrell	09/10/2008	Added references to DlrRptAccs, 
 * 							  LienHldrRptAccs, SubconRptAccs
 * 							modify qryEventSecurityReport()
 * 							defect 9710 Ver Defect_POS_B
 * K Harrell	10/27/2008	Add reference to DsabldPlcrdRptAccs, 
 * 								DsabldPlcrdInqAccs. 
 * 							Sort by EventName.   
 * 							modify qryEventSecurityReport()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	01/27/2009	Update SQL to preface event names with 
 * 							 Event Categories where applicable. Add 
 * 							 query components for INVENTORY - HOLD/RELEASE, 
 * 							 INVENTORY - PROFILE AND REPORT. 
 * 							 Modified criteria for 
 * 							 Title/Registration, Miscellaneous 
 * 							add EVT_QRY_PART1, EVT_QRY_PART2, 
 * 							 EVT_QRY_PART3, UNIONALL, ciEvtSubstaId   
 * 							add eventQryInit(), eventQryAppend()
 * 							modify qryEventSecurityReport() 
 * 							defect 7116 Ver Defect_POS_D   
 * K Harrell	02/06/2009	Corrected typo for Dealer Rpt Access Oly
 * 							modify qryEventSecurityReport()
 * 							defect 7116 Ver Defect_POS_D
 * B Hargrove	03/09/2009	Added references to ETtlRptAccs 
 * 							modify qryEventSecurityReport()
 * 							defect 9960 Ver Defect_POS_E 
 * Min Wang		08/11/2009	Added references to PrivateLawEnfVehAccs 
 * 							modify qryEventSecurityReport()
 * 							defect 10153 Ver Defect_POS_F 
 * K Harrell	10/06/2009	Correct earlier Merge Issue InvAccs twice
 * 							modify qryEventSecurityReport()
 * 							defect 10215 Defect_POS_G 
 * Min Wang 	11/24/2009	Merge Issue. delete duplicated code.
 * 							defect 10153 Ver Defect_POS_H
 * Min Wang 	01/13/2011	Added references to WebAgentAccs 
 * 							modify qryEventSecurityReport()
 * 							defect 10717 Ver 6.7.0
 * Min Wang 	01/18/2011	Added references to WebAgntAccs 
 * 							delete WebAgentAccs
 * 							modify qryEventSecurityReport()
 * 							defect 10717 Ver 6.7.0
 * K Harrell	01/20/2011  Add reference to BatchRptMgmt
 * 							modify qryEventSecurityReport()
 * 							defect 10701 Ver 6.7.0 
 * Min Wang		01/26/2011	modify qryEventSecurityReport()
 * 							defect 10717 Ver 6.7.0
 * K Harrell	05/28/2011	Add reference to ModfyTimedPrmtAccs 
 * 							modify qryEventSecurityReport()
 * 							defect 10844 Ver 6.8.0
 * K Harrell	06/17/2011	add reference to Missing Title Package 
 * 							  (all County & Region) 
 * 							add reference to Suspected Fraud Report
 * 							  (all Region & HQ) 	 
 * 							modify qryEventSecurityReport()
 * 							defect 10900 Ver 6.8.0
 * K Harrell	01/11/2012	add ExportAccs, DsabldPlcrdReinstateAccs 
 * 							modify qryEventSecurityReport()
 * 							defect 11231 Ver 6.10.0
 * K Harrell	02/02/2012	Javadoc cleanup
 * 							defect 11231 Ver 6.10.0  
 * ---------------------------------------------------------------------
 */
/**
 * Method to access database for Local Options Reports
 * 
 * @version	6.10.0		 		02/02/2012
 * @author	Kathy Harrell
 * <br>Creation Date:			09/24/2001 
 */
public class LocalOptionsReportsSQL
{
	// defect 7116 
	private static String EVT_QRY_PART1 =
		"SELECT EmpId,UserName, EmpLastName,EmpFirstName,EmpMI,";
	private static String EVT_QRY_PART2 =
		" as EventName from RTS.RTS_SECURITY WHERE ";
	private static String EVT_QRY_PART3 =
		"= 1 AND DELETEINDI = 0 and OFCISSUANCENO = ? AND SUBSTAID = ";
	private static String UNIONALL = " UNION ALL ";
	private int ciEvtSubstaId = 0;
	// end defect 7116 

	DatabaseAccess caDA;

	/**
	 * LocalOptionsReportsSQL constructor
	 * 
	 * @param aaDA DatabaseAccess
	 * @throws RTSException
	 */
	public LocalOptionsReportsSQL(DatabaseAccess aaDA)
		throws RTSException
	{
		caDA = aaDA;
	}

	/** 
	 * Initialize Query
	 * 
	 * @param asQry
	 * @param asName
	 * @param asColumnName
	 */
	private void eventQryInit(
		StringBuffer asQry,
		String asName,
		String asColumnName)
	{
		asQry.append(
			EVT_QRY_PART1
				+ "'"
				+ asName
				+ "'"
				+ EVT_QRY_PART2
				+ asColumnName
				+ EVT_QRY_PART3
				+ ciEvtSubstaId);
	}

	/** 
	 * Append to Qry 
	 * 
	 * @param asQry
	 * @param asName
	 * @param asColumnName
	 */
	private void eventQryAppend(
		StringBuffer asQry,
		String asName,
		String asColumnName)
	{
		asQry.append(
			UNIONALL
				+ EVT_QRY_PART1
				+ "'"
				+ asName
				+ "'"
				+ EVT_QRY_PART2
				+ asColumnName
				+ EVT_QRY_PART3
				+ ciEvtSubstaId);
	}

	/**
	 * Method to Query Security table for Local Options Report
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector 	
	 * @throws RTSException
	 */
	public Vector qryEventSecurityReport(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(
			Log.METHOD,
			this,
			" - qryEventSecurityReport - Begin ");
		StringBuffer lsQry = new StringBuffer();
		StringBuffer lsQry2 = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		Vector lvValues2 = new Vector();
		ResultSet laResultSet;
		// defect 7116
		// Modify reported event names. Use methods to assemble SQL
		ciEvtSubstaId = qrySecuritySubstaId(aaGeneralSearchData);

		// EVENT SET #1 
		int liEventSet1 = 0;
		eventQryInit(lsQry, "ACCOUNTING", "AcctAccs");
		++liEventSet1;

		// defect 9124
		// 'APPLICATION'
		eventQryAppend(
			lsQry,
			"SPECIAL PLATES - APPLICATION",
			"SpclPltApplAccs");
		++liEventSet1;
		// end defect 9124

		// 'ADMINISTRATION' 
		eventQryAppend(
			lsQry,
			"LOCAL OPTIONS - ADMINISTRATIVE FUNCTIONS",
			"AdminAccs");
		++liEventSet1;

		// 'ACKNOWLEDGMENT RECEIPT' 
		eventQryAppend(
			lsQry,
			"INVENTORY - RECEIVE INVOICE",
			"InvAckAccs");
		++liEventSet1;

		// 'ADDR CHNG/PRNT RENWL'
		eventQryAppend(
			lsQry,
			"REGISTRATION ONLY - ADDRESS CHANGE/PRINT RENEWAL",
			"AddrChngAccs");
		++liEventSet1;

		//6
		// 'ADDITIONAL SALES TAX' 
		eventQryAppend(
			lsQry,
			"TITLE/REGISTRATION - ADDITIONAL SALES TAX",
			"AdjSalesTaxAccs");
		++liEventSet1;

		//7
		// 'ALLOCATE' 
		eventQryAppend(lsQry, "INVENTORY - ALLOCATE", "InvAllocAccs");
		++liEventSet1;

		//8
		// 'BONDED TITLE CODE' 
		eventQryAppend(
			lsQry,
			"TITLE/REGISTRATION - STATUS CHANGE - BONDED TITLE CODE",
			"BndedTtlCdAccs");
		++liEventSet1;
		
		
		//9
		// 'CANCEL REG' 
		eventQryAppend(
			lsQry,
			"TITLE/REGISTRATION - STATUS CHANGE - CANCEL REGISTRATION",
			"CancRegAccs");
		++liEventSet1;
		
		// defect 11231 
		// 9a
		// 'EXPORT' 
		eventQryAppend(
			lsQry,
			"TITLE/REGISTRATION - STATUS CHANGE - EXPORT",
			"ExportAccs");
		++liEventSet1;
		// end defect 11231 

		//10
		// 'CCO' 
		eventQryAppend(lsQry, "TITLE/REGISTRATION - CCO", "CCOAccs");
		++liEventSet1;

		//11
		// 'CASH OPERATIONS' 
		eventQryAppend(
			lsQry,
			"FUNDS - CASH DRAWER OPERATIONS",
			"CashOperAccs");
		++liEventSet1;

		//12
		// 'COMPLETE VEHICLE TRANSACTIONS'
		eventQryAppend(
			lsQry,
			"MISCELLANEOUS - COMPLETE VEHICLE TRANSACTIONS",
			"ReprntRcptAccs");
		++liEventSet1;

		//13
		// 'SALES TAX ALLOCATION REPORT'
		eventQryAppend(
			lsQry,
			"REPORTS - SALES TAX ALLOCATION REPORT",
			"CntyRptsAccs");
		++liEventSet1;

		//14
		// 'COA' 
		eventQryAppend(lsQry, "TITLE/REGISTRATION - COA", "COAAccs");
		++liEventSet1;

		//15
		// 'CORRECT TTL REJCTN' 
		eventQryAppend(
			lsQry,
			"TITLE/REGISTRATION - CORRECT TITLE REJECTION",
			"CorrTtlRejAccs");
		++liEventSet1;

		// defect 9710 
		//16a
		// 'CREDIT CARD FEE' 
		eventQryAppend(
			lsQry,
			"LOCAL OPTIONS - CREDIT CARD FEE UPDATE",
			"CrdtCardFeeAccs");
		++liEventSet1;

		//17
		// 'DEALER TITLE' 
		eventQryAppend(
			lsQry,
			"TITLE/REGISTRATION - DEALER TITLES",
			"DlrTtlAccs");
		++liEventSet1;

		// defect 9710 
		//18
		// 'DEALER UPDS & RPT' 
		eventQryAppend(
			lsQry,
			"LOCAL OPTIONS - DEALER UPDATES AND REPORT",
			"DlrAccs");
		++liEventSet1;

		//19
		// vs. 'DEALER RPT'
		eventQryAppend(
			lsQry,
			"LOCAL OPTIONS - DEALER REPORT",
			"DlrAccs = 0 and DlrRptAccs");
		// end defect 9710 
		++liEventSet1;

		//20
		// 'DEDUCT HOT CHECK' 
		eventQryAppend(
			lsQry,
			"ACCOUNTING - DEDUCT HOT CHECK CREDIT",
			"ModfyHotCkAccs");
		++liEventSet1;

		// 'DEL TTL IN PROCESS'
		eventQryAppend(
			lsQry,
			"TITLE/REGISTRATION - DELETE TITLE IN PROCESS",
			"DelTtlInProcsAccs");
		++liEventSet1;

		//22
		// 'DMV FUNDS ADJ' 
		eventQryAppend(
			lsQry,
			"ACCOUNTING - ADDITIONAL COLLECTIONS/TIME LAG",
			"FundsAdjAccs");
		++liEventSet1;

		// defect 9831 
		//23
		// 'DISABLED PLACARD INQUIRY'
		eventQryAppend(
			lsQry,
			"MISCELLANEOUS REGISTRATION - DISABLED PLACARD INQUIRY",
			"DsabldPlcrdInqAccs");
		++liEventSet1;

		//24
		// 'DISABLED PLACARD MGMT' 
		eventQryAppend(
			lsQry,
			"MISCELLANEOUS REGISTRATION - DISABLED PLACARD MANAGEMENT",
			"DsabldPersnAccs");
		++liEventSet1;
		
		// defect 11231 
		// 24a
		// 'DISABLED PLACARD REINSTATE' 
		eventQryAppend(
			lsQry,
			"MISCELLANEOUS REGISTRATION - DISABLED PLACARD REINSTATE",
			"DsabldPlcrdReInstateAccs");
		++liEventSet1;
		// end defect 11231 

		// 25
		// 'DISABLED PLACARD RPT' 
		eventQryAppend(
			lsQry,
			"MISCELLANEOUS REGISTRATION - DISABLED PLACARD REPORT",
			"DsabldPlcrdRptAccs");
		// end defect 9831 
		++liEventSet1;

		//26
		// 'DUPLICATE RECEIPT' 
		eventQryAppend(
			lsQry,
			"REGISTRATION ONLY - DUPLICATE RECEIPT",
			"DuplAccs");
		++liEventSet1;

		//27
		// 'EMP RPT SECURITY'
		eventQryAppend(
			lsQry,
			"LOCAL OPTIONS - EMPLOYEE SECURITY REPORT",
			"EmpSecrtyRptAccs");
		++liEventSet1;

		//28
		// 'EMP SECURITY' 
		eventQryAppend(
			lsQry,
			"LOCAL OPTIONS - EMPLOYEE SECURITY",
			"EmpSecrtyAccs");
		++liEventSet1;

		//29
		// 'EXCHANGE' 
		eventQryAppend(
			lsQry,
			"REGISTRATION ONLY - EXCHANGE",
			"ExchAccs");
		++liEventSet1;

		// 30 
		// 'FUNDS BALANCE' 
		eventQryAppend(
			lsQry,
			"FUNDS - FUNDS BALANCE REPORTS",
			"FundsBalAccs");
		++liEventSet1;

		//31
		// 'FUNDS INQUIRY' 
		eventQryAppend(
			lsQry,
			"ACCOUNTING - FUNDS INQUIRY",
			"FundsInqAccs");
		++liEventSet1;

		//32
		// 'FUNDS MANAGEMENT' 
		eventQryAppend(
			lsQry,
			"FUNDS - FUNDS MANAGEMENT",
			"FundsMgmtAccs");
		++liEventSet1;

		//33
		// 'FUNDS REMITTANCE' 
		eventQryAppend(
			lsQry,
			"ACCOUNTING - COUNTY FUNDS REMITTANCE",
			"FundsRemitAccs");
		++liEventSet1;

		//34
		// 'HOT CHECK CREDIT'
		eventQryAppend(
			lsQry,
			"ACCOUNTING - HOT CHECK CREDIT",
			"HotCkCrdtAccs");
		++liEventSet1;

		//35
		// 'HOT CHECK REDEMD'
		eventQryAppend(
			lsQry,
			"ACCOUNTING - HOT CHECK REDEEMED",
			"HotCkRedemdAccs");
		++liEventSet1;

		//36
		// 'INQUIRY' 
		eventQryAppend(
			lsQry,
			"INQUIRY - VEHICLE INFORMATION",
			"InqAccs");
		++liEventSet1;

		// Internet Renewal  >> ITRNTRENWLACCS
		// 37
		// 'INTERNET RENEWAL' 
		eventQryAppend(
			lsQry,
			"REGISTRATION ONLY - INTERNET RENEWAL",
			"ItrntRenwlAccs");
		++liEventSet1;

		// 37a 
		// defect 10717
		eventQryAppend(
			lsQry,
			"REGISTRATION ONLY - WEBAGENT",
			"WebAgntAccs");
		++liEventSet1;
		// end defect 10717

		// defect 10215 
		//		//38
		//		eventQryAppend(lsQry, "INVENTORY ", "InvAccs");
		//		++liEventSet1;
		// end defect 10215 

		//38
		eventQryAppend(lsQry, "INVENTORY ", "InvAccs");
		++liEventSet1;

		//39
		// 'INV ACTION REPORT'
		eventQryAppend(
			lsQry,
			"INVENTORY - INVENTORY ACTION REPORT",
			"InvActionAccs");
		++liEventSet1;

		// 40 
		// 'ITEMS SEIZED' 
		eventQryAppend(
			lsQry,
			"ACCOUNTING - ITEM(S) SEIZED",
			"ItmSeizdAccs");
		++liEventSet1;

		//41
		// 'ISSUE DRIVER ED' 
		eventQryAppend(
			lsQry,
			"REGISTRATION ONLY - ISSUE DRIVER EDUCATION PLATE",
			"IssueDrvsEdAccs");
		++liEventSet1;

		//42
		// 'LEGAL RESTRNT NO' 
		eventQryAppend(
			lsQry,
			"TITLE/REGISTRATION - STATUS CHANGE - LEGAL RESTRNT NO",
			"LegalRestrntNoAccs");
		++liEventSet1;

		// defect 9710 
		//43
		// 'LIENHLDR UPDTS & RPT' 
		eventQryAppend(
			lsQry,
			"LOCAL OPTIONS - LIENHOLDER UPDATES AND REPORT",
			"LienHldrAccs");
		++liEventSet1;

		//44
		// vs. 'LIENHLDR RPT'
		eventQryAppend(
			lsQry,
			"LOCAL OPTIONS - LIENHOLDER REPORT",
			"LienHldrAccs = 0 and LienHldrRptAccs");
		// end defect 9710
		++liEventSet1;

		// defect 10701 
		// 44a
		// 
		eventQryAppend(
			lsQry,
			"LOCAL OPTIONS - BATCH REPORT MANAGEMENT",
			"BatchRptMgmtAccs");
		++liEventSet1;

		// end defect 10701 

		//45
		eventQryAppend(lsQry, "LOCAL OPTIONS", "LocalOptionsAccs");
		++liEventSet1;

		// EVENT SET #2  
		int liEventSet2 = 0;
		//46
		// 'MAIL RETURN' 
		eventQryInit(
			lsQry2,
			"TITLE/REGISTRATION - STATUS CHANGE - MAIL RETURN",
			"MailRtrnAccs");
		++liEventSet2;

		//47
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS REGISTRATION",
			"MiscRegAccs");
		++liEventSet2;

		//48
		// 'MISC REMARKS' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - STATUS CHANGE - MISCELLANEOUS REMARKS",
			"MiscRemksAccs");
		++liEventSet2;

		//49
		// Actually, MISCELLANEOUS is always enabled as 
		// Set Print Destination is always enabled. 
		eventQryAppend(lsQry2, "MISCELLANEOUS", "1");
		++liEventSet2;

		//50
		// 'MODIFY' 
		eventQryAppend(
			lsQry2,
			"REGISTRATION ONLY - MODIFY",
			"ModfyAccs");
		++liEventSet2;

		//51
		// 'NON RES AG PERMIT' 
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS REGISTRATION - NON-RESIDENT AGRICULTURE PERMIT",
			"NonResPrmtAccs");
		++liEventSet2;

		//52
		// 'PLATE NO' 
		eventQryAppend(
			lsQry2,
			"INQUIRY - RECORD RETRIEVAL BY PLATE NO",
			"PltNoAccs");
		++liEventSet2;

		//53
		// 'PLATE OPTIONS' 
		eventQryAppend(lsQry2, "INQUIRY - PLATE OPTIONS", "InqAccs");
		++liEventSet2;

		//54
		// 'REFUND' 
		eventQryAppend(lsQry2, "ACCOUNTING - REFUND", "RefAccs");
		++liEventSet2;

		//55
		// 'REGISTER BY' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - STATUS CHANGE - REGISTER BY",
			"RgstrByAccs");
		++liEventSet2;

		//56
		eventQryAppend(lsQry2, "REGISTRATION ONLY", "RegOnlyAccs");
		++liEventSet2;

		//57
		// 'REGISTRATION REFUND' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - STATUS CHANGE - REGISTRATION REFUND",
			"RegRefAmtAccs");
		++liEventSet2;

		// defect 9124
		//58
		// 'RESERVE' 
		eventQryAppend(
			lsQry2,
			"SPECIAL PLATES - RESERVE",
			"SpclPltResrvPltAccs");
		++liEventSet2;

		//59
		// 'REVISE' 
		eventQryAppend(
			lsQry2,
			"SPECIAL PLATES - REVISE",
			"SpclPltRevisePltAccs");
		// end defect 9124
		++liEventSet2;

		//60
		// PCR 34
		// 'REPRINT STICKER REPORT'
		eventQryAppend(
			lsQry2,
			"REPORTS - REPRINT STICKER REPORT",
			"ReprntStkrRptAccs");
		++liEventSet2;

		//61    
		// 'REGIONAL COLLECTION' 
		eventQryAppend(
			lsQry2,
			"ACCOUNTING - REGIONAL COLLECTION",
			"RegnlColltnAccs");
		++liEventSet2;

		//62
		// 'RENEWAL' 
		eventQryAppend(
			lsQry2,
			"REGISTRATION ONLY - RENEWAL",
			"RenwlAccs");
		++liEventSet2;

		// defect 9124
		//63
		// 'RENEW PLATE ONLY' 
		eventQryAppend(
			lsQry2,
			"SPECIAL PLATES - RENEW PLATE ONLY",
			"SpclPltRenwPltAccs");
		// end defect 9124
		++liEventSet2;

		// 64
		// 'REPLACEMENT' 
		eventQryAppend(
			lsQry2,
			"REGISTRATION ONLY - REPLACEMENT",
			"ReplAccs");
		++liEventSet2;

		//65
		// defect 10900 
		//eventQryAppend(lsQry2, "REPORTS", "RptsAccs");
		eventQryAppend(lsQry2, "REPORTS", "1");
		++liEventSet2;
		// end defect 10900 

		//66
		// 'REPRINT RECEIPTS' 
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS - REPRINT RECEIPT",
			"ReprntRcptAccs");
		++liEventSet2;

		//67
		// defect 8900
		// 'EXEMPT AUDIT REPORT'
		eventQryAppend(
			lsQry2,
			"REPORTS - EXEMPT AUDIT REPORT",
			"ExmptAuditRptAccs");
		// end defect 8900
		++liEventSet2;

		//67a
		// defect 9960
		// 'ELECTRONIC TITLE REPORT'
		eventQryAppend(
			lsQry2,
			"REPORTS - ELECTRONIC TITLE REPORT",
			"ETtlRptAccs");
		// end defect 9960
		++liEventSet2;

		//68
		// 'REPRINT REPORTS' 
		eventQryAppend(
			lsQry2,
			"REPORTS - REPRINT REPORTS",
			"ReprntRptAccs");
		++liEventSet2;

		// defect 10900
		// Found Title Package Missing 
		 
		// TITLE PACKAGE 
		eventQryAppend(
			lsQry2,
			"REPORTS - TITLE PACKAGE",
			" OfcissuanceNo <= 276 and 1");
		++liEventSet2;

		// SUSPECTED FRAUD 
		eventQryAppend(
			lsQry2,
			"REPORTS - SUSPECTED FRAUD",
			" OfcissuanceNo >= 260 and 1");
		++liEventSet2;
		// end defect 10900 

		// defect 9710 
		//69a
		// 'RSPS STATUS UPDATE'
		//defect 7311
		eventQryAppend(
			lsQry2,
			"LOCAL OPTIONS - RSPS STATUS UPDATE",
			"RSPSUpdtAccs");
		//end defect 7311
		++liEventSet2;

		//70
		// 'SALVAGE' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - SALVAGE",
			"SalvAccs");
		++liEventSet2;

		//71
		// 'SECURITY' 
		eventQryAppend(
			lsQry2,
			"LOCAL OPTIONS - SECURITY",
			"SecrtyAccs");
		++liEventSet2;

		//72
		// 'PRINT IMMEDIATE' 
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS - PRINT IMMEDIATE",
			"PrntImmedAccs");
		++liEventSet2;

		//73
		// 'SET PRINT DESTINATION' 
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS - SET PRINT DESTINATION",
			"ReprntRcptAccs");
		++liEventSet2;

		// defect 9124
		//74
		eventQryAppend(lsQry2, "SPECIAL PLATES", "SpclPltAccs");
		++liEventSet2;

		//75
		// 'SPCL PLT DELETE'  
		eventQryAppend(
			lsQry2,
			"SPECIAL PLATES - DELETE",
			"SpclPltDelPltAccs");
		++liEventSet2;

		//76
		// 'SPCL PLT REPORT' 
		eventQryAppend(
			lsQry2,
			"SPECIAL PLATES - REPORTS",
			"SpclPltRptsAccs");
		// end defect 9124
		++liEventSet2;

		//77
		// 'STATUS CHANGE' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - STATUS CHANGE",
			"StatusChngAccs");
		++liEventSet2;

		//78
		// 'STOLEN/SRS' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - STATUS CHANGE - STOLEN/SRS",
			"StlnSRSAccs");
		++liEventSet2;

		// defect 9710 
		//79
		// 'SUBCON UPDTS & RPT
		eventQryAppend(
			lsQry2,
			"LOCAL OPTIONS - SUBCONTRACTOR UPDATES AND REPORT",
			"SubconAccs");
		++liEventSet2;

		// 80
		// vs. SUBCON RPT 
		eventQryAppend(
			lsQry2,
			"LOCAL OPTIONS - SUBCONTRACTOR REPORT",
			"SubconAccs = 0 and SubconRptAccs ");
		// end defect 9710
		++liEventSet2;

		//81
		// vs. 'SUBCON RENWL' 
		eventQryAppend(
			lsQry2,
			"REGISTRATION ONLY - SUBCONTRACTOR RENEWAL",
			"SubconRenwlAccs");
		++liEventSet2;

		//82
		// 'TEMP ADDL WEIGHT' 
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS REGISTRATION - TEMPORARY ADDITIONAL WEIGHT",
			"TempAddlWtAccs");
		++liEventSet2;

		//83
		// 'TIMED PERMIT' 
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS REGISTRATION - TIMED PERMIT",
			"TimedPrmtAccs");
		++liEventSet2;

		// defect 10844
		//83a
		// 'TIMED PERMIT'- MODIFY 
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS REGISTRATION - TIMED PERMIT - MODIFY",
			"ModfyTimedPrmtAccs");
		++liEventSet2;
		// end defect 10844 

		//84
		// 'EXEMPT AUTHORITY' 
		// defect 8900
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - EXEMPT AUTHORITY",
			"ExmptAuthAccs");
		// end defect 8900
		++liEventSet2;

		//85
		// 'TITLE APPLICATION' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - TITLE APPLICATION",
			"TtlApplAccs");
		++liEventSet2;

		//86
		// TtlRegAccs is set for every new user
		eventQryAppend(lsQry2, "TITLE/REGISTRATION", "TtlRegAccs");
		++liEventSet2;

		//87
		// 'TOW TRUCK'
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS REGISTRATION - TOW TRUCK",
			"TowTrkAccs");
		++liEventSet2;

		// defect 9124
		//88
		// 'UNACCEPTABLE' 
		eventQryAppend(
			lsQry2,
			"SPECIAL PLATES - UNACCEPTABLE",
			"SpclPltUnAccptblPltAccs");
		++liEventSet2;
		// end defect 9124

		//89
		// 'INVENTORY INQUIRY' 
		eventQryAppend(lsQry2, "INVENTORY - INQUIRY", "InvInqAccs");
		++liEventSet2;

		//90
		// 'TITLE REVOKED' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - STATUS CHANGE - TITLE REVOKED",
			"TtlRevkdAccs");
		++liEventSet2;

		//91
		// 'TITLE SURRENDER' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - STATUS CHANGE - TITLE SURRENDERED",
			"TtlSurrAccs");
		++liEventSet2;

		//92
		// 'VEHICLE JUNKED' 
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - STATUS CHANGE - VEHICLE JUNKED",
			"JnkAccs");
		++liEventSet2;

		//92a
		// defect 10153
		// 'PRIVATE LAW ENFORCEMENT VEHICLE'
		eventQryAppend(
			lsQry2,
			"TITLE/REGISTRATION - PRIVATE LAW ENFORCEMENT VEHICLE",
			"PrivateLawEnfVehAccs");
		++liEventSet2;
		// end defect 10153

		//93
		// 'DELETE' 
		eventQryAppend(lsQry2, "INVENTORY - DELETE", "InvDelAccs");
		++liEventSet2;

		//94
		// 'VOID TRANSACTION' 
		eventQryAppend(
			lsQry2,
			"MISCELLANEOUS - VOID TRANSACTION",
			"VoidTransAccs");
		++liEventSet2;

		//95
		// INVENTORY - PROFILE 
		eventQryAppend(
			lsQry2,
			"INVENTORY - PROFILE AND REPORT",
			"InvProfileAccs");
		++liEventSet2;

		//96
		// INVENTORY - HOLD/RELEASE 
		eventQryAppend(
			lsQry2,
			"INVENTORY - HOLD/RELEASE",
			"InvHldRlseAccs");
		++liEventSet2;

		for (int i = 0; i < liEventSet1; i++)
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
		}

		for (int i = 0; i < liEventSet2; i++)
		{
			lvValues2.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(
						aaGeneralSearchData.getIntKey1())));
		}

		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qryEventSecurityReport (EventSet1) - SQL - Begin ");

			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryEventSecurityReport (EventSet1) - SQL - End ");
			while (laResultSet.next())
			{
				EventSecurityReportData laEventSecurityReportData =
					new EventSecurityReportData();
				laEventSecurityReportData.setEmpId(
					caDA.getStringFromDB(laResultSet, "EmpId"));
				laEventSecurityReportData.setUserName(
					caDA.getStringFromDB(laResultSet, "UserName"));
				laEventSecurityReportData.setEmpLastName(
					caDA.getStringFromDB(laResultSet, "EmpLastName"));
				laEventSecurityReportData.setEmpFirstName(
					caDA.getStringFromDB(laResultSet, "EmpFirstName"));
				laEventSecurityReportData.setEmpMI(
					caDA.getStringFromDB(laResultSet, "EmpMI"));
				laEventSecurityReportData.setEventName(
					caDA.getStringFromDB(laResultSet, "EventName"));
				// Add element to the Vector
				lvRslt.addElement(laEventSecurityReportData);
			} //End of While
			laResultSet.close();
			laResultSet = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryEventSecurityReport (EventSet1) - End  ");

			Log.write(
				Log.SQL,
				this,
				" - qryEventSecurityReport (EventSet2) - SQL - Begin ");
			laResultSet =
				caDA.executeDBQuery(lsQry2.toString(), lvValues2);
			Log.write(
				Log.SQL,
				this,
				" - qryEventSecurityReport (EventSet2) - SQL - End ");
			while (laResultSet.next())
			{
				EventSecurityReportData laEventSecurityReportData =
					new EventSecurityReportData();
				laEventSecurityReportData.setEmpId(
					caDA.getStringFromDB(laResultSet, "EmpId"));
				laEventSecurityReportData.setUserName(
					caDA.getStringFromDB(laResultSet, "UserName"));
				laEventSecurityReportData.setEmpLastName(
					caDA.getStringFromDB(laResultSet, "EmpLastName"));
				laEventSecurityReportData.setEmpFirstName(
					caDA.getStringFromDB(laResultSet, "EmpFirstName"));
				laEventSecurityReportData.setEmpMI(
					caDA.getStringFromDB(laResultSet, "EmpMI"));
				laEventSecurityReportData.setEventName(
					caDA.getStringFromDB(laResultSet, "EventName"));
				// Add element to the Vector
				lvRslt.addElement(laEventSecurityReportData);
			} //End of While

			UtilityMethods.sort(lvRslt);
			laResultSet.close();
			laResultSet = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryEventSecurityReport (EventSet2) - End  ");
			// defect 7116 
			return (lvRslt);
		}
		// defect 10844
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryEventSecurityReport - Exception  "
					+ aeRTSEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeRTSEx);
		}
		// end defect 10844 
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryEventSecurityReport - Exception  "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}

	} //END OF QUERY METHOD

	/**
	 * Method to Query SubstationSubscription, Substation
	 * 
	 * @param aiOfcIssuanceNo int
	 * @return Vector
	 * @throws RTSException
	 */
	public Vector qryPublishingReport(int aiOfcIssuanceNo)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qryPublishingReport - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvRslt = new Vector();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		lsQry.append(
			"SELECT "
				+ "A.OfcIssuanceNo,"
				+ "A.SubstaId,"
				+ "SubstaName,"
				+ "TblName,"
				+ "TblSubstaId,"
				+ "TblUpdtIndi "
				+ "FROM RTS.RTS_SUBSTA_SUBSCR A, RTS.RTS_SUBSTA B "
				+ "WHERE A.OFCISSUANCENO = B.OFCISSUANCENO AND "
				+ "A.SUBSTAID = B.SUBSTAID AND A.SUBSTAID <> "
				+ "A.TblSubstaId and A.OfcIssuanceno = ? "
				+ "ORDER BY A.SUBSTAID, A.TBLNAME ");
		try
		{
			lvValues.addElement(
				new DBValue(
					Types.INTEGER,
					DatabaseAccess.convertToString(aiOfcIssuanceNo)));
			Log.write(
				Log.SQL,
				this,
				" - qryPublishingReport - SQL - Begin");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qryPublishingReport - SQL - End");
			while (laResultSet.next())
			{
				PublishingReportData laPublishingReportData =
					new PublishingReportData();
				laPublishingReportData.setOfcIssuanceNo(
					caDA.getIntFromDB(laResultSet, "OfcIssuanceNo"));
				laPublishingReportData.setSubstaId(
					caDA.getIntFromDB(laResultSet, "SubstaId"));
				laPublishingReportData.setSubstaName(
					caDA.getStringFromDB(laResultSet, "SubstaName"));
				laPublishingReportData.setTblName(
					caDA.getStringFromDB(laResultSet, "TblName"));
				laPublishingReportData.setTblSubstaId(
					caDA.getIntFromDB(laResultSet, "TblSubstaId"));
				laPublishingReportData.setTblUpdtIndi(
					caDA.getIntFromDB(laResultSet, "TblUpdtIndi"));
				// Add element to the Vector
				lvRslt.addElement(laPublishingReportData);
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(
				Log.METHOD,
				this,
				" - qryPublishingReport - End ");
			return (lvRslt);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qryPublishingReport - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD

	/**
	 * Method to Determine SubstaId of Security Data
	 * 
	 * @param aaGeneralSearchData GeneralSearchData
	 * @return Vector
	 * @throws RTSException	
	 */
	public int qrySecuritySubstaId(GeneralSearchData aaGeneralSearchData)
		throws RTSException
	{
		Log.write(Log.METHOD, this, " - qrySecuritySubstaId - Begin");
		StringBuffer lsQry = new StringBuffer();
		Vector lvValues = new Vector();
		ResultSet laResultSet;
		int liSubstaId = Integer.MIN_VALUE;
		lsQry.append(
			"SELECT TblSubstaId  "
				+ " from RTS.RTS_SUBSTA_SUBSCR "
				+ "where "
				+ " TblName = 'RTS_SECURITY' and "
				+ " OfcIssuanceNo = ? and  "
				+ " SubstaId = ? ");
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey1())));
		lvValues.addElement(
			new DBValue(
				Types.INTEGER,
				DatabaseAccess.convertToString(
					aaGeneralSearchData.getIntKey2())));
		try
		{
			Log.write(
				Log.SQL,
				this,
				" - qrySecuritySubstaId - SQL - Begin");
			laResultSet =
				caDA.executeDBQuery(lsQry.toString(), lvValues);
			Log.write(
				Log.SQL,
				this,
				" - qrySecuritySubstaId - SQL - End");
			while (laResultSet.next())
			{
				liSubstaId =
					caDA.getIntFromDB(laResultSet, "TblSubstaId");
			} //End of While 
			laResultSet.close();
			laResultSet = null;
			Log.write(
				Log.METHOD,
				this,
				" - qrySecuritySubstaId - End ");
			if (liSubstaId == Integer.MIN_VALUE)
			{
				liSubstaId = aaGeneralSearchData.getIntKey2();
			}
			return (liSubstaId);
		}
		catch (SQLException aeSQLEx)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				" - qrySecuritySubstaId - Exception "
					+ aeSQLEx.getMessage());
			throw new RTSException(RTSException.DB_ERROR, aeSQLEx);
		}
	} //END OF QUERY METHOD
}
