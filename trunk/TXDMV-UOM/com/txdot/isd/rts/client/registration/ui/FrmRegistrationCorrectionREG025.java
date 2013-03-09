package com.txdot.isd.rts.client.registration.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.client.registration.business.RegistrationClientBusiness;
import com.txdot.isd.rts.client.registration.business.RegistrationClientUtilityMethods;
import com.txdot.isd.rts.client.title.ui.TitleClientUtilityMethods;

import com.txdot.isd.rts.services.cache.*;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmRegistrationCorrectionREG025.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik		04/18/2002	Global change for startWorking() and 
 *							doneWorking()
 * T Pederson 	04/30/2002  Don't allow spaces to be entered in input 
 *							field
 * J Kwik		05/21/2002	Fix defect CQU100004008 - If headquarters 
 *							then display message whether to complete 
 *							trans and don't display Fees Due PMT004 
 * MAbs			05/23/2002	CQU100004078
 * Kwik&Taylor	05/28/2002	Fix defect CQU100004077 - put cursor in
 *							plate no field after error msg 86.
 * N Ting		05/31/2002	Fix CQU100004175, check for null for VIN in 
 *							setData()
 * J Kwik		06/10/2002	Fix CQU100004247, set ciVehGrossWt and added 
 *							else if in validateInputFields() related to 
 *							fixed wt indi.
 * R Taylor		07/25/2002	Fix 4247 check if ton reqd for fixed wt 
 *							vehicles
 * B Hargrove	12/05/2003  Changed screen input edit on txtVehMk to '-1'
 *							(default, no edit) from '6' (AlphaNoSpace) so 
 *							Vehicle Make of '????' passes edit.
 *				    	    modify FrmRegistrationCorrectionREG025 
 *							(Input display)
 *				    	    modify gettxtVehMk()
 *						    defect 6228  Ver 5.1.5 fix 2
 * B Hargrove	12/15/2003	Added edit on gettxtVehModlYr so that it 
 *							must be 4 digits
 *                           if field is enabled and Model Year is entered
 *						    modify validateInputFields()
 *						    defect 6411  Ver 5.1.5 fix 2
 * B Hargrove	12/11/2003	Added code in to update the Vehicle Gross 
 *							Weight (during Modify, the user can update 
 *							Empty Weight, which causes Gross Weight to 
 *							change)
 *						    modify completeTrans()
 *						    defect 6497  Ver 5.1.5 Fix 2
 * K Harrell	01/28/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Imported; Reapplied 6228, 6411, 6497.
 *							delete ivjstcLblStickerNo, ivjstcLblStickerNo
 *							delete getstcLblStickerNo(), gettxtStickerNO()
 *							modify setData(),completeTrans()
 *  							Ver 5.2.0	
 * B Hargrove	11/18/2004	Added edit for Model Year > 1880 and <= 
 *							Today + 3 years. 
 *						    modify validateInputFields()
 *						    defect 6872  Ver 5.2.2
 * B Hargrove	11/30/2004	Format code and comments. 
 *						    modify validateInputFields()
 *						    defect 6872  Ver 5.2.2
 * B Hargrove	12/07/2004	Move comment verbiage onto separate line. 
 *						    modify validateInputFields()
 *						    defect 6872  Ver 5.2.2
 * B Hargrove	05/11/2005	Update help based on User Guide updates.
 * 							See also: services.util.RTSHelp
 * 							(fix merged in from VAJ)
 *  							modify actionPerfomed() 
 * 							defect 8177 Ver 5.2.2 Fix 5
 * B Hargrove	06/23/2005	Modify code from move to Java 1.4
 * 							Bring code to standards.
 * 							Arrow key handling is now done in 
 * 							ButtonPanel.  Add code to handle
 * 							Pneumatic\Solid radio button traversal.
 * 							Remove unused methods.
 * 							add carrRadioButton[]
 *  							modify initialize(), keyPressed()
 * 							delete implements KeyListener
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	07/19/2005	Refactor\Move 
 * 							RegistrationClientUtilityMethods class from
 *							com.txdot.isd.rts.client.reg.ui to
 *							com.txdot.isd.rts.client.reg.business.
 *							defect 7894 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Jeff S.		01/03/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), carrRadioButton
 * 							modify getJPanel7(), initialize(), 
 * 								getradioPneumatic(), getradioSolid()
 * 							defect 7894 Ver 5.2.3
 * Jeff S.		06/22/2006	Used screen constant for CTL001 Title.
 * 							remove CONFIRM_ACTION
 * 							modify actionPerformed()
 * 							defect 8756 Ver 5.2.3
 * K Harrell	10/07/2006	Disable Exp Mo/Yr if Standard Exempt
 * 							Additional class cleanup
 * 							add cbStandardExempt
 * 							modify setData(), validateInputFields()
 * 							defect 8900 Ver 5.3.0 
 * K Harrell	02/21/2007	Disable PlateNo if Special Plate
 * 							modify setData()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/17/2007	Use SystemProperty.isHQ(); Use Original	
 * 							VehicleInquiryData from RegValidData; 
 * 							Compare SpecialPlates Objects to see if 
 * 							changed
 * 							modify procsSoftStops(), actionPerformed(),
 * 							validateInputFields()
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/26/2007	Use CommonConstant.TXT_COMPLETE_TRANS_QUESTION
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	07/22/2007	Added additional precision re: Spcl Plt data
 * 							modification.
 * 							modify validateInputFields()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/17/2007	Copy Exp Mo/Yr to Special Plate RegisData 
 * 							modify setInputFields()
 * 							defect 9261 Ver Special Plates 
 * K Harrell	05/22/2008	Disable RegPltNo field when RegPltRmvCd !=0 
 * 							modify setData()
 * 							defect 9670 Ver 3 Defect POS A 
 * B Hargrove	07/11/2008	Do not copy Exp Mo/Yr to Special Plate 
 * 							RegisData for Vendor Plates 
 * 							modify setInputFields()
 * 							defect 9689 Ver Defect MyPlates_POS
 * K Harrell	09/05/2008	Consolidate Soft Stop Processing. 
 * 							Use RTSInputField constants for input types.
 * 							Add'l class cleanup. 
 * 							add isOutOfCounty() 
 * 							modify procsStops(), validateInputFields()
 * 							defect 7860 Ver Defect_POS_B
 * K Harrell	09/10/2008	Use calculated Vehicle Gross Wt vs. 
 * 							caRegValidData.getOrigVehGrossWt() as 
 * 							caRegValidData is never set. 
 * 							modify procsSoftStops()
 * 							defect 9821 Ver Defect_POS_B  
 * K Harrell	01/07/2009  Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify setInputFields(), validateInputFields()  
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	07/01/2009	Implement new OwnerData
 * 							delete setOrigVehInqData(), setRegValidData(), 
 * 							 setVehInqData() 
 * 							modify setData(), validateInputFields(),
 * 							 setInputFields()  
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/16/2009	refactor name/methods to better map to 
 * 							 Title/Registration. Implement ErrorConstant
 * 							CommonConstant where defined. 
 * 							modify validateInputFields()
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	07/22/2009	Disable OwnerId Label, Input field if not 
 * 							HQ.
 * 							modify initialize() 
 * 							defect 10130 Ver Defect_POS_F 
 * K Harrell	12/31/2009  Implement character validation for MF
 * 							add cvAddlMFValid
 *  						modify setData(), initialize(), 
 * 							  validateInputFields() 
 * 							defect 10299 Ver Defect_POS_H 
 * K Harrell	03/30/2010	Do not validate Gross Weight on Focus Lost 
 * 							when select Enter/Cancel/Help w/ Mouse
 * 							modify focusLost()
 * 							defect 10424 Ver POS_640   
 * K Harrell	03/30/2010	Correct merge issue
 * 							modify setData() 
 * 							defect 10425 Ver POS_640 
 * K Harrell	11/07/2011	delete MAX_GROSS_WEIGHT
 * 							modify calcGrossWt()
 * 							defect 10959 Ver 6.9.0
 * K Harrell	11/15/2011	modify validateInputFields()
 * 							defect 10959 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 *
 * This form allows the user to modify owner and registration 
 * information in the Registration Correction event. 
 *
 * @version 6.9.0   11/15/2011	
 * @author	Joseph Kwik
 * @since 			06/26/2001 15:47:31
 */
public class FrmRegistrationCorrectionREG025
	extends RTSDialogBox
	implements ActionListener, FocusListener
{
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JPanel ivjJPanel31 = null;
	private JPanel ivjJPanel7 = null;
	private JLabel ivjlblCapacity = null;
	private JLabel ivjlblGross = null;
	private JRadioButton ivjradioPneumatic = null;
	private JRadioButton ivjradioSolid = null;
	private JLabel ivjstcLblBodyStyle = null;
	private JLabel ivjstcLblCapacity = null;
	private JLabel ivjstcLblEmpty = null;
	private JLabel ivjstcLblExpMoYr = null;
	private JLabel ivjstcLblGross = null;
	private JLabel ivjstcLblModel = null;
	private JLabel ivjstcLblOwnerId = null;
	private JLabel ivjstcLblOwnerName = null;
	private JLabel ivjstcLblPlateNo = null;
	private JLabel ivjstcLblVin = null;
	private JLabel ivjstcLblYrMake = null;
	private RTSInputField ivjtxtExpMo = null;
	private RTSInputField ivjtxtExpYr = null;
	private RTSInputField ivjtxtOwnerId = null;
	private RTSInputField ivjtxtPlateNo = null;
	private JPanel ivjFrmRegistrationCorrectionREG025ContentPane1 =
		null;
	// defect 10112
	private RTSInputField ivjtxtOwnerName1 = null;
	private RTSInputField ivjtxtOwnerName2 = null;
	// end defect 10112 
	private RTSInputField ivjtxtVehBdyType = null;
	private RTSInputField ivjtxtVehEmptyWt = null;
	private RTSInputField ivjtxtVehMk = null;
	private RTSInputField ivjtxtVehModl = null;
	private RTSInputField ivjtxtVehModlYr = null;
	private RTSInputField ivjtxtVIN = null;
	private ButtonPanel ivjbuttonPanel = null;

	// defect 8900
	private boolean cbStandardExempt = false;
	private boolean cbSpclPlt = false;
	// end defect 8900 

	// int
	private int ciVehGrossWt = 0;

	// efect 10299
	private Vector cvAddlMFValid = new Vector();
	// end defect 10299 

	// Object 
	private CompleteTransactionData caCompTransData = null;
	private RegistrationValidationData caRegValidData;
	private VehicleInquiryData caOrigVehInqData;
	private VehicleInquiryData caVehInqData;

	// Constants
	private final static String APPORTIONED_TRL_PLT = "APPRTR";
	private final static String APPORTIONED_TRUCK_PLT = "APPRTK";
	private final static String BODY_STYLE = "Body Style:";
	private final static String CARRY_CAP = "Carrying Capacity:";
	private final static String EMPTY_WT = "Empty Weight:";
	private final static String EXP_MOYR = "Exp Mo/Yr:";
	private final static String GROSS_WT = "Gross Weight:";
	private final static String GROSS_WT_SOFT_STOP_MSG =
		"NEW GROSS WEIGHT LESS THAN ORIGINAL";
	private final static String MISC_VEH_CLASS = "MISC";
	private final static String MODEL = "Model:";
	private final static String OWNR_ID = "Owner Id:";
	private final static String OWNR_NAME = "Owner Name:";
	private final static String PLATE_NO = "Plate No:";
	private final static String PNEUMATIC = "Pneumatic";
	private final static String PNEUMATIC_TIRE_TYPE = "P";
	private final static String SEL_TIRE_TYPE = "Select tire type:";
	private final static String SOLID = "Solid";
	private final static String SOLID_TIRE_TYPE = "S";
	private final static String TITLE_REG025 =
		"Registration Correction     REG025";
	private final static String VIN = "VIN:";
	private final static String YEAR_MAKE = "Year/Make:";
	
	// defect 10959 
	//private final static int MAX_GROSS_WEIGHT = 80000;
	// end defect 10959 
	
	private final static int THIRD_OF_VEH_WEIGHT = 3;

	/**
	 * FrmRegistrationCorrectionREG025 constructor comment.
	 */
	public FrmRegistrationCorrectionREG025()
	{
		super();
		initialize();
	}

	/**
	 * FrmRegistrationCorrectionREG025 constructor comment.
	 * 
	 * @param aaParent Dialog
	 */
	public FrmRegistrationCorrectionREG025(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmRegistrationCorrectionREG025 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmRegistrationCorrectionREG025(JFrame aaParent)
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
		// Code to avoid actionPerformed being executed more than once
		// when clicking on the button multiple times.
		if (!startWorking())
		{
			return;
		}
		try
		{
			//field validation
			clearAllColor(this);
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				if (!validateInputFields())
				{
					return;
				}
				setInputFields();

				if (!completeTrans())
				{
					return;
				}

				if (SystemProperty.isHQ())
				{
					// defect 8756
					// Used Screen constant for CTL001 title
					RTSException leRTSEx =
						new RTSException(
							RTSException.CTL001,
							CommonConstant.TXT_COMPLETE_TRANS_QUESTION,
							ScreenConstant.CTL001_FRM_TITLE);
					// end defect 8756
					int liResponse = leRTSEx.displayError(this);
					if (liResponse == RTSException.NO)
					{
						return;
					}
				}

				getController().processData(
					AbstractViewController.ENTER,
					caCompTransData);
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				if (UtilityMethods.isMFUP())
				{
					RTSHelp.displayHelp(RTSHelp.REG025A);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.REG025B);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Calculate gross weight from empty weight.
	 * 
	 * @param aeEx RTSException
	 * @return boolean
	 */
	private boolean calcGrossWt(RTSException aeEx)
	{
		// Calculate gross weight from empty weight
		RegistrationData laRegData =
			caVehInqData.getMfVehicleData().getRegData();
		String lsVehEmptyWt = gettxtVehEmptyWt().getText();
		int liVehEmptyWt =
			lsVehEmptyWt.equals("")
				? 0
				: Integer.parseInt(lsVehEmptyWt);
		ciVehGrossWt = liVehEmptyWt + laRegData.getVehCaryngCap();
		gettxtVehEmptyWt().setText(Integer.toString(liVehEmptyWt));
		getlblGross().setText(Integer.toString(ciVehGrossWt));
		RegistrationClassData laData = null;
		try
		{
			laData =
				RegistrationClientBusiness.getRegClassDataObject(
					caVehInqData);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			return false;
		}
		// Determine if Heavy Vehicle Use Tax Verify required
		if (laData.getHvyVehUseWt() > 0)
		{
			caRegValidData.setHvyVehUseTaxMask(true);
			if (ciVehGrossWt >= laData.getHvyVehUseWt())
			{
				caRegValidData.setHvyVehUseTaxRequired(1);
			}
			else
			{
				caRegValidData.setHvyVehUseTaxRequired(0);
			}
		}
		else
		{
			caRegValidData.setHvyVehUseTaxRequired(0);
		}
		// Validate carrying capacity for trailers
		if (laData.getTrlrCapValidReqd() == 1)
		{
			// determine if carrying capacity is at least one-third
			// of the empty weight
			if (laRegData.getVehCaryngCap()
				< liVehEmptyWt / THIRD_OF_VEH_WEIGHT)
			{
				if (aeEx == null)
				{
					gettxtVehEmptyWt().setBackground(
						RTSException.ERR_COLOR);
					return false;
				}
				else
				{
					aeEx.addException(
						new RTSException(13),
						gettxtVehEmptyWt());
				}
			}
		}
		// defect 10959 
		// Now validated on REG003 
		// Test gross weight not greater than 80000 pounds
		//		if (ciVehGrossWt > MAX_GROSS_WEIGHT)
		//		{
		//			if (aeEx == null)
		//			{
		//				gettxtVehEmptyWt().setBackground(
		//					RTSException.ERR_COLOR);
		//				return false;
		//			}
		//			else
		//			{
		//				aeEx.addException(
		//					new RTSException(89),
		//					gettxtVehEmptyWt());
		//			}
		//		}
		// end defect 10959 
		return true;
	}

	/**
	 * Complete transaction. Call calcFees.
	 * 
	 * @return boolean
	 */
	private boolean completeTrans()
	{
		// Calculate fees
		caCompTransData =
			RegistrationClientUtilityMethods.prepFees(
				caVehInqData,
				caRegValidData.getOrigVehInqData());

		RegistrationData laRegData =
			caVehInqData.getMfVehicleData().getRegData();

		// Determine if plate number has been updated
		if (!laRegData
			.getRegPltNo()
			.equals(caRegValidData.getOrigRegPltNo()))
		{
			// Update regis plate number via owner-supplied info
			caCompTransData.setOwnrSuppliedPltNo(
				laRegData.getRegPltNo());
			laRegData.setOwnrSuppliedExpYr(laRegData.getRegExpYr());
		}

		else if (
			laRegData.getRegExpYr()
				!= caRegValidData.getOrigRegExpYr())
		{
			// Update regis expiration year via owner-supplied info                  
			laRegData.setOwnrSuppliedExpYr(laRegData.getRegExpYr());

			// Update regis plate number via owner-supplied info
			caCompTransData.setOwnrSuppliedPltNo(
				laRegData.getRegPltNo());
		}

		// Restore current plate/expiration for mainframe processing
		laRegData.setRegPltNo(caRegValidData.getOrigRegPltNo());
		laRegData.setRegExpYr(caRegValidData.getOrigRegExpYr());
		// defect 6497 - Update Gross Weight if it has changed
		// (ciVehGrossWt is calculated in calcGrossWt()
		if (ciVehGrossWt != laRegData.getVehGrossWt())
		{
			laRegData.setVehGrossWt(ciVehGrossWt);
		}
		// end defect 6497
		return true;
	}

	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE java.awt.event.FocusEvent
	 */
	public void focusGained(java.awt.event.FocusEvent aaFE)
	{
		//empty code block
	}

	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE java.awt.event.FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		if (!aaFE.isTemporary()
			&& aaFE.getSource() == gettxtVehEmptyWt())
		{
			// defect 10424 
			// Do not bother to invoke calcGrossWt() if 
			// Enter/Cancel/Help.  
			// Enter will be handled via validateInputFields() 
			if (aaFE.getOppositeComponent()
				!= getbuttonPanel().getBtnCancel()
				&& aaFE.getOppositeComponent()
					!= getbuttonPanel().getBtnEnter()
				&& aaFE.getOppositeComponent()
					!= getbuttonPanel().getBtnHelp())
			{
				// end defect 10424

				// calc gross weight from empty weight
				if (!calcGrossWt(null))
				{
					gettxtVehEmptyWt().requestFocus();
					return;
				}
			}
		}
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
				ivjbuttonPanel.setBounds(20, 353, 560, 46);
				ivjbuttonPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjbuttonPanel.setRequestFocusEnabled(false);
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
	 * Return FrmRegistrationCorrectionREG025ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */

	private javax
		.swing
		.JPanel getFrmRegistrationCorrectionREG025ContentPane1()
	{
		if (ivjFrmRegistrationCorrectionREG025ContentPane1 == null)
		{
			try
			{
				ivjFrmRegistrationCorrectionREG025ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmRegistrationCorrectionREG025ContentPane1.setName(
					"FrmRegistrationCorrectionREG025ContentPane1");
				ivjFrmRegistrationCorrectionREG025ContentPane1
					.setLayout(
					null);
				ivjFrmRegistrationCorrectionREG025ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmRegistrationCorrectionREG025ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(516, 474));
				ivjFrmRegistrationCorrectionREG025ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				getFrmRegistrationCorrectionREG025ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
				getFrmRegistrationCorrectionREG025ContentPane1().add(
					getJPanel2(),
					getJPanel2().getName());
				getFrmRegistrationCorrectionREG025ContentPane1().add(
					getJPanel3(),
					getJPanel3().getName());
				getFrmRegistrationCorrectionREG025ContentPane1().add(
					getJPanel31(),
					getJPanel31().getName());
				getFrmRegistrationCorrectionREG025ContentPane1().add(
					getbuttonPanel(),
					getbuttonPanel().getName());
				getFrmRegistrationCorrectionREG025ContentPane1().add(
					getJPanel7(),
					getJPanel7().getName());
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
		return ivjFrmRegistrationCorrectionREG025ContentPane1;
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
				ivjJPanel1.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setBounds(21, 12, 264, 109);
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(222, 107));
				java.awt.GridBagConstraints constraintsstcLblPlateNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblPlateNo.gridx = 1;
				constraintsstcLblPlateNo.gridy = 1;
				constraintsstcLblPlateNo.ipadx = 16;
				constraintsstcLblPlateNo.insets =
					new java.awt.Insets(18, 31, 7, 6);
				getJPanel1().add(
					getstcLblPlateNo(),
					constraintsstcLblPlateNo);
				java.awt.GridBagConstraints constraintsstcLblExpMoYr =
					new java.awt.GridBagConstraints();
				constraintsstcLblExpMoYr.gridx = 1;
				constraintsstcLblExpMoYr.gridy = 2;
				constraintsstcLblExpMoYr.ipadx = 9;
				constraintsstcLblExpMoYr.insets =
					new java.awt.Insets(7, 29, 49, 6);
				getJPanel1().add(
					getstcLblExpMoYr(),
					constraintsstcLblExpMoYr);
				java.awt.GridBagConstraints constraintstxtPlateNo =
					new java.awt.GridBagConstraints();
				constraintstxtPlateNo.gridx = 2;
				constraintstxtPlateNo.gridy = 1;
				constraintstxtPlateNo.gridwidth = 2;
				constraintstxtPlateNo.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtPlateNo.weightx = 1.0;
				constraintstxtPlateNo.ipadx = 84;
				constraintstxtPlateNo.insets =
					new java.awt.Insets(15, 6, 4, 67);
				getJPanel1().add(
					gettxtPlateNo(),
					constraintstxtPlateNo);
				java.awt.GridBagConstraints constraintstxtExpMo =
					new java.awt.GridBagConstraints();
				constraintstxtExpMo.gridx = 2;
				constraintstxtExpMo.gridy = 2;
				constraintstxtExpMo.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtExpMo.weightx = 1.0;
				constraintstxtExpMo.ipadx = 44;
				constraintstxtExpMo.insets =
					new java.awt.Insets(4, 6, 46, 5);
				getJPanel1().add(gettxtExpMo(), constraintstxtExpMo);
				java.awt.GridBagConstraints constraintstxtExpYr =
					new java.awt.GridBagConstraints();
				constraintstxtExpYr.gridx = 3;
				constraintstxtExpYr.gridy = 2;
				constraintstxtExpYr.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtExpYr.weightx = 1.0;
				constraintstxtExpYr.ipadx = 62;
				constraintstxtExpYr.insets =
					new java.awt.Insets(4, 6, 46, 30);
				getJPanel1().add(gettxtExpYr(), constraintstxtExpYr);
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
		return ivjJPanel1;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return javax.swing.JPanel
	 */

	private javax.swing.JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new javax.swing.JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjJPanel2.setLayout(new java.awt.GridBagLayout());
				ivjJPanel2.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel2.setBounds(305, 12, 272, 109);
				ivjJPanel2.setMinimumSize(
					new java.awt.Dimension(222, 107));
				java.awt.GridBagConstraints constraintsstcLblOwnerId =
					new java.awt.GridBagConstraints();
				constraintsstcLblOwnerId.gridx = 1;
				constraintsstcLblOwnerId.gridy = 1;
				constraintsstcLblOwnerId.ipadx = 14;
				constraintsstcLblOwnerId.insets =
					new java.awt.Insets(10, 22, 8, 12);
				getJPanel2().add(
					getstcLblOwnerId(),
					constraintsstcLblOwnerId);
				java.awt.GridBagConstraints constraintsstcLblOwnerName =
					new java.awt.GridBagConstraints();
				constraintsstcLblOwnerName.gridx = 1;
				constraintsstcLblOwnerName.gridy = 2;
				constraintsstcLblOwnerName.ipadx = 6;
				constraintsstcLblOwnerName.insets =
					new java.awt.Insets(5, 12, 2, 7);
				getJPanel2().add(
					getstcLblOwnerName(),
					constraintsstcLblOwnerName);
				java.awt.GridBagConstraints constraintstxtOwnerId =
					new java.awt.GridBagConstraints();
				constraintstxtOwnerId.gridx = 2;
				constraintstxtOwnerId.gridy = 1;
				constraintstxtOwnerId.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtOwnerId.weightx = 1.0;
				constraintstxtOwnerId.ipadx = 111;
				constraintstxtOwnerId.insets =
					new java.awt.Insets(7, 7, 5, 48);
				getJPanel2().add(
					gettxtOwnerId(),
					constraintstxtOwnerId);
				java.awt.GridBagConstraints constraintstxtOwnrTtlName1 =
					new java.awt.GridBagConstraints();
				constraintstxtOwnrTtlName1.gridx = 1;
				constraintstxtOwnrTtlName1.gridy = 3;
				constraintstxtOwnrTtlName1.gridwidth = 2;
				constraintstxtOwnrTtlName1.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtOwnrTtlName1.weightx = 1.0;
				constraintstxtOwnrTtlName1.ipadx = 247;
				constraintstxtOwnrTtlName1.insets =
					new java.awt.Insets(2, 12, 3, 9);
				getJPanel2().add(
					gettxtOwnerName1(),
					constraintstxtOwnrTtlName1);
				java.awt.GridBagConstraints constraintstxtOwnrTtlName2 =
					new java.awt.GridBagConstraints();
				constraintstxtOwnrTtlName2.gridx = 1;
				constraintstxtOwnrTtlName2.gridy = 4;
				constraintstxtOwnrTtlName2.gridwidth = 2;
				constraintstxtOwnrTtlName2.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtOwnrTtlName2.weightx = 1.0;
				constraintstxtOwnrTtlName2.ipadx = 247;
				constraintstxtOwnrTtlName2.insets =
					new java.awt.Insets(4, 12, 7, 9);
				getJPanel2().add(
					gettxtOwnerName2(),
					constraintstxtOwnrTtlName2);
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
		return ivjJPanel2;
	}

	/**
	 * Return the JPanel3 property value.
	 * 
	 * @return javax.swing.JPanel
	 */

	private javax.swing.JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new javax.swing.JPanel();
				ivjJPanel3.setName("JPanel3");
				ivjJPanel3.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjJPanel3.setLayout(new java.awt.GridBagLayout());
				ivjJPanel3.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel3.setBounds(20, 132, 557, 112);
				ivjJPanel3.setMinimumSize(
					new java.awt.Dimension(457, 124));
				java.awt.GridBagConstraints constraintsstcLblYrMake =
					new java.awt.GridBagConstraints();
				constraintsstcLblYrMake.gridx = 1;
				constraintsstcLblYrMake.gridy = 1;
				constraintsstcLblYrMake.ipadx = 9;
				constraintsstcLblYrMake.insets =
					new java.awt.Insets(15, 76, 4, 6);
				getJPanel3().add(
					getstcLblYrMake(),
					constraintsstcLblYrMake);
				java.awt.GridBagConstraints constraintsstcLblBodyStyle =
					new java.awt.GridBagConstraints();
				constraintsstcLblBodyStyle.gridx = 1;
				constraintsstcLblBodyStyle.gridy = 2;
				constraintsstcLblBodyStyle.ipadx = 1;
				constraintsstcLblBodyStyle.insets =
					new java.awt.Insets(5, 85, 4, 6);
				getJPanel3().add(
					getstcLblBodyStyle(),
					constraintsstcLblBodyStyle);
				java.awt.GridBagConstraints constraintsstcLblModel =
					new java.awt.GridBagConstraints();
				constraintsstcLblModel.gridx = 1;
				constraintsstcLblModel.gridy = 3;
				constraintsstcLblModel.ipadx = 14;
				constraintsstcLblModel.insets =
					new java.awt.Insets(5, 97, 4, 6);
				getJPanel3().add(
					getstcLblModel(),
					constraintsstcLblModel);
				java.awt.GridBagConstraints constraintsstcLblVin =
					new java.awt.GridBagConstraints();
				constraintsstcLblVin.gridx = 1;
				constraintsstcLblVin.gridy = 4;
				constraintsstcLblVin.ipadx = 27;
				constraintsstcLblVin.insets =
					new java.awt.Insets(5, 99, 14, 6);
				getJPanel3().add(getstcLblVin(), constraintsstcLblVin);
				java.awt.GridBagConstraints constraintstxtVehModlYr =
					new java.awt.GridBagConstraints();
				constraintstxtVehModlYr.gridx = 2;
				constraintstxtVehModlYr.gridy = 1;
				constraintstxtVehModlYr.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtVehModlYr.weightx = 1.0;
				constraintstxtVehModlYr.ipadx = 44;
				constraintstxtVehModlYr.insets =
					new java.awt.Insets(12, 6, 1, 14);
				getJPanel3().add(
					gettxtVehModlYr(),
					constraintstxtVehModlYr);
				java.awt.GridBagConstraints constraintstxtVehMk =
					new java.awt.GridBagConstraints();
				constraintstxtVehMk.gridx = 3;
				constraintstxtVehMk.gridy = 1;
				constraintstxtVehMk.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtVehMk.weightx = 1.0;
				constraintstxtVehMk.ipadx = 63;
				constraintstxtVehMk.insets =
					new java.awt.Insets(12, 15, 1, 253);
				getJPanel3().add(gettxtVehMk(), constraintstxtVehMk);
				java.awt.GridBagConstraints constraintstxtVehBdyType =
					new java.awt.GridBagConstraints();
				constraintstxtVehBdyType.gridx = 2;
				constraintstxtVehBdyType.gridy = 2;
				constraintstxtVehBdyType.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtVehBdyType.weightx = 1.0;
				constraintstxtVehBdyType.ipadx = 44;
				constraintstxtVehBdyType.insets =
					new java.awt.Insets(2, 6, 1, 14);
				getJPanel3().add(
					gettxtVehBdyType(),
					constraintstxtVehBdyType);
				java.awt.GridBagConstraints constraintstxtVehModl =
					new java.awt.GridBagConstraints();
				constraintstxtVehModl.gridx = 2;
				constraintstxtVehModl.gridy = 3;
				constraintstxtVehModl.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtVehModl.weightx = 1.0;
				constraintstxtVehModl.ipadx = 44;
				constraintstxtVehModl.insets =
					new java.awt.Insets(2, 6, 1, 14);
				getJPanel3().add(
					gettxtVehModl(),
					constraintstxtVehModl);
				java.awt.GridBagConstraints constraintstxtVIN =
					new java.awt.GridBagConstraints();
				constraintstxtVIN.gridx = 2;
				constraintstxtVIN.gridy = 4;
				constraintstxtVIN.gridwidth = 2;
				constraintstxtVIN.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtVIN.weightx = 1.0;
				constraintstxtVIN.ipadx = 220;
				constraintstxtVIN.insets =
					new java.awt.Insets(2, 6, 11, 173);
				getJPanel3().add(gettxtVIN(), constraintstxtVIN);
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
		return ivjJPanel3;
	}

	/**
	 * Return the JPanel31 property value.
	 * 
	 * @return javax.swing.JPanel
	 */

	private javax.swing.JPanel getJPanel31()
	{
		if (ivjJPanel31 == null)
		{
			try
			{
				ivjJPanel31 = new javax.swing.JPanel();
				ivjJPanel31.setName("JPanel31");
				ivjJPanel31.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjJPanel31.setLayout(new java.awt.GridBagLayout());
				ivjJPanel31.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel31.setBounds(23, 252, 264, 85);
				ivjJPanel31.setMinimumSize(
					new java.awt.Dimension(222, 75));
				java.awt.GridBagConstraints constraintsstcLblEmpty =
					new java.awt.GridBagConstraints();
				constraintsstcLblEmpty.gridx = 1;
				constraintsstcLblEmpty.gridy = 1;
				constraintsstcLblEmpty.ipadx = 63;
				constraintsstcLblEmpty.insets =
					new java.awt.Insets(13, 20, 3, 10);
				getJPanel31().add(
					getstcLblEmpty(),
					constraintsstcLblEmpty);
				java.awt.GridBagConstraints constraintsstcLblCapacity =
					new java.awt.GridBagConstraints();
				constraintsstcLblCapacity.gridx = 1;
				constraintsstcLblCapacity.gridy = 2;
				constraintsstcLblCapacity.ipadx = 57;
				constraintsstcLblCapacity.insets =
					new java.awt.Insets(4, 12, 4, 10);
				getJPanel31().add(
					getstcLblCapacity(),
					constraintsstcLblCapacity);
				java.awt.GridBagConstraints constraintslblCapacity =
					new java.awt.GridBagConstraints();
				constraintslblCapacity.gridx = 2;
				constraintslblCapacity.gridy = 2;
				constraintslblCapacity.ipadx = 48;
				constraintslblCapacity.ipady = 6;
				constraintslblCapacity.insets =
					new java.awt.Insets(1, 10, 1, 68);
				getJPanel31().add(
					getlblCapacity(),
					constraintslblCapacity);
				java.awt.GridBagConstraints constraintsstcLblGross =
					new java.awt.GridBagConstraints();
				constraintsstcLblGross.gridx = 1;
				constraintsstcLblGross.gridy = 3;
				constraintsstcLblGross.ipadx = 68;
				constraintsstcLblGross.insets =
					new java.awt.Insets(4, 16, 15, 10);
				getJPanel31().add(
					getstcLblGross(),
					constraintsstcLblGross);
				java.awt.GridBagConstraints constraintslblGross =
					new java.awt.GridBagConstraints();
				constraintslblGross.gridx = 2;
				constraintslblGross.gridy = 3;
				constraintslblGross.ipadx = 27;
				constraintslblGross.ipady = 6;
				constraintslblGross.insets =
					new java.awt.Insets(1, 10, 12, 68);
				getJPanel31().add(getlblGross(), constraintslblGross);
				java.awt.GridBagConstraints constraintstxtVehEmptyWt =
					new java.awt.GridBagConstraints();
				constraintstxtVehEmptyWt.gridx = 2;
				constraintstxtVehEmptyWt.gridy = 1;
				constraintstxtVehEmptyWt.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtVehEmptyWt.weightx = 1.0;
				constraintstxtVehEmptyWt.ipadx = 51;
				constraintstxtVehEmptyWt.insets =
					new java.awt.Insets(10, 10, 0, 68);
				getJPanel31().add(
					gettxtVehEmptyWt(),
					constraintstxtVehEmptyWt);
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
		return ivjJPanel31;
	}

	/**
	 * Return the JPanel7 property value.
	 * 
	 * @return javax.swing.JPanel
	 */

	private javax.swing.JPanel getJPanel7()
	{
		if (ivjJPanel7 == null)
		{
			try
			{
				ivjJPanel7 = new javax.swing.JPanel();
				ivjJPanel7.setName("JPanel7");
				ivjJPanel7.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						SEL_TIRE_TYPE));
				ivjJPanel7.setLayout(new java.awt.GridBagLayout());
				ivjJPanel7.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel7.setBounds(305, 252, 272, 85);
				ivjJPanel7.setMinimumSize(
					new java.awt.Dimension(230, 97));
				java.awt.GridBagConstraints constraintsradioPneumatic =
					new java.awt.GridBagConstraints();
				constraintsradioPneumatic.gridx = 1;
				constraintsradioPneumatic.gridy = 1;
				constraintsradioPneumatic.ipadx = 22;
				constraintsradioPneumatic.insets =
					new java.awt.Insets(9, 55, 4, 55);
				getJPanel7().add(
					getradioPneumatic(),
					constraintsradioPneumatic);
				java.awt.GridBagConstraints constraintsradioSolid =
					new java.awt.GridBagConstraints();
				constraintsradioSolid.gridx = 1;
				constraintsradioSolid.gridy = 2;
				constraintsradioSolid.ipadx = 55;
				constraintsradioSolid.insets =
					new java.awt.Insets(5, 55, 9, 55);
				getJPanel7().add(
					getradioSolid(),
					constraintsradioSolid);
				// user code begin {1}
				// defect 7894
				// Changed ButtonGroup to RTSButtonGroup
				RTSButtonGroup laBtnGrp = new RTSButtonGroup();
				laBtnGrp.add(getradioPneumatic());
				laBtnGrp.add(getradioSolid());
				// end defect 7894
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjJPanel7;
	}

	/**
	 * Return the lblCapacity property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getlblCapacity()
	{
		if (ivjlblCapacity == null)
		{
			try
			{
				ivjlblCapacity = new javax.swing.JLabel();
				ivjlblCapacity.setName("lblCapacity");
				ivjlblCapacity.setText("");
				ivjlblCapacity.setMaximumSize(
					new java.awt.Dimension(7, 14));
				ivjlblCapacity.setMinimumSize(
					new java.awt.Dimension(7, 14));
				ivjlblCapacity.setHorizontalAlignment(4);
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
		return ivjlblCapacity;
	}

	/**
	 * Return the lblGross property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getlblGross()
	{
		if (ivjlblGross == null)
		{
			try
			{
				ivjlblGross = new javax.swing.JLabel();
				ivjlblGross.setName("lblGross");
				ivjlblGross.setText("");
				ivjlblGross.setMaximumSize(
					new java.awt.Dimension(28, 14));
				ivjlblGross.setMinimumSize(
					new java.awt.Dimension(28, 14));
				ivjlblGross.setHorizontalAlignment(4);
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
		return ivjlblGross;
	}

	/**
	 * Get Original Vehicle Inquiry Data
	 *  
	 * @return VehicleInquiryData
	 */
	private VehicleInquiryData getOrigVehInqData()
	{
		return caOrigVehInqData;
	}

	/**
	 * Return the radioPneumatic property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private javax.swing.JRadioButton getradioPneumatic()
	{
		if (ivjradioPneumatic == null)
		{
			try
			{
				ivjradioPneumatic = new javax.swing.JRadioButton();
				ivjradioPneumatic.setName("radioPneumatic");
				ivjradioPneumatic.setMnemonic(KeyEvent.VK_N);
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
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioPneumatic;
	}

	/**
	 * Return the radioSolid property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */

	private javax.swing.JRadioButton getradioSolid()
	{
		if (ivjradioSolid == null)
		{
			try
			{
				ivjradioSolid = new javax.swing.JRadioButton();
				ivjradioSolid.setName("radioSolid");
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
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjradioSolid;
	}

	/**
	 * Return the stcLblBodyStyle property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblBodyStyle()
	{
		if (ivjstcLblBodyStyle == null)
		{
			try
			{
				ivjstcLblBodyStyle = new javax.swing.JLabel();
				ivjstcLblBodyStyle.setName("stcLblBodyStyle");
				ivjstcLblBodyStyle.setText(BODY_STYLE);
				ivjstcLblBodyStyle.setMaximumSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblBodyStyle.setMinimumSize(
					new java.awt.Dimension(62, 14));
				ivjstcLblBodyStyle.setHorizontalAlignment(4);
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
		return ivjstcLblBodyStyle;
	}

	/**
	 * Return the stcLblCapacity property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblCapacity()
	{
		if (ivjstcLblCapacity == null)
		{
			try
			{
				ivjstcLblCapacity = new javax.swing.JLabel();
				ivjstcLblCapacity.setName("stcLblCapacity");
				ivjstcLblCapacity.setText(CARRY_CAP);
				ivjstcLblCapacity.setMaximumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblCapacity.setMinimumSize(
					new java.awt.Dimension(52, 14));
				ivjstcLblCapacity.setHorizontalAlignment(4);
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
		return ivjstcLblCapacity;
	}

	/**
	 * Return the stcLblEmpty property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblEmpty()
	{
		if (ivjstcLblEmpty == null)
		{
			try
			{
				ivjstcLblEmpty = new javax.swing.JLabel();
				ivjstcLblEmpty.setName("stcLblEmpty");
				ivjstcLblEmpty.setText(EMPTY_WT);
				ivjstcLblEmpty.setMaximumSize(
					new java.awt.Dimension(38, 14));
				ivjstcLblEmpty.setMinimumSize(
					new java.awt.Dimension(38, 14));
				ivjstcLblEmpty.setHorizontalAlignment(4);
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
		return ivjstcLblEmpty;
	}

	/**
	 * Return the stcLblExpMoYr property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblExpMoYr()
	{
		if (ivjstcLblExpMoYr == null)
		{
			try
			{
				ivjstcLblExpMoYr = new javax.swing.JLabel();
				ivjstcLblExpMoYr.setName("stcLblExpMoYr");
				ivjstcLblExpMoYr.setText(EXP_MOYR);
				ivjstcLblExpMoYr.setMaximumSize(
					new java.awt.Dimension(59, 14));
				ivjstcLblExpMoYr.setMinimumSize(
					new java.awt.Dimension(59, 14));
				ivjstcLblExpMoYr.setHorizontalAlignment(4);
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
		return ivjstcLblExpMoYr;
	}

	/**
	 * Return the stcLblGross property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblGross()
	{
		if (ivjstcLblGross == null)
		{
			try
			{
				ivjstcLblGross = new javax.swing.JLabel();
				ivjstcLblGross.setName("stcLblGross");
				ivjstcLblGross.setText(GROSS_WT);
				ivjstcLblGross.setMaximumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblGross.setMinimumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblGross.setHorizontalAlignment(4);
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
		return ivjstcLblGross;
	}

	/**
	 * Return the stcLblModel property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblModel()
	{
		if (ivjstcLblModel == null)
		{
			try
			{
				ivjstcLblModel = new javax.swing.JLabel();
				ivjstcLblModel.setName("stcLblModel");
				ivjstcLblModel.setText(MODEL);
				ivjstcLblModel.setMaximumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblModel.setMinimumSize(
					new java.awt.Dimension(37, 14));
				ivjstcLblModel.setHorizontalAlignment(4);
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
		return ivjstcLblModel;
	}

	/**
	 * Return the stcLblOwnerId property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblOwnerId()
	{
		if (ivjstcLblOwnerId == null)
		{
			try
			{
				ivjstcLblOwnerId = new javax.swing.JLabel();
				ivjstcLblOwnerId.setName("stcLblOwnerId");
				ivjstcLblOwnerId.setText(OWNR_ID);
				ivjstcLblOwnerId.setMaximumSize(
					new java.awt.Dimension(54, 14));
				ivjstcLblOwnerId.setMinimumSize(
					new java.awt.Dimension(54, 14));
				ivjstcLblOwnerId.setHorizontalAlignment(4);
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
		return ivjstcLblOwnerId;
	}

	/**
	 * Return the stcLblOwnerName property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblOwnerName()
	{
		if (ivjstcLblOwnerName == null)
		{
			try
			{
				ivjstcLblOwnerName = new javax.swing.JLabel();
				ivjstcLblOwnerName.setName("stcLblOwnerName");
				ivjstcLblOwnerName.setText(OWNR_NAME);
				ivjstcLblOwnerName.setMaximumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblOwnerName.setMinimumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblOwnerName.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
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
		return ivjstcLblOwnerName;
	}

	/**
	 * Return the stcLblPlateNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblPlateNo()
	{
		if (ivjstcLblPlateNo == null)
		{
			try
			{
				ivjstcLblPlateNo = new javax.swing.JLabel();
				ivjstcLblPlateNo.setName("stcLblPlateNo");
				ivjstcLblPlateNo.setText(PLATE_NO);
				ivjstcLblPlateNo.setMaximumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblPlateNo.setMinimumSize(
					new java.awt.Dimension(50, 14));
				ivjstcLblPlateNo.setHorizontalAlignment(4);
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
		return ivjstcLblPlateNo;
	}

	/**
	 * Return the stcLblVin property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblVin()
	{
		if (ivjstcLblVin == null)
		{
			try
			{
				ivjstcLblVin = new javax.swing.JLabel();
				ivjstcLblVin.setName("stcLblVin");
				ivjstcLblVin.setText(VIN);
				ivjstcLblVin.setMaximumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVin.setMinimumSize(
					new java.awt.Dimension(22, 14));
				ivjstcLblVin.setHorizontalAlignment(4);
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
		return ivjstcLblVin;
	}

	/**
	 * Return the stcLblYrMake property value.
	 * 
	 * @return javax.swing.JLabel
	 */

	private javax.swing.JLabel getstcLblYrMake()
	{
		if (ivjstcLblYrMake == null)
		{
			try
			{
				ivjstcLblYrMake = new javax.swing.JLabel();
				ivjstcLblYrMake.setName("stcLblYrMake");
				ivjstcLblYrMake.setText(YEAR_MAKE);
				ivjstcLblYrMake.setMaximumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYrMake.setMinimumSize(
					new java.awt.Dimension(63, 14));
				ivjstcLblYrMake.setHorizontalAlignment(4);
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
		return ivjstcLblYrMake;
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
				//ivjtxtExpMo.setInput(1);
				ivjtxtExpMo.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtExpMo.setManagingFocus(false);
				ivjtxtExpMo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtExpMo.setMaxLength(2);
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
		return ivjtxtExpMo;
	}

	/**
	 * Return the txtExpYr property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtExpYr()
	{
		if (ivjtxtExpYr == null)
		{
			try
			{
				ivjtxtExpYr = new RTSInputField();
				ivjtxtExpYr.setName("txtExpYr");
				//ivjtxtExpYr.setInput(1);
				ivjtxtExpYr.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtExpYr.setManagingFocus(false);
				ivjtxtExpYr.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtExpYr.setMaxLength(4);
				// user code begin {1}
				ivjtxtExpYr.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtExpYr;
	}

	/**
	 * Return the txtOwnerId property value.
	 * 
	 * @return RTSInputField
	 */

	private RTSInputField gettxtOwnerId()
	{
		if (ivjtxtOwnerId == null)
		{
			try
			{
				ivjtxtOwnerId = new RTSInputField();
				ivjtxtOwnerId.setName("txtOwnerId");
				ivjtxtOwnerId.setManagingFocus(false);
				ivjtxtOwnerId.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerId.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerId.setMaxLength(
					CommonConstant.LENGTH_OWNERID);
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
		return ivjtxtOwnerId;
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
				ivjtxtOwnerName1.setManagingFocus(false);
				ivjtxtOwnerName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName1.setBackground(java.awt.Color.white);
				ivjtxtOwnerName1.setEditable(true);
				ivjtxtOwnerName1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
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
		return ivjtxtOwnerName1;
	}

	/**
	 * Return the ivjtxtOwnerName1 property value.
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
				ivjtxtOwnerName2.setName("ivjtxtOwnrName2");
				ivjtxtOwnerName2.setManagingFocus(false);
				ivjtxtOwnerName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName2.setBackground(java.awt.Color.white);
				ivjtxtOwnerName2.setEditable(true);
				ivjtxtOwnerName2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName2.setMaxLength(
					CommonConstant.LENGTH_NAME);
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
		return ivjtxtOwnerName2;
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
				//ivjtxtPlateNo.setInput(6);
				ivjtxtPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPlateNo.setManagingFocus(false);
				ivjtxtPlateNo.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtPlateNo.setMaxLength(7);
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
		return ivjtxtPlateNo;
	}

	/**
	 * Return the txtVehBdyType property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehBdyType()
	{
		if (ivjtxtVehBdyType == null)
		{
			try
			{
				ivjtxtVehBdyType = new RTSInputField();
				ivjtxtVehBdyType.setName("txtVehBdyType");
				ivjtxtVehBdyType.setManagingFocus(false);
				ivjtxtVehBdyType.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehBdyType.setBackground(java.awt.Color.white);
				//ivjtxtVehBdyType.setInput(6);
				ivjtxtVehBdyType.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVehBdyType.setEditable(true);
				ivjtxtVehBdyType.setMaxLength(2);
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
		return ivjtxtVehBdyType;
	}

	/**
	 * Return the txtVehEmptyWt property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehEmptyWt()
	{
		if (ivjtxtVehEmptyWt == null)
		{
			try
			{
				ivjtxtVehEmptyWt = new RTSInputField();
				ivjtxtVehEmptyWt.setName("txtVehEmptyWt");
				ivjtxtVehEmptyWt.setManagingFocus(false);
				ivjtxtVehEmptyWt.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehEmptyWt.setText("");
				ivjtxtVehEmptyWt.setBackground(java.awt.Color.white);
				//ivjtxtVehEmptyWt.setInput(1);
				ivjtxtVehEmptyWt.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtVehEmptyWt.setEditable(true);
				ivjtxtVehEmptyWt.setHorizontalAlignment(4);
				ivjtxtVehEmptyWt.setMaxLength(6);
				// user code begin {1}
				ivjtxtVehEmptyWt.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtVehEmptyWt;
	}

	/**
	 * Return the txtVehMk property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehMk()
	{
		if (ivjtxtVehMk == null)
		{
			try
			{
				ivjtxtVehMk = new RTSInputField();
				ivjtxtVehMk.setName("txtVehMk");
				ivjtxtVehMk.setManagingFocus(false);
				ivjtxtVehMk.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehMk.setBackground(java.awt.Color.white);
				//ivjtxtVehMk.setInput(6);
				// defect 6228
				//ivjtxtVehMk.setInput(-1);
				ivjtxtVehMk.setInput(RTSInputField.DEFAULT);
				// end defect 6228
				ivjtxtVehMk.setEditable(true);
				ivjtxtVehMk.setMaxLength(4);
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
		return ivjtxtVehMk;
	}

	/**
	 * Return the txtVehModl property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVehModl()
	{
		if (ivjtxtVehModl == null)
		{
			try
			{
				ivjtxtVehModl = new RTSInputField();
				ivjtxtVehModl.setName("txtVehModl");
				ivjtxtVehModl.setManagingFocus(false);
				ivjtxtVehModl.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehModl.setBackground(java.awt.Color.white);
				//ivjtxtVehModl.setInput(6);
				ivjtxtVehModl.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVehModl.setEditable(true);
				ivjtxtVehModl.setMaxLength(3);
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
		return ivjtxtVehModl;
	}

	/**
	 * Return the txtVehModlYr property value.
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
				ivjtxtVehModlYr.setName("txtVehModlYr");
				ivjtxtVehModlYr.setManagingFocus(false);
				ivjtxtVehModlYr.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVehModlYr.setBackground(java.awt.Color.white);
				//ivjtxtVehModlYr.setInput(1);
				ivjtxtVehModlYr.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtVehModlYr.setEditable(true);
				ivjtxtVehModlYr.setMaxLength(4);
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
		return ivjtxtVehModlYr;
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
				ivjtxtVIN.setText("");
				ivjtxtVIN.setBackground(java.awt.Color.white);
				//ivjtxtVIN.setInput(6);
				ivjtxtVIN.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVIN.setEditable(true);
				ivjtxtVIN.setMaxLength(22);
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
		return ivjtxtVIN;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		RTSException aeRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		aeRTSEx.displayError(this);
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
			setName("FrmRegistrationCorrectionREG025");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(600, 425);
			setTitle(TITLE_REG025);
			setContentPane(
				getFrmRegistrationCorrectionREG025ContentPane1());
		}
		catch (Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		// user code begin {2}

		// defect 10130 
		// Only enable for HQ 
		gettxtOwnerId().setEnabled(SystemProperty.isHQ());
		getstcLblOwnerId().setEnabled(SystemProperty.isHQ());
		// end defect 10130 

		// defect 10299  
		cvAddlMFValid = new Vector();
		cvAddlMFValid.add(gettxtOwnerName1());
		cvAddlMFValid.add(gettxtOwnerName2());
		// end defect 10299 

		// user code end
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
			FrmRegistrationCorrectionREG025 laFrmRegistrationCorrectionREG025;
			laFrmRegistrationCorrectionREG025 =
				new FrmRegistrationCorrectionREG025();
			laFrmRegistrationCorrectionREG025.setModal(true);
			laFrmRegistrationCorrectionREG025
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmRegistrationCorrectionREG025.show();
			java.awt.Insets laInsets =
				laFrmRegistrationCorrectionREG025.getInsets();
			laFrmRegistrationCorrectionREG025.setSize(
				laFrmRegistrationCorrectionREG025.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmRegistrationCorrectionREG025.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmRegistrationCorrectionREG025.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/** 
	 * Method to determine if "Out of County" so that prompt for 
	 * Supervisor Override Code 
	 * 
	 * @return boolean 
	 */
	private boolean isOutofCounty()
	{
		OwnerData laOwnrData =
			caVehInqData.getMfVehicleData().getOwnerData();

		return caVehInqData
			.getMfVehicleData()
			.getRegData()
			.getResComptCntyNo()
			!= caOrigVehInqData
				.getMfVehicleData()
				.getRegData()
				.getResComptCntyNo()
			&& laOwnrData.getAddressData().getSt1().equals(
				caOrigVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getAddressData()
					.getSt1())
			&& laOwnrData.getAddressData().getSt2().equals(
				caOrigVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getAddressData()
					.getSt2())
			&& laOwnrData.getAddressData().getCity().equals(
				caOrigVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getAddressData()
					.getCity())
			&& laOwnrData.getAddressData().getState().equals(
				caOrigVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getAddressData()
					.getState());
	}

	/**
	 * Check for soft stops.
	 * 
	 * @return boolean true if 'Enter' is selected in Supervisor Override
	 */
	private boolean procsSoftStops()
	{
		boolean lbReturn = true;

		// Test if workstation is located not at HEADQUARTERS
		if (caVehInqData.getMfDown() == 0
			&& !SystemProperty.isHQ()
			&& (caVehInqData.getVehMiscData().getSupvOvride() == null
				|| caVehInqData.getVehMiscData().getSupvOvride().length()
					== 0))
		{
			// if soft stops exist
			// if indiStopCd == "S"
			Vector lvIndis =
				IndicatorLookup.getIndicators(
					caVehInqData.getMfVehicleData(),
					caRegValidData.getTransCode(),
					IndicatorLookup.SCREEN);

			// defect 7860 
			String lsSoftStopReasons = CommonConstant.STR_SPACE_EMPTY;

			if (isOutofCounty())
			{
				lsSoftStopReasons =
					RegistrationConstant.OUT_OF_COUNTY + "\n";
			}

			// defect 9821 
			// if ciVehGrossWt < caRegValidData.getOrigVehGrossWt()
			if (ciVehGrossWt
				< caOrigVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVehEmptyWt()
					+ caOrigVehInqData
						.getMfVehicleData()
						.getRegData()
						.getVehCaryngCap())
			{
				// end defect 9821 

				lsSoftStopReasons =
					lsSoftStopReasons + GROSS_WT_SOFT_STOP_MSG + "\n";
			}

			if (IndicatorLookup.hasSoftStop(lvIndis)
				|| lsSoftStopReasons.length() != 0)
			{
				caVehInqData.getVehMiscData().setSupvOvrideReason(
					lsSoftStopReasons
						+ IndicatorLookup.getSoftStopReasons(lvIndis));

				// Present Screen for Supervisor Override  		
				getController().processData(
					VCRegistrationCorrectionREG025.SUPV_OVRIDE,
					caVehInqData);

				if (caVehInqData.getVehMiscData().getSupvOvride()
					== null
					|| caVehInqData
						.getVehMiscData()
						.getSupvOvride()
						.length()
						== 0)
				{
					lbReturn = false;
				}
			}
			// end defect 7860 
		}
		return lbReturn;
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			// defect 10112 
			// defect 10299 
			caVehInqData =
				(VehicleInquiryData) UtilityMethods.copy(aaDataObject);
			// end defect 10299 

			caRegValidData =
				(RegistrationValidationData) caVehInqData
					.getValidationObject();

			caOrigVehInqData = caRegValidData.getOrigVehInqData();
			// end defect 10112 

			gettxtPlateNo().setText(
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegPltNo());

			// defect 9085 		
			// Disable PlateNo if Special Plate
			if (caVehInqData.getMfVehicleData().getSpclPltRegisData()
				!= null)
			{
				cbSpclPlt = true;
				gettxtPlateNo().setEnabled(false);
			}
			// end defect 9085 

			// defect 9670 
			// Disable Plate No Field if plate removed (displays "NOPLATE")
			else if (
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getPltRmvCd()
					!= 0)
			{
				gettxtPlateNo().setEnabled(false);
			}
			// end defect 9670 

			// defect 10425 
			// Merge Issue  
			//			// defect 9670 
			//			// Disable Plate No Field if plate removed (displays "NOPLATE")
			//			else if (
			//				caVehInqData
			//					.getMfVehicleData()
			//					.getRegData()
			//					.getPltRmvCd()
			//					!= 0)
			//			{
			//				gettxtPlateNo().setEnabled(false);
			//			}
			//			// end defect 9670 
			//
			//			// defect 9670 
			//			// Disable Plate No Field if plate removed (displays "NOPLATE")
			//			else if (
			//				caVehInqData
			//					.getMfVehicleData()
			//					.getRegData()
			//					.getPltRmvCd()
			//					!= 0)
			//			{
			//				gettxtPlateNo().setEnabled(false);
			//			}
			//			// end defect 9670 
			// end defect 10425 

			// defect 8900 
			// Do not populate ExpMo/Yr if Standard Exempt
			cbStandardExempt =
				CommonFeesCache.isStandardExempt(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegClassCd());
			if (!cbStandardExempt)
			{
				gettxtExpMo().setText(
					Integer.toString(
						caVehInqData
							.getMfVehicleData()
							.getRegData()
							.getRegExpMo()));
				gettxtExpYr().setText(
					Integer.toString(
						caVehInqData
							.getMfVehicleData()
							.getRegData()
							.getRegExpYr()));
			}
			// end defect 8900 
			gettxtOwnerId().setText(
				caVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getOwnrId()
					!= null
					? caVehInqData
						.getMfVehicleData()
						.getOwnerData()
						.getOwnrId()
					: "");

			// defect 10112 
			gettxtOwnerName1().setText(
				caVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getName1());

			gettxtOwnerName2().setText(
				caVehInqData
					.getMfVehicleData()
					.getOwnerData()
					.getName2());
			// end defect 10112 

			gettxtVehModlYr().setText(
				Integer.toString(
					caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getVehModlYr()));

			gettxtVehMk().setText(
				caVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVehMk());

			gettxtVehBdyType().setText(
				caVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVehBdyType());

			gettxtVehModl().setText(
				caVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVehModl());

			gettxtVIN().setText(
				caVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVin()
					!= null
					? caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getVin()
					: "");

			gettxtVehEmptyWt().setText(
				Integer.toString(
					caVehInqData
						.getMfVehicleData()
						.getVehicleData()
						.getVehEmptyWt()));

			getlblCapacity().setText(
				Integer.toString(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getVehCaryngCap()));

			getlblGross().setText(
				Integer.toString(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getVehGrossWt()));

			// defect 4247
			// added the following line of code.
			ciVehGrossWt =
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getVehGrossWt();

			// Adjust fields if vehicle class is miscellaneous
			// for reg correction
			boolean lbEntryAllowed = false;

			if (caVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.getVehClassCd()
				.equals(MISC_VEH_CLASS))
			{
				lbEntryAllowed = true;
			}
			else
			{
				lbEntryAllowed = false;
			}

			// Set up owner names
			gettxtOwnerName1().setEditable(lbEntryAllowed);
			gettxtOwnerName1().setEnabled(lbEntryAllowed);
			gettxtOwnerName2().setEditable(lbEntryAllowed);
			gettxtOwnerName2().setEnabled(lbEntryAllowed);
			getstcLblOwnerName().setEnabled(lbEntryAllowed);
			// Set up vehicle year/make
			gettxtVehModlYr().setEditable(lbEntryAllowed);
			gettxtVehModlYr().setEnabled(lbEntryAllowed);
			gettxtVehMk().setEditable(lbEntryAllowed);
			gettxtVehMk().setEnabled(lbEntryAllowed);
			getstcLblYrMake().setEnabled(lbEntryAllowed);
			// Set up vehicle body style
			gettxtVehBdyType().setEditable(lbEntryAllowed);
			gettxtVehBdyType().setEnabled(lbEntryAllowed);
			getstcLblBodyStyle().setEnabled(lbEntryAllowed);
			// Set up vehicle model
			gettxtVehModl().setEditable(lbEntryAllowed);
			gettxtVehModl().setEnabled(lbEntryAllowed);
			getstcLblModel().setEnabled(lbEntryAllowed);
			// Set up VIN
			gettxtVIN().setEditable(lbEntryAllowed);
			gettxtVIN().setEnabled(lbEntryAllowed);
			getstcLblVin().setEnabled(lbEntryAllowed);
			// Set up empty weight
			gettxtVehEmptyWt().setEditable(lbEntryAllowed);
			gettxtVehEmptyWt().setEnabled(lbEntryAllowed);
			getstcLblEmpty().setEnabled(lbEntryAllowed);
			// if vehicle is fixed weight then display only
			if (caVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.getFxdWtIndi()
				== 1)
			{
				gettxtVehEmptyWt().setEditable(false);
				gettxtVehEmptyWt().setEnabled(false);
				getstcLblEmpty().setEnabled(false);
			}

			// disable reg exp date if vehicle is apportioned
			// for regis correction
			if (caVehInqData
				.getMfVehicleData()
				.getRegData()
				.getRegPltCd()
				.equals(APPORTIONED_TRUCK_PLT)
				|| caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegPltCd()
					.equals(
					APPORTIONED_TRL_PLT))
			{
				lbEntryAllowed = false;
			}
			else
			{
				lbEntryAllowed = true;
			}
			// defect 8900
			// Disable ExpMo/Yr if Standard Exempt
			lbEntryAllowed = lbEntryAllowed && !cbStandardExempt;
			// end defect 8900 
			gettxtExpMo().setEditable(lbEntryAllowed);
			gettxtExpMo().setEnabled(lbEntryAllowed);
			gettxtExpYr().setEditable(lbEntryAllowed);
			gettxtExpYr().setEnabled(lbEntryAllowed);
			getstcLblExpMoYr().setEnabled(lbEntryAllowed);

			// set default tire type
			if (caVehInqData
				.getMfVehicleData()
				.getRegData()
				.getTireTypeCd()
				== null
				|| caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getTireTypeCd()
					.equals(
					"")
				|| caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getTireTypeCd()
					.equals(
					PNEUMATIC_TIRE_TYPE))
			{
				getradioPneumatic().setSelected(true);
			}
			else if (
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getTireTypeCd()
					.equals(
					SOLID_TIRE_TYPE))
			{
				getradioSolid().setSelected(true);
			}

			// Enable/Disable tire type selection "SOLID" based on reg class
			// :Test Find(String$(MfVeh{Row}.RegClassCd,2,0),
			// " 15 21 31 32 35 36 37 38 43 46 47 49 54 57 60 61 ")
			RegistrationWeightFeesData laData =
				RegistrationWeightFeesCache.getRegWtFee(
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getRegClassCd(),
					SOLID_TIRE_TYPE,
					caVehInqData.getRTSEffDt(),
					caVehInqData
						.getMfVehicleData()
						.getRegData()
						.getVehGrossWt());

			if (laData != null)
			{
				getradioSolid().setEnabled(true);
			}
			else
			{
				getradioSolid().setEnabled(false);
			}
		}
	}

	/**
	 * Set input field values to data object.
	 * 
	 */
	private void setInputFields()
	{
		RegistrationData laRegData =
			caVehInqData.getMfVehicleData().getRegData();
		VehicleData laVehData =
			caVehInqData.getMfVehicleData().getVehicleData();
		OwnerData laOwnrData =
			caVehInqData.getMfVehicleData().getOwnerData();

		// Set value of TireTypeCd based on Tire Type buttons
		if (getradioPneumatic().isSelected())
		{
			laRegData.setTireTypeCd(PNEUMATIC_TIRE_TYPE);
		}
		else if (getradioSolid().isSelected())
		{
			laRegData.setTireTypeCd(SOLID_TIRE_TYPE);
		}
		else
		{
			laRegData.setTireTypeCd(PNEUMATIC_TIRE_TYPE);
		}
		if (gettxtPlateNo().isEnabled())
		{
			laRegData.setRegPltNo(
				gettxtPlateNo().getText().toUpperCase());
		}
		if (gettxtExpMo().isEnabled())
		{
			laRegData.setRegExpMo(
				Integer.parseInt(gettxtExpMo().getText()));
		}
		if (gettxtExpYr().isEnabled())
		{
			laRegData.setRegExpYr(
				Integer.parseInt(gettxtExpYr().getText()));
		}

		if (gettxtOwnerId().isEnabled())
		{
			laOwnrData.setOwnrId(
				gettxtOwnerId().getText().toUpperCase());
		}

		// defect 10112 
		if (gettxtOwnerName1().isEnabled())
		{
			laOwnrData.setName1(
				gettxtOwnerName1().getText().toUpperCase());
		}
		if (gettxtOwnerName2().isEnabled())
		{
			laOwnrData.setName2(
				gettxtOwnerName2().getText().toUpperCase());
		}
		// end defect 10112  

		if (gettxtVehModlYr().isEnabled())
		{
			laVehData.setVehModlYr(
				Integer.parseInt(gettxtVehModlYr().getText()));
		}
		if (gettxtVehMk().isEnabled())
		{
			laVehData.setVehMk(gettxtVehMk().getText().toUpperCase());
		}
		if (gettxtVehBdyType().isEnabled())
		{
			laVehData.setVehBdyType(
				gettxtVehBdyType().getText().toUpperCase());
		}
		if (gettxtVehModl().isEnabled())
		{
			laVehData.setVehModl(
				gettxtVehModl().getText().toUpperCase());
		}
		if (gettxtVIN().isEnabled())
		{
			laVehData.setVin(gettxtVIN().getText().toUpperCase());
		}
		if (gettxtVehEmptyWt().isEnabled())
		{
			laVehData.setVehEmptyWt(
				Integer.parseInt(gettxtVehEmptyWt().getText()));
		}
		// test if reg exp mo or year has changed
		if ((laRegData.getRegExpMo()
			!= caRegValidData.getOrigRegExpMo())
			|| (laRegData.getRegExpYr()
				!= caRegValidData.getOrigRegExpYr()))
		{
			new RTSException(572).displayError(this);
		}
		// defect 9864 
		// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
		// defect 9261 
		// Copy Exp Mo/Yr to Special Plate Regis Data
		// defect 9689
		// Do not copy (synch) ExpMo\Yr for Vendor Plates 
		if (cbSpclPlt
			&& !PlateTypeCache.isVendorPlate(laRegData.getRegPltCd()))
		{
			// end defect 9689
			caVehInqData
				.getMfVehicleData()
				.getSpclPltRegisData()
				.setPltExpMo(
				laRegData.getRegExpMo());
			caVehInqData
				.getMfVehicleData()
				.getSpclPltRegisData()
				.setPltExpYr(
				laRegData.getRegExpYr());
		}
		// end defect 9261 
		// end defect 9864 
	}
	
	/**
	 * Input fields validations.
	 * 
	 * @return boolean
	 */
	private boolean validateInputFields()
	{
		// defect 10299 
		// Add UtilityMethods.trimRTSInputField()
		// Refactor aeRTSEx to leRTSEx 
		// Implement validation for MF Characters 
		UtilityMethods.trimRTSInputField(this);

		RTSException leRTSEx = new RTSException();
		// end defect 10299 

		// defect 8900 
		// Do not validate Exp Mo/Yr if Standard Exempt
		if (!cbStandardExempt)
		{
			// expiration month validation
			String lsExpMo = gettxtExpMo().getText();
			if (lsExpMo == null || lsExpMo.equals(""))
			{
				leRTSEx.addException(
					new RTSException(
						RTSException.WARNING_MESSAGE,
						MiscellaneousRegConstant
							.INVALID_EXP_MONTH_ENTRY,
						MiscellaneousRegConstant.ERROR_TITLE),
					gettxtExpMo());
			}
			else
			{
				int liExpMo = Integer.parseInt(lsExpMo);
				if (liExpMo < 1 || liExpMo > 12)
				{
					leRTSEx.addException(
						new RTSException(
							RTSException.WARNING_MESSAGE,
							MiscellaneousRegConstant
								.INVALID_EXP_MONTH_ENTRY,
							MiscellaneousRegConstant.ERROR_TITLE),
						gettxtExpMo());
				}
				// expiration year validation: 1. check if year range is
				// invalid; 2. test RegExpYr has not been set back
				if (RegistrationClientUtilityMethods
					.isInvalidRegExpYr(gettxtExpYr().getText()))
				{
					leRTSEx.addException(
						new RTSException(
							RTSException.WARNING_MESSAGE,
							MiscellaneousRegConstant
								.INVALID_EXP_YEAR_ENTRY,
							MiscellaneousRegConstant.ERROR_TITLE),
						gettxtExpYr());
				}
				else
				{
					int liExpYr =
						Integer.parseInt(gettxtExpYr().getText());

					if (liExpYr
						< getOrigVehInqData()
							.getMfVehicleData()
							.getRegData()
							.getRegExpYr())
					{
						leRTSEx.addException(
							new RTSException(728),
							gettxtExpYr());
					}
				}
			}
		}
		// end defect 8900

		// owner id value must be 9 chars in length
		// defect 10127 
		if (!gettxtOwnerId().isEmpty()
			&& gettxtOwnerId().getText().length()
				< CommonConstant.LENGTH_OWNERID)
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtOwnerId());
		}
		// Add missing validation for MISC 
		if (gettxtOwnerName1().isEnabled()
			&& gettxtOwnerName1().isEmpty())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtOwnerName1());
		}
		// end defect 10127 

		// defect 10299

		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvAddlMFValid,
			leRTSEx);
		// end defect 10299 

		// defect 6411 
		// Edit Model Year for LEN 4
		if (gettxtVehModlYr().isEnabled())
		{
			// model year must be 4 char length
			// defect 6872
			// Year must be >1880 and < Today + 3 years
			//if (gettxtVehModlYr().getText().trim().length() < 4
			if (CommonValidations
				.isInvalidYearModel(gettxtVehModlYr().getText().trim()))
			{
				leRTSEx.addException(
					new RTSException(2006),
					gettxtVehModlYr());
			}
			// end defect 6872
		}
		// end defect 6411	
		if (gettxtVehBdyType().isEnabled())
		{
			// body style must be 2 char length
			if (gettxtVehBdyType().getText().trim().length() < 2)
			{
				// defect 10127
				// Use ErrorConstant vs. "hard copy" 
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtVehBdyType());
				// end defect 10127 
			}
		}
		// defect 10959 
		int liGrossWt = 0;
		int liRegClassCd = caVehInqData.getMfVehicleData().getRegData().getRegClassCd();
		int liEmptyWt = 0;
		try
		{
			liEmptyWt = Integer.parseInt(gettxtVehEmptyWt().getText()); 
		}
		catch (NumberFormatException aeNFEx)
		{
			
		}
		
		if (gettxtVehEmptyWt().isEnabled())
		{
			if  (gettxtVehEmptyWt().isEmpty() || liEmptyWt == 0)
			{
				leRTSEx.addException(new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID), gettxtVehEmptyWt()); 
			}
			else
			{
				calcGrossWt(leRTSEx); 
				
				liGrossWt = Integer.parseInt(getlblGross().getText());

				try
				{
					TitleClientUtilityMethods.validateGrossWtForRegClassCd(liRegClassCd,liGrossWt); 
				}
				catch (RTSException aeRTSEx)
				{
					leRTSEx.addException(aeRTSEx, gettxtVehEmptyWt()); 
				}
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return false;
				}
			}
		}
		// end defect 10959 
		
		// Set local value of TireTypeCd based on Tire Type buttons
		String lsTireTypeCd = "";
		if (getradioPneumatic().isSelected())
		{
			lsTireTypeCd = PNEUMATIC_TIRE_TYPE;
		}
		else
		{
			lsTireTypeCd = SOLID_TIRE_TYPE;
		}
		boolean lbFieldUpdated = false;

		// display error if none of the following fields has been updated
		if (cbSpclPlt)
		{
			SpecialPlatesRegisData laOrigSpclPlt =
				caOrigVehInqData
					.getMfVehicleData()
					.getSpclPltRegisData();

			SpecialPlatesRegisData laModSpclPlt =
				caVehInqData.getMfVehicleData().getSpclPltRegisData();

			if (UtilityMethods
				.isAllZeros(laOrigSpclPlt.getPltOwnrPhoneNo()))
			{
				laOrigSpclPlt.setPltOwnrPhoneNo("");
			}
			if (UtilityMethods
				.isAllZeros(laOrigSpclPlt.getOwnrData().getOwnrId()))
			{
				laOrigSpclPlt.getOwnrData().setOwnrId("");
			}
			if (UtilityMethods
				.isAllZeros(laModSpclPlt.getPltOwnrPhoneNo()))
			{
				laModSpclPlt.setPltOwnrPhoneNo("");
			}
			if (UtilityMethods
				.isAllZeros(laModSpclPlt.getOwnrData().getOwnrId()))
			{
				laModSpclPlt.getOwnrData().setOwnrId("");
			}
			// Determine change in one of the following: 
			//  E-Mail
			//  Phone
			//  OwnerId
			//  Reg Exp Mo
			//  Reg Exp Yr
			//  Additional Set

			// defect 9864 
			// Modified in refactor of RegExpMo/Yr to PltExpMo/Yr  
			if (!laOrigSpclPlt
				.getPltOwnrEMail()
				.equals(laModSpclPlt.getPltOwnrEMail())
				|| !laOrigSpclPlt.getPltOwnrPhoneNo().equals(
					laModSpclPlt.getPltOwnrPhoneNo())
				|| !laOrigSpclPlt.getOwnrData().getOwnrId().equals(
					laModSpclPlt.getOwnrData().getOwnrId())
				|| laOrigSpclPlt.getPltExpMo()
					!= laModSpclPlt.getPltExpMo()
				|| laOrigSpclPlt.getPltExpYr()
					!= laModSpclPlt.getPltExpYr()
				|| laOrigSpclPlt.getAddlSetIndi()
					!= laModSpclPlt.getAddlSetIndi())
			{
				// end defect 9864 
				lbFieldUpdated = true;
			}
		}

		if (!lbFieldUpdated
			&& caRegValidData.getAddlToknFeeIndi() == 0
			&& caVehInqData
				.getMfVehicleData()
				.getVehicleData()
				.getDieselIndi()
				== caRegValidData.getOrigDieselIndi()
			&& gettxtPlateNo().getText().equals(
				caRegValidData.getOrigRegPltNo())
			// PCR 34 comments removed.  10/7/06  kph
			&& gettxtOwnerId().getText().equals(
				caRegValidData.getOrigOwnerData().getOwnrId())
			// defect 10112 
			&& gettxtOwnerName1().getText().equals(
				caRegValidData.getOrigOwnerData().getName1())
			&& gettxtOwnerName2().getText().equals(
				caRegValidData
					.getOrigOwnerData()
					.getName2()) // end defect 10112 
		// defect 8900
		// do not compare ExpMo/Yr if Standard Exempt 
			&& (cbStandardExempt
				|| (Integer.parseInt(gettxtExpMo().getText())
					== caRegValidData.getOrigRegExpMo()
					&& Integer.parseInt(gettxtExpYr().getText())
						== caRegValidData.getOrigRegExpYr()))
			// end defect 8900 
			&& Integer.parseInt(gettxtVehModlYr().getText())
				== caRegValidData.getOrigVehModlYr()
			&& gettxtVehMk().getText().equals(
				caRegValidData.getOrigVehMk())
			&& gettxtVehBdyType().getText().equals(
				caRegValidData.getOrigVehBdyType())
			&& gettxtVehModl().getText().equals(
				caRegValidData.getOrigVehModl())
			&& gettxtVIN().getText().equals(caRegValidData.getOrigVIN())
			// defect 10959 
			&& liEmptyWt
			//&& Integer.parseInt(gettxtVehEmptyWt().getText())
				== caRegValidData.getOrigVehEmptyWt()
			// end defect 10959 
			&& lsTireTypeCd.equals(caRegValidData.getOrigTireTypeCd()))
		{
			// Not Enabled for Special Plates 
			if (gettxtPlateNo().isEnabled())
			{
				leRTSEx.addException(
					new RTSException(86),
					gettxtPlateNo());
			}
			else
			{
				leRTSEx.addException(
					new RTSException(86),
					gettxtExpMo());
			}
			lbFieldUpdated = false;
		}
		// calc gross weight from empty weight and do weight validations.
		if (gettxtVehEmptyWt().isEnabled())
		{
			calcGrossWt(leRTSEx);
		}

		// Determine if Ton Req'd
		RegistrationClassData laRegClassObject =
			RegistrationClassCache.getRegisClass(
				caVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVehClassCd(),
				caVehInqData
					.getMfVehicleData()
					.getRegData()
					.getRegClassCd(),
				RTSDate.getCurrentDate().getYYYYMMDDDate());

		// Validate gross weight for fixed weight vehicles
		// Test if vehicle is NOT fixed weight
		if (caVehInqData
			.getMfVehicleData()
			.getVehicleData()
			.getFxdWtIndi()
			== 1
			&& laRegClassObject.getTonReqd() == 1)
		{
			Dollar laVehTon =
				caVehInqData
					.getMfVehicleData()
					.getVehicleData()
					.getVehTon();
			CommercialVehicleWeightsData laCommVehWts =
				CommercialVehicleWeightsCache.getCommVehWts(laVehTon);
			if (laCommVehWts == null)
			{
				leRTSEx.addException(
					new RTSException(201),
					getFrmRegistrationCorrectionREG025ContentPane1());
			}
			// Determine if fixed weight is valid
			// defect 4247
			// Added else to the following code.
			else if (
				ciVehGrossWt < laCommVehWts.getMinGrossWtAllowble())
			{
				leRTSEx.addException(
					new RTSException(90),
					getFrmRegistrationCorrectionREG025ContentPane1());
			}
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			if (lbFieldUpdated == false)
			{
				clearAllColor(this);
			}
			return false;
		}

		if (!procsSoftStops())
		{
			return false;
		}
		caRegValidData.setEnterOnRegCorrection(true);
		return true;
	}
}
