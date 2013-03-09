package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.DealerData;
import com.txdot.isd.rts.services.data.DealerTitleData;
import com.txdot.isd.rts.services.data.DealerTitleInvalidRecordData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * 
 * FrmDealerMediaContentsDTA007.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validations update
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs			06/27/2002	Eliminated OS/2 flash on error messages 
 * 							defect 4311
 * J Rue		07/09/2002	Added second condition to if statement if 
 * 							(e.getSource()==getButtonPanel1().
 *							getBtnEnter() || e.getSource() == 
 *							getScrollPaneTable()). 
 * 							method actionPerformed()
 * 							defect 4313 
 * J Rue		07/10/2002	Calculate the number of transaction minus 
 * 				08/02/2002	the "***SKIP***". new method calculateTotal 
 * 							CntExclSkip(), modify setData(), 
 * 							checkForSkippedRecords()
 * 							defect 4440 
 * J Rue 		07/11/2002	Ensure there is a desktop or parent screen 
 * 							visible before enterring DTA007 screen, 
 * 							method setData().
 * 							defect 4456 
 * J Rue		07/19/2002	Fixed null pointer error on missing Form31 
 * 							number by putting a null pointer check in 
 * 							calculateTotalCntExclSkip method.
 * 							defect 4490 
 * J Rue		08/12/2002	Add code to set variable if the dealer sends 
 * 							Form31 number = "       ". 
 * 							modify checkForSkippedRecords()
 * 							defect 4490 
 * J Rue 		09/11/2002 	Align the fees on the priliminary frame. 
 * 							Adjusted using Visual Composition. 
 * 							method getScrollPaneTable()
 * 							defect 4703 
 * J Rue		11/01/2002	Set displayError() to desktop if parent does 
 * 							not exist. 
 * 							method setData()
 * 							defect 5003-5006 
 * S Govindappa 03/11/2002	Fix defect 4985. Added try and catch for
 *							NumberFormat exception in
 *							checkForSkippedRecords(..) when form31
 *                          number is worng on the disk.
 * B Arredondo	 02/20/2004	Modify visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * K Harrell	03/20/2004	5.2.0 Merge. Ver 5.2.0 Imported
 *							Ver 5.2.0
 * J Rue		07/21/2004	Incorporate prior 5.1.6 DTA defects to 
 *							5.2.1
 *	`						Defect 7350 Ver 5.2.1
 * J Rue		07/27/2004	Increase the width of the display panel so 
 *							the
 *							 column headings can be read
 *							Adjust using Visual Composition.
 *							defect 7239 VER 5.2.1
 * J Rue		07/28/2004	Center the fees data on DTS007 frame
 *							method gettblDealer()
 *							defect 7379 VER 5.2.1
 * J Rue		08/13/2004	Print Sticker/Receipt Report if RSPS-WSID 
 *							exist.
 * 							modify actionPerformed()
 *							defect 7429 Ver 5.2.1
 * J Rue		12/08/2004	Add new method isNumeric() that returns
 *							a boolean if Form31No number is not numeric. 
 *							Update Prolog and JavaDoc
 *							modify checkForSkippedRecords()
 *							defect 7692 Ver 5.2.2
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/23/2005	VAJ to WSAD Clean Up
 * 							Set check box field length to display text.
 * 							modify Visual Editor
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/06/2005	Remove setNextFocusableComponent()
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/06/2005	Comment out unused imports
 * 							defect 7898 Ver 5.2.3
 * J Rue		05/06/2005	Comment out cursor movement for 
 * 							Enter/Cancel/Help. ButtonPannel
 * 							will controll movement
 * 							modify KeyPressed()
 * 							defect 7719 Ver 5.2.3
 * J Rue		05/19/2005	Replace try/catch with 
 * 							UtilityMethods.isNumeric()
 * 							Clean up code
 * 							modify checkForSkippedRecords()
 * 							defect 7772, 7898 Ver 5.2.3 
 * J Rue		06/15/2005	Match RSPS getters/setters set in 
 * 							DealerTitleData to better define their 
 * 							meaning
 * 							modify actionPerformed()
 * 							defect 8217 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3  
 * J Rue		08/17/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3  
 * J Rue		10/28/2005	Add title to borders
 * 							modify getJPanel1()
 * 							defect 8416 Ver 5.2.3
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3 
 * J Rue		11/09/2005	Rename lsCount to liCount  
 * 							modify calculateTotalCntExclSkip()
 * 							defect 7898 Ver 5.2.3 
 * J Rue		11/21/2005	Put setLayout(null) on a seperate line.
 * 							modify getJPanel1()
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/01/2005	Add colon ":" to constant SELECT_CHOICE
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/06/2005  Correct verbiage in JavaDoc
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/07/2005	Comment out color forground setting to blue.
 * 							modify getlblDealerContact()
 * 							modify getlblDealerId()
 * 							modify getlblDealerName()
 * 							modify getlblDealerPhoneNo()
 * 							defect 7898 Ver 5.2.3
 * J Rue		12/13/2005	Comment out setSelected(true) for arrow key
 * 							movement on radio buttons
 * 							modify KeyPressed()
 * 							defect 7898 Ver 5.2.3
 * Jeff S.		01/04/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), keyTyped(), 
 * 								keyReleased(), ciSelectedNum
 * 							modify getJDialogContentPane(), 
 * 								getradioNoPrintAndContinueProc(),
 * 								getradioPrintAndContinueProc(), 
 * 								getradioPrintAndReturn()
 * 							defect 7898 Ver 5.2.3 
 * J Rue		02/09/2006	Change constant setting PHONE_NO = 3 to
 *          				PHONE_NO = 6
 * 							defect 7898 Ver 5.2.3
 * J Rue		01/15/2009	Change how to calculate skip Form31No using
 * 							ValidateInventoryPattern.calcInvUnknown in
 * 							MediaValidations.isItmBreak()
 * 							modify checkForSkippedRecords()
 * 							defect 9045 Ver Defect_POS_D
 * B Hargrove	03/12/2009	Add column for 'Electronic Title' indicator
 * 							(ETtlRqst). Adjust columns accordingly.
 * 							Use title constants for table columns.
 * 							modify gettblDealer()
 * 							defect 9977 Ver Defect_POS_E
 * B Hargrove	05/08/2009	Make display listbox wider. After ETitle 
 * 							column was added, column names did not fit
 * 							when elevator slider bar is present.
 * 							modify via visual editor
 * 							defect 9977 Ver Defect_POS_E
 * B Hargrove	06/05/2009  Add Flashdrive option to DTA. Change title 
 * 							and screen verbiage from 'diskette'.
 * 							refactor\rename\modify:
 * 							FrmDealerDisketteContentsDTA007 / 
 * 							FrmDealerMediaContentsDTA007
 * 							NO_TRANS_ON_DISKETTE / NO_TRANS_ON_EXTERNAL_MEDIA
 *                   		modify initialize() 
 * 							defect 10075 Ver Defect_POS_F
 * K Harrell	06/25/2009	Implement new DealerData
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	12/28/2009	Add label for 2nd DTA Name. 
 * 							DTA Cleanup
 * 							add ivjtxtDealerName2, get method()
 * 							add calculateTotalFee(), 
 * 							 displayExceptionIfInvalidRecords(),							 
 * 							 handleOverMaxTrans(), 
 * 							 setDealerTransDataToDisplay() 
 * 							delete calculateFee(), 
 * 							   calculateTotalCntExclSkip(), 
 * 							   checkForSkippedRecords()  
 * 							modify getJPanel2(), setData()  
 * 							defect 10290 Ver Defect_POS_H    
 * K Harrell	01/02/2009	Implement DealerTitleData.getDlrBatchNo()
 * 							add logInvalidData()
 * 							modify setDealerTransDataToDisplay()
 * 							defect 10290 Ver Defect_POS_H   
 * ---------------------------------------------------------------------
 */

/**
 * Frame Dealer External Media Contents DTA007
 *
 * @version	Defect_POS_H	01/02/2009	
 * @author	J Kwik
 * <br>Creation Date: 		08/17/2001 15:18:14 
 */
public class FrmDealerMediaContentsDTA007
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJDialogContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblCountyName = null;
	private JLabel ivjlblDealerContact = null;
	private JLabel ivjlblDealerId = null;
	private JLabel ivjlblDealerName1 = null;
	// defect 10290 
	private JLabel ivjlblDealerName2 = null;
	// end defect 10290 
	private JLabel ivjlblDealerPhoneNo = null;
	private JLabel ivjlblDlrBatchNo = null;
	private JLabel ivjlblTotalActualNoTrans = null;
	private JLabel ivjlblTotalFees = null;
	private JRadioButton ivjradioNoPrintAndContinueProc = null;
	private JRadioButton ivjradioPrintAndContinueProc = null;
	private JRadioButton ivjradioPrintAndReturn = null;
	private JLabel ivjstcLblBatchNo = null;
	private JLabel ivjstcLblCounty = null;
	private JLabel ivjstcLblNoTransOnExtMedia = null;
	private JLabel ivjstcLblTotalFees = null;
	private RTSTable ivjtblDealer = null;

	private TMTTLDTA007 caDlrTtlTableModel = null;

	private DealerTitleData caFirstDlrTtlData;

	private int ciSelectedRow = 0;

	private Vector cvDlrTtlData = null;

	private String REFER_TO_RECORD = ".&nbsp &nbsp Refer to Record";

	// Constants
	private final static int AREA_CODE_LENGTH = 3;
	private final static int AREA_CODE_PLUS_PREFIX_LENGTH = 6;
	private final static String BATCH_NO = "Batch Number:";
	private final static String COUNTY = "County:";
	private final static String NO_PRNT_CONTINUE_PROCS =
		"No print & continue processing";
	private final static String NO_TRANS_ON_EXTERNAL_MEDIA =
		"Number of Transactions On External Media:";
	private static final String NORSPRRPT =
		"No RSPS processing has occurred."
			+ "  A Sticker Receipt Report will not be printed.";
	private final static String PRNT_CONTINUE_PROCS =
		"Print & continue processing";
	private final static String PRNT_RTN_MAIN_MENU =
		"Print & Return to main menu";
	private final static String SELECT_CHOICE = "Select Choice:";
	private final static String STKR_RCPT_RPT =
		"Sticker Receipt Report";
	private final static String STR_PHONE_FORMAT = "(###) ###-####";
	private final static String TOTAL_FEES_DEALER =
		"Total Fees Calculated By Dealer:";

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs an array of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmDealerMediaContentsDTA007 laFrmDealerDisketteContentsDTA007;
			laFrmDealerDisketteContentsDTA007 =
				new FrmDealerMediaContentsDTA007();
			laFrmDealerDisketteContentsDTA007.setModal(true);
			laFrmDealerDisketteContentsDTA007
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmDealerDisketteContentsDTA007.show();
			java.awt.Insets insets =
				laFrmDealerDisketteContentsDTA007.getInsets();
			laFrmDealerDisketteContentsDTA007.setSize(
				laFrmDealerDisketteContentsDTA007.getWidth()
					+ insets.left
					+ insets.right,
				laFrmDealerDisketteContentsDTA007.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmDealerDisketteContentsDTA007.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmDealerMediaContentsDTA007 constructor.
	 */
	public FrmDealerMediaContentsDTA007()
	{
		super();
		initialize();
	}

	/**
	 * FrmDealerMediaContentsDTA007 constructor.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmDealerMediaContentsDTA007(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmDealerMediaContentsDTA007 constructor.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmDealerMediaContentsDTA007(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel/Help is pressed
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
			// ENTER 
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10290  
				// Print RSPS Sticker Report via VC  
				if (!UtilityMethods
					.isEmpty(caFirstDlrTtlData.getRSPSId()))
				{
					getController().processData(
						VCDealerMediaContentsDTA007.PRINT_RSPS_STKR_RPT,
						cvDlrTtlData);
					// end defect 10290 
				}
				else
				{
					new RTSException(
						RTSException.INFORMATION_MESSAGE,
						NORSPRRPT,
						STKR_RCPT_RPT).displayError(
						this);
				}

				// No Print and Continue  
				if (getradioNoPrintAndContinueProc().isSelected())
				{
					getController().processData(
						VCDealerMediaContentsDTA007
							.NO_PRINT_AND_CONTINUE,
						cvDlrTtlData);
				}
				// Print and Continue 
				else if (getradioPrintAndContinueProc().isSelected())
				{
					getController().processData(
						VCDealerMediaContentsDTA007.PRINT_AND_CONTINUE,
						cvDlrTtlData);
				}
				// Print and Return   
				else
				{
					getController().processData(
						VCDealerMediaContentsDTA007.PRINT_AND_RETURN,
						cvDlrTtlData);
				}
			}
			// CANCEL 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// HELP 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.DTA007);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Calculate the Total Fee
	 * 
	 * @return Dollar
	 */
	private Dollar calculateTotalFee()
	{
		Dollar laTotalFee = new Dollar(CommonConstant.STR_ZERO_DOLLAR);

		for (int liIndex = 0; liIndex < cvDlrTtlData.size(); liIndex++)
		{
			DealerTitleData laDlrTtlData =
				(DealerTitleData) cvDlrTtlData.get(liIndex);

			if (laDlrTtlData.getFee() != null)
			{
				laTotalFee = laTotalFee.add(laDlrTtlData.getFee());
			}
		}
		return laTotalFee;
	}

	/**
	 * Display Exception if Invalid Records
	 */
	private void displayExceptionIfInvalidRecords()
	{
		boolean lbErrDisp = false;
		String lsRecords = "";
		int liRecordCount = 0;

		// Check for error from diskette
		// Build message including the record(s) in error
		for (int i = 1; i <= cvDlrTtlData.size(); i++)
		{
			DealerTitleData laDlrTtlData =
				(DealerTitleData) cvDlrTtlData.get(i - 1);

			if (laDlrTtlData.isInvalidRecord())
			{
				liRecordCount++;

				logInvalidData(laDlrTtlData, i);

				// First record lbErrDisp will be false. 
				if (lbErrDisp)
				{
					lsRecords += CommonConstant.STR_COMMA;
				}
				lsRecords += i;

				lbErrDisp = true;
			}
			laDlrTtlData.setDTAInvalidRecordData(null);
		}

		if (lbErrDisp)
		{
			String lsSuffix = liRecordCount > 1 ? "s" : "";

			String[] larrStrArray = new String[1];
			String lsMsg =
				REFER_TO_RECORD + lsSuffix + ": " + lsRecords;
			larrStrArray[0] = lsMsg;
			
			lsSuffix = lsSuffix+" ("+liRecordCount+")" ;
			
			Log.write(
				Log.SQL_EXCP,
				this,
				"*** DTA Invalid Record" + lsSuffix + " End *** ");

			// 237
			new RTSException(
				ErrorsConstant.ERR_MSG_DTA_RECORDS_DO_NOT_CONFORM,
				larrStrArray).displayError(
				this);
		}
	}


	/**
	 * Return the ivjButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setBounds(489, 384, 218, 34);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the ivjJDialogContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJDialogContentPane()
	{
		if (ivjJDialogContentPane == null)
		{
			try
			{
				ivjJDialogContentPane = new JPanel();
				ivjJDialogContentPane.setName("ivjJDialogContentPane");
				ivjJDialogContentPane.setLayout(null);
				ivjJDialogContentPane.add(getJScrollPane1(), null);
				ivjJDialogContentPane.add(getstcLblCounty(), null);
				ivjJDialogContentPane.add(getstcLblBatchNo(), null);
				ivjJDialogContentPane.add(getlblCountyName(), null);
				ivjJDialogContentPane.add(getlblDlrBatchNo(), null);
				ivjJDialogContentPane.add(getJPanel1(), null);
				ivjJDialogContentPane.add(getJPanel2(), null);
				ivjJDialogContentPane.add(getButtonPanel1(), null);
				ivjJDialogContentPane.add(getJPanel3(), null);
				// user code begin {1}
				// Button Group to manage tab, cursor movement keys 
				RTSButtonGroup laBG = new RTSButtonGroup();
				laBG.add(getradioPrintAndContinueProc());
				laBG.add(getradioNoPrintAndContinueProc());
				laBG.add(getradioPrintAndReturn());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJDialogContentPane;
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
				// Add title to border
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT_CHOICE));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(310, 113));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(310, 113));
				ivjJPanel1.setBounds(489, 220, 238, 107);
				getJPanel1().add(
					getradioPrintAndContinueProc(),
					getradioPrintAndContinueProc().getName());
				getJPanel1().add(
					getradioNoPrintAndContinueProc(),
					getradioNoPrintAndContinueProc().getName());
				getJPanel1().add(
					getradioPrintAndReturn(),
					getradioPrintAndReturn().getName());
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
		return ivjJPanel1;
	}

	/**
	 * Return the ivjJPanel2 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("ivjJPanel2");
				ivjJPanel2.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjJPanel2.setLayout(null);
				ivjJPanel2.setMaximumSize(
					new java.awt.Dimension(284, 88));
				ivjJPanel2.setAlignmentX(
					java.awt.Component.LEFT_ALIGNMENT);
				ivjJPanel2.setMinimumSize(
					new java.awt.Dimension(284, 88));
				ivjJPanel2.setBounds(489, 93, 239, 111);
				ivjJPanel2.add(getlblDealerName1(), null);
				// defect 10290 
				// Add 2nd Dealer Name 
				ivjJPanel2.add(getlblDealerName2(), null);
				// end defect 10290 
				ivjJPanel2.add(getlblDealerId(), null);
				ivjJPanel2.add(getlblDealerPhoneNo(), null);
				ivjJPanel2.add(getlblDealerContact(), null);
				// user code begin {2}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * Return the ivjJPanel3 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("ivjJPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.add(getstcLblTotalFees(), null);
				ivjJPanel3.add(getstcLblNoTransOnExtMedia(), null);
				ivjJPanel3.add(getlblTotalFees(), null);
				ivjJPanel3.add(getlblTotalActualNoTrans(), null);
				ivjJPanel3.setBounds(42, 385, 396, 56);
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
		return ivjJPanel3;
	}

	/**
	 * Return the ivjJScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("ivjJScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getJScrollPane1().setViewportView(gettblDealer());
				ivjJScrollPane1.setBounds(11, 16, 462, 357);
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
		return ivjJScrollPane1;
	}

	/**
	 * Return the ivjlblCountyName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCountyName()
	{
		if (ivjlblCountyName == null)
		{
			try
			{
				ivjlblCountyName = new javax.swing.JLabel();
				ivjlblCountyName.setSize(116, 20);
				ivjlblCountyName.setName("ivjlblOfcName");
				ivjlblCountyName.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblCountyName.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblCountyName.setLocation(587, 40);
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
		return ivjlblCountyName;
	}

	/**
	 * Return the ivjlblDealerContact property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDealerContact()
	{
		if (ivjlblDealerContact == null)
		{
			try
			{
				ivjlblDealerContact = new javax.swing.JLabel();
				ivjlblDealerContact.setSize(205, 20);
				ivjlblDealerContact.setName("ivjlblDealerContact");
				ivjlblDealerContact.setMaximumSize(
					new java.awt.Dimension(90, 14));
				ivjlblDealerContact.setMinimumSize(
					new java.awt.Dimension(90, 14));
				ivjlblDealerContact.setAlignmentX(
					java.awt.Component.LEFT_ALIGNMENT);
				ivjlblDealerContact.setLocation(10, 85);
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
		return ivjlblDealerContact;
	}

	/**
	 * Return the ivjlblDealerId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDealerId()
	{
		if (ivjlblDealerId == null)
		{
			try
			{
				ivjlblDealerId = new JLabel();
				ivjlblDealerId.setSize(99, 20);
				ivjlblDealerId.setName("ivjlblDealerId");
				ivjlblDealerId.setBackground(
					java.awt.SystemColor.activeCaption);
				ivjlblDealerId.setMaximumSize(
					new java.awt.Dimension(90, 14));
				ivjlblDealerId.setMinimumSize(
					new java.awt.Dimension(90, 14));
				ivjlblDealerId.setLocation(10, 5);
				ivjlblDealerId.setAlignmentX(
					java.awt.Component.LEFT_ALIGNMENT);
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
		return ivjlblDealerId;
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
				ivjlblDealerName1.setSize(223, 20);
				ivjlblDealerName1.setName("ivjlblDealerName1");
				ivjlblDealerName1.setMaximumSize(
					new java.awt.Dimension(90, 14));
				ivjlblDealerName1.setMinimumSize(
					new java.awt.Dimension(90, 14));
				ivjlblDealerName1.setAlignmentX(
					java.awt.Component.LEFT_ALIGNMENT);
				ivjlblDealerName1.setLocation(10, 25);
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
		return ivjlblDealerName1;
	}

	/**
	 * This method initializes ivjtxtDealerName2
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDealerName2()
	{
		if (ivjlblDealerName2 == null)
		{
			ivjlblDealerName2 = new JLabel();
			ivjlblDealerName2.setName("ivjlblDealerName2");
			ivjlblDealerName2.setSize(223, 20);
			ivjlblDealerName2.setLocation(10, 45);
		}
		return ivjlblDealerName2;
	}

	/**
	 * Return the ivjlblDealerPhoneNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDealerPhoneNo()
	{
		if (ivjlblDealerPhoneNo == null)
		{
			try
			{
				ivjlblDealerPhoneNo = new JLabel();
				ivjlblDealerPhoneNo.setSize(205, 20);
				ivjlblDealerPhoneNo.setName("ivjlblDealerPhoneNo");
				ivjlblDealerPhoneNo.setMaximumSize(
					new java.awt.Dimension(90, 14));
				ivjlblDealerPhoneNo.setAlignmentX(
					java.awt.Component.LEFT_ALIGNMENT);
				ivjlblDealerPhoneNo.setMinimumSize(
					new java.awt.Dimension(90, 14));
				ivjlblDealerPhoneNo.setLocation(10, 65);
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
		return ivjlblDealerPhoneNo;
	}

	/**
	 * Return the ivjlblDlrBatchNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDlrBatchNo()
	{
		if (ivjlblDlrBatchNo == null)
		{
			try
			{
				ivjlblDlrBatchNo = new JLabel();
				ivjlblDlrBatchNo.setSize(116, 20);
				ivjlblDlrBatchNo.setName("ivjlblDlrBatchNo");
				ivjlblDlrBatchNo.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblDlrBatchNo.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblDlrBatchNo.setLocation(587, 59);
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
		return ivjlblDlrBatchNo;
	}

	/**
	 * Return the ivjlblTotalActualNoTrans property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTotalActualNoTrans()
	{
		if (ivjlblTotalActualNoTrans == null)
		{
			try
			{
				ivjlblTotalActualNoTrans = new JLabel();
				ivjlblTotalActualNoTrans.setSize(46, 20);
				ivjlblTotalActualNoTrans.setName(
					"ivjlblTotalActualNoTrans");
				ivjlblTotalActualNoTrans.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblTotalActualNoTrans.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblTotalActualNoTrans.setLocation(272, 27);
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
		return ivjlblTotalActualNoTrans;
	}

	/**
	 * Return the ivjlblTotalFees property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTotalFees()
	{
		if (ivjlblTotalFees == null)
		{
			try
			{
				ivjlblTotalFees = new JLabel();
				ivjlblTotalFees.setSize(79, 20);
				ivjlblTotalFees.setName("ivjlblTotalFees");
				ivjlblTotalFees.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblTotalFees.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblTotalFees.setLocation(248, 3);
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
		return ivjlblTotalFees;
	}

	/**
	 * Return String in Phone No Format
	 * 
	 * @param asPhoneNo String
	 * @return String
	 */
	private String getPhoneFormat(String asPhoneNo)
	{
		StringBuffer lsStrBuff = new StringBuffer();

		if (!UtilityMethods.isEmpty(asPhoneNo))
		{
			asPhoneNo = asPhoneNo.trim();

			if (asPhoneNo.length() > AREA_CODE_LENGTH)
			{
				// Phone number = 5124446789
				// Build the Area Code (512)  
				lsStrBuff.append(CommonConstant.STR_OPEN_PARENTHESES);
				lsStrBuff.append(
					asPhoneNo.substring(0, AREA_CODE_LENGTH));
				lsStrBuff.append(
					CommonConstant.STR_CLOSE_PARENTHESES
						+ CommonConstant.STR_SPACE_ONE);

				if (asPhoneNo.length() >= AREA_CODE_PLUS_PREFIX_LENGTH)
				{
					// Append 444 - to (512)
					lsStrBuff.append(
						asPhoneNo.substring(
							AREA_CODE_LENGTH,
							AREA_CODE_PLUS_PREFIX_LENGTH));

					lsStrBuff.append(CommonConstant.STR_DASH);

					// Append 6789 to (512) 444 -
					if (asPhoneNo.length()
						> AREA_CODE_PLUS_PREFIX_LENGTH)
					{
						lsStrBuff.append(
							asPhoneNo.substring(
								AREA_CODE_PLUS_PREFIX_LENGTH));
					}
				}
				else
				{
					lsStrBuff.append(
						asPhoneNo.substring(AREA_CODE_LENGTH));
				}
			}
			else
			{
				lsStrBuff.append(asPhoneNo);
			}
		}
		return lsStrBuff.toString();
	}

	/**
	 * Return the ivjradioNoPrintAndContinueProc property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioNoPrintAndContinueProc()
	{
		if (ivjradioNoPrintAndContinueProc == null)
		{
			try
			{
				ivjradioNoPrintAndContinueProc =
					new javax.swing.JRadioButton();
				ivjradioNoPrintAndContinueProc.setName(
					"ivjradioNoPrintAndContinueProc");
				ivjradioNoPrintAndContinueProc.setMnemonic(
					KeyEvent.VK_N);
				ivjradioNoPrintAndContinueProc.setText(
					NO_PRNT_CONTINUE_PROCS);
				ivjradioNoPrintAndContinueProc.setMaximumSize(
					new java.awt.Dimension(244, 22));
				ivjradioNoPrintAndContinueProc.setActionCommand(
					NO_PRNT_CONTINUE_PROCS);
				ivjradioNoPrintAndContinueProc.setBounds(
					11,
					47,
					200,
					22);
				ivjradioNoPrintAndContinueProc.setMinimumSize(
					new java.awt.Dimension(244, 22));
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
		return ivjradioNoPrintAndContinueProc;
	}

	/**
	 * Return the ivjradioPrintAndContinueProc property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPrintAndContinueProc()
	{
		if (ivjradioPrintAndContinueProc == null)
		{
			try
			{
				ivjradioPrintAndContinueProc =
					new javax.swing.JRadioButton();
				ivjradioPrintAndContinueProc.setName(
					"ivjradioPrintAndContinueProc");
				ivjradioPrintAndContinueProc.setMnemonic(KeyEvent.VK_P);
				ivjradioPrintAndContinueProc.setText(
					PRNT_CONTINUE_PROCS);
				ivjradioPrintAndContinueProc.setMaximumSize(
					new java.awt.Dimension(180, 22));
				ivjradioPrintAndContinueProc.setActionCommand(
					PRNT_CONTINUE_PROCS);
				ivjradioPrintAndContinueProc.setBounds(11, 17, 200, 22);
				ivjradioPrintAndContinueProc.setMinimumSize(
					new java.awt.Dimension(180, 22));
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
		return ivjradioPrintAndContinueProc;
	}

	/**
	 * Return the ivjradioPrintAndReturn property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPrintAndReturn()
	{
		if (ivjradioPrintAndReturn == null)
		{
			try
			{
				ivjradioPrintAndReturn = new JRadioButton();
				ivjradioPrintAndReturn.setName(
					"ivjradioPrintAndReturn");
				ivjradioPrintAndReturn.setMnemonic(KeyEvent.VK_R);
				ivjradioPrintAndReturn.setText(PRNT_RTN_MAIN_MENU);
				ivjradioPrintAndReturn.setMaximumSize(
					new java.awt.Dimension(180, 22));
				ivjradioPrintAndReturn.setMinimumSize(
					new java.awt.Dimension(180, 22));
				ivjradioPrintAndReturn.setActionCommand(
					PRNT_RTN_MAIN_MENU);
				ivjradioPrintAndReturn.setBounds(11, 78, 200, 22);
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
		return ivjradioPrintAndReturn;
	}

	/**
	 * Return the ivjstcLblBatchNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBatchNo()
	{
		if (ivjstcLblBatchNo == null)
		{
			try
			{
				ivjstcLblBatchNo = new JLabel();
				ivjstcLblBatchNo.setBounds(489, 59, 91, 20);
				ivjstcLblBatchNo.setName("ivjstcLblBatchNo");
				ivjstcLblBatchNo.setText(BATCH_NO);
				ivjstcLblBatchNo.setMaximumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblBatchNo.setMinimumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblBatchNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblBatchNo;
	}

	/**
	 * Return the stcLblCounty property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCounty()
	{
		if (ivjstcLblCounty == null)
		{
			try
			{
				ivjstcLblCounty = new JLabel();
				ivjstcLblCounty.setBounds(489, 40, 91, 20);
				ivjstcLblCounty.setName("stcLblCounty");
				ivjstcLblCounty.setText(COUNTY);
				ivjstcLblCounty.setMaximumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblCounty.setMinimumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblCounty.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblCounty;
	}

	/**
	 * Return the ivjstcLblNoTransOnExtMedia property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblNoTransOnExtMedia()
	{
		if (ivjstcLblNoTransOnExtMedia == null)
		{
			try
			{
				ivjstcLblNoTransOnExtMedia = new JLabel();
				ivjstcLblNoTransOnExtMedia.setSize(255, 20);
				ivjstcLblNoTransOnExtMedia.setName(
					"ivjstcLblNoTransOnExtMedia");
				ivjstcLblNoTransOnExtMedia.setText(
					NO_TRANS_ON_EXTERNAL_MEDIA);
				ivjstcLblNoTransOnExtMedia.setMaximumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblNoTransOnExtMedia.setMinimumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblNoTransOnExtMedia.setLocation(8, 27);
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
		return ivjstcLblNoTransOnExtMedia;
	}

	/**
	 * Return the ivjstcLblTotalFees property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTotalFees()
	{
		if (ivjstcLblTotalFees == null)
		{
			try
			{
				ivjstcLblTotalFees = new JLabel();
				ivjstcLblTotalFees.setSize(197, 20);
				ivjstcLblTotalFees.setName("ivjstcLblTotalFees");
				ivjstcLblTotalFees.setText(TOTAL_FEES_DEALER);
				ivjstcLblTotalFees.setMaximumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblTotalFees.setMinimumSize(
					new java.awt.Dimension(35, 14));
				// user code begin {1}
				ivjstcLblTotalFees.setHorizontalTextPosition(
					javax.swing.SwingConstants.TRAILING);
				ivjstcLblTotalFees.setLocation(43, 3);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTotalFees;
	}

	/**
	 * Return the ivjtblDealer property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblDealer()
	{
		if (ivjtblDealer == null)
		{
			try
			{
				ivjtblDealer =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjtblDealer.setName("ivjtblDealer");
				getJScrollPane1().setColumnHeaderView(
					ivjtblDealer.getTableHeader());
				ivjtblDealer.setModel(new TMTTLDTA007());
				ivjtblDealer.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblDealer.setShowVerticalLines(false);
				ivjtblDealer.setShowHorizontalLines(false);
				ivjtblDealer.setAutoCreateColumnsFromModel(false);
				ivjtblDealer.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblDealer.setBounds(0, 0, 200, 200);
				ivjtblDealer.setRowHeight(26);
				// user code begin {1}
				caDlrTtlTableModel =
					(TMTTLDTA007) ivjtblDealer.getModel();
				ivjtblDealer.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblDealer.init();
				// defect 9977
				// Add Electronic Title as 3rd column
				ivjtblDealer.setColumnSize(
					TitleConstant.DTA007_COL_PRINTED,
					.08);
				ivjtblDealer.setColumnSize(
					TitleConstant.DTA007_COL_PROCESSED,
					.08);
				ivjtblDealer.setColumnSize(
					TitleConstant.DTA007_COL_ETITLE,
					.08);
				ivjtblDealer.setColumnSize(
					TitleConstant.DTA007_COL_FORM31,
					.3);
				ivjtblDealer.setColumnSize(
					TitleConstant.DTA007_COL_FEES,
					.54);
				ivjtblDealer.setColumnAlignment(
					TitleConstant.DTA007_COL_PRINTED,
					RTSTable.CENTER);
				ivjtblDealer.setColumnAlignment(
					TitleConstant.DTA007_COL_PROCESSED,
					RTSTable.CENTER);
				ivjtblDealer.setColumnAlignment(
					TitleConstant.DTA007_COL_ETITLE,
					RTSTable.CENTER);
				ivjtblDealer.setColumnAlignment(
					TitleConstant.DTA007_COL_FORM31,
					RTSTable.CENTER);
				// defect 7379
				// Set fees data to center
				ivjtblDealer.setColumnAlignment(
					TitleConstant.DTA007_COL_FEES,
					RTSTable.CENTER);
				//ivjtblDealer.setColumnAlignment(3, RTSTable.RIGHT);
				// end defect 7379
				// end defect 9977
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblDealer;
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
	 * If over Maximum DTA Transactions, Show Error, Remove Records
	 */
	private void handleOverMaxTrans()
	{
		if (cvDlrTtlData.size() > TitleConstant.DTA_MAX_RECORDS)
		{
			// Display Error if > Max (25) records 
			// RTSException leRTSEx = new RTSException(262);
			// leRTSEx.displayError(this)
			new RTSException(
				ErrorsConstant
					.ERR_MSG_DTA_MORE_THAN_MAX_TRANS)
					.displayError(
				this);

			int liSize = cvDlrTtlData.size();

			for (; liSize > TitleConstant.DTA_MAX_RECORDS; liSize--)
				cvDlrTtlData.remove(liSize - 1);
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
			setName(ScreenConstant.DTA007_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(741, 475);
			setTitle(ScreenConstant.DTA007_FRAME_TITLE);
			setContentPane(getJDialogContentPane());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}

		// user code begin {2}
		gettblDealer().requestFocus();

		//set the default selection
		getradioPrintAndContinueProc().setSelected(true);
		// user code end
	}

	/**
	 * Log InvalidData
	 * 
	 * @param aaDlrTtlData 
	 * @param aiTransNo 
	 */
	private void logInvalidData(
		DealerTitleData aaDlrTtlData,
		int aiTransNo)
	{
		Vector lvDTAInvalidRecordData =
			aaDlrTtlData.getDTAInvalidRecordData();

		Log.write(Log.SQL_EXCP, this, "*** DTA Invalid Record *** ");

		for (int j = 0; j < lvDTAInvalidRecordData.size(); j++)
		{
			DealerTitleInvalidRecordData laDTAInvRcdData =
				(
					DealerTitleInvalidRecordData) lvDTAInvalidRecordData
						.elementAt(
					j);

			Log.write(Log.SQL_EXCP, this, "TransNo: " + aiTransNo);
			Log.write(
				Log.SQL_EXCP,
				this,
				"Form31No: " + aaDlrTtlData.getForm31No());
			Log.write(
				Log.SQL_EXCP,
				this,
				"VIN: "
					+ aaDlrTtlData
						.getMFVehicleData()
						.getVehicleData()
						.getVin());
			Log.write(
				Log.SQL_EXCP,
				this,
				"Field Name: " + laDTAInvRcdData.getFieldName());
			Log.write(
				Log.SQL_EXCP,
				this,
				"Field AbstractValue: " + laDTAInvRcdData.getFieldValue());
			Log.write(
				Log.SQL_EXCP,
				this,
				"Reset AbstractValue: " + laDTAInvRcdData.getResetValue());
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			cvDlrTtlData = (Vector) aaDataObject;

			// defect 10290 
			// Display Exception / Remove records if over 25 trans 
			handleOverMaxTrans();

			// Display Exception if any record marked Invalid 			
			displayExceptionIfInvalidRecords();

			// Display Dealer Data / Dealer Transaction Data  
			setDealerTransDataToDisplay();
			// end defect 10290 
		}
	}

	/**
	 * Set Dealer Transaction Data to Display 
	 */
	private void setDealerTransDataToDisplay()
	{
		// Sort Vector by Form31 InvItmNo
		UtilityMethods.sort(cvDlrTtlData);

		caFirstDlrTtlData = (DealerTitleData) cvDlrTtlData.get(0);

		// Set in TitleClientBusiness when parse DTA Data 
		DealerData laDealerData = Transaction.getDTADealerData();

		if (laDealerData != null)
		{
			getlblDealerName1().setText(laDealerData.getName1());

			if (!UtilityMethods.isEmpty(laDealerData.getName2()))
			{
				getlblDealerName2().setText(laDealerData.getName2());
			}

			getlblDealerContact().setText(laDealerData.getContact());

			getlblDealerPhoneNo().setText(
				getPhoneFormat(laDealerData.getPhoneNo()));
		}
		else
		{
			getlblDealerPhoneNo().setText(STR_PHONE_FORMAT);

			// 734
			new RTSException(
				ErrorsConstant
					.ERR_MSG_DTA_DISKETTE_INVALID_DEALERID)
					.displayError(
				this);
		}

		// County (Office) Name 
		getlblCountyName().setText(
			OfficeIdsCache.getOfcName(
				SystemProperty.getOfficeIssuanceNo()));

		// Dealer Id
		getlblDealerId().setText(caFirstDlrTtlData.getStrDealerId());

		// Batch No 
		getlblDlrBatchNo().setText(caFirstDlrTtlData.getDlrBatchNo());

		// Total Transactions, Total Fees 
		// Note: These Totals are after removal of Trans > Max 
		getlblTotalActualNoTrans().setText(
			CommonConstant.STR_SPACE_EMPTY + cvDlrTtlData.size());

		getlblTotalFees().setText(calculateTotalFee().printDollar());

		// Table Model using new Vector w/ Skips  
		Vector lvData =
			UtilityMethods.getNewDTADlrTtlDataVectorWithSkip(
				cvDlrTtlData);

		caDlrTtlTableModel.add(lvData);

		gettblDealer().setRowSelectionInterval(0, 0);
		// end defect 10290 
	}
	
	/** 
	 * Called whenever the value of the selection changes.
	 * 
	 * @param aaLSE the event that characterizes the change.
	 */
	public void valueChanged(
		javax.swing.event.ListSelectionEvent aaLSE)
	{
		ciSelectedRow = gettblDealer().getSelectedRow();
	}
}
