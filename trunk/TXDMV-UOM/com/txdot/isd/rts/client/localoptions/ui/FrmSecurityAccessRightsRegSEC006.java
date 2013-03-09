package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
 * FrmSecurityAccessRightsRegSEC006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	5.2.0 Merge/Cleanup.  See PCR 34 comments.
 *							Remove checkbox for QuickCounter. 
 *							modify actionPerformed(),disableBtnOnOfcId()
 *							initialize(),selectChkButtons(),
 *							setValuesToDataObj()
 * 							Ver 5.2.0
 * Ray Rowehl	03/17/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7891 Ver 5.2.3
 * Min Wang 	04/06/2005	Remove arrays since they are causing 
 * 							initialization errors.
 * 							delete carrBtn, carrBtnSec
 * 							modify initialize(), keyPressed()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/16/2005	remove unused method
 * 							delete setController()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	04/28/2005	Enable/disable HQ IssueDrvsEd w/ space bar.
 * 							modify selectChkButtons()
 * 							defect 7788  Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                  
 * B Hargrove	07/13/2005	Change verbiage for 'Issue Driver Ed Plate'. 
 * 							modify getchkIssueDrvsEd()
 * 							defect 7836  Ver 5.2.3                  
 * B Hargrove	09/01/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Min Wang		08/31/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * K Harrell	10/19/2005	Align checkboxes via Visual Editor
 * 							defect 7836  Ver 5.2.3
 * Min Wang		12/09/2005  Uncomment code for arrow key function.
 * 							modify keyPressed(), initialize()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	02/06/2006	Screen manipulation for Tab Key movement
 * 							through checkboxes. 
 * 							defect 7891 Ver 5.2.3
 * M Reyes		10/11/2006	Changes for Exempts
 * 							modify  setData(), getchkIssueDrvsEd()
 * 							defect 8900 Ver 5.3.0
 * K Harrell	04/20/2007	Implemented SystemProperty.isCounty()
 * 							modify disableBtnOnOfcId(),setData()
 * 							defect 9085 Ver Special Plates   
 * K Harrell	03/03/2010	HQ no longer uses Internet Renewal
 * 							add ciSelectedCount,handleSelections(), 
 * 							 disableBtnOnOfcId()    
 * 							delete ciEnableCount, cbSelectAll 
 * 							modify actionPerformed(),selectChkButtons(), 
 * 							 setData(), checkCountZero(), setSelection()   
 * 							defect 10387 Ver POS_640 
 * Min Wang		01/04/2011	Add checkbox chkWebAgent via Visual Editor.
 * 							add ivjchkWebAgent, WEB_AGENT
 * 							modify carrBtnSec, keyPressed(), setData(),
 * 							setValuesToDataObj(),
 * 							defect 10717 Ver POS_670
 * Min Wang		01/18/2011	modify setData(), setValuesToDataObj()
 * 							defect 10717 Ver POS_670
 * Min Wang		01/24/2011	Align checkbox by visual editor.
 * 							modify selectChkButtons()
 * 							defect 10717 Ver POS_670
 * K Harrell	03/04/2011	Disable WebAgent until later release 
 * 							modify getchkWebAgent(), setData()
 *							defect 10717 Ver 6.7.0 
 * K Harrell	04/04/2011	Enable WebAgent
 * 							modify getchkWebAgent(), setData()
 * 							defect 10717 Ver 6.7.1 
 * ---------------------------------------------------------------------
 */

/**
 * This class is used for managing security access rights for
 * Miscellaneous. Data on this screen is managed through 
 * SecurityClientDataObject.
 *
 * @version	6.7.1 			04/04/2011 
 * @author	Administrator
 * <br>Creation Date:		06/27/2001 18:56:54
 */

public class FrmSecurityAccessRightsRegSEC006
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	private JCheckBox ivjchkAddChange = null;
	private JCheckBox ivjchkDuplicateReceipt = null;
	private JCheckBox ivjchkExchange = null;
	private JCheckBox ivjchkModify = null;
	private JCheckBox ivjchkRegistrationOnly = null;
	private JCheckBox ivjchkRenewal = null;
	private JCheckBox ivjchkReplacement = null;
	private JCheckBox ivjchkSubconRenewal = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmSecurityAccessRightsRegSEC006ContentPane1 =
		null;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private JLabel ivjstcLblRegOnly = null;
	private int ciSelectedCount = 0;
	private SecurityClientDataObject caSecData = null;
	private JCheckBox ivjchkInternetRnwl = null;
	private JCheckBox ivjchkIssueDrvsEd = null;
	private JPanel ivjJPanel1 = null;

	private static final String ADDRESS_CHANGE =
		"Address Change/Print Renewal";
	private static final String DUPL_RECEIPT = "Duplicate Receipt";
	private static final String EXCHANGE = "Exchange";
	private static final String INTERNET_RENEW = "Internet Renewal";
	private static final String ISSUE_DRIVE_EDU_PLT =
		"Issue Driver Education Plate";
	private static final String MODIFY = "Modify";
	private static final String REG_ONLY = "Registration Only";
	private static final String RENEWAL = "Renewal";
	private static final String REPLACEMENT = "Replacement";
	private static final String SUBCON_RENEW = "Subcontractor Renewal";
	private static final String LABEL1 = "JLabel1";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String SEC006_FRAME_TITLE =
		"Security Access Rights Registration Only   SEC006";

	private int ciSelect = 0;
	private int ciSelectSec = 0;
	// defect 10717
	private JCheckBox[] carrBtnSec = new JCheckBox[11];
	// end defect 10717
	private RTSButton[] carrBtn = new RTSButton[3];

	// defect 10717
	private JCheckBox ivjchkWebAgent = null;
	private static final String WEBAGENT = "WebAgent";
	// end defect 10717

	/**
	 * FrmSecurityAccessRightsRegSEC006 constructor comment.
	 */
	public FrmSecurityAccessRightsRegSEC006()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsRegSEC006 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsRegSEC006(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsRegSEC006 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsRegSEC006(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsRegSEC006 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsRegSEC006(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsRegSEC006 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsRegSEC006(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsRegSEC006 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsRegSEC006(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsRegSEC006 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsRegSEC006(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsRegSEC006 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsRegSEC006(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsRegSEC006 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsRegSEC006(
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
			// defect 10387 
			// Remove multiple returns. Consolidate radio button actions.
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				caSecData.setSEC006(false);
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				setValuesToDataObj();
				getController().processData(
					AbstractViewController.ENTER,
					caSecData);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.SEC006);
			}
			else if (aaAE.getSource() instanceof JCheckBox)
			{
				if (aaAE.getSource() == getchkRegistrationOnly())
				{
					selectChkButtons(
						getchkRegistrationOnly().isSelected());
				}
				else
				{
					handleSelections((JCheckBox) aaAE.getSource());
				}
				checkCountZero();
			}
			// end defect 10387 
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Check for SelectedCount field for zero value and 
	 * set the RegistrationOnly checkbox
	 */
	private void checkCountZero()
	{
		// defect 10387 
		getchkRegistrationOnly().setSelected(ciSelectedCount != 0);
		// end defect 10387 
	}

	/** 
	 * Enable According to Location, 
	 *   e.g. Enable InternetRenewal only if County 
	 *
	 */
	private void disableBtnOnOfcId()
	{
		getchkInternetRnwl().setEnabled(SystemProperty.isCounty());
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
				ivjButtonPanel1.setSize(314, 49);
				ivjButtonPanel1.setName("ButtonPanel1");
				// user code begin {1}
				ivjButtonPanel1.getBtnCancel().addActionListener(this);
				ivjButtonPanel1.getBtnEnter().addActionListener(this);
				ivjButtonPanel1.getBtnHelp().addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
				ivjButtonPanel1.setLocation(149, 350);
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
	 * Return the chkAddChange property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkAddChange()
	{
		if (ivjchkAddChange == null)
		{
			try
			{
				ivjchkAddChange = new JCheckBox();
				ivjchkAddChange.setSize(224, 22);
				ivjchkAddChange.setName("chkAddChange");
				ivjchkAddChange.setMnemonic('a');
				ivjchkAddChange.setText(ADDRESS_CHANGE);
				ivjchkAddChange.setMaximumSize(
					new java.awt.Dimension(201, 22));
				ivjchkAddChange.setActionCommand(ADDRESS_CHANGE);
				ivjchkAddChange.setMinimumSize(
					new java.awt.Dimension(201, 22));
				// user code begin {1}
				ivjchkAddChange.setLocation(330, 196);
				ivjchkAddChange.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkAddChange;
	}

	/**
	 * Return the chkDuplicateReceipt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDuplicateReceipt()
	{
		if (ivjchkDuplicateReceipt == null)
		{
			try
			{
				ivjchkDuplicateReceipt = new JCheckBox();
				ivjchkDuplicateReceipt.setBounds(39, 86, 169, 22);
				ivjchkDuplicateReceipt.setName("chkDuplicateReceipt");
				ivjchkDuplicateReceipt.setMnemonic('d');
				ivjchkDuplicateReceipt.setText(DUPL_RECEIPT);
				ivjchkDuplicateReceipt.setMaximumSize(
					new java.awt.Dimension(124, 22));
				ivjchkDuplicateReceipt.setActionCommand(DUPL_RECEIPT);
				ivjchkDuplicateReceipt.setMinimumSize(
					new java.awt.Dimension(124, 22));
				// user code begin {1}
				ivjchkDuplicateReceipt.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDuplicateReceipt;
	}

	/**
	 * Return the chkExchange property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkExchange()
	{
		if (ivjchkExchange == null)
		{
			try
			{
				ivjchkExchange = new JCheckBox();
				ivjchkExchange.setBounds(39, 125, 169, 22);
				ivjchkExchange.setName("chkExchange");
				ivjchkExchange.setMnemonic('x');
				ivjchkExchange.setText(EXCHANGE);
				ivjchkExchange.setMaximumSize(
					new java.awt.Dimension(81, 22));
				ivjchkExchange.setActionCommand(EXCHANGE);
				ivjchkExchange.setMinimumSize(
					new java.awt.Dimension(81, 22));
				// user code begin {1}
				ivjchkExchange.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkExchange;
	}

	/**
	 * Return the chkInternetRnwl property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkInternetRnwl()
	{
		if (ivjchkInternetRnwl == null)
		{
			try
			{
				ivjchkInternetRnwl = new JCheckBox();
				ivjchkInternetRnwl.setSize(163, 24);
				ivjchkInternetRnwl.setName("chkInternetRnwl");
				ivjchkInternetRnwl.setMnemonic('I');
				ivjchkInternetRnwl.setText(INTERNET_RENEW);
				// user code begin {1}
				ivjchkInternetRnwl.setLocation(330, 268);
				ivjchkInternetRnwl.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkInternetRnwl;
	}

	/**
	 * Return the chkIssueDrvsEd property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkIssueDrvsEd()
	{
		if (ivjchkIssueDrvsEd == null)
		{
			try
			{
				ivjchkIssueDrvsEd = new JCheckBox();
				ivjchkIssueDrvsEd.setSize(252, 24);
				ivjchkIssueDrvsEd.setName("chkIssueDrvsEd");
				ivjchkIssueDrvsEd.setMnemonic('p');
				ivjchkIssueDrvsEd.setText(
				// defect 7836
				//	"Issue Driver\'s Education Plate");
				ISSUE_DRIVE_EDU_PLT);
				// end defect 7836
				ivjchkIssueDrvsEd.setLocation(330, 232);
				// user code begin {1}
				// user code end
				// defect 8900
				ivjchkIssueDrvsEd.addActionListener(this);
				// end defect 8900
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkIssueDrvsEd;
	}

	/**
	 * Return the chkModify property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkModify()
	{
		if (ivjchkModify == null)
		{
			try
			{
				ivjchkModify = new JCheckBox();
				ivjchkModify.setBounds(330, 124, 169, 22);
				ivjchkModify.setName("chkModify");
				ivjchkModify.setMnemonic('m');
				ivjchkModify.setText(MODIFY);
				ivjchkModify.setMaximumSize(
					new java.awt.Dimension(62, 22));
				ivjchkModify.setActionCommand(MODIFY);
				ivjchkModify.setMinimumSize(
					new java.awt.Dimension(62, 22));
				// user code begin {1}
				ivjchkModify.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkModify;
	}

	/**
	 * Return the chkRegistrationOnly property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkRegistrationOnly()
	{
		if (ivjchkRegistrationOnly == null)
		{
			try
			{
				ivjchkRegistrationOnly = new JCheckBox();
				ivjchkRegistrationOnly.setBounds(39, 10, 169, 22);
				ivjchkRegistrationOnly.setName("chkRegistrationOnly");
				ivjchkRegistrationOnly.setMnemonic('g');
				ivjchkRegistrationOnly.setText(REG_ONLY);
				ivjchkRegistrationOnly.setMaximumSize(
					new java.awt.Dimension(122, 22));
				ivjchkRegistrationOnly.setActionCommand(REG_ONLY);
				ivjchkRegistrationOnly.setMinimumSize(
					new java.awt.Dimension(122, 22));
				// user code begin {1}
				ivjchkRegistrationOnly.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkRegistrationOnly;
	}

	/**
	 * Return the chkRenewal property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkRenewal()
	{
		if (ivjchkRenewal == null)
		{
			try
			{
				ivjchkRenewal = new JCheckBox();
				ivjchkRenewal.setBounds(39, 48, 169, 22);
				ivjchkRenewal.setName("chkRenewal");
				ivjchkRenewal.setMnemonic('r');
				ivjchkRenewal.setText(RENEWAL);
				ivjchkRenewal.setMaximumSize(
					new java.awt.Dimension(74, 22));
				ivjchkRenewal.setActionCommand(RENEWAL);
				ivjchkRenewal.setMinimumSize(
					new java.awt.Dimension(74, 22));
				// user code begin {1}
				ivjchkRenewal.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkRenewal;
	}

	/**
	 * Return the chkReplacement property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkReplacement()
	{
		if (ivjchkReplacement == null)
		{
			try
			{
				ivjchkReplacement = new JCheckBox();
				ivjchkReplacement.setBounds(39, 165, 169, 22);
				ivjchkReplacement.setName("chkReplacement");
				ivjchkReplacement.setMnemonic('t');
				ivjchkReplacement.setText(REPLACEMENT);
				ivjchkReplacement.setMaximumSize(
					new java.awt.Dimension(100, 22));
				ivjchkReplacement.setActionCommand(REPLACEMENT);
				ivjchkReplacement.setMinimumSize(
					new java.awt.Dimension(100, 22));
				// user code begin {1}
				ivjchkReplacement.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkReplacement;
	}

	/**
	 * Return the chkSubconRenewal property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSubconRenewal()
	{
		if (ivjchkSubconRenewal == null)
		{
			try
			{
				ivjchkSubconRenewal = new JCheckBox();
				ivjchkSubconRenewal.setSize(224, 22);
				ivjchkSubconRenewal.setName("chkSubconRenewal");
				ivjchkSubconRenewal.setMnemonic('s');
				ivjchkSubconRenewal.setText(SUBCON_RENEW);
				ivjchkSubconRenewal.setMaximumSize(
					new java.awt.Dimension(159, 22));
				ivjchkSubconRenewal.setActionCommand(SUBCON_RENEW);
				ivjchkSubconRenewal.setMinimumSize(
					new java.awt.Dimension(159, 22));
				// user code begin {1}
				ivjchkSubconRenewal.setLocation(330, 160);
				ivjchkSubconRenewal.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSubconRenewal;
	}
	
	/**
	 * Return the chkWebAgent property value.
	 * 
	 * @return JCheckBox
	 */
	private javax.swing.JCheckBox getchkWebAgent()
	{
		if (ivjchkWebAgent == null)
		{
			ivjchkWebAgent = new javax.swing.JCheckBox();
			ivjchkWebAgent.setSize(113, 21);
			ivjchkWebAgent.setText(WEBAGENT);
			ivjchkWebAgent.setMnemonic(java.awt.event.KeyEvent.VK_W);
			//user code begin {1}
			ivjchkWebAgent.setLocation(330, 304);
			ivjchkWebAgent.addActionListener(this);
			// user code end
		}
		return ivjchkWebAgent;
	}

	/**
	 * Return the FrmSecurityAccessRightsRegSEC006ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsRegSEC006ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsRegSEC006ContentPane1 == null)
		{
			try
			{
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1 =
					new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints11 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints12 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints14 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints15 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints13 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints16 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints17 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints18 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints103 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints19 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints104 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints105 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints106 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints108 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints109 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints107 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints111 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints185 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints110 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints187 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints188 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints186 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints189 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints191 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints192 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints190 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints193 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints187.insets =
					new java.awt.Insets(15, 4, 6, 2);
				consGridBagConstraints187.ipady = -2;
				consGridBagConstraints187.ipadx = 23;
				consGridBagConstraints187.gridy = 0;
				consGridBagConstraints187.gridx = 1;
				consGridBagConstraints189.insets =
					new java.awt.Insets(18, 2, 4, 115);
				consGridBagConstraints189.ipadx = 102;
				consGridBagConstraints189.gridwidth = 2;
				consGridBagConstraints189.gridy = 2;
				consGridBagConstraints189.gridx = 2;
				consGridBagConstraints191.insets =
					new java.awt.Insets(2, 4, 18, 26);
				consGridBagConstraints191.ipady = 1;
				consGridBagConstraints191.ipadx = 17;
				consGridBagConstraints191.gridy = 1;
				consGridBagConstraints191.gridx = 4;
				consGridBagConstraints192.insets =
					new java.awt.Insets(4, 23, 10, 23);
				consGridBagConstraints192.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints192.weighty = 1.0;
				consGridBagConstraints192.weightx = 1.0;
				consGridBagConstraints192.gridwidth = 5;
				consGridBagConstraints192.gridy = 3;
				consGridBagConstraints192.gridx = 0;
				consGridBagConstraints111.insets =
					new java.awt.Insets(10, 19, 16, 57);
				consGridBagConstraints111.ipady = 36;
				consGridBagConstraints111.ipadx = 98;
				consGridBagConstraints111.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints111.weighty = 1.0;
				consGridBagConstraints111.weightx = 1.0;
				consGridBagConstraints111.gridwidth = 3;
				consGridBagConstraints111.gridy = 4;
				consGridBagConstraints111.gridx = 1;
				consGridBagConstraints190.insets =
					new java.awt.Insets(2, 3, 19, 4);
				consGridBagConstraints190.ipadx = 141;
				consGridBagConstraints190.gridy = 1;
				consGridBagConstraints190.gridx = 3;
				consGridBagConstraints188.insets =
					new java.awt.Insets(3, 4, 20, 3);
				consGridBagConstraints188.ipady = -2;
				consGridBagConstraints188.ipadx = 145;
				consGridBagConstraints188.gridwidth = 2;
				consGridBagConstraints188.gridy = 1;
				consGridBagConstraints188.gridx = 1;
				consGridBagConstraints185.insets =
					new java.awt.Insets(15, 14, 2, 33);
				consGridBagConstraints185.ipady = 2;
				consGridBagConstraints185.ipadx = 12;
				consGridBagConstraints185.gridy = 0;
				consGridBagConstraints185.gridx = 0;
				consGridBagConstraints106.insets =
					new java.awt.Insets(3, 4, 20, 3);
				consGridBagConstraints106.ipady = -2;
				consGridBagConstraints106.ipadx = 145;
				consGridBagConstraints106.gridwidth = 2;
				consGridBagConstraints106.gridy = 1;
				consGridBagConstraints106.gridx = 1;
				consGridBagConstraints105.insets =
					new java.awt.Insets(15, 4, 6, 2);
				consGridBagConstraints105.ipady = -2;
				consGridBagConstraints105.ipadx = 23;
				consGridBagConstraints105.gridy = 0;
				consGridBagConstraints105.gridx = 1;
				consGridBagConstraints14.insets =
					new java.awt.Insets(3, 4, 20, 3);
				consGridBagConstraints14.ipady = -2;
				consGridBagConstraints14.ipadx = 145;
				consGridBagConstraints14.gridwidth = 2;
				consGridBagConstraints14.gridy = 1;
				consGridBagConstraints14.gridx = 1;
				consGridBagConstraints11.insets =
					new java.awt.Insets(15, 14, 2, 33);
				consGridBagConstraints11.ipady = 2;
				consGridBagConstraints11.ipadx = 12;
				consGridBagConstraints11.gridy = 0;
				consGridBagConstraints11.gridx = 0;
				consGridBagConstraints12.insets =
					new java.awt.Insets(4, 14, 19, 3);
				consGridBagConstraints12.ipady = -2;
				consGridBagConstraints12.ipadx = 19;
				consGridBagConstraints12.gridy = 1;
				consGridBagConstraints12.gridx = 0;
				consGridBagConstraints17.insets =
					new java.awt.Insets(2, 4, 18, 26);
				consGridBagConstraints17.ipady = 1;
				consGridBagConstraints17.ipadx = 17;
				consGridBagConstraints17.gridy = 1;
				consGridBagConstraints17.gridx = 4;
				consGridBagConstraints13.insets =
					new java.awt.Insets(15, 4, 6, 2);
				consGridBagConstraints13.ipady = -2;
				consGridBagConstraints13.ipadx = 23;
				consGridBagConstraints13.gridy = 0;
				consGridBagConstraints13.gridx = 1;
				consGridBagConstraints15.insets =
					new java.awt.Insets(18, 2, 4, 115);
				consGridBagConstraints15.ipadx = 102;
				consGridBagConstraints15.gridwidth = 2;
				consGridBagConstraints15.gridy = 2;
				consGridBagConstraints15.gridx = 2;
				consGridBagConstraints18.insets =
					new java.awt.Insets(4, 23, 10, 23);
				consGridBagConstraints18.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints18.weighty = 1.0;
				consGridBagConstraints18.weightx = 1.0;
				consGridBagConstraints18.gridwidth = 5;
				consGridBagConstraints18.gridy = 3;
				consGridBagConstraints18.gridx = 0;
				consGridBagConstraints103.insets =
					new java.awt.Insets(15, 14, 2, 33);
				consGridBagConstraints103.ipady = 2;
				consGridBagConstraints103.ipadx = 12;
				consGridBagConstraints103.gridy = 0;
				consGridBagConstraints103.gridx = 0;
				consGridBagConstraints108.insets =
					new java.awt.Insets(2, 3, 19, 4);
				consGridBagConstraints108.ipadx = 141;
				consGridBagConstraints108.gridy = 1;
				consGridBagConstraints108.gridx = 3;
				consGridBagConstraints109.insets =
					new java.awt.Insets(2, 4, 18, 26);
				consGridBagConstraints109.ipady = 1;
				consGridBagConstraints109.ipadx = 17;
				consGridBagConstraints109.gridy = 1;
				consGridBagConstraints109.gridx = 4;
				consGridBagConstraints107.insets =
					new java.awt.Insets(18, 2, 4, 115);
				consGridBagConstraints107.ipadx = 102;
				consGridBagConstraints107.gridwidth = 2;
				consGridBagConstraints107.gridy = 2;
				consGridBagConstraints107.gridx = 2;
				consGridBagConstraints16.insets =
					new java.awt.Insets(2, 3, 19, 4);
				consGridBagConstraints16.ipadx = 141;
				consGridBagConstraints16.gridy = 1;
				consGridBagConstraints16.gridx = 3;
				consGridBagConstraints19.insets =
					new java.awt.Insets(10, 19, 16, 57);
				consGridBagConstraints19.ipady = 36;
				consGridBagConstraints19.ipadx = 98;
				consGridBagConstraints19.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints19.weighty = 1.0;
				consGridBagConstraints19.weightx = 1.0;
				consGridBagConstraints19.gridwidth = 3;
				consGridBagConstraints19.gridy = 4;
				consGridBagConstraints19.gridx = 1;
				consGridBagConstraints104.insets =
					new java.awt.Insets(4, 14, 19, 3);
				consGridBagConstraints104.ipady = -2;
				consGridBagConstraints104.ipadx = 19;
				consGridBagConstraints104.gridy = 1;
				consGridBagConstraints104.gridx = 0;
				consGridBagConstraints110.insets =
					new java.awt.Insets(4, 23, 10, 23);
				consGridBagConstraints110.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints110.weighty = 1.0;
				consGridBagConstraints110.weightx = 1.0;
				consGridBagConstraints110.gridwidth = 5;
				consGridBagConstraints110.gridy = 3;
				consGridBagConstraints110.gridx = 0;
				consGridBagConstraints193.insets =
					new java.awt.Insets(10, 19, 16, 57);
				consGridBagConstraints193.ipady = 36;
				consGridBagConstraints193.ipadx = 98;
				consGridBagConstraints193.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints193.weighty = 1.0;
				consGridBagConstraints193.weightx = 1.0;
				consGridBagConstraints193.gridwidth = 3;
				consGridBagConstraints193.gridy = 4;
				consGridBagConstraints193.gridx = 1;
				consGridBagConstraints186.insets =
					new java.awt.Insets(4, 14, 19, 3);
				consGridBagConstraints186.ipady = -2;
				consGridBagConstraints186.ipadx = 19;
				consGridBagConstraints186.gridy = 1;
				consGridBagConstraints186.gridx = 0;
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1
					.setName(
					"FrmSecurityAccessRightsRegSEC006ContentPane1");
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getstcLblEmployeeId(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getstcLblEmployeeName(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getlblEmployeeId(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getlblEmployeeLastName(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getstcLblRegOnly(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getlblEmployeeFirstName(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getlblEmployeeMName(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getJPanel1(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getchkModify(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getchkSubconRenewal(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getchkAddChange(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getchkIssueDrvsEd(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getchkInternetRnwl(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1.add(
					getchkWebAgent(),
					null);
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSecurityAccessRightsRegSEC006ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(531, 367));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmSecurityAccessRightsRegSEC006ContentPane1;
	}

	/**
	 * Get the int equivalent for the boolean parameter
	 * Returns 1 if parameter is true or returns 0.
	 * 
	 * @return int
	 * @param bVal boolean
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
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints80 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints79 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints82 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints83 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints81 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints84 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints85 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints87 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints88 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints86 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints89 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints90 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints84.insets =
					new java.awt.Insets(10, 44, 8, 115);
				consGridBagConstraints84.ipady = -2;
				consGridBagConstraints84.ipadx = 107;
				consGridBagConstraints84.gridy = 0;
				consGridBagConstraints84.gridx = 1;
				consGridBagConstraints79.insets =
					new java.awt.Insets(10, 39, 8, 30);
				consGridBagConstraints79.ipady = -2;
				consGridBagConstraints79.ipadx = 47;
				consGridBagConstraints79.gridy = 0;
				consGridBagConstraints79.gridx = 0;
				consGridBagConstraints80.insets =
					new java.awt.Insets(8, 39, 8, 30);
				consGridBagConstraints80.ipady = -2;
				consGridBagConstraints80.ipadx = 95;
				consGridBagConstraints80.gridy = 1;
				consGridBagConstraints80.gridx = 0;
				consGridBagConstraints82.insets =
					new java.awt.Insets(9, 39, 9, 30);
				consGridBagConstraints82.ipady = -2;
				consGridBagConstraints82.ipadx = 88;
				consGridBagConstraints82.gridy = 3;
				consGridBagConstraints82.gridx = 0;
				consGridBagConstraints85.insets =
					new java.awt.Insets(8, 44, 8, 60);
				consGridBagConstraints85.ipady = -2;
				consGridBagConstraints85.ipadx = 65;
				consGridBagConstraints85.gridy = 1;
				consGridBagConstraints85.gridx = 1;
				consGridBagConstraints83.insets =
					new java.awt.Insets(9, 39, 4, 30);
				consGridBagConstraints83.ipady = -2;
				consGridBagConstraints83.ipadx = 69;
				consGridBagConstraints83.gridy = 4;
				consGridBagConstraints83.gridx = 0;
				consGridBagConstraints81.insets =
					new java.awt.Insets(8, 39, 8, 30);
				consGridBagConstraints81.ipady = -2;
				consGridBagConstraints81.ipadx = 45;
				consGridBagConstraints81.gridy = 2;
				consGridBagConstraints81.gridx = 0;
				consGridBagConstraints87.insets =
					new java.awt.Insets(8, 44, 8, 32);
				consGridBagConstraints87.ipadx = 68;
				consGridBagConstraints87.gridy = 3;
				consGridBagConstraints87.gridx = 1;
				consGridBagConstraints86.insets =
					new java.awt.Insets(8, 44, 8, 60);
				consGridBagConstraints86.ipady = -2;
				consGridBagConstraints86.ipadx = 23;
				consGridBagConstraints86.gridy = 2;
				consGridBagConstraints86.gridx = 1;
				consGridBagConstraints88.insets =
					new java.awt.Insets(8, 44, 3, 121);
				consGridBagConstraints88.ipadx = 42;
				consGridBagConstraints88.gridy = 4;
				consGridBagConstraints88.gridx = 1;
				consGridBagConstraints89.insets =
					new java.awt.Insets(9, 14, 4, 55);
				consGridBagConstraints89.ipady = 168;
				consGridBagConstraints89.ipadx = 159;
				consGridBagConstraints89.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints89.weighty = 1.0;
				consGridBagConstraints89.weightx = 1.0;
				consGridBagConstraints89.gridheight = 5;
				consGridBagConstraints89.gridy = 0;
				consGridBagConstraints89.gridx = 0;
				consGridBagConstraints90.insets =
					new java.awt.Insets(10, 30, 5, 37);
				consGridBagConstraints90.ipady = 166;
				consGridBagConstraints90.ipadx = 251;
				consGridBagConstraints90.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints90.weighty = 1.0;
				consGridBagConstraints90.weightx = 1.0;
				consGridBagConstraints90.gridheight = 5;
				consGridBagConstraints90.gridy = 0;
				consGridBagConstraints90.gridx = 1;
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(null);
				ivjJPanel1.add(getchkRegistrationOnly(), null);
				ivjJPanel1.add(getchkRenewal(), null);
				ivjJPanel1.add(getchkDuplicateReceipt(), null);
				ivjJPanel1.add(getchkExchange(), null);
				ivjJPanel1.add(getchkReplacement(), null);
				ivjJPanel1.setBounds(23, 114, 283, 191);
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
				ivjlblEmployeeFirstName.setBounds(330, 37, 186, 16);
				ivjlblEmployeeFirstName.setName("lblEmployeeFirstName");
				ivjlblEmployeeFirstName.setText(LABEL1);
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
				ivjlblEmployeeId.setBounds(134, 15, 68, 14);
				ivjlblEmployeeId.setName("lblEmployeeId");
				ivjlblEmployeeId.setText(LABEL1);
				ivjlblEmployeeId.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblEmployeeId.setMinimumSize(
					new java.awt.Dimension(45, 14));
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
				ivjlblEmployeeLastName.setBounds(134, 38, 190, 14);
				ivjlblEmployeeLastName.setName("lblEmployeeLastName");
				ivjlblEmployeeLastName.setText(LABEL1);
				ivjlblEmployeeLastName.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblEmployeeLastName.setMinimumSize(
					new java.awt.Dimension(45, 14));
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
				ivjlblEmployeeMName = new JLabel();
				ivjlblEmployeeMName.setBounds(524, 37, 62, 17);
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText(LABEL1);
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
				ivjstcLblEmployeeId.setBounds(14, 15, 83, 18);
				ivjstcLblEmployeeId.setName("stcLblEmployeeId");
				ivjstcLblEmployeeId.setText(EMP_ID);
				ivjstcLblEmployeeId.setMaximumSize(
					new java.awt.Dimension(71, 14));
				ivjstcLblEmployeeId.setMinimumSize(
					new java.awt.Dimension(71, 14));
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
				ivjstcLblEmployeeName.setBounds(14, 39, 113, 14);
				ivjstcLblEmployeeName.setName("stcLblEmployeeName");
				ivjstcLblEmployeeName.setText(EMP_NAME);
				ivjstcLblEmployeeName.setMaximumSize(
					new java.awt.Dimension(94, 14));
				ivjstcLblEmployeeName.setMinimumSize(
					new java.awt.Dimension(94, 14));
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
	 * Return the stcLblRegOnly property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblRegOnly()
	{
		if (ivjstcLblRegOnly == null)
		{
			try
			{
				ivjstcLblRegOnly = new JLabel();
				ivjstcLblRegOnly.setBounds(206, 90, 199, 16);
				ivjstcLblRegOnly.setName("stcLblRegOnly");
				ivjstcLblRegOnly.setText(REG_ONLY);
				ivjstcLblRegOnly.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
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
		return ivjstcLblRegOnly;
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
	 * Handle Selections
	 *
	 * @param aaChkBox 
	 */
	private void handleSelections(JCheckBox aaChkBox)
	{
		if (aaChkBox.isSelected())
		{
			ciSelectedCount++;
		}
		else
		{
			ciSelectedCount--;
		}
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
			// focus manager necessary to make form follow tab tag order
			// Had a focus manager
			//FocusManager.setCurrentManager(
			//	new ContainerOrderFocusManager());
			// user code end
			setName("FrmSecurityAccessRightsRegSEC006");
			setSize(620, 440);
			setTitle(SEC006_FRAME_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(
				getFrmSecurityAccessRightsRegSEC006ContentPane1());
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
		carrBtnSec[0] = getchkRegistrationOnly();
		carrBtnSec[1] = getchkRenewal();
		carrBtnSec[2] = getchkDuplicateReceipt();
		carrBtnSec[3] = getchkExchange();
		carrBtnSec[4] = getchkReplacement();
		carrBtnSec[5] = getchkModify();
		carrBtnSec[6] = getchkSubconRenewal();
		carrBtnSec[7] = getchkAddChange();
		carrBtnSec[8] = getchkIssueDrvsEd();
		// PCR 34 - remove QuickCounter
		// btnArrSec[8] = getchkQuickCounterRenewal();
		// END PCR 34
		carrBtnSec[9] = getchkInternetRnwl();
		// end defect 7891 

		// defect 10717
		carrBtnSec[10] = getchkWebAgent();
		// end defect 10717
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
			// defect 10717
			ciSelectSec = 0;
			for (int i = 0; i < 11; i++)
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
					if (ciSelectSec > 10)
					{
						ciSelectSec = 0;
					}
					if (ciSelectSec < 11
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
					ciSelectSec = ciSelectSec - 1;
					if (ciSelectSec < 0)
					{
						ciSelectSec = 10;
					}
					if (ciSelectSec >= 0
						&& carrBtnSec[ciSelectSec].isEnabled())
					{
						carrBtnSec[ciSelectSec].requestFocus();
						lbStop = false;
					}
				}
				// end defect 10717
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
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSecurityAccessRightsRegSEC006 aFrmSecurityAccessRightsRegSEC006;
			aFrmSecurityAccessRightsRegSEC006 =
				new FrmSecurityAccessRightsRegSEC006();
			aFrmSecurityAccessRightsRegSEC006.setModal(true);
			aFrmSecurityAccessRightsRegSEC006
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			aFrmSecurityAccessRightsRegSEC006.show();
			java.awt.Insets insets =
				aFrmSecurityAccessRightsRegSEC006.getInsets();
			aFrmSecurityAccessRightsRegSEC006.setSize(
				aFrmSecurityAccessRightsRegSEC006.getWidth()
					+ insets.left
					+ insets.right,
				aFrmSecurityAccessRightsRegSEC006.getHeight()
					+ insets.top
					+ insets.bottom);
			aFrmSecurityAccessRightsRegSEC006.setVisible(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeThrowable.printStackTrace(System.out);
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
		// Logic :
		// Based on abVal set the checkboxes selected or remove selection
		// defect 10717
		// defect 10387 
		//for (int i = 1; i <= 9; i++)
		for (int i = 1; i <= 10; i++)
		{
			JCheckBox laChkBox = (JCheckBox) carrBtnSec[i];

			if (laChkBox.isEnabled())
			{
				if (abVal)
				{
					ciSelectedCount++;
				}
				else if (laChkBox.isSelected())
				{
					ciSelectedCount--;
				}
				laChkBox.setSelected(abVal);
			}
		}
		// end defect 10397
		// end defect 10717
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

			// defect 10387
			// Enable disable check boxes
			disableBtnOnOfcId();

			// Registration Only 
			getchkRegistrationOnly().setSelected(
				laSecData.getRegOnlyAccs() == 1);

			// Renewal 
			setSelection(laSecData.getRenwlAccs(), getchkRenewal());

			// Duplicate Receipt 
			setSelection(
				laSecData.getDuplAccs(),
				getchkDuplicateReceipt());

			// Exchange 
			setSelection(laSecData.getExchAccs(), getchkExchange());

			// Replacement 
			setSelection(laSecData.getReplAccs(), getchkReplacement());

			// Modifiy 
			setSelection(laSecData.getModfyAccs(), getchkModify());

			// SubconRenwlAccs 
			setSelection(
				laSecData.getSubconRenwlAccs(),
				getchkSubconRenewal());

			// Driver Education 	
			setSelection(
				laSecData.getIssueDrvsEdAccs(),
				getchkIssueDrvsEd());

			// Address Change 
			setSelection(
				laSecData.getAddrChngAccs(),
				getchkAddChange());

			// defect 10717
			setSelection(laSecData.getWebAgntAccs(), getchkWebAgent());
			// end defect 10717

			// Internet Address Changes
			// Removed Internet AddressChange Report
			// Check for isCounty() to be removed w/ defect 10388
			//   after 6.4.0 
			if (SystemProperty.isCounty())
			{
				setSelection(
					laSecData.getItrntRenwlAccs(),
					getchkInternetRnwl());
			}
			// end defect 10387
		}
		checkCountZero();
	}

	/** 
	 * Set Checkbox Selection.  Increment Selected Count. 
	 * 
	 * @param aiValue
	 * @param aaChkBox 
	 */
	private void setSelection(int aiValue, JCheckBox aaChkBox)
	{
		if (aiValue == 1)
		{
			aaChkBox.setSelected(true);
			ciSelectedCount = ciSelectedCount + 1;
		}
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Registration.
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC006(true);

		//Take the new Values from the window and set to data object
		caSecData.getSecData().setAddrChngAccs(
			getIntValue(getchkAddChange().isSelected()));

		caSecData.getSecData().setDuplAccs(
			getIntValue(getchkDuplicateReceipt().isSelected()));

		caSecData.getSecData().setExchAccs(
			getIntValue(getchkExchange().isSelected()));

		caSecData.getSecData().setModfyAccs(
			getIntValue(getchkModify().isSelected()));

		caSecData.getSecData().setRegOnlyAccs(
			getIntValue(getchkRegistrationOnly().isSelected()));

		caSecData.getSecData().setRenwlAccs(
			getIntValue(getchkRenewal().isSelected()));

		caSecData.getSecData().setReplAccs(
			getIntValue(getchkReplacement().isSelected()));

		caSecData.getSecData().setSubconRenwlAccs(
			getIntValue(getchkSubconRenewal().isSelected()));

		caSecData.getSecData().setItrntRenwlAccs(
			getIntValue(getchkInternetRnwl().isSelected()));

		//PCR DRVED
		caSecData.getSecData().setIssueDrvsEdAccs(
			getIntValue(getchkIssueDrvsEd().isSelected()));

		//defect 10717
		caSecData.getSecData().setWebAgntAccs(
			getIntValue(getchkWebAgent().isSelected()));
		// end defect 10717
	}

}