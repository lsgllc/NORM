package com.txdot.isd.rts.server.systemcontrolbatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com
	.txdot
	.isd
	.rts
	.services
	.reports
	.inventory
	.GenBatchInventoryActionReport;
import com
	.txdot
	.isd
	.rts
	.services
	.reports
	.reports
	.GenSetAsideTransactionReport;
import com.txdot.isd.rts.services.reports.reports.GenTitlePackageReport;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.SystemControlBatchConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.server.reports.ReportsServerBusiness;

/*
 *
 * SystemControlBatchServerBusiness.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Hicks		12/19/2001	Moved endTransaction() to finally clause  in
 *							getMaxTransDate()
 * MAbs			04/12/2002	Fixing error in Susbstation Summary report 
 *							defect 3455
 * MAbs			04/18/2002	Fixing error in County Wide Report 
 *							defect 3455
 * Jeff S.		02/25/2004	Added function ID to let through to the
 *							server when DB is down.  This is for
 *							redirecting batch outside of the current
 *							substation. (county wide). Used in Print.
 *							setConnectionStatus(,)
 *							modify processData(int, int, Object)
 *							add getWsIds(Object, DatabaseAccess)
 *							(copied from InventoryServerBusiness)
 *							defect 6848, 6894 Ver 5.1.6
 * Jeff S.		03/05/2004	Created method that updates the
 *							RTS_WS_Status with the current workstation
 *							status only if it has changed.
 *							modify processData(int, int, Object)
 *							add updateWorkstationStatus(Object)
 *							deprecate getMaxLogDate(Object) defect 6614
 *							defect 6918 Ver 5.1.6
 * K Harrell	03/18/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							modify genBatchInvActionRpt(); Add code for 
 * 							BIAR part J/K
 * 							Ver 5.2.0
 * Ray Rowehl	05/17/2004	Update call to start SendTrans Tester.
 *							modify processData()
 *							defect 6785 Ver 5.2.0
 * K Harrell	11/24/2004	Return TransactionData object vs. RTSDate
 *							Also, cleaned up variable names
 *							modify getMaxTransDate()
 *							defect 7747 Ver 5.2.2 
 * B Hargrove	05/03/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7897 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3 
 * Ray Rowehl	09/30/2005	Moved InventoryActionReportSQL to server.db
 * 							since this is a sql class.
 * 							modified imports.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	11/08/2005	DB Processing Cleanup.  Removed reference
 * 							to MAX_LOG.
 * 							add BIAR_FILE_NAME,UNKNOWN_INVLOCIDCD,
 * 							REISSUE_VOIDED_INVLOCIDCD
 * 							Use ReportConstant.RPT_7_COPIES
 * 							delete getMaxLogData()
 * 							defect 7897 Ver 5.2.3 
 * Ray Rowehl	08/02/2006	Handle StaleConnection when checking for 
 * 							max trans for SendCache.
 * 							add csClientHost
 * 							add SystemControlBatchServerBusiness(String)
 * 							modify getMaxTransDate()
 * 							defect 8869 Ver 5.2.4  
 * Ray Rowehl	08/03/2006	Re-order add to rtsexception message.
 * 							modify getMaxTransDate()
 * 							defect 8869 Ver 5.2.4 
 * Ray Rowehl	08/04/2005	Move the beginTranssaction done into the 
 * 							loop.  Also write the staleconnection to 
 * 							the log.
 * 							modify getMaxTransDate()
 * 							defect 8869 Ver 5.2.4 
 * Ray Rowehl	09/11/2006	Modify Batch update to allow for handling 
 * 							of db2 restricted mode.
 * 							Do some code cleanup.
 * 							Add braces to case statement in processData().
 * 							add updateBatch(int, Object)
 * 							delete updateBatch(Object) 
 * 							modify processData()
 * 							defect 8923 Ver 5.2.5
 * Ray Rowehl	09/30/2006	Re-do processData.  
 * 							Dropping in FallAdminTables.
 * 							modify processData()
 * 							defect 8923 Ver 5.2.5
 * K Harrell	05/09/2007	add logic for generation of Special Plates
 * 							 Application Batch Report
 * 							add genSpecialPlatesReport()
 * 							modify processData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/20/2007	Corrected Batch Name for above.
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/26/2007	add logic to complete the inventory associated
 * 							with setAside Print Immediate transacctions
 * 							modify getSetAsideReport()
 * 							defect 9805 Ver Special Plates   
 * 							defect 9085 Ver Special Plates
 * Ray Rowehl	06/27/2007	Server Side batch should use the batch id
 * 							and password.  This was missed in 8923.
 * 							Also setup to set DataBaseAccess back
 * 							to null;
 * 							modify getBatchProcesses(), updateBatch()
 * 							defect 9116 Ver Special Plates
 * K Harrell	08/02/2007	Sort OfcIssuanceNo vector for Special Plates
 * 							Report.
 * 							modify genSpecialPlatesReport()
 * 							defect 9207 Ver Special Plates
 * K Harrell	08/05/2007	Omit 257 in list of offices for batch report
 *							modify genSpecialPlatesReport()
 * 							defect 9219 Ver Special Plates
 * K Harrell	02/09/2009	Remove call for Special Plates Report - never
 * 							 implemented.
 * 							delete genSpecialPlatesReport()
 * 							modify processData() 
 * 							defect 9941 Ver Defect_POS_D 		
 * K Harrell	08/17/2009	Implement new ReportSearchData constructor
 * 							add initReportProperties()  
 * 							modify genBatchInvActionRpt(), 
 * 							 genSetAsideReport(), 
 * 							 genTitlePackageReport()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	08/27/2009	Modify to use refactored TransHdr/Trans 
 * 							 queries 
 * 							modify genSetAsideReport()
 * 							defect 10184 Ver Defect_POS_F 	
 * K Harrell	03/29/2010	Only retry getMaxTransDate on error.
 * 							Return Boolean on updateWorkstationStatus()  
 * 							modify getMaxTransDate(), 
 * 							 updateWorkstationStatus()  
 * 							defect 8087 Ver POS_640 	
 * K Harrell	08/19/2011	Implement new BatchScheduleData constructor 
 * 							modify getBatchProcesses() 	
 * 							defect 10976 Ver 6.8.1
 * K Harrell	06/30/2012	modify getMaxTransDate() 
 * 							defect 11073 Ver 7.0.0 
 * ---------------------------------------------------------------------
 */

/**
 * Handles the server side logic for System Control/Batch
 *
 * @version	7.0.0	06/30/2012
 * @author	Michael Abernethy	
 * @since 			11/05/2001 15:58:53	
 */

public class SystemControlBatchServerBusiness
{
	private static final String BIAR_FILE_NAME = "IARBAT";
	private static final String UNKNOWN_INVLOCIDCD = "U";
	private static final String REISSUE_VOIDED_INVLOCIDCD = "V";
	private String csClientHost = CommonConstant.TXT_UNKNOWN;

	/**
	 * Creates a SystemControlBatchServerBusiness 
	 */
	public SystemControlBatchServerBusiness()
	{
		super();
	}

	/**
	 * Creates a SystemControlBatchServerBusiness 
	 * 
	 * @param String asClientHost
	 */
	public SystemControlBatchServerBusiness(String asClientHost)
	{
		super();
		csClientHost = asClientHost;
	}

	/**
	 * Generate the Batch Inventory Action Report (BIAR).
	 * 
	 * @param Object aaData
	 * @throws RTSException
	 * @return Object 
	 */
	private Object genBatchInvActionRpt(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		int liOfcNo = laRptSearchData.getIntKey1();
		int liSubstaId = laRptSearchData.getIntKey2();
		String lsRequestAMDate = laRptSearchData.getKey2();

		// Database Access 
		DatabaseAccess laDBAccess = new DatabaseAccess();

		try
		{
			// defect 8628 
			// UOW #1 BEGIN
			// REPORT PROPERTIES 
			ReportProperties laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBAccess,
					ReportConstant.BIAR_REPORT_ID);
			// UOW #1 END 
			// end defect 8628 

			// GEN REPORT
			GenBatchInventoryActionReport laBIAR =
				new GenBatchInventoryActionReport(
					ReportConstant.BIAR_REPORT,
					laRptProps);

			RTSDate laReportDate =
				new RTSDate(
					RTSDate.AMDATE,
					Integer.parseInt(lsRequestAMDate));

			// Setup Vector to pass Data Objects  
			Vector lvReportData = new Vector();
			lvReportData.addElement(laReportDate.toString());
			Vector lvRptData = new Vector();

			// Begin BIAR SQL
			// Using existing laRptSearchData as using assigned  
			//  OfcIssuanceNo, SubstaId, AMDate 
			// Part A: Issued and not removed  
			laRptSearchData.setKey1(UNKNOWN_INVLOCIDCD);

			// UOW #2 BEGIN
			InventoryActionReportSQL laInvRptSQL =
				new InventoryActionReportSQL(laDBAccess);
			laDBAccess.beginTransaction();
			lvRptData = laInvRptSQL.qryIARPartAB(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END

			lvReportData.addElement(lvRptData);
			laBIAR.partA(lvReportData);

			// Part B: Re-Issue Voided Inventory 
			laRptSearchData.setKey1(REISSUE_VOIDED_INVLOCIDCD);
			lvReportData.removeElementAt(1);
			// UOW #3 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData = laInvRptSQL.qryIARPartAB(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #3 END
			lvReportData.addElement(lvRptData);
			laBIAR.partB(lvReportData);

			// Part C: Voided 
			lvReportData.removeElementAt(1);
			// UOW #4 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData = laInvRptSQL.qryIARPartC(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #4 END
			lvReportData.addElement(lvRptData);
			laBIAR.partC(lvReportData);

			// Part D: On Hold due to Technical Problem 
			laRptSearchData.setIntKey3(2);
			//status code - used in D and E
			lvReportData.removeElementAt(1);
			// UOW #5 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData = laInvRptSQL.qryIARPartDE(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #5 END
			lvReportData.addElement(lvRptData);
			laBIAR.partD(lvReportData);

			// Part E: On User Hold 
			lvReportData.removeElementAt(1);
			laRptSearchData.setIntKey3(1);
			// UOW #6 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData = laInvRptSQL.qryIARPartDE(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #6 END
			lvReportData.addElement(lvRptData);
			laBIAR.partE(lvReportData);

			// Part F: Below Minimum 
			lvReportData.removeElementAt(1);
			// UOW #7 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData = laInvRptSQL.qryIARPartF(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #7 END
			lvReportData.addElement(lvRptData);
			laBIAR.partF(lvReportData);

			// Part G: Above Maximum 
			lvReportData.removeElementAt(1);
			// UOW #8 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData = laInvRptSQL.qryIARPartG(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #8 END
			lvReportData.addElement(lvRptData);
			laBIAR.partG(lvReportData);

			// Part H: Deleted Using Delete Event 
			lvReportData.removeElementAt(1);
			// UOW #9 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData = laInvRptSQL.qryIARPartH(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #9 END
			lvReportData.addElement(lvRptData);
			laBIAR.partH(lvReportData);

			// Part I: Mismatched 
			lvReportData.removeElementAt(1);
			// UOW #10 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData = laInvRptSQL.qryIARPartI(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #10 END
			lvReportData.addElement(lvRptData);
			laBIAR.partI(lvReportData);

			// Part J: Reprinted on POS 
			lvReportData.remove(1);
			// UOW #11 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData =
				laInvRptSQL.qryIARPartJ(
					laRptSearchData,
					RTSDate.getCurrentDate().add(RTSDate.DATE, -1));
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #11 END
			lvReportData.addElement(lvRptData);
			laBIAR.partJ(lvReportData);

			// Part K: Printed at RSPS 
			lvReportData.remove(1);
			// UOW #12 BEGIN 
			laDBAccess.beginTransaction();
			lvRptData =
				laInvRptSQL.qryIARPartK(
					laRptSearchData,
					RTSDate.getCurrentDate().add(RTSDate.DATE, -1));
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #12 END
			lvReportData.addElement(lvRptData);
			laBIAR.partK(lvReportData);

			InventoryAllocation laInvAllc =
				new InventoryAllocation(laDBAccess);
			InventoryAllocationData laInvAllocData =
				new InventoryAllocationData();
			laInvAllocData.setOfcIssuanceNo(liOfcNo);
			laInvAllocData.setSubstaId(liSubstaId);
			// UOW #13 BEGIN 
			laDBAccess.beginTransaction();
			laInvAllc.updInventoryAllocationInvStatusCd(laInvAllocData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #13 END

			// defect 8628
			// RETURN REPORT DATA/ATTRIBUTES 
			return new ReportSearchData(
				laBIAR.getFormattedReport().toString(),
				BIAR_FILE_NAME,
				ReportConstant.BIAR_REPORT,
				ReportConstant.RPT_7_COPIES,
				ReportConstant.PORTRAIT);
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Generate the Set-Aside Report 
	 * 
	 * @param Object aaData
	 * @return Object 
	 * @throws RTSException
	 */
	private Object genSetAsideReport(Object aaData) throws RTSException
	{
		// Instantiating a new Report Class 
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		int liOfcNo = laRptSearchData.getIntKey1();
		int liSubstaId = laRptSearchData.getIntKey2();
		int liToday = new RTSDate().getAMDate();
		laRptSearchData.setIntKey4(liToday);

		Vector lvRptData = new Vector();

		DatabaseAccess laDBAccess = new DatabaseAccess();

		// defect 8628 
		ReportProperties laRptProps = new ReportProperties();

		try
		{
			// UOW #1 BEGIN
			// REPORT PROPERTIES 
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBAccess,
					ReportConstant.COMPL_SETASIDE_REPORT_ID);
			// UOW #1 END
			// end defect 8628   

			// INCOMPLETE TRANS 
			// UOW #2 BEGIN
			laDBAccess.beginTransaction();
			Transaction laTrans = new Transaction(laDBAccess);
			// defect 10184 
			// Implemented method with more descriptive name 
			lvRptData =
				laTrans.qryPrintImmediateIncomplTrans(laRptSearchData);
			// end defect 10184
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 	

			if (lvRptData.size() > 0)
			{
				InventoryAllocationData laInvAllocData =
					new InventoryAllocationData();
				laInvAllocData.setOfcIssuanceNo(liOfcNo);
				laInvAllocData.setSubstaId(liSubstaId);
				laInvAllocData.setTransAmDate(liToday);

				TransactionHeaderData laTransHdrData =
					new TransactionHeaderData();
				laTransHdrData.setOfcIssuanceNo(liOfcNo);
				laTransHdrData.setSubstaId(liSubstaId);
				laTransHdrData.setTransAMDate(liToday);

				// UOW #3 BEGIN
				laDBAccess.beginTransaction();
				InventoryVirtual laInvVirtual =
					new InventoryVirtual(laDBAccess);
				// 1st Complete the Virtual Inventory Request 
				laInvVirtual.updInventoryVirtualForBatch(
					laInvAllocData);

				// 2nd Complete the Transaction
				TransactionHeader laTransHdr =
					new TransactionHeader(laDBAccess);
				// defect 10184 
				// Implemented method with more descriptive name
				laTransHdr.updPrintImmediateIncomplTransHeader(
					laTransHdrData);
				// end defect 10184 
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #3 END
			}
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// GEN REPORT 
		GenSetAsideTransactionReport laGenSATransRpt =
			new GenSetAsideTransactionReport(
				ReportConstant.COMPL_SETASIDE_REPORT_TITLE,
				laRptProps);
		laGenSATransRpt.formatReport(lvRptData);

		// defect 8628 
		// RETURN REPORT DATA/ATTRIBUTES 
		return new ReportSearchData(
			laGenSATransRpt.getFormattedReport().toString(),
			ReportConstant.COMPL_SETASIDE_REPORT_FILENAME,
			ReportConstant.COMPL_SETASIDE_REPORT_TITLE,
			ReportConstant.RPT_7_COPIES,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Generate the Title Package Report 
	 * 
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object genTitlePackageReport(Object aaData)
		throws RTSException
	{
		ReportSearchData laRptSearchData = (ReportSearchData) aaData;
		Vector lvRptData = new Vector();

		DatabaseAccess laDBAccess = new DatabaseAccess();

		// defect 8628 
		ReportProperties laRptProps = new ReportProperties();

		try
		{
			// UOW #1 BEGIN 
			// REPORT PROPERTIES
			laRptProps =
				initReportProperties(
					laRptSearchData,
					laDBAccess,
					"RTS.POS.5911");
			// UOW #1 END 	
			// end defect 8628 

			// UOW #2 BEGIN
			laDBAccess.beginTransaction();
			TitlePackageReportSQL laTtlPkgRptSQL =
				new TitlePackageReportSQL(laDBAccess);
			lvRptData =
				laTtlPkgRptSQL.qryTitlePackageReport(laRptSearchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		// GEN REPORT 
		GenTitlePackageReport laGenTtlPRpt =
			new GenTitlePackageReport(
				"TITLE PACKAGE REPORT",
				laRptProps);
		Vector lvReportSelection = laRptSearchData.getVector();
		Vector lvReportInput = new Vector();
		lvReportInput.addElement(lvRptData);
		lvReportInput.addElement(lvReportSelection);
		lvReportInput.addElement(laRptSearchData.getKey2());
		laGenTtlPRpt.formatReport(lvReportInput);

		// defect 8628 
		// RETURN REPORT DATA/ATTRIBUTES 
		return new ReportSearchData(
			laGenTtlPRpt.getFormattedReport().toString(),
			ReportConstant.BATCH_TITLE_PACKAGE_REPORT_FILENAME,
			ReportConstant.BATCH_TITLE_PACKAGE_REPORT_TITLE,
			ReportConstant.RPT_7_COPIES,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Returns the Batch Processes to run on the client
	 * 
	 * @return Object
	 * @param aaData Object
	 * @throws RTSException
	 */
	private Object getBatchProcesses(Object aaData) throws RTSException
	{
		HashMap lhmHash = (HashMap) aaData;
		int liOfcNo = ((Integer) lhmHash.get("OFC")).intValue();
		int liSubstaId = ((Integer) lhmHash.get("SUB")).intValue();

		// defect 9116
		// Server Side batch should use batch id and password.
		// DatabaseAccess 
		DatabaseAccess laDBAccess = null;

		try
		{
			if (liOfcNo != 0)
			{
				// this is client side batch.
				laDBAccess = new DatabaseAccess();
			}
			else
			{
				// this is server side batch
				laDBAccess =
					new DatabaseAccess(
						SystemProperty.getDBUserBatch(),
						SystemProperty.getDBPasswordBatch());
			}
			// end defect 9116

			BatchSchedule laBatch = new BatchSchedule(laDBAccess);

			Log.write(
				Log.APPLICATION,
				this,
				"Starting DB call to BatchSchedule");

			// defect 10976 
			BatchScheduleData laData = new BatchScheduleData(liOfcNo,liSubstaId);
			//laData.setOfcIssuanceNo(liOfcNo);
			//laData.setSubStaId(liSubstaId);
			// end defect 10976 

			// UOW #1 BEGIN
			laDBAccess.beginTransaction();
			Vector lvVector = laBatch.qryBatchSchedule(laData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			Log.write(
				Log.APPLICATION,
				this,
				"Successful DB call to BatchSchedule");
			return lvVector;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to BatchSchedule");
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Returns Transaction object with Max TransAMDate, TransTime for
	 *  given Ofcissuanceno, Substaid, TransWsId 
	 * 
	 * @param Object aaData
	 * @return TransactionData
	 * @throws RTSException 
	 */
	private TransactionData getMaxTransDate(Object aaData)
		throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		Transaction laTransaction = new Transaction(laDBAccess);
		try
		{
			// defect 7747
			// Return TransactionData object vs. RTSDate to avoid date conversion
			// issues. 
			Map laMap = (Map) aaData;
			int liOfcNo = ((Integer) laMap.get("OFC")).intValue();
			int liSubstaId = ((Integer) laMap.get("SUB")).intValue();
			int liWsId = ((Integer) laMap.get("WSID")).intValue();
			Log.write(
				Log.APPLICATION,
				this,
				"Starting DB call to Transaction");

			TransactionData laTransactionData = new TransactionData();
			laTransactionData.setOfcIssuanceNo(liOfcNo);
			laTransactionData.setSubstaId(liSubstaId);
			laTransactionData.setTransWsId(liWsId);

			// defect 8869
			Vector lvResults = new Vector();

			// Retry ONLY on StaleConnectionException
			for (int i = 0; i < 2; i++)
			{
				try
				{
					// UOW #1 BEGIN 
					laDBAccess.beginTransaction();

					lvResults =
						laTransaction.qryMaxTransaction(
							laTransactionData);

					// defect 8087 
					break;
					// end defect 8087 
				}
				catch (RTSException aeRTSEx1)
				{
					if (aeRTSEx1.getDetailMsg() != null
						&& aeRTSEx1.getDetailMsg().indexOf(
							"StaleConnection")
							> -1)
					{
						// log the exception
						aeRTSEx1.writeExceptionToLog();
						// re-establish connection.
						laDBAccess = new DatabaseAccess();
						laTransaction = new Transaction(laDBAccess);
						laDBAccess.beginTransaction();
					}
					else
					{
						// add the client host to the message detail
						aeRTSEx1.setDetailMsg(
							aeRTSEx1.getDetailMsg()
								+ CommonConstant.SYSTEM_LINE_SEPARATOR
								+ "Client "
								+ csClientHost);
						throw aeRTSEx1;
					}
				}
			}
			// end defect 8869

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			// defect 11073 
			//laTransactionData = (TransactionData) lvResults.get(0);
			laTransactionData = (TransactionData) lvResults.lastElement();
			// end defect 11073 
			
			Log.write(
				Log.APPLICATION,
				this,
				"Successful DB call to Transaction");
			return laTransactionData;
			//end defect 7747 
		}
		catch (RTSException aeRTSEx)
		{
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to Transaction");
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/**
	 * Returns the start time from the BATCH_SCHEDULE table
	 * 
	 * @return java.lang.Object
	 * @param Object aaData
	 * @throws RTSException 
	 */
	private Object getStartTime(Object aaData) throws RTSException
	{
		Map laMap = (Map) aaData;
		int liOfcNo = ((Integer) laMap.get("OFC")).intValue();
		int liSubstaId = ((Integer) laMap.get("SUB")).intValue();

		DatabaseAccess laDBAccess = new DatabaseAccess();
		BatchSchedule laBatch = new BatchSchedule(laDBAccess);
		try
		{
			Log.write(
				Log.APPLICATION,
				this,
				"Starting DB call to BatchSchedule");

			// defect 10976 
			BatchScheduleData laBatchData = new BatchScheduleData(liOfcNo,liSubstaId,0);
			// laBatchData.setOfcIssuanceNo(liOfcNo);
			// laBatchData.setSubStaId(liSubstaId);
			// laBatchData.setJobSeqNo(0);
			// end defect 10976 

			// UOW #1 BEGIN 		
			laDBAccess.beginTransaction();
			Vector lvVector = laBatch.qryBatchSchedule(laBatchData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			if (lvVector == null || lvVector.size() == 0)
			{
				throw new RTSException();
			}
			BatchScheduleData laResultData =
				(BatchScheduleData) lvVector.get(0);
			Integer liStartTime =
				new Integer(laResultData.getStartTime());
			Log.write(
				Log.APPLICATION,
				this,
				"Successful DB call to BatchSchedule");
			return liStartTime;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to BatchSchedule");
			throw aeRTSEx;
		}

	}

	/**
	 * Gets the substation information to be used in the Substation 
	 * Summary Report
	 * 
	 * @param Object aaData
	 * @return Object
	 * @throws RTSException
	 */
	private Object getSubstaInfo(Object aaData) throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		CloseOutHistory laCloseoutHistory =
			new CloseOutHistory(laDBAccess);

		try
		{
			Log.write(
				Log.APPLICATION,
				this,
				"Starting DB call to CloseOutHistory");

			Map laMap = (Map) aaData;
			int liOfcNo = ((Integer) laMap.get("OFC")).intValue();
			int liSubstaId = ((Integer) laMap.get("SUB")).intValue();

			CloseOutHistoryData laClsHstryData =
				new CloseOutHistoryData();
			laClsHstryData.setOfcIssuanceNo(liOfcNo);
			laClsHstryData.setSubstaId(liSubstaId);

			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();
			int liAMDate =
				laCloseoutHistory.qryCloseOutHistoryMaxCloseDate(
					laClsHstryData);

			RTSDate laDate = new RTSDate(RTSDate.AMDATE, liAMDate);
			Vector lvDrawers =
				laCloseoutHistory.qryCloseOutHistory(laClsHstryData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			FundsData laFundsData = new FundsData();

			// defect 8628
			laFundsData.setOfficeIssuanceNo(liOfcNo);
			laFundsData.setSubStationId(liSubstaId);
			// end defect 8628

			laFundsData.setSummaryEffDate(laDate);
			laFundsData.setSelectedCashDrawers(lvDrawers);
			Log.write(
				Log.APPLICATION,
				this,
				"Successful DB call to CloseOutHistory");
			return laFundsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to CloseOutHistory");
			throw aeRTSEx;
		}
	}

	/**
	 * Returns the number of Title Packages run
	 * 
	 * @param Object aaData
	 * @return Object 
	 * @throws RTSException
	 */
	private Object getTitleNum(Object aaData) throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();
		Transaction laTrans = new Transaction(laDBAccess);
		try
		{
			Map laMap = (Map) aaData;
			int liOfcNo = ((Integer) laMap.get("OFC")).intValue();
			int liSubstaId = ((Integer) laMap.get("SUB")).intValue();
			RTSDate laDate = (RTSDate) laMap.get("DATE");

			TransactionData laTransData = new TransactionData();
			laTransData.setOfcIssuanceNo(liOfcNo);
			laTransData.setSubstaId(liSubstaId);
			laTransData.setTransAMDate(
				laDate.add(RTSDate.DATE, -1).getAMDate());
			Log.write(
				Log.APPLICATION,
				this,
				"Starting DB call to Transaction");

			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();
			int liTransCount =
				laTrans.qryTitleTransactionCount(laTransData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 	

			Log.write(
				Log.APPLICATION,
				this,
				"Successful DB call to Transaction");
			return new Integer(liTransCount);
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to Transaction");
			throw aeRTSEx;
		}
	}

	/**
	* Returns all the workstations given SubstationId and 
	* OfficeIssuanceNumber.
	*
	* @return Vector of workstation information in the form of
	*         AssignedWorkstationIdsData object.
	* @param aaData Object The search parameters in the form of 
	*		GeneralSearchData.  The
	*         required fields are as follows:
	*     <br>IntKey(1) - The office issuance number
	*     <br>IntKey(2) - The substation id
	* @throws RTSException 
	*/
	private Vector getWsIds(Object aaData) throws RTSException
	{
		Log.write(Log.METHOD, this, "getWsIds - Begin");
		Vector lvAssgndWsIdsData = new Vector();
		GeneralSearchData laGSData = (GeneralSearchData) aaData;
		int liOfcNo = laGSData.getIntKey1();
		int liSubstaId = laGSData.getIntKey2();

		DatabaseAccess laDBAccess = new DatabaseAccess();
		AssignedWorkstationIds laAssgndWsIds =
			new AssignedWorkstationIds(laDBAccess);

		try
		{
			AssignedWorkstationIdsData laAssgndWsIdsData =
				new AssignedWorkstationIdsData();
			laAssgndWsIdsData.setOfcIssuanceNo(liOfcNo);
			laAssgndWsIdsData.setSubstaId(liSubstaId);
			laAssgndWsIdsData.setWsId(Integer.MIN_VALUE);

			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();
			lvAssgndWsIdsData =
				laAssgndWsIds.qryAssignedWorkstationIds(
					laAssgndWsIdsData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			Log.write(Log.METHOD, this, "getWsIds - End");
			return lvAssgndWsIdsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
	}

	/** 
	 * Init ReportProperties
	 * 
	 * @param aaDBAccess
	 * @param aaRptSearchData
	 * @return ReportProperties
	 * @throws RTSException
	 */
	private ReportProperties initReportProperties(
		ReportSearchData aaRptSearchData,
		DatabaseAccess aaDBAccess,
		String asReportId)
		throws RTSException
	{
		ReportsServerBusiness laRptSvrBusiness =
			new ReportsServerBusiness();

		return laRptSvrBusiness.initReportProperties(
			aaRptSearchData,
			aaDBAccess,
			asReportId);
	}

	/**
	 * The interface method to the SystemControlBatchServerBusiness
	 * 
	 * @param aiModule int
	 * @param aiFunctionId int
	 * @param aaData Object
	 * @return java.lang.Object
	 * @throws RTSException 
	 */
	public Object processData(
		int aiModule,
		int aiFunctionId,
		Object aaData)
		throws RTSException
	{
		switch (aiFunctionId)
		{
			case SystemControlBatchConstant.MAX_TRANS :
				{
					return getMaxTransDate(aaData);
				}
			case SystemControlBatchConstant.GET_BATCH_PROCESS :
				{
					return getBatchProcesses(aaData);
				}
			case SystemControlBatchConstant.UPDATE_BATCH_RESULTS :
			case SystemControlBatchConstant
				.UPDATE_BATCH_RESULTS_SERVER :
				{
					return updateBatch(aiFunctionId, aaData);
				}
			case SystemControlBatchConstant.GEN_BATCH_REPORT :
				{
					return genBatchInvActionRpt(aaData);
				}
			case SystemControlBatchConstant.GEN_COMPLETE_SETASIDE :
				{
					return genSetAsideReport(aaData);
				}
			case SystemControlBatchConstant.GEN_TITLE_PACKAGE :
				{
					return genTitlePackageReport(aaData);
				}
			case SystemControlBatchConstant.GET_START_TIME :
				{
					return getStartTime(aaData);
				}
			case SystemControlBatchConstant.GET_TITLE_NUM :
				{
					return getTitleNum(aaData);
				}
			case SystemControlBatchConstant.GET_SUBSTA_INFO :
				{
					return getSubstaInfo(aaData);
				}
			case SystemControlBatchConstant.GET_WS_IDS :
				{
					// Added another function to GET_WS_IDS for redirecting
					// batch printouts
					return getWsIds(aaData);
				}
			case SystemControlBatchConstant.UPDATE_WORKSTATION_STATUS :
				{
					// Added update to RTS_WS_Status to update the db with the
					// current status of the WS - called from client.BatchProcessorServer
					return updateWorkstationStatus(aaData);
				}
			case SystemControlBatchConstant.TEST_SERVER_BATCH :
				{
					// Use constant 
					return test(aaData);
				}
			default :
				{
					return null;
				}
		}
	}

	/**
	 * Used by the RTSServerTester to test server side System Control 
	 * functionality.  The aaData object is a vector containing test 
	 * parameters
	 * 
	 * @param  Object aaData
	 * @return Object
	 * @throws RTSException 
	 */
	private Object test(Object aaData) throws RTSException
	{
		// defect 6785
		Vector lvTestParameters = (Vector) aaData;

		RTSServer laRTSServer = null;

		if (lvTestParameters.size() < 1)
		{
			laRTSServer = new RTSServer();
		}
		else
		{
			// we passed in some values.  Set up the initial parameters
			int liStartOfc = 0;
			int liEndOfc = 300;
			boolean lbR99Switch = false;

			// There is a start office number
			if (lvTestParameters.size() > 0)
			{
				liStartOfc =
					((Integer) lvTestParameters.elementAt(0))
						.intValue();
			}

			// There is an end office number
			if (lvTestParameters.size() > 1)
			{
				liEndOfc =
					((Integer) lvTestParameters.elementAt(1))
						.intValue();
			}

			// There is a boolean
			if (lvTestParameters.size() > 2)
			{
				lbR99Switch =
					((Boolean) lvTestParameters.elementAt(2))
						.booleanValue();
			}

			// instantiate with the new parameters
			laRTSServer =
				new RTSServer(liStartOfc, liEndOfc, lbR99Switch);
		}
		// end defect 6785

		laRTSServer.go();
		return new Vector();
	}

	/**
	 * Updates the BATCH_SCHEDULE table after the BatchProcessorServer has run
	 *
	 * @param aaData Object
	 * @return Object 
	 * @throws RTSException
	 */
	private Object updateBatch(int aiFunctionId, Object aaData)
		throws RTSException
	{
		// defect 8923
		// Use the db batch user id and password when updating the
		// status in db2 restricted mode.
		DatabaseAccess laDBAccess = null;

		try
		{
			if (aiFunctionId
				== SystemControlBatchConstant.UPDATE_BATCH_RESULTS_SERVER)
			{
				// updating status in db2 restricted mode
				laDBAccess =
					new DatabaseAccess(
						SystemProperty.getDBUserBatch(),
						SystemProperty.getDBPasswordBatch());
			}
			else
			{
				// updating status in db2 normal mode
				laDBAccess =
					new DatabaseAccess(
						SystemProperty.getDBUser(),
						SystemProperty.getDBPassword());
			}
			// end defect 8923
			BatchSchedule laBatch = new BatchSchedule(laDBAccess);

			Log.write(
				Log.APPLICATION,
				this,
				"Starting DB call to BatchSchedule");

			BatchScheduleData laData = (BatchScheduleData) aaData;

			laDBAccess.beginTransaction();
			if (laData.getJobSeqNo() == 0)
			{
				laBatch.updBatchSchedule(laData);
			}
			else
			{
				laBatch.updBatchScheduleJob(laData);
			}
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			Log.write(
				Log.APPLICATION,
				this,
				"Successful DB call to BatchSchedule");
			return new Vector();
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to BatchSchedule");
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Updates the RTS_WS_Status table with the workstations current 
	 * status
	 *
	 * @param aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	private Object updateWorkstationStatus(Object aaData)
		throws RTSException
	{
		DatabaseAccess laDBAccess = new DatabaseAccess();

		WorkstationStatus laWsStatus =
			new WorkstationStatus(laDBAccess);

		try
		{
			Log.write(
				Log.APPLICATION,
				this,
				"Starting DB call to RTS_WS_Status");

			WorkstationStatusData laWsData =
				(WorkstationStatusData) aaData;

			// UOW #1 BEGIN
			laDBAccess.beginTransaction();

			// defect 8087 
			// Returns Boolean vs. int 
			Boolean lbChanged =
				laWsStatus.updWorkstationStatus(laWsData);
			// end defect 8087 

			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			Log.write(
				Log.APPLICATION,
				this,
				"Successful DB call to RTS_WS_Status");

			// defect 8087 
			// Return Boolean vs. Vector
			
			// Vector lvReturnCode = new Vector();
			// lvReturnCode.addElement(new Integer(liReturnCode));
			// return lvReturnCode;
			
			return lbChanged;
			// end defect 8087 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			Log.write(
				Log.APPLICATION,
				this,
				"Failed DB call to RTS_WS_Status");
			throw aeRTSEx;
		}
	}
}