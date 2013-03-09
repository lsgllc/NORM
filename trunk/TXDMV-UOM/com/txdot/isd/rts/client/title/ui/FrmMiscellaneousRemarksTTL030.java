package com.txdot.isd.rts.client.title.ui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.services.cache.DocumentTypesCache;
import com.txdot.isd.rts.services.cache.VehicleColorCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.DocTypeConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmMiscellaneousRemarksTTL030.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * J Rue		03/09/2005	VAJ to WSAD Clean Up
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
 * 							defect 7899  Ver 5.2.3  
 * S Johnston	06/22/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify keyPressed
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
 * K Harrell	05/07/2007	Use SystemProperty.isCounty()
 * 							modify setData()
 * 							defect 9085 Ver Special Plates  
 * K Harrell	04/06/2009	set ETtlCd for TitleData 
 * 							modify LEGAL_RESTRAINT_MAX_LEN
 * 							modify setDataToVehObj() 
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	05/29/2009	Verify LegalRestraintNo field not empty
 * 							on NoRecordFound.  Sorted members. 
 * 							Screen alignment via Visual Editor.
 * 							Constants for mnemonics. Add'l screen 
 * 							 cleanup. 
 * 							add validateData()
 * 							modify actionPerformed() 
 * 							defect 10036 Ver Defect_POS_F 
 * K Harrell	02/18/2010	Add MF Character validation for 
 * 								Registered By
 * 							add cvMFValid
 * 							modify initialize(), validateData()
 * 							defect 10347 Ver POS_640
 * Min Wang		03/15/2010	Remove privacy act options from the screen.		
 * 							delete BOTH, COMMERCIAL, INDIVIDUAL, NONE,
 * 							ivjradioBoth, ivjradioCommercial, 
 * 							ivjradioIndividual, ivjradioNone,	
 * 							getJPanel3(),getradioBoth(),getradioCommercial(),
 * 							getradioIndividual(), getradioNone(),
 * 							modify setData(),
 * 							getFrmMiscellaneousRemarksTTL030ContentPane1(),
 * 	 						Used VE to change the frames.
 * 							defect 10159 Ver POS_640						
 * T Pederson	06/16/2011	Added vehicle color drop down boxes 
 * 							add ivjcomboMajorColor, ivjcomboMinorColor,
 * 							ivjstcLblColorMajor, ivjstcLblColorMinor,
 * 							MAJORCOLOR, MINORCOLOR, 
 * 							add getcomboMajorColor(), getcomboMinorColor(),
 * 							populateMajorColor(), populateMinorColor(),
 * 							populateVehColorVector(),
 * 							getMajorColorCdSelected(), 
 * 							getMinorColorCdSelected(),
 * 							isMajorColorSameAsMinorColor(),
 * 							isMinorColorSelectedMajorColorNotSelected(),
 * 							modify setData(), actionPerformed(), 
 * 							setDataToVehObj(), validateData()
 * 							defect 10830 Ver 6.8.0
 * K Harrell	11/12/2011	add isDataModified()
 * 							modify actionPerformed(),setDataToVehObj() 
 * 							defect 11004 Ver 6.9.0 
 * K Harrell	12/03/2011	Validity check issue w/ G360 automation  
 * 							delete isDataModified()
 * 							modify setDataToVehObj() 
 * 							defect 11004 Ver 6.9.0
 * B Woodson    01/12/2012	add EXPORT, getchkExport(), isAllowedDocTypeForExport(),
 * 							ivjchkExport
 * 							modify getButtonPanel1(), getchkDeleteVehicleSoldDate(),
 * 							getchkTitleRevoked(), getJPanel1(), getJPanel2(),
 * 							getstcLblStopRefer(), getstcLblVehicleSoldDate(), 
 * 							gettxtLegalRestraintNo(), gettxtVehicleSoldDate(),
 * 							initialize(), setData(), setDataToVeh()
 * 							defect 11228 Ver 6.10.0
 * B Woodson	01/25/2012	modify setData() again for additional requirement 
 * 							 to disable ivjchkExport if checked
 * 							defect 11228 Ver 6.10.0
 * B Woodson	02/02/2012	modify setData again for rts_doc_types.doctypegrpcd
 * 							removed isAllowedDocTypeForExport()
 * B Woodson	02/10/2012	modified getchkExport() to hide checkbox, validateData()
 * 							defect 11228 Ver 6.10.0
 * B Woodson	02/13/2012	modified validateData() to use 
 * 							 ErrorsConstant.ERR_NUM_ACTION_INVALID_EXPORT_LEGAL_RESTRAINT
 * 							defect 11228 Ver 6.10.0
 * 
 * ---------------------------------------------------------------------
 */
/**
 * This form is used to capture miscellaneous remarks in status change. 
 *
 * @version	6.10.0 			02/13/2012
 * @author	Todd Pederson
 * <br>Creation Date:		06/26/2001 15:26:56
 */
public class FrmMiscellaneousRemarksTTL030
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkDeleteVehicleSoldDate = null;
	private JCheckBox ivjchkMailReturn = null;
	private JCheckBox ivjchkTitleRevoked = null;
	private JCheckBox ivjchkVINInError = null;
	// defect 10830 
	private JComboBox ivjcomboMajorColor = null;
	private JComboBox ivjcomboMinorColor = null;
	private Vector cvVehColor = new Vector();
	// end defect 10830 
	private JComboBox ivjcomboSelectBonded = null;
	private JPanel ivjFrmMiscellaneousRemarksTTL030ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	// defect 10830 
	private JLabel ivjstcLblColorMajor = null;
	private JLabel ivjstcLblColorMinor = null;
	// end defect 10830 
	private JLabel ivjstcLblRegisteredBy = null;
	private JLabel ivjstcLblStopRefer = null;
	private JLabel ivjstcLblVehicleSoldDate = null;
	private RTSInputField ivjtxtLegalRestraintNo = null;
	private RTSInputField ivjtxtRegPlateOwnerName = null;
	private RTSDateField ivjtxtVehicleSoldDate = null;

	private VehicleInquiryData caVehInqData = null;

	// defect 10347 
	Vector cvMFValid;
	// end defect 10347 

	private static final String B = "B";
	private static final String BONDED = "BONDED TITLE";
	private static final String BONDEDWAITING =
		"BONDED TITLE WAITING FOR REMOVAL";
	private static final String CHK_IF_APPLICABLE =
		"Check if Applicable:";
	private static final String DEL_VEH_SOLD_DATE =
		"Delete Vehicle Sold Date";
	private static final String EMPTY = CommonConstant.STR_SPACE_EMPTY;
	//defect 11228
	private static final String EXPORT = "Export";
	//end defect 11228
	private static final int LEGAL_RESTRAINT_MAX_LEN = 9;
	private static final String MAILED_RETURNED = "Mail Returned";
	private static final int OWNR_NAME_MAX_LEN = 30;
	private static final String REGIS_BY = "Registered By: ";
	private static final String S = "S";
	private static final String SEL_BONDED_TTL =
		"Select Bonded Title Code:";
	private static final String STOP_REFER = "Stop Refer:";
	private static final String SUSPENDED = "SUSPENDED BONDED TITLE";
	private static final String TITLE_REVOKED = "Title Revoked";
	private static final String VEH_SOLD_DATE = " Vehicle Sold Date:";
	private static final String VIN_IN_ERROR = "VIN in Error";
	private static final String W = "W";

	// defect 10830 
	private final static int ZERO = 0;
	private static final String MAJORCOLOR = "Major Color:";
	private static final String MINORCOLOR = "Minor Color:";
	// end defect 10830
	
	//defect 11228
	private JCheckBox ivjchkExport = null;
	//end defect 11228
 



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
			FrmMiscellaneousRemarksTTL030 laFrmMiscellaneousRemarksTTL030;
			laFrmMiscellaneousRemarksTTL030 =
				new FrmMiscellaneousRemarksTTL030();
			laFrmMiscellaneousRemarksTTL030.setModal(true);
			laFrmMiscellaneousRemarksTTL030
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmMiscellaneousRemarksTTL030.show();
			Insets laInsets =
				laFrmMiscellaneousRemarksTTL030.getInsets();
			laFrmMiscellaneousRemarksTTL030.setSize(
				laFrmMiscellaneousRemarksTTL030.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmMiscellaneousRemarksTTL030.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmMiscellaneousRemarksTTL030.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmMiscellaneousRemarksTTL030 constructor
	 */
	public FrmMiscellaneousRemarksTTL030()
	{
		super();
		initialize();
	}

	/**
	 * FrmMiscellaneousRemarksTTL030 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmMiscellaneousRemarksTTL030(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmMiscellaneousRemarksTTL030 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmMiscellaneousRemarksTTL030(JFrame aaParent)
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
			clearAllColor(this);

			// Clear the sold date field when the check box is checked
			if (aaAE.getSource() == getchkDeleteVehicleSoldDate())
			{
				if (getchkDeleteVehicleSoldDate().isSelected())
				{
					gettxtVehicleSoldDate().setText(EMPTY);
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10036 
				// moved validation of data to validateData()
				if (validateData())
				{
					setDataToVehObj();
					getController().processData(
						AbstractViewController.ENTER,
						getController().getData());
				}
				// end defect 10036 
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
				RTSHelp.displayHelp(RTSHelp.TTL030);
			}
		}
		// defect 11004
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this); 
		}
		// end defect 11004 
		finally
		{
			doneWorking();
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setBounds(49, 423, 312, 40);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setRequestFocusEnabled(false);
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
	 * Return the chkDeleteVehicleSoldDate property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkDeleteVehicleSoldDate()
	{
		if (ivjchkDeleteVehicleSoldDate == null)
		{
			try
			{
				ivjchkDeleteVehicleSoldDate = new JCheckBox();
				ivjchkDeleteVehicleSoldDate.setName(
					"ivjchkDeleteVehicleSoldDate");
				ivjchkDeleteVehicleSoldDate.setBounds(115, 388, 174, 22);
				ivjchkDeleteVehicleSoldDate.setText(DEL_VEH_SOLD_DATE);
				ivjchkDeleteVehicleSoldDate.setActionCommand(
					DEL_VEH_SOLD_DATE);
				// user code begin {1}
				ivjchkDeleteVehicleSoldDate.setMnemonic(KeyEvent.VK_D);
				ivjchkDeleteVehicleSoldDate.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDeleteVehicleSoldDate;
	}
	
	//defect 11228
	/**
	 * This method initializes ivjchkExport	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getchkExport()
	{
		if (ivjchkExport == null)
		{
			ivjchkExport = new JCheckBox();
			ivjchkExport.setBounds(66, 106, 110, 21);
			ivjchkExport.setMnemonic(KeyEvent.VK_E);
			ivjchkExport.setText(EXPORT);
			ivjchkExport.setName("ivjchkTitleRevoked");
			ivjchkExport.setVisible(false);
		}
		return ivjchkExport;
	}	
	//end defect 11228

	/**
	 * Return the chkMailreturn property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkMailReturn()
	{
		if (ivjchkMailReturn == null)
		{
			try
			{
				ivjchkMailReturn = new JCheckBox();
				ivjchkMailReturn.setName("ivjchkMailReturn");
				ivjchkMailReturn.setLocation(66, 27);
				ivjchkMailReturn.setSize(135, 22);
				ivjchkMailReturn.setText(MAILED_RETURNED);
				ivjchkMailReturn.setActionCommand(MAILED_RETURNED);
				// user code begin {1}
				ivjchkMailReturn.setMnemonic(KeyEvent.VK_M);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkMailReturn;
	}

	/**
	 * Return the chkTitlerevoked property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkTitleRevoked()
	{
		if (ivjchkTitleRevoked == null)
		{
			try
			{
				ivjchkTitleRevoked = new JCheckBox();
				ivjchkTitleRevoked.setName("ivjchkTitleRevoked");
				ivjchkTitleRevoked.setLocation(66, 81);
				ivjchkTitleRevoked.setSize(149, 22);
				ivjchkTitleRevoked.setText(TITLE_REVOKED);
				// user code begin {1}
				ivjchkTitleRevoked.setMnemonic(KeyEvent.VK_T);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkTitleRevoked;
	}

	/**
	 * Return the chkVINInError property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkVINInError()
	{
		if (ivjchkVINInError == null)
		{
			try
			{
				ivjchkVINInError = new JCheckBox();
				ivjchkVINInError.setName("ivjchkVINInError");
				ivjchkVINInError.setBounds(66, 55, 143, 22);
				ivjchkVINInError.setText(VIN_IN_ERROR);
				ivjchkVINInError.setActionCommand(VIN_IN_ERROR);
				// user code begin {1}
				ivjchkVINInError.setMnemonic(KeyEvent.VK_V);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkVINInError;
	}

	/**
	 * Return the comboMajorColor property value.
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getcomboMajorColor()
	{
		if (ivjcomboMajorColor == null)
		{
			try
			{
				ivjcomboMajorColor = new javax.swing.JComboBox();
				ivjcomboMajorColor.setBounds(118, 9, 178, 24);
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
	private javax.swing.JComboBox getcomboMinorColor()
	{
		if (ivjcomboMinorColor == null)
		{
			try
			{
				ivjcomboMinorColor = new javax.swing.JComboBox();
				ivjcomboMinorColor.setBounds(118, 37, 178, 24);
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
	 * Return the comboSelectBonded property value.
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboSelectBonded()
	{
		if (ivjcomboSelectBonded == null)
		{
			try
			{
				ivjcomboSelectBonded = new JComboBox();
				ivjcomboSelectBonded.setName("ivjcomboSelectBonded");
				ivjcomboSelectBonded.setLocation(15, 25);
				ivjcomboSelectBonded.setSize(294, 24);
				ivjcomboSelectBonded.setEditor(
					new javax
						.swing
						.plaf
						.metal
						.MetalComboBoxEditor
						.UIResource());
				ivjcomboSelectBonded.setRenderer(
					new javax
						.swing
						.plaf
						.basic
						.BasicComboBoxRenderer
						.UIResource());
				ivjcomboSelectBonded.setBackground(Color.white);
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
		return ivjcomboSelectBonded;
	}

	/**
	 * Return the FrmMiscellaneousRemarksTTL030ContentPane1 property 
	 * value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmMiscellaneousRemarksTTL030ContentPane1()
	{
		if (ivjFrmMiscellaneousRemarksTTL030ContentPane1 == null)
		{
			try
			{
				ivjFrmMiscellaneousRemarksTTL030ContentPane1 =
					new JPanel();
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.setName(
					"FrmMiscellaneousRemarksTTL030ContentPane1");
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.setLayout(
					null);

				ivjFrmMiscellaneousRemarksTTL030ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					getstcLblRegisteredBy(),
					null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					gettxtRegPlateOwnerName(),
					null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					getstcLblStopRefer(),
					null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					getJPanel1(),
					null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					gettxtLegalRestraintNo(),
					null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					getstcLblVehicleSoldDate(),
					null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					gettxtVehicleSoldDate(),
					null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					getchkDeleteVehicleSoldDate(),
					null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(
					getJPanel2(),
					null);
				// defect 10159
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(getcomboMinorColor(), null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(getcomboMajorColor(), null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(getstcLblColorMinor(), null);
				ivjFrmMiscellaneousRemarksTTL030ContentPane1.add(getstcLblColorMajor(), null);
				// end defect 10159
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
		return ivjFrmMiscellaneousRemarksTTL030ContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
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
				ivjJPanel1.setBounds(35, 93, 324, 148);
				ivjJPanel1.setLayout(null);
				ivjJPanel1.add(getchkMailReturn(), null);
				ivjJPanel1.add(getchkVINInError(), null);
				ivjJPanel1.add(getchkTitleRevoked(), null);
				//defect 11228
				ivjJPanel1.add(getchkExport(), null);
				//end defect 11228
				// user code begin {1} 
				Border laBrdr =
					new TitledBorder(
						new EtchedBorder(),
						CHK_IF_APPLICABLE);
				ivjJPanel1.setBorder(laBrdr);
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
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("ivjJPanel2");
				ivjJPanel2.setBounds(36, 284, 326, 66);
				ivjJPanel2.setLayout(null);
				ivjJPanel2.add(getcomboSelectBonded(), null);
				// user code begin {1}
				Border laBrdr =
					new TitledBorder(
						new EtchedBorder(),
						SEL_BONDED_TTL);
				ivjJPanel2.setBorder(laBrdr);
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
	 * Return the stcLblColorMajor property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblColorMajor()
	{
		if (ivjstcLblColorMajor == null)
		{
			try
			{
				ivjstcLblColorMajor = new javax.swing.JLabel();
				ivjstcLblColorMajor.setBounds(35, 15, 77, 14);
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
	private javax.swing.JLabel getstcLblColorMinor()
	{
		if (ivjstcLblColorMinor == null)
		{
			try
			{
				ivjstcLblColorMinor = new javax.swing.JLabel();
				ivjstcLblColorMinor.setBounds(35, 43, 77, 14);
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
	 * Return the ivjstcLblRegisteredBy property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRegisteredBy()
	{
		if (ivjstcLblRegisteredBy == null)
		{
			try
			{
				ivjstcLblRegisteredBy = new JLabel();
				ivjstcLblRegisteredBy.setName("ivjstcLblRegisteredBy");
				ivjstcLblRegisteredBy.setLocation(27, 65);
				ivjstcLblRegisteredBy.setSize(85, 20);
				ivjstcLblRegisteredBy.setText(REGIS_BY);
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
		return ivjstcLblRegisteredBy;
	}

	/**
	 * Return the ivjstcLblStopRefer property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblStopRefer()
	{
		if (ivjstcLblStopRefer == null)
		{
			try
			{
				ivjstcLblStopRefer = new JLabel();
				ivjstcLblStopRefer.setName("ivjstcLblStopRefer");
				ivjstcLblStopRefer.setLocation(49, 253);
				ivjstcLblStopRefer.setSize(65, 20);
				ivjstcLblStopRefer.setText(STOP_REFER);
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
		return ivjstcLblStopRefer;
	}

	/**
	 * Return the ivjstcLblVehicleSoldDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVehicleSoldDate()
	{
		if (ivjstcLblVehicleSoldDate == null)
		{
			try
			{
				ivjstcLblVehicleSoldDate = new JLabel();
				ivjstcLblVehicleSoldDate.setName(
					"ivjstcLblVehicleSoldDate");
				ivjstcLblVehicleSoldDate.setLocation(106, 361);
				ivjstcLblVehicleSoldDate.setSize(106, 20);
				ivjstcLblVehicleSoldDate.setText(VEH_SOLD_DATE);
				// user code begin {1}
				ivjstcLblVehicleSoldDate.setDisplayedMnemonic(
					KeyEvent.VK_S);
				ivjstcLblVehicleSoldDate.setLabelFor(
					gettxtVehicleSoldDate());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblVehicleSoldDate;
	}

	/**
	 * Return the ivjtxtLegalRestraintNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtLegalRestraintNo()
	{
		if (ivjtxtLegalRestraintNo == null)
		{
			try
			{
				ivjtxtLegalRestraintNo = new RTSInputField();
				ivjtxtLegalRestraintNo.setName(
					"ivjtxtLegalRestraintNo");
				ivjtxtLegalRestraintNo.setLocation(120, 253);
				ivjtxtLegalRestraintNo.setSize(119, 20);
				ivjtxtLegalRestraintNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtLegalRestraintNo.setMaxLength(
					LEGAL_RESTRAINT_MAX_LEN);
				ivjtxtLegalRestraintNo.setText("");
				// user code begin {1}
				ivjtxtLegalRestraintNo.setInput(RTSInputField.DEFAULT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLegalRestraintNo;
	}

	/**
	 * Return the ivjtxtRegPlateOwnerName property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRegPlateOwnerName()
	{
		if (ivjtxtRegPlateOwnerName == null)
		{
			try
			{
				ivjtxtRegPlateOwnerName = new RTSInputField();
				ivjtxtRegPlateOwnerName.setName(
					"ivjtxtRegPlateOwnerName");
				ivjtxtRegPlateOwnerName.setLocation(118, 65);
				ivjtxtRegPlateOwnerName.setSize(242, 20);
				ivjtxtRegPlateOwnerName.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// user code begin {1}
				ivjtxtRegPlateOwnerName.setMaxLength(OWNR_NAME_MAX_LEN);
				ivjtxtRegPlateOwnerName.setInput(RTSInputField.DEFAULT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtRegPlateOwnerName;
	}

	/**
	 * Return the ivjtxtVehicleSoldDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtVehicleSoldDate()
	{
		if (ivjtxtVehicleSoldDate == null)
		{
			try
			{
				ivjtxtVehicleSoldDate = new RTSDateField();
				ivjtxtVehicleSoldDate.setName("ivjtxtVehicleSoldDate");
				ivjtxtVehicleSoldDate.setLocation(220, 361);
				ivjtxtVehicleSoldDate.setSize(75, 20);
				ivjtxtVehicleSoldDate.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
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
		return ivjtxtVehicleSoldDate;
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
			setName(ScreenConstant.TTL030_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(404, 514);
			setTitle(ScreenConstant.TTL030_FRAME_TITLE);
			setContentPane(
				getFrmMiscellaneousRemarksTTL030ContentPane1());
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
		// defect 10347  
		cvMFValid = new Vector();
		cvMFValid.add(gettxtRegPlateOwnerName());
		// end defect 10347
	}
	
//	/**
//	 * Determine if Data has been changed before allowing transaction completion
//	 * 
//	 * @return boolean 
//	 */
//	private boolean isDataModified()
//	{
//		TitleData laTtlData = caVehInqData.getMfVehicleData().getTitleData();
//		VehicleData laVehData = caVehInqData.getMfVehicleData().getVehicleData();
//		RegistrationData laRegData = caVehInqData.getMfVehicleData().getRegData();
//		
//		TitleValidObj laTtlValidObj = (TitleValidObj) caVehInqData.getValidationObject();
//		MFVehicleData laMfOrig = (MFVehicleData) laTtlValidObj.getMfVehOrig(); 
//		TitleData laTtlOrig = laMfOrig.getTitleData(); 
//		VehicleData laVehOrig = laMfOrig.getVehicleData();
//		RegistrationData laRegOrig = laMfOrig.getRegData();
//		
//		return !laVehData.getVehMjrColorCd().equals(laVehOrig.getVehMjrColorCd()) 
//		||  !laVehData.getVehMnrColorCd().equals(laVehOrig.getVehMnrColorCd())
//		|| !laRegData.getRegPltOwnrName().equals(laRegOrig.getRegPltOwnrName())
//		|| !laTtlData.getLegalRestrntNo().equals(laTtlOrig.getLegalRestrntNo())
//		|| !laTtlData.getBndedTtlCd().equals(laTtlOrig.getBndedTtlCd())
//		|| laRegData.getRenwlMailRtrnIndi() != laRegOrig.getRenwlMailRtrnIndi()
//		|| laVehData.getVinErrIndi() != laVehOrig.getVinErrIndi() 
//		|| laTtlData.getTtlRevkdIndi()!= laTtlOrig.getTtlRevkdIndi()		 
//		|| laTtlData.getVehSoldDate() != laTtlOrig.getVehSoldDate(); 
//	}
	
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
			VehicleData laVehData =
				caVehInqData.getMfVehicleData().getVehicleData();
			TitleData laTtlData =
				caVehInqData.getMfVehicleData().getTitleData();
			RegistrationData laRegData =
				caVehInqData.getMfVehicleData().getRegData();

			// Get and set sold date coming from mainframe
			if (laTtlData.getVehSoldDate() > 0)
			{
				RTSDate laVehSoldDt =
					new RTSDate(
						RTSDate.YYYYMMDD,
						laTtlData.getVehSoldDate());
				gettxtVehicleSoldDate().setDate(laVehSoldDt);
			}

			// Get and set data coming from mainframe
			if (laRegData.getRegPltOwnrName() != null)
			{
				gettxtRegPlateOwnerName().setText(
					laRegData.getRegPltOwnrName());
			}
			gettxtLegalRestraintNo().setText(
				laTtlData.getLegalRestrntNo());
			if (laRegData.getRenwlMailRtrnIndi() == 1)
			{
				getchkMailReturn().setSelected(true);
			}
			if (laVehData.getVinErrIndi() == 1)
			{
				getchkVINInError().setSelected(true);
			}
			if (laTtlData.getTtlRevkdIndi() == 1)
			{
				getchkTitleRevoked().setSelected(true);
			}
			
			// defect 10830
			if (cvVehColor.size() == 0)
			{
				populateVehColorVector();
				populateMajorColor(); 
				populateMinorColor(); 
			}
			// end defect 10830

			SecurityData laSecurityData =
				getController()
					.getMediator()
					.getDesktop()
					.getSecurityData();
			
			//defect 11228
			if (SystemProperty.isHQ() &&
					caVehInqData.getNoMFRecs() > 0 &&
					laSecurityData.getExportAccs() == 1 &&
					DocumentTypesCache.isExportEligible(laTtlData.getDocTypeCd()))
			{
				getchkExport().setEnabled(true);
			}
			else
			{
				getchkExport().setEnabled(false);
			}
			
			if (laTtlData.getExportIndi() == 1)
			{
				getchkExport().setSelected(true);
				getchkExport().setEnabled(false);
			}
			//end defect 11228
			
			if (SystemProperty.isCounty())
			{
				getchkVINInError().setEnabled(false);
				getchkTitleRevoked().setEnabled(false);
				getchkDeleteVehicleSoldDate().setEnabled(false);
				getstcLblStopRefer().setEnabled(false);
				gettxtLegalRestraintNo().setEnabled(false);
				getcomboSelectBonded().setEnabled(false);
				getstcLblVehicleSoldDate().setEnabled(false);
				gettxtVehicleSoldDate().setEnabled(false);
			}
			else if (caVehInqData.getNoMFRecs() == 0)
				// disable items not allowed when no record found
			{
				getstcLblRegisteredBy().setEnabled(false);
				gettxtRegPlateOwnerName().setEnabled(false);
				getchkMailReturn().setEnabled(false);
				getchkVINInError().setEnabled(false);
				getchkTitleRevoked().setEnabled(false);
				getchkDeleteVehicleSoldDate().setEnabled(false);
				getcomboSelectBonded().setEnabled(false);
				// defect 10830
				getstcLblColorMajor().setEnabled(false);
				getstcLblColorMinor().setEnabled(false);
				getcomboMajorColor().setEnabled(false);
				getcomboMinorColor().setEnabled(false);
				// end defect 10830
				getstcLblVehicleSoldDate().setEnabled(false);
				gettxtVehicleSoldDate().setEnabled(false);
				if (laSecurityData.getLegalRestrntNoAccs() == 0)
				{
					getstcLblStopRefer().setEnabled(false);
					gettxtLegalRestraintNo().setEnabled(false);
				}
			}
			else // disable items based on security access 
				{
				if (laSecurityData.getLegalRestrntNoAccs() == 0)
				{
					getstcLblStopRefer().setEnabled(false);
					gettxtLegalRestraintNo().setEnabled(false);
				}
				if (laSecurityData.getRgstrByAccs() == 0)
				{
					getstcLblRegisteredBy().setEnabled(false);
					gettxtRegPlateOwnerName().setEnabled(false);
				}
				if (laSecurityData.getMailRtrnAccs() == 0)
				{
					getchkMailReturn().setEnabled(false);
				}
				if (laSecurityData.getTtlRevkdAccs() == 0)
				{
					getchkTitleRevoked().setEnabled(false);
				}
				if (laSecurityData.getBndedTtlCdAccs() == 0)
				{
					getcomboSelectBonded().setEnabled(false);
				}
			}
			if (caVehInqData.getNoMFRecs() > 0)
			{
				if (laTtlData.getBndedTtlCd().equalsIgnoreCase(W))
				{
					getcomboSelectBonded().setEnabled(false);
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant
								.ERR_NUM_CANNOT_CHANGE_BONDED_TITLE);
					leRTSEx.displayError(this);
				}
				else if (laTtlData.getBndedTtlCd().equalsIgnoreCase(B))
				{
					getcomboSelectBonded().addItem(BONDED);
					getcomboSelectBonded().addItem(BONDEDWAITING);
					getcomboSelectBonded().addItem(SUSPENDED);
					getcomboSelectBonded().setSelectedIndex(0);
				}
				else if (laTtlData.getBndedTtlCd().equalsIgnoreCase(S))
				{
					getcomboSelectBonded().addItem(BONDED);
					getcomboSelectBonded().addItem(SUSPENDED);
					getcomboSelectBonded().setSelectedIndex(1);
				}
			}
			if (getcomboSelectBonded().getItemCount() < 1)
			{
				getcomboSelectBonded().setEnabled(false);
			}
		}
	}

	/**
	 * Sets the data from the screen to the vehicle object.
	 */
	private void setDataToVehObj() throws RTSException
	{
		RegistrationData laRegData =
			caVehInqData.getMfVehicleData().getRegData();
		VehicleData laVehData =
			caVehInqData.getMfVehicleData().getVehicleData();
		TitleData laTtlData =
			caVehInqData.getMfVehicleData().getTitleData();

		if (caVehInqData != null)
		{
			if (caVehInqData.getNoMFRecs() == 0)
			{
				laTtlData.setDocTypeCd(DocTypeConstant.LEGAL_RESTRAINT);

				// defect 9971				
				// Set to default ETtlCd per DocTypeCd 
				laTtlData.setETtlCd(
					DocumentTypesCache.getDefaultETtlCd(
						laTtlData.getDocTypeCd()));
				// end defect 9971
			}
		}

		// defect 10830
		if (laVehData != null)
		{
			// Set the Major Color
			String lsColorCd = CommonConstant.STR_SPACE_EMPTY;
						
			lsColorCd = getMajorColorCdSelected();
			if (lsColorCd == null)
			{
				lsColorCd = CommonConstant.STR_SPACE_EMPTY;
			}
			laVehData.setVehMjrColorCd(lsColorCd);
	
			// Set the Minor Color
			lsColorCd = getMinorColorCdSelected();
			if (lsColorCd == null)
			{
				lsColorCd = CommonConstant.STR_SPACE_EMPTY;
			}
			laVehData.setVehMnrColorCd(lsColorCd);
		}
		// end defect 10830

		if (laRegData != null)
		{
			String lsRegBy = gettxtRegPlateOwnerName().getText().trim();

			String lsLglResNo =
				gettxtLegalRestraintNo().getText().trim();

			String lsBondedTtlCd =
				(String) getcomboSelectBonded().getSelectedItem();

			int liVehSldDt = 0;

			if (gettxtVehicleSoldDate().getDate() != null)
			{
				liVehSldDt =
					gettxtVehicleSoldDate().getDate().getYYYYMMDDDate();
				laTtlData.setVehSoldDate(liVehSldDt);
			}
			else
			{
				laTtlData.setVehSoldDate(liVehSldDt);
			}
			if (gettxtRegPlateOwnerName().isEnabled())
			{
				laRegData.setRegPltOwnrName(lsRegBy);
			}
			if (gettxtLegalRestraintNo().isEnabled())
			{
				laTtlData.setLegalRestrntNo(lsLglResNo);
			}
			if (getchkMailReturn().isSelected())
			{
				laRegData.setRenwlMailRtrnIndi(1);
			}
			else
			{
				laRegData.setRenwlMailRtrnIndi(0);
			}
			if (getchkVINInError().isSelected())
			{
				laVehData.setVinErrIndi(1);
			}
			else
			{
				laVehData.setVinErrIndi(0);
			}
			if (getchkTitleRevoked().isSelected())
			{
				laTtlData.setTtlRevkdIndi(1);
			}
			else
			{
				laTtlData.setTtlRevkdIndi(0);
			}
			
			//defect 11228
			if (getchkExport().isSelected())
			{
				laTtlData.setExportIndi(1);
			}
			else
			{
				laTtlData.setExportIndi(0);
			}
			//end defect 11228
			
			if (lsBondedTtlCd != null)
			{
				if (lsBondedTtlCd.equals(BONDED))
				{
					laTtlData.setBndedTtlCd(B);
				}
				else if (lsBondedTtlCd.equals(BONDEDWAITING))
				{
					laTtlData.setBndedTtlCd(W);
				}
				else if (lsBondedTtlCd.equals(SUSPENDED))
				{
					laTtlData.setBndedTtlCd(S);
				}
			}
		}
		// defect 11004
//		if (!isDataModified())
//		{
//			throw new RTSException(
//				ErrorsConstant.ERR_NUM_DATA_REQD_TO_COMPLETE_TRANS); 
// 		}
		// end defect 11004
	}

	/** 
	 * Validate Data on Screen
	 *
	 * @return boolean
	 */
	private boolean validateData()
	{
		boolean lbValid = true;
		RTSException leRTSEx = new RTSException();
		
		// defect 11004 
		UtilityMethods.trimRTSInputField(this); 
		// end defect 11004 
		
		//defect 11228		
		if (gettxtLegalRestraintNo()
				.getText()
				.toUpperCase()
				.indexOf("EXPORT") != -1)
		{
			leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_ACTION_INVALID_EXPORT_LEGAL_RESTRAINT),
							gettxtLegalRestraintNo());
		}
		//defect 11228
		
		// Verify Vehicle Sold Date 
		if (gettxtVehicleSoldDate().isEnabled()
			&& !gettxtVehicleSoldDate().isDateEmpty()
			&& !gettxtVehicleSoldDate().isValidDate())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtVehicleSoldDate());
		}
		// Verify Legal Restraint No on No Record Found
		if (caVehInqData.getNoMFRecs() == 0
			&& UtilityMethods.isEmpty(gettxtLegalRestraintNo().getText()))
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtLegalRestraintNo());
		}

		// defect 10830
		if (getcomboMajorColor().isEnabled())
		{
			if (isMinorColorSelectedMajorColorNotSelected())
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					getcomboMajorColor());
			}
			else if (isMajorColorSameAsMinorColor())
			{
				leRTSEx.addException(
					new RTSException(160),
					getcomboMinorColor());
			}
			
		}
		// end defect 10830

		// defect 10347
		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvMFValid,
			leRTSEx);
		// end defect 10347

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		
		return lbValid;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="6,9"
