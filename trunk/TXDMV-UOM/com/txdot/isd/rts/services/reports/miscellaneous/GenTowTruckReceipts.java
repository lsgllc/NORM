package com.txdot.isd.rts.services.reports.miscellaneous;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;
import com.txdot.isd.rts.services.util.Dollar;

/*
 * GenTowTruckReceipts.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang    	12/05/2001  New Class
 * S Govindappa	07/10/2002  Merged PCR25 code
 * 							modify printPayment()
 * MAbs	 		08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * B Arredondo	02/05/2003	modified FeesAssessedLength from 25 to 30
 *							to fit description.
 *							defect 5207 
 * K Harrell	03/21/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify formatReceipt()
 * 							Ver 5.2.0	
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896 Ver 5.2.3
 * Min Wang		02/14/2008	Change text on tow truck receipt.
 * 							modify NOTESTRING2
 * 							defect 9560 Ver 3 Amigos Prep
 * B Hargrove	04/07/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify queryData()
 * 							defect 9631 Ver Defect POS A
 * T Pederson	05/16/2008	Changed MONEYPOSITION to 82 to match 
 * 							other receipts.
 *							defect 8576 Ver Defect Pos A
 * K Harrell	07/12/2009	Implement new OwnerData. 
 * 							Implement new class variable, caCTData  
 * 							add caCTData 
 * 							delete lineBreakHandling() 
 * 							modify queryData(), 
 * 							 formatReceipt(), printInventoryAndFees(), 
 * 							 printPayment(),  printVehicleData() 
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	09/28/2010	modify formatReceipt() 
 * 							defect 10590 Ver 6.6.0 
 * Min Wang		10/08/2010	Allow printing of the Max length of the price. 
 * 							modify ITEMPRICELENGTH  
 * 							defect 10596 Ver 6.6.0   
 * ---------------------------------------------------------------------
 */
/**
 * This class generates Tow Truck Receipts.
 * 
 * @version	6.6.0			10/08/2010
 * @author	Min Wang
 * <br>Creation Date:		11/12/2001
 */
public class GenTowTruckReceipts extends ReceiptTemplate
{
	boolean cbCreditFeeIncMsgIndi = true;

	// defect 10112 
	CompleteTransactionData caCTData;
	// end defect 10112 

	private static final int FIRSTCOLUMNPOSITION = 5;
	private static final int YEARPOSITION = 37;
	private static final int YEARLENGTH = 4;
	// defect 10596
	//private static final int ITEMPRICELENGTH = 12;
	private static final int ITEMPRICELENGTH = 14;
	// end defect 10596
	private static final int FEEASSESSEDPOSITION = 47;
	private static final int FEEASSESSEDLENGTH = 30;
	private static final int TOTALPOSITION = 65;
	private static final int DOLLARSIGNPOSITION = 80;
	private static final int PAYMENTHEADERPOSITION = 55;
	private static final int PAYMENTPOSITION = 62;
	//	defect 8576 
	// Receipt Fees Alignment consistent w/ other Receipts
	//private static final int MONEYPOSITION = 81;
	private static final int MONEYPOSITION = 82;
	// end defect 8576
	private static final int SIGNATUREPOSITION = 46;
	private static final int UNDERLINESPOSITION = 40;
	private static final int TRANSOBJPOSITION = 0;
	private static final int LENGTH1 = 1;
	private static final int LENGTH5 = 5;
	private static final int LENGTH36 = 36;

	private static final String VINSTRING =
		"VEHICLE IDENTIFICATION NO: ";
	private static final String YRMAKESTRING = "YR/MAKE: ";
	private static final String INVITMSTRING = "INVENTORY ITEM(S)";
	private static final String YEARSTRING = "YR";
	private static final String FEEASSESSEDSTRING = "FEES ASSESSED";
	private static final String TOTALSTRING = "TOTAL";
	private static final String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	private static final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	private static final String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	private static final String REGCREDITSTRING =
		"** RETAIN RECEIPT FOR CREDIT ON NEXT YEAR'S REGISTRATION.";
	private static final String VEHISSUEDSTRING =
		"THIS RECEIPT TO BE CARRIED IN THE VEHICLE FOR WHICH ISSUED.";
	private static final String AUTOMATION_1_STRING =
		"Current law requires an additional $1.00 fee (already";
	private static final String AUTOMATION_2_STRING =
		"included) in counties with 50,000 or more vehicles.";
	private static final String COMMERCIALVEHICLESTRING =
		"THIS RECEIPT TO BE CARRIED IN ALL COMERCIAL VEHICLES.";
	private static final String SIGNATURESTRING =
		"SIGNATURE OF OWNER/AGENT";
	private static final String NOTESTRING1 =
		"VALID UNTIL EXPIRATION DATE UNLESS TOW TRUCK "
			+ "CERTIFICATION IS CANCELLED BY THE";
	// defect 9560
	//private static final String NOTESTRING2 =
	//	"TEXAS DEPARTMENT OF TRANSPORTATION, MOTOR CARRIER DIVISION.";
	private static final String NOTESTRING2 =
		"TEXAS DEPARTMENT OF LICENSING AND REGULATION.";
	// end defect 9560
	private static final String UNDERLINES =
		"____________________________________";
	private static final String LARGECOUNTYSTRING = "L";
	private static final String DOLLARSIGN = "$";
	private static final String SLASH = "/";

	/**
	 * GenTowTruckReceipts constructor
	 */
	public GenTowTruckReceipts()
	{
		super();
	}

	/**
	 * GenTowTruckReceipts constructor
	 * 
	 * @param asRcptName String
	 * @param aaRcptProps ReceiptProperties
	 */
	public GenTowTruckReceipts(
		String asRcptName,
		ReceiptProperties aaRcptProps)
	{
		super(asRcptName, aaRcptProps);
	}

	/**
	 * Add element to vector if it is appropriate.
	 * 
	 * @param laIndicatorCode IndicatorDescriptionsData
	 * @param lvNotationsVector Vector
	 * @return int - Vector size
	 */
	public int addToNotations(
		IndicatorDescriptionsData laIndicatorCode,
		Vector lvNotationsVector)
	{
		if (laIndicatorCode.getIndiRcptPriority() > 0)
		{
			lvNotationsVector.addElement(laIndicatorCode);
		}
		return lvNotationsVector.size();
	}

	/**
	 * Get the object off of the DataVector and check to see it they are
	 * Payment Objects.  If they are, put them on the outgoing Payment Vector.
	 * 
	 * @param avDataVector Vector
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

			// inspect the object to see if it is a TransactionPaymentData object
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
		// defect 10112 
		caCTData =
			(CompleteTransactionData) lvDataVector.elementAt(
				TRANSOBJPOSITION);

		// defect 10590 
		if (!caCTData.isPreviewReceipt()
			&& caCTData.getStickers() != null
			&& caCTData.getStickers().size() > 0)
		{
			cbShouldStickersPrint = true;
		}
		// end defect 10590 

		// Get the Transid off of the Vector
		// Get the first Payment object off of the Vector
		//TransactionPaymentData lTransactionPaymentData = (TransactionPaymentData) lvDataVector.elementAt(2);
		// System data for logon and substation Id
		try
		{
			//call generateHeader;
			generateReceiptHeader(lvDataVector);
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println("Had a problem with Heading Lookup!");
			aeRTSEx.printStackTrace();
		}
		caRpt.blankLines(19 - caRpt.getCurrX());
		// Process the Vehicle Information
		printVehicleData();
		// Process Inventory Items and Fees
		printInventoryAndFees();
		caRpt.nextLine();
		// Process Notations and Payment
		if (lvDataVector.size() > 1)
			printPayment(lvDataVector);

		// Print signature
		caRpt.blankLines(44 - caRpt.getCurrX());
		printSignature();
		// print note
		caRpt.blankLines(54 - caRpt.getCurrX());
		printNote();
		// Process Messages
		printMessages();
		// PCR 34
		if (cbShouldStickersPrint)
		{
			generateStickers(caCTData);
		}
		// End PCR 34
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
		GenTowTruckReceipts laGTTR =
			new GenTowTruckReceipts(
				"NOT USED FOR RECEIPTS",
				laRcptProps);
		// Generating Demo data to display.
		// String method get its data from a generated object
		Vector lvFakeDataVector = laGTTR.queryData1();
		try
		{
			laGTTR.formatReceipt(lvFakeDataVector);
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
		laPout.print(laGTTR.caRpt.getReport().toString());
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
	 */
	public void printInventoryAndFees()
	{
		// Look up the Item Codes description to print
		caRpt.print(
			INVITMSTRING,
			FIRSTCOLUMNPOSITION,
			INVITMSTRING.length());
		caRpt.print(YEARSTRING, YEARPOSITION - 2, YEARLENGTH);
		caRpt.print(
			FEEASSESSEDSTRING,
			FEEASSESSEDPOSITION + 2,
			FEEASSESSEDSTRING.length());

		caRpt.nextLine();

		// defect 10112  
		// Use caCTData, local references for Inventory, Fees 
		if (caCTData.getAllocInvItms() == null)
		{
			caCTData.setAllocInvItms(new Vector());
		}
		if (caCTData.getRegFeesData().getVectFees() == null)
		{
			caCTData.getRegFeesData().setVectFees(new Vector());
		}
		Vector lvAllocInvItms = caCTData.getAllocInvItms();
		Vector lvRegFees = caCTData.getRegFeesData().getVectFees();
		// end defect 10112 

		int liInvCount = 0;
		int liFeeCount = 0;
		boolean lbKeepLooping = true;

		Dollar laZeroDollar = new Dollar(0);
		Dollar laFeeTotal = new Dollar(0);

		while (lbKeepLooping)
		{
			if (liInvCount < lvAllocInvItms.size())
			{
				ProcessInventoryData laInvData =
					(ProcessInventoryData) lvAllocInvItms.elementAt(
						liInvCount);

				ItemCodesData laItmCode =
					ItemCodesCache.getItmCd(laInvData.getItmCd());

				caRpt.print(
					laItmCode.getItmCdDesc(),
					FIRSTCOLUMNPOSITION,
					(laItmCode.getItmCdDesc()).length());

				if (laInvData.getInvItmYr() > 0)
				{
					caRpt.print(
						String.valueOf(laInvData.getInvItmYr()),
						YEARPOSITION - 2,
						YEARLENGTH);
				}
				liInvCount = liInvCount + 1;
			}
			if (liFeeCount < lvRegFees.size())
			{
				FeesData laFeeData =
					(FeesData) lvRegFees.elementAt(liFeeCount);

				caRpt.print(
					laFeeData.getDesc(),
					FEEASSESSEDPOSITION + 2,
					FEEASSESSEDLENGTH);

				caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);

				caRpt.rightAlign(
					laFeeData.getItemPrice().printDollar().substring(1),
					MONEYPOSITION,
					ITEMPRICELENGTH);

				liFeeCount = liFeeCount + 1;
				laFeeTotal = laFeeTotal.add(laFeeData.getItemPrice());
			}
			caRpt.nextLine();

			// if both vectors are finished turn off lbKeepLooping
			if (liInvCount >= lvAllocInvItms.size()
				&& liFeeCount >= lvRegFees.size())
			{
				lbKeepLooping = false;
			}
		}
		// end defect 10112 

		if (!laFeeTotal.equals(laZeroDollar))
		{
			caRpt.print(TOTALSTRING, TOTALPOSITION + 2, LENGTH5);
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			caRpt.rightAlign(
				laFeeTotal.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			caRpt.nextLine();
		}
	}

	/**
	 * Print the Receipt Messages.
	 * 
	 */
	public void printMessages()
	{
		try
		{
			// print the Commercial Vehicle message.
			// Look up the Trans Code to get RCPTMSGCD to see if we need
			// to print the COMMERCIALVEHICLESTRING.
			// this look up can create an RTSException.
			// We are just eating it!
			TransactionCodesData laTransCodeData =
				(
					TransactionCodesData) TransactionCodesCache
						.getTransCd(
					caCTData.getTransCode());

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
			if (caCTData.getCrdtRemaining() != null
				&& !caCTData.getCrdtRemaining().equals(new Dollar(0.00)))
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
					caCTData.getOfcIssuanceNo(),
					caCTData.getOfcIssuanceNo());

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
	 * Print the note.
	 */
	public void printNote()
	{
		caRpt.print(
			NOTESTRING1,
			FIRSTCOLUMNPOSITION,
			NOTESTRING1.length());
		caRpt.nextLine();
		caRpt.print(
			NOTESTRING2,
			FIRSTCOLUMNPOSITION,
			NOTESTRING2.length());
		caRpt.blankLines(2);
	}

	/**
	 * Print the Inventory and Fees Section.
	 * 
	 * @param avDataVector Vector
	 */
	public void printPayment(Vector avDataVector)
	{
		// Notations are supposed to start at line 31?
		// build up the Payment Vector
		Vector lvPaymentData = buildPaymentVector(avDataVector);

		// Do not print payment for any offices except counties
		if (lvPaymentData != null && lvPaymentData.size() > 0)
		{
			caRpt.nextLine();
			caRpt.print(
				PAYMENTSTRING,
				PAYMENTHEADERPOSITION + 2,
				PAYMENTSTRING.length());
		}
		caRpt.nextLine();
		Dollar laZeroDollar = new Dollar(0);
		Dollar laPaymentTotal = new Dollar(0);
		Dollar laChangeBackAmt = new Dollar(0);
		int liPaymentCount = 0;

		boolean lbKeepLooping = true;
		while (lbKeepLooping)
		{
			if (lvPaymentData != null
				&& liPaymentCount < lvPaymentData.size())
			{
				TransactionPaymentData laPaymentData =
					(TransactionPaymentData) lvPaymentData.elementAt(
						liPaymentCount);
				caRpt.print(
					laPaymentData.getPymntType(),
					PAYMENTPOSITION + 4,
					laPaymentData.getPymntType().length());
				// print the check number if it exists
				if (laPaymentData.getPymntCkNo() != null)
				{
					caRpt.print(" #" + laPaymentData.getPymntCkNo());
				}
				caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
				caRpt.rightAlign(
					laPaymentData
						.getPymntTypeAmt()
						.printDollar()
						.substring(
						1),
					MONEYPOSITION,
					ITEMPRICELENGTH);
				// PCR25 Code
				// Print credit card message if needed
				if (cbCreditFeeIncMsgIndi
					&& laPaymentData.isCreditCardFee())
				{
					cbCreditFeeIncMsgIndi = false;
					String lsCreditCardFeeMessage =
						formatCreditCardFeeMessage();
					if (lsCreditCardFeeMessage != null)
					{
						caRpt.nextLine();
						caRpt.print(
							lsCreditCardFeeMessage,
							MONEYPOSITION - 5,
							lsCreditCardFeeMessage.length());
						// Move down 1 lines
					}
				}
				liPaymentCount = liPaymentCount + 1;
				laPaymentTotal =
					(laPaymentTotal
						.add(laPaymentData.getPymntTypeAmt()));
				if (laPaymentData.getChngDue() != null
					&& !laPaymentData.getChngDue().equals(laZeroDollar))
				{
					laChangeBackAmt = laPaymentData.getChngDue();
				}
			}
			caRpt.nextLine();
			if ((lvPaymentData == null)
				|| liPaymentCount >= lvPaymentData.size())
			{
				lbKeepLooping = false;
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
	 * Print the note.
	 */
	public void printSignature()
	{
		caRpt.print(UNDERLINES, UNDERLINESPOSITION, LENGTH36);
		caRpt.nextLine();
		caRpt.print(SIGNATURESTRING, SIGNATUREPOSITION, LENGTH36);
		caRpt.blankLines(15);
	}

	/**
	 * Print the Vehicle Information Information.
	 * 
	 */
	public void printVehicleData()
	{
		// print VIN
		caRpt.print(VINSTRING, FIRSTCOLUMNPOSITION, VINSTRING.length());
		caRpt.print(
			caCTData.getVehicleInfo().getVehicleData().getVin());

		caRpt.nextLine();

		// print vehicle year and make
		caRpt.print(
			YRMAKESTRING,
			FIRSTCOLUMNPOSITION,
			YRMAKESTRING.length());
		caRpt.print(
			String.valueOf(
				caCTData
					.getVehicleInfo()
					.getVehicleData()
					.getVehModlYr()));
		caRpt.print(SLASH);
		caRpt.print(
			caCTData.getVehicleInfo().getVehicleData().getVehMk());
		caRpt.nextLine();
		caRpt.nextLine();
	}

	/**
	 * This version of queryData returns the CompleteTransactionData object.
	 */
	public Vector queryData()
	{
		// Generating Demo data to display.
		MFVehicleData laMFData = new MFVehicleData();
		OwnerData laOwnrData = new OwnerData();
		TitleData laTtlData = new TitleData();
		VehicleData laVehData = new VehicleData();
		RegistrationData laRegData = new RegistrationData();
		SalvageData laOtherStateCntry = new SalvageData();
		AddressData laAddrData = new AddressData();
		laOwnrData.setName1("Ray Rowehl");
		laOwnrData.setName2("Roger Ramjet");
		laAddrData.setCity("Austin");
		laAddrData.setSt1("Some Street1");
		laAddrData.setState("TX");
		laAddrData.setZpcd("2222222");
		laAddrData.setZpcdp4("2222");
		laOwnrData.setAddressData(laAddrData);
		laMFData.setOwnerData(laOwnrData);
		laVehData.setVehBdyType("4T");
		laVehData.setVehMk("HOND");
		laVehData.setVehModlYr(2002);
		laVehData.setVehEmptyWt(0);
		laVehData.setVin("1234567890asdfghj");
		laMFData.setVehicleData(laVehData);
		laTtlData.setDocNo("11111111111111111");
		laTtlData.setTtlIssueDate(20011018);
		laMFData.setTitleData(laTtlData);
		//RegistrationData RegData = new RegistrationData();
		laRegData.setRegPltNo("B01BBBB");
		laMFData.setRegData(laRegData);
		//SalvageData OtherStateCntry = new SalvageData();
		laOtherStateCntry.setOthrStateCntry(null);
		laMFData.setVctSalvage(new Vector());
		laMFData.getVctSalvage().add(laOtherStateCntry);
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
		laTransData.setVehicleInfo(laMFData);
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
	 * @param lsQuery String
	 * @return Vector
	 */
	public Vector queryData1()
	{
		Vector lvReceipt = new Vector();
		CompleteTransactionData laTransData =
			new CompleteTransactionData();
		laTransData = (CompleteTransactionData) Vehicle.getVeh();
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
