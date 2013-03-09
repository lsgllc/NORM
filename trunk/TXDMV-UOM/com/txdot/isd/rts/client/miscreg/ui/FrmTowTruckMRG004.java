package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com
	.txdot
	.isd
	.rts
	.client
	.miscreg
	.business
	.MiscellaneousRegClientUtilityMethods;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmTowTruckMRG004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		06/25/2001	Created
 * J Kwik		04/18/2002	Global change for startWorking() and 
 *							doneWorking()
 * K Harrell	12/31/2003	Cursor Movement Keys would not work w/in fields
 *							modify keyPressed()
 *							defect 6147. Ver 5.1.5 Fix 2
 * K Harrell	05/21/2004	Tow Truck should print US vs. TTS
 *							modify constant STICKER
 *							defect 7110  Ver 5.2.0
 * K Harrell	06/02/2004	Backout 7110; Reinstate TTS
 *							defect 7138   Ver 5.2.0 
 * B Hargrove	11/18/2004  Change errmsg process so that they beep:
 *							model year, state, zip, zip4
 *							add error messages to RTS_ERR_MSGS
 *							modify validateInputFields()
 *							defect 6823, 6869  Ver 5.2.2
 * K Harrell	01/14/2005	Clear Tow Truck Plate No and clear color 
 *							when deselect "Currently Registered".
 *							JavaDoc/Formatting/Variable Name Cleanup
 *							Assign trimmed screen input fields for
 *							 class objects.
 *							Trim Certificate No for validation.
 *							delete getTowTrkExpMo() - not used
 *								(final static variable!!) 
 *							modify input field properties via VC
 *							modify handleTowTrkPltNoField(),
 *							 setInputFields() 
 *							defects 7817,7883 Ver 5.2.3 
 * B Hargrove	03/18/2005	Modify code for move to Java 1.4.
 *							Bring code to standards.
 *  	 					Use new getters\setters for frame, 
 *							controller, etc.
 *  	 					Remove unused method.
 * 							modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel.
 *							defect 7893 Ver 5.2.3
 * B Hargrove	04/12/2005	Added JPanel to allow tabbing down left side
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
 * K Harrell	01/02/2006	Set indicator in TimedPermitData object
 * 							for currently registered.
 * 							modify setInputFields()
 * 							defect 6801 Ver 5.2.3 
 * T Pederson	09/28/2006	Changed call for converting vin i's and o's 
 * 							to 1's and 0's to combined non-static 
 * 							method convert_i_and_o_to_1_and_0(). 
 * 							delete getBuilderData()
 * 							modify focusLost() 
 * 							defect 8902 Ver Exempts
 * K Harrell	04/06/2007	CommonValidations.convert_i_and_o_to_1_and_0() 
 * 							call is now static
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/30/2009	Implement new OwnerData. Additional cleanup.
 * 							add ivjJPanel1, getJPanel1 
 * 							delete jPanel, getJPanel
 * 							delete getOwnrData(), setOwnrData(), 
 * 							 getRegData(), setRegData(), getAddressData(),
 * 							 setAddressData(), getCompleteTransData(), 
 * 							 setCompleteTransData(), setMFVehData(), 
 * 							 getVehData(), setVehData(), getTimedPrmtData(),
 * 							modify setData(), setInputFields(), 
 * 							 validateInputFields() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/19/2009	Implement new validation of Country 
 * 							field. Use ErrorsConstant. Use standard 
 * 							mechanism for field visibility.
 *							add caNameAddrComp
 *							delete TX 
 *							delete caRegData, caVehData, caOwnrData, 
 *							  caCompleteTransData, caAddressData
 *							delete ivjOwnerCntry, ivjOwnerCntryZip, 
 *							 get/set methods. 
 *							modify  itemStateChanged(), 
 *							 gettxtMake(),gettxtOwnerCity(), 
 *							 gettxtOwnerName1(),gettxtOwnerName2(),
 *							 gettxtOwnerState(),gettxtOwnerStreet1(),
 *							 gettxtOwnerStreet2(),gettxtOwnerZpcd(),
 *							 gettxtOwnerZpcdP4(),gettxtPlateNo(),
 *							 gettxtTonnage(),gettxtVIN(), 
 *							 gettxtStateRegistered(), 
 *							 getTowTruckPlateNo(),getJPanel6(),
 *							 handleTowTrkPltNoField(),
 *							 initialize(), itemStateChanged(), 
 *							 setInputFields(), validateFields() 
 * 							defect 10127 Ver Defect_POS_F
 * Min Wang		07/20/2010	Delete the existing Certificate No. input 
 * 							field and add check box Verified Tow Truck
 * 							Certificate on the screen.
 * 							add getchkTowTruckCert(),ivjchkTowTruckCert,
 * 							delete CERTIFICATE_NO, gettxtCertificateNo,
 * 							ivjstcLblCertificateNo,ivjtxtCertificateNo
 * 							modify setInputFields(), getJPanel1(), 
 * 							validateFields()
 * 							defect 10007 Ver 6.5.0
 * R Pilon		07/23/2012	Move the code deselecting the "Currently registered"
 * 							  check box to after the display of the error
 * 							  as this was causing two error messages to display.
 * 							  The migration to the 1.7 JRE will now cause the
 * 							  itemStateChanged(ItemEvent) method to fire twice
 * 							  due to the the deselecting of the check box.
 * 							modify handleTowTrkPltNoField()
 * 							defect 11417 Ver 7.0.0
 * ---------------------------------------------------------------------
 */
/**
* Tow truck class for miscellaneous registration.
*
* @version	7.0.0		 	07/23/2012
* @author	Joseph Kwik
* <br>Creation Date:		06/25/2001
*/
public class FrmTowTruckMRG004
	extends RTSDialogBox
	implements ActionListener, FocusListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkCurrentlyRegistered = null;
	// defect 10007
	private JCheckBox ivjchkTowTruckCert = null;
	// end defect 10007
	private JPanel ivjFrmTowTruckMRG004ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel6 = null;
	// defect 10007
	//private JLabel ivjstcLblCertificateNo = null;
	// end defect 10007
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblOwnerAddress = null;
	private JLabel ivjstcLblOwnerName = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblStateRegistered = null;
	private JLabel ivjstcLblTowTruckPlateNo = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblYearMake = null;
	// defect 10007
	//private RTSInputField ivjtxtCertificateNo = null;
	// end defect 10007
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
	private RTSInputField ivjtxtTowTruckPlateNo = null;
	private RTSInputField ivjtxtVIN = null;
	private RTSInputField ivjtxtYear = null;

	// Objects
	private MFVehicleData caMFVehData = new MFVehicleData();
	// defect 10127 
	private NameAddressComponent caNameAddrComp = null;
	// end defect 10127 
	private TimedPermitData caTimedPrmtData = new TimedPermitData();

	// Constants
	// defect 10007
	//private final static String CERTIFICATE_NO = "Certificate No:";
	// end defect 10007
	private final static String CURR_REG = "Currently registered";
	private final static String DASH = "-";
	private final static String ERRMSG_DATA_MISSING =
		"Data missing for IsNextVCREG029";
	private final static String ERRMSG_ERROR = "ERROR";
	private final static String OWNR_ADDR = "Owner Address:";
	private final static String OWNR_NAME = "Owner Name:";
	private final static String PLATE_NO = "Plate No:";
	private final static String STATE_REG = "State Registered:";
	private final static String STICKER = "TTS";
	private final static String TITLE_MRG004 =
		"Tow Truck          MRG004";
	private final static int TOW_TRK_EXP_MO = 1;
	private final static String TOW_TRUCK_PLATE_NO =
		"Tow Truck Plate No:";
	private final static int TWELVE_MONTHS = 12;
	private final static int TWO_MONTH_WINDOW = 2;
	private final static String VIN = "VIN:";
	private final static String YEAR_MAKE = "Year/Make:";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmTowTruckMRG004 aaFrmTowTruckMRG004;
			aaFrmTowTruckMRG004 = new FrmTowTruckMRG004();
			aaFrmTowTruckMRG004.setModal(true);
			aaFrmTowTruckMRG004
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			aaFrmTowTruckMRG004.show();
			java.awt.Insets insets = aaFrmTowTruckMRG004.getInsets();
			aaFrmTowTruckMRG004.setSize(
				aaFrmTowTruckMRG004.getWidth()
					+ insets.left
					+ insets.right,
				aaFrmTowTruckMRG004.getHeight()
					+ insets.top
					+ insets.bottom);
			aaFrmTowTruckMRG004.setVisibleRTS(true);
		}
		catch (Throwable aeRTSEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeRTSEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmTowTruckMRG004 constructor comment.
	 */
	public FrmTowTruckMRG004()
	{
		super();
		initialize();
	}

	/**
	 * FrmTowTruckMRG004 constructor with parent.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmTowTruckMRG004(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmTowTruckMRG004 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmTowTruckMRG004(JFrame aaParent)
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
				RTSHelp.displayHelp(RTSHelp.MRG004);
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
	public void focusGained(FocusEvent aaFE)
	{
		// empty code block
	}

	/**
	 * Focus Lost
	 * 
	 * @param aaFE FocusEvent
	 */
	public void focusLost(FocusEvent aaFE)
	{
		if (!aaFE.isTemporary() && aaFE.getSource() == gettxtVIN())
		{
			String lsVin = gettxtVIN().getText().trim().toUpperCase();
			// defect 8902
			lsVin = CommonValidations.convert_i_and_o_to_1_and_0(lsVin);
			// end defect 8902
			gettxtVIN().setText(lsVin);
			return;
		}
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
				ivjButtonPanel1.setBounds(112, 242, 308, 42);
				ivjButtonPanel1.setName("ButtonPanel1");
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
	 * Return the ivjchkCurrentlyRegistered property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkCurrentlyRegistered()
	{
		if (ivjchkCurrentlyRegistered == null)
		{
			try
			{
				ivjchkCurrentlyRegistered = new JCheckBox();
				ivjchkCurrentlyRegistered.setBounds(48, 160, 140, 22);
				ivjchkCurrentlyRegistered.setName(
					"ivjchkCurrentlyRegistered");
				ivjchkCurrentlyRegistered.setMnemonic(71);
				ivjchkCurrentlyRegistered.setText(CURR_REG);
				ivjchkCurrentlyRegistered.setMaximumSize(
					new java.awt.Dimension(139, 22));
				ivjchkCurrentlyRegistered.setActionCommand(CURR_REG);
				ivjchkCurrentlyRegistered.setMinimumSize(
					new java.awt.Dimension(139, 22));
				// user code begin {1}
				ivjchkCurrentlyRegistered.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjchkCurrentlyRegistered;
	}

	/**
	 * Return the ivjFrmTowTruckMRG004ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmTowTruckMRG004ContentPane1()
	{
		if (ivjFrmTowTruckMRG004ContentPane1 == null)
		{
			try
			{
				ivjFrmTowTruckMRG004ContentPane1 = new JPanel();
				ivjFrmTowTruckMRG004ContentPane1.setName(
					"ivjFrmTowTruckMRG004ContentPane1");
				ivjFrmTowTruckMRG004ContentPane1.setLayout(null);
				ivjFrmTowTruckMRG004ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmTowTruckMRG004ContentPane1.setMinimumSize(
					new java.awt.Dimension(545, 332));
				ivjFrmTowTruckMRG004ContentPane1.setBounds(0, 0, 0, 0);

				ivjFrmTowTruckMRG004ContentPane1.add(
					gettxtOwnerName1(),
					null);
				ivjFrmTowTruckMRG004ContentPane1.add(
					gettxtOwnerName2(),
					null);
				ivjFrmTowTruckMRG004ContentPane1.add(
					gettxtOwnerStreet1(),
					null);
				ivjFrmTowTruckMRG004ContentPane1.add(
					gettxtOwnerStreet2(),
					null);
				ivjFrmTowTruckMRG004ContentPane1.add(
					getstcLblOwnerName(),
					null);
				ivjFrmTowTruckMRG004ContentPane1.add(
					getstcLblOwnerAddress(),
					null);
				ivjFrmTowTruckMRG004ContentPane1.add(
					getJPanel6(),
					null);
				ivjFrmTowTruckMRG004ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmTowTruckMRG004ContentPane1.add(
					getJPanel1(),
					null);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjFrmTowTruckMRG004ContentPane1;
	}
	/**
	 * This method initializes ivjJPanel1
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			ivjJPanel1 = new JPanel();
			ivjJPanel1.setLayout(null);
			ivjJPanel1.add(getstcLblPlateNo(), null);
			ivjJPanel1.add(getstcLblStateRegistered(), null);
			ivjJPanel1.add(getstcLblYearMake(), null);
			ivjJPanel1.add(getstcLblVIN(), null);
			// defect 10007
			//ivjJPanel1.add(getstcLblCertificateNo(), null);
			// end defect 10007
			ivjJPanel1.add(gettxtPlateNo(), null);
			ivjJPanel1.add(gettxtStateRegistered(), null);
			ivjJPanel1.add(gettxtYear(), null);
			ivjJPanel1.add(gettxtMake(), null);
			ivjJPanel1.add(gettxtVIN(), null);
			// defect 10007
			//ivjJPanel1.add(gettxtCertificateNo(), null);
			// end defect 10007
			ivjJPanel1.add(getchkCurrentlyRegistered(), null);
			ivjJPanel1.add(getstcLblTowTruckPlateNo(), null);
			ivjJPanel1.add(gettxtTowTruckPlateNo(), null);
			ivjJPanel1.add(getchkTowTruckCert(), null);
			ivjJPanel1.setBounds(4, 13, 255, 224);
		}
		return ivjJPanel1;
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
				ivjJPanel6.setLayout(new java.awt.GridBagLayout());
				ivjJPanel6.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel6.setMinimumSize(
					new java.awt.Dimension(258, 24));

				java.awt.GridBagConstraints constraintstxtOwnerCity =
					new java.awt.GridBagConstraints();
				constraintstxtOwnerCity.gridx = 2;
				constraintstxtOwnerCity.gridy = 2;
				constraintstxtOwnerCity.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtOwnerCity.weightx = 1.0;
				constraintstxtOwnerCity.ipadx = 137;
				constraintstxtOwnerCity.insets =
					new java.awt.Insets(9, 4, 9, 3);
				getJPanel6().add(
					gettxtOwnerCity(),
					constraintstxtOwnerCity);

				java.awt.GridBagConstraints constraintstxtOwnerState =
					new java.awt.GridBagConstraints();
				constraintstxtOwnerState.gridx = 3;
				constraintstxtOwnerState.gridy = 2;
				constraintstxtOwnerState.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtOwnerState.weightx = 1.0;
				constraintstxtOwnerState.ipadx = 20;
				constraintstxtOwnerState.insets =
					new java.awt.Insets(9, 3, 9, 3);
				getJPanel6().add(
					gettxtOwnerState(),
					constraintstxtOwnerState);

				java.awt.GridBagConstraints constraintstxtOwnerZip =
					new java.awt.GridBagConstraints();
				constraintstxtOwnerZip.gridx = 4;
				constraintstxtOwnerZip.gridy = 2;
				constraintstxtOwnerZip.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtOwnerZip.weightx = 1.0;
				constraintstxtOwnerZip.ipadx = 42;
				constraintstxtOwnerZip.insets =
					new java.awt.Insets(9, 3, 9, 2);
				getJPanel6().add(
					gettxtOwnerZpcd(),
					constraintstxtOwnerZip);

				java.awt.GridBagConstraints constraintsstcLblDash1 =
					new java.awt.GridBagConstraints();
				constraintsstcLblDash1.gridx = 5;
				constraintsstcLblDash1.gridy = 2;
				constraintsstcLblDash1.ipadx = 4;
				constraintsstcLblDash1.insets =
					new java.awt.Insets(9, 2, 15, 0);
				getJPanel6().add(
					getstcLblDash(),
					constraintsstcLblDash1);

				java.awt.GridBagConstraints constraintstxtOwnerZipP4 =
					new java.awt.GridBagConstraints();
				constraintstxtOwnerZipP4.gridx = 6;
				constraintstxtOwnerZipP4.gridy = 2;
				constraintstxtOwnerZipP4.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtOwnerZipP4.weightx = 1.0;
				constraintstxtOwnerZipP4.ipadx = 38;
				constraintstxtOwnerZipP4.insets =
					new java.awt.Insets(9, 0, 9, 10);
				getJPanel6().add(
					gettxtOwnerZpcdP4(),
					constraintstxtOwnerZipP4);
				// defect 10127 
				//		java.awt.GridBagConstraints constraintstxtOwnerCntry =
				//			new java.awt.GridBagConstraints();
				//		constraintstxtOwnerCntry.gridx = 2;
				//		constraintstxtOwnerCntry.gridy = 2;
				//		constraintstxtOwnerCntry.gridwidth = -1;
				//		constraintstxtOwnerCntry.gridheight = -1;
				//		constraintstxtOwnerCntry.fill =
				//			java.awt.GridBagConstraints.HORIZONTAL;
				//		constraintstxtOwnerCntry.weightx = 1.0;
				//		constraintstxtOwnerCntry.ipadx = -4;
				//		constraintstxtOwnerCntry.ipady = -20;
				//		getJPanel6().add(
				//			gettxtOwnerCntry(),
				//			constraintstxtOwnerCntry);
				//
				//		java.awt.GridBagConstraints constraintstxtOwnerCntryZip =
				//			new java.awt.GridBagConstraints();
				//		constraintstxtOwnerCntryZip.gridx = 2;
				//		constraintstxtOwnerCntryZip.gridy = 2;
				//		constraintstxtOwnerCntryZip.gridwidth = -1;
				//		constraintstxtOwnerCntryZip.gridheight = -1;
				//		constraintstxtOwnerCntryZip.fill =
				//			java.awt.GridBagConstraints.HORIZONTAL;
				//		constraintstxtOwnerCntryZip.weightx = 1.0;
				//		constraintstxtOwnerCntryZip.ipadx = -4;
				//		constraintstxtOwnerCntryZip.ipady = -20;
				//		getJPanel6().add(
				//			gettxtOwnerCntryZpcd(),
				//			constraintstxtOwnerCntryZip);
				// end defect 10127 
				// user code begin {1}
				ivjJPanel6.setBounds(263, 200, 278, 25);
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
	 * Return the ivjstcLblCertificateNo property value.
	 * 
	 * @return JLabel
	 */
	//	private JLabel getstcLblCertificateNo()
	//	{
	//		if (ivjstcLblCertificateNo == null)
	//		{
	//			try
	//			{
	//				ivjstcLblCertificateNo = new JLabel();
	//				ivjstcLblCertificateNo.setBounds(23, 133, 86, 14);
	//				ivjstcLblCertificateNo.setName(
	//					"ivjstcLblCertificateNo");
	//				ivjstcLblCertificateNo.setText(CERTIFICATE_NO);
	//				ivjstcLblCertificateNo.setMaximumSize(
	//					new java.awt.Dimension(80, 14));
	//				ivjstcLblCertificateNo.setMinimumSize(
	//					new java.awt.Dimension(80, 14));
	//				// user code begin {1}
	//				// user code end
	//			}
	//			catch (Throwable aeIvjEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIvjEx);
	//			}
	//		}
	//		return ivjstcLblCertificateNo;
	//	}

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
	 * Return the ivjstcLblOwnerAddress property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOwnerAddress()
	{
		if (ivjstcLblOwnerAddress == null)
		{
			try
			{
				ivjstcLblOwnerAddress = new JLabel();
				ivjstcLblOwnerAddress.setBounds(266, 120, 178, 14);
				ivjstcLblOwnerAddress.setName("ivjstcLblOwner");
				ivjstcLblOwnerAddress.setText(OWNR_ADDR);
				ivjstcLblOwnerAddress.setMaximumSize(
					new java.awt.Dimension(92, 14));
				ivjstcLblOwnerAddress.setMinimumSize(
					new java.awt.Dimension(92, 14));
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
	 * Return the ivjstcLblOwnerName property value.
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
				ivjstcLblOwnerName.setBounds(266, 34, 183, 14);
				ivjstcLblOwnerName.setName("ivjstcLblOwnerName");
				ivjstcLblOwnerName.setText(OWNR_NAME);
				ivjstcLblOwnerName.setMaximumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblOwnerName.setMinimumSize(
					new java.awt.Dimension(77, 14));
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
	 * Return the ivjstcLblPlateNo property value.
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
				ivjstcLblPlateNo.setBounds(54, 21, 52, 14);
				ivjstcLblPlateNo.setName("ivjstcLblPlateNo");
				ivjstcLblPlateNo.setText(PLATE_NO);
				ivjstcLblPlateNo.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblPlateNo.setMinimumSize(
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
		return ivjstcLblPlateNo;
	}

	/**
	 * Return the ivjstcLblStateRegistered property value.
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
				ivjstcLblStateRegistered.setBounds(5, 45, 101, 14);
				ivjstcLblStateRegistered.setName(
					"ivjstcLblStateRegistered");
				ivjstcLblStateRegistered.setText(STATE_REG);
				ivjstcLblStateRegistered.setMaximumSize(
					new java.awt.Dimension(98, 14));
				ivjstcLblStateRegistered.setMinimumSize(
					new java.awt.Dimension(98, 14));
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
	 * Return the ivjstcLblTowTruckPlateNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblTowTruckPlateNo()
	{
		if (ivjstcLblTowTruckPlateNo == null)
		{
			try
			{
				ivjstcLblTowTruckPlateNo = new JLabel();
				ivjstcLblTowTruckPlateNo.setBounds(5, 190, 124, 21);
				ivjstcLblTowTruckPlateNo.setName(
					"ivjstcLblTowTruckPlateNo");
				ivjstcLblTowTruckPlateNo.setText(TOW_TRUCK_PLATE_NO);
				ivjstcLblTowTruckPlateNo.setMaximumSize(
					new java.awt.Dimension(113, 14));
				ivjstcLblTowTruckPlateNo.setMinimumSize(
					new java.awt.Dimension(113, 14));
				ivjstcLblTowTruckPlateNo.setEnabled(true);
				ivjstcLblTowTruckPlateNo.setHorizontalAlignment(
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
		return ivjstcLblTowTruckPlateNo;
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
				ivjstcLblVIN.setBounds(22, 107, 24, 14);
				ivjstcLblVIN.setName("ivjstcLblVIN");
				ivjstcLblVIN.setText(VIN);
				ivjstcLblVIN.setMaximumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVIN.setMinimumSize(
					new java.awt.Dimension(22, 14));
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
				ivjstcLblYearMake.setBounds(38, 74, 68, 14);
				ivjstcLblYearMake.setName("ivjstcLblYearMake");
				ivjstcLblYearMake.setText(YEAR_MAKE);
				ivjstcLblYearMake.setMaximumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYearMake.setMinimumSize(
					new java.awt.Dimension(63, 14));
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
	 * Return the ivjtxtCertificateNo property value.
	 * 
	 * @return RTSInputField
	 */
	//	private RTSInputField gettxtCertificateNo()
	//	{
	//		if (ivjtxtCertificateNo == null)
	//		{
	//			try
	//			{
	//				ivjtxtCertificateNo = new RTSInputField();
	//				ivjtxtCertificateNo.setBounds(127, 130, 120, 20);
	//				ivjtxtCertificateNo.setName("ivjtxtCertificateNo");
	//				ivjtxtCertificateNo.setHighlighter(
	//					new javax
	//						.swing
	//						.plaf
	//						.basic
	//						.BasicTextUI
	//						.BasicHighlighter());
	//				ivjtxtCertificateNo.setMaxLength(6);
	//				// defect 10127 
	//				ivjtxtCertificateNo.setInput(RTSInputField.DEFAULT);
	//				// end defect 10127		
	//				// user code begin {1}
	//				// user code end
	//			}
	//			catch (Throwable aeIvjEx)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIvjEx);
	//			}
	//		}
	//		return ivjtxtCertificateNo;
	//	}

	/**
	 * Return the ivjtxtMake property value.
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
				ivjtxtMake.setBounds(201, 71, 47, 20);
				ivjtxtMake.setName("ivjtxtMake");
				ivjtxtMake.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

				// defect 10127 		
				ivjtxtMake.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtMake.setMaxLength(CommonConstant.LENGTH_MAKE);
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
		return ivjtxtMake;
	}

	/**
	 * Return the ivjtxtOwnerCity property value.
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
				ivjtxtOwnerCity.setName("ivjtxtOwnerCity");
				ivjtxtOwnerCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCity.setText("");

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
				ivjtxtOwnerName1.setBounds(266, 55, 272, 20);
				ivjtxtOwnerName1.setName("ivjtxtOwnerName1");
				ivjtxtOwnerName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

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
				ivjtxtOwnerName2.setBounds(266, 84, 272, 20);
				ivjtxtOwnerName2.setName("ivjtxtOwnerName2");
				ivjtxtOwnerName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

				// defect 10127 
				ivjtxtOwnerName2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName2.setMaxLength(
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
		return ivjtxtOwnerName2;
	}

	/**
	 * Return the ivjtxtOwnerState property value.
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
				ivjtxtOwnerState.setName("ivjtxtOwnerState");
				ivjtxtOwnerState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

				// defect 10127
				ivjtxtOwnerState.setText(CommonConstant.STR_TX);
				ivjtxtOwnerState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtOwnerState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// end defect 10127 

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
				ivjtxtOwnerStreet1.setBounds(266, 143, 272, 20);
				ivjtxtOwnerStreet1.setName("ivjtxtOwnerStreet1");
				ivjtxtOwnerStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

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
				ivjtxtOwnerStreet2.setBounds(266, 174, 272, 20);
				ivjtxtOwnerStreet2.setName("ivjtxtOwnerStreet2");
				ivjtxtOwnerStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

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
				ivjtxtOwnerZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

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
				ivjtxtOwnerZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

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
	 * Return the ivjtxtPlateNo property value.
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
				ivjtxtPlateNo.setBounds(111, 18, 119, 20);
				ivjtxtPlateNo.setName("ivjtxtPlateNo");
				ivjtxtPlateNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

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
	 * Return the ivjtxtStateRegistered property value.
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
				ivjtxtStateRegistered.setBounds(111, 42, 49, 20);
				ivjtxtStateRegistered.setName("ivjtxtStateRegistered");
				ivjtxtStateRegistered.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

				// defect 10127 
				ivjtxtStateRegistered.setText(CommonConstant.STR_TX);
				ivjtxtStateRegistered.setInput(
					RTSInputField.ALPHA_ONLY);
				ivjtxtStateRegistered.setMaxLength(
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
		return ivjtxtStateRegistered;
	}

	/**
	 * Return the ivjtxtTowTruckPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtTowTruckPlateNo()
	{
		if (ivjtxtTowTruckPlateNo == null)
		{
			try
			{
				ivjtxtTowTruckPlateNo = new RTSInputField();
				ivjtxtTowTruckPlateNo.setBounds(136, 190, 114, 21);
				ivjtxtTowTruckPlateNo.setName("ivjtxtTowTruckPlateNo");
				ivjtxtTowTruckPlateNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtTowTruckPlateNo.setEnabled(true);

				// defect 10127
				ivjtxtTowTruckPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtTowTruckPlateNo.setMaxLength(
					CommonConstant.LENGTH_PLTNO);
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
		return ivjtxtTowTruckPlateNo;
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
				ivjtxtVIN.setBounds(51, 104, 196, 20);
				ivjtxtVIN.setName("ivjtxtVIN");
				ivjtxtVIN.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVIN.setMaxLength(22);

				// defect 10127 
				ivjtxtVIN.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				// end defect 10127 		

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
	 * Return the ivjtxtYear property value.
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
				ivjtxtYear.setBounds(111, 71, 79, 20);
				ivjtxtYear.setName("ivjtxtYear");
				ivjtxtYear.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());

				// defect 10127 		
				ivjtxtYear.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtYear.setMaxLength(CommonConstant.LENGTH_YEAR);
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
	 * Manage enable/disable tow truck plate number field when 
	 * "Currently Registered" is check/unchecked.
	 *
	 */
	private void handleTowTrkPltNoField()
	{
		// defect 10127 
		if (getchkCurrentlyRegistered().isSelected())
		{
			// end defect 10127 

			int liCurrentMonth = RTSDate.getCurrentDate().getMonth();

			int liMonthDifference = 0;

			// Compute difference between Tow Truck renewal month and 
			// current month
			if (TOW_TRK_EXP_MO >= liCurrentMonth)
			{
				liMonthDifference = TOW_TRK_EXP_MO - liCurrentMonth;
			}
			else
			{
				liMonthDifference =
					TOW_TRK_EXP_MO + TWELVE_MONTHS - liCurrentMonth;
			}

			// Check renewal window for tow truck sticker
			if (liMonthDifference >= TWO_MONTH_WINDOW)
			{
				// defect 11417
//				getchkCurrentlyRegistered().setSelected(false);
				// end defect 11417

				// defect 10112 
				// 50

				new RTSException(
					ErrorsConstant
						.ERR_NUM_NO_REG_IN_ADVANCE)
						.displayError(
					this);
				// end defect 10112 

				// defect 11417
				getchkCurrentlyRegistered().setSelected(false);
				// end defect 11417

				getchkCurrentlyRegistered().requestFocus();
			}
			else
			{
				getstcLblTowTruckPlateNo().setEnabled(true);
				gettxtTowTruckPlateNo().setEnabled(true);
			}
		}
		else
		{
			getstcLblTowTruckPlateNo().setEnabled(false);
			// defect 10127  
			clearAllColor(gettxtTowTruckPlateNo());
			// end defect 10127 
			gettxtTowTruckPlateNo().setEnabled(false);
			gettxtTowTruckPlateNo().setText("");
		}
	}

	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("FrmTowTruckMRG004");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(560, 318);
			setTitle(TITLE_MRG004);
			setContentPane(getFrmTowTruckMRG004ContentPane1());
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		//		defect 10127 
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
				getstcLblDash(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
				ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
				CommonConstant.TX_DEFAULT_STATE);
		// end defect 10127 		

		// user code begin {2}
		// user code end
	}

	/**
	 * Item State Changed
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		if (aaIE.getSource().equals(getchkCurrentlyRegistered()))
		{
			// defect 10127 
			handleTowTrkPltNoField();
			// end defect 10127 
		}
	}

	/**
	 * All subclasses must implement this method.
	 * It sets the data on the screen and is how the controller relays 
	 * information to the view
	 *
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject instanceof Vector)
		{
			Vector lvIsNextVCREG029 = (Vector) aaDataObject;
			if (lvIsNextVCREG029 != null)
			{
				if (lvIsNextVCREG029.size() == 2)
				{
					// determine next vc if NOT reg029
					if (lvIsNextVCREG029.get(0) instanceof Boolean)
						// first element is flag whether to go to REG029
					{
						getController().processData(
							VCTowTruckMRG004.REDIRECT_IS_NEXT_VC_REG029,
							lvIsNextVCREG029);
					}
					else if (lvIsNextVCREG029.get(0) instanceof String)
					{
						getController().processData(
							VCTowTruckMRG004.REDIRECT_NEXT_VC,
							lvIsNextVCREG029);
					}
				}
				else
				{
					new RTSException(
						RTSException.FAILURE_MESSAGE,
						ERRMSG_DATA_MISSING,
						ERRMSG_ERROR).displayError(
						this);
				}
				return;
			}
		}
		getstcLblTowTruckPlateNo().setEnabled(false);
		gettxtTowTruckPlateNo().setEnabled(false);
		gettxtStateRegistered().setText(CommonConstant.STR_TX);
		gettxtOwnerState().setText(CommonConstant.STR_TX);

		// defect 10127 
		// removed initialization of caTimedPermit. Now in 
		// setInputFields()
		// end defect 10127  
	}

	/**
	 * Set input field values to data object.
	 */
	private void setInputFields()
	{
		// defect 10127		
		RegistrationData laRegData = new RegistrationData();
		VehicleData laVehData = new VehicleData();
		OwnerData laOwnerData = new OwnerData();

		caNameAddrComp.setNameAddressToDataObject(laOwnerData);

		// TIMED PERMIT
		caTimedPrmtData.setTimedPrmtType(TransCdConstant.TOWP);
		caTimedPrmtData.setItmCd(STICKER);
		caTimedPrmtData.setVehRegState(
			gettxtStateRegistered().getText());
		// defect 10007
		//caTimedPrmtData.setDlsCertNo(
		//	gettxtCertificateNo().getText().trim());
		caTimedPrmtData.setDlsCertNo("");
		// end defect 10007
		caTimedPrmtData.setTowTrkPltNo(
			gettxtTowTruckPlateNo().getText());
		caTimedPrmtData.setRegistered(
			getchkCurrentlyRegistered().isSelected());

		// REG DATA 
		laRegData.setRegPltNo(gettxtPlateNo().getText());

		// VEH DATA 
		laVehData.setVehModlYr(
			Integer.parseInt(gettxtYear().getText()));
		laVehData.setVehMk(gettxtMake().getText());
		laVehData.setVin(gettxtVIN().getText());

		// MFVEHICLE DATA 
		caMFVehData.setRegData(laRegData);
		caMFVehData.setVehicleData(laVehData);
		caMFVehData.setOwnerData(laOwnerData);
		// end defect 10127
	}

	/**
	 * Validates fields.
	 * 
	 * @return boolean
	 */
	private boolean validateFields()
	{
		RTSException leRTSEx = new RTSException();

		// defect 10127
		boolean lbValid = true;

		// Plate No  
		if (gettxtPlateNo().isEmpty())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPlateNo());
		}

		// State Registered 
		if (gettxtStateRegistered().isEmpty())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtStateRegistered());
		}
		else if (!gettxtStateRegistered().isValidState())
		{
			// defect 6823
			// 150 => 2004 
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_INVALID_STATE_REENTER),
				gettxtStateRegistered());
			// end defect 6823
		}

		// Year
		if (gettxtYear().isEmpty()
			|| CommonValidations.isInvalidYearModel(
				gettxtYear().getText()))
		{
			// defect 6869
			// 2006
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_MODEL_YEAR_NOT_VALID),
				gettxtYear());
			// end defect 6869
		}

		// Make 
		if (gettxtMake().isEmpty())
		{
			// 150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtMake());
		}

		// VIN 
		if (gettxtVIN().isEmpty())
		{
			// 150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtVIN());
		}
		// defect 7817
		// defect 10007
		// Certificate No 
		//		if (gettxtCertificateNo().isEmpty())
		//		{
		//			// 150
		//			leRTSEx.addException(
		//				new RTSException(
		//					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
		//				gettxtCertificateNo());
		//		}
		// end defect 7817
		// end defect 10007
		// Currently Registered & Tow Truck Plate No 
		if (getchkCurrentlyRegistered().isSelected()
			&& gettxtTowTruckPlateNo().isEmpty())
		{
			// 150
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtTowTruckPlateNo());
		}

		// defect 10007
		if (getchkTowTruckCert().isEnabled()
			&& !getchkTowTruckCert().isSelected())
		{
			leRTSEx.addException(
				new RTSException(991),
				getchkTowTruckCert());

		}
		// end defect 10007	

		caNameAddrComp.validateNameAddressFields(leRTSEx);

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}

		return lbValid;
		// end defect 10127 

	}
	/**
	 * This method initializes ivjchkTowTruckCert
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkTowTruckCert()
	{
		if (ivjchkTowTruckCert == null)
		{
			try
			{
				ivjchkTowTruckCert = new javax.swing.JCheckBox();
				ivjchkTowTruckCert.setBounds(48, 133, 197, 21);
				ivjchkTowTruckCert.setName("ivjchkTowTruckCert");
				ivjchkTowTruckCert.setMnemonic(
					java.awt.event.KeyEvent.VK_V);
				ivjchkTowTruckCert.setText(
					"Verified Tow Truck Certificate");
			}
			catch (Throwable aeIvjEx)
			{
				handleException(aeIvjEx);
			}
		}
		return ivjchkTowTruckCert;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
