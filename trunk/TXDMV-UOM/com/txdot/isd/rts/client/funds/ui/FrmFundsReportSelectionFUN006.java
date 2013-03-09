package com.txdot.isd.rts.client.funds.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.FundsReportData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmFundsReportSelectionFUN006.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Ray Rowehl	7/02/2002	Fixed defect CQU100004342.
 *							Updated keyListener to handle transition 
 *							from none to Employee correctly.
 * R Taylor		6/24/2004	Enlarged "Primary split by:" box to allow
 *							room for "Employee" radiobutton.
 *							defect 6829 Ver 5.2.1 
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							delete implements KeyListener
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	12/09/2005	Radio Buttons should not select
 * 							modify keyPressed()
 * 							defect 7886 Ver 5.2.3 
 * K Harrell	05/07/2008	Disable "Show Source of Money" for HQ as 
 * 							it is not applicable to Trans Recon Rpt.
 * 							Add'l code cleanup.
 * 							modify setData()
 * 							defect 9653 Ver Defect POS A 
 * K Harrell	06/08/2009	modify PRIMARY_SPLIT, SELECT_ENTITY
 * 							Additional class cleanup. 
 * 							defect 9943 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/**
 * Screen prompts user to enter reporting selection criteria, such
 * as Entity, Primary Split, and Money Source.
 *
 * @version	Defect_POS_F	06/08/2009 
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmFundsReportSelectionFUN006
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkShowSourceOfMoney = null;
	private JPanel ivjFrmFundsReportSelectionFUN006ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JRadioButton ivjradioCashDrawer = null;
	private JRadioButton ivjradioCashDrawer2 = null;
	private JRadioButton ivjradioEmployee = null;
	private JRadioButton ivjradioEmployee2 = null;
	private JRadioButton ivjradioNone = null;
	private JRadioButton ivjradioSubstation = null;

	// Constants 
	private final static String CASH_DRWR = "Cash Drawer";
	private final static String EMPLOYEE = "Employee";
	private final static String NONE = "None";
	// defect 9943 
	private final static String PRIMARY_SPLIT = "Primary Split by:";
	private final static String SELECT_ENTITY = "Select Entity:";
	// end defect 9943 
	private final static String SHOW_SOURCE = "Show Source of Money";
	private final static String SUBSTA = "Substation";
	private final static String TITLE_FUN006 =
		"Funds Report Selection     FUN006";

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmFundsReportSelectionFUN006 laFrmFundsReportSelectionFUN006;
			laFrmFundsReportSelectionFUN006 =
				new FrmFundsReportSelectionFUN006();
			laFrmFundsReportSelectionFUN006.setModal(true);
			laFrmFundsReportSelectionFUN006
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmFundsReportSelectionFUN006.show();
			java.awt.Insets laInsets =
				laFrmFundsReportSelectionFUN006.getInsets();
			laFrmFundsReportSelectionFUN006.setSize(
				laFrmFundsReportSelectionFUN006.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmFundsReportSelectionFUN006.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmFundsReportSelectionFUN006.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of util.JDialogTxDot");
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmFundsReportSelectionFUN006 constructor comment.
	 */
	public FrmFundsReportSelectionFUN006()
	{
		super();
		initialize();
	}

	/**
	 * FrmFundsReportSelectionFUN006 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmFundsReportSelectionFUN006(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmFundsReportSelectionFUN006 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmFundsReportSelectionFUN006(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when user performs an action.  The user can select their
	 * reporting choices, and continue by pressing the "Enter" button.
	 *
	 * Cancel or Help buttons respectively result in destroying the
	 * window or providing appropriate help.
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

			FundsData laFundsData = new FundsData();
			FundsReportData laFundsReportData = new FundsReportData();

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (getradioCashDrawer().isSelected() == true)
				{
					laFundsReportData.setEntity(
						FundsConstant.CASH_DRAWER);
				}
				else if (getradioEmployee().isSelected() == true)
				{
					laFundsReportData.setEntity(FundsConstant.EMPLOYEE);
				}
				else if (getradioSubstation().isSelected() == true)
				{
					laFundsReportData.setEntity(
						FundsConstant.SUBSTATION);
				}

				if (getradioCashDrawer2().isSelected() == true)
				{
					laFundsReportData.setPrimarySplit(
						FundsConstant.CASH_DRAWER);
				}
				else if (getradioEmployee2().isSelected() == true)
				{
					laFundsReportData.setPrimarySplit(
						FundsConstant.EMPLOYEE);
				}
				else if (getradioNone().isSelected() == true)
				{
					laFundsReportData.setPrimarySplit(
						FundsConstant.NONE);
				}

				if (getchkShowSourceOfMoney().isSelected() == true)
				{
					laFundsReportData.setShowSourceMoney(true);
				}

				laFundsData.setFundsReportData(laFundsReportData);

				getController().processData(
					AbstractViewController.ENTER,
					laFundsData);
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
				RTSHelp.displayHelp(RTSHelp.FUN006);
			}

			//Logic to override a user unselecting a radio button by
			//clicking on it
			//Instead the button will now stay selected
			else if (aaAE.getSource() == getradioNone())
			{
				if (getradioNone().isSelected() == false)
				{
					getradioNone().setSelected(true);
				}
			}
			else if (aaAE.getSource() == getradioEmployee2())
			{
				if (getradioEmployee2().isSelected() == false)
				{
					getradioEmployee2().setSelected(true);
				}
			}
			else if (aaAE.getSource() == getradioCashDrawer2())
			{
				if (getradioCashDrawer2().isSelected() == false)
				{
					getradioCashDrawer2().setSelected(true);
				}
			}
			else if (aaAE.getSource() == getradioEmployee())
			{
				if (getradioEmployee().isSelected() == false)
				{
					getradioEmployee().setSelected(true);
				}
			}
			else if (aaAE.getSource() == getradioCashDrawer())
			{
				if (getradioCashDrawer().isSelected() == false)
				{
					getradioCashDrawer().setSelected(true);
				}
			}
		}

		finally
		{
			doneWorking();
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setLayout(new java.awt.GridBagLayout());
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
	 * Return the ivjchkShowSourceOfMoney property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkShowSourceOfMoney()
	{
		if (ivjchkShowSourceOfMoney == null)
		{
			try
			{
				ivjchkShowSourceOfMoney = new javax.swing.JCheckBox();
				ivjchkShowSourceOfMoney.setName("ivjchkShowSourceOfMoney");
				ivjchkShowSourceOfMoney.setMnemonic(
					java.awt.event.KeyEvent.VK_W);
				ivjchkShowSourceOfMoney.setText(SHOW_SOURCE);
				ivjchkShowSourceOfMoney.setMaximumSize(
					new java.awt.Dimension(155, 22));
				ivjchkShowSourceOfMoney.setActionCommand(SHOW_SOURCE);
				ivjchkShowSourceOfMoney.setMinimumSize(
					new java.awt.Dimension(155, 22));
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
		return ivjchkShowSourceOfMoney;
	}

	/**
	 * Return the ivjFrmFundsReportSelectionFUN006ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmFundsReportSelectionFUN006ContentPane1()
	{
		if (ivjFrmFundsReportSelectionFUN006ContentPane1 == null)
		{
			try
			{
				ivjFrmFundsReportSelectionFUN006ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmFundsReportSelectionFUN006ContentPane1.setName(
					"ivjFrmFundsReportSelectionFUN006ContentPane1");
				ivjFrmFundsReportSelectionFUN006ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmFundsReportSelectionFUN006ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmFundsReportSelectionFUN006ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(437, 352));

				java.awt.GridBagConstraints constraintsJPanel1 =
					new java.awt.GridBagConstraints();
				constraintsJPanel1.gridx = 1;
				constraintsJPanel1.gridy = 1;
				constraintsJPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJPanel1.weightx = 1.0;
				constraintsJPanel1.weighty = 1.0;
				constraintsJPanel1.insets =
					new java.awt.Insets(12, 32, 3, 23);
				getFrmFundsReportSelectionFUN006ContentPane1().add(
					getJPanel1(),
					constraintsJPanel1);

				java
					.awt
					.GridBagConstraints constraintschkShowSourceOfMoney =
					new java.awt.GridBagConstraints();
				constraintschkShowSourceOfMoney.gridx = 2;
				constraintschkShowSourceOfMoney.gridy = 1;
				constraintschkShowSourceOfMoney.ipadx = 3;
				constraintschkShowSourceOfMoney.ipady = -4;
				constraintschkShowSourceOfMoney.insets =
					new java.awt.Insets(52, 21, 53, 38);
				getFrmFundsReportSelectionFUN006ContentPane1().add(
					getchkShowSourceOfMoney(),
					constraintschkShowSourceOfMoney);

				java.awt.GridBagConstraints constraintsJPanel2 =
					new java.awt.GridBagConstraints();
				constraintsJPanel2.gridx = 1;
				constraintsJPanel2.gridy = 2;
				constraintsJPanel2.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJPanel2.weightx = 1.0;
				constraintsJPanel2.weighty = 1.0;
				constraintsJPanel2.ipady = 19;
				constraintsJPanel2.insets =
					new java.awt.Insets(3, 34, 3, 21);
				getFrmFundsReportSelectionFUN006ContentPane1().add(
					getJPanel2(),
					constraintsJPanel2);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 3;
				constraintsButtonPanel1.gridwidth = 2;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 38;
				constraintsButtonPanel1.ipady = 35;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(4, 100, 6, 115);
				getFrmFundsReportSelectionFUN006ContentPane1().add(
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
		return ivjFrmFundsReportSelectionFUN006ContentPane1;
	}

	/**
	 * Return the ivjJPanel1 property value.
	 * 
	 * @return JPanel
	 */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsradioCashDrawer =
					new java.awt.GridBagConstraints();
				constraintsradioCashDrawer.gridx = 1;
				constraintsradioCashDrawer.gridy = 1;
				constraintsradioCashDrawer.ipadx = 9;
				constraintsradioCashDrawer.insets =
					new java.awt.Insets(10, 38, 5, 32);
				getJPanel1().add(
					getradioCashDrawer(),
					constraintsradioCashDrawer);

				java.awt.GridBagConstraints constraintsradioEmployee =
					new java.awt.GridBagConstraints();
				constraintsradioEmployee.gridx = 1;
				constraintsradioEmployee.gridy = 2;
				constraintsradioEmployee.ipadx = 28;
				constraintsradioEmployee.insets =
					new java.awt.Insets(5, 38, 5, 32);
				getJPanel1().add(
					getradioEmployee(),
					constraintsradioEmployee);

				java.awt.GridBagConstraints constraintsradioSubstation =
					new java.awt.GridBagConstraints();
				constraintsradioSubstation.gridx = 1;
				constraintsradioSubstation.gridy = 3;
				constraintsradioSubstation.ipadx = 22;
				constraintsradioSubstation.insets =
					new java.awt.Insets(5, 38, 12, 32);
				getJPanel1().add(
					getradioSubstation(),
					constraintsradioSubstation);
				// user code begin {1}
				Border laBorder =
					new TitledBorder(new EtchedBorder(), SELECT_ENTITY);
				ivjJPanel1.setBorder(laBorder);
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
	private javax.swing.JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new javax.swing.JPanel();
				ivjJPanel2.setName("ivjJPanel2");
				ivjJPanel2.setLayout(new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsradioNone =
					new java.awt.GridBagConstraints();
				constraintsradioNone.gridx = 1;
				constraintsradioNone.gridy = 1;
				constraintsradioNone.ipadx = 54;
				constraintsradioNone.insets =
					new java.awt.Insets(10, 40, 5, 30);
				getJPanel2().add(getradioNone(), constraintsradioNone);

				java.awt.GridBagConstraints constraintsradioCashDrawer2 =
					new java.awt.GridBagConstraints();
				constraintsradioCashDrawer2.gridx = 1;
				constraintsradioCashDrawer2.gridy = 2;
				constraintsradioCashDrawer2.ipadx = 9;
				constraintsradioCashDrawer2.insets =
					new java.awt.Insets(5, 40, 5, 30);
				getJPanel2().add(
					getradioCashDrawer2(),
					constraintsradioCashDrawer2);

				java.awt.GridBagConstraints constraintsradioEmployee2 =
					new java.awt.GridBagConstraints();
				constraintsradioEmployee2.gridx = 1;
				constraintsradioEmployee2.gridy = 3;
				constraintsradioEmployee2.ipadx = 28;
				constraintsradioEmployee2.insets =
					new java.awt.Insets(5, 40, 11, 30);
				getJPanel2().add(
					getradioEmployee2(),
					constraintsradioEmployee2);
				// user code begin {1}
				Border laBorder =
					new TitledBorder(new EtchedBorder(), PRIMARY_SPLIT);
				ivjJPanel2.setBorder(laBorder);
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
	 * Return the ivjradioCashDrawer property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCashDrawer()
	{
		if (ivjradioCashDrawer == null)
		{
			try
			{
				ivjradioCashDrawer = new javax.swing.JRadioButton();
				ivjradioCashDrawer.setName("ivjradioCashDrawer");
				ivjradioCashDrawer.setMnemonic(
					java.awt.event.KeyEvent.VK_D);
				ivjradioCashDrawer.setText(CASH_DRWR);
				ivjradioCashDrawer.setMaximumSize(
					new java.awt.Dimension(99, 22));
				ivjradioCashDrawer.setActionCommand(CASH_DRWR);
				ivjradioCashDrawer.setMinimumSize(
					new java.awt.Dimension(99, 22));
				// user code begin {1}
				ivjradioCashDrawer.addActionListener(this);
				ivjradioCashDrawer.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCashDrawer;
	}

	/**
	 * Return the ivjradioCashDrawer2 property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCashDrawer2()
	{
		if (ivjradioCashDrawer2 == null)
		{
			try
			{
				ivjradioCashDrawer2 = new javax.swing.JRadioButton();
				ivjradioCashDrawer2.setName("ivjradioCashDrawer2");
				ivjradioCashDrawer2.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				ivjradioCashDrawer2.setText(CASH_DRWR);
				ivjradioCashDrawer2.setMaximumSize(
					new java.awt.Dimension(99, 22));
				ivjradioCashDrawer2.setActionCommand(CASH_DRWR);
				ivjradioCashDrawer2.setMinimumSize(
					new java.awt.Dimension(99, 22));
				// user code begin {1}
				ivjradioCashDrawer2.addActionListener(this);
				ivjradioCashDrawer2.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCashDrawer2;
	}

	/**
	 * Return the ivjradioEmployee property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioEmployee()
	{
		if (ivjradioEmployee == null)
		{
			try
			{
				ivjradioEmployee = new javax.swing.JRadioButton();
				ivjradioEmployee.setName("ivjradioEmployee");
				ivjradioEmployee.setMnemonic(
					java.awt.event.KeyEvent.VK_M);
				ivjradioEmployee.setText(EMPLOYEE);
				ivjradioEmployee.setMaximumSize(
					new java.awt.Dimension(80, 22));
				ivjradioEmployee.setActionCommand(EMPLOYEE);
				ivjradioEmployee.setMinimumSize(
					new java.awt.Dimension(80, 22));
				// user code begin {1}
				ivjradioEmployee.addActionListener(this);
				ivjradioEmployee.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioEmployee;
	}

	/**
	 * Return the ivjradioEmployee2 property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioEmployee2()
	{
		if (ivjradioEmployee2 == null)
		{
			try
			{
				ivjradioEmployee2 = new javax.swing.JRadioButton();
				ivjradioEmployee2.setName("ivjradioEmployee2");
				ivjradioEmployee2.setMnemonic(
					java.awt.event.KeyEvent.VK_P);
				ivjradioEmployee2.setText(EMPLOYEE);
				ivjradioEmployee2.setMaximumSize(
					new java.awt.Dimension(80, 22));
				ivjradioEmployee2.setActionCommand(EMPLOYEE);
				ivjradioEmployee2.setMinimumSize(
					new java.awt.Dimension(80, 22));
				// user code begin {1}
				ivjradioEmployee2.addActionListener(this);
				ivjradioEmployee2.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioEmployee2;
	}

	/**
	 * Return the ivjradioNone property value.
	 * 
	 * @return JRadioButton
	 */
	private javax.swing.JRadioButton getradioNone()
	{
		if (ivjradioNone == null)
		{
			try
			{
				ivjradioNone = new javax.swing.JRadioButton();
				ivjradioNone.setName("ivjradioNone");
				ivjradioNone.setMnemonic(KeyEvent.VK_N);
				ivjradioNone.setText(NONE);
				ivjradioNone.setMaximumSize(
					new java.awt.Dimension(54, 22));
				ivjradioNone.setActionCommand(NONE);
				ivjradioNone.setMinimumSize(
					new java.awt.Dimension(54, 22));
				// user code begin {1}
				ivjradioNone.addActionListener(this);
				ivjradioNone.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioNone;
	}

	/**
	 * Return the ivjradioSubstation property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioSubstation()
	{
		if (ivjradioSubstation == null)
		{
			try
			{
				ivjradioSubstation = new javax.swing.JRadioButton();
				ivjradioSubstation.setName("ivjradioSubstation");
				ivjradioSubstation.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjradioSubstation.setText(SUBSTA);
				ivjradioSubstation.setMaximumSize(
					new java.awt.Dimension(86, 22));
				ivjradioSubstation.setActionCommand(SUBSTA);
				ivjradioSubstation.setMinimumSize(
					new java.awt.Dimension(86, 22));
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
		return ivjradioSubstation;
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
			setName("FrmFundsReportSelectionFUN006");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(450, 325);
			setTitle(TITLE_FUN006);
			setContentPane(
				getFrmFundsReportSelectionFUN006ContentPane1());
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
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		//If Employee is selected as Entity, it must be disabled as
		//split option	
		if (aaIE.getSource() == getradioEmployee())
		{
			if (getradioEmployee().isSelected()
				&& getradioEmployee2().isSelected())
			{
				getradioEmployee2().setEnabled(false);
				getradioEmployee2().setSelected(false);
				getradioCashDrawer2().setEnabled(true);
				getradioCashDrawer().setSelected(false);
				getradioNone().setSelected(true);
			}

			else if (getradioEmployee().isSelected())
			{
				getradioEmployee2().setEnabled(false);
				getradioEmployee2().setSelected(false);
				getradioCashDrawer2().setEnabled(true);
				getradioCashDrawer().setSelected(false);
			}
		}

		//If Cash drawer is selected as Entity, it must be disabled as
		//split option	
		else if (aaIE.getSource() == getradioCashDrawer())
		{
			if (getradioCashDrawer().isSelected()
				&& getradioCashDrawer2().isSelected())
			{
				getradioEmployee2().setEnabled(true);
				getradioCashDrawer2().setEnabled(false);
				getradioCashDrawer2().setSelected(false);
				getradioEmployee().setSelected(false);
				getradioNone().setSelected(true);
			}
			else if (getradioCashDrawer().isSelected())
			{
				getradioEmployee2().setEnabled(true);
				getradioCashDrawer2().setEnabled(false);
				getradioCashDrawer2().setSelected(false);
				getradioEmployee().setSelected(false);
			}
		}

		//If None is selected, than employee and cash drawer must not
		//be selected
		else if (aaIE.getSource() == getradioNone())
		{
			if (getradioNone().isSelected())
			{
				getradioEmployee2().setSelected(false);
				getradioCashDrawer2().setSelected(false);
			}
		}

		//If Employee is selected, than none and cash drawer must not
		//be selected
		else if (aaIE.getSource() == getradioEmployee2())
		{
			if (getradioEmployee2().isSelected())
			{
				getradioNone().setSelected(false);
				getradioCashDrawer2().setSelected(false);
			}
		}

		//If Cash Drawer is selected, than none and employee must not
		//be selected
		else if (aaIE.getSource() == getradioCashDrawer2())
		{
			if (getradioCashDrawer2().isSelected())
			{
				getradioEmployee2().setSelected(false);
				getradioNone().setSelected(false);
			}
		}

	}

	/**
	 * Listen to key strokes and take the appropriate action.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		// handle arrow right (includes arrow up)
		if (aaKE.getKeyCode() == KeyEvent.VK_UP
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
		{

			//Logic to toggle focus depending on what has focus and what
			//buttons are enabled move from cash drawer to employee id
			//employee is enabled.

			// Do not select when focus moved. 
			if (getradioCashDrawer().hasFocus()
				&& getradioEmployee().isEnabled())
			{
				getradioEmployee().requestFocus();
			}
			// move from employee to cash drawer
			else if (getradioEmployee().hasFocus())
			{
				getradioCashDrawer().requestFocus();
			}
			// move from Employee 2 to none
			if (getradioEmployee2().hasFocus())
			{
				getradioNone().requestFocus();
			}
			// move from cash drawer 2 to none.
			else if (getradioCashDrawer2().hasFocus())
			{
				getradioNone().requestFocus();
			}
			//move from none to cash drawer 2 if cash drawer 2 is enabled
			else if (
				getradioNone().hasFocus()
					&& getradioCashDrawer2().isEnabled())
			{
				getradioCashDrawer2().requestFocus();
			}
			// move from none to employee 2 if employee 2 is enabled
			// defect 4342
			else if (
				getradioNone().hasFocus()
					&& getradioEmployee2().isEnabled())
			{
				getradioEmployee2().requestFocus();
			}
			// end defect 4342
		}

		// handle arrow right (also key down)
		else if (
			aaKE.getKeyCode() == KeyEvent.VK_DOWN
				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			//Logic to toggle focus depending on what has focus and what
			//buttons are enabled move from cash drawer to employee if
			//employee is enabled
			if (getradioCashDrawer().hasFocus()
				&& getradioEmployee().isEnabled())
			{
				getradioEmployee().requestFocus();
			}
			// move from employee to cash drawer
			else if (getradioEmployee().hasFocus())
			{
				getradioCashDrawer().requestFocus();
			}
			// move from Employee2 to none
			else if (getradioEmployee2().hasFocus())
			{
				getradioEmployee2().setSelected(false);
				getradioNone().requestFocus();
				getradioNone().setSelected(true);
			}
			// move from cash drawer 2 to none
			else if (getradioCashDrawer2().hasFocus())
			{
				getradioNone().requestFocus();
			}
			//move from none  to Cash Drawer 2 if cash drawer 2 is enabled
			else if (
				getradioNone().hasFocus()
					&& getradioCashDrawer2().isEnabled())
			{
				//getradioNone().setSelected(false);
				getradioCashDrawer2().requestFocus();
				//getradioCashDrawer2().setSelected(true);
			}
			// move from none to Employee 2 if it is enabled
			// defect 4342
			else if (
				getradioNone().hasFocus()
					&& getradioEmployee2().isEnabled())
			{
				getradioEmployee2().requestFocus();
			}
			// end defect 4342

		}

		super.keyPressed(aaKE);
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			//Check Trans Code != FundsBalance Reports, 
			//enable/disable buttons
			if (!getController()
				.getTransCode()
				.equals(TransCdConstant.BALANCE))
			{
				getradioCashDrawer().setSelected(true);
				getradioNone().setSelected(true);
				getradioEmployee().setEnabled(false);
				getradioSubstation().setEnabled(false);
				getradioCashDrawer2().setEnabled(false);
				getradioCashDrawer().requestFocus();
			}

			//Check Trans Code == FundsBalance Reports,
			//enable/disable buttons
			if (getController()
				.getTransCode()
				.equals(TransCdConstant.BALANCE))
			{
				if (getradioEmployee().isSelected() == true)
				{
					getradioEmployee2().setEnabled(false);
					getradioEmployee2().setSelected(false);
					if (getradioCashDrawer2().isSelected())
					{
						getradioCashDrawer2().setSelected(true);
					}
					else
					{
						getradioNone().setSelected(true);
					}
					getradioCashDrawer().setSelected(false);
				}
				else if (
					getradioNone().isSelected() == false
						&& getradioCashDrawer2().isSelected())
				{
					getradioCashDrawer2().setSelected(true);
				}
				else if (
					getradioNone().isSelected() == false
						&& getradioEmployee2().isSelected())
				{
					getradioEmployee2().setSelected(true);
				}
				else
				{
					getradioCashDrawer().setSelected(true);
					getradioNone().setSelected(true);
					getradioSubstation().setEnabled(false);
				}
				// defect 9653
				// Do not enable for HQ
				getchkShowSourceOfMoney().setEnabled(
					!SystemProperty.isHQ());
				// end defect 9653 
			}
		}

		catch (NullPointerException aeNPEx)
		{
			RTSException leEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			leEx.displayError(this);
			leEx = null;
		}

	}
}
