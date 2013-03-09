package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmAllocateToLocationINV010.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		05/08/2002	CQU100003801 fix - lengthen subcon combo 
 *							field.
 * Min Wang		05/13/2002	CQU100003892 fixed when an entity selected,
 *							then to location is changed, set location 
 *							code to central.
 * Min Wang		06/25/2004	Show entity field when wrap around 22 char.
 *							Resize Frame
 *							defect 6827 Ver 5.2.1
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source,
 * 							rename fields, define some constants
 * 							add setVisibleRTS()
 * 							delete setVisible()
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3  
 * Ray Rowehl	08/08/2005	Cleanup pass
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove Key actions from button panel.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Setup to use new default font and size 
 * 							function when setting bold.
 * 							Move constants to appropriate constants
 * 							classes.
 * 							modify getlblFromEntityId(),
 * 								getlblFromLoc()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Remove selection from key processing.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/18/2005	Remove color from text.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	update Mnemonics.
 * 							defect 7890 Ver 5.2.3
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setcomboSelectToEntityId(), 
 * 								setcomboSelectToLoc()
 * 							defect 8479 Ver 5.2.3 
 * Jeff S.		12/20/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), ciSelctdRadioButton, 
 * 								carrRadioButton
 * 							modify getpnlSelectFromEntity(), 
 * 								getradioDlr(), getradioEmp(), 
 * 								getradioServer(), getradioSubcon(),
 * 								getradioWs(), initialize()
 * 							defect 7890 Ver 5.2.3
 * Min Wang		05/20/2008	Show longest name with several �W� on the 
 * 							INV010 the bottom half of the name.
 * 							modify getstcLblFrom(), getlblFromEntity()
 * 							getlblFromEntityId()
 * 							defect 8733 Ver Defect_POS_A
 * K Harrell	06/25/2009	Refactored DealersData to DealerData
 * 							Additional Cleanup.
 * 							modify setcomboListData() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	02/18/2010	Implement new SubcontractorData
 * 							modify setcomboListData() 
 * 							defect 10161 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * This class is for screen INV010 Allocate To Location.
 *
 * @version	POS_640 		02/18/2010
 * @author	Charlie Walker
 * <br>Creation Date:		06/27/2001 10:18:07
 */

public class FrmAllocateToLocationINV010
	extends RTSDialogBox
	implements ActionListener, ItemListener 
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JComboBox ivjcomboSelectToEntityId = null;
	private JComboBox ivjcomboSelectToLoc = null;
	private JPanel ivjFrmAllocateToLocationINV010ContentPane1 = null;
	private JLabel ivjlblFromEntity = null;
	private RTSTextArea ivjlblFromEntityId = null;
	private RTSTextArea ivjlblFromLoc = null;
	private JPanel ivjpnlLayout = null;
	private JPanel ivjpnlSelectToEntity = null;
	private JPanel ivjpnlSelectToEntityId = null;
	private JPanel ivjpnlSelectToLoc = null;
	private JRadioButton ivjradioDlr = null;
	private JRadioButton ivjradioEmp = null;
	private JRadioButton ivjradioServer = null;
	private JRadioButton ivjradioSubcon = null;
	private JRadioButton ivjradioWs = null;
	private JLabel ivjstcLblFrom = null;

	/**
	 * Data object used to store the Subcontractors, Workstations, 
	 * Dealer, and Employees for the combo box, and the Substations
	 */
	private AllocationDbData caAlloctnDbData = new AllocationDbData();

	/**
	 * InventoryAllocationUIData object used to collect the UI data
	 */
	private InventoryAllocationUIData caInvAlloctnUIData =
		new InventoryAllocationUIData();

	/**
	 * InventoryAllocationUIData object used to store the orignal data
	 * object from INV009
	 */
	private InventoryAllocationUIData caInvAlloctnUIDataOld =
		new InventoryAllocationUIData();

	/**
	 * Vector used to store the Dealer for the combo box
	 */
	private Vector cvDlrComboData = new Vector();

	/**
	 * Vector used to store the Employees for the combo box
	 */
	private Vector cvEmpComboData = new Vector();

	/**
	 * Vector used to store the Subcontractors for the combo box
	 */
	private Vector cvSubconComboData = new Vector();

	/**
	 * Vector used to store the Workstations for the combo box
	 */
	private Vector cvWsComboData = new Vector();

	private static final int MAX_ROW_COUNT = 5;

	private static final String TEXT_COUNTY_OFFICE = "County\nOffice";

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
			FrmAllocateToLocationINV010 laFrmAllocateToLocationINV010;
			laFrmAllocateToLocationINV010 =
				new FrmAllocateToLocationINV010();
			laFrmAllocateToLocationINV010.setModal(true);
			laFrmAllocateToLocationINV010
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmAllocateToLocationINV010.show();
			java.awt.Insets insets =
				laFrmAllocateToLocationINV010.getInsets();
			laFrmAllocateToLocationINV010.setSize(
				laFrmAllocateToLocationINV010.getWidth()
					+ insets.left
					+ insets.right,
				laFrmAllocateToLocationINV010.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmAllocateToLocationINV010.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmAllocateToLocationINV010 constructor comment.
	 */
	public FrmAllocateToLocationINV010()
	{
		super();
		initialize();
	}

	/**
	 * FrmAllocateToLocationINV010 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmAllocateToLocationINV010(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmAllocateToLocationINV010 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmAllocateToLocationINV010(JFrame aaParent)
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
				setpnlSelectToEntityId();
			}

			// Determines what actions to take when Enter, Cancel, 
			// or Help are pressed.
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// Handles the case when server is not selected and 
				// the combo box doesn't contain any values
				if (caInvAlloctnUIData.getInvLocIdCd()
					!= InventoryConstant.CHAR_C
					&& ((String) getcomboSelectToEntityId()
						.getSelectedItem()
						== null
						|| (
							(String) getcomboSelectToEntityId()
								.getSelectedItem())
								.equals(
							CommonConstant.STR_SPACE_EMPTY)))
				{
					RTSException leRTSEx = new RTSException();
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_ENTITY_INVALID),
						getcomboSelectToEntityId());
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}

				// Store the user input.
				captureUserInput();

				Vector lvDataOut = new Vector();
				lvDataOut.addElement(caAlloctnDbData);
				lvDataOut.addElement(caInvAlloctnUIData);

				getController().processData(
					AbstractViewController.ENTER,
					lvDataOut);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caInvAlloctnUIDataOld);
			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV010);
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
			.getInvLocIdCd()
			.equals(InventoryConstant.CHAR_C))
		{
			caInvAlloctnUIData.setInvId(CommonConstant.STR_ZERO);
			caInvAlloctnUIData.setToInvIdName(CommonConstant.STR_ZERO);

		}
		else if (
			caInvAlloctnUIData.getInvLocIdCd().equals(
				InventoryConstant.CHAR_W))
		{
			caInvAlloctnUIData.setInvId(
				(String) getcomboSelectToEntityId().getSelectedItem());
			caInvAlloctnUIData.setToInvIdName(
				(String) getcomboSelectToEntityId().getSelectedItem());

		}
		else
		{
			String lsSubstring =
				new String(
					(String) getcomboSelectToEntityId()
						.getSelectedItem());
			lsSubstring =
				lsSubstring.substring(
					0,
					lsSubstring.indexOf(CommonConstant.STR_DASH));
			lsSubstring = lsSubstring.trim();
			caInvAlloctnUIData.setInvId(lsSubstring);
			caInvAlloctnUIData.setToInvIdName(
				(String) getcomboSelectToEntityId().getSelectedItem());
		}

		caInvAlloctnUIData.setToLoc(
			(String) getcomboSelectToLoc().getSelectedItem());

		for (int i = 0;
			i < caAlloctnDbData.getSubstaWrap().size();
			i++)
		{
			SubstationData laSubstaData = new SubstationData();
			laSubstaData =
				(SubstationData) caAlloctnDbData.getSubstaWrap().get(i);
			if (laSubstaData
				.getSubstaName()
				.equals(
					(String) getcomboSelectToLoc().getSelectedItem()))
			{
				caInvAlloctnUIData.setSubstaId(
					laSubstaData.getSubstaId());
				break;
			}
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setBounds(187, 325, 284, 58);
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
	 * Return the ivjcomboSelectToEntityId property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboSelectToEntityId()
	{
		if (ivjcomboSelectToEntityId == null)
		{
			try
			{
				ivjcomboSelectToEntityId = new JComboBox();
				ivjcomboSelectToEntityId.setName(
					"ivjcomboSelectToEntityId");
				ivjcomboSelectToEntityId.setBackground(
					java.awt.Color.white);
				ivjcomboSelectToEntityId.setVisible(false);
				ivjcomboSelectToEntityId.setMaximumSize(
					new java.awt.Dimension(50, 23));
				ivjcomboSelectToEntityId.setMaximumRowCount(
					MAX_ROW_COUNT);
				ivjcomboSelectToEntityId.setPreferredSize(
					new java.awt.Dimension(50, 23));
				ivjcomboSelectToEntityId.setBounds(7, 30, 440, 23);
				ivjcomboSelectToEntityId.setMinimumSize(
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
		return ivjcomboSelectToEntityId;
	}

	/**
	 * Return the ivjcomboSelectToLoc property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboSelectToLoc()
	{
		if (ivjcomboSelectToLoc == null)
		{
			try
			{
				ivjcomboSelectToLoc = new JComboBox();
				ivjcomboSelectToLoc.setName("ivjcomboSelectToLoc");
				ivjcomboSelectToLoc.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboSelectToLoc.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboSelectToLoc.setBackground(java.awt.Color.white);
				ivjcomboSelectToLoc.setMaximumSize(
					new java.awt.Dimension(50, 23));
				ivjcomboSelectToLoc.setPreferredSize(
					new java.awt.Dimension(50, 23));
				ivjcomboSelectToLoc.setBounds(7, 30, 320, 23);
				ivjcomboSelectToLoc.setMinimumSize(
					new java.awt.Dimension(50, 23));
				// user code begin {1}
				ivjcomboSelectToLoc.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboSelectToLoc;
	}

	/**
	 * Return the ivjFrmAllocateToLocationINV010ContentPane1 property 
	 * value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmAllocateToLocationINV010ContentPane1()
	{
		if (ivjFrmAllocateToLocationINV010ContentPane1 == null)
		{
			try
			{
				ivjFrmAllocateToLocationINV010ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmAllocateToLocationINV010ContentPane1.setName(
					"ivjFrmAllocateToLocationINV010ContentPane1");
				ivjFrmAllocateToLocationINV010ContentPane1.setLayout(
					null);
				ivjFrmAllocateToLocationINV010ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(495, 378));
				ivjFrmAllocateToLocationINV010ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(495, 378));
				ivjFrmAllocateToLocationINV010ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmAllocateToLocationINV010ContentPane1().add(
					getpnlSelectToEntity(),
					getpnlSelectToEntity().getName());
				getFrmAllocateToLocationINV010ContentPane1().add(
					getpnlSelectToLoc(),
					getpnlSelectToLoc().getName());
				getFrmAllocateToLocationINV010ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmAllocateToLocationINV010ContentPane1().add(
					getpnlLayout(),
					getpnlLayout().getName());
				getFrmAllocateToLocationINV010ContentPane1().add(
					getpnlSelectToEntityId(),
					getpnlSelectToEntityId().getName());
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
		return ivjFrmAllocateToLocationINV010ContentPane1;
	}

	/**
	 * Return the ivjlblFromEntity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblFromEntity()
	{
		if (ivjlblFromEntity == null)
		{
			try
			{
				ivjlblFromEntity = new javax.swing.JLabel();
				ivjlblFromEntity.setName("ivjlblFromEntity");
				ivjlblFromEntity.setText(
					InventoryConstant.TXT_ENTITY_COLON);
				ivjlblFromEntity.setBounds(13, 59, 157, 14);
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
		return ivjlblFromEntity;
	}

	/**
	 * Return the ivjlblFromEntityId property value.
	 * 
	 * @return RTSTextArea
	 */
	private RTSTextArea getlblFromEntityId()
	{
		if (ivjlblFromEntityId == null)
		{
			try
			{
				ivjlblFromEntityId = new RTSTextArea();
				ivjlblFromEntityId.setName("ivjlblFromEntityId");
				ivjlblFromEntityId.setLineWrap(true);
				ivjlblFromEntityId.setWrapStyleWord(true);
				ivjlblFromEntityId.setText(
					InventoryConstant.TXT_SPECIFIC_ENTITY);
				ivjlblFromEntityId.setBackground(
					new java.awt.Color(204, 204, 204));
				ivjlblFromEntityId.setFont(
					new java.awt.Font(
						UtilityMethods.getDefaultFont(),
						java.awt.Font.BOLD,
						UtilityMethods.getDefaultFontSize()));
				ivjlblFromEntityId.setBounds(58, 79, 147, 51);
				ivjlblFromEntityId.setEditable(false);
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
		return ivjlblFromEntityId;
	}

	/**
	 * Return the ivjlblFromLoc property value.
	 * 
	 * @return RTSTextArea
	 */
	private RTSTextArea getlblFromLoc()
	{
		if (ivjlblFromLoc == null)
		{
			try
			{
				ivjlblFromLoc = new RTSTextArea();
				ivjlblFromLoc.setName("ivjlblFromLoc");
				ivjlblFromLoc.setAlignmentY(
					java.awt.Component.CENTER_ALIGNMENT);
				ivjlblFromLoc.setWrapStyleWord(true);
				ivjlblFromLoc.setBounds(58, 14, 147, 41);
				ivjlblFromLoc.setMinimumSize(
					new java.awt.Dimension(34, 14));
				ivjlblFromLoc.setEditable(false);
				ivjlblFromLoc.setLineWrap(true);
				ivjlblFromLoc.setText(TEXT_COUNTY_OFFICE);
				ivjlblFromLoc.setBackground(
					new java.awt.Color(204, 204, 204));
				ivjlblFromLoc.setMaximumSize(
					new java.awt.Dimension(34, 14));
				ivjlblFromLoc.setPreferredSize(
					new java.awt.Dimension(34, 14));
				// make the text bold
				ivjlblFromLoc.setFont(
					new java.awt.Font(
						UtilityMethods.getDefaultFont(),
						java.awt.Font.BOLD,
						UtilityMethods.getDefaultFontSize()));
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
		return ivjlblFromLoc;
	}

	/**
	 * Return the ivjpnlLayout property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlLayout()
	{
		if (ivjpnlLayout == null)
		{
			try
			{
				ivjpnlLayout = new JPanel();
				ivjpnlLayout.setName("ivjpnlLayout");
				ivjpnlLayout.setLayout(null);
				ivjpnlLayout.setBounds(7, 192, 217, 132);
				getpnlLayout().add(
					getlblFromLoc(),
					getlblFromLoc().getName());
				getpnlLayout().add(
					getstcLblFrom(),
					getstcLblFrom().getName());
				getpnlLayout().add(
					getlblFromEntity(),
					getlblFromEntity().getName());
				getpnlLayout().add(
					getlblFromEntityId(),
					getlblFromEntityId().getName());
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
		return ivjpnlLayout;
	}

	/**
	 * Return the ivjpnlSelectToEntity property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectToEntity()
	{
		if (ivjpnlSelectToEntity == null)
		{
			try
			{
				ivjpnlSelectToEntity = new JPanel();
				ivjpnlSelectToEntity.setName("ivjpnlSelectToEntity");
				ivjpnlSelectToEntity.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_SELECT_TO_ENTITY_COLON));
				ivjpnlSelectToEntity.setLayout(null);
				ivjpnlSelectToEntity.setMaximumSize(
					new java.awt.Dimension(154, 207));
				ivjpnlSelectToEntity.setMinimumSize(
					new java.awt.Dimension(154, 207));
				ivjpnlSelectToEntity.setBounds(21, 11, 177, 174);
				getpnlSelectToEntity().add(
					getradioServer(),
					getradioServer().getName());
				getpnlSelectToEntity().add(
					getradioWs(),
					getradioWs().getName());
				getpnlSelectToEntity().add(
					getradioSubcon(),
					getradioSubcon().getName());
				getpnlSelectToEntity().add(
					getradioDlr(),
					getradioDlr().getName());
				getpnlSelectToEntity().add(
					getradioEmp(),
					getradioEmp().getName());
				// user code begin {1}
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
		return ivjpnlSelectToEntity;
	}

	/**
	 * Return the ivjpnlSelectToEntityId property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectToEntityId()
	{
		if (ivjpnlSelectToEntityId == null)
		{
			try
			{
				ivjpnlSelectToEntityId = new JPanel();
				ivjpnlSelectToEntityId.setName(
					"ivjpnlSelectToEntityId");
				ivjpnlSelectToEntityId.setLayout(null);
				ivjpnlSelectToEntityId.setBounds(223, 166, 454, 151);
				getpnlSelectToEntityId().add(
					getcomboSelectToEntityId(),
					getcomboSelectToEntityId().getName());
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
		return ivjpnlSelectToEntityId;
	}

	/**
	 * Return the ivjpnlSelectToLoc property value.
	 * 
	 * @return JPanel
	 */
	private javax.swing.JPanel getpnlSelectToLoc()
	{
		if (ivjpnlSelectToLoc == null)
		{
			try
			{
				ivjpnlSelectToLoc = new JPanel();
				ivjpnlSelectToLoc.setName("ivjpnlSelectToLoc");
				ivjpnlSelectToLoc.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant
							.TXT_SELECT_TO_LOCATION_COLON));
				ivjpnlSelectToLoc.setLayout(null);
				ivjpnlSelectToLoc.setMaximumSize(
					new java.awt.Dimension(275, 97));
				ivjpnlSelectToLoc.setMinimumSize(
					new java.awt.Dimension(275, 97));
				ivjpnlSelectToLoc.setBounds(223, 11, 345, 131);
				getpnlSelectToLoc().add(
					getcomboSelectToLoc(),
					getcomboSelectToLoc().getName());
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
		return ivjpnlSelectToLoc;
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
				ivjradioDlr.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjradioDlr.setText(InventoryConstant.TXT_DEALER);
				ivjradioDlr.setMaximumSize(
					new java.awt.Dimension(62, 22));
				ivjradioDlr.setActionCommand(
					InventoryConstant.TXT_DEALER);
				ivjradioDlr.setBounds(33, 106, 108, 22);
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
				ivjradioEmp.setMnemonic(java.awt.event.KeyEvent.VK_M);
				ivjradioEmp.setText(InventoryConstant.TXT_EMPLOYEE);
				ivjradioEmp.setMaximumSize(
					new java.awt.Dimension(80, 22));
				ivjradioEmp.setActionCommand(
					InventoryConstant.TXT_EMPLOYEE);
				ivjradioEmp.setBounds(33, 133, 108, 22);
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
				ivjradioServer.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjradioServer.setText(InventoryConstant.TXT_SERVER);
				ivjradioServer.setMaximumSize(
					new java.awt.Dimension(63, 22));
				ivjradioServer.setActionCommand(
					InventoryConstant.TXT_SERVER);
				ivjradioServer.setSelected(true);
				ivjradioServer.setBounds(33, 25, 108, 22);
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
				ivjradioSubcon.setMnemonic(
					java.awt.event.KeyEvent.VK_B);
				ivjradioSubcon.setText(
					InventoryConstant.TXT_SUBCONTRACTOR);
				ivjradioSubcon.setMaximumSize(
					new java.awt.Dimension(107, 22));
				ivjradioSubcon.setActionCommand(
					InventoryConstant.TXT_SUBCONTRACTOR);
				ivjradioSubcon.setBounds(33, 79, 108, 22);
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
				ivjradioWs = new JRadioButton();
				ivjradioWs.setName("ivjradioWs");
				ivjradioWs.setMnemonic(java.awt.event.KeyEvent.VK_W);
				ivjradioWs.setText(InventoryConstant.TXT_WORKSTATION);
				ivjradioWs.setMaximumSize(
					new java.awt.Dimension(95, 22));
				ivjradioWs.setActionCommand(
					InventoryConstant.TXT_WORKSTATION);
				ivjradioWs.setBounds(33, 52, 108, 22);
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
	 * Return the ivjstcLblFrom property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblFrom()
	{
		if (ivjstcLblFrom == null)
		{
			try
			{
				ivjstcLblFrom = new JLabel();
				ivjstcLblFrom.setName("ivjstcLblFrom");
				ivjstcLblFrom.setText(InventoryConstant.TXT_FROM_COLON);
				ivjstcLblFrom.setVerticalAlignment(
					javax.swing.SwingConstants.TOP);
				ivjstcLblFrom.setMaximumSize(
					new java.awt.Dimension(32, 14));
				ivjstcLblFrom.setMinimumSize(
					new java.awt.Dimension(32, 14));
				ivjstcLblFrom.setBounds(13, 14, 39, 14);
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
		return ivjstcLblFrom;
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
			setName(ScreenConstant.INV010_FRAME_NAME);
			setSize(685, 390);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV010_FRAME_TITLE);
			setContentPane(
				getFrmAllocateToLocationINV010ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * 
	 * <p>If the TO Location in the combo box is different than the
	 * FROM Location, then select the Server radio button, and disable
	 * all the other radio buttons and the TO Entity combo box.
	 * 
	 * @param aaIE ItemEvent 
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		if (aaIE.getSource() instanceof JComboBox)
		{
			if (aaIE.getSource() == getcomboSelectToLoc())
			{
				if (!getcomboSelectToLoc()
					.getSelectedItem()
					.equals(caInvAlloctnUIData.getFromLoc()))
				{
					getradioServer().setSelected(true);
					getradioServer().requestFocus();
					getradioWs().setEnabled(false);
					getradioSubcon().setEnabled(false);
					getradioDlr().setEnabled(false);
					getradioEmp().setEnabled(false);
					getcomboSelectToEntityId().setVisible(false);
					getpnlSelectToEntityId().setBorder(null);
					getcomboSelectToLoc().requestFocus();
					//changing substations, so must go to central
					caInvAlloctnUIData.setInvLocIdCd(
						InventoryConstant.CHAR_C);
				}
				else
				{
					getradioServer().setSelected(true);
					getradioServer().requestFocus();
					getradioWs().setEnabled(true);
					getradioSubcon().setEnabled(true);
					getradioDlr().setEnabled(true);
					getradioEmp().setEnabled(true);
					getcomboSelectToEntityId().setVisible(false);
					getpnlSelectToEntityId().setBorder(null);
				}
			}
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
		/**
		 * Concatenate subcontractor information
		 */
		Vector lvSubconData = aaAlloctnDbData.getSubconWrap();
		if (lvSubconData != null)
		{
			for (int i = 0; i < lvSubconData.size(); i++)
			{
				// defect 10161 
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
	 * Used to fill the Select TO Entity Id combobox with the 
	 * appropriate info depending on which radio button is selected.
	 */
	private void setcomboSelectToEntityId()
	{
		if (getradioServer().isSelected())
		{
			getcomboSelectToEntityId().removeAllItems();
		}
		else if (getradioWs().isSelected())
		{
			getcomboSelectToEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvWsComboData));
			getcomboSelectToEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboSelectToEntityId());
			// end defect 8479
		}
		else if (getradioSubcon().isSelected())
		{
			getcomboSelectToEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvSubconComboData));
			getcomboSelectToEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboSelectToEntityId());
			// end defect 8479
		}
		else if (getradioDlr().isSelected())
		{
			getcomboSelectToEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvDlrComboData));
			getcomboSelectToEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboSelectToEntityId());
			// end defect 8479
		}
		else if (getradioEmp().isSelected())
		{
			getcomboSelectToEntityId().removeAllItems();
			ComboBoxModel laComboModel =
				new DefaultComboBoxModel(
					(Vector) UtilityMethods.copy(cvEmpComboData));
			getcomboSelectToEntityId().setModel(laComboModel);
			// defect 8479
			comboBoxHotKeyFix(getcomboSelectToEntityId());
			// end defect 8479
		}
	}

	/**
	 * Used to set the data for the Select TO Location combo box & 
	 * default to the current substation..
	 * 
	 * @param avSubstaData Vector
	 */
	private void setcomboSelectToLoc(Vector avSubstaData)
	{
		SubstationData laSubstaData = new SubstationData();
		String lsCrrntSubstaName = new String();

		if (caInvAlloctnUIData.getSubstaId() == 0)
		{
			getcomboSelectToLoc().removeAllItems();
			for (int i = 0; i < avSubstaData.size(); i++)
			{
				laSubstaData = (SubstationData) avSubstaData.get(i);
				getcomboSelectToLoc().addItem(
					laSubstaData.getSubstaName());
				if (laSubstaData.getSubstaId()
					== SystemProperty.getSubStationId())
				{
					lsCrrntSubstaName = laSubstaData.getSubstaName();
				}
			}
		}
		else
		{
			for (int i = 0; i < avSubstaData.size(); i++)
			{
				laSubstaData = (SubstationData) avSubstaData.get(i);
				if (caInvAlloctnUIData.getSubstaId()
					== laSubstaData.getSubstaId()
					|| laSubstaData.getSubstaId() == 0)
				{
					getcomboSelectToLoc().addItem(
						laSubstaData.getSubstaName());
					if (laSubstaData.getSubstaId()
						== SystemProperty.getSubStationId())
					{
						lsCrrntSubstaName =
							laSubstaData.getSubstaName();
					}
				}
			}
		}

		// Default to the current substa name
		getcomboSelectToLoc().setSelectedItem(lsCrrntSubstaName);
		// defect 8479
		comboBoxHotKeyFix(getcomboSelectToLoc());
		// end defect 8479
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

		if (aaData == null)
		{
			return;
		}
		Vector lvDataIn = (Vector) aaData;

		caAlloctnDbData =
			(AllocationDbData) lvDataIn.elementAt(
				CommonConstant.ELEMENT_0);
		InventoryAllocationUIData laInvAllocUIData =
			(InventoryAllocationUIData) lvDataIn.elementAt(
				CommonConstant.ELEMENT_1);

		caInvAlloctnUIDataOld =
			(InventoryAllocationUIData) UtilityMethods.copy(
				laInvAllocUIData);
		caInvAlloctnUIData =
			(InventoryAllocationUIData) UtilityMethods.copy(
				laInvAllocUIData);

		// Set the FROM office location and entity
		setFromLbls();

		// Populate the combo box which displays the TO Location
		setcomboSelectToLoc(caAlloctnDbData.getSubstaWrap());

		if (caInvAlloctnUIData.getInvLocIdCd() == null)
		{
			caInvAlloctnUIData.setInvLocIdCd(InventoryConstant.CHAR_C);
		}
		getradioServer().requestFocus();
		setcomboListData(caAlloctnDbData);
	}

	/**
	 * Used to display the FROM office location and entity.
	 */
	private void setFromLbls()
	{
		// Display the FROM Loc
		getlblFromLoc().setText(caInvAlloctnUIDataOld.getFromLoc());

		// Display or hide the FROM Entity
		getlblFromEntityId().setText(
			caInvAlloctnUIDataOld.getFromInvIdName());
		if (caInvAlloctnUIDataOld
			.getFromInvLocIdCd()
			.equals(InventoryConstant.CHAR_C))
		{
			getlblFromEntity().setText(CommonConstant.STR_SPACE_ONE);
			getlblFromEntityId().setText(CommonConstant.STR_SPACE_ONE);
		}
		else if (
			caInvAlloctnUIDataOld.getFromInvLocIdCd().equals(
				InventoryConstant.CHAR_W))
		{
			getlblFromEntity().setText(
				InventoryConstant.STR_W.toUpperCase()
					+ CommonConstant.STR_COLON);
		}
		else if (
			caInvAlloctnUIDataOld.getFromInvLocIdCd().equals(
				InventoryConstant.CHAR_S))
		{
			getlblFromEntity().setText(
				InventoryConstant.STR_S.toUpperCase()
					+ CommonConstant.STR_COLON);
		}
		else if (
			caInvAlloctnUIDataOld.getFromInvLocIdCd().equals(
				InventoryConstant.CHAR_D))
		{
			getlblFromEntity().setText(
				InventoryConstant.STR_D.toUpperCase()
					+ CommonConstant.STR_COLON);
		}
		else if (
			caInvAlloctnUIDataOld.getFromInvLocIdCd().equals(
				InventoryConstant.CHAR_E))
		{
			getlblFromEntity().setText(
				InventoryConstant.STR_E.toUpperCase()
					+ CommonConstant.STR_COLON);
		}
	}

	/**
	 * Depending on which radio button is selected, this method sets 
	 * the boarder of of the Select TO Entity Id panel and calls 
	 * setcomboSelectFromEntityId().
	 */
	private void setpnlSelectToEntityId()
	{
		if (getradioServer().isSelected())
		{
			getpnlSelectToEntityId().setBorder(null);
			getcomboSelectToEntityId().setVisible(false);
			setcomboSelectToEntityId();
			caInvAlloctnUIData.setInvLocIdCd(InventoryConstant.CHAR_C);
		}
		else if (getradioWs().isSelected())
		{
			getpnlSelectToEntityId().setBorder(
				new TitledBorder(
					new EtchedBorder(),
					InventoryConstant.TXT_SELECT_TO
						+ ivjradioWs.getActionCommand()
						+ CommonConstant.STR_COLON));
			getcomboSelectToEntityId().setVisible(true);
			setcomboSelectToEntityId();
			caInvAlloctnUIData.setInvLocIdCd(InventoryConstant.CHAR_W);
		}
		else if (getradioSubcon().isSelected())
		{
			getpnlSelectToEntityId().setBorder(
				new TitledBorder(
					new EtchedBorder(),
					InventoryConstant.TXT_SELECT_TO
						+ ivjradioSubcon.getActionCommand()
						+ CommonConstant.STR_COLON));
			getcomboSelectToEntityId().setVisible(true);
			setcomboSelectToEntityId();
			caInvAlloctnUIData.setInvLocIdCd(InventoryConstant.CHAR_S);
		}
		else if (getradioDlr().isSelected())
		{
			getpnlSelectToEntityId().setBorder(
				new TitledBorder(
					new EtchedBorder(),
					InventoryConstant.TXT_SELECT_TO
						+ ivjradioDlr.getActionCommand()
						+ CommonConstant.STR_COLON));
			getcomboSelectToEntityId().setVisible(true);
			setcomboSelectToEntityId();
			caInvAlloctnUIData.setInvLocIdCd(InventoryConstant.CHAR_D);
		}
		else if (getradioEmp().isSelected())
		{
			getpnlSelectToEntityId().setBorder(
				new TitledBorder(
					new EtchedBorder(),
					InventoryConstant.TXT_SELECT_TO
						+ ivjradioEmp.getActionCommand()
						+ CommonConstant.STR_COLON));
			getcomboSelectToEntityId().setVisible(true);
			setcomboSelectToEntityId();
			caInvAlloctnUIData.setInvLocIdCd(InventoryConstant.CHAR_E);
		}
	}
}
