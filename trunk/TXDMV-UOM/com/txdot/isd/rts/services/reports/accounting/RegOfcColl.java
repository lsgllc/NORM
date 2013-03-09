package com.txdot.isd.rts.services.reports.accounting;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * RegOfcColl.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name       	Date        Description
 * ------------	-----------	--------------------------------------------
 * Jeff Rue		01/29/2002	new class
 * Ray Rowehl	02/07/2002	Print Payment Total and Header if
 * 							there are any payment objects.
 *							Print Notations at
 *							FirstColumnPosition. (CQU100001336)
 * Ray Rowehl	04/25/2002	Print Change Due regardless of
 * 							payment amount.	(CQU100003550)
 * Jeff Rue		05/03/2002	Fix problem in accumulating total
 * 							CQU100003460
 * S Govindappa	05/15/2002	Fixed CQU100003928 Changed 
 * 							printPayment(
 * 							CompleteTransactionData) to prevent
 * 							liPaymentCount from being
 * 							incremented twice.
 * S Govindappa	07/10/2002	Merged the PCR25 code for credit
 * 							card fee display
 * MAbs	 		08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * B Arredondo	11/08/2002	Defect 4954, added For loop to
 * 							method buildFeesAssessed() to
 * 							include MiscFees that are added on.
 * S Johnston	03/11/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify queryData()
 *							defect 7896 Ver 5.2.3
 * S Johnston	05/10/2005	fix @param on JavaDoc, <p> to <br>
 * 							chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3
 * K Harrell	05/22/2005	Java 1.4 Work; Constant rename from Receipt
 * 							Template. 
 * 							defect 7896 Ver 5.2.3
 * K Harrell	01/25/2008	Modify to use AccountingConstant for 
 * 							Maximum Inventory Items in RGNCOL 
 * 							modify buildInvIssued() 
 * 							defect 9519 Ver 3 Amigos Prep  
 * K Harrell	01/28/2009	Change Due Payment Type is not used 
 * 							modify printPayment()
 * 							defect 8469 Ver Defect_POS_D 
 * K Harrell	02/28/2009	Do not print "Note:" on CCO Receipt. 
 * 							printPayment() does not use parameter.
 * 							Changed variables from protected to private
 * 							 - no classes/subclasses w/ which to share. 
 * 							Made methods private vs. public where 
 * 							appropriate.  
 * 							add printPayment()
 * 							delete caFeesData, caFundFunc, caPymntData,
 * 							 laTotalFees,
 * 							 RENEWLPOSITION, AMTPOSITION, 
 * 							 ITEMPRICEPOSITION, TONNAGEPOSITION 
 * 							delete printPayment(CompleteTransactionData),
 * 							  setTotalFees() 
 * 							modify formatReceipt()
 * 							defect 9969 Ver Defect_POS_E 
 * Min Wang		11/08/2010	Accommodate new max field length of the price.
 * 							modify ITEMPRICELENGTH
 * 							defect 10656 Ver 6.6.0
 * ---------------------------------------------------------------------
 */
/**
 * Regional Office Collections Receipt.  Also used for CCO. 
 *
 * @version	.6.6.0 		11/08/2010
 * @author	Jeff Rue
 * <br>Creation Date:	01/29/2002 08:51:38
 */
public class RegOfcColl extends ReceiptTemplate
{
	// boolean 
	private boolean cbCreditFeeIncMsgIndi = true;

	// Object 
	private CompleteTransactionData caTransData =
		new CompleteTransactionData();
	private RegFeesData caRegFeeData = new RegFeesData();
	private ProcessInventoryData caProcessInvData =
		new ProcessInventoryData();

	// Vector 
	private Vector cvFeesData = new Vector();
	private Vector cvPymntData = new Vector();

	// Constants 
	// int 
	private final int COLUMN = 5;
	private final int PAYMENTPOSITION = COLUMN + 55;
	private final int PAYMENTHEADERPOSITION = COLUMN + 50;
	private final int FEEASSESSEDPOSITION = COLUMN + 44;
	private final int FEEASSESSEDLENGTH = 30;
	private final int TOTALPOSITION = COLUMN + 65;
	// defect 10656
	//private final int ITEMPRICELENGTH = 12;
	private final int ITEMPRICELENGTH = 14;
	// end defect 10656
	private final int DOLLARSIGNPOSITION = COLUMN + 75;
	private final int MONEYPOSITION = COLUMN + 77;
	
	// String 
	private final static String TOTALSTRING = "TOTAL";
	private final static String NUMBERTEXT = "NUMBER";

	/**
	 * RegOfcColl constructor
	 */
	public RegOfcColl()
	{
		super();
	}

	/**
	 * RegOfcColl constructor
	 * 
	 * @param asRcptString String
	 * @param aaRcptProperties ReceiptProperties
	 */
	public RegOfcColl(
		String asRcptString,
		ReceiptProperties aaRcptProperties)
	{
		super(asRcptString, aaRcptProperties);
	}

	/**
	 * Build the print stream for Fees assessed
	 *
	 * @return Vector
	 */
	private Vector buildFeesAssessed()
	{
		Dollar laTotalFees = new Dollar("0.00");
		Vector lvReturn = new Vector();
		int liFeeCount = 0;
		this.caRpt.print(
			FEES_ASSESSED_TEXT,
			FEEASSESSEDPOSITION,
			FEES_ASSESSED_TEXT.length());
		this.caRpt.nextLine();
		//defect 4954
		//added For loop to include MiscFees
		if (cvFeesData != null || cvFeesData.size() != 0)
		{
			for (int i = 0; i < cvFeesData.size(); i++)
			{
				FeesData laFeeData =
					(FeesData) cvFeesData.elementAt(liFeeCount);
				this.caRpt.print(
					laFeeData.getDesc().toUpperCase(),
					FEEASSESSEDPOSITION,
					FEEASSESSEDLENGTH);
				this.caRpt.print("$", DOLLARSIGNPOSITION, 1);
				// S447500 just prints the price assumes that qty is 1
				this.caRpt.rightAlign(
					laFeeData.getItemPrice().printDollar().substring(1),
					MONEYPOSITION,
					ITEMPRICELENGTH);
				this.caRpt.nextLine();
				liFeeCount = liFeeCount + 1;
				laTotalFees =
					(laTotalFees.add(laFeeData.getItemPrice()));
			}
		}
		this.caRpt.blankLines(2);
		this.caRpt.print(
			TOTALSTRING,
			TOTALPOSITION - 3,
			TOTALSTRING.length());
		this.caRpt.print("$", DOLLARSIGNPOSITION, 1);
		this.caRpt.rightAlign(
			laTotalFees.printDollar().substring(1),
			MONEYPOSITION,
			ITEMPRICELENGTH);
		return lvReturn;
	}

	/**
	 * buildInvDescYr
	 * 
	 * @param aaInvProcsData Object
	 * @return String
	 */
	private String buildInvDescYr(Object aaInvProcsData)
	{
		ProcessInventoryData laInvProcsData =
			(ProcessInventoryData) aaInvProcsData;
		String lsInvDesc =
			getCachePlateTypeDesc(laInvProcsData.getItmCd());
		String lsInvYr = "";
		if (laInvProcsData.getInvItmYr() != 0)
		{
			lsInvYr = String.valueOf(laInvProcsData.getInvItmYr());
		}
		String lsInvItmNo = laInvProcsData.getInvItmNo();
		String lsResult =
			UtilityMethods.addPaddingRight(
				new String[] { lsInvDesc, lsInvYr, lsInvItmNo },
				new int[] { 31, 5, 12 },
				" ");
		return lsResult;
	}

	/**
	 * Build inventory description and yr for printing.
	 * 
	 * @return Vector
	 */
	private Vector buildInvIssued()
	{
		// defect 9519 
		int liMaxInvLn = AccountingConstant.MAX_RGN_COLL_INV_QTY;
		// end defect 9519 
		Vector lvHoldData = new Vector();
		Vector lvReturn = new Vector();
		lvHoldData = caTransData.getAllocInvItms();
		if (lvHoldData == null)
		{
			lvHoldData = new Vector();
		}
		if (lvHoldData.size() > 0)
		{
			lvReturn.addElement(
				INV_ITM_TEXT
					+ caRpt.blankSpaces(14)
					+ INV_ITM_YR_TEXT
					+ caRpt.blankSpaces(3)
					+ NUMBERTEXT);
		}
		for (int i = 0; i < liMaxInvLn; i++)
		{
			if (i < lvHoldData.size())
			{
				caProcessInvData =
					(ProcessInventoryData) lvHoldData.elementAt(i);
				String lsIsInvDescYr = buildInvDescYr(caProcessInvData);
				lvReturn.addElement(lsIsInvDescYr);
			}
			else
			{
				lvReturn.addElement(caRpt.blankSpaces(34));
			}
		}
		return lvReturn;
	}

	/**
	 * Format Regional Office Collections procedures
	 * 
	 * @param avResults Vector
	 */
	public void formatReceipt(Vector avResults)
	{
		int liTransDataObj = 0;
		Vector lvHoldInvData = new Vector();
		Vector lvHoldFeeData = new Vector();
		// Parse TransData object
		caTransData =
			(CompleteTransactionData) avResults.elementAt(
				liTransDataObj);
		caRegFeeData = caTransData.getRegFeesData();
		cvFeesData = (Vector) caRegFeeData.getVectFees();
		// Parse Payment data
		cvPymntData = (Vector) buildPaymentVector(avResults);
		try
		{
			generateReceiptHeader(avResults);
		}
		catch (Exception aeEx)
		{
			// empty code block
		}
		caRpt.blankLines(3);

		// **************  PRINT INVENTORY ITEMS, LINE 5  **************
		lvHoldInvData = buildInvIssued();
		printInv(lvHoldInvData);

		// **************  PRINT PAYMENT, LINE 16 **************
		caRpt.blankLines(25 - this.caRpt.getCurrX());
		lvHoldFeeData = buildFeesAssessed();

		// defect 9969
		// No parameter required. 
		printPayment();

		// **************  PRINT NOTE, LINE 44 **************
		// CCO does not provide an input field for the "Note:" section 
		if (!caTransData.getTransCode().equals(TransCdConstant.CCO))
		{
			caRpt.blankLines(53 - this.caRpt.getCurrX());
			printMsg();
		}
		// end defect 9969  
	}

	/**
	 * Main method to run Title Receipts.
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			CacheManager.loadCache();
			// Instantiating a new Report Class 
			ReceiptProperties laRcptProps = new ReceiptProperties();
			RegOfcColl laGPRgpr =
				new RegOfcColl("NOT USED FOR RECEIPTS", laRcptProps);
			// Generating Demo data to display.
			String lsQuery = "Select * FROM RTS_TBL";
			Vector lvQuery = laGPRgpr.queryData(lsQuery);
			laGPRgpr.formatReceipt(lvQuery);
			//Writing the Formatted String onto a local file
			File laOutputFile;
			FileOutputStream laFout;
			PrintStream laPout = null;
			laOutputFile = new File("c:\\RTS\\RCPT\\REGCOL.RCP");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
			laPout.print(laGPRgpr.getReceipt().toString());
			laPout.close();
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\RTS\\RCPT\\RECEIPT.RPT");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			laFrmPreviewReport.setVisible(true);
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec("cmd.exe /c copy
			// c:\\QuickCtcRpt.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.services.report.title."
					+ "GenTitleReceipt");
			aeEx.printStackTrace(System.out);
		}
	}
	/**
	 * Print Inventory
	 * 
	 * @param avInv Vector
	 */
	private void printInv(Vector avInv)
	{
		String lsInvData = "";
		for (int i = 0; i < avInv.size(); i++)
		{
			lsInvData = (String) avInv.elementAt(i);
			caRpt.print(lsInvData, COL_05, charLength(lsInvData));
			caRpt.nextLine();
		}
	}
	
	/**
	 * Print reason message.
	 * 
	 */
	private void printMsg()
	{
		int liMsgPos = 44;
		for (int i = caRpt.getCurrX(); i < liMsgPos; i++)
		{
			caRpt.nextLine();
		}
		String lsReason = caRegFeeData.getReason();
		if (lsReason == null)
		{
			lsReason = "";
		}
		String lsMsg = "Note: " + lsReason;
		caRpt.print(lsMsg, COL_05, charLength(lsMsg));
	}

	/**
	 * Print the Inventory and Fees Section.
	 * 
	 * @param aaTransData CompleteTransactionData
	 */
	private void printPayment()
	{
		this.caRpt.blankLines(2);
		// print payment header if there are any payment objects
		if (cvPymntData.size() > 0)
		{
			this.caRpt.print(
				PAYMENTSTRING,
				PAYMENTHEADERPOSITION + 2,
				PAYMENTSTRING.length());
			this.caRpt.nextLine();
		}
		Dollar laZeroDollar = new Dollar(0);
		Dollar laPaymentTotal = new Dollar(0);
		Dollar laChangeBackAmt = new Dollar(0);

		// index through the Vehicle Notations vector
		int liPaymentCount = 0; // index through the Payment vector
		// loop controller.  keep looping until it is set off.
		boolean lbKeepLooping = true;
		while (lbKeepLooping)
		{
			if (cvPymntData != null
				&& liPaymentCount < cvPymntData.size())
			{
				TransactionPaymentData laPaymentData =
					(TransactionPaymentData) cvPymntData.elementAt(
						liPaymentCount);
				// Add up Change Due regardless of payment made.
				if ((laPaymentData.getChngDue() != null)
					&& !laPaymentData.getChngDue().equals(laZeroDollar))
				{
					laChangeBackAmt = laPaymentData.getChngDue();
				}

				// make sure there is a non zero value before printing.
				//		(CQU100001336)
				if (!laPaymentData
					.getPymntTypeAmt()
					.equals(laZeroDollar))
				{
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
					this.caRpt.print(
						DOLLAR_SIGN,
						DOLLARSIGNPOSITION,
						1);
					this.caRpt.rightAlign(
						laPaymentData
							.getPymntTypeAmt()
							.printDollar()
							.substring(
							1),
						MONEYPOSITION,
						ITEMPRICELENGTH);
					liPaymentCount = liPaymentCount + 1;
					laPaymentTotal =
						laPaymentTotal.add(
							laPaymentData.getPymntTypeAmt());
					//Code for PCR25                                                         
					if (cbCreditFeeIncMsgIndi
						&& laPaymentData.isCreditCardFee())
					{
						cbCreditFeeIncMsgIndi = false;
						if (laPaymentData.isCreditCardFee())
						{
							String lsCreditCardFeeMessage =
								formatCreditCardFeeMessage();
							if (lsCreditCardFeeMessage != null)
							{
								this.caRpt.blankLines(1);
								this.caRpt.rightAlign(
									lsCreditCardFeeMessage,
									MONEYPOSITION - 5,
									lsCreditCardFeeMessage.length());
							}
						}
					}
				}
				else
				{
					++liPaymentCount;
				}
			}
			this.caRpt.nextLine();

			// if both vectors are finished turn off lbKeepLooping
			// remove notation vector stuff for now..
			if ((cvPymntData == null
				|| liPaymentCount >= cvPymntData.size()))
			{
				lbKeepLooping = false;
			}
		}
		// Print the Total Payment Line.  Print the total if there was
		// data.
		// Even if the total is zero (CQU100001336)
		if (cvPymntData.size() > 0)
		{
			this.caRpt.nextLine();
			this.caRpt.print(
				PAYMENTTOTALSTRING,
				PAYMENTPOSITION + 2,
				PAYMENTTOTALSTRING.length());
			this.caRpt.print(DOLLAR_SIGN, DOLLARSIGNPOSITION, 1);
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
			this.caRpt.print(DOLLAR_SIGN, DOLLARSIGNPOSITION, 1);
			this.caRpt.rightAlign(
				laChangeBackAmt.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			this.caRpt.nextLine();
		}
	}

	/**
	 * Load the three objects into local class vectors.
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		Vector lvReceipt = new Vector();
		// TransData
		CompleteTransactionData laTransData =
			new CompleteTransactionData();
		laTransData = (CompleteTransactionData) Vehicle.getVeh();
		lvReceipt.addElement(laTransData);
		lvReceipt.addElement(cvPymntData);
		return lvReceipt;
	}
}
