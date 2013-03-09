package com.txdot.isd.rts.client.reports.ui;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.FraudStateData;
import com.txdot.isd.rts.services.data.FraudUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * FrmSuspectedFraudReportRPR009.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/03/2011	Created
 * 							defect 10900 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * Frame RPR009 prompts for Search Criteria for Suspected Fraud Report 
 *
 * @version	6.8.0 			06/03/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		06/03/2011 11:02:17
 */
public class FrmSuspectedFraudReportRPR009
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkAllFraudTypes = null;
	private JCheckBox ivjchkIdentification = null;
	private JCheckBox ivjchkLetterOfAuthorization = null;
	private JCheckBox ivjchkPowerOfAttorney = null;
	private JCheckBox ivjchkReleaseOfLien = null;
	private JPanel ivjFrmSuspectedFraudReporttRPR009ContentPane1 = null;
	private RTSButtonGroup caSearchCriteriaRadioGroup;
	private RTSButtonGroup caFraudTypesRadioGroup;
	private RTSButtonGroup caTransTypesRadioGroup;
	private JPanel ivjJPanelAddlSearch = null;
	private JPanel ivjJPanelFraudTypes = null;
	private JPanel ivjJPanelTransTypes = null;
	private JPanel ivjpnlSelectReport = null;
	private JPanel jPanel = null;
	private JPanel ivjJPanelRadioButtonGroup = null;
	private JRadioButton ivjradioAddTrans = null;
	private JRadioButton ivjradioAllTransTypes = null;
	private JRadioButton ivjradioDeleteTrans = null;
	private JRadioButton ivjradioDocumentNo = null;
	private JRadioButton ivjradioExport = null;
	private JRadioButton ivjradioNone = null;
	private JRadioButton ivjradioPlateNo = null;
	private JRadioButton ivjradioReport = null;
	private JRadioButton ivjradioVIN = null;
	private JLabel ivjstcLblBeginDate = null;
	private JLabel ivjstcLblEndDate = null;
	private RTSDateField ivjtxtBeginDate = null;
	private RTSDateField ivjtxtEndDate = null;
	private RTSInputField ivjtxtInquiryKey = null;

	// boolean
	private boolean cbLockDown = true;
	private boolean cbSaved;

	// int 
	private int ciSelectedCount = 0;

	// Object 
	private RTSDate caBeginDate;
	private RTSDate caEndDate;

	// String
	private static final String ADMIN_LOG_ENTITY = "Fraud Log";
	private static final String TITLE =
		"Suspected Fraud Report    RPR009";

	/**
	 * FrmSuspectedFraudReportRPR009 constructor comment.
	 */
	public FrmSuspectedFraudReportRPR009()
	{
		super();
		initialize();
	}

	/**
	 * FrmSuspectedFraudReportRPR009 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmSuspectedFraudReportRPR009(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmSuspectedFraudReportRPR009 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSuspectedFraudReportRPR009(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * This method is called when a event happens
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

			if (aaAE.getSource() instanceof JCheckBox)
			{
				if (aaAE.getSource() == getchkAllFraudTypes())
				{
					selectChkButtons(
						getchkAllFraudTypes().isSelected());
				}
				else
				{
					JCheckBox laJCheck = (JCheckBox) aaAE.getSource();
					ciSelectedCount =
						ciSelectedCount
							+ (laJCheck.isSelected() ? 1 : -1);
				}
				checkCountZero();
			}

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateData())
				{
					Vector lvData = new Vector();

					AdministrationLogData laAdminLogData =
						new AdministrationLogData(
							ReportConstant.CLIENT);
					laAdminLogData.setEntity(ADMIN_LOG_ENTITY);

					String lsSearch =
						UtilityMethods
							.addPadding(
								caSearchCriteriaRadioGroup
									.getSelection()
									.getActionCommand(),
								8,
								" ")
							.substring(3)
							.trim();

					laAdminLogData.setEntityValue(
						caBeginDate.toString().substring(0, 6)
							+ caBeginDate.toString().substring(8, 10)
							+ CommonConstant.STR_DASH
							+ caEndDate.toString().substring(0, 6)
							+ caEndDate.toString().substring(8, 10)
							+ " "
							+ lsSearch);

					lvData.add(getFraudUIData());

					if (getradioExport().isSelected())
					{
						laAdminLogData.setAction(
							CommonConstant.TXT_EXPORT);
						lvData.add(laAdminLogData);
						getController().processData(
							VCSuspectedFraudReportRPR009.EXPORT_DATA,
							lvData);
					}
					else
					{
						laAdminLogData.setAction(
							CommonConstant.TXT_REPORT);
						lvData.add(laAdminLogData);
						getController().processData(
							VCExemptAuditReportRPR005.DISPLAY_REPORT,
							lvData);
					}
					if (cbSaved) // Export saved
					{
						getController().processData(
							AbstractViewController.CANCEL,
							null);
					}
				}
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
				RTSHelp.displayHelp(RTSHelp.RPR005);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return FraudUIData with selected parameters
	 * 
	 * @return FraudUIData  
	 */
	private FraudUIData getFraudUIData()
	{
		FraudUIData laFraudUIData = new FraudUIData();
		laFraudUIData.setBeginDate(caBeginDate);
		laFraudUIData.setEndDate(caEndDate);

		int liFraudTypes = FraudUIData.ALL;

		if (!getradioAllTransTypes().isSelected())
		{
			liFraudTypes =
				getradioAddTrans().isSelected()
					? FraudUIData.ADD
					: FraudUIData.DELETE;
		}
		laFraudUIData.setTransType(liFraudTypes);

		FraudStateData laFraudTypeData =
			new FraudStateData(
				getchkIdentification().isSelected(),
				getchkReleaseOfLien().isSelected(),
				getchkPowerOfAttorney().isSelected(),
				getchkLetterOfAuthorization().isSelected());

		laFraudUIData.setFraudTypeData(laFraudTypeData);

		if (!getradioNone().isSelected())
		{
			laFraudUIData.setAddlSearch(
				caSearchCriteriaRadioGroup
					.getSelection()
					.getActionCommand());
			laFraudUIData.setSearchKey(gettxtInquiryKey().getText());
		}
		return laFraudUIData;
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
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(52, 435, 262, 36);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel1;
	}
	/**
	 * This method initializes ivjchkAllFraudTypes
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkAllFraudTypes()
	{
		if (ivjchkAllFraudTypes == null)
		{
			ivjchkAllFraudTypes = new JCheckBox();
			ivjchkAllFraudTypes.setSize(89, 20);
			ivjchkAllFraudTypes.setText("All Types");
			ivjchkAllFraudTypes.setMnemonic(KeyEvent.VK_Y);
			ivjchkAllFraudTypes.setLocation(11, 11);
			ivjchkAllFraudTypes.addActionListener(this);
		}
		return ivjchkAllFraudTypes;
	}
	/**
	 * This method initializes ivjchkIdentification
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkIdentification()
	{
		if (ivjchkIdentification == null)
		{
			ivjchkIdentification = new JCheckBox();
			ivjchkIdentification.setSize(133, 20);
			ivjchkIdentification.setText("Identification");
			ivjchkIdentification.setMnemonic(KeyEvent.VK_I);
			ivjchkIdentification.setLocation(11, 36);
			ivjchkIdentification.addActionListener(this);
		}
		return ivjchkIdentification;
	}
	/**
	 * This method initializes ivjchkLetterOfAuthorization
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkLetterOfAuthorization()
	{
		if (ivjchkLetterOfAuthorization == null)
		{
			ivjchkLetterOfAuthorization = new JCheckBox();
			ivjchkLetterOfAuthorization.setSize(151, 20);
			ivjchkLetterOfAuthorization.setText(
				"Letter of Authorization");
			ivjchkLetterOfAuthorization.setMnemonic(KeyEvent.VK_L);
			ivjchkLetterOfAuthorization.setLocation(11, 62);
			ivjchkLetterOfAuthorization.addActionListener(this);
		}
		return ivjchkLetterOfAuthorization;
	}
	/**
	 * This method initializes ivjchkPowerOfAttorney
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPowerOfAttorney()
	{
		if (ivjchkPowerOfAttorney == null)
		{
			ivjchkPowerOfAttorney = new JCheckBox();
			ivjchkPowerOfAttorney.setSize(137, 20);
			ivjchkPowerOfAttorney.setText("Power of Attorney");
			ivjchkPowerOfAttorney.setMnemonic(KeyEvent.VK_O);
			ivjchkPowerOfAttorney.setLocation(190, 52);
			ivjchkPowerOfAttorney.addActionListener(this);
		}
		return ivjchkPowerOfAttorney;
	}
	/**
	 * This method initializes ivjchkReleaseOfLien
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkReleaseOfLien()
	{
		if (ivjchkReleaseOfLien == null)
		{
			ivjchkReleaseOfLien = new JCheckBox();
			ivjchkReleaseOfLien.setSize(129, 20);
			ivjchkReleaseOfLien.setText("Release of Lien");
			ivjchkReleaseOfLien.setMnemonic(KeyEvent.VK_S);
			ivjchkReleaseOfLien.setLocation(190, 78);
			ivjchkReleaseOfLien.addActionListener(this);
		}
		return ivjchkReleaseOfLien;
	}

	/**
	 * Return the FrmSuspectedFraudReporttRPR009ContentPane1 property
	 * value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmSuspectedFraudReporttRPR009ContentPane1()
	{
		if (ivjFrmSuspectedFraudReporttRPR009ContentPane1 == null)
		{
			try
			{
				ivjFrmSuspectedFraudReporttRPR009ContentPane1 =
					new JPanel();
				ivjFrmSuspectedFraudReporttRPR009ContentPane1.setName(
					"FrmSuspectedFraudReporttRPR009ContentPane1");
				ivjFrmSuspectedFraudReporttRPR009ContentPane1
					.setLayout(
					null);
				ivjFrmSuspectedFraudReporttRPR009ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSuspectedFraudReporttRPR009ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmSuspectedFraudReporttRPR009ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				getFrmSuspectedFraudReporttRPR009ContentPane1().add(
					getpnlSelectReport(),
					getpnlSelectReport().getName());
				getFrmSuspectedFraudReporttRPR009ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				ivjFrmSuspectedFraudReporttRPR009ContentPane1.add(
					getJPanelAddlSearch(),
					null);
				ivjFrmSuspectedFraudReporttRPR009ContentPane1.add(
					getJPanelTransTypes(),
					null);
				ivjFrmSuspectedFraudReporttRPR009ContentPane1.add(
					getJPanelFraudTypes(),
					null);
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjFrmSuspectedFraudReporttRPR009ContentPane1;
	}
	/**
	 * This method initializes ivjJPanelAddlSearch
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelAddlSearch()
	{
		if (ivjJPanelAddlSearch == null)
		{
			ivjJPanelAddlSearch = new JPanel();
			ivjJPanelAddlSearch.setLayout(null);
			ivjJPanelAddlSearch.add(getradioPlateNo(), null);
			ivjJPanelAddlSearch.add(getradioVIN(), null);
			ivjJPanelAddlSearch.add(getradioDocumentNo(), null);
			ivjJPanelAddlSearch.add(gettxtInquiryKey(), null);
			ivjJPanelAddlSearch.add(getradioNone(), null);
			ivjJPanelAddlSearch.setBounds(21, 292, 337, 135);
			ivjJPanelAddlSearch.setBorder(
				new TitledBorder(
					new EtchedBorder(),
					"Additional Search Criteria:"));
		}
		return ivjJPanelAddlSearch;
	}
	/**
	 * This method initializes ivjJPanelFraudTypes
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelFraudTypes()
	{
		if (ivjJPanelFraudTypes == null)
		{
			ivjJPanelFraudTypes = new JPanel();
			ivjJPanelFraudTypes.setLayout(null);
			ivjJPanelFraudTypes.add(getchkPowerOfAttorney(), null);
			ivjJPanelFraudTypes.add(getchkReleaseOfLien(), null);
			ivjJPanelFraudTypes.add(getJPanel(), null);
			ivjJPanelFraudTypes.setBounds(21, 170, 337, 111);
			ivjJPanelFraudTypes.setBorder(
				new TitledBorder(
					new EtchedBorder(),
					"Select Fraud Type:"));
		}
		return ivjJPanelFraudTypes;
	}

	/**
	 * This method initializes jPanelRadioButtonGroup
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelRadioButtonGroup()
	{
		if (ivjJPanelRadioButtonGroup == null)
		{
			ivjJPanelRadioButtonGroup = new JPanel();
			ivjJPanelRadioButtonGroup.setLayout(null);
			ivjJPanelRadioButtonGroup.add(getradioFraudReport(), null);
			ivjJPanelRadioButtonGroup.add(getradioExport(), null);
			ivjJPanelRadioButtonGroup.setBounds(20, 22, 78, 57);
		}
		return ivjJPanelRadioButtonGroup;
	}
	/**
	 * This method initializes ivjJPanelTransTypes
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelTransTypes()
	{
		if (ivjJPanelTransTypes == null)
		{
			ivjJPanelTransTypes = new JPanel();
			ivjJPanelTransTypes.setLayout(null);
			ivjJPanelTransTypes.add(getradioAllTransTypes(), null);
			ivjJPanelTransTypes.add(getradioAddTrans(), null);
			ivjJPanelTransTypes.add(getradioDeleteTrans(), null);
			ivjJPanelTransTypes.setBounds(20, 102, 338, 60);
			ivjJPanelTransTypes.setBorder(
				new TitledBorder(
					new EtchedBorder(),
					"Select Action Type:"));
		}
		return ivjJPanelTransTypes;
	}

	private JPanel getpnlSelectReport()
	{
		if (ivjpnlSelectReport == null)
		{
			try
			{
				ivjpnlSelectReport = new JPanel();
				ivjpnlSelectReport.setName(
					CommonConstant.TXT_SELECT_ONE_COLON);
				ivjpnlSelectReport.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						CommonConstant.TXT_SELECT_ONE_COLON));
				ivjpnlSelectReport.setLayout(null);
				ivjpnlSelectReport.setMaximumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setMinimumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setBounds(18, 10, 339, 86);
				ivjpnlSelectReport.add(gettxtBeginDate(), null);
				ivjpnlSelectReport.add(gettxtEndDate(), null);
				ivjpnlSelectReport.add(getstcLblEndDate(), null);
				ivjpnlSelectReport.add(getstcLblBeginDate(), null);
				ivjpnlSelectReport.add(
					getJPanelRadioButtonGroup(),
					null);

				RTSButtonGroup laradioGrp = new RTSButtonGroup();
				laradioGrp.add(getradioExport());
				laradioGrp.add(getradioFraudReport());
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectReport;
	}
	/**
	 * This method initializes ivjradioAddTrans
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioAddTrans()
	{
		if (ivjradioAddTrans == null)
		{
			ivjradioAddTrans = new JRadioButton();
			ivjradioAddTrans.setBounds(127, 26, 80, 20);
			ivjradioAddTrans.setText("Add");
			ivjradioAddTrans.setMnemonic(KeyEvent.VK_A);
		}
		return ivjradioAddTrans;
	}

	/**
	 * This method initializes ivjradioAllTransTypes
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioAllTransTypes()
	{
		if (ivjradioAllTransTypes == null)
		{
			ivjradioAllTransTypes = new JRadioButton();
			ivjradioAllTransTypes.setBounds(24, 26, 86, 20);
			ivjradioAllTransTypes.setText("All Types");
			ivjradioAllTransTypes.setMnemonic(KeyEvent.VK_T);
		}
		return ivjradioAllTransTypes;
	}

	/**
	 * This method initializes ivjradioDeleteTrans
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioDeleteTrans()
	{
		if (ivjradioDeleteTrans == null)
		{
			ivjradioDeleteTrans = new JRadioButton();
			ivjradioDeleteTrans.setSize(96, 20);
			ivjradioDeleteTrans.setText("Delete");
			ivjradioDeleteTrans.setLocation(225, 26);
			ivjradioDeleteTrans.setMnemonic(KeyEvent.VK_D);
		}
		return ivjradioDeleteTrans;
	}

	/**
	 * This method initializes ivjradioDocumentNo
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioDocumentNo()
	{
		if (ivjradioDocumentNo == null)
		{
			ivjradioDocumentNo = new JRadioButton();
			ivjradioDocumentNo.setSize(138, 21);
			ivjradioDocumentNo.setText("Document No");
			ivjradioDocumentNo.setLocation(24, 76);
			ivjradioDocumentNo.setActionCommand("DocNo");
			ivjradioDocumentNo.setMnemonic(KeyEvent.VK_C);
			ivjradioDocumentNo.addItemListener(this);
		}
		return ivjradioDocumentNo;
	}
	/**
	 * Return the ivjradioExport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioExport()
	{
		if (ivjradioExport == null)
		{
			try
			{
				ivjradioExport = new JRadioButton();
				ivjradioExport.setBounds(4, 28, 62, 22);
				ivjradioExport.setName("ivjradioExport");
				ivjradioExport.setMnemonic(
					java.awt.event.KeyEvent.VK_X);
				ivjradioExport.setText(CommonConstant.TXT_EXPORT);
				// Need Constant
				ivjradioExport.setMaximumSize(
					new java.awt.Dimension(154, 22));
				ivjradioExport.setActionCommand(
					CommonConstant.TXT_EXPORT);
				// Need Constant
				ivjradioExport.setMinimumSize(
					new java.awt.Dimension(154, 22));
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioExport;
	}

	/**
	 * Return the ivjradioReport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioFraudReport()
	{
		if (ivjradioReport == null)
		{
			try
			{
				ivjradioReport = new JRadioButton();
				ivjradioReport.setBounds(4, 0, 63, 22);
				ivjradioReport.setName("ivjradioReport");
				ivjradioReport.setMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjradioReport.setText(CommonConstant.TXT_REPORT);
				// Need Constant
				ivjradioReport.setMaximumSize(
					new java.awt.Dimension(145, 22));
				ivjradioReport.setActionCommand(
					CommonConstant.TXT_REPORT);
				// Need Constant
				ivjradioReport.setMinimumSize(
					new java.awt.Dimension(145, 22));
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioReport;
	}
	/**
	 * This method initializes ivjradioNone
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioNone()
	{
		if (ivjradioNone == null)
		{
			ivjradioNone = new JRadioButton();
			ivjradioNone.setSize(107, 21);
			ivjradioNone.setText("None");
			ivjradioNone.setActionCommand("None");
			ivjradioNone.setMnemonic(KeyEvent.VK_N);
			ivjradioNone.setLocation(24, 101);
			ivjradioNone.addItemListener(this);
			ivjradioNone.setSelected(true);
		}
		return ivjradioNone;
	}

	/**
	 * This method initializes ivjradioPlateNo
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPlateNo()
	{
		if (ivjradioPlateNo == null)
		{
			ivjradioPlateNo = new JRadioButton();
			ivjradioPlateNo.setSize(119, 21);
			ivjradioPlateNo.setText("Plate No");
			ivjradioPlateNo.setLocation(24, 26);
			ivjradioPlateNo.setActionCommand("RegPltNo");
			ivjradioPlateNo.addItemListener(this);
			ivjradioPlateNo.setMnemonic(KeyEvent.VK_P);
		}
		return ivjradioPlateNo;
	}
	/**
	 * This method initializes ivjradioVIN
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioVIN()
	{
		if (ivjradioVIN == null)
		{
			ivjradioVIN = new JRadioButton();
			ivjradioVIN.setSize(130, 21);
			ivjradioVIN.setText("VIN");
			ivjradioVIN.setLocation(24, 51);
			ivjradioVIN.addItemListener(this);
			ivjradioVIN.setActionCommand("VIN");
			ivjradioVIN.setMnemonic(KeyEvent.VK_V);
		}
		return ivjradioVIN;
	}

	/**
	 * Return the stcLblStartDate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblBeginDate()
	{
		if (ivjstcLblBeginDate == null)
		{
			try
			{
				ivjstcLblBeginDate = new JLabel();
				ivjstcLblBeginDate.setLabelFor(gettxtBeginDate());
				ivjstcLblBeginDate.setBounds(169, 22, 69, 22);
				ivjstcLblBeginDate.setName("stcLblBeginDate");
				ivjstcLblBeginDate.setText(
					InventoryConstant.TXT_BEGIN_DATE_COLON);
				ivjstcLblBeginDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_B);
				ivjstcLblBeginDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblBeginDate;
	}
	/**
	 * Return the stcLblEndDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEndDate()
	{
		if (ivjstcLblEndDate == null)
		{
			try
			{
				ivjstcLblEndDate = new JLabel();
				ivjstcLblEndDate.setLabelFor(gettxtEndDate());
				ivjstcLblEndDate.setBounds(178, 50, 60, 22);
				ivjstcLblEndDate.setName("stcLblEndDate");
				ivjstcLblEndDate.setText(
					InventoryConstant.TXT_END_DATE_COLON);
				ivjstcLblEndDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_E);
				ivjstcLblEndDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblEndDate;
	}

	/**
	 * Return the txtBeginDate property value.
	 * 
	 * @return RTSDateField
	 */

	private RTSDateField gettxtBeginDate()
	{
		if (ivjtxtBeginDate == null)
		{
			try
			{
				ivjtxtBeginDate = new RTSDateField();
				ivjtxtBeginDate.setBounds(245, 23, 72, 20);
				ivjtxtBeginDate.setName("txtStartDate");
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjtxtBeginDate;
	}

	/**
	 * Return the txtEndDate property value.
	 * 
	 * @return RTSDateField
	 */

	private RTSDateField gettxtEndDate()
	{
		if (ivjtxtEndDate == null)
		{
			try
			{
				ivjtxtEndDate = new RTSDateField();
				ivjtxtEndDate.setBounds(245, 52, 72, 20);
				ivjtxtEndDate.setName("txtEndDate");
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjtxtEndDate;
	}
	/**
	 * This method initializes ivjtxtInquiryKey
	 * 
	 * @return JTextField
	 */
	private RTSInputField gettxtInquiryKey()
	{
		if (ivjtxtInquiryKey == null)
		{
			ivjtxtInquiryKey = new RTSInputField();
			ivjtxtInquiryKey.setSize(165, 20);
			ivjtxtInquiryKey.setLocation(152, 27);
			ivjtxtInquiryKey.setMaxLength(
				CommonConstant.LENGTH_VIN_MAX);
			ivjtxtInquiryKey.setInput(
				RTSInputField.ALPHANUMERIC_NOSPACE);
		}
		return ivjtxtInquiryKey;
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
			setName(TITLE);
			setSize(378, 505);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(TITLE);
			setContentPane(
				getFrmSuspectedFraudReporttRPR009ContentPane1());
			setRequestFocus(false);
		}
		catch (java.lang.Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}

		caSearchCriteriaRadioGroup = new RTSButtonGroup();
		caSearchCriteriaRadioGroup.add(ivjradioPlateNo);
		caSearchCriteriaRadioGroup.add(ivjradioVIN);
		caSearchCriteriaRadioGroup.add(ivjradioDocumentNo);
		caSearchCriteriaRadioGroup.add(ivjradioNone);

		caTransTypesRadioGroup = new RTSButtonGroup();
		caTransTypesRadioGroup.add(ivjradioAllTransTypes);
		caTransTypesRadioGroup.add(ivjradioAddTrans);
		caTransTypesRadioGroup.add(ivjradioDeleteTrans);

		caFraudTypesRadioGroup = new RTSButtonGroup();
		caFraudTypesRadioGroup.add(ivjchkAllFraudTypes);
		caFraudTypesRadioGroup.add(ivjchkIdentification);
		caFraudTypesRadioGroup.add(ivjchkLetterOfAuthorization);
		caFraudTypesRadioGroup.add(ivjchkPowerOfAttorney);
		caFraudTypesRadioGroup.add(ivjchkReleaseOfLien);
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		clearAllColor(this);

		if (aaIE.getSource() instanceof JRadioButton
			&& ((JRadioButton) aaIE.getSource()).isSelected())
		{
			if (aaIE.getSource() == getradioNone())
			{
				gettxtInquiryKey().setText(new String());
				gettxtInquiryKey().setEnabled(false);
				getradioNone().requestFocus();
			}
			else
			{
				gettxtInquiryKey().setEnabled(true);
				gettxtInquiryKey().requestFocus();
			}
		}
	}

	/**
	 * This method saves the results to a file.  If the user cancels
	 * from the save dialog none of the fields are cleared.  If the user
	 * enters a filename without an extension then .sql is automaticly
	 * added as a file extention.
	 * 
	 * If the file already exists then the user is prompted to overwrite
	 * the existing file.  If the user cancels from the over write then
	 * none of the fields are cleared.
	 * @param laData
	 */
	private boolean saveExportToFile(String laData)
	{
		boolean lbReturnValue = false;

		if (SystemProperty.getExportsDirectory() == null)
		{
			cbLockDown = false;
		}

		ExemptAuditSaveDialog chooser =
			new ExemptAuditSaveDialog(cbLockDown);

		if (cbLockDown)
		{
			File exportDir =
				new File(SystemProperty.getExportsDirectory());
			chooser.setCurrentDirectory(exportDir);
			chooser.setDialogTitle(
				CommonConstant.TXT_SAVE_IN_COLON_SPACE
					+ SystemProperty.getExportsDirectory().toUpperCase());
		}

		int liReturnVal = chooser.showSaveDialog(this);

		if (liReturnVal == JFileChooser.APPROVE_OPTION)
		{
			File laFile = chooser.getSelectedFile();
			if (laFile.getName().indexOf(CommonConstant.STR_PERIOD)
				== -1)
			{
				laFile =
					new File(
						laFile.getAbsolutePath()
							+ CommonConstant.TXT_PERIOD_CSV);
			}

			if (laFile.exists())
			{
				int liResponse = 0;
				String lsMsg =
					CommonConstant.TXT_OVERWRITE
						+ laFile.getAbsolutePath().toUpperCase()
						+ CommonConstant.STR_QUESTION_MARK;
				RTSException leRTSExMsg =
					new RTSException(RTSException.CTL001, lsMsg, null);
				liResponse = leRTSExMsg.displayError(this);

				if (liResponse == RTSException.YES)
				{
					writeFile(laFile, laData.toString());
					lbReturnValue = true;
				}
			}
			else
			{
				writeFile(laFile, laData.toString());
				lbReturnValue = true;
			}
		}
		return lbReturnValue;
	}
	/**
	 * Sets the various check boxes depending on whether 
	 * they are enabled and updates ciSelectedCount
	 * 
	 * @param abVal boolean
	 */
	private void selectChkButtons(boolean abVal)
	{
		if (abVal)
		{
			ciSelectedCount = 4;
		}
		else
		{
			if (getchkIdentification().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			if (getchkLetterOfAuthorization().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			if (getchkPowerOfAttorney().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			if (getchkReleaseOfLien().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
		}

		getchkIdentification().setSelected(abVal);
		getchkLetterOfAuthorization().setSelected(abVal);
		getchkPowerOfAttorney().setSelected(abVal);
		getchkReleaseOfLien().setSelected(abVal);
		checkCountZero();
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData != null)
		{
			if (aaData.toString().length() != 0)
			{
				cbSaved = saveExportToFile(aaData.toString());
			}
			else
			{
				RTSException leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
				leRTSEx.displayError(this);
			}
		}
		else
		{
			getradioFraudReport().requestFocus();
			getradioFraudReport().setSelected(true);
			getradioAllTransTypes().setSelected(true);
			selectChkButtons(true);
			getchkAllFraudTypes().setSelected(true);
			gettxtEndDate().setDate(RTSDate.getCurrentDate());
			gettxtBeginDate().setDate(RTSDate.getCurrentDate());
		}
	}

	/**
	 * Check for SelectedCount field for zero value and 
	 * set the LocalOptions checkbox
	 */
	private void checkCountZero()
	{
		getchkAllFraudTypes().setSelected(ciSelectedCount == 4);
	}

	/**
	 * Use a PrintWriter wrapped around a BufferedWriter, which in turn
	 * is wrapped around a FileWriter, to write the string data to the
	 * given file.
	 */
	private boolean writeFile(File file, String dataString)
	{
		try
		{
			PrintWriter out =
				new PrintWriter(
					new BufferedWriter(new FileWriter(file)));
			out.print(dataString);
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
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
			jPanel.add(getchkLetterOfAuthorization(), null);
			jPanel.add(getchkAllFraudTypes(), null);
			jPanel.add(getchkIdentification(), null);
			jPanel.setBounds(14, 16, 174, 83);
		}
		return jPanel;
	}
	/**
	 * Validate Data
	 *
	 * @return boolean
	 */
	private boolean validateData()
	{
		int liMaxDays = SystemProperty.getMaxFraudReportDays();
		boolean lbValid = true;
		RTSException laRTSEx = new RTSException();

		if (!gettxtBeginDate().isValidDate())
		{ // 733
			laRTSEx.addException(
				new RTSException(ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
				gettxtBeginDate());
			lbValid = false; 
		}

		if (!gettxtEndDate().isValidDate())
		{
			// 733
			laRTSEx.addException(
				new RTSException(ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
				gettxtEndDate());
			lbValid = false; 
		}
		
		if (lbValid)
		{
			caBeginDate = gettxtBeginDate().getDate();

			caEndDate = gettxtEndDate().getDate();

			if (caBeginDate.compareTo(RTSDate.getCurrentDate()) == 1)
			{
				// 427 
				laRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_MSG_DATE_CANNOT_BE_IN_FUTURE),
					gettxtBeginDate());
			}
			if (caEndDate.compareTo(RTSDate.getCurrentDate()) == 1)
			{
				// 427 
				laRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_MSG_DATE_CANNOT_BE_IN_FUTURE),
					gettxtEndDate());

			}
			if (caEndDate.compareTo(caBeginDate) == -1)
			{
				// 733
				laRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
					gettxtEndDate());
			}
			
			if ((caBeginDate.add(RTSDate.DATE, liMaxDays))
				.compareTo(caEndDate)
				== -1)
			{
				String lsAppend = " " + liMaxDays + " DAYS.";

				// 774 
				laRTSEx.addException(
					new RTSException(ErrorsConstant.ERR_NUM_DATE_RANGE_CANNOT_EXCEED, new String[] { lsAppend }),
					gettxtEndDate());
			}
		}

		// No Selection for Checkboxes for Fraud Types 
		if (ciSelectedCount == 0)
		{
			// 150 
			laRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getchkAllFraudTypes());
		}
		if (!getradioNone().isSelected()
			&& gettxtInquiryKey().isEmpty())
		{
			laRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtInquiryKey());
		}
		else if (
			getradioPlateNo().isSelected()
				&& gettxtInquiryKey().getText().length()
					> CommonConstant.LENGTH_PLTNO)
		{
			laRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtInquiryKey());
		}
		else if (
			getradioDocumentNo().isSelected()
				&& gettxtInquiryKey().getText().length()
					!= CommonConstant.LENGTH_DOCNO)
		{
			laRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtInquiryKey());
		}

		if (laRTSEx.isValidationError())
		{
			laRTSEx.displayError(this);
			laRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;

	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
