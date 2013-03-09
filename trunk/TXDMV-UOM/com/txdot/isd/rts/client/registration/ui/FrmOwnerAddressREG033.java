package com.txdot.isd.rts.client.registration.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.*;
import com
	.txdot
	.isd
	.rts
	.client
	.registration
	.business
	.RegistrationVerify;
import com.txdot.isd.rts.client.title.ui.FrmCountyConfirmCTL002;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.cache.RegistrationClassCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmOwnerAddressREG033.java
 *
 * ---------------------------------------------------------------------
 * Change History:
 * Name   		Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and
 * 							doneWorking()
 * J Kwik		04/26/2002	Fix CQU100003653 - if USA checkbox is 
 *							checked/unchecked, put the focus back to the 
 *							owner address 1 field.
 * MAbs			05/08/2002	Made country field bigger CQU100003823
 * J Kwik		05/13/2002	Fix CQU100003872 - lengthen the country zip
 *							field.
 * J Kwik		05/13/2002	Fix CQU100003839 - make cnty res 
 *							functionality as in RTS1.
 * J Kwik		05/15/2002	Fix CQU100003931 - validateReqRenwlNotc
 *							incorrectly validates 3 month renewal window.
 * J Kwik		05/22/2002	Fix CQU100004056 - increase iPadY for delete 
 *							location checkbox.
 * J Kwik		05/24/2002	Fix CQU100004087 - handle Modify events for 
 *							RegistrationModifyData object in MF 
 *							unavailable mode.
 * S Govindappa 08/26/2002  Fix CQU100004677 - Trimmed the Renewal 
 *                          notice address text fields and vehicle 
 *                          location address text fields in 
 *                          validateRenwlMailngAddr method and 
 *                          validateVehLocAddr method when spaces are 
 *                          entered 
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 *                          make changes in actionPerformed().
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the 
 *                          user help guide so had to make changes in 
 *							actionPerformed().
 * B Brown     	12/18/2002	Defect 5060. Changed the field txtResCnty to 
 *                          input of 1(numeric only) in the Visual Comp 
 *                          editor so non-numeric data could not be 
 *                          entered.
 * T Pederson	12/14/2004	In handleDataToDisplay, disabled request 
 *							renewal checkbox so it could not be checked
 *							using hot key (alt q) when not visible.
 *							modify handleDataToDisplay()
 *							defect 6494 Ver 5.2.2
 * B Hargrove	04/12/2005	Added JPanel to allow tabbing down left side
 * 							before tabbing down right side.
 * 							Fully defined 
 * 							border.EtchedBorder() and
 * 							TitledBorder() to remove warning.  
 *							added jPanel
 *							modify getJPanel5()
 *							defect 7894 Ver 5.2.3
 * B Hargrove	05/20/2005	Increase size of USA checkbox (was showing  
 *							'U...'. When doing an Address Change, the 
 *							Renewal Recipient Name text was 'crunched'.
 *							modify visual composition
 *							defect 7894 Ver 5.2.3
 * B Hargrove	06/08/2005	When checking 'Request Renewal Notice' and
 *							then unchecking and escaping, then going 
 *							forward, the checkbox is still checked.
 *							Class boolean (cbRenwlReq) for 'renewal 
 *							requested' was not being cleared. 
 *							modify itemStateChanged() 
 *							defect 7894 Ver 5.2.3
 * J Zwiener	06/09/2005	Comment out setRequestFocus(false) since 
 * 							RTSDialogBox now default it to false.
 * 							modify method initialize()
 * 							defect 8215 Ver 5.2.3
 * B Hargrove	07/18/2005	Modify code for move to Java 1.4
 * 							Remove unused methods,arguments and variables.
 * 							Remove keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete implements KeyListener
 * 							modify validateResCnty()
 * 							delete displayError(), getMFVehicleData()
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * B Hargrove	11/14/2005	Add check for Reg Period Length must be 12 
 *							months or more to request a renewal notice.
 *							add isRPLTwelveMonths()
 *							modify handleDataToDisplay()
 *							defect 8404 Ver 5.2.2 fix 7
 * K Harrell	12/14/2005	Restored setRequestFocus(false) as 
 * 							RTSDialogBox restored to true.
 * 							modify method initialize()
 * 							defect 7894 Ver 5.2.3
 * K Harrell	12/15/2005	Focus returning to checkboxes on error	
 * 							Removed call to super.keyPressed()
 * 							modify keyPressed()
 * 							defect 7894 Ver 5.2.3 
 * T. Pederson	12/21/2005	Moved setting default focus to setData.
 * 							delete windowActivated(), windowClosed(), 
 * 							windowClosing(), windowDeactivated(),
 * 							windowDeiconified(), windowIconified() and 
 * 							windowOpened()
 * 							modify setData()	
 * 							defect 8494 Ver 5.2.3
 * K Harrell	02/07/2006	Work for USA and Recipient chkboxes when
 * 							use space bar for selection; Add'l screen 
 * 							alignment work.
 * 							modify keyPressed() 
 * 							defect 7894 Ver 5.2.3
 * K Harrell	02/13/2006  Do not setColor for OwnerId label
 * 							modify getstcLblOwnerId()
 * 							defect 7894 Ver 5.2.3 
 * K Harrell	02/05/2007	Use PlateTypeCache vs. 
 * 							    RegistrationRenewalsCache
 * 							delete getBuilderData()
 * 							modify validateReqRenwlNotc()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/16/2007	use SystemProperty.isCounty()
 * 							modify confirmCntyRes(),
 * 							 validateResCnty()		
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/05/2007	Add logic for rejecting RNR request if 
 * 							unlinked Special Plate.   
 * 							modify validateReqRenwlNotc()
 * 							defect 9085 Ver Special Plates
 * J Rue		05/14/2008	Set VTRAuth to false if AuthCd is empty.
 * 							modify validateNameWithNoSSN()
 * 							defect 8627 Ver Defect POS A
 * K Harrell	05/22/2008	Prevent RNR when Plate Removed
 * 							add PLATE_REMOVED 
 * 							modify validateReqRenwlNotc()
 * 							defect 9670 Ver Defect POS A
 * K Harrell	05/28/2008	Use RTSInputField Constants to define inputs
 * 							Further class cleanup. 
 * 							defect 7894 Ver Defect POS A  
 * B Hargrove	07/17/2008 	Change edit for Request Renewal Notice to
 * 							also allow Reg Renewal Code = 3. 
 * 							(3 = RTS produces a renewal notice with Reg 
 * 							fees only (plate fees charged by outside vendor)
 * 							modify validateReqRenwlNotc() 
 * 							defect 9760 Ver MyPlates_POS
 * K Harrell	09/03/2008	Remove FocusListener as no method implements
 * 							Add'l class cleanup. Remove returns where not 
 * 								used.
 * 							delete focusLost(), focusGained()
 * 							modify validateOwnrAddr(), 
 * 								validateOwnrName1(),
 * 							    validateRenwlMailngAddr(),
 * 								validateVehLocAddr()
 * 							defect 9811 Ver Defect_POS_B 
 * Min Wang		09/10/2008	Show one message text for error number 738 
 * 							for ATV.  The Plate type is invalid.
 * 							modify validateReqRenwlNotc()
 *							defect 9436 Defect_POS_B
 * K Harrell	06/30/2009	Implement new OwnerData 
 * 							add setDataToDataObject() 
 * 							delete CNTRY_ZIP_MAXLENGTH,INDI_0,  
 * 							 ciCheckBoxWithFocus, checkBoxes
 * 							delete setOwnerAddress(), setOwnerData(),
 * 							 setRegistrationData(), setRegValidData(),
 * 							 setTitleData(), setVehicleInquiryData(),
 * 							 keyPressed(), keyReleased(),setInputFields()
 * 							modify handleDataToDisplay(),
 * 							 setData(), setInputFields(), 
 * 							 confirmCntyRes(), validateResCnty(), 
 * 							 getJPanel5(),initialize(), 
 * 							 isRPLTwelveMonths()   
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	07/19/2009	Implement new validation of Country field.
 * 							 refactor RTSInputFields, get methods to 
 * 							 standards 
 * 							Add Constants for RTSInputFields, 
 * 							 mnemonics 
 * 							add caOwnrNameAddrComp, caRenwlAddrComp,
 * 							 caVehLocAddrComp
 * 							add ivjtxtVehicleUnitNo, gettxtVehicleUnitNo()
 * 							 setNonOwnerInfoToDisplay(), 
 * 							 setOwnerDataToDisplay() 
 * 							delete TX, USA 
 * 							delete ivjtxtUnitNo, gettxtUnitNo(), 
 * 							 setUSAAddressDisplay(), 
 * 							  setNonUSAAddressDisplay()
 * 							modify handleDataToDisplay(), initialize(), 
 * 							 itemStateChanged(), setData() 
 * 							 setDataToDataObject(), validateFields(), 
 * 							 validateOwnrName1(),
 *  						 validateRenwlMailingAddr(), 
 * 							 validateVehLocAddr()
 *  						defect 10127 Ver Defect_POS_F
 * K Harrell	07/22/2009	Disable OwnerId in MF Down if not HQ 
 * 							modify setData()
 * 							defect 10130 Ver Defect_POS_F 
 * K Harrell	08/07/2009	Validate Owner Address, Renewal Address, 
 * 							 Vehicle Loc Address upon initial presentation.
 * 							modify setData() 
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	09/05/2009	Add UtilityMethods.trimRTSInputField()	
 * 							modify setDataToDataObject()
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	12/29/2009  Implement character validation for MF
 * 							add cvAddlMFValid1, cvAddlMFValid2
 *  						modify initialize(), validateFields(),
 * 							 setData(), setDataToDataObject()  
 * 							defect 10299 Ver Defect_POS_H 
 * K Harrell	02/16/2010	Enlarge Screen to accommodate EMail 
 * 							add ivjstclblEMail, ivjtxtRecpntEMail, 
 * 							 get methods 
 * 							add validateRecpntEMail() 
 * 							modify setNonOwnerInfoToDisplay(), 
 * 							 setDataToDataObject(), getJPanel11(), 
 * 							 getFrmOwnerAddressREG033ContentPane1()  
 * 							defect 10372 Ver POS_640 
 * K Harrell	03/09/2010	Add validation of OwnerId if enabled. Use 
 * 							test for is Enabled vs. MF Down when validating
 * 							OwnrName1. 
 * 							add validateOwnrId()
 * 							modify validateFields(), validateOwnrName1()
 * 							defect 10189 Ver POS_640 
 * K Harrell	07/11/2010	Add E-Reminder Checkbox, processing logic
 * 							Modification via Visual Editor 
 * 							add ivjchkEReminder, get method, 
 * 							   validateEReminder() 
 * 							modify setNonOwnerInfoToDisplay(), 
 * 							 setDataToDataObject(), getJPanel11(), 
 * 							 validateFields(), itemStateChanged()  
 * 							defect 10508 Ver 6.5.0 
 * K Harrell	10/06/2010	Delete existing "Delete" checkboxes; Add
 * 							"Delete" RTSButtons, including for EMail. 
 * 							Add keyReleased processing for enabling/
 * 							disabling pushbuttons. Modify via Visual
 * 							 Editor to match TTL007. 
 * 							Implement constants for all mnemonics
 * 							add DELETE_EMAIL
 * 							add ivjbtnDeleteLocation, ivjbtnDeleteRecipient, 
 * 							 ivjbtnDeleteRenewal, ivjbtnDeleteEMail, 
 * 							 get methods 
 * 							add setupDeleteButtons(), keyReleased()
 * 							delete ivjchkDeleteLocation, 
 * 							 ivjchkDeleteRecipient, ivjchkDeleteRenewal, 
 * 							 get methods 
 * 							modify itemStateChanged(),getJPanel5(), 
 * 							 actionPerformed(), setData()
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	12/07/2010	deleted duplicate getchkEReminder() 
 * 							6.7.0 Merge Issue 
 * K Harrell	03/08/2011	deleted duplicate getchkEReminder() 
 * 							6.7.1 Merge Issue  
 * ---------------------------------------------------------------------
 */
/** 
 * This form presents and allows the user to modify the Owner Address,
 * the Renewal Recipient Name, Renewal Notice Address, the Vehicle 
 * Location Address and the Unit No.
 * 
 * @version	6.7.1		 	03/08/2011
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 10:11:29
 */

public class FrmOwnerAddressREG033
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;

	private JCheckBox ivjchkRequestRenewalMailing = null;
	private JCheckBox ivjchkUSA = null;
	private JPanel ivjFrmOwnerAddressREG033ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel11 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel4 = null;
	private JPanel ivjJPanel5 = null;
	private JPanel ivjJPanel6 = null;
	private JPanel ivjJPanel7 = null;
	private JLabel ivjstcLblCounty = null;
	private JLabel ivjstcLblDash1 = null;
	private JLabel ivjstcLblDash2 = null;
	private JLabel ivjstcLblDash3 = null;
	private JLabel ivjstcLblOwnerAddress = null;
	private JLabel ivjstcLblOwnerId = null;
	private JLabel ivjstcLblOwnerName = null;
	private JLabel ivjstcLblRenewalMailing = null;
	private JLabel ivjstcLblRenewalRecipient = null;
	private JLabel ivjstcLblUnitNo = null;
	private JLabel ivjstcLblVehicleLocation = null;
	private RTSInputField ivjtxtOwnerCity = null;
	private RTSInputField ivjtxtOwnerCntry = null;
	private RTSInputField ivjtxtOwnerCntryZpcd = null;
	private RTSInputField ivjtxtOwnerId = null;
	private RTSInputField ivjtxtOwnerName1 = null;
	private RTSInputField ivjtxtOwnerName2 = null;
	private RTSInputField ivjtxtOwnerState = null;
	private RTSInputField ivjtxtOwnerStreet1 = null;
	private RTSInputField ivjtxtOwnerStreet2 = null;
	private RTSInputField ivjtxtOwnerZpcd = null;
	private RTSInputField ivjtxtOwnerZpcdP4 = null;
	private RTSInputField ivjtxtRenewalMailingCity = null;
	private RTSInputField ivjtxtRenewalMailingState = null;
	private RTSInputField ivjtxtRenewalMailingStreet1 = null;
	private RTSInputField ivjtxtRenewalMailingStreet2 = null;
	private RTSInputField ivjtxtRenewalMailingZpcd = null;
	private RTSInputField ivjtxtRenewalMailingZpcdP4 = null;
	private RTSInputField ivjtxtRenewalRecipientName = null;
	private RTSInputField ivjtxtResComptCntyNo = null;
	private RTSInputField ivjtxtVehicleLocationCity = null;
	private RTSInputField ivjtxtVehicleLocationState = null;
	private RTSInputField ivjtxtVehicleLocationStreet1 = null;
	private RTSInputField ivjtxtVehicleLocationStreet2 = null;
	private RTSInputField ivjtxtVehicleLocationZpcd = null;
	private RTSInputField ivjtxtVehicleLocationZpcdP4 = null;
	private RTSInputField ivjtxtVehicleUnitNo = null;

	// defect 10372 
	private JLabel ivjstcLblEMail = null;
	private RTSInputField ivjtxtRecpntEMail = null;
	// end defect 10372 

	// defect 10508 
	private JCheckBox ivjchkEReminder = null;
	// end defect 10508 

	// defect 10592 
	private RTSButton ivjbtnDeleteLocation = null;
	private RTSButton ivjbtnDeleteRecipient = null;
	private RTSButton ivjbtnDeleteRenewal = null;
	private RTSButton ivjbtnDeleteEMail = null;
	// end defect 10592 

	// Objects
	private CommonFeesData caCommFeesData = null;
	private MFVehicleData caMFVehicleData = null;
	private AddressData caOwnerAddress = null;
	private OwnerData caOwnerData = null;
	private NameAddressComponent caOwnrNameAddrComp = null;
	private RegistrationData caRegistrationData = null;
	private RegistrationValidationData caRegValidData = null;
	private NameAddressComponent caRenwlAddrComp = null;
	private TitleData caTitleData = null;
	private VehicleInquiryData caVehicleInquiryData = null;
	private NameAddressComponent caVehLocAddrComp = null;

	//	boolean
	private boolean cbSetDataFinished = false;

	// String 
	String csTransCd = new String();

	// Vector 
	private static Vector cvAddlMFValid1 = new Vector();
	private static Vector cvAddlMFValid2 = new Vector();

	// Constant 
	private final static String CNTY_RESIDE = "County of Residence:";

	private final static String DASH = "-";
	private final static String DEFLT_CNTY = "161";

	// defect 10592 
	private final static String DELETE_EMAIL = "Delete E-Mail";
	// end defect 10592 

	private final static String DELETE_LOC_ADDR =
		"Delete Location Address";
	private final static String DELETE_RECIP_NAME =
		"Delete Recipient Name";
	private final static String DELETE_RENEW_ADDR =
		"Delete Renewal Address";
	private final static String ERRMSG_CPS_INVALID =
		" THE CLASS/PLATE/STICKER COMBINATION IS INVALID.";
	private final static String ERRMSG_DYWT_CANCEL =
		"Are you sure you want to Cancel?\nIf yes, changes will "
			+ "not be saved.";
	private final static String ERRMSG_PLATE_TYPE_INVALID =
		" THE PLATE TYPE IS INVALID.";
	private final static String ERRMSG_REFER_TO_REMARKS =
		" REFER TO THE REMARKS BOX FOR ADDITIONAL INFORMATION.";
	private final static String ERRMSG_REG_EXP =
		" THE REGISTRATION HAS EXPIRED.";
	private final static String ERRMSG_REG_IN_FUTURE =
		" THE REGISTRATION IS IN THE FUTURE.";
	private final static String ERRMSG_REN_CANNOT_PRINT =
		"  A RENEWAL NOTICE CANNOT BE PRINTED.";
	private final static String ERRMSG_VERIFY_ADDR =
		" VERIFY THE OWNER ADDRESS OR RENEWAL RECIPIENT ADDRESS.";
	private final static int MAX_CNTY_NO = 254;
	private final static String OWNR_ADDR = "Owner Address:";
	private final static String OWNR_ID = "Owner Id:";
	private final static String OWNR_NAME = "Owner Name:";
	private final static String PLATE_REMOVED =
		"THE PLATE HAS BEEN REMOVED.";
	private final static String RENEW_NOTICE_ADDR =
		"Renewal Notice Address: (if different)";
	private final static String RENEW_RECIP_NAME =
		"Renewal Recipient Name: (If different)";
	private final static String REQ_RENEW_NOTICE =
		"Request Renewal Notice";
	private final static String SELECT = "Select if needed:";
	private final static long serialVersionUID = 3998098222564760187L;
	private final static String TITLE_REG033 =
		"Owner Address          REG033";
	private final static String UNIT_NO = "Unit No:";
	private final static String VEH_LOC_ADDR =
		" Vehicle Location Address: (if different)";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */

	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmOwnerAddressREG033 aFrmOwnerAddressREG033;
			aFrmOwnerAddressREG033 = new FrmOwnerAddressREG033();
			aFrmOwnerAddressREG033.setModal(true);
			aFrmOwnerAddressREG033
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			aFrmOwnerAddressREG033.show();
			java.awt.Insets insets = aFrmOwnerAddressREG033.getInsets();
			aFrmOwnerAddressREG033.setSize(
				aFrmOwnerAddressREG033.getWidth()
					+ insets.left
					+ insets.right,
				aFrmOwnerAddressREG033.getHeight()
					+ insets.top
					+ insets.bottom);
			aFrmOwnerAddressREG033.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}
	// end defect 10299 

	/**
	 * FrmOwnerAddressREG033 constructor comment.
	 */
	public FrmOwnerAddressREG033()
	{
		super();
		initialize();
	}

	/**
	 * FrmOwnerAddressREG033 constructor with parent.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmOwnerAddressREG033(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmOwnerAddressREG033 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmOwnerAddressREG033(JFrame aaParent)
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

			gettxtRenewalRecipientName().setBackground(Color.white);

			// defect 10127
			// ENTER 
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateFields() && verifyResComptCntyNo())
				{
					setDataToDataObject();

					if (validateNameWithNoSSN())
					{
						// Not referenced 
						// caRegValidData.setOwnrAddrOK(true);

						// TODO Determine why this is here 
						// RNR transcd set in REG003 when complete 
						// transaction.
						if (caRegValidData
							.getTransCode()
							.equals(TransCdConstant.RNR))
						{
							caRegValidData.setTransCode(
								TransCdConstant.ADDR);
						}
						// end defect 10127

						getController().processData(
							AbstractViewController.ENTER,
							caVehicleInquiryData);
					}
				}
			}
			// defect 10592 
			else if (aaAE.getSource() == getbtnDeleteRecipient())
			{
				gettxtRenewalRecipientName().setText("");
				getbtnDeleteRecipient().setEnabled(false);
			}
			else if (aaAE.getSource() == getbtnDeleteRenewal())
			{
				caRenwlAddrComp.resetNonOwnerAddr();
				getbtnDeleteRenewal().setEnabled(false);
			}
			else if (aaAE.getSource() == getbtnDeleteLocation())
			{
				caVehLocAddrComp.resetNonOwnerAddr();
				getbtnDeleteLocation().setEnabled(false);
			}
			else if (aaAE.getSource() == getbtnDeleteEMail())
			{
				gettxtRecpntEMail().setText("");
				if (!getchkEReminder().isSelected())
				{
					clearAllColor(gettxtRecpntEMail());
				}
				getbtnDeleteEMail().setEnabled(false);
			}
			// end defect 10592 
			// CANCEL 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				if (!handleCancel())
				{
					return;
				}
				getController().processData(
					AbstractViewController.CANCEL,
					caVehicleInquiryData);
			}
			// HELP 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				if (UtilityMethods.isMFUP())
				{
					// defect 10112 
					// Use csTransCd 
					if (csTransCd.equals(TransCdConstant.DUPL))
					{
						RTSHelp.displayHelp(RTSHelp.REG033A);
					}
					else if (csTransCd.equals(TransCdConstant.EXCH))
					{
						RTSHelp.displayHelp(RTSHelp.REG033B);
					}
					else if (csTransCd.equals(TransCdConstant.REPL))
					{
						RTSHelp.displayHelp(RTSHelp.REG033C);
					}
					else if (csTransCd.equals(TransCdConstant.PAWT))
					{
						RTSHelp.displayHelp(RTSHelp.REG033D);
					}
					else if (
						(csTransCd.equals(TransCdConstant.CORREG))
							&& (caRegValidData.getRegModify()
								== RegistrationConstant
									.REG_MODIFY_APPREHENDED))
					{
						RTSHelp.displayHelp(RTSHelp.REG033E);
					}
					else if (csTransCd.equals(TransCdConstant.CORREG))
					{
						RTSHelp.displayHelp(RTSHelp.REG033F);
					}
					else if (csTransCd.equals(TransCdConstant.ADDR))
					{
						RTSHelp.displayHelp(RTSHelp.REG033G);
					}
					else if (csTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.REG033N);
					}
				}
				//if MF is unavailable
				else
				{
					if (csTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.REG033H);
					}
					else if (csTransCd.equals(TransCdConstant.REPL))
					{
						RTSHelp.displayHelp(RTSHelp.REG033I);
					}
					else if (csTransCd.equals(TransCdConstant.EXCH))
					{
						RTSHelp.displayHelp(RTSHelp.REG033J);
					}
					else if (csTransCd.equals(TransCdConstant.PAWT))
					{
						RTSHelp.displayHelp(RTSHelp.REG033K);
					}
					else if (
						(csTransCd.equals(TransCdConstant.CORREG))
							&& (caRegValidData.getRegModify()
								== RegistrationConstant
									.REG_MODIFY_APPREHENDED))
					{
						RTSHelp.displayHelp(RTSHelp.REG033L);
					}
					else if (csTransCd.equals(TransCdConstant.CORREG))
					{
						RTSHelp.displayHelp(RTSHelp.REG033M);
					}
				}
				// end defect 10112
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
	private boolean confirmResComptCntyNo()
	{
		String lsCurrCnty = gettxtResComptCntyNo().getText();
		int liOfcId = Integer.parseInt(lsCurrCnty);
		OfficeIdsData laOfcData = OfficeIdsCache.getOfcId(liOfcId);

		FrmCountyConfirmCTL002 laCntyCnfrm =
			new FrmCountyConfirmCTL002(
				getController().getMediator().getDesktop(),
				lsCurrCnty,
				laOfcData.getOfcName());

		// defect 10112
		return laCntyCnfrm.displayWindow()
			== FrmCountyConfirmCTL002.YES;
		// end dect 10112 
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
				ivjButtonPanel1.setLayout(new java.awt.FlowLayout());
				ivjButtonPanel1.setBounds(188, 425, 232, 44);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * This method initializes ivjbtnDeleteEMail
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnDeleteEMail()
	{
		if (ivjbtnDeleteEMail == null)
		{
			ivjbtnDeleteEMail = new RTSButton();
			ivjbtnDeleteEMail.setSize(205, 26);
			ivjbtnDeleteEMail.setText(DELETE_EMAIL);
			ivjbtnDeleteEMail.setLocation(25, 79);
			ivjbtnDeleteEMail.setMnemonic(KeyEvent.VK_I);
			ivjbtnDeleteEMail.addActionListener(this);

		}
		return ivjbtnDeleteEMail;
	}

	/**
	 * Return the ivjbtnDeleteLocation property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnDeleteLocation()
	{
		if (ivjbtnDeleteLocation == null)
		{
			try
			{
				ivjbtnDeleteLocation = new RTSButton();
				ivjbtnDeleteLocation.setBounds(25, 108, 205, 26);
				ivjbtnDeleteLocation.setName("ivjbtnDeleteLocation");
				ivjbtnDeleteLocation.setMnemonic(KeyEvent.VK_L);
				ivjbtnDeleteLocation.setText(DELETE_LOC_ADDR);
				ivjbtnDeleteLocation.setMaximumSize(
					new java.awt.Dimension(164, 22));
				ivjbtnDeleteLocation.setActionCommand(DELETE_LOC_ADDR);
				ivjbtnDeleteLocation.setMinimumSize(
					new java.awt.Dimension(164, 22));
				ivjbtnDeleteLocation.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbtnDeleteLocation;
	}

	/**
	 * Return the ivjbtnDeleteRecipient property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnDeleteRecipient()
	{
		if (ivjbtnDeleteRecipient == null)
		{
			try
			{
				ivjbtnDeleteRecipient = new RTSButton();
				ivjbtnDeleteRecipient.setSize(205, 26);
				ivjbtnDeleteRecipient.setName("ivjbtnDeleteRecipient");
				ivjbtnDeleteRecipient.setMnemonic(KeyEvent.VK_D);
				ivjbtnDeleteRecipient.setText(DELETE_RECIP_NAME);
				ivjbtnDeleteRecipient.setMaximumSize(
					new java.awt.Dimension(153, 22));
				ivjbtnDeleteRecipient.setActionCommand(
					DELETE_RECIP_NAME);
				ivjbtnDeleteRecipient.setMinimumSize(
					new java.awt.Dimension(153, 22));
				ivjbtnDeleteRecipient.setLocation(25, 21);
				// user code begin {1}
				ivjbtnDeleteRecipient.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbtnDeleteRecipient;
	}

	/**
	 * Return the ivjbtnDeleteRenewal property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnDeleteRenewal()
	{
		if (ivjbtnDeleteRenewal == null)
		{
			try
			{
				ivjbtnDeleteRenewal = new RTSButton();
				ivjbtnDeleteRenewal.setSize(205, 26);
				ivjbtnDeleteRenewal.setName("ivjbtnDeleteRenewal");
				ivjbtnDeleteRenewal.setMnemonic(KeyEvent.VK_R);
				ivjbtnDeleteRenewal.setText(DELETE_RENEW_ADDR);
				ivjbtnDeleteRenewal.setMaximumSize(
					new java.awt.Dimension(164, 22));
				ivjbtnDeleteRenewal.setActionCommand(DELETE_RENEW_ADDR);
				ivjbtnDeleteRenewal.setMinimumSize(
					new java.awt.Dimension(164, 22));
				ivjbtnDeleteRenewal.setLocation(25, 50);
				// user code begin {1}
				ivjbtnDeleteRenewal.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbtnDeleteRenewal;
	}

	
	/**
	 * This method initializes ivjchkEReminder
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkEReminder()
	{
		if (ivjchkEReminder == null)
		{
			ivjchkEReminder = new JCheckBox();
			ivjchkEReminder.setSize(91, 20);
			ivjchkEReminder.setText(
				RegistrationConstant.EREMINDER_CHKBX_LABEL);
			ivjchkEReminder.setMnemonic(KeyEvent.VK_E);
			ivjchkEReminder.setLocation(187, 200);
			ivjchkEReminder.addItemListener(this); 
		}
		return ivjchkEReminder;
	}

	/**
	 * Return the ivjchkRequestRenewalMailing property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkRequestRenewalMailing()
	{
		if (ivjchkRequestRenewalMailing == null)
		{
			try
			{
				ivjchkRequestRenewalMailing = new JCheckBox();
				ivjchkRequestRenewalMailing.setSize(238, 20);
				ivjchkRequestRenewalMailing.setName(
					"ivjchkRequestRenewalMailing");
				ivjchkRequestRenewalMailing.setMnemonic(KeyEvent.VK_Q);
				ivjchkRequestRenewalMailing.setText(REQ_RENEW_NOTICE);
				ivjchkRequestRenewalMailing.setVisible(true);
				// user code begin {1}
				ivjchkRequestRenewalMailing.setLocation(4, 6);
				ivjchkRequestRenewalMailing.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjchkRequestRenewalMailing;
	}

	/**
	 * Return the ivjchkUSA property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkUSA()
	{
		if (ivjchkUSA == null)
		{
			try
			{
				ivjchkUSA = new JCheckBox();
				ivjchkUSA.setBounds(230, 12, 50, 19);
				ivjchkUSA.setName("ivjchkUSA");
				// defect 10127 
				ivjchkUSA.setMnemonic(KeyEvent.VK_U);
				// end defect 10127 
				ivjchkUSA.setText(CommonConstant.STR_USA);
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
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjchkUSA;
	}

	/**
	 * Return the ivjFrmOwnerAddressREG033ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmOwnerAddressREG033ContentPane1()
	{
		if (ivjFrmOwnerAddressREG033ContentPane1 == null)
		{
			try
			{
				ivjFrmOwnerAddressREG033ContentPane1 = new JPanel();
				ivjFrmOwnerAddressREG033ContentPane1.setName(
					"FrmOwnerAddressREG033ContentPane1");
				// defect 10372 
				ivjFrmOwnerAddressREG033ContentPane1.setLayout(null);

				ivjFrmOwnerAddressREG033ContentPane1.add(
					getJPanel11(),
					null);
				ivjFrmOwnerAddressREG033ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmOwnerAddressREG033ContentPane1.add(
					getJPanel7(),
					null);
				ivjFrmOwnerAddressREG033ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmOwnerAddressREG033ContentPane1.setMinimumSize(
					new java.awt.Dimension(600, 450));
				ivjFrmOwnerAddressREG033ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				// end defect 10372 

			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjFrmOwnerAddressREG033ContentPane1;

	}

	/**
	 * Return the ivjJPanel1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setLayout(null);
				ivjJPanel1.add(getstcLblCounty(), null);
				ivjJPanel1.add(gettxtResComptCntyNo(), null);
				ivjJPanel1.setBounds(10, 7, 266, 24);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the ivjJPanel11 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel11()
	{
		if (ivjJPanel11 == null)
		{
			try
			{
				ivjJPanel11 = new JPanel();
				ivjJPanel11.setName("ivjJPanel11");
				ivjJPanel11.setLayout(null);
				ivjJPanel11.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel11.setMinimumSize(
					new java.awt.Dimension(284, 378));

				ivjJPanel11.add(getchkRequestRenewalMailing(), null);
				ivjJPanel11.add(getstcLblRenewalMailing(), null);
				ivjJPanel11.add(getstcLblVehicleLocation(), null);
				ivjJPanel11.add(getstcLblUnitNo(), null);
				ivjJPanel11.add(gettxtRenewalRecipientName(), null);
				ivjJPanel11.add(gettxtRenewalMailingStreet1(), null);
				ivjJPanel11.add(gettxtRenewalMailingStreet2(), null);
				ivjJPanel11.add(gettxtRenewalMailingCity(), null);
				ivjJPanel11.add(gettxtRenewalMailingState(), null);
				ivjJPanel11.add(gettxtRenewalMailingZpcd(), null);
				ivjJPanel11.add(gettxtRenewalMailingZpcdP4(), null);
				ivjJPanel11.add(gettxtVehicleLocationStreet1(), null);
				ivjJPanel11.add(gettxtVehicleLocationStreet2(), null);
				ivjJPanel11.add(gettxtVehicleLocationCity(), null);
				ivjJPanel11.add(gettxtVehicleLocationState(), null);
				ivjJPanel11.add(gettxtVehicleLocationZpcd(), null);
				ivjJPanel11.add(gettxtVehicleLocationZpcdP4(), null);
				ivjJPanel11.add(gettxtVehicleUnitNo(), null);
				ivjJPanel11.add(getstcLblDash2(), null);
				ivjJPanel11.add(getstcLblDash3(), null);
				// user code end

				ivjJPanel11.add(getstcLblRenewalRecipient(), null);
				ivjJPanel11.add(getstcLblEMail(), null);
				ivjJPanel11.add(gettxtRecpntEMail(), null);
				// defect 10508 
				ivjJPanel11.add(getchkEReminder(), null);
				// end defect 10508 
				ivjJPanel11.setBounds(302, 7, 313, 388);
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel11;
	}

	/**
	 * Return the ivjJPanel2 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("ivjJPanel2");
				ivjJPanel2.setLayout(null);
				ivjJPanel2.add(getstcLblOwnerId(), null);
				ivjJPanel2.add(getstcLblOwnerName(), null);
				ivjJPanel2.add(gettxtOwnerId(), null);
				ivjJPanel2.add(gettxtOwnerName1(), null);
				ivjJPanel2.add(gettxtOwnerName2(), null);
				ivjJPanel2.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel2.setMinimumSize(
					new java.awt.Dimension(275, 88));
				ivjJPanel2.setBounds(2, 3, 295, 97);
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * Return the ivjJPanel3 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("ivjJPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.add(getchkUSA(), null);
				ivjJPanel3.add(gettxtOwnerStreet1(), null);
				ivjJPanel3.add(gettxtOwnerStreet2(), null);
				ivjJPanel3.add(getstcLblOwnerAddress(), null);
				ivjJPanel3.add(getJPanel6(), null);
				ivjJPanel3.setBounds(2, 106, 295, 113);
				ivjJPanel3.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel3.setMinimumSize(
					new java.awt.Dimension(275, 141));

			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * Return the ivjJPanel4 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel4()
	{
		if (ivjJPanel4 == null)
		{
			try
			{
				ivjJPanel4 = new JPanel();
				ivjJPanel4.setName("ivjJPanel4");
				ivjJPanel4.setLayout(null);
				ivjJPanel4.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel4.setMinimumSize(
					new java.awt.Dimension(520, 238));

				ivjJPanel4.add(getJPanel1(), null);
				ivjJPanel4.add(getJPanel5(), null);
				ivjJPanel4.setBounds(2, 225, 295, 184);

			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel4;
	}

	/**
	 * Return the ivjJPanel5 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel5()
	{
		if (ivjJPanel5 == null)
		{
			try
			{
				ivjJPanel5 = new JPanel();
				ivjJPanel5.setName("ivjJPanel5");
				ivjJPanel5.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT));
				ivjJPanel5.setLayout(null);
				ivjJPanel5.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel5.setPreferredSize(
					new java.awt.Dimension(245, 66));
				ivjJPanel5.setMinimumSize(
					new java.awt.Dimension(245, 103));

				// defect 10592 
				ivjJPanel5.add(getbtnDeleteRecipient(), null);
				ivjJPanel5.add(getbtnDeleteRenewal(), null);
				ivjJPanel5.add(getbtnDeleteEMail(), null);
				ivjJPanel5.add(getbtnDeleteLocation(), null);

				RTSButtonGroup laButtonGrp = new RTSButtonGroup();
				laButtonGrp.add(getbtnDeleteRecipient());
				laButtonGrp.add(getbtnDeleteRenewal());
				laButtonGrp.add(getbtnDeleteEMail());
				laButtonGrp.add(getbtnDeleteLocation());
				// end defect 10592 

				ivjJPanel5.setBounds(12, 35, 254, 142);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel5;
	}

	/**
	 * Return the ivjJPanel6 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel6()
	{
		if (ivjJPanel6 == null)
		{
			try
			{
				ivjJPanel6 = new JPanel();
				ivjJPanel6.setName("ivjJPanel6");
				ivjJPanel6.setLayout(null);
				ivjJPanel6.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel6.setMinimumSize(
					new java.awt.Dimension(260, 24));
				getJPanel6().add(
					gettxtOwnerCity(),
					gettxtOwnerCity().getName());
				getJPanel6().add(
					gettxtOwnerState(),
					gettxtOwnerState().getName());
				getJPanel6().add(
					gettxtOwnerZpcd(),
					gettxtOwnerZpcd().getName());
				getJPanel6().add(
					getstcLblDash1(),
					getstcLblDash1().getName());
				getJPanel6().add(
					gettxtOwnerZpcdP4(),
					gettxtOwnerZpcdP4().getName());
				getJPanel6().add(
					gettxtOwnerCntry(),
					gettxtOwnerCntry().getName());
				getJPanel6().add(
					gettxtOwnerCntryZpcd(),
					gettxtOwnerCntryZpcd().getName());
				// user code begin {1}
				ivjJPanel6.setBounds(8, 80, 279, 25);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel6;
	}

	/**
	 * This method initializes ivjJPanel7
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel7()
	{
		if (ivjJPanel7 == null)
		{
			ivjJPanel7 = new JPanel();
			ivjJPanel7.setLayout(null);
			ivjJPanel7.add(getJPanel2(), null);
			ivjJPanel7.add(getJPanel3(), null);
			ivjJPanel7.add(getJPanel4(), null);
			ivjJPanel7.setBounds(2, 7, 297, 413);
		}
		return ivjJPanel7;
	}

	/**
	 * Return the ivjstcLblCounty property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCounty()
	{
		if (ivjstcLblCounty == null)
		{
			try
			{
				ivjstcLblCounty = new JLabel();
				ivjstcLblCounty.setName("ivjstcLblCounty");
				ivjstcLblCounty.setDisplayedMnemonic(KeyEvent.VK_C);
				ivjstcLblCounty.setText(CNTY_RESIDE);
				ivjstcLblCounty.setMaximumSize(
					new java.awt.Dimension(119, 14));
				ivjstcLblCounty.setMinimumSize(
					new java.awt.Dimension(119, 14));
				// user code begin {1}
				ivjstcLblCounty.setLabelFor(gettxtResComptCntyNo());
				// user code end
				ivjstcLblCounty.setSize(139, 20);
				ivjstcLblCounty.setLocation(10, 0);
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblCounty;
	}

	/**
	 * Return the ivjstcLblDash1 property value.
	 * (for Owner Address)
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDash1()
	{
		if (ivjstcLblDash1 == null)
		{
			try
			{
				ivjstcLblDash1 = new JLabel();
				ivjstcLblDash1.setName("ivjstcLblDash1");
				ivjstcLblDash1.setText(DASH);
				ivjstcLblDash1.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash1.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash1.setBounds(228, 1, 8, 24);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblDash1;
	}

	/**
	 * Return the ivjstcLblDash2 property value.
	 * (for Renewal Address) 
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDash2()
	{
		if (ivjstcLblDash2 == null)
		{
			try
			{
				ivjstcLblDash2 = new JLabel();
				ivjstcLblDash2.setSize(9, 14);
				ivjstcLblDash2.setName("ivjstcLblDash2");
				ivjstcLblDash2.setText(DASH);
				ivjstcLblDash2.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash2.setMinimumSize(
					new java.awt.Dimension(4, 14));
				// user code begin {1}
				ivjstcLblDash2.setLocation(232, 168);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblDash2;
	}

	/**
	 * Return the ivjstcLblDash3 property value.
	 * (for Vehicle Location Address) 
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDash3()
	{
		if (ivjstcLblDash3 == null)
		{
			try
			{
				ivjstcLblDash3 = new JLabel();
				ivjstcLblDash3.setSize(9, 14);
				ivjstcLblDash3.setName("ivjstcLblDash3");
				ivjstcLblDash3.setText(DASH);
				ivjstcLblDash3.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash3.setMinimumSize(
					new java.awt.Dimension(4, 14));
				// user code begin {1}
				ivjstcLblDash3.setLocation(232, 332);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblDash3;
	}

	/**
	 * Return the ivjstcLblOwnerAddress property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerAddress()
	{
		if (ivjstcLblOwnerAddress == null)
		{
			try
			{
				ivjstcLblOwnerAddress = new JLabel();
				ivjstcLblOwnerAddress.setName("ivjstcLblOwnerAddress");
				ivjstcLblOwnerAddress.setDisplayedMnemonic(
					KeyEvent.VK_O);
				ivjstcLblOwnerAddress.setText(OWNR_ADDR);
				ivjstcLblOwnerAddress.setMaximumSize(
					new java.awt.Dimension(92, 14));
				ivjstcLblOwnerAddress.setMinimumSize(
					new java.awt.Dimension(92, 14));
				// user code begin {1}
				ivjstcLblOwnerAddress.setLabelFor(gettxtOwnerStreet1());
				// user code end
				ivjstcLblOwnerAddress.setBounds(12, 11, 135, 20);
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblOwnerAddress;
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
				ivjstcLblOwnerId = new JLabel();
				ivjstcLblOwnerId.setBounds(9, 3, 63, 20);
				ivjstcLblOwnerId.setName("ivjstcLblOwnerId");
				ivjstcLblOwnerId.setText(OWNR_ID);
				ivjstcLblOwnerId.setMaximumSize(
					new java.awt.Dimension(54, 14));
				ivjstcLblOwnerId.setMinimumSize(
					new java.awt.Dimension(54, 14));
				ivjstcLblOwnerId.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblOwnerId;
	}

	/**
	 * Return the ivjstcLblOwnerName property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerName()
	{
		if (ivjstcLblOwnerName == null)
		{
			try
			{
				ivjstcLblOwnerName = new JLabel();
				ivjstcLblOwnerName.setBounds(10, 26, 114, 20);
				ivjstcLblOwnerName.setName("ivjstcLblOwnerName");
				ivjstcLblOwnerName.setText(OWNR_NAME);
				ivjstcLblOwnerName.setMaximumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblOwnerName.setMinimumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblOwnerName.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblOwnerName;
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
			ivjstcLblEMail.setSize(42, 20);
			ivjstcLblEMail.setText(CommonConstant.TXT_EMAIL);
			ivjstcLblEMail.setLocation(8, 200);
			ivjstcLblEMail.setDisplayedMnemonic(
				java.awt.event.KeyEvent.VK_M);
			ivjstcLblEMail.setLabelFor(gettxtRecpntEMail());
		}
		return ivjstcLblEMail;
	}

	/**
	 * Return the ivjstcLblRenewalMailing property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRenewalMailing()
	{
		if (ivjstcLblRenewalMailing == null)
		{
			try
			{
				ivjstcLblRenewalMailing = new JLabel();
				ivjstcLblRenewalMailing.setName(
					"ivjstcLblRenewalMailing");
				ivjstcLblRenewalMailing.setDisplayedMnemonic(
					KeyEvent.VK_A);
				ivjstcLblRenewalMailing.setText(RENEW_NOTICE_ADDR);
				ivjstcLblRenewalMailing.setMaximumSize(
					new java.awt.Dimension(211, 14));
				ivjstcLblRenewalMailing.setMinimumSize(
					new java.awt.Dimension(211, 14));
				// user code begin {1}
				ivjstcLblRenewalMailing.setLabelFor(
					gettxtRenewalMailingStreet1());
				// user code end
				ivjstcLblRenewalMailing.setSize(240, 20);
				ivjstcLblRenewalMailing.setLocation(9, 94);
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblRenewalMailing;
	}

	/**
	 * Return the ivjstcLblRenewalRecipient property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRenewalRecipient()
	{
		if (ivjstcLblRenewalRecipient == null)
		{
			try
			{
				ivjstcLblRenewalRecipient = new JLabel();
				ivjstcLblRenewalRecipient.setName(
					"ivjstcLblRenewalRecipient");
				ivjstcLblRenewalRecipient.setDisplayedMnemonic(
					KeyEvent.VK_N);
				ivjstcLblRenewalRecipient.setText(RENEW_RECIP_NAME);
				ivjstcLblRenewalRecipient.setMaximumSize(
					new java.awt.Dimension(213, 14));
				ivjstcLblRenewalRecipient.setMinimumSize(
					new java.awt.Dimension(213, 14));
				// user code begin {1}
				ivjstcLblRenewalRecipient.setLabelFor(
					gettxtRenewalRecipientName());
				// user code end
				ivjstcLblRenewalRecipient.setSize(226, 20);
				ivjstcLblRenewalRecipient.setLocation(7, 29);
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblRenewalRecipient;
	}

	/**
	 * Return the ivjstcLblUnitNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblUnitNo()
	{
		if (ivjstcLblUnitNo == null)
		{
			try
			{
				ivjstcLblUnitNo = new JLabel();
				ivjstcLblUnitNo.setName("ivjstcLblUnitNo");
				ivjstcLblUnitNo.setDisplayedMnemonic(KeyEvent.VK_T);
				ivjstcLblUnitNo.setText(UNIT_NO);
				ivjstcLblUnitNo.setMaximumSize(
					new java.awt.Dimension(43, 14));
				ivjstcLblUnitNo.setMinimumSize(
					new java.awt.Dimension(43, 14));
				ivjstcLblUnitNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				ivjstcLblUnitNo.setLabelFor(gettxtVehicleUnitNo());
				// user code end
				ivjstcLblUnitNo.setBounds(53, 358, 68, 20);
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblUnitNo;
	}

	/**
	 * Return the ivjstcLblVehicleLocation property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVehicleLocation()
	{
		if (ivjstcLblVehicleLocation == null)
		{
			try
			{
				ivjstcLblVehicleLocation = new JLabel();
				ivjstcLblVehicleLocation.setName(
					"ivjstcLblVehicleLocation");
				ivjstcLblVehicleLocation.setDisplayedMnemonic(
					KeyEvent.VK_V);
				ivjstcLblVehicleLocation.setText(VEH_LOC_ADDR);
				ivjstcLblVehicleLocation.setMaximumSize(
					new java.awt.Dimension(217, 14));
				ivjstcLblVehicleLocation.setMinimumSize(
					new java.awt.Dimension(217, 14));
				// user code begin {1}
				ivjstcLblVehicleLocation.setLabelFor(
					gettxtVehicleLocationStreet1());
				// user code end
				ivjstcLblVehicleLocation.setSize(243, 20);
				ivjstcLblVehicleLocation.setLocation(8, 256);
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblVehicleLocation;
	}

	/**
	 * Return the ivjtxtOwnerCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerCity()
	{
		if (ivjtxtOwnerCity == null)
		{
			try
			{
				ivjtxtOwnerCity = new RTSInputField();
				ivjtxtOwnerCity.setName("ivjtxtOwnerCity");
				ivjtxtOwnerCity.setManagingFocus(false);
				ivjtxtOwnerCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCity.setText("");
				ivjtxtOwnerCity.setBounds(3, 4, 135, 20);
				// defect 10127 
				ivjtxtOwnerCity.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// end defect 10127
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerCity;
	}

	/**
	 * Return the ivjtxtOwnerCntry property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerCntry()
	{
		if (ivjtxtOwnerCntry == null)
		{
			try
			{
				ivjtxtOwnerCntry = new RTSInputField();
				ivjtxtOwnerCntry.setName("ivjtxtOwnerCntry");
				ivjtxtOwnerCntry.setManagingFocus(false);
				ivjtxtOwnerCntry.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCntry.setText("");
				ivjtxtOwnerCntry.setVisible(true);
				ivjtxtOwnerCntry.setBounds(143, 4, 44, 20);
				ivjtxtOwnerCntry.setInput(RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtOwnerCntry.setMaxLength(
					CommonConstant.LENGTH_CNTRY);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerCntry;
	}

	/**
	 * Return the ivjtxtOwnerCntryZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerCntryZpcd()
	{
		if (ivjtxtOwnerCntryZpcd == null)
		{
			try
			{
				ivjtxtOwnerCntryZpcd = new RTSInputField();
				ivjtxtOwnerCntryZpcd.setName("ivjtxtOwnerCntryZpcd");
				ivjtxtOwnerCntryZpcd.setManagingFocus(false);
				ivjtxtOwnerCntryZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCntryZpcd.setText("");
				ivjtxtOwnerCntryZpcd.setVisible(true);
				ivjtxtOwnerCntryZpcd.setBounds(193, 4, 79, 20);
				ivjtxtOwnerCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				// defect 10127
				ivjtxtOwnerCntryZpcd.setMaxLength(
					CommonConstant.LENGTH_CNTRY_ZIP);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerCntryZpcd;
	}

	/**
	 * Return the ivjtxtOwnerId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerId()
	{
		if (ivjtxtOwnerId == null)
		{
			try
			{
				ivjtxtOwnerId = new RTSInputField();
				ivjtxtOwnerId.setBounds(76, 3, 159, 20);
				ivjtxtOwnerId.setName("ivjtxtOwnerId");
				ivjtxtOwnerId.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerId.setBackground(java.awt.Color.white);
				ivjtxtOwnerId.setForeground(java.awt.Color.black);
				ivjtxtOwnerId.setEditable(true);
				// defect 10127
				ivjtxtOwnerId.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerId.setMaxLength(
					CommonConstant.LENGTH_OWNERID);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerId;
	}

	/**
	 * Return the ivjtxtOwnerName1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerName1()
	{
		if (ivjtxtOwnerName1 == null)
		{
			try
			{
				ivjtxtOwnerName1 = new RTSInputField();
				ivjtxtOwnerName1.setBounds(10, 46, 269, 20);
				ivjtxtOwnerName1.setName("ivjtxtOwnerName1");
				ivjtxtOwnerName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName1.setBackground(java.awt.Color.white);
				ivjtxtOwnerName1.setEditable(true);
				ivjtxtOwnerName1.setInput(RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtOwnerName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerName1;
	}

	/**
	 * Return the ivjtxtOwnerName2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerName2()
	{
		if (ivjtxtOwnerName2 == null)
		{
			try
			{
				ivjtxtOwnerName2 = new RTSInputField();
				ivjtxtOwnerName2.setSize(269, 20);
				ivjtxtOwnerName2.setName("ivjtxtOwnerName2");
				ivjtxtOwnerName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName2.setBackground(java.awt.Color.white);
				ivjtxtOwnerName2.setEditable(true);
				ivjtxtOwnerName2.setLocation(10, 70);
				ivjtxtOwnerName2.setInput(RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtOwnerName2.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerName2;
	}

	/**
	 * Return the ivjtxtOwnerState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerState()
	{
		if (ivjtxtOwnerState == null)
		{
			try
			{
				ivjtxtOwnerState = new RTSInputField();
				ivjtxtOwnerState.setName("ivjtxtOwnerState");
				ivjtxtOwnerState.setManagingFocus(false);
				ivjtxtOwnerState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerState.setText("");
				ivjtxtOwnerState.setBounds(146, 4, 25, 20);
				ivjtxtOwnerState.setInput(RTSInputField.ALPHA_ONLY);
				// defect 10127 
				ivjtxtOwnerState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerState;
	}

	/**
	 * Return the ivjtxtOwnerStreet1 property value.
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerStreet1()
	{
		if (ivjtxtOwnerStreet1 == null)
		{
			try
			{
				ivjtxtOwnerStreet1 = new RTSInputField();
				ivjtxtOwnerStreet1.setBounds(11, 35, 269, 20);
				ivjtxtOwnerStreet1.setName("ivjtxtOwnerAddress1");
				ivjtxtOwnerStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet1.setEditable(true);
				ivjtxtOwnerStreet1.setInput(RTSInputField.DEFAULT);
				// defect 10127
				ivjtxtOwnerStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerStreet1;
	}

	/**
	 * Return the ivjtxtOwnerStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerStreet2()
	{
		if (ivjtxtOwnerStreet2 == null)
		{
			try
			{
				ivjtxtOwnerStreet2 = new RTSInputField();
				ivjtxtOwnerStreet2.setBounds(11, 59, 269, 20);
				ivjtxtOwnerStreet2.setName("ivjtxtOwnerStreet2");
				ivjtxtOwnerStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet2.setInput(RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtOwnerStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerStreet2;
	}

	/**
	 * Return the ivjtxtOwnerZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerZpcd()
	{
		if (ivjtxtOwnerZpcd == null)
		{
			try
			{
				ivjtxtOwnerZpcd = new RTSInputField();
				ivjtxtOwnerZpcd.setName("ivjtxtOwnerZpcd");
				ivjtxtOwnerZpcd.setManagingFocus(false);
				ivjtxtOwnerZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerZpcd.setBounds(181, 4, 40, 20);
				ivjtxtOwnerZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				// defect 10127 
				ivjtxtOwnerZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerZpcd;
	}

	/**
	 * Return the ivjtxtOwnerZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerZpcdP4()
	{
		if (ivjtxtOwnerZpcdP4 == null)
		{
			try
			{
				ivjtxtOwnerZpcdP4 = new RTSInputField();
				ivjtxtOwnerZpcdP4.setName("ivjtxtOwnerZpcdP4");
				ivjtxtOwnerZpcdP4.setManagingFocus(false);
				ivjtxtOwnerZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerZpcdP4.setBounds(236, 4, 36, 20);
				// defect 10127
				// RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtOwnerZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerZpcdP4;
	}

	/**
	 * This method initializes ivjtxtRecpntEMail
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRecpntEMail()
	{
		if (ivjtxtRecpntEMail == null)
		{
			ivjtxtRecpntEMail = new RTSInputField();
			ivjtxtRecpntEMail.setSize(300, 20);
			ivjtxtRecpntEMail.setInput(RTSInputField.DEFAULT);
			ivjtxtRecpntEMail.setMaxLength(CommonConstant.LENGTH_EMAIL);
			ivjtxtRecpntEMail.setLocation(8, 223);
		}
		return ivjtxtRecpntEMail;
	}

	/**
	 * Return the ivjtxtRenewalMailingCity property value.
	 * 
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingCity()
	{
		if (ivjtxtRenewalMailingCity == null)
		{
			try
			{
				ivjtxtRenewalMailingCity = new RTSInputField();
				ivjtxtRenewalMailingCity.setSize(135, 20);
				ivjtxtRenewalMailingCity.setName(
					"ivjtxtRenewalMailingCity");
				ivjtxtRenewalMailingCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingCity.setInput(
					RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtRenewalMailingCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// end defect 10127 
				// user code begin {1}
				ivjtxtRenewalMailingCity.setLocation(8, 165);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtRenewalMailingCity;
	}

	/**
	 * Return the ivjtxtRenewalMailingState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingState()
	{
		if (ivjtxtRenewalMailingState == null)
		{
			try
			{
				ivjtxtRenewalMailingState = new RTSInputField();
				ivjtxtRenewalMailingState.setSize(25, 20);
				ivjtxtRenewalMailingState.setName(
					"ivjtxtRenewalMailingState");
				ivjtxtRenewalMailingState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingState.setInput(
					RTSInputField.ALPHA_ONLY);
				// defect 10127 
				ivjtxtRenewalMailingState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// end defect 10127 
				// user code begin {1}
				ivjtxtRenewalMailingState.setLocation(150, 165);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtRenewalMailingState;
	}

	/**
	 * Return the ivjtxtRenewalMailingStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingStreet1()
	{
		if (ivjtxtRenewalMailingStreet1 == null)
		{
			try
			{
				ivjtxtRenewalMailingStreet1 = new RTSInputField();
				ivjtxtRenewalMailingStreet1.setBounds(8, 117, 269, 20);
				ivjtxtRenewalMailingStreet1.setName(
					"ivjtxtRenewalMailingStreet1");
				ivjtxtRenewalMailingStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingStreet1.setInput(
					RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtRenewalMailingStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtRenewalMailingStreet1;
	}

	/**
	 * Return the ivjtxtRenewalMailingStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingStreet2()
	{
		if (ivjtxtRenewalMailingStreet2 == null)
		{
			try
			{
				ivjtxtRenewalMailingStreet2 = new RTSInputField();
				ivjtxtRenewalMailingStreet2.setSize(269, 20);
				ivjtxtRenewalMailingStreet2.setName(
					"ivjtxtRenewalMailingStreet2");
				ivjtxtRenewalMailingStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingStreet2.setInput(
					RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtRenewalMailingStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10127 
				// user code begin {1}
				ivjtxtRenewalMailingStreet2.setLocation(8, 141);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtRenewalMailingStreet2;
	}

	/**
	 * Return the ivjtxtRenewalMailingZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingZpcd()
	{
		if (ivjtxtRenewalMailingZpcd == null)
		{
			try
			{
				ivjtxtRenewalMailingZpcd = new RTSInputField();
				ivjtxtRenewalMailingZpcd.setSize(45, 20);
				ivjtxtRenewalMailingZpcd.setName(
					"ivjtxtRenewalMailingZpcd");
				ivjtxtRenewalMailingZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalMailingZpcd.setInput(
					RTSInputField.NUMERIC_ONLY);
				// defect 10127 
				ivjtxtRenewalMailingZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// end defect 10127 
				// user code begin {1}
				ivjtxtRenewalMailingZpcd.setLocation(183, 165);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtRenewalMailingZpcd;
	}

	/**
	 * Return the ivjtxtRenewalMailingZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalMailingZpcdP4()
	{
		if (ivjtxtRenewalMailingZpcdP4 == null)
		{
			try
			{
				ivjtxtRenewalMailingZpcdP4 = new RTSInputField();
				ivjtxtRenewalMailingZpcdP4.setSize(34, 20);
				ivjtxtRenewalMailingZpcdP4.setName(
					"ivjtxtRenewalMailingZpcdP4");
				ivjtxtRenewalMailingZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// defect 10127 
				// RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtRenewalMailingZpcdP4.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtRenewalMailingZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// end defect 10127 
				// user code begin {1}
				ivjtxtRenewalMailingZpcdP4.setLocation(243, 165);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtRenewalMailingZpcdP4;
	}

	/**
	 * Return the ivjtxtRenewalRecipientName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalRecipientName()
	{
		if (ivjtxtRenewalRecipientName == null)
		{
			try
			{
				ivjtxtRenewalRecipientName = new RTSInputField();
				ivjtxtRenewalRecipientName.setBounds(6, 49, 269, 20);
				ivjtxtRenewalRecipientName.setName(
					"ivjtxtRenewalRecipientName");
				ivjtxtRenewalRecipientName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRenewalRecipientName.setInput(
					RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtRenewalRecipientName.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// end defect 1027 

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtRenewalRecipientName;
	}

	/**
	 * Return the ivjtxtResComptCntyNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtResComptCntyNo()
	{
		if (ivjtxtResComptCntyNo == null)
		{
			try
			{
				ivjtxtResComptCntyNo = new RTSInputField();
				ivjtxtResComptCntyNo.setBounds(163, 0, 33, 20);
				ivjtxtResComptCntyNo.setName("ivjtxtResComptCntyNo");
				ivjtxtResComptCntyNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtResComptCntyNo.setText(DEFLT_CNTY);
				ivjtxtResComptCntyNo.setBackground(
					java.awt.Color.white);
				ivjtxtResComptCntyNo.setEnabled(true);
				ivjtxtResComptCntyNo.setInput(
					RTSInputField.NUMERIC_ONLY);
				// defect 10127 
				ivjtxtResComptCntyNo.setMaxLength(
					CommonConstant.LENGTH_OFFICE_ISSUANCENO);
				// end defect 10127 
				ivjtxtResComptCntyNo.setEditable(true);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtResComptCntyNo;
	}

	/**
	 * Return the ivjtxtVehicleLocationCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationCity()
	{
		if (ivjtxtVehicleLocationCity == null)
		{
			try
			{
				ivjtxtVehicleLocationCity = new RTSInputField();
				ivjtxtVehicleLocationCity.setSize(135, 20);
				ivjtxtVehicleLocationCity.setName(
					"ivjtxtVehicleLocationCity");
				ivjtxtVehicleLocationCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationCity.setLocation(8, 328);
				ivjtxtVehicleLocationCity.setText("");
				ivjtxtVehicleLocationCity.setInput(
					RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtVehicleLocationCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtVehicleLocationCity;
	}

	/**
	 * Return the ivjtxtVehicleLocationState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationState()
	{
		if (ivjtxtVehicleLocationState == null)
		{
			try
			{
				ivjtxtVehicleLocationState = new RTSInputField();
				ivjtxtVehicleLocationState.setBounds(150, 328, 25, 20);
				ivjtxtVehicleLocationState.setName(
					"ivjtxtVehicleLocationState");
				ivjtxtVehicleLocationState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationState.setInput(
					RTSInputField.ALPHA_ONLY);
				// defect 10127
				ivjtxtVehicleLocationState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtVehicleLocationState;
	}

	/**
	 * Return the ivjtxtVehicleLocationStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationStreet1()
	{
		if (ivjtxtVehicleLocationStreet1 == null)
		{
			try
			{
				ivjtxtVehicleLocationStreet1 = new RTSInputField();
				ivjtxtVehicleLocationStreet1.setSize(269, 20);
				ivjtxtVehicleLocationStreet1.setName(
					"ivjtxtVehicleLocationStreet1");
				ivjtxtVehicleLocationStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationStreet1.setInput(
					RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtVehicleLocationStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10127 
				ivjtxtVehicleLocationStreet1.setLocation(8, 280);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtVehicleLocationStreet1;
	}

	/**
	 * Return the ivjtxtVehicleLocationStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationStreet2()
	{
		if (ivjtxtVehicleLocationStreet2 == null)
		{
			try
			{
				ivjtxtVehicleLocationStreet2 = new RTSInputField();
				ivjtxtVehicleLocationStreet2.setSize(269, 20);
				ivjtxtVehicleLocationStreet2.setName(
					"ivjtxtVehicleLocationStreet2");
				ivjtxtVehicleLocationStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationStreet2.setInput(
					RTSInputField.DEFAULT);
				// defect 10127 
				ivjtxtVehicleLocationStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10127 
				// user code begin {1}
				ivjtxtVehicleLocationStreet2.setLocation(8, 304);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtVehicleLocationStreet2;
	}

	/**
	 * Return the ivjtxtVehicleLocationZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationZpcd()
	{
		if (ivjtxtVehicleLocationZpcd == null)
		{
			try
			{
				ivjtxtVehicleLocationZpcd = new RTSInputField();
				ivjtxtVehicleLocationZpcd.setBounds(183, 328, 45, 20);
				ivjtxtVehicleLocationZpcd.setName(
					"ivjtxtVehicleLocationZpcd");
				ivjtxtVehicleLocationZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLocationZpcd.setInput(
					RTSInputField.NUMERIC_ONLY);
				// defect 10127
				ivjtxtVehicleLocationZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// end defect 10127 
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtVehicleLocationZpcd;
	}

	/**
	 * Return the ivjtxtVehicleLocationZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleLocationZpcdP4()
	{
		if (ivjtxtVehicleLocationZpcdP4 == null)
		{
			try
			{
				ivjtxtVehicleLocationZpcdP4 = new RTSInputField();
				ivjtxtVehicleLocationZpcdP4.setBounds(243, 328, 34, 20);
				ivjtxtVehicleLocationZpcdP4.setName(
					"ivjtxtVehicleLocationZpcdP4");
				ivjtxtVehicleLocationZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

				// defect 10127 
				// RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtVehicleLocationZpcdP4.setInput(
					RTSInputField.NUMERIC_ONLY);

				ivjtxtVehicleLocationZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// end defect 10127

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtVehicleLocationZpcdP4;
	}

	/**
	 * Return the ivjtxtUnitNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleUnitNo()
	{
		if (ivjtxtVehicleUnitNo == null)
		{
			try
			{
				ivjtxtVehicleUnitNo = new RTSInputField();
				ivjtxtVehicleUnitNo.setSize(49, 20);
				ivjtxtVehicleUnitNo.setName("ivjtxtUnitNo");
				ivjtxtVehicleUnitNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleUnitNo.setText("");
				ivjtxtVehicleUnitNo.setInput(RTSInputField.DEFAULT);
				ivjtxtVehicleUnitNo.setMaxLength(
					CommonConstant.LENGTH_UNIT_NO);
				// user code begin {1}
				ivjtxtVehicleUnitNo.setLocation(129, 358);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtVehicleUnitNo;
	}

	/**
	 * Process the cancel button or escape key event.
	 * 
	 * @return boolean
	 */
	private boolean handleCancel()
	{
		boolean lbReturn = true;

		if (getController()
			.getTransCode()
			.equals(TransCdConstant.ADDR))
		{
			RTSException leRTSException =
				new RTSException(
					RTSException.CTL001,
					ERRMSG_DYWT_CANCEL,
					null);

			lbReturn =
				leRTSException.displayError(this) == RTSException.YES;
		}
		return lbReturn;
	}

	/**
	 * Manage county of residence field.
	 * 
	 */
	private void handleCntyRes()
	{
		// Adjust county of residence depending on transaction
		String lsTransCd = null;
		if (caVehicleInquiryData.getMfDown() == 0)
		{
			lsTransCd = caRegValidData.getTransCode();
			gettxtResComptCntyNo().setText(
				Integer.toString(
					caRegistrationData.getResComptCntyNo()));
		}
		else
		{
			lsTransCd = getController().getTransCode();
			if (caRegistrationData.getResComptCntyNo() == 0)
			{
				if (SystemProperty.getOfficeIssuanceNo()
					<= MAX_CNTY_NO)
				{
					gettxtResComptCntyNo().setText(
						Integer.toString(
							SystemProperty.getOfficeIssuanceNo()));
				}
				else
				{
					gettxtResComptCntyNo().setText("0");
				}
			}
			else
			{
				gettxtResComptCntyNo().setText(
					Integer.toString(
						caRegistrationData.getResComptCntyNo()));
			}
		}

		// The following has been modified per Issues Logged During
		// Phase I document. Undo modification per Issues document;
		// refer to defect CQU100003839

		// Test if workstation is located in COUNTY
		if (SystemProperty.isCounty())
		{
			if (lsTransCd.equals(TransCdConstant.RENEW)
				|| lsTransCd.equals(TransCdConstant.PAWT)
				|| lsTransCd.equals(TransCdConstant.EXCH)
				|| lsTransCd.equals(TransCdConstant.CORREG))
			{
				// set apprehended resident county number if not specified
				if (caRegValidData.getApprndComptCntyNo() == 0)
				{
					caRegistrationData.setResComptCntyNo(
						SystemProperty.getOfficeIssuanceNo());
				}

				gettxtResComptCntyNo().setEnabled(false);
				getstcLblCounty().setEnabled(false);
			}
			else
			{
				gettxtResComptCntyNo().setEnabled(true);
				getstcLblCounty().setEnabled(true);
			}
			gettxtResComptCntyNo().setText(
				Integer.toString(
					caRegistrationData.getResComptCntyNo()));
		}
	}

	/**
	 * Get data from mfVehicleData to display.
	 */
	private void handleDataToDisplay()
	{
		String lsTransCd = caRegValidData.getTransCode();

		// defect 10127 
		setOwnerDataToDisplay();
		setNonOwnerInfoToDisplay();

		handleCntyRes();
		gettxtVehicleUnitNo().setText(caTitleData.getVehUnitNo());

		// Display request renewal notice checkbox if ADDR or RNR trans
		// defect 8404
		// Check for Reg Period Length. It must be twelve months or more
		// to qualify for Renewal Notice.
		if ((lsTransCd.equals(TransCdConstant.ADDR)
			|| lsTransCd.equals(TransCdConstant.RNR))
			&& isRPLTwelveMonths())
		{
			getchkRequestRenewalMailing().setVisible(true);
			getchkRequestRenewalMailing().setSelected(
				caRegValidData.isRenwlReq());
		}
		else
		{
			getchkRequestRenewalMailing().setVisible(false);
			// defect 6494
			// Disable Request Renewal Notice checkbox
			getchkRequestRenewalMailing().setEnabled(false);
			// end defect 6494
		}
		// end defect 8404

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
	private void initialize()
	{
		try
		{
			// user code begin {1}
			setRequestFocus(false);
			// defect 10112 
			// set check box array
			//			checkBoxes = new JCheckBox[3];
			//			checkBoxes[0] = getbtnDeleteRecipient();
			//			checkBoxes[1] = getbtnDeleteRenewal();
			//			checkBoxes[2] = getbtnDeleteLocation();
			// end defect 10112 
			// user code end
			setName("FrmOwnerAddressREG033");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(628, 498);
			setTitle(TITLE_REG033);
			setContentPane(getFrmOwnerAddressREG033ContentPane1());
		}
		catch (Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		// user code begin {2}
		//		defect 10127 
		caOwnrNameAddrComp =
			new NameAddressComponent(
				gettxtOwnerName1(),
				gettxtOwnerName2(),
				gettxtOwnerStreet1(),
				gettxtOwnerStreet2(),
				gettxtOwnerCity(),
				gettxtOwnerState(),
				gettxtOwnerZpcd(),
				gettxtOwnerZpcdP4(),
				gettxtOwnerCntry(),
				gettxtOwnerCntryZpcd(),
				getchkUSA(),
				getstcLblDash1(),
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID,
				ErrorsConstant.ERR_NUM_INCOMPLETE_ADDR_DATA,
				CommonConstant.TX_DEFAULT_STATE);

		caRenwlAddrComp =
			new NameAddressComponent(
				gettxtRenewalMailingStreet1(),
				gettxtRenewalMailingStreet2(),
				gettxtRenewalMailingCity(),
				gettxtRenewalMailingState(),
				gettxtRenewalMailingZpcd(),
				gettxtRenewalMailingZpcdP4(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_REN_ADDR_DATA);

		caVehLocAddrComp =
			new NameAddressComponent(
				gettxtVehicleLocationStreet1(),
				gettxtVehicleLocationStreet2(),
				gettxtVehicleLocationCity(),
				gettxtVehicleLocationState(),
				gettxtVehicleLocationZpcd(),
				gettxtVehicleLocationZpcdP4(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_VEH_ADDR_DATA);
		// end defect 10127 

		// defect 10299  
		cvAddlMFValid1 = new Vector();
		cvAddlMFValid1.add(gettxtOwnerName1());
		cvAddlMFValid1.add(gettxtOwnerName2());
		cvAddlMFValid2 = new Vector();
		cvAddlMFValid2.add(gettxtRenewalRecipientName());
		// end defect 10299 

		addWindowListener(this);
	}

	/**
	 * Check if Reg Period Length is twelve months or more.
	 * 
	 * @return boolean
	 */
	private boolean isRPLTwelveMonths()
	{
		int liRTSEffDate = new RTSDate().getYYYYMMDDDate();
		int liRegClassCd = caRegistrationData.getRegClassCd();
		caCommFeesData =
			CommonFeesCache.getCommonFee(liRegClassCd, liRTSEffDate);

		// defect 10112 
		return caCommFeesData != null
			&& caCommFeesData.getRegPeriodLngth() >= 12;
		// end defect 10112 
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

		// defect 10127 
		if (aaIE.getSource() instanceof JCheckBox)
		{
			JCheckBox laChkbx = (JCheckBox) aaIE.getSource();

			if (laChkbx == getchkUSA() & isSetDataFinished())
			{
				// defect 10127 
				caOwnrNameAddrComp.resetPerUSA(laChkbx.isSelected());
				// end defect 10127
			}
			// defect 10508
			else if (
				aaIE.getSource() == getchkEReminder()
					&& aaIE.getStateChange() == ItemEvent.DESELECTED
					&& gettxtRecpntEMail().isEmpty())
			{
				clearAllColor(gettxtRecpntEMail());
			}
			// end defect 10508
			else if (laChkbx.isSelected())
			{
				// defect 10592 
				//	if (laChkbx == getchkDeleteRecipient())
				//	{
				//		gettxtRenewalRecipientName().setText("");
				//	}
				//	else if (laChkbx == getchkDeleteLocation())
				//	{
				//		caVehLocAddrComp.resetNonOwnerAddr();
				//	}
				//	else if (laChkbx == getchkDeleteRenewal())
				//	{
				//		caRenwlAddrComp.resetNonOwnerAddr();
				//	}
				//	else			
				// end defect 10592 

				if (laChkbx == getchkRequestRenewalMailing())
				{
					if (!validateReqRenwlNotc())
					{
						getchkRequestRenewalMailing().setSelected(
							false);
						getchkRequestRenewalMailing().requestFocus();
					}
				}
			}
			// defect 10508
			else if (
				aaIE.getSource() == getchkEReminder()
					&& aaIE.getStateChange() == ItemEvent.DESELECTED
					&& gettxtRecpntEMail().isEmpty())
			{
				clearAllColor(gettxtRecpntEMail());
			}
			// end defect 10508
		}
		// end defect 10127 
	}

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyChar() == KeyEvent.VK_ESCAPE)
		{
			if (!handleCancel())
			{
				return;
			}
			getController().processData(
				AbstractViewController.CANCEL,
				caVehicleInquiryData);

		}
		else
		{
			setupDeleteButtons();
		}
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
			// defect 10112 
			csTransCd = getController().getTransCode();
			// end defect 10112 

			caVehicleInquiryData = (VehicleInquiryData) aaDataObject;
			caMFVehicleData = caVehicleInquiryData.getMfVehicleData();
			caOwnerData = caMFVehicleData.getOwnerData();
			caTitleData = caMFVehicleData.getTitleData();
			caRegistrationData = caMFVehicleData.getRegData();
			caOwnerAddress = caOwnerData.getAddressData();
			
			if (caVehicleInquiryData.getValidationObject()
				instanceof RegistrationValidationData)
			{
				caRegValidData =
					(RegistrationValidationData) caVehicleInquiryData
						.getValidationObject();
			}
			else if (
				caVehicleInquiryData.getValidationObject()
					instanceof Object)
			{
				// mainframe unavailable scenario
				Object laObj =
					caVehicleInquiryData.getValidationObject();
				caVehicleInquiryData.setValidationObject(
					new RegistrationValidationData());
				caRegValidData =
					(RegistrationValidationData) caVehicleInquiryData
						.getValidationObject();
				if (laObj instanceof RegistrationModifyData)
				{
					caVehicleInquiryData.setValidationObject(laObj);
				}
				// defect 10112 
				caRegValidData.setTransCode(csTransCd);
				// end defect 10112 
			}

			handleDataToDisplay();

			// MF Available
			// Disable OwnerId, Name1, Name2  
			if (caVehicleInquiryData.isMFUp())
			{
				gettxtOwnerId().setEnabled(false);
				gettxtOwnerName1().setEnabled(false);
				gettxtOwnerName2().setEnabled(false);
				getstcLblOwnerId().setEnabled(false);
				getstcLblOwnerName().setEnabled(false);

				// TODO (KPH) This is weird 
				if (caRegValidData
					.getTransCode()
					.equals(TransCdConstant.RNR))
				{
					caRegValidData.setTransCode(TransCdConstant.ADDR);
				}

				// defect 10299
				if (validateFields())
				{
					setDefaultFocusField(gettxtOwnerStreet1());
				}
				// end defect 10299 
			}
			// MF Not Available 
			else
			{
				// defect 10130 
				gettxtOwnerId().setEnabled(SystemProperty.isHQ());
				getstcLblOwnerId().setEnabled(SystemProperty.isHQ());
				// end defect 10130 

				// 1st Time through - int to TX 
				if (gettxtOwnerState().isEmpty())
				{
					gettxtOwnerState().setText(CommonConstant.STR_TX);
				}
				setDefaultFocusField(gettxtOwnerId());
			}

			// Removed - also done above 
			// defect 8494
			// Moved from windowOpened
			//			if (caVehicleInquiryData.getMfDown() == 0)
			//			{
			//				setDefaultFocusField(gettxtOwnerStreet1());
			//			}
			//			else
			//			{
			//				setDefaultFocusField(gettxtOwnerId());
			//			}
			// end defect 8494
			// end defect 10127

			// defect 10592 
			setupDeleteButtons();
			// end defect 10592 
		}
	}

	/**
	 * Set Data to Data Object 
	 * 
	 */
	private void setDataToDataObject()
	{
		// defect 10299 
		// Moved to validateFields()
		//UtilityMethods.trimRTSInputField(this);
		// end defect 10299

		// OwnerId		
		caOwnerData.setOwnrId(gettxtOwnerId().getText());

		// Owner Name/Addr 
		caOwnrNameAddrComp.setNameAddressToDataObject(caOwnerData);

		// RegValidData RenwlReq 
		caRegValidData.setRenwlReq(
			getchkRequestRenewalMailing().isSelected());

		// Recipient Name 
		caRegistrationData.setRecpntName(
			gettxtRenewalRecipientName().getText());

		// Renewal Mail Address
		caRenwlAddrComp.setAddressToDataObject(
			caRegistrationData.getRenwlMailAddr());

		// defect 10372 
		caRegistrationData.setRecpntEMail(
			gettxtRecpntEMail().getText());
		// end defect 10372 

		// Vehicle Location 
		caVehLocAddrComp.setAddressToDataObject(
			caTitleData.getTtlVehAddr());

		// ResComptCntyNo
		caRegistrationData.setResComptCntyNo(
			Integer.parseInt(gettxtResComptCntyNo().getText()));

		// VehUnitNo 
		caTitleData.setVehUnitNo(gettxtVehicleUnitNo().getText());
		// end defect 10127 

		// defect 10508 
		int liEMailRenwlReqCd = getchkEReminder().isSelected() ? 1 : 0;
		caRegistrationData.setEMailRenwlReqCd(liEMailRenwlReqCd);
		// end defect 10508 
	}

	/**
	 * Set Renwl Name, Addr, Veh Loc Addr, Recipient EMail Address 
	 *
	 */
	private void setNonOwnerInfoToDisplay()
	{
		// From RegistrationData  - Renewal Name/Addr 	
		gettxtRenewalRecipientName().setText(
			caRegistrationData.getRecpntName());

		// Renewal Address 
		caRenwlAddrComp.setAddressDataToDisplay(
			caRegistrationData.getRenwlMailAddr());

		// defect 10372 
		// Renewal EMail Address 
		if (caRegistrationData.getRecpntEMail() != null)
		{
			gettxtRecpntEMail().setText(
				caRegistrationData.getRecpntEMail());
		}
		// end defect 10372 

		// defect 10508
		getchkEReminder().setSelected(caRegistrationData.isEReminder());
		// end defect 10508  

		// From TitleData - Vehicle Location
		caVehLocAddrComp.setAddressDataToDisplay(
			caTitleData.getTtlVehAddr());
	}

	/**
	 * Set Owner Name and Address  
	 * 
	 * @param aaOwnerData
	 */
	private void setOwnerDataToDisplay()
	{
		gettxtOwnerId().setText(caOwnerData.getOwnrId());

		caOwnrNameAddrComp.setNameAddressDataToDisplay(caOwnerData);
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
	 * Enable/Disable Delete Buttons per data
	 * 
	 */
	private void setupDeleteButtons()
	{
		getbtnDeleteRecipient().setEnabled(
			!(gettxtRenewalRecipientName().isEmpty()));
		getbtnDeleteEMail().setEnabled(
			!(gettxtRecpntEMail().isEmpty()));
		getbtnDeleteRenewal().setEnabled(
			!caRenwlAddrComp.isUSAAddressEmpty());
		getbtnDeleteLocation().setEnabled(
			!caVehLocAddrComp.isUSAAddressEmpty());
	}

	/**
	 * Validate EReminder 
	 * 
	 * @return boolean 
	 */
	private void validateEReminder(RTSException aeRTSEx)
	{
		if (getchkEReminder().isSelected()
			&& gettxtRecpntEMail().isEmpty())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtRecpntEMail());
		}
	}

	/**
	* Validate Fields() 
	* 
	* @return boolean
	*/
	private boolean validateFields()
	{
		// defect 10299 
		// Moved from setDataToDataObject()
		UtilityMethods.trimRTSInputField(this);
		// end defect 10299 

		RTSException leRTSEx = new RTSException();

		boolean lbValid = true;

		// defect 10189 
		validateOwnrId(leRTSEx);
		// end defect 10189 

		validateOwnrName1(leRTSEx);
		// defect 10299
		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvAddlMFValid1,
			leRTSEx);
		// end defect 10299

		// defect 10127 
		caOwnrNameAddrComp.validateAddressFields(leRTSEx);

		// defect 10299
		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvAddlMFValid2,
			leRTSEx);
		// end defect 10299

		validateRenwlMailngAddr(leRTSEx);

		// defect 10508 
		validateEReminder(leRTSEx);
		// end defect 10508

		// defect 10372 
		validateRecpntEMail(leRTSEx);
		// end defect 10372  

		validateVehLocAddr(leRTSEx);

		validateResComptCntyNo(leRTSEx);
		// end defect 10127  

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
	}

	/**
	 * Validate owner name does not contain SSN.
	 * 
	 * @return boolean
	 */
	private boolean validateNameWithNoSSN()
	{
		// Ensure Recipient Name field is without SSN
		String lsRecpntName = gettxtRenewalRecipientName().getText();

		boolean lbValid = true;

		if (CommonValidations.isStringWithSSN(lsRecpntName))
		{
			VehMiscData laVehMiscData =
				caVehicleInquiryData.getVehMiscData();

			if (UtilityMethods.isEmpty(laVehMiscData.getAuthCd()))
			{
				getController().processData(
					VCOwnerAddressREG033.VTR_SSN_AUTHORIZATION,
					caVehicleInquiryData);
			}
			if (UtilityMethods.isEmpty(laVehMiscData.getAuthCd()))
			{
				lbValid = false;
				(
					(RegistrationValidationData) caVehicleInquiryData
						.getValidationObject())
						.setVTRAuth(
					lbValid);
			}
		}
		return lbValid;
	}

	/**
	 * Validate OwnerId if enabled  
	 * 
	 * @param aeRTSEx
	 */
	private void validateOwnrId(RTSException aeRTSEx)
	{
		if (gettxtOwnerId().isEnabled()
			&& !gettxtOwnerId().isValidOwnerId())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtOwnerId());
		}
	}

	/**
	 * Validate owner name is complete.
	 * 
	 * @param aeRTSEx RTSException 
	 */
	private void validateOwnrName1(RTSException aeRTSEx)
	{
		// defect 10189 
		// Modified to use isEnabled()  
		//if (caVehicleInquiryData.isMFDown()
		if (gettxtOwnerName1().isEnabled()
			&& gettxtOwnerName1().isEmpty())
		{
			// end defect 10189 

			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtOwnerName1());
		}
	}

	/**
	 * Validate Recipient EMail Address 
	 * 
	 * @param aeRTSEx
	 */
	private void validateRecpntEMail(RTSException aeRTSEx)
	{
		// Validate EMail Address if supplied
		if (!CommonValidations
			.isValidEMail(gettxtRecpntEMail().getText()))
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtRecpntEMail());
		}
	}

	/**
	 * Validate owner address is complete.
	 * 
	 * @param aeRTSEx RTSException 
	 */
	private void validateRenwlMailngAddr(RTSException aeRTSEx)
	{
		// defect 10127 
		if (!caRenwlAddrComp.isUSAAddressEmpty())
		{
			caRenwlAddrComp.validateAddressFields(aeRTSEx);
		}
		// end defect 10127 
	}

	/**
	 * validate if renewal can be printed.
	 * 
	 * @return boolean
	 */
	private boolean validateReqRenwlNotc()
	{
		// defect 9436
		// Test if vehicle record is current                                                                  
		//		if (caRegValidData.getRegistrationExpired() == 1)
		//		{
		//			RTSException leRTSEx =
		//				new RTSException(738, new String[] { ERRMSG_REG_EXP });
		//			leRTSEx.displayError(this);
		//			return false;
		//		}
		//
		//		// if current date is within 3 months before renewal date
		//		RTSDate laRTSDateCurrent = RTSDate.getCurrentDate();
		//
		//		if (2
		//			< 12
		//				* (caRegistrationData.getRegExpYr()
		//					- laRTSDateCurrent.getYear())
		//				+ (caRegistrationData.getRegExpMo()
		//					- laRTSDateCurrent.getMonth()))
		//		{
		//			RTSException leRTSEx =
		//				new RTSException(
		//					738,
		//					new String[] { ERRMSG_REG_IN_FUTURE });
		//			leRTSEx.displayError(this);
		//			return false;
		//		}
		// end defect 9436
		// Test if vehicle/regis class and plate type are valid combo
		RegistrationClassData laRegClassData =
			RegistrationClassCache.getRegisClass(
				caMFVehicleData.getVehicleData().getVehClassCd(),
				caRegistrationData.getRegClassCd(),
				caVehicleInquiryData.getRTSEffDt());

		if (laRegClassData == null)
		{
			// 738
			RTSException leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_CANNOT_PRINT_RENEWAL_NOTICE,
					new String[] { ERRMSG_CPS_INVALID });
			leRTSEx.displayError(this);
			return false;
		}

		// defect 9670 
		if (caRegistrationData.getPltRmvCd() != 0)
		{
			// 738 
			RTSException leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_CANNOT_PRINT_RENEWAL_NOTICE,
					new String[] { PLATE_REMOVED });
			leRTSEx.displayError(this);
			return false;
		}
		// end defect 9670 

		// Validate regis class/plate type/sticker type combination
		try
		{
			int liRetVal =
				RegistrationVerify.verifyClassPltStkrCombination(
					caVehicleInquiryData);

			if (liRetVal == -1)
			{
				// 738 
				RTSException leRTSEx =
					new RTSException(
						ErrorsConstant
							.ERR_NUM_CANNOT_PRINT_RENEWAL_NOTICE,
						new String[] { ERRMSG_CPS_INVALID });
				leRTSEx.displayError(this);
				return false;
			}
		}

		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			return false;
		}

		// test if plate type is invalid.
		PlateTypeData laPlateTypeData =
			PlateTypeCache.getPlateType(
				caRegistrationData.getRegPltCd());

		// defect 9760
		// Also allow RegRenewlCd = 3
		if (laPlateTypeData.getRegRenwlCd() != 1
			&& laPlateTypeData.getRegRenwlCd() != 3)
		{
			// end defect 9760
			RTSException leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_CANNOT_PRINT_RENEWAL_NOTICE,
					new String[] { ERRMSG_PLATE_TYPE_INVALID });
			leRTSEx.displayError(this);
			return false;
		}
		try
		{
			RegistrationVerify.verifyValidSpclPlt(
				caVehicleInquiryData,
				TransCdConstant.RNR);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			return false;
		}

		// Test if there is an outstanding reg or title hot check
		if (caRegistrationData.getRegHotCkIndi() != 0
			|| caTitleData.getTtlHotCkIndi() != 0)
		{
			RTSException leRTSEx =
				new RTSException(
					231,
					new String[] { ERRMSG_REN_CANNOT_PRINT });
			leRTSEx.displayError(this);
			return false;
		}

		// Test for RenwlMailRtrnIndi and check if address has changed
		if (caRegistrationData.getRenwlMailRtrnIndi() != 0)
		{
			if (caRegValidData
				.getOrigOwnerData()
				.getAddressData()
				.getSt1()
				.equalsIgnoreCase(gettxtOwnerStreet1().getText())
				&& caRegValidData
					.getOrigOwnerData()
					.getAddressData()
					.getSt2()
					.equalsIgnoreCase(gettxtOwnerStreet2().getText())
				&& caRegValidData
					.getOrigOwnerData()
					.getAddressData()
					.getCity()
					.equalsIgnoreCase(gettxtOwnerCity().getText())
				&& caRegValidData
					.getOrigOwnerData()
					.getAddressData()
					.getState()
					.equalsIgnoreCase(gettxtOwnerState().getText())
				&& caRegValidData
					.getOrigOwnerData()
					.getAddressData()
					.getCntry()
					.equalsIgnoreCase(gettxtOwnerCntry().getText())
				&& caRegValidData
					.getOrigOwnerData()
					.getAddressData()
					.getZpcd()
					.equalsIgnoreCase(gettxtOwnerZpcd().getText())
				&& caRegValidData
					.getOrigOwnerData()
					.getAddressData()
					.getZpcdp4()
					.equalsIgnoreCase(gettxtOwnerZpcdP4().getText())
				&& caRegValidData
					.getOrigRenwlMailAddr()
					.getSt1()
					.equalsIgnoreCase(
					gettxtRenewalMailingStreet1().getText())
				&& caRegValidData
					.getOrigRenwlMailAddr()
					.getSt2()
					.equalsIgnoreCase(
					gettxtRenewalMailingStreet2().getText())
				&& caRegValidData
					.getOrigRenwlMailAddr()
					.getCity()
					.equalsIgnoreCase(
					gettxtRenewalMailingCity().getText())
				&& caRegValidData
					.getOrigRenwlMailAddr()
					.getState()
					.equalsIgnoreCase(
					gettxtRenewalMailingState().getText())
				&& caRegValidData
					.getOrigRenwlMailAddr()
					.getZpcd()
					.equalsIgnoreCase(
					gettxtRenewalMailingZpcd().getText())
				&& caRegValidData
					.getOrigRenwlMailAddr()
					.getZpcdp4()
					.equalsIgnoreCase(
					gettxtRenewalMailingZpcdP4().getText()))
			{
				RTSException leRTSEx =
					new RTSException(
						738,
						new String[] { ERRMSG_VERIFY_ADDR });
				leRTSEx.displayError(this);
				return false;
			}
		}

		// Test if renewal can be printed based on indis on vehicle rec
		Vector lvIndis =
			IndicatorLookup.getIndicators(
				caMFVehicleData,
				TransCdConstant.RNR,
				IndicatorLookup.SCREEN);
		if (IndicatorLookup.hasHardStop(lvIndis))
		{
			RTSException leRTSEx =
				new RTSException(
					738,
					new String[] { ERRMSG_REFER_TO_REMARKS });
			leRTSEx.displayError(this);
			return false;
		}
		// defect 9436
		// only test dates if every thing else is ok.
		// Test if vehicle record is current                                                                  
		if (caRegValidData.getRegistrationExpired() == 1)
		{
			RTSException leRTSEx =
				new RTSException(738, new String[] { ERRMSG_REG_EXP });
			leRTSEx.displayError(this);
			return false;
		}

		// if current date is within 3 months before renewal date
		RTSDate laRTSDateCurrent = RTSDate.getCurrentDate();

		if (2
			< 12
				* (caRegistrationData.getRegExpYr()
					- laRTSDateCurrent.getYear())
				+ (caRegistrationData.getRegExpMo()
					- laRTSDateCurrent.getMonth()))
		{
			RTSException leRTSEx =
				new RTSException(
					738,
					new String[] { ERRMSG_REG_IN_FUTURE });
			leRTSEx.displayError(this);
			return false;
		}
		// end defect 9436

		caRegValidData.setRenwlReq(true);
		return true;
	}

	/**
	 * 
	 * ValidateResComptCntyNo
	 * 
	 * @param aeRTSEx
	 */
	private void validateResComptCntyNo(RTSException aeRTSEx)
	{
		if (!gettxtResComptCntyNo().isValidCountyNo())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtResComptCntyNo());
		}
	}

	/**
	 * Validate owner address is complete.
	 * 
	 * @param aeRTSEx RTSException 
	 */
	private void validateVehLocAddr(RTSException aeRTSEx)
	{
		// defect 10127 
		if (!caVehLocAddrComp.isUSAAddressEmpty())
		{
			caVehLocAddrComp.validateAddressFields(aeRTSEx);
		}
		// end defect 10127 
	}

	/**
	 * Confirm Resident County if owner address has changed.
	 * 
	 * @return boolean
	 * @param aeRTSEx RTSException 
	 */
	private boolean verifyResComptCntyNo()
	{
		boolean lbValid = true;

		// Test if resident county has changed or
		// test if owner address has changed
		if (caRegValidData.getOrigResComptCntyNo()
			!= Integer.parseInt(gettxtResComptCntyNo().getText())
			|| (caRegValidData.getOrigOwnerData() == null)
			|| (!caRegValidData
				.getOrigOwnerData()
				.getAddressData()
				.getSt1()
				.equalsIgnoreCase(gettxtOwnerStreet1().getText())
				|| !caRegValidData
					.getOrigOwnerData()
					.getAddressData()
					.getSt2()
					.equalsIgnoreCase(gettxtOwnerStreet2().getText())
				|| !caRegValidData
					.getOrigOwnerData()
					.getAddressData()
					.getCity()
					.equalsIgnoreCase(gettxtOwnerCity().getText())
				|| !caRegValidData
					.getOrigOwnerData()
					.getAddressData()
					.getState()
					.equalsIgnoreCase(gettxtOwnerState().getText())))
		{
			// The following has been modified per Issues Logged During
			// Phase I document. Undo modification per Issues document;
			// refer to defect CQU100003839
			if (!csTransCd.equals(TransCdConstant.RENEW)
				&& !csTransCd.equals(TransCdConstant.PAWT)
				&& !csTransCd.equals(TransCdConstant.EXCH)
				&& !csTransCd.equals(TransCdConstant.CORREG)
				|| !SystemProperty.isCounty()
				|| caRegValidData.getApprndComptCntyNo() != 0)
			{
				// defect 10112 
				if (!confirmResComptCntyNo())
				{
					lbValid = false;
					// TODO This doesn't work 
					gettxtResComptCntyNo().requestFocus();
				}
				// end defect 10112 
			}
		}
		return lbValid;
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
