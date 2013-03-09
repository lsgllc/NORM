package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
import com.txdot.isd.rts.services.util.constants.LocalOptionConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSecurityAccessRightsTitleRegSEC007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Min Wang		11/24/2004 	Fix space bar does not enable all in HQ (
 *							only happen on developer setup ).
 *							modify selectChkButtons()
 *							defect 7100 Ver 5.2.2
 * Ray Rowehl	03/17/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7891 Ver 5.2.3
 * Min Wang 	04/06/2005	Remove arrays since they are having 
 * 							initialization errors
 * 							delete carrBtn, carrBtnSec
 * 							modify initialize(), keyPressed()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	remove unused method
 * 							delete setController()
 * 							defect 7891 Ver 5.2.3
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Ray Rowehl	08/23/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		12/09/2005  Uncomment code for arrow key function.
 * 							modify keyPressed(), initialize()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	01/19/2006	Screen manipulation for Tab Key movement
 * 							through checkboxes. 
 * 							defect 7891 Ver 5.2.3
 * M Reyes		10/11/2006	Changes for Exempts
 * 							Used visual editor to add/adjust checkboxes
 * 							modify actionPerformed()
 *                          checkCountZero(),getchkAdditionalSalesTax() 
 * 							initialize(), keyPressed()
 * 							selectChkButtons(), setData()
 * 							setValuesToDataObj(), getJPanel()
 * 							Add getchkExemptAuth() 
 * 							defect 8900 Ver Exempts
 * K Harrell	04/20/2007	Implemented SystemProperty.isCounty()
 * 							organized variables 
 * 							modify disableBtnOnOfcId()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/03/2008	Salvage authority is now prerequisite for 
 * 							COA authority.  Moved COA under Salvage 
 * 							 via Visual Editor to show dependency. 
 * 							modify actionPerformed(), setData()
 * 							defect 9642 Ver Defect POS A
 * K Harrell	06/09/2008	Regions can no longer access Salvage 
 * 							modify disableBtnOnOfcId(), setData()
 * 							defect 9701 Ver Defect POS A
 * K Harrell	06/19/2008	Handle select all / deselect all for COA.
 * 							modify selectChkButtons()
 * 							defect 9719 Ver Defect POS A 
 * Min Wang		08/19/2009	add a checkbox for “Private Law Enforcement 
 * 							Vehicle” on the screen.
 * 							add ivjchkPrivateLawEnfVeh, 
 * 							getchkPrivateLawEnfVeh()
 * 							modify actionPerformed(), getchkExemptAuth(),
 * 							getJPanel(),initialize(),  keyPressed(), 
 * 							selectChkButtons(),	setData()
 * 							defect 10153 Ver Defect_POS_F
 * --------------------------------------------------------------------- 
 */

/**
 * This class is used for managing security access rights for
 * Title/Registration. Data on this screen is managed through 
 * SecurityClientDataObject.
 * 
 * @version	Defect_POS_F	08/19/2008
 * @author	Ashish Marajan 
 * <br>Creation Date: 		06/27/2001 19:13:29 
 */

public class FrmSecurityAccessRightsTitleRegSEC007
	extends RTSDialogBox
	implements ActionListener
{
	private JCheckBox ivjchkAdditionalSalesTax = null;
	private JCheckBox ivjchkCCOCCDO = null;
	private JCheckBox ivjchkCOA = null;
	private JCheckBox ivjchkCorrectTitleRejection = null;
	private JCheckBox ivjchkDealerTitle = null;
	private JCheckBox ivjchkDeleteTitleInProcess = null;
	private JCheckBox ivjchkSalvage = null;
	private JCheckBox ivjchkTitleApplication = null;
	private JCheckBox ivjchkTitleRegistration = null;
	private JCheckBox ivjchkExemptAuth = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private JLabel ivjstcLblTitleRegistration = null;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private JPanel ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1 =
		null;
	// defect 10153
	private JCheckBox ivjchkPrivateLawEnfVeh = null;
	// end defect 10153
	private JPanel jPanel = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private RTSButton[] carrBtn = new RTSButton[3];
	// defect 10153
	//private JCheckBox[] carrBtnSec = new JCheckBox[10];
	private JCheckBox[] carrBtnSec = new JCheckBox[11];
	// end defect 10153

	// Object 
	private SecurityClientDataObject caSecData = null;

	// boolean 
	private boolean cbSelectAll = false;

	// int 
	private int ciEnableCount = 0;
	private int ciSelect = 0;
	private int ciSelectSec = 0;

	// Constants 
	private static final String SEC007_FRAME_TITLE =
		"Security Access Rights Title/Registration   SEC007";
	private static final String SEC007_FRAME_NAME =
		"FrmSecurityAccessRightsTitleRegSEC007";
	private static final String TXT_TITLE_SLASH_REGISTRATION =
		"Title/Registration";
	private static final String TXT_TITLE_REGISTRATION =
		"Title Registration";
	private static final String TXT_TITLE_APPLICATION =
		"Title Application";
	private static final String TXT_EXEMPT_AUTHORITY =
		"Exempt Authority";
	private static final String TXT_SALVAGE = "Salvage";
	private static final String TXT_DELETE_TITLE_IN_PROCESS =
		"Delete Title In Process";
	private static final String TXT_DEALER_TITLE = "Dealer Title";
	private static final String TXT_CORRECT_TITLE_REJECTION =
		"Correct Title Rejection";
	private static final String TXT_COA = "COA";
	private static final String TXT_CCO_SLASH_CCDO = "CCO/CCDO";
	private static final String TXT_CCO = "CCO";
	private static final String TXT_ADDITIONAL_SALES_TAX =
		"Additional Sales Tax";

	/**
	 * FrmSecurityAccessRightsTitleRegSEC007 constructor comment.
	 */
	public FrmSecurityAccessRightsTitleRegSEC007()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsTitleRegSEC007 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsTitleRegSEC007(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsTitleRegSEC007 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsTitleRegSEC007(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsTitleRegSEC007 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsTitleRegSEC007(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsTitleRegSEC007 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsTitleRegSEC007(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsTitleRegSEC007 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsTitleRegSEC007(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsTitleRegSEC007 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsTitleRegSEC007(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsTitleRegSEC007 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsTitleRegSEC007(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsTitleRegSEC007 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsTitleRegSEC007(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE  ActionEvent
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
				caSecData.setSEC007(false);
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
				RTSHelp.displayHelp(RTSHelp.SEC007);
				return;
			}
			else if (aaAE.getSource() == getchkTitleRegistration())
			{
				if (!getchkTitleRegistration().isSelected())
				{
					cbSelectAll = false;

				}
				else
				{
					cbSelectAll = true;

				}
				selectChkButtons(cbSelectAll);
			}
			else if (aaAE.getSource() == getchkAdditionalSalesTax())
			{
				if (getchkAdditionalSalesTax().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
					ciEnableCount = ciEnableCount - 1;
			}
			else if (aaAE.getSource() == getchkCCOCCDO())
			{
				if (getchkCCOCCDO().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkCOA())
			{
				if (getchkCOA().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkCorrectTitleRejection())
			{
				if (getchkCorrectTitleRejection().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkDealerTitle())
			{
				if (getchkDealerTitle().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkDeleteTitleInProcess())
			{
				if (getchkDeleteTitleInProcess().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
				}
			}
			else if (aaAE.getSource() == getchkSalvage())
			{
				// defect 9642 
				// COA implemented w/in Salvage 
				if (getchkSalvage().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
					getchkCOA().setEnabled(true);
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;
					if (getchkCOA().isSelected())
					{
						ciEnableCount = ciEnableCount - 1;
					}
					getchkCOA().setSelected(false);
					getchkCOA().setEnabled(false);
				}
				// end defect 9642 
			}
			else if (aaAE.getSource() == getchkTitleApplication())
			{
				if (getchkTitleApplication().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
					// defect 8900
					getchkExemptAuth().setEnabled(true);
					// end defect 8900
					// end defect 10153
					if (SystemProperty.isHQ())
					{
						if (!getchkPrivateLawEnfVeh().isEnabled())
						{
							getchkPrivateLawEnfVeh().setEnabled(true);	
						}
					}
					else
					{
						if (getchkPrivateLawEnfVeh().isEnabled())
						{
							getchkPrivateLawEnfVeh().setSelected(false);
							getchkPrivateLawEnfVeh().setEnabled(false);
						}
					}
					// end defect 10153
				}
				else
				{
					ciEnableCount = ciEnableCount - 1;

					// defect 8900
					// Disable Exempt Auth
					// Reduce count if exempt auth selected 

					if (getchkExemptAuth().isSelected())
					{
						ciEnableCount = ciEnableCount - 1;
					}
					getchkExemptAuth().setEnabled(false);
					getchkExemptAuth().setSelected(false);
					// defect 10153
					if (getchkPrivateLawEnfVeh().isSelected())
					{
						ciEnableCount = ciEnableCount - 1;	
					}
					getchkPrivateLawEnfVeh().setSelected(false);
					getchkPrivateLawEnfVeh().setEnabled(false);
					// end defect 10153				
				}
				// end defect 8900
			}
			// defect 8900
			else if (aaAE.getSource() == getchkExemptAuth())
			{
				if (getchkExemptAuth().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
					ciEnableCount = ciEnableCount - 1;
			}
			// end defect 8900
			// defect 10153
			else if (aaAE.getSource() == getchkPrivateLawEnfVeh())
			{
				if (getchkPrivateLawEnfVeh().isSelected())
				{
					ciEnableCount = ciEnableCount + 1;
				}
				else
					ciEnableCount = ciEnableCount - 1;
				}
			// end defect 10153
			checkCountZero();
		}
		finally
		{
			doneWorking();
		}
	}


	/**
	 * Check for EnableCount field for zero value and 
	 * set the Reports checkbox
	 */
	private void checkCountZero()
	{
		if (ciEnableCount == 0)
		{
			getchkTitleRegistration().setSelected(false);
			// defect 8900
			getchkExemptAuth().setSelected(false);
			getchkExemptAuth().setEnabled(false);
			// end defect 8900
			// defect 10153
			getchkPrivateLawEnfVeh().setSelected(false);
			getchkPrivateLawEnfVeh().setEnabled(false);
			// end defect 10153
		}
		else
		{
			getchkTitleRegistration().setSelected(true);
		}
	}

	/**
	 * Disable the various checkbox based on various conditions
	 */
	private void disableBtnOnOfcId()
	{
		// defect 9085 
		//if (caSecData.getWorkStationType()
		//	== LocalOptionConstant.COUNTY)
		if (SystemProperty.isCounty())
		{
			getchkCCOCCDO().setEnabled(false);
			getchkCOA().setEnabled(false);
			getchkSalvage().setEnabled(false);
			getchkDeleteTitleInProcess().setEnabled(false);
		}
		else
		{
			// defect 9642, 9701
			// COA access iff Salvage Access
			// Region has access to neither  
			getchkSalvage().setEnabled(SystemProperty.isHQ());
			getchkCOA().setEnabled(
				SystemProperty.isHQ()
					&& caSecData.getSecData().getSalvAccs() == 1);
			// end defect 9642, 9701
			getchkDealerTitle().setEnabled(false);
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
				ivjButtonPanel1.setBounds(143, 307, 292, 76);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the chkAdditionalSalesTax property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkAdditionalSalesTax()
	{
		if (ivjchkAdditionalSalesTax == null)
		{
			try
			{
				ivjchkAdditionalSalesTax = new JCheckBox();
				ivjchkAdditionalSalesTax.setSize(224, 22);
				ivjchkAdditionalSalesTax.setName(
					"chkAdditionalSalesTax");
				// defect 8900
				//ivjchkAdditionalSalesTax.setMnemonic('x');
				ivjchkAdditionalSalesTax.setMnemonic('d');
				// end defect 8900
				ivjchkAdditionalSalesTax.setText(
					TXT_ADDITIONAL_SALES_TAX);
				ivjchkAdditionalSalesTax.setMaximumSize(
					new java.awt.Dimension(140, 22));
				ivjchkAdditionalSalesTax.setActionCommand(
					TXT_ADDITIONAL_SALES_TAX);
				ivjchkAdditionalSalesTax.setMinimumSize(
					new java.awt.Dimension(140, 22));
				// user code begin {1}
				ivjchkAdditionalSalesTax.setLocation(323, 277);
				ivjchkAdditionalSalesTax.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkAdditionalSalesTax;
	}

	/**
	 * Return the chkCCOCCDO property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkCCOCCDO()
	{
		if (ivjchkCCOCCDO == null)
		{
			try
			{
				ivjchkCCOCCDO = new JCheckBox();
				ivjchkCCOCCDO.setSize(169, 22);
				ivjchkCCOCCDO.setName("chkCCOCCDO");
				ivjchkCCOCCDO.setMnemonic('c');
				ivjchkCCOCCDO.setText(TXT_CCO);
				ivjchkCCOCCDO.setMaximumSize(
					new java.awt.Dimension(86, 22));
				ivjchkCCOCCDO.setActionCommand(TXT_CCO_SLASH_CCDO);
				ivjchkCCOCCDO.setMinimumSize(
					new java.awt.Dimension(86, 22));
				// user code begin {1}
				ivjchkCCOCCDO.setLocation(323, 117);
				ivjchkCCOCCDO.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkCCOCCDO;
	}

	/**
	 * Return the chkCOA property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkCOA()
	{
		if (ivjchkCOA == null)
		{
			try
			{
				ivjchkCOA = new JCheckBox();
				ivjchkCOA.setSize(224, 22);
				ivjchkCOA.setName("chkCOA");
				ivjchkCOA.setMnemonic('O');
				ivjchkCOA.setText(TXT_COA);
				ivjchkCOA.setMaximumSize(
					new java.awt.Dimension(50, 22));
				ivjchkCOA.setActionCommand(TXT_COA);
				ivjchkCOA.setMinimumSize(
					new java.awt.Dimension(50, 22));
				// user code begin {1}
				ivjchkCOA.setLocation(341, 195);
				ivjchkCOA.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkCOA;
	}

	/**
	 * Return the chkCorrectTitleRejection property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkCorrectTitleRejection()
	{
		if (ivjchkCorrectTitleRejection == null)
		{
			try
			{
				ivjchkCorrectTitleRejection = new JCheckBox();
				ivjchkCorrectTitleRejection.setSize(169, 22);
				ivjchkCorrectTitleRejection.setName(
					"chkCorrectTitleRejection");
				ivjchkCorrectTitleRejection.setMnemonic('l');
				ivjchkCorrectTitleRejection.setText(
					TXT_CORRECT_TITLE_REJECTION);
				ivjchkCorrectTitleRejection.setMaximumSize(
					new java.awt.Dimension(151, 22));
				ivjchkCorrectTitleRejection.setActionCommand(
					TXT_CORRECT_TITLE_REJECTION);
				ivjchkCorrectTitleRejection.setMinimumSize(
					new java.awt.Dimension(151, 22));
				// user code begin {1}
				ivjchkCorrectTitleRejection.setLocation(56, 138);
				ivjchkCorrectTitleRejection.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkCorrectTitleRejection;
	}

	/**
	 * Return the chkDealerTitle property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDealerTitle()
	{
		if (ivjchkDealerTitle == null)
		{
			try
			{
				ivjchkDealerTitle = new JCheckBox();
				ivjchkDealerTitle.setSize(169, 22);
				ivjchkDealerTitle.setName("chkDealerTitle");
				ivjchkDealerTitle.setMnemonic('i');
				ivjchkDealerTitle.setText(TXT_DEALER_TITLE);
				ivjchkDealerTitle.setMaximumSize(
					new java.awt.Dimension(89, 22));
				ivjchkDealerTitle.setActionCommand(TXT_DEALER_TITLE);
				ivjchkDealerTitle.setMinimumSize(
					new java.awt.Dimension(89, 22));
				// user code begin {1}
				ivjchkDealerTitle.setLocation(56, 178);
				ivjchkDealerTitle.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkDealerTitle;
	}

	/**
	 * Return the chkDeleteTitleInProcess property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDeleteTitleInProcess()
	{
		if (ivjchkDeleteTitleInProcess == null)
		{
			try
			{
				ivjchkDeleteTitleInProcess = new JCheckBox();
				ivjchkDeleteTitleInProcess.setSize(224, 22);
				ivjchkDeleteTitleInProcess.setName(
					"chkDeleteTitleInProcess");
				ivjchkDeleteTitleInProcess.setMnemonic('P');
				ivjchkDeleteTitleInProcess.setText(
					TXT_DELETE_TITLE_IN_PROCESS);
				ivjchkDeleteTitleInProcess.setMaximumSize(
					new java.awt.Dimension(152, 22));
				ivjchkDeleteTitleInProcess.setActionCommand(
					TXT_DELETE_TITLE_IN_PROCESS);
				ivjchkDeleteTitleInProcess.setMinimumSize(
					new java.awt.Dimension(152, 22));
				// user code begin {1}
				ivjchkDeleteTitleInProcess.setLocation(323, 237);
				ivjchkDeleteTitleInProcess.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkDeleteTitleInProcess;
	}

	/**
	 * Return the chkSalvage property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSalvage()
	{
		if (ivjchkSalvage == null)
		{
			try
			{
				ivjchkSalvage = new JCheckBox();
				ivjchkSalvage.setSize(224, 22);
				ivjchkSalvage.setName("chkSalvage");
				ivjchkSalvage.setMnemonic('S');
				ivjchkSalvage.setText(TXT_SALVAGE);
				ivjchkSalvage.setMaximumSize(
					new java.awt.Dimension(70, 22));
				ivjchkSalvage.setActionCommand(TXT_SALVAGE);
				ivjchkSalvage.setMinimumSize(
					new java.awt.Dimension(70, 22));
				// user code begin {1}
				ivjchkSalvage.setLocation(323, 157);
				ivjchkSalvage.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkSalvage;
	}

	/**
	 * Return the chkTitleApplication property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkTitleApplication()
	{
		if (ivjchkTitleApplication == null)
		{
			try
			{
				ivjchkTitleApplication = new JCheckBox();
				ivjchkTitleApplication.setBounds(56, 58, 169, 22);
				ivjchkTitleApplication.setName("chkTitleApplication");
				ivjchkTitleApplication.setMnemonic('T');
				ivjchkTitleApplication.setText(TXT_TITLE_APPLICATION);
				ivjchkTitleApplication.setMaximumSize(
					new java.awt.Dimension(115, 22));
				ivjchkTitleApplication.setActionCommand(
					TXT_TITLE_APPLICATION);
				ivjchkTitleApplication.setMinimumSize(
					new java.awt.Dimension(115, 22));
				// user code begin {1}
				ivjchkTitleApplication.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkTitleApplication;
	}
	/**
	 * Return the chkExemptAuth property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	// defect 8900
	private javax.swing.JCheckBox getchkExemptAuth()
	{
		if (ivjchkExemptAuth == null)
		{
			try
			{
				ivjchkExemptAuth = new javax.swing.JCheckBox();
				ivjchkExemptAuth.setSize(134, 20);
				// defect 10153
				//ivjchkExemptAuth.setLocation(73, 98);
				ivjchkExemptAuth.setLocation(74, 85);
				// end defect 10153
				ivjchkExemptAuth.setText(TXT_EXEMPT_AUTHORITY);
				ivjchkExemptAuth.setMnemonic('x');
				ivjchkExemptAuth.setName("chkExemptAuth");
				ivjchkExemptAuth.addActionListener(this);
			}
			catch (java.lang.Throwable ivjExc)
			{
				//	user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkExemptAuth;
	}
	//	end defect 8900

	/**
	 * Return the chkTitleRegistration property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkTitleRegistration()
	{
		if (ivjchkTitleRegistration == null)
		{
			try
			{
				ivjchkTitleRegistration = new JCheckBox();
				ivjchkTitleRegistration.setBounds(56, 18, 169, 22);
				ivjchkTitleRegistration.setName("chkTitleRegistration");
				ivjchkTitleRegistration.setMnemonic('g');
				ivjchkTitleRegistration.setText(TXT_TITLE_REGISTRATION);
				ivjchkTitleRegistration.setMaximumSize(
					new java.awt.Dimension(121, 22));
				ivjchkTitleRegistration.setActionCommand(
					TXT_TITLE_REGISTRATION);
				ivjchkTitleRegistration.setMinimumSize(
					new java.awt.Dimension(121, 22));
				// user code begin {1}
				ivjchkTitleRegistration.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjchkTitleRegistration;
	}

	/**
	 * Return the FrmSecurityAccessRightsTitleRegSEC007ContentPane1
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1
			== null)
		{
			try
			{
				ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1 =
					new JPanel();
				ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1
					.setName(
					"FrmSecurityAccessRightsTitleRegSEC007ContentPane1");
				ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(504, 385));
				ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(504, 385));
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getchkCOA(),
					getchkCOA().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getchkSalvage(),
					getchkSalvage().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getchkDeleteTitleInProcess(),
					getchkDeleteTitleInProcess().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getchkAdditionalSalesTax(),
					getchkAdditionalSalesTax().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getstcLblTitleRegistration(),
					getstcLblTitleRegistration().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1()
					.add(
					getlblEmployeeMName(),
					getlblEmployeeMName().getName());
				// user code begin {1}
				ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1
					.add(
					getJPanel(),
					null);
				// user code end
				ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1
					.add(
					getchkCCOCCDO(),
					null);
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFrmSecurityAccessRightsTitleRegSEC007ContentPane1;
	}

	/**
	 * Get the int equivalent for the boolean parameter
	 * Returns 1 if parameter is true or returns 0.
	 * 
	 * @param abVal boolean
	 * @return int
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
				ivjlblEmployeeFirstName.setText("JLabel1");
				ivjlblEmployeeFirstName.setBounds(326, 39, 184, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
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
				ivjlblEmployeeId.setText("JLabel1");
				ivjlblEmployeeId.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblEmployeeId.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblEmployeeId.setBounds(137, 19, 68, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
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
				ivjlblEmployeeLastName.setText("JLabel1");
				ivjlblEmployeeLastName.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblEmployeeLastName.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjlblEmployeeLastName.setBounds(137, 39, 179, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
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
				ivjlblEmployeeMName = new JLabel();
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText("JLabel1");
				ivjlblEmployeeMName.setBounds(525, 39, 63, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
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
				ivjstcLblEmployeeId.setText(
					LocalOptionConstant.TXT_EMPLOYEE_ID_COLON);
				ivjstcLblEmployeeId.setMaximumSize(
					new java.awt.Dimension(71, 14));
				ivjstcLblEmployeeId.setMinimumSize(
					new java.awt.Dimension(71, 14));
				ivjstcLblEmployeeId.setBounds(21, 16, 83, 18);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
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
				ivjstcLblEmployeeName.setText(
					LocalOptionConstant.TXT_EMPLOYEE_NAME_COLON);
				ivjstcLblEmployeeName.setMaximumSize(
					new java.awt.Dimension(94, 14));
				ivjstcLblEmployeeName.setMinimumSize(
					new java.awt.Dimension(94, 14));
				ivjstcLblEmployeeName.setBounds(21, 39, 113, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblEmployeeName;
	}

	/**
	 * Return the stcLblTitleRegistration property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTitleRegistration()
	{
		if (ivjstcLblTitleRegistration == null)
		{
			try
			{
				ivjstcLblTitleRegistration = new JLabel();
				ivjstcLblTitleRegistration.setName(
					"stcLblTitleRegistration");
				ivjstcLblTitleRegistration.setText(
					TXT_TITLE_SLASH_REGISTRATION);
				ivjstcLblTitleRegistration.setMaximumSize(
					new java.awt.Dimension(96, 14));
				ivjstcLblTitleRegistration.setMinimumSize(
					new java.awt.Dimension(96, 14));
				ivjstcLblTitleRegistration.setBounds(252, 83, 207, 14);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjstcLblTitleRegistration;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7891
		///* Uncomment the following lines to print uncaught exceptions
		// *  to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7891
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
			setName(SEC007_FRAME_NAME);
			setSize(620, 400);
			setTitle(SEC007_FRAME_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(
				getFrmSecurityAccessRightsTitleRegSEC007ContentPane1());
		}
		catch (java.lang.Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		// defect 7891
		carrBtn[0] = getButtonPanel1().getBtnEnter();
		carrBtn[1] = getButtonPanel1().getBtnCancel();
		carrBtn[2] = getButtonPanel1().getBtnHelp();
		carrBtnSec[0] = getchkTitleRegistration();
		carrBtnSec[1] = getchkTitleApplication();
		// defect 8900
		carrBtnSec[2] = getchkExemptAuth();
		// defect 10153
		carrBtnSec[3] = getchkPrivateLawEnfVeh();
		carrBtnSec[4] = getchkCorrectTitleRejection();
		carrBtnSec[5] = getchkDealerTitle();
		carrBtnSec[6] = getchkCCOCCDO();
		carrBtnSec[7] = getchkSalvage();
		carrBtnSec[8] = getchkCOA();
		carrBtnSec[9] = getchkDeleteTitleInProcess();
		carrBtnSec[10] = getchkAdditionalSalesTax();
		// end defect 10153
		// end defect 8900
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
			// defect 8900
			//for (int i = 0; i < 9; i++)
			// defect 10153
			//for (int i = 0; i < 10; i++)
			for (int i = 0; i < 11; i++)
			// end defect 8900
			// end defect 10153
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
					ciSelectSec = ciSelectSec + 1;
					// defect 10153
					// defect 8900
					//if (ciSelectSec > 8)
					//if (ciSelectSec > 9)
					if (ciSelectSec > 10)
					// end defect 8900
					{
						ciSelectSec = 0;
					}
					// defect 8900
			
					//if (ciSelectSec < 9
					//	&& carrBtnSec[ciSelectSec].isEnabled())
					//if (ciSelectSec < 10
					//	&& carrBtnSec[ciSelectSec].isEnabled())
					if (ciSelectSec < 11
						&& carrBtnSec[ciSelectSec].isEnabled())
						// end defect 8900		
					{
						carrBtnSec[ciSelectSec].requestFocus();
						lbStop = false;
					}
					// end defect 10153
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
					ciSelectSec = ciSelectSec - 1;
					if (ciSelectSec < 0)
					{
						// defect 10153
						// defect 8900
						//ciSelectSec = 8;
						//ciSelectSec = 9;
						ciSelectSec = 10;
						// end defect 8900
						// end defect 10153
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
					ciSelect = ciSelect + 1;
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
					ciSelect = ciSelect - 1;
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
		// end defect 7891
		super.keyPressed(aaKE);
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs java.lang.String[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmSecurityAccessRightsTitleRegSEC007 aFrmSecurityAccessRightsTitleRegSEC007;
			aFrmSecurityAccessRightsTitleRegSEC007 =
				new FrmSecurityAccessRightsTitleRegSEC007();
			aFrmSecurityAccessRightsTitleRegSEC007.setModal(true);
			aFrmSecurityAccessRightsTitleRegSEC007
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			aFrmSecurityAccessRightsTitleRegSEC007.show();
			java.awt.Insets insets =
				aFrmSecurityAccessRightsTitleRegSEC007.getInsets();
			aFrmSecurityAccessRightsTitleRegSEC007.setSize(
				aFrmSecurityAccessRightsTitleRegSEC007.getWidth()
					+ insets.left
					+ insets.right,
				aFrmSecurityAccessRightsTitleRegSEC007.getHeight()
					+ insets.top
					+ insets.bottom);
			aFrmSecurityAccessRightsTitleRegSEC007.setVisibleRTS(true);
		}
		catch (Throwable leThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			leThrowable.printStackTrace(System.out);
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
		if (getchkAdditionalSalesTax().isEnabled())
		{
			if (abVal && !getchkAdditionalSalesTax().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkAdditionalSalesTax().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkAdditionalSalesTax().setSelected(abVal);
		}
		if (getchkCCOCCDO().isEnabled())
		{
			if (abVal && !getchkCCOCCDO().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkCCOCCDO().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkCCOCCDO().setSelected(abVal);
		}
		// defect 9719 
		// Do not manage COA independently; Later as a fcn of Salvage 
		//		if (getchkCOA().isEnabled())
		//		{
		//			if (abVal && !getchkCOA().isSelected())
		//			{
		//				ciEnableCount = ciEnableCount + 1;
		//			}
		//			else if (!abVal && getchkCOA().isSelected())
		//			{
		//				ciEnableCount = ciEnableCount - 1;
		//			}
		//			getchkCOA().setSelected(abVal);
		//		}
		// end defect 9719 

		if (getchkCorrectTitleRejection().isEnabled())
		{
			if (abVal && !getchkCorrectTitleRejection().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (
				!abVal && getchkCorrectTitleRejection().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkCorrectTitleRejection().setSelected(abVal);
		}
		if (getchkDealerTitle().isEnabled())
		{
			if (abVal && !getchkDealerTitle().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (!abVal && getchkDealerTitle().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkDealerTitle().setSelected(abVal);
		}
		//defect 7100
		//only happen on developer setup
		else
		{
			if (getchkDealerTitle().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
				getchkDealerTitle().setSelected(false);
			}
		}
		//end defect 7100
		if (getchkDeleteTitleInProcess().isEnabled())
		{
			if (abVal && !getchkDeleteTitleInProcess().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (
				!abVal && getchkDeleteTitleInProcess().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
			}
			getchkDeleteTitleInProcess().setSelected(abVal);
		}
		if (getchkSalvage().isEnabled())
		{
			// defect 9719 
			// Manage COA enabling as a function of Salvage Access 
			if (abVal)
			{
				ciEnableCount = ciEnableCount + 1;
			}
			else if (getchkSalvage().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;
				if (getchkCOA().isSelected())
				{
					ciEnableCount = ciEnableCount - 1;
					getchkCOA().setSelected(false);
				}
			}
			getchkSalvage().setSelected(abVal);
			getchkCOA().setEnabled(abVal);
			// end defect 9719
		}
		if (getchkTitleApplication().isEnabled())
		{
			// defect 8900 
			if (abVal && !getchkTitleApplication().isSelected())
			{
				ciEnableCount = ciEnableCount + 1;

				// Disable Exempt if !Title 
				getchkExemptAuth().setEnabled(true);
				// defect 10153
				if (SystemProperty.isHQ())
				{
					getchkPrivateLawEnfVeh().setEnabled(true);
				}
				// end defect 10153
			}
			else if (!abVal && getchkTitleApplication().isSelected())
			{
				ciEnableCount = ciEnableCount - 1;

				if (getchkExemptAuth().isSelected())
				{
					ciEnableCount = ciEnableCount - 1;
					getchkExemptAuth().setSelected(abVal);
				}
				// defect 10153
				if (SystemProperty.isHQ())
				{
					if (getchkPrivateLawEnfVeh().isSelected())
					{
						ciEnableCount = ciEnableCount - 1;
					}	
					getchkPrivateLawEnfVeh().setSelected(abVal);
				}
//				else
//				{
//					getchkPrivateLawEnfVeh().setSelected(false);	
//				}
				getchkPrivateLawEnfVeh().setEnabled(false);	
				// end defect 10153		
				getchkExemptAuth().setEnabled(false);
				// end defect 8900 
			}
			getchkTitleApplication().setSelected(abVal);
		}
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
		if (aaData != null)
		{
			caSecData = (SecurityClientDataObject) aaData;
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
			if (laSecData.getTtlRegAccs() == 1)
			{
				getchkTitleRegistration().setSelected(true);
			}
			if (laSecData.getTtlApplAccs() == 1)
			{
				getchkTitleApplication().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			// defect 8900
			if (laSecData.getExmptAuthAccs() == 1)
			{
				getchkExemptAuth().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			//end defect 8900
			if (laSecData.getCorrTtlRejAccs() == 1)
			{
				getchkCorrectTitleRejection().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getDlrTtlAccs() == 1)
			{
				getchkDealerTitle().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getCCOAccs() == 1)
			{
				getchkCCOCCDO().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}

			// defect 9701 
			// For transition in 5.7.0 
			// Salvage no longer available to Regions 
			if (SystemProperty.isHQ())
			{
				if (laSecData.getSalvAccs() == 1)
				{
					getchkSalvage().setSelected(true);
					ciEnableCount = ciEnableCount + 1;

					// defect 9642 
					// COA access only available if Salvage Access 
					if (laSecData.getCOAAccs() == 1)
					{
						getchkCOA().setSelected(true);
						ciEnableCount = ciEnableCount + 1;
					}
					// end defect 9642
				}
			}
			// end defect 9701 

			if (laSecData.getDelTtlInProcsAccs() == 1)
			{
				getchkDeleteTitleInProcess().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			if (laSecData.getAdjSalesTaxAccs() == 1)
			{
				getchkAdditionalSalesTax().setSelected(true);
				ciEnableCount = ciEnableCount + 1;
			}
			// defect 10153
			if (SystemProperty.isHQ())
			{
				getchkPrivateLawEnfVeh().setEnabled(true);
				if (laSecData.getPrivateLawEnfVehAccs() == 1)
				{
					getchkPrivateLawEnfVeh().setSelected(true);
					ciEnableCount = ciEnableCount + 1;
				}
				else
				{
					getchkPrivateLawEnfVeh().setSelected(false);
				}
			}
			else
			{
				getchkPrivateLawEnfVeh().setEnabled(false);
			}
				
			// end defect 10153
		}
		
		checkCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Title/Registration.
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC007(true);
		//Take the new Values fromt the window and set to data object
		caSecData.getSecData().setTtlRegAccs(
			getIntValue(getchkTitleRegistration().isSelected()));
		caSecData.getSecData().setTtlApplAccs(
			getIntValue(getchkTitleApplication().isSelected()));
		caSecData.getSecData().setCorrTtlRejAccs(
			getIntValue(getchkCorrectTitleRejection().isSelected()));
		caSecData.getSecData().setDlrTtlAccs(
			getIntValue(getchkDealerTitle().isSelected()));
		caSecData.getSecData().setCCOAccs(
			getIntValue(getchkCCOCCDO().isSelected()));
		caSecData.getSecData().setCOAAccs(
			getIntValue(getchkCOA().isSelected()));
		caSecData.getSecData().setSalvAccs(
			getIntValue(getchkSalvage().isSelected()));
		caSecData.getSecData().setDelTtlInProcsAccs(
			getIntValue(getchkDeleteTitleInProcess().isSelected()));
		caSecData.getSecData().setAdjSalesTaxAccs(
			getIntValue(getchkAdditionalSalesTax().isSelected()));
		// defect 8900
		caSecData.getSecData().setExmptAuthAccs(
			getIntValue(getchkExemptAuth().isSelected()));
		// end defect 8900
		// defect 10153
		caSecData.getSecData().setPrivateLawEnfVehAccs(
			getIntValue(getchkPrivateLawEnfVeh().isSelected()));
		// end defect 10153
	}
	/**
	 * This method initializes jPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new javax.swing.JPanel();
			jPanel.setLayout(null);
			jPanel.add(getchkTitleRegistration(), null);
			jPanel.add(getchkTitleApplication(), null);
			jPanel.add(getchkCorrectTitleRejection(), null);
			jPanel.add(getchkDealerTitle(), null);
			// defect 8900
			jPanel.add(getchkExemptAuth(), null);
			// end defect 8900
			// defect 10153
			jPanel.add(getchkPrivateLawEnfVeh(), null);
		    jPanel.setBounds(19, 99, 289, 200);
			//jPanel.setBounds(19, 99, 260, 200);
			// end defect 10153

		}
		return jPanel;
	}
	/**
		 * This method initializes ivjchkPrivateLawEnfVeh
		 * 
		 * @return ivjchkPrivateLawEnfVeh
		 */
		private javax.swing.JCheckBox getchkPrivateLawEnfVeh() 
		{
			if(ivjchkPrivateLawEnfVeh == null) 
			{
				ivjchkPrivateLawEnfVeh = new javax.swing.JCheckBox();
				ivjchkPrivateLawEnfVeh.setBounds(74, 113, 213, 21);
				ivjchkPrivateLawEnfVeh.setText("Private Law Enforcement Vehicle");
				ivjchkPrivateLawEnfVeh.setMnemonic('f');
				ivjchkPrivateLawEnfVeh.addActionListener(this);
				
			}
			return ivjchkPrivateLawEnfVeh;
		}
}