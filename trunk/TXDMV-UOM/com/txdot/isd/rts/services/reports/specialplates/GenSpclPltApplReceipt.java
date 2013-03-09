package com.txdot.isd.rts.services.reports.specialplates;

import java.util.Vector;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.OrganizationNumberCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ReceiptProperties;
import com.txdot.isd.rts.services.reports.ReceiptTemplate;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * GenSpclPltApplReceipt.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	03/20/2007	Create class.  
 * 							defect 9126 Ver Special Plates  
 * Jeff S.		03/29/2007	Changes to handle printing a plate sticker
 * 							for dealer plates.
 * 							modify formatReceipt(), 
 * 							defect 9145 Ver Special Plates
 * Jeff S.		03/30/2007	Changes to handle printing of mfg inv.
 * 							modify printInventoryAndFees(), 
 * 								printSpclPltData()
 * 							defect 9145 Ver Special Plates
 * K Harrell	03/31/2007	Add'l Work 
 * 							defect 9085 Ver Special Plates 
 * Jeff S.		04/06/2007	More manufacturing notation work.
 * 							modify printInventoryAndFees()
 * 							defect 9145 Ver Special Plates
 * K Harrell	04/15/2007	Moved addition of Sticker to Receipt from 
 * 							FRMSPL002. 
 * 							deleted queryData(), main() for future 
 * 							enhancement
 * 							modify formatReceipt(),printInventoryAndFees()    
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/22/2007	Added Status if Revise and MfgStatus = 
 * 							Unacceptable || Reserve
 * 							modify printSpclPltData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/01/2007	Use UtilityMethods.isSPAPPL() vs. check for
 * 							SPAPPL 
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/01/2007	Removed PLPDlrPlt Processing
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/19/2007	Was not printing all payments
 * 							modify printPayment()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/09/2007	Only print "Unacceptable" or "Reserved" if
 * 							SPREV or SPDEL 
 * 							modify printSpclPltData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	09/17/2007	Initialize cbShouldStickersPrint for correct 
 * 							paper selection 
 * 							modify formatReceipt() 
 * 							defect 9318 Ver Special Plates
 * K Harrell	06/16/2010	modify to handle Special Plate Remarks
 * 							add PLTNOTATIONSTRING
 * 							add printNotations()
 * 							modify formatReceipt()
 * 							defect 10507 Ver 6.5.0   
 * Min Wang		10/08/2010	Allow printing of the Max length of the price. 
 * 							modify ITEMPRICELENGTH  
 * 							defect 10596 Ver 6.6.0      
 * ---------------------------------------------------------------------
 */
/**
 *
 * Create receipt for Special Plate Application transaction.
 * This receipt is also used for Special Plate Renewal.
 *
 * @version	6.6.0 			10/08/2010
 * @author	Bill Hargrove
 * @author  Jeff Seifert 
 * @author  Kathy Harrell 
 * <br>Creation Date:		03/20/2007 10:30:00
 */
public class GenSpclPltApplReceipt extends ReceiptTemplate
{
	private static final int COLUMN = 5;
	private static final int DOLLARSIGNPOSITION = COLUMN + 75;
	private static final int FEEASSESSEDLENGTH = 30;
	private static final int FEEASSESSEDPOSITION = COLUMN + 44;
	private static final int FIRSTCOLUMNPOSITION = COLUMN;
	// defect 10596
	//private static final int ITEMPRICELENGTH = 12;
	private static final int ITEMPRICELENGTH = 15;
	// end defect 10596
	private static final int LENGTH_1 = 1;
	private static final int MONEYPOSITION = COLUMN + 77;
	private static final int PAYMENTHEADERPOSITION = COLUMN + 50;
	private static final int PAYMENTPOSITION = COLUMN + 55;
	private static final int STARTLINEMESSAGES = 70;
	private static final int TOTALPOSITION = COLUMN + 65;
	private static final int YEARLENGTH = 4;
	private static final int YEARPOSITION = COLUMN + 36;

	private static final String DOLLARSIGN = "$";
	private static final String FEEASSESSEDSTRING = "FEES ASSESSED";
	private static final String FOLDMARKSTRING = "..";
	private static final String INVITMSTRING = "INVENTORY ITEM(S)";
	private static final String MFGPLTNO = "MFG PLATE NO:";
	private static final String ORGNO = "ORGANIZATION:";
	private static final String PAYMENTCHANGEDUESTRING = "CHANGE DUE";
	private static final String PAYMENTSTRING =
		"METHOD OF PAYMENT AND PAYMENT AMOUNT:";
	private static final String PAYMENTTOTALSTRING =
		"TOTAL AMOUNT PAID ";
	private static final String PLATETYPE = "PLATE TYPE:";
	private static final String RENEWAL_WARNING =
		"    WARNING: This renewal receipt does not represent vehicle registration renewal.";
	private static final String TOTALSTRING = "TOTAL";
	private static final String YEARSTRING = "YR";
	private static final String STATUS = "STATUS:";
	
	// defect 10507
	private static final String PLTNOTATIONSTRING =
		"SPECIAL PLATE RECORD NOTATIONS";
	// end defect 10507 

	// Object 
	CompleteTransactionData caCTData = null;
	SpecialPlatesRegisData caSpclPltRegisData = null;

	/**
	 * GenSpclPltApplReceipt constructor
	 */
	public GenSpclPltApplReceipt()
	{
		super();
	}

	/**
	 * GenSpclPltApplReceipt constructor
	 */
	public GenSpclPltApplReceipt(
		String asRcptString,
		ReceiptProperties asRcptProperties)
	{
		super(asRcptString, asRcptProperties);
	}

	/**
	 * This abstract method must be implemented in all subclasses
	 * 
	 * @param avResults Vector
	 */
	public void formatReceipt(Vector avResults)
	{
		caCTData = (CompleteTransactionData) avResults.elementAt(0);
		caSpclPltRegisData =
			caCTData.getVehicleInfo().getSpclPltRegisData();

		// defect 9318 
		// Initialize for correct paper selection  
		cbShouldStickersPrint =
			caCTData.getStickers() != null
				&& caCTData.getStickers().size() > 0;
		// end defect 9318

		try
		{
			generateReceiptHeader(avResults);
		}
		catch (Exception aeEx)
		{
			new RTSException(RTSException.JAVA_ERROR, aeEx)
				.displayError();
		}

		sectionStartingPoint(16);
		printSpclPltData();
		sectionStartingPoint(this.caRpt.getCurrX() + 5);

		// Inventory and Fees 
		printInventoryAndFees();

		// Process Notations and Payment
		sectionStartingPoint(this.caRpt.getCurrX() + 1);

		printPayment(avResults);

		// defect 10507 
		printNotations();
		// end defect 10507

		if (caCTData.getTransCode().equals(TransCdConstant.SPRNW))
		{
			lineBreakHandling(15);
			caRpt.print(RENEWAL_WARNING, 1, RENEWAL_WARNING.length());
		}
		// Process Messages
		sectionStartingPoint(STARTLINEMESSAGES - 1);

		// Handle fold lines
		caRpt.print("&a120H&a2196V" + FOLDMARKSTRING);
		caRpt.print("&a120H&a4752V" + FOLDMARKSTRING);

		// defect 9318 
		if (cbShouldStickersPrint)
		{
			generateStickers(caCTData);
		}
		// end defect 9318 
	}
	/**
	 * This method prints the blank lines requested by the caller.
	 * It also handles positioning and printing of fold marks required
	 * by certain receipts.
	 * 
	 * @param aiNumOfLines int
	 * @param asTransCode String
	 */
	public void lineBreakHandling(int aiNumOfLines)
	{
		for (int i = 0; i < aiNumOfLines; i++)
		{
			caRpt.blankLines(1);
		}
	}

	/**
	 * Print the Inventory and Fees Areas.
	 * 
	 * @param aaTransData CompleteTransactionData
	 */
	public void printInventoryAndFees()
	{
		String lsMfgPltCd = CommonConstant.STR_SPACE_EMPTY;

		// defect 9145
		// Changed how we determine if manufactured
		if (caSpclPltRegisData != null
			&& caSpclPltRegisData.getMFGStatusCd() != null
			&& caSpclPltRegisData.getMFGStatusCd().equals(
				SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD))
		{
			ProcessInventoryData laProInvData =
				new ProcessInventoryData();
			laProInvData.setItmCd(caSpclPltRegisData.getRegPltCd());
			laProInvData.setInvItmNo(caSpclPltRegisData.getRegPltNo());
			lsMfgPltCd = caSpclPltRegisData.getRegPltCd();
			laProInvData.setInvItmYr(caSpclPltRegisData.getInvItmYr());
			if (caCTData.getAllocInvItms() == null)
			{
				caCTData.setAllocInvItms(new Vector());
			}
			caCTData.getAllocInvItms().addElement(laProInvData);
		}
		// end defect 9145
		if (caCTData.getAllocInvItms() != null
			&& caCTData.getAllocInvItms().size() > 0)
		{
			caRpt.print(
				INVITMSTRING,
				FIRSTCOLUMNPOSITION,
				INVITMSTRING.length());
			caRpt.print(YEARSTRING, YEARPOSITION - 5, YEARLENGTH);
		}
		caRpt.print(
			FEEASSESSEDSTRING,
			FEEASSESSEDPOSITION,
			FEEASSESSEDSTRING.length());
		lineBreakHandling(1);
		int liInvCount = 0;

		// index through the inventory vector
		int liFeeCount = 0;

		// index through the Fees vector
		boolean lbKeepLooping = true;

		// loop controller.  keep looping until it is set off.
		Dollar laFeeTotal = new Dollar(0);
		int liToday = new RTSDate().getYYYYMMDDDate();
		boolean lbPrintMfgNotation = false;
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
				// Get the Item Description from cache
				ItemCodesData laItmCode =
					ItemCodesCache.getItmCd(laInvData.getItmCd());
				String lsDesc = laItmCode.getItmCdDesc();
				if (laItmCode.getItmCd().trim().equals(lsMfgPltCd))
				{
					//lsDesc = lsDesc + " (Mfg)";
					lbPrintMfgNotation = true;
				}
				caRpt.print(
					lsDesc,
					FIRSTCOLUMNPOSITION,
					lsDesc.length());
				caRpt.print(
					laInvData.getInvItmYr() == 0
						? CommonConstant.STR_SPACE_EMPTY
						: String.valueOf(laInvData.getInvItmYr()),
					YEARPOSITION - 5,
					YEARLENGTH);
			}
			liInvCount = liInvCount + 1;
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
				if (laFeeData.getDesc() == null
					|| laFeeData.getDesc().trim().equals(
						CommonConstant.STR_SPACE_EMPTY))
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
				caRpt.rightAlign(
					laFeeData.getItemPrice().printDollar().substring(1),
					MONEYPOSITION,
					ITEMPRICELENGTH);
				liFeeCount = liFeeCount + 1;
				laFeeTotal = (laFeeTotal.add(laFeeData.getItemPrice()));
			}
			lineBreakHandling(1);

			// defect 9145
			// Printing the mfg notation
			if (lbPrintMfgNotation)
			{
				printManufacturingNotation(FIRSTCOLUMNPOSITION);
				lbPrintMfgNotation = false;
			}
			// end defect 9145

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
				// Remove element for correct processing in Transaction
				if (!lsMfgPltCd.equals(""))
				{
					caCTData.getAllocInvItms().removeElementAt(
						caCTData.getAllocInvItms().size() - 1);
				}
			} // end do while	
		}

		// Print Total if there are any records
		lineBreakHandling(1);
		caRpt.print(
			TOTALSTRING,
			TOTALPOSITION - 3,
			TOTALSTRING.length());
		caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
		caRpt.rightAlign(
			laFeeTotal.printDollar().substring(1),
			MONEYPOSITION,
			ITEMPRICELENGTH);
		lineBreakHandling(1);
	}

	/**
	 * Print Notations 
	 * 
	 */
	private void printNotations()
	{
		CompleteTransactionData laCTData =
			(CompleteTransactionData) UtilityMethods.copy(caCTData);

		// We need a "robust" MFVehicleData object for 
		//   indicators to work.
		// Else, can check for nulls in IndicatorLookup!     
		laCTData.getVehicleInfo().setRegData(new RegistrationData());
		laCTData.getVehicleInfo().setTitleData(new TitleData());
		laCTData.getVehicleInfo().setVehicleData(new VehicleData());

		Vector lvNotationsData = buildNotationsVector(laCTData);

		if (!lvNotationsData.isEmpty())
		{
			caRpt.nextLine();
			caRpt.nextLine();
			caRpt.print(
				PLTNOTATIONSTRING,
				FIRSTCOLUMNPOSITION,
				PLTNOTATIONSTRING.length());
			caRpt.nextLine();

			for (int i = 0; i < lvNotationsData.size(); i++)
			{
				String lsRemark = (String) lvNotationsData.elementAt(i);
				caRpt.print(
					lsRemark,
					FIRSTCOLUMNPOSITION,
					lsRemark.length());
				caRpt.nextLine();

			}

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
		Dollar ldZeroDollar = new Dollar(0);
		Dollar ldPaymentTotal = new Dollar(0);
		Dollar ldChangeBackAmt = new Dollar(0);
		int liPaymentCount = 0;

		// print payment header if there are any payment objects
		if (lvPaymentData.size() > 0)
		{
			caRpt.print(
				PAYMENTSTRING,
				PAYMENTHEADERPOSITION + 2,
				PAYMENTSTRING.length());

			lineBreakHandling(1);

			while (liPaymentCount < lvPaymentData.size())
			{
				TransactionPaymentData laPaymentData =
					(TransactionPaymentData) lvPaymentData.elementAt(
						liPaymentCount);

				caRpt.print(
					laPaymentData.getPymntType(),
					PAYMENTPOSITION + 6,
					laPaymentData.getPymntType().length());

				// print the check number if it exists
				if (laPaymentData.getPymntCkNo() != null)
				{
					caRpt.print(" #" + laPaymentData.getPymntCkNo());
				}

				caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);

				caRpt.rightAlign(
					laPaymentData
						.getPymntTypeAmt()
						.printDollar()
						.substring(
						1),
					MONEYPOSITION,
					ITEMPRICELENGTH);

				ldPaymentTotal =
					(ldPaymentTotal
						.add(laPaymentData.getPymntTypeAmt()));

				if ((laPaymentData.getChngDue() != null)
					&& !laPaymentData.getChngDue().equals(ldZeroDollar))
				{
					ldChangeBackAmt = laPaymentData.getChngDue();
				}

				++liPaymentCount;
				caRpt.nextLine();
			} // end while 

			TransactionPaymentData laPaymentData =
				(TransactionPaymentData) lvPaymentData.elementAt(
					lvPaymentData.size() - 1);

			// If Credit Card Fee, will be last element in vector
			if (laPaymentData.isCreditCardFee())
			{
				String lsCreditCardFeeMessage =
					formatCreditCardFeeMessage();
				caRpt.rightAlign(
					lsCreditCardFeeMessage,
					MONEYPOSITION - 5,
					lsCreditCardFeeMessage.length());
				caRpt.nextLine();
			}

			// Print the Total Payment Line, Even if Zero 
			caRpt.nextLine();
			caRpt.print(
				PAYMENTTOTALSTRING,
				PAYMENTPOSITION + 2,
				PAYMENTTOTALSTRING.length());
			caRpt.print(DOLLARSIGN, DOLLARSIGNPOSITION, LENGTH_1);
			caRpt.rightAlign(
				ldPaymentTotal.printDollar().substring(1),
				MONEYPOSITION,
				ITEMPRICELENGTH);
			caRpt.nextLine();

			// Print out the Change Due Line
			if (!ldChangeBackAmt.equals(ldZeroDollar))
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
	 * Print the Special Plate data.
	 * 
	 * @param aaTransData CompleteTransactionData
	 */
	public void printSpclPltData()
	{
		int liStartColSpclPltData;
		liStartColSpclPltData =
			FIRSTCOLUMNPOSITION
				+ Math.max(PLATETYPE.length(), MFGPLTNO.length())
				+ 1;
		caRpt.print(PLATETYPE, FIRSTCOLUMNPOSITION, PLATETYPE.length());
		caRpt.print(
			getCachePlateTypeDesc(caSpclPltRegisData.getRegPltCd()),
			liStartColSpclPltData,
			getCachePlateTypeDesc(caSpclPltRegisData.getRegPltCd())
				.length());
		caRpt.nextLine();
		caRpt.print(ORGNO, FIRSTCOLUMNPOSITION, ORGNO.length());
		String lsOrgName =
			OrganizationNumberCache.getOrgName(
				caSpclPltRegisData.getRegPltCd(),
				caSpclPltRegisData.getOrgNo());
		caRpt.print(
			lsOrgName,
			liStartColSpclPltData,
			lsOrgName.length());
		caRpt.nextLine();
		// defect 9145
		// Created a constant for mfg plate no
		caRpt.print(MFGPLTNO, FIRSTCOLUMNPOSITION, MFGPLTNO.length());
		// end defect 9145
		caRpt.print(
			caSpclPltRegisData.getMfgPltNo(),
			liStartColSpclPltData,
			caSpclPltRegisData.getMfgPltNo().length());
		caRpt.nextLine();

		// On Revise or Delete, print Status if Unacceptable or Reserve
		// defect 9237
		// Only Print for SPREV or SPDEL  (not SPUNAC, SPRSRV)
		if ((caCTData.getTransCode().equals(TransCdConstant.SPREV)
			|| caCTData.getTransCode().equals(TransCdConstant.SPDEL))
			&& (caSpclPltRegisData
				.getMFGStatusCd()
				.equals(SpecialPlatesConstant.RESERVE_MFGSTATUSCD)
				|| caSpclPltRegisData.getMFGStatusCd().equals(
					SpecialPlatesConstant.UNACCEPTABLE_MFGSTATUSCD)))
		{
			caRpt.nextLine();
			String lsStatus =
				(String) SpecialPlatesConstant.INTERPRET_STATUSCDS.get(
					caSpclPltRegisData.getMFGStatusCd().trim());
			caRpt.print(STATUS, FIRSTCOLUMNPOSITION, STATUS.length());
			caRpt.print(
				lsStatus,
				liStartColSpclPltData,
				lsStatus.length());
			caRpt.nextLine();
		}
		// end defect 9237 
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
		// print those lines. use linebreakhandling to get foldmarks if needed.
		for (int i = 0; i < aiLineNumber; i++)
		{
			lineBreakHandling(1);
		}
	}
}
