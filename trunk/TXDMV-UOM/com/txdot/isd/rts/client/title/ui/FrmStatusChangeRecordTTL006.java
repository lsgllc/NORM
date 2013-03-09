package com.txdot.isd.rts.client.title.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.DocumentTypesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.cache.VehicleBodyTypesCache;
import com.txdot.isd.rts.services.cache.VehicleMakesCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 *
 * FrmStatusChangeRecordTTL006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			Made changes for validations
 * T Pederson   04/30/2002  Don't allow spaces to be entered in input 
 * 							field
 * T Pederson   05/10/2002  Increase size of doc type description field
 * MAbs			06/03/2002	Made sure focus is never lost on 
 * 							Shift+Tab CQU100004164
 * J Rue		03/11/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	Increase length of selected fields
 * 							not displaying entire text
 * 							Visual Editor
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884  Ver 5.2.3       
 * S Johnston	06/22/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyReleased, keyTyped
 * 							modify keyPressed
 * 							defect 8240 Ver 5.2.3            
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/19/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/04/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * J Ralph		12/02/205	Fixed NULL pointer in itemStateChanged()
 * 							Java 1.4 impact
 * 							Remove text color from gettxtAOwnerAddr() 
 * 							modify getJComboBoxMake(),gettxtAOwnerAddr() 								
 * 							defect 7898 Ver 5.2.3
 * J Ralph		12/09/2005	Fixed arrow key handling to follow 5.2.3
 * 							standard.
 * 							modify keyPressed()
 * 							defect 7898 Ver 5.2.3   
 * T Pederson	12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify populateBodyTypes() and 
 * 							populateVehicleMakes().
 * 							defect 8479 Ver 5.2.3  
 * T. Pederson	12/30/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * Jeff S.		01/04/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), focusGained(), 
 * 								focusLost(), statusType
 * 							modify getlstIndiDescription(), 
 * 								getradioCancelRegistration(), 
 * 								getradioMiscellaneousRemarks(), 
 * 								getradioRegistrationRefund(), 
 * 								getradioStolenSRS(), initialize(),
 * 								getradioTitleSurrendered(), 
 * 								getradioVehicleJunked(),
 * 								getpnlGrpSelectChoice()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	10/12/2006	Do not present "0" for Exp Mo / Exp Yr
 * 							modify setData()
 * 							defect 8900 Ver Exempts  
 * K Harrell	02/27/2007	Remove reference to InvProcsngCd
 * 							delete getBuilderData()
 * 							modify setData(), isPlateOutsideScope()
 * 							defect 9085 Ver Special Plates
 * K Harrell	05/07/2007	Use SystemProperty.isCounty()
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/01/2009	modify setData(), getlstIndiDescription()
 * 							defect 9971 Ver Defect_POS_E    
 * K Harrell	04/14/2009	Enlarge Scroll Panel so that do not get 
 * 							horizontal bar. Use CommonConstant for Font. 
 * 							defect 9971 Ver Defect_POS_E   
 * K Harrell	05/29/2009	Correct tabbing in No Record Found via 
 * 							restructure, realignment via Visual Editor, 
 * 							 enlarge screen, move combo boxes from 
 * 							 jPanel2; Additional cleanup including use
 * 							 of  constants for mnemonics, vertical 
 * 							 alignment, sort members.
 * 							add validateData() 
 * 							delete ivjPanel2, getJPanel2(), etched, 
 * 							  getEtched(), setEtched() 
 *							modify SELECT_CHOICE, setData(),
 *							  getFrmStatusChangeRecordTTL006ContentPane1(),
 *							  actionPerformed()
 *							defect 10038 Ver Defect_POS_F
 * K Harrell	06/01/2009	Convert I/O to 1/0 when VIN enabled.
 * 							ciSelectedNum not used. Disable Remarks List
 * 							Box. 
 * 							add FocusListener, focusLost(), focusGained()
 * 							add convertVIN()
 * 							delete ciSelectedNum
 * 							modify actionPerformed(), setData()
 * 							defect 10038 Ver Defect_POS_F          
 * K Harrell	07/02/2009	Implement new OwnerData, new AddressData 
 * 							method
 * 							modify setData()
 * 							defect 10112 Ver Defect_POS_F                                                     
 * ---------------------------------------------------------------------
 */
/**
 * This form is used to display the vehicle record that the status 
 * change will be performed on.
 * 
 * @version	Defect_POS_F	07/02/2009
 * @author	Todd Pederson
 * <br>Creation Date:		07/01/2001 12:46:14
 */
public class FrmStatusChangeRecordTTL006
	extends RTSDialogBox
	implements ActionListener, ItemListener, FocusListener
{
	private ButtonPanel ivjButtonPanel = null;
	private JPanel ivjFrmStatusChangeRecordTTL006ContentPane1 = null;
	private JComboBox ivjJComboBoxBodyStyle = null;
	private JComboBox ivjJComboBoxMake = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblDocNo = null;
	private JLabel ivjlblDocTypeCodeDesc = null;
	private JLabel ivjlblOwnerId = null;
	private JLabel ivjlblOwnerTitleName1 = null;
	private JLabel ivjlblOwnerTitleName2 = null;
	private JLabel ivjlblRegExpMo = null;
	private JLabel ivjlblRegExpYr = null;
	private JLabel ivjlblRegPlateNo = null;
	private JLabel ivjlblResComptCntyNo = null;
	private JLabel ivjlblTitleIssueDate = null;
	private JLabel ivjlblVehicleBodyType = null;
	private JList ivjlstIndiDescription = null;
	private JPanel ivjpnlGrpSelectChoice = null;
	private JRadioButton ivjradioCancelRegistration = null;
	private JRadioButton ivjradioMiscellaneousRemarks = null;
	private JRadioButton ivjradioRegistrationRefund = null;
	private JRadioButton ivjradioStolenSRS = null;
	private JRadioButton ivjradioTitleSurrendered = null;
	private JRadioButton ivjradioVehicleJunked = null;
	private JLabel ivjstcLblBodyStyle = null;
	private JLabel ivjstcLblCountyNo = null;
	private JLabel ivjstcLblDocumentNo = null;
	private JLabel ivjstcLblIssued = null;
	private JLabel ivjstcLblModel = null;
	private JLabel ivjstcLblOwnerAddress = null;
	private JLabel ivjstcLblOwnerId = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblRegExpires = null;
	private JLabel ivjstcLblSlash = null;
	private JLabel ivjstcLblYearMake = null;
	private JLabel ivjstcVIN = null;
	private RTSTextArea ivjtxtAOwnrAddr = null;
	private RTSInputField ivjtxtVehicleMake = null;
	private RTSInputField ivjtxtVehicleModel = null;
	private RTSInputField ivjtxtVehicleModelYear = null;
	private RTSInputField ivjtxtVIN = null;

	// defect 10038 
	// int 
	//private int ciSelectedNum = 0;
	// end defect 10038 

	// Objects 
	private VehicleInquiryData caVehInqData = null;
	private MFVehicleData caMFVehicleData = null;
	private Vector cvIndicator = null;

	// Constants 
	private final static String ADDRESS_EXAMPLE =
		"6331 PADRE DR\nSAN ANTONIO, TX  78224-2322";
	private final static String BODY_STYLE = "Body Style:";
	private final static String CANCEL_REGISTRATION =
		"Cancel Registration";
	private final static String COUNTY_NO = "County No:";
	private final static String DATE_FORMAT = "  /  /    ";
	private final static String DOC_NO_DEFAULT = "01511236061134915";
	private final static String DOCUMENT_NO = "Document No:";
	private final static String EXPIRES = "Expires:";
	private final static String ISSUED = "Issued:";
	private final static String MISCELLANEOUS_REPORTS =
		"Miscellaneous Remarks";
	private final static String MODEL = "Model:";
	private final static String NEW = "-NEW- -  ";
	private final static String OWNER_1 = "OWNER 1";
	private final static String OWNER_2 = "OWNER 2";
	private final static String OWNER_ADRESS = "Owner Address";
	private final static String OWNER_ID = "Owner Id:";
	private final static String OWNER_ID_DEFAULT = "376667494";
	private final static String PLATE_NO = "Plate No:";
	private final static String REGISTRATION_REFUND =
		"Registration Refund";
	private final static String REGULAR_TITLE = "REGULAR TITLE";
	// defect 10038 
	private final static String SELECT_CHOICE = "Select Choice:";
	// end defect 10038 
	private final static String STR_12 = "12";
	private final static String STR_15 = "15";
	private final static String STR_2001 = "2001";
	private final static String STR_4D = "4D";
	private final static String STR_DATE_EXMPL = "10/07/1990";
	private final static String STR_STOLEN_SRS = "Stolen/SRS";
	private final static String TITLE_SURRENDERED = "Title Surrendered";
	private final static String VEHICLE_JUNKED = "Vehicle Junked";
	private final static int VEH_MODL_MAX_LEN = 3;
	private final static int VEH_MAKE_MAX_LEN = 4;
	private final static String VIN = "VIN:";
	private final static int VIN_MAX_LEN = 22;
	private final static String YEAR_MAKE = "Year/Make:";
	private final static int YEAR_MAX_LEN = 4;

	// Used by VC 
	public final static int VEH_JUNKED = 1;
	public final static int TTL_SURR = 2;
	public final static int MISC_REM = 3;
	public final static int STOLEN_SRS = 4;
	public final static int REG_REFUND = 5;
	public final static int CANCEL_REG = 6;

	/**
	 * FrmStatusChangeRecordTTL006 constructor comment.
	 */
	public FrmStatusChangeRecordTTL006()
	{
		super();
		initialize();
	}

	/**
	 * FrmStatusChangeRecordTTL006 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmStatusChangeRecordTTL006(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmStatusChangeRecordTTL006 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmStatusChangeRecordTTL006(JFrame aaParent)
	{
		super(aaParent);
		initialize();
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
			FrmStatusChangeRecordTTL006 laFrmStatusChangeRecordTTL006;
			laFrmStatusChangeRecordTTL006 =
				new FrmStatusChangeRecordTTL006();
			laFrmStatusChangeRecordTTL006.setModal(true);
			laFrmStatusChangeRecordTTL006
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent laRTSDBox)
				{
					System.exit(0);
				};
			});
			laFrmStatusChangeRecordTTL006.show();
			Insets insets = laFrmStatusChangeRecordTTL006.getInsets();
			laFrmStatusChangeRecordTTL006.setSize(
				laFrmStatusChangeRecordTTL006.getWidth()
					+ insets.left
					+ insets.right,
				laFrmStatusChangeRecordTTL006.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmStatusChangeRecordTTL006.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * Invoked when an action occurs
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		//Code to avoid clicking on the button multiple times
		if (!startWorking())
		{
			return;
		}
		try
		{
			//Clear All Fields
			clearAllColor(this);
			if (aaAE.getSource() == getButtonPanel().getBtnEnter())
			{
				if (checkHardStops())
				{
					return;
				}

				// defect 10038
				convertVIN();

				if (!validateData())
				{
					return;
				}
				// end defect 10038

				// set the data before going to the next screen
				setDataToVehObj();
				getController().processData(
					AbstractViewController.ENTER,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (aaAE.getSource() == getButtonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.TTL006);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Determine if any hard stops exist and have been cleared.
	 * 
	 * @return boolean
	 */
	private boolean checkHardStops()
	{
		boolean lbRet = false;
		if (caVehInqData.getNoMFRecs() == 1)
		{
			if (IndicatorLookup.hasHardStop(cvIndicator))
			{
				lbRet = true;
				String lsAuth =
					caVehInqData.getVehMiscData().getAuthCd();
				if (lsAuth != null && lsAuth.length() > 0)
				{
					lbRet = false;
				}
				else
				{
					getController().processData(
						VCTitleRecordTTL003.VTR_AUTH,
						caVehInqData);
					lsAuth = caVehInqData.getVehMiscData().getAuthCd();
					if (lsAuth != null && lsAuth.length() > 0)
					{
						lbRet = false;
					}
				}
			}
		}
		return lbRet;
	}

	/**
	 * convertVIN
	 */
	public void convertVIN()
	{
		if (gettxtVIN().isEnabled())
		{
			String lsVin = gettxtVIN().getText().trim().toUpperCase();
			lsVin = CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
			gettxtVIN().setText(lsVin);
		}
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE FocuseEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		// Not used
	}

	/**
	 * Invoked when Odometer Reading, Empty Weight, Carrying Capacity or 
	 * VIN field loses the keyboard focus.
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		// Convert I/O => 1/0 
		if (!aaFE.isTemporary() && aaFE.getSource() == gettxtVIN())
		{
			convertVIN();
		}
		// end defect 8523
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel()
	{
		if (ivjButtonPanel == null)
		{
			try
			{
				ivjButtonPanel = new ButtonPanel();
				ivjButtonPanel.setName("ivjButtonPanel");
				ivjButtonPanel.setBounds(254, 333, 242, 39);
				ivjButtonPanel.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel.addActionListener(this);
				ivjButtonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjButtonPanel;
	}

	/**
	 * Return the FrmStatusChangeRecordTTL006ContentPane1 property
	 * value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmStatusChangeRecordTTL006ContentPane1()
	{
		if (ivjFrmStatusChangeRecordTTL006ContentPane1 == null)
		{
			try
			{
				ivjFrmStatusChangeRecordTTL006ContentPane1 =
					new JPanel();
				ivjFrmStatusChangeRecordTTL006ContentPane1.setName(
					"ivjFrmStatusChangeRecordTTL006ContentPane1");
				ivjFrmStatusChangeRecordTTL006ContentPane1.setLayout(
					null);
				ivjFrmStatusChangeRecordTTL006ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmStatusChangeRecordTTL006ContentPane1
					.setMinimumSize(
					new Dimension(612, 446));
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblOwnerId(),
					getstcLblOwnerId().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblOwnerAddress(),
					getstcLblOwnerAddress().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblPlateNo(),
					getstcLblPlateNo().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblRegExpires(),
					getstcLblRegExpires().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblCountyNo(),
					getstcLblCountyNo().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblModel(),
					getstcLblModel().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblYearMake(),
					getstcLblYearMake().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblBodyStyle(),
					getstcLblBodyStyle().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblDocTypeCodeDesc(),
					getlblDocTypeCodeDesc().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcVIN(),
					getstcVIN().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblDocumentNo(),
					getstcLblDocumentNo().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblOwnerId(),
					getlblOwnerId().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblIssued(),
					getstcLblIssued().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblRegPlateNo(),
					getlblRegPlateNo().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblRegExpMo(),
					getlblRegExpMo().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblResComptCntyNo(),
					getlblResComptCntyNo().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblVehicleBodyType(),
					getlblVehicleBodyType().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblDocNo(),
					getlblDocNo().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblTitleIssueDate(),
					getlblTitleIssueDate().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getstcLblSlash(),
					getstcLblSlash().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					gettxtVehicleModelYear(),
					gettxtVehicleModelYear().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					gettxtVehicleMake(),
					gettxtVehicleMake().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					gettxtVehicleModel(),
					gettxtVehicleModel().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					gettxtVIN(),
					gettxtVIN().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblOwnerTitleName1(),
					getlblOwnerTitleName1().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getpnlGrpSelectChoice(),
					getpnlGrpSelectChoice().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getButtonPanel(),
					getButtonPanel().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblOwnerTitleName2(),
					getlblOwnerTitleName2().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					gettxtAOwnrAddr(),
					gettxtAOwnrAddr().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getlblRegExpYr(),
					getlblRegExpYr().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				// defect 10038 
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getJComboBoxBodyStyle(),
					getJComboBoxBodyStyle().getName());
				getFrmStatusChangeRecordTTL006ContentPane1().add(
					getJComboBoxMake(),
					getJComboBoxMake().getName());
				// end defect 10038 
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
		return ivjFrmStatusChangeRecordTTL006ContentPane1;
	}

	/**
	 * Get indicators and build string to display indicators in text 
	 * area.
	 */
	private void getIndicators()
	{
		cvIndicator =
			IndicatorLookup.getIndicators(
				caMFVehicleData,
				getController().getTransCode(),
				IndicatorLookup.SCREEN);
		StringBuffer lsIndis =
			new StringBuffer(CommonConstant.STR_SPACE_EMPTY);

		int liNumIndis = cvIndicator.size();
		if (liNumIndis > 0)
		{
			Vector lvRows = new Vector();
			for (int liIndex = 0; liIndex < liNumIndis; liIndex++)
			{
				IndicatorData laData =
					(IndicatorData) cvIndicator.get(liIndex);
				lsIndis.append(
					laData.getStopCode() == null
						? CommonConstant.STR_SPACE_ONE
						: laData.getStopCode());
				lsIndis.append(CommonConstant.STR_SPACE_TWO);
				lsIndis.append(laData.getDesc());
				lvRows.add(lsIndis.toString());
				lsIndis =
					new StringBuffer(CommonConstant.STR_SPACE_EMPTY);
			}
			getlstIndiDescription().setListData(lvRows);
			getlstIndiDescription().setSelectedIndex(0);

			// Set focus to indicator box if more than n entries
			// defect 9971  
			if (liNumIndis > CommonConstant.MAX_INDI_NO_SCROLL)
			{
				getlstIndiDescription().requestFocus();
			}
			else
			{
				getButtonPanel().getBtnEnter().requestFocus();
			}
			// end defect 9971 
		}
	}

	/**
	 * Return the JComboBoxBodyStyle property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getJComboBoxBodyStyle()
	{
		if (ivjJComboBoxBodyStyle == null)
		{
			try
			{
				ivjJComboBoxBodyStyle = new JComboBox();
				ivjJComboBoxBodyStyle.setName("ivjJComboBoxBodyStyle");
				ivjJComboBoxBodyStyle.setBounds(323, 98, 146, 20);
				ivjJComboBoxBodyStyle.setBackground(Color.white);
				populateBodyTypes();
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
		return ivjJComboBoxBodyStyle;
	}

	/**
	 * Return the JComboBoxMake property value.
	 * 
	 * @return JComboBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JComboBox getJComboBoxMake()
	{
		if (ivjJComboBoxMake == null)
		{
			try
			{
				ivjJComboBoxMake = new JComboBox();
				ivjJComboBoxMake.setName("ivjJComboBoxMake");
				ivjJComboBoxMake.setBounds(369, 75, 184, 20);
				ivjJComboBoxMake.setBackground(Color.white);
				// user code begin {1}
				// defect 7898
				// reverse order to populate before addItemListener() 
				populateVehicleMakes();
				ivjJComboBoxMake.addItemListener(this);
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
		return ivjJComboBoxMake;
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
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setLayout(new GridBagLayout());
				ivjJPanel1.setBounds(7, 201, 218, 179);
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
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("ivjJScrollPane1");
				ivjJScrollPane1.setBounds(233, 242, 288, 79);
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				// defect 9971
				// Use CommonConstant for Font;  Enlarge width of 
				// Panel 
				ivjJScrollPane1.setFont(
					new Font(CommonConstant.FONT_JLIST, 0, 12));
				// end defect 9971 
				getJScrollPane1().setViewportView(
					getlstIndiDescription());
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
	 * Return the lblDocNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblDocNo()
	{
		if (ivjlblDocNo == null)
		{
			try
			{
				ivjlblDocNo = new JLabel();
				ivjlblDocNo.setName("ivjlblDocNo");
				ivjlblDocNo.setBounds(323, 167, 149, 20);
				ivjlblDocNo.setText(DOC_NO_DEFAULT);
				ivjlblDocNo.setMinimumSize(new Dimension(119, 14));
				ivjlblDocNo.setMaximumSize(new Dimension(119, 14));
				ivjlblDocNo.setHorizontalAlignment(
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
		return ivjlblDocNo;
	}

	/**
	 * Return the lblDocTypeCodeDesc property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblDocTypeCodeDesc()
	{
		if (ivjlblDocTypeCodeDesc == null)
		{
			try
			{
				ivjlblDocTypeCodeDesc = new JLabel();
				ivjlblDocTypeCodeDesc.setName("ivjlblDocTypeCodeDesc");
				ivjlblDocTypeCodeDesc.setLocation(265, 193);
				ivjlblDocTypeCodeDesc.setSize(224, 20);
				ivjlblDocTypeCodeDesc.setText(REGULAR_TITLE);
				ivjlblDocTypeCodeDesc.setMinimumSize(
					new Dimension(88, 14));
				ivjlblDocTypeCodeDesc.setMaximumSize(
					new Dimension(88, 14));
				ivjlblDocTypeCodeDesc.setHorizontalAlignment(
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
		return ivjlblDocTypeCodeDesc;
	}

	/**
	 * Return the lblOwnerId property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblOwnerId()
	{
		if (ivjlblOwnerId == null)
		{
			try
			{
				ivjlblOwnerId = new JLabel();
				ivjlblOwnerId.setName("ivjlblOwnerId");
				ivjlblOwnerId.setBounds(93, 203, 80, 15);
				ivjlblOwnerId.setText(OWNER_ID_DEFAULT);
				ivjlblOwnerId.setMinimumSize(new Dimension(63, 14));
				ivjlblOwnerId.setMaximumSize(new Dimension(63, 14));
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
		return ivjlblOwnerId;
	}

	/**
	 * Return the lblOwnerTitleName1 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblOwnerTitleName1()
	{
		if (ivjlblOwnerTitleName1 == null)
		{
			try
			{
				ivjlblOwnerTitleName1 = new JLabel();
				ivjlblOwnerTitleName1.setName("ivjlblOwnerTitleName1");
				ivjlblOwnerTitleName1.setBounds(12, 221, 201, 15);
				ivjlblOwnerTitleName1.setText(OWNER_1);
				ivjlblOwnerTitleName1.setOpaque(true);
				ivjlblOwnerTitleName1.setForeground(Color.white);
				ivjlblOwnerTitleName1.setBackground(Color.red);
				ivjlblOwnerTitleName1.setMinimumSize(
					new Dimension(87, 14));
				ivjlblOwnerTitleName1.setMaximumSize(
					new Dimension(87, 14));
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
		return ivjlblOwnerTitleName1;
	}

	/**
	 * Return the lblOwnerTitleName2 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblOwnerTitleName2()
	{
		if (ivjlblOwnerTitleName2 == null)
		{
			try
			{
				ivjlblOwnerTitleName2 = new JLabel();
				ivjlblOwnerTitleName2.setName("ivjlblOwnerTitleName2");
				ivjlblOwnerTitleName2.setBounds(12, 241, 201, 15);
				ivjlblOwnerTitleName2.setText(OWNER_2);
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
		return ivjlblOwnerTitleName2;
	}

	/**
	 * Return the lblRegExpMo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblRegExpMo()
	{
		if (ivjlblRegExpMo == null)
		{
			try
			{
				ivjlblRegExpMo = new JLabel();
				ivjlblRegExpMo.setName("ivjlblRegExpMo");
				ivjlblRegExpMo.setLocation(323, 28);
				ivjlblRegExpMo.setSize(20, 20);
				ivjlblRegExpMo.setText(STR_12);
				ivjlblRegExpMo.setMaximumSize(new Dimension(7, 14));
				ivjlblRegExpMo.setMinimumSize(new Dimension(7, 14));
				ivjlblRegExpMo.setHorizontalAlignment(
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
		return ivjlblRegExpMo;
	}

	/**
	 * Return the lblRegExpYr property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblRegExpYr()
	{
		if (ivjlblRegExpYr == null)
		{
			try
			{
				ivjlblRegExpYr = new JLabel();
				ivjlblRegExpYr.setName("ivjlblRegExpYr");
				ivjlblRegExpYr.setBounds(357, 28, 37, 20);
				ivjlblRegExpYr.setText(STR_2001);
				ivjlblRegExpYr.setMinimumSize(new Dimension(7, 14));
				ivjlblRegExpYr.setMaximumSize(new Dimension(7, 14));
				ivjlblRegExpYr.setHorizontalAlignment(
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
		return ivjlblRegExpYr;
	}

	/**
	 * Return the lblRegPlateNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblRegPlateNo()
	{
		if (ivjlblRegPlateNo == null)
		{
			try
			{
				ivjlblRegPlateNo = new JLabel();
				ivjlblRegPlateNo.setName("ivjlblRegPlateNo");
				ivjlblRegPlateNo.setBounds(323, 6, 107, 18);
				ivjlblRegPlateNo.setText(REGULAR_TITLE);
				ivjlblRegPlateNo.setMinimumSize(new Dimension(88, 14));
				ivjlblRegPlateNo.setMaximumSize(new Dimension(88, 14));
				ivjlblRegPlateNo.setHorizontalAlignment(
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
		return ivjlblRegPlateNo;
	}

	/**
	 * Return the lblResComptCntyNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblResComptCntyNo()
	{
		if (ivjlblResComptCntyNo == null)
		{
			try
			{
				ivjlblResComptCntyNo = new JLabel();
				ivjlblResComptCntyNo.setName("ivjlblResComptCntyNo");
				ivjlblResComptCntyNo.setBounds(323, 52, 32, 20);
				ivjlblResComptCntyNo.setText(STR_15);
				ivjlblResComptCntyNo.setMinimumSize(
					new Dimension(14, 14));
				ivjlblResComptCntyNo.setMaximumSize(
					new Dimension(14, 14));
				ivjlblResComptCntyNo.setHorizontalAlignment(
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
		return ivjlblResComptCntyNo;
	}

	/**
	 * Return the lblTitleIssueDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblTitleIssueDate()
	{
		if (ivjlblTitleIssueDate == null)
		{
			try
			{
				ivjlblTitleIssueDate = new JLabel();
				ivjlblTitleIssueDate.setName("ivjlblTitleIssueDate");
				ivjlblTitleIssueDate.setLocation(323, 218);
				ivjlblTitleIssueDate.setSize(94, 20);
				ivjlblTitleIssueDate.setText(STR_DATE_EXMPL);
				ivjlblTitleIssueDate.setMinimumSize(
					new Dimension(62, 14));
				ivjlblTitleIssueDate.setMaximumSize(
					new Dimension(62, 14));
				ivjlblTitleIssueDate.setHorizontalAlignment(
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
		return ivjlblTitleIssueDate;
	}

	/**
	 * Return the lblVehicleBodyType property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblVehicleBodyType()
	{
		if (ivjlblVehicleBodyType == null)
		{
			try
			{
				ivjlblVehicleBodyType = new JLabel();
				ivjlblVehicleBodyType.setName("ivjlblVehicleBodyType");
				ivjlblVehicleBodyType.setBounds(323, 98, 33, 20);
				ivjlblVehicleBodyType.setText(STR_4D);
				ivjlblVehicleBodyType.setMinimumSize(
					new Dimension(15, 14));
				ivjlblVehicleBodyType.setMaximumSize(
					new Dimension(15, 14));
				ivjlblVehicleBodyType.setHorizontalAlignment(
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
		return ivjlblVehicleBodyType;
	}

	/**
	 * Return the lstIndiDescription property value.
	 * 
	 * @return JList
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JList getlstIndiDescription()
	{
		if (ivjlstIndiDescription == null)
		{
			try
			{
				ivjlstIndiDescription = new JList();
				ivjlstIndiDescription.setName("ivjlstIndiDescription");
				ivjlstIndiDescription.setBounds(0, 0, 282, 52);
				// defect 9971 
				ivjlstIndiDescription.setFont(
					new java.awt.Font(
						CommonConstant.FONT_JLIST,
						0,
						12));
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
		return ivjlstIndiDescription;
	}

	/**
	 * Return the pnlGrpSelectChoice property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlGrpSelectChoice()
	{
		if (ivjpnlGrpSelectChoice == null)
		{
			try
			{
				ivjpnlGrpSelectChoice = new JPanel();
				GridBagConstraints consGridBagConstraints7 =
					new GridBagConstraints();
				GridBagConstraints consGridBagConstraints8 =
					new GridBagConstraints();
				GridBagConstraints consGridBagConstraints9 =
					new GridBagConstraints();
				GridBagConstraints consGridBagConstraints11 =
					new GridBagConstraints();
				GridBagConstraints consGridBagConstraints10 =
					new GridBagConstraints();
				GridBagConstraints consGridBagConstraints12 =
					new GridBagConstraints();
				consGridBagConstraints11.insets =
					new Insets(3, 24, 3, 44);
				consGridBagConstraints11.ipady = -2;
				consGridBagConstraints11.ipadx = 13;
				consGridBagConstraints11.gridy = 4;
				consGridBagConstraints11.gridx = 0;
				consGridBagConstraints9.insets =
					new Insets(3, 24, 3, 24);
				consGridBagConstraints9.ipady = -2;
				consGridBagConstraints9.ipadx = 8;
				consGridBagConstraints9.gridy = 2;
				consGridBagConstraints9.gridx = 0;
				consGridBagConstraints7.insets =
					new Insets(13, 24, 3, 72);
				consGridBagConstraints7.ipady = -2;
				consGridBagConstraints7.ipadx = 10;
				consGridBagConstraints7.gridy = 0;
				consGridBagConstraints7.gridx = 0;
				consGridBagConstraints10.insets =
					new Insets(3, 24, 3, 86);
				consGridBagConstraints10.ipady = -2;
				consGridBagConstraints10.ipadx = 20;
				consGridBagConstraints10.gridy = 3;
				consGridBagConstraints10.gridx = 0;
				consGridBagConstraints12.insets =
					new Insets(3, 24, 16, 48);
				consGridBagConstraints12.ipady = -2;
				consGridBagConstraints12.ipadx = 10;
				consGridBagConstraints12.gridy = 5;
				consGridBagConstraints12.gridx = 0;
				consGridBagConstraints8.insets =
					new Insets(3, 24, 3, 64);
				consGridBagConstraints8.ipady = -2;
				consGridBagConstraints8.ipadx = 6;
				consGridBagConstraints8.gridy = 1;
				consGridBagConstraints8.gridx = 0;
				ivjpnlGrpSelectChoice.setName("ivjpnlGrpSelectChoice");
				ivjpnlGrpSelectChoice.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						SELECT_CHOICE));
				ivjpnlGrpSelectChoice.setLayout(new GridBagLayout());
				ivjpnlGrpSelectChoice.add(
					getradioVehicleJunked(),
					consGridBagConstraints7);
				ivjpnlGrpSelectChoice.add(
					getradioTitleSurrendered(),
					consGridBagConstraints8);
				ivjpnlGrpSelectChoice.add(
					getradioMiscellaneousRemarks(),
					consGridBagConstraints9);
				ivjpnlGrpSelectChoice.add(
					getradioStolenSRS(),
					consGridBagConstraints10);
				ivjpnlGrpSelectChoice.add(
					getradioRegistrationRefund(),
					consGridBagConstraints11);
				ivjpnlGrpSelectChoice.add(
					getradioCancelRegistration(),
					consGridBagConstraints12);
				ivjpnlGrpSelectChoice.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjpnlGrpSelectChoice.setBounds(7, 9, 218, 191);
				ivjpnlGrpSelectChoice.setMinimumSize(
					new Dimension(225, 199));
				// user code begin {1}
				// defect 7898
				// Changed ButtonGroup to RTSButtonGroup
				RTSButtonGroup laBG = new RTSButtonGroup();
				laBG.add(getradioVehicleJunked());
				laBG.add(getradioTitleSurrendered());
				laBG.add(getradioMiscellaneousRemarks());
				laBG.add(getradioStolenSRS());
				laBG.add(getradioRegistrationRefund());
				laBG.add(getradioCancelRegistration());
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
		return ivjpnlGrpSelectChoice;
	}

	/**
	 * Return the radioCancelRegistration property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioCancelRegistration()
	{
		if (ivjradioCancelRegistration == null)
		{
			try
			{
				ivjradioCancelRegistration = new JRadioButton();
				ivjradioCancelRegistration.setName(
					"ivjradioCancelRegistration");
				ivjradioCancelRegistration.setText(CANCEL_REGISTRATION);
				ivjradioCancelRegistration.setMinimumSize(
					new Dimension(136, 22));
				ivjradioCancelRegistration.setMaximumSize(
					new Dimension(136, 22));
				ivjradioCancelRegistration.setActionCommand(
					CANCEL_REGISTRATION);
				// user code begin {1}
				ivjradioCancelRegistration.setMnemonic(KeyEvent.VK_C);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCancelRegistration;
	}

	/**
	 * Return the radioMiscellaneousRemarks property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioMiscellaneousRemarks()
	{
		if (ivjradioMiscellaneousRemarks == null)
		{
			try
			{
				ivjradioMiscellaneousRemarks = new JRadioButton();
				ivjradioMiscellaneousRemarks.setName(
					"ivjradioMiscellaneousRemarks");
				ivjradioMiscellaneousRemarks.setText(
					MISCELLANEOUS_REPORTS);
				ivjradioMiscellaneousRemarks.setMinimumSize(
					new Dimension(162, 22));
				ivjradioMiscellaneousRemarks.setMaximumSize(
					new Dimension(162, 22));
				ivjradioMiscellaneousRemarks.setActionCommand(
					MISCELLANEOUS_REPORTS);
				// user code begin {1}
				ivjradioMiscellaneousRemarks.setMnemonic(KeyEvent.VK_M);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioMiscellaneousRemarks;
	}

	/**
	 * Return the radioRegistrationRefund property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioRegistrationRefund()
	{
		if (ivjradioRegistrationRefund == null)
		{
			try
			{
				ivjradioRegistrationRefund = new JRadioButton();
				ivjradioRegistrationRefund.setName(
					"ivjradioRegistrationRefund");
				ivjradioRegistrationRefund.setText(REGISTRATION_REFUND);
				ivjradioRegistrationRefund.setMinimumSize(
					new Dimension(137, 22));
				ivjradioRegistrationRefund.setMaximumSize(
					new Dimension(137, 22));
				ivjradioRegistrationRefund.setActionCommand(
					REGISTRATION_REFUND);
				// user code begin {1}
				ivjradioRegistrationRefund.setMnemonic(KeyEvent.VK_R);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioRegistrationRefund;
	}
	/**
	 * Return the radioStolenSRS property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioStolenSRS()
	{
		if (ivjradioStolenSRS == null)
		{
			try
			{
				ivjradioStolenSRS = new JRadioButton();
				ivjradioStolenSRS.setName("ivjradioStolenSRS");
				ivjradioStolenSRS.setText(STR_STOLEN_SRS);
				ivjradioStolenSRS.setMinimumSize(new Dimension(88, 22));
				ivjradioStolenSRS.setMaximumSize(new Dimension(88, 22));
				ivjradioStolenSRS.setActionCommand(STR_STOLEN_SRS);
				// user code begin {1}
				ivjradioStolenSRS.setMnemonic(KeyEvent.VK_S);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioStolenSRS;
	}
	/**
	 * Return the radioTitleSurrendered property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioTitleSurrendered()
	{
		if (ivjradioTitleSurrendered == null)
		{
			try
			{
				ivjradioTitleSurrendered = new JRadioButton();
				ivjradioTitleSurrendered.setName(
					"ivjradioTitleSurrendered");
				ivjradioTitleSurrendered.setText(TITLE_SURRENDERED);
				ivjradioTitleSurrendered.setMinimumSize(
					new Dimension(124, 22));
				ivjradioTitleSurrendered.setMaximumSize(
					new Dimension(124, 22));
				ivjradioTitleSurrendered.setActionCommand(
					TITLE_SURRENDERED);
				// user code begin {1}
				ivjradioTitleSurrendered.setMnemonic(KeyEvent.VK_T);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioTitleSurrendered;
	}
	/**
	 * Return the radioVehicleJunked property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioVehicleJunked()
	{
		if (ivjradioVehicleJunked == null)
		{
			try
			{
				ivjradioVehicleJunked = new JRadioButton();
				ivjradioVehicleJunked.setName("ivjradioVehicleJunked");
				ivjradioVehicleJunked.setText(VEHICLE_JUNKED);
				ivjradioVehicleJunked.setMinimumSize(
					new Dimension(112, 22));
				ivjradioVehicleJunked.setMaximumSize(
					new Dimension(112, 22));
				ivjradioVehicleJunked.setActionCommand(VEHICLE_JUNKED);
				// user code begin {1}
				ivjradioVehicleJunked.setMnemonic(KeyEvent.VK_V);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioVehicleJunked;
	}
	/**
	 * getSelectedRadioButton
	 * 
	 * @return int
	 */
	public int getSelectedRadioButton()
	{
		if (getradioVehicleJunked().isSelected())
		{
			return VEH_JUNKED;
		}
		else if (getradioTitleSurrendered().isSelected())
		{
			return TTL_SURR;
		}
		else if (getradioMiscellaneousRemarks().isSelected())
		{
			return MISC_REM;
		}
		else if (getradioStolenSRS().isSelected())
		{
			return STOLEN_SRS;
		}
		else if (getradioRegistrationRefund().isSelected())
		{
			return REG_REFUND;
		}
		else if (getradioCancelRegistration().isSelected())
		{
			return CANCEL_REG;
		}
		return Integer.MIN_VALUE;
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
				ivjstcLblBodyStyle.setName("ivjstcLblBodyStyle");
				ivjstcLblBodyStyle.setLocation(231, 98);
				ivjstcLblBodyStyle.setSize(78, 20);
				ivjstcLblBodyStyle.setText(BODY_STYLE);
				ivjstcLblBodyStyle.setMinimumSize(
					new Dimension(62, 14));
				ivjstcLblBodyStyle.setMaximumSize(
					new Dimension(62, 14));
				ivjstcLblBodyStyle.setHorizontalAlignment(
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
		return ivjstcLblBodyStyle;
	}
	/**
	 * Return the stcLblCountyNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblCountyNo()
	{
		if (ivjstcLblCountyNo == null)
		{
			try
			{
				ivjstcLblCountyNo = new JLabel();
				ivjstcLblCountyNo.setName("ivjstcLblCountyNo");
				ivjstcLblCountyNo.setBounds(231, 52, 78, 20);
				ivjstcLblCountyNo.setText(COUNTY_NO);
				ivjstcLblCountyNo.setMinimumSize(new Dimension(60, 14));
				ivjstcLblCountyNo.setMaximumSize(new Dimension(60, 14));
				ivjstcLblCountyNo.setHorizontalAlignment(
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
		return ivjstcLblCountyNo;
	}
	/**
	 * Return the stcLblDocumentNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDocumentNo()
	{
		if (ivjstcLblDocumentNo == null)
		{
			try
			{
				ivjstcLblDocumentNo = new JLabel();
				ivjstcLblDocumentNo.setName("ivjstcLblDocumentNo");
				ivjstcLblDocumentNo.setLocation(227, 167);
				ivjstcLblDocumentNo.setSize(82, 20);
				ivjstcLblDocumentNo.setText(DOCUMENT_NO);
				ivjstcLblDocumentNo.setMinimumSize(
					new Dimension(79, 14));
				ivjstcLblDocumentNo.setMaximumSize(
					new Dimension(79, 14));
				ivjstcLblDocumentNo.setHorizontalAlignment(
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
		return ivjstcLblDocumentNo;
	}
	/**
	 * Return the stcLblIssued property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblIssued()
	{
		if (ivjstcLblIssued == null)
		{
			try
			{
				ivjstcLblIssued = new JLabel();
				ivjstcLblIssued.setSize(78, 20);
				ivjstcLblIssued.setLocation(231, 218);
				ivjstcLblIssued.setName("ivjstcLblIssued");
				ivjstcLblIssued.setText(ISSUED);
				ivjstcLblIssued.setMinimumSize(new Dimension(41, 14));
				ivjstcLblIssued.setMaximumSize(new Dimension(41, 14));
				ivjstcLblIssued.setHorizontalAlignment(
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
		return ivjstcLblIssued;
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
				ivjstcLblModel.setName("ivjstcLblModel");
				ivjstcLblModel.setBounds(231, 121, 78, 20);
				ivjstcLblModel.setText(MODEL);
				ivjstcLblModel.setMinimumSize(new Dimension(37, 14));
				ivjstcLblModel.setMaximumSize(new Dimension(37, 14));
				ivjstcLblModel.setHorizontalAlignment(
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
		return ivjstcLblModel;
	}
	/**
	 * Return the stcLblOwnerAddress property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblOwnerAddress()
	{
		if (ivjstcLblOwnerAddress == null)
		{
			try
			{
				ivjstcLblOwnerAddress = new JLabel();
				ivjstcLblOwnerAddress.setName("ivjstcLblOwnerAddress");
				ivjstcLblOwnerAddress.setBounds(11, 266, 141, 15);
				ivjstcLblOwnerAddress.setText(OWNER_ADRESS);
				ivjstcLblOwnerAddress.setMinimumSize(
					new Dimension(89, 14));
				ivjstcLblOwnerAddress.setMaximumSize(
					new Dimension(89, 14));
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
		return ivjstcLblOwnerAddress;
	}
	/**
	 * Return the stcLblOwnerId property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblOwnerId()
	{
		if (ivjstcLblOwnerId == null)
		{
			try
			{
				ivjstcLblOwnerId = new JLabel();
				ivjstcLblOwnerId.setName("ivjstcLblOwnerId");
				ivjstcLblOwnerId.setBounds(9, 203, 60, 15);
				ivjstcLblOwnerId.setText(OWNER_ID);
				ivjstcLblOwnerId.setMinimumSize(new Dimension(54, 14));
				ivjstcLblOwnerId.setMaximumSize(new Dimension(54, 14));
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
		return ivjstcLblOwnerId;
	}
	/**
	 * Return the stcLblPlateNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblPlateNo()
	{
		if (ivjstcLblPlateNo == null)
		{
			try
			{
				ivjstcLblPlateNo = new JLabel();
				ivjstcLblPlateNo.setName("ivjstcLblPlateNo");
				ivjstcLblPlateNo.setBounds(250, 4, 59, 22);
				ivjstcLblPlateNo.setText(PLATE_NO);
				ivjstcLblPlateNo.setMinimumSize(new Dimension(50, 14));
				ivjstcLblPlateNo.setMaximumSize(new Dimension(50, 14));
				ivjstcLblPlateNo.setHorizontalAlignment(
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
		return ivjstcLblPlateNo;
	}
	/**
	 * Return the stcLblRegExpires property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblRegExpires()
	{
		if (ivjstcLblRegExpires == null)
		{
			try
			{
				ivjstcLblRegExpires = new JLabel();
				ivjstcLblRegExpires.setName("ivjstcLblRegExpires");
				ivjstcLblRegExpires.setLocation(231, 29);
				ivjstcLblRegExpires.setSize(78, 20);
				ivjstcLblRegExpires.setText(EXPIRES);
				ivjstcLblRegExpires.setMinimumSize(
					new Dimension(46, 14));
				ivjstcLblRegExpires.setMaximumSize(
					new Dimension(46, 14));
				ivjstcLblRegExpires.setHorizontalAlignment(
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
		return ivjstcLblRegExpires;
	}
	/**
	 * Return the stcLblSlash property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblSlash()
	{
		if (ivjstcLblSlash == null)
		{
			try
			{
				ivjstcLblSlash = new JLabel();
				ivjstcLblSlash.setName("ivjstcLblSlash");
				ivjstcLblSlash.setLocation(345, 28);
				ivjstcLblSlash.setSize(12, 20);
				ivjstcLblSlash.setText(CommonConstant.STR_SLASH);
				ivjstcLblSlash.setMaximumSize(new Dimension(3, 14));
				ivjstcLblSlash.setMinimumSize(new Dimension(3, 14));
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
				ivjstcLblYearMake.setName("stcLblYearMake");
				ivjstcLblYearMake.setLocation(231, 75);
				ivjstcLblYearMake.setSize(78, 20);
				ivjstcLblYearMake.setText(YEAR_MAKE);
				ivjstcLblYearMake.setMinimumSize(new Dimension(63, 14));
				ivjstcLblYearMake.setMaximumSize(new Dimension(63, 14));
				ivjstcLblYearMake.setHorizontalAlignment(
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
		return ivjstcLblYearMake;
	}
	/**
	 * Return the stcVIN property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcVIN()
	{
		if (ivjstcVIN == null)
		{
			try
			{
				ivjstcVIN = new JLabel();
				ivjstcVIN.setName("ivjstcVIN");
				ivjstcVIN.setLocation(231, 144);
				ivjstcVIN.setSize(78, 20);
				ivjstcVIN.setText(VIN);
				ivjstcVIN.setMinimumSize(new Dimension(22, 14));
				ivjstcVIN.setMaximumSize(new Dimension(22, 14));
				ivjstcVIN.setHorizontalAlignment(
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
		return ivjstcVIN;
	}
	/**
	 * Return the txtAOwnrAddr property value.
	 * 
	 * @return RTSTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTextArea gettxtAOwnrAddr()
	{
		if (ivjtxtAOwnrAddr == null)
		{
			try
			{
				ivjtxtAOwnrAddr = new RTSTextArea();
				ivjtxtAOwnrAddr.setName("ivjtxtAOwnrAddr");
				ivjtxtAOwnrAddr.setBounds(12, 289, 194, 80);
				ivjtxtAOwnrAddr.setText(ADDRESS_EXAMPLE);
				ivjtxtAOwnrAddr.setBackground(new Color(204, 204, 204));
				ivjtxtAOwnrAddr.setFont(new Font("Arial", 1, 12));
				ivjtxtAOwnrAddr.setEditable(false);
				ivjtxtAOwnrAddr.setSelectedTextColor(Color.black);
				ivjtxtAOwnrAddr.setRequestFocusEnabled(false);
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
		return ivjtxtAOwnrAddr;
	}
	/**
	 * Return the txtVehicleMake property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleMake()
	{
		if (ivjtxtVehicleMake == null)
		{
			try
			{
				ivjtxtVehicleMake = new RTSInputField();
				ivjtxtVehicleMake.setName("ivjtxtVehicleMake");
				ivjtxtVehicleMake.setBounds(368, 75, 80, 20);
				ivjtxtVehicleMake.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtVehicleMake.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtVehicleMake.setEnabled(false);
				ivjtxtVehicleMake.setMaxLength(VEH_MAKE_MAX_LEN);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleMake;
	}
	/**
	 * Return the txtVehicleModel property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleModel()
	{
		if (ivjtxtVehicleModel == null)
		{
			try
			{
				ivjtxtVehicleModel = new RTSInputField();
				ivjtxtVehicleModel.setName("ivjtxtVehicleModel");
				ivjtxtVehicleModel.setBounds(323, 121, 40, 20);
				ivjtxtVehicleModel.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleModel.setEnabled(false);
				// user code begin {1}
				ivjtxtVehicleModel.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtVehicleModel.setMaxLength(VEH_MODL_MAX_LEN);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleModel;
	}

	/**
	 * Return the txtVehicleModelYear property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVehicleModelYear()
	{
		if (ivjtxtVehicleModelYear == null)
		{
			try
			{
				ivjtxtVehicleModelYear = new RTSInputField();
				ivjtxtVehicleModelYear.setName(
					"ivjtxtVehicleModelYear");
				ivjtxtVehicleModelYear.setBounds(323, 75, 39, 20);
				ivjtxtVehicleModelYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehicleModelYear.setText(
					CommonConstant.STR_SPACE_EMPTY);
				ivjtxtVehicleModelYear.setEnabled(false);
				// user code begin {1}
				ivjtxtVehicleModelYear.setInput(
					RTSInputField.NUMERIC_ONLY);
				ivjtxtVehicleModelYear.setMaxLength(YEAR_MAX_LEN);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtVehicleModelYear;
	}

	/**
	 * Return the txtVIN property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtVIN()
	{
		if (ivjtxtVIN == null)
		{
			try
			{
				ivjtxtVIN = new RTSInputField();
				ivjtxtVIN.setName("ivjtxtVIN");
				ivjtxtVIN.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVIN.setEnabled(false);
				ivjtxtVIN.setBounds(323, 144, 178, 20);
				// user code begin {1}
				ivjtxtVIN.setInput(RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtVIN.setMaxLength(VIN_MAX_LEN);
				ivjtxtVIN.addFocusListener(this);
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
			// user code end
			setName(ScreenConstant.TTL006_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(577, 407);
			setTitle(ScreenConstant.TTL006_FRAME_TITLE);
			setContentPane(
				getFrmStatusChangeRecordTTL006ContentPane1());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * This method determines whether the Registration Plate Code in the
	 * Vehicle record is outside RTS scope (issued by VTR Headquarters)
	 * 
	 * @return boolean
	 */
	private boolean isPlateOutsideScope(int aiOfcCd)
	{
		String lsPlateCd =
			caVehInqData.getMfVehicleData().getRegData().getRegPltCd();

		return PlateTypeCache.isOutOfScopePlate(
			lsPlateCd,
			aiOfcCd,
			SpecialPlatesConstant.REGIS_TYPE_EVENTS);

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
		if (aaIE.getSource() == getJComboBoxMake())
		{
			String lsSelected =
				(String) getJComboBoxMake().getSelectedItem();
			if (lsSelected.equals(NEW))
			{
				getJComboBoxMake().setVisible(false);
				gettxtVehicleMake().setVisible(true);
				gettxtVehicleMake().setEnabled(true);
				gettxtVehicleMake().requestFocus();
			}
		}
	}

	/**
	 * populateBodyTypes
	 */
	private void populateBodyTypes()
	{
		if (getJComboBoxBodyStyle().isEnabled())
		{
			Vector lvVehBdyType =
				VehicleBodyTypesCache.getVehBdyTypesVec();
			UtilityMethods.sort(lvVehBdyType);

			Vector vcComboValues = new Vector();
			if (lvVehBdyType != null)
			{
				for (int liIndex = 0;
					liIndex < lvVehBdyType.size();
					liIndex++)
				{
					VehicleBodyTypesData laVehDataType =
						(VehicleBodyTypesData) lvVehBdyType.get(
							liIndex);
					String lsDesc =
						laVehDataType.getVehBdyType()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ laVehDataType.getVehBdyTypeDesc();
					vcComboValues.add(lsDesc);
				}
			}
			DefaultComboBoxModel laMDL =
				new DefaultComboBoxModel(vcComboValues);
			getJComboBoxBodyStyle().setModel(laMDL);

			getJComboBoxBodyStyle().setSelectedIndex(-1);
			// defect 8479
			comboBoxHotKeyFix(getJComboBoxBodyStyle());
			// end defect 8479
		}
	}

	/**
	 * Populates the vehicle makes
	 */
	private void populateVehicleMakes()
	{
		if (getJComboBoxMake().isEnabled())
		{
			Vector lvVehMk = VehicleMakesCache.getVehMks();
			UtilityMethods.sort(lvVehMk);

			Vector vcComboVal = new Vector();
			if (lvVehMk != null)
			{
				for (int liIndex = 0;
					liIndex < lvVehMk.size();
					liIndex++)
				{
					VehicleMakesData laVehMk =
						(VehicleMakesData) lvVehMk.get(liIndex);
					String lsDesc =
						laVehMk.getVehMk()
							+ CommonConstant.STR_SPACE_ONE
							+ CommonConstant.STR_DASH
							+ laVehMk.getVehMkDesc();
					vcComboVal.add(lsDesc);
				}
				vcComboVal.add(NEW);
			}
			DefaultComboBoxModel laMDL =
				new DefaultComboBoxModel(vcComboVal);
			getJComboBoxMake().setModel(laMDL);

			getJComboBoxMake().setSelectedIndex(-1);
			// defect 8479
			comboBoxHotKeyFix(getJComboBoxMake());
			// end defect 8479
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
		if (aaDataObject != null)
		{
			caVehInqData = (VehicleInquiryData) aaDataObject;
			//Common is filling the validation object. 
			//	Need to initialize it back to null.
			if (caVehInqData != null)
			{
				TitleValidObj laValidObj = new TitleValidObj();
				MFVehicleData laMfOrig =
					caVehInqData.getMfVehicleData();
				MFVehicleData laMfOrigCpy =
					(MFVehicleData) UtilityMethods.copy(laMfOrig);
				laValidObj.setMfVehOrig(laMfOrigCpy);
				caVehInqData.setValidationObject(laValidObj);
			}

			caMFVehicleData = caVehInqData.getMfVehicleData();
			int liNoOfVehicleRecords = caVehInqData.getNoMFRecs();
			TitleData laTtlData =
				caVehInqData.getMfVehicleData().getTitleData();
			RegistrationData laRegData =
				caVehInqData.getMfVehicleData().getRegData();
			OwnerData laOwnData =
				caVehInqData.getMfVehicleData().getOwnerData();
			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();

			if (liNoOfVehicleRecords > 0)
			{
				// defect 10112 
				// Implement new OwnerData, new AddressData method
				// Get and set owner data
				getlblOwnerId().setText(laOwnData.getOwnrId());
				getlblOwnerTitleName1().setText(laOwnData.getName1());

				getlblOwnerTitleName2().setText(laOwnData.getName2());

				gettxtAOwnrAddr().setText(
					laOwnData
						.getAddressData()
						.getAddressStringBuffer()
						.toString());

				//	gettxtAOwnrAddr().setText(
				//		laOwnData.getAddressData().getSt1()
				//			+ CommonConstant.SYSTEM_LINE_SEPARATOR);
				//	if (!laOwnData
				//		.getAddressData()
				//		.getSt2()
				//			.equals(CommonConstant.STR_SPACE_EMPTY))
				//	{
				//		gettxtAOwnrAddr().append(
				//			laOwnData.getAddressData().getSt2()
				//				+ CommonConstant.SYSTEM_LINE_SEPARATOR);
				//	}
				//	gettxtAOwnrAddr().append(
				//		laOwnData.getAddressData().getCity()
				//			+ CommonConstant.STR_COMMA
				//			+ CommonConstant.STR_SPACE_ONE);
				//	if (laOwnData
				//		.getAddressData()
				//		.getCntry()
				//		.equals(CommonConstant.STR_SPACE_EMPTY))
				//	{
				//		gettxtAOwnrAddr().append(
				//			laOwnData.getAddressData().getState()
				//				+ CommonConstant.STR_SPACE_TWO);
				//		gettxtAOwnrAddr().append(
				//			laOwnData.getAddressData().getZpcd());
				//		if (!laOwnData
				//			.getAddressData()
				//			.getZpcdp4()
				//			.equals(CommonConstant.STR_SPACE_EMPTY))
				//		{
				//			gettxtAOwnrAddr().append(
				//				CommonConstant.STR_DASH
				//					+ laOwnData.getAddressData().getZpcdp4());
				//		}
				//	}
				//	else
				//	{
				//		gettxtAOwnrAddr().append(
				//			laOwnData.getAddressData().getCntry()
				//				+ CommonConstant.STR_SPACE_TWO);
				//		gettxtAOwnrAddr().append(
				//			laOwnData.getAddressData().getZpcd());
				//		gettxtAOwnrAddr().append(
				//			laOwnData.getAddressData().getZpcdp4());
				//	}
				// end defect 10112 

				// Get and set registration data
				getlblRegPlateNo().setText(laRegData.getRegPltNo());
				// defect 8900
				// Do not present "0" for Exp Mo/Yr
				int liRegExpMo = laRegData.getRegExpMo();
				getlblRegExpMo().setText(
					liRegExpMo == 0
						? ""
						: Integer.toString(liRegExpMo));

				int liRegExpYr = laRegData.getRegExpYr();
				getlblRegExpYr().setText(
					liRegExpYr == 0
						? ""
						: Integer.toString(liRegExpYr));
				// end defect 8900

				getlblResComptCntyNo().setText(
					Integer.toString(laRegData.getResComptCntyNo()));
					
				// Get and set vehicle data
				gettxtVehicleModelYear().setText(
					Integer.toString(laVehData.getVehModlYr()));
				gettxtVehicleMake().setText(laVehData.getVehMk());
				getlblVehicleBodyType().setText(
					laVehData.getVehBdyType());
				gettxtVehicleModel().setText(laVehData.getVehModl());
				gettxtVIN().setText(laVehData.getVin());

				// Get and set title data
				getlblDocNo().setText(laTtlData.getDocNo());
				//get doc type description from cache
				int liDocTypeCd = laTtlData.getDocTypeCd();
				String lsDocTypeCd =
					DocumentTypesCache
						.getDocType(liDocTypeCd)
						.getDocTypeCdDesc();
				getlblDocTypeCodeDesc().setText(lsDocTypeCd);
				if (laTtlData.getTtlIssueDate() != 0)
				{
					RTSDate laTtlIssDt =
						new RTSDate(
							RTSDate.YYYYMMDD,
							laTtlData.getTtlIssueDate());
					getlblTitleIssueDate().setText(
						laTtlIssDt.toString());
				}
				else
				{
					getlblTitleIssueDate().setText(DATE_FORMAT);
				}

				getIndicators();

				// Enable/disable fields
				gettxtVehicleModelYear().setEnabled(false);
				gettxtVehicleMake().setEnabled(false);
				gettxtVehicleModel().setEnabled(false);
				gettxtVIN().setEnabled(false);
				getJComboBoxBodyStyle().setVisible(false);
				getJComboBoxBodyStyle().setEnabled(false);
				getJComboBoxMake().setVisible(false);
				getJComboBoxMake().setEnabled(false);

				// Enable/disable radio buttons depending on security 
				//access
				SecurityData laScrtyData =
					getController()
						.getMediator()
						.getDesktop()
						.getSecurityData();

				if (laScrtyData.getCancRegAccs() == 0)
				{
					getradioCancelRegistration().setEnabled(false);
				}
				else
				{
					if (laScrtyData.getCancRegAccs() == 1
						&& !SystemProperty.isCounty())
					{
						getradioCancelRegistration().setSelected(true);
					}
					else
					{
						// Modified parameters for isPlateOutsideScope 
						//if (isPlateOutsideScope(liOfcIssuanceCd))
						if (isPlateOutsideScope(SystemProperty
							.getOfficeIssuanceCd()))
						{
							// plate is not available for processing
							// for the given office
							getradioCancelRegistration().setSelected(
								true);
						}
						else
						{
							getradioCancelRegistration().setEnabled(
								false);
						}
					}
				}
				// end defect 9085
				if (laScrtyData.getRegRefAmtAccs() == 1)
				{
					getradioRegistrationRefund().setSelected(true);
				}
				else
				{
					getradioRegistrationRefund().setEnabled(false);
				}
				if (laScrtyData.getStlnSRSAccs() == 1)
				{
					getradioStolenSRS().setSelected(true);
				}
				else
				{
					getradioStolenSRS().setEnabled(false);
				}
				if (laScrtyData.getMiscRemksAccs() == 1)
				{
					getradioMiscellaneousRemarks().setSelected(true);
				}
				else
				{
					getradioMiscellaneousRemarks().setEnabled(false);
				}
				if (laScrtyData.getTtlSurrAccs() == 1)
				{
					getradioTitleSurrendered().setSelected(true);
				}
				else
				{
					getradioTitleSurrendered().setEnabled(false);
				}
				if (laScrtyData.getJnkAccs() == 1)
				{
					getradioVehicleJunked().setSelected(true);
				}
				else
				{
					getradioVehicleJunked().setEnabled(false);
				}
			}
			else
			{
				// Set owner data
				getlblOwnerId().setText(CommonConstant.STR_SPACE_EMPTY);
				getlblOwnerTitleName1().setText(
					CommonConstant.STR_SPACE_EMPTY);
				getlblOwnerTitleName1().setBackground(
					getlblOwnerTitleName2().getBackground());
				getlblOwnerTitleName2().setText(
					CommonConstant.STR_SPACE_EMPTY);
				gettxtAOwnrAddr().setText(
					CommonConstant.STR_SPACE_EMPTY);

				// Set registration data
				if (laRegData.getRegPltNo() != null)
				{
					getlblRegPlateNo().setText(laRegData.getRegPltNo());
				}
				else
				{
					getlblRegPlateNo().setText(
						CommonConstant.STR_SPACE_EMPTY);
				}
				getlblRegExpMo().setText(CommonConstant.STR_ZERO);
				getlblRegExpYr().setText(CommonConstant.STR_ZERO);
				getlblResComptCntyNo().setText(CommonConstant.STR_ZERO);

				// Set vehicle data
				// defect 10038 
				// Do not populate w/ "0" 
				gettxtVehicleModelYear().setText(
					CommonConstant.STR_SPACE_EMPTY);
				// end defect 10038 

				getlblVehicleBodyType().setText(
					CommonConstant.STR_SPACE_EMPTY);
				if (laVehData.getVin() != null)
				{
					gettxtVIN().setText(laVehData.getVin());
				}
				else
				{
					gettxtVIN().setText(CommonConstant.STR_SPACE_EMPTY);
				}

				// Get and set title data
				if (laTtlData.getDocNo() != null)
				{
					getlblDocNo().setText(laTtlData.getDocNo());
				}
				else
				{
					getlblDocNo().setText(
						CommonConstant.STR_SPACE_EMPTY);
				}
				getlblDocTypeCodeDesc().setText(
					CommonConstant.STR_SPACE_EMPTY);
				getlblTitleIssueDate().setText(DATE_FORMAT);

				// Enable/disable fields
				gettxtVehicleModelYear().setEnabled(true);
				gettxtVehicleModel().setEnabled(true);
				gettxtVIN().setEnabled(true);
				getJComboBoxBodyStyle().setVisible(true);
				getJComboBoxBodyStyle().setEnabled(true);
				getJComboBoxMake().setVisible(true);
				getJComboBoxMake().setEnabled(true);
				gettxtVehicleMake().setVisible(false);

				// Disable all radio buttons except Miscellaneous 
				//	Remarks which is selected.
				getradioCancelRegistration().setEnabled(false);
				getradioRegistrationRefund().setEnabled(false);
				getradioStolenSRS().setEnabled(false);
				getradioTitleSurrendered().setEnabled(false);
				getradioVehicleJunked().setEnabled(false);
				getradioMiscellaneousRemarks().setSelected(true);
				// defect 10038
				setDefaultFocusField(gettxtVehicleModelYear());
				getlstIndiDescription().setEnabled(false);
				//ciSelectedNum = 2;
				// end defect 10038  
			}

			// defect 8494
			// Moved from windowOpened
			// Set focus to indicator box if more than x entries 
			if (cvIndicator != null)
			{
				// defect 9971 
				// Use CommonConstant.MAX_INDI_NO_SCROLL
				if (cvIndicator.size()
					> CommonConstant.MAX_INDI_NO_SCROLL)
				{
					// end defect 9971 
					setDefaultFocusField(getlstIndiDescription());
				}
			}
			// end defect 8494
		}
	}

	/**
	 * setDataToVehObj
	 */
	private void setDataToVehObj()
	{

		VehicleData laVehData =
			caVehInqData.getMfVehicleData().getVehicleData();

		if (laVehData != null)
		{
			String lsVehMake = CommonConstant.STR_SPACE_EMPTY;
			String lsBdySt = CommonConstant.STR_SPACE_EMPTY;
			String lsVehModlYr =
				gettxtVehicleModelYear().getText().trim();
			String lsVehModl = gettxtVehicleModel().getText().trim();
			String lsVIN = gettxtVIN().getText().trim();
			if (getJComboBoxBodyStyle().isVisible())
			{
				lsBdySt =
					(String) getJComboBoxBodyStyle().getSelectedItem();
				lsBdySt =
					lsBdySt
						.substring(
							0,
							lsBdySt.indexOf(CommonConstant.STR_DASH))
						.trim();
			}
			else
			{
				lsBdySt = getlblVehicleBodyType().getText();
			}
			if (getJComboBoxMake().isVisible())
			{
				lsVehMake =
					(String) getJComboBoxMake().getSelectedItem();
				lsVehMake =
					lsVehMake
						.substring(
							0,
							lsVehMake.indexOf(CommonConstant.STR_DASH))
						.trim();
			}
			else
			{
				lsVehMake = gettxtVehicleMake().getText().trim();
			}

			laVehData.setVehModlYr(Integer.parseInt(lsVehModlYr));
			laVehData.setVehMk(lsVehMake);
			laVehData.setVehBdyType(lsBdySt);
			laVehData.setVehModl(lsVehModl);
			laVehData.setVin(lsVIN);
		}
	}

	/** 
	 * 
	 * Validate Data (on no record found)
	 * 
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;

		if (caVehInqData.getNoMFRecs() == 0)
		{
			RTSException leRTSEx = new RTSException();

			// Validate vehicle year
			if (CommonValidations
				.isInvalidYearModel(gettxtVehicleModelYear().getText()))
			{
				leRTSEx.addException(
				//new RTSException(150),
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtVehicleModelYear());
			}
			// Validate vehicle make
			if (getJComboBoxMake().isEnabled()
				&& getJComboBoxMake().getSelectedItem() == null)
			{
				leRTSEx.addException(
				//new RTSException(150),
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getJComboBoxMake());
			}
			else if (
				gettxtVehicleMake().isEnabled()
					&& (gettxtVehicleMake().getText() == null
						|| gettxtVehicleMake().getText().equals(
							CommonConstant.STR_SPACE_EMPTY)))
			{
				leRTSEx.addException(
				// new RTSException(150),
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtVehicleMake());
			}
			// Validate vehicle body type
			if (getJComboBoxMake().isEnabled()
				&& getJComboBoxBodyStyle().getSelectedItem() == null)
			{
				leRTSEx.addException(
				//new RTSException(150),
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getJComboBoxBodyStyle());
			}
			if (leRTSEx.isValidationError())
			{
				leRTSEx.displayError(this);
				leRTSEx.getFirstComponent().requestFocus();
				lbReturn = false;
			}
		}
		return lbReturn;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
