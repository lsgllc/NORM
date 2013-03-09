package com.txdot.isd.rts.server.funds;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeTimeZoneCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.funds.*;
import com.txdot.isd.rts.services.util.Log;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

import com.txdot.isd.rts.server.common.business.CommonServerBusiness;
import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.*;
import com.txdot.isd.rts.server.reports.ReportsServerBusiness;

/* 
 * FundsServerBusiness.java
 * 
 * (c) Texas Department of Transportation  2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * BTulsiani	04/16/2002	Made substation summary synchronized and 
 *							removed system.out in payment report
 * MAbs			04/18/2002 	Fixing error in County Wide Report 
 * 							defect 3455
 * RHicks 		10/07/2002	Remove synchronize from method signature
 *							of generateSubstationSummaryReport()
 * S Govindappa 10/09/2002  Changed genPaymentReportShowSource()
 *                          to update the current timestamp in the 
 * 							database when "show source of money"
 *							is selected on FUN006 screen.
 *							defect 4697. 
 * K Harrell  	10/18/2002  Substation Summary Performance: 
 * 							generateSubstationSummaryReport
 * 							defect 4912 
 * J Seifert  	06/23/2003  add Log.Write statements to track when 
 * 							DB calls occur
 * 							defect 6168
 * J Seifert	06/02/2003	Changed the reports filename from PMT to
 * 							PMC and also the report description to 
 * 							"Current Status" - genPaymentReportShowSource()
 *							defect 5559
 * Ray Rowehl	01/05/2004	Use static constants in case statements
 * 							Access DBA constants in static.
 * 							modify genPaymentReport(), 
 * 							genPaymentReportShowSource()
 * 							updateCurrentStatus()
 * 							defect 6735  Ver 5.1.5  Fix 2
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify sortInventoryResults
 * 							Ver 5.2.0
 * K Harrell	05/11/2004	Correct reference to insInventorySummary...
 *							fromTrInvDetailOld. Merge continued. 
 *							modify generateSubstationSummaryReport()
 *							Ver 5.2.0
 * K Harrell	06/20/2004	Really correct reference FromTrInvDetailOld
 *							modify generateSubstationSummaryReport()
 *							defect 7226 Ver 5.2.0
 * K Harrell	07/18/2004	PaymentReportShowSource can be either
 *							Current Status or Payment Report
 *							modify genPaymentReportShowSource()
 *							defect 7330 Ver 5.2.1
 * K Harrell	11/10/2004	modify to use FundsData.getWorkstationId
 *							for report headers.
 *							modify generateFeesReport(), 
 *							generateInventoryDReport, 
 *							generateInventorySReport(), 
 *							generateTransReconReport(), 
 *							genPaymentReport(), 
 *							genPaymentReportShowSource()
 *							defect 7681 Ver 5.2.2
 * K Harrell	03/21/2005	Modifications for smaller units of work
 *							plus Standardization Work throughout class
 *							modify setTimeStamps,updateCurrentStatus
 *							defect 7077 Ver 5.2.2 Fix 3
 * K Harrell	03/23/2005	Copied from 5.2.2 Fix 3 
 * 							add'l Java 1.4 work  
 * 							Ver 5.2.3
 * K Harrell	03/30/2005	Incorrect reassignment of laDatabaseAccess
 *							modify genPaymentReportShowSource()
 *							defect 8142 Ver 5.2.2 Fix 3 to Ver 5.2.3 
 * K Harrell	04/14/2005	Missing reassignment of CurrStatTimestmp
 *							modify updateCurrentStatus() 
 *							defect 8158 Ver 5.2.2 Fix 3 to Ver 5.2.3
 * Ray Rowehl	07/06/2005	Rename of some ReportConstants.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	08/17/2005	Code Review  5.2.2 Fix n => 5.2.3
 * 							Ver 5.2.3 
 * K Harrell	08/28/2005	Java 1.4 Work
 * 							Use laGenInvDetailRpt.getInvDStatusVec()
 * 							vs. laGenInvDetailRpt.InvDStatVec()
 * 							modify generateInventoryDReport()
 * 							Ver 5.2.3
 * K Harrell	11/02/2005	Use ReportConstant.RPT_1_COPY
 * 							modify generateSubstationSummaryReport()
 * 							defect 8379 Ver 5.2.3
 * K Harrell	11/10/2005	Error when attempt to closeout where 
 * 							closeoutreqindi = 1. 
 * 							modify generatePaymentReport()
 * 							defect 8421 Ver 5.2.3
 * K Harrell	03/17/2006	Do not write 'CLSOUT' transaction
 * 							modify generatePaymentReport()
 * 							defect 8623 Ver 5.2.3
 * K Harrell	05/23/2006	Restore 'CLSOUT' transaction  
 * 							modify generatePaymentReport()
 * 							defect 8798 Ver 5.2.3
 * K Harrell	10/09/2006	CloseoutendTstmp passed in vector to Server
 * 							to reflect client timestmp vs. server
 * 							modify generatePaymentReport()
 * 							defect 7824 Ver Fall_Admin_Tables 
 * Jeff S.		10/10/2006	Checked code in for Kathy.
 * 							defect 7824 Ver Fall_Admin_Tables
 * K Harrell	06/03/2009	Implement FundsConstant
 * 							modify generateFeesReport(), 
 *							generateInventoryDReport, 
 *							generateInventorySReport(), 
 *							generateTransReconReport(),
 *							generatePaymentReport(), 
 *							genPaymentReport(), 
 *							genPaymentReportShowSource()
 * 							defect 9943 Ver Defect_POS_F    
 * K Harrell	06/15/2009	Use ReportConstant values.   
 * 							Removed Module, FunctionId 
 * 							parameters from methods.
 * 							modify processData(), getAllCashDrwrs(),
 * 							 getCashDrwr(), getEmployeeData(), 
 * 							 generatePaymentReport(), 
 * 							 generateReports(), generateReports(),
 * 							 getCashDrwrsReset(), resetCashDrwrs(),
 * 							 getCashDrwrsSubstation(),
 * 							 generateCloseoutStatsReport(),
 * 							 generateCountywideReport(),
 * 							 generateSubstationSummaryReport(),
 * 							 getFundFuncTransAndTrFdsDetail() 
 * 							defect 10086 Ver Defect_POS_F 	 
 * K Harrell	08/16/2009	Work to standardize Reporting interface
 * 							 add initReportProperties() 
 * 							 modify generateFeesReport(), 
 *							  generateInventoryDReport, 
 *							  generateInventorySReport(), 
 *							  generateTransReconReport(),
 *							  genPaymentReport(),
 *							  genPaymentReportShowSource(), 
 * 							  generateCountywideReport(),
 * 							  generateSubstationSummaryReport()
 * 							defect 8628 Ver Defect_POS_F   
 * K Harrell	03/01/2010	Insert AdminLogData entry for Rerun 
 * 							 Substation Summary.
 * 							 modify generateSubstationSummary()
 * 							 defect 10168 Ver POS_640       
 * K Harrell	03/23/2010	Set Query Optimization Level for  
 * 							qryCloseoutRange() then restore according
 * 							to System AbstractProperty.
 * 							Add Logging for multiple FtWorth closeouts. 
 * 							Insert maximum of 1 closeout record per 
 * 							 request.  
 * 							modify generatePaymentReports() 
 * 							defect 10413 Ver Defect_POS_H      
 * K Harrell	04/03/2010	Implement OfficeTimeZoneCache
 * 							modify setTimeStamps()  
 * 							defect 10427 Ver POS_640
 * K Harrell	06/19/2010	Removing logging for Optimization Level 
 * 							set/reset if not Tarrant and mult cashdrawers
 * 							modify generatePaymentReports() 
 * 							defect 10525 Ver 6.5.0      	 
 * ---------------------------------------------------------------------
 */
/** 
 * Class will handle all of the server processing for any event in 
 * Funds
 * 
 * @version	6.5.0			06/19/2010
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/09/2001 
 */
public class FundsServerBusiness
{
	private ReportStatusData caInvDReportStatus =
		new ReportStatusData();
	private ReportStatusData caInvSReportStatus =
		new ReportStatusData();
	private ReportStatusData caPymtReportStatus =
		new ReportStatusData();
	private ReportStatusData caFeesReportStatus =
		new ReportStatusData();
	private ReportStatusData caTransReportStatus =
		new ReportStatusData();

	/**
	 * FundsServerBusiness constructor comment.
	 */
	public FundsServerBusiness()
	{
		super();
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * Closeout Statistics Report
	 *
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object generateCloseoutStatsReport(Object aaData)
		throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;

		DatabaseAccess laDBAccess = new DatabaseAccess();

		FundsReportsSQL laFundsReportsSQL =
			new FundsReportsSQL(laDBAccess);

		try
		{
			// defect 8628 
			// UOW #1 BEGIN 
			ReportProperties laRptProps =
				initReportProperties(
					laFundsData,
					laDBAccess,
					ReportConstant.CLOSEOUT_STATISTICS_REPORT_ID);
			// UOW #1 END 
			// end defect 8628 

			// Get Closeout Statistics 
			// UOW #2 BEGIN 
			laDBAccess.beginTransaction();
			Vector lvQueryResults =
				laFundsReportsSQL.qryCloseOutStatistics(laFundsData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END 

			// defect 10086 
			CloseoutStatisticsReport laCloseoutStatRpt =
				new CloseoutStatisticsReport(
					ReportConstant.CLOSEOUT_STATISTICS_REPORT_TITLE,
					laRptProps);
			// end defect 10086 

			laCloseoutStatRpt.formatReport(lvQueryResults);

			// defect 8628 
			// Note the RPT_COPIES_FUNDS_FILENAME_BY_WSID
			//  saveReports uses "0" as flag for
			//  Funds Reports w/ WsId as beginning of File Name. 
			return new ReportSearchData(
				laCloseoutStatRpt.getFormattedReport().toString(),
				"CLSS",
				ReportConstant.CLOSEOUT_STATISTICS_REPORT_TITLE,
				ReportConstant.RPT_COPIES_FUNDS_FILENAME_BY_WSID,
				ReportConstant.PORTRAIT);
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * County Wide Report
	 *
	 * @param  aaData 		Object
	 * @return ReportSearchData
	 * @throws RTSException  
	 */
	public ReportSearchData generateCountywideReport(Object aaData)
		throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;

		// Gen and Format report
		GenCountyWideReports laGenCtyWideRpts =
			new GenCountyWideReports("BLANK", new ReportProperties());
		laGenCtyWideRpts.formatReport(laFundsData);

		// defect 8628
		boolean lbBatch = laFundsData.isBatch();
		String lsFileName = lbBatch ? "CWRBAT" : "CWRONLN";
		int liNumCopies =
			lbBatch
				? ReportConstant.RPT_7_COPIES
				: ReportConstant.RPT_1_COPY;

		return new ReportSearchData(
			laGenCtyWideRpts.getFormattedReport().toString(),
			lsFileName,
			"COUNTYWIDE REPORT",
			liNumCopies,
			ReportConstant.PORTRAIT);
		// end defect 8628 
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * Fees Report
	 *
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object generateFeesReport(Object aaData) throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;

		// defect 8628 
		ReportSearchData laRptSearchData = new ReportSearchData();
		// end defect 8628 

		DatabaseAccess laDBAccess = new DatabaseAccess();
		FeeTypeReportSQL laFeeRptSQL = new FeeTypeReportSQL(laDBAccess);

		try
		{
			// defect 8628 
			// UOW #1 BEGIN 
			ReportProperties laRptProps =
				initReportProperties(
					laFundsData,
					laDBAccess,
					ReportConstant.FEES_REPORT_ID);
			// UOW #1 END 
			// end defect 8628 

			// defect 10086 
			GenFeeReport laGenFeeReport =
				new GenFeeReport(
					ReportConstant.FEES_REPORT_TITLE,
					laRptProps);
			// end defect 10086 

			// Get Timestamps for Date Range if user has not entered them
			// UOWs in call 
			if (laFundsData.getFundsReportData().getRange()
				!= FundsConstant.DATE_RANGE)
			{
				laFundsData = setTimeStamps(laFundsData, laDBAccess);
			}
			// Reset Report Status vector
			// If Entity = Cashdrawer   
			if (laFundsData.getFundsReportData().getEntity()
				== FundsConstant.CASH_DRAWER)
			{
				for (int i = 0;
					i < laFundsData.getSelectedCashDrawers().size();
					i++)
				{
					CashWorkstationCloseOutData laCashWksCloseOutData =
						(CashWorkstationCloseOutData) laFundsData
							.getSelectedCashDrawers()
							.elementAt(i);
					laCashWksCloseOutData.setRptStatus("");
				}
			}

			// Else, Entity = Employee 
			if (laFundsData.getFundsReportData().getEntity()
				== FundsConstant.EMPLOYEE)
			{
				for (int i = 0;
					i < laFundsData.getSelectedEmployees().size();
					i++)
				{
					EmployeeData laEmployeeObj =
						(EmployeeData) laFundsData
							.getSelectedEmployees()
							.elementAt(
							i);
					laEmployeeObj.setRptStatus("");
				}
			}

			//If no data returned from timestamp update, no transactions exist    	
			if (laFundsData.getFundsReportData().getRange()
				!= FundsConstant.DATE_RANGE
				&& laFundsData.getSelectedCashDrawers() == null)
			{
				if (laFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					for (int i = 0;
						i < laFundsData.getSelectedCashDrawers().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						CashWorkstationCloseOutData laCashWksCloseOutData =
							(CashWorkstationCloseOutData) laFundsData
								.getSelectedCashDrawers()
								.get(
								i);
						laEntityStatusData.setCashWsId(
							laCashWksCloseOutData.getCashWsId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laGenFeeReport.cvFeesStatVec.add(
							laEntityStatusData);
					}
				}
				else
				{
					for (int i = 0;
						i < laFundsData.getSelectedEmployees().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						EmployeeData laEmployeeData =
							(EmployeeData) laFundsData
								.getSelectedEmployees()
								.get(
								i);
						laEntityStatusData.setEmployeeId(
							laEmployeeData.getEmployeeId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laGenFeeReport.cvFeesStatVec.add(
							laEntityStatusData);
					}
				}
				caFeesReportStatus.setReportName(
					FundsConstant.FEES_REPORT);
				caFeesReportStatus.setStatusVector(
					(Vector) laGenFeeReport.cvFeesStatVec);
			}
			else
			{

				// UOW #2 BEGIN
				laDBAccess.beginTransaction();
				Vector lvQueryResults =
					laFeeRptSQL.qryFeeTypeReport(laFundsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #2 END

				//If no data returned, no transactions exist
				if (lvQueryResults.size() == 0)
				{
					if (laFundsData.getFundsReportData().getEntity()
						== FundsConstant.CASH_DRAWER)
					{
						for (int i = 0;
							i
								< laFundsData
									.getSelectedCashDrawers()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							CashWorkstationCloseOutData laCashWksCloseOutData =
								(CashWorkstationCloseOutData) laFundsData
									.getSelectedCashDrawers()
									.get(
									i);
							laEntityStatusData.setCashWsId(
								laCashWksCloseOutData.getCashWsId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943 
							laGenFeeReport.cvFeesStatVec.add(
								laEntityStatusData);
						}
					}
					else
					{
						for (int i = 0;
							i
								< laFundsData
									.getSelectedEmployees()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							EmployeeData laEmployeeData =
								(EmployeeData) laFundsData
									.getSelectedEmployees()
									.get(
									i);
							laEntityStatusData.setEmployeeId(
								laEmployeeData.getEmployeeId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943 
							laGenFeeReport.cvFeesStatVec.add(
								laEntityStatusData);
						}
					}
					caFeesReportStatus.setReportName(
						FundsConstant.FEES_REPORT);
					caFeesReportStatus.setStatusVector(
						(Vector) laGenFeeReport.cvFeesStatVec);
				}
				//Else if query results had data, generate and format report	
				else
				{
					laGenFeeReport.formatReport(
						lvQueryResults,
						laFundsData);

					// defect 8628 
					laRptSearchData =
						new ReportSearchData(
							laGenFeeReport
								.getFormattedReport()
								.toString(),
							"FEES",
							FundsConstant.FEES_REPORT,
							ReportConstant.RPT_7_COPIES,
							ReportConstant.PORTRAIT);
					// end defect 8628 

					// The following will be returned via processData  
					caFeesReportStatus =
						(ReportStatusData) laGenFeeReport.caFeesStatus;
					caFeesReportStatus.setStatusVector(
						(Vector) laGenFeeReport.cvFeesStatVec);
				}
			}
			// defect 8628
			return laRptSearchData;
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * Inventory Detail Report
	 *
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object generateInventoryDReport(Object aaData)
		throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;

		// defect 8628 
		ReportSearchData laRptSearchData = new ReportSearchData();
		// end defect 8628 

		DatabaseAccess laDBAccess = new DatabaseAccess();

		InventoryDetailReportSQL laInvDetailRptSQL =
			new InventoryDetailReportSQL(laDBAccess);

		try
		{
			// defect 8628 
			// UOW #1 BEGIN 
			ReportProperties laRptProps =
				initReportProperties(
					laFundsData,
					laDBAccess,
					ReportConstant.INVENTORY_DETAIL_REPORT_ID);
			// UOW #1 END 
			// end defect 8628

			// defect 10086
			GenInvDetailReport laGenInvDetailRpt =
				new GenInvDetailReport(
					ReportConstant.INVENTORY_DETAIL_REPORT_TITLE,
					laRptProps);
			// end defect 10086 

			// Get Time Stamps for Date Range if user has not entered them
			// UOWs in call 
			if (laFundsData.getFundsReportData().getRange()
				!= FundsConstant.DATE_RANGE)
			{
				laFundsData = setTimeStamps(laFundsData, laDBAccess);
			}
			// Reset Report Status vector
			// If Entity = Cashdrawer 
			if (laFundsData.getFundsReportData().getEntity()
				== FundsConstant.CASH_DRAWER)
			{
				for (int i = 0;
					i < laFundsData.getSelectedCashDrawers().size();
					i++)
				{
					CashWorkstationCloseOutData laCashWksCloseOutData =
						(CashWorkstationCloseOutData) laFundsData
							.getSelectedCashDrawers()
							.elementAt(i);
					laCashWksCloseOutData.setRptStatus("");
				}
			}
			// Else, Entity = Employee
			if (laFundsData.getFundsReportData().getEntity()
				== FundsConstant.EMPLOYEE)
			{
				for (int i = 0;
					i < laFundsData.getSelectedEmployees().size();
					i++)
				{
					EmployeeData laEmployeeData =
						(EmployeeData) laFundsData
							.getSelectedEmployees()
							.elementAt(
							i);
					laEmployeeData.setRptStatus("");
				}
			}
			//If no data returned from timestamp update, no transactions exists   	
			if (laFundsData.getFundsReportData().getRange()
				!= FundsConstant.DATE_RANGE
				&& laFundsData.getSelectedCashDrawers() == null)
			{
				if (laFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					for (int i = 0;
						i < laFundsData.getSelectedCashDrawers().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						CashWorkstationCloseOutData laCashWksCloseOutData =
							(CashWorkstationCloseOutData) laFundsData
								.getSelectedCashDrawers()
								.get(
								i);
						laEntityStatusData.setCashWsId(
							laCashWksCloseOutData.getCashWsId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laGenInvDetailRpt.getInvDStatusVec().add(
							laEntityStatusData);
					}
				}
				else
				{
					for (int i = 0;
						i < laFundsData.getSelectedEmployees().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						EmployeeData laEmployeeDataObj =
							(EmployeeData) laFundsData
								.getSelectedEmployees()
								.get(
								i);
						laEntityStatusData.setEmployeeId(
							laEmployeeDataObj.getEmployeeId());
						// defect 9943 
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laGenInvDetailRpt.getInvDStatusVec().add(
							laEntityStatusData);
					}
				}
				caInvDReportStatus.setReportName(
					FundsConstant.INVENTORYD_REPORT);
				caInvDReportStatus.setStatusVector(
					(Vector) laGenInvDetailRpt.getInvDStatusVec());
			}
			//Else query for transactions
			else
			{
				// UOW #2 BEGIN
				laDBAccess.beginTransaction();
				Vector lvQueryResults =
					laInvDetailRptSQL.qryInventoryDetailReport(
						laFundsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #2 END

				if (lvQueryResults.size() == 0)
				{
					if (laFundsData.getFundsReportData().getEntity()
						== FundsConstant.CASH_DRAWER)
					{
						for (int i = 0;
							i
								< laFundsData
									.getSelectedCashDrawers()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							CashWorkstationCloseOutData laCashWksCloseOutData =
								(CashWorkstationCloseOutData) laFundsData
									.getSelectedCashDrawers()
									.get(
									i);
							laEntityStatusData.setCashWsId(
								laCashWksCloseOutData.getCashWsId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943 
							laGenInvDetailRpt.getInvDStatusVec().add(
								laEntityStatusData);
						}
					}
					else
					{
						for (int i = 0;
							i
								< laFundsData
									.getSelectedEmployees()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							EmployeeData laEmployeeDataObj =
								(EmployeeData) laFundsData
									.getSelectedEmployees()
									.get(
									i);
							laEntityStatusData.setEmployeeId(
								laEmployeeDataObj.getEmployeeId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943 
							laGenInvDetailRpt.getInvDStatusVec().add(
								laEntityStatusData);
						}
					}
					caInvDReportStatus.setReportName(
						FundsConstant.INVENTORYD_REPORT);
					caInvDReportStatus.setStatusVector(
						(Vector) laGenInvDetailRpt.getInvDStatusVec());
				}
				//Else if query results had data, generate and format report
				else
				{
					laGenInvDetailRpt.formatReport(
						lvQueryResults,
						laFundsData);

					// defect 8628 
					laRptSearchData =
						new ReportSearchData(
							laGenInvDetailRpt
								.getFormattedReport()
								.toString(),
							"INVD",
							FundsConstant.INVENTORYD_REPORT,
							ReportConstant.RPT_7_COPIES,
							ReportConstant.PORTRAIT);
					// end defect 8628 

					caInvDReportStatus =
						(ReportStatusData) laGenInvDetailRpt
							.getInvDStatus();
					caInvDReportStatus.setStatusVector(
						(Vector) laGenInvDetailRpt.getInvDStatusVec());
				}
			}
			// defect 8628
			return laRptSearchData;
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * Inventory Summary Report
	 *
	 * @param  aaData Object
	 * @return Object
	 * @throws RTSException
	 */
	public Object generateInventorySReport(Object aaData)
		throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;

		// defect 8628 
		ReportSearchData laRptSearchData = new ReportSearchData();
		// end defect 8628

		DatabaseAccess laDBAccess = new DatabaseAccess();

		InventorySummaryTypeReportSQL laInvSummaryRptSQL =
			new InventorySummaryTypeReportSQL(laDBAccess);
		try
		{
			// defect 8628 
			// UOW #1 BEGIN 
			ReportProperties laRptProps =
				initReportProperties(
					laFundsData,
					laDBAccess,
					ReportConstant
						.RPT_5221_INVENTORY_SUMMARY_REPORT_ID);
			// UOW #1 END 
			// end defect 8628 

			InventorySummaryReport laInvSummaryRpt =
				new InventorySummaryReport(
					"INVENTORY SUMMARY REPORT",
					laRptProps);

			// Get Time Stamps for Date Range if user has not entered them
			// UOWs in call 
			if (laFundsData.getFundsReportData().getRange()
				!= FundsConstant.DATE_RANGE)
			{
				laFundsData = setTimeStamps(laFundsData, laDBAccess);
			}
			// Reset Report Status vector
			// If Entity = Cashdrawer 
			if (laFundsData.getFundsReportData().getEntity()
				== FundsConstant.CASH_DRAWER)
			{
				for (int i = 0;
					i < laFundsData.getSelectedCashDrawers().size();
					i++)
				{
					CashWorkstationCloseOutData laCashWksCloseOutData =
						(CashWorkstationCloseOutData) laFundsData
							.getSelectedCashDrawers()
							.elementAt(i);
					laCashWksCloseOutData.setRptStatus("");
				}
			}
			// Else, Entity = Employee
			if (laFundsData.getFundsReportData().getEntity()
				== FundsConstant.EMPLOYEE)
			{
				for (int i = 0;
					i < laFundsData.getSelectedEmployees().size();
					i++)
				{
					EmployeeData laEmployeeData =
						(EmployeeData) laFundsData
							.getSelectedEmployees()
							.elementAt(
							i);
					laEmployeeData.setRptStatus("");
				}
			}
			// If no data returned from timestamp update, 
			// no transactions exsists    	
			if (laFundsData.getFundsReportData().getRange()
				!= FundsConstant.DATE_RANGE
				&& laFundsData.getSelectedCashDrawers() == null)
			{
				if (laFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					for (int i = 0;
						i < laFundsData.getSelectedCashDrawers().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						CashWorkstationCloseOutData laCashWksCloseOutData =
							(CashWorkstationCloseOutData) laFundsData
								.getSelectedCashDrawers()
								.get(
								i);
						laEntityStatusData.setCashWsId(
							laCashWksCloseOutData.getCashWsId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943
						laInvSummaryRpt.getInvSStatVec().add(
							laEntityStatusData);
					}
				}
				else
				{
					for (int i = 0;
						i < laFundsData.getSelectedEmployees().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						EmployeeData laEmployeeDataObj =
							(EmployeeData) laFundsData
								.getSelectedEmployees()
								.get(
								i);
						laEntityStatusData.setEmployeeId(
							laEmployeeDataObj.getEmployeeId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laInvSummaryRpt.getInvSStatVec().add(
							laEntityStatusData);
					}
				}
				caInvSReportStatus.setReportName(
					FundsConstant.INVENTORYD_REPORT);
				caInvSReportStatus.setStatusVector(
					(Vector) laInvSummaryRpt.getInvSStatVec());
			}
			// Else query for transactions
			else
			{
				// UOW #2 BEGIN
				laDBAccess.beginTransaction();
				Vector lvQueryResults =
					laInvSummaryRptSQL
						.qryInventorySummaryTypeReportSQL(
						laFundsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #2 END

				Vector lvInventoryVector = new java.util.Vector();
				if (lvQueryResults.size() > 0)
				{
					laFundsData.setInventoryData(lvQueryResults);
					lvInventoryVector =
						sortInventoryResults(laFundsData);
				}
				if (lvQueryResults.size() == 0)
				{
					if (laFundsData.getFundsReportData().getEntity()
						== FundsConstant.CASH_DRAWER)
					{
						for (int i = 0;
							i
								< laFundsData
									.getSelectedCashDrawers()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							CashWorkstationCloseOutData laCashWksCloseOutData =
								(CashWorkstationCloseOutData) laFundsData
									.getSelectedCashDrawers()
									.get(
									i);
							laEntityStatusData.setCashWsId(
								laCashWksCloseOutData.getCashWsId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943 
							laInvSummaryRpt.getInvSStatVec().add(
								laEntityStatusData);
						}
					}
					else
					{
						for (int i = 0;
							i
								< laFundsData
									.getSelectedEmployees()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							EmployeeData laEmployeeDataObj =
								(EmployeeData) laFundsData
									.getSelectedEmployees()
									.get(
									i);
							laEntityStatusData.setEmployeeId(
								laEmployeeDataObj.getEmployeeId());
							// defect 9943 
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943 
							laInvSummaryRpt.getInvSStatVec().add(
								laEntityStatusData);
						}
					}
					caInvSReportStatus.setReportName(
						FundsConstant.INVENTORYD_REPORT);
					caInvSReportStatus.setStatusVector(
						(Vector) laInvSummaryRpt.getInvSStatVec());
				}
				// Else if query results had data, generate and format report    
				else
				{
					laInvSummaryRpt.formatReport(
						lvInventoryVector,
						laFundsData);

					// defect 8628 
					laRptSearchData =
						new ReportSearchData(
							laInvSummaryRpt
								.getFormattedReport()
								.toString(),
							"INVS",
							FundsConstant.INVENTORYS_REPORT,
							ReportConstant.RPT_7_COPIES,
							ReportConstant.PORTRAIT);
					// end defect 8628 

					caInvSReportStatus =
						(ReportStatusData) laInvSummaryRpt
							.getInvSStatus();
					caInvSReportStatus.setStatusVector(
						(Vector) laInvSummaryRpt.getInvSStatVec());
				}
			}
			return laRptSearchData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * Payment Report
	 *
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object generatePaymentReport(Object aaData)
		throws RTSException
	{
		// defect 8798 
		// Restore CLSOUT transaction 
		Vector lvInput = (Vector) aaData;
		TransactionHeaderData laTransactionHeaderData =
			(TransactionHeaderData) lvInput.get(0);
		FundsData laFundsData = (FundsData) lvInput.get(1);
		// end defect 8798

		// defect 7824 
		RTSDate laClientTimestmp = new RTSDate((String) lvInput.get(2));
		// end defect 7824  

		GeneralSearchData laGenSearchData = new GeneralSearchData();
		Vector lvGenSearchData = new Vector();
		Vector lvDrawers = laFundsData.getSelectedCashDrawers();

		DatabaseAccess laDBAccess = new DatabaseAccess();
		CashWorkstationCloseOut laCashWksCloseOut =
			new CashWorkstationCloseOut(laDBAccess);

		TransactionHeader laTransactionHeader =
			new TransactionHeader(laDBAccess);

		int liNumSetAside = 0;
		int liOfcIssuanceNo =
			((CashWorkstationCloseOutData) lvDrawers.get(0))
				.getOfcIssuanceNo();
		int liSubstaId =
			((CashWorkstationCloseOutData) lvDrawers.get(0))
				.getSubstaId();

		// defect 8421 
		// added new int 
		int liNumCloseoutReqIndi = 0;
		// end defect 8421 

		// defect 10413 
		// Set boolean to denote Tarrant & Multiple CashDrawers 
		boolean lbLogCloseOutTimes =
			laFundsData.getOfficeIssuanceNo() == 220
				&& laFundsData.getSubStationId() == 0
				&& lvDrawers.size() > 1;

		if (lbLogCloseOutTimes)
		{
			Log.write(
				Log.SQL_EXCP,
				this,
				"Tarrant County: qryIncompleteTrans()/updCashWsIds() Begin");
		}
		// end defect 10413 

		try
		{
			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();

			for (int i = 0; i < lvDrawers.size(); i++)
			{
				//Check transactionHeader for set aside transactions
				CashWorkstationCloseOutData laCashWksCOData1 =
					(CashWorkstationCloseOutData) lvDrawers.get(i);

				laGenSearchData = new GeneralSearchData();
				laGenSearchData.setIntKey1(liOfcIssuanceNo);
				laGenSearchData.setIntKey2(liSubstaId);
				laGenSearchData.setIntKey3(
					laCashWksCOData1.getCashWsId());

				laCashWksCOData1.setCloseOutReqWsId(
					laFundsData.getCashWsId());
				int liSetAsideTrans =
					laTransactionHeader.qryIncompleteTransactionCount(
						laGenSearchData);
				if (liSetAsideTrans > 0)
				{
					laCashWksCOData1.setPendingTransactions(true);
					laCashWksCOData1.setRptStatus(
						FundsConstant.SET_ASIDE);
					liNumSetAside = liNumSetAside + 1;
				}
				//If no set aside transactions, update close out indicator to set
				else
				{
					laCashWksCOData1.setCloseOutReqIndi(1);

					// Update RTS_CASH_WS_IDS
					//   set CloseoutReqIndi, CloseoutReqWsId

					// Note: updCashWorkstationIds will return 0 if 
					//  no rows are updated, i.e. closeoutindi already   
					//  set with a different workstation 
					int liIndiSet =
						laCashWksCloseOut.updCashWorkstationIds(
							laCashWksCOData1);

					// liIndiSet = 0 ==> closeoutreqindi already set
					// by another workstation 			
					if (liIndiSet == 0)
					{
						// defect 8421 
						liNumCloseoutReqIndi = liNumCloseoutReqIndi + 1;
						// end defect 8421  
						laCashWksCOData1.setCloseOutReqIndi(0);
						laCashWksCOData1.setRptStatus(
							FundsConstant.INDI_SET);
					}
				}
			}
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			// defect 10413 
			if (lbLogCloseOutTimes)
			{
				Log.write(
					Log.SQL_EXCP,
					this,
					"Tarrant County: qryIncompleteTrans()/updCashWsIds() End");
			}
			// end defect 10413 

			// defect 8421
			// Added "+liNumCloseoutIndi" to equation as a closeout
			// is not required if all cashdrawers are either in set-aside
			// or have closeout  
			// If Cashdrawers need to be closed out
			// i.e. Sum of Set Aside + CloseOutIndi != # Cashdrawers
			if ((liNumSetAside + liNumCloseoutReqIndi)
				!= lvDrawers.size())
				// end defect 8421	
			{
				// defect 7824
				// Set information for current time zone
				// Get Time Zone for offfice
				//CacheManagerServerBusiness laCacheMgrSrvrBusiness =
				//	new CacheManagerServerBusiness();
				//String lsTimeZone =
				//	laCacheMgrSrvrBusiness.getOfficeTimeZone(
				//		liOfcIssuanceNo,
				//		liSubstaId);

				// Get Current Timestamp from Database for Office
				// UOW #2 BEGIN 
				//laDatabaseAccess.beginTransaction();
				//String lsDatabaseDate =
				//	((RTSDate) laMiscellaneous
				//		.qryCurrentTimestamp(
				//			liOfcIssuanceNo,
				//			liSubstaId))
				//		.getDB2Date();
				//laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #2 END
				// end defect 7824 

				// Adjust time from DB based on time zone
				//RTSDate laTZAdjustedDate =
				//	new RTSDate(
				//		lsDatabaseDate).getTimeZoneAdjustedDate(
				//		lsTimeZone);

				// defect 10413
				DB2CurrentQueryOptimization laDB2OptLvl =
					new DB2CurrentQueryOptimization(laDBAccess);

				int liSavedOptimizationLevel = 0;

				if (SystemProperty.isResetFundsDB2QueryOptimization())
				{
					// defect 10525 
					if (lbLogCloseOutTimes)
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							"Tarrant County: Set Optimization "
								+ SystemProperty
									.getResetFundsDB2QueryOptimizationLevel()
								+ " Begin");
					}
					// end defect 10525 

					// UOW #1B BEGIN 
					laDBAccess.beginTransaction();

					liSavedOptimizationLevel =
						laDB2OptLvl.getDB2CurrentQueryOptimization();

					laDB2OptLvl.setDB2CurrentQueryOptimization(
						SystemProperty
							.getResetFundsDB2QueryOptimizationLevel());

					laDBAccess.endTransaction(DatabaseAccess.COMMIT);
					// UOW #1B END 

					// defect 10525 
					if (lbLogCloseOutTimes)
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							"Tarrant County: Set Optimization "
								+ SystemProperty
									.getResetFundsDB2QueryOptimizationLevel()
								+ " End");
					}
					// end defect 10525 
				}
				if (lbLogCloseOutTimes)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Tarrant County: qryCloseOutRange() Begin");
				}
				// end defect 10413 

				// UOW #2 BEGIN
				laDBAccess.beginTransaction();

				for (int j = 0; j < lvDrawers.size(); j++)
				{
					CashWorkstationCloseOutData laCashWksCloseOutData =
						(CashWorkstationCloseOutData) lvDrawers.get(j);

					// Do not process if Set-Aside or CloseoutIndi 	
					if (!laCashWksCloseOutData
						.getRptStatus()
						.equals(FundsConstant.SET_ASIDE)
						&& !laCashWksCloseOutData.getRptStatus().equals(
							FundsConstant.INDI_SET))
					{
						// defect 7824 
						laCashWksCloseOutData.setCurrentTimestmp(
							laClientTimestmp);
						//laTZAdjustedDate);

						//Query for CloseOut Timestamp Ranges
						Vector lvRange =
							laCashWksCloseOut.qryCloseOutRange(
								laCashWksCloseOutData);

						if (lvRange.size() != 0)
						{
							CashWorkstationCloseOutData laCashWksCODataRange =
								(
									CashWorkstationCloseOutData) lvRange
										.get(
									0);

							// Set TransSinceCloseOut 		
							laCashWksCloseOutData
								.setTransSinceCloseOut(
								laCashWksCODataRange
									.getTransSinceCloseOut());

							// Set CloseOutBegTstmp  
							laCashWksCloseOutData.setCloseOutBegTstmp(
								laCashWksCODataRange
									.getCloseOutBegTstmp());

							// Set CloseOutEndTstmp							
							laCashWksCloseOutData.setCloseOutEndTstmp(
								laClientTimestmp);
							//	laTZAdjustedDate);

							// Set TransSinceCloseOut 
							laCashWksCloseOutData.setCurrStatTimestmp(
								laClientTimestmp);
							//laTZAdjustedDate);
							// end defect 7824 

							// Check if no trans exists
							if ((laCashWksCloseOutData
								.getTransSinceCloseOut()
								== 0)
								&& (laCashWksCloseOutData
									.getRptStatus()
									.equals("")))
							{
								// defect 9943 
								laCashWksCloseOutData.setRptStatus(
									FundsConstant
										.NO_COMPL_TRANSACTIONS);
								// end defect 9943 
							}
							else if (
								laCashWksCloseOutData
									.getRptStatus()
									.equals(
									""))
							{
								laCashWksCloseOutData.setRptStatus(
									FundsConstant.CLOSE_COMPLETE);
							}
						}
					}
				}
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #2 END 

				// defect 10413
				if (lbLogCloseOutTimes)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Tarrant County: qryCloseOutRange() End");
				}

				if (SystemProperty.isResetFundsDB2QueryOptimization())
				{
					if (lbLogCloseOutTimes)
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							"Tarrant County: Reset Optimization "
								+ liSavedOptimizationLevel
								+ " Begin");
					}

					// UOW #2B BEGIN 
					laDBAccess.beginTransaction();

					laDB2OptLvl.setDB2CurrentQueryOptimization(
						liSavedOptimizationLevel);

					laDBAccess.endTransaction(DatabaseAccess.COMMIT);
					
					if (lbLogCloseOutTimes)
					{
						Log.write(
							Log.SQL_EXCP,
							this,
							"Tarrant County: Reset Optimization "
								+ liSavedOptimizationLevel
								+ " End");
					}
					// UOW #2B END

				}
				// end defect 10413 

				laFundsData.setSelectedCashDrawers(lvDrawers);

				// Generate Payment Report for a CloseOut

				// defect 10413 
				// Log genPaymentReport() Begin 
				if (lbLogCloseOutTimes)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Tarrant County: genPaymentReport() Begin");
				}
				// end defect 10413 

				laGenSearchData =
					(GeneralSearchData) genPaymentReport(laFundsData);

				// defect 10413 
				// Log genPaymentReport() End  
				if (lbLogCloseOutTimes)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Tarrant County: genPaymentReport() End");
				}
				// end defect 10413 

				lvGenSearchData = new Vector();
				lvGenSearchData.add(laGenSearchData);

				//Complete Transaction CloseOut Process
				// UOW #3 BEGIN 
				laDBAccess.beginTransaction();

				// defect 8798 
				// Restore 'CLSOUT' transaction
				int liNumClosed = 0;

				// defect 10413 
				if (lbLogCloseOutTimes)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Tarrant County: insCloseOutHstry(), ins CLSOUT Begin");
				}
				// end defect 10413 

				for (int j = 0; j < lvDrawers.size(); j++)
				{
					CashWorkstationCloseOutData laCashWksCloseOutData =
						(CashWorkstationCloseOutData) lvDrawers.get(j);

					// defect 8421 
					// Added check for !FundsConstant.INDI_SET	
					// Insert CloseOutHstry if !Set-Aside and !CloseoutIndi 
					if (!laCashWksCloseOutData
						.getRptStatus()
						.equals(FundsConstant.SET_ASIDE)
						&& !laCashWksCloseOutData.getRptStatus().equals(
							FundsConstant.INDI_SET))
						// end defect 8421
					{
						// Insert RTS_CLOSEOUT_HSTRY 
						laCashWksCloseOut.insCloseOutHistory(
							laCashWksCloseOutData);

						// UPDATE RTS_CASH_WS_IDS  
						laCashWksCloseOutData.setCloseOutReqIndi(0);
						laCashWksCloseOutData.setCloseOutReqWsId(
							Integer.MIN_VALUE);
						laCashWksCloseOut.updCashWorkstationIds(
							laCashWksCloseOutData);

						// Write CLSOUT transaction
						if (laCashWksCloseOutData
							.getRptStatus()
							.equals(FundsConstant.CLOSE_COMPLETE))
						{
							liNumClosed = liNumClosed + 1;

							// defect 10413
							// Only write CLSOUT transaction once, 
							//  Even if Multiple 
							if (liNumClosed == 1)
							{
								Vector lvIn = new Vector();
								lvIn.addElement(
									laTransactionHeaderData);
								CompleteTransactionData laCompleteTransactionData =
									new CompleteTransactionData();
								laCompleteTransactionData.setTransCode(
									TransCdConstant.CLSOUT);
								laCompleteTransactionData.setCashWsid(
									laCashWksCloseOutData
										.getCashWsId());
								lvIn.addElement(
									laCompleteTransactionData);
								lvIn.addElement(laDBAccess);

								//On nth ClSOUT Trans where n>1 wait for 1 second between transactions.
								//Without this pause, there is a duplicate key error.

								// 	if (liNumClosed != 1)
								//	{
								//			Thread.sleep(1000);
								//	}
								CommonServerBusiness laCommonServerBusiness =
									new CommonServerBusiness();
								laCommonServerBusiness.processData(
									GeneralConstant.COMMON,
									CommonConstant.PROC_TRANS,
									lvIn);
							} // getRptStatus().equals(FundsConstant.CLOSE_COMPLETE))
						} // end defect 10413 

						// end defect 8798
					} // !Set-Aside & !CloseOutIndi 
				} //for (int j = 0; j < lvDrawers.size(); j++)
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #3 END

				// defect 10413 
				if (lbLogCloseOutTimes)
				{
					Log.write(
						Log.SQL_EXCP,
						this,
						"Tarrant County: insCloseOutHstry(), ins CLSOUT End");
				}
				// end defect 10413 

			} // Not all with Set Aside || CloseoutReqIndi

			ReportSearchData laReportSearchData =
				new ReportSearchData();
			laReportSearchData.setVector(lvGenSearchData);
			laReportSearchData.setNextScreen(ScreenConstant.FUN015);
			laReportSearchData.setData(laFundsData);
			return laReportSearchData;
		}
		// defect 10413 
		// Remove Again 
		// defect 8798
		// Restored w/ CLSOUT transaction  
		//		catch (InterruptedException aeIEx)
		//		{
		//			RTSException leRTSEx =
		//				new RTSException(RTSException.JAVA_ERROR, aeIEx);
		//			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
		//			throw leRTSEx;
		//		}
		// end defect 8798
		// end defect 10413  

		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method takes the list of report names to generate, and passes control
	 * to each report generation method. The method then adds each report returned
	 * to Gen Search Data object, along with the status of report generation, and
	 * packages a ReportSearchData object to return.
	 *
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object generateReports(Object aaData) throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;
		Vector lvReports =
			laFundsData.getFundsReportData().getReportNames();
		Vector lvGenSearchData = new Vector();
		ReportSearchData laReportSearchData = new ReportSearchData();
		Vector lvReportStatus = new Vector();

		//For each report (lsReportName), call the generate method
		for (int i = 0; i < lvReports.size(); i++)
		{
			//Retrieve report name in list
			String lsReportName = (String) lvReports.get(i);
			// Payment Report
			if (lsReportName.equals(FundsConstant.PAYMENT_REPORT))
			{
				if (laFundsData
					.getFundsReportData()
					.isShowSourceMoney())
				{
					lvGenSearchData.add(
						genPaymentReportShowSource(laFundsData));
				}
				else
				{
					lvGenSearchData.add(genPaymentReport(laFundsData));
				}
				lvReportStatus.add(caPymtReportStatus);
			}
			// Transaction Reconciliation Report 
			else if (
				lsReportName.equals(FundsConstant.TRANSACTION_REPORT))
			{
				lvGenSearchData.add(
					generateTransReconReport(laFundsData));
				lvReportStatus.add(caTransReportStatus);
			}
			// Fees Report
			else if (lsReportName.equals(FundsConstant.FEES_REPORT))
			{
				lvGenSearchData.add(generateFeesReport(aaData));
				lvReportStatus.add(caFeesReportStatus);
			}
			// Inventory Detail Report
			else if (
				lsReportName.equals(FundsConstant.INVENTORYD_REPORT))
			{
				lvGenSearchData.add(generateInventoryDReport(aaData));
				lvReportStatus.add(caInvDReportStatus);
			}
			// Inventory Summary Report
			else if (
				lsReportName.equals(FundsConstant.INVENTORYS_REPORT))
			{
				lvGenSearchData.add(generateInventorySReport(aaData));
				lvReportStatus.add(caInvSReportStatus);
			}
		}
		//Set reports, next screen, and status vector in Report Search Data	
		laReportSearchData.setVector(lvGenSearchData);
		laReportSearchData.setNextScreen(ScreenConstant.FUN013);
		laFundsData.setReportStatus(lvReportStatus);
		laReportSearchData.setData(laFundsData);
		return laReportSearchData;
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * Substation Summary report
	 *
	 * @param  aaData 		Object
	 * @return ReportSearchData
	 * @throws RTSException 
	 */
	public synchronized ReportSearchData generateSubstationSummaryReport(Object aaData)
		throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;
		Vector lvDrawers = laFundsData.getSelectedCashDrawers();
		RTSDate laSumEffDate = laFundsData.getSummaryEffDate();
		int liSubstaId = laFundsData.getSubStationId();
		int liOfcIssuanceNo = laFundsData.getOfficeIssuanceNo();

		DatabaseAccess laDBAccess = new DatabaseAccess();

		CloseOutHistory laCloseOutHistory =
			new CloseOutHistory(laDBAccess);
		FeeSummary laFeeSummary = new FeeSummary(laDBAccess);
		InventorySummary laInventorySummary =
			new InventorySummary(laDBAccess);
		PaymentSummary laPaymentSummary =
			new PaymentSummary(laDBAccess);
		SubstationSummary laSubstationSummary =
			new SubstationSummary(laDBAccess);
		TransactionHeader laTransactionHeader =
			new TransactionHeader(laDBAccess);

		try
		{
			// defect 10168 
			// Insert Admin Log for Rerun Substa Summary 
			if (laFundsData.getAdminLogData() != null)
			{
				// UOW #0 BEGIN
				laDBAccess.beginTransaction();
				AdministrationLog laAdminLog =
					new AdministrationLog(laDBAccess);
				laAdminLog.insAdministrationLog(
					laFundsData.getAdminLogData());
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #0 END 
			}
			// end defect 10168 

			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();

			// RTS_CLOSEOUT_HSTRY 
			for (int i = 0; i < lvDrawers.size(); i++)
			{
				CloseOutHistoryData laCloseOutHstryData =
					(CloseOutHistoryData) lvDrawers.get(i);
				laCloseOutHstryData.setSummaryEffDate(
					laSumEffDate.getAMDate());
				//Update CloseOutHistory with SummaryEffDate	
				laCloseOutHistory.updCloseOutHistory(
					laCloseOutHstryData);
			}

			// KPH (2002.10.18): CQU10004912 Substation Summary Performance

			SubstationSummaryData laSubstationSummaryData =
				new SubstationSummaryData();
			laSubstationSummaryData.setOfcIssuanceNo(liOfcIssuanceNo);
			laSubstationSummaryData.setSubstaId(liSubstaId);
			laSubstationSummaryData.setSummaryEffDate(
				laSumEffDate.getAMDate());

			// RTS_TRANS_HDR
			// Update TransHdr with SummaryEffDate
			laTransactionHeader.updSummaryEffDateTransactionHeader(
				laSubstationSummaryData);

			// RTS_SUBSTA_SUMMARY
			// Delete SubstationSummary data for SubstaId an SummaryEffDate	
			laSubstationSummary.delSubstationSummary(
				laSubstationSummaryData);

			// Insert SubstationSummary run into SubstationSummary tables
			laSubstationSummary.insSubstationSummary(
				laSubstationSummaryData);

			// RTS_PYMNT_SUMMARY 
			PaymentSummaryData laPaymentSummaryData =
				new PaymentSummaryData();
			laPaymentSummaryData.setOfcIssuanceNo(liOfcIssuanceNo);
			laPaymentSummaryData.setSubstaId(liSubstaId);
			laPaymentSummaryData.setSummaryEffDate(
				laSumEffDate.getAMDate());

			// Select/Insert Pay Type Positive amounts into Payment Summary
			laPaymentSummary.insPaymentSummaryFromTransPayment(
				laPaymentSummaryData);

			// Select Void Payment Info from Trans/TransPayment
			Vector lvResults =
				laPaymentSummary.qryVoidPaymentForPaymentSummary(
					laPaymentSummaryData);

			for (int j = 0; j < lvResults.size(); j++)
			{
				//Update/Insert PaymentSummary
				laPaymentSummaryData =
					(PaymentSummaryData) lvResults.get(j);
				int liUpdate =
					laPaymentSummary.updPaymentSummary(
						laPaymentSummaryData);
				if (liUpdate == 0)
				{
					laPaymentSummary.insPaymentSummary(
						laPaymentSummaryData);
				}
			}

			// Select cash change transaction data for RTS_PYMNT_SUMMARY 
			Vector lvResults2 =
				laPaymentSummary.qryCashChangeForPaymentSummary(
					laPaymentSummaryData);

			// Subtract cash change trans data for payment summary
			for (int k = 0; k < lvResults2.size(); k++)
			{
				//Update/Insert PaymentSummary
				laPaymentSummaryData =
					(PaymentSummaryData) lvResults2.get(k);
				int liUpdate =
					laPaymentSummary.updPaymentSummary(
						laPaymentSummaryData);
				if (liUpdate == 0)
				{
					laPaymentSummary.insPaymentSummary(
						laPaymentSummaryData);
				}
			}

			// RTS_FEE_SUMMARY 
			FeeSummaryData laFeeSummaryData = new FeeSummaryData();
			laFeeSummaryData.setOfcIssuanceNo(liOfcIssuanceNo);
			laFeeSummaryData.setSubstaId(liSubstaId);
			laFeeSummaryData.setSummaryEffDate(
				laSumEffDate.getAMDate());

			//Select/Insert into FeeSummary
			laFeeSummary.insFeeSummaryFromTrFdsDetail(laFeeSummaryData);

			//Select/Insert voids into FeeSummary
			laFeeSummary.insFeeSummaryForVoid(laFeeSummaryData);

			//Insert Check change trans data into FeeSummary
			laFeeSummary.insFeeSummaryForCheckChange(laFeeSummaryData);

			// RTS_INV_SUMMARY 
			InventorySummaryData laInventorySummaryData =
				new InventorySummaryData();
			laInventorySummaryData.setOfcIssuanceNo(liOfcIssuanceNo);
			laInventorySummaryData.setSubstaId(liSubstaId);
			laInventorySummaryData.setSummaryEffDate(
				laSumEffDate.getAMDate());

			// defect 7226
			// Really modify reference from ...old
			// laInventorySummary.insInventorySummaryFromTrInvDetailOld(
			// laInventorySummaryData);
			// Insert Inventory sold trans data into inv summary
			laInventorySummary.insInventorySummaryFromTrInvDetail(
				laInventorySummaryData);
			// end defect 7226

			//Select inventory reused trans data
			Vector lvResults3 =
				laInventorySummary
					.qryReuseInventoryForInventorySummary(
					laInventorySummaryData);
			for (int l = 0; l < lvResults3.size(); l++)
			{
				//Update/Insert InventorySummary
				InventorySummaryData laInvSummaryData =
					(InventorySummaryData) lvResults3.get(l);
				int liUpdate =
					laInventorySummary.updInventorySummary(
						laInvSummaryData);
				if (liUpdate == 0)
				{
					laInventorySummary.insInventorySummary(
						laInvSummaryData);
				}
			}

			//Select inventory voided trans data
			Vector lvResults4 =
				laInventorySummary.qryVoidInventoryForInventorySummary(
					laInventorySummaryData);

			for (int m = 0; m < lvResults4.size(); m++)
			{
				//Update/Insert InventorySummary
				InventorySummaryData laInvSummaryData2 =
					(InventorySummaryData) lvResults4.get(m);
				int liUpdate =
					laInventorySummary.updInventorySummary(
						laInvSummaryData2);
				if (liUpdate == 0)
				{
					laInventorySummary.insInventorySummary(
						laInvSummaryData2);
				}
			}

			// RTS_SUBSTA_SUMMARY 
			// Update SubstationSummary w/ Current Timestamp
			laSubstationSummary.updSubstationSummary(
				laSubstationSummaryData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END 

			//Generate County Wide report in Batch mode
			if (laFundsData.isBatch()
				&& laFundsData.getSubStationId() == 0
				&& laFundsData.runCountyWide())
			{
				// UOW #2 BEGIN 
				laDBAccess.beginTransaction();
				int liAMDate =
					laSubstationSummary.qrySubstationSummary(
						laFundsData.getOfficeIssuanceNo());
				RTSDate laRTSDate =
					new RTSDate(RTSDate.AMDATE, liAMDate);
				laFundsData.setSummaryEffDate(laRTSDate);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #2 END
				return generateCountywideReport(laFundsData);
			}

			// Generate and Format Substation Report and info    

			GenSubStationSummaryReports lGenRpt =
				new GenSubStationSummaryReports(
					"BLANK",
					new ReportProperties());

			lGenRpt.formatReport(laFundsData);

			// defect 8628
			boolean lbBatch = laFundsData.isBatch();
			String lsFileName = lbBatch ? "SSTBAT" : "SSTONLN";
			int liNumCopies =
				lbBatch
					? ReportConstant.RPT_7_COPIES
					: ReportConstant.RPT_1_COPY;

			return new ReportSearchData(
				lGenRpt.getFormattedReport().toString(),
				lsFileName,
				"SUBSTATION SUMMARY REPORT",
				liNumCopies,
				ReportConstant.PORTRAIT);
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * TransRecon report
	 *
	 * @param  aaFundsData FundsData
	 * @return Object
	 * @throws RTSException 
	 */
	public Object generateTransReconReport(FundsData aaFundsData)
		throws RTSException
	{
		FundsReportData laFundsReportData =
			(FundsReportData) aaFundsData.getFundsReportData();

		// defect 8628 
		ReportSearchData laRptSearchData = new ReportSearchData();
		// end defect 8628

		DatabaseAccess laDBAccess = new DatabaseAccess();

		TransactionReconciliationReportSQL laTransReconRptSQL =
			new TransactionReconciliationReportSQL(laDBAccess);
		try
		{
			// defect 8628 
			// UOW #1 BEGIN
			ReportProperties laRptProps =
				initReportProperties(
					aaFundsData,
					laDBAccess,
					ReportConstant.CLOSEOUT_STATISTICS_REPORT_ID);
			// UOW #1 END 
			// end defect 8628

			laRptProps.setPageOrientation(ReportConstant.PORTRAIT);

			TransReconReport laTransReconReport =
				new TransReconReport(
					FundsConstant.TRANSACTION_REPORT,
					laRptProps);

			// Get Timestamps for Date Range if user has not entered them
			// Pass DatabaseAccess, UOW managed within setTimeStamps 
			if (laFundsReportData.getRange()
				!= FundsConstant.DATE_RANGE)
			{
				aaFundsData = setTimeStamps(aaFundsData, laDBAccess);
			}

			//Reset Report Status vector
			if (laFundsReportData.getEntity()
				== FundsConstant.CASH_DRAWER)
			{
				for (int i = 0;
					i < aaFundsData.getSelectedCashDrawers().size();
					i++)
				{
					CashWorkstationCloseOutData laCashWksCloseOutData =
						(CashWorkstationCloseOutData) aaFundsData
							.getSelectedCashDrawers()
							.elementAt(i);
					laCashWksCloseOutData.setRptStatus("");
				}
			}
			else if (
				laFundsReportData.getEntity()
					== FundsConstant.EMPLOYEE)
			{
				for (int i = 0;
					i < aaFundsData.getSelectedEmployees().size();
					i++)
				{
					EmployeeData lEmployeeObj =
						(EmployeeData) aaFundsData
							.getSelectedEmployees()
							.elementAt(
							i);
					lEmployeeObj.setRptStatus("");
				}
			}

			// If no data returned from timestamp update, 
			// no transactions exist 
			if (laFundsReportData.getRange()
				!= FundsConstant.DATE_RANGE
				&& aaFundsData.getSelectedCashDrawers() == null)
			{
				if (aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					for (int i = 0;
						i < aaFundsData.getSelectedCashDrawers().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						CashWorkstationCloseOutData laCashWksCloseOutData =
							(CashWorkstationCloseOutData) aaFundsData
								.getSelectedCashDrawers()
								.get(
								i);
						laEntityStatusData.setCashWsId(
							laCashWksCloseOutData.getCashWsId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laTransReconReport.cvTrrStatVec.add(
							laEntityStatusData);
					}
				}
				else
				{
					for (int i = 0;
						i < aaFundsData.getSelectedEmployees().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						EmployeeData laEmployleeData =
							(EmployeeData) aaFundsData
								.getSelectedEmployees()
								.get(
								i);
						laEntityStatusData.setEmployeeId(
							laEmployleeData.getEmployeeId());
						// defect 9943 
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laTransReconReport.cvTrrStatVec.add(
							laEntityStatusData);
					}
				}
				caTransReportStatus.setReportName(
					FundsConstant.TRANSACTION_REPORT);
				caTransReportStatus.setStatusVector(
					(Vector) laTransReconReport.cvTrrStatVec);
			}
			//Else query for transactions
			else
			{
				Vector[] larrVector = new Vector[4];

				// UOW #2 BEGIN
				laDBAccess.beginTransaction();
				larrVector[0] =
					laTransReconRptSQL
						.qryTransactionReconciliationReportTransaction(
						aaFundsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #2 END 

				// UOW #3 BEGIN 	
				laDBAccess.beginTransaction();
				larrVector[1] =
					laTransReconRptSQL
						.qryTransactionReconciliationReportPayment(
						aaFundsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #3 END 

				// UOW #4 BEGIN
				laDBAccess.beginTransaction();
				larrVector[2] =
					laTransReconRptSQL
						.qryTransactionReconciliationReportFees(
						aaFundsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #4 END 

				// UOW #5 BEGIN	
				laDBAccess.beginTransaction();
				larrVector[3] =
					laTransReconRptSQL
						.qryTransactionReconciliationReportInventory(
						aaFundsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #5 END

				// Release DatabaseAccess
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);

				//If no data was returned from query results
				// defect 9943
				// Implement FundsConstant 	
				if (larrVector[0].size() == 0)
				{
					if (aaFundsData.getFundsReportData().getEntity()
						== FundsConstant.CASH_DRAWER)
					{
						for (int i = 0;
							i
								< aaFundsData
									.getSelectedCashDrawers()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							CashWorkstationCloseOutData laCashWksCloseOutData =
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i);
							laEntityStatusData.setCashWsId(
								laCashWksCloseOutData.getCashWsId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943
							laTransReconReport.cvTrrStatVec.add(
								laEntityStatusData);
						}
					}
					else
					{
						for (int i = 0;
							i
								< aaFundsData
									.getSelectedEmployees()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							EmployeeData laEmployleeData =
								(EmployeeData) aaFundsData
									.getSelectedEmployees()
									.get(
									i);
							laEntityStatusData.setEmployeeId(
								laEmployleeData.getEmployeeId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943 
							laTransReconReport.cvTrrStatVec.add(
								laEntityStatusData);
						}
					}
					caTransReportStatus.setReportName(
						FundsConstant.TRANSACTION_REPORT);
					caTransReportStatus.setStatusVector(
						(Vector) laTransReconReport.cvTrrStatVec);
				}
				//Else if query results had data, generate and format report
				else
				{
					if (aaFundsData.getFundsReportData().getEntity()
						== FundsConstant.CASH_DRAWER)
					{
						for (int i = 0;
							i
								< aaFundsData
									.getSelectedCashDrawers()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							CashWorkstationCloseOutData laCashWksCloseOutData =
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i);
							laEntityStatusData.setCashWsId(
								laCashWksCloseOutData.getCashWsId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.REPORT_GENERATED);
							//"Report Generated");
							// end defect 9943

							laTransReconReport.cvTrrStatVec.add(
								laEntityStatusData);
						}
					}
					else
					{
						for (int i = 0;
							i
								< aaFundsData
									.getSelectedEmployees()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							EmployeeData laEmployleeData =
								(EmployeeData) aaFundsData
									.getSelectedEmployees()
									.get(
									i);
							laEntityStatusData.setEmployeeId(
								laEmployleeData.getEmployeeId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.REPORT_GENERATED);
							//"Report Generated");
							// end defect 9943
							laTransReconReport.cvTrrStatVec.add(
								laEntityStatusData);
						}
					}
					laTransReconReport.formatReport(
						larrVector,
						aaFundsData);

					// defect 8628 
					laRptSearchData =
						new ReportSearchData(
							laTransReconReport
								.getFormattedReport()
								.toString(),
							"TRR",
							FundsConstant.TRANSACTION_REPORT,
							ReportConstant.RPT_7_COPIES,
							ReportConstant.PORTRAIT);
					// end defect 8628 

					caTransReportStatus =
						(
							ReportStatusData) laTransReconReport
								.cReportStatusData;
					caTransReportStatus.setStatusVector(
						(Vector) laTransReconReport.cvTrrStatVec);
				}
			}
			// defect 8628
			return laRptSearchData;
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * Payment report
	 *
	 * @param  aaFundsData FundsData
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genPaymentReport(FundsData aaFundsData)
		throws RTSException
	{
		FundsReportData laFundsReportData =
			(FundsReportData) aaFundsData.getFundsReportData();

		// defect 8628 
		ReportSearchData laRptSearchData = new ReportSearchData();
		// end defect 8628

		DatabaseAccess laDBAccess = new DatabaseAccess();

		PaymentTypeReportSQL laPymtRptSQL =
			new PaymentTypeReportSQL(laDBAccess);

		try
		{
			// defect 8628 
			// UOW #1 BEGIN
			ReportProperties laRptProps =
				initReportProperties(
					aaFundsData,
					laDBAccess,
					"RTS.POS.5211");
			// UOW #1 END 
			// end defect 8628

			laRptProps.setPageOrientation(ReportConstant.PORTRAIT);

			PaymentReportCloseOut laPaymentReportCloseOut =
				new PaymentReportCloseOut(
					FundsConstant.PAYMENT_REPORT,
					laRptProps);

			// Get the appropriate Report Number and Title
			int liReportType =
				aaFundsData.getFundsReportData().getRange();

			// set up for testing
			laPaymentReportCloseOut.setReportNum(liReportType);
			laPaymentReportCloseOut.caRpt.csName =
				laPaymentReportCloseOut.csReportName;

			//Get Time Stamps for Date Range if user has not ented them
			if (laFundsReportData.getRange()
				!= FundsConstant.DATE_RANGE
				&& laFundsReportData.getRange()
					!= FundsConstant.CLOSE_OUT_FOR_DAY)
			{
				aaFundsData = setTimeStamps(aaFundsData, laDBAccess);
			}

			// Clear Status if function is not CloseOut for the day, 
			// If it is closeoutday, status has already been set in 
			// generatePaymentReport
			if (aaFundsData.getFundsReportData().getRange()
				!= FundsConstant.CLOSE_OUT_FOR_DAY)
			{
				if (aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					for (int i = 0;
						i < aaFundsData.getSelectedCashDrawers().size();
						i++)
					{
						CashWorkstationCloseOutData laCashWksCloseOutData =
							(CashWorkstationCloseOutData) aaFundsData
								.getSelectedCashDrawers()
								.elementAt(i);
						laCashWksCloseOutData.setRptStatus("");
					}
				}
				if (aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.EMPLOYEE)
				{
					for (int i = 0;
						i < aaFundsData.getSelectedEmployees().size();
						i++)
					{
						EmployeeData lEmployeeObj =
							(EmployeeData) aaFundsData
								.getSelectedEmployees()
								.elementAt(
								i);
						lEmployeeObj.setRptStatus("");
					}
				}
			}
			// If no data returned from timestamp update, 
			// no transactions exists 	
			if (laFundsReportData.getRange()
				!= FundsConstant.DATE_RANGE
				&& aaFundsData.getSelectedCashDrawers() == null)
			{
				if (aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					for (int i = 0;
						i < aaFundsData.getSelectedCashDrawers().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						CashWorkstationCloseOutData laCashWksCloseOutData =
							(CashWorkstationCloseOutData) aaFundsData
								.getSelectedCashDrawers()
								.get(
								i);
						laEntityStatusData.setCashWsId(
							laCashWksCloseOutData.getCashWsId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laPaymentReportCloseOut.getPymtStatVec().add(
							laEntityStatusData);
					}
				}
				else
				{
					for (int i = 0;
						i < aaFundsData.getSelectedEmployees().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						EmployeeData laEmployeeDataObj =
							(EmployeeData) aaFundsData
								.getSelectedEmployees()
								.get(
								i);
						laEntityStatusData.setEmployeeId(
							laEmployeeDataObj.getEmployeeId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laPaymentReportCloseOut.getPymtStatVec().add(
							laEntityStatusData);
					}
				}
				caPymtReportStatus.setReportName(
					FundsConstant.PAYMENT_REPORT);
				caPymtReportStatus.setStatusVector(
					(Vector) laPaymentReportCloseOut.getPymtStatVec());
			}
			//Else query for transactions
			else
			{
				// UOW #2 BEGIN 
				laDBAccess.beginTransaction();
				Vector lvQueryResults =
					laPymtRptSQL.qryPaymentTypeReport(aaFundsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #2 END

				if (lvQueryResults.size() == 0)
				{
					if (aaFundsData.getFundsReportData().getEntity()
						== FundsConstant.CASH_DRAWER)
					{
						for (int i = 0;
							i
								< aaFundsData
									.getSelectedCashDrawers()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							CashWorkstationCloseOutData laCashWksCloseOutData =
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i);
							laEntityStatusData.setCashWsId(
								laCashWksCloseOutData.getCashWsId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943
							laPaymentReportCloseOut
								.getPymtStatVec()
								.add(
								laEntityStatusData);
						}
					}
					else
					{
						for (int i = 0;
							i
								< aaFundsData
									.getSelectedEmployees()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							EmployeeData laEmployeeDataObj =
								(EmployeeData) aaFundsData
									.getSelectedEmployees()
									.get(
									i);
							laEntityStatusData.setEmployeeId(
								laEmployeeDataObj.getEmployeeId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943 
							laPaymentReportCloseOut
								.getPymtStatVec()
								.add(
								laEntityStatusData);
						}
					}
					caPymtReportStatus.setReportName(
						FundsConstant.PAYMENT_REPORT);
					caPymtReportStatus.setStatusVector(
						(Vector) laPaymentReportCloseOut
							.getPymtStatVec());
				}
				//Else if query results had data, generate and format report	
				else
				{
					laPaymentReportCloseOut.ciEntity =
						aaFundsData.getFundsReportData().getEntity();
					laPaymentReportCloseOut.ciPrimarySplit =
						aaFundsData
							.getFundsReportData()
							.getPrimarySplit();
					laPaymentReportCloseOut.setUpBreakByCode();
					switch (laPaymentReportCloseOut.ciBreakByCode)
					{
						case PaymentReportCloseOut.CASH_DRAWER_NONE :
						case PaymentReportCloseOut.CASH_DRAWER_EMP :
							{
								laPaymentReportCloseOut
									.setSortKeysForByCashDrawer(
									lvQueryResults);
								break;
							}
						case PaymentReportCloseOut.EMP_NONE :
						case PaymentReportCloseOut.EMP_CASH_DRAWER :
							{
								laPaymentReportCloseOut
									.setSortKeysForByEmployee(
									lvQueryResults);
								break;
							}
					}

					// Sort the results
					UtilityMethods.sort(lvQueryResults);
					laPaymentReportCloseOut.formatReport(
						lvQueryResults,
						aaFundsData);

					// defect 8628 
					String lsFileName = new String();
					String lsReportTitle = new String();
					int liRange =
						aaFundsData.getFundsReportData().getRange();
					switch (liRange)
					{
						case FundsConstant.CURRENT_STATUS :
							{
								lsFileName = "PMC";
								lsReportTitle = " CURRENT STATUS";
								break;
							}
						case FundsConstant.CLOSE_OUT_FOR_DAY :
							{
								lsFileName = "PMS";
								lsReportTitle = "CLOSEOUT";
								break;
							}
						default :
							{
								lsFileName = "PMT";
								lsReportTitle =
									FundsConstant.PAYMENT_REPORT;
							}
					}

					laRptSearchData =
						new ReportSearchData(
							laPaymentReportCloseOut
								.getFormattedReport()
								.toString(),
							lsFileName,
							lsReportTitle,
							ReportConstant.RPT_7_COPIES,
							ReportConstant.PORTRAIT);
					// end defect 8628 

					caPymtReportStatus =
						(ReportStatusData) laPaymentReportCloseOut
							.getPymtStatus();
					caPymtReportStatus.setStatusVector(
						(Vector) laPaymentReportCloseOut
							.getPymtStatVec());
				}
			}
			if (aaFundsData.getFundsReportData().getRange()
				== FundsConstant.CURRENT_STATUS)
			{
				updateCurrentStatus(aaFundsData, laDBAccess);
			}

			// defect 8628
			return laRptSearchData;
			// end defect 8628 
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method calls the services funds reports class to generate the
	 * Payment report show source
	 *
	 * @param  aaFundsData FundsData
	 * @return Object
	 * @throws RTSException 
	 */
	public Object genPaymentReportShowSource(FundsData aaFundsData)
		throws RTSException
	{
		FundsReportData laFundsReportData =
			(FundsReportData) aaFundsData.getFundsReportData();

		// defect 8628 
		ReportSearchData laRptSearchData = new ReportSearchData();
		// end defect 8628

		DatabaseAccess laDBAccess = new DatabaseAccess();

		PaymentTypeReportSQL laPymtRptSQL =
			new PaymentTypeReportSQL(laDBAccess);

		try
		{
			// defect 8628 
			// UOW #1 BEGIN
			ReportProperties laRptProps =
				initReportProperties(
					aaFundsData,
					laDBAccess,
					"RTS.POS.5211");
			// UOW #1 END 
			// end defect 8628

			laRptProps.setPageOrientation(ReportConstant.LANDSCAPE);

			PaymentReportCloseOutShowSource laPymtRptCloseOutShowSource =
				new PaymentReportCloseOutShowSource(
					FundsConstant.PAYMENT_REPORT,
					laRptProps);

			// Get the appropriate Report Number and Title
			int liReportType =
				aaFundsData.getFundsReportData().getRange();
			// set up for testing
			laPymtRptCloseOutShowSource.setReportNum(liReportType);
			laPymtRptCloseOutShowSource.caRpt.csName =
				laPymtRptCloseOutShowSource.csReportName;

			//Get Time Stamps for Date Range if user has not entered them	
			if (laFundsReportData.getRange()
				!= FundsConstant.DATE_RANGE)
			{
				aaFundsData = setTimeStamps(aaFundsData, laDBAccess);
			}
			// Clear Status if function is not CloseOut for the day, 
			// If it is closeoutday, status has already been set in 
			// generatePaymentReport
			if (aaFundsData.getFundsReportData().getRange()
				!= FundsConstant.CLOSE_OUT_FOR_DAY)
			{
				if (aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					for (int i = 0;
						i < aaFundsData.getSelectedCashDrawers().size();
						i++)
					{
						CashWorkstationCloseOutData laCashWksCloseOutData =
							(CashWorkstationCloseOutData) aaFundsData
								.getSelectedCashDrawers()
								.elementAt(i);
						laCashWksCloseOutData.setRptStatus("");
					}
				}
				if (aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.EMPLOYEE)
				{
					for (int i = 0;
						i < aaFundsData.getSelectedEmployees().size();
						i++)
					{
						EmployeeData laEmployeeData =
							(EmployeeData) aaFundsData
								.getSelectedEmployees()
								.elementAt(
								i);
						laEmployeeData.setRptStatus("");
					}
				}
			}

			// If no data returned from timestamp update, 
			// no transactions exist    	
			if (laFundsReportData.getRange()
				!= FundsConstant.DATE_RANGE
				&& aaFundsData.getSelectedCashDrawers() == null)
			{
				if (aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					for (int i = 0;
						i < aaFundsData.getSelectedCashDrawers().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						CashWorkstationCloseOutData laCashWksCloseOutData =
							(CashWorkstationCloseOutData) aaFundsData
								.getSelectedCashDrawers()
								.get(
								i);
						laEntityStatusData.setCashWsId(
							laCashWksCloseOutData.getCashWsId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laPymtRptCloseOutShowSource
							.getPymtStatVec()
							.add(
							laEntityStatusData);
					}
				}
				else
				{
					for (int i = 0;
						i < aaFundsData.getSelectedEmployees().size();
						i++)
					{
						EntityStatusData laEntityStatusData =
							new EntityStatusData();
						EmployeeData laEmployeeData =
							(EmployeeData) aaFundsData
								.getSelectedEmployees()
								.get(
								i);
						laEntityStatusData.setEmployeeId(
							laEmployeeData.getEmployeeId());
						// defect 9943
						laEntityStatusData.setReportStatus(
							FundsConstant.NO_TRANSACTIONS);
						//"No Transactions");
						// end defect 9943 
						laPymtRptCloseOutShowSource
							.getPymtStatVec()
							.add(
							laEntityStatusData);
					}
				}
				caPymtReportStatus.setReportName(
					FundsConstant.PAYMENT_REPORT);
				caPymtReportStatus.setStatusVector(
					(Vector) laPymtRptCloseOutShowSource
						.getPymtStatVec());
			}
			//Else query for transactions
			else
			{
				laDBAccess.beginTransaction();
				Vector lvQueryResults =
					laPymtRptSQL.qryPaymentTypeReport(aaFundsData);
				laDBAccess.endTransaction(DatabaseAccess.COMMIT);

				if (lvQueryResults.size() == 0)
				{
					if (aaFundsData.getFundsReportData().getEntity()
						== FundsConstant.CASH_DRAWER)
					{
						for (int i = 0;
							i
								< aaFundsData
									.getSelectedCashDrawers()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							CashWorkstationCloseOutData laCashWksCloseOutData =
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i);
							laEntityStatusData.setCashWsId(
								laCashWksCloseOutData.getCashWsId());
							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943 
							laPymtRptCloseOutShowSource
								.getPymtStatVec()
								.add(
								laEntityStatusData);
						}
					}
					else
					{
						for (int i = 0;
							i
								< aaFundsData
									.getSelectedEmployees()
									.size();
							i++)
						{
							EntityStatusData laEntityStatusData =
								new EntityStatusData();
							EmployeeData laEmployeeData =
								(EmployeeData) aaFundsData
									.getSelectedEmployees()
									.get(
									i);
							laEntityStatusData.setEmployeeId(
								laEmployeeData.getEmployeeId());

							// defect 9943
							laEntityStatusData.setReportStatus(
								FundsConstant.NO_TRANSACTIONS);
							//"No Transactions");
							// end defect 9943
							laPymtRptCloseOutShowSource
								.getPymtStatVec()
								.add(
								laEntityStatusData);
						}
					}
					caPymtReportStatus.setReportName(
						FundsConstant.PAYMENT_REPORT);
					caPymtReportStatus.setStatusVector(
						(Vector) laPymtRptCloseOutShowSource
							.getPymtStatVec());
				}
				//Else if query results had data, generate and format report
				else
				{
					laPymtRptCloseOutShowSource.ciEntity =
						aaFundsData.getFundsReportData().getEntity();
					laPymtRptCloseOutShowSource.ciPrimarySplit =
						aaFundsData
							.getFundsReportData()
							.getPrimarySplit();
					laPymtRptCloseOutShowSource.setUpBreakByCode();
					// defect 6735
					// use static constants in case statements
					switch (laPymtRptCloseOutShowSource.ciBreakByCode)
					{
						case PaymentReportCloseOutShowSource
							.CASH_DRAWER_NONE :
						case PaymentReportCloseOutShowSource
							.CASH_DRAWER_EMP :
							{
								laPymtRptCloseOutShowSource
									.setSortKeysForByCashDrawer(
									lvQueryResults);
								break;
							}
						case PaymentReportCloseOutShowSource.EMP_NONE :
						case PaymentReportCloseOutShowSource
							.EMP_CASH_DRAWER :
							{
								laPymtRptCloseOutShowSource
									.setSortKeysForByEmployee(
									lvQueryResults);
								break;
							}
					}
					// end defect 6735
					// Sort the results
					UtilityMethods.sort(lvQueryResults);
					laPymtRptCloseOutShowSource.formatReport(
						lvQueryResults,
						aaFundsData);

					// defect 8628 
					String lsFileName = lsFileName = "PMT";
					String lsReportTitle = FundsConstant.PAYMENT_REPORT;

					if (aaFundsData.getFundsReportData().getRange()
						== FundsConstant.CURRENT_STATUS)
					{
						lsFileName = "PMC";
						lsReportTitle = " CURRENT STATUS";
					}

					laRptSearchData =
						new ReportSearchData(
							laPymtRptCloseOutShowSource
								.getFormattedReport()
								.toString(),
							lsFileName,
							lsReportTitle,
							ReportConstant.RPT_7_COPIES,
							ReportConstant.LANDSCAPE);
					// end defect 8628 

					caPymtReportStatus =
						(ReportStatusData) laPymtRptCloseOutShowSource
							.getPymtStatus();
					caPymtReportStatus.setStatusVector(
						(Vector) laPymtRptCloseOutShowSource
							.getPymtStatVec());
				}
			}
			if (aaFundsData.getFundsReportData().getRange()
				== FundsConstant.CURRENT_STATUS)
			{
				updateCurrentStatus(aaFundsData, laDBAccess);
			}
			// defect 8628
			return laRptSearchData;
			// end defect 8628  
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method queries the db and returns all workstation information associted 
	 * with a ofcissuance, subsation combinaton
	 *
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException 
	 */
	public Object getAllCashDrwrs(Object aaData) throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;

		DatabaseAccess laDBAccess = new DatabaseAccess();

		CashWorkstationCloseOut laCashWksCloseOut =
			new CashWorkstationCloseOut(laDBAccess);

		try
		{
			// CashWorkstationCloseOutData Object for Query 
			CashWorkstationCloseOutData laCashWksCloseOutData =
				new CashWorkstationCloseOutData();
			laCashWksCloseOutData.setOfcIssuanceNo(
				laFundsData.getOfficeIssuanceNo());
			laCashWksCloseOutData.setSubstaId(
				laFundsData.getSubStationId());
			laCashWksCloseOutData.setCashWsId(Integer.MIN_VALUE);

			// UOW #1 BEGIN
			laDBAccess.beginTransaction();
			Vector lvDrawers =
				laCashWksCloseOut.qryCashWorkstationCloseOut(
					laCashWksCloseOutData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			//Return results in laFundsData
			laFundsData.setCashDrawers(lvDrawers);
			return laFundsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}

		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method queries the db and returns a speicific workstation information associated 
	 * with a ofcissuance, subsation combinatopm
	 *
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException  
	 */
	public Object getCashDrwr(Object aaData) throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;

		DatabaseAccess laDBAccess = new DatabaseAccess();

		CashWorkstationCloseOut laCashWksCloseOut =
			new CashWorkstationCloseOut(laDBAccess);

		try
		{
			CashWorkstationCloseOutData laCashWksCloseOutData =
				new CashWorkstationCloseOutData();
			laCashWksCloseOutData.setOfcIssuanceNo(
				laFundsData.getOfficeIssuanceNo());
			laCashWksCloseOutData.setSubstaId(
				laFundsData.getSubStationId());
			laCashWksCloseOutData.setCashWsId(
				laFundsData.getCashWsId());

			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();
			Vector lvDrawers =
				laCashWksCloseOut.qryCashWorkstationCloseOut(
					laCashWksCloseOutData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			//Return results in laFundsData
			laFundsData.setCashDrawers(lvDrawers);
			return laFundsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method queries the db and returns all workstations information 
	 * associted with a ofcissuance, subsation combinaton that have the 
	 * close out indicator set
	 *
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException  
	 */
	public Object getCashDrwrsReset(Object aaData) throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;

		DatabaseAccess laDBAccess = new DatabaseAccess();

		CashWorkstationIds laCashWksIds =
			new CashWorkstationIds(laDBAccess);

		try
		{
			CashWorkstationIdsData laCashWksIdsData =
				new CashWorkstationIdsData();
			laCashWksIdsData.setOfcIssuanceNo(
				laFundsData.getOfficeIssuanceNo());
			laCashWksIdsData.setSubstaId(laFundsData.getSubStationId());
			laCashWksIdsData.setCloseOutReqIndi(1);

			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();
			Vector lvDrawers =
				laCashWksIds.qryCashWorkstationIds(laCashWksIdsData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			//Return results in laFundsData
			laFundsData.setCashDrawers(lvDrawers);
			return laFundsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method queries the db and returns all closeouts associated with 
	 * a ofcissuance, substation combinaton that should be displayed on 
	 * FUN010
	 * 
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException  
	 */
	public Object getCashDrwrsSubstation(Object aaData)
		throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;

		DatabaseAccess laDBAccess = new DatabaseAccess();

		CloseOutHistory laCloseOutHistory =
			new CloseOutHistory(laDBAccess);

		try
		{
			//Create an obj to query db
			CloseOutHistoryData laCloseOutHistoryData =
				new CloseOutHistoryData();
			laCloseOutHistoryData.setOfcIssuanceNo(
				laFundsData.getOfficeIssuanceNo());
			laCloseOutHistoryData.setSubstaId(
				laFundsData.getSubStationId());

			// UOW #1 BEGIN
			laDBAccess.beginTransaction();
			Vector lvDrawers =
				laCloseOutHistory.qryCloseOutHistory(
					laCloseOutHistoryData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			// UOW #2 BEGIN
			// Query db for the maximum SummaryEffDate
			//*** Could also query RTS_SUBSTA_SUMMARY  (KPH) 
			laDBAccess.beginTransaction();
			int liSumEffDate =
				laCloseOutHistory.qryCloseOutHistoryMaxDate(
					laCloseOutHistoryData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END

			RTSDate laSumEffDate = new RTSDate(2, liSumEffDate);
			laFundsData.setSummaryEffDate(laSumEffDate);
			//Return data in FundsData
			laFundsData.setCashDrawers(lvDrawers);
			return laFundsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Method queries the db and returns all employee information associated 
	 * with a ofcissuance, substation combinaton
	 *
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException  
	 */
	public Object getEmployeeData(Object aaData) throws RTSException
	{
		FundsData aaFundsData = (FundsData) aaData;

		DatabaseAccess laDBAccess = new DatabaseAccess();

		Security laSecurity = new Security(laDBAccess);

		try
		{
			SecurityData laSecurityData = new SecurityData();
			laSecurityData.setOfcIssuanceNo(
				aaFundsData.getOfficeIssuanceNo());
			laSecurityData.setSubstaId(aaFundsData.getSubStationId());

			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();
			Vector lvEmp = laSecurity.qryTransSecurity(laSecurityData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			Vector lvEmployees = new java.util.Vector();

			//Extract each data in vector and put into laEmployeeData
			for (int i = 0; i < lvEmp.size(); i++)
			{
				laSecurityData = (SecurityData) lvEmp.get(i);
				EmployeeData laEmployeeData = new EmployeeData();
				laEmployeeData.setEmployeeId(laSecurityData.getEmpId());
				laEmployeeData.setFirstName(
					laSecurityData.getEmpFirstName());
				laEmployeeData.setLastName(
					laSecurityData.getEmpLastName());
				lvEmployees.add(laEmployeeData);
			}
			//Return employee objects in Funds Data
			aaFundsData.setEmployees(lvEmployees);
			return aaFundsData;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/**
	 * Get Funds related data from DB
	 *  
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException 
	 * 
	 */
	public Object getFundFuncTransAndTrFdsDetail(Object aaData)
		throws RTSException
	{
		TransactionKey laTransactionKey = (TransactionKey) aaData;
		Vector lvReturn = new Vector();

		DatabaseAccess laDBAccess = new DatabaseAccess();

		FundFunctionTransaction laFundFuncTrans =
			new FundFunctionTransaction(laDBAccess);

		TransactionFundsDetail laTransFundsDetail =
			new TransactionFundsDetail(laDBAccess);

		try
		{
			//Set FundFuncTrans attributes 
			FundFunctionTransactionData laFundFuncTransData =
				new FundFunctionTransactionData();
			laFundFuncTransData.setOfcIssuanceNo(
				laTransactionKey.getOfcIssuanceNo());
			laFundFuncTransData.setSubstaId(
				laTransactionKey.getSubstaId());
			laFundFuncTransData.setTransAMDate(
				laTransactionKey.getTransAMDate());
			laFundFuncTransData.setTransWsId(
				laTransactionKey.getTransWsId());
			laFundFuncTransData.setCustSeqNo(
				laTransactionKey.getCustSeqNo());
			laFundFuncTransData.setTransTime(
				laTransactionKey.getTransTime());

			// UOW #1 BEGIN 
			laDBAccess.beginTransaction();
			Vector lvTempVector =
				laFundFuncTrans.qryFundFunctionTransaction(
					laFundFuncTransData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			if (lvTempVector == null || lvTempVector.size() == 0)
			{
				/*	throw new RTSException(
					   RTSException.FAILURE_MESSAGE,
					  "No FundFuncTrans aaData",
					   "ERROR");
				 */
			}
			else
			{
				lvReturn.addElement(lvTempVector.get(0));
			}
			//Get TrFdsDetail
			TransactionFundsDetailData laTransFundsDetailData =
				new TransactionFundsDetailData();
			laTransFundsDetailData.setOfcIssuanceNo(
				laTransactionKey.getOfcIssuanceNo());
			laTransFundsDetailData.setSubstaId(
				laTransactionKey.getSubstaId());
			laTransFundsDetailData.setTransAMDate(
				laTransactionKey.getTransAMDate());
			laTransFundsDetailData.setTransWsId(
				laTransactionKey.getTransWsId());
			laTransFundsDetailData.setCustSeqNo(
				laTransactionKey.getCustSeqNo());
			laTransFundsDetailData.setTransTime(
				laTransactionKey.getTransTime());

			// UOW #2 BEGIN 
			laDBAccess.beginTransaction();
			lvTempVector =
				laTransFundsDetail.qryTransactionFundsDetail(
					laTransFundsDetailData);
			laDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END

			if (lvTempVector == null || lvTempVector.size() == 0)
			{
				/*  throw new RTSException(
						RTSException.FAILURE_MESSAGE,
						"No TrFdsDetail aaData",
						"ERROR");
				*/
			}
			else
			{
				lvReturn.addElement(lvTempVector);
			}
			return lvReturn;
		}
		catch (RTSException aeRTSEx)
		{
			laDBAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			laDBAccess = null;
		}
	}

	/** 
	 * Init ReportProperties
	 * 
	 * @param aaDBAccess
	 * @param aaFundsData
	 * @return ReportProperties
	 * @throws RTSException
	 */
	private ReportProperties initReportProperties(
		FundsData aaFundsData,
		DatabaseAccess aaDBAccess,
		String asReportId)
		throws RTSException
	{
		ReportsServerBusiness laRptSvrBusiness =
			new ReportsServerBusiness();

		return laRptSvrBusiness.initReportProperties(
			aaFundsData.getReportSearchData(),
			aaDBAccess,
			asReportId);
	}

	/**
	 * Process aaData directs the server method calls
	 *
	 * @param  aiModule  	int
	 * @param  aiFunctionId	int 
	 * @param  aaData 		Object
	 * @return Object
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
			// defect 10086 
			// aiModule, aiFunctionId not used in methods
			case FundsConstant.GET_ALL_CASH_DRAWERS :
				return getAllCashDrwrs(aaData);

			case FundsConstant.GET_CASH_DRAWER :
				return getCashDrwr(aaData);

			case FundsConstant.GET_EMPLOYEE_DATA :
				return getEmployeeData(aaData);

			case FundsConstant.GENERATE_PAYMENT_REPORT :
				return generatePaymentReport(aaData);

			case FundsConstant.DISPLAY_REPORTS :
				return generateReports(aaData);

			case FundsConstant.PRINT_REPORTS :
				return generateReports(aaData);

			case FundsConstant.GET_CASH_DRAWERS_RESET :
				return getCashDrwrsReset(aaData);

			case FundsConstant.RESET_DRAWERS :
				return resetCashDrwrs(aaData);

			case FundsConstant.GET_CASH_DRAWERS_SUBSTATION :
				return getCashDrwrsSubstation(aaData);

			case FundsConstant.GENERATE_CLOSEOUT_STATISTICS_REPORT :
				return generateCloseoutStatsReport(aaData);

			case FundsConstant.GENERATE_COUNTYWIDE_REPORT :
				return generateCountywideReport(aaData);

			case FundsConstant.GENERATE_SUBSTATION_SUMMARY_REPORT :
				return generateSubstationSummaryReport(aaData);

			case FundsConstant.GET_FUND_FUNC_TRANS_AND_TR_FDS_DETAIL :
				return getFundFuncTransAndTrFdsDetail(aaData);
				// end defect 10086 
		}
		return null;
	}

	/**
	 * Method updates db to reset the indicator for a specific workstation,
	 * ofcissuance, and substation combinaton
	 *
	 * @param  aaData 		Object
	 * @return Object
	 * @throws RTSException  
	 */
	public Object resetCashDrwrs(Object aaData) throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;
		Vector lvDrawers = laFundsData.getSelectedCashDrawers();

		// defect 7077
		// Reorganize for maximum DB concurrency
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();
		CashWorkstationIds laCashWksIds =
			new CashWorkstationIds(laDatabaseAccess);

		// Consider "IN" 
		try
		{
			// UOW #1 BEGIN 
			laDatabaseAccess.beginTransaction();

			// For each workstation drawer selected, update the 
			// closeoutreqindi 
			for (int i = 0; i < lvDrawers.size(); i++)
			{
				(
					(CashWorkstationIdsData) lvDrawers.get(
						i)).setCloseOutReqWsId(
					Integer.MIN_VALUE);
				(
					(CashWorkstationIdsData) lvDrawers.get(
						i)).setCloseOutReqIndi(
					0);
				int k =
					laCashWksIds.updCashWorkstationIds(
						((CashWorkstationIdsData) lvDrawers.get(i)));
			}
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			laFundsData.setSelectedCashDrawers(lvDrawers);
		}
		catch (RTSException aeRTSEx)
		{
			laDatabaseAccess.endTransaction(DatabaseAccess.ROLLBACK);
			throw aeRTSEx;
		}
		finally
		{
			// Release DatabaseAccess
			laDatabaseAccess = null;
		}
		// end defect 7077 
		return null;
	}

	/**
	 * Method will call the appropriate Database query to retrieve the 
	 * time stamps
	 * Beg and End time stamps based on if the user selected Last Close, 
	 * Since Last Close, Date Range, etc..  
	 * Further takes into account if the user selected employee as the
	 * entity, the range will differ.
	 *
	 * @param  aaFundsData FundsData
	 * @throws RTSException 
	 */
	public FundsData setTimeStamps(
		FundsData aaFundsData,
		DatabaseAccess aaDatabaseAccess)
		throws RTSException
	{
		Vector lvDrawer = new Vector();
		// defect 7077
		// Reorganize for maximum DB concurrency
		try
		{
			CashWorkstationCloseOutData laCashWksCloseOutData =
				new CashWorkstationCloseOutData();
			laCashWksCloseOutData.setOfcIssuanceNo(
				aaFundsData.getOfficeIssuanceNo());
			laCashWksCloseOutData.setSubstaId(
				aaFundsData.getSubStationId());

			CashWorkstationCloseOut laCashWksCloseOut =
				new CashWorkstationCloseOut(aaDatabaseAccess);
			Miscellaneous laMiscellaneous =
				new Miscellaneous(aaDatabaseAccess);

			//Retrieve time zone
			int liOfcIssuanceNo = aaFundsData.getOfficeIssuanceNo();
			int liSubstaId = aaFundsData.getSubStationId();

			// defect 10427 		
			//			CacheManagerServerBusiness laCacheManagerServerBusiness =
			//				new CacheManagerServerBusiness();
			//			String lsTimeZone =
			//				laCacheManagerServerBusiness.getOfficeTimeZone(
			//					liOfcIssuanceNo,
			//					liSubstaId);
			String lsTimeZone =
				OfficeTimeZoneCache.getTimeZone(liOfcIssuanceNo);
			// end defect 10427 

			// UOW #1 BEGIN 
			// Qry for database time to get current timestamp, returned in Central Time
			aaDatabaseAccess.beginTransaction();
			String lsDatabaseDate =
				((RTSDate) laMiscellaneous
					.qryCurrentTimestamp(liOfcIssuanceNo, liSubstaId))
					.getDB2Date();
			aaDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			// Adjust current time based on time zone
			RTSDate laTimeZoneAdjustedDate =
				new RTSDate(lsDatabaseDate).getTimeZoneAdjustedDate(
					lsTimeZone);
			//Set current time to adjusted time 
			laCashWksCloseOutData.setCurrentTimestmp(
				laTimeZoneAdjustedDate);

			//Queries to set timestamps if entity = employee
			// UOW #2 BEGIN
			aaDatabaseAccess.beginTransaction();
			if (aaFundsData.getFundsReportData().getEntity()
				== FundsConstant.EMPLOYEE)
			{
				laCashWksCloseOutData.setCashWsId(Integer.MIN_VALUE);
				switch (aaFundsData.getFundsReportData().getRange())
				{
					case (FundsConstant.LAST_CLOSE) :
						lvDrawer =
							laCashWksCloseOut.qryLastCloseOut(
								laCashWksCloseOutData);
						break;
					case (FundsConstant.AFTER_SUBSTATION) :
						lvDrawer =
							laCashWksCloseOut
								.qrySinceSubstationSummary(
								laCashWksCloseOutData);
						break;
					case (FundsConstant.SINCE_CURRENT) :
						lvDrawer =
							laCashWksCloseOut.qryCurrentStatus(
								laCashWksCloseOutData);
						break;
					case (FundsConstant.CURRENT_STATUS) :
						lvDrawer =
							laCashWksCloseOut.qryCurrentStatus(
								laCashWksCloseOutData);
						break;
					case (FundsConstant.SINCE_CLOSE) :
						lvDrawer =
							laCashWksCloseOut.qrySinceLastCloseOut(
								laCashWksCloseOutData);
						break;
					case (FundsConstant.CLOSE_OUT_FOR_DAY) :
						lvDrawer =
							laCashWksCloseOut.qrySinceLastCloseOut(
								laCashWksCloseOutData);
						break;
				}
				if (lvDrawer.size() != 0)
				{
					aaFundsData.setSelectedCashDrawers(lvDrawer);
				}
			}
			// Queries to set time stamps if entity=cash drawer
			else if (
				aaFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
			{
				Vector lvCashDrawers =
					aaFundsData.getSelectedCashDrawers();
				switch (aaFundsData.getFundsReportData().getRange())
				{
					case (FundsConstant.LAST_CLOSE) :
						for (int i = 0; i < lvCashDrawers.size(); i++)
						{
							CashWorkstationCloseOutData laCashDrawer =
								(
									CashWorkstationCloseOutData) lvCashDrawers
										.get(
									i);
							laCashWksCloseOutData.setCashWsId(
								laCashDrawer.getCashWsId());
							lvDrawer =
								laCashWksCloseOut.qryLastCloseOut(
									laCashWksCloseOutData);
							CashWorkstationCloseOutData laDrawer =
								(
									CashWorkstationCloseOutData) lvDrawer
										.get(
									0);
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutBegTstmp(
								laDrawer.getCloseOutBegTstmp());
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutEndTstmp(
								laDrawer.getCloseOutEndTstmp());
						}
						break;
					case (FundsConstant.AFTER_SUBSTATION) :
						for (int i = 0; i < lvCashDrawers.size(); i++)
						{
							CashWorkstationCloseOutData laCashDrawer =
								(
									CashWorkstationCloseOutData) lvCashDrawers
										.get(
									i);
							laCashWksCloseOutData.setCashWsId(
								laCashDrawer.getCashWsId());
							lvDrawer =
								laCashWksCloseOut
									.qrySinceSubstationSummary(
									laCashWksCloseOutData);
							if (lvDrawer.size() != 0)
							{
								CashWorkstationCloseOutData laDrawer =
									(
										CashWorkstationCloseOutData) lvDrawer
											.get(
										0);
								(
									(CashWorkstationCloseOutData) aaFundsData
										.getSelectedCashDrawers()
										.get(
										i)).setCloseOutBegTstmp(
									laDrawer.getCloseOutBegTstmp());
								(
									(CashWorkstationCloseOutData) aaFundsData
										.getSelectedCashDrawers()
										.get(
										i)).setCloseOutEndTstmp(
									laDrawer.getCloseOutEndTstmp());
							}
						}
						break;
					case (FundsConstant.SINCE_CURRENT) :
						for (int i = 0; i < lvCashDrawers.size(); i++)
						{
							CashWorkstationCloseOutData laCashDrawer =
								(
									CashWorkstationCloseOutData) lvCashDrawers
										.get(
									i);
							laCashWksCloseOutData.setCashWsId(
								laCashDrawer.getCashWsId());
							lvDrawer =
								laCashWksCloseOut.qryCurrentStatus(
									laCashWksCloseOutData);
							CashWorkstationCloseOutData laDrawer =
								(
									CashWorkstationCloseOutData) lvDrawer
										.get(
									0);
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutBegTstmp(
								laDrawer.getCloseOutBegTstmp());
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutEndTstmp(
								laDrawer.getCloseOutEndTstmp());
						}
						break;
					case (FundsConstant.CURRENT_STATUS) :
						for (int i = 0; i < lvCashDrawers.size(); i++)
						{
							CashWorkstationCloseOutData laCashDrawer =
								(
									CashWorkstationCloseOutData) lvCashDrawers
										.get(
									i);
							laCashWksCloseOutData.setCashWsId(
								laCashDrawer.getCashWsId());
							lvDrawer =
								laCashWksCloseOut.qryCurrentStatus(
									laCashWksCloseOutData);
							CashWorkstationCloseOutData laDrawer =
								(
									CashWorkstationCloseOutData) lvDrawer
										.get(
									0);
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutBegTstmp(
								laDrawer.getCloseOutBegTstmp());
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutEndTstmp(
								laDrawer.getCloseOutEndTstmp());
						}
						break;
					case (FundsConstant.SINCE_CLOSE) :
						for (int i = 0; i < lvCashDrawers.size(); i++)
						{
							CashWorkstationCloseOutData laCashDrawer =
								(
									CashWorkstationCloseOutData) lvCashDrawers
										.get(
									i);
							laCashWksCloseOutData.setCashWsId(
								laCashDrawer.getCashWsId());
							lvDrawer =
								laCashWksCloseOut.qrySinceLastCloseOut(
									laCashWksCloseOutData);
							CashWorkstationCloseOutData laDrawer =
								(
									CashWorkstationCloseOutData) lvDrawer
										.get(
									0);
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutBegTstmp(
								laDrawer.getCloseOutBegTstmp());
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutEndTstmp(
								laDrawer.getCloseOutEndTstmp());
						}
						break;
					case (FundsConstant.CLOSE_OUT_FOR_DAY) :
						for (int i = 0; i < lvCashDrawers.size(); i++)
						{
							CashWorkstationCloseOutData laCashDrawer =
								(
									CashWorkstationCloseOutData) lvCashDrawers
										.get(
									i);
							laCashWksCloseOutData.setCashWsId(
								laCashDrawer.getCashWsId());
							lvDrawer =
								laCashWksCloseOut.qrySinceLastCloseOut(
									laCashWksCloseOutData);
							CashWorkstationCloseOutData laDrawer =
								(
									CashWorkstationCloseOutData) lvDrawer
										.get(
									0);
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutBegTstmp(
								laDrawer.getCloseOutBegTstmp());
							(
								(CashWorkstationCloseOutData) aaFundsData
									.getSelectedCashDrawers()
									.get(
									i)).setCloseOutEndTstmp(
								laDrawer.getCloseOutEndTstmp());
						}
						break;
				}
			}
			aaDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #2 END
			// Return Funds Data object which now contains updated timestamps
			// end defect 7077 
			return aaFundsData;
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;

		}
	}

	/**
	 * Method sorts the Inventory results returned from the query for
	 * the Inventory Summary Report.
	 *
	 * @param  aaFundsData
	 * @return Vector 
	 */
	public Vector sortInventoryResults(FundsData aaFundsData)
	{
		Vector lvInvData = (Vector) aaFundsData.getInventoryData();
		Vector lvInventory = new Vector();

		//Match algortithm: Matches data while the itm type & year are the same.  Totals
		//the amounts for each of the categories (Sold, Voided, or Reused)

		int liCount = -1;

		for (int i = 0; i < lvInvData.size(); i++)
		{
			int j = liCount;
			boolean lbMatch = false;
			do
			{
				if (i > 0 && j >= 0)
				{
					InventorySummaryTypeReportData laInvSumTypeRptData =
						(InventorySummaryTypeReportData) (lvInvData
							.get(i));
					InventoryData laInvData =
						(InventoryData) (lvInventory.get(j));

					if (aaFundsData.getFundsReportData().getEntity()
						== FundsConstant.CASH_DRAWER
						&& aaFundsData
							.getFundsReportData()
							.getPrimarySplit()
							== FundsConstant.NONE)
					{
						if (laInvSumTypeRptData.getCashWsId()
							== laInvData.getCashWsId()
							&& laInvSumTypeRptData.getItmCdDesc().equals(
								laInvData.getItmCdDesc())
							&& laInvSumTypeRptData.getInvItmYr()
								== laInvData.getInvItmYr()
							&& !lbMatch)
						{
							lbMatch = true;
							if (laInvSumTypeRptData
								.getInvStatus()
								.equals("SOLD"))
							{
								laInvData.setQtySold(
									laInvData.getQtySold()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							else if (
								laInvSumTypeRptData
									.getInvStatus()
									.equals(
									"VOIDED"))
							{
								laInvData.setQtyVoided(
									laInvData.getQtyVoided()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							else if (
								laInvSumTypeRptData
									.getInvStatus()
									.equals(
									"REUSED"))
							{
								laInvData.setQtyReused(
									laInvData.getQtyReused()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							// PCR 34
							if (laInvSumTypeRptData.getReprntQty() > 0)
							{
								laInvData.setQtyReprnted(
									laInvData.getQtyReprnted()
										+ laInvSumTypeRptData
											.getReprntQty());
							}
							// End PCR 34
						}
					}
					else if (
						(aaFundsData.getFundsReportData().getEntity()
							== FundsConstant.EMPLOYEE
							&& aaFundsData
								.getFundsReportData()
								.getPrimarySplit()
								== FundsConstant.CASH_DRAWER)
							|| (aaFundsData
								.getFundsReportData()
								.getEntity()
								== FundsConstant.CASH_DRAWER
								&& aaFundsData
									.getFundsReportData()
									.getPrimarySplit()
									== FundsConstant.EMPLOYEE))
					{
						if (laInvSumTypeRptData
							.getTransEmpId()
							.equals(laInvData.getTransEmpId())
							&& laInvSumTypeRptData.getCashWsId()
								== laInvData.getCashWsId()
							&& laInvSumTypeRptData.getItmCdDesc().equals(
								laInvData.getItmCdDesc())
							&& laInvSumTypeRptData.getInvItmYr()
								== laInvData.getInvItmYr()
							&& !lbMatch)
						{
							lbMatch = true;
							if (laInvSumTypeRptData
								.getInvStatus()
								.equals("SOLD"))
							{
								laInvData.setQtySold(
									laInvData.getQtySold()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							else if (
								laInvSumTypeRptData
									.getInvStatus()
									.equals(
									"VOIDED"))
							{
								laInvData.setQtyVoided(
									laInvData.getQtyVoided()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							else if (
								laInvSumTypeRptData
									.getInvStatus()
									.equals(
									"REUSED"))
							{
								laInvData.setQtyReused(
									laInvData.getQtyReused()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							// PCR 34
							if (laInvSumTypeRptData.getReprntQty() > 0)
							{
								laInvData.setQtyReprnted(
									laInvData.getQtyReprnted()
										+ laInvSumTypeRptData
											.getReprntQty());
							}
							// END PCR 34
						}
					}
					else if (
						aaFundsData.getFundsReportData().getEntity()
							== FundsConstant.EMPLOYEE)
					{
						if (laInvSumTypeRptData
							.getTransEmpId()
							.equals(laInvData.getTransEmpId())
							&& laInvSumTypeRptData.getItmCdDesc().equals(
								laInvData.getItmCdDesc())
							&& laInvSumTypeRptData.getInvItmYr()
								== laInvData.getInvItmYr()
							&& !lbMatch)
						{
							lbMatch = true;
							if (laInvSumTypeRptData
								.getInvStatus()
								.equals("SOLD"))
							{
								laInvData.setQtySold(
									laInvData.getQtySold()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							else if (
								laInvSumTypeRptData
									.getInvStatus()
									.equals(
									"VOIDED"))
							{
								laInvData.setQtyVoided(
									laInvData.getQtyVoided()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							else if (
								laInvSumTypeRptData
									.getInvStatus()
									.equals(
									"REUSED"))
							{
								laInvData.setQtyReused(
									laInvData.getQtyReused()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
						}
					}
					else if (
						aaFundsData.getFundsReportData().getEntity()
							== FundsConstant.NONE)
					{
						if (laInvSumTypeRptData
							.getItmCdDesc()
							.equals(laInvData.getItmCdDesc())
							&& laInvSumTypeRptData.getInvItmYr()
								== laInvData.getInvItmYr()
							&& !lbMatch)
						{
							lbMatch = true;
							if (laInvSumTypeRptData
								.getInvStatus()
								.equals("SOLD"))
							{
								laInvData.setQtySold(
									laInvData.getQtySold()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							else if (
								laInvSumTypeRptData
									.getInvStatus()
									.equals(
									"VOIDED"))
							{
								laInvData.setQtyVoided(
									laInvData.getQtyVoided()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
							else if (
								laInvSumTypeRptData
									.getInvStatus()
									.equals(
									"REUSED"))
							{
								laInvData.setQtyReused(
									laInvData.getQtyReused()
										+ Integer.parseInt(
											laInvSumTypeRptData
												.getInvQty()));
							}
						}
					}
				}
				j--;
			}
			while (j >= 0 && !lbMatch);
			if (!lbMatch)
			{
				InventoryData laInvData = new InventoryData();
				InventorySummaryTypeReportData laInvSumTypeRptData =
					(InventorySummaryTypeReportData) (lvInvData.get(i));
				laInvData.setCashWsId(
					laInvSumTypeRptData.getCashWsId());
				laInvData.setInvItmYr(
					laInvSumTypeRptData.getInvItmYr());
				laInvData.setItmCdDesc(
					laInvSumTypeRptData.getItmCdDesc());
				laInvData.setTransEmpId(
					laInvSumTypeRptData.getTransEmpId());
				if (laInvSumTypeRptData.getInvStatus().equals("SOLD"))
				{
					laInvData.setQtySold(
						Integer.parseInt(
							laInvSumTypeRptData.getInvQty()));
				}
				else if (
					laInvSumTypeRptData.getInvStatus().equals(
						"VOIDED"))
				{
					laInvData.setQtyVoided(
						Integer.parseInt(
							laInvSumTypeRptData.getInvQty()));
				}
				else if (
					laInvSumTypeRptData.getInvStatus().equals(
						"REUSED"))
				{
					laInvData.setQtyReused(
						Integer.parseInt(
							laInvSumTypeRptData.getInvQty()));
				}
				// PCR 34
				if (laInvSumTypeRptData.getReprntQty() > 0)
				{
					laInvData.setQtyReprnted(
						laInvSumTypeRptData.getReprntQty());
				}
				// PCR 34
				liCount++;
				lvInventory.add(laInvData);
			}
		}
		return lvInventory;
	}
	/**
	* Method updates db to set Current Status Timestamp for Current
	* Status event
	*
	* @param  aaData Object
	* @param  aaDBAccess DatabaseAccess
	* @throws RTSException 
	*/
	public void updateCurrentStatus(
		Object aaData,
		DatabaseAccess aaDBAccess)
		throws RTSException
	{
		FundsData laFundsData = (FundsData) aaData;
		Vector lvDrawers = laFundsData.getSelectedCashDrawers();
		// defect 7077
		// Reorganize for maximum DB concurrency
		CashWorkstationCloseOut laCashWksCloseOut =
			new CashWorkstationCloseOut(aaDBAccess);
		// defect 8158
		// Create new CashWorkstationCloseoutData for update request
		// Note: If Cancel off the Screen for Current Statistics Reports
		//       when on DB Server, this shows an updated CloseOut and
		//       a Current Status which is unchanged. 

		CashWorkstationCloseOutData laCashWksCloseOutData =
			new CashWorkstationCloseOutData();
		try
		{
			// UOW #1 BEGIN
			aaDBAccess.beginTransaction();
			// For each cash workstation, assign fields from vector
			for (int i = 0; i < lvDrawers.size(); i++)
			{
				CashWorkstationCloseOutData laCashWksCloseOutData1 =
					(CashWorkstationCloseOutData) lvDrawers.get(i);
				laCashWksCloseOutData.setOfcIssuanceNo(
					laCashWksCloseOutData1.getOfcIssuanceNo());
				laCashWksCloseOutData.setSubstaId(
					laCashWksCloseOutData1.getSubstaId());
				laCashWksCloseOutData.setCashWsId(
					laCashWksCloseOutData1.getCashWsId());
				laCashWksCloseOutData.setCurrStatTimestmp(
					laCashWksCloseOutData1.getCloseOutEndTstmp());
				laCashWksCloseOut.updCashWorkstationIds(
					laCashWksCloseOutData);
			}
			aaDBAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END
			// end defect 8158
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
	}
}
