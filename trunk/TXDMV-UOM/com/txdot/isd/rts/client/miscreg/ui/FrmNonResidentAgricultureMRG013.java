package com.txdot.isd.rts.client.miscreg.ui;

//import java.awt.Color;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientBusiness;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientUtilityMethods;

import com.txdot.isd.rts.services.cache.CommercialVehicleWeightsCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * FrmNonResidentAgricultureMRG013.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and 
 *							doneWorking()   
 * T Pederson   04/30/2002  Don't allow spaces to be entered in 
 *							input field
 * MAbs			06/03/2002	CQU100004190
 * Bob Brown	07/23/2002  For defect # 4474, added code in 
 *							method validate input fields to ensure 
 *							that the number 0 is not valid for 
 *							empty weight. For defect #4453, went 
 *							into visual composition editor, and 
 *							made the inset right value equal to 
 *                          other fields right inset value.
 * S Govindappa 07/24/2002  Fixed Defect#4505 and 4506. Made 
 *							changes to handleCaryngCapField method
 *						    not to change the color of the 
 *							Carrying capacity field to red on focus
 *							lost and not to display the error# 200 
 *							when Trailer is selected and Enter is 
 *							pressed on MRG013 screen.
 * K. Harrell	08/02/2002  Defect CQU100004504; Reset Text field 
 *							for Entry Point when select Texas 
 *							Products
 * Bob Brown	08/16/2002  For defect # 4474, added code in method
 *                          validate input fields to ensure that 
 *							the number 0 is not valid for carrying
 *							capacity.
 * Bob Brown	12/09/2002  For defect # 4838, commented out code 
 *							in focus lost method because we are 
 *							validating all fields when the user
 *                          pressed the enter key. The validation 
 *							was already being done in the 
 *							validateInputFields method. Also, 
 *							removed the focusListener from the
 *							following fields/methods: 
 *							gettxtCarryingCapacity, 
 *							gettxtEmptyWeight, gettxtTonnage, 
 *							gettxtYear, and gettxtVin.
 * S Govindappa	01/28/2002	Fixing defect #5314. Calculation of 
 *							gross weight, conversion of VIN and 
 *							Tonnage conversion for blank input
 *                          was added back to focusLost method. It 
 *							was removed while fixing defect # 4838.
 * J Zwiener 	12/30/2003	Change error message from 150 to 200 
 *							for an empty or zero carrying capacity.
 *                          Method:  validateInputFields()
 *                          Defect 6644  Ver 5.1.5.2
 * R Taylor		06/29/2004	Increased box height around fields on 
 *							right side of screen to allow room for
 *							plate number entry field.
 *							defect 7249 Ver 5.2.0
 * J Zwiener	11/19/2004	Screen not advancing when carrying capacity
 *							not entered.  Number format exception.
 *							Present error 150 if trailer or 200 if truck
 *							modify validateInputFields()
 *							defect 6984, 7442 Ver 5.2.2
 * J Zwiener	11/19/2004	Address label truncated
 *							modify stcLblOwnerAddress via VCE
 *							defect 7266 Ver 5.2.2
 * J Zwiener	11/19/2004	Entry point field (Out of State) enlarges
 *							and other fields shift upward when tabbing
 *							from Empty Weight field.
 *							modify via VCE - change JPanel1 layout from
 *							gridbaglayout to null
 *							defect 7341 Ver 5.2.2
 * J Zwiener	11/19/2004	Not beeping when invalid year msg presented.
 *							Also true for state registered & owner state
 *							used err 2004 for invalid state
 *							used err 2006 for invalid vehicle model yr
 *							modify validateInputFields()
 *							defect 6868 Ver 5.2.2
 * K Harrell	01/17/2004	Expand field for Zip+4 via VC
 *							JavaDoc/Formatting/Variable Name Cleanup
 *							defect 7875 Ver 5.2.3 
 * B Hargrove	03/17/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							defect 7893 Ver 5.2.3
 * B Hargrove	03/31/2005	Comment out setNextFocusableComponent() 
 *							modify getradioOutOfStateProducts(),
 *							getradioPneumatic(),getradioSemiTrailer(),
 *							getradioSolid(),getradioTexasProducts(),
 *							getradioTrailer(),getradioTruck(),
 *							getradioTruckTractor()
 *							defect 7893 Ver 5.2.3
 * B Hargrove	04/14/2005	Added JPanel to allow tabbing down left side
 * 							before tabbing down right side. Handle focus
 * 							and selection of radio buttons with tab and
 * 							arrow keys. 
 *							added jPanel
 *							modify initialize(), keyPressed()
 *							defect 7893 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                
 * B Hargrove	06/23/2005	Remove unused methods and variables.
 * 							delete implements KeyListener
 *							defect 7893 Ver 5.2.3
 * B Hargrove	07/12/2005	Refactor\Move 
 * 							MiscellaneousRegClientUtilityMethods class  
 *							to com.txdot.isd.rts.client.miscreg.business.
 *							defect 7893 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), truckType, 
 * 								SEMI_TRAILER, TRUCK, selectedNum
 * 							modify getpnlProduct(), getpnlSolid(), 
 * 								getpnlTruckType(), initialize()
 * 							defect 7893 Ver 5.2.3
 * K Harrell	02/03/2006	Correct action on itemStateChanged for 
 * 							Registration types
 * 							modify handleDieselAndTonnageFields()
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
 * K Harrell	06/29/2009	Implement new OwnerData.  Additional Cleanup
 * 							add ivjJPanel2, getJPanel2()	
 * 							delete setOwnerAddressCity(),
 * 							 setOwnerAddressState(), setOwnerAddressZip(),
 * 							 setOwnerAddressZipP4(), jPanel, getJanel() 
 * 							modify setData(), setInputFields()	
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/19/2009	Implement new validation of Country 
 * 							field. Use ErrorsConstant. Use standard 
 * 							mechanism for field visibility.
 * 							TODO Further Cleanup remains.  
 *							add caNameAddrComp
 *							delete TX 
 *							delete caRegData, caVehData, caOwnrData, 
 *							  caCompleteTransData 
 *							delete handleEmptyWeight()  
 *							delete caPanelBackgroundColor, 
 *							 caSavedBackground, caSavedBorder, 
 *							 caSavedForegroundColor, get/set methods							
 *							modify focusLost(), gettxtCarryingCapacity(),
 *							 gettxtEmptyWeight(),gettxtEntryPoint(),
 *							 gettxtMake(),gettxtOwnerCity(), 
 *							 gettxtOwnerName1(),gettxtOwnerName2(),
 *							 gettxtOwnerState(),gettxtOwnerStreet1(),
 *							 gettxtOwnerStreet2(),gettxtOwnerZpcd(),
 *							 gettxtOwnerZpcdP4(),gettxtPlateNo(),
 *							 gettxtTonnage(),gettxtVIN(), 
 *							 handleCaryngCapField(),
 *							 handleDieselAndTonnageFields(),  
 *							 handleEntryPointField(),
 *							 handleNonResPermitType(),
 *							 handleTonnageField()
 *							 handleVehGrossWt(),  
 *							 initialize(), itemStateChanged(), 
 *							 setInputFields(), validateFields() 
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	12/17/2009	add UtilityMethods.trimRTSInputField()
 * 							modify validateFields()
 * 							defect 10299 Ver Defect_POS_H
 * ---------------------------------------------------------------------
 */

/**
 * Registration of Non-Resident Agriculture Permit.
 *
 * @version	Defect_POS_H	12/17/2009
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 14:36:48
 */

public class FrmNonResidentAgricultureMRG013
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{

	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkDiesel = null;
	private JPanel ivjFrmNonResidentAgricultureMRG013ContentPane1 =
		null;
	private JPanel ivjJPanel1 = null;
	// defect 10112
	private JPanel ivjJPanel2 = null;
	// end defect 10112 
	private JLabel ivjlblEffectiveDate = null;
	private JLabel ivjlblExpirationDate = null;
	private JLabel ivjlblGrossWeight = null;
	private JPanel ivjpnlDiesel = null;
	private JPanel ivjpnlProduct = null;
	private JPanel ivjpnlSolid = null;
	private JPanel ivjpnlTruckType = null;
	private JRadioButton ivjradioOutOfStateProducts = null;
	private JRadioButton ivjradioPneumatic = null;
	private JRadioButton ivjradioSemiTrailer = null;
	private JRadioButton ivjradioSolid = null;
	private JRadioButton ivjradioTexasProducts = null;
	private JRadioButton ivjradioTrailer = null;
	private JRadioButton ivjradioTruck = null;
	private JRadioButton ivjradioTruckTractor = null;
	private JLabel ivjstcLblCarryCapacity = null;
	private JLabel ivjstcLblDash1 = null;
	private JLabel ivjstcLblEffectiveDate = null;
	private JLabel ivjstcLblEmptyWeight = null;
	private JLabel ivjstcLblEntryPoint = null;
	private JLabel ivjstcLblExpirationDate = null;
	private JLabel ivjstcLblGrossWeight = null;
	private JLabel ivjstcLblOwnerAddress = null;
	private JLabel ivjstcLblOwnerName = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblStateRegistered = null;
	private JLabel ivjstcLblTonnage = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblYearMake = null;
	private RTSInputField ivjtxtCarryingCapacity = null;
	private RTSInputField ivjtxtEmptyWeight = null;
	private RTSInputField ivjtxtEntryPoint = null;
	private RTSInputField ivjtxtMake = null;
	private RTSInputField ivjtxtOwnerCity = null;
	private RTSInputField ivjtxtOwnerName1 = null;
	private RTSInputField ivjtxtOwnerName2 = null;
	private RTSInputField ivjtxtOwnerState = null;
	private RTSInputField ivjtxtOwnerStreet1 = null;
	private RTSInputField ivjtxtOwnerStreet2 = null;
	private RTSInputField ivjtxtOwnerZpcd = null;
	private RTSInputField ivjtxtOwnerZpcdP4 = null;
	private RTSInputField ivjtxtPlateNo = null;
	private RTSInputField ivjtxtStateRegistered = null;
	private RTSInputField ivjtxtTonnage = null;
	private RTSInputField ivjtxtVIN = null;
	private RTSInputField ivjtxtYear = null;
	private RTSButtonGroup caButtonGroupProduct;
	private RTSButtonGroup caButtonGroupTireType;
	private RTSButtonGroup caButtonGroupTruckType;

	// Objects
	private CommercialVehicleWeightsData caCommVehWtData = null;
	private MFVehicleData caMFVehData = new MFVehicleData();
	private TimedPermitData caTimedPrmtData = new TimedPermitData();
	// defect 10127 
	private NameAddressComponent caNameAddrComp = null;
	// end defect 10127	

	// Constants 
	private final static String CARRY_CAP = "Carrying Capacity:";
	private final static String CHK_IF_DIESEL = "Check if diesel:";
	private final static String DASH = "-";
	private final static String DEFAULT_ZIP = "13245";
	private final static String DEFAULT_ZIP4 = "9876";
	private final static String DIESEL = "Diesel";
	private final static String EFF_DT = "Effective Date:";
	private final static String EMPTY_WT = "Empty Weight:";
	private final static String ENTRY_PT = "Entry Point:";
	private final static String ERRMSG_DATA_MISSING =
		"Data missing for GET_NEXT_COMPLETE_TRANS_VC";
	private final static String ERROR = "ERROR";
	private final static String EXP_DT = "Expiration Date:";
	private final static String GROSS_WT = "Gross Weight:";
	private final static int MAX_GROSS_WEIGHT = 80000;
	private final static String NON_RESIDENT_INSTATE_PERMIT = "NRIPT";
	private final static String NON_RESIDENT_OUTSTATE_PERMIT = "NROPT";
	private final static String OUTSTATE_PRODS =
		"Out-of-State Products";
	private final static String OWNR_ADDR = "Owner Address:";
	private final static String OWNR_NAME = "Owner Name:";
	private final static String PLATE_NO = "Plate No:";
	private final static String PNEUMATIC = "Pneumatic";
	private final static String SELECT_ONE = "Select one:";
	private final static String SEMI_TRLR = "Semi-Trailer";
	private final static String SOLID = "Solid";
	private final static String STATE_REGIS = "State Registered:";
	private final static String TEX_PRODS = "Texas Products";
	private final static String TIRE_TYPE_PNEUMATIC = "P";
	private final static String TIRE_TYPE_SOLID = "S";
	private final static String TONNAGE = "Tonnage:";
	private final static String TRAILER = "Trailer";
	private final static int TRAILER_REG_CLASS = 37;
	private final static String TRUCK_LBL = "Truck";
	private final static int TRUCK_REG_CLASS = 36;
	private final static String TRUCK_TRACTOR = "Truck-Tractor";
	private final static String VIN = "VIN:";
	private final static String YR_MAKE = "Year/Make:";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmNonResidentAgricultureMRG013 laFrmNonResidentAgricultureMRG013;
			laFrmNonResidentAgricultureMRG013 =
				new FrmNonResidentAgricultureMRG013();
			laFrmNonResidentAgricultureMRG013.setModal(true);
			laFrmNonResidentAgricultureMRG013
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				}
			});
			laFrmNonResidentAgricultureMRG013.show();
			java.awt.Insets laInsets =
				laFrmNonResidentAgricultureMRG013.getInsets();
			laFrmNonResidentAgricultureMRG013.setSize(
				laFrmNonResidentAgricultureMRG013.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmNonResidentAgricultureMRG013.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmNonResidentAgricultureMRG013.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of javax.swing.JDialog");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmNonResidentAgricultureMRG013 constructor comment.
	 */
	public FrmNonResidentAgricultureMRG013()
	{
		super();
		initialize();
	}

	/**
	 * FrmNonResidentAgricultureMRG013 constructor with the parent.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmNonResidentAgricultureMRG013(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmNonResidentAgricultureMRG013 constructor with the parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmNonResidentAgricultureMRG013(JFrame aaParent)
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

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{

				if (!validateFields())
				{
					return;
				}
				setInputFields();

				getController().processData(
					AbstractViewController.ENTER,
					MiscellaneousRegClientUtilityMethods.prepFees(
						caTimedPrmtData,
						caMFVehData));
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.MRG013);
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
		//empty code block
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		if (!aaFE.isTemporary() && aaFE.getSource() == gettxtVIN())
		{
			String lsVin = gettxtVIN().getText();
			// defect 8902
			lsVin = CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
			// end defect 8902
			gettxtVIN().setText(lsVin);
		}
		else if (
			!aaFE.isTemporary() && aaFE.getSource() == gettxtTonnage())
		{
			// defect 10127
			if (gettxtTonnage().isEmpty())
			{
				// end defect 10127 
				gettxtTonnage().setText(new Dollar("0").toString());
			}
		}

		if (!aaFE.isTemporary()
			&& (aaFE.getSource() == gettxtEmptyWeight()
				|| aaFE.getSource() == gettxtCarryingCapacity()))
		{

			String lsCarryngCap = gettxtCarryingCapacity().getText();

			int liCarryngCap =
				lsCarryngCap.equals("")
					? 0
					: Integer.parseInt(lsCarryngCap);
			String lsEmptyWt = gettxtEmptyWeight().getText();

			int liEmptyWt =
				lsEmptyWt.equals("") ? 0 : Integer.parseInt(lsEmptyWt);

			// Calculate vehicle gross weight
			int ciVehGrossWt = liEmptyWt + liCarryngCap;
			getlblGrossWeight().setText(Integer.toString(ciVehGrossWt));
		}
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return util.ButtonPanel
	 */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setBounds(168, 382, 248, 35);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
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
	 * Return the ivjchkDiesel property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkDiesel()
	{
		if (ivjchkDiesel == null)
		{
			try
			{
				ivjchkDiesel = new javax.swing.JCheckBox();
				ivjchkDiesel.setName("ivjchkDiesel");
				//ivjchkDiesel.setMnemonic(68);
				ivjchkDiesel.setMnemonic(KeyEvent.VK_D);
				ivjchkDiesel.setText(DIESEL);
				ivjchkDiesel.setMaximumSize(
					new java.awt.Dimension(60, 22));
				ivjchkDiesel.setActionCommand(DIESEL);
				ivjchkDiesel.setMinimumSize(
					new java.awt.Dimension(60, 22));
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
		return ivjchkDiesel;
	}

	/**
	 * Return the FrmNonResidentAgricultureMRG013ContentPane1 property
	 * value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax
		.swing
		.JPanel getFrmNonResidentAgricultureMRG013ContentPane1()
	{
		if (ivjFrmNonResidentAgricultureMRG013ContentPane1 == null)
		{
			try
			{
				ivjFrmNonResidentAgricultureMRG013ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmNonResidentAgricultureMRG013ContentPane1.setName(
					"FrmNonResidentAgricultureMRG013ContentPane1");
				ivjFrmNonResidentAgricultureMRG013ContentPane1
					.setLayout(
					null);
				ivjFrmNonResidentAgricultureMRG013ContentPane1.add(
					getJPanel1(),
					null);
				ivjFrmNonResidentAgricultureMRG013ContentPane1.add(
					getJPanel2(),
					null);
				ivjFrmNonResidentAgricultureMRG013ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmNonResidentAgricultureMRG013ContentPane1
					.setLayout(
					null);
				ivjFrmNonResidentAgricultureMRG013ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmNonResidentAgricultureMRG013ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmNonResidentAgricultureMRG013ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjFrmNonResidentAgricultureMRG013ContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(323, 362));
				getJPanel1().add(
					getstcLblPlateNo(),
					getstcLblPlateNo().getName());
				getJPanel1().add(
					getstcLblStateRegistered(),
					getstcLblStateRegistered().getName());
				getJPanel1().add(
					getstcLblYearMake(),
					getstcLblYearMake().getName());
				getJPanel1().add(
					getstcLblVIN(),
					getstcLblVIN().getName());
				getJPanel1().add(
					getstcLblEmptyWeight(),
					getstcLblEmptyWeight().getName());
				getJPanel1().add(
					getstcLblCarryCapacity(),
					getstcLblCarryCapacity().getName());
				getJPanel1().add(
					getstcLblGrossWeight(),
					getstcLblGrossWeight().getName());
				getJPanel1().add(
					gettxtPlateNo(),
					gettxtPlateNo().getName());
				getJPanel1().add(
					gettxtStateRegistered(),
					gettxtStateRegistered().getName());
				getJPanel1().add(gettxtYear(), gettxtYear().getName());
				getJPanel1().add(gettxtMake(), gettxtMake().getName());
				getJPanel1().add(gettxtVIN(), gettxtVIN().getName());
				getJPanel1().add(
					gettxtEmptyWeight(),
					gettxtEmptyWeight().getName());
				getJPanel1().add(
					gettxtCarryingCapacity(),
					gettxtCarryingCapacity().getName());
				getJPanel1().add(
					gettxtEntryPoint(),
					gettxtEntryPoint().getName());
				getJPanel1().add(
					getlblGrossWeight(),
					getlblGrossWeight().getName());
				getJPanel1().add(
					getstcLblOwnerName(),
					getstcLblOwnerName().getName());
				getJPanel1().add(
					gettxtOwnerName1(),
					gettxtOwnerName1().getName());
				getJPanel1().add(
					gettxtOwnerName2(),
					gettxtOwnerName2().getName());
				getJPanel1().add(
					getstcLblOwnerAddress(),
					getstcLblOwnerAddress().getName());
				getJPanel1().add(
					gettxtOwnerStreet1(),
					gettxtOwnerStreet1().getName());
				getJPanel1().add(
					gettxtOwnerStreet2(),
					gettxtOwnerStreet2().getName());
				getJPanel1().add(
					getstcLblEntryPoint(),
					getstcLblEntryPoint().getName());
				getJPanel1().add(
					gettxtOwnerCity(),
					gettxtOwnerCity().getName());
				getJPanel1().add(
					gettxtOwnerState(),
					gettxtOwnerState().getName());
				getJPanel1().add(
					gettxtOwnerZpcd(),
					gettxtOwnerZpcd().getName());
				getJPanel1().add(
					getstcLblDash1(),
					getstcLblDash1().getName());
				getJPanel1().add(
					gettxtOwnerZpcdP4(),
					gettxtOwnerZpcdP4().getName());
				// user code begin {1}
				ivjJPanel1.setBounds(267, 7, 315, 354);
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
	 * This method initializes ivjJPanel2
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			ivjJPanel2 = new javax.swing.JPanel();
			ivjJPanel2.setLayout(null);
			ivjJPanel2.add(getpnlProduct(), null);
			ivjJPanel2.add(getpnlTruckType(), null);
			ivjJPanel2.add(getpnlDiesel(), null);
			ivjJPanel2.add(getpnlSolid(), null);
			ivjJPanel2.add(getstcLblExpirationDate(), null);
			ivjJPanel2.add(getlblExpirationDate(), null);
			ivjJPanel2.add(getstcLblEffectiveDate(), null);
			ivjJPanel2.add(getlblEffectiveDate(), null);
			ivjJPanel2.setBounds(4, 8, 260, 352);
			ivjJPanel2.setOpaque(true);
		}
		return ivjJPanel2;
	}

	/**
	 * Return the lblEffectiveDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getlblEffectiveDate()
	{
		if (ivjlblEffectiveDate == null)
		{
			try
			{
				ivjlblEffectiveDate = new javax.swing.JLabel();
				ivjlblEffectiveDate.setBounds(138, 310, 72, 14);
				ivjlblEffectiveDate.setName("lblEffectiveDate");
				ivjlblEffectiveDate.setText("#");
				ivjlblEffectiveDate.setMaximumSize(
					new java.awt.Dimension(44, 14));
				ivjlblEffectiveDate.setMinimumSize(
					new java.awt.Dimension(44, 14));
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
		return ivjlblEffectiveDate;
	}

	/**
	 * Return the lblExpirationDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getlblExpirationDate()
	{
		if (ivjlblExpirationDate == null)
		{
			try
			{
				ivjlblExpirationDate = new javax.swing.JLabel();
				ivjlblExpirationDate.setBounds(138, 331, 72, 14);
				ivjlblExpirationDate.setName("lblExpirationDate");
				ivjlblExpirationDate.setText("#");
				ivjlblExpirationDate.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjlblExpirationDate.setMinimumSize(
					new java.awt.Dimension(50, 14));
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
		return ivjlblExpirationDate;
	}

	/**
	 * Return the lblGrossWeight property value.
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getlblGrossWeight()
	{
		if (ivjlblGrossWeight == null)
		{
			try
			{
				ivjlblGrossWeight = new javax.swing.JLabel();
				ivjlblGrossWeight.setName("lblGrossWeight");
				ivjlblGrossWeight.setText("0");
				ivjlblGrossWeight.setMaximumSize(
					new java.awt.Dimension(54, 14));
				ivjlblGrossWeight.setHorizontalTextPosition(0);
				ivjlblGrossWeight.setBounds(149, 156, 68, 17);
				ivjlblGrossWeight.setMinimumSize(
					new java.awt.Dimension(54, 14));
				ivjlblGrossWeight.setHorizontalAlignment(4);
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
		return ivjlblGrossWeight;
	}

	/**
	 * Return the ivjpnlDiesel property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getpnlDiesel()
	{
		if (ivjpnlDiesel == null)
		{
			try
			{
				ivjpnlDiesel = new javax.swing.JPanel();
				ivjpnlDiesel.setName("ivjpnlDiesel");
				ivjpnlDiesel.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						CHK_IF_DIESEL));
				ivjpnlDiesel.setLayout(new java.awt.GridBagLayout());
				ivjpnlDiesel.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlDiesel.setMinimumSize(
					new java.awt.Dimension(127, 66));
				java.awt.GridBagConstraints constraintschkDiesel =
					new java.awt.GridBagConstraints();
				constraintschkDiesel.gridx = 1;
				constraintschkDiesel.gridy = 1;
				constraintschkDiesel.gridwidth = 2;
				constraintschkDiesel.ipadx = 26;
				constraintschkDiesel.insets =
					new java.awt.Insets(11, 9, 2, 13);
				getpnlDiesel().add(
					getchkDiesel(),
					constraintschkDiesel);
				java.awt.GridBagConstraints constraintsstcLblTonnage =
					new java.awt.GridBagConstraints();
				constraintsstcLblTonnage.gridx = 1;
				constraintsstcLblTonnage.gridy = 2;
				constraintsstcLblTonnage.ipady = 6;
				constraintsstcLblTonnage.insets =
					new java.awt.Insets(2, 6, 20, 2);
				getpnlDiesel().add(
					getstcLblTonnage(),
					constraintsstcLblTonnage);
				java.awt.GridBagConstraints constraintstxtTonnage =
					new java.awt.GridBagConstraints();
				constraintstxtTonnage.gridx = 2;
				constraintstxtTonnage.gridy = 2;
				constraintstxtTonnage.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtTonnage.weightx = 2.0;
				constraintstxtTonnage.ipadx = 37;
				constraintstxtTonnage.insets =
					new java.awt.Insets(3, 3, 22, 7);
				getpnlDiesel().add(
					gettxtTonnage(),
					constraintstxtTonnage);
				// user code begin {1}
				ivjpnlDiesel.setBounds(129, 102, 123, 85);
				Border laBdrDiesel =
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						CHK_IF_DIESEL);
				ivjpnlDiesel.setBorder(laBdrDiesel);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjpnlDiesel;
	}

	/**
	 * Return the ivjpnlProduct property value.
	 * 
	 * @return JPanel
	 */

	private JPanel getpnlProduct()
	{
		if (ivjpnlProduct == null)
		{
			try
			{
				ivjpnlProduct = new javax.swing.JPanel();
				ivjpnlProduct.setName("ivjpnlProduct");
				ivjpnlProduct.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT_ONE));
				ivjpnlProduct.setLayout(new java.awt.GridBagLayout());
				ivjpnlProduct.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlProduct.setMinimumSize(
					new java.awt.Dimension(191, 68));
				java
					.awt
					.GridBagConstraints constraintsradioTexasProducts =
					new java.awt.GridBagConstraints();
				constraintsradioTexasProducts.gridx = 1;
				constraintsradioTexasProducts.gridy = 1;
				constraintsradioTexasProducts.ipadx = 37;
				constraintsradioTexasProducts.insets =
					new java.awt.Insets(12, 10, 1, 9);
				getpnlProduct().add(
					getradioTexasProducts(),
					constraintsradioTexasProducts);
				java
					.awt
					.GridBagConstraints constraintsradioOutOfStateProducts =
					new java.awt.GridBagConstraints();
				constraintsradioOutOfStateProducts.gridx = 1;
				constraintsradioOutOfStateProducts.gridy = 2;
				constraintsradioOutOfStateProducts.ipadx = 3;
				constraintsradioOutOfStateProducts.insets =
					new java.awt.Insets(1, 10, 19, 9);
				getpnlProduct().add(
					getradioOutOfStateProducts(),
					constraintsradioOutOfStateProducts);
				// user code begin {1}
				ivjpnlProduct.setBounds(11, 7, 167, 88);
				// defect 7893
				caButtonGroupProduct = new RTSButtonGroup();
				caButtonGroupProduct.add(getradioTexasProducts());
				caButtonGroupProduct.add(getradioOutOfStateProducts());
				// end defect 7893
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjpnlProduct;
	}

	/**
	 * Return the ivjpnlSolid property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSolid()
	{
		if (ivjpnlSolid == null)
		{
			try
			{
				ivjpnlSolid = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints1 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints2 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints1.insets =
					new java.awt.Insets(17, 13, 2, 6);
				consGridBagConstraints1.ipady = -2;
				consGridBagConstraints1.ipadx = 6;
				consGridBagConstraints1.gridy = 0;
				consGridBagConstraints1.gridx = 0;
				consGridBagConstraints2.insets =
					new java.awt.Insets(2, 13, 17, 10);
				consGridBagConstraints2.ipady = -2;
				consGridBagConstraints2.ipadx = 35;
				consGridBagConstraints2.gridy = 1;
				consGridBagConstraints2.gridx = 0;
				ivjpnlSolid.setName("pnlSolid");
				ivjpnlSolid.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT_ONE));
				ivjpnlSolid.setLayout(new java.awt.GridBagLayout());
				ivjpnlSolid.add(
					getradioPneumatic(),
					consGridBagConstraints1);
				ivjpnlSolid.add(
					getradioSolid(),
					consGridBagConstraints2);
				ivjpnlSolid.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlSolid.setMinimumSize(
					new java.awt.Dimension(127, 81));
				ivjpnlSolid.setBounds(129, 192, 123, 110);
				caButtonGroupTireType = new RTSButtonGroup();
				caButtonGroupTireType.add(getradioPneumatic());
				caButtonGroupTireType.add(getradioSolid());
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjpnlSolid;
	}

	/**
	 * Return the ivjpnlTruckTypeproperty value.
	 * 
	 * @return JPanel
	 */

	private JPanel getpnlTruckType()
	{
		if (ivjpnlTruckType == null)
		{
			try
			{
				ivjpnlTruckType = new javax.swing.JPanel();
				ivjpnlTruckType.setName("ivjpnlTruckType");
				ivjpnlTruckType.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(),
						SELECT_ONE));
				ivjpnlTruckType.setLayout(null);
				ivjpnlTruckType.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjpnlTruckType.setMinimumSize(
					new java.awt.Dimension(127, 152));
				ivjpnlTruckType.add(getradioTruck(), null);
				ivjpnlTruckType.add(getradioTruckTractor(), null);
				ivjpnlTruckType.add(getradioTrailer(), null);
				ivjpnlTruckType.add(getradioSemiTrailer(), null);
				ivjpnlTruckType.setBounds(8, 102, 117, 200);
				// defect 7893
				// Changed from ButtonGroup to RTSButtonGroup
				caButtonGroupTruckType = new RTSButtonGroup();
				caButtonGroupTruckType.add(getradioTruck());
				caButtonGroupTruckType.add(getradioTruckTractor());
				caButtonGroupTruckType.add(getradioTrailer());
				caButtonGroupTruckType.add(getradioSemiTrailer());
				// end defect 7893
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjpnlTruckType;
	}

	/**
	 * Return the ivjradioOutOfStateProducts property value.
	 * 
	 * @return JRadioButton
	 */
	private javax.swing.JRadioButton getradioOutOfStateProducts()
	{
		if (ivjradioOutOfStateProducts == null)
		{
			try
			{
				ivjradioOutOfStateProducts =
					new javax.swing.JRadioButton();
				ivjradioOutOfStateProducts.setName(
					"ivjradioOutOfStateProducts");
				//ivjradioOutOfStateProducts.setMnemonic(79);
				ivjradioOutOfStateProducts.setMnemonic(KeyEvent.VK_O);
				ivjradioOutOfStateProducts.setText(OUTSTATE_PRODS);
				ivjradioOutOfStateProducts.setMaximumSize(
					new java.awt.Dimension(149, 22));
				ivjradioOutOfStateProducts.setActionCommand(
					OUTSTATE_PRODS);
				ivjradioOutOfStateProducts.setMinimumSize(
					new java.awt.Dimension(149, 22));
				// user code begin {1}
				ivjradioOutOfStateProducts.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradioOutOfStateProducts;
	}

	/**
	 * Return the ivjradioPneumatic property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioPneumatic()
	{
		if (ivjradioPneumatic == null)
		{
			try
			{
				ivjradioPneumatic = new javax.swing.JRadioButton();
				ivjradioPneumatic.setName("ivjradioPneumatic");
				//ivjradioPneumatic.setMnemonic(80);
				ivjradioPneumatic.setMnemonic(KeyEvent.VK_P);
				ivjradioPneumatic.setText(PNEUMATIC);
				ivjradioPneumatic.setMaximumSize(
					new java.awt.Dimension(86, 22));
				ivjradioPneumatic.setActionCommand(PNEUMATIC);
				ivjradioPneumatic.setSelected(true);
				ivjradioPneumatic.setMinimumSize(
					new java.awt.Dimension(86, 22));
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
		return ivjradioPneumatic;
	}

	/**
	 * Return the ivjradioSemiTrailer property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioSemiTrailer()
	{
		if (ivjradioSemiTrailer == null)
		{
			try
			{
				ivjradioSemiTrailer = new JRadioButton();
				ivjradioSemiTrailer.setBounds(9, 147, 99, 22);
				ivjradioSemiTrailer.setName("ivjradioSemiTrailer");
				//ivjradioSemiTrailer.setMnemonic(77);
				ivjradioSemiTrailer.setMnemonic(KeyEvent.VK_M);
				ivjradioSemiTrailer.setText(SEMI_TRLR);
				ivjradioSemiTrailer.setMaximumSize(
					new java.awt.Dimension(95, 22));
				ivjradioSemiTrailer.setActionCommand(SEMI_TRLR);
				ivjradioSemiTrailer.setMinimumSize(
					new java.awt.Dimension(95, 22));
				// user code begin {1}
				ivjradioSemiTrailer.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradioSemiTrailer;
	}

	/**
	 * Return the ivjradioSolid property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioSolid()
	{
		if (ivjradioSolid == null)
		{
			try
			{
				ivjradioSolid = new JRadioButton();
				ivjradioSolid.setName("ivjradioSolid");
				//ivjradioSolid.setMnemonic(83);
				ivjradioSolid.setMnemonic(KeyEvent.VK_S);
				ivjradioSolid.setText(SOLID);
				ivjradioSolid.setMaximumSize(
					new java.awt.Dimension(53, 22));
				ivjradioSolid.setActionCommand(SOLID);
				ivjradioSolid.setMinimumSize(
					new java.awt.Dimension(53, 22));
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
		return ivjradioSolid;
	}

	/**
	 * Return the ivjradioTexasProducts property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioTexasProducts()
	{
		if (ivjradioTexasProducts == null)
		{
			try
			{
				ivjradioTexasProducts = new javax.swing.JRadioButton();
				ivjradioTexasProducts.setName("ivjradioTexasProducts");
				//ivjradioTexasProducts.setMnemonic(84);
				ivjradioTexasProducts.setMnemonic(KeyEvent.VK_T);
				ivjradioTexasProducts.setText(TEX_PRODS);
				ivjradioTexasProducts.setMaximumSize(
					new java.awt.Dimension(115, 22));
				ivjradioTexasProducts.setActionCommand(TEX_PRODS);
				ivjradioTexasProducts.setSelected(true);
				ivjradioTexasProducts.setMinimumSize(
					new java.awt.Dimension(115, 22));
				// user code begin {1}
				ivjradioTexasProducts.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradioTexasProducts;
	}

	/**
	 * Return the ivjradioTrailer property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioTrailer()
	{
		if (ivjradioTrailer == null)
		{
			try
			{
				ivjradioTrailer = new javax.swing.JRadioButton();
				ivjradioTrailer.setBounds(9, 119, 99, 22);
				ivjradioTrailer.setName("ivjradioTrailer");
				//ivjradioTrailer.setMnemonic(73);
				ivjradioTrailer.setMnemonic(KeyEvent.VK_I);
				ivjradioTrailer.setText(TRAILER);
				ivjradioTrailer.setMaximumSize(
					new java.awt.Dimension(62, 22));
				ivjradioTrailer.setActionCommand(TRAILER);
				ivjradioTrailer.setMinimumSize(
					new java.awt.Dimension(62, 22));
				// user code begin {1}
				ivjradioTrailer.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradioTrailer;
	}

	/**
	 * Return the ivjradioTruck property value.
	 * 
	 * @return JRadioButton
	 */

	private JRadioButton getradioTruck()
	{
		if (ivjradioTruck == null)
		{
			try
			{
				ivjradioTruck = new JRadioButton();
				ivjradioTruck.setBounds(9, 63, 99, 22);
				ivjradioTruck.setName("ivjradioTruck");
				//ivjradioTruck.setMnemonic(85);
				ivjradioTruck.setMnemonic(KeyEvent.VK_U);
				ivjradioTruck.setText(TRUCK_LBL);
				ivjradioTruck.setMaximumSize(
					new java.awt.Dimension(58, 22));
				ivjradioTruck.setActionCommand(TRUCK_LBL);
				// defaults to selected 
				ivjradioTruck.setSelected(true);
				ivjradioTruck.setMinimumSize(
					new java.awt.Dimension(58, 22));
				// user code begin {1}
				ivjradioTruck.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradioTruck;
	}

	/**
	 * Return the btnTruckTractor property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioTruckTractor()
	{
		if (ivjradioTruckTractor == null)
		{
			try
			{
				ivjradioTruckTractor = new javax.swing.JRadioButton();
				ivjradioTruckTractor.setBounds(9, 91, 104, 22);
				ivjradioTruckTractor.setName("ivjradioTruckTractor");
				//ivjradioTruckTractor.setMnemonic(67);
				ivjradioTruckTractor.setMnemonic(KeyEvent.VK_C);
				ivjradioTruckTractor.setText(TRUCK_TRACTOR);
				ivjradioTruckTractor.setMaximumSize(
					new java.awt.Dimension(104, 22));
				ivjradioTruckTractor.setActionCommand(TRUCK_TRACTOR);
				ivjradioTruckTractor.setMinimumSize(
					new java.awt.Dimension(104, 22));
				// user code begin {1}
				ivjradioTruckTractor.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjradioTruckTractor;
	}

	/**
	 * Return the stcLblCarryCapacity property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblCarryCapacity()
	{
		if (ivjstcLblCarryCapacity == null)
		{
			try
			{
				ivjstcLblCarryCapacity = new javax.swing.JLabel();
				ivjstcLblCarryCapacity.setName("stcLblCarryCapacity");
				ivjstcLblCarryCapacity.setText(CARRY_CAP);
				ivjstcLblCarryCapacity.setMaximumSize(
					new java.awt.Dimension(103, 14));
				ivjstcLblCarryCapacity.setMinimumSize(
					new java.awt.Dimension(103, 14));
				ivjstcLblCarryCapacity.setHorizontalAlignment(4);
				ivjstcLblCarryCapacity.setBounds(10, 135, 112, 14);
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
		return ivjstcLblCarryCapacity;
	}

	/**
	 * Return the ivjstcLblDash1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDash1()
	{
		if (ivjstcLblDash1 == null)
		{
			try
			{
				ivjstcLblDash1 = new javax.swing.JLabel();
				ivjstcLblDash1.setName("ivjstcLblDash1");
				ivjstcLblDash1.setText(DASH);
				ivjstcLblDash1.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash1.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash1.setBounds(257, 330, 7, 20);
				ivjstcLblDash1.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
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
		return ivjstcLblDash1;
	}

	/**
	 * Return the ivjstcLblEffectiveDate property value.
	 * 
	 * @return swing.JLabel
	 */
	private JLabel getstcLblEffectiveDate()
	{
		if (ivjstcLblEffectiveDate == null)
		{
			try
			{
				ivjstcLblEffectiveDate = new javax.swing.JLabel();
				ivjstcLblEffectiveDate.setBounds(34, 310, 95, 14);
				ivjstcLblEffectiveDate.setName(
					"ivjstcLblEffectiveDate");
				ivjstcLblEffectiveDate.setText(EFF_DT);
				ivjstcLblEffectiveDate.setMaximumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEffectiveDate.setMinimumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEffectiveDate.setHorizontalAlignment(4);
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
		return ivjstcLblEffectiveDate;
	}

	/**
	 * Return the ivjstcLblEmptyWeight property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEmptyWeight()
	{
		if (ivjstcLblEmptyWeight == null)
		{
			try
			{
				ivjstcLblEmptyWeight = new javax.swing.JLabel();
				ivjstcLblEmptyWeight.setName("ivjstcLblEmptyWeight");
				ivjstcLblEmptyWeight.setText(EMPTY_WT);
				ivjstcLblEmptyWeight.setMaximumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEmptyWeight.setMinimumSize(
					new java.awt.Dimension(81, 14));
				ivjstcLblEmptyWeight.setHorizontalAlignment(4);
				ivjstcLblEmptyWeight.setBounds(29, 109, 93, 16);
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
		return ivjstcLblEmptyWeight;
	}

	/**
	 * Return the ivjstcLblEntryPoint property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblEntryPoint()
	{
		if (ivjstcLblEntryPoint == null)
		{
			try
			{
				ivjstcLblEntryPoint = new JLabel();
				ivjstcLblEntryPoint.setName("stcLblEntryPoint");
				ivjstcLblEntryPoint.setText(ENTRY_PT);
				ivjstcLblEntryPoint.setMaximumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblEntryPoint.setMinimumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblEntryPoint.setHorizontalAlignment(4);
				ivjstcLblEntryPoint.setBounds(37, 177, 84, 19);
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
		return ivjstcLblEntryPoint;
	}

	/**
	 * Return the stcLblExpirationDate property value.
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
				ivjstcLblExpirationDate.setBounds(32, 331, 97, 14);
				ivjstcLblExpirationDate.setName("stcLblExpirationDate");
				ivjstcLblExpirationDate.setText(EXP_DT);
				ivjstcLblExpirationDate.setMaximumSize(
					new java.awt.Dimension(89, 14));
				ivjstcLblExpirationDate.setMinimumSize(
					new java.awt.Dimension(89, 14));
				ivjstcLblExpirationDate.setHorizontalAlignment(4);
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
		return ivjstcLblExpirationDate;
	}

	/**
	 * Return the stcLblGrossWeight property value.
	 * 
	 * @return JLabel
	 */

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
					new java.awt.Dimension(80, 14));
				ivjstcLblGrossWeight.setMinimumSize(
					new java.awt.Dimension(80, 14));
				ivjstcLblGrossWeight.setHorizontalAlignment(4);
				ivjstcLblGrossWeight.setBounds(29, 158, 93, 13);
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
		return ivjstcLblGrossWeight;
	}

	/**
	 * Return the stcLblOwnerAddress property value.
	 * @return JLabel
	 */

	private JLabel getstcLblOwnerAddress()
	{
		if (ivjstcLblOwnerAddress == null)
		{
			try
			{
				ivjstcLblOwnerAddress = new JLabel();
				ivjstcLblOwnerAddress.setName("stcLblOwnerAddress");
				ivjstcLblOwnerAddress.setText(OWNR_ADDR);
				ivjstcLblOwnerAddress.setMaximumSize(
					new java.awt.Dimension(92, 14));
				ivjstcLblOwnerAddress.setMinimumSize(
					new java.awt.Dimension(92, 14));
				ivjstcLblOwnerAddress.setBounds(51, 267, 124, 14);
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
		return ivjstcLblOwnerAddress;
	}

	/**
	 * Return the stcLblOwnerName property value.
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
				ivjstcLblOwnerName.setName("stcLblOwnerName");
				ivjstcLblOwnerName.setText(OWNR_NAME);
				ivjstcLblOwnerName.setMaximumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblOwnerName.setMinimumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblOwnerName.setBounds(49, 201, 90, 16);
				ivjstcLblOwnerName.setHorizontalAlignment(
					SwingConstants.LEFT);
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
		return ivjstcLblOwnerName;
	}

	/**
	 * Return the stcLblPlateNo property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblPlateNo()
	{
		if (ivjstcLblPlateNo == null)
		{
			try
			{
				ivjstcLblPlateNo = new JLabel();
				ivjstcLblPlateNo.setName("stcLblPlateNo");
				ivjstcLblPlateNo.setText(PLATE_NO);
				ivjstcLblPlateNo.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblPlateNo.setMinimumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblPlateNo.setHorizontalAlignment(4);
				ivjstcLblPlateNo.setBounds(29, 24, 93, 14);
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
		return ivjstcLblPlateNo;
	}

	/**
	 * Return the stcLblStateRegistered property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblStateRegistered()
	{
		if (ivjstcLblStateRegistered == null)
		{
			try
			{
				ivjstcLblStateRegistered = new JLabel();
				ivjstcLblStateRegistered.setName(
					"stcLblStateRegistered");
				ivjstcLblStateRegistered.setText(STATE_REGIS);
				ivjstcLblStateRegistered.setMaximumSize(
					new java.awt.Dimension(98, 14));
				ivjstcLblStateRegistered.setMinimumSize(
					new java.awt.Dimension(98, 14));
				ivjstcLblStateRegistered.setHorizontalAlignment(4);
				ivjstcLblStateRegistered.setBounds(6, 46, 116, 14);
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
		return ivjstcLblStateRegistered;
	}

	/**
	 * Return the stcLblTonnage property value.
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
				ivjstcLblTonnage.setName("stcLblTonnage");
				ivjstcLblTonnage.setText(TONNAGE);
				ivjstcLblTonnage.setMaximumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblTonnage.setMinimumSize(
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
		return ivjstcLblTonnage;
	}

	/**
	 * Return the stcLblVIN property value.
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
				ivjstcLblVIN.setName("stcLblVIN");
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setMinimumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setHorizontalAlignment(4);
				ivjstcLblVIN.setBounds(29, 88, 93, 15);
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
	 * Return the stcLblYearMake property value.
	 * @return JLabel
	 */

	private JLabel getstcLblYearMake()
	{
		if (ivjstcLblYearMake == null)
		{
			try
			{
				ivjstcLblYearMake = new JLabel();
				ivjstcLblYearMake.setName("stcLblYearMake");
				ivjstcLblYearMake.setText(YR_MAKE);
				ivjstcLblYearMake.setMaximumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYearMake.setMinimumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYearMake.setHorizontalAlignment(4);
				ivjstcLblYearMake.setBounds(29, 67, 93, 14);
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
		return ivjstcLblYearMake;
	}

	/**
	 * Return the txtCarryingCapacity property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtCarryingCapacity()
	{
		if (ivjtxtCarryingCapacity == null)
		{
			try
			{
				ivjtxtCarryingCapacity = new RTSInputField();
				ivjtxtCarryingCapacity.setName("txtCarryingCapacity");
				ivjtxtCarryingCapacity.setManagingFocus(false);
				ivjtxtCarryingCapacity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtCarryingCapacity.setBounds(142, 132, 88, 20);
				ivjtxtCarryingCapacity.setHorizontalAlignment(4);
				// defect 10127 
				ivjtxtCarryingCapacity.setInput(
					RTSInputField.NUMERIC_ONLY);
				// end defect 10127 

				ivjtxtCarryingCapacity.setMaxLength(6);
				// user code begin {1}
				ivjtxtCarryingCapacity.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtCarryingCapacity;
	}

	/**
	 * Return the txtEmptyWeight property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtEmptyWeight()
	{
		if (ivjtxtEmptyWeight == null)
		{
			try
			{
				ivjtxtEmptyWeight = new RTSInputField();
				ivjtxtEmptyWeight.setName("txtEmptyWeight");
				ivjtxtEmptyWeight.setManagingFocus(false);
				ivjtxtEmptyWeight.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEmptyWeight.setBounds(142, 108, 88, 22);
				ivjtxtEmptyWeight.setHorizontalAlignment(
					JTextField.RIGHT);

				// defect 10127 
				ivjtxtEmptyWeight.setInput(RTSInputField.NUMERIC_ONLY);
				// end defect 10127 

				ivjtxtEmptyWeight.setMaxLength(6);
				// user code begin {1}
				ivjtxtEmptyWeight.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtEmptyWeight;
	}

	/**
	 * Return the txtOwnerName11 property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtEntryPoint()
	{
		if (ivjtxtEntryPoint == null)
		{
			try
			{
				ivjtxtEntryPoint = new RTSInputField();
				ivjtxtEntryPoint.setName("txtEntryPoint");
				ivjtxtEntryPoint.setManagingFocus(false);
				ivjtxtEntryPoint.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtEntryPoint.setBackground(java.awt.Color.white);
				ivjtxtEntryPoint.setBounds(142, 175, 156, 22);

				// defect 10127 
				ivjtxtEntryPoint.setInput(RTSInputField.DEFAULT);
				ivjtxtEntryPoint.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// end defect 10127 

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
		return ivjtxtEntryPoint;
	}

	/**
	 * Return the txtMake property value.
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
				ivjtxtMake.setName("txtMake");
				ivjtxtMake.setManagingFocus(false);
				ivjtxtMake.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMake.setBounds(213, 64, 60, 20);
				// defect 10127 
				ivjtxtMake.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				// end defect 10127 
				ivjtxtMake.setMaxLength(4);
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
		return ivjtxtMake;
	}

	/**
	 * Return the ivjtxtOwnerCity property value.
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
				ivjtxtOwnerCity.setBounds(49, 331, 124, 20);
				// defect 10127 
				ivjtxtOwnerCity.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// end defect 10127
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
	 * Return the txtOwnerName1 property value.
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
				ivjtxtOwnerName1.setName("txtOwnerName1");
				ivjtxtOwnerName1.setManagingFocus(false);
				ivjtxtOwnerName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName1.setBounds(49, 218, 249, 20);
				// defect 10127
				ivjtxtOwnerName1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// end defect 10127
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
	 * Return the txtOwnerName2 property value.
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
				ivjtxtOwnerName2.setName("txtOwnerName2");
				ivjtxtOwnerName2.setManagingFocus(false);
				ivjtxtOwnerName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName2.setBounds(49, 241, 249, 20);
				// defect 10127
				ivjtxtOwnerName2.setInput(RTSInputField.DEFAULT);
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
	 * Return the txtOwnerAddressState property value.
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
				ivjtxtOwnerState.setBounds(178, 331, 29, 20);
				// defect 10127
				ivjtxtOwnerState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtOwnerState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// end defect 10127
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
				ivjtxtOwnerStreet1.setName("ivjtxtOwnerStreet1");
				ivjtxtOwnerStreet1.setManagingFocus(false);
				ivjtxtOwnerStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet1.setBounds(49, 283, 249, 20);
				// defect 10127 
				ivjtxtOwnerStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10127 
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
				ivjtxtOwnerStreet2.setManagingFocus(false);
				ivjtxtOwnerStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet2.setBounds(49, 307, 249, 20);
				// defect 10127 
				ivjtxtOwnerStreet2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10127 
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
				ivjtxtOwnerZpcd.setManagingFocus(false);
				ivjtxtOwnerZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerZpcd.setText(DEFAULT_ZIP);
				ivjtxtOwnerZpcd.setBounds(214, 331, 41, 20);
				// defect 10127
				ivjtxtOwnerZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// end defect 10127 
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
				ivjtxtOwnerZpcdP4.setText(DEFAULT_ZIP4);
				ivjtxtOwnerZpcdP4.setBounds(265, 331, 33, 20);
				// defect 10127
				ivjtxtOwnerZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// end defect 10127 

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
	 * Return the txtPlateNo property value.
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
				ivjtxtPlateNo.setName("txtPlateNo");
				ivjtxtPlateNo.setManagingFocus(false);
				ivjtxtPlateNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPlateNo.setBounds(142, 20, 120, 21);
				// defect 10127
				ivjtxtPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPlateNo.setMaxLength(CommonConstant.LENGTH_PLTNO);
				// end defect 10127 
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
		return ivjtxtPlateNo;
	}

	/**
	 * Return the txtStateRegistered property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtStateRegistered()
	{
		if (ivjtxtStateRegistered == null)
		{
			try
			{
				ivjtxtStateRegistered = new RTSInputField();
				ivjtxtStateRegistered.setName("txtStateRegistered");
				ivjtxtStateRegistered.setManagingFocus(false);
				ivjtxtStateRegistered.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtStateRegistered.setBounds(142, 43, 63, 20);
				// defect 10127 
				ivjtxtStateRegistered.setMaxLength(
					CommonConstant.LENGTH_STATE);
				ivjtxtStateRegistered.setInput(
					RTSInputField.ALPHA_ONLY);
				// end defect 10127 
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
		return ivjtxtStateRegistered;
	}

	/**
	 * Return the txtTonnage property value.
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
				ivjtxtTonnage.setName("txtTonnage");
				ivjtxtTonnage.setManagingFocus(false);
				ivjtxtTonnage.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtTonnage.setHorizontalAlignment(4);
				ivjtxtTonnage.addFocusListener(this);
				// defect 10127 
				ivjtxtTonnage.setInput(RTSInputField.DOLLAR_ONLY);
				// end defect 10127 
				ivjtxtTonnage.setMaxLength(5);
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
		return ivjtxtTonnage;
	}

	/**
	 * Return the txtVIN property value.
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
				ivjtxtVIN.setName("txtVIN");
				ivjtxtVIN.setManagingFocus(false);
				ivjtxtVIN.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVIN.setBounds(142, 85, 156, 21);
				// defect 10127 
				ivjtxtVIN.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				// end defect 10127 
				ivjtxtVIN.setMaxLength(22);
				// user code begin {1}
				ivjtxtVIN.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtxtVIN;
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
				ivjtxtYear.setManagingFocus(false);
				ivjtxtYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtYear.setBounds(142, 64, 64, 20);
				ivjtxtYear.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtYear.setMaxLength(CommonConstant.LENGTH_YEAR);
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
		return ivjtxtYear;
	}

	/**
	 * validate carrying capacity input value.
	 *
	 * @param aeRTSEx RTSException
	 */
	private void handleCaryngCapField(RTSException aeRTSEx)
	{
		// Validate carrying capacity based on tonnage
		String lsCarryngCap = gettxtCarryingCapacity().getText();

		int liCarryngCap =
			lsCarryngCap.equals("")
				? 0
				: Integer.parseInt(lsCarryngCap);

		if (caCommVehWtData != null
			&& liCarryngCap < caCommVehWtData.getMinCaryngCap()
			&& gettxtTonnage().isEnabled())
		{
			// defect 10127 
			// 200 
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_MUST_INCR_CARRYING_CAPACITY),
				gettxtCarryingCapacity());
		}
		// Calculate vehicle gross weight                                               
		else
		{
			handleVehGrossWt(aeRTSEx);
		}
		// end defect 10127		
	}

	/**
	 * Manage enable/disable diesel and tonnage fields based on 
	 * truck type.
	 */
	private void handleDieselAndTonnageFields()
	{
		// Check vehicle type to disable diesel
		// defect 10127
		boolean lbTruckRadio = isTruckSelected();

		// Enable/Disable 
		getchkDiesel().setEnabled(lbTruckRadio);
		getstcLblTonnage().setEnabled(lbTruckRadio);
		gettxtTonnage().setEnabled(lbTruckRadio);

		if (!lbTruckRadio
			|| (lbTruckRadio && gettxtTonnage().isEmpty()))
		{
			gettxtTonnage().setText(new Dollar("0").toString());

			if (!lbTruckRadio)
			{
				getchkDiesel().setSelected(false);
			}
		}
		// end defect 10127 
	}

	/**
	 * Manage show/hide entry point field.
	 */
	private void handleEntryPointField(boolean abOSSelected)
	{
		// defect 10127 
		// Standardized handling of visibility   
		gettxtEntryPoint().setEnabled(abOSSelected);
		gettxtEntryPoint().setVisible(abOSSelected);
		getstcLblEntryPoint().setVisible(abOSSelected);
		// end defect 10127 
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
	 * Determine the non-resident permit type and set data accordingly.
	 */
	private void handleNonResPermitType()
	{
		// defect 10127 
		// Set the EffDate, ExpDate according to PrmtValidtyPeriod 
		boolean lbOSProd = getradioOutOfStateProducts().isSelected();

		String lsAcctItmCd =
			lbOSProd
				? NON_RESIDENT_OUTSTATE_PERMIT
				: NON_RESIDENT_INSTATE_PERMIT;

		caTimedPrmtData.setTimedPrmtType(lsAcctItmCd);
		caTimedPrmtData.setItmCd(lsAcctItmCd);

		// Effective Date 
		MiscellaneousRegClientBusiness.setTimedPrmtTime(
			caTimedPrmtData);
		// Expiration Date 
		MiscellaneousRegClientBusiness.setTimedPrmtExpDtAndTime(
			caTimedPrmtData);

		getlblEffectiveDate().setText(
			caTimedPrmtData.getRTSDateEffDt().toString());
		getlblExpirationDate().setText(
			caTimedPrmtData.getRTSDateExpDt().toString());

		// Enable EntryPoint if Out-of-State, else clear/disable   
		handleEntryPointField(lbOSProd);
		// end defect 10127 
	}

	/**
	 * validate tonnage field.
	 *
	 * @param aeRTSEx RTSException
	 */
	private void handleTonnageField(RTSException aeRTSEx)
	{
		// defect 10127 
		if (isTruckSelected())
		{
			try
			{
				if (gettxtTonnage().isEmpty())
				{
					gettxtTonnage().setText(new Dollar(0).toString());
				}

				Dollar laDollarTonnage =
					new Dollar(gettxtTonnage().getText());

				// valid tonnage from Commercial Vehicle Wts cache
				caCommVehWtData =
					CommercialVehicleWeightsCache.getCommVehWts(
						laDollarTonnage);

				if (caCommVehWtData == null)
				{
					// 201
					aeRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_VEH_TONNAGE_INVALID),
						gettxtTonnage());
				}
			}
			catch (NumberFormatException aeNFEx)
			{
				// 201 
				aeRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_VEH_TONNAGE_INVALID),
					gettxtTonnage());
			}
		}
		// end defect 10127 
	}

	/**
	 * Validate vehicle gross weight.
	 * 
	 * @param aeRTSEx
	 */
	private void handleVehGrossWt(RTSException aeRTSEx)
	{
		// defect 10127 
		// added RTSException as parameter, removed boolean return
		// Carrying Capacity 
		String lsCarryngCap = gettxtCarryingCapacity().getText();
		int liCarryngCap =
			lsCarryngCap.equals("")
				? 0
				: Integer.parseInt(lsCarryngCap);

		// Empty Weight 
		String lsEmptyWt = gettxtEmptyWeight().getText();
		int liEmptyWt =
			lsEmptyWt.equals("") ? 0 : Integer.parseInt(lsEmptyWt);

		// Calculate vehicle gross weight
		int liVehGrossWt = liEmptyWt + liCarryngCap;

		// Test gross weight not greater than 80000 pounds
		if (liVehGrossWt > MAX_GROSS_WEIGHT)
		{
			aeRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_GROSS_WT_EXCEEDS_MAX),
				gettxtCarryingCapacity());
		}
		else
		{
			getlblGrossWeight().setText(Integer.toString(liVehGrossWt));
		}
		// end defect 10127 
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
			setName("FrmNonResidentAgricultureMRG013");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(595, 478);
			setTitle("Non-Resident Agriculture          MRG013");
			setContentPane(
				getFrmNonResidentAgricultureMRG013ContentPane1());

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
					null,
					null,
					null,
					null,
					ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
					ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
					CommonConstant.TX_NOT_DEFAULT_STATE);
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
	 * Return boolean based on Truck Type selection
	 * 
	 * @return
	 */
	private boolean isTruckSelected()
	{
		return getradioTruck().isSelected()
			|| getradioTruckTractor().isSelected();
	}

	/**
	 * Set TimedPrmtData property based upon radio button selection.
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		// defect 10127 
		// add clearAllColor; Restrict action to selected.  
		//  handlexxx need only be called once. 
		clearAllColor(this);

		if (aaIE.getSource() instanceof JRadioButton)
		{
			JRadioButton laRadio = (JRadioButton) aaIE.getSource();

			if (laRadio.isSelected())
			{
				if (laRadio.equals(getradioTexasProducts())
					|| laRadio.equals(getradioOutOfStateProducts()))
				{
					handleNonResPermitType();
				}
				else if (
					laRadio.equals(getradioTruck())
						|| laRadio.equals(getradioTruckTractor())
						|| laRadio.equals(getradioTrailer())
						|| laRadio.equals(getradioSemiTrailer()))
				{
					handleDieselAndTonnageFields();
				}
			}
		}
		// end defect 10127		
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
		if (aaDataObject instanceof Vector)
		{
			Vector lvIsNextVC = (Vector) aaDataObject;
			if (lvIsNextVC != null)
			{
				if (lvIsNextVC.size() == 2)
				{
					if (lvIsNextVC.get(0) instanceof String)
					{
						// defect 10112 
						getController().processData(
						// VCDisabledParkingCardMRG002.REDIRECT,						
						VCNonResidentAgricultureMRG013.REDIRECT,
							lvIsNextVC);
						// end defect 10112 
						return;
					}
				}
				else
				{
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						ERRMSG_DATA_MISSING,
						ERROR).displayError(
						this);
					return;
				}
			}
		}

		// defect 10127 
		// Default Tonnage 
		gettxtTonnage().setText(new Dollar("0").toString());

		// Handle Effective Date, End Date 
		caTimedPrmtData.setRTSDateEffDt(RTSDate.getCurrentDate());
		handleNonResPermitType();

		// Initialize Diesel checkbox, Tonnage field
		//  Note: Truck radio defaults to selected 
		handleDieselAndTonnageFields();
		// end dect 10127 
	}

	/**
	 * Set input data values to data objects.
	 * 
	 */
	private void setInputFields()
	{
		// defect 10127 
		// Use local variables/ caNameAddrComp  
		RegistrationData laRegData = new RegistrationData();
		VehicleData laVehData = new VehicleData();
		OwnerData laOwnerData = new OwnerData();

		// VehicleData
		laVehData.setDieselIndi(getchkDiesel().isSelected() ? 1 : 0);
		laVehData.setVehBdyType("");
		laVehData.setVehEmptyWt(
			Integer.parseInt(gettxtEmptyWeight().getText()));
		laVehData.setVehMk(gettxtMake().getText());
		laVehData.setVehModlYr(
			Integer.parseInt(gettxtYear().getText()));
		laVehData.setVin(gettxtVIN().getText());
		laVehData.setVehTon(new Dollar("0"));
		if (gettxtTonnage().isEnabled() && !gettxtTonnage().isEmpty())
		{
			laVehData.setVehTon(new Dollar(gettxtTonnage().getText()));
		}

		// RegistrationData 		
		laRegData.setVehGrossWt(
			Integer.parseInt(getlblGrossWeight().getText()));

		if (getradioPneumatic().isSelected())
		{
			laRegData.setTireTypeCd(TIRE_TYPE_PNEUMATIC);
		}
		else if (getradioSolid().isSelected())
		{
			laRegData.setTireTypeCd(TIRE_TYPE_SOLID);
		}
		laRegData.setRegPltNo(gettxtPlateNo().getText());
		laRegData.setVehCaryngCap(
			Integer.parseInt(gettxtCarryingCapacity().getText()));
		laRegData.setRegClassCd(
			isTruckSelected() ? TRUCK_REG_CLASS : TRAILER_REG_CLASS);

		// OwnerData 
		// Assign via caNameAddrComp 
		caNameAddrComp.setNameAddressToDataObject(laOwnerData);

		// TimedPermitData 
		caTimedPrmtData.setVehRegState(
			gettxtStateRegistered().getText());
		caTimedPrmtData.setEntryOriginPnt(gettxtEntryPoint().getText());

		caMFVehData.setRegData(laRegData);
		caMFVehData.setVehicleData(laVehData);
		caMFVehData.setOwnerData(laOwnerData);
		// end defect 10127

	}

	/**
	 * Validate input fields.
	 *
	 * @return boolean
	 */
	private boolean validateFields()
	{
		// defect 10299
		UtilityMethods.trimRTSInputField(this);
		// end defect 10299
		
		RTSException leRTSEx = new RTSException();

		// defect 10112 / 10127  
		// Implement ErrorsConstant, isEmpty()
		boolean lbValid = true;

		// Truck vs. TruckTractor 
		if (isTruckSelected())
		{
			handleTonnageField(leRTSEx);
		}

		// PlateNo  
		if (gettxtPlateNo().isEmpty())
		{
			// 150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPlateNo());
		}

		// State Registered 
		if (gettxtStateRegistered().isEmpty())
		{
			// 150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtStateRegistered());
		}
		else
		{
			if (gettxtStateRegistered()
				.getText()
				.equalsIgnoreCase(CommonConstant.STR_TX))
			{
				// Validate vehicle registered state is not Texas for 
				// non-resident agriculture
				// 162
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant
							.ERR_NUM_TX_INVALID_ANOTHER_STATE),
					gettxtStateRegistered());
			}
			else if (!gettxtStateRegistered().isValidState())
			{
				// 2004
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_INVALID_STATE_REENTER),
					gettxtStateRegistered());
			}
		}

		// Model Year 
		if (gettxtYear().isEmpty())
		{
			// 150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtYear());
		}
		else if (
			CommonValidations.isInvalidYearModel(
				gettxtYear().getText()))
		{
			// 2006 
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_MODEL_YEAR_NOT_VALID),
				gettxtYear());
		}

		// Make 
		if (gettxtMake().isEmpty())
		{
			//	150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtMake());
		}

		// VIN 
		if (gettxtVIN().isEmpty())
		{
			//	150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtVIN());
		}

		// Empty Weight 
		// defect 4474  
		if (gettxtEmptyWeight().isEmpty()
			|| Integer.parseInt(gettxtEmptyWeight().getText()) == 0)
			// end defect 4474  
		{
			//	150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtEmptyWeight());
		}
		else
		{
			// handleEmptyWtField just calls handleVehGrossWt
			handleVehGrossWt(leRTSEx);
			//handleEmptyWtField(leRTSEx);
		}

		handleCaryngCapField(leRTSEx);

		// Carrying Capacity
		// defect 6984
		if (gettxtCarryingCapacity().isEmpty()
			|| Integer.parseInt(gettxtCarryingCapacity().getText()) == 0)
		{
			//	150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtCarryingCapacity());
		}
		// end defect 6984

		// Out of State Products 
		if (getradioOutOfStateProducts().isSelected())
		{
			if (gettxtEntryPoint().isEmpty())
			{
				//	150
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtEntryPoint());
			}
		}

		// defect 10127 
		caNameAddrComp.validateNameAddressFields(leRTSEx);
		// end defect 10127 

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="21,17"
