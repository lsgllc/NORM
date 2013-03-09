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
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSecurityAccessRightsMiscellaneousSEC011.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/19/2004	5.2.0 Merge/Cleanup.  See PCR 34 comments.
 * 							Version 5.2.0	
 * T Pederson	09/09/2004	Added mnemonic for Reprint Receipt checkbox.
 * 							Defect 7547, Version 5.2.1
 * Min Wang		11/22/2004	Correct misspelling security.
 *							modify initialize() on VCE 
 *							defect 7564 Ver 5.2.2 
 * Min Wang		03/15/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3
 * Min Wang 	04/08/2005	Remove arrays since they are causing 
 * 							initialization errors.
 * 							delete carrRTSBtn, carrBtnSec
 * 							delete checkIfChkSlctdDsbld(), 
 * 							modify disableBtnOnOfcId(), initialize(), 
 * 								keyPressed(), 
 * 								resetSelectAllForEnabledSlctd()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3               
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Min Wang		08/31/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		12/13/2005  Uncomment code for arrow key function.
 * 							modify keyPressed(), initialize()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	04/20/2007	Implemented SystemProperty.isCounty()
 * 							modify disableBtnOnOfcId()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	06/11/2007	Enable Print Immediate for Region (in 
 * 							addition to County) 
 * 							Disable CompleteTransaction for HQ 
 * 							modify disableBtnOnOfcId(), 
 * 							 resetSelectAllForEnabledSlcted()
 * 							defect 9157 Ver Special Plates    
 * ---------------------------------------------------------------------
 */

/**
 * This class is used for managing security access rights for
 * Miscellaneous. Data on this screen is managed through 
 * SecurityClientDataObject.
 *
 * @version	Special Plates	06/11/2007 
 * @author	Administrator
 * <br>Creation Date:		06/28/2001 13:48:38
 */

public class FrmSecurityAccessRightsMiscellaneousSEC011
	extends RTSDialogBox
	implements ActionListener, WindowListener
{
	private JCheckBox ivjchkMiscellaneous = null;
	private JCheckBox ivjchkPrintImmediate = null;
	private JCheckBox ivjchkReprintReceipt = null;
	private JCheckBox ivjchkSetPrintDest = null;
	private JCheckBox ivjchkVoidTransaction = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private JLabel ivjstcLblMiscReg = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1 =
		null;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private JCheckBox ivjchkCompVehicleTrans = null;

	// boolean 
	private boolean cbSelectAll = false;

	// int 
	private int ciEnableCount = 0;

	// Object 
	private SecurityClientDataObject caSecData = null;

	private static final String COMPLETE_VEHICLE_TRANS =
		"Complete Vehicle Transactions";
	private static final String MISC = "Miscellaneous";
	private static final String PRINT_IMMEDIATE = "Print Immediate";
	private static final String REPRINT_RECEIPT = "Reprint Receipt";
	private static final String SET_PRINT_DEST =
		"Set Print Destination";
	private static final String VOID_TRANS = "Void Transaction";
	private static final String LABEL1 = "JLabel1";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String SEC011_FRAME_TITLE =
		"Security Access Rights Miscellaneous   SEC011";
	// defect 7891 
	private RTSButton[] carrRTSBtn = new RTSButton[3];
	private JCheckBox[] carrBtnSec = new JCheckBox[6];
	private int ciSelect = 0;
	private int ciSelectSec = 0;
	// end defect 7891

	/**
	 * FrmSecurityAccessRightsMiscellaneousSEC011 constructor comment.
	 */
	public FrmSecurityAccessRightsMiscellaneousSEC011()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsMiscellaneousSEC011 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsMiscellaneousSEC011(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsMiscellaneousSEC011 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsMiscellaneousSEC011(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsMiscellaneousSEC011 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsMiscellaneousSEC011(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsMiscellaneousSEC011 constructor comment.
	 * 
	 * @param aaOwner  Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsMiscellaneousSEC011(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsMiscellaneousSEC011 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsMiscellaneousSEC011(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsMiscellaneousSEC011 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsMiscellaneousSEC011(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsMiscellaneousSEC011 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsMiscellaneousSEC011(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsMiscellaneousSEC011 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsMiscellaneousSEC011(
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
				caSecData.setSEC011(false);
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
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.SEC011);
				return;
			}
			else if (aaAE.getSource() == getchkMiscellaneous())
			{
				//make sure to reset cbSelectAll to true if all enbld 
				// chks are selected 
				resetSelectAllForEnabledSlctd();
				if (cbSelectAll)
				{
					cbSelectAll = false;
				}
				else
				{
					cbSelectAll = true;
				}
				selectChkButtons(cbSelectAll);
			}
			else if (aaAE.getSource() == getchkCompVehicleTrans())
			{
				if (getchkCompVehicleTrans().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkPrintImmediate())
			{
				if (getchkPrintImmediate().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkReprintReceipt())
			{
				if (getchkReprintReceipt().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkSetPrintDest())
			{
				if (getchkSetPrintDest().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkVoidTransaction())
			{
				if (getchkVoidTransaction().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
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
			getchkMiscellaneous().setSelected(false);
		}
		else
		{
			getchkMiscellaneous().setSelected(true);
		}
	}
	/**
	 * Disable the various checkbox based on various conditions
	 */
	private void disableBtnOnOfcId()
	{
		//getchkReprintReceipt().setEnabled(false);
		getchkVoidTransaction().setEnabled(true);
		getchkCompVehicleTrans().setEnabled(false);
		getchkSetPrintDest().setEnabled(false);
		if (!getchkReprintReceipt().isSelected())
		{
			getchkReprintReceipt().setSelected(false);
			//getchkReprintReceipt().setSelected(true);
			// END PCR 34
			ciEnableCount++;
		}
		if (!getchkSetPrintDest().isSelected())
		{
			getchkSetPrintDest().setSelected(true);
			ciEnableCount++;
		}
		// defect 9157 
		if (!getchkCompVehicleTrans().isSelected()
			& !SystemProperty.isHQ())
		{
			getchkCompVehicleTrans().setSelected(true);
			ciEnableCount++;
		}
		// defect 9085 
		//if (caSecData.getWorkStationType()
		//	== LocalOptionConstant.COUNTY)
		// defect 9157
		// Enable for Region (in addition to County) 
		if (!SystemProperty.isHQ())
		{
			getchkPrintImmediate().setEnabled(true);
			// defect 7891
			// enable boolean if all chkboxes are selected
			// in a county.
			if (ciEnableCount >= 5)
			{
				cbSelectAll = true;
			}
			// end defect 7891
		}
		else
		{
			getchkPrintImmediate().setEnabled(false);
			getchkCompVehicleTrans().setEnabled(false);
			// defect 7891
			// enable the boolean if all chkboxes are selected 
			// in a non-county
			if (ciEnableCount >= 4)
			{
				cbSelectAll = true;
			}
			// end defect 7891
		}
		// end defect 9085 
		// end defect 9157 
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return  ButtonPanel
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
				ivjButtonPanel1.setBounds(87, 297, 402, 85);
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
	 * Return the chkDisabledVehicleTrans property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkCompVehicleTrans()
	{
		if (ivjchkCompVehicleTrans == null)
		{
			try
			{
				ivjchkCompVehicleTrans = new JCheckBox();
				ivjchkCompVehicleTrans.setName("chkCompVehicleTrans");
				ivjchkCompVehicleTrans.setText(COMPLETE_VEHICLE_TRANS);
				ivjchkCompVehicleTrans.setMaximumSize(
					new Dimension(202, 22));
				ivjchkCompVehicleTrans.setMinimumSize(
					new Dimension(202, 22));
				ivjchkCompVehicleTrans.setActionCommand(
					COMPLETE_VEHICLE_TRANS);
				ivjchkCompVehicleTrans.setBounds(170, 167, 218, 22);
				// user code begin {1}
				ivjchkCompVehicleTrans.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkCompVehicleTrans;
	}

	/**
	 * Return the chkMiscellaneous property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkMiscellaneous()
	{
		if (ivjchkMiscellaneous == null)
		{
			try
			{
				ivjchkMiscellaneous = new JCheckBox();
				ivjchkMiscellaneous.setName("chkMiscellaneous");
				ivjchkMiscellaneous.setText(MISC);
				ivjchkMiscellaneous.setMaximumSize(
					new Dimension(107, 22));
				ivjchkMiscellaneous.setMinimumSize(
					new Dimension(107, 22));
				ivjchkMiscellaneous.setActionCommand(MISC);
				ivjchkMiscellaneous.setBounds(170, 103, 218, 22);
				// user code begin {1}
				ivjchkMiscellaneous.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkMiscellaneous;
	}

	/**
	 * Return the chkPrintImmediate property value.
	 * 
	 * @return  JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkPrintImmediate()
	{
		if (ivjchkPrintImmediate == null)
		{
			try
			{
				ivjchkPrintImmediate = new JCheckBox();
				ivjchkPrintImmediate.setName("chkPrintImmediate");
				ivjchkPrintImmediate.setMnemonic('P');
				ivjchkPrintImmediate.setText(PRINT_IMMEDIATE);
				ivjchkPrintImmediate.setMaximumSize(
					new Dimension(115, 22));
				ivjchkPrintImmediate.setActionCommand(PRINT_IMMEDIATE);
				ivjchkPrintImmediate.setBounds(170, 256, 218, 22);
				ivjchkPrintImmediate.setMinimumSize(
					new Dimension(115, 22));
				// user code begin {1}
				ivjchkPrintImmediate.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkPrintImmediate;
	}

	/**
	 * Return the chkReprintReceipt property value.
	 * 
	 * @return  JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkReprintReceipt()
	{
		if (ivjchkReprintReceipt == null)
		{
			try
			{
				ivjchkReprintReceipt = new JCheckBox();
				ivjchkReprintReceipt.setName("chkReprintReceipt");
				ivjchkReprintReceipt.setMnemonic('R');
				ivjchkReprintReceipt.setText(REPRINT_RECEIPT);
				ivjchkReprintReceipt.setMaximumSize(
					new Dimension(112, 22));
				ivjchkReprintReceipt.setActionCommand(REPRINT_RECEIPT);
				ivjchkReprintReceipt.setBounds(170, 135, 218, 22);
				ivjchkReprintReceipt.setMinimumSize(
					new Dimension(112, 22));
				// user code begin {1}
				ivjchkReprintReceipt.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkReprintReceipt;
	}

	/**
	 * Return the chkSetPrintDest property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSetPrintDest()
	{
		if (ivjchkSetPrintDest == null)
		{
			try
			{
				ivjchkSetPrintDest = new JCheckBox();
				ivjchkSetPrintDest.setName("chkSetPrintDest");
				ivjchkSetPrintDest.setText(SET_PRINT_DEST);
				ivjchkSetPrintDest.setMaximumSize(
					new Dimension(141, 22));
				ivjchkSetPrintDest.setMinimumSize(
					new Dimension(141, 22));
				ivjchkSetPrintDest.setActionCommand(SET_PRINT_DEST);
				ivjchkSetPrintDest.setBounds(170, 225, 218, 22);
				// user code begin {1}
				ivjchkSetPrintDest.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSetPrintDest;
	}

	/**
	 * Return the chkVoidTransaction property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkVoidTransaction()
	{
		if (ivjchkVoidTransaction == null)
		{
			try
			{
				ivjchkVoidTransaction = new JCheckBox();
				ivjchkVoidTransaction.setName("chkVoidTransaction");
				ivjchkVoidTransaction.setMnemonic('V');
				ivjchkVoidTransaction.setText(VOID_TRANS);
				ivjchkVoidTransaction.setMaximumSize(
					new Dimension(121, 22));
				ivjchkVoidTransaction.setActionCommand(VOID_TRANS);
				ivjchkVoidTransaction.setBounds(170, 197, 218, 22);
				ivjchkVoidTransaction.setMinimumSize(
					new Dimension(121, 22));
				// user code begin {1}
				ivjchkVoidTransaction.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkVoidTransaction;
	}

	/**
	 * Return the FrmSecurityAccessRightsMiscellaneousSEC011ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1
			== null)
		{
			try
			{
				ivjFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1 =
					new JPanel();
				ivjFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1
					.setName(
					"FrmSecurityAccessRightsMiscellaneousSEC011ContentPane1");
				ivjFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1
					.setMaximumSize(
					new Dimension(470, 311));
				ivjFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1
					.setMinimumSize(
					new Dimension(470, 311));
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getstcLblMiscReg(),
					getstcLblMiscReg().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getchkMiscellaneous(),
					getchkMiscellaneous().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getchkReprintReceipt(),
					getchkReprintReceipt().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getchkCompVehicleTrans(),
					getchkCompVehicleTrans().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getchkVoidTransaction(),
					getchkVoidTransaction().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getchkSetPrintDest(),
					getchkSetPrintDest().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getchkPrintImmediate(),
					getchkPrintImmediate().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
					.add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1()
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
		return ivjFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1;
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
				ivjlblEmployeeFirstName.setBounds(327, 31, 177, 14);
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
	 * @return JLabel
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
				ivjlblEmployeeId.setBounds(144, 8, 68, 14);
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
				ivjlblEmployeeLastName.setBounds(144, 31, 175, 14);
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
				ivjlblEmployeeMName.setText(LABEL1);
				ivjlblEmployeeMName.setBounds(525, 31, 48, 14);
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
				ivjstcLblEmployeeId.setBounds(12, 8, 83, 18);
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
				ivjstcLblEmployeeName.setBounds(12, 31, 113, 14);
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
	 * Return the stcLblMiscReg property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMiscReg()
	{
		if (ivjstcLblMiscReg == null)
		{
			try
			{
				ivjstcLblMiscReg = new JLabel();
				ivjstcLblMiscReg.setName("stcLblMiscReg");
				ivjstcLblMiscReg.setText(MISC);
				ivjstcLblMiscReg.setMaximumSize(new Dimension(82, 14));
				ivjstcLblMiscReg.setMinimumSize(new Dimension(82, 14));
				ivjstcLblMiscReg.setBounds(225, 64, 149, 14);
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
		return ivjstcLblMiscReg;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		//defect 7891
		//System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		//aeEx.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
		//defect 7891
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
			setName("FrmSecurityAccessRightsMiscellaneousSEC011");
			setSize(600, 400);
			setTitle(SEC011_FRAME_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(
				getFrmSecurityAccessRightsMiscellaneousSEC011ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		addWindowListener(this);
		// defect 7891
		carrRTSBtn[0] = getButtonPanel1().getBtnEnter();
		carrRTSBtn[1] = getButtonPanel1().getBtnCancel();
		carrRTSBtn[2] = getButtonPanel1().getBtnHelp();
		carrBtnSec[0] = getchkMiscellaneous();
		carrBtnSec[1] = getchkReprintReceipt();
		carrBtnSec[2] = getchkCompVehicleTrans();
		carrBtnSec[3] = getchkVoidTransaction();
		carrBtnSec[4] = getchkSetPrintDest();
		carrBtnSec[5] = getchkPrintImmediate();
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
			for (int i = 0; i < 6; i++)
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
					if (ciSelectSec > 5)
					{
						ciSelectSec = 0;
					}
					if (ciSelectSec < 6
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
						ciSelectSec = 5;
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
				if (carrRTSBtn[i].hasFocus())
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
					if (ciSelect < 3
						&& carrRTSBtn[ciSelect].isEnabled())
					{
						carrRTSBtn[ciSelect].requestFocus();
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
					if (ciSelect >= 0
						&& carrRTSBtn[ciSelect].isEnabled())
					{
						carrRTSBtn[ciSelect].requestFocus();
						lbStop = false;
					}
				}
				while (lbStop);
			}
		}
		// end defect 7891
		super.keyPressed(aaKE);
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSecurityAccessRightsMiscellaneousSEC011 laFrmSecurityAccessRightsMiscellaneousSEC011;
			laFrmSecurityAccessRightsMiscellaneousSEC011 =
				new FrmSecurityAccessRightsMiscellaneousSEC011();
			laFrmSecurityAccessRightsMiscellaneousSEC011.setModal(true);
			laFrmSecurityAccessRightsMiscellaneousSEC011
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSecurityAccessRightsMiscellaneousSEC011.show();
			Insets insets =
				laFrmSecurityAccessRightsMiscellaneousSEC011
					.getInsets();
			laFrmSecurityAccessRightsMiscellaneousSEC011.setSize(
				laFrmSecurityAccessRightsMiscellaneousSEC011.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSecurityAccessRightsMiscellaneousSEC011
					.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSecurityAccessRightsMiscellaneousSEC011.setVisibleRTS(
				true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * Select all buttons based on various conditions
	 * 
	 * @deprecated 
	 */
	private void resetSelectAllForEnabledSlctd()
	{
		int liEnblCnt = 0;
		int liSlctCnt = 0;
		// defect 9157 
		// No need for loop 
		//for (int i = 1; i < 6; i++)
		//{
		// defect 7891
		// replace array processing with straight if's
		if (getchkCompVehicleTrans().isEnabled())
		{
			liEnblCnt = liEnblCnt + 1;
			if (getchkCompVehicleTrans().isSelected())
			{
				liSlctCnt = liSlctCnt + 1;
			}
		}
		// Misc should not be considered as it will 
		// always be reselected. 
		//		if (getchkMiscellaneous().isEnabled())
		//		{
		//			liEnblCnt = liEnblCnt + 1;
		//			if (getchkMiscellaneous().isSelected())
		//			{
		//				liSlctCnt = liSlctCnt + 1;
		//			}
		//		}
		if (getchkPrintImmediate().isEnabled())
		{
			liEnblCnt = liEnblCnt + 1;
			if (getchkPrintImmediate().isSelected())
			{
				liSlctCnt = liSlctCnt + 1;
			}
		}
		if (getchkReprintReceipt().isEnabled())
		{
			liEnblCnt = liEnblCnt + 1;
			if (getchkReprintReceipt().isSelected())
			{
				liSlctCnt = liSlctCnt + 1;
			}
		}
		if (getchkSetPrintDest().isEnabled())
		{
			liEnblCnt = liEnblCnt + 1;
			if (getchkSetPrintDest().isSelected())
			{
				liSlctCnt = liSlctCnt + 1;
			}
		}
		if (getchkVoidTransaction().isEnabled())
		{
			liEnblCnt = liEnblCnt + 1;
			if (getchkVoidTransaction().isSelected())
			{
				liSlctCnt = liSlctCnt + 1;
			}
		}
		//			if (carrBtnSec[i].isEnabled())
		//			{
		//				liEnblCnt++;
		//			}
		//			if (carrBtnSec[i].isEnabled()
		//				&& carrBtnSec[i].isSelected())
		//				liSlctCnt++;
		//			}
		// end defect 7891
		//		}
		//Means everything is selected
		//		if (liSlctCnt == 0 && liEnblCnt == 0)
		//		{
		//			cbSelectAll = false;
		//		}
		//		else 
		if ((liSlctCnt == liEnblCnt) && cbSelectAll == false)
		{
			cbSelectAll = true;
		}
		// Only Enabled are Reprint & Void 
		else if (liSlctCnt == 0 && liEnblCnt == 2)
		{
			cbSelectAll = false;
		}
		// end defect 9157 
	}

	/**
	 * Sets the various check boxes depending on whether 
	 * they are enabled and updates iEnableCount
	 * 
	 * @param abVal boolean
	 */
	private void selectChkButtons(boolean abVal)
	{
		if (getchkCompVehicleTrans().isEnabled())
		{
			if (abVal && !getchkCompVehicleTrans().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkCompVehicleTrans().isSelected())
			{
				ciEnableCount--;
			}
			getchkCompVehicleTrans().setSelected(abVal);
		}
		if (getchkPrintImmediate().isEnabled())
		{
			if (abVal && !getchkPrintImmediate().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkPrintImmediate().isSelected())
			{
				ciEnableCount--;
			}
			getchkPrintImmediate().setSelected(abVal);
		}
		if (getchkReprintReceipt().isEnabled())
		{
			if (abVal && !getchkReprintReceipt().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkReprintReceipt().isSelected())
			{
				ciEnableCount--;
			}
			getchkReprintReceipt().setSelected(abVal);
		}
		if (getchkSetPrintDest().isEnabled())
		{
			if (abVal && !getchkSetPrintDest().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkSetPrintDest().isSelected())
			{
				ciEnableCount--;
			}
			getchkSetPrintDest().setSelected(abVal);
		}
		if (getchkVoidTransaction().isEnabled())
		{
			if (abVal && !getchkVoidTransaction().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkVoidTransaction().isSelected())
			{
				ciEnableCount--;
			}
			getchkVoidTransaction().setSelected(abVal);
		}
		//if(bVal)
		//	iEnableCount = NUM_CHKBOX;
		//else
		//	iEnableCount = 0;
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
			//Not sure of this
			//if(secData.getMiscRemksAccs() ==1)
			//	getchkMiscellaneous().setSelected(true);
			if (laSecData.getReprntRcptAccs() == 1)
			{
				getchkReprintReceipt().setSelected(true);
				ciEnableCount++;
			}

			if (laSecData.getVoidTransAccs() == 1)
			{
				getchkVoidTransaction().setSelected(true);
				ciEnableCount++;
			}

			//if(secData.get() == 1)

			//{

			//	getVoidTransAccs().setSelected(true);

			//	iEnableCount++;

			//}		

			if (laSecData.getPrntImmedAccs() == 1)
			{
				getchkPrintImmediate().setSelected(true);
				ciEnableCount++;
			}
		}
		//Enable disable check boxes
		disableBtnOnOfcId();
		checkCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Miscellaneous.
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC011(true);
		//Take the new Values fromt the window and set to data object
		caSecData.getSecData().setReprntRcptAccs(
			getIntValue(getchkReprintReceipt().isSelected()));
		caSecData.getSecData().setPrntImmedAccs(
			getIntValue(getchkPrintImmediate().isSelected()));
		caSecData.getSecData().setVoidTransAccs(
			getIntValue(getchkVoidTransaction().isSelected()));
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowActivated(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosed(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.  If the program does not 
	 * explicitly hide or dispose the window while processing 
	 * this event, the window close operation will be cancelled.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowClosing(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is no longer the user's active
	 * window, which means that keyboard events will no longer
	 * be delivered to the window or its subcomponents.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowDeactivated(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowDeiconified(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window 
	 * is displayed as the icon specified in the window's 
	 * iconImage property.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowIconified(WindowEvent aaWE)
	{
		//empty code block
	}

	/**
	 * Invoked the first time a window is made visible.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowOpened(WindowEvent aaWE)
	{
		//WindowEvent aaWE
	}
}
