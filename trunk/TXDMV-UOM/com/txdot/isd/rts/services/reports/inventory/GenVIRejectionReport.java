package com.txdot.isd.rts.services.reports.inventory;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * 
 * GenInventoryInquiryReport.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		04/03/2009	fix typo 
 * 							modify ColumnHeader()
 * 							defect 9117 Ver Special Plates	
 * Min Wang		04/11/2009	reformat report
 * 							modify formatReport()
 * 			 				defect 9117 Ver Special Plates	
 * Min Wang		04/16/2007  define constants.
 * 							defect 9117 Ver Special Plates
 * Min Wang		07/03/2007	display the error on the report even though 
 * 							there is no err message in the table.
 * 							modify formatReport()
 * 			 				defect 9117 Ver Special Plates	
 * K Harrell	08/10/2009	Implement new generateFooter(boolean) 
 * 							modify formatReport() 
 * 							defect 8628 Ver Defect_POS_F      
 * ---------------------------------------------------------------------
 */

/**
 * This class generates the Virtual Inventory Rejection Report
 *
 * @version	Defect_POS_F	08/10/2009
 * @author  Min Wang
 * <br>Creation Date: 		10/12/2001 05:16:32
 */

public class GenVIRejectionReport extends ReportTemplate
{
	private static final int START_PT_1 = 1;
	private static final int START_PT_16 = 16;
	private static final int START_PT_28 = 28;
	private static final int START_PT_43 = 43;
	private static final int START_PT_52 = 52;
	private static final int START_PT_55 = 55;
	private static final int START_PT_62 = 62;
	private static final int START_PT_85 = 85;
	private static final int START_PT_110 = 110;
	private static final int START_PT_118 = 118;
	private static final int LENGTH_1 = 1;
	private static final int LENGTH_2 = 2;
	private static final int LENGTH_3 = 3;
	private static final int LENGTH_4 = 4;
	private static final int LENGTH_5 = 5;
	private static final int LENGTH_6 = 6;
	private static final int LENGTH_7 = 7;
	private static final int LENGTH_8 = 8;
	private static final int LENGTH_9 = 9;
	private static final int LENGTH_11 = 11;
	private static final int LENGTH_15 = 15;
	private static final int LENGTH_19 = 19;

	private static final int NUMBER_DETAIL_LINES = 3;

	private static final int WHITE_SPACE = 4;

	private static final int SAMPLE_SUBSTAID_1 = 0;
	private static final String DATE = "DATE";
	private static final String REQUEST = "REQUEST";
	private static final String MANUFACTURE = "MANUFACTURE";
	private static final String ITEM = "ITEM";
	private static final String PLATE = "PLATE";
	private static final String REASON = "REASON";
	private static final String REQUESTER = "REQUESTER";
	private static final String NUMBER = "NUMBER";
	private static final String CODE = "CODE";
	private static final String IVTRS = "IVTRS";
	private static final String IP = "IP";
	private static final String TIME= "TIME";
	private static final String OFC = "OFC";
	private static final String EMPLOYEE = "EMPLOYEE";
	private static final String REASON_DESC = "REASON DESCRIPTION: ";
	private static final String NO_DESC = "NO DESCRIPTION";
	
	private static final String SAMPLE_REPORT_FILENAME =
		"c:\\VIRejRpt.txt";

	/**
	 * GenVIRejectionReport constructor
	 */
	public GenVIRejectionReport()
	{
		super();
	}

	/**
	 * GenVIRejectionReport constructor
	 * 
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenVIRejectionReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Formatting data for the report.
	 * 
	 * @param avReportIn Vector
	 */
	public void formatReport(Vector avReportIn)
	{
		// get the SpecialPlateRejectionLogData object off the vector
		String laReportDate = (String) avReportIn.get(0);
		// get the queryResults vector off of the input vector
		Vector avResults = (Vector) avReportIn.get(1);

		// Defining vectors
		Vector lvHeader = new Vector();
		Vector lvTable = new Vector();
		Vector lvRow1 = new Vector();
		Vector lvRow2 = new Vector();
		Vector lvRow3 = new Vector();

		// Change how Dates are reported						
		// Adding additional Header information
		lvHeader.addElement(DATE);
		lvHeader.addElement(laReportDate);
		
		// Column Header for Row1
		ColumnHeader laColumn_1_1 =
			new ColumnHeader(REQUEST, START_PT_1, LENGTH_7);
		ColumnHeader laColumn_1_2 = new ColumnHeader(REQUEST, START_PT_16, LENGTH_7);
		 
		//Column Header for Row2
		ColumnHeader laColumn_2_1 = new ColumnHeader(MANUFACTURE, START_PT_1, LENGTH_11);
		ColumnHeader laColumn_2_2 = new ColumnHeader(ITEM, START_PT_16, LENGTH_4);
		ColumnHeader laColumn_2_3 = new ColumnHeader(PLATE, START_PT_28, LENGTH_5);
		ColumnHeader laColumn_2_4 = new ColumnHeader(REASON, START_PT_43, LENGTH_6);
		ColumnHeader laColumn_2_6 = new ColumnHeader(REQUESTER, START_PT_62, LENGTH_9);
		ColumnHeader laColumn_2_7 = new ColumnHeader(REQUEST, START_PT_85, LENGTH_7);
		
		//Column Header for Row3
		ColumnHeader laColumn_3_1 = new ColumnHeader(PLATE, START_PT_1, LENGTH_5);
		ColumnHeader laColumn_3_2 = new ColumnHeader(NUMBER, START_PT_16, LENGTH_6);
		ColumnHeader laColumn_3_3 = new ColumnHeader(NUMBER, START_PT_28, LENGTH_6);
		ColumnHeader laColumn_3_4 = new ColumnHeader(CODE, START_PT_43, LENGTH_4); 
		ColumnHeader laColumn_3_5 = new ColumnHeader(IVTRS, START_PT_52, LENGTH_5); 
		ColumnHeader laColumn_3_6 = new ColumnHeader(IP, START_PT_62, LENGTH_2);
		ColumnHeader laColumn_3_7 = new ColumnHeader(TIME, START_PT_85, LENGTH_4);
		ColumnHeader laColumn_3_8 = new ColumnHeader(OFC, START_PT_110, LENGTH_3);
		ColumnHeader laColumn_3_9 = new ColumnHeader(EMPLOYEE, START_PT_118, LENGTH_8);

		 
		//Alignment of column headers to rows 1, 2 and 3.   
		lvRow1.addElement(laColumn_1_1);
		lvRow1.addElement(laColumn_1_2);
		lvRow2.addElement(laColumn_2_1);
		lvRow2.addElement(laColumn_2_2);
		lvRow2.addElement(laColumn_2_3);
		lvRow2.addElement(laColumn_2_4);
		lvRow2.addElement(laColumn_2_6);
		lvRow2.addElement(laColumn_2_7);
		lvRow3.addElement(laColumn_3_1);
		lvRow3.addElement(laColumn_3_2);
		lvRow3.addElement(laColumn_3_3);
		lvRow3.addElement(laColumn_3_4);
		lvRow3.addElement(laColumn_3_5);
		lvRow3.addElement(laColumn_3_6);
		lvRow3.addElement(laColumn_3_7);
		lvRow3.addElement(laColumn_3_8);
		lvRow3.addElement(laColumn_3_9);

		//Adding ColumnHeader Information	
		lvTable.addElement(lvRow1);
		lvTable.addElement(lvRow2);
		lvTable.addElement(lvRow3);

		SpecialPlateRejectionLogData laSpclPltRejLogData =
			new SpecialPlateRejectionLogData();

		int i = 0;
		if (avResults != null && avResults.size() > 0)
		{
			while (i < avResults.size())
			{ //Loop through the results
				generateHeader(lvHeader, lvTable);
				int j = getNoOfDetailLines() - WHITE_SPACE;
				//Get Available lines on the page
				for (int k = 0; k <= j; k = k + NUMBER_DETAIL_LINES)
				{
					if (i < avResults.size())
					{
						laSpclPltRejLogData =
							(
								SpecialPlateRejectionLogData) avResults
									.elementAt(
								i);
						this.caRpt.print(
							String.valueOf(
								laSpclPltRejLogData.getMfgPltNo()),
						START_PT_1,
						LENGTH_7);
						this.caRpt.print(
							laSpclPltRejLogData.getInvItmNo(),
						START_PT_16,
						LENGTH_7);
						this.caRpt.print(
							laSpclPltRejLogData.getRegPltNo(),
						START_PT_28,
						LENGTH_7);
						this.caRpt.print(
							String.valueOf(
								laSpclPltRejLogData.getRejReasnCd()),
						START_PT_43,
						LENGTH_4);
						if ( laSpclPltRejLogData.getItrntReqIndi() != 0 )
						{
							this.caRpt.print(
								String.valueOf(
								laSpclPltRejLogData.getItrntReqIndi()),
							START_PT_55,
							LENGTH_1);
						}
						this.caRpt.print(
							laSpclPltRejLogData.getReqIPAddr(),
						START_PT_62,
						LENGTH_15);
						this.caRpt.print(
							laSpclPltRejLogData
								.getReqTimestmp()
								.getDB2Date(),
						START_PT_85,
						LENGTH_19);

						this.caRpt.rightAlign(
							String.valueOf(
								laSpclPltRejLogData.getOfcIssuanceNo()),
						START_PT_110,
						LENGTH_3);
						this.caRpt.print(
							laSpclPltRejLogData.getTransEmpId(),
						START_PT_118,
						LENGTH_15);
						this.caRpt.nextLine();
						if (laSpclPltRejLogData.getErrMsgDesc().equals(CommonConstant.STR_SPACE_EMPTY))
						{
							this.caRpt.print(REASON_DESC + NO_DESC);
						}
						else
						{
							this.caRpt.print(
							REASON_DESC + 
							laSpclPltRejLogData.getErrMsgDesc(),
							START_PT_1,
							80);
						}
						this.caRpt.nextLine();
						i = i + 1;
					}
				}
				// defect 8628 
				//if (i >= avResults.size())
				//{
				//	generateEndOfReport();
				//}
				//generateFooter();
				generateFooter(i >= avResults.size());
				// end defect 8628  
			}
		}
		else
		{
			generateHeader(lvHeader, lvTable);
			generateNoRecordFoundMsg();
			// defect 8628 
			// generateFooter();
			generateFooter(true); 
			// end defect 8628
		}
	}

	/**
	 * Generate Attributes for Report
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Main method for stand alone testing.
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{

		ReportProperties laRptProps =
			new ReportProperties(
				ReportConstant.RPT_3073_VI_REJ_REPORT_ID);
		GenVIRejectionReport laGenVIRejReport =
			new GenVIRejectionReport(
				ReportConstant.RPT_3073_VI_REJ_REPORT_TITLE,
				laRptProps);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File(SAMPLE_REPORT_FILENAME);
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGenVIRejReport.caRpt.getReport().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport(SAMPLE_REPORT_FILENAME);
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(SAMPLE_SUBSTAID_1);
				};
			});
			laFrmPreviewReport.show();
			laFrmPreviewReport.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Data generated for demo.
	 * 
	 * @param asQuery String
	 */
	public Vector queryData(String asQuery)
	{
		// Generating Demo data to display.
		Vector lvData = new Vector();

		return lvData;
	}
}
