package com.txdot.isd.rts.services.reports.accounting;import java.awt.event.WindowAdapter;import java.awt.event.WindowEvent;import java.io.File;import java.io.FileOutputStream;import java.io.IOException;import java.io.PrintStream;import java.util.Vector;import com.txdot.isd.rts.services.data.*;import com.txdot.isd.rts.services.reports.FrmPreviewReport;import com.txdot.isd.rts.services.reports.ReceiptTemplate;import com.txdot.isd.rts.services.reports.ReportProperties;import com.txdot.isd.rts.services.reports.ReportTemplate;import com.txdot.isd.rts.services.util.Dollar;import com.txdot.isd.rts.services.util.UtilityMethods;/* * * GenTimeLagReport.java * * (c) Texas Department of Transportation 2002 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * J Rue		09/19/2001	*** * S Govindappa 07/18/2002	Added the code for PCR25 to display *							credit card fee message on the report  *							when CHARGE is a method of payment. * MAbs			08/28/2002	PCR 41 * MAbs			09/17/2002	PCR 41 Integration * MListberger	10/17/2002	Move first page heading starting point down  *                          one line.  This matches all other reports * 							within the system and page 2 of this report. * 							Defect 4684. * MAbs			10/22/2002	PCR 41 Fixes * Jim Zwiener	05/17/2004	Remove AcctCd from Addl Collections report *							modify printColumnHeadings() *							modify printItemCodes() *							defect 6945 Ver 5.2.0 Fix 0 * K Harrell	06/04/2004	Additional formatting of report *							Added a line between the column headers and  *							the detail. Adjusted start position for *							QUANTITY and ITEM PRICE.Reduced width for *							item prices to 13 char vs. 15.Modified *							printing for Total w/ Credit Card so that: *							a) Total printed in same position / format  *							   as Total w/o Credit Card *							b) Right aligned "(Credit Card Fee)" message *						       ==> This is consistent w/ Receipts.  *							code reorganization, javadoc cleanup *							defect 7147   Ver 5.2.0  * S Johnston	05/10/2005	chg '/**' to '/*' to begin prolog. * 							defect 7896 Ver 5.2.3 	 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade * 							modify  *							defect 7896 Ver 5.2.3 * K Harrell	08/10/2009	Implement new generateFooter(boolean)  * 							modify formatReport() * 							defect 8628 Ver Defect_POS_F  * --------------------------------------------------------------------- *//** * Generates reports for Additional Collections * * @version	Defect_POS_F 	08/10/2009 * @author	Jeff Rue * <br>Creation Date:		01/08/2002  *//* &GenTimeLagReport& */public class GenTimeLagReport extends ReportTemplate{/* &GenTimeLagReport'caCompleteTrans& */	public CompleteTransactionData caCompleteTrans =		new CompleteTransactionData();/* &GenTimeLagReport'caFeesData& */	public FeesData caFeesData = new FeesData();/* &GenTimeLagReport'caMFVehData& */	public MFVehicleData caMFVehData = new MFVehicleData();/* &GenTimeLagReport'caProcessInvData& */	public ProcessInventoryData caProcessInvData = new ProcessInventoryData();/* &GenTimeLagReport'caReceiptTemplate& */	public ReceiptTemplate caReceiptTemplate = new ReceiptTemplate();/* &GenTimeLagReport'caRegFeeData& */	public RegFeesData caRegFeeData = new RegFeesData();/* &GenTimeLagReport'caRegisData& */	public RegistrationData caRegisData = new RegistrationData();/* &GenTimeLagReport'caTitleData& */	public TitleData caTitleData = new TitleData();/* &GenTimeLagReport'cvFeesData& */	public Vector cvFeesData = new Vector();	// NUMBER CONSTANTS/* &GenTimeLagReport'NUM_05& */	private final static int NUM_05 = 5;/* &GenTimeLagReport'NUM_08& */	private final static int NUM_08 = 8;	// defect 6945 / 7147	// create new constants for alignment/* &GenTimeLagReport'NUM_13& */	private final static int NUM_13 = 13;/* &GenTimeLagReport'NUM_15& */	private final static int NUM_15 = 15;/* &GenTimeLagReport'NUM_42& */	private final static int NUM_42 = 42;/* &GenTimeLagReport'NUM_47& */	private final static int NUM_47 = 47;/* &GenTimeLagReport'NUM_60& */	private final static int NUM_60 = 60;/* &GenTimeLagReport'NUM_62& */	private final static int NUM_62 = 62;/* &GenTimeLagReport'NUM_67& */	private final static int NUM_67 = 67;	// end defect 6945 / 7147 /* &GenTimeLagReport'caTotItemPrice& */	private Dollar caTotItemPrice = new Dollar("0.00");/* &GenTimeLagReport'csReportNumber& */	private final static String csReportNumber = "RTS.POS.2301";/* &GenTimeLagReport'csReportName& */	private final static String csReportName =		"ADDITIONAL COLLECTIONS REPORT";/* &GenTimeLagReport'csFilePathLocation& */	private final static String csFilePathLocation =		"C:\\RTS\\RPT\\ADJ0.RPT";/* &GenTimeLagReport'REASONTEXT& */	private final static String REASONTEXT = "REASON:  ";/* &GenTimeLagReport'TRANSIDTEXT& */	private final String TRANSIDTEXT = "TRANSID:";/* &GenTimeLagReport'TOTALTEXT& */	private final String TOTALTEXT = "TOTAL:";/* &GenTimeLagReport'DESCRIPTIONTEXT& */	private final String DESCRIPTIONTEXT =		"ACCOUNT ITEM CODE DESCRIPTION";/* &GenTimeLagReport'QUANTITY& */	private final String QUANTITY = "QUANTITY";/* &GenTimeLagReport'ITEMPRICETEXT& */	private final String ITEMPRICETEXT = "ITEM PRICE";/* &GenTimeLagReport'DOLLARSIGNTEXT& */	private final String DOLLARSIGNTEXT = "$";/* &GenTimeLagReport'CREDIT_CARD_MSG& */	private final String CREDIT_CARD_MSG = "(Credit Card Fee)";	/**	 * GenTimeLagReport constructor	 *//* &GenTimeLagReport.GenTimeLagReport& */	public GenTimeLagReport()	{		super();	}		/**	 * 	 * GenTimeLagReport(String, ReportProperties) constructor	 * 	 * @param asRptString String	 * @param asRptProperties ReportProperties	 *//* &GenTimeLagReport.GenTimeLagReport$1& */	public GenTimeLagReport(		String asRptString,		ReportProperties asRptProperties)	{		super(asRptString, asRptProperties);	}		/**	 * Get the object off of the DataVector and check to see it they are	 * Payment Objects.  If they are, put them on the outgoing Payment	 * Vector. This method was added for PCR25	 * 	 * @param avQueryData Vector	 * @return Vector	 *//* &GenTimeLagReport.buildPaymentVector& */	public Vector buildPaymentVector(Vector avDataVector)	{		// create the Payment Vector		Vector lvPaymentVector = new Vector();		// counter for adding elements to Vector		int liPaymentCounter = 0;		for (int i = 0; i < avDataVector.size(); i++)		{			// pull the object out so we can inspect it			Object laThisElement = avDataVector.elementAt(i);			// inspect the object to see if it is a			// TransactionPaymentData object			if (laThisElement instanceof TransactionPaymentData)			{				lvPaymentVector.addElement(					(TransactionPaymentData) avDataVector.elementAt(i));				liPaymentCounter = liPaymentCounter + 1;			}		}		// Send the new Payment Vector back.		return lvPaymentVector;	}		/**	 * Format the string to show the Credit Card Fee Added message if	 * credit card is charged. This method was added for PCR25 	 * 	 * @return String - This is the message about the charge	 * @deprecated	 *//* &GenTimeLagReport.formatCreditCardFeeMessage& */	protected String formatCreditCardFeeMessage()	{		return "(Credit Card Fee)";		/*		String creditCardMessage = "";		CreditCardFeeData creditFeeInfo = CreditCardFeesCache.		getCurrentCreditCardFees(SystemProperty.getOfficeIssuanceNo(),		new RTSDate());		if (creditFeeInfo != null && ! creditFeeInfo.isPercentage() &&		creditFeeInfo.getItmPrice().compareTo(new Dollar("0.00")) > 0)			return "(INCLUDES $ " + creditFeeInfo.getItmPrice() +			" AS CREDIT CARD FEE)";		else if (creditFeeInfo != null && creditFeeInfo.getItmPrice().		compareTo(new Dollar("0.00")) > 0)		{			Dollar sum = new Dollar("0.00");			Dollar percentage = creditFeeInfo.getItmPrice().divide(			new Dollar("100.00"));			sum = sum.add(paymentData.getPymntTypeAmt());			sum = sum.multiply(new Dollar("1.00").subtract(percentage));			Dollar creditAddOn = sum.multiply(percentage);			return "(INCLUDES $ " + creditAddOn +			" AS CREDIT CARD FEE)";		}		return creditCardMessage;		*/	}		/**	 * Pad fees to 13 characters. Add the dollar sign.	 * 	 * @param asDollarAmt String	 * @return String	 *//* &GenTimeLagReport.formatDollarAmt& */	private String formatDollarAmt(String asDollarAmt)	{		// Get string and pad length		asDollarAmt =			UtilityMethods.addPadding(asDollarAmt, NUM_13, " ");		// Add dollar sign		asDollarAmt =			caRpt.blankSpaces(1) + DOLLARSIGNTEXT + asDollarAmt;		return asDollarAmt;	}		/**	 * Generate the Time Lag report	 * 	 * @param avResults Vector 	 *//* &GenTimeLagReport.formatReport& */	public void formatReport(Vector avResults)	{		Vector lvHoldData = new Vector();		// Complete Transaction Data		caCompleteTrans =			(CompleteTransactionData) avResults.elementAt(0);		caRegFeeData = caCompleteTrans.getRegFeesData();		cvFeesData = caRegFeeData.getVectFees();		// Generate the header		caRpt.nextLine(); //  defect 4684		generateTimeLagHdr();		// Print TransId		printTransId();		// Print column headings		printColumnHeadings();		// Get item descriptions and prices		lvHoldData = getItemDescPrices();		// Print item codes		printItemCodes(lvHoldData);		// Print totals		generateReportTotal(avResults);		// Print Reason		printReason();		// defect 8628 		// Print end of report		//generateEndOfReport();		// Print Footer		//generateFooter();		generateFooter(true); 		// end defect 8628  	}		/**	 * This abstract method must be implemented in all subclasses	 *//* &GenTimeLagReport.generateAttributes& */	public void generateAttributes()	{		// empty code block	}		/**	 * Print the total item prices for the report and credit fee 	 * addition message on charge.	 * 	 * @param avResults Vector	 *//* &GenTimeLagReport.generateReportTotal& */	private void generateReportTotal(Vector avResults)	{		this.caRpt.blankLines(2);		this.caRpt.print(			TOTALTEXT,			NUM_42,			caReceiptTemplate.charLength(TOTALTEXT));		// build up the Payment Vector		Vector lvPaymentData = buildPaymentVector(avResults);		String lsPrice = "";		boolean lbCreditFeeIncMsgIndi = false;		if (lvPaymentData != null)		{			for (int i = 0; i < lvPaymentData.size(); i++)			{				TransactionPaymentData laPaymentData =					(TransactionPaymentData) lvPaymentData.elementAt(i);				//Code for PCR25 changes				if (laPaymentData.isCreditCardFee())				{					lbCreditFeeIncMsgIndi = true;					String lsCreditPrice =						laPaymentData.getPymntTypeAmt().toString();					// Print Report Total					lsPrice =						formatDollarAmt(getTotItemPrice().toString());					this.caRpt.print(lsPrice, NUM_62, NUM_15);					// Print Credit Card Fee					this.caRpt.nextLine();					lsPrice = formatDollarAmt(lsCreditPrice.toString());					this.caRpt.print(lsPrice, NUM_62, NUM_15);					// Print Credit Card Fee Message					this.caRpt.nextLine();					this.caRpt.print(						CREDIT_CARD_MSG,						NUM_60,						CREDIT_CARD_MSG.length());				}			}		}		if (!lbCreditFeeIncMsgIndi)		{			// Format & Print Total Item Price			String lsTotal = getTotItemPrice().toString();			String lsTotItemPrices = formatDollarAmt(lsTotal);			this.caRpt.print(lsTotItemPrices, NUM_62, NUM_15);		}	}		/**	 * Print the report header.	 *//* &GenTimeLagReport.generateTimeLagHdr& */	private void generateTimeLagHdr()	{		Vector lvHeader = new Vector();		Vector lvTable = new Vector();		generateHeader(lvHeader, lvTable);	}		/**	 * Build Vector of Item Desc Prices	 * 	 * @return Vector	 *//* &GenTimeLagReport.getItemDescPrices& */	public Vector getItemDescPrices()	{		String lsItemPrice = "";		String lsItemPricePadded = "";		Dollar laTotalFees = new Dollar("0.00");		Vector lvHoldData = new Vector();		Vector lvReturn = new Vector();		FeesData laFeesData = new FeesData();		if (cvFeesData != null || cvFeesData.size() != 0)		{			for (int i = 0; i < cvFeesData.size(); i++)			{				// Add data to vector				laFeesData = (FeesData) cvFeesData.elementAt(i);				// Case the record				lvHoldData.addElement(laFeesData.getAcctItmCd());				// Get account code				lvHoldData.addElement(laFeesData.getDesc());				// Get account code description				lvHoldData.addElement(					String.valueOf(laFeesData.getItmQty()));				// Get quantity				// Pad item price to 13 characters and add to vector				lsItemPrice = laFeesData.getItemPrice().toString();				// Get the item price				lsItemPricePadded = formatDollarAmt(lsItemPrice);				// Padd fees amount to 13 chars				lvHoldData.addElement(lsItemPricePadded);				// Add string to vector				// Add data to the returning vector				lvReturn.addElement(lvHoldData);				// Clear vector for new data				lvHoldData = new Vector();				// Accumulate prices				laTotalFees =					laTotalFees.add(laFeesData.getItemPrice());				// Accumulate total fees			} // end for loop		} // end if		setTotItemPrice(laTotalFees);		return lvReturn;	}		/**	 * Return value of TotItemPrice 	 * 	 * @return Dollar	 *//* &GenTimeLagReport.getTotItemPrice& */	public Dollar getTotItemPrice()	{		return caTotItemPrice;	}		/**	 * Starts the application.	 * 	 *  			****  N O T E  !!!  ****	 * This method is executed for testing purpose only 	 * 	 * @param aarrArgs String[] an array of command-line arguments	 *//* &GenTimeLagReport.main& */	public static void main(String[] aarrArgs)	{		// Instantiating a new Report Class 		ReportProperties laRptProps =			new ReportProperties(csReportNumber);		GenTimeLagReport laGPR =			new GenTimeLagReport(csReportName, laRptProps);		// Generating Demo data to display.		String lsQuery = "dummy input statement";		Vector lvQueryResults = laGPR.queryData(lsQuery);		laGPR.formatReport(lvQueryResults);		//Writing the Formatted String onto a local file		File laOutputFile;		FileOutputStream laFout;		PrintStream laPout = null;		try		{			laOutputFile = new File(csFilePathLocation);			laFout = new FileOutputStream(laOutputFile);			laPout = new PrintStream(laFout);		}		catch (IOException aeIOEx)		{			System.err.println(				"Exception occurred in main() of"					+ " com.txdot.isd.rts.client.general.ui.RTSDialogBox");			aeIOEx.printStackTrace(System.out);		}		laPout.print(laGPR.caRpt.getReport().toString());		laPout.close();		//Display the report		try		{			FrmPreviewReport laFrmPreviewReport;			laFrmPreviewReport =				new FrmPreviewReport(csFilePathLocation);			laFrmPreviewReport.setModal(true);			laFrmPreviewReport.addWindowListener(new WindowAdapter()			{				public void windowClosing(WindowEvent aaWE)				{					System.exit(0);				};			});			laFrmPreviewReport.show();			laFrmPreviewReport.setVisibleRTS(true);			// One way to Print Report.			// Process p = Runtime.getRuntime().exec("cmd.exe /c			// copy c:\\TitlePkgRpt.txt prn");		}		catch (Throwable aeEx)		{			System.err.println(				"Exception occurred in main() of"					+ " com.txdot.isd.rts.client.general.ui.RTSDialogBox");			aeEx.printStackTrace(System.out);		}	}		/**	 * Print Time Lag Column Headings 	 *//* &GenTimeLagReport.printColumnHeadings& */	private void printColumnHeadings()	{		caRpt.nextLine();		// Print column headings		// defect 6945  / 7147 		// comment out acctcd column header / align columns		// cRpt.print(ACCTCODETEXT, NUM_05,		// cReceiptTemplate.charLength(ACCTCODETEXT));		// cRpt.print(DESCRIPTIONTEXT, NUM_20,		// cReceiptTemplate.charLength(DESCRIPTIONTEXT));		// cRpt.print(QUANTITY, NUM_55,		// cReceiptTemplate.charLength(QUANTITY));		// cRpt.print(ITEMPRICETEXT, NUM_72,		// cReceiptTemplate.charLength(ITEMPRICETEXT));		caRpt.print(			DESCRIPTIONTEXT,			NUM_05,			caReceiptTemplate.charLength(DESCRIPTIONTEXT));		caRpt.print(			QUANTITY,			NUM_47,			caReceiptTemplate.charLength(QUANTITY));		caRpt.print(			ITEMPRICETEXT,			NUM_67,			caReceiptTemplate.charLength(ITEMPRICETEXT));		caRpt.nextLine();		caRpt.nextLine();		// end defect 6945 / 7147 	}		/**	 * Print Time Lag Item Details	 * 	 * @param avItemCdData Vector	 *//* &GenTimeLagReport.printItemCodes& */	private void printItemCodes(Vector avItemCdData)	{		String lsItemData = "";		Vector lvItemData = new Vector();		int liTotalNumOfLines = getNoOfDetailLines();		int liNumOfLines = caRpt.getCurrX();		for (int i = 0; i < avItemCdData.size(); i++)		{			lvItemData = (Vector) avItemCdData.elementAt(i);			if (liNumOfLines >= liTotalNumOfLines)			{				generateFooter();				generateTimeLagHdr();				printTransId();				printColumnHeadings();				liTotalNumOfLines = getNoOfDetailLines();				liNumOfLines = caRpt.getCurrX();			}			// defect 6945, 7147   			// comment out acctcd, moved others 			//        lsItemData = (String) lvItemData.elementAt(0);			//        cRpt.print(lsItemData, NUM_05, cReceiptTemplate.			//			charLength(lsItemData));			//          			//        lsItemData = (String) lvItemData.elementAt(1);			//        cRpt.print(lsItemData, NUM_20, cReceiptTemplate.			//			charLength(lsItemData));			//			//        lsItemData = (String) lvItemData.elementAt(2);			//        cRpt.rightAlign(lsItemData, NUM_55, NUM_08);			//        lsItemData = (String) lvItemData.elementAt(3);			//        cRpt.print(lsItemData, NUM_65, cReceiptTemplate.			//			charLength(lsItemData));			// Account Item Code Description 			lsItemData = (String) lvItemData.elementAt(1);			caRpt.print(				lsItemData,				NUM_05,				caReceiptTemplate.charLength(lsItemData));			// Quantity 			lsItemData = (String) lvItemData.elementAt(2);			caRpt.rightAlign(lsItemData, NUM_47, NUM_08);			// Item Price 			lsItemData = (String) lvItemData.elementAt(3);			caRpt.print(lsItemData, NUM_62, NUM_15);			// end defect 6945, 7147 			caRpt.nextLine();			liNumOfLines = caRpt.getCurrX();		} // end for loop, i	}		/**	 * Print Time Lag Reason at the end of the report	 *//* &GenTimeLagReport.printReason& */	private void printReason()	{		// Get Reason 		String lsReason = caRegFeeData.getReason();		// Print TransId, column 5		caRpt.blankLines(2);		caRpt.print(			REASONTEXT,			NUM_05,			caReceiptTemplate.charLength(REASONTEXT));		caRpt.println(lsReason);		caRpt.blankLines(1);	}		/**	 * Print the transaction Id.	 *//* &GenTimeLagReport.printTransId& */	private void printTransId()	{		// Get TransId		String lsTransId = caCompleteTrans.getTransId();		// Print TransId, column 5		caRpt.nextLine();		caRpt.print(			TRANSIDTEXT,			NUM_05,			caReceiptTemplate.charLength(TRANSIDTEXT));		caRpt.print("  " + lsTransId);		caRpt.blankLines(2);	}		/**	 * The queryData() loads the TitlePackageReportData data object	 * into a vector avResults.	 * This is passed to the formatReport() method.	 *	 * 			****  N O T E  !!!  ****	 * This method is executed for testing purpose only	 *	 * @param asQuery String: Dummy string	 * @return Vector: Records	 *//* &GenTimeLagReport.queryData& */	public Vector queryData(String asQuery)	{		// Generating vector for data object of  to display.		Vector avResults = new Vector();		/*		    String lsAcctItmCd = null;		    String lsBatchNo = "";		    Dollar ldItmPrice1 = new Dollar(123.78);		    Dollar ldItmPrice2 = new Dollar(58.80);		    Dollar ldItmPrice3 = new Dollar(647.78);		    int liOfcIssuanceNo = 0;		    int liTransAmDate = 0;		    String lsTransCd = null;		    int liTransTime = 0;		    int liTransWsId = 0;		    int liVoid = 0;					// test records		   	TitlePackageReportData dataline = 		   		new TitlePackageReportData();			   	dataline.setAcctItmCd("TITLE");			  	dataline.setBatchNo("16110035150");			 	dataline.setItmPrice(ldItmPrice1);			dataline.setOfcIssuanceNo(161);			dataline.setTransAMDate(37150);			dataline.setTransCd("RENEW");			dataline.setTransTime(80934);			dataline.setTransWsId(100);			dataline.setVoidedTransIndi(00);			avResults.addElement(dataline);			for (int i = 0;i < 80; i++)			{			avResults.addElement(dataline);			}					    TitlePackageReportData dataline1 = 		    	new TitlePackageReportData();		    dataline1.setAcctItmCd("TITLE");		    dataline1.setBatchNo("16110035151");		    dataline1.setItmPrice(ldItmPrice2);		    dataline1.setOfcIssuanceNo(161);		    dataline1.setTransAMDate(37150);		    dataline1.setTransCd("RENEW");		    dataline1.setTransTime(90934);		    dataline1.setTransWsId(100);		    dataline1.setVoidedTransIndi(1);		    avResults.addElement(dataline1);				   	TitlePackageReportData dataline2 = 		   		new TitlePackageReportData();    		    dataline2.setAcctItmCd("TITLE");		    dataline2.setBatchNo("16110035151");		    dataline2.setItmPrice(ldItmPrice3);		    dataline2.setOfcIssuanceNo(161);		    dataline2.setTransAMDate(37150);		    dataline2.setTransCd("RENEW");		    dataline2.setTransTime(80934);		    dataline2.setTransWsId(100);		    dataline2.setVoidedTransIndi(1);		    avResults.addElement(dataline2);				    TitlePackageReportData dataline3 = 		    	new TitlePackageReportData();		    dataline3.setAcctItmCd("TITLE");		    dataline3.setBatchNo("16110035152");		    dataline3.setItmPrice(ldItmPrice2);		    dataline3.setOfcIssuanceNo(220);		    dataline3.setTransAMDate(37150);		    dataline3.setTransCd("TITLE");		    dataline3.setTransTime(80934);		    dataline3.setTransWsId(100);		    dataline3.setVoidedTransIndi(1);		    avResults.addElement(dataline3);				   	TitlePackageReportData dataline4 =		   		new TitlePackageReportData();			dataline4.setAcctItmCd("TITLE");		    dataline4.setBatchNo("16110035152");		    dataline4.setItmPrice(ldItmPrice2);		    dataline4.setOfcIssuanceNo(220);		    dataline4.setTransAMDate(37150);		    dataline4.setTransCd("RENEW");		    dataline4.setTransTime(80934);		    dataline4.setTransWsId(200);		    dataline4.setVoidedTransIndi(1);		    avResults.addElement(dataline4);				    TitlePackageReportData dataline5 = 		    	new TitlePackageReportData();		    dataline5.setAcctItmCd("TITLE");		    dataline5.setBatchNo("16120035152");		    dataline5.setItmPrice(ldItmPrice2);		    dataline5.setOfcIssuanceNo(161);		    dataline5.setTransAMDate(37150);		    dataline5.setTransCd("RENEW");		    dataline5.setTransTime(90934);		    dataline5.setTransWsId(200);		    dataline5.setVoidedTransIndi(1);		    avResults.addElement(dataline5);				    TitlePackageReportData dataline6 = 		    	new TitlePackageReportData();		    dataline6.setAcctItmCd("TITLE");		    dataline6.setBatchNo("16120035152");		    dataline6.setItmPrice(ldItmPrice2);		    dataline6.setOfcIssuanceNo(161);		    dataline6.setTransAMDate(37150);		    dataline6.setTransCd("RENEW");		    dataline6.setTransTime(90934);		    dataline6.setTransWsId(200);		    dataline6.setVoidedTransIndi(1);		    avResults.addElement(dataline6);				    TitlePackageReportData dataline7 =		    	new TitlePackageReportData();		    dataline7.setAcctItmCd("TITLE");		    dataline7.setBatchNo("16120035152");		    dataline7.setItmPrice(ldItmPrice2);		    dataline7.setOfcIssuanceNo(161);		    dataline7.setTransAMDate(37150);		    dataline7.setTransCd("RENEW");		    dataline7.setTransTime(90934);		    dataline7.setTransWsId(200);		    dataline7.setVoidedTransIndi(1);		    avResults.addElement(dataline7);				*/		return avResults;		/*    		try		    {		    // Build the arrays, add data elements to the array		    while (!(lsBatchNo == null))		        {		        lsAcctItmCd = dataline.getAcctItmCd();		        lsBatchNo = dataline.getBatchNo();		        ldItmPrice = dataline.getItmPrice();		        liOfcIssuanceNo = dataline.getOfcIssuanceNo();		        liTransAmDate = dataline.getTransAMDate();		        lsTransCd = dataline.getTransCd();		        liTransTime = dataline.getTransTime();		        liTransWsId = dataline.getTransWsId();		        liVoid = dataline.getVoidedTransIndi();				        // Build the transaction Id		        String liTransactionId =		            String.valueOf(liOfcIssuanceNo)		                + String.valueOf(liTransWsId)		                + String.valueOf(liTransAmDate)		                + String.valueOf(liTransTime);				        //        if (!(lsBatchNo == null))		        //            {		        avResults.addElement(dataline);		        //        }		        int t = avResults.size();		    } // end while		} // end try block		catch (Throwable exception)		    {		    System.err.println(		        "Exception occurred in queryData() of com.txdot.isd.rts.		        services.reports.GenTitlePackageReport");		    exception.printStackTrace(System.out);		}		*/	}		/**	 * Assign TotItemPrice 	 * 	 * @param aaNewTotItemPrice Dollar	 *//* &GenTimeLagReport.setTotItemPrice& */	public void setTotItemPrice(Dollar aaNewTotItemPrice)	{		caTotItemPrice = aaNewTotItemPrice;	}}/* #GenTimeLagReport# */