package com.txdot.isd.rts.client.common.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.registration.business.RegistrationClientUtilityMethods;
import com.txdot.isd.rts.client.title.ui.TitleValidObj;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmClassPlateStickerTypeREG008.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking()
 * MAbs		    05/22/2002	Always bring back values, even in MF down 
 *							defect 4082
 * S Govindappa 07/25/2002  Changed populateRegClassComboBox and 
 *	&& J Rue				populatePlateTypeComboBox
 * 							methods to populate default values for 
 *							RegClassCdCombobox and PlateTypeComboBox
 *							when the RegClassCd or PlateCd captured 
 *							from mainframe or dealer diskette is wrong.
 *							defect 4510
 * Ray Rowehl	09/01/2002	Clear cvStkrTypeClassCombo
 *							when entering populateStkrTypeComboBox().
 *							defect 4707.  
 * S Govindappa 10/07/2002  Changed setData() to populate the 
 *	KHarrell				comboboxes for more Trans Title.
 *							defect 4833  
 * S Govindappa 11/21/2002  Prevented freeze of the application in 
 *							TTL002 and REG008 screens
 *                          while escaping back fast in the title event. 
 *                          Changed to ignore the Cancel call when the
 *							REG008 frame is invisible.
 *							modify actionPerformed()
 *							defect 4987 
 * B Arredondo	12/16/2002	Made changes for the user help guide so 
 *							had to make changes
 *							modify actionPerformed().
 * 							defect 5147.
 * J Rue		02/04/2003	Set frame REG008 to defeult PASS if 
 *							VehClassCD is invalid.
 *							modify setData()
 *							defect 5173 
 * S Govindappa 02/26/2003  Made changes to setData(..) to correct the 
 *							fix for defect 5173
 *							defect 5173. 
 * B Arredondo	10/30/2003	Commented code that was redundant and 
 * J Rue					unnecessary.
 * 							Formatted method populatePlateTypeComboBox()
 *							modify setData()
 *							defects 6029, 6090, 6134, and 6312
 * J Rue		09/07/2004	Determine if RegStkrCd is printable
 *							add  isDTAToBePrinted()
 *							method actionPerformed()
 *							defect 7527 Ver 5.2.1
 * J Rue		09/09/2004	Catch and return FALSE if not  
 *							DealerTitleData object.
 *							modify isDTAToBePrinted()
 *							defect 7549 Ver 5.2.1
 * J Rue		10/14/2004	Replace instanceof compare from 
 *							CompleteTransaction to TitleValidObj.
 *							modify isDTAToBePrinted()
 *							defect 7527 Ver 5.2.1
 * J Rue		12/31/2004	DTA, OffHwyUse and NewPltNo exist, display
 *							message cannot issue new plates. 
 *							Clear NewPltNo, NewRegExpMo, NewRegExpYr
 *							Formatting/JavaDoc/Variable Name Cleanup
 *							add dlrOffHwyUseCheck()
 *							modify actionPerformed()
 *							defect 7538 Ver 5.2.2
 * K Harrell	01/03/2005  Do not edit RegClassCd pulldown for 
 *							passenger vehichles, etc.
 *							which are LE 6000 & GT 6000
 *							(Reversal of PPR 1448 OF RTS I) 
 *							Formatting/JavaDoc/Variable Name Cleanup
 *							deprecate convertStrToInt()
 *							modify populateRegClassComboBox()
 *							defect 5657 Ver 5.2.2 
 * T Pederson	02/25/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	03/31/2005	Removed setNextFocusableComponent.
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/12/2004  ClassToPlate, PlateToSticker Implementation	
 * 							modify populatePlateTypeComboBox, 
 * 							populateStkrTypeComboBox,
 * 							getPlateCodeFromDesc()
 * 							defect 8218 Ver 5.2.3
 * S Johnston	06/20/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							defect 8240 Ver 5.2.3
 * B Hargrove	06/27/2005	Refactor\Move 
 * 							RegistrationModifyData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * T Pederson	07/12/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/19/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	09/12/2005	Modified so that "Plate Type" visible
 * 							defect 7885/7264 Ver 5.2.3
 * K Harrell	10/12/2005	Only set values to default when VehClassCd 
 * 							is null.    
 * 							modify setData() 
 * 			  				defect 8393 Ver 5.2.3 (from Ver 5.2.2 Fix 7)
 * Jeff S.		12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify setData()
 * 							defect 8479 Ver 5.2.3  
 * T. Pederson	12/21/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * K Harrell	01/17/2006	Need to establish earlier if RegWaived || 
 *							OffHwyUse to prevent PlateCd assignment on 
 *							No Regis Vehicles. 
 *							modify setData()
 *							defect 8509 Ver 5.2.2 Fix 8  
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove TXT_TITLE_CTL001 
 * 							modify actionPerformed(),dlrOffHwyUseCheck()
 * 							defect 8756 Ver 5.2.3
 * K Harrell	10/12/2006	Throw error 2013 if attempt to change from/to 
 * 							Standard Exempt	and not authorized. Throw 
 * 							error 471 if attempt to select Standard Exempt
 * 							in Renew or DTA
 * 							add cbOrigStandardExempt
 * 							add validateFields()
 * 							modify setData(),actionPerformed()
 * 							defect 8900 Ver Exempts
 * K Harrell	10/16/2006	setEnterOnClassPltStkr() for 
 * 							RegistrationValidationData 
 * 							modify actionperformed()
 * 							defect 8900 Ver Exempts
 * J Rue		03/08/2007	Add KEY002 processing
 * 							Move code to ensure capture of REG008 data
 * 							modify actionPerformed(), validateFields()
 * 							defect 9086 Ver Special Plates
 * J Rue		03/27/2007	Add code to handle Cancel from SPL002
 * 							modify setData()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/17/2007	Set SpecialPlateRegisData null if 
 * 							!Special Plate selected.  Needed to 
 * 							take Plt Options in consideration.
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	05/17/2007	Correct handling of out of scope plates
 * 							in Title. 
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * J Rue		05/23/2007	Update ErrMsgNo from 150 to 2017
 * 							modify validateFields()
 * 							defect 9086 Ver Special Plates
 * K Harrell	05/25/2008	Throw error if DTA and selected Customer 
 * 							 Supplied and not PTO Eligible.  Additional
 * 							 class cleanup.
 * 							add cbDTA, caDlrTtlData 
 * 							modify dlrOffHwyUseCheck(),isDTAToBePrinted(),
 * 							 setData(), validateFields()
 * 							defect 9584 3 Amigos PH B  
 * K Harrell	04/20/2010	Throw error 2022 if select ATV/ROV and 
 * 							 Off-Highway not selected 
 * 							add ERR_MSG_2022_APPEND,
 * 							  ERR_MSG_2022_RPO_ONLY_APPEND 
 * 							modify actionPerformed(), validateFields()
 * 							defect 10453 Ver POS_640
 * K Harrell	04/27/2010	Add check for RPO (as well as ATV) 
 * 							modify validateFields() 
 * 							defect 10453 Ver POS_640 
 * Min Wang		08/03/2010	Verify Tow Truck Certificate
 * 							modify actionPerformed()
 * 							defect 10007 Ver 6.5.0
 * Min Wang		09/14/2010	Fix null point when change the vehicle to 
 * 							the incorrect vehicle class for the truck.
 * 							modify actionPerformed()
 * 							defect 10591 Ver 6.6.0
 * B Hargrove	10/01/2010	Add process to default combo boxes based
 * 							on previous selection (ie: Tractor/Tractor/
 * 							Tractor Plt/Plate Sticker).
 * 							add handleMultPltTypes()
 * 							modify populatePlateTypeComboBox(), 
 * 							populateRegClassComboBox(),
 * 							populateStkrTypeComboBox()
 * 							defect 10600 Ver 6.6.0
 * B Hargrove	10/13/2010	Add check for Token Trailer to not set up
 * 							Sticker combo box. It's an annual plate but
 * 							no longer has a sticker.
 * 							modify populateStkrTypeComboBox()
 * 							defect 10623 Ver 6.6.0
 * B Hargrove 	10/29/2010  Token Trailer now does not print sticker.
 * 							Comment out all changes. They want to store 
 * 							inv detail, just don't print sticker.
 * 							modify populateStkrTypeComboBox()
 * 							defect 10623 Ver 6.6.0
 * ---------------------------------------------------------------------
 */
/**
 * This class allows for the selection of Vehicle Class, 
 * Registration Class, Plate Type and Sticker Type from
 * ComboBox.
 *
 * @version Ver 6.6.0			10/29/2010
 * @author	Sunil Govindappa
 * <br>Creation Date: 		06/26/2001 10:50:12 
 */

public class FrmClassPlateStickerTypeREG008
	extends RTSDialogBox
	implements ActionListener, KeyListener
{
	// REG008 Objects
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmClassPlateStickerTypeREG008ContentPane1 = null;
	private JPanel ivjPlateType = null;
	private JPanel ivjStickerType = null;
	private JPanel ivjVehicleClass = null;
	private JPanel ivjRegistrationClass = null;
	private JComboBox ivjcomboPlateType = null;
	private JComboBox ivjcomboRegClass = null;
	private JComboBox ivjcomboStickerType = null;
	private JComboBox ivjcomboVehClass = null;

	// boolean
	// defect 8900
	boolean cbOrigStandardExempt = false;
	// end defect 8900 

	// int
	private int ciOffHwyUseIndi = 0;
	private int ciRegClassCd;
	private int ciRegWaivedIndi = 0;
	private int ciRTSEffDt;
	private int ciStkrReqdIndi = 0;
	private int ciStolenIndi;
	private int ciTempRegClassCd;

	// Objects
	private VehicleInquiryData caVehInqData;
	// defect 9085
	private RegistrationData caOrigRegData;
	// end defect 9085

	// defect 9584
	private boolean cbDTA = false;
	private DealerTitleData caDlrTtlData = null;
	// end defect 9584 

	// String 
	private String csPlateCd;
	private String csStkrCd;
	private String csTempPlateCd;
	private String csTempStkrCd;
	private String csTempVehClassCd;
	private String csTransCd = null;
	private String csVehClassCd;

	// Vector 
	private Vector cvPltTypeClassCombo = new Vector();
	private Vector cvRegClassCombo = new Vector();
	private Vector cvStkrTypeClassCombo = new Vector();
	private Vector cvVehClassCombo = new Vector();

	// Constants 
	// defect 10600
	//private final static String PASSENGER1 = "PASSENGER-LESS/EQL 6000";
	//private final static String PASSENGER2 = "PASSENGER-MORETHAN 6000";
	//private final static String TRK_LESS_ONE = "TRK<=1";
	//private final static String TRK_GT_ONE = "TRK>1";
	//private final static String TRUCK1 = "TRUCK-LESS/EQL. 1 TON";
	//private final static String TRUCK2 = "TRUCK-MORE THAN 1 TON";
	private final static String WINDSHIELD = "WINDSHIELD STICKER";

	private final static int REGCLASS_APPORTIONED = 6;
	private final static int REGCLASS_EXEMPT = 39;

	private final static String PLTCD_APPRTK = "APPRTK";
	private final static String PLTCD_APPRTR = "APPRTR";
	private final static String PLTCD_EXPDBL = "EXPDBL";
	private final static String PLTCD_EXPMC = "EXPMCP";
	private final static String PLTCD_EXPSGL= "EXPSGL";
	private final static String VEH_CODE_BUS = "BUS";
	private final static String VEH_CODE_MOPED = "MOPED";
	private final static String VEH_CODE_MTRCYCLE = "MTRCYCLE";
	private final static String VEH_CODE_PASS_TRK = "PASS-TRK";
	private final static String VEH_CODE_TRACTOR = "TRACTOR";
	private final static String VEH_CODE_TRK_LE1 = "TRK<=1";
	private final static String VEH_CODE_TRK_GT1 = "TRK>1";
	private final static String VEH_CODE_TRLR = "TRLR";
	// end defect 10600
	private final static String VEH_CODE_PASS = "PASS";
	private final static String VEH_CODE_MISC = "MISC";

	// Text Constants 
	private final static String FRM_NAME_REG008 =
		"FrmClassPlateStickerTypeREG008";
	private final static String FRM_TITLE_REG008 =
		"Class/Plate/Sticker Type Choice         REG008";
	private final static String TXT_VEH_CLASS = "Vehicle class";
	private final static String TXT_REG_CLASS = "Registration class";
	private final static String TXT_PLT_TYPE = "Plate type";
	private final static String TXT_STKR_TYPE = "Sticker type";
	private final static String TXT_ERROR = "error";
	private final static String TXT_NULL_EFF_DT = "Null Effective Date";
	private final static String TXT_NULL_ITM_CD = "Null Item Code";
	private final static String TXT_NULL_PLT_TYPE = "Null Plate Type";
	private final static String TXT_NULL_REG_CLS = "Null reg class";
	private final static String TXT_NULL_VEH_CLS = "Null vehicle class";
	private final static String TXT_NULL_EFF_DT_OR_VEH_CD =
		"Null Effective Date or Vehicle Code";
	private final static String TXT_CANT_ISSUE_PLT =
		"Cannot issue a new plate for this transaction. ";
	private final static String TXT_CANT_PRINT_STKR =
		"A sticker cannot be printed for this transaction";

	// defect 10453 
	private final static String ERR_MSG_2022_APPEND =
		"  PLEASE RETURN TO TTL002 TO SPECIFY OFF-HIGHWAY.";
	private final static String ERR_MSG_2022_RPO_ONLY_APPEND =
		"  PLEASE RETURN TO TTL002 TO CHANGE THE TITLE TYPE AND SPECIFY OFF-HIGHWAY.";
	// end defect 10453 

	/**
	 * FrmClassPlateStickerTypeREG008 constructor
	 */
	public FrmClassPlateStickerTypeREG008()
	{
		super();
		initialize();
	}

	/**
	 * FrmClassPlateStickerTypeREG008 constructor
	 *
	 * @param aaParent Dialog 
	 */
	public FrmClassPlateStickerTypeREG008(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmClassPlateStickerTypeREG008 constructor
	 *
	 * @param aaParent JFrame
	 */
	public FrmClassPlateStickerTypeREG008(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs
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
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10453 
				// No need to validate if PLTOPT 
				if (csTransCd.equals(TransCdConstant.PLTOPT))
				{
					getController().processData(
						AbstractViewController.CANCEL,
						null);
				}
				else
				{
					// end defect 10453 

					if (!validateFields())
					{
						return;
					}

					try
					{
						if (getcomboStickerType().getSelectedItem()
							!= null)
						{
							csTempStkrCd =
								getStickerCodeFromDesc(
									getcomboStickerType()
										.getSelectedIndex());
						}
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(this);
					}

					// defect 10453 
					//	if (csTransCd.equals(TransCdConstant.PLTOPT))
					//	{
					//		getController().processData(
					//			AbstractViewController.CANCEL,
					//			null);
					//	}
					//	else
					// end defect 10453 

					if (csTempVehClassCd == null
						|| ciTempRegClassCd == 0
						|| (csTempPlateCd == null
							&& ciRegWaivedIndi == 0
							&& ciOffHwyUseIndi == 0)
						|| (csTempStkrCd == null
							&& ciRegWaivedIndi == 0
							&& ciOffHwyUseIndi == 0
							&& ciStkrReqdIndi != 0
							&& ciStolenIndi == 0))
					{
						RTSException leEx = new RTSException(482);
						leEx.displayError(this);
					}
					else
					{
						if (csTempPlateCd == null)
						{
							csTempPlateCd =
								CommonConstant.STR_SPACE_EMPTY;
						}
						if (csTempStkrCd == null)
						{
							csTempStkrCd =
								CommonConstant.STR_SPACE_EMPTY;
						}

						// defect 9085
						RegistrationValidationData laRegValidData =
							null;

						caVehInqData
							.getMfVehicleData()
							.getVehicleData()
							.setVehClassCd(
							csTempVehClassCd);
						RegistrationData laRegData =
							caVehInqData
								.getMfVehicleData()
								.getRegData();
						laRegData.setRegClassCd(ciTempRegClassCd);
						// defect 7538
						// Set Dlr OffHwyUse ATV PlateCd
						dlrOffHwyUseCheck();
						// end defect 7538
						laRegData.setRegPltCd(csTempPlateCd);
						laRegData.setRegStkrCd(csTempStkrCd);

						// defect 7527
						// Display message that sticker will not be printed
						// if Print Sticker was selected and is not a 
						// printable sticker and set ToBePrinted = false.
						if (isDTAToBePrinted(csTempStkrCd))
						{
							RTSException leException =
								new RTSException(
									RTSException.WARNING_MESSAGE,
									TXT_CANT_PRINT_STKR,
									ScreenConstant.CTL001_FRM_TITLE);
							leException.displayError(this);
							caDlrTtlData.setToBePrinted(false);
						}
						// end defect 7527

						// defect 8900
						if (caVehInqData.getValidationObject()
							instanceof RegistrationValidationData)
						{
							laRegValidData =
								(RegistrationValidationData) caVehInqData
									.getValidationObject();
									
							// defect 10007	
							int liRTSDate =
								laRegData.getRegEffDt() == 0
									 ? new RTSDate().getYYYYMMDDDate()
									 : laRegData.getRegEffDt();
							// defect 10591
//							RegistrationClientUtilityMethods
//							.procsVerifyTowTruckMask(
//								laRegValidData,
//								RegistrationClassCache.getRegisClass(
//								laRegValidData.getOrigVehClassCd(),
//								laRegData.getRegClassCd(),
//								liRTSDate));
							RegistrationClientUtilityMethods
								.procsVerifyTowTruckMask(
								laRegValidData,
								RegistrationClassCache.getRegisClass(
								caVehInqData
								.getMfVehicleData()
								.getVehicleData().getVehClassCd(),
								laRegData.getRegClassCd(),
								liRTSDate));
							// end defect 10591

						// end defect 10007
									
							laRegValidData.setEnterOnClassPltStkr(true);
						}
						// end defect 8900

						// defect 9086
						//	Add cbTxTransMfDown
						//	set if VehClassCd is null, 
						//	 set when MFDown, Texas Transfer (TTL040), 
						//	 go to TTL014 (set in VC)
						//
						// If PlateTypeCd is a special plate and 
						//	transaction is not DTA, call KEY002
						if (PlateTypeCache.isSpclPlate(csTempPlateCd))
						{
							if (PlateTypeCache
								.isOutOfScopePlate(csTempPlateCd))
							{
								if (!(csTransCd
									.equals(TransCdConstant.TITLE)
									|| csTransCd.equals(
										TransCdConstant.NONTTL)
									|| csTransCd.equals(
										TransCdConstant.REJCOR)))
								{
									RTSException leEx =
										new RTSException(18);
									leEx.displayError(this);
								}
								else
								{
									caVehInqData
										.getMfVehicleData()
										.setSpclPltRegisData(
										null);
									getController().processData(
										AbstractViewController.ENTER,
										caVehInqData);

								}
							}
							else
							{
								getController().processData(
									VCClassPlateStickerTypeREG008
										.GET_SPCL_PLT,
									caVehInqData);
							}
						}
						else
						{
							caVehInqData
								.getMfVehicleData()
								.setSpclPltRegisData(
								null);
							getController().processData(
								AbstractViewController.ENTER,
								caVehInqData);
						}
						//	end defect 9086 
					}
					// defect 10453 	
				}
				// end defect 10453 
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				// defect 9086
				// Set EnterOnClassPltStkr to false from REG039
				if (!csTransCd.equals(TransCdConstant.PLTOPT))
				{
					RegistrationValidationData laRegValidData = null;
					if (caVehInqData.getValidationObject()
						instanceof RegistrationValidationData)
					{
						laRegValidData =
							(RegistrationValidationData) caVehInqData
								.getValidationObject();
						laRegValidData.setEnterOnClassPltStkr(false);
						caVehInqData.getMfVehicleData().setRegData(
							caOrigRegData);
					}
				}
				if (isVisible())
				{
					getController().processData(
						AbstractViewController.CANCEL,
						null);
				}
				else
				{
					return;
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				String lsTransCd = getController().getTransCode();
				if (lsTransCd.equals(TransCdConstant.PLTOPT))
				{
					RTSHelp.displayHelp(RTSHelp.REG008A);
				}
				else if (
					lsTransCd.equals(TransCdConstant.DTAORK)
						|| lsTransCd.equals(TransCdConstant.DTANTK))
				{
					RTSHelp.displayHelp(RTSHelp.REG008M);
				}
				else if (
					lsTransCd.equals(TransCdConstant.DTAORD)
						|| lsTransCd.equals(TransCdConstant.DTANTD))
				{
					RTSHelp.displayHelp(RTSHelp.REG008N);
				}
				else if (lsTransCd.equals(TransCdConstant.CORREG))
				{
					VehicleInquiryData laVehInqData =
						(VehicleInquiryData) getController().getData();
					RegistrationModifyData laRegModData =
						(RegistrationModifyData) laVehInqData
							.getValidationObject();
					if (laRegModData.getRegModifyType()
						== RegistrationConstant.REG_MODIFY_APPREHENDED)
					{
						RTSHelp.displayHelp(RTSHelp.REG008O);
					}
				}
				else if (lsTransCd.equals(TransCdConstant.EXCH))
				{
					RTSHelp.displayHelp(RTSHelp.REG008P);
				}
				else if (UtilityMethods.isMFUP())
				{
					if (lsTransCd.equals(TransCdConstant.TITLE)
						|| lsTransCd.equals(TransCdConstant.NONTTL)
						|| lsTransCd.equals(TransCdConstant.REJCOR))
					{
						RTSHelp.displayHelp(RTSHelp.REG008C);
					}
					else if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.REG008B);
					}
				}
				else if (!UtilityMethods.isMFUP())
				{
					if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.REG008D);
					}
					else if (
						lsTransCd.equals(TransCdConstant.TITLE)
							|| lsTransCd.equals(TransCdConstant.NONTTL)
							|| lsTransCd.equals(TransCdConstant.REJCOR))
					{
						RTSHelp.displayHelp(RTSHelp.REG008E);
					}
					else if (lsTransCd.equals(TransCdConstant.REPL))
					{
						RTSHelp.displayHelp(RTSHelp.REG008F);
					}
					else if (lsTransCd.equals(TransCdConstant.EXCH))
					{
						RTSHelp.displayHelp(RTSHelp.REG008G);
					}
					else if (lsTransCd.equals(TransCdConstant.PAWT))
					{
						RTSHelp.displayHelp(RTSHelp.REG008H);
					}
					else if (lsTransCd.equals(TransCdConstant.CORREG))
					{
						VehicleInquiryData laVehInqData =
							(VehicleInquiryData) getController()
								.getData();
						RegistrationModifyData laRegModData =
							(RegistrationModifyData) laVehInqData
								.getValidationObject();
						if (laRegModData.getRegModifyType()
							== RegistrationConstant
								.REG_MODIFY_APPREHENDED)
						{
							RTSHelp.displayHelp(RTSHelp.REG008I);
						}
						else
						{
							RTSHelp.displayHelp(RTSHelp.REG008J);
						}
					}
					else if (lsTransCd.equals(TransCdConstant.TAWPT))
					{
						RTSHelp.displayHelp(RTSHelp.REG008L);
					}
				}
			}
			try
			{
				if (aaAE.getSource().equals(getcomboVehClass()))
				{
					csTempVehClassCd =
						(String) getcomboVehClass().getSelectedItem();
					populateRegClassComboBox(
						ciRTSEffDt,
						csTempVehClassCd);
					//Get the reg code that was selected
					if (getcomboRegClass().getSelectedIndex() != -1)
					{
						ciTempRegClassCd =
							getRegCodeFromDesc(
								getcomboRegClass().getSelectedIndex());
						//Populate the next list box
						populatePlateTypeComboBox(
							ciTempRegClassCd,
							ciRTSEffDt);
						if (getcomboPlateType().getSelectedIndex()
							!= -1)
						{
							//get Plate cd that was selected
							csTempPlateCd =
								getPlateCodeFromDesc(
									getcomboPlateType()
										.getSelectedIndex());
							//Populate the next list box
							populateStkrTypeComboBox(
								ciTempRegClassCd,
								csTempPlateCd,
								ciRTSEffDt);
						}
					}
				}
				else if (aaAE.getSource().equals(getcomboRegClass()))
				{
					if (getcomboRegClass().getSelectedIndex() != -1)
					{
						ciTempRegClassCd =
							getRegCodeFromDesc(
								getcomboRegClass().getSelectedIndex());
						//Populate the next list box
						populatePlateTypeComboBox(
							ciTempRegClassCd,
							ciRTSEffDt);
						if (getcomboPlateType().getSelectedIndex()
							!= -1)
						{
							//get Plate cd that was selected
							csTempPlateCd =
								getPlateCodeFromDesc(
									getcomboPlateType()
										.getSelectedIndex());
							//Populate the next list box
							populateStkrTypeComboBox(
								ciTempRegClassCd,
								csTempPlateCd,
								ciRTSEffDt);
						}
					}
				}
				else if (aaAE.getSource().equals(getcomboPlateType()))
				{
					if (getcomboPlateType().getSelectedIndex() != -1)
					{
						//get Plate cd that was selected
						csTempPlateCd =
							getPlateCodeFromDesc(
								getcomboPlateType().getSelectedIndex());
						//Populate the next list box
						populateStkrTypeComboBox(
							ciTempRegClassCd,
							csTempPlateCd,
							ciRTSEffDt);
					}
				}
				else if (
					aaAE.getSource().equals(getcomboStickerType())
						&& getcomboStickerType().getSelectedIndex() != -1)
				{
					csTempStkrCd =
						getStickerCodeFromDesc(
							getcomboStickerType().getSelectedIndex());
				}
			}
			catch (RTSException aeRTSEx)
			{
				aeRTSEx.displayError(this);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Set New Plate, RegExpMo and RegExpYr to defaults
	 * if transaction is DTA/HwyUse and RegPltCd is disabled
	 * on the Class/Plate/Sticker screen.
	 */
	private void dlrOffHwyUseCheck()
	{
		// Display message if DTA/OffHwyUse 
		// and plate code combo is disabled
		// defect 9584 
		// Use cbDTA , caDlrTtlData 
		if (cbDTA
			&& ciOffHwyUseIndi == 1
			&& !getcomboPlateType().isEnabled()
			&& caDlrTtlData != null
			&& caDlrTtlData.getNewPltNo() != null
			&& caDlrTtlData.getNewPltNo().length() > 0)
		{
			RTSException leException =
				new RTSException(
					RTSException.WARNING_MESSAGE,
					TXT_CANT_ISSUE_PLT,
					ScreenConstant.CTL001_FRM_TITLE);

			leException.displayError(this);
			caDlrTtlData.setNewPltNo(CommonConstant.STR_SPACE_EMPTY);
			caDlrTtlData.setNewRegExpMo(0);
			caDlrTtlData.setNewRegExpYr(0);
		}
		// end defect 9584 
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
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the ComboBox for PlateType property value
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboPlateType()
	{
		if (ivjcomboPlateType == null)
		{
			try
			{
				ivjcomboPlateType = new JComboBox();
				ivjcomboPlateType.setName("comboPlateType");
				// user code begin {1}
				ivjcomboPlateType.addActionListener(this);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjcomboPlateType;
	}

	/**
	 * Return the ComboBox for Registration Class property value.
	 *
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboRegClass()
	{
		if (ivjcomboRegClass == null)
		{
			try
			{
				ivjcomboRegClass = new JComboBox();
				ivjcomboRegClass.setName("comboRegClass");
				// user code begin {1}
				ivjcomboRegClass.addActionListener(this);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjcomboRegClass;
	}

	/**
	 * Return the ComboBox for Sticker type property value.
	 *
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboStickerType()
	{
		if (ivjcomboStickerType == null)
		{
			try
			{
				ivjcomboStickerType = new JComboBox();
				ivjcomboStickerType.setName("comboStickerType");
				// user code begin {1}
				ivjcomboStickerType.addActionListener(this);
				// user code end
			}
			catch (Throwable aeException)
			{
				// user code begin {2}
				// user code end
				handleException(aeException);
			}
		}
		return ivjcomboStickerType;
	}

	/**
	* Return the ComboBox for Vehicle Class property value
	* 
	* @return JComboBox
	*/ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboVehClass()
	{
		if (ivjcomboVehClass == null)
		{
			try
			{
				ivjcomboVehClass = new JComboBox();
				ivjcomboVehClass.setName("comboVehClass");
				// user code begin {1}
				ivjcomboVehClass.addActionListener(this);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjcomboVehClass;
	}

	/**
	* Get the VehicleInquiryData object which is a graph object
	*/
	public Object getData()
	{
		return caVehInqData;
	}

	/**
	 * Return the FrmClassPlateStickerTypeREG008ContentPane1 property 
	 * value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmClassPlateStickerTypeREG008ContentPane1()
	{
		if (ivjFrmClassPlateStickerTypeREG008ContentPane1 == null)
		{
			try
			{
				ivjFrmClassPlateStickerTypeREG008ContentPane1 =
					new javax.swing.JPanel();
				java.awt.GridBagConstraints consGridBagConstraints3 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints4 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints5 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints6 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints7 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints5.insets =
					new java.awt.Insets(13, 44, 14, 57);
				consGridBagConstraints5.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints5.weighty = 1.0;
				consGridBagConstraints5.weightx = 1.0;
				consGridBagConstraints5.gridy = 2;
				consGridBagConstraints5.gridx = 0;
				consGridBagConstraints3.insets =
					new java.awt.Insets(19, 44, 13, 57);
				consGridBagConstraints3.ipady = -6;
				consGridBagConstraints3.ipadx = 87;
				consGridBagConstraints3.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints3.weighty = 1.0;
				consGridBagConstraints3.weightx = 1.0;
				consGridBagConstraints3.gridy = 0;
				consGridBagConstraints3.gridx = 0;
				consGridBagConstraints6.insets =
					new java.awt.Insets(15, 44, 5, 57);
				consGridBagConstraints6.ipady = -6;
				consGridBagConstraints6.ipadx = 87;
				consGridBagConstraints6.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints6.weighty = 1.0;
				consGridBagConstraints6.weightx = 1.0;
				consGridBagConstraints6.gridy = 3;
				consGridBagConstraints6.gridx = 0;
				consGridBagConstraints4.insets =
					new java.awt.Insets(13, 44, 13, 57);
				consGridBagConstraints4.ipady = -6;
				consGridBagConstraints4.ipadx = 87;
				consGridBagConstraints4.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints4.weighty = 1.0;
				consGridBagConstraints4.weightx = 1.0;
				consGridBagConstraints4.gridy = 1;
				consGridBagConstraints4.gridx = 0;
				consGridBagConstraints7.insets =
					new java.awt.Insets(5, 97, 21, 114);
				consGridBagConstraints7.ipady = 22;
				consGridBagConstraints7.ipadx = 25;
				consGridBagConstraints7.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints7.weighty = 1.0;
				consGridBagConstraints7.weightx = 1.0;
				consGridBagConstraints7.gridy = 4;
				consGridBagConstraints7.gridx = 0;
				ivjFrmClassPlateStickerTypeREG008ContentPane1.setName(
					"FrmClassPlateStickerTypeREG008ContentPane1");
				ivjFrmClassPlateStickerTypeREG008ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmClassPlateStickerTypeREG008ContentPane1.add(
					getVehicleClass(),
					consGridBagConstraints3);
				ivjFrmClassPlateStickerTypeREG008ContentPane1.add(
					getRegistrationClass(),
					consGridBagConstraints4);
				ivjFrmClassPlateStickerTypeREG008ContentPane1.add(
					getPlateType(),
					consGridBagConstraints5);
				ivjFrmClassPlateStickerTypeREG008ContentPane1.add(
					getStickerType(),
					consGridBagConstraints6);
				ivjFrmClassPlateStickerTypeREG008ContentPane1.add(
					getButtonPanel1(),
					consGridBagConstraints7);
				ivjFrmClassPlateStickerTypeREG008ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				ivjFrmClassPlateStickerTypeREG008ContentPane1
					.setMinimumSize(
					new Dimension(0, 0));
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjFrmClassPlateStickerTypeREG008ContentPane1;
	}

	/**
	 * Returns a vector of Item Codes retrieved from ItemCodesCache
	 *
	 * @return Vector
	 * @throws RTSException 
	 */
	private Vector getItemCds() throws RTSException
	{
		Vector lvItemCdResult = new Vector();
		lvItemCdResult =
			ItemCodesCache.getItmCds(1, 0, InventoryConstant.CHAR_S);
		if (lvItemCdResult == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				TXT_NULL_ITM_CD,
				TXT_ERROR);
		}
		int liSize = lvItemCdResult.size();
		if (liSize == 0)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				TXT_NULL_ITM_CD,
				TXT_ERROR);
		}
		return lvItemCdResult;
	}

	/**
	 * Return the Plate Code from Plate Type ComboBox
	 * for the index passed as a parameter
	 *
	 * @param  aiDesc int 
	 * @return String
	 * @throws RTSException
	 */
	private String getPlateCodeFromDesc(int aiDesc) throws RTSException
	{
		String lsPltCd = null;
		ClassToPlateDescriptionData laClassToPltDescData =
			(
				ClassToPlateDescriptionData) cvPltTypeClassCombo
					.elementAt(
				aiDesc);
		lsPltCd = laClassToPltDescData.getRegPltCd();
		return lsPltCd;
	}

	/**
	 * Return the PlateType property value.
	 *
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getPlateType()
	{
		if (ivjPlateType == null)
		{
			try
			{
				ivjPlateType = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints2 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints2.insets =
					new java.awt.Insets(5, 6, 32, 3);
				consGridBagConstraints2.ipadx = 311;
				consGridBagConstraints2.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				consGridBagConstraints2.weightx = 1.0;
				consGridBagConstraints2.gridy = 0;
				consGridBagConstraints2.gridx = 0;
				ivjPlateType.setName("PlateType");
				ivjPlateType.setLayout(new java.awt.GridBagLayout());
				ivjPlateType.add(
					getcomboPlateType(),
					consGridBagConstraints2);
				Border laBorder =
					new TitledBorder(new EtchedBorder(), TXT_PLT_TYPE);
				ivjPlateType.setBorder(laBorder);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjPlateType;
	}

	/**
	 * Get the Registration class code for Registration class ComboBox
	 * for the index passed as parameter
	 *
	 * @param aiDesc int
	 * @return int
	 * @throws RTSException
	 */
	private int getRegCodeFromDesc(int aiDesc) throws RTSException
	{
		VehicleClassRegistrationClassData laRegCacheData =
			(
				VehicleClassRegistrationClassData) cvRegClassCombo
					.elementAt(
				aiDesc);
		int liRegCd = laRegCacheData.getRegClassCd();
		return liRegCd;
	}

	/**
	 * Return the RegistrationClass property value.
	 *
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getRegistrationClass()
	{
		if (ivjRegistrationClass == null)
		{
			try
			{
				ivjRegistrationClass = new JPanel();
				ivjRegistrationClass.setName("RegistrationClass");
				ivjRegistrationClass.setLayout(new GridBagLayout());
				GridBagConstraints constraintscomboRegClass =
					new GridBagConstraints();
				constraintscomboRegClass.gridx = 1;
				constraintscomboRegClass.gridy = 1;
				constraintscomboRegClass.fill =
					GridBagConstraints.HORIZONTAL;
				constraintscomboRegClass.weightx = 1.0;
				constraintscomboRegClass.ipadx = 224;
				constraintscomboRegClass.insets =
					new Insets(7, 4, 36, 5);
				getRegistrationClass().add(
					getcomboRegClass(),
					constraintscomboRegClass);
				// user code begin {1}
				Border laBorder =
					new TitledBorder(new EtchedBorder(), TXT_REG_CLASS);
				ivjRegistrationClass.setBorder(laBorder);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjRegistrationClass;
	}

	/**
	 * Return the Sticker Code from Sticker Type ComboBox
	 * for the index passed as a parameter
	 *
	 * @param aiDesc int
	 * @return String
	 * @throws RTSException 
	 */
	private String getStickerCodeFromDesc(int aiDesc)
		throws RTSException
	{
		PlateToStickerData laPltToStkrData =
			(PlateToStickerData) cvStkrTypeClassCombo.get(aiDesc);
		String lsStkrCd = laPltToStkrData.getRegStkrCd();
		return lsStkrCd;
	}

	/**
	 * Return the StickerType property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getStickerType()
	{
		if (ivjStickerType == null)
		{
			try
			{
				ivjStickerType = new JPanel();
				ivjStickerType.setName("StickerType");
				ivjStickerType.setLayout(new GridBagLayout());
				GridBagConstraints constraintscomboStickerType =
					new GridBagConstraints();
				constraintscomboStickerType.gridx = 1;
				constraintscomboStickerType.gridy = 1;
				constraintscomboStickerType.fill =
					GridBagConstraints.HORIZONTAL;
				constraintscomboStickerType.weightx = 1.0;
				constraintscomboStickerType.ipadx = 224;
				constraintscomboStickerType.insets =
					new Insets(8, 6, 35, 3);
				getStickerType().add(
					getcomboStickerType(),
					constraintscomboStickerType);
				// user code begin {1}
				Border laBorder =
					new TitledBorder(new EtchedBorder(), TXT_STKR_TYPE);
				ivjStickerType.setBorder(laBorder);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjStickerType;
	}

	/**
	 * Return the VehicleClass property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getVehicleClass()
	{
		if (ivjVehicleClass == null)
		{
			try
			{
				ivjVehicleClass = new JPanel();
				ivjVehicleClass.setName("VehicleClass");
				ivjVehicleClass.setLayout(new GridBagLayout());
				GridBagConstraints constraintscomboVehClass =
					new GridBagConstraints();
				constraintscomboVehClass.gridx = 1;
				constraintscomboVehClass.gridy = 1;
				constraintscomboVehClass.fill =
					GridBagConstraints.HORIZONTAL;
				constraintscomboVehClass.weightx = 1.0;
				constraintscomboVehClass.ipadx = 224;
				constraintscomboVehClass.insets =
					new Insets(7, 5, 36, 4);
				getVehicleClass().add(
					getcomboVehClass(),
					constraintscomboVehClass);
				// user code begin {1}
				Border laBorder =
					new TitledBorder(new EtchedBorder(), TXT_VEH_CLASS);
				ivjVehicleClass.setBorder(laBorder);
				// user code end
			}
			catch (Throwable aeEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeEx);
			}
		}
		return ivjVehicleClass;
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
	 * Determine one of many plate types for this RegClassCd
	 * 
	 * @param asRegPltCd String 
	 * @return boolean
	 */
	private boolean handleMultPltTypes(String asRegPltCd)
	{
		boolean lbFoundPltForVehClass = false;
		String lsVehClass_Selected = (String) getcomboVehClass().
				getSelectedItem();

		// For RegClass = Apportioned (6)
		if (lsVehClass_Selected.equals(VEH_CODE_TRK_LE1) &&
			asRegPltCd.equals(PLTCD_APPRTK))
		{
			lbFoundPltForVehClass = true;
		}
		else if (lsVehClass_Selected.equals(VEH_CODE_TRLR) &&
			asRegPltCd.equals(PLTCD_APPRTR)) 
		{
			lbFoundPltForVehClass = true;
		}
		// For RegClass = Exempt (39)
		else if((lsVehClass_Selected.equals(VEH_CODE_BUS) ||
				lsVehClass_Selected.equals(VEH_CODE_PASS) ||
				lsVehClass_Selected.equals(VEH_CODE_PASS_TRK) ||
				lsVehClass_Selected.equals(VEH_CODE_TRK_LE1) ||
				lsVehClass_Selected.equals(VEH_CODE_TRK_GT1)) 
					&& asRegPltCd.equals(PLTCD_EXPDBL)) 
		{
			lbFoundPltForVehClass = true;
		}
		else if((lsVehClass_Selected.equals(VEH_CODE_MOPED) ||
				lsVehClass_Selected.equals(VEH_CODE_MTRCYCLE)) 
					&& asRegPltCd.equals(PLTCD_EXPMC)) 
		{
			lbFoundPltForVehClass = true;
		}
		else if((lsVehClass_Selected.equals(VEH_CODE_MISC) ||
				lsVehClass_Selected.equals(VEH_CODE_TRACTOR) ||
				lsVehClass_Selected.equals(VEH_CODE_TRLR)) 
					&& asRegPltCd.equals(PLTCD_EXPSGL)) 
		{
			lbFoundPltForVehClass = true;
		}
 
 		return lbFoundPltForVehClass;
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
			setName(FRM_NAME_REG008);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(460, 464);
			setTitle(FRM_TITLE_REG008);
			setContentPane(
				getFrmClassPlateStickerTypeREG008ContentPane1());
		}
		catch (Throwable aeEx)
		{
			handleException(aeEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Determine if RegStkrCd is printable.
	 * 
	 * @param asRegStkrCd String 
	 * @return boolean
	 */
	private boolean isDTAToBePrinted(String asRegStkrCd)
	{
		// defect 9584
		// Restructure; Use class variables 
		boolean lbNotPrintable = false;

		// If DTA and Print Sticker is Selected, verify printable
		// sticker
		if (cbDTA
			&& caDlrTtlData != null
			&& caDlrTtlData.isToBePrinted())
		{
			lbNotPrintable = true;

			if (asRegStkrCd != null
				&& !asRegStkrCd.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				ItemCodesData laItmCodes =
					ItemCodesCache.getItmCd(asRegStkrCd);

				if (laItmCodes != null)
				{
					lbNotPrintable = laItmCodes.getPrintableIndi() == 0;
				}
			}
		}
		return lbNotPrintable;
		// end defect 9584 
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
			FrmClassPlateStickerTypeREG008 laFrmClassPlateStickerTypeREG008;
			laFrmClassPlateStickerTypeREG008 =
				new FrmClassPlateStickerTypeREG008();
			laFrmClassPlateStickerTypeREG008.setModal(true);
			laFrmClassPlateStickerTypeREG008
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmClassPlateStickerTypeREG008.show();
			Insets laInsets =
				laFrmClassPlateStickerTypeREG008.getInsets();
			laFrmClassPlateStickerTypeREG008.setSize(
				laFrmClassPlateStickerTypeREG008.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmClassPlateStickerTypeREG008.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmClassPlateStickerTypeREG008.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Populates the Plate Type ComboBox based on the selected 
	 * Registration Class Code and Effective Date
	 *
	 * @param aiRegClassCd int
	 * @param aiEffDt int 
	 * @throws RTSException 
	 */
	private void populatePlateTypeComboBox(
		int aiRegClassCd,
		int aiEffDt)
		throws RTSException
	{
		ivjcomboPlateType.removeActionListener(this);
		ivjcomboPlateType.removeAllItems();

		if ((ciRegWaivedIndi == 0) && (ciOffHwyUseIndi == 0))
		{
			int liSize;
			Vector lvClassPltDescData = new Vector();
			// defect 10600
			// Index for default Reg Class for this Veh Class 
			int liDefaultIndx = 0;
			// end defect 10600
			if (aiRegClassCd != 0 && aiEffDt != 0)
			{
				cvPltTypeClassCombo =
					ClassToPlateDescriptionCache.getClassToPltDescs(
						aiRegClassCd,
						aiEffDt);
				if (cvPltTypeClassCombo == null)
				{
					throw new RTSException(
						RTSException.FAILURE_MESSAGE,
						TXT_NULL_PLT_TYPE,
						TXT_ERROR);
				}
				liSize = cvPltTypeClassCombo.size();
				if (liSize == 0)
				{
					throw new RTSException(
						RTSException.FAILURE_MESSAGE,
						TXT_NULL_PLT_TYPE,
						TXT_ERROR);
				}

				for (int i = 0; i < liSize; i++)
				{
					ClassToPlateDescriptionData laClassToPlateDescData =
						(
							ClassToPlateDescriptionData) cvPltTypeClassCombo
								.get(
							i);
					lvClassPltDescData.addElement(
						laClassToPlateDescData.getItmCdDesc());
					// defect 10600
					if (laClassToPlateDescData.getDfltPltCdIndi() == 1)
					{
						// Some RegClassCds are 'multiple': there are
						// more than one PltType for these RegClassCd
						if (laClassToPlateDescData.getRegClassCd() ==
							REGCLASS_APPORTIONED ||
							laClassToPlateDescData.getRegClassCd() ==
							REGCLASS_EXEMPT)
						{
							if(handleMultPltTypes(laClassToPlateDescData.
								getRegPltCd()))
							{
								liDefaultIndx = i;
							}
						}
						else  
						{
							liDefaultIndx = i;
						}
					}
					// end defect 10600
				}
				ComboBoxModel laModel =
					new DefaultComboBoxModel(lvClassPltDescData);
				ivjcomboPlateType.setModel(laModel);
			}
			else
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					TXT_NULL_EFF_DT_OR_VEH_CD,
					TXT_ERROR);
			}
			// defect 10600
			// Defaults now contained in RTS_CLASS_TO_PLT
			// Indicator which tells whether PlateCd was found in 
			// vector of PlateCd
//			boolean lbPlateCdFoundIndi = false;
//			if (csPlateCd != null)
//			{
//				for (int i = 0; i < liSize; i++)
//				{
//					ClassToPlateDescriptionData laClassToPlateDescData =
//						(
//							ClassToPlateDescriptionData) cvPltTypeClassCombo
//								.get(
//							i);
//					if (laClassToPlateDescData
//						.getRegPltCd()
//						.equals(csPlateCd))
//					{
//						ivjcomboPlateType.setSelectedIndex(i);
//						lbPlateCdFoundIndi = true;
//                                                                                                                                                                                                                                                                                                                                                                                                                                   						break;
//					}
//				}
//			}
//
//			//Set Default Plate Types depending on Reg Class selected
//			
//			if (csPlateCd == null)
//			{
				ivjcomboPlateType.setSelectedIndex(liDefaultIndx);
//			}
//			if (csPlateCd == null || !lbPlateCdFoundIndi)
//			{
//
//				if (ivjcomboRegClass
//					.getSelectedItem()
//					.equals(PASSENGER1)
//					|| ivjcomboRegClass.getSelectedItem().equals(
//						PASSENGER2))
//				{
//					for (int i = 0; i < liSize; i++)
//					{
//
//						ClassToPlateDescriptionData laClassToPlateDescData =
//							(
//								ClassToPlateDescriptionData) cvPltTypeClassCombo
//									.get(
//								i);
//						if (laClassToPlateDescData
//							.getItmCdDesc()
//							.equals(CommonFeesConstant.PASSENGER_PLT))
//						{
//							ivjcomboPlateType.setSelectedIndex(i);
//						}
//					}
//				}
//				else if (
//					ivjcomboRegClass.getSelectedItem().equals(TRUCK1)
//						|| ivjcomboRegClass.getSelectedItem().equals(
//							TRUCK2))
//				{
//					for (int i = 0; i < liSize; i++)
//					{
//
//						ClassToPlateDescriptionData laClassToPlateDescData =
//							(
//								ClassToPlateDescriptionData) cvPltTypeClassCombo
//									.get(
//								i);
//						if (laClassToPlateDescData
//							.getItmCdDesc()
//							.equals(CommonFeesConstant.TRUCK_PLT))
//						{
//							ivjcomboPlateType.setSelectedIndex(i);
//						}
//					}
//				}
//				else if (liSize != 0)
//				{
//					ivjcomboPlateType.setSelectedIndex(0);
//				}
//			}
			// end defect 10600
		}
		ivjcomboPlateType.addActionListener(this);
	}

	/**
	 * Populates the Registration Class ComboBox based on the selected 
	 * Vehicle Class Code and Effective Date
	 * 
	 * @param aiEffDt int
	 * @param asVehCd String
	 * @throws RTSException 
	 */
	private void populateRegClassComboBox(int aiEffDt, String asVehCd)
		throws RTSException
	{
		ivjcomboRegClass.removeActionListener(this);
		ivjcomboRegClass.removeAllItems();
		Vector lvRegDescsCombo = new Vector();
		// defect 10600
		// Index for default Reg Class for this Veh Class 
		int liDefaultIndx = 0;
		// end defect 10600
		int liSize;
		if (aiEffDt != 0 && asVehCd != null)
		{
			cvRegClassCombo =
				VehicleClassRegistrationClassCache
					.getVehClassRegClassDescs(
					asVehCd,
					aiEffDt);
			if (cvRegClassCombo == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					TXT_NULL_REG_CLS,
					TXT_ERROR);
			}
			liSize = cvRegClassCombo.size();
			if (liSize == 0)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					TXT_NULL_REG_CLS,
					TXT_ERROR);
			}
			for (int i = 0; i < liSize; i++)
			{
				VehicleClassRegistrationClassData caRegCacheData =
					(
						VehicleClassRegistrationClassData) cvRegClassCombo
							.get(
						i);
				lvRegDescsCombo.addElement(
					(String) caRegCacheData.getRegClassCdDesc());
				// defect 10600
				if (caRegCacheData.getDfltRegClassCdIndi() == 1)
				{
					liDefaultIndx = i; 
				}
				// end defect 10600
			}
		}
		else
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				TXT_NULL_EFF_DT_OR_VEH_CD,
				TXT_ERROR);
		}
		//Indicator which tells whether RegClassCd was found in vector 
		// of RegClassCd
		boolean lbRegClassCdIndi = false;
		if (ciRegClassCd != 0)
		{
			ComboBoxModel laModel1 =
				new DefaultComboBoxModel(lvRegDescsCombo);
			ivjcomboRegClass.setModel(laModel1);
			for (int i = 0; i < liSize; i++)
			{
				VehicleClassRegistrationClassData laRegCacheData =
					(
						VehicleClassRegistrationClassData) cvRegClassCombo
							.get(
						i);
				if (laRegCacheData.getRegClassCd() == ciRegClassCd)
				{
					ivjcomboRegClass.setSelectedIndex(i);
					lbRegClassCdIndi = true;
					break;
				}
			}
		}

		//Set default Reg Class depending on the selected Veh Class
		if (ciRegClassCd == 0 || !lbRegClassCdIndi)
		{
			ComboBoxModel laModel1 =
				new DefaultComboBoxModel(lvRegDescsCombo);
			ivjcomboRegClass.setModel(laModel1);
			
			// defect 10600
			// Defaults now contained in RTS_REGIS_CLASS
			ivjcomboRegClass.setSelectedIndex(liDefaultIndx);
//			if (asVehCd.equals(VEH_CODE_PASS))
//			{
//				for (int i = 0; i < liSize; i++)
//				{
//					VehicleClassRegistrationClassData laRegCacheData =
//						(
//							VehicleClassRegistrationClassData) cvRegClassCombo
//								.get(
//							i);
//					if (laRegCacheData.getRegClassCd()
//						== CommonFeesConstant.PASSENGER_LESS_EQL_6000_CD)
//					{
//						ivjcomboRegClass.setSelectedIndex(i);
//					}
//				}
//			}
//			else if (asVehCd.equals(TRK_LESS_ONE))
//			{
//				for (int i = 0; i < liSize; i++)
//				{
//					VehicleClassRegistrationClassData laRegCacheData =
//						(
//							VehicleClassRegistrationClassData) cvRegClassCombo
//								.get(
//							i);
//					if (laRegCacheData.getRegClassCd()
//						== CommonFeesConstant.TRUCK_LESS_EQL_1_TON_CD)
//					{
//						ivjcomboRegClass.setSelectedIndex(i);
//					}
//				}
//			}
//			else if (asVehCd.equals(TRK_GT_ONE))
//			{
//				for (int i = 0; i < liSize; i++)
//				{
//					VehicleClassRegistrationClassData laRegCacheData =
//						(
//							VehicleClassRegistrationClassData) cvRegClassCombo
//								.get(
//							i);
//					if (laRegCacheData.getRegClassCd()
//						== CommonFeesConstant.TRUCK_MORE_THAN_1_TON_CD)
//					{
//						ivjcomboRegClass.setSelectedIndex(i);
//					}
//				}
//			}
//			else if (liSize != 0)
//			{
//				ivjcomboRegClass.setSelectedIndex(0);
//			}
			// end defect 10600
		}
		ivjcomboRegClass.addActionListener(this);
	}

	/**
	 * Populates the Sticker Type ComboBox based on the selected 
	 * Registration Class Code, Registration Plate code and 
	 * Effective Date
	 * 
	 * @param aiRegClassCd int 
	 * @param asRegPlateCd String
	 * @param aiEffDt int
	 * @throws RTSException 
	 */
	private void populateStkrTypeComboBox(
		int aiRegClassCd,
		String asRegPlateCd,
		int aiEffDate)
		throws RTSException
	{
		ivjcomboStickerType.removeActionListener(this);
		ivjcomboStickerType.removeAllItems();
		ivjcomboStickerType.setEnabled(false);
		// defect 4707
		// clear out the vector before adding to it.
		cvStkrTypeClassCombo.clear();
		// end defect 4707
		// defect 10623
		// Do not set Sticker for Token Trailer
		ciStkrReqdIndi = 0;
		if ((ciRegWaivedIndi == 0) && (ciOffHwyUseIndi == 0))
//			&& aiRegClassCd != RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
		{
			// end defect 10623
			int liSize = 0;
			int liIndex = -1;
			Vector lvItmCodes = new Vector();
			// defect 10600
			// Index for default Sticker for this Plate Cd 
			int liDefaultIndx = -1;
			// end defect 10600
			// defect 8218 
			Vector lvPltToStkrData = new Vector();
			lvItmCodes = getItemCds();
			for (int i = 0; i < lvItmCodes.size(); i++)
			{
				ItemCodesData lsItemCodesData =
					(ItemCodesData) lvItmCodes.get(i);
				String lsRegStkrCd = lsItemCodesData.getItmCd();
				if (aiRegClassCd != 0
					&& aiEffDate != 0
					&& asRegPlateCd != null
					&& lsRegStkrCd != null)
				{
					lvPltToStkrData =
						RegistrationPlateStickerCache.getPltStkrs(
							aiRegClassCd,
							asRegPlateCd,
							aiEffDate,
							lsRegStkrCd);
					if (lvPltToStkrData == null)
					{
						continue;
					}
					liSize = lvPltToStkrData.size();
					if (liSize == 0)
					{
						continue;
					}
					else
					{
						PlateToStickerData laPltToStkrData =
							(PlateToStickerData) lvPltToStkrData.get(0);

						cvStkrTypeClassCombo.addElement(
							laPltToStkrData);
						liIndex = liIndex + 1;
						// defect 10600
						// Capture index of default Stkr for this Plt
						if (laPltToStkrData.getDfltStkrCdIndi() == 1)
						{
							liDefaultIndx = liIndex; 
						}
						ivjcomboStickerType.setEnabled(true);
						ivjcomboStickerType.addItem(
							lsItemCodesData.getItmCdDesc());
//						if (lsItemCodesData
//							.getItmCdDesc()
//							.equals(WINDSHIELD))
//						{
//							liIndex =
//								ivjcomboStickerType.getItemCount() - 1;
//						}
						// end defect 10600
					}
				}
				else
				{
					throw new RTSException(
						RTSException.FAILURE_MESSAGE,
						TXT_NULL_EFF_DT_OR_VEH_CD,
						TXT_ERROR);
				}
			}
			ciStkrReqdIndi = ivjcomboStickerType.getItemCount();

			//Set the default selection for Stkr Type ComboBox
			if (csStkrCd != null)
			{
				for (int i = 0; i < cvStkrTypeClassCombo.size(); i++)
				{
					PlateToStickerData laPltToStkrData =
						(PlateToStickerData) cvStkrTypeClassCombo.get(
							i);
					if (laPltToStkrData
						.getRegStkrCd()
						.equals(csStkrCd))
					{
						ivjcomboStickerType.setSelectedIndex(i);
						break;
					}
				}
			}
			else
			// defect 10600
			// Defaults now contained in RTS_PLT_TO_STKR
			// Note that currently, not all Plate Types have a default.
			// If it hasn't already been set (ie: there is no default
			// for this Plate Type, and there is only one sticker
			// choice, use that, else default to 'WS'
			// If liDefaultIndx != -1, then there was a default, use it.
			if (liDefaultIndx == -1)
			{
				if (cvStkrTypeClassCombo.size() == 1)
				{
					ivjcomboStickerType.setSelectedIndex(0);
				}
				else
				{
					ivjcomboStickerType.setSelectedItem(WINDSHIELD);
				}
			}
			else
			{
				ivjcomboStickerType.setSelectedIndex(liDefaultIndx);
			}
//			{
//				if (liIndex != -1)
//				{
//					ivjcomboStickerType.setSelectedIndex(liIndex);
//					getcomboStickerType().setEnabled(true);
//				}
//				else if (ciStkrReqdIndi != 0)
//				{
//					ivjcomboStickerType.setSelectedIndex(0);
//					getcomboStickerType().setEnabled(true);
//				}
//			}
			// end defect 10600
			ivjcomboStickerType.addActionListener(this);
		}
	}

	/**
	 * Populates the Vehicle Class ComboBox based on the 
	 * Effective Date
	 * 
	 * @param aiEffDt int
	 * @throws RTSException 
	 */
	private void populateVehClassComboBox(int aiEffDt)
		throws RTSException
	{
		ivjcomboVehClass.removeActionListener(this);
		ivjcomboVehClass.removeAllItems();
		if (aiEffDt != 0)
		{
			cvVehClassCombo =
				RegistrationClassCache.getVehClassCds(aiEffDt);
			if (cvVehClassCombo == null)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					TXT_NULL_VEH_CLS,
					TXT_ERROR);
			}
			int liSize = cvVehClassCombo.size();
			if (liSize == 0)
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					TXT_NULL_VEH_CLS,
					TXT_ERROR);
			}
			for (int i = 0; i < liSize; i++)
			{
				RegistrationClassData laVehCdCacheData =
					(RegistrationClassData) cvVehClassCombo.get(i);
				ivjcomboVehClass.addItem(
					(String) laVehCdCacheData.getVehClassCd());
			}
		}
		else
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				TXT_NULL_EFF_DT,
				TXT_ERROR);
		}
		ivjcomboVehClass.addActionListener(this);
	}

	/**
	 * All subclasses must implement this method -
	 * it sets the data on the screen and 
	 * is how the controller relays information to the view
	 *
	 * @param aaDataObject Object 
	 */
	public void setData(Object aaDataObject)
	{
		csTransCd = getController().getTransCode();

		// defect 9086
		// aaDataObject = null means Cancelled from SPL002
		//  or in Plate Options 
		// Set setEnterOnClassPltStkr = false
		if (aaDataObject == null
			&& !csTransCd.equals(TransCdConstant.PLTOPT))
		{
			RegistrationValidationData laRegValidData = null;

			// defect 8900
			if (caVehInqData != null
				&& caVehInqData.getValidationObject()
					instanceof RegistrationValidationData)
			{
				laRegValidData =
					(RegistrationValidationData) caVehInqData
						.getValidationObject();
				laRegValidData.setEnterOnClassPltStkr(false);
			}
			return;
			// end defect 9086					
		}
		if (aaDataObject != null)
		{
			caVehInqData = (VehicleInquiryData) aaDataObject;

			RegistrationData laRegData =
				caVehInqData.getMfVehicleData().getRegData();

			// defect 8900
			if (laRegData != null)
			{
				cbOrigStandardExempt =
					CommonFeesCache.isStandardExempt(
						laRegData.getRegClassCd());
				caOrigRegData =
					(RegistrationData) UtilityMethods.copy(laRegData);
			}
			// end defect 8900

			// defect 9584 
			// Determine if DTA Transaction 
			cbDTA = UtilityMethods.isDTA(csTransCd);
			if (cbDTA)
			{
				TitleValidObj laValidObj =
					(TitleValidObj) caVehInqData.getValidationObject();
				if (laValidObj != null)
				{
					caDlrTtlData =
						(DealerTitleData) laValidObj.getDlrTtlData();

				}
			}
			// end defect 9584 

			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();

			// defect 8509 
			// Establish earlier if RegWaived or OffHwyUse 
			// Once code is in place for RTS Effective Date then 
			// it should be set from caVehInqData
			ciRTSEffDt = caVehInqData.getRTSEffDt();
			ciRegWaivedIndi = laRegData.getRegWaivedIndi();
			ciOffHwyUseIndi = laRegData.getOffHwyUseIndi();
			// end defect 8509 

			// defect 8393 
			// Only set to default when VehClassCd is null 
			//			if (caVehInqData.getMfDown() == 1
			//				&& !(csTransCd.equals(TransCdConstant.NONTTL)
			//				&& caVehInqData.getNoMFRecs() == 0))
			if (laVehData.getVehClassCd() == null)
			{
				try
				{
					RTSDate laRTSEffDt = new RTSDate();
					ciRTSEffDt = laRTSEffDt.getYYYYMMDDDate();
					populateVehClassComboBox(ciRTSEffDt);
					ivjcomboVehClass.setSelectedItem(VEH_CODE_PASS);
				}
				catch (Throwable aeEx)
				{
					handleException(aeEx);
				}
			}
			// end defect 8393

			// These values will be used to initially set values in the 
			// combo boxes
			if (laRegData.getRegClassCd() != 0)
			{
				ciRegClassCd = laRegData.getRegClassCd();
			}
			if (caVehInqData
				.getMfVehicleData()
				.getRegData()
				.getRegPltCd()
				!= null
				&& !(laRegData
					.getRegPltCd()
					.trim()
					.equals(CommonConstant.STR_SPACE_EMPTY)))
			{
				csPlateCd = laRegData.getRegPltCd();
			}
			if (laRegData.getRegStkrCd() != null
				&& !(laRegData
					.getRegStkrCd()
					.trim()
					.equals(CommonConstant.STR_SPACE_EMPTY)))
			{
				csStkrCd = laRegData.getRegStkrCd();
			}
			if (laVehData.getVehClassCd() != null
				&& !(laVehData
					.getVehClassCd()
					.trim()
					.equals(CommonConstant.STR_SPACE_EMPTY)))
			{
				csVehClassCd = laVehData.getVehClassCd();
				if (laVehData.getDisableVehClassIndi() == 1
					&& (getController()
						.getTransCode()
						.equals(TransCdConstant.RENEW)
						|| csVehClassCd.equals(VEH_CODE_MISC)))
				{
					getcomboVehClass().addItem(csVehClassCd);
					getcomboVehClass().setEnabled(false);
					ciRegClassCd = 0;
					csPlateCd = null;
					csStkrCd = null;
					csVehClassCd = null;
				}
				else
				{
					try
					{
						populateVehClassComboBox(ciRTSEffDt);
						// defect 5173
						getcomboVehClass().setSelectedItem(
							VEH_CODE_PASS);
						int laVehClassComboSize =
							cvVehClassCombo.size();
						for (int i = 0; i < laVehClassComboSize; i++)
						{
							RegistrationClassData laVehCdCacheData =
								(
									RegistrationClassData) cvVehClassCombo
										.get(
									i);
							if (laVehCdCacheData
								.getVehClassCd()
								.equals(csVehClassCd))
							{
								getcomboVehClass().setSelectedItem(
									csVehClassCd);
								break;
							}
						} // end defect 5173  
						ciRegClassCd = 0;
						csPlateCd = null;
						csStkrCd = null;
						csVehClassCd = null;
					}
					catch (RTSException aeRTSEx)
					{
						aeRTSEx.displayError(this);
					}
				}
			}
			else
			{
				try
				{
					RTSDate laRTSEffDt = new RTSDate();
					ciRTSEffDt = laRTSEffDt.getYYYYMMDDDate();
					populateVehClassComboBox(ciRTSEffDt);
					ivjcomboVehClass.setSelectedItem(VEH_CODE_PASS);
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.printStackTrace();
				}
			}
			if ((ciRegWaivedIndi == 1) || (ciOffHwyUseIndi == 1))
			{
				ivjcomboStickerType.removeAllItems();
				ivjcomboPlateType.removeAllItems();
				getcomboPlateType().setEnabled(false);
				getcomboStickerType().setEnabled(false);
			}
		}
		//	User looking through Vehicle Options
		else
		{
			try
			{
				RTSDate laRTSEffDt = new RTSDate();
				ciRTSEffDt = laRTSEffDt.getYYYYMMDDDate();
				populateVehClassComboBox(ciRTSEffDt);
				ivjcomboVehClass.setSelectedItem(VEH_CODE_PASS);
			}
			catch (Throwable aeEx)
			{
				handleException(aeEx);
			}
		}
		if (getcomboVehClass().isEnabled())
		{
			comboBoxHotKeyFix(getcomboVehClass());
		}
		if (getcomboStickerType().isEnabled())
		{
			comboBoxHotKeyFix(getcomboStickerType());
		}
		if (getcomboRegClass().isEnabled())
		{
			comboBoxHotKeyFix(getcomboRegClass());
		}
		if (getcomboPlateType().isEnabled())
		{
			comboBoxHotKeyFix(getcomboPlateType());
		}
		// defect 8494
		if (getcomboVehClass().isEnabled())
		{
			setDefaultFocusField(getcomboVehClass());
		}
		else if (getcomboRegClass().isEnabled())
		{
			setDefaultFocusField(getcomboRegClass());
		}
		else if (getcomboPlateType().isEnabled())
		{
			setDefaultFocusField(getcomboPlateType());
		}
		else if (getcomboStickerType().isEnabled())
		{
			setDefaultFocusField(getcomboStickerType());
		}
		// end defect 8494
	}

	/**
	 * Controller for field validations.
	 * Return true if no validation errors; Else return false.
	 *  
	 * @return boolean
	 */
	private boolean validateFields()
	{
		// Corrected validation fields for exceptions  
		boolean lbReturn = true;
		RTSException leEx = new RTSException();

		boolean lbSelectionStandardExempt =
			CommonFeesCache.isStandardExempt(ciTempRegClassCd);

		// defect 10453 
		// If (ATV or ROV) && !OffHwyUse, throw error  
		if ((ciTempRegClassCd == RegistrationConstant.ATV_REGCLASSCD
			|| ciTempRegClassCd == RegistrationConstant.ROV_REGCLASSCD)
			&& ciOffHwyUseIndi == 0)
		{
			// If RPO, clerk must also change Title Type (to Original) 
			// 			before 'Off-Highway' is enabled. 
			boolean lbRPO =
				caVehInqData
					.getMfVehicleData()
					.getTitleData()
					.getTtlTypeIndi()
					== TitleTypes.INT_REGPURPOSE;

			String lsAppend =
				lbRPO
					? ERR_MSG_2022_RPO_ONLY_APPEND
					: ERR_MSG_2022_APPEND;

			leEx.addException(
				new RTSException(
					ErrorsConstant.ERR_MSG_NO_REG_FOR_ATV_ROV,
					new String[] { lsAppend }),
				getcomboRegClass());
		}
		// end defect 10453 

		// defect 9086
		// Check PlateTypeCd, is DTA and Special Plate
		//	Set error message.
		boolean lbSelectSpclPlate =
			PlateTypeCache.isSpclPlate(csTempPlateCd);

		if (lbSelectSpclPlate & cbDTA)
		{
			leEx.addException(
				new RTSException(2017),
				getcomboPlateType());
		}
		// end defect 9086

		// defect 9584 
		// Check for PTO Eligible when Customer Supplied && DTA
		boolean lbPTOEligible =
			ClassToPlateCache.isPTOEligible(
				ciTempRegClassCd,
				csTempPlateCd);

		if (cbDTA
			&& !lbPTOEligible
			&& caDlrTtlData != null
			&& caDlrTtlData.isCustSuppliedPlt())
		{
			leEx.addException(
				new RTSException(2021),
				getcomboRegClass());

			leEx.addException(
				new RTSException(2021),
				getcomboPlateType());
		}
		// end defect 9584 

		// If Selection Standard Exempt && (Renew || DTA)
		if (lbSelectionStandardExempt
			&& (csTransCd.equals(TransCdConstant.RENEW) || cbDTA))
		{
			leEx.addException(
				new RTSException(471),
				getRegistrationClass());
		}
		// Is user authorized to change from/to Standard Exempt 
		boolean lbExemptAuth =
			(getController()
				.getMediator()
				.getDesktop()
				.getSecurityData()
				.getExmptAuthAccs()
				== 1);

		// If Selection Standard Exempt and !Authorized 			
		if (lbSelectionStandardExempt && !lbExemptAuth)
		{
			leEx.addException(
				new RTSException(2013),
				getcomboRegClass());
		}
		// If Original Standard Exempt 
		//  && !(Selected Standard Exempt)
		//  && !Authorized 			
		if (cbOrigStandardExempt
			&& !lbSelectionStandardExempt
			&& !lbExemptAuth)
		{
			leEx.addException(
				new RTSException(2013),
				getcomboRegClass());
		}

		if (leEx.isValidationError())
		{
			leEx.displayError(this);
			leEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
	}
}