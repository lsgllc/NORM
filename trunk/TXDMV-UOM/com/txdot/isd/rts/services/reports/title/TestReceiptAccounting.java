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
import com.txdot.isd.rts.services.util.Dollar;

/*
 *
 * TestReceiptAccounting.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/01/2001	Methods created
 * J Rue      	09/19/2001	Added comments
 * S Johnston	03/16/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify printFeesData(), main(),
 * 							queryData()
 *							defect 7896 Ver 5.2.3
 * K Harrell	05/12/2005 	refactoring of printTrlvTrlrLngth() to 
 * 							printTrvlTrlrLngth() in ReceiptTemplate
 * 							defect 7562 Ver 5.2.3
 * K Harrell	05/22/2005	Java 1.4 Work; Constant rename from Receipt
 * 							Template. Modified to use the column 
 * 							position constants from ReceiptTemplate 
 * 							deprecate printFeesData
 * 							defect 7896 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3  
 * K Harrell	07/10/2009	delete printFeesData(), CHAR_LNGTH_12
 * 							defect 10112 Ver Defect_POS_F                    	  
 * ---------------------------------------------------------------------
 */
/**
 * @version	Defect_POS_F	07/10/2009
 * @author	Jeff Rue
 * <br>Creation Date:		12/18/2001 11:13:45
 */
public class TestReceiptAccounting extends ReceiptTemplate
{
	private final static int COL_01 = 1;
	
	/**
	 * Test Receipt Accounting
	 * 
	 */
	public TestReceiptAccounting()
	{
		// empty code block
	}
	/**
	 * Test Receipt Accounting
	 * 
	 * @param asRcptString String
	 * @param aaRcptProperties ReceiptProperties
	 */
	public TestReceiptAccounting(
		String asRcptString,
		ReceiptProperties aaRcptProperties)
	{
		super(asRcptString, aaRcptProperties);
	}
	/**
	 * This abstract method must be implemented in all subclasses
	 * 
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
		OwnerData laOwnrInfo = (OwnerData) laMFVehData.getOwnerData();
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
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}

		// ***************  VEHICLE INFORMATION  ***************
		lvRcptInfo =
			parseRegisInfoAcct(laVehData, laRegData, laTitleData);
		printVehDataAcct(lvRcptInfo);

		// ***************  FEES INFORMATION  ***************
		printFeesData(laOwnrInfo);
 
		caRpt.blankLines(1);

		// ***************  VEHICLE NOTATIONS  ***************
		lvRcptInfo = parseVehNotation(laVehRmks);

		printVehNotation(lvRcptInfo);

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
	 * 
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
		TestReceiptAccounting laGPR =
			new TestReceiptAccounting(
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
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
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
			laFrmPreviewReport.setVisible(true);
			// end defect 7590
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec("cmd.exe /c
			// copy c:\\QuickCtcRpt.txt prn");

		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() "
					+ "of com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}

	}
	/**
	 * Insert the method's description here.
	 * 
	 * @param aaVehData Object
	 * @param aaRegisData Object
	 * @param aaTitleData Object
	 * @return String
	 */
	private Vector parseRegisInfoAcct(
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

		// Add data to the vector
		lvVehData.addElement(laVehData.getVehClassCd());
		lvVehData.addElement(laRegData.getPrevPltNo());
		lvVehData.addElement(laVehData.getVin());
		lvVehData.addElement(lsYrMk);
		lvVehData.addElement(laVehData.getVehModl());
		lvVehData.addElement(laVehData.getVehBdyType());
		lvVehData.addElement(laTitleData.getVehUnitNo());
		lvVehData.addElement(new Integer(laVehData.getVehEmptyWt()));
		lvVehData.addElement(new Integer(laRegData.getVehCaryngCap()));
		lvVehData.addElement(new Integer(laRegData.getVehGrossWt()));
		lvVehData.addElement(laVehData.getVehTon());
		lvVehData.addElement(laVehData.getVehBdyVin());
		lvVehData.addElement(new Integer(laVehData.getVehLngth()));

		// Use for testing
		/*	lvVehData.addElement("PASS");	 VEHICLE CLASSIFICATION
			lvVehData.addElement("AAAMMMM");	 PREVIOUS PLATE
			lvVehData.addElement("1G6KS5187HU809801");	 VIN
			lvVehData.addElement("1983/CADI");		 	 YEAR/MAKE
			lvVehData.addElement("SEV");				 MODEL
			lvVehData.addElement("4D");					 BODY STYLE
			lvVehData.addElement(null);					 UNIT NUMBER	
			lvVehData.addElement(new Integer(3500));	 EMPTY WEIGHT
			lvVehData.addElement(new Integer(0));	 CARRING CAPACITY
			lvVehData.addElement(new Integer(3500));	 GROSS WEIGHT
			lvVehData.addElement(ldVehTonTest);			 TONNAGE
			lvVehData.addElement(null);				 VEHICLE BODY VIN
			lvVehData.addElement(new Integer(0)); TRAVEL TRAILER LENGTH
		*/
		for (int i = 0; i < lvVehData.size(); i++)
		{
			if (lvVehData.elementAt(i) == null)
			{
				lvVehData.setElementAt("", i);
			} 
		} 

		return lvVehData;
	}
	
	/**
	 * Parse the vehicle record notations.
	 * 
	 * @param aaVehNotation Object
	 */
	public Vector parseVehNotation(Object aaVehNotation)
	{
		Vector lvIndiVector = new Vector();

		// ********** Build  IndiDesc  data object **********     	
		// Set Indi Desc data
		// It has been determine here this information will be coming
		// from: 11/02/2001
		lvIndiVector.addElement("RELEASE OF PERSONAL INFO RESTRICTED");
		lvIndiVector.addElement("EXEMPT");
		lvIndiVector.addElement("ACTUAL MILEAGE");

		return lvIndiVector;

	}
	/**
	 * printFeesData
	 * 
	 * @param aaOwnrTtlInfo Object
	 */
	private void printFeesData(Object aaOwnrTtlInfo)
	{
		String lsTransCd = "HOTCK";
		String lsFeesHeading = "";
		String lsTtlOwnrName = "JIM'S TEST RECORD";
		Dollar laAmt = new Dollar("-5.00");
		ReceiptTemplate laAssitMethods = new ReceiptTemplate();

		try
		{
			Vector lvFeesInfo = new Vector();

	 
			caRpt.blankLines(1);

			// Print Customer Name if REFUND
			// defect 7896
			// changed method call to be referenced in a static way
			if (lsTransCd.equals(ReceiptTemplate.TRANSCD_REFUND))
				// end defect 7896
			{
				lsTtlOwnrName = CUST_NAME_TEXT + lsTtlOwnrName;
				caRpt.print(
					lsTtlOwnrName,
					COL_49,
					laAssitMethods.charLength(lsTtlOwnrName));
				
				caRpt.blankLines(2);
			} // end if

			// Print the Fees heading
			lsFeesHeading = generaterFeesHeader(lsTransCd);
			lvFeesInfo.addElement(lsFeesHeading);
			caRpt.print(
				lsFeesHeading,
				COL_49,
				laAssitMethods.charLength(lsFeesHeading));
	 
			caRpt.blankLines(1);

			// Print account item codes and fees to the vector,
			// this will be in a loop
			printFees("REF-CNTY R & B ADD-ON FEE", laAmt);
	 
			caRpt.blankLines(1);
			printFees("TRANSFER FEE", laAmt);
			

			caRpt.blankLines(2);

			printFeeTotal();
			
			caRpt.blankLines(2);

			// Print Refund type: CASH or CHECK
			// defect 7896
			// changed method call to be referenced in a static way
			if (lsTransCd.equals(ReceiptTemplate.TRANSCD_REFUND))
				// end defect 7896
			{
				caRpt.print(
					RF_CHECK_TEXT,
					COL_68,
					laAssitMethods.charLength(RF_CHECK_TEXT));
				caRpt.blankLines(1);
			} 
			// defect 7896
			// changed method call to be referenced in a static way
			else if (lsTransCd.equals(ReceiptTemplate.TRANSCD_RFCASH))
			// end defect 7896
			{
				caRpt.print(
					RF_CASH_TEXT,
					COL_68,
					laAssitMethods.charLength(RF_CASH_TEXT));
				caRpt.blankLines(1);
			} 

		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leEx = new RTSException("", aeNPEx);
		}
	}
//	/**
//	 * Insert the method's description here.
//	 *
//	 * @param aaOwnrTtlInfo Object
//	 * @param asTransCd String
//	 * @deprecated
//	 */
//	private void printFeesData(Object aaOwnrTtlInfo, String asTransCd)
//	{
//		String lsTtlOwnrName;
//		String lsFeesHeading = "";
//		Dollar laAmt = new Dollar("-5.00");
//		Dollar laTotalAmt = new Dollar("0.00");
//		ReceiptTemplate laAssitMethods = new ReceiptTemplate();
//
//		try
//		{
//			Vector lvFeesInfo = new Vector();
//			OwnerData laOwnrInfo = (OwnerData) aaOwnrTtlInfo;
//
//	 
//			caRpt.blankLines(1);
//
//			// Print Customer Name if REFUND
//			// defect 7896
//			// changed method call to be referenced statically
//			if (asTransCd.equals(ReceiptTemplate.TRANSCD_REFUND))
//				// end defect 7896
//			{
//				// defect 10112 
//				lsTtlOwnrName =
//					CUST_NAME_TEXT + laOwnrInfo.getName1();
//				// end defect 10112 
//				
//				caRpt.print(
//					lsTtlOwnrName,
//					COL_49,
//					laAssitMethods.charLength(lsTtlOwnrName));
//				
//				caRpt.blankLines(2);
//			} // end if
//
//			// Print the Fees heading
//			lsFeesHeading = generaterFeesHeader(asTransCd);
//			lvFeesInfo.addElement(lsFeesHeading);
//			caRpt.print(
//				lsFeesHeading,
//				COL_49,
//				laAssitMethods.charLength(lsFeesHeading));
//	 
//			caRpt.blankLines(1);
//
//			// Print account item codes and fees to the vector,
//			// this will be in a loop
//			caRpt.print(
//				"REF-CNTY R & B ADD-ON FEE",
//				COL_49,
//				laAssitMethods.charLength("REF-CNTY R & B ADD-ON FEE"));
//			caRpt.print(
//				DOLLAR_SIGN,
//				COL_80,
//				laAssitMethods.charLength(DOLLAR_SIGN));
//			caRpt.rightAlign(
//				laAmt.toString(),
//				COL_81,
//				CHAR_LNGTH_12);
//			
//			caRpt.blankLines(2);
//
//			laTotalAmt = laTotalAmt.add(laAmt);
//			caRpt.print(
//				TOTAL_TEXT,
//				COL_68,
//				laAssitMethods.charLength(TOTAL_TEXT));
//			caRpt.print(
//				DOLLAR_SIGN,
//				COL_80,
//				laAssitMethods.charLength(DOLLAR_SIGN));
//			caRpt.rightAlign(
//				laTotalAmt.toString(),
//				COL_81,
//				CHAR_LNGTH_12);
//			
//			caRpt.blankLines(2);
//
//			// Print Refund type: CASH or CHECK
//			// defect 7896
//			// change method call to be accessed statically
//			if (asTransCd.equals(ReceiptTemplate.TRANSCD_REFUND))
//				// end defect 7896
//			{
//				caRpt.print(
//					RF_CHECK_TEXT,
//					COL_68,
//					laAssitMethods.charLength(RF_CHECK_TEXT));
//				caRpt.blankLines(1);
//			} 
//			// defect 7896
//			// change method call to be statically accessed
//			else if (asTransCd.equals(ReceiptTemplate.TRANSCD_RFCASH))
//				// end defect 7896
//			{
//				caRpt.print(
//					RF_CASH_TEXT,
//					COL_68,
//					laAssitMethods.charLength(RF_CASH_TEXT));
//				caRpt.blankLines(1);
//			} 
//
//		}
//		catch (NullPointerException leNPEx)
//		{
//			RTSException leRTSEx = new RTSException("", leNPEx);
//		}
//	}
	/**
	 * Print Vehicle Data Account
	 * 
	 * @param avVehdata Vector
	 */
	public void printVehDataAcct(Vector avVehdata)
	{
		int liVehInfoRow = 26;

		// Set printer pointer
		for (int i = getCurrX(); i < liVehInfoRow; i++)
		{
			this.caRpt.blankLines(1);
		} 
		
		for (int i = 0; i < avVehdata.size(); i++)
		{
			switch (i)
			{
				case 0 : // Print VehClassCd
					{
						printVehClassifcation(
							avVehdata.elementAt(i).toString(),
							COL_01);
						break;
					}
				case 1 : // Print PrevPltNo
					{
						printPrevPltNo(
							avVehdata.elementAt(i).toString(),
							COL_39);
						
						caRpt.blankLines(1);
						break;
					}
				case 2 : // Print VIN
					{
						printVIN(
							avVehdata.elementAt(i).toString(),
							COL_01);
						
						caRpt.blankLines(1);
						break;
					}
				case 3 : // Print Year/Make
					{
						printVehYrMk(
							avVehdata.elementAt(i).toString(),
							COL_01);
						break;
					}
				case 4 : // Print Model
					{
						printVehModl(
							avVehdata.elementAt(i).toString(),
							COL_20);
						break;
					}
				case 5 : // Print Body Style
					{
						printVehBdyStyle(
							avVehdata.elementAt(i).toString(),
							COL_33);
						break;
					}
				case 6 : // Print Unit number
					{
						printVehUnitNo(
							avVehdata.elementAt(i).toString(),
							COL_50);
						
						caRpt.blankLines(1);
						break;
					}
				case 7 : // Print Empty weight
					{
						printVehEmptyWt(
							avVehdata.elementAt(i).toString(),
							COL_01);
						break;
					}
				case 8 : // Print Carrying capacity
					{
						printCarryCap(
							avVehdata.elementAt(i).toString(),
							COL_18);
						break;
					}
				case 9 : // Print Gross Weight
					{
						printVehGrossWt(
							avVehdata.elementAt(i).toString(),
							COL_45);
						break;
					}
				case 10 : // Print Tonnage
					{
						printVehTon(
							avVehdata.elementAt(i).toString(),
							COL_62);
						
						caRpt.blankLines(1);
						break;
					}
				case 11 : // Print Body VIN
					{
						printVehBdyStyle(
							avVehdata.elementAt(i).toString(),
							COL_01);
						break;
					}
				case 12 : // Print Unit number
					{
						printTrvlTrlrLngth(
							avVehdata.elementAt(i).toString(),
							COL_57);
						
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
		// change method call to be statically referenced
		laTransData = (CompleteTransactionData) Vehicle.getVeh();
		// end defect 7896

		lvReceipt.addElement(laTransData);

		return lvReceipt;
	}
}
