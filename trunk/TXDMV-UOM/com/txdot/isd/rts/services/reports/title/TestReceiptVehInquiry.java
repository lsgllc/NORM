package com.txdot.isd.rts.services.reports.title;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;

/*
 *
 * TestReceiptVehInquiry.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/01/2001	Methods created
 * J Rue      	09/19/2001	Added comments
 * K Harrell	05/12/2005 	refactoring of printTrlvTrlrLngth() to 
 * 							printTrvlTrlrLngth() in ReceiptTemplate
 * 							defect 7562 Ver 5.2.3  
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3  
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify main(), queryData()
 *							defect 7896 Ver 5.2.3  
 * Min Wang		10/10/2006	New Requirement for handling plate age 
 * 							modify parseRegisInfoVehInq()
 *							defect 8901 Ver Exempts             	 
 * ---------------------------------------------------------------------
 */
/**
 * @version	Exempts			10/11/2006
 * @author  Jeff Rue
 * <br>Creation Date:		10/19/2001 08:53:10
 */
public class TestReceiptVehInquiry extends ReceiptTemplate
{
	private final static int ciCol_01 = 1;
	private final static int ciCol_20 = 20;
	private final static int ciCol_33 = 33;
	private final static int ciCol_50 = 50;
	private final static int ciCol_18 = 18;
	private final static int ciCol_45 = 45;
	private final static int ciCol_62 = 62;
	private final static int ciCol_57 = 57;
	private final static int ciCol_55 = 55;
	private final static int ciCol_43 = 43;
	private final static int ciCol_80 = 80;
	private final static int ciCol_68 = 68;
	private final static int ciCol_39 = 39;
	private final static int ciCol_35 = 35;
	private final static int ciCol_26 = 26;
	private final static int ciCol_82 = 82;

	/**
	 * TestReceiptVehInquiry constructor
	 */
	public TestReceiptVehInquiry()
	{
		// empty code block
	}

	/**
	 * TestReceiptVehInquiry constructor
	 * 
	 * @param asRcptString String
	 * @param aaRcptProperties aaRcptProperties
	 */
	public TestReceiptVehInquiry(
		String asRcptString,
		ReceiptProperties aaRcptProperties)
	{
		super(asRcptString, aaRcptProperties);
	}

	/**
	 * Format Receipt
	 * 
	 * @param avResults Vector
	 */
	public void formatReceipt(Vector avResults)
	{
		int liTransDataObj = 0;
		Vector lvRcptInfo = new Vector();

		// Parse TransData object
		CompleteTransactionData laTransData =
			(CompleteTransactionData) avResults.elementAt(
				liTransDataObj);
		MFVehicleData laMFVehData =
			(MFVehicleData) laTransData.getVehicleInfo();
		//OwnerData laOwnrInfo = (OwnerData) laMFVehData.getOwnerData();
		RegistrationData laRegData =
			(RegistrationData) laMFVehData.getRegData();
		TitleData laTitleData = (TitleData) laMFVehData.getTitleData();
		VehicleData laVehData =
			(VehicleData) laMFVehData.getVehicleData();

		IndicatorDescriptionsData laVehRmks =
			new IndicatorDescriptionsData();
		// This is the cache object

		try
		{
			generateReceiptHeader(avResults);
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.printStackTrace();
		}
		// ***************  VEHICLE INFORMATION  ***************
		lvRcptInfo =
			parseRegisInfoVehInq(laVehData, laRegData, laTitleData);
		printVehDataVehInq(lvRcptInfo);

		// ***************  VEHICLE NOTATIONS  ***************
		lvRcptInfo = parseVehNotation(laVehRmks);
		// This data object has not been defined
		printVehNotation(lvRcptInfo);

		// ***************  FEES INFORMATION  ***************
		//	printFeesData(lOwnrInfo);
		// Move down 5 line
		caRpt.blankLines(5);
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// empty code block
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * main
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class 
		ReceiptProperties laRcptProps = new ReceiptProperties();
		TestReceiptVehInquiry laGPR =
			new TestReceiptVehInquiry(
				"NOT USED FOR RECEIPTS",
				laRcptProps);

		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS_TBL";
		Vector lvQueryDTAPreliminary = laGPR.queryData(lsQuery);
		laGPR.formatReceipt(lvQueryDTAPreliminary);

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\RTS\\RCPT\\RECEIPT.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laGPR.getReceipt().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport =
				new FrmPreviewReport("c:\\RTS\\RCPT\\RECEIPT.RPT");
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
			// Process p = Runtime.getRuntime().exec("cmd.exe /c
			// copy c:\\QuickCtcRpt.txt prn");
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
	 * parseRegisInfoVehInq
	 * 
	 * @param aaVehData Object
	 * @param aaRegisData Object
	 * @param aaTitleData Object
	 * @return Vector
	 */
	private Vector parseRegisInfoVehInq(
		Object aaVehData,
		Object aaRegisData,
		Object aaTitleData)
	{

		Vector lvVehData = new Vector();

		VehicleData laVehData = (VehicleData) aaVehData;
		RegistrationData laRegData = (RegistrationData) aaRegisData;
		TitleData laTitleData = (TitleData) aaTitleData;

		int liVehModlYr = laVehData.getVehModlYr();
		String lsVehMk = laVehData.getVehMk();
		String lsYrMk = String.valueOf(liVehModlYr) + "/" + lsVehMk;

		String lsPrevExpMoYr =
			laRegData.getPrevExpMo() + "/" + laRegData.getPrevExpYr();

		String lsCityState =
			laTitleData.getPrevOwnrCity()
				+ ","
				+ laTitleData.getPrevOwnrState();

		// Add data to the vector
		lvVehData.addElement(laVehData.getVin());
		lvVehData.addElement(laVehData.getVehClassCd());
		lvVehData.addElement(lsYrMk);
		lvVehData.addElement(laVehData.getVehModl());
		lvVehData.addElement(laVehData.getVehBdyType());
		lvVehData.addElement(laTitleData.getVehUnitNo());
		lvVehData.addElement(
			new Integer(laRegData.getResComptCntyNo()));
		lvVehData.addElement(new Integer(laVehData.getVehEmptyWt()));
		lvVehData.addElement(new Integer(laRegData.getVehCaryngCap()));
		lvVehData.addElement(new Integer(laRegData.getVehGrossWt()));
		lvVehData.addElement(laVehData.getVehTon());
		lvVehData.addElement(laVehData.getTrlrType());
		lvVehData.addElement(laVehData.getVehBdyVin());
		lvVehData.addElement(new Integer(laVehData.getVehLngth()));
		lvVehData.addElement(laRegData.getTireTypeCd());
		lvVehData.addElement(new Integer(laRegData.getRegIssueDt()));
		lvVehData.addElement(laVehData.getVehOdmtrReadng());
		lvVehData.addElement(laVehData.getVehOdmtrBrnd());
		lvVehData.addElement(laRegData.getPrevPltNo());
		lvVehData.addElement(lsPrevExpMoYr);
		lvVehData.addElement(laTitleData.getPrevOwnrName());
		lvVehData.addElement(lsCityState);
		// defect 8901
		// lvVehData.addElement(new Integer(laRegData.getRegPltAge()));
		lvVehData.addElement(
			new Integer(laRegData.getRegPltAge(false)));
		// end defect 8901
		for (int i = 0; i < lvVehData.size(); i++)
		{
			if (lvVehData.elementAt(i) == null)
			{
				lvVehData.setElementAt("", i);
			} // end if
		} // end for loop

		return lvVehData;
	}

	/**
	 * Parse the vehicle record notations.
	 * 
	 * @param aaVehNotation Object
	 * @return Vector
	 */
	public Vector parseVehNotation(Object aaVehNotation)
	{
		Vector lvIndiVector = new Vector();

		// ********** Build  IndiDesc  data object **********     	
		// Set Indi Desc data
		// It has been determine here this information will be coming
		// from: 11/02/2001
		lvIndiVector.addElement("RELEASE OF PERSONAL INFO RESTRICTED");
		lvIndiVector.addElement("DPS-EMISSION PRGM NON-COMPLIANCE");
		lvIndiVector.addElement("EXEMPT");

		return lvIndiVector;
	}

	/**
	 * printVehDataVehInq
	 * 
	 * @param avVehdata Vector
	 */
	protected void printVehDataVehInq(Vector avVehdata)
	{
		int liVehInfoRow = 26;

		//ReceiptTemplate laAssitMethods = new ReceiptTemplate();
		// Common methods found in .reports.title

		// Set printer pointer
		for (int i = getCurrX(); i < liVehInfoRow; i++)
		{
			this.caRpt.blankLines(1);
		} // end for loop

		for (int i = 0; i < avVehdata.size(); i++)
		{
			switch (i)
			{
				case 0 : // Print VIN
					{
						printVIN(
							avVehdata.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 1 : // Print VehClassCd
					{
						printVehClassifcation(
							avVehdata.elementAt(i).toString(),
							ciCol_50);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 2 : // Print Year/Make
					{
						printVehYrMk(
							avVehdata.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 3 : // Print Model
					{
						printVehModl(
							avVehdata.elementAt(i).toString(),
							ciCol_20);
						break;
					}
				case 4 : // Print Body Style
					{
						printVehBdyStyle(
							avVehdata.elementAt(i).toString(),
							ciCol_33);
						break;
					}
				case 5 : // Print Unit number
					{
						printVehUnitNo(
							avVehdata.elementAt(i).toString(),
							ciCol_50);
						break;
					}
				case 6 : // Print Resident Compt County Number
					{
						printResCntyNo(
							avVehdata.elementAt(i).toString(),
							ciCol_68);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 7 : // Print Empty weight
					{
						printVehEmptyWt(
							avVehdata.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 8 : // Print Carrying capacity
					{
						printCarryCap(
							avVehdata.elementAt(i).toString(),
							ciCol_18);
						break;
					}
				case 9 : // Print Gross Weight
					{
						printVehGrossWt(
							avVehdata.elementAt(i).toString(),
							ciCol_45);
						break;
					}
				case 10 : // Print Tonnage
					{
						printVehTon(
							avVehdata.elementAt(i).toString(),
							ciCol_62);
						break;
					}
				case 11 : // Print Trailer Type
					{
						printTrlrType(
							avVehdata.elementAt(i).toString(),
							ciCol_80);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 12 : // Print Body VIN
					{
						printVehBdyVIN(
							avVehdata.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 13 : // Print Travel TRLR Length
					{
						printTrvlTrlrLngth(
							avVehdata.elementAt(i).toString(),
							ciCol_55);
						break;
					}
				case 14 : // Print Tire Type
					{
						printTireType(
							avVehdata.elementAt(i).toString(),
							ciCol_82);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 15 : // Print Registartion Issue Date
					{
						printRegIssueDt(
							avVehdata.elementAt(i).toString(),
							ciCol_01);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 16 : // Print Odometer Reading
					{
						printVehOdoRdng(
							avVehdata.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 17 : // Print Odometer Brand
					{
						printVehOdoBrnd(
							avVehdata.elementAt(i).toString(),
							ciCol_26);
						break;
					}
				case 18 : // Print Previous Plate No.
					{
						printPrevPltNo(
							avVehdata.elementAt(i).toString(),
							ciCol_35);
						break;
					}
				case 19 : // Print Previous Expirate Month/Year
					{
						printPrevExpMoYr(
							avVehdata.elementAt(i).toString(),
							ciCol_62);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 20 : // Print Previous Owner
					{
						printPrevOwnr(
							avVehdata.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 21 : // Print Previous City/State
					{
						printPrevCityState(
							avVehdata.elementAt(i).toString(),
							ciCol_43);
						break;
					}
				case 22 : // Print Plate Age
					{
						printPltAge(
							avVehdata.elementAt(i).toString(),
							ciCol_82);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				default :
					{
						System.out.println(
							"oops, drop out of the switch prematurely");
					}
			} // switch
		} // end for loop
	}

	/**
	 * Print Veh Notation
	 * 
	 * @param avVehnotatio Vector
	 */
	protected void printVehNotation(Vector avVehNotation)
	{
		int liVehInfoRow = 26;

		// ReceiptTemplate laAssitMethods = new ReceiptTemplate();
		// Common methods found in .reports.title

		// Set printer pointer
		for (int i = getCurrX(); i < liVehInfoRow; i++)
		{
			this.caRpt.blankLines(1);
		} // end for loop

		for (int i = 0; i < avVehNotation.size(); i++)
		{
			switch (i)
			{
				case 0 : // Print VehClassCd
					{
						printVehClassifcation(
							avVehNotation.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 1 : // Print PrevPltNo
					{
						printPrevPltNo(
							avVehNotation.elementAt(i).toString(),
							ciCol_39);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 2 : // Print VIN
					{
						printVIN(
							avVehNotation.elementAt(i).toString(),
							ciCol_01);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 3 : // Print Year/Make
					{
						printVehYrMk(
							avVehNotation.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 4 : // Print Model
					{
						printVehModl(
							avVehNotation.elementAt(i).toString(),
							ciCol_20);
						break;
					}
				case 5 : // Print Body Style
					{
						printVehBdyStyle(
							avVehNotation.elementAt(i).toString(),
							ciCol_33);
						break;
					}
				case 6 : // Print Unit number
					{
						printVehUnitNo(
							avVehNotation.elementAt(i).toString(),
							ciCol_50);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 7 : // Print Empty weight
					{
						printVehEmptyWt(
							avVehNotation.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 8 : // Print Carrying capacity
					{
						printCarryCap(
							avVehNotation.elementAt(i).toString(),
							ciCol_18);
						break;
					}
				case 9 : // Print Gross Weight
					{
						printVehGrossWt(
							avVehNotation.elementAt(i).toString(),
							ciCol_45);
						break;
					}
				case 10 : // Print Tonnage
					{
						printVehTon(
							avVehNotation.elementAt(i).toString(),
							ciCol_62);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 11 : // Print Body VIN
					{
						printVehBdyStyle(
							avVehNotation.elementAt(i).toString(),
							ciCol_01);
						break;
					}
				case 12 : // Print Unit number
					{
						printTrvlTrlrLngth(
							avVehNotation.elementAt(i).toString(),
							ciCol_57);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				default :
					{
						System.out.println(
							"oops, drop out of the switch prematurely");
					}
			} // switch
		} // end for loop
	}

	/**
	 * Query Data
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		Vector lvReceipt = new Vector();

		//Vehicle ctdObj = new Vehicle();
		CompleteTransactionData laTransData =
			new CompleteTransactionData();
		// defect 7896
		// change method call to be statically accessed
		laTransData = (CompleteTransactionData) Vehicle.getVeh();
		// end defect 7896

		lvReceipt.addElement(laTransData);

		return lvReceipt;
	}
}
