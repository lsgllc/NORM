package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.InventoryInquiryUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmInventoryInquirySelectionINV020.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		07/02/2002	Added focusGained() and focusLost() to put
 *							focus on selected radio button. 
 *							defect 4344
 * Min Wang		07/22/2002	Removed addFocusListener().
 * 							defect 4481
 * K. Harrell   08/07/2002  Assigned InvIds for Central
 * 							defect 4584
 * Min Wang     08/08/2002  Modified to allow mouse selection of Central
 * 							defect 4580
 * Ray Rowehl	02/21/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/25/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	04/04/2005	Change help pointer
 * 							modify actionPerformed()
 * 							defect 6964 Ver 5.2.3
 * Ray Rowehl	08/10/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove key processing from button panel.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Remove select from key processing.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3
 * Jeff S.		12/29/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), keyReleased(), 
 * 								cbKeyPressed, carrRadioButton,
 * 								ciSelctdRadioButton
 * 							modify getpnlSelectOne(), getradioCntrl(), 
 * 								getradioDlr(), getradioEmp(), 
 * 								getradioItmTypesAndYr(), 
 * 								getradioSpecificItmNoAndYr(), 
 * 								getradioWs(), initialize(), 
 * 								actionPerformed()
 * 							defect 7890 Ver 5.2.3 
 * Min Wang		03/28/2007	Disable some radio buttons for 
 * 							inquiry virtual inventroy
 * 							modify getradioDlr(), getradioSubcon()
 * 							defect 9117 Ver Special Plates
 * Min Wang		03/29/2007  when user select Employee, Workstation, and 
 * 							Central for VI report, it will refer to 
 * 							Item Type(s) and Year.
 * 							modify captureUserInput()
 * 							defect 9117 Ver Special Plates
 * K Harrell	07/08/2007	Disable all but "Item Types and Year" for HQ
 * 							Use SystemProperty.isHQ()
 * 							add disableWhereNotApplicable()
 * 							delete getBuilderData()
 * 							modify setData(),getradioSubcon(),
 * 							  getradioDlr()
 * 							defect 9085 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * In the Inquiry event, frame INV020 is the entry point where the
 * entity type, for which the report should be run, is selected.
 *
 * @version	Special Plates	07/08/2007
 * @author	Charlie Walker
 * <br>Creation Date:		06/28/2001 09:48:16
 */

public class FrmInventoryInquirySelectionINV020
	extends RTSDialogBox
	implements ActionListener 
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjpnlSelectOne = null;
	private JPanel ivjFrmInventoryInquirySelectionINV020ContentPane1 =
		null;
	private JRadioButton ivjradioCntrl = null;
	private JRadioButton ivjradioDlr = null;
	private JRadioButton ivjradioEmp = null;
	private JRadioButton ivjradioItmTypesAndYr = null;
	private JRadioButton ivjradioSpecificItmNoAndYr = null;
	private JRadioButton ivjradioSubcon = null;
	private JRadioButton ivjradioWs = null;

	/**
	 * InventoryInquiryUIData object used to collect the UI data
	 */
	private InventoryInquiryUIData caInvInqUIData =
		new InventoryInquiryUIData();

	/**
	 * FrmInventoryInquirySelectionINV020 constructor comment.
	 */
	public FrmInventoryInquirySelectionINV020()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryInquirySelectionINV020 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryInquirySelectionINV020(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryInquirySelectionINV020 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryInquirySelectionINV020(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Used to determine what action to take when an action is 
	 * performed on the screen.
	 *
	 * @param aaAE ActionEvent  
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		// Code to prevent multiple button clicks
		if (!startWorking())
		{
			return;
		}
		try
		{
			// Determines what actions to take when Enter, Cancel, or
			// Help are pressed.
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Store the user input.
				captureUserInput();

				getController().processData(
					AbstractViewController.ENTER,
					caInvInqUIData);
			}

			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caInvInqUIData);
			}

			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				// defect 6964
				RTSHelp.displayHelp(RTSHelp.INV020);
				// end defect 6964
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

		caInvInqUIData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		caInvInqUIData.setSubstaId(SystemProperty.getSubStationId());

		if (getradioEmp().isSelected())
		{
			caInvInqUIData.setInvLocIdCd(InventoryConstant.CHAR_E);
			caInvInqUIData.setInvInqBy(InventoryConstant.EMP);
		}
		else if (getradioWs().isSelected())
		{
			caInvInqUIData.setInvLocIdCd(InventoryConstant.CHAR_W);
			caInvInqUIData.setInvInqBy(InventoryConstant.WS);
		}
		else if (getradioDlr().isSelected())
		{
			caInvInqUIData.setInvLocIdCd(InventoryConstant.CHAR_D);
			caInvInqUIData.setInvInqBy(InventoryConstant.DLR);
		}
		else if (getradioSubcon().isSelected())
		{
			caInvInqUIData.setInvLocIdCd(InventoryConstant.CHAR_S);
			caInvInqUIData.setInvInqBy(InventoryConstant.SUBCON);
		}
		else if (getradioItmTypesAndYr().isSelected())
		{
			caInvInqUIData.setInvLocIdCd(
				CommonConstant.STR_SPACE_EMPTY);
			caInvInqUIData.setInvInqBy(InventoryConstant.ITM_TYPES);
		}
		else if (getradioSpecificItmNoAndYr().isSelected())
		{
			caInvInqUIData.setInvLocIdCd(
				CommonConstant.STR_SPACE_EMPTY);
			caInvInqUIData.setInvInqBy(InventoryConstant.SPECIFIC_ITM);
		}
		// defect 9085
		// Removed
		// defect 9117
		//		else if (
		//			SystemProperty.getOfficeIssuanceCd() == 1
		//				&& (getradioCntrl().isSelected()
		//					|| getradioEmp().isSelected()
		//					|| getradioWs().isSelected()))
		//		{
		//			caInvInqUIData.setInvLocIdCd(
		//				CommonConstant.STR_SPACE_EMPTY);
		//			caInvInqUIData.setInvInqBy(InventoryConstant.ITM_TYPES);
		//		}
		// end defect 9117
		// end defect 9085
		else if (getradioCntrl().isSelected())
		{
			caInvInqUIData.setInvLocIdCd(InventoryConstant.CHAR_C);
			caInvInqUIData.setInvInqBy(InventoryConstant.CNTRL);
			Vector lvInvIds = new Vector();
			lvInvIds.addElement(CommonConstant.STR_ZERO);
			caInvInqUIData.setInvIds(lvInvIds);
		}
	}

	/**
	 * 
	 * Disable Buttons Where Not Applicable
	 * 
	 * Note:  The call to this method - and this method can 
	 * be removed when ready to implement HQ. 
	 */
	private void disableWhereNotApplicable()
	{
		if (SystemProperty.isHQ())
		{
			getradioEmp().setEnabled(false);
			getradioSpecificItmNoAndYr().setEnabled(false);
			getradioCntrl().setEnabled(false);
			getradioWs().setEnabled(false);
			getradioItmTypesAndYr().setSelected(true);
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
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the FrmInventoryInquirySelectionINV020ContentPane1
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmInventoryInquirySelectionINV020ContentPane1()
	{
		if (ivjFrmInventoryInquirySelectionINV020ContentPane1 == null)
		{
			try
			{
				ivjFrmInventoryInquirySelectionINV020ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmInventoryInquirySelectionINV020ContentPane1
					.setName(
					"FrmInventoryInquirySelectionINV020ContentPane1");
				ivjFrmInventoryInquirySelectionINV020ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmInventoryInquirySelectionINV020ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(332, 341));
				ivjFrmInventoryInquirySelectionINV020ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(332, 341));
				ivjFrmInventoryInquirySelectionINV020ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);

				java.awt.GridBagConstraints constraintspnlSelectOne =
					new java.awt.GridBagConstraints();
				constraintspnlSelectOne.gridx = 1;
				constraintspnlSelectOne.gridy = 1;
				constraintspnlSelectOne.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectOne.weightx = 1.0;
				constraintspnlSelectOne.weighty = 1.0;
				constraintspnlSelectOne.insets =
					new java.awt.Insets(17, 33, 4, 35);
				getFrmInventoryInquirySelectionINV020ContentPane1()
					.add(
					getpnlSelectOne(),
					constraintspnlSelectOne);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 2;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 35;
				constraintsButtonPanel1.ipady = 24;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(4, 33, 7, 35);
				getFrmInventoryInquirySelectionINV020ContentPane1()
					.add(
					getButtonPanel1(),
					constraintsButtonPanel1);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjFrmInventoryInquirySelectionINV020ContentPane1;
	}

	/**
	 * Return the pnlSelectOne property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
					new java.awt.Dimension(264, 250));
				ivjpnlSelectOne.setMinimumSize(
					new java.awt.Dimension(264, 250));

				java.awt.GridBagConstraints constraintsradioEmp =
					new java.awt.GridBagConstraints();
				constraintsradioEmp.gridx = 1;
				constraintsradioEmp.gridy = 1;
				constraintsradioEmp.ipadx = 121;
				constraintsradioEmp.insets =
					new java.awt.Insets(8, 23, 4, 28);
				getpnlSelectOne().add(
					getradioEmp(),
					constraintsradioEmp);

				java.awt.GridBagConstraints constraintsradioWs =
					new java.awt.GridBagConstraints();
				constraintsradioWs.gridx = 1;
				constraintsradioWs.gridy = 2;
				constraintsradioWs.ipadx = 106;
				constraintsradioWs.insets =
					new java.awt.Insets(4, 23, 4, 28);
				getpnlSelectOne().add(getradioWs(), constraintsradioWs);

				java.awt.GridBagConstraints constraintsradioDlr =
					new java.awt.GridBagConstraints();
				constraintsradioDlr.gridx = 1;
				constraintsradioDlr.gridy = 3;
				constraintsradioDlr.ipadx = 139;
				constraintsradioDlr.insets =
					new java.awt.Insets(4, 23, 4, 28);
				getpnlSelectOne().add(
					getradioDlr(),
					constraintsradioDlr);

				java.awt.GridBagConstraints constraintsradioSubcon =
					new java.awt.GridBagConstraints();
				constraintsradioSubcon.gridx = 1;
				constraintsradioSubcon.gridy = 4;
				constraintsradioSubcon.ipadx = 94;
				constraintsradioSubcon.insets =
					new java.awt.Insets(4, 23, 4, 28);
				getpnlSelectOne().add(
					getradioSubcon(),
					constraintsradioSubcon);

				java
					.awt
					.GridBagConstraints constraintsradioItmTypesAndYr =
					new java.awt.GridBagConstraints();
				constraintsradioItmTypesAndYr.gridx = 1;
				constraintsradioItmTypesAndYr.gridy = 5;
				constraintsradioItmTypesAndYr.ipadx = 53;
				constraintsradioItmTypesAndYr.insets =
					new java.awt.Insets(4, 23, 4, 28);
				getpnlSelectOne().add(
					getradioItmTypesAndYr(),
					constraintsradioItmTypesAndYr);

				java
					.awt
					.GridBagConstraints constraintsradioSpecificItmNoAndYr =
					new java.awt.GridBagConstraints();
				constraintsradioSpecificItmNoAndYr.gridx = 1;
				constraintsradioSpecificItmNoAndYr.gridy = 6;
				constraintsradioSpecificItmNoAndYr.ipadx = 1;
				constraintsradioSpecificItmNoAndYr.insets =
					new java.awt.Insets(4, 23, 4, 28);
				getpnlSelectOne().add(
					getradioSpecificItmNoAndYr(),
					constraintsradioSpecificItmNoAndYr);

				java.awt.GridBagConstraints constraintsradioCntrl =
					new java.awt.GridBagConstraints();
				constraintsradioCntrl.gridx = 1;
				constraintsradioCntrl.gridy = 7;
				constraintsradioCntrl.ipadx = 135;
				constraintsradioCntrl.insets =
					new java.awt.Insets(4, 23, 14, 28);
				getpnlSelectOne().add(
					getradioCntrl(),
					constraintsradioCntrl);
				// user code begin {1}
				// defect 7890
				RTSButtonGroup laRadioGrp = new RTSButtonGroup();
				laRadioGrp.add(getradioEmp());
				laRadioGrp.add(getradioWs());
				laRadioGrp.add(getradioDlr());
				laRadioGrp.add(getradioSubcon());
				laRadioGrp.add(getradioItmTypesAndYr());
				laRadioGrp.add(getradioSpecificItmNoAndYr());
				laRadioGrp.add(getradioCntrl());
				// end defect 7890
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjpnlSelectOne;
	}

	/**
	 * Return the radioCentral property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioCntrl()
	{
		if (ivjradioCntrl == null)
		{
			try
			{
				ivjradioCntrl = new JRadioButton();
				ivjradioCntrl.setName("radioCntrl");
				ivjradioCntrl.setMnemonic(java.awt.event.KeyEvent.VK_C);
				ivjradioCntrl.setText(InventoryConstant.TXT_CENTRAL);
				ivjradioCntrl.setMaximumSize(
					new java.awt.Dimension(66, 22));
				ivjradioCntrl.setActionCommand(
					InventoryConstant.TXT_CENTRAL);
				ivjradioCntrl.setMinimumSize(
					new java.awt.Dimension(66, 22));
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioCntrl;
	}

	/**
	 * Return the radioDealer property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioDlr()
	{
		if (ivjradioDlr == null)
		{
			try
			{
				ivjradioDlr = new JRadioButton();
				ivjradioDlr.setName("radioDlr");
				ivjradioDlr.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjradioDlr.setText(InventoryConstant.TXT_DEALER);
				ivjradioDlr.setMaximumSize(
					new java.awt.Dimension(62, 22));
				ivjradioDlr.setActionCommand(
					InventoryConstant.TXT_DEALER);
				ivjradioDlr.setMinimumSize(
					new java.awt.Dimension(62, 22));
				// user code begin {1}
				// end defect 7890
				// defect 9117/9085
				if (SystemProperty.isHQ())
				{
					ivjradioDlr.setEnabled(false);
				}
				// end defect 9117 / 9085
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioDlr;
	}

	/**
	 * Return the radioEmployee property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioEmp()
	{
		if (ivjradioEmp == null)
		{
			try
			{
				ivjradioEmp = new JRadioButton();
				ivjradioEmp.setName("radioEmp");
				ivjradioEmp.setMnemonic(java.awt.event.KeyEvent.VK_M);
				ivjradioEmp.setText(InventoryConstant.TXT_EMPLOYEE);
				ivjradioEmp.setMaximumSize(
					new java.awt.Dimension(80, 22));
				ivjradioEmp.setActionCommand(
					InventoryConstant.TXT_EMPLOYEE);
				ivjradioEmp.setSelected(true);
				ivjradioEmp.setMinimumSize(
					new java.awt.Dimension(80, 22));
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioEmp;
	}

	/**
	 * Return the radioItemTypesAndYear property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioItmTypesAndYr()
	{
		if (ivjradioItmTypesAndYr == null)
		{
			try
			{
				ivjradioItmTypesAndYr = new JRadioButton();
				ivjradioItmTypesAndYr.setName("radioItmTypesAndYr");
				ivjradioItmTypesAndYr.setMnemonic(
					java.awt.event.KeyEvent.VK_T);
				ivjradioItmTypesAndYr.setText(
					InventoryConstant.TXT_ITEM_TYPES_AND_YEAR);
				ivjradioItmTypesAndYr.setMaximumSize(
					new java.awt.Dimension(148, 22));
				ivjradioItmTypesAndYr.setActionCommand(
					InventoryConstant.TXT_ITEM_TYPES_AND_YEAR);
				ivjradioItmTypesAndYr.setMinimumSize(
					new java.awt.Dimension(148, 22));
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioItmTypesAndYr;
	}

	/**
	 * Return the radioSpecificItemNumberAndYear property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioSpecificItmNoAndYr()
	{
		if (ivjradioSpecificItmNoAndYr == null)
		{
			try
			{
				ivjradioSpecificItmNoAndYr =
					new javax.swing.JRadioButton();
				ivjradioSpecificItmNoAndYr.setName(
					"radioSpecificItmNoAndYr");
				ivjradioSpecificItmNoAndYr.setMnemonic(
					java.awt.event.KeyEvent.VK_I);
				ivjradioSpecificItmNoAndYr.setText(
					InventoryConstant.TXT_SPECIFIC_ITEM_NO_AND_YEAR);
				ivjradioSpecificItmNoAndYr.setMaximumSize(
					new java.awt.Dimension(200, 22));
				ivjradioSpecificItmNoAndYr.setActionCommand(
					InventoryConstant.TXT_SPECIFIC_ITEM_NO_AND_YEAR);
				ivjradioSpecificItmNoAndYr.setMinimumSize(
					new java.awt.Dimension(200, 22));
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioSpecificItmNoAndYr;
	}

	/**
	 * Return the radioSubcontractor property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioSubcon()
	{
		if (ivjradioSubcon == null)
		{
			try
			{
				ivjradioSubcon = new JRadioButton();
				ivjradioSubcon.setName("radioSubcon");
				ivjradioSubcon.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjradioSubcon.setText(
					InventoryConstant.TXT_SUBCONTRACTOR);
				ivjradioSubcon.setMaximumSize(
					new java.awt.Dimension(107, 22));
				ivjradioSubcon.setActionCommand(
					InventoryConstant.TXT_SUBCONTRACTOR);
				ivjradioSubcon.setMinimumSize(
					new java.awt.Dimension(107, 22));
				// user code begin {1}
				// defect 9117 / 9085
				if (SystemProperty.isHQ())
				{
					ivjradioSubcon.setEnabled(false);
				}
				// end defect 9117 / 9085
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioSubcon;
	}

	/**
	 * Return the radioWorkstation property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioWs()
	{
		if (ivjradioWs == null)
		{
			try
			{
				ivjradioWs = new JRadioButton();
				ivjradioWs.setName("radioWs");
				ivjradioWs.setMnemonic(java.awt.event.KeyEvent.VK_W);
				ivjradioWs.setText(InventoryConstant.TXT_WORKSTATION);
				ivjradioWs.setMaximumSize(
					new java.awt.Dimension(95, 22));
				ivjradioWs.setActionCommand(
					InventoryConstant.TXT_WORKSTATION);
				ivjradioWs.setMinimumSize(
					new java.awt.Dimension(95, 22));
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjradioWs;
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
		// * to stdout */
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName(ScreenConstant.INV020_FRAME_NAME);
			setSize(320, 315);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV020_FRAME_TITLE);
			setContentPane(
				getFrmInventoryInquirySelectionINV020ContentPane1());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmInventoryInquirySelectionINV020 laFrmInventoryInquirySelectionINV020;
			laFrmInventoryInquirySelectionINV020 =
				new FrmInventoryInquirySelectionINV020();
			laFrmInventoryInquirySelectionINV020.setModal(true);
			laFrmInventoryInquirySelectionINV020
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryInquirySelectionINV020.show();
			java.awt.Insets insets =
				laFrmInventoryInquirySelectionINV020.getInsets();
			laFrmInventoryInquirySelectionINV020.setSize(
				laFrmInventoryInquirySelectionINV020.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryInquirySelectionINV020.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryInquirySelectionINV020.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
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
		caInvInqUIData = (InventoryInquiryUIData) aaData;
		// defect 9085 
		disableWhereNotApplicable();
		// end defect 9085 

	}
}