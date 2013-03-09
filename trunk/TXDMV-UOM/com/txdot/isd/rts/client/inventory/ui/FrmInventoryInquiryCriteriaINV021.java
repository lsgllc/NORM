package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.InventoryInquiryUIData;
import com.txdot.isd.rts.services.data.SubstationData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmInventoryInquiryCriteriaINV021.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang     06/30/2003  Check if frame is visible to prevent null 
 * 							pointer.
 * 							modify actionPerformed()
 * 							defect 5940
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source.
 * 							rename fields.
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3 
 * Ray Rowehl	08/09/2005	Cleanup pass
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove key processing on button panel.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants 
 * 							classes.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/17/2005	Remove selection from key processing.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	12/08/2005	Allow arrow keys to move between the check
 * 							boxes.
 * 							modify getchkCurrBal(), getchkHistory()
 * 								keyPressed
 * 							defect 7890 Ver 5.2.3
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setpnlSelectSubstaOfc()
 * 							defect 8479 Ver 5.2.3 
 * Jeff S.		12/20/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.  Added group of check
 * 							boxes to a group so that arrowing does not
 * 							have to be handled in this class.
 * 							remove keyPressed()
 * 							modify getpnlSelectOne(), getchkCurrBal()
 * 								getpnlSelectOneOrBoth(),getchkHistory(),
 * 								getradioMainOfc(), getradioSubsta()
 * 							defect 7890 Ver 5.2.3 
 * Min Wang 	03/01/2007	Add check box for Current Virtual Inventory
 * 							report.
 * 							add getchkCurVir()
 * 							modify captureUserInput(), 
 * 							setpnlSelectDtForHistory(), validateFrame()
 * 							defect 9117 Ver Special Plates
 * Min Wang		03/28/2007	make check box work properly for VI report.
 * 							modify setpnlSelectDtForHistory()
 * 							defect 9117 Ver Special Plates
 * Min Wang		05/03/2007	VI report is only available to HQ.
 * 							modify setData()
 * 							defect 9117 Ver Special Plates
 * K Harrell	07/08/2007	Only VI report available to HQ
 * 							comment out setpnlSelectDtForHistory()
 * 							delete getBuilderData()
 * 							add handleChkboxSelection()
 * 							modify setData(), actionPerformed(), 
 * 							 getRadioMainOfc(), getRadioSubsta()
 * 							defect 9085 Ver Special Plates 
 * Min Wang  	05/22/2008	disable Substation Radio Button if 
 * 							no substations exist 
 * 							modify setData()
 * 							defect 8597 Ver Defect_POS_A
 * K Harrell	08/25/2009	Implement ReportSearchData.initForClient()
 * 							modify actionPerformed() 
 * 							defect 8628 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * In the Inquiry event, frame INV021 prompts for the Inquiry report
 * type and for which office the report should be run.
 *
 * @version	Defect_POS_F	08/25/2009
 * @author	Charlie Walker
 * <br>Creation Date:		06/28/2001 10:08:40
 */

public class FrmInventoryInquiryCriteriaINV021
	extends RTSDialogBox
	implements ActionListener
{

	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkCurrBal = null;
	private JCheckBox ivjchkCurrVir = null;
	private JCheckBox ivjchkHistory = null;
	private JComboBox ivjcomboSubstaOfcs = null;
	private JPanel ivjFrmInventoryInquiryCriteriaINV021ContentPane1 =
		null;
	private JPanel ivjpnlSelectDtForHistory = null;
	private JPanel ivjpnlSelectOne = null;
	private JPanel ivjpnlSelectOneOrBoth = null;
	private JPanel ivjpnlSelectSubstaOfc = null;
	private JRadioButton ivjradioMainOfc = null;
	private JRadioButton ivjradioSubsta = null;
	private JLabel ivjstcLblBegDt = null;
	private JLabel ivjstcLblEndDt = null;
	private RTSDateField ivjtxtBegDt = null;
	private RTSDateField ivjtxtEndDt = null;
	
	/**
	 * InventoryInquiryUIData object used to collect the UI data
	 */
	private InventoryInquiryUIData caInvInqUIData =
		new InventoryInquiryUIData();

	/**
	 * Vector used to store the Substations for the combo box
	 */
	private Vector cvSubstaData = new Vector();

	// Constant 
	private static final int MAX_SUBSTAS_SHOW = 4;
	
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
			FrmInventoryInquiryCriteriaINV021 laFrmInventoryInquiryCriteriaINV021;
			laFrmInventoryInquiryCriteriaINV021 =
				new FrmInventoryInquiryCriteriaINV021();
			laFrmInventoryInquiryCriteriaINV021.setModal(true);
			laFrmInventoryInquiryCriteriaINV021
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmInventoryInquiryCriteriaINV021.show();
			java.awt.Insets insets =
				laFrmInventoryInquiryCriteriaINV021.getInsets();
			laFrmInventoryInquiryCriteriaINV021.setSize(
				laFrmInventoryInquiryCriteriaINV021.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryInquiryCriteriaINV021.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryInquiryCriteriaINV021.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmInventoryInquiryCriteriaINV021 constructor comment.
	 */
	public FrmInventoryInquiryCriteriaINV021()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryInquiryCriteriaINV021 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryInquiryCriteriaINV021(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryInquiryCriteriaINV021 constructor comment.
	 * @param aaParent JFrame
	 */
	public FrmInventoryInquiryCriteriaINV021(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Used to determine what action to take when an action is 
	 * performed on the screen.
	 *
	 *<p>Actions are not available while the frame is not visible.
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

			// CHECKBOX 
			if (aaAE.getSource() instanceof JCheckBox)
			{
				// defect 9085
				handleChkboxSelection((JCheckBox) aaAE.getSource());
				// end defect 9085 
			}
			// RADIO BUTTON 
			else if (aaAE.getSource() instanceof JRadioButton)
			{
				setpnlSelectSubstaOfc();
			}
			// ENTER
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Validate the check boxes, dates and combo box on 
				// the screen
				if (!validateFrame())
				{
					return;
				}

				// Store the user input.
				captureUserInput();

				getController().processData(
					AbstractViewController.ENTER,
					caInvInqUIData);
			}
			// CANCEL 
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caInvInqUIData);
			}
			// HELP 
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV021);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture the user input so it can be sent to the next screen.
	 */
	private void captureUserInput()
	{
		// Save the report type
		if (getchkCurrBal().isSelected()
			&& !getchkHistory().isSelected())
		{
			caInvInqUIData.setRptType(InventoryConstant.CUR_BAL);
		}
		else if (
			!getchkCurrBal().isSelected()
				&& getchkHistory().isSelected())
		{
			caInvInqUIData.setRptType(InventoryConstant.HISTORY);
			caInvInqUIData.setBeginDt(gettxtBegDt().getDate());
			caInvInqUIData.setEndDt(gettxtEndDt().getDate());
		}
		else if (
			getchkCurrBal().isSelected()
				&& getchkHistory().isSelected())
		{
			caInvInqUIData.setRptType(
				InventoryConstant.CUR_BAL_HISTORY);
			caInvInqUIData.setBeginDt(gettxtBegDt().getDate());
			caInvInqUIData.setEndDt(gettxtEndDt().getDate());
		}
		// defect 9117
		else if (getchkCurrVir().isSelected())
		{
			caInvInqUIData.setRptType(InventoryConstant.CUR_VIRTUAL);
		}
		// end defect 9117
		// Save the office type
		if (getradioMainOfc().isSelected())
		{
			caInvInqUIData.setOfcType(CommonConstant.STR_SPACE_EMPTY);
			caInvInqUIData.setSubstaId(0);
		}
		else if (getradioSubsta().isSelected())
		{
			String lsSubstaName =
				(String) getcomboSubstaOfcs().getSelectedItem();
			int liSubstaId =
				new Integer(
					lsSubstaName.substring(
						0,
						lsSubstaName.indexOf(
							CommonConstant.STR_SPACE_ONE)))
					.intValue();
			caInvInqUIData.setOfcType(lsSubstaName);
			caInvInqUIData.setSubstaId(liSubstaId);
		}
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
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
	 * Return the chkCurrentBalance property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkCurrBal()
	{
		if (ivjchkCurrBal == null)
		{
			try
			{
				ivjchkCurrBal = new JCheckBox();
				ivjchkCurrBal.setName("chkCurrBal");
				ivjchkCurrBal.setMnemonic(java.awt.event.KeyEvent.VK_B);
				ivjchkCurrBal.setText(
					InventoryConstant.TXT_CURRENT_BALANCE);
				ivjchkCurrBal.setMaximumSize(
					new java.awt.Dimension(117, 22));
				ivjchkCurrBal.setActionCommand(
					InventoryConstant.TXT_CURRENT_BALANCE);
				ivjchkCurrBal.setMinimumSize(
					new java.awt.Dimension(117, 22));
				// defect 9117
				getchkCurrBal().addActionListener(this);
				// end defect 9117
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkCurrBal;
	}
	/**
	 * This method initializes jCheckBox
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkCurrVir()
	{
		if (ivjchkCurrVir == null)
		{
			ivjchkCurrVir = new JCheckBox();
			ivjchkCurrVir.setName("chkCurrVir");
			ivjchkCurrVir.setMnemonic(java.awt.event.KeyEvent.VK_V);
			ivjchkCurrVir.setText("Current Virtual");
			ivjchkCurrVir.setActionCommand(
				InventoryConstant.CUR_VIRTUAL);
			getchkCurrVir().addActionListener(this);
		}
		return ivjchkCurrVir;
	}

	/**
	 * Return the chkHistory property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkHistory()
	{
		if (ivjchkHistory == null)
		{
			try
			{
				ivjchkHistory = new JCheckBox();
				ivjchkHistory.setName("chkHistory");
				ivjchkHistory.setMnemonic(java.awt.event.KeyEvent.VK_T);
				ivjchkHistory.setText(InventoryConstant.TXT_HISTORY);
				ivjchkHistory.setMaximumSize(
					new java.awt.Dimension(65, 22));
				ivjchkHistory.setActionCommand(
					InventoryConstant.TXT_HISTORY);
				ivjchkHistory.setMinimumSize(
					new java.awt.Dimension(65, 22));
				// user code begin {1}
				getchkHistory().addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkHistory;
	}

	/**
	 * Return the comboSubstationOffices property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboSubstaOfcs()
	{
		if (ivjcomboSubstaOfcs == null)
		{
			try
			{
				ivjcomboSubstaOfcs = new JComboBox();
				ivjcomboSubstaOfcs.setName("comboSubstaOfcs");
				ivjcomboSubstaOfcs.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboSubstaOfcs.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboSubstaOfcs.setBackground(java.awt.Color.white);
				ivjcomboSubstaOfcs.setMaximumRowCount(MAX_SUBSTAS_SHOW);
				ivjcomboSubstaOfcs.setEnabled(false);
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
		return ivjcomboSubstaOfcs;
	}

	/**
	 * Return the FrmInventoryInquiryCriteriaINV021ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmInventoryInquiryCriteriaINV021ContentPane1()
	{
		if (ivjFrmInventoryInquiryCriteriaINV021ContentPane1 == null)
		{
			try
			{
				ivjFrmInventoryInquiryCriteriaINV021ContentPane1 =
					new JPanel();
				ivjFrmInventoryInquiryCriteriaINV021ContentPane1
					.setName(
					"FrmInventoryInquiryCriteriaINV021ContentPane1");
				ivjFrmInventoryInquiryCriteriaINV021ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmInventoryInquiryCriteriaINV021ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(462, 484));
				ivjFrmInventoryInquiryCriteriaINV021ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(462, 484));
				ivjFrmInventoryInquiryCriteriaINV021ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);

				java
					.awt
					.GridBagConstraints constraintspnlSelectOneOrBoth =
					new java.awt.GridBagConstraints();
				constraintspnlSelectOneOrBoth.gridx = 1;
				constraintspnlSelectOneOrBoth.gridy = 1;
				constraintspnlSelectOneOrBoth.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectOneOrBoth.weightx = 1.0;
				constraintspnlSelectOneOrBoth.weighty = 1.0;
				constraintspnlSelectOneOrBoth.ipadx = -12;
				constraintspnlSelectOneOrBoth.ipady = -20;
				constraintspnlSelectOneOrBoth.insets =
					new java.awt.Insets(12, 20, 8, 21);
				getFrmInventoryInquiryCriteriaINV021ContentPane1().add(
					getpnlSelectOneOrBoth(),
					constraintspnlSelectOneOrBoth);

				java
					.awt
					.GridBagConstraints constraintspnlSelectDtForHistory =
					new java.awt.GridBagConstraints();
				constraintspnlSelectDtForHistory.gridx = 1;
				constraintspnlSelectDtForHistory.gridy = 2;
				constraintspnlSelectDtForHistory.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectDtForHistory.weightx = 1.0;
				constraintspnlSelectDtForHistory.weighty = 1.0;
				constraintspnlSelectDtForHistory.ipadx = -12;
				constraintspnlSelectDtForHistory.ipady = -20;
				constraintspnlSelectDtForHistory.insets =
					new java.awt.Insets(8, 20, 7, 21);
				getFrmInventoryInquiryCriteriaINV021ContentPane1().add(
					getpnlSelectDtForHistory(),
					constraintspnlSelectDtForHistory);

				java.awt.GridBagConstraints constraintspnlSelectOne =
					new java.awt.GridBagConstraints();
				constraintspnlSelectOne.gridx = 1;
				constraintspnlSelectOne.gridy = 3;
				constraintspnlSelectOne.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectOne.weightx = 1.0;
				constraintspnlSelectOne.weighty = 1.0;
				constraintspnlSelectOne.ipadx = -12;
				constraintspnlSelectOne.ipady = -20;
				constraintspnlSelectOne.insets =
					new java.awt.Insets(8, 20, 5, 21);
				getFrmInventoryInquiryCriteriaINV021ContentPane1().add(
					getpnlSelectOne(),
					constraintspnlSelectOne);

				java
					.awt
					.GridBagConstraints constraintspnlSelectSubstaOfc =
					new java.awt.GridBagConstraints();
				constraintspnlSelectSubstaOfc.gridx = 1;
				constraintspnlSelectSubstaOfc.gridy = 4;
				constraintspnlSelectSubstaOfc.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectSubstaOfc.weightx = 1.0;
				constraintspnlSelectSubstaOfc.weighty = 1.0;
				constraintspnlSelectSubstaOfc.ipadx = -12;
				constraintspnlSelectSubstaOfc.ipady = -20;
				constraintspnlSelectSubstaOfc.insets =
					new java.awt.Insets(6, 20, 5, 21);
				getFrmInventoryInquiryCriteriaINV021ContentPane1().add(
					getpnlSelectSubstaOfc(),
					constraintspnlSelectSubstaOfc);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 5;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 48;
				constraintsButtonPanel1.ipady = 1;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(5, 92, 13, 93);
				getFrmInventoryInquiryCriteriaINV021ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
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
		return ivjFrmInventoryInquiryCriteriaINV021ContentPane1;
	}

	/**
	 * Return the pnlSelectDateForHistory property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectDtForHistory()
	{
		if (ivjpnlSelectDtForHistory == null)
		{
			try
			{
				ivjpnlSelectDtForHistory = new JPanel();
				ivjpnlSelectDtForHistory.setName(
					"pnlSelectDtForHistory");
				ivjpnlSelectDtForHistory.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant
							.TXT_SELECT_BEG_END_DATE_HIST));
				ivjpnlSelectDtForHistory.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlSelectDtForHistory.setMaximumSize(
					new java.awt.Dimension(421, 80));
				ivjpnlSelectDtForHistory.setMinimumSize(
					new java.awt.Dimension(421, 80));

				java.awt.GridBagConstraints constraintsstcLblBegDt =
					new java.awt.GridBagConstraints();
				constraintsstcLblBegDt.gridx = 1;
				constraintsstcLblBegDt.gridy = 1;
				constraintsstcLblBegDt.ipadx = 8;
				constraintsstcLblBegDt.insets =
					new java.awt.Insets(24, 13, 22, 2);
				getpnlSelectDtForHistory().add(
					getstcLblBegDt(),
					constraintsstcLblBegDt);

				java.awt.GridBagConstraints constraintsstcLblEndDt =
					new java.awt.GridBagConstraints();
				constraintsstcLblEndDt.gridx = 3;
				constraintsstcLblEndDt.gridy = 1;
				constraintsstcLblEndDt.ipadx = 6;
				constraintsstcLblEndDt.insets =
					new java.awt.Insets(24, 17, 22, 2);
				getpnlSelectDtForHistory().add(
					getstcLblEndDt(),
					constraintsstcLblEndDt);

				java.awt.GridBagConstraints constraintstxtBegDt =
					new java.awt.GridBagConstraints();
				constraintstxtBegDt.gridx = 2;
				constraintstxtBegDt.gridy = 1;
				constraintstxtBegDt.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtBegDt.weightx = 1.0;
				constraintstxtBegDt.ipadx = 96;
				constraintstxtBegDt.insets =
					new java.awt.Insets(21, 3, 19, 17);
				getpnlSelectDtForHistory().add(
					gettxtBegDt(),
					constraintstxtBegDt);

				java.awt.GridBagConstraints constraintstxtEndDt =
					new java.awt.GridBagConstraints();
				constraintstxtEndDt.gridx = 4;
				constraintstxtEndDt.gridy = 1;
				constraintstxtEndDt.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtEndDt.weightx = 1.0;
				constraintstxtEndDt.ipadx = 96;
				constraintstxtEndDt.insets =
					new java.awt.Insets(21, 3, 19, 21);
				getpnlSelectDtForHistory().add(
					gettxtEndDt(),
					constraintstxtEndDt);
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
		return ivjpnlSelectDtForHistory;
	}

	/**
	 * Return the pnlSelectOne property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectOne()
	{
		if (ivjpnlSelectOne == null)
		{
			try
			{
				ivjpnlSelectOne = new JPanel();
				ivjpnlSelectOne.setName("pnlSelectOne");
				ivjpnlSelectOne.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_SELECT_ONE_COLON));
				ivjpnlSelectOne.setLayout(new java.awt.GridBagLayout());
				ivjpnlSelectOne.setMaximumSize(
					new java.awt.Dimension(421, 76));
				ivjpnlSelectOne.setMinimumSize(
					new java.awt.Dimension(421, 76));

				java.awt.GridBagConstraints constraintsradioMainOfc =
					new java.awt.GridBagConstraints();
				constraintsradioMainOfc.gridx = 1;
				constraintsradioMainOfc.gridy = 1;
				constraintsradioMainOfc.ipadx = 19;
				constraintsradioMainOfc.insets =
					new java.awt.Insets(15, 11, 13, 53);
				getpnlSelectOne().add(
					getradioMainOfc(),
					constraintsradioMainOfc);

				java.awt.GridBagConstraints constraintsradioSubsta =
					new java.awt.GridBagConstraints();
				constraintsradioSubsta.gridx = 2;
				constraintsradioSubsta.gridy = 1;
				constraintsradioSubsta.ipadx = 22;
				constraintsradioSubsta.insets =
					new java.awt.Insets(15, 54, 13, 75);
				getpnlSelectOne().add(
					getradioSubsta(),
					constraintsradioSubsta);
				// user code begin {1}
				// defect 7890
				// Changed from ButtonGroup to RTSButtonGroup
				RTSButtonGroup laRadioGrp = new RTSButtonGroup();
				laRadioGrp.add(getradioMainOfc());
				laRadioGrp.add(getradioSubsta());
				// end defect 7890
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectOne;
	}

	/**
	 * Return the pnlSelectOneOrBoth property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectOneOrBoth()
	{
		if (ivjpnlSelectOneOrBoth == null)
		{
			try
			{
				ivjpnlSelectOneOrBoth = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints2 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints1 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints3 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints2.insets =
					new java.awt.Insets(15, 11, 18, 2);
				consGridBagConstraints2.ipady = -2;
				consGridBagConstraints2.ipadx = 24;
				consGridBagConstraints2.gridy = 0;
				consGridBagConstraints2.gridx = 1;
				consGridBagConstraints3.insets =
					new java.awt.Insets(16, 3, 18, 24);
				consGridBagConstraints3.ipady = -3;
				consGridBagConstraints3.ipadx = 8;
				consGridBagConstraints3.gridy = 0;
				consGridBagConstraints3.gridx = 2;
				consGridBagConstraints1.insets =
					new java.awt.Insets(15, 13, 18, 11);
				consGridBagConstraints1.ipady = -2;
				consGridBagConstraints1.ipadx = 15;
				consGridBagConstraints1.gridy = 0;
				consGridBagConstraints1.gridx = 0;
				ivjpnlSelectOneOrBoth.setName("pnlSelectOneOrBoth");
				ivjpnlSelectOneOrBoth.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant
							.TXT_SELECT_ONE_OR_BOTH_COLON));
				ivjpnlSelectOneOrBoth.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlSelectOneOrBoth.add(
					getchkCurrBal(),
					consGridBagConstraints1);
				ivjpnlSelectOneOrBoth.add(
					getchkHistory(),
					consGridBagConstraints2);
				ivjpnlSelectOneOrBoth.add(
					getchkCurrVir(),
					consGridBagConstraints3);
				ivjpnlSelectOneOrBoth.setMaximumSize(
					new java.awt.Dimension(421, 80));
				ivjpnlSelectOneOrBoth.setMinimumSize(
					new java.awt.Dimension(421, 80));

				// check boxes.
				RTSButtonGroup laRTSButtonGrp = new RTSButtonGroup();
				laRTSButtonGrp.add(getchkCurrBal());
				laRTSButtonGrp.add(getchkHistory());
				// end defect 7890
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
		return ivjpnlSelectOneOrBoth;
	}

	/**
	 * Return the getpnlSelectSubstaOfc property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectSubstaOfc()
	{
		if (ivjpnlSelectSubstaOfc == null)
		{
			try
			{
				ivjpnlSelectSubstaOfc = new JPanel();
				ivjpnlSelectSubstaOfc.setName("pnlSelectSubstaOfc");
				ivjpnlSelectSubstaOfc.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_SELECT_SUBSTA_COLON));
				ivjpnlSelectSubstaOfc.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlSelectSubstaOfc.setMaximumSize(
					new java.awt.Dimension(421, 111));
				ivjpnlSelectSubstaOfc.setMinimumSize(
					new java.awt.Dimension(421, 111));

				java.awt.GridBagConstraints constraintscomboSubstaOfcs =
					new java.awt.GridBagConstraints();
				constraintscomboSubstaOfcs.gridx = 1;
				constraintscomboSubstaOfcs.gridy = 1;
				constraintscomboSubstaOfcs.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintscomboSubstaOfcs.weightx = 1.0;
				constraintscomboSubstaOfcs.ipadx = 262;
				constraintscomboSubstaOfcs.insets =
					new java.awt.Insets(27, 10, 35, 11);
				getpnlSelectSubstaOfc().add(
					getcomboSubstaOfcs(),
					constraintscomboSubstaOfcs);
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
		return ivjpnlSelectSubstaOfc;
	}

	/**
	 * Return the radioMainOffice property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioMainOfc()
	{
		if (ivjradioMainOfc == null)
		{
			try
			{
				ivjradioMainOfc = new JRadioButton();
				ivjradioMainOfc.setName("radioMainOfc");
				ivjradioMainOfc.setMnemonic(KeyEvent.VK_M);
				ivjradioMainOfc.setText(
					InventoryConstant.TXT_MAIN_OFFICE);
				ivjradioMainOfc.setMaximumSize(
					new java.awt.Dimension(89, 22));
				ivjradioMainOfc.setActionCommand(
					InventoryConstant.TXT_MAIN_OFFICE);
				ivjradioMainOfc.setSelected(true);
				ivjradioMainOfc.setMinimumSize(
					new java.awt.Dimension(89, 22));
				// user code begin {1}
				getradioMainOfc().addActionListener(this);
				// defect 9117 / 9085
				if (SystemProperty.isHQ())
				{
					getradioMainOfc().setEnabled(false);
					getradioMainOfc().setSelected(false);
				}
				// end defect 9117 / 9085
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioMainOfc;
	}

	/**
	 * Return the radioSubstation property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioSubsta()
	{
		if (ivjradioSubsta == null)
		{
			try
			{
				ivjradioSubsta = new JRadioButton();
				ivjradioSubsta.setName("radioSubsta");
				ivjradioSubsta.setMnemonic(KeyEvent.VK_S);
				ivjradioSubsta.setText(
					InventoryConstant.TXT_SUBSTATION);
				ivjradioSubsta.setMaximumSize(
					new java.awt.Dimension(86, 22));
				ivjradioSubsta.setActionCommand(
					InventoryConstant.TXT_SUBSTATION);
				ivjradioSubsta.setMinimumSize(
					new java.awt.Dimension(86, 22));
				// user code begin {1}
				getradioSubsta().addActionListener(this);
				// defect 9117 / 9085
				if (SystemProperty.isHQ())
				{
					getradioSubsta().setEnabled(false);
				}
				// end defect 9117 / 9085
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioSubsta;
	}

	/**
	 * Return the stcLblBeginDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBegDt()
	{
		if (ivjstcLblBegDt == null)
		{
			try
			{
				ivjstcLblBegDt = new JLabel();
				ivjstcLblBegDt.setName("stcLblBegDt");
				ivjstcLblBegDt.setText(
					InventoryConstant.TXT_BEGIN_DATE_COLON);
				ivjstcLblBegDt.setMaximumSize(
					new java.awt.Dimension(64, 14));
				ivjstcLblBegDt.setMinimumSize(
					new java.awt.Dimension(64, 14));
				ivjstcLblBegDt.setHorizontalAlignment(
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
		return ivjstcLblBegDt;
	}

	/**
	 * Return the stcLblEndDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEndDt()
	{
		if (ivjstcLblEndDt == null)
		{
			try
			{
				ivjstcLblEndDt = new JLabel();
				ivjstcLblEndDt.setName("stcLblEndDt");
				ivjstcLblEndDt.setText(
					InventoryConstant.TXT_END_DATE_COLON);
				ivjstcLblEndDt.setMaximumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblEndDt.setMinimumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblEndDt.setHorizontalAlignment(
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
		return ivjstcLblEndDt;
	}

	/**
	 * Return the txtBeginDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtBegDt()
	{
		if (ivjtxtBegDt == null)
		{
			try
			{
				ivjtxtBegDt = new RTSDateField();
				ivjtxtBegDt.setName("txtBegDt");
				ivjtxtBegDt.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtBegDt.setEnabled(false);
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
		return ivjtxtBegDt;
	}

	/**
	 * Return the txtEndDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtEndDt()
	{
		if (ivjtxtEndDt == null)
		{
			try
			{
				ivjtxtEndDt = new RTSDateField();
				ivjtxtEndDt.setName("txtEndDt");
				ivjtxtEndDt.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEndDt.setEnabled(false);
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
		return ivjtxtEndDt;
	}

	/**
	 * Handle checkbox selection
	 * 
	 */
	private void handleChkboxSelection(JCheckBox aaJChkBx)
	{
		if (aaJChkBx.equals(getchkCurrVir()))
		{
			getchkCurrVir().setSelected(true);
		}
		else if (getchkHistory().isSelected())
		{
			gettxtBegDt().setEnabled(true);
			gettxtEndDt().setEnabled(true);
		}
		else
		{
			gettxtBegDt().setEnabled(false);
			gettxtEndDt().setEnabled(false);
			gettxtBegDt().setDate(null);
			gettxtEndDt().setDate(null);
		}
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7890 
		///* Uncomment the following lines to print uncaught exceptions
		// *  to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7890
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
			setName(ScreenConstant.INV021_FRAME_NAME);
			setSize(450, 380);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV021_FRAME_TITLE);
			setContentPane(
				getFrmInventoryInquiryCriteriaINV021ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		Vector lvDataIn = (Vector) aaData;

		cvSubstaData =
			(Vector) lvDataIn.elementAt(CommonConstant.ELEMENT_0);

		// defect 8597
		if (cvSubstaData.size() == 1)
		{
			getradioSubsta().setEnabled(false);
		}
		// end defect 8597	

		caInvInqUIData = (InventoryInquiryUIData) lvDataIn.elementAt(1);

		// defect 9117 , 9085 
		if (!SystemProperty.isHQ())
		{
			if (SystemProperty.getSubStationId() == 0)
			{
				getradioMainOfc().setSelected(true);
			}
			else
			{
				getradioSubsta().setSelected(true);
			}
			getchkCurrVir().setEnabled(false);
			setpnlSelectSubstaOfc();
		}
		else
		{
			getchkCurrVir().setEnabled(true);
			getchkCurrVir().setSelected(true);
			getchkCurrBal().setEnabled(false);
			getchkHistory().setEnabled(false);
			getstcLblBegDt().setEnabled(false);
			getstcLblEndDt().setEnabled(false);
		}
		// end defect 9117, 9085
	}

	/**
	 * Setup Panel to Select Substa/Office.
	 */
	private void setpnlSelectSubstaOfc()
	{
		if (getradioMainOfc().isSelected())
		{
			getcomboSubstaOfcs().removeAllItems();
			getcomboSubstaOfcs().setEnabled(false);
		}
		else
		{
			String lsSubstaName = new String();
			for (int i = 0; i < cvSubstaData.size(); i++)
			{
				SubstationData lSubstaData =
					(SubstationData) cvSubstaData.get(i);
				if (lSubstaData.getSubstaId() != 0)
				{
					String lsStr =
						lSubstaData.getSubstaId()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ lSubstaData.getSubstaName();
					getcomboSubstaOfcs().addItem(lsStr);
					if (lSubstaData.getSubstaId()
						== SystemProperty.getSubStationId())
					{
						lsSubstaName = lsStr;
					}
				}
			}
			getcomboSubstaOfcs().setSelectedItem(lsSubstaName);
			// defect 8479
			comboBoxHotKeyFix(getcomboSubstaOfcs());
			// end defect 8479

			if (SystemProperty.getSubStationId() != 0)
			{
				getradioMainOfc().setEnabled(false);
				getradioSubsta().setEnabled(false);
				getcomboSubstaOfcs().setEnabled(false);
			}
			else
			{
				getcomboSubstaOfcs().setEnabled(true);
			}
		}
	}

	/**
	 * Validate the check boxes, dates, and combo box on the screen.
	 * 
	 * @return boolean
	 */
	private boolean validateFrame()
	{
		RTSException leRTSEx = new RTSException();

		// Verify that at least one check box is checked.
		if (!getchkCurrBal().isSelected()
			&& !getchkHistory().isSelected()
			&& !getchkCurrVir().isSelected())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_NO_CHKBOXES_SELECTED),
				getchkCurrBal());
		}

		// Validate the begin and end dates if the history check box 
		// is selected.
		if (getchkHistory().isSelected())
		{
			RTSDate laCurrentDt = new RTSDate();

			// Validate the date format.
			if (gettxtBegDt().isValidDate())
			{
				// Validate that the begin date is between now 
				// and 11 days ago.
				if (laCurrentDt.compareTo(gettxtBegDt().getDate()) < 0
					|| (laCurrentDt.add(RTSDate.DATE, -11)).compareTo(
						gettxtBegDt().getDate())
						> 0)
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_INV_INQUIRY_DATE_INCORRECT),
						gettxtBegDt());
				}

				// Validate the date format.
				if (gettxtEndDt().isValidDate())
				{
					// Validate that the end date is between now 
					// and the begin date.
					if (laCurrentDt.compareTo(gettxtEndDt().getDate())
						< 0
						|| (gettxtBegDt().getDate()).compareTo(
							gettxtEndDt().getDate())
							> 0)
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_INV_INQUIRY_DATE_INCORRECT),
							gettxtEndDt());
					}
				}
				else
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtEndDt());
				}
			}
			else
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtBegDt());
			}
		}

		// If subcontractor is selected, verify that something is
		// selected in the combo box.
		if (getradioSubsta().isSelected()
			&& getcomboSubstaOfcs().getSelectedItem() == null)
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_NO_ITEMS_SELECTED),
				getcomboSubstaOfcs());
		}

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			return false;
		}
		return true;
	}
}
