package com.txdot.isd.rts.services.reports.reports;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.data.TitlePackageReportData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Print;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;

/*
 * GenTitlePackageReport.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/19/2001	new class
 * J Rue		03/15/2002	Add page break for first/last WsId
 *    						no found.
 * J Rue		03/22/2002	Refer to remarks, method noRecordsFoundMsg()
 * S Govindappa 04/25/2002	Fix CQU100003641, determineNoRecFoundPrnt
 * J Rue 	    05/08/2002	Fixed CQU100003778 by making changes to
 *							determineNoRecFoundPrnt
 * S Govindappa
 * J Kwik 	    05/11/2002	Fixed CQU100003886 - in printLastRecord(),
 *							do not call determineNoRecFoundPrnt().
 * S Govindappa 05/20/2002  Fixed CQU100004012 by making changes
 *							to determineNoRecFoundPrnt to check for
 *							size of cvSelectedWsId
 * Jeff                     before updating the index.
 * Jeff 		05/21/2002  Fixed CQU100004014, Changed printLastRecord()
 *							to comment out printVoidMessage
 * S Govindappa 06/27/2002  Fixed CQU100004377, Changed formatReport()
 *							to remove wrong sorting of selected WsID.
 *							It already comes sorted.
 * Jeff                     This prevents Title package report being
 *							printed twice for a workstation.
 * S Govindappa 06/27/2002  Fixed CQU100004377, Added correct sorting
 *							algorithm back to formatReport()
 * S. Haskett	12/6/2002	Rewrote report to simplify maintenance.
 *                          defect 5138
 * Jeff S.		07/18/2003	Added barcode to report like in RTSI
 *							generateHeader() - Defect# 3994
 * Jeff S.		08/07/2003	3994 failed UAT because the contents
 *							of the barcode was incorrect.  Added county
 *							number to barcode and padded it to make sure
 *							leading zeros made it onto the barcode.
 *							generateHeader() - Defect# 3994
 * Ray Rowehl	09/03/2004	Improve exception catch in formatReport to
 *							log error to app log.  note that we can not
 *							throw the exception because we are not
 *							returning anything.
 *							Also cleaned up some formatting.
 *							Formatted the file and class headers.
 *							deprecate generatePageBreak()
 *							modify formatReport()
 *							defect 7530 Ver 5.2.1
 * Ray Rowehl	09/17/2004	Add defect markers!
 *							modify formatReport()
 *							defect 7530 Ver 5.2.1
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3
 * K Harrell	10/17/2005	TitlePackageReportData moved to services.data
 * 							defect 7896 Ver 5.2.3
 * K Harrell	02/09/2006	"Workstation" heading adjustment
 * 							modify WORKSTATION
 * 							defect 7896 Ver 5.2.3   
 * K Harrell	01/05/2009	Initialize ciPrevWsId to 1st selected WsId 
 * 							to correct reporting when 1st WsId has >1
 * 							batch.
 * 							modify sortSelectedWsIdVector()
 * 							defect 7002 Ver Defect_POS_D 
 * T Pederson	03/18/2009	Added ETitle Column and totals to report.
 * 							modify generateBatchTotal(), 
 * 							generateBatchWsTotal(), generateDataLine(),
 * 							generateHeader() and generateReportTotal()
 * 							defect 9976 Ver Defect_POS_E 
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F  
 * Min Wang		12/17/2009	Line up between batch totals, 
 * 							workstation totals, report totals. 
 * 							modify generateBatchWsTotal(),
 * 						 	generateReportTotal(),generateBatchWsTotal()
 * 							defect 8568 Ver Defect_POS_H 
 * Min Wang		12/28/2009  Create constants for printing report totals.
 * 							add TOTAL_START_PT_90, TOTAL_LENTH_4
 * 							defect 8568 Ver Defect_POS_H
 * K Harrell	10/03/2010	New Title Package Format:  New Column for 
 * 							Special Exam, New Section/Totals for Void
 * 							add cbPrintedHeader, ciRptVoidCount,
 * 							  cvVoidedTrans, cbContd, ciIndex 
 * 							add QTY_DESC_START, QTY_DESC_LENGTH, 
 * 							 QTY_START, QTY_LENGTH, ETITLE_START, 
 * 							 ETTL_COL_HDR, ETTL_COL_START, TRANSID_COL_HDR,
 * 							 TRANSID_COL_START, FEE_COL_HDR, 
 * 							 FEE_COL_START, SPCL_EXAM_COL_HDR, 
 * 							 SPCL_EXAM_COL_START, VOID_TOTAL,
 * 							 NEXT_DAY_VOID_TOTAL, EXCEPTION,
 * 							 NO_RECS_FOR_WSIDS,NO_RECS_FOR_WSID 
 * 							add printVoidedTrans(), generateVoidDataLine(),
 * 							 generateExceptionHeader(), 
 * 							 printNextDayVoidedTrans(), 
 * 							 generateNoRecordFoundMsg() 
 * 							delete TOTAL_START_PT_90, TOTAL_LENTH_4, 
 * 								TRANSACTION_ID_FEE, ETITLE_TOTAL_START_POINT
 * 							delete generateNoBatchTitles(), 
 * 							 sortSelectedWsIdVector() 
 * 							modify BATCH_TOTAL, REPORT_TOTAL_PACKAGES, 
 * 							    WORKSTATION 
 * 							modify formatReport(), generateDataLine(), 
 * 							 generateReportTotal(), generateBatchTotal(), 
 * 							 generateBatchWsTotal(), breakOnBatchNo(),
 * 							 generateHeader(), generatePageBreak() 
 * 							defect 10013 Ver 6.6.0 
 * K Harrell	10/24/2010	Increase required lines for ReportTotal 
 * 							modify generateReportTotal()
 * 							defect 10013 Ver 6.6.0 
 * K Harrell	12/16/2010  Void transactions printing itmprice of 
 * 							last unvoided transaction.	
 * 							modify generateVoidedTrans()
 * 							defect 10699 Ver 6.6.0  
 * ---------------------------------------------------------------------
 */
/**
 * The formatReport() generates the Title Package Report. 
 * 
 * This includes Headers/Columns, page breaks, totaling Batch and
 * Workstation iterations, printing reports totals and end of report
 * messages.
 *
 * @version	6.6.0  			12/16/2010
 * @author  Kathy Harrell 
 * @author	Jeff Rue
 * <br>Creation Date:		09/19/2001  
 */
public class GenTitlePackageReport extends ReportTemplate
{
	// This is where we put the incoming TtlPkgData
	private Vector cvSelectedWsId = new Vector();
	private Vector cvTtlPkgData = new Vector();

	// defect 10013
	private boolean cbPrintedHeader = false;
	private boolean cbContd = false;
	private int ciIndex = 0;
	private int ciRptVoidCount = 0;
	private int ciRptNextDayVoidCount = 0;
	private Vector cvVoidedTrans = new Vector();
	private Vector cvNextDayVoidedTrans = new Vector();
	private Vector cvWsIdWithData = new Vector();
	private Vector cvWsIdWithNoData = new Vector();
	// end defect 10013  

	private String csAMReportDt = "";

	//	Holds one element from cvTtlPkgData
	// variables used to 'hold on' to data for iterative comparisons	
	// initialized with values guaranteed to create a break on first row
	private TitlePackageReportData caTtlPkgRptData =
		new TitlePackageReportData();

	// Previous Workstation Id used for 'break' logic
	private int ciPrevWsId = -1;

	// Previous Batch Number used for 'break' logic,
	private String csPrevBatchNumber = "***";

	// Used to save wsid from page header to where it is echoed
	// in the WS total line
	private int ciWsId;

	// Used to save batchno from page header
	private String csBatchNo;

	// Variables used to accumulate totals
	// Batch iteration
	private int ciBatchQty = 0;

	// Total Workstation Batch iterations
	private int ciBatchQtyWsTotal = 0;

	// Total Report Batch iterations
	private int ciBatchQtyRptTotal = 0;

	// defect 9976
	// Batch ETitle iteration
	private int ciBatchETtlQty = 0;

	// Total Workstation Batch iterations
	private int ciBatchETtlQtyWsTotal = 0;

	// Total Report Batch iterations
	private int ciBatchETtlQtyRptTotal = 0;

	private String csEttlTotal;

	// defect 10013
	private final static int QTY_DESC_START = 86;
	private final static int QTY_DESC_LENGTH = 22;
	private final static int QTY_START = 112;
	private final static int QTY_LENGTH = 4;
	private final static int ETITLE_START = 119;
	private final static String ETTL_COL_HDR = "ETITLE";
	private final static int ETTL_COL_START = 23;
	private final static String TRANSID_COL_HDR = "TRANSACTION ID";
	private final static int TRANSID_COL_START = 50;
	private final static String FEE_COL_HDR = "FEE";
	private final static int FEE_COL_START = 75;
	private final static String SPCL_EXAM_COL_HDR = "SPECIAL EXAM";
	private final static int SPCL_EXAM_COL_START = 33;
	private final static String VOID_TOTAL = "VOID TOTAL:";
	private final static String NEXT_DAY_VOID_TOTAL =
		"NEXT DAY VOID TOTAL:";
	private final static String EXCEPTION = "EXCEPTION";
	private final static String NO_RECS_FOR_WSIDS =
		"NO RECORDS FOUND FOR WORKSTATION IDS: ";
	private final static String NO_RECS_FOR_WSID =
		"NO RECORDS FOUND FOR WORKSTATION ID: ";
	// end defect 10013 

	private final static String BATCH_TOTAL = "BATCH TOTAL:";
	private final static String FILE_PATH_LOCATION =
		"C:\\RTS\\RPT\\TTLPONLN.TXT";
	private final static String REPORT_NAME = "TITLE PACKAGE REPORT";
	private final static String REPORT_NUMBER = "RTS.POS.5911";
	private final static String REPORT_TOTAL_PACKAGES =
		"REPORT TOTAL PACKAGES:";

	private final static String TOTAL = " TOTAL:";
	private final static String LEFTPAREN = "(";
	private final static String ETITLE = " ETITLES)";
	private final static String ETITLE_SINGLE = " ETITLE)";
	private final static String WORKSTATION = "WORKSTATION ";

	// Set up a couple of useful integer format
	private DecimalFormat caThreeDigits = new DecimalFormat("000");

	// This variable is used for unit testing: it tracks the order
	// different methods were called when
	// creating a report. Ideally it should be in the superclass...
	private StringBuffer csTestProgramFlow = new StringBuffer();

	/**
	 * GenTitlePackageReport constructor
	 */
	public GenTitlePackageReport()
	{
		super();
	}

	/**
	 * GenTitlePackageReport constructor
	 */
	public GenTitlePackageReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Determines whether BatchNo has changed from previous record
	 * 
	 * @return boolean
	 */
	private boolean breakOnBatchNo()
	{
		String lsBatchNo = caTtlPkgRptData.getBatchNo();
		boolean lbReturn = !(csPrevBatchNumber.equals(lsBatchNo));

		// True if not same as previous
		if (lsBatchNo.equals(""))
		{
			csPrevBatchNumber = "" + caTtlPkgRptData.getTransWsId();
		}
		// Stick wsid into batch no if no batchno in record
		else
		{
			csPrevBatchNumber = lsBatchNo;
		}

		// defect 10013
		// Maintain Vector of WsId w/ Data 
		String lsWsId =
			UtilityMethods.addPadding(
				"" + caTtlPkgRptData.getTransWsId(),
				3,
				"0");

		if (!cvWsIdWithData.contains(lsWsId))
		{
			cvWsIdWithData.addElement(lsWsId);
		}
		// end defect 10013

		return lbReturn;
	}

	/**
	 * Returns true if WsId is not the same as previous record
	 * 
	 * @return boolean
	 */
	private boolean breakOnWsId()
	{
		// Has the WSID changed?
		boolean lbReturn =
			(caTtlPkgRptData.getTransWsId() != ciPrevWsId);

		// Save the current WSID for later comparison
		ciPrevWsId = caTtlPkgRptData.getTransWsId();

		return lbReturn;
	}

	/**
	 * The formatReport() generates the Title Package Report.
	 * This includes Headers/Columns, page breaks, totaling Batch and
	 * Workstation iterations, printing reports totals and end of report
	 * messages.
	 *
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// Load object and vector from input vector
		// Title Package Data 
		cvTtlPkgData = (Vector) avResults.elementAt(0);

		// Selected Ws Ids
		cvSelectedWsId = (Vector) avResults.elementAt(1);

		csAMReportDt = (String) avResults.elementAt(2); // TransAmDate

		//insertHeaderRowsIntoDataVector();

		// Fake an outer join on dataset to simplify report generation
		try
		{
			// Process/Print records - process depends on sorted data!
			for (ciIndex = 0; ciIndex < cvTtlPkgData.size(); ciIndex++)
			{
				getRecord();

				if (breakOnBatchNo())
				{
					// on change of batch no print summary footer and headings 
					if (ciIndex > 0)
					{
						cbPrintedHeader = false;

						// defect 10013 
						generatePageBreak(3, true);
						printVoidedTrans();
						generatePageBreak(3, false);
						printNextDayVoidedTrans();
						// end defect 10013 
						generateBatchTotal();

						// this is where we print the totals, as appropriate
						if (breakOnWsId())
						{
							generateBatchWsTotal();
						}
						generateFooter();
					}

					// defect 10013 
					cbPrintedHeader = false;
					// end defect 10013  

					generateHeader(
						caTtlPkgRptData.getTransWsId(),
						caTtlPkgRptData.getBatchNo());
				}

				generateDataLine();
			}
			// out of data, finish up report

			// defect 10013 
			if (cvTtlPkgData.size() > 0)
			{
				generatePageBreak(3, true);
				printVoidedTrans();
				generatePageBreak(3, false);
				printNextDayVoidedTrans();
				generateBatchTotal();
				generateBatchWsTotal();
				generateReportTotal();
			}
			generateExceptionPage();

			// end defect 10013 

			generateFooter(true);
			// end defect 8628 

		}
		catch (Exception aeEx)
		{
			System.err.println(
				"Exception occurred in formatReport() of "
					+ "com.txdot.isd.rts.services.reports.GenTitlePackageReport");
			aeEx.printStackTrace(System.out);
			RTSException leRTSEx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSEx.writeExceptionToLog();
		}
	}

	/**
	 * Currently not implemented
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Print the batch total for the workstations
	 */
	private void generateBatchTotal()
	{
		// defect 10013  
		//		if (ciBatchQty == 0)
		//		{
		//			generateNoBatchTitles();
		//		}
		// end defect 10013

		generatePageBreak(3, false);
		caRpt.nextLine();

		// defect 9976
		csEttlTotal = CommonConstant.STR_SPACE_EMPTY;
		if (ciBatchQty != 0)
		{
			// defect 10013
			String lsSuffix =
				ciBatchETtlQty == 1 ? ETITLE_SINGLE : ETITLE;
			//csEttlTotal = LEFTPAREN + ciBatchETtlQty + ETITLE;
			csEttlTotal = LEFTPAREN + ciBatchETtlQty + lsSuffix;
		}
		caRpt.print(BATCH_TOTAL, QTY_DESC_START, QTY_DESC_LENGTH);
		caRpt.rightAlign(
			Integer.toString(ciBatchQty),
			QTY_START,
			QTY_LENGTH);
		caRpt.print(csEttlTotal, ETITLE_START, csEttlTotal.length());
		// end defect 10013 

		// end defect 9976
		// cumulate totals
		ciBatchQtyWsTotal = ciBatchQtyWsTotal + ciBatchQty;
		ciBatchQtyRptTotal = ciBatchQtyRptTotal + ciBatchQty;
		ciBatchETtlQtyWsTotal = ciBatchETtlQtyWsTotal + ciBatchETtlQty;
		ciBatchETtlQtyRptTotal =
			ciBatchETtlQtyRptTotal + ciBatchETtlQty;

		// reset Batch Counters
		ciBatchQty = 0;
		ciBatchETtlQty = 0;

		// unit test
		csTestProgramFlow.append("Tb");
	}

	/**
	 * Print the batch total for the workstations
	 * 
	 * <br>defect 7896 - removed parameter that was unused in this method
	 * <br>// param aiTransWsId int 
	 * <br>end defect 7896
	 */
	private void generateBatchWsTotal()
	{
		generatePageBreak(3, false);
		// defect 9976
		caRpt.nextLine();
		csEttlTotal = CommonConstant.STR_SPACE_EMPTY;
		if (ciBatchQtyWsTotal != 0)
		{
			// defect 10013
			String lsSuffix =
				ciBatchETtlQtyWsTotal == 1 ? ETITLE_SINGLE : ETITLE;
			//csEttlTotal = LEFTPAREN + ciBatchETtlQtyWsTotal + ETITLE;
			csEttlTotal = LEFTPAREN + ciBatchETtlQtyWsTotal + lsSuffix;
			// end defect 10013 
		}

		// defect 10013 
		String lsWsInfo =
			WORKSTATION + caThreeDigits.format(ciWsId) + TOTAL;
		caRpt.print(lsWsInfo, QTY_DESC_START, QTY_DESC_LENGTH);

		caRpt.rightAlign(
			Integer.toString(ciBatchQtyWsTotal),
			QTY_START,
			QTY_LENGTH);

		caRpt.print(csEttlTotal, ETITLE_START, csEttlTotal.length());
		// end defect 10013

		// Do not need to cumulate totals since it just needs to be zero

		// sum batch workstation totals
		// set etitle workstation total back to 0
		ciBatchETtlQtyWsTotal = 0;

		// sum batch etitle report totals
		ciBatchETtlQtyRptTotal =
			ciBatchETtlQtyRptTotal + ciBatchETtlQty;
		// end defect 9976

		// set workstation total back to 0
		ciBatchQtyWsTotal = 0;

		// sum batch report totals
		ciBatchQtyRptTotal = ciBatchQtyRptTotal + ciBatchQty;
		csTestProgramFlow.append("Tw"); // unit test
	}

	/**
	 * Print the record.
	 */
	private void generateDataLine()
	{
		generatePageBreak(1, true);

		// Collect Voided Transactions 
		// defect 10013 
		if (caTtlPkgRptData.isVoidedTrans())
		{
			if (caTtlPkgRptData.isNextDayVoid())
			{
				cvNextDayVoidedTrans.add(caTtlPkgRptData);
			}
			else
			{
				cvVoidedTrans.add(caTtlPkgRptData);
			}
		}
		else
		{
			if (!cbPrintedHeader)
			{
				caRpt.blankLines(1);
				String lsHdr = "COMPLETED TITLES:";

				if (!cbContd
					&& ((ciIndex == 0 && cvTtlPkgData.size() == 1)
						|| (ciIndex == cvTtlPkgData.size() - 1)
						|| !caTtlPkgRptData.getBatchNo().equals(
							((TitlePackageReportData) cvTtlPkgData
								.get(ciIndex + 1))
								.getBatchNo())))
				{
					lsHdr = "COMPLETED TITLE:";
				}

				caRpt.println(lsHdr);
				cbPrintedHeader = true;
				if (cbContd)
				{
					caRpt.print("  (CONT'D) ");
					cbContd = false;
				}
			}

			if (caTtlPkgRptData.isETitle())
			{
				caRpt.print("E", ETTL_COL_START + 2, 1);
				ciBatchETtlQty++;

			}

			if (caTtlPkgRptData.isTtlExmn())
			{
				caRpt.print("S", SPCL_EXAM_COL_START + 5, 1);
			}
			// end defect 10013 

			caRpt.print(
				caTtlPkgRptData.getTransId(),
				TRANSID_COL_START,
				20);

			caRpt.print("$", FEE_COL_START, 1);

			caRpt.rightAlign(
				"" + caTtlPkgRptData.getItmPrice(),
				FEE_COL_START + 1,
				6);

			caRpt.blankLines(1);

			// increment batch count
			ciBatchQty++;

			// unit test
			csTestProgramFlow.append("D");
		}
	}

	/** 
	 * Generate Exception Header 
	 * 
	 */
	private void generateExceptionHeader()
	{
		// Report Period
		RTSDate laAmReportDt =
			new RTSDate(RTSDate.AMDATE, Integer.parseInt(csAMReportDt));

		// County Number
		int liOfcissuanceId = caTtlPkgRptData.getOfcIssuanceNo();
		String lsCountyNumber;
		if (liOfcissuanceId < 1)
		{
			lsCountyNumber =
				String.valueOf(caRptProps.getOfficeIssuanceId());
		}
		else
		{
			lsCountyNumber = String.valueOf(liOfcissuanceId);
		}

		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(caRptProps.getOfficeIssuanceName());
		caRpt.center(caRptProps.getSubstationName());
		caRpt.center(EXCEPTION);
		caRpt.blankLines(1);
		caRpt.println("    REPORTED PERIOD      : " + laAmReportDt);
		caRpt.println(
			"    COUNTY NUMBER        : "
				+ UtilityMethods.addPadding(lsCountyNumber, 3, "0"));
		caRpt.println("    TRANS DATE           : " + csAMReportDt);
		//caRpt.blankLines(1);
		String lsWorkstationIds = "WORKSTATION IDS";
		if (cvSelectedWsId.size() == 1)
		{
			lsWorkstationIds = "WORKSTATION ID ";
		}

		caRpt.print("    " + lsWorkstationIds + "      : ");
		String lsWsId = new String();
		boolean lbPrinted = false;
		UtilityMethods.sort(cvSelectedWsId);
		for (int i = 0; i < cvSelectedWsId.size(); i++)
		{
			lsWsId =
				lsWsId + (String) cvSelectedWsId.elementAt(i) + ",";
			if (lsWsId.length() > 86)
			{
				if (i == cvSelectedWsId.size() - 1)
				{
					lsWsId = lsWsId.substring(0, lsWsId.length() - 1);
				}
				caRpt.println(lsWsId);
				lsWsId = new String();
				lbPrinted = true;
			}
		}
		if (lsWsId.length() > 0)
		{
			lsWsId = lsWsId.substring(0, lsWsId.length() - 1);
			if (!lbPrinted)
			{
				caRpt.println(lsWsId);
			}
			else
			{
				caRpt.println("                           " + lsWsId);
			}

		}
		caRpt.drawDashedLine();
		caRpt.blankLines(1);

	}

	/**
	 * Prints the list of Workstations with no data 
	 */
	private void generateExceptionPage()
	{
		for (int i = 0; i < cvSelectedWsId.size(); i++)
		{
			String lsWsId = (String) cvSelectedWsId.elementAt(i);

			if (!cvWsIdWithData.contains(lsWsId))
			{
				cvWsIdWithNoData.add(lsWsId);
			}
		}

		if (cvWsIdWithData.size() != 0 && cvWsIdWithNoData.size() > 0)
		{
			generateFooter(false);
		}

		if (cvWsIdWithNoData.size() > 0)
		{
			generateExceptionHeader();

			if (cvSelectedWsId.size() == cvWsIdWithNoData.size())
			{
				generateNoRecordFoundMsg();
			}
			else
			{
				String lsTitle =
					cvWsIdWithNoData.size() > 1
						? NO_RECS_FOR_WSIDS
						: NO_RECS_FOR_WSID;
				caRpt.print("  " + lsTitle);
				caRpt.blankLines(1);

				UtilityMethods.sort(cvWsIdWithNoData);

				for (int i = 0; i < cvWsIdWithNoData.size(); i++)
				{
					caRpt.print(
						"    "
							+ (String) cvWsIdWithNoData.elementAt(i));
					caRpt.blankLines(1);
				}
			}
		}
	}
	/**
	 * generateNoRecordFoundMsg
	 */
	public void generateNoRecordFoundMsg()
	{
		caRpt.center("**********************************");
		caRpt.nextLine();
		caRpt.center("******   NO RECORDS FOUND   ******");
		caRpt.nextLine();
		caRpt.center("**********************************");
		caRpt.nextLine();
	}

	/**
	 * Print the voided record 
	 */
	private void generateVoidDataLine(TitlePackageReportData aaTtlPkgRptData)
	{
		generatePageBreak(3, false);

		if (!cbPrintedHeader)
		{
			caRpt.blankLines(1);

			String lsHdr = "VOIDED TITLES:";

			Vector lvVoid =
				aaTtlPkgRptData.isNextDayVoid()
					? cvNextDayVoidedTrans
					: cvVoidedTrans;

			if (!cbContd && lvVoid.size() == 1)
			{
				lsHdr = "VOIDED TITLE:";
			}
			if (aaTtlPkgRptData.isNextDayVoid())
			{
				lsHdr = "NEXT DAY " + lsHdr;
			}
			caRpt.println(lsHdr);
			cbPrintedHeader = true;
			if (cbContd)
			{
				caRpt.print("  (CONT'D) ");
				cbContd = false;
			}
		}

		if (aaTtlPkgRptData.isETitle())
		{
			caRpt.print("E", ETTL_COL_START + 2, 1);
		}

		if (aaTtlPkgRptData.isTtlExmn())
		{
			caRpt.print("S", SPCL_EXAM_COL_START + 5, 1);
		}
		caRpt.print(
			aaTtlPkgRptData.getTransId(),
			TRANSID_COL_START,
			20);
		caRpt.print("$", FEE_COL_START, 1);

		// defect 10699 
		// Use correct object for Title Package Data 
		// Passed vs. Class
		//		caRpt.rightAlign(
		//					"" + caTtlPkgRptData.getItmPrice(), 
		caRpt.rightAlign(
			"" + aaTtlPkgRptData.getItmPrice(),
			FEE_COL_START + 1,
			6);
		// end defect 10699 

		caRpt.blankLines(1);

		// unit test
		csTestProgramFlow.append("V");
	}

	/**
	 * The headerColumn method adds text that will be printed when needed
	 */
	private void generateHeader()
	{
		// Report Period
		RTSDate laAmReportDt =
			new RTSDate(RTSDate.AMDATE, Integer.parseInt(csAMReportDt));

		// County Number
		int liOfcissuanceId = caTtlPkgRptData.getOfcIssuanceNo();
		String lsCountyNumber;
		if (liOfcissuanceId < 1)
		{
			lsCountyNumber =
				String.valueOf(caRptProps.getOfficeIssuanceId());
		}
		else
		{
			lsCountyNumber = String.valueOf(liOfcissuanceId);
		}

		// defect 10013 
		// Batch Number
		//		String lsBatchNo = csBatchNo;
		//
		//		if (csBatchNo == null || csBatchNo.equals("0"))
		//		{
		//			lsBatchNo = "";
		//		}
		// end defect 10013 

		// Generate header
		// defect 3994
		// Added barcode to title package report - needed blank line
		caRpt.blankLines(1);

		// end defect 3994
		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(caRptProps.getOfficeIssuanceName());
		caRpt.center(caRptProps.getSubstationName());

		// defect 3994
		// Added barcode to title package report
		// only print if there are records
		if (csBatchNo != "")
		{
			caRpt.print("", 86, 1);
			caRpt.println(
				Print.getBARCODE_TAG(
					UtilityMethods.addPadding(lsCountyNumber, 3, "0")
						+ csBatchNo,
					16.67));
		}
		// Else print a blank line - this was here before defect 3994
		else
		{
			caRpt.blankLines(1);
		}
		// end defect 3994

		caRpt.println("    REPORTED PERIOD      : " + laAmReportDt);

		// defect 10013 
		caRpt.println(
			"    COUNTY NUMBER        : "
				+ UtilityMethods.addPadding(lsCountyNumber, 3, "0"));
		// end defect 10013 

		caRpt.println("    TRANS DATE           : " + csAMReportDt);

		// defect 10013 
		caRpt.println(
			"    WORKSTATION ID       : "
				+ UtilityMethods.addPadding("" + ciWsId, 3, "0"));
		// end defect 10013 

		caRpt.println("    BATCH NUMBER         : " + csBatchNo);

		caRpt.blankLines(1);
		// defect 10013 
		caRpt.center(
			ETTL_COL_HDR,
			ETTL_COL_START,
			ETTL_COL_HDR.length());
		caRpt.center(
			SPCL_EXAM_COL_HDR,
			SPCL_EXAM_COL_START,
			SPCL_EXAM_COL_HDR.length());
		caRpt.center(
			TRANSID_COL_HDR,
			TRANSID_COL_START + 1,
			TRANSID_COL_HDR.length());
		caRpt.center(
			FEE_COL_HDR,
			FEE_COL_START + 2,
			FEE_COL_HDR.length());
		// end defect 10013 
		caRpt.blankLines(1);
		caRpt.drawDashedLine();
		csTestProgramFlow.append("H"); // unit test
	}

	/**
	 * The headerColumn method adds text that will be printed when needed
	 *
	 * @param aiWsId int
	 * @param asBatchNo String
	 */
	private void generateHeader(int aiWsId, String asBatchNo)
	{
		ciWsId = aiWsId;
		csBatchNo = asBatchNo;
		generateHeader();

	}

	// defect 10013 
	//	/**
	//	 * Print the report total
	//	 */
	//	private void generateNoBatchTitles()
	//	{
	//		generatePageBreak(2);
	//		caRpt.blankLines(1);
	//		caRpt.println(
	//			"    THERE ARE NO BATCH TITLES FOR THE REPORTING PERIOD.");
	//
	//		// unit test
	//		csTestProgramFlow.append("Nbt");
	//	}
	// end defect 10013 

	/**
	 * Generate the page break with lines required
	 * 
	 * @param aiLinesRequired int
	 */
	private void generatePageBreak(
		int aiLinesRequired,
		boolean abTitle)
	{
		if (getNoOfDetailLines() < aiLinesRequired)
		{
			generateFooter();
			generateHeader();

			// defect 10013
			if (abTitle)
			{
				if (ciIndex != 0)
				{
					TitlePackageReportData laPrevData =
						(
							TitlePackageReportData) cvTtlPkgData
								.elementAt(
							ciIndex - 1);

					if (laPrevData.getTransWsId()
						== caTtlPkgRptData.getTransWsId())
					{
						cbPrintedHeader = false;
						cbContd = true;
					}
				}
			}
			else
			{
				cbContd = cbPrintedHeader;
				cbPrintedHeader = false;
			}
			// end defect 10013 
		}
	}

	/**
	 * Print the total batch count for the report
	 */
	private void generateReportTotal()
	{
		generatePageBreak(4, false);

		// defect 9976 
		caRpt.blankLines(2);
		csEttlTotal = CommonConstant.STR_SPACE_EMPTY;

		if (ciBatchQtyRptTotal != 0)
		{
			// defect 10013
			String lsSuffix =
				ciBatchETtlQtyRptTotal == 1 ? ETITLE_SINGLE : ETITLE;
			// csEttlTotal = LEFTPAREN + ciBatchETtlQtyRptTotal + ETITLE;
			csEttlTotal = LEFTPAREN + ciBatchETtlQtyRptTotal + lsSuffix;
			// end defect 10013 
		}

		// defect 10013
		caRpt.print(
			REPORT_TOTAL_PACKAGES,
			QTY_DESC_START,
			REPORT_TOTAL_PACKAGES.length());
		caRpt.rightAlign(
			Integer.toString(ciBatchQtyRptTotal),
			QTY_START,
			QTY_LENGTH);

		caRpt.print(csEttlTotal, ETITLE_START, csEttlTotal.length());

		caRpt.blankLines(1);
		caRpt.print(VOID_TOTAL, QTY_DESC_START, QTY_DESC_LENGTH);

		caRpt.rightAlign(
			Integer.toString(ciRptVoidCount),
			QTY_START,
			QTY_LENGTH);

		caRpt.blankLines(1);
		caRpt.print(
			NEXT_DAY_VOID_TOTAL,
			QTY_DESC_START,
			NEXT_DAY_VOID_TOTAL.length());

		caRpt.rightAlign(
			Integer.toString(ciRptNextDayVoidCount),
			QTY_START,
			QTY_LENGTH);
		// end defect 10013 

		csTestProgramFlow.append("Tr"); // unit test   		
	}

	/**
	 * Get next Title Package Package record.
	 * 
	 * @param aiRecIndex int
	 */
	private void getRecord()
	{
		// defect 10013 
		caTtlPkgRptData =
			(TitlePackageReportData) cvTtlPkgData.elementAt(ciIndex);
		// end defect 10013 
	}

	/**
	 *  
	 * Print Voided Transactions
	 *
	 */
	private void printVoidedTrans()
	{
		if (cvVoidedTrans != null && !cvVoidedTrans.isEmpty())
		{
			generatePageBreak(2, false);
			cbPrintedHeader = false;
			cbContd = false;

			for (int j = 0; j < cvVoidedTrans.size(); j++)
			{
				TitlePackageReportData laData =
					(TitlePackageReportData) cvVoidedTrans.elementAt(j);
				generateVoidDataLine(laData);
			}
			ciRptVoidCount = ciRptVoidCount + cvVoidedTrans.size();
			cvVoidedTrans = new Vector();
		}
		cbPrintedHeader = false;
		cbContd = false;
	}

	/**
	 *  
	 * Print Next Day Voided Transactions
	 *
	 */
	private void printNextDayVoidedTrans()
	{
		if (cvNextDayVoidedTrans != null
			&& !cvNextDayVoidedTrans.isEmpty())
		{
			generatePageBreak(2, false);
			caRpt.blankLines(1);
			cbPrintedHeader = false;

			for (int j = 0; j < cvNextDayVoidedTrans.size(); j++)
			{
				TitlePackageReportData laData =
					(
						TitlePackageReportData) cvNextDayVoidedTrans
							.elementAt(
						j);

				generateVoidDataLine(laData);
			}

			ciRptNextDayVoidCount =
				ciRptNextDayVoidCount + cvNextDayVoidedTrans.size();
			cvNextDayVoidedTrans = new Vector();
		}
	}

	// defect 10013 
	//	/**
	//	 * Inserts header row (selected WsIds) into cvTtlPkgData vector where
	//	 * no TtlPkgData exists for a workstation.
	//	 */
	//	private void insertHeaderRowsIntoDataVector()
	//	{
	//		TitlePackageReportData laReportElement;
	//		int liSelectedWsId;
	//		int liDataRow = 0;
	//
	//		sortSelectedWsIdVector();
	//		
	//		getRecord(liDataRow);
	//		for (int i = 0; i < cvSelectedWsId.size(); i++)
	//		{
	//			liSelectedWsId =
	//				Integer
	//					.valueOf((String) cvSelectedWsId.elementAt(i))
	//					.intValue();
	//
	//			if ((caTtlPkgRptData == null)
	//				|| caTtlPkgRptData.getTransWsId() != liSelectedWsId)
	//			{
	//				cvNoBatch.add(new Integer(liSelectedWsId)); 
	//				laReportElement = new TitlePackageReportData();
	//				laReportElement.setNoTitle(true);
	//				laReportElement.setAcctItmCd("");
	//				laReportElement.setBatchNo("");
	//				laReportElement.setItmPrice(new Dollar(0));
	//				laReportElement.setOfcIssuanceNo(0);
	//				laReportElement.setTransAMDate(0);
	//				laReportElement.setTransCd("");
	//				laReportElement.setTransTime(0);
	//				laReportElement.setTransWsId(liSelectedWsId);
	//				laReportElement.setVoidedTransIndi(0);
	//				cvTtlPkgData.insertElementAt(
	//					laReportElement,
	//					liDataRow);
	//				liDataRow++;
	//			}
	//			while (liDataRow < cvTtlPkgData.size())
	//			{
	//				getRecord(liDataRow);
	//				if (liSelectedWsId != caTtlPkgRptData.getTransWsId())
	//				{
	//					break;
	//				}
	//				liDataRow++;
	//			}
	//		}
	//	}
	// end defect 10013 

	/**
	 * Starts the application.
	 * 
	 * 				****  N O T E  !!!  ****
	 * This method is executed for testing purpose only 
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties(REPORT_NUMBER);
		GenTitlePackageReport laGPR =
			new GenTitlePackageReport(REPORT_NAME, laRptProps);
		// Generating Demo data to display.
		String lsQuery = "dummy input statement";
		Vector lvQueryResults = laGPR.queryData(lsQuery);

		laGPR.formatReport(lvQueryResults);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;

		try
		{
			laOutputFile = new File(FILE_PATH_LOCATION);
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGPR.caRpt.getReport().toString());
		laPout.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport(FILE_PATH_LOCATION);
			laFrmPreviewReport.setModal(true);

			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			// defect 7590
			// change setVisible to setVisibleRTS
			laFrmPreviewReport.setVisibleRTS(true);
			// end defect 7590
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec(
			// "cmd.exe /c copy c:\\TitlePkgRpt.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * The queryData() loads the TitlePackageReportData data object
	 * into a vector avResults.
	 * This is passed to the formatReport() method.
	 *
	 * 			****  N O T E  !!!  ****
	 * This method is executed for testing purpose only
	 *
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		//		// Generating vector for data object 
		//		Vector lvHoldData = new Vector();
		//		Vector lvWsId = new Vector();
		//		Vector lvResults = new Vector();
		//
		//		for (int i = 0; i < 100; i++)
		//		{
		//			lvHoldData.addElement(
		//				new TitlePackageReportData(
		//					161,
		//					1,
		//					1,
		//					37600,
		//					90000 + i,
		//					"TITLE",
		//					"16100101",
		//					"TITLE",
		//					0,
		//					new Dollar(13.00),
		//					2,
		//					1));
		//		}
		//
		//		for (int i = 0; i < 61; i++)
		//		{
		//			lvHoldData.addElement(
		//				new TitlePackageReportData(
		//					161,
		//					1,
		//					51,
		//					37600,
		//					10000 + i,
		//					"TITLE",
		//					"16105101",
		//					"TITLE",
		//					0,
		//					new Dollar(13.00),
		//					2,
		//					1));
		//		}
		//
		//		lvHoldData.addElement(
		//			new TitlePackageReportData(
		//				161,
		//				1,
		//				201,
		//				37600,
		//				95234,
		//				"TITLE",
		//				"16120101",
		//				"TITLE",
		//				0,
		//				new Dollar(13.00),
		//				2,
		//				1));
		//
		//		lvHoldData.addElement(
		//			new TitlePackageReportData(
		//				161,
		//				1,
		//				201,
		//				37600,
		//				100422,
		//				"DTAORD",
		//				"16120101",
		//				"TITLE",
		//				0,
		//				new Dollar(13.00),
		//				2,
		//				1));
		//
		//		lvHoldData.addElement(
		//			new TitlePackageReportData(
		//				161,
		//				1,
		//				201,
		//				37600,
		//				100548,
		//				"DTAORD",
		//				"16120101",
		//				"TITLE",
		//				1,
		//				new Dollar(13.00),
		//				2,
		//				1));
		//
		//		lvHoldData.addElement(
		//			new TitlePackageReportData(
		//				161,
		//				1,
		//				201,
		//				37600,
		//				143603,
		//				"TITLE",
		//				"16120102",
		//				"TITLE",
		//				0,
		//				new Dollar(137.50),
		//				2,
		//				1));
		//
		//		lvHoldData.addElement(
		//			new TitlePackageReportData(
		//				161,
		//				1,
		//				201,
		//				37600,
		//				144856,
		//				"TITLE",
		//				"16120102",
		//				"TITLE",
		//				0,
		//				new Dollar(5.30),
		//				2,
		//				1));
		//
		//		lvResults.addElement(lvHoldData);
		//		lvWsId.addElement("51");
		//
		//		// exactly full page (should produce no extra page, 
		//		// subtotals should flow onto next page)
		//		lvWsId.addElement("101"); // no data
		//		lvWsId.addElement("201"); // 2 batch numbers
		//		lvWsId.addElement("1"); // 1 batch number, force page overflow
		//		lvWsId.addElement("0"); // no batch number
		//
		//		lvResults.addElement(lvWsId);
		//		lvResults.addElement("37601");

		return new Vector();
	}

	//	/**
	//	 * Code for sorting the cvSelectedWsId
	//	 */
	//	private void sortSelectedWsIdVector()
	//	{
	//		Vector lvSelectedWsId = new Vector();
	//
	//		Integer liSelectedWsId = null;
	//
	//		for (int i = 0; i < cvSelectedWsId.size(); i++)
	//		{
	//			liSelectedWsId =
	//				Integer.valueOf((String) cvSelectedWsId.elementAt(i));
	//
	//			lvSelectedWsId.addElement(liSelectedWsId);
	//		}
	//
	//		if (lvSelectedWsId.size() != 0)
	//		{
	//			cvSelectedWsId = new Vector();
	//
	//			// throw away unsorted WsId char vector and replace with sorted...
	//			Collections.sort(lvSelectedWsId);
	//
	//			for (int i = 0; i < lvSelectedWsId.size(); i++)
	//			{
	//				liSelectedWsId = (Integer) lvSelectedWsId.elementAt(i);
	//				cvSelectedWsId.addElement(liSelectedWsId.toString());
	//			}
	//			// defect 7002
	//			// Initialize ciPrevWsId to 1st selected WsId 
	//			ciPrevWsId =
	//				((Integer) lvSelectedWsId.elementAt(0)).intValue();
	//			// end defect 7002 
	//		}
	//	}
}