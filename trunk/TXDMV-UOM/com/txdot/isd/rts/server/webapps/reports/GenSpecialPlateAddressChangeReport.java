package com.txdot.isd.rts.server.webapps.reports;

import java.util.Vector;

import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;

import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;

/*
 * GenSpecialPlateAddressChangeReport.java
 * 
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Bob Brown	08/20/2003	Filled class with variables,
 *                          and added body to methods .
 * 							defect 4885 
 * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog.
 * 							Format, Hungarian notation for variables. 
 * 							defect 7889 Ver 5.2.3 
 * K Harrell	02/06/2009	deprecated 
 * 							defect 9941 Ver Defect_POS_D 
 *----------------------------------------------------------------------
 */

/**
 * Generate Special Plate Address Change Report
 *  
 * @version	Defect_POS_D	02/06/2009 
 * @author	Bob Brown  
 * @deprecated
 * <br>Creation Date:	unknown
 */
public class GenSpecialPlateAddressChangeReport extends ReportTemplate
{
	private int PLATENO_STARTPT = 1;
	private int PLATENO_LENGTH = 7;
	private String PLATENO_HEADER = "PLATE";

	private int DOCNO_STARTPT = 9;
	private int DOCNO_LENGTH = 17;
	private String DOCNO_HEADER = "DOCUMENT NUMBER";

	private int VIN_STARTPT = 28;
	private int VIN_LENGTH = 22;
	private String VIN_HEADER = "VIN";

	private int NAME_STARTPT = 50;
	private int NAME_LENGTH = 28;
	private String NAME_HEADER = "OWNER NAME";

	private int ST1_STARTPT = 79;
	private int ST1_LENGTH = 30;
	private int CITY_STATE_STARTPT = 79;
	private int CITY_STATE_LENGTH = 25;

	private int ST2_STARTPT = 110;
	private int ST2_LENGTH = 9;
	private int ZIPCD_STARTPT = 105;
	private int ZIPCD_LENGTH = 10;

	private String ST1_HEADER = "ADDRESS";

	private int COUNTY_STARTPT = 120;
	private int COUNTY_LENGTH = 15;
	private String COUNTY_HEADER = "COUNTY NAME";

	private String csDateRange;

	private int LINES_TWO = 2;
	private int LINES_ONE = 1;
	//private Vector lvKeyData = null;
	// CQU100003811 this was 2
	private int END_OF_PAGE_WHITESPACE = 2;

	DatabaseAccess caDBAccess;

	/**
	 * GenSpecialPlateAddressChangeReport constructor
	 */
	public GenSpecialPlateAddressChangeReport()
	{
		super();
	}

	/**
	 * GenSpecialPlateAddressChangeReport constructor
	 * 
	 * @param String asRptName
	 * @param ReportProperties aaRptProps
	 * @param String asDateRange
	 */
	public GenSpecialPlateAddressChangeReport(
		String asRptName,
		ReportProperties aaRptProps,
		String asDateRange)
	{
		super(asRptName, aaRptProps);

		csDateRange = asDateRange;
	}

	/**
	 * Format Report
	 * 
	 * @param Vector avResults
	 */
	public void formatReport(Vector avResults)
	{

		Vector lvHeader = new Vector();

		// vector to contain additional heading information
		// no additional heading information needed.  empty vector

		Vector lvTable = new Vector();
		// vector to contain rows of column headings
		Vector lvRow1 = new Vector();
		// vector for the first row of column headings
		// there is only one row.

		// column header
		ColumnHeader laColumn1 =
			new ColumnHeader(
				PLATENO_HEADER,
				PLATENO_STARTPT,
				PLATENO_LENGTH);

		// column header
		ColumnHeader laColumn2 =
			new ColumnHeader(DOCNO_HEADER, DOCNO_STARTPT, DOCNO_LENGTH);

		ColumnHeader laColumn3 =
			new ColumnHeader(VIN_HEADER, VIN_STARTPT, VIN_LENGTH);

		ColumnHeader laColumn4 =
			new ColumnHeader(NAME_HEADER, NAME_STARTPT, NAME_LENGTH);

		ColumnHeader laColumn5 =
			new ColumnHeader(ST1_HEADER, ST1_STARTPT, ST1_LENGTH);

		ColumnHeader laColumn6 =
			new ColumnHeader(
				COUNTY_HEADER,
				COUNTY_STARTPT,
				COUNTY_LENGTH);

		lvRow1.addElement(laColumn1);
		lvRow1.addElement(laColumn2);
		lvRow1.addElement(laColumn3);
		lvRow1.addElement(laColumn4);
		lvRow1.addElement(laColumn5);
		lvRow1.addElement(laColumn6);

		lvTable.addElement(lvRow1);

		lvHeader.add("TRANSACTIONS FROM:    ");
		lvHeader.add(csDateRange);

		if ((avResults == null) || (avResults.size() <= 0))
		{
			generateHeader(lvHeader, lvTable);
			generateNoRecordFoundMsg();
			generateFooter();

			return;
		}

		generateHeader(lvHeader, lvTable);

		//	int numChanges = 0; 	// keep track of total changes
		int i = 0; // i will be used to get each object.

		while (i < avResults.size())
		{
			int j = getRemainingLinesAndHandleFooter();

			// Output available number of lines data to the page.
			for (int k = 0; k <= j; k++)
			{
				if (i < avResults.size())
				{
					SpecialPlateAddressChangeReportData laDataline =
						(
							SpecialPlateAddressChangeReportData) avResults
								.elementAt(
							i);
					//numChanges += dataline.getNumChanges();
					this.caRpt.print(
						laDataline.getPltNo(),
						PLATENO_STARTPT,
						PLATENO_LENGTH);
					this.caRpt.print(
						laDataline.getDocNo(),
						DOCNO_STARTPT,
						DOCNO_LENGTH);
					this.caRpt.print(
						laDataline.getVin(),
						VIN_STARTPT,
						VIN_LENGTH);
					this.caRpt.print(
						laDataline.getOwnerName(),
						NAME_STARTPT,
						NAME_LENGTH);
					AddressData lAD =
						(AddressData) laDataline.getAddress();
					this.caRpt.print(
						lAD.getSt1(),
						ST1_STARTPT,
						ST1_LENGTH);
					this.caRpt.print(
						lAD.getSt2(),
						ST2_STARTPT,
						ST2_LENGTH);
					this.caRpt.print(
						laDataline.getCountyName(),
						COUNTY_STARTPT,
						COUNTY_LENGTH);
					// this.cRpt.print(String.valueOf(dataline.getNumChanges()), NUMCHG_STARTPT, NUMCHG_LENGTH);
					// this.cRpt.rightAlign(String.valueOf(dataline.getNumChanges()), NUMCHG_STARTPT, NUMCHG_LENGTH);
					this.caRpt.nextLine();
					this.caRpt.print(
						lAD.getCity() + ", " + lAD.getState(),
						CITY_STATE_STARTPT,
						CITY_STATE_LENGTH);
					if (lAD.getZpcdp4().length() < 4)
						this.caRpt.print(
							lAD.getZpcd(),
							ZIPCD_STARTPT,
							ZIPCD_LENGTH);
					else
						this.caRpt.print(
							lAD.getZpcd() + "-" + lAD.getZpcdp4(),
							ZIPCD_STARTPT,
							ZIPCD_LENGTH);
					this.caRpt.blankLines(2);
					i += 1;
					k += 1;

				}
			}
		}
		getRemainingLinesAndHandleFooter();
		this.caRpt.nextLine();
		getRemainingLinesAndHandleFooter();
		this.caRpt.nextLine();
		getRemainingLinesAndHandleFooter();
		this.caRpt.center("TOTAL ADDRESS CHANGES: " + i);

		generateFooter();
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
	 * Get Remaining Lines And Handle Footer
	 * 
	 * @return int
	 */
	protected int getRemainingLinesAndHandleFooter()
	{
		int j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;

		if (j <= 0)
		{
			generateFooter();
			j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;
		}
		return j;
	}
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
	 * Query Data
	 *  
	 * @return Vector
	 * @param String asQuery
	 */
	public Vector queryData(String asQuery)
	{

		return null;
	}
}
