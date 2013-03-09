package com.txdot.isd.rts.client.title.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TitleConstant;

/*
 * 
 * FrmSalvageCCOMailingInfoTTL013.java
 *
 * (c) Texas Department of Transportation  2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Marx Rajangam			validation updated
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs			05/16/2002	Added capturing of mailing information 
 * 							CQU100003963
 * MAbs			05/28/2002	Added mail object in VehInqData to store 
 * 							mail info CQU100004122 
 * J Rue		03/10/2005	VAJ to WSAD Clean Up
 * 							defect 7898 Ver 5.2.3
 * J Rue		03/16/2005	VAJ to WSAD Clean Up
 * 							Add new JavaDoc standards.
 * 							defect 7898 Ver 5.2.3
 * J Rue		04/29/2005	Comment out unused variables
 * 							caVehicleInqData
 * 							defect 7898 Ver 5.2.3
 * J Zwiener	05/27/2005	Mouse will not check/uncheck USA box. Fixed.
 *							remove FocusListener
 *							modify getchkUSA()
 * 							commented out focusGained, focusLost
 * 							defect 7858 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899 Ver 5.2.3                 
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * J Rue		08/19/2005	Replace class constants with common const.
 * 							defect 7898 Ver 5.2.3   
 * J Rue		11/03/2005	Update incomplete method headers.
 * 							Define/Add CommonConstants where needed.
 * 							Move setText() verbage to class level.
 * 							Replace magic nums with meaningful verbage.
 * 							deprecate getBuilderData()
 * 							defect 7898 Ver 5.2.3    
 * K Harrell	04/28/2008	Update for USA checkbox
 * 							add itemStateChanged(), validateFields(),
 * 							  setDataToDateObject()  
 * 							modify initialize(), getchkUSA(),setData()
 * 							defect 9636 Ver Defect_POS_A 
 * K Harrell	06/09/2008	Cntry field was numeric.  
 * 							modify gettxtCntry()
 * 							defect 9636 Ver Defect_POS_A
 * K Harrell	09/03/2008	Use isValidState() vs. isState() 
 * 							modify validateFields()
 * 							defect 9811 Ver Defect_POS_B 
 * K Harrell	03/16/2009	Cleanup; Use Standard Constants for field
 * 							lengths.  
 * 							defect 9971 Ver Defect_POS_E
 * K Harrell	07/03/2009	Implement new LienholderData, NameAddressData
 * 							modify caMailingAddressData, setData(), 
 * 							 setDataToDataObject()  
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	07/18/2009	Implement new NameAddressComponent. 
 * 							refactor RTSInputFields, get methods to 
 * 							 standards 
 * 							add caNameAddrComp 
 * 							delete setNonUSAAddressDisplay(), 
 * 							 setUSAAddressDisplay(), EMPTY  
 * 							modify initialize(), itemStateChanged(), 
 * 							  refreshScreen(), setData(), 
 * 							  setDataToDataObject(), validateFields() 
 * 							defect 10127 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/**
 * This form is used to capture mailing address for CCO Salvage/NRCOT  
 *
 * @version	Defect_POS_F	07/15/2009 
 * @author	Michael Abernethy
 * <br>Creation Date:		06/26/2001 22:59:08
 */

public class FrmSalvageCCOMailingInfoTTL013
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkUSA = null;
	private JLabel ivjstcLblDash = null;
	private JLabel ivjstcLblMailCCOTo = null;
	private JLabel ivjstcLblMailingAddress = null;
	private JPanel ivjFrmSalvageCCOMailingInfoTTL013ContentPane1 = null;
	private RTSInputField ivjtxtMailingCntry = null;
	private RTSInputField ivjtxtMailingCntryZpcd = null;
	private RTSInputField ivjtxtMailingCity = null;
	private RTSInputField ivjtxtMailingName1 = null;
	private RTSInputField ivjtxtMailingName2 = null;
	private RTSInputField ivjtxtMailingState = null;
	private RTSInputField ivjtxtMailingStreet1 = null;
	private RTSInputField ivjtxtMailingStreet2 = null;
	private RTSInputField ivjtxtMailingZpcd = null;
	private RTSInputField ivjtxtMailingZpcdP4 = null;

	// Object 
	// defect 10112 
	private NameAddressData caMailingAddressData =
		new NameAddressData();
	// end defect 10112 

	// defect 10127
	private NameAddressComponent caNameAddrComp = null;
	// end defect 10127 

	private MFVehicleData caMFVehicleData;
	private VehicleInquiryData caVehInqData;

	// Constant String
	private final static String CCO_MAIL_TO = "Mail CCO To:";
	private final static String USA = "USA";
	private final static String MAILING_ADDRESS = "Mailing Address:";

	/**
	 * FrmSalvageCCOMailingInfoTTL013 constructor comment.
	 */
	public FrmSalvageCCOMailingInfoTTL013()
	{
		super();
		initialize();
	}

	/**
	 * FrmSalvageCCOMailingInfoTTL013 constructor.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmSalvageCCOMailingInfoTTL013(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmSalvageCCOMailingInfoTTL013 constructor.
	 * 
	 * @param aaParent javax.swing.JFrame
	 */
	public FrmSalvageCCOMailingInfoTTL013(JFrame aaParent)
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

					getController().processData(
						AbstractViewController.ENTER,
						caVehInqData);
				}
				// end defect 10127 
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{

				RTSHelp.displayHelp(RTSHelp.TTL013);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setBounds(38, 196, 300, 39);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
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
	 * Return the chkUSA property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkUSA()
	{
		if (ivjchkUSA == null)
		{
			try
			{
				ivjchkUSA = new JCheckBox();
				ivjchkUSA.setSize(49, 20);
				ivjchkUSA.setName("chkUSA");
				ivjchkUSA.setMnemonic(85);
				ivjchkUSA.setText(USA);
				ivjchkUSA.setMaximumSize(
					new java.awt.Dimension(49, 22));
				ivjchkUSA.setSelected(true);
				ivjchkUSA.setMinimumSize(
					new java.awt.Dimension(49, 22));
				// user code begin {1}
				ivjchkUSA.setLocation(278, 95);
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
	 * Return the FrmSalvageCCOMailingInfoTTL013ContentPane1 property 
	 * value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmSalvageCCOMailingInfoTTL013ContentPane1()
	{
		if (ivjFrmSalvageCCOMailingInfoTTL013ContentPane1 == null)
		{
			try
			{
				ivjFrmSalvageCCOMailingInfoTTL013ContentPane1 =
					new JPanel();
				ivjFrmSalvageCCOMailingInfoTTL013ContentPane1.setName(
					"FrmSalvageCCOMailingInfoTTL013ContentPane1");
				ivjFrmSalvageCCOMailingInfoTTL013ContentPane1
					.setLayout(
					null);
				ivjFrmSalvageCCOMailingInfoTTL013ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSalvageCCOMailingInfoTTL013ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					getstcLblMailCCOTo(),
					getstcLblMailCCOTo().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					getstcLblMailingAddress(),
					getstcLblMailingAddress().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingName1(),
					gettxtMailingName1().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingName2(),
					gettxtMailingName2().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingStreet1(),
					gettxtMailingStreet1().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingStreet2(),
					gettxtMailingStreet2().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingCity(),
					gettxtMailingCity().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingState(),
					gettxtMailingState().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingZpcd(),
					gettxtMailingZpcd().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					getstcLblDash(),
					getstcLblDash().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingZpcdP4(),
					gettxtMailingZpcdP4().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					getchkUSA(),
					getchkUSA().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingCntry(),
					gettxtMailingCntry().getName());
				getFrmSalvageCCOMailingInfoTTL013ContentPane1().add(
					gettxtMailingCntryZpcd(),
					gettxtMailingCntryZpcd().getName());
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
		return ivjFrmSalvageCCOMailingInfoTTL013ContentPane1;
	}

	/**
	 * Return the stcLblDash property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDash()
	{
		if (ivjstcLblDash == null)
		{
			try
			{
				ivjstcLblDash = new JLabel();
				ivjstcLblDash.setSize(6, 20);
				ivjstcLblDash.setName("stcLblDash");
				ivjstcLblDash.setText(CommonConstant.STR_DASH);
				ivjstcLblDash.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setMinimumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setLocation(283, 163);
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
	 * Return the stcLblMailCCOTo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMailCCOTo()
	{
		if (ivjstcLblMailCCOTo == null)
		{
			try
			{
				ivjstcLblMailCCOTo = new JLabel();
				ivjstcLblMailCCOTo.setSize(74, 20);
				ivjstcLblMailCCOTo.setName("stcLblMailCCOTo");
				ivjstcLblMailCCOTo.setText(CCO_MAIL_TO);
				ivjstcLblMailCCOTo.setMaximumSize(
					new java.awt.Dimension(71, 14));
				ivjstcLblMailCCOTo.setMinimumSize(
					new java.awt.Dimension(71, 14));
				ivjstcLblMailCCOTo.setLocation(52, 20);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblMailCCOTo;
	}

	/**
	 * Return the stcLblMailingAddress property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblMailingAddress()
	{
		if (ivjstcLblMailingAddress == null)
		{
			try
			{
				ivjstcLblMailingAddress = new JLabel();
				ivjstcLblMailingAddress.setSize(114, 20);
				ivjstcLblMailingAddress.setName("stcLblMailingAddress");
				ivjstcLblMailingAddress.setText(MAILING_ADDRESS);
				ivjstcLblMailingAddress.setMaximumSize(
					new java.awt.Dimension(94, 14));
				ivjstcLblMailingAddress.setMinimumSize(
					new java.awt.Dimension(94, 14));
				ivjstcLblMailingAddress.setLocation(51, 95);
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
	 * Return the txtMailingCntry property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtMailingCntry()
	{
		if (ivjtxtMailingCntry == null)
		{
			try
			{
				ivjtxtMailingCntry = new RTSInputField();
				ivjtxtMailingCntry.setName("txtMailingCntry");
				// defect 9636
				//ivjtxtMailingCntry.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtMailingCntry.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				// end defect 9636 
				ivjtxtMailingCntry.setBounds(207, 163, 39, 20);
				ivjtxtMailingCntry.setMaxLength(
					CommonConstant.LENGTH_CNTRY);
				// user code begin {1}
				ivjtxtMailingCntry.setText("");
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
	 * Return the ivjtxtMailingCntryZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtMailingCntryZpcd()
	{
		if (ivjtxtMailingCntryZpcd == null)
		{
			try
			{
				ivjtxtMailingCntryZpcd = new RTSInputField();
				ivjtxtMailingCntryZpcd.setName(
					"ivjtxtMailingCntryZpcd");
				ivjtxtMailingCntryZpcd.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtMailingCntryZpcd.setBounds(256, 163, 71, 20);
				ivjtxtMailingCntryZpcd.setMaxLength(
					CommonConstant.LENGTH_CNTRY_ZIP);
				// user code begin {1}
				ivjtxtMailingCntryZpcd.setText("");
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
	 * Return the txtMailingCity property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtMailingCity()
	{
		if (ivjtxtMailingCity == null)
		{
			try
			{
				ivjtxtMailingCity = new RTSInputField();
				ivjtxtMailingCity.setName("txtMailingCity");
				ivjtxtMailingCity.setInput(
					RTSInputField.ALPHANUMERIC_ONLY);
				ivjtxtMailingCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingCity.setBounds(51, 163, 150, 20);
				ivjtxtMailingCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
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
		return ivjtxtMailingCity;
	}

	/**
	 * Return the txtMailingName1 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtMailingName1()
	{
		if (ivjtxtMailingName1 == null)
		{
			try
			{
				ivjtxtMailingName1 = new RTSInputField();
				ivjtxtMailingName1.setName("txtMailingName1");
				ivjtxtMailingName1.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingName1.setBounds(51, 42, 276, 20);
				ivjtxtMailingName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
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
		return ivjtxtMailingName1;
	}

	/**
	 * Return the txtMailingName2 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtMailingName2()
	{
		if (ivjtxtMailingName2 == null)
		{
			try
			{
				ivjtxtMailingName2 = new RTSInputField();
				ivjtxtMailingName2.setName("txtMailingName2");
				ivjtxtMailingName2.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingName2.setBounds(51, 65, 276, 20);
				ivjtxtMailingName2.setMaxLength(
					CommonConstant.LENGTH_NAME);
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
		return ivjtxtMailingName2;
	}

	/**
	 * Return the txtMailingState property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtMailingState()
	{
		if (ivjtxtMailingState == null)
		{
			try
			{
				ivjtxtMailingState = new RTSInputField();
				ivjtxtMailingState.setName("txtMailingState");
				ivjtxtMailingState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtMailingState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingState.setBounds(207, 163, 24, 20);
				ivjtxtMailingState.setMaxLength(
					CommonConstant.LENGTH_STATE);
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
		return ivjtxtMailingState;
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
				ivjtxtMailingStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingStreet1.setBounds(51, 117, 276, 20);
				ivjtxtMailingStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
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
		return ivjtxtMailingStreet1;
	}

	/**
	 * Return the ivjtxtMailingStreet2 property value.
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
				ivjtxtMailingStreet2.setName("ivjtxtMailingStreet2");
				ivjtxtMailingStreet2.setInput(RTSInputField.DEFAULT);
				ivjtxtMailingStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingStreet2.setBounds(51, 140, 276, 20);
				ivjtxtMailingStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
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
		return ivjtxtMailingStreet2;
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
				ivjtxtMailingZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtMailingZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtMailingZpcd.setText("");
				ivjtxtMailingZpcd.setBounds(236, 163, 43, 20);
				ivjtxtMailingZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
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
		return ivjtxtMailingZpcd;
	}

	/**
	 * Return the ivjtxtMailingZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtMailingZpcdP4()
	{
		if (ivjtxtMailingZpcdP4 == null)
		{
			try
			{
				ivjtxtMailingZpcdP4 = new RTSInputField();
				ivjtxtMailingZpcdP4.setName("ivjtxtMailingZpcdP4");
				// defect 10127 
				ivjtxtMailingZpcdP4.setInput(
					RTSInputField.NUMERIC_ONLY);
				// end defect 10127 
				ivjtxtMailingZpcdP4.setText("");
				ivjtxtMailingZpcdP4.setBounds(293, 163, 34, 20);
				ivjtxtMailingZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
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
			setName(ScreenConstant.TTL013_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(388, 272);
			setTitle(ScreenConstant.TTL013_FRAME_TITLE);
			setContentPane(
				getFrmSalvageCCOMailingInfoTTL013ContentPane1());
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
	 * Starts the application.
	 * 
	 * @param aarrArgs an array of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSalvageCCOMailingInfoTTL013 laFrmSalvageCCOMailingInfoTTL013;
			laFrmSalvageCCOMailingInfoTTL013 =
				new FrmSalvageCCOMailingInfoTTL013();
			laFrmSalvageCCOMailingInfoTTL013.setModal(true);
			laFrmSalvageCCOMailingInfoTTL013
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent laRTSDBox)
				{
					System.exit(0);
				}
			});
			laFrmSalvageCCOMailingInfoTTL013.show();
			java.awt.Insets insets =
				laFrmSalvageCCOMailingInfoTTL013.getInsets();
			laFrmSalvageCCOMailingInfoTTL013.setSize(
				laFrmSalvageCCOMailingInfoTTL013.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSalvageCCOMailingInfoTTL013.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSalvageCCOMailingInfoTTL013.setVisibleRTS(true);
		}
		catch (Throwable aeTHRWEx)
		{
			System.err.println(ErrorsConstant.ERR_MSG_MAIN_JDIALOG);
			aeTHRWEx.printStackTrace(System.out);
		}
	}

	/**
	 * ItemListener method.
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(java.awt.event.ItemEvent aaIE)
	{
		// defect 10127 
		caNameAddrComp.resetPerUSA(
			aaIE.getStateChange() == ItemEvent.SELECTED);
		// end defect 10127
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
		caVehInqData = (VehicleInquiryData) aaDataObject;
		caMFVehicleData = caVehInqData.getMfVehicleData();

		// defect 10112 
		LienholderData laLienHolderData =
			caMFVehicleData.getTitleData().getLienholderData(
				TitleConstant.LIENHLDR1);
		// end defect 10112 

		OwnerData laOwnerData = caMFVehicleData.getOwnerData();

		// set the data from data object
		//   if there is a LienHolder, fill up with LienHolder address
		//   else with owner address
		// defect 10112 
		if (laLienHolderData.isPopulated())
		{
			// end defect 10112 
			caMailingAddressData =
				(NameAddressData) UtilityMethods.copy(laLienHolderData);
		}
		else
		{
			caMailingAddressData =
				(NameAddressData) UtilityMethods.copy(laOwnerData);
		}
		// end defect 10112 

		// defect 10127  
		caNameAddrComp.setNameAddressDataToDisplay(caMailingAddressData);
		// end defect 10127 
	}

	/**
	 * 
	 * Set Data To Data Object for Transaction Completion  
	 */
	private void setDataToDataObject()
	{
		// defect 10127 
		caNameAddrComp.setNameAddressToDataObject(caMailingAddressData);
		caVehInqData.setMailingAddress(
			(NameAddressData) UtilityMethods.copy(
				caMailingAddressData));
		// end defect 10127 
	}

	/**
	 * Validate Fields on Screen  
	 *
	 * @return boolean 
	 */
	private boolean validateFields()
	{
		boolean lbReturn = true;
		RTSException leRTSEx = new RTSException();

		// defect 10127 
		// Implement new NameAddressComponent for validation  
		// Empty:  Name, if required(Contact, Phone - 150);  Else - 186
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