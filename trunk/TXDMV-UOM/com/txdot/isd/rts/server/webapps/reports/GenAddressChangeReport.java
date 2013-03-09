package com.txdot.isd.rts.server.webapps.reports;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Log;

/*
 * GenAddressChangeReport.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	05/13/2002	correct page handling on report.
 *							CQU100003811
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3
 * K Harrell	08/14/2009	Implement new generateFooter(boolean) 
 * 							delete LINES_TWO, LINES_ONE  
 * 							modify formatReport()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	08/14/2009	2nd page has no Report Heading, Column 
 * 							 Heading. Additional Cleanup. 
 * 							add cvHeader, cvTable
 * 							modify formatReport(), 
 * 							 	getRemainingLinesAndHandleFooter()
 * 							defect 10101 Ver Defect_POS_F
 * K Harrell	02/27/2010	deprecate GenAddressChangeReport 
 * 							defect 10387 Ver POS_640   
 *----------------------------------------------------------------------
 */

/**
 * Gen Address Change Report
 *  
 * @version	POS_640			02/27/2010	
 * @author	Administrator
 * <br>Creation Date:		10/17/01 16:04:02
 * @deprecated 
 */
public class GenAddressChangeReport extends ReportTemplate
{

	private Vector cvKeyData = null;
	
	// defect 10101 
	// vector to contain additional heading information
	// no additional heading information needed.  empty vector
	private Vector cvHeader = new Vector();

	// vector to contain rows of column headings	
	private Vector cvTable = new Vector();
	// end defect 10101 

	private final static int CNTYNAME_LENGTH = 50;
	private final static int CNTYNAME_STARTPT = 17;
	private final static int COL1_LENGTH = 5;
	private final static int COL1_STARTPT = 6;
	private final static int END_OF_PAGE_WHITESPACE = 2;
	private final static int NUMCHG_LENGTH = 30;
	private final static int NUMCHG_STARTPT = 60;
		
	private final static String CNTYNAME_HEADER = "COUNTY";
	private final static String COL1_HEADER = "";
	private final static String NUMCHG_HEADER = "ADDRESS CHANGES";
	

	/**
	 * Starts the application.
	 * 
	 * @param args an array of command-line arguments
	 */
	public static void main(java.lang.String[] args)
	{
		// empty code block
	}

	/**
	 * GenAddressChangeReport constructor comment.
	 */
	public GenAddressChangeReport()
	{
		super();
	}

	/**
	 * GenAddressChangeReport constructor comment.
	 * 
	 * @param asRptName String 
	 * @param aaRptProps ReportProperties
	 */
	public GenAddressChangeReport(
		String asRptName,
		ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * GenAddressChangeReport constructor comment.
	 * 
	 * @param asRptName java.lang.String
	 * @param aaRptProps com.txdot.isd.rts.services.reports.ReportProperties
	 * @param avKeyData Vector
	 */
	public GenAddressChangeReport(
		String asRptName,
		ReportProperties aaRptProps,
		Vector avKeyData)
	{
		super(asRptName, aaRptProps);
		cvKeyData = avKeyData;
	}

	/**
	 * Format Report
	 * 
	 * @param Vector avResults
	 */
	public void formatReport(Vector avResults)
	{
//		// vector for the first row of column headings
//		// there is only one row.
//		Vector lvRow1 = new Vector();
//
//		// column header
//		ColumnHeader laColumn1 =
//			new ColumnHeader(COL1_HEADER, COL1_STARTPT, COL1_LENGTH);
//
//		// column header
//		ColumnHeader laColumn2 =
//			new ColumnHeader(
//				CNTYNAME_HEADER,
//				CNTYNAME_STARTPT,
//				CNTYNAME_LENGTH);
//
//		// column header (starting point +22 ==> since the real number is right aligned)
//		ColumnHeader laColumn3 =
//			new ColumnHeader(
//				NUMCHG_HEADER,
//				NUMCHG_STARTPT + 22,
//				NUMCHG_LENGTH);
//
//		lvRow1.addElement(laColumn1);
//		lvRow1.addElement(laColumn2);
//		lvRow1.addElement(laColumn3);
//		cvTable.addElement(lvRow1);
//
//		// obtain additional key data and add into header
//		if (cvKeyData != null && cvKeyData.size() > 0)
//		{
//			Object laObj = cvKeyData.get(0);
//
//			if (laObj instanceof String)
//			{
//				String lsHdr = (String) laObj;
//				cvHeader.add("TRANSACTIONS FROM:    ");
//				cvHeader.add(lsHdr);
//			}
//		}
//
//		// defect 10101 
//		generateHeader(cvHeader, cvTable);
//		// end defect 10101 
//
//		if (avResults == null || avResults.size() == 0)
//		{
//			generateNoRecordFoundMsg();
//			// defect 8628
//			// Implement ". . . END OF REPORT . . ." w/in footer
//			generateFooter(true);
//			// end defect 8628 
//		}
//		else
//		{
//			int liNumChanges = 0; // keep track of total changes
//			int i = 0; // i will be used to get each object.
//
//			while (i < avResults.size())
//			{
//				int j =
//					getRemainingLinesAndHandleFooter();
//
//				// Output available number of lines data to the page.
//				for (int k = 0; k <= j; k++)
//				{
//					if (i < avResults.size())
//					{
//						AddressChangeReportData laDataline =
//							(
//								AddressChangeReportData) avResults
//									.elementAt(
//								i);
//						liNumChanges += laDataline.getNumChanges();
//
//						this.caRpt.print(
//							laDataline.getCountyName(),
//							CNTYNAME_STARTPT,
//							CNTYNAME_LENGTH);
//						this.caRpt.rightAlign(
//							String.valueOf(laDataline.getNumChanges()),
//							NUMCHG_STARTPT,
//							NUMCHG_LENGTH);
//						this.caRpt.nextLine();
//						i = i + 1;
//					}
//				}
//			}
//			getRemainingLinesAndHandleFooter();
//			this.caRpt.nextLine();
//
//			getRemainingLinesAndHandleFooter();
//			this.caRpt.nextLine();
//
//			getRemainingLinesAndHandleFooter();
//			this.caRpt.center("TOTAL ADDRESS CHANGES: " + liNumChanges);
//			
//			// defect 8628 
//			generateFooter(true);
//			// end defect 8628 
//		}
	}

	/**
	 * Generate Attributes
	 * This abstract method must be implemented in all subclasses
	 * 
	 */
	public void generateAttributes()
	{
		this.caRpt.printAttributes(
			"THIS NEEDS TO BE REPLACED WITH PRINT STRING");
	}

	/**
	 * Get Remaining Lines.  Generate Footer, new Header as required.
	 * 
	 * @return int
	 */
	protected int getRemainingLinesAndHandleFooter()
	{
		int j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;

		if (j <= 0)
		{
			generateFooter();
			
			// defect 10101
			generateHeader(cvHeader, cvTable);
			// end defect 10101 
			
			j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;
		}
		return j;
	}

	/**
	 * Run query and return results.
	 * 
	 * @param String asQuery 
	 */
	public Vector queryData(String asQuery)
	{
		return new Vector(); 
//		StringBuffer lsQry = new StringBuffer();
//		Vector lvRslt = new Vector();
//		ResultSet laQry;
//
//		lsQry.append(
//			"SELECT b.ofcName, a.ItrntTimeStmp, count(*) as cnt "
//				+ "FROM RTS.RTS_ITRNT_TRANS a, RTS.RTS_OFFICE_IDS b "
//				+ "WHERE a.ofcIssuanceNo = b.ofcIssuanceNo AND a.transCd = 'IADDR' "
//				+ "GROUP BY b.ofcName, a.ItrntTimeStmp");
//
//		try
//		{
//
//			DatabaseAccess laDBAccess = new DatabaseAccess();
//			laDBAccess.beginTransaction();
//			laQry = laDBAccess.executeDBQuery(lsQry.toString());
//
//			while (laQry.next())
//			{
//				AddressChangeReportData laAddrChangeRptData =
//					new AddressChangeReportData();
//				laAddrChangeRptData.setCountyName(
//					laDBAccess.getStringFromDB(laQry, "ofcName"));
//				laAddrChangeRptData.setTimeStamp(
//					laDBAccess.getRTSDateFromDB(
//						laQry,
//						"ItrntTimeStmp"));
//				laAddrChangeRptData.setNumChanges(
//					laDBAccess.getIntFromDB(laQry, "cnt"));
//
//				// Add element to the Vector
//				lvRslt.addElement(laAddrChangeRptData);
//			}
//
//			laQry.close();
//			laQry = null;
//			laDBAccess.endTransaction(DatabaseAccess.NONE);
//
//			return (lvRslt);
//
//		}
//		catch (SQLException leSQLEx)
//		{
//			Log.write(
//				Log.SQL_EXCP,
//				this,
//				"ERROR: Address Change Report: "
//					+ leSQLEx.getMessage());
//			return null;
//		}
//		catch (RTSException leRTSEx)
//		{
//			Log.write(
//				Log.SQL_EXCP,
//				this,
//				"ERROR: Address Change Report: "
//					+ leRTSEx.getMessage());
//			return null;
//		}

	}
}
