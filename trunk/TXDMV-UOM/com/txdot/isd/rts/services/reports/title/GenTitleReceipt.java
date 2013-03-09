package com.txdot.isd.rts.services.reports.title;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;
import com.txdot.isd.rts.services.reports.registration.GenRegistrationReceipts;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * GenTitleReceipt.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Salvi		10/17/2002	CQU100004905 Fix - changed the format of 
 *							vehicle width to feet and inches from just 
 *							inches.
 * J Rue		09/14/2001	Added method printPymntReqd(avResults) to 
 *							determine if payment was made.
 * J Rue		05/20/2002	Fixed 4015, $$ printing for Registration 
 *							Penalty. Should be $ sign. Refer to
 *							buildDieselRegPentlyFee().
 * J Rue		05/24/2002	Fix bug 4079, payment not printing. Set 
 *							conditional to if PymntAmt > 0.00. 
 *							printPymntReqd()
 * S Govindappa 07/10/2002 	Merged PCR25 changes by making changes to 
 *							buildPymnt method
 * MAbs	 		08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * B Arredondo	09/04/2003	method buildDieselRegPentlyFee() removed
 *							the Else from an If Else If statement
 *							to be able to print both itemized fees.
 *							defect 6541
 * J Rue		09/12/2003	Format javaFile/javaDoc comments
 * J Rue		12/17/2003	Print lienholder info even if bar code 
 *							receipt.
 *							modify printFooterMessage()
 *							defect 6199, 6748, Ver 5.1.5.2
 * K Harrell	04/06/2004	5.2.0 Merge.  See PCR 34 comments
 *							modify formatReceipt()
 *							Ver 5.2.0 
 * B Hargrove	11/15/2004	Need to check that Diesel Indi is still
 *							set before printing the diesel fee.
 *							*Notice that  diesel fee will still be
 *							in MVFuncTrans. Title Correction does
 *							change reg fees.*
 *							modify buildDieselRegPentlyFee()
 *						    defect 6893  Ver 5.2.2
 * B Hargrove	11/24/2004	Cleanup method comments. Remove
 *							'Creation date: (1/2/02 8:48:52 AM)'.
 *							modify buildDieselRegPentlyFee()
 *						    defect 6893  Ver 5.2.2
 * K Harrell	05/12/2005	Modify the minimum starting location
 *	J Rue					for lienholder & automation msg.
 *							Continued Java Cleanup.
 *							deprecate printLienholderMsg() 
 *							modify printFooterMessage()
 *							defect 7896,8193 Ver 5.2.2 Fix 3
 * K Harrell	05/19/2005 	refactoring of printTrlvTrlrLngth() to 
 * 							printTrvlTrlrLngth() in ReceiptTemplate
 * 							defect 7562 Ver 5.2.3
 * K Harrell	05/22/2005	Java 1.4 Work; Constant rename from Receipt
 * 							Template. 
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify getCacheOwnrshipEvidenceVector(),
 * 							getCacheSalesTaxCatDesc(), main(),
 * 							queryData()
 *							defect 7896 Ver 5.2.3
 * K Harrell	10/17/2005	Use addPaddingRight(String,int,String) vs.
 *    	 					addPaddingRight(String[],int[],String)
 * 							add'l Java 1.4 Cleanup  
 * 							defect 7896 Ver 5.2.3 
 * Jeff S.		04/06/2007	More manufacturing notation work.
 * 							modify buildInvIssued()
 * 							defect 9145 Ver Special Plates
 * B Hargrove	04/07/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify buildDieselRegPentlyFee()
 * 							defect 9631 Ver Defect POS A
 * K Harrell	06/02/2008	Modify Sales Tax date constant to include
 * 							"Date of Assignment/" 
 * 							modify SALESTAXDATETEXT
 * 							defect 9584 Ver Defect POS A 
 * K Harrell	03/02/2009	Streamline using new Lienholder/Address 
 * 							methods. Prefaced objects with "private". 
 * 							delete caGenRegisRcp, caPymntData 
 * 							delete printLienholderMsg()
 * 							modify parseLienHld1(),2(),3(),
 * 							 parseTitleData1()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	03/04/2009	Implement new text for Electronic Title
 * 							add ELTMSG
 * 							delete FIRSTCOLUMNPOSITION
 * 							delete  printTitleMessages()
 * 							modify printFooterMessages()
 * 							defect 9978 Ver Defect_POS_E
 * K Harrell	07/10/2009	Implement new LienholderData
 * 							modify buildSalesTaxInfo(), 
 * 							 getItmPrice(), parseLienHld1(), 
 * 							 parseLienHld2(),parseLienHld3(), 
 * 							 printFooterMessages()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	12/16/2009	refactored caCTData from caTransData
 * 							modify formatReceipt() 
 * 							defect 10290 Ver Defect_POS_H 
 * K Harrell	09/28/2010	modify formatReceipt()
 * 							defect 10590 Ver 6.6.0 
 * K Harrell	11/02/2010	DTA should not show special characters in 
 * 							Preview Receipt. 
 * 							modify formatReceipt()
 * 							defect 10652 Ver 6.6.0 
 * B Hargrove 	11/10/2010  Token Trailer now does not print sticker.
 * 							They want to store inv detail, just don't 
 * 							print sticker.
 * 							Should print from tray 2.
 * 							modify formatReceipt()
 * 							defect 10623 Ver 6.6.0
 * Min Wang		01/26/2011  Remove the text of the automation fee
 * 							delete GenRegistrationReceipts.	
 * 									AUTOMATION_1_STRING
 * 									GenRegistrationReceipts.
 * 									AUTOMATION_2_STRING
 * 							defect 10715 Ver 6.7.0
 * Min Wang		02/07/2011  modify buildFeesAssessed()
 * 							defect 10715 Ver 6.7.0
 * K Harrell	03/28/2012	Print Permit No where IMCNOReqd = 1 vs. 
 * 							  hard coded description.
 * 							delete cbAutomateFees,
 * 							  SELLERFINANCEDSALESTEXT,RENTALTEXT  
 * 							delete getCacheSalesTaxCatDesc()
 * 							modify getPermitNo() 
 * 							defect 11242 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */
/**
 *
 * Create receipt for Title transaction.
 *
 * @version	6.10.0  		03/28/2012
 * @author	Jeff Rue
 * <br>Creation Date:		12/18/2001 11:13:45
 */
public class GenTitleReceipt extends ReceiptTemplate
{
	// booleans 
	private boolean cbBarCode = false;
	private boolean cbCreditFeeIncMsgIndi = true;

	// Vector
	private Vector cvFeesData = new Vector();
	private Vector cvPymntData = new Vector();

	// Object
	private DealerTitleData caDlrTtlData = new DealerTitleData();
	private FeesData caFeesData = new FeesData();
	private MFVehicleData caMFVehData = new MFVehicleData();
	private OwnerData caOwnrData = new OwnerData();
	private ProcessInventoryData caProcessInvData =
		new ProcessInventoryData();
	private RegFeesData caRegFeeData = new RegFeesData();
	private RegistrationData caRegisData = new RegistrationData();
	private TitleData caTitleData = new TitleData();

	// defect 10290 
	// refactored from caTransData 
	private CompleteTransactionData caCTData =
		new CompleteTransactionData();
	// end defect 10290 
	private VehicleData caVehicleData = new VehicleData();
	private VehMiscData caVehMiscData = new VehMiscData();

	// Constant 
	private final static String ANDREGISTRATION = " AND REGISTRATION.";
	private final static String BATCHCNTTEXT = "Batch Count: ";
	private final static String BATCHNOTEXT = "Batch No: ";
	private final static String CHECKTEXT = "CHECK";
	private final static int COL_05 = 5;
	private final static int COL_10 = 10;
	private final static int COL_11 = 11;
	private final static int COL_27 = 27;
	private final static int COL_91 = 91;
	private final static String COMMERCIALVEHICLES2 =
		"THIS RECEIPT TO BE CARRIED IN THE VEHICLE FOR WHICH ISSUED.";
	private final static String DIESELTEXT = "DIESEL FEE ";
	private final static String DOLLARSIGNTEXT = "$";
	private final static String ELTMSG =
		"TITLE WILL BE ELECTRONICALLY FILED WITH THE LIENHOLDER.";
	private final static String EXEMPTEXT = "EXEMPT";
	private final static String FEESASSESSEDTEXT = "FEES ASSESSED";
	private final static String ITEMIZEDTEXT = "ITEMIZED FEES";
	private final static String OTHERSTATETAXTEXT =
		"Less Other State Tax Paid";
	private final static String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	private final static String PENALTYTEXT = "PENALTY FEE";
	private final static String POUNDSIGNTEXT = " #";
	private final static String PROOFTTLAPPLRCPT =
		"THIS RECEIPT IS YOUR PROOF OF APPLICATION FOR"
			+ " CERTIFICATE OF TITLE";
	private final static String SALESPRICETEXT = "Sales Price";
	private final static String SALESTAXCATEXT = "SALES TAX CATEGORY: ";
	private final static String SALESTAXDATETEXT =
		"Date of Assignment/Sales Tax Date: ";
	private final static String SALESTAXPAIDTEXT = "Sales Tax Paid";
	private final static String SLSTXPEN = "SLSTXPEN";
	private final static String TAXABLEAMOUNTTEXT = "Taxable Amount";
	private final static String TAXPENALTYTEXT = "Tax Penalty";
	private final static String TOTALTAXPAIDTEXT = "TOTAL TAX PAID";
	private final static String TOTALTEXT = "TOTAL  ";
	private final static String TRADEINALLOWANCETEXT =
		"Less Trade In Allowance";

	private final static String TTLMAILEDTOLIENHLD =
		"CERTIFICATE OF TITLE WILL BE MAILED TO 1st LIENHOLDER.";
	
	
	// defect 11242 
	//private boolean cbAutomateFees = false;
	//private final static String SELLERFINANCEDSALESTEXT =
	//	"SELLER-FINANCED SALE";
	// private final static String RENTALTEXT = "RENTAL";
	// end defect 11242
	
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
			GenTitleReceipt laGPR =
				new GenTitleReceipt(
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

			laOutputFile = new File("c:\\RTS\\RCPT\\RECEIPT.RPT");
			laFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(laFout);

			laPout.print(laGPR.getReceipt().toString());
			laPout.close();

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
			// changed setVisible to setVisibleRTS
			laFrmPreviewReport.setVisibleRTS(true);
			// end defect 7590
			// One way to Print Report.
			//Process p = Runtime.getRuntime().exec("cmd.exe /c
			// copy c:\\QuickCtcRpt.txt prn");

		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of"
					+ " com.txdot.isd.rts.services.report.title."
					+ "GenTitleReceipt");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * GenTitleReceipt constructor
	 */
	public GenTitleReceipt()
	{
		super();
	}

	/**
	 * GenTitleReceipt constructor
	 */
	public GenTitleReceipt(
		String asRcptString,
		ReceiptProperties asRcptProperties)
	{
		super(asRcptString, asRcptProperties);
	}

	/**
	 * Build Batch Number
	 * 
	 * @return String
	 */
	protected String buildBatchNo()
	{
		String lsBatchNo = "";
		String lsBatchCt = "";
		String lsRtrn = "";

		// Set batch number	
		if (caCTData.getTransCode().equals(TRANSCD_DTANTD)
			|| caCTData.getTransCode().equals(TRANSCD_DTANTK)
			|| caCTData.getTransCode().equals(TRANSCD_NONTTL))
		{
			lsBatchNo = BATCHNOTEXT + caRpt.blankSpaces(COL_16);
		}
		else
		{
			lsBatchNo = BATCHNOTEXT + caTitleData.getBatchNo();
			// Set field length to = 26
			for (int i = lsBatchNo.length(); i < COL_26; i++)
			{
				lsBatchNo = lsBatchNo + caRpt.blankSpaces(1);
			}
		}

		// Set batch count
		if (caCTData.getTransCode().equals(TRANSCD_DTANTD)
			|| caCTData.getTransCode().equals(TRANSCD_DTANTK)
			|| caCTData.getTransCode().equals(TRANSCD_NONTTL))
		{
			lsBatchCt = BATCHCNTTEXT + caRpt.blankSpaces(COL_08);
		}
		else
		{
			lsBatchCt =
				BATCHCNTTEXT + String.valueOf(caCTData.getBatchCount());
			// Set field length to = 26
			for (int i = lsBatchCt.length(); i < COL_21; i++)
			{
				lsBatchCt = lsBatchCt + caRpt.blankSpaces(1);
			}
		}

		lsRtrn = lsBatchNo + lsBatchCt;

		return lsRtrn;
	}

	/**
	 * Build vector for Diesel and Registration Penalty Fees.
	 *
	 * @return Vector
	 */
	private Vector buildDieselRegPentlyFee()
	{
		Dollar laDollarZero = new Dollar("0.00");
		int liMaxLineCnt = 3;

		Vector lvReturn = new Vector();

		// Set heading
		// defect 6893  
		// Check if Diesel Indi is still set
		if (caVehicleData.getDieselIndi() == 1)
		{
			if (caRegisData.getCustDieselFee() != null
				&& caRegisData.getCustDieselFee().compareTo(laDollarZero)
					> 0)
			{
				String lsItemText =
					UtilityMethods.addPaddingRight(
						ITEMIZEDTEXT,
						42,
						" ");

				lvReturn.addElement(lsItemText);

				String lsDieselFee =
					formatDollarAmt(
						caRegisData
							.getCustDieselFee()
							.printDollar()
							.substring(
							1),
						COL_11);
				String lsDiesel = DIESELTEXT + lsDieselFee;
				String lsPaddedDesc =
					UtilityMethods.addPaddingRight(lsDiesel, 42, " ");
				lvReturn.addElement(lsPaddedDesc);
			} // end if/Diesel
		}
		// end 6893 

		// defect 6541
		// Removed the Else to be able to print both itemized fees. 
		if (caCTData.getRegisPenaltyFee() != null
			&& caCTData.getRegisPenaltyFee().compareTo(laDollarZero) > 0)
			//end defect 6541
		{
			String lsPenaltyFee =
				formatDollarAmt(
					caCTData
						.getRegisPenaltyFee()
						.printDollar()
						.substring(
						1),
					COL_11);
			String lsPenaltyF = PENALTYTEXT + lsPenaltyFee;
			String lsPaddedDesc =
				UtilityMethods.addPaddingRight(lsPenaltyF, 42, " ");
			lvReturn.addElement(lsPaddedDesc);
		} // end if/penalty

		for (int i = lvReturn.size(); i < liMaxLineCnt; i++)
		{
			lvReturn.addElement("");
		}

		return lvReturn;
	}

	/**
	 * Build the print stream for Fees assessed
	 * 
	 * @return Vector
	 */
	public Vector buildFeesAssessed()
	{
		String lsFeesDesc = "";
		String lsFees = "";
		String lsFeesPadded = "";

		Dollar laTotalFees = new Dollar("0.00");

		Vector lvReturn = new Vector();

		FeesData laFeesData = new FeesData();

		// **********  ADD PRINT POSITION FOR FEES ASSESED **********
		String lsBlankSpcs = caRpt.blankSpaces(COL_80 - COL_50 + 2);
		String lsFeesText = FEESASSESSEDTEXT + lsBlankSpcs;
		lvReturn.addElement(lsFeesText);

		if (cvFeesData != null || cvFeesData.size() != 0)
		{
			for (int i = 0; i < cvFeesData.size(); i++)
			{
				laFeesData = (FeesData) cvFeesData.elementAt(i);
				// Case the record
				lsFees =
					laFeesData.getItemPrice().printDollar().substring(
						1);
				// Get the item price
				lsFeesPadded = formatDollarAmt(lsFees, 13);
				// Padd fees amount to 11 chars
				String lsPaddedDesc =
					UtilityMethods.addPaddingRight(
						laFeesData.getDesc(),
						29,
						" ");
				lsFeesDesc = lsPaddedDesc + " " + lsFeesPadded;
				// Concatanate Descr and fees
				lvReturn.addElement(lsFeesDesc);
				// Add string to vector
				laTotalFees =
					laTotalFees.add(laFeesData.getItemPrice());
				// defect 10715
				// Accumulate total fees
//				if (laFeesData.getAcctItmCd().equals("AUTOMATE"))
//				{
//					cbAutomateFees = true;
//				}
				// end defect 10715
			} // end for loop
		} // end if
		lvReturn.addElement(
			TOTALTEXT
				+ "     "
				+ formatDollarAmt(
					laTotalFees.printDollar().substring(1),
					13));

		if (caCTData.getCrdtRemaining() != null
			&& !caCTData.getCrdtRemaining().equals(new Dollar("0.00")))
		{
			lvReturn.addElement(
				"** CREDIT REMAINING -"
					+ formatDollarAmt(
						caCTData
							.getCrdtRemaining()
							.printDollar()
							.substring(
							1),
						13));
		}
		lsBlankSpcs = caRpt.blankSpaces(COL_91 - COL_50);
		lvReturn.addElement(lsBlankSpcs);
		// Adding a blank line from column 50 to 91	
		return lvReturn;
	}

	/**
	 * Build inventory description and yr for printing.
	 * 
	 * @param  aaInvProcsData Object
	 * @return String
	 */
	private String buildInvDescYr(Object aaInvProcsData)
	{
		ProcessInventoryData laInvProcsData =
			(ProcessInventoryData) aaInvProcsData;

		int liInvItmYr = 0;
		String lsInvYr = "";
		String lsInvDesc = "";
		String lsInvDescYr = "";

		lsInvDesc = getCachePlateTypeDesc(laInvProcsData.getItmCd());
		liInvItmYr = laInvProcsData.getInvItmYr();

		// Convert inventory item year to a string, set to blank if zero.
		if (liInvItmYr == 0)
		{
			lsInvYr = "";
		}
		else
		{
			lsInvYr = String.valueOf(liInvItmYr);
		}

		int liInvDescCol = COL_31 - charLength(lsInvDesc);

		lsInvDescYr =
			lsInvDesc + caRpt.blankSpaces(liInvDescCol) + lsInvYr;

		return lsInvDescYr;
	}

	/**
	 * Build inventory description and yr for printing.
	 * 
	 * @return Vector
	 */
	private Vector buildInvIssued()
	{
		int liMaxInvLn = 3;

		Vector lvHoldData = new Vector();
		Vector lvReturn = new Vector();

		// Get Inventory vector
		lvHoldData = caCTData.getAllocInvItms();

		// defect 9145
		// Add manufacturing notation
		SpecialPlatesRegisData laSpclPltsRegisData =
			caCTData.getVehicleInfo().getSpclPltRegisData();
		String lsMfgPltCd = CommonConstant.STR_SPACE_EMPTY;

		if (laSpclPltsRegisData != null
			&& laSpclPltsRegisData.getMFGStatusCd() != null
			&& laSpclPltsRegisData.getMFGStatusCd().equals(
				SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD))
		{
			ProcessInventoryData laProInvData =
				new ProcessInventoryData();
			laProInvData.setItmCd(laSpclPltsRegisData.getRegPltCd());
			laProInvData.setInvItmNo(laSpclPltsRegisData.getRegPltNo());
			lsMfgPltCd = laSpclPltsRegisData.getRegPltCd();
			laProInvData.setInvItmYr(laSpclPltsRegisData.getInvItmYr());

			if (caCTData.getAllocInvItms() == null)
			{
				caCTData.setAllocInvItms(new Vector());
			}
			caCTData.getAllocInvItms().addElement(laProInvData);
		}
		// end defect 9145

		if (lvHoldData == null)
		{
			lvHoldData = new Vector();
		}
		// Add Inventory Item text to return vector
		if (lvHoldData.size() > 0)
		{
			lvReturn.addElement(
				INV_ITM_TEXT
					+ caRpt.blankSpaces(
						COL_31 - charLength(INV_ITM_TEXT))
					+ INV_ITM_YR_TEXT);
		}
		else
		{
			lvReturn.addElement(caRpt.blankSpaces(34));
		}

		// Build Inventory issued data, add to return vector
		for (int i = 0; i < liMaxInvLn; i++)
		{
			if (i < lvHoldData.size())
			{
				caProcessInvData =
					(ProcessInventoryData) lvHoldData.elementAt(i);
				String lsIsInvDescYr = buildInvDescYr(caProcessInvData);
				lvReturn.addElement(lsIsInvDescYr);
				// defect 9145
				// Add manufacturing notation
				if (caProcessInvData
					.getItmCd()
					.trim()
					.equals(lsMfgPltCd))
				{
					lvReturn.addElement(MFG_REQ_NOTATION);
				}
				// end defect 9145
			}
			else
			{
				lvReturn.addElement(caRpt.blankSpaces(34));
			}
		}

		// Remove element for correct processing in Transaction
		if (!lsMfgPltCd.equals(""))
		{
			caCTData.getAllocInvItms().removeElementAt(
				caCTData.getAllocInvItms().size() - 1);
		}

		return lvReturn;
	}

	/**
	 * Build Odometer Reading and Brand string.
	 * 
	 * @return String
	 */
	private String buildOdomtr()
	{
		String lsOdomtrRdng = "";
		String lsBlanks = "";
		String lsOdomtrBrnd = "";

		if (caVehicleData.getVehOdmtrReadng() != null
			&& !caVehicleData.getVehOdmtrReadng().equals(""))
		{
			lsOdomtrRdng =
				VEH_ODO_RDNG_TEXT + caVehicleData.getVehOdmtrReadng();
		}
		else
		{
			lsOdomtrRdng =
				VEH_ODO_RDNG_TEXT + caRpt.blankSpaces(COL_10);
		}
		lsBlanks = caRpt.blankSpaces(COL_27 - lsOdomtrRdng.length());
		String lsBrand = caVehicleData.getVehOdmtrBrnd();
		if (lsBrand == null)
		{
			lsBrand = "";
		}
		lsOdomtrBrnd =
			lsOdomtrRdng + lsBlanks + VEH_ODO_BRND_TEXT + lsBrand;

		return lsOdomtrBrnd;
	}

	/**
	 * Build the print stream for Payment.
	 * 
	 * @return Vector
	 */
	public Vector buildPymnt(Vector avResults)
	{
		String lsPymntDesc = "";
		String lsPymnt = "";
		String lsPymntPadded = "";
		String lsBlankSpcs = "";

		Dollar laTotalPymnt = new Dollar("0.00");

		Vector lvReturn = new Vector();

		// Do not print payment if amount <= 0.00
		boolean lbPrintPymnt = printPymntReqd(avResults);
		int liCounter = 5;
		if (!lbPrintPymnt)
		{
			// Add 5 blank lines to the end of the payment
			for (int i = 0; i < liCounter; i++)
			{
				lvReturn.addElement(lsBlankSpcs);
				// Adding a blank line from column 50 to 91
			}
			return lvReturn;
		}

		TransactionPaymentData laPymntData =
			new TransactionPaymentData();

		// **********  ADD PRINT POSITION FOR PAYMENT **********
		lsBlankSpcs =
			caRpt.blankSpaces(
				COL_80 - COL_50 - METHOD_OF_PAY_TEXT.length());
		String lsPymntText = METHOD_OF_PAY_TEXT + lsBlankSpcs;
		if (cbBarCode)
		{
			lvReturn.addElement("");
		}
		else
		{
			lvReturn.addElement(lsPymntText);
		}
		Dollar laChangeDue = new Dollar("0.00");
		if ((cvPymntData != null || cvPymntData.size() != 0))
		{
			for (int i = 0; i < cvPymntData.size(); i++)
			{
				laPymntData =
					(TransactionPaymentData) cvPymntData.elementAt(i);
				// Case the record
				lsPymnt =
					laPymntData
						.getPymntTypeAmt()
						.printDollar()
						.substring(
						1);
				// Get the item price
				lsPymntPadded = formatDollarAmt(lsPymnt, 13);
				// Pad Pymnt amount to 11 chars
				if (laPymntData.getChngDue() != null
					&& !laPymntData.getChngDue().equals(
						new Dollar("0.00")))
				{
					laChangeDue = laPymntData.getChngDue();
				}

				String lsPaddedDesc =
					UtilityMethods.addPaddingRight(
						laPymntData.getPymntType(),
						13,
						" ");

				// Concatanate Descr and Pymnt
				if (laPymntData.getPymntType().equals(CHECKTEXT))
				{
					lsPymntDesc =
						laPymntData.getPymntType()
							+ POUNDSIGNTEXT
							+ laPymntData.getPymntCkNo();
					lsPaddedDesc =
						UtilityMethods.addPaddingRight(
							lsPymntDesc,
							13,
							" ");
				}

				//String lsPymntType = laPymntData.getPymntType();

				lsPymntDesc = lsPaddedDesc + lsPymntPadded;

				if (cbBarCode)
				{
					lvReturn.addElement("");
				}
				else
				{
					lvReturn.addElement(lsPymntDesc);
				}
				// Add string to vector

				//CODE FOR PCR25
				if (cbCreditFeeIncMsgIndi
					&& laPymntData.isCreditCardFee())
				{
					cbCreditFeeIncMsgIndi = false;
					String lsCreditCardFeeMessage =
						formatCreditCardFeeMessage();
					if (lsCreditCardFeeMessage != null)
					{
						// Move down 1 lines
						lvReturn.addElement(lsCreditCardFeeMessage);
					}
				}

				laTotalPymnt =
					laTotalPymnt.add(laPymntData.getPymntTypeAmt());
				// Accumulate total Pymnt
			} // end for loop
		} // end if
		lsBlankSpcs = caRpt.blankSpaces(COL_91 - COL_50);
		lvReturn.addElement(lsBlankSpcs);
		// Adding a blank line from column 50 to 91
		if (cbBarCode)
		{
			lvReturn.addElement("");
		}
		else
		{
			lvReturn.addElement(
				TOTALAMTPAIDTEXT
					+ formatDollarAmt(
						laTotalPymnt.printDollar().substring(1),
						13));
		}
		liCounter = 3;
		if (!laChangeDue.equals(new Dollar("0.00")))
		{
			//  lvReturn.addElement(lsBlankSpcs);
			if (cbBarCode)
			{
				lvReturn.addElement("");
			}
			else
			{
				lvReturn.addElement(
					UtilityMethods.addPaddingRight(
						PAYMENTCHANGEDUESTRING,
						17,
						" ")
						+ formatDollarAmt(
							laChangeDue.printDollar().substring(1),
							13));
			}
			liCounter = 2;
		}

		// Add 3 blank lines to the end of the payment
		for (int i = 0; i < liCounter; i++)
		{
			lvReturn.addElement(lsBlankSpcs);
		}

		return lvReturn;
	}

	/**
	 * Get the Sales Price Text
	 *
	 * @return String
	 */
	protected String buildSalesPriceText()
	{
		int liResult = 0;

		String lsSalesPriceText = "0.00";
		Dollar laItmPrice = new Dollar("0.00");
		Dollar laRebate = caVehMiscData.getTotalRebateAmt();
		if (laRebate == null)
		{
			laRebate = new Dollar("0.00");
		}

		if (caVehMiscData != null)
		{
			liResult = laRebate.compareTo(laItmPrice);
			if (liResult > 0)
			{
				lsSalesPriceText =
					SALESPRICETEXT
						+ " (Less "
						+ laRebate.printDollar()
						+ " rebate)";
			}
			else
			{
				lsSalesPriceText = SALESPRICETEXT;
			}
		}
		else
		{
			return lsSalesPriceText;
		}

		return lsSalesPriceText;
	}

	/**
	 * Build the print stream for Payment.
	 *
	 * @return Vector
	 */
	public Vector buildSalesTaxInfo()
	{
		// defect 10112 
		//String lsSalesTaxCatDesc = "";
		// end defect 10112 

		String lsSalesTaxCat = "";

		String lsBlankSpcs = "";
		Vector lvReturn = new Vector();

		// *************  SALES TAX CATEGORY  *************
		// defect 10112 
		//		if (caVehMiscData != null)
		//		{
		//			lsSalesTaxCatDesc = caVehMiscData.getSalesTaxCat();
		//		}
		// end defect 10112 

		if (caVehMiscData == null)
		{
			lsSalesTaxCat = SALESTAXCATEXT + caRpt.blankSpaces(14);
		}
		else
		{
			if (caVehMiscData.getSalesTaxCat() != null)
			{
				String lsPaddedDesc =
					UtilityMethods.addPaddingRight(
						caVehMiscData.getSalesTaxCat(),
						14,
						" ");
				lsSalesTaxCat = SALESTAXCATEXT + lsPaddedDesc;
			}
			else
			{
				lsSalesTaxCat = SALESTAXCATEXT + caRpt.blankSpaces(14);
			}
		}
		lvReturn.addElement(lsSalesTaxCat);

		// *************  PERMIT NUMBER  *************
		String lsPermitNo = getPermitNo();
		if (lsPermitNo == null || lsPermitNo.equals("0"))
		{
			// Adding a blank line from column 50 to 91
			lvReturn.addElement(caRpt.blankSpaces(COL_91 - COL_50));
		}
		else
		{
			String lsPaddedDesc =
				UtilityMethods.addPaddingRight(lsPermitNo, 14, " ");
			String lsPaddedPermit = PERMIT_NO_TEXT + lsPaddedDesc;

			lvReturn.addElement(lsPaddedPermit);
		}

		// *************  SALES TAX DATE  *************
		if (caVehMiscData != null
			&& caVehMiscData.getSalesTaxDate() != 0)
		{
			RTSDate laSalesTaxDate =
				new RTSDate(
					RTSDate.YYYYMMDD,
					caVehMiscData.getSalesTaxDate());
			lvReturn.addElement(
				SALESTAXDATETEXT + laSalesTaxDate.toString() + "    ");
		}
		else
		{
			lvReturn.addElement(
				SALESTAXDATETEXT + caRpt.blankSpaces(14));
		}

		// *************  SALES PRICE  *************
		String lsSalesPriceText = buildSalesPriceText();
		Dollar laSalesPrice = caTitleData.getVehSalesPrice();
		if (laSalesPrice == null)
		{
			laSalesPrice = new Dollar("0.00");
		}
		String lsSalesPricePadded =
			formatDollarAmt(
				laSalesPrice.printDollar().substring(1),
				13);
		lvReturn.addElement(lsSalesPriceText + lsSalesPricePadded);

		// *************  TRADE IN ALLOWANCE  *************
		String lsVehTradeinAllownce = null;
		if (caTitleData != null
			&& caTitleData.getVehTradeinAllownce() != null)
		{
			lsVehTradeinAllownce =
				caTitleData
					.getVehTradeinAllownce()
					.printDollar()
					.substring(
					1);
		}
		if (lsVehTradeinAllownce == null
			|| lsVehTradeinAllownce.equals(""))
		{
			lsBlankSpcs = formatDollarAmt(lsVehTradeinAllownce, 13);
			lvReturn.addElement(TRADEINALLOWANCETEXT + lsBlankSpcs);
		}
		else
		{
			String lsVehTradeinAllowncePadded =
				formatDollarAmt(lsVehTradeinAllownce, 13);
			lvReturn.addElement(
				TRADEINALLOWANCETEXT + lsVehTradeinAllowncePadded);
		}

		// *************  TAXABLE AMOUNT  *************
		String lsVehTaxAmt = null;
		if (caVehMiscData != null
			&& caVehMiscData.getVehTaxAmt() != null)
		{
			lsVehTaxAmt =
				caVehMiscData.getVehTaxAmt().printDollar().substring(1);
		}
		if (lsVehTaxAmt == null || lsVehTaxAmt.equals(""))
		{
			lsBlankSpcs = formatDollarAmt(lsVehTaxAmt, 13);
			lvReturn.addElement(TAXABLEAMOUNTTEXT + lsBlankSpcs);
		}
		else
		{
			String lsVehTaxAmtPadded = formatDollarAmt(lsVehTaxAmt, 13);
			lvReturn.addElement(TAXABLEAMOUNTTEXT + lsVehTaxAmtPadded);
		}

		// *************  SALES TAX PAID  *************
		Dollar laVehSalesTaxAmt = caCTData.getVehSalesTaxAmt();
		if (laVehSalesTaxAmt == null)
		{
			laVehSalesTaxAmt = new Dollar("0.00");
		}
		String lsVehSalesTaxPaid =
			laVehSalesTaxAmt.printDollar().substring(1);
		String lsVehSalesTaxPaidPadded =
			formatDollarAmt(lsVehSalesTaxPaid, 13);

		// Add string to vector
		lvReturn.addElement(SALESTAXPAIDTEXT + lsVehSalesTaxPaidPadded);

		// *************  OTHER STATE TAX PAID  *************
		Dollar laTaxPdOthrState = new Dollar(0.00);
		if (caVehMiscData != null
			&& caVehMiscData.getTaxPdOthrState() != null)
		{
			laTaxPdOthrState = caVehMiscData.getTaxPdOthrState();
		}
		String lsTaxPdOthrState =
			laTaxPdOthrState.printDollar().substring(1);
		String lsTaxPdOthrStatePadded =
			formatDollarAmt(lsTaxPdOthrState, 13);

		// Add string to vector
		lvReturn.addElement(OTHERSTATETAXTEXT + lsTaxPdOthrStatePadded);

		// *************  TAX PENALTY  *************
		Dollar laTaxPenalty = getItmPrice(SLSTXPEN);
		String lsTaxPenalty = laTaxPenalty.printDollar().substring(1);
		String lsTaxPenaltyPadded = formatDollarAmt(lsTaxPenalty, 13);

		// Add string to vector
		lvReturn.addElement(TAXPENALTYTEXT + lsTaxPenaltyPadded);

		// *************  TOTAL TAX PAID  *************
		Dollar laTotalSalesTaxPaid = new Dollar(0.00);
		if (caCTData.getVehTotalSalesTaxPd() != null)
		{
			laTotalSalesTaxPaid = caCTData.getVehTotalSalesTaxPd();
		}
		String lsTotalSalesTaxPaid =
			laTotalSalesTaxPaid.printDollar().substring(1);
		String lsTotalSalesTaxPaidPadded =
			formatDollarAmt(lsTotalSalesTaxPaid, 13);

		// Add string to vector
		lvReturn.addElement(
			TOTALTAXPAIDTEXT + lsTotalSalesTaxPaidPadded);

		return lvReturn;
	}

	/**
	 * Build vehicle notation for printing.
	 * 
	 * @return Vector
	 */
	private Vector buildVehNotation()
	{
		int liMaxVehNotLn = 9;

		Vector lvHoldData = new Vector();
		Vector lvReturn = new Vector();

		lvHoldData = buildNotationsVector(caCTData);
		lvReturn.addElement(VEH_NOTATION_TEXT);

		int liVecSize = 0;
		if (lvHoldData != null)
		{
			liVecSize = lvHoldData.size();
		}

		for (int i = 0; i < liMaxVehNotLn; i++)
		{
			if (i < liVecSize)
			{
				lvReturn.addElement(lvHoldData.elementAt(i));
			}
			else
			{
				lvReturn.addElement(caRpt.blankSpaces(34));
			}
		}

		lvReturn.addElement(caRpt.blankSpaces(34));

		return lvReturn;
	}

	/**
	 * Insert blank spaces to set record for printing.
	 * 
	 * @param avTitle1 Vector
	 * @param avTitle2 Vector
	 * @return Vector
	 */
	private Vector concatanateBodyData(
		Vector avTitle1,
		Vector avTitle2)
	{
		int liRecCnt = 0;

		String lsBodyData = "";
		String lsTitle1Size = "";
		String lsTitle2Size = "";

		Vector lvBodyRcpt = new Vector();

		for (int i = 0; i < COL_40; i++)
		{
			lsTitle1Size = (String) avTitle1.elementAt(i);
			lsTitle2Size = (String) avTitle2.elementAt(i);

			liRecCnt =
				89 - (lsTitle1Size.length() + lsTitle2Size.length());
			lsBodyData =
				lsTitle1Size
					+ caRpt.blankSpaces(liRecCnt)
					+ lsTitle2Size;
			lvBodyRcpt.addElement(lsBodyData);
		}

		return lvBodyRcpt;
	}

	/**
	 * Pad fees to eleven characters. Add the dollar sign.
	 * 
	 * @param asDollarAmt String
	 * @param liPad int
	 * @return String
	 */
	public String formatDollarAmt(String asDollarAmt, int liPad)
	{
		if (asDollarAmt == null || asDollarAmt.equals(""))
		{
			asDollarAmt = "0.00";
		}
		String lsDollarStr = asDollarAmt;

		// Get string and pad length
		String lsPadLngth = caRpt.blankSpaces(liPad);
		int liAmtLngth = asDollarAmt.length();
		int liThirteenDigits = lsPadLngth.length();

		// Pad string
		for (int i = liAmtLngth; i < liThirteenDigits; i++)
		{
			lsDollarStr = caRpt.blankSpaces(1) + lsDollarStr;
		}

		// Add dollar sign
		lsDollarStr =
			caRpt.blankSpaces(1) + DOLLARSIGNTEXT + lsDollarStr;

		return lsDollarStr;
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param avResults Vector
	 * @param abBarcode boolean
	 */
	public void formatReceipt(Vector avResults, boolean abBarcode)
	{
		int liTransDataObj = 0;
		// defect 7896
		// Not used  
		//int liPymntDataVct = 1;
		//int liDlrTransObj = 2;
		// end defect 7896

		cbBarCode = abBarcode;
		Vector lvRcptInfo = new Vector();
		Vector lvTitleData1 = new Vector();
		Vector lvTitleData2 = new Vector();
		Vector lvBodyRcpt = new Vector();
		// Parse TransData object
		caCTData =
			(CompleteTransactionData) avResults.elementAt(
				liTransDataObj);
		caMFVehData = (MFVehicleData) caCTData.getVehicleInfo();
		caOwnrData = (OwnerData) caMFVehData.getOwnerData();
		caRegisData = (RegistrationData) caMFVehData.getRegData();
		caTitleData = (TitleData) caMFVehData.getTitleData();
		caVehicleData = (VehicleData) caMFVehData.getVehicleData();
		caRegFeeData = (RegFeesData) caCTData.getRegFeesData();
		cvFeesData = (Vector) caRegFeeData.getVectFees();
		caVehMiscData = (VehMiscData) caCTData.getVehMisc();
		// Parse Payment data
		cvPymntData = (Vector) buildPaymentVector(avResults);

		// defect 10590
		// defect 10623
		// Token Trailers no longer print sticker, print from tray 2.  
		if (!caCTData.isPreviewReceipt()
			&& caCTData.getStickers() != null
			&& caCTData.getStickers().size() > 0
			&& caRegisData.getRegClassCd() != 
				RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
		{
			// end defect 10623
			cbShouldStickersPrint = true;
		}
		// end defect 10590 

		if (UtilityMethods.isDTA(caCTData.getTransCode()))
		{
			// defect 10290 
			// Only work with one DlrTtlData object at a time
			// defect 10652
			// Do not include special characters if Preview Receipt  
			cbShouldStickersPrint =
				!caCTData.isPreviewReceipt()
					&& caCTData.getDlrTtlData().isToBePrinted();
			// end defect 10652 

			//			for (int i = caTransData.getDlrTtlDataObjs().size() - 1;
			//				i > -1;
			//				i--)
			//			{
			//				DealerTitleData laDTD =
			//					(DealerTitleData) caTransData
			//						.getDlrTtlDataObjs()
			//						.get(
			//						i);
			//				if (laDTD.isProcessed())
			//				{
			//					if (!laDTD.isToBePrinted())
			//					{
			//						cbShouldStickersPrint = false;
			//					}
			//					else if (laDTD.isToBePrinted())
			//					{
			//						cbShouldStickersPrint = true;
			//					}
			//					break;
			//				}
			//			}
			// end defect 10290 
		}
		// End PCR 34 
		try
		{
			generateReceiptHeader(avResults, abBarcode);
		}
		catch (Exception aeEx)
		{
			// empty code block
		}
		// ***************  VEHICLE INFORMATION  ***************
		lvRcptInfo = parseRegisInfoAcct();
		printVehDataTitle(lvRcptInfo);
		// ***************  BUILD BODY OF RECEIPT  ***************
		lvTitleData1 = parseTitleData1();
		//lvTitleData2 = parseTitleData2(lvTitleData1, avResults);
		lvTitleData2 = parseTitleData2(avResults);
		lvBodyRcpt = concatanateBodyData(lvTitleData1, lvTitleData2);
		printBody(lvBodyRcpt);
		//printTitleMessages();
		printFooterMessages();
		// PCR 34
		if (cbShouldStickersPrint)
		{
			// defect 10623
			// Do not print sticker for Token Trailer
			if (caCTData.getVehicleInfo().getRegData().getRegClassCd()
				!= RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
			{
				generateStickers(caCTData);
			}
		}
		// End PCR 34 
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
	 * Get the doc type description from the Ownership Evidence Code
	 *  vector argument.
	 * 
	 * @param avOwnrshpEdv Vector Ownership Evidence data
	 * @param aiOwnrshpEdvCd int Ownership Evidence Code
	 * @return String
	 */
	private String getCacheOwnrshipEvidenceDesc(
		Vector avOwnrshpEdv,
		int aiOwnrshpEdvCd)
	{
		String lsOwnrshpEdvDesc = "";

		OwnershipEvidenceCodesData laOwnrshpEdv =
			new OwnershipEvidenceCodesData();

		for (int i = 0; i < avOwnrshpEdv.size(); i++)
		{
			laOwnrshpEdv =
				(OwnershipEvidenceCodesData) avOwnrshpEdv.elementAt(i);
			if (aiOwnrshpEdvCd == laOwnrshpEdv.getOwnrshpEvidCd())
			{
				lsOwnrshpEdvDesc = laOwnrshpEdv.getOwnrshpEvidCdDesc();
				break;
			}
		}
		if (lsOwnrshpEdvDesc != null)
		{
			return lsOwnrshpEdvDesc;
		}
		else
		{
			return "";
		}
	}
	/**
	 * Get the Ownership Evidence data vector from the cache tables and
	 *  return a vector of objects.
	 *
	 * @return Vector
	 */
	protected Vector getCacheOwnrshipEvidenceVector()
	{
		int liOwnrshpEdvSort = 0;

		Vector lvOnwrshpEdv = new Vector();
		try
		{
			// defect 7896
			// changed method call to be statically referenced
			//OwnershipEvidenceCodesCache laCache =
			//	new OwnershipEvidenceCodesCache();
			lvOnwrshpEdv =
				OwnershipEvidenceCodesCache.getOwnrEvidCds(
					liOwnrshpEdvSort);
			// end defect 7896

			return lvOnwrshpEdv;
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}
		return null;
	}
//	/**
//	 * Get the Sales Tax Category Description
//	 *
//	 * @param  aiSalesTaxCatExmptCd int
//	 * @return String
//	 */
//	protected String getCacheSalesTaxCatDesc(int aiSalesTaxCatExmptCd)
//	{
//		String lsSalesTaxExmptDesc = "";
//
//		Vector lvSalesTaxExmptCds = new Vector();
//
//		TaxExemptCodeData laTaxExemptCodeData = new TaxExemptCodeData();
//
//		// get vector of sales tax exempt codes
//
//		// defect 7896
//		// changed method call to be statically referenced
//		//TaxExemptCodeCache laCache = new TaxExemptCodeCache();
//		lvSalesTaxExmptCds =
//			(Vector) TaxExemptCodeCache.getTaxExmptCds();
//		// end defect 7896
//
//		// find/return tax exempt code description
//		for (int i = 0; i < lvSalesTaxExmptCds.size(); i++)
//		{
//			laTaxExemptCodeData =
//				(TaxExemptCodeData) lvSalesTaxExmptCds.elementAt(i);
//			if (laTaxExemptCodeData.getSalesTaxExmptCd()
//				== aiSalesTaxCatExmptCd)
//			{
//				lsSalesTaxExmptDesc =
//					laTaxExemptCodeData.getSalesTaxExmptDesc();
//			}
//		}
//
//		return lsSalesTaxExmptDesc;
//	}
	/**
	 * Get the Sales Tax Paid
	 *
	 * @param asAcctItmCd String
	 * @return Dollar
	 */
	protected Dollar getItmPrice(String asAcctItmCd)
	{
		String lsAccItmCd = "";
		Dollar laItmPrice = new Dollar("0.00");

		if (cvFeesData.size() > 0)
		{
			for (int liFeesIndex = 0;
				liFeesIndex < cvFeesData.size();
				liFeesIndex++)
			{
				caFeesData =
					(FeesData) cvFeesData.elementAt(liFeesIndex);
				lsAccItmCd = caFeesData.getAcctItmCd();
				if (lsAccItmCd.equals(asAcctItmCd))
				{
					laItmPrice = caFeesData.getItemPrice();
					// defect 10112 
					break;
					// end defect 10112 
				}
			}
		}
		else
		{
			return laItmPrice;
		}

		return laItmPrice;
	}

	/**
	 * Get permit number if applicable
	 * 
	 * @return String
	 */
	private String getPermitNo()
	{
		// defect 11242 
		//String lsTaxExemptCdDesc = "";
		// end defect 11242 
		long llIMCNo = 0;

		if (caVehMiscData != null
			&& caVehMiscData.getSalesTaxCat() != null)
		{
			if (caVehMiscData.getSalesTaxCat().equals(EXEMPTEXT))
			{
				// defect 11242 
				TaxExemptCodeData laTaxExmptCdData = 
					TaxExemptCodeCache.getTaxExmptCd(caVehMiscData.getSalesTaxExmptCd());
				//				lsTaxExemptCdDesc =
				//					getCacheSalesTaxCatDesc(
				//						caVehMiscData.getSalesTaxExmptCd());
				//				if (lsTaxExemptCdDesc.equals(RENTALTEXT)
				//					|| lsTaxExemptCdDesc.equals(SELLERFINANCEDSALESTEXT))
				if (laTaxExmptCdData!= null && laTaxExmptCdData.getIMCNoReqd() ==1)
				{
					// end defect 11242 
					llIMCNo = caVehMiscData.getIMCNo();
				}
			}
		}
		return String.valueOf(llIMCNo);
	}

	/**
	 * Return the lien holder 1 data
	 * 
	 * @return Vector
	 */
	private Vector parseLienHld1()
	{
		int liLienMaxSize = 6;
		Vector lvLienHld1 = new Vector();

		// defect 10112 
		LienholderData laLienData =
			(LienholderData) caTitleData.getLienholderData(
				TitleConstant.LIENHLDR1);

		if (laLienData.getLienDate() != 0) // Lienholder date
		{
			String lsLienDate =
				FIRSTLIENTXT
					+ caRpt.blankSpaces(19 - FIRSTLIENTXT.length())
					+ DATE_TEXT
					+ (new RTSDate(RTSDate.YYYYMMDD,
						laLienData.getLienDate())
						.toString());
			lvLienHld1.addElement(lsLienDate);
		}
		else
		{
			String lsLienText = FIRSTLIENTXT;
			lvLienHld1.addElement(lsLienText);
		}

		// defect 9969 
		lvLienHld1.addAll(laLienData.getNameAddressVector());
		// end defect 9969 
		// end defect 10112 

		for (int i = lvLienHld1.size(); i < liLienMaxSize; i++)
		{
			lvLienHld1.addElement(caRpt.blankSpaces(34));
		}

		return lvLienHld1;
	}

	/**
	 * return the lien holder 2 data
	 * 
	 * @return Vector
	 */
	private Vector parseLienHld2()
	{
		int liLienMaxSize = 3;

		Vector lvLienHld2 = new Vector();

		// defect 10112
		LienholderData laLienData =
			(LienholderData) caTitleData.getLienholderData(
				TitleConstant.LIENHLDR2);
		// end defect 10112 

		if (laLienData.getLienDate() != 0) // Lienholder date
		{
			String lsLienDate =
				SECONDLIENTXT
					+ caRpt.blankSpaces(19 - SECONDLIENTXT.length())
					+ DATE_TEXT
					+ (new RTSDate(RTSDate.YYYYMMDD,
						laLienData.getLienDate())
						.toString());
			lvLienHld2.addElement(lsLienDate);
		}
		else
		{
			String lsLienText = SECONDLIENTXT;
			lvLienHld2.addElement(lsLienText);
		}

		// defect 9969 
		lvLienHld2.addAll(laLienData.getLienHldrSubsetDataVector());
		// end defect 9969 

		for (int i = lvLienHld2.size(); i < liLienMaxSize; i++)
		{
			lvLienHld2.addElement(caRpt.blankSpaces(34));
		}

		return lvLienHld2;
	}

	/**
	 * return the lien holder 3 data
	 * 
	 * @return Vector
	 */
	private Vector parseLienHld3()
	{
		int liLienMaxSize = 3;
		Vector lvLienHld3 = new Vector();

		// defect 10112 
		LienholderData laLienData =
			(LienholderData) caTitleData.getLienholderData(
				TitleConstant.LIENHLDR3);
		// end defect 10112 

		if (laLienData.getLienDate() != 0) // Lienholder date
		{
			String lsLienDate =
				THIRDLIENTXT
					+ caRpt.blankSpaces(19 - THIRDLIENTXT.length())
					+ DATE_TEXT
					+ (new RTSDate(RTSDate.YYYYMMDD,
						laLienData.getLienDate())
						.toString());
			lvLienHld3.addElement(lsLienDate);
		}
		else
		{
			String lsLienText = THIRDLIENTXT;
			lvLienHld3.addElement(lsLienText);
		}

		// defect 9969 
		lvLienHld3.addAll(laLienData.getLienHldrSubsetDataVector());
		// end defect 9969 

		for (int i = lvLienHld3.size(); i < liLienMaxSize; i++)
		{
			lvLienHld3.addElement(caRpt.blankSpaces(34));
		}

		return lvLienHld3;
	}

	/**
	 * Parse Registration Information Account
	 * 
	 * @return Vector
	 */
	private Vector parseRegisInfoAcct()
	{

		Vector lvVehData = new Vector();

		int liVehModlYr = caVehicleData.getVehModlYr();
		String lsVehMk = caVehicleData.getVehMk();
		String lsYrMk = String.valueOf(liVehModlYr) + "/" + lsVehMk;

		String lsPrevCityState =
			caTitleData.getPrevOwnrCity()
				+ ", "
				+ caTitleData.getPrevOwnrState();

		// Add data to the vector
		lvVehData.addElement(caVehicleData.getVin());
		lvVehData.addElement(caVehicleData.getVehClassCd());
		lvVehData.addElement(lsYrMk);
		lvVehData.addElement(caVehicleData.getVehModl());
		lvVehData.addElement(caVehicleData.getVehBdyType());
		lvVehData.addElement(caTitleData.getVehUnitNo());
		lvVehData.addElement(
			new Integer(caVehicleData.getVehEmptyWt()));
		lvVehData.addElement(
			new Integer(caRegisData.getVehCaryngCap()));
		lvVehData.addElement(new Integer(caRegisData.getVehGrossWt()));
		lvVehData.addElement(caVehicleData.getVehTon());
		lvVehData.addElement(caVehicleData.getTrlrType());
		lvVehData.addElement(caVehicleData.getVehBdyVin());
		if (caVehicleData.getVehWidth() > 0)
		{

			// defect 4905
			// Changing format of width when printed on receipt.

			int liVehicleWidth = (int) caVehicleData.getVehWidth();
			int liVehicleWidthFeet = liVehicleWidth / 12;
			int liVehicleWidthInches = liVehicleWidth % 12;

			lvVehData.addElement(
				caVehicleData.getVehLngth()
					+ "/"
					+ liVehicleWidthFeet
					+ "\'"
					+ liVehicleWidthInches
					+ "\"");
		}
		else
			lvVehData.addElement(
				new Integer(caVehicleData.getVehLngth()));
		lvVehData.addElement(caTitleData.getPrevOwnrName());
		lvVehData.addElement(lsPrevCityState);

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
	 * Build the vector containing the data for the left size of the
	 * receipt.
	 * 
	 * @return Vector
	 */
	private Vector parseTitleData1()
	{
		int liPrtLines = COL_40; // Maximun lines for Title body

		Vector lvHoldData = new Vector();
		Vector lvTitleData1 = new Vector();

		// *************  FILLER, LINE 18  *************
		lvTitleData1.addElement(caRpt.blankSpaces(34));

		// *************  INVENTORY DESCRIPTION, LINE 19  *************
		lvHoldData = buildInvIssued();
		for (int i = 0; i < lvHoldData.size(); i++)
		{
			lvTitleData1.addElement(lvHoldData.elementAt(i));
		}
		lvHoldData = new Vector();

		// *************  VEHICLE NOTATION, LINE 23  *************
		lvHoldData = buildVehNotation();
		for (int i = 0; i < lvHoldData.size(); i++)
		{
			lvTitleData1.addElement(lvHoldData.elementAt(i));
		}
		lvHoldData = new Vector();

		// *************  ODOMETER REAGING/BRAND, LINE 34  *************
		String lsOdomtr = buildOdomtr();
		lvTitleData1.addElement(lsOdomtr);

		// *************  OWNERSHIP EVIDENCE, LINE 35  *************
		lvHoldData = getCacheOwnrshipEvidenceVector();
		String lsOwnrshipEvidenceDesc =
			getCacheOwnrshipEvidenceDesc(
				lvHoldData,
				caTitleData.getOwnrShpEvidCd());
		String lsOwnrShipEvdnce =
			OWNRSHIP_EVID_TEXT + " " + lsOwnrshipEvidenceDesc;
		lvTitleData1.addElement(lsOwnrShipEvdnce);
		lvHoldData = new Vector();

		// *************  LIENHOLDERS, LINE 36  *************

		// defect 9969 
		lvTitleData1.addAll(parseLienHld1());
		lvTitleData1.addAll(parseLienHld2());
		lvTitleData1.addAll(parseLienHld3());
		// end defect 9969 

		// *************  COMPLETE PRINT LINES  *************
		if (lvTitleData1.size() < liPrtLines)
		{
			for (int i = lvTitleData1.size(); i < liPrtLines; i++)
			{
				lvTitleData1.addElement(caRpt.blankSpaces(34));
			}
		}
		return lvTitleData1;
	}

	/**
	 * Build the vector containing the data for the fees size of the
	 *  receipt.
	 * 
	 * @param avResults Vector
	 * @return Vector
	 */
	private Vector parseTitleData2(Vector avResults)
	{
		int liPrtLines = COL_40; // Maximum lines for Title body

		String lsBlankSpcs = "";

		Vector lvHoldData = new Vector();
		Vector lvTitleData2 = new Vector();

		// *************  DIESEL/REGPENALTY FEES  *************
		lvHoldData = buildDieselRegPentlyFee();
		for (int i = 0; i < lvHoldData.size(); i++)
		{
			lvTitleData2.addElement(lvHoldData.elementAt(i));
		}
		lvHoldData = new Vector();

		// *************  FEES  *************
		lvHoldData = buildFeesAssessed();
		for (int i = 0; i < lvHoldData.size(); i++)
		{
			lvTitleData2.addElement(lvHoldData.elementAt(i));
		}
		lvHoldData = new Vector(avResults);

		// *************  PAYMENT DATA  *************
		lvHoldData = buildPymnt(avResults);
		for (int i = 0; i < lvHoldData.size(); i++)
		{
			lvTitleData2.addElement(lvHoldData.elementAt(i));
		}
		lvHoldData = new Vector();

		// *************  SALES TAX INFORMATION  *************
		lvHoldData = buildSalesTaxInfo();
		for (int i = 0; i < lvHoldData.size(); i++)
		{
			lvTitleData2.addElement(lvHoldData.elementAt(i));
		}
		lvHoldData = new Vector();

		// *************  BATCH NUMBER  *************
		lvTitleData2.addElement(buildBatchNo());

		// *************  COMPLETE PRINT LINES  *************
		if (lvTitleData2.size() < liPrtLines)
		{
			for (int i = lvTitleData2.size(); i < liPrtLines; i++)
			{
				lsBlankSpcs = caRpt.blankSpaces(COL_91 - COL_50);
				lvTitleData2.addElement(lsBlankSpcs);
			}
		}

		return lvTitleData2;
	}
	/**
	* Print body.
	* 
	* @param avBodyData Vector
	*/
	private void printBody(Vector avBodyData)
	{

		//int liRecCnt = 0;
		int liStartPrt = COL_05;

		String lsBodyData = "";

		for (int i = 0; i < avBodyData.size(); i++)
		{
			lsBodyData = (String) avBodyData.elementAt(i);
			caRpt.print(lsBodyData, liStartPrt, charLength(lsBodyData));
			caRpt.nextLine();
		}

	}
	/**
	 * Print footer messages for title, CorTTL, RejCor, DTAs.
	 */
	protected void printFooterMessages()
	{
		Dollar laZero = new Dollar("0.00");

		// defect 10112 
		LienholderData laLienData1 =
			(LienholderData) caTitleData.getLienholderData(
				TitleConstant.LIENHLDR1);
		//end defect 10112 

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
					caCTData.getTransCode());
			// defect 8193
			// Move up to accomodate larger Owner Address font
			// Set print pointer to the correct position
			// for (int i = caRpt.getCurrX(); i < 69; i++)
			for (int i = caRpt.getCurrX(); i < 67; i++)
			{
				caRpt.nextLine();
			}
			// end defect 8193 
			// Print title will be mailed to the lienholder

			// defect 10112 
			if (!UtilityMethods.isEmpty(laLienData1.getName1()))
			{
				// end defect 10112

				// defect 9978 
				if (DocumentTypesCache
					.isETtlAllowd(caTitleData.getDocTypeCd()))
				{
					//if (caTitleData.getDocTypeCd() == 1
					//	|| caTitleData.getDocTypeCd() == 2)
					String lsRcptMsg =
						(caTitleData.isETitle()
							&& caTitleData.hasLien())
							? ELTMSG
							: TTLMAILEDTOLIENHLD;

					caRpt.print(lsRcptMsg, COL_40, lsRcptMsg.length());
					caRpt.nextLine();
				}
				// end defect 9978 
			}

			// defect 6199, 6748
			//	Print the remaining messages if receipt is not bar code
			if (!cbBarCode)
			{
				// end defect 6199, 6748

				// Print commercial vehicle  message
				if (laTransCodeData.getRcptMsgCd() == 1)
				{
					this.caRpt.print(
						COMMERCIALVEHICLESTRING,
						COL_40,
						COMMERCIALVEHICLESTRING.length());
					caRpt.nextLine();
				}
				if (laTransCodeData.getRcptMsgCd() == 2)
				{
					this.caRpt.print(
						COMMERCIALVEHICLESTRING,
						COL_40,
						COMMERCIALVEHICLES2.length());
					caRpt.nextLine();
				}
				// Print Credit Remaining
				if (caCTData.getCrdtRemaining() != null
					&& caCTData.getCrdtRemaining().compareTo(laZero) > 0)
				{
					caRpt.print(
						GenRegistrationReceipts.REGCREDITSTRING,
						COL_40,
						charLength(
							GenRegistrationReceipts.REGCREDITSTRING));
					// Move down 1 line
					caRpt.nextLine();
				}
				// defect 8193
				// Move up to accomodate larger Owner Address font
				// for (int i = caRpt.getCurrX(); i < 72; i++) 
				for (int i = caRpt.getCurrX(); i < 70; i++)
				{
					caRpt.nextLine();
				}
				//end defect 8193 

				// defect 10715
				// Print Automation Message
//				if (cbAutomateFees)
//				{
//					this.caRpt.print(
//						GenRegistrationReceipts.AUTOMATION_1_STRING,
//						COL_40,
//						GenRegistrationReceipts
//							.AUTOMATION_1_STRING
//							.length());
//					this.caRpt.nextLine();
//					this.caRpt.print(
//						GenRegistrationReceipts.AUTOMATION_2_STRING,
//						COL_40,
//						GenRegistrationReceipts
//							.AUTOMATION_2_STRING
//							.length());
//					this.caRpt.nextLine();
//				}
				// defect 10715
				if (caTitleData.getOwnrShpEvidCd() != 21)
				{
					String lsProof = PROOFTTLAPPLRCPT;
					if (caTitleData.getDocTypeCd() == 2
						|| caTitleData.getDocTypeCd() == 11)
					{
						lsProof = lsProof + ".";
					}
					else
					{
						lsProof = lsProof + ANDREGISTRATION;
					}
					caRpt.print(lsProof, COL_05, lsProof.length());
					// Move down 1 line
					caRpt.nextLine();
				}
			} // end if, not bar code
		} // end try box

		catch (RTSException aeRTSEx)
		{
			// this was bad.  but do not fail on this!
		}
		// this is where VTR messages would go if we used them!
		// There are three possible lines.
	}

	/**
	 * Set boolean if printing of payment  data is required.
	 *
	 * @param avTransPymntData Vector  
	 * @return boolean
	 */
	private boolean printPymntReqd(Vector avResults)
	{

		boolean lbRtn = false;
		Dollar laPymntAmt = new Dollar(0.00);
		Dollar laZeroDollar = new Dollar(0);

		for (int i = 0; i < avResults.size(); i++)
		{
			// pull the object out so we can inspect it
			Object laThisElement = avResults.elementAt(i);

			// inspect the object to see if it is a
			// TransactionPaymentData object
			if (laThisElement instanceof TransactionPaymentData)
			{
				TransactionPaymentData laPaymentData =
					(TransactionPaymentData) avResults.elementAt(i);
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
	//	/**
	//	 * printTitleMessages
	//	 * 
	//	 */
	//	protected void printTitleMessages()
	//	{
	//		Dollar laZero = new Dollar("0.00");
	//
	//		LienholdersData laLHD1 =
	//			(LienholdersData) caTitleData.getLienHolder1();
	//
	//		try
	//		{
	//			// print the Commercial Vehicle message.
	//			// Look up the Trans Code to get RCPTMSGCD to see if we need to print
	//			// the COMMERCIALVEHICLESTRING.
	//			// this look up can create an RTSException.  We are just eating it!
	//			//TransactionCodesCache lTransCodesCache = new TransactionCodesCache();
	//			TransactionCodesData laTransCodeData =
	//				(
	//					TransactionCodesData) TransactionCodesCache
	//						.getTransCd(
	//					caTransData.getTransCode());
	//
	//			// Set print pointer to the correct position
	//			for (int i = caRpt.getCurrX(); i < COL_58; i++)
	//			{
	//				caRpt.nextLine();
	//			}
	//
	//			// Print title will be mailed to the lienholder
	//			if (laLHD1.getLienHldrName1() != null
	//				&& !laLHD1.getLienHldrName1().equals("")) // first Name
	//			{
	//				if (caTitleData.getDocTypeCd() == 1
	//					|| caTitleData.getDocTypeCd() == 2)
	//				{
	//					caRpt.print(
	//						TTLMAILEDTOLIENHLD,
	//						COL_40,
	//						charLength(TTLMAILEDTOLIENHLD));
	//				}
	//			}
	//
	//			// Move down 1 line
	//			caRpt.nextLine();
	//
	//			// Print Credit Remaining
	//			if (caTransData.getCrdtRemaining() != null
	//				&& caTransData.getCrdtRemaining().compareTo(laZero) > 0)
	//			{
	//				caRpt.print(
	//					GenRegistrationReceipts.REGCREDITSTRING,
	//					COL_40,
	//					charLength(
	//						GenRegistrationReceipts.REGCREDITSTRING));
	//			}
	//
	//			// Move down 1 line
	//			caRpt.nextLine();
	//
	//			// Print commercial vehicle  message
	//			if (laTransCodeData.getRcptMsgCd() == 1)
	//			{
	//				this.caRpt.print(
	//					COMMERCIALVEHICLESTRING,
	//					COL_40,
	//					COMMERCIALVEHICLESTRING.length());
	//			}
	//
	//			// Print Automation Message
	//			if (cbAutomateFees)
	//			{
	//				this.caRpt.print(
	//					GenRegistrationReceipts.AUTOMATION_1_STRING,
	//					FIRSTCOLUMNPOSITION,
	//					GenRegistrationReceipts
	//						.AUTOMATION_1_STRING
	//						.length());
	//				this.caRpt.nextLine();
	//
	//				this.caRpt.print(
	//					GenRegistrationReceipts.AUTOMATION_2_STRING,
	//					FIRSTCOLUMNPOSITION,
	//					GenRegistrationReceipts
	//						.AUTOMATION_2_STRING
	//						.length());
	//				this.caRpt.nextLine();
	//			}
	//			// Move down 1 line
	//			caRpt.nextLine();
	//
	//			if (caTitleData.getOwnrShpEvidCd() != 21)
	//			{
	//				String lsProof = PROOFTTLAPPLRCPT;
	//				if (caTitleData.getDocTypeCd() == 2
	//					|| caTitleData.getDocTypeCd() == 11)
	//				{
	//					lsProof = lsProof + ".";
	//				}
	//				else
	//				{
	//					lsProof = lsProof + ANDREGISTRATION;
	//				}
	//				caRpt.print(lsProof, COL_05, lsProof.length());
	//				// Move down 1 line
	//				caRpt.nextLine();
	//			}
	//		} // end try box
	//
	//		catch (RTSException aeRTSEx)
	//		{
	//			// this was bad.  but do not fail on this!
	//		}
	//		// this is where VTR messages would go if we used them!
	//		// There are three possible lines.
	//	}

	/**
	 * Print Vehicle Data Title
	 * 
	 * @param avVehdata Vector
	 */
	public void printVehDataTitle(Vector avVehdata)
	{
		int liVehInfoRow = 22;

		// Set printer pointer
		for (int i = getCurrX(); i < liVehInfoRow; i++)
		{
			this.caRpt.blankLines(1);
		}

		for (int i = 0; i < avVehdata.size(); i++)
		{
			switch (i)
			{
				case 0 : // print VehVin
					{
						printVIN(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 1 : // Print VehClassCd
					{
						printVehClassifcation(
							avVehdata.elementAt(i).toString(),
							COL_56);
						// Move down 1 line
						caRpt.nextLine();
						break;
					}
				case 2 : // Print Year/Make
					{
						printVehYrMk(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 3 : // Print Model
					{
						printVehModl(
							avVehdata.elementAt(i).toString(),
							COL_26 - 1);
						break;
					}
				case 4 : // Print Body Style
					{
						printVehBdyStyle(
							avVehdata.elementAt(i).toString(),
							COL_38 - 1);
						break;
					}
				case 5 : // Print Unit number
					{
						printVehUnitNo(
							avVehdata.elementAt(i).toString(),
							COL_56 - 1);
						// Move down 1 line
						caRpt.nextLine();
						break;
					}
				case 6 : // Print Empty weight
					{
						printVehEmptyWt(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 7 : // Print Carrying capacity
					{
						printCarryCap(
							avVehdata.elementAt(i).toString(),
							COL_24 - 1);
						break;
					}
				case 8 : // Print Gross Weight
					{
						printVehGrossWt(
							avVehdata.elementAt(i).toString(),
							COL_51 - 1);
						break;
					}
				case 9 : // Print Tonnage
					{
						String lsVehTon =
							avVehdata.elementAt(i).toString();
						if (lsVehTon == null || lsVehTon.equals(""))
						{
							lsVehTon = "0.00";
						}
						printVehTon(lsVehTon, COL_68 - 1);
						break;
					}
				case 10 : // Print Trailer Type
					{
						printTrlrType(
							avVehdata.elementAt(i).toString(),
							COL_83 - 1);
						// Move down 1 line
						caRpt.nextLine();
						break;
					}
				case 11 : // Print Body VIN
					{
						printVehBdyVIN(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 12 : // Print Trailer Length
					{
						printTrvlTrlrLngth(
							avVehdata.elementAt(i).toString(),
							COL_62 - 1);
						// Move down 1 line
						caRpt.nextLine();
						break;
					}
				case 13 : // Print prev owner name
					{
						printPrevOwnr(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 14 : // Print prev owner City, State
					{
						printPrevCityState(
							avVehdata.elementAt(i).toString(),
							COL_49 - 1);
						caRpt.blankLines(1);
						break;
					}
				default :
					{
						System.out.println(
							"oops, drop out of the switch prematurely");
					}
			} // switch
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

		// Complete Transaction Data
		CompleteTransactionData laCTData =
			new CompleteTransactionData();
		laCTData = (CompleteTransactionData) Vehicle.getVeh();
		lvReceipt.addElement(laCTData);

		// PaymentData
		lvReceipt.addElement(cvPymntData);

		// DealerTransData
		lvReceipt.addElement(caDlrTtlData);

		return lvReceipt;
	}
}
