package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientBusiness;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientUtilityMethods;
import com.txdot.isd.rts.client.title.ui.TitleClientUtilityMethods;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmTempAddlWeightMRG010.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs		    05/28/2002	Made focus go to owner address field 1 when 
 * 							enabled CQU100004107
 * Bob Brown	07/23/2002	For defect #4476, lined up field labels so 
 * 							the colons were vertically aligned in VCE
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Removed references/methods for Sticker
 *							delete ivjlblSticker,ivjstcLblSticker
 *							deprecate getlblSticker()
 *							modify setData()
 * 							processing. Ver 5.2.0	
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	06/23/2005	Modify code for move to Java 1.4.
 * 							Bring code to standards
 * 							Remove unused methods and variables.
 * 							Remove keyPressed()  (arrow key handling is 
 * 							done in ButtonPanel)
 *							defect 7893 Ver 5.2.3
 * B Hargrove	07/14/2005	Refactor\Move 
 * 							MiscellaneousRegClientUtilityMethods class  
 *							to com.txdot.isd.rts.client.miscreg.business.
 *							defect 7893 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T. Pederson	12/21/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * Min Wang		10/11/2006	New Requirement for handling plate age 
 * 							modify setData() 
 *							defect 8901 Ver Exempts
 * Min Wang		10/19/2006	New Requirement for handling plate age 
 * 							modify setData() 
 * 							defect 8901 Ver Exempts
 * K Harrell	11/14/2006	Add "/" between Exp Mo & Exp Yr
 * 							Do not present "0" for Exp Mo / Exp Yr
 * 							add ivjlblSlash
 * 							add getivjlblSlash()
 * 							delete getBuilderData(),windowOpened(),
 * 							 getlblSticker(), getMFVehicleData(),
 * 							 getOwnerData(),getRegistrationData(),
 * 							 getTitleData()
 * 							modify getpnlRegistration,setData()
 * 							defect 8900 Ver Exempts 
 * T Pederson	02/12/2007	Added organization name to screen.  
 * 							modify setData()   
 * 							defect 9123 Ver Special Plates
 * K Harrell	03/09/2007	Implemented SystemProperty.isHQ()
 * 							modify procsSoftStop()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/20/2008	Correct handling of Temp Add'l Weight field
 * 							validation. Code Cleanup. 
 * 							Aligned USA checkbox with OwnerId.
 * 							add cbTempWeightError  
 * 							delete getTimedPrmtData(), setMFVehicleData(), 
 * 							  setTitleData(), setOwnerData(),keyPressed(), 
 * 							  setRegistrationData(), setTimedPrmtData(),
 * 							  setVehicleData(), setVehInqData() 
 * 							modify actionPerformed(),focusLost(), 
 * 							  handleTempAddlWt(), setData(),
 * 							  validateInputFields() 
 * 							defect 7063 Ver POS Defect A
 * K Harrell	05/20/2008	MRG010: Screen Alignment 
 * 							via Visual Editor
 * 							defect 8583 Ver POS Defect A   
 * K Harrell	01/08/2009	Remove reference to RegisData.setOrgNo() 
 * 							modify setData() 
 * 							defect 9912 Ver Defect_POS_D
 * K Harrell	04/01/2009	Enlarged Remarks Box 
 * 							Use CommonConstant.MAX_INDI_NO_SCROLL; 
 * 							Use Constant for Remarks font 
 *							modify setData(), getlstIndicator() 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	06/29/2009	Implement new OwnerData.  Additional Cleanup.
 * 							add DFLT_ORGANIZATION, ORG 
 * 							delete CNTRY_ZIP_MAXLENGTH 
 * 							modify setData(), setInputFields(), 
 * 							 procsSoftStops(), procsHardStops(), 
 * 							 handleTempAddlWt()
 * 							defect 10112 Ver Defect_POS_F  
 * K Harrell	07/18/2009	Implement new validation of Country field.
 * 							 refactor RTSInputFields, get methods to 
 * 							 standards 
 *							add caNameAddrComp
 * 							modify initialize(), setData(),
 * 							 itemStateChanged(), setInputFields(), 
 * 							 validateInputFields()
 *							defect 10127 Ver Defect_POS_F 
 * K Harrell 	11/07/2011	delete MAX_GROSS_WEIGHT
 * 							modify handleTempAddlWt()
 * 							defect 10959 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 * Registration of Temporary Additional Weight.
 * 
 * @version	6.9.0 			11/07/2011
 * @author	Joseph Kwik
  * <br>Creation Date:		06/26/2001 
 */
public class FrmTempAddlWeightMRG010
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkUSA = null;
	private JPanel ivjFrmTempAddlWeightMRG010ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel6 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblAge = null;
	private JLabel ivjlblBodyVIN = null;
	private JLabel ivjlblCapacity = null;
	private JLabel ivjlblClass = null;
	private JLabel ivjlblCounty = null;
	private JLabel ivjlblDocTypeCdDescription = null;
	private JLabel ivjlblDocumentNo = null;
	private JLabel ivjlblEmpty = null;
	private JLabel ivjlblExpirationMonth = null;
	private JLabel ivjlblExpirationYear = null;
	private JLabel ivjlblGross = null;
	private JLabel ivjlblIssued = null;
	private JLabel ivjlblMake = null;
	private JLabel ivjlblOrg = null;
	private JLabel ivjlblOwnerId = null;
	private JLabel ivjlblPlate = null;
	private JLabel ivjlblSlash = null;
	private JLabel ivjlblTempCarryingCapacity = null;
	private JLabel ivjlblTempGrossWeight = null;
	private JLabel ivjlblTons = null;
	private JLabel ivjlblType = null;
	private JLabel ivjlblUnitNo = null;
	private JLabel ivjlblVehBodyType = null;
	private JLabel ivjlblVehicleClass = null;
	private JLabel ivjlblVehModel = null;
	private JLabel ivjlblVIN = null;
	private JLabel ivjlblYear = null;
	private JList ivjlstIndicator = null;
	private JPanel ivjpnlDocument = null;
	private JPanel ivjpnlOwner = null;
	private JPanel ivjpnlRegistration = null;
	private JPanel ivjpnlVehicle = null;
	private JPanel ivjpnlWeight = null;
	private JLabel ivjstcLblAge = null;
	private JLabel ivjstcLblBodyVIN = null;
	private JLabel ivjstcLblCapacity = null;
	private JLabel ivjstcLblClass = null;
	private JLabel ivjstcLblCounty = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblDocumentNo = null;
	private JLabel ivjstcLblEmpty = null;
	private JLabel ivjstcLblExpires = null;
	private JLabel ivjstcLblGross = null;
	private JLabel ivjstcLblId = null;
	private JLabel ivjstcLblIssued = null;
	private JLabel ivjstcLblOrg = null;
	private JLabel ivjstcLblPlate = null;
	private JLabel ivjstcLblTempAddlWeight = null;
	private JLabel ivjstcLblTempCarryingCapacity = null;
	private JLabel ivjstcLblTempGrossWeight = null;
	private JLabel ivjstcLblTons = null;
	private JLabel ivjstcLblType = null;
	private JLabel ivjstcLblUnitNo = null;
	private JLabel ivjstcLblVehicleClass = null;
	private RTSInputField ivjtxtOwnerCity = null;
	private RTSInputField ivjtxtOwnerCntry = null;
	private RTSInputField ivjtxtOwnerCntryZpcd = null;
	private RTSInputField ivjtxtOwnerName1 = null;
	private RTSInputField ivjtxtOwnerName2 = null;
	private RTSInputField ivjtxtOwnerState = null;
	private RTSInputField ivjtxtOwnerStreet1 = null;
	private RTSInputField ivjtxtOwnerStreet2 = null;
	private RTSInputField ivjtxtOwnerZpcd = null;
	private RTSInputField ivjtxtOwnerZpcdP4 = null;
	private RTSInputField ivjtxtTempAddlWeight = null;

	// boolean 
	private boolean cbSetDataFinished = false;
	private boolean cbTempWeightError = false;

	// int 
	private int ciNumIndis = 0;

	// defect 10127 
	private NameAddressComponent caNameAddrComp = null;
	// end defect 10127 

	//	Objects  
	private MFVehicleData caMFVehicleData;
	private MFVehicleData caOldMFVehData = null;
	private OwnerData caOwnerData;
	private RegistrationData caRegistrationData = null;
	private TimedPermitData caTimedPrmtData = null;
	private TitleData caTitleData = null;
	private VehicleData caVehicleData = null;
	private VehicleInquiryData caVehInqData = null;

	// Constants
	private final static String AGE = "Age:";
	private final static String BODY_VIN = "Body VIN:";
	private final static String CAPACITY = "Capacity:";
	private final static String CLASS = "Class:";
	private final static String COUNTY = "County:";
	// end defect 10112
	private final static String DASH = "-";
	private final static String DFLT_2DIGIT = "12";
	private final static String DFLT_BODY = "2D";
	private final static String DFLT_CNTY = "County";
	private final static String DFLT_DOCNO = "00000000001151115";
	private final static String DFLT_DOCTYPE_CD_DESC =
		"docTypeCdDescription";
	private final static String DFLT_DOLLAR_AMT = "0.00";
	private final static String DFLT_ISSUE_DT = "01/01/2001";
	private final static String DFLT_MODEL = "CHEV";
	// defect 10112 
	private final static String DFLT_ORGANIZATION = "Organization";
	// end defect 10112 
	private final static String DFLT_OWNER_ID = "102030405";
	private final static String DFLT_PLATE_NO = "XXX999X";
	private final static String DFLT_PLATE_TYPE = "Plate Type";
	private final static String DFLT_REG_CLASS = "Reg Class";
	private final static String DFLT_UNIT = "Unit #";
	private final static String DFLT_VEH_CLASS = "Veh Class";
	private final static String DFLT_VEH_LB = "Veh Lb.";
	private final static String DFLT_VIN = "1234567890123456789";
	private final static String DFLT_YEAR = "2000";
	private final static String DFLT_ZERO = "0";
	private final static String DOCNO = "Document No:";
	private final static String DOCUMENT = "Document:";
	private final static String EMPTY = "Empty:";
	private final static String ERRMSG_ERROR = "ERROR!";
	private final static String ERRMSG_TYPE =
		"Type not expected in setView";
	private final static String EXPIRES = "Expires:";
	private final static String GROSS = "Gross:";
	private final static String ID = "Id:";
	private final static String ISSUED = "Issued:";
	// defect 10112 
	// private final static int CNTRY_ZIP_MAXLENGTH = 5;
	private final static int LENGTH_ADDL_WT = 5;
	private final static String ORG = "Org:";
	// end defect 10112
	// defect 10959 
	//private final static int MAX_GROSS_WEIGHT = 80000;
	// end defect 10959 
	private final static String OWNER = "Owner:";
	private final static String PLATE = "Plate:";
	private final static String REGIS = "Registration:";
	private final static String TEMP_ADDL_WT = "Temp Addl Weight:";
	private final static String TEMP_CARRYCAP =
		"Temp Carrying Capacity:";
	private final static String TEMP_GROSS_WT = "Temp Gross Weight:";
	private final static String TITLE_MRG010 =
		"Temp Addl Weight     MRG010";
	private final static String TONS = "Tons:";
	private final static String TYPE = "Type:";
	private final static String UNIT_NO = "Unit No:";
	private final static String USA = "USA";
	private final static String VEHICLE = "Vehicle:";
	private final static String VIN = "VIN";
	private final static String WEIGHT = "Weight:";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmTempAddlWeightMRG010 laFrmTempAddlWeightMRG010;
			laFrmTempAddlWeightMRG010 = new FrmTempAddlWeightMRG010();
			laFrmTempAddlWeightMRG010.setModal(true);
			laFrmTempAddlWeightMRG010
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmTempAddlWeightMRG010.show();
			java.awt.Insets laInsets =
				laFrmTempAddlWeightMRG010.getInsets();
			laFrmTempAddlWeightMRG010.setSize(
				laFrmTempAddlWeightMRG010.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmTempAddlWeightMRG010.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmTempAddlWeightMRG010.setVisibleRTS(true);
			laFrmTempAddlWeightMRG010.setSetDataFinished(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmTempAddlWeightMRG010 constructor comment.
	 */
	public FrmTempAddlWeightMRG010()
	{
		super();
		initialize();
	}

	/**
	 * FrmTempAddlWeightMRG010 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmTempAddlWeightMRG010(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmTempAddlWeightMRG010 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmTempAddlWeightMRG010(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Action Performed
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

			// defect 7063 
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateInputFields())
				{
					setInputFields();

					// check for hard stops
					if (!procsHardStops())
					{
						return;
					}
					if (!procsSoftStops())
					{
						return;
					}
					CompleteTransactionData laData =
						MiscellaneousRegClientUtilityMethods.prepFees(
							caTimedPrmtData,
							caMFVehicleData,
							caOldMFVehData,
							caVehInqData.getVehMiscData());
					MiscellaneousRegClientBusiness.setTimedPrmtTime(
						caTimedPrmtData);
					getController().processData(
						AbstractViewController.ENTER,
						laData);
				}
				cbTempWeightError = false;
			}
			// end defect 7063 
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
				// defect 8177 
				//RTSHelp.displayHelp(RTSHelp.MRG010);
				if (UtilityMethods.isMFUP())
				{
					RTSHelp.displayHelp(RTSHelp.MRG010A);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.MRG010B);
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
	 * Focus Gained
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		//empty code block
	}

	/**
	 * Focus Lost
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		// defect 7063 
		// Calculate temp additional weight
		if (!aaFE.isTemporary()
			&& !cbTempWeightError
			&& aaFE.getOppositeComponent() != null
			&& !aaFE.getOppositeComponent().equals(
				getButtonPanel1().getBtnCancel())
			&& !aaFE.getOppositeComponent().equals(
				getButtonPanel1().getBtnHelp()))
		{
			RTSException leRTSEx = new RTSException();
			handleTempAddlWt(leRTSEx);
			if (leRTSEx.isValidationError())
			{
				leRTSEx.displayError(this);
				cbTempWeightError = false;
			}
			else
			{
				clearAllColor(this);
			}
		}
		// end defect 7063 
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setBounds(162, 407, 312, 37);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
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
				ivjchkUSA.setName("ivjchkUSA");
				ivjchkUSA.setMnemonic(85);
				ivjchkUSA.setText(USA);
				ivjchkUSA.setMaximumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA.setActionCommand(USA);
				ivjchkUSA.setBounds(170, 11, 57, 17);
				ivjchkUSA.setMinimumSize(
					new java.awt.Dimension(49, 22));
				// user code begin {1}
				ivjchkUSA.addItemListener(this);
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
	 * Return the FrmTempAddlWeightMRG010ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmTempAddlWeightMRG010ContentPane1()
	{
		if (ivjFrmTempAddlWeightMRG010ContentPane1 == null)
		{
			try
			{
				ivjFrmTempAddlWeightMRG010ContentPane1 = new JPanel();
				ivjFrmTempAddlWeightMRG010ContentPane1.setName(
					"FrmTempAddlWeightMRG010ContentPane1");
				ivjFrmTempAddlWeightMRG010ContentPane1.setLayout(null);
				ivjFrmTempAddlWeightMRG010ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmTempAddlWeightMRG010ContentPane1.setMinimumSize(
					new java.awt.Dimension(709, 551));
				ivjFrmTempAddlWeightMRG010ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				ivjFrmTempAddlWeightMRG010ContentPane1.setVisible(true);
				getFrmTempAddlWeightMRG010ContentPane1().add(
					getpnlRegistration(),
					getpnlRegistration().getName());
				getFrmTempAddlWeightMRG010ContentPane1().add(
					getpnlVehicle(),
					getpnlVehicle().getName());
				getFrmTempAddlWeightMRG010ContentPane1().add(
					getpnlWeight(),
					getpnlWeight().getName());
				getFrmTempAddlWeightMRG010ContentPane1().add(
					getpnlOwner(),
					getpnlOwner().getName());
				getFrmTempAddlWeightMRG010ContentPane1().add(
					getpnlDocument(),
					getpnlDocument().getName());
				getFrmTempAddlWeightMRG010ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmTempAddlWeightMRG010ContentPane1().add(
					getJPanel2(),
					getJPanel2().getName());
				getFrmTempAddlWeightMRG010ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
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
		return ivjFrmTempAddlWeightMRG010ContentPane1;
	}

	/**
	 * Get indicators, build string to display indicators in text area.
	 */
	private void getIndicators()
	{
		Vector lvIndis =
			IndicatorLookup.getIndicators(
				caVehInqData.getMfVehicleData(),
				getController().getTransCode(),
				IndicatorLookup.SCREEN);
		StringBuffer lsIndis = new StringBuffer("");
		ciNumIndis = lvIndis.size();
		if (ciNumIndis > 0)
		{
			Vector lvRows = new java.util.Vector();
			for (int i = 0; i < ciNumIndis; i++)
			{
				IndicatorData lData = (IndicatorData) lvIndis.get(i);
				lsIndis.append(
					lData.getStopCode() == null
						? " "
						: lData.getStopCode());
				lsIndis.append("  ");
				lsIndis.append(lData.getDesc());
				lvRows.add(lsIndis.toString());
				lsIndis = new StringBuffer("");
			}
			getlstIndicator().setListData(lvRows);
			getlstIndicator().setSelectedIndex(0);
		}
	}

	/**
	 * Return the ivjlblSlash property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getivjlblSlash()
	{
		if (ivjlblSlash == null)
		{
			ivjlblSlash = new JLabel();
			ivjlblSlash.setSize(8, 14);
			ivjlblSlash.setText(CommonConstant.STR_SLASH);
			ivjlblSlash.setName("ivjlblSlash");
			ivjlblSlash.setLocation(98, 51);
		}
		return ivjlblSlash;
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
				ivjJPanel1.setBounds(274, 233, 319, 93);
				getJPanel1().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
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
		return ivjJPanel1;
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
				ivjJPanel2.setBounds(274, 332, 312, 74);
				ivjJPanel2.add(getstcLblTempAddlWeight(), null);
				ivjJPanel2.add(gettxtTempAddlWeight(), null);
				ivjJPanel2.add(getstcLblTempCarryingCapacity(), null);
				ivjJPanel2.add(getlblTempCarryingCapacity(), null);
				ivjJPanel2.add(getstcLblTempGrossWeight(), null);
				ivjJPanel2.add(getlblTempGrossWeight(), null);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJPanel2;
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
					new java.awt.Dimension(258, 24));
				ivjJPanel6.setBounds(5, 118, 302, 27);
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
					getstcLblDash(),
					getstcLblDash().getName());
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
				// user code end
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
	 * Return the ivjJScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("ivjJScrollPane1");
				ivjJScrollPane1.setBounds(1, 4, 311, 81);
				getJScrollPane1().setViewportView(getlstIndicator());
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
		return ivjJScrollPane1;
	}

	/**
	 * Return the ivjlblAge property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAge()
	{
		if (ivjlblAge == null)
		{
			try
			{
				ivjlblAge = new JLabel();
				ivjlblAge.setName("ivjlblAge");
				ivjlblAge.setText(DFLT_2DIGIT);
				ivjlblAge.setMaximumSize(new java.awt.Dimension(7, 14));
				ivjlblAge.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblAge.setBounds(186, 25, 23, 14);
				ivjlblAge.setMinimumSize(new java.awt.Dimension(7, 14));
				ivjlblAge.setHorizontalAlignment(SwingConstants.RIGHT);
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
		return ivjlblAge;
	}

	/**
	 * Return the ivjlblBodyVIN property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblBodyVIN()
	{
		if (ivjlblBodyVIN == null)
		{
			try
			{
				ivjlblBodyVIN = new JLabel();
				ivjlblBodyVIN.setName("ivjlblBodyVIN");
				ivjlblBodyVIN.setText(VIN);
				ivjlblBodyVIN.setMaximumSize(
					new java.awt.Dimension(19, 14));
				ivjlblBodyVIN.setMinimumSize(
					new java.awt.Dimension(19, 14));
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
		return ivjlblBodyVIN;
	}

	/**
	 * Return the ivjlblCapacity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCapacity()
	{
		if (ivjlblCapacity == null)
		{
			try
			{
				ivjlblCapacity = new JLabel();
				ivjlblCapacity.setName("ivjlblCapacity");
				ivjlblCapacity.setText(DFLT_YEAR);
				ivjlblCapacity.setMaximumSize(
					new java.awt.Dimension(59, 14));
				ivjlblCapacity.setHorizontalTextPosition(
					SwingConstants.RIGHT);
				ivjlblCapacity.setBounds(80, 33, 53, 14);
				ivjlblCapacity.setMinimumSize(
					new java.awt.Dimension(59, 14));
				ivjlblCapacity.setHorizontalAlignment(
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
		return ivjlblCapacity;
	}

	/**
	 * Return the ivjlblClass property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblClass()
	{
		if (ivjlblClass == null)
		{
			try
			{
				ivjlblClass = new JLabel();
				ivjlblClass.setName("ivjlblClass");
				ivjlblClass.setText(DFLT_REG_CLASS);
				ivjlblClass.setMaximumSize(
					new java.awt.Dimension(60, 14));
				ivjlblClass.setMinimumSize(
					new java.awt.Dimension(60, 14));
				ivjlblClass.setBounds(78, 73, 176, 14);
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
		return ivjlblClass;
	}

	/**
	 * Return the ivjlblCounty property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCounty()
	{
		if (ivjlblCounty == null)
		{
			try
			{
				ivjlblCounty = new JLabel();
				ivjlblCounty.setName("ivjlblCounty");
				ivjlblCounty.setText(DFLT_CNTY);
				ivjlblCounty.setMaximumSize(
					new java.awt.Dimension(39, 14));
				ivjlblCounty.setMinimumSize(
					new java.awt.Dimension(39, 14));
				ivjlblCounty.setBounds(78, 141, 164, 14);
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
		return ivjlblCounty;
	}

	/**
	 * Return the ivjlblDocTypeCdDescription property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDocTypeCdDescription()
	{
		if (ivjlblDocTypeCdDescription == null)
		{
			try
			{
				ivjlblDocTypeCdDescription = new JLabel();
				ivjlblDocTypeCdDescription.setName(
					"ivjlblDocTypeCdDescription");
				ivjlblDocTypeCdDescription.setText(
					DFLT_DOCTYPE_CD_DESC);
				ivjlblDocTypeCdDescription.setMaximumSize(
					new java.awt.Dimension(52, 14));
				ivjlblDocTypeCdDescription.setMinimumSize(
					new java.awt.Dimension(52, 14));
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
		return ivjlblDocTypeCdDescription;
	}

	/**
	 * Return the ivjlblDocumentNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDocumentNo()
	{
		if (ivjlblDocumentNo == null)
		{
			try
			{
				ivjlblDocumentNo = new JLabel();
				ivjlblDocumentNo.setName("ivjlblDocumentNo");
				ivjlblDocumentNo.setText(DFLT_DOCNO);
				ivjlblDocumentNo.setMaximumSize(
					new java.awt.Dimension(68, 14));
				ivjlblDocumentNo.setMinimumSize(
					new java.awt.Dimension(68, 14));
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
		return ivjlblDocumentNo;
	}

	/**
	 * Return the ivjlblEmpty property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblEmpty()
	{
		if (ivjlblEmpty == null)
		{
			try
			{
				ivjlblEmpty = new JLabel();
				ivjlblEmpty.setName("ivjlblEmpty");
				ivjlblEmpty.setText(DFLT_YEAR);
				ivjlblEmpty.setMaximumSize(
					new java.awt.Dimension(55, 14));
				ivjlblEmpty.setHorizontalTextPosition(
					SwingConstants.CENTER);
				ivjlblEmpty.setBounds(80, 16, 53, 14);
				ivjlblEmpty.setMinimumSize(
					new java.awt.Dimension(55, 14));
				ivjlblEmpty.setHorizontalAlignment(
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
		return ivjlblEmpty;
	}

	/**
	 * Return the ivjlblExpirationMonth property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblExpirationMonth()
	{
		if (ivjlblExpirationMonth == null)
		{
			try
			{
				ivjlblExpirationMonth = new JLabel();
				ivjlblExpirationMonth.setName("ivjlblExpirationMonth");
				ivjlblExpirationMonth.setText(DFLT_2DIGIT);
				ivjlblExpirationMonth.setMaximumSize(
					new java.awt.Dimension(41, 14));
				ivjlblExpirationMonth.setMinimumSize(
					new java.awt.Dimension(41, 14));
				ivjlblExpirationMonth.setBounds(78, 51, 19, 14);
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
		return ivjlblExpirationMonth;
	}

	/**
	 * Return the ivjlblExpirationYear property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblExpirationYear()
	{
		if (ivjlblExpirationYear == null)
		{
			try
			{
				ivjlblExpirationYear = new JLabel();
				ivjlblExpirationYear.setSize(35, 14);
				ivjlblExpirationYear.setName("ivjlblExpirationYear");
				ivjlblExpirationYear.setText(DFLT_YEAR);
				ivjlblExpirationYear.setMaximumSize(
					new java.awt.Dimension(36, 14));
				ivjlblExpirationYear.setMinimumSize(
					new java.awt.Dimension(36, 14));
				ivjlblExpirationYear.setLocation(108, 51);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjlblExpirationYear;
	}

	/**
	 * Return the ivjlblGross property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblGross()
	{
		if (ivjlblGross == null)
		{
			try
			{
				ivjlblGross = new JLabel();
				ivjlblGross.setName("ivjlblGross");
				ivjlblGross.setText(DFLT_YEAR);
				ivjlblGross.setMaximumSize(
					new java.awt.Dimension(54, 14));
				ivjlblGross.setHorizontalTextPosition(
					SwingConstants.CENTER);
				ivjlblGross.setBounds(80, 50, 53, 14);
				ivjlblGross.setMinimumSize(
					new java.awt.Dimension(54, 14));
				ivjlblGross.setHorizontalAlignment(
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
		return ivjlblGross;
	}

	/**
	 * Return the ivjlblIssued property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblIssued()
	{
		if (ivjlblIssued == null)
		{
			try
			{
				ivjlblIssued = new JLabel();
				ivjlblIssued.setName("ivjlblIssued");
				ivjlblIssued.setText(DFLT_ISSUE_DT);
				ivjlblIssued.setMaximumSize(
					new java.awt.Dimension(60, 14));
				ivjlblIssued.setMinimumSize(
					new java.awt.Dimension(60, 14));
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
		return ivjlblIssued;
	}

	/**
	 * Return the ivjlblMake property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblMake()
	{
		if (ivjlblMake == null)
		{
			try
			{
				ivjlblMake = new JLabel();
				ivjlblMake.setName("ivjlblMake");
				ivjlblMake.setText(DFLT_MODEL);
				ivjlblMake.setMaximumSize(
					new java.awt.Dimension(56, 14));
				ivjlblMake.setMinimumSize(
					new java.awt.Dimension(56, 14));
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
		return ivjlblMake;
	}

	/**
	 * Return the ivjlblOrg property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblOrg()
	{
		if (ivjlblOrg == null)
		{
			ivjlblOrg = new JLabel();
			ivjlblOrg.setBounds(78, 120, 164, 14);
			ivjlblOrg.setText(DFLT_ORGANIZATION);
			ivjlblOrg.setName("ivjlblOrg");
		}
		return ivjlblOrg;
	}

	/**
	 * Return the ivjlblOwnerId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblOwnerId()
	{
		if (ivjlblOwnerId == null)
		{
			try
			{
				ivjlblOwnerId = new JLabel();
				ivjlblOwnerId.setName("ivjlblOwnerId");
				ivjlblOwnerId.setText(DFLT_OWNER_ID);
				ivjlblOwnerId.setMaximumSize(
					new java.awt.Dimension(52, 14));
				ivjlblOwnerId.setMinimumSize(
					new java.awt.Dimension(52, 14));
				ivjlblOwnerId.setBounds(36, 14, 126, 14);
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
		return ivjlblOwnerId;
	}

	/**
	 * Return the ivjlblPlate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlate()
	{
		if (ivjlblPlate == null)
		{
			try
			{
				ivjlblPlate = new JLabel();
				ivjlblPlate.setName("ivjlblPlate");
				ivjlblPlate.setText(DFLT_PLATE_NO);
				ivjlblPlate.setMaximumSize(
					new java.awt.Dimension(39, 14));
				ivjlblPlate.setMinimumSize(
					new java.awt.Dimension(39, 14));
				ivjlblPlate.setBounds(78, 25, 65, 14);
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
		return ivjlblPlate;
	}

	/**
	 * Return the ivjlblTempCarryingCapacity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTempCarryingCapacity()
	{
		if (ivjlblTempCarryingCapacity == null)
		{
			try
			{
				ivjlblTempCarryingCapacity = new JLabel();
				ivjlblTempCarryingCapacity.setBounds(187, 27, 70, 14);
				ivjlblTempCarryingCapacity.setName(
					"ivjlblTempCarryingCapacity");
				ivjlblTempCarryingCapacity.setText(DFLT_2DIGIT);
				ivjlblTempCarryingCapacity.setMaximumSize(
					new java.awt.Dimension(59, 14));
				ivjlblTempCarryingCapacity.setHorizontalTextPosition(
					SwingConstants.RIGHT);
				ivjlblTempCarryingCapacity.setMinimumSize(
					new java.awt.Dimension(59, 14));
				ivjlblTempCarryingCapacity.setHorizontalAlignment(
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
		return ivjlblTempCarryingCapacity;
	}

	/**
	 * Return the ivjlblTempGrossWeight property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTempGrossWeight()
	{
		if (ivjlblTempGrossWeight == null)
		{
			try
			{
				ivjlblTempGrossWeight = new JLabel();
				ivjlblTempGrossWeight.setBounds(187, 48, 70, 14);
				ivjlblTempGrossWeight.setName("ivjlblTempGrossWeight");
				ivjlblTempGrossWeight.setText(DFLT_2DIGIT);
				ivjlblTempGrossWeight.setMaximumSize(
					new java.awt.Dimension(54, 14));
				ivjlblTempGrossWeight.setHorizontalTextPosition(
					SwingConstants.LEFT);
				ivjlblTempGrossWeight.setMinimumSize(
					new java.awt.Dimension(54, 14));
				ivjlblTempGrossWeight.setHorizontalAlignment(
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
		return ivjlblTempGrossWeight;
	}

	/**
	 * Return the ivjlblTons property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblTons()
	{
		if (ivjlblTons == null)
		{
			try
			{
				ivjlblTons = new JLabel();
				ivjlblTons.setName("ivjlblTons");
				ivjlblTons.setText(DFLT_VEH_LB);
				ivjlblTons.setMaximumSize(
					new java.awt.Dimension(42, 14));
				ivjlblTons.setMinimumSize(
					new java.awt.Dimension(42, 14));
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
		return ivjlblTons;
	}

	/**
	 * Return the ivjlblType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblType()
	{
		if (ivjlblType == null)
		{
			try
			{
				ivjlblType = new JLabel();
				ivjlblType.setName("ivjlblType");
				ivjlblType.setText(DFLT_PLATE_TYPE);
				ivjlblType.setMaximumSize(
					new java.awt.Dimension(59, 14));
				ivjlblType.setMinimumSize(
					new java.awt.Dimension(59, 14));
				ivjlblType.setBounds(78, 97, 164, 14);
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
		return ivjlblType;
	}

	/**
	 * Return the ivjlblUnitNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblUnitNo()
	{
		if (ivjlblUnitNo == null)
		{
			try
			{
				ivjlblUnitNo = new JLabel();
				ivjlblUnitNo.setName("ivjlblUnitNo");
				ivjlblUnitNo.setText(DFLT_UNIT);
				ivjlblUnitNo.setMaximumSize(
					new java.awt.Dimension(32, 14));
				ivjlblUnitNo.setMinimumSize(
					new java.awt.Dimension(32, 14));
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
		return ivjlblUnitNo;
	}

	/**
	 * Return the lblVehBodyType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVehBodyType()
	{
		if (ivjlblVehBodyType == null)
		{
			try
			{
				ivjlblVehBodyType = new JLabel();
				ivjlblVehBodyType.setName("ivjlblVehBodyType");
				ivjlblVehBodyType.setText(DFLT_BODY);
				ivjlblVehBodyType.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblVehBodyType.setMinimumSize(
					new java.awt.Dimension(45, 14));
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
		return ivjlblVehBodyType;
	}

	/**
	 * Return the ivjlblVehicleClass property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVehicleClass()
	{
		if (ivjlblVehicleClass == null)
		{
			try
			{
				ivjlblVehicleClass = new JLabel();
				ivjlblVehicleClass.setName("ivjlblVehicleClass");
				ivjlblVehicleClass.setText(DFLT_VEH_CLASS);
				ivjlblVehicleClass.setMaximumSize(
					new java.awt.Dimension(57, 14));
				ivjlblVehicleClass.setMinimumSize(
					new java.awt.Dimension(57, 14));
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
		return ivjlblVehicleClass;
	}

	/**
	 * Return the ivjlblVehModel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVehModel()
	{
		if (ivjlblVehModel == null)
		{
			try
			{
				ivjlblVehModel = new JLabel();
				ivjlblVehModel.setName("ivjlblVehModel");
				ivjlblVehModel.setText(DFLT_MODEL);
				ivjlblVehModel.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblVehModel.setMinimumSize(
					new java.awt.Dimension(45, 14));
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
		return ivjlblVehModel;
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
				ivjlblVIN.setName("ivjlblVIN");
				ivjlblVIN.setText(DFLT_VIN);
				ivjlblVIN.setMaximumSize(
					new java.awt.Dimension(45, 14));
				ivjlblVIN.setMinimumSize(
					new java.awt.Dimension(45, 14));
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
	* Return the ivjlblYear property value.
	* 
	* @return JLabel
	*/
	private JLabel getlblYear()
	{
		if (ivjlblYear == null)
		{
			try
			{
				ivjlblYear = new JLabel();
				ivjlblYear.setName("ivjlblYear");
				ivjlblYear.setText(DFLT_YEAR);
				ivjlblYear.setMaximumSize(
					new java.awt.Dimension(37, 14));
				ivjlblYear.setMinimumSize(
					new java.awt.Dimension(37, 14));
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
		return ivjlblYear;
	}

	/**
	 * Return the ivjlstIndicator property value.
	 * 
	 * @return JList
	 */
	private JList getlstIndicator()
	{
		if (ivjlstIndicator == null)
		{
			try
			{
				ivjlstIndicator = new JList();
				ivjlstIndicator.setName("ivjlstIndicator");
				// defect 9971 
				ivjlstIndicator.setFont(
					new java.awt.Font(
						CommonConstant.FONT_JLIST,
						0,
						12));
				// end defect 9971 
				ivjlstIndicator.setBounds(-10, 0, 295, 62);
				ivjlstIndicator.setSelectedIndex(1);
				ivjlstIndicator.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
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
		return ivjlstIndicator;
	}

	/**
	 * Return the ivjpnlDocument property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlDocument()
	{
		if (ivjpnlDocument == null)
		{
			try
			{
				ivjpnlDocument = new JPanel();
				ivjpnlDocument.setName("ivjpnlDocument");
				ivjpnlDocument.setBorder(
					new TitledBorder(new EtchedBorder(), DOCUMENT));
				ivjpnlDocument.setLayout(new java.awt.GridBagLayout());
				ivjpnlDocument.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlDocument.setMinimumSize(
					new java.awt.Dimension(288, 89));
				ivjpnlDocument.setBounds(274, 158, 312, 74);
				java.awt.GridBagConstraints constraintsstcLblDocumentNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblDocumentNo.gridx = 1;
				constraintsstcLblDocumentNo.gridy = 1;
				constraintsstcLblDocumentNo.ipadx = 5;
				constraintsstcLblDocumentNo.insets =
					new java.awt.Insets(11, 35, 0, 5);
				getpnlDocument().add(
					getstcLblDocumentNo(),
					constraintsstcLblDocumentNo);
				java.awt.GridBagConstraints constraintslblDocumentNo =
					new java.awt.GridBagConstraints();
				constraintslblDocumentNo.gridx = 2;
				constraintslblDocumentNo.gridy = 1;
				constraintslblDocumentNo.ipadx = 64;
				constraintslblDocumentNo.insets =
					new java.awt.Insets(11, 5, 0, 51);
				getpnlDocument().add(
					getlblDocumentNo(),
					constraintslblDocumentNo);
				java.awt.GridBagConstraints constraintsstcLblIssued =
					new java.awt.GridBagConstraints();
				constraintsstcLblIssued.gridx = 1;
				constraintsstcLblIssued.gridy = 2;
				constraintsstcLblIssued.ipadx = 4;
				constraintsstcLblIssued.insets =
					new java.awt.Insets(1, 74, 0, 5);
				getpnlDocument().add(
					getstcLblIssued(),
					constraintsstcLblIssued);
				java.awt.GridBagConstraints constraintslblIssued =
					new java.awt.GridBagConstraints();
				constraintslblIssued.gridx = 2;
				constraintslblIssued.gridy = 2;
				constraintslblIssued.ipadx = 43;
				constraintslblIssued.insets =
					new java.awt.Insets(1, 5, 0, 80);
				getpnlDocument().add(
					getlblIssued(),
					constraintslblIssued);
				java
					.awt
					.GridBagConstraints constraintslblDocTypeCdDescription =
					new java.awt.GridBagConstraints();
				constraintslblDocTypeCdDescription.gridx = 1;
				constraintslblDocTypeCdDescription.gridy = 3;
				constraintslblDocTypeCdDescription.gridwidth = 2;
				constraintslblDocTypeCdDescription.ipadx = 192;
				constraintslblDocTypeCdDescription.insets =
					new java.awt.Insets(0, 39, 12, 29);
				getpnlDocument().add(
					getlblDocTypeCdDescription(),
					constraintslblDocTypeCdDescription);
				// user code begin {1}
				Border bdrDocument =
					new TitledBorder(new EtchedBorder(), DOCUMENT);
				ivjpnlDocument.setBorder(bdrDocument);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjpnlDocument;
	}

	/**
	 * Return the ivjpnlOwner property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlOwner()
	{
		if (ivjpnlOwner == null)
		{
			try
			{
				ivjpnlOwner = new JPanel();
				ivjpnlOwner.setName("ivjpnlOwner");
				ivjpnlOwner.setBorder(
					new TitledBorder(new EtchedBorder(), OWNER));
				ivjpnlOwner.setLayout(null);
				ivjpnlOwner.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlOwner.setMinimumSize(
					new java.awt.Dimension(369, 109));
				ivjpnlOwner.setBounds(274, 2, 312, 153);
				getpnlOwner().add(
					getstcLblId(),
					getstcLblId().getName());
				getpnlOwner().add(getchkUSA(), getchkUSA().getName());
				getpnlOwner().add(
					gettxtOwnerName1(),
					gettxtOwnerName1().getName());
				getpnlOwner().add(
					gettxtOwnerName2(),
					gettxtOwnerName2().getName());
				getpnlOwner().add(
					gettxtOwnerStreet1(),
					gettxtOwnerStreet1().getName());
				getpnlOwner().add(
					gettxtOwnerStreet2(),
					gettxtOwnerStreet2().getName());
				getpnlOwner().add(
					getlblOwnerId(),
					getlblOwnerId().getName());
				getpnlOwner().add(getJPanel6(), getJPanel6().getName());
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
		return ivjpnlOwner;
	}

	/**
	 * Return the ivjpnlRegistration property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlRegistration()
	{
		if (ivjpnlRegistration == null)
		{
			try
			{
				ivjpnlRegistration = new JPanel();
				ivjpnlRegistration.setName("ivjpnlRegistration");
				ivjpnlRegistration.setBorder(
					new TitledBorder(new EtchedBorder(), REGIS));
				ivjpnlRegistration.setLayout(null);
				ivjpnlRegistration.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlRegistration.setMinimumSize(
					new java.awt.Dimension(302, 162));
				ivjpnlRegistration.setBounds(6, 7, 255, 163);
				getpnlRegistration().add(
					getstcLblPlate(),
					getstcLblPlate().getName());
				getpnlRegistration().add(
					getlblPlate(),
					getlblPlate().getName());
				getpnlRegistration().add(
					getstcLblAge(),
					getstcLblAge().getName());
				getpnlRegistration().add(
					getlblAge(),
					getlblAge().getName());
				getpnlRegistration().add(
					getstcLblExpires(),
					getstcLblExpires().getName());
				getpnlRegistration().add(
					getlblExpirationMonth(),
					getlblExpirationMonth().getName());
				getpnlRegistration().add(
					getstcLblClass(),
					getstcLblClass().getName());
				getpnlRegistration().add(
					getlblClass(),
					getlblClass().getName());
				getpnlRegistration().add(
					getstcLblType(),
					getstcLblType().getName());
				getpnlRegistration().add(
					getlblType(),
					getlblType().getName());
				getpnlRegistration().add(
					getlblExpirationYear(),
					getlblExpirationYear().getName());
				getpnlRegistration().add(
					getstcLblCounty(),
					getstcLblCounty().getName());
				getpnlRegistration().add(
					getlblCounty(),
					getlblCounty().getName());
				// defect 8900
				getpnlRegistration().add(getivjlblSlash(), null);
				// end defect 8900
				ivjpnlRegistration.add(getstcLblOrg(), null);
				ivjpnlRegistration.add(getlblOrg(), null);
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
		return ivjpnlRegistration;
	}

	/**
	 * Return the ivjpnlVehicle property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlVehicle()
	{
		if (ivjpnlVehicle == null)
		{
			try
			{
				ivjpnlVehicle = new JPanel();
				ivjpnlVehicle.setName("ivjpnlVehicle");
				ivjpnlVehicle.setBorder(
					new TitledBorder(new EtchedBorder(), VEHICLE));
				ivjpnlVehicle.setLayout(new java.awt.GridBagLayout());
				ivjpnlVehicle.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlVehicle.setMinimumSize(
					new java.awt.Dimension(254, 177));
				ivjpnlVehicle.setBounds(6, 177, 255, 149);
				java.awt.GridBagConstraints constraintslblYear =
					new java.awt.GridBagConstraints();
				constraintslblYear.gridx = 1;
				constraintslblYear.gridy = 1;
				constraintslblYear.ipadx = 16;
				constraintslblYear.insets =
					new java.awt.Insets(14, 17, 4, 5);
				getpnlVehicle().add(getlblYear(), constraintslblYear);
				java.awt.GridBagConstraints constraintslblMake =
					new java.awt.GridBagConstraints();
				constraintslblMake.gridx = 2;
				constraintslblMake.gridy = 1;
				constraintslblMake.ipadx = 9;
				constraintslblMake.insets =
					new java.awt.Insets(14, 6, 4, 4);
				getpnlVehicle().add(getlblMake(), constraintslblMake);
				java.awt.GridBagConstraints constraintslblVehModel =
					new java.awt.GridBagConstraints();
				constraintslblVehModel.gridx = 4;
				constraintslblVehModel.gridy = 1;
				constraintslblVehModel.ipadx = -4;
				constraintslblVehModel.insets =
					new java.awt.Insets(14, 4, 4, 15);
				getpnlVehicle().add(
					getlblVehModel(),
					constraintslblVehModel);
				java.awt.GridBagConstraints constraintslblVIN =
					new java.awt.GridBagConstraints();
				constraintslblVIN.gridx = 1;
				constraintslblVIN.gridy = 2;
				constraintslblVIN.gridwidth = 4;
				constraintslblVIN.ipadx = 176;
				constraintslblVIN.insets =
					new java.awt.Insets(4, 15, 3, 19);
				getpnlVehicle().add(getlblVIN(), constraintslblVIN);
				java.awt.GridBagConstraints constraintsstcLblBodyVIN =
					new java.awt.GridBagConstraints();
				constraintsstcLblBodyVIN.gridx = 1;
				constraintsstcLblBodyVIN.gridy = 3;
				constraintsstcLblBodyVIN.ipadx = 3;
				constraintsstcLblBodyVIN.insets =
					new java.awt.Insets(4, 14, 3, 5);
				getpnlVehicle().add(
					getstcLblBodyVIN(),
					constraintsstcLblBodyVIN);
				java
					.awt
					.GridBagConstraints constraintsstcLblVehicleClass =
					new java.awt.GridBagConstraints();
				constraintsstcLblVehicleClass.gridx = 1;
				constraintsstcLblVehicleClass.gridy = 4;
				constraintsstcLblVehicleClass.ipadx = 21;
				constraintsstcLblVehicleClass.insets =
					new java.awt.Insets(4, 14, 2, 5);
				getpnlVehicle().add(
					getstcLblVehicleClass(),
					constraintsstcLblVehicleClass);
				java.awt.GridBagConstraints constraintsstcLblTons =
					new java.awt.GridBagConstraints();
				constraintsstcLblTons.gridx = 1;
				constraintsstcLblTons.gridy = 5;
				constraintsstcLblTons.ipadx = 26;
				constraintsstcLblTons.insets =
					new java.awt.Insets(3, 13, 4, 5);
				getpnlVehicle().add(
					getstcLblTons(),
					constraintsstcLblTons);
				java.awt.GridBagConstraints constraintsstcLblUnitNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblUnitNo.gridx = 1;
				constraintsstcLblUnitNo.gridy = 6;
				constraintsstcLblUnitNo.ipadx = 14;
				constraintsstcLblUnitNo.insets =
					new java.awt.Insets(4, 13, 16, 5);
				getpnlVehicle().add(
					getstcLblUnitNo(),
					constraintsstcLblUnitNo);
				java.awt.GridBagConstraints constraintslblBodyVIN =
					new java.awt.GridBagConstraints();
				constraintslblBodyVIN.gridx = 2;
				constraintslblBodyVIN.gridy = 3;
				constraintslblBodyVIN.gridwidth = 3;
				constraintslblBodyVIN.ipadx = 129;
				constraintslblBodyVIN.insets =
					new java.awt.Insets(4, 7, 3, 25);
				getpnlVehicle().add(
					getlblBodyVIN(),
					constraintslblBodyVIN);
				java.awt.GridBagConstraints constraintslblVehicleClass =
					new java.awt.GridBagConstraints();
				constraintslblVehicleClass.gridx = 2;
				constraintslblVehicleClass.gridy = 4;
				constraintslblVehicleClass.gridwidth = 3;
				constraintslblVehicleClass.ipadx = 89;
				constraintslblVehicleClass.insets =
					new java.awt.Insets(4, 7, 2, 27);
				getpnlVehicle().add(
					getlblVehicleClass(),
					constraintslblVehicleClass);
				java.awt.GridBagConstraints constraintslblTons =
					new java.awt.GridBagConstraints();
				constraintslblTons.gridx = 2;
				constraintslblTons.gridy = 5;
				constraintslblTons.gridwidth = 3;
				constraintslblTons.ipadx = 104;
				constraintslblTons.insets =
					new java.awt.Insets(3, 7, 4, 27);
				getpnlVehicle().add(getlblTons(), constraintslblTons);
				java.awt.GridBagConstraints constraintslblUnitNo =
					new java.awt.GridBagConstraints();
				constraintslblUnitNo.gridx = 2;
				constraintslblUnitNo.gridy = 6;
				constraintslblUnitNo.ipadx = 10;
				constraintslblUnitNo.insets =
					new java.awt.Insets(4, 7, 16, 26);
				getpnlVehicle().add(
					getlblUnitNo(),
					constraintslblUnitNo);
				java.awt.GridBagConstraints constraintslblVehBodyType =
					new java.awt.GridBagConstraints();
				constraintslblVehBodyType.gridx = 3;
				constraintslblVehBodyType.gridy = 1;
				constraintslblVehBodyType.ipadx = -8;
				constraintslblVehBodyType.insets =
					new java.awt.Insets(14, 5, 4, 3);
				getpnlVehicle().add(
					getlblVehBodyType(),
					constraintslblVehBodyType);
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
		return ivjpnlVehicle;
	}

	/**
	 * Return the ivjpnlWeight property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlWeight()
	{
		if (ivjpnlWeight == null)
		{
			try
			{
				ivjpnlWeight = new JPanel();
				ivjpnlWeight.setName("ivjpnlWeight");
				ivjpnlWeight.setBorder(
					new TitledBorder(new EtchedBorder(), WEIGHT));
				ivjpnlWeight.setLayout(null);
				ivjpnlWeight.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlWeight.setMinimumSize(
					new java.awt.Dimension(248, 99));
				ivjpnlWeight.setBounds(6, 332, 155, 74);
				getpnlWeight().add(
					getstcLblEmpty(),
					getstcLblEmpty().getName());
				getpnlWeight().add(
					getstcLblCapacity(),
					getstcLblCapacity().getName());
				getpnlWeight().add(
					getstcLblGross(),
					getstcLblGross().getName());
				getpnlWeight().add(
					getlblEmpty(),
					getlblEmpty().getName());
				getpnlWeight().add(
					getlblCapacity(),
					getlblCapacity().getName());
				getpnlWeight().add(
					getlblGross(),
					getlblGross().getName());
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
		return ivjpnlWeight;
	}

	/**
	 * Return the ivjstcLblAge property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAge()
	{
		if (ivjstcLblAge == null)
		{
			try
			{
				ivjstcLblAge = new JLabel();
				ivjstcLblAge.setName("ivjstcLblAge");
				ivjstcLblAge.setText(AGE);
				ivjstcLblAge.setMaximumSize(
					new java.awt.Dimension(25, 14));
				ivjstcLblAge.setMinimumSize(
					new java.awt.Dimension(25, 14));
				ivjstcLblAge.setHorizontalAlignment(4);
				ivjstcLblAge.setBounds(144, 25, 35, 14);
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
		return ivjstcLblAge;
	}

	/**
	 * Return the ivjstcLblBodyVIN property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBodyVIN()
	{
		if (ivjstcLblBodyVIN == null)
		{
			try
			{
				ivjstcLblBodyVIN = new JLabel();
				ivjstcLblBodyVIN.setName("ivjstcLblBodyVIN");
				ivjstcLblBodyVIN.setText(BODY_VIN);
				ivjstcLblBodyVIN.setMaximumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblBodyVIN.setMinimumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblBodyVIN.setHorizontalAlignment(4);
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
		return ivjstcLblBodyVIN;
	}

	/**
	 * Return the ivjstcLblCapacity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCapacity()
	{
		if (ivjstcLblCapacity == null)
		{
			try
			{
				ivjstcLblCapacity = new JLabel();
				ivjstcLblCapacity.setName("ivjstcLblCapacity");
				ivjstcLblCapacity.setText(CAPACITY);
				ivjstcLblCapacity.setMaximumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblCapacity.setMinimumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblCapacity.setBounds(15, 33, 57, 14);
				ivjstcLblCapacity.setHorizontalAlignment(
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
		return ivjstcLblCapacity;
	}

	/**
	 * Return the ivjstcLblClass property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblClass()
	{
		if (ivjstcLblClass == null)
		{
			try
			{
				ivjstcLblClass = new JLabel();
				ivjstcLblClass.setName("ivjstcLblClass");
				ivjstcLblClass.setText(CLASS);
				ivjstcLblClass.setMaximumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblClass.setMinimumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblClass.setHorizontalAlignment(4);
				ivjstcLblClass.setBounds(29, 73, 40, 14);
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
		return ivjstcLblClass;
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
				ivjstcLblCounty.setText(COUNTY);
				ivjstcLblCounty.setMaximumSize(
					new java.awt.Dimension(42, 14));
				ivjstcLblCounty.setMinimumSize(
					new java.awt.Dimension(42, 14));
				ivjstcLblCounty.setHorizontalAlignment(4);
				ivjstcLblCounty.setBounds(23, 141, 46, 14);
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
		return ivjstcLblCounty;
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
				ivjstcLblDash.setName("ivjstcLblDash");
				ivjstcLblDash.setText(DASH);
				ivjstcLblDash.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setBounds(239, 3, 8, 14);
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
	 * Return the ivjstcLblDocumentNo property value.
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
				ivjstcLblDocumentNo.setText(DOCNO);
				ivjstcLblDocumentNo.setMaximumSize(
					new java.awt.Dimension(79, 14));
				ivjstcLblDocumentNo.setMinimumSize(
					new java.awt.Dimension(79, 14));
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
		return ivjstcLblDocumentNo;
	}

	/**
	 * Return the ivjstcLblEmpty property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEmpty()
	{
		if (ivjstcLblEmpty == null)
		{
			try
			{
				ivjstcLblEmpty = new JLabel();
				ivjstcLblEmpty.setName("ivjstcLblEmpty");
				ivjstcLblEmpty.setText(EMPTY);
				ivjstcLblEmpty.setMaximumSize(
					new java.awt.Dimension(38, 14));
				ivjstcLblEmpty.setMinimumSize(
					new java.awt.Dimension(38, 14));
				ivjstcLblEmpty.setBounds(15, 16, 57, 14);
				ivjstcLblEmpty.setHorizontalAlignment(
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
		return ivjstcLblEmpty;
	}

	/**
	 * Return the ivjstcLblExpires property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblExpires()
	{
		if (ivjstcLblExpires == null)
		{
			try
			{
				ivjstcLblExpires = new JLabel();
				ivjstcLblExpires.setName("ivjstcLblExpires");
				ivjstcLblExpires.setText(EXPIRES);
				ivjstcLblExpires.setMaximumSize(
					new java.awt.Dimension(46, 14));
				ivjstcLblExpires.setMinimumSize(
					new java.awt.Dimension(46, 14));
				ivjstcLblExpires.setHorizontalAlignment(4);
				ivjstcLblExpires.setBounds(20, 51, 49, 14);
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
		return ivjstcLblExpires;
	}

	/**
	 * Return the ivjstcLblGross property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblGross()
	{
		if (ivjstcLblGross == null)
		{
			try
			{
				ivjstcLblGross = new JLabel();
				ivjstcLblGross.setName("ivjstcLblGross");
				ivjstcLblGross.setText(GROSS);
				ivjstcLblGross.setMaximumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblGross.setMinimumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblGross.setBounds(15, 50, 57, 14);
				ivjstcLblGross.setHorizontalAlignment(
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
		return ivjstcLblGross;
	}

	/**
	 * Return the ivjstcLblId property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblId()
	{
		if (ivjstcLblId == null)
		{
			try
			{
				ivjstcLblId = new JLabel();
				ivjstcLblId.setName("ivjstcLblId");
				ivjstcLblId.setText(ID);
				ivjstcLblId.setMaximumSize(
					new java.awt.Dimension(13, 14));
				ivjstcLblId.setMinimumSize(
					new java.awt.Dimension(13, 14));
				ivjstcLblId.setBounds(7, 14, 24, 14);
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
		return ivjstcLblId;
	}

	/**
	 * Return the ivjstcLblIssued property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblIssued()
	{
		if (ivjstcLblIssued == null)
		{
			try
			{
				ivjstcLblIssued = new JLabel();
				ivjstcLblIssued.setName("ivjstcLblIssued");
				ivjstcLblIssued.setText(ISSUED);
				ivjstcLblIssued.setMaximumSize(
					new java.awt.Dimension(41, 14));
				ivjstcLblIssued.setMinimumSize(
					new java.awt.Dimension(41, 14));
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
		return ivjstcLblIssued;
	}

	/**
	 * Return the ivjstcLblOrg property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOrg()
	{
		if (ivjstcLblOrg == null)
		{
			ivjstcLblOrg = new JLabel();
			ivjstcLblOrg.setBounds(35, 120, 34, 14);
			ivjstcLblOrg.setText(ORG);
			ivjstcLblOrg.setHorizontalAlignment(SwingConstants.RIGHT);
			ivjstcLblOrg.setName("ivjstcLblOrg");
		}
		return ivjstcLblOrg;
	}

	/**
	 * Return the ivjstcLblPlate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPlate()
	{
		if (ivjstcLblPlate == null)
		{
			try
			{
				ivjstcLblPlate = new JLabel();
				ivjstcLblPlate.setName("ivjstcLblPlate");
				ivjstcLblPlate.setText(PLATE);
				ivjstcLblPlate.setMaximumSize(
					new java.awt.Dimension(32, 14));
				ivjstcLblPlate.setMinimumSize(
					new java.awt.Dimension(32, 14));
				ivjstcLblPlate.setHorizontalAlignment(4);
				ivjstcLblPlate.setBounds(34, 25, 35, 14);
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
		return ivjstcLblPlate;
	}

	/**
	 * Return the ivjstcLblTempAddlWeight property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTempAddlWeight()
	{
		if (ivjstcLblTempAddlWeight == null)
		{
			try
			{
				ivjstcLblTempAddlWeight = new JLabel();
				ivjstcLblTempAddlWeight.setBounds(45, 6, 110, 14);
				ivjstcLblTempAddlWeight.setName(
					"ivjstcLblTempAddlWeight");
				ivjstcLblTempAddlWeight.setText(TEMP_ADDL_WT);
				ivjstcLblTempAddlWeight.setMaximumSize(
					new java.awt.Dimension(106, 14));
				ivjstcLblTempAddlWeight.setMinimumSize(
					new java.awt.Dimension(106, 14));
				ivjstcLblTempAddlWeight.setHorizontalAlignment(4);
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
		return ivjstcLblTempAddlWeight;
	}

	/**
	 * Return the ivjstcLblTempCarryingCapacity property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTempCarryingCapacity()
	{
		if (ivjstcLblTempCarryingCapacity == null)
		{
			try
			{
				ivjstcLblTempCarryingCapacity = new JLabel();
				ivjstcLblTempCarryingCapacity.setBounds(
					13,
					27,
					142,
					14);
				ivjstcLblTempCarryingCapacity.setName(
					"ivjstcLblTempCarryingCapacity");
				ivjstcLblTempCarryingCapacity.setText(TEMP_CARRYCAP);
				ivjstcLblTempCarryingCapacity.setMaximumSize(
					new java.awt.Dimension(138, 14));
				ivjstcLblTempCarryingCapacity.setMinimumSize(
					new java.awt.Dimension(138, 14));
				ivjstcLblTempCarryingCapacity.setHorizontalAlignment(4);
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
		return ivjstcLblTempCarryingCapacity;
	}

	/**
	 * Return the ivjstcLblTempGrossWeight property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTempGrossWeight()
	{
		if (ivjstcLblTempGrossWeight == null)
		{
			try
			{
				ivjstcLblTempGrossWeight = new JLabel();
				ivjstcLblTempGrossWeight.setBounds(35, 48, 120, 14);
				ivjstcLblTempGrossWeight.setName(
					"ivjstcLblTempGrossWeight");
				ivjstcLblTempGrossWeight.setText(TEMP_GROSS_WT);
				ivjstcLblTempGrossWeight.setMaximumSize(
					new java.awt.Dimension(115, 14));
				ivjstcLblTempGrossWeight.setMinimumSize(
					new java.awt.Dimension(115, 14));
				ivjstcLblTempGrossWeight.setHorizontalAlignment(4);
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
		return ivjstcLblTempGrossWeight;
	}

	/**
	 * Return the ivjstcLblTons property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTons()
	{
		if (ivjstcLblTons == null)
		{
			try
			{
				ivjstcLblTons = new JLabel();
				ivjstcLblTons.setName("ivjstcLblTons");
				ivjstcLblTons.setText(TONS);
				ivjstcLblTons.setMaximumSize(
					new java.awt.Dimension(31, 14));
				ivjstcLblTons.setMinimumSize(
					new java.awt.Dimension(31, 14));
				ivjstcLblTons.setHorizontalAlignment(4);
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
		return ivjstcLblTons;
	}

	/**
	 * Return the ivjstcLblType property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblType()
	{
		if (ivjstcLblType == null)
		{
			try
			{
				ivjstcLblType = new JLabel();
				ivjstcLblType.setName("ivjstcLblType");
				ivjstcLblType.setText(TYPE);
				ivjstcLblType.setMaximumSize(
					new java.awt.Dimension(30, 14));
				ivjstcLblType.setMinimumSize(
					new java.awt.Dimension(30, 14));
				ivjstcLblType.setHorizontalAlignment(4);
				ivjstcLblType.setBounds(35, 97, 34, 14);
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
		return ivjstcLblType;
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
				ivjstcLblUnitNo.setText(UNIT_NO);
				ivjstcLblUnitNo.setMaximumSize(
					new java.awt.Dimension(43, 14));
				ivjstcLblUnitNo.setMinimumSize(
					new java.awt.Dimension(43, 14));
				ivjstcLblUnitNo.setHorizontalAlignment(4);
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
		return ivjstcLblUnitNo;
	}

	/**
	 * Return the ivjstcLblVehicleClass property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVehicleClass()
	{
		if (ivjstcLblVehicleClass == null)
		{
			try
			{
				ivjstcLblVehicleClass = new JLabel();
				ivjstcLblVehicleClass.setName("ivjstcLblVehicleClass");
				ivjstcLblVehicleClass.setText(CLASS);
				ivjstcLblVehicleClass.setMaximumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblVehicleClass.setMinimumSize(
					new java.awt.Dimension(35, 14));
				ivjstcLblVehicleClass.setHorizontalAlignment(4);
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
		return ivjstcLblVehicleClass;
	}

	/**
	 * Return the gettxtOwnerCity property value.
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
				ivjtxtOwnerCity.setName("ivjtxtOwnerAddressCity");
				//ivjtxtOwnerAddressCity.setInput(-1);
				ivjtxtOwnerCity.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCity.setText("");
				ivjtxtOwnerCity.setBounds(2, 3, 149, 20);
				ivjtxtOwnerCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
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
				ivjtxtOwnerCntry.setName("txtOwnerCntry");
				//ivjtxtOwnerCntry.setInput(-1);
				ivjtxtOwnerCntry.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCntry.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCntry.setVisible(false);
				ivjtxtOwnerCntry.setBounds(159, 3, 45, 20);
				ivjtxtOwnerCntry.setMaxLength(
					CommonConstant.LENGTH_CNTRY);
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
		return ivjtxtOwnerCntry;
	}

	/**
	 * Return the txtOwnerCntryZip property value.
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
				ivjtxtOwnerCntryZpcd.setName("txtOwnerCntryZip");
				//ivjtxtOwnerCntryZip.setInput(4);
				ivjtxtOwnerCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtOwnerCntryZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCntryZpcd.setVisible(false);
				ivjtxtOwnerCntryZpcd.setBounds(214, 3, 85, 20);
				ivjtxtOwnerCntryZpcd.setMaxLength(
					CommonConstant.LENGTH_CNTRY_ZIP);
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
		return ivjtxtOwnerCntryZpcd;
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
				ivjtxtOwnerName1.setName("ivjtxtOwnerName1");
				//ivjtxtOwnerName1.setInput(-1);
				ivjtxtOwnerName1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName1.setBounds(7, 29, 275, 20);
				//ivjtxtOwnerName1.setMaxLength(2147483647);
				ivjtxtOwnerName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
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
				ivjtxtOwnerName2.setName("ivjtxtOwnerName2");
				//ivjtxtOwnerName2.setInput(-1);
				ivjtxtOwnerName2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName2.setBounds(7, 52, 275, 20);
				//ivjtxtOwnerName2.setMaxLength(2147483647);
				ivjtxtOwnerName2.setMaxLength(
					CommonConstant.LENGTH_NAME);
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
		return ivjtxtOwnerName2;
	}

	/**
	 * Return the ivjtxtOwnerAddressState property value.
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
				ivjtxtOwnerState.setName("txtOwnerAddressState");
				//ivjtxtOwnerAddressState.setInput(0);
				ivjtxtOwnerState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtOwnerState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerState.setBounds(159, 3, 24, 20);
				ivjtxtOwnerState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				ivjtxtOwnerState.setRequestFocusEnabled(true);
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
		return ivjtxtOwnerState;
	}

	/**
	 * Return the ivjtxtOwnerStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerStreet1()
	{
		if (ivjtxtOwnerStreet1 == null)
		{
			try
			{
				ivjtxtOwnerStreet1 = new RTSInputField();
				ivjtxtOwnerStreet1.setName("ivjtxtStreetName1");
				ivjtxtOwnerStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet1.setBounds(7, 75, 275, 20);
				//ivjtxtOwnerStreet1.setMaxLength(2147483647);
				ivjtxtOwnerStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
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
				ivjtxtOwnerStreet2.setName("ivjtxtOwnerStreet2");
				ivjtxtOwnerStreet2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet2.setBounds(7, 98, 275, 20);
				//ivjtxtOwnerStreet2.setMaxLength(2147483647);
				ivjtxtOwnerStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
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
				//ivjtxtOwnerAddressZip.setInput(1);
				ivjtxtOwnerZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerZpcd.setText("");
				ivjtxtOwnerZpcd.setBounds(190, 3, 45, 20);
				ivjtxtOwnerZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
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
				//ivjtxtOwnerAddressZipP4.setInput(4);
				ivjtxtOwnerZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerZpcdP4.setBounds(247, 3, 38, 20);
				ivjtxtOwnerZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
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
		return ivjtxtOwnerZpcdP4;
	}

	/**
	 * Return the ivjtxtTempAddlWeight property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTempAddlWeight()
	{
		if (ivjtxtTempAddlWeight == null)
		{
			try
			{
				ivjtxtTempAddlWeight = new RTSInputField();
				ivjtxtTempAddlWeight.setSize(74, 20);
				ivjtxtTempAddlWeight.setName("ivjtxtTempAddlWeight");
				ivjtxtTempAddlWeight.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtTempAddlWeight.setText("#");
				//ivjtxtTempAddlWeight.setInput(1);
				ivjtxtTempAddlWeight.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtTempAddlWeight.setAlignmentX(
					java.awt.Component.CENTER_ALIGNMENT);
				ivjtxtTempAddlWeight.setMaxLength(LENGTH_ADDL_WT);
				ivjtxtTempAddlWeight.setHorizontalAlignment(
					JTextField.RIGHT);
				// user code begin {1}
				ivjtxtTempAddlWeight.setLocation(185, 3);
				ivjtxtTempAddlWeight.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtTempAddlWeight;
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
	 * Validates temp additional weight field.
	 * 
	 * @param aeRTSEx RTSException
	 */
	private void handleTempAddlWt(RTSException aeRTSEx)
	{
		String lsTempAddlWt = gettxtTempAddlWeight().getText();
		int liTempAddlWt;
		if (lsTempAddlWt.equals("") || lsTempAddlWt == null)
		{
			gettxtTempAddlWeight().setText(DFLT_ZERO);
			liTempAddlWt = 0;
		}
		else
		{
			liTempAddlWt = Integer.parseInt(lsTempAddlWt);
		}
		// Calculate temp additional weight
		int liTempVehCaryngCap =
			liTempAddlWt + caRegistrationData.getVehCaryngCap();
		int liTempVehGrossWt =
			liTempAddlWt + caRegistrationData.getVehGrossWt();
		
		// defect 10959
		if (liTempAddlWt == 0)
		{
			cbTempWeightError = true;
			aeRTSEx.addException(new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), gettxtTempAddlWeight()); 
			gettxtTempAddlWeight().requestFocus();
		}
		else 
		{
			try
			{
				TitleClientUtilityMethods.validateGrossWtForRegClassCd(
						caRegistrationData.getRegClassCd(),liTempVehGrossWt); 
			}
			catch (RTSException aeRTSEx1)
			{
				cbTempWeightError = true;
				aeRTSEx.addException(aeRTSEx1, gettxtTempAddlWeight()); 
				gettxtTempAddlWeight().requestFocus();
			}
		}
		// end defect 10959 
		
		getlblTempCarryingCapacity().setText(
			Integer.toString(liTempVehCaryngCap));
		getlblTempGrossWeight().setText(
			Integer.toString(liTempVehGrossWt));
		caTimedPrmtData.setTempVehCaryngCap(liTempVehCaryngCap);
		caTimedPrmtData.setTempVehGrossWt(liTempVehGrossWt);
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
			setName("FrmTempAddlWeightMRG010");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setRequestFocus(false);
			setSize(605, 480);
			setTitle(TITLE_MRG010);
			setContentPane(getFrmTempAddlWeightMRG010ContentPane1());
			
			// defect 10127 
			caNameAddrComp =
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
					getstcLblDash(),
					ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
					ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
					CommonConstant.TX_DEFAULT_STATE);
			// end defect 10127 
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * ItemListener method.
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		if (aaIE.getSource() == ivjchkUSA && cbSetDataFinished == true)
		{
			if (aaIE.getSource() == getchkUSA())
		{
			// defect 10127 
			caNameAddrComp.resetPerUSA(
				aaIE.getStateChange() == ItemEvent.SELECTED);
			// end defect 10127
		}
		}
	}

	/**
	 * Determine if any hard stops exist and have been cleared.
	 *
	 * @return boolean 
	 */
	private boolean procsHardStops()
	{
		// defect 10112 
		boolean lbReturn = true;

		if (caVehInqData.getMfDown() == 0)
		{

			// test for existence of hard stops
			// if indiStopCd == "H"
			Vector lvIndis =
				IndicatorLookup.getIndicators(
					caMFVehicleData,
					getController().getTransCode(),
					IndicatorLookup.SCREEN);

			if (IndicatorLookup.hasHardStop(lvIndis))
			{
				VehMiscData laVehMiscData =
					caVehInqData.getVehMiscData();

				if (laVehMiscData == null
					|| UtilityMethods.isEmpty(laVehMiscData.getAuthCd()))
				{
					getController().processData(
						VCTempAddlWeightMRG010.VTR_AUTHORIZATION,
						caVehInqData);

					if (laVehMiscData == null
						|| UtilityMethods.isEmpty(
							laVehMiscData.getAuthCd()))
					{
						lbReturn = false;
					}
				}
			}
		}
		return lbReturn;
		// end defect 10112
	}

	/**
	 * Check for soft stops.
	 * 
	 * @return boolean
	 */
	private boolean procsSoftStops()
	{
		// defect 9085 
		// Restructured & Replaced check for !HQ
		boolean lbSoftStop = true;

		if (caVehInqData.getMfDown() == 0 && !SystemProperty.isHQ())
		{
			// if soft stops exist
			// if indiStopCd == "S"
			Vector lvIndis =
				IndicatorLookup.getIndicators(
					caMFVehicleData,
					getController().getTransCode(),
					IndicatorLookup.SCREEN);

			if (IndicatorLookup.hasSoftStop(lvIndis))
			{
				caVehInqData.getVehMiscData().setSupvOvrideReason(
					IndicatorLookup.getSoftStopReasons(lvIndis));

				getController().processData(
					VCTempAddlWeightMRG010.SUPV_OVRIDE,
					caVehInqData);

				// defect 10112 
				if (UtilityMethods
					.isEmpty(
						caVehInqData.getVehMiscData().getSupvOvride()))
				{
					lbSoftStop = false;
				}
				// end defect 10112 
			}
		}
		return lbSoftStop;
		// end defect 9085
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
			caVehInqData = (VehicleInquiryData) aaDataObject;
			caMFVehicleData = caVehInqData.getMfVehicleData();
			VehicleInquiryData laOldData =
				(VehicleInquiryData) UtilityMethods.copy(aaDataObject);
			caOldMFVehData = laOldData.getMfVehicleData();
			caTitleData = caMFVehicleData.getTitleData();
			caOwnerData = caMFVehicleData.getOwnerData();
			caRegistrationData = caMFVehicleData.getRegData();
			caVehicleData = caMFVehicleData.getVehicleData();
			caTimedPrmtData = new TimedPermitData();
			// defect 9123
			SpecialPlatesRegisData laSpecialPlatesRegData =
				caMFVehicleData.getSpclPltRegisData();
			// end defect 9123
			caTimedPrmtData.setRTSDateEffDt(RTSDate.getCurrentDate());
			caTimedPrmtData.setTimedPrmtType(
				getController().getTransCode());
			getlblPlate().setText(caRegistrationData.getRegPltNo());
			// defect 8901
			//getlblAge().setText(
			//	Integer.toString(caRegistrationData.getRegPltAge()));
			getlblAge().setText(
				Integer.toString(
					caRegistrationData.getRegPltAge(false)));
			// end defect 8901
			// defect 8900
			// Do not present "0" for Exp Mo/Yr
			int liRegExpMo = caRegistrationData.getRegExpMo();
			getlblExpirationMonth().setText(
				liRegExpMo == 0 ? "" : Integer.toString(liRegExpMo));
			int liRegExpYr = caRegistrationData.getRegExpYr();
			getlblExpirationYear().setText(
				liRegExpYr == 0 ? "" : Integer.toString(liRegExpYr));
			// end defect 8900

			getlblCounty().setText(
				Integer.toString(
					caRegistrationData.getResComptCntyNo()));

			//get and set class code description from cache to display
			int liRegClassCd = caRegistrationData.getRegClassCd();
			CommonFeesData laCommonFeesData =
				CommonFeesCache.getCommonFee(
					liRegClassCd,
					RTSDate.getCurrentDate().getYYYYMMDDDate());
			getlblClass().setText(laCommonFeesData.getRegClassCdDesc());

			//get and set plate type description from cache to display 
			String lsRegPltCd = caRegistrationData.getRegPltCd();
			ItemCodesData laItemCodesData =
				ItemCodesCache.getItmCd(lsRegPltCd);
			getlblType().setText(laItemCodesData.getItmCdDesc());

			// defect 9123
			String lsOrgNo = new String();
			if (laSpecialPlatesRegData != null)
			{
				lsOrgNo = laSpecialPlatesRegData.getOrgNo();
			}

			// Get Organization Name  
			String lsOrgName = new String();
			if (lsRegPltCd != null
				&& lsRegPltCd.length() != 0
				&& lsOrgNo != null)
			{
				// defect 9912
				// Remove reference to RegisData.setOrgNo() 
				//caRegistrationData.setOrgNo(lsOrgNo);
				// end defect 9912 
				lsOrgName =
					OrganizationNumberCache.getOrgName(
						lsRegPltCd,
						lsOrgNo);
			}
			getlblOrg().setText(lsOrgName);
			// end defect 9123

			// get and set the office name from cache to display 
			int liOfcNo = caRegistrationData.getResComptCntyNo();
			if (liOfcNo > 0)
			{
				OfficeIdsData laOfficeIdsData =
					OfficeIdsCache.getOfcId(liOfcNo);
				getlblCounty().setText(
					Integer.toString(liOfcNo)
						+ "   "
						+ laOfficeIdsData.getOfcName());
			}
			else if (liOfcNo == 0)
			{
				getlblCounty().setText(Integer.toString(liOfcNo));
			}

			// get and set vehicle data 
			getlblYear().setText(
				Integer.toString(caVehicleData.getVehModlYr()));
			getlblMake().setText(caVehicleData.getVehMk());
			getlblVehBodyType().setText(caVehicleData.getVehBdyType());
			getlblVehModel().setText(caVehicleData.getVehModl());
			getlblVIN().setText(caVehicleData.getVin());
			getlblBodyVIN().setText(
				caVehicleData.getVehBdyVin() == null
					? ""
					: caVehicleData.getVehBdyVin());
			getlblVehicleClass().setText(caVehicleData.getVehClassCd());
			if (caVehicleData.getVehTon() == null)
			{
				getlblTons().setText(DFLT_DOLLAR_AMT);
			}
			else
			{
				getlblTons().setText(
					caVehicleData.getVehTon().getValue());
			}
			getlblUnitNo().setText(
				caTitleData.getVehUnitNo() == null
					? ""
					: caTitleData.getVehUnitNo());
			getlblEmpty().setText(
				Integer.toString(caVehicleData.getVehEmptyWt()));
			getlblCapacity().setText(
				Integer.toString(caRegistrationData.getVehCaryngCap()));
			getlblGross().setText(
				Integer.toString(caRegistrationData.getVehGrossWt()));
			getlblOwnerId().setText(
				caOwnerData.getOwnrId() == null
					? ""
					: caOwnerData.getOwnrId());

			// defect 10127 
			// New OwnerData
			caNameAddrComp.setNameAddressDataToDisplay(caOwnerData); 
			// end defect 10127 

			// get and set title data 
			getlblDocumentNo().setText(caTitleData.getDocNo());
			// if mainframe is down hide issue date and doc type desc.
			int liMfDown = caVehInqData.getMfDown();

			// additional weight data
			gettxtTempAddlWeight().setText(DFLT_ZERO);
			getlblTempCarryingCapacity().setText(DFLT_ZERO);
			getlblTempGrossWeight().setText(DFLT_ZERO);
			if (liMfDown == 0)
			{
				int liIssueDate = caTitleData.getTtlIssueDate();
				if (liIssueDate > 0)
				{
					getlblIssued().setText(
						new RTSDate(
							RTSDate.YYYYMMDD,
							caTitleData.getTtlIssueDate())
							.toString());
				}
				else
				{
					getlblIssued().setText("");
				}

				// get and set document type code description 
				int liDocTypeCd = caTitleData.getDocTypeCd();
				DocumentTypesData lDocTypeData =
					DocumentTypesCache.getDocType(liDocTypeCd);
				getlblDocTypeCdDescription().setText(
					lDocTypeData.getDocTypeCdDesc());

				// get and display indicators
				getIndicators();

				// make address fields non-editable
				getchkUSA().setEnabled(false);
				gettxtOwnerName1().setEnabled(false);
				gettxtOwnerName2().setEnabled(false);
				gettxtOwnerStreet1().setEnabled(false);
				gettxtOwnerStreet2().setEnabled(false);
				gettxtOwnerCity().setEnabled(false);
				gettxtOwnerState().setEnabled(false);
				gettxtOwnerZpcd().setEnabled(false);
				gettxtOwnerZpcdP4().setEnabled(false);
				// defect 10112 
				gettxtOwnerCntry().setEnabled(false);
				gettxtOwnerCntryZpcd().setEnabled(false);
				// end defect 10112 
			}
			else if (liMfDown == 1)
			{
				getstcLblIssued().setVisible(false);
				getlblIssued().setVisible(false);
				getlblDocTypeCdDescription().setVisible(false);
				gettxtOwnerName1().requestFocus();
			}

			// defect 9971
			// Use CommonConstant.MAX_INDI_NO_SCROLL
			// defect 8494
			// Moved from windowOpened
			if (ciNumIndis > CommonConstant.MAX_INDI_NO_SCROLL)
			{
				// end defect 9971 
				setDefaultFocusField(getlstIndicator());
			}
			else
			{
				if (gettxtOwnerName1().isEnabled())
				{
					setDefaultFocusField(gettxtOwnerName1());
				}
				else
				{
					setDefaultFocusField(gettxtTempAddlWeight());
				}
			} // end defect 8494
		}
		else
		{
			new RTSException(
				RTSException.FAILURE_MESSAGE,
				ERRMSG_TYPE,
				ERRMSG_ERROR).displayError(
				this);
		}
		cbSetDataFinished = true;
	}

	/**
	 * Set input field values to data object.
	 */
	private void setInputFields()
	{
		if (caVehInqData.getMfDown() == 1)
		{
			// defect 10112 
			caOwnerData.setName1(
				gettxtOwnerName1().getText().toUpperCase());
			caOwnerData.setName2(
				gettxtOwnerName2().getText().toUpperCase());
			caOwnerData.getAddressData().setSt1(
				gettxtOwnerStreet1().getText().toUpperCase());
			caOwnerData.getAddressData().setSt2(
				gettxtOwnerStreet2().getText().toUpperCase());
			caOwnerData.getAddressData().setCity(
				gettxtOwnerCity().getText().toUpperCase());

			// Determine if country is USA and adjust country, state and
			// zip codes
			if (getchkUSA().isSelected())
			{
				caOwnerData.getAddressData().setState(
					gettxtOwnerState().getText().toUpperCase());
				caOwnerData.getAddressData().setZpcd(
					gettxtOwnerZpcd().getText());
				caOwnerData.getAddressData().setZpcdp4(
					gettxtOwnerZpcdP4().getText());
				caOwnerData.getAddressData().setCntry("");
			}
			else
			{
				caOwnerData.getAddressData().setCntry(
					gettxtOwnerCntry().getText().toUpperCase());
				caOwnerData.getAddressData().setCntryZpcd(
					gettxtOwnerCntryZpcd().getText().trim());

				//		int liLen = gettxtOwnerCntryZip().getText().length();
				//		if (liLen > CNTRY_ZIP_MAXLENGTH)
				//		{
				//			caOwnerData.getAddressData().setZpcd(
				//		gettxtOwnerCntryZip().getText().substring(
				//			0,
				//			5));
				//			caOwnerData.getAddressData().setZpcdp4(
				//		gettxtOwnerCntryZip().getText().substring(
				//			5,
				//			gettxtOwnerCntryZip().getText().length()
				//				- 1));
				//		}
				//		else
				//		{
				//			caOwnerData.getAddressData().setZpcd(
				//		gettxtOwnerCntryZip().getText());
				//			caOwnerData.getAddressData().setZpcdp4("");
				//		}
				caOwnerData.getAddressData().setState("");
			}
			// end defect 10112 
		}
	}


	/**
	 * Set SetDataFinished
	 *  
	 * @param abNewSetDataFinished boolean
	 */
	private void setSetDataFinished(boolean abNewSetDataFinished)
	{
		cbSetDataFinished = abNewSetDataFinished;
	}

	/**
	 * Validates input fields.
	 * 
	 * @return boolean
	 */
	private boolean validateInputFields()
	{
		boolean lbValid = true; 
		RTSException leRTSEx = new RTSException();

		// defect 10127
		if (caVehInqData.isMFDown())
		{
			caNameAddrComp.validateNameAddressFields(leRTSEx); 
		}
		// Problem with focus if errors in above and in weight (kph) 
		if (!leRTSEx.isValidationError())
		{
			clearAllColor(this); 
			handleTempAddlWt(leRTSEx);
		}
		// end defect 10127 
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;  
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
