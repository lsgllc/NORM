package com.txdot.isd.rts.client.registration.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.registration.business.RegistrationClientBusiness;
import com.txdot.isd.rts.client.registration.business.RegistrationClientUtilityMethods;

import com.txdot.isd.rts.services.cache.OrganizationNumberCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.cache.RegistrationPlateStickerCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.RegistrationConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.SpecialPlatesConstant;

/*
 *
 * FrmReplacementChoicesREG016.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for 
 * S Govindappa				startWorking() and doneWorking()
 * Kathy        10/08/2002  Fixing 4818, 4819, 4824,4825 and 4835. Made 
 *                          changes to setData() to disable the 
 *                          PlateSticker choicebox when both plate and 
 * 							sticker cannot be replaced for a vehicle.
 *                          Changed actionPerformed() to restore the 
 * 							registration plate code in the begining of 
 *						    the method.				
 * B Hargrove	03/10/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							defect 7894 Ver 5.2.3
 * B Hargrove	03/31/2005	Comment out setNextFocusableComponent() 
 *							modify getradioPlate(),
 *							getradioPlateSticker(), getradioSticker(),
 *							keyPressed()
 *							defect 7894 Ver 5.2.3
 * B Hargrove	04/28/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							add import com.txdot.isd.rts.services.util.
 * 							UtilityMethods;
 * 							(fix merged in from VAJ)
 *  						modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * K Harrell	06/17/2004 	Corrected display of "Plate & Sticker" via
 * 							Visual Editor  
 * 							ClassToPlate, PlateToSticker Implementation
 * 							deleted DISABLED_PERSON_PLT,
 * 							DISABLED_MOTORCYCLE_PLT
 * 							deprecate completeTrans()
 * 							modify handleStkrTypes,setData()
 * 							defect 8218 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3                   
 * B Hargrove	06/21/2005	modify keyPressed()  (arrow key handling is
 * 							done in ButtonPanel).
 * 							Remove unused method.
 * 							delete implements KeyListener
 *							defect 7894 Ver 5.2.3
 * B Hargrove	06/27/2005	Refactor\Move 
 * 							RegistrationClientUtilityMethods class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.client.reg.business.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	07/19/2005	Refactor\Move
 * 							RegistrationValidationData class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.services.data.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/31/2005	Comment out handling mnemonics in keyPressed
 * 							modify keyPressed()
 * 							defect 7894 Ver 5.2.3
 * K Harrell	12/14/2005	Arrow Keys should not select
 * 							delete getSelctdRadioButton(), 
 * 							setSelctdRadioButton(),getCarrRadioButton(),
 * 							setCarrRadioButton() 
 * 							modify keyPressed() 
 * 							renamed ciSelctdRadioButton to
 * 							ciRadioButtonWithFocus 
 * 							defect 7894 Ver 5.2.3 
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.  Set RequestFocus to
 * 							false so that when the hot keys are chossen
 * 							on a radio field the focus does not stay on 
 * 							that radio button.
 * 							remove keyPressed(), ciRadioButtonWithFocus,
 * 								carrRadioButton
 * 							modify getJPanel1(), initialize()
 * 							defect 7894 Ver 5.2.3
 * B Hargrove	02/23/2007	Modify to handle new plate option for 
 * 							Special Plate. If customer has special plate,
 * 							they can replace with either special plate
 * 							or regular plate.
 * 							add handlePlateTypes()
 * 							modify actionPerformed()
 * 							defect 9126 Ver Special Plates
 * K Harrell	03/04/2007	Continued work on Special Plates
 * 							add setReplPltOptions()
 * 							modify handlePlateTypes(),actionPerformed(),
 * 							 handleStkrTypes()
 * 							defect 9085 Ver Special Plates 
 * K Harrell	09/10/2007	Prevent the remanufacture of Sunsetted Plates
 * 							add cbSpclPltSunset, ERRORNO_SUNSET
 * 							modify handlePltTypes(), setReplPltOptions()
 * 							defect 9288 Ver Special Plates   
 * B Hargrove	07/11/2008	Do not allow Plate replacement if Vendor Plate 
 * 							is expired (Plate Only nor Plate and Sticker).
 * 							Adjust enabling of Sticker Only. If Vendor 
 * 							Plate is expired but regis is current, 
 * 							Sticker Only will be enabled.  
 * 							Note:
 * 							RegistrationVerify.verifyValidSpclPlt() will
 * 							not display this frame if both Vendor Plate
 * 							plate and registration are expired.
 * 							modify setData()
 * 							defect 9689 Ver Defect MyPlates_POS
 * K Harrell	01/07/2009 	Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify setData()  
 *        					defect 9864 Ver Defect_POS_D 
 * B Hargrove	10/11/2010	For Token Trailer no longer prints sticker.
 * 							Disable Sticker and Plate / Sticker.
 * 							modify setData()
 * 							defect 10623 Ver Rel 6.6.0
 * ---------------------------------------------------------------------
 */
/**
 * This form allows the user to specify the type of inventory to be 
 * replaced in the replacement event, which can be one of the following:
 * 1) Plate & Sticker, 2) Sticker or 3) Plate.
 *
 * @version	6.6.0			10/29/2010 
 * @author	Joseph Kwik
 * <br>Creation Date:		06/26/2001 15:14:17
 */

public class FrmReplacementChoicesREG016
	extends RTSDialogBox
	implements ActionListener
{
	private JPanel ivjFrmReplacementChoicesREG016ContentPane1 = null;
	private JRadioButton ivjradioPlate = null;
	private JRadioButton ivjradioPlateSticker = null;
	private JRadioButton ivjradioSticker = null;
	private JPanel ivjJPanel1 = null;
	private ButtonPanel ivjbuttonPanel = null;

	// Objects 
	private CompleteTransactionData caCompTransData = null;
	private RegistrationValidationData caRegValidData = null;
	private VehicleInquiryData caVehicleInquiryData = null;

	// Replacement values to handle changes
	private VehicleInquiryData caReplVehicleInquiryData = null;
	private SpecialPlatesRegisData caReplSpclPltRegisData = null;
	private RegistrationValidationData caReplRegValidData = null;
	private RegistrationData caReplRegisData = null;

	// defect 9288 
	// boolean
	private boolean cbSpclPltSunset = false;
	// end defect 9288 

	// String 
	private String csRegPltCd = null;

	// Constant 
	// defect 9288 
	private final static int ERRORNO_SUNSET = 2016;
	// end defect 9288 
	private final static String ERRMSG_DATA_MISS =
		"Data missing for IsNextVCREG029";
	private final static String ERRMSG_ERROR = "ERROR";
	private final static String PLATE = "Plate";
	private final static String PLATE_STKR = "Plate & Sticker";
	private final static String SELECT = "Select One:";
	private final static String STKR = "Sticker";
	private final static String TITLE_REG016 =
		"Replacement Choices   REG016";

	/**
	 * FrmReplacementChoicesREG016 constructor.
	 */

	public FrmReplacementChoicesREG016()
	{
		super();
		initialize();
	}

	/**
	 * FrmReplacementChoicesREG016 constructor with parent.
	 * 
	 * @param aaParent Dialog
	 */
	public FrmReplacementChoicesREG016(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmReplacementChoicesREG016 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmReplacementChoicesREG016(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		// Determines what actions to take when Enter, Cancel, or 
		// Help are pressed.
		if (!startWorking())
		{
			return;
		}

		try
		{
			//field validation
			clearAllColor(this);

			// defect 9126/ 9085 
			if (aaAE.getSource() == ivjbuttonPanel.getBtnEnter())
			{
				createReplVehInqDataCopy();

				if (getradioPlateSticker().isSelected())
				{
					// Set new plate indicator to yes
					caReplRegValidData.setNewPltReplIndi(1);
					setReplPltOptions();
					handlePltTypes();
					handleStkrTypes();
				}
				else if (getradioSticker().isSelected())
				{
					caRegValidData.setNewPltReplIndi(0);
					handleStkrTypes();
				}
				// Plate Only
				else if (getradioPlate().isSelected())
				{
					// Set new plate indicator to yes
					caReplRegValidData.setNewPltReplIndi(1);
					setReplPltOptions();
					handlePltTypes();

				}
				caReplRegValidData.setEnterOnReplChoices(true);
			}
			// end defect 9126 /9085 

			else if (aaAE.getSource() == ivjbuttonPanel.getBtnCancel())
			{
				caRegValidData.setEnterOnReplChoices(false);
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == ivjbuttonPanel.getBtnHelp())
			{
				// defect 8177
				//RTSHelp.displayHelp(RTSHelp.REG016);
				if (UtilityMethods.isMFUP())
				{
					RTSHelp.displayHelp(RTSHelp.REG016);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.REG016A);
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
	 * 
	 * Create Copy of Veh InquiryData 
	 * 
	 */
	private void createReplVehInqDataCopy()
	{
		// Copy VehicleInquiryData 
		caReplVehicleInquiryData =
			(VehicleInquiryData) UtilityMethods.copy(
				caVehicleInquiryData);

		// Copy Validation Object
		caReplRegValidData =
			(RegistrationValidationData) UtilityMethods.copy(
				caVehicleInquiryData.getValidationObject());

		// Set Validation Object
		caReplVehicleInquiryData.setValidationObject(
			caReplRegValidData);

		// Copy Registration Data
		caReplRegisData =
			(RegistrationData) UtilityMethods.copy(
				caVehicleInquiryData.getMfVehicleData().getRegData());

		// Set Registration Data
		caReplVehicleInquiryData.getMfVehicleData().setRegData(
			caReplRegisData);

		// Copy Special Plates Regis Data 
		caReplSpclPltRegisData =
			(SpecialPlatesRegisData) UtilityMethods.copy(
				caVehicleInquiryData
					.getMfVehicleData()
					.getSpclPltRegisData());

		// Set Special Plates Regis Data 
		caReplVehicleInquiryData
			.getMfVehicleData()
			.setSpclPltRegisData(
			caReplSpclPltRegisData);
	}

	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				ivjbuttonPanel.setLayout(getbuttonPanelFlowLayout());
				ivjbuttonPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjbuttonPanel;
	}
	/**
	 * Return the buttonPanelFlowLayout property value.
	 * 
	 * @return java.awt.FlowLayout
	 */
	private java.awt.FlowLayout getbuttonPanelFlowLayout()
	{
		java.awt.FlowLayout ivjbuttonPanelFlowLayout = null;
		try
		{
			// Create part
			ivjbuttonPanelFlowLayout = new java.awt.FlowLayout();
			ivjbuttonPanelFlowLayout.setVgap(10);
		}
		catch (Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		return ivjbuttonPanelFlowLayout;
	}

	/**
	 * Return the FrmReplacementChoicesREG016ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax
		.swing
		.JPanel getFrmReplacementChoicesREG016ContentPane1()
	{
		if (ivjFrmReplacementChoicesREG016ContentPane1 == null)
		{
			try
			{
				ivjFrmReplacementChoicesREG016ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmReplacementChoicesREG016ContentPane1.setName(
					"FrmReplacementChoicesREG016ContentPane1");
				ivjFrmReplacementChoicesREG016ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmReplacementChoicesREG016ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmReplacementChoicesREG016ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(312, 226));
				ivjFrmReplacementChoicesREG016ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				java.awt.GridBagConstraints constraintsJPanel1 =
					new java.awt.GridBagConstraints();
				constraintsJPanel1.gridx = 1;
				constraintsJPanel1.gridy = 1;
				constraintsJPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJPanel1.weightx = 1.0;
				constraintsJPanel1.weighty = 1.0;
				constraintsJPanel1.ipadx = -12;
				constraintsJPanel1.ipady = -5;
				constraintsJPanel1.insets =
					new java.awt.Insets(16, 40, 3, 40);
				getFrmReplacementChoicesREG016ContentPane1().add(
					getJPanel1(),
					constraintsJPanel1);

				java.awt.GridBagConstraints constraintsbuttonPanel =
					new java.awt.GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 2;
				constraintsbuttonPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 21;
				constraintsbuttonPanel.ipady = 47;
				constraintsbuttonPanel.insets =
					new java.awt.Insets(4, 31, 12, 31);
				getFrmReplacementChoicesREG016ContentPane1().add(
					getbuttonPanel(),
					constraintsbuttonPanel);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjFrmReplacementChoicesREG016ContentPane1;
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
				java.awt.GridBagConstraints consGridBagConstraints2 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints3 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints1 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints2.insets =
					new java.awt.Insets(3, 52, 3, 52);
				consGridBagConstraints2.ipady = -2;
				consGridBagConstraints2.ipadx = 42;
				consGridBagConstraints2.gridy = 1;
				consGridBagConstraints2.gridx = 0;
				consGridBagConstraints3.insets =
					new java.awt.Insets(3, 52, 14, 52);
				consGridBagConstraints3.ipady = -2;
				consGridBagConstraints3.ipadx = 54;
				consGridBagConstraints3.gridy = 2;
				consGridBagConstraints3.gridx = 0;
				consGridBagConstraints1.insets =
					new java.awt.Insets(11, 52, 3, 23);
				consGridBagConstraints1.ipady = -2;
				consGridBagConstraints1.ipadx = 27;
				consGridBagConstraints1.gridy = 0;
				consGridBagConstraints1.gridx = 0;
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(
					new TitledBorder(new EtchedBorder(), SELECT));
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());
				ivjJPanel1.add(
					getradioPlateSticker(),
					consGridBagConstraints1);
				ivjJPanel1.add(
					getradioSticker(),
					consGridBagConstraints2);
				ivjJPanel1.add(
					getradioPlate(),
					consGridBagConstraints3);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(232, 145));

				RTSButtonGroup laRadioGrp = new RTSButtonGroup();
				laRadioGrp.add(getradioPlateSticker());
				laRadioGrp.add(getradioSticker());
				laRadioGrp.add(getradioPlate());
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the radioPlate property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private javax.swing.JRadioButton getradioPlate()
	{
		if (ivjradioPlate == null)
		{
			try
			{
				ivjradioPlate = new javax.swing.JRadioButton();
				ivjradioPlate.setName("radioPlate");
				ivjradioPlate.setMnemonic(80);
				ivjradioPlate.setText(PLATE);
				ivjradioPlate.setMaximumSize(
					new java.awt.Dimension(54, 22));
				ivjradioPlate.setActionCommand(PLATE);
				ivjradioPlate.setMinimumSize(
					new java.awt.Dimension(54, 22));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioPlate;
	}

	/**
	 * Return the radioPlateSticker property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private javax.swing.JRadioButton getradioPlateSticker()
	{
		if (ivjradioPlateSticker == null)
		{
			try
			{
				ivjradioPlateSticker = new javax.swing.JRadioButton();
				ivjradioPlateSticker.setName("radioPlateSticker");
				ivjradioPlateSticker.setMnemonic(76);
				ivjradioPlateSticker.setText(PLATE_STKR);
				ivjradioPlateSticker.setMaximumSize(
					new java.awt.Dimension(110, 22));
				ivjradioPlateSticker.setActionCommand(PLATE_STKR);
				ivjradioPlateSticker.setMinimumSize(
					new java.awt.Dimension(110, 22));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}

				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioPlateSticker;
	}

	/**
	 * Return the radioSticker property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private javax.swing.JRadioButton getradioSticker()
	{
		if (ivjradioSticker == null)
		{
			try
			{
				ivjradioSticker = new javax.swing.JRadioButton();
				ivjradioSticker.setName("radioSticker");
				ivjradioSticker.setMnemonic(83);
				ivjradioSticker.setText(STKR);
				ivjradioSticker.setMaximumSize(
					new java.awt.Dimension(66, 22));
				ivjradioSticker.setActionCommand(STKR);
				ivjradioSticker.setMinimumSize(
					new java.awt.Dimension(66, 22));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioSticker;
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
	 * 
	 * Proceed from REG016 based upon Plate Options
	 *
	 */
	private void handlePltTypes()
	{
		if (!caReplRegValidData.getReplPltOptions())
		{
			caReplRegValidData.setInvItms(new Vector());
			caReplRegisData.setRegPltCd(
				caReplRegValidData.getReplPltCd());

			RegistrationClientBusiness.procsReplPltInv(
				caReplVehicleInquiryData);

			caReplRegisData.setPrevPltNo(
				caRegValidData
					.getOrigVehInqData()
					.getMfVehicleData()
					.getRegData()
					.getRegPltNo());

			// defect 9288
			// Notify that sunsetted plate not available for 
			// remanufacture.  
			if (cbSpclPltSunset)
			{
				RTSException leRTSEx = new RTSException(ERRORNO_SUNSET);
				leRTSEx.displayError();
			}
			// end defect 9288 

		}
		if (!getradioPlateSticker().isSelected())
		{
			//defect 9085
			if (caReplRegValidData.getReplPltOptions())
			{
				getController().processData(
					VCReplacementChoicesREG016.PLATE_SELECTION,
					caReplVehicleInquiryData);
			}
			// Replace with same plate 
			else
			{
				//	RegistrationClientUtilityMethods.addItmCdToInv(
				//		caRegValidData,
				//		caRegValidData.getOrigRegPltCd());

				// Calculate fees
				caCompTransData =
					RegistrationClientUtilityMethods.prepFees(
						caReplVehicleInquiryData,
						caRegValidData.getOrigVehInqData());

				getController().processData(
					AbstractViewController.ENTER,
					caCompTransData);
			}
		}
	}

	/**
	 * If more than one sticker type
	 *  - Display REG001
	 * 
	 * Else
	 * 	- Add sticker to inventory
	 *  - If Sticker Only:
	 * 		- Calc Fees
	 * 		- Issue Inventory
	 *  - If Plate and Sticker: 
	 * 	    - If One Sticker and One Plate Option
	 *        - Add both to inventory
	 *        - Calc Fees
	 *        - Issue Inventory
	 *      - Else
	 *        - Go to REG011
	 * 
	 */
	private void handleStkrTypes()
	{
		// defect 9085
		// get sticker count

		Vector lvPltToStkrData =
			RegistrationPlateStickerCache.getPltStkrs(
				caReplRegisData.getRegClassCd(),
				caReplRegisData.getRegPltCd(),
				caReplVehicleInquiryData.getRTSEffDt());

		if (lvPltToStkrData != null)
		{
			int liNumStkrTypes = lvPltToStkrData.size();

			caReplRegValidData.setInvItms(null);

			// Test if only one sticker type found; Bypass sticker 
			// selection screen
			// Set sticker code equal to replacement sticker code
			if (liNumStkrTypes == 1)
			{
				// Set sticker code equal to replacement sticker code
				PlateToStickerData laData =
					(PlateToStickerData) lvPltToStkrData.get(0);

				caReplRegisData.setRegStkrCd(laData.getRegStkrCd());

				RegistrationClientUtilityMethods.addItmCdToInv(
					caReplRegValidData,
					laData.getRegStkrCd());

				// RegistrationClientUtilityMethods.procsDsabldPersnPlt(
				// caVehicleInquiryData);

				// If choose to issue new plate and no plate options exist
				if (getradioPlateSticker().isSelected()
					&& !caReplRegValidData.getReplPltOptions())
				{
					// set previous plate no 
					caReplRegisData.setPrevPltNo(
						caRegValidData
							.getOrigVehInqData()
							.getMfVehicleData()
							.getRegData()
							.getRegPltNo());

					RegistrationClientBusiness.procsReplPltInv(
						caReplVehicleInquiryData);
				}
			}

			// KPH: This is bizarre; wouldn't be in here if 
			// had not selected sticker   
			//			else if (liNumStkrTypes < 1)
			//			{
			//				caVehicleInquiryData
			//					.getMfVehicleData()
			//					.getRegData()
			//					.setRegStkrCd(
			//					"");
			//RegistrationClientUtilityMethods.procsDsabldPersnPlt(
			//	caVehicleInquiryData);
			//}

			// If only have one sticker option and no plate options - or 
			// no plate, calcFees and Issue Inventory 
			if (liNumStkrTypes == 1)
			{
				if (getradioSticker().isSelected()
					|| !caReplRegValidData.getReplPltOptions())
				{
					caCompTransData =
						RegistrationClientUtilityMethods.prepFees(
							caReplVehicleInquiryData,
							caRegValidData.getOrigVehInqData());
					getController().processData(
						AbstractViewController.ENTER,
						caCompTransData);
				}
				else
				{
					getController().processData(
						VCReplacementChoicesREG016.PLATE_SELECTION,
						caReplVehicleInquiryData);

				}
			}
			else
			{
				getController().processData(
					VCReplacementChoicesREG016.STICKER_SELECTION,
					caReplVehicleInquiryData);
			}
		}
		// end defect 9085 
	}

	/**
	 * Initialize the class.
	 */

	private void initialize()
	{
		try
		{
			setName("FrmReplacementChoicesREG016");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(300, 210);
			setTitle(TITLE_REG016);
			setContentPane(
				getFrmReplacementChoicesREG016ContentPane1());
		}
		catch (Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */

	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmReplacementChoicesREG016 laFrmReplacementChoicesREG016;

			laFrmReplacementChoicesREG016 =
				new FrmReplacementChoicesREG016();

			laFrmReplacementChoicesREG016.setModal(true);

			laFrmReplacementChoicesREG016
				.addWindowListener(new java.awt.event.WindowAdapter()
			{

				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{

					System.exit(0);

				}

			});

			laFrmReplacementChoicesREG016.show();

			java.awt.Insets laInsets =
				laFrmReplacementChoicesREG016.getInsets();

			laFrmReplacementChoicesREG016.setSize(
				laFrmReplacementChoicesREG016.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmReplacementChoicesREG016.getHeight()
					+ laInsets.top
					+ laInsets.bottom);

			laFrmReplacementChoicesREG016.setVisibleRTS(true);

		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);

			aeEx.printStackTrace(System.out);
		}

	}

	/**
	 *  Set an indicator in RegistrationValidationData re: options
	 *  for plate replacement 
	 */
	private void setReplPltOptions()
	{
		boolean lbPltOptions = false;
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(csRegPltCd);

		if (laPltTypeData != null)
		{
			// NO Repl Options if Owner, e.g. OLDPLT  
			if (!laPltTypeData
				.getNeedsProgramCd()
				.equals(SpecialPlatesConstant.OWNER))
			{

				if (caReplVehicleInquiryData
					.getMfVehicleData()
					.isSpclPlt())
				{
					// defect 9288
					// Do not allow remanufacture if Sunset Special Plate

					// If Not Expired && !Sunset
					if (!RegistrationClientUtilityMethods
						.isExpiredSpclPlt(caReplVehicleInquiryData)
						&& !cbSpclPltSunset)
					{
						lbPltOptions = true;

					}
					// Reset SpclPltRegisData
					else
					{
						caReplSpclPltRegisData = null;
					}
					// end defect 9288 
				}
				// If !Spcl Plt, RegPltCd != ReplPltCd 
				else if (
					!csRegPltCd.equals(caRegValidData.getReplPltCd()))
				{
					lbPltOptions = true;
				}
			}
		}
		caReplRegValidData.setReplPltOptions(lbPltOptions);
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
			if (aaDataObject instanceof Vector)
			{
				Vector lvIsNextVCREG029 = (Vector) aaDataObject;
				if (lvIsNextVCREG029 != null)
				{
					if (lvIsNextVCREG029.size() == 2)
					{ // determine next vc if NOT reg029
						if (lvIsNextVCREG029.get(0) instanceof Boolean)
						{
							// first element is flag whether to go
							// to REG029
							getController().processData(
								VCReplacementChoicesREG016
									.REDIRECT_IS_NEXT_VC_REG029,
								lvIsNextVCREG029);
						}
						else if (
							lvIsNextVCREG029.get(0) instanceof String)
						{
							getController().processData(
								VCReplacementChoicesREG016
									.REDIRECT_NEXT_VC,
								lvIsNextVCREG029);
						}
					}
					else
					{
						new RTSException(
							RTSException.FAILURE_MESSAGE,
							ERRMSG_DATA_MISS,
							ERRMSG_ERROR).displayError(
							this);
						return;
					}
				}
			}
			else if (aaDataObject instanceof VehicleInquiryData)
			{
				caVehicleInquiryData =
					(VehicleInquiryData) aaDataObject;
				caRegValidData =
					(RegistrationValidationData) caVehicleInquiryData
						.getValidationObject();

				// defect 9288
				// Save the registration plate code
				csRegPltCd =
					caVehicleInquiryData
						.getMfVehicleData()
						.getRegData()
						.getRegPltCd();

				// defect 9085 
				// If Not Annual && Special Plate is Expired 
				//   do not allow Sticker Replacement 
				boolean lbSpclPltExpired = false;

				SpecialPlatesRegisData caSpclPltRegisData =
					caVehicleInquiryData
						.getMfVehicleData()
						.getSpclPltRegisData();

				if (caSpclPltRegisData != null)
				{
					// defect 9864 
					// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  

					lbSpclPltExpired =
						CommonValidations.isRegistrationExpired(
							caSpclPltRegisData.getPltExpMo(),
							caSpclPltRegisData.getPltExpYr());
					// end defect 9864 

					if (caSpclPltRegisData.getOrgNo() != null)
					{
						cbSpclPltSunset =
							(OrganizationNumberCache
								.isSunsetted(
									csRegPltCd,
									caSpclPltRegisData
										.getOrgNo()
										.trim()));
					}
				}
				// end defect 9085
				// end defect 9288  

				// Disable PlateSticker and Sticker radio buttons 
				// if registration has expired
				// defect 10623
				// Token Trlr no longer prints sticker. Add check for
				// Token Trlr to also disable Sticker; Plate / Sticker.
				if (caRegValidData.getRegistrationExpired() == 1
					|| caVehicleInquiryData.getMfVehicleData()
						.getRegData().getRegClassCd() ==
							RegistrationConstant.REGCLASSCD_TOKEN_TRLR)
				{
					// end defect 10623
					getradioPlateSticker().setEnabled(false);
					getradioSticker().setEnabled(false);
					getradioPlate().setSelected(true);
					getradioPlate().requestFocus();
				}
				else
				{
					// defect 8218
					// Returned data is of type PlateToStickerData
					Vector lvPltToStkrData = null;
					// get sticker count
					lvPltToStkrData =
						RegistrationPlateStickerCache.getPltStkrs(
							caVehicleInquiryData
								.getMfVehicleData()
								.getRegData()
								.getRegClassCd(),
							caRegValidData.getReplPltCd(),
							caVehicleInquiryData.getRTSEffDt());
					String lsRegStkrCd = "";
					if (lvPltToStkrData.size() != 0)
					{
						lsRegStkrCd =
							((PlateToStickerData) lvPltToStkrData
								.elementAt(0))
								.getRegStkrCd();
					}

					if (lvPltToStkrData.size() <= 1
						&& lsRegStkrCd.trim().equals(""))
					{
						getradioPlateSticker().setSelected(false);
						getradioPlateSticker().setEnabled(false);
						getradioSticker().setSelected(true);
						getradioSticker().requestFocus();
					}
					else
					{
						getradioPlateSticker().setSelected(true);
						getradioPlateSticker().requestFocus();
						// defect 9689
						// Vendor Plate can have current Regis with an 
						// expired plate
						//getradioSticker().setEnabled(!lbSpclPltExpired);
						if (caSpclPltRegisData != null
							&& PlateTypeCache.isVendorPlate(
								caVehicleInquiryData
									.getMfVehicleData()
									.getRegData()
									.getRegPltCd()))
						{
							getradioSticker().setEnabled(true);
						}
						else
						{
							getradioSticker().setEnabled(
								!lbSpclPltExpired);
						}
						// end defect 9689
					}
					// end defect 8218

				}
			}

		}

	}
}
