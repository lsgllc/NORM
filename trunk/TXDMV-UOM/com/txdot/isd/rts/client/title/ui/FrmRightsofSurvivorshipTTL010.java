package com.txdot.isd.rts.client.title.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.services.data.MFVehicleData;
import com.txdot.isd.rts.services.data.ScreenTTL010SavedData;
import com.txdot.isd.rts.services.data.TitleData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmRightsofSurvivorshipTTL010.java
 * 
 * (c) Texas Department of Transportation 2012
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/13/2012	Created
 * 							defect 10827 Ver 6.10.0 
 * --------------------------------------------------------------------- 
 */

/**
 * This form is used to capture Rights of Survivorship Names as well as set the
 * Additional Survivor Indicator
 * 
 * @version 6.10.0 		01/13/2012
 * @author Kathy Harrell
 * @since 				01/13/2012 17:04:17
 */

public class FrmRightsofSurvivorshipTTL010 extends RTSDialogBox
		implements ActionListener, ItemListener
{
	
	private JPanel ivjFrmRightsofSurvivorshipTTL010ContentPane1 = null;
	private JLabel ivjstcLblName1 = null;
	private RTSInputField ivjtxtName1 = null;  
	private RTSInputField ivjtxtName2 = null;
	private JLabel ivjstcLblName2 = null; 
	private JCheckBox ivjchkAddlSurvivorsExist = null;
	private JLabel ivjstcLblOR = null;
	private RTSButton ivjbtnEnter = null;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnBypass = null;
	private RTSButton ivjbtnReset = null;
	
	// boolean 
	private static boolean cbByPassAppl = false; 
	
	// Vector 
	private static Vector cvMFValid = new Vector();
	
	// Object 
	private VehicleInquiryData caVehInqData = null;
	
	// Static
	private static final String TTL010_FRM_TITLE = "Rights of Survivorship     TTL010";
	private static final String NAME1_LBL = "Name 1:";
	private static final String NAME2_LBL = "Name 2:";
	private static final String MULTIPLE_CHKBX_LBL = "Multiple Survivors";
	private static final String ENTER_BTN_LABEL = "Enter";
	private static final String CANCEL_BTN_LABEL = "Cancel";
	private static final String RESET_BTN_LABEL = "Reset";
	private static final String BYPASS_BTN_LABEL = "Bypass";
	private static final String OR_LBL = "OR";
	
	/**
	 * FrmRightsofSurvivorshipTTL010 constructor comment.
	 */
	public FrmRightsofSurvivorshipTTL010()
	{
		super();
		initialize();
	}

	/**
	 * FrmRightsofSurvivorshipTTL010 constructor comment.
	 * 
	 * @param owner
	 *            java.awt.Dialog
	 */
	public FrmRightsofSurvivorshipTTL010(Dialog owner)
	{
		super(owner);
		initialize();
	}

	/**
	 * FrmRightsofSurvivorshipTTL010 constructor comment.
	 */
	public FrmRightsofSurvivorshipTTL010(JFrame parent)
	{
		super(parent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (aaAE.getSource() instanceof RTSButton &&  
					((RTSButton) aaAE.getSource()).isEnabled())
			{
				clearAllColor(this);

				UtilityMethods.trimRTSInputField(this);

				// RESET NAMES 
				if (aaAE.getSource() == getbtnReset())
				{
					handleResetRequest(); 
				}
				// CANCEL
				else if (aaAE.getSource() == getbtnCancel())
				{
					saveScreenTTL010Data();

					getController().processData(
							AbstractViewController.CANCEL, null);
				}
				// ENTER, BYPASS
				else if (aaAE.getSource() == getbtnBypass() || 
						(aaAE.getSource() == getbtnEnter() && validateData()))
				{

					getController().processData(
							AbstractViewController.ENTER, setDataToDataObject());
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}


	/**
	 * This method initializes ivjbtnBypass	
	 * 	
	 * @return RTSButton
	 */
	private RTSButton getbtnBypass()
	{
		if (ivjbtnBypass == null)
		{
			try
			{
				ivjbtnBypass = new RTSButton();
				ivjbtnBypass.setBounds(new Rectangle(321, 186, 82, 26));
				ivjbtnBypass.setMnemonic(KeyEvent.VK_B);
				ivjbtnBypass.addActionListener(this);
				ivjbtnBypass.setText(BYPASS_BTN_LABEL);
				ivjbtnBypass.setEnabled(false);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnBypass;
	}

	/**
	 * This method initializes ivjbtnCancel	
	 * 	
	 * @return RTSButton
	 */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setBounds(new Rectangle(145, 186, 75, 26));
				ivjbtnCancel.setText(CANCEL_BTN_LABEL);
				ivjbtnCancel.addActionListener(this);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * This method initializes ivjbtnEnter	
	 * 	
	 * @return RTSButton
	 */
	private RTSButton getbtnEnter()
	{
		if (ivjbtnEnter == null)
		{
			try
			{
				ivjbtnEnter = new RTSButton();
				ivjbtnEnter.setBounds(new Rectangle(59, 186, 74, 26));
				ivjbtnEnter.addActionListener(this);
				ivjbtnEnter.setText(ENTER_BTN_LABEL);
				getRootPane().setDefaultButton(ivjbtnEnter);
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
		return ivjbtnEnter;
	}
	
	/**
	 * This method initializes ivjbtnReset	
	 * 	
	 * @return RTSButton
	 */
	private RTSButton getbtnReset()
	{
		if (ivjbtnReset == null)
		{
			try
			{
				ivjbtnReset = new RTSButton();
				ivjbtnReset.setBounds(new Rectangle(232, 186, 75, 26));
				ivjbtnReset.setMnemonic(KeyEvent.VK_R);
				ivjbtnReset.setText(RESET_BTN_LABEL);
				ivjbtnReset.addActionListener(this);
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
		return ivjbtnReset;
	}
	/**
	 * This method initializes ivjchkAddlSurvivorsExist
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkAddlSurvivorsExist()
	{
		if (ivjchkAddlSurvivorsExist == null)
		{
			try
			{
				ivjchkAddlSurvivorsExist = new JCheckBox();
				ivjchkAddlSurvivorsExist.setBounds(new Rectangle(165, 141, 136, 25));
				ivjchkAddlSurvivorsExist.setMnemonic(KeyEvent.VK_M);
				ivjchkAddlSurvivorsExist
				.setText(MULTIPLE_CHKBX_LBL);
				ivjchkAddlSurvivorsExist.addItemListener(this);
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
		return ivjchkAddlSurvivorsExist;
	}
	/**
	 * Return the FrmRightsofSurvivorshipTTL010ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmRightsofSurvivorshipTTL010ContentPane1()
	{
		if (ivjFrmRightsofSurvivorshipTTL010ContentPane1 == null)
		{
			try
			{
				ivjFrmRightsofSurvivorshipTTL010ContentPane1 = new JPanel();
				ivjFrmRightsofSurvivorshipTTL010ContentPane1
						.setName("FrmTransactionKeyVOI001ContentPane1");
				ivjFrmRightsofSurvivorshipTTL010ContentPane1
						.setLayout(null);
				ivjFrmRightsofSurvivorshipTTL010ContentPane1
						.setMaximumSize(new java.awt.Dimension(
								2147483647, 2147483647));
				ivjFrmRightsofSurvivorshipTTL010ContentPane1
						.setMinimumSize(new java.awt.Dimension(425, 200));
				getFrmRightsofSurvivorshipTTL010ContentPane1().add(
						getstcLblName1(), getstcLblName1().getName());
				getFrmRightsofSurvivorshipTTL010ContentPane1().add(
						gettxtName1(), gettxtName1().getName());
				ivjFrmRightsofSurvivorshipTTL010ContentPane1.add(
						gettxtName2(), null);
				ivjFrmRightsofSurvivorshipTTL010ContentPane1.add(
						getstcLblName2(), null);
				ivjFrmRightsofSurvivorshipTTL010ContentPane1.add(
						getchkAddlSurvivorsExist(), null);
				ivjFrmRightsofSurvivorshipTTL010ContentPane1.add(getstcLblOR(), null);
				ivjFrmRightsofSurvivorshipTTL010ContentPane1.add(getbtnEnter(), null);
				ivjFrmRightsofSurvivorshipTTL010ContentPane1.add(getbtnCancel(), null);
				ivjFrmRightsofSurvivorshipTTL010ContentPane1.add(getbtnBypass(), null);
				ivjFrmRightsofSurvivorshipTTL010ContentPane1.add(getbtnReset(), null);
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
		return ivjFrmRightsofSurvivorshipTTL010ContentPane1;
	}

	/**
	 * Return the ivjstcLblName1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblName1()
	{
		if (ivjstcLblName1 == null)
		{
			try
			{
				ivjstcLblName1 = new JLabel();
				ivjstcLblName1.setSize(55, 20);
				ivjstcLblName1.setName("stcLblName1");
				ivjstcLblName1.setText(NAME1_LBL);
				ivjstcLblName1.setLocation(new Point(48, 44));
				ivjstcLblName1.setLabelFor(gettxtName1());
				ivjstcLblName1.setMaximumSize(new java.awt.Dimension(
						108, 14));
				ivjstcLblName1.setMinimumSize(new java.awt.Dimension(
						108, 14));
				ivjstcLblName1.setDisplayedMnemonic(KeyEvent.VK_1);
				ivjstcLblName1
						.setHorizontalAlignment(SwingConstants.RIGHT);
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
		return ivjstcLblName1;
	}

	/**
	 * Return the ivjstcLblName2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblName2()
	{
		if (ivjstcLblName2 == null)
		{
			try
			{
				ivjstcLblName2 = new JLabel();
				ivjstcLblName2.setText(NAME2_LBL);
				ivjstcLblName2.setLocation(new Point(50, 75));
				ivjstcLblName2.setSize(new Dimension(53, 20));
				ivjstcLblName2.setDisplayedMnemonic(KeyEvent.VK_2);
				ivjstcLblName2
						.setHorizontalAlignment(SwingConstants.RIGHT);
				ivjstcLblName2.setLabelFor(gettxtName2());
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
		return ivjstcLblName2;
	}
	
	/**
	 * Return the ivjstcLblOR property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblOR()
	{
		if (ivjstcLblOR == null)
		{
			try
			{
				ivjstcLblOR = new JLabel();
				ivjstcLblOR.setHorizontalTextPosition(SwingConstants.CENTER);
				ivjstcLblOR.setHorizontalAlignment(SwingConstants.CENTER);
				ivjstcLblOR.setLocation(new Point(219, 115));
				ivjstcLblOR.setSize(new Dimension(17, 16));
				ivjstcLblOR.setText(OR_LBL);
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
		return ivjstcLblOR;
	}

	/**
	 * Return the txtTransactionId property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtName1()
	{
		if (ivjtxtName1 == null)
		{
			try
			{
				ivjtxtName1 = new RTSInputField();
				ivjtxtName1.setSize(269, 20);
				ivjtxtName1.setName("ivjtxtName1");
				ivjtxtName1.setInput(RTSInputField.DEFAULT);
				ivjtxtName1.setLocation(new Point(114, 44));
				ivjtxtName1.setMaxLength(CommonConstant.LENGTH_NAME);
				ivjtxtName1.addActionListener(this);
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
		return ivjtxtName1;
	}
	
	/**
	 * This method initializes ivjtxtName2
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtName2()
	{
		if (ivjtxtName2 == null)
		{
			try
			{
				ivjtxtName2 = new RTSInputField();
				ivjtxtName2.setMaxLength(CommonConstant.LENGTH_NAME);
				ivjtxtName2.setInput(RTSInputField.DEFAULT); 
				ivjtxtName2.setSize(new Dimension(269, 20));
				ivjtxtName2.setLocation(new Point(114, 75));
				ivjtxtName2.setName("ivjtxtName2");
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
		return ivjtxtName2;
	} 

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException
	 */
	private void handleException(Throwable aeException)
	{
		RTSException leRTSEx = new RTSException(
				RTSException.JAVA_ERROR, (Exception) aeException);
		leRTSEx.displayError(this);
	}
	
	/**
	 * Initialize the class.
	 */
	private void initialize()
	{
		try
		{
			setName("FrmRightsofSurvivorshipTTL010");
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(459, 263);
			setTitle(TTL010_FRM_TITLE);
			setContentPane(getFrmRightsofSurvivorshipTTL010ContentPane1());
			// user code begin {1}
			// user code end
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		cvMFValid = new Vector();
		cvMFValid.add(gettxtName1());
		cvMFValid.add(gettxtName2());
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
		if (aaIE.getSource() == getchkAddlSurvivorsExist())
		{
			clearAllColor(this); 
			
			boolean lbAddlChkSelected = getchkAddlSurvivorsExist().isSelected();
			
			if (lbAddlChkSelected)
			{
				gettxtName1().setText(new String());
				gettxtName2().setText(new String());
			}
			else
			{
				gettxtName1().requestFocus();
			}
			gettxtName1().setEnabled(!lbAddlChkSelected);
			gettxtName2().setEnabled(!lbAddlChkSelected);
			getstcLblName1().setEnabled(!lbAddlChkSelected);
			getstcLblName2().setEnabled(!lbAddlChkSelected);
			getstcLblOR().setEnabled(!lbAddlChkSelected);
			getbtnReset().setEnabled(lbAddlChkSelected);
			getbtnEnter().setEnabled(lbAddlChkSelected);
			getbtnBypass().setEnabled(!lbAddlChkSelected && cbByPassAppl); 
		}
	}
	
	/**
	 * Invoked when a key has been pressed.
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyReleased(KeyEvent aaKE)
	{
		if (aaKE.getKeyChar() == KeyEvent.VK_ESCAPE)
		{
			saveScreenTTL010Data();

			getController().processData(
				AbstractViewController.CANCEL,
				null);

		}
		else if (aaKE.getKeyChar() != KeyEvent.VK_ENTER)
		{
			setupScreenObjects();
		}
	}
	
	/**
	 * Handle Reset Request   
	 */
	private void handleResetRequest()
	{
		gettxtName1().setText(new String()); 
		gettxtName2().setText(new String());
		getbtnReset().setEnabled(false);
		getstcLblOR().setEnabled(true);
		getchkAddlSurvivorsExist().setEnabled(true);
		getchkAddlSurvivorsExist().setSelected(false);
		getbtnBypass().setEnabled(cbByPassAppl);
		getbtnEnter().setEnabled(false); 
	}
	
	/** 
	 * Save Screen Data for later Display 
	 */
	private void saveScreenTTL010Data()
	{
		ScreenTTL010SavedData laTTL010Data =
			new ScreenTTL010SavedData();
		laTTL010Data.setName1(gettxtName1().getText());
		laTTL010Data.setName2(gettxtName2().getText());
		laTTL010Data.setAddlSurvivors(getchkAddlSurvivorsExist().isSelected()); 

		getController().getMediator().closeVault(
			ScreenConstant.TTL010,
			UtilityMethods.copy(laTTL010Data));
	}
	
	/**
	 * all subclasses must implement this method - it sets the data on the
	 * screen and is how the controller relays information to the view
	 * 
	 * @param aaData 
	 */
	public void setData(Object aaData)
	{
		try
		{
			caVehInqData = (VehicleInquiryData) aaData;
			TitleData laTitleData = caVehInqData.getMfVehicleData().getTitleData();
			String lsTransCd = getController().getTransCode();
			boolean lbCorr = lsTransCd.equals(TransCdConstant.REJCOR) || 
				laTitleData.getTtlTypeIndi() == TitleTypes.INT_CORRECTED ;
			
			TitleValidObj laValidObj = (TitleValidObj) caVehInqData.getValidationObject();
			TitleData laOrigTitleData = null;
			if (laValidObj != null)
			{
				laOrigTitleData = ((MFVehicleData) laValidObj.getMfVehOrig()).getTitleData();
			}
			
			Object laTTL010 =
				getController().getMediator().openVault(
					ScreenConstant.TTL010);
			
			if (laTTL010 != null
					&& laTTL010 instanceof ScreenTTL010SavedData)
			{
				setSavedDataToDisplay((ScreenTTL010SavedData) laTTL010);
			}
			else if (lbCorr)
				{
					if (laTitleData.getAddlSurvivorIndi() ==1)
					{
						getchkAddlSurvivorsExist().setSelected(true); 
					}
					else
					{
						gettxtName1().setText(laTitleData.getSurvShpRightsName1());
						gettxtName2().setText(laTitleData.getSurvShpRightsName2());
					}
				}
			
			cbByPassAppl = 
					(lbCorr  && laValidObj != null 
							&& laOrigTitleData!= null && laOrigTitleData.getSurvshpRightsIndi() == 1
							&& UtilityMethods.isEmpty(laOrigTitleData.getSurvShpRightsName1())
							&& laOrigTitleData.getAddlSurvivorIndi() == 0)
					|| (UtilityMethods.isDTA(lsTransCd) && 
						caVehInqData.getRTSEffDt()< SystemProperty.getDTARejectSurvivorDate());

			setupScreenObjects();
			
			if (!getbtnEnter().isEnabled())
			{
				gettxtName1().requestFocus();
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTSEx = new RTSException(
					RTSException.FAILURE_MESSAGE, aeNPEx);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
	}

	/**
	 * Set Data to Data Object 
	 *  
	 * @return VehicleInquiryData 
	 */
	private VehicleInquiryData setDataToDataObject()
	{
		VehicleInquiryData laVehInqData = (VehicleInquiryData) UtilityMethods.copy(caVehInqData); 
		laVehInqData.getMfVehicleData().getTitleData()
				.setSurvShpRightsName1(gettxtName1().getText());
		laVehInqData.getMfVehicleData().getTitleData()
				.setSurvShpRightsName2(gettxtName2().getText());
		laVehInqData
				.getMfVehicleData()
				.getTitleData()
				.setAddlSurvivorIndi(
						getchkAddlSurvivorsExist().isSelected() ? 1 : 0);
		
		return laVehInqData; 
	}

	/** 
	 * Display data saved earlier 
	 * 
	 * @param aaTTL007Data
	 */
	private void setSavedDataToDisplay(ScreenTTL010SavedData aaTTL010Data)
	{
		if (!aaTTL010Data.isEmpty())
		{
			gettxtName1().setText(aaTTL010Data.getName1());
			gettxtName2().setText(aaTTL010Data.getName2());
			getchkAddlSurvivorsExist().setSelected(aaTTL010Data.isAddlSurvivors());
			getstcLblOR().setEnabled(false); 
		}
	}

	/**
	 * Enable/Disable: 
	 * 	   Buttons:   Enter, Reset, Bypass
	 *     Label:     OR
	 *     Checkbox:  Multiple Survivors   
	 */
	private void setupScreenObjects()
	{
		boolean lbEmptyName = gettxtName1().isEmpty() && gettxtName2().isEmpty();
		boolean lbAddlSurvivor = getchkAddlSurvivorsExist().isSelected(); 
		boolean lbNoData = lbEmptyName & !lbAddlSurvivor; 
		getchkAddlSurvivorsExist().setEnabled(lbEmptyName);
		getstcLblOR().setEnabled(lbNoData);
		getbtnBypass().setEnabled(lbNoData && cbByPassAppl);
		getbtnReset().setEnabled(!lbNoData);
		getbtnEnter().setEnabled(lbAddlSurvivor || !gettxtName1().isEmpty()); 
	}

	/**
	 * Validate Data on Screen 
	 * 
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;

		RTSException leRTSEx = new RTSException();

		UtilityMethods.trimRTSInputField(this);

		if (!getchkAddlSurvivorsExist().isSelected())
		{
			CommonValidations.addRTSExceptionForInvalidMFCharacters(
					cvMFValid,
					leRTSEx);
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
	}
}  //  @jve:decl-index=0:visual-constraint="58,11"