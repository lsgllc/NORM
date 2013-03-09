package com.txdot.isd.rts.client.registration.ui;

import java.awt.Dialog;
import java.awt.event.*;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.txdot.isd.rts.client.general.ui.*;
import com
	.txdot
	.isd
	.rts
	.client
	.registration
	.business
	.SubcontractorHelper;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.event.BarCodeListener;

/* 
 * FrmModifySubcontractorRenewalREG009.java
 * 
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		04/17/2002	Global change for startWorking() and 
 * 							doneWorking()
 * N Ting		07/01/2002	set message to beep for	different county
 * 							defect 4329 
 * K Salvi		11/11/2002	made changes to	dataChanged and changed 
 * 							error messages for invalid exp month and 
 * 							issue year.
 * 							defect 5072  
 * K Salvi		11/21/2002 	Corrected input	types for docno,vin, etc to
 * 							not allow spaces
 * 							defect 5072 
 * K Salvi		02/04/2003	Check for doc no to be exactly 17 digits.
 * 							defect 5348  
 * K Salvi		03/10/2003	focus now goes 
 * 							to the first error component if there is an 
 * 							error.
 * 							defect 5748   
 * KHarrell&
 * SGovindappa 	08/30/2002 	Subcon Performance; Use HashSet for
 * 							validation
 * 							defect 4590 
 * RTaylor		10/18/2002  remove cleanUpBarCode method that 
 * 							strips leading zeros in plate number in 
 * 							handleRenwlBarCode(RenewalBarCodeData)
 * 							defect 4848  
 * K Harrell    10/29/2002  Modified record inherits VIN/DocNo 
 * 							of last scan (handleEnter)
 * 							defect 4974 
 * S Govindappa 03/19/2003  Overrided the setVisible(..)
 * 							method to hide the frame instead of disposing
 * 							the frame to prevent the freeze after subcon 
 * 							modify.
 * 							defect 5139 
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Ver 5.2.0
 * K Harrell	08/20/2004	modify VIN field label to upper case.
 *							in VisualComposition 
 *							defect 7470  Ver 5.2.1
 * K Harrell	08/20/2004	Do not cleanup DocNo, VIN, RegPltNo
 *							defect 7473 Ver 5.2.1
 * K Harrell	08/21/2004	Return to REG007 if no datachange on
 *							Enter.
 *							modify handleEnter()
 *							defect 7473 Ver 5.2.1
 * K Harrell	08/23/2004	Disable New Plate appropriately.
 *							modify postShowPlateAndStickerSelected()
 *							defect 7490  Ver 5.2.1
 * K Harrell	08/23/2004	Save entered New Plate No
 *							modify postShowPlateAndStickerSelected(),
 *							stateChanged(), setData()
 *							defect 7489  Ver 5.2.1
 * K Harrell	08/23/2004	Enable New Plate No on Plate&Sticker
 *							when from diskette
 *							modify postShowPlateAndStickerSelected(),
 *							defect 7486  Ver 5.2.1
 * K Harrell	08/23/2004	ClearAllErrors on radio reselection
 *							modify postShowPlateAndStickerSelected(),
 *							defect 7492  Ver 5.2.1
 * J Rue		08/24/2004	Set tabbing order using Visual Composition
 *							defect 7478 Ver 5.2.1
 * K Harrell	08/29/20004	Set mneumonic for New Plate to Alt-N
 *							to match REG009.
 *							defect 7516
 * K Harrell	08/30/2004	Do not pad VIN for ATVS
 *							modify populateDataObject()
 *							defect 7476  Ver 5.2.1
 * K Harrell	08/30/2004	Move the function of the Plt-Stkr-Plt/Stkr
 *							to from stateChange() to actionPerformed()
 *							deprecated stateChange()
 *							modifygetradioPlt(), getradioStkr()
 *							getradioPltStkr(), actionPerformed()
 *							defect 7506 Ver 5.2.1
 * K Harrell	08/31/20004	Set mneumonic for New Plate to Alt-N
 *							to match REG009. (again!)
 *							defect 7516
 * J Rue		08/31/2004	deprecated stateChange()
 *							defect 7506 Ver 5.2.1
 * T Pederson	09/03/2004	In setData(), disable issue date when 
 *							processing data from diskette.
 *							defect 7537 Ver 5.2.1
 * T Pederson	09/10/2004	In handleRenwlBarCode(), if plate type combo
 *							box is enabled, try to default choice to 
 *							match plate type of renewal scanned.
 *							If disabled, blank out plate code.
 *							defect 7543 Ver 5.2.1
 * J Zwiener	09/17/2004	Capture AuditTrailTransid for MV_Func_Trans
 *							modify populateDataObject()
 *							defect 7553 Ver 5.2.1
 * K Harrell	10/11/2004	Modified for setVIN()
 *							modify dataChanged(),populateData(),
 *							setData(),validateEntries()
 *							PCR 34     Ver 5.2.1
 * K Harrell	10/18/2004	Save entered PlateNo upon modification
 *							modify scrValNewPltNo()
 *							defect 7641 Ver 5.2.1
 * K Harrell	10/29/2004	VIN no longer required
 *							deprecate scrValVin()
 *							modify validateEntries()
 *							defect 7674 Ver 5.2.1 Fix 1
 * K Harrell	11/08/2004	Validation of issue date
 *							modify dataChanged(), scrValFee(),
 *							scrValIssueDate()
 *							defect 7656 Ver 5.2.1 Fix 1
 * K Harrell	01/27/2005	Change I->1, O->0 in VIN.
 *							modify focustLost(),gettxtVin() 
 *							defect 7930 Ver 5.2.2 	
 * B Hargrove	03/04/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							add setVisibleRTS()
 *							remove setVisible()
 *							defect 7894 Ver 5.2.3
 * K Harrell	04/28/2005	Java 1.4 Work
 * 							deprecate Data(), handleStkrNo(),
 *  						inventoryInfoChanged(),stateChanged()
 * 							defect 8020 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7899  Ver 5.2.3                 
  * B Hargrove	06/22/2005	Remove unused methods and variables.
 * 							Modify arrow key handling (it is now
 * 							done in ButtonPanel). Add code to handle
 * 							traversal of radio buttons.
 * 							add ciSelctdRadioButton, carrRadioButton[]
 * 							modify initialize(), keyPressed()
 * 							delete implements KeyListener 
 *							defect 7894 Ver 5.2.3
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify handlePlateBarCode(), 
 * 								handleRenwlBarCode(), 
 * 								populatePltDropDown(), 
 * 								populateStkrDropDown(), preSelectCombo()
 * 							defect 8479 Ver 5.2.3 
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing. Set RequestFocus to
 * 							false so that when the hot keys are chossen
 * 							on a radio field the focus does not stay on 
 * 							that radio button.
 * 							remove keyPressed(), carrRadioButton, 
 * 								ciSelctdRadioButton
 * 							modify initialize()
 * 							defect 7894 Ver 5.2.3 
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify focusLost() 
 * 							defect 8902 Ver Exempts
 * T Pederson	10/09/2006	Allow for a fee amount of 0.00.  Add  
 * 							confirmation when amount 0.00. 
 * 							modify confirmMinMaxFee() and scrValFee() 
 * 							defect 8900 Ver Exempts
 * Ray Rowehl	10/05/2006	Modify check for FeeCalcCat = 0 to also 
 * 							check for 4.  For Exempts.
 * 							modify scrValRegClassCd()
 * 							defect 8900 Ver FallAdminTables
 * K Harrell	10/25/2007	Special Plate Processing	
 * 							add ivjstcLblSpclPltTypeCode, 
 * 							 ivjstcLblOrganizationName, ivjchkSpclPlt, 
 * 							 ivjchkAddlSet, ivjcomboOrganizationName, 
 * 							 ivjcomboSpclPltTypeCode, ciSpclPltIndi,
 * 							 ciAddlSetIndi, OMIT_PLPDLR,csOrgNo, 
 * 							 csSpclRegPltCd,cvOrgNo, svSpclPltDesc
 *							add buildSpclPltVector(), 
 *							 getOrgNoFromDropDown(), 
 *							 getSpclRegPltCdFromDropdown(),handleAddlSet(), 
 *							 populateOrganizationNameDropDown(), 
 *							 populateSpclPltDropDown(), 
 *							 preSelectAddlSetChkBox(), resetSpclPlates(), 
 *							 scrValSpclPlt(), scrValSpclPltStkr(), 
 *							 scrValSpclOrgNo() 
 *							modify actionPerformed(), datachanged, 
 *							 getstcLblVin(), getstcLblCurrentPlateNo(), 
 *							 getstcLblExpMo(), getstcLblFee(), 
 *							 getstcLblOrganizationName(), 
 *							 getstcLblSpclPltTypeCode(), 
 *							 getstcLblRegClass(), getstcLblYear(), 
 *							 initialize(), populateDataObject(),
 *							 preSelectCombo(), setData(), 
 *							 scrValRegClassCd(), validateEntries()
 *							defect 9192/9194/9085/9362 Ver Special Plates 2
 * K Harrell	10/31/2007	Throw error if New Exp Mo/Yr less than 6
 * 							 or over 15 months in the future. 
 * 							add cbValidYear
 * 							modify scrValExpMo(), scrValIssueYear() 
 * 							defect 9097 Ver Special Plates 2 
 * B Hargrove	04/16/2008	Checking references to CustBaseRegFee 
 * 							and\or DieselFee which are no longer 
 * 							retrieved from mainframe. (see: defect 9557).
 * 							No changes needed here. 
 * 							defect 9097 Ver POS A 
 * K Harrell	03/15/2010	Validate IssueDate Year: Either prior or 
 * 							current year. 
 * 							add caToday 
 * 							modify scrValIssueDate() 
 * 							defect 10355 Ver POS_640 
 * T Pederson	04/26/2010 	Add plate expiration mo/yr and plate term
 * 							fields to screen. 
 * 							modify populateDataObject(), initialize(),
 * 							actionPerformed(), handleRenwlBarCode(),
 * 							resetSpclPlates(), handleVendorPlate(),
 * 							setData()
 *							defect 10392  Ver POS_640
 * T Pederson	04/30/2010 	Remove ATVS from plate code drop down  
 * 							vector. 
 *							defect 10453  Ver POS_640
 * T Pederson	05/12/2010 	Moved code in setData so that special plate   
 * 							check box will be enabled. Correct null 
 * 							pointer in dataChanged when enter pressed 
 * 							and not a vendor plate.  Validation for 
 * 							entry of plate expiration month referencing 
 * 							wrong variable.  
 * 							modify dataChanged(), setData(), 
 * 							scrValPltExpMo()  
 *							defect 10488  Ver POS_640
 * K Harrell	10/28/2010	add new element to svPltTerm
 * 							modify populatePltTermDropDown() 
 * 							defect 10644 Ver 6.6.0  
 * R Pilon		06/13/2012  Change to only log the exception when attempting to 
 * 							initialize the BarCodeScanner.
 * 							modify setData(Object)
 * 							defect 11071 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/** 
 * Frame for FrmModifySubcontractorRenewalREG009
 * 
 * @version	POS_700			06/13/2012
 * @author	Nancy Ting 
 * <br>Creation Date:		06/26/2001 
 */

public class FrmModifySubcontractorRenewalREG009
	extends RTSDialogBox
	implements
		BarCodeListener,
		ActionListener,
		FocusListener,
		ItemListener,
		WindowListener,
		ChangeListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkPrintStkr = null;
	private JComboBox ivjcomboStickerCode = null;
	private JComboBox ivjcomboPltCode = null;
	private JLabel ivjlblDocno = null;
	private JLabel ivjlblSubcontractor = null;
	private JLabel ivjlblVin = null;
	private JLabel ivjstcLblCurrentPlateNo = null;
	private JLabel ivjstcLblExpMo = null;
	private JLabel ivjstcLblFee = null;
	private JLabel ivjstcLblIssueDate = null;
	private JLabel ivjstcLblNewPltNo = null;
	private JLabel ivjstcLblPltItemCode = null;
	private JLabel ivjstcLblRegClass = null;
	private JLabel ivjstcLblStickerItemCode = null;
	private JLabel ivjstcLblSubcontractorId = null;
	private JLabel ivjstcLblYear = null;
	private JPanel ivjJPanel5 = null;
	private JPanel ivjFrmModifySubcontractorRenewalREG009ContentPane1 =
		null;
	private JRadioButton ivjradioPlt = null;
	private JRadioButton ivjradioPltStkr = null;
	private JRadioButton ivjradioStkr = null;
	private JTextField caFocusRTSInputField = null;

	private RTSInputField ivjtxtCurrentPlateNo = null;
	private RTSInputField ivjtxtExpMo = null;
	private RTSInputField ivjtxtFee = null;
	private RTSDateField ivjtxtIssueDate = null;
	private RTSInputField ivjtxtRegClass = null;
	private RTSInputField ivjtxtYear = null;
	private RTSInputField ivjtxtDocno = null;
	private RTSInputField ivjtxtNewPlateNo = null;
	private RTSInputField ivjtxtVin = null;

	//boolean
	private boolean cbDiffCnty;
	private boolean cbIssueDateValid = false;
	private boolean cbReentry = false;
	private boolean cbWindowOpened = false;

	// defect 9097 
	private boolean cbValidYear = false;
	// end defect 9097  

	// defect 10355 
	private RTSDate caToday =
		new RTSDate(
			RTSDate.YYYYMMDD,
			RTSDate.getCurrentDate().getYYYYMMDDDate());
	// end defect 10355 

	// Objects 	
	private BarCodeScanner caBarCodeScanner;
	private RenewalBarCodeData caRenewalBarCodeData;
	private SubcontractorRenewalData caSubconToBeModified = null;
	protected SubcontractorRenewalCacheData caSubcontractorRenewalCacheData =
		null;

	// String 
	private String csNewPlateNo = "";

	// Vector
	protected static Vector cvPltCd = null;
	protected static Vector cvPltSkrCd = null;

	//Constant
	public static final Dollar BUNDLE_MAX_AMT =
		FrmRegistrationSubcontractorRenewalREG007.BUNDLE_MAX_AMT;
	public static final Dollar MIN_FEE =
		FrmRegistrationSubcontractorRenewalREG007.MIN_FEE;
	public static final Dollar MAX_FEE =
		FrmRegistrationSubcontractorRenewalREG007.MAX_FEE;
	public static final int MAX_BUNDLE_AMT =
		FrmRegistrationSubcontractorRenewalREG007.MAX_BUNDLE_AMT;
	public static final int YR_BEGIN =
		FrmRegistrationSubcontractorRenewalREG007.YR_BEGIN;
	public static final int YR_END =
		FrmRegistrationSubcontractorRenewalREG007.YR_END;
	public static final String STKR_TRCKNG_TYPE =
		FrmRegistrationSubcontractorRenewalREG007.STKR_TRCKNG_TYPE;
	public static final String PLT_TRCKNG_TYPE =
		FrmRegistrationSubcontractorRenewalREG007.PLT_TRCKNG_TYPE;
	public static final int PROCSNG_CD =
		FrmRegistrationSubcontractorRenewalREG007.PROCSNG_CD;
	public static final String DEFAULT_STKR_CD =
		FrmRegistrationSubcontractorRenewalREG007.DEFAULT_STKR_CD;
	public static final String SEPARATOR =
		FrmRegistrationSubcontractorRenewalREG007.SEPARATOR;

	//	defect 9085
	private JLabel ivjstcLblSpclPltTypeCode = null;
	private JLabel ivjstcLblOrganizationName = null;
	private JComboBox ivjcomboSpclPltTypeCode = null;
	private JComboBox ivjcomboOrganizationName = null;
	private JCheckBox ivjchkSpclPlt = null;
	private JCheckBox ivjchkAddlSet = null;

	private boolean cbPrintChecked = false;
	private boolean cbInitialize = true;

	private int ciSpclPltIndi = 0;
	private int ciAddlSetIndi = 0;

	private String csSpclRegPltCd = null;
	private String csOrgNo = null;
	private Vector cvOrgNo = null;

	private static boolean OMIT_PLPDLR = true;

	protected static Vector svSpclPltDesc = new Vector();
	// end defect 9085

	// defect 10392 
	private JLabel ivjstcLblPltTerm = null;
	private JLabel ivjstcLblPltExp = null;
	private JLabel ivjstcLblSlash = null;

	private RTSInputField ivjtxtPltExpMo = null;
	private RTSInputField ivjtxtPltExpYr = null;
	private JComboBox ivjcomboPltTerm = null;
	protected static Vector svPltTerm = null;
	// end defect 10392 

	static {
		//Plate only code
		cvPltCd = new Vector();
		// defect 10453  
		//svPltCd.addElement("ATVS");
		// end defect 10453  
		cvPltCd.addElement("CP");
		cvPltCd.addElement("DRP");
		cvPltCd.addElement("FP");
		cvPltCd.addElement("SCP");
		//most common Plate Code for Plate and Skr
		cvPltSkrCd = new Vector();
		cvPltSkrCd.addElement("PSP");
		cvPltSkrCd.addElement("TKP");
		cvPltSkrCd.addElement("TLP");

		// defect 10392 
		svPltTerm = new Vector();
		svPltTerm.addElement("1");
		svPltTerm.addElement("5");
		svPltTerm.addElement("10");
		// end defect 10392 

		// defect 10644 
		svPltTerm.addElement("25");
		// end defect 10644 

		// defect 9085 
		buildSpclPltVector();
		// end defect 9085  
	}

	private static final String STICKER = "Sticker";
	private static final String PLATE = "Plate";
	private static final String PLATESTICKER = "Plate & Sticker";

	/**
	 * FrmModifySubcontractorRenewalREG009 constructor.
	 */
	public FrmModifySubcontractorRenewalREG009()
	{
		super();
		initialize();
	}
	/**
	 * FrmModifySubcontractorRenewalREG009 constructor.
	 * 
	 * @param aaOwner java.awt.Frame
	 */
	public FrmModifySubcontractorRenewalREG009(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * FrmModifySubcontractorRenewalREG009 constructor comment.
	 * 
	 * @param aaOwner java.awt.Frame
	 */
	public FrmModifySubcontractorRenewalREG009(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Creates the Spcl Plt Vector of Special Plate Descriptions  
	 * to be loaded upon selection of the "Special Plate" checkbox.
	 */
	private static void buildSpclPltVector()
	{
		Vector lvSpclPltTypeCd =
			PlateTypeCache.getVectorOfSpecialPlateTypes(
				new RTSDate().getYYYYMMDDDate(),
				SystemProperty.getOfficeIssuanceCd(),
				SpecialPlatesConstant.REGIS_TYPE_EVENTS,
				OMIT_PLPDLR);

		for (int i = 0; i < lvSpclPltTypeCd.size(); i++)
		{
			PlateTypeData laPltTypeData =
				(PlateTypeData) lvSpclPltTypeCd.get(i);
			String lsRegPltCd = laPltTypeData.getRegPltCd();
			String lsRegPltCdDesc =
				UtilityMethods.addPaddingRight(
					laPltTypeData.getRegPltCdDesc(),
					75,
					" ");
			svSpclPltDesc.add(lsRegPltCdDesc + lsRegPltCd);
		}
		UtilityMethods.sort(svSpclPltDesc);
	}
	/**
	 * Invoked when an Enter/Cancel/Help/Plate Button/
	 * Sticker Button/Plate & Sticker Button
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
				handleEnter();
				return;
			}
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				int liReturnStatus =
					new RTSException(
						RTSException.CTL001,
						"Are you sure that you do NOT want to modify"
							+ " the renewal record?",
						null).displayError(
						this);
				if (liReturnStatus == RTSException.YES)
				{
					handleCancel();
					gettxtYear().requestFocus();
					return;
				}
				else
				{
					gettxtYear().requestFocus();
					return;
				}
			}
			if (aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.REG009B);
			}
			else if (aaAE.getSource() instanceof JRadioButton)
			{
				// end defect 7492
				if (gettxtNewPlateNo().isEnabled())
				{
					csNewPlateNo = gettxtNewPlateNo().getText();
				}
				//PLATE 
				if (aaAE.getSource() == getradioPlt())
				{
					if (getradioPlt().isSelected())
					{
						showPlateAndStickerSelected(true, false);
					}
				}
				else
				{
					//PLATE STICKER
					if (aaAE.getSource() == getradioPltStkr())
					{
						if (getradioPltStkr().isSelected())
						{
							showPlateAndStickerSelected(true, true);

						}
					}
					else
					{
						//STICKER
						if (aaAE.getSource() == getradioStkr()
							&& getradioStkr().isSelected())
						{
							// defect 9085
							getstcLblStickerItemCode().setEnabled(true);
							getstcLblPltItemCode().setEnabled(false);
							getchkSpclPlt().setEnabled(true);
							// end defect 9085 
							showPlateAndStickerSelected(false, true);
						}
					}
				}
				postShowPlateAndStickerSelected();
			}
			// defect 9085
			else if (aaAE.getSource() instanceof JCheckBox)
			{
				if (aaAE.getSource() == getchkSpclPlt())
				{
					if (getchkSpclPlt().isSelected())
					{
						getstcLblSpclPltTypeCode().setEnabled(true);
						getcomboSpclPltTypeCode().setEnabled(true);
						ciSpclPltIndi = 1;
						populateSpclPltDropDown();
						getcomboSpclPltTypeCode().requestFocus();
					}
					else
					{
						resetSpclPlates();
						gettxtIssueDate().requestFocus();
					}
				}
				else if (aaAE.getSource() == getchkAddlSet())
				{
					ciAddlSetIndi =
						(getchkAddlSet().isSelected() ? 1 : 0);
				}
				else if (aaAE.getSource() == getchkPrintStkr())
				{
					if (getchkPrintStkr().isEnabled())
					{
						cbPrintChecked = getchkPrintStkr().isSelected();
					}
				}
			}
			else if (
				!cbInitialize
					&& aaAE.getSource() == getcomboSpclPltTypeCode())
			{
				int liIndex =
					getcomboSpclPltTypeCode().getSelectedIndex();
				if (liIndex != -1)
				{
					getSpclRegPltCdFromDropDown(
						getcomboSpclPltTypeCode().getSelectedIndex());
					handleAddlSet();

					csOrgNo = null;
					populateOrganizationNameDropDown();
					getcomboSpclPltTypeCode().requestFocus();
				}
				// defect 10392 
				handleVendorPlate(null);
				// end defect 10392 
			}
			else if (
				!cbInitialize
					&& aaAE.getSource() == getcomboOrganizationName())
			{
				int liIndex =
					getcomboOrganizationName().getSelectedIndex();
				if (liIndex != -1)
				{
					getOrgNoFromDropDown(
						getcomboOrganizationName().getSelectedIndex());
				}
			}
			// end defect 9085 
		}
		catch (RTSException leException)
		{
			leException.displayError(this);
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Invoked when a barcode is scanned.
	 * 
	 * @param aaBCE BarCodeEvent
	 */
	public void barCodeScanned(
		com.txdot.isd.rts.services.util.event.BarCodeEvent aaBCE)
	{
		Object laBarCodeData = aaBCE.getBarCodeData();
		try
		{
			if (laBarCodeData instanceof RenewalBarCodeData)
			{
				RenewalBarCodeData lRenewalBarCodeData =
					(RenewalBarCodeData) laBarCodeData;
				handleRenwlBarCode(lRenewalBarCodeData);
			}
			else if (laBarCodeData instanceof PlateBarCodeData)
			{
				PlateBarCodeData laPlateBarCodeData =
					(PlateBarCodeData) laBarCodeData;
				handlePlateBarCode(laPlateBarCodeData);
			}
		}
		catch (RTSException leRTSEx)
		{
			if (leRTSEx.getMessage() != null
				&& leRTSEx.getMessage().equals(
					SubcontractorHelper.DUPL_DOC))
			{
				RTSException leEx = new RTSException();
				leEx.addException(leRTSEx, gettxtDocno());
				leEx.displayError(this);
				leEx.getFirstComponent().requestFocus();
			}
			else
			{
				leRTSEx.displayError(this);
			}
			return;
		}
	}
	/**
	 * Check whether the input String contains maximum two decimal
	 * 
	 * @param asString String
	 * @return boolean 
	 */
	private boolean check2Decimals(String asString)
	{
		StringTokenizer laStringTokenizer =
			new StringTokenizer(asString, ".");
		if (laStringTokenizer.countTokens() == 1)
		{
			//no .
			return true;
		}
		else if (laStringTokenizer.countTokens() > 2)
		{
			return false;
		}
		else
		{
			if (asString
				.substring(asString.indexOf('.') + 1)
				.trim()
				.length()
				> 2)
			{
				return false;
			}
		}
		return true;
	}
	/**
	 * Remove leading zeroes in barcode
	 * 
	 * @param asBarCodeStr String
	 * @return String 
	 */
	private String cleanUpBarCode(String asBarCodeStr)
	{
		if (asBarCodeStr != null)
		{
			asBarCodeStr = asBarCodeStr.trim();
			//remove leading zeros
			boolean lbContinueCleanup = true;
			while (lbContinueCleanup)
			{
				if (asBarCodeStr.substring(0, 1).equals("0"))
				{
					if (asBarCodeStr.length() == 1)
					{
						lbContinueCleanup = false;
					}
					else
					{
						asBarCodeStr = asBarCodeStr.substring(1);
					}
				}
				else
				{
					lbContinueCleanup = false;
				}
			}
			return asBarCodeStr;
		}
		else
		{
			return null;
		}
	}
	/**
	 * Confirm the Issue Date
	 * 
	 * @return boolean
	 */
	public boolean confirmIssueDate()
	{
		int liIssueAMDate = gettxtIssueDate().getDate().getAMDate();
		int liCurrentAMDate = new RTSDate().getAMDate();

		//PROC confirm SubconIssueAMDate 90 days old or older
		if (liIssueAMDate < (liCurrentAMDate - 90))
		{
			//only pop up message if not pop up before
			if (caSubconToBeModified.getSubconIssueDate()
				!= gettxtIssueDate().getDate().getYYYYMMDDDate())
			{
				//Confirm Message
				int liReturnStatus =
					new RTSException(
						RTSException.CTL001,
						"Issue Date is over 90 days old,"
							+ " confirm issue date.",
						null).displayError(
						this);
				if (liReturnStatus == RTSException.NO)
				{
					gettxtIssueDate().requestFocus();
					return false;
				}
			}
		}
		return true;
	}
	/**
	 * Confirm the entered fee against minimum and maximum
	 * 
	 * @return boolean
	 */
	public boolean confirmMinMaxFee()
	{
		//Confirm Input Renewal Fee to be between Min Fee and Max Fee
		String lsErrorMsg = null;
		Dollar laFee = new Dollar(gettxtFee().getText());
		// defect 8900
		if (laFee.equals(CommonConstant.ZERO_DOLLAR))
		{
			lsErrorMsg = "Fee Amount is zero.\n";
		}
		// end defect 8900
		else if (laFee.compareTo(MIN_FEE) < 0)
		{
			lsErrorMsg = "Fee Amount is less than " + MIN_FEE + ".\n";
		}
		else if (laFee.compareTo(MAX_FEE) > 0)
		{
			lsErrorMsg = "Fee Amount is more than " + MAX_FEE + ".\n";
		}
		if (lsErrorMsg != null)
		{
			lsErrorMsg += "Is this correct?";
			int liReturn =
				new RTSException(
					RTSException.CTL001,
					lsErrorMsg,
					null).displayError(
					this);
			if (liReturn == RTSException.NO)
			{
				gettxtFee().requestFocus();
				return false;
			}
		}
		return true;
	}
	/**
	 * Check if the original data is modified
	 *  
	 * @return boolean
	 * @throws RTSException
	 */
	private boolean dataChanged() throws RTSException
	{
		//if from disk and not processed,
		//always return data changed to true
		if (!caSubconToBeModified.isProcessed())
		{
			return true;
		}
		//if different plate/sticker combination is selected
		if (getCurrRecordTypeSelect()
			!= caSubconToBeModified.getRecordType())
		{
			return true;
		}
		//Check if print option has changed.
		if (getchkPrintStkr().isSelected()
			!= caSubconToBeModified.isPrint())
		{
			return true;
		}
		//check item code and inv number
		switch (caSubconToBeModified.getRecordType())
		{
			case SubcontractorRenewalCacheData.PLT :
				if (!getCdFromDropDown((String) getcomboPltCode()
					.getSelectedItem())
					.equals(caSubconToBeModified.getPltItmCd()))
				{
					return true;
				}
				if (gettxtNewPlateNo().isEnabled())
				{
					if (!gettxtNewPlateNo()
						.getText()
						.trim()
						.equals(caSubconToBeModified.getNewPltNo()))
					{
						return true;
					}
				}
				break;
			case SubcontractorRenewalCacheData.STKR :
				if (!getCdFromDropDown((String) getcomboStickerCode()
					.getSelectedItem())
					.equals(caSubconToBeModified.getStkrItmCd()))
				{
					return true;
				}
				// defect 9085 
				if (ciSpclPltIndi
					!= caSubconToBeModified.getSpclPltIndi())
				{
					return true;
				}
				if (ciSpclPltIndi == 1)
				{
					if (ciAddlSetIndi
						!= caSubconToBeModified.getAddlSetIndi())
					{
						return true;
					}
					if (!csOrgNo
						.equals(caSubconToBeModified.getOrgNo()))
					{
						return true;
					}
					if (!csSpclRegPltCd
						.equals(
							caSubconToBeModified.getSpclPltRegPltCd()))
					{
						return true;
					}
				}
				break;
				// end defect 9085 
			case SubcontractorRenewalCacheData.PLT_STKR :
				if (!getCdFromDropDown((String) getcomboPltCode()
					.getSelectedItem())
					.equals(caSubconToBeModified.getPltItmCd()))
				{
					return true;
				}
				if (!getCdFromDropDown((String) getcomboStickerCode()
					.getSelectedItem())
					.equals(caSubconToBeModified.getStkrItmCd()))
				{
					return true;
				}
				if (gettxtNewPlateNo().isEnabled())
				{
					if (!gettxtNewPlateNo()
						.getText()
						.trim()
						.equals(caSubconToBeModified.getNewPltNo()))
					{
						return true;
					}
				}
				break;
		}
		//Begin - fix for CQU100005072
		//if (!gettxtYear().getText().trim().equals("") && 
		//Integer.parseInt(gettxtYear().getText().trim()) !=
		// caSubconToBeModified.getNewExpYr()){
		if (!gettxtYear()
			.getText()
			.trim()
			.equals("" + caSubconToBeModified.getNewExpYr()))
		{
			return true;
		}
		//End - fix for CQU100005072
		if (!gettxtDocno()
			.getText()
			.trim()
			.equals(caSubconToBeModified.getDocNo()))
		{
			return true;
		}
		if (!gettxtVin()
			.getText()
			.trim()
			.equals(caSubconToBeModified.getVIN()))
		{
			return true;
		}
		if (!gettxtCurrentPlateNo()
			.getText()
			.trim()
			.equals(caSubconToBeModified.getRegPltNo()))
		{
			return true;
		}
		//Begin - fix for CQU100005072
		//if (!gettxtExpMo().getText().trim().equals("") &&
		// Integer.parseInt(gettxtExpMo().getText().trim()) !=
		// caSubconToBeModified.getRegExpMo()){
		if (!gettxtExpMo()
			.getText()
			.trim()
			.equals("" + caSubconToBeModified.getRegExpMo()))
		{
			return true;
		}
		//if (!gettxtRegClass().getText().trim().equals("") &&
		//Integer.parseInt(gettxtRegClass().getText().trim()) != 
		//Integer.parseInt(caSubconToBeModified.getRegClassCd())){
		if (!gettxtRegClass()
			.getText()
			.trim()
			.equals(new String(caSubconToBeModified.getRegClassCd())))
		{
			return true;
		}
		//if (!gettxtFee().getText().trim().equals("") && 
		//new Dollar(gettxtFee().getText().trim()).compareTo(
		//caSubconToBeModified.getRenwlTotalFees()) != 0){
		if (!gettxtFee()
			.getText()
			.trim()
			.equals(
				caSubconToBeModified.getRenwlTotalFees().toString()))
		{
			return true;
		}
		// defect 10392 
		// defect 10488 
		String lsPltTerm = "";
		if (getcomboPltTerm().isEnabled())
		{
			lsPltTerm = getcomboPltTerm().getSelectedItem().toString();
		}
		if (!lsPltTerm
			.equals("" + caSubconToBeModified.getPltVldtyTerm()))
			// end defect 10488 
		{
			return true;
		}
		// defect 10488 
		String lsPltExpMo = "";
		if (gettxtPltExpMo().isEnabled())
		{
			lsPltExpMo = gettxtPltExpMo().getText().trim();
		}
		if (!lsPltExpMo
			.equals("" + caSubconToBeModified.getPltExpMo()))
			// end defect 10488 
		{
			return true;
		}
		// defect 10488 
		String lsPltExpYr = "";
		if (gettxtPltExpYr().isEnabled())
		{
			lsPltExpYr = gettxtPltExpYr().getText().trim();
		}
		// end defect 10488 
		if (!lsPltExpYr
			.equals("" + caSubconToBeModified.getPltExpYr()))
		{
			return true;
		}
		// end defect 10392 

		// defect 7656
		// validate issue date
		if (!gettxtIssueDate().isValidDate())
		{
			cbIssueDateValid = false;
			return true;
		}
		else
		{
			if (gettxtIssueDate().getDate().getYYYYMMDDDate()
				!= caSubconToBeModified.getSubconIssueDate())
			{
				return true;
			}
		}
		// end defect 7656 

		return false;
	}
	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param FocusEvent aaFE
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		// empty code block
	}
	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param FocusEvent aaFE
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		if (!aaFE.isTemporary())
		{
			// defect 7930
			// Change I->1, O->0 in VIN 
			if (aaFE.getSource() == gettxtVin())
			{
				String lsVin =
					gettxtVin().getText().trim().toUpperCase();
				// defect 8902
				lsVin =
					CommonValidations.convert_i_and_o_to_1_and_0(lsVin);

				// end defect 8902
				gettxtVin().setText(lsVin);
			}
			// end defect 7930 
			if (aaFE.getSource() instanceof JTextField)
			{
				caFocusRTSInputField = (JTextField) aaFE.getSource();
			}
		}
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
				ivjButtonPanel1.setBounds(170, 284, 248, 38);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjButtonPanel1;
	}
	/**
	 * Get the item code from the selected item in the combo box
	 * 
	 * @param asDropDownValue String
	 * @return String 
	 * @throws RTSException
	 */
	private String getCdFromDropDown(String asDropDownValue)
		throws RTSException
	{
		int liSepIndex = asDropDownValue.indexOf(SEPARATOR);
		if (liSepIndex != -1)
		{
			return asDropDownValue.substring(0, liSepIndex);
		}
		else
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				SEPARATOR + " not present in Drop down",
				"Error!");
		}
	}
	/**
	 * Return the chkPrintStkr property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkPrintStkr()
	{
		if (ivjchkPrintStkr == null)
		{
			try
			{
				ivjchkPrintStkr = new JCheckBox();
				ivjchkPrintStkr.setName("chkPrintStkr");
				ivjchkPrintStkr.setMnemonic('r');
				ivjchkPrintStkr.setText("Print");
				ivjchkPrintStkr.setBounds(508, 206, 56, 22);
				// user code begin {1}
				ivjchkPrintStkr.addActionListener(this);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjchkPrintStkr;
	}
	/**
	 * Return the comboPltCode property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboPltCode()
	{
		if (ivjcomboPltCode == null)
		{
			try
			{
				ivjcomboPltCode = new JComboBox();
				ivjcomboPltCode.setSize(229, 23);
				ivjcomboPltCode.setName("comboPltCode");
				ivjcomboPltCode.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboPltCode.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboPltCode.setBackground(java.awt.Color.white);
				ivjcomboPltCode.setLocation(13, 71);

				// defect 7894
				// remove key listener
				//ivjcomboPltCode.addKeyListener(this);
				// end defect 7894
				ivjcomboPltCode.addItemListener(this);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjcomboPltCode;
	}
	/**
	 * This method initializes ivjcomboPltTerm
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getcomboPltTerm()
	{
		if (ivjcomboPltTerm == null)
		{
			ivjcomboPltTerm = new javax.swing.JComboBox();
			ivjcomboPltTerm.setBounds(212, 213, 42, 25);
		}
		return ivjcomboPltTerm;
	}

	/**
	 * Return the comboStickerCode property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboStickerCode()
	{
		if (ivjcomboStickerCode == null)
		{
			try
			{
				ivjcomboStickerCode = new JComboBox();
				ivjcomboStickerCode.setSize(229, 23);
				ivjcomboStickerCode.setName("comboStickerCode");
				ivjcomboStickerCode.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboStickerCode.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboStickerCode.setBackground(java.awt.Color.white);
				ivjcomboStickerCode.setLocation(261, 71);

				// defect 7894
				// remove key listener
				//ivjcomboStickerCode.addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjcomboStickerCode;
	}
	/**
	 * Return Current Record Type Selection 
	 * 
	 * @return int
	 */
	private int getCurrRecordTypeSelect()
	{
		if (getcomboPltCode().isEnabled()
			&& !getcomboStickerCode().isEnabled())
		{
			return SubcontractorRenewalCacheData.PLT;
		}
		if (!getcomboPltCode().isEnabled()
			&& getcomboStickerCode().isEnabled())
		{
			return SubcontractorRenewalCacheData.STKR;
		}
		return SubcontractorRenewalCacheData.PLT_STKR;
	}

	/**
	 * Return the FrmModifySubcontractorRenewalREG009ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmModifySubcontractorRenewalREG009ContentPane1()
	{
		if (ivjFrmModifySubcontractorRenewalREG009ContentPane1 == null)
		{
			try
			{
				ivjFrmModifySubcontractorRenewalREG009ContentPane1 =
					new JPanel();
				ivjFrmModifySubcontractorRenewalREG009ContentPane1
					.setName(
					"FrmModifySubcontractorRenewalREG009ContentPane1");
				ivjFrmModifySubcontractorRenewalREG009ContentPane1
					.setLayout(
					null);
				ivjFrmModifySubcontractorRenewalREG009ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmModifySubcontractorRenewalREG009ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(600, 300));
				ivjFrmModifySubcontractorRenewalREG009ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				getFrmModifySubcontractorRenewalREG009ContentPane1()
					.add(
					getJPanel5(),
					getJPanel5().getName());
				getFrmModifySubcontractorRenewalREG009ContentPane1()
					.add(
					getstcLblSubcontractorId(),
					getstcLblSubcontractorId().getName());
				getFrmModifySubcontractorRenewalREG009ContentPane1()
					.add(
					getlblSubcontractor(),
					getlblSubcontractor().getName());
				getFrmModifySubcontractorRenewalREG009ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjFrmModifySubcontractorRenewalREG009ContentPane1;
	}
	/**
	 * Get the full item description of the item code from Cache
	 * 
	 * @param asCd String
	 * @return String 
	 * @throws RTSException
	 */
	private String getFullDescFromCd(String asCd) throws RTSException
	{
		ItemCodesData laItemCodesData = ItemCodesCache.getItmCd(asCd);
		if (laItemCodesData == null)
		{
			throw new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Item Code not found: " + asCd,
				"ERROR");
		}
		return asCd + SEPARATOR + laItemCodesData.getItmCdDesc();
	}
	/**
	 * Return the JPanel5 property value.
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
				ivjJPanel5.setName("JPanel5");
				ivjJPanel5.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						"Add a Renewal:"));
				ivjJPanel5.setLayout(null);
				ivjJPanel5.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel5.setMinimumSize(
					new java.awt.Dimension(563, 55));
				ivjJPanel5.setBounds(13, 35, 569, 248);
				getJPanel5().add(
					getradioPlt(),
					getradioPlt().getName());
				getJPanel5().add(
					getradioStkr(),
					getradioStkr().getName());
				getJPanel5().add(
					getradioPltStkr(),
					getradioPltStkr().getName());
				getJPanel5().add(
					getstcLblPltItemCode(),
					getstcLblPltItemCode().getName());
				getJPanel5().add(
					getstcLblStickerItemCode(),
					getstcLblStickerItemCode().getName());
				getJPanel5().add(
					getstcLblYear(),
					getstcLblYear().getName());
				getJPanel5().add(
					getcomboPltCode(),
					getcomboPltCode().getName());
				getJPanel5().add(
					getcomboStickerCode(),
					getcomboStickerCode().getName());
				getJPanel5().add(gettxtYear(), gettxtYear().getName());
				getJPanel5().add(
					getstcLblIssueDate(),
					getstcLblIssueDate().getName());
				getJPanel5().add(
					getstcLblVin(),
					getstcLblVin().getName());
				getJPanel5().add(
					getstcLblDocno(),
					getstcLblDocno().getName());
				getJPanel5().add(
					gettxtIssueDate(),
					gettxtIssueDate().getName());
				getJPanel5().add(
					gettxtDocno(),
					gettxtDocno().getName());
				getJPanel5().add(gettxtVin(), gettxtVin().getName());
				getJPanel5().add(
					getstcLblNewPltNo(),
					getstcLblNewPltNo().getName());
				getJPanel5().add(
					getstcLblCurrentPlateNo(),
					getstcLblCurrentPlateNo().getName());
				getJPanel5().add(
					getstcLblExpMo(),
					getstcLblExpMo().getName());
				getJPanel5().add(
					getstcLblRegClass(),
					getstcLblRegClass().getName());
				getJPanel5().add(
					getstcLblFee(),
					getstcLblFee().getName());
				getJPanel5().add(
					gettxtNewPlateNo(),
					gettxtNewPlateNo().getName());
				getJPanel5().add(
					gettxtCurrentPlateNo(),
					gettxtCurrentPlateNo().getName());
				getJPanel5().add(
					gettxtExpMo(),
					gettxtExpMo().getName());
				getJPanel5().add(
					gettxtRegClass(),
					gettxtRegClass().getName());
				getJPanel5().add(gettxtFee(), gettxtFee().getName());
				getJPanel5().add(
					getchkPrintStkr(),
					getchkPrintStkr().getName());
				// user code begin {1}
				ivjJPanel5.add(getstcLblSpclPltTypeCode(), null);
				ivjJPanel5.add(getstcLblOrganizationName(), null);
				ivjJPanel5.add(getcomboSpclPltTypeCode(), null);
				ivjJPanel5.add(getcomboOrganizationName(), null);
				ivjJPanel5.add(getchkSpclPlt(), null);
				ivjJPanel5.add(getchkAddlSet(), null);
				// user code end
				ivjJPanel5.add(getstcLblPltExp(), null);
				ivjJPanel5.add(getstcLblPltTerm(), null);
				ivjJPanel5.add(getcomboPltTerm(), null);
				ivjJPanel5.add(gettxtPltExpMo(), null);
				ivjJPanel5.add(gettxtPltExpYr(), null);
				ivjJPanel5.add(getstcLblSlash(), null);
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjJPanel5;
	}
	/**
	 * Return the lblDocno property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDocno()
	{
		if (ivjlblDocno == null)
		{
			try
			{
				ivjlblDocno = new JLabel();
				ivjlblDocno.setName("lblDocno");
				ivjlblDocno.setText("Doc No");
				ivjlblDocno.setBounds(113, 145, 45, 21);
				ivjlblDocno.setHorizontalAlignment(SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjlblDocno.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_D);
				ivjlblDocno.setLabelFor(gettxtDocno());
				// end defect 9085 
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjlblDocno;
	}
	/**
	 * Return the lblSubcontractor property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblSubcontractor()
	{
		if (ivjlblSubcontractor == null)
		{
			try
			{
				ivjlblSubcontractor = new JLabel();
				ivjlblSubcontractor.setSize(447, 18);
				ivjlblSubcontractor.setName("lblSubcontractor");
				ivjlblSubcontractor.setText(
					"001   Education Credit Union - Rev");
				ivjlblSubcontractor.setMaximumSize(
					new java.awt.Dimension(159, 14));
				ivjlblSubcontractor.setMinimumSize(
					new java.awt.Dimension(159, 14));
				ivjlblSubcontractor.setLocation(119, 12);

				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjlblSubcontractor;
	}
	/**
	 * Return the lblVin property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVin()
	{
		if (ivjlblVin == null)
		{
			try
			{
				ivjlblVin = new JLabel();
				ivjlblVin.setName("lblVin");
				ivjlblVin.setText(" VIN");
				ivjlblVin.setBounds(261, 145, 25, 21);
				ivjlblVin.setHorizontalAlignment(SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjlblVin.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_V);
				ivjlblVin.setLabelFor(gettxtVin());
				// end defect 9085 
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjlblVin;
	}
	/**
	 * Get the new plate text field
	 *
	 * @return RTSInputField
	 */
	public RTSInputField getNewPlt()
	{
		return gettxtNewPlateNo();
	}
	/**
	 * Return the radioPlt property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioPlt()
	{
		if (ivjradioPlt == null)
		{
			try
			{
				ivjradioPlt = new JRadioButton();
				ivjradioPlt.setSize(64, 22);
				ivjradioPlt.setName("radioPlt");
				ivjradioPlt.setMnemonic(KeyEvent.VK_P);
				ivjradioPlt.setText(PLATE);
				ivjradioPlt.setLocation(13, 19);

				ivjradioPlt.addActionListener(this);
				// defect 7894
				// remove key listener
				//ivjradioPlt.addKeyListener(this);
				// end defect 7894
				//ivjradioPlt.addChangeListener(this);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjradioPlt;
	}
	/**
	 * Return the radioPltStkr property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioPltStkr()
	{
		if (ivjradioPltStkr == null)
		{
			try
			{
				ivjradioPltStkr = new JRadioButton();
				ivjradioPltStkr.setName("radioPltStkr");
				ivjradioPltStkr.setMnemonic(KeyEvent.VK_T);
				ivjradioPltStkr.setText(PLATESTICKER);
				ivjradioPltStkr.setBounds(150, 19, 119, 22);
				// user code begin {1}
				ivjradioPltStkr.addActionListener(this);
				// defect 7894
				// remove key listener
				//ivjradioPltStkr.addKeyListener(this);
				// end defect 7894
				//ivjradioPltStkr.addChangeListener(this);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjradioPltStkr;
	}
	/**
	 * Return the radioStkr property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioStkr()
	{
		if (ivjradioStkr == null)
		{
			try
			{
				ivjradioStkr = new JRadioButton();
				ivjradioStkr.setName("radioStkr");
				ivjradioStkr.setMnemonic(KeyEvent.VK_S);
				ivjradioStkr.setText(STICKER);
				ivjradioStkr.setBounds(81, 19, 72, 22);
				// user code begin {1}
				ivjradioStkr.addActionListener(this);
				// defect 7894
				// remove key listener
				//ivjradioStkr.addKeyListener(this);
				// end defect 7894
				//ivjradioStkr.addChangeListener(this);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjradioStkr;
	}
	/**
	 * Get the item from the value in the combo box
	 * 
	 * @param aiIndex
	 */
	private void getSpclRegPltCdFromDropDown(int aiIndex)
	{
		String lsSpclRegPltCd =
			(String) svSpclPltDesc.elementAt(aiIndex);
		csSpclRegPltCd = lsSpclRegPltCd.substring(50).trim();
	}
	/**
	 * Get the item from the value in the combo box
	 * 
	 * @param aiIndex
	 * @return  
	 */
	private void getOrgNoFromDropDown(int aiIndex)
	{
		String lsOrgNo = (String) cvOrgNo.elementAt(aiIndex);
		csOrgNo = lsOrgNo.substring(50).trim();
	}
	/**
	 * Get the renewal fee
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField getRenwlFee()
	{
		return gettxtFee();
	}
	/**
	 * Return the stcLblCurrentPlateNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCurrentPlateNo()
	{
		if (ivjstcLblCurrentPlateNo == null)
		{
			try
			{
				ivjstcLblCurrentPlateNo = new JLabel();
				ivjstcLblCurrentPlateNo.setName("stcLblCurrentPlateNo");
				ivjstcLblCurrentPlateNo.setText("Curr Plt");
				ivjstcLblCurrentPlateNo.setMaximumSize(
					new java.awt.Dimension(93, 14));
				ivjstcLblCurrentPlateNo.setMinimumSize(
					new java.awt.Dimension(93, 14));
				ivjstcLblCurrentPlateNo.setBounds(113, 189, 47, 21);
				ivjstcLblCurrentPlateNo.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjstcLblCurrentPlateNo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_U);
				ivjstcLblCurrentPlateNo.setLabelFor(
					gettxtCurrentPlateNo());
				// end defect 9085 
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblCurrentPlateNo;
	}
	/**
	 * Return the stcLblExpMo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblExpMo()
	{
		if (ivjstcLblExpMo == null)
		{
			try
			{
				ivjstcLblExpMo = new JLabel();
				ivjstcLblExpMo.setName("stcLblExpMo");
				ivjstcLblExpMo.setText("Exp Mo");
				ivjstcLblExpMo.setMaximumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblExpMo.setMinimumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblExpMo.setBounds(351, 189, 46, 21);
				ivjstcLblExpMo.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjstcLblExpMo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_X);
				ivjstcLblExpMo.setLabelFor(gettxtExpMo());
				// end defect 9085 
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblExpMo;
	}
	/**
	 * Return the stcLblFee property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblFee()
	{
		if (ivjstcLblFee == null)
		{
			try
			{
				ivjstcLblFee = new JLabel();
				ivjstcLblFee.setName("stcLblFee");
				ivjstcLblFee.setText("Fee");
				ivjstcLblFee.setMaximumSize(
					new java.awt.Dimension(20, 14));
				ivjstcLblFee.setMinimumSize(
					new java.awt.Dimension(20, 14));
				ivjstcLblFee.setBounds(451, 189, 31, 21);
				ivjstcLblFee.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjstcLblFee.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_F);
				ivjstcLblFee.setLabelFor(gettxtFee());
				// end defect 9085 
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblFee;
	}
	/**
	 * Return the stcLblIssueDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblIssueDate()
	{
		if (ivjstcLblIssueDate == null)
		{
			try
			{
				ivjstcLblIssueDate = new JLabel();
				ivjstcLblIssueDate.setName("stcLblIssueDate");
				ivjstcLblIssueDate.setDisplayedMnemonic(73);
				ivjstcLblIssueDate.setText("Issue Date");
				ivjstcLblIssueDate.setMaximumSize(
					new java.awt.Dimension(60, 14));
				ivjstcLblIssueDate.setLabelFor(gettxtIssueDate());
				ivjstcLblIssueDate.setBounds(13, 145, 68, 21);
				ivjstcLblIssueDate.setMinimumSize(
					new java.awt.Dimension(60, 14));
				ivjstcLblIssueDate.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblIssueDate;
	}
	/**
	 * Return the stcLblNewPltNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblNewPltNo()
	{
		if (ivjstcLblNewPltNo == null)
		{
			try
			{
				ivjstcLblNewPltNo = new JLabel();
				ivjstcLblNewPltNo.setName("stcLblNewPltNo");
				ivjstcLblNewPltNo.setText("New Plate");
				ivjstcLblNewPltNo.setMaximumSize(
					new java.awt.Dimension(59, 14));
				ivjstcLblNewPltNo.setBounds(13, 189, 63, 21);

				ivjstcLblNewPltNo.setMinimumSize(
					new java.awt.Dimension(59, 14));
				ivjstcLblNewPltNo.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				ivjstcLblNewPltNo.setLabelFor(gettxtNewPlateNo());
				ivjstcLblNewPltNo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_N);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblNewPltNo;
	}
	/**
	 * Return the stcLblPltItemCode property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPltItemCode()
	{
		if (ivjstcLblPltItemCode == null)
		{
			try
			{
				ivjstcLblPltItemCode = new JLabel();
				ivjstcLblPltItemCode.setSize(198, 21);
				ivjstcLblPltItemCode.setName("stcLblPltItemCode");
				ivjstcLblPltItemCode.setText(
					"Plate Item Code - Description");
				ivjstcLblPltItemCode.setMaximumSize(
					new java.awt.Dimension(176, 14));
				ivjstcLblPltItemCode.setMinimumSize(
					new java.awt.Dimension(176, 14));

				ivjstcLblPltItemCode.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				ivjstcLblPltItemCode.setLocation(13, 49);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblPltItemCode;
	}
	/**
	 * Return the stcLblPltExp property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPltExp()
	{
		if (ivjstcLblPltExp == null)
		{
			try
			{
				ivjstcLblPltExp = new JLabel();
				ivjstcLblPltExp.setBounds(264, 189, 86, 21);
				ivjstcLblPltExp.setName("stcLblPltExp");
				ivjstcLblPltExp.setText("Curr Plate Exp");
				ivjstcLblPltExp.setMaximumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblPltExp.setMinimumSize(
					new java.awt.Dimension(26, 14));
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblPltExp;
	}
	/**
	 * Return the stcLblPltTerm property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPltTerm()
	{
		if (ivjstcLblPltTerm == null)
		{
			try
			{
				ivjstcLblPltTerm = new JLabel();
				ivjstcLblPltTerm.setBounds(205, 189, 53, 21);
				ivjstcLblPltTerm.setName("stcLblPltTerm");
				ivjstcLblPltTerm.setText("Plt Term");
				ivjstcLblPltTerm.setMaximumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblPltTerm.setMinimumSize(
					new java.awt.Dimension(26, 14));
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblPltTerm;
	}
	/**
	 * Return the stcLblRegClass property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblRegClass()
	{
		if (ivjstcLblRegClass == null)
		{
			try
			{
				ivjstcLblRegClass = new JLabel();
				ivjstcLblRegClass.setName("stcLblRegClass");
				ivjstcLblRegClass.setText("Reg Cl");
				ivjstcLblRegClass.setMaximumSize(
					new java.awt.Dimension(57, 14));
				ivjstcLblRegClass.setMinimumSize(
					new java.awt.Dimension(57, 14));
				ivjstcLblRegClass.setBounds(400, 189, 46, 21);
				ivjstcLblRegClass.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjstcLblRegClass.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_G);
				ivjstcLblRegClass.setLabelFor(gettxtRegClass());
				// end defect 9085 
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblRegClass;
	}
	/**
	 * Return the stcLblSlash property value.
	 * 
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblSlash()
	{
		if (ivjstcLblSlash == null)
		{
			try
			{
				ivjstcLblSlash = new javax.swing.JLabel();
				ivjstcLblSlash.setBounds(302, 212, 8, 21);
				ivjstcLblSlash.setName("stcLblSlash");
				ivjstcLblSlash.setText("/");
				ivjstcLblSlash.setMaximumSize(
					new java.awt.Dimension(20, 14));
				ivjstcLblSlash.setMinimumSize(
					new java.awt.Dimension(20, 14));
				ivjstcLblSlash.setHorizontalAlignment(
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
		return ivjstcLblSlash;
	}

	/**
	 * Return the stcLblStickerItemCode property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblStickerItemCode()
	{
		if (ivjstcLblStickerItemCode == null)
		{
			try
			{
				ivjstcLblStickerItemCode = new JLabel();
				ivjstcLblStickerItemCode.setSize(190, 21);
				ivjstcLblStickerItemCode.setName(
					"stcLblStickerItemCode");
				ivjstcLblStickerItemCode.setText(
					"Sticker Item Code - Description");
				ivjstcLblStickerItemCode.setMaximumSize(
					new java.awt.Dimension(176, 14));
				ivjstcLblStickerItemCode.setMinimumSize(
					new java.awt.Dimension(176, 14));

				ivjstcLblStickerItemCode.setHorizontalAlignment(
					SwingConstants.LEFT);
				// user code begin {1}
				ivjstcLblStickerItemCode.setLocation(261, 49);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblStickerItemCode;
	}
	/**
	 * Return the stcLblSubcontractorId property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblSubcontractorId()
	{
		if (ivjstcLblSubcontractorId == null)
		{
			try
			{
				ivjstcLblSubcontractorId = new JLabel();
				ivjstcLblSubcontractorId.setSize(101, 18);
				ivjstcLblSubcontractorId.setName(
					"stcLblSubcontractorId");
				ivjstcLblSubcontractorId.setText("Subcontractor Id:");
				ivjstcLblSubcontractorId.setMaximumSize(
					new java.awt.Dimension(98, 14));
				ivjstcLblSubcontractorId.setMinimumSize(
					new java.awt.Dimension(98, 14));
				ivjstcLblSubcontractorId.setHorizontalAlignment(4);
				ivjstcLblSubcontractorId.setLocation(8, 12);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblSubcontractorId;
	}
	/**
	 * Return the stcLblYear property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblYear()
	{
		if (ivjstcLblYear == null)
		{
			try
			{
				ivjstcLblYear = new JLabel();
				ivjstcLblYear.setSize(39, 21);
				ivjstcLblYear.setName("stcLblYear");
				ivjstcLblYear.setText(" Year");
				ivjstcLblYear.setMaximumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblYear.setMinimumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblYear.setLocation(505, 49);
				// user code begin
				// defect 9085 
				ivjstcLblYear.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_Y);
				ivjstcLblYear.setLabelFor(gettxtYear());
				// end defect 9085 
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjstcLblYear;
	}
	/**
	 * Return the txtCurrentPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtCurrentPlateNo()
	{
		if (ivjtxtCurrentPlateNo == null)
		{
			try
			{
				ivjtxtCurrentPlateNo = new RTSInputField();
				ivjtxtCurrentPlateNo.setName("txtCurrentPlateNo");
				ivjtxtCurrentPlateNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCurrentPlateNo.setText("QQQQOOO");
				ivjtxtCurrentPlateNo.setInput(6);
				ivjtxtCurrentPlateNo.setBounds(113, 214, 70, 20);
				ivjtxtCurrentPlateNo.setMaxLength(7);
				ivjtxtCurrentPlateNo.setHorizontalAlignment(
					JTextField.RIGHT);
				// user code begin {1}
				ivjtxtCurrentPlateNo.addFocusListener(this);
				// defect 7894
				// remove key listener
				//ivjtxtCurrentPlateNo.addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtCurrentPlateNo;
	}
	/**
	 * Return the txtDocno property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtDocno()
	{
		if (ivjtxtDocno == null)
		{
			try
			{
				ivjtxtDocno = new RTSInputField();
				ivjtxtDocno.setName("txtDocno");
				ivjtxtDocno.setInput(1);
				ivjtxtDocno.setText("");
				ivjtxtDocno.setBounds(114, 168, 127, 20);
				ivjtxtDocno.setMaxLength(17);
				// user code begin {1}
				ivjtxtDocno.setHorizontalAlignment(JTextField.RIGHT);

				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtDocno;
	}
	/**
	 * Return the txtExpMo property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtExpMo()
	{
		if (ivjtxtExpMo == null)
		{
			try
			{
				ivjtxtExpMo = new RTSInputField();
				ivjtxtExpMo.setName("txtExpMo");
				ivjtxtExpMo.setInput(1);
				ivjtxtExpMo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtExpMo.setBounds(360, 214, 30, 20);
				ivjtxtExpMo.setMaxLength(2);
				ivjtxtExpMo.setHorizontalAlignment(JTextField.RIGHT);

				// user code begin {1}
				ivjtxtExpMo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtExpMo.addFocusListener(this);
				// defect 7894
				// remove key listener
				//ivjtxtExpMo.addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtExpMo;
	}
	/**
	 * Return the txtFee property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtFee()
	{
		if (ivjtxtFee == null)
		{
			try
			{
				ivjtxtFee = new RTSInputField();
				ivjtxtFee.setName("txtFee");
				ivjtxtFee.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtFee.setText("9999.99");
				ivjtxtFee.setInput(5);
				ivjtxtFee.setBounds(453, 214, 53, 20);
				ivjtxtFee.setMaxLength(7);
				ivjtxtFee.setHorizontalAlignment(JTextField.RIGHT);

				// user code begin {1}
				ivjtxtFee.setInput(RTSInputField.DOLLAR_ONLY);
				ivjtxtFee.addFocusListener(this);
				// defect 7894
				// remove key listener
				//ivjtxtFee.addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtFee;
	}
	/**
	 * Return the txtIssueDate property value.
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSDateField gettxtIssueDate()
	{
		if (ivjtxtIssueDate == null)
		{
			try
			{
				ivjtxtIssueDate = new RTSDateField();
				ivjtxtIssueDate.setName("txtIssueDate");
				ivjtxtIssueDate.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtIssueDate.setText("  /  /    ");
				ivjtxtIssueDate.setBounds(13, 168, 67, 20);
				ivjtxtIssueDate.setColumns(10);
				// user code begin {1}
				ivjtxtIssueDate.addFocusListener(this);
				// defect 7894
				// remove key listener
				//ivjtxtIssueDate.addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtIssueDate;
	}
	/**
	 * Return the txtNewPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtNewPlateNo()
	{
		if (ivjtxtNewPlateNo == null)
		{
			try
			{
				ivjtxtNewPlateNo = new RTSInputField();
				ivjtxtNewPlateNo.setName("txtNewPlateNo");
				ivjtxtNewPlateNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtNewPlateNo.setText("QQQQOOO");
				ivjtxtNewPlateNo.setInput(6);
				ivjtxtNewPlateNo.setBounds(13, 214, 70, 20);
				ivjtxtNewPlateNo.setMaxLength(7);
				ivjtxtNewPlateNo.setHorizontalAlignment(
					JTextField.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtNewPlateNo;
	}
	/**
	 * Return the txtPltExpMo property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtPltExpMo()
	{
		if (ivjtxtPltExpMo == null)
		{
			try
			{
				ivjtxtPltExpMo = new RTSInputField();
				ivjtxtPltExpMo.setBounds(270, 214, 30, 20);
				ivjtxtPltExpMo.setName("txtPltExpMo");
				ivjtxtPltExpMo.setInput(1);
				ivjtxtPltExpMo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPltExpMo.setMaxLength(2);
				ivjtxtPltExpMo.setHorizontalAlignment(JTextField.RIGHT);

				// user code begin {1}
				ivjtxtPltExpMo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtPltExpMo.addFocusListener(this);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtPltExpMo;
	}
	/**
	 * Return the txtPltExpYr property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtPltExpYr()
	{
		if (ivjtxtPltExpYr == null)
		{
			try
			{
				ivjtxtPltExpYr = new RTSInputField();
				ivjtxtPltExpYr.setBounds(307, 214, 46, 20);
				ivjtxtPltExpYr.setName("txtPltExpYr");
				ivjtxtPltExpYr.setInput(1);
				ivjtxtPltExpYr.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPltExpYr.setMaxLength(4);
				ivjtxtPltExpYr.setHorizontalAlignment(JTextField.RIGHT);

				// user code begin {1}
				ivjtxtPltExpYr.addFocusListener(this);
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtPltExpYr;
	}
	/**
	 * Return the txtRegClass property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtRegClass()
	{
		if (ivjtxtRegClass == null)
		{
			try
			{
				ivjtxtRegClass = new RTSInputField();
				ivjtxtRegClass.setName("txtRegClass");
				ivjtxtRegClass.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtRegClass.setText("999");
				ivjtxtRegClass.setInput(1);
				ivjtxtRegClass.setBounds(407, 214, 29, 20);
				ivjtxtRegClass.setMaxLength(3);
				ivjtxtRegClass.setHorizontalAlignment(JTextField.RIGHT);

				// user code begin {1}
				ivjtxtRegClass.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtRegClass.addFocusListener(this);
				// defect 7894
				// remove key listener
				//ivjtxtRegClass.addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtRegClass;
	}
	/**
	 * Return the txtVin property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVin()
	{
		if (ivjtxtVin == null)
		{
			try
			{
				ivjtxtVin = new RTSInputField();
				ivjtxtVin.setName("txtVin");
				ivjtxtVin.setInput(6);
				ivjtxtVin.setText("");
				ivjtxtVin.setBounds(261, 168, 227, 20);
				ivjtxtVin.setMaxLength(22);
				// user code begin {1}
				ivjtxtVin.setHorizontalAlignment(JTextField.RIGHT);

				// defect 7930
				// For VIN modification 
				ivjtxtVin.addFocusListener(this);
				// end defect 7930 
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtVin;
	}
	/**
	 * Return the txtYear property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtYear()
	{
		if (ivjtxtYear == null)
		{
			try
			{
				ivjtxtYear = new RTSInputField();
				ivjtxtYear.setName("txtYear");
				ivjtxtYear.setInput(1);
				ivjtxtYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtYear.setBounds(505, 71, 46, 23);
				ivjtxtYear.setMaxLength(4);
				ivjtxtYear.setHorizontalAlignment(JTextField.RIGHT);

				// user code begin {1}
				ivjtxtYear.addFocusListener(this);
				// defect 7894
				// remove key listener
				//ivjtxtYear.addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (Throwable leIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(leIvjExc);
			}
		}
		return ivjtxtYear;
	}
	/**
	 * Handling the selection and enabling/disabling of the Additional
	 * checkbox.
	 *
	 */
	private void handleAddlSet()
	{
		PlateTypeData laPTData =
			PlateTypeCache.getPlateType(csSpclRegPltCd);

		switch (laPTData.getPltSetImprtnceCd())
		{
			case 0 :
				{
					getchkAddlSet().setEnabled(false);
					getchkAddlSet().setSelected(false);
					ciAddlSetIndi = 0;
					break;
				}
			case 1 :
				{
					getchkAddlSet().setEnabled(false);
					getchkAddlSet().setSelected(false);
					ciAddlSetIndi = 0;
					break;
				}
			case 2 :
				{
					getchkAddlSet().setEnabled(false);
					getchkAddlSet().setSelected(true);
					ciAddlSetIndi = 1;
					break;
				}
			case 3 :
				{
					getchkAddlSet().setEnabled(true);
					getchkAddlSet().setSelected(false);
					break;
				}
		}
	}
	/**
	 * Handle the cancel event
	 * 
	 */
	private void handleCancel()
	{
		caSubcontractorRenewalCacheData.setRecordModified(null);
		caSubcontractorRenewalCacheData.setRecordTobeModified(null);
		caSubcontractorRenewalCacheData.setModified(false);
		getController().processData(
			AbstractViewController.CANCEL,
			caSubcontractorRenewalCacheData);
	}
	/**
	 * Handle the Enter event.
	 * 
	 */
	private void handleEnter()
	{
		clearAllColor(this);
		try
		{
			if (validateEntries())
			{
				if (dataChanged())
				{
					populateDataObject();
					getController().processData(
						AbstractViewController.ENTER,
						caSubcontractorRenewalCacheData);
				}
				// return to REG007 on no data change
				else
				{
					handleCancel();
				}
			}
			else
			{
				return;
			}

		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(this);
		}
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
	 * Handle the plate barcode input and populate screen
	 * 
	 * @param PlateBarCodeData aaPlateBarCodeData
	 */
	private void handlePlateBarCode(PlateBarCodeData aaPlateBarCodeData)
		throws RTSException
	{
		//set the display selection
		if (Integer
			.parseInt(cleanUpBarCode(aaPlateBarCodeData.getItemYr()))
			> 0)
		{
			//PLT
			showPlateAndStickerSelected(true, false);
		}
		else
		{
			//PLT and STKR
			showPlateAndStickerSelected(true, true);
		}
		//Merge Action - Removed cleanUpBarCode.
		gettxtNewPlateNo().setText(aaPlateBarCodeData.getItemNo());
		//gettxtNewPlateNo().setText(
		//cleanUpBarCode(aPlateBarCodeData.getItemNo()));	
		String lsPltCode =
			getFullDescFromCd(
				cleanUpBarCode(aaPlateBarCodeData.getItemCd()));
		if (lsPltCode == null)
		{
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				SubcontractorHelper.BAR_CODE_INV_ERROR,
				"").displayError(
				this);
		}
		else
		{
			getcomboPltCode().setSelectedItem(lsPltCode);
			// defect 8479
			comboBoxHotKeyFix(getcomboPltCode());
			// end defect 8479
		}
		if (Integer
			.parseInt(cleanUpBarCode(aaPlateBarCodeData.getItemYr()))
			> 0)
		{
			gettxtYear().setText(
				cleanUpBarCode(aaPlateBarCodeData.getItemYr()));
		}
	}
	/**
	 * Handle the renewal notice barcode input and populate screen
	 * 
	 * @param RenewalBarCodeData aaRenewalBarCodeData
	 */
	private void handleRenwlBarCode(RenewalBarCodeData aaRenewalBarCodeData)
		throws RTSException
	{
		//if plate number is OLDPLTX pop up mesage
		if (aaRenewalBarCodeData.getRegPltCd() != null
			&& aaRenewalBarCodeData.getRegPltCd().trim().equals(
				"OLDPLTX"))
		{
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Plate code is OLDPLTX.  Please use renewal event to continue.",
				"");
		}
		//Find duplicate doc no
		// defect 7473 
		//String lsDocNo = cleanUpBarCode(aRenewalBarCodeData.getDocNo());
		String lsDocNo = aaRenewalBarCodeData.getDocNo();
		// end defect 7473 
		if (!caSubconToBeModified.getDocNo().equals(lsDocNo))
		{
			if (caSubcontractorRenewalCacheData
				.getTransDocNo()
				.contains(lsDocNo))
			{
				throw new RTSException(
					RTSException.FAILURE_MESSAGE,
					SubcontractorHelper.DUPL_DOC + lsDocNo,
					"ERROR");
			}
		}
		// defect 9085
		String lsRegPltCd = aaRenewalBarCodeData.getRegPltCd().trim();
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(lsRegPltCd);
		//	Set Selections on Screen
		if (aaRenewalBarCodeData.treatAsSpclPlt())
		{
			if (laPltTypeData.getAnnualPltIndi() == 1
				&& !laPltTypeData.getPltOwnrshpCd().equals(
					SpecialPlatesConstant.VEHICLE))
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						"The scanned renewal notice cannot be processed in this event. "
							+ "Please use the Registration Renewal event to process.",
						"ERROR");
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}
			else if (getradioStkr().isSelected())
			{
				csSpclRegPltCd = lsRegPltCd;
				ciSpclPltIndi = 1;
				getchkSpclPlt().setEnabled(true);
				getchkSpclPlt().setSelected(true);
				getstcLblSpclPltTypeCode().setEnabled(true);
				csOrgNo = aaRenewalBarCodeData.getOrgNo();
				preSelectAddlSetChkBox(
					aaRenewalBarCodeData.getAddlSetIndi() == 1,
					csSpclRegPltCd);
				populateSpclPltDropDown();
				// defect 10392 
				//populates plate exp mo
				gettxtPltExpMo().setText(
					cleanUpBarCode(aaRenewalBarCodeData.getPltExpMo()));

				//populates plate exp yr
				gettxtPltExpYr().setText(
					String.valueOf(aaRenewalBarCodeData.getPltExpYr()));

				handleVendorPlate(
					aaRenewalBarCodeData.getPltValidityTerm());
				// end defect 10392 
			}
			else
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						"The scanned renewal notice can only be processed with the Sticker selection.",
						"ERROR");
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}
		}
		// end defect 9085 

		/// defect 9192
		// Do not allow processing of non-annual registration when
		// plate selected.  
		else if (
			laPltTypeData.getAnnualPltIndi() == 0
				&& getradioPlt().isSelected())
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					"The scanned renewal notice can not be processed with the Plate selection.",
					"ERROR");
			leRTSEx.setBeep(RTSException.BEEP);
			throw leRTSEx;
		}
		else if (
			laPltTypeData.getAnnualPltIndi() == 0
				&& getchkSpclPlt().isSelected())
		{
			resetSpclPlates();
		}
		else if (
			!laPltTypeData.getPltOwnrshpCd().equals(
				SpecialPlatesConstant.VEHICLE)
				&& getradioPltStkr().isSelected())
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					"The scanned renewal notice is not available for new Plate processing.",
					"ERROR");
			leRTSEx.setBeep(RTSException.BEEP);
			throw leRTSEx;

		}
		else if (
			laPltTypeData.getAnnualPltIndi() == 1
				&& !getradioPlt().isSelected())
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					"The scanned renewal notice can not be processed with the current selection.",
					"ERROR");
			leRTSEx.setBeep(RTSException.BEEP);
			throw leRTSEx;
		}
		// end defect 9192 
		//populate doc no
		gettxtDocno().setText(lsDocNo.trim());
		//populate vin
		gettxtVin().setText(aaRenewalBarCodeData.getVin().trim());
		//populates current plate
		// defect 7473 
		//gettxtCurrentPlateNo().setText(
		//	cleanUpBarCode(aRenewalBarCodeData.getRegPltNo().trim()));
		gettxtCurrentPlateNo().setText(
			aaRenewalBarCodeData.getRegPltNo().trim());
		// end defect 7473 
		//populates exp mo
		gettxtExpMo().setText(
			cleanUpBarCode(aaRenewalBarCodeData.getRegExpMo()));
		//populates reg class
		gettxtRegClass().setText(
			String.valueOf(aaRenewalBarCodeData.getRegClassCd()));

		//populates fee
		Dollar ldFees = new Dollar("0.00");
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice1());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice2());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice3());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice4());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice5());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice6());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice7());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice8());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice9());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice10());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice11());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice12());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice13());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice14());
		ldFees = ldFees.add(aaRenewalBarCodeData.getItmPrice15());

		gettxtFee().setText(ldFees.toString());

		if (getcomboStickerCode().isEnabled())
		{
			//preselect sticker combo
			String lsStkrCode =
				getFullDescFromCd(
					cleanUpBarCode(
						aaRenewalBarCodeData.getRegStkrCd()));
			if (lsStkrCode != null)
			{
				getcomboStickerCode().setSelectedItem(lsStkrCode);
				// defect 8479
				comboBoxHotKeyFix(getcomboStickerCode());
				// end defect 8479
			}
		}

		//display errmsg and turn off barcode indi if county is different
		if (!String
			.valueOf(SystemProperty.getOfficeIssuanceNo())
			.equals(cleanUpBarCode(aaRenewalBarCodeData.getCntyNo())))
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.INFORMATION_MESSAGE,
					SubcontractorHelper.DIFF_CNTY,
					"");
			leRTSEx.setBeep(RTSException.BEEP);
			leRTSEx.displayError(this);
			cbDiffCnty = true;
		}
		else
		{
			cbDiffCnty = false;
		}
		caRenewalBarCodeData = aaRenewalBarCodeData;
		caSubconToBeModified.setRenewalBarCodeData(
			aaRenewalBarCodeData);

		// preselect plate combo            
		// defect 7543
		if (getcomboPltCode().isEnabled())
		{
			String lsPltCode =
				getFullDescFromCd(
					cleanUpBarCode(aaRenewalBarCodeData.getRegPltCd()));
			if (lsPltCode != null)
			{
				getcomboPltCode().setSelectedItem(lsPltCode);
				// defect 8479
				comboBoxHotKeyFix(getcomboPltCode());
				// end defect 8479
			}
		}
		else
		{
			aaRenewalBarCodeData.setRegPltCd("");
		}
		// end defect 7543
		caRenewalBarCodeData.setRenwlPrice(ldFees);
	}

	/**
	 * Handle Modify Entry event
	 * 
	 * @param  asPltTerm String
	 */
	private void handleVendorPlate(String asPltTerm)
		throws RTSException
	{
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(csSpclRegPltCd);

		if (laPltTypeData.isVendorPlt())
		{
			populatePltTermDropDown(asPltTerm);
			getstcLblPltTerm().setEnabled(true);
			getstcLblPltExp().setEnabled(true);
			getcomboPltTerm().setEnabled(true);
			gettxtPltExpMo().setEnabled(true);
			gettxtPltExpYr().setEnabled(true);
		}
		else
		{
			populatePltTermDropDown("1");
			getstcLblPltTerm().setEnabled(false);
			getstcLblPltExp().setEnabled(false);
			getcomboPltTerm().setEnabled(false);
			gettxtPltExpMo().setEnabled(false);
			gettxtPltExpMo().setText("");
			gettxtPltExpYr().setEnabled(false);
			gettxtPltExpYr().setText("");
		}
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

			getradioPlt().setSelected(true);
			getradioPlt().requestFocus();
			addWindowListener(this);

			// user code end
			setName("FrmModifySubcontractorRenewalREG009");
			setSize(599, 360);
			setTitle("Modify Subcontractor Renewal          REG009");
			setContentPane(
				getFrmModifySubcontractorRenewalREG009ContentPane1());
		}
		catch (Throwable leIvjExc)
		{
			handleException(leIvjExc);
		}
		// user code begin {2}

		RTSButtonGroup laButtonGrp = new RTSButtonGroup();
		// Set RequestFocus to false so that when the hot keys are used
		// the correct focus changes occur.
		setRequestFocus(false);
		// end defect 7894
		laButtonGrp.add(getradioPlt());
		laButtonGrp.add(getradioStkr());
		laButtonGrp.add(getradioPltStkr());
		// user code end
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 *
	 * @param ItemEvent aaIE
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		try
		{
			if (aaIE.getSource() == getcomboPltCode())
			{
				postShowPlateAndStickerSelected();
			}
		}
		catch (RTSException rtsException)
		{
			rtsException.displayError(this);
		}
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmModifySubcontractorRenewalREG009 laFrmModifySubcontractorRenewalREG009;
			laFrmModifySubcontractorRenewalREG009 =
				new FrmModifySubcontractorRenewalREG009();
			laFrmModifySubcontractorRenewalREG009.setModal(true);
			laFrmModifySubcontractorRenewalREG009
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmModifySubcontractorRenewalREG009.show();
			java.awt.Insets laInsets =
				laFrmModifySubcontractorRenewalREG009.getInsets();
			laFrmModifySubcontractorRenewalREG009.setSize(
				laFrmModifySubcontractorRenewalREG009.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmModifySubcontractorRenewalREG009.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmModifySubcontractorRenewalREG009.setVisibleRTS(true);
		}
		catch (Throwable leException)
		{
			System.err.println(
				"Exception occurred in main() of RTSDialogBox");
			leException.printStackTrace(System.out);
		}
	}
	/**
	 * Populate data from the screen (and scanner object) and set it to
	 * TempSubcontractorRenewalCacheData
	 *
	 * @throws RTSException 
	 */
	private void populateDataObject() throws RTSException
	{
		//clean up data
		caSubcontractorRenewalCacheData.setException(null);
		SubcontractorRenewalData laSubconData =
			new SubcontractorRenewalData();
		laSubconData.setDiskEntry(
			caSubcontractorRenewalCacheData
				.getRecordTobeModified()
				.isDiskEntry());
		//check if bar code object should be set.
		if (useBarCodeScanner())
		{
			laSubconData.setRenewalBarCodeData(caRenewalBarCodeData);
			if (!cbDiffCnty)
			{
				laSubconData.setBarCdIndi(1);
			}
		}
		//populate the object
		Dollar ldFee = new Dollar(gettxtFee().getText());
		laSubconData.setCntyNo(SystemProperty.getOfficeIssuanceNo());
		laSubconData.setCustBaseRegFees(ldFee);
		laSubconData.setDocNo(gettxtDocno().getText().trim());
		laSubconData.setNewExpYr(
			Integer.parseInt(gettxtYear().getText()));
		// defect 7553
		if (caSubconToBeModified != null)
		{
			laSubconData.setAuditTrailTransid(
				caSubconToBeModified.getAuditTrailTransid());
		}
		// end defect 7553

		laSubconData.setVIN(gettxtVin().getText().trim());

		if (gettxtNewPlateNo().isEnabled())
		{
			laSubconData.setNewPltNo(
				gettxtNewPlateNo().getText().trim());
		}
		if (getcomboPltCode().isEnabled())
		{

			laSubconData.setPltItmCd(
				getCdFromDropDown(
					(String) getcomboPltCode().getSelectedItem()));
			// defect 7477
			// Show New Plate No as the last 6 digits of VIN 
			if (StickerPrintingUtilities
				.isStickerPrintable(laSubconData.getPltItmCd()))
			{
				String lsVIN = laSubconData.getVIN();
				if (lsVIN.length() > 6)
				{
					lsVIN =
						UtilityMethods.addPadding(
							laSubconData.getVIN(),
							22,
							"0").substring(
							16);
				}
				laSubconData.setNewPltNo(lsVIN);
			}
			// end defect 7477 
		}
		if (getchkPrintStkr().isSelected())
		{
			laSubconData.setPrint(true);
		}
		laSubconData.setRecordType(getCurrRecordTypeSelect());
		if (gettxtNewPlateNo().isEnabled())
		{
			laSubconData.setValidatePltIndi(
				SubcontractorRenewalData.VALIDATE_PLT);
		}
		laSubconData.setRegClassCd(
			cleanUpBarCode(gettxtRegClass().getText().trim()));
		laSubconData.setRegExpMo(
			Integer.parseInt(gettxtExpMo().getText()));
		laSubconData.setRegPltNo(
			gettxtCurrentPlateNo().getText().trim());
		laSubconData.setRenwlTotalFees(ldFee);
		if (getcomboStickerCode().isEnabled())
		{

			laSubconData.setStkrItmCd(
				getCdFromDropDown(
					(String) getcomboStickerCode().getSelectedItem()));
		}
		laSubconData.setSubconIssueDate(
			gettxtIssueDate().getDate().getYYYYMMDDDate());

		// defect 9085 
		if (getchkSpclPlt().isSelected())
		{
			laSubconData.setSpclPltIndi(1);
			laSubconData.setSpclPltRegPltCd(csSpclRegPltCd);
			laSubconData.setOrgNo(csOrgNo);
			laSubconData.setAddlSetIndi(
				getchkAddlSet().isSelected() ? 1 : 0);
			// defect 10392 
			laSubconData.setPltVldtyTerm(
				Integer.parseInt(
					getcomboPltTerm().getSelectedItem().toString()));
			if (caRenewalBarCodeData != null)
			{
				laSubconData.setPltNextExpMo(
					Integer.parseInt(
						caRenewalBarCodeData.getPltNextExpMo()));
				laSubconData.setPltNextExpYr(
					Integer.parseInt(
						caRenewalBarCodeData.getPltNextExpYr()));
			}
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(csSpclRegPltCd);
			if (laPltTypeData.isVendorPlt())
			{
				laSubconData.setPltExpMo(
					Integer.parseInt(
						gettxtPltExpMo().getText().trim()));
				laSubconData.setPltExpYr(
					Integer.parseInt(
						gettxtPltExpYr().getText().trim()));
			}
			else
			{
				laSubconData.setPltExpMo(
					Integer.parseInt(gettxtExpMo().getText().trim()));
				laSubconData.setPltExpYr(
					Integer.parseInt(gettxtYear().getText().trim())
						- 1);
			}
			// end defect 10392 
		}
		// end defect 9085 
		caSubcontractorRenewalCacheData.setTempSubconRenewalData(
			laSubconData);
	}
	/**
	 * Populate the plate drop down
	 * 
	 * @param abPltOnly boolean
	 * @throws RTSException
	 */
	private void populatePltDropDown(boolean abPltOnly)
		throws RTSException
	{
		getcomboPltCode().removeAllItems();
		if (abPltOnly)
		{
			for (int i = 0; i < cvPltCd.size(); i++)
			{
				ItemCodesData laItemCodesData =
					ItemCodesCache.getItmCd((String) cvPltCd.get(i));
				if (laItemCodesData == null)
				{
					throw new RTSException(584);
				}
				else
				{
					getcomboPltCode().addItem(
						laItemCodesData.getItmCd()
							+ SEPARATOR
							+ laItemCodesData.getItmCdDesc());
				}
			}
		}
		else
		{
			Vector lvPltSkrDesc =
				ItemCodesCache.getItmCds(
					ItemCodesCache.BOTH,
					PROCSNG_CD,
					PLT_TRCKNG_TYPE);
			if (lvPltSkrDesc == null || lvPltSkrDesc.size() == 0)
			{
				throw new RTSException(584);
			}
			for (int i = 0; i < cvPltSkrCd.size(); i++)
			{
				ItemCodesData laItemCodesData =
					ItemCodesCache.getItmCd((String) cvPltSkrCd.get(i));
				if (laItemCodesData == null)
				{
					throw new RTSException(584);
				}
				else
				{
					getcomboPltCode().addItem(
						laItemCodesData.getItmCd()
							+ SEPARATOR
							+ laItemCodesData.getItmCdDesc());
				}
			}
			for (int i = 0; i < lvPltSkrDesc.size(); i++)
			{
				ItemCodesData laItemCodesData =
					(ItemCodesData) lvPltSkrDesc.get(i);
				if (laItemCodesData == null)
				{
					throw new RTSException(584);
				}
				else
				{
					if (!(cvPltCd.contains(laItemCodesData.getItmCd())
						|| cvPltSkrCd.contains(
							laItemCodesData.getItmCd())))
					{
						getcomboPltCode().addItem(
							laItemCodesData.getItmCd()
								+ SEPARATOR
								+ laItemCodesData.getItmCdDesc());
					}
				}
			}
		}
		getcomboPltCode().setSelectedIndex(0);
		// defect 8479
		comboBoxHotKeyFix(getcomboPltCode());
		// end defect 8479
	}
	/**
	 * Populate the plate term combo.
	 * 
	 * @param  asPltTerm String
	 * @throws RTSException
	 */
	private void populatePltTermDropDown(String asPltTerm)
		throws RTSException
	{
		getcomboPltTerm().removeAllItems();

		boolean lbPreselected;
		String lsPltTerm = "";
		if (asPltTerm != null)
		{
			lbPreselected = true;
			// Remove any leading zeroes
			int liPltTerm = Integer.parseInt(asPltTerm);
			lsPltTerm = Integer.toString(liPltTerm);
		}
		else
		{
			lbPreselected = false;
		}
		boolean lbFound = false;
		int liIndex = -1;

		// defect 10644 
		for (int i = 0; i < svPltTerm.size(); i++)
		{
			// end defect 10644 
			
			String lsPltTermInList = svPltTerm.get(i).toString().trim();

			if (lbPreselected)
			{
				if (!lbFound && lsPltTerm.equals(lsPltTermInList))
				{
					liIndex = i;
					lbFound = true;
				}
			}

			getcomboPltTerm().addItem(lsPltTermInList);
		}
		getcomboPltTerm().setSelectedIndex(liIndex);

		// defect 8479
		comboBoxHotKeyFix(getcomboPltTerm());
		// end defect 8479
	}

	/**
	 * Populate the sticker drop down
	 *
	 * @throws RTSException 
	 */
	private void populateStkrDropDown() throws RTSException
	{
		getcomboStickerCode().removeAllItems();
		Vector lvStkrDesc = null;
		lvStkrDesc =
			ItemCodesCache.getItmCds(
				ItemCodesCache.BOTH,
				PROCSNG_CD,
				STKR_TRCKNG_TYPE);
		if (lvStkrDesc == null || lvStkrDesc.size() == 0)
		{
			throw new RTSException(584);
		}
		ItemCodesData laDefaultItemCodesData =
			ItemCodesCache.getItmCd(DEFAULT_STKR_CD);
		getcomboStickerCode().addItem(
			laDefaultItemCodesData.getItmCd()
				+ SEPARATOR
				+ laDefaultItemCodesData.getItmCdDesc());
		for (int i = 0; i < lvStkrDesc.size(); i++)
		{
			ItemCodesData laItemCodesData =
				(ItemCodesData) lvStkrDesc.get(i);
			if (!laItemCodesData.getItmCd().equals(DEFAULT_STKR_CD))
			{
				getcomboStickerCode().addItem(
					laItemCodesData.getItmCd()
						+ SEPARATOR
						+ laItemCodesData.getItmCdDesc());
			}
		}
		getcomboStickerCode().setSelectedIndex(0);
		// defect 8479
		comboBoxHotKeyFix(getcomboStickerCode());
		// end defect 8479
	}
	/**
	 * Set the screen visibility of the plate and sticker combo box and
	 * populate the combo box with appropriate value according to state
	 * 
	 * @throws RTSException
	 */
	private void postShowPlateAndStickerSelected() throws RTSException
	{
		//populate dropdown
		try
		{
			boolean lbATVSSelected = false;
			if (getcomboPltCode().getSelectedItem() != null)
			{
				lbATVSSelected =
					getCdFromDropDown(
						(String) getcomboPltCode()
							.getSelectedItem())
							.equals(
						FrmRegistrationSubcontractorRenewalREG007
							.ATVS_CD);
			}
			// Set Check Print Inventory
			// Enable if ((Sticker || ATVS &&) !Diskette )
			if ((getcomboStickerCode().isEnabled() || lbATVSSelected)
				&& caSubcontractorRenewalCacheData.getSubconDiskData()
					== null)
			{
				getchkPrintStkr().setEnabled(true);
				getchkPrintStkr().setSelected(cbPrintChecked);
			}
			else
			{
				getchkPrintStkr().setEnabled(false);
				// defect 5975
				getchkPrintStkr().setSelected(false);
				// end defect 5975 
			}
			// Enable New Plate if (ComboPlt && !ATVS)
			// defect 7486, 7487, 7489
			// Do Not Disable NewPlate for Diskette Entry &&
			// caSubcontractorRenewalCacheData.getSubconDiskData()== null
			if (getcomboPltCode().isEnabled() & !lbATVSSelected)
			{
				getstcLblNewPltNo().setEnabled(true);
				gettxtNewPlateNo().setEnabled(true);
				gettxtNewPlateNo().setText(csNewPlateNo);

			}
			else
			{
				getstcLblNewPltNo().setEnabled(false);
				gettxtNewPlateNo().setEnabled(false);
				gettxtNewPlateNo().setText("");
			}
		}
		catch (RTSException leEx)
		{
			leEx.displayError(this);
		}
	}
	/**
	 * preSelectAddlSetChkBox 
	 * 
	 * @param abAddlSet
	 * @param asSpclPltRegPltCd 
	 */

	private void preSelectAddlSetChkBox(
		boolean abAddlSet,
		String asSpclPltRegPltCd)
	{
		getchkAddlSet().setSelected(abAddlSet);
		PlateTypeData laPTData =
			PlateTypeCache.getPlateType(asSpclPltRegPltCd);

		switch (laPTData.getPltSetImprtnceCd())
		{
			case 0 :
				{
					getchkAddlSet().setEnabled(false);
					break;
				}
			case 1 :
				{
					getchkAddlSet().setEnabled(false);
					break;
				}
			case 2 :
				{
					getchkAddlSet().setEnabled(false);
					break;
				}
			case 3 :
				{
					getchkAddlSet().setEnabled(true);
					break;
				}
		}
	}
	/**
	 * Pre-select the combo box for special plate
	 * 
	 * @param aaSubcontractorRenewalCacheData SubcontractorRenewalData,
	 * @throws RTSException 
	 */
	private void preSelectSpclPltComboBox(SubcontractorRenewalData aaSubcontractorRenewalData)
		throws RTSException
	{
		// Only called if SpclPltIndi = 1, but just in case
		if (aaSubcontractorRenewalData.getSpclPltIndi() == 1)
		{
			csSpclRegPltCd =
				aaSubcontractorRenewalData.getSpclPltRegPltCd();
			csOrgNo = aaSubcontractorRenewalData.getOrgNo();
			populateSpclPltDropDown();
		}
	}
	/**
	 * Populate the Organization Name
	 * 
	 * @throws RTSException
	 */
	private void populateOrganizationNameDropDown() throws RTSException
	{
		getcomboOrganizationName().removeAllItems();
		getcomboOrganizationName().removeActionListener(this);

		// Add 2nd parameter to indicate that should present organizations
		// even if not current. 
		Vector lvOrgNo =
			OrganizationNumberCache.getOrgsPerPlt(
				csSpclRegPltCd,
				false);

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

			int liIndex = (cvOrgNo.size() == 0 ? 0 : -1);

			for (int i = 0; i < lvOrgNo.size(); i++)
			{
				String lsOrgNo = (String) cvOrgNo.elementAt(i);
				if (csOrgNo != null)
				{
					if (csOrgNo.equals(lsOrgNo.substring(50).trim()))
					{
						liIndex = i;
					}
				}
				getcomboOrganizationName().addItem(
					lsOrgNo.substring(0, 50).trim());
			}
			if (cvOrgNo.size() == 1)
			{
				liIndex = 0;
				csOrgNo =
					((String) cvOrgNo.elementAt(0))
						.substring(50)
						.trim();
			}
			getcomboOrganizationName().setSelectedIndex(liIndex);
			getstcLblOrganizationName().setEnabled(true);
			getcomboOrganizationName().setEnabled(true);
			getcomboOrganizationName().addActionListener(this);
			comboBoxHotKeyFix(getcomboOrganizationName());
		}
	}
	/**
	 * Populate the Special Plate Type combo.
	 * 
	 * @throws RTSException
	 */
	private void populateSpclPltDropDown() throws RTSException
	{
		getcomboSpclPltTypeCode().removeAllItems();
		getcomboSpclPltTypeCode().removeActionListener(this);
		getstcLblSpclPltTypeCode().setEnabled(true);

		boolean lbPreselected = (csSpclRegPltCd != null);
		boolean lbFound = false;
		int liIndex = -1;
		for (int i = 0; i < svSpclPltDesc.size(); i++)
		{
			String lsSpclPltDesc = (String) svSpclPltDesc.elementAt(i);
			String lsSpclPltCd = lsSpclPltDesc.substring(50).trim();

			if (lbPreselected)
			{
				if (!lbFound && csSpclRegPltCd.equals(lsSpclPltCd))
				{
					liIndex = i;
					lbFound = true;
					csSpclRegPltCd = lsSpclPltCd;
				}
			}
			getcomboSpclPltTypeCode().addItem(
				lsSpclPltDesc.substring(0, 50).trim());
		}
		getcomboSpclPltTypeCode().setSelectedIndex(liIndex);
		if (lbFound)
		{
			populateOrganizationNameDropDown();
		}
		comboBoxHotKeyFix(getcomboSpclPltTypeCode());
		getcomboSpclPltTypeCode().addActionListener(this);
		getcomboSpclPltTypeCode().setEnabled(true);
	}
	/**
	 * Pre-select the combo box for the appropriate value
	 * 
	 * @param aaSubcontractorRenewalCacheData SubcontractorRenewalData,
	 * @param abPlt boolean
	 * @param abStkr boolean
	 * @throws RTSException 
	 */
	private void preSelectCombo(
		SubcontractorRenewalData aaSubcontractorRenewalData,
		boolean abPlt,
		boolean abStkr)
		throws RTSException
	{
		if (abStkr)
		{
			getcomboStickerCode().setSelectedItem(
				getFullDescFromCd(
					aaSubcontractorRenewalData.getStkrItmCd()));
			// defect 8479
			comboBoxHotKeyFix(getcomboStickerCode());
			// end defect 8479
		}
		if (abPlt)
		{
			getcomboPltCode().setSelectedItem(
				getFullDescFromCd(
					aaSubcontractorRenewalData.getPltItmCd()));
			// defect 8479
			comboBoxHotKeyFix(getcomboPltCode());
			// end defect 8479
		}
		// defect 9085
		if (aaSubcontractorRenewalData.getSpclPltIndi() == 1)
		{
			getchkSpclPlt().setEnabled(true);
			getchkSpclPlt().setSelected(true);
			ciSpclPltIndi = 1;
			preSelectAddlSetChkBox(
				aaSubcontractorRenewalData.getAddlSetIndi() == 1,
				aaSubcontractorRenewalData.getSpclPltRegPltCd());
			preSelectSpclPltComboBox(aaSubcontractorRenewalData);
		}
		else
		{
			getcomboSpclPltTypeCode().setEnabled(false);
			getcomboOrganizationName().setEnabled(false);
			getchkAddlSet().setEnabled(false);

		}
		// end defect 9085
	}

	/**
	 * Validate the current plate number
	 *
	 * @param aeRTSEx RTSException  
	 */
	private void scrValCurrPltNo(RTSException aeRTSEx)
	{
		if (gettxtCurrentPlateNo().getText().equals(""))
		{
			aeRTSEx.addException(
				new RTSException(150),
				gettxtCurrentPlateNo());
			return;
		}
	}
	/**
	 * Validate the document number
	 * 
	 * @param aeRTSEx RTSException  
	 */
	private void scrValDocNo(RTSException aeRTSEx)
	{
		if (gettxtDocno().getText().equals("")
			|| gettxtDocno().getText().length() < 17)
		{
			aeRTSEx.addException(new RTSException(150), gettxtDocno());
		}
	}
	/**
	 * Validate the expiration month
	 * 
	 * @param aeRTSEx  
	 */
	private void scrValExpMo(RTSException aeRTSEx)
	{
		if (gettxtExpMo().getText().equals(""))
		{
			aeRTSEx.addException(new RTSException(150), gettxtExpMo());
		}
		else
		{
			RTSException leRTSEx =
				SubcontractorHelper.validateMo(
					gettxtExpMo().getText().trim());

			// defect 9097 
			// Evaluate the number of months if valid year 
			if (leRTSEx != null)
			{
				aeRTSEx.addException(leRTSEx, gettxtExpMo());
			}
			else if (cbValidYear)
			{
				RTSDate laRTSDate = new RTSDate();
				int liCurrentMonths =
					laRTSDate.getMonth() + laRTSDate.getYear() * 12;
				int liRegExpMonths =
					Integer.parseInt(gettxtExpMo().getText())
						+ Integer.parseInt(gettxtYear().getText()) * 12;
				int liNoOfMonths = liRegExpMonths - liCurrentMonths;
				if (liNoOfMonths
					> RegistrationConstant.MAX_SUBCON_MONTHS
					|| liNoOfMonths
						< RegistrationConstant.MIN_SUBCON_MONTHS)
				{
					aeRTSEx.addException(
						new RTSException(150),
						gettxtExpMo());
					aeRTSEx.addException(
						new RTSException(150),
						gettxtYear());
					cbValidYear = false;
				}
			}
			// end defect 9097 
		}
	}
	/**
	 * Validate the Fees
	 * 
	 * @param RTSException aeRTSEx 
	 */
	private void scrValFee(RTSException aeRTSEx)
	{
		if (gettxtFee()
			.getText()
			.equals(CommonConstant.STR_SPACE_EMPTY))
		{
			aeRTSEx.addException(new RTSException(150), gettxtFee());
			return;
		}
		// total renewal fees must be >= 0
		Dollar laFee = new Dollar(gettxtFee().getText());
		// defect 8900
		if (!check2Decimals(gettxtFee().getText().trim())
			|| laFee.compareTo(CommonConstant.ZERO_DOLLAR) < 0
			|| laFee.compareTo(new Dollar("9999.99")) > 0)
			// end defect 8900
		{
			aeRTSEx.addException(new RTSException(150), gettxtFee());
			return;
		}
		//input renewal fee greater than MinRegFee + ReflectnFee
		//if reg class is empty, this check cannot be performed
		// defect 7656

		if (cbIssueDateValid)
		{
			if (!gettxtRegClass()
				.getText()
				.trim()
				.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				CommonFeesData laCommonFeesData =
					CommonFeesCache.getCommonFee(
						Integer.parseInt(
							gettxtRegClass().getText().trim()),
						gettxtIssueDate().getDate().getYYYYMMDDDate());
				//note lCommonFeesData should not be null since validate
				//in validateRegCd
				// defect 8900
				if (!laFee.equals(CommonConstant.ZERO_DOLLAR))
					// end defect 8900
				{
					if (laCommonFeesData != null)
					{
						Dollar ldMinRegFee =
							laCommonFeesData.getMinRegFee().add(
								laCommonFeesData.getReflectnFee());
						if (laFee.compareTo(ldMinRegFee) == -1)
						{
							aeRTSEx.addException(
								new RTSException(706),
								gettxtFee());
							return;
						}
					}
				}
			}
			//renwlTotalFee cannot make bundle exceed maximum total
			if (caSubcontractorRenewalCacheData
				.getRunningTotal()
				.compareTo(BUNDLE_MAX_AMT)
				> 0)
			{
				aeRTSEx.addException(
					new RTSException(622),
					gettxtFee());
				return;
			}
		}
		// end defect 7656 
	}

	//	/**
	//	 * Validate the issue date
	//	 * 
	//	 * @param RTSException aeRTSEx 
	//	 */
	//	private void scrValIssueDate(RTSException aeRTSEx)
	//	{
	//		cbIssueDateValid = false;
	//		if (!gettxtIssueDate().isValidDate())
	//		{
	//			aeRTSEx.addException(
	//				new RTSException(150),
	//				gettxtIssueDate());
	//		}
	//		else
	//		{
	//			int liIssueAMDate = gettxtIssueDate().getDate().getAMDate();
	//			int liCurrentAMDate = new RTSDate().getAMDate();
	//			//PROC Validate SubconIssueAMDate for 0, null or in the future
	//			if (liIssueAMDate == 0)
	//			{
	//				aeRTSEx.addException(
	//					new RTSException(150),
	//					gettxtIssueDate());
	//			}
	//			else
	//			{
	//				if ((liIssueAMDate > liCurrentAMDate))
	//				{
	//					aeRTSEx.addException(
	//						new RTSException(427),
	//						gettxtIssueDate());
	//				}
	//				else
	//				{
	//					cbIssueDateValid = true;
	//				}
	//			}
	//		}
	//		return;
	//	}

	/**
	 * Validate the issue date
	 * 
	 * @param aeRTSEx 
	 */
	private void scrValIssueDate(RTSException aeRTSEx)
	{
		// defect 10355
		int liErrMsgNo = 0;
		cbIssueDateValid = true;

		if (!gettxtIssueDate().isValidDate())
		{
			liErrMsgNo = ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
		}
		else
		{
			RTSDate laIssueDate = gettxtIssueDate().getDate();

			// Today < Issue Date 
			if (caToday.compareTo(laIssueDate) == -1)
			{
				liErrMsgNo =
					ErrorsConstant.ERR_MSG_DATE_CANNOT_BE_IN_FUTURE;
			}
			// IssueDate Year < Current Year -1 
			else if (laIssueDate.getYear() < caToday.getYear() - 1)
			{
				liErrMsgNo = ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
			}
		}
		if (liErrMsgNo != 0)
		{
			aeRTSEx.addException(
				new RTSException(liErrMsgNo),
				gettxtIssueDate());
			cbIssueDateValid = false;
		}
		// end defect 10355
	}

	/**
	 * Validate issue year
	 * 
	 * @param RTSException aeRTSEx 
	 */
	private void scrValIssueYear(RTSException aeRTSEx)
	{
		// defect 9097  
		// Set flag for invalid year so that do not validate in 
		//  RegExpMo validation  
		cbValidYear = true;
		String lsYr = gettxtYear().getText().trim();
		int liYr = 0;
		if (!lsYr.equals(""))
		{
			liYr = Integer.parseInt(lsYr);
		}
		if (lsYr.equals("") || liYr > YR_END || liYr < YR_BEGIN)
		{

			aeRTSEx.addException(new RTSException(150), gettxtYear());
			return;
		}
		int liCurrYr = new RTSDate().getYear();
		if (!(liYr >= liCurrYr && liYr <= liCurrYr + 2))
		{
			cbValidYear = false;
			aeRTSEx.addException(new RTSException(150), gettxtYear());
		}
		// end defect 9097 
	}
	/**
	 * Validate new plate number
	 * 
	 * @param RTSException aeRTSEx 
	 */
	private void scrValNewPltNo(RTSException aeRTSEx)
		throws RTSException
	{
		if (gettxtNewPlateNo().isEnabled())
		{
			// defect 7641
			// Save entered Plate No 
			if (gettxtNewPlateNo().getText().length() > 0)
			{
				csNewPlateNo = gettxtNewPlateNo().getText();
			}
			// end defect 7641 
			if (gettxtNewPlateNo().getText().equals(""))
			{
				aeRTSEx.addException(
					new RTSException(150),
					gettxtNewPlateNo());
				return;
			}
			if (!SubcontractorHelper
				.validatePltStkrEntry(
					SubcontractorHelper.PLT_MASK,
					gettxtNewPlateNo().getText().trim()))
			{
				aeRTSEx.addException(
					new RTSException(150),
					gettxtNewPlateNo());
			}
			else
			{
				//validate inventory pattern, added feature in RTS II
				ValidateInventoryPattern laValidateInventoryPattern =
					new ValidateInventoryPattern();
				//ok if no exception thrown
				ProcessInventoryData laProcessInventoryData =
					new ProcessInventoryData();
				laProcessInventoryData.setItmCd(
					getCdFromDropDown(
						(String) getcomboPltCode().getSelectedItem()));
				if (getcomboStickerCode().isEnabled())
				{
					laProcessInventoryData.setInvItmYr(0);
				}
				else
				{
					laProcessInventoryData.setInvItmYr(
						Integer.parseInt(gettxtYear().getText()));
				}
				laProcessInventoryData.setInvQty(1);
				laProcessInventoryData.setInvItmNo(
					gettxtNewPlateNo().getText().trim());
				try
				{
					laValidateInventoryPattern.validateItmNoInput(
						laProcessInventoryData
							.convertToInvAlloctnUIData(
							laProcessInventoryData));
				}
				catch (RTSException leRTSEx)
				{
					aeRTSEx.addException(leRTSEx, gettxtNewPlateNo());
				}
			}
		}
	}
	/**
	 * Validate plate expiration month
	 * 
	 * @param aeRTSEx  
	 */
	private void scrValPltExpMo(RTSException aeRTSEx)
	{
		if (gettxtPltExpMo().getText().equals(""))
		{
			aeRTSEx.addException(
				new RTSException(150),
				gettxtPltExpMo());
		}
		else
		{
			RTSException leRTSEx =
				SubcontractorHelper.validateMo(
					gettxtPltExpMo().getText().trim());

			if (leRTSEx != null)
			{
				aeRTSEx.addException(leRTSEx, gettxtPltExpMo());
			}
		}
	}
	/**
	 * Validate plate expiration year
	 * 
	 * @param aeRTSEx RTSException  
	 */
	private void scrValPltExpYr(RTSException aeRTSEx)
	{
		String lsYr = gettxtPltExpYr().getText().trim();
		int liYr = 0;
		if (!lsYr.equals(""))
		{
			liYr = Integer.parseInt(lsYr);
		}
		if (lsYr.equals("") || liYr > YR_END || liYr < YR_BEGIN)
		{

			aeRTSEx.addException(
				new RTSException(150),
				gettxtPltExpYr());
			return;
		}
		int liCurrYr = new RTSDate().getYear();
		if (!(liYr >= liCurrYr - 1 && liYr <= liCurrYr + 10))
		{
			cbValidYear = false;
			aeRTSEx.addException(
				new RTSException(150),
				gettxtPltExpYr());
		}
	}
	/**
	 * 
	 * Validate Special Plate Selection
	 * 
	 * @param aeRTSEx
	 */
	private void scrValPltTerm(RTSException aeRTSEx)
	{
		if (getcomboPltTerm().getSelectedIndex() == -1)
		{
			aeRTSEx.addException(
				new RTSException(150),
				getcomboPltTerm());
		}
	}
	/**
	 * Validate Reg Class code
	 * 
	 * @param RTSException aeRTSEx 
	 */
	private void scrValRegClassCd(RTSException aeRTSEx)
	{
		if (gettxtRegClass().getText().trim().equals(""))
		{
			aeRTSEx.addException(
				new RTSException(150),
				gettxtRegClass());
		}
		else
		{
			int liRegClassCd =
				Integer.parseInt(gettxtRegClass().getText().trim());
			if (cbIssueDateValid)
			{
				int liIssueDate =
					gettxtIssueDate().getDate().getYYYYMMDDDate();

				// defect 9362 
				// Use Common Method for validation
				if (!SubcontractorHelper
					.validateRegClassCd(liRegClassCd, liIssueDate))
				{
					aeRTSEx.addException(
						new RTSException(150),
						gettxtRegClass());
				}
				// end defect 9362

				// defect 9085
				// Verify that Special Plate is valid for the specified 
				// RegClassCd
				if (ciSpclPltIndi == 1
					&& getcomboSpclPltTypeCode().getSelectedIndex() != -1)
				{
					Object laClassToPltData =
						ClassToPlateCache.getClassToPlate(
							liRegClassCd,
							csSpclRegPltCd,
							liIssueDate);
					if (laClassToPltData == null)
					{
						aeRTSEx.addException(
							new RTSException(150),
							gettxtRegClass());
					}
				}
				// end defect 9085  
			}
		}
	}
	/**
	 * 
	 * Validate Special Plate Selection
	 * 
	 * @param aeRTSEx
	 */
	private void scrValSpclPlt(RTSException aeRTSEx)
	{
		if (getchkSpclPlt().isSelected())
		{
			if (getcomboSpclPltTypeCode().getSelectedIndex() == -1)
			{
				aeRTSEx.addException(
					new RTSException(150),
					getcomboSpclPltTypeCode());
			}
		}
	}
	/**
	 * 
	 * Validate Special Plate Sticker Selection
	 * 
	 * @param aeRTSEx
	 */
	private void scrValSpclPltStkr(RTSException aeRTSEx)
	{
		String lsStkrCd = "";

		if (getchkSpclPlt().isSelected()
			&& (getcomboSpclPltTypeCode().getSelectedIndex() != -1))
		{
			try
			{
				lsStkrCd =
					getCdFromDropDown(
						(String) getcomboStickerCode()
							.getSelectedItem());
			}
			catch (RTSException aeRTSEx1)
			{
			}
			Vector lvVector =
				PlateToStickerCache.getPltToStkrs(
					csSpclRegPltCd,
					lsStkrCd,
					new RTSDate().getYYYYMMDDDate());
			if (lvVector == null || lvVector.size() == 0)
			{
				aeRTSEx.addException(
					new RTSException(150),
					getcomboStickerCode());
			}
		}
	}
	/**
	 * 
	 * Validate Special Organization Selection
	 * 
	 * @param aeRTSEx
	 */
	private void scrValSpclOrgNo(RTSException aeRTSEx)
	{
		if (getchkSpclPlt().isSelected()
			&& getcomboSpclPltTypeCode().getSelectedIndex() != -1
			&& getcomboOrganizationName().getSelectedIndex() == -1)
		{
			aeRTSEx.addException(
				new RTSException(150),
				getcomboOrganizationName());
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * Receives SubcontractorRenewalCacheData
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			if (aaDataObject instanceof SubcontractorRenewalCacheData)
			{
				caSubcontractorRenewalCacheData =
					(SubcontractorRenewalCacheData) aaDataObject;
			}
			if (!cbReentry)
			{
				cbReentry = true;
				//initialize bar code scanner
				try
				{
					caBarCodeScanner =
						getController()
							.getMediator()
							.getAppController()
							.getBarCodeScanner();
					caBarCodeScanner.addBarCodeListener(this);
				}
				catch (RTSException rtsException)
				{
					// defect 11071
					Log.write(Log.DEBUG, this, rtsException.getDetailMsg());
					// end defect 11071
				}
				//Heading display
				getlblSubcontractor().setText(
					caSubcontractorRenewalCacheData
						.getDisplaySubconInfo());
				caSubconToBeModified =
					caSubcontractorRenewalCacheData
						.getRecordTobeModified();
				//Plate/Sticker display
				int liRecordType = caSubconToBeModified.getRecordType();
				csNewPlateNo = gettxtNewPlateNo().getText().trim();
				switch (liRecordType)
				{
					case SubcontractorRenewalCacheData.STKR :
						getradioStkr().setSelected(true);
						showPlateAndStickerSelected(false, true);
						preSelectCombo(
							caSubconToBeModified,
							false,
							true);
						postShowPlateAndStickerSelected();
						break;
					case SubcontractorRenewalCacheData.PLT :
						getradioPlt().setSelected(true);
						showPlateAndStickerSelected(true, false);
						preSelectCombo(
							caSubconToBeModified,
							true,
							false);
						postShowPlateAndStickerSelected();
						break;
					case SubcontractorRenewalCacheData.PLT_STKR :
						getradioPltStkr().setSelected(true);
						showPlateAndStickerSelected(true, true);
						preSelectCombo(
							caSubconToBeModified,
							true,
							true);
						postShowPlateAndStickerSelected();
						break;
				}
				// defect 10488 
				// defect 9085 
				if (caSubconToBeModified.getSpclPltIndi() == 1)
				{
					csSpclRegPltCd =
						caSubconToBeModified.getSpclPltRegPltCd();
					csOrgNo = caSubconToBeModified.getOrgNo();
					// defect 10392 
					//Set Plate Exp Mo
					gettxtPltExpMo().setText(
						String.valueOf(
							caSubconToBeModified.getPltExpMo()));
					//Set Plate Exp Yr
					gettxtPltExpYr().setText(
						String.valueOf(
							caSubconToBeModified.getPltExpYr()));
					handleVendorPlate(
						Integer.toString(
							caSubconToBeModified.getPltVldtyTerm()));
					// end defect 10392 
				}
				else
				{
					resetSpclPlates();
				}
				// end defect 9085 
				// end defect 10488 
				if (caSubcontractorRenewalCacheData.getSubconDiskData()
					!= null)
				{
					getchkPrintStkr().setEnabled(false);
				}
				if (getchkPrintStkr().isEnabled())
				{
					getchkPrintStkr().setSelected(
						caSubconToBeModified.isPrint());
					cbPrintChecked = caSubconToBeModified.isPrint();
				}

				//Set Next Year
				gettxtYear().setText(
					String.valueOf(caSubconToBeModified.getNewExpYr()));
				//Set Issue Date
				gettxtIssueDate().setDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						caSubconToBeModified.getSubconIssueDate()));
				// defect 7537
				if (caSubcontractorRenewalCacheData.getSubconDiskData()
					!= null)
				{
					gettxtIssueDate().setEnabled(false);
					getstcLblIssueDate().setEnabled(false);
				}
				// end defect 7537				
				//Set Plt No
				if (getcomboPltCode().isEnabled()
					&& !getCdFromDropDown(
						(String) getcomboPltCode()
							.getSelectedItem())
							.equals(
						"ATVS"))
				{
					getNewPlt().setEnabled(true);
					gettxtNewPlateNo().setText(
						caSubconToBeModified.getNewPltNo());
				}
				//Set doc no
				gettxtDocno().setText(caSubconToBeModified.getDocNo());
				//Set Vin
				gettxtVin().setText(caSubconToBeModified.getVIN());
				//Set Curr Plt
				gettxtCurrentPlateNo().setText(
					caSubconToBeModified.getRegPltNo());
				//Set Exp Mo
				gettxtExpMo().setText(
					String.valueOf(caSubconToBeModified.getRegExpMo()));
				//Set Reg Class
				gettxtRegClass().setText(
					caSubconToBeModified.getRegClassCd());
				//Set Reg Fee
				gettxtFee().setText(
					caSubconToBeModified
						.getRenwlTotalFees()
						.toString());
				gettxtYear().requestFocus();

				cbInitialize = false;
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
	}
	/**
	* Insert the method's description here.
	*  
	* @param abSetVisible boolean
	*/
	public void setVisibleRTS(boolean abSetVisible)
	{

		if (abSetVisible)
		{
			super.setVisibleRTS(abSetVisible);
		}
		else
		{
			hide();
		}
	}
	/**
	 * Reset checkbox and comboboxes 
	 * 
	 */
	private void resetSpclPlates()
	{
		getcomboOrganizationName().removeAllItems();
		getcomboSpclPltTypeCode().removeAllItems();

		getchkSpclPlt().setSelected(false);
		getchkSpclPlt().setEnabled(getradioStkr().isSelected());

		getstcLblOrganizationName().setEnabled(false);
		getcomboOrganizationName().setEnabled(false);

		getstcLblSpclPltTypeCode().setEnabled(false);
		getcomboSpclPltTypeCode().setEnabled(false);

		getchkAddlSet().setEnabled(false);
		getchkAddlSet().setSelected(false);

		ciAddlSetIndi = 0;
		ciSpclPltIndi = 0;
		csSpclRegPltCd = null;
		csOrgNo = null;
		// defect 10392 
		getcomboPltTerm().removeAllItems();
		getstcLblPltTerm().setEnabled(false);
		getstcLblPltExp().setEnabled(false);
		getcomboPltTerm().setEnabled(false);
		gettxtPltExpMo().setEnabled(false);
		gettxtPltExpYr().setEnabled(false);

		gettxtPltExpMo().setText("");
		gettxtPltExpYr().setText("");
		// end defect 10392 
	}
	/**
	 * Set the screen visibility of the plate and sticker combo box and 
	 * populate combo box with appropriate value according to the state
	 * 
	 * @param boolean abPlate, boolean abSticker
	 * @throws RTSException
	 */
	private void showPlateAndStickerSelected(
		boolean abPlate,
		boolean abSticker)
		throws RTSException
	{
		//populate dropdown
		try
		{
			//combo
			if (abPlate && !abSticker)
			{
				getcomboStickerCode().removeAllItems();
				populatePltDropDown(true);
				getstcLblStickerItemCode().setEnabled(false);
				getcomboStickerCode().setEnabled(false);
				getcomboPltCode().setEnabled(true);
				getcomboPltCode().requestFocus();
				// defect 9085 
				resetSpclPlates();
				// end defect 9085 
			}
			// Just Sticker
			else if (!abPlate && abSticker)
			{
				getcomboPltCode().removeAllItems();
				populateStkrDropDown();
				getcomboStickerCode().setEnabled(true);
				getstcLblPltItemCode().setEnabled(false);
				getcomboPltCode().setEnabled(false);
				getcomboStickerCode().requestFocus();
			}
			else
			{
				populatePltDropDown(false);
				populateStkrDropDown();
				getstcLblStickerItemCode().setEnabled(true);
				getstcLblPltItemCode().setEnabled(true);
				getcomboPltCode().setEnabled(true);
				getcomboStickerCode().setEnabled(true);
				getcomboPltCode().requestFocus();
				// defect 9085 
				resetSpclPlates();
				// end defect 9085 
			}
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(this);
		}
	}
	/**
	 * Invoked when the target of the listener has changed its state.
	 *
	 * @param aaCE  ChangeEvent 
	 * @deprecated
	 */
	public void stateChanged(ChangeEvent aaCE)
	{
		try
		{
			// defect 7492
			clearAllColor(this);
			// end defect 7492
			if (gettxtNewPlateNo().isEnabled())
			{
				csNewPlateNo = gettxtNewPlateNo().getText();
			}
			//PLATE 
			if (aaCE.getSource() == getradioPlt())
			{
				if (getradioPlt().isSelected())
				{
					showPlateAndStickerSelected(true, false);
				}
			}
			else
			{
				//PLATE STICKER
				if (aaCE.getSource() == getradioPltStkr())
				{
					if (getradioPltStkr().isSelected())
					{
						showPlateAndStickerSelected(true, true);
					}
				}
				else
				{
					//STICKER
					if (aaCE.getSource() == getradioStkr()
						&& getradioStkr().isSelected())
					{
						showPlateAndStickerSelected(false, true);
					}
				}
			}
			postShowPlateAndStickerSelected();
		}
		catch (RTSException leRTSEx)
		{
			leRTSEx.displayError(this);
		}
	}

	/**
	 * Use BarCode Scanner
	 * 
	 * @return boolean
	 */
	private boolean useBarCodeScanner()
	{
		if (caRenewalBarCodeData != null)
		{
			//current plate, exp mo, reg class, fee
			// defect 7473 
			if (caRenewalBarCodeData
				.getDocNo()
				.equals(gettxtDocno().getText().trim())
				&& caRenewalBarCodeData.getVin().equals(
					gettxtVin().getText().trim())
				&& caRenewalBarCodeData.getRegClassCd()
					== Integer.parseInt(gettxtRegClass().getText())
				&& Integer.parseInt(caRenewalBarCodeData.getRegExpMo())
					== Integer.parseInt(gettxtExpMo().getText())
				&& caRenewalBarCodeData.getRegPltNo().equals(
					gettxtCurrentPlateNo().getText().trim())
				&& caRenewalBarCodeData.getRenwlPrice().compareTo(
					new Dollar(gettxtFee().getText().trim()))
					== 0)
			{
				return true;
			}
			// end defect 7473 
		}
		return false;
	}
	/**
	 * Validate the entries
	 * 
	 * @return boolean continue processing
	 * @exception RTSException 
	 */
	public boolean validateEntries() throws RTSException
	{
		//indicate whether record is disk modify and not yet processed.
		boolean lbDiskNotProcd =
			caSubconToBeModified.isDiskEntry()
				&& !caSubconToBeModified.isProcessed();
		//improve performance to split up to basic and secondary checks
		//Basic field validations
		RTSException leRTSEx = new RTSException();
		scrValIssueYear(leRTSEx);
		// defect 9085 
		scrValSpclPlt(leRTSEx);
		scrValSpclOrgNo(leRTSEx);
		scrValSpclPltStkr(leRTSEx);
		// end defect 9085
		scrValDocNo(leRTSEx);
		// defect 7674
		// Null VIN allowed
		//scrValVin(rtsException);
		// end defect 7674 
		scrValIssueDate(leRTSEx);
		scrValNewPltNo(leRTSEx);
		scrValCurrPltNo(leRTSEx);
		scrValExpMo(leRTSEx);
		scrValRegClassCd(leRTSEx);
		scrValFee(leRTSEx);
		// defect 10392 
		if (getcomboPltTerm().isEnabled())
		{
			scrValPltTerm(leRTSEx);
		}
		if (gettxtPltExpMo().isEnabled())
		{
			scrValPltExpMo(leRTSEx);
		}
		if (gettxtPltExpYr().isEnabled())
		{
			scrValPltExpYr(leRTSEx);
		}
		// end defect 10392 
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			return false;
		}
		//secondary field validations, duplicate checks
		leRTSEx = new RTSException();
		RTSException leEx = null;
		//no need to validate dupl doc number if same as original record
		if (!gettxtDocno()
			.getText()
			.trim()
			.equals(caSubconToBeModified.getDocNo())
			|| lbDiskNotProcd)
		{
			if (lbDiskNotProcd)
			{
				leEx =
					SubcontractorHelper.validateDuplDocNo(
						caSubcontractorRenewalCacheData,
						gettxtDocno().getText().trim(),
						true);
			}
			else
			{
				leEx =
					SubcontractorHelper.validateDuplDocNo(
						caSubcontractorRenewalCacheData,
						gettxtDocno().getText().trim(),
						false);
			}
			if (leEx != null)
			{
				leRTSEx.addException(leEx, gettxtDocno());
				leEx = null;
			}
		}
		// defect 7674
		// do not validate Duplicate VIN if no VIN entered 
		if (!gettxtVin()
			.getText()
			.trim()
			.equals(caSubconToBeModified.getVIN())
			|| lbDiskNotProcd)
		{
			if (gettxtVin().getText().trim().length() != 0)
			{
				if (lbDiskNotProcd)
				{
					leEx =
						SubcontractorHelper.validateDuplVIN(
							caSubcontractorRenewalCacheData,
							gettxtVin().getText().trim(),
							true);
				}
				else
				{
					leEx =
						SubcontractorHelper.validateDuplVIN(
							caSubcontractorRenewalCacheData,
							gettxtVin().getText().trim(),
							false);
				}
				if (leEx != null)
				{
					leRTSEx.addException(leEx, gettxtVin());
					leEx = null;
				}
			}
		}
		// end defect 7674 
		if (gettxtNewPlateNo().isEnabled()
			&& (!gettxtNewPlateNo()
				.getText()
				.trim()
				.equals(caSubconToBeModified.getNewPltNo())
				|| lbDiskNotProcd))
		{
			if (lbDiskNotProcd)
			{
				leEx =
					SubcontractorHelper.validateDuplNewPlt(
						caSubcontractorRenewalCacheData,
						gettxtNewPlateNo().getText().trim(),
						true);
			}
			else
			{
				leEx =
					SubcontractorHelper.validateDuplNewPlt(
						caSubcontractorRenewalCacheData,
						gettxtNewPlateNo().getText().trim(),
						false);
			}
			if (leEx != null)
			{
				leRTSEx.addException(leEx, gettxtNewPlateNo());
				leEx = null;
			}
		}
		if (!gettxtCurrentPlateNo()
			.getText()
			.trim()
			.equals(caSubconToBeModified.getRegPltNo())
			|| lbDiskNotProcd)
		{
			if (lbDiskNotProcd)
			{
				leEx =
					SubcontractorHelper.validateDuplCurrPlt(
						caSubcontractorRenewalCacheData,
						gettxtCurrentPlateNo().getText().trim(),
						true);
			}
			else
			{
				leEx =
					SubcontractorHelper.validateDuplCurrPlt(
						caSubcontractorRenewalCacheData,
						gettxtCurrentPlateNo().getText().trim(),
						false);
			}
			if (leEx != null)
			{
				leRTSEx.addException(leEx, gettxtCurrentPlateNo());
				leEx = null;
			}
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			return false;
		}
		//confirmation
		return (confirmIssueDate() && confirmMinMaxFee());
	}
	/**
	 * Request focus when window opens
	 * 
	 * @param aaWE java.awt.event.WindowEvent
	 */
	public void windowOpened(WindowEvent aaWE)
	{
		//defect 5748
		//gettxtYear().requestFocus();
		//for error record on diskette validation
		if (caSubconToBeModified.isError() && !cbWindowOpened)
		{
			handleEnter();
			cbWindowOpened = true;
		}
		else
		{
			if (!cbWindowOpened)
			{
				gettxtYear().requestFocus();
			}
		}
		//end defect 5748
	}
	/**
	 * This method initializes ivjstcLblSpclPltTypeCode
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSpclPltTypeCode()
	{
		if (ivjstcLblSpclPltTypeCode == null)
		{
			ivjstcLblSpclPltTypeCode = new JLabel();
			ivjstcLblSpclPltTypeCode.setSize(198, 21);
			ivjstcLblSpclPltTypeCode.setText(
				"Special Plate Description");
			ivjstcLblSpclPltTypeCode.setLocation(13, 97);
		}
		return ivjstcLblSpclPltTypeCode;
	}
	/**
	 * This method initializes ivjstcLblOrganizationName
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOrganizationName()
	{
		if (ivjstcLblOrganizationName == null)
		{
			ivjstcLblOrganizationName = new JLabel();
			ivjstcLblOrganizationName.setBounds(261, 97, 198, 21);
			ivjstcLblOrganizationName.setText("Organization Name");
		}
		return ivjstcLblOrganizationName;
	}
	/**
	 * This method initializes ivjcomboSpclPltTypeCode
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboSpclPltTypeCode()
	{
		if (ivjcomboSpclPltTypeCode == null)
		{
			try
			{
				ivjcomboSpclPltTypeCode = new JComboBox();
				ivjcomboSpclPltTypeCode.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboSpclPltTypeCode.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboSpclPltTypeCode.setBackground(
					java.awt.Color.white);

				ivjcomboSpclPltTypeCode.setBounds(13, 120, 229, 23);
				ivjcomboSpclPltTypeCode.setName("ivjcomboSpclPltCode");
				// user code begin {1}
				ivjcomboSpclPltTypeCode.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboSpclPltTypeCode;
	}
	/**
	 * This method initializes ivjcomboOrganizationName
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboOrganizationName()
	{
		if (ivjcomboOrganizationName == null)
		{
			ivjcomboOrganizationName = new JComboBox();
			ivjcomboOrganizationName.setEditor(
				new javax
					.swing
					.plaf
					.metal
					.MetalComboBoxEditor
					.UIResource());
			ivjcomboOrganizationName.setRenderer(
				new javax
					.swing
					.plaf
					.basic
					.BasicComboBoxRenderer
					.UIResource());
			ivjcomboOrganizationName.setBackground(
				java.awt.Color.white);
			ivjcomboOrganizationName.setBounds(261, 120, 229, 23);
			ivjcomboOrganizationName.setName("ivjcomboSpclPltCode");
			// user code begin {1}
			ivjcomboOrganizationName.addActionListener(this);
			// user code end
		}
		return ivjcomboOrganizationName;
	}
	/**
	 * This method initializes ivjchkSpclPlt
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSpclPlt()
	{
		if (ivjchkSpclPlt == null)
		{
			ivjchkSpclPlt = new JCheckBox();
			ivjchkSpclPlt.setSize(105, 21);
			ivjchkSpclPlt.setText("Special Plate");
			ivjchkSpclPlt.setMnemonic(java.awt.event.KeyEvent.VK_C);
			ivjchkSpclPlt.setLocation(343, 20);
			// user code begin {1}
			ivjchkSpclPlt.addActionListener(this);
			// user code end
		}
		return ivjchkSpclPlt;
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
			ivjchkAddlSet.setSize(105, 21);
			ivjchkAddlSet.setText("Additional Set");
			ivjchkAddlSet.setLocation(450, 20);
			ivjchkAddlSet.setMnemonic(java.awt.event.KeyEvent.VK_L);
			// user code begin {1}
			ivjchkAddlSet.addActionListener(this);
			// user code end
		}
		return ivjchkAddlSet;
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
