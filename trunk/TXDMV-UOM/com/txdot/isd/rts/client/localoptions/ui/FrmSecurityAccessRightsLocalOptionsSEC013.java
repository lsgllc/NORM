package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.SecurityClientDataObject;
import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;

/*
 *
 * FrmSecurityAccessRightsLocalOptionsSEC013.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/04/2004	Corrected spelling of class
 *							defect 6909 Ver 5.1.6
 * Min Wang		07/20/2004	Add check box for RSPS status update
 *							add getchkRSPSUpdt()
 *							modify actionPerformed(), initialize(),
 *								   selectChkButtons(),  	
 *								   setValuesToDataObj(), VC	
 *							defect 7310 Ver 5.2.1
 * Min Wang		11/15/2004	ensure object is not null before referring
 * 							to it.
 *							modify setData()
 *							defect 7031 Ver 5.2.2
 * Min Wang 	11/24/2004	Fix space bar does not enable all in HQ.
 *							modify selectChkButtons()
 *							defect 7100 Ver 5.2.2
 * Min Wang		03/15/2005	Make basic RTS 5.2.3 changes.
 * 							organize imports, format source.
 * 							modify handleException()
 *							defect 7891  Ver 5.2.3 
 * Min Wang		04/06/2005	Remove arrays since they are having 
 * 							initialization problems
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
 * Min Wang		08/31/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		12/13/2005  Uncomment code for arrow key function.
 * 							modify keyPressed(), initialize()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	01/19/2006	Align Employee Id & Employee Name to match
 * 							other Security Screens. 
 * 							defect 7891 Ver 5.2.3 
 * Min Wang		06/15/2006  Fix clipped text "Subcontractor Report &
 * 							Updates.
 * 							modify getchkSubcontRprtUpdt()
 * 							defect 8758 Ver 5.2.3
 * K Harrell	04/20/2007	Implemented SystemProperty.isCounty(),
 * 							 isRegion() 
 * 							modify disableBtnOnOfcId()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/18/2008	Allow HQ to access Dealer Update/Reports
 * 							modify disableBtnOnOfcId()
 * 							defect 9654 Defect_POS_A 	    
 * K Harrell	09/10/2008	Add checkboxes for Dealer, Subcon, Lienholder
 * 							Report only.  Realign via Visual Editor.
 * 							If an Update is selected, the corresponding
 * 							Report is selected and disabled. Could select
 * 							Admin alone but would not select Local Options.
 * 							Class cleanup for mnemonic standards. 
 * 							add ivjchkDlrRpt, ivjchkLienhldrRpt, 
 * 							 ivjchkSubconRpt, jPanel, get methods()
 * 							add DEALER_RPT, LIENHOLDR_RPT, SUBCON_RPT 
 * 							modify actionPerformed(),disableBtnOnOfcId(),
 * 							  checkCountZero(), setData(), initialize(),
 * 							  getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1(),
 * 							  setValuesToDataObj(), selectChkButtons(),
 * 							  keyPressed(), carrBtnSec[]
 * 							renamed variables, methods for clarity
 * 							defect 9710 Defect_POS_B
 * J Zwiener	08/01/2009	add "s" to "Lienholder Reports" using visual
 * 							  editor
 * 							defect 10088 Defect_POS_F
 * K Harrell	01/20/2011	add chkbox for BatchRptMgmt
 * 							add BATCHRPTMGMT
 * 							add ivjBatchRptMgmt, get method 
 * 							modify actionPerformed(),disableBtnOnOfcId(),
 * 							  checkCountZero(), setData(), initialize(),
 * 							  getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1(),
 * 							  setValuesToDataObj(), selectChkButtons(),
 * 							  carrBtnSec[], getJPanel(),keyPressed() 
 * 							defect 10701 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class is used for managing security access rights for 
 * Local options. Data on this screen is managed through
 * SecurityClientDataObject object.
 *
 * @version	6.7.0			01/20/2011  
 * @author Administrator
 * <br>Creation Date:		06/28/2001 17:17:23 
 */

public class FrmSecurityAccessRightsLocalOptionsSEC013
	extends RTSDialogBox
	implements ActionListener
{
	private JCheckBox ivjchkLocalOptions = null;
	private JCheckBox ivjchkSecurity = null;
	private JCheckBox ivjchkRSPSUpdt = null;
	private JCheckBox ivjchkAdministration = null;
	private JCheckBox ivjchkCreditCards = null;
	private JCheckBox ivjchkDlrUpdt = null;
	private JCheckBox ivjchkLienhldrUpdt = null;
	private JCheckBox ivjchkSubconUpdt = null;
	private JCheckBox ivjchkDlrRpt = null;
	private JCheckBox ivjchkSubconRpt = null;
	private JCheckBox ivjchkLienhldrRpt = null;
	// defect 10701 
	private JCheckBox[] carrBtnSec = new JCheckBox[12];
	// end defect 10701

	// Panel to facilitate tabbing 
	private javax.swing.JPanel jPanel = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private JLabel ivjstcLblLocalOptions = null;
	private JLabel ivjstcLblComm1 = null;
	private JLabel ivjstcLblComm2 = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private SecurityClientDataObject caSecData = null;
	private RTSButton[] carrBtn = new RTSButton[3];

	private JPanel ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1 =
		null;

	private boolean cbSelectAll = false;
	private int ciSelectedCount = 0;
	private int ciSelect = 0;
	private int ciSelectSec = 0;

	// Static 
	private static final String ADMINISTRATION = "Administration";
	private static final String CREDIT_CARD_FEE_UPDT =
		"Credit Card Fee Update";

	// defect 9710
	// Renames  
	private static final String DEALER_UPDT = "Dealer Updates";
	private static final String LIENHOLDR_UPDT = "Lienholder Updates";
	private static final String SUBCON_UPDT = "Subcontractor Updates";
	// New 
	private static final String DEALER_RPT = "Dealer Report";
	private static final String LIENHOLDR_RPT = "Lienholder Reports";
	private static final String SUBCON_RPT = "Subcontractor Report";
	// end defect 9710 	

	// defect 10701 
	private static final String BATCHRPTMGMT =
		"Batch Report Management";
	// end defect 10701 

	private static final String LOCAL_OPT = "Local Options";
	private static final String RSPS_STATUS_UPDT =
		"RSPS Status Updates";
	private static final String SECURITY = "Security";

	private static final String LABEL1 = "JLabel1";
	private static final String LABEL2 = "JLabel2";
	private static final String SECURITY_CHECKED =
		"If 'Security' is checked, access will also be allowed for";
	private static final String SECURITY_RPT =
		"Employee Security & Employee Security Reports.";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String SEC013_FRAME_TITLE =
		"Security Access Rights Local Options   SEC013";

	// defect 10701 	
	private JCheckBox ivjchkBatchRptMgmt = null;
	// end defect 10701 

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013(Frame aaOwner)
	{
		super(aaOwner);
	}

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013(
		Frame owner,
		String asTitle)
	{
		super(owner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsLocalOptionsSEC013 constructor comment.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmSecurityAccessRightsLocalOptionsSEC013(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
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
			int liSelectCountDelta = 0;
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				caSecData.setSEC013(false);
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
				RTSHelp.displayHelp(RTSHelp.SEC013);
				return;
			}
			// LOCAL OPTIONS 
			else if (aaAE.getSource() == getchkLocalOptions())
			{
				if (!getchkLocalOptions().isSelected())
				{
					cbSelectAll = false;
				}
				else
				{
					cbSelectAll = true;
				}
				selectChkButtons(cbSelectAll);
			}
			// defect 9710 
			// RPT will ALWAYS be selected/disabled if Update selected 
			// If Update is Selected, Rpt is Selected & Disabled
			//   In the ciSelectedCount, must take into consideration
			//    scenario where Rpt selected but Updt is not.  
			// If Update is Deselected, Rpt is Deselected/Enabled

			// DEALER UPDATE  
			else if (aaAE.getSource() == getchkDlrUpdt())
			{
				liSelectCountDelta =
					getchkDlrUpdt().isSelected() ? 1 : -2;
				if (!getchkDlrRpt().isSelected()
					&& getchkDlrUpdt().isSelected())
				{
					liSelectCountDelta++;
				}
				ciSelectedCount = ciSelectedCount + liSelectCountDelta;
				getchkDlrRpt().setSelected(
					getchkDlrUpdt().isSelected());
				getchkDlrRpt().setEnabled(
					!getchkDlrUpdt().isSelected());
			}
			// DEALER REPORT
			else if (aaAE.getSource() == getchkDlrRpt())
			{
				ciSelectedCount =
					ciSelectedCount
						+ (getchkDlrRpt().isSelected() ? 1 : -1);
			}
			// LIENHOLDER UPDATE
			else if (aaAE.getSource() == getchkLienhldrUpdt())
			{
				liSelectCountDelta =
					getchkLienhldrUpdt().isSelected() ? 1 : -2;
				if (!getchkLienhldrRpt().isSelected()
					&& getchkLienhldrUpdt().isSelected())
				{
					liSelectCountDelta++;
				}
				ciSelectedCount = ciSelectedCount + liSelectCountDelta;
				getchkLienhldrRpt().setSelected(
					getchkLienhldrUpdt().isSelected());
				getchkLienhldrRpt().setEnabled(
					!getchkLienhldrUpdt().isSelected());
			}
			// LIENHOLDER REPORT
			else if (aaAE.getSource() == getchkLienhldrRpt())
			{
				ciSelectedCount =
					ciSelectedCount
						+ (getchkLienhldrRpt().isSelected() ? 1 : -1);
			}
			// SUBCON UPDATE 
			else if (aaAE.getSource() == getchkSubconUpdt())
			{
				liSelectCountDelta =
					getchkSubconUpdt().isSelected() ? 1 : -2;
				if (!getchkSubconRpt().isSelected()
					&& getchkSubconUpdt().isSelected())
				{
					liSelectCountDelta++;
				}
				ciSelectedCount = ciSelectedCount + liSelectCountDelta;
				getchkSubconRpt().setSelected(
					getchkSubconUpdt().isSelected());
				getchkSubconRpt().setEnabled(
					!getchkSubconUpdt().isSelected());
			}
			// SUBCON REPORT
			else if (aaAE.getSource() == getchkSubconRpt())
			{
				ciSelectedCount =
					ciSelectedCount
						+ (getchkSubconRpt().isSelected() ? 1 : -1);
			}
			// end defect 9710  

			// ADMINISTRATION 
			else if (aaAE.getSource() == getchkAdministration())
			{
				ciSelectedCount =
					ciSelectedCount
						+ (getchkAdministration().isSelected() ? 1 : -1);
			}
			// SECURITY 
			else if (aaAE.getSource() == getchkSecurity())
			{
				if (getchkSecurity().isSelected())
				{
					ciSelectedCount++;
				}
				else
				{
					ciSelectedCount--;
				}

			}

			// CREDIT CARD FEES
			else if (aaAE.getSource() == getchkCreditCards())
			{
				if (getchkCreditCards().isSelected())
				{
					ciSelectedCount++;
				}
				else
				{
					ciSelectedCount--;
				}
			}
			// defect 7310
			// RSPS STATUS UPDATE 
			else if (aaAE.getSource() == getchkRSPSUpdt())
			{
				if (getchkRSPSUpdt().isSelected())
				{
					ciSelectedCount++;
				}
				else
				{
					ciSelectedCount--;
				}
				//end defect 7310
			}
			// defect 10701 
			else if (aaAE.getSource() == getchkBatchRptMgmt())
			{
				if (getchkBatchRptMgmt().isSelected())
				{
					ciSelectedCount++;
				}
				else
				{
					ciSelectedCount--;
				}
			}
			// end defect 10701 

			checkCountZero();
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Check for EnableCount field for zero value and set the 
	 * LocalOptions checkbox 
	 */
	private void checkCountZero()
	{
		// defect 9710 
		getchkLocalOptions().setSelected(ciSelectedCount != 0);
		// end defect 9710 
	}

	/**
	 * Disable the Dealer Update & Report checkboxes for Region and VTR
	 */
	private void disableBtnOnOfcId()
	{
		// Allow HQ to access Dealer Report/Update 
		getchkDlrUpdt().setEnabled(!SystemProperty.isRegion());
		getchkDlrRpt().setEnabled(!SystemProperty.isRegion());
		getchkCreditCards().setEnabled(!SystemProperty.isHQ());

		// defect 10701 
		getchkBatchRptMgmt().setEnabled(!SystemProperty.isHQ());
		// end defect 10701 
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
				ivjButtonPanel1.setBounds(189, 367, 220, 37);
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
	 * Return the chkAdministration property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkAdministration()
	{
		if (ivjchkAdministration == null)
		{
			try
			{
				ivjchkAdministration = new javax.swing.JCheckBox();
				ivjchkAdministration.setSize(108, 22);
				ivjchkAdministration.setName("chkAdministration");
				ivjchkAdministration.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				ivjchkAdministration.setText(ADMINISTRATION);
				ivjchkAdministration.setMaximumSize(
					new Dimension(108, 22));
				ivjchkAdministration.setActionCommand(ADMINISTRATION);
				ivjchkAdministration.setMinimumSize(
					new Dimension(108, 22));
				ivjchkAdministration.setLocation(361, 267);
				ivjchkAdministration.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkAdministration;
	}

	/**
	 * Return the chkCreditCards property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkCreditCards()
	{
		if (ivjchkCreditCards == null)
		{
			try
			{
				ivjchkCreditCards = new JCheckBox();
				ivjchkCreditCards.setSize(155, 22);
				ivjchkCreditCards.setName("chkCreditCards");
				ivjchkCreditCards.setMnemonic(
					java.awt.event.KeyEvent.VK_C);
				ivjchkCreditCards.setText(CREDIT_CARD_FEE_UPDT);
				ivjchkCreditCards.setLocation(14, 117);
				ivjchkCreditCards.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkCreditCards;
	}

	/**
	 * Return the chkDealerRprtUpdt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDlrUpdt()
	{
		if (ivjchkDlrUpdt == null)
		{
			try
			{
				ivjchkDlrUpdt = new JCheckBox();
				ivjchkDlrUpdt.setBounds(14, 33, 165, 22);
				ivjchkDlrUpdt.setName("chkDealerRprtUpdt");
				ivjchkDlrUpdt.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjchkDlrUpdt.setText(DEALER_UPDT);
				ivjchkDlrUpdt.setMaximumSize(new Dimension(172, 22));
				ivjchkDlrUpdt.setActionCommand(DEALER_UPDT);
				ivjchkDlrUpdt.setMinimumSize(new Dimension(172, 22));
				// user code begin {1}
				ivjchkDlrUpdt.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDlrUpdt;
	}

	/**
	 * Return the chkLienhldrUpdt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkLienhldrUpdt()
	{
		if (ivjchkLienhldrUpdt == null)
		{
			try
			{
				ivjchkLienhldrUpdt = new JCheckBox();
				ivjchkLienhldrUpdt.setSize(172, 22);
				ivjchkLienhldrUpdt.setName("chkLienhldrUpdt");
				ivjchkLienhldrUpdt.setMnemonic(
					java.awt.event.KeyEvent.VK_I);
				ivjchkLienhldrUpdt.setText(LIENHOLDR_UPDT);
				ivjchkLienhldrUpdt.setMaximumSize(
					new Dimension(202, 22));
				ivjchkLienhldrUpdt.setActionCommand(LIENHOLDR_UPDT);
				ivjchkLienhldrUpdt.setMinimumSize(
					new Dimension(202, 22));
				// user code begin {1}
				ivjchkLienhldrUpdt.setLocation(14, 89);
				ivjchkLienhldrUpdt.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkLienhldrUpdt;
	}

	/**
	 * Return the chkLocalOptions property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkLocalOptions()
	{
		if (ivjchkLocalOptions == null)
		{
			try
			{
				ivjchkLocalOptions = new JCheckBox();
				ivjchkLocalOptions.setBounds(14, 5, 215, 22);
				ivjchkLocalOptions.setName("chkLocalOptions");
				ivjchkLocalOptions.setMnemonic(
					java.awt.event.KeyEvent.VK_L);
				ivjchkLocalOptions.setText(LOCAL_OPT);
				ivjchkLocalOptions.setMaximumSize(
					new Dimension(103, 22));
				ivjchkLocalOptions.setActionCommand(LOCAL_OPT);
				ivjchkLocalOptions.setMinimumSize(
					new Dimension(103, 22));
				// user code begin {1}
				ivjchkLocalOptions.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkLocalOptions;
	}

	/**
	 * Return the chkRSPSUpdt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkRSPSUpdt()
	{
		if (ivjchkRSPSUpdt == null)
		{
			try
			{
				ivjchkRSPSUpdt = new JCheckBox();
				ivjchkRSPSUpdt.setSize(161, 22);
				ivjchkRSPSUpdt.setName("chkRSPSUpdt");
				ivjchkRSPSUpdt.setMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjchkRSPSUpdt.setText(RSPS_STATUS_UPDT);
				ivjchkRSPSUpdt.setLocation(14, 145);
				ivjchkRSPSUpdt.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkRSPSUpdt;
	}

	/**
	 * Return the chkSecurity property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSecurity()
	{
		if (ivjchkSecurity == null)
		{
			try
			{
				ivjchkSecurity = new JCheckBox();
				ivjchkSecurity.setBounds(361, 239, 181, 22);
				ivjchkSecurity.setName("chkSecurity");
				ivjchkSecurity.setMnemonic(
					java.awt.event.KeyEvent.VK_Y);
				ivjchkSecurity.setText(SECURITY);
				ivjchkSecurity.setMaximumSize(new Dimension(72, 22));
				ivjchkSecurity.setActionCommand(SECURITY);
				ivjchkSecurity.setMinimumSize(new Dimension(72, 22));
				// user code begin {1}
				ivjchkSecurity.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSecurity;
	}

	/**
	 * Return the chkSubcontRprtUpdt property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSubconUpdt()
	{
		if (ivjchkSubconUpdt == null)
		{
			try
			{
				ivjchkSubconUpdt = new JCheckBox();
				ivjchkSubconUpdt.setBounds(14, 61, 170, 22);
				ivjchkSubconUpdt.setName("chkSubcontRprtUpdt");
				ivjchkSubconUpdt.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjchkSubconUpdt.setText(SUBCON_UPDT);
				ivjchkSubconUpdt.setMaximumSize(new Dimension(217, 22));
				ivjchkSubconUpdt.setActionCommand(SUBCON_UPDT);
				// defect 8758
				ivjchkSubconUpdt.setMinimumSize(new Dimension(217, 22));
				// user code begin {1}
				ivjchkSubconUpdt.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSubconUpdt;
	}

	/**
	 * Return the FrmSecurityAccessRightsLocalOptionsSEC013ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
			== null)
		{
			try
			{
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1 =
					new JPanel();
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.setName(
					"FrmSecurityAccessRightsLocalOptionsSEC013ContentPane1");
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.setMaximumSize(
					new Dimension(481, 352));
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.setMinimumSize(
					new Dimension(481, 352));
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getstcLblEmployeeId(),
					getstcLblEmployeeId().getName());
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getstcLblEmployeeName(),
					getstcLblEmployeeName().getName());
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getlblEmployeeLastName(),
					getlblEmployeeLastName().getName());
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getlblEmployeeId(),
					getlblEmployeeId().getName());
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getstcLblLocalOptions(),
					getstcLblLocalOptions().getName());
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getstcLblComm1(),
					getstcLblComm1().getName());
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getstcLblComm2(),
					getstcLblComm2().getName());
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getlblEmployeeFirstName(),
					getlblEmployeeFirstName().getName());
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1()
					.add(
					getlblEmployeeMName(),
					getlblEmployeeMName().getName());
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.add(
					getJPanel(),
					null);
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.add(
					getchkDlrRpt(),
					null);
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.add(
					getchkSubconRpt(),
					null);
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.add(
					getchkLienhldrRpt(),
					null);
				// defect 10701 
				// Replaced Credit Card 
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.add(
					getchkSecurity(),
					null);
				// end defect 10701
				ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1
					.add(
					getchkAdministration(),
					null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1;
	}

	/**
	 * Get the int equivalent for the boolean parameter
	 * Returns 1 if parameter is true or returns 0.
	 * 
	 * @return int
	 * @param lbVal boolean
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
				ivjlblEmployeeFirstName.setBounds(338, 30, 179, 14);
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
	private javax.swing.JLabel getlblEmployeeId()
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
				ivjlblEmployeeId.setBounds(134, 13, 108, 14);
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
				ivjlblEmployeeLastName.setBounds(134, 30, 193, 14);
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
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText(LABEL2);
				ivjlblEmployeeMName.setBounds(524, 30, 47, 14);
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
	 * Return the stcLblComm1 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblComm1()
	{
		if (ivjstcLblComm1 == null)
		{
			try
			{
				ivjstcLblComm1 = new JLabel();
				ivjstcLblComm1.setName("stcLblComm1");
				ivjstcLblComm1.setText(SECURITY_CHECKED);
				ivjstcLblComm1.setMaximumSize(new Dimension(308, 14));
				ivjstcLblComm1.setMinimumSize(new Dimension(308, 14));
				ivjstcLblComm1.setBounds(142, 311, 336, 18);
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
		return ivjstcLblComm1;
	}

	/**
	 * Return the stcLblComm2 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblComm2()
	{
		if (ivjstcLblComm2 == null)
		{
			try
			{
				ivjstcLblComm2 = new JLabel();
				ivjstcLblComm2.setName("stcLblComm2");
				ivjstcLblComm2.setText(SECURITY_RPT);
				ivjstcLblComm2.setMaximumSize(new Dimension(273, 14));
				ivjstcLblComm2.setMinimumSize(new Dimension(273, 14));
				ivjstcLblComm2.setBounds(143, 334, 336, 18);
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
		return ivjstcLblComm2;
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
				ivjstcLblEmployeeId.setBounds(15, 9, 74, 18);
				ivjstcLblEmployeeId.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
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
				ivjstcLblEmployeeName.setBounds(15, 30, 102, 14);
				ivjstcLblEmployeeName.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
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
	 * Return the stcLblLocalOptions property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblLocalOptions()
	{
		if (ivjstcLblLocalOptions == null)
		{
			try
			{
				ivjstcLblLocalOptions = new JLabel();
				ivjstcLblLocalOptions.setName("stcLblLocalOptions");
				ivjstcLblLocalOptions.setText(LOCAL_OPT);
				ivjstcLblLocalOptions.setMaximumSize(
					new Dimension(78, 14));
				ivjstcLblLocalOptions.setMinimumSize(
					new Dimension(78, 14));
				ivjstcLblLocalOptions.setBounds(217, 63, 165, 14);
				ivjstcLblLocalOptions.setHorizontalAlignment(
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
		return ivjstcLblLocalOptions;
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
			setName("FrmSecurityAccessRightsLocalOptionsSEC013");
			setSize(600, 445);
			setTitle(SEC013_FRAME_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(
				getFrmSecurityAccessRightsLocalOptionsSEC013ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		carrBtn[0] = getButtonPanel1().getBtnEnter();
		carrBtn[1] = getButtonPanel1().getBtnCancel();
		carrBtn[2] = getButtonPanel1().getBtnHelp();

		carrBtnSec[0] = getchkLocalOptions();
		carrBtnSec[1] = getchkDlrUpdt();
		carrBtnSec[2] = getchkSubconUpdt();
		carrBtnSec[3] = getchkLienhldrUpdt();
		carrBtnSec[4] = getchkCreditCards();
		carrBtnSec[5] = getchkRSPSUpdt();

		// defect 10701 
		carrBtnSec[6] = getchkBatchRptMgmt();
		carrBtnSec[7] = getchkDlrRpt();
		carrBtnSec[8] = getchkSubconRpt();
		carrBtnSec[9] = getchkLienhldrRpt();
		carrBtnSec[10] = getchkSecurity();
		carrBtnSec[11] = getchkAdministration();
		// end defect 10701 

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
			for (int i = 0; i < carrBtnSec.length; i++)
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
					if (ciSelectSec > (carrBtnSec.length - 1))
					{
						ciSelectSec = 0;
					}
					// defect 10701 
					if (ciSelectSec < carrBtnSec.length
						&& carrBtnSec[ciSelectSec].isEnabled())
					{
						// end defect 10701
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
						ciSelectSec = (carrBtnSec.length - 1);
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
		// end defect 7891
		super.keyPressed(aaKE);

	}

	/**
	 * main entrypoint - starts the part when it is run as 
	 * an application
	 * 
	 * @param aarrArgs  String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSecurityAccessRightsLocalOptionsSEC013 laFrmSecurityAccessRightsLocalOptionsSEC013;
			laFrmSecurityAccessRightsLocalOptionsSEC013 =
				new FrmSecurityAccessRightsLocalOptionsSEC013();
			laFrmSecurityAccessRightsLocalOptionsSEC013.setModal(true);
			laFrmSecurityAccessRightsLocalOptionsSEC013
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSecurityAccessRightsLocalOptionsSEC013.show();
			Insets insets =
				laFrmSecurityAccessRightsLocalOptionsSEC013.getInsets();
			laFrmSecurityAccessRightsLocalOptionsSEC013.setSize(
				laFrmSecurityAccessRightsLocalOptionsSEC013.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSecurityAccessRightsLocalOptionsSEC013.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSecurityAccessRightsLocalOptionsSEC013.setVisibleRTS(
				true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(
				"Exception occurred in main() of javax.swing.JDialog");
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
		if (getchkAdministration().isEnabled())
		{
			//if is selected unselect
			if (getchkAdministration().isSelected() && !abVal)
			{
				getchkAdministration().setSelected(abVal);
				// defect 9710 
				ciSelectedCount--;
				// end defect 9710 
			}
		}
		// defect 9710 
		if (getchkDlrUpdt().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkDlrUpdt().isSelected())
			{
				ciSelectedCount--;
			}
			getchkDlrUpdt().setSelected(abVal);
		}

		if (getchkDlrRpt().isEnabled() || getchkDlrRpt().isSelected())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkDlrRpt().isSelected())
			{
				ciSelectedCount--;
			}
			getchkDlrRpt().setSelected(abVal);
			getchkDlrRpt().setEnabled(!abVal);
		}

		if (getchkLienhldrUpdt().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkLienhldrUpdt().isSelected())
			{
				ciSelectedCount--;
			}
			getchkLienhldrUpdt().setSelected(abVal);
		}

		if (getchkLienhldrRpt().isEnabled()
			|| getchkLienhldrRpt().isSelected())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkLienhldrRpt().isSelected())
			{
				ciSelectedCount--;
			}
			getchkLienhldrRpt().setSelected(abVal);
			getchkLienhldrRpt().setEnabled(!abVal);
		}

		if (getchkRSPSUpdt().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkRSPSUpdt().isSelected())
			{
				ciSelectedCount--;
			}
			getchkRSPSUpdt().setSelected(abVal);
		}

		// defect 10701 
		if (getchkBatchRptMgmt().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkBatchRptMgmt().isSelected())
			{
				ciSelectedCount--;
			}
			getchkBatchRptMgmt().setSelected(abVal);
		}
		// end defect 10701 

		if (getchkSecurity().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkSecurity().isSelected())
			{
				ciSelectedCount--;
			}
			getchkSecurity().setSelected(abVal);
		}

		if (getchkSubconUpdt().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkSubconUpdt().isSelected())
			{
				ciSelectedCount--;
			}
			getchkSubconUpdt().setSelected(abVal);
		}

		if (getchkSubconRpt().isEnabled()
			|| getchkSubconRpt().isSelected())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkSubconRpt().isSelected())
			{
				ciSelectedCount--;
			}
			getchkSubconRpt().setSelected(abVal);
			getchkSubconRpt().setEnabled(!abVal);
		}

		if (getchkCreditCards().isEnabled())
		{
			if (abVal)
			{
				ciSelectedCount++;
			}
			else if (getchkCreditCards().isSelected())
			{
				ciSelectedCount--;
			}
			getchkCreditCards().setSelected(abVal);
		}
		// end defect 9710

	}

	/**
	 * This sets the data on the screen and is how the controller relays 
	 * information to the view
	 * 
	 * @param aaSecObj Object
	 */
	public void setData(Object aaSecObj)
	{
		if (aaSecObj != null)
		{
			//Security information of the employee entered on SEC005 screen.
			caSecData =
				(SecurityClientDataObject)
					((Vector) aaSecObj).elementAt(
					1);
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

			if (laSecData.getLocalOptionsAccs() == 1)
				getchkLocalOptions().setSelected(true);

			if (laSecData.getSubconAccs() == 1)
			{
				getchkSubconUpdt().setSelected(true);
				ciSelectedCount++;
			}
			if (laSecData.getDlrAccs() == 1)
			{
				getchkDlrUpdt().setSelected(true);
				ciSelectedCount++;
			}

			if (laSecData.getLienHldrAccs() == 1)
			{
				getchkLienhldrUpdt().setSelected(true);
				ciSelectedCount++;
			}

			// defect 9710
			// Add RptAccs for Subcon, Dealer, Lienholder 
			if (laSecData.getDlrAccs() == 1
				|| laSecData.getDlrRptAccs() == 1)
			{
				getchkDlrRpt().setSelected(true);
				getchkDlrRpt().setEnabled(laSecData.getDlrAccs() == 0);
				ciSelectedCount++;
			}

			if (laSecData.getLienHldrAccs() == 1
				|| laSecData.getLienHldrRptAccs() == 1)
			{
				getchkLienhldrRpt().setSelected(true);
				getchkLienhldrRpt().setEnabled(
					laSecData.getLienHldrAccs() == 0);
				ciSelectedCount++;
			}

			if (laSecData.getSubconAccs() == 1
				|| laSecData.getSubconRptAccs() == 1)
			{
				getchkSubconRpt().setSelected(true);
				getchkSubconRpt().setEnabled(
					laSecData.getSubconAccs() == 0);
				ciSelectedCount++;
			}
			// end defect 9710 

			//defect 7310
			//added for RSPS status update	
			if (laSecData.getRSPSUpdtAccs() == 1)
			{
				getchkRSPSUpdt().setSelected(true);
				ciSelectedCount++;
			}
			//defect 7310

			// defect 10701 
			if (laSecData.getBatchRptMgmtAccs() == 1)
			{
				getchkBatchRptMgmt().setSelected(true);
				ciSelectedCount++;
			}
			// end defect 10701 

			if (laSecData.getSecrtyAccs() == 1)
			{
				getchkSecurity().setSelected(true);
				ciSelectedCount++;
			}
			// added for credit card fees
			if (laSecData.getCrdtCardFeeAccs() == 1)
			{
				getchkCreditCards().setSelected(true);
				ciSelectedCount++;
			}

			//Security information of currently logged employee
			SecurityClientDataObject CurrEmpSecClntData =
				(SecurityClientDataObject)
					((Vector) aaSecObj).elementAt(
					0);
			SecurityData CurrEmpSecData =
				(SecurityData) CurrEmpSecClntData.getSecData();

			//If the current user does not have administrative access rights
			//disable the checkbox for Administration Access
			//defect 7031
			if (CurrEmpSecData == null
				|| CurrEmpSecData.getAdminAccs() != 1)
			{
				getchkAdministration().setEnabled(false);
			}
			else
			{
				if (laSecData.getAdminAccs() == 1)
				{
					getchkAdministration().setSelected(true);
					// defect 9710 
					ciSelectedCount++;
					// end defect 9710 
				}
			}
			//end defect 7031	
		}
		checkCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Local Options.
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC013(true);

		//Take the new Values fromt the window and set to data object
		caSecData.getSecData().setAdminAccs(
			getIntValue(getchkAdministration().isSelected()));
		caSecData.getSecData().setDlrAccs(
			getIntValue(getchkDlrUpdt().isSelected()));
		caSecData.getSecData().setLienHldrAccs(
			getIntValue(getchkLienhldrUpdt().isSelected()));
		caSecData.getSecData().setLocalOptionsAccs(
			getIntValue(getchkLocalOptions().isSelected()));
		// defect 9710 
		caSecData.getSecData().setDlrRptAccs(
			getIntValue(getchkDlrRpt().isSelected()));
		caSecData.getSecData().setLienHldrRptAccs(
			getIntValue(getchkLienhldrRpt().isSelected()));
		caSecData.getSecData().setSubconRptAccs(
			getIntValue(getchkSubconRpt().isSelected()));
		// end defect 9710 

		caSecData.getSecData().setSecrtyAccs(
			getIntValue(getchkSecurity().isSelected()));
		caSecData.getSecData().setEmpSecrtyAccs(
			getIntValue(getchkSecurity().isSelected()));
		caSecData.getSecData().setEmpSecrtyRptAccs(
			getIntValue(getchkSecurity().isSelected()));

		caSecData.getSecData().setSubconAccs(
			getIntValue(getchkSubconUpdt().isSelected()));

		//defect 7310
		//added RSPS status update
		caSecData.getSecData().setRSPSUpdtAccs(
			getIntValue(getchkRSPSUpdt().isSelected()));
		//end defect 7310

		// added for Credit Card Fees
		caSecData.getSecData().setCrdtCardFeeAccs(
			getIntValue(getchkCreditCards().isSelected()));

		// defect 10701 
		caSecData.getSecData().setBatchRptMgmtAccs(
			getIntValue(getchkBatchRptMgmt().isSelected()));
		// end defect 10701 

	}

	/**
	 * This method initializes ivjchkDlrRpt
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkDlrRpt()
	{
		if (ivjchkDlrRpt == null)
		{
			ivjchkDlrRpt = new javax.swing.JCheckBox();
			ivjchkDlrRpt.setSize(103, 22);
			ivjchkDlrRpt.setText(DEALER_RPT);
			// user code begin {1}
			ivjchkDlrRpt.addActionListener(this);
			ivjchkDlrRpt.setMnemonic(java.awt.event.KeyEvent.VK_E);
			// user code end

			ivjchkDlrRpt.setLocation(361, 127);
		}
		return ivjchkDlrRpt;
	}

	/**
	 * This method initializes ivjchkSubconRpt
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkSubconRpt()
	{
		if (ivjchkSubconRpt == null)
		{
			ivjchkSubconRpt = new javax.swing.JCheckBox();
			ivjchkSubconRpt.setSize(148, 22);
			ivjchkSubconRpt.setText(SUBCON_RPT);
			// user code begin {1}
			ivjchkSubconRpt.setMnemonic(java.awt.event.KeyEvent.VK_U);
			ivjchkSubconRpt.setLocation(361, 155);
			ivjchkSubconRpt.addActionListener(this);
			// user code end
		}
		return ivjchkSubconRpt;
	}
	/**
	 * This method initializes ivjchkLienhldrRpt
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkLienhldrRpt()
	{
		if (ivjchkLienhldrRpt == null)
		{
			ivjchkLienhldrRpt = new javax.swing.JCheckBox();
			ivjchkLienhldrRpt.setSize(134, 22);
			ivjchkLienhldrRpt.setText(LIENHOLDR_RPT);
			// user code begin {1}
			ivjchkLienhldrRpt.setMnemonic(java.awt.event.KeyEvent.VK_O);
			ivjchkLienhldrRpt.setLocation(361, 183);
			ivjchkLienhldrRpt.addActionListener(this);
			// user code end

		}
		return ivjchkLienhldrRpt;
	}
	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new javax.swing.JPanel();
			jPanel.setLayout(null);
			jPanel.add(getchkLocalOptions(), null);
			jPanel.add(getchkDlrUpdt(), null);
			jPanel.add(getchkSubconUpdt(), null);
			jPanel.add(getchkLienhldrUpdt(), null);
			jPanel.add(getchkRSPSUpdt(), null);
			// defect 10701
			// Credit Card replaced Security
			// add BatchRptMgmt 
			jPanel.add(getchkCreditCards(), null);
			jPanel.add(getchkBatchRptMgmt(), null);
			// end defect 10701 
			jPanel.setBounds(114, 94, 214, 202);
		}
		return jPanel;
	}
	/**
	 * This method initializes ivjchkBatchRptMgmt
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkBatchRptMgmt()
	{
		if (ivjchkBatchRptMgmt == null)
		{
			ivjchkBatchRptMgmt = new javax.swing.JCheckBox();
			ivjchkBatchRptMgmt.setSize(204, 22);
			ivjchkBatchRptMgmt.setText(BATCHRPTMGMT);
			ivjchkBatchRptMgmt.setLocation(14, 173);
			ivjchkBatchRptMgmt.setMnemonic(
				java.awt.event.KeyEvent.VK_B);
			ivjchkBatchRptMgmt.addActionListener(this);
		}
		return ivjchkBatchRptMgmt;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
