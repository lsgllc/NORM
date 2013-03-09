package com.txdot.isd.rts.services.reports.title;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CommercialVehicleWeightsCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.Print;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.OdometerBrands;
import com.txdot.isd.rts.services.util.constants.TitleConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * GenNRPCOT.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		10/19/2001	New Class
 * Min Wang		05/16/2002  (CQU3965) Using Mailing address Object.
 * J Rue		05/05/2003	Add code to print patch code.
 *							modify formatReport()
 *							Defect 6032
 * 				Additional	Add process to print the appropriate blanks 
 *							line
 *							method actionPerformed()
 *							Defect 6032/Release 5.1.4
 * J Rue		07/14/2003	Align the mailing addres with RTSI
 *							method	formatReport()
 *							Defect 6182
 * J Rue		08/29/2003	Move mail address up 1 line.
 *							method formatReport()
 *							Defect 6355 v5.1.5
 * J Rue 		09/10/2003	In build, the print did not move up to
 *							correct print height. Set Mailing Addr to
 *							print 1 line higher.
 *							method formatReport()
 *							Defect 6355 v5.1.5 (Modify)
 * J Rue		09/12/2003	Format javaFile/javaDoc commects
 * K Harrell	05/11/2004	Remove reference to printReceipt(null)
 *							modify main()
 *							defect 7096 Ver 5.2.0
 * B Hargrove	05/17/2004	Print all 9 (possible) chars in zip+zip4 for
 *                          non-USA Owner Address.
 *							modify printCityState(), 
 *							printMailingAddress(),
 *							printOwnerAddress(),
 *							printOwnerAddressAndRemark()
 * B Hargrove	05/19/2004	Re-formatted comments in all methods above.
 *							defect 6911 Ver 5.2.0
 * Jeff S.		12/09/2004	The address was getting cut off.  Used
 *							string length instead of length of 30 when
 *							printing the string.
 *							modify  printOwnerAddressAndRemark()
 *							defect 7734 Ver 5.2.2
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify main(), queryData()
 *							defect 7896 Ver 5.2.3
 * J Zwiener	09/29/2005	Revert back due to Null Ptr Exception
 * 							modify printMailingAddress()
 * 							defect 7896 Ver 5.2.3   
 * K Harrell	04/28/2008	Add Transaction Description to receipt 
 * 							modify formatReport()
 * 							defect 9636 Ver POS Defect A
 * K Harrell	06/02/2008	Include Logic for COA.  Print "Certificate 
 * 							of Authority" for COA.
 * 							add START_PT_COA_NOTATION
 * 							add printDocumentationNotation() 
 * 							delete printCertifiedCopy()
 * 							modify formatReport()
 * 							defect 9642 Ver POS Defect A
 * K Harrell	07/11/2008	Add logic for printing lienholder info
 * 							add constants  (from GenSCOT) 
 * 							add printLienData(), printLienHolderAddress(),
 * 							 printLienholderTemplate0(), 
 * 							 printLienholderTemplate1(),
 * 							 printLienholderTemplate2(),
 * 							 printLienholderTemplate3(),
 * 							 printLienholderTemplate4()
 * 							modify formatReport()
 * 							defect 9731 Ver MyPlates_POS  
 * K Harrell	03/03/2009	Extreme Code cleanup regarding above (9731)
  * 							add caTitle, caLienData, caLienAddrData, caCTData
 * 							add isEmpty(), printLienholderData(), 
 * 							 printNoLienDataNotation(),
 * 							 printMailingAddress(),
 *							 printVehicleInfo(),
 *							 printDocNoAndIssuedDate(),
 *							 printVehicleData(),
 *							 printPreviousOwner(),
 *							 printOwnerAddressAndRemark()   
 * 							delete printLienData(), printLienHolderAddress(),
 * 							 printLienholderTemplate0(), 
 * 							 printLienholderTemplate1(),
 * 							 printLienholderTemplate2(),
 * 							 printLienholderTemplate3(),
 * 							 printLienholderTemplate4(),
 * 							 printMailingAddress(int),
 * 							 printVehicleInfo(CompleteTransactionData),
 *							 printDocNoAndIssuedDate(CompleteTransactionData),
 *							 printVehicleData(CompleteTransactionData),
 *							 printPreviousOwner(CompleteTransactionData, int, int),
 *							 printOwnerAddressAndRemark(CompleteTransactionData)
 * 							modify formatReport()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/03/2009	Implement new LienholderData 
 * 							modify formatReport(), printLienholderData(), 
 * 							 printMailingAddress(), printOwnerAddressandRemark(), 
 * 							 queryData() 
 * 							defect 10112 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */
/**
 * This class formats the Texas Nonrepairable Certificate of Title.
 *
 * @version	Defect_POS_E 	07/03/2009
 * @author	Min Wang
 * @author	Kathy Harrell
 * <br>Creation Date:		10/19/2001 08:53:10
 */
public class GenNRPCOT extends ReportTemplate
{
	// defect 9969 
	private TitleData caTitleData;
	private LienholderData caLienData;
	private AddressData caLienAddrData;
	private CompleteTransactionData caCTData;
	// end defect 9969 

	// defect 9731
	// Added to print Lienholder Info for NRCOT  (copied from GenSCOT)   
	private static final String AUTHORIZESTRING =
		"        AUTHORIZED AGENT       ";
	private static final String BODYSTYLESTRING = "BODY STYLE";
	private static final String BYSTRING =
		"BY_____________________________";
	private static final String LIENDATESTRING = "DATE OF LIEN";
	private static final String LIENHOLDSTRING = "LIENHOLDER";
	private static final String LIENRELEASEDSTRING =
		"1ST LIEN RELEASED______________";
	private static final int LENGTH12 = 12;
	private static final int LENGTH35 = 35;
	private static final int START_PT10 = 10;
	private static final int START_PT17 = 17;
	private static final int START_PT37 = 37;
	private static final int START_PT96 = 96;
	// end defect 9731	

	private static final String CAPACITYSTRING = "MFG CAPACITY IN TONS";
	private static final String CERTIFICATE = "CERTIFICATE";
	private static final String CERTIFIEDCOPYISSUEDATE =
		"CERTIFIED COPY ISSUE DATE";
	private static final String CERTIFIEDCOPYSTRING = "CERTIFIED COPY";
	private static final String DATEISSUEDSTRING = "DATE ISSUED";
	private static final String DATESTRING =
		"                     DATE      ";
	private static final String DOCNOSTRING = "TITLE DOCUMENT NUMBER";
	private static final String EXEMPT = "EXEMPT";

	private static final int LENGTH1 = 1;
	private static final int LENGTH10 = 10;
	private static final int LENGTH11 = 11;
	private static final int LENGTH14 = 14;
	private static final int LENGTH15 = 15;
	private static final int LENGTH17 = 17;
	private static final int LENGTH2 = 2;
	private static final int LENGTH20 = 20;
	private static final int LENGTH22 = 22;
	private static final int LENGTH25 = 25;
	private static final int LENGTH30 = 30;
	private static final int LENGTH4 = 4;
	private static final int LENGTH5 = 5;
	private static final int LENGTH6 = 6;
	private static final int LENGTH7 = 7;
	private static final int LENGTH9 = 9;
	private static final String LICENSESTRING = "LICENSE NUMBER";

	private static final int LINES2 = 2;
	private static final String NONESTRING = "NONE";
	private static final String MAKERSTRING = "MAKE OF VEHICLE";
	private static final String MODELSTRING = "MODEL";
	private static final String MODELYEARSTRING = "YEAR MODEL";

	private static final String ODOMETERREADSTRING = "ODOMETER READING";
	private static final String OWNERSTRING = "OWNER";
	private static final String PREVIOUSOWNERSTRING = "PREVIOUS OWNER";
	private static final String PREVIOUSTITLESTRING =
		"PREVIOUS TITLE STATE";
	private static final String RECEIPT = "RECEIPT";
	private static final String REMARKSTRING = "REMARK(S)";

	// defect 9642 
	private static final int START_PT_COA_NOTATION = 23;
	// end defect 9642 

	private static final int START_PT107 = 107;
	private static final int START_PT113 = 113;
	private static final int START_PT13 = 13;

	private static final int START_PT19 = 19;
	private static final int START_PT20 = 20;
	private static final int START_PT27 = 27;
	private static final int START_PT33 = 33;
	private static final int START_PT34 = 34;
	private static final int START_PT38 = 38;
	private static final int START_PT39 = 39;
	private static final int START_PT41 = 41;
	private static final int START_PT44 = 44;
	private static final int START_PT48 = 48;
	private static final int START_PT49 = 49;
	private static final int START_PT51 = 51;
	private static final int START_PT54 = 54;
	private static final int START_PT57 = 57;
	private static final int START_PT64 = 64;
	private static final int START_PT67 = 67;
	private static final int START_PT7 = 7;
	private static final int START_PT70 = 70;
	private static final int START_PT77 = 77;
	private static final int START_PT87 = 87;
	private static final int START_PT95 = 95;
	private static final int START_PT98 = 98;
	private static final String VINSTRING =
		"VEHICLE IDENTIFICATION NUMBER";
	private static final String WEIGHTSTRING = "WEIGHT";

	/**
	 * Starts the application.
	 *
	 * @param aarrArgs String[] an array of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.SALVCCO");
		laRptProps.setPageHeight(115);
		laRptProps.setPageWidth(160);
		GenNRPCOT laGNRPCOT =
			new GenNRPCOT("SALVCCO Document", laRptProps);

		// Generating Demo data to display.
		// Do a Receipt
		Vector lvReceiptData = laGNRPCOT.queryData("");

		// Do a Certificate
		// Vector lvReceiptData = gscot.queryData(0);	//for test 

		laGNRPCOT.formatReport(lvReceiptData);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\SALVCCO.TXT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGNRPCOT.caRpt.getReport().toString());
		laPout.close();

		// Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport =
				new FrmPreviewReport("c:\\SALVCCO.TXT");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
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
	 * GenNRPCOT constructor
	 */
	public GenNRPCOT()
	{
		super();
	}

	/**
	 * GenNRPCOT constructor
	 *
	 * @param asRptName String
	 * @param aaRptProps ReportProperties
	 */
	public GenNRPCOT(String asRptName, ReportProperties aaRptProps)
	{
		super(asRptName, aaRptProps);
	}

	/**
	 * Changes the reports font.
	 *
	 * @param abNewLine boolean
	 */
	public void changeNormalFont(boolean abNewLine)
	{
		if (abNewLine)
		{
			caRpt.printAttributes(
				Print.getPRINT_LASER() + Print.getPRINT_HORHDRSPC12());
		}
		else
		{
			caRpt.printAttributesNoReturn(
				Print.getPRINT_LASER() + Print.getPRINT_HORHDRSPC12());
		}
	}

	/**
	 * Changes the reports font.
	 *
	 * @param abNewLine boolean
	 */
	public void changeSmallFont(boolean abNewLine)
	{
		if (abNewLine)
		{
			caRpt.printAttributes(
				Print.getPRINT_HDRLASER()
					+ Print.getPRINT_HORHDRSPC6());
		}
		else
		{
			caRpt.printAttributesNoReturn(
				Print.getPRINT_HDRLASER()
					+ Print.getPRINT_HORHDRSPC6());
		}
	}

	/**
	 * This method generates the report.
	 * 
	 * @param avResults Vector 
	 */
	public void formatReport(Vector avResults)
	{
		caCTData = (CompleteTransactionData) avResults.elementAt(0);

		caTitleData = caCTData.getVehicleInfo().getTitleData();

		// defect 10112 
		//caLienData = caTitleData.getLienHolder1();
		caLienData =
			caTitleData.getLienholderData(TitleConstant.LIENHLDR1);
		// end defect 10112 

		caLienAddrData = caLienData.getAddressData();

		String lsOutputType = (String) avResults.elementAt(1);

		changeNormalFont(true);
		caRpt.printAttributes("&l4C");

		// test for receipt or certificate
		if (lsOutputType.equals(RECEIPT))
		{
			// defect 9643 
			// Write Transaction description to barcoded receipt 
			try
			{
				TransactionCodesData laTransCdData =
					TransactionCodesCache.getTransCd(
						caCTData.getTransCode());
				caRpt.print(laTransCdData.getTransCdDesc().trim());
			}
			catch (RTSException aeRTSEx)
			{
				System.out.print("Transaction Code Not Found");
			}
			// end defect 9643 

			// defect 6032
			// new method - print patch code
			printPatchCode();
			// end defect 6032

			// defect 6182
			// Move bar code down for proper alignment
			caRpt.blankLines(2);
			// end defect 6182		
			printBarCode(caCTData.getTransId());
			// defect 6032
			// Add process to print the appropriate blanks line
			caRpt.blankLines(18 - caRpt.getCurrX());
			// end defect 6032
		}
		// defect 6182
		// Add ELSE to set pointer for Title receipt
		else
		{
			// defect 6355 
			// caRpt.blankLines(10 - caRpt.getCurrX()); 
			caRpt.blankLines(8 - caRpt.getCurrX());
		}
		// end defect 6182, 6355

		printMailingAddress();

		// defect 9642 
		// test for documents with notations:  CCO or COA 
		if (lsOutputType.equals(CERTIFICATE)
			&& (caCTData.getTransCode().equals(TransCdConstant.CCONRT)
				|| caCTData.getTransCode().equals(TransCdConstant.COA)))
		{
			caRpt.blankLines(27 - caRpt.getCurrX());
			printDocumentNotation();
		}
		// end defect 9642 

		// defect 6032
		// Add process to print the appropriate blanks line
		if (lsOutputType.equals(RECEIPT))
		{
			caRpt.blankLines(32 - caRpt.getCurrX());
		}
		// defect 6182, For Certificate
		else
		{
			caRpt.blankLines(40 - caRpt.getCurrX());
		}
		// end defect 6182
		printVehicleInfo();
		printDocNoAndIssuedDate();
		printVehicleData();
		printPreviousOwner(); //START_PT7, LENGTH30);
		printOwnerAddressAndRemark();
		// defect 9731 
		caRpt.blankLines(14 - caRpt.getCurrX());
		if (!caCTData.getTransCode().equals(TransCdConstant.COA))
		{
			printLienholderData();
		}
		// end defect 9731 
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Call UtilityMethods "isEmpty()" to determine if empty string
	 * 
	 * @param asString 
	 */
	private boolean isEmpty(String asString)
	{
		return UtilityMethods.isEmpty(asString);
	}

	/**
	 * Prints the barcode tag on the report.
	 *
	 * @param asTransId String 
	 */
	private void printBarCode(String asTransId)
	{
		caRpt.blankLines(LENGTH9);
		caRpt.print("", START_PT48, LENGTH1);
		caRpt.print(Print.getBARCODE_TAG(asTransId, 7.2));
		// ********* verify this later **********
		caRpt.nextLine();
	}

	/**
	 * Print the Vehicle Information Information.
	 *
	 * @param aaTransData CompleteTransactionData
	 */
	private void printDocNoAndIssuedDate()
	{
		changeSmallFont(true);
		caRpt.print(DOCNOSTRING, START_PT67, LENGTH22);
		caRpt.print(DATEISSUEDSTRING, START_PT107 - 4, LENGTH11);
		caRpt.nextLine();
		changeNormalFont(true);

		// print document number
		if (caCTData.getTransCode().equals(TransCdConstant.CCONRT))
		{
			caRpt.print(caTitleData.getDocNo(), START_PT34, LENGTH17);
		}
		else
		{
			caRpt.print(caCTData.getTransId(), START_PT34, LENGTH17);
		}

		// print date issued
		RTSDate laIssueDate =
			new RTSDate(
				RTSDate.YYYYMMDD,
				caTitleData.getTtlIssueDate());
		caRpt.print(laIssueDate.toString(), START_PT54 - 2, LENGTH10);
		caRpt.blankLines(LINES2);
	}

	/**
	 * Prints the Certified Copy string on the report.
	 *
	 * @param aaTransData CompleteTransactionData
	 */
	private void printDocumentNotation()
	{
		if (caCTData.getTransCode().equals(TransCdConstant.CCONRT))
		{
			caRpt.print(CERTIFIEDCOPYSTRING, START_PT27, LENGTH14);
			caRpt.nextLine();
		}
		else if (caCTData.getTransCode().equals(TransCdConstant.COA))
		{
			String lsNotation = "CERTIFICATE OF AUTHORITY";
			caRpt.print(
				lsNotation,
				START_PT_COA_NOTATION,
				lsNotation.length());
			caRpt.nextLine();
		}
	}

	/**
	 * Print No Lien Data Notation 
	 *
	 * @param aaTransData CompleteTransactionData
	 */
	private void printNoLienDataNotation()
	{
		changeSmallFont(true);
		caRpt.print(LIENDATESTRING, START_PT10, LENGTH12);
		caRpt.print(LIENHOLDSTRING, START_PT34, LENGTH10);
		caRpt.nextLine();
		changeNormalFont(true);
		caRpt.print(NONESTRING, START_PT17, LENGTH4);
	}

	/**
	 * Print Lienholder Data
	 */
	private void printLienholderData()
	{
		// defect 10112 
		String lsLienHldrName1 = caLienData.getName1();
		// end defect 10112 

		// No Lienholder Data  
		if (caLienData == null || isEmpty(lsLienHldrName1))
		{
			printNoLienDataNotation();
		}
		else
		{
			// Print Lienholder Data 

			// lvData will be a vector of Name and Address 
			// Minimum size of 3, Maximum size of 5 based upon 
			// presence of 2nd Name, 2nd Street
			// defect 10112 
			Vector lvData = caLienData.getNameVector();
			// end defect 10112 
			lvData.addAll(caLienAddrData.getAddressVector(false));

			// Line 1
			changeSmallFont(true);
			caRpt.print(LIENDATESTRING, START_PT13, LENGTH12);
			caRpt.print(LIENHOLDSTRING, START_PT37, LENGTH10);
			caRpt.nextLine();

			caRpt.print(LIENRELEASEDSTRING, START_PT96, LENGTH35);
			changeNormalFont(false);
			caRpt.nextLine();

			// defect 10112 
			int liDate = caLienData.getLienDate();
			// end defect 10112 
			RTSDate laLienHolderDate = new RTSDate();
			if (liDate != 0)
			{
				laLienHolderDate =
					new RTSDate(RTSDate.YYYYMMDD, liDate);
			}

			caRpt.print(
				laLienHolderDate.toString(),
				START_PT7,
				LENGTH10);

			// Line 2 
			// Lienholder name1	
			caRpt.print(
				(String) lvData.elementAt(0),
				START_PT19,
				LENGTH30);
			changeSmallFont(true);

			// Line 2 - small font
			caRpt.print(DATESTRING, START_PT96, LENGTH35);
			changeNormalFont(true);

			// Line 3 
			caRpt.print(
				(String) lvData.elementAt(1),
				START_PT19,
				LENGTH30);
			caRpt.nextLine();
			caRpt.nextLine();

			// Line 4 
			caRpt.print(
				(String) lvData.elementAt(2),
				START_PT19,
				LENGTH30);
			changeSmallFont(true);

			// Line 4 - small font
			caRpt.print(BYSTRING, START_PT96, LENGTH35);

			if (lvData.size() > 3)
			{
				// Line 5 
				changeNormalFont(true);

				caRpt.print(
					(String) lvData.elementAt(3),
					START_PT19,
					LENGTH30);
			}
			changeSmallFont(true);

			// Line 5 small font
			caRpt.print(AUTHORIZESTRING, START_PT96, LENGTH35);

			if (lvData.size() > 4)
			{
				// Line 6 
				changeNormalFont(true);
				caRpt.print(
					(String) lvData.elementAt(4),
					START_PT19,
					LENGTH30);
			}
		}
	}

	/**
	 * Print the Mailing Address Information.
	 *
	 * @param aiStartPt int
	 * @param aiLength int
	 */
	private void printMailingAddress()
	{
		changeNormalFont(true);

		OwnerData laOwnerData =
			caCTData.getVehicleInfo().getOwnerData();

		Vector lvData = new Vector();
		AddressData laAddressData = null;
		
		// defect 10112 
		NameAddressData laMAData = caCTData.getMailingAddrData();

		if (laMAData == null || isEmpty(laMAData.getName1()))
		{
			if (caLienData == null || isEmpty(caLienData.getName1()))
			{
				lvData = laOwnerData.getNameVector();
				laAddressData = laOwnerData.getAddressData();
			}
			else
			{
				lvData = caLienData.getNameVector();
				laAddressData = caLienData.getAddressData();
			}
		}
		else
		{
			lvData = laMAData.getNameVector();
			laAddressData = laMAData.getAddressData();
		}
		// end defect 10112 
		
		lvData.addAll(laAddressData.getAddressVector(false));

		for (int i = 0; i < lvData.size(); i++)
		{
			caRpt.print(
				(String) lvData.elementAt(i),
				START_PT13,
				((String) lvData.elementAt(i)).length());
			caRpt.blankLines(LINES2);
		}
	}

	/**
	 * Print the Owner Address Information.
	 */
	private void printOwnerAddressAndRemark()
	{
		int liOwnerCount = 0;
		Vector lvRemarks = new Vector();
		int liRemarksCount = 0;

		OwnerData laOwnerData =
			caCTData.getVehicleInfo().getOwnerData();

		changeSmallFont(true);
		caRpt.print(OWNERSTRING, START_PT13, LENGTH14);
		caRpt.print(REMARKSTRING, START_PT87, LENGTH9);
		caRpt.nextLine();
		changeNormalFont(true);

		// build owner's address vector
		// defect 10112 
		Vector lvOwnerData = laOwnerData.getNameVector();
		lvOwnerData.addAll(
			laOwnerData.getAddressData().getAddressVector(false));
		// end defect 10112 

		MFVehicleData laMFVehData = caCTData.getVehicleInfo();
		Vector lvIndicators = null;
		String lsOdometerBrand = "";
		String lsOdometerReading = "";

		if (laMFVehData != null
			&& laMFVehData.getVehicleData() != null)
		{
			lsOdometerReading =
				laMFVehData.getVehicleData().getVehOdmtrReadng();
		}
		if (!isEmpty(lsOdometerReading)
			&& !lsOdometerReading.equalsIgnoreCase(EXEMPT))
		{
			lsOdometerBrand =
				laMFVehData.getVehicleData().getVehOdmtrBrnd();

			if (lsOdometerBrand == null)
			{
				// empty code block
			}
			else if (
				lsOdometerBrand.equals(OdometerBrands.ACTUAL_CODE))
			{
				lvRemarks.add(OdometerBrands.ACTUAL);
			}
			else if (
				lsOdometerBrand.equals(OdometerBrands.EXCEED_CODE))
			{
				lvRemarks.add(OdometerBrands.EXCEED);
			}
			else if (
				lsOdometerBrand.equals(OdometerBrands.NOTACT_CODE))
			{
				lvRemarks.add(OdometerBrands.NOTACT);
			}
		}
		lvIndicators =
			IndicatorLookup.getIndicators(
				laMFVehData,
				caCTData.getTransCode(),
				IndicatorLookup.SALVAGE);

		for (int i = 0; i < lvIndicators.size(); i++)
		{
			IndicatorData laData = (IndicatorData) lvIndicators.get(i);
			lvRemarks.add(laData.getDesc());
		}

		boolean lbKeepLooping = true;

		while (lbKeepLooping)
		{
			if (liOwnerCount < lvOwnerData.size())
			{
				// defect 7734
				// use length of field instead of LENGTH30
				caRpt.print(
					String.valueOf(lvOwnerData.elementAt(liOwnerCount)),
					START_PT7,
					String
						.valueOf(lvOwnerData.elementAt(liOwnerCount))
						.length());
				// end defect 7734
				liOwnerCount = liOwnerCount + 1;
			}

			if (liRemarksCount < lvRemarks.size()
				&& lvRemarks.elementAt(liRemarksCount) != null)
			{
				caRpt.print(
					String.valueOf(lvRemarks.elementAt(liRemarksCount)),
					START_PT44,
					LENGTH30);
				liRemarksCount = liRemarksCount + 1;

			}

			caRpt.blankLines(2);

			if ((liOwnerCount >= lvOwnerData.size())
				&& (liRemarksCount >= lvRemarks.size()))
			{
				lbKeepLooping = false;
			}
		}
	}

	/**
	 * Print the Vehicle Information.
	 *
	 */
	private void printPreviousOwner()
	{
		changeSmallFont(true);
		caRpt.print(PREVIOUSOWNERSTRING, START_PT13, LENGTH15);
		if (caCTData.getTransCode().equals(TransCdConstant.CCONRT))
		{
			caRpt.print(CERTIFIEDCOPYISSUEDATE, START_PT87, LENGTH25);
		}
		caRpt.nextLine();
		changeNormalFont(true);

		// Print previous owner's name
		if (!isEmpty(caTitleData.getPrevOwnrName()))
		{
			caRpt.print(
				caTitleData.getPrevOwnrName(),
				START_PT7,
				caTitleData.getPrevOwnrName().length());
		}

		// Print TransAMDate
		if (caCTData.getTransCode().equals(TransCdConstant.CCONRT))
		{
			RTSDate laRTSDate = new RTSDate();
			caRpt.print(
				String.valueOf(laRTSDate.toString()),
				START_PT44,
				LENGTH10);
		}
		caRpt.blankLines(LINES2);

		// Print previous owner's address
		if (!isEmpty(caTitleData.getPrevOwnrCity()))
		{
			String lsPrevCityState =
				caTitleData.getPrevOwnrCity()
					+ ", "
					+ caTitleData.getPrevOwnrState();

			caRpt.print(
				lsPrevCityState,
				START_PT7,
				lsPrevCityState.length());

			caRpt.blankLines(LINES2);
		}
	}

	/**
	 * Print the Vehicle Information.
	 *
	 */
	private void printVehicleData()
	{
		VehicleData laVehicleData =
			caCTData.getVehicleInfo().getVehicleData();

		changeSmallFont(true);
		caRpt.print(CAPACITYSTRING, START_PT13, LENGTH20);
		caRpt.print(WEIGHTSTRING, START_PT39, LENGTH6);
		caRpt.print(LICENSESTRING, START_PT51, LENGTH15);
		caRpt.print(PREVIOUSTITLESTRING, START_PT70, LENGTH20);
		caRpt.print(ODOMETERREADSTRING, START_PT95, LENGTH17);
		caRpt.nextLine();
		changeNormalFont(true);

		Dollar laDollar = new Dollar("0.00");

		if (laVehicleData.getVehTon() != null
			&& !laVehicleData.getVehTon().equals(laDollar))
		{
			Dollar laVehTon = laVehicleData.getVehTon();
			CommercialVehicleWeightsData laComVehData =
				CommercialVehicleWeightsCache.getCommVehWts(laVehTon);
			caRpt.center(
				laComVehData.getVehTonDesc(),
				START_PT7,
				LENGTH7);
		}

		//print weight 
		if (laVehicleData.getVehEmptyWt() != 0)
		{
			caRpt.print(
				String.valueOf(laVehicleData.getVehEmptyWt()),
				START_PT20,
				LENGTH5);
		}

		//print license number
		String lsPlate =
			caCTData.getVehicleInfo().getRegData().getRegPltNo();
		if (lsPlate == null)
		{
			lsPlate = "";
		}
		caRpt.print(lsPlate, START_PT27, LENGTH7);

		// print previous title state
		String lsOtherSt =
			((SalvageData) caCTData
				.getVehicleInfo()
				.getVctSalvage()
				.get(0))
				.getOthrStateCntry();

		if (lsOtherSt == null)
		{
			lsOtherSt = "";
		}
		caRpt.print(lsOtherSt, START_PT38, lsOtherSt.length());

		//print odometer reading
		String lsVehReading = laVehicleData.getVehOdmtrReadng();

		if (lsVehReading == null)
		{
			lsVehReading = "";
		}
		caRpt.print(
			laVehicleData.getVehOdmtrReadng(),
			START_PT49,
			LENGTH6);
		caRpt.blankLines(LINES2);
	}

	/**
	 * Print the Vehicle Information.
	 *
	 */
	private void printVehicleInfo()
	{
		VehicleData laVehicleData =
			caCTData.getVehicleInfo().getVehicleData();

		changeSmallFont(true);
		caRpt.print(VINSTRING, START_PT13, LENGTH30);
		caRpt.print(MODELYEARSTRING, START_PT64, LENGTH10);
		caRpt.print(MAKERSTRING, START_PT77, LENGTH15);
		caRpt.print(BODYSTYLESTRING, START_PT98 - 2, LENGTH10);
		caRpt.print(MODELSTRING, START_PT113 - 4, LENGTH5);
		caRpt.nextLine();
		changeNormalFont(true);

		//print VIN
		String lsVin = laVehicleData.getVin();
		if (lsVin == null)
		{
			lsVin = "";
		}
		caRpt.print(lsVin, START_PT7, LENGTH22);

		//print year
		int liYear = laVehicleData.getVehModlYr();
		if (liYear == 0)
		{
			caRpt.print("", START_PT33, LENGTH4);
		}
		else
		{
			caRpt.print(
				"" + laVehicleData.getVehModlYr(),
				START_PT33,
				LENGTH4);
		}

		//print maker
		String lsMake = laVehicleData.getVehMk();
		if (lsMake == null)
		{
			lsMake = "";
		}
		caRpt.print(lsMake, START_PT41, LENGTH4);

		//print  style
		String lsStyle = laVehicleData.getVehBdyType();
		if (lsStyle == null)
		{
			lsStyle = "";
		}
		caRpt.print(lsStyle, START_PT51 - 1, LENGTH2);

		//print model
		String lsModel = laVehicleData.getVehModl();
		if (lsModel == null)
		{
			lsModel = "";
		}
		caRpt.print(lsModel, START_PT57 - 2, LENGTH4);
		caRpt.blankLines(LINES2);
	}

	/**
	 * Sets up Query Data
	 *
	 * @return CompleteTransactionData
	 */
	public CompleteTransactionData queryData()
	{
		// Generating Demo data to display.
		CompleteTransactionData laTransData =
			new CompleteTransactionData();

		MFVehicleData laMFdata = new MFVehicleData();
		OwnerData laOwnrData = new OwnerData();
		TitleData laTtlData = new TitleData();
		VehicleData laVehData = new VehicleData();
		RegistrationData laRegData = new RegistrationData();
		SalvageData laOtherStateCntry = new SalvageData();
		AddressData laAddrData = new AddressData();

		// defect 10112 
		laOwnrData.setName1("Min Wang");
		// end defect 10112 

		laAddrData.setCity("Austin");
		laAddrData.setCntry("US");
		laAddrData.setSt1("Some Street1");
		laAddrData.setState("TX");
		laAddrData.setZpcd("11111");
		laAddrData.setZpcdp4("2222");
		laOwnrData.setAddressData(laAddrData);

		//MFVehicleData MFdata = new MFVehicleData();
		laMFdata.setOwnerData(laOwnrData);

		//VehicleData VehData = new VehicleData();
		laVehData.setVehBdyType("4T");
		laVehData.setVehMk("HOND");
		laVehData.setVehModlYr(2002);
		laVehData.setVehEmptyWt(1900);
		laVehData.setVin("1234567890asdfghj");
		laMFdata.setVehicleData(laVehData);

		//TitleData TtlData = new TitleData();
		laTtlData.setDocNo("11111111111111111");
		laTtlData.setTtlIssueDate(10182001);
		laMFdata.setTitleData(laTtlData);

		//RegistrationData RegData = new RegistrationData();
		laRegData.setRegPltNo("B01BBBB");
		laMFdata.setRegData(laRegData);

		//SalvageData OtherStateCntry = new SalvageData();
		laOtherStateCntry.setOthrStateCntry("US");
		laMFdata.setVctSalvage(new Vector());
		laMFdata.getVctSalvage().add(laOtherStateCntry);

		laTransData.setVehicleInfo(laMFdata);

		return laTransData;
	}

	/**
	 * Sets up Query Data.
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		Vector lvReceipt = new Vector();

		CompleteTransactionData laTransData =
			new CompleteTransactionData();

		laTransData = (CompleteTransactionData) Vehicle.getVeh();
		laTransData.setTransCode("NRCOT");

		laTransData.getVehicleInfo().getVehicleData().setVehModl(
			"hond");

		// Add the lTransData to the vector
		lvReceipt.addElement(laTransData);

		// Add the transid to the vector
		lvReceipt.addElement("17010037205111155");

		// Add the output type to the vector
		lvReceipt.addElement(RECEIPT);
		//lvReceipt.addElement(CERTIFICATE);

		return lvReceipt;
	}
}
