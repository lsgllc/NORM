package com.txdot.isd.rts.client.specialplates.ui;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * FrmSpecialPlateDuplicateInsigniaSPL005.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	12/21/2010	Created 
 * 							defect 10700 Ver 6.7.0
 * ---------------------------------------------------------------------
 */

/**
 * SpecialPlateDuplicateInsigniaSPL005 Frame 
 *
 * @version	6.7.0			12/21/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		12/21/2010 11:30:17
 */
public class FrmSpecialPlateDuplicateInsigniaSPL005
	extends RTSDialogBox
	implements ActionListener, ItemListener
{

	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblSpecialPlateNo = null;
	private RTSInputField ivjtxtSpecialPlateNo = null;
	private ButtonPanel ivjButtonPanel1 = null;

	// String
	private String csTransCd;

	// Constant 
	private static final String SPECIAL_PLATE_NO = "Special Plate No:";

	/**
	 * FrmSpecialPlateDuplicateInsigniaSPL005.java Constructor
	 */
	public FrmSpecialPlateDuplicateInsigniaSPL005()
	{
		super();
		initialize();
	}

	/**
	 * FrmSpecialPlateDuplicateInsigniaSPL005 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSpecialPlateDuplicateInsigniaSPL005(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmSpecialPlateDuplicateInsigniaSPL005 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSpecialPlateDuplicateInsigniaSPL005(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param ActionEvent aaAE
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking() || isVisible() == false)
		{
			return;
		}
		try
		{
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// Validate plate number
				String lsRegPltNo =
					gettxtSpecialPlateNo().getText().trim();

				if (plateKeyValidation(lsRegPltNo))
				{
					getController().processData(
						AbstractViewController.ENTER,
						lsRegPltNo);
				}
				else
				{
					gettxtSpecialPlateNo().requestFocus();
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
			if (getDefaultFocusField() != null
				&& getDefaultFocusField().isEnabled()
				&& getDefaultFocusField().isEnabled())
			{
				getDefaultFocusField().requestFocus();
			}
		}
	}

	/**
	 * Return the stcLbl property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				ivjRTSDialogBoxContentPane.add(
					getstcLblSpecialPlateNo(),
					getstcLblSpecialPlateNo().getName());
				ivjRTSDialogBoxContentPane.add(
					gettxtSpecialPlateNo(),
					gettxtSpecialPlateNo().getName());
				ivjRTSDialogBoxContentPane.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
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
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * This method initializes the class
	 * 
	 */
	private void initialize()
	{
		try
		{

			this.setContentPane(getRTSDialogBoxContentPane());
			setName("FrmSpecialPlateDuplicateInsigniaSPL005");
			setTitle("Special Plate Duplicate Insignia    SPL005");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(343, 178);
			setRequestFocus(false);
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		clearAllColor(this);

		gettxtSpecialPlateNo().setEnabled(true);
		ivjstcLblSpecialPlateNo.setEnabled(true);
		gettxtSpecialPlateNo().requestFocus(true);
	}

	/**
	 * This method initializes ivjstcLblSpecialPlateNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblSpecialPlateNo()
	{
		if (ivjstcLblSpecialPlateNo == null)
		{
			try
			{
				ivjstcLblSpecialPlateNo = new JLabel();
				ivjstcLblSpecialPlateNo.setSize(95, 20);
				ivjstcLblSpecialPlateNo.setText(SPECIAL_PLATE_NO);
				// user code begin {1}
				ivjstcLblSpecialPlateNo.setLocation(75, 49);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblSpecialPlateNo;
	}

	/**
	 * Return the ECH property value.
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
				ivjButtonPanel1.setBounds(36, 101, 273, 42);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code 
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
	 * Return the ivjtxtSpecialPlateNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtSpecialPlateNo()
	{
		if (ivjtxtSpecialPlateNo == null)
		{
			try
			{
				ivjtxtSpecialPlateNo = new RTSInputField();
				ivjtxtSpecialPlateNo.setSize(90, 20);
				ivjtxtSpecialPlateNo.setName("ivjtxtSpecialPlateNo");
				ivjtxtSpecialPlateNo.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtSpecialPlateNo.setMaxLength(
					CommonConstant.LENGTH_PLTNO);
				ivjtxtSpecialPlateNo.setLocation(177, 49);
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
		return ivjtxtSpecialPlateNo;
	}

	/**
	 * Called whenever an exception is thrown
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
	 * Plate key validation on empty and length
	 * 
	 * @param asRegPltNo 
	 * @return boolean 
	 */
	private boolean plateKeyValidation(String asRegPltNo)
	{
		RTSException leRTSEx = new RTSException();
		boolean lbRtn = true;

		// Check if plate number was entered
		if (asRegPltNo == null || asRegPltNo.length() == 0)
		{
			leRTSEx.addException(
				new RTSException(150),
				gettxtSpecialPlateNo());
			leRTSEx.displayError(this);
			lbRtn = false;
		}
		return lbRtn;
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param Object aaDataObject
	 */
	public void setData(Object aaDataObject)
	{
		csTransCd = getController().getTransCode();
		setDefaultFocusField(gettxtSpecialPlateNo());
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="35,10"
