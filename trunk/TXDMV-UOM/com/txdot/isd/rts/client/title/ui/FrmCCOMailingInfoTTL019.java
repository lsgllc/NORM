package com.txdot.isd.rts.client.title.ui;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 *  
 * FrmCCOCCDOMailingInfoTTL019.java
 * 
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validations update
 * M Rajangam 	04/11/2002	Adding javadoc, comments
 * MAbs			05/22/2002	CQU100004035
 * Min Wang     04/22/2003  Modified gettxtTitleMailingName2(), 
 * 							gettxtTitleStree1(), 
 *                          gettxtTitleStree2(), gettxtTitleZipcodeP4(), 
 *                          and gettxtCntryZpcd(). Defect 5703.
 * J Rue		05/07/2003	Defect 6074, The functionality of the USA 
 * 							check box in RTSII 
 *							is the same as RTSI. Create method 
 *							itemStateChanged()
 * B Arredondo	05/20/2003	Defect 6136, Modified method focusGained() 
 * 							by commenting out the code.
 * J Rue		02/17/2005	VAJ to WSAD clean up
 * 							defect 7898 Ver 5.2.3
 * J Rue		02/22/2005	Change AbstractViewController to
 * 							getController()
 * 							modify actionPerformed()
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/08/2005	VAJ to WSAD clean up
 * 							setVisible() to setVisibleRTS()
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * Ray Rowehl	03/21/2005	Use getters and setters for parent fields
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/30/2005	Set catch Parm aa to le
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/06/2005	Remove setFocusableComponent()
 * 							deprecated displayError(int)
 * 							defect 7898 Ver 7898
 * J Rue		05/05/2005	Add comments to process flow
 * 							modify setData()
 * 							defect 7898 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3        
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/17/2005	Replace class constants with project 
 * 							constants
 * 							defect 7898 Ver 5.2.3
 * J Rue		11/07/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * J Rue		07/28/2006	Restructure process flow from CURRENT to
 * 							DIRECT_CALL in the VC. This will prevent the 
 * 							application from passing through setData() 
 * 							multiple times.
 * 							modify setData()
 * 							defect 8600 Ver 5.2.4
 * K Harrell	04/22/2007	Use SystemProperty.isRegion()
 * 							delete getOfcIssuanceCd()
 * 							modify actionPerformed() 
 * 							defect 9085 Ver Special Plates 
 * K Harrell	04/26/2008	Cleanup; Should have no reference to 
 * 							 SCOT/NRCOT type transactions  
 * 							add validateFields(), setDataToDataObject()
 * 							delete caUSAMailingAddressData,
 * 								caCNTRYMailingAddressData
 * 							modify actionPerformed(), itemStateChanged(),
 * 								setData() 
 * 							defect 9636 Version Defect POS A   
 * K Harrell	03/07/2009	Update use of LienholderData. 
 * 							Implement CommonConstant vs. local for 
 * 							AddressLength specification. Additional 
 * 							cleanup including alignments via Vis Comp.
 * 							Moved USA checkbox above address.
 * 							add EMPTY  
 * 							delete local constants to use common
 * 							delete focusGained(), focusLost()
 * 							modify setData(), actionPerformed()
 * 							defect 9969 Ver Defect_POS_E
 * K Harrell	06/24/2009	Set USA checkbox according to data in 
 * 							 Address Object
 * 							modify setData()
 * 							defect 10106 Ver Defect_POS_F
 * K Harrell	07/01/2009	Implement new OwnerData, LienholderData, 
 * 							 NameAddressData 
 * 							modify caMailingAddressData, setData(),
 * 							 setDataToDataObject()  
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/18/2009	Implement new NameAddressComponent. 
 * 							refactor RTSInputFields, get methods to 
 * 							 standards 
 * 							add caNameAddrComp 
 * 							delete setNonUSAAddressDisplay(), 
 * 							 setUSAAddressDisplay(), EMPTY  
 * 							modify actionPerformed(), gettxtMailingZpcdP4(), 
 * 							 initialize(), itemStateChanged(), 
 * 							  setData(), setDataToDataObject(), 
 * 							  validateFields() 
 * 							defect 10127 Ver Defect_POS_F
 * K Harrell	08/04/2009	refactored from FrmCCOCCDOMailingInfoTTL019
 * 							CCDO discontinued via RTB 007-02, 1/14/2002,  
 *  						implemented in RTS 4.3 
 * 							defect 10112 Ver Defect_POS_F 
 * K Harrell	12/09/2010	Save data when cancel back
 * 							modify actionPerformed(), setData(),
 * 							  setDataToDataObject()
 * 							defect 10665 Ver 6.7.0  
 * ---------------------------------------------------------------------
 */

/**
 * The Mailing screen for CCO
 * 
 * @version	6.7.0  			12/09/2010
 * @author	Todd Pederson
 * <br>Creation Date:		08/03/2001 16:39:54
 */

public class FrmCCOMailingInfoTTL019
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkUSA = null;
	private JPanel ivjFrmCCOCCDOMailingInfoTTL019ContentPane1 = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblMailCCO = null;
	private JLabel ivjstcLblMailingAddress = null;
	private RTSInputField ivjtxtMailingCity = null;
	private RTSInputField ivjtxtMailingCntry = null;
	private RTSInputField ivjtxtMailingCntryZpcd = null;
	private RTSInputField ivjtxtMailingName1 = null;
	private RTSInputField ivjtxtMailingName2 = null;
	private RTSInputField ivjtxtMailingStreet1 = null;
	private RTSInputField ivjtxtMailingStreet2 = null;
	private RTSInputField ivjtxtMailingState = null;
	private RTSInputField ivjtxtMailingZpcd = null;
	private RTSInputField ivjtxtMailingZpcdP4 = null;

	// defect 10127
	private NameAddressComponent caNameAddrComp = null;
	// end defect 10127 

	/** 
	 * Complete Transaction data object of the class. Created and filled
	 *  and sent to the Transaction class to be posted to the Server db. 
	 */
	private CompleteTransactionData caCompTransData =
		new CompleteTransactionData();

	/** 
	 * Mailing address data object of the class. Attached to the 
	 * Complete Transaction data so that customer address can be printed 
	 * on the receipts
	 */
	// defect 10112
	private NameAddressData caMailingAddressData =
		new NameAddressData();
	// end defect 10112 

	//	Constants 
	private final static String MAIL_CCO_TO = "Mail CCO To: ";
	private final static String MAILING_ADDRESS = "Mailing Address:";

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmCCOMailingInfoTTL019 laFrmCCOMailingInfoTTL019;
			laFrmCCOMailingInfoTTL019 = new FrmCCOMailingInfoTTL019();
			laFrmCCOMailingInfoTTL019.setModal(true);
			laFrmCCOMailingInfoTTL019
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aeWE)
				{
					System.exit(0);
				}
			});
			laFrmCCOMailingInfoTTL019.show();
			java.awt.Insets insets =
				laFrmCCOMailingInfoTTL019.getInsets();
			laFrmCCOMailingInfoTTL019.setSize(
				laFrmCCOMailingInfoTTL019.getWidth()
					+ insets.left
					+ insets.right,
				laFrmCCOMailingInfoTTL019.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmCCOMailingInfoTTL019.setVisibleRTS(true);
		}
		catch (Throwable aeIVJEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeIVJEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmCCOMailingInfoTTL019 constructor comment.
	 */
	public FrmCCOMailingInfoTTL019()
	{
		super();
		initialize();
	}

	/**
	 * FrmCCOMailingInfoTTL019 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmCCOMailingInfoTTL019(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmCCOMailingInfoTTL019 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCCOMailingInfoTTL019(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs. If the action originates from
	 * Enter, Cancel, Help
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

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10127 
				// One return 
				if (validateFields())
				{
					setDataToDataObject();

					int liAction = AbstractViewController.ENTER;

					if (SystemProperty.isHQ())
					{
						// Recreate CompleteTransactionData 
						CompleteTitleTransaction laTtlTrans =
							new CompleteTitleTransaction(
								(VehicleInquiryData) (getController()
									.getData()),
								getController().getTransCode());

						caCompTransData =
							laTtlTrans.doCompleteTransaction();

						liAction = VCCCOMailingInfoTTL019.ADD_TRANS;
					}

					caCompTransData.setMailingAddrData(
						(NameAddressData) UtilityMethods.copy(
							caMailingAddressData));

					getController().processData(
						liAction,
						caCompTransData);
					// end defect 10127 
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				// defect 10665 
				setDataToDataObject();
				// end defect 10665  

				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.TTL019);
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError();
		}
		finally
		{
			doneWorking();
		}
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setLayout(new java.awt.FlowLayout());
				ivjButtonPanel1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setBounds(60, 221, 303, 46);
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

	/**
	 * Return the ivjchkUSA property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkUSA()
	{
		if (ivjchkUSA == null)
		{
			try
			{
				ivjchkUSA = new JCheckBox();
				ivjchkUSA.setSize(49, 24);
				ivjchkUSA.setName("ivjchkUSA");
				ivjchkUSA.setMaximumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA.setActionCommand(CommonConstant.STR_USA);
				ivjchkUSA.setSelected(true);
				ivjchkUSA.setMinimumSize(
					new java.awt.Dimension(49, 22));
				// user code begin {1}
				ivjchkUSA.setText(CommonConstant.STR_USA);
				ivjchkUSA.setMnemonic(KeyEvent.VK_U);
				ivjchkUSA.setLocation(345, 101);
				ivjchkUSA.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkUSA;
	}

	/**
	 * Return the FrmCCOCCDOMailingInfoTTL019ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	private javax
		.swing
		.JPanel getFrmCCOCCDOMailingInfoTTL019ContentPane1()
	{
		if (ivjFrmCCOCCDOMailingInfoTTL019ContentPane1 == null)
		{
			try
			{
				ivjFrmCCOCCDOMailingInfoTTL019ContentPane1 =
					new JPanel();
				ivjFrmCCOCCDOMailingInfoTTL019ContentPane1.setName(
					"ivjFrmCCOCCDOMailingInfoTTL019ContentPane1");
				ivjFrmCCOCCDOMailingInfoTTL019ContentPane1.setLayout(
					null);
				ivjFrmCCOCCDOMailingInfoTTL019ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmCCOCCDOMailingInfoTTL019ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(635, 238));
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					getstcLblMailCCO(),
					getstcLblMailCCO().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingName1(),
					gettxtMailingName1().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingName2(),
					gettxtMailingName2().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					getstcLblMailingAddress(),
					getstcLblMailingAddress().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingStreet1(),
					gettxtMailingStreet1().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingStreet2(),
					gettxtMailingStreet2().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingCity(),
					gettxtMailingCity().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					getstcLblDash(),
					getstcLblDash().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingState(),
					gettxtMailingState().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingZpcd(),
					gettxtMailingZpcd().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingZpcdP4(),
					gettxtMailingZpcdP4().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingCntry(),
					gettxtMailingCntry().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					gettxtMailingCntryZpcd(),
					gettxtMailingCntryZpcd().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmCCOCCDOMailingInfoTTL019ContentPane1().add(
					getchkUSA(),
					getchkUSA().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeTHRWEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeTHRWEx);
			}
		}
		return ivjFrmCCOCCDOMailingInfoTTL019ContentPane1;
	}

	/**
	 * Return the ivjstcLbldash property value.
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
				ivjstcLblDash.setName("ivjstcLbldash");
				ivjstcLblDash.setText(CommonConstant.STR_DASH);
				ivjstcLblDash.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setBounds(348, 178, 6, 20);
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
		return ivjstcLblDash;
	}

	/**
	 * Return the ivjstcLblMailCCO property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMailCCO()
	{
		if (ivjstcLblMailCCO == null)
		{
			try
			{
				ivjstcLblMailCCO = new JLabel();
				ivjstcLblMailCCO.setSize(81, 20);
				ivjstcLblMailCCO.setName("ivjstcLblMailCCO");
				ivjstcLblMailCCO.setText(MAIL_CCO_TO);
				ivjstcLblMailCCO.setMaximumSize(
					new java.awt.Dimension(110, 14));
				ivjstcLblMailCCO.setMinimumSize(
					new java.awt.Dimension(110, 14));
				ivjstcLblMailCCO.setLocation(27, 19);
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
		return ivjstcLblMailCCO;
	}

	/**
	 * Return the ivjstcLblMailingAddress property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblMailingAddress()
	{
		if (ivjstcLblMailingAddress == null)
		{
			try
			{
				ivjstcLblMailingAddress = new JLabel();
				ivjstcLblMailingAddress.setSize(118, 20);
				ivjstcLblMailingAddress.setName(
					"ivjstcLblMailingAddress");
				ivjstcLblMailingAddress.setText(MAILING_ADDRESS);
				ivjstcLblMailingAddress.setMaximumSize(
					new java.awt.Dimension(94, 14));
				ivjstcLblMailingAddress.setMinimumSize(
					new java.awt.Dimension(94, 14));
				ivjstcLblMailingAddress.setLocation(28, 103);
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
		return ivjstcLblMailingAddress;
	}

	/**
	 * Return the ivjtxtMailingCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingCity()
	{
		if (ivjtxtMailingCity == null)
		{
			try
			{
				ivjtxtMailingCity = new RTSInputField();
				ivjtxtMailingCity.setSize(195, 20);
				ivjtxtMailingCity.setName("ivjtxtMailingCity");
				ivjtxtMailingCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingCity.setLocation(50, 178);
				// user code begin {1}										
				ivjtxtMailingCity.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingCity;
	}

	/**
	 * Return the ivjtxtMailingCntry property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingCntry()
	{
		if (ivjtxtMailingCntry == null)
		{
			try
			{
				ivjtxtMailingCntry = new RTSInputField();
				ivjtxtMailingCntry.setName("ivjtxtMailingCntry");
				ivjtxtMailingCntry.setBounds(257, 178, 59, 20);
				// user code begin {1}				
				ivjtxtMailingCntry.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingCntry.setMaxLength(
					CommonConstant.LENGTH_CNTRY);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingCntry;
	}

	/**
	 * Return the txtMailingCntryZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingCntryZpcd()
	{
		if (ivjtxtMailingCntryZpcd == null)
		{
			try
			{
				ivjtxtMailingCntryZpcd = new RTSInputField();
				ivjtxtMailingCntryZpcd.setName(
					"ivjtxtMailingCntryZpcd");
				ivjtxtMailingCntryZpcd.setBounds(327, 178, 67, 20);
				// user code begin {1}				
				ivjtxtMailingCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtMailingCntryZpcd.setMaxLength(
					CommonConstant.LENGTH_CNTRY_ZIP);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingCntryZpcd;
	}

	/**
	 * Return the ivjtxtMailingName1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingName1()
	{
		if (ivjtxtMailingName1 == null)
		{
			try
			{
				ivjtxtMailingName1 = new RTSInputField();
				ivjtxtMailingName1.setName("ivjtxtMailingName1");
				ivjtxtMailingName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingName1.setBounds(50, 45, 344, 20);
				// user code begin {1}				
				ivjtxtMailingName1.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingName1;
	}

	/**
	 * Return the ivjtxtMailingName2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingName2()
	{
		if (ivjtxtMailingName2 == null)
		{
			try
			{
				ivjtxtMailingName2 = new RTSInputField();
				ivjtxtMailingName2.setSize(344, 20);
				ivjtxtMailingName2.setName("ivjtxtMailingName2");
				ivjtxtMailingName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingName2.setLocation(50, 69);
				// user code begin {1}									
				ivjtxtMailingName2.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingName2.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingName2;
	}

	/**
	 * Return the ivjtxtMailingStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingStreet1()
	{
		if (ivjtxtMailingStreet1 == null)
		{
			try
			{
				ivjtxtMailingStreet1 = new RTSInputField();
				ivjtxtMailingStreet1.setName("ivjtxtMailingStreet1");
				ivjtxtMailingStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingStreet1.setBounds(50, 130, 344, 20);
				// user code begin {1}				
				ivjtxtMailingStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingStreet1;
	}

	/**
	 * Return the ivjtxtMailingSt2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingStreet2()
	{
		if (ivjtxtMailingStreet2 == null)
		{
			try
			{
				ivjtxtMailingStreet2 = new RTSInputField();
				ivjtxtMailingStreet2.setSize(344, 20);
				ivjtxtMailingStreet2.setName("ivjtxtMailingSt2");
				ivjtxtMailingStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingStreet2.setLocation(50, 154);
				// user code begin {1}						
				ivjtxtMailingStreet2.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingStreet2;
	}

	/**
	 * Return the ivjtxState property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingState()
	{
		if (ivjtxtMailingState == null)
		{
			try
			{
				ivjtxtMailingState = new RTSInputField();
				ivjtxtMailingState.setName("ivjtxtMailingState");
				ivjtxtMailingState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingState.setBounds(258, 178, 25, 20);
				// user code begin {1}				
				ivjtxtMailingState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtMailingState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingState;
	}

	/**
	 * Return the ivjtxtMailingZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingZpcd()
	{
		if (ivjtxtMailingZpcd == null)
		{
			try
			{
				ivjtxtMailingZpcd = new RTSInputField();
				ivjtxtMailingZpcd.setName("ivjtxtMailingZpcd");
				ivjtxtMailingZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingZpcd.setBounds(297, 178, 43, 20);
				// user code begin {1}				
				ivjtxtMailingZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtMailingZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingZpcd;
	}

	/**
	 * Return the ivjtxtMailingZpCdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMailingZpcdP4()
	{
		if (ivjtxtMailingZpcdP4 == null)
		{
			try
			{
				ivjtxtMailingZpcdP4 = new RTSInputField();
				ivjtxtMailingZpcdP4.setName("ivjtxtMailingZpCdP4");
				ivjtxtMailingZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingZpcdP4.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjtxtMailingZpcdP4.setBounds(359, 178, 35, 20);
				// user code begin {1}				
				// defect 10127 
				// vs. DEFAULT 
				ivjtxtMailingZpcdP4.setInput(
					RTSInputField.NUMERIC_ONLY);
				// end defect 10127  
				ivjtxtMailingZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtMailingZpcdP4;
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
			setName(ScreenConstant.TTL019_FRAME_NAME);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(426, 295);
			setTitle(ScreenConstant.TTL019_FRAME_TITLE);
			setContentPane(
				getFrmCCOCCDOMailingInfoTTL019ContentPane1());
			setRequestFocus(false);
			// user code begin {1}
			// defect 10127 
			caNameAddrComp =
				new NameAddressComponent(
					gettxtMailingName1(),
					gettxtMailingName2(),
					gettxtMailingStreet1(),
					gettxtMailingStreet2(),
					gettxtMailingCity(),
					gettxtMailingState(),
					gettxtMailingZpcd(),
					gettxtMailingZpcdP4(),
					gettxtMailingCntry(),
					gettxtMailingCntryZpcd(),
					getchkUSA(),
					getstcLblDash(),
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID,
					ErrorsConstant.ERR_NUM_INCOMPLETE_ADDR_DATA,
					CommonConstant.TX_NOT_DEFAULT_STATE);
			// end defect 10127 
			// user code end
		}
		catch (Throwable aeTHRWEx)
		{
			handleException(aeTHRWEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Save data for USA check box Selected/Deselected.
	 * 
	 * @param aaIE ItemEvent 
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		if (aaIE.getSource() == getchkUSA())
		{
			// defect 10127 
			caNameAddrComp.resetPerUSA(
				aaIE.getStateChange() == ItemEvent.SELECTED);
			// end defect 10127
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 */
	public void setData(Object aaDataObject)
	{
		// defect 9969 
		if (aaDataObject != null)
		{
			// defect 10112
			// Use Local Variables for MFVehicleData, VehicleInquiry   
			MFVehicleData laMFVehicleData = new MFVehicleData();

			// Region: CompleteTransaction;  HQ: VehicleInquiryData 	  
			if (aaDataObject instanceof CompleteTransactionData)
			{
				caCompTransData =
					(CompleteTransactionData) aaDataObject;
				laMFVehicleData = caCompTransData.getOrgVehicleInfo();
			}
			else if (aaDataObject instanceof VehicleInquiryData)
			{
				VehicleInquiryData laVehInqData =
					(VehicleInquiryData) aaDataObject;

				laMFVehicleData = laVehInqData.getMfVehicleData();
			}

			LienholderData laLienhldrData =
				laMFVehicleData.getTitleData().getLienholderData(
					TitleConstant.LIENHLDR1);

			OwnerData laOwnerData = laMFVehicleData.getOwnerData();

			// Use NameAddressData to reference either OwnerData or 
			//  LienholderData or Saved Data 
			NameAddressData laNameAddrData = new NameAddressData();
			
			// defect 10665 
			Object laTTL019 =
				getController().getMediator().openVault(
					ScreenConstant.TTL019);

			if (laTTL019 != null
				&& laTTL019 instanceof ScreenTTL019SavedData)
			{
				ScreenTTL019SavedData laTTL019Data =
					(ScreenTTL019SavedData) laTTL019;

				laNameAddrData = laTTL019Data.getMailingNameAddressData();
			}
			else
			{
				// Use 1st Lienholder if exists
				if (laLienhldrData != null
					&& laLienhldrData.isPopulated())
				{
					laNameAddrData = (NameAddressData) laLienhldrData;
				}
				else
				{
					laNameAddrData = (NameAddressData) laOwnerData;
				}
				// end defect 10112
			}
			// end defect 10665 

			// defect 10127  
			caNameAddrComp.setNameAddressDataToDisplay(laNameAddrData);
			// end defect 10127 
		}

	}

	/**
	 * Set Data from Screen to Objects
	 * 
	 */
	private void setDataToDataObject()
	{
		// defect 10127 
		caNameAddrComp.setNameAddressToDataObject(caMailingAddressData);
		// end defect 10127 

		// defect 10665 
		ScreenTTL019SavedData laTTL019Data =
			new ScreenTTL019SavedData();

		laTTL019Data.setMailingNameAddressData(
			(NameAddressData) UtilityMethods.copy(
				caMailingAddressData));

		getController().getMediator().closeVault(
			ScreenConstant.TTL019,
			UtilityMethods.copy(laTTL019Data));
		// end defect 10665 
	}

	/**
	 * Validate Screen Data
	 * 
	 * @return boolean 
	 */
	private boolean validateFields()
	{
		boolean lbReturn = true;
		RTSException leRTSEx = new RTSException();

		// defect 10127 
		caNameAddrComp.validateNameAddressFields(leRTSEx);
		// end defect 10127 

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
