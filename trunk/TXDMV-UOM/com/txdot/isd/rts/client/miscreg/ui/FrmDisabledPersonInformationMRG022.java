package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.title.ui.FrmCountyConfirmCTL002;

import com.txdot.isd.rts.services.cache.DisabledPlacardCustomerIdTypeCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmDisabledPersonInformationMRG022.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created.
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	10/30/2008	On new record, do not enabled fields until
 * 							Type Id is selected.
 * 							add setEnabledForDisplay()
 * 							modify actionPerformed(), 
 * 							  handleDataToDisplay()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/01/2008	Adjust field physical lengths for new 
 * 							name length requirements. 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/15/2008	Adjust screen size via Visual Comp 
 * 							defect 9870 Ver Defect_POS_B
 * K Harrell	07/19/2009	refactor name/methods to better map to 
 * 							 Title/Registration. Use CommonConstant 
 * 							 lengths. 
 * 							add caNameAddrComp 
 *  						delete CNTRY_ZIP_MAXLENGTH, ERRNO_186,
 * 							 MAX_CNTY_NO, USA 
 * 							delete keyReleased(), processKeyEvent() 
 *							modify handleDataToDisplay(), initialzie(),
 *							 itemStateChanged(), 
 *							 setDataToDataObject(), validateAddress(), 
 *							 validateFields(),validateName(), 
 *							 validateResCnty()
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	07/26/2009	Implement HB 3095 - Delete "Type", Retitle
 * 							"Term" as "Type" 
 * 							delete ivjpanelTerm, getpanelTerm(), 
 * 							 getradioMobility(), getradioMobilityNone(),
 * 							 getradioNonMobility()  
 * 							modify getpanelType(), handleDataToDisplay(),
 * 							 isDataMotivied(), setDataToDataObject(), 
 * 							 verifyFields() 
 * 							defect 10133 Ver Defect_POS_F
 * K Harrell	12/17/2009  Implement character validation for MF
 * 							add cvAddlMFValid
 *  						modify initialize(), validateFields() 
 * 							defect 10299 Ver Defect_POS_H   
 * K Harrell	07/11/2010	Implement new ErrorsConstant for Applicant  
 * 							modify initialize(), validateName()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	12/09/2010	Disabled Temporary radio button if Institution 
 * 							modify setEnabledForDisplay(), 
 * 							 validateFields()  
 * 							defect 10679 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Disabled PersonOld Information Frame
 *
 * @version	6.7.0			12/09/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		10/21/2008 
 */
public class FrmDisabledPersonInformationMRG022
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private RTSButton ivjbtnRcdNotApplicable = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkDisabledPlate = null;
	private JCheckBox ivjchkDsabldVetPlate = null;
	private JCheckBox ivjchkUSA = null;
	private JComboBox ivjcomboIdType = null;
	private JPanel ivjFrmDisabledPersonInformationMRG022ContentPane1 =
		null;
	private JLabel ivjlblPrevCounty = null;
	private JLabel ivjlblScreenMessage = null;
	private JPanel ivjpanelAddress = null;
	private JPanel ivjpanelCheckbox = null;
	private JPanel ivjpanelCityStateCntry = null;
	private JPanel ivjpanelContactInfo = null;
	private JPanel ivjpanelCustId = null;
	private JPanel ivjpanelCustomerType = null;
	private JPanel ivjpanelDisabledName = null;
	private JPanel ivjpanelInstitutionName = null;
	private JPanel ivjpanelType = null;
	private JRadioButton ivjradioCustIdTypeNone = null;
	private JRadioButton ivjradioIndividual = null;
	private JRadioButton ivjradioInstitution = null;
	private JRadioButton ivjradioPermanent = null;
	private JRadioButton ivjradioTemporary = null;
	private JRadioButton ivjradioTermNone = null;
	private JLabel ivjstcLblAddress = null;
	private JLabel ivjstcLblCounty = null;
	private JLabel ivjstcLblCustFirstName = null;
	private JLabel ivjstcLblCustId = null;
	private JLabel ivjstcLblCustLastName = null;
	private JLabel ivjstcLblCustMI = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblEMail = null;
	private JLabel ivjstcLblIdType = null;
	private JLabel ivjstcLblPhone = null;
	private RTSInputField ivjtxtCustStreet1 = null;
	private RTSInputField ivjtxtCustStreet2 = null;
	private RTSInputField ivjtxtCustCity = null;
	private RTSInputField ivjtxtCustState = null;
	private RTSInputField ivjtxtCustZpcd = null;
	private RTSInputField ivjtxtCustZpcdP4 = null;
	private RTSInputField ivjtxtCustCntry = null;
	private RTSInputField ivjtxtCustCntryZpcd = null;
	private RTSInputField ivjtxtCustFirstName = null;
	private RTSInputField ivjtxtCustId = null;
	private RTSInputField ivjtxtCustLastName = null;
	private RTSInputField ivjtxtCustMI = null;
	private RTSInputField ivjtxtEMail = null;
	private RTSInputField ivjtxtInstitutionName = null;
	private RTSPhoneField ivjtxtPhoneNo = null;
	private RTSInputField ivjtxtResCnty = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;

	// boolean 
	private boolean cbInitialSet = true;
	private boolean cbSetDataFinished = false;
	private boolean cbReleaseRequired = false;
	private boolean cbInProcsAlreadyDsplyed = false;

	// Object 
	// defect 10127 
	private NameAddressComponent caNameAddrComp = null;
	// end defect 10127 

	private DisabledPlacardCustomerData caOrigDPCustData = null;
	private DisabledPlacardCustomerData caDPCustData = null;
	private AddressData caOrigDsabldAddressData = null;
	private AddressData caDsabldAddressData = null;

	// Vector 
	private Vector cvIdType = new Vector();

	// defect 10299
	private Vector cvAddlMFValid = new Vector();
	// end defect 10299 

	// Constant
	private final static String DASH = "-";
	private final static String ERRMSG_DYWT_CANCEL =
		"Are you sure you want to Cancel?\nIf yes, changes will "
			+ "not be saved.";
	private final static String ID = "Applicant Id:";
	private final static String MSG_MODIFIED_APPPLICANT =
		"Applicant information has been modified. ";
	private final static String MSG_NEW_APPLICANT =
		"A new Applicant for Disabled Placards will be added. ";
	private final static String MSG_UPDATE_APPPLICANT =
		"Applicant information will be updated. ";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */

	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmDisabledPersonInformationMRG022 aaFrmDsabldInfoMRG022;
			aaFrmDsabldInfoMRG022 =
				new FrmDisabledPersonInformationMRG022();
			aaFrmDsabldInfoMRG022.setModal(true);
			aaFrmDsabldInfoMRG022
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			aaFrmDsabldInfoMRG022.show();
			java.awt.Insets insets = aaFrmDsabldInfoMRG022.getInsets();
			aaFrmDsabldInfoMRG022.setSize(
				aaFrmDsabldInfoMRG022.getWidth()
					+ insets.left
					+ insets.right,
				aaFrmDsabldInfoMRG022.getHeight()
					+ insets.top
					+ insets.bottom);

			aaFrmDsabldInfoMRG022.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmDisabledPersonInformationMRG022 constructor comment.
	 */
	public FrmDisabledPersonInformationMRG022()
	{
		super();
		initialize();
	}

	/**
	 * FrmDisabledPersonInformationMRG022 constructor with parent.
	 * 
	 * @param aaCust Dialog
	 */
	public FrmDisabledPersonInformationMRG022(Dialog aaCust)
	{
		super(aaCust);
		initialize();
	}

	/**
	 * FrmDisabledPersonInformationMRG022 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDisabledPersonInformationMRG022(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);

			// RECORD NOT APPLICABLE 
			if (aaAE.getSource() == getbtnRcdNotApplicable())
			{
				resetScreen();
			}
			// COMBO ID TYPE 
			else if (aaAE.getSource() == getcomboIdType())
			{
				int liIndex = getcomboIdType().getSelectedIndex();
				if (liIndex >= 0)
				{
					String lsCustIdTypeCd =
						(String) cvIdType.elementAt(liIndex);
					lsCustIdTypeCd =
						lsCustIdTypeCd.substring(50).trim();
					DisabledPlacardCustomerIdTypeData laDPCustIdTypeData =
						DisabledPlacardCustomerIdTypeCache
							.getDsabldPlcrdCustIdType(
							(new Integer(lsCustIdTypeCd)).intValue());

					boolean lbDPPerson =
						getradioIndividual().isSelected();

					if (laDPCustIdTypeData.getInstIndi() == 1)
					{
						if (!getradioInstitution().isSelected())
						{
							getradioInstitution().setSelected(true);
							getchkDsabldVetPlate().setSelected(false);
							getchkDsabldVetPlate().setEnabled(false);
							getradioPermanent().setSelected(true);
						}
					}
					else if (!lbDPPerson)
					{
						getradioIndividual().setSelected(true);
						getchkDsabldVetPlate().setEnabled(true);
						getchkDsabldPlate().setSelected(false);
						getradioTermNone().setSelected(true);
					}
					setEnabledForDisplay(true);
					if (!getButtonPanel1().getBtnEnter().isEnabled())
					{
						getButtonPanel1().getBtnEnter().setEnabled(
							true);
					}
				}
			}
			// ENTER 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (!validateFields() || !confirmCntyRes())
				{
					return;
				}

				setDataToDataObject();

				if (caDPCustData.isNoRecordFound() & cbInitialSet)
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.CTL001,
							MSG_NEW_APPLICANT
								+ CommonConstant.TXT_CONTINUE_QUESTION,
							ScreenConstant.CTL001_FRM_TITLE);

					int liRetCode = leRTSEx.displayError(this);
					if (liRetCode == RTSException.YES)
					{
						getController().processData(
							MiscellaneousRegConstant.INSERT,
							caDPCustData);
					}
					else
					{
						return;
					}
				}
				else if (isDataModified() || isCountyModified())
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.CTL001,
							MSG_UPDATE_APPPLICANT
								+ CommonConstant.TXT_CONTINUE_QUESTION,
							ScreenConstant.CTL001_FRM_TITLE);
					int liRetCode = leRTSEx.displayError(this);
					if (liRetCode == RTSException.YES)
					{
						getController().processData(
							MiscellaneousRegConstant.UPDATE,
							caDPCustData);
					}
					else
					{
						return;
					}
				}
				else
				{
					getController().processData(
						VCDisabledPersonInformationMRG022.MRG023,
						caDPCustData);
				}
				cbInitialSet = false;
			}
			// CANCEL 
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				boolean lbContinue = true;

				if (cbInitialSet
					&& !caDPCustData.isNoRecordFound()
					&& isDataModified())
				{
					lbContinue = handleCancel();
				}
				if (lbContinue)
				{
					int liProcess =
						cbReleaseRequired
							? VCDisabledPersonInformationMRG022
								.RELEASECANCEL
							: AbstractViewController.CANCEL;

					getController().processData(
						liProcess,
						caDPCustData);
				}
			}
			// HELP 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
		}

		finally
		{
			doneWorking();
		}
	}

	/**
	 * Verify resident county. If no set focus to county resident number.
	 * 
	 * @return boolean
	 */
	private boolean confirmCntyRes()
	{
		boolean lbConfirm = true;

		if (!isINQ() && !SystemProperty.isCounty())
		{
			String lsCurrCnty = gettxtResCnty().getText().trim();

			int liOfcId = Integer.parseInt(lsCurrCnty);

			OfficeIdsData laOfcData = OfficeIdsCache.getOfcId(liOfcId);

			FrmCountyConfirmCTL002 laCntyCnfrm =
				new FrmCountyConfirmCTL002(
					getController().getMediator().getDesktop(),
					lsCurrCnty,
					laOfcData.getOfcName());

			lbConfirm =
				laCntyCnfrm.displayWindow()
					== FrmCountyConfirmCTL002.YES;
		}
		return lbConfirm;
	}

	/**
	 * This method initializes ivjbtnRcdNotApplicable
	 * 
	 * @return JButton
	 */
	private RTSButton getbtnRcdNotApplicable()
	{
		if (ivjbtnRcdNotApplicable == null)
		{
			ivjbtnRcdNotApplicable = new RTSButton();
			ivjbtnRcdNotApplicable.setSize(161, 29);
			ivjbtnRcdNotApplicable.setLocation(397, 386);
			ivjbtnRcdNotApplicable.setText("Record Not Applicable");
			ivjbtnRcdNotApplicable.setEnabled(true);
			ivjbtnRcdNotApplicable.setMnemonic(KeyEvent.VK_C);
			ivjbtnRcdNotApplicable.addActionListener(this);
		}
		return ivjbtnRcdNotApplicable;
	}

	/**
	 * This method initializes the ENTER, CANCEL, HELP panel.
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
				ivjButtonPanel1.setBounds(213, 449, 257, 33);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * This method initializes ivjchkDisabledPlate
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkDsabldPlate()
	{
		if (ivjchkDisabledPlate == null)
		{
			ivjchkDisabledPlate = new JCheckBox();
			ivjchkDisabledPlate.setSize(194, 24);
			ivjchkDisabledPlate.setText("Existing Disabled Plate ");
			ivjchkDisabledPlate.setMnemonic(KeyEvent.VK_B);
			ivjchkDisabledPlate.setLocation(65, 19);
			ivjchkDisabledPlate.setPreferredSize(
				new java.awt.Dimension(160, 24));
		}
		return ivjchkDisabledPlate;
	}

	/**
	 * This method initializes ivjchkDsabldVetPlate
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkDsabldVetPlate()
	{
		if (ivjchkDsabldVetPlate == null)
		{
			ivjchkDsabldVetPlate = new JCheckBox();
			ivjchkDsabldVetPlate.setSize(211, 24);
			ivjchkDsabldVetPlate.setText(
				"Existing Disabled Veteran Plate");
			ivjchkDsabldVetPlate.setLocation(65, 44);
			ivjchkDsabldVetPlate.setMnemonic(KeyEvent.VK_V);
		}
		return ivjchkDsabldVetPlate;
	}

	/**
	 * Return the chkUSA property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkUSA()
	{
		if (ivjchkUSA == null)
		{
			try
			{
				ivjchkUSA = new JCheckBox();
				ivjchkUSA.setBounds(232, 11, 50, 19);
				ivjchkUSA.setName("chkUSA");

				// defect 10127 
				ivjchkUSA.setMnemonic(KeyEvent.VK_U);
				ivjchkUSA.setText(CommonConstant.STR_USA);
				// end defect 10127 

				ivjchkUSA.setMaximumSize(
					new java.awt.Dimension(49, 24));
				ivjchkUSA.setActionCommand(CommonConstant.STR_USA);
				ivjchkUSA.setSelected(true);
				ivjchkUSA.setMinimumSize(
					new java.awt.Dimension(49, 24));
				// user code begin {1}
				ivjchkUSA.addItemListener(this);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjchkUSA;
	}
	/**
	 * This method initializes ivjcomboIdType
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboIdType()
	{
		if (ivjcomboIdType == null)
		{
			ivjcomboIdType = new JComboBox();
			ivjcomboIdType.setSize(262, 25);
			ivjcomboIdType.setLocation(95, 27);
			ivjcomboIdType.setEnabled(false);
		}
		return ivjcomboIdType;
	}

	/**
	 * Return the FrmDisabledPersonInformationMRG022ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmDisabledPersonInformationMRG022ContentPane1()
	{
		if (ivjFrmDisabledPersonInformationMRG022ContentPane1 == null)
		{
			try
			{
				ivjFrmDisabledPersonInformationMRG022ContentPane1 =
					new JPanel();
				ivjFrmDisabledPersonInformationMRG022ContentPane1
					.setName(
					"FrmCustAddressREG033ContentPane1");
				ivjFrmDisabledPersonInformationMRG022ContentPane1
					.setLayout(
					null);
				ivjFrmDisabledPersonInformationMRG022ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmDisabledPersonInformationMRG022ContentPane1.add(
					getJPanel(),
					null);
				ivjFrmDisabledPersonInformationMRG022ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmDisabledPersonInformationMRG022ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(600, 450));
				ivjFrmDisabledPersonInformationMRG022ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);

			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjFrmDisabledPersonInformationMRG022ContentPane1;

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
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getpanelContactInfo(), null);
			jPanel.add(getlblScreenMessage(), null);
			jPanel.add(getbtnRcdNotApplicable(), null);
			jPanel.add(getpanelAddress(), null);
			jPanel.add(getJPanel1(), null);
			jPanel.add(getpanelCustId(), null);
			jPanel.setBounds(8, 9, 645, 435);
			Border b = new TitledBorder(new EtchedBorder(), "");
			jPanel.setBorder(b);

		}
		return jPanel;
	}
	/**
	 * This method initializes jPanel1
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.add(getpanelType(), null);
			jPanel1.add(getpanelCheckbox(), null);
			jPanel1.add(getpanelDisabledName(), null);
			jPanel1.add(getpanelInstitutionName(), null);
			jPanel1.add(getpanelCustomerType(), null);
			jPanel1.setBounds(8, 59, 319, 368);
		}
		return jPanel1;
	}
	/**
	 * This method initializes ivjlblPrevCounty
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPrevCounty()
	{
		if (ivjlblPrevCounty == null)
		{
			ivjlblPrevCounty = new JLabel();
			ivjlblPrevCounty.setBounds(115, 108, 190, 20);
			ivjlblPrevCounty.setFont(
				new java.awt.Font("Dialog", java.awt.Font.ITALIC, 12));
		}
		return ivjlblPrevCounty;
	}

	/**
	 * This method initializes ivjlblScreenMessage
	 * 
	 * @return JLabel
	 */
	private JLabel getlblScreenMessage()
	{
		if (ivjlblScreenMessage == null)
		{
			ivjlblScreenMessage = new JLabel();
			ivjlblScreenMessage.setBounds(331, 350, 309, 31);
			ivjlblScreenMessage.setText("");
			ivjlblScreenMessage.setForeground(java.awt.Color.red);
			ivjlblScreenMessage.setHorizontalAlignment(
				SwingConstants.CENTER);
		}
		return ivjlblScreenMessage;
	}

	/**
	 * Return the JPanel3 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpanelAddress()
	{
		if (ivjpanelAddress == null)
		{
			try
			{
				ivjpanelAddress = new JPanel();
				ivjpanelAddress.setName("JPanel3");
				ivjpanelAddress.setLayout(null);
				ivjpanelAddress.add(getchkUSA(), null);
				ivjpanelAddress.add(gettxtCustStreet1(), null);
				ivjpanelAddress.add(gettxtCustStreet2(), null);
				ivjpanelAddress.add(getpanelCityStateCntry(), null);
				ivjpanelAddress.add(getstcLblCounty(), null);
				ivjpanelAddress.add(gettxtResCnty(), null);
				ivjpanelAddress.add(getstcLblAddress(), null);
				ivjpanelAddress.add(getlblPrevCounty(), null);
				ivjpanelAddress.setSize(308, 135);
				ivjpanelAddress.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpanelAddress.setMinimumSize(
					new java.awt.Dimension(275, 141));
				Border b = new TitledBorder(new EtchedBorder(), "");
				ivjpanelAddress.setBorder(b);
				ivjpanelAddress.setLocation(331, 129);
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjpanelAddress;
	}

	/**
	 * This method initializes ivjpanelCheckbox
	 * 
	 * @return JPanel
	 */
	private JPanel getpanelCheckbox()
	{
		if (ivjpanelCheckbox == null)
		{
			ivjpanelCheckbox = new JPanel();
			ivjpanelCheckbox.setLayout(null);
			ivjpanelCheckbox.add(getchkDsabldVetPlate(), null);
			ivjpanelCheckbox.add(getchkDsabldPlate(), null);
			ivjpanelCheckbox.setBounds(7, 211, 306, 74);
			Border b =
				new TitledBorder(
					new EtchedBorder(),
					"Select as Appropriate:");
			ivjpanelCheckbox.setBorder(b);
		}
		return ivjpanelCheckbox;
	}

	/**
	 * Return the JPanel6 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpanelCityStateCntry()
	{
		if (ivjpanelCityStateCntry == null)
		{
			try
			{
				ivjpanelCityStateCntry = new JPanel();
				ivjpanelCityStateCntry.setName("JPanel6");
				ivjpanelCityStateCntry.setLayout(null);
				ivjpanelCityStateCntry.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpanelCityStateCntry.setMinimumSize(
					new java.awt.Dimension(260, 24));
				getpanelCityStateCntry().add(
					gettxtCustCity(),
					gettxtCustCity().getName());
				getpanelCityStateCntry().add(
					gettxtCustState(),
					gettxtCustState().getName());
				getpanelCityStateCntry().add(
					gettxtCustZpcd(),
					gettxtCustZpcd().getName());
				getpanelCityStateCntry().add(
					getstcLblDash(),
					getstcLblDash().getName());
				getpanelCityStateCntry().add(
					gettxtCustZpcdP4(),
					gettxtCustZpcdP4().getName());
				getpanelCityStateCntry().add(
					gettxtCustCntry(),
					gettxtCustCntry().getName());
				getpanelCityStateCntry().add(
					gettxtCustCntryZpcd(),
					gettxtCustCntryZpcd().getName());
				// user code begin {1}
				ivjpanelCityStateCntry.setBounds(11, 79, 279, 25);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjpanelCityStateCntry;
	}

	/**
	 * Return the JPanel4 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpanelContactInfo()
	{
		if (ivjpanelContactInfo == null)
		{
			try
			{
				ivjpanelContactInfo = new JPanel();
				ivjpanelContactInfo.setLayout(null);
				ivjpanelContactInfo.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpanelContactInfo.setMinimumSize(
					new java.awt.Dimension(520, 238));

				ivjpanelContactInfo.add(getstcLblEMail(), null);
				ivjpanelContactInfo.add(getstcLblPhone(), null);
				ivjpanelContactInfo.add(gettxtEMail(), null);
				ivjpanelContactInfo.add(gettxtPhoneNo(), null);
				ivjpanelContactInfo.setSize(310, 74);
				Border b =
					new TitledBorder(
						new EtchedBorder(),
						"Contact Information:");
				ivjpanelContactInfo.setBorder(b);

				ivjpanelContactInfo.setLocation(330, 270);
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjpanelContactInfo;
	}

	/**
	 * Return the ivjpanelCustId property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpanelCustId()
	{
		if (ivjpanelCustId == null)
		{
			try
			{
				ivjpanelCustId = new JPanel();
				ivjpanelCustId.setLayout(null);
				ivjpanelCustId.add(getstcLblCustId(), null);
				ivjpanelCustId.add(gettxtCustId(), null);
				ivjpanelCustId.add(getstcLblIdType(), null);
				ivjpanelCustId.add(getcomboIdType(), null);
				ivjpanelCustId.setBounds(13, 6, 367, 53);
				ivjpanelCustId.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpanelCustId.setMinimumSize(
					new java.awt.Dimension(275, 88));

			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjpanelCustId;
	}
	/**
	 * This method initializes ivjpanelCustomerType
	 * 
	 * @return JPanel
	 */
	private JPanel getpanelCustomerType()
	{
		if (ivjpanelCustomerType == null)
		{
			ivjpanelCustomerType = new JPanel();
			ivjpanelCustomerType.setLayout(null);
			ivjpanelCustomerType.add(getradioIndividual(), null);
			ivjpanelCustomerType.add(getradioInstitution(), null);
			ivjpanelCustomerType.add(getradioCustIdTypeNone(), null);
			ivjpanelCustomerType.setBounds(7, 7, 306, 51);
			RTSButtonGroup laButtonGrp = new RTSButtonGroup();
			laButtonGrp.add(getradioIndividual());
			laButtonGrp.add(getradioInstitution());
			laButtonGrp.add(getradioCustIdTypeNone());
			Border b =
				new TitledBorder(new EtchedBorder(), "Select One:");
			ivjpanelCustomerType.setBorder(b);
		}
		return ivjpanelCustomerType;
	}

	/**
	 * This method initializes ivjpanelDisabledName
	 * 
	 * @return JPanel
	 */
	private JPanel getpanelDisabledName()
	{
		if (ivjpanelDisabledName == null)
		{
			ivjpanelDisabledName = new JPanel();
			ivjpanelDisabledName.setLayout(null);
			ivjpanelDisabledName.add(getstcLblCustFirstName(), null);
			ivjpanelDisabledName.add(gettxtCustFirstName(), null);
			ivjpanelDisabledName.add(getstcLblCustMI(), null);
			ivjpanelDisabledName.add(gettxtCustMI(), null);
			ivjpanelDisabledName.add(getstcLblCustLastName(), null);
			ivjpanelDisabledName.add(gettxtCustLastName(), null);
			ivjpanelDisabledName.setBounds(8, 112, 306, 94);
			Border b =
				new TitledBorder(
					new EtchedBorder(),
					"Disabled PersonOld:");
			ivjpanelDisabledName.setBorder(b);

		}
		return ivjpanelDisabledName;
	}
	/**
	 * This method initializes ivjpanelInstitutionName
	 * 
	 * @return JPanel
	 */
	private JPanel getpanelInstitutionName()
	{
		if (ivjpanelInstitutionName == null)
		{
			ivjpanelInstitutionName = new JPanel();
			ivjpanelInstitutionName.setLayout(null);
			ivjpanelInstitutionName.add(gettxtInstitutionName(), null);
			ivjpanelInstitutionName.setSize(307, 46);
			Border b =
				new TitledBorder(
					new EtchedBorder(),
					"Institution Name:");
			ivjpanelInstitutionName.setBorder(b);

			ivjpanelInstitutionName.setLocation(7, 62);
		}
		return ivjpanelInstitutionName;
	}

	/**
	 * This method initializes ivjpanelType
	 * 
	 * @return JPanel
	 */
	private JPanel getpanelType()
	{
		if (ivjpanelType == null)
		{
			ivjpanelType = new JPanel();
			ivjpanelType.setLayout(null);
			// defect 10133 
			ivjpanelType.add(getradioPermanent(), null);
			ivjpanelType.add(getradioTemporary(), null);
			ivjpanelType.add(getradioTermNone(), null);
			ivjpanelType.setBounds(7, 290, 306, 66);
			RTSButtonGroup laButtonGrp = new RTSButtonGroup();
			laButtonGrp.add(getradioPermanent());
			laButtonGrp.add(getradioTemporary());
			laButtonGrp.add(getradioTermNone());
			// end defect 10133 
			Border b =
				new TitledBorder(new EtchedBorder(), "Select Type:");
			ivjpanelType.setBorder(b);
		}
		return ivjpanelType;
	}

	/**
	 * This method initializes ivjradioCustIdTypeNone
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCustIdTypeNone()
	{
		if (ivjradioCustIdTypeNone == null)
		{
			ivjradioCustIdTypeNone = new JRadioButton();
			ivjradioCustIdTypeNone.setBounds(271, 18, 21, 21);
			ivjradioCustIdTypeNone.setVisible(false);
			ivjradioCustIdTypeNone.setEnabled(false);
			ivjradioCustIdTypeNone.setSelected(true);
		}
		return ivjradioCustIdTypeNone;
	}
	/**
	 * This method initializes ivjradioIndividual
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioIndividual()
	{
		if (ivjradioIndividual == null)
		{
			ivjradioIndividual = new JRadioButton();
			ivjradioIndividual.setBounds(162, 16, 133, 24);
			ivjradioIndividual.setText("Disabled PersonOld");
			ivjradioIndividual.addItemListener(this);
			ivjradioIndividual.setMnemonic(KeyEvent.VK_D);
		}
		return ivjradioIndividual;
	}
	/**
	 * This method initializes ivjradioInstitution
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioInstitution()
	{
		if (ivjradioInstitution == null)
		{
			ivjradioInstitution = new JRadioButton();
			ivjradioInstitution.setSize(81, 24);
			ivjradioInstitution.setText("Institution");
			ivjradioInstitution.addItemListener(this);
			ivjradioInstitution.setMnemonic(KeyEvent.VK_S);
			ivjradioInstitution.setLocation(48, 16);
		}
		return ivjradioInstitution;
	}

	/**
	 * This method initializes ivjradioPermanent
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPermanent()
	{
		if (ivjradioPermanent == null)
		{
			ivjradioPermanent = new JRadioButton();
			ivjradioPermanent.setSize(106, 21);
			ivjradioPermanent.setText("Permanent");
			ivjradioPermanent.setLocation(48, 23);
			ivjradioPermanent.addActionListener(this);
			ivjradioPermanent.setMnemonic(KeyEvent.VK_R);
		}
		return ivjradioPermanent;
	}
	/**
	 * This method initializes ivjradioTemporary
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioTemporary()
	{
		if (ivjradioTemporary == null)
		{
			ivjradioTemporary = new JRadioButton();
			ivjradioTemporary.setBounds(162, 23, 97, 21);
			ivjradioTemporary.setText("Temporary");
			ivjradioTemporary.addActionListener(this);
			ivjradioTemporary.setMnemonic(KeyEvent.VK_T);
		}
		return ivjradioTemporary;
	}
	/**
	 * This method initializes ivjradioTermNone
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioTermNone()
	{
		if (ivjradioTermNone == null)
		{
			ivjradioTermNone = new JRadioButton();
			ivjradioTermNone.setBounds(257, 26, 21, 21);
			ivjradioTermNone.setVisible(false);
			ivjradioTermNone.setSelected(true);
			ivjradioTermNone.setEnabled(false);
		}
		return ivjradioTermNone;
	}
	/**
	 * This method initializes ivjstcLblAddress
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAddress()
	{
		if (ivjstcLblAddress == null)
		{
			ivjstcLblAddress = new JLabel();
			ivjstcLblAddress.setText("Address:");
			ivjstcLblAddress.setDisplayedMnemonic(KeyEvent.VK_A);
			ivjstcLblAddress.setLabelFor(gettxtCustStreet1());
			ivjstcLblAddress.setSize(190, 21);
			ivjstcLblAddress.setLocation(15, 9);
		}
		return ivjstcLblAddress;
	}

	/**
	 * Return the stcLblCounty property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCounty()
	{
		if (ivjstcLblCounty == null)
		{
			try
			{
				ivjstcLblCounty = new JLabel();
				ivjstcLblCounty.setSize(60, 20);
				ivjstcLblCounty.setName("stcLblCounty");
				ivjstcLblCounty.setText("County No:");
				ivjstcLblCounty.setMaximumSize(
					new java.awt.Dimension(119, 14));
				ivjstcLblCounty.setMinimumSize(
					new java.awt.Dimension(119, 14));
				// user code begin {1}
				ivjstcLblCounty.setLocation(15, 108);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblCounty;
	}

	/**
	 * Return the stcLblCustName property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCustFirstName()
	{
		if (ivjstcLblCustFirstName == null)
		{
			try
			{
				ivjstcLblCustFirstName = new JLabel();
				ivjstcLblCustFirstName.setName("stcLblCustName");
				ivjstcLblCustFirstName.setText("First Name:");
				ivjstcLblCustFirstName.setMaximumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblCustFirstName.setMinimumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblCustFirstName.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjstcLblCustFirstName.setDisplayedMnemonic(
					KeyEvent.VK_F);
				ivjstcLblCustFirstName.setLabelFor(
					gettxtCustFirstName());
				// user code begin {1}
				ivjstcLblCustFirstName.setSize(64, 20);
				ivjstcLblCustFirstName.setLocation(19, 19);
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblCustFirstName;
	}

	/**
	 * Return the stcLblCustId property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCustId()
	{
		if (ivjstcLblCustId == null)
		{
			try
			{
				ivjstcLblCustId = new JLabel();
				ivjstcLblCustId.setSize(69, 20);
				ivjstcLblCustId.setName("stcLblCustId");
				ivjstcLblCustId.setText(ID);
				ivjstcLblCustId.setMaximumSize(
					new java.awt.Dimension(54, 14));
				ivjstcLblCustId.setMinimumSize(
					new java.awt.Dimension(54, 14));
				ivjstcLblCustId.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				ivjstcLblCustId.setLocation(14, 3);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblCustId;
	}
	/**
	 * This method initializes ivjstcLblCustLastName
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCustLastName()
	{
		if (ivjstcLblCustLastName == null)
		{
			ivjstcLblCustLastName = new JLabel();
			ivjstcLblCustLastName.setBounds(19, 66, 64, 20);
			ivjstcLblCustLastName.setText("Last Name:");
			ivjstcLblCustLastName.setDisplayedMnemonic(KeyEvent.VK_L);
			ivjstcLblCustLastName.setLabelFor(gettxtCustLastName());
		}
		return ivjstcLblCustLastName;
	}
	/**
	 * This method initializes ivjstcLblCustMI
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCustMI()
	{
		if (ivjstcLblCustMI == null)
		{
			ivjstcLblCustMI = new JLabel();
			ivjstcLblCustMI.setBounds(10, 42, 73, 20);
			ivjstcLblCustMI.setText("Middle Initial:");
			ivjstcLblCustMI.setDisplayedMnemonic(KeyEvent.VK_M);
			ivjstcLblCustMI.setLabelFor(gettxtCustMI());
		}
		return ivjstcLblCustMI;
	}

	/**
	 * Return the stcLblDash12 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDash()
	{
		if (ivjstcLblDash == null)
		{
			try
			{
				ivjstcLblDash = new JLabel();
				ivjstcLblDash.setName("stcLblDash12");
				ivjstcLblDash.setText(DASH);
				ivjstcLblDash.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setBounds(228, 1, 8, 24);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjstcLblDash;
	}
	/**
	 * This method initializes ivjtxtEMail
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEMail()
	{
		if (ivjstcLblEMail == null)
		{
			ivjstcLblEMail = new JLabel();
			ivjstcLblEMail.setSize(37, 20);
			ivjstcLblEMail.setText("E-Mail:");
			ivjstcLblEMail.setLocation(33, 23);
			ivjstcLblEMail.setDisplayedMnemonic(KeyEvent.VK_E);
			ivjstcLblEMail.setLabelFor(gettxtEMail());
		}
		return ivjstcLblEMail;
	}

	/**
	 * This method initializes ivjstcLblIdType
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblIdType()
	{
		if (ivjstcLblIdType == null)
		{
			ivjstcLblIdType = new JLabel();
			ivjstcLblIdType.setSize(43, 20);
			ivjstcLblIdType.setText("Id Type:");
			ivjstcLblIdType.setLocation(40, 30);
			ivjstcLblIdType.setDisplayedMnemonic(KeyEvent.VK_I);
			ivjstcLblIdType.setLabelFor(getcomboIdType());
		}
		return ivjstcLblIdType;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPhone()
	{
		if (ivjstcLblPhone == null)
		{
			ivjstcLblPhone = new JLabel();
			ivjstcLblPhone.setSize(57, 20);
			ivjstcLblPhone.setText("Phone No:");
			ivjstcLblPhone.setLocation(14, 48);
			ivjstcLblPhone.setDisplayedMnemonic(KeyEvent.VK_P);
			ivjstcLblPhone.setLabelFor(gettxtPhoneNo());
		}
		return ivjstcLblPhone;
	}

	/**
	 * Return the ivjtxtCustStreet1 property value.
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustStreet1()
	{
		if (ivjtxtCustStreet1 == null)
		{
			try
			{
				ivjtxtCustStreet1 = new RTSInputField();
				ivjtxtCustStreet1.setSize(269, 20);
				ivjtxtCustStreet1.setName("ivjtxtCustStreet1");
				ivjtxtCustStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustStreet1.setEditable(true);
				ivjtxtCustStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtCustStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code begin {1}
				ivjtxtCustStreet1.setLocation(14, 37);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustStreet1;
	}

	/**
	 * Return the ivjtxtCustStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustStreet2()
	{
		if (ivjtxtCustStreet2 == null)
		{
			try
			{
				ivjtxtCustStreet2 = new RTSInputField();
				ivjtxtCustStreet2.setBounds(14, 61, 269, 20);
				ivjtxtCustStreet2.setName("ivjtxtCustStreet2");
				ivjtxtCustStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustStreet2.setInput(RTSInputField.DEFAULT);
				ivjtxtCustStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustStreet2;
	}

	/**
	 * Return the ivjtxtCustCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustCity()
	{
		if (ivjtxtCustCity == null)
		{
			try
			{
				ivjtxtCustCity = new RTSInputField();
				ivjtxtCustCity.setName("ivjtxtCustCity");
				ivjtxtCustCity.setManagingFocus(false);
				ivjtxtCustCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustCity.setBounds(3, 5, 135, 20);
				ivjtxtCustCity.setText("");
				ivjtxtCustCity.setInput(RTSInputField.DEFAULT);
				ivjtxtCustCity.setMaxLength(CommonConstant.LENGTH_CITY);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustCity;
	}

	/**
	 * Return the ivjtxtCustState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustState()
	{
		if (ivjtxtCustState == null)
		{
			try
			{
				ivjtxtCustState = new RTSInputField();
				ivjtxtCustState.setName("ivjtxtCustState");
				ivjtxtCustState.setManagingFocus(false);
				ivjtxtCustState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustState.setText("");
				ivjtxtCustState.setBounds(146, 4, 25, 20);
				ivjtxtCustState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtCustState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustState;
	}

	/**
	 * Return the ivjtxtCustZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustZpcd()
	{
		if (ivjtxtCustZpcd == null)
		{
			try
			{
				ivjtxtCustZpcd = new RTSInputField();
				ivjtxtCustZpcd.setName("ivjtxtCustZpcd");
				ivjtxtCustZpcd.setManagingFocus(false);
				ivjtxtCustZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustZpcd.setBounds(181, 5, 40, 20);
				ivjtxtCustZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtCustZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustZpcd;
	}

	/**
	 * Return the ivjtxtCustZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustZpcdP4()
	{
		if (ivjtxtCustZpcdP4 == null)
		{
			try
			{
				ivjtxtCustZpcdP4 = new RTSInputField();
				ivjtxtCustZpcdP4.setName("ivjtxtCustZpcdP4");
				ivjtxtCustZpcdP4.setManagingFocus(false);
				ivjtxtCustZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustZpcdP4.setBounds(236, 5, 36, 20);
				ivjtxtCustZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtCustZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustZpcdP4;
	}

	/**
	 * Return the txtCustCntry property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtCustCntry()
	{
		if (ivjtxtCustCntry == null)
		{
			try
			{
				ivjtxtCustCntry = new RTSInputField();
				ivjtxtCustCntry.setSize(44, 20);
				ivjtxtCustCntry.setName("txtCustCntry");
				ivjtxtCustCntry.setManagingFocus(false);
				ivjtxtCustCntry.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustCntry.setText("");
				ivjtxtCustCntry.setVisible(true);
				ivjtxtCustCntry.setInput(RTSInputField.DEFAULT);
				ivjtxtCustCntry.setMaxLength(
					CommonConstant.LENGTH_CNTRY);
				// user code begin {1}
				ivjtxtCustCntry.setLocation(143, 5);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustCntry;
	}

	/**
	 * Return the ivjtxtCustCntryZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustCntryZpcd()
	{
		if (ivjtxtCustCntryZpcd == null)
		{
			try
			{
				ivjtxtCustCntryZpcd = new RTSInputField();
				ivjtxtCustCntryZpcd.setSize(79, 20);
				ivjtxtCustCntryZpcd.setName("ivjtxtCustCntryZpcd");
				ivjtxtCustCntryZpcd.setManagingFocus(false);
				ivjtxtCustCntryZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustCntryZpcd.setText("");
				ivjtxtCustCntryZpcd.setVisible(true);
				ivjtxtCustCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtCustCntryZpcd.setMaxLength(
					CommonConstant.LENGTH_CNTRY_ZIP);
				// user code begin {1}
				ivjtxtCustCntryZpcd.setLocation(193, 5);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustCntryZpcd;
	}

	/**
	 * Return the ivjtxtCustFirstName property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtCustFirstName()
	{
		if (ivjtxtCustFirstName == null)
		{
			try
			{
				ivjtxtCustFirstName = new RTSInputField();
				ivjtxtCustFirstName.setBounds(87, 19, 213, 20);
				ivjtxtCustFirstName.setName("txtCustFirstName");
				ivjtxtCustFirstName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustFirstName.setBackground(java.awt.Color.white);
				ivjtxtCustFirstName.setEditable(true);
				ivjtxtCustFirstName.setInput(RTSInputField.DEFAULT);
				ivjtxtCustFirstName.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// user code begin {1}
				ivjtxtCustFirstName.setEnabled(false);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustFirstName;
	}

	/**
	 * Return the ivjtxtCustId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustId()
	{
		if (ivjtxtCustId == null)
		{
			try
			{
				ivjtxtCustId = new RTSInputField();
				ivjtxtCustId.setBounds(95, 3, 140, 20);
				ivjtxtCustId.setName("txtCustId");
				ivjtxtCustId.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustId.setInput(RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtCustId.setMaxLength(15);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustId;
	}

	/**
	 * This method initializes ivjtxtCustLastName
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustLastName()
	{
		if (ivjtxtCustLastName == null)
		{
			ivjtxtCustLastName = new RTSInputField();
			ivjtxtCustLastName.setBounds(87, 66, 213, 20);
			ivjtxtCustLastName.setInput(RTSInputField.DEFAULT);
			ivjtxtCustLastName.setMaxLength(CommonConstant.LENGTH_NAME);
			ivjtxtCustLastName.setEnabled(false);
		}
		return ivjtxtCustLastName;
	}

	/**
	 * Return the ivjtxtCustMI property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private RTSInputField gettxtCustMI()
	{
		if (ivjtxtCustMI == null)
		{
			try
			{
				ivjtxtCustMI = new RTSInputField();
				ivjtxtCustMI.setBounds(87, 42, 21, 20);
				ivjtxtCustMI.setName("ivjtxtCustMI");
				ivjtxtCustMI.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustMI.setBackground(java.awt.Color.white);
				ivjtxtCustMI.setEditable(true);
				ivjtxtCustMI.setInput(RTSInputField.DEFAULT);
				ivjtxtCustMI.setMaxLength(1);
				// user code begin {1}
				ivjtxtCustMI.setEnabled(false);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtCustMI;
	}

	/**
	 * This method initializes RTSInputField
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtEMail()
	{
		if (ivjtxtEMail == null)
		{
			ivjtxtEMail = new RTSInputField();
			ivjtxtEMail.setMaxLength(50);
			ivjtxtEMail.setBounds(75, 22, 228, 21);
		}
		return ivjtxtEMail;
	}

	/**
	 * This method initializes ivjtxtInstitutionName
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtInstitutionName()
	{
		if (ivjtxtInstitutionName == null)
		{
			ivjtxtInstitutionName = new RTSInputField();
			ivjtxtInstitutionName.setSize(294, 21);
			ivjtxtInstitutionName.setMaxLength(60);
			ivjtxtInstitutionName.setLocation(7, 17);
		}
		return ivjtxtInstitutionName;
	}

	/**
	 * This method initializes ivjtxtPhoneNo
	 * 
	 * @return RTSPhoneField
	 */
	private RTSPhoneField gettxtPhoneNo()
	{
		if (ivjtxtPhoneNo == null)
		{
			ivjtxtPhoneNo = new RTSPhoneField();
			ivjtxtPhoneNo.setSize(91, 20);
			ivjtxtPhoneNo.setLocation(75, 48);
		}
		return ivjtxtPhoneNo;
	}

	/**
	 * Return the txtResCnty property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtResCnty()
	{
		if (ivjtxtResCnty == null)
		{
			try
			{
				ivjtxtResCnty = new RTSInputField();
				ivjtxtResCnty.setBounds(77, 108, 33, 20);
				ivjtxtResCnty.setName("txtResCnty");
				ivjtxtResCnty.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtResCnty.setText("");
				ivjtxtResCnty.setBackground(java.awt.Color.white);
				ivjtxtResCnty.setEnabled(true);
				ivjtxtResCnty.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtResCnty.setMaxLength(
					CommonConstant.LENGTH_OFFICE_ISSUANCENO);
				ivjtxtResCnty.setEditable(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjtxtResCnty;
	}

	/**
	 * Process the cancel button or escape key event.
	 * 
	 * @return boolean
	 */
	private boolean handleCancel()
	{
		RTSException leRTSException =
			new RTSException(
				RTSException.CTL001,
				MSG_MODIFIED_APPPLICANT + ERRMSG_DYWT_CANCEL,
				null);

		return leRTSException.displayError(this) == RTSException.YES;
	}

	/**
	 * Handle Data to Display
	 */
	private void handleDataToDisplay()
	{
		// Reset Radio Buttons
		getradioCustIdTypeNone().setSelected(true);
		getradioTermNone().setSelected(true);

		gettxtCustId().setEnabled(false);
		populateIdType();

		gettxtCustId().setText(
			returnEmptyIfNull(caDPCustData.getCustId()));

		gettxtCustFirstName().setText(
			returnEmptyIfNull(caDPCustData.getDsabldFrstName()));
		gettxtCustMI().setText(
			returnEmptyIfNull(caDPCustData.getDsabldMI()));
		gettxtCustLastName().setText(
			returnEmptyIfNull(caDPCustData.getDsabldLstName()));

		gettxtInstitutionName().setText(
			returnEmptyIfNull(caDPCustData.getInstName()));

		// Address 
		// defect 10127
		caNameAddrComp.setAddressDataToDisplay(
			caDPCustData.getAddressData());
		// end defect 10127   

		// County will NOT change if using INQ or record "In Process"  
		if (!caDPCustData.isAvailableForUpdate())
		{
			gettxtResCnty().setText(
				"" + caDPCustData.getResComptCntyNo());
			gettxtResCnty().setEnabled(false);
		}
		else
		{
			if (SystemProperty.isCounty())
			{
				gettxtResCnty().setText(
					"" + SystemProperty.getOfficeIssuanceNo());
				gettxtResCnty().setEnabled(false);
			}
			else
			{
				gettxtResCnty().setText(
					"" + caDPCustData.getResComptCntyNo());
				gettxtResCnty().setEnabled(true);
			}

			int liPrevCntyNo = caDPCustData.getResComptCntyNo();
			getlblPrevCounty().setText("");
			if (!caDPCustData.isNoRecordFound()
				&& SystemProperty.isCounty()
				&& liPrevCntyNo != SystemProperty.getOfficeIssuanceNo())
			{
				OfficeIdsData laData =
					OfficeIdsCache.getOfcId(liPrevCntyNo);
				if (laData != null)
				{
					getlblPrevCounty().setText(
						"(Prev: "
							+ liPrevCntyNo
							+ "  "
							+ laData.getOfcName()
							+ ")");
					getlblPrevCounty().setEnabled(false);
				}
			}
		}

		gettxtEMail().setText(
			returnEmptyIfNull(caDPCustData.getEMail()));
		gettxtPhoneNo().setPhoneNo(
			returnEmptyIfNull(caDPCustData.getPhone()));

		if (caDPCustData.isNoRecordFound())
		{
			// Only Combo for Id Type to be enabled 
			getcomboIdType().setEnabled(true);
			getcomboIdType().setSelectedIndex(-1);

			// Record Not Applicable does not make sense on NRF 
			getbtnRcdNotApplicable().setEnabled(false);

			getstcLblCustFirstName().setEnabled(false);
			getstcLblCustMI().setEnabled(false);
			getstcLblCustLastName().setEnabled(false);

			getlblScreenMessage().setText("NO RECORD FOUND");

			// "None" radio buttons used to reset prior selections, i.e.
			//  neither Perm, Temp, Mobility, Non-Mobility selected  
			getradioCustIdTypeNone().setSelected(true);
			getradioTermNone().setSelected(true);

			getchkDsabldPlate().setSelected(false);
			getchkDsabldVetPlate().setSelected(false);

			// Enter is enabled when Cust Id Type Enabled  
			getButtonPanel1().getBtnEnter().setEnabled(false);
		}
		else
		{
			// Record Not Applicable Not available if either  
			//  - Do not search by Cust Id 
			//  - Using Placard Inquiry 
			getbtnRcdNotApplicable().setEnabled(
				caDPCustData.getSearchType()
					== MiscellaneousRegConstant.CUST_ID
					&& !isINQ());

			// Can NOT modify Cust Id, Id Type, Name 		
			getcomboIdType().setEnabled(false);

			// Institution or Disabled PersonOld  Radio
			boolean lbInstitution = caDPCustData.isInstitution();
			getradioInstitution().setSelected(lbInstitution);
			getradioIndividual().setSelected(!lbInstitution);

			// defect 10133 
			//	// Mobility or Non-Mobility Radio 
			//	boolean lbMobility =
			//	 caDPCustData.getMobltyDsabldIndi() == 1;
			//	getradioMobility().setSelected(lbMobility);
			//	getradioNonMobility().setSelected(!lbMobility);

			// Permanent or Temporary Radio 
			boolean lbPermanent = caDPCustData.isPermDsabld();
			// end defect 10133

			getradioPermanent().setSelected(lbPermanent);
			getradioTemporary().setSelected(!lbPermanent);

			getchkDsabldPlate().setSelected(
				caDPCustData.isDsabldPltIndi());
			getchkDsabldVetPlate().setSelected(
				caDPCustData.isDsabldVetPltIndi());
		}
		getradioIndividual().setEnabled(false);
		getradioInstitution().setEnabled(false);
		gettxtCustFirstName().setEnabled(false);
		gettxtCustMI().setEnabled(false);
		gettxtCustLastName().setEnabled(false);
		gettxtInstitutionName().setEnabled(false);
		setEnabledForDisplay(cbReleaseRequired);

		if (!caDPCustData.isNoRecordFound())
		{
			String lsMsg = "NO ASSIGNED PLACARDS";
			int liActPlcrdNo = 0;
			Vector lvPlcrd = caDPCustData.getDsabldPlcrd();
			for (int i = 0; i < lvPlcrd.size(); i++)
			{
				DisabledPlacardData laDPData =
					(DisabledPlacardData) lvPlcrd.elementAt(i);
				if (laDPData.isActive())
				{
					liActPlcrdNo = liActPlcrdNo + 1;
				}
			}
			if (liActPlcrdNo > 0)
			{
				if (liActPlcrdNo == 1)
				{
					lsMsg = "ONE ASSIGNED PLACARD ";
				}
				else
				{
					lsMsg = "MORE THAN ONE ASSIGNED PLACARD";
				}
			}
			getlblScreenMessage().setText(lsMsg);

			if (!isINQ()
				&& !cbReleaseRequired
				&& !cbInProcsAlreadyDsplyed)
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.INFORMATION_MESSAGE,
						"A transaction involving this applicant is "
							+ "currently in process. "
							+ "Update will not be available. ",
						"INFORMATION");
				RTSDialogBox laRTSDBox =
					getController().getMediator().getParent();
				if (laRTSDBox != null)
				{
					leRTSEx.displayError(laRTSDBox);
				}
				else
				{
					leRTSEx.displayError(this);
				}
				cbInProcsAlreadyDsplyed = true;
			}
		}
		setSetDataFinished(true);
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private void initialize()
	{
		try
		{
			// user code begin {1}
			setRequestFocus(false);
			// user code end
			setName("FrmDisabledPersonInformationMRG022");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(665, 514);
			setTitle("Disabled PersonOld Information     MRG022");
			setContentPane(
				getFrmDisabledPersonInformationMRG022ContentPane1());

			// defect 10491 
			// Implement ErrorsConstant.ERR_NUM_INCOMPLETE_APPL_DATA
			// defect 10127 
			caNameAddrComp =
				new NameAddressComponent(
					gettxtCustFirstName(),
					gettxtCustLastName(),
					gettxtCustStreet1(),
					gettxtCustStreet2(),
					gettxtCustCity(),
					gettxtCustState(),
					gettxtCustZpcd(),
					gettxtCustZpcdP4(),
					gettxtCustCntry(),
					gettxtCustCntryZpcd(),
					getchkUSA(),
					getstcLblDash(),
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID,
					ErrorsConstant.ERR_NUM_INCOMPLETE_APPL_DATA,
					CommonConstant.TX_DEFAULT_STATE);
			// end defect 10127 
			// end defect 10491 

			// defect 10299  
			cvAddlMFValid = new Vector();
			cvAddlMFValid.add(gettxtInstitutionName());
			cvAddlMFValid.add(gettxtCustFirstName());
			cvAddlMFValid.add(gettxtCustMI());
			cvAddlMFValid.add(gettxtCustLastName());
			// end defect 10299 
		}
		catch (Throwable aeEx)
		{
			handleException(aeEx);
		}
		// user code begin {2}
		//addWindowListener(this);
	}

	/**
	 * Return boolean denoting if in Disabled Placard Inquiry Event
	 * 
	 * @return boolean 
	 */
	private boolean isINQ()
	{
		return getController().getTransCode().equals(
			MiscellaneousRegConstant.INQ);
	}

	/**
	 * Return boolean denoting if in Data has been modified
	 *   (does not compare ResComptCntyNo)   
	 * 
	 * @return boolean
	 */
	private boolean isDataModified()
	{
		boolean lbModified = false;

		if (!caOrigDPCustData.isNoRecordFound())
		{
			lbModified =
				(!caOrigDsabldAddressData
					.getSt1()
					.trim()
					.equals(gettxtCustStreet1().getText().trim()))
					|| (!caOrigDsabldAddressData
						.getSt2()
						.trim()
						.equals(gettxtCustStreet2().getText().trim()))
					|| (!caOrigDsabldAddressData
						.getCity()
						.trim()
						.equals(gettxtCustCity().getText().trim()))
					|| (caOrigDsabldAddressData.isUSA()
						!= getchkUSA().isSelected());

			if (!lbModified)
			{
				if (caOrigDsabldAddressData.isUSA())
				{
					lbModified =
						!caOrigDsabldAddressData
							.getState()
							.trim()
							.equals(
							gettxtCustState().getText().trim())
							|| !caOrigDsabldAddressData.getZpcd().equals(
								gettxtCustZpcd().getText().trim());

					if (!lbModified)
					{
						String lsOrigZpCdP4 =
							returnEmptyIfNull(
								caOrigDsabldAddressData.getZpcdp4());
						String lsZpCdP4 =
							gettxtCustZpcdP4().getText().trim();
						lbModified = !lsOrigZpCdP4.equals(lsZpCdP4);
					}
				}
				else
				{
					lbModified =
						!gettxtCustCntry().getText().trim().equals(
							caOrigDsabldAddressData.getCntry().trim())
							|| !gettxtCustCntryZpcd()
								.getText()
								.trim()
								.equals(
								caOrigDsabldAddressData.getCntryZpcd());
				}
			}
			if (!lbModified)
			{
				String lsEMail = gettxtEMail().getText().trim();
				String lsOrigEMail =
					returnEmptyIfNull(caOrigDPCustData.getEMail());

				String lsPhone = gettxtPhoneNo().getPhoneNo().trim();
				String lsOrigPhone =
					returnEmptyIfNull(caOrigDPCustData.getPhone());

				lbModified =
					!lsEMail.equals(lsOrigEMail)
						|| !lsPhone.equals(lsOrigPhone);
			}
			if (!lbModified)
			{
				lbModified =
					(getchkDsabldPlate().isSelected()
						!= caOrigDPCustData.isDsabldPltIndi())
						|| getchkDsabldVetPlate().isSelected()
							!= caOrigDPCustData.isDsabldVetPltIndi();
			}
			if (!lbModified)
			{
				// defect 10133 
				lbModified =
					(getradioPermanent().isSelected()
						!= caOrigDPCustData.isPermDsabld());
				//						|| getradioMobility().isSelected()
				//							!= caOrigDPCustData.isMobltyDsabld();
				// end defect 10133 
			}
		}
		return lbModified;
	}

	/**
	 * Return boolean to denote that the County No has changed
	 * 
	 * @return boolean 
	 */
	private boolean isCountyModified()
	{
		return caDPCustData.getResComptCntyNo()
			!= caOrigDPCustData.getResComptCntyNo();
	}

	/**
	 * Return boolean indicator whether setData method has completed.
	 * 
	 * @return boolean
	 */
	private boolean isSetDataFinished()
	{
		return cbSetDataFinished;
	}

	/**
	 * ItemListener method.
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{

		if (aaIE.getSource() == ivjchkUSA && isSetDataFinished())
		{
			// defect 10127 
			caNameAddrComp.resetPerUSA(
				aaIE.getStateChange() == ItemEvent.SELECTED);
			// end defect 10127
		}
		else if (aaIE.getSource() instanceof JRadioButton)
		{
			clearAllColor(this);

			// These may not be drivent by Id Type
			if (aaIE.getSource() == ivjradioInstitution)
			{
				gettxtCustFirstName().setText("");
				gettxtCustMI().setText("");
				gettxtCustLastName().setText("");
				gettxtCustFirstName().setEnabled(false);
				getstcLblCustFirstName().setEnabled(false);
				getstcLblCustMI().setEnabled(false);
				getstcLblCustLastName().setEnabled(false);
				gettxtCustMI().setEnabled(false);
				gettxtCustLastName().setEnabled(false);
				gettxtInstitutionName().setEnabled(true);
				getchkDsabldVetPlate().setSelected(false);
				getchkDsabldVetPlate().setEnabled(false);
			}
			else if (aaIE.getSource() == ivjradioIndividual)
			{
				gettxtCustFirstName().setEnabled(true);
				gettxtCustMI().setEnabled(true);
				gettxtCustLastName().setEnabled(true);
				getstcLblCustFirstName().setEnabled(true);
				getstcLblCustMI().setEnabled(true);
				getstcLblCustLastName().setEnabled(true);
				gettxtInstitutionName().setEnabled(false);
				gettxtInstitutionName().setText("");
				getchkDsabldVetPlate().setEnabled(true);
				getchkDsabldPlate().setSelected(false);
			}
		}
	}

	/**
	 *  Populate Id Type Combo Box from Cache 
	 */
	private void populateIdType()
	{
		getcomboIdType().removeActionListener(this);
		getcomboIdType().removeAllItems();
		getcomboIdType().setSelectedIndex(-1);
		cvIdType = new Vector();
		Vector lvIdType =
			(Vector) UtilityMethods.copy(
				DisabledPlacardCustomerIdTypeCache
					.getDsabldPlcrdCustIdType());
		DisabledPlacardCustomerIdTypeData laIdTypeData =
			new DisabledPlacardCustomerIdTypeData();

		for (int i = 0; i < lvIdType.size(); i++)
		{
			laIdTypeData =
				(DisabledPlacardCustomerIdTypeData) lvIdType.elementAt(
					i);

			if (caDPCustData.isNoRecordFound()
				|| (caDPCustData.getCustIdTypeCd()
					== laIdTypeData.getCustIdTypeCd()))
			{
				cvIdType.add(
					laIdTypeData.getCustIdTypeDsplyOrdr()
						+ UtilityMethods.addPaddingRight(
							laIdTypeData
								.getCustIdTypeDesc()
								.toUpperCase(),
							50,
							" ")
						+ laIdTypeData.getCustIdTypeCd());
			}
		}
		UtilityMethods.sort(cvIdType);
		for (int i = 0; i < cvIdType.size(); i++)
		{
			String lsElement =
				((String) cvIdType.get(i)).substring(1, 45);
			getcomboIdType().addItem(lsElement.trim());
		}
		if (cvIdType.size() == 1)
		{
			getcomboIdType().setEnabled(false);
		}
		getcomboIdType().addActionListener(this);
	}

	/**
	 * Reset Screen on "Record Not Applicable"
	 * 
	 */
	private void resetScreen()
	{
		if (!cbInProcsAlreadyDsplyed)
		{
			getController().processData(
				VCDisabledPersonInformationMRG022.RELEASE,
				caDPCustData);
		}
		DisabledPlacardCustomerData laDPCustData =
			new DisabledPlacardCustomerData();
		laDPCustData.setCustId(caDPCustData.getCustId());
		laDPCustData.setSearchType(caDPCustData.getSearchType());
		laDPCustData.setNoRecordFound(true);
		cbInitialSet = true;
		setData(laDPCustData);
		getcomboIdType().requestFocus();
	}

	/**
	 * Return Empty String if String is Null 
	 * 
	 * @return String 
	 */
	private String returnEmptyIfNull(String asString)
	{
		return asString != null
			? asString
			: CommonConstant.STR_SPACE_EMPTY;

	}

	/**
	 * Enable/Disabled per parameter
	 * 
	 * @param abEnabled
	 */
	private void setEnabledForDisplay(boolean abEnabled)
	{
		getradioPermanent().setEnabled(abEnabled);

		// defect 10679 
		// Institutions should only be setup as Permanent 
		getradioTemporary().setEnabled(
			abEnabled && !getradioInstitution().isSelected());
		// end defect 10679 

		getchkDsabldPlate().setEnabled(abEnabled);
		getchkDsabldVetPlate().setEnabled(
			abEnabled && getradioIndividual().isSelected());
		getchkUSA().setEnabled(abEnabled);
		gettxtCustStreet1().setEnabled(abEnabled);
		gettxtCustStreet2().setEnabled(abEnabled);
		gettxtCustCity().setEnabled(abEnabled);
		gettxtCustState().setEnabled(abEnabled);
		gettxtCustZpcd().setEnabled(abEnabled);
		gettxtCustZpcdP4().setEnabled(abEnabled);
		gettxtCustCntry().setEnabled(abEnabled);
		gettxtCustCntryZpcd().setEnabled(abEnabled);
		gettxtEMail().setEnabled(abEnabled);
		gettxtPhoneNo().setEnabled(abEnabled);
	}

	/**
	 * Set Cust Address
	 * 
	 * @param aaNewCustAddress AddressData
	 */
	public void setCustAddress(AddressData aaNewCustAddress)
	{
		caDsabldAddressData = aaNewCustAddress;
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
		if (aaDataObject != null)
		{
			caDPCustData = (DisabledPlacardCustomerData) aaDataObject;

			if (isINQ())
			{
				caDPCustData.setInProcsIndi(1);
			}

			cbReleaseRequired =
				(caDPCustData.isAvailableForUpdate()
					&& !(caDPCustData.isNoRecordFound() && cbInitialSet));

			if (caDPCustData.getDsabldPlcrd() == null)
			{
				caDPCustData.setDsabldPlcrd(new Vector());
			}

			caDsabldAddressData = caDPCustData.getAddressData();

			if (caDsabldAddressData == null)
			{
				caDsabldAddressData = new AddressData();
				caDPCustData.setAddressData(caDsabldAddressData);
			}
			caOrigDPCustData =
				(DisabledPlacardCustomerData) (UtilityMethods
					.copy(caDPCustData));
			caOrigDsabldAddressData =
				(AddressData) (UtilityMethods
					.copy(caDsabldAddressData));

			handleDataToDisplay();

			if (caDPCustData.isNoRecordFound())
			{
				setDefaultFocusField(getcomboIdType());
			}
			else
			{
				setDefaultFocusField(gettxtCustStreet1());
			}
		}
	}

	/**
	 * Set input field values to data object.
	 * 
	 */
	private void setDataToDataObject()
	{
		if (caDPCustData.isNoRecordFound())
		{
			String lsIdTypeCd =
				(String) cvIdType.elementAt(
					getcomboIdType().getSelectedIndex());
			int liIdTypeCd =
				(new Integer((lsIdTypeCd.substring(50)).trim()))
					.intValue();
			caDPCustData.setCustIdTypeCd(liIdTypeCd);
		}

		if (getradioInstitution().isSelected())
		{
			caDPCustData.setInstIndi(1);
			caDPCustData.setInstName(gettxtInstitutionName().getText());

		}
		else
		{
			caDPCustData.setInstIndi(0);
			caDPCustData.setDsabldFrstName(
				gettxtCustFirstName().getText());
			caDPCustData.setDsabldMI(gettxtCustMI().getText());
			caDPCustData.setDsabldLstName(
				gettxtCustLastName().getText());
		}

		// ADDRESS INFO
		// defect 10127
		caNameAddrComp.setAddressToDataObject(caDsabldAddressData);
		// end defect 10127 

		caDPCustData.setAddressData(caDsabldAddressData);
		caDPCustData.setDsabldPltIndi(
			getchkDsabldPlate().isSelected() ? 1 : 0);
		caDPCustData.setDsabldVetPltIndi(
			getchkDsabldVetPlate().isSelected() ? 1 : 0);
		caDPCustData.setPermDsabldIndi(
			getradioPermanent().isSelected() ? 1 : 0);

		// defect 10133 
		//caDPCustData.setMobltyDsabldIndi(
		//	getradioMobility().isSelected() ? 1 : 0);
		// end defect 10133 

		caDPCustData.setEMail(gettxtEMail().getText());
		caDPCustData.setPhone(gettxtPhoneNo().getPhoneNo());
		caDPCustData.setResComptCntyNo(
			Integer.parseInt(gettxtResCnty().getText()));
		caDPCustData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		caDPCustData.setSubstaId(SystemProperty.getSubStationId());
		caDPCustData.setWsId(SystemProperty.getWorkStationId());
		caDPCustData.setEmpId(SystemProperty.getCurrentEmpId());
	}

	/**
	 * Set Set Data Finished
	 * 
	 * @param aaNewSetDataFinished boolean
	 */

	private void setSetDataFinished(boolean aaNewSetDataFinished)
	{
		cbSetDataFinished = aaNewSetDataFinished;
	}

	/**
	 * Validate EMail Address & Phone No
	 * 
	 * @param aeRTSEx
	 */
	private void validateEMailPhoneNo(RTSException aeRTSEx)
	{
		// Validate EMail Address if supplied
		if (!CommonValidations.isValidEMail(gettxtEMail().getText()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtEMail());
		}
		// Confirm Phone No Valid if Provided
		if (gettxtPhoneNo().isPhoneNoAllZeros()
			|| (!gettxtPhoneNo().isPhoneNoEmpty()
				&& !gettxtPhoneNo().isValidPhoneNo()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPhoneNo());
		}
	}

	/**
	 * Cust Address controller for field validations.
	 * Return true if there were no validation error else return false.
	 * 
	 * @return boolean
	 */
	private boolean validateFields()
	{
		boolean lbValid = true;

		// defect 10299
		// Add UtilityMethods.trimRTSInputField()
		// Refactor leEx to leRTSEx 
		// Implement validation for MF Characters 
		UtilityMethods.trimRTSInputField(this);

		RTSException leRTSEx = new RTSException();

		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvAddlMFValid,
			leRTSEx);
		// end defect 10299  

		// defect 10127
		// Use ErrorConstant  
		if (caDPCustData.isNoRecordFound())
		{
			if (getcomboIdType().getSelectedIndex() == -1)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getcomboIdType());
			}

			if (!getradioCustIdTypeNone().isSelected())
			{
				validateName(leRTSEx);
			}
			else
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getradioIndividual());
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getradioInstitution());
			}
		}

		caNameAddrComp.validateAddressFields(leRTSEx);
		// end defect 10127

		validateResCnty(leRTSEx);

		validateEMailPhoneNo(leRTSEx);

		// defect 10127 
		// Use Error Constant 
		// Ensure Permanent or Temporary Selected 
		if (getradioTermNone().isSelected())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getradioTemporary());
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getradioPermanent());
		}
		// end defect 10127 
		// defect 10679
		else if (
			getradioInstitution().isSelected()
				&& !getradioPermanent().isSelected())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_TEMP_INVALID_FOR_INST),
				getradioPermanent());
		}
		// end defect 10679 
		
		// defect 10133 
		// Remove verification of Mobility/Non-Mobility 
		//if (getradioMobilityNone().isSelected())
		//	{
		//		leEx.addException(
		//			new RTSException(
		//				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
		//			getradioMobility());
		//		leEx.addException(
		//			new RTSException(
		//				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
		//			getradioNonMobility());
		//	}
		// end defect 10133 

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
	}

	/**
	 * Validate name is complete.
	 * 
	 * @param aeRTSEx RTSException 
	 */
	private void validateName(RTSException aeRTSEx)
	{
		// defect 10127 
		// Use ErrorConstant 

		// Disabled Individual 
		if (getradioIndividual().isSelected())
		{
			if (gettxtCustFirstName().isEmpty())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_INCOMPLETE_APPL_DATA),
					gettxtCustFirstName());
			}
			if (gettxtCustLastName().isEmpty())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_INCOMPLETE_APPL_DATA),
					gettxtCustLastName());
			}
		}
		// Institution 
		else if (getradioInstitution().isSelected())
		{
			if (gettxtInstitutionName().isEmpty())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_INCOMPLETE_APPL_DATA),
					gettxtInstitutionName());

			}
		}
		// end defect 10127 
	}

	/**
	 * Confirm Resident County  
	 * 
	 * @param aeRTSEx RTSException 
	 */
	private void validateResCnty(RTSException aeRTSEx)
	{
		// defect 10127 
		if (!SystemProperty.isCounty()
			&& !gettxtResCnty().isValidCountyNo())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtResCnty());
		}
		// end defect 10127 
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,28"
