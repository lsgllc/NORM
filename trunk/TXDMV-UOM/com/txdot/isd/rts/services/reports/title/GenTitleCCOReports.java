package com.txdot.isd.rts.services.reports.title;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CommercialVehicleWeightsCache;
import com.txdot.isd.rts.services.cache.RegistrationClassCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * GenTitleCCOReports.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/01/2001	Methods created
 * J Rue      	09/19/2001	Added comments
 * J Rue		06/27/2002	Defect ####, break up the indi description
 * 							if length > 18 charactors, add menthod
 * 							modifyStrLngth()
 * Ray Rowehl	03/28/2003	Defect 5887.  Line up Body Vin with Vin
 * Jeff S.		11/09/2004	Used <= when refering to starting
 *							position so that everything would
 *							drop down a line.
 *							printVehicleInfo()
 *							printLienhldr2()
 *							printLienhldr1()
 *							defect 7715 Ver 5.2.1
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify getData()
 *							defect 7896 Ver 5.2.3 
 * K Harrell	03/03/2009	Modify to use new Lienholder, new Address 
 * 							Data object features. Streamline coding.
 * 							add caCTData, caMFVehData, caTitleData
 * 							delete TWO, THREE 
 *							 csRunDateFormated, csRunTimeFormated,
 *							 csOwnrTtlName1, csOwnrTtlName2, csOwnrSt1,
 *							 csOwnrSt2, csOwnrCity, csOwnrState,
 *							 csOwnrCntry, csOwnrZipCd, csOwnrZipCdP4,
 *							 ciLienholdrData1,ciLienholdrData2,
 *							 ciLienholdrData3,csLienhd1City,
 *							 csLienhdlName1,csLienhdlName2, csLienhdlSt1,
 *							 csLienhdlSt2, csLienhdlCntry, 
 *							 csLienhdlState, csLienhdlZpCd, 
 *							 csLienhdlZpCdP4, csLienhd2City,
 *							 csLienhd2Name1, csLienhd2Name2,
 *							 csLienhd2St1, csLienhd2St2, csLienhd2Cntry,
 *							 csLienhd2State, csLienhd2ZpCd,
 *							 csLienhd2ZpCdP4, csLienhd3City,
 *							 csLienhd3Name1, csLienhd3Name2,
 *							 csLienhd3St1, csLienhd3St2, csLienhd3Cntry,
 *							 csLienhd3State, csLienhd3ZpCd,
 *							 csLienhd3ZpCdP4, csMailingName,
 *							 csMailingName2, csMailingOwnrSt1,
 *							 csMailingOwnrSt2, csMailingOwnrCity,
 *							 csMailingOwnrState, csMailingOwnrCntry,
 *							 csMailingOwnrZipCd, csMailingOwnrZipCdP4,
 * 							modify printLienhldr1(),
 *							 printLienhldr2(),printLienhldr3(),
 *							 printOwnerInfo(),printPrevOwnrInfo(),
 *							 printTitleMailingNames()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/01/2009	Implement new TitleData
 * 							add NO_LIEN_LINES 
 * 							add printLienhldr() 
 * 							delete INIT_LIEN1_INFO_LINE, 
 * 							 INIT_LIEN2_INFO_LINE, INIT_LIEN3_INFO_LINE
 * 							delete printLienhldr1(), printLienhldr2(),
 * 							 printLienhldr3()
 * 							modify formatReport(), printOwnerInfo(), 
 * 							 printTitleMailingNames(), queryData()  
 * 							defect 10112 Ver Defect_POS_F 
 * Min Wang		02/12/2010	realignment on new Form 30-CCO REV. 6/2009
 * 							vs. 12/1999.
 * 							modify INIT_VEH_INFO_LINE
 * 							defect 10398 Ver POS_640
 * Min Wang 	07/21/2010	Enhance correctly align the printing of CCO 
 * 							in tray three when using the 4015 printer.
 * 							modify CURR_DATE_ADJ_POS, DEFAULT_START_POS,
 * 							DOC_NO_ADJ_POS,  INIT_LIEN_POS, INIT_MAIL_POS,
 * 							INIT_MAILING_ROW, REG_PLT_NO_AND_ADJ_POS,
 * 							TON_DESC_ADJ_POS, TTL_ISSUE_DATE_ADJ_POS ,
 * 							VEH_BDY_TYPE_ADJ_POS, VEH_EMPTY_WT_ADJ_POS,
 * 							INIT_LIEN_POS, VEH_MK_ADJ_POS, VEH_MODL_ADJ_POS,
 * 							VEH_MODL_YR_ADJ_POS, ODOMTR_READING_ADJ_POS 
 * 							defect 10398 Ver 6.5.0
 * Min Wang		06/06/2011	Adjust the printing position of Remark(s) to 
 * 							show " OFF HIGHWAY USE ONLY".
 * 							modify INIT_VEH_RMK_POS
 * 							defect 10880 Ver 6.8.0
 * ---------------------------------------------------------------------
 */
/**
 * CCO prints the Certify Copy of the Original title.
 *
 * @version	6.8.0			06/06/2011
 * @author  Jeff Rue
 * <br>Creation Date:		10/19/2001 08:53:10 
 */
public class GenTitleCCOReports extends ReportTemplate
{
	private int ciDocTypeCd = 0;
	private int ciTtlIssueDate = 0;
	private int ciVehEmptyWt = 0;
	private int ciVehModlYr = 0;
	private String csCommVehWts = "";

	private String csDocNo = "";
	private String csOdomtrReqd = "";
	private String csPrevOwnrCity = "";
	private String csPrevOwnrName = "";
	private String csPrevOwnrState = "";

	private String csRegPltNo = "";

	private String csVehBdyType = "";
	private String csVehBdyVIN = "";
	private String csVehMk = "";
	private String csVehModl = "";
	private String csVehodmtrBrnd = "";
	private String csVehodmtrReadng = "";

	private String csVin = "";
	private Vector cvIndiDescData;

	private Vector cvRemarks = new Vector();

	// defect 9969 
	private CompleteTransactionData caCTData = null;
	private MFVehicleData caMFVehData = null;
	private TitleData caTitleData = null;
	// end defect 9969 
	private static final int CHAR_LENGTH_02 = 2;
	private static final int CHAR_LENGTH_04 = 4;
	private static final int CHAR_LENGTH_06 = 6;
	private static final int CHAR_LENGTH_10 = 10;
	private static final int CHAR_LENGTH_17 = 17;
	private static final int CHAR_LENGTH_30 = 30;
	private static final String COMMA = ", ";
	// defect 10398
	private static final int CURR_DATE_ADJ_POS = 42;//49;
	private static final int DEFAULT_START_POS = 6;//13;
	private static final int DOC_NO_ADJ_POS = 33;//40;
	// end defect 10398
	private static final String EXEMPT_TEXT = "EXEMPT";
	// defect 10398
	private static final int INIT_LIEN_POS = 17;//24;
	// end defect 10398
	private static final int INIT_LIEN_INFO_START_LINE = 54;
	// defect 10112 
	//	private static final int INIT_LIEN1_INFO_LINE = 54;
	//private static final int INIT_LIEN2_INFO_LINE = 61;
	//private static final int INIT_LIEN3_INFO_LINE = 68;
	// end defect 10112 
	// defect 10398
	private static final int INIT_MAIL_POS = 9;//16;
	// end defect 10398
	private static final int INIT_MAILING_ROW = 9;
	// defect 10398
	//private static final int INIT_VEH_INFO_LINE = 29;
	private static final int INIT_VEH_INFO_LINE = 28;
	// defect 10880
	//private static final int INIT_VEH_RMK_POS = 47;//51;
	private static final int INIT_VEH_RMK_POS = 43;
	// end defect 10880
	// end defect 10398
	private static final String ISSUED_TEXT = "ISSUED **";
	private static final int MAX_STR_LENGTH = 18;
	private static final String NEGOTIABLE_TEXT = "** NEGOTIABLE";
	// defect 10112 
	private static final int NO_LIEN_LINES = 7; 
	// end defect 10112 
	private static final String NO_MILEAGE_BRAND_TEXT =
		"NO MILEAGE BRAND";
	private static final String OFF_HIGHWAY_USE_TEXT =
		"OFF HIGHWAY USE ONLY";
	private static final String ONE = "1";
	private static final String PURPOSE_ONLY_TEXT = "PURPOSE ONLY";
	// defect 10398
	private static final int REG_PLT_NO_AND_ADJ_POS = 33;//40;
	// end defect 10398
	private static final String REGISTRATION_TEXT = "REGISTRATION";
	private static final int START_POS = 1;
	private static final String TITLE_NOT_TEXT = "TITLE NOT";
	// defect 10398
	private static final int TON_DESC_ADJ_POS = 20;//27;
	private static final int TTL_ISSUE_DATE_ADJ_POS = 52;//59;
	private static final int VEH_BDY_TYPE_ADJ_POS = 52;//59;
	private static final int VEH_EMPTY_WT_ADJ_POS = 27;//34;
	private static final int VEH_MK_ADJ_POS = 39;//46;
	private static final int VEH_MODL_ADJ_POS = 10;//17;
	private static final int VEH_MODL_YR_ADJ_POS = 28;//35;
	private static final int VEH_ODOMTR_READING_ADJ_POS = 56;//63;
	// end defect 10398
	private static final int VIN_ADJ_NO = 12;

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs an array of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps = new ReportProperties("");
		GenTitleCCOReports laGPR =
			new GenTitleCCOReports("", laRptProps);

		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lvQueryCCOorCCDO = laGPR.queryData(lsQuery);
		laGPR.formatReport(lvQueryCCOorCCDO);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\RTS\\RPT\\CCO.RPT");
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
				new FrmPreviewReport("c:\\RTS\\RPT\\CCO.RPT");
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
	 * GenTitleCCOReports
	 */
	public GenTitleCCOReports()
	{
		super();
	}

	/**
	 * GenTitleCCOReports
	 * 
	 * @param asRptString String
	 * @param asRptProperties ReportProperties
	 */
	public GenTitleCCOReports(
		String asRptString,
		ReportProperties asRptProperties)
	{
		super(asRptString, asRptProperties);
	}

	/**
	 * formatReports calls each section of the CCO print.
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		getData(avResults);
		printTitleMailingNames();
		generateRemarks();
		printVehicleInfo();
		printPrevOwnrInfo();
		printOdomtrReading();
		printOwnerInfo();
		// defect 10112 
		printLienhldr();
		// end defect 10112 
	}

	/**
	 * generateAttributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Print the remarks, based of DocTypeCd.
	 */
	private void generateRemarks()
	{
		String lsIndiDesc = "";

		switch (ciDocTypeCd)
		{
			case 5 :
				{
					cvRemarks.add(REGISTRATION_TEXT);
					cvRemarks.add(PURPOSE_ONLY_TEXT);
					cvRemarks.add(NEGOTIABLE_TEXT);
					cvRemarks.add(TITLE_NOT_TEXT);
					cvRemarks.add(ISSUED_TEXT);
					break;
				}
			default :
				{
					if (csVehodmtrBrnd.equals("")
						&& (!(csVehodmtrReadng.equals(EXEMPT_TEXT))))
					{
						if (csOdomtrReqd.equals(ONE))
						{
							String lsVehodmtrBrnd =
								NO_MILEAGE_BRAND_TEXT;
							cvRemarks.add(lsVehodmtrBrnd);
						}
					}
					switch (ciDocTypeCd)
					{
						case 2 :
							{
								cvRemarks.add(OFF_HIGHWAY_USE_TEXT);
							}
					}
				}
		}

		// Print remarks from the indi table
		if (cvIndiDescData.size() > 0)
		{
			lsIndiDesc = (String) cvIndiDescData.elementAt(0);
			int liNumOfIndiElements = cvIndiDescData.size();
			if (!(lsIndiDesc == null || lsIndiDesc.equals("")))
			{
				for (int i = 0; i < liNumOfIndiElements; i++)
				{
					lsIndiDesc = (String) cvIndiDescData.elementAt(i);
					String lsRtn = modifyStrLngth(lsIndiDesc);
					cvRemarks.add(lsRtn);
				}
			}
		}
	}

	/**
	 * Get the data and assign to the class variables.
	 * 
	 * @param avResults Vector
	 */
	private void getData(Vector avResults)
	{
		// Define data objects for MFVehicle Data objects
		caCTData = (CompleteTransactionData) avResults.get(0);
		caMFVehData = caCTData.getVehicleInfo();
		caTitleData = caMFVehData.getTitleData();
		RegistrationData laRegisData = caMFVehData.getRegData();
		VehicleData laVehData = caMFVehData.getVehicleData();

		csVin = laVehData.getVin(); // VIN
		ciVehModlYr = laVehData.getVehModlYr(); //  model year
		csVehModl = laVehData.getVehModl(); //  model
		csVehMk = laVehData.getVehMk(); //  make
		csVehBdyType = laVehData.getVehBdyType(); //  body type
		csVehBdyVIN = laVehData.getVehBdyVin(); //  body VIN
		ciVehEmptyWt = laVehData.getVehEmptyWt(); //  empty wieght
		csVehodmtrReadng = laVehData.getVehOdmtrReadng();

		csVehodmtrBrnd = laVehData.getVehOdmtrBrnd(); // Odometer Brand

		csDocNo = caTitleData.getDocNo(); // Document Number
		ciDocTypeCd = caTitleData.getDocTypeCd(); // Doc Type Code
		ciTtlIssueDate = caTitleData.getTtlIssueDate();
		csPrevOwnrName = caTitleData.getPrevOwnrName();
		csPrevOwnrCity = caTitleData.getPrevOwnrCity();
		csPrevOwnrState = caTitleData.getPrevOwnrState();
		csRegPltNo = laRegisData.getRegPltNo();

		// Read data for Commercial Weights data Object
		CommercialVehicleWeightsData laCVDO =
			CommercialVehicleWeightsCache.getCommVehWts(
				laVehData.getVehTon());

		if (laCVDO != null)
		{
			csCommVehWts = laCVDO.getVehTonDesc();
		}
		else
		{
			csCommVehWts = "";
		}

		RegistrationClassData laRCD =
			RegistrationClassCache.getRegisClass(
				laVehData.getVehClassCd(),
				laRegisData.getRegClassCd(),
				RTSDate.getCurrentDate().getYYYYMMDDDate());

		if (laRCD != null)
		{
			csOdomtrReqd = String.valueOf(laRCD.getOdmtrReqd());
		}
		// 1 or 0
		else
		{
			csOdomtrReqd = "0";
		}
		cvIndiDescData = new Vector();
		Vector lvIndicators =
			IndicatorLookup.getIndicators(caMFVehData, "CCO", 2);

		for (int i = 0; i < lvIndicators.size(); i++)
		{
			IndicatorData laData = (IndicatorData) lvIndicators.get(i);
			cvIndiDescData.add(laData.getDesc());
		}
		// ********** END RECORDS FROM THE INPUT  VECTOR   *************
	}

	/**
	 * Modify indi desciption string to a maximun length of 18 charactors
	 * 
	 * @param asInpStr String
	 * @return String
	 */
	public String modifyStrLngth(String asInpStr)
	{
		String lsCont = asInpStr;

		if (asInpStr.length() > MAX_STR_LENGTH)
		{
			StringTokenizer laST = new StringTokenizer(asInpStr);

			// Get first token add to concantanated string
			lsCont = laST.nextToken();

			while (laST.hasMoreTokens())
			{
				if (lsCont.length() + 3 < MAX_STR_LENGTH)
				{
					lsCont = lsCont + " " + laST.nextToken();
				}
				else
				{
					cvRemarks.add(lsCont);
					lsCont = caRpt.blankSpaces(2) + laST.nextToken();
				}

			}
		}
		return lsCont;
	}

	/**
	 * Print Lienholder data 
	 */
	private void printLienhldr()
	{
		for (int j = 1; j <= 3; j++)
		{
			LienholderData laLienData =
				caTitleData.getLienholderData(new Integer(j));

			if (laLienData.isPopulated())
			{
				int liCurrRow = 0;

				// Set print pointer to the print line for lienholder1 info
				liCurrRow = caRpt.getCurrX();
				int liInitLine =
					INIT_LIEN_INFO_START_LINE + (j - 1) * NO_LIEN_LINES;

				for (int i = liCurrRow; i <= liInitLine; i++)
				{
					caRpt.blankLines(1);
				}

				// Print Lien Date 
				caRpt.print(
					new RTSDate(
						RTSDate.YYYYMMDD,
						laLienData.getLienDate())
						.toString(),
					DEFAULT_START_POS,
					CHAR_LENGTH_10);

				Vector lvLienhldr = laLienData.getNameAddressVector();

				for (int i = 0; i < lvLienhldr.size(); i++)
				{
					String lsData = (String) lvLienhldr.elementAt(i);
					caRpt.print(lsData, INIT_LIEN_POS, lsData.length());
					caRpt.blankLines(1);
				}
			}
		}
	}

	//	/**
	//	 * Print Lienholder data 1.
	//	 */
	//	private void printLienhldr1()
	//	{
	//		// Define local variables
	//		int liCurrRow = 0;
	//
	//		// Set print pointer to the print line for lienholder1 info
	//		liCurrRow = caRpt.getCurrX();
	//
	//		// defect 9969 
	//		// Major Cleanup 
	//
	//		// defect 7715
	//		// Used <= instead of < so that everything would drop a line
	//		for (int i = liCurrRow; i <= INIT_LIEN1_INFO_LINE; i++)
	//			// end defect 7715
	//		{
	//			caRpt.blankLines(1);
	//		}
	//
	//		LienholderData laLienData =
	//			caTitleData.getLienholderData(TitleConstant.LIENHLDR1);
	//
	//		// Print Lien Date 
	//		caRpt.print(
	//			new RTSDate(RTSDate.YYYYMMDD, laLienData.getLienDate())
	//				.toString(),
	//			DEFAULT_START_POS,
	//			CHAR_LENGTH_10);
	//
	//		// defect 10112 
	//		Vector lvLienhldr1 =
	//			caTitleData
	//				.getLienholderData(TitleConstant.LIENHLDR1)
	//				.getNameAddressVector();
	//		// end defect 10112 
	//
	//		for (int i = 0; i < lvLienhldr1.size(); i++)
	//		{
	//			String lsData = (String) lvLienhldr1.elementAt(i);
	//			caRpt.print(lsData, INIT_LIEN_POS, lsData.length());
	//			caRpt.blankLines(1);
	//		}
	//		// end defect 9969 
	//	}
	//
	//	/**
	//	 * Print Lienholder data 2.
	//	 */
	//	private void printLienhldr2()
	//	{
	//		int liCurrRow = 0;
	//
	//		// Set print pointer to the print line for lienholder1 info
	//		liCurrRow = caRpt.getCurrX();
	//
	//		// defect 9969 
	//		// Major Cleanup 
	//
	//		// defect 7715
	//		// Used <= instead of < so that everythign would drop a line
	//		for (int i = liCurrRow; i <= INIT_LIEN2_INFO_LINE; i++)
	//			// end defect 7715
	//		{
	//			caRpt.blankLines(1);
	//		}
	//
	//		// Print Lien 2 Info
	//		LienholderData laLienData =
	//			caTitleData.getLienholderData(TitleConstant.LIENHLDR2);
	//
	//		// Print Lien Date 
	//		caRpt.print(
	//			new RTSDate(RTSDate.YYYYMMDD, laLienData.getLienDate())
	//				.toString(),
	//			DEFAULT_START_POS,
	//			CHAR_LENGTH_10);
	//
	//		// defect 10112 
	//		Vector lvLienhldr2 = laLienData.getNameAddressVector();
	//		// end defect 10112 
	//
	//		for (int i = 0; i < lvLienhldr2.size(); i++)
	//		{
	//			String lsData = (String) lvLienhldr2.elementAt(i);
	//			caRpt.print(lsData, INIT_LIEN_POS, lsData.length());
	//			caRpt.blankLines(1);
	//		}
	//		// end defect 9969 
	//	}
	//
	//	/**
	//	 * Print Lienholder data 3.
	//	 */
	//	private void printLienhldr3()
	//	{
	//		int liCurrRow = 0;
	//
	//		// Set print pointer to the print line for lienholder3 info
	//		liCurrRow = caRpt.getCurrX();
	//
	//		// defect 9969 
	//		for (int i = liCurrRow; i < INIT_LIEN3_INFO_LINE; i++)
	//		{
	//			caRpt.blankLines(1);
	//		}
	//
	//		// Print Lien 3 Info
	//		LienholderData laLienData =
	//			caTitleData.getLienholderData(TitleConstant.LIENHLDR3);
	//
	//		// Print Lien Date  
	//		caRpt.print(
	//			new RTSDate(RTSDate.YYYYMMDD, laLienData.getLienDate())
	//				.toString(),
	//			DEFAULT_START_POS,
	//			CHAR_LENGTH_10);
	//
	//		// defect 10112 
	//		Vector lvLienhldr3 = laLienData.getNameAddressVector();
	//		// end defect 10112 
	//
	//		for (int i = 0; i < lvLienhldr3.size(); i++)
	//		{
	//			String lsData = (String) lvLienhldr3.elementAt(i);
	//			caRpt.print(lsData, INIT_LIEN_POS, lsData.length());
	//			caRpt.blankLines(1);
	//		}
	//		// end defect 9969 
	//	}

	/**
	 * Print the vehicle odometer reading.
	 */
	private void printOdomtrReading()
	{
		// Define local variables
		int liOdomtrReadngCol = 0;

		// Print VIN
		liOdomtrReadngCol = START_POS + VEH_ODOMTR_READING_ADJ_POS;
		caRpt.print(
			csVehodmtrReadng,
			liOdomtrReadngCol,
			CHAR_LENGTH_06);

		// Move down 2 lines
		caRpt.blankLines(2);
	}

	/**
	 * Print title owner data.
	 */
	private void printOwnerInfo()
	{
		// Define local variables
		int liCounter = 0;

		// defect 10112 
		// defect 9969 
		// Major Cleanup 
		OwnerData laOwnrData = caMFVehData.getOwnerData();
		AddressData laOwnrAddrData = laOwnrData.getAddressData();

		// OwnrName1
		caRpt.print(
			laOwnrData.getName1(),
			DEFAULT_START_POS,
			laOwnrData.getName1().length());

		if (cvRemarks != null && cvRemarks.size() > liCounter)
		{
			String lsRmk = (String) cvRemarks.get(liCounter);
			caRpt.print(lsRmk, INIT_VEH_RMK_POS, lsRmk.length());
			liCounter++;
		}
		// OwnrName2 if available
		if (!(laOwnrData.getName2().equals("")))
		{
			caRpt.blankLines(1);
			caRpt.print(
				laOwnrData.getName2(),
				DEFAULT_START_POS,
				laOwnrData.getName2().length());

			if (cvRemarks != null && cvRemarks.size() > liCounter)
			{
				String lsRmk = (String) cvRemarks.get(liCounter);
				caRpt.print(lsRmk, INIT_VEH_RMK_POS, lsRmk.length());
				liCounter++;
			}
		} // end if
		// end defect 10112 

		caRpt.blankLines(1);

		// Print owner street 1
		caRpt.print(
			laOwnrAddrData.getSt1(),
			DEFAULT_START_POS,
			laOwnrAddrData.getSt1().length());

		if (cvRemarks != null && cvRemarks.size() > liCounter)
		{
			String lsRmk = (String) cvRemarks.get(liCounter);
			caRpt.print(lsRmk, INIT_VEH_RMK_POS, lsRmk.length());
			liCounter++;
		}
		// If there is a second owner name
		if (!(laOwnrAddrData.getSt2().equals("")))
		{
			caRpt.blankLines(1);
			caRpt.print(
				laOwnrAddrData.getSt2(),
				DEFAULT_START_POS,
				laOwnrAddrData.getSt2().length());

			if (cvRemarks != null && cvRemarks.size() > liCounter)
			{
				String lsRmk = (String) cvRemarks.get(liCounter);
				caRpt.print(lsRmk, INIT_VEH_RMK_POS, lsRmk.length());
				liCounter++;
			}
		} // end if

		caRpt.blankLines(1);
		caRpt.print(
			laOwnrAddrData.getCityStateCntryZip(),
			DEFAULT_START_POS,
			laOwnrAddrData.getCityStateCntryZip().length());

		// Complete printing of the indiDesc
		while (cvRemarks != null && cvRemarks.size() > liCounter)
		{
			String lsRmk = (String) cvRemarks.get(liCounter);
			caRpt.print(lsRmk, INIT_VEH_RMK_POS, lsRmk.length());
			liCounter++;
			caRpt.blankLines(1);
		}
		// end defect 9969 
	}

	/**
	 * Print previous owner data.
	 */
	private void printPrevOwnrInfo()
	{
		// Define local variables
		String lsPrevOwnrInfo = "";

		// Print Previous Owner info
		lsPrevOwnrInfo =
			csPrevOwnrName
				+ " "
				+ csPrevOwnrCity
				+ COMMA
				+ csPrevOwnrState;
		// defect 9969 
		caRpt.print(
			lsPrevOwnrInfo,
			DEFAULT_START_POS,
			lsPrevOwnrInfo.length());
		// end defect 9969 
	}

	/**
	 * Print the mailing address.
	 */
	private void printTitleMailingNames()
	{
		// Set print pointer to the first line of print
		caRpt.blankLines(INIT_MAILING_ROW);

		// defect 10112 
		// defect 9969
		// Major Cleanup  
		NameAddressData laMailAddrData = caCTData.getMailingAddrData();

		Vector lvMailData = laMailAddrData.getNameAddressVector();

		for (int i = 0; i < lvMailData.size(); i++)
		{
			String lsData = (String) lvMailData.elementAt(i);
			caRpt.print(lsData, INIT_MAIL_POS, CHAR_LENGTH_30);
			caRpt.blankLines(1);
		}
		// end defect 9969 
		// end defect 10112 
	}

	/**
	 * Print vehicle data.
	 */
	private void printVehicleInfo()
	{
		int liCurrRow = 0;
		int liVinCol = 0;
		int liVehModlYrCol = 0;
		int liVehMkCol = 0;
		int liVehBdyTypeCol = 0;
		int liDocNoCol = 0;
		int liTtlIssueDateCol = 0;
		int liVehModlCol = 0;
		int liTonnageDescCol = 0;
		int liVehEmptyWtCol = 0;
		int liRegPltNoCol = 0;
		int liCurrDateCol = 0;

		// Set print pointer to the first line for vehicle info
		liCurrRow = caRpt.getCurrX();

		// defect 7715
		// Used <= instead of < so that everything would drop a line
		for (int i = liCurrRow; i <= INIT_VEH_INFO_LINE; i++)
			// end defect 7715
		{
			caRpt.blankLines(1);
		} // end for loop

		// Print VIN
		int liVehCharLngth = csVin.length();
		float lfVinFloat = ((float) liVehCharLngth) / 2;
		int liVinConv = roundFractionToInt(lfVinFloat, 0);
		liVinCol = START_POS + VIN_ADJ_NO - liVinConv;
 		caRpt.print(csVin, liVinCol + 1, liVehCharLngth);

		// Print vehicle model year
		liVehModlYrCol = START_POS + VEH_MODL_YR_ADJ_POS;
		caRpt.print(
			String.valueOf(ciVehModlYr),
			liVehModlYrCol,
			CHAR_LENGTH_04);

		// Print vehicle make
		int liVehMkCharLngth = csVehMk.length();
		float lfVehMkFloat = ((float) liVehMkCharLngth) / 2;
		int liVehMkConv = roundFractionToInt(lfVehMkFloat, 0);
		liVehMkCol = START_POS + VEH_MK_ADJ_POS - liVehMkConv;
		caRpt.print(csVehMk, liVehMkCol, liVehMkCharLngth);

		// Print vehicle body type
		liVehBdyTypeCol = START_POS + VEH_BDY_TYPE_ADJ_POS;
		caRpt.print(csVehBdyType, liVehBdyTypeCol, CHAR_LENGTH_02);

		// Move down 1 lines
		caRpt.blankLines(2);
		
		// Print vehicle body Vin
		int liVehBdyVinCharLngth = csVehBdyVIN.length();

		caRpt.print(csVehBdyVIN, liVinCol + 9, liVehBdyVinCharLngth);

		// Print Doc Number
		liDocNoCol = START_POS + DOC_NO_ADJ_POS;
		caRpt.print(csDocNo, liDocNoCol, CHAR_LENGTH_17);

		// Print title issued date 
		String lsTtlIssuedDate = "  /  /    ";
		if (ciTtlIssueDate != 0)
		{
			RTSDate laRTSDtTtlIssuedDate =
				new RTSDate(RTSDate.YYYYMMDD, ciTtlIssueDate);
			lsTtlIssuedDate = laRTSDtTtlIssuedDate.toString();
		}
		liTtlIssueDateCol = START_POS + TTL_ISSUE_DATE_ADJ_POS;
		caRpt.print(lsTtlIssuedDate, liTtlIssueDateCol, CHAR_LENGTH_10);

		// Move down 2 lines
		 caRpt.blankLines(3);
		
		// Print vehicle model 
		liVehModlCol = START_POS + VEH_MODL_ADJ_POS;
		caRpt.print(csVehModl, liVehModlCol, CHAR_LENGTH_04);

		// Print tonnage description
		int liTonDescCharLngth = csCommVehWts.length();
		liTonnageDescCol = START_POS + TON_DESC_ADJ_POS;
		caRpt.print(csCommVehWts, liTonnageDescCol, liTonDescCharLngth);

		if (!(ciVehEmptyWt == 0))
		{
			// Print vehicle empty weight
			String lsVehEmpty = String.valueOf(ciVehEmptyWt);
			int liVehEmptyCharLngth = lsVehEmpty.length();
			liVehEmptyWtCol = START_POS + VEH_EMPTY_WT_ADJ_POS;
			caRpt.print(
				lsVehEmpty,
				liVehEmptyWtCol,
				liVehEmptyCharLngth);
		}

		// Print registration plate number
		int liRegPltCharLngth = csRegPltNo.length();
		liRegPltNoCol = START_POS + REG_PLT_NO_AND_ADJ_POS;
		caRpt.print(csRegPltNo, liRegPltNoCol, liRegPltCharLngth);

		// Print current date
		RTSDate laCurrDatew = RTSDate.getCurrentDate();
		liCurrDateCol = START_POS + CURR_DATE_ADJ_POS;
		caRpt.print(
			laCurrDatew.toString(),
			liCurrDateCol,
			CHAR_LENGTH_10);

		// Move down 3 lines
		caRpt.blankLines(3);
 	}

	/**
	 * Set data for standalone
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		// Define local variables
		String lsTonnageDesc = "";
		int liOdomtrReqd = 0;

		Vector lvCCO = new Vector();

		// Define data objects for MFVehicle Data objects
		MFVehicleData laMFVehData = new MFVehicleData();
		OwnerData laOwnrData = new OwnerData();
		RegistrationData laRegisData = new RegistrationData();
		TitleData laTitleData = new TitleData();
		LienholderData laLienHoldData = new LienholderData();
		VehicleData laVehData = new VehicleData();
		// defect 10112 
		NameAddressData laMailAddr = new NameAddressData();
		// end defect 10112 

		CommercialVehicleWeightsData laCommVehWts =
			new CommercialVehicleWeightsData();
		RegistrationClassData laRegisClass =
			new RegistrationClassData();

		Vector lvIndiVector = new Vector();

		// ********** Build  MFVehData  data object **********
		// Set vehicle data
		laVehData.setVehOdmtrReadng("EXEMPT");
		laVehData.setVehOdmtrBrnd("ACTUAL MILEAGE");
		laVehData.setVin("CCL249F484761");
		laVehData.setVehModlYr(1979);
		laVehData.setVehMk("CHEV");
		laVehData.setVehBdyType("PK");
		laVehData.setVehBdyVin("");
		laVehData.setVehModl("C15");
		laVehData.setVehEmptyWt(4500);
		laMFVehData.setVehicleData(laVehData);
		// add Vehicle data object to MFVeh data object

		// Set Title data
		laTitleData.setDocNo("10122136267161454");
		laTitleData.setTtlIssueDate(20011023);
		laTitleData.setPrevOwnrName("HOLLOWEEN SALES");
		laTitleData.setPrevOwnrCity("AUSTIN");
		laTitleData.setPrevOwnrState("TX");
		laTitleData.setDocTypeCd(2);
		
		// defect 10112 
		//laTitleData.setLienHolder1Date(20011011);
		//laTitleData.setLienHolder2Date(20011011);
		//laTitleData.setLienHolder3Date(20011011);

		// Set Lienholder data, from Title data object
		laLienHoldData.setLienDate(20011011);
		laLienHoldData.getAddressData().setCity("AUSTIN");
		laLienHoldData.setName1("CCO LOANS");
		laLienHoldData.setName2("CCO SECOND LOANS");
		laLienHoldData.getAddressData().setSt1("123 FOURTH ST");
		laLienHoldData.getAddressData().setSt2("567 SEVENTH ST");
		laLienHoldData.getAddressData().setCntry("US");
		laLienHoldData.getAddressData().setState("TX");
		laLienHoldData.getAddressData().setZpcd("44444");
		laLienHoldData.getAddressData().setZpcdp4("2222");
		//		laTitleData.setLienHolder1(laLienHoldData);
		//		laTitleData.setLienHolder2(laLienHoldData);
		//		laTitleData.setLienHolder3(laLienHoldData);
		laTitleData.setLienholderData(
			TitleConstant.LIENHLDR1,
			laLienHoldData);
		laTitleData.setLienholderData(
			TitleConstant.LIENHLDR2,
			laLienHoldData);
		laTitleData.setLienholderData(
			TitleConstant.LIENHLDR3,
			laLienHoldData);
		// add Lienholder to Title data object
		// end defect 10112 

		laMFVehData.setTitleData(laTitleData);
		// add Title data object to MFVeh data object

		// Set ownwer data
		laOwnrData.setName1("JEFF RUE"); // set owner name1
		laOwnrData.setName2("JENNIFER RUE"); // set owner name2

		// Set address data, from Owner data object
		AddressData laAddressOwnr = new AddressData();
		laAddressOwnr.setCity("AUSTIN");
		laAddressOwnr.setSt1("1313 MOCKINGBIRD LN");
		laAddressOwnr.setSt2("1314 OVER THERE LN");
		laAddressOwnr.setState("TX");
		laAddressOwnr.setCntry("US");
		laAddressOwnr.setZpcd("76999");
		laAddressOwnr.setZpcdp4("5555");
		// defect 10112
		laOwnrData.setAddressData(laAddressOwnr);
		// end defect 10112 

		laMFVehData.setOwnerData(laOwnrData);

		// Set Registration data
		laRegisData.setRegPltNo("AA0630");
		laMFVehData.setRegData(laRegisData);

		// Add MFVehData to the passing vector
		lvCCO.addElement(laMFVehData);
		// ************* End  MFVehData  data object ***************

		// ********** Build  MailingAddressData  data object **********     
		// Set Mailing data
		laMailAddr.setName1("JEFF RUE"); // set owner name1
		laMailAddr.setName2("CHRISTINE RUE"); // set owner name2

		// Set address type, Mailing Address   	 
		AddressData laAddressMail = new AddressData();
		laAddressMail.setCity("AZLE");
		laAddressMail.setState("TX");
		laAddressMail.setSt1("1313 MAPLE");
		laAddressMail.setSt2("1314 UP THERE LN");
		laAddressMail.setCntry("US");
		laAddressMail.setZpcd("76333");
		laAddressMail.setZpcdp4("2345");

		laMailAddr.setAddressData(laAddressMail);
		// set owner data object

		// Add Mailing Data to the passing vector
		lvCCO.addElement(laMailAddr);

		// ********** End  MailingAddressData  data object **********

		// ********** Build  Commercial Tonnage  data object **********     
		// Set Commercial tonnage data
		laCommVehWts.setVehTonDesc("3/4");
		lsTonnageDesc = laCommVehWts.getVehTonDesc();

		// Add Tonnage Description Data to the passing vector
		lvCCO.addElement(lsTonnageDesc);

		// ********** End  Commercial Tonnage  data object **********     	

		// ********** Build  Registration Class Odometer Required  data object **********     	
		// Set Odometer reading required data
		laRegisClass.setOdmtrReqd(1);
		liOdomtrReqd = laRegisClass.getOdmtrReqd();
		String lsOdomtrReqd = String.valueOf(liOdomtrReqd);

		// Add Odometer Required Data to the passing vector
		lvCCO.addElement(lsOdomtrReqd);

		// ********** End  Registration Class Odometer Required  data object **********     	

		// ********** Build  IndiDesc  data object **********     	
		// Set Indi Desc data
		lvIndiVector.addElement(null);

		// Add Dealer Data to the passing vector
		lvCCO.addElement(lvIndiVector);

		// ********** End  IndiDesc  data object **********     	
		return lvCCO;
	}

	/**
	 * Round fraction to a integer.
	 * 
	 * @param afNum float
	 * @param aiUpDown int: 0 = Round Up, 1 = Round down
	 * @return int
	 */
	private int roundFractionToInt(float afNum, int aiUpDown)
	{
		float lfLngth = (afNum % 2);
		int liLngth = (int) lfLngth;

		switch (liLngth)
		{
			case 0 :
				{
					liLngth = (int) afNum;
					break;
				}
			case 1 :
				{
					liLngth = (int) afNum;
					break;
				}
			default :
				{
					switch (aiUpDown)
					{
						case 0 :
							{
								liLngth = (int) afNum + 1;
								break;
							}
						case 1 :
							{
								liLngth = (int) afNum;
							}
						default :
							{
								System.out.println(
									"Invalid up/dowm param");
								System.out.println(
									"   valid parms  0 = up, 1 = down");
							}
					} // end switch
				}
		} // end switch
		return liLngth;
	}
}
