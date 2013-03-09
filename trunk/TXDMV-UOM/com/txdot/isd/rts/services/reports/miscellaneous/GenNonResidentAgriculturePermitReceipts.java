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
 * GenNonResidentAgriculturePermitReceipts.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * Min Wang     12/05/2001  New Class
 * Min Wang		05/03/2002 	using buildPaymentVector()
 *							from ReceiptTemplate.
 * S Govindappa 07/10/2002  merging the PCR25 code
 * MAbs	  		08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * B Arredondo	03/26/2003	Defect 5868, increased feeassessedlength
 * 							from 25 to 29 to be able to fit all of
 * 							'Non-resident outstate permit'.
 * B Arredondo	03/26/2003	Defect 5889, in method
 * 							printInventoryAndFees(), added substring(1)
 * 							after printDollar() to get rid of the
 *							second dollar sign in front of the amount.  
 * S Johnston	05/13/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify printInventoryAndFees(),
 * 							printMessages(), main(), queryData()
 *							defect 7896 Ver 5.2.3
 * B Hargrove	04/07/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify queryData()
 * 							defect 9631 Ver Defect POS A
 * K Harrell	07/12/2009	Implement new OwnerData. Remove unused 
 * 							parameters.  Implement new class variables,
 * 							 csTransCd, caCTData  
 * 							add caCTData 
 * 							delete lineBreakHandling() 
 * 							modify queryData(), sectionStartingPoint(), 
 * 							 formatReceipt(), printInventoryAndFees(), 
 * 							 printNotationsAndPayment(), 
 * 							 printVehicleData() 
 * 							defect 10112 Ver Defect_POS_F 
 * Min Wang		10/08/2010	Allow printing of the Max length of the price. 
 * 							modify ITEMPRICELENGTH  
 * 							defect 10596 Ver 6.6.0   
 * ---------------------------------------------------------------------
 */
/**
 * This class generates all Non Resident Agriculture Permit Receipts.
 *  
 * @version	6.6.0			10/08/2010
 * @author	Min Wang
 * <br>Creation Date:		11/12/2001 07:33:33
 */
public class GenNonResidentAgriculturePermitReceipts
	extends ReceiptTemplate
{
	boolean cbCreditFeeIncMsgIndi = true;

	// defect 10112 
	CompleteTransactionData caCTData;
	// end defect 10112 

	private static final int FIRSTCOLUMNPOSITION = 5;
	private static final int YEARPOSITION = 37;
	private static final int CARYCAPPOSITION = 24;
	private static final int GROSSWTPOSITION = 51;
	private static final int DIESELPOSITION = 68;
	private static final int FEEASSESSEDPOSITION = 47;
	private static final int PAYMENTHEADERPOSITION = 55;
	private static final int PAYMENTPOSITION = 60;
	private static final int TOTALPOSITION = 65;
	private static final int DOLLARSIGNPOSITION = 80;
	private static final int MONEYPOSITION = 81;
	private static final int TRANSOBJPOSITION = 0;
	// defect 10596
	//private static final int ITEMPRICELENGTH = 12;
	private static final int ITEMPRICELENGTH = 14;
	// end defect 10596
	private static final int YEARLENGTH = 4;
	//defect 5868
	//changed from 25 to 29
	private static final int FEEASSESSEDLENGTH = 29;
	//end defect 5868
	private static final int LENGTH12 = 12;
	private static final int LENGTH1 = 1;
	private static final int LENGTH5 = 5;

	private static final String VINSTRING =
		"VEHICLE IDENTIFICATION NO: ";
	private static final String YRMAKESTRING = "YR/MAKE: ";
	private static final String EMPTYWTSTRING = "EMPTY WT: ";
	private static final String CARYCAPSTRING = "CARRYING CAPACITY: ";
	private static final String GROSSWTSTRING = "GROSS WT: ";
	private static final String DIESELSTRING = "DIESEL";
	private static final String INVITMSTRING = "INVENTORY ITEM(S)";
	private static final String YEARSTRING = "YR";
	private static final String FEEASSESSEDSTRING = "FEES ASSESSED";
	private static final String TOTALSTRING = "TOTAL";
	private static final String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT";
	private static final String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	private static final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
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
	private static final String LARGECOUNTYSTRING = "L";
	private static final String DOLLARSIGN = "$";
	private static final String SLASH = "/";

	/**
	 * GenNonResidentAgriculturePermitReceipts constructor
	 */
	public GenNonResidentAgriculturePermitReceipts()
	{
		super();
	}
	/**
	 * GenNonResidentAgriculturePermitReceipts constructor
	 * 
	 * @param asRcptName String
	 * @param aaRcptProps ReceiptProperties
	 */
	public GenNonResidentAgriculturePermitReceipts(
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
	 * This method generates the Registration Renewal Receipt.
	 * 
	 * @param lvDataVector Vector
	 */
	public void formatReceipt(Vector lvDataVector)
	{
		// Get the CompleteTransData object out of the vector
		caCTData =
			(CompleteTransactionData) lvDataVector.elementAt(
				TRANSOBJPOSITION);
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
		// skip down 5 lines from title of receipt
		caRpt.blankLines(5);
		// Process the Vehicle Information
		printVehicleData();
		// Process Inventory Items and Fees
		printInventoryAndFees();
		// Process Notations and Payment
		printNotationsAndPayment(lvDataVector);
		// Process Messages
		caRpt.blankLines(58 - caRpt.getCurrX());
		printMessages();
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
		GenNonResidentAgriculturePermitReceipts laGnrAPR =
			new GenNonResidentAgriculturePermitReceipts(
				"NOT USED FOR RECEIPTS",
				laRcptProps);

		// Generating Demo data to display.
		// String method get its data from a generated object
		Vector lvFakeDataVector = laGnrAPR.queryData1();
		try
		{
			laGnrAPR.formatReceipt(lvFakeDataVector);
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
			// block code block
		}

		laPout.print(laGnrAPR.caRpt.getReport().toString());
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
			//Process p = Runtime.getRuntime().exec("cmd.exe
			// /c copy c:\\QuickCtrRpt.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of"
					+ " com.txdot.isd.rts.client.general.ui.RTSDialogBox");
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
		caRpt.print(YEARSTRING, YEARPOSITION, YEARLENGTH);
		caRpt.print(
			FEEASSESSEDSTRING,
			FEEASSESSEDPOSITION,
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

		int liInvCount = 0; // index through the inventory vector
		int liFeeCount = 0; // index through the Fees vector
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
						YEARPOSITION,
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
					FEEASSESSEDPOSITION,
					FEEASSESSEDLENGTH);
				caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);

				// defect 5889
				//  added substring(1) after printDollar() to
				//  get rid of the
				//  second dollar sign in front of the amount. 
				caRpt.rightAlign(
					laFeeData.getItemPrice().printDollar().substring(1),
					MONEYPOSITION,
					ITEMPRICELENGTH);
				//end defect 5889.

				liFeeCount = liFeeCount + 1;
				laFeeTotal = laFeeTotal.add(laFeeData.getItemPrice());
			}
			caRpt.nextLine();

			// if both vectors are finished turn off lbKeepLooping
			if (liInvCount >= lvAllocInvItms.size()
				&& (liFeeCount >= lvRegFees.size()))
			{
				lbKeepLooping = false;
			}
		} // end while

		if (!laFeeTotal.equals(laZeroDollar))
		{
			caRpt.nextLine();
			caRpt.print(TOTALSTRING, TOTALPOSITION, LENGTH5);
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			// defect 5889
			// added substring(1) after printDollar() to get rid of the
			// second dollar sign in front of the amount. 
			caRpt.rightAlign(
				laFeeTotal.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			//end defect 5889.
			caRpt.nextLine();
		}
		// end defect 10112
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
			
			// defect 10112 
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
		// end defect 10112 
		
		// this is where VTR messages would go if we used them!
		// There are three possible lines.
	}

	/**
	 * Print the Inventory and Fees Section.
	 * 
	 * @param avDataVector Vector
	 */
	public void printNotationsAndPayment(Vector avDataVector)
	{
		// build up the Payment Vector
		Vector lvPaymentData = buildPaymentVector(avDataVector);

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

		// index through the Vehicle Notations vector
		int liPaymentCount = 0; // index through the Payment vector

		// loop controller.  keep looping until it is set off.
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
					PAYMENTPOSITION,
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
			if ((lvPaymentData == null
				|| liPaymentCount >= lvPaymentData.size()))
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

		// print vehicle empty weight, carrying capacity, gross weight 
		caRpt.print(
			EMPTYWTSTRING,
			FIRSTCOLUMNPOSITION,
			EMPTYWTSTRING.length());

		caRpt.print(
			String.valueOf(
				caCTData
					.getVehicleInfo()
					.getVehicleData()
					.getVehEmptyWt()));

		caRpt.print(
			CARYCAPSTRING,
			CARYCAPPOSITION,
			CARYCAPSTRING.length());

		caRpt.print(
			String.valueOf(
				caCTData
					.getVehicleInfo()
					.getRegData()
					.getVehCaryngCap()));

		caRpt.print(
			GROSSWTSTRING,
			GROSSWTPOSITION,
			GROSSWTSTRING.length());

		caRpt.print(
			String.valueOf(
				caCTData
					.getVehicleInfo()
					.getRegData()
					.getVehGrossWt()));
		// check on DIESELINDI
		if (caCTData.getVehicleInfo().getVehicleData().getDieselIndi()
			> 0)
		{
			caRpt.print(DIESELSTRING, DIESELPOSITION, LENGTH12);
		}
		caRpt.blankLines(2);
	}

	/**
	 * This version of queryData returns the CompleteTransactionData
	 * 	object.
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

		laOwnrData.setName1("Ray Rowehl");
		laOwnrData.setName2("Roger Ramjet");

		laAddrData.setCity("Austin");
		laAddrData.setSt1("Some Street1");
		laAddrData.setState("TX");
		laAddrData.setZpcd("2222222");
		laAddrData.setZpcdp4("2222");
		laOwnrData.setAddressData(laAddrData);

		laMFdata.setOwnerData(laOwnrData);

		laVehData.setVehBdyType("4T");
		laVehData.setVehMk("HOND");
		laVehData.setVehModlYr(2002);
		laVehData.setVehEmptyWt(0);
		laVehData.setVin("1234567890asdfghj");
		laMFdata.setVehicleData(laVehData);

		laTtlData.setDocNo("11111111111111111");
		laTtlData.setTtlIssueDate(20011018);
		laMFdata.setTitleData(laTtlData);

		laRegData.setRegPltNo("B01BBBB");
		laMFdata.setRegData(laRegData);

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
		laTransData.setDisableAddlFees(0);
		laTransData.setDisableCtyFees(0);
		laTransData.setOfcIssuanceNo(170);
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
	 * This version of queryData returns the CompleteTransactionData
	 *  object.
	 * 
	 * @param lsQuery String
	 */
	public Vector queryData1()
	{
		Vector lvReceipt = new Vector();

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
