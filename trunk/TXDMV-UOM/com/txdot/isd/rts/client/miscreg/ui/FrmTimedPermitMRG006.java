package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientBusiness;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientUtilityMethods;

import com.txdot.isd.rts.services.cache.VehicleBodyTypesCache;
import com.txdot.isd.rts.services.cache.VehicleMakesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmTimedPermitMRG006.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date        Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and
 *							doneWorking()
 * J Kwik		04/23/2002	changed the AM/PM combo setting.
 *							defect 3601
 * T Pederson   04/30/2002  Don't allow spaces to be entered in input
 *							field
 * MAbs			05/29/2002	Validated timefield with exception
 *							defect 4135
 * S Govindappa 07/01/2002	Made changes to validateEffTime(RTSException
 *							ex), populateDatesAndTimes(),
 *                          handleMilitaryTime(RTSDate aRTSDate) fields
 *							to prevent switching of AM to PM
 *                          and effective date when the effective time
 *							entered is not a military time(>13hrs).
 *							defect 4386 and 4392 
 * MAbs			07/02/2002	Fixing error when escaping off of 2nd screen.
 *							defect 4392
 * MAbs			07/08/2002	Not clearing red fields after AM/PM change
 *							defect 4419
 * Bob Brown	07/23/2002	Lined up field labels so
 *							the colons were vertically aligned in VCE
 *							defect 4477, 
 * Bob Brown/	08/05/2002	added code in focusGained
 *	Govindappa				to change the expiration date on tabbing off
 *							(losing focus from) effective date field.
 *							Also commented out code in
 *                          validateEffDt (RTSException) which compared
 *							the user entered effective date with the
 *                          Timed permit object effective date.
 *							defect 4530, 
 * Bob Brown/	08/21/2002	Added code in focusGained
 *	Govindappa				to change the expiration time on tabbing off
 *							(losing focus from) effective time field.
 *							defect 4630
 *							added a check in validateEffTime()
 *							to add 12 to PM time only if the PM hour is
 *                          not 12. Added ActionListener to
 *							gettxtEffectimeTime().
 *							defect 4575 
 * J Seifert	12/10/2003	Validate Model Year (gettxtYear()) only if
 *							it is not empty. Allow this field to be
 *							optional. Changed: validateInputFields(),
 *							setInputFields()
 *							defect 6422 ver. 5.1.5.2
 * J Zwiener	06/29/2004  enlarged Make field on Timed Permit MRG006
 *							modified thru VCE
 *							defect 6669 Ver 5.2.1
 * B Hargrove	11/18/2004  Change errmsg process so that they beep:
 *							model year, body style, state, zip, zip4
 *							add error messages to RTS_ERR_MSGS
 *							modify validateInputFields()
 *							defect 6870 Ver 5.2.2
 * K Harrell	01/13/2005	JavaDoc/Formatting/Variable Name Cleanup
 * 							Do not show "0" in Yr after same vehicle if
 *							not populated in prior transaction.
 *							modify handleData() 
 *							defect 7864 Ver 5.2.3  
 * Ray Rowehl	02/08/2005	Remove import for fees
 * 							delete import
 * 							defect 7705 Ver 5.2.3	
 * B Hargrove	03/18/2005	Modify code for move to Java 1.4.
 *							Bring code to standards
 * 							Comment out setNextFocusableComponent() 
 *  	 					Remove unused variables.
 * 							Remove keyPressed()  (arrow key handling is
 * 							done in ButtonPanel.
 *							modify gettxtOwnerName2()
 * 							delete implements KeyListener
 *							defect 7893 Ver 5.2.3
 * B Hargrove	04/11/2005	Added JPanel to allow tabbing down left side
 * 							before tabbing down right side 
 *							added jPanel
 *							defect 7893 Ver 5.2.3
 * B Hargrove	07/15/2005	Refactor\Move 
 * 							MiscellaneousRegClientUtilityMethods class  
 *							to com.txdot.isd.rts.client.miscreg.business.
 *							defect 7893 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	01/24/2006	Alignment work.	Move cursor to 1st line of 
 * 							Address upon selection/deselection of "USA" 
 * 							checkbox.
 * 							defect 7893 Ver 5.2.3 	
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify focusLost() 
 * 							defect 8902 Ver Exempts
 * K Harrell	04/06/2007	CommonValidations.convert_i_and_o_to_1_and_0() 
 * 							call is now static
 * 							defect 9085 Ver Special Plates 
 * T Pederson	05/20/2008	Changed populateAmPmCombo() so only AM and  
 * 							PM will be the choices in combo box.
 * 							modify populateAmPmCombo()
 *							defect 8601 Ver Defect_POS_A
 * K Harrell	06/08/2009	Implement RTSDate.getClockTimeNoMs(), 
 * 								additional class cleanup.
 * 							add BDY_STYLE_REQD_LENGTH, BODY_STYLE
 * 							add ivjJPanel, get method 
 * 							delete jPanel, get method
 *							modify populateDatesAndTimes(),
 *							 validateEffDt(), validateEffTime(),
 *							 actionPerformed(), validateInputFields() 
 * 							defect 9943 Ver Defect_POS_F 
 * K Harrell	06/30/2009	Implement new OwnerData
 * 							delete CNTRY_ZIP_MAXLENGTH
 * 							delete getVehData(), setVehData(), getAddressData(),
 * 							 setAddressData(), getMFVehData(), setMFVehData(),
 * 							 setOwnrData(), getOwnrData()
 * 							modify handleData()
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	07/18/2009	Cleanup for consistency between TTL007 and 
 * 							REG033.
 * 							add caNameAddrComp
 * 							modify initialize(), handleData(),
 * 							 itemStateChanged(), setInputFields(), 
 * 							 validateInputFields()
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	09/02/2009	Assign max length, type for ivjtxtOwnerCntryZpcd 
 * 							modify gettxtOwnerCntryZpcd()
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	01/25/2010	Do not clear OwnerData when return from
 * 							MRG001.  
 * 							modify handleData() 
 * 							defect 10340 Ver Defect_POS_H
 * K Harrell	02/12/2010	Correct error introduced by 10340 - System
 * 							error when cancel back from MRG001 
 * 							 (30 Day Permit)  
 * 							modify handleData()
 * 							defect 10346 Ver Defect_POS_H
 * K Harrell	06/20/2010	Update for Timed Permit Project 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/07/2010	Add processing logic for HQ.  Search for 
 * 							  VehMkDesc if missing. Allow restore of 
 * 							  combo box after select "-NEW-" or initialized
 * 							  to non-standard VehMk. 
 * 							add confirmForAutoTrans(), keyPressed()
 * 							modify actionPerformed(), 
 * 							   setVehicleMakesToDisplay()
 * 							defect 10491 Ver 6.5.0  
 * K Harrell	07/11/2010	Implement new ErrorsConstant for Applicant  
 * 							Restore combo color after error as 
 * 							clearColor(this) makes white.
 * 							add restoreComboColor()
 * 							modify actionPerformed(), itemStateChanged(), 
 * 							 initialize(), validateName()
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/15/2010	Restrict presentation of Help to "Help is not
 * 							 Available" message. 
 * 							modify actionPerformed() 
 * 							defect 10491 Ver 6.5.0 	
 * K Harrell	07/18/2010	Modified mnemonic for comboBodyType to 'T' 
 * 							disable Name labels on Inquiry/Duplicate Permit 
 * 							 when Business
 * 							modify getstcLblBodyStyle(), 
 * 							 setCustomerDataToDisplay() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/20/2010 	use KeyListener vs. FocusListener for 
 * 							validation of Effective Date/Time.
 * 							delete cbChangeAMPMCombo, cbRecalcExpDate,
 * 							 cbRecalcExpTime
 * 							delete focusGained(), focusLost()  
 * 							modify keyPressed(), gettxtEffectiveDate(), 
 * 							  gettxtEffectiveTime()
 * 							defect 10491 Ver 6.5.0
 * K Harrell	07/29/2010	Updated requirements from VTR:
 * 							1)Veh Reg State/Cntry are not required 
 * 							2)Veh Reg Country either CANA/MEXI
 * 						       only applies to 72/144 Hr Permits.
 * 							modify validateData() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	09/08/2010	add "Issued By:" Label/Text. 
 * 							Screen adjustments via Visual Editor.  
 * 							add ivjstcLblIssuedBy, ivjlblIssuedBy get 
 * 								methods
 *							add COUNTY, REGION, REGIONAL_OFFICE
 *  						modify setInqFieldsToDisplay(), 
 * 								getJPanelIssueDateAmtPaid() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	10/01/2010	Implement ScreenMRG006SavedData
 * 							add saveScreenMRG006Data() 
 * 							modify actionPerformed(),
 * 							 setDataToDataObject(), 
 * 							 setOneTripDataToDisplay(), 
 * 							 validateEffTime()    
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	10/22/2010	Save Phone No even if invalid on Cancel 
 * 							modify setDateToDataObject() 
 * 							defect 10592 Ver 6.6.0 
 * K Harrell	01/07/2011	Construct Screen title from ItmCd; 
 * 							 Implement new PermitData methods for  
 * 							  Issuing Office Name 
 * 							delete COUNTY, REGION, REGIONAL_OFFICE  
 * 							modify setPermitTitleToDisplay(), 
 * 							 setInqFieldsToDisplay()  
 * 							defect 10726 Ver 6.7.0 
 * K Harrell	06/02/2011	changes to handle Permit Modification
 * 							add ivjtxtVIN, get method
 * 							add ivjlblPrmtIssuanceId, 
 * 							 ivjlblAuditTrailTransId, get methods
 * 							add setDevDataToDisplay(), 
 * 							  disableEffDateTimeIfNeeded()
 * 							delete setFDPTToDisplay() 
 * 							modify setData(), setDataToDataObject(),
 * 							 setPermitDataToDisplay(),
 * 							 setVehicleDataToDisplay(), validateData(), 
 * 							 validateEffDt(), setInqFieldsToDisplay(), 
 * 							 getFrmTimedPermitMRG006ContentPane1(),
 * 							 getJPanelVehicleInfo()
 * 							defect 10844 Ver 6.8.0 
 * K Harrell	09/06/2011	Convert O/I to 0/l 
 * 							modify setDataToDataObject() 
 * 							defect 10989 Ver 6.8.0 
 * K Harrell	10/18/2011	Validate Registered Country with new rules
 * 							modify validateData() 
 * 							defect 11004 Ver 6.9.0  
 * ---------------------------------------------------------------------
 */

/**
 * MRG006 handles the Presentation/Collection of Timed Permit Data
 *
 * @version	6.9.0   		10/18/2011
 * @author  Kathy Harrell
 * @author	Joseph Kwik	
 * <br>Creation Date:		06/21/2004 08:52:13
 */
public class FrmTimedPermitMRG006
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkRegisteredUSA = null;
	private JCheckBox ivjchkUSA = null;
	private JComboBox ivjcomboBodyType = null;
	private JComboBox ivjcomboEffectiveAMPM = null;
	private JComboBox ivjcomboVehicleMake = null;
	private JPanel ivjFrmTimedPermitMRG006ContentPane1 = null;
	private JPanel ivjJPanel6 = null;
	private JPanel ivjJPanelAddress = null;
	private JPanel ivjJPanelApplicantType = null;
	private JPanel ivjJPanelBusinessName = null;
	private JPanel ivjJPanelContactInfo = null;
	private JPanel ivjJPanelIndividualName = null;
	private JPanel ivjJPanelIssueDateAmtPaid = null;
	private JPanel ivjJPanelOTPT = null;
	private JPanel ivjJPanelPermitInfo = null;
	private JPanel ivjJPanelVehicleInfo = null;
	private JLabel ivjlblAmountPaid = null;
	private JLabel ivjlblExpirationAMPM = null;
	private JLabel ivjlblExpirationDate = null;
	private JLabel ivjlblExpirationTime = null;
	private JLabel ivjlblIssueDate = null;
	private JLabel ivjlblIssuedBy = null;
	private JLabel ivjlblPermitNo = null;
	private JLabel ivjlblVIN = null;
	private JPanel ivjPanelVehiclePermitInfo = null;
	private JRadioButton ivjradioBusiness = null;
	private JRadioButton ivjradioIndividual = null;
	private JLabel ivjstcLblAddress = null;
	private JLabel ivjstcLblAmountPaid = null;
	private JLabel ivjstcLblBodyStyle = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblDestn = null;
	private JLabel ivjstcLblEffectiveDate = null;
	private JLabel ivjstcLblEffectiveTime = null;
	private JLabel ivjstcLblEMail = null;
	private JLabel ivjstcLblExpirationDate = null;
	private JLabel ivjstcLblExpirationTime = null;
	private JLabel ivjstcLblFirstName = null;
	private JLabel ivjstcLblIntPt1 = null;
	private JLabel ivjstcLblIntPt2 = null;
	private JLabel ivjstcLblIntPt3 = null;
	private JLabel ivjstcLblIssued = null;
	private JLabel ivjstcLblIssuedBy = null;
	private JLabel ivjstcLblLastName = null;
	private JLabel ivjstcLblMiddleInitial = null;
	private JLabel ivjstcLblOrigin = null;
	private JLabel ivjstclblPermitNox = null;
	private JLabel ivjstcLblPermitTitle = null;
	private JLabel ivjstcLblPhoneNo = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblRegisteredStateCountry = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblYearMake = null;
	private RTSInputField ivjtxtBusinessName = null;
	private RTSInputField ivjtxtCustCity = null;
	private RTSInputField ivjtxtCustCntry = null;
	private RTSInputField ivjtxtCustCntryZpcd = null;
	private RTSInputField ivjtxtCustFirstName = null;
	private RTSInputField ivjtxtCustLastName = null;
	private RTSInputField ivjtxtCustMI = null;
	private RTSInputField ivjtxtCustState = null;
	private RTSInputField ivjtxtCustStreet1 = null;
	private RTSInputField ivjtxtCustStreet2 = null;
	private RTSInputField ivjtxtCustZpcd = null;
	private RTSInputField ivjtxtCustZpcdP4 = null;
	private RTSInputField ivjtxtDestn = null;
	private RTSDateField ivjtxtEffectiveDate = null;
	private RTSTimeField ivjtxtEffectiveTime = null;
	private RTSInputField ivjtxtEMail = null;
	private RTSInputField ivjtxtIntPt1 = null;
	private RTSInputField ivjtxtIntPt2 = null;
	private RTSInputField ivjtxtIntPt3 = null;
	private RTSInputField ivjtxtOrigin = null;
	private RTSPhoneField ivjtxtPhoneNo = null;
	private RTSInputField ivjtxtPlateNo = null;
	private RTSInputField ivjtxtRegisteredCountry = null;
	private RTSInputField ivjtxtRegisteredState = null;
	private RTSInputField ivjtxtVehMake = null;
	private RTSInputField ivjtxtVehModlYr = null;

	// defect 10844 
	private JLabel ivjlblAuditTrailTransId = null;
	private JLabel ivjlblPrmtIssuanceId = null;
	private RTSInputField ivjtxtVIN = null;
	// end defect 10844 

	// boolean
	private boolean cbINQ = false;
	private boolean cbOTPT = false;

	// String
	private String csItmCd = new String();
	private String csTransCd = new String();

	//	Objects 
	private NameAddressComponent caNameAddrComp = null;
	private PermitData caPrmtData = null;

	//	Vector 
	private Vector cvMFValid = new Vector();
	private Vector cvOTPTComponents = new Vector();

	// Constants
	private static final String ABBR_CANADA = "CANA";
	private static final String ABBR_MEXICO = "MEXI";
	private static final String AM = "AM";
	private static final String AMPM = "AMPM";
	private static final String BODY_STYLE = "Body Style:";
	private static final String DASH = "-";
	private static final String DFLT_ADDR =
		"123456789012345678901234567890";
	private static final String DFLT_CITY = "1234567890123456789";
	private static final String DFLT_STATE = "TX";
	private static final String DFLT_TIME = "11:15:15";
	private static final String EFF_DATE = "Effective Date:";
	private static final String EFF_TIME = "Effective Time:";
	private static final int END_OF_SECOND_INDEX = 8;
	private static final String EXP_DATE = "Expiration Date:";
	private static final String EXP_TIME = "Expiration Time:";
	private static final String NEW = "-NEW- -  ";
	private static final int NOON_HOUR = 12;
	private static final String PERMIT_TITLE_144_HOUR =
		"144-Hour Permit";
	private static final String PERMIT_TITLE_30_DAY = "30 Day Permit";
	private static final String PERMIT_TITLE_30_DAY_MOTORCYCLE =
		"30 Day Motorycle Permit";
	private static final String PERMIT_TITLE_72_HOUR = "72-Hour Permit";
	private static final String PERMIT_TITLE_FACTORY_DELIVERY =
		"Factory Delivery Permit";
	private static final String PERMIT_TITLE_ONE_TRIP =
		"One Trip Permit";
	private static final String PERMIT_TITLE_ONE_TRIP_MOTORCYCLE =
		"One Trip Motorcycle Permit";
	private static final String PM = "PM";
	private final static String SPACE_DASH =
		CommonConstant.STR_SPACE_ONE + CommonConstant.STR_DASH;
	private static final String STATIC_144_HOUR =
		"Static 144-Hour Permit";
	private static final String TITLE_MRG006 =
		"Timed Permit          MRG006";
	private static final String TWELVE = "12";
	private static final String USA = "USA";
	private static final String VIN = "VIN:";
	private static final String YEAR_MAKE = "Year/Make:";
	private static final String ZERO = "0";
	private static final String ZEROZERO = "00";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmTimedPermitMRG006 laFrmTimedPermitMRG006;
			laFrmTimedPermitMRG006 = new FrmTimedPermitMRG006();
			laFrmTimedPermitMRG006.setModal(true);
			laFrmTimedPermitMRG006
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmTimedPermitMRG006.show();
			java.awt.Insets insets = laFrmTimedPermitMRG006.getInsets();
			laFrmTimedPermitMRG006.setSize(
				laFrmTimedPermitMRG006.getWidth()
					+ insets.left
					+ insets.right,
				laFrmTimedPermitMRG006.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmTimedPermitMRG006.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmTimedPermitMRG006 constructor comment.
	 */
	public FrmTimedPermitMRG006()
	{
		super();
		initialize();
	}

	/**
	 * FrmTimedPermitMRG006 constructor comment with parent.
	 *
	 * @param aaParent JDialog
	 */
	public FrmTimedPermitMRG006(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmTimedPermitMRG006 constructor comment with parent.
	 *
	 * @param aaParent JFrame
	 */
	public FrmTimedPermitMRG006(JFrame aaParent)
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
			restoreComboColor();

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				processEnter();
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				releaseVirtualInventoryIfHeld();

				// defect 10592 
				if (!cbINQ)
				{
					setDataToDataObject();
				}
				// end defect 10592 

				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				// defect 10491 
				//	String lsTransCd =
				//		cbINQ ? caPrmtData.getTransCd() : csTransCd;
				//
				//	if (lsTransCd.equals(TransCdConstant.PT144))
				//	{
				//		RTSHelp.displayHelp(RTSHelp.MRG006_144_HOUR);
				//	}
				//	else if (lsTransCd.equals(TransCdConstant.PT30))
				//	{
				//		RTSHelp.displayHelp(RTSHelp.MRG006_30_DAY);
				//	}
				//	else if (lsTransCd.equals(TransCdConstant.PT72))
				//	{
				//		RTSHelp.displayHelp(RTSHelp.MRG006_72_HOUR);
				//	}
				//	else if (lsTransCd.equals(TransCdConstant.FDPT))
				//	{
				//		RTSHelp.displayHelp(RTSHelp.MRG006_FACTORY);
				//	}
				//	else if (lsTransCd.equals(TransCdConstant.OTPT))
				//	{
				//		RTSHelp.displayHelp(RTSHelp.MRG006_ONE_TRIP);
				//	}
				//	else
				//	{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
				//	}
				// end defect 10491 
			}
			else if (aaAE.getSource().equals(getcomboEffectiveAMPM()))
			{
				String lsObj =
					(String) getcomboEffectiveAMPM().getSelectedItem();
				if (lsObj != null && lsObj.equals(AM))
				{
					getlblExpirationAMPM().setText(AM);
				}
				else if (
					lsObj != null
						&& lsObj.equals(PM)
						&& (caPrmtData
							.getTimedPrmtType()
							.equals(TransCdConstant.PT72)
							|| caPrmtData.getTimedPrmtType().equals(
								TransCdConstant.PT144)))
				{
					getlblExpirationAMPM().setText(PM);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Confirm that user wishes to complete transaction if HQ
	 * 
	 * 
	 * @return booelan 
	 */
	private boolean confirmForAutoTrans()
	{
		boolean lbReturn = true;

		if (SystemProperty.isHQ())
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.CTL001,
					CommonConstant.TXT_COMPLETE_TRANS_QUESTION,
					ScreenConstant.CTL001_FRM_TITLE);

			int liResponse = leRTSEx.displayError(this);

			if (liResponse == RTSException.NO)
			{
				lbReturn = false;
			}
		}
		return lbReturn;
	}

	/**
	 * Set FDPT data 
	 *
	 */
	private void disableEffDateTimeIfNeeded()
	{
		// Disable permit effective date and time if 
		// (Factory Delivery) || (Modify Permit && Current)
		if (csItmCd.equals(TransCdConstant.FDPT)
			|| (csTransCd.equals(TransCdConstant.MODPT)
				&& caPrmtData.isCurrent()))
		{
			gettxtEffectiveDate().setEditable(false);
			gettxtEffectiveDate().setEnabled(false);
			gettxtEffectiveTime().setEditable(false);
			gettxtEffectiveTime().setEnabled(false);
			getcomboEffectiveAMPM().setEditable(false);
			getcomboEffectiveAMPM().setEnabled(false);
		}
	}

	// defect 10491 
	//	/**
	//	 * Invoked when a component loses the keyboard focus.
	//	 *
	//	 * @param aaFE FocusEvent
	//	 */
	//	public void focusGained(FocusEvent aaFE)
	//	{
	//		if (aaFE.getSource() == gettxtEffectiveDate())
	//		{
	//			cbRecalcExpDate = true;
	//			if (cbRecalcExpTime)
	//			{
	//				cbRecalcExpTime = false;
	//				RTSException leRTSEx = new RTSException();
	//				validateEffTime(leRTSEx);
	//				if (leRTSEx.isValidationError())
	//				{
	//					leRTSEx.displayError(this);
	//					leRTSEx.getFirstComponent().requestFocus();
	//				}
	//			}
	//		}
	//		else if (aaFE.getSource() == gettxtEffectiveTime())
	//		{
	//			cbRecalcExpTime = true;
	//			if (cbRecalcExpDate)
	//			{
	//				cbRecalcExpDate = false;
	//				RTSException leRTSEx = new RTSException();
	//				validateEffDt(leRTSEx);
	//				if (leRTSEx.isValidationError())
	//				{
	//					leRTSEx.displayError(this);
	//					leRTSEx.getFirstComponent().requestFocus();
	//				}
	//			}
	//		}
	//		else if (
	//			aaFE.getSource() != getButtonPanel1().getBtnEnter()
	//				&& aaFE.getSource() != getButtonPanel1().getBtnCancel()
	//				&& aaFE.getSource() != getButtonPanel1().getBtnHelp())
	//		{
	//			if (cbRecalcExpDate)
	//			{
	//				cbRecalcExpDate = false;
	//				RTSException leRTSEx = new RTSException();
	//				validateEffDt(leRTSEx);
	//				if (leRTSEx.isValidationError())
	//				{
	//					leRTSEx.displayError(this);
	//					leRTSEx.getFirstComponent().requestFocus();
	//				}
	//			}
	//			if (cbRecalcExpTime)
	//			{
	//				cbRecalcExpTime = false;
	//				RTSException leRTSEx = new RTSException();
	//				validateEffTime(leRTSEx);
	//				if (leRTSEx.isValidationError())
	//				{
	//					leRTSEx.displayError(this);
	//					leRTSEx.getFirstComponent().requestFocus();
	//				}
	//			}
	//		}
	//		else if (
	//			aaFE.getSource() == getButtonPanel1().getBtnEnter()
	//				|| aaFE.getSource() == getButtonPanel1().getBtnCancel())
	//		{
	//			cbRecalcExpDate = false;
	//			cbRecalcExpTime = false;
	//		}
	//	}
	//
	//	/**
	//	 * Invoked when a component loses the keyboard focus.
	//	 *
	//	 * @param aaFE FocusEvent
	//	 */
	//   public void focusLost(FocusEvent aaFE)
	//   {
	//	   if (!aaFE.isTemporary() && aaFE.getSource() == getlblVIN())
	//	   {
	//		   String lsVin = getlblVIN().getText().trim().toUpperCase();
	//		   // defect 8902
	//		   lsVin = CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
	//		   // end defect 8902
	//		   getlblVIN().setText(lsVin);
	//	   }
	//   }
	// end defect 10491

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
				ivjButtonPanel1.setSize(268, 34);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.setLocation(225, 507);
				ivjButtonPanel1.addActionListener(this);
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
	 * This method initializes ivjchkRegisteredUSA
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkRegisteredUSA()
	{
		if (ivjchkRegisteredUSA == null)
		{
			ivjchkRegisteredUSA = new JCheckBox();
			ivjchkRegisteredUSA.setSize(49, 20);
			ivjchkRegisteredUSA.setText(USA);
			ivjchkRegisteredUSA.setMnemonic(KeyEvent.VK_S);
			ivjchkRegisteredUSA.setLocation(292, 105);
		}
		return ivjchkRegisteredUSA;
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
				ivjchkUSA.setSize(49, 24);
				ivjchkUSA.setName("ivjchkUSA");
				ivjchkUSA.setText(USA);
				ivjchkUSA.setMinimumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA.setMaximumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA.setActionCommand(USA);
				ivjchkUSA.setSelected(true);
				// user code begin {1}
				ivjchkUSA.setMnemonic(KeyEvent.VK_U);
				ivjchkUSA.setLocation(237, 4);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjchkUSA;
	}

	/**
	 * This method initializes ivjcomboBodyType
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboBodyType()
	{
		if (ivjcomboBodyType == null)
		{
			ivjcomboBodyType = new JComboBox();
			ivjcomboBodyType.setSize(165, 22);
			ivjcomboBodyType.setLocation(81, 52);
		}
		return ivjcomboBodyType;
	}

	/**
	 * Return the ivjcomboEffectiveAMPM property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboEffectiveAMPM()
	{
		if (ivjcomboEffectiveAMPM == null)
		{
			try
			{
				ivjcomboEffectiveAMPM = new JComboBox();
				ivjcomboEffectiveAMPM.setSize(47, 22);
				ivjcomboEffectiveAMPM.setName("ivjcomboEffectiveAMPM");
				ivjcomboEffectiveAMPM.setSelectedIndex(-1);
				ivjcomboEffectiveAMPM.setMinimumSize(
					new java.awt.Dimension(46, 23));
				ivjcomboEffectiveAMPM.setEnabled(true);
				// user code begin {1}
				ivjcomboEffectiveAMPM.addActionListener(this);
				ivjcomboEffectiveAMPM.setLocation(184, 74);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjcomboEffectiveAMPM;
	}

	/**
	 * This method initializes ivjcomboVehicleMake
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboVehicleMake()
	{
		if (ivjcomboVehicleMake == null)
		{
			ivjcomboVehicleMake = new JComboBox();
			ivjcomboVehicleMake.setSize(186, 22);
			ivjcomboVehicleMake.setLocation(127, 26);
			ivjcomboVehicleMake.setSelectedIndex(-1);
		}
		return ivjcomboVehicleMake;
	}

	/**
	 * Return the ivjFrmTimedPermitMRG006ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmTimedPermitMRG006ContentPane1()
	{
		if (ivjFrmTimedPermitMRG006ContentPane1 == null)
		{
			try
			{
				ivjFrmTimedPermitMRG006ContentPane1 = new JPanel();
				ivjFrmTimedPermitMRG006ContentPane1.setName(
					"FrmTimedPermitMRG006ContentPane1");
				ivjFrmTimedPermitMRG006ContentPane1.setLayout(null);
				ivjFrmTimedPermitMRG006ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmTimedPermitMRG006ContentPane1.setMinimumSize(
					new java.awt.Dimension(565, 306));
				ivjFrmTimedPermitMRG006ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				ivjFrmTimedPermitMRG006ContentPane1.add(
					getstcLblPermitTitle(),
					null);
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getJPanelApplicantType(),
					null);
				// user code end
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getJPanelBusinessName(),
					null);
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getJPanelIndividualName(),
					null);
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getJPanelAddress(),
					null);
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getJPanelContactInfo(),
					null);
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getJPanelVehiclePermitInfo(),
					null);
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getJPanelIssueDateAmtPaid(),
					null);

				// defect 10844 
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getlblPrmtIssuanceId(),
					null);
				ivjFrmTimedPermitMRG006ContentPane1.add(
					getlblAuditTrailTransId(),
					null);
				// end defect 10844 
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjFrmTimedPermitMRG006ContentPane1;
	}

	/**
	 * Return ivjJPanel6 property value.
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
					new java.awt.Dimension(258, 24));

				ivjJPanel6.add(gettxtCustCity(), null);
				ivjJPanel6.add(gettxtCustState(), null);
				ivjJPanel6.add(gettxtCustZpcd(), null);
				ivjJPanel6.add(getstcLblDash(), null);
				ivjJPanel6.add(gettxtCustZpcdP4(), null);
				ivjJPanel6.add(gettxtCustCntry(), null);
				ivjJPanel6.add(gettxtCustCntryZpcd(), null);
				ivjJPanel6.setSize(307, 25);
				ivjJPanel6.setLocation(16, 83);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJPanel6;
	}

	/**
	 * This method initializes ivjJPanelAddress
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelAddress()
	{
		if (ivjJPanelAddress == null)
		{
			ivjJPanelAddress = new JPanel();
			ivjJPanelAddress.setLayout(null);
			ivjJPanelAddress.add(getchkUSA(), null);
			ivjJPanelAddress.add(gettxtCustStreet1(), null);
			ivjJPanelAddress.add(gettxtCustStreet2(), null);
			ivjJPanelAddress.add(getJPanel6(), null);
			ivjJPanelAddress.add(getstcLblAddress(), null);
			// Address
			ivjJPanelAddress.setSize(345, 113);
			Border b = new TitledBorder(new EtchedBorder(), "");
			ivjJPanelAddress.setBorder(b);
			ivjJPanelAddress.setLocation(364, 253);
		}
		return ivjJPanelAddress;
	}

	/**
	 * This method initializes ivjJPanelApplicantType
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelApplicantType()
	{
		if (ivjJPanelApplicantType == null)
		{
			ivjJPanelApplicantType = new JPanel();
			ivjJPanelApplicantType.setLayout(null);
			ivjJPanelApplicantType.add(getradioBusiness(), null);
			ivjJPanelApplicantType.add(getradioIndividual(), null);
			ivjJPanelApplicantType.setSize(348, 52);
			RTSButtonGroup laButtonGrp = new RTSButtonGroup();
			laButtonGrp.add(getradioIndividual());
			laButtonGrp.add(getradioBusiness());

			Border b =
				new TitledBorder(new EtchedBorder(), "Applicant Type:");
			ivjJPanelApplicantType.setBorder(b);
			ivjJPanelApplicantType.setLocation(362, 31);
		}
		return ivjJPanelApplicantType;
	}

	/**
	 * This method initializes ivjJPanelBusinessName
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelBusinessName()
	{
		if (ivjJPanelBusinessName == null)
		{
			ivjJPanelBusinessName = new JPanel();
			ivjJPanelBusinessName.setLayout(null);
			ivjJPanelBusinessName.add(gettxtBusinessName(), null);
			ivjJPanelBusinessName.setBounds(362, 87, 348, 53);
			Border b =
				new TitledBorder(new EtchedBorder(), "Business Name:");
			ivjJPanelBusinessName.setBorder(b);
		}
		return ivjJPanelBusinessName;
	}

	/**
	 * This method initializes ivjJPanelContactInfo
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelContactInfo()
	{
		if (ivjJPanelContactInfo == null)
		{
			ivjJPanelContactInfo = new javax.swing.JPanel();
			ivjJPanelContactInfo.setLayout(null);
			ivjJPanelContactInfo.add(getstcLblEMail(), null);
			ivjJPanelContactInfo.add(getstcLblPhoneNo(), null);
			ivjJPanelContactInfo.add(gettxtEMail(), null);
			ivjJPanelContactInfo.add(gettxtPhoneNo(), null);
			ivjJPanelContactInfo.setBounds(362, 372, 348, 83);
			Border b =
				new TitledBorder(
					new EtchedBorder(),
					"Contact Information:");
			ivjJPanelContactInfo.setBorder(b);
		}
		return ivjJPanelContactInfo;
	}

	/**
	 * This method initializes ivjJPanelIndividualName
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelIndividualName()
	{
		if (ivjJPanelIndividualName == null)
		{
			ivjJPanelIndividualName = new JPanel();
			ivjJPanelIndividualName.setLayout(null);
			ivjJPanelIndividualName.add(gettxtCustFirstName(), null);
			ivjJPanelIndividualName.add(gettxtCustLastName(), null);
			ivjJPanelIndividualName.add(getstcLblCustFirstName(), null);
			ivjJPanelIndividualName.add(getstcLblCustLastName(), null);
			ivjJPanelIndividualName.add(getstcLblCustMI(), null);
			ivjJPanelIndividualName.add(gettxtCustMI(), null);
			ivjJPanelIndividualName.setBounds(362, 143, 348, 105);
			Border b =
				new TitledBorder(
					new EtchedBorder(),
					"Individual Name:");
			ivjJPanelIndividualName.setBorder(b);
		}
		return ivjJPanelIndividualName;
	}

	/**
	 * This method initializes ivjJPanelIssueDateAmtPaid
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelIssueDateAmtPaid()
	{
		if (ivjJPanelIssueDateAmtPaid == null)
		{
			ivjJPanelIssueDateAmtPaid = new JPanel();
			ivjJPanelIssueDateAmtPaid.setLayout(null);
			ivjJPanelIssueDateAmtPaid.add(getstcLblIssued(), null);
			ivjJPanelIssueDateAmtPaid.add(getlblIssueDate(), null);
			ivjJPanelIssueDateAmtPaid.add(getstcLblAmountPaid(), null);
			ivjJPanelIssueDateAmtPaid.add(getlblAmountPaid(), null);
			ivjJPanelIssueDateAmtPaid.add(getstcLblIssuedBy(), null);
			ivjJPanelIssueDateAmtPaid.add(getlblIssuedBy(), null);
			ivjJPanelIssueDateAmtPaid.setBounds(364, 456, 354, 50);
		}
		return ivjJPanelIssueDateAmtPaid;
	}

	/**
	 * This method initializes ivjJPanelOTPT
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelOTPT()
	{
		if (ivjJPanelOTPT == null)
		{
			ivjJPanelOTPT = new JPanel();
			ivjJPanelOTPT.setLayout(null);
			ivjJPanelOTPT.add(gettxtOrigin(), null);
			ivjJPanelOTPT.add(gettxtIntPt1(), null);
			ivjJPanelOTPT.add(gettxtIntPt2(), null);
			ivjJPanelOTPT.add(gettxtIntPt3(), null);
			ivjJPanelOTPT.add(gettxtDestn(), null);
			ivjJPanelOTPT.add(getstcLblOrigin(), null);
			ivjJPanelOTPT.add(getstcLblIntPt1(), null);
			ivjJPanelOTPT.add(getstcLblIntPt2(), null);
			ivjJPanelOTPT.add(getstcLblIntPt3(), null);
			ivjJPanelOTPT.add(getstcLblDestn(), null);
			ivjJPanelOTPT.setBounds(6, 304, 345, 149);
			Border b =
				new TitledBorder(new EtchedBorder(), "One Trip:");
			ivjJPanelOTPT.setBorder(b);
		}
		return ivjJPanelOTPT;
	}

	/**
	 * This method initializes ivjJPanelPermitInfo
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelPermitInfo()
	{
		if (ivjJPanelPermitInfo == null)
		{
			ivjJPanelPermitInfo = new JPanel();
			ivjJPanelPermitInfo.setLayout(null);
			ivjJPanelPermitInfo.add(getstcLblExpirationDate(), null);
			ivjJPanelPermitInfo.add(getlblExpirationDate(), null);
			ivjJPanelPermitInfo.add(getstcLblExpirationTime(), null);
			ivjJPanelPermitInfo.add(getlblExpirationTime(), null);
			ivjJPanelPermitInfo.add(getlblExpirationAMPM(), null);
			ivjJPanelPermitInfo.add(getstcLblEffectiveDate(), null);
			ivjJPanelPermitInfo.add(gettxtEffectiveDate(), null);
			ivjJPanelPermitInfo.add(getstcLblEffectiveTime(), null);
			ivjJPanelPermitInfo.add(gettxtEffectiveTime(), null);
			ivjJPanelPermitInfo.add(getcomboEffectiveAMPM(), null);
			ivjJPanelPermitInfo.add(getstcLblPermitNo(), null);
			ivjJPanelPermitInfo.add(getlblPermitNo(), null);
			ivjJPanelPermitInfo.setBounds(7, 138, 345, 163);
			Border b =
				new TitledBorder(new EtchedBorder(), "Permit Info:");
			ivjJPanelPermitInfo.setBorder(b);
		}
		return ivjJPanelPermitInfo;
	}

	/**
	 * This method initializes ivjPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelVehicleInfo()
	{
		if (ivjJPanelVehicleInfo == null)
		{
			ivjJPanelVehicleInfo = new JPanel();
			ivjJPanelVehicleInfo.setName("ivjJPanelVehicleInfo");
			ivjJPanelVehicleInfo.setLayout(null);
			ivjJPanelVehicleInfo.add(gettxtVehModlYr(), null);
			ivjJPanelVehicleInfo.add(gettxtVehMake(), null);
			ivjJPanelVehicleInfo.add(getlblVIN(), null);
			ivjJPanelVehicleInfo.add(getstcLblYearMake(), null);
			ivjJPanelVehicleInfo.add(getstcLblBodyStyle(), null);
			ivjJPanelVehicleInfo.add(getstcLblVIN(), null);
			ivjJPanelVehicleInfo.add(getcomboVehicleMake(), null);
			ivjJPanelVehicleInfo.add(
				getstcLblRegisteredStateCountry(),
				null);
			ivjJPanelVehicleInfo.add(gettxtRegisteredCountry(), null);
			ivjJPanelVehicleInfo.add(getchkRegisteredUSA(), null);

			ivjJPanelVehicleInfo.add(getcomboBodyType(), null);
			ivjJPanelVehicleInfo.add(gettxtRegisteredState(), null);
			ivjJPanelVehicleInfo.add(getstcLblPlateNo(), null);
			ivjJPanelVehicleInfo.add(gettxtPlateNo(), null);

			// defect 10844 
			ivjJPanelVehicleInfo.add(gettxtVIN(), null);
			ivjJPanelVehicleInfo.setBounds(7, 1, 345, 132);
			// end defect 10844 

			Border b =
				new TitledBorder(new EtchedBorder(), "Vehicle Info:");
			ivjJPanelVehicleInfo.setBorder(b);
		}
		return ivjJPanelVehicleInfo;
	}
	/**
	 * This method initializes ivjPanelVehiclePermitInfo
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanelVehiclePermitInfo()
	{
		if (ivjPanelVehiclePermitInfo == null)
		{
			ivjPanelVehiclePermitInfo = new javax.swing.JPanel();
			ivjPanelVehiclePermitInfo.setLayout(null);
			ivjPanelVehiclePermitInfo.add(getJPanelVehicleInfo(), null);
			ivjPanelVehiclePermitInfo.add(getJPanelPermitInfo(), null);
			ivjPanelVehiclePermitInfo.add(getJPanelOTPT(), null);
			ivjPanelVehiclePermitInfo.setBounds(4, 30, 357, 456);
		}
		return ivjPanelVehiclePermitInfo;
	}
	/**
	 * This method initializes ivjlblAmountPaid
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAmountPaid()
	{
		if (ivjlblAmountPaid == null)
		{
			ivjlblAmountPaid = new JLabel();
			ivjlblAmountPaid.setBounds(259, 5, 64, 21);
			ivjlblAmountPaid.setText("$50.00");
		}
		return ivjlblAmountPaid;
	}

	/**
	 * This method initializes ivjlblAuditTrailTransId
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAuditTrailTransId()
	{
		if (ivjlblAuditTrailTransId == null)
		{
			ivjlblAuditTrailTransId = new javax.swing.JLabel();
			ivjlblAuditTrailTransId.setBounds(502, 528, 205, 14);
			ivjlblAuditTrailTransId.setEnabled(false);
		}
		return ivjlblAuditTrailTransId;
	}
	/**
	 * Return the ivjlblExpirationAMPM property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblExpirationAMPM()
	{
		if (ivjlblExpirationAMPM == null)
		{
			try
			{
				ivjlblExpirationAMPM = new JLabel();
				ivjlblExpirationAMPM.setSize(40, 20);
				ivjlblExpirationAMPM.setName("ivjlblExpirationAMPM");
				ivjlblExpirationAMPM.setText(AMPM);
				ivjlblExpirationAMPM.setMaximumSize(
					new java.awt.Dimension(39, 14));
				ivjlblExpirationAMPM.setMinimumSize(
					new java.awt.Dimension(39, 14));
				// user code begin {1}
				ivjlblExpirationAMPM.setLocation(185, 126);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjlblExpirationAMPM;
	}

	/**
	 * Return the ivjlblExpirationDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblExpirationDate()
	{
		if (ivjlblExpirationDate == null)
		{
			try
			{
				ivjlblExpirationDate = new JLabel();
				ivjlblExpirationDate.setSize(67, 21);
				ivjlblExpirationDate.setName("ivjlblExpirationDate");
				ivjlblExpirationDate.setText("");
				ivjlblExpirationDate.setMaximumSize(
					new java.awt.Dimension(42, 14));
				ivjlblExpirationDate.setMinimumSize(
					new java.awt.Dimension(42, 14));
				// user code begin {1}
				ivjlblExpirationDate.setLocation(116, 102);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjlblExpirationDate;
	}

	/**
	 * Return the ivjlblExpirationTime property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblExpirationTime()
	{
		if (ivjlblExpirationTime == null)
		{
			try
			{
				ivjlblExpirationTime = new JLabel();
				ivjlblExpirationTime.setSize(57, 20);
				ivjlblExpirationTime.setName("ivjlblExpirationTime");
				ivjlblExpirationTime.setText("");
				ivjlblExpirationTime.setMaximumSize(
					new java.awt.Dimension(55, 14));
				ivjlblExpirationTime.setMinimumSize(
					new java.awt.Dimension(55, 14));
				// user code begin {1}
				ivjlblExpirationTime.setLocation(116, 126);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjlblExpirationTime;
	}

	/**
	 * This method initializes ivjlblIssueDate
	 * 
	 * @return JLabel
	 */
	private JLabel getlblIssueDate()
	{
		if (ivjlblIssueDate == null)
		{
			ivjlblIssueDate = new JLabel();
			ivjlblIssueDate.setSize(72, 21);
			ivjlblIssueDate.setText("12/30/2010");
			ivjlblIssueDate.setLocation(72, 5);
		}
		return ivjlblIssueDate;
	}

	/**
	 * This method initializes ivjlblIssuedBy
	 * 
	 * @return JLabel
	 */
	private JLabel getlblIssuedBy()
	{
		if (ivjlblIssuedBy == null)
		{
			ivjlblIssuedBy = new JLabel();
			ivjlblIssuedBy.setBounds(72, 25, 279, 21);
			ivjlblIssuedBy.setText("");
		}
		return ivjlblIssuedBy;
	}

	/**
	 * This method initializes ivjstcLblPermitNo
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPermitNo()
	{
		if (ivjlblPermitNo == null)
		{
			ivjlblPermitNo = new JLabel();
			ivjlblPermitNo.setSize(75, 20);
			ivjlblPermitNo.setLocation(116, 23);
			// TODO (KPH) Perhaps different color for Permit No 
			//RGB
			//ivjlblPermitNo.setForeground(
			//new java.awt.Color(255, 0, 0)); // Red
			// new java.awt.Color(0, 255, 0)); // Green
			//new java.awt.Color(0, 0, 255)); // Blue 
			//new java.awt.Color(255, 255, 255));
			//ivjlblPermitNo.setFont(new java.awt.Font("Arial", 1, 14));
			ivjlblPermitNo.setText("");
		}
		return ivjlblPermitNo;
	}
	/**
	 * This method initializes ivjlblPrmtIssuanceId
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPrmtIssuanceId()
	{
		if (ivjlblPrmtIssuanceId == null)
		{
			ivjlblPrmtIssuanceId = new javax.swing.JLabel();
			ivjlblPrmtIssuanceId.setBounds(502, 511, 205, 14);
			ivjlblPrmtIssuanceId.setEnabled(false);
		}
		return ivjlblPrmtIssuanceId;
	}

	/**
	 * Return the ivjlblVIN property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVIN()
	{
		if (ivjlblVIN == null)
		{
			try
			{
				ivjlblVIN = new JLabel();
				ivjlblVIN.setSize(170, 20);
				ivjlblVIN.setName("ivjlblVIN");
				ivjlblVIN.setLocation(81, 79);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjlblVIN;
	}

	/**
	 * This method initializes ivjradioBusiness
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioBusiness()
	{
		if (ivjradioBusiness == null)
		{
			ivjradioBusiness = new JRadioButton();
			ivjradioBusiness.setSize(84, 21);
			ivjradioBusiness.setText("Business");
			ivjradioBusiness.setLocation(58, 21);
			ivjradioBusiness.setMnemonic(KeyEvent.VK_B);
		}
		return ivjradioBusiness;
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
			ivjradioIndividual.setSize(84, 21);
			ivjradioIndividual.setText("Individual");
			ivjradioIndividual.setLocation(148, 21);
			ivjradioIndividual.setMnemonic(KeyEvent.VK_I);
		}
		return ivjradioIndividual;
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
			ivjstcLblAddress.setSize(67, 20);
			ivjstcLblAddress.setText("Address:");
			ivjstcLblAddress.setLocation(22, 10);
			ivjstcLblAddress.setDisplayedMnemonic(KeyEvent.VK_A);
			ivjstcLblAddress.setLabelFor(ivjtxtCustStreet1);
		}
		return ivjstcLblAddress;
	}

	/**
	 * This method initializes ivjstcLblAmountPaid
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAmountPaid()
	{
		if (ivjstcLblAmountPaid == null)
		{
			ivjstcLblAmountPaid = new JLabel();
			ivjstcLblAmountPaid.setBounds(164, 5, 87, 21);
			ivjstcLblAmountPaid.setText("Amount Paid: ");
			ivjstcLblAmountPaid.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblAmountPaid;
	}

	/**
	 * Return the ivjstcLblBodyStyle property value.
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
				ivjstcLblBodyStyle.setLocation(10, 53);
				ivjstcLblBodyStyle.setSize(65, 20);
				ivjstcLblBodyStyle.setText(BODY_STYLE);
				ivjstcLblBodyStyle.setDisplayedMnemonic(KeyEvent.VK_T);
				ivjstcLblBodyStyle.setLabelFor(getcomboBodyType());
				ivjstcLblBodyStyle.setMaximumSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblBodyStyle.setMinimumSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblBodyStyle.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblBodyStyle;
	}

	/**
	 * This method initializes ivjstcLblFirstName
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCustFirstName()
	{
		if (ivjstcLblFirstName == null)
		{
			ivjstcLblFirstName = new JLabel();
			ivjstcLblFirstName.setBounds(10, 21, 96, 20);
			ivjstcLblFirstName.setText("First Name:");
			ivjstcLblFirstName.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblFirstName.setDisplayedMnemonic(KeyEvent.VK_F);
			ivjstcLblFirstName.setLabelFor(ivjtxtCustFirstName);

		}
		return ivjstcLblFirstName;
	}

	/**
	 * This method initializes ivjstcLblLastName
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCustLastName()
	{
		if (ivjstcLblLastName == null)
		{
			ivjstcLblLastName = new JLabel();
			ivjstcLblLastName.setSize(95, 20);
			ivjstcLblLastName.setText("Last Name:");
			ivjstcLblLastName.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblLastName.setLocation(11, 73);
			ivjstcLblLastName.setDisplayedMnemonic(KeyEvent.VK_L);
			ivjstcLblLastName.setLabelFor(ivjtxtCustLastName);
		}
		return ivjstcLblLastName;
	}

	/**
	 * This method initializes ivjstcLblMiddleInitial
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCustMI()
	{
		if (ivjstcLblMiddleInitial == null)
		{
			ivjstcLblMiddleInitial = new JLabel();
			ivjstcLblMiddleInitial.setSize(89, 20);
			ivjstcLblMiddleInitial.setText("Middle Initial:");
			ivjstcLblMiddleInitial.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblMiddleInitial.setLocation(17, 47);
			ivjstcLblMiddleInitial.setDisplayedMnemonic(KeyEvent.VK_M);
			ivjstcLblMiddleInitial.setLabelFor(gettxtCustMI());
		}
		return ivjstcLblMiddleInitial;
	}

	/**
	 * Return the ivjstcLblDash property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDash()
	{
		if (ivjstcLblDash == null)
		{
			try
			{
				ivjstcLblDash = new JLabel();
				ivjstcLblDash.setBounds(225, 1, 8, 17);
				ivjstcLblDash.setName("ivjstcLblDash");
				ivjstcLblDash.setText(DASH);
				ivjstcLblDash.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setMinimumSize(
					new java.awt.Dimension(4, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblDash;
	}

	/**
	 * This method initializes ivjstcLblDestn
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDestn()
	{
		if (ivjstcLblDestn == null)
		{
			ivjstcLblDestn = new javax.swing.JLabel();
			ivjstcLblDestn.setText("Destination:");
			ivjstcLblDestn.setHorizontalAlignment(SwingConstants.RIGHT);
			ivjstcLblDestn.setDisplayedMnemonic(KeyEvent.VK_D);
			ivjstcLblDestn.setLabelFor(ivjtxtDestn);
			ivjstcLblDestn.setSize(67, 20);
			ivjstcLblDestn.setLocation(23, 118);
		}
		return ivjstcLblDestn;
	}

	/**
	 * Return the ivjstcLblEffectiveDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEffectiveDate()
	{
		if (ivjstcLblEffectiveDate == null)
		{
			try
			{
				ivjstcLblEffectiveDate = new JLabel();
				ivjstcLblEffectiveDate.setSize(83, 20);
				ivjstcLblEffectiveDate.setName(
					"ivjstcLblEffectiveDate");
				ivjstcLblEffectiveDate.setText(EFF_DATE);
				ivjstcLblEffectiveDate.setMaximumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEffectiveDate.setMinimumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEffectiveDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				ivjstcLblEffectiveDate.setLocation(26, 49);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblEffectiveDate;
	}

	/**
	 * Return the ivjstcLblEffectiveTime property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEffectiveTime()
	{
		if (ivjstcLblEffectiveTime == null)
		{
			try
			{
				ivjstcLblEffectiveTime = new JLabel();
				ivjstcLblEffectiveTime.setSize(86, 20);
				ivjstcLblEffectiveTime.setName(
					"ivjstcLblEffectiveTime");
				ivjstcLblEffectiveTime.setText(EFF_TIME);
				ivjstcLblEffectiveTime.setMaximumSize(
					new java.awt.Dimension(83, 14));
				ivjstcLblEffectiveTime.setMinimumSize(
					new java.awt.Dimension(83, 14));
				ivjstcLblEffectiveTime.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				ivjstcLblEffectiveTime.setLocation(23, 74);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblEffectiveTime;
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
			ivjstcLblEMail.setSize(37, 20);
			ivjstcLblEMail.setText("E-Mail:");
			ivjstcLblEMail.setHorizontalAlignment(SwingConstants.RIGHT);
			ivjstcLblEMail.setLocation(30, 25);
			ivjstcLblEMail.setDisplayedMnemonic(KeyEvent.VK_E);
			ivjstcLblEMail.setLabelFor(gettxtEMail());
		}
		return ivjstcLblEMail;
	}

	/**
	 * Return the ivjstcLblExpirationDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblExpirationDate()
	{
		if (ivjstcLblExpirationDate == null)
		{
			try
			{
				ivjstcLblExpirationDate = new JLabel();
				ivjstcLblExpirationDate.setSize(93, 21);
				ivjstcLblExpirationDate.setName(
					"ivjstcLblExpirationDate");
				ivjstcLblExpirationDate.setText(EXP_DATE);
				ivjstcLblExpirationDate.setMaximumSize(
					new java.awt.Dimension(89, 14));
				ivjstcLblExpirationDate.setMinimumSize(
					new java.awt.Dimension(89, 14));
				ivjstcLblExpirationDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				ivjstcLblExpirationDate.setLocation(16, 102);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblExpirationDate;
	}

	/**
	 * Return the ivjstcLblExpirationTime property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblExpirationTime()
	{
		if (ivjstcLblExpirationTime == null)
		{
			try
			{
				ivjstcLblExpirationTime = new JLabel();
				ivjstcLblExpirationTime.setSize(93, 20);
				ivjstcLblExpirationTime.setName(
					"ivjstcLblExpirationTime");
				ivjstcLblExpirationTime.setText(EXP_TIME);
				ivjstcLblExpirationTime.setMaximumSize(
					new java.awt.Dimension(91, 14));
				ivjstcLblExpirationTime.setMinimumSize(
					new java.awt.Dimension(91, 14));
				ivjstcLblExpirationTime.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				ivjstcLblExpirationTime.setLocation(16, 126);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblExpirationTime;
	}

	/**
	 * This method initializes ivjstcLblIntPt1
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblIntPt1()
	{
		if (ivjstcLblIntPt1 == null)
		{
			ivjstcLblIntPt1 = new javax.swing.JLabel();
			ivjstcLblIntPt1.setSize(75, 19);
			ivjstcLblIntPt1.setText("Intermediate:");
			ivjstcLblIntPt1.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblIntPt1.setLocation(15, 47);
		}
		return ivjstcLblIntPt1;
	}

	/**
	 * This method initializes ivjstcLblIntPt2
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblIntPt2()
	{
		if (ivjstcLblIntPt2 == null)
		{
			ivjstcLblIntPt2 = new JLabel();
			ivjstcLblIntPt2.setBounds(15, 71, 75, 19);
			ivjstcLblIntPt2.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblIntPt2;
	}

	/**
	 * This method initializes ivjstcLblIntPt3
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblIntPt3()
	{
		if (ivjstcLblIntPt3 == null)
		{
			ivjstcLblIntPt3 = new javax.swing.JLabel();
			ivjstcLblIntPt3.setBounds(15, 95, 75, 19);
			ivjstcLblIntPt3.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblIntPt3;
	}
	/**
	 * This method initializes ivjstcLblIssued
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblIssued()
	{
		if (ivjstcLblIssued == null)
		{
			ivjstcLblIssued = new JLabel();
			ivjstcLblIssued.setSize(51, 21);
			ivjstcLblIssued.setText("Issued:");
			ivjstcLblIssued.setLocation(14, 5);
			ivjstcLblIssued.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblIssued;
	}

	/**
	 * This method initializes ivjstcLblIssuedBy
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblIssuedBy()
	{
		if (ivjstcLblIssuedBy == null)
		{
			ivjstcLblIssuedBy = new JLabel();
			ivjstcLblIssuedBy.setSize(64, 21);
			ivjstcLblIssuedBy.setText("Issued By:");
			ivjstcLblIssuedBy.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblIssuedBy.setLocation(1, 25);
		}
		return ivjstcLblIssuedBy;
	}

	/**
	 * This method initializes ivstcLblOrigin
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOrigin()
	{
		if (ivjstcLblOrigin == null)
		{
			ivjstcLblOrigin = new javax.swing.JLabel();
			ivjstcLblOrigin.setSize(73, 20);
			ivjstcLblOrigin.setText("Origination:");
			ivjstcLblOrigin.setLocation(17, 22);
			ivjstcLblOrigin.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblOrigin.setDisplayedMnemonic(KeyEvent.VK_O);
			ivjstcLblOrigin.setLabelFor(ivjtxtOrigin);
		}
		return ivjstcLblOrigin;
	}

	/**
	 * This method initializes ivjlblPermitNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPermitNo()
	{
		if (ivjstclblPermitNox == null)
		{
			ivjstclblPermitNox = new JLabel();
			ivjstclblPermitNox.setSize(63, 20);
			ivjstclblPermitNox.setText("Permit No:");
			ivjstclblPermitNox.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstclblPermitNox.setLocation(46, 23);
		}
		return ivjstclblPermitNox;
	}

	/**
	 * Return the ivjstcLblPermitTitle property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPermitTitle()
	{
		if (ivjstcLblPermitTitle == null)
		{
			try
			{
				ivjstcLblPermitTitle = new JLabel();
				ivjstcLblPermitTitle.setSize(262, 14);
				ivjstcLblPermitTitle.setName("ivjstcLblPermitTitle");
				ivjstcLblPermitTitle.setText(STATIC_144_HOUR);
				ivjstcLblPermitTitle.setMaximumSize(
					new java.awt.Dimension(86, 14));
				ivjstcLblPermitTitle.setMinimumSize(
					new java.awt.Dimension(86, 14));
				ivjstcLblPermitTitle.setHorizontalAlignment(
					SwingConstants.CENTER);
				// user code begin {1}
				ivjstcLblPermitTitle.setLocation(216, 12);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblPermitTitle;
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
			ivjstcLblPhoneNo.setLocation(10, 49);
			ivjstcLblPhoneNo.setDisplayedMnemonic(KeyEvent.VK_P);
			ivjstcLblPhoneNo.setLabelFor(gettxtPhoneNo());
		}
		return ivjstcLblPhoneNo;
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
			ivjstcLblPlateNo = new javax.swing.JLabel();
			ivjstcLblPlateNo.setSize(57, 20);
			ivjstcLblPlateNo.setText("Plate No:");
			ivjstcLblPlateNo.setLocation(18, 105);
			ivjstcLblPlateNo.setHorizontalAlignment(
				SwingConstants.RIGHT);
			ivjstcLblPlateNo.setDisplayedMnemonic(KeyEvent.VK_N);
			ivjstcLblPlateNo.setLabelFor(gettxtPlateNo());
		}
		return ivjstcLblPlateNo;
	}

	/**
	 * This method initializes ivjstcLblIssuedBy
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegisteredStateCountry()
	{
		if (ivjstcLblRegisteredStateCountry == null)
		{
			ivjstcLblRegisteredStateCountry = new JLabel();
			ivjstcLblRegisteredStateCountry.setSize(69, 20);
			ivjstcLblRegisteredStateCountry.setText("State/Cntry: ");
			ivjstcLblRegisteredStateCountry.setLocation(183, 105);
			ivjstcLblRegisteredStateCountry.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblRegisteredStateCountry;
	}

	/**
	 * Return the ivjstcLblVIN property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVIN()
	{
		if (ivjstcLblVIN == null)
		{
			try
			{
				ivjstcLblVIN = new JLabel();
				ivjstcLblVIN.setBounds(51, 79, 24, 20);
				ivjstcLblVIN.setName("ivjstcLblVIN");
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setMinimumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblVIN;
	}

	/**
	 * Return the ivjstcLblYearMake property value.
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
				ivjstcLblYearMake.setSize(68, 20);
				ivjstcLblYearMake.setName("ivjstcLblYearMake");
				ivjstcLblYearMake.setText(YEAR_MAKE);
				ivjstcLblYearMake.setDisplayedMnemonic(KeyEvent.VK_Y);
				ivjstcLblYearMake.setLabelFor(ivjtxtVehModlYr);
				ivjstcLblYearMake.setMaximumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYearMake.setMinimumSize(
					new java.awt.Dimension(63, 14));
				// user code begin {1}
				ivjstcLblYearMake.setLocation(7, 27);
				ivjstcLblYearMake.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblYearMake;
	}

	/**
	 * This method initializes ivjtxtBusinessName
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtBusinessName()
	{
		if (ivjtxtBusinessName == null)
		{
			ivjtxtBusinessName = new RTSInputField();
			ivjtxtBusinessName.setSize(329, 20);
			ivjtxtBusinessName.setLocation(12, 22);
			ivjtxtBusinessName.setMaxLength(
				CommonConstant.LENGTH_BUSINESS_NAME);
		}
		return ivjtxtBusinessName;
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
				ivjtxtCustCity.setBounds(4, 1, 142, 20);
				ivjtxtCustCity.setName("ivjtxtCustCity");
				ivjtxtCustCity.setManagingFocus(false);
				ivjtxtCustCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustCity.setText(DFLT_CITY);
				// user code begin {1}
				ivjtxtCustCity.setInput(RTSInputField.DEFAULT);
				ivjtxtCustCity.setMaxLength(CommonConstant.LENGTH_CITY);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtCustCity;
	}

	/**
	 * Return the ivjtxtCustCntry property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustCntry()
	{
		if (ivjtxtCustCntry == null)
		{
			try
			{
				ivjtxtCustCntry = new RTSInputField();
				ivjtxtCustCntry.setBounds(151, 1, 40, 20);
				ivjtxtCustCntry.setName("ivjtxtCustCntry");
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
				// user code begin {1}
				ivjtxtCustCntry.setInput(RTSInputField.DEFAULT);
				ivjtxtCustCntry.setMaxLength(
					CommonConstant.LENGTH_CNTRY);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtCustCntryZpcd.setBounds(196, 1, 84, 20);
				ivjtxtCustCntryZpcd.setName("ivjtxtCustCntryZpcd");
				ivjtxtCustCntryZpcd.setManagingFocus(false);
				ivjtxtCustCntryZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustCntryZpcd.setVisible(true);
				// user code begin {1}
				ivjtxtCustCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtCustCntryZpcd.setMaxLength(
					CommonConstant.LENGTH_CNTRY_ZIP);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtCustCntryZpcd;
	}

	/**
	 * Return the ivjtxtCustFirstName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustFirstName()
	{
		if (ivjtxtCustFirstName == null)
		{
			try
			{
				ivjtxtCustFirstName = new RTSInputField();
				ivjtxtCustFirstName.setSize(219, 20);
				ivjtxtCustFirstName.setName("ivjtxtCustFirstName");
				ivjtxtCustFirstName.setManagingFocus(false);
				ivjtxtCustFirstName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustFirstName.setInput(RTSInputField.DEFAULT);
				ivjtxtCustFirstName.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// user code end
				ivjtxtCustFirstName.setLocation(113, 21);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtCustFirstName;
	}

	/**
	 * Return the ivjtxtCustLastName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustLastName()
	{
		if (ivjtxtCustLastName == null)
		{
			try
			{
				ivjtxtCustLastName = new RTSInputField();
				ivjtxtCustLastName.setSize(219, 20);
				ivjtxtCustLastName.setName("ivjtxtCustLastName");
				ivjtxtCustLastName.setManagingFocus(false);
				ivjtxtCustLastName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtCustLastName.setInput(RTSInputField.DEFAULT);
				ivjtxtCustLastName.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// user code end
				ivjtxtCustLastName.setLocation(113, 73);
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtCustLastName;
	}

	/**
	 * This method initializes ivjtxtCustMI
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustMI()
	{
		if (ivjtxtCustMI == null)
		{
			ivjtxtCustMI = new RTSInputField();
			ivjtxtCustMI.setSize(25, 20);
			ivjtxtCustMI.setLocation(113, 47);
			ivjtxtCustMI.setMaxLength(CommonConstant.LENGTH_MI_NAME);
		}
		return ivjtxtCustMI;
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
				ivjtxtCustState.setBounds(151, 1, 25, 20);
				ivjtxtCustState.setName("ivjtxtCustState");
				ivjtxtCustState.setManagingFocus(false);
				ivjtxtCustState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustState.setText(DFLT_STATE);
				// user code begin {1}
				ivjtxtCustState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtCustState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtCustState;
	}

	/**
	 * Return the ivjtxtCustStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCustStreet1()
	{
		if (ivjtxtCustStreet1 == null)
		{
			try
			{
				ivjtxtCustStreet1 = new RTSInputField();
				ivjtxtCustStreet1.setSize(265, 19);
				ivjtxtCustStreet1.setName("ivjtxtCustStreet1");
				ivjtxtCustStreet1.setManagingFocus(false);
				ivjtxtCustStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCustStreet1.setText(DFLT_ADDR);
				ivjtxtCustStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtCustStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code begin {1}
				ivjtxtCustStreet1.setLocation(20, 34);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtCustStreet2.setSize(265, 19);
				ivjtxtCustStreet2.setName("ivjtxtCustStreet2");
				ivjtxtCustStreet2.setManagingFocus(false);
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
				ivjtxtCustStreet2.setLocation(20, 60);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtCustStreet2;
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
				ivjtxtCustZpcd.setBounds(181, 1, 41, 20);
				ivjtxtCustZpcd.setName("ivjtxtCustZpcd");
				ivjtxtCustZpcd.setManagingFocus(false);
				ivjtxtCustZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtCustZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtCustZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
				ivjtxtCustZpcdP4.setBounds(234, 1, 35, 20);
				ivjtxtCustZpcdP4.setName("ivjtxtCustZpcdP4");
				ivjtxtCustZpcdP4.setManagingFocus(false);
				ivjtxtCustZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtCustZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtCustZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtCustZpcdP4;
	}

	/**
	 * This method initializes ivjtxtDestn
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtDestn()
	{
		if (ivjtxtDestn == null)
		{
			ivjtxtDestn = new RTSInputField();
			ivjtxtDestn.setSize(235, 20);
			ivjtxtDestn.setLocation(96, 118);
			ivjtxtDestn.setMaxLength(CommonConstant.LENGTH_OTPT_PT);
		}
		return ivjtxtDestn;
	}

	/**
	 * Return the ivjtxtEffectiveDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtEffectiveDate()
	{
		if (ivjtxtEffectiveDate == null)
		{
			try
			{
				ivjtxtEffectiveDate = new RTSDateField();
				ivjtxtEffectiveDate.setSize(75, 20);
				ivjtxtEffectiveDate.setName("ivjtxtEffectiveDate");
				ivjtxtEffectiveDate.setMonthYrOnly(false);
				ivjtxtEffectiveDate.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEffectiveDate.setText(" ");
				ivjtxtEffectiveDate.setColumns(10);
				// user code begin {1}
				ivjtxtEffectiveDate.setLocation(116, 49);
				ivjtxtEffectiveDate.setFocusTraversalKeysEnabled(false);
				// defect 10491 
				//ivjtxtEffectiveDate.addFocusListener(this);
				ivjtxtEffectiveDate.setFocusTraversalKeysEnabled(false);
				// end defect 10491 
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtEffectiveDate;
	}

	/**
	 * Return the ivjtxtEffectiveTime property value.
	 * 
	 * @return RTSTimeField
	 */
	private RTSTimeField gettxtEffectiveTime()
	{
		if (ivjtxtEffectiveTime == null)
		{
			try
			{
				ivjtxtEffectiveTime = new RTSTimeField();
				ivjtxtEffectiveTime.setSize(62, 20);
				ivjtxtEffectiveTime.setName("ivjtxtEffectiveTime");
				ivjtxtEffectiveTime.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEffectiveTime.setText("");
				ivjtxtEffectiveTime.setTime(DFLT_TIME);
				ivjtxtEffectiveTime.setColumns(8);
				// user code begin {1}
				ivjtxtEffectiveTime.setLocation(116, 74);
				// defect 10491 
				// ivjtxtEffectiveTime.addFocusListener(this);
				ivjtxtEffectiveTime.setFocusTraversalKeysEnabled(false);
				// end defect 10491
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtEffectiveTime;
	}

	/**
	 * This method initializes ivjtxtEMail
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtEMail()
	{
		if (ivjtxtEMail == null)
		{
			ivjtxtEMail = new RTSInputField();
			ivjtxtEMail.setSize(266, 20);
			ivjtxtEMail.setLocation(74, 25);
			ivjtxtEMail.setMaxLength(CommonConstant.LENGTH_EMAIL);
		}
		return ivjtxtEMail;
	}

	/**
	 * This method initializes ivjtxtIntPt1
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtIntPt1()
	{
		if (ivjtxtIntPt1 == null)
		{
			ivjtxtIntPt1 = new RTSInputField();
			ivjtxtIntPt1.setSize(235, 20);
			ivjtxtIntPt1.setLocation(96, 46);
			ivjtxtIntPt1.setMaxLength(CommonConstant.LENGTH_OTPT_PT);
		}
		return ivjtxtIntPt1;
	}

	/**
	 * This method initializes ivjtxtIntPt2
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtIntPt2()
	{
		if (ivjtxtIntPt2 == null)
		{
			ivjtxtIntPt2 = new RTSInputField();
			ivjtxtIntPt2.setSize(235, 20);
			ivjtxtIntPt2.setLocation(96, 70);
			ivjtxtIntPt2.setMaxLength(CommonConstant.LENGTH_OTPT_PT);
		}
		return ivjtxtIntPt2;
	}

	/**
	 * This method initializes ivjtxtIntPt3
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtIntPt3()
	{
		if (ivjtxtIntPt3 == null)
		{
			ivjtxtIntPt3 = new RTSInputField();
			ivjtxtIntPt3.setSize(235, 20);
			ivjtxtIntPt3.setLocation(96, 94);
			ivjtxtIntPt3.setMaxLength(CommonConstant.LENGTH_OTPT_PT);
		}
		return ivjtxtIntPt3;
	}

	/**
	 * This method initializes ivjtxtOrigin
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOrigin()
	{
		if (ivjtxtOrigin == null)
		{
			ivjtxtOrigin = new RTSInputField();
			ivjtxtOrigin.setSize(235, 20);
			ivjtxtOrigin.setLocation(96, 22);
			ivjtxtOrigin.setMaxLength(CommonConstant.LENGTH_OTPT_PT);
		}
		return ivjtxtOrigin;
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
			ivjtxtPhoneNo.setBounds(74, 49, 86, 20);
		}
		return ivjtxtPhoneNo;
	}

	/**
	 * This method initializes ivjtxtPlateNo
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtPlateNo()
	{
		if (ivjtxtPlateNo == null)
		{
			ivjtxtPlateNo = new RTSInputField();
			ivjtxtPlateNo.setSize(87, 20);
			ivjtxtPlateNo.setLocation(81, 105);
			ivjtxtPlateNo.setMaxLength(15);
			ivjtxtPlateNo.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
		}
		return ivjtxtPlateNo;
	}

	/**
	 * This method initializes ivjtxtRegisteredCountry
	 * 
	 * @return JTextField
	 */
	private RTSInputField gettxtRegisteredCountry()
	{
		if (ivjtxtRegisteredCountry == null)
		{
			ivjtxtRegisteredCountry = new RTSInputField();
			ivjtxtRegisteredCountry.setSize(39, 20);
			ivjtxtRegisteredCountry.setLocation(254, 105);
			ivjtxtRegisteredCountry.setMaxLength(
				CommonConstant.LENGTH_CNTRY);
		}
		return ivjtxtRegisteredCountry;
	}

	/**
	 * This method initializes ivjtxtRegisteredState
	 * 
	 * @return JTextField
	 */
	private RTSInputField gettxtRegisteredState()
	{
		if (ivjtxtRegisteredState == null)
		{
			ivjtxtRegisteredState = new RTSInputField();
			ivjtxtRegisteredState.setSize(25, 20);
			ivjtxtRegisteredState.setMaxLength(
				CommonConstant.LENGTH_STATE);
			ivjtxtRegisteredState.setLocation(254, 105);
		}
		return ivjtxtRegisteredState;
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
				ivjtxtVehMake.setSize(45, 20);
				ivjtxtVehMake.setName("ivjtxtVehMake");
				ivjtxtVehMake.setInput(6);
				ivjtxtVehMake.setManagingFocus(false);
				ivjtxtVehMake.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehMake.setMaxLength(4);
				// user code begin {1}
				ivjtxtVehMake.setLocation(134, 27);
				ivjtxtVehMake.setVisible(false);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtVehMake;
	}

	/**
	 * Return the ivjtxtVehModlYr property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehModlYr()
	{
		if (ivjtxtVehModlYr == null)
		{
			try
			{
				ivjtxtVehModlYr = new RTSInputField();
				ivjtxtVehModlYr.setBounds(81, 27, 39, 20);
				ivjtxtVehModlYr.setName("ivjtxtVehModlYr");
				ivjtxtVehModlYr.setInput(1);
				ivjtxtVehModlYr.setManagingFocus(false);
				ivjtxtVehModlYr.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehModlYr.setMaxLength(
					CommonConstant.LENGTH_YEAR);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtVehModlYr;
	}
	/**
	 * This method initializes ivjtxtVIN
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVIN()
	{
		if (ivjtxtVIN == null)
		{
			ivjtxtVIN = new RTSInputField();
			ivjtxtVIN.setBounds(81, 79, 170, 20);
			ivjtxtVIN.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
			ivjtxtVIN.setMaxLength(22);
		}
		return ivjtxtVIN;
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
	 * Converts military time to regular time.
	 * 
	 * @param aaRTSDate RTSDate
	 * @param abSetAMPMCombo 
	 */
	private String handleMilitaryTime(
		RTSDate aaRTSDate,
		boolean abSetAMPMCombo)
	{
		String lsTime;

		if (aaRTSDate.getHour() > 12)
		{
			lsTime = aaRTSDate.getClockTime();
			if ((aaRTSDate.getHour() - 12) < 10)
			{
				lsTime =
					ZERO
						+ Integer.toString(aaRTSDate.getHour() - 12)
						+ lsTime.substring(2, lsTime.length() - 1);
			}
			else
			{
				lsTime =
					Integer.toString(aaRTSDate.getHour() - 12)
						+ lsTime.substring(2, lsTime.length() - 1);
			}
			if (abSetAMPMCombo)
			{
				getcomboEffectiveAMPM().setSelectedItem(PM);
			}
			return lsTime;
		}
		else
		{
			return aaRTSDate.getClockTime();
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
			// user code end
			setName("FrmTimedPermitMRG006");
			setRequestFocus(false);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(727, 573);
			setTitle(TITLE_MRG006);
			setContentPane(getFrmTimedPermitMRG006ContentPane1());

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

			loadOTPTComponents();

			cvMFValid.add(gettxtPlateNo());
			cvMFValid.add(gettxtBusinessName());
			cvMFValid.add(gettxtCustFirstName());
			cvMFValid.add(gettxtCustMI());
			cvMFValid.add(gettxtCustLastName());
			cvMFValid.add(gettxtVehMake());
			cvMFValid.add(gettxtRegisteredState());
			cvMFValid.add(gettxtRegisteredCountry());
			cvMFValid.add(gettxtOrigin());
			cvMFValid.add(gettxtIntPt1());
			cvMFValid.add(gettxtIntPt2());
			cvMFValid.add(gettxtIntPt3());
			cvMFValid.add(gettxtDestn());
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
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
		restoreComboColor();

		if (aaIE.getSource() == ivjchkUSA)
		{
			caNameAddrComp.resetPerUSA(
				aaIE.getStateChange() == ItemEvent.SELECTED);
		}
		else if (aaIE.getSource() == ivjchkRegisteredUSA)
		{
			boolean lbUSA = getchkRegisteredUSA().isSelected();
			gettxtRegisteredCountry().setEnabled(!lbUSA);
			gettxtRegisteredCountry().setVisible(!lbUSA);
			gettxtRegisteredCountry().setText("");
			gettxtRegisteredState().setEnabled(lbUSA);
			gettxtRegisteredState().setVisible(lbUSA);
			gettxtRegisteredState().setText(
				lbUSA ? CommonConstant.STR_TX : "");
			gettxtRegisteredState().setText("");
			if (lbUSA)
			{
				gettxtRegisteredState().requestFocus();
			}
			else
			{
				gettxtRegisteredCountry().requestFocus();
			}
		}
		else if (aaIE.getSource() == getcomboVehicleMake())
		{
			String lsSelected =
				(String) getcomboVehicleMake().getSelectedItem();

			if (lsSelected != null && lsSelected.equals(NEW))
			{
				getcomboVehicleMake().setSelectedIndex(-1);
				getcomboVehicleMake().setVisible(false);
				getcomboVehicleMake().setEnabled(false);
				gettxtVehMake().setVisible(true);
				gettxtVehMake().setEnabled(true);
				gettxtVehMake().requestFocus();
			}
		}
		else if (
			aaIE.getSource() == getradioBusiness()
				&& getradioBusiness().isSelected())
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
			gettxtBusinessName().setEnabled(true);
		}
		else if (
			aaIE.getSource() == ivjradioIndividual
				&& getradioIndividual().isSelected())
		{
			gettxtCustFirstName().setEnabled(true);
			gettxtCustMI().setEnabled(true);
			gettxtCustLastName().setEnabled(true);
			getstcLblCustFirstName().setEnabled(true);
			getstcLblCustMI().setEnabled(true);
			getstcLblCustLastName().setEnabled(true);
			gettxtBusinessName().setEnabled(false);
			gettxtBusinessName().setText("");
		}
	}

	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		if (aaKE.getSource() == gettxtEffectiveDate()
			|| aaKE.getSource() == gettxtEffectiveTime()
			|| aaKE.getSource() == gettxtPhoneNo())
		{
			char lchKey = aaKE.getKeyChar();
			if (lchKey == KeyEvent.VK_ENTER)
			{
				clearAllColor(this);
				processEnter();
			}
			else
			{
				if (aaKE.getSource() == gettxtEffectiveDate()
					&& aaKE.getKeyCode() == KeyEvent.VK_TAB)
				{
					clearAllColor(this);
					RTSException leRTSEx = new RTSException();
					validateEffDt(leRTSEx);
					if (leRTSEx.isValidationError())
					{
						leRTSEx.displayError(this);
						leRTSEx.getFirstComponent().requestFocus();
					}
					else
					{
						if (aaKE.getModifiers() == KeyEvent.SHIFT_MASK)
						{
							getchkRegisteredUSA().requestFocus();
						}
						else
						{
							gettxtEffectiveTime().requestFocus();
						}
					}
				}
				else if (
					aaKE.getSource() == gettxtEffectiveTime()
						&& aaKE.getKeyCode() == KeyEvent.VK_TAB)
				{
					clearAllColor(this);
					RTSException leRTSEx = new RTSException();
					validateEffTime(leRTSEx);
					if (leRTSEx.isValidationError())
					{
						leRTSEx.displayError(this);
						leRTSEx.getFirstComponent().requestFocus();
					}
					else
					{
						if (aaKE.getModifiers() == KeyEvent.SHIFT_MASK)
						{
							gettxtEffectiveDate().requestFocus();
						}
						else
						{
							getcomboEffectiveAMPM().requestFocus();
						}
					}
				}
			}
		}
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
	}

	/**
	 * Load vector of OTPT components
	 * 
	 */
	private void loadOTPTComponents()
	{
		cvOTPTComponents.add(ivjtxtOrigin);
		cvOTPTComponents.add(ivjtxtIntPt1);
		cvOTPTComponents.add(ivjtxtIntPt2);
		cvOTPTComponents.add(ivjtxtIntPt3);
		cvOTPTComponents.add(ivjtxtDestn);
		cvOTPTComponents.add(ivjstcLblOrigin);
		cvOTPTComponents.add(ivjstcLblIntPt1);
		cvOTPTComponents.add(ivjstcLblIntPt2);
		cvOTPTComponents.add(ivjstcLblIntPt3);
		cvOTPTComponents.add(ivjstcLblDestn);
	}

	/**
	 * Fills the AM / PM Combo Box 
	 */
	private void populateAmPmCombo()
	{
		if (getcomboEffectiveAMPM().getItemCount() == 0)
		{
			getcomboEffectiveAMPM().addItem(AM);
			getcomboEffectiveAMPM().addItem(PM);
		}
	}

	/**
	 * Populates effective and expiration dates and times.
	 * 
	 */
	private void populateDatesAndTimes()
	{
		String lsTime = "";

		// populate effective date
		gettxtEffectiveDate().setDate(caPrmtData.getRTSDateEffDt());

		// populate effective time
		if (caPrmtData.getRTSDateEffDt().getHour() >= NOON_HOUR)
		{
			lsTime =
				(handleMilitaryTime(caPrmtData.getRTSDateEffDt(), true)
					.substring(0, END_OF_SECOND_INDEX));

			getcomboEffectiveAMPM().setSelectedItem(PM);
		}
		else
		{
			lsTime = caPrmtData.getRTSDateEffDt().getClockTimeNoMs();

			// if 0 hour display it as 12
			if (lsTime.substring(0, 2).equals(ZEROZERO))
			{
				String lsHour = TWELVE;
				String lsRest = lsTime.substring(2);
				lsTime = lsHour + lsRest;
			}
			getcomboEffectiveAMPM().setSelectedItem(AM);
		}
		gettxtEffectiveTime().setTime(lsTime);

		// populate expiration date
		getlblExpirationDate().setText(
			caPrmtData.getRTSDateExpDt().toString());

		// populate expiration time
		if (caPrmtData.getRTSDateExpDt().getHour() >= NOON_HOUR)
		{
			lsTime =
				(handleMilitaryTime(caPrmtData.getRTSDateExpDt(),
					false)
					.substring(0, END_OF_SECOND_INDEX));

			getlblExpirationAMPM().setText(PM);
		}
		else
		{
			lsTime = caPrmtData.getRTSDateExpDt().getClockTimeNoMs();

			// if 0 hour display it as 12
			if (lsTime.substring(0, 2).equals(ZEROZERO))
			{
				String lsHour = TWELVE;
				String lsRest = lsTime.substring(2);
				lsTime = lsHour + lsRest;
			}
			getlblExpirationAMPM().setText(AM);
		}
		getlblExpirationTime().setText(lsTime);
	}

	/**
	 * Process Enter
	 * 
	 */
	private void processEnter()
	{
		if (csTransCd.equals(MiscellaneousRegConstant.PRMINQ))
		{
			getController().processData(
				AbstractViewController.FINAL,
				caPrmtData);
		}
		else
		{
			int liFcnId =
				SystemProperty.isHQ()
					? VCTimedPermitMRG006.ADD_TRANS
					: AbstractViewController.ENTER;

			if (caPrmtData.isPrmDupTrans() && confirmForAutoTrans())
			{
				getController().processData(
					liFcnId,
					MiscellaneousRegClientUtilityMethods.prepFees(
						caPrmtData,
						setMFVehForFees()));
			}
			else if (validateData() && confirmForAutoTrans())
			{
				setDataToDataObject();

				getController().processData(
					liFcnId,
					MiscellaneousRegClientUtilityMethods.prepFees(
						caPrmtData,
						setMFVehForFees()));
			}
		}
	}

	/**
	 * Release Virtual Inventory
	 */
	private void releaseVirtualInventoryIfHeld()
	{
		if (caPrmtData.getVIAllocData() != null)
		{
			if (SystemProperty.isDevStatus())
			{
				System.out.println(
					"Releasing "
						+ caPrmtData.getVIAllocData().getInvItmNo());
			}
			getController().processData(
				InventoryConstant.INV_VI_RELEASE_HOLD,
				caPrmtData);
		}
	}

	/**
	 *  Restore Color of Combo Boxes in case of prior error
	 */
	private void restoreComboColor()
	{
		getcomboBodyType().setForeground(Color.black);
		getcomboBodyType().setBackground(new Color(204, 204, 204));
		getcomboVehicleMake().setForeground(Color.black);
		getcomboVehicleMake().setBackground(new Color(204, 204, 204));
	}

	/** 
	 * Save Screen Data for later Display 
	 */
	private void saveScreenMRG006Data()
	{
		ScreenMRG006SavedData laMRG006Data =
			new ScreenMRG006SavedData();
		laMRG006Data.setPrmtData(
			(PermitData) UtilityMethods.copy(caPrmtData));
		getController().getMediator().closeVault(
			ScreenConstant.MRG006,
			UtilityMethods.copy(laMRG006Data));
	}

	/**
	 * Populates the body style combo box.
	 */
	private void setBodyTypeToDisplay()
	{
		// Verify MF/VINA VehBdyTypes	
		if (getcomboBodyType().isEnabled())
		{
			String lsBdyType = caPrmtData.getVehBdyType();
			int liBdyDefault = -1;
			Vector lvVehBdyTpye =
				VehicleBodyTypesCache.getVehBdyTypesVec();
			UtilityMethods.sort(lvVehBdyTpye);
			Vector lvComboValues = new Vector();
			if (lvVehBdyTpye != null)
			{
				for (int i = 0; i < lvVehBdyTpye.size(); i++)
				{
					VehicleBodyTypesData laVehBdyType =
						(VehicleBodyTypesData) lvVehBdyTpye.get(i);
					if (lsBdyType != null
						&& laVehBdyType.getVehBdyType().equals(lsBdyType)
						&& liBdyDefault == -1)
					{
						liBdyDefault = i;
					}
					String lsDesc =
						laVehBdyType.getVehBdyType()
							+ " - "
							+ laVehBdyType.getVehBdyTypeDesc();
					lvComboValues.add(lsDesc);
				}
			}
			if (cbINQ && liBdyDefault == -1 && lsBdyType != null)
			{
				lvComboValues.add(lsBdyType);
				liBdyDefault = lvComboValues.size() - 1;
			}

			DefaultComboBoxModel laDCBM =
				new DefaultComboBoxModel(lvComboValues);
			getcomboBodyType().setModel(laDCBM);
			getcomboBodyType().setSelectedIndex(liBdyDefault);
			comboBoxHotKeyFix(getcomboBodyType());
		}
		getcomboBodyType().setEnabled(!cbINQ);
	}

	/**
	 * Set Customer Data To Display
	 *
	 */
	private void setCustomerDataToDisplay()
	{
		CustomerData laCustData = caPrmtData.getCustomerData();
		CustomerNameData laCustNameData = laCustData.getCustNameData();
		boolean lbBusiness = laCustNameData.isBusiness();
		getradioBusiness().setSelected(lbBusiness);
		getradioIndividual().setSelected(!lbBusiness);

		if (lbBusiness)
		{
			gettxtBusinessName().setText(
				laCustNameData.getCustBsnName());
			gettxtCustFirstName().setEnabled(false);
			gettxtCustMI().setEnabled(false);
			gettxtCustLastName().setEnabled(false);
			gettxtBusinessName().setEnabled(true);
			getstcLblCustFirstName().setEnabled(false);
			getstcLblCustMI().setEnabled(false);
			getstcLblCustLastName().setEnabled(false);
		}
		else
		{
			gettxtCustFirstName().setText(
				laCustNameData.getCustFstName());
			gettxtCustMI().setText(laCustNameData.getCustMIName());
			gettxtCustLastName().setText(
				laCustNameData.getCustLstName());
			gettxtCustFirstName().setEnabled(true);
			gettxtCustMI().setEnabled(true);
			gettxtCustLastName().setEnabled(true);
			gettxtBusinessName().setEnabled(false);
		}

		caNameAddrComp.setAddressDataToDisplay(
			laCustData.getAddressData());

		gettxtEMail().setText(laCustData.getEMail());
		gettxtPhoneNo().setPhoneNo(laCustData.getPhoneNo());

	}

	/**
	 * All subclasses must implement this method
	 * It sets the data on the screen and is how the controller relays 
	 * information to the view
	 *
	 * @param aaDataObject Object
	 */
	public void setData(Object aaData)
	{
		if (aaData != null)
		{
			csTransCd = getController().getTransCode();
			cbINQ =
				csTransCd.equals(MiscellaneousRegConstant.PRMINQ)
					|| csTransCd.equals(TransCdConstant.PRMDUP);

			if (aaData instanceof PermitData)
			{
				caPrmtData = (PermitData) aaData;

				// defect 10844 
				if (csTransCd.equals(TransCdConstant.MODPT)
					&& UtilityMethods.isEmpty(
						caPrmtData.getTimedPrmtType()))
				{
					caPrmtData.setTimedPrmtType(
						caPrmtData.getAcctItmCd());
				}
				// end defect 10844 

				setPermitTitleToDisplay();
				setDataToDisplay();
			}
			getcomboEffectiveAMPM().addActionListener(this);
		}
	}

	/**
	 * Set data to data object (caPrmtData)
	 * 
	 */
	private void setDataToDataObject()
	{
		CustomerData laCustData = caPrmtData.getCustomerData();
		CustomerNameData laCustNameData = laCustData.getCustNameData();

		if (getradioBusiness().isSelected())
		{
			laCustNameData.setCustBsnName(
				gettxtBusinessName().getText());
			laCustNameData.setCustFstName(null);
			laCustNameData.setCustMIName(null);
			laCustNameData.setCustLstName(null);
		}
		else
		{
			laCustNameData.setCustFstName(
				gettxtCustFirstName().getText());
			laCustNameData.setCustMIName(gettxtCustMI().getText());
			laCustNameData.setCustLstName(
				gettxtCustLastName().getText());
			laCustNameData.setCustBsnName(null);
		}

		caNameAddrComp.setAddressToDataObject(
			caPrmtData.getCustomerData().getAddressData());

		laCustData.setEMail(gettxtEMail().getText());
		// defect 10592 
		laCustData.setPhoneNo(
		//gettxtPhoneNo().getPhoneNo());
		gettxtPhoneNo().getPhoneText());
		// end defect 10592 

		// ONE TRIP DATA 
		OneTripData laOTData = new OneTripData();
		if (cbOTPT)
		{
			laOTData.setOrigtnPnt(gettxtOrigin().getText());
			laOTData.setIntrmdtePnt1(gettxtIntPt1().getText());
			laOTData.setIntrmdtePnt2(gettxtIntPt2().getText());
			laOTData.setIntrmdtePnt3(gettxtIntPt3().getText());
			laOTData.setDestPnt(gettxtDestn().getText());
		}
		caPrmtData.setOneTripData(laOTData);

		String lsModlYr = gettxtVehModlYr().getText();

		String lsVehMk = null;
		String lsVehMkDesc = new String();

		// defect 10592 
		if (getcomboVehicleMake().isVisible()
			&& getcomboVehicleMake().getSelectedIndex() != -1)
		{
			// end defect 10592 
			String lsTempVehMk =
				(String) getcomboVehicleMake().getSelectedItem();
			int liIndexDash = lsTempVehMk.indexOf('-');
			lsVehMk = lsTempVehMk.substring(0, liIndexDash).trim();
			lsVehMkDesc = lsTempVehMk.substring(liIndexDash + 1).trim();
		}
		else
		{
			lsVehMk = gettxtVehMake().getText();
		}

		// defect 10844
		if (gettxtVIN().isEnabled())
		{
			// defect 10989
			if (!gettxtVIN().isEmpty())
			{
				String lsVIN = gettxtVIN().getText().trim();
				gettxtVIN().setText(CommonValidations.convert_i_and_o_to_1_and_0(lsVIN));
			}
			// end defect 10989 
			
			caPrmtData.setVin(gettxtVIN().getText());
		}
		// end defect 10844 

		String lsBodStyl = new String();

		// defect 10592 
		if (getcomboBodyType().getSelectedIndex() != -1)
		{
			String lsTempStrBodSt =
				(String) getcomboBodyType().getSelectedItem();
			int liIndexDash = lsTempStrBodSt.indexOf('-');
			lsBodStyl = lsTempStrBodSt.substring(0, liIndexDash).trim();
		}

		if (!UtilityMethods.isEmpty(lsModlYr))
		{
			caPrmtData.setVehModlYr(Integer.parseInt(lsModlYr));
		}
		// end defect 10592

		caPrmtData.setVehMk(lsVehMk);
		caPrmtData.setVehMkDesc(lsVehMkDesc);
		caPrmtData.setVehBdyType(lsBodStyl);
		caPrmtData.setVehRegPltNo(gettxtPlateNo().getText());

		if (getchkRegisteredUSA().isSelected())
		{
			caPrmtData.setVehRegState(
				gettxtRegisteredState().getText());
			caPrmtData.setVehRegCntry(new String());
		}
		else
		{
			caPrmtData.setVehRegCntry(
				gettxtRegisteredCountry().getText());
			caPrmtData.setVehRegState(new String());
		}

		// from 6.4.0 
		// Sync caPrmtData effective hour value with selected 
		// AM/PM value.
		//		int liHour = caPrmtData.getRTSDateEffDt().getHour();
		//		if (getcomboEffectiveAMPM().getSelectedIndex() == 0
		//			&& liHour > 12)
		//		{
		//			caPrmtData.getRTSDateEffDt().setHour(liHour - 12);
		//			// Sync caPrmtData expiration hour value with selected AM/PM value.
		//			if (caPrmtData.getTransCd().equals(TransCdConstant.PT144)
		//				|| caPrmtData.getTransCd().equals(TransCdConstant.PT72))
		//			{
		//				caPrmtData.getRTSDateExpDt().setHour(liHour - 12);
		//			}
		//		}
		//		else if (
		//			getcomboEffectiveAMPM().getSelectedIndex() == 1
		//				&& liHour < 12)
		//		{
		//			caPrmtData.getRTSDateEffDt().setHour(liHour + 12);
		//			
		//			// Sync caPrmtData expiration hour value with selected
		//			// AM/PM value.
		//			if (caPrmtData.getTransCd().equals(TransCdConstant.PT144)
		//				|| caPrmtData.getTransCd().equals(TransCdConstant.PT72))
		//			{
		//				caPrmtData.getRTSDateExpDt().setHour(liHour + 12);
		//			}
		//		}
		//		else if (
		//			getcomboEffectiveAMPM().getSelectedIndex() == 0
		//				&& liHour == 12)
		//		{
		//			caPrmtData.getRTSDateEffDt().setHour(0);
		//			// Set to midnight.
		//
		//			// Sync caPrmtData expiration hour value with selected
		//			// AM/PM value.
		//			if (ccaPrmtData.getTransCd().equals(TransCdConstant.PT144)
		//				|| caPrmtData.getTransCd().equals(TransCdConstant.PT72))
		//			{
		//				caPrmtData.getRTSDateExpDt().setHour(0);
		//			}
		//		}
		// end defect 10491 

		caPrmtData.setEffDt(
			caPrmtData.getRTSDateEffDt().getYYYYMMDDDate());
		caPrmtData.setEffTime(
			caPrmtData.getRTSDateEffDt().get24HrTime());
		caPrmtData.setExpDt(
			caPrmtData.getRTSDateExpDt().getYYYYMMDDDate());
		caPrmtData.setExpTime(
			caPrmtData.getRTSDateExpDt().get24HrTime());

		// defect 10592
		saveScreenMRG006Data();
		// end defect 10592  

	}

	/**
	 * Manages incoming data for display.  Determines if incoming data 
	 * is used within same vehicle transaction.
	 * 
	 */
	private void setDataToDisplay()
	{
		setCustomerDataToDisplay();
		setPermitDataToDisplay();
		setVehicleDataToDisplay();
		setVehicleMakesToDisplay();
		setBodyTypeToDisplay();

		// defect 10844 
		setInqFieldsToDisplay(
		// cbINQ); 
		cbINQ || csTransCd.equals(TransCdConstant.MODPT));
		// end defect 10844 

		if (!cbINQ)
		{
			getchkUSA().addItemListener(this);
			getchkRegisteredUSA().addItemListener(this);
			getradioBusiness().addItemListener(this);
			getradioIndividual().addItemListener(this);
			ivjcomboVehicleMake.addItemListener(this);
		}
		else
		{
			UtilityMethods.disableAllFields(this);
		}
	}

	/**
	 * Enable/Setup Data for Development 
	 *
	 */
	private void setDevDataToDisplay()
	{
		if (SystemProperty.isDevStatus()
			&& !UtilityMethods.isEmpty(caPrmtData.getPrmtIssuanceId()))
		{
			getlblPrmtIssuanceId().setText(
				"O: "
					+ caPrmtData.getPrmtIssuanceId()
					+ "  "
					+ new TransactionIdData(
						caPrmtData.getPrmtIssuanceId())
						.getTransactionDateMMDDYYYY());

			if (!UtilityMethods
				.isEmpty(caPrmtData.getAuditTrailTransId()))
			{
				getlblAuditTrailTransId().setText(
					"A: "
						+ caPrmtData.getAuditTrailTransId()
						+ "  "
						+ new TransactionIdData(
							caPrmtData.getAuditTrailTransId())
							.getTransactionDateMMDDYYYY());
			}
		}
	}
	/**
	 * 
	 * Set Inquiry Fields to Display
	 *
	 * @param abInq
	 */
	private void setInqFieldsToDisplay(boolean abInq)
	{
		getJPanelIssueDateAmtPaid().setVisible(abInq);
		getstcLblIssued().setVisible(abInq);
		getstcLblAmountPaid().setVisible(abInq);
		getlblIssueDate().setVisible(abInq);
		getlblAmountPaid().setVisible(abInq);
		getstcLblIssuedBy().setVisible(abInq);
		getlblIssuedBy().setVisible(abInq);

		if (abInq)
		{
			// defect 10726 
			getlblIssuedBy().setText(caPrmtData.getIssuingOfcName());

			// defect 10844
			// Issue Date will be derived from either AuditTrailTransId 
			//  or PermitIssuanceNo if AuditTrailTransId is empty.  
			getlblIssueDate().setText(
				new TransactionIdData(caPrmtData.getLastTransId())
					.getTransactionDateMMDDYYYY());
			// end defect 10844

			Dollar laDollar = caPrmtData.getPrmtPdAmt();

			if (laDollar != null)
			{
				getlblAmountPaid().setText(laDollar.printDollar());
			}
		}
	}

	/** 
	 * Setup MFVehicleData for Fees 
	 * 
	 * @return MFVehicleData
	 */
	private MFVehicleData setMFVehForFees()
	{
		MFVehicleData laMFVehData = new MFVehicleData();
		VehicleData laVehData = new VehicleData();
		laVehData.setVehModlYr(caPrmtData.getVehModlYr());
		laVehData.setVehMk(caPrmtData.getVehMk());
		laVehData.setVehBdyType(caPrmtData.getVehBdyType());
		laVehData.setVin(caPrmtData.getVin());
		laMFVehData.setVehicleData(laVehData);
		laMFVehData.setRegData(new RegistrationData());
		OwnerData laOwnrData = new OwnerData();
		String lsCustomerName =
			caPrmtData.getCustomerData().getCustNameData().getCustName(
				false);
		laOwnrData.setName1(lsCustomerName);
		laOwnrData.setAddressData(
			caPrmtData.getCustomerData().getAddressData());
		laMFVehData.setOwnerData(laOwnrData);
		return laMFVehData;
	}

	/**
	 * 
	 * Populate One Trip Data
	 *
	 */
	private void setOneTripDataToDisplay()
	{
		boolean lbEnable = cbOTPT && !cbINQ;
		for (int i = 0; i < cvOTPTComponents.size(); i++)
		{
			Component laComponent =
				(Component) cvOTPTComponents.elementAt(0);
			laComponent.setVisible(cbOTPT);
			if (laComponent instanceof RTSInputField)
			{
				laComponent.setEnabled(lbEnable);
			}
		}

		// defect 10592 
		if (cbOTPT && (cbINQ || caPrmtData.getOneTripData() != null))
		{
			// end defect 10592 
			gettxtOrigin().setText(
				caPrmtData.getOneTripData().getOrigtnPnt());
			gettxtIntPt1().setText(
				caPrmtData.getOneTripData().getIntrmdtePnt1());
			gettxtIntPt2().setText(
				caPrmtData.getOneTripData().getIntrmdtePnt2());
			gettxtIntPt3().setText(
				caPrmtData.getOneTripData().getIntrmdtePnt3());
			gettxtDestn().setText(
				caPrmtData.getOneTripData().getDestPnt());
		}
		getJPanelOTPT().setVisible(cbOTPT);
	}

	/**
	 * Set Permit Data To Display 
	 *
	 */
	private void setPermitDataToDisplay()
	{
		getlblPermitNo().setText(caPrmtData.getPrmtNo());
		csItmCd = caPrmtData.getItmCd();

		// For Inquiry/Duplicate Receipt 
		if (caPrmtData.getRTSDateEffDt() == null
			&& caPrmtData.getRTSDateEffDt() == null)
		{
			caPrmtData.setRTSDateEffDt(
				new RTSDate(RTSDate.YYYYMMDD, caPrmtData.getEffDt()));

			caPrmtData.setRTSDateExpDt(
				new RTSDate(RTSDate.YYYYMMDD, caPrmtData.getExpDt()));

			caPrmtData.getRTSDateEffDt().setTime(
				caPrmtData.getEffTime());

			caPrmtData.getRTSDateExpDt().setTime(
				caPrmtData.getExpTime());
		}

		populateAmPmCombo();
		populateDatesAndTimes();
		setOneTripDataToDisplay();
		// defect 10844 
		// setFDPTToDisplay(); 
		disableEffDateTimeIfNeeded();
		setDevDataToDisplay();
		// end defect 10844 

	}

	/**
	 * Set permit title text based on permit type.
	 * 
	 */
	private void setPermitTitleToDisplay()
	{
		// defect 10726 
		// Use ItmCd for Title 

		String lsPermitType = caPrmtData.getItmCd();

		if (lsPermitType.equals(MiscellaneousRegConstant.ITMCD_72PT))
		{
			lsPermitType = PERMIT_TITLE_72_HOUR;
		}
		else if (
			lsPermitType.equals(MiscellaneousRegConstant.ITMCD_144PT))
		{
			lsPermitType = PERMIT_TITLE_144_HOUR;
		}
		else if (
			lsPermitType.equals(MiscellaneousRegConstant.ITMCD_OTPT))
		{
			lsPermitType = PERMIT_TITLE_ONE_TRIP;
			cbOTPT = true;
		}
		else if (
			lsPermitType.equals(MiscellaneousRegConstant.ITMCD_OTMCPT))
		{
			lsPermitType = PERMIT_TITLE_ONE_TRIP_MOTORCYCLE;
			cbOTPT = true;
		}
		else if (
			lsPermitType.equals(MiscellaneousRegConstant.ITMCD_FDPT))
		{
			lsPermitType = PERMIT_TITLE_FACTORY_DELIVERY;
		}
		else if (
			lsPermitType.equals(MiscellaneousRegConstant.ITMCD_30PT))
		{
			lsPermitType = PERMIT_TITLE_30_DAY;
		}
		else if (
			lsPermitType.equals(MiscellaneousRegConstant.ITMCD_30MCPT))
		{
			lsPermitType = PERMIT_TITLE_30_DAY_MOTORCYCLE;
		}
		// end defect 10726 

		getstcLblPermitTitle().setText(lsPermitType);
	}

	/**
	 * Populates Vehicle Data on Display
	 *
	 */
	private void setVehicleDataToDisplay()
	{
		if (caPrmtData.getVehModlYr() != 0)
		{
			gettxtVehModlYr().setText(
				Integer.toString(caPrmtData.getVehModlYr()));
		}
		else
		{
			gettxtVehModlYr().setText("");
		}
		// defect 10844 
		boolean lbEnableModVin =
			csTransCd.equals(TransCdConstant.MODPT);
		getlblVIN().setVisible(!lbEnableModVin);
		gettxtVIN().setVisible(lbEnableModVin);
		gettxtVIN().setEnabled(lbEnableModVin);

		if (lbEnableModVin)
		{
			gettxtVIN().setText(caPrmtData.getVin());
		}
		else
		{
			getlblVIN().setText(caPrmtData.getVin());
		}
		// end defect 10844 

		boolean lbRegUSA =
			UtilityMethods.isEmpty(caPrmtData.getVehRegCntry());

		boolean lbEnable = !cbINQ;

		getchkRegisteredUSA().setSelected(lbRegUSA);
		gettxtRegisteredCountry().setVisible(!lbRegUSA);
		gettxtRegisteredState().setVisible(lbRegUSA);
		gettxtRegisteredCountry().setEnabled(!lbRegUSA && lbEnable);
		gettxtRegisteredState().setEnabled(lbRegUSA && lbEnable);
		gettxtRegisteredState().setVisible(lbRegUSA);

		if (lbRegUSA)
		{
			gettxtRegisteredState().setText(
				caPrmtData.getVehRegState());
		}
		else
		{
			gettxtRegisteredCountry().setText(
				caPrmtData.getVehRegCntry());
		}
		gettxtPlateNo().setText(caPrmtData.getVehRegPltNo());
	}

	/**
	 * Populates the vehicle make combo box.
	 */
	private void setVehicleMakesToDisplay()
	{
		String lsVehMk =
			caPrmtData.getVehMk() == null
				? new String()
				: caPrmtData.getVehMk();
		String lsVehMkDesc = caPrmtData.getVehMkDesc();

		if (getcomboVehicleMake().isEnabled())
		{
			Vector lvVehMkCache = VehicleMakesCache.getVehMks();

			int liMkDefault = -1;

			UtilityMethods.sort(lvVehMkCache);
			Vector lvComboVal = new Vector();
			if (lvVehMkCache != null)
			{
				for (int i = 0; i < lvVehMkCache.size(); i++)
				{
					VehicleMakesData laVehMkdata =
						(VehicleMakesData) lvVehMkCache.get(i);

					if (laVehMkdata.getVehMk().equals(lsVehMk))
					{
						if ((UtilityMethods.isEmpty(lsVehMkDesc)
							|| lsVehMkDesc.equals(
								laVehMkdata.getVehMkDesc()))
							&& liMkDefault == -1)
						{
							liMkDefault = i;
						}
					}
					String lsDesc =
						laVehMkdata.getVehMk()
							+ SPACE_DASH
							+ " "
							+ laVehMkdata.getVehMkDesc();
					lvComboVal.add(lsDesc);
				}
				lvComboVal.add(NEW);
			}

			DefaultComboBoxModel laDCBM =
				new DefaultComboBoxModel(lvComboVal);
			getcomboVehicleMake().setModel(laDCBM);
			getcomboVehicleMake().setSelectedIndex(liMkDefault);
			comboBoxHotKeyFix(getcomboVehicleMake());
		}

		// defect 10592 
		if (!UtilityMethods.isEmpty(lsVehMk))
			//&& caPrmtData.getNoMFRecs() != 0)
		{
			// end defect 10592 
			int liSelected = getcomboVehicleMake().getSelectedIndex();
			if (liSelected == -1)
			{
				getcomboVehicleMake().setVisible(false);
				getcomboVehicleMake().setEnabled(false);
				gettxtVehMake().setVisible(true);
				gettxtVehMake().setText(lsVehMk);
				gettxtVehMake().setEnabled(!cbINQ);
			}
			else
			{
				getcomboVehicleMake().setEnabled(!cbINQ);
			}
		}
	}

	/**
	 * Validate Data  
	 *
	 * @return boolean
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;

		UtilityMethods.trimRTSInputField(this);

		RTSException leRTSEx = new RTSException();

		// VEHICLE YEAR 
		if (gettxtVehModlYr().isEmpty()
			|| CommonValidations.isInvalidYearModel(
				gettxtVehModlYr().getText().trim()))
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_MODEL_YEAR_NOT_VALID),
				gettxtVehModlYr());
		}

		// VEHICLE MAKE 
		if (getcomboVehicleMake().isVisible())
		{
			if (getcomboVehicleMake().getSelectedIndex() == -1)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getcomboVehicleMake());
			}
		}
		else
		{
			if (gettxtVehMake().isEmpty())
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtVehMake());
			}
		}

		// BODY TYPE 
		if (getcomboBodyType().getSelectedIndex() == -1)
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				getcomboBodyType());
		}

		// REGISTERED STATE / COUNTRY 
		if (getchkRegisteredUSA().isSelected())
		{
			if (!gettxtRegisteredState().isEmpty())
			{
				if (!CommonValidations
					.isValidState(gettxtRegisteredState().getText()))
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_INVALID_STATE_REENTER),
						gettxtRegisteredState());
				}
			}
		}
		else
		{
			if (!gettxtRegisteredCountry().isEmpty())
			{
				if (gettxtRegisteredCountry().isValidState())
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_VALID_STATE_IN_CNTRY_FIELD),
						gettxtRegisteredCountry());

					leRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_VALID_STATE_IN_CNTRY_FIELD),
						getchkRegisteredUSA());
				}
				else
				{
					String lsCntry =
						gettxtRegisteredCountry().getText();

					// Requirement for either "CANA" / "MEXI" only 
					//   applies to 72 Hr or 144 Hr Permits 
					if (caPrmtData
						.getTimedPrmtType()
						.equals(TransCdConstant.PT72)
						|| caPrmtData.getTimedPrmtType().equals(
							TransCdConstant.PT144))
					{
						if (!(lsCntry.equals(ABBR_CANADA)
							|| lsCntry.equals(ABBR_MEXICO)))
						{
							leRTSEx.addException(
								new RTSException(
									ErrorsConstant
										.ERR_NUM_PRMT_CNTRY_OF_REG_INVALID),
								gettxtRegisteredCountry());
						}
					}
					// defect 11004
					CommonValidations.addRTSExceptionForInvalidCntryStateCntry(gettxtRegisteredCountry(), 
							leRTSEx,TitleConstant.NOT_REQUIRED);
					// end defect 11004 
				}
			}
		}

		// EFF DATE 
		validateEffDt(leRTSEx);

		// EFF TIME 	
		validateEffTime(leRTSEx);

		// ORIGIN / DESTINATION 
		if (cbOTPT)
		{
			if (gettxtOrigin().isEmpty())
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtOrigin());
			}
			if (gettxtDestn().isEmpty())
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtDestn());
			}
		}

		// INVALID CHARACTERS 
		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvMFValid,
			leRTSEx);

		// NAME  (BUSINESS / INDIVIDUAL)  
		validateName(leRTSEx);

		// ADDRESS 
		caNameAddrComp.validateAddressFields(leRTSEx);

		// EMAIL / PHONE 
		validateEMailPhoneNo(leRTSEx);

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		// defect 10844 
		else
		{
			if (gettxtVIN().isEnabled()
				&& gettxtVIN().getText().length()
					!= CommonConstant.LENGTH_VIN)
			{
				// 555
				new RTSException(
					ErrorsConstant
						.ERR_NUM_VIN_NOT_17_DIGITS)
						.displayError(
					this);
			}
		}
		// end defect 10844 
		return lbReturn;
	}

	/**
	 * Validate the input effective date.
	 *
	 * @param aeRTSEx RTSException
	 */
	private void validateEffDt(RTSException aeRTSEx)
	{
		// defect 10844 
		if (!(csTransCd.equals(TransCdConstant.MODPT)
			&& caPrmtData.isCurrent()))
		{
			// end defect 10844 

			if (!gettxtEffectiveDate().isValidDate())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtEffectiveDate());
			}
			else
			{
				RTSDate laRTSDate = gettxtEffectiveDate().getDate();
				// defect 10844 
				RTSDate laMaxDate =
					new RTSDate().add(RTSDate.MONTH, 12);

				if (laRTSDate.getYYYYMMDDDate()
					> laMaxDate.getYYYYMMDDDate())
				{
					aeRTSEx.addException(new RTSException(
					//	ErrorsConstant
					//	.ERR_NUM_PRMT_DATE_NOT_LESS_THAN_CURRENT),
					2124), gettxtEffectiveDate());
				}
				else
					// end defect 10844
					// is entered date greater than today. 
					if (laRTSDate.getYYYYMMDDDate()
						>= RTSDate.getCurrentDate().getYYYYMMDDDate())
					{
						caPrmtData.setRTSDateEffDt(
							new RTSDate(
								laRTSDate.getYear(),
								laRTSDate.getMonth(),
								laRTSDate.getDate(),
								caPrmtData.getRTSDateEffDt().getHour(),
								caPrmtData
									.getRTSDateEffDt()
									.getMinute(),
								caPrmtData
									.getRTSDateEffDt()
									.getSecond(),
								0));

						MiscellaneousRegClientBusiness
							.setTimedPrmtExpDtAndTime(
							caPrmtData);

						getlblExpirationDate().setText(
							caPrmtData.getRTSDateExpDt().toString());
					}
					else
					{
						// date entered is less than today
						aeRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_PRMT_DATE_NOT_LESS_THAN_CURRENT),
							gettxtEffectiveDate());
					}
			}
			// defect 10844 
		}
		// end defect 10844 
	}

	/**
	 * Validate the input effective time.
	 *
	 * @param aeRTSEx RTSException 
	 */
	private void validateEffTime(RTSException aeRTSEx)
	{
		if (!gettxtEffectiveTime().isValidTime())
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtEffectiveTime());
		}
		// defect 10491
		else
		{
			int liInputTime = gettxtEffectiveTime().get24HrTime();

			int liHour = liInputTime / 10000;

			boolean lbPM =
				getcomboEffectiveAMPM().getSelectedItem().equals(PM);

			if (lbPM && liHour < 12 && liHour != 0)
			{
				liHour = liHour + 12;
			}
			else if (!lbPM && liHour == 12)
			{
				liHour = liHour - 12;
			}
			String lsInputTime =
				UtilityMethods.addPadding("" + liInputTime, 6, "0");

			if (SystemProperty.isDevStatus())
			{
				System.out.println(
					"Current: "
						+ caPrmtData.getRTSDateEffDt().get24HrTime());
				System.out.println(
					"    New: " + liHour + lsInputTime.substring(2));
			}

			// defect 10592 
			if (gettxtEffectiveDate().isValidDate())
			{
				caPrmtData.setRTSDateEffDt(
					gettxtEffectiveDate().getDate());
			}
			// end defect 10592  

			// reconstruct effective date with new time
			RTSDate laRTSDateExpDt = caPrmtData.getRTSDateEffDt();

			caPrmtData.setRTSDateEffDt(
				new RTSDate(
					laRTSDateExpDt.getYear(),
					laRTSDateExpDt.getMonth(),
					laRTSDateExpDt.getDate(),
					liHour,
					Integer.parseInt(lsInputTime.substring(2, 4)),
					Integer.parseInt(lsInputTime.substring(4, 6)),
					0));

			MiscellaneousRegClientBusiness.setTimedPrmtExpDtAndTime(
				caPrmtData);

			populateDatesAndTimes();
		}
		if (!aeRTSEx.isValidationError())
		{
			clearAllColor(gettxtEffectiveTime());
		}
		// end defect 10491   
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
	 * Validate name is complete.
	 * 
	 * @param aeRTSEx RTSException 
	 */
	private void validateName(RTSException aeRTSEx)
	{
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
		// Business 
		else if (getradioBusiness().isSelected())
		{
			if (gettxtBusinessName().isEmpty())
			{
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_INCOMPLETE_APPL_DATA),
					gettxtBusinessName());

			}
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
