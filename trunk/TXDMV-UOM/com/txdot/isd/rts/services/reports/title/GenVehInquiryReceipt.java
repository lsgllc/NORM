package com.txdot.isd.rts.services.reports.title;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.CreditCardFeesCache;
import com.txdot.isd.rts.services.cache.DocumentTypesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * GenVehInquiryReceipt.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Rue		09/01/2001	Methods created
 * J Rue      	09/19/2001	Added comments
 * J Rue        04/16/2002 	Added variable ciTotInqFeePos to 
 *							set print position
 * J Rue		5/30/2002	Defect 4149, set condition check to
 * 							print Registration fee even
 *							if amount = 0.00
 * S Govindappa 07/10/2002  Merged PCR25 changes for credit card fee
 * 							message display on receipt
 * S Govindappa 07/11/2002  Fixed CQU100004451, by putting null
 * 							point check for CustActlRegFee in 
 * 							printSalesTtlIssueDtDocNoData method
 * MAbs			08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * K Salvi		10/15/2002	CQU100004869 fixed - added check to not
 * 							print credit fee when a fee is not 
 * 							assessed.
 * Ray Rowehl	05/08/2003	Fix receipt to handle Canceled Plates
 *							modify parseRegisInfo and printLienHold2
 *							defect 6068
 * Ray Rowehl	05/20/2003	Fix to always print some customer name
 * 							for headquarters
 *							modify printLienHold1()
 *							defect 6135
 * K Harrell	12/04/2003	Incorrect dates printed for Lienholder2
 * 							& Lienholder3. made javadoc consistent
 * 							with TxDOT standards; removed commented 
 * 							import statements; rearranged imports.
 *							in modified classes, added brackets for 
 *							else statements, removed references to
 *							RTSDate
 *							modified printLienHold1(),
 *							printLienHold2(), printLienHold3() 
 * 							defect 6716.  Ver 5.1.5 Fix 2
 * S Johnston	03/16/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify main(), queryData()
 *							defect 7896 Ver 5.2.3
 * K Harrell	05/19/2005 	refactoring of printTrlvTrlrLngth() to 
 * 							printTrvlTrlrLngth() in ReceiptTemplate
 * 							plus add'l Java 1.4 Work 
 * 							defect 7562 Ver 5.2.3
 * K Harrell	05/22/2005	Java 1.4 Work; Constant rename from Receipt
 * 							Template. 
 * 							defect 7896 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3 
 * Min Wang		10/11/2006	New Requirement for handling plate age 
 * 							modify parseRegisInfoAcct()
 *							defect 8901 Ver Exempts
 * K Harrell	03/09/2007	Use SystemProperty.isHQ()
 * 							printLienHold1()
 * 							defect 9085 Ver Special Plates                        	 
 * B Hargrove	12/31/2008	Credit Card Fee message is not printing 
 * 							correctly on Inquiry receipt.
 * 							modify printVehPymnt()
 * 							defect 9094 Ver Defect_POS_D
 * K Harrell	03/03/2009	Streamline to use Lienholder Address
 * 							modify parseLienHld1-3() 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	03/27/2009	Check for Null LienholderData (Cancelled Plt)
 * 							modify parseLienHld1-3() 
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	07/03/2009	modify parseLienHld1(),parseLienHld2(),
 * 							 parseLienHld3()
 * 							defect 10112 Ver Defect_POS_F
 * B Hargrove	08/13/2009	Allow printing of new additional fees (Misc
 * 							and new Volunteer Veteran Fund).
 * 							(Note: this defect was moved to POS E also)
 * 							modify printLienHold1() 
 * 							defect 10122 Ver Defect_POS_F
 * Min Wang		10/06/2010	Allow printing of the Max length of price.
 * 							add COL_64, COL_84, COL_87 
 * 							delete COL_87 = 87
 * 							modify printLienHold3(), printTotalFee(),
 * 							printSalesTtlIssueDtDocNoData(),printVehPymnt(),
 * 							printTotalPymnt(),
 * 							defect 10596 Ver 6.6.0
 * Min Wang		10/07/2010	modify SALES_CHAR_LNGTH
 * 							defect 10596 Ver 6.6.0
 * Min Wang		10/15/2010	Reformat the receipt.
 * 							delect COL_84
 * 							modify SALES_CHAR_LNGTH,
 * 							printLienHold3(), printTotalFee(),
 * 							printSalesTtlIssueDtDocNoData(),printVehPymnt(),
 * 							printTotalPymnt(),
 * 							defect 10596 Ver 6.6.0
 * Min Wang		10/19/2010	Show the change due on the receipt when
 * 							there is change due and the payment is more 
 * 							than one line item.
 * 							defect 10596 Ver 6.6.0
 * B Hargrove	08/04/2011	Allow printing of new Parks and Wildlife Fund).
 * 							modify printLienHold1() 
 * 							defect 10965 Ver 6.8.1
 * ---------------------------------------------------------------------
 */
/**
 *
 * This class prints the Vehicle Inquiry Receipt
 *
 * @version	6.8.1			08/04/2011 
 * @author	Jeff Rue
 * <br>Creation Date:		12/18/2001 11:13:45
 */
public class GenVehInquiryReceipt extends ReceiptTemplate
{
	// boolean
	private boolean cbPymntMade = true;
	private boolean cbPymntTotPrnt = false;
	private boolean cbCreditFeeIncMsgIndi = true;

	// int
	private int ciPymntMaxRecs = 0;
	private int ciPymntIndex = 0;

	// Objects
	private CompleteTransactionData caTransData =
		new CompleteTransactionData();
	private Dollar caChangeDue = new Dollar(0);
	private Dollar caTotalFees = new Dollar("0.00");
	private Dollar caTotalPymnt = new Dollar("0.00");
	private FeesData caFeesData = new FeesData();
	private GenTitleReceipt caGentitleRcpt = new GenTitleReceipt();
	private MFVehicleData caMFVehData = new MFVehicleData();
	private OwnerData caOwnrData = new OwnerData();
	private RegFeesData caRegFeeData = new RegFeesData();
	private RegistrationData caRegisData = new RegistrationData();
	private TitleData caTitleData = new TitleData();
	private TransactionPaymentData caPymntData =
		new TransactionPaymentData();
	private VehicleData caVehicleData = new VehicleData();

	// String
	private String csPrevPaymntDesc = "";

	// Vector
	private Vector cvPymntData = new Vector();
	private Vector cvPymntDataItms = new Vector();
	private Vector cvPymntData2 = new Vector();

	// Constants
	private final static String REGISTRATIONFEESPAIDTEXT =
		"REGISTRATION FEES PAID";
	private final static String SALESTAXINFORTEXT =
		"SALES TAX INFORMATION";
	private final static String TITLEISSUEDDATETEXT =
		"TITLE ISSUE DATE: ";
	private final static String DOCTYPETEXT = "DOCUMENT TYPE: ";
	private final static String REGISTRATION$TEXT = "REGISTRATION $";
	private final static String SALESPRICRTEXT = "SALES PRICE   ";
	private final static String TRADEINALLOWANCETEXT =
		"TRADE IN ALLOWANCE";
	private final static String SALESTAXPAIDTEXT = "SALES TAX PAID";
	private final static String DOLLAR$TEXT = "$";
	private final static String CUSTOMERNAMETEXT = "CUSTOMER NAME: ";
	private final static String METHODOFPYMNTEXT =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	private final static String DATE = "DATE: ";
	private final static String TOTALAMTPAIDTEXT = "TOTAL AMOUNT PAID";

	private final static int COL_05 = 5;
	private final static int COL_23 = 23;
	private final static int COL_25 = 25;
	private final static int COL_37 = 37;
	private final static int COL_41 = 41;
	private final static int COL_43 = 43;
	private final static int COL_61 = 61;
	private final static int COL_69 = 69;
	private final static int COL_71 = 71;
	private final static int COL_82 = 82;
	private final static int COL_85 = 85;
	// defect 10596
	//private final static int COL_87 = 87;
	//private final static int COL_88 = 88;
	// end defect 10596

	private final static int NUM_11 = 1;
	// defect 10596
	//private final static int SALES_CHAR_LNGTH = 9;
	private final static int SALES_CHAR_LNGTH = 14;
	// end defect 10596
	private final static int REGISTRATION_FEE = 6;
	//private final static int TOT_INQ_FEE_POS = COL_85;
	
	// defect 10596
	private final static int COL_64 = 64;
	private final static int COL_87 = 87;
	// end defect 10596

	/**
	 * GenVehInquiryReceipt constructor
	 * 
	 */
	public GenVehInquiryReceipt()
	{
		super();
	}
	/**
	 * GenVehInquiryReceipt constructor
	 * 
	 * @param asRcptString String
	 * @param asRcptProperties ReceiptProperties
	 */
	public GenVehInquiryReceipt(
		String asRcptString,
		ReceiptProperties asRcptProperties)
	{
		super(asRcptString, asRcptProperties);
	}
	/**
	 * formatReceipt
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

		// ***************  LOAD PAYMENT VECTOR  ***************

		cvPymntData = (Vector) buildPaymentVector(avResults);

		try
		{
			generateReceiptHeader(avResults);
		} // end try box
		catch (RTSException aeRTSEx)
		{
			System.out.println(
				"com.txdot.isd.rts.services.receipt.title.formatReceipt "
					+ aeRTSEx);
		}

		// ***************  VEHICLE INFORMATION  ***************
		lvRcptInfo = parseRegisInfoAcct();
		printVehDataAcct(lvRcptInfo);

		// ***************  VEHICLE NOTATION  ***************
		printVehInqVehNotation();

		// ***************  FEES INFORMATION  ***************
		printSalesTtlIssueDtDocNoData();
		caRpt.blankLines(1);

		// ***************  LIENHOLDER1/FEES  ***************
		printLienHold1(caRegFeeData.getVectFees());

		// ***************  LIENHOLDER2/PAYMENT  ***************
		printLienHold2();

		// ***************  LIENHOLDER3  ***************
		printLienHold3();
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
	 * Get the doc type for the cache table.
	 * 
	 * @param  aiDocTypeCd int
	 * @return String
	 */
	protected String getCacheDocTypeDesc(int aiDocTypeCd)
	{
		DocumentTypesData laDocTypeData =
			DocumentTypesCache.getDocType(aiDocTypeCd);

		if (laDocTypeData != null)
		{
			return laDocTypeData.getDocTypeCdDesc();
		}
		else
		{
			return "";
		}
	}
	/**
	 * Get Total Fees
	 * 
	 * @return Dollar
	 */
	private Dollar getTotalFees()
	{
		return caTotalFees;
	}
	/**
	 * Get Total Payment
	 * 
	 * @return Dollar
	 */
	private Dollar getTotalPymnt()
	{
		return caTotalPymnt;
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
			GenVehInquiryReceipt laGPR =
				new GenVehInquiryReceipt(
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
			//Process p = Runtime.getRuntime().exec("cmd.exe
			// /c copy c:\\QuickCtcRpt.txt prn");

		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() "
					+ "of com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}
	/**
	 * return the lien holder 1 data
	 * 
	 * @return Vector
	 */
	private Vector parseLienHld1()
	{
		int liMaxLien1Line = 6;
		Vector lvLienHld1 = new Vector();

		LienholderData laLienData =
			(LienholderData) caTitleData.getLienholderData(
				TitleConstant.LIENHLDR1);

		if (laLienData.getLienDate() != 0) // Lienholder date
		{

			lvLienHld1.addElement(
				String.valueOf(laLienData.getLienDate()));
		}
		else
		{
			lvLienHld1.addElement("");
		}

		// defect 10112 
		if (laLienData.isPopulated())
		{
			lvLienHld1.addAll(laLienData.getNameAddressVector());
		}
		// end defect 10112 

		for (int i = lvLienHld1.size(); i < liMaxLien1Line; i++)
		{
			lvLienHld1.addElement("");
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
		int liMaxLien2Line = 3;
		Vector lvLienHld2 = new Vector();

		LienholderData laLienData =
			(LienholderData) caTitleData.getLienholderData(
				TitleConstant.LIENHLDR2);

		if (laLienData.getLienDate() == 0) // Lienholder date
		{
			lvLienHld2.addElement("");
		}
		else
		{
			lvLienHld2.addElement(
				String.valueOf(laLienData.getLienDate()));
		}

		// defect 10112 
		if (laLienData.isPopulated())
		{
			lvLienHld2.addAll(laLienData.getNameAddressVector());
		}
		// end defect 10112 

		for (int i = lvLienHld2.size(); i < liMaxLien2Line; i++)
		{
			lvLienHld2.addElement("");
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
		int liMaxLien3Line = 3;
		Vector lvLienHld3 = new Vector();

		LienholderData laLienData =
			(LienholderData) caTitleData.getLienholderData(
				TitleConstant.LIENHLDR3);

		if (laLienData.getLienDate() == 0) // Lienholder date
		{
			lvLienHld3.addElement("");
		}
		else
		{
			lvLienHld3.addElement(
				String.valueOf(laLienData.getLienDate()));
		}

		// defect 10112 
		if (laLienData.isPopulated())
		{
			lvLienHld3.addAll(laLienData.getNameAddressVector());
		}
		// end defect 10112 

		for (int i = lvLienHld3.size(); i < liMaxLien3Line; i++)
		{
			lvLienHld3.addElement("");
		}

		return lvLienHld3;
	}
	
	/**
	 * return the fees description and item price in a vector
	 * 
	 * @return Vector
	 */
	private Vector parsePymntData()
	{
		Vector lvHoldData = new Vector();
		Dollar laZeroDollar = new Dollar(0);

		int liPymntMaxSize = 0;
		if (cvPymntData != null)
		{
			liPymntMaxSize = cvPymntData.size();
		}
		// Set total number of fees

		if (liPymntMaxSize > 0)
		{
			for (int liPymntIndex = 0;
				liPymntIndex < liPymntMaxSize;
				liPymntIndex++)
			{
				caPymntData =
					(TransactionPaymentData) cvPymntData.elementAt(
						liPymntIndex);
				lvHoldData.addElement(caPymntData.getPymntType());
				lvHoldData.addElement(caPymntData.getPymntTypeAmt());
				lvHoldData.addElement(caPymntData.getPymntCkNo());
				cvPymntDataItms.addElement(lvHoldData);
				lvHoldData = new Vector();
				if (caPymntData.getChngDue() != null
					&& !caPymntData.getChngDue().equals(laZeroDollar))
				{
					caChangeDue = caPymntData.getChngDue();
				}
			}
		}

		return cvPymntDataItms;
	}
	/**
	 * Parse Regis Info so it can be used on receipt.
	 * Note special handling for canceled plates.
	 * 
	 * @return Vector
	 */
	private Vector parseRegisInfoAcct()
	{

		Vector lvVehData = new Vector();

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

		String lsPrevExpMoYr =
			String.valueOf(caRegisData.getPrevExpMo())
				+ "/"
				+ String.valueOf(caRegisData.getPrevExpYr());

		String lsPrevCityState =
			caTitleData.getPrevOwnrCity()
				+ ", "
				+ caTitleData.getPrevOwnrState();

		if (caTitleData.getPrevOwnrCity() == null
			|| caTitleData.getPrevOwnrCity().equals(""))
			lsPrevCityState = caTitleData.getPrevOwnrState();

		// Add data to the vector
		// defect 6068
		// use canceled Vin for canceled plates
		if (caRegisData.getCancPltIndi() > 0)
		{
			lvVehData.addElement(caRegisData.getCancPltVin());
		}
		else
		{
			lvVehData.addElement(caVehicleData.getVin());
		}
		// end defect 6068

		lvVehData.addElement(caVehicleData.getVehClassCd());
		lvVehData.addElement(lsYrMk);
		lvVehData.addElement(caVehicleData.getVehModl());
		lvVehData.addElement(caVehicleData.getVehBdyType());
		lvVehData.addElement(caTitleData.getVehUnitNo());
		lvVehData.addElement(
			new Integer(caRegisData.getResComptCntyNo()));
		lvVehData.addElement(
			new Integer(caVehicleData.getVehEmptyWt()));
		lvVehData.addElement(
			new Integer(caRegisData.getVehCaryngCap()));
		lvVehData.addElement(new Integer(caRegisData.getVehGrossWt()));
		lvVehData.addElement(caVehicleData.getVehTon());
		lvVehData.addElement(caVehicleData.getTrlrType());
		lvVehData.addElement(caVehicleData.getVehBdyVin());
		lvVehData.addElement(new Integer(caVehicleData.getVehLngth()));
		lvVehData.addElement(caRegisData.getTireTypeCd());
		lvVehData.addElement(new Integer(caRegisData.getRegIssueDt()));
		lvVehData.addElement(caVehicleData.getVehOdmtrReadng());
		lvVehData.addElement(caVehicleData.getVehOdmtrBrnd());
		lvVehData.addElement(caRegisData.getPrevPltNo());
		lvVehData.addElement(lsPrevExpMoYr);
		lvVehData.addElement(caTitleData.getPrevOwnrName());
		lvVehData.addElement(lsPrevCityState);
		// defect 8901
		// lvVehData.addElement(new Integer(caRegisData.getRegPltAge()));
		lvVehData.addElement(
			new Integer(caRegisData.getRegPltAge(false)));
		// end defect 8901
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
	 * Set printer pointer to correct print row.
	 *
	 * @param asCustName String
	 * @param aiPrntLoc int
	 */
	public void printCustName(String asCustName, int aiPrntLoc)
	{
		int liCustomerRow = 32;

		for (int i = caRpt.getCurrX(); i < liCustomerRow; i++)
		{
			caRpt.nextLine();
		}

		caRpt.print(
			CUSTOMERNAMETEXT,
			aiPrntLoc,
			charLength(CUSTOMERNAMETEXT));
		caRpt.print(asCustName);
	}
	/**
	 * printLienHold1
	 *  
	 * @param avFeesData Vector
	 */
	protected void printLienHold1(Vector avFeesData)
	{
		// Set total number of fees   
		int liFeesMaxSize = 0;
		if (avFeesData != null)
		{
			liFeesMaxSize = avFeesData.size();
		}
		int liFeesIndex = 0; // Set Fees index to first element
		String lsLienHldData = "";
		Dollar laSetZero = new Dollar("0.00");
		int liDate = 0;
		int liItem1 = 1;
		int liItem2 = 2;
		int liItem3 = 3;
		int liItem4 = 4;
		int liItem5 = 5;
		Vector lvLienHld1 = parseLienHld1();
		try
		{
			int liFeesRow = 45;
			for (int i = caRpt.getCurrX(); i < liFeesRow; i++)
			{
				caRpt.blankLines(1);
			}
			// get the Fees assessed
			//        lbMoreFees = parseFeesData(avFeesData);
			// ******** Print LienHolder1 Header, Date and Customer Name
			// -- Line 1
			caRpt.print(FIRSTLIENTXT, COL_05, charLength(FIRSTLIENTXT));
			lsLienHldData = (String) lvLienHld1.elementAt(liDate);
			String lsDate = "";
			if (lsLienHldData != null && !lsLienHldData.equals(""))
			{
				caRpt.print(DATE, COL_24, charLength(DATE));
				lsDate =
					convertDtToStr(Integer.parseInt(lsLienHldData));
				caRpt.print(lsDate);
			}
			////////////////////////////////////////////////////////////
			// defect 6135
			// if this is HeadQuarters, assign a customer name
			// some code copied from REG002 where it was updated by
			// defect 6065
			// defect 9085 
			//if (CommonValidations
			//	.isHq(SystemProperty.getOfficeIssuanceNo()))
			if (SystemProperty.isHQ())
			{
				String lsCustName = "";
				if (caTransData.getVehicleInfo() != null)
				{
					if (caTransData.getVehicleInfo().getOwnerData()
						!= null
						&& caTransData
							.getVehicleInfo()
							.getOwnerData()
							.getName1()
							!= null
						&& caTransData
							.getVehicleInfo()
							.getOwnerData()
							.getName1()
							.length()
							> 0)
					{
						lsCustName =
							caTransData
								.getVehicleInfo()
								.getOwnerData()
								.getName1();
					}
					else
					{
						if (caTransData.getVehicleInfo().getRegData()
							!= null
							&& caTransData
								.getVehicleInfo()
								.getRegData()
								.getRecpntName()
								!= null
							&& caTransData
								.getVehicleInfo()
								.getRegData()
								.getRecpntName()
								.length()
								> 0)
						{
							lsCustName =
								caTransData
									.getVehicleInfo()
									.getRegData()
									.getRecpntName();
						}
						else
						{
							if (caTransData
								.getVehicleInfo()
								.getRegData()
								!= null
								&& caTransData
									.getVehicleInfo()
									.getRegData()
									.getCancPltIndi()
									> 0)
							{
								lsCustName = "** Canceled Plate **";
							}
						}
						caTransData.setCustName(lsCustName);
					}
				}
			}
			// end defect 6135
			// end defect 9085 
			//////////////////////////////////////////////////////////////////// 
			printCustName(caTransData.getCustName(), COL_50);

			caRpt.blankLines(1);
			// **********************************************************************************
			// *********** Print LienHolder1 Name 1, Fees Assessed Header -- Line 2
			if (lvLienHld1.elementAt(liItem1) != null)
			{
				lsLienHldData = (String) lvLienHld1.elementAt(liItem1);
				caRpt.print(
					lsLienHldData,
					COL_05,
					charLength(lsLienHldData));
			}
			caRpt.print(
				FEES_ASSESSED_TEXT,
				COL_50,
				charLength(FEES_ASSESSED_TEXT));

			caRpt.blankLines(1);
			// **********************************************************************************
			// *********** Print LienHolder1 Name 2 or Street 1, Fees Data -- line 3
			if (lvLienHld1.elementAt(liItem2) != null)
			{
				lsLienHldData = (String) lvLienHld1.elementAt(liItem2);
				caRpt.print(
					lsLienHldData,
					COL_05,
					charLength(lsLienHldData));
			}
			// *****  Print Fees Assessed
			if (liFeesMaxSize > 0)
			{
				caFeesData =
					(FeesData) avFeesData.elementAt(liFeesIndex);
				printVehInqFees(
					caFeesData.getDesc(),
					caFeesData.getItemPrice());
			}
			else
			{
				caRpt.print(
					caRpt.blankSpaces(1),
					COL_50,
					charLength(caRpt.blankSpaces(1)));
			}
			caRpt.blankLines(1);

			// *********************************************************
			// *** Print LienHolder1 Street 1 or 2, Fees Data  -- Line 4
			if (!(lvLienHld1.elementAt(liItem3) == null))
			{
				lsLienHldData = (String) lvLienHld1.elementAt(liItem3);
				caRpt.print(
					lsLienHldData,
					COL_05,
					charLength(lsLienHldData));
			}
			// *****  Print blank line
			// cRpt.print(cRpt.blankSpaces(1), COL_50, charLength(
			// cRpt.blankSpaces(1)));
			// Move down 1 lines
			// *****  Print Fees Assessed
			if (liFeesMaxSize > 1)
			{
				caFeesData =
					(FeesData) avFeesData.elementAt(liFeesIndex + 1);
				printVehInqFees(
					caFeesData.getDesc(),
					caFeesData.getItemPrice());
			}
			else
			{
				caRpt.print(
					caRpt.blankSpaces(1),
					COL_50,
					charLength(caRpt.blankSpaces(1)));
			}
			caRpt.blankLines(1);
			
			
			// *********************************************************
			// Print LienHolder1 Street 2 or City/State/zip, Fees Data
			// -- Line 5
			if (!(lvLienHld1.elementAt(liItem4) == null))
			{
				lsLienHldData = (String) lvLienHld1.elementAt(liItem4);
				caRpt.print(
					lsLienHldData,
					COL_05,
					charLength(lsLienHldData));
			}
			
			// defect 10122
			// May now have 2 additional fees on Inquiry (Misc & Vet Fund)
			if (liFeesMaxSize > 2)
			{
				caFeesData =
					(FeesData) avFeesData.elementAt(liFeesIndex + 2);
				printVehInqFees(
					caFeesData.getDesc(),
					caFeesData.getItemPrice());
			}
			else
			{
				caRpt.print(
					caRpt.blankSpaces(1),
					COL_50,
					charLength(caRpt.blankSpaces(1)));
			}
			caRpt.blankLines(1);
			// end defect 10122
			
			// *********************************************************
			// Print LienHolder1 City/State/Zip, Fees Data  -- Line 6
			if (!(lvLienHld1.elementAt(liItem5) == null))
			{
				lsLienHldData = (String) lvLienHld1.elementAt(liItem5);
				caRpt.print(
					lsLienHldData,
					COL_05,
					charLength(lsLienHldData));
			}
			
			// defect 10122
			// May now have 2 additional fees on Inquiry (Misc & Vet Fund)
			if (liFeesMaxSize > 3)
			{
				caFeesData =
					(FeesData) avFeesData.elementAt(liFeesIndex + 3);
				printVehInqFees(
					caFeesData.getDesc(),
					caFeesData.getItemPrice());
			}
			else
			{
				caRpt.print(
					caRpt.blankSpaces(1),
					COL_50,
					charLength(caRpt.blankSpaces(1)));
			}
			// end defect 10122
			// defect 10965
			// May now Parks and Wildlife Fund
			caRpt.blankLines(1);
			if (liFeesMaxSize > 4)
			{
				caFeesData =
					(FeesData) avFeesData.elementAt(liFeesIndex + 4);
				printVehInqFees(
					caFeesData.getDesc(),
					caFeesData.getItemPrice());
			}
			else
			{
				caRpt.print(
					caRpt.blankSpaces(1),
					COL_50,
					charLength(caRpt.blankSpaces(1)));
			}
			// end defect 10965
			
			// *****  Print Fees Assessed total
			caRpt.blankLines(1);
			printTotalFee();
			setTotalFees(laSetZero);

			caRpt.blankLines(1);
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTSEx = new RTSException("", aeNPEx);
		}
	}
	/**
	 * Print lienholder 2 information
	 * 
	 */
	protected void printLienHold2()
	{
		String lsLienHldData = "";
		Dollar laSetZero = new Dollar("0.00");
		int liDate = 0;
		int liItem1 = 1;
		int liItem2 = 2;
		Vector lvLienHld2 = parseLienHld2();
		cvPymntData2 = parsePymntData();
		ciPymntMaxRecs = cvPymntData2.size();
		ciPymntIndex = 0;
		try
		{
			int liFeesRow = 50;
			for (int i = caRpt.getCurrX(); i < liFeesRow; i++)
			{
				caRpt.blankLines(1);
			}
			// ******** Print LienHolder2 Header, Date -- Line 1 Method Header
			caRpt.print(
				SECONDLIENTXT,
				COL_05,
				charLength(SECONDLIENTXT));
			lsLienHldData = (String) lvLienHld2.elementAt(liDate);

			// defect 6716
			//  Print Correct Lienholder2 Date
			//			if (!(lsLienHldData == null || lsLienHldData.equals("")))
			//			{
			//			    cRpt.print(DATE, COL_25, charLength(DATE));
			//			    lRTSDate = new RTSDate(Integer.parseInt(lsLienHldData));
			//			    cRpt.print(lRTSDate.toString());
			//			} // end if
			String lsDate = "";
			if (lsLienHldData != null && !lsLienHldData.equals(""))
			{
				caRpt.print(DATE, COL_24, charLength(DATE));
				lsDate =
					convertDtToStr(Integer.parseInt(lsLienHldData));
				caRpt.print(lsDate);
			}
			// end defect 6716    
			if (ciPymntMaxRecs > 0)
			{
				caRpt.print(
					METHODOFPYMNTEXT,
					COL_50,
					charLength(METHODOFPYMNTEXT));
			}
			// defect 6068
			// set the payment made flag to false if there is no payments
			else
			{
				cbPymntMade = false;
				cbPymntTotPrnt = false;
			}
			// end defect 6068

			caRpt.blankLines(1);
			// *********************************************************
			// *********** Print LienHolder2 Name 1,  
			// -- Line 2, payment fess 1
			lsLienHldData = (String) lvLienHld2.elementAt(liItem1);
			if (!(lsLienHldData == null || lsLienHldData.equals("")))
			{
				caRpt.print(
					lsLienHldData,
					COL_05,
					charLength(lsLienHldData));
			}
			if (ciPymntIndex < ciPymntMaxRecs)
			{
				Vector lvPymntItm1 =
					(Vector) cvPymntData2.elementAt(ciPymntIndex);
				String lsPymnt1 = (String) lvPymntItm1.elementAt(0);
				Dollar lsPymnt2 = (Dollar) lvPymntItm1.elementAt(1);
				String lsPymnt3 = (String) lvPymntItm1.elementAt(2);
				// Test if payment amount is 0.00
				int liResult = lsPymnt2.compareTo(laSetZero);
				// Test to print total payment paid 
				if (liResult > 0)
				{
					printVehPymnt(lsPymnt1, lsPymnt2, lsPymnt3);
					csPrevPaymntDesc = lsPymnt1;
					TransactionPaymentData laPaymentData =
						new TransactionPaymentData();
					laPaymentData.setPymntTypeAmt(lsPymnt2);
					//Added for PCR25 Code
					if (!(cbCreditFeeIncMsgIndi
						&& lsPymnt1.equals(CHARGE)
						&& formatCreditCardFeeMessage() != null))
					{
						ciPymntIndex++;
					}
				}
				else
				{
					cbPymntMade = false;
				} // end else
			}

			caRpt.blankLines(1);
			// *********************************************************
			// ************  Print LienHolder2 City/State/Zip, Fees Data
			// -- Line 3 , payment fees 2
			lsLienHldData = (String) lvLienHld2.elementAt(liItem2);
			if (!(lsLienHldData == null || lsLienHldData.equals("")))
			{
				caRpt.print(
					lsLienHldData,
					COL_05,
					charLength(lsLienHldData));
			}
			if (cbPymntMade)
			{
				if (ciPymntIndex < ciPymntMaxRecs)
				{
					Vector lvPymntItm2 =
						(Vector) cvPymntData2.elementAt(ciPymntIndex);
					String lsPymnt1 = (String) lvPymntItm2.elementAt(0);
					Dollar lsPymnt2 = (Dollar) lvPymntItm2.elementAt(1);
					String lsPymnt3 = (String) lvPymntItm2.elementAt(2);
					printVehPymnt(lsPymnt1, lsPymnt2, lsPymnt3);
					csPrevPaymntDesc = lsPymnt1;
					TransactionPaymentData laPaymentData =
						new TransactionPaymentData();
					laPaymentData.setPymntTypeAmt(lsPymnt2);
					//Added for PCR25 Code
					if (!(cbCreditFeeIncMsgIndi
						&& lsPymnt1.equals(CHARGE)
						&& formatCreditCardFeeMessage() != null))
					{
						ciPymntIndex++;
					}
				}
				else
				{
					cbPymntTotPrnt = true;
				}
			} // end if (cbPymntMade)
			// defect 6068
			// only print total if flag is on
			// changed else to else if
			else
			{
				if (cbPymntTotPrnt)
				{
					printTotalPymnt();
				}
				// end defect 6068
				// Move down 1 lines
				caRpt.blankLines(1);
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTSEx = new RTSException("", aeNPEx);
		}
	}
	/**
	 * Print lienholder 3 information.
	 * 
	 */
	protected void printLienHold3()
	{
		String lsLienHldData = "";
		int liDate = 0;
		int liItem1 = 1;
		int liItem2 = 2;
		boolean lbFlag = false;
		Vector lvLienHld3 = parseLienHld3();
		int liFeesRow = 53;
		for (int i = caRpt.getCurrX(); i < liFeesRow; i++)
		{
			caRpt.blankLines(1);
		}
		// ******** Print LienHolder2 Header, Date -- Line 1
		caRpt.print(THIRDLIENTXT, COL_05, charLength(THIRDLIENTXT));
		lsLienHldData = (String) lvLienHld3.elementAt(liDate);
		// defect 6716
		// Print correct Lienholder3 Date
		//		if (!(lsLienHldData == null || lsLienHldData.equals("")))
		//		    {
		//		    cRpt.print(DATE, COL_25, charLength(DATE));
		//		    lRTSDate = new RTSDate(Integer.parseInt(lsLienHldData));
		//		    cRpt.print(lRTSDate.toString());
		//		} // end if

		String lsDate = "";
		if (lsLienHldData != null && !lsLienHldData.equals(""))
		{
			caRpt.print(DATE, COL_24, charLength(DATE));
			lsDate = convertDtToStr(Integer.parseInt(lsLienHldData));
			caRpt.print(lsDate);
		}
		// end defect 6716    
		if (cbPymntMade)
		{
			if (ciPymntIndex < ciPymntMaxRecs)
			{
				Vector lvPymntItm1 =
					(Vector) cvPymntData2.elementAt(ciPymntIndex);
				String lsPymnt1 = (String) lvPymntItm1.elementAt(0);
				Dollar lsPymnt2 = (Dollar) lvPymntItm1.elementAt(1);
				String lsPymnt3 = (String) lvPymntItm1.elementAt(2);
				printVehPymnt(lsPymnt1, lsPymnt2, lsPymnt3);
				csPrevPaymntDesc = lsPymnt1;
				TransactionPaymentData laPaymentData =
					new TransactionPaymentData();
				laPaymentData.setPymntTypeAmt(lsPymnt2);
				//Added for PCR25 Code
				if (!(cbCreditFeeIncMsgIndi
					&& lsPymnt1.equals(CHARGE)
					&& formatCreditCardFeeMessage() != null))
				{
					ciPymntIndex++;
				}
			}
			else
			{
				if (cbPymntTotPrnt)
				{
					printTotalPymnt();
					lbFlag = true;
				}
			}
		}
		caRpt.blankLines(1);
		// ************************************************************
		// *********** Print LienHolder2 Name 1,  -- Line 2
		lsLienHldData = (String) lvLienHld3.elementAt(liItem1);
		if (!(lvLienHld3.elementAt(liItem1) == null
			|| lsLienHldData.equals("")))
		{
			caRpt.print(
				lsLienHldData,
				COL_05,
				charLength(lsLienHldData));
		}

		if (cbPymntMade)
		{
			if (ciPymntIndex < ciPymntMaxRecs && lbFlag == false)
			{
				Vector lvPymntItm1 =
					(Vector) cvPymntData2.elementAt(ciPymntIndex);
				String lsPymnt1 = (String) lvPymntItm1.elementAt(0);
				Dollar lsPymnt2 = (Dollar) lvPymntItm1.elementAt(1);
				String lsPymnt3 = (String) lvPymntItm1.elementAt(2);
				printVehPymnt(lsPymnt1, lsPymnt2, lsPymnt3);
				csPrevPaymntDesc = lsPymnt1;
				TransactionPaymentData laPaymentData =
					new TransactionPaymentData();
				laPaymentData.setPymntTypeAmt(lsPymnt2);
				//Added for PCR25 Code
				if (!(cbCreditFeeIncMsgIndi
					&& lsPymnt1.equals(CHARGE)
					&& formatCreditCardFeeMessage() != null))
				{
					ciPymntIndex++;
				}
			}
			else
			{
				if (lbFlag == false)
				{
					printTotalPymnt();
					lbFlag = true;
				}
				else
				{
					if (lbFlag == true
						&& !caChangeDue.equals(new Dollar(0)))
					{
						caRpt.print(
							"CHANGE DUE",
							COL_62,
							charLength("CHANGE DUE"));
						// defect 10569
						caRpt.print(
							DOLLAR$TEXT,
							COL_82,
							//COL_87,
							charLength(DOLLAR$TEXT));
						this.caRpt.rightAlign(
							caChangeDue.printDollar().substring(1),
							COL_83,
							//COL_88,
							SALES_CHAR_LNGTH);
						// end defect 10569
					}
				}
			}
		} // end if (cbPymntMade)
		caRpt.blankLines(1);
		// *************************************************************
		// ************  Print LienHolder1 City/State/Zip, Fees Data  
		// -- Line 6
		if (!(lvLienHld3.elementAt(liItem2) == null))
		{
			lsLienHldData = (String) lvLienHld3.elementAt(liItem2);
			caRpt.print(
				lsLienHldData,
				COL_05,
				charLength(lsLienHldData));
		}
		//Added code for PCR25
		if (ciPymntIndex < ciPymntMaxRecs && lbFlag == false)
		{
			Vector lvPymntItm1 =
				(Vector) cvPymntData2.elementAt(ciPymntIndex);
			String lsPymnt1 = (String) lvPymntItm1.elementAt(0);
			Dollar lsPymnt2 = (Dollar) lvPymntItm1.elementAt(1);
			String lsPymnt3 = (String) lvPymntItm1.elementAt(2);
			printVehPymnt(lsPymnt1, lsPymnt2, lsPymnt3);
			csPrevPaymntDesc = lsPymnt1;
			TransactionPaymentData laPaymentData =
				new TransactionPaymentData();
			laPaymentData.setPymntTypeAmt(lsPymnt2);
			//Added for PCR25 Code
			if (!(cbCreditFeeIncMsgIndi
				&& lsPymnt1.equals(CHARGE)
				&& formatCreditCardFeeMessage() != null))
			{
				ciPymntIndex++;
			}
		}
		caRpt.blankLines(1);
		if (cbPymntMade)
		{
			if (lbFlag == false)
			{
				printTotalPymnt();
			}
			caRpt.blankLines(1);
			// defect 10596
//			if (!lbFlag == false && !caChangeDue.equals(new Dollar(0)))
//			{
			if (lbFlag && !caChangeDue.equals(new Dollar(0)))
			{
				caRpt.print(
					"CHANGE DUE",
					COL_62,
					charLength("CHANGE DUE"));
				// defect 10596
				caRpt.print(
					DOLLAR$TEXT,
					COL_82, 
					//COL_87,
					charLength(DOLLAR$TEXT));
				this.caRpt.rightAlign(
					caChangeDue.printDollar().substring(1),
					COL_83,
					//COL_88,
					SALES_CHAR_LNGTH);
				// end defect 10596
			}
			// end defect 10596
		} // end if (cbPymntMade)    
	}
	/**
	 * Print Sales/Title Issue Date/Sales.
	 * 
	 */
	private void printSalesTtlIssueDtDocNoData()
	{
		Dollar laZeroDollarAmt = new Dollar("0.00");

		int liFeesRow = 40;

		for (int i = caRpt.getCurrX(); i < liFeesRow; i++)
		{
			caRpt.blankLines(1);
		}

		// Print the Registration Fees Paid heading
		// defec 10596
		caRpt.print(
			REGISTRATIONFEESPAIDTEXT,
			COL_35,
			//COL_43,
			charLength(REGISTRATIONFEESPAIDTEXT));
		caRpt.print(
			SALESTAXINFORTEXT,
			COL_63,
			//COL_68,
			charLength(SALESTAXINFORTEXT));
		// end defect 10596
		caRpt.blankLines(1);

		// Print the Title Issue Date
		caRpt.print(
			TITLEISSUEDDATETEXT,
			COL_05,
			charLength(TITLEISSUEDDATETEXT));
		if (caTitleData.getTtlIssueDate() != 0)
		{
			String lsTtlIssuedDt =
				convertDtToStr(caTitleData.getTtlIssueDate());
			caRpt.print(lsTtlIssuedDt);
		}

		// Print the Registration Fees
		int liResult = 0;
		if (caRegisData != null
			&& caRegisData.getCustActlRegFee() != null)
			liResult =
				caRegisData.getCustActlRegFee().compareTo(
					laZeroDollarAmt);

		if (liResult >= 0)
		{
			// defect 10596
			caRpt.print(
				REGISTRATION$TEXT,
				COL_35,
				//COL_43,
				charLength(REGISTRATION$TEXT));

			if (caRegisData.getCustActlRegFee() != null)
			{
				caRpt.rightAlign(
					caRegisData.getCustActlRegFee().toString(),
					COL_56,
					//COL_58,
					REGISTRATION_FEE);
			}
			else
			{
				//caRpt.rightAlign("0.00",COL_58, REGISTRATION_FEE);
				caRpt.rightAlign("0.00",COL_56, REGISTRATION_FEE);
			}
			// end defect 10596
		} // end if
		else
		{
			caRpt.print(caRpt.blankSpaces(1));
		}

		// Print the Sales Price
		// defect 10596
		//caRpt.print(SALESPRICRTEXT, COL_68, charLength(SALESPRICRTEXT));
		//caRpt.print(DOLLAR$TEXT, COL_87, charLength(DOLLAR$TEXT));
		caRpt.print(SALESPRICRTEXT, COL_63, charLength(SALESPRICRTEXT));
		caRpt.print(DOLLAR$TEXT, COL_82, charLength(DOLLAR$TEXT));
		// end defect 10596
		Dollar laSalesPrice = caTitleData.getVehSalesPrice();
		if (laSalesPrice == null)
		{
			laSalesPrice = new Dollar("0.00");
		}
		// defect 10596
		caRpt.rightAlign(
			laSalesPrice.printDollar(false),
			COL_83,
			//COL_88,
			SALES_CHAR_LNGTH);
		// defect 10596
		caRpt.blankLines(1);

		// Print the Trade In Allowance
		// defect 10596
		caRpt.print(
			TRADEINALLOWANCETEXT,
			COL_63,
			//COL_68,
			charLength(TRADEINALLOWANCETEXT));
		//caRpt.print(DOLLAR$TEXT,COL_87, charLength(DOLLAR$TEXT));
		caRpt.print(DOLLAR$TEXT, 82, charLength(DOLLAR$TEXT));
		// end defect 10596
		Dollar laTradePrice = caTitleData.getVehTradeinAllownce();
		if (laTradePrice == null)
		{
			laTradePrice = new Dollar("0.00");
		}
		// defect 10596
		caRpt.rightAlign(
			laTradePrice.printDollar(false),
			COL_83,
			//COL_88,
			SALES_CHAR_LNGTH);
		// end defect 10596
		caRpt.blankLines(1);

		// Print the Document Number
		caRpt.print(DOCTYPETEXT, COL_05, charLength(DOCTYPETEXT));
		String lsDocTypeCd =
			getCacheDocTypeDesc(caTitleData.getDocTypeCd());
		caRpt.print(lsDocTypeCd);

		// Print the sales Tax Paid
		// defect 10596
		caRpt.print(
			SALESTAXPAIDTEXT,
			COL_63,
			//COL_68,
			charLength(SALESTAXPAIDTEXT));
		//caRpt.print(DOLLAR$TEXT,COL_87,charLength(DOLLAR$TEXT));
		caRpt.print(DOLLAR$TEXT, 82,charLength(DOLLAR$TEXT));

		if (caTitleData.getSalesTaxPdAmt() != null)
		{
			caRpt.rightAlign(
				caTitleData.getSalesTaxPdAmt().printDollar(false),
				COL_83,
				//COL_88,
				SALES_CHAR_LNGTH);
		}
		else
		{
			//caRpt.rightAlign("0.00", *COL_88, SALES_CHAR_LNGTH);
			caRpt.rightAlign("0.00",COL_87,SALES_CHAR_LNGTH);
		}
		// end defect 10596
		caRpt.blankLines(1);

	}
	/**
	 * Print the total fees
	 * 
	 */
	private void printTotalFee()
	{
		String lsTotalFees =
			caGentitleRcpt.formatDollarAmt(
				getTotalFees().printDollar(false),
				NUM_11);

		this.caRpt.blankLines(1);
		// defect 10596
		//this.caRpt.print(TOTAL_TEXT, COL_68, charLength(TOTAL_TEXT));
		this.caRpt.print(TOTAL_TEXT, COL_64, charLength(TOTAL_TEXT));
		//this.caRpt.print(DOLLAR$TEXT, COL_81, DOLLAR$TEXT.length());
		this.caRpt.print(DOLLAR$TEXT, COL_82, DOLLAR$TEXT.length());
		this.caRpt.rightAlign(
			lsTotalFees.substring(2),
			83,//TOT_INQ_FEE_POS,
			SALES_CHAR_LNGTH);
//		// end defect 10596
	}
	/**
	 * Print payment total.
	 * 
	 */
	private void printTotalPymnt()
	{
		caRpt.print(
			TOTALAMTPAIDTEXT,
			COL_62,
			charLength(TOTALAMTPAIDTEXT));
		// defect 10596
		//caRpt.print(DOLLAR$TEXT,COL_87,charLength(DOLLAR$TEXT));
		caRpt.print(DOLLAR$TEXT, 82,charLength(DOLLAR$TEXT));
		this.caRpt.rightAlign(
			getTotalPymnt().printDollar().substring(1),
			COL_83, 
			//COL_88,
			SALES_CHAR_LNGTH);
		// end defect 10596
	}
	/**
	 * Print Vehicle Data Account
	 * 
	 * @param avVehdata Vector
	 */
	protected void printVehDataAcct(Vector avVehdata)
	{
		int liVehInfoRow = 20;

		// Set printer pointer
		for (int i = getCurrX(); i < liVehInfoRow; i++)
		{
			this.caRpt.blankLines(1);
		}

		for (int i = 0; i < avVehdata.size(); i++)
		{
			switch (i)
			{
				case 0 : // Print VIN
					{
						printVIN(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 1 : // Print VIN
					{
						printVehClassifcation(
							avVehdata.elementAt(i).toString(),
							COL_56);
						// Move down 1 line
						caRpt.blankLines(1);
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
							COL_25);
						break;
					}
				case 4 : // Print Body Style
					{
						printVehBdyStyle(
							avVehdata.elementAt(i).toString(),
							COL_37);
						break;
					}
				case 5 : // Print Unit number
					{
						printVehUnitNo(
							avVehdata.elementAt(i).toString(),
							COL_55);
						break;
					}
				case 6 : // Print Resident Compt Number
					{
						printResCntyNo(
							avVehdata.elementAt(i).toString(),
							COL_71);
						// Move down 1 line
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
							COL_23);
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
						break;
					}
				case 11 : // Print Trailer Type
					{
						printTrlrType(
							avVehdata.elementAt(i).toString(),
							COL_82);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 12 : // Print Body VIN
					{
						printVehBdyVIN(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 13 : // Print Vehicle Length
					{
						printTrvlTrlrLngth(
							avVehdata.elementAt(i).toString(),
							COL_61);
						break;
					}
				case 14 : // Print Tire Type
					{
						printTireType(
							avVehdata.elementAt(i).toString(),
							COL_85);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 15 : // Print Registration Issue Date
					{
						printRegIssueDt(
							avVehdata.elementAt(i).toString(),
							COL_05);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 16 : // Print Odometer Reading
					{
						printVehOdoRdng(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 17 : // Print Odometer Brand
					{
						printVehOdoBrnd(
							avVehdata.elementAt(i).toString(),
							COL_31);
						break;
					}
				case 18 : // Print Previous Plate Number
					{
						printPrevPltNo(
							avVehdata.elementAt(i).toString(),
							COL_41);
						break;
					}
				case 19 : // Print Previous Exp Mo/Yr
					{
						printPrevExpMoYr(
							avVehdata.elementAt(i).toString(),
							COL_69);
						// Move down 1 line
						caRpt.blankLines(1);
						break;
					}
				case 20 : // Print Previous Owner
					{
						printPrevOwnr(
							avVehdata.elementAt(i).toString(),
							COL_05);
						break;
					}
				case 21 : // Print Previous City/State
					{
						printPrevCityState(
							avVehdata.elementAt(i).toString(),
							COL_49);
						break;
					}
				case 22 : // Print Plate Age
					{
						printPltAge(
							avVehdata.elementAt(i).toString(),
							COL_83);
						// Move down 1 line
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
	 * Print assessed fees.
	 * 
	 * @param asFeeDescr String
	 * @param aaFeeAmt Dollar
	 */
	private void printVehInqFees(String asFeeDescr, Dollar aaFeeAmt)
	{
		String lsFees =
			caGentitleRcpt.formatDollarAmt(
				aaFeeAmt.printDollar(false),
				NUM_11);

		this.caRpt.print(asFeeDescr, COL_50, charLength(asFeeDescr));
		// defect 10596
		//this.caRpt.print(DOLLAR$TEXT, COL_81, DOLLAR$TEXT.length());
		this.caRpt.print(DOLLAR$TEXT, COL_82, DOLLAR$TEXT.length());
		// defect 10596
		this.caRpt.rightAlign(
			lsFees.substring(2),
			COL_83,//TOT_INQ_FEE_POS,
			SALES_CHAR_LNGTH);

		setTotalFees(caTotalFees.add(aaFeeAmt));
	}
	/**
	* Prnt vehicle notations.
	* 
	*/
	private void printVehInqVehNotation()
	{
		int ciVehNotationCol_59 = 59;
		int liIndex = 0;

		Vector lvRcptInfo = new Vector();
		// GenRegistrationReceipts lGenRegisRecpt = new GenRegistrationReceipts();

		// Print the title text for vehicle notation
		caRpt.print(
			VEH_NOTATION_TEXT,
			COL_43,
			charLength(VEH_NOTATION_TEXT));
		// Move down 1 lines
		caRpt.nextLine();

		lvRcptInfo = buildNotationsVector(caTransData);
		int liNumOfVehNotations = 0;
		if (lvRcptInfo != null)
		{
			liNumOfVehNotations = lvRcptInfo.size();
		}

		while (liIndex < liNumOfVehNotations)
		{
			caRpt.print(
				lvRcptInfo.elementAt(liIndex).toString(),
				COL_05,
				charLength(lvRcptInfo.elementAt(liIndex).toString()));
			liIndex++;
			if (liIndex < liNumOfVehNotations)
			{
				caRpt.print(
					lvRcptInfo.elementAt(liIndex).toString(),
					ciVehNotationCol_59,
					charLength(
						lvRcptInfo.elementAt(liIndex).toString()));
				liIndex++;
				// Move down 1 lines
				caRpt.blankLines(1);
			}
		} // end while loop
	}
	/**
	 * Print assessed fees.
	 * 
	 * @param asPymntDescr String
	 * @param aaPymntAmt Dollar
	 * @param asPymntCk String
	 */
	private void printVehPymnt(
		String asPymntDescr,
		Dollar aaPymntAmt,
		String asPymntCk)
	{
		TransactionPaymentData laCreditFeeData =
			(TransactionPaymentData) cvPymntData.get(
				cvPymntData.size() - 1);

		//defect 4869
		// Do not print credit fee details if there is not
		// credit fee.
		CreditCardFeeData laCreditCardFeeData =
			CreditCardFeesCache.getCurrentCreditCardFees(
				SystemProperty.getOfficeIssuanceNo(),
				RTSDate.getCurrentDate());
		//  if (creditFeeData.isCreditCardFee() 
		//  && creditFeeData.getPymntTypeAmt().equals(adPymntAmt))
		if (cbCreditFeeIncMsgIndi
			&& csPrevPaymntDesc.equals(CHARGE)
			|| (laCreditFeeData.isCreditCardFee()
				&& laCreditFeeData.getPymntTypeAmt().equals(aaPymntAmt)))
		{
			if (laCreditCardFeeData == null)
			{
				return;
			}
			cbCreditFeeIncMsgIndi = false;
			TransactionPaymentData laPaymentData =
				new TransactionPaymentData();
			laPaymentData.setPymntTypeAmt(aaPymntAmt);
			String lsCreditCardFeeMessage = null;
			lsCreditCardFeeMessage = formatCreditCardFeeMessage();
			if (lsCreditCardFeeMessage != null
				&& laCreditFeeData.isCreditCardFee()
				&& laCreditFeeData.getPymntTypeAmt().equals(aaPymntAmt))
			{
				//defect 9094
				// Drop down one line before printing Credit Card Fee msg					
				this.caRpt.nextLine();
				//end defect 9094					
				this.caRpt.print(
					lsCreditCardFeeMessage,
					COL_62 + 18,
					charLength(lsCreditCardFeeMessage));
			}
			else
			{
				this.caRpt.print(
					asPymntDescr,
					COL_62,
					charLength(asPymntDescr));

				if (asPymntDescr.equals(CHECK))
				{
					this.caRpt.print(" #");
				}
				if (asPymntCk != null && !asPymntCk.equals(""))
				{
					this.caRpt.print(asPymntCk);
				}
				// defect 10596
				this.caRpt.print(
					DOLLAR$TEXT,
					COL_82,
					//COL_87,
					charLength(DOLLAR$TEXT));
				// end defect 10596
				this.caRpt.rightAlign(
					laCreditFeeData
						.getPymntTypeAmt()
						.printDollar()
						.substring(
						1),
						83,
					SALES_CHAR_LNGTH);

				setTotalPymnt(
					caTotalPymnt.add(
						laCreditFeeData.getPymntTypeAmt()));
			}
		}
		else
		{
			this.caRpt.print(
				asPymntDescr,
				COL_62,
				charLength(asPymntDescr));

			if (asPymntDescr.equals(CHECK))
			{
				this.caRpt.print(" #");
			}
			if (asPymntCk != null && !asPymntCk.equals(""))
			{
				this.caRpt.print(asPymntCk);
			}
			// defect 10596
			this.caRpt.print(
				DOLLAR$TEXT,
				COL_82,
				//COL_87,
				charLength(DOLLAR$TEXT));
			this.caRpt.rightAlign(
				aaPymntAmt.printDollar().substring(1),
				83,
				//COL_88,
				SALES_CHAR_LNGTH);
			// end defect 10596
			setTotalPymnt(caTotalPymnt.add(aaPymntAmt));
		}
	}
	/**
	 * Query Data
	 * 
	 * @param  asQuery String
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
	/**
	 * Set Total Fees
	 *  
	 * @param aaNewTotalFees Dollar
	 */
	private void setTotalFees(Dollar aaNewTotalFees)
	{
		caTotalFees = aaNewTotalFees;
	}
	/**
	 * Set Total Payment
	 * 
	 * @param aaNewTotalPymnt Dollar
	 */
	private void setTotalPymnt(Dollar aaNewTotalPymnt)
	{
		caTotalPymnt = aaNewTotalPymnt;
	}
}
