package com.txdot.isd.rts.services.reports;

import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.OfficeTimeZoneCache;
import com.txdot.isd.rts.services.communication.Comm;
import com.txdot.isd.rts.services.util.Print;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * ReportTemplate.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * R Duggirala	08/31/2001	New Class
 * R Duggirala	09/04/2001	Added Comments
 * Ray Rowehl	05/13/2002	Change how an exception shows up
 *							on the footer.  CQU100003811
 * Ray Rowehl	06/01/2002	Add center and right alignment to
 *							generateHeader(Vector, Vector, Vector)
 *							It was added to the two vector method.
 *							Just left off on the three vector method.
 *							defect 4195
 * J Rue		05/05/2003	Defect 6032, Add method printPatchCode()
 *							to print patch code from salvage.
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify
 *							defect 7896 Ver 5.2.3
 * J Ralph		01/27/2006	FormFeed constant cleanup
 * 							Source format
 * 							modify generateFooter()
 * 							defect 8524 Ver 5.2.3   
 * T Pederson	04/05/2007	add common address formatting methods 
 * 							(copied from ReceiptTemplate)
 * 							add generateCityCntry() and
 * 							generateZipCdP4()
 * 							defect 9123 Ver Special Plates 
 * Bob Brown	12/16/2008	Add credit card type
 * 							add csCardType, and its getter and setters.
 * 							defect 9878 Ver Defect_POS_C 
 * Bob Brown	12/16/2008	Add totals page for credit card totals
 * 							add generateTotalHeader()
 * 							defect 9878 Ver Defect_POS_C
 * K Harrell	06/08/2009	Implement RTSDate.getClockTimeNoMs()
 * 							modify generateFooter()
 * 							defect 9963 Ver Defect_POS_D
 * K Harrell	07/12/2009	delete generateCityCntry(), generateZipCdP4 
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	08/10/2009	add END_OF_REPORT
 * 							add generateFooter(boolean) 
 * 							modify generateFooter()
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	10/12/2009	add generateHeader(Vector,Vector,boolean) 
 * 							modify generateHeader(Vector,Vector) 
 * 							defect 10207 Ver Defect_POS_G 
 * K Harrell	04/03/2010	Implement OfficeTimeZoneCache
 * 							modify generateFooter() 
 * 							defect 10427 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/**
 * The ReportTemplate Class is an abstract class that must be
 * implemented by all classes that intend to generate reports.
 *
 * @version	POS_640 			04/03/2010
 * @author	Rakesh Duggirala
 * <br>Creation Date:			08/31/2001 
 */
public abstract class ReportTemplate
{
	private final static int LENGTH1 = 1;
	private final static int START_PT49 = 49;

	// defect 8628 
	private final static String END_OF_REPORT =
		". . . END OF REPORT . . .";
	// end defect 8628  

	public Report caRpt = new Report();

	protected ReportProperties caRptProps = new ReportProperties();

	/**
	 * ReportTemplate default constructor
	 */
	public ReportTemplate()
	{
		super();
	}

	/**
	 * This constructor must be called to instantiate this class
	 * 
	 * @param asRptName String Name of the report
	 * @param aaRptProps ReportProperties Properties of the report
	 */
	public ReportTemplate(
		String asRptName,
		ReportProperties aaRptProps)
	{
		caRpt =
			new Report(
				asRptName,
				aaRptProps.getPageWidth(),
				aaRptProps.getPageHeight());
		caRptProps = aaRptProps;
	}

	/**
	 *	This method is used to help of keep lines of data together
	 * 	(prevention of widow/orpahn). If the number of data lines that
	 *	are to be printed out in a group, is more that what is available
	 *	we will send back TRUE to indicate that a new page is needed. 
	 *	FALSE is returned when there is enough lines available for the
	 *	group of data. For example, if a report that has already printed
	 *	out 72 lines and has a group of  data that requires 5 lines
	 *	we would return TRUE - a new page is needed. (77 total lines
	 *	minus 72 lines already printed minus 2 report footer lines
	 *	leaves 3 available lines remaining  for printing and we need 5).
	 *	Another example, if a report that has already printed out 54
	 *	lines and has a group of data that requires 5 lines we would
	 *	return FALSE - no new page is needed ( 77 - 54 - 3 = 20 lines
	 *	available for printing).  The five lines will be accomodated.	 
	 * 
	 * @param aiLinesNeeded int
	 * @return boolean
	 */
	public boolean checkForWidowOrphan(int aiLinesNeeded)
	{
		// Number of lines of a page less the number of lines we have
		// already printed out, less report footer lines
		// gives us the number of lines that are available for printing
		int liTotalLinesRemaining =
			caRptProps.getPageHeight() - caRpt.getCurrX() - 2;

		// if Lines available on the page is >= the the lines needed by
		// the group of data, no new page is needed	
		if (liTotalLinesRemaining >= aiLinesNeeded)
		{
			return false;
		}
		// lines available on the page is less that the lines need
		// by the group of data, a new page is needed.
		else
		{
			return true;
		}
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param avResults Vector
	 */
	public abstract void formatReport(Vector avResults);

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public abstract void generateAttributes();

	// defect 8628 
	//	/**
	//	 * Print the End Of Report text
	//	 */
	//	protected void generateEndOfReport()
	//	{
	//		caRpt.nextLine();
	//		caRpt.center(". . . END OF REPORT . . .");
	//	}
	// end defect 8628 

	/**
	 * 
	 * Generate Footer
	 */
	protected void generateFooter()
	{
		generateFooter(false);
	}

	/**
	 * This Method appends the footer information each time it is called
	 */
	protected void generateFooter(boolean abEndOfReport)
	{
		// defect 10427 
		// Use OfficeTimeZoneCache vs. DB2 Call via 
		//  CacheManagerServerBusiness
		//	try
		//{
		// end defect 10427 
		
		String lsTimeZone = "";
		RTSDate laTimeZoneAdjustedDate = new RTSDate();

		if (Comm.isServer())
		{
			// defect 10427 
			//CacheManagerServerBusiness laCacheManagerServerBusiness =
			//	new CacheManagerServerBusiness();
			// lsTimeZone =  
			//					laCacheManagerServerBusiness.getOfficeTimeZone(
			//						caRptProps.getOfficeIssuanceId(),
			//						caRptProps.getSubstationId());

			lsTimeZone =
				OfficeTimeZoneCache.getTimeZone(
					caRptProps.getOfficeIssuanceId());
			// end defect 10427 

			laTimeZoneAdjustedDate = RTSDate.getCurrentDate(lsTimeZone);
		}
		caRpt.setFooter(2);

		// defect 8628 
		caRpt.print("RUNDATE  " + laTimeZoneAdjustedDate.toString());
		if (abEndOfReport)
		{
			caRpt.center(END_OF_REPORT, 1, caRpt.getWidth());
		}
		caRpt.println(CommonConstant.STR_SPACE_EMPTY);
		// end defect 8628 

		// defect 9943
		caRpt.print(
			"RUNTIME  " + laTimeZoneAdjustedDate.getClockTimeNoMs());
		//.getClockTime().substring(0,8));
		// end defect 9943 

		DecimalFormat laTwoDigits = new DecimalFormat("#0");
		String lsPageFormated = laTwoDigits.format(caRpt.ciPages);
		// defect 8524
		// FormFeed constant cleanup 
		caRpt.rightAlign(
			"PAGE " + lsPageFormated + " " + ReportConstant.FF);
		// end defect 8524
		
		// defect 10427 
		//		}
		//		catch (RTSException aeRTSEx)
		//		{
		//			// do not show Server when an exception is shown
		//			// Add exception thrown though.  CQU100003811
		//			//throw new RTSException(RTSException.JAVA_ERROR, e);
		//			//Temp code till I go back to fix the RTSException
		//			aeRTSEx.printStackTrace();
		//			RTSDate timeZoneAdjustedDate = new RTSDate();
		//			caRpt.setFooter(2);
		//			caRpt.println(
		//				"RUNDATE "
		//					+ timeZoneAdjustedDate.toString()
		//					+ " Exception Thrown");
		//
		//			// defect 9943 
		//			caRpt.print(
		//				"RUNTIME " + timeZoneAdjustedDate.getClockTimeNoMs());
		//			//.getClockTime().substring(0,8));
		//			// end defect 9943 
		//
		//			DecimalFormat laTwoDigits = new DecimalFormat("#0");
		//			String lsPageFormated = laTwoDigits.format(caRpt.ciPages);
		//			// defect 8524
		//			// FormFeed constant cleanup 
		//			caRpt.rightAlign(
		//				"PAGE " + lsPageFormated + " " + ReportConstant.FF);
		//			// end defect 8524
		//		}
		// end defect 10427 
	}

	/**
	 * This Method appends the header information each time it is called
	 *
	 * @param avTableHeader Vector Table headers that need to be
	 * 	displayed on each page
	 */
	protected void generateHeader(Vector avTableHeader)
	{
		caRpt.blankLines(3);
		if (avTableHeader != null)
		{
			for (int i = 0; i < avTableHeader.size(); i++)
			{
				Vector lvLine = (Vector) avTableHeader.elementAt(i);
				for (int j = 0; j < lvLine.size(); j++)
				{
					ColumnHeader laColHead =
						(ColumnHeader) lvLine.elementAt(j);
					switch (laColHead.getAlignment())
					{
						case 1 : //Center Align
							{
								caRpt.center(
									laColHead.csDesc,
									laColHead.ciStartPoint,
									laColHead.ciLength);
								break;
							}
						case 2 : //Right Align
							{
								caRpt.rightAlign(
									laColHead.csDesc,
									laColHead.ciStartPoint,
									laColHead.ciLength);
								break;
							}
						default : //Left Align
							{
								caRpt.print(
									laColHead.csDesc,
									laColHead.ciStartPoint,
									laColHead.ciLength);
							}
					}
				}
				caRpt.nextLine();
			}
		}
		caRpt.drawDashedLine();
	}

	/**
	 * This Method appends the header information each time it is called
	 *
	 * @param avMoreHeader Vector Additional Headers that need to be
	 * 	displayed on each page
	 * @param avTableHeader Vector Table headers that need to be
	 * 	displayed on each page
	 */
	protected void generateHeader(
		Vector avMoreHeader,
		Vector avTableHeader)
	{
		// defect 10207 
		generateHeader(avMoreHeader, avTableHeader, true);
		// end defect 10207
	}

	/**
	 * This Method appends the header information each time it is called
	 *
	 * @param avMoreHeader Vector Additional Headers that need to be
	 * 	displayed on each page
	 * @param avTableHeader Vector Table headers that need to be
	 * 	displayed on each page
	 * @param abPrintDashedLine 
	 */
	protected void generateHeader(
		Vector avMoreHeader,
		Vector avTableHeader,
		boolean abPrintDashedLine)
	{
		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(caRptProps.getOfficeIssuanceName());
		caRpt.center(caRptProps.getSubstationName());
		caRpt.blankLines(1);
		caRpt.print("WORKSTATION ID", 0, 20);
		caRpt.print(" : ");
		caRpt.println(caRptProps.getWorkstationId() + "");
		caRpt.print("REQUESTED BY", 0, 20);
		caRpt.print(" : ");
		caRpt.println(caRptProps.getRequestedBy().toUpperCase());
		if (avMoreHeader != null)
		{
			for (int i = 0; i < avMoreHeader.size(); i++)
			{
				if ((i == 0) || (i % 2 == 0))
				{
					caRpt.print(
						avMoreHeader.elementAt(i).toString(),
						0,
						20);
					caRpt.print(" : ");
				}
				else
				{
					caRpt.println(avMoreHeader.elementAt(i).toString());
				}
			}
		}
		caRpt.blankLines(2);

		if (avTableHeader != null
			&& avTableHeader.size() > 0) // end defect 7069
		{
			for (int i = 0; i < avTableHeader.size(); i++)
			{
				Vector lvLine = (Vector) avTableHeader.elementAt(i);
				for (int j = 0; j < lvLine.size(); j++)
				{
					ColumnHeader laColHead =
						(ColumnHeader) lvLine.elementAt(j);
					switch (laColHead.getAlignment())
					{
						case 1 : //Center Align
							{
								caRpt.center(
									laColHead.csDesc,
									laColHead.ciStartPoint,
									laColHead.ciLength);
								break;
							}
						case 2 : //Right Align
							{
								caRpt.rightAlign(
									laColHead.csDesc,
									laColHead.ciStartPoint,
									laColHead.ciLength);
								break;
							}
						default : //Left Align
							{
								caRpt.print(
									laColHead.csDesc,
									laColHead.ciStartPoint,
									laColHead.ciLength);
							}
					}
				}
				caRpt.nextLine();
			}
		}
		if (abPrintDashedLine)
		{
			caRpt.drawDashedLine();
		}
	}

	/**
	 * Generate the report header for the case where there is an 
	 * additional description line(s) (aMoreDetails) needed above the
	 * column headers.
	 * 
	 * @param avMoreHeader Vector
	 * @param avMoreDetails Vector
	 * @param avTableHeader Vector
	 */
	public void generateHeader(
		Vector avMoreHeader,
		Vector avMoreDetails,
		Vector avTableHeader)
	{
		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(caRptProps.getOfficeIssuanceName());
		caRpt.center(caRptProps.getSubstationName());
		caRpt.blankLines(1);
		caRpt.print("WORKSTATION ID", 0, 20);
		caRpt.print(" : ");
		caRpt.println(caRptProps.getWorkstationId() + "");
		caRpt.print("REQUESTED BY", 0, 20);
		caRpt.print(" : ");
		caRpt.println(caRptProps.getRequestedBy());

		if (avMoreHeader != null)
		{
			for (int i = 0; i < avMoreHeader.size(); i++)
			{
				if ((i == 0) || (i % 2 == 0))
				{
					caRpt.print(
						avMoreHeader.elementAt(i).toString(),
						0,
						20);
					caRpt.print(" : ");
				}
				else
					caRpt.println(avMoreHeader.elementAt(i).toString());
			}
		}
		caRpt.blankLines(1);

		if (avMoreDetails != null)
		{
			for (int i = 0; i < avMoreDetails.size(); i++)
			{
				caRpt.center(avMoreDetails.elementAt(i).toString());
			}
		}

		caRpt.blankLines(1);
		if (avTableHeader != null)
		{
			for (int i = 0; i < avTableHeader.size(); i++)
			{
				Vector lvLine = (Vector) avTableHeader.elementAt(i);
				// Defect 4195
				// commented out code is original.
				// New code below is copied directly from
				// generateHeader(Vector, Vector)
				//    for (int j = 0; j < line.size(); j++) {
				//        ColumnHeader cHead = (ColumnHeader)
				//		  	line.elementAt(j);
				//        cRpt.print(cHead.csDesc, cHead.ciStartPoint,
				//			cHead.ciLength);
				//    }
				for (int j = 0; j < lvLine.size(); j++)
				{
					ColumnHeader laColHead =
						(ColumnHeader) lvLine.elementAt(j);
					switch (laColHead.getAlignment())
					{
						case 1 : //Center Align
							{
								caRpt.center(
									laColHead.csDesc,
									laColHead.ciStartPoint,
									laColHead.ciLength);
								break;
							}
						case 2 : //Right Align
							{
								caRpt.rightAlign(
									laColHead.csDesc,
									laColHead.ciStartPoint,
									laColHead.ciLength);
								break;
							}
						default : //Left Align
							{
								caRpt.print(
									laColHead.csDesc,
									laColHead.ciStartPoint,
									laColHead.ciLength);
							}
					}
				}
				// end defect 4195
				caRpt.nextLine();
			}
		}
		caRpt.drawDashedLine();
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
	 * This Method appends the header information each time it is called
	 *
	 * @param avMoreHeader Vector Additional Headers that need to be
	 * 	displayed on each page
	 */
	protected void generateTotalHeader(Vector avMoreHeader)
	{
		caRpt.print(caRptProps.getUniqueName());
		caRpt.center(caRpt.csName);
		caRpt.center(caRptProps.getOfficeIssuanceName());
		caRpt.center(caRptProps.getSubstationName());
		caRpt.blankLines(1);
		caRpt.print("WORKSTATION ID", 0, 20);
		caRpt.print(" : ");
		caRpt.println(caRptProps.getWorkstationId() + "");
		caRpt.print("REQUESTED BY", 0, 20);
		caRpt.print(" : ");
		caRpt.println(caRptProps.getRequestedBy().toUpperCase());
		if (avMoreHeader != null)
		{
			for (int i = 0; i < avMoreHeader.size(); i++)
			{
				if ((i == 0) || (i % 2 == 0))
				{
					caRpt.print(
						avMoreHeader.elementAt(i).toString(),
						0,
						20);
					caRpt.print(" : ");
				}
				else
				{
					caRpt.println(avMoreHeader.elementAt(i).toString());
				}
			}
		}
		caRpt.blankLines(2);
		caRpt.print("SUMMARY TOTALS BY CREDIT CARD TYPE");
		caRpt.blankLines(1);
		caRpt.drawDashedLine();
	}

	/**
	 * Get Formatted Report
	 * 
	 * @return StringBuffer
	 */
	public StringBuffer getFormattedReport()
	{
		return caRpt.getReport();
	}

	/**
	 * Returns the available lines within the page that can be written
	 * onto
	 * 
	 * @return int
	 */
	public int getNoOfDetailLines()
	{
		return caRptProps.getPageHeight() - caRpt.getCurrX() - 2;
	}

	/**
	 * Print patch code for salvage.
	 * Starting point will always be at column postion 49 because of
	 * the microfiche reader.
	 */
	public void printPatchCode()
	{
		this.caRpt.print("", START_PT49, LENGTH1);
		this.caRpt.print(Print.getPatchCode()); // verify this later
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public abstract Vector queryData(String asQuery);
}
