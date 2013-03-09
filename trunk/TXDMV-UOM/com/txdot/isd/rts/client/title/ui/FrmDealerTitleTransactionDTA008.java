package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.CertifiedLienholderCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *
 * FrmDealerTitleTransactionDTA008.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validations update
 * N Ting		04/15/2002	disable checkbox Reject Entire Batch in 
 *							PrintImmediate On Scenario
 *							defect 3496
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()  
 * T Pederson	04/26/2002	Corrected display of error 508 in setData
 * 							to keep os/2 window from flashing.
 * J Rue		06/27/2002	Remove leading zeros from the 
 * 							NewRegExpStkr number 
 * 							add removeLeadingZeros() 
 * 							modify setDataToDataObject()
 * 							defect 4368 
 * J Rue		07/02/2002	Added checks for Form31, NewPltNo and 
 * 							NewStkrNo fields lengths. 
 * 							modify actionPerformed()
 * 							defect 4393 
 * B Arredondo	12/16/2002	Made changes for the user help guide so 
 * 							had to make changes in actionPerformed().
 *							defect 5147
 * B Brown		02/05/2004	Fix the multiple cursor issue by not 
 *							requesting focus with an exception. Change 
 *							method setData -- comment out this line: 
 *							displayException.getFirstComponent().
 *							requestFocus();
 *        					defect 6047 Version 5.1.6.
 * B Arredondo	02/20/2004	Modifiy visual composition to change
 *							defaultCloseOperation to 
 *							DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Min Wang     03/29/2004  Fix 3 cursors by completing the second one 
 *							and rejecting it when you go back to the
 *							DTA008 screen. This is the one where you 
 *							cannot go forward.
 *							modify setData(), actionPerformed()
 *							defect 6980 Ver 5.1.6
 * Min Wang		05/06/2004	Fix double cursors after finishing input 
 *							field's validation.
 *							modify actionPerformed()
 * 							defect 7051  Ver 5.1.6
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Import from 5.2.0
 * 							Ver 5.2.0	
 * T Pederson	07/21/2004	Removed GridBagLayout from frame
 * 							defect 7256  Ver 5.2.1
 * T Pederson	08/09/2004	Cleared new sticker number if on disk
 *							modify setData() 
 * 							defect 7362  Ver 5.2.1
 * J Rue		08/16/2004	If "Print Sticker" is check, 
 *							NewRegExpMo/Yr/New Plate must exist.
 *							Add NewRegExpMo/Yr to if statement in
 *							setData()
 *							modify actionPerformed(), setData()
 *							defect 7438 Ver 5.2.1
 * J Rue		08/17/2004	Set select method vars. to blank 
 *							modify setData()
 *							defect 7438 Ver 5.2.1
 * J Rue		08/18/2004	Add ErrMsgNo 743 to display if 
 *							NewRegExpMo/Yr=0 if chkPrint is selected. 
 *							modify actionPerformed()
 *							defect 7438 Ver 5.2.1
 * J Rue		08/25/2004	DealerTitleData.NewRegExpMo() and 
 *							DealerTitleData.NewRegExpYr() were 
 *							converted to integer.
 *							modify setData(), setDataToDataObject(),
 *							actionPerformed()
 *							defect 7496 Ver 5.2.1
 * J Rue		08/31/2004	Add ErrMsgNo 743 to display if 
 *							NewRegExpMo/Yr=0
 *							and Print Sticker chk box is selected.
 *							Do not set Print Sticker check box to red
 *							modify actionPerformed()
 *							defect 7512 Ver 5.2.1
 * J Rue		08/31/2004	Add criteria if Record has NOT been 
 *							processed. 
 *							Set Print Sticker check box to default 
 *							else keep user input/selection
 *							modify setData()
 *							defect 7511 Ver 5.2.1
 * J Rue		09/01/2004	Check if NewRegExpMo and NewRegExpYr are 
 *							blank. Set fields to 0.
 *							modify setDataToDataObject()
 *							defect 7496 Ver 5.2.1
 * J Rue		09/08/2004	If batch was rejected, process at front of 
 *							actionPerformed()
 *							modify actionPerformed()
 *							defect 7528 Ver 5.2.1
 * J Rue		11/23/2004	Allow processing to continue if transaction
 *							was VOIDED at RSPS.
 *							modify actionPerformed()
 *							defect 7397 Ver 5.2.2
 * K Harrell	12/16/2004	Change setNewNewStkrNo() to setNewStkrNo()
 *							modify setData() 
 *							defect 7736  Ver 5.2.2
 * J Rue		01/03/2005	Do not check if trans had been process thru
 *							POS if rejected.
 *							JavaDoc/Formatting/Variable Name Cleanup
 *							modify actionPerformed()
 *							defect 7841 Ver 5.2.2
 * K Harrell	02/03/2005	Disable print sticker when processing RSPS
 *	& J Rue					diskette. On subsequent manual entry, same
 *							custseqno, enable.
 *							modify initializeForManual(),setData()  
 *							defect 7965 Ver 5.2.2
 * K Harrell	02/14/2005	Assign Input type of 6 
 * 							(AlphaNumeric_NoSpace) to Form31 and 
 * 							New PlateNo input fields
 *							modify via Visual Composition
 *							defect 7991 Ver 5.2.2.1  
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * K Harrell	03/23/2005	Merged 7991 (2/14/2005 Ver 5.2.2.1)
 * 							modify gettxtPlateNo(),gettxtForm31()
 * 							Ver 5.2.3
 * J Rue		03/23/2005	Change DTA008A to DTA008
 * 							Cleanup code
 * 							modify actionPerformed(), setData()
 * 							defect 6963,7898 Ver 5.2.3
 * J Rue		03/25/2005	Remove the Java v1.3 double cursor fix.
 * 							Defects 6980, 7051, 6047
 * 							modify setData(), actionPerformed()
 * 							defect 7312 Ver 5.2.3
 * J Rue		03/25/2005	Remove the Java v1.3 double cursor fix to 
 * 							ensure DTA/More Trans/Keyboard the default
 * 							is the DlrTransDate field
 * 							Defects 6980, 7051, 6047
 * 							modify setData()
 * 							defect 7313 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set fields into panels for tabbing.
 * 							defect 7898 Ver5.2.3
 * J Rue		03/31/2005	Comment out all setNextFocusableComponent()
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/04/2005	Remove TODOs from setNextFocusableComponent
 * 							defect 7898 Ver 5.23
 * J Rue		04/19/2005	Remove unused variables.
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/19/2005	Do not reset backround/foreground to 
 * 							white/black
 * 							modify focusLost()
 * 							defect 7529 Ver 5.2.3
 * J Rue		06/15/2005	Match RSPS getters/setters set in 
 * 							DealerTitleData to better define their 
 * 							meaning
 * 							modify setData(), actionPerformed()
 * 							defect 8217 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3     
 * J Rue		06/23/2005	Update comments for isProcessed() in
 * 							setData(). Move local vars to class level.
 *							defect 7898 Ver 5.2.3      
 * J Rue		07/18/2005	Update comments for isProcessed() in
 * 							setData(). Move local vars to class level.
 *							defect 7898 Ver 5.2.3   
 * J Rue		08/17/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		08/25/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3  
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3  
 * J Rue		12/01/2005	NewRegExpMo/Yr label fields were flip.
 * 							modify getstcLblNewRegMo(),  
 * 							modify getstcLblNewRegYr()
 * 							defect 7898 Ver 5.2.3  
 * J Rue		12/02/2005	Code cleanup.
 * 							Replace try/catch with isNumeric()
 * 							Change constant setting to MIN_NO_MONTHS = 1
 * 							modify actionPerformed()
 * 							defect 7898 Ver 5.2.3
 * Jeff S.		02/03/2005	Focus was not going to enter on a no vin
 * 							no plate situation.  This is a temp fix.
 * 							modify initialize(), focusGained()
 * 							defect 7898 Ver 5.2.3
 * Jeff S.		06/26/2006	Used screen constant for CTL001 Title.
 * 							remove CONFIRMACTION, CONFIRM_ACTION
 * 							modify setData(), actionPerformed()
 * 							defect 8756 Ver 5.2.3
 * K Harrell	11/05/2007	DTA PTO work 
 * 							add ivjchkCustSupplied
 * 							add getchkCustSupplied()
 * 							delete getBuilderData(), removeLeadingZeros(),
 * 							  validateInv()
 * 							modify setData(), actionPerformed(), 
 * 							 getJPanel1(), initializeForManual(), 
 * 							 setDataToDataObject() 
 * 							defect 9425 Ver Special Plates 2
 * K Harrell	11/12/2007	Add Plate Age Processing 	
 * 							add ivjstcLblPlateAge,ivjtxtPlateAge,
 * 							 MAX_PLT_AGE, CUST_SUPPLIED, PLATE_AGE
 * 							add getRegPltAge()
 *  						modify actionPerformed(),setData(), 
 * 							  setDataToDataObject(), initialize()  
 * 							defect 9425 Ver Special Plates 2
 * K Harrell	05/25/2008	Set Customer Supplied Plate Info 
 * 							modify setData(), setDataToDataObject()
 * 							defect 9656 Ver Defect_POS_A
 * J Rue		12/30/2008	Save Form31No for rejected records
 * 							modify rejectTrans()
 * 							defect 8921 Ver Defect_POS_D
 * K Harrell	03/27/2009	Add DTA ELT data validation
 * 							add LIEN_MSG_SUFFIX, VALID_LIEN_RQD_MSG,
 * 							  MULT_LIEN_FOR_ETTL_MSG,
 * 							  NO_ADDL_LIEN_FOR_ETTL_MSG,
 * 							  ERR_ADDL_LIEN_REMOVED,
 * 							  ERR_ETITLE_RQST_REMOVED
 * 							add verifyLien() 
 * 							modify setData() 
 * 							defect 9979 Ver Defect_POS_E
 * K Harrell	07/02/2009	Implement new DealerData, LienholderData
 * 							modify verifyLien(), setData(), 
 * 							 initializeForManual()  
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	07/18/2009	Implement error detection of ETitle 
 * 							Certfd Lienholder for non-ETitle request.
 * 							modify verifyLien()  
 * 							defect 10124 Ver Defect_POS_F 
 * J Rue		09/21/2009	setNewStkrNo() remove in v6.1.0
 * 							modify setData()
 * 							defect 10232 Ver Defect_POS_F 
 * K Harrell	12/28/2009	DTA Cleanup
 * 							add validateData(), initForNewTrans(),
 * 							 setUpForNextTrans(), validateForm31(),
 * 							 csDealerId, csDealerSeqNo 
 * 							modify actionPerformed(), 
 * 							  setDataToDataObject()  
 * 							delete cvDealerTitleData
 * 							defect 10290 Ver Defect_POS_H   
 * K Harrell	01/05/2010	Corrected handling of iterator w/ Keyboard
 * 							modify rejectTrans()
 * 							defect 10290 Ver Defect_POS_H 
 * K Harrell	01/07/2010	Typo on assignment of DealerId
 * 							modify initDealerInfo()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	01/13/2010	add assignment to 
 * 								Transaction.svDTAOrigDlrTtlData
 * 							modify setData()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	03/12/2010 	Implement new Constant for 427
 * 							modify validateData()  
 * 							defect 10355 Ver POS_640  
 * K Harrell	11/11/2010	Clear TTL035 entry in vault
 * 							modify initForNewTrans() 
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	01/16/2012	Do not allow the processing of DTA trans
 * 							where SurvivorShipRights = 1  after 
 * 							 SystemProperty DTARejectSurvivorDate
 * 							modify setupForNextTrans() 
 * 							defect 10827 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Frame Dealer Title Transaction DTA008
 * 
 * @version	6.10.0			01/16/2012	
 * @author	A Yang
 * @author  Jeff Rue
 * @author  Kathy Harrell
 * <br>Creation Date:		08/17/2001 15:35.32
 */

public class FrmDealerTitleTransactionDTA008
	extends RTSDialogBox
	implements ActionListener, WindowListener, FocusListener
{
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private ButtonPanel ivjbtnECH = null;
	private JLabel ivjlblDealerName1 = null;
	private JLabel ivjlblDealerName2 = null;
	private JLabel ivjlbDealerId = null;
	private JLabel ivjstcLblDealerId = null;
	private JLabel ivjstcLblDealerName = null;
	private JLabel ivjstcLblFeePd = null;
	private JLabel ivjstcLblTrnsDt = null;
	private JLabel ivjlblTransactionNo = null;
	private JLabel ivjstcLblTrans = null;
	private JLabel ivjstcLblForm31 = null;
	private JLabel ivjstcLblNewRegMo = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblRegYr = null;
	private RTSDateField ivjtxtTransactionDate = null;
	private RTSInputField ivjtxtFeePd = null;
	private RTSInputField ivjtxtExpMo = null;
	private RTSInputField ivjtxtExpYr = null;
	private RTSInputField ivjtxtForm31 = null;
	private RTSInputField ivjtxtPlateNo = null;
	private JCheckBox ivjchkRejectThisTrans = null;
	private JCheckBox ivjchkPrint = null;
	private JCheckBox ivjchkRejEntBatch = null;

	// defect 9425 
	private JLabel ivjstcLblPlateAge = null;
	private JCheckBox ivjchkCustSupplied = null;
	private RTSInputField ivjtxtPlateAge = null;
	private final static int MAX_PLT_AGE = 7;
	private final static String CUST_SUPPLIED = "Customer Supplied";
	private final static String PLATE_AGE = "Plate Age:";
	// end defect 9425 

	// Variables
	private boolean cbShow = true;
	private int ciNoRejectedTrans = 0;
	private int ciIterator = -1;
	private int ciTransCount = 0;

	// defect 10290 
	private String csDealerId = new String();
	private String csDealerSeqNo = new String();
	// end defect 10290 

	// Constants int
	private final static int MONTH_MAX_LEN = 2;
	private final static int YEAR_MAX_LEN = 4;
	private final static int FEES_PAID_MAX_LEN = 11;
	private final static int FORM31NO_MAX_LEN = 7;
	private final static int PLT_NO_MAX_LEN = 7;
	private final static int MAX_BATCH_NO = 25;

	// Constants String
	private final static String INV_FORM31NO = "FORM31";
	private final static String NEW_REG_EXP_MO = "New Reg Exp Mo:";
	private final static String NEW_PLATE_NO = "New Plate No:";
	private final static String NEW_REG_EXP_YR = "New Reg Exp Yr:";
	private final static String TRANSACTION = "Transaction:";
	private final static String TRANSACTION_DATE = "Transaction Date:";
	private final static String INVENTORY = "Inventory:";
	private final static String FORM31NO = "Form 31 No:";
	private final static String FEES_PAID = "Fees Paid:";
	private final static String DEALER_NAME = "Dealer Name:";
	private final static String DEALER_ID = "Dealer Id:";
	private final static String REJECT_ENTIRE_BATCH =
		"Reject Entire Batch";
	private final static String REJECT_TRANS =
		"Reject (or Cancel) this Transaction";
	private final static String PRNT_STKR = "Print Sticker";
	private final static String ZERO = CommonConstant.STR_ZERO;
	private final static String MORETRANSPRCS =
		"Do you have any more transactions to enter?";
	private final static String CANCEL_ALL_TRANS_BATCH =
		"Do you really want to cancel all of \n "
			+ "the transactions in this batch?";
	private final static String RECS_BEEN_PRCS =
		"This record has already been processed.  "
			+ "Continue and process record again?";

	// defect 9979 
	private final static String LIEN_MSG_SUFFIX =
		"  Please reject this transaction or re-specify the "
			+ "Lienholder Information on TTL035.";
	private final static String VALID_LIEN_RQD_MSG =
		"A single, valid Lienholder Id, name and address must be "
			+ "specified for an ETitle Request. "
			+ "The ETitle Request will be ignored.";
	private final static String MULT_LIEN_FOR_ETTL_MSG =
		"Multiple Lienholders were specified for an"
			+ " Electronic Title. The ETitle Request will be ignored.";
	private final static String NO_ADDL_LIEN_FOR_ETTL_MSG =
		"The Additional Lien Indicator cannot be set for an"
			+ " Electronic Title.  The Indicator will be ignored.";
	private final static String ERR_ADDL_LIEN_REMOVED =
		"Additional Lien Request removed.";
	private final static String ERR_ETITLE_RQST_REMOVED =
		"ETitle Request removed.";
	// end defect 9979

	// defect 10290
	//private Vector cvDealerTitleData = null;
	// end defect 10290 

	// Objects
	private DealerTitleData caCurrDlrTtlData = null;

	/**
	 * FrmDealerTitleTransactionDTA008 constructor.
	 */
	public FrmDealerTitleTransactionDTA008()
	{
		super();
		initialize();
	}

	/**
	 * FrmDealerTitleTransactionDTA008 constructor.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDealerTitleTransactionDTA008(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmDealerTitleTransactionDTA008 constructor.
	 * 
	 * @param aaParent JFrame
	
	 */
	public FrmDealerTitleTransactionDTA008(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an Enter/Cancel/Help is pressed
	 *
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}

		try
		{
			clearAllColor(this);

			RTSException laRTSEx;

			// defect 10290 
			// reorganize for clarity

			// CANCEL || (ENTER & Reject Entire Batch) 
			if (aaAE.getSource() == getbtnECH().getBtnCancel()
				|| (aaAE.getSource() == getbtnECH().getBtnEnter()
					&& getchkRejEntBatch().isSelected()))
			{
				laRTSEx =
					new RTSException(
						RTSException.CTL001,
						CANCEL_ALL_TRANS_BATCH,
						ScreenConstant.CTL001_FRM_TITLE);

				if (laRTSEx.displayError(this) == RTSException.YES)
				{
					getController().processData(
						VCDealerTitleTransactionDTA008.CANCEL,
						null);
				}
			}
			else if (aaAE.getSource() == getbtnECH().getBtnEnter())
			{
				if (getchkRejectThisTrans().isSelected())
				{
					rejectTrans();
				}
				else
				{
					if (!validateData())
					{
						return;
					}
					setDataToDataObject();

					if (caCurrDlrTtlData.isPOSProcsIndi())
					{
						laRTSEx =
							new RTSException(
								RTSException.CTL001,
								RECS_BEEN_PRCS,
								ScreenConstant.CTL001_FRM_TITLE);

						if (laRTSEx.displayError(this)
							== RTSException.NO)
						{
							rejectTrans();
						}
					}
				}
				getController().processData(
					AbstractViewController.ENTER,
					caCurrDlrTtlData);
			}
			// end defect 10290 
			// HELP 
			else if (aaAE.getSource() == getbtnECH().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.DTA008);
			}
			// CUSTOMER SUPPLIED 
			else if (aaAE.getSource() == getchkCustSupplied())
			{
				if (getchkCustSupplied().isSelected())
				{
					getstcLblPlateAge().setEnabled(true);
					gettxtPlateAge().setEnabled(true);
					gettxtPlateAge().requestFocus();
				}
				else
				{
					getstcLblPlateAge().setEnabled(false);
					gettxtPlateAge().setText("");
					gettxtPlateAge().setEnabled(false);
				}
			}
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
		finally
		{
			doneWorking();
		}
	}

	/** 
	 * Set DlrTtlTrans Data To Display  
	 * 
	 */
	private void setDlrTtlTransInfoToDisplay()
	{
		Dollar laDlrFee = caCurrDlrTtlData.getFee();
		String lsForm31 = caCurrDlrTtlData.getForm31No();
		String lsNewPlt = caCurrDlrTtlData.getNewPltNo();
		int liNewRegMo = caCurrDlrTtlData.getNewRegExpMo();
		int liNewRegYr = caCurrDlrTtlData.getNewRegExpYr();

		// TransDate 
		String lsTransDt = caCurrDlrTtlData.getTransDate();

		if (!UtilityMethods.isEmpty(lsTransDt))
		{
			int liLnDt = Integer.parseInt(lsTransDt);

			RTSDate laRTSDate = new RTSDate(RTSDate.YYYYMMDD, liLnDt);

			gettxtTransactionDate().setDate(laRTSDate);
		}

		// Fee Paid 
		gettxtFeePd().setText(
			laDlrFee == null
				? CommonConstant.STR_SPACE_EMPTY
				: laDlrFee.printDollar(false));

		// Form 31 
		gettxtForm31().setText(
			lsForm31 == null
				? CommonConstant.STR_SPACE_EMPTY
				: lsForm31);

		// Exp Mo 
		gettxtExpMo().setText(
			liNewRegMo == 0
				? CommonConstant.STR_SPACE_EMPTY
				: String.valueOf(liNewRegMo));

		// Exp Yr 
		gettxtExpYr().setText(
			liNewRegYr == 0
				? CommonConstant.STR_SPACE_EMPTY
				: String.valueOf(liNewRegYr));

		// New Plate 
		gettxtPlateNo().setText(
			lsNewPlt == null
				? CommonConstant.STR_SPACE_EMPTY
				: lsNewPlt);

		// defect 7965
		// Per VTR, disable print sticker for all RSPS 
		// Diskette processing. 
		if (!UtilityMethods.isEmpty(caCurrDlrTtlData.getRSPSId()))
		{
			getchkPrint().setEnabled(false);
		}
		else
		{
			getchkPrint().setSelected(
				caCurrDlrTtlData.isKeyBoardEntry()
					|| (liNewRegMo > 0 && liNewRegYr > 0));

		}
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		if (aaFE.getSource() == gettxtFeePd())
		{
			gettxtFeePd().moveCaretPosition(0);
			gettxtFeePd().select(0, 0);
		}
		// defect 7898
		// added for a temp fix to the problem when canceling back
		// from a no vin no plate search
		else if (aaFE.getSource() == this)
		{
			getbtnECH().getBtnEnter().requestFocus();
		} // end defect 7898
	}
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		// No Action 	
	}

	/**
	 * Get First DlrTtlData
	 * 
	 * @return DealerTitleData
	 */
	private DealerTitleData getFirstDlrTtlData()
	{
		return (DealerTitleData) Transaction
			.getDTADlrTtlData()
			.elementAt(
			0);
	}

	/**
	 * Return the ivjbtnECH property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getbtnECH()
	{
		if (ivjbtnECH == null)
		{
			try
			{
				ivjbtnECH = new ButtonPanel();
				ivjbtnECH.setName("ivjbtnECH");
				ivjbtnECH.setLayout(new java.awt.FlowLayout());
				ivjbtnECH.setBounds(160, 358, 273, 43);
				// user code begin {1}
				ivjbtnECH.addActionListener(this);
				ivjbtnECH.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnECH;
	}

	/**
	 * Return the ivjchkPrint property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPrint()
	{
		if (ivjchkPrint == null)
		{
			try
			{
				ivjchkPrint = new JCheckBox();
				ivjchkPrint.setSize(118, 24);
				ivjchkPrint.setName("ivjchkPrint");
				ivjchkPrint.setSelected(false);
				ivjchkPrint.setMnemonic(KeyEvent.VK_P);
				ivjchkPrint.setText(PRNT_STKR);
				// user code begin {1}
				ivjchkPrint.setLocation(129, 90);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkPrint;
	}

	/**
	 * Return the chkRejectThisTrans property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkRejectThisTrans()
	{
		if (ivjchkRejectThisTrans == null)
		{
			try
			{
				ivjchkRejectThisTrans = new JCheckBox();
				ivjchkRejectThisTrans.setName("ivjchkRejectThisTrans");
				ivjchkRejectThisTrans.setMnemonic(KeyEvent.VK_T);
				ivjchkRejectThisTrans.setText(REJECT_TRANS);
				ivjchkRejectThisTrans.setBounds(162, 293, 265, 22);
				ivjchkRejectThisTrans.setRequestFocusEnabled(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkRejectThisTrans;
	}

	/**
	 * Return the ivjchkRejEntBatch property value.
	 * 
	 * @return  JCheckBox
	 */
	private JCheckBox getchkRejEntBatch()
	{
		if (ivjchkRejEntBatch == null)
		{
			try
			{
				ivjchkRejEntBatch = new JCheckBox();
				ivjchkRejEntBatch.setName("ivjchkRejEntBatch");
				ivjchkRejEntBatch.setMnemonic(KeyEvent.VK_B);
				ivjchkRejEntBatch.setText(REJECT_ENTIRE_BATCH);
				ivjchkRejEntBatch.setBounds(162, 321, 265, 22);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkRejEntBatch;
	}

	/**
	 * Return the ivjJPanel1 property value.
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
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						INVENTORY));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.add(getJPanel(), null);
				ivjJPanel1.add(getJPanel12(), null);
				ivjJPanel1.add(getchkPrint(), null);
				ivjJPanel1.add(getchkCustSupplied(), null);
				ivjJPanel1.add(getstcLblPlateAge(), null);
				ivjJPanel1.add(gettxtPlateAge(), null);
				ivjJPanel1.setBounds(17, 135, 551, 151);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the ivjlblDealerName1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDealerName1()
	{
		if (ivjlblDealerName1 == null)
		{
			try
			{
				ivjlblDealerName1 = new JLabel();
				ivjlblDealerName1.setSize(350, 20);
				ivjlblDealerName1.setName("ivjlblDealerName1");
				ivjlblDealerName1.setText("");
				ivjlblDealerName1.setHorizontalTextPosition(
					SwingConstants.LEFT);
				// user code begin {1}
				ivjlblDealerName1.setLocation(149, 37);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblDealerName1;
	}

	/**
	 * This method initializes ivjlblDealerName2
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDealerName2()
	{
		if (ivjlblDealerName2 == null)
		{
			ivjlblDealerName2 = new JLabel();
			ivjlblDealerName2.setName("ivjlblDealerName2");
			ivjlblDealerName2.setSize(350, 20);
			ivjlblDealerName2.setText("");
			ivjlblDealerName2.setLocation(149, 59);
		}
		return ivjlblDealerName2;
	}

	/**
	 * Return the ivjlbDealerId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDealerId()
	{
		if (ivjlbDealerId == null)
		{
			try
			{
				ivjlbDealerId = new JLabel();
				ivjlbDealerId.setName("ivjlbDealerId");
				ivjlbDealerId.setBounds(149, 14, 115, 20);
				ivjlbDealerId.setHorizontalTextPosition(
					SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlbDealerId;
	}

	/**
	 * Return the ivjlblTransactionNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTransactionNo()
	{
		if (ivjlblTransactionNo == null)
		{
			try
			{
				ivjlblTransactionNo = new JLabel();
				ivjlblTransactionNo.setName("ivjlblTransactionNo");
				ivjlblTransactionNo.setBounds(455, 14, 23, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblTransactionNo;
	}

	//	/**
	//	 * Return boolean to determine if more Transaction
	//	 * 
	//	 * @return boolean 
	//	 */
	//	private boolean getNextTrans()
	//	{
	//		ciTransCount++;
	//
	//		// TODO Transaction  
	//		caCurrDlrTtlData =
	//			(DealerTitleData) Transaction.getDTAInfo().get(
	//				ciIterator++);
	//
	//		// How can record already be rejected? 
	//		while (caCurrDlrTtlData.isRecordRejected()
	//			&& ciIterator < Transaction.getDTAInfo().size())
	//		{
	//			ciTransCount++;
	//			caCurrDlrTtlData =
	//				(DealerTitleData) Transaction.getDTAInfo().get(
	//					ciIterator++);
	//		}
	//		return ciIterator <= Transaction.getDTAInfo().size();
	//	}

	/**
	 * Return the ivjRTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"ivjRTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				getRTSDialogBoxContentPane().add(
					gettxtTransactionDate(),
					gettxtTransactionDate().getName());
				getRTSDialogBoxContentPane().add(
					gettxtFeePd(),
					gettxtFeePd().getName());
				getRTSDialogBoxContentPane().add(
					getJPanel1(),
					getJPanel1().getName());
				getRTSDialogBoxContentPane().add(
					getchkRejectThisTrans(),
					getchkRejectThisTrans().getName());
				getRTSDialogBoxContentPane().add(
					getchkRejEntBatch(),
					getchkRejEntBatch().getName());
				getRTSDialogBoxContentPane().add(
					getbtnECH(),
					getbtnECH().getName());
				getRTSDialogBoxContentPane().add(
					getstcLblDealerId(),
					getstcLblDealerId().getName());
				getRTSDialogBoxContentPane().add(
					getstcLblDealerName(),
					getstcLblDealerName().getName());
				getRTSDialogBoxContentPane().add(
					getstcLblTrnsDt(),
					getstcLblTrnsDt().getName());
				getRTSDialogBoxContentPane().add(
					getstcLblFeePd(),
					getstcLblFeePd().getName());
				getRTSDialogBoxContentPane().add(
					getlblDealerId(),
					getlblDealerId().getName());
				getRTSDialogBoxContentPane().add(
					getlblDealerName1(),
					getlblDealerName1().getName());
				getRTSDialogBoxContentPane().add(
					getstcLblTrans(),
					getstcLblTrans().getName());
				getRTSDialogBoxContentPane().add(
					getlblTransactionNo(),
					getlblTransactionNo().getName());
				// user code begin {1}
				ivjRTSDialogBoxContentPane.add(
					getlblDealerName2(),
					null);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the ivjstcLblDealerId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDealerId()
	{
		if (ivjstcLblDealerId == null)
		{
			try
			{
				ivjstcLblDealerId = new JLabel();
				ivjstcLblDealerId.setSize(110, 20);
				ivjstcLblDealerId.setName("ivjstcLblDealerId");
				ivjstcLblDealerId.setText(DEALER_ID);
				ivjstcLblDealerId.setHorizontalTextPosition(
					SwingConstants.RIGHT);
				ivjstcLblDealerId.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				ivjstcLblDealerId.setLocation(30, 14);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDealerId;
	}

	/**
	 * Return the stcLblDlrName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDealerName()
	{
		if (ivjstcLblDealerName == null)
		{
			try
			{
				ivjstcLblDealerName = new JLabel();
				ivjstcLblDealerName.setName("ivjstcLblDealerName");
				ivjstcLblDealerName.setText(DEALER_NAME);
				ivjstcLblDealerName.setBounds(30, 37, 110, 20);
				ivjstcLblDealerName.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblDealerName;
	}

	/**
	 * Return the stcLblFeePd property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblFeePd()
	{
		if (ivjstcLblFeePd == null)
		{
			try
			{
				ivjstcLblFeePd = new JLabel();
				ivjstcLblFeePd.setName("ivjstcLblFeePd");
				ivjstcLblFeePd.setText(FEES_PAID);
				ivjstcLblFeePd.setBounds(30, 109, 110, 20);
				ivjstcLblFeePd.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblFeePd;
	}

	/**
	 * Return the ivjstcLblForm31 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblForm31()
	{
		if (ivjstcLblForm31 == null)
		{
			try
			{
				ivjstcLblForm31 = new JLabel();
				ivjstcLblForm31.setBounds(20, 13, 67, 16);
				ivjstcLblForm31.setName("ivjstcLblForm31");
				ivjstcLblForm31.setText(FORM31NO);
				ivjstcLblForm31.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblForm31;
	}

	/**
	 * Return the ivjstcLblNewRegMo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblNewRegMo()
	{
		if (ivjstcLblNewRegMo == null)
		{
			try
			{
				ivjstcLblNewRegMo = new JLabel();
				ivjstcLblNewRegMo.setBounds(22, 14, 97, 16);
				ivjstcLblNewRegMo.setName("ivjstcLblNewRegMo");
				ivjstcLblNewRegMo.setText(NEW_REG_EXP_MO);
				ivjstcLblNewRegMo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblNewRegMo;
	}

	/**
	 * Return the ivjstcLblPlateNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateNo()
	{
		if (ivjstcLblPlateNo == null)
		{
			try
			{
				ivjstcLblPlateNo = new JLabel();
				ivjstcLblPlateNo.setBounds(9, 40, 78, 16);
				ivjstcLblPlateNo.setName("ivjstcLblPlateNo");
				ivjstcLblPlateNo.setText(NEW_PLATE_NO);
				ivjstcLblPlateNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblPlateNo;
	}

	/**
	 * Return the ivjstcLblRegYr property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegYr()
	{
		if (ivjstcLblRegYr == null)
		{
			try
			{
				ivjstcLblRegYr = new JLabel();
				ivjstcLblRegYr.setBounds(27, 41, 92, 16);
				ivjstcLblRegYr.setName("ivjstcLblRegYr");
				ivjstcLblRegYr.setText(NEW_REG_EXP_YR);
				ivjstcLblRegYr.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblRegYr;
	}

	/**
	 * Return the ivjstcLblTrans property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTrans()
	{
		if (ivjstcLblTrans == null)
		{
			try
			{
				ivjstcLblTrans = new JLabel();
				ivjstcLblTrans.setName("ivjstcLblTrans");
				ivjstcLblTrans.setText(TRANSACTION);
				ivjstcLblTrans.setBounds(368, 14, 80, 20);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTrans;
	}

	/**
	 * Return the ivjstcLblTrnsDt property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTrnsDt()
	{
		if (ivjstcLblTrnsDt == null)
		{
			try
			{
				ivjstcLblTrnsDt = new JLabel();
				ivjstcLblTrnsDt.setName("ivjstcLblTrnsDt");
				ivjstcLblTrnsDt.setText(TRANSACTION_DATE);
				ivjstcLblTrnsDt.setBounds(30, 86, 110, 20);
				ivjstcLblTrnsDt.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTrnsDt;
	}

	/**
	 * Return the ivjtxtTransactionDate property value.
	 * 
	 * @return RTSDateField
	 * */
	private RTSDateField gettxtTransactionDate()
	{
		if (ivjtxtTransactionDate == null)
		{
			try
			{
				ivjtxtTransactionDate = new RTSDateField();
				ivjtxtTransactionDate.setName("ivjtxtTransactionDate");
				ivjtxtTransactionDate.setBounds(149, 86, 94, 20);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtTransactionDate;
	}

	/**
	 * Return the ivjtxtExpMo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtExpMo()
	{
		if (ivjtxtExpMo == null)
		{
			try
			{
				ivjtxtExpMo = new RTSInputField();
				ivjtxtExpMo.setBounds(128, 15, 37, 20);
				ivjtxtExpMo.setName("ivjtxtExpMo");
				ivjtxtExpMo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtExpMo.setMaxLength(MONTH_MAX_LEN);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtExpMo;
	}

	/**
	 * Return the ivjtxtExpYr property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtExpYr()
	{
		if (ivjtxtExpYr == null)
		{
			try
			{
				ivjtxtExpYr = new RTSInputField();
				ivjtxtExpYr.setBounds(128, 39, 54, 20);
				ivjtxtExpYr.setName("ivjtxtExpYr");
				ivjtxtExpYr.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtExpYr.setMaxLength(YEAR_MAX_LEN);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtExpYr;
	}
	/**
	 * Return the txtFeePd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtFeePd()
	{
		if (ivjtxtFeePd == null)
		{
			try
			{
				ivjtxtFeePd = new RTSInputField();
				ivjtxtFeePd.setName("txtFeePd");
				ivjtxtFeePd.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjtxtFeePd.setBounds(149, 109, 94, 20);
				ivjtxtFeePd.setMaxLength(FEES_PAID_MAX_LEN);
				gettxtFeePd().setInput(RTSInputField.DOLLAR_ONLY);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtFeePd;
	}

	/**
	 * Return the ivjtxtForm31 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtForm31()
	{
		if (ivjtxtForm31 == null)
		{
			try
			{
				ivjtxtForm31 = new RTSInputField();
				ivjtxtForm31.setBounds(100, 12, 118, 20);
				ivjtxtForm31.setName("ivjtxtForm31");
				ivjtxtForm31.setMaxLength(FORM31NO_MAX_LEN);
				ivjtxtForm31.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtForm31;
	}

	/**
	 * Return the ivjtxtPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPlateNo()
	{
		if (ivjtxtPlateNo == null)
		{
			try
			{
				ivjtxtPlateNo = new RTSInputField();
				ivjtxtPlateNo.setBounds(100, 36, 118, 20);
				ivjtxtPlateNo.setName("ivjtxtPlateNo");
				ivjtxtPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPlateNo.setMaxLength(PLT_NO_MAX_LEN);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPlateNo;
	}

	/**
	 * Go to Pending Transaction Screen
	 * 
	 * @param aeException
	 */
	private void goToPendingTrans()
	{
		getController().processData(
			VCDealerTitleTransactionDTA008.DO_PAYMENT,
			ciIterator + 1 > ciNoRejectedTrans
				? Transaction.getDTADlrTtlData()
				: null);
		cbShow = false;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException java.lang.Throwable
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
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(ScreenConstant.DTA008_FRAME_NAME);
			setRequestFocus(false);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(611, 438);
			setTitle(ScreenConstant.DTA008_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		} // user code begin {2}
		addWindowListener(this);
		// user code end
	}

	/**
	 * Initialize screen values for DTA008
	 *
	 * @throws RTSException 
	 */
	private void initializeForAddlManualTrans() throws RTSException
	{
		getchkPrint().setSelected(true);
		getchkPrint().setEnabled(true);
		caCurrDlrTtlData = new DealerTitleData();
		// Defaults to True 
		caCurrDlrTtlData.setKeyBoardEntry(true);
		caCurrDlrTtlData.setDealerSeqNo(csDealerSeqNo);
		caCurrDlrTtlData.setDealerId(csDealerId);
		ciIterator++;
		ciTransCount++;
		Transaction.getDTADlrTtlData().addElement(caCurrDlrTtlData);
		Transaction.setDTADlrTtlDataIndex(ciIterator);
		getlblTransactionNo().setText("" + ciTransCount);
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmDealerTitleTransactionDTA008 laFrmDealerTitleTransactionDTA008;
			laFrmDealerTitleTransactionDTA008 =
				new FrmDealerTitleTransactionDTA008();
			laFrmDealerTitleTransactionDTA008.setModal(true);
			laFrmDealerTitleTransactionDTA008
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmDealerTitleTransactionDTA008.show();
			java.awt.Insets insets =
				laFrmDealerTitleTransactionDTA008.getInsets();
			laFrmDealerTitleTransactionDTA008.setSize(
				laFrmDealerTitleTransactionDTA008.getWidth()
					+ insets.left
					+ insets.right,
				laFrmDealerTitleTransactionDTA008.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmDealerTitleTransactionDTA008.setVisibleRTS(true);
		}
		catch (Throwable leTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_EXCEPT_IN_MAIN);
			leTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * 
	 * Initialize Dealer Info
	 *
	 */
	private void initDealerInfo()
	{
		caCurrDlrTtlData =
			(DealerTitleData) UtilityMethods.copy(getFirstDlrTtlData());

		csDealerSeqNo = caCurrDlrTtlData.getDealerSeqNo();
		csDealerId = caCurrDlrTtlData.getStrDealerId();

		getlblDealerId().setText(csDealerId);

		DealerData laDealerData = Transaction.getDTADealerData();

		if (laDealerData != null)
		{
			getlblDealerName1().setText(laDealerData.getName1());

			if (!UtilityMethods.isEmpty(laDealerData.getName2()))
			{
				getlblDealerName2().setText(laDealerData.getName2());
			}
		}
		else if (caCurrDlrTtlData.isKeyBoardEntry())
		{
			// 508
			new RTSException(
				ErrorsConstant
					.ERR_MSG_DTA_KEYBOARD_INVALID_DEALERID)
					.displayError(
				this);
		}
	}

	/**
	 * Initialize for New Transaction 
	 * 
	 */
	private void initForNewTrans()
	{
		getlblTransactionNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtExpMo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtExpYr().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtFeePd().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtForm31().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtPlateNo().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtTransactionDate().setText(CommonConstant.STR_SPACE_EMPTY);
		getchkRejectThisTrans().setSelected(false);
		getchkPrint().setEnabled(true);

		// Clear Owner Info  
		getController().getMediator().closeVault(
			ScreenConstant.TTL007,
			null);
		
		// defect 10592 
		getController().getMediator().closeVault(
			ScreenConstant.TTL035,
			null);
		// end defect 10592 

		// Enable CustSupplied Checkbox if >= PTO Start Date  
		getchkCustSupplied().setEnabled(
			RTSDate.getCurrentDate().compareTo(
				SystemProperty.getPTOStartDate())
				>= 0);
	}

	/**
	 * Substract 1 from record count, Rejected Trans
	 */
	private void rejectTrans()
	{
		gettxtTransactionDate().setDate(null);
		gettxtTransactionDate().requestFocus();
		caCurrDlrTtlData.setRecordRejected(true);
		String lsForm31 = gettxtForm31().getText().trim();
		ProcessInventoryData laProcessInvData =
			new ProcessInventoryData();
		laProcessInvData.setInvItmNo(lsForm31);
		laProcessInvData.setItmCd(INV_FORM31NO);
		Vector lvInv = new Vector();
		lvInv.add(laProcessInvData);
		caCurrDlrTtlData.setInventoryData(lvInv);
		// TODO Transaction 
		if (caCurrDlrTtlData.isKeyBoardEntry())
		{
			ciTransCount--;
			ciIterator--;
			Transaction.getDTADlrTtlData().removeElementAt(
				ciTransCount);
			Transaction.setDTADlrTtlDataIndex(ciTransCount - 1);
		}
		else
		{
			ciNoRejectedTrans++;

			Transaction.getDTADlrTtlData().set(
				Transaction.getDTADlrTtlDataIndex(),
				UtilityMethods.copy(caCurrDlrTtlData));
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			if (aaDataObject != null
				&& !(aaDataObject instanceof RTSException))
			{
				// Initialize Screen for New DTA Transaction 
				initForNewTrans();

				// Vector of DealerTitle Data from DTA007, DTA009 
				if (aaDataObject instanceof Vector)
				{
					// Save unmodified data for future re-write to media 
					Transaction.setDTAOrigDlrTtlData(
						(Vector) UtilityMethods.copy(aaDataObject));

					// Vector of data to be modified for transactions 
					Transaction.setDTADlrTtlData(
						(Vector) UtilityMethods.copy(aaDataObject));

					Transaction.setDTADlrTtlDataIndex(0);

					initDealerInfo();

					aaDataObject = null;
				}
				else if (
					aaDataObject instanceof CompleteTransactionData)
				{
					aaDataObject = null;
				}

				if (ciIterator + 1 == MAX_BATCH_NO)
				{
					goToPendingTrans();
				}
				else
				{
					// If more DlrTtlData objects in vector 
					if (ciIterator
						< Transaction.getDTADlrTtlData().size() - 1)
					{
						setupForNextTrans();
					}
					else
					{
						RTSException leRTSEx =
							new RTSException(
								RTSException.CTL001,
								MORETRANSPRCS,
								ScreenConstant.CTL001_FRM_TITLE);

						if (leRTSEx.displayError(this)
							== RTSException.NO)
						{
							goToPendingTrans();
							return;
						}
						else
						{
							initializeForAddlManualTrans();
							// Not hitting finally because of stack call
							// setting it manually
							// KPH What is this about? 
							//setWorking(false);
							gettxtTransactionDate().requestFocus();
						}
					}
				}
			}
		}
		catch (Exception aeEx)
		{
			RTSException leRTSx =
				new RTSException(RTSException.JAVA_ERROR, aeEx);
			leRTSx.displayError(this);
		}
	}

	/**
	 * Save data in DealerTitleData object
	 */
	private void setDataToDataObject()
	{
		// TODO This shouldn't happen
		if (caCurrDlrTtlData == null)
		{
			caCurrDlrTtlData = new DealerTitleData();
		}

		// defect 10290 
		caCurrDlrTtlData.setTransDt(
			CommonConstant.STR_SPACE_EMPTY
				+ gettxtTransactionDate().getDate().getYYYYMMDDDate());

		caCurrDlrTtlData.setFee(new Dollar(gettxtFeePd().getText()));

		caCurrDlrTtlData.setForm31No(gettxtForm31().getText());

		String lsNewRegMo = ZERO;
		String lsNewRegYr = ZERO;

		if (!gettxtExpMo().isEmpty())
		{
			lsNewRegMo = gettxtExpMo().getText();
		}
		if (!gettxtExpYr().isEmpty())
		{
			lsNewRegYr = gettxtExpYr().getText();
		}

		caCurrDlrTtlData.setNewRegExpMo(Integer.parseInt(lsNewRegMo));

		caCurrDlrTtlData.setNewRegExpYr(Integer.parseInt(lsNewRegYr));

		caCurrDlrTtlData.setNewPltNo(gettxtPlateNo().getText());
		caCurrDlrTtlData.setToBePrinted(getchkPrint().isSelected());
		// end defect 10290 

		caCurrDlrTtlData.setCustSuppliedPltIndi(
			getchkCustSupplied().isSelected() ? 1 : 0);

		if (caCurrDlrTtlData.isCustSuppliedPlt())
		{
			caCurrDlrTtlData.setCustSuppliedPltAge(
				Integer.parseInt(gettxtPlateAge().getText()));
		}
	}

	/**
	 * 
	 * Set Up For Next Transaction  
	 *
	 */
	private void setupForNextTrans()
	{
		ciTransCount++;
		ciIterator++;

		caCurrDlrTtlData =
			(DealerTitleData) Transaction.getDTADlrTtlData().get(
				ciIterator);

		Transaction.setDTADlrTtlDataIndex(ciIterator);
		getlblTransactionNo().setText("" + ciTransCount);
		
		// defect 10827 
		TitleData laTitleData =
			caCurrDlrTtlData.getMFVehicleData().getTitleData();
		
		if (laTitleData.getSurvshpRightsIndi() == 1 && 
				Integer.parseInt(caCurrDlrTtlData.getTransDate()) >= SystemProperty.getDTARejectSurvivorDate()) 
		{
			getchkRejectThisTrans().setSelected(true); 
			getchkRejectThisTrans().setEnabled(false); 
			new RTSException(ErrorsConstant.ERR_NUM_MUST_REJECT_DTA_TRANS_WITH_SURVIVOR).displayError(this); 
		}
		else
		{
			getchkRejectThisTrans().setEnabled(true); 
			// end defect 10827 

			if (SystemProperty.getPrintImmediateIndi() != 0
					&& ciIterator != 0)
			{
				getbtnECH().getBtnCancel().setEnabled(false);
				getchkRejEntBatch().setEnabled(false);
			}

			if (caCurrDlrTtlData.isCustSuppliedPlt())
			{
				getchkCustSupplied().setSelected(true);
				gettxtPlateAge().setEnabled(true);
				getstcLblPlateAge().setEnabled(true);
				gettxtPlateAge().setText(
						Integer.toString(
								caCurrDlrTtlData.getCustSuppliedPltAge()));
			}
			else
			{
				getchkCustSupplied().setSelected(false);
				getstcLblPlateAge().setEnabled(false);
				gettxtPlateAge().setEnabled(false);
				gettxtPlateAge().setText(CommonConstant.STR_SPACE_EMPTY);
			}
		// defect 10827 
		}
		// end defect 10827
		setDlrTtlTransInfoToDisplay();
		verifyLien();
	}

	/**
	 * Verify Lien Information on Diskette wrt Certified/ETitle Requests 
	 */
	private void verifyLien()
	{
		// Verify PermLienholderIds & Same Address
		boolean lbETtlIndi = caCurrDlrTtlData.isETtlRqst();
		String lsErrMsg = new String();
		int liNumPermLienHldr = 0;
		Vector lvErrMsg = new Vector();
		TitleData laTitleData =
			caCurrDlrTtlData.getMFVehicleData().getTitleData();

		// PermLienhldrId 
		Vector lvPermLienhldr = new Vector(3);
		lvPermLienhldr.add(laTitleData.getPermLienHldrId1());
		lvPermLienhldr.add(laTitleData.getPermLienHldrId2());
		lvPermLienhldr.add(laTitleData.getPermLienHldrId3());
		Vector lvInvalidPermLienhldrId = new Vector();

		// Info for TTL035 
		Vector lvLienError = new Vector();
		for (int i = 0; i < lvPermLienhldr.size(); i++)
		{
			lsErrMsg = "";
			String lsPermLienhldrid =
				(String) lvPermLienhldr.elementAt(i);
			if (TitleClientUtilityMethods
				.isValidPermLienhldrId(lsPermLienhldrid))
			{
				++liNumPermLienHldr;
				CertifiedLienholderData laCertLien =
					CertifiedLienholderCache.getCurrentCertfdLienhldr(
						lsPermLienhldrid);
				if (laCertLien == null)
				{
					lsErrMsg =
						"An invalid Lienholder Id,"
							+ " "
							+ lsPermLienhldrid
							+ ", was specified. The Id will be ignored.";
					laTitleData.setPermLienHldrId(
						i + 1,
						CommonConstant.STR_SPACE_EMPTY);
					lvPermLienhldr.setElementAt(new String(), i);
					lvInvalidPermLienhldrId.add(lsPermLienhldrid);
				}
				else
				{
					// defect 10112 
					//	LienholderData laLien =
					//		(LienholderData) lvLien.elementAt(i);
					LienholderData laLien =
						laTitleData.getLienholderData(
							new Integer(i + 1));
					// end defect 10112 
					if (laLien == null
						|| laCertLien.getLienholderData().compareTo(
							laLien)
							!= 0)
					{
						lsErrMsg =
							"A valid Lienholder Id,"
								+ " "
								+ lsPermLienhldrid
								+ ", was specified, but with invalid"
								+ " name or address. The Id will be ignored.";
						laTitleData.setPermLienHldrId(
							i + 1,
							CommonConstant.STR_SPACE_EMPTY);
						lvPermLienhldr.setElementAt(new String(), i);
						lvInvalidPermLienhldrId.add(lsPermLienhldrid);
					}
					else
					{
						if (lbETtlIndi
							&& i == 0
							&& !laCertLien.isElienRdy())
						{
							lsErrMsg =
								"A valid Id,"
									+ " "
									+ lsPermLienhldrid
									+ ", was specified which does not"
									+ "  support Electronic Titles. "
									+ " The ETitle request will be ignored.";
							lbETtlIndi = false;
							lvLienError.add(ERR_ETITLE_RQST_REMOVED);
						} // defect 10124 
						else if (
							!lbETtlIndi && laCertLien.isElienRdy())
						{
							lsErrMsg =
								"A valid Id,"
									+ " "
									+ lsPermLienhldrid
									+ ", was specified which is not available "
									+ "  for a non-ETitle request."
									+ " The Id will be ignored.";
							laTitleData.setPermLienHldrId(
								i + 1,
								CommonConstant.STR_SPACE_EMPTY);
							lvPermLienhldr.setElementAt(
								new String(),
								i);
							lvInvalidPermLienhldrId.add(
								lsPermLienhldrid);
						} //	 end defect 10124 
					}
				}
			}
			if (lsErrMsg.length() > 0)
			{
				lvErrMsg.add(lsErrMsg);
			}
		}
		if (lvInvalidPermLienhldrId.size() > 0)
		{
			int liRows = lvInvalidPermLienhldrId.size();
			String lsMsg = "Lienholder Id ";
			for (int i = 0; i < liRows; i++)
			{
				lsMsg = lsMsg + lvInvalidPermLienhldrId.elementAt(i);
				if (i != liRows - 1)
				{
					lsMsg = lsMsg + ", ";
				}
			}
			lvLienError.add(lsMsg + " removed.");
		}

		if (lbETtlIndi)
		{
			// defect 10112 
			// boolean lbLien2 =
			//laTitleData.getLienHolder2().isLienPopulated();
			boolean lbLien2 =
				laTitleData
					.getLienholderData(TitleConstant.LIENHLDR2)
					.isPopulated();
			//boolean lbLien3 =
			//laTitleData.getLienHolder3().isLienPopulated();
			boolean lbLien3 =
				laTitleData
					.getLienholderData(TitleConstant.LIENHLDR3)
					.isPopulated();
			// end defect 10112 
			if (lbLien2 || lbLien3)
			{
				lvErrMsg.add(MULT_LIEN_FOR_ETTL_MSG);
				lbETtlIndi = false;
				lvLienError.add(ERR_ETITLE_RQST_REMOVED);
			}
			else if (
				((String) lvPermLienhldr.elementAt(0)).equals(
					new String()))
			{
				lvErrMsg.add(VALID_LIEN_RQD_MSG);
				lbETtlIndi = false;
				lvLienError.add(ERR_ETITLE_RQST_REMOVED);
			}
			else if (laTitleData.getAddlLienRecrdIndi() == 1)
			{
				lvErrMsg.add(NO_ADDL_LIEN_FOR_ETTL_MSG);
				laTitleData.setAddlLienRecrdIndi(0);
				lvLienError.add(ERR_ADDL_LIEN_REMOVED);
			}

		}
		if (!lbETtlIndi)
		{
			caCurrDlrTtlData.setETtlRqst(0);
			laTitleData.setETtlCd(0);
		}

		if (lvErrMsg.size() > 0)
		{
			for (int i = 0; i < lvErrMsg.size(); i++)
			{
				lsErrMsg =
					(String) lvErrMsg.elementAt(i)
						+ " (Transaction #"
						+ (ciIterator + 1)
						+ ")";
				if (i == lvErrMsg.size() - 1)
				{
					lsErrMsg = lsErrMsg + "<br><br>" + LIEN_MSG_SUFFIX;
				}
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						lsErrMsg,
						"ERROR",
						true);
				leRTSEx.setBeep(RTSException.BEEP);
				leRTSEx.displayError(this);
			}
		}
		caCurrDlrTtlData.setLienError(lvLienError);
	}

	/**
	 * Validate Data 
	 */
	private boolean validateData()
	{
		boolean lbValid = true;
		RTSException leRTSEx = new RTSException();
		RTSDate laCurrDt = RTSDate.getCurrentDate();
		RTSDate laCurrDtLess60 = laCurrDt.add(RTSDate.DATE, -61);

		// Transaction Date
		RTSDate laTransDt = gettxtTransactionDate().getDate();
		if (laTransDt == null)
		{
			// 150 
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtTransactionDate());
		}
		else if (laTransDt.compareTo(laCurrDt) > 0)
		{
			// 427 THE DATE MAY NOT BE IN THE FUTURE.
			// defect 10355 
			leRTSEx.addException(new RTSException(
			//ErrorsConstant.ERR_MSG_DTA_TRANS_DATE_CANNOT_BE_IN_FUTURE),
			ErrorsConstant.ERR_MSG_DATE_CANNOT_BE_IN_FUTURE),
				gettxtTransactionDate());
			// end defect 10355 
		}

		// Fees
		Dollar laDlrFee = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
		if (!gettxtFeePd().isEmpty())
		{
			laDlrFee = new Dollar(gettxtFeePd().getText());
		}
		if (laDlrFee
			.compareTo(new Dollar(CommonConstant.STR_ZERO_DOLLAR))
			<= 0
			|| laDlrFee.compareTo(new Dollar(1000000000)) >= 0)
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtFeePd());
		}

		// Form31      
		if (gettxtForm31().isEmpty())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtForm31());
		}
		else
		{
			validateForm31(leRTSEx);
		}

		// New Reg Exp Mo Yr 
		int liMo = 0;
		int liYr = 0;

		if (!gettxtExpMo().isEmpty())
		{
			liMo = Integer.parseInt(gettxtExpMo().getText());

			if (!(liMo == 0 || gettxtExpMo().isValidMonth()))
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtExpMo());
			}
		}

		if (!gettxtExpYr().isEmpty())
		{
			liYr = Integer.parseInt(gettxtExpYr().getText());

			if (!(liYr == 0 || (liYr > 1993 && liYr < 2050)))
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtExpYr());
			}
		}

		// If New Plate No, ExpMo/Yr must be populated  
		if (!gettxtPlateNo().isEmpty())
		{
			// 538 THE NEW REG EXPIRATION MONTH AND YEAR ARE REQUIRED 
			//      WHEN ISSUING A NEW PLATE.
			if (liMo == 0)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_MSG_DTA_NEW_EXP_MO_YR_REQUIRED_FOR_NEW_PLT),
					gettxtExpMo());
			}
			if (liYr == 0)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_MSG_DTA_NEW_EXP_MO_YR_REQUIRED_FOR_NEW_PLT),
					gettxtExpYr());
			}
			if (getchkCustSupplied().isSelected())
			{
				if (gettxtPlateAge().isEmpty())
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtPlateAge());
				}
				else if (
					(Integer.parseInt(gettxtPlateAge().getText()))
						> MAX_PLT_AGE)
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtPlateAge());
				}
			}
		}
		else if (getchkCustSupplied().isSelected())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getchkCustSupplied());

			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPlateAge());

			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPlateNo());
		}

		if ((liMo != 0 || liYr != 0) && ((liMo * liYr) == 0))
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				liMo == 0 ? gettxtExpMo() : gettxtExpYr());
		}

		//  If Print Sticker, NewRegExpMo/Yr must be valid  
		if (getchkPrint().isSelected())
		{
			//  743  NEW EXPIRATION MONTH AND YEAR MUST BE ENTERED TO 
			//       PRINT A REGISTRATION STICKER.
			if (liMo < 1 || liMo > 12)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_MSG_DTA_NEW_EXP_MO_YR_REQUIRED_FOR_NEW_STKR),
					gettxtExpMo());
			}
			if (liYr == 0)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_MSG_DTA_NEW_EXP_MO_YR_REQUIRED_FOR_NEW_STKR),
					gettxtExpYr());
			}
		}

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		else if (laTransDt.compareTo(laCurrDtLess60) < 0)
		{
			// 268 DEALER TRANSACTION IS MORE THAN 60 DAYS OLD.
			RTSException leRTSExc = new RTSException(268);
			leRTSExc.displayError(this);
		}
		return lbValid;
	}

	/**
	 * Validate Form31 
	 * 
	 * @param aeRTSEx
	 */
	private void validateForm31(RTSException aeRTSEx)
	{
		ValidateInventoryPattern laValidateInventoryPattern =
			new ValidateInventoryPattern();

		ProcessInventoryData laProcessInventoryData =
			new ProcessInventoryData();
		laProcessInventoryData.setItmCd("FORM31");
		laProcessInventoryData.setInvQty(1);
		laProcessInventoryData.setInvItmNo(gettxtForm31().getText());
		laProcessInventoryData.setInvItmYr(0);

		try
		{
			laValidateInventoryPattern.validateItmNoInput(
				laProcessInventoryData.convertToInvAlloctnUIData(
					laProcessInventoryData));
		}
		catch (RTSException aeRTSEx1)
		{
			aeRTSEx.addException(aeRTSEx1, gettxtForm31());

		}
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowActivated(java.awt.event.WindowEvent aaWE)
	{
		if (cbShow == false)
		{
			setVisible(false);
		}
	}

	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosed(java.awt.event.WindowEvent aaWE)
	{
	}

	/**
	 *  Invoked when the user attempts to close the window
	 *  from the window's system menu.  If the program does not 
	 *  explicitly hide or dispose the window while processing 
	 *  this event, the window close operation will be cancelled.
	 *  
	 *  @param aaWE WindowEvent
	 */
	public void windowClosing(java.awt.event.WindowEvent aaWE)
	{
	}

	/**
	 *  Invoked when a window is no longer the user's active
	 *  window, which means that keyboard events will no longer
	 *  be delivered to the window or its subcomponents.
	 *  
	 *  @param aaWE WindowEvent
	 *  
	 */
	public void windowDeactivated(java.awt.event.WindowEvent aaWe)
	{
	}

	/**
	 *  Invoked when a window is changed from a minimized
	 *  to a normal state.
	 *  
	 *  @param aaWE WindowEvent
	 */
	public void windowDeiconified(java.awt.event.WindowEvent aaWe)
	{
	}

	/**
	 *  Invoked when a window is changed from a normal to a
	 *  minimized state. For many platforms, a minimized window 
	 *  is displayed as the icon specified in the window's 
	 *  iconImage property.
	 *  @see Frame#setIconImage
	 *  
	 *  @param aaWE WindowEvent
	 */
	public void windowIconified(java.awt.event.WindowEvent aaWE)
	{
	}

	/**
	 *  Invoked the first time a window is made visible.
	 *  
	 *  @param aaWE WindowEvent
	 */
	public void windowOpened(java.awt.event.WindowEvent aaWe)
	{
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getstcLblPlateNo(), null);
			jPanel.add(getstcLblForm31(), null);
			jPanel.add(gettxtPlateNo(), null);
			jPanel.add(gettxtForm31(), null);
			jPanel.setBounds(29, 20, 235, 66);
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel12()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.add(getstcLblRegYr(), null);
			jPanel1.add(gettxtExpMo(), null);
			jPanel1.add(gettxtExpYr(), null);
			jPanel1.add(getstcLblNewRegMo(), null);
			jPanel1.setBounds(329, 19, 197, 67);
		}
		return jPanel1;
	}

	/**
	 * This method initializes ivjchkCustSupplied
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkCustSupplied()
	{
		if (ivjchkCustSupplied == null)
		{
			ivjchkCustSupplied = new JCheckBox();
			ivjchkCustSupplied.setSize(148, 21);
			ivjchkCustSupplied.setText(CUST_SUPPLIED);
			ivjchkCustSupplied.setMnemonic(
				java.awt.event.KeyEvent.VK_C);
			ivjchkCustSupplied.addActionListener(this);
			ivjchkCustSupplied.setLocation(356, 93);
		}
		return ivjchkCustSupplied;
	}

	/**
	 * This method initializes ivjstcLblPlateAge
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateAge()
	{
		if (ivjstcLblPlateAge == null)
		{
			ivjstcLblPlateAge = new JLabel();
			ivjstcLblPlateAge.setBounds(388, 117, 66, 22);
			ivjstcLblPlateAge.setText(PLATE_AGE);
			ivjstcLblPlateAge.setLabelFor(gettxtPlateAge());
			ivjstcLblPlateAge.setDisplayedMnemonic(
				java.awt.event.KeyEvent.VK_A);
		}
		return ivjstcLblPlateAge;
	}

	/**
	 * This method initializes ivjtxtPlateAge
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPlateAge()
	{
		if (ivjtxtPlateAge == null)
		{
			ivjtxtPlateAge = new RTSInputField();
			ivjtxtPlateAge.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtPlateAge.setMaxLength(2);
			ivjtxtPlateAge.setBounds(457, 119, 24, 20);
			ivjtxtPlateAge.setRequestFocusEnabled(true);
		}
		return ivjtxtPlateAge;
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"