package com.txdot.isd.rts.client.registration.ui;

import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.desktop.RTSDeskTop;
import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.registration.business.SubcontractorHelper;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.common.Transaction;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.event.BarCodeEvent;
import com.txdot.isd.rts.services.util.event.BarCodeListener;

/*
 * RegistrationSubcontractorRenewalREG007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Salvi		11/11/2002	Errors being accumulated were causing 
 * 							exceptions in other checks.
 * 							defect 5071 	
 * K Salvi		11/11/2002	missing bracket added to error message for 
 * 							duplicate plate number.
 * 							defect 5062 
 * K Salvi		11/18/2002	added a check for fees being null.
 * 							defect 5066 
 * K Salvi		11/21/2002	corrected verbiage of error message.
 * 							defect 5062
 * K Salvi		11/21/2002 	Corrected input types for docno, vin, etc 
 * 							to not allow spaces. 
 * 							defect 5072 
 * K Salvi		12/03/2002	Added null check for transactions that 
 * 							have been already deleted from list.
 * 							defect 5062 
 * K Salvi		02/03/2003	Capitalized VIN
 * 							defect 5356 
 * K Salvi		02/04/2003	Check for doc no to be exactly 17 digits 
 * 							long.
 * 							defect 5348 
 * K Salvi		03/10/2003	New plate text field was not getting 
 * 							enabled on diskette entry.
 * 							defect 5692 
 * K Salvi		03/10/2003	Validate doc no for 17 digits when  
 * 							scanning bar code as well as reading from 
 * 							disk.
 * 							defect 5751 
 * K Salvi		03/10/2003	Clear new plate field when a new plate is 
 * 							not being issued.
 * 							defect 5663
 * K Salvi		03/10/2003	Tabbing gets awkward with some processing
 * 							sequences.
 * 							defect 5734
 * K Salvi		03/19/2003	Error Records text was printing very high.
 * 							defect 5819 
 * K Harrell	03/20/2004	5.2.0 Merge.  See PCR 34 comments.Formated.
 * 							Ver 5.2.0
 * K Harrell	08/09/2004	Deselect Print checkbox when not "ATVS"
 *							modify itemStateChanged()
 *							defect 5975  Ver 5.2.1
 * K Harrell	08/17/2004	Correctly spell "validation" in RTSException
 *							defect      Ver 5.2.1
 * B Hargrove	08/18/2004	Modify to handle 15 fees for Ver 4 barcode
 *							modify handleRenwlBarCode()
 *							defect 7348  Ver 5.2.1
 * B Hargrove	08/20/2004	Print the Plate code and description if from
 *							barcode.
 *							modify populateDataObject()
 *							defect 7348  Ver 5.2.1
 * K Harrell	08/20/2004	Do not cleanup DocNo, VIN, RegPltNo
 *							defect 7473 Ver 5.2.1
 * K Harrell	08/21/2004	Set Print checked if enabled (and prior 
 *							printable item was printed if not 1st trans)
 *							modify populateDataObject(),
 *							 itemStateChanged(),setData(),stateChanged()
 *							defect 7469  Ver 5.2.1
 * K Harrell	08/21/2004	Assign last 6 digits of VIN to PltNo for
 *							ATVS.
 *							modify populateDataObject()
 *							defect 7476  Ver 5.2.1
 * K Harrell	08/23/2004	Disable New Plate appropriately.
 *							modify stateChanged()
 *							defect 7490  Ver 5.2.1
 * K Harrell	08/23/2004	Should save entered New Plate
 *							modify itemStateChanged(),stateChanged()
 *							defect 7490  Ver 5.2.1
 * J Rue		08/26/2004	Move the function of the Plt-Stkr-Plt/Stkr
 *							to from stateChange() to actionPerformed()
 *							add setchkBoxNewPltPrtStkr()
 *							deprecated stateChange()
 *							modify getradioPlt(), getradioStkr(),
 *							getradioPltStkr(), actionPerformed()
 *							defect 7506 Ver 5.2.1
 * K Harrell	08/29/2004	Disable Modify & Delete after return from
 *							REG009 (Modify)
 *							modify actionPerformed()
 *							defect 7474 Ver 5.2.1
 * K Harrell	08/29/2004	No exception was thrown on automaticAdd when
 *							barcode scanned with no VIN
 *							modify automaticAdd()
 *							defect 7472  Ver 5.2.1
 * K Harrell	08/30/2004	Do not pad VIN for ATVS
 *							modify populateDataObject()
 *							defect 7476  Ver 5.2.1
 * K Harrell	08/30/2004	Set mneumonic 'a' for New Plate
 *							as it was in 5.1.6
 *							defect 7516  Ver 5.2.1
 * Min Wang		08/31/2004	Fix multiple cursors.
 *							modify automaticAdd(), validateEntries()
 *							defect 7505  Ver 5.2.1
 * K Harrell	08/31/2004	Set mneumonic back to 'n' (Add uses 'a')
 *							defect 7516  Ver 5.2.1
 * T Pederson	09/03/2004	In setData(), Disable issue date when 
 *							processing data from diskette.
 *							defect 7537  Ver 5.2.1
 * B Hargrove	09/09/2004	'Clear all color' before disabling  
 *							New Plate field (ie: after error on Combo
 *							Plate, user then selected ATVS and the New
 *							Plate field was disabled and still red).
 *							modify itemStateChanged() 
 *							defect 7546  Ver 5.2.1
 * T Pederson	09/10/2004	In handleRenwlBarCode(), if plate type combo
 *							box is enabled, try to default choice to 
 *							match plate type of renewal scanned. If  
 *							disabled, blank out plate code.
 *							defect 7543 Ver 5.2.1
 * J Zwiener	09/17/2004	Capture AuditTrailTransid for MV_Func_Trans
 *							modify populateDataObject()
 *							defect 7553 Ver 5.2.1
 * K Harrell	10/11/2004	Modify for setVIN() change
 *							getDuplicateString(),populateData()
 *							defect   Ver 5.2.1
 * K Harrell	10/12/2004	Do not save Barcode Plate Code
 *							modify populateDataObject(),
 *							  handleRenwlBarCode() 
 *							defect 7543 Ver 5.2.1
 * K Harrell	10/20/2004	Reset Saved Plate No when next inventory
 *							item not available.
 *							itemStateChanged(),setData()
 *							defect 7650 Ver 5.2.1
 * K Harrell	10/29/2004	Assign isWorking = true/doneWorking() in 
 *							handleRenwlBarCode()
 *							defect 7552 Ver 5.2.1 Fix 1
 * K Harrell	10/29/2004	Cursor Management on Modify
 *							modify actionPerformed(),handleModifyEntry()
 *							defect 7627 Ver 5.2.1 Fix 1
 * K Harrell	10/29/2004	VIN no longer required
 *							deprecate scrValVin() 
 *							modify validateEntries() 
 *							defect 7674 Ver 5.2.1 Fix 1
 * K Harrell	11/08/2004	Issue Date Validation
 *							modify validateEntries(),
 *							scrValIssueDate()
 *							defect 7656 Ver 5.2.1 Fix 1
 * K Harrell	12/01/2004	Upon bundle restore, populate Issue Date
 *							with that of the last entered transaction.
 *							modify setData()
 *							defect 7725 Ver 5.2.2
 * K Harrell	01/27/2005	Change I->1, O->0 in VIN.
 *							modify focustLost(),gettxtVin() 
 *							defect 7930 Ver 5.2.2
 * K Harrell	02/01/2005	Use boolean cbAllTransPosted to determine 
 *							if all transactions posted vs. 
 *							cbUnProcessedTrans
 *							add cbAllTransPosted
 *							modify actionPerformed(),
 *							finalClientSideCheck()
 *							defect 7900 Ver 5.2.2 
 * Ray Rowehl	02/08/2005	Change import for Transaction
 * 							modify import
 * 							defect 7705 Ver 5.2.3
 * K Harrell	02/22/2005	Can not complete trans if inventory doesn't
 *							exist for subcontractor.
 * 							modify actionPerformed() 
 *							defect 8031 Ver 5.2.2 Fix 2	
 * B Hargrove	03/10/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							defect 7894 Ver 5.2.3
 * B Hargrove	03/21/2005	Change var isWorking to isWorking() 
 *							modify handleRenwlBarCode()
 *							defect 7894 Ver 5.2.3
 * K Harrell	03/22/2005	Merge 8031 (2/22/2005 Ver 5.2.2 Fix 2)
 * 							modify actionPerformed()
 * 							Ver 5.2.3 
 * B Hargrove	03/31/2005	Comment out setNextFocusableComponent() 
 *							modify getlstSubconItems(), setData()
 *							defect 7894 Ver 5.2.3
 * K Harrell	05/04/2005	Java 1.4 Work
 * 							deprecate getDescFromDropDown(),
 * 							getNextYear(),preSelectCombo(),
 * 							recalInvalidInventoryCount(),
 * 							reverseVector(),scrValVin()
 * 							delete isHoldingItems()
 * 							defect 8020 Ver 5.2.3
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * S Johnston	06/15/2005	Fixed the hotkeys for the frame
 * 							Fixed arrow keys on button panel
 * 							modify keyReleased(), itemStateChanged(),
 * 							and getchkPrintStkr()
 * 							defect 7491 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7899  Ver 5.2.3                 
 * B Hargrove	06/21/2005	modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							delete implements KeyListener
 *							defect 7894 Ver 5.2.3
 * Jeff S.		12/15/2005	Added a temp fix for the JComboBox problem.
 * 							modify handleRenwlBarCode(), 
 * 								populatePltDropDown(), 
 * 								populateStkrDropDown(),
 * 								setchkBoxNewPltPrtStkr(), stateChanged()
 * 							defect 8479 Ver 5.2.3 
 * T. Pederson	12/21/2005	Moved setting default focus to initialize.
 * 							delete windowOpened()
 * 							modify initialize() 	
 * 							defect 8494 Ver 5.2.3
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.  Set RequestFocus to
 * 							false so that when the hot keys are chosen
 * 							on a radio field the focus does not stay on 
 * 							that radio button.
 * 							remove keyPressed(), keyReleased()
 * 							modify initialize()
 * 							defect 7894 Ver 5.2.3
 * K Harrell	01/12/2006	Adjusted length of plate and sticker combo
 * 							boxes to accommodate maximum lengths
 *  						defect 7894 Ver 5.2.3
 * K Harrell	01/13/2006	Remove focus manipulation required in 
 * 							Java 1.3
 * 							modify actionPerformed(),handleModifyEntry()
 * 							defect 7894 Ver 5.2.3
 * K Harrell	02/06/2006	Focus & alignment work
 * 							modify getJPanel2, handleModifyEntry()
 * 							defect 7894 Ver 5.2.3
 * K Harrell	02/09/2006	Restored 5.2.2 Code vs. reorganized code
 * 							modify actionPerformed(),finalDisketteValidations()
 * 							defect 7894 Ver 5.2.3
 * K Harrell	04/13/2006	replace isWorking() w/ setWorking(true) 
 * 							modify handleRenwlBarCode()
 * 							defect 8686 Ver 5.2.3  
 * Ray Rowehl	10/05/2006	Modify check for FeeCalcCat = 0 to also 
 * 							check for 4.  For Exempts.
 * 							modify diskValRegClassCd(), scrValRegClassCd()
 * 							defect 8900 Ver FallAdminTables 
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
 * K Harrell	10/25/2007	Special Plate Processing
 * 							add ivjstcLblSpclPltTypeCode, 
 * 							 ivjstcLblOrganizationName, ivjchkSpclPlt, 
 * 							 ivjchkAddlSet, ivjcomboOrganizationName, 
 * 							 ivjcomboSpclPltTypeCode,ciSpclPltIndi,
 * 							 ciAddlSetIndi, OMIT_PLPDLR,csOrgNo, 
 * 							 csSpclRegPltCd, cvOrgNo, svSpclPltDesc
 *							add buildSpclPltVector(), 
 *							 getOrgNoFromDropDown(), 
 *							 getSpclRegPltCdFromDropdown(),
 *							 populateOrganizationNameDropDown(), 
 *							 populateSpclPltDropDown(), 
 *							 preSelectAddlSetChkBox(), resetSpclPlates(),
 *							 scrValSpclPlt(), scrValSpclPltStkr(), 
 *							 scrValSpclOrgNo() 
 *							modify actionPerformed(), diskValRegClassCd(),
 *							 getchkPrintStkr(), getstcLblVin(), 
 *							 getstcLblCurrentPlateNo(), getstcLblExpMo(),
 *							 getstcLblFee(), getstcLblOrganizationName(),
 *							 getstcLblSpclPltTypeCode(), 
 *							 getstcLblRegClass(), initialize(), 
 *							 populateDataObject(), setData(), 
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
 * 							defect 9631 Ver Defect POS A
 * K Harrell	01/14/2009	Do not allow clerk to complete a set-aside 
 * 							bundle from a different date.
 * 							add cbCurrentDate 
 * 							modify setData()
 * 							defect 8772 Ver Defect_POS_D 
 * K Harrell	03/15/2010	Validate IssueDate Year: Either prior or 
 * 							current year. 
 * 							add caToday 
 * 							modify scrValIssueDate(), diskValIssueDate() 
 * 							defect 10355 Ver POS_640 
 * T Pederson	04/26/2010 	Add plate expiration mo/yr and plate term
 * 							fields to screen. 
 * 							Change MAX_FEE to 1000.00
 * 							modify populateDataObject(), initialize(),
 * 							actionPerformed(), handleRenwlBarCode(),
 * 							resetSpclPlates(), handleVendorPlate(),
 * 							setData()
 *							defect 10392  Ver POS_640
 * T Pederson	04/30/2010 	Remove ATVS from plate code drop down  
 * 							vector. 
 *							defect 10453  Ver POS_640
 * T Pederson	05/12/2010 	Validation for entry of plate expiration 
 * 							month referencing wrong variable.  
 * 							modify scrValPltExpMo()
 *							defect 10488  Ver POS_640
 * T Pederson	06/30/2010 	Remove default of plate expiration yr 
 * 							so it doesnt wipe out the scanned in value. 
 * 							modify handleVendorPlate()
 *							defect 10392  Ver POS_640
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
 * Frame for Registration Subcontractor Renewal REG007
 *
 * @version	POS_700			06/13/2012
 * @author 	Nancy Ting
 * <br>Creation Date:		12/06/2001 09:49:14 
 */
public class FrmRegistrationSubcontractorRenewalREG007
	extends RTSDialogBox
	implements
		BarCodeListener,
		ActionListener,
		FocusListener,
		ItemListener,
		ChangeListener,
		ListSelectionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JCheckBox ivjchkPrintStkr = null;
	private JComboBox ivjcomboStickerCode = null;
	private JComboBox ivjcomboPltCode = null;
	private JLabel ivjstcLblPltItemCode = null;
	private JLabel ivjlblSubtotal = null;
	private JLabel ivjstcLblCurrentPlateNo = null;
	private JLabel ivjstcLblExpMo = null;
	private JLabel ivjstcLblFee = null;
	private JLabel ivjstcLblIssueDate = null;
	private JLabel ivjstcLblNewPltNo = null;
	private JLabel ivjstcLblRegClass = null;
	private JLabel ivjstcLblStickerItemCode = null;
	private JLabel ivjstcLblSubcontractorId = null;
	private JLabel ivjstcLblSubtotal = null;
	private JLabel ivjstcLblYear = null;
	private JLabel ivjlblSubcontractor = null;
	private JLabel ivjlblstcTransCount = null;
	private JLabel ivjlblTransEntered = null;
	private JLabel ivjlblDocno = null;
	private JLabel ivjlblErrorRecord = null;
	private JLabel ivjlblVin = null;
	private JLabel ivjstcLblErrRecord = null;
	private JPanel ivjFrmRegistrationSubcontractorRenewalREG007ContentPane1 =
		null;
	private JPanel ivjJPanel6 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel5 = null;
	private JPanel ivjJPanel2 = null;
	private JRadioButton ivjradioPlt = null;
	private JRadioButton ivjradioPltStkr = null;
	private JRadioButton ivjradioStkr = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSButton ivjbtnAddTrans = null;
	private RTSButton ivjbtnDeleteEntry = null;
	private RTSButton ivjbtnDraftReport = null;
	private RTSButton ivjbtnModifyEntry = null;
	private RTSDateField ivjtxtIssueDate = null;
	private RTSInputField ivjtxtCurrentPlateNo = null;
	private RTSInputField ivjtxtExpMo = null;
	private RTSInputField ivjtxtFee = null;
	private RTSInputField ivjtxtRegClass = null;
	private RTSInputField ivjtxtYear = null;
	private RTSInputField ivjtxtNewPlateNo = null;
	private RTSInputField ivjtxtDocno = null;
	private RTSInputField ivjtxtVin = null;
	private RTSTable ivjlstSubconItems = null;
	private TMREG007 cTableModel = null;

	// boolean
	private boolean cbIsAcceptingBarCodes = true;
	private boolean cbDiskEntry;
	private boolean cbReEntry;
	private boolean cbDiffCnty;
	private boolean cbPrintChecked = true;
	private boolean cbAllTransPosted = false;
	private boolean cbIssueDateValid = true;

	// defect 9097 
	private boolean cbValidYear = false;
	// end defect 9097

	// defect 8772
	private boolean cbCurrentDate = true;
	// end defect 8772   

	// int
	private int ciErrorSetType;
	private int ciSelectedModifyRow = 0;
	private int ciStoreLastTransIssueDate;
	private int ciUnProcDiskRenwlCount = 0;

	// Object
	private BarCodeScanner caBarCodeScanner;
	private HashSet chsUnProcIndex = new HashSet();
	private HashSet chsInventoryCheckList = new HashSet();
	private RenewalBarCodeData caRenewalBarCodeData;
	private Set csErrorSet;
	private SubcontractorRenewalCacheData caSubcontractorRenewalCacheData =
		null;

	// defect 10355 
	private RTSDate caToday =
		new RTSDate(
			RTSDate.YYYYMMDD,
			RTSDate.getCurrentDate().getYYYYMMDDDate());
	// end defect 10355 

	// String 
	private String csBarCodePltCd;
	private String csNewPlateNo = "";
	// store the duplicate string value
	private String csErrInfo;

	// Vector 
	protected static Vector svPltCd = null;
	protected static Vector svPltSkrCd = null;

	private Vector cvRenewalQueue;

	// defect 9085 
	private JLabel ivjstcLblSpclPltTypeCode = null;
	private JLabel ivjstcLblOrganizationName = null;

	private JCheckBox ivjchkSpclPlt = null;
	private JCheckBox ivjchkAddlSet = null;

	private JComboBox ivjcomboOrganizationName = null;
	private JComboBox ivjcomboSpclPltTypeCode = null;

	private int ciSpclPltIndi = 0;
	private int ciAddlSetIndi = 0;

	private String csOrgNo = "";
	private String csSpclRegPltCd = "";

	private Vector cvOrgNo = null;
	protected static Vector svSpclPltDesc = new Vector();

	private static boolean OMIT_PLPDLR = true;
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

	// Constant 
	public static final Dollar BUNDLE_MAX_AMT = new Dollar("99999.99");
	public static final Dollar MIN_FEE = new Dollar("25.00");
	// defect 10392 
	//public static final Dollar MAX_FEE = new Dollar("100.00");
	public static final Dollar MAX_FEE = new Dollar("1000.00");
	// end defect 10392 

	public static final int PROCSNG_CD = 1;
	public static final int MAX_BUNDLE_AMT = 1999;
	public static final int YR_BEGIN = 1900;
	public static final int YR_END = 2100;

	public static final String STKR_TRCKNG_TYPE = "S";
	public static final String PLT_TRCKNG_TYPE = "P";
	public static final String DEFAULT_STKR_CD = "WS";
	public static final String SEPARATOR = "-";
	public static final String ATVS_CD = "ATVS";

	static {
		//Plate only code
		svPltCd = new Vector();
		// defect 10453  
		//svPltCd.addElement("ATVS");
		// end defect 10453  
		svPltCd.addElement("CP");
		svPltCd.addElement("DRP");
		svPltCd.addElement("FP");
		svPltCd.addElement("SCP");
		//most common Plate Code for Plate and Stkr
		svPltSkrCd = new Vector();
		svPltSkrCd.addElement("PSP");
		svPltSkrCd.addElement("TKP");
		svPltSkrCd.addElement("TLP");
		
		// defect 10392 
		svPltTerm = new Vector();
		svPltTerm.addElement("1");
		svPltTerm.addElement("5");
		svPltTerm.addElement("10");
		// end defect 10392
		// defect 10644 
		svPltTerm.addElement("25");
		// end defect 10644

	}
	// defect 9085 
	static {
		buildSpclPltVector();
	}
	// end defect 9085  

	//to keep track of the error type in the error set (doc no, vin, 
	// new plate or current plate)
	private static final int ERR_DOC_NO = 1;
	private static final int ERR_VIN = 2;
	private static final int ERR_NEW_PLT = 3;
	private static final int ERR_CURR_PLT = 4;
	private static final int ERR_CLIENT_VAL = 5;
	private static final int ERR_INV_CHECK = 6;

	private static final String STICKER = "Sticker";
	private static final String PLATE = "Plate";
	private static final String PLATESTICKER = "Plate & Sticker";

	/**
	 * FrmRegistrationSubcontractorRenewal constructor comment.
	 */
	public FrmRegistrationSubcontractorRenewalREG007()
	{
		super();
		initialize();
	}
	/**
	 * FrmRegistrationSubcontractorRenewal constructor comment.
	 * 
	 * @param aaOwner java.awt.Frame
	 */
	public FrmRegistrationSubcontractorRenewalREG007(JDialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmRegistrationSubcontractorRenewal constructor comment.
	 * 
	 * @param aaOwner java.awt.Frame
	 */
	public FrmRegistrationSubcontractorRenewalREG007(JFrame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when Enter/Cancel/Help/Draft Report
	 * Modify Entry/Plate/Sticker/Plate and Sticker radio Button is
	 * being pressed
	 * 
	 * @param aaAE ActionEvent 
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
			if (aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				handleCancel();
				Transaction.reset();
			}
			else if (aaAE.getSource() == getbtnAddTrans())
			{
				clearErrorSet();
				if (validateEntries())
				{
					populateDataObject();
					getbtnAddTrans().requestFocus();
					getController().processData(
						VCRegistrationSubcontractorRenewalREG007
							.PROCESS_RENWL,
						caSubcontractorRenewalCacheData);

				}
				else
				{
					return;
				}
			}
			else if (aaAE.getSource() == getbtnDeleteEntry())
			{
				handleDeleteEntry();
				getbtnDeleteEntry().setEnabled(false);
				getbtnModifyEntry().setEnabled(false);
				getlstSubconItems().unselectAllRows();
				focusTable();
				if (caSubcontractorRenewalCacheData
					.getSubconTransData()
					.size()
					== 0)
				{
					gettxtDocno().requestFocus();
				}
				else
				{
					getlstSubconItems().requestFocus();
				}

				return;
			}
			else if (aaAE.getSource() == getbtnModifyEntry())
			{
				handleModifyEntry();
				// defect 7627
				getbtnModifyEntry().setEnabled(false);
				getbtnDeleteEntry().setEnabled(false);
				getlstSubconItems().unselectAllRows();
				// end defect 7627

				return;
			}
			else if (aaAE.getSource() == getbtnDraftReport())
			{
				focusTable();
				getController().processData(
					VCRegistrationSubcontractorRenewalREG007
						.GENERATE_REPORT,
					caSubcontractorRenewalCacheData);
				return;
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				// defect 8177

				if (cbDiskEntry)
				{
					RTSHelp.displayHelp(RTSHelp.REG007A);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.REG007B);
				}
				return;
				// end defect 8177
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				clearErrorSet();
				Transaction.setRunningSubtotal(
					caSubcontractorRenewalCacheData.getRunningTotal());
				if (finalDisketteValidations())
				{
					// defect 7900
					// Use new boolean, cbUnPostedTrans to, to determine
					// if all transactions are posted and transaction
					// set (bundle) is ready to be completed

					//if no unposted diskette renewals
					//if (ciUnProcDiskRenwlCount == 0)
					if (cbAllTransPosted)
					{
						int liReturnStatus =
							new RTSException(
								RTSException.CTL001,
								"Total is $ "
									+ Transaction
										.getRunningSubtotal()
										.toString(),
								"").displayError(
								this);
						if (liReturnStatus == RTSException.YES)
						{
							getController().processData(
								AbstractViewController.ENTER,
								caSubcontractorRenewalCacheData);
						}
					}
					// end defect 7900 
					else
					{
						// other diskette scenarios
						// there are unposted diskette renewals
						// defect 8031
						// Also include if ciUnProcDiskRenwlCount = 0, 
						// i.e. if have all have been through 
						// the Modify Screen 
						//if (csInventoryCheckList.size()	
						// == cSubcontractorRenewalCacheData.
						// getDiskHeldPltList().size())
						if (chsInventoryCheckList.size()
							== caSubcontractorRenewalCacheData
								.getDiskHeldPltList()
								.size()
							|| ciUnProcDiskRenwlCount == 0)
						{
							// no need inventory validation, 
							// i.e. inventory found or processed 
							// through Modify
							// just needed to add to database
							caSubcontractorRenewalCacheData
								.setUnProcsList(
								chsUnProcIndex);
							getController().processData(
								VCRegistrationSubcontractorRenewalREG007
									.ADD_AND_END_TRANS,
								caSubcontractorRenewalCacheData);
						}
						// end defect 8031 
						else if (
							caSubcontractorRenewalCacheData
								.getDiskHeldPltList()
								.size()
								== 0)
						{
							//need inventory validation
							//diskette inventory check
							caSubcontractorRenewalCacheData
								.setUnProcsList(
								chsUnProcIndex);
							caSubcontractorRenewalCacheData
								.setInventoryCheckList(
								chsInventoryCheckList);
							getController().processData(
								VCRegistrationSubcontractorRenewalREG007
									.CHECK_DISK_INVENTORY,
								caSubcontractorRenewalCacheData);
						}
						else
						{
							//already have inventory validation but not 
							//all are confirmed in INV014 yet
							new RTSException(
								RTSException.FAILURE_MESSAGE,
								"Not all renewals are validated."
									+ "  Please select modify for any"
									+ " renewals that contain a red icon.",
								"").displayError(
								this);
							focusTable();
						}
					}
				}
				return;
			}
			// defect 7506
			//  Moved the Plate/Sticker/Plate-Sticker radio button
			//  functions from stateChange() to actionPerformed()
			// defect 7489
			// Save entered Plate No
			//PLATE
			else if (aaAE.getSource() instanceof JRadioButton)
			{
				if (aaAE.getSource() == getradioPlt())
				{
					// Save New Plate Number
					if (gettxtNewPlateNo().isEnabled())
					{
						csNewPlateNo = gettxtNewPlateNo().getText();
					}
					getcomboStickerCode().removeAllItems();
					populatePltDropDown(true);

					getcomboStickerCode().setEnabled(false);
					getcomboPltCode().setEnabled(true);
					getcomboPltCode().requestFocus();
					gettxtNewPlateNo().setEnabled(true);
					setchkBoxNewPltPrtStkr();
					// defect 9085
					getstcLblStickerItemCode().setEnabled(false);
					getstcLblPltItemCode().setEnabled(true);
					resetSpclPlates();
					// end defect 9085 
				}
				// STICKER 	
				else if (aaAE.getSource() == getradioStkr())
				{
					// Save New Plate Number
					if (gettxtNewPlateNo().isEnabled())
					{
						csNewPlateNo = gettxtNewPlateNo().getText();
					}
					getcomboPltCode().removeAllItems();
					populateStkrDropDown();
					getcomboStickerCode().setEnabled(true);
					getcomboPltCode().setEnabled(false);
					getcomboStickerCode().requestFocus();
					getstcLblNewPltNo().setEnabled(false);
					gettxtNewPlateNo().setEnabled(false);
					setchkBoxNewPltPrtStkr();
					// defect 9085
					getstcLblStickerItemCode().setEnabled(true);
					getstcLblPltItemCode().setEnabled(false);
					getchkSpclPlt().setEnabled(true);
					// end defect  
				}
				// PLATE STICKER 			
				else if (aaAE.getSource() == getradioPltStkr())
				{
					// Save New Plate Number
					if (gettxtNewPlateNo().isEnabled())
					{
						csNewPlateNo = gettxtNewPlateNo().getText();
					}
					populatePltDropDown(false);
					populateStkrDropDown();
					getcomboPltCode().setEnabled(true);
					getcomboStickerCode().setEnabled(true);
					getcomboPltCode().requestFocus();
					setchkBoxNewPltPrtStkr();
					// defect 9085
					getstcLblStickerItemCode().setEnabled(true);
					getstcLblPltItemCode().setEnabled(true);
					resetSpclPlates();
					// end defect 9085 
				}
				// end defect 7489
				// end defect 7506
			}
			//	defect 9085
			else if (aaAE.getSource() == getchkSpclPlt())
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
			else if (aaAE.getSource() == getchkPrintStkr())
			{
				if (getchkPrintStkr().isEnabled())
				{
					cbPrintChecked = getchkPrintStkr().isSelected();
				}
			}
			else if (aaAE.getSource() == getchkAddlSet())
			{
				ciAddlSetIndi = (getchkAddlSet().isSelected() ? 1 : 0);
			}
			else if (aaAE.getSource() == getcomboSpclPltTypeCode())
			{
				int liIndex =
					getcomboSpclPltTypeCode().getSelectedIndex();
				if (liIndex != -1)
				{
					getSpclRegPltCdFromDropDown(
						getcomboSpclPltTypeCode().getSelectedIndex());
					handleAddlSet();

					populateOrganizationNameDropDown();
					getcomboSpclPltTypeCode().requestFocus();
				}
				// defect 10392 
				handleVendorPlate(null);
				// end defect 10392 
			}
			else if (aaAE.getSource() == getcomboOrganizationName())
			{
				int liIndex =
					getcomboOrganizationName().getSelectedIndex();
				if (liIndex != -1)
				{
					getOrgNoFromDropDown(
						getcomboOrganizationName().getSelectedIndex());
				}
			} // end defect 9085 
		}
		catch (RTSException aeRTSEx)
		{
			//getbtnAddTrans().requestFocus();
			aeRTSEx.displayError(this);
			aeRTSEx.getFirstComponent().requestFocus();
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * When bar code is scanned and all required fields are populated,
	 *  the add transaction operation is automatically initiated
	 * 
	 * @return boolean
	 */
	private boolean automaticAdd()
	{
		//defect 7505
		getbtnAddTrans().requestFocus();
		//end defect 7505

		if (validateEntries())
		{
			populateDataObject();
			getController().processData(
				VCRegistrationSubcontractorRenewalREG007.PROCESS_RENWL,
				caSubcontractorRenewalCacheData);
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Invoked when a barcode is scanned.
	 * 
	 * @param aaBCE BarCodeEvent
	 */
	public void barCodeScanned(BarCodeEvent aaBCE)
	{
		// if focus is in list box, don't allow barcodes to be scanned
		if (!cbIsAcceptingBarCodes)
		{
			return;
		}
		clearAllColor(this);
		Object laBarCodeData = aaBCE.getBarCodeData();
		try
		{
			if (laBarCodeData instanceof RenewalBarCodeData)
			{
				RenewalBarCodeData laRenewalBarCodeData =
					(RenewalBarCodeData) laBarCodeData;
				caRenewalBarCodeData = laRenewalBarCodeData;
				if (cvRenewalQueue.size() == 0)
				{
					handleRenwlBarCode(laRenewalBarCodeData);
				}
				else
				{
					cvRenewalQueue.add(laRenewalBarCodeData);
				}
			}
			else if (laBarCodeData instanceof PlateBarCodeData)
			{
				PlateBarCodeData laPlateBarCodeData =
					(PlateBarCodeData) laBarCodeData;
				handlePlateBarCode(laPlateBarCodeData);
			}
			else if (laBarCodeData instanceof RTSException)
			{
				displayScannerMsg();
			}
		}
		catch (RTSException aeRTSEx)
		{

			if (aeRTSEx.getMessage() != null
				&& aeRTSEx.getMessage().equals(
					SubcontractorHelper.DUPL_DOC))
			{
				RTSException leEx = new RTSException();
				leEx.addException(aeRTSEx, gettxtDocno());
				leEx.displayError(this);
				leEx.getFirstComponent().requestFocus();
			}
			else
			{
				aeRTSEx.displayError(this);
				if (aeRTSEx.getFirstComponent() != null)
				{
					aeRTSEx.getFirstComponent().requestFocus();
				}
			}
			repaintComponents();
			return;
		}
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
	 * Check if the input string has maximum two decimal places.
	 * Also make sure some number is entered.
	 * 
	 * @param asString String
	 * @return boolean 
	 */
	private boolean check2Decimals(String asString)
	{
		StringTokenizer laStrToken = new StringTokenizer(asString, ".");
		// make sure there is at least something besides a "." entered.
		if (laStrToken.countTokens() < 1)
		{
			return false;
		}
		if (laStrToken.countTokens() == 1)
		{
			return true;
		}
		else if (laStrToken.countTokens() > 2)
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
	 * Clean up the barcode string by removing unnecessary zeroes
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
	 * Clear the error indicators
	 * 
	 */
	private void clearErrorSet()
	{
		if (csErrorSet != null)
		{
			SortedMap laTransInfo =
				caSubcontractorRenewalCacheData.getSubconTransData();
			Iterator laIterator = csErrorSet.iterator();
			while (laIterator.hasNext())
			{
				SubcontractorRenewalData laSubconData =
					(SubcontractorRenewalData) laTransInfo.get(
						laIterator.next());
				// defect 5062
				// Check for a null here since the record might have 
				// already been deleted from transInfo.
				if (laSubconData != null)
				{
					laSubconData.setError(false);
				} // end defect 5062

			}
			csErrorSet = null;
			ciErrorSetType = 0;
			getlblErrorRecord().setVisible(false);
			getstcLblErrRecord().setVisible(false);
		}
	}

	/**
	 * Confirm Issue Date - Non disk 
	 * 
	 * @return boolean
	 */
	private boolean confirmIssueDate()
	{
		int liIssueAMDate = gettxtIssueDate().getDate().getAMDate();
		int liCurrentAMDate = new RTSDate().getAMDate();
		//PROC confirm SubconIssueAMDate 90 days old or older
		if (liIssueAMDate < (liCurrentAMDate - 90))
		{
			//only pop up message if not pop up before
			if (ciStoreLastTransIssueDate
				!= gettxtIssueDate().getDate().getYYYYMMDDDate())
			{
				//Confirm Message
				int liReturnStatus =
					new RTSException(
						RTSException.CTL001,
						"Issue Date is over 90 days old, confirm"
							+ " issue date.",
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
	 * Confirm Min / Max - Non disk
	 * 
	 * @return boolean
	 */
	private boolean confirmMinMaxFee()
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
	 * Confirm Issue Date on Disk
	 * 
	 * @param  aaSubconData
	 * @return boolean  
	 */
	private boolean diskConfirmIssueDate(SubcontractorRenewalData aaSubconData)
	{
		int liIssueAMDate = aaSubconData.getSubconIssueDate();
		int liCurrentAMDate = new RTSDate().getAMDate();
		//PROC confirm SubconIssueAMDate 90 days old or older
		if (liIssueAMDate < (liCurrentAMDate - 90))
		{
			//only pop up message if not pop up before
			if (ciStoreLastTransIssueDate != liIssueAMDate)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Confirm MinMaxFee on Disk 
	 * 
	 * @param  aaSubconData  
	 * @return boolean 
	 */
	private boolean diskConfirmMinMaxFee(SubcontractorRenewalData aaSubconData)
	{
		//Confirm Input Renewal Fee to be between Min Fee and Max Fee
		Dollar laFee = aaSubconData.getRenwlTotalFees();
		if (laFee.compareTo(MIN_FEE) < 0)
		{
			return false;
		}
		else if (laFee.compareTo(MAX_FEE) > 0) 
		{
			return false;
		}
		return true;
	}
	/**
	 * Validate Disk Doc No length 
	 * 
	 * @param  aaSubconData
	 * @return boolean
	 */
	private boolean diskValDocNo(SubcontractorRenewalData aaSubconData)
	{
		String lsDocNo = aaSubconData.getDocNo();
		if (lsDocNo == null || lsDocNo.length() < 17)
		{
			return false;
		}
		return true;
	}

	/**
	 * Validate the expiration month
	 * 
	 * @param  aaSubconData  
	 * @return boolean
	 */
	private boolean diskValExpMo(SubcontractorRenewalData aaSubconData)
	{
		RTSException leRTSEx =
			SubcontractorHelper.validateMo(
				String.valueOf(aaSubconData.getRegExpMo()));
		if (leRTSEx != null)
		{
			return false;
		}
		return true;
	}

	/**
	 * Validate the Fees
	 * 
	 * @param  aaSubconData  
	 * @return boolean 
	 */
	private boolean diskValFee(SubcontractorRenewalData aaSubconData)
	{
		//total renewal fees must be > 0
		Dollar laFee = aaSubconData.getRenwlTotalFees();
		if (!check2Decimals(laFee.toString())
			|| laFee.compareTo(CommonConstant.ZERO_DOLLAR) <= 0
			|| laFee.compareTo(new Dollar("9999.99")) > 0)
		{
			return false;
		}
		//input renewal fee greater than MinRegFee + ReflectnFee
		//if reg class is empty, this check cannot be performed
		CommonFeesData laCommonFeesData =
			CommonFeesCache.getCommonFee(
				Integer.parseInt(aaSubconData.getRegClassCd()),
				aaSubconData.getSubconIssueDate());
		//note laCommonFeesData should not be null since validate in
		// validateRegCd
		Dollar laMinRegFee =
			laCommonFeesData.getMinRegFee().add(
				laCommonFeesData.getReflectnFee());
		if (laFee.compareTo(laMinRegFee) == -1)
		{
			return false;
		}
		return true;
	}

	/**
	 * Validate the issue date
	 * 
	 * @param  aaSubconData  
	 * @return boolean 
	 */
	private boolean diskValIssueDate(SubcontractorRenewalData aaSubconData)
	{
		// defect 10355 
		// Streamline for only one return ALTHOUGH unnecessary as 
		// validated on REG006 and disabled on REG007. 
		// Note:  new RTSDate(aaSubconData.getSubconIssueDate()) doesn't work. 

		//		RTSDate laDate = new RTSDate(aaSubconData.getSubconIssueDate());
		//		int liIssueAMDate = laDate.getAMDate();
		//		int liCurrentAMDate = new RTSDate().getAMDate();
		//
		//		//PROC Validate SubconIssueAMDate for 0, null or in the future
		//		if (!(liIssueAMDate > 0 && liIssueAMDate <= liCurrentAMDate))
		//		{
		//			return false;
		//		}
		//		return true;

		RTSDate laTransDate =
			new RTSDate(
				RTSDate.YYYYMMDD,
				aaSubconData.getSubconIssueDate());

		boolean lbValid =
			caToday.compareTo(laTransDate) != -1
				&& laTransDate.getYear() >= caToday.getYear() - 1;

		return lbValid;
		// defect 10355 
	}

	/**
	 * Validate issue year
	 * 
	 * @param  aaSubconData
	 * @return boolean  
	 */
	private boolean diskValIssueYear(SubcontractorRenewalData aaSubconData)
	{
		int liYr = aaSubconData.getNewExpYr();
		if (liYr > YR_END || liYr < YR_BEGIN)
		{
			return false;
		}
		int liCurrYr = new RTSDate().getYear();
		if (!(liYr >= liCurrYr && liYr <= liCurrYr + 2))
		{
			return false;
		}
		return true;
	}

	/**
	 * Validate new plate number
	 * 
	 * @param aaSubconData
	 * @return boolean  
	 */
	private boolean diskValNewPltNo(SubcontractorRenewalData aaSubconData)
	{
		if (aaSubconData.getPltItmCd() != null)
		{
			if (!SubcontractorHelper
				.validatePltStkrEntry(
					SubcontractorHelper.PLT_MASK,
					aaSubconData.getPltItmCd()))
			{
				return false;
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
					aaSubconData.getPltItmCd());
				if (aaSubconData.getStkrItmCd() != null)
				{
					laProcessInventoryData.setInvItmYr(0);
				}
				else
				{
					laProcessInventoryData.setInvItmYr(
						aaSubconData.getNewExpYr());
				}
				laProcessInventoryData.setInvQty(1);
				laProcessInventoryData.setInvItmNo(
					aaSubconData.getNewPltNo());
				try
				{
					laValidateInventoryPattern.validateItmNoInput(
						laProcessInventoryData
							.convertToInvAlloctnUIData(
							laProcessInventoryData));
				}
				catch (RTSException aeRTSEx)
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Validate Reg Class code
	 * 
	 * @param aaSubconData
	 * @return boolean  
	 */
	private boolean diskValRegClassCd(SubcontractorRenewalData aaSubconData)
	{
		// defect 9362 
		// Use Common Method for validation
		return SubcontractorHelper.validateRegClassCd(
			Integer.parseInt(aaSubconData.getRegClassCd()),
			aaSubconData.getSubconIssueDate());
		// end defect 9362   
	}
	/**
	 * Display scanner msg
	 * 
	 */
	private void displayScannerMsg()
	{
		cvRenewalQueue.clear();
		new RTSException(
			RTSException.FAILURE_MESSAGE,
			"Barcode data is not captured properly. Please check"
				+ " your work and continue scanning!",
			"").displayError(
			this);
	}

	/**
	 * Check for duplicate doc no, vin, new plate, current plate when
	 * user presses Enter on disk entry mode
	 * 
	 * @return boolean
	 */
	private boolean finalCheckForDiskDuplicates()
	{
		//validate duplicates
		//doc number
		Set laSet =
			SubcontractorHelper.getFirstDuplicateSet(
				caSubcontractorRenewalCacheData.getTransDocNo());
		if (laSet != null)
		{
			String lsDupDocNo =
				getDuplicateString(
					caSubcontractorRenewalCacheData
						.getSubconTransData(),
					laSet,
					ERR_DOC_NO);
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Duplicate document number. ("
					+ lsDupDocNo
					+ ")  Please delete the duplicate records with"
					+ " the red diskette icon",
				"ERROR").displayError(
				this);
			formatErrorInfo(laSet);
			ciErrorSetType = ERR_DOC_NO;
			csErrInfo =
				getDuplicateString(
					caSubcontractorRenewalCacheData
						.getSubconTransData(),
					laSet,
					ciErrorSetType);
			return false;
		}
		//VIN
		laSet =
			SubcontractorHelper.getFirstDuplicateSet(
				caSubcontractorRenewalCacheData.getTransVIN());
		if (laSet != null)
		{
			String lsDupVIN =
				getDuplicateString(
					caSubcontractorRenewalCacheData
						.getSubconTransData(),
					laSet,
					ERR_VIN);
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Duplicate VIN. ("
					+ lsDupVIN
					+ ") Please delete the duplicate records with"
					+ " the red diskette icon",
				"ERROR").displayError(
				this);
			formatErrorInfo(laSet);
			ciErrorSetType = ERR_VIN;
			csErrInfo =
				getDuplicateString(
					caSubcontractorRenewalCacheData
						.getSubconTransData(),
					laSet,
					ciErrorSetType);
			return false;
		}

		//new plate number
		laSet =
			SubcontractorHelper.getFirstDuplicateSet(
				caSubcontractorRenewalCacheData.getTransNewPltNo());
		if (laSet != null)
		{
			String lsDupNewPlt =
				getDuplicateString(
					caSubcontractorRenewalCacheData
						.getSubconTransData(),
					laSet,
					ERR_NEW_PLT);
			//defect 5062
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Duplicate new plate number. ("
					+ lsDupNewPlt
					+ ") Please delete the duplicate records with"
					+ " the red diskette icon",
				"ERROR").displayError(
				this);
			//end defect 5062
			formatErrorInfo(laSet);
			ciErrorSetType = ERR_NEW_PLT;
			csErrInfo =
				getDuplicateString(
					caSubcontractorRenewalCacheData
						.getSubconTransData(),
					laSet,
					ciErrorSetType);
			return false;
		}

		//current plate
		SubcontractorHelper.getFirstDuplicateSet(
			caSubcontractorRenewalCacheData.getTransCurrPltNo());
		if (laSet != null)
		{
			String lsDupCurrPlt =
				getDuplicateString(
					caSubcontractorRenewalCacheData
						.getSubconTransData(),
					laSet,
					ERR_CURR_PLT);
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				"Duplicate current plate number. ("
					+ lsDupCurrPlt
					+ ") Please delete the duplicate records with"
					+ " the red diskette icon",
				"ERROR").displayError(
				this);
			formatErrorInfo(laSet);
			ciErrorSetType = ERR_CURR_PLT;
			csErrInfo =
				getDuplicateString(
					caSubcontractorRenewalCacheData
						.getSubconTransData(),
					laSet,
					ciErrorSetType);
			return false;
		}
		return true;
	}

	/**
	 * Check on client side
	 * 
	 * @param  abPopUpMsg  
	 * @return boolean 
	 */
	private boolean finalClientSideCheck(boolean abPopUpMsg)
	{
		//if total is larger than max bundle, do not Proceed
		if (caSubcontractorRenewalCacheData
			.getRunningTotal()
			.compareTo(BUNDLE_MAX_AMT)
			== 1)
		{
			if (abPopUpMsg)
			{
				new RTSException(622).displayError(this);
				focusTable();
				getlstSubconItems().requestFocus();
			}
			return false;
		}
		//loop through all diskette transactions that are not processed
		SortedMap laSortedMap =
			caSubcontractorRenewalCacheData.getSubconTransData();
		Iterator laIterator = laSortedMap.keySet().iterator();
		HashSet lhsErrorHashSet = new HashSet();
		boolean lbProceed = true;

		//count the number of unprocessed
		ciUnProcDiskRenwlCount = 0;
		chsUnProcIndex.clear();

		// defect 7900
		// Use cbAllTransPosted to indicate all transactions sent
		cbAllTransPosted = true;
		while (laIterator.hasNext())
		{
			SubcontractorRenewalData laSubconData =
				(SubcontractorRenewalData) laSortedMap.get(
					laIterator.next());

			if (laSubconData.getTransTime() == 0)
			{
				cbAllTransPosted = false;
			}
			if (!laSubconData.isProcessed())
			{

				//used to speed up inventory check
				++ciUnProcDiskRenwlCount;
				chsUnProcIndex.add(laSubconData.getTransKeyNumber());

				//check for client side error
				if (!validateDiskEntries(laSubconData)
					|| !diskConfirmIssueDate(laSubconData)
					|| !diskConfirmMinMaxFee(laSubconData))
				{
					laSubconData.setError(true);
					lhsErrorHashSet.add(
						laSubconData.getTransKeyNumber());
					lbProceed = false;
				}
			}
		} // end defect 7900

		if (!lbProceed)
		{
			if (abPopUpMsg)
			{
				//defect 5062 part 3
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					"Some renewals require data correction or"
						+ " validation.  Please select the renewals with"
						+ " a red diskette icon and click modify.",
					"").displayError(
					this);
				//end defect 5062 part 3
			}
			csErrorSet = lhsErrorHashSet;
			ciErrorSetType = ERR_CLIENT_VAL;
			getlblErrorRecord().setText(
				String.valueOf(lhsErrorHashSet.size()));
			getlblErrorRecord().setVisible(true);
			getstcLblErrRecord().setVisible(true);
			getlstSubconItems().repaint();
		}
		else
		{
			clearErrorSet();
		}
		return lbProceed;
	}

	/**
	 * Diskette validations when user presses enter
	 * 
	 * @return boolean 
	 */
	private boolean finalDisketteValidations()
	{
		if (cbDiskEntry)
		{ //check for duplicates

			if (finalCheckForDiskDuplicates())
			{
				if (finalClientSideCheck(true))
				{
					return finalInventoryCheck();
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Direct call to inventory
	 * 
	 * @return boolean
	 */
	private boolean finalInventoryCheck()
	{
		//iterate through the unprocessed

		//all of disk renewals are processed
		if (ciUnProcDiskRenwlCount == 0)
		{
			return true;
		}
		else
		{
			//find out which unprocessed renewals need inventory check
			Iterator laIterator = chsUnProcIndex.iterator();
			SortedMap laTransInfo =
				caSubcontractorRenewalCacheData.getSubconTransData();
			//clear inventory check list from before
			chsInventoryCheckList.clear();
			while (laIterator.hasNext())
			{
				SubcontractorRenewalData laSubconData =
					(SubcontractorRenewalData) laTransInfo.get(
						laIterator.next());
				ProcessInventoryData laProcInvPlt =
					new ProcessInventoryData();
				laProcInvPlt.setItmCd(laSubconData.getPltItmCd());
				if (laSubconData.getPltItmCd() != null
					&& !laSubconData.getPltItmCd().equals("")
					&& !com
						.txdot
						.isd
						.rts
						.services
						.util
						.constants
						.StickerPrintingUtilities
						.isStickerPrintable(laProcInvPlt))
				{
					chsInventoryCheckList.add(
						laSubconData.getTransKeyNumber());
				}
			}
			if (chsInventoryCheckList.size() > 0)
			{
				ciErrorSetType = ERR_INV_CHECK;
			}
		}
		return true;
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE  FocusEvent 
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		if (aaFE.getSource() == getbuttonPanel().getBtnEnter())
		{
			getlstSubconItems().unselectAllRows();
			getbtnModifyEntry().setEnabled(false);
			getbtnDeleteEntry().setEnabled(false);
		}
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE  FocusEvent 
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		// defect 7930
		// Convert I/O => 1/0 
		if (!aaFE.isTemporary() && aaFE.getSource() == gettxtVin())
		{
			String lsVin = gettxtVin().getText().trim().toUpperCase();
			// defect 8902
			lsVin = CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
			// end defect 8902
			gettxtVin().setText(lsVin);
		}
		// end defect 7930 
	}

	/**
	 * Set the focus on the table
	 * 
	 */
	private void focusTable()
	{
		if (caSubcontractorRenewalCacheData.getSubconTransData().size()
			!= 0)
		{
			getlstSubconItems().setRowSelectionInterval(0, 0);
		}
		else
		{
			gettxtDocno().requestFocus();
		}
	}

	/**
	 * Format the error information so that the display on the
	 * table will be updated
	 * 
	 * @param aaErrorSet Set
	 */
	private void formatErrorInfo(Set aaErrorSet)
	{
		Iterator laIterator = aaErrorSet.iterator();
		while (laIterator.hasNext())
		{
			SubcontractorRenewalData laSubconData =
				(SubcontractorRenewalData) caSubcontractorRenewalCacheData
					.getSubconTransData()
					.get(laIterator.next());
			laSubconData.setError(true);
		}
		((TMREG007) getlstSubconItems().getModel()).add(
			caSubcontractorRenewalCacheData.getSubconTransData());
		getlblErrorRecord().setVisible(true);
		getstcLblErrRecord().setVisible(true);
		getlblErrorRecord().setText(String.valueOf(aaErrorSet.size()));
		csErrorSet = aaErrorSet;
	}

	/**
	 * Return the btnAddTrans property value.
	 * 
	 * @return RTSButton
	 */

	private RTSButton getbtnAddTrans()
	{
		if (ivjbtnAddTrans == null)
		{
			try
			{
				ivjbtnAddTrans =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnAddTrans.setSize(57, 25);
				ivjbtnAddTrans.setName("btnAddTrans");
				ivjbtnAddTrans.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				ivjbtnAddTrans.setText("Add");
				ivjbtnAddTrans.setLocation(504, 205);
				// user code begin {1}
				ivjbtnAddTrans.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnAddTrans;
	}

	/**
	 * Return the btnDeleteEntry property value.
	 * 
	 * @return RTSButton
	 */

	private RTSButton getbtnDeleteEntry()
	{
		if (ivjbtnDeleteEntry == null)
		{
			try
			{
				ivjbtnDeleteEntry =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnDeleteEntry.setName("btnDeleteEntry");
				ivjbtnDeleteEntry.setMnemonic(
					java.awt.event.KeyEvent.VK_D);
				ivjbtnDeleteEntry.setText("Delete Entry");
				ivjbtnDeleteEntry.setMaximumSize(
					new java.awt.Dimension(103, 25));
				ivjbtnDeleteEntry.setActionCommand("Delete Entry");
				ivjbtnDeleteEntry.setBounds(10, 180, 110, 25);
				ivjbtnDeleteEntry.setMinimumSize(
					new java.awt.Dimension(103, 25));
				// user code begin {1}
				ivjbtnDeleteEntry.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnDeleteEntry;
	}

	/**
	 * Return the btnDraftReport property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnDraftReport()
	{
		if (ivjbtnDraftReport == null)
		{
			try
			{
				ivjbtnDraftReport =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnDraftReport.setSize(107, 25);
				ivjbtnDraftReport.setName("btnDraftReport");
				ivjbtnDraftReport.setMnemonic(
					java.awt.event.KeyEvent.VK_O);
				ivjbtnDraftReport.setText("Draft Report");
				ivjbtnDraftReport.setMaximumSize(
					new java.awt.Dimension(103, 25));
				ivjbtnDraftReport.setActionCommand("Draft Report");

				ivjbtnDraftReport.setMinimumSize(
					new java.awt.Dimension(103, 25));
				// user code begin {1}
				ivjbtnDraftReport.setLocation(12, 12);
				ivjbtnDraftReport.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnDraftReport;
	}

	/**
	 * Return the btnModifyEntry property value.
	 * 
	 * @return RTSButton
	 */

	private RTSButton getbtnModifyEntry()
	{
		if (ivjbtnModifyEntry == null)
		{
			try
			{
				ivjbtnModifyEntry =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnModifyEntry.setName("btnModifyEntry");
				ivjbtnModifyEntry.setMnemonic(
					java.awt.event.KeyEvent.VK_M);
				ivjbtnModifyEntry.setText("Modify Entry");
				ivjbtnModifyEntry.setMaximumSize(
					new java.awt.Dimension(103, 25));
				ivjbtnModifyEntry.setActionCommand("Modify Entry");
				ivjbtnModifyEntry.setBounds(125, 180, 110, 25);
				ivjbtnModifyEntry.setMinimumSize(
					new java.awt.Dimension(103, 25));
				// user code begin {1}
				ivjbtnModifyEntry.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnModifyEntry;
	}

	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				ivjbuttonPanel.setBounds(162, 4, 258, 41);
				ivjbuttonPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjbuttonPanel.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjbuttonPanel.getBtnEnter().setMnemonic(
					java.awt.event.KeyEvent.VK_E);
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.getBtnEnter().addFocusListener(this);
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
	 * Get the item from the value in the combo box
	 * 
	 * @param asDropDownValue 
	 * @return String 
	 */
	private String getCdFromDropDown(String asDropDownValue)
	{
		int liSepIndex = asDropDownValue.indexOf(SEPARATOR);
		if (liSepIndex != -1)
		{
			return asDropDownValue.substring(0, liSepIndex);
		}
		else
		{
			return null;
		}
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
			ivjchkAddlSet = new javax.swing.JCheckBox();
			ivjchkAddlSet.setBounds(451, 21, 104, 21);
			ivjchkAddlSet.setText("Additional Set");
			ivjchkAddlSet.setMnemonic(java.awt.event.KeyEvent.VK_L);
			ivjchkAddlSet.addActionListener(this);
		}
		return ivjchkAddlSet;
	}

	/**
	 * Return the chkPrintStkr property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkPrintStkr()
	{
		if (ivjchkPrintStkr == null)
		{
			try
			{
				ivjchkPrintStkr = new JCheckBox();
				ivjchkPrintStkr.setName("chkPrintStkr");
				ivjchkPrintStkr.setMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjchkPrintStkr.setText("Print");
				ivjchkPrintStkr.setBounds(449, 207, 53, 22);
				// user code begin {1}
				// defect 9085 
				ivjchkPrintStkr.addActionListener(this);
				// end defect 9085 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkPrintStkr;
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
			ivjchkSpclPlt = new javax.swing.JCheckBox();
			ivjchkSpclPlt.setSize(101, 21);
			ivjchkSpclPlt.setText("Special Plate");
			ivjchkSpclPlt.setMnemonic(java.awt.event.KeyEvent.VK_C);
			ivjchkSpclPlt.setLocation(345, 21);
			ivjchkSpclPlt.addActionListener(this);
		}
		return ivjchkSpclPlt;
	}
	/**
	 * This method initializes ivjcomboOrganizationName
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboOrganizationName()
	{
		try
		{
			if (ivjcomboOrganizationName == null)
			{
				ivjcomboOrganizationName = new javax.swing.JComboBox();
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
				ivjcomboOrganizationName.setBounds(269, 118, 241, 23);
				ivjcomboOrganizationName.setName("ivjcomboSpclPltCode");
				// user code begin {1}
				ivjcomboOrganizationName.addActionListener(this);
				// user code end
			}
		}
		catch (Throwable aeIVJEx)
		{
			// user code begin {2}
			// user code end
			handleException(aeIVJEx);
		}
		return ivjcomboOrganizationName;
	}

	/**
	 * Return the comboPltCode property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboPltCode()
	{
		if (ivjcomboPltCode == null)
		{
			try
			{
				ivjcomboPltCode = new JComboBox();
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
				ivjcomboPltCode.setBounds(15, 70, 241, 23);
				// user code begin {1}
				// defect 9192 
				// restored ItemListener 
				ivjcomboPltCode.addItemListener(this);
				// end defect 9192 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
		if(ivjcomboPltTerm == null) {
			ivjcomboPltTerm = new javax.swing.JComboBox();
			ivjcomboPltTerm.setBounds(172, 207, 42, 25);
		}
		return ivjcomboPltTerm;
	}

	/**
	 * This method initializes ivjcomboSpclPltTypeCode
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getcomboSpclPltTypeCode()
	{

		if (ivjcomboSpclPltTypeCode == null)
		{
			try
			{
				ivjcomboSpclPltTypeCode = new javax.swing.JComboBox();
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
				ivjcomboSpclPltTypeCode.setBounds(13, 118, 241, 23);
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
	 * Return the comboStickerCode property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboStickerCode()
	{
		if (ivjcomboStickerCode == null)
		{
			try
			{
				ivjcomboStickerCode = new JComboBox();
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
				ivjcomboStickerCode.setBounds(269, 70, 241, 23);
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
		return ivjcomboStickerCode;
	}

	/**
	 * Get Current Record Type Select
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
	 * Get Duplicate String.
	 * 
	 * @return String
	 * @param aaTransInfo SortedMap
	 * @param aaErrorSet Set
	 * @param aiFieldNo int
	 */
	private String getDuplicateString(
		SortedMap aaTransInfo,
		Set aaErrorSet,
		int aiFieldNo)
	{
		if (aaTransInfo.size() > 0
			&& aaErrorSet != null
			&& aaErrorSet.size() > 0
			&& aiFieldNo != 0)
		{
			Integer liIndex = (Integer) aaErrorSet.iterator().next();
			SubcontractorRenewalData laSubconData =
				(SubcontractorRenewalData) aaTransInfo.get(liIndex);
			switch (aiFieldNo)
			{
				case ERR_DOC_NO :
					return laSubconData.getDocNo();
				case ERR_VIN :
					return laSubconData.getVIN();
				case ERR_NEW_PLT :
					return laSubconData.getNewPltNo();
				case ERR_CURR_PLT :
					return laSubconData.getRegPltNo();
			}
		}
		return null;
	}

	/**
	 * Return the FrmRegistrationSubcontractorRenewalREG007ContentPane1 
	 * property value.
	 * 
	 * @return JPanel
	 */

	private javax
		.swing
		.JPanel getFrmRegistrationSubcontractorRenewalREG007ContentPane1()
	{
		if (ivjFrmRegistrationSubcontractorRenewalREG007ContentPane1
			== null)
		{
			try
			{
				ivjFrmRegistrationSubcontractorRenewalREG007ContentPane1 =
					new JPanel();
				ivjFrmRegistrationSubcontractorRenewalREG007ContentPane1
					.setName(
					"FrmRegistrationSubcontractorRenewalREG007ContentPane1");
				ivjFrmRegistrationSubcontractorRenewalREG007ContentPane1
					.setLayout(
					null);
				ivjFrmRegistrationSubcontractorRenewalREG007ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmRegistrationSubcontractorRenewalREG007ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(600, 450));
				getFrmRegistrationSubcontractorRenewalREG007ContentPane1()
					.add(
					getstcLblSubcontractorId(),
					getstcLblSubcontractorId().getName());
				getFrmRegistrationSubcontractorRenewalREG007ContentPane1()
					.add(
					getlblSubcontractor(),
					getlblSubcontractor().getName());
				getFrmRegistrationSubcontractorRenewalREG007ContentPane1()
					.add(
					getJPanel6(),
					getJPanel6().getName());
				getFrmRegistrationSubcontractorRenewalREG007ContentPane1()
					.add(
					getJPanel3(),
					getJPanel3().getName());
				// user code begin {1}
				ivjFrmRegistrationSubcontractorRenewalREG007ContentPane1
					.add(
					getJPanel5(),
					null);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmRegistrationSubcontractorRenewalREG007ContentPane1;
	}

	/**
	 * Get the description of the item code using Cache
	 * 
	 * @param asCd String
	 * @return String 
	 */
	private String getFullDescFromCd(String asCd)
	{
		ItemCodesData laItemCodesData = ItemCodesCache.getItmCd(asCd);
		if (laItemCodesData == null)
		{
			return null;
		}
		return asCd + SEPARATOR + laItemCodesData.getItmCdDesc();
	}

	/**
	 * get the inventory check list
	 * 
	 * @return HashSet
	 */
	public HashSet getInventoryCheckList()
	{
		return chsInventoryCheckList;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setLayout(null);
				getJPanel2().add(
					getlblstcTransCount(),
					getlblstcTransCount().getName());
				getJPanel2().add(
					getlblTransEntered(),
					getlblTransEntered().getName());
				// user code begin {1}
				ivjJPanel2.setSize(228, 20);
				ivjJPanel2.setLocation(328, 10);
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
	 * Return the JPanel3 property value.
	 * 
	 * @return JPanel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel3.setMinimumSize(new java.awt.Dimension(0, 0));
				ivjJPanel3.setBounds(9, 478, 570, 51);
				getJPanel3().add(
					getbtnDraftReport(),
					getbtnDraftReport().getName());
				getJPanel3().add(
					getbuttonPanel(),
					getbuttonPanel().getName());
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
		return ivjJPanel3;
	}

	/**
	 * Return the JPanel5 property value.
	 * 
	 * @return JPanel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
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

				getJPanel5().add(
					getlblDocno(),
					getlblDocno().getName());
				getJPanel5().add(
					getstcLblCurrentPlateNo(),
					getstcLblCurrentPlateNo().getName());
				getJPanel5().add(
					getstcLblVin(),
					getstcLblVin().getName());
				getJPanel5().add(
					getstcLblExpMo(),
					getstcLblExpMo().getName());
				getJPanel5().add(
					getstcLblPltItemCode(),
					getstcLblPltItemCode().getName());
				getJPanel5().add(
					getstcLblStickerItemCode(),
					getstcLblStickerItemCode().getName());
				getJPanel5().add(
					getstcLblIssueDate(),
					getstcLblIssueDate().getName());
				getJPanel5().add(
					getstcLblRegClass(),
					getstcLblRegClass().getName());
				getJPanel5().add(
					getstcLblFee(),
					getstcLblFee().getName());
				getJPanel5().add(
					getstcLblYear(),
					getstcLblYear().getName());
				getJPanel5().add(
					getstcLblNewPltNo(),
					getstcLblNewPltNo().getName());
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
					getcomboPltCode(),
					getcomboPltCode().getName());
				getJPanel5().add(
					getcomboStickerCode(),
					getcomboStickerCode().getName());
				getJPanel5().add(gettxtYear(), gettxtYear().getName());
				getJPanel5().add(
					gettxtIssueDate(),
					gettxtIssueDate().getName());
				getJPanel5().add(
					gettxtDocno(),
					gettxtDocno().getName());
				getJPanel5().add(gettxtVin(), gettxtVin().getName());
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
				getJPanel5().add(
					getbtnAddTrans(),
					getbtnAddTrans().getName());
				// user code begin {1}
				// defect 9085 
				ivjJPanel5.add(getcomboSpclPltTypeCode(), null);
				ivjJPanel5.add(getstcLblSpclPltTypeCode(), null);
				ivjJPanel5.add(getstcLblOrganizationName(), null);
				ivjJPanel5.add(getchkSpclPlt(), null);
				ivjJPanel5.add(getcomboOrganizationName(), null);
				ivjJPanel5.add(getchkAddlSet(), null);
				// end defect 9085 
				// defect 10392 
				ivjJPanel5.add(getstcLblPltTerm(), null);
				ivjJPanel5.add(getstcLblPltExp(), null);
				ivjJPanel5.add(gettxtPltExpMo(), null);
				ivjJPanel5.add(gettxtPltExpYr(), null);
				ivjJPanel5.add(getcomboPltTerm(), null);
				// end defect 10392 
				ivjJPanel5.add(getstcLblSlash(), null);
				ivjJPanel5.setBounds(10, 24, 570, 236);
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
	 * Return the JPanel6 property value.
	 * 
	 * @return javax.swing.JPanel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JPanel getJPanel6()
	{
		if (ivjJPanel6 == null)
		{
			try
			{
				ivjJPanel6 = new javax.swing.JPanel();
				ivjJPanel6.setName("JPanel6");
				ivjJPanel6.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						"Transaction Summary"));
				ivjJPanel6.setLayout(null);
				getJPanel6().add(getJPanel2(), getJPanel2().getName());
				getJPanel6().add(
					getstcLblSubtotal(),
					getstcLblSubtotal().getName());
				getJPanel6().add(
					getlblSubtotal(),
					getlblSubtotal().getName());
				getJPanel6().add(
					getbtnDeleteEntry(),
					getbtnDeleteEntry().getName());
				getJPanel6().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getJPanel6().add(
					getbtnModifyEntry(),
					getbtnModifyEntry().getName());
				getJPanel6().add(
					getlblErrorRecord(),
					getlblErrorRecord().getName());
				getJPanel6().add(
					getstcLblErrRecord(),
					getstcLblErrRecord().getName());
				// user code begin {1}
				ivjJPanel6.setSize(571, 214);
				ivjJPanel6.setLocation(10, 262);
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
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setBounds(10, 31, 553, 144);
				ivjJScrollPane1.setRequestFocusEnabled(false);
				getJScrollPane1().setViewportView(getlstSubconItems());
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
		return ivjJScrollPane1;
	}

	/**
	 * Return the lblDocno property value.
	 * 
	 * @return JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblDocno()
	{
		if (ivjlblDocno == null)
		{
			try
			{
				ivjlblDocno = new JLabel();
				ivjlblDocno.setName("lblDocno");
				ivjlblDocno.setText("Doc No");
				ivjlblDocno.setBounds(96, 142, 45, 21);
				ivjlblDocno.setHorizontalAlignment(SwingConstants.LEFT);
				ivjlblDocno.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_D);
				ivjlblDocno.setLabelFor(gettxtDocno());
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
		return ivjlblDocno;
	}

	/**
	 * Return the lblErrorRecord property value.
	 * 
	 * @return JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblErrorRecord()
	{
		if (ivjlblErrorRecord == null)
		{
			try
			{
				ivjlblErrorRecord = new JLabel();
				ivjlblErrorRecord.setName("lblErrorRecord");
				ivjlblErrorRecord.setFont(
					new java.awt.Font("Arial", 1, 14));
				ivjlblErrorRecord.setText("100");
				//defect 5819
				ivjlblErrorRecord.setBounds(228, 14, 47, 15);
				//end defect 5819
				ivjlblErrorRecord.setForeground(java.awt.Color.red);
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
		return ivjlblErrorRecord;
	}

	/**
	 * Return the lblstcTransCount property value.
	 * 
	 * @return JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblstcTransCount()
	{
		if (ivjlblstcTransCount == null)
		{
			try
			{
				ivjlblstcTransCount = new JLabel();
				ivjlblstcTransCount.setName("lblstcTransCount");
				ivjlblstcTransCount.setText("Transaction(s) Entered: ");
				ivjlblstcTransCount.setBounds(20, 3, 139, 14);
				ivjlblstcTransCount.setHorizontalAlignment(
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
		return ivjlblstcTransCount;
	}

	/**
	 * Return the lblSubcontractor2 property value.
	 * 
	 * @return JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblSubcontractor()
	{
		if (ivjlblSubcontractor == null)
		{
			try
			{
				ivjlblSubcontractor = new JLabel();
				ivjlblSubcontractor.setName("lblSubcontractor");
				ivjlblSubcontractor.setText(
					"001   Education Credit Union - Rev");
				ivjlblSubcontractor.setMaximumSize(
					new java.awt.Dimension(159, 14));
				ivjlblSubcontractor.setMinimumSize(
					new java.awt.Dimension(159, 14));
				ivjlblSubcontractor.setBounds(123, 7, 447, 14);
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
		return ivjlblSubcontractor;
	}

	/**
	 * Return the lblSubtotal property value.
	 * 
	 * @return JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblSubtotal()
	{
		if (ivjlblSubtotal == null)
		{
			try
			{
				ivjlblSubtotal = new JLabel();
				ivjlblSubtotal.setName("lblSubtotal");
				ivjlblSubtotal.setText("623.22");
				ivjlblSubtotal.setMaximumSize(
					new java.awt.Dimension(24, 14));
				ivjlblSubtotal.setMinimumSize(
					new java.awt.Dimension(24, 14));
				ivjlblSubtotal.setHorizontalAlignment(4);
				ivjlblSubtotal.setBounds(485, 180, 66, 23);
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
		return ivjlblSubtotal;
	}

	/**
	 * Return the lblTransEntered property value.
	 * 
	 * @return JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblTransEntered()
	{
		if (ivjlblTransEntered == null)
		{
			try
			{
				ivjlblTransEntered = new JLabel();
				ivjlblTransEntered.setName("lblTransEntered");
				ivjlblTransEntered.setText("15");
				ivjlblTransEntered.setBounds(168, 3, 50, 14);
				ivjlblTransEntered.setHorizontalAlignment(
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
		return ivjlblTransEntered;
	}

	/**
	 * Return the lblVin property value.
	 * 
	 * @return JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVin()
	{
		if (ivjlblVin == null)
		{
			try
			{
				ivjlblVin = new JLabel();
				ivjlblVin.setName("lblVin");
				ivjlblVin.setText(" VIN");
				ivjlblVin.setBounds(269, 142, 25, 21);
				ivjlblVin.setHorizontalAlignment(SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjlblVin.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_V);
				ivjlblVin.setLabelFor(gettxtVin());
				// end defect 9085
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblVin;
	}

	/**
	 * Return the lstSubconItems property value.
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.RTSTable
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable getlstSubconItems()
	{
		if (ivjlstSubconItems == null)
		{
			try
			{
				ivjlstSubconItems =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjlstSubconItems.setName("lstSubconItems");
				getJScrollPane1().setColumnHeaderView(
					ivjlstSubconItems.getTableHeader());

				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjlstSubconItems.setModel(new TMREG007());

				ivjlstSubconItems.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjlstSubconItems.setShowVerticalLines(false);
				ivjlstSubconItems.setShowHorizontalLines(false);
				ivjlstSubconItems.setBounds(0, 0, 547, 81);
				ivjlstSubconItems.setRowHeight(25);
				// user code begin {1}
				ivjlstSubconItems.setAutoCreateColumnsFromModel(false);
				cTableModel = (TMREG007) ivjlstSubconItems.getModel();
				TableColumn laInputTypeTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(0));
				laInputTypeTableColumn.setPreferredWidth(25);
				TableColumn laPrintIndiTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(1));
				laPrintIndiTableColumn.setPreferredWidth(25);
				TableColumn laIssueDateTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(2));
				laIssueDateTableColumn.setPreferredWidth(55);
				TableColumn laExpYrTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(3));
				laExpYrTableColumn.setPreferredWidth(30);
				TableColumn laNewPltTypeTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(4));
				laNewPltTypeTableColumn.setPreferredWidth(35);
				TableColumn laNewPltTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(5));
				laNewPltTableColumn.setPreferredWidth(60);
				TableColumn newStkrTypeTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(6));
				newStkrTypeTableColumn.setPreferredWidth(40);
				TableColumn laCurrPltTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(7));
				laCurrPltTableColumn.setPreferredWidth(60);
				TableColumn laExpMoYrTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(8));
				laExpMoYrTableColumn.setPreferredWidth(45);
				TableColumn laFeesTableColumn =
					ivjlstSubconItems.getColumn(
						ivjlstSubconItems.getColumnName(9));
				//			feesTableColumn.setPreferredWidth(145);
				ivjlstSubconItems.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjlstSubconItems.init();
				//DefaultTableCellRenderer ren = new ImageTableCellRenderer();
				laInputTypeTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.CENTER));
				laPrintIndiTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.CENTER));
				laIssueDateTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.CENTER));
				laExpYrTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.CENTER));
				laNewPltTypeTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.CENTER));
				laExpYrTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.CENTER));
				laNewPltTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.CENTER));
				newStkrTypeTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.CENTER));
				laCurrPltTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.CENTER));
				laExpMoYrTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.RIGHT));
				laFeesTableColumn.setCellRenderer(
					ivjlstSubconItems.setColumnAlignment(
						RTSTable.RIGHT));

				ivjlstSubconItems.addMultipleSelectionListener(this);

				ivjlstSubconItems.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlstSubconItems;
	}

	/**
	 * Return New Fee 
	 *
	 * @return RTSInputField
	 */
	public RTSInputField getNewFeeTxtField()
	{
		return gettxtFee();
	}

	/**
	 * Return New Plate No
	 * 
	 * @return RTSInputField
	 */
	public RTSInputField getNewPlateTxtField()
	{
		return gettxtNewPlateNo();
	}
	/**
	 * Get the item from the value in the combo box
	 * 
	 * @param aiIndex
	 */
	private void getOrgNoFromDropDown(int aiIndex)
	{
		String lsOrgNo = (String) cvOrgNo.elementAt(aiIndex);
		csOrgNo = lsOrgNo.substring(50).trim();
	}
	/**
	 * Get the item from the value in the combo box
	 * 
	 * @param aiIndex
	 */
	private void getSpclRegPltCdFromDropDown(int aiIndex)
	{
		String lsRegPltCd = (String) svSpclPltDesc.elementAt(aiIndex);
		csSpclRegPltCd = lsRegPltCd.substring(50).trim();
	}
	/**
	 * Return the radioPlt property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioPlt()
	{
		if (ivjradioPlt == null)
		{
			try
			{
				ivjradioPlt = new javax.swing.JRadioButton();
				ivjradioPlt.setSize(67, 22);
				ivjradioPlt.setName("radioPlt");
				ivjradioPlt.setMnemonic(KeyEvent.VK_P);
				ivjradioPlt.setText(PLATE);
				ivjradioPlt.setLocation(12, 20);
				// user code begin  {1}
				ivjradioPlt.addActionListener(this);

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioPlt;
	}

	/**
	 * Return the radioPltStkr property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioPltStkr()
	{
		if (ivjradioPltStkr == null)
		{
			try
			{
				ivjradioPltStkr = new javax.swing.JRadioButton();
				ivjradioPltStkr.setName("radioPltStkr");
				ivjradioPltStkr.setMnemonic(KeyEvent.VK_T);
				ivjradioPltStkr.setText(PLATESTICKER);
				ivjradioPltStkr.setBounds(152, 20, 112, 22);
				// user code begin {1}
				ivjradioPltStkr.addActionListener(this);

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioPltStkr;
	}

	/**
	 * Return the radioStkr property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JRadioButton getradioStkr()
	{
		if (ivjradioStkr == null)
		{
			try
			{
				ivjradioStkr = new javax.swing.JRadioButton();
				ivjradioStkr.setName("radioStkr");
				ivjradioStkr.setMnemonic(KeyEvent.VK_S);
				ivjradioStkr.setText(STICKER);
				ivjradioStkr.setBounds(81, 20, 67, 22);
				// user code begin {1}
				ivjradioStkr.addActionListener(this);

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioStkr;
	}

	/**
	 * Get the textfield of the renewal fee
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
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblCurrentPlateNo()
	{
		if (ivjstcLblCurrentPlateNo == null)
		{
			try
			{
				ivjstcLblCurrentPlateNo = new javax.swing.JLabel();
				ivjstcLblCurrentPlateNo.setName("stcLblCurrentPlateNo");
				ivjstcLblCurrentPlateNo.setText("Curr Plt");
				// defect 9085 
				ivjstcLblCurrentPlateNo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_U);
				ivjstcLblCurrentPlateNo.setLabelFor(
					gettxtCurrentPlateNo());
				// end defect 9085
				ivjstcLblCurrentPlateNo.setMaximumSize(
					new java.awt.Dimension(93, 14));
				ivjstcLblCurrentPlateNo.setMinimumSize(
					new java.awt.Dimension(93, 14));
				ivjstcLblCurrentPlateNo.setBounds(97, 188, 47, 21);
				ivjstcLblCurrentPlateNo.setHorizontalAlignment(
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
		return ivjstcLblCurrentPlateNo;
	}

	/**
	 * Return the stcLblErrRecord property value.
	 * 
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblErrRecord()
	{
		if (ivjstcLblErrRecord == null)
		{
			try
			{
				ivjstcLblErrRecord = new javax.swing.JLabel();
				ivjstcLblErrRecord.setName("stcLblErrRecord");
				ivjstcLblErrRecord.setFont(
					new java.awt.Font("Arial", 1, 14));
				ivjstcLblErrRecord.setText("Error Records: ");
				// defect 5819
				ivjstcLblErrRecord.setBounds(95, 14, 113, 14);
				// end defect 5819
				ivjstcLblErrRecord.setForeground(java.awt.Color.red);
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
		return ivjstcLblErrRecord;
	}

	/**
	 * Return the stcLblExpMo property value.
	 * 
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblExpMo()
	{
		if (ivjstcLblExpMo == null)
		{
			try
			{
				ivjstcLblExpMo = new javax.swing.JLabel();
				ivjstcLblExpMo.setName("stcLblExpMo");
				ivjstcLblExpMo.setText("Exp Mo");
				ivjstcLblExpMo.setMaximumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblExpMo.setMinimumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblExpMo.setBounds(311, 188, 46, 21);
				ivjstcLblExpMo.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjstcLblExpMo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_X);
				ivjstcLblExpMo.setLabelFor(gettxtExpMo());
				// end defect 9085
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblExpMo;
	}

	/**
	 * Return the stcLblFee property value.
	 * 
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblFee()
	{
		if (ivjstcLblFee == null)
		{
			try
			{
				ivjstcLblFee = new javax.swing.JLabel();
				ivjstcLblFee.setName("stcLblFee");
				ivjstcLblFee.setText("Fee");
				ivjstcLblFee.setMaximumSize(
					new java.awt.Dimension(20, 14));
				ivjstcLblFee.setMinimumSize(
					new java.awt.Dimension(20, 14));
				ivjstcLblFee.setBounds(408, 188, 31, 21);
				ivjstcLblFee.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjstcLblFee.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_F);
				ivjstcLblFee.setLabelFor(gettxtFee());
				// end defect 9085
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblFee;
	}

	/**
	 * Return the stcLblIssueDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblIssueDate()
	{
		if (ivjstcLblIssueDate == null)
		{
			try
			{
				ivjstcLblIssueDate = new javax.swing.JLabel();
				ivjstcLblIssueDate.setName("stcLblIssueDate");

				ivjstcLblIssueDate.setMaximumSize(
					new java.awt.Dimension(60, 14));
				ivjstcLblIssueDate.setLabelFor(gettxtIssueDate());
				ivjstcLblIssueDate.setSize(68, 21);
				ivjstcLblIssueDate.setMinimumSize(
					new java.awt.Dimension(60, 14));
				ivjstcLblIssueDate.setLocation(15, 142);
				ivjstcLblIssueDate.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}

				ivjstcLblIssueDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_I);
				ivjstcLblIssueDate.setText("Issue Date");
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblIssueDate;
	}

	/**
	 * Return the stcLblNewPltNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblNewPltNo()
	{
		if (ivjstcLblNewPltNo == null)
		{
			try
			{
				ivjstcLblNewPltNo = new javax.swing.JLabel();
				ivjstcLblNewPltNo.setName("stcLblNewPltNo");

				ivjstcLblNewPltNo.setText("New Plate");
				ivjstcLblNewPltNo.setLocation(15, 188);
				ivjstcLblNewPltNo.setMaximumSize(
					new java.awt.Dimension(59, 14));

				ivjstcLblNewPltNo.setSize(63, 21);

				ivjstcLblNewPltNo.setMinimumSize(
					new java.awt.Dimension(59, 14));
				ivjstcLblNewPltNo.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}

				ivjstcLblNewPltNo.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_N);
				ivjstcLblNewPltNo.setLabelFor(gettxtNewPlateNo());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblNewPltNo;
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
			ivjstcLblOrganizationName = new javax.swing.JLabel();
			ivjstcLblOrganizationName.setBounds(269, 96, 228, 21);
			ivjstcLblOrganizationName.setText("Organization Name");
		}
		return ivjstcLblOrganizationName;
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
			ivjstcLblSpclPltTypeCode = new javax.swing.JLabel();
			ivjstcLblSpclPltTypeCode.setSize(228, 21);
			ivjstcLblSpclPltTypeCode.setText(
				"Special Plate Description");
			ivjstcLblSpclPltTypeCode.setLocation(15, 96);
		}
		return ivjstcLblSpclPltTypeCode;
	}
	/**
	 * Return the stcLblPltItemCode property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblPltItemCode()
	{
		if (ivjstcLblPltItemCode == null)
		{
			try
			{
				ivjstcLblPltItemCode = new javax.swing.JLabel();
				ivjstcLblPltItemCode.setSize(198, 21);
				ivjstcLblPltItemCode.setName("stcLblPltItemCode");
				ivjstcLblPltItemCode.setText(
					"Plate Type Code - Description");
				ivjstcLblPltItemCode.setMaximumSize(
					new java.awt.Dimension(176, 14));
				ivjstcLblPltItemCode.setMinimumSize(
					new java.awt.Dimension(176, 14));

				ivjstcLblPltItemCode.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				ivjstcLblPltItemCode.setLocation(15, 48);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblPltItemCode;
	}

	/**
	 * Return the stcLblPltExp property value.
	 * 
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblPltExp()
	{
		if (ivjstcLblPltExp == null)
		{
			try
			{
				ivjstcLblPltExp = new javax.swing.JLabel();
				ivjstcLblPltExp.setName("stcLblPltExp");
				ivjstcLblPltExp.setText("Curr Plate Exp");
				ivjstcLblPltExp.setMaximumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblPltExp.setMinimumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblPltExp.setBounds(218, 188, 89, 21);
				ivjstcLblPltExp.setHorizontalAlignment(
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
		return ivjstcLblPltExp;
	}

	/**
	 * Return the stcLblPltTerm property value.
	 * 
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblPltTerm()
	{
		if (ivjstcLblPltTerm == null)
		{
			try
			{
				ivjstcLblPltTerm = new javax.swing.JLabel();
				ivjstcLblPltTerm.setName("stcLblExpMo");
				ivjstcLblPltTerm.setText("Plt Term");
				ivjstcLblPltTerm.setMaximumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblPltTerm.setMinimumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblPltTerm.setBounds(162, 188, 53, 21);
				ivjstcLblPltTerm.setHorizontalAlignment(
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
		return ivjstcLblPltTerm;
	}

	/**
	 * Return the stcLblRegClass property value.
	 * 
	 * @returnJLabel
	 */

	private JLabel getstcLblRegClass()
	{
		if (ivjstcLblRegClass == null)
		{
			try
			{
				ivjstcLblRegClass = new javax.swing.JLabel();
				ivjstcLblRegClass.setSize(41, 21);
				ivjstcLblRegClass.setName("stcLblRegClass");
				ivjstcLblRegClass.setText("Reg Cl");
				ivjstcLblRegClass.setLocation(363, 188);
				ivjstcLblRegClass.setMaximumSize(
					new java.awt.Dimension(57, 14));
				ivjstcLblRegClass.setMinimumSize(
					new java.awt.Dimension(57, 14));

				ivjstcLblRegClass.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				// defect 9085 
				ivjstcLblRegClass.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_G);
				ivjstcLblRegClass.setLabelFor(gettxtRegClass());
				// end defect 9085
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
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
				ivjstcLblSlash.setBounds(247, 209, 8, 21);
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
	 * @return javax.swing.JLabel
	 */ /* WARNING: THIS METHOD WILL BE REGENERATED. */

	private javax.swing.JLabel getstcLblStickerItemCode()
	{
		if (ivjstcLblStickerItemCode == null)
		{
			try
			{
				ivjstcLblStickerItemCode = new javax.swing.JLabel();
				ivjstcLblStickerItemCode.setSize(190, 21);
				ivjstcLblStickerItemCode.setName(
					"stcLblStickerItemCode");
				ivjstcLblStickerItemCode.setText(
					"Sticker Type Code - Description");
				ivjstcLblStickerItemCode.setMaximumSize(
					new java.awt.Dimension(176, 14));
				ivjstcLblStickerItemCode.setMinimumSize(
					new java.awt.Dimension(176, 14));

				ivjstcLblStickerItemCode.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				// user code begin {1}
				ivjstcLblStickerItemCode.setLocation(269, 48);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblStickerItemCode;
	}

	/**
	 * Return the stcLblSubcontractorId property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblSubcontractorId()
	{
		if (ivjstcLblSubcontractorId == null)
		{
			try
			{
				ivjstcLblSubcontractorId = new javax.swing.JLabel();
				ivjstcLblSubcontractorId.setName(
					"stcLblSubcontractorId");
				ivjstcLblSubcontractorId.setText("Subcontractor Id:");
				ivjstcLblSubcontractorId.setMaximumSize(
					new java.awt.Dimension(98, 14));
				ivjstcLblSubcontractorId.setMinimumSize(
					new java.awt.Dimension(98, 14));
				ivjstcLblSubcontractorId.setHorizontalAlignment(4);
				ivjstcLblSubcontractorId.setBounds(12, 6, 101, 15);
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
		return ivjstcLblSubcontractorId;
	}

	/**
	 * Return the stcLblSubtotal property value.
	 * 
	 * @return JLabel
	 */

	private javax.swing.JLabel getstcLblSubtotal()
	{
		if (ivjstcLblSubtotal == null)
		{
			try
			{
				ivjstcLblSubtotal = new javax.swing.JLabel();
				ivjstcLblSubtotal.setName("stcLblSubtotal");
				ivjstcLblSubtotal.setText("Subtotal:");
				ivjstcLblSubtotal.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblSubtotal.setMinimumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblSubtotal.setHorizontalAlignment(4);
				ivjstcLblSubtotal.setBounds(406, 180, 72, 23);
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
		return ivjstcLblSubtotal;
	}

	/**
	 * Return the stcLblYear property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblYear()
	{
		if (ivjstcLblYear == null)
		{
			try
			{
				ivjstcLblYear = new javax.swing.JLabel();
				ivjstcLblYear.setName("stcLblYear");
				ivjstcLblYear.setText(" Year");
				ivjstcLblYear.setMaximumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblYear.setMinimumSize(
					new java.awt.Dimension(26, 14));
				ivjstcLblYear.setBounds(517, 50, 39, 19);
				// user code begin {1}
				ivjstcLblYear.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_Y);
				// defect 9194
				ivjstcLblYear.setLabelFor(gettxtYear());
				// end defect 9194 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblYear;
	}

	/**
	 * Return the txtCurrentPlateNo property value.
	 * 
	 * @return RTSInputField
	 */

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
				ivjtxtCurrentPlateNo.setBounds(97, 210, 70, 20);
				ivjtxtCurrentPlateNo.setMaxLength(7);
				ivjtxtCurrentPlateNo.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
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
		return ivjtxtCurrentPlateNo;
	}

	/**
	 * Return the txtDocno property value.
	 * 
	 * @return RTSInputField
	 */

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
				ivjtxtDocno.setBounds(96, 164, 143, 20);
				ivjtxtDocno.setMaxLength(17);
				ivjtxtDocno.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtDocno.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtDocno;
	}

	/**
	 * Return the txtExpMo property value.
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
				ivjtxtExpMo.setName("txtExpMo");
				ivjtxtExpMo.setInput(1);
				ivjtxtExpMo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtExpMo.setBounds(318, 210, 30, 20);
				ivjtxtExpMo.setMaxLength(2);
				ivjtxtExpMo.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
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
	 * Return the txtFee property value.
	 * 
	 * @return RTSInputField
	 */

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
				ivjtxtFee.setBounds(400, 210, 47, 20);
				ivjtxtFee.setMaxLength(7);
				ivjtxtFee.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtFee.setInput(RTSInputField.DOLLAR_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtFee;
	}

	/**
	 * Return the txtIssueDate property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSDateField gettxtIssueDate()
	{
		if (ivjtxtIssueDate == null)
		{
			try
			{
				ivjtxtIssueDate = new RTSDateField();
				ivjtxtIssueDate.setSize(67, 20);
				ivjtxtIssueDate.setName("txtIssueDate");
				ivjtxtIssueDate.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtIssueDate.setText("  /  /    ");

				ivjtxtIssueDate.setColumns(10);
				// user code begin {1}
				ivjtxtIssueDate.setLocation(15, 164);
				ivjtxtIssueDate.addFocusListener(this);
				// defect 7894
				// remove key listener
				//ivjtxtIssueDate.addKeyListener(this);
				// end defect 7894
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtIssueDate;
	}

	/**
	 * Return the txtStickerNo1 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtNewPlateNo()
	{
		if (ivjtxtNewPlateNo == null)
		{
			try
			{
				ivjtxtNewPlateNo = new RTSInputField();
				ivjtxtNewPlateNo.setSize(70, 20);
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

				ivjtxtNewPlateNo.setMaxLength(7);
				ivjtxtNewPlateNo.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtNewPlateNo.setLocation(15, 210);
				ivjtxtNewPlateNo.addFocusListener(this);

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtNewPlateNo;
	}

	/**
	 * Return the txtPltExpMo property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtPltExpMo()
	{
		if (ivjtxtPltExpMo == null)
		{
			try
			{
				ivjtxtPltExpMo = new RTSInputField();
				ivjtxtPltExpMo.setName("txtExpMo");
				ivjtxtPltExpMo.setInput(1);
				ivjtxtPltExpMo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPltExpMo.setBounds(217, 210, 30, 20);
				ivjtxtPltExpMo.setMaxLength(2);
				ivjtxtPltExpMo.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtPltExpMo.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPltExpMo;
	}

	/**
	 * Return the txtPltExpYr property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtPltExpYr()
	{
		if (ivjtxtPltExpYr == null)
		{
			try
			{
				ivjtxtPltExpYr = new RTSInputField();
				ivjtxtPltExpYr.setName("txtYear");
				ivjtxtPltExpYr.setInput(1);
				ivjtxtPltExpYr.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPltExpYr.setBounds(252, 210, 46, 20);
				ivjtxtPltExpYr.setMaxLength(4);
				ivjtxtPltExpYr.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtPltExpYr.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtPltExpYr.addFocusListener(this);

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPltExpYr;
	}

	/**
	 * Return the txtRegClass property value.
	 * 
	 * @return RTSInputField
	 */

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
				ivjtxtRegClass.setBounds(365, 210, 29, 20);
				ivjtxtRegClass.setMaxLength(3);
				ivjtxtRegClass.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtRegClass.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtRegClass.addFocusListener(this);

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRegClass;
	}

	/**
	 * Return the txtVin property value.
	 * 
	 * @return RTSInputField
	 */

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
				ivjtxtVin.setBounds(269, 164, 227, 20);
				ivjtxtVin.setMaxLength(22);
				ivjtxtVin.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				// defect 7930
				// For VIN modification 
				ivjtxtVin.addFocusListener(this);
				// end defect 7930 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVin;
	}

	/**
	 * Return the txtYear property value.
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
				ivjtxtYear.setName("txtYear");
				ivjtxtYear.setInput(1);
				ivjtxtYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtYear.setBounds(514, 70, 46, 23);
				ivjtxtYear.setMaxLength(4);
				ivjtxtYear.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtYear.setInput(RTSInputField.NUMERIC_ONLY);
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
	 * Handle Cancel event
	 * 
	 * @throws RTSException
	 */
	private void handleCancel() throws RTSException
	{
		if (caSubcontractorRenewalCacheData.getSubconTransData().size()
			!= 0)
		{
			//CTL001 Confirmation screen
			int liReturn =
				new RTSException(
					RTSException.CTL001,
					"Select 'YES' to keep Bundle",
					null).displayError(
					this);
			if (liReturn == RTSException.YES)
			{
				getController().processData(
					VCRegistrationSubcontractorRenewalREG007
						.CANCEL_HELD_ITMS_ONLY,
					caSubcontractorRenewalCacheData);
			}
			else
			{
				if (Transaction.getTransactionHeaderData() != null
					|| (cbDiskEntry
						&& caSubcontractorRenewalCacheData
							.getDiskHeldPltList()
							!= null
						&& caSubcontractorRenewalCacheData
							.getDiskHeldPltList()
							.size()
							> 0))
				{
					getController().processData(
						VCRegistrationSubcontractorRenewalREG007
							.CLEAN_CANCEL,
						caSubcontractorRenewalCacheData);
				}
				else
				{
					getController().processData(
						AbstractViewController.CANCEL,
						null);
				}
				if (SubconBundleManager.bundleExists())
				{
					SubconBundleManager.deleteBundle();
				}
			}
		}
		else
		{
			if (Transaction.getTransactionHeaderData() != null
				|| caSubcontractorRenewalCacheData
					.getTransactionHeaderData()
					!= null)
			{
				//if trans header exist, need to delete the header record
				getController().processData(
					VCRegistrationSubcontractorRenewalREG007
						.CLEAN_CANCEL,
					caSubcontractorRenewalCacheData);
			}
			else
			{
				//cancel held if any			
				getController().processData(
					VCRegistrationSubcontractorRenewalREG007
						.CANCEL_HELD_ITMS_ONLY,
					caSubcontractorRenewalCacheData);
			}
			SubconBundleManager.deleteBundle();
		}
	}

	/**
	 * handle the Delete Subcon item event
	 * 
	 */
	private void handleDeleteEntry()
	{
		int liReturnStatus =
			new RTSException(
				RTSException.CTL001,
				"Are you sure you want to DELETE?",
				"Confirm Delete").displayError(
				this);
		if (liReturnStatus == RTSException.YES)
		{
			caSubcontractorRenewalCacheData.setDeleteIndex(
				getlstSubconItems().getSelectedRowNumbers());
			getController().processData(
				VCRegistrationSubcontractorRenewalREG007
					.DEL_SELECTED_SUBCON_RENWL,
				caSubcontractorRenewalCacheData);
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
	 * Handle Modify Entry event
	 * 
	 */
	private void handleModifyEntry()
	{
		Set laRowNums = getlstSubconItems().getSelectedRowNumbers();
		Iterator laIterator = laRowNums.iterator();
		Integer liSelectedRow = (Integer) laIterator.next();
		Vector lvTransList =
			new Vector(
				caSubcontractorRenewalCacheData
					.getSubconTransData()
					.values());
		ciSelectedModifyRow = liSelectedRow.intValue();
		SubcontractorRenewalData laToBeModified =
			(SubcontractorRenewalData) lvTransList.get(
				liSelectedRow.intValue());
		caSubcontractorRenewalCacheData.setRecordTobeModified(
			laToBeModified);
		caSubcontractorRenewalCacheData.setRecordModifyIndex(
			laToBeModified.getTransKeyNumber());
		// defect 7894 
		// remove focus management required in Java 1.3	
		//getbuttonPanel().getBtnEnter().requestFocus();
		//getbuttonPanel().getBtnEnter().setFocusPainted(false);
		getController().processData(
			VCRegistrationSubcontractorRenewalREG007
				.MODIFY_SUBCON_RENWL_RECORD,
			caSubcontractorRenewalCacheData);
		getbuttonPanel().getBtnEnter().requestFocus();
		// end defect 7894 
	}

	/**
	 * Populate screen data based on Plate bar code data
	 * 
	 * @param  aaPlateBarCodeData 
	 * @throws RTSException
	 */
	private void handlePlateBarCode(PlateBarCodeData aaPlateBarCodeData)
		throws RTSException
	{ //set the display selection

		if (Integer
			.parseInt(cleanUpBarCode(aaPlateBarCodeData.getItemYr()))
			> 0)
		{ //PLT

			getradioPlt().setSelected(true);
		}
		else
		{ //PLT and STKR

			getradioPltStkr().setSelected(true);
		}
		gettxtNewPlateNo().setText(aaPlateBarCodeData.getItemNo());
		csBarCodePltCd =
			getFullDescFromCd(
				cleanUpBarCode(aaPlateBarCodeData.getItemCd()));
		if (csBarCodePltCd == null)
		{
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				SubcontractorHelper.BAR_CODE_INV_ERROR,
				"").displayError(
				this);
		}
	}

	/**
	 * Populate screen data based on Renewal bar code data
	 * 
	 * @param  aaRenewalBarCodeData 
	 * @throws RTSException
	 */
	private void handleRenwlBarCode(RenewalBarCodeData aaRenewalBarCodeData)
		throws RTSException
	{
		boolean lbRenwlBarCodeValid = false;
		// defect 7552
		// Set isWorking true initially so that Enter/Cancel/Help/Add
		// can not be invoked
		// Set false in finally clause
		try
		{
			//	synchronized (this)
			//	{
			// defect 8686
			// Use setWorking() vs. isWorking()
			// defect 7894
			// isWorking = true;
			// isWorking();
			// end defect 7894
			setWorking(true);
			// end defect 8686 
			// check if any parsing error
			if (aaRenewalBarCodeData.getCntyNo() == null
				|| aaRenewalBarCodeData.getCntyNo().trim().equals("")
				|| aaRenewalBarCodeData.getRegExpMo() == null
				|| aaRenewalBarCodeData.getRegExpMo().trim().equals(""))
			{
				System.out.println("info is null");
				displayScannerMsg();
			}
			else
			{
				try
				{
					int i =
						Integer.parseInt(
							aaRenewalBarCodeData.getCntyNo());
					if (i == 0)
					{
						displayScannerMsg();
					}
					i =
						Integer.parseInt(
							aaRenewalBarCodeData.getRegExpMo());
					if (i == 0)
					{
						displayScannerMsg();
					}
				}
				catch (NumberFormatException leNFEx)
				{
					displayScannerMsg();
				}
			}
			//if plate number is OLDPLTX pop up mesage
			if (aaRenewalBarCodeData.getRegPltCd() != null
				&& aaRenewalBarCodeData.getRegPltCd().trim().equals(
					"OLDPLTX"))
			{
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					"Plate code is OLDPLTX.  Please use renewal"
						+ " event to continue.",
					"");
			}
			//Find duplicate doc no
			// defect 5751 
			// Removed cleanUpBarCode in order to
			// keep docno at length 17
			//String lsDocNo = 
			// cleanUpBarCode(aRenewalBarCodeData.getDocNo());
			// end defect 5751
			String lsDocNo = aaRenewalBarCodeData.getDocNo();
			if (caSubcontractorRenewalCacheData
				.getTransDocNo()
				.contains(lsDocNo))
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						SubcontractorHelper.DUPL_DOC + lsDocNo,
						"ERROR");
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}

			// Set Selections on Screen

			String lsRegPltCd =
				aaRenewalBarCodeData.getRegPltCd().trim();
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(lsRegPltCd);
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
					ciAddlSetIndi =
						aaRenewalBarCodeData.getAddlSetIndi();
					preSelectAddlSetChkBox(
						ciAddlSetIndi == 1,
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
			// defect 9192
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
			gettxtCurrentPlateNo().setText(
				aaRenewalBarCodeData.getRegPltNo().trim());

			//populates exp mo
			gettxtExpMo().setText(
				cleanUpBarCode(aaRenewalBarCodeData.getRegExpMo()));

			//populates reg class
			gettxtRegClass().setText(
				String.valueOf(aaRenewalBarCodeData.getRegClassCd()));

			//populates fee
			Dollar laFees = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice1());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice2());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice3());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice4());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice5());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice6());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice7());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice8());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice9());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice10());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice11());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice12());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice13());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice14());
			laFees = laFees.add(aaRenewalBarCodeData.getItmPrice15());

			//end defect 7348
			gettxtFee().setText(laFees.toString());
			caRenewalBarCodeData.setRenwlPrice(laFees);
			
			//preselect sticker combo            
			if (getcomboStickerCode().isEnabled())
			{
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

			//preselect plate combo            
			// defect 7543 - revisit; do not reassign to "";
			if (getcomboPltCode().isEnabled())
			{
				String lsPltCode =
					getFullDescFromCd(
						cleanUpBarCode(
							aaRenewalBarCodeData.getRegPltCd()));
				if (lsPltCode != null)
				{
					getcomboPltCode().setSelectedItem(lsPltCode);
					// defect 8479
					comboBoxHotKeyFix(getcomboPltCode());
					// end defect 8479
				}
			}

			//			else
			//			{
			//				resetSpclPlates();
			//			}
			//
			//else
			//{
			//	aRenewalBarCodeData.setRegPltCd("");
			//}
			// end defect 7543 revisit
			//display error msg and turn off barcode indi if county is
			// different
			if (!String
				.valueOf(SystemProperty.getOfficeIssuanceNo())
				.equals(
					cleanUpBarCode(aaRenewalBarCodeData.getCntyNo())))
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
			//automatically add transaction if everything is filled out 
			lbRenwlBarCodeValid = automaticAdd();
			if (!lbRenwlBarCodeValid)
			{
				caRenewalBarCodeData = null;
			} //}
			if (cvRenewalQueue.size() > 0 && lbRenwlBarCodeValid)
			{
				RenewalBarCodeData laData =
					(RenewalBarCodeData) cvRenewalQueue.remove(0);
				handleRenwlBarCode(laData);
			}
		}
		finally
		{
			doneWorking();
		} // end defect 7552
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

	private void initialize()
	{
		try
		{
			// user code begin {1}
			// defect 8494
			// Moved from windowOpened.
			setDefaultFocusField(getcomboStickerCode());
			// end defect 8494
			// defect 5734
			// defect 7894
			//FocusManager.setCurrentManager(new ContainerOrderFocusManager());
			// end defect 7894
			// user code end
			setName("FrmRegistrationSubcontractorRenewalREG007");
			setSize(596, 557);
			setTitle("Registration Subcontractor Renewal          REG007");
			setContentPane(
				getFrmRegistrationSubcontractorRenewalREG007ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		} // user code begin {2}

		addWindowListener(this);
		cvRenewalQueue = new Vector();
		// defect 7894
		// Changed from ButtonGroup to RTSButtonGroup
		RTSButtonGroup laBtn = new RTSButtonGroup();
		// Set RequestFocus to false so that when the hot keys are used
		// the correct focus changes occur.
		setRequestFocus(false);
		// end defect 7894
		laBtn.add(getradioPlt());
		laBtn.add(getradioStkr());
		laBtn.add(getradioPltStkr());
		try
		{
			populateStkrDropDown();
			getradioStkr().setSelected(true);
			getlblErrorRecord().setVisible(false);
			getstcLblErrRecord().setVisible(false);
			getbtnModifyEntry().setEnabled(false);
			getbtnDeleteEntry().setEnabled(false);
			if (getradioStkr().isSelected())
			{
				getcomboPltCode().removeAllItems();
				populateStkrDropDown();
				getcomboStickerCode().setEnabled(true);
				getcomboPltCode().setEnabled(false);
				getcomboStickerCode().requestFocus();
				getstcLblNewPltNo().setEnabled(false);
				getstcLblPltItemCode().setEnabled(false);
			}
			// defect 9085
			getstcLblOrganizationName().setEnabled(false);
			getstcLblSpclPltTypeCode().setEnabled(false);
			getcomboSpclPltTypeCode().setEnabled(false);
			getcomboOrganizationName().setEnabled(false);
			getcomboSpclPltTypeCode().removeAllItems();
			getchkAddlSet().setEnabled(false);
			getchkSpclPlt().setEnabled(true);
			// end defect 9085 
			// defect 10392 
			getcomboPltTerm().removeAllItems();
			getstcLblPltTerm().setEnabled(false);
			getstcLblPltExp().setEnabled(false);
			getcomboPltTerm().setEnabled(false);
			gettxtPltExpMo().setEnabled(false);
			gettxtPltExpYr().setEnabled(false);
			// end defect 10392 
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			aeRTSEx.getFirstComponent().requestFocus();
		} // user code end
	}
	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE ItemEvent 
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		// defect 9085 
		// Do not move focus as moves focus when disabled on annual plts
		// defect 7491
		// added to request focus on checkbox when changed
		//		if (aaIE.getSource() == getchkPrintStkr())
		//		{
		//			getchkPrintStkr().requestFocus();
		//		} 
		// end defect 7491
		// end defect 9085 
		if (aaIE.getSource() == getcomboPltCode())
		{
			// defect 7650
			// reset csNewPlateNo if user has overtyped
			if (gettxtNewPlateNo().isEnabled()
				&& !gettxtNewPlateNo().getText().trim().equals(
					csNewPlateNo))
			{
				csNewPlateNo = gettxtNewPlateNo().getText().trim();
			}
			// end defect 7650 
			boolean lbATVSSelected = false;
			if (getcomboPltCode().getSelectedItem() != null)
			{
				lbATVSSelected =
					getCdFromDropDown(
						(String) getcomboPltCode()
							.getSelectedItem())
							.equals(
						ATVS_CD);
			}
			//fields
			if ((lbATVSSelected || getcomboStickerCode().isEnabled())
				&& !cbDiskEntry)
			{
				getchkPrintStkr().setEnabled(true);
				// defect 7469
				getchkPrintStkr().setSelected(cbPrintChecked);
				// end defect 7469
			}
			else
			{
				getchkPrintStkr().setEnabled(false);
				// defect 5975
				getchkPrintStkr().setSelected(false);
				// end defect 5975 
			}
			if (lbATVSSelected)
			{
				// defect 7546
				clearAllColor(this);
				// end defect 7546
				gettxtNewPlateNo().setEnabled(false);
				// defect 5663
				gettxtNewPlateNo().setText(" ");
				getstcLblNewPltNo().setEnabled(false);
				// end defect 5663
			}
			else
			{
				gettxtNewPlateNo().setEnabled(true);
				gettxtNewPlateNo().setText(csNewPlateNo);
				getstcLblNewPltNo().setEnabled(true);
			}
		}
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param arrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmRegistrationSubcontractorRenewalREG007 laFrmRegistrationSubcontractorRenewal;
			laFrmRegistrationSubcontractorRenewal =
				new FrmRegistrationSubcontractorRenewalREG007();
			laFrmRegistrationSubcontractorRenewal.setModal(true);
			laFrmRegistrationSubcontractorRenewal
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmRegistrationSubcontractorRenewal.show();
			java.awt.Insets laInsets =
				laFrmRegistrationSubcontractorRenewal.getInsets();
			laFrmRegistrationSubcontractorRenewal.setSize(
				laFrmRegistrationSubcontractorRenewal.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmRegistrationSubcontractorRenewal.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmRegistrationSubcontractorRenewal.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Populate data from the screen (and scanner object) and set it to
	 * TempSubcontractorRenewalCacheData
	 * 
	 */
	private void populateDataObject()
	{
		//clear exception 
		caSubcontractorRenewalCacheData.setException(null);
		SubcontractorRenewalData laSubconData =
			new SubcontractorRenewalData();
		//check if bar code object should be set.
		if (useBarCodeScanner())
		{
			laSubconData.setRenewalBarCodeData(caRenewalBarCodeData);
			if (!cbDiffCnty)
			{
				laSubconData.setBarCdIndi(1);
			}
		} //populate the object

		Dollar laFee = new Dollar(gettxtFee().getText());
		laSubconData.setCntyNo(SystemProperty.getOfficeIssuanceNo());
		laSubconData.setCustBaseRegFees(laFee);
		laSubconData.setDocNo(gettxtDocno().getText().trim());
		laSubconData.setNewExpYr(
			Integer.parseInt(gettxtYear().getText()));
		// defect 7553
		if (caRenewalBarCodeData != null)
		{
			long llAuditTrailTransid =
				Long
					.valueOf(
						caRenewalBarCodeData.getAuditTrailTransId())
					.longValue();
			laSubconData.setAuditTrailTransid(llAuditTrailTransid);
		} // end defect 7553

		// Do not cleaup VIN! 
		//subconData.setVin(cleanUpBarCode(gettxtVin().getText().trim()));
		laSubconData.setVIN(gettxtVin().getText().trim());
		if (gettxtNewPlateNo().isEnabled())
		{
			laSubconData.setNewPltNo(
				gettxtNewPlateNo().getText().trim());
		}
		if (getcomboPltCode().isEnabled())
		{
			//subconData.setPltDesc(
			//	getDescFromDropDown((String) getcomboPltCode().getSelectedItem()));
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
			} // end defect 7477 
		} // defect 7348

		// defect 7543 revisit
		//else
		//{
		//	if (caRenewalBarCodeData != null)
		//	{
		//		subconData.setPltItmCd(caRenewalBarCodeData.getRegPltCd());
		//ItemCodesData lItmCode =
		//	ItemCodesCache.getItmCd(caRenewalBarCodeData.getRegPltCd().trim());
		//subconData.setPltDesc(lItmCode.getItmCdDesc());
		//	}
		//}
		//end defect 7348
		// end defect 7543 revisit 
		// defect 7469
		if (getchkPrintStkr().isSelected())
		{
			laSubconData.setPrint(true);
			cbPrintChecked = true;
		}
		else if (getchkPrintStkr().isEnabled())
		{
			cbPrintChecked = false;
		}
		// end defect 7469

		// defect 9085 
		if (ciSpclPltIndi == 1)
		{
			laSubconData.setSpclPltIndi(1);
			laSubconData.setOrgNo(csOrgNo);
			laSubconData.setSpclPltRegPltCd(csSpclRegPltCd);
			laSubconData.setAddlSetIndi(ciAddlSetIndi);
			// defect 10392 
			laSubconData.setPltVldtyTerm(Integer.parseInt(
				getcomboPltTerm().getSelectedItem().toString()));
			if (caRenewalBarCodeData != null)
			{
				laSubconData.setPltNextExpMo(Integer.parseInt(
					caRenewalBarCodeData.getPltNextExpMo()));
				laSubconData.setPltNextExpYr(Integer.parseInt(
					caRenewalBarCodeData.getPltNextExpYr()));
			}
			PlateTypeData laPltTypeData =
				PlateTypeCache.getPlateType(csSpclRegPltCd);
			if (laPltTypeData.isVendorPlt())
			{
				laSubconData.setPltExpMo(Integer.parseInt(
					gettxtPltExpMo().getText().trim()));
				laSubconData.setPltExpYr(Integer.parseInt(
					gettxtPltExpYr().getText().trim()));
			}
			else
			{
				laSubconData.setPltExpMo(Integer.parseInt(
					gettxtExpMo().getText().trim()));
				laSubconData.setPltExpYr(Integer.parseInt(
					gettxtYear().getText().trim()) - 1);
			}
			// end defect 10392 
		}
		// end defect 9085 

		laSubconData.setRecordType(getCurrRecordTypeSelect());
		if (gettxtNewPlateNo().isEnabled())
		{
			laSubconData.setValidatePltIndi(
				SubcontractorRenewalData.VALIDATE_PLT);
		}
		laSubconData.setRegClassCd(
			cleanUpBarCode(gettxtRegClass().getText().trim()));
		laSubconData.setRegExpMo(
			Integer.parseInt(gettxtExpMo().getText().trim()));
		laSubconData.setRegPltNo(
			gettxtCurrentPlateNo().getText().trim());
		laSubconData.setRenwlTotalFees(laFee);
		if (getcomboStickerCode().isEnabled())
		{
			laSubconData.setStkrItmCd(
				getCdFromDropDown(
					(String) getcomboStickerCode().getSelectedItem()));
		}
		laSubconData.setSubconIssueDate(
			gettxtIssueDate().getDate().getYYYYMMDDDate());
		// end Do Not Cleanup VIN 
		caSubcontractorRenewalCacheData.setTempSubconRenewalData(
			laSubconData);
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
	 * Populate the plate combo.
	 * 
	 * @param  abPltOnly boolean
	 * @throws RTSException
	 */
	private void populatePltDropDown(boolean abPltOnly)
		throws RTSException
	{
		getcomboPltCode().removeAllItems();
		if (abPltOnly)
		{
			for (int i = 0; i < svPltCd.size(); i++)
			{
				ItemCodesData laItemCodesData =
					ItemCodesCache.getItmCd((String) svPltCd.get(i));
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
			for (int i = 0; i < svPltSkrCd.size(); i++)
			{
				ItemCodesData laItemCodesData =
					ItemCodesCache.getItmCd((String) svPltSkrCd.get(i));
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
					if (!(svPltCd.contains(laItemCodesData.getItmCd())
						|| svPltSkrCd.contains(
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
	 * Used to recalculate the error indicator
	 * 
	 * @return boolean
	 */
	private boolean recalDisketteValidations()
	{
		//check what type of error it was before
		Set laSet = null;
		//check which type of duplicates it was checking before
		switch (ciErrorSetType)
		{
			case ERR_DOC_NO :
				laSet =
					SubcontractorHelper.getFirstDuplicateSet(
						caSubcontractorRenewalCacheData
							.getTransDocNo());
				break;
			case ERR_VIN :
				laSet =
					SubcontractorHelper.getFirstDuplicateSet(
						caSubcontractorRenewalCacheData.getTransVIN());
				break;
			case ERR_NEW_PLT :
				laSet =
					SubcontractorHelper.getFirstDuplicateSet(
						caSubcontractorRenewalCacheData
							.getTransNewPltNo());
				break;
			case ERR_CURR_PLT :
				laSet =
					SubcontractorHelper.getFirstDuplicateSet(
						caSubcontractorRenewalCacheData
							.getTransCurrPltNo());
				break;
		} //if the previous error is from client validation

		if (ciErrorSetType == ERR_CLIENT_VAL)
		{
			return finalClientSideCheck(false);
		}
		else if (ciErrorSetType == ERR_INV_CHECK)
		{
			Iterator laIterator =
				caSubcontractorRenewalCacheData
					.getInvValIndex()
					.iterator();
			SortedMap laTransInfo =
				caSubcontractorRenewalCacheData.getSubconTransData();
			HashSet lhsRecalErrorSet = new HashSet();
			while (laIterator.hasNext())
			{
				Integer liKey = (Integer) laIterator.next();
				SubcontractorRenewalData laSubconData =
					(SubcontractorRenewalData) laTransInfo.get(liKey);
				if (laSubconData != null && laSubconData.isError())
				{
					lhsRecalErrorSet.add(liKey);
				}
			}
			caSubcontractorRenewalCacheData.setInvValIndex(
				lhsRecalErrorSet);
			if (lhsRecalErrorSet.size() == 0)
			{
				getlblErrorRecord().setVisible(false);
				getstcLblErrRecord().setVisible(false);
			}
			else
			{
				getlblErrorRecord().setText(
					String.valueOf(lhsRecalErrorSet.size()));
			}
			return true;
		}
		else
		{
			String lsCurrentDuplStr =
				getDuplicateString(
					caSubcontractorRenewalCacheData
						.getSubconTransData(),
					laSet,
					ciErrorSetType);
			if (lsCurrentDuplStr != null && csErrInfo != null)
			{
				if (lsCurrentDuplStr.equals(csErrInfo))
				{
					//duplicate of previous validation still exist
					//recalculate the actual count
					getlblErrorRecord().setText(
						String.valueOf(laSet.size()));
					return true;
				}
				else
				{
					//only duplicate records due to new duplicates
					clearErrorSet();
					getlblErrorRecord().setVisible(false);
					getstcLblErrRecord().setVisible(false);
					return false;
				}
			}
			else
			{
				//no more duplicate records
				clearErrorSet();
				getlblErrorRecord().setVisible(false);
				getstcLblErrRecord().setVisible(false);
				return false;
			}
		}
	}

	/**
	 * Repaint all the components
	 * 
	 */
	public void repaintComponents()
	{
		gettxtCurrentPlateNo().repaint();
		gettxtExpMo().repaint();
		gettxtFee().repaint();
		gettxtIssueDate().repaint();
		gettxtNewPlateNo().repaint();
		gettxtRegClass().repaint();
		gettxtYear().repaint();
	}

	/**
	 * Validate the current plate number
	 *
	 * @param aeRTSEx 
	 */
	private void scrValCurrPltNo(RTSException aeRTSEx)
	{
		if (gettxtCurrentPlateNo().getText().equals(""))
		{
			aeRTSEx.addException(
				new RTSException(150),
				gettxtCurrentPlateNo());
		}
	}
	/**
	 * Validate the document number
	 *
	 * @param aeRTSEx 
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
	 * Validate Fee
	 * 
	 * @param aeRTSEx  
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
		// defect 8900
		Dollar laFee = new Dollar(gettxtFee().getText());
		if (!check2Decimals(gettxtFee().getText().trim())
			|| laFee.compareTo(CommonConstant.ZERO_DOLLAR) < 0
			|| laFee.compareTo(new Dollar("9999.99")) > 0)
		{
			aeRTSEx.addException(new RTSException(150), gettxtFee());
			return;
		}
		// end defect 8900

		// input renewal fee greater than MinRegFee + ReflectnFee
		// if reg class is empty, this check cannot be performed
		// defect 7501
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

				// note laCommonFeesData should not be null since 
				// validate in validateRegCd
				// defect 5066
				if (laCommonFeesData == null)
				{
					aeRTSEx.addException(
						new RTSException(150),
						gettxtRegClass());
					return;
				}
				// end defect 5066
				// defect 8900
				if (!laFee.equals(CommonConstant.ZERO_DOLLAR))
				{
					Dollar laMinRegFee =
						laCommonFeesData.getMinRegFee().add(
							laCommonFeesData.getReflectnFee());
					if (laFee.compareTo(laMinRegFee) == -1)
					{
						aeRTSEx.addException(
							new RTSException(706),
							gettxtFee());
						return;
					}
				}
				// end defect 8900
			}
		} // end defect 7501 

		// renwlTotalFee cannot make bundle exceed maximum total
		if (caSubcontractorRenewalCacheData
			.getRunningTotal()
			.compareTo(BUNDLE_MAX_AMT)
			> 0)
		{
			aeRTSEx.addException(new RTSException(622), gettxtFee());
			return;
		}
	}

	//	/**
	//	 * Validate the issue date
	//	 * 
	//	 * @param aeRTSEx 
	//	 */
	//	private void scrValIssueDate(RTSException aeRTSEx)
	//	{
	//		if (!gettxtIssueDate().isValidDate())
	//		{
	//			aeRTSEx.addException(
	//				new RTSException(150),
	//				gettxtIssueDate());
	//			// defect 7656 
	//			// gettxtIssueDate().setDate(null);
	//			// end defect 7656 
	//		}
	//		else
	//		{
	//			int liIssueAMDate = gettxtIssueDate().getDate().getAMDate();
	//			int liCurrentAMDate = new RTSDate().getAMDate();
	//			//PROC Validate SubconIssueAMDate for 0, null or in future
	//			if (liIssueAMDate <= 0)
	//			{
	//				aeRTSEx.addException(
	//					new RTSException(150),
	//					gettxtIssueDate());
	//			}
	//			else if (liIssueAMDate > liCurrentAMDate)
	//			{
	//				aeRTSEx.addException(
	//					new RTSException(427),
	//					gettxtIssueDate());
	//			}
	//			else
	//			{
	//				cbIssueDateValid = true;
	//			}
	//		}
	//		return;
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
			else if (
				laIssueDate.getYear() < caToday.getYear() - 1)
			{
				liErrMsgNo =
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID;
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
	 * @param aeRTSEx RTSException  
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
	 * @param aeRTSEx  
	 */
	private void scrValNewPltNo(RTSException aeRTSEx)
	{
		if (gettxtNewPlateNo().isEnabled())
		{
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
				catch (RTSException aeRTSEx1)
				{
					aeRTSEx.addException(aeRTSEx1, gettxtNewPlateNo());
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
			aeRTSEx.addException(new RTSException(150), gettxtPltExpMo());
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

			aeRTSEx.addException(new RTSException(150), gettxtPltExpYr());
			return;
		}
		int liCurrYr = new RTSDate().getYear();
		if (!(liYr >= liCurrYr - 1 && liYr <= liCurrYr + 10))
		{
			cbValidYear = false;
			aeRTSEx.addException(new RTSException(150), gettxtPltExpYr());
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
		if (getchkSpclPlt().isSelected()
			&& (getcomboSpclPltTypeCode().getSelectedIndex() != -1))
		{
			String lsStkrCd =
				getCdFromDropDown(
					(String) getcomboStickerCode().getSelectedItem());
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
	 * Validate Reg Class code
	 * 
	 * @param aeRTSEx  RTSException
	 */
	private void scrValRegClassCd(RTSException aeRTSEx)
	{
		if (gettxtRegClass().getText().trim().equals(""))
		{
			aeRTSEx.addException(
				new RTSException(150),
				gettxtRegClass());
			return;
		}

		// defect 7501
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
			// Verify that Special Plate is valid for the specified RegClassCd
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
		// end defect 7501 
	}

	/**
	 * Called from actionPerformed to set Print Sticker and 
	 * New Plate Number fields.
	 *  
	 */
	private void setchkBoxNewPltPrtStkr()
	{
		//called from handlePlateBarCode, select combo
		if (csBarCodePltCd != null)
		{
			getcomboPltCode().setSelectedItem(csBarCodePltCd);
			// defect 8479
			comboBoxHotKeyFix(getcomboPltCode());
			// end defect 8479
			csBarCodePltCd = null;
		}
		boolean lbATVSSelected = false;
		if (getcomboPltCode().getSelectedItem() != null)
		{
			lbATVSSelected =
				getCdFromDropDown(
					(String) getcomboPltCode()
						.getSelectedItem())
						.equals(
					ATVS_CD);
		} //fields
		if ((getcomboStickerCode().isEnabled() || lbATVSSelected)
			&& !cbDiskEntry)
		{
			getchkPrintStkr().setEnabled(true);
			// defect 7469
			getchkPrintStkr().setSelected(cbPrintChecked);
			// end defect 7469
		}
		else
		{
			getchkPrintStkr().setEnabled(false);
			getchkPrintStkr().setSelected(false);
		}
		//defect 5692 
		//commented !cdDiskEntry.
		if (getcomboPltCode().isEnabled()
			&& !lbATVSSelected) //		&& !cbDiskEntry)
			// end defect 5692 
		{
			gettxtNewPlateNo().setEnabled(true);
			getstcLblNewPltNo().setEnabled(true);
			gettxtNewPlateNo().setText(csNewPlateNo);
		}
		else
		{
			gettxtNewPlateNo().setEnabled(false);
			// defect 5663
			gettxtNewPlateNo().setText("");
			// end defect 5663 
		}

	}
	/**
	 * All subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * <br> Receives a SubcontractorRenewalCacheData
	 * 
	 * @param aaDataObject 
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			caSubcontractorRenewalCacheData =
				(SubcontractorRenewalCacheData) aaDataObject;

			// cbReEntry is initially false; set to true after the screen
			// has been initialized.  
			if (cbReEntry)
			{
				if (caSubcontractorRenewalCacheData.isModified())
				{
					caSubcontractorRenewalCacheData.setModified(false);
				}
				else
				{
					SubcontractorRenewalData laPreviousTrans =
						(SubcontractorRenewalData) caSubcontractorRenewalCacheData
							.getSubconTransData()
							.get(
								caSubcontractorRenewalCacheData
									.getCurrTransIndex());
					if (laPreviousTrans != null)
					{
						ciStoreLastTransIssueDate =
							laPreviousTrans.getSubconIssueDate();
						gettxtIssueDate().setDate(
							new RTSDate(
								RTSDate.YYYYMMDD,
								ciStoreLastTransIssueDate));
						gettxtRegClass().setText(
							laPreviousTrans.getRegClassCd());
						gettxtExpMo().setText(
							String.valueOf(
								laPreviousTrans.getRegExpMo()));
						gettxtDocno().setText("");
						gettxtVin().setText("");
						// defect 9085 
						resetSpclPlates();
						// end defect 9085 
						if (caSubcontractorRenewalCacheData
							.getHeldInvPlt()
							!= null)
						{
							//gettxtNewPlateNo().setText(
							//	caSubcontractorRenewalCacheData.
							//  getHeldInvPlt().getInvItmNo());
							csNewPlateNo =
								caSubcontractorRenewalCacheData
									.getHeldInvPlt()
									.getInvItmNo();
						}
						else
						{
							gettxtNewPlateNo().setText("");
							// defect 7650
							// Reset saved Plate No
							csNewPlateNo = "";
							// end defect 7650 
						}
						gettxtCurrentPlateNo().setText("");
						gettxtFee().setText("");
						gettxtDocno().requestFocus();
						// defect 7469
						// later in setData(), for all cases, if enabled
						// set to true 
						//if (getchkPrintStkr().isEnabled())
						//{
						//	getchkPrintStkr().setSelected(false);
						//}
						// end defect 7469 
					}
				} //update error set count

				if (caSubcontractorRenewalCacheData
					.getInvValIndex()
					.size()
					> 0
					&& ciErrorSetType != ERR_INV_CHECK)
				{
					//first time error inventory results, not from modify
					ciErrorSetType = ERR_INV_CHECK;
					csErrorSet =
						caSubcontractorRenewalCacheData
							.getInvValIndex();
					getlblErrorRecord().setText(
						String.valueOf(csErrorSet.size()));
					getstcLblErrRecord().setVisible(true);
					getlblErrorRecord().setVisible(true);
				}
				else if (cbDiskEntry && csErrorSet != null)
				{
					recalDisketteValidations();
				}
				else if (
					cbDiskEntry
						&& caSubcontractorRenewalCacheData
							.getInvValIndex()
							.size()
							> 0
						&& ciErrorSetType == ERR_INV_CHECK)
				{
					recalDisketteValidations();
				}
			}
			else
			{
				// defect 8772 
				// Set boolean to denote if restored bundle is from 
				// Current Date.  If not, do not not allow additional
				// transactions. Do not add BarCodeListener.   
				TransactionHeaderData laTransHdrData =
					caSubcontractorRenewalCacheData
						.getTransactionHeaderData();
				if (laTransHdrData != null
					&& laTransHdrData.getTransAMDate()
						!= new RTSDate().getAMDate())
				{
					cbCurrentDate = false;
				}
				else
				{
					try
					{
						caBarCodeScanner =
							getController()
								.getMediator()
								.getAppController()
								.getBarCodeScanner();
						caBarCodeScanner.addBarCodeListener(this);
					} // defect 5827
					catch (RTSException aeRTSEx)
					{
						// defect 11071
//						RTSDialogBox laRTSDiagBox =
//							getController().getMediator().getParent();
//						RTSDeskTop laRTSDeskTop =
//							getController().getMediator().getDesktop();
//						if (laRTSDiagBox != null)
//						{
//							aeRTSEx.displayError(laRTSDiagBox);
//						}
//						else if (laRTSDeskTop != null)
//						{
//							aeRTSEx.displayError(laRTSDeskTop);
//						}
//						else
//						{
//							aeRTSEx.displayError(this);
//						}
						Log.write(Log.DEBUG, this, aeRTSEx.getDetailMsg());
						// end defect 11071
					} // end defect 5827
				}
				// end defect 8772

				if (caSubcontractorRenewalCacheData.getSubconDiskData()
					!= null)
				{
					cbDiskEntry = true;
					cbPrintChecked = false;
					getchkPrintStkr().setEnabled(false);
					getchkPrintStkr().setSelected(cbPrintChecked);
					if (caSubcontractorRenewalCacheData
						.getInvalidRecordsMsg()
						!= null)
					{
						// defect 5827
						RTSException leEx =
							new RTSException(
								RTSException.WARNING_MESSAGE,
								caSubcontractorRenewalCacheData
									.getInvalidRecordsMsg(),
								"");
						RTSDialogBox laRTSDiagBox =
							getController().getMediator().getParent();
						RTSDeskTop laRTSDeskTop =
							getController().getMediator().getDesktop();
						if (laRTSDiagBox != null)
						{
							leEx.displayError(laRTSDiagBox);
						}
						else
						{
							if (laRTSDeskTop != null)
							{
								leEx.displayError(laRTSDeskTop);
							}
							else
							{
								leEx.displayError(this);
							}
						} // end defect5827

						caSubcontractorRenewalCacheData
							.setInvalidRecordsMsg(
							null);
						SubconBundleManager.saveBundle(
							caSubcontractorRenewalCacheData);
					}
				}
				getlblSubcontractor().setText(
					caSubcontractorRenewalCacheData
						.getDisplaySubconInfo());
				// defect 7725
				// If exists, populate issue date from prior transaction
				// Else, use that entered on REG006 
				SubcontractorRenewalData laPreviousTrans =
					(SubcontractorRenewalData) caSubcontractorRenewalCacheData
						.getSubconTransData()
						.get(
							caSubcontractorRenewalCacheData
								.getCurrTransIndex());
				if (laPreviousTrans != null)
				{
					ciStoreLastTransIssueDate =
						laPreviousTrans.getSubconIssueDate();
				}
				else
				{
					ciStoreLastTransIssueDate =
						caSubcontractorRenewalCacheData
							.getSubcontractorHdrData()
							.getSubconIssueDate();
				} // end defect 7725 	

				gettxtIssueDate().setDate(
					new RTSDate(
						RTSDate.YYYYMMDD,
						ciStoreLastTransIssueDate));
				gettxtYear().setText(
					String.valueOf(new RTSDate().getYear() + 1));
				cbReEntry = true;
			}
			((TMREG007) getlstSubconItems().getModel()).add(
				caSubcontractorRenewalCacheData.getSubconTransData());
			if (caSubcontractorRenewalCacheData
				.getSubconTransData()
				.size()
				== 0)
			{
				getbuttonPanel().getBtnEnter().setEnabled(false);
				getbtnDraftReport().setEnabled(false);
				// defect 7894
				//getbtnAddTrans().setNextFocusableComponent(
				//	getbuttonPanel().getBtnCancel());
				// end defect 7894
			}
			else
			{
				getbuttonPanel().getBtnEnter().setEnabled(true);
				getbtnDraftReport().setEnabled(true);
				// defect 7894
				//getbtnAddTrans().setNextFocusableComponent(
				//	getlstSubconItems());
				// end defect 7894
			}
			getlblTransEntered().setText(
				String.valueOf(
					caSubcontractorRenewalCacheData
						.getSubconTransData()
						.size()));
			getlblSubtotal().setText(
				caSubcontractorRenewalCacheData
					.getRunningTotal()
					.printDollar());
			// defect 7469
			if (getchkPrintStkr().isEnabled())
			{
				getchkPrintStkr().setSelected(cbPrintChecked);
			}
			setchkBoxNewPltPrtStkr();
			// end defect 7469 
			// defect 7537
			if (caSubcontractorRenewalCacheData.getSubconDiskData()
				!= null)
			{
				gettxtIssueDate().setEnabled(false);
				getstcLblIssueDate().setEnabled(false);
			} // end defect 7537

			// defect 8772 
			if (!cbCurrentDate)
			{
				getlstSubconItems().removeActionListener(this);
				getlstSubconItems().removeMultipleSelectionListener(
					this);
				getbtnAddTrans().setEnabled(false);
				getbuttonPanel().getBtnEnter().setEnabled(false);
				RTSException leRTSEx =
					new RTSException(
						RTSException.FAILURE_VERIFICATION,
						"You are Restoring a Saved "
							+ "Subcontractor Bundle from a Prior Date.<br><br>"
							+ "Please use Cancel to Delete.",
						"ERROR",
						true);
				leRTSEx.setBeep(RTSException.BEEP);
				throw leRTSEx;
			}
			// end defect 8772 
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
	}
	/**
	 * 
	 * Show Record error
	 * 
	 * @param aiErrorCount
	 */
	public void showError(int aiErrorCount)
	{
		getlblErrorRecord().setVisible(true);
		getstcLblErrRecord().setVisible(true);
		getlblErrorRecord().setText(String.valueOf(aiErrorCount));
	}

	/**
	 * Invoked when the target of the listener has changed its state.
	 *
	 * @param aaCE  a ChangeEvent object
	 * @deprecated 
	 */
	public void stateChanged(javax.swing.event.ChangeEvent aaCE)
	{
		// defect 7506
		//  Plate/Sticker/Plate-Sticker radio buttons listener
		//  have been change to action. These radio buttons no longer
		//  enter this method.
		try
		{
			// defect 7492
			// Clear errors on re selection 
			clearAllColor(this);
			// end defect 7492 
			// defect 7489
			// Save entered Plate No
			//PLATE
			if (aaCE.getSource() == getradioPlt())
			{
				if (getradioPlt().isSelected())
				{
					getcomboStickerCode().removeAllItems();
					populatePltDropDown(true);
					getcomboStickerCode().setEnabled(false);
					getcomboPltCode().setEnabled(true);
					getcomboPltCode().requestFocus();
				}
				else
				{
					if (gettxtNewPlateNo().isEnabled())
					{
						csNewPlateNo = gettxtNewPlateNo().getText();
					}
				}
			}
			else
			{ // STICKER 	

				if (aaCE.getSource() == getradioStkr())
				{
					if (getradioStkr().isSelected())
					{
						getcomboPltCode().removeAllItems();
						populateStkrDropDown();
						getcomboStickerCode().setEnabled(true);
						getcomboPltCode().setEnabled(false);
						getcomboStickerCode().requestFocus();
						getstcLblNewPltNo().setEnabled(false);
					}
				}
				else
				{ // PLATE STICKER 			

					if (aaCE.getSource() == getradioPltStkr())
					{
						if (getradioPltStkr().isSelected())
						{
							populatePltDropDown(false);
							populateStkrDropDown();
							getcomboPltCode().setEnabled(true);
							getcomboStickerCode().setEnabled(true);
							getcomboPltCode().requestFocus();
							getstcLblNewPltNo().setEnabled(true);
						}
						else
						{
							if (gettxtNewPlateNo().isEnabled())
							{
								csNewPlateNo =
									gettxtNewPlateNo().getText();
							}
						}
					}
				}
			} // end defect 7489 

			//called from handlePlateBarCode, select combo
			if (csBarCodePltCd != null)
			{
				getcomboPltCode().setSelectedItem(csBarCodePltCd);
				// defect 8479
				comboBoxHotKeyFix(getcomboPltCode());
				// end defect 8479
				csBarCodePltCd = null;
			}
			boolean lbATVSSelected = false;
			if (getcomboPltCode().getSelectedItem() != null)
			{
				lbATVSSelected =
					getCdFromDropDown(
						(String) getcomboPltCode()
							.getSelectedItem())
							.equals(
						ATVS_CD);
			}
			//fields
			if ((getcomboStickerCode().isEnabled() || lbATVSSelected)
				&& !cbDiskEntry)
			{
				getchkPrintStkr().setEnabled(true);
				// defect 7469
				getchkPrintStkr().setSelected(cbPrintChecked);
				// end defect 7469
			}
			else
			{
				getchkPrintStkr().setEnabled(false);
				getchkPrintStkr().setSelected(false);
			}
			// defect 5692
			// commented !cbDiskEntry.
			if (getcomboPltCode().isEnabled()
				&& !lbATVSSelected) //	&& !cbDiskEntry)
			{
				gettxtNewPlateNo().setEnabled(true);
				getstcLblNewPltNo().setEnabled(true);
			}
			else
			{
				gettxtNewPlateNo().setEnabled(false);
				// defect 5663
				gettxtNewPlateNo().setText("");
				// end defect 5663 
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
	}

	/**
	 * Determine if should use the scanned data 
	 * 
	 * @return boolean
	 */
	private boolean useBarCodeScanner()
	{
		if (caRenewalBarCodeData != null)
		{ //current plate, exp mo, reg class, fee

			if (caRenewalBarCodeData
				.getDocNo()
				.equals(gettxtDocno().getText().trim())
				&& caRenewalBarCodeData.getVin().trim().equals(
					gettxtVin().getText().trim())
				&& caRenewalBarCodeData.getRegClassCd()
					== Integer.parseInt(gettxtRegClass().getText())
				&& Integer.parseInt(caRenewalBarCodeData.getRegExpMo())
					== Integer.parseInt(gettxtExpMo().getText())
				&& caRenewalBarCodeData.getRegPltNo().trim().equals(
					gettxtCurrentPlateNo().getText().trim())
				&& caRenewalBarCodeData.getRenwlPrice().compareTo(
					new Dollar(gettxtFee().getText().trim()))
					== 0)
			{
				return true;
			}
		}
		return false;
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
		csSpclRegPltCd = "";
		csOrgNo = "";
		
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
	 * Validate disk entries.  Return true to continue processing.
	 * 
	 * @param SubcontractorRenewalData 
	 * @return boolean 
	 */
	private boolean validateDiskEntries(SubcontractorRenewalData aaSubconData)
	{
		//improve performance to split up to basic and secondary checks
		//Basic field validations
		if (diskValIssueYear(aaSubconData)
			&& diskValIssueDate(aaSubconData)
			&& diskValNewPltNo(aaSubconData)
			&& diskValExpMo(aaSubconData)
			&& diskValRegClassCd(aaSubconData)
			&& diskValFee(aaSubconData)
			&& // defect 5751
		diskValDocNo(aaSubconData))
		{
		}
		else
		{
			return false;
		} //confirmation

		return (
			diskConfirmIssueDate(aaSubconData)
				&& diskConfirmMinMaxFee(aaSubconData));
	}

	/**
	 * Validate the entries
	 * 
	 * @return boolean 
	 */
	private boolean validateEntries()
	{
		//improve performance to split up to basic and secondary checks
		//Basic field validations
		RTSException leRTSEx = new RTSException();
		//getbtnAddTrans().requestFocus();
		// defect 7501 
		cbIssueDateValid = false;
		// end defect 7501 
		scrValIssueYear(leRTSEx);
		// defect 9085 
		// Validate Selection of Special Plate and Organization if 
		// Special Plate chkbox is selected. 
		scrValSpclPlt(leRTSEx);
		scrValSpclOrgNo(leRTSEx);
		scrValSpclPltStkr(leRTSEx);
		// end defect 9085 
		// defect 7501
		scrValDocNo(leRTSEx);
		// defect 7674
		// scrValVin(rtsException);
		// end defect 7674 
		scrValIssueDate(leRTSEx);
		scrValNewPltNo(leRTSEx);
		scrValCurrPltNo(leRTSEx);
		scrValExpMo(leRTSEx);
		// Also validate RegClassCd & Fees if Issue Date is Valid
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
		//defect 5071
		//Check to see if any of the above checks failed. If they did, 
		//the following checks will throw exceptions. Hence, we will 
		//display the error message at this point and prevent the
		//exceptions in the next two checks.

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			//defect 7505
			leRTSEx.getFirstComponent().requestFocus();
			//defect 7505
			return false;
		}
		// end defect 5071
		// end defect 7501 

		leRTSEx = new RTSException();
		RTSException leEx = null;
		leEx =
			SubcontractorHelper.validateDuplDocNo(
				caSubcontractorRenewalCacheData,
				gettxtDocno().getText().trim(),
				false);
		if (leEx != null)
		{
			leRTSEx.addException(leEx, gettxtDocno());
			leEx = null;
		}
		// defect 7674
		// Do not validate if no VIN entered 
		if (gettxtVin().getText().trim().length() != 0)
		{
			leEx =
				SubcontractorHelper.validateDuplVIN(
					caSubcontractorRenewalCacheData,
					gettxtVin().getText().trim(),
					false);
			if (leEx != null)
			{
				leRTSEx.addException(leEx, gettxtVin());
				leEx = null;
			}
		}
		// end defect 7674 

		if (gettxtNewPlateNo().isEnabled())
		{
			leEx =
				SubcontractorHelper.validateDuplNewPlt(
					caSubcontractorRenewalCacheData,
					gettxtNewPlateNo().getText().trim(),
					false);
			if (leEx != null)
			{
				leRTSEx.addException(leEx, gettxtNewPlateNo());
				leEx = null;
			}
		}
		leEx =
			SubcontractorHelper.validateDuplCurrPlt(
				caSubcontractorRenewalCacheData,
				gettxtCurrentPlateNo().getText().trim(),
				false);
		if (leEx != null)
		{
			leRTSEx.addException(leEx, gettxtCurrentPlateNo());
			leEx = null;
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			//defect 7505
			leRTSEx.getFirstComponent().requestFocus();
			//defect 7505
			return false;
		} //confirmation

		return (confirmIssueDate() && confirmMinMaxFee());
	}
	/** 
	 * Called whenever the value of the selection changes.
	 * 
	 * @param aaLSE ListSelectionEvent 
	 */

	public void valueChanged(
		javax.swing.event.ListSelectionEvent aaLSE)
	{
		if (!aaLSE.getValueIsAdjusting())
		{
			switch (getlstSubconItems().getSelectedRows().length)
			{
				case 0 :
					getbtnDeleteEntry().setEnabled(false);
					getbtnModifyEntry().setEnabled(false);
					break;
				case 1 :
					getbtnDeleteEntry().setEnabled(true);
					getbtnModifyEntry().setEnabled(true);
					break;
				default : //many rows selected

					getbtnDeleteEntry().setEnabled(true);
					getbtnModifyEntry().setEnabled(false);
			}
		}
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
