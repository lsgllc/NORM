package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.AddressData;
import com.txdot.isd.rts.services.data.InternetRegRecData;
import com.txdot.isd.rts.services.data.NameAddressComponent;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.CommonValidations;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.webapps.util.constants.CommonConstants;
import com.txdot.isd.rts.services.webapps.util.constants.RegRenProcessingConstants;

/*
 * FrmAddressInfoREG105.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Clifford		06/05/2002	This is reverted to 5/23's version and based
 * 							on that to fix the Alt + whatever problem 
 * 							(#4044). The original fix did not fully 
 * 							address the issue, bringing new (e.g.4218),
 * 							so reverted.
 * Bob Brown	07/05/2002	Fixed processData method did the convert to 
 * 							upper case method for all text fields in the
 * 							InternetRegRecData object, which ends up 
 * 							getting used to create the receipt
 * 							defect 4274
 * Clifford		07/09/2002	Trim trailing spaces
 * 							defect 4405
 * Clifford		09/03/2002	DB down, address change update, indicator 
 * 							update, update code moved to 
 * 							FrmProcessVehicleREG103.
 * 							defect 3700
 * Clifford		09/10/2002	Auto cap fix.
 * 							defect 4274
 * B Brown		10/23/2002	(putting and accessing Phase 2 error msgs in
 * 							rts_err_msgs).
 * 							Changed methods:  processData, 
 * 								handleCancelRequest
 * 							defect 4205
 * Jeff S.		02/22/2005	Get code to standard. Changed a non-static
 * 							call to a static call. Fixed setVisible for 
 * 							java 1.4
 * 							modify keyTyped(), handleException(), 
 * 								handleCancelRequest(), main()
 * 							removed setVisible() - meaningless
 *							defect 7889 Ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		06/17/2005	Renamed frame to have the frame number in 
 * 							the class name.  Used the normal convention
 * 							to handle max input on input boxes - made
 * 							all the fields RTSInputFields.  Added a new
 * 							RTSPhoneField that acts lite RTSDATEField
 * 							but it uses - instead of /.  Now using the
 * 							ECH button panel.
 * 							modify actionPerformed(), disableEdit(), 
 * 							getTxtPhoneNo(), 
 * 							add getButtonPanel1(), isRecordChanged()
 * 							deprecate hasValidFocus(), initConnections()
 * 							defect 7889 Ver 5.2.3 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	10/07/2005	Remove setting of color
 * 							Modified comments to reference 7889 vs. 7888
 * 							for Java 1.4 work. 7888 is IADDR.   
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		12/09/2005	Removed setting of the focus manager.  This
 * 							code was making the HotKeys and Acceler. on
 * 							the desktop stop working.
 * 							modify initialize() 
 *							defect 7889 Ver 5.2.3
 * K Harrell	02/11/2007	delete getBuilderData()
 * 							defect 9085 Ver Special Plates
 * Min Wang		06/18/2007	modify constants and screen	
 * 							defect 8768 Ver Special Plates 
 * B Brown		09/08/2008	Make state a larger display field to prevent
 * 							elipses(...) from displaying for certain
 * 							states like MD (Maryland).  
 * 							modify getcmbState()
 * 							defect 9478 Ver Defect_POS_B
 * K Harrell	02/09/2009	Use cache vs. call to server for OfcName. 
 * 							Also, general class cleanup.  
 * 							modify setData()
 * 							defect 9941 Ver Defect_POS_D 
 * K Harrell	03/22/2010	Add Recipient EMail Address, Standardize
 * 							Screen Format, Error Reporting. 
 * 							Implement CommonConstants for Length vs. 
 * 							 RegRenProcessingConstants, RTSInput constants
 * 							 for field type. 
 * 							Return to previous screen on Enter, 
 * 							 vs. update data, disable Enter.
 * 							Implement NameAddressComponent to facilitate
 * 							 set data to screen, data validation, including
 * 							 for MF invalid characters, assign data to  
 * 							 object.  
 * 							Single return for isRecordChanged()  
 * 							delete ItemListener 
 * 							delete keyReleased(),itemStateChanged(), 
 * 							 processData(), validateAddress(), runUpdate(), 
 * 							 handleCancel() 
 * 							delete cbIsDirty, cbIsEnabled, caData
 * 							delete ivjcomboState, get method
 * 							delete ENTERCORRECTFORMAT, ENTERREQUIREDINFO, 
 * 							 VALIDATION,COMMA,RECIPIENTNAME,ADDRESS1,CITY,
 *  						 ZIP_CODE,STATE,PHONE_NO,ZIP_EXT, ADDRESS, 
 * 							 CITY_LBL, PHONE_LBL
 * 							renamed JLabels, RTSInputFields, get methods
 * 							 to standard. 
 * 							add ivjstcLblRecpntEMail, ivjtxtRecpntEmail, 
 * 							 get methods.
 * 							add ivjtxtState, get method. 
 * 							add setDataToDataObject(), validateData(), 
 * 							 verifyCancel() 
 * 							add caItrntRegRecData, caRecpntAddrData, 
 * 							 caRenwlAddrComp, cvMFValid 
 * 							modify actionPerformed(), disableEdit(), 
 * 							 getJPanel1(), setData(), initialize(), 
 * 							 getstcLblCity(), getstcLblPhoneNo(), 
 * 							 all prior gettxtXXX methods.
 * 							defect 10372, 10397 Ver POS_640 
 * K Harrell	11/13/2011	disabled email address when required
 * 							modify disableEdit()
 * 							defect 11127 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */
/**
 * This frame displays the address info.
 *
 * @version	6.9.0 		11/13/2011
 * @author	SDUTULL
 * @since				03/19/2002 09:34:16
 */
public class FrmAddressInfoREG105
	extends RTSDialogBox
	implements ActionListener
// defect 10372 
//, ItemListener
// end defect 10372  
{
	private JPanel ivjJDialogContentPane = null;
	private JLabel ivjstcLblAddress1 = null;
	private JLabel ivjstcLblAddress2 = null;
	private JLabel ivjstcLblCity = null;
	private JLabel ivjstcLblCounty = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblName = null;
	private JLabel ivjstcLblPhoneNo = null;
	private JLabel ivjstcLblSate = null;
	private JLabel ivjstcLblZip = null;
	private JPanel ivjJPanel1 = null;
	private JLabel ivstcLblCounty = null;
	private RTSInputField ivjtxtAddress1 = null;
	private RTSInputField ivjtxtAddress2 = null;
	private RTSInputField ivjtxtCity = null;
	private RTSPhoneField ivjtxtPhoneNo = null;
	private RTSInputField ivjtxtRecipientName = null;
	private RTSInputField ivjtxtZpcd = null;
	private RTSInputField ivjtxtZpcdP4 = null;
	private ButtonPanel ivjButtonPanel1 = null;

	// defect 10397 
	//private JComboBox ivjcomboState = null;
	private RTSInputField ivjtxtState = null;
	private NameAddressComponent caRenwlAddrComp = null;
	private Vector cvMFValid = new Vector();
	// end defect 10397

	// defect 10372 
	private JLabel ivjstcLblRecpntEMail = null;
	private RTSInputField ivjtxtRecpntEMail = null;
	private InternetRegRecData caItrntRegRecData = null;
	private AddressData caRecpntAddrData;

	//	private boolean cbIsDirty = false;
	//	private boolean cbIsEnabled = true;
	//	private static final String ENTERCORRECTFORMAT =
	//		"Enter Fields in Correct Format ";
	//	private static final String ENTERREQUIREDINFO =
	//		"Enter Required Information ";
	//	private static final String VALIDATION = "Validation";
	//	private static final String COMMA = ", ";
	//	private static final String RECIPIENT_NAME = "RECIPIENT NAME";
	//	private static final String ADDRESS1 = "ADDRESS1";
	//	private static final String CITY = "CITY";
	//	private static final String ZIP_CODE = "ZIP CODE";
	//	private static final String STATE = "STATE";
	//	private static final String PHONE_NO = "PHONE NO.";
	//	private static final String ZIP_EXT = "ZIP EXT.";
	//  private static final String ADDRESS = "Address:";
	//  private static final String CITY_LBL = "City:";
	//  private static final String PHONE_LBL = "Phone No:";
	// end defect 10372

	// Constants
	private static final String FRM_TITLE =
		"Recipient Address Information     REG105";
	private static final String MAIN_EXCEPTION_MSG =
		"Exception occurred in main() of javax.swing.JDialog";
	private static final String ADDRESS1_LBL = "Address 1:";
	private static final String ADDRESS2_LBL = "Address 2:";
	private static final String COUNTY_LBL = "County:";
	private static final String DASH_LBL = "-";
	private static final String NAME_LBL = "Recipient Name:";
	private static final String STATE_LBL = "State:";
	private static final String ZIP_LBL = "Zip:";
	private static final String EMPTY_STRING = "";
	private static final String RTS000979 = "RTS000979";
	private static final String DIALOG_FONT = "dialog";

	/**
	 * AddressInfo constructor comment.
	 */
	public FrmAddressInfoREG105()
	{
		super();
		initialize();
	}

	/**
	 * AddressInfo constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmAddressInfoREG105(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * AddressInfo constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmAddressInfoREG105(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmAddressInfoREG105 laFrmAddressInfo;
			laFrmAddressInfo = new FrmAddressInfoREG105();
			laFrmAddressInfo.setModal(true);
			laFrmAddressInfo
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				}
			});
			laFrmAddressInfo.show();
			java.awt.Insets insets = laFrmAddressInfo.getInsets();
			laFrmAddressInfo.setSize(
				laFrmAddressInfo.getWidth()
					+ insets.left
					+ insets.right,
				laFrmAddressInfo.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmAddressInfo.setVisibleRTS(true);
		}
		catch (Throwable aeThrowable)
		{
			System.err.println(MAIN_EXCEPTION_MSG);
			aeThrowable.printStackTrace(System.out);
		}
	}

	/**
	 * Handles action events that occur on this frame.
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
			// defect 10372 
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateData())
				{
					setDataToDataObject();

					getController().processData(
						AbstractViewController.ENTER,
						caItrntRegRecData);
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				if (verifyCancel())
				{
					getController().processData(
						AbstractViewController.CANCEL,
						null);
				}
			}
			// end defect 10372
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.REG105);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Disable all the editable fields.
	 */
	private void disableEdit()
	{
		gettxtRenewalRecipientName().setEnabled(false);
		gettxtAddress1().setEnabled(false);
		gettxtAddress2().setEnabled(false);
		gettxtCity().setEnabled(false);
		gettxtPhoneNo().setEnabled(false);
		gettxtZpcd().setEnabled(false);
		gettxtZpcdP4().setEnabled(false);
		getButtonPanel1().getBtnEnter().setEnabled(false);
		gettxtState().setEnabled(false);
		// defect 11127 
		gettxtRecpntEMail().setEnabled(false); 
		// end defect 11127 
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * This is the ECH.
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
				ivjButtonPanel1.setBounds(129, 301, 250, 36);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setRequestFocusEnabled(false);
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

	// defect 10397 
	//	/**
	//	 * Return the JComboBox1 property value.
	//	 * 
	//	 * @return JComboBox
	//	 */
	//	private JComboBox getcomboState()
	//	{
	//		if (ivjcomboState == null)
	//		{
	//			try
	//			{
	//				ivjcomboState = new JComboBox();
	//				ivjcomboState.setName("cmbState");
	//				ivjcomboState.setFont(
	//					new java.awt.Font(DIALOG_FONT, 0, 12));
	//				// defect 9478	
	//				//ivjcmbState.setBounds(122, 117, 45, 20);	
	//				ivjcomboState.setBounds(122, 117, 52, 20);
	//				// end defect 9478
	//				ivjcomboState.setFont(
	//					new java.awt.Font(DIALOG_FONT, Font.PLAIN, 12));
	//				String[] larrStateNames =
	//					{
	//						"",
	//						"AL",
	//						"AK",
	//						"AZ",
	//						"AR",
	//						"CA",
	//						"CO",
	//						"CT",
	//						"DE",
	//						"DC",
	//						"FL",
	//						"GA",
	//						"HI",
	//						"ID",
	//						"IL",
	//						"IN",
	//						"IA",
	//						"KS",
	//						"KY",
	//						"LA",
	//						"ME",
	//						"MD",
	//						"MA",
	//						"MI",
	//						"MN",
	//						"MS",
	//						"MO",
	//						"MT",
	//						"NE",
	//						"NV",
	//						"NH",
	//						"NJ",
	//						"NM",
	//						"NY",
	//						"NC",
	//						"ND",
	//						"OH",
	//						"OK",
	//						"OR",
	//						"PA",
	//						"RI",
	//						"SC",
	//						"SD",
	//						"TN",
	//						"TX",
	//						"UT",
	//						"VT",
	//						"VA",
	//						"WA",
	//						"WV",
	//						"WI",
	//						"WY" };
	//
	//				for (int i = 0; i < larrStateNames.length; i++)
	//				{
	//					ivjcomboState.addItem(larrStateNames[i]);
	//				}
	//				//ivjcmbState.addItemListener(this);
	//				// user code end
	//			}
	//			catch (Throwable aeIVJExc)
	//			{
	//				// user code begin {2}
	//				// user code end
	//				handleException(aeIVJExc);
	//			}
	//		}
	//		return ivjcomboState;
	//	}
	// end defect 10397 

	/**
	 * Return the JDialogContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJDialogContentPane()
	{
		if (ivjJDialogContentPane == null)
		{
			try
			{
				ivjJDialogContentPane = new JPanel();
				ivjJDialogContentPane.setName("JDialogContentPane");
				ivjJDialogContentPane.setLayout(null);
				getJDialogContentPane().add(
					getJPanel1(),
					getJPanel1().getName());
				getJDialogContentPane().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJDialogContentPane;
	}

	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAddress1()
	{
		if (ivjstcLblAddress1 == null)
		{
			try
			{
				ivjstcLblAddress1 = new JLabel();
				ivjstcLblAddress1.setName("ivjstcLblAddress1");
				ivjstcLblAddress1.setText(ADDRESS1_LBL);
				ivjstcLblAddress1.setBounds(22, 52, 94, 14);
				// user code begin {1}
				ivjstcLblAddress1.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstcLblAddress1.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblAddress1;
	}

	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblAddress2()
	{
		if (ivjstcLblAddress2 == null)
		{
			try
			{
				ivjstcLblAddress2 = new JLabel();
				ivjstcLblAddress2.setName("ivjstcLblAddress2");
				ivjstcLblAddress2.setText(ADDRESS2_LBL);
				ivjstcLblAddress2.setBounds(22, 77, 94, 14);
				// user code begin {1}
				ivjstcLblAddress2.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstcLblAddress2.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblAddress2;
	}

	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCity()
	{
		if (ivjstcLblCity == null)
		{
			try
			{
				ivjstcLblCity = new JLabel();
				ivjstcLblCity.setName("ivjstcLblCity");

				// defect 10372 
				//ivjstcLblCity.setText(CITY_LBL);
				ivjstcLblCity.setText(CommonConstant.TXT_CITY_COLON);
				// end defect 10372 

				ivjstcLblCity.setBounds(22, 102, 94, 14);
				// user code begin {1}
				ivjstcLblCity.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstcLblCity.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblCity;
	}

	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCounty()
	{
		if (ivjstcLblCounty == null)
		{
			try
			{
				ivjstcLblCounty = new JLabel();
				ivjstcLblCounty.setSize(94, 14);
				ivjstcLblCounty.setName("ivjstcLblCounty");
				ivjstcLblCounty.setText(COUNTY_LBL);
				// user code begin {1}
				ivjstcLblCounty.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstcLblCounty.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
				ivjstcLblCounty.setLocation(22, 227);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblCounty;
	}

	/**
	 * Return the JLabel property value.
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
				ivjstcLblDash.setText(DASH_LBL);
				ivjstcLblDash.setBounds(167, 150, 10, 14);
				// user code begin {1}
				ivjstcLblDash.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
				ivjstcLblDash.setHorizontalTextPosition(
					javax.swing.SwingConstants.CENTER);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblDash;
	}

	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblName()
	{
		if (ivjstcLblName == null)
		{
			try
			{
				ivjstcLblName = new JLabel();
				ivjstcLblName.setName("ivjstcLblName");
				ivjstcLblName.setText(NAME_LBL);
				ivjstcLblName.setBounds(22, 27, 94, 14);
				// user code begin {1}
				ivjstcLblName.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstcLblName.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblName;
	}

	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPhoneNo()
	{
		if (ivjstcLblPhoneNo == null)
		{
			try
			{
				ivjstcLblPhoneNo = new JLabel();
				ivjstcLblPhoneNo.setSize(94, 14);
				ivjstcLblPhoneNo.setName("ivjstcLblPhoneNo");

				// defect 10372 
				//ivjstcLblPhoneNo.setText(PHONE_LBL);
				ivjstcLblPhoneNo.setText(
					CommonConstant.TXT_PHONE_NO_COLON);
				// end defect 10372 

				// user code begin {1}
				ivjstcLblPhoneNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstcLblPhoneNo.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
				ivjstcLblPhoneNo.setLocation(22, 202);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblPhoneNo;
	}

	/**
	 * This method initializes ivjstcLblRecpntEMail
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblRecpntEMail()
	{
		if (ivjstcLblRecpntEMail == null)
		{
			ivjstcLblRecpntEMail = new javax.swing.JLabel();
			ivjstcLblRecpntEMail.setSize(91, 16);
			ivjstcLblRecpntEMail.setText(CommonConstant.TXT_EMAIL);
			ivjstcLblRecpntEMail.setHorizontalAlignment(
				javax.swing.SwingConstants.TRAILING);
			ivjstcLblRecpntEMail.setLocation(25, 177);
		}
		return ivjstcLblRecpntEMail;
	}

	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblState()
	{
		if (ivjstcLblSate == null)
		{
			try
			{
				ivjstcLblSate = new JLabel();
				ivjstcLblSate.setSize(94, 14);
				ivjstcLblSate.setName("ivjstcLblSate");
				ivjstcLblSate.setText(STATE_LBL);
				// user code begin {1}
				ivjstcLblSate.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstcLblSate.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
				ivjstcLblSate.setLocation(22, 127);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblSate;
	}

	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblZip()
	{
		if (ivjstcLblZip == null)
		{
			try
			{
				ivjstcLblZip = new JLabel();
				ivjstcLblZip.setSize(94, 14);
				ivjstcLblZip.setName("ivjstcLblZip");
				ivjstcLblZip.setText(ZIP_LBL);
				// user code begin {1}
				ivjstcLblZip.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstcLblZip.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
				ivjstcLblZip.setLocation(22, 152);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblZip;
	}

	/**
	 * Return the JPanel property value.
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
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(14, 29, 485, 263);
				getJPanel1().add(
					getstcLblAddress1(),
					getstcLblAddress1().getName());
				getJPanel1().add(
					getstcLblCity(),
					getstcLblCity().getName());
				getJPanel1().add(
					getstcLblState(),
					getstcLblState().getName());
				getJPanel1().add(
					getstcLblZip(),
					getstcLblZip().getName());
				getJPanel1().add(
					gettxtAddress1(),
					gettxtAddress1().getName());
				getJPanel1().add(gettxtCity(), gettxtCity().getName());

				// defect 10397 
				//	getJPanel1().add(
				//	getcomboState(),
				//	getcomboState().getName());
				getJPanel1().add(gettxtState(), null);
				// end defect 10397 

				getJPanel1().add(gettxtZpcd(), gettxtZpcd().getName());
				getJPanel1().add(
					getstcLblName(),
					getstcLblName().getName());
				getJPanel1().add(
					gettxtRenewalRecipientName(),
					gettxtRenewalRecipientName().getName());
				getJPanel1().add(
					getstcLblPhoneNo(),
					getstcLblPhoneNo().getName());
				getJPanel1().add(
					gettxtPhoneNo(),
					gettxtPhoneNo().getName());
				getJPanel1().add(
					getstcLblCounty(),
					getstcLblCounty().getName());
				getJPanel1().add(
					getlblCounty(),
					getlblCounty().getName());
				getJPanel1().add(
					gettxtAddress2(),
					gettxtAddress2().getName());
				getJPanel1().add(
					getstcLblAddress2(),
					getstcLblAddress2().getName());
				getJPanel1().add(
					getstcLblDash(),
					getstcLblDash().getName());
				getJPanel1().add(
					gettxtZpcdP4(),
					gettxtZpcdP4().getName());
				// user code begin {1}

				// defect 10372 
				getJPanel1().add(getstcLblRecpntEMail(), null);
				getJPanel1().add(gettxtRecpntEMail(), null);
				getJPanel1().setBorder(
				//new TitledBorder(new EtchedBorder(), ADDRESS));
				new TitledBorder(
					new EtchedBorder(),
					CommonConstant.TXT_ADDRESS_COLON));
				// end defect 10372

				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the JLabel property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCounty()
	{
		if (ivstcLblCounty == null)
		{
			try
			{
				ivstcLblCounty = new JLabel();
				ivstcLblCounty.setSize(119, 17);
				ivstcLblCounty.setName("ivstcLblCounty");
				ivstcLblCounty.setFont(
					new java.awt.Font(DIALOG_FONT, 0, 12));
				ivstcLblCounty.setText(EMPTY_STRING);
				ivstcLblCounty.setLocation(124, 226);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivstcLblCounty;
	}

	/**
	 * Return the RTSInputField property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtAddress1()
	{
		if (ivjtxtAddress1 == null)
		{
			try
			{
				ivjtxtAddress1 = new RTSInputField();
				ivjtxtAddress1.setSize(276, 17);
				ivjtxtAddress1.setName("ivjtxtAddress1");
				// defect 10372 
				ivjtxtAddress1.setInput(RTSInputField.DEFAULT);
				ivjtxtAddress1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10372 
				ivjtxtAddress1.setLocation(124, 50);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtAddress1;
	}

	/**
	 * Return the RTSInputField property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtAddress2()
	{
		if (ivjtxtAddress2 == null)
		{
			try
			{
				ivjtxtAddress2 = new RTSInputField();
				ivjtxtAddress2.setSize(276, 17);
				ivjtxtAddress2.setName("ivjtxtAddress2");
				// defect 10372 
				ivjtxtAddress2.setInput(RTSInputField.DEFAULT);
				ivjtxtAddress2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10372 
				ivjtxtAddress2.setLocation(124, 75);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtAddress2;
	}

	/**
	 * Return the RTSInputField property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCity()
	{
		if (ivjtxtCity == null)
		{
			try
			{
				ivjtxtCity = new RTSInputField();
				ivjtxtCity.setSize(167, 17);
				ivjtxtCity.setName("ivjtxtCity");
				// defect 10372 
				ivjtxtCity.setInput(RTSInputField.DEFAULT);
				ivjtxtCity.setMaxLength(CommonConstant.LENGTH_CITY);
				// end defect 10372 
				ivjtxtCity.setLocation(124, 100);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtCity;
	}

	/**
	 * Return the RTSPhoneField property value.
	 * 
	 * @return RTSPhoneField
	 */
	private RTSPhoneField gettxtPhoneNo()
	{
		if (ivjtxtPhoneNo == null)
		{
			try
			{
				ivjtxtPhoneNo = new RTSPhoneField();
				ivjtxtPhoneNo.setSize(87, 17);
				ivjtxtPhoneNo.setName("ivjtxtPhoneNo");
				ivjtxtPhoneNo.setLocation(124, 200);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtPhoneNo;
	}

	/**
	 * This method initializes ivjtxtRecpntEMail
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRecpntEMail()
	{
		if (ivjtxtRecpntEMail == null)
		{
			ivjtxtRecpntEMail = new RTSInputField();
			ivjtxtRecpntEMail.setSize(347, 17);
			ivjtxtRecpntEMail.setInput(RTSInputField.DEFAULT);
			ivjtxtRecpntEMail.setMaxLength(CommonConstant.LENGTH_EMAIL);
			ivjtxtRecpntEMail.setLocation(124, 175);
		}
		return ivjtxtRecpntEMail;
	}

	/**
	 * Return the RTSInputField property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtRenewalRecipientName()
	{
		if (ivjtxtRecipientName == null)
		{
			try
			{
				ivjtxtRecipientName = new RTSInputField();
				ivjtxtRecipientName.setSize(276, 17);
				ivjtxtRecipientName.setName("ivjtxtRecipientName");
				// defect 10372 
				ivjtxtRecipientName.setInput(RTSInputField.DEFAULT);
				ivjtxtRecipientName.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// end defect 10372 
				ivjtxtRecipientName.setLocation(124, 25);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtRecipientName;
	}

	/**
	 * This method initializes RTSInputField
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtState()
	{
		if (ivjtxtState == null)
		{
			ivjtxtState = new RTSInputField();
			ivjtxtState.setMaxLength(CommonConstant.LENGTH_STATE);
			ivjtxtState.setInput(RTSInputField.ALPHA_ONLY);
			ivjtxtState.setSize(25, 17);
			ivjtxtState.setText("");
			ivjtxtState.setLocation(124, 125);
		}
		return ivjtxtState;
	}

	/**
	 * Return the RTSInputField property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtZpcd()
	{
		if (ivjtxtZpcd == null)
		{
			try
			{
				ivjtxtZpcd = new RTSInputField();
				ivjtxtZpcd.setSize(40, 17);
				ivjtxtZpcd.setName("ivjtxtZpcd");
				// defect 10372 
				ivjtxtZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtZpcd.setMaxLength(CommonConstant.LENGTH_ZIPCODE);
				// end defect 10372 
				ivjtxtZpcd.setLocation(124, 150);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtZpcd;
	}

	/**
	 * Return the RTSInputField property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtZpcdP4()
	{
		if (ivjtxtZpcdP4 == null)
		{
			try
			{
				ivjtxtZpcdP4 = new RTSInputField();
				ivjtxtZpcdP4.setName("ivjtxtZpcdP4");
				ivjtxtZpcdP4.setBounds(181, 150, 34, 17);
				// defect 10372 
				ivjtxtZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// end defect 10372 

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtZpcdP4;
	}

	// defect 10372 
	//		/**
	//		 * Handle when the cancel button is pressed.  This is called from
	//		 * actionperformed.
	//		 */
	//		private void handleCancelRequest()
	//		{
	//			// If anything of the fields have changed during this session.
	//			if (cbIsDirty)
	//			{
	//				//confirm update and prompt to continue
	//
	//
	//				RTSException leRTSEx =
	//					new RTSException(
	//						RTSException.CTL001,
	//						RegRenProcessingConstants.ERR_MODIFIED_REC_979,
	//						RTS000979);
	//				int retCode = leRTSEx.displayError(this);
	//				if (retCode == RTSException.YES)
	//				{
	//					cbIsDirty = false;
	//					getController().processData(
	//						AbstractViewController.CANCEL,
	//						caData);
	//				}
	//			}
	//			else
	//			{
	//				getController().processData(
	//					AbstractViewController.CANCEL,
	//					caData);
	//			}
	//		}
	// end defect 10372 

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeIVJExc Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
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
			setModal(true);
			setName("AddressInfo");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(520, 375);
			setTitle(FRM_TITLE);
			setContentPane(getJDialogContentPane());
		}
		catch (Throwable aeThrowable)
		{
			handleException(aeThrowable);
		}
		// user code begin {2}
		// user code end

		// defect 10372 
		// Enter no longer enabled only if Record Changed. 
		// keyPressed() was not initially reflecting change if typed in
		//  special characters.  Also, non-standard interface.

		// disable the enter button by default.  Only enable it if any
		// on the frame changes during the life of the frame.
		// getButtonPanel1().getBtnEnter().setEnabled(false);

		// end defect 10372 

		// defect 10397
		cvMFValid = new Vector();
		cvMFValid.add(gettxtRenewalRecipientName());

		caRenwlAddrComp =
			new NameAddressComponent(
				gettxtAddress1(),
				gettxtAddress2(),
				gettxtCity(),
				gettxtState(),
				gettxtZpcd(),
				gettxtZpcdP4(),
				ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
		// end defect 10397 
	}

	/**
	 * This method compares all of the fields on the frame to thier 
	 * associatedto field in the data object that was passed to the
	 * frame.  If anything does not match then return true else return
	 * false.
	 * 
	 * @return boolean
	 */
	private boolean isRecordChanged()
	{
		// defect 10372 
		// Reorganize for single return 
		//  Implement caRecpntAddrData
		// Note: Test records show in upper case - did not remove.

		// Name
		return !gettxtRenewalRecipientName().getText().equals(
			caItrntRegRecData
				.getCompleteRegRenData()
				.getVehUserData()
				.getRecipientName()
				.toUpperCase())

		// Address 1
		|| !gettxtAddress1().getText().equals(
			caRecpntAddrData.getSt1().toUpperCase())

		// Address 2
		|| !gettxtAddress2().getText().equals(
			caRecpntAddrData.getSt2().toUpperCase())

		// City
		|| !gettxtCity().getText().equals(
			caRecpntAddrData.getCity().toUpperCase())

		// State
		|| !gettxtState().getText().equals(
			caRecpntAddrData.getState().toUpperCase())

		// Zpcd
		|| !gettxtZpcd().getText().equals(caRecpntAddrData.getZpcd())

		// ZpcdP4
		|| !gettxtZpcdP4().getText().equals(caRecpntAddrData.getZpcdp4())

		// E-Mail 
		|| !gettxtRecpntEMail().getText().equals(
			caItrntRegRecData
				.getCompleteRegRenData()
				.getVehUserData()
				.getEmail()
				.toUpperCase())

		// Phone Number
		|| !gettxtPhoneNo().getPhoneNo().equals(
			caItrntRegRecData
				.getCompleteRegRenData()
				.getVehUserData()
				.getPhoneNumber())

		// defect 10397
		// State
		//		||
		//				!getcomboState().getSelectedItem().toString().equals(
		//					caRecpntAddrData.getState());
		//		
		|| !gettxtState().getText().equals(
			caRecpntAddrData.getState().toUpperCase());
		// end defect 10397 
		// end defect 10372 
	}

	// defect 10372 
	//	/**
	//	 * Handles Item State Changes.  This is used by the state combo box
	//	 * to check if anything on the frame has changed.  If something has
	//	 * changed then the enter button will be enabled and the boolean
	//	 * is updated so the check for a changed record will not occur 
	//	 * again.
	//	 * 
	//	 * @param aaIE ItemEvent
	//	 */
	//	public void itemStateChanged(ItemEvent aaIE)
	//	{
	//		// defect 7889
	//		// Added call to isRecordChanged() that actually compares
	//		// the data to see if anything has changed.
	//		if (cbIsEnabled && !cbIsDirty && isRecordChanged())
	//		{
	//			cbIsDirty = true;
	//			getButtonPanel1().getBtnEnter().setEnabled(true);
	//		}
	//		// end defect 7889
	//	}
	//	/**
	//	 * Handles Key Released events.
	//	 * 
	//	 * @param aaKE KeyEvent
	//	 */
	//	public void keyReleased(KeyEvent aaKE)
	//	{
	//		// defect 7889
	//		// Added call to isRecordChanged() that actually compares
	//		// the data to see if anything has changed.  Also removed the
	//		// code that handled ESC because dialog will handle for you.
	//		//super.keyReleased(aaKE);
	//		if (cbIsEnabled && !cbIsDirty && isRecordChanged())
	//		{
	//			cbIsDirty = true;
	//			getButtonPanel1().getBtnEnter().setEnabled(true);
	//		}
	//	}

	//	/**
	//	 * This method is called to process all data.
	//	 */
	//	private void processData()
	//	{
	//		RTSException leRTSEx = new RTSException();
	//		// update address
	//		// set values to update
	//		// defect 4405 
	//		String lsRecipientName =
	//			getTxtRecipientName().getText().trim().toUpperCase();
	//		String lsRenwlMailingSt1 =
	//			getTxtAddress1().getText().trim().toUpperCase();
	//		String lsRenwlMailingSt2 =
	//			getTxtAddress2().getText().trim().toUpperCase();
	//		String lsRenwlMailingCity =
	//			getTxtCity().getText().trim().toUpperCase();
	//		getTxtRecipientName().setText(lsRecipientName);
	//		getTxtAddress1().setText(lsRenwlMailingSt1);
	//		getTxtAddress2().setText(lsRenwlMailingSt2);
	//		getTxtCity().setText(lsRenwlMailingCity);
	//		// end 4405  
	//		String lsRenwlMailingState =
	//			getcmbState().getSelectedItem().toString().toUpperCase();
	//		String lsRenwlMailingZpCd = getTxtZip().getText();
	//		String lsRenwlMailingZpCd4 = getTxtZipExt().getText();
	//		String lsPhone = getTxtPhoneNo().getPhoneNo();
	//		caData
	//			.getCompleteRegRenData()
	//			.getVehUserData()
	//			.setRecipientName(
	//			lsRecipientName);
	//		caData
	//			.getCompleteRegRenData()
	//			.getVehUserData()
	//			.getAddress()
	//			.setSt1(
	//			lsRenwlMailingSt1);
	//		caData
	//			.getCompleteRegRenData()
	//			.getVehUserData()
	//			.getAddress()
	//			.setSt2(
	//			lsRenwlMailingSt2);
	//		caData
	//			.getCompleteRegRenData()
	//			.getVehUserData()
	//			.getAddress()
	//			.setCity(
	//			lsRenwlMailingCity);
	//		//end defect #4274
	//		caData
	//			.getCompleteRegRenData()
	//			.getVehUserData()
	//			.getAddress()
	//			.setState(
	//			lsRenwlMailingState);
	//		caData
	//			.getCompleteRegRenData()
	//			.getVehUserData()
	//			.getAddress()
	//			.setZpcd(
	//			lsRenwlMailingZpCd);
	//		caData
	//			.getCompleteRegRenData()
	//			.getVehUserData()
	//			.getAddress()
	//			.setZpcdp4(
	//			lsRenwlMailingZpCd4);
	//		// The data object holds phone in 5123332222 format
	//		caData.getCompleteRegRenData().getVehUserData().setPhoneNumber(
	//			lsPhone);
	//		leRTSEx.addException(
	//			new RTSException(978),
	//			leRTSEx.getFirstComponent());
	//		leRTSEx.displayError(this);
	//		clearAllColor(this);
	//		caData.setAddressChanged(true);
	//	}

	//	/**
	//	 * This method starts the update of the Address info.
	//	 */
	//	private void runUpdate()
	//	{
	//		//perform form validation
	//		if (validateAddress())
	//		{
	//			processData();
	//			//reset
	//			//
	//			getButtonPanel1().getBtnEnter().setEnabled(false);
	//			getButtonPanel1().getBtnCancel().requestFocus();
	//			cbIsDirty = false;
	//		}
	//	}
	// defect 10372 

	/**
	 * This method is called to process all data.
	 */
	private void setDataToDataObject()
	{
		if (isRecordChanged())
		{
			//new RTSException(ErrorsConstant.ERR_NUM_RECORD_UPDATED)
			new RTSException(
				ErrorsConstant.ERR_MSG_ADDRESS_UPDATED_UPON_APPROVAL)
				.displayError();
		}

		// Renewal Recipient Name 		
		caItrntRegRecData
			.getCompleteRegRenData()
			.getVehUserData()
			.setRecipientName(gettxtRenewalRecipientName().getText());

		// defect 10397 
		// Renewal Mailing Address  
		caRenwlAddrComp.setAddressToDataObject(caRecpntAddrData);
		// end defect 10397 

		// Phone 
		caItrntRegRecData
			.getCompleteRegRenData()
			.getVehUserData()
			.setPhoneNumber(gettxtPhoneNo().getPhoneNo());

		// EMail 
		caItrntRegRecData
			.getCompleteRegRenData()
			.getVehUserData()
			.setEmail(
			gettxtRecpntEMail().getText());

		// This is really bogus; Left over from processData().
		// Opening defect 10399 - KPH 3/3/10
		caItrntRegRecData.setAddressChanged(true);
	}

	/**
	 * This an abstract method that is inhereted by RTSDialogBox.
	 * This is used to set all the data on the frame before it is 
	 * displayed.
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData != null && aaData instanceof InternetRegRecData)
		{
			caItrntRegRecData = (InternetRegRecData) aaData;

			//update display values
			gettxtRenewalRecipientName().setText(
				caItrntRegRecData
					.getCompleteRegRenData()
					.getVehUserData()
					.getRecipientName());

			// defect 10372
			// Implement caRecpntAddrData 
			caRecpntAddrData =
				caItrntRegRecData
					.getCompleteRegRenData()
					.getVehUserData()
					.getAddress();
			// end defect 10372 

			// defect 10397
			// Renewal Address 
			caRenwlAddrComp.setAddressDataToDisplay(caRecpntAddrData);
			//	getTxtRecipientName().setText(
			//		caItrntRegRecData
			//			.getCompleteRegRenData()
			//			.getVehUserData()
			//			.getRecipientName()
			//			.toUpperCase());
			//	getTxtAddress1().setText(
			//		caItrntRegRecData
			//			.getCompleteRegRenData()
			//			.getVehUserData()
			//			.getAddress()
			//			.getSt1()
			//			.toUpperCase());
			//	getTxtAddress2().setText(
			//		caItrntRegRecData
			//			.getCompleteRegRenData()
			//			.getVehUserData()
			//			.getAddress()
			//			.getSt2()
			//			.toUpperCase());
			//	getTxtCity().setText(
			//		caItrntRegRecData
			//			.getCompleteRegRenData()
			//			.getVehUserData()
			//			.getAddress()
			//			.getCity()
			//			.toUpperCase());
			//	getTxtZip().setText(
			//		caItrntRegRecData
			//			.getCompleteRegRenData()
			//			.getVehUserData()
			//			.getAddress()
			//			.getZpcd());
			//	// set value to state (combobox)
			//	getcmbState().removeItemListener(this);
			//	getcmbState().setSelectedItem(
			//	caItrntRegRecData
			//	.getCompleteRegRenData()
			//	.getVehUserData()
			//	.getAddress()
			//	.getState());
			//	getcmbState().addItemListener(this);
			// end defect 10397 

			// defect 10372 
			gettxtRecpntEMail().setText(
				caItrntRegRecData
					.getCompleteRegRenData()
					.getVehUserData()
					.getEmail());
			// end defect 10372 

			gettxtPhoneNo().setPhoneNo(
				caItrntRegRecData
					.getCompleteRegRenData()
					.getVehUserData()
					.getPhoneNumber());

			// disable edit if status=approved or declined
			if (caItrntRegRecData
				.getStatus()
				.equals(CommonConstants.APPROVED + EMPTY_STRING)
				|| caItrntRegRecData.getStatus().equals(
					CommonConstants.DECLINED_REFUND_PENDING
						+ EMPTY_STRING)
				|| caItrntRegRecData.getStatus().equals(
					CommonConstants.DECLINED_REFUND_APPROVED
						+ EMPTY_STRING)
				|| caItrntRegRecData.getStatus().equals(
					CommonConstants.DECLINED_REFUND_FAILED
						+ EMPTY_STRING))
			{
				disableEdit();
			}

			// defect 9941
			// Use OfficeIds Cache vs. call to Server 
			OfficeIdsData laOfficeIds =
				OfficeIdsCache.getOfcId(
					SystemProperty.getOfficeIssuanceNo());
			getlblCounty().setText(laOfficeIds.getOfcName());
			// end defect 9941
		}
	}

	// defect 10397 
	//	/**
	//	 * This methods validates the address data that was entered before
	//	 * we update the DB with the new address data.
	//	 * 
	//	 * @return boolean
	//	 */
	//	private boolean validateAddress()
	//	{
	//		//check for missing field values
	//		String lsMissingFields = EMPTY_STRING;
	//		String lsErrorFields = EMPTY_STRING;
	//		//standard validation
	//		clearAllColor(this);
	//		RTSException leRTSEx = new RTSException();
	//		if (getTxtRecipientName().getText().trim().length() == 0)
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtRecipientName());
	//	lsMissingFields += COMMA + RECIPIENT_NAME;
	//		}
	//		if (getTxtAddress1().getText().trim().length() == 0)
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtAddress1());
	//	lsMissingFields += COMMA + ADDRESS1;
	//		}
	//		if (getTxtCity().getText().trim().length() == 0)
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtCity());
	//	lsMissingFields += COMMA + CITY;
	//		}
	//		if (getTxtZip().getText().trim().length() == 0)
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtZip());
	//	lsMissingFields += COMMA + ZIP_CODE;
	//		}
	//		if (getcmbState().getSelectedItem().equals(EMPTY_STRING))
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getcmbState());
	//	lsMissingFields += COMMA + STATE;
	//		}
	//		if (getTxtPhoneNo().isPhoneNoEmpty())
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtPhoneNo());
	//	lsMissingFields += COMMA + PHONE_NO;
	//		}
	//		//error checking (missing fields)
	//		if (leRTSEx.isValidationError())
	//		{
	//	if (lsMissingFields.startsWith(COMMA))
	//	{
	//		lsMissingFields =
	//			lsMissingFields.substring(
	//		2,
	//		lsMissingFields.length());
	//	}
	//	leRTSEx.setMessage(ENTERREQUIREDINFO + lsMissingFields);
	//	leRTSEx.displayError(this);
	//	leRTSEx.getFirstComponent().requestFocus();
	//	return false;
	//		}
	//		//check for incomplete/incorrect field values
	//		if (getTxtRecipientName().getText().trim().length()
	//	< RegRenProcessingConstants.MIN_LNAME)
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtRecipientName());
	//	lsErrorFields += COMMA + RECIPIENT_NAME;
	//		}
	//		if (getTxtAddress1().getText().trim().length()
	//	< RegRenProcessingConstants.MIN_ADDR)
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtAddress1());
	//	lsErrorFields += COMMA + ADDRESS1;
	//		}
	//		if (getTxtCity().getText().trim().length()
	//	< RegRenProcessingConstants.MIN_CITY)
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtCity());
	//	lsErrorFields += COMMA + CITY;
	//		}
	//
	//		if (!getTxtPhoneNo().isValidPhoneNo())
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtPhoneNo());
	//	lsErrorFields += COMMA + PHONE_NO;
	//		}
	//
	//		if (getTxtZip().getText().length()
	//	< RegRenProcessingConstants.MIN_ZIP)
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtZip());
	//	lsErrorFields += COMMA + ZIP_CODE;
	//		}
	//		if (getTxtZipExt().getText().length() > 0
	//	&& getTxtZipExt().getText().length()
	//		!= RegRenProcessingConstants.MAX_ZIP_EXT)
	//		{
	//	leRTSEx.addException(
	//		new RTSException(
	//			RTSException.FAILURE_VALIDATION,
	//			EMPTY_STRING,
	//			VALIDATION),
	//		getTxtZipExt());
	//	lsErrorFields += COMMA + ZIP_EXT;
	//		}
	//		//error checking (incorrect format)
	//		if (leRTSEx.isValidationError())
	//		{
	//	if (lsErrorFields.startsWith(COMMA))
	//	{
	//		lsErrorFields =
	//			lsErrorFields.substring(2, lsErrorFields.length());
	//	}
	//	leRTSEx.setMessage(ENTERCORRECTFORMAT + lsErrorFields);
	//	leRTSEx.displayError(this);
	//	leRTSEx.getFirstComponent().requestFocus();
	//	return false;
	//		}
	//		return true;
	//	}
	// end defect 10397 

	/**
	 * This methods validates the address data that was entered before
	 * we update the DB with the new address data.
	 * 
	 * @return boolean
	 */
	private boolean validateData()
	{
		boolean lbValid = true;

		UtilityMethods.trimRTSInputField(this);

		RTSException leRTSEx = new RTSException();

		CommonValidations.addRTSExceptionForInvalidMFCharacters(
			cvMFValid,
			leRTSEx);

		// Renewal Recipient Name 
		if (gettxtRenewalRecipientName().isEmpty())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtRenewalRecipientName());
		}

		// defect 10397
		caRenwlAddrComp.validateAddressFields(leRTSEx);
		// end defect 10397 

		// Recipient EMail 
		if (!CommonValidations
			.isValidEMail(gettxtRecpntEMail().getText()))
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtRecpntEMail());
		}

		if (gettxtPhoneNo().isPhoneNoEmpty())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtPhoneNo());
		}

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
	}

	/**
	 * Handle when the cancel button is pressed.  This is called from
	 * actionperformed.
	 */
	private boolean verifyCancel()
	{
		boolean lbCancel = true;

		if (isRecordChanged())
		{
			// Confirm update; Prompt to continue. 
			RTSException leRTSEx =
				new RTSException(
					RTSException.CTL001,
					RegRenProcessingConstants.ERR_MODIFIED_REC_979,
					RTS000979);

			lbCancel = leRTSEx.displayError(this) == RTSException.YES;
		}
		return lbCancel;
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"