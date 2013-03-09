package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.DisabledPlacardCustomerData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmDisabledPlacardInquiryMRG020.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created.
 * 							defect 9831 Ver Defect_POS_B 
 * K Harrell	10/30/2008	PlacardNo - AlphaNumeric No Space
 * 							Added Invitmno validation.
 * 							modify gettxtPlacardNumber(), validateData()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/17/2008	No longer validate Inventory Item No 
 * 							for Placard No. 
 * 							modify validateData()
 * 							defect 9867 Ver Defect_POS_B  
 * K Harrell	01/05/2009	Control focus upon return to MRG020. 
 * 							add getLastFocus() 
 * 							modify actionPerformed()
 * 							defect 9883 Ver Defect_POS_D
 * B Hargrove	03/25/2009	Add help. 
 * 							modify actionPerformed() 
 * 							defect 10004 Ver Defect_POS_E 
 * ---------------------------------------------------------------------
 */

/**
 * Inquiry Screen for Disabled Placard. 
 *
 * @version	Defect_POS_E	03/25/2009  
 * @author	Kathy Harrell	
 * <br>Creation Date:		10/21/2008
 */
public class FrmDisabledPlacardInquiryMRG020
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjJDialogBoxContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JRadioButton ivjradioCustId = null;
	private JRadioButton ivjradioDisabledPersonName = null;
	private JRadioButton ivjradioInstitutionName = null;
	private JRadioButton ivjradioPlacardNumber = null;
	private JLabel ivjstcLblFirstName = null;
	private JLabel ivjstcLblLastName = null;
	private JLabel ivjstcLblMiddleInitial = null;
	private JLabel ivjstcLblReEnterId = null;
	private RTSInputField ivjtxtCustId = null;
	private RTSInputField ivjtxtFirstName = null;
	private RTSInputField ivjtxtInstitutionName = null;
	private RTSInputField ivjtxtLastName = null;
	private RTSInputField ivjtxtMiddleInitial = null;
	private RTSInputField ivjtxtPlacardNumber = null;
	private RTSInputField ivjtxtReenterId = null;

	private final static int CUST_ID_LENGTH = 15;
	private final static int FIRST_NAME_LENGTH = 30;
	private final static int INSTITUTION_NAME_LENGTH = 60;
	private final static int LAST_NAME_LENGTH = 30;
	private final static int MI_LENGTH = 1;
	private final static int PLACARD_NUMBER_LENGTH = 10;

	private final static String CUST_ID = "Applicant Id";
	private final static String DISABLED_PERSON_NAME =
		"Disabled PersonOld Name";
	private final static String FIRST_NAME = "First Name:";
	private final static String INSTITUTION_NAME = "Institution Name";
	private final static String LAST_NAME = "Last Name:";
	private final static String MIDDLE_INITIAL = "Middle Initial:";
	private final static String PLACARD_NUMBER = "Placard Number";
	private final static String REENTER_ID = "Re-enter Id";
	private final static String SELECT_ONE = "Select Inquiry Key:";
	private final static String FRM_TITLE =
		"Disabled Placard Inquiry    MRG020";

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmDisabledPlacardInquiryMRG020 aaFrmMRG020;
			aaFrmMRG020 = new FrmDisabledPlacardInquiryMRG020();
			aaFrmMRG020.setModal(true);
			aaFrmMRG020
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			aaFrmMRG020.show();
			java.awt.Insets laInsets = aaFrmMRG020.getInsets();
			aaFrmMRG020.setSize(
				aaFrmMRG020.getWidth() + laInsets.left + laInsets.right,
				aaFrmMRG020.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			aaFrmMRG020.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmDisabledPlacardInquiryMRG020 constructor comment.
	 */
	public FrmDisabledPlacardInquiryMRG020()
	{
		super();
		initialize();
	}

	/**
	 * Creates a FrmDisabledPlacardInquiryMRG020 with the parent
	 * 
	 * @param aaParent	Dialog
	 */
	public FrmDisabledPlacardInquiryMRG020(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Creates a FrmDisabledPlacardInquiryMRG020 with the parent
	 * 
	 * @param aaParent	JFrame 
	 */
	public FrmDisabledPlacardInquiryMRG020(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE	ActionEvent
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

			if (aaAE.getSource() instanceof JRadioButton)
			{
				setRadioButtons();
				if (getradioCustId().isSelected())
				{
					gettxtCustId().requestFocus();
				}
				else if (getradioDisabledPersonName().isSelected())
				{
					gettxtFirstName().requestFocus();
				}
				else if (getradioPlacardNumber().isSelected())
				{
					gettxtPlacardNumber().requestFocus();
				}
				else
				{
					gettxtInstitutionName().requestFocus();
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				DisabledPlacardCustomerData laDPCustData =
					new DisabledPlacardCustomerData();

				if (validateData(laDPCustData))
				{
					// defect 9883
					// Save and restore focus according to 
					//  current selection  
					Component laLastFocus = getLastFocus();
 
					getController().processData(
						VCDisabledPlacardInquiryMRG020.ENTER,
						laDPCustData);

					laLastFocus.requestFocus();
					// end defect 9883 
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
				// defect 10004
				//RTSException leRTSEx =
				//	new RTSException(
				//		RTSException.WARNING_MESSAGE,
				//		ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
				//		"Information");
				//leRTSEx.displayError(this);
				RTSHelp.displayHelp(RTSHelp.MRG020);
				// end defect 10004
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * This method initializes the ENTER, CANCEL, HELP panel.
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
				ivjButtonPanel1.setBounds(170, 312, 216, 36);
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
	 * Return the JDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJDialogBoxContentPane()
	{
		if (ivjJDialogBoxContentPane == null)
		{
			try
			{
				ivjJDialogBoxContentPane = new JPanel();
				ivjJDialogBoxContentPane.setName(
					"JDialogBoxContentPane");
				ivjJDialogBoxContentPane.setLayout(null);
				ivjJDialogBoxContentPane.add(getButtonPanel1(), null);
				ivjJDialogBoxContentPane.add(getJPanel1(), null);
				RTSButtonGroup laButtonGrp = new RTSButtonGroup();
				laButtonGrp.add(getradioCustId());
				laButtonGrp.add(getradioPlacardNumber());
				laButtonGrp.add(getradioDisabledPersonName());
				laButtonGrp.add(getradioInstitutionName());

			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJDialogBoxContentPane;
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
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(null);
				ivjJPanel1.add(getradioInstitutionName(), null);
				ivjJPanel1.add(gettxtInstitutionName(), null);
				ivjJPanel1.add(getradioPlacardNumber(), null);
				ivjJPanel1.add(gettxtLastName(), null);
				ivjJPanel1.add(getradioDisabledPersonName(), null);
				ivjJPanel1.add(gettxtCustId(), null);
				ivjJPanel1.add(getradioCustId(), null);
				ivjJPanel1.add(gettxtPlacardNumber(), null);
				ivjJPanel1.add(getstcLblFirstName(), null);
				ivjJPanel1.add(getstcLblMiddleInitial(), null);
				ivjJPanel1.add(gettxtMiddleInitial(), null);
				ivjJPanel1.add(getstcLblLastName(), null);
				ivjJPanel1.add(gettxtFirstName(), null);
				ivjJPanel1.add(gettxtReEnterId(), null);
				ivjJPanel1.add(getstcLblReEnterId(), null);
				ivjJPanel1.setBounds(9, 20, 539, 289);
				// user code begin {1}
				Border laBorder =
					new TitledBorder(new EtchedBorder(), SELECT_ONE);
				ivjJPanel1.setBorder(laBorder);
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
	 * Return default component given selected radio button
	 * 
	 * @return Component 
	 */
	private Component getLastFocus()
	{
		Component laLastFocus = gettxtCustId();
		if (getradioPlacardNumber().isSelected())
		{
			laLastFocus = gettxtPlacardNumber();
		}
		else if (getradioDisabledPersonName().isSelected())
		{
			laLastFocus = gettxtFirstName();
		}
		else if (getradioInstitutionName().isSelected())
		{
			laLastFocus = gettxtInstitutionName();
		}
		return laLastFocus;
	}
	
	/**
	 * Return the ivjradioCustId property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private JRadioButton getradioCustId()
	{
		if (ivjradioCustId == null)
		{
			try
			{
				ivjradioCustId = new JRadioButton();
				ivjradioCustId.setBounds(45, 36, 117, 24);
				ivjradioCustId.setName("radioApplicantId");
				ivjradioCustId.setText(CUST_ID);
				// user code begin {1}				
				ivjradioCustId.addActionListener(this);
				ivjradioCustId.setMnemonic(KeyEvent.VK_A);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCustId;
	}

	/**
	 * Return the ivjradioDisabledPersonName property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private JRadioButton getradioDisabledPersonName()
	{
		if (ivjradioDisabledPersonName == null)
		{
			try
			{
				ivjradioDisabledPersonName = new JRadioButton();
				ivjradioDisabledPersonName.setBounds(45, 155, 157, 24);
				ivjradioDisabledPersonName.setName(
					"radioDisabledPersonName");
				ivjradioDisabledPersonName.setText(
					DISABLED_PERSON_NAME);
				// user code begin {1}
				ivjradioDisabledPersonName.addActionListener(this);
				ivjradioDisabledPersonName.setMnemonic(KeyEvent.VK_D);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioDisabledPersonName;
	}

	/**
	 * Return the ivjradioInstitutionName property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private JRadioButton getradioInstitutionName()
	{
		if (ivjradioInstitutionName == null)
		{
			try
			{
				ivjradioInstitutionName = new JRadioButton();
				ivjradioInstitutionName.setBounds(45, 256, 146, 24);
				ivjradioInstitutionName.setName("radioInstitutionName");
				ivjradioInstitutionName.setText(INSTITUTION_NAME);
				// user code begin {1}
				ivjradioInstitutionName.addActionListener(this);
				ivjradioInstitutionName.setMnemonic(KeyEvent.VK_I);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioInstitutionName;
	}

	/**
	 * Return the ivjradioPlacardNumber property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */

	private JRadioButton getradioPlacardNumber()
	{
		if (ivjradioPlacardNumber == null)
		{
			try
			{
				ivjradioPlacardNumber = new JRadioButton();
				ivjradioPlacardNumber.setBounds(45, 119, 118, 24);
				ivjradioPlacardNumber.setName("placardNumber");
				ivjradioPlacardNumber.setText(PLACARD_NUMBER);
				// user code begin {1}
				ivjradioPlacardNumber.addActionListener(this);
				ivjradioPlacardNumber.setMnemonic(KeyEvent.VK_P);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioPlacardNumber;
	}

	/**
	 * This method initializes ivjstcLblFirstName
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblFirstName()
	{
		if (ivjstcLblFirstName == null)
		{
			ivjstcLblFirstName = new JLabel();
			ivjstcLblFirstName.setSize(64, 20);
			ivjstcLblFirstName.setText(FIRST_NAME);
			ivjstcLblFirstName.setLocation(226, 157);
			ivjstcLblFirstName.setEnabled(false);
		}
		return ivjstcLblFirstName;
	}

	/**
	 * This method initializes ivjstcLblLastName
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLastName()
	{
		if (ivjstcLblLastName == null)
		{
			ivjstcLblLastName = new JLabel();
			ivjstcLblLastName.setSize(64, 20);
			ivjstcLblLastName.setText(LAST_NAME);
			ivjstcLblLastName.setLocation(226, 224);
			ivjstcLblLastName.setEnabled(false);
		}
		return ivjstcLblLastName;
	}

	/**
	 * This method initializes ivjstcLblMiddleInitial
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMiddleInitial()
	{
		if (ivjstcLblMiddleInitial == null)
		{
			ivjstcLblMiddleInitial = new JLabel();
			ivjstcLblMiddleInitial.setSize(73, 20);
			ivjstcLblMiddleInitial.setText(MIDDLE_INITIAL);
			ivjstcLblMiddleInitial.setLocation(217, 190);
			ivjstcLblMiddleInitial.setEnabled(false);
		}
		return ivjstcLblMiddleInitial;
	}

	/**
	 * This method initializes ivjstcLblReEnterId
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblReEnterId()
	{
		if (ivjstcLblReEnterId == null)
		{
			ivjstcLblReEnterId = new JLabel();
			ivjstcLblReEnterId.setBounds(69, 80, 72, 21);
			ivjstcLblReEnterId.setText(REENTER_ID);
			ivjstcLblReEnterId.setEnabled(true);
		}
		return ivjstcLblReEnterId;
	}

	/**
	 * Return the ivjtxtCustId property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtCustId()
	{
		if (ivjtxtCustId == null)
		{
			try
			{
				ivjtxtCustId = new RTSInputField();
				ivjtxtCustId.setSize(165, 20);
				ivjtxtCustId.setLocation(209, 40);
				// user code begin {1}
				ivjtxtCustId.setEnabled(true);
				ivjtxtCustId.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtCustId.setMaxLength(CUST_ID_LENGTH);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtCustId;
	}

	/**
	 * This method initializes ivjtxtFirstName
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtFirstName()
	{
		if (ivjtxtFirstName == null)
		{
			try
			{
				ivjtxtFirstName = new RTSInputField();
				ivjtxtFirstName.setBounds(305, 157, 205, 20);
				// user code begin {1}
				ivjtxtFirstName.setMaxLength(FIRST_NAME_LENGTH);
				ivjtxtFirstName.setInput(RTSInputField.DEFAULT);
				ivjtxtFirstName.setEnabled(false);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}

		}
		return ivjtxtFirstName;
	}

	/**
	 * Return the RTSInputFieldproperty value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtInstitutionName()
	{
		if (ivjtxtInstitutionName == null)
		{
			try
			{
				ivjtxtInstitutionName = new RTSInputField();
				ivjtxtInstitutionName.setBounds(209, 258, 301, 20);
				// user code begin {1}
				ivjtxtInstitutionName.setInput(RTSInputField.DEFAULT);
				ivjtxtInstitutionName.setMaxLength(
					INSTITUTION_NAME_LENGTH);
				ivjtxtInstitutionName.setEnabled(false);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtInstitutionName;
	}

	/**
	 * Return the ivjtxtDisabledPersonLastName property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtLastName()
	{
		if (ivjtxtLastName == null)
		{
			try
			{
				ivjtxtLastName = new RTSInputField();
				ivjtxtLastName.setBounds(305, 224, 205, 20);
				// user code begin {1}
				ivjtxtLastName.setInput(RTSInputField.DEFAULT);
				ivjtxtLastName.setEnabled(false);
				ivjtxtLastName.setMaxLength(LAST_NAME_LENGTH);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtLastName;
	}

	/**
	 * This method initializes ivjtxtMiddleInitial
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMiddleInitial()
	{
		if (ivjtxtMiddleInitial == null)
		{
			try
			{
				ivjtxtMiddleInitial = new RTSInputField();
				ivjtxtMiddleInitial.setBounds(305, 190, 22, 20);
				// user code begin {1}
				ivjtxtMiddleInitial.setMaxLength(MI_LENGTH);
				ivjtxtMiddleInitial.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtMiddleInitial.setEnabled(false);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMiddleInitial;
	}

	/**
	 * Return the ivjtxtPlacardNumber property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtPlacardNumber()
	{
		if (ivjtxtPlacardNumber == null)
		{
			try
			{
				ivjtxtPlacardNumber = new RTSInputField();
				ivjtxtPlacardNumber.setSize(165, 20);
				ivjtxtPlacardNumber.setLocation(209, 123);
				// user code begin {1}
				ivjtxtPlacardNumber.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				ivjtxtPlacardNumber.setEnabled(false);
				ivjtxtPlacardNumber.setMaxLength(PLACARD_NUMBER_LENGTH);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtPlacardNumber;
	}

	/**
	 * This method initializes ivjtxtReenterId
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtReEnterId()
	{
		if (ivjtxtReenterId == null)
		{
			try
			{
				ivjtxtReenterId = new RTSInputField();
				ivjtxtReenterId.setBounds(209, 83, 165, 20);
				// user code begin {1}
				ivjtxtReenterId.setEnabled(true);
				ivjtxtReenterId.setMaxLength(CUST_ID_LENGTH);
				ivjtxtReenterId.setInput(
					RTSInputField.ALPHANUMERIC_NOSPACE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}

		}
		return ivjtxtReenterId;
	}

	/**
	* Called whenever the part throws an exception.
	* 
	* @param aeException 
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
			setName("FrmDisabledPlacardInquiryMRG020");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(564, 382);
			setModal(true);
			setTitle(FRM_TITLE);
			setContentPane(getJDialogBoxContentPane());
			setRequestFocus(false);
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaObject	Object 
	 */
	public void setData(Object aaObject)
	{
		getradioCustId().setSelected(true);
		setDefaultFocusField(gettxtCustId());
	}

	/**
	 * For radio buttons & entry fields, clear text, enable/disable and 
	 * set focus.
	 */
	private void setRadioButtons()
	{
		// Reset all RTSInput fields 
		gettxtCustId().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtReEnterId().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtPlacardNumber().setText(CommonConstant.STR_SPACE_EMPTY);

		gettxtFirstName().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtMiddleInitial().setText(CommonConstant.STR_SPACE_EMPTY);
		gettxtLastName().setText(CommonConstant.STR_SPACE_EMPTY);

		gettxtInstitutionName().setText(CommonConstant.STR_SPACE_EMPTY);

		// Enable RTSInput field base upon Radio selection 
		gettxtCustId().setEnabled(getradioCustId().isSelected());
		getstcLblReEnterId().setEnabled(getradioCustId().isSelected());
		gettxtReEnterId().setEnabled(getradioCustId().isSelected());
		gettxtPlacardNumber().setEnabled(
			getradioPlacardNumber().isSelected());
		gettxtInstitutionName().setEnabled(
			getradioInstitutionName().isSelected());

		// Enable Labels and RTSInput Fields for
		// Disabled PersonOld
		boolean lbDisabledPerson =
			getradioDisabledPersonName().isSelected();

		gettxtFirstName().setEnabled(lbDisabledPerson);
		gettxtMiddleInitial().setEnabled(lbDisabledPerson);
		gettxtLastName().setEnabled(lbDisabledPerson);
		getstcLblFirstName().setEnabled(lbDisabledPerson);
		getstcLblMiddleInitial().setEnabled(lbDisabledPerson);
		getstcLblLastName().setEnabled(lbDisabledPerson);
	}

	/**
	 * Validate Data  
	 *
	 * @param aaDPAData
	 * @return boolean
	 */
	private boolean validateData(DisabledPlacardCustomerData aaDPCustData)
	{
		boolean lbReturn = true;

		RTSException leRTSEx = new RTSException();
		try
		{
			if (getradioCustId().isSelected())
			{
				String lsCustId = gettxtCustId().getText().trim();
				String lsReEnterId = gettxtReEnterId().getText().trim();

				if (lsCustId.length() == 0
					|| lsReEnterId.length() == 0)
				{
					if (lsCustId.length() == 0)
					{
						leRTSEx.addException(
							new RTSException(150),
							gettxtCustId());
					}
					if (lsReEnterId.length() == 0)
					{
						leRTSEx.addException(
							new RTSException(150),
							gettxtReEnterId());
						throw leRTSEx;
					}
				}
				else if (!lsCustId.equals(lsReEnterId))
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtReEnterId());
					throw leRTSEx;
				}

				aaDPCustData.setSearchType(
					MiscellaneousRegConstant.CUST_ID);
				aaDPCustData.setCustId(lsCustId);
			}
			else if (getradioPlacardNumber().isSelected())
			{
				String lsPlacardNo =
					gettxtPlacardNumber().getText().trim();

				if (lsPlacardNo.length() == 0)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtPlacardNumber());
					throw leRTSEx;
				}

				// defect 9867 
				//				ValidateInventoryPattern laValidateInventoryPattern =
				//					new ValidateInventoryPattern();
				//
				//				ProcessInventoryData laProcessInventoryData =
				//					new ProcessInventoryData();
				//				laProcessInventoryData.setInvQty(1);
				//				laProcessInventoryData.setInvItmNo(lsPlacardNo);
				//				laProcessInventoryData.setInvItmYr(0);
				//				String larrItmCd[] =
				//					{
				//						MiscellaneousRegConstant.NONMOBLTY_PLCRD_ITMCD,
				//						MiscellaneousRegConstant.MOBLTY_PLCRD_ITMCD };
				//
				//				for (int i = 0; i < larrItmCd.length; i++)
				//				{
				//					String lsItmCd = larrItmCd[i];
				//					laProcessInventoryData.setItmCd(lsItmCd);
				//					try
				//					{
				//						laValidateInventoryPattern.validateItmNoInput(
				//							laProcessInventoryData
				//								.convertToInvAlloctnUIData(
				//								laProcessInventoryData));
				//						break;
				//					}
				//					catch (RTSException aeRTSEx1)
				//					{
				//						leRTSEx.addException(
				//							aeRTSEx1,
				//							gettxtPlacardNumber());
				//
				//						if (i == 1)
				//						{
				//							throw leRTSEx;
				//						}
				//					}
				//				}
				// end defect 9867 

				aaDPCustData.setSearchType(
					MiscellaneousRegConstant.PLACARD_NUMBER);
				aaDPCustData.setPlcrdNo(lsPlacardNo);
			}
			else if (getradioDisabledPersonName().isSelected())
			{
				String lsFirstName = gettxtFirstName().getText().trim();
				String lsMI = gettxtMiddleInitial().getText().trim();
				String lsLastName = gettxtLastName().getText().trim();

				if (lsFirstName.length() == 0
					&& lsMI.length() == 0
					&& lsLastName.length() == 0)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtFirstName());
					leRTSEx.addException(
						new RTSException(150),
						gettxtMiddleInitial());
					leRTSEx.addException(
						new RTSException(150),
						gettxtLastName());
					throw leRTSEx;
				}

				aaDPCustData.setSearchType(
					MiscellaneousRegConstant.DISABLED_NAME);
				aaDPCustData.setDsabldFrstName(lsFirstName);
				aaDPCustData.setDsabldMI(lsMI);
				aaDPCustData.setDsabldLstName(lsLastName);
			}
			else if (getradioInstitutionName().isSelected())
			{
				String lsInstitutionName =
					gettxtInstitutionName().getText().trim();

				if (lsInstitutionName.length() == 0)
				{
					leRTSEx.addException(
						new RTSException(150),
						gettxtInstitutionName());
					throw leRTSEx;
				}

				aaDPCustData.setSearchType(
					MiscellaneousRegConstant.INSTITUTION_NAME);
				aaDPCustData.setInstName(lsInstitutionName);
			}
			aaDPCustData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			aaDPCustData.setWsId(SystemProperty.getWorkStationId());
			aaDPCustData.setEmpId(SystemProperty.getCurrentEmpId());
			aaDPCustData.setTransCd(getController().getTransCode());
		}
		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.isValidationError())
			{
				leRTSEx.displayError(this);
				leRTSEx.getFirstComponent().requestFocus();
				lbReturn = false;
			}
		}
		return lbReturn;
	}
}
