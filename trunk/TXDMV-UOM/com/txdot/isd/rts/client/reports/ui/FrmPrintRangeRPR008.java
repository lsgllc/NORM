package com.txdot.isd.rts.client.reports.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmPrintRangeRPR008.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/15/2009	Created
 * 							defect 10086 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * Print Range Frame  
 *
 * @version	Defect_POS_F	06/15/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		06/15/2009
 */
public class FrmPrintRangeRPR008
	extends RTSDialogBox
	implements ItemListener, ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmPrintRangeRPR008ContentPane1 = null;
	private JPanel ivjpnlSelectReport = null;
	private JRadioButton ivjradioPrintAll = null;
	private JRadioButton ivjradioPrintCurrent = null;
	private JRadioButton ivjradioPrintSelected = null;
	private JLabel ivjstclblPrintSelectAssistLine1 = null;
	private JLabel ivjstclblPrintSelectAssistLine2 = null;
	private RTSInputField ivjtxtPageSelection = null;

	// Object 
	private ReportSearchData caRptSearchData;

	// Constants
	private final static int PAGES_MAX_LENGTH = 200;
	private final static String PRINT_ALL = "All";
	private final static String PRINT_CURRENT = "Current";
	private final static String PRINT_RANGE = "Print Range:";
	private final static String PRINT_SELECT_ASSIST_LINE1 =
		"Enter page numbers and/or page ranges";
	private final static String PRINT_SELECT_ASSIST_LINE2 =
		"separated by commas.  For example,  1,3,5-12";
	private final static String PRINT_SELECT_PAGES = "Pages: ";
	private final static String TITLE = "Print   RPR008";

	/**
	 * main used to start class as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmPrintRangeRPR008 laFrmPrintRangeRPR008;
			laFrmPrintRangeRPR008 = new FrmPrintRangeRPR008();
			laFrmPrintRangeRPR008.setModal(true);
			laFrmPrintRangeRPR008
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPrintRangeRPR008.show();
			java.awt.Insets insets = laFrmPrintRangeRPR008.getInsets();
			laFrmPrintRangeRPR008.setSize(
				laFrmPrintRangeRPR008.getWidth()
					+ insets.left
					+ insets.right,
				laFrmPrintRangeRPR008.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmPrintRangeRPR008.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace();
		}
	}

	/**
	 * FrmPrintRangeRPR008 constructor comment.
	 */
	public FrmPrintRangeRPR008()
	{
		super();
		initialize();
	}

	/**
	 * FrmPrintRangeRPR008 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmPrintRangeRPR008(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmPrintRangeRPR008 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmPrintRangeRPR008(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * This method is called when a event happens
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
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateData())
				{
					setDataToDataObject();
					getController().processData(
						AbstractViewController.ENTER,
						caRptSearchData);

				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_MSG_HELP_NOT_AVAILABLE,
						ErrorsConstant.ERR_MSG_HELP_NOT_AVAILABLE_TTL);
				leRTSEx.displayError(this);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ButtonPanel1 property value.
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
				ivjButtonPanel1.setSize(216, 36);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
				ivjButtonPanel1.setLocation(68, 200);
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the FrmPrintRangeRPR008ContentPane1 property
	 * value.
	 * 
	 * @return JPanel
	 */

	private JPanel getFrmPrintRangeRPR008ContentPane1()
	{
		if (ivjFrmPrintRangeRPR008ContentPane1 == null)
		{
			try
			{
				ivjFrmPrintRangeRPR008ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmPrintRangeRPR008ContentPane1.setName(
					"FrmPrintRangeRPR008ContentPane1");
				ivjFrmPrintRangeRPR008ContentPane1.setLayout(null);
				ivjFrmPrintRangeRPR008ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmPrintRangeRPR008ContentPane1.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmPrintRangeRPR008ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmPrintRangeRPR008ContentPane1().add(
					getpnlSelectReport(),
					getpnlSelectReport().getName());
				ivjFrmPrintRangeRPR008ContentPane1.add(
					getButtonPanel1(),
					null);
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjFrmPrintRangeRPR008ContentPane1;
	}

	private JPanel getpnlSelectReport()
	{
		if (ivjpnlSelectReport == null)
		{
			try
			{
				ivjpnlSelectReport = new JPanel();
				ivjpnlSelectReport.setBorder(
					new TitledBorder(new EtchedBorder(), PRINT_RANGE));
				ivjpnlSelectReport.setLayout(null);
				ivjpnlSelectReport.setMaximumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setMinimumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setBounds(20, 27, 315, 166);
				ivjpnlSelectReport.add(getradioPrintAll(), null);
				ivjpnlSelectReport.add(getradioPrintCurrent(), null);
				ivjpnlSelectReport.add(getradioPrintSelected(), null);
				ivjpnlSelectReport.add(gettxtPageSelection(), null);
				ivjpnlSelectReport.add(
					getstclblPrintSelectAssistLine1(),
					null);
				ivjpnlSelectReport.add(
					getstclblPrintSelectAssistLine2(),
					null);
				RTSButtonGroup laradioGrp = new RTSButtonGroup();
				laradioGrp.add(getradioPrintAll());
				laradioGrp.add(getradioPrintCurrent());
				laradioGrp.add(getradioPrintSelected());
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectReport;
	}

	/**
	 * Return the getradioPrintAll property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPrintAll()
	{
		if (ivjradioPrintAll == null)
		{
			try
			{
				ivjradioPrintAll = new JRadioButton();
				ivjradioPrintAll.setBounds(19, 28, 63, 22);
				ivjradioPrintAll.setName("ivjradioPrintAll");
				ivjradioPrintAll.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				ivjradioPrintAll.setText(PRINT_ALL);
				ivjradioPrintAll.setMaximumSize(
					new java.awt.Dimension(145, 22));
				ivjradioPrintAll.setActionCommand(
					CommonConstant.TXT_REPORT);
				ivjradioPrintAll.setMinimumSize(
					new java.awt.Dimension(145, 22));
				ivjradioPrintAll.addItemListener(this);
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioPrintAll;
	}
	/**
	 * Return the radioReceiveHistoryReport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPrintCurrent()
	{
		if (ivjradioPrintCurrent == null)
		{
			try
			{
				ivjradioPrintCurrent = new JRadioButton();
				ivjradioPrintCurrent.setBounds(19, 61, 109, 22);
				ivjradioPrintCurrent.setName("ivjradioPrintCurrent");
				ivjradioPrintCurrent.setMnemonic(
					java.awt.event.KeyEvent.VK_E);
				ivjradioPrintCurrent.setText(PRINT_CURRENT);
				ivjradioPrintCurrent.setMaximumSize(
					new java.awt.Dimension(154, 22));
				ivjradioPrintCurrent.setMinimumSize(
					new java.awt.Dimension(154, 22));
				ivjradioPrintCurrent.setPreferredSize(
					new java.awt.Dimension(100, 35));
				ivjradioPrintCurrent.addItemListener(this);
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioPrintCurrent;
	}
	/**
	 * This method initializes ivjradioPrintSelected
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPrintSelected()
	{
		if (ivjradioPrintSelected == null)
		{
			try
			{
				ivjradioPrintSelected = new JRadioButton();
				ivjradioPrintSelected.setBounds(19, 97, 67, 20);
				ivjradioPrintSelected.setText(PRINT_SELECT_PAGES);
				ivjradioPrintSelected.setMnemonic(
					java.awt.event.KeyEvent.VK_G);
				ivjradioPrintSelected.addItemListener(this);
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioPrintSelected;
	}

	/**
	 * This method initializes ivjstclblPrintSelectAssistLine1
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblPrintSelectAssistLine1()
	{
		if (ivjstclblPrintSelectAssistLine1 == null)
		{
			try
			{
				ivjstclblPrintSelectAssistLine1 = new JLabel();
				ivjstclblPrintSelectAssistLine1.setSize(252, 20);
				ivjstclblPrintSelectAssistLine1.setText(
					PRINT_SELECT_ASSIST_LINE1);
				ivjstclblPrintSelectAssistLine1.setFont(
					new java.awt.Font(
						"Dialog",
						java.awt.Font.PLAIN,
						12));
				ivjstclblPrintSelectAssistLine1.setLocation(40, 120);
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjstclblPrintSelectAssistLine1;
	}

	/**
	 * This method initializes ivjstclblPrintSelectAssistLine2
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblPrintSelectAssistLine2()
	{
		if (ivjstclblPrintSelectAssistLine2 == null)
		{
			try
			{
				ivjstclblPrintSelectAssistLine2 = new JLabel();
				ivjstclblPrintSelectAssistLine2.setSize(262, 20);
				ivjstclblPrintSelectAssistLine2.setText(
					PRINT_SELECT_ASSIST_LINE2);
				ivjstclblPrintSelectAssistLine2.setLocation(40, 139);
				ivjstclblPrintSelectAssistLine2.setFont(
					new java.awt.Font(
						"Dialog",
						java.awt.Font.PLAIN,
						12));
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjstclblPrintSelectAssistLine2;
	}

	/**
	 * This method initializes ivjtxtPageSelection
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPageSelection()
	{
		if (ivjtxtPageSelection == null)
		{
			try
			{
				ivjtxtPageSelection = new RTSInputField();
				ivjtxtPageSelection.setBounds(91, 93, 208, 24);
				ivjtxtPageSelection.setInput(RTSInputField.DEFAULT); 
				ivjtxtPageSelection.setEnabled(false);
				ivjtxtPageSelection.setMaxLength(PAGES_MAX_LENGTH);
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPageSelection;
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
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName(TITLE);
			setSize(361, 270);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(TITLE);
			setContentPane(getFrmPrintRangeRPR008ContentPane1());
			setRequestFocus(false);
		}
		catch (java.lang.Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		addWindowListener(this);
	}

	/**
	 * Item state changed
	 */
	public void itemStateChanged(ItemEvent aeIE)
	{
		clearAllColor(this);

		if (getradioPrintSelected().isSelected())
		{
			gettxtPageSelection().setEnabled(true);
			gettxtPageSelection().requestFocus();
		}
		else
		{
			gettxtPageSelection().setEnabled(false);
			gettxtPageSelection().setText(
				CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		caRptSearchData = (ReportSearchData) aaData;

		getradioPrintAll().requestFocus();
		getradioPrintAll().setSelected(true);
	}

	/**
	 * Save data to caRptSearchData object in order to pass 
	 * information back to RPR000
	 */
	private void setDataToDataObject()
	{
		if (getradioPrintAll().isSelected())
		{
			caRptSearchData.setIntKey4(ReportConstant.PRINT_ALL);
		}
		else if (getradioPrintCurrent().isSelected())
		{
			caRptSearchData.setIntKey4(ReportConstant.PRINT_CURRENT);
		}
		else
		{
			caRptSearchData.setIntKey4(ReportConstant.PRINT_SELECTED);
			caRptSearchData.setKey4(gettxtPageSelection().getText());
			
			if (SystemProperty.isDevStatus())
			{
				System.out.println(PRINT_SELECT_PAGES+caRptSearchData.getKey4()); 	
			}
		}
	}

	/**
	 * Validate Data on Enter
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;
		
		RTSException leRTSEx = new RTSException();
		
		if (getradioPrintSelected().isSelected())
		{
			lbReturn =
				CommonValidations.isValidPrintRange(
					gettxtPageSelection().getText());

			if (!lbReturn)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtPageSelection());
				leRTSEx.displayError(this);
				leRTSEx.getFirstComponent().requestFocus();
			}
		}
		return lbReturn;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
