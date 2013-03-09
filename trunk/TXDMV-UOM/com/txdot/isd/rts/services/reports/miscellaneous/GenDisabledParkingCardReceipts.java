package com.txdot.isd.rts.services.reports.miscellaneous;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.DisabledPlacardDeleteReasonCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.reports.Vehicle;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * GenDisabledParkingCardReceipts.java
 *
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang     11/11/2001  New Class
 * Min Wang		05/03/2002	using buildPaymentVector()
 *							from ReceiptTemplate.
 * S Govindappa 07/10/2002 	Merging the PCR25 code in printPayment
 * 							method
 * MAbs	 		08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3   
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify printInventoryAndFees(), main()
 * 							queryData()
 *							defect 7896 Ver 5.2.3
 * J Zwiener	08/05/2005  Enhancement for Disable Placard event
 * 							modify class variable
 * 							defect 8268 Ver 5.2.2 Fix 6
 * K Harrell	10/27/2008	Enhancements for Disabled Placard
 * 							add DELREASN, DSABLDMSG, RPLREASN   
 * 							modify formatReceipt()
 * 							add printMessage(), printDelReason()
 * 							defect 9831 Ver Defect_POS_B 
 * Min Wang	10/08/2010		Allow printing of the Max length of the price. 
 * 							modify ITEMPRICELENGTH  
 * 							defect 10596 Ver 6.6.0   
 * ---------------------------------------------------------------------
 */

/**
 * This class generates all the Disabled Parking Card Receipts.
 *
 * @version	6.6.0			10/08/2010
 * @author	Min Wang
 * <br>Creation Date:		11/12/2001 07:33:33
 */
public class GenDisabledParkingCardReceipts extends ReceiptTemplate
{
	private static final int DOLLARSIGNPOSITION = 80;
	private static final int FEEASSESSEDLENGTH = 28;
	private static final int FEEASSESSEDPOSITION = 47;
	private static final int FIRSTCOLUMNPOSITION = 5;
	// defect 10596
	//private static final int ITEMPRICELENGTH = 12;
	private static final int ITEMPRICELENGTH = 14;
	// end defect 10596
	private static final int LENGTH1 = 1;
	private static final int LENGTH5 = 5;
	private static final int MONEYPOSITION = 81;
	private static final int PAYMENTHEADERPOSITION = 55;
	private static final int PAYMENTPOSITION = 64;
	private static final int TOTALPOSITION = 65;
	private static final int TRANSOBJPOSITION = 0;
	private static final int YEARLENGTH = 4;
	private static final int YEARPOSITION = 37;

	// defect 9831
	private static final String DELREASN = "DELETE REASON: ";
	private static final String RPLREASN = "REPLACE REASON: ";
	private static final String DSABLDMSG =
		"PLEASE RETAIN THIS RECEIPT FOR YOUR RECORDS.";
	// end defect 9831 

	private static final String DOLLARSIGN = "$";
	private static final String FEEASSESSEDSTRING = "FEES ASSESSED";
	private static final String INVITMSTRING = "INVENTORY ITEM(S)";
	private static final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	private static final String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT";
	private static final String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	private static final String TOTALSTRING = "TOTAL";
	private static final String YEARSTRING = "YR";

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
		GenDisabledParkingCardReceipts laGTPR =
			new GenDisabledParkingCardReceipts(
				"NOT USED FOR RECEIPTS",
				laRcptProps);

		// Generating Demo data to display.
		Vector lvFakeDataVector = laGTPR.queryData();
		laGTPR.formatReceipt(lvFakeDataVector);

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

		laPout.print(laGTPR.caRpt.getReport().toString());
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
			// Process p = Runtime.getRuntime().exec("cmd.exe
			// /c copy c:\\QuickCtcRpt.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of"
					+ " com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}
	private boolean cbCreditFeeIncMsgIndi = true;

	/**
	 * GenDisabledParkingCardReceipts constructor
	 */
	public GenDisabledParkingCardReceipts()
	{
		super();
	}

	/**
	 * GenDisabledParkingCardReceipts constructor
	 * 
	 * @param asRcptName String
	 * @param aaRcptProps ReceiptProperties
	 */
	public GenDisabledParkingCardReceipts(
		String asRcptName,
		ReceiptProperties aaRcptProps)
	{
		super(asRcptName, aaRcptProps);
	}

	/**
	 * Format Receipt
	 * 
	 * @param avDataVector Vector
	 */
	public void formatReceipt(Vector avDataVector)
	{
		// Get the CompleteTransData object out of the vector
		CompleteTransactionData laTransData =
			(CompleteTransactionData) avDataVector.elementAt(
				TRANSOBJPOSITION);

		try
		{
			generateReceiptHeader(avDataVector);
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println("Had a problem with Heading Lookup!");
			aeRTSEx.printStackTrace();
		}

		// defect 9831 
		if (!(UtilityMethods
			.getEventType(laTransData.getTransCode())
			.equals(TransCdConstant.DEL_DP_EVENT_TYPE)))
		{
			// print Inventory Items and Fees 
			printInventoryAndFees(laTransData);

			// print Payment Info
			printPayment(avDataVector);
		}
		if (!(UtilityMethods
			.getEventType(laTransData.getTransCode())
			.equals(TransCdConstant.ADD_DP_EVENT_TYPE)
			|| UtilityMethods
			.getEventType(laTransData.getTransCode())
			.equals(TransCdConstant.REI_DP_EVENT_TYPE) 
			|| UtilityMethods
			.getEventType(laTransData.getTransCode())
			.equals(TransCdConstant.REN_DP_EVENT_TYPE)))
		{
			printDelReasn(laTransData);
		}

		caRpt.blankLines(51 - caRpt.getCurrX());
		printMessage();
		// end defect 9831 
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Print the Delete Reason
	 * 
	 * @param laTransData 
	 * 
	 */
	public void printDelReasn(CompleteTransactionData laTransData)
	{
		caRpt.blankLines(3);
		DisabledPlacardCustomerData laDPCustData =
			laTransData.getTimedPermitData().getDPCustData();
		Vector lvDPData = laDPCustData.getDsabldPlcrd();

		if (lvDPData != null && lvDPData.size() > 0)
		{
			for (int i = 0; i < lvDPData.size(); i++)
			{
				DisabledPlacardData laDPData =
					(DisabledPlacardData) lvDPData.elementAt(i);

				if (laDPData.getTransTypeCd()
					== MiscellaneousRegConstant.DP_DEL_TRANS_TYPE_CD)
				{
					int liDelReasn = laDPData.getDelReasnCd();

					DisabledPlacardDeleteReasonData laDelReasnData =
						DisabledPlacardDeleteReasonCache
							.getDsabldPlcrdDelReasn(
							liDelReasn);

					if (laDelReasnData != null)
					{
						String lsReasn =
							UtilityMethods.getEventType(
								laTransData.getTransCode()).equals(
								TransCdConstant.RPL_DP_EVENT_TYPE)
								? RPLREASN
								: DELREASN;
								
						caRpt.print(
							lsReasn,
							FIRSTCOLUMNPOSITION,
							lsReasn.length());

						caRpt.print(laDelReasnData.getDelReasnDesc());
					}
				}
			}
		}
		caRpt.blankLines(2);
	}

	/**
	 * Print the Inventory and Fees Section.
	 * 
	 * @param aaTransData CompleteTransactionData
	 */
	public void printInventoryAndFees(CompleteTransactionData aaTransData)
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

		int liInvCount = 0; // index through the inventory vector
		int liFeeCount = 0; // index through the Fees vector
		boolean lbKeepLooping = true;
		// loop controller.  keep looping until it is set off.
		Dollar laFeeTotal = new Dollar(0);

		while (lbKeepLooping)
		{
			if (aaTransData.getAllocInvItms() != null
				&& liInvCount < aaTransData.getAllocInvItms().size())
			{
				ProcessInventoryData laInvData =
					(ProcessInventoryData) aaTransData
						.getAllocInvItms()
						.elementAt(
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
			if (aaTransData.getRegFeesData().getVectFees() != null
				&& liFeeCount
					< aaTransData.getRegFeesData().getVectFees().size())
			{
				FeesData laFeeData =
					(FeesData) aaTransData
						.getRegFeesData()
						.getVectFees()
						.elementAt(
						liFeeCount);
				caRpt.print(
					laFeeData.getDesc(),
					FEEASSESSEDPOSITION,
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
			if ((aaTransData.getAllocInvItms() == null
				|| liInvCount >= aaTransData.getAllocInvItms().size())
				&& (aaTransData.getRegFeesData().getVectFees() == null
					|| liFeeCount
						>= aaTransData
							.getRegFeesData()
							.getVectFees()
							.size()))
			{
				lbKeepLooping = false;
			}
		} // end while

		caRpt.nextLine();
		caRpt.print(TOTALSTRING, TOTALPOSITION, LENGTH5);
		caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
		caRpt.rightAlign(
			laFeeTotal.printDollar().substring(1),
			MONEYPOSITION,
			ITEMPRICELENGTH);
		caRpt.nextLine();
	}

	/**
	 * Print the Receipt Message for Disabled Placards.
	 */
	public void printMessage()
	{
		caRpt.print(
			DSABLDMSG,
			FIRSTCOLUMNPOSITION,
			DSABLDMSG.length());
		caRpt.blankLines(2);
	}

	/**
	 * Print the Payment Section  
	 * 
	 * @param avDataVector Vector
	 */
	public void printPayment(Vector avDataVector)
	{
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

		for (int i = 0; i < lvPaymentData.size(); i++)
		{

			TransactionPaymentData laPaymentData =
				(TransactionPaymentData) lvPaymentData.elementAt(i);
			caRpt.print(
				laPaymentData.getPymntType(),
				PAYMENTPOSITION + 2,
				laPaymentData.getPymntType().length());

			// print the check number if it exists
			//&& lPaymentData.getPymntCkNo().length() > 0
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

			laPaymentTotal =
				(laPaymentTotal.add(laPaymentData.getPymntTypeAmt()));
			if (laPaymentData.getChngDue() != null
				&& !laPaymentData.getChngDue().equals(laZeroDollar))
			{
				laChangeBackAmt = laPaymentData.getChngDue();
			}
			caRpt.nextLine();
		}
		// Print the Total Payment Line
		if (!laPaymentTotal.equals(laZeroDollar))
		{
			caRpt.nextLine();
			caRpt.print(
				PAYMENTTOTALSTRING,
				60 + 2,
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
				60 + 2,
				PAYMENTCHANGEDUESTRING.length());
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			caRpt.rightAlign(
				laChangeBackAmt.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			caRpt.blankLines(60 - caRpt.getCurrX());
		}
	}

	/**
	 * This version of queryData returns the CompleteTransactionData
	 * 	object.
	 */
	public Vector queryData()
	{
		Vector lvReceipt = new Vector();

		CompleteTransactionData laTransData =
			new CompleteTransactionData();
		laTransData = (CompleteTransactionData) Vehicle.getVeh();
		lvReceipt.addElement(laTransData);
		laTransData.setTransCode("72PT");

		// Add the transid to the vector
		lvReceipt.addElement("17010037205111155");

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
