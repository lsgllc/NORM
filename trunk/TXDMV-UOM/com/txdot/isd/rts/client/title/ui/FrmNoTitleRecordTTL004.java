package com.txdot.isd.rts.client.title.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.registration.business.RegistrationSpecialExemptions;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

import java.awt.Dimension;
import javax.swing.JCheckBox;
import java.awt.Point;

/*
 * 
 * FrmNoTitleRecordTTL004.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validation updated
 * T Pederson   04/30/2002	Don't allow spaces in input field
 * T Pederson   04/30/2002	Corrected actionPerformed error displays
 * J Rue		06/27/2002	Set VehBdyType to 'UT' if no record found in
 *							RTS table.
 *							modify populateBodyTypes()
 *							defect 4367
 * J Rue		07/02/2002	Search dealer title data if MF did not 
 * 							return a VehBdyType. 
 *							add buildDlrVehBdyType()
 *							modify populateBodyTypes()
 *							defect 4398
 * J Rue		07/22/2002	Set vehicle brand N = 'NOT ACTUAL MILEAGE",
 *							X = "EXCEEDS MECH. LIMITS" in vehicle
 *							odometer brand combo box
 *							modify setData()
 *							defect 4498, 4499
 * J Rue		07/25/2002	Correct logic to disable VehOdoMtrBrand if
 *							VehOdoMtrReading = EXEMPT.
 *							modify setData()
 *							defect 4513 
 * S Govindappa	07/29/2002	Changed actionPerformed method to display
 *							error 207("The tonage is in error. An
 * 							examination is required.) and proceed to
 *							next screen on pressing OK button.
 *							defect 4529
 * J Rue		08/08/2002	The TTL004 screen in RTSII for travel
 *							trailer length is set set to 0 if blank.
 *							modify setData()
 *							defect 4567
 * MAbs			08/26/2002	PCR 41
 * MAbs			09/17/2002	PCR 41 Integration
 * J Rue		10/21/2002	Change cursor position from
 *							gettxtVehicleEmptyWeight() to
 *							gettxtVehicleCarryingCapacity() if failed.
 *							modify actionPerformed()
 *							defect 4832
 * K Salvi		10/30/2002	CQU100004905 fix - populate width if at
 *							least one of feet and inches is not empty.
 * B Arredondo	12/03/2002	Made changes to user help guide so had to
 *							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Made changes for the user help guide so had
 *							to make changes in actionPerformed().
 *							defect 5147
 * J Rue		02/03/2003	Initialize tonnage if not required.
 *							modify disabledFields()
 *							defect 5173
 * M Wang 		08/19/2003  Chenged properties on the screen for XP.
 *                          Defect 6003. Version 5.1.6
 * B Arredondo	10/14/2003	Modified Input to default of -1.
 *							modify gettxtMake()
 *							defect 5187
 * B Arr/J Rue	10/28/2003	Added code from VCTitleTypesTTL002.
 * 							processData() which fixed code for defect 
 * 							5590 but caused the above defects.
 *							modify setDatatoDataObject()
 *							defect 6029, 6090, 6134, and 6312
 * J Seifert	12/10/2003	Added validation on the two replica fields
 *							- Model Year/Make
 *							Rules: If one field is entered then then
 *							both have to have data,Year must be valid.
 *							modify actionPerformed()
 *							defect 6311 ver. 5.1.5.2
 * B Hargrove	11/18/2004  Change errmsg process so that they beep:
 *							model year, vehicle tonnage
 *							see also : FrmTitleRecordTTL003.
 *							            actionPerformed()
 *							add error messages to RTS_ERR_MSGS
 *							modified actionPerformed()
 *							defect 7062, 7064 Ver 5.2.2
 * B Hargrove	11/30/2004  Format Class comments, import statements
 *							defect 7064 Ver 5.2.2
 * J Rue		02/25/2005	VAJ to WSAD Clean Up
 * 							modify itemStateChanged(), initialize() 
 * 							populateVehicleMakes()
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/10/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/14/2005	Set tabbing to RTS standards
 * 							Remove unused variables
 * 							deprecated isDTA()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3   
 * S Johnston	06/23/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							defect 8240 Ver 5.2.3             
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/17/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * T Pederson	12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify populateBodyTypes(), populateBrand(), 
 * 							populateTrailerType(), 
 * 							populateVehicleMakes() and setData().
 * 							defect 8479 Ver 5.2.3
 * K Harrell	12/20/2005	Changes for focus on odometer, empty weight
 * 							add cbErrorOnOdometer
 * 							modify actionPerformed(),focusLost() 
 * 							defect 7898 Ver 5.2.3   
 * Jeff S.		01/09/2006	Made changes to handle the recalc on the 
 * 							focus lost of the odometer txt field.  This 
 * 							will be handled the same both the TTL003 and
 * 							the TTL004 screen.  Also removed the 
 * 							actionListener on all of the text fields.
 * 							This listeners was not allowing the enter
 * 							action to be taken when enter was pressed
 * 							when focus was in one of these fields.
 * 							add keyPressed(), setVehOdomtrBrand(),
 * 								cbTabKeyPressed
 *							modify focusLost(), 
 *								gettxtVehicleOdometerReading(),
 *								gettxtReplicaVehicleMake(), 
 *								gettxtReplicaVehicleModelYear(), 
 *								gettxtVehicleCarryingCapacity(),
 *								gettxtVehicleEmptyWeight(),
 *								gettxtVehicleLength(), 
 *								gettxtVehicleModel(),
 *								gettxtVehicleModelYear(),
 *								gettxtVehicleOdometerReading(),
 *								gettxtVehicleTonnage(), 
 *								gettxtVehicleVIN()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	02/06/2006	Convert I/O to 1/0 in Body VIN
 *							modify focusLost(), gettxtVehicleVIN()  
 *							defect 8523 Ver 5.2.2 Fix 8
 * K Harrell	02/10/2006	Ensure Brand set appropriately
 * 							modify setDataToDataObject()
 * 							defect 7898 Ver 5.2.3   
 * B Hargrove	02/13/2006	Ensure Odometer Reading is not blank before
 * 							trying to set the Brand.
 * 							modify setDataToDataObject()  
 * 							defect 7898 Ver 5.2.3
 * K Harrell	03/07/2006	One last look at Odometer Reading
 * 							modify setDataToDataObject()
 * 							defect 7898 Ver 5.2.3
 * T Pederson	09/08/2006	Added button to get the presumptive value
 * 							of vehicle displayed using VIN and odometer
 * 							reading.
 * 							add getbtnSPV()
 *							modify actionPerformed(), getJPanel2(),
 *							keyPressed()
 * 							defect 8926 Ver 5.2.5
 * Jeff S.		09/15/2006	Added the Office of Issuance as a value
 * 							that is passed from the client so that
 * 							we can send it to BlackBook for reporting.
 * 							Added Mnemonic to SPV button.
 * 							add SPV
 * 							modify actionPerformed()
 * 							defect 8926 Ver 5.2.5
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify focusLost()
 * 							defect 8902 Ver Exempts
 * K Harrell	11/22/2006	Remove earlier Exempt Coding removing error
 * 							messages. 
 * 							defect 8900 Ver Exempts
 * K Harrell	03/09/2007	Use SystemProperty.isCounty()
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/06/2007	CommonValidations.convert_i_and_o_to_1_and_0() 
 * 							call is now static
 * 							defect 9085 Ver Special Plates   
 * B Hargrove	12/31/2008	Add check for Vehicle Class when checking
 * 							Trk<=1 (Apportioned (RegClassCd 6) does not
 * 							have 'Appr<=1' type of RegClassCd, so must 
 * 							check VehClassCd = 'Trk <=1').
 * 							modify actionPerformed()
 * 							defect 8703 Ver Defect_POS_D
 * B Hargrove	01/10/2009	Add check for Vehicle Class when checking
 * 							Trk>1 so can get rid of hard-coded
 * 							RegClassCds in TitleClientUtilityMethods. 
 * 							modify actionPerformed()
 * 							defect 9910 Ver Defect_POS_D
 * K Harrell	12/16/2009	DTA Cleanup
 * 							modify buildDlrBdyType()
 * 							defect 10290 Ver Defect_POS_H
 * K Harrell	07/11/2010	add Hard Stop Evaluation for Special Plate
 * 							  ElectionPndngIndi 
 * 							add cbAlreadySet
 * 							add verifyIndicators() 
 * 							modify actionPerformed(), setData(), 
 * 							defect 10507 Ver 6.5.0
 * K Harrell	09/23/2010	modify to change odometer to EXEMPT based 
 * 							upon TIMA
 * 							add isValidYear(), resetOdometerBrand() 
 * 							delete isInvalidYear() 
 * 							modify actionPerformed(), keyPressed(), 
 * 							 focusLost()
 * 							defect 10597 Ver 6.6.0  
 * T Pederson	01/31/2011	Added vehicle color drop down boxes 
 * 							add ivjcomboMajorColor, ivjcomboMinorColor,
 * 							ivjstcLblColorMajor, ivjstcLblColorMinor,
 * 							MAJORCOLOR, MINORCOLOR
 * 							add getcomboMajorColor(), getcomboMinorColor(),
 * 							populateMajorColor(), populateMinorColor(),
 * 							getMajorColorCdSelected(),
 * 							getMinorColorCdSelected(),
 * 							isMajorColorReqdButNotSelected(),
 * 							isMajorColorSameAsMinorColor(),
 * 							isMinorColorSelectedMajorColorNotSelected()
 * 							modify setData(), setDataToDataObject(),
 * 							actionPerformed()
 * 							defect 10689 Ver 6.7.0
 * K Harrell	02/03/2011	Remove use of error msg 2235.  Not needed as
 * 							implemented focusLost(), keyPressed() 
 * 							solutions.  (defect 10597)   
 * 							modify actionPerformed()
 * 							defect 10707 Ver 6.7.0 
 * K Harrell	10/15/2011	Add checkbox for Travel Trailer 
 * 							add ivjchkTravelTrailer, get method
 * 							add TRAVEL_TRAILER
 * 							add handleTravelTrailer() 
 * 							modify itemStateChanged(),disableFields() 
 * 							defect 11049 Ver 6.9.0
 * K Harrell	11/07/2011	Implement CommonFees max/min weight validation
 * 							delete MAX_GROSS_WT
 * 							modify actionPerformed(), setData() 
 * 							defect 10959 Ver 6.9.0 
 * K Harrell	11/13/2011	Do not allow 12 inches on Trailer Length
 * 							add INCHES_PER_FOOT
 * 							modify actionPerformed(), setDataToDataObject()
 * 							defect 11049 Ver 6.9.0
 * K Harrell	11/17/2011	Show blank if 0
 * 							add setFromRegisClassVehData(), int2bool() 
 * 							modify setData(), disableFields(), 
 * 								resetOdometerBrand(), itemStateChanged()  
 * 							defect 11126 Ver 6.9.0  
 * K Harrell	11/22/2011	modify disableFields() 
 * 							defect 11126 Ver 6.9.0 	
 * ---------------------------------------------------------------------
 */
/**
 * This form is used to capture vehicle information of a new vehicle
 * applying for title. 
 *
 * @version	6.9.0 			11/22/2011
 * @author	Todd Pederson
 * <br>Creation Date:		06/27/2001 20:54:25
 */
public class FrmNoTitleRecordTTL004
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{
	private JPanel ivjFrmNoTitleRecordTTL004ContentPane1 = null;
	private JLabel ivjstcLblBodyStyle = null;
	private JLabel ivjstcLblBodyVIN = null;
	private JLabel ivjstcLblBrand = null;
	private JLabel ivjstcLblCarryingCapacity = null;
	private JLabel ivjstcLblEmptyWeight = null;
	private JLabel ivjstcLblGrossWeight = null;
	// defect 10689 
	private JLabel ivjstcLblColorMajor = null;
	private JLabel ivjstcLblColorMinor = null;
	// end defect 10689 
	private JLabel ivjstcLblModel = null;
	private JLabel ivjstcLblNewTitleType = null;
	private JLabel ivjstcLblOdometerReading = null;
	private JLabel ivjstcLblRegistrationClass = null;
	private JLabel ivjstcLblTonnage = null;
	private JLabel ivjstcLblVehicleClass = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblYearMake = null;
	private JLabel ivjstcLblReplicaYearMake = null;
	private JLabel ivjstcLblTrailerType = null;
	private JLabel ivjstcLblTravelTlrLength = null;
	private JLabel ivjlblClassCodeDesc = null;
	private JLabel ivjlblClassCodeDesc1 = null;
	private JLabel ivjlblVehicleClassCode = null;
	private JComboBox ivjcomboBodyType = null;
	private JComboBox ivjcomboVehicleMake = null;
	private JLabel ivjlblVIN = null;
	private RTSInputField ivjtxtVehicleModel = null;
	private RTSInputField ivjtxtVehicleModelYear = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkFixedWeight = null;
	private JCheckBox ivjchkPermitRequired = null;
	// defect 10689 
	private JComboBox ivjcomboMajorColor = null;
	private JComboBox ivjcomboMinorColor = null;
	// end defect 10689 
	private JComboBox ivjcomboTrailerType = null;
	private JComboBox ivjcomboVehicleOdometerBrand = null;
	private JLabel ivjlblVehicleGrossWeight = null;
	private RTSInputField ivjtxtReplicaVehicleMake = null;
	private RTSInputField ivjtxtReplicaVehicleModelYear = null;
	private RTSInputField ivjtxtVehicleCarryingCapacity = null;
	private RTSInputField ivjtxtVehicleEmptyWeight = null;
	private RTSInputField ivjtxtVehicleLength = null;
	private RTSInputField ivjtxtVehicleOdometerReading = null;
	private RTSInputField ivjtxtVehicleTonnage = null;
	private RTSInputField ivjtxtVehicleVIN = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;

	private VehicleInquiryData caVehInqData = null;
	private RegistrationData caRegData = null; 
	private VehicleData caVehData = null; 
	private RegistrationClassData caRegClassData = null;
	private RTSInputField ivjtxtVehMake = null;
	private JPanel ivjJPanel4 = null;
	private JLabel ivjstcLblFeet = null;
	private JLabel ivjstcLblInches = null;
	private JLabel ivjstcLblTlrWidth = null;
	private RTSInputField ivjtxtFeet = null;
	private RTSInputField ivjtxtInches = null;

	// boolean 
	private boolean cbChkKeyOnCombo;
	// defect 7898
	// added to handle who needs to handle the focuslost event
	// If KeyPressed has already handled it there is no need to.
	private boolean cbTabKeyPressed = false;
	// end defect 7898

	// defect 10507
	private boolean cbAlreadySet = false;
	// end defect 10507  

	// defect 10689 
	private Vector cvVehColor = new Vector();
	// end defect 10689 

	// Constant int
	private final static int ZERO = 0;
	// defect 10959 
	// private static int MAX_GROSS_WT = 80000;
	// end defect 10959
	private final static int VEH_YEAR_MAX_LEN = 4;
	private final static int FEET_MAX_LEN = 2;
	private final static int INCHES_MAX_LEN = 2;
	private final static int VEH_MK_MAX_LEN = 4;
	private final static int CARRY_CAP_MAX_LEN = 6;
	private final static int EMPTY_WT_MAX_LEN = 6;
	private final static int VEH_LNGTH_MAX_LEN = 2;
	private final static int VEH_MODL_MAX_LEN = 3;
	private final static int MODL_YR_MAX_LEN = 4;
	private final static int ODOMTR_RNDG_MAX_LEN = 6;
	private final static int TONNAGE_MAX_LEN = 5;
	private final static int VIN_MAX_LEN = 22;
	
	// defect 11049 
	private static final int INCHES_PER_FOOT = 12;
	// end defect 11049 

	// Constants String
	private static final String NEW = "-NEW- -  ";
	private static final String EXEMPT = "EXEMPT";
	private static final String MISC = "MISC";
	private final static String EMPTY = CommonConstant.STR_SPACE_EMPTY;
	private final static String SPACE_DASH =
		CommonConstant.STR_SPACE_ONE + CommonConstant.STR_DASH;

	private final static String FIXED_WT = "Fixed Weight";
	private final static String PARK_MODL_TRLR = "Park Model Trailer";
	private final static String FULL = "FULL";
	private final static String CODE_DESCR = "Code Description";
	private final static String CODE = "Code";
	private final static String LBLVIN = "VIN";
	private final static String BODY_STYLE = "Body Style:";
	private final static String BODY_VIN = "Body VIN:";
	private final static String BRAND = "Brand:";
	private final static String CARRY_CAPACITY = "Carrying Capacity:";
	private final static String EMPTY_WT = "Empty Weight:";
	private final static String FT = "Ft";
	private final static String GROSS_WT = "Gross Weight:";
	private final static String INCHES = "Inches";
	// defect 11049 
	private final static String TRAVEL_TRAILER = "Travel Trailer";
	// end defect 11049
	// defect 10689 
	private static final String MAJORCOLOR = "Major Color:";
	private static final String MINORCOLOR = "Minor Color:";
	// end defect 10689 
	private final static String MODEL = "Model:";
	private final static String NEW_TTL_TYPE = "New Title Type:";
	private final static String ODOMTR_READNG = "Odometer Reading:";
	private final static String REGIS_CLASS = "Registration Class:";
	private final static String REPLICA_YR_MK = "Replica Year/Make:";
	//defect 8926
	private final static String SPV = "SPV";
	// end defect 8926
	private final static String TRVL_TRLR_WIDTH = "Travel Tlr Width:";
	private final static String TONNAGE = "Tonnage:";
	private final static String TRLR_TYPE = "Trailer Type:";
	private final static String TRVL_TRLR_LENGTH = "Travel Tlr Length:";
	private final static String VEH_CLASS = "Vehicle Class:";
	private final static String VIN = "VIN:";
	private final static String YEAR_MAKE = "Year/Make:";
	private final static String ACTUAL_MILEAGE = "ACTUAL MILEAGE";

	private RTSButton btnSPV = null;
	
	// defect 11049
	private JCheckBox ivjchkTravelTrailer = null;
	// end defect 11049 

	/**
	 * FrmNoTitleRecordTTL004 constructor
	 */
	public FrmNoTitleRecordTTL004()
	{
		super();
		initialize();
	}

	/**
	 * FrmNoTitleRecordTTL004 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmNoTitleRecordTTL004(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmNoTitleRecordTTL004 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmNoTitleRecordTTL004(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when user performs an action.  The user completes the 
	 * screen by making appropriate selections/entries and pressing 
	 * enter.
	 *
	 * Cancel or Help buttons respectively result in destroying the 
	 * window or providing appropriate help.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}

		RTSException leRTSEx = new RTSException();
		try
		{
			//Clear All Fields
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				UtilityMethods.trimRTSInputField(this); 
				
				//Begin validations
				TitleData laTtlData =
					caVehInqData.getMfVehicleData().getTitleData();

				RegistrationData laRegData =
					caVehInqData.getMfVehicleData().getRegData();

				String lsVehClss = new String(EMPTY);
				if (caVehData.getVehClassCd() != null)
				{
					lsVehClss = caVehData.getVehClassCd().trim();
				}

				int liRegCd = laRegData.getRegClassCd();
				String lsRegPltCd = laRegData.getRegPltCd();
				String lsYrMdl =
					gettxtVehicleModelYear().getText();

				String lsVehMake = null;
				if (getcomboVehicleMake().isVisible())
				{
					lsVehMake =
						(String) getcomboVehicleMake()
							.getSelectedItem();
				}
				else
				{
					lsVehMake = gettxtVehMake().getText();
				}

				String lsVin = getlblVIN().getText();

				// defect 10597 
				resetOdometerBrand();
				// end defect 10597 

				String lsOdoRndng =
					gettxtVehicleOdometerReading().getText();
				String lsEmptyWt =
					gettxtVehicleEmptyWeight().getText();
				String lsCarrCap =
					gettxtVehicleCarryingCapacity().getText();
				int liGrossWt =
					CommonValidations.calcGrossWeight(
						lsEmptyWt,
						lsCarrCap);
				getlblVehicleGrossWeight().setText(
					Integer.toString(liGrossWt));
				String lsGrossWt =
					getlblVehicleGrossWeight().getText();
				String lsVehTon =
					gettxtVehicleTonnage().getText();
				String lsTrlLen =
					gettxtTrlrLength().getText();
				// PCR 30C  
				String lsInches = gettxtInches().getText();
				if (lsInches.equals(EMPTY))
				{
					lsInches = CommonConstant.STR_ZERO;
				}

				// defect 11049 
				if (gettxtInches().isEnabled()
						&& Integer.parseInt(lsInches) >= INCHES_PER_FOOT)
				{
					// 150 
					leRTSEx.addException(
							new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
							gettxtInches());
				}
				// end defect 11049
				
				String lsWidth =
					gettxtFeet().getText()
						+ CommonConstant.STR_PERIOD
						+ lsInches;
				
				// END PCR 30C
				String lsStrTrlType =
					(String) getcomboTrailerType().getSelectedItem();

				// If veh class is misc and title type is original or 
				//	RPO dont let the user go ahead.
				if (lsVehClss.equals(MISC)
					&& ((laTtlData.getTtlTypeIndi()
						== TitleTypes.INT_ORIGINAL)
						|| laTtlData.getTtlTypeIndi()
							== TitleTypes.INT_REGPURPOSE))
				{
					RTSException leRTSEx472 = new RTSException(472);
					leRTSEx472.displayError(this);
					return;
				}

				// If VIN is empty and vehClsCd is not misc dont let the
				//	user go ahead.
				if (lsVin.length() == ZERO && !lsVehClss.equals(MISC))
				{
					RTSException leRTSEx176 = new RTSException(176);
					leRTSEx176.displayError(this);
					return;
				}

				// defect 10597 
				// Implement isValidYear 
				if (!isValidYear(lsYrMdl))
				{
					// end defect 10597 

					// defect 7062	
					//laRTSE.addException(new RTSException(RTSException.
					//	WARNING_MESSAGE,"Model Year is not valid",
					//	"ERROR!"), gettxtVehicleModelYear());
					leRTSEx.addException(
						new RTSException(2006),
						gettxtVehicleModelYear());
					// end defect 7062	
				}
				if (gettxtVehicleTonnage().isEnabled()
					&& TitleClientUtilityMethods
						.isVehTonInvalidForDisabledPlates(
						lsRegPltCd,
						lsVehTon))
				{
					// defect 7064	
					//String lsMsgType = RTSException.FAILURE_MESSAGE;
					//String lsMsg = "Vehicle tonnage cannot be greater 
					//	than two tons for Disabled PersonOld or Disabled
					//	Veteran Plates";
					//String lsMsgTtl = "Validate carrying capacity";
					//laRTSE.addException(new RTSException(lsMsgType, 
					//	lsMsg, lsMsgTtl), gettxtVehicleTonnage());
					leRTSEx.addException(
						new RTSException(2010),
						gettxtVehicleTonnage());
					// end defect 7064	
				}
				if (getcomboVehicleMake().isVisible()
					&& getcomboVehicleMake().getSelectedIndex() == -1)
				{
					leRTSEx.addException(
						new RTSException(150),
						getcomboVehicleMake());
				}
				// defect 10689 
				if (isMajorColorReqdButNotSelected()
					|| isMinorColorSelectedMajorColorNotSelected())
				{
					leRTSEx.addException(
						new RTSException(150),
						getcomboMajorColor());
				}
				else if (isMajorColorSameAsMinorColor())
				{
					leRTSEx.addException(
						new RTSException(160),
						getcomboMinorColor());
				}
				// end defect 10689 
				if (getcomboBodyType().getSelectedIndex() == -1)
				{
					leRTSEx.addException(
						new RTSException(150),
						getcomboBodyType());
				}
				if (gettxtVehicleOdometerReading().isEnabled()
					&& lsOdoRndng.length() == ZERO)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtVehicleOdometerReading());
				}
				if (gettxtVehicleOdometerReading().isEnabled()
					&& !TitleClientUtilityMethods.isValidOdometerReading(
						lsOdoRndng))
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtVehicleOdometerReading());
				}
				// defect 10959
				if (gettxtVehicleEmptyWeight().isEnabled()
					&& gettxtVehicleEmptyWeight().isEmpty())
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtVehicleEmptyWeight());
				}
				if (gettxtVehicleCarryingCapacity().isEnabled()
					&& gettxtVehicleCarryingCapacity().isEmpty())
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtVehicleCarryingCapacity());
				}
				// end defect 10959 
				
				if (gettxtVehicleOdometerReading().isEnabled()
					&& TitleClientUtilityMethods
						.isInvalidOdometerReadingBasedOnTIMA(
						lsYrMdl,
						lsVehTon,
						lsGrossWt,
						lsOdoRndng))
				{
					leRTSEx.addException(
						new RTSException(711),
						gettxtVehicleOdometerReading());
				}
				if (lsVehMake != null && lsVehMake.length() < 1)
				{
					if (gettxtVehMake().isVisible())
					{
						leRTSEx.addException(
							new RTSException(150),
							gettxtVehMake());
					}
					else
					{
						leRTSEx.addException(
							new RTSException(150),
							getcomboVehicleMake());
					}
				}
				// defect 6311
				// Added validation for the two replica fields - 
				//	Model Year, Make
				// Rules: If one is entered then then both have to have 
				//	data, Year must be valid
				if (gettxtReplicaVehicleModelYear().isEnabled()
					&& gettxtReplicaVehicleMake().isEnabled())
				{
					if (!gettxtReplicaVehicleModelYear().isEmpty()
						|| !gettxtReplicaVehicleMake().isEmpty())
					{
						if (gettxtReplicaVehicleModelYear().isEmpty()
							|| CommonValidations.isInvalidYearModel(
								gettxtReplicaVehicleModelYear()
									.getText()
									))
						{
							// defect 7062	
							//laRTSE.addException(new RTSException(
							//	RTSException.WARNING_MESSAGE,
							//	"Model Year is not valid",
							//	MiscellaneousRegConstant.ERROR_TITLE),
							//	gettxtReplicaVehicleModelYear());
							leRTSEx.addException(
								new RTSException(2006),
								gettxtReplicaVehicleModelYear());
							// end defect 7062	
						}
						if (gettxtReplicaVehicleMake().isEmpty())
						{
							leRTSEx.addException(
								new RTSException(150),
								gettxtReplicaVehicleMake());
						}
					}
				}
				// end defect 6311
				// defect 10959 
				//				if (gettxtVehicleEmptyWeight().isEnabled()
				//					&& TitleClientUtilityMethods
				//						.isEmptyWtInvalidForRegClassLsEq6000(
				//						liRegCd,
				//						lsEmptyWt))
				//				{
				//					leRTSEx.addException(
				//						new RTSException(2),
				//						gettxtVehicleEmptyWeight());
				//				}
				//				if (gettxtVehicleEmptyWeight().isEnabled()
				//					&& TitleClientUtilityMethods
				//						.isEmptyWtInvalidForRegClassGrt6000(
				//						liRegCd,
				//						lsEmptyWt))
				//				{
				//					leRTSEx.addException(
				//						new RTSException(2),
				//						gettxtVehicleEmptyWeight());
				//				}
				// end defect 10959
				
				if (!getchkFixedWeight().isSelected()
					&& TitleClientUtilityMethods.isTrlCarrCapInvalid(
						caRegClassData,
						lsEmptyWt,
						lsCarrCap))
				{
					leRTSEx.addException(
						new RTSException(13),
						gettxtVehicleCarryingCapacity());
				}
				if (TitleClientUtilityMethods
					.isCarrCapInvalid(
						caRegClassData,
						lsGrossWt,
						lsCarrCap,
						lsVehTon,
						caVehInqData
							.getMfVehicleData()
							.getVehicleData()
							.getFxdWtIndi()))
				{
					leRTSEx.addException(
						new RTSException(200),
						gettxtVehicleCarryingCapacity());
				}
				if (gettxtVehicleTonnage().isEnabled()
					&& TitleClientUtilityMethods.isVehTonInvalid(
						lsVehTon))
				{
					leRTSEx.addException(
						new RTSException(201),
						gettxtVehicleTonnage());
				}
				// defect 8703
				// Add Vehicle Class (Apportioned does not have specific
				// '<= 1 ton' and '> 1 ton' Reg Classes.
				String lsVehClass =
					caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getVehClassCd();
				if (TitleClientUtilityMethods
					.isVehTonInvalidForTruckLsEqOne(
						liRegCd,
						lsVehClass,
						lsVehTon))
				{
					// end defect 8703
					leRTSEx.addException(
						new RTSException(201),
						gettxtVehicleTonnage());
				}
				// defect 9910
				// Add Vehicle Class 
				if (TitleClientUtilityMethods
					.isVehTonInvalidForTruckGrt1orComb(
						liRegCd,
						lsVehClass,
						lsVehTon))
				{
					// end defect 9910
					leRTSEx.addException(
						new RTSException(201),
						gettxtVehicleTonnage());
				}
				
				
				
				if (TitleClientUtilityMethods
					.isFrmTrlMinWt(caRegClassData, lsGrossWt))
				{
					leRTSEx.addException(
						new RTSException(202),
						gettxtVehicleCarryingCapacity());
				}

				if (getchkFixedWeight().isSelected()
					&& gettxtVehicleTonnage().isEnabled()
					&& TitleClientUtilityMethods.isFixedWtIndi(
						caRegClassData,
						lsVehTon,
						lsGrossWt))
				{
					leRTSEx.addException(
						new RTSException(90),
						gettxtVehicleEmptyWeight());
				}
				if (!TitleClientUtilityMethods
					.isNonTtlTrlrWtValid(
						caRegClassData,
						lsVehClss,
						lsStrTrlType,
						lsGrossWt,
						lsEmptyWt))
				{
					if (liGrossWt > 34000)
					{
						displayError(96);
					}
					else if (liGrossWt > 4000)
					{
						leRTSEx.addException(
							new RTSException(580),
							gettxtVehicleEmptyWeight());
					}
				}
				if (TitleClientUtilityMethods
					.isTrailerWtInvalid(
						lsVehClss,
						liRegCd,
						lsStrTrlType,
						lsGrossWt,
						lsEmptyWt))
				{
					leRTSEx.addException(
						new RTSException(142),
						gettxtVehicleEmptyWeight());

				}
				
				// defect 10959
				//	if (liGrossWt > MAX_GROSS_WT)
				//		{
				//			leRTSEx.addException(
				//				new RTSException(89),
				//				gettxtVehicleEmptyWeight());
				//		}
				if (caRegClassData.getEmptyWtReqd() ==1)
				{
					try
					{
						TitleClientUtilityMethods.validateGrossWtForRegClassCd(liRegCd,liGrossWt); 
					}
					catch (RTSException aeRTSEx)
					{
						leRTSEx.addException(aeRTSEx, gettxtVehicleEmptyWeight()); 
					}
				}

				//				if (TitleClientUtilityMethods
				//					.isGrossWtInvalidForCombVeh(liRegCd, lsGrossWt))
				//				{
				//					leRTSEx.addException(
				//						new RTSException(371),
				//						gettxtVehicleEmptyWeight());
				//				}
				// end defect 10959 
				
				// PCR 30C
				// defect 11049
				//				if (gettxtInches().isEnabled()
				//					&& !lsStrInches.equals(EMPTY)
				//					&& Double.parseDouble(lsStrInches) > 12.0)
				//				{
				//					leRTSEx.addException(
				//						new RTSException(150),
				//						gettxtInches());
				//				}
				// end defect 11049
				if (gettxtTrlrLength().isEnabled()
					&& getchkPermitRequired().isSelected()
					&& TitleClientUtilityMethods
						.isLengthInvalidForParkTlr(
						lsTrlLen)
					&& !TitleClientUtilityMethods.isWidthValidForParkTlr(
						lsWidth))
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtTrlrLength());
				}
				// END PCR 30C
				if (gettxtTrlrLength().isEnabled()
					&& !getchkPermitRequired().isSelected()
					&& TitleClientUtilityMethods
						.isLengthInvalidForTrvTrlr(
						lsTrlLen))
				{
					leRTSEx.addException(
						new RTSException(203),
						gettxtTrlrLength());
				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				else if (
					gettxtVehicleEmptyWeight().isEnabled()
						&& isVINAEmptyWt())
				{
					displayError(3);
				}
				if (gettxtVehicleTonnage().isEnabled()
					&& isVINAInBegTonAndEndTon())
				{
					RTSException RTSExp207 = new RTSException(207);
					RTSExp207.displayError(this);
				}

				// defect 10507 
				if (verifyIndicators())
				{
					setDataToDataObject();

					getController().processData(
						AbstractViewController.ENTER,
						getController().getData());
				}
				// end defect 10507 
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{

				String lsTransCd = getController().getTransCode();

				if (lsTransCd.equals(TransCdConstant.TITLE)
					|| lsTransCd.equals(TransCdConstant.NONTTL)
					|| lsTransCd.equals(TransCdConstant.REJCOR))
				{
					if (UtilityMethods.isMFUP())
					{
						RTSHelp.displayHelp(RTSHelp.TTL004A);
					}
					else
					{
						RTSHelp.displayHelp(RTSHelp.TTL004B);
					}
				}
				else if (
					lsTransCd.equals(TransCdConstant.DTAORK)
						|| lsTransCd.equals(TransCdConstant.DTANTK))
				{
					RTSHelp.displayHelp(RTSHelp.TTL004C);
				}
				else if (
					lsTransCd.equals(TransCdConstant.DTAORD)
						|| lsTransCd.equals(TransCdConstant.DTANTD))
				{
					RTSHelp.displayHelp(RTSHelp.TTL004D);
				}
			}
			// defect 8926
			else if (aaAE.getSource() == getbtnSPV())
			{
				PresumptiveValueData laPresumptiveValueDataNew =
					new PresumptiveValueData();
				String lsOdmtrRdng =
					gettxtVehicleOdometerReading().getText();
				int liOdmtrRdng = 0;
				try
				{
					liOdmtrRdng = Integer.parseInt(lsOdmtrRdng);
				}
				catch (NumberFormatException aeNFEx)
				{
					liOdmtrRdng = 0;
				}
				laPresumptiveValueDataNew.setOdometerReading(
					liOdmtrRdng);
				laPresumptiveValueDataNew.setVIN(
					caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getVin());
				// Used for reporting
				laPresumptiveValueDataNew.setOfficeIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				getController().processData(
					VCNoTitleRecordTTL004.PRESUMP_VAL,
					laPresumptiveValueDataNew);
			}
			// end defect 8926
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * If dealer title data exist, search VehBdyType vector for match.
	 * -1 means no match found
	 *
	 * @param avVehBdyTypes Vector
	 * @return int
	 */
	protected int buildDlrVehBdyType(Vector avVehBdyTypes)
	{
		int liBdyDefault = -1;

		// get Dealer data
		TitleValidObj laTtlValObj =
			(TitleValidObj) caVehInqData.getValidationObject();

		// defect 10290
		// refactored from laDlrTTlData  
		DealerTitleData laDlrTtlData =
			(DealerTitleData) laTtlValObj.getDlrTtlData();

		// If Dealer Data exist, search VehBdyType vector
		if (laDlrTtlData != null)
		{
			MFVehicleData laMFVehDTA =
				(MFVehicleData) laDlrTtlData.getMFVehicleData();
			// end defect 10290 

			VehicleData laVehDtDTA =
				(VehicleData) laMFVehDTA.getVehicleData();
			String lsDTABdyType = laVehDtDTA.getVehBdyType();
			if (lsDTABdyType != null && !lsDTABdyType.equals(EMPTY))
			{
				if (avVehBdyTypes != null)
				{
					for (int liIndex = 0;
						liIndex < avVehBdyTypes.size();
						liIndex++)
					{
						VehicleBodyTypesData laVehBdyType =
							(VehicleBodyTypesData) avVehBdyTypes.get(
								liIndex);
						if (lsDTABdyType != null
							&& laVehBdyType.getVehBdyType().equals(
								lsDTABdyType)
							&& liBdyDefault == -1)
						{
							liBdyDefault = liIndex;
						}
					}
				}
			}
		}
		return liBdyDefault;
	}

	/**
	  * Disables fields based on data read from RegClass cache.
	  */
	 private void disableFields()
	 {
		 getstcLblBodyVIN().setEnabled(true);
		 getstcLblOdometerReading().setEnabled(true);
		 getstcLblBrand().setEnabled(true);

		 boolean lbEnable = true;

		 if (caRegClassData != null)
		 {
			 // defect 11126 
			  // Vehicle Body VIN
			setFromRegisClassVehData(caRegClassData.getBdyVinReqd(),
					getstcLblBodyVIN(), gettxtVehicleBodyVIN(),
					caVehData.getVehBdyVin());

			
			// Fixed Weight &&  Carrying Capacity
		    // Note that FxdWtReqd is not really Required. Means it is available. 
		    
		    // If Fixed, Carrying Capacity is not collected
		    lbEnable = int2bool(caRegClassData.getFxdWtReqd());
		    if (!lbEnable)
		    {
		    	getchkFixedWeight().setSelected(false); 
		    }
		    getchkFixedWeight().setEnabled(lbEnable);
		    
		    int liValue = caRegClassData.getCaryngCapReqd();
		    liValue = getchkFixedWeight().isSelected() ? 0 : liValue; 
		    
		    setFromRegisClassVehData(liValue, 
		      getstcLblCarryingCapacity(), gettxtVehicleCarryingCapacity(), 
		      ""+caRegData.getVehCaryngCap());
		    
		    // Empty Weight
		    setFromRegisClassVehData(caRegClassData.getEmptyWtReqd(), 
		      getstcLblEmptyWeight(), gettxtVehicleEmptyWeight(), 
		      ""+caVehData.getVehEmptyWt());
		    
		    // Gross Weight
		    recalcGrossWt(); 
		    lbEnable = int2bool(caRegClassData.getEmptyWtReqd());
		    getstcLblGrossWeight().setEnabled(lbEnable);  
		    getlblVehicleGrossWeight().setEnabled(lbEnable);
		    
			 // Tonnage 
			 setFromRegisClassVehData(caRegClassData.getTonReqd(),getstcLblTonnage(),
					 gettxtVehicleTonnage(),""+ caVehData.getVehTon());

		    //Length
		    setFromRegisClassVehData(caRegClassData.getLngthReqd(),getstcLblTlrLength(),
		      gettxtTrlrLength(),""+caVehData.getVehLngth());

		    // Width - Feet  
			 setFromRegisClassVehData(caRegClassData.getWidthReqd(),getstcLblTlrWidth(),
					 gettxtFeet(),(""+ (int)caVehData.getVehWidth()/12));

			 // Width - Inches
			 setFromRegisClassVehData(caRegClassData.getWidthReqd(),getstcLblTlrWidth(),
					 gettxtInches(),(""+ (int)caVehData.getVehWidth() %INCHES_PER_FOOT));

			 getstcLblFeet().setEnabled(int2bool(caRegClassData.getWidthReqd()));
			 getstcLblInches().setEnabled(int2bool(caRegClassData.getWidthReqd()));

			 // Odometer
			 if (caRegClassData.getOdmtrReqd() == ZERO)
			 {
				 gettxtVehicleOdometerReading().setEnabled(false);
				 getstcLblOdometerReading().setEnabled(false);
				 gettxtVehicleOdometerReading().setText(EMPTY);
				 // clear fields
				 getcomboVehicleOdometerBrand().removeAllItems();
				 getcomboVehicleOdometerBrand().setEnabled(false);
				 getcomboVehicleOdometerBrand().setSelectedIndex(-1);
				 getstcLblBrand().setEnabled(false);
			 }
			 else
			 {
				  // Odometer 
				  setFromRegisClassVehData(caRegClassData.getOdmtrReqd(), 
						      getstcLblOdometerReading(), gettxtVehicleOdometerReading(), 
						      caVehData.getVehOdmtrReadng() == null ? new String() : 
						    		  ""+caVehData.getVehOdmtrReadng());
				  
				 gettxtVehicleOdometerReading().setEnabled(true);
			 }
			 // end defect 11126 
			 
			 // Permit To Move  (Park Trailer) 
			 // defect 11049 
			 lbEnable = caRegClassData.getPrmtToMoveReqd() != ZERO;
			 getchkPermitRequired().setEnabled(lbEnable);
			 if (!lbEnable)
			 {
				 getchkPermitRequired().setSelected(false);
			 }
			 handleTravelTrailerCheckbox();
			 // end defect 11049

			 // Trailer Type Required
			 lbEnable = caRegClassData.getTrlrTypeReqd() != ZERO;
			 getstcLblTrailerType().setEnabled(lbEnable);
			 getcomboTrailerType().setEnabled(lbEnable);
			 if (!lbEnable)
			 {
				 getcomboTrailerType().setSelectedIndex(-1);
				 if (caVehData != null)
				 {
					 caVehData.setTrlrType(null);
				 }
			 }
			 else
			 {
				 getcomboTrailerType().setSelectedIndex(0);
			 }

		 }
	 }


	/**
	 * Displays the error message associated with the error number 
	 * passed.
	 * 
	 * @param aiErrCd int
	 */
	private void displayError(int aiErrCd)
	{
		RTSException leRTSEx = new RTSException(aiErrCd);
		leRTSEx.displayError(this);
	}
	private void displayMsgIfOutOfScope()
	{
//		 defect 9085
		if (caVehInqData
			.getMfVehicleData()
			.getRegData()
			.getRegPltCd()
			!= null
			&& !caVehInqData
				.getMfVehicleData()
				.getRegData()
				.getRegPltCd()
				.equals(
				EMPTY)
			&& RegistrationSpecialExemptions
				.verifyPltTypeScope(
				caVehInqData
					.getMfVehicleData()
					.getRegData())
				!= ZERO
			&& SystemProperty.isCounty())
			// end defect 9085 
		{
			RTSException leRTSEx18 = new RTSException(18);
			RTSDialogBox laRTSDBox =
				getController().getMediator().getParent();
			if (laRTSDBox != null)
			{
				leRTSEx18.displayError(laRTSDBox);
			}
			else
			{
				leRTSEx18.displayError(this);
			}
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
	 * Invoked when Odometer Reading, Empty Weight or Carrying Capacity 
	 * field loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		// Vehicle Empty Weight 
		if (!aaFE.isTemporary()
			&& aaFE.getSource() == gettxtVehicleEmptyWeight())
		{
			String lsEmptyWt =
				gettxtVehicleEmptyWeight().getText();
			String lsCarryingCap =
				gettxtVehicleCarryingCapacity().getText();
			int liGrossWt =
				CommonValidations.calcGrossWeight(
					lsEmptyWt,
					lsCarryingCap);
			getlblVehicleGrossWeight().setText(
				Integer.toString(liGrossWt));

		}
		// defect 8523
		// Convert Body Vin 
		else if (
			!aaFE.isTemporary()
				&& aaFE.getSource() == gettxtVehicleBodyVIN())
		{
			String lsVin =
				gettxtVehicleBodyVIN().getText().toUpperCase();
			// defect 8902
			lsVin = CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
			// end defect 8902
			gettxtVehicleBodyVIN().setText(lsVin);
		}
		// end defect 8523
		// Carrying Capacity 
		else if (
			!aaFE.isTemporary()
				&& aaFE.getSource() == gettxtVehicleCarryingCapacity())
		{
			String lsEmptyWt =
				gettxtVehicleEmptyWeight().getText();
			String lsCarryingCap =
				gettxtVehicleCarryingCapacity().getText();
			int liGrossWt =
				CommonValidations.calcGrossWeight(
					lsEmptyWt,
					lsCarryingCap);
			getlblVehicleGrossWeight().setText(
				Integer.toString(liGrossWt));
		}
		// Vehicle Odometer Reading 
		// defect 7898
		// Replaced the commented out code with the code below so that
		// TTL003 would handle the OdometerReading the same.
		else if (aaFE.getSource() == gettxtVehicleOdometerReading())
		{

			// If KeyPressed has already handles the setVehOdomtrBrand
			// then don't do it here.
			if (!cbTabKeyPressed)
			{
				setVehOdomtrBrand();
			}
			else
			{
				cbTabKeyPressed = false;
			}
		}
		//			else if (
		//				aaFE.getSource() == gettxtVehicleOdometerReading())
		//			{
		//				String lsOdoRd =
		//					gettxtVehicleOdometerReading().getText().trim();
		//				try
		//				{
		//					// defect 7898
		//					//	The purpose of this code is to determine if 
		//					//	something other than numerics were entered.
		//					//	This would indicate "EXEMPT" was entered.
		//					Integer.parseInt(lsOdoRd);
		//					// end defect 7898
		//					if (TitleClientUtilityMethods
		//						.isValidOdometerReading(lsOdoRd))
		//					{
		//						getstcLblBrand().setEnabled(true);
		//						getcomboVehicleOdometerBrand().setEnabled(true);
		//						populateBrand();
		//						if (!cbErrorOnOdometer)
		//						{
		//							aaFE.getOppositeComponent().requestFocus();
		//						}
		//						else
		//						{
		//							cbErrorOnOdometer = false;
		//							gettxtVehicleOdometerReading()
		//								.requestFocus();
		//						}
		//					}
		//				}
		//				catch (NumberFormatException aeNFE)
		//				{
		//					if (lsOdoRd.equalsIgnoreCase(EXEMPT))
		//					{
		//						getstcLblBrand().setEnabled(false);
		//						getcomboVehicleOdometerBrand().setEnabled(
		//							false);
		//						getcomboVehicleOdometerBrand().removeAllItems();
		//						if (!cbErrorOnOdometer)
		//						{
		//							if (aaFE
		//								.getOppositeComponent()
		//								.equals(getcomboVehicleOdometerBrand()))
		//							{
		//								gettxtVehicleEmptyWeight()
		//									.requestFocus();
		//							}
		//							else
		//							{
		//								aaFE
		//									.getOppositeComponent()
		//									.requestFocus();
		//							}
		//						}
		//						else
		//						{
		//							cbErrorOnOdometer = false;
		//						}
		//					}
		//				}
		//			}
		// end defect 7898

		// defect 10597
		resetOdometerBrand();
		// end defect 10597 
	}

	/**
	 * Return the btnSPV property value.
	 * 
	 * @return javax.swing.JButton
	 */
	private com.txdot.isd.rts.client.general.ui.RTSButton getbtnSPV()
	{
		if (btnSPV == null)
		{
			btnSPV =
				new com.txdot.isd.rts.client.general.ui.RTSButton();
			btnSPV.setBounds(210, 210, 93, 27);
			btnSPV.setText(SPV);
			btnSPV.setMnemonic(KeyEvent.VK_S);
			btnSPV.addActionListener(this);
		}
		return btnSPV;
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
				ivjButtonPanel1.setBounds(167, 384, 321, 40);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
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
	 * Return the chkFixedWeight property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkFixedWeight()
	{
		if (ivjchkFixedWeight == null)
		{
			try
			{
				ivjchkFixedWeight = new JCheckBox();
				ivjchkFixedWeight.setName("chkFixedWeight");
				ivjchkFixedWeight.setMnemonic(70);
				ivjchkFixedWeight.setText(FIXED_WT);
				ivjchkFixedWeight.setMaximumSize(new Dimension(97, 22));
				ivjchkFixedWeight.setActionCommand("JCheckBox1");
				ivjchkFixedWeight.setBounds(84, 106, 156, 20);
				ivjchkFixedWeight.setMinimumSize(new Dimension(97, 22));
				// user code begin {1}

				ivjchkFixedWeight.addItemListener(this);

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkFixedWeight;
	}

	/**
	 * Return the chkPermitRequired property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkPermitRequired()
	{
		if (ivjchkPermitRequired == null)
		{
			try
			{
				ivjchkPermitRequired = new JCheckBox();
				ivjchkPermitRequired.setName("chkPermitRequired");
				ivjchkPermitRequired.setMnemonic(80);
				ivjchkPermitRequired.setSize(new Dimension(156, 20));
				ivjchkPermitRequired.setLocation(new Point(84, 152));
				ivjchkPermitRequired.setText(PARK_MODL_TRLR);
				ivjchkPermitRequired.setMaximumSize(
					new Dimension(97, 22));
				ivjchkPermitRequired.setActionCommand("JCheckBox1");
				ivjchkPermitRequired.setMinimumSize(
					new Dimension(97, 22));
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
		return ivjchkPermitRequired;
	}
	
	/**
	 * This method initializes ivjchkTravelTrailer	
	 * 	
	 * @return JCheckBox	
	 */
	private JCheckBox getchkTravelTrailer()
	{
		if (ivjchkTravelTrailer == null)
		{
			ivjchkTravelTrailer = new JCheckBox();
			ivjchkTravelTrailer.setText(TRAVEL_TRAILER);
			ivjchkTravelTrailer.setLocation(new Point(84, 128));
			ivjchkTravelTrailer.setSize(new Dimension(136, 20));
			ivjchkTravelTrailer.setEnabled(false);
			ivjchkTravelTrailer.addItemListener(this);
		}
		return ivjchkTravelTrailer;
	}
	/**
	 * Return the comboBodyType property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboBodyType()
	{
		if (ivjcomboBodyType == null)
		{
			try
			{
				ivjcomboBodyType = new JComboBox();
				ivjcomboBodyType.setBounds(127, 39, 152, 24);
				ivjcomboBodyType.setName("comboBodyType");
				ivjcomboBodyType.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboBodyType.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboBodyType.setBackground(
					new Color(255, 255, 255));
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
		return ivjcomboBodyType;
	}

	/**
	 * Return the comboMajorColor property value.
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getcomboMajorColor()
	{
		if (ivjcomboMajorColor == null)
		{
			try
			{
				ivjcomboMajorColor = new javax.swing.JComboBox();
				ivjcomboMajorColor.setBounds(127, 94, 178, 24);
				ivjcomboMajorColor.setName("comboMajorColor");
				ivjcomboMajorColor.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboMajorColor.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboMajorColor.setBackground(java.awt.Color.white);
				// user code begin (1)
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboMajorColor;
	}

	/**
	 * Return the comboMinorColor property value.
	 * 
	 * @return javax.swing.JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JComboBox getcomboMinorColor()
	{
		if (ivjcomboMinorColor == null)
		{
			try
			{
				ivjcomboMinorColor = new javax.swing.JComboBox();
				ivjcomboMinorColor.setBounds(127, 125, 178, 24);
				ivjcomboMinorColor.setName("comboMinorColor");
				ivjcomboMinorColor.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboMinorColor.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboMinorColor.setBackground(java.awt.Color.white);
				// user code begin (1)
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboMinorColor;
	}

	/**
	 * Return the LocalRenderer property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboTrailerType()
	{
		if (ivjcomboTrailerType == null)
		{
			try
			{
				javax
					.swing
					.plaf
					.basic
					.BasicComboBoxRenderer
					.UIResource ivjLocalRenderer;
				ivjLocalRenderer =
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource();
				ivjLocalRenderer.setName("LocalRenderer");
				ivjLocalRenderer.setText(FULL);
				ivjLocalRenderer.setMaximumSize(new Dimension(30, 16));
				ivjLocalRenderer.setMinimumSize(new Dimension(30, 16));
				ivjLocalRenderer.setForeground(new Color(0, 0, 0));
				ivjcomboTrailerType = new JComboBox();
				ivjcomboTrailerType.setName("comboTrailerType");
				ivjcomboTrailerType.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboTrailerType.setRenderer(ivjLocalRenderer);
				ivjcomboTrailerType.setBackground(
					new Color(255, 255, 255));
				ivjcomboTrailerType.setBounds(127, 233, 109, 20);
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
		return ivjcomboTrailerType;
	}

	/**
	 * Return the comboVehicleMake property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboVehicleMake()
	{
		if (ivjcomboVehicleMake == null)
		{
			try
			{
				ivjcomboVehicleMake = new JComboBox();
				ivjcomboVehicleMake.setName("comboVehicleMake");
				ivjcomboVehicleMake.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboVehicleMake.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboVehicleMake.setBackground(
					new Color(255, 255, 255));
				ivjcomboVehicleMake.setVisible(true);
				ivjcomboVehicleMake.setBounds(52, 7, 188, 23);
				// user code begin {1}
				ivjcomboVehicleMake.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboVehicleMake;
	}

	/**
	 * Return the comboVehicleOdometerBrand property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getcomboVehicleOdometerBrand()
	{
		if (ivjcomboVehicleOdometerBrand == null)
		{
			try
			{
				javax
					.swing
					.plaf
					.metal
					.MetalComboBoxEditor
					.UIResource ivjLocalEditor;
				ivjLocalEditor =
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource();
				ivjLocalEditor.setItem(ACTUAL_MILEAGE);
				ivjcomboVehicleOdometerBrand = new JComboBox();
				ivjcomboVehicleOdometerBrand.setName(
					"comboVehicleOdometerBrand");
				ivjcomboVehicleOdometerBrand.setEditor(ivjLocalEditor);
				ivjcomboVehicleOdometerBrand.setBounds(
					127,
					245,
					176,
					22);
				ivjcomboVehicleOdometerBrand.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboVehicleOdometerBrand.setBackground(
					new Color(255, 255, 255));
				// user code begin {1}

				populateBrand();

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjcomboVehicleOdometerBrand;
	}

	/**
	 * Return the FrmNoTitleRecordTTL004ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmNoTitleRecordTTL004ContentPane1()
	{
		if (ivjFrmNoTitleRecordTTL004ContentPane1 == null)
		{
			try
			{
				ivjFrmNoTitleRecordTTL004ContentPane1 = new JPanel();
				ivjFrmNoTitleRecordTTL004ContentPane1.setName(
					"FrmNoTitleRecordTTL004ContentPane1");
				ivjFrmNoTitleRecordTTL004ContentPane1.setLayout(null);
				ivjFrmNoTitleRecordTTL004ContentPane1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmNoTitleRecordTTL004ContentPane1.setMinimumSize(
					new Dimension(0, 0));
				ivjFrmNoTitleRecordTTL004ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				ivjFrmNoTitleRecordTTL004ContentPane1.add(
					getJPanel2(),
					null);
				ivjFrmNoTitleRecordTTL004ContentPane1.add(
					getJPanel3(),
					null);
				ivjFrmNoTitleRecordTTL004ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmNoTitleRecordTTL004ContentPane1.add(
					getJPanel1(),
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
		return ivjFrmNoTitleRecordTTL004ContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
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
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new GridBagLayout());
				ivjJPanel1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(new Dimension(400, 27));
				GridBagConstraints laConstraintsstcLblVehicleClass =
					new GridBagConstraints();
				laConstraintsstcLblVehicleClass.gridx = 1;
				laConstraintsstcLblVehicleClass.gridy = 1;
				laConstraintsstcLblVehicleClass.ipadx = 3;
				laConstraintsstcLblVehicleClass.ipady = 3;
				laConstraintsstcLblVehicleClass.insets =
					new Insets(14, 61, 7, 6);
				getJPanel1().add(
					getstcLblVehicleClass(),
					laConstraintsstcLblVehicleClass);
				GridBagConstraints laConstraintslblVehicleClassCode =
					new GridBagConstraints();
				laConstraintslblVehicleClassCode.gridx = 2;
				laConstraintslblVehicleClassCode.gridy = 1;
				laConstraintslblVehicleClassCode.ipadx = 60;
				laConstraintslblVehicleClassCode.ipady = 3;
				laConstraintslblVehicleClassCode.insets =
					new Insets(14, 6, 7, 5);
				getJPanel1().add(
					getlblVehicleClassCode(),
					laConstraintslblVehicleClassCode);
				GridBagConstraints laConstraintsstcLblRegistrationClass =
					new GridBagConstraints();
				laConstraintsstcLblRegistrationClass.gridx = 3;
				laConstraintsstcLblRegistrationClass.gridy = 1;
				laConstraintsstcLblRegistrationClass.ipadx = 5;
				laConstraintsstcLblRegistrationClass.ipady = 3;
				laConstraintsstcLblRegistrationClass.insets =
					new Insets(14, 6, 7, 4);
				getJPanel1().add(
					getstcLblRegistrationClass(),
					laConstraintsstcLblRegistrationClass);
				GridBagConstraints laConstraintslblClassCodeDesc1 =
					new GridBagConstraints();
				laConstraintslblClassCodeDesc1.gridx = 4;
				laConstraintslblClassCodeDesc1.gridy = 1;
				laConstraintslblClassCodeDesc1.ipadx = 125;
				laConstraintslblClassCodeDesc1.ipady = 3;
				laConstraintslblClassCodeDesc1.insets =
					new Insets(14, 4, 7, 50);
				getJPanel1().add(
					getlblClassCodeDesc1(),
					laConstraintslblClassCodeDesc1);
				GridBagConstraints laConstraintsstcLblNewTitleType =
					new GridBagConstraints();
				laConstraintsstcLblNewTitleType.gridx = 1;
				laConstraintsstcLblNewTitleType.gridy = 2;
				laConstraintsstcLblNewTitleType.ipadx = 6;
				laConstraintsstcLblNewTitleType.ipady = 3;
				laConstraintsstcLblNewTitleType.insets =
					new Insets(7, 53, 15, 6);
				getJPanel1().add(
					getstcLblNewTitleType(),
					laConstraintsstcLblNewTitleType);
				GridBagConstraints laConstraintslblClassCodeDesc =
					new GridBagConstraints();
				laConstraintslblClassCodeDesc.gridx = 2;
				laConstraintslblClassCodeDesc.gridy = 2;
				laConstraintslblClassCodeDesc.gridwidth = 3;
				laConstraintslblClassCodeDesc.ipadx = 183;
				laConstraintslblClassCodeDesc.ipady = 3;
				laConstraintslblClassCodeDesc.insets =
					new Insets(7, 6, 15, 212);
				getJPanel1().add(
					getlblClassCodeDesc(),
					laConstraintslblClassCodeDesc);
				// user code begin {1}
				ivjJPanel1.setBounds(13, 4, 603, 68);
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
	 * Return the JPanel2 property value.
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
				ivjJPanel2.setLayout(null);
				ivjJPanel2.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjJPanel2.setMinimumSize(new Dimension(365, 208));

				ivjJPanel2.add(getstcLblYearMake(), null);
				ivjJPanel2.add(getstcLblBodyStyle(), null);
				ivjJPanel2.add(getstcLblModel(), null);
				ivjJPanel2.add(getstcLblVIN(), null);
				ivjJPanel2.add(getlblVIN(), null);
				ivjJPanel2.add(getstcLblBodyVIN(), null);
				ivjJPanel2.add(getstcLblOdometerReading(), null);
				ivjJPanel2.add(getstcLblBrand(), null);
				ivjJPanel2.add(getJPanel4(), null);
				ivjJPanel2.add(getcomboBodyType(), null);
				ivjJPanel2.add(gettxtVehicleModel(), null);
				ivjJPanel2.add(gettxtVehicleBodyVIN(), null);
				ivjJPanel2.add(gettxtVehicleOdometerReading(), null);
				ivjJPanel2.add(getcomboVehicleOdometerBrand(), null);
				ivjJPanel2.add(getbtnSPV(), null);
				ivjJPanel2.add(getcomboMajorColor(), null);
				ivjJPanel2.add(getcomboMinorColor(), null);
				ivjJPanel2.add(getstcLblColorMinor(), null);
				ivjJPanel2.add(getstcLblColorMajor(), null);
				ivjJPanel2.setBounds(5, 78, 359, 290);
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
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
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
					new Dimension(2147483647, 2147483647));
				ivjJPanel3.setMinimumSize(new Dimension(221, 225));
				getJPanel3().add(
					getstcLblEmptyWeight(),
					getstcLblEmptyWeight().getName());
				getJPanel3().add(
					gettxtVehicleEmptyWeight(),
					gettxtVehicleEmptyWeight().getName());
				getJPanel3().add(
					getstcLblCarryingCapacity(),
					getstcLblCarryingCapacity().getName());
				getJPanel3().add(
					gettxtVehicleCarryingCapacity(),
					gettxtVehicleCarryingCapacity().getName());
				getJPanel3().add(
					getstcLblGrossWeight(),
					getstcLblGrossWeight().getName());
				getJPanel3().add(
					getlblVehicleGrossWeight(),
					getlblVehicleGrossWeight().getName());
				getJPanel3().add(
					getstcLblTonnage(),
					getstcLblTonnage().getName());
				getJPanel3().add(
					gettxtVehicleTonnage(),
					gettxtVehicleTonnage().getName());
				getJPanel3().add(
					getchkFixedWeight(),
					getchkFixedWeight().getName());
				getJPanel3().add(
					getchkPermitRequired(),
					getchkPermitRequired().getName());
				getJPanel3().add(
					getstcLblTlrLength(),
					getstcLblTlrLength().getName());
				getJPanel3().add(
					gettxtTrlrLength(),
					gettxtTrlrLength().getName());
				getJPanel3().add(
					getstcLblTrailerType(),
					getstcLblTrailerType().getName());
				getJPanel3().add(
					getcomboTrailerType(),
					getcomboTrailerType().getName());
				getJPanel3().add(
					getstcLblReplicaYearMake(),
					getstcLblReplicaYearMake().getName());
				getJPanel3().add(gettxtFeet(), gettxtFeet().getName());
				getJPanel3().add(
					gettxtInches(),
					gettxtInches().getName());
				getJPanel3().add(
					gettxtReplicaVehicleModelYear(),
					gettxtReplicaVehicleModelYear().getName());
				getJPanel3().add(
					gettxtReplicaVehicleMake(),
					gettxtReplicaVehicleMake().getName());
				getJPanel3().add(
					getstcLblTlrWidth(),
					getstcLblTlrWidth().getName());
				getJPanel3().add(
					getstcLblFeet(),
					getstcLblFeet().getName());
				getJPanel3().add(
					getstcLblInches(),
					getstcLblInches().getName());
				// user code begin {1}

				ivjJPanel3.setBounds(363, 80, 260, 290);
				// user code end
				ivjJPanel3.add(getchkTravelTrailer(), null);
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
	 * Return the JPanel4 property value.
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
				ivjJPanel4.setName("JPanel4");
				ivjJPanel4.setLayout(null);
				ivjJPanel4.setMinimumSize(new Dimension(0, 0));
				getJPanel4().add(
					gettxtVehicleModelYear(),
					gettxtVehicleModelYear().getName());
				getJPanel4().add(
					getcomboVehicleMake(),
					getcomboVehicleMake().getName());
				getJPanel4().add(
					gettxtVehMake(),
					gettxtVehMake().getName());
				// user code begin {1}

				ivjJPanel4.setBounds(119, 0, 240, 36);
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
	 * Return the lblClassCodeDesc property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblClassCodeDesc()
	{
		if (ivjlblClassCodeDesc == null)
		{
			try
			{
				ivjlblClassCodeDesc = new JLabel();
				ivjlblClassCodeDesc.setName("lblClassCodeDesc");
				ivjlblClassCodeDesc.setText(CODE_DESCR);
				ivjlblClassCodeDesc.setMaximumSize(
					new Dimension(97, 14));
				ivjlblClassCodeDesc.setMinimumSize(
					new Dimension(97, 14));
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
		return ivjlblClassCodeDesc;
	}

	/**
	 * Return the lblClassCodeDesc1 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblClassCodeDesc1()
	{
		if (ivjlblClassCodeDesc1 == null)
		{
			try
			{
				ivjlblClassCodeDesc1 = new JLabel();
				ivjlblClassCodeDesc1.setName("lblClassCodeDesc1");
				ivjlblClassCodeDesc1.setText(CODE_DESCR);
				ivjlblClassCodeDesc1.setMaximumSize(
					new Dimension(97, 14));
				ivjlblClassCodeDesc1.setMinimumSize(
					new Dimension(97, 14));
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
		return ivjlblClassCodeDesc1;
	}

	/**
	 * Return the lblVehicleClassCode property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVehicleClassCode()
	{
		if (ivjlblVehicleClassCode == null)
		{
			try
			{
				ivjlblVehicleClassCode = new JLabel();
				ivjlblVehicleClassCode.setName("lblVehicleClassCode");
				ivjlblVehicleClassCode.setText(CODE);
				ivjlblVehicleClassCode.setMaximumSize(
					new Dimension(29, 14));
				ivjlblVehicleClassCode.setMinimumSize(
					new Dimension(29, 14));
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
		return ivjlblVehicleClassCode;
	}

	/**
	 * Return the lblVehicleGrossWeight property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVehicleGrossWeight()
	{
		if (ivjlblVehicleGrossWeight == null)
		{
			try
			{
				ivjlblVehicleGrossWeight = new JLabel();
				ivjlblVehicleGrossWeight.setName(
					"lblVehicleGrossWeight");
				ivjlblVehicleGrossWeight.setText(
					CommonConstant.STR_ZERO);
				ivjlblVehicleGrossWeight.setMaximumSize(
					new Dimension(29, 14));
				ivjlblVehicleGrossWeight.setAlignmentX(0.5F);
				ivjlblVehicleGrossWeight.setBounds(128, 58, 104, 14);
				ivjlblVehicleGrossWeight.setMinimumSize(
					new Dimension(29, 14));
				ivjlblVehicleGrossWeight.setHorizontalAlignment(4);
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
		return ivjlblVehicleGrossWeight;
	}

	/**
	 * Return the lblVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVIN()
	{
		if (ivjlblVIN == null)
		{
			try
			{
				ivjlblVIN = new JLabel();
				ivjlblVIN.setBounds(127, 156, 206, 14);
				ivjlblVIN.setName("lblVIN");
				ivjlblVIN.setText(LBLVIN);
				ivjlblVIN.setMaximumSize(new Dimension(98, 14));
				ivjlblVIN.setMinimumSize(new Dimension(98, 14));
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
		return ivjlblVIN;
	}

	/**
	 * Return the Major Color Code selected in Drop-Down list
	 * 
	 * @return String
	 */
	private String getMajorColorCdSelected()
	{
		String lsColorCd = CommonConstant.STR_SPACE_EMPTY;
		int liSelectedIndx = getcomboMajorColor().getSelectedIndex();
		if (liSelectedIndx > -1)
		{
			lsColorCd = (String) cvVehColor.elementAt(liSelectedIndx);
			lsColorCd = lsColorCd.substring(40);
		}

		return lsColorCd;
	}

	/**
	 * Return the Minor Color Code selected in Drop-Down list
	 * 
	 * @return String
	 */
	private String getMinorColorCdSelected()
	{
		String lsColorCd = CommonConstant.STR_SPACE_EMPTY;
		int liSelectedIndx = getcomboMinorColor().getSelectedIndex();
		// Must be greater than zero since first row is -NONE- selection
		if (liSelectedIndx > 0)
		{
			lsColorCd = (String) cvVehColor.elementAt(liSelectedIndx - 1);
			lsColorCd = lsColorCd.substring(40);
		}
		return lsColorCd;
	}

	/**
	 * Return the stcLblBodyStyle property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblBodyStyle()
	{
		if (ivjstcLblBodyStyle == null)
		{
			try
			{
				ivjstcLblBodyStyle = new JLabel();
				ivjstcLblBodyStyle.setBounds(52, 44, 66, 14);
				ivjstcLblBodyStyle.setName("stcLblBodyStyle");
				ivjstcLblBodyStyle.setText(BODY_STYLE);
				ivjstcLblBodyStyle.setMaximumSize(
					new Dimension(62, 14));
				ivjstcLblBodyStyle.setMinimumSize(
					new Dimension(62, 14));
				ivjstcLblBodyStyle.setHorizontalAlignment(4);
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
	 * Return the stcLblBodyVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblBodyVIN()
	{
		if (ivjstcLblBodyVIN == null)
		{
			try
			{
				ivjstcLblBodyVIN = new JLabel();
				ivjstcLblBodyVIN.setBounds(61, 182, 57, 14);
				ivjstcLblBodyVIN.setName("stcLblBodyVIN");
				ivjstcLblBodyVIN.setText(BODY_VIN);
				ivjstcLblBodyVIN.setMaximumSize(new Dimension(53, 14));
				ivjstcLblBodyVIN.setMinimumSize(new Dimension(53, 14));
				ivjstcLblBodyVIN.setHorizontalAlignment(4);
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
		return ivjstcLblBodyVIN;
	}

	/**
	 * Return the stcLblBrand property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblBrand()
	{
		if (ivjstcLblBrand == null)
		{
			try
			{
				ivjstcLblBrand = new JLabel();
				ivjstcLblBrand.setBounds(74, 249, 44, 14);
				ivjstcLblBrand.setName("stcLblBrand");
				ivjstcLblBrand.setText(BRAND);
				ivjstcLblBrand.setMaximumSize(new Dimension(37, 14));
				ivjstcLblBrand.setMinimumSize(new Dimension(37, 14));
				ivjstcLblBrand.setHorizontalAlignment(4);
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
		return ivjstcLblBrand;
	}

	/**
	 * Return the stcLblCarryingCapacity property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCarryingCapacity()
	{
		if (ivjstcLblCarryingCapacity == null)
		{
			try
			{
				ivjstcLblCarryingCapacity = new JLabel();
				ivjstcLblCarryingCapacity.setName(
					"stcLblCarryingCapacity");
				ivjstcLblCarryingCapacity.setText(CARRY_CAPACITY);
				ivjstcLblCarryingCapacity.setMaximumSize(
					new Dimension(103, 14));
				ivjstcLblCarryingCapacity.setMinimumSize(
					new Dimension(103, 14));
				ivjstcLblCarryingCapacity.setHorizontalAlignment(4);
				ivjstcLblCarryingCapacity.setBounds(9, 34, 108, 14);
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
	 * Return the stcLblEmptyWeight property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEmptyWeight()
	{
		if (ivjstcLblEmptyWeight == null)
		{
			try
			{
				ivjstcLblEmptyWeight = new JLabel();
				ivjstcLblEmptyWeight.setName("stcLblEmptyWeight");
				ivjstcLblEmptyWeight.setText(EMPTY_WT);
				ivjstcLblEmptyWeight.setMaximumSize(
					new Dimension(81, 14));
				ivjstcLblEmptyWeight.setMinimumSize(
					new Dimension(81, 14));
				ivjstcLblEmptyWeight.setHorizontalAlignment(4);
				ivjstcLblEmptyWeight.setBounds(30, 10, 87, 14);
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
		return ivjstcLblEmptyWeight;
	}

	/**
	 * Return the stcLblFeet property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblFeet()
	{
		if (ivjstcLblFeet == null)
		{
			try
			{
				ivjstcLblFeet = new JLabel();
				ivjstcLblFeet.setName("stcLblFeet");
				ivjstcLblFeet.setLocation(new Point(155, 206));
				ivjstcLblFeet.setSize(new Dimension(18, 20));
				ivjstcLblFeet.setText(FT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblFeet;
	}

	/**
	 * Return the stcLblGrossWeight property value.
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
				ivjstcLblGrossWeight.setName("stcLblGrossWeight");
				ivjstcLblGrossWeight.setText(GROSS_WT);
				ivjstcLblGrossWeight.setMaximumSize(
					new Dimension(80, 14));
				ivjstcLblGrossWeight.setMinimumSize(
					new Dimension(80, 14));
				ivjstcLblGrossWeight.setHorizontalAlignment(4);
				ivjstcLblGrossWeight.setBounds(32, 58, 85, 14);
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
	 * Return the stcLblInches property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblInches()
	{
		if (ivjstcLblInches == null)
		{
			try
			{
				ivjstcLblInches = new JLabel();
				ivjstcLblInches.setName("stcLblInches");
				ivjstcLblInches.setSize(new Dimension(45, 20));
				ivjstcLblInches.setLocation(new Point(206, 206));
				ivjstcLblInches.setText(INCHES);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblInches;
	}

	/**
	 * Return the stcLblColorMajor property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblColorMajor()
	{
		if (ivjstcLblColorMajor == null)
		{
			try
			{
				ivjstcLblColorMajor = new javax.swing.JLabel();
				ivjstcLblColorMajor.setBounds(41, 101, 77, 14);
				ivjstcLblColorMajor.setName("stcLblColorMajor");
				ivjstcLblColorMajor.setText(MAJORCOLOR);
				ivjstcLblColorMajor.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblColorMajor;
	}

	/**
	 * Return the stcLblColorMinor property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblColorMinor()
	{
		if (ivjstcLblColorMinor == null)
		{
			try
			{
				ivjstcLblColorMinor = new javax.swing.JLabel();
				ivjstcLblColorMinor.setBounds(41, 131, 77, 14);
				ivjstcLblColorMinor.setName("stcLblColorMinor");
				ivjstcLblColorMinor.setText(MINORCOLOR);
				ivjstcLblColorMinor.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblColorMinor;
	}

	/**
	 * Return the stcLblModel property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblModel()
	{
		if (ivjstcLblModel == null)
		{
			try
			{
				ivjstcLblModel = new JLabel();
				ivjstcLblModel.setBounds(73, 71, 45, 14);
				ivjstcLblModel.setName("stcLblModel");
				ivjstcLblModel.setText(MODEL);
				ivjstcLblModel.setMaximumSize(new Dimension(37, 14));
				ivjstcLblModel.setMinimumSize(new Dimension(37, 14));
				ivjstcLblModel.setHorizontalAlignment(4);
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
	 * Return the stcLblNewTitleType property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblNewTitleType()
	{
		if (ivjstcLblNewTitleType == null)
		{
			try
			{
				ivjstcLblNewTitleType = new JLabel();
				ivjstcLblNewTitleType.setName("stcLblNewTitleType");
				ivjstcLblNewTitleType.setText(NEW_TTL_TYPE);
				ivjstcLblNewTitleType.setMaximumSize(
					new Dimension(85, 14));
				ivjstcLblNewTitleType.setMinimumSize(
					new Dimension(85, 14));
				ivjstcLblNewTitleType.setHorizontalAlignment(4);
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
		return ivjstcLblNewTitleType;
	}

	/**
	 * Return the stcLblOdometerReading property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblOdometerReading()
	{
		if (ivjstcLblOdometerReading == null)
		{
			try
			{
				ivjstcLblOdometerReading = new JLabel();
				ivjstcLblOdometerReading.setName(
					"stcLblOdometerReading");
				ivjstcLblOdometerReading.setDisplayedMnemonic(79);
				ivjstcLblOdometerReading.setText(ODOMTR_READNG);
				ivjstcLblOdometerReading.setMaximumSize(
					new Dimension(109, 14));
				ivjstcLblOdometerReading.setMinimumSize(
					new Dimension(109, 14));
				ivjstcLblOdometerReading.setHorizontalAlignment(4);
				// user code begin {1}

				ivjstcLblOdometerReading.setLabelFor(
					gettxtVehicleOdometerReading());

				ivjstcLblOdometerReading.setBounds(6, 210, 112, 21);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblOdometerReading;
	}

	/**
	 * Return the stcLblRegistrationClass property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblRegistrationClass()
	{
		if (ivjstcLblRegistrationClass == null)
		{
			try
			{
				ivjstcLblRegistrationClass = new JLabel();
				ivjstcLblRegistrationClass.setName(
					"stcLblRegistrationClass");
				ivjstcLblRegistrationClass.setText(REGIS_CLASS);
				ivjstcLblRegistrationClass.setMaximumSize(
					new Dimension(107, 14));
				ivjstcLblRegistrationClass.setMinimumSize(
					new Dimension(107, 14));
				ivjstcLblRegistrationClass.setHorizontalAlignment(4);
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
		return ivjstcLblRegistrationClass;
	}

	/**
	 * Return the stcLblReplicaYearMake property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblReplicaYearMake()
	{
		if (ivjstcLblReplicaYearMake == null)
		{
			try
			{
				ivjstcLblReplicaYearMake = new JLabel();
				ivjstcLblReplicaYearMake.setName(
					"stcLblReplicaYearMake");
				ivjstcLblReplicaYearMake.setLocation(new Point(6, 259));
				ivjstcLblReplicaYearMake.setSize(new Dimension(110, 20));
				ivjstcLblReplicaYearMake.setText(REPLICA_YR_MK);
				ivjstcLblReplicaYearMake.setMaximumSize(
					new Dimension(108, 14));
				ivjstcLblReplicaYearMake.setMinimumSize(
					new Dimension(108, 14));
				ivjstcLblReplicaYearMake.setHorizontalAlignment(4);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblReplicaYearMake;
	}

	/**
	 * Return the stcLblTlrWidth property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTlrWidth()
	{
		if (ivjstcLblTlrWidth == null)
		{
			try
			{
				ivjstcLblTlrWidth = new JLabel();
				ivjstcLblTlrWidth.setName("stcLblTlrWidth");
				ivjstcLblTlrWidth.setLocation(new Point(17, 206));
				ivjstcLblTlrWidth.setSize(new Dimension(99, 20));
				ivjstcLblTlrWidth.setText(TRVL_TRLR_WIDTH);
				ivjstcLblTlrWidth.setHorizontalAlignment(
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
		return ivjstcLblTlrWidth;
	}

	/**
	 * Return the stcLblTonnage property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTonnage()
	{
		if (ivjstcLblTonnage == null)
		{
			try
			{
				ivjstcLblTonnage = new JLabel();
				ivjstcLblTonnage.setName("stcLblTonnage");
				ivjstcLblTonnage.setText(TONNAGE);
				ivjstcLblTonnage.setMaximumSize(new Dimension(52, 14));
				ivjstcLblTonnage.setMinimumSize(new Dimension(52, 14));
				ivjstcLblTonnage.setHorizontalAlignment(4);
				ivjstcLblTonnage.setBounds(61, 82, 56, 14);
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
	 * Return the stcLblTrailerType property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTrailerType()
	{
		if (ivjstcLblTrailerType == null)
		{
			try
			{
				ivjstcLblTrailerType = new JLabel();
				ivjstcLblTrailerType.setName("stcLblTrailerType");
				ivjstcLblTrailerType.setLocation(new Point(42, 232));
				ivjstcLblTrailerType.setSize(new Dimension(74, 20));
				ivjstcLblTrailerType.setText(TRLR_TYPE);
				ivjstcLblTrailerType.setMaximumSize(
					new Dimension(70, 14));
				ivjstcLblTrailerType.setMinimumSize(
					new Dimension(70, 14));
				ivjstcLblTrailerType.setHorizontalAlignment(4);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTrailerType;
	}

	/**
	 * Return the stcLblTravelTlrLength property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblTlrLength()
	{
		if (ivjstcLblTravelTlrLength == null)
		{
			try
			{
				ivjstcLblTravelTlrLength = new JLabel();
				ivjstcLblTravelTlrLength.setName(
					"stcLblTravelTlrLength");
				ivjstcLblTravelTlrLength.setLocation(new Point(14, 180));
				ivjstcLblTravelTlrLength.setSize(new Dimension(102, 20));
				ivjstcLblTravelTlrLength.setText(TRVL_TRLR_LENGTH);
				ivjstcLblTravelTlrLength.setMaximumSize(
					new Dimension(98, 14));
				ivjstcLblTravelTlrLength.setMinimumSize(
					new Dimension(98, 14));
				ivjstcLblTravelTlrLength.setHorizontalAlignment(4);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTravelTlrLength;
	}

	/**
	 * Return the stcLblVehicleClass property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVehicleClass()
	{
		if (ivjstcLblVehicleClass == null)
		{
			try
			{
				ivjstcLblVehicleClass = new JLabel();
				ivjstcLblVehicleClass.setName("stcLblVehicleClass");
				ivjstcLblVehicleClass.setText(VEH_CLASS);
				ivjstcLblVehicleClass.setMaximumSize(
					new Dimension(80, 14));
				ivjstcLblVehicleClass.setMinimumSize(
					new Dimension(80, 14));
				ivjstcLblVehicleClass.setHorizontalAlignment(4);
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
		return ivjstcLblVehicleClass;
	}

	/**
	 * Return the stcLblVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblVIN()
	{
		if (ivjstcLblVIN == null)
		{
			try
			{
				ivjstcLblVIN = new JLabel();
				ivjstcLblVIN.setBounds(89, 156, 29, 14);
				ivjstcLblVIN.setName("stcLblVIN");
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(new Dimension(22, 14));
				ivjstcLblVIN.setMinimumSize(new Dimension(22, 14));
				ivjstcLblVIN.setHorizontalAlignment(4);
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
		return ivjstcLblVIN;
	}

	/**
	 * Return the stcLblYearMake property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblYearMake()
	{
		if (ivjstcLblYearMake == null)
		{
			try
			{
				ivjstcLblYearMake = new JLabel();
				ivjstcLblYearMake.setBounds(47, 13, 71, 15);
				ivjstcLblYearMake.setName("stcLblYearMake");
				ivjstcLblYearMake.setAlignmentY(
					Component.TOP_ALIGNMENT);
				ivjstcLblYearMake.setText(YEAR_MAKE);
				ivjstcLblYearMake.setMaximumSize(new Dimension(63, 14));
				ivjstcLblYearMake.setMinimumSize(new Dimension(63, 14));
				ivjstcLblYearMake.setHorizontalAlignment(4);
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
	 * Return the txtFeet property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtFeet()
	{
		if (ivjtxtFeet == null)
		{
			try
			{
				ivjtxtFeet = new RTSInputField();
				ivjtxtFeet.setName("txtFeet");
				ivjtxtFeet.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtFeet.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjtxtFeet.setSize(new Dimension(25, 20));
				ivjtxtFeet.setLocation(new Point(127, 206));
				ivjtxtFeet.setMaxLength(FEET_MAX_LEN);
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
		return ivjtxtFeet;
	}

	/**
	 * Return the txtInches property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtInches()
	{
		if (ivjtxtInches == null)
		{
			try
			{
				ivjtxtInches = new RTSInputField();
				ivjtxtInches.setName("txtInches");
				ivjtxtInches.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtInches.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjtxtInches.setBounds(177, 206, 22, 20);
				ivjtxtInches.setMaxLength(INCHES_MAX_LEN);
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
		return ivjtxtInches;
	}

	/**
	 * Return the ivjtxtVehMake property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehMake()
	{
		if (ivjtxtVehMake == null)
		{
			try
			{
				ivjtxtVehMake = new RTSInputField();
				ivjtxtVehMake.setName("ivjtxtVehMake");
				ivjtxtVehMake.setManagingFocus(false);
				ivjtxtVehMake.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehMake.setVisible(false);
				//Defect 5187
				//Change input to allow all characters to be entered.
				ivjtxtVehMake.setInput(RTSInputField.DEFAULT);
				//end defect 5187
				ivjtxtVehMake.setBounds(51, 8, 79, 20);
				ivjtxtVehMake.setMaxLength(VEH_MK_MAX_LEN);
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
		return ivjtxtVehMake;
	}

	/**
	 * Return the txtReplicaVehicleMake property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtReplicaVehicleMake()
	{
		if (ivjtxtReplicaVehicleMake == null)
		{
			try
			{
				ivjtxtReplicaVehicleMake = new RTSInputField();
				ivjtxtReplicaVehicleMake.setName(
					"txtReplicaVehicleMake");
				ivjtxtReplicaVehicleMake.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtReplicaVehicleMake.setManagingFocus(false);
				ivjtxtReplicaVehicleMake.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtReplicaVehicleMake.setBounds(181, 259, 57, 20);
				ivjtxtReplicaVehicleMake.setMaxLength(VEH_MK_MAX_LEN);
				// user code begin {1}
				// defect 7898
				// Removing this will allow enter to be pressed when on 
				// this field
				//ivjtxtReplicaVehicleMake.addActionListener(this);
				// end defect 7898
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtReplicaVehicleMake;
	}

	/**
	 * Return the txtReplicaVehicleModelYear property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtReplicaVehicleModelYear()
	{
		if (ivjtxtReplicaVehicleModelYear == null)
		{
			try
			{
				ivjtxtReplicaVehicleModelYear = new RTSInputField();
				ivjtxtReplicaVehicleModelYear.setName(
					"txtReplicaVehicleModelYear");
				ivjtxtReplicaVehicleModelYear.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtReplicaVehicleModelYear.setManagingFocus(false);
				ivjtxtReplicaVehicleModelYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtReplicaVehicleModelYear.setBounds(127, 259, 47, 20);
				ivjtxtReplicaVehicleModelYear.setMaxLength(
					VEH_MK_MAX_LEN);
				// user code begin {1}

				// defect 7898
				// Removing this will allow enter to be pressed when on 
				// this field
				//ivjtxtReplicaVehicleModelYear.addActionListener(this);
				// end defect 7898

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtReplicaVehicleModelYear;
	}

	/**
	 * Return the txtVehicleCarryingCapacity property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleCarryingCapacity()
	{
		if (ivjtxtVehicleCarryingCapacity == null)
		{
			try
			{
				ivjtxtVehicleCarryingCapacity = new RTSInputField();
				ivjtxtVehicleCarryingCapacity.setName(
					"txtVehicleCarryingCapacity");
				ivjtxtVehicleCarryingCapacity.setManagingFocus(false);
				ivjtxtVehicleCarryingCapacity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleCarryingCapacity.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleCarryingCapacity.setBounds(
					128,
					31,
					104,
					20);
				ivjtxtVehicleCarryingCapacity.setHorizontalAlignment(4);
				ivjtxtVehicleCarryingCapacity.setMaxLength(
					CARRY_CAP_MAX_LEN);
				// user code begin {1}
				ivjtxtVehicleCarryingCapacity.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleCarryingCapacity;
	}

	/**
	 * Return the txtVehicleEmptyWeight property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleEmptyWeight()
	{
		if (ivjtxtVehicleEmptyWeight == null)
		{
			try
			{
				ivjtxtVehicleEmptyWeight = new RTSInputField();
				ivjtxtVehicleEmptyWeight.setName(
					"txtVehicleEmptyWeight");
				ivjtxtVehicleEmptyWeight.setManagingFocus(false);
				ivjtxtVehicleEmptyWeight.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleEmptyWeight.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleEmptyWeight.setBounds(128, 7, 104, 20);
				ivjtxtVehicleEmptyWeight.setHorizontalAlignment(4);
				ivjtxtVehicleEmptyWeight.setMaxLength(EMPTY_WT_MAX_LEN);
				// user code begin {1}
				ivjtxtVehicleEmptyWeight.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleEmptyWeight;
	}

	/**
	 * Return the txtVehicleLength property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTrlrLength()
	{
		if (ivjtxtVehicleLength == null)
		{
			try
			{
				ivjtxtVehicleLength = new RTSInputField();
				ivjtxtVehicleLength.setName("txtVehicleLength");
				ivjtxtVehicleLength.setManagingFocus(false);
				ivjtxtVehicleLength.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleLength.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleLength.setBounds(127, 180, 66, 20);
				ivjtxtVehicleLength.setHorizontalAlignment(4);
				ivjtxtVehicleLength.setMaxLength(VEH_LNGTH_MAX_LEN);
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
		return ivjtxtVehicleLength;
	}

	/**
	 * Return the txtVehicleModel property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleModel()
	{
		if (ivjtxtVehicleModel == null)
		{
			try
			{
				ivjtxtVehicleModel = new RTSInputField();
				ivjtxtVehicleModel.setBounds(127, 69, 37, 19);
				ivjtxtVehicleModel.setName("txtVehicleModel");
				ivjtxtVehicleModel.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVehicleModel.setManagingFocus(false);
				ivjtxtVehicleModel.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleModel.setMaxLength(VEH_MODL_MAX_LEN);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}

				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtxtVehicleModel;
	}

	/**
	 * Return the txtVehicleModelYear property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehicleModelYear()
	{
		if (ivjtxtVehicleModelYear == null)
		{
			try
			{
				ivjtxtVehicleModelYear = new RTSInputField();
				ivjtxtVehicleModelYear.setName("txtVehicleModelYear");
				ivjtxtVehicleModelYear.setManagingFocus(false);
				ivjtxtVehicleModelYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleModelYear.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjtxtVehicleModelYear.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleModelYear.setBounds(7, 8, 37, 20);
				ivjtxtVehicleModelYear.setMaxLength(MODL_YR_MAX_LEN);
				// user code begin {1}
				ivjtxtVehicleModelYear.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtVehicleModelYear;
	}

	/**
	 * Return the txtVehicleOdometerReading property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleOdometerReading()
	{
		if (ivjtxtVehicleOdometerReading == null)
		{
			try
			{
				ivjtxtVehicleOdometerReading = new RTSInputField();
				ivjtxtVehicleOdometerReading.setBounds(
					127,
					210,
					74,
					21);
				ivjtxtVehicleOdometerReading.setName(
					"txtVehicleOdometerReading");
				ivjtxtVehicleOdometerReading.setManagingFocus(true);
				ivjtxtVehicleOdometerReading.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleOdometerReading.setInput(
					RTSInputField.DEFAULT);
				ivjtxtVehicleOdometerReading.setHorizontalAlignment(2);
				ivjtxtVehicleOdometerReading.setMaxLength(
					ODOMTR_RNDG_MAX_LEN);
				// user code begin {1}
				// defect 7898
				// Removing this will allow enter to be pressed when on 
				// this field
				//ivjtxtVehicleOdometerReading.addActionListener(this);
				// Added so that the TAB and SHIFT+TAB will go through
				// KeyPressed.
				ivjtxtVehicleOdometerReading
					.setFocusTraversalKeysEnabled(
					false);
				// end defect 7898
				ivjtxtVehicleOdometerReading.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleOdometerReading;
	}

	/**
	 * Return the txtVehicleTonnage property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleTonnage()
	{
		if (ivjtxtVehicleTonnage == null)
		{
			try
			{
				ivjtxtVehicleTonnage = new RTSInputField();
				ivjtxtVehicleTonnage.setName("txtVehicleTonnage");
				ivjtxtVehicleTonnage.setManagingFocus(false);
				ivjtxtVehicleTonnage.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleTonnage.setInput(
					RTSInputField.DOLLAR_ONLY);
				ivjtxtVehicleTonnage.setBounds(128, 79, 66, 20);
				ivjtxtVehicleTonnage.setHorizontalAlignment(4);
				ivjtxtVehicleTonnage.setMaxLength(TONNAGE_MAX_LEN);
				// user code begin {1}
				ivjtxtVehicleTonnage.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleTonnage;
	}

	/**
	 * Return the txtVehicleVIN property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleBodyVIN()
	{
		if (ivjtxtVehicleVIN == null)
		{
			try
			{
				ivjtxtVehicleVIN = new RTSInputField();
				ivjtxtVehicleVIN.setBounds(127, 179, 206, 20);
				ivjtxtVehicleVIN.setName("txtVehicleVIN");
				ivjtxtVehicleVIN.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVehicleVIN.setManagingFocus(false);
				ivjtxtVehicleVIN.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleVIN.setMaxLength(VIN_MAX_LEN);
				// user code begin {1}
				// defect 7898
				// Removing this will allow enter to be pressed when on 
				// this field
				//ivjtxtVehicleVIN.addActionListener(this);
				// end defect 7898

				// defect 8523
				ivjtxtVehicleVIN.addFocusListener(this);
				// end defect 8523

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleVIN;
	}
	
	/**
	 * 
	 */
	private void handleTravelTrailerCheckbox()
	{
		String lsVehClassCd = caVehInqData.getMfVehicleData()
		.getVehicleData().getVehClassCd();
		int liRegClassCd = caVehInqData.getMfVehicleData().getRegData()
		.getRegClassCd();
		if (!UtilityMethods.isEmpty(lsVehClassCd))
		{
			if ((lsVehClassCd.trim().equals("TRLR") && liRegClassCd == 
				RegistrationConstant.REGCLASSCD_TITLE_ONLY))
			{
				getchkTravelTrailer().setEnabled(true);
			}
			else
			{
				if (liRegClassCd == RegistrationConstant.REGCLASSCD_TRAVEL_TRAILER)
				{
					getchkTravelTrailer().setSelected(true);
				}
			}
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
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// focus manager necessary to make form follow tab tag order
			// defect 7898
			//	Remove FocusManager 
			//FocusManager.setCurrentManager(
			//	new ContainerOrderFocusManager());
			// end defect 7898

			// user code end
			setName(ScreenConstant.TTL004_FRAME_NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(638, 464);
			setTitle(ScreenConstant.TTL004_FRAME_TITLE);
			setContentPane(getFrmNoTitleRecordTTL004ContentPane1());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		gettxtVehMake().setVisible(false);
		// user code end
	}

	/**
	 * Checks to see if Major Color is required to be selected
	 * based on the System AbstractProperty VehColorStartDate.  Returns true
	 * if required and not selected.
	 * 
	 * @return boolean
	 */
	private boolean isMajorColorReqdButNotSelected()
	{
		boolean lbRet = false;

		String lsMajorColor = getMajorColorCdSelected();
		if (RTSDate.getCurrentDate().getYYYYMMDDDate()
			>= SystemProperty.getVehColorStartDate().getYYYYMMDDDate()
			&& (lsMajorColor.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			lbRet = true;
		}

		return lbRet;
	}

	/**
	 * Checks to see if Major Color selected is same as Minor Color.
	 * 
	 * @return boolean
	 */
	private boolean isMajorColorSameAsMinorColor()
	{
		boolean lbRet = false;

		String lsMajorColor = getMajorColorCdSelected();
		String lsMinorColor = getMinorColorCdSelected();
		if (!(lsMajorColor.equals(CommonConstant.STR_SPACE_EMPTY))
				&& (lsMajorColor.equalsIgnoreCase(lsMinorColor)))
		{
			lbRet = true;
		}

		return lbRet;
	}

	/**
	 * Checks to see if Minor Color is selected but Major Color is 
	 * not selected.
	 * 
	 * @return boolean
	 */
	private boolean isMinorColorSelectedMajorColorNotSelected()
	{
		boolean lbRet = false;

		String lsMajorColor = getMajorColorCdSelected();
		String lsMinorColor = getMinorColorCdSelected();
		if (lsMajorColor.equals(CommonConstant.STR_SPACE_EMPTY) 
			&& !(lsMinorColor.equals(CommonConstant.STR_SPACE_EMPTY)))
		{
			lbRet = true;
		}

		return lbRet;
	}

	/**
	 * Validates the year model entered.
	 * 
	 * @param asYr String
	 * @return boolean
	 */
	private boolean isValidYear(String asYr)
	{
		return CommonValidations.isValidYearModel(asYr);
	}

	/**
	 * Validates the empty weight against the VINA empty weight.
	 * 
	 * @return boolean
	 */
	private boolean isVINAEmptyWt()
	{
		boolean lbRet = false;
		int liEmptyWt =
			caVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.getVINAVehEmptyWt();
		String lsEmptWt = gettxtVehicleEmptyWeight().getText().trim();
		if (lsEmptWt.length() > ZERO)
		{
			int liEmptyWtInput = Integer.parseInt(lsEmptWt);
			if (liEmptyWt > ZERO && liEmptyWt > liEmptyWtInput)
			{
				lbRet = true;
			}
		}
		return lbRet;
	}

	/**
	 * Validates the vehicle tonnage with the VINA begin and end tonnage.
	 * 
	 * @return boolean
	 */
	private boolean isVINAInBegTonAndEndTon()
	{
		boolean lbRet = false;
		double ldVINABegTon =
			caVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.getVinaBegTon()
				/ 100.0;
		double ldVINAEndTon =
			caVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.getVinaEndTon()
				/ 100.0;
		if (ldVINABegTon > 0 && ldVINAEndTon > 0)
		{
			String lsVehTon = gettxtVehicleTonnage().getText().trim();
			if (lsVehTon.length() > ZERO)
			{
				try
				{
					double ldTon = Double.parseDouble(lsVehTon);
					if (ldTon < ldVINABegTon || ldTon > ldVINAEndTon)
					{
						lbRet = true;
					}
				}
				catch (NumberFormatException aeNFE)
				{
					lbRet = true;
				}
			}
		}
		return lbRet;
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
		
		// defect 11049 
		if (aaIE.getSource() == getchkTravelTrailer())
		{
			if (getchkTravelTrailer().isSelected()) 
			{
				if (caRegClassData.getRegClassCd() == RegistrationConstant.REGCLASSCD_TITLE_ONLY)
				{
					caRegClassData = RegistrationClassCache.getRegisClass("TRLR",
							RegistrationConstant.REGCLASSCD_TRAVEL_TRAILER,
							new RTSDate().getYYYYMMDDDate());
					disableFields(); 
				}
			}
			else
			{
				caRegClassData = RegistrationClassCache.getRegisClass("TRLR",
						RegistrationConstant.REGCLASSCD_TITLE_ONLY,
						new RTSDate().getYYYYMMDDDate());
				disableFields(); 
			}
		}
		else if (aaIE.getSource() == getcomboVehicleMake())
		{
			// end defect 11049 
			
			String lsSelected =
				(String) getcomboVehicleMake().getSelectedItem();
			// defect 7898
			//	Added check for null
			if (lsSelected != null && lsSelected.equals(NEW))
			{
				getcomboVehicleMake().setVisible(false);
				gettxtVehMake().setVisible(true);
				gettxtVehMake().setEnabled(true);
				cbChkKeyOnCombo = true;
				gettxtVehMake().requestFocus();
			}
			// end defect 7898
		}
		else if (
			aaIE.getSource() == getchkFixedWeight()
				&& caRegClassData.getCaryngCapReqd() == 1)
		{
			if (getchkFixedWeight().isSelected())
			{
				gettxtVehicleCarryingCapacity().setText(
					CommonConstant.STR_SPACE_EMPTY);
				getstcLblCarryingCapacity().setEnabled(false);
				gettxtVehicleCarryingCapacity().setEnabled(false);
				// defect 11126
				String lsGrossWt = new String(); 
				int liGrossWt = 
						CommonValidations.calcGrossWeight(
							gettxtVehicleEmptyWeight().getText(),
							gettxtVehicleCarryingCapacity()
								.getText());
				if (liGrossWt!= 0)
				{
					lsGrossWt = ""+liGrossWt; 
				}
				getlblVehicleGrossWeight().setText(lsGrossWt);
				// end defect 11126 
			}
			else
			{
				getstcLblCarryingCapacity().setEnabled(true);
				gettxtVehicleCarryingCapacity().setEnabled(true);
			}
		}
	}

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(java.awt.event.KeyEvent aaKE)
	{
		super.keyPressed(aaKE);

		if (aaKE.getSource() == gettxtVehicleOdometerReading()
			&& aaKE.getKeyCode() == KeyEvent.VK_TAB)
		{
			cbTabKeyPressed = true;
			// defect 10597 
			resetOdometerBrand();
			// setVehOdomtrBrand();
			// end defect 10597 

			if (aaKE.isShiftDown())
			{
				// we are going backwards
				if (gettxtVehicleBodyVIN().isEnabled())
				{
					gettxtVehicleBodyVIN().requestFocus();
				}
				else
				{
					gettxtVehicleModel().requestFocus();
				}
			}
			else
			{
				// We are going forward
				// defect 8926
				if (getbtnSPV().isEnabled())
				{
					getbtnSPV().requestFocus();
				}
				// end defect 8926
				else if (getcomboVehicleOdometerBrand().isEnabled())
				{
					getcomboVehicleOdometerBrand().requestFocus();
				}
				else
				{
					gettxtVehicleEmptyWeight().requestFocus();
				}
			}
		}
		// defect 10597 
		else if (
			aaKE.getSource() instanceof RTSInputField
				&& aaKE.getSource() == gettxtVehMake()
				&& gettxtVehMake().isEmpty()
				&& aaKE.getKeyChar() == CommonConstant.CHAR_DASH)
		{
			gettxtVehMake().setVisible(false);
			gettxtVehMake().setText(new String());
			gettxtVehMake().setEnabled(false);
			getcomboVehicleMake().setEnabled(true);
			getcomboVehicleMake().setSelectedIndex(-1);
			getcomboVehicleMake().setVisible(true);
			getcomboVehicleMake().requestFocus(true);
		}
		// end defect 10597 
	}

	/**
	 * Invoked when a key has been released.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		super.keyReleased(aaKE);
		//had to add this code to avoid '-' char being typed in text 
		//	field.
		if (cbChkKeyOnCombo)
		{
			gettxtVehMake().setText(EMPTY);
			cbChkKeyOnCombo = false;
		}
	}

	

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmNoTitleRecordTTL004 laFrmNoTitleRecordTTL004;
			laFrmNoTitleRecordTTL004 = new FrmNoTitleRecordTTL004();
			laFrmNoTitleRecordTTL004.setModal(true);
			laFrmNoTitleRecordTTL004
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmNoTitleRecordTTL004.show();
			Insets laInsets = laFrmNoTitleRecordTTL004.getInsets();
			laFrmNoTitleRecordTTL004.setSize(
				laFrmNoTitleRecordTTL004.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmNoTitleRecordTTL004.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmNoTitleRecordTTL004.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * Populates the body style combo box.
	 */
	private void populateBodyTypes()
	{
		// Verify MF/VINA VehBdyTypes	
		if (getcomboBodyType().isEnabled())
		{
			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();
			String lsBdyType = laVehData.getVehBdyType();
			int liBdyDefault = -1;
			Vector lvVehBdyType =
				VehicleBodyTypesCache.getVehBdyTypesVec();
			UtilityMethods.sort(lvVehBdyType);
			Vector lvComboValues = new Vector();
			if (lvVehBdyType != null)
			{
				for (int liIndex = ZERO;
					liIndex < lvVehBdyType.size();
					liIndex++)
				{
					VehicleBodyTypesData laVehBdyType =
						(VehicleBodyTypesData) lvVehBdyType.get(
							liIndex);
					if (lsBdyType != null
						&& laVehBdyType.getVehBdyType().equals(lsBdyType)
						&& liBdyDefault == -1)
					{
						liBdyDefault = liIndex;
					}
					String lsDesc =
						laVehBdyType.getVehBdyType()
							+ SPACE_DASH
							+ laVehBdyType.getVehBdyTypeDesc();
					lvComboValues.add(lsDesc);
				}
			}
			// If no match found from MF, search Dealer Title
			if (liBdyDefault == -1)
			{
				liBdyDefault = buildDlrVehBdyType(lvVehBdyType);
			}
			DefaultComboBoxModel laDCBM =
				new DefaultComboBoxModel(lvComboValues);
			getcomboBodyType().setModel(laDCBM);
			getcomboBodyType().setSelectedIndex(liBdyDefault);
			// defect 8479
			comboBoxHotKeyFix(getcomboBodyType());
			// end defect 8479
		}
	}

	/**
	 * Populates the odometer brand combo box.
	 */
	private void populateBrand()
	{
		if (getcomboVehicleOdometerBrand().isEnabled())
		{
			Vector lvBrand =
				TitleClientUtilityMethods.getOdometerBrands();
			Vector lvComboVal = new Vector();
			for (int liIndex = ZERO;
				liIndex < lvBrand.size();
				liIndex++)
			{
				String lsNext = (String) lvBrand.get(liIndex);
				lvComboVal.add(lsNext);
			}
			DefaultComboBoxModel laDCBM =
				new DefaultComboBoxModel(lvComboVal);
			getcomboVehicleOdometerBrand().setModel(laDCBM);
			// defect 8479
			comboBoxHotKeyFix(getcomboVehicleOdometerBrand());
			// end defect 8479
		}
	}

	/**
	 * Populates the trailer type combo box.
	 */
	private void populateTrailerType()
	{
		if (getcomboTrailerType().isEnabled())
		{
			Vector lvTrlTyp =
				(Vector) TitleClientUtilityMethods.getTrailerTypes();
			if (lvTrlTyp != null)
			{
				for (int liIndex = ZERO;
					liIndex < lvTrlTyp.size();
					liIndex++)
				{
					getcomboTrailerType().addItem(
						lvTrlTyp.get(liIndex));
				}
			}
			// defect 8479
			comboBoxHotKeyFix(getcomboTrailerType());
			// end defect 8479
		}
	}

	/**
	 * Fills the Select Fee combo box with the VehicleColorData.
	 */
	private void populateMajorColor()
	{
		if (getcomboMajorColor().isEnabled())
		{
			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();
			String lsVehMajorColorCd = laVehData.getVehMjrColorCd();
			int liColorDefault = -1;
			Vector lvComboVal = new Vector();
			if (cvVehColor != null)
			{
				for (int liIndex = ZERO;
					liIndex < cvVehColor.size();
					liIndex++)
				{
					String lsVehColorCd = cvVehColor
									.get(liIndex)
										.toString()
											.substring(40);
					if (lsVehMajorColorCd != null
							&& lsVehColorCd.equals(lsVehMajorColorCd)
								&& liColorDefault == -1)
					{
						liColorDefault = liIndex;
					}
					String lsDesc =
						((String) cvVehColor.get(liIndex))
												.substring(0, 30);
					lvComboVal.add(lsDesc);
				}
			}
			DefaultComboBoxModel laDCBM =
				new DefaultComboBoxModel(lvComboVal);
			getcomboMajorColor().setModel(laDCBM);
			getcomboMajorColor().setSelectedIndex(liColorDefault);
			// defect 8479
			comboBoxHotKeyFix(getcomboMajorColor());
			// end defect 8479
		}
	}

	/**
	 * Fills the Minor Color combo box with the VehicleColorData.
	 */
	private void populateMinorColor()
	{
		if (getcomboMinorColor().isEnabled())
		{
			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();
			String lsVehMinorColorCd = laVehData.getVehMnrColorCd();
			int liColorDefault = -1;
			Vector lvComboVal = new Vector();
			if (cvVehColor != null)
			{
				for (int liIndex = ZERO;
					liIndex < cvVehColor.size();
					liIndex++)
				{
					String lsVehColorCd = cvVehColor
									.get(liIndex)
										.toString()
											.substring(40);
					if (lsVehMinorColorCd != null
							&& lsVehColorCd.equals(lsVehMinorColorCd)
								&& liColorDefault == -1)
					{
						// Add 1 due to row with "- NONE -" being added
						liColorDefault = liIndex + 1;
					}
					String lsDesc =
						((String) cvVehColor.get(liIndex))
												.substring(0, 30);
					lvComboVal.add(lsDesc);
				}
				String lsDesc = "- NONE -";
				lvComboVal.insertElementAt(lsDesc, 0);
			}
			DefaultComboBoxModel laDCBM =
				new DefaultComboBoxModel(lvComboVal);
			getcomboMinorColor().setModel(laDCBM);
			getcomboMinorColor().setSelectedIndex(liColorDefault);
			// defect 8479
			comboBoxHotKeyFix(getcomboMinorColor());
			// end defect 8479
		}
	}

	/**
	 * Fills the Vehicle Color vector with VehicleColorData.
	 */
	private void populateVehColorVector()
	{
		cvVehColor = VehicleColorCache.getVehColorVec();
		UtilityMethods.sort(cvVehColor);
		if (cvVehColor != null)
		{
			for (int liIndex = ZERO;
				liIndex < cvVehColor.size();
				liIndex++)
			{
				VehicleColorData laVehColor =
					(VehicleColorData) cvVehColor.get(liIndex);
				String lsDesc =
					UtilityMethods.addPaddingRight(
						laVehColor.getVehColorDesc(),
						40,
						CommonConstant.STR_SPACE_ONE)
					+ laVehColor.getVehColorCd();
				cvVehColor.setElementAt(lsDesc, liIndex);
			}
		}
	}

	/**
	 * Populates the vehicle make combo box.
	 */
	private void populateVehicleMakes()
	{
		if (getcomboVehicleMake().isEnabled())
		{
			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();
			String lsVehMk = laVehData.getVehMk();
			int liMkDefault = -1;
			Vector lvVehMkCache = VehicleMakesCache.getVehMks();
			UtilityMethods.sort(lvVehMkCache);
			Vector lvComboVal = new Vector();
			if (lvVehMkCache != null)
			{
				for (int liIndex = ZERO;
					liIndex < lvVehMkCache.size();
					liIndex++)
				{
					VehicleMakesData laVehMkdata =
						(VehicleMakesData) lvVehMkCache.get(liIndex);
					if (lsVehMk != null
						&& laVehMkdata.getVehMk().equals(lsVehMk)
						&& liMkDefault == -1)
					{
						liMkDefault = liIndex;
					}
					String lsDesc =
						laVehMkdata.getVehMk()
							+ SPACE_DASH
							+ laVehMkdata.getVehMkDesc();
					lvComboVal.add(lsDesc);
				}
				lvComboVal.add(NEW);
			}
			DefaultComboBoxModel laDCBM =
				new DefaultComboBoxModel(lvComboVal);
			getcomboVehicleMake().setModel(laDCBM);
			getcomboVehicleMake().setSelectedIndex(liMkDefault);
			// defect 8479
			comboBoxHotKeyFix(getcomboVehicleMake());
			// end defect 8479
		}
	}
	
	/** 
	 * 
	 */
	private int recalcGrossWt()
	{
		String lsEmptyWt =
			gettxtVehicleEmptyWeight().getText();
		String lsCarryingCap =
			gettxtVehicleCarryingCapacity().getText();
		int liGrossWt =
			CommonValidations.calcGrossWeight(
				lsEmptyWt,
				lsCarryingCap);
		String lsGrossWt = liGrossWt == 0 ? new String() : ""+liGrossWt; 
		getlblVehicleGrossWeight().setText(lsGrossWt);
		return liGrossWt; 
	}

	/** 
	 * ResetOdometerBrand where necessary
	 * 
	 */
	private void resetOdometerBrand()
	{
		String lsOdoRndng =
			gettxtVehicleOdometerReading().getText().trim();
		// defect 11126 
		//		String lsEmptyWt = gettxtVehicleEmptyWeight().getText().trim();
		//		String lsCarrCap =
		//			gettxtVehicleCarryingCapacity().getText().trim();
		//		int liGrossWt =
		//			CommonValidations.calcGrossWeight(lsEmptyWt, lsCarrCap);
		
		//		getlblVehicleGrossWeight().setText(Integer.toString(liGrossWt));
		int liGrossWt = recalcGrossWt();
		// end defect 11126
		
		String lsGrossWt = getlblVehicleGrossWeight().getText().trim();
		String lsVehTon = gettxtVehicleTonnage().getText().trim();
		String lsYrMdl = gettxtVehicleModelYear().getText().trim();

		if (isValidYear(lsYrMdl)
			|| lsGrossWt.length() != 0
			|| lsVehTon.length() != 0)
		{
			if (!(!isValidYear(lsYrMdl) && liGrossWt == 0))
			{
				if (gettxtVehicleOdometerReading().isEnabled()
					&& TitleClientUtilityMethods
						.isInvalidOdometerReadingBasedOnTIMA(
						lsYrMdl,
						lsVehTon,
						lsGrossWt,
						lsOdoRndng))
				{
					clearAllColor(gettxtVehicleOdometerReading());

					if (!lsOdoRndng.equals(EXEMPT))
					{
						gettxtVehicleOdometerReading().setText(EXEMPT);
						setVehOdomtrBrand();
					}
					else
					{
						gettxtVehicleOdometerReading().setText("");
						setVehOdomtrBrand();
					}
				}
			}
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information to the 
	 * view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			if (aaDataObject != null)
			{
				// defect 10507 
				if (!cbAlreadySet)
				{
					cbAlreadySet = true;
					// end defect 10507 

					caVehInqData = (VehicleInquiryData) aaDataObject;
					
					// defect 1049 
					caRegData = caVehInqData.getMfVehicleData().getRegData();
					caVehData = caVehInqData.getMfVehicleData().getVehicleData();

					displayMsgIfOutOfScope();

					TitleData laTtlData =
						(TitleData) caVehInqData
							.getMfVehicleData()
							.getTitleData();
					if (caRegData != null)
					{
						// The following block of code is for DTA 
						//	transactions
						if (caRegData.getVehCaryngCap() != ZERO)
							gettxtVehicleCarryingCapacity().setText(
								String.valueOf(
									caRegData.getVehCaryngCap()));
					}
					if (caVehData != null)
					{
						String lsVehCd =
							caVehData.getVehClassCd();
						
						int liRegCd =
							caRegData.getRegClassCd();
						
						int liEffDt =
							RTSDate.getCurrentDate().getYYYYMMDDDate();
						caRegClassData =
							RegistrationClassCache.getRegisClass(
								lsVehCd,
								liRegCd,
								liEffDt);
						String lsVin = caVehData.getVin();
						String lsVehModlYr =
							Integer.toString(caVehData.getVehModlYr());
						//				String strMk = vehData.getVehMk();
						String lsModl = caVehData.getVehModl();
						// Default data if brought back from VINA
						// default of make and body style done in populate
						//	methods
						if (lsVehModlYr != null
							&& lsVehModlYr.length() == VEH_YEAR_MAX_LEN)
						{
							gettxtVehicleModelYear().setText(
								lsVehModlYr);
						}
						if (lsVin != null)
						{
							getlblVIN().setText(lsVin);
						}
						else
						{
							getlblVIN().setText(EMPTY);
						}
						if (lsModl != null)
						{
							gettxtVehicleModel().setText(lsModl);
						}
						populateVehicleMakes();
						populateBodyTypes();
						populateBrand();
						populateTrailerType();
						// defect 10689
						populateVehColorVector();
						populateMajorColor();
						populateMinorColor();
						// end defect 10689
						
						// defect 10959 
						String lsRegDesc =
							CommonFeesCache
								.getRegClassCdDesc(
								liRegCd,
								liEffDt);
						//	TitleClientUtilityMethods.getRegClassCdDesc(
						//		liRegCode,
						//		liDate);
						// end defect 10959 
						
						getlblVehicleClassCode().setText(lsVehCd);
						getlblClassCodeDesc1().setText(lsRegDesc);
						// defect 11126 
						if (SystemProperty.isDevStatus())
						{
							System.out.println(caVehData.getVehOdmtrReadng()); 
						}
//						gettxtVehicleEmptyWeight().setText(EMPTY);
//						
//						// The following block of code is for DTA 
//						//	transactions
//						if (caVehData.getVehBdyVin() != null)
//						{
//							gettxtVehicleBodyVIN().setText(
//								caVehData.getVehBdyVin());
//						}
//						// VehOdomtrReading
//						if (caVehData.getVehOdmtrReadng() != null)
//						{
//							gettxtVehicleOdometerReading().setText(
//								caVehData.getVehOdmtrReadng());
//						}
//						// *************************************************
//						if (caVehData.getVehOdmtrBrnd() != null
//							&& !caVehData
//								.getVehOdmtrReadng()
//								.equalsIgnoreCase(
//								EXEMPT))
//						{
//							String lsOdoBrnd =
//								caVehData.getVehOdmtrBrnd();
//							if (lsOdoBrnd
//								.equals(OdometerBrands.ACTUAL_CODE))
//							{
//								getcomboVehicleOdometerBrand()
//									.setSelectedItem(
//									OdometerBrands.ACTUAL);
//							}
//							else if (
//								lsOdoBrnd.equals(
//									OdometerBrands.EXCEED_CODE))
//							{
//								getcomboVehicleOdometerBrand()
//									.setSelectedItem(
//									OdometerBrands.EXCEED);
//							}
//							else if (
//								lsOdoBrnd.equals(
//									OdometerBrands.NOTACT_CODE))
//							{
//								getcomboVehicleOdometerBrand()
//									.setSelectedItem(
//									OdometerBrands.NOTACT);
//							}
//							// defect 8479
//							comboBoxHotKeyFix(
//								getcomboVehicleOdometerBrand());
//							// end defect 8479
//						}
//						// *************************************************
//
//						if (caVehData.getVehEmptyWt() != ZERO)
//						{
//							gettxtVehicleEmptyWeight().setText(
//								String.valueOf(
//									caVehData.getVehEmptyWt()));
//						}
//						if (caVehData.getVehTon() != null)
//						{
//							gettxtVehicleTonnage().setText(
//								caVehData.getVehTon().getValue());
//						}
//						if (caVehData.getFxdWtIndi() == 1)
//						{
//							getchkFixedWeight().setSelected(true);
//						}
//						if (caVehData.getPrmtReqrdIndi() == 1)
//						{
//							getchkPermitRequired().setSelected(true);
//						}
//						if (caVehData.getVehLngth() > ZERO)
//						{
//							gettxtTrlrLength().setText(
//								String.valueOf(
//									caVehData.getVehLngth()));
//						} 
//						else
//						{
//							gettxtTrlrLength().setText(
//								String.valueOf(ZERO));
//						} //  end else
//						if (caVehData.getTrlrType() != null)
//						{
//							String lsTrlrType = caVehData.getTrlrType();
//							if (lsTrlrType
//								.equals(TrailerTypes.FULL_CODE))
//							{
//								getcomboTrailerType().setSelectedItem(
//									TrailerTypes.FULL);
//							}
//							else if (
//								lsTrlrType.equals(
//									TrailerTypes.SEMI_CODE))
//							{
//								getcomboTrailerType().setSelectedItem(
//									TrailerTypes.SEMI);
//							}
//							// defect 8479
//							comboBoxHotKeyFix(getcomboTrailerType());
//							// end defect 8479
//						}
//						String lsReplicaYr =
//							String.valueOf(
//								caVehData.getReplicaVehModlYr());
//						if (lsReplicaYr != null
//							&& lsReplicaYr.length() == VEH_YEAR_MAX_LEN)
//						{
//							gettxtReplicaVehicleModelYear().setText(
//								lsReplicaYr);
//						}
//						if (caVehData.getReplicaVehMk() != null)
//						{
//							gettxtReplicaVehicleMake().setText(
//								caVehData.getReplicaVehMk());
//						}
						// end defect 11126 
					}
					if (laTtlData != null)
					{
						int liTtltype = laTtlData.getTtlTypeIndi();
						String lsTtlType =
							CommonValidations.getTtlTypeString(
								liTtltype);
						getlblClassCodeDesc().setText(lsTtlType);
					}

					// Set field display for No Title Record Found frame
					disableFields();

					//******************************************************
					// Set OdomtrBrand if VehOdomtrReading = EXEMPT
					if (caVehData.getVehOdmtrReadng() != null
						&& caVehData.getVehOdmtrReadng().equalsIgnoreCase(
							EXEMPT))
					{
						getcomboVehicleOdometerBrand()
							.setSelectedIndex(
							-1);
						getstcLblBrand().setEnabled(false);
						getcomboVehicleOdometerBrand().setEnabled(
							false);
					}
					// defect 11126 
					//******************************************************
					// Set Vehicle Gross Weoght
//					String lsEmptyWt =
//						gettxtVehicleEmptyWeight().getText().trim();
//
//					String lsCarryingCap =
//						gettxtVehicleCarryingCapacity()
//							.getText()
//							.trim();
//
//					int liGrossWt =
//						CommonValidations.calcGrossWeight(
//							lsEmptyWt,
//							lsCarryingCap);
//
//					getlblVehicleGrossWeight().setText(
//						Integer.toString(liGrossWt));
// end defect 11126 
					
					// defect 10597 
					resetOdometerBrand();
					// end defect 10597 

					gettxtVehicleModelYear().requestFocus();

					// defect 10507 
				}
				// end defect 10507 
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
		catch (NumberFormatException aeNFEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNFEx);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
	}

	/**
	 * Sets the data from the screen to the vehicle object.
	 */
	private void setDataToDataObject()
	{
		// defect 11126 
		UtilityMethods.trimRTSInputField(this);
		// end defect 11126 
		
		//defects 6029, 6090, 6134, and 6312
		//Added code from VCTitleTypesTTL002.processData() 
		//which was to fix code for defect 5590 but caused 
		//the above defects.
		//If getRegWaivedIndi = 1 set setDpsStlnIndi to 1 
		if (caVehInqData.getMfVehicleData() != null)
		{
			if (caRegData.getRegWaivedIndi() == 1)
			{
				caVehData.setDpsStlnIndi(1);
			}
		}
		// end defects 6029, 6090, 6134, and 6312

		String lsModlYr = gettxtVehicleModelYear().getText();
		
		// Make 
		String lsVehMk = null;

		if (getcomboVehicleMake().isVisible())
		{
			String lsTempVehMk =
				(String) getcomboVehicleMake()
				.getSelectedItem();
			
			int liIndexDash = lsTempVehMk.indexOf('-');
			lsVehMk = lsTempVehMk.substring(0, liIndexDash).trim();
		}
		else
		{
			lsVehMk = gettxtVehMake().getText();
		}
		// Body Style 
		String lsTempStrBodSt =
			(String) getcomboBodyType().getSelectedItem();
		int liIndexDash = lsTempStrBodSt.indexOf('-');
		String lsBodStyl =
			lsTempStrBodSt.substring(0, liIndexDash).trim();
		
		// Model 
		String lsVehMod = gettxtVehicleModel().getText();
		String lsBdyVin = gettxtVehicleBodyVIN().getText();
		String lsOdoRndng =	gettxtVehicleOdometerReading().getText();
		caVehData.setVehOdmtrReadng(lsOdoRndng);

		// defect 7898
		// Ensure that Brand set correctly.
		String lsOdoBrand = CommonConstant.STR_SPACE_EMPTY;
		if (!lsOdoRndng
				.equals(CommonConstant.STR_SPACE_EMPTY)
				&& !lsOdoRndng.equals(EXEMPT))
		{
			if (getcomboVehicleOdometerBrand()
					.getSelectedItem()
					!= null)
			{
				lsOdoBrand =
					(String) getcomboVehicleOdometerBrand()
					.getSelectedItem();
				if (lsOdoBrand
						.equals(OdometerBrands.ACTUAL))
				{
					lsOdoBrand = OdometerBrands.ACTUAL_CODE;
				}
				else if (
						lsOdoBrand.equals(
								OdometerBrands.EXCEED))
				{
					lsOdoBrand = OdometerBrands.EXCEED_CODE;
				}
				else if (
						lsOdoBrand.equals(
								OdometerBrands.NOTACT))
				{
					lsOdoBrand = OdometerBrands.NOTACT_CODE;
				}
			}
			else
			{
				lsOdoBrand = OdometerBrands.ACTUAL_CODE;
			}
		}
		caVehData.setVehOdmtrBrnd(lsOdoBrand);
		// end defect 7898 

		String lsRepVehModlYr =	gettxtReplicaVehicleModelYear().getText();
		String lsRepVehMk =	gettxtReplicaVehicleMake().getText();
		String lsTon = gettxtVehicleTonnage().getText();
		lsTon = UtilityMethods.isEmpty(lsTon) ? "0" : lsTon; 
		String lsEmptyWt =gettxtVehicleEmptyWeight().getText();
		String lsVehLen =gettxtTrlrLength().getText();
		String lsTrlWidthFeet =	gettxtFeet().getText();
		String lsTrlWidthInches =gettxtInches().getText();
		String lsTrlType = (String) getcomboTrailerType().getSelectedItem();

		// Model Year
		if (lsModlYr != null && lsModlYr.length() > ZERO)
		{
			int liYr = Integer.parseInt(lsModlYr);
			caVehData.setVehModlYr(liYr);
		}

		caVehData.setVehMk(lsVehMk);
		caVehData.setVehBdyType(lsBodStyl);
		caVehData.setVehModl(lsVehMod);
		caVehData.setVehBdyVin(lsBdyVin);

		// defect 10689
		// Set the Major Color
		String lsColorCd = CommonConstant.STR_SPACE_EMPTY;

		lsColorCd = getMajorColorCdSelected();
		if (lsColorCd == null)
		{
			lsColorCd = CommonConstant.STR_SPACE_EMPTY;
		}
		caVehData.setVehMjrColorCd(lsColorCd);

		// Set the Minor Color
		lsColorCd = getMinorColorCdSelected();
		if (lsColorCd == null)
		{
			lsColorCd = CommonConstant.STR_SPACE_EMPTY;
		}
		caVehData.setVehMnrColorCd(lsColorCd);
		// end defect 10689
		
		// Replica Model Year 
		if (lsRepVehModlYr != null
				&& lsRepVehModlYr.length() > ZERO)
		{
			int liRepVehModlYr =
				Integer.parseInt(lsRepVehModlYr);
			caVehData.setReplicaVehModlYr(liRepVehModlYr);
		}
		
		// Replica Make 
		if (lsRepVehMk != null
				&& lsRepVehMk.length() > ZERO)
		{
			caVehData.setReplicaVehMk(lsRepVehMk);
		}
		// Tonnage 
		if (lsTon != null && lsTon.length() > ZERO)
		{
			Dollar laTon = new Dollar(lsTon);
			caVehData.setVehTon(laTon);
		}
		// Empty Weight 
		if (lsEmptyWt != null && lsEmptyWt.length() > ZERO)
		{
			int liEmpWt = Integer.parseInt(lsEmptyWt);
			caVehData.setVehEmptyWt(liEmpWt);
		}
		// Length 
		if (lsVehLen != null && lsVehLen.length() > ZERO)
		{
			int liLng = Integer.parseInt(lsVehLen);
			caVehData.setVehLngth(liLng);
		}

		// Width
		//CQU100004905 - K Salvi - changed the checks to 
		//	make sure that width is populated even if either
		//	 one of feet and inches is empty.
		double ldWidth = 0.0;

		if (gettxtFeet().isEnabled()
				&& !lsTrlWidthFeet.equals(EMPTY))
		{
			ldWidth
			+= (Double.parseDouble(lsTrlWidthFeet) * INCHES_PER_FOOT);
		}

		if (gettxtInches().isEnabled()
				&& !lsTrlWidthInches.equals(EMPTY))
		{
			ldWidth += Double.parseDouble(lsTrlWidthInches);
		}
		caVehData.setVehWidth(ldWidth);

		// Trailer Type 
		// Set trailer type to first character in the choice
		//	selected
		if (lsTrlType != null
				&& getcomboTrailerType().isEnabled())
		{
			caVehData.setTrlrType(
					lsTrlType.substring(0, 1));
		}
		else
		{
			caVehData.setTrlrType(EMPTY);
		}
		
		// Permit Required 
		if (getchkPermitRequired().isSelected())
		{
			caVehData.setPrmtReqrdIndi(1);
		}
		else
		{
			caVehData.setPrmtReqrdIndi(ZERO);
		}
		
		// Fixed Weight 
		if (getchkFixedWeight().isSelected())
		{
			caVehData.setFxdWtIndi(1);
		}
		else
		{
			caVehData.setFxdWtIndi(ZERO);
		}
		// Carrying Capacity / Gross Weight 
		String lsCarryCap =
			gettxtVehicleCarryingCapacity()
			.getText();
		
		String lsGrosWt = getlblVehicleGrossWeight().getText();
		int liCarrCap = 0;
		int liGrosWt = 0;

		if (lsCarryCap != null
				&& lsCarryCap.length() > ZERO)
		{
			liCarrCap = Integer.parseInt(lsCarryCap);
		}

		if (lsGrosWt != null && lsGrosWt.length() > ZERO)
		{
			liGrosWt = Integer.parseInt(lsGrosWt);
		}
		caRegData.setVehCaryngCap(liCarrCap);
		caRegData.setVehGrossWt(liGrosWt);
	}
	
	/**
	 * Set Field from RegisClassValue 
	 * 
	 * @param liIndi
	 * @param aaLabel
	 * @param aaInputField
	 * @param asInputData
	 */
	private void setFromRegisClassVehData(int liIndi, JLabel aaLabel, 
			RTSInputField aaInputField, String asInputData)
	{
		boolean lbEnable = int2bool(liIndi);
		
		if (lbEnable) 
		{
			if (aaInputField.isEmpty())
			{
				try
				{
					int liInputData = Integer.parseInt(asInputData);
					if (liInputData == 0)
					{
						asInputData = new String(); 
					}
				}
				catch (NumberFormatException aeNFE)
				{
					
				}
				aaInputField.setText(asInputData);
			}
		}
		else
		{
			aaInputField.setText(new String()); 
		}
		aaInputField.setEnabled(lbEnable);
		aaLabel.setEnabled(lbEnable);
	}
	
	/**
	 * Convert an int to a boolean.
	 * <br>0 is false.
	 * <br>Non-zero is true.
	 * 
	 * @param aiIndi int
	 * @return boolean
	 */
	private boolean int2bool(int aiIndi)
	{
		return (aiIndi != 0);
	}

	

	/**
	 * Set the odometer brand combo box enable and disabled depending
	 * on what was typed in the VehicleOdometerReading field.
	 */
	private void setVehOdomtrBrand()
	{
		String lsOdoRd =
			gettxtVehicleOdometerReading().getText().trim();
		if (lsOdoRd.equalsIgnoreCase(EXEMPT))
		{
			getstcLblBrand().setEnabled(false);
			getcomboVehicleOdometerBrand().setEnabled(false);
			getcomboVehicleOdometerBrand().setSelectedIndex(-1);
		}
		else
		{
			getstcLblBrand().setEnabled(true);
			getcomboVehicleOdometerBrand().setEnabled(true);
			populateBrand();
		}
	}

	/**
	 * Verify Indicators
	 *
	 * @return boolean
	 */
	private boolean verifyIndicators()
	{
		boolean lbRet = true;

		if (caVehInqData.hasHardStops(getController().getTransCode()))
		{
			if (!caVehInqData.hasAuthCode())
			{
				getController().processData(
					VCNoTitleRecordTTL004.VTR_AUTH,
					caVehInqData);

				lbRet = caVehInqData.hasAuthCode();
			}
		}
		return lbRet;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
