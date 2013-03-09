package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.SecurityClientDataObject;
import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 *
 * FrmSecurityAccessRightsInventorySEC015.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		05/17/2002	Implement Help
 * 							defect 3983  
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		03/15/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3 
 * Min Wang		04/06/2005	Remove arrays because they are causing 
 * 							initialization errors
 * 							delete carrBtn, carrBtnSec
 * 							modify initialize(), keyPressed()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	remove unused method
 * 							delete setController()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3             
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Min Wang		08/30/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		12/13/2005  Uncomment code for arrow key function.
 * 							modify keyPressed(), initialize()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	01/03/2006	Alignment work for checkboxes; Mnemonic work.
 * 							defect 7891,8400 Ver 5.2.3
 * M Reyes		02/12/2007	Changes for Special plates
 * 							comment out disableBtnOnOfcId()
 * 							defect 9124 Ver Special Plates
 * M Reyes		06/13/2007	Changes for Special Plates. Only enable
 * 							Inquiry for HQ. 
 * 							defect 9124 Ver Special Plates 
 * --------------------------------------------------------------------- 
 */

/**
 * This class is used for managing security access rights for
 * Inquiry. Data on this screen is managed through 
 * SecurityClientDataObject.
 * 
 * @version	Special Plates	06/13/2007 
 * @author	Administrator
 * <br>Creation Date:		06/28/2001 17:33:09
 */

public class FrmSecurityAccessRightsInventorySEC015
	extends RTSDialogBox
	implements ActionListener
{
	private JCheckBox ivjchkAllocate = null;
	private JCheckBox ivjchkDelete = null;
	private JCheckBox ivjchkHoldRelease = null;
	private JCheckBox ivjchkInquiry = null;
	private JCheckBox ivjchkInvActionReport = null;
	private JCheckBox ivjchkInventory = null;
	private JCheckBox ivjchkProfRprtUpdt = null;
	private JCheckBox ivjchkReceiveInvoice = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private JLabel ivjstcLblInventory = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmSecurityAccessRightsInventorySEC015ContentPane1 =
		null;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;

	//boolean 
	private boolean cbSelectAll = false;

	// int 	
	private int ciEnableCount = 0;

	// Object 
	private SecurityClientDataObject caSecData = null;

	private static final String ALLOCATE = "Allocate";
	private static final String DELETE = "Delete";
	private static final String HOLD_RELEASE = "Hold/Release";
	private static final String INQUIRY = "Inquiry";
	private static final String INV_ACTION_RPT =
		"Inventory Action Report";
	private static final String INVENTORY = "Inventory";
	private static final String PROFILE_RPT_UPDT =
		"Profile Report & Update";
	private static final String RECEIVE_INVOICE = "Receive Invoice";
	private static final String LABEL1 = "JLabel1";
	private static final String LABEL2 = "JLabel2";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String SEC015_FRAME_TITLE =
		"Security Access Rights Inventory   SEC015";
	// defect 7891
	private RTSButton[] carrBtn = new RTSButton[3];
	private JCheckBox[] carrBtnSec = new JCheckBox[8];
	private int ciSelect = 0;
	private int ciSelectSec = 0;
	// end defect 7891

	/**
	 * FrmSecurityAccessRightsInventorySEC015 constructor comment.
	 */
	public FrmSecurityAccessRightsInventorySEC015()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsInventorySEC015 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsInventorySEC015(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsInventorySEC015 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param title String
	 */
	public FrmSecurityAccessRightsInventorySEC015(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsInventorySEC015 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsInventorySEC015(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsInventorySEC015 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsInventorySEC015(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsInventorySEC015 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsInventorySEC015(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsInventorySEC015 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsInventorySEC015(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsInventorySEC015 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsInventorySEC015(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsInventorySEC015 constructor comment.
	 * 
	 * @param owner Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsInventorySEC015(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * Invoked when an action occurs.
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
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				caSecData.setSEC015(false);
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
				return;
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				setValuesToDataObj();
				getController().processData(
					AbstractViewController.ENTER,
					caSecData);
				return;
			}
			else if (aaAE.getSource() == getchkInventory())
			{
				if (!getchkInventory().isSelected())
				{
					cbSelectAll = false;
				}
				else
				{
					cbSelectAll = true;
				}
				selectChkButtons(cbSelectAll);
			}
			else if (aaAE.getSource() == getchkAllocate())
			{
				if (getchkAllocate().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkDelete())
			{
				if (getchkDelete().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkHoldRelease())
			{
				if (getchkHoldRelease().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkInquiry())
			{
				if (getchkInquiry().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkInvActionReport())
			{
				if (getchkInvActionReport().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkProfRprtUpdt())
			{
				if (getchkProfRprtUpdt().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkReceiveInvoice())
			{
				if (getchkReceiveInvoice().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.SEC015);
				return;
			}
			checkCountZero();
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Check for EnableCount field for zero value and 
	 * set the LocalOptions checkbox
	 */
	private void checkCountZero()
	{
		if (ciEnableCount == 0)
		{
			getchkInventory().setSelected(false);
		}
		else
		{
			getchkInventory().setSelected(true);
		}
	}
	//	defect 9124
	private void disableBtnOnOfcId()
	{
		getchkAllocate().setEnabled(!SystemProperty.isHQ());
		getchkProfRprtUpdt().setEnabled(!SystemProperty.isHQ());
		getchkReceiveInvoice().setEnabled(!SystemProperty.isHQ());
		getchkHoldRelease().setEnabled(!SystemProperty.isHQ());
		getchkDelete().setEnabled(!SystemProperty.isHQ());
		getchkInvActionReport().setEnabled(!SystemProperty.isHQ());
	}
	// end defect 9124

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
				ivjButtonPanel1.setBounds(113, 343, 377, 73);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
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
	 * Return the chkAllocate property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkAllocate()
	{
		if (ivjchkAllocate == null)
		{
			try
			{
				ivjchkAllocate = new JCheckBox();
				ivjchkAllocate.setName("chkAllocate");
				ivjchkAllocate.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				ivjchkAllocate.setText(ALLOCATE);
				ivjchkAllocate.setMaximumSize(new Dimension(71, 22));
				ivjchkAllocate.setActionCommand(ALLOCATE);
				ivjchkAllocate.setBounds(185, 115, 180, 22);
				ivjchkAllocate.setMinimumSize(new Dimension(71, 22));
				// user code begin {1}
				ivjchkAllocate.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkAllocate;
	}

	/**
	 * Return the chkDelete property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDelete()
	{
		if (ivjchkDelete == null)
		{
			try
			{
				ivjchkDelete = new javax.swing.JCheckBox();
				ivjchkDelete.setName("chkDelete");
				ivjchkDelete.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjchkDelete.setText(DELETE);
				ivjchkDelete.setMaximumSize(new Dimension(61, 22));
				ivjchkDelete.setActionCommand(DELETE);
				ivjchkDelete.setBounds(185, 250, 180, 22);
				ivjchkDelete.setMinimumSize(new Dimension(61, 22));
				// user code begin {1}
				ivjchkDelete.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDelete;
	}

	/**
	 * Return the chkHoldRelease property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkHoldRelease()
	{
		if (ivjchkHoldRelease == null)
		{
			try
			{
				ivjchkHoldRelease = new javax.swing.JCheckBox();
				ivjchkHoldRelease.setName("chkHoldRelease");
				ivjchkHoldRelease.setMnemonic(
					java.awt.event.KeyEvent.VK_O);
				ivjchkHoldRelease.setText(HOLD_RELEASE);
				ivjchkHoldRelease.setMaximumSize(new Dimension(99, 22));
				ivjchkHoldRelease.setActionCommand(HOLD_RELEASE);
				ivjchkHoldRelease.setBounds(185, 223, 180, 22);
				ivjchkHoldRelease.setMinimumSize(new Dimension(99, 22));
				// user code begin {1}
				ivjchkHoldRelease.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkHoldRelease;
	}

	/**
	 * Return the chkInquiry property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkInquiry()
	{
		if (ivjchkInquiry == null)
		{
			try
			{
				ivjchkInquiry = new javax.swing.JCheckBox();
				ivjchkInquiry.setName("chkInquiry");
				ivjchkInquiry.setMnemonic(java.awt.event.KeyEvent.VK_Q);
				ivjchkInquiry.setText(INQUIRY);
				ivjchkInquiry.setMaximumSize(new Dimension(63, 22));
				ivjchkInquiry.setActionCommand(INQUIRY);
				ivjchkInquiry.setBounds(185, 142, 180, 22);
				ivjchkInquiry.setMinimumSize(new Dimension(63, 22));
				// user code begin {1}
				ivjchkInquiry.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkInquiry;
	}

	/**
	 * Return the chkInvActionReport property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkInvActionReport()
	{
		if (ivjchkInvActionReport == null)
		{
			try
			{
				ivjchkInvActionReport = new JCheckBox();
				ivjchkInvActionReport.setName("chkInvActionReport");
				ivjchkInvActionReport.setMnemonic(
					java.awt.event.KeyEvent.VK_Y);
				ivjchkInvActionReport.setText(INV_ACTION_RPT);
				ivjchkInvActionReport.setMaximumSize(
					new Dimension(157, 22));
				ivjchkInvActionReport.setActionCommand(INV_ACTION_RPT);
				ivjchkInvActionReport.setBounds(185, 277, 180, 22);
				ivjchkInvActionReport.setMinimumSize(
					new Dimension(157, 22));
				// user code begin {1}
				ivjchkInvActionReport.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkInvActionReport;
	}

	/**
	 * Return the chkInventory property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkInventory()
	{
		if (ivjchkInventory == null)
		{
			try
			{
				ivjchkInventory = new JCheckBox();
				ivjchkInventory.setName("chkInventory");
				ivjchkInventory.setMnemonic(
					java.awt.event.KeyEvent.VK_I);
				ivjchkInventory.setText(INVENTORY);
				ivjchkInventory.setMaximumSize(new Dimension(77, 22));
				ivjchkInventory.setActionCommand(INVENTORY);
				ivjchkInventory.setBounds(185, 88, 180, 22);
				ivjchkInventory.setMinimumSize(new Dimension(77, 22));
				// user code begin {1}
				ivjchkInventory.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkInventory;
	}

	/**
	 * Return the chkProfRprtUpdt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkProfRprtUpdt()
	{
		if (ivjchkProfRprtUpdt == null)
		{
			try
			{
				ivjchkProfRprtUpdt = new JCheckBox();
				ivjchkProfRprtUpdt.setName("chkProfRprtUpdt");
				ivjchkProfRprtUpdt.setMnemonic(
					java.awt.event.KeyEvent.VK_P);
				ivjchkProfRprtUpdt.setText(PROFILE_RPT_UPDT);
				ivjchkProfRprtUpdt.setMaximumSize(
					new Dimension(158, 22));
				ivjchkProfRprtUpdt.setActionCommand(PROFILE_RPT_UPDT);
				ivjchkProfRprtUpdt.setBounds(185, 169, 180, 22);
				ivjchkProfRprtUpdt.setMinimumSize(
					new Dimension(158, 22));
				// user code begin {1}
				ivjchkProfRprtUpdt.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkProfRprtUpdt;
	}

	/**
	 * Return the chkReceiveInvoice property value.
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkReceiveInvoice()
	{
		if (ivjchkReceiveInvoice == null)
		{
			try
			{
				ivjchkReceiveInvoice = new JCheckBox();
				ivjchkReceiveInvoice.setName("chkReceiveInvoice");
				ivjchkReceiveInvoice.setMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjchkReceiveInvoice.setText(RECEIVE_INVOICE);
				ivjchkReceiveInvoice.setMaximumSize(
					new Dimension(113, 22));
				ivjchkReceiveInvoice.setActionCommand(RECEIVE_INVOICE);
				ivjchkReceiveInvoice.setBounds(185, 196, 180, 22);
				ivjchkReceiveInvoice.setMinimumSize(
					new Dimension(113, 22));
				// user code begin {1}
				ivjchkReceiveInvoice.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkReceiveInvoice;
	}

	/**
	 * Return the FrmSecurityAccessRightsInventorySEC015ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsInventorySEC015ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsInventorySEC015ContentPane1
			== null)
		{
			try
			{
				ivjFrmSecurityAccessRightsInventorySEC015ContentPane1 =
					new JPanel();
				ivjFrmSecurityAccessRightsInventorySEC015ContentPane1
					.setName(
					"FrmSecurityAccessRightsInventorySEC015ContentPane1");
				ivjFrmSecurityAccessRightsInventorySEC015ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsInventorySEC015ContentPane1
					.setMaximumSize(
					new Dimension(477, 392));
				ivjFrmSecurityAccessRightsInventorySEC015ContentPane1
					.setMinimumSize(
					new Dimension(477, 392));
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getstcLblInventory(),
					getstcLblInventory().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getchkInventory(),
					getchkInventory().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getchkAllocate(),
					getchkAllocate().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getchkInquiry(),
					getchkInquiry().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getchkProfRprtUpdt(),
					getchkProfRprtUpdt().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getchkReceiveInvoice(),
					getchkReceiveInvoice().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getchkHoldRelease(),
					getchkHoldRelease().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getchkDelete(),
					getchkDelete().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getchkInvActionReport(),
					getchkInvActionReport().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getFrmSecurityAccessRightsInventorySEC015ContentPane1()
					.add(
					getlblEmployeeMName(),
					getlblEmployeeMName().getName());
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
		return ivjFrmSecurityAccessRightsInventorySEC015ContentPane1;
	}

	/**
	 * Get the int equivalent for the boolean parameter
	 * Returns 1 if parameter is true or returns 0.
	 * 
	 * @return int
	 * @param abVal boolean
	 */
	private int getIntValue(boolean abVal)
	{
		int liReturn = 0;
		if (abVal == true)
		{
			liReturn = 1;
		}
		return liReturn;
	}

	/**
	 * Return the lblEmployeeFirstName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEmployeeFirstName()
	{
		if (ivjlblEmployeeFirstName == null)
		{
			try
			{
				ivjlblEmployeeFirstName = new JLabel();
				ivjlblEmployeeFirstName.setName("lblEmployeeFirstName");
				ivjlblEmployeeFirstName.setText(LABEL1);
				ivjlblEmployeeFirstName.setBounds(347, 33, 184, 14);
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
		return ivjlblEmployeeFirstName;
	}

	/**
	 * Return the lblEmployeeId property value.
	 * 
	 * @returnv JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEmployeeId()
	{
		if (ivjlblEmployeeId == null)
		{
			try
			{
				ivjlblEmployeeId = new JLabel();
				ivjlblEmployeeId.setName("lblEmployeeId");
				ivjlblEmployeeId.setText(LABEL1);
				ivjlblEmployeeId.setMaximumSize(new Dimension(45, 14));
				ivjlblEmployeeId.setMinimumSize(new Dimension(45, 14));
				ivjlblEmployeeId.setBounds(166, 10, 93, 14);
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
		return ivjlblEmployeeId;
	}

	/**
	 * Return the lblEmployeeName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEmployeeLastName()
	{
		if (ivjlblEmployeeLastName == null)
		{
			try
			{
				ivjlblEmployeeLastName = new JLabel();
				ivjlblEmployeeLastName.setName("lblEmployeeLastName");
				ivjlblEmployeeLastName.setText(LABEL1);
				ivjlblEmployeeLastName.setMaximumSize(
					new Dimension(45, 14));
				ivjlblEmployeeLastName.setMinimumSize(
					new Dimension(45, 14));
				ivjlblEmployeeLastName.setBounds(166, 33, 170, 14);
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
		return ivjlblEmployeeLastName;
	}

	/**
	 * Return the lblEmployeeMName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblEmployeeMName()
	{
		if (ivjlblEmployeeMName == null)
		{
			try
			{
				ivjlblEmployeeMName = new javax.swing.JLabel();
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText(LABEL2);
				ivjlblEmployeeMName.setBounds(540, 33, 49, 14);
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
		return ivjlblEmployeeMName;
	}

	/**
	 * Return the stcLblEmployeeId property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEmployeeId()
	{
		if (ivjstcLblEmployeeId == null)
		{
			try
			{
				ivjstcLblEmployeeId = new JLabel();
				ivjstcLblEmployeeId.setName("stcLblEmployeeId");
				ivjstcLblEmployeeId.setText(EMP_ID);
				ivjstcLblEmployeeId.setMaximumSize(
					new Dimension(71, 14));
				ivjstcLblEmployeeId.setMinimumSize(
					new Dimension(71, 14));
				ivjstcLblEmployeeId.setBounds(25, 10, 83, 18);
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
		return ivjstcLblEmployeeId;
	}

	/**
	 * Return the stcLblEmployeeName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEmployeeName()
	{
		if (ivjstcLblEmployeeName == null)
		{
			try
			{
				ivjstcLblEmployeeName = new JLabel();
				ivjstcLblEmployeeName.setName("stcLblEmployeeName");
				ivjstcLblEmployeeName.setText(EMP_NAME);
				ivjstcLblEmployeeName.setMaximumSize(
					new Dimension(94, 14));
				ivjstcLblEmployeeName.setMinimumSize(
					new Dimension(94, 14));
				ivjstcLblEmployeeName.setBounds(25, 33, 113, 14);
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
		return ivjstcLblEmployeeName;
	}

	/**
	 * Return the stcLblInventory property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblInventory()
	{
		if (ivjstcLblInventory == null)
		{
			try
			{
				ivjstcLblInventory = new javax.swing.JLabel();
				ivjstcLblInventory.setName("stcLblInventory");
				ivjstcLblInventory.setText(INVENTORY);
				ivjstcLblInventory.setMaximumSize(
					new Dimension(52, 14));
				ivjstcLblInventory.setMinimumSize(
					new Dimension(52, 14));
				ivjstcLblInventory.setBounds(271, 61, 114, 14);
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
		return ivjstcLblInventory;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
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
			setName("FrmSecurityAccessRightsInventorySEC015");
			setSize(600, 430);
			setTitle(SEC015_FRAME_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(
				getFrmSecurityAccessRightsInventorySEC015ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// defect 7891
		carrBtn[0] = getButtonPanel1().getBtnEnter();
		carrBtn[1] = getButtonPanel1().getBtnCancel();
		carrBtn[2] = getButtonPanel1().getBtnHelp();
		carrBtnSec[0] = getchkInventory();
		carrBtnSec[1] = getchkAllocate();
		carrBtnSec[2] = getchkInquiry();
		carrBtnSec[3] = getchkProfRprtUpdt();
		carrBtnSec[4] = getchkReceiveInvoice();
		carrBtnSec[5] = getchkHoldRelease();
		carrBtnSec[6] = getchkDelete();
		carrBtnSec[7] = getchkInvActionReport();
		// end defect 7891
		// user code end
	}

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent 
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		// defect 7891 
		if (aaKE.getSource() instanceof JCheckBox)
		{
			ciSelectSec = 0;
			for (int i = 0; i < 8; i++)
			{
				if (carrBtnSec[i].hasFocus())
				{
					ciSelectSec = i;
					break;
				}
			}
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
			{
				boolean lbStop = true;
				do
				{
					ciSelectSec++;
					if (ciSelectSec > 7)
					{
						ciSelectSec = 0;
					}
					if (ciSelectSec < 8
						&& carrBtnSec[ciSelectSec].isEnabled())
					{
						carrBtnSec[ciSelectSec].requestFocus();
						lbStop = false;
					}
				}
				while (lbStop);
			}
			else if (
				aaKE.getKeyCode() == KeyEvent.VK_LEFT
					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				boolean lbStop = true;
				do
				{
					ciSelectSec--;
					if (ciSelectSec < 0)
					{
						ciSelectSec = 7;
					}
					if (ciSelectSec >= 0
						&& carrBtnSec[ciSelectSec].isEnabled())
					{
						carrBtnSec[ciSelectSec].requestFocus();
						lbStop = false;
					}
				}
				while (lbStop);
			}
		}
		else if (aaKE.getSource() instanceof JButton)
		{
			ciSelect = 0;
			for (int i = 0; i < 3; i++)
			{
				if (carrBtn[i].hasFocus())
				{
					ciSelect = i;
					break;
				}
			}
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
			{
				boolean lbStop = true;
				do
				{
					ciSelect++;
					if (ciSelect > 2)
					{
						ciSelect = 0;
					}
					if (ciSelect < 3 && carrBtn[ciSelect].isEnabled())
					{
						carrBtn[ciSelect].requestFocus();
						lbStop = false;
					}
				}
				while (lbStop);
			}
			else if (
				aaKE.getKeyCode() == KeyEvent.VK_LEFT
					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
			{
				boolean lbStop = true;
				do
				{
					ciSelect--;
					if (ciSelect < 0)
					{
						ciSelect = 2;
					}
					if (ciSelect >= 0 && carrBtn[ciSelect].isEnabled())
					{
						carrBtn[ciSelect].requestFocus();
						lbStop = false;
					}
				}
				while (lbStop);
			}
		}
		super.keyPressed(aaKE);
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
			FrmSecurityAccessRightsInventorySEC015 laFrmSecurityAccessRightsInventorySEC015;
			laFrmSecurityAccessRightsInventorySEC015 =
				new FrmSecurityAccessRightsInventorySEC015();
			laFrmSecurityAccessRightsInventorySEC015.setModal(true);
			laFrmSecurityAccessRightsInventorySEC015
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSecurityAccessRightsInventorySEC015.show();
			Insets insets =
				laFrmSecurityAccessRightsInventorySEC015.getInsets();
			laFrmSecurityAccessRightsInventorySEC015.setSize(
				laFrmSecurityAccessRightsInventorySEC015.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSecurityAccessRightsInventorySEC015.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSecurityAccessRightsInventorySEC015.setVisibleRTS(
				true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * Sets the various check boxes depending on whether 
	 * they are enabled and updates iEnableCount
	 *  
	 * @param abVal boolean
	 */
	private void selectChkButtons(boolean abVal)
	{
		if (getchkAllocate().isEnabled())
		{
			if (abVal && !getchkAllocate().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkAllocate().isSelected())
			{
				ciEnableCount--;
			}
			getchkAllocate().setSelected(abVal);
		}
		if (getchkDelete().isEnabled())
		{
			if (abVal && !getchkDelete().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkDelete().isSelected())
			{
				ciEnableCount--;
			}
			getchkDelete().setSelected(abVal);
		}
		if (getchkHoldRelease().isEnabled())
		{
			if (abVal && !getchkHoldRelease().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkHoldRelease().isSelected())
			{
				ciEnableCount--;
			}
			getchkHoldRelease().setSelected(abVal);
		}
		if (getchkInquiry().isEnabled())
		{
			if (abVal && !getchkInquiry().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkInquiry().isSelected())
			{
				ciEnableCount--;
			}
			getchkInquiry().setSelected(abVal);
		}
		if (getchkInvActionReport().isEnabled())
		{
			if (abVal && !getchkInvActionReport().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkInvActionReport().isSelected())
			{
				ciEnableCount--;
			}
			getchkInvActionReport().setSelected(abVal);
		}
		if (getchkProfRprtUpdt().isEnabled())
		{
			if (abVal && !getchkProfRprtUpdt().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkProfRprtUpdt().isSelected())
			{
				ciEnableCount--;
			}
			getchkProfRprtUpdt().setSelected(abVal);
		}
		if (getchkReceiveInvoice().isEnabled())
		{
			if (abVal && !getchkReceiveInvoice().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkReceiveInvoice().isSelected())
			{
				ciEnableCount--;
			}
			getchkReceiveInvoice().setSelected(abVal);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view.
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			caSecData = (SecurityClientDataObject) aaDataObject;
			SecurityData laSecData =
				(SecurityData) caSecData.getSecData();
			getlblEmployeeId().setText(laSecData.getEmpId());
			getlblEmployeeLastName().setText(
				laSecData.getEmpLastName());
			getlblEmployeeFirstName().setText(
				laSecData.getEmpFirstName());
			getlblEmployeeMName().setText(laSecData.getEmpMI());
			disableBtnOnOfcId();
			if (laSecData.getInvAccs() == 1)
			{
				getchkInventory().setSelected(true);
			}
			if (laSecData.getInvAllocAccs() == 1)
			{
				getchkAllocate().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getInvInqAccs() == 1)
			{
				getchkInquiry().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getInvProfileAccs() == 1)
			{
				getchkProfRprtUpdt().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getInvAckAccs() == 1)
			{
				getchkReceiveInvoice().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getInvHldRlseAccs() == 1)
			{
				getchkHoldRelease().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getInvDelAccs() == 1)
			{
				getchkDelete().setSelected(true);
				ciEnableCount++;
			}
			/*	
			if(secData.getInvAdjAccs() == 1)
			{
				getchkAdjustment().setSelected(true);
				iEnableCount++;
			}
			*/
			if (laSecData.getInvActionAccs() == 1)
			{
				getchkInvActionReport().setSelected(true);
				ciEnableCount++;
			}
		}
		checkCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Inventory. 
	 *  
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC015(true);
		//Take the new Values fromt the window and set to data object
		caSecData.getSecData().setInvAllocAccs(
			getIntValue(getchkAllocate().isSelected()));
		caSecData.getSecData().setInvDelAccs(
			getIntValue(getchkDelete().isSelected()));
		caSecData.getSecData().setInvHldRlseAccs(
			getIntValue(getchkHoldRelease().isSelected()));
		caSecData.getSecData().setInvInqAccs(
			getIntValue(getchkInquiry().isSelected()));
		caSecData.getSecData().setInvActionAccs(
			getIntValue(getchkInvActionReport().isSelected()));
		caSecData.getSecData().setInvAccs(
			getIntValue(getchkInventory().isSelected()));
		caSecData.getSecData().setInvProfileAccs(
			getIntValue(getchkProfRprtUpdt().isSelected()));
		caSecData.getSecData().setInvAckAccs(
			getIntValue(getchkReceiveInvoice().isSelected()));
	}
}
