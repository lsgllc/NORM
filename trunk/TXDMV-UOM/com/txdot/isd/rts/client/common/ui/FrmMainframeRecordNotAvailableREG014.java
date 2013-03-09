package com.txdot.isd.rts.client.common.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.title.ui.TitleClientUtilityMethods;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmMainframeRecordNotAvailableREG014.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()  
 * T Pederson   04/30/2002  Don't allow spaces to be entered in input 
 * 							fields
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							No longer captures sticker info. 
 *							modify captureData(), setData()	
 * 							Ver 5.2.0	
 * T Pederson	03/09/2005	Java 1.4 Work
 * 							defect 7885 Ver 5.2.3 
 * T Pederson	03/30/2005	Removed setNextFocusableComponent, added 
 * 							panel to frame to manage correct tabbing.
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * K Harrell	05/17/2005	rename RegistrationClassData.getDiesleReqd()
 * 							to getDieselReqd()
 * 							defect 7786  Ver 5.2.3
 * B Hargrove	05/20/2005	Cannot rely on TransCd for CORREG vs CORREGX.
 * 							Use RegistrationModifyData.getRegModifyType()
 *							import com.txdot.isd.rts.client.
 *								registration.ui.RegistrationModifyData;
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * S Johnston	06/17/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							defect 8240 Ver 5.2.3
 * K Harrell	06/20/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3 
 * B Hargrove	06/27/2005	Refactor\Move 
 * 							RegistrationModifyData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * T Pederson	07/28/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	08/26/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * K Harrell	09/14/2005	Enable Solid Tire Radio Button usind data
 * 							in cache vs. hard coding
 * 							modify setData() 
 * 							defect 8310 Ver 5.2.3  
 * T Pederson	10/06/2005	Code cleanup
 * 							defect 7885 Ver 5.2.3
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify setData()
 * 							defect 8479 Ver 5.2.3 
 * Jeff S.		01/06/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							modify initialize()
 * 							defect 7885 Ver 5.2.3
 * K Harrell	02/17/2006	Convert I/O to 1/0 in VIN
 *							modify focusLost()
 *							modify gettxtVIN()  
 *							defect 8466 Ver 5.2.2 Fix 8   
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify captureData(), focusLost() 
 * 							defect 8902 Ver Exempts
 * K Harrell	11/17/2006	Add Exempt Checkbox and special handling for
 * 							Standard Exempts. Add'l Java/Screen Cleanup. 
 * 							add cbStandardExempt,TXT_CHK_IF_APPLICABLE 
 * 							add getchkExempt()
 * 							delete TXT_CHK_IF_DIESEL
 * 							modify captureData(),setData(),
 *							 validateData(),isInvalidExpYear(),
 *							 isInvalidYearModel(),getJPanel6()
 * 							defect 9023 Ver Exempts
 * K Harrell	04/06/2007	CommonValidations.convert_i_and_o_to_1_and_0() 
 * 							call is now static; Use RTSInputFields 
 * 							constants for input fields. 
 * 							defect 9085 Ver Special Plates
 * K Harrell	11/05/2007	Set and Disable Plate No and Plate Age if 
 * 							Special Plate
 * 							modify setData() 
 * 							defect 9389 Ver Special Plates 2
 * K Harrell	04/11/2008	Default tonnage to ZERO_DOLLAR if 
 * 							RegistrationClass.getTonReqd() == 0
 * 							modify captureData()
 * 							defect 9044 Ver POS Defect A       
 * J Rue		05/20/2008	Add PltRmvdCd, ignore edits on RegPltNo and 
 * 							RegPltAge if selected.
 * 							add getchkPltRmvd()
 * 							modify validateData(), getJPanel2(),
 * 								captureData()
 * 							defect 9630 Ver Defect_POS_A
 * J Rue		05/30/2008	RegistrationData change get/set RmvRegPltNo 
 * 							to get/set RmvdRegPltNo
 * 							modify captureData()
 * 							defetc 9630 Ver Defect_POS_A
 * J Rue		06/12/2008	Change Ver to Defect_POS_A
 * 							defect 9630 Ver Defect_POS_A
 * K Harrell	03/18/2009	Screen Cleanup/Member Sort. 
 * 							Moved Fees into JPanel4()
 * 							Changed mnemonic from P to U for Pneumatic
 * 							add caChkboxBtnGrp, caRadioBtnGrp1, 
 * 							  get/set methods 
 * 							add ivjradioPrinted, ivjradioElectronic,
 * 							 ivjradioNonTitled, get/set methods 
 * 							modify getJPanel5(), getJPanel6  
 * 							defect 9971 Ver Defect_POS_E 
 * K Harrell	06/03/2009  Remove Eff Date, Fees from screen.  
 * 							Additional Screencleanup.
 * 							add TXT_ELECTRONIC, TXT_PRINTED,
 * 							 TXT_NON_TITLED 
 *							delete ivjstcLblEffectiveDate, 
 *							 ivjstcLblFeesPaid, ivjtxtEffDate, 
 *							 ivjtxtFeesPd, get methods
 *							delete TEN_THOUSAND_DOLLARS, TXT_EFF_DT, 
 *							 TXT_FEES_PD, ZERO_DOLLARS
 * 							modify actionPerformed(), getJPanel3(), 
 * 							 getJPanel4(), setData(),
 * 							 setDataToDataObject(),  validateData() 
 *							defect 8414 Ver Defect_POS_F 
 * Min Wang		12/22/2009	Fix TireTypeCd lowercase "s" on MF Down.
 * 							modify TIRE_CD_SOLID
 * 							defect 10197 Ver Defect_POS_H 
 * Min Wang		12/28/2009	Fix TireTypeCd lowercase "p" on MF Down.
 * 							modify TIRE_CD_PNEUMATIC
 * 							defect 10197 Ver Defect_POS_H 
 * Min Wang		01/04/2010	Use common constants for tire type.
 * 							deleted TIRE_CD_PNEUMATIC, TIRE_CD_SOLID
 * 							modify setDataToDataObject()
 * 							defect 10317 Ver Defect_POS_H 
 * K Harrell	11/07/2011	delete MAX_GROSS_WT
 * 							modify validateData()
 * 							defect 10959 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**  
 * Frame for Mainframe Record Not Available
 * 
 * @version	6.9.0			11/07/2011
 * @author	Administrator
 * @since 					06/26/2001 
 */

public class FrmMainframeRecordNotAvailableREG014
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JCheckBox ivjchkDiesel = null;
	private JCheckBox ivjchkExempt = null;
	private JCheckBox ivjchkPltRmvd = null;
	private JComboBox ivjcomboBody = null;
	private JComboBox ivjcomboMake = null;
	private JPanel ivjFrmMainframeRecordNotAvailableREG014ContentPane1 =
		null;
	private JPanel ivjJPanel0 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel4 = null;
	private JPanel ivjJPanel5 = null;
	private JPanel ivjJPanel6 = null;
	private JPanel ivjJPanel7 = null;
	private JLabel ivjlblGrossWeight = null;
	private JLabel ivjlblGrossWt = null;
	private JRadioButton ivjradioElectronic = null;
	private JRadioButton ivjradioNonTitled = null;
	private JRadioButton ivjradioPneumatic = null;
	private JRadioButton ivjradioPrinted = null;
	private JRadioButton ivjradioSolid = null;
	private JLabel ivjstcLblBodyStyle = null;
	private JLabel ivjstcLblCarryingCapacity = null;
	private JLabel ivjstcLblDocumentNo = null;
	private JLabel ivjstcLblEmptyWeight = null;
	private JLabel ivjstcLblExpirationMoYear = null;
	private JLabel ivjstcLblGrossWeight = null;
	private JLabel ivjstcLblModel = null;
	private JLabel ivjstcLblPlate = null;
	private JLabel ivjstcLblPlateAge = null;
	private JLabel ivjstcLblTonnage = null;
	private JLabel ivjstcLblVin = null;
	private JLabel ivjstcLblYearMake = null;
	private RTSInputField ivjtxtCarryingCap = null;
	private RTSInputField ivjtxtDocNo = null;
	private RTSInputField ivjtxtEmptyWt = null;
	private RTSInputField ivjtxtExpMo = null;
	private RTSInputField ivjtxtExpYr = null;
	private RTSInputField ivjtxtMake = null;
	private RTSInputField ivjtxtModel = null;
	private RTSInputField ivjtxtPlateAge = null;
	private RTSInputField ivjtxtPlateNo = null;
	private RTSInputField ivjtxtTonnage = null;
	private RTSInputField ivjtxtVIN = null;
	private RTSInputField ivjtxtYear = null;
	private RTSButtonGroup caChkboxBtnGrp = new RTSButtonGroup();
	private RTSButtonGroup caRadioBtnGrp = new RTSButtonGroup();
	private RTSButtonGroup caRadioBtnGrp1 = new RTSButtonGroup();

	// defect 8414
	//private JLabel ivjstcLblEffectiveDate = null;
	//private JLabel ivjstcLblFeesPaid = null;
	//private RTSDateField ivjtxtEffDate = null;
	//private RTSInputField ivjtxtFeesPd = null;
	// end defect 8414 

	// Object 
	private RegistrationClassData caReqdData;
	private VehicleInquiryData caVehData;

	// boolean 
	private boolean cbStandardExempt = false;

	// String 
	private String csDocNo = null;

	// Vector
	private Vector cvVehBodyTypes = new Vector();
	private Vector cvVehMakes = new Vector();

	// Constants 
	private final static String DASH = " -";
	private final static int EIGHTEEN_EIGHTY = 1880;
	private final static int FIVE = 5;
	private final static String FRM_NAME_REG014 =
		"FrmMainframeRecordNotAvailableREG014";
	private final static String FRM_TITLE_REG014 =
		"Mainframe Record Not Available       REG014";
	private final static int MAX_DOC_LENGTH = 17;
	
	// defect 10959 
	//private final static int MAX_GROSS_WT = 80000;
	// end defect 10959
	
	private final static int MAX_PLT_AGE = 7;
	
	private final static String NEW = "-NEW- -  ";
	private final static String NINE_ZEROES = "000000000";
	// defect 10197
	//private final static String TIRE_CD_PNEUMATIC = "p";
	//private final static String TIRE_CD_PNEUMATIC = "P";
	//private final static String TIRE_CD_SOLID = "s";
	//private final static String TIRE_CD_SOLID = "S";
	// end defect 10197
	private final static int TWO = 2;
	private final static String TXT_BODY_STYLE = "Body Style:";
	private final static String TXT_CARYNG_CAP = "Carrying Capacity:";
	private final static String TXT_CHK_IF_APPLICABLE =
		"Check if Applicable:";
	private final static String TXT_DIESEL = "Diesel";
	private final static String TXT_DOC_NO = "Document No:";
	private final static String TXT_EMPTY_WT = "Empty Weight:";
	private final static String TXT_ERROR = "ERROR!";
	private final static String TXT_EXEMPT = "Exempt";
	private final static String TXT_EXP_MO_BAD =
		"Expiration month must be between 1 and 12";
	private final static String TXT_EXP_MO_YR = "Expiration Mo/Yr:";
	private final static String TXT_EXP_YR_BAD =
		"Expiration Year is not valid";
	private final static String TXT_GROSS_WT = "Gross Weight:";
	private final static String TXT_MODEL = "Model:";
	private final static String TXT_MODEL_YR_BAD =
		"Model Year is not valid";
	private final static String TXT_PLATE = "Plate:";
	private final static String TXT_PLATE_AGE = "Plate Age:";
	private final static String TXT_PLT_RMVD = "Plate Removed";
	private final static String TXT_PNEUMATIC = "Pneumatic";
	private final static String TXT_SELECT_ONE = "Select One:";
	private final static String TXT_SOLID = "Solid";
	private final static String TXT_TONNAGE = "Tonnage:";
	private final static String TXT_VIN = "VIN:";
	private final static String TXT_YR_MAKE = "Year/Make:";

	// defect 8414 
	private final static String TXT_ELECTRONIC = "Electronic";
	private final static String TXT_PRINTED = "Printed";
	private final static String TXT_NON_TITLED = "Non-Titled";
	//private final static double TEN_THOUSAND_DOLLARS = 10000.00;
	//private final static String TXT_EFF_DT = "Effective Date:";
	//private final static String TXT_FEES_PD = "Fees Paid:";
	//private final static double ZERO_DOLLARS = 0.00;
	// end defect 8414

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aaArgs String[]
	 */
	public static void main(String[] aaArgs)
	{
		try
		{
			FrmMainframeRecordNotAvailableREG014 laFrmMainframeRecordNotAvailableREG014;
			laFrmMainframeRecordNotAvailableREG014 =
				new FrmMainframeRecordNotAvailableREG014();
			laFrmMainframeRecordNotAvailableREG014.setModal(true);
			laFrmMainframeRecordNotAvailableREG014
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmMainframeRecordNotAvailableREG014.show();
			Insets laInsets =
				laFrmMainframeRecordNotAvailableREG014.getInsets();
			laFrmMainframeRecordNotAvailableREG014.setSize(
				laFrmMainframeRecordNotAvailableREG014.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmMainframeRecordNotAvailableREG014.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmMainframeRecordNotAvailableREG014.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			// empty code block
		}
	}

	/**
	 * FrmMainframeRecordNotAvailableREG014 constructor
	 */
	public FrmMainframeRecordNotAvailableREG014()
	{
		super();
		initialize();
	}

	/**
	 * FrmMainframeRecordNotAvailableREG014 constructor
	 * 
	 * @param aaParent Dialog
	 */
	public FrmMainframeRecordNotAvailableREG014(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmMainframeRecordNotAvailableREG014 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmMainframeRecordNotAvailableREG014(JFrame aaParent)
	{
		super(aaParent);
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
			clearAllColor(this);

			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				// defect 8414 
				// Use boolean vs. int 
				if (validateData())
				{
					getController().processData(
						VCMainframeRecordNotAvailableREG014
							.CONFIRM_DOC_NO,
						gettxtDocNo().getText());

					//If DocNo confirmation was successful
					if (csDocNo != null)
					{
						csDocNo = null;
						caVehData.setNoMFRecs(1);
						setDataToDataObject();
						getController().processData(
							AbstractViewController.ENTER,
							caVehData);
					}
				}
				// end defect 8414 
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				// defect 8177
				//RTSHelp.displayHelp(RTSHelp.REG014);
				String lsTransCd = getController().getTransCode();
				if (lsTransCd.equals(TransCdConstant.RENEW))
				{
					RTSHelp.displayHelp(RTSHelp.REG014A);
				}
				else if (lsTransCd.equals(TransCdConstant.TITLE))
				{
					RTSHelp.displayHelp(RTSHelp.REG014B);
				}
				else if (lsTransCd.equals(TransCdConstant.REPL))
				{
					RTSHelp.displayHelp(RTSHelp.REG014C);
				}
				else if (lsTransCd.equals(TransCdConstant.EXCH))
				{
					RTSHelp.displayHelp(RTSHelp.REG014D);
				}
				else if (lsTransCd.equals(TransCdConstant.PAWT))
				{
					RTSHelp.displayHelp(RTSHelp.REG014E);
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
						RTSHelp.displayHelp(RTSHelp.REG014F);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.REG014G);
					}
				}
				else if (lsTransCd.equals(TransCdConstant.TAWPT))
				{
					RTSHelp.displayHelp(RTSHelp.REG014H);
				}
				// end defect 8177
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{
		// Empty code block
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		if (aaFE.getSource().equals(gettxtEmptyWt())
			|| aaFE.getSource().equals(gettxtCarryingCap()))
		{
			String lsEmptyWt = gettxtEmptyWt().getText().trim();
			String lsCarryingCap = gettxtCarryingCap().getText().trim();
			int liGrossWt =
				CommonValidations.calcGrossWeight(
					lsEmptyWt,
					lsCarryingCap);
			getlblGrossWt().setText(Integer.toString(liGrossWt));
		}
		// defect 8466
		// Convert I/O to 1/0 in VIN
		else if (aaFE.getSource().equals(gettxtVIN()))
		{
			String lsVIN = gettxtVIN().getText().trim().toUpperCase();
			// defect 8902
			lsVIN = CommonValidations.convert_i_and_o_to_1_and_0(lsVIN);
			// end defect 8902
			gettxtVIN().setText(lsVIN);
		}
		// end defect 8466
	}

	/**
	 * Return the ivjbuttonPanel property value
	 * 
	 * @return  ButtonPanel
	 */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setBounds(164, 381, 276, 39);
				ivjbuttonPanel.setName("ivjbuttonPanel");
				ivjbuttonPanel.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbuttonPanel;
	}

	/**
	 * Return the ivjchkDiesel property value
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkDiesel()
	{
		if (ivjchkDiesel == null)
		{
			try
			{
				ivjchkDiesel = new JCheckBox();
				ivjchkDiesel.setName("ivjchkDiesel");
				ivjchkDiesel.setLocation(76, 22);
				ivjchkDiesel.setSize(97, 22);
				ivjchkDiesel.setMinimumSize(new Dimension(60, 22));
				ivjchkDiesel.setMaximumSize(new Dimension(60, 22));
				ivjchkDiesel.setActionCommand(TXT_DIESEL);
				ivjchkDiesel.setText(TXT_DIESEL);
				ivjchkDiesel.setEnabled(true);
				// user code begin {1}
				ivjchkDiesel.setMnemonic(java.awt.event.KeyEvent.VK_D);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDiesel;
	}

	/**
	 * This method initializes ivjchkExempt
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkExempt()
	{
		if (ivjchkExempt == null)
		{
			try
			{
				ivjchkExempt = new javax.swing.JCheckBox();
				ivjchkExempt.setName("ivjchkExempt");
				ivjchkExempt.setBounds(76, 45, 97, 22);
				ivjchkExempt.setText(TXT_EXEMPT);
				// user code begin {1}
				ivjchkExempt.setMnemonic(KeyEvent.VK_X);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkExempt;
	}

	/**
	 * Return the ivjchkPltRmvd property value
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPltRmvd()
	{
		if (ivjchkPltRmvd == null)
		{
			try
			{
				ivjchkPltRmvd = new JCheckBox();
				ivjchkPltRmvd.setName("ivjchkPltRmvd");
				ivjchkPltRmvd.setBounds(95, 114, 121, 22);
				ivjchkPltRmvd.setText(TXT_PLT_RMVD);
				ivjchkPltRmvd.setMinimumSize(new Dimension(60, 22));
				ivjchkPltRmvd.setMaximumSize(new Dimension(60, 22));
				ivjchkPltRmvd.setActionCommand(TXT_PLT_RMVD);
				ivjchkPltRmvd.setEnabled(true);
				// user code begin {1}
				ivjchkPltRmvd.setMnemonic(KeyEvent.VK_R);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkPltRmvd;
	}

	/**
	 * Return the ivjcomboBody property value
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboBody()
	{
		if (ivjcomboBody == null)
		{
			try
			{
				ivjcomboBody = new JComboBox();
				ivjcomboBody.setBounds(81, 43, 173, 25);
				ivjcomboBody.setName("ivjcomboBody");
				ivjcomboBody.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboBody.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboBody.setAlignmentY(Component.TOP_ALIGNMENT);
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
		return ivjcomboBody;
	}

	/**
	 * Return the ivjcomboMake property value
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboMake()
	{
		if (ivjcomboMake == null)
		{
			try
			{
				ivjcomboMake = new JComboBox();
				ivjcomboMake.setName("ivjcomboMake");
				ivjcomboMake.setBounds(127, 2, 190, 25);
				ivjcomboMake.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboMake.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboMake.setAlignmentY(Component.BOTTOM_ALIGNMENT);
				ivjcomboMake.setEditable(false);
				// user code begin {1}
				ivjcomboMake.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboMake;
	}

	/**
	 * Return the FrmMainframeRecordNotAvailableREG014ContentPane1 
	 * property value
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmMainframeRecordNotAvailableREG014ContentPane1()
	{
		if (ivjFrmMainframeRecordNotAvailableREG014ContentPane1
			== null)
		{
			try
			{
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1 =
					new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints28 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints27 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints29 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints30 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints31 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints32 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints30.insets =
					new java.awt.Insets(4, 3, 13, 22);
				consGridBagConstraints30.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints30.weighty = 1.0;
				consGridBagConstraints30.weightx = 1.0;
				consGridBagConstraints30.gridy = 3;
				consGridBagConstraints30.gridx = 1;
				consGridBagConstraints31.insets =
					new java.awt.Insets(2, 168, 11, 186);
				consGridBagConstraints31.ipady = 3;
				consGridBagConstraints31.ipadx = 60;
				consGridBagConstraints31.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints31.weighty = 1.0;
				consGridBagConstraints31.weightx = 1.0;
				consGridBagConstraints31.gridwidth = 2;
				consGridBagConstraints31.gridy = 4;
				consGridBagConstraints31.gridx = 0;
				consGridBagConstraints28.insets =
					new java.awt.Insets(5, 3, 2, 22);
				consGridBagConstraints28.ipady = -7;
				consGridBagConstraints28.ipadx = -4;
				consGridBagConstraints28.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints28.weighty = 1.0;
				consGridBagConstraints28.weightx = 1.0;
				consGridBagConstraints28.gridy = 1;
				consGridBagConstraints28.gridx = 1;
				consGridBagConstraints29.insets =
					new java.awt.Insets(3, 3, 3, 22);
				consGridBagConstraints29.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints29.weighty = 1.0;
				consGridBagConstraints29.weightx = 1.0;
				consGridBagConstraints29.gridy = 2;
				consGridBagConstraints29.gridx = 1;
				consGridBagConstraints32.insets =
					new java.awt.Insets(3, 4, 2, 2);
				consGridBagConstraints32.ipady = 393;
				consGridBagConstraints32.ipadx = 354;
				consGridBagConstraints32.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints32.weighty = 1.0;
				consGridBagConstraints32.weightx = 1.0;
				consGridBagConstraints32.gridheight = 4;
				consGridBagConstraints32.gridy = 0;
				consGridBagConstraints32.gridx = 0;
				consGridBagConstraints27.insets =
					new java.awt.Insets(5, 3, 5, 22);
				consGridBagConstraints27.ipady = -13;
				consGridBagConstraints27.ipadx = -4;
				consGridBagConstraints27.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints27.weighty = 1.0;
				consGridBagConstraints27.weightx = 1.0;
				consGridBagConstraints27.gridy = 0;
				consGridBagConstraints27.gridx = 1;
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.setName(
					"FrmMainframeRecordNotAvailableREG014ContentPane1");
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.setLayout(
					null);
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.add(
					getJPanel4(),
					null);
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.add(
					getJPanel5(),
					null);
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.add(
					getJPanel6(),
					null);
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.add(
					getJPanel7(),
					null);
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.add(
					getbuttonPanel(),
					null);
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.add(
					getJPanel(),
					null);
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmMainframeRecordNotAvailableREG014ContentPane1
					.setMinimumSize(
					new Dimension(642, 502));
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
		return ivjFrmMainframeRecordNotAvailableREG014ContentPane1;
	}

	/**
	 * This method initializes ivjJPanel0
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (ivjJPanel0 == null)
		{
			try
			{
				ivjJPanel0 = new JPanel();
				ivjJPanel0.setLayout(null);
				ivjJPanel0.add(getJPanel3(), null);
				ivjJPanel0.add(getJPanel2(), null);
				ivjJPanel0.add(getJPanel1(), null);
				ivjJPanel0.setBounds(4, 3, 355, 359);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
				// user code begin {2}
				// user code end
			}
		}
		return ivjJPanel0;
	}

	/**
	 * Return the JPanel1 property value
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
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setBounds(7, 6, 341, 129);
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMinimumSize(new Dimension(326, 89));
				ivjJPanel1.setMaximumSize(new Dimension(326, 89));
				ivjJPanel1.add(gettxtYear(), null);
				ivjJPanel1.add(getstcLblYearMake(), null);
				ivjJPanel1.add(getcomboMake(), null);
				ivjJPanel1.add(getstcLblBodyStyle(), null);
				ivjJPanel1.add(getcomboBody(), null);
				ivjJPanel1.add(gettxtMake(), null);
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
		return ivjJPanel1;
	}

	/**
	 * Return the JPanel2 property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setBounds(7, 141, 341, 150);
				ivjJPanel2.setLayout(null);
				ivjJPanel2.setMaximumSize(new Dimension(330, 163));
				ivjJPanel2.setMinimumSize(new Dimension(330, 163));
				ivjJPanel2.add(getstcLblModel(), null);
				ivjJPanel2.add(getstcLblVin(), null);
				ivjJPanel2.add(getstcLblDocumentNo(), null);
				ivjJPanel2.add(getstcLblPlate(), null);
				ivjJPanel2.add(getstcLblPlateAge(), null);
				ivjJPanel2.add(gettxtModel(), null);
				ivjJPanel2.add(gettxtVIN(), null);
				ivjJPanel2.add(gettxtDocNo(), null);
				ivjJPanel2.add(gettxtPlateNo(), null);
				ivjJPanel2.add(gettxtPlateAge(), null);
				// defect 9630
				ivjJPanel2.add(getchkPltRmvd(), null);
				// end defect 9630
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel2;
	}

	/**
	 * Return the JPanel3 property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("ivjJPanel3");
				ivjJPanel3.setBounds(7, 296, 262, 55);
				ivjJPanel3.setLayout(null);
				ivjJPanel3.add(getstcLblExpirationMoYear(), null);
				// defect 8414
				//ivjJPanel3.add(getstcLblEffectiveDate(), null);
				//ivjJPanel3.add(gettxtEffDate(), null);
				// end defect 8414 
				ivjJPanel3.add(gettxtExpMo(), null);
				ivjJPanel3.add(gettxtExpYr(), null);
				ivjJPanel3.setMinimumSize(new Dimension(330, 101));
				ivjJPanel3.setMaximumSize(new Dimension(330, 101));

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel3;
	}

	/**
	 * Return the JPanel4 property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel4()
	{
		if (ivjJPanel4 == null)
		{
			try
			{
				ivjJPanel4 = new JPanel();
				ivjJPanel4.setName("ivjJPanel4");
				ivjJPanel4.setBounds(364, 5, 244, 131);
				ivjJPanel4.setLayout(null);
				ivjJPanel4.setMinimumSize(new Dimension(272, 146));
				ivjJPanel4.setMaximumSize(new Dimension(272, 146));
				ivjJPanel4.add(getstcLblTonnage(), null);
				ivjJPanel4.add(getstcLblEmptyWeight(), null);
				ivjJPanel4.add(getstcLblCarryingCapacity(), null);
				ivjJPanel4.add(getstcLblGrossWeight(), null);
				ivjJPanel4.add(getlblGrossWeight(), null);
				ivjJPanel4.add(gettxtTonnage(), null);
				ivjJPanel4.add(gettxtEmptyWt(), null);
				ivjJPanel4.add(gettxtCarryingCap(), null);
				ivjJPanel4.add(getlblGrossWt(), null);
				// defect 8414
				//ivjJPanel4.add(getstcLblFeesPaid(), null);
				//ivjJPanel4.add(gettxtFeesPd(), null);
				// end defect 8414 
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
		return ivjJPanel4;
	}

	/**
	 * Return the JPanel5 property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel5()
	{
		if (ivjJPanel5 == null)
		{
			try
			{
				ivjJPanel5 = new JPanel();
				ivjJPanel5.setName("ivjJPanel5");
				ivjJPanel5.setBounds(364, 144, 244, 86);
				ivjJPanel5.setLayout(null);
				ivjJPanel5.add(getradioPrinted(), null);
				ivjJPanel5.add(getradioElectronic(), null);
				ivjJPanel5.add(getradioNonTitled(), null);
				ivjJPanel5.setMinimumSize(new Dimension(272, 55));
				ivjJPanel5.setMaximumSize(new Dimension(272, 55));
				// defect 9971 
				Border b =
					new TitledBorder(
						new EtchedBorder(),
						TXT_SELECT_ONE);
				ivjJPanel5.setBorder(b);
				caRadioBtnGrp1 = new RTSButtonGroup();
				caRadioBtnGrp1.add(getradioPrinted());
				caRadioBtnGrp1.add(getradioElectronic());
				caRadioBtnGrp1.add(getradioNonTitled());
				// end defect 9971 
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
		return ivjJPanel5;
	}

	/**
	 * Return the JPanel6 property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel6()
	{
		if (ivjJPanel6 == null)
		{
			try
			{
				ivjJPanel6 = new JPanel();
				ivjJPanel6.setName("ivjJPanel6");
				ivjJPanel6.setBounds(364, 235, 244, 71);
				ivjJPanel6.setLayout(null);
				ivjJPanel6.setMaximumSize(new Dimension(280, 90));
				ivjJPanel6.setMinimumSize(new Dimension(280, 90));
				ivjJPanel6.add(getchkDiesel(), null);
				// defect 9023 
				ivjJPanel6.add(getchkExempt(), null);
				// Modify "Check if Diesel" to "Check if Applicable"
				Border b =
					new TitledBorder(
						new EtchedBorder(),
						TXT_CHK_IF_APPLICABLE);
				// end defect 9023 
				ivjJPanel6.setBorder(b);
				// defect 9971 
				caChkboxBtnGrp = new RTSButtonGroup();
				caChkboxBtnGrp.add(getchkDiesel());
				caChkboxBtnGrp.add(getchkExempt());
				// end defect 9971 

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
		return ivjJPanel6;
	}

	/**
	 * Return the JPanel7 property value
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel7()
	{
		if (ivjJPanel7 == null)
		{
			try
			{
				ivjJPanel7 = new JPanel();
				ivjJPanel7.setName("ivjJPanel7");
				ivjJPanel7.setLayout(null);
				ivjJPanel7.add(getradioPneumatic(), null);
				ivjJPanel7.add(getradioSolid(), null);
				ivjJPanel7.setSize(244, 51);
				ivjJPanel7.setMinimumSize(new Dimension(280, 123));
				ivjJPanel7.setMaximumSize(new Dimension(280, 123));
				Border b =
					new TitledBorder(
						new EtchedBorder(),
						TXT_SELECT_ONE);
				ivjJPanel7.setBorder(b);
				// user code begin {1}
				ivjJPanel7.setLocation(364, 311);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel7;
	}

	/**
	 * Return the lblGrossWeight property value
	 * 
	 * @return JLabel
	 */
	private JLabel getlblGrossWeight()
	{
		if (ivjlblGrossWeight == null)
		{
			try
			{
				ivjlblGrossWeight = new JLabel();
				ivjlblGrossWeight.setBounds(0, 0, 0, 0);
				ivjlblGrossWeight.setName("ivjlblGrossWeight");
				ivjlblGrossWeight.setText("0");
				ivjlblGrossWeight.setMaximumSize(new Dimension(7, 14));
				ivjlblGrossWeight.setMinimumSize(new Dimension(7, 14));
				ivjlblGrossWeight.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjlblGrossWeight;
	}

	/**
	 * Return the lblGrossWt property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblGrossWt()
	{
		if (ivjlblGrossWt == null)
		{
			try
			{
				ivjlblGrossWt = new JLabel();
				ivjlblGrossWt.setSize(55, 19);
				ivjlblGrossWt.setName("lblGrossWt");
				ivjlblGrossWt.setText("0");
				ivjlblGrossWt.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjlblGrossWt.setHorizontalTextPosition(
					SwingConstants.RIGHT);
				// user code begin {1}
				ivjlblGrossWt.setLocation(136, 99);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblGrossWt;
	}

	/**
	 * This method initializes ivjradioElectronic
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioElectronic()
	{
		if (ivjradioElectronic == null)
		{
			ivjradioElectronic = new JRadioButton();
			ivjradioElectronic.setBounds(70, 37, 124, 21);
			ivjradioElectronic.setText(TXT_ELECTRONIC);
			ivjradioElectronic.setMnemonic(KeyEvent.VK_E);
		}
		return ivjradioElectronic;
	}

	/**
	 * This method initializes ivjradioNonTitled
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioNonTitled()
	{
		if (ivjradioNonTitled == null)
		{
			ivjradioNonTitled = new JRadioButton();
			ivjradioNonTitled.setBounds(70, 60, 136, 21);
			ivjradioNonTitled.setText(TXT_NON_TITLED);
			ivjradioNonTitled.setMnemonic(KeyEvent.VK_N);
		}
		return ivjradioNonTitled;
	}

	/**
	 * Return the radioPneumatic property value
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioPneumatic()
	{
		if (ivjradioPneumatic == null)
		{
			try
			{
				ivjradioPneumatic = new JRadioButton();
				ivjradioPneumatic.setSize(90, 22);
				ivjradioPneumatic.setName("ivjradioPneumatic");
				ivjradioPneumatic.setMnemonic(
					java.awt.event.KeyEvent.VK_U);
				ivjradioPneumatic.setText(TXT_PNEUMATIC);
				ivjradioPneumatic.setMaximumSize(new Dimension(86, 22));
				ivjradioPneumatic.setActionCommand(TXT_PNEUMATIC);
				ivjradioPneumatic.setMinimumSize(new Dimension(86, 22));
				// user code begin {1}
				ivjradioPneumatic.setLocation(36, 18);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioPneumatic;
	}

	/**
	 * This method initializes ivjradioPrinted
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPrinted()
	{
		if (ivjradioPrinted == null)
		{
			ivjradioPrinted = new JRadioButton();
			ivjradioPrinted.setLocation(70, 16);
			ivjradioPrinted.setSize(82, 21);
			ivjradioPrinted.setText(TXT_PRINTED);
			ivjradioPrinted.setMnemonic(KeyEvent.VK_P);
			ivjradioPrinted.setSelected(true);
		}
		return ivjradioPrinted;
	}

	/**
	 * Return the radioSolid property value
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioSolid()
	{
		if (ivjradioSolid == null)
		{
			try
			{
				ivjradioSolid = new JRadioButton();
				ivjradioSolid.setBounds(135, 18, 80, 22);
				ivjradioSolid.setName("ivjradioSolid");
				ivjradioSolid.setText(TXT_SOLID);
				ivjradioSolid.setMinimumSize(new Dimension(53, 22));
				ivjradioSolid.setMaximumSize(new Dimension(53, 22));
				ivjradioSolid.setActionCommand(TXT_SOLID);
				ivjradioSolid.setEnabled(true);
				// user code begin {1}
				ivjradioSolid.setMnemonic(KeyEvent.VK_S);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioSolid;
	}

	/**
	 * Return the ivjstcLblBodyStyle property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBodyStyle()
	{
		if (ivjstcLblBodyStyle == null)
		{
			try
			{
				ivjstcLblBodyStyle = new JLabel();
				ivjstcLblBodyStyle.setName("ivjstcLblBodyStyle");
				ivjstcLblBodyStyle.setBounds(10, 46, 62, 20);
				ivjstcLblBodyStyle.setAlignmentY(
					Component.TOP_ALIGNMENT);
				ivjstcLblBodyStyle.setText(TXT_BODY_STYLE);
				ivjstcLblBodyStyle.setMinimumSize(
					new Dimension(62, 14));
				ivjstcLblBodyStyle.setMaximumSize(
					new Dimension(62, 14));
				ivjstcLblBodyStyle.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblBodyStyle;
	}

	/**
	 * Return the ivjstcLblCarryingCapacity property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCarryingCapacity()
	{
		if (ivjstcLblCarryingCapacity == null)
		{
			try
			{
				ivjstcLblCarryingCapacity = new JLabel();
				ivjstcLblCarryingCapacity.setName(
					"stcLblCarryingCapacity");
				ivjstcLblCarryingCapacity.setSize(103, 20);
				ivjstcLblCarryingCapacity.setLocation(25, 69);
				ivjstcLblCarryingCapacity.setText(TXT_CARYNG_CAP);
				ivjstcLblCarryingCapacity.setMinimumSize(
					new Dimension(103, 14));
				ivjstcLblCarryingCapacity.setMaximumSize(
					new Dimension(103, 14));
				ivjstcLblCarryingCapacity.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblCarryingCapacity;
	}

	/**
	 * Return the ivjstcLblDocumentNo property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDocumentNo()
	{
		if (ivjstcLblDocumentNo == null)
		{
			try
			{
				ivjstcLblDocumentNo = new JLabel();
				ivjstcLblDocumentNo.setName("ivjstcLblDocumentNo");
				ivjstcLblDocumentNo.setBounds(7, 62, 84, 20);
				ivjstcLblDocumentNo.setText(TXT_DOC_NO);
				ivjstcLblDocumentNo.setMinimumSize(
					new Dimension(79, 14));
				ivjstcLblDocumentNo.setMaximumSize(
					new Dimension(79, 14));
				ivjstcLblDocumentNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblDocumentNo;
	}

	//	/**
	//	 * Return the ivjstcLblEffectiveDate property value
	//	 * 
	//	 * @return JLabel
	//	 */
	//	private JLabel getstcLblEffectiveDate()
	//	{
	//		if (ivjstcLblEffectiveDate == null)
	//		{
	//			try
	//			{
	//				ivjstcLblEffectiveDate = new JLabel();
	//				ivjstcLblEffectiveDate.setName(
	//					"ivjstcLblEffectiveDate");
	//				ivjstcLblEffectiveDate.setBounds(27, 25, 85, 20);
	//				ivjstcLblEffectiveDate.setText(TXT_EFF_DT);
	//				ivjstcLblEffectiveDate.setMaximumSize(
	//					new Dimension(81, 14));
	//				ivjstcLblEffectiveDate.setMinimumSize(
	//					new Dimension(81, 14));
	//				ivjstcLblEffectiveDate.setHorizontalAlignment(
	//					SwingConstants.RIGHT);
	//				// user code begin {1}
	//				// user code end
	//			}
	//			catch (Throwable aeIVJEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIVJEx);
	//			}
	//		}
	//		return ivjstcLblEffectiveDate;
	//	}

	/**
	 * Return the ivjstcLblEmptyWeight property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEmptyWeight()
	{
		if (ivjstcLblEmptyWeight == null)
		{
			try
			{
				ivjstcLblEmptyWeight = new JLabel();
				ivjstcLblEmptyWeight.setName("ivjstcLblEmptyWeight");
				ivjstcLblEmptyWeight.setLocation(47, 39);
				ivjstcLblEmptyWeight.setSize(81, 20);
				ivjstcLblEmptyWeight.setText(TXT_EMPTY_WT);
				ivjstcLblEmptyWeight.setMinimumSize(
					new Dimension(81, 14));
				ivjstcLblEmptyWeight.setMaximumSize(
					new Dimension(81, 14));
				ivjstcLblEmptyWeight.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// defect 9971
				ivjstcLblEmptyWeight.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_W);
				ivjstcLblEmptyWeight.setLabelFor(gettxtEmptyWt());
				// end defect 9971
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblEmptyWeight;
	}

	/**
	 * Return the ivjstcLblExpirationMoYear property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblExpirationMoYear()
	{
		if (ivjstcLblExpirationMoYear == null)
		{
			try
			{
				ivjstcLblExpirationMoYear = new JLabel();
				ivjstcLblExpirationMoYear.setBounds(12, 20, 102, 20);
				ivjstcLblExpirationMoYear.setName(
					"ivjstcLblExpirationMoYear");
				ivjstcLblExpirationMoYear.setText(TXT_EXP_MO_YR);
				ivjstcLblExpirationMoYear.setMinimumSize(
					new Dimension(95, 14));
				ivjstcLblExpirationMoYear.setMaximumSize(
					new Dimension(95, 14));
				ivjstcLblExpirationMoYear.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblExpirationMoYear;
	}

	//	/**
	//	 * Return the ivjstcLblFeesPaid property value
	//	 * 
	//	 * @return JLabel
	//	 */
	//	private JLabel getstcLblFeesPaid()
	//	{
	//		if (ivjstcLblFeesPaid == null)
	//		{
	//			try
	//			{
	//				ivjstcLblFeesPaid = new JLabel();
	//				ivjstcLblFeesPaid.setName("ivjstcLblFeesPaid");
	//				ivjstcLblFeesPaid.setLocation(70, 129);
	//				ivjstcLblFeesPaid.setSize(58, 20);
	//				ivjstcLblFeesPaid.setText(TXT_FEES_PD);
	//				ivjstcLblFeesPaid.setMaximumSize(new Dimension(58, 14));
	//				ivjstcLblFeesPaid.setMinimumSize(new Dimension(58, 14));
	//				ivjstcLblFeesPaid.setHorizontalAlignment(
	//					SwingConstants.RIGHT);
	//				// user code begin {1}
	//				// defect 9971 
	//				ivjstcLblFeesPaid.setDisplayedMnemonic(
	//					java.awt.event.KeyEvent.VK_F);
	//				ivjstcLblFeesPaid.setLabelFor(gettxtFeesPd());
	//				// end defect 9971
	//				// user code end
	//			}
	//			catch (Throwable aeIVJEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIVJEx);
	//			}
	//		}
	//		return ivjstcLblFeesPaid;
	//	}

	/**
	 * Return the ivjstcLblGrossWeight property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblGrossWeight()
	{
		if (ivjstcLblGrossWeight == null)
		{
			try
			{
				ivjstcLblGrossWeight = new JLabel();
				ivjstcLblGrossWeight.setName("ivjstcLblGrossWeight");
				ivjstcLblGrossWeight.setSize(80, 20);
				ivjstcLblGrossWeight.setLocation(48, 98);
				ivjstcLblGrossWeight.setText(TXT_GROSS_WT);
				ivjstcLblGrossWeight.setMinimumSize(
					new Dimension(80, 14));
				ivjstcLblGrossWeight.setMaximumSize(
					new Dimension(80, 14));
				ivjstcLblGrossWeight.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblGrossWeight;
	}

	/**
	 * Return the ivjstcLblModel property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblModel()
	{
		if (ivjstcLblModel == null)
		{
			try
			{
				ivjstcLblModel = new JLabel();
				ivjstcLblModel.setName("ivjstcLblModel");
				ivjstcLblModel.setBounds(46, 6, 45, 20);
				ivjstcLblModel.setText(TXT_MODEL);
				ivjstcLblModel.setMinimumSize(new Dimension(37, 14));
				ivjstcLblModel.setMaximumSize(new Dimension(37, 14));
				ivjstcLblModel.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblModel;
	}

	/**
	 * Return the ivjstcLblPlate property value
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPlate()
	{
		if (ivjstcLblPlate == null)
		{
			try
			{
				ivjstcLblPlate = new JLabel();
				ivjstcLblPlate.setName("ivjstcLblPlate");
				ivjstcLblPlate.setLocation(45, 92);
				ivjstcLblPlate.setSize(46, 20);
				ivjstcLblPlate.setText(TXT_PLATE);
				ivjstcLblPlate.setMinimumSize(new Dimension(32, 14));
				ivjstcLblPlate.setMaximumSize(new Dimension(32, 14));
				ivjstcLblPlate.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblPlate;
	}

	/**
	 * Return the ivjstcLblPlateAge property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlateAge()
	{
		if (ivjstcLblPlateAge == null)
		{
			try
			{
				ivjstcLblPlateAge = new JLabel();
				ivjstcLblPlateAge.setName("ivjstcLblPlateAge");
				ivjstcLblPlateAge.setBounds(189, 92, 65, 20);
				ivjstcLblPlateAge.setText(TXT_PLATE_AGE);
				ivjstcLblPlateAge.setMinimumSize(new Dimension(57, 14));
				ivjstcLblPlateAge.setMaximumSize(new Dimension(57, 14));
				ivjstcLblPlateAge.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblPlateAge.setEnabled(true);
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
		return ivjstcLblPlateAge;
	}

	/**
	 * Return the ivjstcLblTonnage property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTonnage()
	{
		if (ivjstcLblTonnage == null)
		{
			try
			{
				ivjstcLblTonnage = new JLabel();
				ivjstcLblTonnage.setName("ivjstcLblTonnage");
				ivjstcLblTonnage.setLocation(76, 9);
				ivjstcLblTonnage.setSize(52, 20);
				ivjstcLblTonnage.setText(TXT_TONNAGE);
				ivjstcLblTonnage.setMinimumSize(new Dimension(52, 14));
				ivjstcLblTonnage.setMaximumSize(new Dimension(52, 14));
				ivjstcLblTonnage.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblTonnage;
	}

	/**
	 * Return the ivjstcLblVin property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVin()
	{
		if (ivjstcLblVin == null)
		{
			try
			{
				ivjstcLblVin = new JLabel();
				ivjstcLblVin.setName("ivjstcLblVin");
				ivjstcLblVin.setBounds(46, 33, 45, 20);
				ivjstcLblVin.setText(TXT_VIN);
				ivjstcLblVin.setMinimumSize(new Dimension(22, 14));
				ivjstcLblVin.setMaximumSize(new Dimension(22, 14));
				ivjstcLblVin.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblVin;
	}

	/**
	 * Return the ivjstcLblYearMake property value
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblYearMake()
	{
		if (ivjstcLblYearMake == null)
		{
			try
			{
				ivjstcLblYearMake = new JLabel();
				ivjstcLblYearMake.setName("ivjstcLblYearMake");
				ivjstcLblYearMake.setBounds(10, 3, 66, 20);
				ivjstcLblYearMake.setAlignmentY(
					Component.TOP_ALIGNMENT);
				ivjstcLblYearMake.setText(TXT_YR_MAKE);
				ivjstcLblYearMake.setMinimumSize(new Dimension(63, 14));
				ivjstcLblYearMake.setMaximumSize(new Dimension(63, 14));
				ivjstcLblYearMake.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblYearMake;
	}

	/**
	 * Return the ivjtxtCarryingCap property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCarryingCap()
	{
		if (ivjtxtCarryingCap == null)
		{
			try
			{
				ivjtxtCarryingCap = new RTSInputField();
				ivjtxtCarryingCap.setName("ivjtxtCarryingCap");
				ivjtxtCarryingCap.setLocation(136, 69);
				ivjtxtCarryingCap.setSize(56, 20);
				ivjtxtCarryingCap.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCarryingCap.setEnabled(true);
				// user code begin {1}
				ivjtxtCarryingCap.setMaxLength(6);
				ivjtxtCarryingCap.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtCarryingCap.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtCarryingCap;
	}

	/**
	 * Return the ivjtxtDocNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtDocNo()
	{
		if (ivjtxtDocNo == null)
		{
			try
			{
				ivjtxtDocNo = new RTSInputField();
				ivjtxtDocNo.setName("ivjtxtDocNo");
				ivjtxtDocNo.setBounds(99, 62, 156, 20);
				ivjtxtDocNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtDocNo.setMaxLength(17);
				ivjtxtDocNo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtDocNo.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtDocNo;
	}

	//	/**
	//	 * Return the ivjtxtEffDate property value
	//	 * 
	//	 * @return RTSDateField
	//	 */
	//	private RTSDateField gettxtEffDate()
	//	{
	//		if (ivjtxtEffDate == null)
	//		{
	//			try
	//			{
	//				ivjtxtEffDate = new RTSDateField();
	//				ivjtxtEffDate.setName("ivjtxtEffDate");
	//				ivjtxtEffDate.setLocation(122, 25);
	//				ivjtxtEffDate.setSize(72, 20);
	//				ivjtxtEffDate.setHighlighter(
	//					new javax
	//						.swing
	//						.plaf
	//						.basic
	//						.BasicTextUI
	//						.BasicHighlighter());
	//				// user code begin {1}
	//				// user code end
	//			}
	//			catch (Throwable aeIVJEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIVJEx);
	//			}
	//		}
	//		return ivjtxtEffDate;
	//	}

	/**
	 * Return the ivjtxtEmptyWt property value
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtEmptyWt()
	{
		if (ivjtxtEmptyWt == null)
		{
			try
			{
				ivjtxtEmptyWt = new RTSInputField();
				ivjtxtEmptyWt.setName("ivjtxtEmptyWt");
				ivjtxtEmptyWt.setLocation(136, 39);
				ivjtxtEmptyWt.setSize(56, 20);
				ivjtxtEmptyWt.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEmptyWt.setText("");
				ivjtxtEmptyWt.setEnabled(true);
				// user code begin {1}
				ivjtxtEmptyWt.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtEmptyWt.setMaxLength(6);
				ivjtxtEmptyWt.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtEmptyWt;
	}

	/**
	 * Return the ivjtxtExpMo property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtExpMo()
	{
		if (ivjtxtExpMo == null)
		{
			try
			{
				ivjtxtExpMo = new RTSInputField();
				ivjtxtExpMo.setName("ivjtxtExpMo");
				ivjtxtExpMo.setBounds(124, 20, 23, 20);
				ivjtxtExpMo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtExpMo.setMaxLength(2);
				ivjtxtExpMo.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtExpMo;
	}

	/**
	 * Return the ivjtxtExpYr property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtExpYr()
	{
		if (ivjtxtExpYr == null)
		{
			try
			{
				ivjtxtExpYr = new RTSInputField();
				ivjtxtExpYr.setName("ivjtxtExpYr");
				ivjtxtExpYr.setBounds(154, 20, 40, 20);
				ivjtxtExpYr.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtExpYr.setMaxLength(4);
				ivjtxtExpYr.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtExpYr;
	}

	//	/**
	//	 * Return the ivjtxtFeesPd property value
	//	 * 
	//	 * @return RTSInputField
	//	 */
	//	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	//	private RTSInputField gettxtFeesPd()
	//	{
	//		if (ivjtxtFeesPd == null)
	//		{
	//			try
	//			{
	//				ivjtxtFeesPd = new RTSInputField();
	//				ivjtxtFeesPd.setName("ivjtxtFeesPd");
	//				ivjtxtFeesPd.setLocation(136, 129);
	//				ivjtxtFeesPd.setSize(43, 20);
	//				ivjtxtFeesPd.setHighlighter(
	//					new javax
	//						.swing
	//						.plaf
	//						.basic
	//						.BasicTextUI
	//						.BasicHighlighter());
	//				ivjtxtFeesPd.setEnabled(true);
	//				ivjtxtFeesPd.setText("");
	//				// user code begin {1}
	//				ivjtxtFeesPd.setMaxLength(7);
	//				ivjtxtFeesPd.setInput(RTSInputField.DOLLAR_ONLY);
	//				// user code end
	//			}
	//			catch (Throwable aeIVJEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIVJEx);
	//			}
	//		}
	//		return ivjtxtFeesPd;
	//	}

	/**
	 * Return the ivjtxtMake property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMake()
	{
		if (ivjtxtMake == null)
		{
			try
			{
				ivjtxtMake = new RTSInputField();
				ivjtxtMake.setName("ivjtxtMake");
				ivjtxtMake.setLocation(128, 3);
				ivjtxtMake.setSize(52, 20);
				// user code begin {1}
				ivjtxtMake.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtMake.setMaxLength(4);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMake;
	}

	/**
	 * Return the ivjtxtModel property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtModel()
	{
		if (ivjtxtModel == null)
		{
			try
			{
				ivjtxtModel = new RTSInputField();
				ivjtxtModel.setName("ivjtxtModel");
				ivjtxtModel.setLocation(99, 6);
				ivjtxtModel.setSize(31, 20);
				ivjtxtModel.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtModel.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtModel.setMaxLength(3);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtModel;
	}

	/**
	 * Return the ivjtxtPlateAge property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPlateAge()
	{
		if (ivjtxtPlateAge == null)
		{
			try
			{
				ivjtxtPlateAge = new RTSInputField();
				ivjtxtPlateAge.setName("ivjtxtPlateAge");
				ivjtxtPlateAge.setLocation(263, 92);
				ivjtxtPlateAge.setSize(23, 20);
				ivjtxtPlateAge.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPlateAge.setEnabled(true);
				// user code begin {1}
				ivjtxtPlateAge.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtPlateAge.setMaxLength(2);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPlateAge;
	}

	/**
	 * Return the ivjtxtPlateNo property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPlateNo()
	{
		if (ivjtxtPlateNo == null)
		{
			try
			{
				ivjtxtPlateNo = new RTSInputField();
				ivjtxtPlateNo.setName("ivjtxtPlateNo");
				ivjtxtPlateNo.setBounds(99, 92, 80, 20);
				ivjtxtPlateNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPlateNo.setMaxLength(7);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPlateNo;
	}

	/**
	 * Return the ivjtxtTonnage property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTonnage()
	{
		if (ivjtxtTonnage == null)
		{
			try
			{
				ivjtxtTonnage = new RTSInputField();
				ivjtxtTonnage.setBounds(136, 9, 43, 20);
				ivjtxtTonnage.setName("ivjtxtTonnage");
				ivjtxtTonnage.setEnabled(true);
				// user code begin {1}
				ivjtxtTonnage.setInput(RTSInputField.DOLLAR_ONLY);
				ivjtxtTonnage.setMaxLength(5);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtTonnage;
	}

	/**
	 * Return the ivjtxtVIN property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVIN()
	{
		if (ivjtxtVIN == null)
		{
			try
			{
				ivjtxtVIN = new RTSInputField();
				ivjtxtVIN.setName("ivjtxtVIN");
				ivjtxtVIN.setBounds(99, 33, 200, 20);
				ivjtxtVIN.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtVIN.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVIN.setMaxLength(22);
				// defect 8466
				ivjtxtVIN.addFocusListener(this);
				// end defect 8466 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVIN;
	}

	/**
	 * Return the ivjtxtYear property value
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtYear()
	{
		if (ivjtxtYear == null)
		{
			try
			{
				ivjtxtYear = new RTSInputField();
				ivjtxtYear.setName("ivjtxtYear");
				ivjtxtYear.setLocation(81, 3);
				ivjtxtYear.setSize(40, 20);
				ivjtxtYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtYear.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtYear.setMaxLength(4);
				ivjtxtYear.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtYear;
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
			caRadioBtnGrp.add(getradioPneumatic());
			caRadioBtnGrp.add(getradioSolid());
			gettxtMake().setVisible(false);
			// user code end
			setName(FRM_NAME_REG014);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(638, 449);
			setTitle(FRM_TITLE_REG014);
			setContentPane(
				getFrmMainframeRecordNotAvailableREG014ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
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
			|| liExpYear > (RTSDate.getCurrentDate().getYear() + FIVE))
		{
			lbRetVal = true;
		}
		return lbRetVal;
	}

	/**
	 * Validates that the year model input is between 1880 and 
	 * current year + 2.
	 *
	 * @return boolean
	 */
	private boolean isInvalidYearModel()
	{
		String lsYearModel = gettxtYear().getText();
		boolean lbRetVal = false;
		int liYearModel = Integer.parseInt(lsYearModel);
		if (liYearModel <= EIGHTEEN_EIGHTY
			|| liYearModel > (RTSDate.getCurrentDate().getYear() + TWO))
		{
			lbRetVal = true;
		}
		return lbRetVal;
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
		if (aaIE.getSource() == getcomboMake())
		{
			String lsSelected =
				(String) getcomboMake().getSelectedItem();
			if (lsSelected != null && lsSelected.equals(NEW))
			{
				getcomboMake().setVisible(false);
				gettxtMake().setVisible(true);
				gettxtMake().setEnabled(true);
				gettxtMake().setText(CommonConstant.STR_SPACE_EMPTY);
				gettxtMake().requestFocus();
			}
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
		if (aaDataObject instanceof String)
		{
			csDocNo = (String) aaDataObject;
		}
		else
		{
			caVehData = (VehicleInquiryData) aaDataObject;
			String lsTransCd = getController().getTransCode();
			caReqdData =
				RegistrationClassCache.getRegisClass(
					caVehData
						.getMfVehicleData()
						.getVehicleData()
						.getVehClassCd(),
					caVehData
						.getMfVehicleData()
						.getRegData()
						.getRegClassCd(),
					caVehData.getRTSEffDt());

			if (caReqdData.getEmptyWtReqd() == 0)
			{
				getstcLblEmptyWeight().setEnabled(false);
				ivjtxtEmptyWt.setEnabled(false);
			}
			if (caReqdData.getCaryngCapReqd() == 0)
			{
				getstcLblCarryingCapacity().setEnabled(false);
				ivjtxtCarryingCap.setEnabled(false);
			}
			if (caReqdData.getTonReqd() == 0)
			{
				getstcLblTonnage().setEnabled(false);
				ivjtxtTonnage.setEnabled(false);
			}
			if (caReqdData.getDieselReqd() == 0)
			{
				ivjchkDiesel.setEnabled(false);
			}
			// defect 8414 
			//			if (!lsTransCd.equals(TransCdConstant.CORREG)
			//				&& !lsTransCd.equals(TransCdConstant.EXCH)
			//				&& !lsTransCd.equals(TransCdConstant.PAWT)
			//				&& !lsTransCd.equals(TransCdConstant.TITLE))
			//			{
			//				getstcLblFeesPaid().setEnabled(false);
			//				ivjtxtFeesPd.setEnabled(false);
			//			}
			// end defect 8414 

			// defect 9389 
			// Prefill/Disable Plate No, Plate Age if Special Plate  
			if (!lsTransCd.equals(TransCdConstant.RENEW)
				&& !lsTransCd.equals(TransCdConstant.TITLE))
			{
				getstcLblPlateAge().setEnabled(false);
				ivjtxtPlateAge.setEnabled(false);
			}
			else if (caVehData.getMfVehicleData().isSpclPlt())
			{
				SpecialPlatesRegisData laSpclPltRegisData =
					caVehData.getMfVehicleData().getSpclPltRegisData();

				ivjtxtPlateAge.setText(
					"" + laSpclPltRegisData.getRegPltAge(false));

				if (laSpclPltRegisData.getRegPltNo() != null)
				{
					ivjtxtPlateNo.setText(
						laSpclPltRegisData.getRegPltNo().trim());
				}
				ivjtxtPlateAge.setEnabled(false);
				ivjtxtPlateNo.setEnabled(false);
			}
			// end defect 9389 

			//Get Tire type codes from Regis Wt Fees
			RegistrationData laRegData =
				caVehData.getMfVehicleData().getRegData();

			// defect 9023 
			// Add Exempt Checkbox 
			boolean lbExemptAuth =
				(getController()
					.getMediator()
					.getDesktop()
					.getSecurityData()
					.getExmptAuthAccs()
					== 1);

			cbStandardExempt =
				CommonFeesCache.isStandardExempt(
					laRegData.getRegClassCd());

			if (cbStandardExempt)
			{
				getchkExempt().setSelected(true);
				getchkExempt().setEnabled(false);
				getstcLblExpirationMoYear().setEnabled(false);
				gettxtExpMo().setEnabled(false);
				gettxtExpYr().setEnabled(false);
				// defect 8414
				//	if (gettxtFeesPd().isEnabled())
				//	{
				//		gettxtFeesPd().setText("0.00");
				//		getstcLblFeesPaid().setEnabled(false);
				//	}
				// end defect 8414 
			}
			else
			{
				getchkExempt().setEnabled(lbExemptAuth);
			}
			// end defect 9023 

			// defect 8310				
			// Enable Solid Tire Radio Button based upon Cache 	
			getradioSolid().setEnabled(
				RegistrationWeightFeesCache.canUseSolidTires(
					laRegData.getRegClassCd()));

			//Assign Search information to text boxes
			if (caVehData.getMfVehicleData().getRegData().getRegPltNo()
				!= null)
			{
				ivjtxtPlateNo.setText(
					caVehData
						.getMfVehicleData()
						.getRegData()
						.getRegPltNo());
			}
			else if (
				caVehData.getMfVehicleData().getVehicleData().getVin()
					!= null)
			{
				ivjtxtVIN.setText(
					caVehData
						.getMfVehicleData()
						.getVehicleData()
						.getVin());
			}
			else if (
				caVehData.getMfVehicleData().getTitleData().getDocNo()
					!= null)
			{
				ivjtxtDocNo.setText(
					caVehData
						.getMfVehicleData()
						.getTitleData()
						.getDocNo());
			}
			//Populate Vehicle Make combo box
			Vector lvVM = new Vector();
			cvVehMakes = VehicleMakesCache.getVehMks();
			UtilityMethods.sort(cvVehMakes);
			int liSize1 = cvVehMakes.size();
			for (int i = 0; i < liSize1; i++)
			{
				VehicleMakesData laMakeTypes =
					(VehicleMakesData) cvVehMakes.get(i);
				String lsMakeTypes =
					laMakeTypes.getVehMk()
						+ DASH
						+ laMakeTypes.getVehMkDesc();
				lvVM.addElement(lsMakeTypes);
			}
			lvVM.addElement(NEW);
			ComboBoxModel laModel1 = new DefaultComboBoxModel(lvVM);
			ivjcomboMake.setModel(laModel1);
			ivjcomboMake.setSelectedIndex(-1);

			// defect 8479
			comboBoxHotKeyFix(getcomboMake());
			// end defect 8479

			//Populate Vehicle Body Type combo box
			Vector lvVBT = new Vector();
			cvVehBodyTypes = VehicleBodyTypesCache.getVehBdyTypesVec();
			UtilityMethods.sort(cvVehBodyTypes);
			int liSize2 = cvVehBodyTypes.size();
			for (int i = 0; i < liSize2; i++)
			{
				VehicleBodyTypesData laBodyTypes =
					(VehicleBodyTypesData) cvVehBodyTypes.get(i);
				String lsBodyTypes =
					laBodyTypes.getVehBdyType()
						+ DASH
						+ laBodyTypes.getVehBdyTypeDesc();
				lvVBT.addElement(lsBodyTypes);
			}
			ComboBoxModel laModel = new DefaultComboBoxModel(lvVBT);
			ivjcomboBody.setModel(laModel);
			ivjcomboBody.setSelectedIndex(-1);
			// defect 8479
			comboBoxHotKeyFix(getcomboBody());
			// end defect 8479
			getradioPneumatic().setSelected(true);
			gettxtYear().requestFocus();
		}
	}

	/**
	 * Places all of the data in Vehicle Inquiry Data
	 */
	public void setDataToDataObject()
	{
		// Vehicle Year
		caVehData.getMfVehicleData().getVehicleData().setVehModlYr(
			Integer.parseInt(ivjtxtYear.getText().trim()));

		// Vehicle Make  
		int liMk = 0;
		String lsMk = null;
		if (getcomboMake().isVisible())
		{
			liMk = ivjcomboMake.getSelectedIndex();
			VehicleMakesData laMakeTypes =
				(VehicleMakesData) cvVehMakes.get(liMk);
			lsMk = laMakeTypes.getVehMk();
		}
		else
		{
			lsMk = gettxtMake().getText().trim();
		}
		caVehData.getMfVehicleData().getVehicleData().setVehMk(lsMk);

		// Vehicle Body
		int liBdy = 0;
		liBdy = ivjcomboBody.getSelectedIndex();
		VehicleBodyTypesData lBodyTypes =
			(VehicleBodyTypesData) cvVehBodyTypes.get(liBdy);
		caVehData.getMfVehicleData().getVehicleData().setVehBdyType(
			lBodyTypes.getVehBdyType());

		// Vehicle Model
		caVehData.getMfVehicleData().getVehicleData().setVehModl(
			ivjtxtModel.getText().trim().toUpperCase());

		// VIN
		String lsVIN;
		lsVIN = ivjtxtVIN.getText().trim().toUpperCase();
		// defect 8902
		lsVIN = CommonValidations.convert_i_and_o_to_1_and_0(lsVIN);
		// end defect 8902
		ivjtxtVIN.setText(lsVIN);
		caVehData.getMfVehicleData().getVehicleData().setVin(lsVIN);

		// Doc No
		caVehData.getMfVehicleData().getTitleData().setDocNo(
			ivjtxtDocNo.getText().trim());

		if (getchkPltRmvd().isSelected())
		{
			caVehData.getMfVehicleData().getRegData().setRmvdRegPltNo(
				ivjtxtPlateNo.getText().trim().toUpperCase());
			// defect 8414
			caVehData.getMfVehicleData().getRegData().setRegPltNo(
			//"NOPLATE");
			RegistrationConstant.NOPLATE);
			// end defect 8414  
		}
		else
		{
			caVehData.getMfVehicleData().getRegData().setRegPltNo(
				ivjtxtPlateNo.getText().trim().toUpperCase());
		}
		// end defect 9630

		// Plate Age
		if (ivjtxtPlateAge
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			caVehData.getMfVehicleData().getRegData().setRegPltAge(0);
		}
		else
		{
			caVehData.getMfVehicleData().getRegData().setRegPltAge(
				Integer.parseInt(ivjtxtPlateAge.getText().trim()));
		}

		// Exp Mo
		if (ivjtxtExpMo
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			caVehData.getMfVehicleData().getRegData().setRegExpMo(0);
		}
		else
		{
			caVehData.getMfVehicleData().getRegData().setRegExpMo(
				Integer.parseInt(ivjtxtExpMo.getText().trim()));
		}

		// Exp Yr
		if (ivjtxtExpYr
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			caVehData.getMfVehicleData().getRegData().setRegExpYr(0);
		}
		else
		{
			caVehData.getMfVehicleData().getRegData().setRegExpYr(
				Integer.parseInt(ivjtxtExpYr.getText().trim()));
		}

		// Tonnage
		String lsTonnage = ivjtxtTonnage.getText().trim();

		// defect 9044 
		// Also assign when length = 0 
		if (lsTonnage.length() > 0)
		{
			caVehData.getMfVehicleData().getVehicleData().setVehTon(
				new Dollar(lsTonnage));
		}
		else
		{
			caVehData.getMfVehicleData().getVehicleData().setVehTon(
				CommonConstant.ZERO_DOLLAR);
		}
		// end defect 9044 

		// Empty Wt
		if (ivjtxtEmptyWt
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			caVehData
				.getMfVehicleData()
				.getVehicleData()
				.setVehEmptyWt(
				0);
		}
		else
		{
			caVehData
				.getMfVehicleData()
				.getVehicleData()
				.setVehEmptyWt(
				Integer.parseInt(ivjtxtEmptyWt.getText().trim()));
		}

		// Carrying Cap
		if (ivjtxtCarryingCap
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			caVehData.getMfVehicleData().getRegData().setVehCaryngCap(
				0);
		}
		else
		{
			caVehData.getMfVehicleData().getRegData().setVehCaryngCap(
				Integer.parseInt(ivjtxtCarryingCap.getText().trim()));
		}

		// Gross Wt
		caVehData.getMfVehicleData().getRegData().setVehGrossWt(
			Integer.parseInt(ivjlblGrossWt.getText().trim()));

		// DeiselIndi
		if (getchkDiesel().isSelected() == true)
		{
			caVehData
				.getMfVehicleData()
				.getVehicleData()
				.setDieselIndi(
				1);
		}
		// defect 9023 
		if (getchkExempt().isSelected())
		{
			caVehData.getMfVehicleData().getRegData().setExmptIndi(1);
		}
		// end defect 9023 

		// TireType
		if (getradioPneumatic().isSelected() == true)
		{
			// defect 10317
			//caVehData.getMfVehicleData().getRegData().setTireTypeCd(
			//	TIRE_CD_PNEUMATIC);
			caVehData.getMfVehicleData().getRegData().setTireTypeCd(
							CommonConstant.TIRE_TYPE_PNEUMATIC);
			// end defect 10317
		}
		else if (getradioSolid().isSelected() == true)
		{
			// defect 10137
			//caVehData.getMfVehicleData().getRegData().setTireTypeCd(
			//	TIRE_CD_SOLID);
			caVehData.getMfVehicleData().getRegData().setTireTypeCd(
							CommonConstant.TIRE_TYPE_SOLID);
			// end defect 10317
		}

		// defect 8414 
		//Get Effective Date
		//caVehData.getMfVehicleData().getRegData().setRegEffDt(
		//			ivjtxtEffDate.getDate().getYYYYMMDDDate());
		// end defect 8414 

		// defect 9971 
		// ETtlCd 
		TitleData laTitleData =
			caVehData.getMfVehicleData().getTitleData();

		if (getradioPrinted().isSelected())
		{
			laTitleData.setETtlCd(
				TitleConstant.NEGOTIABLE_PAPER_ETTLCD);
		}
		else if (getradioElectronic().isSelected())
		{
			laTitleData.setETtlCd(TitleConstant.ELECTRONIC_ETTLCD);
		}
		else
		{
			laTitleData.setETtlCd(TitleConstant.NON_NEGOTIABLE_ETTLCD);
		}
		// defect 9971
	}

	/**
	 * Validates data entered in the input fields.
	 */
	public boolean validateData()
	{
		// defect 8414
		// No validation EffDate, Fees 
		// Use boolean for return vs. int; 
		// All Errors to Constants 
		boolean lbReturn = true;

		RTSException leRTSE = new RTSException();
		if (!ivjtxtYear
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			if (isInvalidYearModel())
			{
				RTSException leMsg =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						TXT_MODEL_YR_BAD,
						TXT_ERROR);
				leRTSE.addException(leMsg, gettxtYear());
			}
		}
		else
		{
			RTSException leMsg =
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					TXT_MODEL_YR_BAD,
					TXT_ERROR);
			leRTSE.addException(leMsg, gettxtYear());
		}

		if (getcomboMake().isVisible()
			&& getcomboMake().getSelectedIndex() == -1)
		{
			//leRTSE.addException(new RTSException(150), getcomboMake());
			leRTSE.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getcomboMake());
		}

		if (gettxtMake().isVisible())
		{
			if (gettxtMake().getText().trim().length() < 1)
			{
				leRTSE.addException(
				//new RTSException(150),
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtMake());
			}
		}
		if (getcomboBody().getSelectedIndex() == -1)
		{
			//leRTSE.addException(new RTSException(150), getcomboBody());
			leRTSE.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getcomboBody());
		}

		String lsDocNo = gettxtDocNo().getText().trim();
		if (lsDocNo.equals(CommonConstant.STR_SPACE_EMPTY)
			|| lsDocNo.endsWith(NINE_ZEROES))
		{
			//leRTSE.addException(new RTSException(150), gettxtDocNo());
			leRTSE.addException(new RTSException(150), gettxtDocNo());
		}
		else if (gettxtDocNo().getText().length() < MAX_DOC_LENGTH)
		{
			leRTSE.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtDocNo());
		}

		String lsRegPltNo = gettxtPlateNo().getText().trim();
		// defect 9630
		//	Add PltRmvdCd to RegPltNo and RegPltAge
		//	If selected, then ignore edits
		if (!getchkPltRmvd().isSelected() && lsRegPltNo.length() == 0)
		{
			//leRTSE.addException(new RTSException(150), gettxtPlateNo());
			leRTSE.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPlateNo());
		}
		String lsRegPltAge = gettxtPlateAge().getText().trim();
		if (!getchkPltRmvd().isSelected()
			&& !(lsRegPltAge.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			int liPlateAge = Integer.parseInt(lsRegPltAge);
			if (liPlateAge > MAX_PLT_AGE)
			{
				leRTSE.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtPlateAge());
			}
		}
		// end defect 9630


		if (!cbStandardExempt)
		{
			String lsExpMonth = gettxtExpMo().getText().trim();
			if (!lsExpMonth.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				int liExpMo = Integer.parseInt(lsExpMonth);
				if (liExpMo < 1 || liExpMo > 12)
				{
					RTSException leMsg =
						new RTSException(
							RTSException.FAILURE_MESSAGE,
							TXT_EXP_MO_BAD,
							TXT_ERROR);
					leRTSE.addException(leMsg, gettxtExpMo());
				}
			}
			else
			{
				RTSException leMsg =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						TXT_EXP_MO_BAD,
						TXT_ERROR);
				leRTSE.addException(leMsg, gettxtExpMo());
			}
			if (!ivjtxtExpYr
				.getText()
				.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				if (isInvalidExpYear())
				{
					RTSException leMsg =
						new RTSException(
							RTSException.FAILURE_MESSAGE,
							TXT_EXP_YR_BAD,
							TXT_ERROR);
					leRTSE.addException(leMsg, gettxtExpYr());
				}
			}
			else
			{
				RTSException leMsg =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						TXT_EXP_YR_BAD,
						TXT_ERROR);
				leRTSE.addException(leMsg, gettxtExpYr());
			}
		}
		// end defect 9023

		String lsVehTon = gettxtTonnage().getText().trim();
		if (gettxtTonnage().isEnabled()
			&& (lsVehTon.equals(CommonConstant.STR_SPACE_EMPTY)
				|| TitleClientUtilityMethods.isVehTonInvalid(lsVehTon)))
		{
			//leRTSE.addException(new RTSException(201), gettxtTonnage());
			leRTSE.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_VEH_TONNAGE_INVALID),
				gettxtTonnage());
		}
		
		
		if (ivjtxtEmptyWt.isEnabled())
		{
			// defect 10959 
			int liGrossWt = 0;
			// end defect 10959 
			
			if (ivjtxtEmptyWt
					.getText()
					.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				leRTSE.addException(
						new RTSException(
								ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
								gettxtEmptyWt());
			}
			else
			{
				String lsEmptyWt = gettxtEmptyWt().getText().trim();
				int liEmptyWt = Integer.parseInt(lsEmptyWt);
				if (liEmptyWt == 0)
				{
					leRTSE.addException(
							new RTSException(
									ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
									gettxtEmptyWt());
				}
				else
				{
					String lsCarryingCap =
						gettxtCarryingCap().getText().trim();
					// defect 10959 
					liGrossWt =
						CommonValidations.calcGrossWeight(
								lsEmptyWt,
								lsCarryingCap);
					// end defect 10959 
					
					getlblGrossWt().setText(
							Integer.toString(liGrossWt));
					
					// defect 10959 
					//	if (liGrossWt > MAX_GROSS_WT)
					//	{
					//		leRTSE.addException(
					//		//new RTSException(89),
					//		new RTSException(
					//			ErrorsConstant
					//				.ERR_NUM_GROSS_WT_EXCEEDS_MAX),
					//			gettxtEmptyWt());
					//	}
					// end defect 10959 
				}
			}
			if (gettxtCarryingCap().isEnabled())
			{
				String lsCarryingCap = gettxtCarryingCap().getText().trim();
				if (lsCarryingCap.equals(CommonConstant.STR_SPACE_EMPTY))
				{
					leRTSE.addException(
							new RTSException(
									ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
									gettxtCarryingCap());
				}
				else
				{
					int liCarryingCap = Integer.parseInt(lsCarryingCap);
					if (liCarryingCap == 0)
					{
						leRTSE.addException(
								new RTSException(
										ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
										gettxtCarryingCap());
					}
				}
			}
			
			// defect 10959 
			try
			{
				int liRegClassCd = caVehData.getMfVehicleData().getRegData().getRegClassCd(); 
				TitleClientUtilityMethods.validateGrossWtForRegClassCd(
						liRegClassCd, liGrossWt); 
			}
			catch (RTSException aeRTSEx1)
			{
				leRTSE.addException(aeRTSEx1,gettxtEmptyWt()); 
			}
			// end defect 10959 
		}

		if (leRTSE.isValidationError())
		{
			leRTSE.displayError(this);
			leRTSE.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
		// end defect 8414 
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"