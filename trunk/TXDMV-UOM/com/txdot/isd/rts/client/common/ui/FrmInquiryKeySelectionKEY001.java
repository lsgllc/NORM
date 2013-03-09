package com.txdot.isd.rts.client.common.ui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.event.BarCodeEvent;
import com.txdot.isd.rts.services.util.event.BarCodeListener;

/*
 *
 * FrmInquiryKeySelectionKEY001.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Joe Peters   09/12/2001	Added Comments and intitialized screen items
 * Joe Peters   09/18/2001	Added Validation
 * MAbs			04/16/2002	Fixing dead screen after cancelled license 
 *							plate 
 *							defect 3503
 * N Ting		04/17/2002	Global change for startWorking() and 
 *							doneWorking()   
 * T Pederson   04/30/2002  Don't allow spaces to be entered in input 
 *							field
 * MAbs			05/06/2002	Pressing Alt+A will not show "Charge Fee" 
 *							defect 3777
 * MAbs			05/10/2002	Moving focus back to textfield after all 
 *							radio buttons 
 *							defect 3854
 * MAbs			05/10/2002	Allowing mouse presses on radio buttons 
 *							defect 3886
 * J Kwik		05/14/2002	store RegistrationModifyData object in 
 *							VehicleInquiryData when Same Vehicle is 
 *							selected
 *							defect 3897
 * S Govindappa 11/25/2002  Made changes to 
 *							actionPerformed() to prevent application 
 *							freezes when escaping back fast from the 
 *							REG003 screens by checking whether KEY001 
 *							screen is visible.
 *							defecgt 4991. 
 * B Arredondo	12/16/2002	Made changes for the 
 *							user help guide so had to make changes
 *							in actionPerformed().
 *							defect 5147. 
 * Ray Rowehl	04/29/2003	Clear color before starting edits in 
 *							actionPerformed.
 *							defect 5702
 * Bob Brown	09/19/2003	Commented out a gettxtInquiryKey().
 *							requestFocus() statement in
 *                          methods actionPerformed and 
 *							focusTxtField, so, after a record is 
 *							not found in active, inactive, or 
 *							archive, and KEY001 is displayed
 *                          again, tabbing works and you can 
 *							change the inquiry field.
 *                          defect 6079 Ver 5.1.6  
 * Min Wang     11/05/2003  Make Shift Tab key to work.
 *							modified getButtonPanel1(), 
 *							gettxtInquiryKey()
 * 							defect 6631  Ver 5.1.6
 * K Harrell	12/10/2003	Do not remove last 0 of Sticker No. 
 *							Was receiving null pointer exception.
 *							modified removeLeadingZeros()
 *							defect 6521 Ver 5.1.6
 * Min Wang 	03/23/2004	Fixed Cusor focus is on Enter Key on 
 *							KEY001 screen when "record not found 
 *							in active, check inactive", and 
 *                          answer OK on RTS057.
 *							modified actionPerformed(), 
 *							gettxtInquiryKey()
 * 							defect 6958  Ver 5.1.6
 * K Harrell	04/29/2004	remove sticker radio button
 *							modified getRadioPanel()
 *							modified isRadioButtonInContext()
 *							modified actionPerformed()
 *							modified setData()
 *							modified keyPressed()
 *							deleted getradioSticker()
 *							defect 5951 Ver 5.2.0
 * K Harrell	05/05/2004	Regenerate for BuilderData
 *							Ver 5.2.0 
 * Ray Rowehl	02/08/2005	Change package for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * T Pederson	03/14/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3
 * K Harrell	06/20/2005	Java 1.4 Work
 * 							delete removeLeadingZeros(),
 * 							stickerKeyValidation(),
 * 							modify processEnter()
 * 							defect 7885 Ver 5.2.3  
 * S Johnston	06/23/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify getButtonPanel1, keyPressed
 * 							defect 8240 Ver 5.2.3  
 * B Hargrove	06/27/2005	Refactor\Move 
 * 							RegistrationModifyData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * S Johnston	06/28/2005	Fixed the arrow key handling on view / view
 * 							& print radio buttons
 * 							defect 7885 Ver 5.2.3
 * T Pederson	07/18/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/25/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/08/2005	setRequestFocus(false)
 * 							modify initialize()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/14/2005	Correct alignment of Plate/Vin/Doc No
 * 							modify via Visual Editor 
 * 							defect 7885 Ver 5.2.3   
 * Jeff S.		12/19/2005	Changed ButtonGroup to use RTSButtonGroup.
 * 							remove FocusListener, focusGained(), 
 * 								focusLost(), cbIsKeyPressed, 
 * 								cbIsKey1EndPressed, keyPressed(), 
 * 								keyReleased()
 * 							modify getRadioPanel(), getradioDocument(), 
 * 								getradioPlate(), getradioPrint(), 
 * 								getradioView(), getradioViewPrint(), 
 * 								getradioVIN(), gettxtInquiryKey(),
 * 								getchkSame(), actionPerformed(), 
 * 								itemStateChanged()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	12/27/2005	Disable Inquiry Key Fields, Radio Buttons
 * 							when Same Vehicle checked.
 * 							add ItemListener
 * 							delete ciItemStateChangeCount
 * 							delete focusGained(), focusLost()
 * 							modify actionPerformed(),getchkSame(),
 * 							getchkCharge(),	itemStateChanged(),
 * 							getradioView(),getradioViewPrint()
 * 							rename ciWkstnId to ciOfcIssuanceCd,
 * 							enableCheckBox() to enableChkCharge()
 * 							defect 8485 Ver 5.2.3
 * T. Pederson	12/29/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * Jeff S.		02/02/2006	Moved forcus to the default field everytime 
 * 							we go through actionPerformed().  This will
 * 							make the focus be in the correct field every
 * 							time we return to this screen.
 * 							remove focusTxtField()
 * 							modify actionPerformed(), initialize()
 * 							defect 7885 Ver 5.2.3
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify vinKeyValidation()
 * 							defect 8902 Ver Exempts
 * K Harrell	02/12/2007	add ivjchkNoVehicle,getchkNoVehicle()
 * 							deleted commented out methods, keyPressed(),	
 * 							  keyReleased(),isRadioButtonInContext()
 * 							modify setData(), itemStateChanged(),
 * 							  getOptionsPanel()
 * 							defect 9085 Ver Special Plates
 * J Rue		02/14/2007	Send data = NULL if NO VEHICLE box is check.
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * J Rue		02/23/2007	Correct if/else block for Same Vehicle	
 * 							modify actionPerformed()
 * 							defect 9086 Ver Special Plates
 * K Harrell	04/06/2007	Implement SystemProperty.isHQ(), etc.
 * 							CommonValidations.convert_i_and_o_to_1_and_0() 
 * 							call is now static
 * 							delete COUNTY,REGION,HQ,ciOfcIssuanceCd
 * 							modify actionPerformed(),setData(),
 * 							 enableChkCharge()
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/01/2007	Use UtilityMethods.isSPAPPL() vs. check for
 * 							SPAPPL 
 * 							modify itemStateChanged()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/02/2007	Did not disable No Vehicle if not Special
 * 							Plate. 
 * 							modify setData()
 * 							defect 9085 Ver Special Plates 
 * B Hargrove 	05/05/2008 	Modify the input field Max Length property
 * 							from 30 to 22.
 * 							modify gettxtInquiryKey()
 * 							defect 8537 Ver Defect POS A
 * J Zwiener	07/07/2009	Remove "Plate is 7 Digits" message.
 * 							delete MAX_PLATE_NO, MAX_VIN_NO, 
 * 							 MAX_DOC_NO, BOTH_ADDL_AND_CUMUL
 * 							Also general cleanup.
 * 							modify plateKeyValidation()
 * 							defect 10091 Ver Defect_POS_F
 * R Pilon		06/10/2011	Implement Special Plate Inquiry
 * 							add validateData()
 * 							deleted dispInvalidFieldEntryExp(), 
 * 							  documentKeyValidation(), 
 * 							  plateKeyValidation(),
 * 							  vinKeyValidation()
 * 							modify actionPerformed(), processEnter(),
 * 							  setData()
 * 							defect 10820 Ver 6.8.0
 * K Harrell	11/08/2011	Restored to 6/10/2011 version 
 * B Woodson				defect 11052 Ver 6.9.0 
 * K Harrell	11/14/2011	Modify to accommodate VTR275
 * 							add TXT_CERTIFIED
 * 							add ivjradioCertified, get method
 * 							add getPrintOptions() 
 * 							delete ivjradioPrint, get method
 * 							modify actionPerformed(), enableChkCharge(),
 * 							  processEnter(), setData()    
 * 							defect 11052 Ver 6.9.0  
 * K Harrell	11/20/2011	Add logic to prevent redirect print on 
 * 							VTR275 print
 * 							modify actionPerformed() 
 * 							defect 11052 Ver 6.9.0
 * K Harrell	02/14/2012	Disabled 3rd Radio button (Certified) vs. 
 * 							just make invisible
 * 							modify setData(), getradioCertified() 
 * 							defect 11285 Ver 6.9.0 
 * K Harrell	02/15/2012	Make Charge Fee visible, enabled, selected
 * 							for Region when Certified Selected after 
 * 							View Only
 * 							add TXT_NON_CERTIFIED
 * 							modify enableChkCharge(), getchkCharge(), 
 * 							  itemStateChanged(), getradioCertified()  
 * 							defect 11289 Ver 6.10.0 
 * R Pilon		06/13/2012  Change to only log the exception when attempting to 
 * 							initialize the BarCodeScanner.
 * 							modify setData(Object)
 * 							defect 11071 Ver 7.0.0
 * ---------------------------------------------------------------------
 */
/**
 * This class is responsible for validating search keys 
 * for MF record retrieval.
 * 
 * @version	POS_700		06/13/2012
 * @author	Joseph Peters
 * @since  			06/25/2001 17:16:14 
 */

public class FrmInquiryKeySelectionKEY001
	extends RTSDialogBox
	implements BarCodeListener, ActionListener, ItemListener
{
	private RTSButtonGroup caRadioGroup;
	private RTSButtonGroup caRadioGroupView;

	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSame = null;
	private JCheckBox ivjchkCharge = null;
	private JCheckBox ivjchkNoVehicle = null;
	private JLabel ivjstcLblInquiry = null;
	private JPanel ivjOptionsPanel = null;
	private JPanel ivjPanelInquiryKey = null;
	private JPanel ivjRadioPanel = null;
	private JPanel ivjJDialogBoxContentPane = null;
	private JRadioButton ivjradioDocument = null;
	private JRadioButton ivjradioPlate = null;
	private JRadioButton ivjradioVIN = null;
	private JRadioButton ivjradioCertified = null;
	private JRadioButton ivjradioView = null;
	private JRadioButton ivjradioViewPrint = null;
	private RTSInputField ivjtxtInquiryKey = null;

	// boolean
	private boolean cbSameVehicle = false;

	// String
	private String csTransCd;

	// Object 
	private GeneralSearchData caSearchCriteria =
		new GeneralSearchData();
	private VehicleInquiryData caInquiryData = new VehicleInquiryData();
	private MFVehicleData caVehData = new MFVehicleData();

	// Constant
	private static final String SAME_SPECIAL_PLATE =
		"Same Special Plate";

	// Text Constants 
	private final static String FRM_NAME_KEY001 =
		"FrmInquiryKeySelectionKEY001";
	private final static String FRM_TITLE_KEY001 =
		"Inquiry Key Selection     KEY001";
	private final static String TXT_CHRG_FEE = "Charge Fee";
	private final static String TXT_SAME_VEH = "Same Vehicle";
	private final static String TXT_SEL_INQ_TYPE =
		"Select Inquiry Type:";
	private final static String TXT_DOC_NO = "Document No";
	private final static String TXT_SEL_APRPT_KEY =
		"Select Appropriate Key:";
	private final static String TXT_PLT_NO = "Plate No";
	// defect 11052 
	private final static String TXT_CERTIFIED = "Certified";
	// end defect 11052
	// defect 11289 
	private final static String TXT_NON_CERTIFIED = "Non-Certified";
	// end defect 11289 
	private final static String TXT_VW_ONLY = "View Only";
	private final static String TXT_VW_PRNT_ONLY = "View and Print";
	private final static String TXT_VIN = "VIN";
	private final static String TXT_INQ_KEY = "Inquiry Key:";

	/**
	 * Creates a FrmInquiryKeySelectionKEY001
	 */
	public FrmInquiryKeySelectionKEY001()
	{
		super();
		initialize();
	}

	/**
	 * Creates a FrmInquiryKeySelectionKEY001
	 * 
	 * @param aaOwner JDialog
	 */
	public FrmInquiryKeySelectionKEY001(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Creates a FrmInquiryKeySelectionKEY001
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInquiryKeySelectionKEY001(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * Enter will perform some validation and pass control on to
	 * the business layer
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking() || !isVisible())
		{
			return;
		}
		try
		{
			RTSException leEx = new RTSException();
			
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				clearAllColor(this);
			
				// defect 11052 
				UtilityMethods.trimRTSInputField(this);
				if (gettxtInquiryKey().isEmpty()
						// end defect 11052 
						&& (!(getchkSame().isVisible()
								&& getchkSame().isSelected())
								&& !(getchkNoVehicle().isVisible()
										&& getchkNoVehicle().isSelected())))
				{
					leEx.addException(
							new RTSException(46),
							gettxtInquiryKey());
				}
				else
				{ 
					// defect 11052 
					if (!SystemProperty.isCounty() && !getradioView().isSelected()) 
					{
						AssignedWorkstationIdsData laAssgndWsIdsData = 
							AssignedWorkstationIdsCache.getAsgndWsId();
						if (laAssgndWsIdsData != null )
						{
							if (SystemProperty.getWorkStationId() != laAssgndWsIdsData.getRedirPrtWsId()) 
							{
								leEx.addException(
										new RTSException(45),
										gettxtInquiryKey());
							}
						}
					}
					if (!leEx.isValidationError())
					{
						// end defect 11052 
						if (getchkSame().isSelected())
						{
							if (csTransCd.equals(TransCdConstant.VEHINQ) 
									|| csTransCd.equals(TransCdConstant.SPINQ))
							{
								// defect 11052 
								caInquiryData.setPrintOptions(getPrintOptions());
								// end defect 11052 
							}
							getController().processData(
									VCInquiryKeySelectionKEY001.SINGLE_REC,
									caInquiryData);
						}
						else
						{
							if (getchkNoVehicle().isSelected())
							{
								getController().processData(
										VCInquiryKeySelectionKEY001.SINGLE_REC,
										null);
							}
							else
							{
								// defect 10820
								if (validateData())
								{
									GeneralSearchData laSearchCriteria;
									laSearchCriteria = processEnter();
									getController().processData(
											AbstractViewController.ENTER,
											laSearchCriteria);
								}
								else
								{
									leEx.addException(
											new RTSException(
													ErrorsConstant
													.ERR_NUM_FIELD_ENTRY_INVALID),
													gettxtInquiryKey());
								}
								// end defect 10820
							}
						}
						// defect 11052 
					}
					// end defect 11052 
			}
			}
			else if (
					aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				if (!isVisible()
						&& !csTransCd.equals(TransCdConstant.CCO))
				{
					return;
				}
				else
				{
					getController().processData(
							AbstractViewController.CANCEL,
							null);
				}
			}
			else if (
					aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{

				String lsTransCd = getController().getTransCode();
				if (UtilityMethods.isMFUP())
				{
					// defect 10820
					if (lsTransCd.equals(TransCdConstant.VEHINQ)
							|| lsTransCd.equals(TransCdConstant.SPINQ))
						// end defect 10820
					{
						RTSHelp.displayHelp(RTSHelp.KEY001A);
					}
					else if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001B);
					}
					else if (lsTransCd.equals(TransCdConstant.DUPL))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001C);
					}
					else if (lsTransCd.equals(TransCdConstant.EXCH))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001D);
					}
					else if (lsTransCd.equals(TransCdConstant.REPL))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001E);
					}
					else if (lsTransCd.equals(TransCdConstant.PAWT))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001F);
					}
					if (lsTransCd.equals(TransCdConstant.CORREG)
							&& ((RegistrationModifyData) getController()
									.getData())
									.getRegModifyType()
									== RegistrationConstant
									.REG_MODIFY_APPREHENDED)
					{
						RTSHelp.displayHelp(RTSHelp.KEY001G);
					}
					else if (lsTransCd.equals(TransCdConstant.CORREG))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001H);
					}
					else if (lsTransCd.equals(TransCdConstant.ADDR))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001I);
					}
					else if (lsTransCd.equals(TransCdConstant.STAT))
					{
						if (SystemProperty.isRegion())
						{
							RTSHelp.displayHelp(RTSHelp.KEY001L);
						}
						else
						{
							RTSHelp.displayHelp(RTSHelp.KEY001J);
						}
					}
					else if (lsTransCd.equals(TransCdConstant.CCO))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001K);
					}
					else if (lsTransCd.equals(TransCdConstant.TAWPT))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001M);
					}
					else if (lsTransCd.equals(TransCdConstant.REFUND))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001N);
					}
					else if (lsTransCd.equals(TransCdConstant.HOTCK))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001O);
					}
					else if (lsTransCd.equals(TransCdConstant.CKREDM))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001P);
					}
					else if (lsTransCd.equals(TransCdConstant.HOTDED))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001Q);
					}
					else if (lsTransCd.equals(TransCdConstant.HCKITM))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001R);
					}
				} //if MF is unavailable
				else
				{
					if (lsTransCd.equals(TransCdConstant.RENEW))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001S);
					}
					else if (lsTransCd.equals(TransCdConstant.REPL))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001T);
					}
					else if (lsTransCd.equals(TransCdConstant.EXCH))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001U);
					}
					else if (lsTransCd.equals(TransCdConstant.PAWT))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001V);
					}
					else if (
							lsTransCd.equals(TransCdConstant.CORREG)
							&& ((RegistrationModifyData) getController()
									.getData())
									.getRegModifyType()
									== RegistrationConstant
									.REG_MODIFY_APPREHENDED)
					{
						RTSHelp.displayHelp(RTSHelp.KEY001W);
					}
					else if (lsTransCd.equals(TransCdConstant.CORREG))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001X);
					}
					else if (lsTransCd.equals(TransCdConstant.TAWPT))
					{
						RTSHelp.displayHelp(RTSHelp.KEY001Y);
					}
				}

			}
			if (leEx.isValidationError())
			{
				leEx.displayError(this);
				leEx.getFirstComponent().requestFocus();
				return;
			}
		} 
		finally
		{
			doneWorking();
			// defect 7885
			// Move focus to the default focus field anytime you go 
			// through action performed for Help, Enter, Cancel
			if (getDefaultFocusField() != null
					&& getDefaultFocusField().isEnabled()
					&& getDefaultFocusField().isEnabled())
			{
				getDefaultFocusField().requestFocus();
			} // end defect 7885
		}
	}
	
	/**
	 * Handle bar code event
	 * 
	 * @param aaBCE BarCodeEvent
	 */
	public void barCodeScanned(BarCodeEvent aaBCE)
	{
		if (aaBCE.getBarCodeData() instanceof RenewalBarCodeData)
		{
			RenewalBarCodeData laBarCodeData =
				(RenewalBarCodeData) aaBCE.getBarCodeData();
			getradioPlate().setSelected(true);
			gettxtInquiryKey().setText(laBarCodeData.getRegPltNo());
		}
	}

	/**
	 * Enable charge fee check box
	 */
	private void enableChkCharge()
	{
		if (!SystemProperty.isHQ())
		{
			ivjchkCharge.setVisible(true);
			ivjchkCharge.setSelected(true);
			// defect 11289 
			ivjchkCharge.setEnabled(true);
			// end defect 11289 
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
	 * Return the chkCharge property value.
	 * 
	 * @return JCheckBox
	 */ 
	private JCheckBox getchkCharge()
	{
		if (ivjchkCharge == null)
		{
			try
			{
				ivjchkCharge = new JCheckBox();
				ivjchkCharge.setName("chkCharge");
				ivjchkCharge.setMnemonic(KeyEvent.VK_F);
				ivjchkCharge.setText(TXT_CHRG_FEE);
				ivjchkCharge.setVisible(false);
				ivjchkCharge.setActionCommand(TXT_CHRG_FEE);
				ivjchkCharge.setFont(new Font("Arial", 1, 12));
				// defect 11289 
				ivjchkCharge.setEnabled(false);
				// end defect 11289

				// user code begin {1}
				ivjchkCharge.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkCharge;
	}
	/**
	 * Return the chkCharge property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkNoVehicle()
	{
		if (ivjchkNoVehicle == null)
		{
			try
			{
				ivjchkNoVehicle = new JCheckBox();
				ivjchkNoVehicle.setName("chkNoVehicle");
				ivjchkNoVehicle.setMnemonic(KeyEvent.VK_N);
				ivjchkNoVehicle.setText("No Vehicle");
				ivjchkNoVehicle.setVisible(false);
				ivjchkNoVehicle.setFont(new Font("Arial", 1, 12));
				// user code begin {1}
				ivjchkNoVehicle.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkNoVehicle;
	}
	/**
	 * Return the chkSameVehicle property value.
	 * 
	 * @return JCheckBox
	 */ 
	private JCheckBox getchkSame()
	{
		if (ivjchkSame == null)
		{
			try
			{
				ivjchkSame = new JCheckBox();
				ivjchkSame.setName("chkSame");
				ivjchkSame.setFont(new Font("Arial", 1, 12));
				ivjchkSame.setMnemonic(KeyEvent.VK_M);
				ivjchkSame.setText(TXT_SAME_VEH);
				ivjchkSame.setEnabled(false);
				ivjchkSame.addItemListener(this);
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
		return ivjchkSame;
	}
	/**
	 * Return the JDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */ 
	private JPanel getJDialogBoxContentPane()
	{
		if (ivjJDialogBoxContentPane == null)
		{
			try
			{
				ivjJDialogBoxContentPane = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints14 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints13 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints15 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints16 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints18 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints17 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints16.insets =
					new java.awt.Insets(4, 94, 8, 64);
				consGridBagConstraints16.ipady = 13;
				consGridBagConstraints16.ipadx = 132;
				consGridBagConstraints16.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints16.weighty = 1.0;
				consGridBagConstraints16.weightx = 1.0;
				consGridBagConstraints16.gridwidth = 2;
				consGridBagConstraints16.gridy = 4;
				consGridBagConstraints16.gridx = 0;
				consGridBagConstraints14.insets =
					new java.awt.Insets(3, 8, 4, 28);
				consGridBagConstraints14.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints14.weighty = 1.0;
				consGridBagConstraints14.weightx = 1.0;
				consGridBagConstraints14.gridy = 2;
				consGridBagConstraints14.gridx = 1;
				consGridBagConstraints15.insets =
					new java.awt.Insets(3, 20, 3, 26);
				consGridBagConstraints15.ipady = -11;
				consGridBagConstraints15.ipadx = 335;
				consGridBagConstraints15.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints15.weighty = 1.0;
				consGridBagConstraints15.weightx = 1.0;
				consGridBagConstraints15.gridwidth = 2;
				consGridBagConstraints15.gridy = 3;
				consGridBagConstraints15.gridx = 0;
				consGridBagConstraints18.insets =
					new java.awt.Insets(42, 8, 3, 191);
				consGridBagConstraints18.ipadx = 30;
				consGridBagConstraints18.gridy = 0;
				consGridBagConstraints18.gridx = 1;
				consGridBagConstraints13.insets =
					new java.awt.Insets(4, 8, 3, 23);
				consGridBagConstraints13.ipady = 4;
				consGridBagConstraints13.ipadx = 259;
				consGridBagConstraints13.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				consGridBagConstraints13.weightx = 1.0;
				consGridBagConstraints13.gridy = 1;
				consGridBagConstraints13.gridx = 1;
				consGridBagConstraints17.insets =
					new java.awt.Insets(40, 21, 2, 8);
				consGridBagConstraints17.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints17.weighty = 1.0;
				consGridBagConstraints17.weightx = 1.0;
				consGridBagConstraints17.gridheight = 3;
				consGridBagConstraints17.gridy = 0;
				consGridBagConstraints17.gridx = 0;
				ivjJDialogBoxContentPane.setName(
					"JDialogBoxContentPane");
				ivjJDialogBoxContentPane.setLayout(
					new java.awt.GridBagLayout());
				ivjJDialogBoxContentPane.add(
					gettxtInquiryKey(),
					consGridBagConstraints13);
				ivjJDialogBoxContentPane.add(
					getPanelInquiryKey(),
					consGridBagConstraints14);
				ivjJDialogBoxContentPane.add(
					getOptionsPanel(),
					consGridBagConstraints15);
				ivjJDialogBoxContentPane.add(
					getButtonPanel1(),
					consGridBagConstraints16);
				ivjJDialogBoxContentPane.add(
					getRadioPanel(),
					consGridBagConstraints17);
				ivjJDialogBoxContentPane.add(
					getstcLblInquiry(),
					consGridBagConstraints18);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJDialogBoxContentPane;
	}
	/**
	 * Return the OptionsPanel property value.
	 * 
	 * @return JPanel
	 */ 
	private JPanel getOptionsPanel()
	{
		if (ivjOptionsPanel == null)
		{
			try
			{
				ivjOptionsPanel = new JPanel();
				ivjOptionsPanel.setName("OptionsPanel");
				ivjOptionsPanel.setLayout(new GridBagLayout());
				GridBagConstraints constraintschkCharge =
					new GridBagConstraints();
				constraintschkCharge.gridx = 1;
				constraintschkCharge.gridy = 1;
				constraintschkCharge.weightx = 1.0;
				constraintschkCharge.ipadx = 38;
				constraintschkCharge.ipady = 12;
				constraintschkCharge.insets = new Insets(6, 171, 8, 5);
				getOptionsPanel().add(
					getchkCharge(),
					constraintschkCharge);
				// defect 9085 
				GridBagConstraints constraintschkNoVehicle =
					new GridBagConstraints();
				constraintschkNoVehicle.gridx = 1;
				constraintschkNoVehicle.gridy = 1;
				constraintschkNoVehicle.weightx = 1.0;
				constraintschkNoVehicle.ipadx = 38;
				constraintschkNoVehicle.ipady = 12;
				constraintschkNoVehicle.insets =
					new Insets(6, 171, 8, 5);
				getOptionsPanel().add(
					getchkNoVehicle(),
					constraintschkNoVehicle);
				// end defect 9085
				GridBagConstraints constraintschkSame =
					new GridBagConstraints();
				constraintschkSame.gridx = 2;
				constraintschkSame.gridy = 1;
				constraintschkSame.anchor = GridBagConstraints.EAST;
				constraintschkSame.weightx = 1.0;
				constraintschkSame.ipady = 12;
				constraintschkSame.insets = new Insets(6, 5, 8, 17);
				getOptionsPanel().add(getchkSame(), constraintschkSame);
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
		return ivjOptionsPanel;
	}
	/**
	 * Return the PanelInquiryKey property value.
	 * 
	 * @return JPanel
	 */ 
	private JPanel getPanelInquiryKey()
	{
		if (ivjPanelInquiryKey == null)
		{
			try
			{
				ivjPanelInquiryKey = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints11 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints12 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints10 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints11.insets =
					new java.awt.Insets(7, 49, 7, 78);
				consGridBagConstraints11.ipady = 8;
				consGridBagConstraints11.ipadx = 24;
				consGridBagConstraints11.gridy = 1;
				consGridBagConstraints11.gridx = 0;
				consGridBagConstraints12.insets =
					new java.awt.Insets(7, 49, 10, 93);
				consGridBagConstraints12.ipady = 2;
				consGridBagConstraints12.ipadx = 36;
				consGridBagConstraints12.gridy = 2;
				consGridBagConstraints12.gridx = 0;
				consGridBagConstraints10.insets =
					new java.awt.Insets(17, 49, 7, 97);
				consGridBagConstraints10.ipady = 6;
				consGridBagConstraints10.ipadx = 31;
				consGridBagConstraints10.gridy = 0;
				consGridBagConstraints10.gridx = 0;
				ivjPanelInquiryKey.setName("PanelInquiryKey");
				ivjPanelInquiryKey.setLayout(
					new java.awt.GridBagLayout());
				ivjPanelInquiryKey.add(
					getradioView(),
					consGridBagConstraints10);
				ivjPanelInquiryKey.add(
					getradioViewPrint(),
					consGridBagConstraints11);
				ivjPanelInquiryKey.add(
					getradioCertified(),
					consGridBagConstraints12);
				Border PrintBorder =
					new TitledBorder(
						new EtchedBorder(),
						TXT_SEL_INQ_TYPE);
				ivjPanelInquiryKey.setBorder(PrintBorder);
				caRadioGroupView = new RTSButtonGroup();
				caRadioGroupView.add(ivjradioCertified, gettxtInquiryKey());
				caRadioGroupView.add(ivjradioView, gettxtInquiryKey());
				caRadioGroupView.add(
					ivjradioViewPrint,
					gettxtInquiryKey());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjPanelInquiryKey;
	}
	/**
	 * Return the radioDocument property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioDocument()
	{
		if (ivjradioDocument == null)
		{
			try
			{
				ivjradioDocument = new JRadioButton();
				ivjradioDocument.setName("radioDocument");
				ivjradioDocument.setFont(new Font("Arial", 1, 12));
				ivjradioDocument.setMnemonic('d');
				ivjradioDocument.setText(TXT_DOC_NO);
				// user code begin {1}
				ivjradioDocument.setActionCommand(
					CommonConstant.DOC_NO);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioDocument;
	}
	/**
	 * Return the RadioPanel property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRadioPanel()
	{
		if (ivjRadioPanel == null)
		{
			try
			{
				ivjRadioPanel = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints2 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints3 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints1 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints2.insets =
					new java.awt.Insets(17, 54, 17, 21);
				consGridBagConstraints2.ipadx = 64;
				consGridBagConstraints2.gridy = 1;
				consGridBagConstraints2.gridx = 0;
				consGridBagConstraints1.insets =
					new java.awt.Insets(30, 54, 17, 21);
				consGridBagConstraints1.ipadx = 36;
				consGridBagConstraints1.gridy = 0;
				consGridBagConstraints1.gridx = 0;
				consGridBagConstraints3.insets =
					new java.awt.Insets(17, 54, 30, 21);
				consGridBagConstraints3.ipadx = 7;
				consGridBagConstraints3.gridy = 2;
				consGridBagConstraints3.gridx = 0;
				ivjRadioPanel.setName("RadioPanel");
				ivjRadioPanel.setLayout(new java.awt.GridBagLayout());
				ivjRadioPanel.add(
					getradioPlate(),
					consGridBagConstraints1);
				ivjRadioPanel.add(
					getradioVIN(),
					consGridBagConstraints2);
				ivjRadioPanel.add(
					getradioDocument(),
					consGridBagConstraints3);
				Border laBrdr =
					new TitledBorder(
						new EtchedBorder(),
						TXT_SEL_APRPT_KEY);
				ivjRadioPanel.setBorder(laBrdr);
				// defect 7885
				caRadioGroup = new RTSButtonGroup();
				// end defect 7885
				caRadioGroup.add(ivjradioDocument, gettxtInquiryKey());
				caRadioGroup.add(ivjradioPlate, gettxtInquiryKey());
				caRadioGroup.add(ivjradioVIN, gettxtInquiryKey());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjRadioPanel;
	}
	/**
	 * Return the radioPlate property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPlate()
	{
		if (ivjradioPlate == null)
		{
			try
			{
				ivjradioPlate = new JRadioButton();
				ivjradioPlate.setName("radioPlate");
				ivjradioPlate.setMnemonic(KeyEvent.VK_P);
				ivjradioPlate.setText(TXT_PLT_NO);
				ivjradioPlate.setSelected(true);
				ivjradioPlate.setFont(new Font("Arial", 1, 12));
				// user code begin {1}
				ivjradioPlate.setActionCommand(
					CommonConstant.REG_PLATE_NO);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioPlate;
	}
	/**
	 * Return the ivjradioCertified property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioCertified()
	{
		if (ivjradioCertified == null)
		{
			try
			{
				ivjradioCertified = new JRadioButton();
				ivjradioCertified.setName("radioPrint");
				ivjradioCertified.setMnemonic(KeyEvent.VK_C);
				ivjradioCertified.setText(TXT_CERTIFIED);
				ivjradioCertified.setVisible(true);
				ivjradioCertified.setFont(new Font("Arial", 1, 12));
				// user code begin {1}
				ivjradioCertified.setVisible(false);
				// defect 11285
				//ivjradioCertified.addActionListener(this);
				ivjradioCertified.setEnabled(false);
				// end defect 11285
				// defect 11289 
				ivjradioCertified.addItemListener(this);
				// end defect 11289

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCertified;
	}
	/**
	 * Return the radioView property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioView()
	{
		if (ivjradioView == null)
		{
			try
			{
				ivjradioView = new JRadioButton();
				ivjradioView.setName("radioView");
				ivjradioView.setMnemonic(KeyEvent.VK_I);
				ivjradioView.setText(TXT_VW_ONLY);
				ivjradioView.setVisible(true);
				ivjradioView.setFont(new Font("Arial", 1, 12));
				// user code begin {1}
				ivjradioView.addItemListener(this);
				ivjradioView.setVisible(false);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioView;
	}
	/**
	 * Return the radioViewPrint property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioViewPrint()
	{
		if (ivjradioViewPrint == null)
		{
			try
			{
				ivjradioViewPrint = new JRadioButton();
				ivjradioViewPrint.setName("radioViewPrint");
				ivjradioViewPrint.setMnemonic(KeyEvent.VK_A);
				ivjradioViewPrint.setText(TXT_VW_PRNT_ONLY);
				ivjradioViewPrint.setVisible(true);
				ivjradioViewPrint.setFont(new Font("Arial", 1, 12));
				// user code begin {1}
				ivjradioViewPrint.addItemListener(this);
				ivjradioViewPrint.setVisible(true);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioViewPrint;
	}
	/**
	 * Return the radioVIN property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioVIN()
	{
		if (ivjradioVIN == null)
		{
			try
			{
				ivjradioVIN = new JRadioButton();
				ivjradioVIN.setName("radioVIN");
				ivjradioVIN.setFont(new java.awt.Font("Arial", 1, 12));
				ivjradioVIN.setMnemonic(KeyEvent.VK_V);
				ivjradioVIN.setText(TXT_VIN);
				// user code begin {1}
				ivjradioVIN.setActionCommand(CommonConstant.VIN);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioVIN;
	}
	/**
	 * Return the stcLblInquiry property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblInquiry()
	{
		if (ivjstcLblInquiry == null)
		{
			try
			{
				ivjstcLblInquiry = new JLabel();
				ivjstcLblInquiry.setName("stcLblInquiry");
				ivjstcLblInquiry.setText(TXT_INQ_KEY);
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
		return ivjstcLblInquiry;
	}
	/**
	 * Return the ivjtxtInquiryKey property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtInquiryKey()
	{
		if (ivjtxtInquiryKey == null)
		{
			try
			{
				ivjtxtInquiryKey = new RTSInputField();
				ivjtxtInquiryKey.setName("txtInquiryKey");
				ivjtxtInquiryKey.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtInquiryKey.setMaxLength(22);
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
			// user code begin {1}
			// user code end
			setName(FRM_NAME_KEY001);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(514, 371);
			setTitle(FRM_TITLE_KEY001);
			setContentPane(getJDialogBoxContentPane());
			// defect 7885 
			setRequestFocus(false);
			// end defect 7885  
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		} // user code begin {2}
		// user code end
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
		if (aaIE.getSource() == getradioView())
		{
			if (getradioView().isSelected())
			{
				getchkCharge().setVisible(false);
				// defect 11289 
				getchkCharge().setEnabled(false); 
				getchkCharge().setSelected(false);
				// end defect 11289 
			}

		}
		// defect 11289
		//		else if (aaIE.getSource() == getradioViewPrint())
		//		{
		//			if (getradioViewPrint().isSelected()
		//				&& getPanelInquiryKey().isEnabled())
		//			{
		//				enableChkCharge();
		//			}
		// 
		else if (aaIE.getSource() == getradioViewPrint() 
				|| aaIE.getSource() == getradioCertified())
		{
			if (((JRadioButton) aaIE.getSource()).isSelected()
				&& getPanelInquiryKey().isEnabled())
			{
				enableChkCharge();
			}
		} 
		// end defect 11289 
		
		// defect 8485
		// Disable/Enable Inquiry Field/Radio Buttons/Label 
		else if (aaIE.getSource() == getchkSame())
		{
			if (getchkSame().isSelected())
			{
				clearAllColor(this);
				getstcLblInquiry().setEnabled(false);
				gettxtInquiryKey().setText("");
				gettxtInquiryKey().setEnabled(false);
				getradioPlate().setEnabled(false);
				getradioVIN().setEnabled(false);
				getradioDocument().setEnabled(false);
				if (UtilityMethods.isSPAPPL(csTransCd))
				{
					getchkNoVehicle().setEnabled(false);
					getchkNoVehicle().setSelected(false);
				}  
			}
			else
			{
				getstcLblInquiry().setEnabled(true);
				gettxtInquiryKey().setEnabled(true);
				getradioPlate().setEnabled(true);
				
				// defect 10820
				// only enable VIN and Doc No radio buttons if NOT
				// special plates only search
				if (!csTransCd.equals(TransCdConstant.SPINQ))
				{
					getradioVIN().setEnabled(true);
					getradioDocument().setEnabled(true);
				}
				// end defect 10820
				// defect 9085
				if (UtilityMethods.isSPAPPL(csTransCd))
				{
					getchkNoVehicle().setEnabled(true);
				} // end defect 9085
				gettxtInquiryKey().requestFocus();
			}
		} 
		else if (aaIE.getSource() == getchkNoVehicle())
		{
			if (getchkNoVehicle().isSelected())
			{
				clearAllColor(this);
				getstcLblInquiry().setEnabled(false);
				gettxtInquiryKey().setText("");
				gettxtInquiryKey().setEnabled(false);
				getradioPlate().setEnabled(false);
				getradioVIN().setEnabled(false);
				getradioDocument().setEnabled(false);
				getchkSame().setEnabled(false);
				getchkSame().setSelected(false);
			}
			else
			{
				getstcLblInquiry().setEnabled(true);
				gettxtInquiryKey().setEnabled(true);
				getradioPlate().setEnabled(true);
				getradioVIN().setEnabled(true);
				getradioDocument().setEnabled(true);
				if (cbSameVehicle)
				{
					getchkSame().setEnabled(true);
				} 
				gettxtInquiryKey().requestFocus();
			}
		}  
		else if (aaIE.getSource() == getchkCharge())
		{
			if (gettxtInquiryKey().isEnabled())
			{
				gettxtInquiryKey().requestFocus();
			}
		} 
	}
	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmInquiryKeySelectionKEY001 laFrmInquiryKeySelectionKEY001;
			laFrmInquiryKeySelectionKEY001 =
				new FrmInquiryKeySelectionKEY001();
			laFrmInquiryKeySelectionKEY001.setModal(true);
			laFrmInquiryKeySelectionKEY001
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInquiryKeySelectionKEY001.show();
			Insets laInsets =
				laFrmInquiryKeySelectionKEY001.getInsets();
			laFrmInquiryKeySelectionKEY001.setSize(
				laFrmInquiryKeySelectionKEY001.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmInquiryKeySelectionKEY001.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmInquiryKeySelectionKEY001.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * Process when enter is pressed
	 * 
	 * @return GeneralSearchData
	 */
	private GeneralSearchData processEnter()
	{

		String lsTextKey;
		String lsRadioKey;
		//Initialize Variables
		caSearchCriteria.setIntKey1(0);
		caSearchCriteria.setIntKey2(0);
		caSearchCriteria.setKey1(null);
		caSearchCriteria.setKey2(null);
		if (ivjchkSame.getSelectedObjects() != null)
		{
			caSearchCriteria.setKey2(ivjchkSame.getActionCommand());
		}
		else
		{
			if (ivjtxtInquiryKey
				.getText()
				.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				lsTextKey = null;
				caSearchCriteria.setKey2(lsTextKey);
			}
			else
			{
				lsRadioKey =
					caRadioGroup.getSelection().getActionCommand();
				lsTextKey = ivjtxtInquiryKey.getText().trim();
				lsTextKey = lsTextKey.toUpperCase();
				caSearchCriteria.setKey1(lsRadioKey);
				caSearchCriteria.setKey2(lsTextKey);
			
				if (getradioVIN().isSelected())
				{
					caSearchCriteria.setKey2(
						CommonValidations.convert_i_and_o_to_1_and_0(
							caSearchCriteria.getKey2()));
				}
			}
		}

		// defect 10820
		// if (csTransCd.equals(TransCdConstant.VEHINQ))
		if (csTransCd.equals(TransCdConstant.VEHINQ)
			|| csTransCd.equals(TransCdConstant.SPINQ))
		// end defect 10820
		{
			// defect 11052 
			caSearchCriteria.setIntKey1(getPrintOptions());
			// end defect 11052			
		}

		// defect 10820
		// if special plate inquiry, set ciIntKey2 = 2 to search special 
		// plates only
		if (csTransCd.equals(TransCdConstant.SPINQ))
		{
			caSearchCriteria.setIntKey2(
				CommonConstant.SEARCH_SPECIAL_PLATES);
		}
		// end defect 10820
		return caSearchCriteria;
	}
	
	/**
	 * Return the Print Option Number
	 * 
	 * @return int 
	 */
	private int getPrintOptions()
	{
		boolean lbChrgFee = getchkCharge().isSelected();
		int liPrint = 0; 
		if (ivjradioView.isSelected())
		{
			liPrint = VehicleInquiryData.VIEW_ONLY;
		}
		else if (ivjradioViewPrint.isSelected())
		{
			if (SystemProperty.isCounty())
			{
				liPrint = lbChrgFee ? VehicleInquiryData.VIEW_AND_PRINT_AND_CHARGE :
					VehicleInquiryData.VIEW_AND_PRINT; 
			}
			else
			{
				liPrint = lbChrgFee ? VehicleInquiryData.VIEW_AND_NONCERTFD_AND_CHARGE :
					VehicleInquiryData.VIEW_AND_NONCERTFD; 
			}
		}
		else if (ivjradioCertified.isSelected())
		{
			liPrint = lbChrgFee ? VehicleInquiryData.VIEW_AND_CERTFD_AND_CHARGE :
				VehicleInquiryData.VIEW_AND_CERTFD; 
		}
		return liPrint; 
	}
	/**
	* Set up the data for this screen
	* 
	* @param aaDataObject Object
	*/
	public void setData(Object aaDataObject)
	{
		csTransCd = getController().getTransCode();

		// defect 10820
		// special plates inquiry - temporary trans code 
		if (csTransCd.equals(TransCdConstant.SPINQ))
		{
			// No VIN or Doc No inquiry for special plates
			getradioVIN().setEnabled(false);
			getradioDocument().setEnabled(false);
			ivjPanelInquiryKey.setVisible(true);
			ivjPanelInquiryKey.setEnabled(true);
			ivjradioView.setVisible(true);
			ivjradioViewPrint.setVisible(true);
			// defect 11052
			ivjradioView.setSelected(!SystemProperty.isRegion());
			getradioViewPrint().setSelected(SystemProperty.isRegion());
			if (!SystemProperty.isCounty())
			{
				getradioViewPrint().setText(TXT_NON_CERTIFIED);
				getradioViewPrint().setMnemonic(KeyEvent.VK_N);
				getradioCertified().setVisible(true);
				// defect 11285
				getradioCertified().setEnabled(true); 
				// end defect 11285

			}
			// end defect 11052
			
			try
			{
				// determine if 'Same Special Plate' should be enabled
				CommonClientBusiness laCommonClientBusiness =
					new CommonClientBusiness();
				Object laSpclPlt =
					laCommonClientBusiness.processData(
						GeneralConstant.COMMON,
						CommonConstant.GET_SAVED_SPCL_PLT,
						null);
						
				// Change text for "Same Vehicle" checkbox to
				// "Same Special Plate" for special plate inquiry
				ivjchkSame.setText(SAME_SPECIAL_PLATE);
				if (laSpclPlt != null
					&& laSpclPlt instanceof SpecialPlatesRegisData)
				{
					MFVehicleData laMFVehData = new MFVehicleData(true);
					laMFVehData.setSpclPltRegisData(
						(SpecialPlatesRegisData) laSpclPlt);
					laMFVehData.setSPRecordOnlyVehInq(true);
					caInquiryData.setMfVehicleData(laMFVehData);
					
					ivjchkSame.setEnabled(true);
					cbSameVehicle = true;
				}
				else
				{
					ivjchkSame.setEnabled(false);
					cbSameVehicle = false;
				}
			}
			catch (RTSException aeRTSException)
			{
				aeRTSException.displayError(this);
			}
		}
		else
		{
			// end defect 10820
			//Initializing Inquiry Screen Items
			if (csTransCd.equals(TransCdConstant.VEHINQ))
			{
				SecurityData laScrtyData =
					getController()
						.getMediator()
						.getDesktop()
						.getSecurityData();
				if (laScrtyData.getPltNoAccs() == 0)
				{
					getradioPlate().setEnabled(false);
					getradioVIN().setSelected(true);
				}
				ivjPanelInquiryKey.setVisible(true);
				ivjPanelInquiryKey.setEnabled(true);
				ivjradioView.setVisible(true);
				ivjradioViewPrint.setVisible(true);
				ivjradioView.setSelected(true);
				// defect 11052 
				if (!SystemProperty.isCounty())
				{
					// end defect 11052 
					getradioViewPrint().setText(TXT_NON_CERTIFIED);
					getradioViewPrint().setMnemonic(KeyEvent.VK_N);
					getradioViewPrint().setSelected(true);
					getradioCertified().setVisible(true);
					// defect 11285
					getradioCertified().setEnabled(true); 
					// end defect 11285

				}
				// end defect 11052 
			}
			else if (csTransCd.equals(TransCdConstant.SPECDL))
			{
				ivjradioDocument.setEnabled(false);
				ivjradioVIN.setEnabled(false);
			}
			else
			{
				ivjPanelInquiryKey.setVisible(false);
				ivjPanelInquiryKey.setEnabled(false);
				getradioView().setVisible(false);
				getradioView().setEnabled(false);
				getradioViewPrint().setVisible(false);
				getradioViewPrint().setEnabled(false);
				// defect 11052 
				getradioCertified().setVisible(false);
				getradioCertified().setEnabled(false);
				// end defect 11052 
			}

			// defect 9200 
			boolean lbSPAPPL = UtilityMethods.isSPAPPL(csTransCd);
			getchkNoVehicle().setEnabled(lbSPAPPL);
			getchkNoVehicle().setVisible(lbSPAPPL);
			// end defect 9200 

			if (Transaction.getCumulativeTransIndi()
				!= CommonConstant.BOTH_ADDL_AND_CUMUL
				|| SystemProperty.isHQ())
			{
				ivjchkSame.setEnabled(false);
			}
			else
			{ 
				//get the saved vehicle
				try
				{
					CommonClientBusiness laCommonClientBusiness =
						new CommonClientBusiness();
					Object laTmpVeh =
						laCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.GET_SAVED_VEH,
							null);
					if (laTmpVeh != null
						&& laTmpVeh instanceof MFVehicleData)
					{
						caVehData = (MFVehicleData) laTmpVeh;
						if (caVehData.isFromMF())
						{
							caInquiryData.setMfDown(0);
						}
						else
						{
							caInquiryData.setMfDown(1);
						}

					}
					Object laTmpMiscData =
						laCommonClientBusiness.processData(
							GeneralConstant.COMMON,
							CommonConstant.GET_VEH_MISC,
							null);
					if (laTmpMiscData != null
						&& laTmpMiscData instanceof VehMiscData)
					{
						caInquiryData.setVehMiscData(
							(VehMiscData) laTmpMiscData);
					}
					caInquiryData.setMfVehicleData(caVehData);
					caInquiryData.setNoMFRecs(1);
					// defect 3897
					if (aaDataObject != null
						&& aaDataObject instanceof RegistrationModifyData)
					{
						caInquiryData.setValidationObject(aaDataObject);
					}
					ivjchkSame.setEnabled(true);
					// defect 9085 
					cbSameVehicle = true;
					// end defect 9085  
				}
				catch (RTSException aeRTSException)
				{
					// display get retrieving saved vehicle info
					aeRTSException.displayError(this);
				}
			}

			try
			{
				BarCodeScanner laBCS =
					getController()
						.getMediator()
						.getAppController()
						.getBarCodeScanner();
				laBCS.addBarCodeListener(this);
			}
			catch (RTSException aeRTSEx)
			{
				// defect 11071
				Log.write(Log.DEBUG, this, aeRTSEx.getDetailMsg());
				// end defect 11071
			} // defect 8494
		// defect 10820 - just the closing bracket below
		}
		// end defect 10820
		// Moved from windowOpened 
		setDefaultFocusField(gettxtInquiryKey());
		// end defect 8494
		// end defect 9085 
	}

	/**
	 * Validate input data  
	 *
	 * @return boolean
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;

		if ((getradioPlate().isSelected()
			&& gettxtInquiryKey().getText().trim().length()
				> CommonConstant.LENGTH_PLTNO))
		{
			// plate search selected and plate is too long
			lbReturn = false;
		}
		else if (
			getradioDocument().isSelected()
				&& gettxtInquiryKey().getText().trim().length()
					!= CommonConstant.LENGTH_DOCNO)
		{
			// doc number search selected and doc number invalid length
			lbReturn = false;
		}

		return lbReturn;
	}
}
	

