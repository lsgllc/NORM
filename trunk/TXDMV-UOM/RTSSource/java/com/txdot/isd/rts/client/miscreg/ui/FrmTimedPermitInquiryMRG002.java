package com.txdot.isd.rts.client.miscreg.ui;import java.awt.Component;import java.awt.Dialog;import java.awt.event.ActionEvent;import java.awt.event.ActionListener;import java.awt.event.KeyEvent;import java.util.Vector;import javax.swing.*;import javax.swing.border.Border;import javax.swing.border.EtchedBorder;import javax.swing.border.TitledBorder;import com.txdot.isd.rts.client.general.ui.*;import com.txdot.isd.rts.services.data.GeneralSearchData;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.CommonValidations;import com.txdot.isd.rts.services.util.RTSDate;import com.txdot.isd.rts.services.util.SystemProperty;import com.txdot.isd.rts.services.util.UtilityMethods;import com.txdot.isd.rts.services.util.constants.CommonConstant;import com.txdot.isd.rts.services.util.constants.ErrorsConstant;import com.txdot.isd.rts.services.util.constants.InventoryConstant;import com.txdot.isd.rts.services.util.constants.TransCdConstant;/* * FrmTimedPermitInquiryMRG002.java * * (c) Texas Department of Transportation 2010 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * K Harrell	05/24/2010	Created * 							defect 10491 Ver 6.5.0  * K Harrell	07/07/2010	Do not show Charge Fee checkbox if HQ * 							modify setData() * 							defect 10491 Ver 6.5.0  * K Harrell	07/18/2010	Throw Error Msg 555 if VIN length < 17 * 							modify validateData()  * 							defect 10491 Ver 6.5.0  * --------------------------------------------------------------------- *//** * Inquiry Screen for Timed Permit (Inquiry & Duplicate Receipt)  * * @version	6.5.0 			07/18/2010 * @author	Kathy Harrell * <br>Creation Date:		05/24/2010	10:22:17 *//* &FrmTimedPermitInquiryMRG002& */public class FrmTimedPermitInquiryMRG002	extends RTSDialogBox	implements ActionListener{/* &FrmTimedPermitInquiryMRG002'ivjbtnResetDates& */	private JButton ivjbtnResetDates = null;/* &FrmTimedPermitInquiryMRG002'ivjButtonPanel1& */	private ButtonPanel ivjButtonPanel1 = null;/* &FrmTimedPermitInquiryMRG002'ivjchkChargeFee& */	private JCheckBox ivjchkChargeFee = null;/* &FrmTimedPermitInquiryMRG002'ivjJDialogBoxContentPane& */	private JPanel ivjJDialogBoxContentPane = null;/* &FrmTimedPermitInquiryMRG002'ivjJPanelBeginEndDate& */	private JPanel ivjJPanelBeginEndDate = null;/* &FrmTimedPermitInquiryMRG002'ivjJPanelDateRange& */	private JPanel ivjJPanelDateRange = null;/* &FrmTimedPermitInquiryMRG002'ivjJPanelInquiryKey& */	private JPanel ivjJPanelInquiryKey = null;/* &FrmTimedPermitInquiryMRG002'ivjradioBusinessName& */	private JRadioButton ivjradioBusinessName = null;/* &FrmTimedPermitInquiryMRG002'ivjradioLastName& */	private JRadioButton ivjradioLastName = null;/* &FrmTimedPermitInquiryMRG002'ivjradioPermitNo& */	private JRadioButton ivjradioPermitNo = null;/* &FrmTimedPermitInquiryMRG002'ivjradioVIN& */	private JRadioButton ivjradioVIN = null;/* &FrmTimedPermitInquiryMRG002'ivjstcLblBeginDate& */	private JLabel ivjstcLblBeginDate = null;/* &FrmTimedPermitInquiryMRG002'ivjstcLblEndDate& */	private JLabel ivjstcLblEndDate = null;/* &FrmTimedPermitInquiryMRG002'ivjtxtBeginDate& */	private RTSDateField ivjtxtBeginDate = null;/* &FrmTimedPermitInquiryMRG002'ivjtxtBusinessName& */	private RTSInputField ivjtxtBusinessName = null;/* &FrmTimedPermitInquiryMRG002'ivjtxtEndDate& */	private RTSDateField ivjtxtEndDate = null;/* &FrmTimedPermitInquiryMRG002'ivjtxtLastName& */	private RTSInputField ivjtxtLastName = null;/* &FrmTimedPermitInquiryMRG002'ivjtxtPermitNo& */	private RTSInputField ivjtxtPermitNo = null;/* &FrmTimedPermitInquiryMRG002'ivjtxtVIN& */	private RTSInputField ivjtxtVIN = null;	// Object/* &FrmTimedPermitInquiryMRG002'caLastFocus& */	Component caLastFocus = getradioPermitNo();	// boolean /* &FrmTimedPermitInquiryMRG002'cbPRMDUP& */	boolean cbPRMDUP;	// String /* &FrmTimedPermitInquiryMRG002'csTransCd& */	String csTransCd;	// Vector/* &FrmTimedPermitInquiryMRG002'cvMFValid& */	Vector cvMFValid = new Vector();	// Constants/* &FrmTimedPermitInquiryMRG002'FRM_TITLE& */	private final static String FRM_TITLE =		"Timed Permit Inquiry     MRG002";/* &FrmTimedPermitInquiryMRG002'KEY_PANEL_TITLE& */	private final static String KEY_PANEL_TITLE = "Select Inquiry Key:";/* &FrmTimedPermitInquiryMRG002'DATE_RANGE_PANEL_TITLE& */	private final static String DATE_RANGE_PANEL_TITLE =		"Enter Date Range (optional):";	/**	 * main entrypoint - starts the part when it is run as an application	 * 	 * @param aarrArgs String[]	 *//* &FrmTimedPermitInquiryMRG002.main& */	public static void main(String[] aarrArgs)	{		try		{			FrmTimedPermitInquiryMRG002 aaFrmMRG002;			aaFrmMRG002 = new FrmTimedPermitInquiryMRG002();			aaFrmMRG002.setModal(true);			aaFrmMRG002				.addWindowListener(new java.awt.event.WindowAdapter()			{				public void windowClosing(					java.awt.event.WindowEvent aaWE)				{					System.exit(0);				}			});			aaFrmMRG002.show();			java.awt.Insets laInsets = aaFrmMRG002.getInsets();			aaFrmMRG002.setSize(				aaFrmMRG002.getWidth() + laInsets.left + laInsets.right,				aaFrmMRG002.getHeight()					+ laInsets.top					+ laInsets.bottom);			aaFrmMRG002.setVisible(true);		}		catch (Throwable aeException)		{			aeException.printStackTrace(System.out);		}	}	/**	 * FrmTimedPermitInquiryMRG002.java Constructor	 * 	 *//* &FrmTimedPermitInquiryMRG002.FrmTimedPermitInquiryMRG002& */	public FrmTimedPermitInquiryMRG002()	{		super();	}	/**	 * Creates a FrmTimedPermitInquiryMRG002 with the parent	 * 	 * @param aaParent	Dialog	 *//* &FrmTimedPermitInquiryMRG002.FrmTimedPermitInquiryMRG002$1& */	public FrmTimedPermitInquiryMRG002(Dialog aaParent)	{		super(aaParent);		initialize();	}	/**	 * Creates a FrmTimedPermitInquiryMRG002 with the parent	 * 	 * @param aaParent	JFrame 	 *//* &FrmTimedPermitInquiryMRG002.FrmTimedPermitInquiryMRG002$2& */	public FrmTimedPermitInquiryMRG002(JFrame aaParent)	{		super(aaParent);		initialize();	}	/**	 * Invoked when an action occurs.	 * 	 * @param aaAE	ActionEvent	 *//* &FrmTimedPermitInquiryMRG002.actionPerformed& */	public void actionPerformed(ActionEvent aaAE)	{		if (!startWorking())		{			return;		}		try		{			clearAllColor(this);			if (aaAE.getSource() instanceof JRadioButton)			{				handleRadioButtonSelection();			}			else if (				aaAE.getSource() == getButtonPanel1().getBtnEnter())			{				processEnter();			}			else if (				aaAE.getSource() == getButtonPanel1().getBtnCancel())			{				getController().processData(					AbstractViewController.CANCEL,					null);			}			else if (aaAE.getSource() == getbtnResetDates())			{				resetDateFields();			}			else if (				aaAE.getSource() == getButtonPanel1().getBtnHelp())			{				RTSException leRTSEx =					new RTSException(						RTSException.WARNING_MESSAGE,						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,						"Information");				leRTSEx.displayError(this);			}		}		finally		{			doneWorking();		}	}	/**	 * Set values in General Search Data Object	 * 	 * @return GeneralSearchData	 *//* &FrmTimedPermitInquiryMRG002.buildGSD& */	private GeneralSearchData buildGSD()	{		GeneralSearchData laGSD = new GeneralSearchData();		if (getradioPermitNo().isSelected())		{			laGSD.setKey1(CommonConstant.PRMT_PRMTNO);			laGSD.setKey2(gettxtPermitNo().getText().trim());		}		else if (getradioVIN().isSelected())		{			laGSD.setKey1(CommonConstant.PRMT_VIN);			laGSD.setKey2(gettxtVIN().getText().trim());		}		else if (getradioLastName().isSelected())		{			laGSD.setKey1(CommonConstant.PRMT_LSTNAME);			laGSD.setKey2(gettxtLastName().getText().trim());		}		else if (getradioBusinessName().isSelected())		{			laGSD.setKey1(CommonConstant.PRMT_BSNNAME);			laGSD.setKey2(gettxtBusinessName().getText().trim());		}		if (!gettxtBeginDate().isDateEmpty())		{			laGSD.setDate1(gettxtBeginDate().getDate());			laGSD.setDate2(gettxtEndDate().getDate());		}		if (cbPRMDUP && getchkChargeFee().isSelected())		{			laGSD.setIntKey4(1);		}		laGSD.setKey3(csTransCd);		return laGSD;	}	/**	 * This method initializes jButton	 * 	 * @return JButton	 *//* &FrmTimedPermitInquiryMRG002.getbtnResetDates& */	private JButton getbtnResetDates()	{		if (ivjbtnResetDates == null)		{			ivjbtnResetDates = new javax.swing.JButton();			ivjbtnResetDates.setBounds(362, 60, 103, 23);			ivjbtnResetDates.setText("Reset Dates");			ivjbtnResetDates.setMnemonic(java.awt.event.KeyEvent.VK_R);			ivjbtnResetDates.setEnabled(false);			ivjbtnResetDates.addActionListener(this);		}		return ivjbtnResetDates;	}	/**	 * This method initializes the ENTER, CANCEL, HELP panel.	 * 	 * @return ButtonPanel	 *//* &FrmTimedPermitInquiryMRG002.getButtonPanel1& */	private ButtonPanel getButtonPanel1()	{		if (ivjButtonPanel1 == null)		{			try			{				ivjButtonPanel1 = new ButtonPanel();				ivjButtonPanel1.setBounds(169, 323, 216, 36);				ivjButtonPanel1.setName("ivjButtonPanel1");				ivjButtonPanel1.setMinimumSize(					new java.awt.Dimension(217, 35));				// user code begin {1}				ivjButtonPanel1.addActionListener(this);				ivjButtonPanel1.setAsDefault(this);				// user code end			}			catch (Throwable aeIvjEx)			{				// user code begin {2}				// user code end				handleException(aeIvjEx);			}		}		return ivjButtonPanel1;	}	/**	 * This method initializes ivjchkChargeFee	 * 	 * @return JCheckBox	 *//* &FrmTimedPermitInquiryMRG002.getchkChargeFee& */	private JCheckBox getchkChargeFee()	{		if (ivjchkChargeFee == null)		{			ivjchkChargeFee = new javax.swing.JCheckBox();			ivjchkChargeFee.setBounds(362, 32, 89, 20);			ivjchkChargeFee.setText("Charge Fee");			ivjchkChargeFee.setMnemonic(java.awt.event.KeyEvent.VK_F);		}		return ivjchkChargeFee;	}	/**	 * Return the ivjJDialogBoxContentPane property value.	 * 	 * @return JPanel	 *//* &FrmTimedPermitInquiryMRG002.getJDialogBoxContentPane& */	private JPanel getJDialogBoxContentPane()	{		if (ivjJDialogBoxContentPane == null)		{			try			{				ivjJDialogBoxContentPane = new JPanel();				ivjJDialogBoxContentPane.setName(					"ivjJDialogBoxContentPane");				ivjJDialogBoxContentPane.setLayout(null);				ivjJDialogBoxContentPane.add(getButtonPanel1(), null);				ivjJDialogBoxContentPane.add(					getJPanelInquiryKey(),					null);				ivjJDialogBoxContentPane.add(					getJPanelDateRange(),					null);				ivjJDialogBoxContentPane.setSize(564, 362);				RTSButtonGroup laButtonGrp = new RTSButtonGroup();				laButtonGrp.add(getradioPermitNo());				laButtonGrp.add(getradioVIN());				laButtonGrp.add(getradioLastName());				laButtonGrp.add(getradioBusinessName());			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjJDialogBoxContentPane;	}	/**	 * This method initializes jPanel1	 * 	 * @return JPanel	 *//* &FrmTimedPermitInquiryMRG002.getJPanelBeginEndDate& */	private JPanel getJPanelBeginEndDate()	{		if (ivjJPanelBeginEndDate == null)		{			ivjJPanelBeginEndDate = new JPanel();			ivjJPanelBeginEndDate.setLayout(null);			ivjJPanelBeginEndDate.add(getstcLblBeginDate(), null);			ivjJPanelBeginEndDate.add(getstcLblEndDate(), null);			ivjJPanelBeginEndDate.add(gettxtBeginDate(), null);			ivjJPanelBeginEndDate.add(gettxtEndDate(), null);			ivjJPanelBeginEndDate.setBounds(103, 29, 183, 60);		}		return ivjJPanelBeginEndDate;	}	/**		 * This method initializes jPanel		 * 		 * @return JPanel		 *//* &FrmTimedPermitInquiryMRG002.getJPanelDateRange& */	private JPanel getJPanelDateRange()	{		if (ivjJPanelDateRange == null)		{			ivjJPanelDateRange = new javax.swing.JPanel();			ivjJPanelDateRange.setLayout(null);			ivjJPanelDateRange.add(getJPanelBeginEndDate(), null);			ivjJPanelDateRange.add(getchkChargeFee(), null);			ivjJPanelDateRange.add(getbtnResetDates(), null);			ivjJPanelDateRange.setSize(525, 105);			Border laBorder =				new TitledBorder(					new EtchedBorder(),					DATE_RANGE_PANEL_TITLE);			ivjJPanelDateRange.setBorder(laBorder);			ivjJPanelDateRange.setLocation(9, 212);		}		return ivjJPanelDateRange;	}	/**	 * Return the ivjJPanelInquiryKey property value.	 * 	 * @return JPanel	 *//* &FrmTimedPermitInquiryMRG002.getJPanelInquiryKey& */	private JPanel getJPanelInquiryKey()	{		if (ivjJPanelInquiryKey == null)		{			try			{				ivjJPanelInquiryKey = new JPanel();				ivjJPanelInquiryKey.setName("JPanel1");				ivjJPanelInquiryKey.setLayout(null);				ivjJPanelInquiryKey.add(getradioBusinessName(), null);				ivjJPanelInquiryKey.add(gettxtBusinessName(), null);				ivjJPanelInquiryKey.add(getradioVIN(), null);				ivjJPanelInquiryKey.add(getradioLastName(), null);				ivjJPanelInquiryKey.add(gettxtPermitNo(), null);				ivjJPanelInquiryKey.add(getradioPermitNo(), null);				ivjJPanelInquiryKey.add(gettxtVIN(), null);				ivjJPanelInquiryKey.add(gettxtLastName(), null);				ivjJPanelInquiryKey.setSize(525, 193);				Border laBorder =					new TitledBorder(						new EtchedBorder(),						KEY_PANEL_TITLE);				ivjJPanelInquiryKey.setBorder(laBorder);				// user code end				ivjJPanelInquiryKey.setEnabled(true);				ivjJPanelInquiryKey.setLocation(9, 20);			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjJPanelInquiryKey;	}	/**	 * Return the ivjradioBusinessName property value.	 * 	 * @return JRadioButton	 *//* &FrmTimedPermitInquiryMRG002.getradioBusinessName& */	private JRadioButton getradioBusinessName()	{		if (ivjradioBusinessName == null)		{			try			{				ivjradioBusinessName = new JRadioButton();				ivjradioBusinessName.setBounds(46, 141, 146, 24);				ivjradioBusinessName.setName("ivjradioBusinessName");				ivjradioBusinessName.setText("Business Name");				// user code begin {1}				ivjradioBusinessName.addActionListener(this);				ivjradioBusinessName.setMnemonic(KeyEvent.VK_B);				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjradioBusinessName;	}	/**	 * Return the ivjradioLastName property value.	 * 	 * @return JRadioButton	 *//* &FrmTimedPermitInquiryMRG002.getradioLastName& */	private JRadioButton getradioLastName()	{		if (ivjradioLastName == null)		{			try			{				ivjradioLastName = new JRadioButton();				ivjradioLastName.setBounds(45, 106, 157, 24);				ivjradioLastName.setName("ivjradioLastName");				ivjradioLastName.setText("Applicant Last Name");				// user code begin {1}				ivjradioLastName.addActionListener(this);				ivjradioLastName.setMnemonic(KeyEvent.VK_A);				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjradioLastName;	}	/**	 * Return the ivjradioPermitNo property value.	 * 	 * @return JRadioButton	 *//* &FrmTimedPermitInquiryMRG002.getradioPermitNo& */	private JRadioButton getradioPermitNo()	{		if (ivjradioPermitNo == null)		{			try			{				ivjradioPermitNo = new JRadioButton();				ivjradioPermitNo.setBounds(45, 36, 117, 24);				ivjradioPermitNo.setName("ivjradioPermitNo");				ivjradioPermitNo.setText("Permit No");				// user code begin {1}								ivjradioPermitNo.addActionListener(this);				ivjradioPermitNo.setMnemonic(KeyEvent.VK_P);				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjradioPermitNo;	}	/**	 * Return the ivjradioVIN property value.	 * 	 * @return JRadioButton	 *//* &FrmTimedPermitInquiryMRG002.getradioVIN& */	private JRadioButton getradioVIN()	{		if (ivjradioVIN == null)		{			try			{				ivjradioVIN = new JRadioButton();				ivjradioVIN.setBounds(45, 71, 118, 24);				ivjradioVIN.setName("ivjradioVIN");				ivjradioVIN.setText("VIN");				// user code begin {1}				ivjradioVIN.addActionListener(this);				ivjradioVIN.setMnemonic(KeyEvent.VK_V);				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjradioVIN;	}	/**	 * This method initializes ivjstcLblBeginDate	 * 	 * @return JLabel	 *//* &FrmTimedPermitInquiryMRG002.getstcLblBeginDate& */	private JLabel getstcLblBeginDate()	{		if (ivjstcLblBeginDate == null)		{			ivjstcLblBeginDate = new JLabel();			ivjstcLblBeginDate.setText("Begin Date:");			ivjstcLblBeginDate.setLabelFor(gettxtBeginDate());			ivjstcLblBeginDate.setSize(64, 20);			ivjstcLblBeginDate.setText(				InventoryConstant.TXT_BEGIN_DATE_COLON);			ivjstcLblBeginDate.setDisplayedMnemonic(				java.awt.event.KeyEvent.VK_G);			ivjstcLblBeginDate.setHorizontalAlignment(				SwingConstants.RIGHT);			ivjstcLblBeginDate.setLocation(31, 3);		}		return ivjstcLblBeginDate;	}	/**	 * This method initializes ivjstcLblEndDate	 * 	 * @return JLabel	 *//* &FrmTimedPermitInquiryMRG002.getstcLblEndDate& */	private JLabel getstcLblEndDate()	{		if (ivjstcLblEndDate == null)		{			ivjstcLblEndDate = new JLabel();			ivjstcLblEndDate.setText("End Date:");			ivjstcLblEndDate.setLabelFor(gettxtEndDate());			ivjstcLblEndDate.setSize(53, 20);			ivjstcLblEndDate.setText(				InventoryConstant.TXT_END_DATE_COLON);			ivjstcLblEndDate.setDisplayedMnemonic(				java.awt.event.KeyEvent.VK_N);			ivjstcLblEndDate.setHorizontalAlignment(				SwingConstants.RIGHT);			ivjstcLblEndDate.setLocation(42, 34);		}		return ivjstcLblEndDate;	}	/**	 * This method initializes ivjtxtBeginDate	 * 	 * @return RTSDateField	 *//* &FrmTimedPermitInquiryMRG002.gettxtBeginDate& */	private RTSDateField gettxtBeginDate()	{		if (ivjtxtBeginDate == null)		{			ivjtxtBeginDate = new RTSDateField();			ivjtxtBeginDate.setBounds(107, 3, 69, 20);		}		return ivjtxtBeginDate;	}	/**	 * Return the RTSInputField property value.	 * 	 * @return RTSInputField	 *//* &FrmTimedPermitInquiryMRG002.gettxtBusinessName& */	private RTSInputField gettxtBusinessName()	{		if (ivjtxtBusinessName == null)		{			try			{				ivjtxtBusinessName = new RTSInputField();				ivjtxtBusinessName.setSize(301, 20);				ivjtxtBusinessName.setInput(RTSInputField.DEFAULT);				ivjtxtBusinessName.setMaxLength(					CommonConstant.LENGTH_BUSINESS_NAME);				ivjtxtBusinessName.setEnabled(false);				ivjtxtBusinessName.setLocation(210, 145);			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjtxtBusinessName;	}	/**	 * This method initializes ivjtxtEndDate	 * 	 * @return RTSDateField	 *//* &FrmTimedPermitInquiryMRG002.gettxtEndDate& */	private RTSDateField gettxtEndDate()	{		if (ivjtxtEndDate == null)		{			ivjtxtEndDate = new RTSDateField();			ivjtxtEndDate.setBounds(107, 34, 69, 20);		}		return ivjtxtEndDate;	}	/**	 * This method initializes ivjtxtLastName	 * 	 * @return RTSInputField	 *//* &FrmTimedPermitInquiryMRG002.gettxtLastName& */	private RTSInputField gettxtLastName()	{		if (ivjtxtLastName == null)		{			try			{				ivjtxtLastName = new RTSInputField();				ivjtxtLastName.setSize(205, 20);				ivjtxtLastName.setMaxLength(30);				ivjtxtLastName.setInput(RTSInputField.DEFAULT);				ivjtxtLastName.setMaxLength(					CommonConstant.LENGTH_LAST_NAME);				ivjtxtLastName.setEnabled(false);				ivjtxtLastName.setLocation(209, 110);			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjtxtLastName;	}	/**	 * Return the ivjtxtPermitNo property value.	 * 	 * @return RTSInputField	 *//* &FrmTimedPermitInquiryMRG002.gettxtPermitNo& */	private RTSInputField gettxtPermitNo()	{		if (ivjtxtPermitNo == null)		{			try			{				ivjtxtPermitNo = new RTSInputField();				ivjtxtPermitNo.setSize(165, 20);				ivjtxtPermitNo.setLocation(209, 40);				// user code begin {1}				ivjtxtPermitNo.setEnabled(true);				ivjtxtPermitNo.setInput(					RTSInputField.ALPHANUMERIC_NOSPACE);				ivjtxtPermitNo.setMaxLength(					CommonConstant.LENGTH_PERMIT_NO);				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjtxtPermitNo;	}	/**	 * Return the ivjtxtVIN property value.	 * 	 * @return RTSInputField	 *//* &FrmTimedPermitInquiryMRG002.gettxtVIN& */	private RTSInputField gettxtVIN()	{		if (ivjtxtVIN == null)		{			try			{				ivjtxtVIN = new RTSInputField();				ivjtxtVIN.setSize(165, 20);				ivjtxtVIN.setLocation(209, 75);				// user code begin {1}				ivjtxtVIN.setInput(RTSInputField.ALPHANUMERIC_NOSPACE);				ivjtxtVIN.setEnabled(false);				ivjtxtVIN.setMaxLength(CommonConstant.LENGTH_VIN_MAX);				// user code end			}			catch (Throwable aeIVJEx)			{				// user code begin {2}				// user code end				handleException(aeIVJEx);			}		}		return ivjtxtVIN;	}	/**	* Called whenever the part throws an exception.	* 	* @param aeException 	*//* &FrmTimedPermitInquiryMRG002.handleException& */	private void handleException(Throwable aeException)	{		RTSException leRTSEx =			new RTSException(				RTSException.JAVA_ERROR,				(Exception) aeException);		leRTSEx.displayError(this);	}	/**	 * For radio buttons & entry fields, clear text, enable/disable and 	 * set focus.	 *//* &FrmTimedPermitInquiryMRG002.handleRadioButtonSelection& */	private void handleRadioButtonSelection()	{		// Reset all RTSInput fields		gettxtPermitNo().setText(CommonConstant.STR_SPACE_EMPTY);		gettxtVIN().setText(CommonConstant.STR_SPACE_EMPTY);		gettxtLastName().setText(CommonConstant.STR_SPACE_EMPTY);		gettxtBusinessName().setText(CommonConstant.STR_SPACE_EMPTY);		// Set Focus / Set Enabled  		if (getradioPermitNo().isSelected())		{			gettxtPermitNo().requestFocus();			caLastFocus = gettxtPermitNo();		}		else if (getradioLastName().isSelected())		{			gettxtLastName().requestFocus();			caLastFocus = gettxtLastName();		}		else if (getradioVIN().isSelected())		{			gettxtVIN().requestFocus();			caLastFocus = gettxtVIN();		}		else		{			gettxtBusinessName().requestFocus();			caLastFocus = gettxtBusinessName();		}		// Enable Fields as appropriate 		gettxtPermitNo().setEnabled(getradioPermitNo().isSelected());		gettxtLastName().setEnabled(getradioLastName().isSelected());		gettxtVIN().setEnabled(getradioVIN().isSelected());		gettxtBusinessName().setEnabled(			getradioBusinessName().isSelected());	}	/**	 * Initialize the class.	 */	/* WARNING: THIS METHOD WILL BE REGENERATED. *//* &FrmTimedPermitInquiryMRG002.initialize& */	private void initialize()	{		try		{			// user code begin {1}			// user code end			setName("FrmTimedPermitInquiryMRG002");			setDefaultCloseOperation(				WindowConstants.DO_NOTHING_ON_CLOSE);			setSize(549, 395);			setModal(true);			setTitle(FRM_TITLE);			setContentPane(getJDialogBoxContentPane());			setRequestFocus(false);			cvMFValid.add(gettxtBusinessName());			cvMFValid.add(gettxtPermitNo());			cvMFValid.add(gettxtVIN());			cvMFValid.add(gettxtLastName());			// end defect 10299 		}		catch (Throwable aeIVJEx)		{			handleException(aeIVJEx);		}		// user code begin {2}		// user code end	}	/**	 * Invoked when a key has been pressed.	 * 	 * @param aaKE KeyEvent	 *//* &FrmTimedPermitInquiryMRG002.keyPressed& */	public void keyPressed(KeyEvent aaKE)	{		if (aaKE.getSource() instanceof RTSDateField)		{			char lchKey = aaKE.getKeyChar();			if (lchKey == KeyEvent.VK_ENTER)			{				clearAllColor(this); 				processEnter();			}			boolean lbEnable =				!(gettxtEndDate().isDateEmpty()					&& gettxtBeginDate().isDateEmpty());			getbtnResetDates().setEnabled(lbEnable);		}	}	/** 	 * process Enter	 *	 *//* &FrmTimedPermitInquiryMRG002.processEnter& */	private void processEnter()	{		if (validateData())		{			getController().processData(				AbstractViewController.ENTER,				buildGSD());			caLastFocus.requestFocus();		}	}	/** 	 * 	 * This method resets the Begin/End DateFields	 *	 *//* &FrmTimedPermitInquiryMRG002.resetDateFields& */	private void resetDateFields()	{		gettxtBeginDate().setDate(null);		gettxtEndDate().setDate(null);		getbtnResetDates().setEnabled(false);		gettxtBeginDate().requestFocus();		clearAllColor(gettxtBeginDate());		clearAllColor(gettxtEndDate());	}	/**	 * all subclasses must implement this method - it sets the data on 	 * the screen and is how the controller relays information	 * to the view	 * 	 * @param aaObject	Object 	 *//* &FrmTimedPermitInquiryMRG002.setData& */	public void setData(Object aaObject)	{		getradioPermitNo().setSelected(true);		setDefaultFocusField(gettxtPermitNo());		csTransCd = getController().getTransCode();		cbPRMDUP = csTransCd.equals(TransCdConstant.PRMDUP);		boolean lbHQ = SystemProperty.isHQ();		getchkChargeFee().setEnabled(cbPRMDUP && !lbHQ);		getchkChargeFee().setVisible(cbPRMDUP && !lbHQ);		getchkChargeFee().setSelected(cbPRMDUP && !lbHQ);		getbtnResetDates().setEnabled(false);	}	/**	 * Validate Data  	 *	 * @return boolean	 *//* &FrmTimedPermitInquiryMRG002.validateData& */	private boolean validateData()	{		boolean lbReturn = true;		RTSException leRTSEx = new RTSException();		UtilityMethods.trimRTSInputField(this);		CommonValidations.addRTSExceptionForInvalidMFCharacters(			cvMFValid,			leRTSEx);		if (getradioPermitNo().isSelected())		{			if (gettxtPermitNo().isEmpty())			{				leRTSEx.addException(					new RTSException(						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),					gettxtPermitNo());			}		}		else if (getradioLastName().isSelected())		{			if (gettxtLastName().isEmpty())			{				leRTSEx.addException(					new RTSException(						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),					gettxtLastName());			}		}		else if (getradioBusinessName().isSelected())		{			if (gettxtBusinessName().isEmpty())			{				leRTSEx.addException(					new RTSException(						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),					gettxtBusinessName());			}		}		else if (getradioVIN().isSelected())		{			if (gettxtVIN().isEmpty())			{				leRTSEx.addException(					new RTSException(						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),					gettxtVIN());			}			else			{				String lsVIN =					CommonValidations.convert_i_and_o_to_1_and_0(						gettxtVIN().getText());				gettxtVIN().setText(lsVIN);			}		}		boolean lbBeginEmpty = gettxtBeginDate().isDateEmpty();		boolean lbEndEmpty = gettxtEndDate().isDateEmpty();		if (!lbBeginEmpty || !lbEndEmpty)		{			if (lbBeginEmpty || lbEndEmpty)			{				Component laCompError =					lbBeginEmpty ? ivjtxtBeginDate : ivjtxtEndDate;				leRTSEx.addException(					new RTSException(						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),					laCompError);			}			if (!lbBeginEmpty)			{				if (!gettxtBeginDate().isValidDate())				{					leRTSEx.addException(						new RTSException(							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),						gettxtBeginDate());				}				else if (					gettxtBeginDate().getDate().compareTo(						SystemProperty.getMFPrmtStartDate())						== -1)				{					leRTSEx.addException(						new RTSException(							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),						gettxtBeginDate());				}			}			if (!lbEndEmpty)			{				if (!gettxtEndDate().isValidDate())				{					leRTSEx.addException(						new RTSException(							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),						gettxtEndDate());				}			}			if (!leRTSEx.isValidationError())			{				RTSDate laBeginDate = gettxtBeginDate().getDate();				RTSDate laEndDate = gettxtEndDate().getDate();				if (laEndDate.compareTo(laBeginDate) < 0)				{					leRTSEx.addException(						new RTSException(							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),						gettxtEndDate());				}			}		}		if (leRTSEx.isValidationError())		{			leRTSEx.displayError(this);			leRTSEx.getFirstComponent().requestFocus();			lbReturn = false;		}		else if (			getradioVIN().isSelected()				&& gettxtVIN().getText().length()					!= CommonConstant.LENGTH_VIN)		{			// 555			new RTSException(				ErrorsConstant.ERR_NUM_VIN_NOT_17_DIGITS).displayError(				this);		}		return lbReturn;	}}/* #FrmTimedPermitInquiryMRG002# */ //  @jve:visual-info  decl-index=0 visual-constraint="10,10"