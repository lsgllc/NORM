package com.txdot.isd.rts.services.reports.registration;

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
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * GenRegistrationReceipts.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl   11/11/2001  New Class
 * Ray Rowehl	02/07/2002	Resolve some formating and data
 *							issues documented in defect.
 *							defect 1336
 * S Govindappa 04/30/2002  Fixed CQU100003482 by checking for 0 value 
 * 							in date field in printVehicleData method to 
 * 							prevent null pointer error
 * S Govindappa	07/10/2002	merged PCR25 code
 * MAbs		   	08/27/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * B Brown		09/18/2002	Changed methods printNotationAndPayment and 
 * 							printInventoryAndFees.
 *                          for Internet Registration renewal, the 
 * 							PymntTypeAmt = 0, but changed it to the fees
 * 							total, and  PymntType was CASH, but changed 
 * 							it to INTERNET. This change was so counties 
 * 							and customers would not be confused when 
 * 							getting their receipt when picking up their 
 * 							sticker and receipt from the county. 
 * 							No defect associated with this - per 
 *							Bob Tanner.
 * K Harrell	03/23/2004	5.2.0 Merge.  See PCR 34 comments.
 *							modify formatReceipt(),
 *							printInventoryAndFees(),
 *							printNotationsAndPayment()
 *							Ver 5.2.0 
 * B Hargrove   08/17/2004  Changes to Subcontractor Receipt, which is 
 * 							now printing as of Ver 5.2.1. 
 * 							modify printInventoryAndFees(),
 *							printNotationsAndPayment(),
 *							printVehicleData()
 *                          defect 7450 Ver 5.2.1.   
 * B Hargrove   08/20/2004  Change Subcontractor Receiptto not 
 * 							print Vehicle Class. 
 * 							modify printVehicleData()
 *                          defect 7450 Ver 5.2.1.
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3  
 * K Harrell	10/25/2006	Call ReceiptTemplate.printPrevExpMoYr() for
 * 							standardization. 
 * 							modify printVehicleData()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/17/2006	Default to new Dollar(0.00) if CustActlRegFee
 * 							is null for Duplicate Receipt 
 * 							modify printVehicleData()
 * 							defect 9018 Ver Exempts
 * K Harrell	03/05/2007	Working on 'reporting' mfg inventory
 * 							modify printInventoryAndFees()
 * 							...to be continued.
 * 							defect 9085 Ver Special Plates
 * Jeff S.		04/09/2007	More manufacturing notation work.
 * 							modify printInventoryAndFees()
 * 							defect 9145 Ver Special Plates
 * K Harrell	04/25/2007	Use SpclPltRegisData RegPltNo, RegPltCd vs
 * 							viInvAlloc as may have just generated a Mfg
 * 							request for same plate
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/03/2007	Work on manufactured Annual Special Plates
 * 							modify printInventoryAndFees()
 * 							defect 9085 Ver Special Plates  
 * B Hargrove	04/16/2008	Comment out references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe (see: defect 9557).
 * 							modify queryData()
 * 							defect 9631 Ver Defect POS A
 * K Harrell	07/12/2009	Implement new OwnerData. Remove unused 
 * 							parameters.  Implement new class variables,
 * 							 csTransCd, caCTData  
 * 							add csTransCd, caCTData 
 * 							delete lineBreakHandling() 
 * 							modify queryData(), sectionStartingPoint(), 
 * 							 formatReceipt(), printInventoryAndFees(), 
 * 							 printNotationsAndPayment(), 
 * 							 printVehicleData() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	09/13/2010	modify formatReceipt()
 * 							defect 10590 Ver 6.6.0 
 * Min Wang		10/07/2010	Allow printing of the Max length of the price.
 * 							modify ITEMPRICELENGTH, MONEYPOSITION
 * 							defect 10596 Ver 6.6.0
 * Min Wang		10/26/2010	modify printInventoryAndFees()
 * 							defect 10596 Ver 6.6.0
 * Min Wang		11/03/2010	Lined up fees correctly registration correction
 * 							receipt.
 * 							modify ITEMPRICELENGTH, MONEYPOSITION,
 * 							printInventoryAndFees()
 * 							defect 10651 Ver 6.6.0
 * B Hargrove 	11/10/2010  Token Trailer now does not print sticker.
 * 							They want to store inv detail, just don't 
 * 							print sticker.
 * 							Should print from tray 2.
 * 							modify formatReceipt()
 * 							defect 10623 Ver 6.6.0
 * Min Wang		01/19/2011  Remove the original message and add new 
 * 							message.
 * 							modify AUTOMATION_1_STRING, 
 * 							AUTOMATION_2_STRING
 * 							defect 10715 Ver 6.7.0
 * Min Wang		01/21/2011	add NO_REGIS_REFUND_1_STRING, 
 * 							  NO_REGIS_REFUND_2_STRING
 * 							delete cbAutomateFees, AUTOMATION_1_STRING, 
 * 							 AUTOMATION_2_STRING
 * 							modify printInventoryAndFees(),printMessages()
 * 							defect 10715 Ver 6.7.0
 * K Harrell	02/02/2011	modify formatReceipt(), printVehicleData()
 * 							defect 10745 Ver 6.7.0
 * K Harrell	11/05/2011	Handle WRENEW Token Trailer
 * 							modify formatReceipt() 
 * 							defect 11138 Ver 6.9.0   
 * T Pederson	11/08/2011	add IMPORTANTDOCSTRING 
 * 							modify printMessages()
 * 							defect 11098 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */
/**
 * This class generates all the Registration Receipts.
 * 
 * @version	6.9.0 			11/08/2011	
 * @author	Ray Rowehl
 * <br>Creation Date: 		11/12/2001	07:33:33 
 */
public class GenRegistrationReceipts extends ReceiptTemplate
{
	// defect 10715
	//private boolean cbAutomateFees = false;
	// end defect 10715
	private boolean cbCreditFeeIncMsgIndi = true;

	// defect 10112 
	private String csTransCd;
	private CompleteTransactionData caCTData;
	// end defect 10112 

	private Dollar caFeeTotal = new Dollar(0);

	private static final int COLUMN = 5;
	private static final int FIRSTCOLUMNPOSITION = COLUMN;
	private static final int THIRDCOLUMNPOSITION = COLUMN + 58;
	// this is the position to print the owner address at 
	// if there is a renewal address
	private static final int STARTLINEPAYMENT = 42;
	private static final int STARTLINEMESSAGES = 70;
	private static final int FEEASSESSEDPOSITION = COLUMN + 44;
	private static final int FEEASSESSEDLENGTH = 30;
	// this is where the renewal notice goes
	private static final int TOTALPOSITION = COLUMN + 65;
	// defect 10596
	//private static final int ITEMPRICELENGTH = 12;
	// defect 10651
	//private static final int ITEMPRICELENGTH = 13;
	private static final int ITEMPRICELENGTH = 14;
	// end defect 10651
	// end defect 10596
	private static final int TONNAGEPOSITION = COLUMN + 63;
	private static final int DOLLARSIGNPOSITION = COLUMN + 75;
	// defect 10596 10651
	private static final int MONEYPOSITION = COLUMN + 77;
	//private static final int MONEYPOSITION = COLUMN + 79;
	// end defect 10596 10651
	private static final int MODELPOSITION = COLUMN + 25;
	private static final int TRANSOBJPOSITION = 0;
	private static final String TOTALSTRING = "TOTAL";
	private static final String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	private static final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	private static final String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	private static final String PREVPLATESTRING = "PREVIOUS PLATE NO: ";
	private static final String VEHCLASSSTRING =
		"VEHICLE CLASSIFICATION: ";
	private static final String VEHISSUEDSTRING =
		"THIS RECEIPT TO BE CARRIED IN THE VEHICLE FOR WHICH ISSUED.";
	private static final String VEHNOTATIONSTRING =
		"VEHICLE RECORD NOTATIONS";
	private static final String VINSTRING =
		"VEHICLE IDENTIFICATION NO: ";
	private static final String YRMAKESTRING = "YR/MAKE: ";
	// defect 11098
	private static final String IMPORTANTDOCSTRING =
		"IMPORTANT DOCUMENT: Please retain for your records.";
	// end defect 11098 
	private static final String COMMERCIALVEHICLESTRING =
		"THIS RECEIPT TO BE CARRIED IN ALL COMMERCIAL VEHICLES.";
	private static final String DOLLARSIGN = "$";
	private static final String SLASH = "/";
	private static final String MODELSTRING = "MODEL: ";
	private static final String BDYSTYLESTRING = "BODY STYLE: ";
	private static final int BDYSTYLEPOSITION = COLUMN + 37;
	private static final String UNITSTRING = "UNIT NO: ";
	private static final int UNITPOSITION = COLUMN + 55;
	private static final int PAYMENTPOSITION = COLUMN + 55;
	private static final int PAYMENTHEADERPOSITION = COLUMN + 50;
	private static final String EMPTYWTSTRING = "EMPTY WT: ";
	private static final String CARYCAPSTRING = "CARRYING CAPACITY: ";
	private static final int CARYCAPPOSITION = COLUMN + 19;
	private static final String GROSSWTSTRING = "GROSS WT: ";
	private static final int GROSSWTPOSITION = COLUMN + 46;
	private static final String TONNAGESTRING = "TONNAGE: ";
	private static final String BDYVINSTRING =
		"BODY VEHICLE IDENTIFICATION NO: ";
	private static final String TRAVTRLRSTRING = "TRAVEL TRLR LENGTH: ";
	private static final String INVITMSTRING = "INVENTORY ITEM(S)";
	private static final String YEARSTRING = "YR";
	private static final int YEARPOSITION = COLUMN + 36;
	private static final int YEARLENGTH = 4;
	private static final String FEEASSESSEDSTRING = "FEES ASSESSED";
	private static final String FOLDMARKSTRING = "..";
	private static final int LENGTH_1 = 1;
	public static final String REGCREDITSTRING =
		"** RETAIN RECEIPT FOR CREDIT ON NEXT YEAR'S REGISTRATION.";
	// defect 10715
	//	public static final String AUTOMATION_1_STRING =
	//		"Current law requires an additional $1.00 fee (already";
	//	public static final String AUTOMATION_2_STRING =
	//		"included) in counties with 50,000 or more vehicles.";
	public static final String NO_REGIS_REFUND_1_STRING =
		"Purchased registration remains with this vehicle and";
	public static final String NO_REGIS_REFUND_2_STRING =
		"will not be refunded if the vehicle is sold.";
	// end defect 10715

	/**
	 * GenRegistrationReceipts constructor
	 */
	public GenRegistrationReceipts()
	{
		super();
	}

	/**
	 * GenRegistrationReceipts constructor
	 * 
	 * @param asRcptName String
	 * @param aaRcptProps ReceiptProperties
	 */
	public GenRegistrationReceipts(
		String asRcptName,
		ReceiptProperties aaRcptProps)
	{
		super(asRcptName, aaRcptProps);
	}

	/**
	 * This method generates the Registration Renewal Receipt.
	 * 
	 * @param avDataVector Vector
	 */
	public void formatReceipt(Vector avDataVector)
	{
		// defect 10112
		// Implement class variables 
		caCTData =
			(CompleteTransactionData) avDataVector.elementAt(
				TRANSOBJPOSITION);

		csTransCd = caCTData.getTransCode();

		// defect 10590 
		// defect 10623
		// defect 11138 
		// Token Trailers no longer print sticker if not WRENEW, print from tray 2. 
		boolean lbDoNotPrntForTokenTrailer = 
			caCTData.getVehicleInfo().getRegData().isTokenTrailer()
					&& !csTransCd.equals(TransCdConstant.WRENEW); 

		if (!caCTData.isPreviewReceipt()
			&& caCTData.getStickers() != null
			&& caCTData.getStickers().size() > 0
			&& !lbDoNotPrntForTokenTrailer)
		{
			// end defect 10623
			// end defect 11138 
			cbShouldStickersPrint = true;
		}
		// end defect 10590 

		try
		{
			generateReceiptHeader(avDataVector);
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println("Had a problem with Heading Lookup!");
			aeRTSEx.printStackTrace();
		}

		if (!csTransCd.equals(TransCdConstant.DRVED))
		{
			sectionStartingPoint(22);

			// Process the Vehicle Information
			printVehicleData();
			sectionStartingPoint(32);
		}
		else
		{
			sectionStartingPoint(16);
		}

		// Process Inventory Items and Fees
		printInventoryAndFees();
		sectionStartingPoint(STARTLINEPAYMENT - 1);

		// Process Notations and Payment
		printNotationsAndPayment(avDataVector);
		sectionStartingPoint(STARTLINEMESSAGES - 1);

		// Process Messages
		printMessages();

		// defect 10745 
		if (csTransCd.equals(TransCdConstant.PAWT)
			|| csTransCd.equals(TransCdConstant.RENEW)
			|| csTransCd.equals(TransCdConstant.EXCH)
			|| csTransCd.equals(TransCdConstant.DUPL)
			|| csTransCd.equals(TransCdConstant.REPL)
			|| csTransCd.equals(TransCdConstant.CORREG)
			|| csTransCd.equals(TransCdConstant.SBRNW)
			|| csTransCd.equals(TransCdConstant.WRENEW))
		{
			// end defect 10745 
			caRpt.print("&a120H&a2196V" + FOLDMARKSTRING);
			caRpt.print("&a120H&a4752V" + FOLDMARKSTRING);
		}
		// end defect 10112

		if (cbShouldStickersPrint)
		{
			// defect 11138 
			// Check for Token Trailer is not necessary  
			// defect 10623
			// Do not print sticker for Token Trailer
			//if (caCTData.getVehicleInfo().getRegData().getRegClassCd()
			//	!= RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
			//{
			// end defect 11138
			generateStickers(caCTData);
				
			// defect 11138 
			//}
			// end defect 10623
			// end defect 11138 
		}
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
	 * @param aarrArgs String[] of command line arguments
	 * @throws RTSException
	 */
	public static void main(String[] aarrArgs) throws RTSException
	{
		// load Cache Manager for standalone testing
		// allows reading in of cache data
		CacheManager.loadCache();
		// Instantiating a new Report Class
		ReceiptProperties laRcptProps = new ReceiptProperties();
		GenRegistrationReceipts laGRR =
			new GenRegistrationReceipts(
				"NOT USED FOR RECEIPTS",
				laRcptProps);

		// Generating Demo data to display.
		// String method get its data from a generated object
		Vector lvFakeDataVector = laGRR.queryData("");
		try
		{
			laGRR.formatReceipt(lvFakeDataVector);
		}
		catch (Exception aeEx)
		{
			System.out.println("format had a problem");
			aeEx.printStackTrace();
		}

		// Writing the Formatted String onto a local file
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
		laPout.print(laGRR.caRpt.getReport().toString());
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
			laFrmPreviewReport.setVisibleRTS(true);
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
		// May have to handle Diesel Fee and Penalty Fee
		// separately from the Fees Vector..

		// defect 1336 
		// Only print the Inventory Header if there is Inventory Issued
		SpecialPlatesRegisData laSpclPltsRegisData =
			caCTData.getVehicleInfo().getSpclPltRegisData();

		String lsMfgPltCd = "";

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

		if (laSpclPltsRegisData != null
			&& laSpclPltsRegisData.getMFGStatusCd() != null
			&& laSpclPltsRegisData.getMFGStatusCd().equals(
				SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD))
		{
			ProcessInventoryData laInvData = new ProcessInventoryData();
			laInvData.setItmCd(laSpclPltsRegisData.getRegPltCd());
			laInvData.setInvItmNo(laSpclPltsRegisData.getRegPltNo());
			lsMfgPltCd = laSpclPltsRegisData.getRegPltCd();

			// defect 9085
			// Handle Annual Special Plates
			// Note:  Special Annual Plate Year (InvItmYr) reset for 
			//        !(UtilityMethods.isSpecialPlates(csTransCd) if
			//        mfg request.  
			//        Transaction.populateSRFuncTrans()

			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(
					laSpclPltsRegisData.getRegPltCd());

			if (laPltTypeData.getAnnualPltIndi() == 1)
			{
				int liYear = laSpclPltsRegisData.getInvItmYr();
				if (liYear == 0)
				{
					liYear =
						caCTData
							.getVehicleInfo()
							.getRegData()
							.getRegExpYr();
				}
				laInvData.setInvItmYr(liYear);
			}
			// end defect 9085 

			lvAllocInvItms.addElement(laInvData);
		}
		// end defect 9145

		if (lvAllocInvItms.size() > 0)
		{
			caRpt.print(
				INVITMSTRING,
				FIRSTCOLUMNPOSITION,
				INVITMSTRING.length());
			caRpt.print(YEARSTRING, YEARPOSITION - 5, YEARLENGTH);
		}

		if (!csTransCd.equals(TransCdConstant.DRVED))
		{
			caRpt.print(
				FEEASSESSEDSTRING,
				FEEASSESSEDPOSITION,
				FEEASSESSEDSTRING.length());
		}

		caRpt.blankLines(1);

		boolean lbKeepLooping = true; // Loop Controller 
		boolean lbPrintMfgNotation = false;
		int liInvCount = 0; // index through the inventory vector
		int liFeeCount = 0; // index through the Fees vector
		int liToday = new RTSDate().getYYYYMMDDDate();
		Dollar laFeeTotal = new Dollar(0);

		while (lbKeepLooping)
		{
			if (liInvCount < lvAllocInvItms.size())
			{
				ProcessInventoryData laInvData =
					(ProcessInventoryData) lvAllocInvItms.elementAt(
						liInvCount);

				// Get the Item Description from cache
				ItemCodesData laItmCode =
					ItemCodesCache.getItmCd(laInvData.getItmCd());
				String lsDesc = laItmCode.getItmCdDesc();

				// defect 9145
				// Printing the mfg notation
				if (laItmCode.getItmCd().trim().equals(lsMfgPltCd))
				{
					lbPrintMfgNotation = true;
				}
				// end defect 9145

				caRpt.print(
					lsDesc,
					FIRSTCOLUMNPOSITION,
					lsDesc.length());
				caRpt.print(
					laInvData.getInvItmYr() == 0
						? ""
						: String.valueOf(laInvData.getInvItmYr()),
					YEARPOSITION - 5,
					YEARLENGTH);
			}
			liInvCount = liInvCount + 1;

			if (liFeeCount < lvRegFees.size())
			{
				FeesData laFeeData =
					(FeesData) lvRegFees.elementAt(liFeeCount);

				if (UtilityMethods.isEmpty(laFeeData.getDesc()))
				{
					AccountCodesData laAcctData =
						AccountCodesCache.getAcctCd(
							laFeeData.getAcctItmCd().trim(),
							liToday);
					if (laAcctData != null)
					{
						laFeeData.setDesc(
							laAcctData.getAcctItmCdDesc());
					}
				}
				caRpt.print(
					laFeeData.getDesc(),
					FEEASSESSEDPOSITION,
					FEEASSESSEDLENGTH);

				caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
				// S447500 just prints the price.  assumes that qty is 1.

				// defect 10596 10651
				caRpt
					.rightAlign(
						laFeeData
							.getItemPrice()
							.printDollar()
							.substring(
							1),
				//85,
				MONEYPOSITION, ITEMPRICELENGTH);
				// end defect 10596 10651

				liFeeCount = liFeeCount + 1;
				laFeeTotal = (laFeeTotal.add(laFeeData.getItemPrice()));
				// defect 10715
				// TODO Add AUTOMATE to AcctCdConstant 
				//if (laFeeData.getAcctItmCd().equals("AUTOMATE"))
				//{
				//	cbAutomateFees = true;
				//}
			}

			caRpt.blankLines(1);

			// defect 9145
			// Printing the mfg notation
			if (lbPrintMfgNotation)
			{
				printManufacturingNotation(FIRSTCOLUMNPOSITION);
				lbPrintMfgNotation = false;
			}
			// end defect 9145

			// if both vectors are finished turn off lbKeepLooping
			if (liInvCount >= lvAllocInvItms.size()
				&& liFeeCount >= lvRegFees.size())
			{
				lbKeepLooping = false;

				// Remove element for correct processing in Transaction
				if (!lsMfgPltCd.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					lvAllocInvItms.removeElementAt(
						lvAllocInvItms.size() - 1);
				}
			}
		} // end while

		// caFeeTotal is for moving total fees into total amount paid
		// for internet regis total
		// amount paid, which was zero, but the counties didn't like that 
		caFeeTotal = laFeeTotal;
		// Print Total if there are any records

		if (!csTransCd.equals(TransCdConstant.DRVED))
		{
			caRpt.blankLines(1);

			caRpt.print(
				TOTALSTRING,
				TOTALPOSITION - 3,
				TOTALSTRING.length());
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
			// defect 10596 10651
			caRpt.rightAlign(laFeeTotal.printDollar().substring(1),
			//85,
			MONEYPOSITION, ITEMPRICELENGTH);
			// end defect 10596 10651
			caRpt.blankLines(1);

			if (caCTData.getCrdtRemaining() != null
				&& !caCTData.getCrdtRemaining().equals(
					new Dollar("0.00")))
			{
				caRpt.rightAlign(
					"** CREDIT REMAINING -",
					FEEASSESSEDPOSITION,
					FEEASSESSEDLENGTH);
				caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
				caRpt.rightAlign(
					caCTData
						.getCrdtRemaining()
						.printDollar()
						.substring(
						1),
					MONEYPOSITION,
					ITEMPRICELENGTH);

				caRpt.blankLines(1);
			}
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
			// defect 10112 
			// Implement class variables 
			if (caCTData.getCrdtRemaining() != null
				&& !caCTData.getCrdtRemaining().equals(new Dollar(0.00)))
			{
				caRpt.print(
					REGCREDITSTRING,
					FIRSTCOLUMNPOSITION,
					REGCREDITSTRING.length());
				caRpt.nextLine();
			}

			// print the Commercial Vehicle message.
			// Look up the Trans Code to get RCPTMSGCD to see if we
			// need to print
			// the COMMERCIALVEHICLESTRING.
			// this look up can create an RTSException.  We are just 
			// eating it!
			TransactionCodesData laTransCodeData =
				(
					TransactionCodesData) TransactionCodesCache
						.getTransCd(
					csTransCd);

			// normal message
			if (laTransCodeData.getRcptMsgCd() == 1)
			{
				// defect 11098
				caRpt.printAttributesNoReturn(Print.getPRINT_BOLD());
				caRpt.print(
					IMPORTANTDOCSTRING,
					FIRSTCOLUMNPOSITION,
					IMPORTANTDOCSTRING.length());
				caRpt.nextLine();
				caRpt.printAttributesNoReturn(Print.getPRINT_NORMAL());
				// end defect 11098 
				caRpt.print(
					COMMERCIALVEHICLESTRING,
					FIRSTCOLUMNPOSITION,
					COMMERCIALVEHICLESTRING.length());
				caRpt.nextLine();
			}
			// message for permits, tow trucks and other cases matching
			// these conditions.
			else if (laTransCodeData.getRcptMsgCd() == 2)
			{
				caRpt.print(
					VEHISSUEDSTRING,
					FIRSTCOLUMNPOSITION,
					VEHISSUEDSTRING.length());
				caRpt.nextLine();
			}
			// defect 10715
			//if (cbAutomateFees)
			//{
			caRpt.print(
				NO_REGIS_REFUND_1_STRING,
				FIRSTCOLUMNPOSITION,
				NO_REGIS_REFUND_1_STRING.length());
			caRpt.nextLine();
			caRpt.print(
				NO_REGIS_REFUND_2_STRING,
				FIRSTCOLUMNPOSITION,
				NO_REGIS_REFUND_2_STRING.length());
			caRpt.nextLine();
			//}
			// end defect 10715
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
	 * Print the Notations and Payment Section.
	 * 
	 * @param avDataVector Vector
	 */
	public void printNotationsAndPayment(Vector avDataVector)
	{
		// defect 10112 
		// Implement class variables 
		if (!(csTransCd.equals(TransCdConstant.DRVED)))
		{
			// Notations are supposed to start at line 31?
			// build up the Notations Vector
			Vector lvNotationsData = buildNotationsVector(caCTData);

			// add Doc type for DUPL event
			if (csTransCd.equals(TransCdConstant.DUPL))
			{
				if (caCTData
					.getVehicleInfo()
					.getTitleData()
					.getOwnrShpEvidCd()
					== 21)
				{
					lvNotationsData.addElement(
						"DOCUMENT TYPE : REGISTRATION PURPOSES ONLY");
				}
				else
				{
					int liDocTypeCd =
						caCTData
							.getVehicleInfo()
							.getTitleData()
							.getDocTypeCd();

					String lsDesc = "";
					DocumentTypesData laDocData =
						DocumentTypesCache.getDocType(liDocTypeCd);

					if (laDocData != null)
					{
						lsDesc = laDocData.getDocTypeCdDesc();
					}
					if (lsDesc == null)
					{
						lsDesc = "";
					}
					lvNotationsData.addElement(
						"DOCUMENT TYPE : " + lsDesc);
				}
			}
			// build up the Payment Vector
			Vector lvPaymentData = buildPaymentVector(avDataVector);
			// end defect 10112 

			// defect 10745 
			if (!csTransCd.equals(TransCdConstant.WRENEW))
			{
				caRpt.print(
					VEHNOTATIONSTRING,
					FIRSTCOLUMNPOSITION,
					VEHNOTATIONSTRING.length());
			}
			// end defect 10745 

			// print payment header if there are any payment objects
			if (lvPaymentData.size() > 0)
			{
				caRpt.print(
					PAYMENTSTRING,
					PAYMENTHEADERPOSITION + 2,
					PAYMENTSTRING.length());
			}

			caRpt.blankLines(1);

			Dollar laZeroDollar = new Dollar(0);
			Dollar laPaymentTotal = new Dollar(0);
			Dollar ldChangeBackAmt = new Dollar(0);
			int liNotationCount = 0;

			// index through the Vehicle Notations vector
			int liPaymentCount = 0;
			String lsPymntType = "";
			boolean lbKeepLooping = true;

			while (lbKeepLooping)
			{
				if (liNotationCount < lvNotationsData.size())
				{
					caRpt.print(
						(String) lvNotationsData.elementAt(
							liNotationCount),
						FIRSTCOLUMNPOSITION,
						((String) lvNotationsData
							.elementAt(liNotationCount))
							.length());
					liNotationCount = liNotationCount + 1;
				}
				if (liPaymentCount < lvPaymentData.size())
				{
					if (cbCreditFeeIncMsgIndi)
					{
						// defect 10112 
						//	if (!cbCreditFeeIncMsgIndi)
						//	{
						//		liPaymentCount = liPaymentCount + 1;
						//	}
						// end defect 10112 

						cbCreditFeeIncMsgIndi = false;

						if (liPaymentCount < lvPaymentData.size())
						{
							TransactionPaymentData laPaymentData =
								(
									TransactionPaymentData) lvPaymentData
										.elementAt(
									liPaymentCount);

							if (!laPaymentData
								.getPymntTypeAmt()
								.equals(laZeroDollar))
							{
								caRpt.print(
									laPaymentData.getPymntType(),
									PAYMENTPOSITION + 6,
									laPaymentData
										.getPymntType()
										.length());
								// print the check number if it exists
								if (laPaymentData.getPymntCkNo()
									!= null)
								{
									caRpt.print(
										" #"
											+ laPaymentData
												.getPymntCkNo());
								}
								caRpt.print(
									DOLLARSIGN,
									DOLLARSIGNPOSITION,
									LENGTH_1);
								caRpt.rightAlign(
									laPaymentData
										.getPymntTypeAmt()
										.printDollar()
										.substring(
										1),
									MONEYPOSITION,
									ITEMPRICELENGTH);
								liPaymentCount = liPaymentCount + 1;
								laPaymentTotal =
									(laPaymentTotal
										.add(
											laPaymentData
												.getPymntTypeAmt()));
								if ((laPaymentData.getChngDue()
									!= null)
									&& !laPaymentData.getChngDue().equals(
										laZeroDollar))
								{
									ldChangeBackAmt =
										laPaymentData.getChngDue();
								}
							}
							//	payment = 0 for internet regis renewl
							else
							{
								++liPaymentCount;
								// where we want to use Internet
								// as payment type and total fees as 
								// payment aount.
								if (csTransCd
									.equals(TransCdConstant.IRENEW))
								{
									laPaymentData.setPymntType(
										"INTERNET");
									caRpt.print(
										laPaymentData.getPymntType(),
										PAYMENTPOSITION + 6,
										laPaymentData
											.getPymntType()
											.length());
									laPaymentTotal = caFeeTotal;
									caRpt.print(
										DOLLARSIGN,
										DOLLARSIGNPOSITION,
										LENGTH_1);
									caRpt.rightAlign(
										laPaymentTotal
											.printDollar()
											.substring(
											1),
										MONEYPOSITION,
										ITEMPRICELENGTH);
								}
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
						if (!laPaymentData
							.getPymntTypeAmt()
							.equals(laZeroDollar))
						{
							caRpt.print(
								laPaymentData.getPymntType(),
								PAYMENTPOSITION + 6,
								laPaymentData.getPymntType().length());

							// print the check number if it exists
							if (laPaymentData.getPymntCkNo() != null)
							{
								caRpt.print(
									" #"
										+ laPaymentData.getPymntCkNo());
							}
							caRpt.print(
								DOLLARSIGN,
								DOLLARSIGNPOSITION,
								LENGTH_1);
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
									.add(
										laPaymentData
											.getPymntTypeAmt()));
							if ((laPaymentData.getChngDue() != null)
								&& !laPaymentData.getChngDue().equals(
									laZeroDollar))
							{
								ldChangeBackAmt =
									laPaymentData.getChngDue();
							}
						}
						// payments = 0, which is true at county for internet renewal
						else
						{
							++liPaymentCount;
						}
					}
				}
				caRpt.nextLine();
				// if both vectors are finished turn off lbKeepLooping
				// remove notation vector stuff for now..
				if (liNotationCount >= lvNotationsData.size()
					&& liPaymentCount >= lvPaymentData.size())
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
			// Print the Total Payment Line.  Print the total if there was data.
			// Even if the total is zero (CQU100001336)
			if (lvPaymentData.size() > 0)
			{
				caRpt.nextLine();
				caRpt.print(
					PAYMENTTOTALSTRING,
					PAYMENTPOSITION + 2,
					PAYMENTTOTALSTRING.length());
				caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
				caRpt.rightAlign(
					laPaymentTotal.printDollar().substring(1),
					MONEYPOSITION,
					ITEMPRICELENGTH);
				caRpt.nextLine();
			}
			// Print out the Change Due Line
			if (!ldChangeBackAmt.equals(laZeroDollar))
			{
				caRpt.print(
					PAYMENTCHANGEDUESTRING,
					PAYMENTPOSITION + 2,
					PAYMENTCHANGEDUESTRING.length());
				caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
				caRpt.rightAlign(
					ldChangeBackAmt.printDollar().substring(1),
					MONEYPOSITION,
					ITEMPRICELENGTH);
				caRpt.nextLine();
			}
		}
	}

	/**
	 * Print the Vehicle Information Information.
	 * 
	 * @param aaTransData CompleteTransactionData
	 */
	public void printVehicleData()
	{
		// defect 10112 
		caRpt.blankLines(1);

		caRpt.print(
			PREVPLATESTRING,
			FIRSTCOLUMNPOSITION,
			PREVPLATESTRING.length());

		if (caCTData.getVehicleInfo().getRegData().getPrevPltNo()
			!= null)
		{
			caRpt.print(
				caCTData.getVehicleInfo().getRegData().getPrevPltNo());
		}
		else
		{
			caRpt.print("");
		}
		if (csTransCd.equals(TransCdConstant.EXCH)
			|| csTransCd.equals(TransCdConstant.DUPL))
		{
			// defect 8900
			//			caRpt.print(
			//				"PREVIOUS EXPIRATION DATE: ",
			//				33,
			//				"PREVIOUS EXPIRATION DATE: ".length());
			int liExpMonth =
				caCTData.getVehicleInfo().getRegData().getPrevExpMo();
			int liExpYr =
				caCTData.getVehicleInfo().getRegData().getPrevExpYr();
			String lsExpDate = "";
			lsExpDate =
				String.valueOf(liExpMonth)
					+ "/"
					+ String.valueOf(liExpYr);
			//caRpt.print(lsExpDate, 61, 7);
			printPrevExpMoYr(lsExpDate, 33);
			// end defect 8900

			caRpt.nextLine();
			caRpt.print(
				VEHCLASSSTRING,
				FIRSTCOLUMNPOSITION,
				VEHCLASSSTRING.length());
			caRpt.print(
				caCTData
					.getVehicleInfo()
					.getVehicleData()
					.getVehClassCd());

			if (csTransCd.equals(TransCdConstant.DUPL))
			{
				caRpt.print(
					"CUSTOMER REG FEES PAID: ",
					45,
					"CUSTOMER REG FEES PAID: ".length());

				// defect 9018 
				Dollar laDollar =
					caCTData
						.getVehicleInfo()
						.getRegData()
						.getCustActlRegFee();

				if (laDollar == null)
				{
					laDollar = new Dollar("0.00");
				}
				caRpt.print(laDollar.printDollar());
				// end defect 9018 
			}
		}
		else
		{
			// defect 10745
			if (!(csTransCd.equals(TransCdConstant.SBRNW)
				|| csTransCd.equals(TransCdConstant.WRENEW)))
			{
				// end defect 10745
				caRpt.print(
					VEHCLASSSTRING,
					THIRDCOLUMNPOSITION - 7,
					VEHCLASSSTRING.length());
				caRpt.print(
					caCTData
						.getVehicleInfo()
						.getVehicleData()
						.getVehClassCd());
			}
		}

		caRpt.blankLines(1);

		// may have to check for Special Owner?
		caRpt.print(VINSTRING, FIRSTCOLUMNPOSITION, VINSTRING.length());
		caRpt.print(
			caCTData.getVehicleInfo().getVehicleData().getVin());

		caRpt.blankLines(1);

		// defect 10745
		if (!(csTransCd.equals(TransCdConstant.SBRNW)
			|| csTransCd.equals(TransCdConstant.WRENEW)))
		{
			// end defect 10745 
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
			caRpt.print(
				MODELSTRING,
				MODELPOSITION - 5,
				MODELSTRING.length());
			caRpt.print(
				caCTData
					.getVehicleInfo()
					.getVehicleData()
					.getVehModl());
			caRpt.print(
				BDYSTYLESTRING,
				BDYSTYLEPOSITION - 5,
				BDYSTYLESTRING.length());
			caRpt.print(
				caCTData
					.getVehicleInfo()
					.getVehicleData()
					.getVehBdyType());
			caRpt.print(
				UNITSTRING,
				UNITPOSITION - 5,
				UNITSTRING.length());
			caRpt.print(
				caCTData
					.getVehicleInfo()
					.getTitleData()
					.getVehUnitNo());

			caRpt.blankLines(1);

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
				CARYCAPPOSITION - 1,
				CARYCAPSTRING.length());
			caRpt.print(
				String.valueOf(
					caCTData
						.getVehicleInfo()
						.getRegData()
						.getVehCaryngCap()));
			caRpt.print(
				GROSSWTSTRING,
				GROSSWTPOSITION - 1,
				GROSSWTSTRING.length());
			caRpt.print(
				String.valueOf(
					caCTData
						.getVehicleInfo()
						.getRegData()
						.getVehGrossWt()));
			caRpt.print(
				TONNAGESTRING,
				TONNAGEPOSITION - 1,
				TONNAGESTRING.length());
			if (caCTData.getVehicleInfo().getVehicleData().getVehTon()
				!= null)
			{
				caRpt.print(
					caCTData
						.getVehicleInfo()
						.getVehicleData()
						.getVehTon()
						.toString());
			}
			else
			{
				caRpt.print("");
			}

			caRpt.blankLines(1);

			if (!csTransCd.equals(TransCdConstant.PAWT))
			{
				caRpt.print(
					BDYVINSTRING,
					FIRSTCOLUMNPOSITION,
					BDYVINSTRING.length());
				caRpt.print(
					caCTData
						.getVehicleInfo()
						.getVehicleData()
						.getVehBdyVin());
				caRpt.print(
					TRAVTRLRSTRING,
					THIRDCOLUMNPOSITION - 2,
					TRAVTRLRSTRING.length());
				caRpt.print(
					String.valueOf(
						caCTData
							.getVehicleInfo()
							.getVehicleData()
							.getVehLngth()));

				caRpt.blankLines(1);
			}
			// end defect 7540		
		}
		// defect 10745 
		else if (
			csTransCd.equals(TransCdConstant.WRENEW)
				&& caCTData.getVehicleInfo().getRegData().getVehGrossWt()
					!= 0)
		{
			caRpt.print(
				GROSSWTSTRING,
				FIRSTCOLUMNPOSITION,
				GROSSWTSTRING.length());

			caRpt.print(
				String.valueOf(
					caCTData
						.getVehicleInfo()
						.getRegData()
						.getVehGrossWt()));

		}
		// end defect 10745 

		if (csTransCd.equals(TransCdConstant.DUPL))
		{
			caRpt.nextLine();
			caRpt.print(
				"REGISTRATION ISSUE DATE: ",
				FIRSTCOLUMNPOSITION,
				"REGISTRATION ISSUE DATE: ".length());
			int liDate =
				caCTData.getVehicleInfo().getRegData().getRegIssueDt();
			if (liDate == 0)
			{
				caRpt.print("");
			}
			else
			{
				caRpt.print(
					new RTSDate(RTSDate.YYYYMMDD, liDate).toString());
			}
			caRpt.print(
				"COUNTY OF REGISTRATION: ",
				44,
				"COUNTY OF REGISTRATION: ".length());
			caRpt.print(
				""
					+ caCTData
						.getVehicleInfo()
						.getRegData()
						.getResComptCntyNo());
			caRpt.blankLines(4);
		}
		if (csTransCd.equals(TransCdConstant.RENEW)
			|| csTransCd.equals(TransCdConstant.IRENEW)
			|| csTransCd.equals(TransCdConstant.EXCH)
			|| csTransCd.equals(TransCdConstant.PAWT)
			|| csTransCd.equals(TransCdConstant.REPL))
		{
			Dollar laDieselFee = caCTData.getDieselFee();

			Dollar laRegPenaltyFee = caCTData.getRegisPenaltyFee();
			boolean lbDiesel = false;
			boolean lbRegPenalty = false;
			if (laDieselFee != null
				&& !laDieselFee.equals(new Dollar("0.00")))
			{
				lbDiesel = true;
			}
			if (laRegPenaltyFee != null
				&& !laRegPenaltyFee.equals(new Dollar("0.00")))
			{
				lbRegPenalty = true;
			}
			if (lbDiesel || lbRegPenalty)
			{
				caRpt.nextLine();
				caRpt.print(
					"ITEMIZED FEES",
					FEEASSESSEDPOSITION,
					FEEASSESSEDLENGTH);
				caRpt.nextLine();
				if (lbDiesel)
				{
					caRpt.print(
						"DIESEL FEE   $  ",
						FEEASSESSEDPOSITION,
						"DIESEL FEE   $  ".length());
					caRpt.print(laDieselFee.printDollar().substring(1));
					caRpt.nextLine();
				}
				if (lbRegPenalty)
				{
					caRpt.print(
						"REG PENALTY  $  ",
						FEEASSESSEDPOSITION,
						"REG PENALTY  $  ".length());
					caRpt.print(
						laRegPenaltyFee.printDollar().substring(1));
					caRpt.nextLine();
				}
				caRpt.nextLine();
			}
		}
		// end defect 10112 
	}

	/**
	 * This version of queryData returns the CompleteTransactionData 
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
		// defect 10112 
		laOwnrData.setName1("Ray Rowehl");
		laOwnrData.setName2("Roger Ramjet");
		laAddrData.setCity("Austin");
		laAddrData.setSt1("Some Street1");
		laAddrData.setState("TX");
		laAddrData.setZpcd("2222222");
		laAddrData.setZpcdp4("2222");
		laOwnrData.setAddressData(laAddrData);
		// end defect 10112 
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
		//Vector lvVectFees = new Vector();
		// create the Complete Transaction Data object to pass
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
		laTransData.setInvItms(lvInvData);
		// add the inventory vector
		laTransData.setRegFeesData(laRegFeesData);
		// add the fees data
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
	public Vector queryData(String lsQuery)
	{
		Vector lvReceipt = new Vector();
		//Vehicle laCtdObj = new Vehicle();
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

	/**
	 * This method positions to the line requested.
	 * 
	 * @param aiLineNumber int
	 * @param asTransCode String
	 */
	public void sectionStartingPoint(int aiLineNumber)
	{
		// compute number of lines to skip
		aiLineNumber = aiLineNumber - caRpt.getCurrX();
		for (int i = 0; i < aiLineNumber; i++)
		{
			// defect 10112
			caRpt.blankLines(1);
			;
			// end defect 10112 
		}
	}
}