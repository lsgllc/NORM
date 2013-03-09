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
 * AdditionalSalesTaxReceipt.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/01/2001	Methods created
 * J Rue      	09/19/2001	Added comments
 * Ray Rowehl	06/01/2002	Add handling for Payment information.
 *							Some how this was completely missed?
 *							defect 4191
 * S Govindappa 07/10/2002  Added code for PCR25 in printPayment method
 * 							to display credit card fee addition message
 * MAbs	 		08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * K Harrell	05/31/2005	Java 1.4 Work
 * 							removed unused variables
 * 							defect 7896 Ver 5.2.3  
 * S Johnston	06/28/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify queryData()
 *							defect 7896 Ver 5.2.3
 * K Harrell	01/28/2009	Change Due Payment Type is not used 
 * 							modify printPayment()
 * 							defect 8469 Ver Defect_POS_D
 * Min Wang	10/08/2010		Allow printing of the Max length of the price. 
 * 							modify ITEMPRICELENGTH  
 * 							defect 10596 Ver 6.6.0   
 * ---------------------------------------------------------------------
 */
/**
 * Additional Sales Tax receipt.
 *
 * @version	6.6.0			10/08/2010
 * @author Jeff Rue
 * <br>Creation Date:		09/24/2001	15:52:10
 */
public class AdditionalSalesTaxReceipt extends ReceiptTemplate
{
	int COL_11 = 11;
	int COL_21 = 21;

	// lengths
	private static final int LENGTH_1 = 1;
	// defect 10596
	//private static final int ITEMPRICELENGTH = 12;
	private static final int ITEMPRICELENGTH = 14;
	// end defect 10596

	// column positions
	private static final int COLUMN = 5;
	private static final int PAYMENTPOSITION = COLUMN + 55;
	private static final int PAYMENTHEADERPOSITION = COLUMN + 50;

	private static final int DOLLARSIGNPOSITION = COLUMN + 75;
	private static final int MONEYPOSITION = COLUMN + 77;

	// text strings
	private static final String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	private static final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	private static final String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	boolean cbCreditFeeIncMsgIndi = true;

	/**
	 * AdditionalSalesTaxReceipt constructor
	 * 
	 */
	public AdditionalSalesTaxReceipt()
	{
		// empty code block
	}
	/**
	 * AdditionalSalesTaxReceipt constructor
	 * 
	 * @param asRcptString String
	 * @param aaRcptProperties ReceiptProperties
	 */
	public AdditionalSalesTaxReceipt(
		String asRcptString,
		ReceiptProperties aaRcptProperties)
	{
		super(asRcptString, aaRcptProperties);
	}
	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param avResults Vector
	 */
	public void formatReceipt(Vector avResults)
	{
		int liTransDataObj = 0;

		CompleteTransactionData laTransData =
			(CompleteTransactionData) avResults.elementAt(
				liTransDataObj);

		try
		{
			generateReceiptHeader(avResults);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}

		RegFeesData laRegFeesData =
			(RegFeesData) laTransData.getRegFeesData();
		MFVehicleData laMFVehData =
			(MFVehicleData) laTransData.getVehicleInfo();
		RegistrationData laRegData = new RegistrationData();
		VehicleData laVehData = new VehicleData();

		if (laMFVehData != null && laMFVehData.getRegData() != null)
		{
			laRegData = laMFVehData.getRegData();
		}

		if (laMFVehData != null
			&& laMFVehData.getVehicleData() != null)
		{
			laVehData = laMFVehData.getVehicleData();
		}

		// **************  VIN AND REG EXP YR INFORMATION  *************

		String lsVin = laVehData.getVin();
		String lsExpYr = Integer.toString(laRegData.getRegExpYr());

		caRpt.print(VEH_INFO_TEXT, COL_05, charLength(VEH_INFO_TEXT));
		caRpt.nextLine();
		caRpt.print(VIN_TEXT, COL_05, charLength(VIN_TEXT));
		if (lsVin != null)
		{
			caRpt.print(lsVin, COL_11, charLength(lsVin));
		}
		caRpt.nextLine();
		caRpt.print(EXP_YR_TEXT, COL_05, charLength(EXP_YR_TEXT));
		if (!lsExpYr.equals("0"))
		{
			caRpt.print(lsExpYr, COL_21, charLength(lsExpYr));
		}
		caRpt.blankLines(5);

		// ***************  FEES INFORMATION  ***************
		Vector lvRegFeesData = laRegFeesData.getVectFees();
		FeesData laFeesData = new FeesData();
		String lsDesc;
		Dollar laItemPrice;

		String lsFEE = "FEES ASSESSED";
		this.caRpt.print(lsFEE, 49, lsFEE.length());
		this.caRpt.nextLine();

		for (int i = 0; i < lvRegFeesData.size(); i++)
		{
			laFeesData = (FeesData) lvRegFeesData.elementAt(i);
			lsDesc = laFeesData.getDesc();
			laItemPrice = laFeesData.getItemPrice();
			printFees(lsDesc, laItemPrice);
		}
		this.caRpt.nextLine();
		printFeeTotal();

		// defect 4191
		// add call for payment processing
		this.caRpt.blankLines(2);
		printPayment(avResults);
		// end defect 4191
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
		AdditionalSalesTaxReceipt laASTR =
			new AdditionalSalesTaxReceipt(
				"NOT USED FOR RECEIPTS",
				laRcptProps);

		// Generating Demo data to display.
		String lsQuery = "Select * FROM RTS_TBL";

		Vector lvQueryASTRPreliminary = laASTR.queryData(lsQuery);
		laASTR.formatReceipt(lvQueryASTRPreliminary);

		//Writing the Formatted String onto a local file 
		File laOutputFile;
		FileOutputStream laFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\RTS\\RCPT\\ASTRECEIPT.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}

		laPout.print(laASTR.getReceipt().toString());
		laPout.close();

		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\RTS\\RCPT\\ASTRECEIPT.RPT");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
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
				"Exception occurred in main() of"
					+ " com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}

	}

	/**
	 * Print the Payment Section.
	 * 
	 * @param avDataVector Vector 
	 */
	public void printPayment(Vector avDataVector)
	{
		// build up the Payment Vector
		Vector lvPaymentData = buildPaymentVector(avDataVector);

		// print payment header if there are any payment objects 
		if (lvPaymentData.size() > 0)
		{
			this.caRpt.print(
				PAYMENTSTRING,
				PAYMENTHEADERPOSITION + 2,
				PAYMENTSTRING.length());
		}

		Dollar laZeroDollar = new Dollar(0);
		Dollar laPaymentTotal = new Dollar(0);
		Dollar laChangeBackAmt = new Dollar(0);

		// defect 8469 
		// Not used 
		// int liChangeBackType = 0;
		// end defect 8469 

		// loop through the payment vector to print all payment lines.
		for (int i = 0; i < lvPaymentData.size(); i++)
		{
			// get the payment line
			TransactionPaymentData laPaymentData =
				(TransactionPaymentData) lvPaymentData.elementAt(i);
			// make sure there is a non zero value before printing.
			// defect 1336
			if (!laPaymentData.getPymntTypeAmt().equals(laZeroDollar))
			{
				// start this printing on the next line
				this.caRpt.nextLine();
				// print the payment description
				this.caRpt.print(
					laPaymentData.getPymntType(),
					PAYMENTPOSITION + 6,
					laPaymentData.getPymntType().length());
				// print the check number if it exists
				if (laPaymentData.getPymntCkNo() != null)
				{
					this.caRpt.print(
						" #" + laPaymentData.getPymntCkNo());
				}

				// print the payment line money
				this.caRpt.print(
					DOLLAR_SIGN,
					DOLLARSIGNPOSITION,
					LENGTH_1);
				this.caRpt.rightAlign(
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
						this.caRpt.nextLine();
						this.caRpt.print(
							lsCreditCardFeeMessage,
							MONEYPOSITION - 5,
							lsCreditCardFeeMessage.length());
					}
				}

				laPaymentTotal =
					(laPaymentTotal
						.add(laPaymentData.getPymntTypeAmt()));
			}
			// add up any change due 
			if ((laPaymentData.getChngDue() != null)
				&& !laPaymentData.getChngDue().equals(laZeroDollar))
			{
				laChangeBackAmt = laPaymentData.getChngDue();
				// defect 8469 
				// Not Used 
				// liChangeBackType = laPaymentData.getChngDuePymntType();
				// end defect 8469 
			}
		}

		// Print the Total Payment Line.  Print the total if there
		// was data.
		// Even if the total is zero (CQU100001336)
		if (lvPaymentData.size() > 0)
		{
			this.caRpt.blankLines(2);
			this.caRpt.print(
				PAYMENTTOTALSTRING,
				PAYMENTPOSITION + 2,
				PAYMENTTOTALSTRING.length());
			this.caRpt.print(DOLLAR_SIGN, DOLLARSIGNPOSITION, LENGTH_1);
			this.caRpt.rightAlign(
				laPaymentTotal.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			this.caRpt.nextLine();
		}

		// Print out the Change Due Line
		if (!laChangeBackAmt.equals(laZeroDollar))
		{
			this.caRpt.print(
				PAYMENTCHANGEDUESTRING,
				PAYMENTPOSITION + 2,
				PAYMENTCHANGEDUESTRING.length());
			this.caRpt.print(DOLLAR_SIGN, DOLLARSIGNPOSITION, LENGTH_1);
			this.caRpt.rightAlign(
				laChangeBackAmt.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			this.caRpt.nextLine();
		}

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

		CompleteTransactionData laTransData =
			new CompleteTransactionData();
		laTransData = (CompleteTransactionData) Vehicle.getVeh();
		// see if the CompleteTransactionData fields get populated
		// after the previous statement
		lvReceipt.addElement(laTransData);

		return lvReceipt;
	}
}
