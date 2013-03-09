package com.txdot.isd.rts.client.registration.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSButtonGroup;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.registration.business.RegistrationClientUtilityMethods;
import com.txdot.isd.rts.client.specialplates.business.SpecialPlatesClientUtilityMethods;
import com.txdot.isd.rts.client.specialplates.ui.VCSpecialPlateInformationSPL002;

import com.txdot.isd.rts.services.cache.ItemCodesCache;
import com.txdot.isd.rts.services.cache.PlateTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmPlateSelectionREG011.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove 	02/27/2007 	Class created.
 * 							defect 9126 Ver Special Plates
 * K Harrell	04/09/2997	Continued work. 
 * 							Pending: Manufacture New, Set 
 *							caCompTransData.setOwnrSuppliedPltNo(
 *							csReplRegPltNo);
 * 							defect 9085 Ver Special Plates
 * K Harrell	04/20/2007	Disable/ Make not visible Repl Plate radio 
 * 							button if Replacement same as Orig RegPltCd
 * 							modify manageSelectandSignedPanels() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Added temporary text for Help
 * 							modify actionPerformed9)
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/25/2007	Add year to descriptions for Annual Plt. 
 * 							Pass InvItmYr for Replacement request for
 * 							Annual Plt. 
 * 							defect 9085 Ver Special Plates 
 * K Harrell	01/07/2009 	Modified in refactor of SpclPltRegisData 
 *        					RegExpMo/Yr methods to PltExpMo/Yr methods.
 *        					modify handleMfgRequest(),
 * 							 setPlateTypeDescriptions()  
 *        					defect 9864 Ver Defect_POS_D
 * K Harrell	01/08/2009	Remove reference to RegisData.setOrgNo() 
 * 							modify prepInv() 
 * 							defect 9912 Ver Defect_POS_D 
 * K Harrell	07/12/2010	Disable Manufacture New Plate if Multi-Year 
 * 							Official Plate 
 * 							modify manageSelectandSignedPanels() 
 * 							defect 10507 Ver 6.5.0 
 * K Harrell	12/26/2010	modify handleMfgRequest() 
 * 							defect 10700 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */
/**
 * This frame presents the plate selections, either regular or special
 * plate during the Replacement event. It also allows the selection of a
 * manufacturing option or issue from inventory. If the user is  
 * replacing a personalized plate, a 'lost\stolen' affidavit is 
 * displayed.
 *
 * @version	6.7.0 			12/26/2010
 * @author  Kathy Harrell 
 * <br>Creation Date:		02/27/2007 11:19:00
 */
public class FrmPlateSelectionREG011
	extends RTSDialogBox
	implements ActionListener, ItemListener
{

	private JPanel FrmPlateSelectionREG011ContentPane = null;
	private ButtonPanel buttonPanel = null;

	private JPanel panelMfgOrIssueInventory = null;
	private JRadioButton radioSamePlate = null;
	private JRadioButton radioNewPlate = null;
	private JRadioButton radioIssuePlate = null;
	private JRadioButton radioRegPlate = null;
	private JRadioButton radioReplPlate = null;
	private JPanel panelSigned = null;
	private JRadioButton radioSignedNo = null;
	private JRadioButton radioSignedYes = null;
	private JPanel panelPlateType = null;
	private JLabel registeredPltDesc = null;
	private JLabel replacementPltDesc = null;

	// Objects
	private CompleteTransactionData caCompTransData = null;
	private VehicleInquiryData caOrigVehInqData = null;
	private RegistrationValidationData caRegValidData = null;
	private VehicleInquiryData caVehicleInquiryData = null;
	private VehicleInquiryData caReplVehicleInquiryData = null;
	private SpecialPlatesRegisData caOrigSpclPltRegisData = null;
	private SpecialPlatesRegisData caSpclPltRegisData = null;
	private PlateTypeData caPlateTypeData = null;
	private InventoryAllocationData caInvAllocData =
		new InventoryAllocationData();

	// String 
	private String csOrigPlateCd = new String();
	private String csReplPlateCd = new String();
	private String csReplRegPltNo = new String();
	private String csSelectedPlateCd = new String();

	// boolean  
	private boolean cbUserPltNo = false;
	private boolean cbSpclPlt = false;
	private boolean cbSamePltType = true;

	// Vector
	private Vector cvOrigInvItms = null;

	// Constants 
	private final static String ERRMSG_DATA_MISS =
		"Data missing for IsNextVCREG029";
	private final static String ERRMSG_ERROR = "ERROR";
	private final static String SELECT_ONE = "Select One:";
	private final static String ISSUE_FROM_INVENTORY =
		"Issue From Inventory";
	private final static String SIGNED_AFFIDAVIT = "Signed Affidavit:";
	private final static String MFG_SAME_PLT_NO =
		"Manufacture Same Plate No";
	private final static String MFG_NEW_PLT_NO =
		"Manufacture New Plate No";
	private final static String SAME_PLT_TYPE = "Same Plate Type:";
	private final static String NEW_PLT_TYPE = "New Plate Type:";
	private final static String TITLE_REG011 =
		"Plate Selection   REG011";

	/**
	 * FrmPlateSelectionREG011 constructor comment.
	 */

	public FrmPlateSelectionREG011()
	{
		super();
		initialize();
	}

	/**
	 * FrmPlateSelectionREG011 constructor with parent.
	 * 
	 * @param aaParent Dialog
	 */
	public FrmPlateSelectionREG011(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmPlateSelectionREG011 constructor with parent.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmPlateSelectionREG011(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs. Determines what actions to take 
	 * when Enter, Cancel, or Help are pressed.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
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

			if (aaAE.getSource() == buttonPanel.getBtnEnter())
			{
				caReplVehicleInquiryData =
					(VehicleInquiryData) UtilityMethods.copy(
						caVehicleInquiryData);

				// Copy the Special Plates Regis Data in the 
				// event the user returns and modifies 
				SpecialPlatesRegisData laSpclPltRegisData =
					(SpecialPlatesRegisData) UtilityMethods.copy(
						caSpclPltRegisData);

				handleChrgRemakeOnPLPReplace(laSpclPltRegisData);

				// populate inventory with data
				prepInv(laSpclPltRegisData);
				handleMfgRequest(laSpclPltRegisData);
				caReplVehicleInquiryData
					.getMfVehicleData()
					.setSpclPltRegisData(
					laSpclPltRegisData);
				caReplVehicleInquiryData.setValidationObject(
					caRegValidData);

				if (getRadioNewPlate().isSelected())
				{
					laSpclPltRegisData.setRequestType(
						SpecialPlatesConstant.MANUFACTURE);

					getController().processData(
						VCPlateSelectionREG011.GET_NEXT_VI_ITEM_NO,
						caReplVehicleInquiryData);
				}
				else
				{
					caCompTransData =
						RegistrationClientUtilityMethods.prepFees(
							caReplVehicleInquiryData,
							caRegValidData.getOrigVehInqData());

					MFVehicleData laMFVehData =
						caCompTransData.getVehicleInfo();

					laMFVehData.getRegData().setPrevPltNo(
						laMFVehData.getRegData().getRegPltNo());

					boolean lbIssueInv =
						getRadioIssuePlate().isSelected();

					if (!lbIssueInv)
					{
						int liRegExpYr =
							laMFVehData.getRegData().getRegExpYr();

						caCompTransData.setOwnrSuppliedPltNo(
							laSpclPltRegisData.getRegPltNo());

						laMFVehData.getRegData().setOwnrSuppliedExpYr(
							liRegExpYr);
					}

					if (!SystemProperty.isHQ()
						|| (SystemProperty.isHQ()
							&& (caCompTransData.getInvItemCount() != 0
								|| lbIssueInv)))
					{

						getController().processData(
							AbstractViewController.ENTER,
							caCompTransData);
					}
					else
					{
						RTSException leRTSEx =
							new RTSException(
								RTSException.CTL001,
								CommonConstant
									.TXT_COMPLETE_TRANS_QUESTION,
								ScreenConstant.CTL001_FRM_TITLE);
						int liResponse = leRTSEx.displayError(this);
						if (liResponse != RTSException.NO)
						{
							getController().processData(
								VCPlateSelectionREG011.ADD_TRANS,
								caCompTransData);
						}

					}

				}
			}

			else if (aaAE.getSource() == buttonPanel.getBtnCancel())
			{
				(
					(RegistrationValidationData) caVehicleInquiryData
						.getValidationObject())
						.setEnterOnPlateSelection(
					false);

				if (caSpclPltRegisData != null
					&& caSpclPltRegisData.getVIAllocData() != null)
				{
					releaseHeldVirtualInventory();
				}

				getController().processData(
					AbstractViewController.CANCEL,
					caOrigVehInqData);
			}

			else if (aaAE.getSource() == buttonPanel.getBtnHelp())
			{
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			aeRTSEx.getFirstComponent().requestFocus();
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return com.txdot.isd.rts.client.general.ui.ButtonPanel
	 */
	private ButtonPanel getButtonPanel()
	{
		if (buttonPanel == null)
		{
			buttonPanel =
				new com.txdot.isd.rts.client.general.ui.ButtonPanel();
			buttonPanel.setBounds(89, 258, 236, 37);
			buttonPanel.addActionListener(this);
			buttonPanel.setAsDefault(this);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes FrmPlateSelectionREG011ContentPane
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmPlateSelectionREG011ContentPane()
	{
		if (FrmPlateSelectionREG011ContentPane == null)
		{
			FrmPlateSelectionREG011ContentPane = new JPanel();
			FrmPlateSelectionREG011ContentPane.setLayout(null);
			FrmPlateSelectionREG011ContentPane.add(
				getButtonPanel(),
				null);
			FrmPlateSelectionREG011ContentPane.add(
				getPanelMfgOrIssueInventory(),
				null);
			FrmPlateSelectionREG011ContentPane.add(
				getPanelSigned(),
				null);
			FrmPlateSelectionREG011ContentPane.add(
				getPanelPlateType(),
				null);
		}
		return FrmPlateSelectionREG011ContentPane;
	}

	/**
	 * This method initializes panelPlateType
	 * 
	 * @return JPanel
	 */
	private JPanel getPanelPlateType()
	{
		if (panelPlateType == null)
		{
			panelPlateType = new JPanel();
			panelPlateType.setLayout(null);
			panelPlateType.setVisible(false);
			panelPlateType.add(getRadioRegPlate(), null);
			panelPlateType.add(getRadioReplPlate(), null);
			panelPlateType.add(getRegisteredPltDesc(), null);
			panelPlateType.add(getReplacementPltDesc(), null);
			panelPlateType.setSize(396, 96);
			panelPlateType.setBorder(
				new TitledBorder(new EtchedBorder(), SELECT_ONE));
			panelPlateType.setLocation(17, 15);
			RTSButtonGroup laRadioGrp = new RTSButtonGroup();
			laRadioGrp.add(getRadioReplPlate());
			laRadioGrp.add(getRadioRegPlate());
		}
		return panelPlateType;
	}

	/**
	 * This method initializes panelMfgOrIssueInventory
	 * 
	 * @return JPanel
	 */
	private JPanel getPanelMfgOrIssueInventory()
	{
		if (panelMfgOrIssueInventory == null)
		{
			panelMfgOrIssueInventory = new JPanel();
			panelMfgOrIssueInventory.setLayout(null);
			panelMfgOrIssueInventory.setVisible(true);
			panelMfgOrIssueInventory.add(getRadioSamePlate(), null);
			panelMfgOrIssueInventory.add(getRadioNewPlate(), null);
			panelMfgOrIssueInventory.add(getRadioIssuePlate(), null);
			panelMfgOrIssueInventory.setSize(203, 122);
			panelMfgOrIssueInventory.setBorder(
				new TitledBorder(new EtchedBorder(), SELECT_ONE));
			panelMfgOrIssueInventory.setLocation(17, 121);
			RTSButtonGroup laRadioGrp = new RTSButtonGroup();
			laRadioGrp.add(getRadioSamePlate());
			laRadioGrp.add(getRadioNewPlate());
			laRadioGrp.add(getRadioIssuePlate());
		}
		return panelMfgOrIssueInventory;
	}

	/**
	 * This method initializes panelSigned
	 * 
	 * @return JPanel
	 */
	private JPanel getPanelSigned()
	{
		if (panelSigned == null)
		{
			panelSigned = new JPanel();
			panelSigned.setLayout(null);
			panelSigned.setVisible(true);
			panelSigned.add(getRadioSignedNo(), null);
			panelSigned.add(getRadioSignedYes(), null);
			panelSigned.setBounds(228, 121, 186, 96);
			panelSigned.setBorder(
				new TitledBorder(new EtchedBorder(), SIGNED_AFFIDAVIT));
			RTSButtonGroup laRadioGrp = new RTSButtonGroup();
			laRadioGrp.add(getRadioSignedYes());
			laRadioGrp.add(getRadioSignedNo());
		}
		return panelSigned;
	}

	/**
	 * This method initializes radioIssuePlate
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioIssuePlate()
	{
		if (radioIssuePlate == null)
		{
			radioIssuePlate = new JRadioButton();
			radioIssuePlate.setSize(143, 24);
			radioIssuePlate.setText(ISSUE_FROM_INVENTORY);
			radioIssuePlate.setMnemonic(java.awt.event.KeyEvent.VK_I);
			radioIssuePlate.setLocation(9, 75);
			// user code begin {1}
			//radioIssuePlate.addItemListener(this);
			// user code end
		}
		return radioIssuePlate;
	}

	/**
	 * This method initializes radioNewPlate
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioNewPlate()
	{
		if (radioNewPlate == null)
		{
			radioNewPlate = new JRadioButton();
			radioNewPlate.setSize(175, 24);
			radioNewPlate.setText(MFG_NEW_PLT_NO);
			radioNewPlate.setMnemonic(java.awt.event.KeyEvent.VK_W);
			radioNewPlate.setLocation(9, 50);
			//	user code begin {1}
			//radioNewPlate.addItemListener(this);
			// user code end
		}
		return radioNewPlate;
	}

	/**
	 * This method initializes radioSamePlate
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioSamePlate()
	{
		if (radioSamePlate == null)
		{
			radioSamePlate = new JRadioButton();
			radioSamePlate.setSize(183, 24);
			radioSamePlate.setMnemonic(java.awt.event.KeyEvent.VK_M);
			radioSamePlate.setText(MFG_SAME_PLT_NO);
			radioSamePlate.setLocation(9, 25);
			//	user code begin {1}
			// user code end
		}
		return radioSamePlate;
	}

	/**
	 * This method initializes radioRegPlate
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioRegPlate()
	{
		if (radioRegPlate == null)
		{
			radioRegPlate = new JRadioButton();
			radioRegPlate.setSize(135, 24);
			radioRegPlate.setText(SAME_PLT_TYPE);
			radioRegPlate.setMnemonic(java.awt.event.KeyEvent.VK_A);
			radioRegPlate.setLocation(15, 25);
			// user code begin {1}
			radioRegPlate.addItemListener(this);
			// user code end
		}
		return radioRegPlate;
	}

	/**
	 * This method initializes radioReplPlate
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioReplPlate()
	{
		if (radioReplPlate == null)
		{
			radioReplPlate = new JRadioButton();
			radioReplPlate.setSize(135, 24);
			radioReplPlate.setText(NEW_PLT_TYPE);
			radioReplPlate.setMnemonic(java.awt.event.KeyEvent.VK_E);
			radioReplPlate.setLocation(15, 50);
			// user code begin {1}
			radioReplPlate.addItemListener(this);
			// user code end
		}
		return radioReplPlate;
	}

	/* This method initializes radioSignedNo
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioSignedNo()
	{
		if (radioSignedNo == null)
		{
			radioSignedNo = new JRadioButton();
			radioSignedNo.setSize(59, 24);
			radioSignedNo.setText("No");
			radioSignedNo.setMnemonic(java.awt.event.KeyEvent.VK_N);
			radioSignedNo.setLocation(25, 50);
			// user code begin {1}
			//radioSignedNo.addItemListener(this);
			// user code end
		}
		return radioSignedNo;
	}

	/**
	 * This method initializes radioSignedYes
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getRadioSignedYes()
	{
		if (radioSignedYes == null)
		{
			radioSignedYes = new JRadioButton();
			radioSignedYes.setSize(58, 24);
			radioSignedYes.setText("Yes");
			radioSignedYes.setMnemonic(java.awt.event.KeyEvent.VK_Y);
			radioSignedYes.setLocation(25, 25);
			// user code begin {1}
			//radioSignedYes.addItemListener(this);
			// user code end
		}
		return radioSignedYes;
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
	 * Handles the enabling of the radio buttons 
	 * 
	 * @param abRegistered 
	 */
	private void manageSelectandSignedPanels(boolean abRegistered)
	{
		getRadioReplPlate().setVisible(!cbSamePltType);
		getReplacementPltDesc().setVisible(!cbSamePltType);

		getRadioSignedYes().setEnabled(cbUserPltNo && abRegistered);
		getRadioSignedNo().setEnabled(cbUserPltNo && abRegistered);
		getRadioSignedNo().setSelected(cbUserPltNo && abRegistered);
		getPanelSigned().setEnabled(cbUserPltNo && abRegistered);

		boolean lbEnableSelectOne =
			!cbUserPltNo || (cbUserPltNo && !abRegistered);
		boolean lbSpecial =
			!caPlateTypeData.getPltOwnrshpCd().equals(
				SpecialPlatesConstant.VEHICLE);

		// defect 10507 
		boolean lbMYOfcPlt = caPlateTypeData.isMultiYrOfclPlt();
		// end defect 10507 

		getPanelMfgOrIssueInventory().setEnabled(true);
		getRadioSamePlate().setEnabled(abRegistered && lbSpecial);
		getRadioSamePlate().setSelected(abRegistered && lbSpecial);

		// defect 10507 
		// disable for Multi-Year Official Plate  
		getRadioNewPlate().setEnabled(
			!lbMYOfcPlt
				&& !cbUserPltNo
				&& abRegistered
				&& lbSpecial
				&& RTSApplicationController.isDBReady());
		// end defect 10507 
		getRadioIssuePlate().setEnabled(lbEnableSelectOne);
		getRadioIssuePlate().setSelected(!abRegistered || !lbSpecial);

		// Reset or Reestablish SpclPltRegisData
		csSelectedPlateCd =
			abRegistered ? csOrigPlateCd : csReplPlateCd;
		handleSpclPltRegisData(abRegistered);

	}

	/**
	 * For Personalized Plate Replaement, set the indicator if 
	 * charge Remake Fee 
	 * 
	 * @param aaSpclPltRegisData
	 */
	private void handleChrgRemakeOnPLPReplace(SpecialPlatesRegisData aaSpclPltRegisData)
	{
		if (cbUserPltNo && getRadioRegPlate().isSelected())
		{
			if (getRadioSignedYes().isSelected())
			{
				aaSpclPltRegisData.setChrgRemakeOnReplace(false);
			}
			else
			{
				aaSpclPltRegisData.setChrgRemakeOnReplace(true);
			}
		}
	}

	/**
	 * 
	 * Handle Mfg Request
	 *
	 * @param aaSpclPltRegisData 
	 * @throws RTSException
	 */
	private void handleMfgRequest(SpecialPlatesRegisData aaSpclPltRegisData)
		throws RTSException
	{
		
		// Either Mfg Same or New Request
		if (aaSpclPltRegisData != null
			&& aaSpclPltRegisData.getMfgSpclPltIndi() == 1)
		{
			aaSpclPltRegisData.setMFGDate(
				getController().getTransCode());
			aaSpclPltRegisData.setMFGStatusCd(
				SpecialPlatesConstant.MANUFACTURE_MFGSTATUSCD);
			aaSpclPltRegisData.setRequestType(
				SpecialPlatesConstant.MANUFACTURE);
			aaSpclPltRegisData.setPltBirthDate(
				(new RTSDate()).getYYYYMMDDDate());
			aaSpclPltRegisData.setRegPltAge(0);
			
			// defect 10700 
			aaSpclPltRegisData.setPrntPrmt(true);
			// end defect 10700 
			 
			RTSException leRTSEx =
				UtilityMethods.createSpclPltMfgInfoMsg(
					aaSpclPltRegisData);
			leRTSEx.displayError(this);
		}
		if (getRadioNewPlate().isSelected())
		{
			csReplRegPltNo = "";
			if (caPlateTypeData.getAnnualPltIndi() == 1)
			{
				// defect 9864 
				// Modified in refactor of RegExpYr to PltExpYr  
				aaSpclPltRegisData.setInvItmYr(
					aaSpclPltRegisData.getPltExpYr());
				// end defect 9864  	
			}
			else
			{
				aaSpclPltRegisData.setInvItmYr(0);
			}
			aaSpclPltRegisData.setVIAllocData(
				SpecialPlatesClientUtilityMethods.setupInvAlloc(
					aaSpclPltRegisData,
					aaSpclPltRegisData.getRegPltNo()));
		}
	}

	/**
	 * Initialize the class.
	 * 
	 */
	private void initialize()
	{
		try
		{
			setContentPane(getFrmPlateSelectionREG011ContentPane());
			setSize(440, 332);
			setTitle(TITLE_REG011);
			setName("FrmPlateSelectionREG011");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
		}
		catch (Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
	}

	/**
	 * ItemListener method.
	 * 
	 * @param aaIE ItemEvent
	 */

	public void itemStateChanged(ItemEvent aaIE)
	{
		// Plate Type
		// Registered Plate  
		if (aaIE.getSource() == radioRegPlate)
		{
			manageSelectandSignedPanels(true);
		}
		// Replacement Plate 
		else if (aaIE.getSource() == radioReplPlate)
		{
			manageSelectandSignedPanels(false);
		}
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmPlateSelectionREG011 laFrmPlateSelectionREG011;
			laFrmPlateSelectionREG011 = new FrmPlateSelectionREG011();
			laFrmPlateSelectionREG011.setModal(true);
			laFrmPlateSelectionREG011
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});

			laFrmPlateSelectionREG011.show();
			java.awt.Insets laInsets =
				laFrmPlateSelectionREG011.getInsets();
			laFrmPlateSelectionREG011.setSize(
				laFrmPlateSelectionREG011.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmPlateSelectionREG011.getHeight()
					+ laInsets.top
					+ laInsets.bottom);

			laFrmPlateSelectionREG011.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);

			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * Handle creation of inventory items needed for inventory module.
	 * 
	 * @param aaSpclPltRegisData
	 */
	private void prepInv(SpecialPlatesRegisData aaSpclPltRegisData)
	{
		caRegValidData.setInvItms(
			(Vector) UtilityMethods.copy(cvOrigInvItms));

		if (getRadioIssuePlate().isSelected()
			|| getRadioReplPlate().isSelected())
		{
			RegistrationClientUtilityMethods.addItmCdToInv(
				caRegValidData,
				csSelectedPlateCd);

			// If Special Plate, reset 
			if (PlateTypeCache.isSpclPlate(csSelectedPlateCd))
			{
				// Set MfgDate to reflect earlier 	
				aaSpclPltRegisData.setMFGDate(
					(new RTSDate()).getYYYYMMDDDate());
				// Reset MfgStatus in the event of earlier request to Manufacture
				aaSpclPltRegisData.setMFGStatusCd(
					SpecialPlatesConstant.ASSIGN_MFGSTATUSCD);
				aaSpclPltRegisData.setRequestType(
					SpecialPlatesConstant.ISSUE_FROM_INVENTORY);
				aaSpclPltRegisData.setMfgSpclPltIndi(0);
			}
			else
			{
				aaSpclPltRegisData = null;
				RegistrationData laRegData =
					(RegistrationData) UtilityMethods.copy(
						caVehicleInquiryData
							.getMfVehicleData()
							.getRegData());
				caReplVehicleInquiryData.getMfVehicleData().setRegData(
					laRegData);
				laRegData.setRegPltCd(csSelectedPlateCd);
				// defect 9912 
				// Remove reference to RegisData.setOrgNo()
				//laRegData.setOrgNo("");
				// end defect 9912 
			}
		}
		else
		{
			if (caRegValidData.getInvItms() == null)
			{
				caRegValidData.setInvItms(new Vector());
			}
			aaSpclPltRegisData.setMfgSpclPltIndi(1);
		}
	}

	/**
	 * Clear or reestablish SpclPltRegisData based upon 
	 * the radio button selections. 
	 * 
	 * @param abRegistered

	 */
	private void handleSpclPltRegisData(boolean abRegistered)
	{
		if (cbSpclPlt)
		{
			if (!abRegistered)
			{
				caSpclPltRegisData = null;
			}
			else if (caSpclPltRegisData == null)
			{
				caSpclPltRegisData =
					(SpecialPlatesRegisData) UtilityMethods.copy(
						caOrigSpclPltRegisData);
						
				if (getRadioSamePlate().isSelected()
					|| getRadioNewPlate().isSelected())
				{
					caSpclPltRegisData.setMfgSpclPltIndi(1);
				}
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

		if (aaDataObject instanceof Vector)
		{
			Vector lvIsNextVCREG029 = (Vector) aaDataObject;
			if (lvIsNextVCREG029 != null)
			{
				if (lvIsNextVCREG029.size() == 2)
				{
					// determine next vc if NOT reg029
					// first element is flag whether to go to REG029
					if (lvIsNextVCREG029.get(0) instanceof Boolean)
					{
						getController().processData(
							VCSpecialPlateInformationSPL002
								.REDIRECT_IS_NEXT_VC_REG029,
							lvIsNextVCREG029);
					}
					else if (lvIsNextVCREG029.get(0) instanceof String)
					{
						getController().processData(
							VCSpecialPlateInformationSPL002
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
				}
			}
		}
		else if (aaDataObject instanceof VehicleInquiryData)
		{
			// Save original Object / Vector 
			if (caOrigVehInqData == null)
			{
				caOrigVehInqData =
					(VehicleInquiryData) UtilityMethods.copy(
						aaDataObject);
				cvOrigInvItms =
					((RegistrationValidationData) caOrigVehInqData
						.getValidationObject())
						.getInvItms();
			}

			caVehicleInquiryData =
				(VehicleInquiryData) UtilityMethods.copy(aaDataObject);

			caRegValidData =
				(RegistrationValidationData) caVehicleInquiryData
					.getValidationObject();

			csOrigPlateCd = caRegValidData.getOrigRegPltCd();
			csReplPlateCd = caRegValidData.getReplPltCd();

			caPlateTypeData =
				PlateTypeCache.getPlateType(csOrigPlateCd);

			caSpclPltRegisData =
				caVehicleInquiryData
					.getMfVehicleData()
					.getSpclPltRegisData();

			caOrigSpclPltRegisData =
				(SpecialPlatesRegisData) UtilityMethods.copy(
					caVehicleInquiryData
						.getMfVehicleData()
						.getSpclPltRegisData());

			if (caPlateTypeData.getUserPltNoIndi() == 1)
			{
				cbUserPltNo = true;
			}
			cbSpclPlt =
				caVehicleInquiryData.getMfVehicleData().isSpclPlt();
			cbSamePltType =
				csOrigPlateCd.trim().equals(csReplPlateCd.trim());
			setPlateTypeDescriptions();
			manageSelectandSignedPanels(true);
			getRadioRegPlate().setSelected(true);
		}
		else if (aaDataObject instanceof InventoryAllocationData)
		{
			caInvAllocData = (InventoryAllocationData) aaDataObject;
			csReplRegPltNo = caInvAllocData.getInvItmNo();
			caSpclPltRegisData.setVIAllocData(caInvAllocData);
			caSpclPltRegisData.setRegPltNo(
				caInvAllocData.getInvItmNo());
		}
	}
	/**
	 * 
	 * Release Held Inventory
	 *
	 */
	public void releaseHeldVirtualInventory()
	{
		if (caSpclPltRegisData.getVIAllocData() != null)
		{
			getController().processData(
				InventoryConstant.INV_VI_RELEASE_HOLD,
				caSpclPltRegisData.getVIAllocData());
		}

	}
	/**
	 * Determine plate type descriptions for labels adjacent to 
	 * radio buttons for the original reg plate type and 
	 * for the replacement plate type.
	 * 
	 */
	private void setPlateTypeDescriptions()
	{
		String lsItmCd = caRegValidData.getOrigRegPltCd();
		String lsDesc = ItemCodesCache.getItmCd(lsItmCd).getItmCdDesc();
		PlateTypeData laPltTypeData =
			PlateTypeCache.getPlateType(lsItmCd);

		if (laPltTypeData.getAnnualPltIndi() == 1)
		{
			lsDesc = lsDesc + "   " + caSpclPltRegisData.getPltExpYr();
		}
		getRegisteredPltDesc().setText(lsDesc);

		// Replacement Code 
		lsItmCd = caRegValidData.getReplPltCd();
		lsDesc = ItemCodesCache.getItmCd(lsItmCd).getItmCdDesc();
		if (laPltTypeData.getAnnualPltIndi() == 1)
		{
			// defect 9864 
			// Modified in refactor of RegExpYr to PltExpYr 
			lsDesc = lsDesc + "   " + caSpclPltRegisData.getPltExpYr();
			// end defect 9864 
		}
		getReplacementPltDesc().setText(lsDesc);
		panelPlateType.setVisible(true);
	}

	/**
	 * This method initializes registeredPltDesc
	 * 
	 * @return JLabel
	 */
	private JLabel getRegisteredPltDesc()
	{
		if (registeredPltDesc == null)
		{
			registeredPltDesc = new JLabel();
			registeredPltDesc.setSize(230, 24);
			registeredPltDesc.setText("");
			registeredPltDesc.setLocation(160, 25);
		}
		return registeredPltDesc;
	}
	/**
	 * This method initializes replacementPltDesc
	 * 
	 * @return JLabel
	 */
	private JLabel getReplacementPltDesc()
	{
		if (replacementPltDesc == null)
		{
			replacementPltDesc = new JLabel();
			replacementPltDesc.setSize(230, 24);
			replacementPltDesc.setText("");
			replacementPltDesc.setLocation(160, 50);
		}
		return replacementPltDesc;
	}
}
