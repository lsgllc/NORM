package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.SecurityClientDataObject;
import com.txdot.isd.rts.services.data.SecurityData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSecurityAccessRightsReportsSEC012.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/24/2004	5.2.0 Merge/Cleanup. Added checkbox for
 * 							Reprint Sticker Report. See PCR 34 comments.
 *							modify actionPerformed(),
 *							disableBtnOnOfcId(),initialize(),
 *							selectChkButtons(), setData()
 * 							Ver 5.2.0
 * K Harrell	07/29/2004	Allow Region to grant access to Reprint
 *							Sticker Report
 *							modify disableBtnOnOfcId()
 *							defect 7385  Ver 5.2.1
 * Ray Rowehl	03/17/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							defect 7891 Ver 5.2.3 
 * Min Wang		04/08/2005	Remove arrays since they are causing
 * 							initialization errors
 * 							Do the intial setting of the select all 
 * 							boolean at start up.
 * 							delete carrBtnArr, carrBtnArrSec
 * 							delete checkIfChkSlctdDsbld()
 * 							modify initialize(), keyPressed()
 * 							defect 7891 Ver 5.2.3
 * Min Wang		04/08/2005	The cbSelectAll was not set when coming 
 * 							into the frame.  It needs to be set so 
 * 							that the first click on the reports chkbox
 * 							has an effect if the user has all rights.
 * 							add MAX_COUNTY_CHKBOXES, MAX_HQ_CHKBOXES
 * 							modify disableBtnOnOfcId()
 * 							defect 8154 Ver 5.2.3
 * Min Wang 	04/16/2005	remove unused method
 * 							delete setController()
 * 							defect 7891 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                 
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Min Wang		09/01/2005	Work on constants.
 * 							defect 7891 Ver 5.2.3
 * Min Wang		12/12/2005  Uncomment code for arrow key function.
 * 							modify keyPressed(), initialize()
 * 							defect 7891 Ver 5.2.3
 * M Reyes		10/16/2006	Changes for Exempts
 * 							Used visual editor to add/adjust checkboxes
 * 							modify getFrmSecurityAccessRights
 * 							ReportsSEC012ContentPane1(), 
 * 							actionPerformed(), initialize(),
 * 							selectChkButtons(), setData(),
 * 							setValuesToDataObj(), keyPressed()
 * 							Add getchkExemptAuditReport()
 * 							defect 8900 Ver Exempts
 * K Harrell	04/20/2007	Implemented SystemProperty.isCounty(),
 * 						 	SystemProperty.isRegion()
 * 							modify disableBtnOnOfcId()
 * 							defect 9085 Ver Special Plates     
 * B Hargrove	03/12/2009	Changes for E-Liens \ Titles.
 * 							Used visual editor to add/adjust checkboxes.
 * 							Add variable 'ciBtnArrSecLength' for number
 * 							of checkboxes to ease maintenance.
 * 							modify getFrmSecurityAccessRights
 * 							ReportsSEC012ContentPane1(), 
 * 							actionPerformed(), initialize(),
 * 							selectChkButtons(), setData(),
 * 							setValuesToDataObj(), keyPressed()
 * 							Add getchkETtlRpt() 
 * 							defect 9960 Ver Defect_POS_E 
 * K Harrell	06/16/2011	Enlarged via Visual Editor  
 * 							Removed GridBagLayout 
 * 							add MAX_HQ_CHKBOXES, SUSPECTED_FRAUD_RPT
 * 							add caCheckBoxButtonGroup, ciSelectedCount
 * 							delete keyPressed(), ciEnableCount 
 * 							modify MAX_REGION_CHKBOXES,
 * 							 ivjchkSuspectedFraudReport, get method
 * 							modify actionPerformed(), 
 * 							 disableBtnOnOfcId(), initialize(), 
 * 							 getFrmSecurityAccessRightsReportsSEC012ContentPane1(),
 * 							 selectChkButtons()  
 * 							defect 10900 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * This class is used for managing security access rights for
 * Reports. Data on this screen is managed through 
 * SecurityClientDataObject.
 *
 * @version	6.8.0			06/16/2011	
 * @author	Administrator
 * <br>Creation Date:		06/28/2001 17:11:01 
 */

public class FrmSecurityAccessRightsReportsSEC012
	extends RTSDialogBox
	implements ActionListener
{
	private JCheckBox ivjchkReports = null;
	private JCheckBox ivjchkReprintReport = null;
	private JCheckBox ivjchkSalesTaxAlloc = null;
	private JCheckBox ivjchkTitlePackageReport = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblEmployeeName = null;
	private JLabel ivjstcLblReports = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmSecurityAccessRightsReportsSEC012ContentPane1 =
		null;
	private JLabel ivjlblEmployeeFirstName = null;
	private JLabel ivjlblEmployeeLastName = null;
	private JLabel ivjlblEmployeeMName = null;
	private JCheckBox ivjchkReprintStickerReport = null;
	private JCheckBox ivjchkExemptAuditReport = null;
	private JCheckBox ivjchkETtlRpt = null;

	// boolean 
	private boolean cbSelectAll = false;

	// int 
	private int ciSelectedCount = 0;

	// Object 
	private SecurityClientDataObject caSecData = null;

	//	defect 10900
	private static final int MAX_HQ_CHKBOXES = 3;
	private static final int MAX_REGION_CHKBOXES = 4;

	private JCheckBox ivjchkSuspectedFraudReport = null;
	private RTSButtonGroup caCheckBoxButtonGroup;
	private static final String SUSPECTED_FRAUD_RPT =
		"Suspected Fraud Report";
	// end defect 10900 

	// Constant 
	private static final int MAX_COUNTY_CHKBOXES = 3;

	private static final String REPORT = "Reports";
	private static final String REPRINT_RPT = "Reprint Report";
	private static final String REPRINT_STK_RPT =
		"Reprint Sticker Report";
	private static final String SALE_TAX_ALLOCATION =
		"Sales Tax Allocation";
	private static final String TITLE_PACKAGE_RPT =
		"Title Package Report";
	private static final String EXEMPT_AUDIT_RPT =
		"Exempt Audit Report";
	private static final String ETITLE_RPT = "Electronic Title Report";
	private static final String LABEL1 = "JLabel1";
	private static final String LABEL2 = "JLabel2";
	private static final String EMP_ID = "Employee Id:";
	private static final String EMP_NAME = "Employee Name:";
	private static final String SEC012_FRAME_TITLE =
		"Security Access Rights Reports   SEC012";

	/**
	 * FrmSecurityAccessRightsReportsSEC012 constructor comment.
	 */
	public FrmSecurityAccessRightsReportsSEC012()
	{
		super();
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsReportsSEC012 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSecurityAccessRightsReportsSEC012(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsReportsSEC012 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsReportsSEC012(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsReportsSEC012 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsReportsSEC012(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsReportsSEC012 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsReportsSEC012(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmSecurityAccessRightsReportsSEC012 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmSecurityAccessRightsReportsSEC012(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmSecurityAccessRightsReportsSEC012 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmSecurityAccessRightsReportsSEC012(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmSecurityAccessRightsReportsSEC012 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsReportsSEC012(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmSecurityAccessRightsReportsSEC012 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param abModal boolean
	 */
	public FrmSecurityAccessRightsReportsSEC012(
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
				caSecData.setSEC012(false);
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
				RTSHelp.displayHelp(RTSHelp.SEC012);
				return;
			}
			else if (aaAE.getSource() == getchkReports())
			{
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
			//defect 10900 
			else if (aaAE.getSource() instanceof JCheckBox)
			{
				JCheckBox laChk = (JCheckBox) aaAE.getSource();
				ciSelectedCount =
					ciSelectedCount + (laChk.isSelected() ? 1 : -1);
			}
			checkCountZero();
			// end defect 10900
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
		if (ciSelectedCount == 0)
		{
			getchkReports().setSelected(false);
		}
		else
		{
			getchkReports().setSelected(true);
		}
	}

	/**
	 * Disable the various checkbox based on various conditions
	 */
	private void disableBtnOnOfcId()
	{
		// defect 10900
		getchkReports().setSelected(true);
		// end defect 10900 
		
		if (SystemProperty.isCounty())
		{
			getchkTitlePackageReport().setEnabled(false);

			// defect 10900 
			getchkSuspectedFraudReport().setEnabled(false);
			// end defect 10900 

			if (!getchkTitlePackageReport().isSelected())
			{
				getchkTitlePackageReport().setSelected(true);
				ciSelectedCount = ciSelectedCount + 1;
			}
			getchkReprintStickerReport().setEnabled(false);

			if (ciSelectedCount >= MAX_COUNTY_CHKBOXES)
			{
				cbSelectAll = true;
			}
		}
		else if (SystemProperty.isRegion())
		{
			getchkTitlePackageReport().setEnabled(false);
			if (!getchkTitlePackageReport().isSelected())
			{
				getchkTitlePackageReport().setSelected(true);
				ciSelectedCount = ciSelectedCount + 1;
			}
			getchkReprintStickerReport().setEnabled(true);

			// defect 10900 
			getchkSuspectedFraudReport().setEnabled(false);
			if (!getchkSuspectedFraudReport().isSelected())
			{
				getchkSuspectedFraudReport().setSelected(true);
				ciSelectedCount = ciSelectedCount + 1;
			}
			// end defect 10900 

			if (ciSelectedCount >= MAX_REGION_CHKBOXES)
			{
				cbSelectAll = true;
			}
		}
		else
		{
			getchkSalesTaxAlloc().setEnabled(false);
			getchkTitlePackageReport().setEnabled(false);
			getchkReprintStickerReport().setEnabled(true);
			
			// defect 10900
			getchkSuspectedFraudReport().setEnabled(false);
			if (!getchkSuspectedFraudReport().isSelected())
			{
				getchkSuspectedFraudReport().setSelected(true);
				ciSelectedCount = ciSelectedCount + 1;
			}
			// end defect 10900 
			if (ciSelectedCount >= MAX_HQ_CHKBOXES)
			{
				cbSelectAll = true;
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
				ivjButtonPanel1.setLayout(new java.awt.FlowLayout());
				ivjButtonPanel1.setBounds(125, 380, 377, 41);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
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
	 * Return the chkReports property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkReports()
	{
		if (ivjchkReports == null)
		{
			try
			{
				ivjchkReports = new javax.swing.JCheckBox();
				ivjchkReports.setSize(218, 24);
				ivjchkReports.setName("chkReports");
				ivjchkReports.setMnemonic('p');
				ivjchkReports.setText(REPORT);
				ivjchkReports.setMaximumSize(
					new java.awt.Dimension(70, 22));
				ivjchkReports.setActionCommand(REPORT);
				ivjchkReports.setMinimumSize(
					new java.awt.Dimension(70, 22));
				// user code begin {1}
				ivjchkReports.setLocation(210, 85);
				ivjchkReports.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkReports;
	}

	/**
	 * Return the chkReprintReport property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkReprintReport()
	{
		if (ivjchkReprintReport == null)
		{
			try
			{
				ivjchkReprintReport = new javax.swing.JCheckBox();
				ivjchkReprintReport.setSize(218, 24);
				ivjchkReprintReport.setName("chkReprintReport");
				ivjchkReprintReport.setMnemonic('r');
				ivjchkReprintReport.setText(REPRINT_RPT);
				ivjchkReprintReport.setMaximumSize(
					new java.awt.Dimension(107, 22));
				ivjchkReprintReport.setActionCommand(REPRINT_RPT);
				ivjchkReprintReport.setMinimumSize(
					new java.awt.Dimension(107, 22));
				// user code begin {1}
				ivjchkReprintReport.setLocation(210, 155);
				ivjchkReprintReport.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkReprintReport;
	}

	/**
	 * Return the chkQuickCounterReport property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkReprintStickerReport()
	{
		if (ivjchkReprintStickerReport == null)
		{
			try
			{
				ivjchkReprintStickerReport =
					new javax.swing.JCheckBox();
				ivjchkReprintStickerReport.setSize(218, 24);
				ivjchkReprintStickerReport.setName(
					"chkReprintStickerReport");
				ivjchkReprintStickerReport.setMnemonic('k');
				ivjchkReprintStickerReport.setText(REPRINT_STK_RPT);
				ivjchkReprintStickerReport.setMaximumSize(
					new java.awt.Dimension(147, 22));
				ivjchkReprintStickerReport.setActionCommand(
					REPRINT_STK_RPT);
				ivjchkReprintStickerReport.setMinimumSize(
					new java.awt.Dimension(147, 22));
				// user code begin {1}
				ivjchkReprintStickerReport.setLocation(210, 190);
				ivjchkReprintStickerReport.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkReprintStickerReport;
	}

	/**
	 * Return the chkSalesTaxAlloc property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSalesTaxAlloc()
	{
		if (ivjchkSalesTaxAlloc == null)
		{
			try
			{
				ivjchkSalesTaxAlloc = new javax.swing.JCheckBox();
				ivjchkSalesTaxAlloc.setSize(218, 24);
				ivjchkSalesTaxAlloc.setName("chkSalesTaxAlloc");
				ivjchkSalesTaxAlloc.setMnemonic('s');
				ivjchkSalesTaxAlloc.setText(SALE_TAX_ALLOCATION);
				ivjchkSalesTaxAlloc.setMaximumSize(
					new java.awt.Dimension(140, 22));
				ivjchkSalesTaxAlloc.setActionCommand(
					SALE_TAX_ALLOCATION);
				ivjchkSalesTaxAlloc.setMinimumSize(
					new java.awt.Dimension(140, 22));
				// user code begin {1}
				ivjchkSalesTaxAlloc.setLocation(210, 120);
				ivjchkSalesTaxAlloc.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSalesTaxAlloc;
	}

	/**
	 * Return the chkTitlePackageReport property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkTitlePackageReport()
	{
		if (ivjchkTitlePackageReport == null)
		{
			try
			{
				ivjchkTitlePackageReport = new javax.swing.JCheckBox();
				ivjchkTitlePackageReport.setSize(218, 24);
				ivjchkTitlePackageReport.setName(
					"chkTitlePackageReport");
				ivjchkTitlePackageReport.setMnemonic('t');
				ivjchkTitlePackageReport.setText(TITLE_PACKAGE_RPT);
				ivjchkTitlePackageReport.setMaximumSize(
					new java.awt.Dimension(143, 22));
				ivjchkTitlePackageReport.setActionCommand(
					TITLE_PACKAGE_RPT);
				ivjchkTitlePackageReport.setMinimumSize(
					new java.awt.Dimension(143, 22));
				// user code begin {1}
				ivjchkTitlePackageReport.setLocation(210, 260);
				ivjchkTitlePackageReport.setPreferredSize(
					new java.awt.Dimension(143, 24));
				ivjchkTitlePackageReport.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkTitlePackageReport;
	}
	// defect 8900
	/**
	 * Return the chkExemptAuditReport property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkExemptAuditReport()
	{
		if (ivjchkExemptAuditReport == null)
		{
			try
			{

				ivjchkExemptAuditReport = new javax.swing.JCheckBox();
				ivjchkExemptAuditReport.setSize(231, 24);
				ivjchkExemptAuditReport.setName("chkExemptAuditReport");
				ivjchkExemptAuditReport.setMnemonic('x');
				ivjchkExemptAuditReport.setText(EXEMPT_AUDIT_RPT);
				ivjchkExemptAuditReport.setLocation(210, 225);
				ivjchkExemptAuditReport.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkExemptAuditReport;
	}
	// end defect 8900

	/**
	 * Return the chkETtlRpt property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkETtlRpt()
	{
		if (ivjchkETtlRpt == null)
		{
			try
			{

				ivjchkETtlRpt = new javax.swing.JCheckBox();
				ivjchkETtlRpt.setSize(160, 24);
				ivjchkETtlRpt.setName("chkETitleRpt");
				ivjchkETtlRpt.setMnemonic(java.awt.event.KeyEvent.VK_C);
				ivjchkETtlRpt.setText(ETITLE_RPT);
				ivjchkETtlRpt.setLocation(210, 295);
				ivjchkETtlRpt.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjchkETtlRpt;
	}

	/**
	 * Return the FrmSecurityAccessRightsReportsSEC012ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSecurityAccessRightsReportsSEC012ContentPane1()
	{
		if (ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
			== null)
		{
			try
			{
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.setName(
					"FrmSecurityAccessRightsReportsSEC012ContentPane1");
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.setLayout(
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getstcLblEmployeeId(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getstcLblEmployeeName(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getlblEmployeeLastName(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getlblEmployeeId(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getstcLblReports(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getchkReports(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getlblEmployeeFirstName(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getchkSalesTaxAlloc(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getchkReprintReport(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getchkTitlePackageReport(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getchkReprintStickerReport(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getButtonPanel1(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getlblEmployeeMName(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getchkExemptAuditReport(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getchkETtlRpt(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.add(
					getchkSuspectedFraudReport(),
					null);
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(471, 314));
				ivjFrmSecurityAccessRightsReportsSEC012ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(471, 314));

				// user code begin {1}
				// end defect 8900
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmSecurityAccessRightsReportsSEC012ContentPane1;
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
				ivjlblEmployeeFirstName = new javax.swing.JLabel();
				ivjlblEmployeeFirstName.setBounds(334, 28, 170, 16);
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
				ivjlblEmployeeId = new javax.swing.JLabel();
				ivjlblEmployeeId.setBounds(118, 9, 68, 14);
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
				ivjlblEmployeeLastName = new javax.swing.JLabel();
				ivjlblEmployeeLastName.setBounds(113, 29, 187, 14);
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
				ivjlblEmployeeMName = new javax.swing.JLabel();
				ivjlblEmployeeMName.setBounds(538, 28, 54, 16);
				ivjlblEmployeeMName.setName("lblEmployeeMName");
				ivjlblEmployeeMName.setText(LABEL2);
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
				ivjstcLblEmployeeId = new javax.swing.JLabel();
				ivjstcLblEmployeeId.setBounds(18, 9, 83, 18);
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
	private JLabel getstcLblEmployeeName()
	{
		if (ivjstcLblEmployeeName == null)
		{
			try
			{
				ivjstcLblEmployeeName = new javax.swing.JLabel();
				ivjstcLblEmployeeName.setBounds(0, 29, 113, 14);
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
	 * Return the stcLblReports property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblReports()
	{
		if (ivjstcLblReports == null)
		{
			try
			{
				ivjstcLblReports = new javax.swing.JLabel();
				ivjstcLblReports.setBounds(220, 55, 168, 14);
				ivjstcLblReports.setName("stcLblReports");
				ivjstcLblReports.setText(REPORT);
				ivjstcLblReports.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblReports.setMinimumSize(
					new java.awt.Dimension(45, 14));
				ivjstcLblReports.setHorizontalAlignment(
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
		return ivjstcLblReports;
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
			setName("FrmSecurityAccessRightsReportsSEC012");
			setSize(600, 463);
			setTitle(SEC012_FRAME_TITLE);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setContentPane(
				getFrmSecurityAccessRightsReportsSEC012ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		addWindowListener(this);

		// defect 10900 
		caCheckBoxButtonGroup = new RTSButtonGroup();
		caCheckBoxButtonGroup.add(ivjchkReports);
		caCheckBoxButtonGroup.add(ivjchkSalesTaxAlloc);
		caCheckBoxButtonGroup.add(ivjchkReprintReport);
		caCheckBoxButtonGroup.add(ivjchkReprintStickerReport);
		caCheckBoxButtonGroup.add(ivjchkExemptAuditReport);
		caCheckBoxButtonGroup.add(ivjchkTitlePackageReport);
		caCheckBoxButtonGroup.add(ivjchkETtlRpt);
		caCheckBoxButtonGroup.add(ivjchkSuspectedFraudReport);
		// end defect 10900
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
			FrmSecurityAccessRightsReportsSEC012 aFrmSecurityAccessRightsReportsSEC012;
			aFrmSecurityAccessRightsReportsSEC012 =
				new FrmSecurityAccessRightsReportsSEC012();
			aFrmSecurityAccessRightsReportsSEC012.setModal(true);
			aFrmSecurityAccessRightsReportsSEC012
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			aFrmSecurityAccessRightsReportsSEC012.show();
			java.awt.Insets insets =
				aFrmSecurityAccessRightsReportsSEC012.getInsets();
			aFrmSecurityAccessRightsReportsSEC012.setSize(
				aFrmSecurityAccessRightsReportsSEC012.getWidth()
					+ insets.left
					+ insets.right,
				aFrmSecurityAccessRightsReportsSEC012.getHeight()
					+ insets.top
					+ insets.bottom);
			aFrmSecurityAccessRightsReportsSEC012.setVisibleRTS(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * Sets the various check boxes depending on whether 
	 * they are enabled and updates ciEnableCount
	 * 
	 * @param abVal boolean
	 */
	private void selectChkButtons(boolean abVal)
	{
		if (getchkReprintStickerReport().isEnabled())
		{
			if (abVal && !getchkReprintStickerReport().isSelected())
			{
				ciSelectedCount = ciSelectedCount + 1;
			}
			else if (
				!abVal && getchkReprintStickerReport().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			getchkReprintStickerReport().setSelected(abVal);
		}
		if (getchkReprintReport().isEnabled())
		{
			if (abVal && !getchkReprintReport().isSelected())
			{
				ciSelectedCount = ciSelectedCount + 1;
			}
			else if (!abVal && getchkReprintReport().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			getchkReprintReport().setSelected(abVal);
		}
		if (getchkSalesTaxAlloc().isEnabled())
		{
			if (abVal && !getchkSalesTaxAlloc().isSelected())
			{
				ciSelectedCount = ciSelectedCount + 1;
			}
			else if (!abVal && getchkSalesTaxAlloc().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			getchkSalesTaxAlloc().setSelected(abVal);
		}
		if (getchkTitlePackageReport().isEnabled())
		{
			if (abVal && !getchkTitlePackageReport().isSelected())
			{
				ciSelectedCount = ciSelectedCount + 1;
			}
			else if (!abVal && getchkTitlePackageReport().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			getchkTitlePackageReport().setSelected(abVal);
		}

		if (getchkExemptAuditReport().isEnabled())
		{
			if (abVal && !getchkExemptAuditReport().isSelected())
			{
				ciSelectedCount = ciSelectedCount + 1;
			}
			else if (!abVal && getchkExemptAuditReport().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			getchkExemptAuditReport().setSelected(abVal);
		}
		if (getchkETtlRpt().isEnabled())
		{
			if (abVal && !getchkETtlRpt().isSelected())
			{
				ciSelectedCount = ciSelectedCount + 1;
			}
			else if (!abVal && getchkETtlRpt().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			getchkETtlRpt().setSelected(abVal);
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
			if (laSecData.getRptsAccs() == 1)
			{
				getchkReports().setSelected(true);
			}
			if (laSecData.getCntyRptsAccs() == 1)
			{
				getchkSalesTaxAlloc().setSelected(true);
				ciSelectedCount = ciSelectedCount + 1;
			}
			if (laSecData.getReprntRptAccs() == 1)
			{
				getchkReprintReport().setSelected(true);
				ciSelectedCount = ciSelectedCount + 1;
			}
			// PCR 34
			if (laSecData.getReprntStkrRptAccs() == 1)
			{
				getchkReprintStickerReport().setSelected(true);
				ciSelectedCount = ciSelectedCount + 1;
			}
			// END PCR 34
			// defect 8900
			if (laSecData.getExmptAuditRptAccs() == 1)
			{
				getchkExemptAuditReport().setSelected(true);
				ciSelectedCount = ciSelectedCount + 1;
			}
			// end defect 8900	
			// defect 9960
			if (laSecData.getETtlRptAccs() == 1)
			{
				getchkETtlRpt().setSelected(true);
				ciSelectedCount = ciSelectedCount + 1;
			}
			// end defect 9960	
		}
		//Enable disable check boxes
		disableBtnOnOfcId();
		//eckCountZero();
	}

	/**
	 * Populate the SecurityClientDataObject object with security access
	 * rights for Reports.
	 */
	private void setValuesToDataObj()
	{
		caSecData.setSEC012(true);
		//Take the new Values from the window and set to data object
		caSecData.getSecData().setReprntStkrRptAccs(
			getIntValue(getchkReprintStickerReport().isSelected()));
		caSecData.getSecData().setRptsAccs(
			getIntValue(getchkReports().isSelected()));
		caSecData.getSecData().setReprntRptAccs(
			getIntValue(getchkReprintReport().isSelected()));
		caSecData.getSecData().setCntyRptsAccs(
			getIntValue(getchkSalesTaxAlloc().isSelected()));
		caSecData.getSecData().setExmptAuditRptAccs(
			getIntValue(getchkExemptAuditReport().isSelected()));
		caSecData.getSecData().setETtlRptAccs(
			getIntValue(getchkETtlRpt().isSelected()));
	}
	/**
	 * This method initializes ivjchkSuspectedFraudReport
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSuspectedFraudReport()
	{
		if (ivjchkSuspectedFraudReport == null)
		{
			ivjchkSuspectedFraudReport = new JCheckBox();
			ivjchkSuspectedFraudReport.setSize(237, 24);
			ivjchkSuspectedFraudReport.setLocation(210, 330);
			ivjchkSuspectedFraudReport.setText(SUSPECTED_FRAUD_RPT);
		}
		return ivjchkSuspectedFraudReport;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
