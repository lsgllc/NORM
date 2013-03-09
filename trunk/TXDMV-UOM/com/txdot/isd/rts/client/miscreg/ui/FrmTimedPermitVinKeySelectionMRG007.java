package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.*;

import javax.swing.*;

import com.txdot.isd.rts.client.common.business.CommonClientBusiness;
import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.common.Transaction;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.PermitData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * FrmTimedPermitVinKeySelectionMRG007.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	05/24/2010	Created
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/08/2010	Correct handling of tabbing, cursor movement
 * 							 keys. Removed creation of MFPermitParitals 	
 * 							 as will not go to INQ004 on same vehicle
 * 							add keyPressed()  
 * 							modify setupSavedPermit() 
 * 							defect 10491 Ver 6.5.0 
 * K Harrell	07/14/2010	Disable "Same Vehicle" if 
 * 							  Transaction.getCumulativeTransIndi() != 1
 * 							modify setupSavedPermit()
 * 							defect 10491 Ver 6.5.0  
 * ---------------------------------------------------------------------
 */

/**
 * Frame Timed Permit Vin Key Selection MRG007
 *
 * @version	6.5.0 			07/14/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		05/24/2010  14:16:17 
 */
public class FrmTimedPermitVinKeySelectionMRG007
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkNoVIN = null;
	private JCheckBox ivjchkSame = null;
	private JPanel ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1 =
		null;
	private JPanel ivjJPanel1 = null;
	private JLabel ivjstcLblVin = null;
	private RTSInputField ivjtxtVin = null;

	// String 
	private String csTransCd;
	private String csVin;

	// Object
	private PermitData caSavedPrmtData = new PermitData();

	// Constant 
	private final static String FRM_NAME_MRG007 =
		"FrmTimedPermitVinKeySelectionMRG007";
	private final static String FRM_TITLE_MRG007 =
		"Timed Permit VIN Key Selection     MRG007";
	private final static String TXT_SAME_VEH = "Same Vehicle";
	private final static String TXT_VIN = "VIN:";

	/**
	 * FrmTimedPermitVinKeySelectionMRG007.java Constructor
	 * 
	 */
	public FrmTimedPermitVinKeySelectionMRG007()
	{
		super();
	}

	/**
	 * Creates a FrmTimedPermitVinKeySelectionMRG007 with the parent
	 * 
	 * @param aaParent	Dialog
	 */
	public FrmTimedPermitVinKeySelectionMRG007(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Creates a FrmTimedPermitVinKeySelectionMRG007 with the parent
	 * 
	 * @param aaParent	JFrame 
	 */
	public FrmTimedPermitVinKeySelectionMRG007(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked for Enter/Cancel/Help
	 * 
	 * @param ActionEvent aaAE
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
				if (getchkSame().isSelected())
				{
					getController().processData(
						VCTimedPermitVinKeySelectionMRG007.MRG005,
						caSavedPrmtData);
				}
				else if (getchkNoVin().isSelected())
				{
					PermitData laPrmtData = new PermitData();
					laPrmtData.setVin(new String());

					getController().processData(
						VCTimedPermitVinKeySelectionMRG007.MRG005,
						laPrmtData);
				}
				else
				{
					csVin = gettxtVin().getText();

					csVin =
						CommonValidations.convert_i_and_o_to_1_and_0(
							csVin);
					ivjtxtVin.setText(csVin);

					if (validateData())
					{
						getController().processData(
							AbstractViewController.ENTER,
							buildGSD());
					}
				}
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
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * This method is used to assign the search variables to 
	 * GeneralSearchData after a user has pressed enter.
	 * 
	 * @return GeneralSearchData
	 */
	private GeneralSearchData buildGSD()
	{
		GeneralSearchData laGSD = new GeneralSearchData();
		laGSD.setKey1(CommonConstant.PRMT_VIN);
		laGSD.setKey2(csVin);
		laGSD.setKey3(TransCdConstant.PT72);
		return laGSD;
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
				ivjButtonPanel1.setBounds(124, 100, 283, 38);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
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
	 * This method initializes ivjchkNoVIN
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkNoVin()
	{
		if (ivjchkNoVIN == null)
		{
			ivjchkNoVIN = new javax.swing.JCheckBox();
			ivjchkNoVIN.setBounds(405, 41, 76, 23);
			ivjchkNoVIN.setMnemonic(KeyEvent.VK_V);
			ivjchkNoVIN.addItemListener(this);
			ivjchkNoVIN.setText("No VIN");
		}
		return ivjchkNoVIN;
	}

	/**
	 * Return the ivjchkSame property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSame()
	{
		if (ivjchkSame == null)
		{
			try
			{
				ivjchkSame = new JCheckBox();
				ivjchkSame.setBounds(405, 74, 103, 21);
				ivjchkSame.setName("ivjchkSame");
				ivjchkSame.setText(TXT_SAME_VEH);
				ivjchkSame.setMaximumSize(new Dimension(103, 22));
				ivjchkSame.setMinimumSize(new Dimension(103, 22));
				ivjchkSame.setActionCommand(TXT_SAME_VEH);
				ivjchkSame.setEnabled(false);
				// user code begin {1}
				ivjchkSame.setMnemonic(KeyEvent.VK_M);
				ivjchkSame.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSame;
	}

	/**
	* Return the ivjFrmVinKeySelectionKEY006ContentPane1 property value.
	* 
	* @return JPanel
	*/
	private JPanel getFrmTimedPermitVinKeySelectionMRG007ContentPane1()
	{
		if (ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1 == null)
		{
			try
			{
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1 =
					new JPanel();
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1
					.setName(
					"ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1");
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1
					.setLayout(
					null);
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1.add(
					getJPanel1(),
					null);
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1.add(
					getButtonPanel1(),
					null);
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1.add(
					getchkNoVin(),
					null);
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1.add(
					getchkSame(),
					null);
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1
					.setMaximumSize(
					new Dimension(500, 200));
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1
					.setMinimumSize(
					new Dimension(500, 200));
				ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
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
		return ivjFrmTimedPermitVinKeySelectionMRG007ContentPane1;
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
				ivjJPanel1.setBounds(54, 16, 335, 71);
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setMaximumSize(new Dimension(392, 112));
				ivjJPanel1.setMinimumSize(new Dimension(392, 112));
				GridBagConstraints laConstraintsstcLblApplicant =
					new GridBagConstraints();
				laConstraintsstcLblApplicant.gridx = 1;
				laConstraintsstcLblApplicant.gridy = 2;
				laConstraintsstcLblApplicant.ipadx = 51;
				laConstraintsstcLblApplicant.insets =
					new Insets(11, 56, 12, 3);
				GridBagConstraints laConstraintstxtApplicant =
					new GridBagConstraints();
				laConstraintstxtApplicant.gridx = 2;
				laConstraintstxtApplicant.gridy = 2;
				laConstraintstxtApplicant.fill =
					GridBagConstraints.HORIZONTAL;
				laConstraintstxtApplicant.weightx = 1.0;
				laConstraintstxtApplicant.ipadx = 72;
				laConstraintstxtApplicant.insets =
					new Insets(8, 3, 9, 87);
				GridBagConstraints laConstraintstxtLast4 =
					new GridBagConstraints();
				laConstraintstxtLast4.gridx = 2;
				laConstraintstxtLast4.gridy = 3;
				laConstraintstxtLast4.fill =
					GridBagConstraints.HORIZONTAL;
				laConstraintstxtLast4.weightx = 1.0;
				laConstraintstxtLast4.ipadx = 32;
				laConstraintstxtLast4.insets =
					new java.awt.Insets(10, 3, 17, 147);
				GridBagConstraints laConstraintsstcLblLast4 =
					new GridBagConstraints();
				laConstraintsstcLblLast4.gridx = 1;
				laConstraintsstcLblLast4.gridy = 3;
				laConstraintsstcLblLast4.ipadx = 8;
				laConstraintsstcLblLast4.insets =
					new Insets(13, 4, 20, 3);

				ivjJPanel1.add(gettxtVin(), null);
				ivjJPanel1.add(getstcLblVin(), null);
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
	 * Return the ivjstcLblVin property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVin()
	{
		if (ivjstcLblVin == null)
		{
			try
			{
				ivjstcLblVin = new JLabel();
				ivjstcLblVin.setBounds(57, 33, 45, 16);
				ivjstcLblVin.setName("ivjstcLblVin");
				ivjstcLblVin.setText(TXT_VIN);
				ivjstcLblVin.setMaximumSize(new Dimension(22, 14));
				ivjstcLblVin.setMinimumSize(new Dimension(22, 14));
				// user code begin {1}
				ivjstcLblVin.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblVin;
	}

	/**
	 * Return the ivjtxtVin property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtVin()
	{
		if (ivjtxtVin == null)
		{
			try
			{
				ivjtxtVin = new RTSInputField();
				ivjtxtVin.setBounds(109, 29, 202, 20);
				ivjtxtVin.setName("ivjtxtVin");
				ivjtxtVin.setManagingFocus(false);
				ivjtxtVin.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtVin.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjtxtVin.setMaximumSize(new Dimension(70, 20));
				ivjtxtVin.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtVin.setMinimumSize(new Dimension(70, 20));
				ivjtxtVin.setMaxLength(22);
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
		return ivjtxtVin;
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
			setName(FRM_NAME_MRG007);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setRequestFocus(false);
			setSize(531, 173);
			setTitle(FRM_TITLE_MRG007);
			setContentPane(
				getFrmTimedPermitVinKeySelectionMRG007ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
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
		if (aaIE.getSource() == getchkSame()
			|| aaIE.getSource() == getchkNoVin())
		{
			if (getchkNoVin().isSelected()
				|| getchkSame().isSelected())
			{
				clearAllColor(this);

				getstcLblVin().setEnabled(false);
				gettxtVin().setText("");
				gettxtVin().setEnabled(false);

				if (aaIE.getSource() == getchkNoVin()
					&& aaIE.getStateChange() == 1)
				{
					getchkSame().setSelected(false);
					getchkNoVin().requestFocus();
				}
				else if (
					aaIE.getSource() == getchkSame()
						&& aaIE.getStateChange() == 1)
				{
					getchkNoVin().setSelected(false);
					getchkSame().requestFocus();
				}
			}
			else
			{
				clearAllColor(this);

				getstcLblVin().setEnabled(true);
				gettxtVin().setEnabled(true);

				getchkSame().setSelected(false);
				getchkNoVin().setSelected(false);
				gettxtVin().requestFocus();
			}
		}
	}

	/**
	 * Handle Cursor Movement Keys for Check Boxes
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		if (aaKE.getSource().equals(gettxtVin())
			&& (aaKE.getKeyCode() == KeyEvent.VK_TAB))
		{
			if (aaKE.getModifiers() == KeyEvent.SHIFT_MASK)
			{
				getButtonPanel1().getBtnHelp().requestFocus();
			}
			else
			{
				if (getchkNoVin().isVisible()
					&& getchkNoVin().isEnabled())
				{
					getchkNoVin().requestFocus();
				}
				else if (
					getchkSame().isVisible()
						&& getchkSame().isEnabled())
				{
					getchkSame().requestFocus();
				}
			}
		}
		else if (
			aaKE.getKeyCode() == KeyEvent.VK_UP
				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT
				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (getchkNoVin().hasFocus() && getchkSame().isEnabled())
			{
				getchkSame().requestFocus();
			}
			else if (
				getchkSame().hasFocus() && getchkNoVin().isEnabled())
			{
				getchkNoVin().requestFocus();
			}
		}
		super.keyPressed(aaKE);
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		csTransCd = getController().getTransCode();

		setupSavedPermit();
	}

	/**
	 * Setup Saved Permit
	 */
	private void setupSavedPermit()
	{
		try
		{
			if (Transaction.getCumulativeTransIndi() == 1)
			{
				CommonClientBusiness laCommonClientBusiness =
					new CommonClientBusiness();

				Object laTmpPrmt =
					laCommonClientBusiness.processData(
						GeneralConstant.COMMON,
						CommonConstant.GET_TIME_PERMIT,
						null);

				if (laTmpPrmt != null
					&& laTmpPrmt instanceof PermitData)
				{
					caSavedPrmtData = (PermitData) laTmpPrmt;
					caSavedPrmtData.setNoMFRecs(1);
					caSavedPrmtData.setSameVeh(true);
					ivjchkSame.setEnabled(true);
				}
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
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
		RTSException leRTSEx = new RTSException();

		if (gettxtVin().isEmpty())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_ENTER_VIN_OR_CHECK_NO_VIN),
				gettxtVin());
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		else
		{
			if (gettxtVin().getText().length()
				!= CommonConstant.LENGTH_VIN)
			{
				new RTSException(
					ErrorsConstant
						.ERR_NUM_VIN_NOT_17_DIGITS)
						.displayError(
					this);
			}
		}
		return lbReturn;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
