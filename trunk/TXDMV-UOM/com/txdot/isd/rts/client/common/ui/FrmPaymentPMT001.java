package com.txdot.isd.rts.client.common.ui;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.CreditCardFeesCache;
import com.txdot.isd.rts.services.cache.PaymentTypeCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.CompleteTransactionData;
import com.txdot.isd.rts.services.data.CreditCardFeeData;
import com.txdot.isd.rts.services.data.PaymentTypeData;
import com.txdot.isd.rts.services.data.TransactionPaymentData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmPaymentPMT001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Govindappa 10/15/2002  CQU100004869 Fix - part of PCR41 - Added a 
 *							check for blank in populateData() for 
 *							txtAmtCharge field before converting to 
 *							Dollar.
 * K Salvi		10/15/2002	CQU100004869 Fix - part of PCR41 - added 
 *							checks to prevent adding a credit card fee 
 *							record when no credit card fee as assessed.
 * K Salvi		10/02/2002	CQU100004814 Fix - part of PCR41 - added 
 *							check to include credit card fee only if a 
 *							positive amount is charged via credit card.
 * K Salvi		10/02/2002	CQU100004806 Fix - part of PCR41 - added 
 *							check for blank amount when type is "CHARGE".
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking() 
 * M Wang	   	07/19/2002	(CQU100004470) Relined  bottom of the payment 
 *							on screen after PCR25.
 * MAbs		   	09/17/2002	PCR 41 Integration
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the user 
 *							help guide so had to make changes in 
 *							actionPerformed().
 *							add csYrToModify
 * Min Wang		05/06/2004  Fix double cursors when part of the payment 
 *							is credit card.
 *							modify displayInsufficientFundsError() 
 *							defect 7050 Ver 5.1.6
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * Ray Rowehl	02/11/2005	Make changes to allow processing on Java 1.4
 * 							modify actionPerformed(), 
 * 								showCreditCardInfo()
 * 							defect 7701 Ver 5.2.3
 * Ray Rowehl	02/14/2005	Code Cleanup
 * 							defect 7885 Ver 5.2.3
 * Ray Rowehl	03/17/2005	Remove FocusManager setup
 * 							modify initialize()
 * 							defect 7885 Ver 5.2.3
 * T Pederson	03/31/2005	Removed setNextFocusableComponent.
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	05/26/2005	Corrected focus problems.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * S Johnston	06/27/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify getButtonPanel, keyPressed
 * 							defect 8240 Ver 5.2.3   
 * T Pederson	07/29/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/05/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	12/14/2005	Moved code from keyPressed() to focusLost() 
 * 							to properly handle tabbing logic.
 * 							modify focusLost(), keyPressed()
 * 							defect 7885 Ver 5.2.3 
 * Jeff S.		12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify populateComboBox()
 * 							defect 8479 Ver 5.2.3 
 * Jeff S.		12/20/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles arrowing.
 * 							remove keyPressed()
 * 							modify getradioCash(), getradioCheck(), 
 * 								initialize()
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	01/12/2006	Moved clearAllColor() in validateDollarField
 * 							modify validateDollarField()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	01/24/2006	Slight alignment for CheckNo heading
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	02/06/2006	Corrected focusLost errors
 * 							modify displayAmtFieldError(), 
 * 							displayInsufficientFundsError,
 * 							validateDollarField
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		06/26/2006	Change title to have 4 spaces to be 
 * 							consistant.
 * 							modify FRM_TITLE_PMT001
 * 							defect 8756 Ver 5.2.3
 * K Harrell	04/27/2007	Use CommonConstant.TXT_COMPLETE_TRANS_QUESTION
 * 							vs local variable for msg.  (Newline did not
 * 							work.
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates 
 * Min Wang		09/20/2010 	Accommodate new max lengh of input field for 
 * 							the payment.
 * 							modify gettxtAmt1(), gettxtAmt2(), 
 * 							gettxtAmt3(), gettxtAmt4(), MAX_AMOUNT
 * 							defect 10596 Ver 6.6.0
 * Min Wang		12/14/2010	The total remittance can not be greater than 
 * 							999,999,999.99
 * 							add cdMaxDollars, ERRMSG_ERROR, 
 * 							ERRMSG_TOTAL_REMITTANCE
 * 							modify reCalculateAndDisplayValidFields()
 * 							defect 10596 Ver 6.6.0
 * ---------------------------------------------------------------------
 */
/**
 * Frame for Screen Fees Due PMT001
 *
 * @version	6.6.0			10/14/2010
 * @author	Nancy Ting
 * <br>Creation Date:		06/26/2001 14:52:12
 */

public class FrmPaymentPMT001
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JRadioButton ivjradioCash = null;
	private JRadioButton ivjradioCheck = null;
	private JLabel ivjstcLblAmount = null;
	private JLabel ivjstcLblChangeDue = null;
	private JLabel ivjstcLblChangeType = null;
	private JLabel ivjstcLblCheckNo = null;
	private JLabel ivjstcLblEnterTheFollowing = null;
	private JLabel ivjstcLblPaymentCode = null;
	private JLabel ivjstcLblTotalDue = null;
	private JLabel ivjstcLblTotalRemittance = null;
	private JLabel ivjlblChangeDue = null;
	private JLabel ivjlblTotalDue = null;
	private JLabel ivjlblTotalRemittance = null;
	private JPanel ivjFrmPaymentPMT001ContentPane1 = null;
	private JComboBox ivjcbPymtCode1 = null;
	private JComboBox ivjcbPymtCode3 = null;
	private JComboBox ivjcbPymtCode4 = null;
	private RTSInputField ivjtxtAmt2 = null;
	private RTSInputField ivjtxtAmt3 = null;
	private RTSInputField ivjtxtAmt4 = null;
	private RTSInputField ivjtxtChkNo1 = null;
	private RTSInputField ivjtxtChkNo2 = null;
	private RTSInputField ivjtxtChkNo3 = null;
	private RTSInputField ivjtxtChkNo4 = null;
	private RTSButtonGroup caButtonGroup = null;
	private RTSInputField ivjtxtAmt1 = null;
	private RTSInputField caRTSInputFieldLastFocused = null;
	private JPanel ivjJPanel1 = null;
	private JComboBox ivjcbPymtCode2 = null;
	private JLabel ivjlblCreditRemaining = null;
	private JLabel ivjstclblCreditRemaining = null;
	private JLabel ivjstcLblCreditCardFee = null;
	private JComboBox ivjcbPymtCodeCharge = null;
	private RTSInputField ivjtxtAmtCharge = null;
	private RTSInputField ivjtxtChkNoCharge = null;

	// boolean 
	private boolean cbDisplayCredMsg;
	private boolean cbInit = false;
	private boolean cbShownChargeAddedMsg = false;

	// int 
	protected int ciChangeDuePaymentType = 0;
	protected int ciCurrentIndex = 0;

	// Objects
	private CompleteTransactionData caCompleteTransactionData;
	private CreditCardFeeData caCreditFeeInfo;
	protected Dollar cdDollarTotalDue = null;
	protected Dollar cdDollarTotalRemittance = null;
	protected Dollar cdDollarChangeDue =
		new Dollar(CommonConstant.STR_ZERO_DOLLAR);
	// defect 10596
	protected Dollar cdMaxDollars =
			new Dollar(999999999.99);
	// end defect 10596
	// Vector 
	protected Vector cvCode = null;
	protected Vector cvDescription = null;
	protected Vector cvTransactionPaymentData = null;

	// Constants
	// defect 10596
	//protected static final int MAX_AMOUNT = 100000;
	protected static final int MAX_AMOUNT = 1000000000;
	private static final String ERRMSG_ERROR = "ERROR!";
	private static final String ERRMSG_TOTAL_REMITTANCE =
	"The total remittance can not be greater than 999,999,999.99";
	// end defect 10596
	protected static final int DECIMAL_NUMBER = 2;
	public static final int CASH = 1;
	public static final int CHECK = 2;
	//default check box code
	protected int DEFAULT = CASH;

	private static final String DOT = CommonConstant.STR_PERIOD;
	public static final String COMBO_CHECK = "CHECK";
	public static final String COMBO_CASH = "CASH";
	public static final String COMBO_CHARGE = "CHARGE";
	private static final String DOLLAR_SIGN = "$";
	private static final String STR_ONE_HUNDRED_DOLLARS = "100.00";
	private static final Dollar ZERO_DOLLAR =
		new Dollar(CommonConstant.STR_ZERO_DOLLAR);

	// Text Constants 
	private final static String FRM_NAME_PMT001 = "FrmPaymentPMT001";
	private final static String FRM_TITLE_PMT001 = "Payment    PMT001";
	private final static String TXT_CRDT_CARD_FEE_ADDED =
		"Credit Card Fee has been added.";
	private final static String TXT_NO_CODE_WITH = "no code with ";
	private final static String TXT_NO_DESC_WITH = "no desc with ";
	private final static String TXT_TOT_REMIT = "Total Remittance:";
	private final static String TXT_TOT_DUE = "Total Due:";
	private final static String TXT_PAY_CD = "Payment Code";
	private final static String TXT_ENTER_FOLWNG =
		"Enter the following:";
	private final static String TXT_CRDT_REM = "Credit Remaining:";
	private final static String TXT_CRDT_CARD_FEE = "Credit Card Fee:";
	private final static String TXT_CHK_NO = "Check No";
	private final static String TXT_CHNG_TYPE = "Change Type:";
	private final static String TXT_CHNG_DUE = "Change Due:";
	private final static String TXT_AMOUNT = "Amount";
	private final static String TXT_ERROR = "error";
	private final static String TXT_NULL_PYMNT_TYPE =
		"Null in payment type";
	private final static String csMsgCTL001ChngDue = "Change Due: ";

	/**
	 * FrmPaymentPMT001 constructor.
	 */
	public FrmPaymentPMT001()
	{
		super();
		initialize();
	}

	/**
	 * FrmPaymentPMT001 constructor.
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmPaymentPMT001(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmPaymentPMT001 constructor.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmPaymentPMT001(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel/Help/Radio Buttons (Cash/Check) is 
	 * selected
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (aaAE.getSource() == getradioCash())
			{
				ciChangeDuePaymentType = CASH;
			}
			else if (aaAE.getSource() == getradioCheck())
			{
				ciChangeDuePaymentType = CHECK;
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				calculateCreditCardTotal();
				// defect 4907 
				if (getcbPymtCode1()
					.getSelectedItem()
					.equals(COMBO_CHARGE)
					|| getcbPymtCode2().getSelectedItem().equals(
						COMBO_CHARGE)
					|| getcbPymtCode3().getSelectedItem().equals(
						COMBO_CHARGE)
					|| getcbPymtCode4().getSelectedItem().equals(
						COMBO_CHARGE))
				{
					displayCreditFeeAddedMsg();
				}
				// end defect 4907

				// defect 7885
				// Check to see if enter was pressed while on one of  
				// the amount entry fields
				caRTSInputFieldLastFocused = null;
				if (gettxtAmt1().isFocusOwner())
				{
					caRTSInputFieldLastFocused = ivjtxtAmt1;
				}
				else if (gettxtAmt2().isFocusOwner())
				{
					caRTSInputFieldLastFocused = ivjtxtAmt2;
				}
				else if (gettxtAmt3().isFocusOwner())
				{
					caRTSInputFieldLastFocused = ivjtxtAmt3;
				}
				else if (gettxtAmt4().isFocusOwner())
				{
					caRTSInputFieldLastFocused = ivjtxtAmt4;
				}
				// Perform handleRowElements here instead in the
				// focusGained method 
				if (caRTSInputFieldLastFocused != null)
				{
					handleRowElements(caRTSInputFieldLastFocused, true);
				}
				// end defect 7885

				boolean lbContinue = handleRowElements(false);

				if (lbContinue)
				{
					StringBuffer lsMsgStr = new StringBuffer();
					if (cdDollarChangeDue.compareTo(ZERO_DOLLAR) == 1)
					{
						displayInsufficientFundsError(
							(aaAE.getSource() instanceof RTSInputField)
								? (RTSInputField) aaAE.getSource()
								: null);
						return;
					}
					else if (
						(cdDollarChangeDue.compareTo(ZERO_DOLLAR)
							== -1))
					{
						//Pop up CTL001
						lsMsgStr.append(csMsgCTL001ChngDue);
						lsMsgStr.append(
							ZERO_DOLLAR
								.subtract(cdDollarChangeDue)
								.printDollar());
					}
					lsMsgStr.append(" "+CommonConstant.TXT_COMPLETE_TRANS_QUESTION); 

					if (isVisible())
					{
						RTSException leRTSExMsg =
							new RTSException(
								RTSException.CTL001,
								lsMsgStr.toString(),
								CommonConstant.STR_SPACE_EMPTY);
						int liReturnValue =
							leRTSExMsg.displayError(this);
						if (liReturnValue == RTSException.YES)
						{
							Vector lvVector = new Vector();
							//PCR25

							// defect 4869
							// added check for credit 
							// card fee before adding extra
							// record to transaction vector.
							CreditCardFeeData laCreditFeeData =
								CreditCardFeesCache
									.getCurrentCreditCardFees(
									SystemProperty
										.getOfficeIssuanceNo(),
									RTSDate.getCurrentDate());
							if (laCreditFeeData != null
								&& gettxtAmtCharge().isVisible()
								&& (
									new Dollar(
										gettxtAmtCharge()
											.getText()
											.trim()))
											.compareTo(
									ZERO_DOLLAR)
									> 0)
							{
								caCreditFeeInfo.setItmPrice(
									new Dollar(
										gettxtAmtCharge()
											.getText()
											.trim()));
								caCompleteTransactionData
									.setCreditCardFeeData(
									caCreditFeeInfo);

							}
							lvVector.addElement(
								caCompleteTransactionData);
							lvVector.addAll(populateData());
							// defect 7701
							leRTSExMsg = null;
							// end defect 7701
							getController().processData(
								AbstractViewController.ENTER,
								lvVector);
						}
						else
						{
							// defect 7701
							leRTSExMsg = null;
							// end defect 7701
							return;
						}
					}

				}

			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					new CompleteTransactionData());
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{

				String lsTransCd =
					((CompleteTransactionData) getController()
						.getData())
						.getTransCode();

				if (lsTransCd.equals(TransCdConstant.RGNCOL))
				{
					RTSHelp.displayHelp(RTSHelp.PMT001A);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.PMT001B);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			return;
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Calculate Credit Card Total.
	 */
	private void calculateCreditCardTotal()
	{
		// defect 4814
		// Added to indicate whether a credit card fee 
		// should be charged or not.
		boolean lbDisplayCreditFeeField = false;

		// defect 4814
		// Compute the total amount being charged via
		// credit card.
		Dollar ldChargeAmt = ZERO_DOLLAR;
		if (getcbPymtCode1().isVisible()
			&& getcbPymtCode1().getSelectedItem().equals(COMBO_CHARGE)
			&& !gettxtAmt1().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			ldChargeAmt =
				ldChargeAmt.add(
					new Dollar(gettxtAmt1().getText().trim()));
		}
		if (getcbPymtCode2().isVisible()
			&& getcbPymtCode2().getSelectedItem().equals(COMBO_CHARGE)
			&& !gettxtAmt2().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			ldChargeAmt =
				ldChargeAmt.add(
					new Dollar(gettxtAmt2().getText().trim()));
		}
		if (getcbPymtCode3().isVisible()
			&& getcbPymtCode3().getSelectedItem().equals(COMBO_CHARGE)
			&& !gettxtAmt3().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			ldChargeAmt =
				ldChargeAmt.add(
					new Dollar(gettxtAmt3().getText().trim()));
		}
		if (getcbPymtCode4().isVisible()
			&& getcbPymtCode4().getSelectedItem().equals(COMBO_CHARGE)
			&& !gettxtAmt4().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			ldChargeAmt =
				ldChargeAmt.add(
					new Dollar(gettxtAmt4().getText().trim()));
		}

		// defect 4814
		// if a positive amount is being charged via
		// credit card, set the flag to true.

		if (ldChargeAmt.compareTo(ZERO_DOLLAR) > 0)
		{
			lbDisplayCreditFeeField = true;
		}

		// defect 4814
		// calculate and modify fee if the flag is set.
		if (lbDisplayCreditFeeField)
		{
			if (caCreditFeeInfo.isPercentage())
			{

				Dollar ldDivide = new Dollar(STR_ONE_HUNDRED_DOLLARS);
				Dollar ldMultAmt =
					caCreditFeeInfo.getItmPrice().divideNoRound(
						ldDivide);
				Dollar ldCreditChargeAmt =
					ldMultAmt.multiply(ldChargeAmt);

				gettxtAmtCharge().setText(ldCreditChargeAmt.toString());
				reCalculateAndDisplayValidFields();
			}
			else
			{
				gettxtAmtCharge().setText(
					caCreditFeeInfo.getItmPrice().toString());
			}
		}
		// defect 4814
		// Set the fee to zero if the flag is not set.
		else
		{
			if (!gettxtAmtCharge()
				.getText()
				.equals(ZERO_DOLLAR.toString()))
			{
				gettxtAmtCharge().setText(ZERO_DOLLAR.toString());
			}
		}

		// defect 4814
		// display or hide the credit card fee according
		// to the flag.
		showCreditCardInfo(lbDisplayCreditFeeField);

	}

	/**
	 * Create the new TransactionPaymentData, populate, add it to 
	 * the vector, increment the count
	 * 
	 * @param aiCurrentIndex int
	 * @param abFirstRow boolean
	 * @return TransactionPaymentData
	 */
	protected TransactionPaymentData createNewPaymentData(
		int aiCurrentIndex,
		boolean abFirstRow)
	{
		TransactionPaymentData laTransactionPaymentData =
			new TransactionPaymentData();
		laTransactionPaymentData.setPymntNo(
			String.valueOf(aiCurrentIndex));
		laTransactionPaymentData.setPymntCkNo(
			CommonConstant.STR_SPACE_EMPTY);
		laTransactionPaymentData.setPymntTypeAmt(ZERO_DOLLAR);
		if (abFirstRow)
		{
			laTransactionPaymentData.setPymntTypeCd(CHECK);
		}
		else
		{
			laTransactionPaymentData.setPymntTypeCd(DEFAULT);
		}
		return laTransactionPaymentData;
	}

	/**
	 * Display Field Validation Error Message Box
	 * 
	 * @param aaRTSInputField Component 
	 */
	protected void displayAmtFieldError(Component aaRTSInputField)
	{
		RTSException leRTSExHolder = new RTSException(150);

		if (aaRTSInputField instanceof RTSInputField)
		{
			RTSException leRTSEx = new RTSException();
			leRTSEx.addException(
				leRTSExHolder,
				(RTSInputField) aaRTSInputField);
			//defect 7885
			// Moved requestFocus here so focusLost won't get called again
			leRTSEx.getFirstComponent().requestFocus();
			//end defect 7885
			leRTSEx.displayError(this);
		}
		else
		{
			leRTSExHolder.displayError(this);
			gettxtAmt1().requestFocus();
		}
	}

	/**
	 * Display credit fee added message
	 */
	private void displayCreditFeeAddedMsg()
	{
		if (!cbShownChargeAddedMsg
			&& !caCreditFeeInfo.getItmPrice().equals(ZERO_DOLLAR))
		{
			// Make sure that an amount is entered for each visible 
			// amount field which has a type "CHARGE".
			if ((gettxtAmt1().isVisible()
				&& (gettxtAmt1().getText().trim().length() == 0
					|| (
						new Dollar(
							gettxtAmt1().getText().trim())).compareTo(
						ZERO_DOLLAR)
						<= 0))
				|| (gettxtAmt2().isVisible()
					&& (gettxtAmt2().getText().trim().length() == 0
						|| (
							new Dollar(
								gettxtAmt2()
									.getText()
									.trim()))
									.compareTo(
							ZERO_DOLLAR)
							<= 0))
				|| (gettxtAmt3().isVisible()
					&& (gettxtAmt3().getText().trim().length() == 0
						|| (
							new Dollar(
								gettxtAmt3()
									.getText()
									.trim()))
									.compareTo(
							ZERO_DOLLAR)
							<= 0))
				|| (gettxtAmt4().isVisible()
					&& (gettxtAmt4().getText().trim().length() == 0
						|| (
							new Dollar(
								gettxtAmt4()
									.getText()
									.trim()))
									.compareTo(
							ZERO_DOLLAR)
							<= 0)))
			{
				// if an amount field is visible and it does not 
				// contain an amount
				// do not pop up the fee added message.
				return;
			}
			showCreditCardInfo(true);

			new RTSException(
				RTSException.INFORMATION_MESSAGE,
				TXT_CRDT_CARD_FEE_ADDED,
				CommonConstant.STR_SPACE_EMPTY).displayError(
				this);
			cbShownChargeAddedMsg = true;
			this.repaint();
		}
	}

	/**
	 * Display Field Validation Error Message Box
	 * 
	 * @param aaRTSInputField Component
	 * @throws RTSException
	 */
	protected void displayInsufficientFundsError(Component aaRTSInputField)
		throws RTSException
	{
		RTSException leRTSExHolder = new RTSException(131);
		RTSInputField laRTSInputField = performFwdRowMgmt(true);

		if (laRTSInputField != null)
		{
			RTSException leRTSEx = new RTSException();
			leRTSEx.addException(leRTSExHolder, laRTSInputField);
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			this.repaint();
		}
	}

	/**
	 * Display Limit Warning if selected is CHECK and it is a field 
	 * validation
	 * 
	 * @param aaRTSInputField Component
	 * @throws RTSException
	 */
	protected void displayLimitExceedWarning(Component aaRTSInputField)
		throws RTSException
	{
		//display only if current remittance is more than due
		if (getCurrentRemittance().compareTo(cdDollarTotalDue) == 1)
		{
			Dollar laChangeDue = ZERO_DOLLAR;

			//normalize the change due
			if (cdDollarChangeDue.compareTo(laChangeDue) == 1)
			{
				laChangeDue = cdDollarChangeDue;
			}
			else if (cdDollarChangeDue.compareTo(laChangeDue) == -1)
			{
				laChangeDue = laChangeDue.subtract(cdDollarChangeDue);
			}

			if (aaRTSInputField == gettxtAmt1()
				&& getCodeFromDesc(
					(String) getcbPymtCode1().getSelectedItem())
					== CHECK
				&& !gettxtAmt1().getText().trim().equals(
					CommonConstant.STR_SPACE_EMPTY)
				&& new Dollar(gettxtAmt1().getText()).compareTo(
					laChangeDue)
					== 1)
			{
				RTSException leRTSEx = new RTSException(387);
				leRTSEx.displayError(this);

			}
			else if (
				aaRTSInputField == gettxtAmt2()
					&& getCodeFromDesc(
						(String) getcbPymtCode2().getSelectedItem())
						== CHECK
					&& new Dollar(gettxtAmt2().getText()).compareTo(
						laChangeDue)
						== 1)
			{
				RTSException leRTSEx = new RTSException(387);
				leRTSEx.displayError(this);

			}
			else if (
				aaRTSInputField == gettxtAmt3()
					&& getCodeFromDesc(
						(String) getcbPymtCode3().getSelectedItem())
						== CHECK
					&& new Dollar(gettxtAmt3().getText()).compareTo(
						laChangeDue)
						== 1)
			{
				RTSException leRTSEx = new RTSException(387);
				leRTSEx.displayError(this);

			}
			else if (
				aaRTSInputField == gettxtAmt4()
					&& getCodeFromDesc(
						(String) getcbPymtCode4().getSelectedItem())
						== CHECK
					&& new Dollar(gettxtAmt4().getText()).compareTo(
						laChangeDue)
						== 1)
			{
				RTSException leRTSEx = new RTSException(387);
				leRTSEx.displayError(this);
			}
		}
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		// defect 7885
		//try
		//{
		if (!aaFE.isTemporary())
		{
			// This is needed to set focus on payment entry field
			// when frame first comes up
			if (!cbInit)
			{
				gettxtAmt1().requestFocus();
				cbInit = true;
			}
		}
		//if (aaFE.getSource() instanceof RTSInputField)
		//{
		//	caRTSInputFieldLastFocused2 =
		//		(RTSInputField) aaFE.getSource();
		//}

		//if (!cbInit)
		//{
		//	if (aaFE.getSource() == gettxtAmt1())
		//	{
		//		getcbPymtCode1().setVisible(true);
		//		gettxtAmt1().requestFocus();
		//		cbInit = true;
		//	}
		//}

		// moved to focusLost
		//if (caRTSInputFieldLastFocused != null)
		//{
		//	handleRowElements(caRTSInputFieldLastFocused, true);
		//}
		//}
		//}
		//catch (RTSException leRTSEx)
		//{
		//	leRTSEx.displayError(this);
		//}
		// end defect 7885
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{

		if (!aaFE.isTemporary()
			&& (aaFE.getSource() instanceof RTSInputField)
			// Check to see if cancel button has been pushed
			&& !(aaFE.getOppositeComponent()
				== (getButtonPanel1().getBtnCancel())))
		{
			caRTSInputFieldLastFocused =
				(RTSInputField) aaFE.getSource();

			// defect 7885
			// added performFwdRowMgmt to display next row of payment 
			// entry if needed
			try
			{
				// moved this from focusGained and added isWorking 
				// check to know whether or not actionPerformed has
				// been executed. 
				if (caRTSInputFieldLastFocused != null && !isWorking())
				{
					handleRowElements(caRTSInputFieldLastFocused, true);

					// calculate and display credit card total
					// code moved from keyPressed()
					if (aaFE.getSource() == gettxtAmt1()
						|| aaFE.getSource() == gettxtAmt2()
						|| aaFE.getSource() == gettxtAmt3()
						|| aaFE.getSource() == gettxtAmt4())
					{
						if (!caCreditFeeInfo
							.getItmPrice()
							.equals(ZERO_DOLLAR)
							|| caCreditFeeInfo.isPercentage())
						{
							calculateCreditCardTotal();
						}
						// check if message should pop up
						if (aaFE.getSource() == gettxtAmt1())
						{
							if (getcbPymtCode1()
								.getSelectedItem()
								.equals(COMBO_CHARGE))
							{
								if ((gettxtAmt2().isVisible()
									&& getcbPymtCode2()
										.getSelectedItem()
										.equals(
										COMBO_CHARGE))
									|| (gettxtAmt3().isVisible()
										&& getcbPymtCode3()
											.getSelectedItem()
											.equals(
											COMBO_CHARGE))
									|| (gettxtAmt4().isVisible()
										&& getcbPymtCode4()
											.getSelectedItem()
											.equals(
											COMBO_CHARGE)))
								{
									// do not pop up
								}
								else
								{
									displayCreditFeeAddedMsg();
								}
							}
						}
						else if (aaFE.getSource() == gettxtAmt2())
						{
							if (getcbPymtCode2()
								.getSelectedItem()
								.equals(COMBO_CHARGE))
							{
								if ((gettxtAmt1().isVisible()
									&& getcbPymtCode1()
										.getSelectedItem()
										.equals(
										COMBO_CHARGE))
									|| (gettxtAmt3().isVisible()
										&& getcbPymtCode3()
											.getSelectedItem()
											.equals(
											COMBO_CHARGE))
									|| (gettxtAmt4().isVisible()
										&& getcbPymtCode4()
											.getSelectedItem()
											.equals(
											COMBO_CHARGE)))
								{
									// do not pop up
								}
								else
								{
									displayCreditFeeAddedMsg();
								}
							}
						}
						else if (aaFE.getSource() == gettxtAmt3())
						{
							if (getcbPymtCode3()
								.getSelectedItem()
								.equals(COMBO_CHARGE))
							{
								if ((gettxtAmt2().isVisible()
									&& getcbPymtCode2()
										.getSelectedItem()
										.equals(
										COMBO_CHARGE))
									|| (gettxtAmt1().isVisible()
										&& getcbPymtCode1()
											.getSelectedItem()
											.equals(
											COMBO_CHARGE))
									|| (gettxtAmt4().isVisible()
										&& getcbPymtCode4()
											.getSelectedItem()
											.equals(
											COMBO_CHARGE)))
								{
									// do not pop up
								}
								else
								{
									displayCreditFeeAddedMsg();
								}
							}
						}
						else if (aaFE.getSource() == gettxtAmt4())
						{
							if (getcbPymtCode4()
								.getSelectedItem()
								.equals(COMBO_CHARGE))
							{
								if ((gettxtAmt2().isVisible()
									&& getcbPymtCode2()
										.getSelectedItem()
										.equals(
										COMBO_CHARGE))
									|| (gettxtAmt3().isVisible()
										&& getcbPymtCode3()
											.getSelectedItem()
											.equals(
											COMBO_CHARGE))
									|| (gettxtAmt1().isVisible()
										&& getcbPymtCode1()
											.getSelectedItem()
											.equals(
											COMBO_CHARGE)))
								{
									// do not pop up
								}
								else
								{
									displayCreditFeeAddedMsg();
								}
							}
						}
						performFwdRowMgmt(false);
					}
					performFwdRowMgmt(false);
				}
			}
			catch (RTSException aeRTSEx)
			{
				aeRTSEx.displayError(this);
			}
			// end defect 7885
		}
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * @return ButtonPanel
	 */

	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(73, 387, 275, 54);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				// defect 8240
				// remove KeyListener from ButtonPanel
				// ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				// ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 8240
				ivjButtonPanel1.getBtnEnter().addFocusListener(this);
				ivjButtonPanel1.getBtnHelp().addFocusListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the cbPymtCode1 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcbPymtCode1()
	{
		if (ivjcbPymtCode1 == null)
		{
			try
			{
				ivjcbPymtCode1 = new javax.swing.JComboBox();
				ivjcbPymtCode1.setName("cbPymtCode1");
				ivjcbPymtCode1.setBackground(java.awt.Color.white);
				ivjcbPymtCode1.setBounds(17, 12, 149, 23);
				// user code begin {1}
				populateComboBox(ivjcbPymtCode1);
				//add listeners
				ivjcbPymtCode1.addItemListener(this);
				ivjcbPymtCode1.addActionListener(this);
				ivjcbPymtCode1.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjcbPymtCode1;
	}

	/**
	 * Return the cbPymtCode2 property value.
	 * 
	 * @return javax.swing.JComboBox
	 */

	private JComboBox getcbPymtCode2()
	{
		if (ivjcbPymtCode2 == null)
		{
			try
			{
				ivjcbPymtCode2 = new JComboBox();
				ivjcbPymtCode2.setName("cbPymtCode2");
				ivjcbPymtCode2.setBackground(java.awt.Color.white);
				ivjcbPymtCode2.setBounds(17, 48, 149, 23);
				// user code begin {1}
				populateComboBox(ivjcbPymtCode2);
				//add listeners
				ivjcbPymtCode2.addItemListener(this);
				ivjcbPymtCode2.addActionListener(this);
				ivjcbPymtCode2.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjcbPymtCode2;
	}

	/**
	 * Return the cbPymtCode3 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcbPymtCode3()
	{
		if (ivjcbPymtCode3 == null)
		{
			try
			{
				ivjcbPymtCode3 = new JComboBox();
				ivjcbPymtCode3.setName("cbPymtCode3");
				ivjcbPymtCode3.setBackground(java.awt.Color.white);
				ivjcbPymtCode3.setBounds(17, 84, 149, 23);
				// user code begin {1}
				populateComboBox(ivjcbPymtCode3);
				//add listeners
				ivjcbPymtCode3.addItemListener(this);
				ivjcbPymtCode3.addActionListener(this);
				ivjcbPymtCode3.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjcbPymtCode3;
	}

	/**
	 * Return the cbPymtCode4 property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcbPymtCode4()
	{
		if (ivjcbPymtCode4 == null)
		{
			try
			{
				ivjcbPymtCode4 = new JComboBox();
				ivjcbPymtCode4.setName("cbPymtCode4");
				ivjcbPymtCode4.setBackground(java.awt.Color.white);
				ivjcbPymtCode4.setBounds(17, 120, 149, 23);
				// user code begin {1}
				populateComboBox(ivjcbPymtCode4);
				//add listeners
				ivjcbPymtCode4.addItemListener(this);
				ivjcbPymtCode4.addActionListener(this);
				ivjcbPymtCode4.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjcbPymtCode4;
	}

	/**
	 * Return the cbPymtCodeCharge property value.
	 * 
	 * @return JComboBox
	 */

	private JComboBox getcbPymtCodeCharge()
	{
		if (ivjcbPymtCodeCharge == null)
		{
			try
			{
				ivjcbPymtCodeCharge = new JComboBox();
				ivjcbPymtCodeCharge.setName("cbPymtCodeCharge");
				ivjcbPymtCodeCharge.setBackground(java.awt.Color.white);
				ivjcbPymtCodeCharge.setBounds(17, 164, 149, 23);
				// user code begin {1}
				populateComboBox(ivjcbPymtCodeCharge);
				//add listeners
				//ivjcbPymtCodeCharge.addItemListener(this);
				//ivjcbPymtCodeCharge.addActionListener(this);
				//ivjcbPymtCodeCharge.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjcbPymtCodeCharge;
	}

	/**
	 * Get Payment code from Description
	 * 
	 * @return int
	 * @param asDesc Payment Description
	 * @throws RTSException RTSxception
	 */
	protected int getCodeFromDesc(String asDesc) throws RTSException
	{
		int liIndex = cvDescription.indexOf(asDesc);
		if (liIndex == -1)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				TXT_NO_CODE_WITH + asDesc,
				TXT_ERROR);
		}
		else
		{
			return ((Integer) cvCode.get(liIndex)).intValue();
		}
	}

	/**
	 * Get the Current Remittance Amount.
	 * 
	 * @return Dollar
	 */
	private Dollar getCurrentRemittance()
	{
		Dollar laDollarRemittance = ZERO_DOLLAR;
		if (gettxtAmt1().isVisible()
			&& !gettxtAmt1().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			laDollarRemittance =
				laDollarRemittance.add(
					new Dollar(gettxtAmt1().getText()));
		}
		if (gettxtAmt2().isVisible()
			&& !gettxtAmt2().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			laDollarRemittance =
				laDollarRemittance.add(
					new Dollar(gettxtAmt2().getText()));
		}
		if (gettxtAmt3().isVisible()
			&& !gettxtAmt3().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			laDollarRemittance =
				laDollarRemittance.add(
					new Dollar(gettxtAmt3().getText()));
		}
		if (gettxtAmt4().isVisible()
			&& !gettxtAmt4().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			laDollarRemittance =
				laDollarRemittance.add(
					new Dollar(gettxtAmt4().getText()));
		}
		if (gettxtAmtCharge().isVisible()
			&& !gettxtAmtCharge().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			laDollarRemittance =
				laDollarRemittance.add(
					new Dollar(gettxtAmtCharge().getText()));
		}
		return laDollarRemittance;
	}

	/**
	 * Get PaymentDescription from code
	 * 
	 * @param aiCode int
	 * @return String 
	 * @throws RTSException 
	 */
	protected String getDescFromCode(int aiCode) throws RTSException
	{
		int liIndex = cvCode.indexOf(new Integer(aiCode));
		if (liIndex == -1)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				TXT_NO_DESC_WITH + aiCode,
				TXT_ERROR);
		}
		else
		{
			return (String) cvDescription.get(liIndex);
		}
	}

	/**
	 * Return the FrmPaymentPMT001ContentPane1 property value.
	 * @return JPanel
	 */

	private JPanel getFrmPaymentPMT001ContentPane1()
	{
		if (ivjFrmPaymentPMT001ContentPane1 == null)
		{
			try
			{
				ivjFrmPaymentPMT001ContentPane1 = new JPanel();
				ivjFrmPaymentPMT001ContentPane1.setName(
					"FrmPaymentPMT001ContentPane1");
				ivjFrmPaymentPMT001ContentPane1.setLayout(null);
				ivjFrmPaymentPMT001ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmPaymentPMT001ContentPane1.setMinimumSize(
					new java.awt.Dimension(377, 350));
				getFrmPaymentPMT001ContentPane1().add(
					getstcLblEnterTheFollowing(),
					getstcLblEnterTheFollowing().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getradioCash(),
					getradioCash().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getstcLblPaymentCode(),
					getstcLblPaymentCode().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getstcLblAmount(),
					getstcLblAmount().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getstcLblCheckNo(),
					getstcLblCheckNo().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getradioCheck(),
					getradioCheck().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getstcLblTotalDue(),
					getstcLblTotalDue().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getlblTotalDue(),
					getlblTotalDue().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getstcLblTotalRemittance(),
					getstcLblTotalRemittance().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getlblTotalRemittance(),
					getlblTotalRemittance().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getstcLblChangeDue(),
					getstcLblChangeDue().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getlblChangeDue(),
					getlblChangeDue().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getstcLblChangeType(),
					getstcLblChangeType().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getstclblCreditRemaining(),
					getstclblCreditRemaining().getName());
				getFrmPaymentPMT001ContentPane1().add(
					getlblCreditRemaining(),
					getlblCreditRemaining().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjFrmPaymentPMT001ContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(16, 66, 382, 192);
				getJPanel1().add(
					getcbPymtCode1(),
					getcbPymtCode1().getName());
				getJPanel1().add(gettxtAmt1(), gettxtAmt1().getName());
				getJPanel1().add(
					gettxtChkNo1(),
					gettxtChkNo1().getName());
				getJPanel1().add(
					getcbPymtCode2(),
					getcbPymtCode2().getName());
				getJPanel1().add(gettxtAmt2(), gettxtAmt2().getName());
				getJPanel1().add(
					gettxtChkNo2(),
					gettxtChkNo2().getName());
				getJPanel1().add(
					getcbPymtCode3(),
					getcbPymtCode3().getName());
				getJPanel1().add(gettxtAmt3(), gettxtAmt3().getName());
				getJPanel1().add(
					gettxtChkNo3(),
					gettxtChkNo3().getName());
				getJPanel1().add(
					getcbPymtCode4(),
					getcbPymtCode4().getName());
				getJPanel1().add(gettxtAmt4(), gettxtAmt4().getName());
				getJPanel1().add(
					gettxtChkNo4(),
					gettxtChkNo4().getName());
				getJPanel1().add(
					getcbPymtCodeCharge(),
					getcbPymtCodeCharge().getName());
				getJPanel1().add(
					getstcLblCreditCardFee(),
					getstcLblCreditCardFee().getName());
				getJPanel1().add(
					gettxtAmtCharge(),
					gettxtAmtCharge().getName());
				getJPanel1().add(
					gettxtChkNoCharge(),
					gettxtChkNoCharge().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the lblChangeDue property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblChangeDue()
	{
		if (ivjlblChangeDue == null)
		{
			try
			{
				ivjlblChangeDue = new JLabel();
				ivjlblChangeDue.setName("lblChangeDue");
				ivjlblChangeDue.setText("$0.00");
				ivjlblChangeDue.setMaximumSize(
					new java.awt.Dimension(31, 14));
				ivjlblChangeDue.setAlignmentX(
					java.awt.Component.LEFT_ALIGNMENT);
				ivjlblChangeDue.setBounds(201, 305, 99, 14);
				ivjlblChangeDue.setMinimumSize(
					new java.awt.Dimension(31, 14));
				ivjlblChangeDue.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblChangeDue;
	}

	/**
	 * Return the lblCreditRemaining property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblCreditRemaining()
	{
		if (ivjlblCreditRemaining == null)
		{
			try
			{
				ivjlblCreditRemaining = new JLabel();
				ivjlblCreditRemaining.setName("lblCreditRemaining");
				ivjlblCreditRemaining.setText("$9,999.99");
				ivjlblCreditRemaining.setBounds(201, 370, 99, 14);
				ivjlblCreditRemaining.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblCreditRemaining;
	}

	/**
	 * Return the lblTotalDue property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblTotalDue()
	{
		if (ivjlblTotalDue == null)
		{
			try
			{
				ivjlblTotalDue = new JLabel();
				ivjlblTotalDue.setName("lblTotalDue");
				ivjlblTotalDue.setText("$0.00");
				ivjlblTotalDue.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblTotalDue.setAlignmentX(
					java.awt.Component.LEFT_ALIGNMENT);
				ivjlblTotalDue.setBounds(201, 261, 99, 14);
				ivjlblTotalDue.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblTotalDue.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblTotalDue;
	}

	/**
	 * Return the lblTotalRemittance property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getlblTotalRemittance()
	{
		if (ivjlblTotalRemittance == null)
		{
			try
			{
				ivjlblTotalRemittance = new JLabel();
				ivjlblTotalRemittance.setName("lblTotalRemittance");
				ivjlblTotalRemittance.setText("$0.00");
				ivjlblTotalRemittance.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblTotalRemittance.setAlignmentX(
					java.awt.Component.LEFT_ALIGNMENT);
				ivjlblTotalRemittance.setBounds(186, 283, 114, 14);
				ivjlblTotalRemittance.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblTotalRemittance.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblTotalRemittance;
	}

	/**
	 * Return the radioCash property value.
	 * 
	 * @return JRadioButton
	 */

	private JRadioButton getradioCash()
	{
		if (ivjradioCash == null)
		{
			try
			{
				ivjradioCash = new JRadioButton();
				ivjradioCash.setName("radioCash");
				ivjradioCash.setMnemonic(KeyEvent.VK_A);
				ivjradioCash.setText(COMBO_CASH);
				ivjradioCash.setMaximumSize(
					new java.awt.Dimension(57, 22));
				ivjradioCash.setActionCommand(COMBO_CASH);
				ivjradioCash.setBounds(218, 325, 65, 19);
				ivjradioCash.setMinimumSize(
					new java.awt.Dimension(57, 22));
				// user code begin {1}
				ivjradioCash.addActionListener(this);
				// defect 7885
				//ivjradioCash.addKeyListener(this);
				//ivjradioCash.addFocusListener(this);
				// end defect 7885
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjradioCash;
	}

	/**
	 * Return the radioCheck property value.
	 * 
	 * @return JRadioButton
	 */

	private JRadioButton getradioCheck()
	{
		if (ivjradioCheck == null)
		{
			try
			{
				ivjradioCheck = new JRadioButton();
				ivjradioCheck.setName("radioCheck");
				ivjradioCheck.setMnemonic(KeyEvent.VK_K);
				ivjradioCheck.setText(COMBO_CHECK);
				ivjradioCheck.setMaximumSize(
					new java.awt.Dimension(64, 22));
				ivjradioCheck.setActionCommand(COMBO_CHECK);
				ivjradioCheck.setBounds(218, 348, 65, 19);
				ivjradioCheck.setMinimumSize(
					new java.awt.Dimension(64, 22));
				// user code begin {1}
				ivjradioCheck.addActionListener(this);
				// defect 7885
				//ivjradioCheck.addKeyListener(this);
				//ivjradioCheck.addFocusListener(this);
				// end defect 7885
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjradioCheck;
	}

	/**
	 * Return the stcLblAmount property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblAmount()
	{
		if (ivjstcLblAmount == null)
		{
			try
			{
				ivjstcLblAmount = new JLabel();
				ivjstcLblAmount.setSize(54, 14);
				ivjstcLblAmount.setName("stcLblAmount");
				ivjstcLblAmount.setText(TXT_AMOUNT);
				ivjstcLblAmount.setMaximumSize(
					new java.awt.Dimension(44, 14));
				ivjstcLblAmount.setMinimumSize(
					new java.awt.Dimension(44, 14));
				ivjstcLblAmount.setLocation(210, 48);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblAmount;
	}

	/**
	 * Return the stcLblChangeDue property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblChangeDue()
	{
		if (ivjstcLblChangeDue == null)
		{
			try
			{
				ivjstcLblChangeDue = new JLabel();
				ivjstcLblChangeDue.setName("stcLblChangeDue");
				ivjstcLblChangeDue.setText(TXT_CHNG_DUE);
				ivjstcLblChangeDue.setMaximumSize(
					new java.awt.Dimension(71, 14));
				ivjstcLblChangeDue.setMinimumSize(
					new java.awt.Dimension(71, 14));
				ivjstcLblChangeDue.setBounds(101, 305, 76, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblChangeDue;
	}

	/**
	 * Return the stcLblChangeType property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblChangeType()
	{
		if (ivjstcLblChangeType == null)
		{
			try
			{
				ivjstcLblChangeType = new JLabel();
				ivjstcLblChangeType.setName("stcLblChangeType");
				ivjstcLblChangeType.setText(TXT_CHNG_TYPE);
				ivjstcLblChangeType.setMaximumSize(
					new java.awt.Dimension(76, 14));
				ivjstcLblChangeType.setMinimumSize(
					new java.awt.Dimension(76, 14));
				ivjstcLblChangeType.setBounds(96, 327, 80, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblChangeType;
	}

	/**
	 * Return the stcLblCheckNo property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblCheckNo()
	{
		if (ivjstcLblCheckNo == null)
		{
			try
			{
				ivjstcLblCheckNo = new JLabel();
				ivjstcLblCheckNo.setSize(61, 14);
				ivjstcLblCheckNo.setName("stcLblCheckNo");
				ivjstcLblCheckNo.setText(TXT_CHK_NO);
				ivjstcLblCheckNo.setMaximumSize(
					new java.awt.Dimension(54, 14));
				ivjstcLblCheckNo.setMinimumSize(
					new java.awt.Dimension(54, 14));
				ivjstcLblCheckNo.setLocation(317, 48);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblCheckNo;
	}

	/**
	 * Return the stcLblCreditCardFee property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblCreditCardFee()
	{
		if (ivjstcLblCreditCardFee == null)
		{
			try
			{
				ivjstcLblCreditCardFee = new JLabel();
				ivjstcLblCreditCardFee.setName("stcLblCreditCardFee");
				ivjstcLblCreditCardFee.setText(TXT_CRDT_CARD_FEE);
				ivjstcLblCreditCardFee.setBounds(135, 146, 112, 14);
				ivjstcLblCreditCardFee.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblCreditCardFee;
	}

	/**
	 * Return the stclblCreditRemaining property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstclblCreditRemaining()
	{
		if (ivjstclblCreditRemaining == null)
		{
			try
			{
				ivjstclblCreditRemaining = new JLabel();
				ivjstclblCreditRemaining.setName(
					"stclblCreditRemaining");
				ivjstclblCreditRemaining.setText(TXT_CRDT_REM);
				ivjstclblCreditRemaining.setBounds(64, 370, 108, 14);
				ivjstclblCreditRemaining.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstclblCreditRemaining;
	}

	/**
	 * Return the stcLblEnterTheFollowing property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblEnterTheFollowing()
	{
		if (ivjstcLblEnterTheFollowing == null)
		{
			try
			{
				ivjstcLblEnterTheFollowing = new JLabel();
				ivjstcLblEnterTheFollowing.setName(
					"stcLblEnterTheFollowing");
				ivjstcLblEnterTheFollowing.setText(TXT_ENTER_FOLWNG);
				ivjstcLblEnterTheFollowing.setMaximumSize(
					new java.awt.Dimension(108, 14));
				ivjstcLblEnterTheFollowing.setMinimumSize(
					new java.awt.Dimension(108, 14));
				ivjstcLblEnterTheFollowing.setHorizontalAlignment(0);
				ivjstcLblEnterTheFollowing.setBounds(136, 19, 145, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblEnterTheFollowing;
	}

	/**
	 * Return the stcLblPaymentCode property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblPaymentCode()
	{
		if (ivjstcLblPaymentCode == null)
		{
			try
			{
				ivjstcLblPaymentCode = new JLabel();
				ivjstcLblPaymentCode.setSize(92, 14);
				ivjstcLblPaymentCode.setName("stcLblPaymentCode");
				ivjstcLblPaymentCode.setText(TXT_PAY_CD);
				ivjstcLblPaymentCode.setMaximumSize(
					new java.awt.Dimension(82, 14));
				ivjstcLblPaymentCode.setMinimumSize(
					new java.awt.Dimension(82, 14));
				ivjstcLblPaymentCode.setLocation(52, 48);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblPaymentCode;
	}

	/**
	 * Return the stcLblTotalDue property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblTotalDue()
	{
		if (ivjstcLblTotalDue == null)
		{
			try
			{
				ivjstcLblTotalDue = new JLabel();
				ivjstcLblTotalDue.setName("stcLblTotalDue");
				ivjstcLblTotalDue.setText(TXT_TOT_DUE);
				ivjstcLblTotalDue.setMaximumSize(
					new java.awt.Dimension(56, 14));
				ivjstcLblTotalDue.setMinimumSize(
					new java.awt.Dimension(56, 14));
				ivjstcLblTotalDue.setBounds(116, 261, 59, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblTotalDue;
	}

	/**
	 * Return the stcLblTotalRemittance property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblTotalRemittance()
	{
		if (ivjstcLblTotalRemittance == null)
		{
			try
			{
				ivjstcLblTotalRemittance = new JLabel();
				ivjstcLblTotalRemittance.setName(
					"stcLblTotalRemittance");
				ivjstcLblTotalRemittance.setText(TXT_TOT_REMIT);
				ivjstcLblTotalRemittance.setMaximumSize(
					new java.awt.Dimension(99, 14));
				ivjstcLblTotalRemittance.setMinimumSize(
					new java.awt.Dimension(99, 14));
				ivjstcLblTotalRemittance.setBounds(73, 283, 103, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblTotalRemittance;
	}

	/**
	 * Return the RTSInputField1 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtAmt1()
	{
		if (ivjtxtAmt1 == null)
		{
			try
			{
				ivjtxtAmt1 = new RTSInputField();
				ivjtxtAmt1.setName("txtAmt1");
				ivjtxtAmt1.setBounds(183, 13, 92, 20);
				// defect 10596
				//ivjtxtAmt1.setMaxLength(8);
				ivjtxtAmt1.setMaxLength(12);
				// end defect 10596
				ivjtxtAmt1.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtAmt1.setInput(RTSInputField.DOLLAR_ONLY);
				//ivjtxtAmt1.setManagingFocus(true);
				ivjtxtAmt1.addFocusListener(this);
				ivjtxtAmt1.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtAmt1;
	}

	/**
	 * Return the txtAmt2 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtAmt2()
	{
		if (ivjtxtAmt2 == null)
		{
			try
			{
				ivjtxtAmt2 = new RTSInputField();
				ivjtxtAmt2.setName("txtAmt2");
				ivjtxtAmt2.setBounds(183, 49, 92, 20);
				// defect 10596
				//ivjtxtAmt2.setMaxLength(8);
				ivjtxtAmt2.setMaxLength(12);
				// end defect 10596
				ivjtxtAmt2.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtAmt2.setInput(RTSInputField.DOLLAR_ONLY);
				//ivjtxtAmt2.setManagingFocus(true);
				ivjtxtAmt2.addFocusListener(this);
				ivjtxtAmt2.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtAmt2;
	}

	/**
	 * Return the txtAmt3 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtAmt3()
	{
		if (ivjtxtAmt3 == null)
		{
			try
			{
				ivjtxtAmt3 = new RTSInputField();
				ivjtxtAmt3.setName("txtAmt3");
				ivjtxtAmt3.setBounds(183, 85, 92, 20);
				// defect 10596
				//ivjtxtAmt3.setMaxLength(8);
				ivjtxtAmt3.setMaxLength(12);
				// end defect 10596
				ivjtxtAmt3.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtAmt3.setInput(RTSInputField.DOLLAR_ONLY);
				//ivjtxtAmt3.setManagingFocus(true);
				ivjtxtAmt3.addFocusListener(this);
				ivjtxtAmt3.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtAmt3;
	}

	/**
	 * Return the txtAmt4 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtAmt4()
	{
		if (ivjtxtAmt4 == null)
		{
			try
			{
				ivjtxtAmt4 = new RTSInputField();
				ivjtxtAmt4.setName("txtAmt4");
				ivjtxtAmt4.setBounds(183, 121, 92, 20);
				// defect 10596
				// ivjtxtAmt4.setMaxLength(8);
				ivjtxtAmt4.setMaxLength(12);
				// end defect 10596
				ivjtxtAmt4.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtAmt4.setInput(RTSInputField.DOLLAR_ONLY);
				//ivjtxtAmt4.setManagingFocus(true);
				ivjtxtAmt4.addFocusListener(this);
				ivjtxtAmt4.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtAmt4;
	}

	/**
	 * Return the txtAmtCharge property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtAmtCharge()
	{
		if (ivjtxtAmtCharge == null)
		{
			try
			{
				ivjtxtAmtCharge = new RTSInputField();
				ivjtxtAmtCharge.setName("txtAmtCharge");
				ivjtxtAmtCharge.setBackground(java.awt.Color.white);
				ivjtxtAmtCharge.setBounds(183, 165, 92, 20);
				ivjtxtAmtCharge.setEditable(false);
				ivjtxtAmtCharge.setMaxLength(8);
				ivjtxtAmtCharge.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				// defect 7885
				//ivjtxtAmtCharge.setNextFocusableComponent(
				//	gettxtChkNoCharge());
				// end defect 7885
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtAmtCharge;
	}

	/**
	 * Return the txtChkNo1 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtChkNo1()
	{
		if (ivjtxtChkNo1 == null)
		{
			try
			{
				ivjtxtChkNo1 = new RTSInputField();
				ivjtxtChkNo1.setName("txtChkNo1");
				ivjtxtChkNo1.setBounds(295, 13, 67, 20);
				ivjtxtChkNo1.setMaxLength(6);
				// user code begin {1}
				//ivjtxtChkNo1.setManagingFocus(true);
				ivjtxtChkNo1.addKeyListener(this);
				ivjtxtChkNo1.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtChkNo1;
	}

	/**
	 * Return the txtChkNo2 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtChkNo2()
	{
		if (ivjtxtChkNo2 == null)
		{
			try
			{
				ivjtxtChkNo2 = new RTSInputField();
				ivjtxtChkNo2.setName("txtChkNo2");
				ivjtxtChkNo2.setBounds(295, 49, 67, 20);
				ivjtxtChkNo2.setMaxLength(6);
				// user code begin {1}
				//ivjtxtChkNo2.setManagingFocus(true);
				ivjtxtChkNo2.addKeyListener(this);
				ivjtxtChkNo2.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtChkNo2;
	}

	/**
	 * Return the txtChkNo3 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtChkNo3()
	{
		if (ivjtxtChkNo3 == null)
		{
			try
			{
				ivjtxtChkNo3 = new RTSInputField();
				ivjtxtChkNo3.setName("txtChkNo3");
				ivjtxtChkNo3.setBounds(295, 85, 67, 20);
				ivjtxtChkNo3.setMaxLength(6);
				// user code begin {1}
				//ivjtxtChkNo3.setManagingFocus(true);
				ivjtxtChkNo3.addKeyListener(this);
				ivjtxtChkNo3.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtChkNo3;
	}

	/**
	 * Return the txtChkNo4 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtChkNo4()
	{
		if (ivjtxtChkNo4 == null)
		{
			try
			{
				ivjtxtChkNo4 = new RTSInputField();
				ivjtxtChkNo4.setName("txtChkNo4");
				ivjtxtChkNo4.setBounds(295, 121, 67, 20);
				ivjtxtChkNo4.setMaxLength(6);
				// user code begin {1}
				//ivjtxtChkNo4.setManagingFocus(true);
				ivjtxtChkNo4.addKeyListener(this);
				ivjtxtChkNo4.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtChkNo4;
	}

	/**
	 * Return the txtChkNoCharge property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtChkNoCharge()
	{
		if (ivjtxtChkNoCharge == null)
		{
			try
			{
				ivjtxtChkNoCharge = new RTSInputField();
				ivjtxtChkNoCharge.setName("txtChkNoCharge");
				ivjtxtChkNoCharge.setBounds(295, 165, 67, 20);
				ivjtxtChkNoCharge.setMaxLength(6);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtChkNoCharge;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
	}

	/**
	 * Detect and handles Focus and Tab events for rows
	 * 
	 * @return boolean
	 * @param aaComp
	 * @param abPopupWarning
	 * @throws RTSException
	 */
	protected boolean handleRowElements(
		Component aaComp,
		boolean abPopupWarning)
		throws RTSException
	{
		//short circuit
		if (!(aaComp instanceof RTSInputField))
		{
			return false;
		}
		// defect 7885
		//caRTSInputFieldLastFocused = null;
		// end defect 7885

		boolean lbValidation = false;
		boolean lbOnFocusLostTarget = false;

		Dollar ldDollarOldAmt = null;
		Dollar ldDollarNewAmt = null;
		int liRow = 0;

		if (aaComp == gettxtAmt1())
		{
			lbValidation =
				validateDollarField(gettxtAmt1(), abPopupWarning);
			lbOnFocusLostTarget = true;
			liRow = 0;
		}
		else if (aaComp == gettxtAmt2())
		{
			lbValidation =
				validateDollarField(gettxtAmt2(), abPopupWarning);
			lbOnFocusLostTarget = true;
			liRow = 1;
		}
		else if (aaComp == gettxtAmt3())
		{
			lbValidation =
				validateDollarField(gettxtAmt3(), abPopupWarning);
			lbOnFocusLostTarget = true;
			liRow = 2;
		}
		else if (aaComp == gettxtAmt4())
		{
			lbValidation =
				validateDollarField(gettxtAmt4(), abPopupWarning);
			lbOnFocusLostTarget = true;
			liRow = 3;
		}
		else if (
			aaComp == gettxtChkNo1()
				|| aaComp == gettxtChkNo2()
				|| aaComp == gettxtChkNo3()
				|| aaComp == gettxtChkNo4())
		{
			return true;
		}

		//recalculate total due, total remittance, and change due    
		if (lbOnFocusLostTarget)
		{
			if (!lbValidation)
			{
				return false;
			}
			else
			{
				TransactionPaymentData laTransactionPaymentData =
					(
						TransactionPaymentData) cvTransactionPaymentData
							.get(
						liRow);
				ldDollarOldAmt =
					laTransactionPaymentData.getPymntTypeAmt();
				switch (liRow)
				{
					case 0 :
						if (gettxtAmt1()
							.getText()
							.trim()
							.equals(CommonConstant.STR_SPACE_EMPTY))
						{
							ldDollarNewAmt = ZERO_DOLLAR;
						}
						else
						{
							ldDollarNewAmt =
								new Dollar(gettxtAmt1().getText());
						}
						displayLimitExceedWarning(gettxtAmt1());
						break;
					case 1 :
						if (gettxtAmt2()
							.getText()
							.trim()
							.equals(CommonConstant.STR_SPACE_EMPTY))
						{
							ldDollarNewAmt = ZERO_DOLLAR;
						}
						else
						{
							ldDollarNewAmt =
								new Dollar(gettxtAmt2().getText());
						}
						displayLimitExceedWarning(gettxtAmt2());
						break;
					case 2 :
						if (gettxtAmt3()
							.getText()
							.trim()
							.equals(CommonConstant.STR_SPACE_EMPTY))
						{
							ldDollarNewAmt = ZERO_DOLLAR;
						}
						else
						{
							ldDollarNewAmt =
								new Dollar(gettxtAmt3().getText());
						}
						displayLimitExceedWarning(gettxtAmt3());
						break;
					case 3 :
						if (gettxtAmt4()
							.getText()
							.trim()
							.equals(CommonConstant.STR_SPACE_EMPTY))
						{
							ldDollarNewAmt = ZERO_DOLLAR;
						}
						else
						{
							ldDollarNewAmt =
								new Dollar(gettxtAmt4().getText());
						}
						displayLimitExceedWarning(gettxtAmt4());
						break;
					default :
						// do nothing on default
				}
				laTransactionPaymentData.setPymntTypeAmt(
					ldDollarNewAmt);
				cdDollarTotalRemittance =
					cdDollarTotalRemittance.subtract(ldDollarOldAmt);
				cdDollarTotalRemittance =
					cdDollarTotalRemittance.add(ldDollarNewAmt);
				reCalculateAndDisplayValidFields();

				return true;
			}
		}
		return false;
	}

	/**
	 * Detect and handles Focus and Tab events for rows
	 * 
	 * @return boolean
	 * @param abPopup
	 */
	protected boolean handleRowElements(boolean abPopup)
	{
		boolean lbFailValidation = false;
		//    boolean lbOnFocusLostTarget = false;
		RTSException leRTSExHolder = new RTSException();
		Dollar ldDollarOldAmt = null;
		Dollar ldDollarNewAmt = null;
		int liRow = 0;

		if (gettxtAmt1().isVisible())
		{
			RTSException leRTSEx = validateDollarField(gettxtAmt1());
			if (leRTSEx != null)
			{
				leRTSExHolder.addException(leRTSEx, gettxtAmt1());
				lbFailValidation = true;
			}
			liRow = 0;
		}

		if (gettxtAmt2().isVisible())
		{
			RTSException leRTSEx = validateDollarField(gettxtAmt2());
			if (leRTSEx != null)
			{
				leRTSExHolder.addException(leRTSEx, gettxtAmt2());
				lbFailValidation = true;
			}
			liRow = 1;
		}

		if (gettxtAmt3().isVisible())
		{
			RTSException leRTSEx = validateDollarField(gettxtAmt3());
			if (leRTSEx != null)
			{
				leRTSExHolder.addException(leRTSEx, gettxtAmt3());
				lbFailValidation = true;
			}
			liRow = 2;
		}

		if (gettxtAmt4().isVisible())
		{
			RTSException leRTSEx = validateDollarField(gettxtAmt4());
			if (leRTSEx != null)
			{
				leRTSExHolder.addException(leRTSEx, gettxtAmt4());
				lbFailValidation = true;
			}
			liRow = 3;
		}

		//recalculate total due, total remittance, and change due    
		if (lbFailValidation)
		{
			if (abPopup)
			{
				leRTSExHolder.displayError(this);
				leRTSExHolder.getFirstComponent().requestFocus();
			}
			return false;
		}
		else
		{
			TransactionPaymentData laTransactionPaymentData =
				(TransactionPaymentData) cvTransactionPaymentData.get(
					liRow);
			ldDollarOldAmt = laTransactionPaymentData.getPymntTypeAmt();
			switch (liRow)
			{
				case 0 :
					if (gettxtAmt1()
						.getText()
						.trim()
						.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						ldDollarNewAmt = ZERO_DOLLAR;
					}
					else
					{
						ldDollarNewAmt =
							new Dollar(gettxtAmt1().getText());
					}
					break;
				case 1 :
					if (gettxtAmt2()
						.getText()
						.trim()
						.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						ldDollarNewAmt = ZERO_DOLLAR;
					}
					else
					{
						ldDollarNewAmt =
							new Dollar(gettxtAmt2().getText());
					}
					break;
				case 2 :
					if (gettxtAmt3()
						.getText()
						.trim()
						.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						ldDollarNewAmt = ZERO_DOLLAR;
					}
					else
					{
						ldDollarNewAmt =
							new Dollar(gettxtAmt3().getText());
					}
					break;
				case 3 :
					if (gettxtAmt4()
						.getText()
						.trim()
						.equals(CommonConstant.STR_SPACE_EMPTY))
					{
						ldDollarNewAmt = ZERO_DOLLAR;
					}
					else
					{
						ldDollarNewAmt =
							new Dollar(gettxtAmt4().getText());
					}
					break;
				default :
					// no action on default
			}
			laTransactionPaymentData.setPymntTypeAmt(ldDollarNewAmt);
			cdDollarTotalRemittance =
				cdDollarTotalRemittance.subtract(ldDollarOldAmt);
			cdDollarTotalRemittance =
							cdDollarTotalRemittance.add(ldDollarNewAmt);

			// defect 10596
			if (cdDollarTotalRemittance.compareTo(cdMaxDollars)> 0)
			{
				RTSException leRTSPriceExp =
					new RTSException(
					RTSException.WARNING_MESSAGE,
					ERRMSG_TOTAL_REMITTANCE,
					ERRMSG_ERROR);
					leRTSPriceExp.displayError();
					return false;
			}
			// end defect 10596
		
			reCalculateAndDisplayValidFields();
			return true;
		}
	}

	/**
	 * Initialize the class.
	 */

	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(FRM_NAME_PMT001);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(418, 451);
			setTitle(FRM_TITLE_PMT001);
			setContentPane(getFrmPaymentPMT001ContentPane1());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// user code begin {2}
		//PCR25
		// set focusable along with the setvisibles
		getstcLblCreditCardFee().setVisible(false);
		getstcLblCreditCardFee().setFocusable(false);

		// defect 7885
		// First combo box should be available when frame comes up
		//getcbPymtCode1().setVisible(false);
		//getcbPymtCode1().setFocusable(false);
		// end defect 7885
		getcbPymtCode2().setVisible(false);
		getcbPymtCode2().setFocusable(false);
		getcbPymtCode3().setVisible(false);
		getcbPymtCode3().setFocusable(false);
		getcbPymtCode4().setVisible(false);
		getcbPymtCode4().setFocusable(false);
		getcbPymtCodeCharge().setVisible(false);
		getcbPymtCodeCharge().setFocusable(false);

		gettxtAmt2().setVisible(false);
		gettxtAmt2().setFocusable(false);
		gettxtAmt3().setVisible(false);
		gettxtAmt3().setFocusable(false);
		gettxtAmt4().setVisible(false);
		gettxtAmt4().setFocusable(false);
		gettxtAmtCharge().setVisible(false);
		gettxtAmtCharge().setFocusable(false);

		gettxtChkNo2().setVisible(false);
		gettxtChkNo2().setFocusable(false);
		gettxtChkNo3().setVisible(false);
		gettxtChkNo3().setFocusable(false);
		gettxtChkNo4().setVisible(false);
		gettxtChkNo4().setFocusable(false);
		gettxtChkNoCharge().setVisible(false);
		gettxtChkNoCharge().setFocusable(false);

		// defect 7885
		// Changed from ButtonGroup to RTSButtonGroup
		caButtonGroup = new RTSButtonGroup();
		caButtonGroup.add(getradioCash());
		caButtonGroup.add(getradioCheck());
		// defect 7885
		getradioCash().setSelected(true);

		getlblCreditRemaining().setVisible(false);
		getlblCreditRemaining().setFocusable(false);
		getstclblCreditRemaining().setVisible(false);
		getstclblCreditRemaining().setFocusable(false);

		// comment out FocusManager for 5.2.3
		// TODO is this right?
		//FocusManager.setCurrentManager(
		//	new ContainerOrderFocusManager());
		// user code end
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		try
		{
			calculateCreditCardTotal();
			if (aaIE.getSource() == getcbPymtCodeCharge())
			{
				if (getcbPymtCodeCharge()
					.getSelectedItem()
					.equals(getDescFromCode(CHECK)))
				{
					gettxtChkNoCharge().setVisible(true);
					gettxtChkNoCharge().setFocusable(true);
				}
				else
				{
					gettxtChkNoCharge().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtChkNoCharge().setVisible(false);
					gettxtChkNoCharge().setFocusable(false);
				}

			}
			else if (aaIE.getSource() == getcbPymtCode1())
			{
				TransactionPaymentData laTransactionPaymentData =
					(
						TransactionPaymentData) cvTransactionPaymentData
							.get(
						0);
				laTransactionPaymentData.setPymntTypeCd(
					getCodeFromDesc(
						(String) getcbPymtCode1().getSelectedItem()));
				if (getcbPymtCode1()
					.getSelectedItem()
					.equals(getDescFromCode(CHECK)))
				{
					gettxtChkNo1().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtChkNo1().setVisible(true);
					gettxtChkNo1().setFocusable(true);
					if (!shouldShowCreditCardFee(1))
					{
						showCreditCardInfo(false);
					}

				}
				else
				{
					gettxtChkNo1().setVisible(false);
					gettxtChkNo1().setFocusable(false);
					laTransactionPaymentData.setPymntCkNo(null);
					if ((!caCreditFeeInfo
						.getItmPrice()
						.equals(ZERO_DOLLAR)
						|| caCreditFeeInfo.isPercentage())
						&& getcbPymtCode1().getSelectedItem().equals(
							COMBO_CHARGE))
					{
						if (aaIE.getStateChange() == 2
							&& !getstcLblCreditCardFee().isVisible())
						{
							cbDisplayCredMsg = true;
						}
					}
					else
					{
						if (!shouldShowCreditCardFee(1))
						{
							showCreditCardInfo(false);
						}
					}
				}
			}
			else if (aaIE.getSource() == getcbPymtCode2())
			{
				TransactionPaymentData laTransactionPaymentData =
					(
						TransactionPaymentData) cvTransactionPaymentData
							.get(
						1);
				laTransactionPaymentData.setPymntTypeCd(
					getCodeFromDesc(
						(String) getcbPymtCode2().getSelectedItem()));
				if (getcbPymtCode2()
					.getSelectedItem()
					.equals(getDescFromCode(CHECK)))
				{
					gettxtChkNo2().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtChkNo2().setVisible(true);
					gettxtChkNo2().setFocusable(true);
					if (!shouldShowCreditCardFee(2))
					{
						showCreditCardInfo(false);
					}

				}
				else
				{
					gettxtChkNo2().setVisible(false);
					gettxtChkNo2().setFocusable(false);
					laTransactionPaymentData.setPymntCkNo(null);
					if ((!caCreditFeeInfo
						.getItmPrice()
						.equals(ZERO_DOLLAR)
						|| caCreditFeeInfo.isPercentage())
						&& getcbPymtCode2().getSelectedItem().equals(
							COMBO_CHARGE))
					{
						if (aaIE.getStateChange() == 2
							&& !getstcLblCreditCardFee().isVisible())
						{
							cbDisplayCredMsg = true;
						}
					}
					else
					{
						if (!shouldShowCreditCardFee(2))
						{
							showCreditCardInfo(false);
						}
					}
				}
			}
			else if (aaIE.getSource() == getcbPymtCode3())
			{
				TransactionPaymentData laTransactionPaymentData =
					(
						TransactionPaymentData) cvTransactionPaymentData
							.get(
						2);
				laTransactionPaymentData.setPymntTypeCd(
					getCodeFromDesc(
						(String) getcbPymtCode3().getSelectedItem()));
				if (getcbPymtCode3()
					.getSelectedItem()
					.equals(getDescFromCode(CHECK)))
				{
					gettxtChkNo3().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtChkNo3().setVisible(true);
					gettxtChkNo3().setFocusable(true);
					if (!shouldShowCreditCardFee(3))
					{
						showCreditCardInfo(false);
					}

				}
				else
				{
					gettxtChkNo3().setVisible(false);
					gettxtChkNo3().setFocusable(false);
					laTransactionPaymentData.setPymntCkNo(null);
					if ((!caCreditFeeInfo
						.getItmPrice()
						.equals(ZERO_DOLLAR)
						|| caCreditFeeInfo.isPercentage())
						&& getcbPymtCode3().getSelectedItem().equals(
							COMBO_CHARGE))
					{
						if (aaIE.getStateChange() == 2
							&& !getstcLblCreditCardFee().isVisible())
						{
							cbDisplayCredMsg = true;
						}
					}
					else
					{
						if (!shouldShowCreditCardFee(3))
						{
							showCreditCardInfo(false);
						}
					}
				}
			}
			else if (aaIE.getSource() == getcbPymtCode4())
			{
				TransactionPaymentData laTransactionPaymentData =
					(
						TransactionPaymentData) cvTransactionPaymentData
							.get(
						3);
				laTransactionPaymentData.setPymntTypeCd(
					getCodeFromDesc(
						(String) getcbPymtCode4().getSelectedItem()));
				if (getcbPymtCode4()
					.getSelectedItem()
					.equals(getDescFromCode(CHECK)))
				{
					gettxtChkNo4().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtChkNo4().setVisible(true);
					gettxtChkNo4().setFocusable(true);
					if (!shouldShowCreditCardFee(4))
					{
						showCreditCardInfo(false);
					}

				}
				else
				{
					gettxtChkNo4().setVisible(false);
					gettxtChkNo4().setFocusable(false);
					laTransactionPaymentData.setPymntCkNo(null);
					if ((!caCreditFeeInfo
						.getItmPrice()
						.equals(ZERO_DOLLAR)
						|| caCreditFeeInfo.isPercentage())
						&& getcbPymtCode4().getSelectedItem().equals(
							COMBO_CHARGE))
					{
						if (aaIE.getStateChange() == 2
							&& !getstcLblCreditCardFee().isVisible())
						{
							cbDisplayCredMsg = true;
						}
					}
					else
					{
						if (!shouldShowCreditCardFee(4))
						{
							showCreditCardInfo(false);
						}
					}
				}
			}
			reCalculateAndDisplayValidFields();
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
	}

	//	/**
	//	 * Process Key Event
	//	 * 
	//	 * @param aaKE KeyEvent
	//	 */
	//	public void keyPressed(KeyEvent aaKE)
	//	{
	//		super.keyPressed(aaKE);
	// defect 8240
	// arrow keys now handled inside ButtonPanel
	//		if (getFocusOwner() == getButtonPanel1().getBtnEnter())
	//		{
	//			if (aaKE.getKeyCode() == KeyEvent.VK_DOWN
	//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
	//			{
	//				getButtonPanel1().getBtnHelp().requestFocus();
	//			}
	//			else if (
	//				aaKE.getKeyCode() == KeyEvent.VK_UP
	//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
	//			{
	//				getButtonPanel1().getBtnCancel().requestFocus();
	//			}
	//		}
	//		else if (getFocusOwner() == getButtonPanel1().getBtnCancel())
	//		{
	//			if (aaKE.getKeyCode() == KeyEvent.VK_DOWN
	//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
	//			{
	//				getButtonPanel1().getBtnEnter().requestFocus();
	//			}
	//			else if (
	//				aaKE.getKeyCode() == KeyEvent.VK_UP
	//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
	//			{
	//				getButtonPanel1().getBtnHelp().requestFocus();
	//			}
	//		}
	//		else if (getFocusOwner() == getButtonPanel1().getBtnHelp())
	//		{
	//			if (aaKE.getKeyCode() == KeyEvent.VK_DOWN
	//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
	//			{
	//				getButtonPanel1().getBtnCancel().requestFocus();
	//			}
	//			else if (
	//				aaKE.getKeyCode() == KeyEvent.VK_UP
	//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
	//			{
	//				getButtonPanel1().getBtnEnter().requestFocus();
	//			}
	//		}
	// end defect 8240
	//		if (getFocusOwner() == getradioCheck()
	//			&& (aaKE.getKeyCode() == KeyEvent.VK_UP
	//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
	//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
	//				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT))
	//		{
	//			getradioCash().requestFocus();
	//			// defect 7885
	//			//getradioCash().setSelected(true);
	//			// end defect 7885
	//		}
	//		else if (
	//			getFocusOwner() == getradioCash()
	//				&& (aaKE.getKeyCode() == KeyEvent.VK_UP
	//					|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
	//					|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
	//					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT))
	//		{
	//			getradioCheck().requestFocus();
	//			// defect 7885
	//			//getradioCheck().setSelected(true);
	//			// end defect 7885
	//		}
	// defect 7885
	// code to handle when tabbing moved to focusLost()
	//		else
	//		{
	//			// row tab management
	//			try
	//			{
	//				if (aaKE.getKeyCode() == KeyEvent.VK_TAB)
	//				{
	//					if (!aaKE.isShiftDown())
	//					{
	//						// calculate and display credit card total
	//						if (aaKE.getSource() == gettxtAmt1()
	//							|| aaKE.getSource() == gettxtAmt2()
	//							|| aaKE.getSource() == gettxtAmt3()
	//							|| aaKE.getSource() == gettxtAmt4())
	//						{
	//							if (!caCreditFeeInfo
	//								.getItmPrice()
	//								.equals(ZERO_DOLLAR)
	//								|| caCreditFeeInfo.isPercentage())
	//							{
	//								calculateCreditCardTotal();
	//							}
	//							// check if message should pop up
	//							if (aaKE.getSource() == gettxtAmt1())
	//							{
	//								if (getcbPymtCode1()
	//									.getSelectedItem()
	//									.equals(COMBO_CHARGE))
	//								{
	//									if ((gettxtAmt2().isVisible()
	//										&& getcbPymtCode2()
	//											.getSelectedItem()
	//											.equals(
	//											COMBO_CHARGE))
	//										|| (gettxtAmt3().isVisible()
	//											&& getcbPymtCode3()
	//												.getSelectedItem()
	//												.equals(
	//												COMBO_CHARGE))
	//										|| (gettxtAmt4().isVisible()
	//											&& getcbPymtCode4()
	//												.getSelectedItem()
	//												.equals(
	//												COMBO_CHARGE)))
	//									{
	//										// do not pop up
	//									}
	//									else
	//									{
	//										displayCreditFeeAddedMsg();
	//									}
	//								}
	//							}
	//							else if (aaKE.getSource() == gettxtAmt2())
	//							{
	//								if (getcbPymtCode2()
	//									.getSelectedItem()
	//									.equals(COMBO_CHARGE))
	//								{
	//									if ((gettxtAmt1().isVisible()
	//										&& getcbPymtCode1()
	//											.getSelectedItem()
	//											.equals(COMBO_CHARGE))
	//										|| (gettxtAmt3().isVisible()
	//											&& getcbPymtCode3()
	//												.getSelectedItem()
	//												.equals(COMBO_CHARGE))
	//										|| (gettxtAmt4().isVisible()
	//											&& getcbPymtCode4()
	//												.getSelectedItem()
	//												.equals(COMBO_CHARGE)))
	//									{
	//										// do not pop up
	//									}
	//									else
	//									{
	//										displayCreditFeeAddedMsg();
	//									}
	//								}
	//							}
	//							else if (aaKE.getSource() == gettxtAmt3())
	//							{
	//								if (getcbPymtCode3()
	//									.getSelectedItem()
	//									.equals(COMBO_CHARGE))
	//								{
	//									if ((gettxtAmt2().isVisible()
	//										&& getcbPymtCode2()
	//											.getSelectedItem()
	//											.equals(COMBO_CHARGE))
	//										|| (gettxtAmt1().isVisible()
	//											&& getcbPymtCode1()
	//												.getSelectedItem()
	//												.equals(COMBO_CHARGE))
	//										|| (gettxtAmt4().isVisible()
	//											&& getcbPymtCode4()
	//												.getSelectedItem()
	//												.equals(COMBO_CHARGE)))
	//									{
	//										// do not pop up
	//									}
	//									else
	//									{
	//										displayCreditFeeAddedMsg();
	//									}
	//								}
	//							}
	//							else if (aaKE.getSource() == gettxtAmt4())
	//							{
	//								if (getcbPymtCode4()
	//									.getSelectedItem()
	//									.equals(COMBO_CHARGE))
	//								{
	//									if ((gettxtAmt2().isVisible()
	//										&& getcbPymtCode2()
	//											.getSelectedItem()
	//											.equals(COMBO_CHARGE))
	//										|| (gettxtAmt3().isVisible()
	//											&& getcbPymtCode3()
	//												.getSelectedItem()
	//												.equals(COMBO_CHARGE))
	//										|| (gettxtAmt1().isVisible()
	//											&& getcbPymtCode1()
	//												.getSelectedItem()
	//												.equals(COMBO_CHARGE)))
	//									{
	//										// do not pop up
	//									}
	//									else
	//									{
	//										displayCreditFeeAddedMsg();
	//									}
	//								}
	//							}
	//						}
	//						performFwdRowMgmt(false);
	//					}
	//				}
	//			}
	//			catch (RTSException aeRTSEx)
	//			{
	//				aeRTSEx.displayError(this);
	//			}
	//		}
	//	end defect 7885
	//	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPaymentPMT001 laFrmPaymentPMT001;
			laFrmPaymentPMT001 = new FrmPaymentPMT001();
			laFrmPaymentPMT001.setModal(true);
			laFrmPaymentPMT001.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPaymentPMT001.show();
			Insets laInsets = laFrmPaymentPMT001.getInsets();
			laFrmPaymentPMT001.setSize(
				laFrmPaymentPMT001.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmPaymentPMT001.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmPaymentPMT001.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Process through the money lines.
	 * 
	 * @param abFromEnter boolean
	 * @return RTSInputField
	 * @throws RTSException
	 */
	private RTSInputField performFwdRowMgmt(boolean abFromEnter)
		throws RTSException
	{
		//recalculate and display
		reCalculateAndDisplayValidFields();
		Dollar ldZeroDollar = ZERO_DOLLAR;

		//do temporary calculations so no conflict with focusLost
		Dollar ldDollarRemittance = getCurrentRemittance();

		TransactionPaymentData laTransactionPaymentData = null;
		if (ldDollarRemittance.compareTo(cdDollarTotalDue) == -1)
		{
			if ((((caRTSInputFieldLastFocused == gettxtAmt1())
				&& (!((String) getcbPymtCode1().getSelectedItem())
					.equals(getDescFromCode(CHECK))))
				|| (caRTSInputFieldLastFocused == gettxtChkNo1()))
				|| (abFromEnter && ciCurrentIndex == 0))
			{
				//add second row
				if (gettxtAmt1()
					.getText()
					.trim()
					.equals(CommonConstant.STR_SPACE_EMPTY)
					|| new Dollar(
						gettxtAmt1().getText().trim()).compareTo(
						ldZeroDollar)
						== 0)
				{
					// do nothing
				}
				else if (!gettxtAmt2().isVisible())
				{
					ciCurrentIndex = ciCurrentIndex + 1;
					laTransactionPaymentData =
						createNewPaymentData(ciCurrentIndex, false);
					cvTransactionPaymentData.addElement(
						laTransactionPaymentData);
					getcbPymtCode2().setSelectedItem(
						getDescFromCode(
							laTransactionPaymentData.getPymntTypeCd()));
					gettxtAmt2().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtChkNo2().setText(
						CommonConstant.STR_SPACE_EMPTY);
					getcbPymtCode2().setVisible(true);
					getcbPymtCode2().setFocusable(true);
					gettxtAmt2().setVisible(true);
					gettxtAmt2().setFocusable(true);
					gettxtAmt2().requestFocus();
					return gettxtAmt2();
				}
			}
			else if (
				(((caRTSInputFieldLastFocused == gettxtAmt2())
					&& ((String) getcbPymtCode2().getSelectedItem()
						!= getDescFromCode(CHECK)))
					|| (caRTSInputFieldLastFocused == gettxtChkNo2()))
					|| (abFromEnter && ciCurrentIndex == 1))
			{
				//add third row
				if (gettxtAmt2()
					.getText()
					.trim()
					.equals(CommonConstant.STR_SPACE_EMPTY)
					|| new Dollar(
						gettxtAmt2().getText().trim()).compareTo(
						ldZeroDollar)
						== 0)
				{
					// do nothing
				}
				else if (!gettxtAmt3().isVisible())
				{
					ciCurrentIndex = ciCurrentIndex + 1;
					laTransactionPaymentData =
						createNewPaymentData(ciCurrentIndex, false);
					cvTransactionPaymentData.addElement(
						laTransactionPaymentData);
					getcbPymtCode3().setSelectedItem(
						getDescFromCode(
							laTransactionPaymentData.getPymntTypeCd()));
					gettxtAmt3().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtChkNo3().setText(
						CommonConstant.STR_SPACE_EMPTY);
					getcbPymtCode3().setVisible(true);
					getcbPymtCode3().setFocusable(true);
					gettxtAmt3().setVisible(true);
					gettxtAmt3().setFocusable(true);
					gettxtAmt3().requestFocus();
					return gettxtAmt3();
				}
			}
			else if (
				(((caRTSInputFieldLastFocused == gettxtAmt3())
					&& ((String) getcbPymtCode3().getSelectedItem()
						!= getDescFromCode(CHECK)))
					|| (caRTSInputFieldLastFocused == gettxtChkNo3()))
					|| (abFromEnter && ciCurrentIndex == 2))
			{
				//add fourth row
				if (gettxtAmt3()
					.getText()
					.trim()
					.equals(CommonConstant.STR_SPACE_EMPTY)
					|| new Dollar(
						gettxtAmt3().getText().trim()).compareTo(
						ldZeroDollar)
						== 0)
				{
					// do nothing
				}
				else if (!gettxtAmt4().isVisible())
				{
					ciCurrentIndex = ciCurrentIndex + 1;
					laTransactionPaymentData =
						createNewPaymentData(ciCurrentIndex, false);
					cvTransactionPaymentData.addElement(
						laTransactionPaymentData);
					getcbPymtCode4().setSelectedItem(
						getDescFromCode(
							laTransactionPaymentData.getPymntTypeCd()));
					gettxtAmt4().setText(
						CommonConstant.STR_SPACE_EMPTY);
					gettxtChkNo4().setText(
						CommonConstant.STR_SPACE_EMPTY);
					getcbPymtCode4().setVisible(true);
					getcbPymtCode4().setFocusable(true);
					gettxtAmt4().setVisible(true);
					gettxtAmt4().setFocusable(true);
					gettxtAmt4().requestFocus();
					return gettxtAmt4();
				}
			}
		}
		//when no rows are added
		RTSInputField laReturnField = null;
		switch (ciCurrentIndex)
		{
			case (0) :
				{
					laReturnField = gettxtAmt1();
					break;
				}
			case (1) :
				{
					laReturnField = gettxtAmt2();
					break;
				}
			case (2) :
				{
					laReturnField = gettxtAmt3();
					break;
				}
			case (3) :
				{
					laReturnField = gettxtAmt4();
					break;
				}
		}
		return laReturnField;
	}

	/**
	 * Populate the combo box
	 * 
	 * @param aaJCombobox JComboBox
	 * @throws RTSException
	 */
	protected void populateComboBox(JComboBox aaJCombobox)
		throws RTSException
	{
		if (cvDescription == null || cvCode == null)
		{
			cvDescription = new Vector();
			cvCode = new Vector();
			Vector lvPaymentTypeData =
				PaymentTypeCache.getPymntTypes(
					PaymentTypeCache.CUST_USE);

			// for CQU100003813
			PaymentTypeData laCash =
				(PaymentTypeData) lvPaymentTypeData.get(0);
			PaymentTypeData laCheck =
				(PaymentTypeData) lvPaymentTypeData.get(1);
			lvPaymentTypeData.set(0, laCheck);
			lvPaymentTypeData.set(1, laCash);
			// end defect fix

			if (lvPaymentTypeData == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					TXT_NULL_PYMNT_TYPE,
					TXT_ERROR);
			}
			int liSize = lvPaymentTypeData.size();
			if (liSize == 0)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					TXT_NULL_PYMNT_TYPE,
					TXT_ERROR);
			}
			for (int i = 0; i < liSize; i++)
			{
				PaymentTypeData laPaymentTypeData =
					(PaymentTypeData) lvPaymentTypeData.get(i);
				cvDescription.addElement(
					laPaymentTypeData.getPymntTypeCdDesc());
				cvCode.addElement(
					new Integer(laPaymentTypeData.getPymntTypeCd()));
			}
		}

		for (int i = 0; i < cvDescription.size(); i++)
		{
			aaJCombobox.addItem((String) cvDescription.get(i));
		}
		// defect 8479
		comboBoxHotKeyFix(aaJCombobox);
		// end defect 8479
	}

	/**
	 * Populate the TransactionPaymentData
	 * 
	 * @return Vector
	 * @throws RTSException
	 */
	protected Vector populateData() throws RTSException
	{
		Vector lvVector = new Vector();
		Dollar ldZeroDollar = ZERO_DOLLAR;

		for (int i = 0; i < ciCurrentIndex + 1; i++)
		{
			TransactionPaymentData laTransactionPaymentData =
				new TransactionPaymentData();
			switch (i)
			{
				case 0 :
					{
						if (gettxtAmt1()
							.getText()
							.trim()
							.equals(CommonConstant.STR_SPACE_EMPTY)
							|| new Dollar(
								gettxtAmt1().getText()).compareTo(
								ldZeroDollar)
								== 0)
						{
							continue;
						}
						laTransactionPaymentData.setPymntTypeAmt(
							new Dollar(gettxtAmt1().getText()));
						if (getcbPymtCode1()
							.getSelectedItem()
							.equals(COMBO_CHECK))
						{
							laTransactionPaymentData.setPymntCkNo(
								gettxtChkNo1().getText());
						}
						laTransactionPaymentData.setPymntTypeCd(
							getCodeFromDesc(
								(String) getcbPymtCode1()
									.getSelectedItem()));
						laTransactionPaymentData.setPymntType(
							(String) getcbPymtCode1()
								.getSelectedItem());
						break;
					}
				case 1 :
					{
						if (gettxtAmt2()
							.getText()
							.trim()
							.equals(CommonConstant.STR_SPACE_EMPTY)
							|| new Dollar(
								gettxtAmt2().getText()).compareTo(
								ldZeroDollar)
								== 0)
						{
							continue;
						}
						laTransactionPaymentData.setPymntTypeAmt(
							new Dollar(gettxtAmt2().getText()));
						if (getcbPymtCode2()
							.getSelectedItem()
							.equals(COMBO_CHECK))
						{
							laTransactionPaymentData.setPymntCkNo(
								gettxtChkNo2().getText());
						}
						laTransactionPaymentData.setPymntTypeCd(
							getCodeFromDesc(
								(String) getcbPymtCode2()
									.getSelectedItem()));
						laTransactionPaymentData.setPymntType(
							(String) getcbPymtCode2()
								.getSelectedItem());
						break;
					}
				case 2 :
					{
						if (gettxtAmt3()
							.getText()
							.trim()
							.equals(CommonConstant.STR_SPACE_EMPTY)
							|| new Dollar(
								gettxtAmt3().getText()).compareTo(
								ldZeroDollar)
								== 0)
						{
							continue;
						}
						laTransactionPaymentData.setPymntTypeAmt(
							new Dollar(gettxtAmt3().getText()));
						if (getcbPymtCode3()
							.getSelectedItem()
							.equals(COMBO_CHECK))
						{
							laTransactionPaymentData.setPymntCkNo(
								gettxtChkNo3().getText());
						}
						laTransactionPaymentData.setPymntTypeCd(
							getCodeFromDesc(
								(String) getcbPymtCode3()
									.getSelectedItem()));
						laTransactionPaymentData.setPymntType(
							(String) getcbPymtCode3()
								.getSelectedItem());
						break;
					}
				case 3 :
					{
						if (gettxtAmt4()
							.getText()
							.trim()
							.equals(CommonConstant.STR_SPACE_EMPTY)
							|| new Dollar(
								gettxtAmt4().getText()).compareTo(
								ldZeroDollar)
								== 0)
						{
							continue;
						}
						laTransactionPaymentData.setPymntTypeAmt(
							new Dollar(gettxtAmt4().getText()));
						if (getcbPymtCode4()
							.getSelectedItem()
							.equals(COMBO_CHECK))
						{
							laTransactionPaymentData.setPymntCkNo(
								gettxtChkNo4().getText());
						}
						laTransactionPaymentData.setPymntTypeCd(
							getCodeFromDesc(
								(String) getcbPymtCode4()
									.getSelectedItem()));
						laTransactionPaymentData.setPymntType(
							(String) getcbPymtCode4()
								.getSelectedItem());
						break;
					}
			}
			lvVector.addElement(laTransactionPaymentData);
		}

		if (lvVector.size() > 0)
		{
			TransactionPaymentData laTransactionPaymentData =
				(TransactionPaymentData) lvVector.get(0);

			laTransactionPaymentData.setChngDue(
				ZERO_DOLLAR.subtract(cdDollarChangeDue));

			if (cdDollarChangeDue.compareTo(ZERO_DOLLAR) == -1)
			{
				if (getradioCash().isSelected())
				{
					laTransactionPaymentData.setChngDuePymntTypeCd(
						CASH);
				}
				else
				{
					laTransactionPaymentData.setChngDuePymntTypeCd(
						CHECK);
				}
			}

		}
		//add a zero payment with change due
		if (lvVector.size() == 0 && getradioCash().isVisible())
		{
			TransactionPaymentData laTransactionPaymentData =
				new TransactionPaymentData();
			laTransactionPaymentData.setPymntTypeAmt(ZERO_DOLLAR);
			laTransactionPaymentData.setPymntTypeCd(CASH);
			laTransactionPaymentData.setPymntType(COMBO_CASH);
			laTransactionPaymentData.setChngDue(
				ZERO_DOLLAR.subtract(cdDollarChangeDue));
			if (getradioCash().isSelected())
			{
				laTransactionPaymentData.setChngDuePymntTypeCd(CASH);
			}
			else
			{
				laTransactionPaymentData.setChngDuePymntTypeCd(CHECK);
			}

			lvVector.addElement(laTransactionPaymentData);
		}

		//PCR41
		//CQU100004869 - Check if there is a credit card fee.

		CreditCardFeeData laCreditFeeData =
			CreditCardFeesCache.getCurrentCreditCardFees(
				SystemProperty.getOfficeIssuanceNo(),
				RTSDate.getCurrentDate());

		Dollar ldCreditFeeAmt = ZERO_DOLLAR;
		if (!gettxtAmtCharge()
			.getText()
			.trim()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			ldCreditFeeAmt =
				new Dollar(gettxtAmtCharge().getText().trim());
		}

		if (gettxtAmtCharge().isVisible()
			&& laCreditFeeData != null
			&& ldCreditFeeAmt.compareTo(ZERO_DOLLAR) > 0)
		{
			TransactionPaymentData laTransactionPaymentData =
				new TransactionPaymentData();
			laTransactionPaymentData.setPymntTypeAmt(
				new Dollar(gettxtAmtCharge().getText()));
			if (getcbPymtCodeCharge()
				.getSelectedItem()
				.equals(COMBO_CHECK))
			{
				laTransactionPaymentData.setPymntCkNo(
					gettxtChkNoCharge().getText());
			}
			laTransactionPaymentData.setPymntTypeCd(
				getCodeFromDesc(
					(String) getcbPymtCodeCharge().getSelectedItem()));
			laTransactionPaymentData.setPymntType(
				(String) getcbPymtCodeCharge().getSelectedItem());
			laTransactionPaymentData.setCreditCardFee(true);
			lvVector.addElement(laTransactionPaymentData);
		}
		return lvVector;
	}

	/**
	 * Recalculate Total due, total remittance and change due
	 */
	protected void reCalculateAndDisplayValidFields()
	{
		//calculate total remittance of valid fields
		boolean lbCalculate = false;
		cdDollarTotalDue = Transaction.getRunningSubtotal();

		cdDollarTotalRemittance = ZERO_DOLLAR;

		if (gettxtAmt1().isVisible()
			&& !gettxtAmt1().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			lbCalculate = validateDollarField(gettxtAmt1(), false);
			if (lbCalculate)
			{
				cdDollarTotalRemittance =
					cdDollarTotalRemittance.add(
						new Dollar(gettxtAmt1().getText()));
			}
		}

		if (gettxtAmt2().isVisible()
			&& !gettxtAmt2().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			lbCalculate = validateDollarField(gettxtAmt2(), false);
			if (lbCalculate)
			{
				cdDollarTotalRemittance =
					cdDollarTotalRemittance.add(
						new Dollar(gettxtAmt2().getText()));
			}
		}

		if (gettxtAmt3().isVisible()
			&& !gettxtAmt3().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			lbCalculate = validateDollarField(gettxtAmt3(), false);
			if (lbCalculate)
			{
				cdDollarTotalRemittance =
					cdDollarTotalRemittance.add(
						new Dollar(gettxtAmt3().getText()));
			}
		}

		if (gettxtAmt4().isVisible()
			&& !gettxtAmt4().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			lbCalculate = validateDollarField(gettxtAmt4(), false);
			if (lbCalculate)
			{
				cdDollarTotalRemittance =
					cdDollarTotalRemittance.add(
						new Dollar(gettxtAmt4().getText()));
			}
		}

		//change due
		cdDollarChangeDue =
			cdDollarTotalDue.subtract(cdDollarTotalRemittance);

		switch (cdDollarChangeDue.compareTo(ZERO_DOLLAR))
		{
			case -1 :
				{
					getstcLblChangeDue().setEnabled(true);
					getlblChangeDue().setText(
						ZERO_DOLLAR
							.subtract(cdDollarChangeDue)
							.printDollar());
					getlblChangeDue().setEnabled(true);
					getstcLblChangeType().setEnabled(true);
					getradioCash().setEnabled(true);
					getradioCash().setFocusable(true);
					getradioCheck().setEnabled(true);
					getradioCheck().setFocusable(true);
					break;
				}
			case 1 :
				{
					//same as 0	
				}
			case 0 :
				{
					getstcLblChangeDue().setEnabled(false);
					getlblChangeDue().setText(
						DOLLAR_SIGN + CommonConstant.STR_ZERO_DOLLAR);
					getlblChangeDue().setEnabled(false);
					getstcLblChangeType().setEnabled(false);
					getradioCash().setEnabled(false);
					getradioCash().setFocusable(false);
					getradioCheck().setEnabled(false);
					getradioCheck().setFocusable(false);
					break;
				}
			default :
				{
					// do nothing as a default
				}
		}

		//add credit card fee
		if (gettxtAmtCharge().isVisible()
			&& !gettxtAmtCharge().getText().trim().equals(
				CommonConstant.STR_SPACE_EMPTY))
		{
			cdDollarTotalDue =
				cdDollarTotalDue.add(
					new Dollar(gettxtAmtCharge().getText()));
			cdDollarTotalRemittance =
				cdDollarTotalRemittance.add(
					new Dollar(gettxtAmtCharge().getText()));
		}

		//total due
		
		// defect 10596
		if (cdDollarTotalDue.compareTo(cdMaxDollars)> 0)
		{
			RTSException leRTSPriceExp =
				new RTSException(
				RTSException.WARNING_MESSAGE,
				ERRMSG_TOTAL_REMITTANCE,
				ERRMSG_ERROR);
				leRTSPriceExp.displayError();
		}
		// end defect 10596
		 
		getlblTotalDue().setText(cdDollarTotalDue.printDollar());

		//total remittance
		getlblTotalRemittance().setText(
			cdDollarTotalRemittance.printDollar());
		//change due
		getlblChangeDue().setText(
			cdDollarTotalRemittance
				.subtract(cdDollarTotalDue)
				.printDollar());

		//credit remaining
		if (Transaction.getCreditRemaining() != null
			&& Transaction.getCreditRemaining().compareTo(ZERO_DOLLAR)
				!= 0)
		{
			getstclblCreditRemaining().setVisible(true);
			getlblCreditRemaining().setVisible(true);
			getlblCreditRemaining().setText(
				Transaction.getCreditRemaining().printDollar());
		}
	}

	/**
	 * Get CompleteTransactionData and use it to populate screen
	 * 
	 * @param aaData
	 */
	public void setData(Object aaData)
	{
		try
		{
			caCompleteTransactionData =
				(CompleteTransactionData) aaData;
			cdDollarTotalDue = Transaction.getRunningSubtotal();

			cdDollarTotalRemittance = ZERO_DOLLAR;

			cvTransactionPaymentData = new Vector();

			//PCR25
			caCreditFeeInfo =
				(CreditCardFeeData) UtilityMethods.copy(
					CreditCardFeesCache.getCurrentCreditCardFees(
						SystemProperty.getOfficeIssuanceNo(),
						new RTSDate()));
			if (caCreditFeeInfo == null)
			{
				caCreditFeeInfo = new CreditCardFeeData();
				caCreditFeeInfo.setItmPrice(ZERO_DOLLAR);
			}

			// row items
			TransactionPaymentData laTransactionPaymentData = null;
			Dollar laDollarAmt = null;
			switch (cvTransactionPaymentData.size())
			{
				case 4 :
					{
						laTransactionPaymentData =
							(
								TransactionPaymentData) cvTransactionPaymentData
									.get(
								3);
						getcbPymtCode4().setVisible(true);
						getcbPymtCode4().setFocusable(true);
						getcbPymtCode4().setSelectedItem(
							getDescFromCode(
								laTransactionPaymentData
									.getPymntTypeCd()));
						gettxtAmt4().setVisible(true);
						gettxtAmt4().setFocusable(true);
						laDollarAmt =
							laTransactionPaymentData.getPymntTypeAmt();
						cdDollarTotalRemittance.add(laDollarAmt);
						gettxtAmt4().setText(laDollarAmt.toString());
						if (laTransactionPaymentData.getPymntTypeCd()
							== CHECK)
						{
							gettxtChkNo4().setVisible(true);
							gettxtChkNo4().setFocusable(true);
							gettxtChkNo4().setText(
								laTransactionPaymentData
									.getPymntCkNo());
						}
					}
				case 3 :
					{
						laTransactionPaymentData =
							(
								TransactionPaymentData) cvTransactionPaymentData
									.get(
								2);
						getcbPymtCode3().setVisible(true);
						getcbPymtCode3().setFocusable(true);
						getcbPymtCode3().setSelectedItem(
							getDescFromCode(
								laTransactionPaymentData
									.getPymntTypeCd()));
						gettxtAmt3().setVisible(true);
						gettxtAmt3().setFocusable(true);
						laDollarAmt =
							laTransactionPaymentData.getPymntTypeAmt();
						cdDollarTotalRemittance.add(laDollarAmt);
						gettxtAmt3().setText(laDollarAmt.toString());
						if (laTransactionPaymentData.getPymntTypeCd()
							== CHECK)
						{
							gettxtChkNo3().setVisible(true);
							gettxtChkNo3().setFocusable(true);
							gettxtChkNo3().setText(
								laTransactionPaymentData
									.getPymntCkNo());
						}
					}
				case 2 :
					{
						laTransactionPaymentData =
							(
								TransactionPaymentData) cvTransactionPaymentData
									.get(
								1);
						getcbPymtCode2().setVisible(true);
						getcbPymtCode2().setFocusable(true);
						getcbPymtCode2().setSelectedItem(
							getDescFromCode(
								laTransactionPaymentData
									.getPymntTypeCd()));
						gettxtAmt2().setVisible(true);
						gettxtAmt2().setFocusable(true);
						laDollarAmt =
							laTransactionPaymentData.getPymntTypeAmt();
						cdDollarTotalRemittance.add(laDollarAmt);
						gettxtAmt2().setText(laDollarAmt.toString());
						if (laTransactionPaymentData.getPymntTypeCd()
							== CHECK)
						{
							gettxtChkNo2().setVisible(true);
							gettxtChkNo2().setFocusable(true);
							gettxtChkNo2().setText(
								laTransactionPaymentData
									.getPymntCkNo());
						}
					}
				case 1 :
					{
						laTransactionPaymentData =
							(
								TransactionPaymentData) cvTransactionPaymentData
									.get(
								0);
						getcbPymtCode1().setSelectedItem(
							getDescFromCode(
								laTransactionPaymentData
									.getPymntTypeCd()));
						laDollarAmt =
							laTransactionPaymentData.getPymntTypeAmt();
						cdDollarTotalRemittance.add(laDollarAmt);
						gettxtAmt1().setText(laDollarAmt.toString());
						if (laTransactionPaymentData.getPymntTypeCd()
							!= CHECK)
						{
							gettxtChkNo1().setVisible(false);
							gettxtChkNo1().setFocusable(false);
						}
						else
						{
							gettxtChkNo1().setText(
								laTransactionPaymentData
									.getPymntCkNo());
						}
						break;
					}
				case 0 :
					{
						laTransactionPaymentData =
							createNewPaymentData(ciCurrentIndex, true);
						cvTransactionPaymentData.addElement(
							laTransactionPaymentData);
						getcbPymtCode1().setSelectedItem(
							getDescFromCode(
								laTransactionPaymentData
									.getPymntTypeCd()));
						gettxtAmt1().setText(
							CommonConstant.STR_SPACE_EMPTY);
						gettxtChkNo1().setText(
							CommonConstant.STR_SPACE_EMPTY);
						gettxtAmt1().requestFocus();
						break;
					}
				default :
					{
						// do nothing on default
					}
			}
			// recal and display total due, total remittance and
			// change due
			reCalculateAndDisplayValidFields();

			//only for initial display
			getlblChangeDue().setText(ZERO_DOLLAR.printDollar());

			gettxtAmt1().requestFocus();
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
	}

	/**
	 * Check all the other combo and see if charge is selected
	 * 
	 * @param aiCurrentChangeCombo JComboBox
	 * @return boolean
	 */
	private boolean shouldShowCreditCardFee(int aiCurrentChangeCombo)
	{

		if (caCreditFeeInfo.getItmPrice().equals(ZERO_DOLLAR))
		{
			return false;
		}
		switch (aiCurrentChangeCombo)
		{
			case 1 :
				{
					if (getcbPymtCode2().isVisible()
						&& getcbPymtCode2().getSelectedItem().equals(
							COMBO_CHARGE))
					{
						return true;
					}
					else if (
						getcbPymtCode3().isVisible()
							&& getcbPymtCode3().getSelectedItem().equals(
								COMBO_CHARGE))
					{
						return true;
					}
					else if (
						getcbPymtCode4().isVisible()
							&& getcbPymtCode4().getSelectedItem().equals(
								COMBO_CHARGE))
					{
						return true;
					}
					break;
				}
			case 2 :
				{
					if (getcbPymtCode1().isVisible()
						&& getcbPymtCode1().getSelectedItem().equals(
							COMBO_CHARGE))
					{
						return true;
					}
					else if (
						getcbPymtCode3().isVisible()
							&& getcbPymtCode3().getSelectedItem().equals(
								COMBO_CHARGE))
					{
						return true;
					}
					else if (
						getcbPymtCode4().isVisible()
							&& getcbPymtCode4().getSelectedItem().equals(
								COMBO_CHARGE))
					{
						return true;
					}
					break;
				}
			case 3 :
				{
					if (getcbPymtCode2().isVisible()
						&& getcbPymtCode2().getSelectedItem().equals(
							COMBO_CHARGE))
					{
						return true;
					}
					else if (
						getcbPymtCode1().isVisible()
							&& getcbPymtCode1().getSelectedItem().equals(
								COMBO_CHARGE))
					{
						return true;
					}
					else if (
						getcbPymtCode4().isVisible()
							&& getcbPymtCode4().getSelectedItem().equals(
								COMBO_CHARGE))
					{
						return true;
					}

					break;
				}
			case 4 :
				{
					if (getcbPymtCode2().isVisible()
						&& getcbPymtCode2().getSelectedItem().equals(
							COMBO_CHARGE))
					{
						return true;
					}
					else if (
						getcbPymtCode3().isVisible()
							&& getcbPymtCode3().getSelectedItem().equals(
								COMBO_CHARGE))
					{
						return true;
					}
					else if (
						getcbPymtCode1().isVisible()
							&& getcbPymtCode1().getSelectedItem().equals(
								COMBO_CHARGE))
					{
						return true;
					}

					break;
				}
		}
		return false;
	}

	/**
	 * Show or not show Credit Card Info.
	 * 
	 * @param abShow boolean
	 */
	private void showCreditCardInfo(boolean abShow)
	{
		getstcLblCreditCardFee().setVisible(abShow);

		//select charge initially
		if (!getcbPymtCodeCharge().isVisible())
		{
			getcbPymtCodeCharge().setSelectedItem(COMBO_CHARGE);
		}

		getcbPymtCodeCharge().setVisible(abShow);
		getcbPymtCodeCharge().setFocusable(abShow);
		gettxtAmtCharge().setVisible(abShow);

		if (!abShow)
		{
			gettxtAmtCharge().setText(CommonConstant.STR_SPACE_EMPTY);
		}

		//		// defect 7701
		//		// only change the state when needed
		//		if ((getstcLblCreditCardFee().isVisible() && !abShow)
		//			|| (!getstcLblCreditCardFee().isVisible() && abShow))
		//		{
		//			getstcLblCreditCardFee().setVisible(abShow);
		//		}
		//
		//		//select charge initially
		//		if (!getcbPymtCodeCharge().isVisible()
		//			&& getcbPymtCodeCharge().getSelectedItem().
		//											equals(COMBO_CHARGE))
		//		{
		//			getcbPymtCodeCharge().setSelectedItem(COMBO_CHARGE);
		//		}
		//
		//		// only change state when needed
		//		if ((getcbPymtCodeCharge().isVisible() && !abShow)
		//			|| (!getcbPymtCodeCharge().isVisible() && abShow))
		//		{
		//			getcbPymtCodeCharge().setVisible(abShow);
		//			gettxtAmtCharge().setVisible(abShow);
		//		}
		//
		//		if (!abShow && !gettxtAmtCharge().getText().equals(
		//				CommonConstant.STR_SPACE_EMPTY))
		//		{
		//			gettxtAmtCharge().setText(CommonConstant.STR_SPACE_EMPTY);
		//		}
		//		// end defect 7701
	}

	/**
	 * Validates the text field
	 * 
	 * @param aaRTSInputField RTSInputField
	 * @return RTSException
	 */
	protected RTSException validateDollarField(RTSInputField aaRTSInputField)
	{
		//clearAllColor(this);
		//Validation
		double ldAmt;
		// defect 7885
		// variable not used
		//Dollar laDollarAmt = null;
		// end defect 7885
		String lsTxtAmt = aaRTSInputField.getText().trim();

		//number validation
		try
		{
			if (!lsTxtAmt.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				ldAmt = Double.parseDouble(lsTxtAmt);

				//range validation
				if (ldAmt >= MAX_AMOUNT)
				{
					return new RTSException(150);
				}

				//decimal validation
				int liDotIndex = lsTxtAmt.indexOf(DOT);
				if (liDotIndex != -1)
				{
					int liStringLength = lsTxtAmt.length();
					if ((liStringLength - liDotIndex - 1)
						> DECIMAL_NUMBER)
					{
						return new RTSException(150);
					}
				}
			}
		}
		catch (NumberFormatException aeNFEx)
		{
			return new RTSException(150);
		}

		return null;
	}

	/**
	 * Validates the text field
	 * 
	 * @param aaRTSInputField RTSInputField
	 * @param abWarningPopup boolean
	 * @return boolean
	 */
	protected boolean validateDollarField(
		RTSInputField aaRTSInputField,
		boolean abWarningPopup)
	{
		//Validation
		double ldAmt;
		// defect 7885
		// variable not used
		//Dollar laDollarAmt = null;
		// end defect 7885
		String lsTxtAmt = aaRTSInputField.getText().trim();

		//number validation
		if (!lsTxtAmt.equals(CommonConstant.STR_SPACE_EMPTY)
			&& (abWarningPopup))
		{
			// defect 7885
			// Moved from beginning of method to inside if statement
			clearAllColor(aaRTSInputField);
			// end defect 7885
			try
			{
				ldAmt = Double.parseDouble(lsTxtAmt);

			}
			catch (NumberFormatException aeNFEx)
			{
				if (abWarningPopup)
				{
					displayAmtFieldError(aaRTSInputField);
				}
				return false;
			}

			//range validation
			if (ldAmt >= MAX_AMOUNT)
			{
				if (abWarningPopup)
				{
					displayAmtFieldError(aaRTSInputField);
				}
				return false;
			}

			//decimal validation
			int liDotIndex = lsTxtAmt.indexOf(DOT);
			if (liDotIndex != -1)
			{
				int liStringLength = lsTxtAmt.length();
				if ((liStringLength - liDotIndex - 1) > DECIMAL_NUMBER)
				{
					if (abWarningPopup)
					{
						displayAmtFieldError(aaRTSInputField);
					}
					return false;
				}
			}
		}
		return true;
	}
}
