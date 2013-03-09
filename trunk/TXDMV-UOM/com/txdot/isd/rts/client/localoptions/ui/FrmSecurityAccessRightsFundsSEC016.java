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
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 *
 * FrmSecurityAccessRightsFundsSEC016.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		03/11/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3 
 * Min Wang 	04/06/2005	Remove the arrays since they are causing 
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
 * Min Wang		10/28/2005  Uncomment code for arrow key function.
 * 							modify keyPressed()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	04/20/2007	Implemented SystemProperty.isHQ() 
 * 							modify disableBtnOnOfcId()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/17/2008	Enable Funds Balance Rpt for HQ 
 * 							modify disableBtnOnOfcId() 
 * 							defect 9653 Ver Defect POS A        
 * ---------------------------------------------------------------------
 */

/**
 * This class is used for managing security access rights for
 * Funds. Data on this screen is managed through 
 * SecurityClientDataObject.
 * 
 * @version	Defect POS A	05/17/2008
 * @author	Administrator
 * <br>Creation Date:		06/28/2001 17:40:18  
 */
public class FrmSecurityAccessRightsFundsSEC016
	extends RTSDialogBox
	implements ActionListener
{
	private JCheckBox ivjchkCashDrawerOpt = null;
	private JCheckBox ivjchkFunds = null;
	private JCheckBox ivjchkFundsBalanceRprt = null;
	private JCheckBox ivjchkFundsMngmt = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private JLabel ivjstcLblFunds = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmSecurityAccessRightsFundsSEC016ContentPane1 =
		null;
	private int ciEnableCount = 0;
	//private final static int NUM_CHKBOX = 3;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private SecurityClientDataObject caSecData = null;
	private boolean cbSelectAll = false;
	// defect 7891
	private RTSButton[] carrBtn = new RTSButton[3];
	private JCheckBox[] carrBtnSec = new JCheckBox[4];
	// end defect 7891
	private int ciSelect = 0;
	private int ciSelectSec = 0;
	private static final String CASH_DRAWER_OPERATION =
		"Cash Drawer Operations";
	private static final String FUNDS = "Funds";
	private static final String FUND_BAL_RPT = "Funds Balance Reports";
	private static final String FUND_MANAGEMENT = "Funds Management";
	private static final String JLABEL1 = "JLabel1";
	private static final String JLABEL2 = "JLabel2";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String SEC016_FRAME_TITLE =
		"Security Access Rights Funds   SEC016";

	/**
	 * FrmSecurityAccessRightsFundsSEC016 constructor comment.
	 */
	public FrmSecurityAccessRightsFundsSEC016()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsFundsSEC016 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsFundsSEC016(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsFundsSEC016 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsFundsSEC016(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsFundsSEC016 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param cbModal boolean
	 */
	public FrmSecurityAccessRightsFundsSEC016(
		java.awt.Dialog aaOwner,
		String asTitle,
		boolean cbModal)
	{
		super(aaOwner, asTitle, cbModal);
	}

	/**
	 * FrmSecurityAccessRightsFundsSEC016 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param cbModal boolean
	 */
	public FrmSecurityAccessRightsFundsSEC016(
		Dialog aaOwner,
		boolean cbModal)
	{
		super(aaOwner, cbModal);
	}

	/**
	 * FrmSecurityAccessRightsFundsSEC016 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsFundsSEC016(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsFundsSEC016 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsFundsSEC016(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsFundsSEC016 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsFundsSEC016(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsFundsSEC016 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsFundsSEC016(
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
				caSecData.setSEC016(false);
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
				RTSHelp.displayHelp(RTSHelp.SEC016);
				return;
			}
			else if (aaAE.getSource() == getchkFunds())
			{
				if (!getchkFunds().isSelected())
				{
					cbSelectAll = false;
				}
				else
				{
					cbSelectAll = true;
				}
				selectChkButtons(cbSelectAll);
			}
			else if (aaAE.getSource() == getchkCashDrawerOpt())
			{
				if (getchkCashDrawerOpt().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkFundsBalanceRprt())
			{
				if (getchkFundsBalanceRprt().isSelected())
				{
					ciEnableCount++;
				}
				else
				{
					ciEnableCount--;
				}
			}
			else if (aaAE.getSource() == getchkFundsMngmt())
			{
				if (getchkFundsMngmt().isSelected())
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
			getchkFunds().setSelected(false);
		}
		else
		{
			getchkFunds().setSelected(true);
		}
	}

	/**
	 * Disable the various checkbox for VTR
	 */
	private void disableBtnOnOfcId()
	{
		// defect 9085 
		//if (caSecData.getWorkStationType() == LocalOptionConstant.VTR)
		if (SystemProperty.isHQ()) 
		{
			getchkCashDrawerOpt().setEnabled(false);
			// defect 9653
			//getchkFunds().setEnabled(false);
			// HQ can now use Funds / Funds Balance Reports  
			//getchkFundsBalanceRprt().setEnabled(false);
			// end defect 9653
			getchkFundsMngmt().setEnabled(false);
		}
		// end defect 9085 
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
				ivjButtonPanel1.setBounds(109, 301, 385, 73);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
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
	 * Return the chkCashDrawerOpt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkCashDrawerOpt()
	{
		if (ivjchkCashDrawerOpt == null)
		{
			try
			{
				ivjchkCashDrawerOpt = new JCheckBox();
				ivjchkCashDrawerOpt.setName("chkCashDrawerOpt");
				ivjchkCashDrawerOpt.setMnemonic('s');
				ivjchkCashDrawerOpt.setText(CASH_DRAWER_OPERATION);
				ivjchkCashDrawerOpt.setMaximumSize(
					new Dimension(165, 22));
				ivjchkCashDrawerOpt.setActionCommand(
					CASH_DRAWER_OPERATION);
				ivjchkCashDrawerOpt.setBounds(180, 147, 186, 22);
				ivjchkCashDrawerOpt.setMinimumSize(
					new Dimension(165, 22));
				// user code begin {1}
				ivjchkCashDrawerOpt.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkCashDrawerOpt;
	}

	/**
	 * Return the chkFunds property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkFunds()
	{
		if (ivjchkFunds == null)
		{
			try
			{
				ivjchkFunds = new JCheckBox();
				ivjchkFunds.setName("chkFunds");
				ivjchkFunds.setMnemonic('f');
				ivjchkFunds.setText(FUNDS);
				ivjchkFunds.setMaximumSize(new Dimension(59, 22));
				ivjchkFunds.setActionCommand(FUNDS);
				ivjchkFunds.setBounds(180, 101, 186, 22);
				ivjchkFunds.setMinimumSize(new Dimension(59, 22));
				// user code begin {1}
				ivjchkFunds.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkFunds;
	}

	/**
	 * Return the chkFundsBalanceRprt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkFundsBalanceRprt()
	{
		if (ivjchkFundsBalanceRprt == null)
		{
			try
			{
				ivjchkFundsBalanceRprt = new javax.swing.JCheckBox();
				ivjchkFundsBalanceRprt.setName("chkFundsBalanceRprt");
				ivjchkFundsBalanceRprt.setMnemonic('u');
				ivjchkFundsBalanceRprt.setText(FUND_BAL_RPT);
				ivjchkFundsBalanceRprt.setMaximumSize(
					new Dimension(156, 22));
				ivjchkFundsBalanceRprt.setActionCommand(FUND_BAL_RPT);
				ivjchkFundsBalanceRprt.setBounds(180, 194, 186, 22);
				ivjchkFundsBalanceRprt.setMinimumSize(
					new Dimension(156, 22));
				// user code begin {1}
				ivjchkFundsBalanceRprt.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjchkFundsBalanceRprt;
	}

	/**
	 * Return the chkFundsMngmt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkFundsMngmt()
	{
		if (ivjchkFundsMngmt == null)
		{
			try
			{
				ivjchkFundsMngmt = new javax.swing.JCheckBox();
				ivjchkFundsMngmt.setName("chkFundsMngmt");
				ivjchkFundsMngmt.setMnemonic('m');
				ivjchkFundsMngmt.setText(FUND_MANAGEMENT);
				ivjchkFundsMngmt.setMaximumSize(new Dimension(136, 22));
				ivjchkFundsMngmt.setActionCommand(FUND_MANAGEMENT);
				ivjchkFundsMngmt.setBounds(180, 239, 186, 22);
				ivjchkFundsMngmt.setMinimumSize(new Dimension(136, 22));
				// user code begin {1}
				ivjchkFundsMngmt.addActionListener(this);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkFundsMngmt;
	}

	/**
	 * Return the FrmSecurityAccessRightsFundsSEC016ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsFundsSEC016ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsFundsSEC016ContentPane1 == null)
		{
			try
			{
				ivjFrmSecurityAccessRightsFundsSEC016ContentPane1 =
					new JPanel();
				ivjFrmSecurityAccessRightsFundsSEC016ContentPane1
					.setName(
					"FrmSecurityAccessRightsFundsSEC016ContentPane1");
				ivjFrmSecurityAccessRightsFundsSEC016ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsFundsSEC016ContentPane1
					.setMaximumSize(
					new Dimension(452, 309));
				ivjFrmSecurityAccessRightsFundsSEC016ContentPane1
					.setMinimumSize(
					new Dimension(452, 309));
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getstcLblFunds(),
					getstcLblFunds().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getchkFunds(),
					getchkFunds().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getchkCashDrawerOpt(),
					getchkCashDrawerOpt().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getchkFundsBalanceRprt(),
					getchkFundsBalanceRprt().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getchkFundsMngmt(),
					getchkFundsMngmt().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getFrmSecurityAccessRightsFundsSEC016ContentPane1()
					.add(
					getlblEmployeeMName(),
					getlblEmployeeMName().getName());
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
		return ivjFrmSecurityAccessRightsFundsSEC016ContentPane1;
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
	 * Return the JLabel EmployeeFirstName property value.
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
				ivjlblEmployeeFirstName.setText(JLABEL1);
				ivjlblEmployeeFirstName.setBounds(366, 38, 165, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjlblEmployeeFirstName;
	}

	/**
	 * Return the lblEmployeeId property value.
	 * 
	 * @return  JLabel
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
				ivjlblEmployeeId.setText(JLABEL1);
				ivjlblEmployeeId.setMaximumSize(new Dimension(45, 14));
				ivjlblEmployeeId.setMinimumSize(new Dimension(45, 14));
				ivjlblEmployeeId.setBounds(139, 17, 101, 14);
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
				ivjlblEmployeeLastName = new javax.swing.JLabel();
				ivjlblEmployeeLastName.setName("lblEmployeeLastName");
				ivjlblEmployeeLastName.setText(JLABEL1);
				ivjlblEmployeeLastName.setMaximumSize(
					new Dimension(45, 14));
				ivjlblEmployeeLastName.setMinimumSize(
					new Dimension(45, 14));
				ivjlblEmployeeLastName.setBounds(139, 38, 206, 14);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ieIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(ieIVJEx);
			}
		}
		return ivjlblEmployeeLastName;
	}

	/**
	 * Return the JLabel EmployeeMName property value.
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
				ivjlblEmployeeMName.setText(JLABEL2);
				ivjlblEmployeeMName.setBounds(535, 38, 50, 14);
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
				ivjstcLblEmployeeId = new javax.swing.JLabel();
				ivjstcLblEmployeeId.setName("stcLblEmployeeId");
				ivjstcLblEmployeeId.setText(EMP_ID);
				ivjstcLblEmployeeId.setMaximumSize(
					new Dimension(71, 14));
				ivjstcLblEmployeeId.setMinimumSize(
					new Dimension(71, 14));
				ivjstcLblEmployeeId.setBounds(17, 17, 83, 18);
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
				ivjstcLblEmployeeName.setBounds(17, 38, 113, 14);
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
		return ivjstcLblEmployeeName;
	}

	/**
	 * Return the stcLblFunds property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblFunds()
	{
		if (ivjstcLblFunds == null)
		{
			try
			{
				ivjstcLblFunds = new JLabel();
				ivjstcLblFunds.setName("stcLblFunds");
				ivjstcLblFunds.setText(FUNDS);
				ivjstcLblFunds.setMaximumSize(new Dimension(34, 14));
				ivjstcLblFunds.setMinimumSize(new Dimension(34, 14));
				ivjstcLblFunds.setBounds(267, 71, 66, 14);
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
		return ivjstcLblFunds;
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
		//end defect 7891
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
			setName("FrmSecurityAccessRightsFundsSEC016");
			setSize(600, 400);
			setTitle(SEC016_FRAME_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(
				getFrmSecurityAccessRightsFundsSEC016ContentPane1());
		}
		catch (Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		// defect 7891 
		carrBtn[0] = getButtonPanel1().getBtnEnter();
		carrBtn[1] = getButtonPanel1().getBtnCancel();
		carrBtn[2] = getButtonPanel1().getBtnHelp();
		carrBtnSec[0] = getchkFunds();
		carrBtnSec[1] = getchkCashDrawerOpt();
		carrBtnSec[2] = getchkFundsBalanceRprt();
		carrBtnSec[3] = getchkFundsMngmt();
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
			for (int i = 0; i < 4; i++)
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
					if (ciSelectSec > 3)
					{
						ciSelectSec = 0;
					}
					if (ciSelectSec < 4
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
						ciSelectSec = 3;
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
				boolean bStop = true;
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
						bStop = false;
					}
				}
				while (bStop);
			}
		}
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
			FrmSecurityAccessRightsFundsSEC016 laFrmSecurityAccessRightsFundsSEC016;
			laFrmSecurityAccessRightsFundsSEC016 =
				new FrmSecurityAccessRightsFundsSEC016();
			laFrmSecurityAccessRightsFundsSEC016.setModal(true);
			laFrmSecurityAccessRightsFundsSEC016
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent e)
				{
					System.exit(0);
				};
			});
			laFrmSecurityAccessRightsFundsSEC016.show();
			Insets insets =
				laFrmSecurityAccessRightsFundsSEC016.getInsets();
			laFrmSecurityAccessRightsFundsSEC016.setSize(
				laFrmSecurityAccessRightsFundsSEC016.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSecurityAccessRightsFundsSEC016.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSecurityAccessRightsFundsSEC016.setVisibleRTS(true);
		}
		catch (Throwable leIVJEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			leIVJEx.printStackTrace(System.out);
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
		if (getchkCashDrawerOpt().isEnabled())
		{
			if (abVal && !getchkCashDrawerOpt().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkCashDrawerOpt().isSelected())
			{
				ciEnableCount--;
			}
			getchkCashDrawerOpt().setSelected(abVal);
		}
		if (getchkFundsBalanceRprt().isEnabled())
		{
			if (abVal && !getchkFundsBalanceRprt().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkFundsBalanceRprt().isSelected())
			{
				ciEnableCount--;
			}
			getchkFundsBalanceRprt().setSelected(abVal);
		}
		if (getchkFundsMngmt().isEnabled())
		{
			if (abVal && !getchkFundsMngmt().isSelected())
			{
				ciEnableCount++;
			}
			else if (!abVal && getchkFundsMngmt().isSelected())
			{
				ciEnableCount--;
			}
			getchkFundsMngmt().setSelected(abVal);
		}
	}
	
	/**
	 * All subclasses must implement this method - 
	 * it sets the data on the screen and is how the controller relays
	 * information to the view.
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
			//Enable disable check boxes
			disableBtnOnOfcId();
			if (laSecData.getCashOperAccs() == 1)
			{
				getchkCashDrawerOpt().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getFundsBalAccs() == 1)
			{
				getchkFundsBalanceRprt().setSelected(true);
				ciEnableCount++;
			}
			if (laSecData.getFundsMgmtAccs() == 1)
			{
				getchkFundsMngmt().setSelected(true);
				ciEnableCount++;
			}
		}
		checkCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Funds. 
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC016(true);
		//Take the new Values from the window and set to data object
		caSecData.getSecData().setCashOperAccs(
			getIntValue(getchkCashDrawerOpt().isSelected()));
		caSecData.getSecData().setFundsBalAccs(
			getIntValue(getchkFundsBalanceRprt().isSelected()));
		caSecData.getSecData().setFundsMgmtAccs(
			getIntValue(getchkFundsMngmt().isSelected()));
	}
}
