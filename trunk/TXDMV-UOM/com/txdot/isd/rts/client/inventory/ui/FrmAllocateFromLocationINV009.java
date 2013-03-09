package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmAllocateFromLocationINV009.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		05/08/2002	lengthen subcon combo field.
 *							defect 3801
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source, 
 * 							rename fields
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/25/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	06/17/2005	Cleanup Button Panel processing
 * 							modify getButtonPanel1(), keyPressed()
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3
 * Ray Rowehl	06/27/2005	Work on Constants.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/08/2005	Cleanup pass
 * 							Work on constants.
 * 							Add white space around methods.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants 
 * 							classes
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	08/17/2005	Remove selection done under key processing.
 * 							defect 7890 Ver 5.2.3 
 * Ray Rowehl	10/05/2005	Work on mnemoics
 * 							defect 7890 Ver 5.2.3
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setcomboSelectFromEntityId()
 * 							defect 8479 Ver 5.2.3 
 * Jeff S.		12/20/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), ciSelctdRadioButton,
 * 								NUMBER_RADIO_BUTTONS, carrRadioButton
 * 							modify getpnlSelectFromEntity(), 
 * 								getradioDlr(), getradioEmp(), 
 * 								getradioServer(), getradioSubcon(),
 * 								getradioWs(), initialize()
 * 							defect 7890 Ver 5.2.3 
 * Min Wang		04/14/2008	Resize the component by visual editor.
 * 							defect 9493 Ver Defect_POS_A
 * K Harrell	06/25/2009	Refactored DealersData to DealerData
 * 							Additional cleanup. 
 * 							modify setcomboListData()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	02/18/2010	Implement new SubcontractorData
 * 							modify setcomboListData() 
 * 							defect 10161 Ver POS_640
 * ---------------------------------------------------------------------
 */

/**
 * This class is for screen INV009 From Location for Allocate.
 *
 * @version	POS_640			02/18/2010
 * @author	Charlie Walker
 * <br>Creation Date:		06/27/2001 10:18:07
 */

public class FrmAllocateFromLocationINV009
	extends RTSDialogBox
	implements ActionListener 
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JRadioButton ivjradioServer = null;
	private JPanel ivjFrmAllocateFromLocationINV009ContentPane1 = null;
	private JComboBox ivjcomboSelectFromEntityId = null;
	private JLabel ivjlblSelectFromLoc = null;
	private JPanel ivjpnlSelectFromEntity = null;
	private JPanel ivjpnlSelectFromEntityId = null;
	private JPanel ivjpnlSelectFromLoc = null;
	private JRadioButton ivjradioDlr = null;
	private JRadioButton ivjradioEmp = null;
	private JRadioButton ivjradioSubcon = null;
	private JRadioButton ivjradioWs = null;

	/**
	 * InventoryAllocationUIData object used to collect the UI data
	 */
	private InventoryAllocationUIData caInvAlloctnUIData =
		new InventoryAllocationUIData();

	/**
	 * Data object used to store the Subcontractors, Workstations, 
	 * Dealers, and Employees for the combo box, and the Substations
	 */
	private AllocationDbData caAlloctnDbData = new AllocationDbData();

	/**
	 * Vector used to store the formatted Subcontractors for the combo 
	 * box
	 */
	private Vector cvSubconComboData = new Vector();

	/**
	 * Vector used to store the formatted Workstations for the combo box
	 */
	private Vector cvWsComboData = new Vector();

	/**
	 * Vector used to store the formatted Dealers for the combo box
	 */
	private Vector cvDlrComboData = new Vector();

	/**
	 * Vector used to store the formatted Employees for the combo box
	 */
	private Vector cvEmpComboData = new Vector();

	/**
	 * FrmAllocateFromLocationINV009 constructor comment.
	 */
	public FrmAllocateFromLocationINV009()
	{
		super();
		initialize();
	}

	/**
	 * FrmAllocateFromLocationINV009 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmAllocateFromLocationINV009(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmAllocateFromLocationINV009 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmAllocateFromLocationINV009(JFrame aaParent)
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
			// Returns all fields to their original color state
			clearAllColor(this);

			if (aaAE.getSource() instanceof JRadioButton)
			{
				setpnlSelectFromEntityId();
			}

			// Determines what actions to take when Enter, Cancel, or
			// Help are pressed.
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Handles the case when server is not selected and 
				// the combo box doesn't contain any values
				if (caInvAlloctnUIData.getFromInvLocIdCd()
					!= InventoryConstant.CHAR_C
					&& ((String) getcomboSelectFromEntityId()
						.getSelectedItem()
						== null
						|| (
							(String) getcomboSelectFromEntityId()
								.getSelectedItem())
								.equals(
							CommonConstant.STR_SPACE_EMPTY)))
				{
					RTSException leRTSEx = new RTSException();
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_ENTITY_INVALID),
						getcomboSelectFromEntityId());
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}

				// Store the user input.
				captureUserInput();

				Vector lvSendData = new Vector();
				lvSendData.addElement(caAlloctnDbData);
				lvSendData.addElement(caInvAlloctnUIData);

				getController().processData(
					AbstractViewController.ENTER,
					lvSendData);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caInvAlloctnUIData);
			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV009);
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
		if (caInvAlloctnUIData
			.getFromInvLocIdCd()
			.equals(InventoryConstant.CHAR_C))
		{
			caInvAlloctnUIData.setFromInvId(CommonConstant.STR_ZERO);
			caInvAlloctnUIData.setFromInvIdName(
				CommonConstant.STR_ZERO);

		}
		else if (
			caInvAlloctnUIData.getFromInvLocIdCd().equals(
				InventoryConstant.CHAR_W))
		{
			caInvAlloctnUIData.setFromInvId(
				(String) getcomboSelectFromEntityId()
					.getSelectedItem());
			caInvAlloctnUIData.setFromInvIdName(
				(String) getcomboSelectFromEntityId()
					.getSelectedItem());

		}
		else
		{
			String lsSubstring =
				new String(
					(String) getcomboSelectFromEntityId()
						.getSelectedItem());
			lsSubstring =
				lsSubstring.substring(
					0,
					lsSubstring.indexOf(CommonConstant.STR_DASH));
			lsSubstring = lsSubstring.trim();
			caInvAlloctnUIData.setFromInvId(lsSubstring);
			caInvAlloctnUIData.setFromInvIdName(
				(String) getcomboSelectFromEntityId()
					.getSelectedItem());
		}
		caInvAlloctnUIData.setFromLoc(getlblSelectFromLoc().getText());
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
				ivjButtonPanel1.setBounds(166, 235, 235, 56);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
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
	 * Return the ivjcomboSelectFromEntityId property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboSelectFromEntityId()
	{
		if (ivjcomboSelectFromEntityId == null)
		{
			try
			{
				ivjcomboSelectFromEntityId =
					new javax.swing.JComboBox();
				ivjcomboSelectFromEntityId.setBounds(10, 48, 371, 23);
				ivjcomboSelectFromEntityId.setName(
					"ivjcomboSelectFromEntityId");
				ivjcomboSelectFromEntityId.setBackground(
					java.awt.Color.white);
				ivjcomboSelectFromEntityId.setVisible(false);
				ivjcomboSelectFromEntityId.setMaximumSize(
					new java.awt.Dimension(50, 23));
				ivjcomboSelectFromEntityId.setPreferredSize(
					new java.awt.Dimension(50, 23));
				ivjcomboSelectFromEntityId.setMinimumSize(
					new java.awt.Dimension(10, 23));
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
		return ivjcomboSelectFromEntityId;
	}

	/**
	 * Return the FrmAllocateFromLocationINV009ContentPane1 property 
	 * value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmAllocateFromLocationINV009ContentPane1()
	{
		if (ivjFrmAllocateFromLocationINV009ContentPane1 == null)
		{
			try
			{
				ivjFrmAllocateFromLocationINV009ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmAllocateFromLocationINV009ContentPane1.setName(
					"ivjFrmAllocateFromLocationINV009ContentPane1");
				ivjFrmAllocateFromLocationINV009ContentPane1.setLayout(
					null);
				ivjFrmAllocateFromLocationINV009ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmAllocateFromLocationINV009ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmAllocateFromLocationINV009ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				ivjFrmAllocateFromLocationINV009ContentPane1.add(
					getpnlSelectFromEntity(),
					null);
				ivjFrmAllocateFromLocationINV009ContentPane1.add(
					getpnlSelectFromLoc(),
					null);
				ivjFrmAllocateFromLocationINV009ContentPane1.add(
					getpnlSelectFromEntityId(),
					null);
				ivjFrmAllocateFromLocationINV009ContentPane1.add(
					getButtonPanel1(),
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
		return ivjFrmAllocateFromLocationINV009ContentPane1;
	}

	/**
	 * Return the ivjlblSelectFromLoc property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblSelectFromLoc()
	{
		if (ivjlblSelectFromLoc == null)
		{
			try
			{
				ivjlblSelectFromLoc = new javax.swing.JLabel();
				ivjlblSelectFromLoc.setName("ivjlblSelectFromLoc");
				ivjlblSelectFromLoc.setText(
					InventoryConstant.TXT_FROM_LOCATION);
				ivjlblSelectFromLoc.setMaximumSize(
					new java.awt.Dimension(85, 14));
				ivjlblSelectFromLoc.setMinimumSize(
					new java.awt.Dimension(85, 14));
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
		return ivjlblSelectFromLoc;
	}

	/**
	 * Return the ivjpnlSelectFromEntity property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectFromEntity()
	{
		if (ivjpnlSelectFromEntity == null)
		{
			try
			{
				ivjpnlSelectFromEntity = new javax.swing.JPanel();
				ivjpnlSelectFromEntity.setName(
					"ivjpnlSelectFromEntity");
				ivjpnlSelectFromEntity.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant
							.TXT_SELECT_FROM_ENTITY_COLON));
				ivjpnlSelectFromEntity.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlSelectFromEntity.setMaximumSize(
					new java.awt.Dimension(142, 181));
				ivjpnlSelectFromEntity.setMinimumSize(
					new java.awt.Dimension(142, 181));

				java.awt.GridBagConstraints constraintsradioServer =
					new java.awt.GridBagConstraints();
				constraintsradioServer.gridx = 1;
				constraintsradioServer.gridy = 1;
				constraintsradioServer.ipadx = 45;
				constraintsradioServer.insets =
					new java.awt.Insets(18, 17, 4, 17);
				getpnlSelectFromEntity().add(
					getradioServer(),
					constraintsradioServer);

				java.awt.GridBagConstraints constraintsradioWs =
					new java.awt.GridBagConstraints();
				constraintsradioWs.gridx = 1;
				constraintsradioWs.gridy = 2;
				constraintsradioWs.ipadx = 13;
				constraintsradioWs.insets =
					new java.awt.Insets(4, 17, 4, 17);
				getpnlSelectFromEntity().add(
					getradioWs(),
					constraintsradioWs);

				java.awt.GridBagConstraints constraintsradioSubcon =
					new java.awt.GridBagConstraints();
				constraintsradioSubcon.gridx = 1;
				constraintsradioSubcon.gridy = 3;
				constraintsradioSubcon.ipadx = 1;
				constraintsradioSubcon.insets =
					new java.awt.Insets(4, 17, 4, 17);
				getpnlSelectFromEntity().add(
					getradioSubcon(),
					constraintsradioSubcon);

				java.awt.GridBagConstraints constraintsradioDlr =
					new java.awt.GridBagConstraints();
				constraintsradioDlr.gridx = 1;
				constraintsradioDlr.gridy = 4;
				constraintsradioDlr.ipadx = 46;
				constraintsradioDlr.insets =
					new java.awt.Insets(4, 17, 4, 17);
				getpnlSelectFromEntity().add(
					getradioDlr(),
					constraintsradioDlr);

				java.awt.GridBagConstraints constraintsradioEmp =
					new java.awt.GridBagConstraints();
				constraintsradioEmp.gridx = 1;
				constraintsradioEmp.gridy = 5;
				constraintsradioEmp.ipadx = 28;
				constraintsradioEmp.insets =
					new java.awt.Insets(4, 17, 21, 17);
				getpnlSelectFromEntity().add(
					getradioEmp(),
					constraintsradioEmp);
				// user code begin {1}
				ivjpnlSelectFromEntity.setBounds(18, 19, 129, 161);
				RTSButtonGroup laRadioGrp = new RTSButtonGroup();
				laRadioGrp.add(getradioServer());
				laRadioGrp.add(getradioWs());
				laRadioGrp.add(getradioSubcon());
				laRadioGrp.add(getradioDlr());
				laRadioGrp.add(getradioEmp());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectFromEntity;
	}

	/**
	 * Return the ivjpnlSelectFromEntityId property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectFromEntityId()
	{
		if (ivjpnlSelectFromEntityId == null)
		{
			try
			{
				ivjpnlSelectFromEntityId = new JPanel();
				ivjpnlSelectFromEntityId.setName(
					"ivjpnlSelectFromEntityId");
				ivjpnlSelectFromEntityId.setOpaque(true);
				ivjpnlSelectFromEntityId.setLayout(null);
				ivjpnlSelectFromEntityId.setVisible(true);
				ivjpnlSelectFromEntityId.setEnabled(true);

				ivjpnlSelectFromEntityId.add(
					getcomboSelectFromEntityId(),
					null);
				ivjpnlSelectFromEntityId.setBounds(159, 107, 390, 119);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectFromEntityId;
	}

	/**
	 * Return the ivjpnlSelectFromLoc property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlSelectFromLoc()
	{
		if (ivjpnlSelectFromLoc == null)
		{
			try
			{
				ivjpnlSelectFromLoc = new JPanel();
				ivjpnlSelectFromLoc.setName("ivjpnlSelectFromLoc");
				ivjpnlSelectFromLoc.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant
							.TXT_SELECT_FROM_LOCATION_COLON));
				ivjpnlSelectFromLoc.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlSelectFromLoc.setMaximumSize(
					new java.awt.Dimension(263, 52));
				ivjpnlSelectFromLoc.setMinimumSize(
					new java.awt.Dimension(263, 52));

				java.awt.GridBagConstraints constraintslblSelectFromLoc =
					new java.awt.GridBagConstraints();
				constraintslblSelectFromLoc.gridx = 1;
				constraintslblSelectFromLoc.gridy = 1;
				constraintslblSelectFromLoc.ipadx = 162;
				constraintslblSelectFromLoc.insets =
					new java.awt.Insets(19, 10, 19, 6);
				getpnlSelectFromLoc().add(
					getlblSelectFromLoc(),
					constraintslblSelectFromLoc);
				// user code begin {1}
				ivjpnlSelectFromLoc.setBounds(172, 22, 361, 62);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectFromLoc;
	}

	/**
	 * Return the ivjradioDlr property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioDlr()
	{
		if (ivjradioDlr == null)
		{
			try
			{
				ivjradioDlr = new JRadioButton();
				ivjradioDlr.setName("ivjradioDlr");
				ivjradioDlr.setMnemonic(KeyEvent.VK_D);
				ivjradioDlr.setText(InventoryConstant.STR_D);
				ivjradioDlr.setMaximumSize(
					new java.awt.Dimension(62, 22));
				ivjradioDlr.setActionCommand(InventoryConstant.STR_D);
				ivjradioDlr.setMinimumSize(
					new java.awt.Dimension(62, 22));
				// user code begin {1}
				ivjradioDlr.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioDlr;
	}

	/**
	 * Return the ivjradioEmp property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioEmp()
	{
		if (ivjradioEmp == null)
		{
			try
			{
				ivjradioEmp = new JRadioButton();
				ivjradioEmp.setName("ivjradioEmp");
				ivjradioEmp.setMnemonic(KeyEvent.VK_M);
				ivjradioEmp.setText(InventoryConstant.STR_E);
				ivjradioEmp.setMaximumSize(
					new java.awt.Dimension(80, 22));
				ivjradioEmp.setActionCommand(InventoryConstant.STR_E);
				ivjradioEmp.setMinimumSize(
					new java.awt.Dimension(80, 22));
				// user code begin {1}
				ivjradioEmp.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioEmp;
	}

	/**
	 * Return the ivjradioServer property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioServer()
	{
		if (ivjradioServer == null)
		{
			try
			{
				ivjradioServer = new JRadioButton();
				ivjradioServer.setName("ivjradioServer");
				ivjradioServer.setMnemonic(KeyEvent.VK_S);
				ivjradioServer.setText(InventoryConstant.STR_A);
				ivjradioServer.setMaximumSize(
					new java.awt.Dimension(63, 22));
				ivjradioServer.setActionCommand(
					InventoryConstant.STR_A);
				ivjradioServer.setSelected(true);
				ivjradioServer.setMinimumSize(
					new java.awt.Dimension(63, 22));
				// user code begin {1}
				ivjradioServer.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioServer;
	}

	/**
	 * Return the ivjradioSubcon property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioSubcon()
	{
		if (ivjradioSubcon == null)
		{
			try
			{
				ivjradioSubcon = new JRadioButton();
				ivjradioSubcon.setName("ivjradioSubcon");
				ivjradioSubcon.setMnemonic(KeyEvent.VK_B);
				ivjradioSubcon.setText(InventoryConstant.STR_S);
				ivjradioSubcon.setMaximumSize(
					new java.awt.Dimension(107, 22));
				ivjradioSubcon.setActionCommand(
					InventoryConstant.STR_S);
				ivjradioSubcon.setMinimumSize(
					new java.awt.Dimension(107, 22));
				// user code begin {1}
				ivjradioSubcon.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioSubcon;
	}

	/**
	 * Return the ivjradioWs property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioWs()
	{
		if (ivjradioWs == null)
		{
			try
			{
				ivjradioWs = new javax.swing.JRadioButton();
				ivjradioWs.setName("ivjradioWs");
				ivjradioWs.setMnemonic(KeyEvent.VK_W);
				ivjradioWs.setText(InventoryConstant.STR_W);
				ivjradioWs.setMaximumSize(
					new java.awt.Dimension(95, 22));
				ivjradioWs.setActionCommand(InventoryConstant.STR_W);
				ivjradioWs.setMinimumSize(
					new java.awt.Dimension(95, 22));
				// user code begin {1}
				ivjradioWs.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
			setName(ScreenConstant.INV009_FRAME_NAME);
			setSize(575, 330);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV009_FRAME_TITLE);
			setContentPane(
				getFrmAllocateFromLocationINV009ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}
	
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
			FrmAllocateFromLocationINV009 laFrmAllocateFromLocationINV009;
			laFrmAllocateFromLocationINV009 =
				new FrmAllocateFromLocationINV009();
			laFrmAllocateFromLocationINV009.setModal(true);
			laFrmAllocateFromLocationINV009
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmAllocateFromLocationINV009.show();
			java.awt.Insets insets =
				laFrmAllocateFromLocationINV009.getInsets();
			laFrmAllocateFromLocationINV009.setSize(
				laFrmAllocateFromLocationINV009.getWidth()
					+ insets.left
					+ insets.right,
				laFrmAllocateFromLocationINV009.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmAllocateFromLocationINV009.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Used to concatenate the required fields to create the list data
	 * for the combo box.
	 * 
	 * @param aaAlloctnDbData AllocationDbData
	 */
	private void setcomboListData(AllocationDbData aaAlloctnDbData)
	{
		// Concatenate subcontractor information
		Vector lvSubconData = aaAlloctnDbData.getSubconWrap();
		if (lvSubconData != null)
		{
			for (int i = 0; i < lvSubconData.size(); i++)
			{
				// defect 10161
				// Implement new SubcontractorData
				String lsSubcon =
					new String(
						((SubcontractorData) lvSubconData.get(i))
							.getId()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ ((SubcontractorData) lvSubconData.get(i))
								.getName1());
				// end defect 10161 
				
				cvSubconComboData.add(lsSubcon);
			}
		}
		else
		{
			cvSubconComboData.add(CommonConstant.STR_SPACE_EMPTY);
		}

		// Concatenate workstation information
		Vector lvWsData = aaAlloctnDbData.getWsWrap();
		if (lvWsData != null)
		{
			for (int i = 0; i < lvWsData.size(); i++)
			{
				int liWs =
					((AssignedWorkstationIdsData) lvWsData.get(i))
						.getWsId();
				cvWsComboData.add(String.valueOf(liWs));
			}
		}
		else
		{
			cvWsComboData.add(CommonConstant.STR_SPACE_EMPTY);
		}

		// Concatenate dealer information
		Vector lvDlrData = aaAlloctnDbData.getDlrWrap();
		if (lvDlrData != null)
		{
			for (int i = 0; i < lvDlrData.size(); i++)
			{
				// defect 10112 
				// Dealer Data 
				String lsDlr =
					new String(
						((DealerData) lvDlrData.get(i)).getId()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ ((DealerData) lvDlrData.get(i)).getName1());
				// end defect 10112 
				
				cvDlrComboData.add(lsDlr);
			}
		}
		else
		{
			cvDlrComboData.add(CommonConstant.STR_SPACE_EMPTY);
		}

		// Concatenate employee information
		Vector lvEmpData = aaAlloctnDbData.getSecrtyWrap();
		if (lvEmpData != null)
		{
			for (int i = 0; i < lvEmpData.size(); i++)
			{
				String lsEmp =
					new String(
						((SecurityData) lvEmpData.get(i)).getEmpId()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ CommonConstant.STR_SPACE_ONE
							+ ((SecurityData) lvEmpData.get(i))
								.getEmpLastName()
							+ CommonConstant.STR_COMMA
							+ CommonConstant.STR_SPACE_ONE
							+ ((SecurityData) lvEmpData.get(i))
								.getEmpFirstName());
				if (((SecurityData) lvEmpData.get(i)).getEmpMI()
					!= null)
				{
					lsEmp =
						lsEmp
							+ CommonConstant.STR_SPACE_ONE
							+ ((SecurityData) lvEmpData.get(i))
								.getEmpMI();
				}
				cvEmpComboData.add(lsEmp);
			}
		}
		else
		{
			cvDlrComboData.add(CommonConstant.STR_SPACE_EMPTY);
		}
	}

	/**
	 * Used to fill the Select FROM Entity Id combobox with the 
	 * appropriate info depending on which radio button is selected.
	 */
	private void setcomboSelectFromEntityId()
	{
		if (getradioServer().isSelected())
		{
			getcomboSelectFromEntityId().removeAllItems();
		}
		else if (getradioWs().isSelected())
		{
			getcomboSelectFromEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvWsComboData));
			getcomboSelectFromEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboSelectFromEntityId());
			// end defect 8479
		}
		else if (getradioSubcon().isSelected())
		{
			getcomboSelectFromEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvSubconComboData));
			getcomboSelectFromEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboSelectFromEntityId());
			// end defect 8479
		}
		else if (getradioDlr().isSelected())
		{
			getcomboSelectFromEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvDlrComboData));
			getcomboSelectFromEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboSelectFromEntityId());
			// end defect 8479
		}
		else if (getradioEmp().isSelected())
		{
			getcomboSelectFromEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvEmpComboData));
			getcomboSelectFromEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboSelectFromEntityId());
			// end defect 8479
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 *
	 * @param aaData Object Vector with InventoryAllocateUIData
	 */
	public void setData(Object aaData)
	{
		if (aaData == null)
		{
			return;
		}
		Vector lvDataIn = (Vector) aaData;

		caAlloctnDbData =
			(AllocationDbData) lvDataIn.elementAt(
				CommonConstant.ELEMENT_0);
		caInvAlloctnUIData =
			(InventoryAllocationUIData) lvDataIn.elementAt(
				CommonConstant.ELEMENT_1);

		setlblSelectFromLoc(caAlloctnDbData.getSubstaWrap());
		getradioServer().requestFocus();
		if (caInvAlloctnUIData.getFromInvLocIdCd() == null)
		{
			caInvAlloctnUIData.setFromInvLocIdCd(
				InventoryConstant.CHAR_C);
		}
		setcomboListData(caAlloctnDbData);
	}

	/**
	 * Used to set the display of the current substation location.
	 *
	 * @param avSubstaData Vector
	 */
	private void setlblSelectFromLoc(Vector avSubstaData)
	{
		SubstationData laSubstaData = new SubstationData();

		for (int i = 0; i < avSubstaData.size(); i++)
		{
			laSubstaData = (SubstationData) avSubstaData.get(i);
			if (caInvAlloctnUIData.getSubstaId()
				== laSubstaData.getSubstaId())
			{
				getlblSelectFromLoc().setText(
					laSubstaData.getSubstaName());
				break;
			}
		}
	}

	/**
	 * Depending on which radio button is selected, this method sets 
	 * the border of of the Select FROM Entity Id panel and calls 
	 * setcomboSelectFromEntityId().
	 */
	private void setpnlSelectFromEntityId()
	{
		if (getradioServer().isSelected())
		{
			ivjpnlSelectFromEntityId.setBorder(null);
			ivjcomboSelectFromEntityId.setVisible(false);
			setcomboSelectFromEntityId();
			caInvAlloctnUIData.setFromInvLocIdCd(
				InventoryConstant.CHAR_C);
		}
		else if (getradioWs().isSelected())
		{
			getpnlSelectFromEntityId().setBorder(
				new TitledBorder(
					new EtchedBorder(),
					InventoryConstant.TXT_SELECT_FROM
						+ ivjradioWs.getActionCommand()
						+ CommonConstant.STR_COLON));
			getcomboSelectFromEntityId().setVisible(true);
			setcomboSelectFromEntityId();
			caInvAlloctnUIData.setFromInvLocIdCd(
				InventoryConstant.CHAR_W);
		}
		else if (getradioSubcon().isSelected())
		{
			getcomboSelectFromEntityId().setVisible(true);
			getpnlSelectFromEntityId().setBorder(
				new TitledBorder(
					new EtchedBorder(),
					InventoryConstant.TXT_SELECT_FROM
						+ ivjradioSubcon.getActionCommand()
						+ CommonConstant.STR_COLON));
			setcomboSelectFromEntityId();
			caInvAlloctnUIData.setFromInvLocIdCd(
				InventoryConstant.CHAR_S);
		}
		else if (getradioDlr().isSelected())
		{
			getcomboSelectFromEntityId().setVisible(true);
			getpnlSelectFromEntityId().setBorder(
				new TitledBorder(
					new EtchedBorder(),
					InventoryConstant.TXT_SELECT_FROM
						+ ivjradioDlr.getActionCommand()
						+ CommonConstant.STR_COLON));
			setcomboSelectFromEntityId();
			caInvAlloctnUIData.setFromInvLocIdCd(
				InventoryConstant.CHAR_D);
		}
		else if (getradioEmp().isSelected())
		{
			getcomboSelectFromEntityId().setVisible(true);
			getpnlSelectFromEntityId().setBorder(
				new TitledBorder(
					new EtchedBorder(),
					InventoryConstant.TXT_SELECT_FROM
						+ ivjradioEmp.getActionCommand()
						+ CommonConstant.STR_COLON));
			setcomboSelectFromEntityId();
			caInvAlloctnUIData.setFromInvLocIdCd(
				InventoryConstant.CHAR_E);
		}
	}
}