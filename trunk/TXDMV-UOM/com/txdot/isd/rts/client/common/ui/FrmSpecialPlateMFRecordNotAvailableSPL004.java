package com.txdot.isd.rts.client.common.ui;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.title.ui.FrmCountyConfirmCTL002;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmSpecialPlateMFRecordNotAvailableSPL004.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	04/12/2007	Created
 * 	J Rue					defect 9085 Ver Special Plates 
 * K Harrell	05/02/2007	Show sunset organizations
 * 							Phone No no longer required. 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Renamed from SPL005 
 * 							defect 9085 Ver Special Plates
  * K Harrell	06/26/2007	Do not enable Exp Mo in CORREG if Apprehended
 * 							rename isCORREG() to isRegCorrection()
 * 							modify isRegCorrection()
 * 						    defect 9085 Ver Special Plates
 * J Rue/		07/02/2007	Do not set actionListener for combo box
 * Kathy H.					modify populateOrganizationName()
 * 							defect 9086 Ver Special Plates
 * J Rue		08/02/2007	Set InvItmYr
 * 							modify setExpMoInvItmYrToDisplay()
 * 							defect 9086 Ver Special Plates 
 * J Rue		08/02/2007	Check RegExpYr = InvItmYr
 * 							modify validateExpMoYr()
 * 							defect 9086 Ver Special Plates
 * K Harrell	08/06/2007	Enable ResComptCntyNo when first shown.
 * 							modify setResComptCntyNoToDisplay()
 * 							defect 9085 Ver Special Plates
 * K Harrell	11/05/2007	Add ISA checkbox.  Default to checked, 
 * 							disabled for Disabled Persons Private Bus
 * 							Plate. 
 * 							add ivjchkISA,ivjstcLblMfgPlateNo,
 * 							 ivjtxtMfgPlateNo
 * 							add getchkISA(), isInitialSPSpecification(),
 * 							 setISAToDisplay(), 
 * 							 setMfgPlateNoToDisplay()  
 * 							modify initialize(), populateOrganizationName(),
 * 							 setData(), setDataToDataObject(), 
 * 							 setAddlSetToDisplay(),
 * 							 setExpMoInvItmYrToDisplay(),
 * 							 setChrgSpclPltFeeToDisplay(),
 * 							 setResComptCntyNoToDisplay() 
 * 							defect 9389 Ver Special Plates 2
 * K Harrell	12/04/2007	Comment Cleanup. Show Charge Special Plate
 * 							 Fee as selected even on 1st presentation.
 * 							add MAX_CNTY_NO
 * 							add validateCountyNo()
 * 							add releaseVirtualInventoryIfHeld()
 * 							modify releaseVirtualInventory(), 
 * 							  setChrgSpclPltFeeToDisplay(), 
 * 							  validateFields(), setAddlSetToDisplay() 
 * 							defect 9460 Ver Special Plates 2    
 * B Hargrove	07/15/2008	Cannot Title with expired Vendor Plate.
 * 							modify validateExpMoYr()
 * 							defect 9689 Ver MyPlates_POS 
 * K Harrell	01/07/2009 Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 * 							refactored ciOrigRegExpMo/Yr to 
 * 							  ciOrigPltExpMo/Yr
 *        					modify setExpMoInvItmYrToDisplay(), 
 * 							 setDataToDataObject()  
 *        					defect 9864 Ver Defect_POS_D
  * K Harrell	07/22/2009	Disable OwnerId Label, Input field if not 
 * 							HQ.  Add constants for mnemonics. 
 * 							Implement new RTSInputField.isValidCounty() 
 * 							delete MAX_CNTY_NO 
 * 							modify setOwnerIdToDisplay(), 
 * 							  validateCountyNo()
 * 							defect 10130 Ver Defect_POS_F  	
 * K Harrell	09/03/2009	Set OrigPltExpMo/Yr 
 * 							modify setDataToDataObject()
 * 							defect 10097 Ver Defect_POS_F 	 
 * K Harrell	07/31/2009	Disable OwnerId Label, Input field if not 
 * 							HQ.  Add constants for mnemonics.  
 * 							modify getchkAddlSet(), getchkChrgSpclPltFee(), 
 * 							  getstcLblEMail(), getstcLblExpires(), 
 * 							  getstcLblOrganization(), getstcLblOwnerId(),
 * 							  getstcLblPhoneNo(),getstcLblResComptCntyNo(),
 * 							  setOwnerIdToDisplay(), setData()
 * 							defect 10130 Ver Defect_POS_E'  
 * K Harrell	04/16/2010	Add selection of Plate ValidityTerm 
 * 							add ivjstcLblTerm, ivjcomboTerm, get methods
 * 							add setTermToDisplay(), validateTerm()  
 * 							modify setData, setDataToDataObject(), 
 * 							  validateFields(), validateExpMoYr(), 
 * 							  getRTSDialogBoxContentPane()
 * 							defect 10458 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * Frame for gathering necessary data to process transaction involving
 * new Special Plate Regis Data during Mainframe Down situations.
 *
 * @version	POS_640 		04/16/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		04/12/2007  19:43:00
 */
public class FrmSpecialPlateMFRecordNotAvailableSPL004
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkAddlSet = null;
	private JCheckBox ivjchkChrgSpclPltFee = null;
	private JCheckBox ivjchkISA = null;
	private JComboBox ivjcomboOrganization = null;
	private JComboBox ivjcomboPlateType = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblEMail = null;
	private JLabel ivjstcLblExpires = null;
	private JLabel ivjstcLblMfgPlateNo = null;
	private JLabel ivjstcLblOrganization = null;
	private JLabel ivjstcLblOwnerId = null;
	private JLabel ivjstcLblPhoneNo = null;
	private JLabel ivjstcLblPlateAge = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblPlateType = null;
	private JLabel ivjstcLblResComptCntyNo = null;
	private JLabel ivjstcLblSlash = null;

	private RTSInputField ivjtxtEMail = null;
	private RTSInputField ivjtxtExpMo = null;
	private RTSInputField ivjtxtExpYr = null;
	private RTSInputField ivjtxtInvItmYr = null;
	private JTextField ivjtxtMfgPlateNo = null;
	private RTSInputField ivjtxtOwnerId = null;
	private RTSPhoneField ivjtxtPhoneNo = null;
	private RTSInputField ivjtxtPlateAge = null;
	private RTSInputField ivjtxtPlateNo = null;
	private RTSInputField ivjtxtResComptCntyNo = null;

	// defect 10458
	private JLabel ivjstcLblTerm = null;
	private JComboBox ivjcomboTerm = null;
	// end defect 10458

	// int
	private int ciOrigPltExpMo = 0;
	private int ciOrigPltExpYr = 0;
	private int ciPrevConfirmedCntyNo = 0;

	// String 
	private String csTransCd;

	// Vector 
	private Vector cvOrgNo = new Vector();

	// Object
	private PlateTypeData caPltTypeData = null;
	private SpecialPlatesRegisData caSpclPltRegisData;
	private VehicleInquiryData caVehInqData = new VehicleInquiryData();

	// Constants 
	private static final int EIGHTEEN_EIGHTY = 1880;
	private final static String ERRMSG_DATA_MISS =
		"Data missing for IsNextVCREG029";
	private final static String ERRMSG_ERROR = "ERROR";
	private static final int MAX_PLT_AGE = 7;

	/**
	 * FrmSpecialPlateMFRecordNotAvailableSPL004 constructor
	 */
	public FrmSpecialPlateMFRecordNotAvailableSPL004()
	{
		super();
		initialize();
	}

	/**
	 * FrmSpecialPlateMFRecordNotAvailableSPL004 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSpecialPlateMFRecordNotAvailableSPL004(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmSpecialPlateMFRecordNotAvailableSPL004 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSpecialPlateMFRecordNotAvailableSPL004(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param ActionEvent aaAE
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

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateFields() && confirmCntyRes())
				{
					setDataToDataObject();

					getController().processData(
						AbstractViewController.ENTER,
						caVehInqData);
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				releaseVirtualInventoryIfHeld();

				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
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
	 * If HQ || Region, verify Resident County. 
	 * 
	 * If not, set focus to county resident number.
	 * 
	 * Do not check again if already confirmed. 
	 * 
	 * @return boolean
	 */
	private boolean confirmCntyRes()
	{
		boolean lbReturn = true;

		if (!SystemProperty.isCounty())
		{
			int liCurrCnty =
				Integer.parseInt(
					gettxtResComptCntyNo().getText().trim());

			if (liCurrCnty != ciPrevConfirmedCntyNo)
			{
				OfficeIdsData laOfcData =
					OfficeIdsCache.getOfcId(liCurrCnty);

				FrmCountyConfirmCTL002 laCntyCnfrm =
					new FrmCountyConfirmCTL002(
						getController().getMediator().getDesktop(),
						new Integer(liCurrCnty).toString(),
						laOfcData.getOfcName());

				if (laCntyCnfrm.displayWindow()
					== FrmCountyConfirmCTL002.NO)
				{
					lbReturn = false;
				}
				else
				{
					ciPrevConfirmedCntyNo = liCurrCnty;
				}
			}
		}
		return lbReturn;
	}

	/**
	 * Return the ECH property value.
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
				ivjButtonPanel1.setBounds(115, 283, 273, 42);
				ivjButtonPanel1.setName("ButtonPanel1");
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
	 * This method initializes ivjchkAddlSet
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkAddlSet()
	{
		if (ivjchkAddlSet == null)
		{
			ivjchkAddlSet = new JCheckBox();
			ivjchkAddlSet.setSize(107, 21);
			ivjchkAddlSet.setText("Additional Set");
			ivjchkAddlSet.setMnemonic(KeyEvent.VK_A);
			ivjchkAddlSet.setLocation(326, 225);
		}
		return ivjchkAddlSet;
	}

	/**
	 * This method initializes ivjchkChrgSpclPltFee
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkChrgSpclPltFee()
	{
		if (ivjchkChrgSpclPltFee == null)
		{
			ivjchkChrgSpclPltFee = new JCheckBox();
			ivjchkChrgSpclPltFee.setSize(166, 21);
			ivjchkChrgSpclPltFee.setText("Charge Special Plate Fee");
			ivjchkChrgSpclPltFee.setMnemonic(KeyEvent.VK_G);
			ivjchkChrgSpclPltFee.setLocation(326, 250);
		}
		return ivjchkChrgSpclPltFee;
	}

	/**
	 * This method initializes ivjchkISA
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkISA()
	{
		if (ivjchkISA == null)
		{
			ivjchkISA = new javax.swing.JCheckBox();
			ivjchkISA.setSize(51, 21);
			ivjchkISA.setText("ISA");
			ivjchkISA.setMnemonic(KeyEvent.VK_I);
			ivjchkISA.setLocation(419, 99);
		}
		return ivjchkISA;
	}

	/**
	 * This method initializes ivjcomboOrganization
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboOrganizationName()
	{
		if (ivjcomboOrganization == null)
		{
			ivjcomboOrganization = new JComboBox();
			ivjcomboOrganization.setSize(222, 20);
			ivjcomboOrganization.setLocation(148, 125);
		}
		return ivjcomboOrganization;
	}

	/**
	 * This method initializes ivjcomboPlateType
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboPlateType()
	{
		if (ivjcomboPlateType == null)
		{
			ivjcomboPlateType = new JComboBox();
			ivjcomboPlateType.setSize(222, 20);
			ivjcomboPlateType.setLocation(148, 100);
		}
		return ivjcomboPlateType;
	}

	/**	
	 * This method initializes ivjcomboTerm
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboTerm()
	{
		if (ivjcomboTerm == null)
		{
			ivjcomboTerm = new JComboBox();
			ivjcomboTerm.setSize(45, 20);
			ivjcomboTerm.setLocation(293, 150);
		}
		return ivjcomboTerm;
	}

	/**
	 * Get the Organization Name, Organization Number from the combo box
	 * 
	 * @return  String
	 */
	private String getOrgNoFromDropDown(int aiIndex)
	{
		String lsOrgNo = (String) cvOrgNo.elementAt(aiIndex);
		return lsOrgNo.substring(50).trim();
	}

	/**
	 * Return the stcLbl property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				ivjRTSDialogBoxContentPane.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				// user code begin {1}
				ivjRTSDialogBoxContentPane.add(
					getstcLblPlateNo(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblPlateType(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblOrganization(),
					null);
				ivjRTSDialogBoxContentPane.add(gettxtPlateNo(), null);
				ivjRTSDialogBoxContentPane.add(
					getcomboPlateType(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getcomboOrganizationName(),
					null);
				ivjRTSDialogBoxContentPane.add(gettxtInvItmYr(), null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblExpires(),
					null);
				ivjRTSDialogBoxContentPane.add(gettxtExpMo(), null);
				ivjRTSDialogBoxContentPane.add(getstcLblSlash(), null);
				ivjRTSDialogBoxContentPane.add(gettxtExpYr(), null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblPlateAge(),
					null);
				ivjRTSDialogBoxContentPane.add(gettxtPlateAge(), null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblPhoneNo(),
					null);
				ivjRTSDialogBoxContentPane.add(gettxtPhoneNo(), null);
				ivjRTSDialogBoxContentPane.add(getstcLblEMail(), null);
				ivjRTSDialogBoxContentPane.add(gettxtEMail(), null);
				ivjRTSDialogBoxContentPane.add(getchkAddlSet(), null);
				ivjRTSDialogBoxContentPane.add(
					getchkChrgSpclPltFee(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblResComptCntyNo(),
					null);
				ivjRTSDialogBoxContentPane.add(
					gettxtResComptCntyNo(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblOwnerId(),
					null);
				ivjRTSDialogBoxContentPane.add(gettxtOwnerId(), null);
				ivjRTSDialogBoxContentPane.add(getchkISA(), null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblMfgPlateNo(),
					null);
				ivjRTSDialogBoxContentPane.add(
					gettxtMfgPlateNo(),
					null);

				// defect 10458 
				ivjRTSDialogBoxContentPane.add(getstcLblTerm(), null);
				ivjRTSDialogBoxContentPane.add(getcomboTerm(), null);
				// end defect 10458 
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * This method initializes ivjstcLblEMail
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEMail()
	{
		if (ivjstcLblEMail == null)
		{
			ivjstcLblEMail = new JLabel();
			ivjstcLblEMail.setSize(46, 20);
			ivjstcLblEMail.setText("E-Mail:");
			ivjstcLblEMail.setDisplayedMnemonic(KeyEvent.VK_M);
			ivjstcLblEMail.setLabelFor(gettxtEMail());
			ivjstcLblEMail.setLocation(96, 175);
		}
		return ivjstcLblEMail;
	}

	/**
	 * This method initializes ivjstcLblExpires
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblExpires()
	{
		if (ivjstcLblExpires == null)
		{
			ivjstcLblExpires = new JLabel();
			ivjstcLblExpires.setSize(46, 20);
			ivjstcLblExpires.setText("Expires:");
			ivjstcLblExpires.setDisplayedMnemonic(KeyEvent.VK_X);
			ivjstcLblExpires.setLabelFor(gettxtExpMo());
			ivjstcLblExpires.setLocation(96, 150);
		}
		return ivjstcLblExpires;
	}

	/**
	 * This method initializes ivjstcLblMfgPlateNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMfgPlateNo()
	{
		if (ivjstcLblMfgPlateNo == null)
		{
			ivjstcLblMfgPlateNo = new javax.swing.JLabel();
			ivjstcLblMfgPlateNo.setSize(74, 20);
			ivjstcLblMfgPlateNo.setText("Mfg Plate No:");
			ivjstcLblMfgPlateNo.setLocation(66, 50);
		}
		return ivjstcLblMfgPlateNo;
	}

	/**
	 * This method initializes ivjstcLblOrganization
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOrganization()
	{
		if (ivjstcLblOrganization == null)
		{
			ivjstcLblOrganization = new JLabel();
			ivjstcLblOrganization.setSize(75, 20);
			ivjstcLblOrganization.setDisplayedMnemonic(KeyEvent.VK_Z);
			ivjstcLblOrganization.setText("Organization:");
			ivjstcLblOrganization.setLabelFor(
				getcomboOrganizationName());
			ivjstcLblOrganization.setLocation(67, 125);
		}
		return ivjstcLblOrganization;
	}

	/**
	 * Return the ivjstcLblOwnerId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerId()
	{
		if (ivjstcLblOwnerId == null)
		{
			try
			{
				ivjstcLblOwnerId = new javax.swing.JLabel();
				ivjstcLblOwnerId.setSize(56, 20);
				ivjstcLblOwnerId.setName("ivjstcLblOwnerId");
				ivjstcLblOwnerId.setText("Owner Id:");
				ivjstcLblOwnerId.setDisplayedMnemonic(KeyEvent.VK_O);
				ivjstcLblOwnerId.setLabelFor(gettxtOwnerId());
				ivjstcLblOwnerId.setLocation(314, 25);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblOwnerId;

	}

	/**
	 * This method initializes ivjstcLblPhoneNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPhoneNo()
	{
		if (ivjstcLblPhoneNo == null)
		{
			ivjstcLblPhoneNo = new JLabel();
			ivjstcLblPhoneNo.setSize(57, 20);
			ivjstcLblPhoneNo.setText("Phone No:");
			ivjstcLblPhoneNo.setDisplayedMnemonic(KeyEvent.VK_P);
			ivjstcLblPhoneNo.setLabelFor(gettxtPhoneNo());
			ivjstcLblPhoneNo.setLocation(85, 200);
		}
		return ivjstcLblPhoneNo;
	}

	/**
	 * This method initializes ivjstcLblPlateAge
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateAge()
	{
		if (ivjstcLblPlateAge == null)
		{
			ivjstcLblPlateAge = new JLabel();
			ivjstcLblPlateAge.setSize(60, 20);
			ivjstcLblPlateAge.setText("Plate Age: ");
			ivjstcLblPlateAge.setLocation(82, 75);
			// user code begin {1}
			ivjstcLblPlateAge.setLabelFor(gettxtPlateAge());
			// user code end {1} 
		}
		return ivjstcLblPlateAge;
	}

	/**
	 * This method initializes ivjstcLblPlateNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateNo()
	{
		if (ivjstcLblPlateNo == null)
		{
			ivjstcLblPlateNo = new JLabel();
			ivjstcLblPlateNo.setSize(53, 20);
			ivjstcLblPlateNo.setText("Plate No: ");
			ivjstcLblPlateNo.setLocation(89, 25);
		}
		return ivjstcLblPlateNo;
	}

	/**
	 * This method initializes ivjstcLblPlateType
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateType()
	{
		if (ivjstcLblPlateType == null)
		{
			ivjstcLblPlateType = new JLabel();
			ivjstcLblPlateType.setSize(65, 20);
			ivjstcLblPlateType.setText("Plate Type: ");
			ivjstcLblPlateType.setLocation(77, 100);
		}
		return ivjstcLblPlateType;
	}

	/**
	 * This method initializes ivjstcLblResComptCntyNo
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblResComptCntyNo()
	{
		if (ivjstcLblResComptCntyNo == null)
		{
			ivjstcLblResComptCntyNo = new javax.swing.JLabel();
			ivjstcLblResComptCntyNo.setSize(121, 20);
			ivjstcLblResComptCntyNo.setText("County Of Residence:");
			ivjstcLblResComptCntyNo.setDisplayedMnemonic(KeyEvent.VK_C);
			ivjstcLblResComptCntyNo.setLabelFor(gettxtResComptCntyNo());
			ivjstcLblResComptCntyNo.setLocation(21, 225);
		}
		return ivjstcLblResComptCntyNo;
	}

	/**
	 * This method initializes ivjstcLblSlash
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSlash()
	{
		if (ivjstcLblSlash == null)
		{
			ivjstcLblSlash = new JLabel();
			ivjstcLblSlash.setBounds(174, 150, 10, 20);
			ivjstcLblSlash.setText("/");
		}
		return ivjstcLblSlash;
	}

	/**
	 * This method initializes ivjstcLblTerm
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTerm()
	{
		if (ivjstcLblTerm == null)
		{
			ivjstcLblTerm = new JLabel();
			ivjstcLblTerm.setBounds(249, 150, 37, 20);
			ivjstcLblTerm.setText("Term:");
			ivjstcLblTerm.setDisplayedMnemonic(KeyEvent.VK_T);
			ivjstcLblTerm.setLabelFor(getcomboTerm());
		}
		return ivjstcLblTerm;
	}

	/**
	 * This method initializes ivjtxtEMail
	 * 
	 * @return JTextField
	 */
	private RTSInputField gettxtEMail()
	{
		if (ivjtxtEMail == null)
		{
			ivjtxtEMail = new RTSInputField();
			ivjtxtEMail.setSize(249, 20);
			ivjtxtEMail.setText(" ");
			ivjtxtEMail.setInput(RTSInputField.DEFAULT);
			ivjtxtEMail.setMaxLength(50);
			ivjtxtEMail.setLocation(148, 175);
		}
		return ivjtxtEMail;
	}

	/**
	 * This method initializes ivjtxtExpMo
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtExpMo()
	{
		if (ivjtxtExpMo == null)
		{
			ivjtxtExpMo = new RTSInputField();
			ivjtxtExpMo.setSize(22, 20);
			ivjtxtExpMo.setMaxLength(2);
			ivjtxtExpMo.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtExpMo.setText(CommonConstant.STR_SPACE_EMPTY);
			ivjtxtExpMo.setLocation(148, 150);
		}
		return ivjtxtExpMo;
	}

	/**
	 * This method initializes ivjtxtExpYr
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtExpYr()
	{
		if (ivjtxtExpYr == null)
		{
			ivjtxtExpYr = new RTSInputField();
			ivjtxtExpYr.setSize(36, 20);
			ivjtxtExpYr.setMaxLength(4);
			ivjtxtExpYr.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtExpYr.setLocation(189, 150);
		}
		return ivjtxtExpYr;
	}

	/**
	 * This method initializes ivjtxtInvItmYr
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtInvItmYr()
	{
		if (ivjtxtInvItmYr == null)
		{
			ivjtxtInvItmYr = new RTSInputField();
			ivjtxtInvItmYr.setSize(36, 20);
			ivjtxtInvItmYr.setMaxLength(4);
			ivjtxtInvItmYr.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtInvItmYr.setText(CommonConstant.STR_SPACE_EMPTY);
			ivjtxtInvItmYr.setLocation(374, 100);
		}
		return ivjtxtInvItmYr;
	}

	/**
	 * This method initializes ivjtxtMfgPlateNo
	 * 
	 * @return JTextField
	 */
	private JTextField gettxtMfgPlateNo()
	{
		if (ivjtxtMfgPlateNo == null)
		{
			ivjtxtMfgPlateNo = new JTextField();
			ivjtxtMfgPlateNo.setSize(75, 20);
			ivjtxtMfgPlateNo.setLocation(148, 50);
			ivjtxtMfgPlateNo.setText("");
		}
		return ivjtxtMfgPlateNo;
	}

	/**
	 * This method initializes ivjtxtOwnrId
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerId()
	{
		if (ivjtxtOwnerId == null)
		{
			ivjtxtOwnerId = new RTSInputField();
			ivjtxtOwnerId.setSize(75, 20);
			ivjtxtOwnerId.setMaxLength(9);
			ivjtxtOwnerId.setLocation(374, 25);
		}
		return ivjtxtOwnerId;
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
			ivjtxtPhoneNo.setSize(87, 20);
			ivjtxtPhoneNo.setText("   -   -");
			ivjtxtPhoneNo.setLocation(148, 200);
		}
		return ivjtxtPhoneNo;
	}

	/**
	 * This method initializes ivjtxtPlateAge
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPlateAge()
	{
		if (ivjtxtPlateAge == null)
		{
			ivjtxtPlateAge = new RTSInputField();
			ivjtxtPlateAge.setSize(22, 20);
			ivjtxtPlateAge.setMaxLength(2);
			ivjtxtPlateAge.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtPlateAge.setLocation(148, 75);
			ivjtxtPlateAge.setText(CommonConstant.STR_SPACE_EMPTY);
		}
		return ivjtxtPlateAge;
	}

	/**
	 * This method initializes ivjtxtPlateNo
	 * 
	 * @return JTextField
	 */
	private RTSInputField gettxtPlateNo()
	{
		if (ivjtxtPlateNo == null)
		{
			ivjtxtPlateNo = new RTSInputField();
			ivjtxtPlateNo.setBounds(148, 25, 75, 20);
			ivjtxtPlateNo.setMaxLength(7);
		}
		return ivjtxtPlateNo;
	}

	/**
	 * This method initializes ivjtxtResComptCntyNo
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtResComptCntyNo()
	{
		if (ivjtxtResComptCntyNo == null)
		{
			ivjtxtResComptCntyNo = new RTSInputField();
			ivjtxtResComptCntyNo.setSize(26, 20);
			ivjtxtResComptCntyNo.setInput(RTSInputField.NUMERIC_ONLY);
			ivjtxtResComptCntyNo.setMaxLength(3);
			ivjtxtResComptCntyNo.setText("");
			ivjtxtResComptCntyNo.setLocation(148, 225);
			ivjtxtResComptCntyNo.setPreferredSize(
				new java.awt.Dimension(25, 20));
		}
		return ivjtxtResComptCntyNo;
	}

	/**
	 * Called whenever an exception is thrown
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
	 * This method initializes the class
	 */
	private void initialize()
	{
		try
		{
			this.setContentPane(getRTSDialogBoxContentPane());
			setTitle("Special Plate Mainframe Record Not Available   SPL004");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(512, 363);
			setRequestFocus(false);
			// end defect 10130
		}
		catch (Throwable aeEx)
		{
			handleException(aeEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Return boolean to denote if this is the initial Special Plates 
	 * Specification
	 * 
	 * @return boolean 
	 */
	private boolean isInitialSPSpecification()
	{
		return (!caSpclPltRegisData.isMFDownSP());

	}

	/**
	 * Validates that the expiration year input is between 1880 and 
	 * current year + 5.
	 *
	 * @return boolean
	 */
	public boolean isInvalidExpYear()
	{
		String lsExpYear = gettxtExpYr().getText().trim();
		boolean lbRetVal = false;
		int liExpYear = Integer.parseInt(lsExpYear);

		if (liExpYear <= EIGHTEEN_EIGHTY
			|| liExpYear > (RTSDate.getCurrentDate().getYear() + 5))
		{
			lbRetVal = true;
		}
		return lbRetVal;
	}

	/**
	 * is Registration Correction
	 *
	 * @return boolean 
	 */
	private boolean isRegCorrection()
	{
		boolean lbRegCorrection = false;

		if (csTransCd.equals(TransCdConstant.CORREG))
		{
			RegistrationValidationData laRegValidData =
				new RegistrationValidationData();

			Object laObject = caVehInqData.getValidationObject();

			if (laObject != null
				&& laObject instanceof RegistrationValidationData)
			{
				laRegValidData = (RegistrationValidationData) laObject;

				if (laRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_REG)
				{
					lbRegCorrection = true;
				}
			}
		}
		return lbRegCorrection;
	}

	/**
	 * Populate Organization Name 
	 */
	private void populateOrganizationName()
	{
		getcomboOrganizationName().removeAllItems();
		getcomboOrganizationName().setSelectedIndex(-1);
		// Add 2nd parameter to indicate want All Plates
		Vector lvOrgNo =
			OrganizationNumberCache.getOrgsPerPlt(
				caSpclPltRegisData.getRegPltCd(),
				false);
		String lsSelectedOrgNo = caSpclPltRegisData.getOrgNo();
		if (lvOrgNo.size() != 0)
		{
			cvOrgNo = new Vector();
			for (int i = 0; i < lvOrgNo.size(); i++)
			{
				OrganizationNumberData laOrgNoData =
					(OrganizationNumberData) lvOrgNo.elementAt(i);
				String lsOrgName =
					UtilityMethods.addPaddingRight(
						laOrgNoData.getOrgNoDesc(),
						50,
						" ")
						+ laOrgNoData.getOrgNo();
				cvOrgNo.add(i, lsOrgName);
			}
			UtilityMethods.sort(cvOrgNo);
			int liIndex = -1;
			for (int i = 0; i < lvOrgNo.size(); i++)
			{
				String lsOrgNo = (String) cvOrgNo.elementAt(i);
				getcomboOrganizationName().addItem(
					lsOrgNo.substring(0, 50).trim());
				if (lsSelectedOrgNo
					.equals(lsOrgNo.substring(50).trim()))
				{
					liIndex = i;
				}
			}
			comboBoxHotKeyFix(getcomboOrganizationName());
			getcomboOrganizationName().setSelectedIndex(liIndex);
			// defect 9389 
			if (!isInitialSPSpecification() || lvOrgNo.size() == 1)
			{
				getcomboOrganizationName().setEnabled(false);
			}
			else
			{
				getcomboOrganizationName().setEnabled(true);
			}
			// end defect 9389 
		}
	}

	/**
	 * Release Virtual Inventory if Held 
	 */
	public void releaseVirtualInventoryIfHeld()
	{
		if (caSpclPltRegisData.getVIAllocData() != null)
		{
			// defect 9460 
			// Release in Replacement iff manufacturing new 
			//  InvItmNo in Replacement 
			if (getController()
				.getTransCode()
				.equals(TransCdConstant.REPL)
				&& !caSpclPltRegisData
					.getVIAllocData()
					.getInvItmNo()
					.equals(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegPltNo()))
			{
				getController().processData(
					InventoryConstant.INV_VI_RELEASE_HOLD,
					caSpclPltRegisData.getVIAllocData());
			}
			// end defect 9460 
		}
	}

	/**
	 * Enable/Disable, select/deselect Additional Set checkbox 
	 * as appropriate 
	 */
	private void setAddlSetToDisplay()
	{
		switch (caPltTypeData.getPltSetImprtnceCd())
		{
			// defect 9460 
			// Use SpecialPlatesConstants
			case SpecialPlatesConstant.PLTSETIMPRTNCECD_NOT_IMPORTANT :
				{
					getchkAddlSet().setEnabled(false);
					getchkAddlSet().setSelected(false);
					break;
				}
			case SpecialPlatesConstant
				.PLTSETIMPRTNCECD_FIRST_SET_ONLY :
				{
					getchkAddlSet().setEnabled(false);
					getchkAddlSet().setSelected(false);
					break;
				}
			case SpecialPlatesConstant.PLTSETIMPRTNCECD_ADDL_SET_ONLY :
				{
					getchkAddlSet().setEnabled(false);
					getchkAddlSet().setSelected(true);
					break;
				}
			case SpecialPlatesConstant
				.PLTSETIMPRTNCECD_EITHER_FIRST_OR_ADDL_SET :
				{
					// defect 9389 
					getchkAddlSet().setEnabled(
						isInitialSPSpecification());
					// end defect 9389 
					getchkAddlSet().setSelected(
						caSpclPltRegisData.getAddlSetIndi() == 1);
					break;
				}
				// end defect 9460 
		}
	}

	/**
	 * Set Charge Special Plate Fee checkbox to display
	 */
	private void setChrgSpclPltFeeToDisplay()
	{
		// defect 9460
		// Disabled if HQ && SpclPrortnIncrmnt == 0 
		if (SystemProperty.isHQ()
			|| caPltTypeData.getSpclPrortnIncrmnt() == 0)
		{
			getchkChrgSpclPltFee().setEnabled(false);
			getchkChrgSpclPltFee().setSelected(false);
		}
		else if (
			csTransCd.equals(TransCdConstant.RENEW)
				|| csTransCd.equals(TransCdConstant.TITLE)
				|| csTransCd.equals(TransCdConstant.EXCH)
				|| csTransCd.equals(TransCdConstant.NONTTL)
				|| csTransCd.equals(TransCdConstant.REJCOR))
		{
			getchkChrgSpclPltFee().setEnabled(true);
			getchkChrgSpclPltFee().setSelected(
				!caSpclPltRegisData.isResetChrgFee());
		}
		else
		{
			getchkChrgSpclPltFee().setEnabled(false);
			getchkChrgSpclPltFee().setSelected(false);
		}
		// end defect 9460
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject instanceof Vector)
		{
			Vector lvIsNextVCREG029 = (Vector) aaDataObject;
			if (lvIsNextVCREG029 != null)
			{
				if (lvIsNextVCREG029.size() == 2)
				{
					// determine next vc if NOT reg029
					// first element is flag whether to go to REG029
					if (lvIsNextVCREG029.get(0) instanceof Boolean)
					{
						getController().processData(
							VCSpecialPlateMFRecordNotAvailableSPL004
								.REDIRECT_IS_NEXT_VC_REG029,
							lvIsNextVCREG029);
					}
					else if (lvIsNextVCREG029.get(0) instanceof String)
					{
						getController().processData(
							VCSpecialPlateMFRecordNotAvailableSPL004
								.REDIRECT_NEXT_VC,
							lvIsNextVCREG029);
					}
				}
				else
				{
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						ERRMSG_DATA_MISS,
						ERRMSG_ERROR).displayError(
						this);
				}
			}
		}
		else if (
			aaDataObject != null
				&& aaDataObject instanceof VehicleInquiryData)
		{
			csTransCd = getController().getTransCode();
			caVehInqData = (VehicleInquiryData) aaDataObject;
			caSpclPltRegisData =
				caVehInqData.getMfVehicleData().getSpclPltRegisData();
			caSpclPltRegisData.setTransCd(
				getController().getTransCode());
			caSpclPltRegisData.initWhereNull();
			// Plate No 
			gettxtPlateNo().setEnabled(false);
			gettxtPlateNo().setText(
				caSpclPltRegisData.getRegPltNo().trim());
			ItemCodesData laItemCodeData =
				ItemCodesCache.getItmCd(
					caSpclPltRegisData.getRegPltCd());
			String lsPlateTypeDesc = laItemCodeData.getItmCdDesc();
			caPltTypeData =
				PlateTypeCache.getPlateType(
					caSpclPltRegisData.getRegPltCd());
			getcomboPlateType().addItem(lsPlateTypeDesc.trim());
			getcomboPlateType().setEnabled(false);
			setExpMoInvItmYrToDisplay();
			populateOrganizationName();
			setAddlSetToDisplay();
			// defect 9389 
			setMfgPlateNoToDisplay();
			setISAToDisplay();
			// end defect 9389  
			setOwnerIdToDisplay();
			setResComptCntyNoToDisplay();
			setEmailPhoneToDisplay();
			setChrgSpclPltFeeToDisplay();
			// defect 10458 
			setTermToDisplay();
			// end defect 10458 
		}

	}

	/**
	 * Set data for return  
	 */
	private void setDataToDataObject()
	{
		// PlateNo, RegPltCd already Set

		// InvItmNo 
		if (gettxtInvItmYr().isEnabled())
		{
			caSpclPltRegisData.setInvItmYr(
				Integer.parseInt(gettxtInvItmYr().getText()));
		}
		// Owner Id
		// defect VE fix
//			caSpclPltRegisData.getOwnrData().setOwnrId(
//					gettxtOwnerId().getText());
			OwnerData laOwnerData = caSpclPltRegisData.getOwnrData();
			laOwnerData.setOwnrId(gettxtOwnerId().getText());
			caSpclPltRegisData.setRegPltAge(
			Integer.parseInt(gettxtPlateAge().getText()));

		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
		// Plt Exp Mo 
		caSpclPltRegisData.setPltExpMo(
			Integer.parseInt(gettxtExpMo().getText()));

		// Plt Exp Yr
		caSpclPltRegisData.setPltExpYr(
			Integer.parseInt(gettxtExpYr().getText()));
		// end defect 9864 

		// defect 10097 
		caSpclPltRegisData.setOrigPltExpMo(
			Integer.parseInt(gettxtExpMo().getText()));

		// Plt Exp Yr
		caSpclPltRegisData.setOrigPltExpYr(
			Integer.parseInt(gettxtExpYr().getText()));
		// end defect 10097 

		// County of Residence
		caSpclPltRegisData.setResComptCntyNo(
			Integer.parseInt(gettxtResComptCntyNo().getText()));

		// Phone 
		caSpclPltRegisData.setPltOwnrPhoneNo(
			gettxtPhoneNo().getPhoneNo());

		// E-Mail 
		caSpclPltRegisData.setPltOwnrEMail(gettxtEMail().getText());

		// AddlSetIndi 
		caSpclPltRegisData.setAddlSetIndi(
			getchkAddlSet().isSelected() ? 1 : 0);

		// defect 9389 
		// ISAIndi 
		caSpclPltRegisData.setISAIndi(getchkISA().isSelected() ? 1 : 0);

		// Note: MfgPltNo will be assigned if populated.  No need to 
		//  assign. Only in MFDown, Replacement 

		// Charge Fee Indicator 
		caSpclPltRegisData.setResetChrgFee(
			!getchkChrgSpclPltFee().isSelected());

		caSpclPltRegisData.setSpclPltChrgFeeIndi(
			getchkChrgSpclPltFee().isSelected() ? 1 : 0);

		caSpclPltRegisData.setMFDownSP(true);
		// end defect 9389 

		// defect 10458 
		int liTerm =
			((Integer) getcomboTerm()
				.getItemAt(getcomboTerm().getSelectedIndex()))
				.intValue();

		caSpclPltRegisData.setPltValidityTerm(liTerm);
		// end defect 10458 
	}

	/***
	 * Set EMail Address and Phone No 
	 */
	private void setEmailPhoneToDisplay()
	{
		gettxtEMail().setText(
			caSpclPltRegisData.getPltOwnrEMail().trim());
		gettxtPhoneNo().setPhoneNo(
			caSpclPltRegisData.getPltOwnrPhoneNo());
	}

	/**
	 * Set InvItmYr
	 */
	private void setExpMoInvItmYrToDisplay()
	{
		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
		ciOrigPltExpMo = caSpclPltRegisData.getPltExpMo();
		ciOrigPltExpYr = caSpclPltRegisData.getPltExpYr();

		gettxtExpMo().setText(
			UtilityMethods.isAllZeros(
				CommonConstant.STR_SPACE_EMPTY
					+ caSpclPltRegisData.getPltExpMo())
				? CommonConstant.STR_SPACE_EMPTY
				: CommonConstant.STR_SPACE_EMPTY
					+ caSpclPltRegisData.getPltExpMo());

		gettxtExpYr().setText(
			UtilityMethods.isAllZeros(
				CommonConstant.STR_SPACE_EMPTY
					+ caSpclPltRegisData.getPltExpYr())
				? CommonConstant.STR_SPACE_EMPTY
				: CommonConstant.STR_SPACE_EMPTY
					+ caSpclPltRegisData.getPltExpYr());
		// end defect 9864

		// Fixed Exp Mo
		if (gettxtExpMo().getText().trim().length() == 0)
		{
			getchkChrgSpclPltFee().setSelected(true);
			SpecialPlateFixedExpirationMonthData laSpclPltFxdExpMoData =
				SpecialPlateFixedExpirationMonthCache.getRegPltCd(
					caSpclPltRegisData.getRegPltCd());

			if (laSpclPltFxdExpMoData != null)
			{
				gettxtExpMo().setText(
					CommonConstant.STR_SPACE_EMPTY
						+ laSpclPltFxdExpMoData.getFxdExpMo());
				gettxtExpMo().setEnabled(false);
			}
		}
		else
		{
			gettxtPlateAge().setText(
				CommonConstant.STR_SPACE_EMPTY
					+ caSpclPltRegisData.getRegPltAge(false));
			gettxtPlateAge().setEnabled(false);
			getchkChrgSpclPltFee().setSelected(
				caSpclPltRegisData.getSpclPltChrgFeeIndi() == 1);
		}
		// defect 9389 
		boolean lbEnable = isInitialSPSpecification();
		// end defect 9389 

		gettxtExpMo().setEnabled(lbEnable);
		gettxtExpYr().setEnabled(lbEnable);

		// defetc 9086
		//	Set InvItmYr
		boolean lbAnnual = caPltTypeData.getAnnualPltIndi() == 1;
		gettxtInvItmYr().setVisible(lbAnnual);
		gettxtInvItmYr().setEnabled(lbAnnual && lbEnable);

		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr
		gettxtInvItmYr().setText(
			UtilityMethods.isAllZeros(
				CommonConstant.STR_SPACE_EMPTY
					+ caSpclPltRegisData.getPltExpYr())
				? CommonConstant.STR_SPACE_EMPTY
				: CommonConstant.STR_SPACE_EMPTY
					+ caSpclPltRegisData.getInvItmYr());
		// end defect 9086
		// end defect 9864 
	}

	/** 
	 * Set ISA to Display 
	 */
	private void setISAToDisplay()
	{
		if ((caPltTypeData
			.getISAAllowdCd()
			.equals(SpecialPlatesConstant.POS_EVENTS)
			|| (caPltTypeData
				.getISAAllowdCd()
				.equals(
					SpecialPlatesConstant.BOTH_POS_AND_ITRNT_EVENTS))))
		{
			boolean lbPLPDPPBP =
				caPltTypeData.getRegPltCd().equals(
					SpecialPlatesConstant.PER_DP_PRIVATE_BUS_PLT);

			getchkISA().setEnabled(
				!lbPLPDPPBP && isInitialSPSpecification());

			getchkISA().setSelected(
				lbPLPDPPBP || caSpclPltRegisData.getISAIndi() == 1);
		}
		else
		{
			getchkISA().setEnabled(false);
			getchkISA().setSelected(false);
		}
	}

	/***
	 * Set MfgPlateNo to Display 
	 */
	private void setMfgPlateNoToDisplay()
	{
		gettxtMfgPlateNo().setEnabled(false);

		if (caSpclPltRegisData.getMfgPltNo() != null
			&& caSpclPltRegisData.getMfgPltNo().trim().length() > 0)
		{
			gettxtMfgPlateNo().setText(
				caSpclPltRegisData.getMfgPltNo());
		}
	}

	/***
	 * Set OwnerId to Display 
	 */
	private void setOwnerIdToDisplay()
	{
		gettxtOwnerId().setText(
			caSpclPltRegisData.getOwnrData().getOwnrId().trim());

		// defect 10130
		boolean lbEnable =
			SystemProperty.isHQ()
				&& (isRegCorrection() || isInitialSPSpecification());

		getstcLblOwnerId().setEnabled(lbEnable);
		gettxtOwnerId().setEnabled(lbEnable);
		// end defect 10130 
	}

	/**
	 * Set ResComptCntyNo
	 */
	private void setResComptCntyNoToDisplay()
	{
		int liResComptCntyNo = caSpclPltRegisData.getResComptCntyNo();
		if (liResComptCntyNo == 0 && SystemProperty.isCounty())
		{
			liResComptCntyNo = SystemProperty.getOfficeIssuanceNo();
		}
		gettxtResComptCntyNo().setText(
			liResComptCntyNo == 0
				? CommonConstant.STR_SPACE_EMPTY
				: CommonConstant.STR_SPACE_EMPTY + liResComptCntyNo);

		// defect 9389 
		// ResComptCntyNo enabled iff !Cnty or 1st time through  
		gettxtResComptCntyNo().setEnabled(
			!SystemProperty.isCounty() || isInitialSPSpecification());
		// end defect 9389 
	}

	/**
	 * Set Term Label, Combo Box
	 *
	 */
	private void setTermToDisplay()
	{
		Vector lvValues = SpecialPlatesConstant.PLATE_VALIDITY_TERMS;

		boolean lbPreSelect =
			!caPltTypeData.isVendorPlt() || !isInitialSPSpecification();

		int liPreSelection =
			isInitialSPSpecification()
				? SpecialPlatesConstant.DEFAULT_SPCLPLT_VALIDITY_TERM
				: caSpclPltRegisData.getPltValidityTerm();

		if (lbPreSelect)
		{
			getcomboTerm().addItem(new Integer(liPreSelection));
			getcomboTerm().setEnabled(false);
		}
		else
		{
			for (int i = 0; i < lvValues.size(); i++)
			{
				Integer liTerm = (Integer) lvValues.elementAt(i);
				getcomboTerm().addItem(liTerm);
			}
			getcomboTerm().setSelectedIndex(-1);
			getcomboTerm().setEnabled(true);
		}
	}

	/**
	 * Validate County No 
	 * 
	 * @param aeRTSEx
	 */
	private void validateCountyNo(RTSException aeRTSEx)
	{
		// Only validate if enabled.
		// Note:  Will be modified to current county after display
		if (gettxtResComptCntyNo().isEnabled())
		{
			// defect 10130 
			if (gettxtResComptCntyNo().isEmpty()
				|| !gettxtResComptCntyNo().isValidCountyNo())
			{
				// end defect 10130

				// 150 
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtResComptCntyNo());
			}
		}
	}

	/**
	 *  Validate EMail address & Phone No
	 * 
	 *  @param aeRTSEx
	 */
	private void validateEMailPhone(RTSException aeRTSEx)
	{
		// Validate EMail Address if supplied
		if (!CommonValidations
			.isValidEMail(gettxtEMail().getText().trim()))
		{
			// 150 
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtEMail());
		}

		// Confirm valid Phone No if provided  
		if (!gettxtPhoneNo().isPhoneNoEmpty()
			&& !gettxtPhoneNo().isValidPhoneNo())
		{
			// 150 
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPhoneNo());
		}
	}

	/**
	 * Validate Exp Mo/Yr
	 * 
	 * @param aeRTSEx
	 */
	private void validateExpMoYr(RTSException aeRTSEx)
	{
		boolean lbMoReturn = true;
		boolean lbYrReturn = true;

		// defect 10458 
		// Remove exception coding for Vendor Plate 
		// 6.4.0 Now handles Exp Mo/Yr synch 
		//		// defect 9689
		//		boolean lbVendorPltReturn = true;
		//		// end defect 9689
		// end defect 10458 

		int liMo = 0;
		int liYr = 0;
		String lsMo = gettxtExpMo().getText().trim();
		if (lsMo.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			lbMoReturn = false;
		}
		else
		{
			try
			{
				liMo = Integer.parseInt(lsMo);
				if (liMo <= 0 || liMo > 12)
				{
					lbMoReturn = false;
					liMo = 0;
				}
				else
				{
					SpecialPlateFixedExpirationMonthData laSpclPltFxdExpMoData =
						SpecialPlateFixedExpirationMonthCache
							.getRegPltCd(
							caSpclPltRegisData.getRegPltCd());
					// Fixed Exp Mo  
					if (laSpclPltFxdExpMoData != null
						&& liMo != laSpclPltFxdExpMoData.getFxdExpMo())
					{
						lbMoReturn = false;
					}
				}
			}

			catch (NumberFormatException aeNumFEx)
			{
				lbMoReturn = false;
			}
		}
		String lsYr = gettxtExpYr().getText().trim();
		if (lsYr.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			liYr = 0;
			lbYrReturn = false;
		}
		else
		{
			try
			{
				liYr = Integer.parseInt(lsYr);
				if (caPltTypeData.getAnnualPltIndi() == 1)
				{
					String lsInvItmYear =
						gettxtInvItmYr().getText().trim();
					try
					{
						int liInvItmYr = Integer.parseInt(lsInvItmYear);
						if (liInvItmYr != liYr)
						{
							lbYrReturn = false;
						}
					}
					catch (NumberFormatException aeNumFEx)
					{
						lbYrReturn = false;
					}
				}
				// defect 9086
				//	Check RegExpYr = InvItmYr
				lbYrReturn =
					lbYrReturn
						&& (!isInvalidExpYear() && liYr >= ciOrigPltExpYr);
				// defect 9086
			}
			catch (NumberFormatException aeNumFEx)
			{
				lbYrReturn = false;
			}
		}

		// defect 10458 
		//		// defect 9689
		//		// Check for expired Vendor Plate
		//		if (PlateTypeCache
		//			.isVendorPlate(caSpclPltRegisData.getRegPltCd()))
		//		{
		//
		//			int liEffectiveExpDate = 0;
		//			int liEffectiveExpDatePlusOne = 0;
		//
		//			// For Renew, check expired as of new reg period.
		//			if (csTransCd.equals(TransCdConstant.RENEW))
		//			{
		//				if (liMo != 12)
		//				{
		//					liEffectiveExpDatePlusOne =
		//						(liYr * 10000) + ((liMo + 1) * 100) + 1;
		//				}
		//				else
		//				{
		//					liEffectiveExpDatePlusOne =
		//						((liYr + 1) * 10000) + 100 + 1;
		//				}
		//
		//				RTSDate laRTSEffExpDate =
		//					(
		//						new RTSDate(
		//							RTSDate.YYYYMMDD,
		//							liEffectiveExpDatePlusOne)).add(
		//						RTSDate.DATE,
		//						-1);
		//				liEffectiveExpDate = laRTSEffExpDate.getYYYYMMDDDate();
		//			}
		//			// If not Renew, check expired as of today
		//			else
		//			{
		//				liEffectiveExpDate = new RTSDate().getYYYYMMDDDate();
		//			}
		//
		//			if (CommonValidations
		//				.isPlateExpired(liMo, liYr, liEffectiveExpDate))
		//			{
		//				lbVendorPltReturn = false;
		//			}
		//		}
		//		// end defect 9689
		// end defect 10458 

		if (!lbMoReturn)
		{
			// 150 
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtExpMo());
		}
		if (!lbYrReturn)
		{
			// 150 
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtExpYr());
		}
		// defect 10458 
		//		// defect 9689
		//		if (!lbVendorPltReturn)
		//		{
		//			aeRTSEx.addException(
		//				new RTSException(
		//					ErrorsConstant.ERR_NUM_EXPIRED_VENDOR_PLATE),
		//				gettxtExpYr());
		//			aeRTSEx.addException(
		//				new RTSException(
		//					ErrorsConstant.ERR_NUM_EXPIRED_VENDOR_PLATE),
		//				gettxtExpMo());
		//		}
		//		// end defect 9689
		// end defect 10458 
	}

	/**
	 * Validate fields 
	 */
	private boolean validateFields()
	{
		RTSException leRTSEx = new RTSException();
		boolean lbValid = true;

		// Validate Plate No for selected Plate Type 
		validatePlate(leRTSEx);
		validateOrganizationName(leRTSEx);
		validateExpMoYr(leRTSEx);
		validateEMailPhone(leRTSEx);
		// defect 9460 
		validateCountyNo(leRTSEx);
		// end defect 9460

		// defect 10458 
		validateTerm(leRTSEx);
		// end defect 10458 

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
	}

	/**
	 * Assign OrgNo from the combo box selection
	 * 
	 * @param aeRTSEx
	 */
	private void validateOrganizationName(RTSException aeRTSEx)
	{
		int liSelectedIndex =
			getcomboOrganizationName().getSelectedIndex();

		if (liSelectedIndex == -1)
		{
			// 150 
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getcomboOrganizationName());
		}
		else
		{
			caSpclPltRegisData.setOrgNo(
				getOrgNoFromDropDown(liSelectedIndex));
		}
	}

	/**
	 * Validate Plate No in the event that regpltcd has changed
	 * 
	 * @param aeRTSEx
	 */
	private void validatePlate(RTSException aeRTSEx)
	{
		if (gettxtPlateAge().isEmpty())
		{
			// 150 
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPlateAge());
		}
		else
		{
			String lsRegPltAge = gettxtPlateAge().getText().trim();
			int liPlateAge = Integer.parseInt(lsRegPltAge);
			if (liPlateAge > MAX_PLT_AGE)
			{
				// 150 
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtPlateAge());
			}
		}

		ValidateInventoryPattern laValidateInventoryPattern =
			new ValidateInventoryPattern();
		ProcessInventoryData laProcessInventoryData =
			new ProcessInventoryData();
		laProcessInventoryData.setItmCd(
			caSpclPltRegisData.getRegPltCd());
		laProcessInventoryData.setInvQty(1);
		laProcessInventoryData.setInvItmNo(
			caSpclPltRegisData.getRegPltNo());
		if (gettxtInvItmYr().isVisible())
		{
			if (gettxtInvItmYr()
				.getText()
				.trim()
				.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				// 150 
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtInvItmYr());
			}
			else
			{
				laProcessInventoryData.setInvItmYr(
					Integer.parseInt(gettxtInvItmYr().getText()));
			}
		}
		else
		{
			laProcessInventoryData.setInvItmYr(0);
		}

		try
		{

			laValidateInventoryPattern.validateItmNoInput(
				laProcessInventoryData.convertToInvAlloctnUIData(
					laProcessInventoryData));
		}
		catch (RTSException aeRTSEx1)
		{
			aeRTSEx.addException(aeRTSEx1, getcomboPlateType());

			if (gettxtInvItmYr().isVisible())
			{
				aeRTSEx.addException(aeRTSEx1, gettxtInvItmYr());
			}
		}
	}

	/** 
	 * Validate selected Term
	 * 
	 * @param aeRTSEx
	 */
	private void validateTerm(RTSException aeRTSEx)
	{
		if (getcomboTerm().getSelectedIndex() == -1)
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getcomboTerm());
		}
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
