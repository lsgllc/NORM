package com.txdot.isd.rts.services.reports.miscellaneous;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * GenTimedPermitReceipts.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * Min Wang     11/11/2001	New Class
 * S Govindappa	07/10/2002	Merged PCR25 code. Changed printPayment()  
 * && J Rue					for PCR25 changes to show credit card fee on
 * 							receipts
 * MAbs	 		08/28/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration 
 * S Johnston	05/31/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify printInventoryAndFees(),
 * 							printMessages(), main(), queryData()
 *							defect 7896 Ver 5.2.3
 * K Harrell	06/20/2010	Modify for new Timed Permit Processing - 
 * 							including Duplicate Permit Receipt.
 * 							Remove unused code. 
 * 							add caPrmtData, caCTData, PERMITFEESPAID 
 * 							delete unused constants
 * 							modify VEHISSUEDSTRING
 * 							modify printMessages(),printOneTripPermitData(),
 * 							  printInventoryAndFees(), printVehicleData() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/14/2010	Creation of Inventory Vector no longer needed
 * 							Permits now include TR_INV_DETAIL records
 * 							modify printInventoryAndFees()
 * 							defect 10491 Ver 6.5.0  
  * Min Wang	10/08/2010	Allow printing of the Max length of the price. 
 * 							modify ITEMPRICELENGTH  
 * 							defect 10596 Ver 6.6.0   	
 * ---------------------------------------------------------------------
 */
/**
 * This class generates all the Timed Permit Receipts.
 *  
 * @version	6.6.0			10/08/2010
 * @author	Min Wang
 * <br>Creation Date:		11/12/2001 07:33:33
 */
public class GenTimedPermitReceipts extends ReceiptTemplate
{
	boolean cbCreditFeeIncMsgIndi = true;

	// defect 10491 
	private PermitData caPrmtData;
	private CompleteTransactionData caCTData;
	// end defect 10491 

	private static final int FIRSTCOLUMNPOSITION = 5;
	private static final int SECONDCOLUMNPOSITION = 39;
	private static final int FEEASSESSEDPOSITION = 45;
	private static final int TOTALPOSITION = 65;
	private static final int DOLLARSIGNPOSITION = 80;
	private static final int MONEYPOSITION = 81;
	private static final int YEARPOSITION = 37;
	private static final int YEARLENGTH = 4;
	private static final int FEEASSESSEDLENGTH = 28;
	// defect 10596
	//private static final int ITEMPRICELENGTH = 12;
	private static final int ITEMPRICELENGTH = 14;
	// end defect 10596
	private static final int PAYMENTHEADERPOSITION = 55;
	private static final int PAYMENTPOSITION = 64;
	private static final int COLUMNLENGTH34 = 34;
	private static final int LENGTH1 = 1;
	private static final int LENGTH5 = 5;
	private static final int TRANSOBJPOSITION = 0;

	private static final String ORIGINATIONPOINT = "ORIGINATION POINT";
	private static final String INTERMEDIATEPOINT =
		"INTERMEDIATE POINT(S) (IF APPLICABLE)";
	private static final String DESTINATIONPOINT = "DESTINATION POINT";
	private static final String VINSTRING =
		"VEHICLE IDENTIFICATION NO: ";
	private static final String YRMAKESTRING = "YR/MAKE: ";
	private static final String BDYSTYLESTRING = "BODY STYLE: ";
	private static final String INVITMSTRING = "INVENTORY ITEM(S)";
	private static final String FEEASSESSEDSTRING = "FEES ASSESSED";
	private static final String TOTALSTRING = "TOTAL";
	private static final String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	private static final String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	private static final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";

	// defect 10491 
	private static final String VEHISSUEDSTRING =
		//"THIS RECEIPT TO BE CARRIED IN THE VEHICLE FOR WHICH ISSUED.";
	"RECEIPT FOR PERMIT MUST BE CARRIED IN THE VEHICLE AT ALL TIMES.";
	private static final String PERMITFEESPAID = "PERMIT FEES PAID";

	// end defect 10491 

	private static final String DOLLARSIGN = "$";
	private static final String SLASH = "/";

	/**
	 * GenTimedPermitReceipts constructor
	 */
	public GenTimedPermitReceipts()
	{
		super();
	}

	/**
	 * GenTimedPermitReceipts constructor
	 * 
	 * @param asRcptName String
	 * @param aaRcptProps ReceiptProperties
	 */
	public GenTimedPermitReceipts(
		String asRcptName,
		ReceiptProperties aaRcptProps)
	{
		super(asRcptName, aaRcptProps);
	}

	/**
	 * Get the object off of the DataVector and check to see it they are
	 * Payment Objects.  If they are, put them on the outgoing Payment
	 * Vector.
	 * 
	 * @param avQueryData Vector
	 * @return lvPaymentVector Vector
	 */
	public Vector buildPaymentVector(Vector avDataVector)
	{
		// create the Payment Vector
		Vector lvPaymentVector = new Vector();

		for (int i = 0; i < avDataVector.size(); i++)
		{
			// pull the object out so we can inspect it
			Object laThisElement = avDataVector.elementAt(i);

			// inspect the object to see if it is a
			// TransactionPaymentData object
			if (laThisElement instanceof TransactionPaymentData)
			{
				lvPaymentVector.add(
					(TransactionPaymentData) laThisElement);
			}
		}
		// Send the new Payment Vector back.
		return lvPaymentVector;
	}

	/**
	 * This method generates the Permit Receipts.
	 * 
	 * @param lvDataVector Vector
	 */
	public void formatReceipt(Vector lvDataVector)
	{
		// Get the CompleteTransData object from vector
		caCTData =
			(CompleteTransactionData) lvDataVector.elementAt(
				TRANSOBJPOSITION);

		// Figure out what heading to put at the top of the Receipt and
		// print that heading.
		try
		{
			generateReceiptHeader(lvDataVector);
		}
		catch (RTSException aeRTSEx)
		{
			System.out.println("Had a problem with Heading Lookup!");
			aeRTSEx.printStackTrace();
		}

		// defect 10491 
		// Should ALWAYS be true  
		if (caCTData.getTimedPermitData() != null
			&& caCTData.getTimedPermitData() instanceof PermitData)
		{
			caPrmtData = (PermitData) caCTData.getTimedPermitData();

			if (caPrmtData.isOTPT())
			{
				printOneTripPermitData();
			}
		}
		// end defect 10491 

		// print vehicle data
		printVehicleData();

		// print inventory items and fees
		printInventoryAndFees();

		// print Payment Info
		printPayment(lvDataVector);

		// ** Add print of messages
		// ** this should also go down some lines
		// Print Vehicle messages
		caRpt.blankLines(62 - caRpt.getCurrX());
		//////before it is 51

		// defect 10491 
		//printMessages(laTransData);
		printMessages();
		// end defect 10491  
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Print the Inventory and Fees Section.
	 * 
	 * @param aaCTData CompleteTransactionData
	 */
	private void printInventoryAndFees()
	{
		// Look up the Item Codes description to print
		caRpt.print(
			INVITMSTRING,
			FIRSTCOLUMNPOSITION,
			INVITMSTRING.length());

		// defect 10491 
		// caRpt.print(YEARSTRING, YEARPOSITION, YEARLENGTH);
		// end defect 10492 

		caRpt.print(
			FEEASSESSEDSTRING,
			FEEASSESSEDPOSITION + 2,
			FEEASSESSEDSTRING.length());
		caRpt.nextLine();

		int liInvCount = 0; // index through the inventory vector
		int liFeeCount = 0; // index through the Fees vector
		boolean lbKeepLooping = true;

		// loop controller.  keep looping until it is set off.
		Dollar laZeroDollar = new Dollar(0);
		Dollar laFeeTotal = new Dollar(0);

		while (lbKeepLooping)
		{
			if (caCTData.getAllocInvItms() != null
				&& liInvCount < caCTData.getAllocInvItms().size())
			{
				ProcessInventoryData laInvData =
					(ProcessInventoryData) caCTData
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
			if (caCTData.getRegFeesData().getVectFees() != null
				&& liFeeCount
					< caCTData.getRegFeesData().getVectFees().size())
			{
				FeesData laFeeData =
					(FeesData) caCTData
						.getRegFeesData()
						.getVectFees()
						.elementAt(
						liFeeCount);
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
			if ((caCTData.getAllocInvItms() == null
				|| liInvCount >= caCTData.getAllocInvItms().size())
				&& (caCTData.getRegFeesData().getVectFees() == null
					|| liFeeCount
						>= caCTData
							.getRegFeesData()
							.getVectFees()
							.size()))
			{
				lbKeepLooping = false;
			}
		} // end while

		if (!laFeeTotal.equals(laZeroDollar))
		{
			caRpt.nextLine();
			caRpt.print(TOTALSTRING, TOTALPOSITION + 2, LENGTH5);
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH1);
			caRpt.rightAlign(
				laFeeTotal.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			caRpt.nextLine();
		}
		caCTData.setAllocInvItms(null);
	}

	/**
	 * Print the Receipt Messages.
	 * 
	 */
	private void printMessages()
	{
		//		try
		//		{
		// defect 10491 
		// Not Applicable 
		//			// print the Commercial Vehicle message.
		//			// Look up the Trans Code to get RCPTMSGCD to see if we
		//			// need to print the COMMERCIALVEHICLESTRING.
		//			// this look up can create an RTSException.  We are
		//			// just eating it!
		//			// defect 7896
		//			// changed method call to be statically referenced
		//			TransactionCodesData laTransCodeData =
		//				(
		//					TransactionCodesData) TransactionCodesCache
		//						.getTransCd(
		//					aaTransData.getTransCode());
		//			// end defect 7896
		//			// normal message
		//			if (laTransCodeData.getRcptMsgCd() == 1)
		//			{
		//				caRpt.print(
		//					COMMERCIALVEHICLESTRING,
		//					FIRSTCOLUMNPOSITION,
		//					COMMERCIALVEHICLESTRING.length());
		//				caRpt.blankLines(2);
		//			}
		//			// message for permits, tow trucks and other cases
		//			// matching these conditions.
		//			if (laTransCodeData.getRcptMsgCd() == 2)
		//			{
		// end defect 10491 
		caRpt.print(
			VEHISSUEDSTRING,
			FIRSTCOLUMNPOSITION,
			VEHISSUEDSTRING.length());
		caRpt.blankLines(2);

		// defect 10491 
		// Not Applicable 
		//}
		// message for credit remaining.
		//			if (aaTransData.getCrdtRemaining() != null
		//				&& !aaTransData.getCrdtRemaining().equals(
		//					new Dollar(0.00)))
		//			{
		//				caRpt.print(
		//					REGCREDITSTRING,
		//					FIRSTCOLUMNPOSITION,
		//					REGCREDITSTRING.length());
		//				caRpt.blankLines(2);
		//			}
		//			// check to see if a county is a "large automation" county.
		//			// if so, print the automation message.
		//			// defect 7896
		//			// changed method call to be statically referenced
		//			CountyCalendarYearData laCountyCalendarYearData =
		//				CountyCalendarYearCache.getCntyCalndrYr(
		//					aaTransData.getOfcIssuanceNo(),
		//					aaTransData.getOfcIssuanceNo());
		//			// end defect 7896
		//			if (laCountyCalendarYearData != null
		//				&& laCountyCalendarYearData.getCntySizeCd().equals(
		//					LARGECOUNTYSTRING))
		//			{
		//				caRpt.print(
		//					AUTOMATION_1_STRING,
		//					FIRSTCOLUMNPOSITION,
		//					AUTOMATION_1_STRING.length());
		//				caRpt.blankLines(1);
		//
		//				caRpt.print(
		//					AUTOMATION_2_STRING,
		//					FIRSTCOLUMNPOSITION,
		//					AUTOMATION_2_STRING.length());
		//				caRpt.blankLines(2);
		//			}
		// end defect 10491 
		//		}
		//		catch (RTSException aeRTSEx)
		//		{
		//			// this was bad.  but do not fail on this!
		//		}
		// this is where VTR messages would go if we used them!
		// There are three possible lines.
	}

	/**
	 * Determine Header based on Trans Code and Print it.
	 * 
	 */
	private void printOneTripPermitData()
	{
		caRpt.blankLines(19 - caRpt.getCurrX());

		// defect 10491 
		Vector lvData = new Vector();
		OneTripData laOTData = caPrmtData.getOneTripData();
		// end defect 10491 

		// Origination 
		caRpt.print(
			ORIGINATIONPOINT,
			FIRSTCOLUMNPOSITION,
			COLUMNLENGTH34);
		caRpt.nextLine();
		caRpt.print(
			laOTData.getOrigtnPnt(),
			FIRSTCOLUMNPOSITION,
			laOTData.getOrigtnPnt().length());
		caRpt.nextLine();
		caRpt.nextLine();

		// Intermediate 
		caRpt.print(
			INTERMEDIATEPOINT,
			FIRSTCOLUMNPOSITION,
			INTERMEDIATEPOINT.length());
		caRpt.nextLine();

		// defect 10491 
		lvData.add(laOTData.getIntrmdtePnt1());
		lvData.add(laOTData.getIntrmdtePnt2());
		lvData.add(laOTData.getIntrmdtePnt3());

		int liCount = 0;

		for (int i = 0; i < 3; i++)
		{
			String lsData = ((String) lvData.elementAt(i)).trim();

			if (lsData.length() > 0)
			{
				caRpt.print(
					lsData,
					FIRSTCOLUMNPOSITION,
					lsData.length());
				caRpt.nextLine();
				liCount++;
			}
		}
		for (int i = liCount; i < 3; i++)
		{
			caRpt.nextLine();
		}
		// end defect 10491 

		caRpt.nextLine();

		// Destination 
		caRpt.print(
			DESTINATIONPOINT,
			FIRSTCOLUMNPOSITION,
			COLUMNLENGTH34);
		caRpt.nextLine();
		caRpt.print(
			laOTData.getDestPnt(),
			FIRSTCOLUMNPOSITION,
			laOTData.getDestPnt().length());
		caRpt.nextLine();
		caRpt.blankLines(2);
	}

	/**
	 * Print the Inventory and Fees Section.
	 * 
	 * @param avDataVector Vector
	 */
	private void printPayment(Vector avDataVector)
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
	 * Print the Vehicle Information Information.
	 * 
	 */
	private void printVehicleData()
	{
		caRpt.blankLines(28 - caRpt.getCurrX());

		//print VIN
		VehicleData laVehData =
			caCTData.getVehicleInfo().getVehicleData();

		caRpt.print(
			VINSTRING + laVehData.getVin(),
			FIRSTCOLUMNPOSITION,
			(VINSTRING + laVehData.getVin()).length());

		caRpt.nextLine();

		//print vehicle year and maker
		caRpt.print(
			YRMAKESTRING
				+ laVehData.getVehModlYr()
				+ SLASH
				+ laVehData.getVehMk(),
			FIRSTCOLUMNPOSITION,
			COLUMNLENGTH34);

		//print vehicle style
		caRpt.print(
			BDYSTYLESTRING + laVehData.getVehBdyType(),
			SECONDCOLUMNPOSITION - 2,
			COLUMNLENGTH34);
		caRpt.blankLines(3);

		// defect 10491 
		if (caCTData.getTransCode().equals(TransCdConstant.PRMDUP))
		{
			String lsLabel = PERMITFEESPAID;

			String lsPrmtIssuanceId = caPrmtData.getPrmtIssuanceId();
			TransactionIdData laTransIdData =
				new TransactionIdData(lsPrmtIssuanceId);
			String lsIssueDate =
				laTransIdData.getTransactionDateMMDDYYYY();
			if (lsIssueDate.length() > 0)
			{
				lsIssueDate = " (" + lsIssueDate + ")";

			}
			lsLabel = lsLabel + lsIssueDate + ": ";
			Dollar laDollar = caPrmtData.getPrmtPdAmt();

			if (laDollar == null)
			{
				laDollar = new Dollar("0.00");
			}

			caRpt.print(lsLabel, FIRSTCOLUMNPOSITION, lsLabel.length());
			caRpt.print(laDollar.printDollar());
			caRpt.blankLines(3);
		}
		// end defect 10491 
	}
}
