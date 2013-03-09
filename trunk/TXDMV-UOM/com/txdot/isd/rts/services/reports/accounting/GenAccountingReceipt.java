package com.txdot.isd.rts.services.reports.accounting;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.FeesData;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.OwnerData;
import com.txdot.isd.rts.services.data.RegFeesData;
import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.TitleData;
import com.txdot.isd.rts.services.data.TransactionPaymentData;
import com.txdot.isd.rts.services.data.VehicleData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 * GenAccountingReceipt.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/01/2001	Methods created
 * J Rue      	09/19/2001	Added comments
 * Btulsiani	04/09/2002	Fixed bug 3476-allignment prob
 * J Rue		05/10/2002	Fixed bug 3858, create printPymntReqd
 *							modify printPayment
 * S Govindappa 07/10/2002  Merged the PCR25 changes to display
 * 							credit card fee message
 *							by making changes to printPayment method
 * S Govindappa 07/10/2002  Fixed defect in printPymntReqd method to
 * 							print the payment information for PCR25
 * MAbs			08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * S Govindappa 02/20/2003  Fixed defect# 5383. Made changes to
 * 							formatReceipt(..) to use the VehClassCd
 * 							as the flag to determine if a vehicle
 * 							record was found or not instead of Doc
 * 							No to print the receipt for bogus Doc No
 * S Johnston	03/11/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify queryData()
 *							defect 7896 Ver 5.2.3
 * S Johnston	05/09/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3
 * K Harrell	05/12/2005 	refactoring of printTrlvTrlrLngth() to 
 * 							printTrvlTrlrLngth() in ReceiptTemplate
 * 							defect 7562 Ver 5.2.3  
 * K Harrell	05/22/2005	Java 1.4 Work; Constant rename from Receipt
 * 							Template. 
 * 							defect 7896 Ver 5.2.3 	
 * K Harrell	01/28/2009	Change Due Payment Type is not used 
 * 							modify printPayment()
 * 							defect 8469 Ver Defect_POS_D
 * K Harrell	07/12/2009	Implement new OwnerData() 
 * 							modify buildOwnrData(), formatReceipt()  
 * 							defect 10112 Ver Defect_POS_F 
 *----------------------------------------------------------------------
 */
/**
 * The GenAccountingReceipts prints the receipts for Refund, Hot Check
 * (Redeemed, Credit, Deduct). 
 * 
 * @version	Defect_POS_F	07/12/2009
 * @author	Jeff Rue
 * <br>Creation Date:		11/06/2001 15:44:03 
 */
public class GenAccountingReceipt extends ReceiptTemplate
{
	// boolean 
	public boolean cbCreditFeeIncMsgIndi = true;

	// Object 
	public CompleteTransactionData caTransData =
		new CompleteTransactionData();
	public FeesData caFeesData = new FeesData();
	public MFVehicleData caMFVehData = new MFVehicleData();
	public OwnerData caOwnrData = new OwnerData();
	public RegFeesData caRegFeeData = new RegFeesData();
	public RegistrationData caRegisData = new RegistrationData();
	public TitleData caTitleData = new TitleData();
	public VehicleData caVehicleData = new VehicleData();

	// Constants 
	public final int COLUMN = 5;
	public final int FIRSTCOLUMNPOSITION = 5;
	public final int SECONDCOLUMNPOSITION = 39;
	public final int THIRDCOLUMNPOSITION = 63;
	public final int CUSTOMERNAMEPOSITION = 5;
	public final int FEEASSESSEDPOSITION = 47;
	public final int TOTALPOSITION = 65;
	public final int DOLLARSIGNPOSITION = 80;
	public final int MONEYPOSITION = 81;
	public final int CRDTPOSITION = 58;
	public final int VEHINFOPOSITION = 5;
	public final int ODMTRREADINGPOSITION = 5;
	public final int ODMTRBRANDPOSITION = 30;
	public final int YEARPOSITION = 37;
	public final int YEARLENGTH = 4;
	public final int FEEASSESSEDLENGTH = 25;
	public final int ITEMPRICELENGTH = 13;
	public final int PAYMENTHEADERPOSITION = 55;
	public final int PAYMENTPOSITION = 64;
	public final int COLUMNLENGTH11 = 11;
	public final int COLUMNLENGTH30 = 30;
	public final int COLUMNLENGTH34 = 34;
	public final int LENGTH1 = 1;
	public final int LENGTH5 = 5;
	public final int BLANKLINES2 = 2;
	public final int BLANKLINES3 = 3;
	public final int BLANKLINES30 = 30;
	public final int BLANKLINES60 = 60;
	public final int TRANSOBJPOSITION = 0;
	public final String CUSTOMERNAMETEXT = "CUSTOMER NAME: ";
	public final String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	public final String PAYMENTTOTALSTRING = "TOTAL AMOUNT PAID ";
	public final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	public final String DOLLARSIGN = "$";

	/**
	 * GenAccountingReceipt
	 */
	public GenAccountingReceipt()
	{
		// empty code block
	}

	/**
	 * GenAccountingReceipt
	 * 
	 * @param asRcptString String
	 * @param asRcptProperties ReceiptProperties
	 */
	public GenAccountingReceipt(
		String asRcptString,
		ReceiptProperties asRcptProperties)
	{
		super(asRcptString, asRcptProperties);
	}

	/**
	 * Build customer name vector.
	 * 
	 * @return Vector
	 */
	private Vector buildOwnrData()
	{
		Vector lvReturn = new Vector();
		lvReturn.addElement(OWNR_ADDR_TEXT);
		lvReturn.addElement("");
		
		// defect 10112 
		lvReturn.addElement(caOwnrData.getName1());
		// end defect 10112
		 
		lvReturn.addElement("");
		for (int i = 0; i < lvReturn.size(); i++)
		{
			if (lvReturn.elementAt(i) == null)
			{
				lvReturn.setElementAt("", i);
			}
		}
		return lvReturn;
	}

	/**
	 * Build registration data description vector.
	 * 
	 * @return Vector
	 */
	private Vector buildRegisData()
	{
		Vector lvReturn = new Vector();
		// **************  TEMPORARY ADDITIONAL WEIGHT **************  
		// Get Registration Class Description
		lvReturn.addElement("");
		lvReturn.addElement(REG_CLASS_TEXT);
		lvReturn.addElement("");
		lvReturn.addElement(PLATE_TYPE_TEXT);
		lvReturn.addElement("");
		lvReturn.addElement(STICKER_TYPE_TEXT);
		return lvReturn;
	}

	/**
	 * Generate the receipts fof the Accounting
	 *
	 * @param avResults Vector
	 */
	public void formatReceipt(Vector avResults)
	{
		int liTransDataObj = 0;
		Vector lvRcptInfo = new Vector();
		// Parse TransData object
		caTransData =
			(CompleteTransactionData) avResults.elementAt(
				liTransDataObj);
		caMFVehData = (MFVehicleData) caTransData.getVehicleInfo();
		caOwnrData = (OwnerData) caMFVehData.getOwnerData();
		caRegisData = (RegistrationData) caMFVehData.getRegData();
		caTitleData = (TitleData) caMFVehData.getTitleData();
		caVehicleData = (VehicleData) caMFVehData.getVehicleData();
		caRegFeeData = (RegFeesData) caTransData.getRegFeesData();
		try
		{
			if (caVehicleData != null
				&& caVehicleData.getVehClassCd() != null
				&& !caVehicleData.getVehClassCd().trim().equals(""))
			{
				generateReceiptHeader(avResults);
			}
			else
			{
				printNoRecsRcptHeader(avResults);
			}
		} // end try box
		catch (RTSException aeRTSEx)
		{
			caRpt.println(
				"com.txdot.isd.rts.services.reports.accounting "
					+ aeRTSEx);
		}
		// ***************  VEHICLE INFORMATION  ***************
		lvRcptInfo =
			parseRegisInfoAcct(caVehicleData, caRegisData, caTitleData);
		printVehDataAcct(lvRcptInfo);
		lvRcptInfo = new Vector();

		// ***************  CUSTOMER NAME  ***************
		String lsTransCd = caTransData.getTransCode();
		if (lsTransCd.equals(TRANSCD_REFUND)
			|| lsTransCd.equals(TRANSCD_RFCASH))
		{
			// defect 10112 
			printCustName(caOwnrData.getName1());
			// end defect 10112 
		}

		// ***************  FEES INFORMATION  ***************
		printFeesData(caRegFeeData.getVectFees());
		caRpt.blankLines(1);

		// *************** print Payment Info ***************
		printPayment(avResults);
		caRpt.blankLines(1);

		// ***************  VEHICLE NOTATIONS  ***************
		if (caVehicleData != null
			&& caVehicleData.getVehClassCd() != null
			&& !caVehicleData.getVehClassCd().trim().equals(""))
		{
			lvRcptInfo = buildNotationsVector(caTransData);
		}
		else
		{
			lvRcptInfo.addElement("");
		}
		printVehNotation(lvRcptInfo, COL_05);
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
		try
		{
			CacheManager.loadCache();
			// Instantiating a new Report Class 
			ReceiptProperties laRcptProps = new ReceiptProperties();
			GenAccountingReceipt laGpr =
				new GenAccountingReceipt(
					"NOT USED FOR RECEIPTS",
					laRcptProps);
			// Generating Demo data to display.
			String lsQuery = "Select * FROM RTS_TBL";
			Vector lvQueryDTAPreliminary = laGpr.queryData(lsQuery);
			laGpr.formatReceipt(lvQueryDTAPreliminary);
			//Writing the Formatted String onto a local file
			File laOutputFile;
			FileOutputStream laFout;
			PrintStream laPout = null;
			laOutputFile = new File("c:\\RTS\\RCPT\\RECEIPT.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);
			laPout.print(laGpr.getReceipt().toString());
			laPout.close();
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
			//Process p = Runtime.getRuntime().exec("cmd.exe /c copy
			// c:\\QuickCtcRpt.txt prn");
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
	 * Parse registration data, add to returning vector.
	 * 
	 * @param aaVehData Object
	 * @param aaRegisData Object
	 * @param aaTitleData Object
	 * @return Vector
	 */
	private Vector parseRegisInfoAcct(
		Object aaVehData,
		Object aaRegisData,
		Object aaTitleData)
	{
		int liVehDataSize = 13;
		Vector lvVehData = new Vector();
		// Determine if data exist
		if (aaVehData != null
			&& aaRegisData != null
			&& aaTitleData != null)
		{
			// Set vehicle Make/Year
			int liVehModlYr = caVehicleData.getVehModlYr();
			String lsVehMk = caVehicleData.getVehMk();
			if (lsVehMk == null)
			{
				lsVehMk = "";
			}
			String lsYrMk = "";
			if (liVehModlYr < 10)
			{
				lsYrMk = String.valueOf(liVehModlYr) + "   /" + lsVehMk;
			}
			else if (liVehModlYr < 100)
			{
				lsYrMk = String.valueOf(liVehModlYr) + "  /" + lsVehMk;
			}
			else if (liVehModlYr < 1000)
			{
				lsYrMk = String.valueOf(liVehModlYr) + " /" + lsVehMk;
			}
			else
			{
				lsYrMk = String.valueOf(liVehModlYr) + "/" + lsVehMk;
			}
			// Add data to the vector
			lvVehData.addElement(caVehicleData.getVehClassCd());
			lvVehData.addElement(caRegisData.getPrevPltNo());
			lvVehData.addElement(caVehicleData.getVin());
			lvVehData.addElement(lsYrMk);
			lvVehData.addElement(caVehicleData.getVehModl());
			lvVehData.addElement(caVehicleData.getVehBdyType());
			lvVehData.addElement(caTitleData.getVehUnitNo());
			lvVehData.addElement(
				new Integer(caVehicleData.getVehEmptyWt()));
			lvVehData.addElement(
				new Integer(caRegisData.getVehCaryngCap()));
			lvVehData.addElement(
				new Integer(caRegisData.getVehGrossWt()));
			if (caVehicleData.getVehTon() == null)
			{
				caVehicleData.setVehTon(new Dollar("0.00"));
			}
			lvVehData.addElement(caVehicleData.getVehTon());
			lvVehData.addElement(caVehicleData.getVehBdyVin());
			lvVehData.addElement(
				new Integer(caVehicleData.getVehLngth()));
			for (int i = 0; i < lvVehData.size(); i++)
			{
				if (lvVehData.elementAt(i) == null)
				{
					lvVehData.setElementAt("", i);
				}
			}
		} // end if
		else
		{
			for (int i = 0; i < liVehDataSize; i++)
			{
				lvVehData.addElement("");
			}
		}
		return lvVehData;
	}

	/**
	 * Set printer pointer to correct print row.
	 * 
	 * @param asCustName String
	 */
	public void printCustName(String asCustName)
	{
		int liCustomerRow = 29;
		for (int i = caRpt.getCurrX(); i < liCustomerRow; i++)
		{
			caRpt.nextLine();
		}
		caRpt.print(
			CUSTOMERNAMETEXT,
			COL_49,
			charLength(CUSTOMERNAMETEXT));
		caRpt.println(asCustName);
	}

	/**
	 * Print Fees data.
	 *
	 * @param avFeesData Vector
	 */
	private void printFeesData(Vector avFeesData)
	{
		String lsFeesHeading = "";
		Vector lvRegFeeData = new Vector();
		lvRegFeeData = caRegFeeData.getVectFees();
		try
		{
			int liFeesRow = 31;
			for (int i = caRpt.getCurrX(); i < liFeesRow; i++)
			{
				caRpt.blankLines(1);
			}
			// Print the Fees heading
			lsFeesHeading =
				generaterFeesHeader(caTransData.getTransCode());
			caRpt.print(
				lsFeesHeading,
				COL_49,
				charLength(lsFeesHeading));

			caRpt.blankLines(1);

			// Print account item codes and fees to the vector
			for (int i = 0; i < lvRegFeeData.size(); i++)
			{
				caFeesData = (FeesData) avFeesData.elementAt(i);
				printFees(
					caFeesData.getDesc(),
					caFeesData.getItemPrice());
			}
			caRpt.blankLines(1);
			printFeeTotal();

			// Print Refund type: CASH or CHECK
			if (caTransData.getTransCode().equals(TRANSCD_REFUND))
			{
				caRpt.blankLines(2);
				caRpt.print(
					RF_CHECK_TEXT,
					COL_68,
					charLength(RF_CHECK_TEXT));
			}
			else if (caTransData.getTransCode().equals(TRANSCD_RFCASH))
			{
				caRpt.blankLines(2);
				caRpt.print(
					RF_CASH_TEXT,
					COL_68,
					charLength(RF_CASH_TEXT));
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leEx = new RTSException("", aeNPEx);
		}
	}

	/**
	 * Print No Recs Rcpt Header
	 * 
	 * @param avRcptHeader Vector
	 */
	private void printNoRecsRcptHeader(Vector avRcptHeader)
	{
		try
		{
			// Parse TransData object
			buildDataObjects(avRcptHeader);
			// ***************  NAME OF THE RECEIPT ***************
			determineAndPrintHeading(caTransData.getTransCode());
			caRpt.blankLines(5);

			// **********  Print County/TAC or Office names ************
			printCntyOffice(SystemProperty.getOfficeIssuanceNo());
			caRpt.nextLine();

			// ******  PRINT THE FIRST THREE LINES OF THE HEADER *******
			printFirst3HeaderLines();
		}
		catch (RTSException aeRTSEx)
		{
			caRpt.println(
				"com.txdot.isd.rts.services.reports.accounting "
					+ aeRTSEx);
		}
		// ***** OWNER INFORMATION - EXCLUDE ADDITIONAL SALES TAX ******
		// *******************  OWNER INFORMATION  *********************
		Vector lvOwnrData = buildOwnrData();
		printOwnerInfo(lvOwnrData);

		// *************  REGIS/PLATE/STICKER DESCRIPTION  *************
		Vector lvRegisData = buildRegisData();
		printRegisDesc(lvRegisData);

		caRpt.blankLines(2);
	}

	/**
	 * Print the Inventory and Fees Section.
	 * 
	 * @param avDataVector Vector
	 */
	public void printPayment(Vector avDataVector)
	{
		// Do not print payment if amount <= 0.00 (return false)
		boolean lbPrintPymnt = printPymntReqd(avDataVector);
		if (!lbPrintPymnt)
		{
			return;
		}
		// build up the Payment Vector
		Vector lvPaymentData = buildPaymentVector(avDataVector);
		// Do not print payment for any offices except counties
		if (lvPaymentData != null && lvPaymentData.size() > 0)
		{
			this.caRpt.nextLine();
			this.caRpt.print(
				PAYMENTSTRING,
				PAYMENTHEADERPOSITION + 2,
				PAYMENTSTRING.length());
		}
		caRpt.nextLine();
		Dollar laZeroDollar = new Dollar(0);
		Dollar laPaymentTotal = new Dollar(0);
		Dollar laChangeBackAmt = new Dollar(0);
		// defect 8469
		// Not used  
		// int liChangeBackType = 0;
		// end defect 8469 
		for (int i = 0; i < lvPaymentData.size(); i++)
		{
			TransactionPaymentData laPaymentData =
				(TransactionPaymentData) lvPaymentData.elementAt(i);
			this.caRpt.print(
				laPaymentData.getPymntType(),
				PAYMENTPOSITION + 2,
				laPaymentData.getPymntType().length());
			// print the check number if it exists
			//&& lPaymentData.getPymntCkNo().length() > 0
			if (laPaymentData.getPymntCkNo() != null)
			{
				this.caRpt.print(" #" + laPaymentData.getPymntCkNo());
			}
			this.caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			this.caRpt.rightAlign(
				laPaymentData
					.getPymntTypeAmt()
					.printDollar()
					.substring(
					1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			laPaymentTotal =
				(laPaymentTotal.add(laPaymentData.getPymntTypeAmt()));
			if (laPaymentData.getChngDue() != null
				&& !laPaymentData.getChngDue().equals(laZeroDollar))
			{
				laChangeBackAmt = laPaymentData.getChngDue();
				// defect 8469 
				// Not used; ChngDuePymntType() deleted 
				//liChangeBackType = laPaymentData.getChngDuePymntType();
				// end defect 8469 
			}
			this.caRpt.nextLine();

			//Code for PCR25 changes
			if (cbCreditFeeIncMsgIndi
				&& laPaymentData.isCreditCardFee())
			{
				cbCreditFeeIncMsgIndi = false;
				String lsCreditCardFeeMessage =
					formatCreditCardFeeMessage();
				if (lsCreditCardFeeMessage != null)
				{
					this.caRpt.rightAlign(
						lsCreditCardFeeMessage,
						MONEYPOSITION - 5,
						lsCreditCardFeeMessage.length());
					this.caRpt.nextLine();
				}
			}
		}
		// Print the Total Payment Line
		if (!laPaymentTotal.equals(laZeroDollar))
		{
			this.caRpt.nextLine();
			this.caRpt.print(
				PAYMENTTOTALSTRING,
				60 + 2,
				PAYMENTTOTALSTRING.length());
			this.caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
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
				60 + 2,
				PAYMENTCHANGEDUESTRING.length());
			this.caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			this.caRpt.rightAlign(
				laChangeBackAmt.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			//	this.cRpt.blankLines(60 - cRpt.getCurrX());
		}
	}

	/**
	 * Set boolean if printing of payment  data is required.
	 * true  => print payment
	 * false => do not print payment
	 * 
	 * @param avTransPymntData Vector
	 * @return boolean
	 */
	private boolean printPymntReqd(Vector avTransPymntData)
	{
		boolean lbRtn = false;
		Dollar laPymntAmt = new Dollar(0.00);
		Dollar laZeroDollar = new Dollar(0);
		for (int i = 0; i < avTransPymntData.size(); i++)
		{
			// pull the object out so we can inspect it
			Object laThisElement = avTransPymntData.elementAt(i);
			// inspect the object to see if it is a
			// TransactionPaymentData object
			if (laThisElement instanceof TransactionPaymentData)
			{
				TransactionPaymentData laPaymentData =
					(
						TransactionPaymentData) avTransPymntData
							.elementAt(
						i);
				laPymntAmt = laPaymentData.getPymntTypeAmt();
				int liAnswr = laPymntAmt.compareTo(laZeroDollar);
				if (liAnswr > 0)
				{
					lbRtn = true;
				}
			}
		}
		return lbRtn;
	}

	/**
	 * Print Vehicle Data Account
	 * 
	 * @param avVehdata Vector
	 */
	protected void printVehDataAcct(Vector avVehdata)
	{
		int liVehInfoRow = 23;

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
							COL_05);
						break;
					}
				case 1 : // Print PrevPltNo
					{
						printPrevPltNo(
							avVehdata.elementAt(i).toString(),
							COL_40);

						caRpt.blankLines(1);
						break;
					}
				case 2 : // Print VIN
					{
						printVIN(
							avVehdata.elementAt(i).toString(),
							COL_05);

						caRpt.blankLines(1);
						break;
					}
				case 3 : // Print Year/Make
					{
						printVehYrMk(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 4 : // Print Model
					{
						printVehModl(
							avVehdata.elementAt(i).toString(),
							25);
						break;
					}
				case 5 : // Print Body Style
					{
						printVehBdyStyle(
							avVehdata.elementAt(i).toString(),
							37);
						break;
					}
				case 6 : // Print Unit number
					{
						printVehUnitNo(
							avVehdata.elementAt(i).toString(),
							COL_55);

						caRpt.blankLines(1);
						break;
					}
				case 7 : // Print Empty weight
					{
						printVehEmptyWt(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 8 : // Print Carrying capacity
					{
						printCarryCap(
							avVehdata.elementAt(i).toString(),
							23);
						break;
					}
				case 9 : // Print Gross Weight
					{
						printVehGrossWt(
							avVehdata.elementAt(i).toString(),
							COL_50);
						break;
					}
				case 10 : // Print Tonnage
					{
						printVehTon(
							avVehdata.elementAt(i).toString(),
							COL_67);

						caRpt.blankLines(1);
						break;
					}
				case 11 : // Print Body VIN
					{
						printVehBdyVIN(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 12 : // Print Vehicle Length
					{
						printTrvlTrlrLngth(
							avVehdata.elementAt(i).toString(),
							COL_62);

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
	 * queryData
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
		lvReceipt.addElement(laTransData);
		return lvReceipt;
	}
}
