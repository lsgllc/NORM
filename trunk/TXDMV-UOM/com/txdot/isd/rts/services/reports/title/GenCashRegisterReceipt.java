package com.txdot.isd.rts.services.reports.title;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.FeesData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.data.TransactionPaymentData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.Print;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 * GenCashRegisterReceipt.java
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl   11/11/2001  New Class 
 * Ray Rowehl	12/06/2001	Add handling for TransId list
 * Ray Rowehl	12/09/2001	Still needs paging and positioning.
 * Ray Rowehl	01/22/2002	Fix Time format to use AM/PM.  
 *							(CQU100001010)
 *							Put a space before printing the
 *							Check Number
 *							(CQU100001122)
 * S Govindappa 05/02/2002  Fixed defect CQU100003772 by making
 * & Ray Rowehl 			changes in formatReceipt() to
 *              			print cash register from Tray2 and
 * 							get ride of certain characters
 *                          from printing in Top Left margin of
 * 							Receipt.
 * S Govindappa 05/09/2002  Fixed CQU100003836 by making changes
 * 							to TRANSIDCOLUMN2, TRANSIDCOLUMN3
 * 							and TRANSIDCOLUMN4 to reduce the
 * 							spaces between TransIDs
 * S Govindappa 07/10/2002  Merged PCR25 code by making changes
 * 							to printPayment method
 * MAbs	 		08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * S Johnston	05/23/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify printHeading()
 * 							printOfficeOrCountyName()
 * 							queryData()
 *							defect 7896 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3   
 * Min Wang	10/08/2010		Allow printing of the Max length of the price. 
 * 							modify ITEMPRICELENGTH  
 * 							defect 10596 Ver 6.6.0                  
 * ---------------------------------------------------------------------
 */
/**
 * This class generates the Cash Register Receipt.
 *
 * @version	6.6.0		10/08/2010
 * @author: Ray Rowehl
 * <br>Creation Date:	12/04/2001 07:33:33
 */
public class GenCashRegisterReceipt extends ReceiptTemplate
{
	private static final int COLUMN = 5;
	private static final int FIRSTCOLUMNPOSITION = COLUMN;
	private static final int SECONDCOLUMNPOSITION = COLUMN + 34;
	//private static final int THIRDCOLUMNPOSITION = COLUMN + 58;
	private static final int FEEASSESSEDPOSITION = COLUMN + 44;
	private static final int FEEASSESSEDLENGTH = 25;
	//private static final int AMTPOSITION = COLUMN + 62;
	private static final int TOTALPOSITION = SECONDCOLUMNPOSITION;
	private static final String TOTALSTRING = "TOTAL";
	//private static final int ITEMPRICEPOSITION = COLUMN + 65;
	// defect 10596
	//private static final int ITEMPRICELENGTH = 12;
	private static final int ITEMPRICELENGTH = 14;
	// end defect 10596
	private static final int DOLLARSIGNPOSITION = COLUMN + 55; //75;
	private static final int MONEYPOSITION = COLUMN + 55;
	//private static final int CRDTPOSITION = COLUMN + 53;
	//private static final int TRANSOBJPOSITION = 0;
	//private static final int TRANSIDLISTPOSITION = 1;
	private static final int TRANSIDCOLUMN1 = COLUMN;
	private static final int TRANSIDCOLUMN2 = COLUMN + 20;
	private static final int TRANSIDCOLUMN3 = COLUMN + 40;
	private static final int TRANSIDCOLUMN4 = COLUMN + 60;
	//private static final int PAGEBREAKPOINT = 60;
	//private static final String PLATENUMBERSTRING = "PLATE NO: ";
	private static final String TRANSTIMESTRING = "TIME: ";
	private static final String TRANSDATESTRING = "DATE: ";
	//private static final String EXPIRATIONDATESTRING =
	//	"EXPIRATION DATE: ";
	private static final String EMPLOYEESTRING = "EMPLOYEE ID: ";
	//private static final String TRANSIDSTRING = "TRANSACTION ID: ";
	//private static final String OWNERADDRSTRING =
	//	"OWNER NAME AND ADDRESS";
	//private static final String REGCLASSSTRING = "REGISTRATION CLASS: ";
	//private static final String PLATETYPESTRING = "PLATE TYPE: ";
	private static final String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	private static final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	private static final String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	//private static final String STICKERTYPESTRING = "STICKER TYPE: ";
	//private static final String PREVPLATESTRING = "PREVIOUS PLATE NO: ";
	//private static final String PERMITNOSTRING = "PERMIT NO: ";
	//private static final String REGCREDITSTRING =
	//"** RETAIN RECEIPT FOR CREDIT ON NEXT YEAR'S REGISTRATION.";
	//private static final String LARGECOUNTYSTRING = "L";
	private static final String DOLLARSIGN = "$";
	//private static final String SLASH = "/";
	private static final int PAYMENTPOSITION = SECONDCOLUMNPOSITION;
	//COLUMN + 55;
	//private static final int PAYMENTHEADERPOSITION = COLUMN + 50;
	//private static final String TONNAGESTRING = "TONNAGE: ";
	//private static final String YEARSTRING = "YR";
	//private static final int YEARPOSITION = COLUMN + 36;
	//private static final int YEARLENGTH = 4;
	private static final String FEEASSESSEDSTRING = "FEES ASSESSED";
	private static final String COUNTYSTRING = "COUNTY: ";
	private static final String OFFICESTRING = "OFFICE: ";
	private static final String TACSTRING = "TAC NAME: ";
	//private static final int COLUMNLENGTH = 34;
	//private static final int OWNRLENGTH = 40;
	//private static final int RENEWLENGTH = 40;
	private static final int LENGTH_1 = 1;
	boolean cbCreditFeeIncMsgIndi = true;

	/**
	 * GenRegistrationReceipts constructor
	 */
	public GenCashRegisterReceipt()
	{
		super();
	}

	/**
	 * GenRegistrationReceipts constructor
	 * 
	 * @param asRcptName String
	 * @param aaRcptProps ReceiptProperties
	 */
	public GenCashRegisterReceipt(
		String asRcptName,
		ReceiptProperties aaRcptProps)
	{
		super(asRcptName, aaRcptProps);
	}

	/**
	 * Get the object off of the DataVector and check to see it they are
	 * Payment Objects.  If they are, put them on the outgoing Payment
	 * Vector.
	 * 
	 * @param avQueryData Vector
	 * @return Vector
	 */
	public Vector buildPaymentVector(Vector avDataVector)
	{
		// create the Payment Vector
		Vector lvPaymentVector = new Vector();

		// counter for adding elements to Vector
		int liPaymentCounter = 0;

		for (int i = 0; i < avDataVector.size(); i++)
		{
			// pull the object out so we can inspect it
			Object laThisElement = avDataVector.elementAt(i);

			// inspect the object to see if it is a
			// TransactionPaymentData object
			if (laThisElement instanceof TransactionPaymentData)
			{
				lvPaymentVector.addElement(
					(TransactionPaymentData) avDataVector.elementAt(i));
				liPaymentCounter = liPaymentCounter + 1;
			}
		}
		// Send the new Payment Vector back.
		return lvPaymentVector;
	}

	/**
	 * This method generates the Registration Renewal Receipt.
	 * 
	 * @param lvDataVector Vector
	 */
	public void formatReceipt(Vector lvDataVector)
	{
		// Get the CompleteTransData object out of the vector

		// Print the Heading
		// defect 7896
		// removed argument laTransData b/c it was never read in method
		// CompleteTransactionData laTransData =
		//	(CompleteTransactionData) lvDataVector.elementAt(
		//		TRANSOBJPOSITION);
		printHeading();
		// end defect 7896

		// Process the TransId List
		// Use new TransIdList from Nancy
		printTransIdList(Transaction.getTransIdList());

		// print the total Fee and Payment Collected
		caRpt.blankLines(2);
		printPayment(lvDataVector);
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * This method runs this class in stand alone mode for testing.
	 * 
	 * @param aarrArgs String[]
	 * @throws RTSException
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		// load Cache Manager for stanalone testing
		// allows reading in of cache data
		CacheManager.loadCache();

		// Instantiating a new Report Class
		ReceiptProperties laRcptProps = new ReceiptProperties();
		GenCashRegisterReceipt laGCRR =
			new GenCashRegisterReceipt(
				"NOT USED FOR RECEIPTS",
				laRcptProps);

		// Generating Demo data to display.
		// String method get its data from a generated object
		// defect 7896 removed unused parameter ("") from method call
		Vector lvFakeDataVector = laGCRR.queryData(); //"");
		// end defect 7896
		try
		{
			laGCRR.formatReceipt(lvFakeDataVector);
		}
		catch (Exception aeEx)
		{
			System.out.println("format had a problem");
			aeEx.printStackTrace();
		}

		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\RECEIPT.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeEx)
		{
			// empty code block
		}

		laPout.print(laGCRR.caRpt.getReport().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\RECEIPT.RPT");
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
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec(
			// "cmd.exe /c copy c:\\RECEIPT.RPT prn");
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
	 * Print the Transaction Employee Id at the spot specified.
	 * 
	 * @param asEmpId String
	 * @param aiPostion int
	 */
	public void printEmpId(String asEmpId, int aiPosition)
	{
		caRpt.print(
			EMPLOYEESTRING,
			aiPosition,
			EMPLOYEESTRING.length());
		caRpt.print(asEmpId);
	}

	/**
	 * Print the Fee Total.
	 * 
	 * @param laTransData CompleteTransactionData
	 */
	public void printFeeTotal(CompleteTransactionData laTransData)
	{
		// May have to handle Diesel Fee and Penalty Fee
		// separately from the Fees Vector..

		// Look up the Item Codes description to print
		// ItemCodesCache laItemCodesCache = new ItemCodesCache();

		// cRpt.print(INVITMSTRING, FIRSTCOLUMNPOSITION,
		// INVITMSTRING.length());
		// cRpt.print(YEARSTRING, YEARPOSITION, YEARLENGTH);
		caRpt.print(
			FEEASSESSEDSTRING,
			FEEASSESSEDPOSITION,
			FEEASSESSEDSTRING.length());
		caRpt.blankLines(1);

		int liFeeCount = 0; // index through the Fees vector
		boolean lbKeepLooping = true;
		// loop controller.  keep looping until it is set off.
		Dollar laZeroDollar = new Dollar(0);
		Dollar laFeeTotal = new Dollar(0);

		while (lbKeepLooping)
		{
			if (laTransData.getRegFeesData().getVectFees() != null
				&& liFeeCount
					< laTransData.getRegFeesData().getVectFees().size())
			{
				FeesData laFeeData =
					(FeesData) laTransData
						.getRegFeesData()
						.getVectFees()
						.elementAt(
						liFeeCount);
				caRpt.print(
					laFeeData.getDesc(),
					FEEASSESSEDPOSITION,
					FEEASSESSEDLENGTH);
				caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
				// S447500 just prints the price.  assumes that qty is 1
				caRpt.rightAlign(
					laFeeData.getItemPrice().printDollar().substring(1),
					MONEYPOSITION,
					ITEMPRICELENGTH);
				liFeeCount = liFeeCount + 1;
				laFeeTotal = (laFeeTotal.add(laFeeData.getItemPrice()));
			}
			caRpt.blankLines(1);
			// if both vectors are finished turn off lbKeepLooping
			if ((laTransData.getRegFeesData().getVectFees() == null
				|| liFeeCount
					>= laTransData.getRegFeesData().getVectFees().size()))
			{
				lbKeepLooping = false;
			}
		}
		if (!laFeeTotal.equals(laZeroDollar))
		{
			caRpt.blankLines(1);
			caRpt.print(
				TOTALSTRING,
				TOTALPOSITION,
				TOTALSTRING.length());
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
			caRpt.rightAlign(
				laFeeTotal.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			caRpt.blankLines(1);
		}
	}

	/**
	 * Print the Heading for Cash Receipt.
	 * 
	 * @param laTransData CompleteTransactionData
	 */
	public void printHeading()//CompleteTransactionData laTransData)
	{
		// Set font for the heading
		// Print laPrint = new Print();
		// defect 7896
		// changed method call to be statically referenced
		String lsPageProperties =
			Print.getPRINT_TOP_MARGIN()
				+ Print.getPRINT_TRAY_2()
				+ Print.getPRINT_ELITE()
				+ Print.getPRINT_LINE_SPACING_1_8()
				+ " "
				+ Print.getPRINT_BOLD()
				+ " "
				+ Print.getPRINT_10CPI();
		// end defect 7896
		caRpt.printAttributesNoReturn(lsPageProperties);

		caRpt.print(
			"CASH REGISTER RECEIPT",
			FIRSTCOLUMNPOSITION,
			"CASH REGISTER RECEIPT".length());
		// defect 7896
		// changed method call to be statically referenced
		caRpt.printAttributes(
			Print.getPRINT_NORMAL() + Print.getPRINT_12CPI());
		caRpt.printAttributes(
			Print.getPRINT_ELITE() + Print.getPRINT_LINE_SPACING_1_8());
		// end defect 7896
		// skip down 5 lines from title of receipt 
		caRpt.blankLines(4);

		// Process Line 1
		// Print out the County Name and TAC
		// printOfficeOrCountyName(lTransData.getOfcIssuanceNo(),
		// lTransData.getTransCode());
		printOfficeOrCountyName(SystemProperty.getOfficeIssuanceNo());

		caRpt.blankLines(1);

		// Process Line 2
		// Print the transaction date
		// create a new RTSDate object to print the transaction Date
		// and Time.
		RTSDate laTransDate = new RTSDate();
		printTransDate(laTransDate, SECONDCOLUMNPOSITION);

		caRpt.blankLines(1);

		// Process Line 3
		// Print the transaction time
		printTransTime(laTransDate, SECONDCOLUMNPOSITION);

		caRpt.blankLines(1);

		// Process Line 4
		// Print Transaction Employee
		printEmpId(
			SystemProperty.getCurrentEmpId(),
			SECONDCOLUMNPOSITION);

		caRpt.blankLines(2);

		// Process Line 6
		// Print the Transaction Ids Line
		caRpt.print(
			"TRANSACTION IDS",
			SECONDCOLUMNPOSITION,
			"TRANSACTION IDS".length());

		caRpt.blankLines(2);
	}
	
	/**
	 * This method prints out the office name and tac string as
	 * appropriate.
	 * 
	 * @param aiOfficeNo int
	 */
	public void printOfficeOrCountyName(int aiOfficeNo)
	{
		// get Office Id Information
		// OfficeIdsCache laCacheReader = new OfficeIdsCache();
		// defect 7896
		// changed method call to be statically referenced
		OfficeIdsData laOfficeInfo =
			OfficeIdsCache.getOfcId(aiOfficeNo);
		// end defect 7896
		String lsOfcOrCounty = "";
		String lsTacName = "";
		if (aiOfficeNo < 260)
		{
			lsOfcOrCounty = COUNTYSTRING;
			lsTacName = TACSTRING + laOfficeInfo.getTacName();
		}
		else
		{
			lsOfcOrCounty = OFFICESTRING;
		}
		caRpt.print(
			lsOfcOrCounty + laOfficeInfo.getOfcName(),
			COLUMN,
			(lsOfcOrCounty + laOfficeInfo.getOfcName()).length());
		caRpt.print(
			lsTacName,
			SECONDCOLUMNPOSITION,
			lsTacName.length());
	}
	
	/**
	 * Print the Payment Infomation.
	 * 
	 * @param avDataVector Vector
	 */
	public void printPayment(Vector avDataVector)
	{
		// build up the Payment Vector
		Vector lvPaymentData = buildPaymentVector(avDataVector);

		Dollar laFeeTotal = Transaction.getRunningSubtotal();
		Dollar laZeroDollar = new Dollar(0);
		Dollar laPaymentTotal = new Dollar(0);
		Dollar laChangeBackAmt = new Dollar(0);
		//int liChangeBackType = 0;

		// Compute the total Fee to be paid for.
		for (int i = 0; i < lvPaymentData.size(); i++)
		{
			//TransactionPaymentData laPaymentData =
			//	(TransactionPaymentData) lvPaymentData.elementAt(i);
		}

		// Print the Total Fees Due Line
		caRpt.print(
			TOTALSTRING,
			TOTALPOSITION,
			PAYMENTTOTALSTRING.length());
		caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
		caRpt.rightAlign(
			laFeeTotal.printDollar().substring(1),
			MONEYPOSITION,
			ITEMPRICELENGTH);
		caRpt.blankLines(2);
		caRpt.print(
			PAYMENTSTRING,
			SECONDCOLUMNPOSITION - 10,
			PAYMENTSTRING.length());
		caRpt.nextLine();

		for (int i = 0; i < lvPaymentData.size(); i++)
		{
			TransactionPaymentData laPaymentData =
				(TransactionPaymentData) lvPaymentData.elementAt(i);
			if (laPaymentData.getPymntTypeAmt().equals(laZeroDollar))
			{
				caRpt.nextLine();
				break;
			}
			caRpt.print(
				laPaymentData.getPymntType(),
				PAYMENTPOSITION,
				laPaymentData.getPymntType().length());
			// print the check number if it exists
			if (laPaymentData.getPymntCkNo() != null)
			{
				caRpt.print(" #" + laPaymentData.getPymntCkNo());
			}
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
			caRpt.rightAlign(
				laPaymentData
					.getPymntTypeAmt()
					.printDollar()
					.substring(
					1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			//Code for PCR25
			//String lsPymntType = laPaymentData.getPymntType();
			if (cbCreditFeeIncMsgIndi
				&& laPaymentData.isCreditCardFee())
			{
				cbCreditFeeIncMsgIndi = false;
				String lsCreditCardFeeMessage =
					formatCreditCardFeeMessage();
				if (lsCreditCardFeeMessage != null)
				{
					// Move down 1 lines
					caRpt.blankLines(1);
					caRpt.rightAlign(
						lsCreditCardFeeMessage,
						MONEYPOSITION - 5,
						lsCreditCardFeeMessage.length());
				}
			}
			laPaymentTotal =
				(laPaymentTotal.add(laPaymentData.getPymntTypeAmt()));
			if (laPaymentData.getChngDue() != null
				&& !laPaymentData.getChngDue().equals(laZeroDollar))
			{
				laChangeBackAmt = laPaymentData.getChngDue();
				//liChangeBackType = laPaymentData.getChngDuePymntType();
			}
			caRpt.nextLine();
		}
		// Print the Total Payment Line
		caRpt.nextLine();
		caRpt.print(
			PAYMENTTOTALSTRING,
			PAYMENTPOSITION,
			PAYMENTTOTALSTRING.length());
		caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
		caRpt.rightAlign(
			laPaymentTotal.printDollar().substring(1),
			MONEYPOSITION,
			ITEMPRICELENGTH);
		caRpt.nextLine();
		// Print out the Change Due Line
		if (!laChangeBackAmt.equals(laZeroDollar))
		{
			caRpt.print(
				PAYMENTCHANGEDUESTRING,
				PAYMENTPOSITION,
				PAYMENTCHANGEDUESTRING.length());
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
			caRpt.rightAlign(
				laChangeBackAmt.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			caRpt.nextLine();
		}
	}
	
	/**
	 * Print the Transaction Date at the spot specified.
	 * 
	 * @param aaTransDate RTSDate
	 * @param aiPosition int
	 */
	public void printTransDate(RTSDate aaTransDate, int aiPosition)
	{
		caRpt.print(
			TRANSDATESTRING,
			aiPosition,
			TRANSDATESTRING.length());
		caRpt.print(aaTransDate.toString());
	}
	
	/**
	 * Print the Transaction Id at the spot specified.
	 * 
	 * @param asTransId String
	 * @param aiPostion int
	 */
	public void printTransId(String asTransId, int aiPosition)
	{
		caRpt.print(asTransId, aiPosition, asTransId.length());
	}
	
	/**
	 * Process through the list of TransIds.
	 * 
	 * @param avTransIds Vector
	 */
	public void printTransIdList(Vector avTransIds)
	{
		// counter to control the case stmt
		int liCaseControl = 1;
		if (caRpt.getCurrX() > 60)
		{
			caRpt.newPage();
		}
		// for loop to process through the vector
		for (int i = 0; i < avTransIds.size(); i++)
		{
			switch (liCaseControl)
			{
				// print at position 1
				case 1 :
					{
						caRpt.nextLine();
						printTransId(
							(String) avTransIds.elementAt(i),
							TRANSIDCOLUMN1);
						liCaseControl = liCaseControl + 1;
						break;
					}
					// print at position 2
				case 2 :
					{
						printTransId(
							(String) avTransIds.elementAt(i),
							TRANSIDCOLUMN2);
						liCaseControl = liCaseControl + 1;
						break;
					}
					// print at position 3
				case 3 :
					{
						printTransId(
							(String) avTransIds.elementAt(i),
							TRANSIDCOLUMN3);
						liCaseControl = liCaseControl + 1;
						break;
					}
					// print at position 4
				case 4 :
					{
						printTransId(
							(String) avTransIds.elementAt(i),
							TRANSIDCOLUMN4);
						liCaseControl = 1;
						break;
					}
					// just print it at column 1
					// assume it is the first one 
				default :
					{
						caRpt.nextLine();
						printTransId(
							(String) avTransIds.elementAt(i),
							TRANSIDCOLUMN1);
						liCaseControl = 1;
					}
			}
		}
	}
	
	/**
	 * Print the Transaction Time at the spot specified.
	 * 
	 * @param aaTransDate RTSDate
	 * @param aiPostion int
	 */
	public void printTransTime(RTSDate aaTransDate, int aiPosition)
	{
		// print the trans time
		caRpt.print(
			TRANSTIMESTRING,
			aiPosition,
			TRANSTIMESTRING.length());
		caRpt.print(aaTransDate.getTime());
	}
	
	/**
	 * This version of queryData returns the CompleteTransactionData
	 * object.
	 *   
	 * <br>defect 7896 - removed unused parameter String lsQuery from 
	 * method call
	 * <br>// param lsQuery String
	 * <br>end defect 7896
	 * @return Vector
	 */
	public Vector queryData()
	{
		Vector lvReceipt = new Vector();

		//Vehicle laCTDObj = new Vehicle();
		CompleteTransactionData laTransData =
			new CompleteTransactionData();
			
		// defect 7896
		// changed method call to be statically referenced
		laTransData = (CompleteTransactionData) Vehicle.getVeh();
		// end defect 7896
		if (laTransData.getTransId() == null
			|| laTransData.getTransId().length() < 1)
		{
			laTransData.setTransId("17010037205111155");
		}
		// add the object to the big vector
		lvReceipt.addElement(laTransData);
		
		// create the transid vector
		Vector lvTransIdList = new Vector();
		lvTransIdList.addElement("17010037205111155");
		lvTransIdList.addElement("17010037205111155");
		lvTransIdList.addElement("17010037205111155");
		lvTransIdList.addElement("17010037205111155");
		
		// Add the transid vector to the big vector
		lvReceipt.addElement(lvTransIdList);
		
		// Add a Payment object to the big vector
		TransactionPaymentData laPymntData1 =
			new TransactionPaymentData();
		laPymntData1.setCashWsId(100);
		laPymntData1.setChngDue(new Dollar(3.01));
		laPymntData1.setOfcIssuanceNo(170);
		laPymntData1.setPymntCkNo("1111");
		laPymntData1.setPymntTypeAmt(new Dollar(40.00));
		laPymntData1.setTransAMDate(37204);
		laPymntData1.setTransWsId(100);
		laPymntData1.setTransEmpId("RROWEHL");
		laPymntData1.setChngDuePymntTypeCd(1);
		laPymntData1.setPymntType("Check #");
		laPymntData1.setPymntTypeCd(4);
		lvReceipt.addElement(laPymntData1);
		return lvReceipt;
	}
}
