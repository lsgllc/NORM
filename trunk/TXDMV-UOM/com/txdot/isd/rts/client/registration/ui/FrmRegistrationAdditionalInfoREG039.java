package com.txdot.isd.rts.client.registration.ui;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.registration.business.RegistrationClientBusiness;
import com.txdot.isd.rts.client.registration.business.RegistrationClientUtilityMethods;
import com.txdot.isd.rts.client.registration.business.RegistrationSpecialExemptions;
import com.txdot.isd.rts.client.registration.business.RegistrationVerify;
import com.txdot.isd.rts.client.title.ui.FrmCountyConfirmCTL002;

import com.txdot.isd.rts.services.cache.CommonFeesCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *  FrmRegistrationAdditionalInfoREG039.java
 *
 * (c) Texas Department of Transportation  2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name   		Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and
 * 							doneWorking()
 * Taylor, Kwik	05/28/2002	Defect 4073 - clear apprend mo and yr when
 * 							apprend cnty no is cleared.
 * MAbs			06/03/2002	Enabled key presses on Cancelled reason
 * 							CQU100004204
 * MAbs			06/26/2002	Allowed focus to go to right place rather
 * 							than hard coding first component
 * 							CQU100004348
 * Robin &&  	08/02/2002  Fixing 4480. Made changes to saveInputs
 * Govindappa				method to save the RegExpiredReason
 * 							irrespective of whether the reg expiration
 * 							reason panel was enabled or not.
 * B Arredondo	12/03/2002	Made changes to user help guide so had to
 * 							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the
 * 							user help guide so had to make changes
 *							in actionPerformed().
 * B Brown &&	02/13/2003  Fixed defect #5233 by commenting out the
 * Sunil G				    focus gained code, and removed the
 *                          nextFocusableComponent from the vaild and
 * 							invalid radio buttons. In this way the
 * 							forward and back tab will hit all three of
 * 							the "resson for expired registration" radio
 *                          buttons. This is consistent with behavior
 * 							on other frames.
 * B Hargrove	04/12/2005	Added JPanels to allow tabbing down left
 * 							side before tabbing down right side.
 * 							Fully defined calls to  
 * 							javax.swing.border.EtchedBorder() and
 * 							TitledBorder() to remove warning.  
 *							added jPanel, jPanel1
 *							modify getJPanel1(),getJPanel2(),
 *							getJPanel3(), getJPanel4()  
 *							defect 7894 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7899  Ver 5.2.3                  
 * B Hargrove	06/23/2005	Modify code for move to Java 1.4.
 * 							Bring code to standards. Comment out unused
 * 							variables and methods.
 * 							modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete implements KeyListener
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	07/18/2005	Refactor\Move 
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	11/13/2005	Reevaluate Verify Heavy Use Tax after change 
 *							registration. Default to valid reason & 
 *							disable when Seasonal Ag.
 *							add cbSeasonalAg,cbChangeReg
 *							modify setData(),actionPerformed()
 *							defect 8404 Ver 5.2.2 Fix 7 
 * K Harrell	11/13/2005	Default to invalid reason when not Seasonal
 *							Ag 
 *							modify setData()
 *							defect 8390 Ver 5.2.2 Fix 7
 * K Harrell	11/28/2005	Check for null CommonFeesData
 *							modify setData()
 *							defect 8437 Ver 5.2.2 Fix 7   
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.  This was done for 
 * 							both the Radio Button.  For check boxes I 
 * 							added them to an RTSButtonGroup and let that
 * 							class handle the arrowing.
 * 							remove keyPressed(), carrCheckBoxes, 
 * 								ciCheckBoxWithFocus
 * 							modify getJPanel2(), initialize(), 
 * 							defect 7894 Ver 5.2.3
 * B Hargrove	02/03/2006	Since Seasonal Ag Combo (RegClassCd 76) was
 *							created, change to look up Emission Fee
 *							Percent from CommonFees table.
 *							add caCommFeesData, ZERODOLLAR
 *							modify handleChangeReg()
 *							delete COMBINATION_REG_CLASS_CD
 *							defect 8516 Ver 5.2.2 Fix 8
 * B Hargrove 	09/25/2006 	Add ExemptIndi checkbox. Handle setting of
 * 							Exempt Indi and Charge Fee Indi
 * 							add ivjchkExempt
 * 							modify actionPerformed(), getchkExempt(), 
 * 							initialize(), saveInputs(), setData()
 * 							defect 8900 Ver Exempts
 * B Hargrove 	10/09/2006 	For Renewal, do not disable Change Regis 
 * 							button if they chose Apprehended Cnty. 
 * 							modify handleChangeRegMask() 
 * 							defect 8611 Ver Exempts
 * K Harrell	10/29/2006	Update Exempt / Charge Fee Checkbox handling
 * 							delete TOKEN_TRAILER,ZERODOLLAR, 
 * 							caCommFeesData
 * 							add cbStandardExempt, cbPriorStandardExempt,
 * 							 cbSeasonalAg
 * 							add handleStandardExempts(),handleChkExempt(), 
 * 							 handleChkChargeFee(),handleRegEmissFee(),
 * 							 handleChkTknTrlrFee()
 * 							delete cbChangeReg 
 * 							delete setVehInqData(),getVehInqData(),
 * 							  setRegValidData(),getRegValidData()
 * 							modify actionPerformed(),setData(), 
 * 							 saveInputs()
 * 							defect 8900 Ver Exempts
 * K Harrell	10/30/2006	Reevaluate Verify Heavy Use Tax upon 
 * 							selection/deselection of Exempt
 * 							modify handleChkExempt()
 * 							defect 8900 Ver Exempts
 * K Harrell	11/08/2006	Set Charge Fee Indicators in 
 * 							handleChkChargeFee() & 
 * 							handleChkExempt() vs. wait for saveInputs()
 * 							modify handleCkhChargeFee()
 * 							defect 8900 Ver Exempts
 * K Harrell	03/09/2007	Use SystemProperty.isHQ()
 * 							modify validateChangeReg()
 * 							defect 9085 Ver Special Plates  
 * J Rue		03/28/2007	Restore original VehInqData if cancelled
 * 							from REG008
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	06/25/2007	Add Beep to RTSException if invalid 
 * 							Special Plate 
 * 							modify validateSpecialPlate() 
 * 							defect 9085 Ver Special Plates 
 * K Harrell	10/14/2007	Add checkbox and associated processing 
 * 							for Charge Plate Transfer Fee
 * 							add CHG_PLT_TRNSFR_FEE
 * 							add ivjchkChrgPltTrnsfrFee
 * 							add getchkChrgPltTrnsfrFee(),
 * 							 handleChkPltTrnsfrFee()
 * 							modify getJPanel2 via Visual Editor (shrunk)
 * 							modify actionPerformed(),getJPanel1(),
 * 							handleChangeReg(), handleChkChargeFee(),
 * 							handleChkExempt(), initialize(),
 * 							saveInputs(), setData()
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	11/26/2007	Make REG039 Plate Transfer Fee / New Plates
 * 							desired handling consistent w/ TTL008
 * 							Sorted members. 
 * 							add cbKeepNewPltsDisabled, cbOrigNPDMask,
 * 							 cbOrigNPDSelection, cbSavedOrigNPD 
 * 							add handleChkNewPlatesDesired()
 * 							modify setData(), actionPerformed(), 
 * 							 handleChkPltTrnsfrFee(), validateChangeReg()  
 * 							defect 9368 Ver Special Plates 2
 * K Harrell	12/03/2007	Correct validation of EffYr
 * 							modify validateEffYr()
 * 							defect 9479 Ver Special Plates 2 
 * Min Wang		07/22/2010	Add check box Verified Tow Truck
 * 							Certificate on the screen.
 * 							add ivjchkTowTruckCert, getchkTowTruckCert()
 * 							modify actionPerformed(), initialize(), 
 * 							getJPanel1(), setData()
 * 							defect 10007 Ver 6.5.0
 * Min Wang		07/28/2010	modify setData()
 * 							defect 10007 Ver 6.5.0
 * K Harrell	10/11/2011	Change wording for Plate Transfer Fee checkbox
 * 							add ivjchkPTOTrnsfr, get method 
 * 							add PTO_TRNSFR
 * 							add handleChkPTOTransfr()
 * 							delete cbKeepNewPltsDisabled
 * 							delete ivjchkPltTrnsfrFee, get method
 * 							delete CHG_PLT_TRNSFR_FEE 
 * 							delete handleChkPltTransfrFee()
 * 							modify actionPerformed(), getJPanel1(), 
 * 							 handleChkExempt(), handleChkChargeFee(),
 * 							 initialize(), setData(), setDataToDataObject(),
 * 							 saveInputs(), handleChangeReg()   
 *  						defect 11030 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */
/**
 * This form is used to collect additional details associated with a
 * registration request. A request to change in vehicle weight or change
 * registration class/plate/sticker details may be made on this form.
 *
 * @version 6.9.0  			10/11/2011
 * @author  Joseph Kwik
 * <br>Creation Date:		06/27/2001 11:20:48
 */

public class FrmRegistrationAdditionalInfoREG039
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{
	// Screen Objects  
	private RTSButton ivjbtnChangeRegistration = null;
	private RTSButton ivjbtnChangeVehicleWeight = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkChargeAddlToken = null;
	private JCheckBox ivjchkChargeEmissionsFee = null;
	private JCheckBox ivjchkChargeFee = null;
	// defect 11030 
	// Plate To Owner Transfer
	private JCheckBox ivjchkPTOTrnsfr = null;
	// end defect 11030  
	private JCheckBox ivjchkDiesel = null;
	private JCheckBox ivjchkExempt = null;
	private JCheckBox ivjchkMailInTransaction = null;
	private JCheckBox ivjchkNewPlatesDesired = null;
	private JCheckBox ivjchkVerifiedHvyVehUse = null;
	private JPanel ivjFrmRegistrationAdditionalInfoREG039ContentPane1 =
		null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel4 = null;
	private JRadioButton ivjradioCitation = null;
	private JRadioButton ivjradioInvalid = null;
	private JRadioButton ivjradioValid = null;
	private JLabel ivjstcLblApprehended = null;
	private JLabel ivjstcLblEffective = null;
	private RTSInputField ivjtxtApprndCntyNo = null;
	private RTSInputField ivjtxtEffMo = null;
	private RTSInputField ivjtxtEffYr = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;

	// boolean 
	private boolean cbPriorStandardExempt = false;
	private boolean cbSeasonalAg = false;
	private boolean cbStandardExempt = false;

	// defect 11030 
	// private boolean cbKeepNewPltsDisabled = false;
	// end defect 11030 
	
	// defect 9368 
	private boolean cbOrigNPDMask = false;
	private boolean cbOrigNPDSelection = false;
	private boolean cbSavedOrigNPD = false;
	// end defect 9368

	// int
	private int ciLastApprndComptCntyNo = 0;

	// Object 
	private RegistrationValidationData caRegValidData = null;
	private VehicleInquiryData caOrigVehData = new VehicleInquiryData();
	private VehicleInquiryData caVehInqData;

	// Constant - int 
	private final static int CNTY_NO_1 = 1;
	private final static int CNTY_NO_254 = 254;
	private final static int EXP_REG_CITATION = 3;
	private final static int EXP_REG_INVALID_REASON = 2;
	private final static int EXP_REG_VALID_REASON = 1;
	private final static int MAX_EFF_YR = 2040;
	private final static int MIN_EFF_YR = 1990;

	// Constant - String
	private final static String APPR_CNTY =
		"Apprehended Funds County No:";
	// defect 11030 
	//private final static String CHG_PLT_TRNSFR_FEE =
	//	"Charge Plate Transfer Fee";
	// end defect 11030
	private final static String CHG_REGIS = "Change Registration";
	private final static String CHG_VEH_WT = "Change Vehicle Weight";
	private final static String CHRG_ADDL_TTL_FEE =
		"Charge Add'l Token Trailer Fee";
	private final static String CHRG_FEE = "Charge Fee";
	private final static String CHRG_REG_EMI_FEE =
		"Charge Registration Emissions Fee";
	private final static String CITATION = "Citation/Charge Penalty";
	private final static String CORR_EFF_MOYR =
		"Effective Mo/Yr of Correction:";
	private final static String DIESEL = "Diesel";
	private final static String ENTER_IF_APPR = "Enter if apprehended:";
	private final static String ERRMSG_CNTY_NO =
		"Enter a valid apprehended funds county number.";
	private final static String ERRMSG_CORR_EFF_MO =
		"Enter a valid apprehended correction  effective month.";
	private final static String ERRMSG_CORR_EFF_YR =
		"Enter a valid apprehended correction effective year.";
	private final static String ERRMSG_PLATES_FROM_VTRD =
		"The registration plate type of this request requires that it be"
			+ " handled by VTRD. This registration change will be voided if you"
			+ " do not correct it.  Do you want to correct this registration change?";
	private final static String ERRMSG_VAL_APPR =
		"Validate Apprehended Data";
	private final static String EXEMPT = "Exempt";
	private final static String INVALID = "Invalid Reason";
	private final static String MAIL_IN = "Mail-In Transaction";
	private final static String MISC_VEH_CLASS_CD = "MISC";
	private final static String NEW_PLATES = "New Plates Desired";
	private final static int NON_TITLED_VEHICLE = 9;
	private final static String SELECT = "Select if needed:";
	private final static String SELECT_EXPIR =
		"Select for expired registration:";
	private final static String TITLE_REG039 =
		"Registration Additional Info   REG039";
	private final static String VALID = "Valid Reason";
	private final static String VERIFY_HVY_USE_TAX =
		"Verified Heavy Vehicle Use Tax";
	// defect 11030 
	private final static String PTO_TRNSFR = "Plate to Owner Transfer";
	// end defect 11030
	// defect 10007
	private javax.swing.JCheckBox ivjchkTowTruckCert = null;
	// end defect 10007
	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmRegistrationAdditionalInfoREG039 laFrmRegistrationAdditionalInfoREG039;
			laFrmRegistrationAdditionalInfoREG039 =
				new FrmRegistrationAdditionalInfoREG039();
			laFrmRegistrationAdditionalInfoREG039.setModal(true);
			laFrmRegistrationAdditionalInfoREG039
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmRegistrationAdditionalInfoREG039.show();
			java.awt.Insets laInsets =
				laFrmRegistrationAdditionalInfoREG039.getInsets();
			laFrmRegistrationAdditionalInfoREG039.setSize(
				laFrmRegistrationAdditionalInfoREG039.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmRegistrationAdditionalInfoREG039.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmRegistrationAdditionalInfoREG039.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmRegistrationAdditionalInfoREG039 constructor comment.
	 */
	public FrmRegistrationAdditionalInfoREG039()
	{
		super();
		initialize();
	}

	/**
	 * FrmRegistrationAdditionalInfoREG039 constructor with parent.
	 * 
	 * @param aaParent Dialog
	 */
	public FrmRegistrationAdditionalInfoREG039(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmRegistrationAdditionalInfoREG039 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmRegistrationAdditionalInfoREG039(JFrame aaParent)
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
		// Code to avoid actionPerformed being executed more than once
		// when clicking on the button multiple times.
		if (!startWorking())
		{
			return;
		}
		try
		{
			//field validation
			clearAllColor(this);
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter()
				|| aaAE.getSource() instanceof RTSInputField)
			{
				RTSException leRTSEx = new RTSException();
				if (!validateApprndCntyNo(leRTSEx))
				{
					return;
				}
				if (!validateAddlInfo(leRTSEx))
				{
					if (leRTSEx.isValidationError())
					{
						leRTSEx.displayError(this);
						leRTSEx.getFirstComponent().requestFocus();
						return;
					}
					return;
				}

				// defect 9086
				//	Check for MfVehicleData null and Cancel on REG008
				if (caOrigVehData.getMfVehicleData() != null
					&& !caRegValidData.isEnterOnClassPltStkr())
				{
					caVehInqData = caOrigVehData;
				}
				// end defect 9086
				
				// defect 10007
				if ( getchkTowTruckCert().isEnabled()
					&& !getchkTowTruckCert().isSelected())
				{
					leRTSEx =
						new RTSException(991);
					//	throw leRTSEx;
					leRTSEx.displayError(this);
					getchkTowTruckCert().requestFocus();
					return;
				}
				// end defect 10007
				saveInputs();
				caRegValidData.setEnterOnAddlInfo(true);
				getController().processData(
					AbstractViewController.ENTER,
					caVehInqData);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caVehInqData);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				String lsTransCd = getController().getTransCode();
				if (UtilityMethods.isMFUP())
				{
					if (lsTransCd.equals(TransCdConstant.DUPL))
					{
						RTSHelp.displayHelp(RTSHelp.REG039A);
					}
					else if (lsTransCd.equals(TransCdConstant.EXCH))
					{
						RTSHelp.displayHelp(RTSHelp.REG039B);
					}
					else if (lsTransCd.equals(TransCdConstant.REPL))
					{
						RTSHelp.displayHelp(RTSHelp.REG039C);
					}
					else if (lsTransCd.equals(TransCdConstant.PAWT))
					{
						RTSHelp.displayHelp(RTSHelp.REG039D);
					}
					else if (
						(lsTransCd.equals(TransCdConstant.CORREG))
							&& (caRegValidData.getRegModify()
								== RegistrationConstant
									.REG_MODIFY_APPREHENDED))
					{
						RTSHelp.displayHelp(RTSHelp.REG039E);
					}
					else if (lsTransCd.equals(TransCdConstant.CORREG))
					{
						RTSHelp.displayHelp(RTSHelp.REG039F);
					}
					else if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.REG039M);
					}
				}
				//if MF is unavailable
				else
				{
					if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.REG039G);
					}
					else if (lsTransCd.equals(TransCdConstant.REPL))
					{
						RTSHelp.displayHelp(RTSHelp.REG039H);
					}
					else if (lsTransCd.equals(TransCdConstant.EXCH))
					{
						RTSHelp.displayHelp(RTSHelp.REG039I);
					}
					else if (lsTransCd.equals(TransCdConstant.PAWT))
					{
						RTSHelp.displayHelp(RTSHelp.REG039J);
					}
					else if (
						(lsTransCd.equals(TransCdConstant.CORREG))
							&& (caRegValidData.getRegModify()
								== RegistrationConstant
									.REG_MODIFY_APPREHENDED))
					{
						RTSHelp.displayHelp(RTSHelp.REG039K);
					}
					else if (lsTransCd.equals(TransCdConstant.CORREG))
					{
						RTSHelp.displayHelp(RTSHelp.REG039L);
					}
				}
			}
			else if (aaAE.getSource() == getbtnChangeRegistration())
			{
				// Determine vehicle class change allowed
				if (caRegValidData
					.getOrigVehClassCd()
					.equals(MISC_VEH_CLASS_CD)
					|| caRegValidData.getTransCode().equals(
						TransCdConstant.RENEW))
				{
					caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.setDisableVehClassIndi(
						1);
				}
				else
				{
					caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.setDisableVehClassIndi(
						0);
				}
				saveInputs();
				// defect 9086
				//	Make a copy of the VehInqData object to restore data
				caOrigVehData =
					(VehicleInquiryData) UtilityMethods.copy(
						caVehInqData);
				// end defect 9086
				do
				{
					caRegValidData.setVehClassOK(0);
					getController().processData(
						VCRegistrationAdditionalInfoREG039.CHANGE_REG,
						caVehInqData);
				}
				while (!validateChangeReg());
			}
			else if (aaAE.getSource() == getbtnChangeVehicleWeight())
			{
				// test out of scope plate
				if (RegistrationSpecialExemptions
					.verifyPltTypeScope(
						caVehInqData.getMfVehicleData().getRegData())
					== 0)
				{
					saveInputs();
					getController().processData(
						VCRegistrationAdditionalInfoREG039
							.VEHICLE_WEIGHT,
						caVehInqData);
				}
				else
				{
					displayError(52);
					return;
				}
			}
			// defect 8900
			// Reevaluate Masks/Indicators based upon Selection
			else if (aaAE.getSource() == getchkExempt())
			{
				handleChkExempt();
			}
			else if (aaAE.getSource() == getchkChargeEmissionsFee())
			{
				handleChkRegEmissFee();

			}
			else if (aaAE.getSource() == getchkChargeAddlToken())
			{
				handleChkTknTrlrFee();
			}
			// defect 9368 
			// defect 11030 
			else if (aaAE.getSource() == getchkPTOTrnsfr())
			{
				// end defect 11030 
				
				handleChkPTOTrnsfr();
			}
			else if (aaAE.getSource() == getchkNewPlatesDesired())
			{
				handleChkNewPlatesDesired();
			}
			// end defect 9368 
			else if (aaAE.getSource() == getchkChargeFee())
			{
				handleChkChargeFee();
			}
			// end defect 8900			
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Verify resident county.If no, set focus to county resident number.
	 * 
	 * @return boolean
	 */
	private boolean confirmCntyRes()
	{
		boolean lbRet = true;
		String lsCurrCnty = gettxtApprndCntyNo().getText();
		if (!lsCurrCnty
			.equals(Integer.toString(getLastApprndComptCntyNo())))
		{
			OfficeIdsData laOfcData =
				OfficeIdsCache.getOfcId(Integer.parseInt(lsCurrCnty));
			FrmCountyConfirmCTL002 laCntyCnfrm =
				new FrmCountyConfirmCTL002(
					getController().getMediator().getDesktop(),
					lsCurrCnty,
					laOfcData.getOfcName());
			int liRet = laCntyCnfrm.displayWindow();
			if (liRet == FrmCountyConfirmCTL002.NO)
			{
				lbRet = false;
			}
			else if (liRet == FrmCountyConfirmCTL002.YES)
			{
				lbRet = true;
			}
		}
		return lbRet;
	}

	/**
	 * Displays the error message
	 * 
	 * @param aiErrorNo int
	 */
	private void displayError(int aiErrorNo)
	{
		RTSException leRTSEx = new RTSException(aiErrorNo);
		leRTSEx.displayError(this);
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE java.awt.event.FocusEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{

	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * Validate DealerID and zip code.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		if (!aaFE.isTemporary()
			&& (aaFE.getSource() == gettxtApprndCntyNo()))
		{
			if (gettxtApprndCntyNo().getText().equals("")
				|| Integer.parseInt(gettxtApprndCntyNo().getText()) == 0)
			{
				gettxtApprndCntyNo().setText("0");
				gettxtEffMo().setText("0");
				gettxtEffYr().setText("0");
			}
			handleChangeRegMask(
				Integer.parseInt(gettxtApprndCntyNo().getText()));
		}
		else if (
			!aaFE.isTemporary() && aaFE.getSource() == gettxtEffMo())
		{
			if (gettxtEffMo().getText().equals(""))
			{
				gettxtEffMo().setText("0");
			}
		}
		else if (
			!aaFE.isTemporary() && aaFE.getSource() == gettxtEffYr())
		{
			if (gettxtEffYr().getText().equals(""))
			{
				gettxtEffYr().setText("0");
			}
		}
	}

	/**
	 * Return the btnChangeRegistration property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnChangeRegistration()
	{
		if (ivjbtnChangeRegistration == null)
		{
			try
			{
				ivjbtnChangeRegistration =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnChangeRegistration.setBounds(49, 27, 188, 25);
				ivjbtnChangeRegistration.setName(
					"btnChangeRegistration");
				ivjbtnChangeRegistration.setMnemonic(82);
				ivjbtnChangeRegistration.setText(CHG_REGIS);
				ivjbtnChangeRegistration.setMaximumSize(
					new java.awt.Dimension(149, 25));
				ivjbtnChangeRegistration.setActionCommand(CHG_REGIS);
				ivjbtnChangeRegistration.setMinimumSize(
					new java.awt.Dimension(149, 25));
				// user code begin {1}
				ivjbtnChangeRegistration.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbtnChangeRegistration;
	}

	/**
	 * Return the btnChangeVehicleWeight property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnChangeVehicleWeight()
	{
		if (ivjbtnChangeVehicleWeight == null)
		{
			try
			{
				ivjbtnChangeVehicleWeight =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnChangeVehicleWeight.setBounds(49, 71, 188, 25);
				ivjbtnChangeVehicleWeight.setName(
					"btnChangeVehicleWeight");
				ivjbtnChangeVehicleWeight.setMnemonic(86);
				ivjbtnChangeVehicleWeight.setText(CHG_VEH_WT);
				ivjbtnChangeVehicleWeight.setMaximumSize(
					new java.awt.Dimension(165, 25));
				ivjbtnChangeVehicleWeight.setActionCommand(CHG_VEH_WT);
				ivjbtnChangeVehicleWeight.setMinimumSize(
					new java.awt.Dimension(165, 25));
				// user code begin {1}
				ivjbtnChangeVehicleWeight.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbtnChangeVehicleWeight;
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
				ivjButtonPanel1.setLayout(new java.awt.FlowLayout());
				ivjButtonPanel1.setBounds(171, 394, 295, 36);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
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
	 * Return the chkChargeAdditional property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkChargeAddlToken()
	{
		if (ivjchkChargeAddlToken == null)
		{
			try
			{
				ivjchkChargeAddlToken = new javax.swing.JCheckBox();
				ivjchkChargeAddlToken.setSize(228, 20);
				ivjchkChargeAddlToken.setName("chkChargeAddlToken");
				ivjchkChargeAddlToken.setMnemonic(KeyEvent.VK_T);
				ivjchkChargeAddlToken.setText(CHRG_ADDL_TTL_FEE);
				ivjchkChargeAddlToken.setMaximumSize(
					new java.awt.Dimension(198, 22));
				ivjchkChargeAddlToken.setActionCommand(
					CHRG_ADDL_TTL_FEE);
				ivjchkChargeAddlToken.setMinimumSize(
					new java.awt.Dimension(198, 22));
				// user code begin {1}
				ivjchkChargeAddlToken.setLocation(15, 118);
				ivjchkChargeAddlToken.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjchkChargeAddlToken;
	}

	/**
	 * Return the chkChargeEmissionsFee property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkChargeEmissionsFee()
	{
		if (ivjchkChargeEmissionsFee == null)
		{
			try
			{
				ivjchkChargeEmissionsFee = new javax.swing.JCheckBox();
				ivjchkChargeEmissionsFee.setSize(224, 20);
				ivjchkChargeEmissionsFee.setName(
					"chkChargeEmissionsFee");
				ivjchkChargeEmissionsFee.setMnemonic(KeyEvent.VK_G);
				ivjchkChargeEmissionsFee.setText(CHRG_REG_EMI_FEE);
				ivjchkChargeEmissionsFee.setMaximumSize(
					new java.awt.Dimension(60, 22));
				ivjchkChargeEmissionsFee.setActionCommand(DIESEL);
				ivjchkChargeEmissionsFee.setMinimumSize(
					new java.awt.Dimension(60, 22));
				// user code begin {1}
				ivjchkChargeEmissionsFee.setLocation(15, 205);
				ivjchkChargeEmissionsFee.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjchkChargeEmissionsFee;
	}

	/**
	 * Return the chkChargeFee property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkChargeFee()
	{
		if (ivjchkChargeFee == null)
		{
			try
			{
				ivjchkChargeFee = new javax.swing.JCheckBox();
				ivjchkChargeFee.setSize(97, 20);
				ivjchkChargeFee.setName("chkChargeFee");
				ivjchkChargeFee.setMnemonic(KeyEvent.VK_F);
				ivjchkChargeFee.setText(CHRG_FEE);
				ivjchkChargeFee.setMaximumSize(
					new java.awt.Dimension(89, 22));
				ivjchkChargeFee.setActionCommand(CHRG_FEE);
				ivjchkChargeFee.setMinimumSize(
					new java.awt.Dimension(89, 22));

				ivjchkChargeFee.setLocation(15, 67);
				// user code begin {1}
				ivjchkChargeFee.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjchkChargeFee;
	}

	/**
	 * This method initializes ivjchkPTOTrnsfr
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPTOTrnsfr()
	{
		if (ivjchkPTOTrnsfr == null)
		{
			ivjchkPTOTrnsfr = new javax.swing.JCheckBox();
			ivjchkPTOTrnsfr.setSize(182, 21);
			ivjchkPTOTrnsfr.setLocation(15, 91);
			ivjchkPTOTrnsfr.setText(PTO_TRNSFR);
			// user code begin {1}
			ivjchkPTOTrnsfr.addActionListener(this);
			// user code end
			ivjchkPTOTrnsfr.setMnemonic(
				java.awt.event.KeyEvent.VK_T);
		}
		return ivjchkPTOTrnsfr;
	}

	/**
	 * Return the chkDiesel property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkDiesel()
	{
		if (ivjchkDiesel == null)
		{
			try
			{
				ivjchkDiesel = new javax.swing.JCheckBox();
				ivjchkDiesel.setSize(97, 20);
				ivjchkDiesel.setName("chkDiesel");
				ivjchkDiesel.setMnemonic(KeyEvent.VK_D);
				ivjchkDiesel.setText(DIESEL);
				ivjchkDiesel.setMaximumSize(
					new java.awt.Dimension(60, 22));
				ivjchkDiesel.setActionCommand(DIESEL);
				ivjchkDiesel.setMinimumSize(
					new java.awt.Dimension(60, 22));
				// user code begin {1}
				ivjchkDiesel.setLocation(15, 178);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjchkDiesel;
	}

	/**
	 * Return the chkExempt property value.
	 *  
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkExempt()
	{
		if (ivjchkExempt == null)
		{
			ivjchkExempt = new javax.swing.JCheckBox();
			ivjchkExempt.setSize(114, 21);
			ivjchkExempt.setText(EXEMPT);
			ivjchkExempt.setMnemonic(KeyEvent.VK_X);
			ivjchkExempt.setName("chkExempt");

			ivjchkExempt.setLocation(15, 233);
			ivjchkExempt.addActionListener(this);
		}
		return ivjchkExempt;
	}

	/**
	 * Return the chkMailInTransaction property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkMailInTransaction()
	{
		if (ivjchkMailInTransaction == null)
		{
			try
			{
				ivjchkMailInTransaction = new javax.swing.JCheckBox();
				ivjchkMailInTransaction.setSize(206, 20);
				ivjchkMailInTransaction.setName("chkMailInTransaction");
				ivjchkMailInTransaction.setMnemonic(KeyEvent.VK_M);
				ivjchkMailInTransaction.setText(MAIL_IN);
				ivjchkMailInTransaction.setMaximumSize(
					new java.awt.Dimension(133, 22));
				ivjchkMailInTransaction.setActionCommand(MAIL_IN);
				ivjchkMailInTransaction.setMinimumSize(
					new java.awt.Dimension(133, 22));
				// user code begin {1}
				ivjchkMailInTransaction.setLocation(15, 19);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjchkMailInTransaction;
	}

	/**
	 * Return the chkNewPlatesDesired property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkNewPlatesDesired()
	{
		if (ivjchkNewPlatesDesired == null)
		{
			try
			{
				ivjchkNewPlatesDesired = new javax.swing.JCheckBox();
				ivjchkNewPlatesDesired.setSize(192, 20);
				ivjchkNewPlatesDesired.setName("chkNewPlatesDesired");
				ivjchkNewPlatesDesired.setMnemonic(KeyEvent.VK_N);
				ivjchkNewPlatesDesired.setText(NEW_PLATES);
				ivjchkNewPlatesDesired.setMaximumSize(
					new java.awt.Dimension(136, 22));
				ivjchkNewPlatesDesired.setActionCommand(NEW_PLATES);
				ivjchkNewPlatesDesired.setMinimumSize(
					new java.awt.Dimension(136, 22));
				// user code begin {1}
				ivjchkNewPlatesDesired.setLocation(15, 44);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjchkNewPlatesDesired;
	}

	/**
	 * Return the chkVerified property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkVerifiedHvyVehUse()
	{
		if (ivjchkVerifiedHvyVehUse == null)
		{
			try
			{
				ivjchkVerifiedHvyVehUse = new javax.swing.JCheckBox();
				ivjchkVerifiedHvyVehUse.setSize(208, 20);
				ivjchkVerifiedHvyVehUse.setName("chkVerifiedHvyVehUse");
				ivjchkVerifiedHvyVehUse.setMnemonic(KeyEvent.VK_E);
				ivjchkVerifiedHvyVehUse.setText(VERIFY_HVY_USE_TAX);
				ivjchkVerifiedHvyVehUse.setMaximumSize(
					new java.awt.Dimension(200, 22));
				ivjchkVerifiedHvyVehUse.setActionCommand(
					VERIFY_HVY_USE_TAX);
				ivjchkVerifiedHvyVehUse.setMinimumSize(
					new java.awt.Dimension(200, 22));
				// user code begin {1}
				ivjchkVerifiedHvyVehUse.setLocation(15, 148);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjchkVerifiedHvyVehUse;
	}

	/**
	 * Return FrmRegistrationAdditionalInfoREG039ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmRegistrationAdditionalInfoREG039ContentPane1()
	{
		if (ivjFrmRegistrationAdditionalInfoREG039ContentPane1 == null)
		{
			try
			{
				ivjFrmRegistrationAdditionalInfoREG039ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmRegistrationAdditionalInfoREG039ContentPane1
					.setName(
					"FrmRegistrationAdditionalInfoREG039ContentPane1");
				ivjFrmRegistrationAdditionalInfoREG039ContentPane1
					.setLayout(
					null);
				ivjFrmRegistrationAdditionalInfoREG039ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmRegistrationAdditionalInfoREG039ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(624, 503));
				ivjFrmRegistrationAdditionalInfoREG039ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				ivjFrmRegistrationAdditionalInfoREG039ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmRegistrationAdditionalInfoREG039ContentPane1.add(
					getJPanel(),
					null);
				ivjFrmRegistrationAdditionalInfoREG039ContentPane1.add(
					getJPanel12(),
					null);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjFrmRegistrationAdditionalInfoREG039ContentPane1;
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
			jPanel.add(getJPanel1(), null);
			jPanel.add(getJPanel2(), null);
			jPanel.setBounds(10, 9, 276, 376);
		}
		return jPanel;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT));
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(279, 240));
				ivjJPanel1.add(getchkMailInTransaction(), null);
				ivjJPanel1.add(getchkNewPlatesDesired(), null);
				ivjJPanel1.add(getchkChargeFee(), null);
				// defect 11030
				ivjJPanel1.add(getchkPTOTrnsfr(), null);
				// end defect 11030
				ivjJPanel1.add(getchkChargeAddlToken(), null);
				ivjJPanel1.add(getchkVerifiedHvyVehUse(), null);
				ivjJPanel1.add(getchkDiesel(), null);
				ivjJPanel1.add(getchkChargeEmissionsFee(), null);
				ivjJPanel1.add(getchkExempt(), null);
				// defect 10007
				ivjJPanel1.add(getchkTowTruckCert(), null);
				// end defect 10007
				ivjJPanel1.setBounds(3, 3, 263, 286);
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
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel12()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new javax.swing.JPanel();
			jPanel1.setLayout(null);
			jPanel1.add(getJPanel3(), null);
			jPanel1.add(getJPanel4(), null);
			jPanel1.setBounds(291, 9, 309, 262);
		}
		return jPanel1;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new javax.swing.JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setOpaque(true);
				ivjJPanel2.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT_EXPIR));
				ivjJPanel2.setLayout(null);
				ivjJPanel2.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel2.setMinimumSize(
					new java.awt.Dimension(279, 134));
				ivjJPanel2.add(getradioValid(), null);
				ivjJPanel2.add(getradioInvalid(), null);
				ivjJPanel2.add(getradioCitation(), null);
				ivjJPanel2.setBounds(3, 295, 263, 76);
				// defect 7894
				// Changed from ButtonGroup to RTSButtonGroup
				RTSButtonGroup laButtonGroup = new RTSButtonGroup();
				// end defect 7894
				laButtonGroup.add(getradioValid());
				laButtonGroup.add(getradioInvalid());
				laButtonGroup.add(getradioCitation());
				// user code end
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
	 * Return the JPanel3 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new javax.swing.JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						ENTER_IF_APPR));
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel3.setMinimumSize(
					new java.awt.Dimension(302, 124));
				ivjJPanel3.add(getstcLblApprehended(), null);
				ivjJPanel3.add(getstcLblEffective(), null);
				ivjJPanel3.add(gettxtApprndCntyNo(), null);
				ivjJPanel3.add(gettxtEffMo(), null);
				ivjJPanel3.add(gettxtEffYr(), null);
				ivjJPanel3.setBounds(9, 3, 286, 99);
				// user code end
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
	 * Return the JPanel4 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel4()
	{
		if (ivjJPanel4 == null)
		{
			try
			{
				ivjJPanel4 = new javax.swing.JPanel();
				ivjJPanel4.setName("JPanel4");
				ivjJPanel4.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT));
				ivjJPanel4.setLayout(null);
				ivjJPanel4.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel4.setMinimumSize(
					new java.awt.Dimension(302, 134));
				ivjJPanel4.add(getbtnChangeRegistration(), null);
				ivjJPanel4.add(getbtnChangeVehicleWeight(), null);
				ivjJPanel4.setBounds(9, 117, 286, 109);
				// user code end
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
	 * Return value of LastApprndComptCntyNo 
	 * 
	 * @return int
	 */
	private int getLastApprndComptCntyNo()
	{
		return ciLastApprndComptCntyNo;
	}

	/**
	 * Return the radioCitation property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioCitation()
	{
		if (ivjradioCitation == null)
		{
			try
			{
				ivjradioCitation = new javax.swing.JRadioButton();
				ivjradioCitation.setBounds(42, 48, 161, 22);
				ivjradioCitation.setName("radioCitation");
				ivjradioCitation.setMnemonic(KeyEvent.VK_P);
				ivjradioCitation.setText(CITATION);
				ivjradioCitation.setMaximumSize(
					new java.awt.Dimension(157, 22));
				ivjradioCitation.setActionCommand(CITATION);
				ivjradioCitation.setMinimumSize(
					new java.awt.Dimension(157, 22));
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
		return ivjradioCitation;
	}

	/**
	 * Return the radioInvalid property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioInvalid()
	{
		if (ivjradioInvalid == null)
		{
			try
			{
				ivjradioInvalid = new javax.swing.JRadioButton();
				ivjradioInvalid.setBounds(123, 19, 112, 22);
				ivjradioInvalid.setName("radioInvalid");
				ivjradioInvalid.setMnemonic(KeyEvent.VK_I);
				ivjradioInvalid.setText(INVALID);
				ivjradioInvalid.setMaximumSize(
					new java.awt.Dimension(107, 22));
				ivjradioInvalid.setActionCommand(INVALID);
				ivjradioInvalid.setMinimumSize(
					new java.awt.Dimension(107, 22));
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
		return ivjradioInvalid;
	}

	/**
	 * Return the radioValid property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioValid()
	{
		if (ivjradioValid == null)
		{
			try
			{
				ivjradioValid = new javax.swing.JRadioButton();
				ivjradioValid.setBounds(14, 18, 108, 22);
				ivjradioValid.setName("radioValid");
				ivjradioValid.setMnemonic(KeyEvent.VK_A);
				ivjradioValid.setText(VALID);
				ivjradioValid.setMaximumSize(
					new java.awt.Dimension(99, 22));
				ivjradioValid.setActionCommand(VALID);
				ivjradioValid.setSelected(true);
				ivjradioValid.setMinimumSize(
					new java.awt.Dimension(99, 22));
				// user code begin {1}
				ivjradioValid.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioValid;
	}

	/**
	 * Return the stcLblApprehended property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblApprehended()
	{
		if (ivjstcLblApprehended == null)
		{
			try
			{
				ivjstcLblApprehended = new javax.swing.JLabel();
				ivjstcLblApprehended.setBounds(12, 32, 182, 14);
				ivjstcLblApprehended.setName("stcLblApprehended");
				ivjstcLblApprehended.setText(APPR_CNTY);
				ivjstcLblApprehended.setMaximumSize(
					new java.awt.Dimension(176, 14));
				ivjstcLblApprehended.setMinimumSize(
					new java.awt.Dimension(176, 14));
				ivjstcLblApprehended.setHorizontalAlignment(4);
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
		return ivjstcLblApprehended;
	}

	/**
	 * Return the stcLblEffective property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblEffective()
	{
		if (ivjstcLblEffective == null)
		{
			try
			{
				ivjstcLblEffective = new javax.swing.JLabel();
				ivjstcLblEffective.setBounds(28, 69, 166, 14);
				ivjstcLblEffective.setName("stcLblEffective");
				ivjstcLblEffective.setText(CORR_EFF_MOYR);
				ivjstcLblEffective.setMaximumSize(
					new java.awt.Dimension(164, 14));
				ivjstcLblEffective.setMinimumSize(
					new java.awt.Dimension(164, 14));
				ivjstcLblEffective.setHorizontalAlignment(4);
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
		return ivjstcLblEffective;
	}

	/**
	 * Return the RTSInputField1 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtApprndCntyNo()
	{
		if (ivjtxtApprndCntyNo == null)
		{
			try
			{
				ivjtxtApprndCntyNo = new RTSInputField();
				ivjtxtApprndCntyNo.setBounds(205, 29, 27, 20);
				ivjtxtApprndCntyNo.setName("txtApprndCntyNo");
				ivjtxtApprndCntyNo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtApprndCntyNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtApprndCntyNo.setMaxLength(3);
				// user code begin {1}
				ivjtxtApprndCntyNo.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtApprndCntyNo;
	}

	/**
	 * Return the RTSInputField3 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtEffMo()
	{
		if (ivjtxtEffMo == null)
		{
			try
			{
				ivjtxtEffMo = new RTSInputField();
				ivjtxtEffMo.setBounds(201, 66, 23, 20);
				ivjtxtEffMo.setName("txtEffMo");
				ivjtxtEffMo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtEffMo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEffMo.setMaxLength(2);
				// user code begin {1}
				ivjtxtEffMo.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtEffMo;
	}

	/**
	 * Return the RTSInputField2 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtEffYr()
	{
		if (ivjtxtEffYr == null)
		{
			try
			{
				ivjtxtEffYr = new RTSInputField();
				ivjtxtEffYr.setBounds(232, 66, 38, 20);
				ivjtxtEffYr.setName("txtEffYr");
				ivjtxtEffYr.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtEffYr.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEffYr.setMaxLength(4);
				// user code begin {1}
				ivjtxtEffYr.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtEffYr;
	}

	/**
	 * Determine if need to re-enable/disable, de-select/select checkboxes
	 */
	private void handleChangeReg()
	{
		RegistrationClientUtilityMethods.procsHvyVehUseTaxVerify(
			caVehInqData);

		RegistrationClientUtilityMethods.procsEmissions(caVehInqData);

		// defect 11030 
		// defect 9368
		RegistrationClientUtilityMethods.procsPTOTrnsfrEligible(
			caVehInqData);
		// end defect 9368
		// end defect 11030 

		handleStandardExempts();

		// If RENEW
		if (caRegValidData
			.getTransCode()
			.equals(TransCdConstant.RENEW))
		{
			RegistrationClientUtilityMethods.procsTokenTrailer(
				caVehInqData);
		}
	}

	/**
	 * Manage setting registration penalty indicator and
	 * enabling/disabling of of Change Registration button.
	 *
	 * @param aiCntyNo int 
	 */
	private void handleChangeRegMask(int aiCntyNo)
	{
		//  Set Charge Penalty indi
		if (caRegValidData.getTransCode().equals(TransCdConstant.RENEW)
			&& aiCntyNo != 0)
		{
			caRegValidData.setRegPnltyChrgIndi(1);
		}
		else
		{
			caRegValidData.setRegPnltyChrgIndi(0);
		}

		// Set Change Registration Mask on Reg Addl Info Screen
		// defect 8611
		// Do not disable Change Regis button if they chose Apprehended Cnty		
		//if (caRegValidData
		//	.getTransCode()
		//	.equals(TransCdConstant.RENEW))
		//{
		//	if (caRegValidData.getRegPnltyChrgIndi() == 0
		//		|| caRegValidData.getInvalidClassPltStkrIndi() == 1)
		//	{
		//		caRegValidData.setChngRegMask(true);
		//	}
		//	else
		//	{
		//		caRegValidData.setChngRegMask(false);
		//	}
		//}
		//else if (
		//	caRegValidData.getTransCode().equals(TransCdConstant.EXCH))

		if (caRegValidData.getTransCode().equals(TransCdConstant.RENEW)
			|| caRegValidData.getTransCode().equals(TransCdConstant.EXCH))
			// end 8611			
		{
			caRegValidData.setChngRegMask(true);
		}
		else
		{
			caRegValidData.setChngRegMask(false);
		}
		getbtnChangeRegistration().setEnabled(
			caRegValidData.getChngRegMask());
	}

	/**
	 * Reset selections/indicators when select/deselect Charge Fee 
	 */
	private void handleChkChargeFee()
	{
		if (!getchkChargeFee().isSelected())
		{
			if (getchkChargeAddlToken().isSelected())
			{
				getchkChargeAddlToken().setSelected(false);
			}

			if (getchkChargeEmissionsFee().isSelected())
			{
				getchkChargeEmissionsFee().setSelected(false);
			}
			caRegValidData.setChrgFeeIndi(0);
			
				// defect 11030 
				// Plt To Owner Transfer is no longer affected by 
				// Charge Fee 
				//	if (getchkPTOTrnsfr().isSelected())
				//	{
				//		getchkPTOTrnsfr().setSelected(false);
				//		getchkNewPlatesDesired().setSelected(
				//		caRegValidData.isNPDSelectedPriorPTO());
				//		if (!getchkNewPlatesDesired().isEnabled()
				//		&& caRegValidData.isNPDDisabledForPTO())
				//		{
				//			getchkNewPlatesDesired().setEnabled(true);
				//			caRegValidData.setNPDDisabledForPTO(false);
				//		}
				//	}
				// end defect 11030 
		}
		else
		{
			// If Modify and select ChargeFee, also select Token Trailer
			caRegValidData.setChrgFeeIndi(1);

			if (caRegValidData
				.getTransCode()
				.equals(TransCdConstant.CORREG)
				&& caRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_REG)
			{
				getchkChargeAddlToken().setSelected(true);
			}
			else
			{
				RegistrationClientUtilityMethods.procsEmissions(
					caVehInqData);
				getchkChargeEmissionsFee().setSelected(
					caRegValidData.getEmissionsFeeIndi() == 1
						? true
						: false);
			}
		}
	}

	/**
	 * Reset selections/indicators when select/deselect Exempt 
	 */
	private void handleChkExempt()
	{
		if (getchkExempt().isSelected())
		{
			getchkChargeFee().setSelected(false);
			getchkChargeFee().setEnabled(true);

			if (getchkChargeAddlToken().isEnabled())
			{
				getchkChargeAddlToken().setSelected(false);
			}
			if (getchkChargeEmissionsFee().isEnabled())
			{
				getchkChargeEmissionsFee().setSelected(false);
			}
			// defect 11030
			//			if (getchkPTOTrnsfr().isEnabled())
			//			{
			//				getchkPTOTrnsfr().setSelected(false);
			//				if (caRegValidData.isNPDDisabledForPTO())
			//					{
			//					getchkNewPlatesDesired().setEnabled(true);
			//					getchkNewPlatesDesired().setSelected(
			//						caRegValidData.isNPDSelectedPriorPTO());
			//					caRegValidData.setNPDDisabledForPTO(false);
			//				}
			//			}
			// end defect 11030 
			
			caVehInqData.getMfVehicleData().getRegData().setExmptIndi(
				1);
		}
		else
		{
			caVehInqData.getMfVehicleData().getRegData().setExmptIndi(
				0);
			// If !Exempt, disable Charge Fee if !Replacement
			// Note: Exempt not enabled in replacement
			getchkChargeFee().setSelected(true);
			getchkChargeFee().setEnabled(false);
			caRegValidData.setChrgFeeIndi(1);
			getchkMailInTransaction().setEnabled(
				caRegValidData.getTransCode().equals(
					TransCdConstant.RENEW));
			RegistrationClientUtilityMethods.procsEmissions(
				caVehInqData);
			getchkChargeEmissionsFee().setSelected(
				caRegValidData.getEmissionsFeeIndi() == 1
					? true
					: false);
		}
		RegistrationClientUtilityMethods.procsHvyVehUseTaxVerify(
			caVehInqData);
	}

	/** 
	 * Reset selections/indicators when select New Plates Desired
	 */
	private void handleChkNewPlatesDesired()
	{
		caRegValidData.setNPDSelectedPriorPTO(
			getchkNewPlatesDesired().isSelected());
	}

	/**
	 * Reset selections/indicators when select Plate to Owner Transfer 
	 *  
	 */
	private void handleChkPTOTrnsfr()
	{
		if (getchkPTOTrnsfr().isSelected())
		{
			caRegValidData.setNPDSelectedPriorPTO(
				getchkNewPlatesDesired().isSelected());
			getchkNewPlatesDesired().setSelected(true);

			if (getchkNewPlatesDesired().isEnabled())
			{
				getchkNewPlatesDesired().setEnabled(false);
				caRegValidData.setNPDDisabledForPTO(true);
			}
			else
			{
				caRegValidData.setNPDDisabledForPTO(false);
			}
		}
		else
		{
			if (caRegValidData.isNPDDisabledForPTO())
			{
				getchkNewPlatesDesired().setEnabled(true);
				caRegValidData.setNPDDisabledForPTO(false);
			}
			getchkNewPlatesDesired().setSelected(
				caRegValidData.isNPDSelectedPriorPTO());
		}
	}

	/**
	 * Reset selections/indicators when select Registration Emissions 
	 */
	private void handleChkRegEmissFee()
	{
		if (getchkChargeEmissionsFee().isSelected()
			&& getchkChargeFee().isEnabled()
			&& !getchkChargeFee().isSelected())
		{
			getchkChargeFee().setSelected(true);
		}
	}

	/**
	 * Reset selections/indicators when select Token Trailer Fee 
	 */
	private void handleChkTknTrlrFee()
	{
		if (getchkChargeAddlToken().isSelected()
			&& getchkChargeFee().isEnabled()
			&& !getchkChargeFee().isSelected())
		{
			getchkChargeFee().setSelected(true);
		}
		else if (
			caRegValidData.getTransCode().equals(
				TransCdConstant.CORREG)
				&& caRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_REG)
		{
			getchkChargeFee().setSelected(false);
		}

	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */ // System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7894
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7894
	}

	/**
	 * Reset masks/indicators when change reg 
	 *  if current/prior Standard Exempt
	 */
	private void handleStandardExempts()
	{
		// Handle Change Reg for Exempts 
		// Only Exchange
		String lsTransCd = caRegValidData.getTransCode();
		if (lsTransCd.equals(TransCdConstant.EXCH))
		{
			if (cbStandardExempt)
			{
				// Set to Not Charge Fees, Disable
				caRegValidData.setChrgFeeIndi(0);
				caRegValidData.setChrgFeeMask(false);

				// Set to Exempt, Disable
				caRegValidData.setExemptIndi(1);
				caRegValidData.setExemptMask(false);

				cbPriorStandardExempt = true;
			}
			else if (cbPriorStandardExempt)
			{
				caRegValidData.setChrgFeeMask(true);
				caRegValidData.setExemptMask(true);
				cbPriorStandardExempt = false;
			}
		}
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// defect 7894
			// Use RTSButton group to handle arrowing through the check
			// boxes
			RTSButtonGroup laCheckBoxBtnGrp = new RTSButtonGroup();
			laCheckBoxBtnGrp.add(getchkMailInTransaction());
			laCheckBoxBtnGrp.add(getchkNewPlatesDesired());
			laCheckBoxBtnGrp.add(getchkChargeFee());
			// defect 11030
			laCheckBoxBtnGrp.add(getchkPTOTrnsfr());
			// end defect 11030 
			laCheckBoxBtnGrp.add(getchkChargeAddlToken());
			laCheckBoxBtnGrp.add(getchkVerifiedHvyVehUse());
			laCheckBoxBtnGrp.add(getchkDiesel());
			laCheckBoxBtnGrp.add(getchkChargeEmissionsFee());
			// defect 8900			
			laCheckBoxBtnGrp.add(getchkExempt());
			// end defect 8900
			// end defect 7894
			// defect 10007
			getchkTowTruckCert().setEnabled(false);
			// end defect 10007
			// user code end
			setName("FrmRegistrationAdditionalInfoREG039");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(618, 469);
			setTitle(TITLE_REG039);
			setContentPane(
				getFrmRegistrationAdditionalInfoREG039ContentPane1());
		}
		catch (Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		// user code begin {2}
		// empty code block
		// user code end
	}

	/**
	 * ItemListener method.
	 * 
	 * @param aaIE java.awt.event.ItemEvent
	*/
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		//empty code block
	}

	/**
	 * set input fields into data.
	 */
	private void saveInputs()
	{
		caRegValidData.setProcsdByMailIndi(
			getchkMailInTransaction().isSelected() ? 1 : 0);
		caRegValidData.setNewPltReplIndi(
			getchkNewPlatesDesired().isSelected() ? 1 : 0);
		caRegValidData.setChrgFeeIndi(
			getchkChargeFee().isSelected() ? 1 : 0);

		// defect 11030
		caRegValidData.setPTOTrnsfrIndi(
			getchkPTOTrnsfr().isSelected() ? 1 : 0);
		// end defect 11030

		caRegValidData.setAddlToknFeeIndi(
			getchkChargeAddlToken().isSelected() ? 1 : 0);
		caRegValidData.setHvyVehUseTaxIndi(
			getchkVerifiedHvyVehUse().isSelected() ? 1 : 0);
		caVehInqData
			.getMfVehicleData()
			.getRegData()
			.setHvyVehUseTaxIndi(
			caRegValidData.getHvyVehUseTaxIndi());
		caVehInqData.getMfVehicleData().getVehicleData().setDieselIndi(
			getchkDiesel().isSelected() ? 1 : 0);
		caRegValidData.setEmissionsFeeIndi(
			getchkChargeEmissionsFee().isSelected() ? 1 : 0);

		// Mail-In
		caRegValidData.setProcsdByMailMask(
			getchkMailInTransaction().isEnabled());
		caRegValidData.setProcsdByMailIndi(
			getchkMailInTransaction().isSelected() ? 1 : 0);

		// defect 8900
		// Charge Fee 
		caRegValidData.setChrgFeeMask(getchkChargeFee().isEnabled());
		caRegValidData.setChrgFeeIndi(
			getchkChargeFee().isSelected() ? 1 : 0);
		caVehInqData.getMfVehicleData().getRegData().setNoChrgIndi(
			caRegValidData.getChrgFeeIndi() == 0 ? 1 : 0);

		// Exempt 			
		caRegValidData.setExemptMask(getchkExempt().isEnabled());
		caRegValidData.setExemptIndi(
			getchkExempt().isSelected() ? 1 : 0);
		caVehInqData.getMfVehicleData().getRegData().setExmptIndi(
			caRegValidData.getExemptIndi());
		// end defect 8900	
		
		// defect 10007
		// Verify Tow Truck Certificate
		caRegValidData.setVerifyTowTruckCertIndi(getchkTowTruckCert().isSelected());
		//caRegValidData.setVerifyTowTruckCertMask(getchkTowTruckCert().isEnabled());
		// defect 10007
		caRegValidData.setApprndComptCntyNo(
			Integer.parseInt(gettxtApprndCntyNo().getText()));
		caRegValidData.setCorrtnEffMo(
			Integer.parseInt(gettxtEffMo().getText()));
		caRegValidData.setCorrtnEffYr(
			Integer.parseInt(gettxtEffYr().getText()));
		if (getradioValid().isSelected())
		{
			caRegValidData.setRegExpiredReason(EXP_REG_VALID_REASON);
		}
		else if (getradioInvalid().isSelected())
		{
			caRegValidData.setRegExpiredReason(EXP_REG_INVALID_REASON);
		}
		else if (getradioCitation().isSelected())
		{
			caRegValidData.setRegExpiredReason(EXP_REG_CITATION);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information to the view
	 *
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// defect 8900 
		if (aaDataObject != null)
		{
			caVehInqData = (VehicleInquiryData) aaDataObject;
			caRegValidData =
				(RegistrationValidationData) caVehInqData
					.getValidationObject();
			caRegValidData.setEnterOnAddlInfo(false);
			setLastApprndComptCntyNo(
				caRegValidData.getApprndComptCntyNo());

			// Re-evaluate frame upon Registration Change
			cbPriorStandardExempt = cbStandardExempt;
			cbStandardExempt =
				CommonFeesCache.isStandardExempt(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegClassCd());

			if (caRegValidData.isEnterOnClassPltStkr())
			{
				handleChangeReg();
			}
			// end defect 8404

			boolean lbExemptAuth =
				(getController()
					.getMediator()
					.getDesktop()
					.getSecurityData()
					.getExmptAuthAccs()
					== 1);

			//Set Focus For Reg Addl Info Screen
			if (caRegValidData.getHvyVehUseTaxRequired() == 1)
			{
				getchkVerifiedHvyVehUse().requestFocus();
			}

			// Enabling Checkboxes 		
			if (caRegValidData.getChngRegIndi() == 1
				&& caRegValidData.getNewPltReplIndi() == 1)
			{
				caRegValidData.setNewPltReplMask(false);
			}
			getchkMailInTransaction().setEnabled(
				caRegValidData.getProcsdByMailMask());

			// defect 11030
			getchkNewPlatesDesired().setEnabled(
				caRegValidData.getNewPltReplMask()
					&& !caRegValidData.isNPDDisabledForPTO());
			getchkPTOTrnsfr().setEnabled(
				caRegValidData.getPTOTrnsfrMask());
			// end defect 11030 

			getchkChargeFee().setEnabled(
				caRegValidData.getChrgFeeMask());
			getchkChargeAddlToken().setEnabled(
				caRegValidData.getAddlToknFeeMask());
			getchkVerifiedHvyVehUse().setEnabled(
				caRegValidData.getHvyVehUseTaxMask());
			getchkDiesel().setEnabled(caRegValidData.getDieselMask());
			getchkChargeEmissionsFee().setEnabled(
				caRegValidData.getEmissionsFeeMask());
			getchkExempt().setEnabled(
				caRegValidData.getExemptMask() && lbExemptAuth);
			getstcLblApprehended().setEnabled(
				caRegValidData.getApprndComptCntyNoMask());
			gettxtApprndCntyNo().setEnabled(
				caRegValidData.getApprndComptCntyNoMask());
			getstcLblEffective().setEnabled(
				caRegValidData.getCorrtnEffDateMask());
			gettxtEffYr().setEnabled(
				caRegValidData.getCorrtnEffDateMask());
			gettxtEffMo().setEnabled(
				caRegValidData.getCorrtnEffDateMask());

			// Set Selected
			getchkMailInTransaction().setSelected(
				caRegValidData.getProcsdByMailIndi() == 1);

			// defect 9368
			getchkNewPlatesDesired().setSelected(
				caRegValidData.getNewPltReplIndi() == 1);
			
			// defect 11030 
			getchkPTOTrnsfr().setSelected(
				caRegValidData.getPTOTrnsfrIndi() == 1);
			//	cbKeepNewPltsDisabled =
			//		!getchkNewPlatesDesired().isEnabled()
			//			&& caRegValidData.isNPDSelectedPriorPTO();
			// end defect 11030
			
			// end defect 9368

			getchkChargeFee().setSelected(
				caRegValidData.getChrgFeeIndi() == 1);
			getchkChargeAddlToken().setSelected(
				caRegValidData.getAddlToknFeeIndi() == 1);
			getchkVerifiedHvyVehUse().setSelected(
				caRegValidData.getHvyVehUseTaxIndi() == 1);
			getchkDiesel().setSelected(
				caVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getDieselIndi()
					== 1);
			getchkChargeEmissionsFee().setSelected(
				caRegValidData.getEmissionsFeeIndi() == 1);
			getchkExempt().setSelected(
				caRegValidData.getExemptIndi() == 1);

			gettxtApprndCntyNo().setText(
				Integer.toString(
					caRegValidData.getApprndComptCntyNo()));
			gettxtEffMo().setText(
				Integer.toString(caRegValidData.getCorrtnEffMo()));
			gettxtEffYr().setText(
				Integer.toString(caRegValidData.getCorrtnEffYr()));
			// defect 8404
			// Reset RegExpiredReason if prior specification Seasonal Ag 
			if (cbSeasonalAg)
			{
				caRegValidData.setRegExpiredReason(0);
			}

			// Determine if new RegClassCd Seasonal Ag
			cbSeasonalAg =
				CommonFeesCache.isSeasonalAg(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegClassCd());

			if (caRegValidData
				.getTransCode()
				.equals(TransCdConstant.RENEW)
				&& caRegValidData.getRegistrationExpired() == 1)
			{
				// defect 8404
				// if RegPeriodLength = 1 (Seasonal Ag)
				//   default to valid & disable 
				if (cbSeasonalAg)
				{
					getradioValid().setEnabled(false);
					getradioInvalid().setEnabled(false);
					getradioCitation().setEnabled(false);
					getJPanel2().setEnabled(false);
					caRegValidData.setRegExpiredReason(
						EXP_REG_VALID_REASON);
				}
				else
				{
					getJPanel2().setEnabled(true);
					getradioValid().setEnabled(true);
					getradioInvalid().setEnabled(true);
					getradioCitation().setEnabled(true);
					// defect 8390 
					// default to invalid if not previously set
					getradioInvalid().setSelected(true);
					// end defect 8390 
				}
				// end defect 8404
			}
			else
			{
				getJPanel2().setEnabled(false);
				getradioValid().setEnabled(false);
				getradioInvalid().setEnabled(false);
				getradioCitation().setEnabled(false);
			}
			// end defect 8900 
			if (caRegValidData.getRegExpiredReason()
				== EXP_REG_VALID_REASON)
			{
				getradioValid().setSelected(true);
			}
			else if (
				caRegValidData.getRegExpiredReason()
					== EXP_REG_INVALID_REASON)
			{
				getradioInvalid().setSelected(true);
			}
			else if (
				caRegValidData.getRegExpiredReason()
					== EXP_REG_CITATION)
			{
				getradioCitation().setSelected(true);
			}
			getbtnChangeRegistration().setEnabled(
				caRegValidData.getChngRegMask());
			getbtnChangeVehicleWeight().setEnabled(
				caRegValidData.getGrossWtMask());

			if (!cbSavedOrigNPD)
			{
				cbOrigNPDMask = caRegValidData.getNewPltReplMask();
				cbOrigNPDSelection =
					getchkNewPlatesDesired().isSelected();
				cbSavedOrigNPD = true;
			}
			// defect 10007
//			if (getController().getTransCode().equals(TransCdConstant.RENEW)
//			//	|| getController().getTransCode().equals(TransCdConstant.TITLE)
//				|| getController().getTransCode().equals(TransCdConstant.EXCH))
//			{
//				getchkTowTruckCert().setSelected(caRegValidData.isVerifyTowTruckCertIndi());
//				if (caRegValidData.getOrigRegClassCd() == 46
//					|| caRegValidData.getOrigRegClassCd() == 47)
//				{
//					getchkTowTruckCert().setEnabled(true);
//				}
//				else 
//				{
//					getchkTowTruckCert().setEnabled(false);
//				}
//			}
//			else
//			{
//				getchkTowTruckCert().setEnabled(false);
//				
//			}
			if (caRegValidData.getVerifyTowTruckCertMask())
			{
				getchkTowTruckCert().setEnabled(true);
				getchkTowTruckCert().setSelected(caRegValidData.
				isVerifyTowTruckCertIndi());
			}
			else 
			{
				getchkTowTruckCert().setSelected(false);
				getchkTowTruckCert().setEnabled(false);
			}

			// end defect 10007 

		}
		// end defect 8900 
		
		 
	}

	/**
	 * Set Last Apprenhended Comp County No
	 * 
	 * @param aiNewLastApprndComptCntyNo int
	 */
	private void setLastApprndComptCntyNo(int aiNewLastApprndComptCntyNo)
	{
		ciLastApprndComptCntyNo = aiNewLastApprndComptCntyNo;
	}

	/**
	 * Check if Reg Add'l Info updated properly on Enter.
	 * 
	 * @param aeRTSEx RTSException
	 * @return boolean
	 */
	private boolean validateAddlInfo(RTSException aeRTSEx)
	{
		if (caRegValidData
			.getTransCode()
			.equals(TransCdConstant.RENEW))
		{
			// Test if class/plate/sticker combination is invalid
			if (caRegValidData.getInvalidClassPltStkrIndi() == 1)
			{
				// Test if class/plate/sticker status OK
				if (caRegValidData.getVehClassOK() == 1)
				{
					caRegValidData.setInvalidClassPltStkrIndi(0);
				}
				else
				{
					aeRTSEx.addException(
						new RTSException(412),
						getFrmRegistrationAdditionalInfoREG039ContentPane1());
					return false;
				}
			}
			// Check if minimum gross weight updated properly
			// Test if minimum gross weight is invalid
			if (caRegValidData.getInvalidMinGrossWtIndi() == 1)
			{
				if (caRegValidData.isVehWtStatusOK())
				{
					// Clear invalid minimum gross weight indicator
					caRegValidData.setInvalidMinGrossWtIndi(0);
				}
				else
				{
					aeRTSEx.addException(
						new RTSException(419),
						getFrmRegistrationAdditionalInfoREG039ContentPane1());
					//displayError(419);
					return false;
				}
			}
			// Check if heavy vehicle use tax updated properly
			// Test if heavy vehicle use tax is required
			if (!validateHvyVehUseTax(aeRTSEx))
			{
				return false;
			}
			// Set charge penalty if Citation/Charge Penalty
			if (getradioCitation().isSelected())
			{
				caRegValidData.setRegPnltyChrgIndi(1);
			}
			// defect 9085 
			if (!validateSpecialPlate(aeRTSEx))
			{
				return false;
			}
			// end defect 9085 
		}
		else if (
			caRegValidData.getTransCode().equals(TransCdConstant.EXCH))
		{
			// Test if class/plate/sticker actually changed
			if (caRegValidData
				.getOrigVehClassCd()
				.equals(
					caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getVehClassCd())
				&& caRegValidData.getOrigRegClassCd()
					== caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegClassCd()
				&& caRegValidData.getOrigRegStkrCd().equals(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegStkrCd())
				&& caRegValidData.getOrigRegPltCd().equals(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegPltCd())
				&& caRegValidData.getVehClassOK() == 0)
			{
				aeRTSEx.addException(
					new RTSException(92),
					getFrmRegistrationAdditionalInfoREG039ContentPane1());
				return false;
			}
			// Check if heavy vehicle use tax updated properly
			// Test if heavy vehicle use tax is required
			if (!validateHvyVehUseTax(aeRTSEx))
			{
				return false;
			}
			// defect 9085 
			if (!validateSpecialPlate(aeRTSEx))
			{
				return false;
			}
			// end defect 9085 
		}
		else if (
			caRegValidData.getTransCode().equals(TransCdConstant.PAWT)
				|| caRegValidData.getRegModify()
					== RegistrationConstant.REG_MODIFY_APPREHENDED)
		{
			// Check if change vehicle weight updated properly
			if (caRegValidData.isVehWtStatusOK() == false)
			{
				displayError(423);
				return false;
			}
			if (!validateHvyVehUseTax(aeRTSEx))
			{
				return false;
			}
			// Determine if apprehended permanent add'l weight
			if (caRegValidData.getRegModify()
				== RegistrationConstant.REG_MODIFY_APPREHENDED)
			{
				if (Integer.parseInt(gettxtApprndCntyNo().getText())
					== 0
					&& Integer.parseInt(gettxtEffMo().getText()) == 0
					&& Integer.parseInt(gettxtEffYr().getText()) == 0)
				{
					aeRTSEx.addException(
						new RTSException(173),
						gettxtApprndCntyNo());
					aeRTSEx.addException(
						new RTSException(173),
						gettxtEffMo());
					aeRTSEx.addException(
						new RTSException(173),
						gettxtEffYr());
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Validate effective year number is valid within the context of RTS.
	 * 
	 * @param aeRTSEx RTSException
	 * @return boolean
	*/
	private boolean validateApprndCntyNo(RTSException aeRTSEx)
	{
		int liCntyNo = Integer.parseInt(gettxtApprndCntyNo().getText());
		if (liCntyNo != 0)
		{
			if (liCntyNo > CNTY_NO_254 || liCntyNo < CNTY_NO_1)
			{
				aeRTSEx.addException(
					new RTSException(173),
					gettxtApprndCntyNo());
			}
		}
		else if (liCntyNo == 0)
		{
			gettxtEffMo().setText(Integer.toString(liCntyNo));
			gettxtEffYr().setText(Integer.toString(liCntyNo));
		}
		if (liCntyNo >= CNTY_NO_1 && liCntyNo <= CNTY_NO_254)
		{
			// if county no is modified then call CTL002 screen
			if (liCntyNo != getLastApprndComptCntyNo())
			{
				// send liCntyNo to CTL002 form for county confirmation
				if (!confirmCntyRes())
				{
					gettxtApprndCntyNo().requestFocus();
					return false;
				}
				// if yes selected in CTL002 setLastApprndComptCntyNo()
				// to liCntyNo
				setLastApprndComptCntyNo(liCntyNo);
			}
		}
		validateEffMo(aeRTSEx);
		validateEffYr(aeRTSEx);
		validateApprndFields(aeRTSEx);
		if (aeRTSEx.isValidationError())
		{
			aeRTSEx.displayError(this);
			aeRTSEx.getFirstComponent().requestFocus();
			return false;
		}
		handleChangeRegMask(liCntyNo);
		return true;
	}

	/**
	 * Validates for blank apprehended fields on Enter.
	 * 
	 * @param aeRTSEx RTSException
	 * @return boolean
	 */
	private boolean validateApprndFields(RTSException aeRTSEx)
	{
		boolean lbError = false;
		int liCntyNo = Integer.parseInt(gettxtApprndCntyNo().getText());
		int liMo = Integer.parseInt(gettxtEffMo().getText());
		int liYr = Integer.parseInt(gettxtEffYr().getText());
		if (liCntyNo != 0)
		{
			if (liMo == 0)
			{
				aeRTSEx.addException(
					new RTSException(
						RTSException.SYSTEM_ERROR,
						ERRMSG_CORR_EFF_MO,
						ERRMSG_VAL_APPR),
					gettxtEffMo());
				lbError = true;
			}
			if (liYr == 0)
			{
				aeRTSEx.addException(
					new RTSException(
						RTSException.SYSTEM_ERROR,
						ERRMSG_CORR_EFF_YR,
						ERRMSG_VAL_APPR),
					gettxtEffYr());
				lbError = true;
			}
		}
		else if (liMo != 0)
		{
			if (liCntyNo == 0)
			{
				aeRTSEx.addException(
					new RTSException(
						RTSException.SYSTEM_ERROR,
						ERRMSG_CNTY_NO,
						ERRMSG_VAL_APPR),
					gettxtApprndCntyNo());
				lbError = true;
			}
			if (liYr == 0)
			{
				aeRTSEx.addException(
					new RTSException(
						RTSException.SYSTEM_ERROR,
						ERRMSG_CORR_EFF_YR,
						ERRMSG_VAL_APPR),
					gettxtEffYr());
				lbError = true;
			}
		}
		else if (liYr != 0)
		{
			if (liCntyNo == 0)
			{
				aeRTSEx.addException(
					new RTSException(
						RTSException.SYSTEM_ERROR,
						ERRMSG_CNTY_NO,
						ERRMSG_VAL_APPR),
					gettxtApprndCntyNo());
				lbError = true;
			}
			else if (liMo == 0)
			{
				aeRTSEx.addException(
					new RTSException(
						RTSException.SYSTEM_ERROR,
						ERRMSG_CORR_EFF_MO,
						ERRMSG_VAL_APPR),
					gettxtEffMo());
				lbError = true;
			}
		}
		if (lbError == true)
		{
			return false;
		}
		return true;
	}

	/**
	 * Validates changes made in REG008 class/plate/sticker frame.
	 * 
	 * @return boolean
	 */
	private boolean validateChangeReg()
	{
		try
		{
			// Test if class/plate/sticker status OK after
			// return from REG008
			if (caRegValidData.getVehClassOK() == 1)
			{
				// Test if vehicle class has changed
				if (!caRegValidData
					.getOrigVehClassCd()
					.equals(
						caVehInqData
							.getMfVehicleData()
							.getVehicleData()
							.getVehClassCd()))
				{
					// Test if vehicle class has been changed to MISC;
					// Test MfVeh{Row}.VehClassCd$="MISC"
					if (caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getVehClassCd()
						.equals(MISC_VEH_CLASS_CD))
					{
						// Change document type code to "NON-TITLED
						// VEHICLE"; Assign MfVeh{Row}.DocTypeCd:=9
						caVehInqData
							.getMfVehicleData()
							.getTitleData()
							.setDocTypeCd(
							NON_TITLED_VEHICLE);
					}
					else
					{
						throw new RTSException(59);
					}
				}
				boolean lbRegChange = false;
				// Check if plate type is in scope of RTS or if
				// workstation is at HEADQUARTERS
				// defect 9085 
				if (RegistrationSpecialExemptions
					.verifyPltTypeScope(
						caVehInqData.getMfVehicleData().getRegData())
					== 0 //|| CommonValidations.isHq(
				//	SystemProperty.getOfficeIssuanceNo()))
					|| SystemProperty.isHQ())
				{
					lbRegChange = true;
				}
				// end defect 9085 
				else
				{
					// Check if registration change correction desired
					RTSException leRTSException =
						new RTSException(
							RTSException.CTL001,
							ERRMSG_PLATES_FROM_VTRD,
							null);
					int liButtonSelected =
						leRTSException.displayError(this);
					if (liButtonSelected == RTSException.NO)
					{
						// Set vehicle class status to NOT OK
						caRegValidData.setVehClassOK(0);
						// Test if class/plate/sticker combination
						// is not valid
						if (caRegValidData.getInvalidClassPltStkrIndi()
							== 1)
						{
							// if class/plate/sticker status OK
							if (caRegValidData.getVehClassOK() == 1)
							{
								caRegValidData
									.setInvalidClassPltStkrIndi(
									0);
							}
							// Clear invalid class/plate/sticker
							// combination indicator
							else
							{
								throw new RTSException(411);
							}
						}
					}
				}
				if (lbRegChange)
				{
					// Validate regis class/plate type/sticker
					// type combination
					int liRetVal =
						RegistrationVerify
							.verifyClassPltStkrCombination(
							caVehInqData);
					if (liRetVal == -1)
					{
						caRegValidData.setInvalidClassPltStkrIndi(1);
						caRegValidData.setVehClassOK(0);
						new RTSException(411).displayError(this);
					}
					// Check if registration class has changed or Test
					// if original registration was invalid
					if (caRegValidData.getOrigRegClassCd()
						!= caVehInqData
							.getMfVehicleData()
							.getRegData()
							.getRegClassCd()
						|| caVehInqData
							.getMfVehicleData()
							.getRegData()
							.getRegInvldIndi()
							== 1)
					{
						// need to re-lookup if reg weight change
						// is allowed
						RegistrationClassData laRegClassData =
							RegistrationClientBusiness
								.getRegClassDataObject(
								caVehInqData);
						int liCaryingCapReqd =
							laRegClassData.getCaryngCapReqd();
						if (liCaryingCapReqd == 1)
						{
							caRegValidData.setGrossWtMask(true);
							getbtnChangeVehicleWeight().setEnabled(
								caRegValidData.getGrossWtMask());
						}
					}
					// Test if plate code has changed
					boolean lbChngPlt =
						!caRegValidData.getOrigRegPltCd().equals(
							caVehInqData
								.getMfVehicleData()
								.getRegData()
								.getRegPltCd());

					SpecialPlatesRegisData laSpclPltRegisData =
						caVehInqData
							.getMfVehicleData()
							.getSpclPltRegisData();

					if (!lbChngPlt
						&& laSpclPltRegisData != null
						&& laSpclPltRegisData.isEnterOnSPL002())
					{
						lbChngPlt = true;
					}
					if (lbChngPlt)
					{
						// Set new plate indicator to yes
						caRegValidData.setNewPltReplIndi(1);
						// defect 8900
						getchkNewPlatesDesired().setSelected(true);
						getchkNewPlatesDesired().setEnabled(false);
						caRegValidData.setNewPltReplMask(false);
						// defect 9368 
						caRegValidData.setNPDDisabledForPTO(false);
						// end defect 9368  
						// end defect 8900
					}
					else
					{
						caRegValidData.setNewPltReplMask(cbOrigNPDMask);
						getchkNewPlatesDesired().setEnabled(
							cbOrigNPDMask);
						getchkNewPlatesDesired().setSelected(
							cbOrigNPDSelection);
					}
					caRegValidData.setChngRegIndi(1);
					return true;
				}
				else
				{
					if (caRegValidData.getVehClassOK() == 0)
					{
						// User selected 'No' in the CTL001
						// confirmation message above. Restore reg
						// class, plate type, and sticker code
						caVehInqData
							.getMfVehicleData()
							.getRegData()
							.setRegClassCd(
							caRegValidData.getOrigRegClassCd());
						caVehInqData
							.getMfVehicleData()
							.getRegData()
							.setRegPltCd(
							caRegValidData.getOrigRegPltCd());
						caVehInqData
							.getMfVehicleData()
							.getRegData()
							.setRegStkrCd(
							caRegValidData.getOrigRegStkrCd());
						return true;
					}
					else
					{
						return false;
					}
				}
			}
			else
			{
				return true;
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			return false;
		}
	}

	/**
	 * Validate the month number is valid.
	 *
	 * @param aeRTSEx RTSException 
	 * @return boolean
	 */
	private boolean validateEffMo(RTSException aeRTSEx)
	{
		boolean lbReturn = true;
		int liMo = 0;
		String lsMo = gettxtEffMo().getText();
		if (!lsMo.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			liMo = Integer.parseInt(lsMo);
			if (liMo > 12)
			{
				aeRTSEx.addException(
					new RTSException(77),
					gettxtEffMo());
				lbReturn = false;
			}
		}
		return lbReturn;
	}

	/**
	 * Validate effective year number is valid within the context of RTS.
	 * 
	 * @param aeRTSEx RTSException
	 * @return boolean
	 */
	private boolean validateEffYr(RTSException aeRTSEx)
	{
		boolean lbReturn = true;
		String lsYr = gettxtEffYr().getText();
		if (!lsYr.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			int liYr = Integer.parseInt(lsYr);
			// defect 9479
			// if (liYr > MIN_EFF_YR && liYr < MAX_EFF_YR))
			if (liYr != 0 && !(liYr > MIN_EFF_YR && liYr < MAX_EFF_YR))
			{
				aeRTSEx.addException(
					new RTSException(77),
					gettxtEffYr());
				lbReturn = false;
			}
			// end defect 9479
		}
		return lbReturn;
	}

	/**
	 * Check if heavy vehicle use tax updated properly
	 * Test if heavy vehicle use tax is required
	 * 
	 * @param aeRTSEx RTSException
	 * @return boolean
	 */
	private boolean validateHvyVehUseTax(RTSException aeRTSEx)
	{
		// Test if heavy vehicle use tax is required
		if (caRegValidData.getHvyVehUseTaxRequired() == 1)
		{
			// Test if heavy vehicle use tax has been verified
			if (!getchkVerifiedHvyVehUse().isSelected())
			{
				// heavy vehicle use tax must be verified
				aeRTSEx.addException(
					new RTSException(204),
					getchkVerifiedHvyVehUse());
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate Special Plates
	 * 
	 * @param aeRTSEx 
	 * @return boolean 
	 */
	private boolean validateSpecialPlate(RTSException aeRTSEx)
	{
		try
		{
			RegistrationVerify.verifyValidSpclPlt(
				caVehInqData,
				getController().getTransCode());
		}
		catch (RTSException aeRTSEx1)
		{
			aeRTSEx.addException(aeRTSEx1, getbtnChangeRegistration());
			aeRTSEx.setBeep(RTSException.BEEP);
			return false;
		}
		return true;
	}
	/**
	 * This method initializes ivjchkTowTruckCert
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkTowTruckCert() {
		if(ivjchkTowTruckCert == null) 
		{
			try
			{
				ivjchkTowTruckCert = new javax.swing.JCheckBox();
				ivjchkTowTruckCert.setBounds(15, 259, 213, 21);
				ivjchkTowTruckCert.setName("ivjchkTowTruckCert");
				ivjchkTowTruckCert.setMnemonic(java.awt.event.KeyEvent.VK_O);
				ivjchkTowTruckCert.setText("Verified Tow Truck Certificate");
			}
			catch (Throwable aeIvjEx)
			{
				handleException(aeIvjEx);
			}
		}
		return ivjchkTowTruckCert;
	}
}