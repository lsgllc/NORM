package com.txdot.isd.rts.services.reports.funds;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.FundsReportData;
import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.SubstationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;
import com.txdot.isd.rts.server.db.AdministrationLog;
import com.txdot.isd.rts.server.db.OfficeIds;
import com.txdot.isd.rts.server.db.Substation;

/*
 *
 * GenCountyWideReports.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	12/21/2001	New Class.  Restart with new approach.
 * Ray Rowehl	02/22/2002	Create the Exception Report
 *							Create the report date and pass it.
 *							defect 1964						
 * Ray Rowehl	03/07/2002	Prevent generation of report sections when
 *							there is not a summary record for that
 *							substation.
 *							defect 1964						
 * Ray Rowehl	03/29/2002	Pass Summary Effective Date to isSummarized.
 *							defect 1964						
 * Ray Rowehl	04/09/2002	JavaDoc work.						
 * K Harrell	03/21/2004	5.2.0 Merge.  JavaDoc Standards work.
 * 							Ver 5.2.0
 * K Harrell	10/27/2004 	Modify to use FundsData.getWorkstationId()
 *							for report header
 *							modify formatReport()
 *							defect 7681 Ver 5.2.2 
 * K Harrell	03/21/2005	Modifications for smaller units of work.
 *							Used 5.2.3 Code for modification
 *							reversed caRpt... to cRpt
 *							variable from public to private
 *							modify isSummarized(),formatReport()
 *							defect 7077 Ver 5.2.2 Fix 3
 * K Harrell	03/21/2005	Restored from Ver 5.2.2 Fix 3 
 * 							reversed cRpt to caRpt, etc. 
 * 							Ver 5.2.3
 * K Harrell	04/29/2005	Funds/SQL class review
 * 							defect 8163 Ver 5.2.3  
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3  
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify 
 *							defect 7896 Ver 5.2.3
 * K Harrell	10/24/2005	Constant renaming 
 * 							defect 8379 Ver 5.2.3     
 * K Harrell	08/17/2009	modify createExceptionReport()
 * 							defect 8628 Ver Defect_POS_F  
 * K Harrell	03/01/2010	Insert AdminLogData entry for Rerun 
 * 							 Countywide.
 * 							 modify formatReport()
 * 							 defect 10168 Ver POS_640                 
 * ---------------------------------------------------------------------
 */
/**
 * This class creates the CountyWide Report.  There are four major 
 * sections to this report.  The sections are:
 *	<ol>
 *	<li>Payment
 * 	<li>Fees
 *	<li>Inventory
 * 	<li>Exceptions
 *	</ol>
 * 	<p>
 * In Payment, Fees, and Inventory, each section has a substation
 * summary for each substation that has summarized.  That is followed by
 * a countywide summary.  The Exceptions section shows substations that 
 * did not summarize. Use GenSubStationSummary since we can share most
 * of the same methods.
 * <p>
 * Extend GenSubStationSummary so we do not have to duplicate all the 
 * formatting.
 *  
 * @version	POS_640 		03/01/2010
 * @author	Ray Rowehl
 * <br>Creation Date:		12/21/2001
 */
public class GenCountyWideReports extends GenSubStationSummaryReports
{
	// Constant

	private static final int COL1_START_PT = 1;
	private static final int COL2_START_PT = 40;
	private static final int COLUMN_LENGTH = 35;

	private static final String SUBSTA_NAME_STRING = "SUBSTATION NAME";
	private static final String STATUS_MSG_STRING = "STATUS MESSAGES";
	private static final String NO_DATA_STRING =
		"NO SUMMARY DATA FOUND.";
	private static final String SUMMARY_STRING = "SUMMARY";
	
	/**
	 * GenCountyWideReport constructor
	 * 
	 */
	public GenCountyWideReports()
	{
		super();
	}
	/**
	 * GenCountyWideReport constructor
	 * Pulls in report name and properties to create report.
	 *
	 * @param asRptName	String 
	 * @param aaRptProps ReportProperties 
	 */
	public GenCountyWideReports(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}
	/**
	 * Create the Exception Report.  This section lists all the
	 * substations that have not been summarized.
	 * Substations on this list should not have a Substation Summary
	 * generated.
	 * 
	 * @param avExceptionList Vector
	 * @param aaGenSearchData GeneralSearchData
	 */
	public void createExceptionReport(
		Vector avExceptionList,
		GeneralSearchData aaGenSearchData)
	{
		// Generate the Exception Report
		// set up the Report Id for Payment
		this.caRptProps.setUniqueName(
			ReportConstant.COUNTYWIDE_EXCEPTION_REPORT_ID);
		this.caRpt.csName =
			ReportConstant.COUNTYWIDE_EXCEPTION_REPORT_TITLE;
		// Set up the Date Header part of the report
		Vector lvHeader = new Vector();

		// this adds the batch run date to the header	
		lvHeader.addElement(REPORT_DATE_STRING);
		lvHeader.addElement(
			(new RTSDate(RTSDate.AMDATE, aaGenSearchData.getIntKey3()))
				.toString());

		// Set up the report column headers
		Vector lvTable = new Vector();

		// This vector contains the first of a single row of column headings
		Vector lvRow1 = new Vector();
		ColumnHeader laColumnA1 =
			new ColumnHeader(
				SUBSTA_NAME_STRING,
				COL1_START_PT,
				COLUMN_LENGTH);
		ColumnHeader laColumnA2 =
			new ColumnHeader(
				STATUS_MSG_STRING,
				COL2_START_PT,
				COLUMN_LENGTH);
		lvRow1.addElement(laColumnA1);
		lvRow1.addElement(laColumnA2);
		lvTable.addElement(lvRow1); //Adding ColumnHeader Information

		// create the header portion of the report
		generateHeader(lvHeader, lvTable);
		String lsSubstaName = "";

		// print the detail lines generateHeader(lvHeader, lvTable);
		for (int i = 0; i < avExceptionList.size(); i++)
		{
			lsSubstaName = (String) avExceptionList.elementAt(i);
			this.caRpt.print(
				lsSubstaName,
				COL1_START_PT,
				COLUMN_LENGTH);
			this.caRpt.print(
				NO_DATA_STRING,
				COL1_START_PT,
				COLUMN_LENGTH);
			this.caRpt.blankLines(2);
		}
		// defect 8628 
		generateFooter(true);
		// end defect 8628 
	}
	
	/**
	 * This method drives the creation of the CountyWide Report.
	 * CountyWide is just an extension of Substation Summary.
	 * Each formatting method is called several times.
	 * Once for each substation and then once for the county summary.
	 * A one substation county only gets a countywide summary however.
	 * There is no need for a substation summary in this situation.
	 * 
	 * @param aaFundsData FundsData - Funds Data selected.
	 */
	public void formatReport(FundsData aaFundsData)
	{
		// defect 7077
		// Reorganize for maximum DB concurrency	
		DatabaseAccess laDatabaseAccess = new DatabaseAccess();

		// Instantiating GeneralSearchData  
		GeneralSearchData laGenSearchData = new GeneralSearchData();
		laGenSearchData.setIntKey1(aaFundsData.getOfficeIssuanceNo());
		laGenSearchData.setIntKey2(aaFundsData.getSubStationId());
		laGenSearchData.setIntKey3(
			aaFundsData.getSummaryEffDate().getAMDate());

		// Instantiate the Exception List.
		Vector lvExceptionList = new Vector();

		try
		{
			// defect 10168 
			// Insert Admin Log for Rerun County Wide  
			if (aaFundsData.getAdminLogData() != null)
			{
				// UOW #0 BEGIN
				laDatabaseAccess.beginTransaction();
				AdministrationLog laAdminLog =
					new AdministrationLog(laDatabaseAccess);
				laAdminLog.insAdministrationLog(
					aaFundsData.getAdminLogData());
				laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
				// UOW #0 END 
			}
		
			OfficeIds laOfficeName = new OfficeIds(laDatabaseAccess);
			CountyWideSQL laCWSQL = new CountyWideSQL(laDatabaseAccess);
			Substation laSubstation = new Substation(laDatabaseAccess);

			// UOW #1 BEGIN
			// Get OfcName
			laDatabaseAccess.beginTransaction();
			String lsOfficeName =
				laOfficeName.qryOfficeId(
					aaFundsData.getOfficeIssuanceNo());

			// Get list of Substations 	
			SubstationData laSubstationData = new SubstationData();
			laSubstationData.setOfcIssuanceNo(
				aaFundsData.getOfficeIssuanceNo());
			Vector lvSubstationList =
				laSubstation.qrySubstation(laSubstationData);
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			int liSubstaId = 0;
			this.caRptProps.setOfficeIssuanceName(lsOfficeName);
			this.caRptProps.setRequestedBy(aaFundsData.getEmployeeId());

			this.caRptProps.setWorkstationId(
				aaFundsData.getWorkstationId());
			this.caRptProps.setOfficeIssuanceId(
				aaFundsData.getOfficeIssuanceNo());

			//----------------------------------------------------------
			// This section generates the Payment portion of the report
			//
			// set up the Report Id for Payment
			this.caRptProps.setUniqueName(
				ReportConstant.COUNTYWIDE_PYMNT_REPORT_ID);
			this.caRpt.csName =
				ReportConstant.COUNTYWIDE_PYMNT_REPORT_TITLE;

			// If more than one Substation, loop through list
			if (lvSubstationList.size() > 1)
			{
				for (int i = 0; i < lvSubstationList.size(); i++)
				{
					// find the SubstaId
					laSubstationData =
						(SubstationData) lvSubstationList.elementAt(i);
					liSubstaId = laSubstationData.getSubstaId();
					// Check to see if Substation Summary has been run
					// for this Substation
					// If so, create this section of the report.
					if (isSummarized(laDatabaseAccess,
						aaFundsData.getOfficeIssuanceNo(),
						liSubstaId,
						laGenSearchData.getIntKey3()))
					{
						// Set up the Substa Name
						this.caRptProps.setSubstationName(
							laSubstationData.getSubstaName());

						this.caRptProps.setSubstationId(liSubstaId);

						// setup the General Search Key2 from the SubstaId
						laGenSearchData.setIntKey2(liSubstaId);

						// UOW #2 BEGIN 
						laDatabaseAccess.beginTransaction();
						Vector lvQueryResultsCash =
							laCWSQL.qryPaymentSubstationSummary(
								laGenSearchData);
						laDatabaseAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// UOW #2 END

						// UOW #3 BEGIN 
						laDatabaseAccess.beginTransaction();
						Vector lvQueryResultsNonCash =
							laCWSQL.qryNonCashDrawerSubstationSummary(
								laGenSearchData);
						laDatabaseAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// UOW #3 END 

						// create vector to pass in objects
						Vector lvDataOutPayment = new Vector();
						lvDataOutPayment.addElement(lvQueryResultsCash);
						lvDataOutPayment.addElement(
							lvQueryResultsNonCash);
						lvDataOutPayment.addElement(aaFundsData);

						// Format Payment Section 
						genPaymentSection(lvDataOutPayment);
					} // end if on summarized
					else
					{
						// add this substation name to the exception
						// list to  go to the exception report
						//lvExceptionList.addElement(
						//	laSubstation.qrySubstationId(
						//		aaFundsData.getOfficeIssuanceNo(),
						//		liSubstaId));
						lvExceptionList.addElement(
							laSubstationData.getSubstaName());
					} // end else on summarized
				} // end for loop
			} // end if stmt

			//----------------------------------------------------------
			// Generate Summary portion of the Payment report
			//
			// Set SummaryEffDate in GeneralSearchData

			laGenSearchData.setIntKey2(laGenSearchData.getIntKey3());

			// UOW #4 BEGIN 
			laDatabaseAccess.beginTransaction();
			Vector lvQueryResultsCash =
				laCWSQL.qryPaymentCountyWide(laGenSearchData);
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #4 END

			// UOW #5 BEGIN 
			laDatabaseAccess.beginTransaction();
			Vector lvQueryResultsNonCash =
				laCWSQL.qryNonCashDrawerCountyWide(laGenSearchData);
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #5 END

			// create vector to pass in objects
			Vector lvDataOutPayment = new Vector();
			lvDataOutPayment.addElement(lvQueryResultsCash);
			lvDataOutPayment.addElement(lvQueryResultsNonCash);
			lvDataOutPayment.addElement(aaFundsData);

			// Set SubstaName for the Summary Page 
			this.caRptProps.setSubstationName(SUMMARY_STRING);

			// Format the report
			genPaymentSection(lvDataOutPayment);
			//----------------------------------------------------------
			// This section generates the Fees portion of the report
			//
			// set up the Report Id for Fees
			this.caRptProps.setUniqueName(
				ReportConstant.COUNTYWIDE_FEES_REPORT_ID);
			this.caRpt.csName =
				ReportConstant.COUNTYWIDE_FEES_REPORT_TITLE;

			// If more than one Substation, loop through list
			if (lvSubstationList.size() > 1)
			{
				for (int i = 0; i < lvSubstationList.size(); i++)
				{
					// find the SubstaId
					laSubstationData =
						(SubstationData) lvSubstationList.elementAt(i);
					liSubstaId = laSubstationData.getSubstaId();
					// Check to see if Substation Summary has been run for substation
					// If so, create this section of the report.
					if (isSummarized(laDatabaseAccess,
						aaFundsData.getOfficeIssuanceNo(),
						liSubstaId,
						laGenSearchData.getIntKey3()))
					{
						// Set up the Substation Information 
						this.caRptProps.setSubstationId(liSubstaId);
						this.caRptProps.setSubstationName(
							laSubstationData.getSubstaName());

						laGenSearchData.setIntKey2(liSubstaId);
						// UOW #6 BEGIN
						laDatabaseAccess.beginTransaction();
						Vector lvQueryResultsFees =
							laCWSQL.qryFeeSummarySubstationSummary(
								laGenSearchData);
						laDatabaseAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// UOW #6 END

						// create vector to pass in objects
						Vector lvDataOutFees = new Vector();
						lvDataOutFees.addElement(lvQueryResultsFees);
						lvDataOutFees.addElement(aaFundsData);
						// format the report
						genFeesSection(lvDataOutFees);
					} // end if summarized
				} // end for loop
			} // end if

			//----------------------------------------------------------
			// Generate Summary portion of the Fees report
			//
			// Set SummaryEffDate in GeneralSearchData

			laGenSearchData.setIntKey2(laGenSearchData.getIntKey3());

			// UOW #6 BEGIN
			laDatabaseAccess.beginTransaction();
			Vector lvQueryResultsFees =
				laCWSQL.qryFeeSummaryCountyWide(laGenSearchData);
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #6 END

			// create vector to pass in objects
			Vector lvDataOutFees = new Vector();
			lvDataOutFees.addElement(lvQueryResultsFees);
			lvDataOutFees.addElement(aaFundsData);

			// Set SubstaName for the Summary Page
			this.caRptProps.setSubstationName(SUMMARY_STRING);
			// format the report
			genFeesSection(lvDataOutFees);

			//----------------------------------------------------------
			// This section generates the Inventory Summary portion of
			// the report
			this.caRptProps.setUniqueName(
				ReportConstant.COUNTYWIDE_INV_REPORT_ID);
			this.caRpt.csName =
				ReportConstant.COUNTYWIDE_INV_REPORT_TITLE;

			// If more than one Substation, loop through list
			if (lvSubstationList.size() > 1)
			{
				for (int i = 0; i < lvSubstationList.size(); i++)
				{
					// find the SubstaId
					laSubstationData =
						(SubstationData) lvSubstationList.elementAt(i);
					liSubstaId = laSubstationData.getSubstaId();
					// Check to see if Substation Summary has been run
					// for this Substation
					// If so, create this section of the report.
					if (isSummarized(laDatabaseAccess,
						aaFundsData.getOfficeIssuanceNo(),
						liSubstaId,
						laGenSearchData.getIntKey3()))
					{
						// Set up the Substa Name
						this.caRptProps.setSubstationName(
							laSubstationData.getSubstaName());
						// setup the General Search Key2 from the SubstaId
						laGenSearchData.setIntKey2(liSubstaId);
						this.caRptProps.setSubstationId(liSubstaId);

						// UOW #7 BEGIN
						laDatabaseAccess.beginTransaction();
						Vector lvQueryResultsInv =
							laCWSQL
								.qryInventorySummarySubstationSummary(
								laGenSearchData);
						laDatabaseAccess.endTransaction(
							DatabaseAccess.COMMIT);
						// UOW #7 END

						Vector lvDataOutInv = new Vector();
						lvDataOutInv.addElement(lvQueryResultsInv);
						lvDataOutInv.addElement(aaFundsData);
						// format the substation's portion of the Inventory Report
						genInvSection(lvDataOutInv);
					} // end if summarized
				} // end for loop
			} // end if

			//----------------------------------------------------------
			// Generate Summary portion of the Inventory report
			//
			// Set SummaryEffDate in GeneralSearchData
			laGenSearchData.setIntKey2(laGenSearchData.getIntKey3());

			// UOW #8 BEGIN
			laDatabaseAccess.beginTransaction();
			Vector lvQueryResultsInv =
				laCWSQL.qryInventorySummaryCountyWide(laGenSearchData);
			laDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #8 END

			Vector lvDataOutInv = new Vector();
			lvDataOutInv.addElement(lvQueryResultsInv);
			lvDataOutInv.addElement(aaFundsData);

			// Set SubstaName for the Summary Page
			this.caRptProps.setSubstationName(SUMMARY_STRING);
			// format the countywide's portion of the Inventory Report
			genInvSection(lvDataOutInv);

			//----------------------------------------------------------
			// print the exception report if it is needed
			laGenSearchData.setIntKey3(
				aaFundsData.getSummaryEffDate().getAMDate());
			if (lvExceptionList.size() > 0)
			{
				// generate the Exception Report.  There are exceptions.
				createExceptionReport(lvExceptionList, laGenSearchData);
			}
			//----------------------------------------------------------
		}
		catch (RTSException aeRTSEx)
		{
			try
			{
				laDatabaseAccess.endTransaction(
					DatabaseAccess.ROLLBACK);
				aeRTSEx.printStackTrace();
			}
			catch (RTSException aeRTSEx1)
			{
				aeRTSEx1.printStackTrace();
			}
		}
		finally
		{
			laDatabaseAccess = null;
		}
		// end defect 7077 
	}
	/**
	 * Check to see if the selected Substation is summarized.
	 * 
	 * @param aiOfficeId int - Office Id
	 * @param aiSubstaId int - Substation Id
	 * @param aiSummaryEffDate int - Summary Effective Date
	 * @param aaDatabaseAccess DatabaseAccess
	 * @return boolean - false if not summarized, true if it is
	 * @throws RTSException 
	 */
	public boolean isSummarized(
		DatabaseAccess aaDatabaseAccess,
		int aiOfficeId,
		int aiSubstaId,
		int aiSummaryEffDate)
		throws RTSException
	{
		boolean lbSummarized = false;
		GeneralSearchData laGenSearchData = new GeneralSearchData();
		try
		{
			CountyWideSQL laCWSQL = new CountyWideSQL(aaDatabaseAccess);
			laGenSearchData.setIntKey1(aiOfficeId);
			laGenSearchData.setIntKey2(aiSubstaId);
			laGenSearchData.setIntKey3(aiSummaryEffDate);
			Vector lvSummaryResults = new Vector();

			// UOW #1 BEGIN 
			aaDatabaseAccess.beginTransaction();
			lvSummaryResults =
				laCWSQL.qrySubstaSummaryCountyWide(laGenSearchData);
			aaDatabaseAccess.endTransaction(DatabaseAccess.COMMIT);
			// UOW #1 END

			if (lvSummaryResults != null
				&& lvSummaryResults.size() > 0)
			{
				lbSummarized = true;
			}
		}
		catch (RTSException aeRTSEx)
		{
			throw aeRTSEx;
		}
		return lbSummarized;
	}
	/**
	 * Starts the application in stand alone / test mode.
	 * This method needs to be updated however.
	 *
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// instantiate the FundsReportData object and set the values
		FundsReportData laFundsReportData = new FundsReportData();
		laFundsReportData.setEntity(FundsConstant.SUBSTATION);
		laFundsReportData.setPrimarySplit(FundsConstant.NONE);
		laFundsReportData.setShowSourceMoney(true);

		// note that the AMDate needs to be updated to match current
		// test data.
		laFundsReportData.setFromRange(new RTSDate());
		// instantiate the FundsData object and set values
		FundsData laFundsData = new FundsData();
		laFundsData.setOfficeIssuanceNo(170);
		laFundsData.setSubStationId(0);
		laFundsData.setCashWsId(115);

		// add FundsReportData to FundsData
		laFundsData.setFundsReportData(laFundsReportData);

		// Instantiating a new Report Class 
		GenSubStationSummaryReports laGSSR =
			new GenSubStationSummaryReports();
		GeneralSearchData laGenSearchData = new GeneralSearchData();
		// formatReport in the Template does not support throwing
		// RTSException
		//try
		//{
		// Query will be done in formatReport
		laGSSR.formatReport(laFundsData);
		//}
		//catch (RTSException rtse)
		//{
		//	rtse.printStackTrace();
		//	System.exit(0);
		//}
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File(laGenSearchData.getKey2());
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			aeIOEx.printStackTrace();
		}
		laPout.print(laGSSR.caRpt.getReport().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport(laGenSearchData.getKey2());
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			laFrmPreviewReport.setVisibleRTS(true);
			// One way to Print Report.
			//Process p = Runtime.getRuntime().exec("cmd.exe /c copy
			// c:\\TitlePkgRpt.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.services.reports.reports."
					+ "GenCountyWideReports");
			aeEx.printStackTrace(System.out);
		}
	}
}
