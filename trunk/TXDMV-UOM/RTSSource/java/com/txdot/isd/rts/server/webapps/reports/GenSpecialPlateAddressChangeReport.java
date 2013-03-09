package com.txdot.isd.rts.server.webapps.reports;import java.util.Vector;import com.txdot.isd.rts.services.data.AddressData;import com.txdot.isd.rts.services.reports.ColumnHeader;import com.txdot.isd.rts.services.reports.ReportProperties;import com.txdot.isd.rts.services.reports.ReportTemplate;import com.txdot.isd.rts.server.dataaccess.DatabaseAccess;/* * GenSpecialPlateAddressChangeReport.java *  * (c) Texas Department of Transportation  2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Bob Brown	08/20/2003	Filled class with variables, *                          and added body to methods . * 							defect 4885  * B Hargrove	05/04/2005	chg '/**' to '/*' to begin prolog. * 							Format, Hungarian notation for variables.  * 							defect 7889 Ver 5.2.3  * K Harrell	02/06/2009	deprecated  * 							defect 9941 Ver Defect_POS_D  *---------------------------------------------------------------------- *//** * Generate Special Plate Address Change Report *   * @version	Defect_POS_D	02/06/2009  * @author	Bob Brown   * @deprecated * <br>Creation Date:	unknown *//* &GenSpecialPlateAddressChangeReport& */public class GenSpecialPlateAddressChangeReport extends ReportTemplate{/* &GenSpecialPlateAddressChangeReport'PLATENO_STARTPT& */	private int PLATENO_STARTPT = 1;/* &GenSpecialPlateAddressChangeReport'PLATENO_LENGTH& */	private int PLATENO_LENGTH = 7;/* &GenSpecialPlateAddressChangeReport'PLATENO_HEADER& */	private String PLATENO_HEADER = "PLATE";/* &GenSpecialPlateAddressChangeReport'DOCNO_STARTPT& */	private int DOCNO_STARTPT = 9;/* &GenSpecialPlateAddressChangeReport'DOCNO_LENGTH& */	private int DOCNO_LENGTH = 17;/* &GenSpecialPlateAddressChangeReport'DOCNO_HEADER& */	private String DOCNO_HEADER = "DOCUMENT NUMBER";/* &GenSpecialPlateAddressChangeReport'VIN_STARTPT& */	private int VIN_STARTPT = 28;/* &GenSpecialPlateAddressChangeReport'VIN_LENGTH& */	private int VIN_LENGTH = 22;/* &GenSpecialPlateAddressChangeReport'VIN_HEADER& */	private String VIN_HEADER = "VIN";/* &GenSpecialPlateAddressChangeReport'NAME_STARTPT& */	private int NAME_STARTPT = 50;/* &GenSpecialPlateAddressChangeReport'NAME_LENGTH& */	private int NAME_LENGTH = 28;/* &GenSpecialPlateAddressChangeReport'NAME_HEADER& */	private String NAME_HEADER = "OWNER NAME";/* &GenSpecialPlateAddressChangeReport'ST1_STARTPT& */	private int ST1_STARTPT = 79;/* &GenSpecialPlateAddressChangeReport'ST1_LENGTH& */	private int ST1_LENGTH = 30;/* &GenSpecialPlateAddressChangeReport'CITY_STATE_STARTPT& */	private int CITY_STATE_STARTPT = 79;/* &GenSpecialPlateAddressChangeReport'CITY_STATE_LENGTH& */	private int CITY_STATE_LENGTH = 25;/* &GenSpecialPlateAddressChangeReport'ST2_STARTPT& */	private int ST2_STARTPT = 110;/* &GenSpecialPlateAddressChangeReport'ST2_LENGTH& */	private int ST2_LENGTH = 9;/* &GenSpecialPlateAddressChangeReport'ZIPCD_STARTPT& */	private int ZIPCD_STARTPT = 105;/* &GenSpecialPlateAddressChangeReport'ZIPCD_LENGTH& */	private int ZIPCD_LENGTH = 10;/* &GenSpecialPlateAddressChangeReport'ST1_HEADER& */	private String ST1_HEADER = "ADDRESS";/* &GenSpecialPlateAddressChangeReport'COUNTY_STARTPT& */	private int COUNTY_STARTPT = 120;/* &GenSpecialPlateAddressChangeReport'COUNTY_LENGTH& */	private int COUNTY_LENGTH = 15;/* &GenSpecialPlateAddressChangeReport'COUNTY_HEADER& */	private String COUNTY_HEADER = "COUNTY NAME";/* &GenSpecialPlateAddressChangeReport'csDateRange& */	private String csDateRange;/* &GenSpecialPlateAddressChangeReport'LINES_TWO& */	private int LINES_TWO = 2;/* &GenSpecialPlateAddressChangeReport'LINES_ONE& */	private int LINES_ONE = 1;	//private Vector lvKeyData = null;	// CQU100003811 this was 2/* &GenSpecialPlateAddressChangeReport'END_OF_PAGE_WHITESPACE& */	private int END_OF_PAGE_WHITESPACE = 2;/* &GenSpecialPlateAddressChangeReport'caDBAccess& */	DatabaseAccess caDBAccess;	/**	 * GenSpecialPlateAddressChangeReport constructor	 *//* &GenSpecialPlateAddressChangeReport.GenSpecialPlateAddressChangeReport& */	public GenSpecialPlateAddressChangeReport()	{		super();	}	/**	 * GenSpecialPlateAddressChangeReport constructor	 * 	 * @param String asRptName	 * @param ReportProperties aaRptProps	 * @param String asDateRange	 *//* &GenSpecialPlateAddressChangeReport.GenSpecialPlateAddressChangeReport$1& */	public GenSpecialPlateAddressChangeReport(		String asRptName,		ReportProperties aaRptProps,		String asDateRange)	{		super(asRptName, aaRptProps);		csDateRange = asDateRange;	}	/**	 * Format Report	 * 	 * @param Vector avResults	 *//* &GenSpecialPlateAddressChangeReport.formatReport& */	public void formatReport(Vector avResults)	{		Vector lvHeader = new Vector();		// vector to contain additional heading information		// no additional heading information needed.  empty vector		Vector lvTable = new Vector();		// vector to contain rows of column headings		Vector lvRow1 = new Vector();		// vector for the first row of column headings		// there is only one row.		// column header		ColumnHeader laColumn1 =			new ColumnHeader(				PLATENO_HEADER,				PLATENO_STARTPT,				PLATENO_LENGTH);		// column header		ColumnHeader laColumn2 =			new ColumnHeader(DOCNO_HEADER, DOCNO_STARTPT, DOCNO_LENGTH);		ColumnHeader laColumn3 =			new ColumnHeader(VIN_HEADER, VIN_STARTPT, VIN_LENGTH);		ColumnHeader laColumn4 =			new ColumnHeader(NAME_HEADER, NAME_STARTPT, NAME_LENGTH);		ColumnHeader laColumn5 =			new ColumnHeader(ST1_HEADER, ST1_STARTPT, ST1_LENGTH);		ColumnHeader laColumn6 =			new ColumnHeader(				COUNTY_HEADER,				COUNTY_STARTPT,				COUNTY_LENGTH);		lvRow1.addElement(laColumn1);		lvRow1.addElement(laColumn2);		lvRow1.addElement(laColumn3);		lvRow1.addElement(laColumn4);		lvRow1.addElement(laColumn5);		lvRow1.addElement(laColumn6);		lvTable.addElement(lvRow1);		lvHeader.add("TRANSACTIONS FROM:    ");		lvHeader.add(csDateRange);		if ((avResults == null) || (avResults.size() <= 0))		{			generateHeader(lvHeader, lvTable);			generateNoRecordFoundMsg();			generateFooter();			return;		}		generateHeader(lvHeader, lvTable);		//	int numChanges = 0; 	// keep track of total changes		int i = 0; // i will be used to get each object.		while (i < avResults.size())		{			int j = getRemainingLinesAndHandleFooter();			// Output available number of lines data to the page.			for (int k = 0; k <= j; k++)			{				if (i < avResults.size())				{					SpecialPlateAddressChangeReportData laDataline =						(							SpecialPlateAddressChangeReportData) avResults								.elementAt(							i);					//numChanges += dataline.getNumChanges();					this.caRpt.print(						laDataline.getPltNo(),						PLATENO_STARTPT,						PLATENO_LENGTH);					this.caRpt.print(						laDataline.getDocNo(),						DOCNO_STARTPT,						DOCNO_LENGTH);					this.caRpt.print(						laDataline.getVin(),						VIN_STARTPT,						VIN_LENGTH);					this.caRpt.print(						laDataline.getOwnerName(),						NAME_STARTPT,						NAME_LENGTH);					AddressData lAD =						(AddressData) laDataline.getAddress();					this.caRpt.print(						lAD.getSt1(),						ST1_STARTPT,						ST1_LENGTH);					this.caRpt.print(						lAD.getSt2(),						ST2_STARTPT,						ST2_LENGTH);					this.caRpt.print(						laDataline.getCountyName(),						COUNTY_STARTPT,						COUNTY_LENGTH);					// this.cRpt.print(String.valueOf(dataline.getNumChanges()), NUMCHG_STARTPT, NUMCHG_LENGTH);					// this.cRpt.rightAlign(String.valueOf(dataline.getNumChanges()), NUMCHG_STARTPT, NUMCHG_LENGTH);					this.caRpt.nextLine();					this.caRpt.print(						lAD.getCity() + ", " + lAD.getState(),						CITY_STATE_STARTPT,						CITY_STATE_LENGTH);					if (lAD.getZpcdp4().length() < 4)						this.caRpt.print(							lAD.getZpcd(),							ZIPCD_STARTPT,							ZIPCD_LENGTH);					else						this.caRpt.print(							lAD.getZpcd() + "-" + lAD.getZpcdp4(),							ZIPCD_STARTPT,							ZIPCD_LENGTH);					this.caRpt.blankLines(2);					i += 1;					k += 1;				}			}		}		getRemainingLinesAndHandleFooter();		this.caRpt.nextLine();		getRemainingLinesAndHandleFooter();		this.caRpt.nextLine();		getRemainingLinesAndHandleFooter();		this.caRpt.center("TOTAL ADDRESS CHANGES: " + i);		generateFooter();	}	/**	 * Generate Attributes	 * This abstract method must be implemented in all subclasses	 * 	 *//* &GenSpecialPlateAddressChangeReport.generateAttributes& */	public void generateAttributes()	{		this.caRpt.printAttributes(			"THIS NEEDS TO BE REPLACED WITH PRINT STRING");	}	/**	 * Get Remaining Lines And Handle Footer	 * 	 * @return int	 *//* &GenSpecialPlateAddressChangeReport.getRemainingLinesAndHandleFooter& */	protected int getRemainingLinesAndHandleFooter()	{		int j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;		if (j <= 0)		{			generateFooter();			j = getNoOfDetailLines() - END_OF_PAGE_WHITESPACE;		}		return j;	}	/**	 * Starts the application.	 * 	 * @param args an array of command-line arguments	 *//* &GenSpecialPlateAddressChangeReport.main& */	public static void main(java.lang.String[] args)	{		// empty code block	}	/**	 * Query Data	 *  	 * @return Vector	 * @param String asQuery	 *//* &GenSpecialPlateAddressChangeReport.queryData& */	public Vector queryData(String asQuery)	{		return null;	}}/* #GenSpecialPlateAddressChangeReport# */