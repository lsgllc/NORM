package com.txdot.isd.rts.services.reports.miscellaneous;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.CountyCalendarYearCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;
import com.txdot.isd.rts.services.util.Dollar;

/*
 * GenTempAdditionalWeightReceipts.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang  	12/05/2001  New Class
 * Min Wang		05/03/2002 	Using buildPaymentVector()
 *							from ReceiptTemplate.
 * J Rue		06/14/2002	Defect 4287, "COMERICAL" is mis-spell 
 * 							for COMMERCIALVEHICLESTRING.
 * S Govindappa 07/10/2002  Merged PCR25 code.  
 * 							modify printNotationsAndPayment()
 * MAbs	     	08/28/2002	PCR 41
 * MAbs		 	09/17/2002	PCR 41 Integration
 * B Brown  	02/12/2003  Changed the FEEASSESSEDLENGTH = 28.
 * 							defect 5298 
 * K Harrell	03/21/2004	5.2.0 Merge.  
 * 							Modified variable constants to private final 
 * 							static definitions.
 * 							Ver 5.2.0
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * K Harrell	09/15/2005	"Temporary" misspelled; 
 * 							Corrected in constant TEMPGROSSWTSTRING.  
 * 							defect 8223 Ver 5.2.3
 * B Hargrove	04/07/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify queryData()
 * 							defect 9631 Ver Defect POS A
 * K Harrell	08/05/2009	Removed reference to CompleteTransactionData
 * 							 ciOwnrSuppliedStkrAge  (removed) 
 * 							modify queryData() 
 * 							defect 10112 Ver Defect_POS_F 
 * Min Wang	10/08/2010		Allow printing of the Max length of the price. 
 * 							modify ITEMPRICELENGTH  
 * 							defect 10596 Ver 6.6.0   
 * ---------------------------------------------------------------------
 */
/**
 * This class generates all Temporary Additional Weight Receipts.
 * 
 * @version	6.6.0			10/08/2010
 * @author	Min Wang
 * <br>Creation Date:		11/12/2001 
 */
public class GenTempAdditionalWeightReceipts extends ReceiptTemplate
{
	boolean cbCreditFeeIncMsgIndi = true;

	private final static int FIRSTCOLUMNPOSITION = 5;
	private final static int UNITPOSITION = 60;
	private final static int MODELPOSITION = 27;
	private final static int BDYSTYLEPOSITION = 40;
	private final static int CARYCAPPOSITION = 24;
	private final static int GROSSWTPOSITION = 51;
	private final static int TONNAGEPOSITION = 68;
	private final static int TEMPGROSSWTPOSITION = 44;
	private final static int FEEASSESSEDPOSITION = 47;
	// defect 5298
	private final static int FEEASSESSEDLENGTH = 28;
	// end defect 5298 
	// defect 10596
	// private final static int ITEMPRICELENGTH = 12;
	private final static int ITEMPRICELENGTH = 14;
	// end defect 10596
	private final static int TOTALPOSITION = 61;
	private final static int DOLLARSIGNPOSITION = 80;
	private final static int PAYMENTHEADERPOSITION = 55;
	private final static int PAYMENTPOSITION = 60;
	private final static int MONEYPOSITION = 81;
	private final static int TRANSOBJPOSITION = 0;
	private final static int LENGTH1 = 1;

	private final static String VEHCLASSSTRING =
		"VEHICLE CLASSIFICATION: ";
	private final static String VINSTRING =
		"VEHICLE IDENTIFICATION NO: ";
	private final static String UNITSTRING = "UNIT NO: ";
	private final static String YRMAKESTRING = "YR/MAKE: ";
	private final static String MODELSTRING = "MODEL: ";
	private final static String BDYSTYLESTRING = "BODY STYLE: ";
	private final static String EMPTYWTSTRING = "EMPTY WT: ";
	private final static String CARYCAPSTRING = "CARRYING CAPACITY: ";
	private final static String GROSSWTSTRING = "GROSS WT: ";
	private final static String TONNAGESTRING = "TONNAGE: ";
	private final static String TEMPCARYCAPSTRING =
		"TEMPORARY CARRYING CAPACITY: ";
	// defect 8223 	
	private final static String TEMPGROSSWTSTRING =
		"TEMPORARY GROSS WEIGHT: ";
	// end defect 8223 
	private final static String FEEASSESSEDSTRING = "FEES ASSESSED";
	private final static String TOTALSTRING = "TOTAL";
	private final static String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT";
	private final static String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	private final static String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	private final static String REGCREDITSTRING =
		"** RETAIN RECEIPT FOR CREDIT ON NEXT YEAR'S REGISTRATION.";
	private final static String VEHISSUEDSTRING =
		"THIS RECEIPT TO BE CARRIED IN THE VEHICLE FOR WHICH ISSUED.";
	private final static String VEHNOTATIONSTRING =
		"VEHICLE RECORD NOTATIONS";
	private final static String AUTOMATION_1_STRING =
		"Current law requires an additional $1.00 fee (already";
	private final static String AUTOMATION_2_STRING =
		"included) in counties with 50,000 or more vehicles.";
	private final static String COMMERCIALVEHICLESTRING =
		"THIS RECEIPT TO BE CARRIED IN ALL COMMERCIAL VEHICLES.";
	private final static String LARGECOUNTYSTRING = "L";
	private final static String DOLLARSIGN = "$";
	private final static String SLASH = "/";

	/**
	 * GenTempAdditionalWeightReceipts constructor
	 */
	public GenTempAdditionalWeightReceipts()
	{
		super();
	}

	/**
	 * GenTempAdditionalWeightReceipts constructor
	 * 
	 * @param asRcptName String
	 * @param aaRcptProps ReceiptProperties
	 */
	public GenTempAdditionalWeightReceipts(
		String asRcptName,
		ReceiptProperties aaRcptProps)
	{
		super(asRcptName, aaRcptProps);
	}

	/**
	 * Add element to vector if it is appropriate.
	 * 
	 * @param aaIndicatorCode IndicatorDescriptionsData
	 * @param avNotationsVector Vector
	 * @return int - Vector size
	 */
	public int addToNotations(
		IndicatorDescriptionsData aaIndicatorCode,
		Vector avNotationsVector)
	{
		if (aaIndicatorCode.getIndiRcptPriority() > 0)
		{
			avNotationsVector.addElement(aaIndicatorCode);
		}
		return avNotationsVector.size();
	}

	/**
	 * This method generates the Registration Renewal Receipt.
	 * 
	 * @param avDataVector Vector
	 */
	public void formatReceipt(Vector avDataVector)
	{
		// Get the CompleteTransData object out of the vector
		CompleteTransactionData laTransData =
			(CompleteTransactionData) avDataVector.elementAt(
				TRANSOBJPOSITION);
		// Get the Transid off of the Vector
		// Get the first Payment object off of the Vector
		// TransactionPaymentData lTransactionPaymentData = 
		// (TransactionPaymentData) lvDataVector.elementAt(2);
		// System data for logon and substation Id
		try
		{
			// call generateHeader;
			generateReceiptHeader(avDataVector);
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println("Had a problem with Heading Lookup!");
			aeRTSEx.printStackTrace();
		}
		// skip down 5 lines from title of receipt
		//	cRpt.blankLines(5);
		// Process the Vehicle Information
		caRpt.blankLines(15 - caRpt.getCurrX());
		printVehicleData(laTransData);
		// Process Inventory Items and Fees
		printFees(laTransData);
		// Process Notations and Payment
		printNotationsAndPayment(laTransData, avDataVector);
		// Process Messages
		caRpt.blankLines(66 - caRpt.getCurrX());
		printMessages(laTransData);
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
	 * @param  aarrArgs String[] of command line arguments
	 * @throws RTSException
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		// load Cache Manager for stanalone testing
		// allows reading in of cache data
		CacheManager.loadCache();
		// Instantiating a new Report Class
		ReceiptProperties laRcptProps = new ReceiptProperties();
		GenTempAdditionalWeightReceipts laGTAWR =
			new GenTempAdditionalWeightReceipts(
				"NOT USED FOR RECEIPTS",
				laRcptProps);
		// Generating Demo data to display.
		// String method get its data from a generated object
		Vector lvFakeDataVector = laGTAWR.queryData1();
		try
		{
			laGTAWR.formatReceipt(lvFakeDataVector);
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
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		laPout.print(laGTAWR.caRpt.getReport().toString());
		laPout.close();
		// Display the report
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
			// defect 7590
			// change setVisible to setVisibleRTS
			laFrmPreviewReport.setVisibleRTS(true);
			// end defect 7590
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec(
			// "cmd.exe /c copy c:\\QuickCtrRpt.txt prn");
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
	 * Print the Inventory and Fees Areas.
	 * 
	 * @param aaTransData CompleteTransactionData
	 */
	public void printFees(CompleteTransactionData aaTransData)
	{
		// Look up the Item Codes description to print
		caRpt.print(
			FEEASSESSEDSTRING,
			FEEASSESSEDPOSITION,
			FEEASSESSEDSTRING.length());
		caRpt.nextLine();
		Dollar laZeroDollar = new Dollar(0);
		Dollar laFeeTotal = new Dollar(0);
		for (int i = 0;
			i < aaTransData.getRegFeesData().getVectFees().size();
			i++)
		{
			FeesData laFeeData =
				(FeesData) aaTransData
					.getRegFeesData()
					.getVectFees()
					.elementAt(
					i);
			caRpt.print(
				laFeeData.getDesc(),
				FEEASSESSEDPOSITION,
				FEEASSESSEDLENGTH);
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			// S447500 just prints the price.  assumes that qty is 1.
			caRpt.rightAlign(
				laFeeData.getItemPrice().printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			laFeeTotal = laFeeTotal.add(laFeeData.getItemPrice());
			caRpt.nextLine();
		}
		if (!laFeeTotal.equals(laZeroDollar))
		{
			caRpt.nextLine();
			caRpt.print(
				TOTALSTRING,
				TOTALPOSITION,
				TOTALSTRING.length());
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			caRpt.rightAlign(
				laFeeTotal.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			caRpt.blankLines(2);
		}
	}

	/**
	 * Print the Receipt Messages.
	 * 
	 * @param aaTransData CompleteTransactionData
	 */
	public void printMessages(CompleteTransactionData aaTransData)
	{
		try
		{
			// print the Commercial Vehicle message.
			// Look up the Trans Code to get RCPTMSGCD to see if we need
			// to print
			// the COMMERCIALVEHICLESTRING.
			// this look up can create an RTSException.  We are just
			// eating it!
			TransactionCodesData laTransCodeData =
				(
					TransactionCodesData) TransactionCodesCache
						.getTransCd(
					aaTransData.getTransCode());
			// normal message
			if (laTransCodeData.getRcptMsgCd() == 1)
			{
				caRpt.print(
					COMMERCIALVEHICLESTRING,
					FIRSTCOLUMNPOSITION,
					COMMERCIALVEHICLESTRING.length());
				caRpt.blankLines(2);
			}
			// message for permits, tow trucks and other cases matching 
			// these conditions.
			if (laTransCodeData.getRcptMsgCd() == 2)
			{
				caRpt.print(
					VEHISSUEDSTRING,
					FIRSTCOLUMNPOSITION,
					VEHISSUEDSTRING.length());
				caRpt.blankLines(2);
			}
			// message for credit remaining.
			if (aaTransData.getCrdtRemaining() != null
				&& !aaTransData.getCrdtRemaining().equals(
					new Dollar(0.00)))
			{
				caRpt.print(
					REGCREDITSTRING,
					FIRSTCOLUMNPOSITION,
					REGCREDITSTRING.length());
				caRpt.blankLines(2);
			}
			// check to see if a county is a "large automation" county.
			// if so, print the automation message.
			CountyCalendarYearData laCountyCalendarYearData =
				CountyCalendarYearCache.getCntyCalndrYr(
					aaTransData.getOfcIssuanceNo(),
					aaTransData.getOfcIssuanceNo());
			if (laCountyCalendarYearData != null
				&& laCountyCalendarYearData.getCntySizeCd().equals(
					LARGECOUNTYSTRING))
			{
				caRpt.print(
					AUTOMATION_1_STRING,
					FIRSTCOLUMNPOSITION,
					AUTOMATION_1_STRING.length());
				caRpt.nextLine();
				caRpt.print(
					AUTOMATION_2_STRING,
					FIRSTCOLUMNPOSITION,
					AUTOMATION_2_STRING.length());
				caRpt.blankLines(2);
			}
		}
		catch (RTSException aeRTSEx)
		{
			// this was bad.  but do not fail on this!
		}
		// this is where VTR messages would go if we used them!
		// There are three possible lines.
	}

	/**
	 * Print the Inventory and Fees Section.
	 * 
	 * @param aaTransData CompleteTransactionData
	 * @param avDataVector Vector
	 */
	public void printNotationsAndPayment(
		CompleteTransactionData aaTransData,
		Vector avDataVector)
	{
		// Notations are supposed to start at line 31?
		// build up the Notations Vector
		Vector lvNotationsData = buildNotationsVector(aaTransData);
		// build up the Payment Vector
		Vector lvPaymentData = buildPaymentVector(avDataVector);
		caRpt.print(
			VEHNOTATIONSTRING,
			FIRSTCOLUMNPOSITION,
			VEHNOTATIONSTRING.length());
		// Do not print payment for any offices except counties
		if (lvPaymentData != null && lvPaymentData.size() > 0)
		{
			caRpt.print(
				PAYMENTSTRING,
				PAYMENTHEADERPOSITION,
				PAYMENTSTRING.length());
		}
		caRpt.nextLine();
		Dollar laZeroDollar = new Dollar(0);
		Dollar laPaymentTotal = new Dollar(0);
		Dollar laChangeBackAmt = new Dollar(0);

		int liNotationCount = 0;
		// index through the Vehicle Notations vector
		int liPaymentCount = 0; // index through the Payment vector
		// loop controller.  keep looping until it is set off.
		boolean lbKeepLooping = true;
		//Added for PCR25
		String lsPymntType = "";
		while (lbKeepLooping)
		{
			if (lvNotationsData != null
				&& liNotationCount < lvNotationsData.size())
			{
				String lsNotation =
					(String) lvNotationsData.elementAt(liNotationCount);
				caRpt.print(
					lsNotation,
					FIRSTCOLUMNPOSITION,
					lsNotation.length());
				liNotationCount = liNotationCount + 1;
			}
			if (lvPaymentData != null
				&& liPaymentCount < lvPaymentData.size())
			{
				// PCR25 Code
				// Print credit card message if needed
				//   TransactionPaymentData paymentData =
				//        (TransactionPaymentData) lvPaymentData.elementAt(
				// liPaymentCount);
				if (cbCreditFeeIncMsgIndi)
				{
					if (!cbCreditFeeIncMsgIndi)
						liPaymentCount = liPaymentCount + 1;
					cbCreditFeeIncMsgIndi = false;
					// TransactionPaymentData paymentData =
					// (TransactionPaymentData) lvPaymentData.elementAt(
					// liPaymentCount);
					// String lsCreditCardFeeMessage = null;
					// if (paymentData.isCreditCardFee())
					// lsCreditCardFeeMessage = formatCreditCardFeeMessage(
					// paymentData);
					/* if (lsCreditCardFeeMessage != null) {
					       //cRpt.nextLine();
					       cRpt.rightAlign(
					           lsCreditCardFeeMessage,
					           PAYMENTPOSITION - 2,
					           lsCreditCardFeeMessage.length());
					       // Move down 1 lines
					   } else*/
					if (liPaymentCount < lvPaymentData.size())
					{
						TransactionPaymentData laPaymentData =
							(
								TransactionPaymentData) lvPaymentData
									.elementAt(
								liPaymentCount);
						caRpt.print(
							laPaymentData.getPymntType(),
							PAYMENTPOSITION,
							laPaymentData.getPymntType().length());
						// print the check number if it exists
						if (laPaymentData.getPymntCkNo() != null)
						{
							caRpt.print(
								" #" + laPaymentData.getPymntCkNo());
						}
						caRpt.print(
							DOLLARSIGN,
							DOLLARSIGNPOSITION,
							LENGTH1);
						caRpt.rightAlign(
							laPaymentData
								.getPymntTypeAmt()
								.printDollar()
								.substring(
								1),
							MONEYPOSITION,
							ITEMPRICELENGTH);
						//Added for PCR25 Code
						lsPymntType = laPaymentData.getPymntType();
						if (!(cbCreditFeeIncMsgIndi
							&& lsPymntType.equals(CHARGE)))
						{
							liPaymentCount = liPaymentCount + 1;
						}
						laPaymentTotal =
							(laPaymentTotal
								.add(laPaymentData.getPymntTypeAmt()));
						if (laPaymentData.getChngDue() != null
							&& !laPaymentData.getChngDue().equals(
								laZeroDollar))
						{
							laChangeBackAmt =
								laPaymentData.getChngDue();
						}
					}
				}
				else
				{
					TransactionPaymentData laPaymentData =
						(
							TransactionPaymentData) lvPaymentData
								.elementAt(
							liPaymentCount);
					caRpt.print(
						laPaymentData.getPymntType(),
						PAYMENTPOSITION,
						laPaymentData.getPymntType().length());
					// print the check number if it exists
					if (laPaymentData.getPymntCkNo() != null)
					{
						caRpt.print(
							" #" + laPaymentData.getPymntCkNo());
					}
					caRpt.print(
						DOLLARSIGN,
						DOLLARSIGNPOSITION,
						LENGTH1);
					caRpt.rightAlign(
						laPaymentData
							.getPymntTypeAmt()
							.printDollar()
							.substring(
							1),
						MONEYPOSITION,
						ITEMPRICELENGTH);
					//Added for PCR25 Code
					lsPymntType = laPaymentData.getPymntType();
					if (!(cbCreditFeeIncMsgIndi
						&& lsPymntType.equals(CHARGE)))
					{
						liPaymentCount = liPaymentCount + 1;
					}
					laPaymentTotal =
						(laPaymentTotal
							.add(laPaymentData.getPymntTypeAmt()));
					if (laPaymentData.getChngDue() != null
						&& !laPaymentData.getChngDue().equals(
							laZeroDollar))
					{
						laChangeBackAmt = laPaymentData.getChngDue();
					}
				}
			}
			caRpt.nextLine();
			// if both vectors are finished turn off lbKeepLooping
			// remove notation vector stuff for now..
			if ((lvNotationsData == null
				|| liNotationCount >= lvNotationsData.size())
				&& (lvPaymentData == null
					|| liPaymentCount >= lvPaymentData.size()))
			{
				lbKeepLooping = false;
			}
		}
		if (lvPaymentData.size() > 0)
		{
			TransactionPaymentData laPaymentData =
				(TransactionPaymentData) lvPaymentData.elementAt(
					lvPaymentData.size() - 1);
			if (laPaymentData.isCreditCardFee())
			{
				String lsCreditCardFeeMessage =
					formatCreditCardFeeMessage();
				//cRpt.nextLine();
				caRpt.rightAlign(
					lsCreditCardFeeMessage,
					MONEYPOSITION - 5,
					lsCreditCardFeeMessage.length());
				caRpt.nextLine();
			}
		}
		// Print the Total Payment Line
		if (!laPaymentTotal.equals(laZeroDollar))
		{
			caRpt.nextLine();
			caRpt.print(
				PAYMENTTOTALSTRING,
				PAYMENTPOSITION,
				PAYMENTTOTALSTRING.length());
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			caRpt.rightAlign(
				laPaymentTotal.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			caRpt.nextLine();
		}
		// Print out the Change Due Line
		if (!laChangeBackAmt.equals(laZeroDollar))
		{
			caRpt.print(
				PAYMENTCHANGEDUESTRING,
				PAYMENTPOSITION,
				PAYMENTCHANGEDUESTRING.length());
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			caRpt.rightAlign(
				laChangeBackAmt.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			caRpt.nextLine();
		}
	}

	/**
	 * Print the Vehicle Information Information.
	 * 
	 * @param aaTransData CompleteTransactionData
	 */
	public void printVehicleData(CompleteTransactionData aaTransData)
	{
		caRpt.blankLines(14 - caRpt.getCurrX());
		// print vehicle calssification
		caRpt.print(
			VEHCLASSSTRING,
			FIRSTCOLUMNPOSITION,
			VEHCLASSSTRING.length());
		caRpt.print(
			aaTransData
				.getVehicleInfo()
				.getVehicleData()
				.getVehClassCd());
		caRpt.nextLine();
		// print VIN
		caRpt.print(VINSTRING, FIRSTCOLUMNPOSITION, VINSTRING.length());
		caRpt.print(
			aaTransData.getVehicleInfo().getVehicleData().getVin());
		// print unit number
		caRpt.print(UNITSTRING, UNITPOSITION, UNITSTRING.length());
		caRpt.print(
			aaTransData.getVehicleInfo().getTitleData().getVehUnitNo());
		caRpt.nextLine();
		// print vehicle year, make, model and style
		caRpt.print(
			YRMAKESTRING,
			FIRSTCOLUMNPOSITION,
			YRMAKESTRING.length());
		caRpt.print(
			String.valueOf(
				aaTransData
					.getVehicleInfo()
					.getVehicleData()
					.getVehModlYr()));
		caRpt.print(SLASH);
		caRpt.print(
			aaTransData.getVehicleInfo().getVehicleData().getVehMk());
		caRpt.print(MODELSTRING, MODELPOSITION, MODELSTRING.length());
		caRpt.print(
			aaTransData.getVehicleInfo().getVehicleData().getVehModl());
		caRpt.print(
			BDYSTYLESTRING,
			BDYSTYLEPOSITION,
			BDYSTYLESTRING.length());
		caRpt.print(
			aaTransData
				.getVehicleInfo()
				.getVehicleData()
				.getVehBdyType());
		caRpt.nextLine();
		// print vehicle empty weight, carrying capacity, gross weight,
		// and tonnage 
		caRpt.print(
			EMPTYWTSTRING,
			FIRSTCOLUMNPOSITION,
			EMPTYWTSTRING.length());
		caRpt.print(
			String.valueOf(
				aaTransData
					.getVehicleInfo()
					.getVehicleData()
					.getVehEmptyWt()));
		caRpt.print(
			CARYCAPSTRING,
			CARYCAPPOSITION,
			CARYCAPSTRING.length());
		caRpt.print(
			String.valueOf(
				aaTransData
					.getVehicleInfo()
					.getRegData()
					.getVehCaryngCap()));
		caRpt.print(
			GROSSWTSTRING,
			GROSSWTPOSITION,
			GROSSWTSTRING.length());
		caRpt.print(
			String.valueOf(
				aaTransData
					.getVehicleInfo()
					.getRegData()
					.getVehGrossWt()));
		caRpt.print(
			TONNAGESTRING,
			TONNAGEPOSITION,
			TONNAGESTRING.length());
		caRpt.print(
			aaTransData
				.getVehicleInfo()
				.getVehicleData()
				.getVehTon()
				.toString());
		caRpt.nextLine();
		//print vehicle temporary carrying capacity and gross weight
		caRpt.print(
			TEMPCARYCAPSTRING,
			FIRSTCOLUMNPOSITION,
			TEMPCARYCAPSTRING.length());
		caRpt.print(
			String.valueOf(
				aaTransData
					.getTimedPermitData()
					.getTempVehCaryngCap()));
		caRpt.print(
			TEMPGROSSWTSTRING,
			TEMPGROSSWTPOSITION,
			TEMPGROSSWTSTRING.length());
		caRpt.print(
			String.valueOf(
				aaTransData.getTimedPermitData().getTempVehGrossWt()));
		caRpt.blankLines(3);
	}

	/**
	 * This version of queryData returns the CompleteTransactionData object
	 */
	public Vector queryData()
	{
		// Generating Demo data to display.
		MFVehicleData laMFdata = new MFVehicleData();
		OwnerData laOwnrData = new OwnerData();
		TitleData laTtlData = new TitleData();
		VehicleData laVehData = new VehicleData();
		RegistrationData laRegData = new RegistrationData();
		SalvageData laOtherStateCntry = new SalvageData();
		AddressData laAddrData = new AddressData();
		//OwnerData OwnrData = new OwnerData();
		laOwnrData.setName1("Ray Rowehl");
		laOwnrData.setName2("Roger Ramjet");
		//AddressData AddrData = new AddressData();
		laAddrData.setCity("Austin");
		//AddrData.setCntry("CHINA");
		laAddrData.setSt1("Some Street1");
		//AddrData.setSt2("Some Street2");
		laAddrData.setState("TX");
		laAddrData.setZpcd("2222222");
		laAddrData.setZpcdp4("2222");
		laOwnrData.setAddressData(laAddrData);
		//MFVehicleData MFdata = new MFVehicleData();
		laMFdata.setOwnerData(laOwnrData);
		//VehicleData VehData = new VehicleData();
		laVehData.setVehBdyType("4T");
		laVehData.setVehMk("HOND");
		laVehData.setVehModlYr(2002);
		laVehData.setVehEmptyWt(0);
		laVehData.setVin("1234567890asdfghj");
		laMFdata.setVehicleData(laVehData);
		//TitleData TtlData = new TitleData();
		laTtlData.setDocNo("11111111111111111");
		laTtlData.setTtlIssueDate(20011018);
		laMFdata.setTitleData(laTtlData);
		//RegistrationData RegData = new RegistrationData();
		laRegData.setRegPltNo("B01BBBB");
		laMFdata.setRegData(laRegData);
		//SalvageData OtherStateCntry = new SalvageData();
		laOtherStateCntry.setOthrStateCntry(null);
		laMFdata.setVctSalvage(new Vector());
		laMFdata.getVctSalvage().add(laOtherStateCntry);
		// setup the Inventory vector
		ProcessInventoryData laInvData = new ProcessInventoryData();
		laInvData.setItmCd("WS");
		laInvData.setInvItmYr(2002);
		laInvData.setInvItmNo("123456WC");
		Vector lvInvData = new Vector();
		lvInvData.addElement(laInvData);
		// create Fees data vector
		RegFeesData laRegFeesData = new RegFeesData();
		// create the Complete Transaction Data objuect to pass
		CompleteTransactionData laTransData =
			new CompleteTransactionData();
		laTransData.setAnnualDieselFee(new Dollar(0.00));
		laTransData.setAnnualRegFee(new Dollar(40.50));
		laTransData.setBsnDateTotalAmt(new Dollar(0.00));
		laTransData.setCrdtRemaining(new Dollar(0.00));
		laTransData.setCustActualRegFee(new Dollar(51.80));
		laTransData.setCustName("Tracey Kahn");
		laTransData.setDealerId(0);
		// defect 9631
		//laTransData.setDieselFee(new Dollar(0.00));
		// end defect 9631
		laTransData.setDisableAddlFees(0);
		laTransData.setDisableCtyFees(0);
		laTransData.setOfcIssuanceNo(170);
		// defect 10112
		// laTransData.setOwnrSuppliedStkrAge(0);
		// end defect 10112 
		laTransData.setTransCode("RENEW");
		laTransData.setVehicleInfo(laMFdata);
		// add the Vehicle Object
		laTransData.setInvItms(lvInvData); // add the inventory vector
		laTransData.setRegFeesData(laRegFeesData); // add the fees data
		// declare the vector to be passed to formatReceipt
		Vector lvTestData = new Vector();
		// CompleteTransactionData to vector
		lvTestData.addElement(laTransData);
		// add the faked TransId
		lvTestData.addElement("17009937205111155");
		return lvTestData;
	}

	/**
	 * This version of queryData returns the CompleteTransactionData object.
	 * 
	 * @return Vector
	 */
	public Vector queryData1()
	{
		Vector lvReceipt = new Vector();
		CompleteTransactionData laTransData =
			(CompleteTransactionData) Vehicle.getVeh();
		if (laTransData.getTransId() == null
			|| laTransData.getTransId().length() < 1)
		{
			laTransData.setTransId("17010037205111155");
		}
		Vector lvInvVect = new Vector();
		ProcessInventoryData laInvData = new ProcessInventoryData();
		laInvData.setInvItmNo("111155WC");
		laInvData.setItmCd("WS");
		laInvData.setInvItmYr(2001);
		lvInvVect.addElement(laInvData);
		laTransData.setAllocInvItms(lvInvVect);
		lvReceipt.addElement(laTransData);
		// Add the transid to the vector
		lvReceipt.addElement(laTransData.getTransId());
		// Add a Payment object to the vector
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