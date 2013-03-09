package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AllocationDbData;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmAllocationSummaryINV014.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		03/25/2003	Made changes to actionPerformed()
 *							defect 5886  
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source,
 * 							rename fields
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/25/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * Min Wang		08/01/2005	Remove item code from screen.
 * 							modify gettblAllocatedItems()
 * 							defect 8269 Ver 5.2.2 Fix 6
 * Ray Rowehl	08/08/2005	Cleanup pass
 * 							Remove key processing.
 * 							Work on constants.
 * 							modify isCancelEnabled()
 * 							defect 7890 Ver 5.2.3  
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants 
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics.
 * 							defect 7890 Ver 5.2.3 
 * Jeff S		02/08/2006	Set the default focus field to the Allocate
 * 							button.
 * 							modify initialize()
 * 							defect 7890 Ver 5.2.3 
 * Min Wang		04/09/2008	Enlarge the lblFromEntityId and lblToEntityId.
 * 							modify getlblFromEntityId(),getlblToEntityId()
 * 							defect 8734 Ver Defect_POS_A
 * K Harrell	06/25/2009	Cleanup;  No logic change.
 * 							delete getBuilderData()
 * 							defect 10112 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * This class is for screen INV014 Allocation Summary.
 *
 * @version	Defect_POS_F	06/25/2009
 * @author	Charlie Walker
 * <br>Creation Date:		06/27/2001 10:18:07
 */

public class FrmAllocationSummaryINV014
	extends RTSDialogBox
	implements ActionListener 
{
	private RTSButton ivjbtnAllocate = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkViewInventoryAllocateReport = null;
	private JPanel ivjFrmAllocationSummaryINV014ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblFromEntity = null;
	private JLabel ivjlblFromEntityId = null;
	private JLabel ivjlblFromLoc = null;
	private JLabel ivjlblToEntity = null;
	private JLabel ivjlblToEntityId = null;
	private JLabel ivjlblToLoc = null;
	private JPanel ivjpnlAlloctnLocs = null;
	private JLabel ivjstcLblFrom = null;
	private JLabel ivjstcLblSentence1 = null;
	private JLabel ivjstcLblTo = null;
	private RTSTable ivjtblAllocatedItems = null;

	/**
	 * Data object used to store the Subcontractors, Workstations, 
	 * Dealers, and Employees for the combo box, and the Substations
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
	private TMINV014 caTableModel = null;

	/**
	 * Flag to determine whether an item has been allocated
	 */
	private boolean cbDisableCancel = false;

	/**
	 * A vector containing all the inventory items that have been 
	 * allocated during this session.
	 */
	private Vector cvAllocatedItms = new Vector();

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
			FrmAllocationSummaryINV014 laFrmAllocationSummaryINV014;
			laFrmAllocationSummaryINV014 =
				new FrmAllocationSummaryINV014();
			laFrmAllocationSummaryINV014.setModal(true);
			laFrmAllocationSummaryINV014
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmAllocationSummaryINV014.show();
			java.awt.Insets insets =
				laFrmAllocationSummaryINV014.getInsets();
			laFrmAllocationSummaryINV014.setSize(
				laFrmAllocationSummaryINV014.getWidth()
					+ insets.left
					+ insets.right,
				laFrmAllocationSummaryINV014.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmAllocationSummaryINV014.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmAllocationSummaryINV014 constructor comment.
	 */
	public FrmAllocationSummaryINV014()
	{
		super();
		initialize();
	}

	/**
	 * FrmAllocationSummaryINV014 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmAllocationSummaryINV014(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmAllocationSummaryINV014 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmAllocationSummaryINV014(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Used to determine what action to take when an action is 
	 * performed on the screen.
	 * 
	 * <p>Do not allow action when the frame is not visible.
	 *
	 * @param aaAE ActionEvent  
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		// Code to prevent multiple button clicks
		if (!startWorking() || !isVisible())
		{
			return;
		}

		try
		{
			// Determines what actions to take when Allocate, Enter, 
			// Cancel, or Help are pressed.
			if (aaAE.getSource() == getbtnAllocate())
			{
				caInvAlloctnUIData.setCalcInv(false);
				getController().processData(
					VCAllocationSummaryINV014.ALLOCATE,
					caInvAlloctnUIData);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnEnter()
					&& !getchkViewInventoryAllocateReport().isSelected())
			{
				Vector lvDataOut = new Vector();
				lvDataOut.addElement(caAlloctnDbData);
				lvDataOut.addElement(cvAllocatedItms);
				lvDataOut.addElement(new Integer(1));

				getController().processData(
					AbstractViewController.ENTER,
					lvDataOut);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnEnter()
					&& getchkViewInventoryAllocateReport().isSelected())
			{
				Vector lvDataOut = new Vector();
				lvDataOut.addElement(caAlloctnDbData);
				lvDataOut.addElement(cvAllocatedItms);
				lvDataOut.addElement(new Integer(1));

				getController().processData(
					VCAllocationSummaryINV014
						.GENERATE_ALLOCATION_REPORT,
					lvDataOut);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV014A);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ivjbtnAllocate property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnAllocate()
	{
		if (ivjbtnAllocate == null)
		{
			try
			{
				ivjbtnAllocate = new RTSButton();
				ivjbtnAllocate.setName("ivjbtnAllocate");
				ivjbtnAllocate.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				ivjbtnAllocate.setText(InventoryConstant.TXT_ALLOCATE);
				// user code begin {1}
				ivjbtnAllocate.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnAllocate;
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
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
	 * Return the ivjchkViewInventoryAllocateReport property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkViewInventoryAllocateReport()
	{
		if (ivjchkViewInventoryAllocateReport == null)
		{
			try
			{
				ivjchkViewInventoryAllocateReport = new JCheckBox();
				ivjchkViewInventoryAllocateReport.setName(
					"ivjchkViewInventoryAllocateReport");
				ivjchkViewInventoryAllocateReport.setSelected(false);
				ivjchkViewInventoryAllocateReport.setMnemonic(
					java.awt.event.KeyEvent.VK_V);
				ivjchkViewInventoryAllocateReport.setText(
					InventoryConstant.TXT_VIEW_INV_ALLOC_RPT);
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
		return ivjchkViewInventoryAllocateReport;
	}

	/**
	 * Return the ivjFrmAllocationSummaryINV014ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmAllocationSummaryINV014ContentPane1()
	{
		if (ivjFrmAllocationSummaryINV014ContentPane1 == null)
		{
			try
			{
				ivjFrmAllocationSummaryINV014ContentPane1 =
					new JPanel();
				ivjFrmAllocationSummaryINV014ContentPane1.setName(
					"ivjFrmAllocationSummaryINV014ContentPane1");
				ivjFrmAllocationSummaryINV014ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmAllocationSummaryINV014ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));

				java.awt.GridBagConstraints constraintspnlAlloctnLocs =
					new java.awt.GridBagConstraints();
				constraintspnlAlloctnLocs.gridx = 1;
				constraintspnlAlloctnLocs.gridy = 1;
				constraintspnlAlloctnLocs.gridwidth = 3;
				constraintspnlAlloctnLocs.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlAlloctnLocs.weightx = 1.0;
				constraintspnlAlloctnLocs.weighty = 1.0;
				constraintspnlAlloctnLocs.ipadx = 60;
				constraintspnlAlloctnLocs.ipady = 43;
				constraintspnlAlloctnLocs.insets =
					new java.awt.Insets(9, 18, 6, 18);
				getFrmAllocationSummaryINV014ContentPane1().add(
					getpnlAlloctnLocs(),
					constraintspnlAlloctnLocs);

				java.awt.GridBagConstraints constraintsstcLblSentence1 =
					new java.awt.GridBagConstraints();
				constraintsstcLblSentence1.gridx = 1;
				constraintsstcLblSentence1.gridy = 2;
				constraintsstcLblSentence1.gridwidth = 3;
				constraintsstcLblSentence1.ipadx = 109;
				constraintsstcLblSentence1.insets =
					new java.awt.Insets(7, 18, 5, 18);
				getFrmAllocationSummaryINV014ContentPane1().add(
					getstcLblSentence1(),
					constraintsstcLblSentence1);

				java.awt.GridBagConstraints constraintsbtnAllocate =
					new java.awt.GridBagConstraints();
				constraintsbtnAllocate.gridx = 1;
				constraintsbtnAllocate.gridy = 4;
				constraintsbtnAllocate.ipadx = 1;
				constraintsbtnAllocate.ipady = 8;
				constraintsbtnAllocate.insets =
					new java.awt.Insets(10, 33, 24, 2);
				getFrmAllocationSummaryINV014ContentPane1().add(
					getbtnAllocate(),
					constraintsbtnAllocate);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 2;
				constraintsButtonPanel1.gridy = 4;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 30;
				constraintsButtonPanel1.ipady = 20;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(5, 2, 7, 3);
				getFrmAllocationSummaryINV014ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);

				java
					.awt
					.GridBagConstraints constraintschkViewInventoryAllocateReport =
					new java.awt.GridBagConstraints();
				constraintschkViewInventoryAllocateReport.gridx = 3;
				constraintschkViewInventoryAllocateReport.gridy = 4;
				constraintschkViewInventoryAllocateReport.ipadx = 9;
				constraintschkViewInventoryAllocateReport.ipady = 9;
				constraintschkViewInventoryAllocateReport.insets =
					new java.awt.Insets(11, 4, 25, 20);
				getFrmAllocationSummaryINV014ContentPane1().add(
					getchkViewInventoryAllocateReport(),
					constraintschkViewInventoryAllocateReport);

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 3;
				constraintsJScrollPane1.gridwidth = 3;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 542;
				constraintsJScrollPane1.ipady = 118;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(6, 18, 5, 18);
				getFrmAllocationSummaryINV014ContentPane1().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
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
		return ivjFrmAllocationSummaryINV014ContentPane1;
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
				ivjJScrollPane1 = new JScrollPane();
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
				ivjJScrollPane1.setOpaque(true);
				ivjJScrollPane1.setBackground(java.awt.Color.white);
				getJScrollPane1().setViewportView(
					gettblAllocatedItems());
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
		return ivjJScrollPane1;
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
				ivjlblFromEntity = new JLabel();
				ivjlblFromEntity.setName("ivjlblFromEntity");
				ivjlblFromEntity.setPreferredSize(
					new java.awt.Dimension(40, 14));
				ivjlblFromEntity.setText(
					InventoryConstant.TXT_FROM_ENTITY_COLON);
				ivjlblFromEntity.setBounds(52, 40, 109, 14);
				ivjlblFromEntity.setMaximumSize(
					new java.awt.Dimension(40, 14));
				ivjlblFromEntity.setMinimumSize(
					new java.awt.Dimension(40, 14));
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
		return ivjlblFromEntity;
	}

	/**
	 * Return the ivjlblFromEntityId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblFromEntityId()
	{
		if (ivjlblFromEntityId == null)
		{
			try
			{
				ivjlblFromEntityId = new JLabel();
				ivjlblFromEntityId.setName("ivjlblFromEntityId");
				ivjlblFromEntityId.setPreferredSize(
					new java.awt.Dimension(60, 14));
				ivjlblFromEntityId.setText(
					InventoryConstant.TXT_SPECIFIC_FROM_ENTITY);
				ivjlblFromEntityId.setBounds(173, 40, 355, 14);
				ivjlblFromEntityId.setMaximumSize(
					new java.awt.Dimension(60, 14));
				ivjlblFromEntityId.setMinimumSize(
					new java.awt.Dimension(60, 14));
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
		return ivjlblFromEntityId;
	}

	/**
	 * Return the ivjlblFromLoc property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblFromLoc()
	{
		if (ivjlblFromLoc == null)
		{
			try
			{
				ivjlblFromLoc = new JLabel();
				ivjlblFromLoc.setName("ivjlblFromLoc");
				ivjlblFromLoc.setText(
					InventoryConstant.TXT_FROM_LOCATION);
				ivjlblFromLoc.setMaximumSize(
					new java.awt.Dimension(81, 14));
				ivjlblFromLoc.setBounds(52, 20, 280, 14);
				ivjlblFromLoc.setMinimumSize(
					new java.awt.Dimension(81, 14));
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
		return ivjlblFromLoc;
	}

	/**
	 * Return the ivjlblToEntity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblToEntity()
	{
		if (ivjlblToEntity == null)
		{
			try
			{
				ivjlblToEntity = new JLabel();
				ivjlblToEntity.setName("ivjlblToEntity");
				ivjlblToEntity.setPreferredSize(
					new java.awt.Dimension(40, 14));
				ivjlblToEntity.setText(
					InventoryConstant.TXT_TO_ENTITY_COLON);
				ivjlblToEntity.setBounds(52, 80, 109, 14);
				ivjlblToEntity.setMaximumSize(
					new java.awt.Dimension(40, 14));
				ivjlblToEntity.setMinimumSize(
					new java.awt.Dimension(40, 14));
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
		return ivjlblToEntity;
	}

	/**
	 * Return the ivjlblToEntityId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblToEntityId()
	{
		if (ivjlblToEntityId == null)
		{
			try
			{
				ivjlblToEntityId = new JLabel();
				ivjlblToEntityId.setName("ivjlblToEntityId");
				ivjlblToEntityId.setPreferredSize(
					new java.awt.Dimension(60, 14));
				ivjlblToEntityId.setText(
					InventoryConstant.TXT_SPECIFIC_TO_ENTITY);
				ivjlblToEntityId.setBounds(173, 80, 355, 14);
				ivjlblToEntityId.setMaximumSize(
					new java.awt.Dimension(60, 14));
				ivjlblToEntityId.setMinimumSize(
					new java.awt.Dimension(60, 14));
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
		return ivjlblToEntityId;
	}

	/**
	 * Return the ivjlblToLoc property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblToLoc()
	{
		if (ivjlblToLoc == null)
		{
			try
			{
				ivjlblToLoc = new JLabel();
				ivjlblToLoc.setName("ivjlblToLoc");
				ivjlblToLoc.setText(InventoryConstant.TXT_TO_LOCATION);
				ivjlblToLoc.setMaximumSize(
					new java.awt.Dimension(66, 14));
				ivjlblToLoc.setBounds(52, 60, 280, 14);
				ivjlblToLoc.setMinimumSize(
					new java.awt.Dimension(66, 14));
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
		return ivjlblToLoc;
	}

	/**
	 * Return the ivjpnlAlloctnLocs property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlAlloctnLocs()
	{
		if (ivjpnlAlloctnLocs == null)
		{
			try
			{
				ivjpnlAlloctnLocs = new JPanel();
				ivjpnlAlloctnLocs.setName("ivjpnlAlloctnLocs");
				ivjpnlAlloctnLocs.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_ALLOCATION_LOCATIONS));
				ivjpnlAlloctnLocs.setLayout(null);
				ivjpnlAlloctnLocs.setMinimumSize(
					new java.awt.Dimension(504, 69));
				getpnlAlloctnLocs().add(
					getstcLblFrom(),
					getstcLblFrom().getName());
				getpnlAlloctnLocs().add(
					getstcLblTo(),
					getstcLblTo().getName());
				getpnlAlloctnLocs().add(
					getlblFromLoc(),
					getlblFromLoc().getName());
				getpnlAlloctnLocs().add(
					getlblToLoc(),
					getlblToLoc().getName());
				getpnlAlloctnLocs().add(
					getlblFromEntity(),
					getlblFromEntity().getName());
				getpnlAlloctnLocs().add(
					getlblToEntity(),
					getlblToEntity().getName());
				getpnlAlloctnLocs().add(
					getlblFromEntityId(),
					getlblFromEntityId().getName());
				getpnlAlloctnLocs().add(
					getlblToEntityId(),
					getlblToEntityId().getName());
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
		return ivjpnlAlloctnLocs;
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
				ivjstcLblFrom.setMaximumSize(
					new java.awt.Dimension(36, 14));
				ivjstcLblFrom.setBounds(2, 20, 45, 14);
				ivjstcLblFrom.setMinimumSize(
					new java.awt.Dimension(36, 14));
				ivjstcLblFrom.setHorizontalAlignment(4);
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
		return ivjstcLblFrom;
	}

	/**
	 * Return the ivjstcLblSentence1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSentence1()
	{
		if (ivjstcLblSentence1 == null)
		{
			try
			{
				ivjstcLblSentence1 = new JLabel();
				ivjstcLblSentence1.setName("ivjstcLblSentence1");
				ivjstcLblSentence1.setText(
					ErrorsConstant.MSG_ITEMS_SUCCESS_ALLOC);
				ivjstcLblSentence1.setMaximumSize(
					new java.awt.Dimension(455, 14));
				ivjstcLblSentence1.setMinimumSize(
					new java.awt.Dimension(455, 14));
				ivjstcLblSentence1.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
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
		return ivjstcLblSentence1;
	}

	/**
	 * Return the ivjstcLblTo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTo()
	{
		if (ivjstcLblTo == null)
		{
			try
			{
				ivjstcLblTo = new JLabel();
				ivjstcLblTo.setName("ivjstcLblTo");
				ivjstcLblTo.setText(InventoryConstant.TXT_TO_COLON);
				ivjstcLblTo.setMaximumSize(
					new java.awt.Dimension(19, 14));
				ivjstcLblTo.setBounds(0, 60, 45, 14);
				ivjstcLblTo.setMinimumSize(
					new java.awt.Dimension(19, 14));
				ivjstcLblTo.setHorizontalAlignment(4);
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
		return ivjstcLblTo;
	}

	/**
	 * Return the ivjtblAllocatedItems property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblAllocatedItems()
	{
		if (ivjtblAllocatedItems == null)
		{
			try
			{
				ivjtblAllocatedItems = new RTSTable();
				ivjtblAllocatedItems.setName("ivjtblAllocatedItems");
				ivjtblAllocatedItems.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblAllocatedItems.setModel(new TMINV014());
				ivjtblAllocatedItems.setShowVerticalLines(false);
				ivjtblAllocatedItems.setShowHorizontalLines(false);
				ivjtblAllocatedItems.setAutoCreateColumnsFromModel(
					false);
				ivjtblAllocatedItems.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caTableModel =
					(TMINV014) ivjtblAllocatedItems.getModel();
				// defect 8269
				TableColumn laTC1 =
					ivjtblAllocatedItems.getColumn(
						ivjtblAllocatedItems.getColumnName(
							CommonConstant.ELEMENT_0));
				laTC1.setPreferredWidth(185);
				TableColumn laTC2 =
					ivjtblAllocatedItems.getColumn(
						ivjtblAllocatedItems.getColumnName(
							CommonConstant.ELEMENT_1));
				laTC2.setPreferredWidth(35);
				TableColumn laTC3 =
					ivjtblAllocatedItems.getColumn(
						ivjtblAllocatedItems.getColumnName(
							CommonConstant.ELEMENT_2));
				laTC3.setPreferredWidth(60);
				TableColumn laTC4 =
					ivjtblAllocatedItems.getColumn(
						ivjtblAllocatedItems.getColumnName(
							CommonConstant.ELEMENT_3));
				laTC4.setPreferredWidth(85);
				TableColumn laTC5 =
					ivjtblAllocatedItems.getColumn(
						ivjtblAllocatedItems.getColumnName(
							CommonConstant.ELEMENT_4));
				laTC5.setPreferredWidth(85);
				ivjtblAllocatedItems.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblAllocatedItems.init();
				laTC1.setCellRenderer(
					ivjtblAllocatedItems.setColumnAlignment(
						RTSTable.LEFT));
				laTC2.setCellRenderer(
					ivjtblAllocatedItems.setColumnAlignment(
						RTSTable.RIGHT));
				laTC3.setCellRenderer(
					ivjtblAllocatedItems.setColumnAlignment(
						RTSTable.RIGHT));
				laTC4.setCellRenderer(
					ivjtblAllocatedItems.setColumnAlignment(
						RTSTable.RIGHT));
				laTC5.setCellRenderer(
					ivjtblAllocatedItems.setColumnAlignment(
						RTSTable.RIGHT));
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtblAllocatedItems;
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
			setName(ScreenConstant.INV014_FRAME_NAME);
			setSize(600, 375);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setModal(true);
			setTitle(ScreenConstant.INV014_FRAME_TITLE);
			setContentPane(getFrmAllocationSummaryINV014ContentPane1());
			setDefaultFocusField(getbtnAllocate());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Method to see if the cancel button has been disabled.  
	 * Used on the vc to determine whether or not to perform the 
	 * cancel code when the esc key is pressed.
	 * 
	 * @return boolean
	 */
	public boolean isCancelEnabled()
	{
		return !cbDisableCancel;
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

		InventoryAllocationUIData laInvAllocUIData =
			(InventoryAllocationUIData) lvDataIn.elementAt(
				CommonConstant.ELEMENT_1);

		caInvAlloctnUIDataOld =
			(InventoryAllocationUIData) UtilityMethods.copy(
				laInvAllocUIData);
		caInvAlloctnUIData =
			(InventoryAllocationUIData) UtilityMethods.copy(
				laInvAllocUIData);

		if (!caInvAlloctnUIData.getAllocatedData())
		{
			caAlloctnDbData =
				(AllocationDbData) lvDataIn.elementAt(
					CommonConstant.ELEMENT_0);
			// Set the From Allocation Locations
			getlblFromLoc().setText(caInvAlloctnUIDataOld.getFromLoc());
			getlblFromEntityId().setText(
				caInvAlloctnUIDataOld.getFromInvIdName());
			if (caInvAlloctnUIDataOld
				.getFromInvLocIdCd()
				.equals(InventoryConstant.CHAR_C))
			{
				getlblFromEntity().setVisible(false);
				getlblFromEntityId().setVisible(false);

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

			// Set the To Allocation Locations
			getlblToLoc().setText(caInvAlloctnUIDataOld.getToLoc());
			if (caInvAlloctnUIDataOld
				.getInvLocIdCd()
				.equals(InventoryConstant.CHAR_C))
			{
				getlblToEntity().setVisible(false);
				getlblToEntityId().setVisible(false);

			}
			else if (
				caInvAlloctnUIDataOld.getInvLocIdCd().equals(
					InventoryConstant.CHAR_W))
			{
				getlblToEntity().setText(
					InventoryConstant.STR_W.toUpperCase()
						+ CommonConstant.STR_COLON);
				getlblToEntityId().setText(
					caInvAlloctnUIDataOld.getToInvIdName());

			}
			else if (
				caInvAlloctnUIDataOld.getInvLocIdCd().equals(
					InventoryConstant.CHAR_S))
			{
				getlblToEntity().setText(
					InventoryConstant.STR_S.toUpperCase()
						+ CommonConstant.STR_COLON);
				getlblToEntityId().setText(
					caInvAlloctnUIDataOld.getToInvIdName());

			}
			else if (
				caInvAlloctnUIDataOld.getInvLocIdCd().equals(
					InventoryConstant.CHAR_D))
			{
				getlblToEntity().setText(
					InventoryConstant.STR_D.toUpperCase()
						+ CommonConstant.STR_COLON);
				getlblToEntityId().setText(
					caInvAlloctnUIDataOld.getToInvIdName());

			}
			else if (
				caInvAlloctnUIDataOld.getInvLocIdCd().equals(
					InventoryConstant.CHAR_E))
			{
				getlblToEntity().setText(
					InventoryConstant.STR_E.toUpperCase()
						+ CommonConstant.STR_COLON);
				getlblToEntityId().setText(
					caInvAlloctnUIDataOld.getToInvIdName());
			}

			getbtnAllocate().requestFocus();
		}
		else if (caInvAlloctnUIData.getAllocatedData())
		{
			cvAllocatedItms.addElement(caInvAlloctnUIData);
			caTableModel.add(cvAllocatedItms);
		}

		// If an item has been allocated, disable the cancel button 
		// so the user can't cancel
		if (cvAllocatedItms.size() > CommonConstant.ELEMENT_0)
		{
			getchkViewInventoryAllocateReport().setSelected(true);
			getButtonPanel1().getBtnCancel().setEnabled(false);
			cbDisableCancel = true;
		}
	}
}
