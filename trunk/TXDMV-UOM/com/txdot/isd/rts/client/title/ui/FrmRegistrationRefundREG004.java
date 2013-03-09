package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.RegistrationData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmRegistrationRefundREG004.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validation updated
 * K. Harrell   07/23/2002  CQU100004492 - >=10,000.00 on Refund
 * J Rue		03/10/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/29/2005	Clean up code
 * 							deprecate setData(java.util.Map aaMapData)
 * 							defect 7898 Ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/18/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3  
 * J Rue		11/03/2005	Deprecate method
 * 							deprecate setData(java.util.Map aaMapData)
 * 							defect 7898 Ver 5.2.3 
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3
 * J Ralph		12/06/2005	Aligned "Partial or Double Registration" 
 * 							radio button using Visual Editor. 
 * 							defect 7898 Ver 5.2.3
 * J Ralph		12/08/2005	Unable to requestFocus() on input field in 
 * 							SetData().
 * 							add WindowListener.windowOpened()
 * 							defect 7898 Ver 5.2.3
 * J Ralph		12/09/2005	Fixed arrow key handling to follow 5.2.3
 * 							standard.
 * 							modify keyPressed()
 * 							defect 7898 Ver 5.2.3      
 * T. Pederson	12/30/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * Jeff S.		01/04/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing. Set RequestFocus to 
 * 							false so that when a hot key is used to 
 * 							activate a radio button the focus moves to
 * 							the text field and is not moved back to the
 * 							radio button.
 * 							remove keyPressed(), focusGained(), 
 * 								focusLost()
 * 							modify getJPanel1(), initialize()
 * 							defect 7898 Ver 5.2.3 
 * B Hargrove	05/22/2008	Display error msg 150 if user does not 
 * 							change amount from 0.00.
 * 							Removed getBuilderData().
 * 							modify actionPerformed() 
 * 							defect 8566 Ver Defect POS A
 * B Hargrove	04/04/2008	Comment out references to CustBaseRegFee 
 * 							which is no longer retrieved from MF.
 * 							modify setDataToVehObj() 
 * 							defect 9631 Ver Defect POS A
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to capture the registration refund amount in status
 *  change.
 *
 * @version	POS A 			05/22/2008
 * @author	Marx Rajangam
 * <br>Creation Date:		6/25/2001 15:56:53
 */

public class FrmRegistrationRefundREG004
	extends RTSDialogBox
	implements ActionListener//, FocusListener, KeyListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JPanel ivjJPanel1 = null;
	private JRadioButton ivjradioFullRefund = null;
	private JRadioButton ivjradioPartial = null;
	// defect 7898
	// Changed from ButtonGroup to RTSButtonGroup
	private RTSButtonGroup caButtonGroup;
	// end defect 7898
	private JLabel ivjstcAmountAuthorized = null;
	private JLabel ivjstcLblEnterRegistration = null;
	private RTSInputField ivjtxtAmountAuthorized = null;
	private JPanel ivjFrmRegistrationRefundREG004ContentPane1 = null;
	private VehicleInquiryData caVehInqData = null;
	
	// Constant int
	private final static int AMT_AUTHORIZED_MAX_LEN = 7;

	// Constant String
	private final static String FULL_REFUND = "Full Refund";
	private final static String PARTIAL_DOUBLE_REGIS = 
		"Partial or Double Registration";
	private final static String AMT_AUTHORIZED = "amount authorized:";
	private final static String ENTER_REGIS_AMT = 
		"Enter registration refund";
	private final static String SELECT_CHOICE = "Select Choice:";
	
	/**
	 * FrmRegistrationRefundREG004 constructor comment.
	 */
	public FrmRegistrationRefundREG004()
	{
		super();
		initialize();
	}
	/**
	 * FrmRegistrationRefundREG004 constructor comment.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmRegistrationRefundREG004(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * FrmRegistrationRefundREG004 constructor comment.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmRegistrationRefundREG004(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		//Code to avoid clicking on the button multiple times
		if (!startWorking())
		{
			return;
		}

		try
		{

			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				clearAllColor(this);

				// CQU100004492 - >=10,000.00 on Refund
				String lsRefAmt =
					gettxtAmountAuthorized().getText().trim();
					
				// defect 8566
				// Add check for user must increase amount from 0.00.
				Dollar laDlrRefAmt = new Dollar(
					CommonConstant.STR_ZERO_DOLLAR);
				if (!lsRefAmt.equals(""))
				{
					laDlrRefAmt = new Dollar(lsRefAmt);
				}
				if (laDlrRefAmt.compareTo(new Dollar(
					CommonConstant.STR_ZERO_DOLLAR)) != 1
					|| laDlrRefAmt.compareTo(new Dollar(9999.99)) == 1)
				{
					RTSException leRTSEx150 = new RTSException();
					leRTSEx150.addException(
						new RTSException(150),
						gettxtAmountAuthorized());
					leRTSEx150.getFirstComponent().requestFocus();
					leRTSEx150.displayError(this);
					return;
				}
				// end defect 8566

				// set the data before going to the next screen
				setDataToVehObj();
				getController().processData(
					AbstractViewController.ENTER,
					getController().getData());

			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.REG004);
			}
		}

		finally
		{
			doneWorking();
		}
	}
//	/**
//	 * Invoked when a component gains the keyboard focus.
//	 * 
//	 * @param aaFE FocusEvent
//	 */
//	public void focusGained(java.awt.event.FocusEvent aaFE)
//	{
//		// Empty block of code
//	}
//	/**
//	 * Invoked when a component loses the keyboard focus.
//	 * 
//	 * @param aaFE FocusEvent
//	 */
//	public void focusLost(java.awt.event.FocusEvent aaFE)
//	{
//		// Empty block of code
//	}
	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				ivjbuttonPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbuttonPanel;
	}
	/**
	 * Return the FrmRegistrationRefundREG004ContentPane1 property 
	 * value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmRegistrationRefundREG004ContentPane1()
	{
		if (ivjFrmRegistrationRefundREG004ContentPane1 == null)
		{
			try
			{
				ivjFrmRegistrationRefundREG004ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmRegistrationRefundREG004ContentPane1.setName(
					"FrmRegistrationRefundREG004ContentPane1");
				ivjFrmRegistrationRefundREG004ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmRegistrationRefundREG004ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmRegistrationRefundREG004ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(462, 206));

				java
					.awt
					.GridBagConstraints constraintstxtAmountAuthorized =
					new java.awt.GridBagConstraints();
				constraintstxtAmountAuthorized.gridx = 2;
				constraintstxtAmountAuthorized.gridy = 3;
				constraintstxtAmountAuthorized.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtAmountAuthorized.weightx = 1.0;
				constraintstxtAmountAuthorized.ipadx = 150;
				constraintstxtAmountAuthorized.insets =
					new java.awt.Insets(10, 7, 13, 43);
				getFrmRegistrationRefundREG004ContentPane1().add(
					gettxtAmountAuthorized(),
					constraintstxtAmountAuthorized);

				java.awt.GridBagConstraints constraintsbuttonPanel =
					new java.awt.GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 4;
				constraintsbuttonPanel.gridwidth = 2;
				constraintsbuttonPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 76;
				constraintsbuttonPanel.ipady = 21;
				constraintsbuttonPanel.insets =
					new java.awt.Insets(9, 78, 11, 79);
				getFrmRegistrationRefundREG004ContentPane1().add(
					getbuttonPanel(),
					constraintsbuttonPanel);

				java.awt.GridBagConstraints constraintsJPanel1 =
					new java.awt.GridBagConstraints();
				constraintsJPanel1.gridx = 1;
				constraintsJPanel1.gridy = 1;
				constraintsJPanel1.gridheight = 3;
				constraintsJPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJPanel1.weightx = 1.0;
				constraintsJPanel1.weighty = 1.0;
				constraintsJPanel1.ipadx = 3;
				constraintsJPanel1.ipady = 10;
				constraintsJPanel1.insets =
					new java.awt.Insets(17, 20, 8, 7);
				getFrmRegistrationRefundREG004ContentPane1().add(
					getJPanel1(),
					constraintsJPanel1);

				java
					.awt
					.GridBagConstraints constraintsstcLblEnterRegistration =
					new java.awt.GridBagConstraints();
				constraintsstcLblEnterRegistration.gridx = 2;
				constraintsstcLblEnterRegistration.gridy = 1;
				constraintsstcLblEnterRegistration.ipadx = 22;
				constraintsstcLblEnterRegistration.insets =
					new java.awt.Insets(17, 8, 3, 35);
				getFrmRegistrationRefundREG004ContentPane1().add(
					getstcLblEnterRegistration(),
					constraintsstcLblEnterRegistration);

				java
					.awt
					.GridBagConstraints constraintsstcAmountAuthorized =
					new java.awt.GridBagConstraints();
				constraintsstcAmountAuthorized.gridx = 2;
				constraintsstcAmountAuthorized.gridy = 2;
				constraintsstcAmountAuthorized.ipadx = 39;
				constraintsstcAmountAuthorized.insets =
					new java.awt.Insets(4, 8, 9, 48);
				getFrmRegistrationRefundREG004ContentPane1().add(
					getstcAmountAuthorized(),
					constraintsstcAmountAuthorized);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmRegistrationRefundREG004ContentPane1;
	}
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				java.awt.GridBagConstraints consGridBagConstraints2 = 
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints1 = 
					new java.awt.GridBagConstraints();
				consGridBagConstraints2.insets = 
					new java.awt.Insets(4,15,5,3);
				consGridBagConstraints2.ipady = -2;
				consGridBagConstraints2.gridy = 1;
				consGridBagConstraints2.gridx = 0;
				consGridBagConstraints1.insets = 
					new java.awt.Insets(5,15,4,3);
				consGridBagConstraints1.ipady = -2;
				consGridBagConstraints1.ipadx = 104;
				consGridBagConstraints1.gridy = 0;
				consGridBagConstraints1.gridx = 0;
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setBorder(
					new javax.swing.border.TitledBorder(
						new javax.swing.border.EtchedBorder(), 
						SELECT_CHOICE));
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());
				ivjJPanel1.add(getradioFullRefund(), consGridBagConstraints1);
				ivjJPanel1.add(getradioPartial(), consGridBagConstraints2);
				ivjJPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel1.setMinimumSize(
					new java.awt.Dimension(228, 95));

				//javax.swing.border.Border b = new javax.swing.border.
				//TitledBorder(new javax.swing.border.EtchedBorder(), 
				//"Select Choice:");

				//ivjJPanel1.setBorder(b);
				// defect 7898
				// Changed from ButtonGroup to RTSButtonGroup
				caButtonGroup = new RTSButtonGroup();
				caButtonGroup.add(getradioFullRefund(), 
					gettxtAmountAuthorized());
				caButtonGroup.add(getradioPartial(), 
					gettxtAmountAuthorized());
				// end defect 7898

				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel1;
	}
	/**
	 * Return the radioFullRefund property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioFullRefund()
	{
		if (ivjradioFullRefund == null)
		{
			try
			{
				ivjradioFullRefund = new javax.swing.JRadioButton();
				ivjradioFullRefund.setName("radioFullRefund");
				ivjradioFullRefund.setMnemonic(70);
				ivjradioFullRefund.setText(FULL_REFUND);
				ivjradioFullRefund.setMaximumSize(
					new java.awt.Dimension(87, 22));
				ivjradioFullRefund.setActionCommand(FULL_REFUND);
				ivjradioFullRefund.setMinimumSize(
					new java.awt.Dimension(87, 22));
				// user code begin {1}
				// defect 7898
				//ivjradioFullRefund.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioFullRefund;
	}
	/**
	 * Return the radioPartial property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JRadioButton getradioPartial()
	{
		if (ivjradioPartial == null)
		{
			try
			{
				ivjradioPartial = new javax.swing.JRadioButton();
				ivjradioPartial.setName("radioPartial");
				ivjradioPartial.setMnemonic(80);
				ivjradioPartial.setText(PARTIAL_DOUBLE_REGIS);
				ivjradioPartial.setMaximumSize(
					new java.awt.Dimension(191, 22));
				ivjradioPartial.setActionCommand(PARTIAL_DOUBLE_REGIS);
				ivjradioPartial.setMinimumSize(
					new java.awt.Dimension(191, 22));
				// user code begin {1}
				// defect 7898
				//ivjradioPartial.addKeyListener(this);
				// end defect 7898
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioPartial;
	}
	/**
	 * Return the stcAmountAuthorized property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcAmountAuthorized()
	{
		if (ivjstcAmountAuthorized == null)
		{
			try
			{
				ivjstcAmountAuthorized = new javax.swing.JLabel();
				ivjstcAmountAuthorized.setName("stcAmountAuthorized");
				ivjstcAmountAuthorized.setText(AMT_AUTHORIZED);
				ivjstcAmountAuthorized.setMaximumSize(
					new java.awt.Dimension(109, 14));
				ivjstcAmountAuthorized.setMinimumSize(
					new java.awt.Dimension(109, 14));
				// user code begin {1}

				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcAmountAuthorized;
	}
	/**
	 * Return the stcLblEnterRegistration property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblEnterRegistration()
	{
		if (ivjstcLblEnterRegistration == null)
		{
			try
			{
				ivjstcLblEnterRegistration = new javax.swing.JLabel();
				ivjstcLblEnterRegistration.setName(
					"stcLblEnterRegistration");
				ivjstcLblEnterRegistration.setText(ENTER_REGIS_AMT);
				ivjstcLblEnterRegistration.setMaximumSize(
					new java.awt.Dimension(139, 14));
				ivjstcLblEnterRegistration.setMinimumSize(
					new java.awt.Dimension(139, 14));
				// user code begin {1}

				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblEnterRegistration;
	}
	/**
	 * Return the txtAmountAuthorized property value.
	 * 
	 * @return RTSInputField
	 */

	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private com
		.txdot
		.isd
		.rts
		.client
		.general
		.ui
		.RTSInputField gettxtAmountAuthorized()
	{
		if (ivjtxtAmountAuthorized == null)
		{
			try
			{
				ivjtxtAmountAuthorized =
					new com
						.txdot
						.isd
						.rts
						.client
						.general
						.ui
						.RTSInputField();
				ivjtxtAmountAuthorized.setName("txtAmountAuthorized");
				ivjtxtAmountAuthorized.setInput(
					RTSInputField.DOLLAR_ONLY);
				ivjtxtAmountAuthorized.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtAmountAuthorized.setMaxLength(
					AMT_AUTHORIZED_MAX_LEN);
				ivjtxtAmountAuthorized.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}

				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}

				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtAmountAuthorized;
	}
	/**
	 * Called whenever the part throws an exception.
	 * @param aeException java.lang.Throwable
	 */

	private void handleException(java.lang.Throwable aeException)
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
			setName(ScreenConstant.REG004_FRAME__NAME);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(450, 202);
			setTitle(ScreenConstant.REG004_FRAME_TITLE);
			setContentPane(
				getFrmRegistrationRefundREG004ContentPane1());
			// defect 7898
			// set RequestFocus to false so that when a hot key is used
			// to activate a radio button the focus moves to the next
			// field and is not moved back to the radio button.
			setRequestFocus(false);
			// end defect 7898
		}
		catch (java.lang.Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// user code begin {2}
		// user code end
	}
//	/**
//	 * Invoked when a key has been pressed.
//	 * 
//	 * @param aaKE KeyEvent
//	 */
//	public void keyPressed(java.awt.event.KeyEvent aaKE)
//	{
//		if (aaKE.getSource() instanceof JRadioButton)
//		{
//			//do refund choice arrow key movement
//			if (aaKE.getKeyCode() == KeyEvent.VK_UP
//				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN
//				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
//			{
//				// defect 7898
//				// Arrow Keys to follow 5.2.3 standard
////				if (getradioFullRefund().isSelected())
////				{
////					if (getradioPartial().isEnabled())
////					{
////						getradioPartial().setSelected(true);
////						getradioPartial().requestFocus();
////					}
////				}
////				else if (getradioPartial().isSelected())
////				{
////					if (getradioPartial().isEnabled())
////					{
////						getradioFullRefund().setSelected(true);
////						getradioFullRefund().requestFocus();
////					}
////				}
//				if (getradioFullRefund().hasFocus())
//				{
//					if (getradioPartial().isEnabled())
//					{
//						getradioPartial().requestFocus();
//					}
//				}
//				else if (getradioPartial().hasFocus())
//				{
//					if (getradioPartial().isEnabled())
//					{
//						getradioFullRefund().requestFocus();
//					}
//				}
//			}
//			// end defect 7898
//		}
//	}
	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs java.lang.String[]
	 */

	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmRegistrationRefundREG004 laFrmRegistrationRefundREG004;
			laFrmRegistrationRefundREG004 =
				new FrmRegistrationRefundREG004();
			laFrmRegistrationRefundREG004.setModal(true);
			laFrmRegistrationRefundREG004
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmRegistrationRefundREG004.show();
			java.awt.Insets insets =
				laFrmRegistrationRefundREG004.getInsets();
			laFrmRegistrationRefundREG004.setSize(
				laFrmRegistrationRefundREG004.getWidth()
					+ insets.left
					+ insets.right,
				laFrmRegistrationRefundREG004.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmRegistrationRefundREG004.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_EXCEPT_IN_MAIN);
			aeTHRWEx.printStackTrace(System.out);
		}
	}
	/**
	 * Set controller.
	 * 
	 * @param aaConttroller getController().Controller
	 */

	public void setController(AbstractViewController aaConttroller)
	{
		super.setController(aaConttroller);
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */

	public void setData(java.lang.Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			caVehInqData = (VehicleInquiryData) aaDataObject;
			RegistrationData laRegData =
				caVehInqData.getMfVehicleData().getRegData();
				
			Dollar laZero = new Dollar(CommonConstant.STR_ZERO_DOLLAR);
			if (laRegData.getRegRefAmt().compareTo(laZero) > 0
				&& laRegData.getRegInvldIndi() == 0)
			{
				getradioPartial().setSelected(true);
			}
			else
			{
				getradioFullRefund().setSelected(true);
			}

			gettxtAmountAuthorized().setText(
				laRegData.getRegRefAmt().toString());
			// defect 8494
			// Moved from windowOpened
			setDefaultFocusField(gettxtAmountAuthorized());
			// end defect 8494
		}
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaMapData
	 * @deprecated
	 */

	public void setData(java.util.Map aaMapData)
	{
		// Empty block of code
	}
	/**
	 * Set data to Vehicle Inquiry Object.
	 */
	private void setDataToVehObj()
	{

		RegistrationData laRegData =
			caVehInqData.getMfVehicleData().getRegData();

		if (laRegData != null)
		{
			String lsRefAmt = gettxtAmountAuthorized().getText().trim();
			Dollar laDlrRefAmt = new Dollar(lsRefAmt);

			// Set reg invalid if full refund and refund amount greater 
			//	than zero
			if (getradioFullRefund().isSelected()
				&& laDlrRefAmt.compareTo(new Dollar(
					CommonConstant.STR_ZERO_DOLLAR)) == 1)
			{
				laRegData.setRegInvldIndi(1);
			}
			else
			{
				laRegData.setRegInvldIndi(0);
			}

			// defect 9631
//			// CustBaseRegFee is no longer retrieved from MF (defect 9557)
//			// Adjust the cust base reg fee
//			Dollar laDlrCustRegFee =
//				new Dollar(laRegData.getCustBaseRegFee().getValue());
//			laDlrCustRegFee =
//				laDlrCustRegFee.add(laRegData.getCustDieselFee());
//			laRegData.setCustDieselFee(new Dollar(
//				CommonConstant.STR_ZERO_DOLLAR));
//			laDlrCustRegFee =
//				laDlrCustRegFee.add(laRegData.getRegRefAmt());
//			laDlrCustRegFee = laDlrCustRegFee.subtract(laDlrRefAmt);
//			if (laDlrCustRegFee.compareTo(new Dollar(
//				CommonConstant.STR_ZERO_DOLLAR)) == 1)
//			{
//				laRegData.setCustBaseRegFee(laDlrCustRegFee);
//			}
//			else
//			{
//				laRegData.setCustBaseRegFee(new Dollar(
//					CommonConstant.STR_ZERO_DOLLAR));
//			}
			// end 9631

			// Set the refund amount in registration data
			laRegData.setRegRefAmt(laDlrRefAmt);
		}

	}
// defect 8494
// Moved to setData
//	// defect 7898
//	// Window Listener added to allow .requestFocus() on open
//	/**
//	 * Focus on Inquiry text field when window opens
//	 * 
//	 * @param aaSearchData WindowEvent
//	 */
//	public void windowOpened(WindowEvent aaWE)
//	{
//		// This is pure notification, windowOpened has no duties.
//		// super.windowOpened(aaWE);
//		gettxtAmountAuthorized().requestFocus();
//	}
//	// end defect 7898
// end defect 8494

}
