package com.txdot.isd.rts.client.registration.ui;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *  FrmOwnerRegistrationREG032.java
 *
 * c Texas Department of Transportation  2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name   		Date		Description
 * ------------	-----------	--------------------------------------------
 * K Salvi		10/01/2002	added state validation (other than checking 
 * 							for	empty input).
 *							defect 4804  
 * K Harrell	04/22/2003	Edit OwnerId for numerics only
 * 							defect 5981 
 * E LyBrand	07/15/2003	Remove mnemonic value 
 *							for OwnerAddress(), OwnerIDNo,
 *							and OwnerName. Add mnemonic value 
 *							back (all within VCE),
 *							allowing VAJ to rebuild value 
 *							as the specified 'character' rather 
 *							than a numeric value...presumably 
 *							the ascii value of the named 
 *							character.
 *							defect 6080   
 * Ray Rowehl	08/13/2003	Set From MF to always be true so 
 *							MV Func will show MFUP.
 *							modify buildCompleteTransactionData
 *							defect 6011
 * Min Wang		11/23/2004	Fix Fast Path Keys.
 *							modify via VCE 
 *							resize stcLblOwnerAddress,
 *							stcLblOwnerIDNo, stcLblOwnerName
 *							defect 6080 Ver 5.2.2
 * B Hargrove	07/18/2005	Modify code for move to Java 1.4. Bring code
 *							to standards.
 * 							defect 7894 Ver 5.2.3 
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	02/13/2006	Do not modify MFVehicleData.cbFromMF as 
 * 							now initialized to true
 * 							modify actionPerformed(),
 * 							  buildCompleteTransactionData() 
 * 							defect 6861 Ver 5.2.3  
 * K Harrell	10/18/2006	set ExemptIndi for DRVED
 * 							delete getBuilderData()
 * 							modify buildCompleteTransactionData()
 * 							defect 8900 Ver Exempts
 * K Harrell	07/01/2009	Implement new OwnerData
 * 							modify buildCompleteTransactionData() 
 * 							defect 10112 Ver Defect_POS_F
 * K Harrell	08/04/2009	Disabled OwnerId/Lable if not HQ 
 * 							modify setData() 
 * 							defect 10130 Ver Defect_POS_F
 * K Harrell	08/30/2009	Implement NameAddressComponent
 * 							add DRVR_ED_REG_CLASS_CD,EXMPT_REG_PLT_CD
 * 							add caOwnrNameAddrComp, caOwnerData 
 * 							add ivjtxtOwnerStreet1(), ivjtxtOwnerStreet2, 
 * 							 ivjtxtOwnerCity, ivjtxtOwnerState,
 * 							 ivjtxtOwnerZpcd, ivjtxtZpcdP4, get methods
 * 							delete ERRNO_150, REG_CLASS_CD, DFLT_STATE,
 * 							  REG_PLT_CD
 * 							delete ivjtxtOwnerAddress1, ivjtxtOwnerAddress2,
 * 							 ivjtxtOwnerAddressCity, ivjtxtOwnerAddressState,
 * 							 ivjtxtOwnerAddressZip1, ivjtxtOwnerAddressZip2,
 * 							 get methods  
 * 							modify initialize(), setData(), 
 * 							  buildCompleteTransactionData(),
 * 							  getFrmOwnerRegistrationREG032ContentPane1(), 
 * 							  validateInput() 
 * 							defect 10127 Ver Defect_POS_F 
 * K Harrell	03/09/2010	Add validation for OwnerId.  Implement
 * 							CommonConstants for field lengths, constants
 * 							for RTSInput types.  
 * 							modify validateInput(), gettxtOwnerId(), 
 * 							 gettxtOwnerName1(),gettxtOwnerName2(), 
 * 							 gettxtOwnerStreet1(), gettxtOwnerStreet2(), 
 * 							 gettxtOwnerCity(), gettxtOwnerState(), 
 * 							 gettxtOwnerZpcd(), gettxtOwnerZpcdP4()  
 * 							defect 10188 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/**
 * This form is used for the Issue Driver Education event.
 *
 * @version POS_640   		03/09/2010
 * @author  Nancy Ting
 * <br>Creation Date:		08/05/2002 14:45:21
 *
 */
public class FrmOwnerRegistrationREG032
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstcLblOwnerAddress = null;
	private JLabel ivjstcLblOwnerIDNo = null;
	private JLabel ivjstcLblOwnerName = null;

	// defect 10127 
	private RTSInputField ivjtxtOwnerStreet1 = null;
	private RTSInputField ivjtxtOwnerStreet2 = null;
	private RTSInputField ivjtxtOwnerCity = null;
	private RTSInputField ivjtxtOwnerState = null;
	private RTSInputField ivjtxtOwnerZpcd = null;
	private RTSInputField ivjtxtOwnerZpcdP4 = null;
	// end defect 10127 

	private RTSInputField ivjtxtOwnerId = null;
	private RTSInputField ivjtxtOwnerName1 = null;
	private RTSInputField ivjtxtOwnerName2 = null;
	private JPanel ivjFrmOwnerRegistrationREG032ContentPane1 = null;
	private JLabel ivjstcLblDash = null;

	// defect 10127 
	private NameAddressComponent caOwnrNameAddrComp = null;

	// Constants 
	// int 
	private final static int DRVR_ED_REG_CLASS_CD = 12;

	// String
	private final static String EXMPT_REG_PLT_CD = "EXPDBL";
	// end defect 10127 

	private final static String DASH = "-";
	private final static String OWNR_ADDR = "Owner Address:";
	private final static String OWNR_ID = "Owner Id Number:";
	private final static String OWNR_NAME = "Owner Name:";
	private final static String TITLE_REG032 =
		"Owner Registration    REG032";

	/**
	 * FrmOwnerRegistrationREG032 constructor comment.
	 */
	public FrmOwnerRegistrationREG032()
	{
		super();
		initialize();
	}

	/**
	 * FrmOwnerRegistrationREG032 constructor comment.
	 * 
	 * @param aaOwner java.awt.Dialog
	 */

	public FrmOwnerRegistrationREG032(java.awt.Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmOwnerRegistrationREG032 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmOwnerRegistrationREG032(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
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

			// CANCEL 
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// ENTER 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateInput())
				{
					getController().processData(
						AbstractViewController.ENTER,
						buildCompleteTransactionData());
				}
			}

			// HELP 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				getController().processData(
					AbstractViewController.HELP,
					null);
			}
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Build CompleteTransactionData
	 * 
	 * @return CompleteTransactionData
	 */
	private CompleteTransactionData buildCompleteTransactionData()
	{
		CompleteTransactionData laCompleteTransactionData =
			new CompleteTransactionData();

		// MF Vehicle Data 	
		MFVehicleData laMFVehicleData = new MFVehicleData();
		laCompleteTransactionData.setVehicleInfo(laMFVehicleData);
		laCompleteTransactionData.setTransCode(TransCdConstant.DRVED);

		// Registration Data 
		RegistrationData laRegData = new RegistrationData();
		laMFVehicleData.setRegData(laRegData);
		laRegData.setRegPltCd(EXMPT_REG_PLT_CD);
		// defect 10127 
		laRegData.setRegClassCd(DRVR_ED_REG_CLASS_CD);
		// end defect 10127 
		laRegData.setRegEffDt(new RTSDate().getYYYYMMDDDate());
		// defect 8900
		laRegData.setExmptIndi(1);
		// end defect 8900  

		// Inventory 
		Vector lvInv = new Vector();
		ProcessInventoryData laProcInvData = new ProcessInventoryData();
		// defect 10127 
		laProcInvData.setItmCd(EXMPT_REG_PLT_CD);
		// end defect 10127 
		laProcInvData.setInvQty(1);
		laProcInvData.setOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		laProcInvData.setSubstaId(SystemProperty.getSubStationId());
		laProcInvData.setTransEmpId(SystemProperty.getCurrentEmpId());
		laProcInvData.setTransWsId(
			Integer.toString(SystemProperty.getWorkStationId()));
		lvInv.add(laProcInvData);
		laCompleteTransactionData.setInvItms(lvInv);

		Vector lvAlloc = new Vector();
		laCompleteTransactionData.setAllocInvItms(lvAlloc);

		// Owner Data 
		OwnerData laOwnerData = new OwnerData();
		// defect 10127 
		caOwnrNameAddrComp.setNameAddressToDataObject(laOwnerData);
		// end defect 10127
		laOwnerData.setOwnrId(gettxtOwnerId().getText().trim());
		laMFVehicleData.setOwnerData(laOwnerData);

		return laCompleteTransactionData;
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
				ivjButtonPanel1.setBounds(65, 310, 241, 65);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				ivjButtonPanel1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the FrmOwnerRegistrationREG032ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmOwnerRegistrationREG032ContentPane1()
	{
		if (ivjFrmOwnerRegistrationREG032ContentPane1 == null)
		{
			try
			{
				ivjFrmOwnerRegistrationREG032ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmOwnerRegistrationREG032ContentPane1.setName(
					"FrmOwnerRegistrationREG032ContentPane1");
				ivjFrmOwnerRegistrationREG032ContentPane1.setLayout(
					null);
				ivjFrmOwnerRegistrationREG032ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmOwnerRegistrationREG032ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(360, 400));
				ivjFrmOwnerRegistrationREG032ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmOwnerRegistrationREG032ContentPane1().add(
					getstcLblOwnerIDNo(),
					getstcLblOwnerIDNo().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					getstcLblOwnerName(),
					getstcLblOwnerName().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					getstcLblOwnerAddress(),
					getstcLblOwnerAddress().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					gettxtOwnerId(),
					gettxtOwnerId().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					gettxtOwnerName1(),
					gettxtOwnerName1().getName());
				// defect 10127 
				getFrmOwnerRegistrationREG032ContentPane1().add(
					gettxtOwnerName2(),
					gettxtOwnerName2().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					gettxtOwnerStreet1(),
					gettxtOwnerStreet1().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					gettxtOwnerStreet2(),
					gettxtOwnerStreet2().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					gettxtOwnerCity(),
					gettxtOwnerCity().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					gettxtOwnerState(),
					gettxtOwnerState().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					gettxtOwnerZpcd(),
					gettxtOwnerZpcd().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					gettxtOwnerZpcdP4(),
					gettxtOwnerZpcdP4().getName());
				// end defect 10127 

				getFrmOwnerRegistrationREG032ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmOwnerRegistrationREG032ContentPane1().add(
					getstcLblDash(),
					getstcLblDash().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjFrmOwnerRegistrationREG032ContentPane1;
	}

	/**
	 * Return the stcLblDash property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblDash()
	{
		if (ivjstcLblDash == null)
		{
			try
			{
				ivjstcLblDash = new javax.swing.JLabel();
				ivjstcLblDash.setName("stcLblDash");
				ivjstcLblDash.setText(DASH);
				ivjstcLblDash.setMaximumSize(
					new java.awt.Dimension(4, 14));
				ivjstcLblDash.setBounds(293, 276, 4, 14);
				ivjstcLblDash.setMinimumSize(
					new java.awt.Dimension(4, 14));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblDash;
	}

	/**
	 * Return the stcLblOwnerAddress property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblOwnerAddress()
	{
		if (ivjstcLblOwnerAddress == null)
		{
			try
			{
				ivjstcLblOwnerAddress = new javax.swing.JLabel();
				ivjstcLblOwnerAddress.setName("stcLblOwnerAddress");
				ivjstcLblOwnerAddress.setDisplayedMnemonic('R');
				ivjstcLblOwnerAddress.setText(OWNR_ADDR);
				ivjstcLblOwnerAddress.setMaximumSize(
					new java.awt.Dimension(92, 14));
				ivjstcLblOwnerAddress.setBounds(30, 180, 131, 22);
				ivjstcLblOwnerAddress.setMinimumSize(
					new java.awt.Dimension(92, 14));
				// user code begin {1}
				ivjstcLblOwnerAddress.setLabelFor(gettxtOwnerStreet1());
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblOwnerAddress;
	}

	/**
	 * Return the stcLblOwnerIDNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblOwnerIDNo()
	{
		if (ivjstcLblOwnerIDNo == null)
		{
			try
			{
				ivjstcLblOwnerIDNo = new javax.swing.JLabel();
				ivjstcLblOwnerIDNo.setName("stcLblOwnerIDNo");
				ivjstcLblOwnerIDNo.setDisplayedMnemonic('W');
				ivjstcLblOwnerIDNo.setText(OWNR_ID);
				ivjstcLblOwnerIDNo.setMaximumSize(
					new java.awt.Dimension(102, 14));
				ivjstcLblOwnerIDNo.setBounds(30, 21, 207, 21);
				ivjstcLblOwnerIDNo.setMinimumSize(
					new java.awt.Dimension(102, 14));
				ivjstcLblOwnerIDNo.setHorizontalAlignment(2);
				// user code begin {1}
				ivjstcLblOwnerIDNo.setLabelFor(gettxtOwnerId());
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblOwnerIDNo;
	}

	/**
	 * Return the stcLblOwnerName property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblOwnerName()
	{
		if (ivjstcLblOwnerName == null)
		{
			try
			{
				ivjstcLblOwnerName = new javax.swing.JLabel();
				ivjstcLblOwnerName.setName("stcLblOwnerName");
				ivjstcLblOwnerName.setDisplayedMnemonic('O');
				ivjstcLblOwnerName.setText(OWNR_NAME);
				ivjstcLblOwnerName.setMaximumSize(
					new java.awt.Dimension(77, 14));
				ivjstcLblOwnerName.setBounds(30, 83, 116, 21);
				ivjstcLblOwnerName.setMinimumSize(
					new java.awt.Dimension(77, 14));
				// user code begin {1}
				ivjstcLblOwnerName.setLabelFor(gettxtOwnerName1());
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjstcLblOwnerName;
	}

	/**
	 * Return the ivjtxtOwnerStreet1 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerStreet1()
	{
		if (ivjtxtOwnerStreet1 == null)
		{
			try
			{
				ivjtxtOwnerStreet1 = new RTSInputField();
				ivjtxtOwnerStreet1.setName("ivjtxtOwnerStreet1");
				ivjtxtOwnerStreet1.setManagingFocus(false);
				ivjtxtOwnerStreet1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet1.setBounds(30, 208, 303, 20);

				// defect 10188 
				//ivjtxtOwnerStreet1.setInput(-1);
				//ivjtxtOwnerStreet1.setMaxLength(30); 
				ivjtxtOwnerStreet1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet1.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10188 

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerStreet1;
	}

	/**
	 * Return the ivjtxtOwnerStreet2 property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerStreet2()
	{
		if (ivjtxtOwnerStreet2 == null)
		{
			try
			{
				ivjtxtOwnerStreet2 = new RTSInputField();
				ivjtxtOwnerStreet2.setName("ivjtxtOwnerStreet2");
				ivjtxtOwnerStreet2.setManagingFocus(false);
				ivjtxtOwnerStreet2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerStreet2.setBounds(30, 240, 303, 20);

				// defect 10188 
				//ivjtxtOwnerStreet2.setInput(-1);
				//ivjtxtOwnerStreet2.setMaxLength(30); 
				ivjtxtOwnerStreet2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerStreet2.setMaxLength(
					CommonConstant.LENGTH_STREET);
				// end defect 10188 

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerStreet2;
	}

	/**
	 * Return the ivjtxtOwnerCity property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerCity()
	{
		if (ivjtxtOwnerCity == null)
		{
			try
			{
				ivjtxtOwnerCity = new RTSInputField();
				ivjtxtOwnerCity.setName("txtOwnerAddressCity");
				ivjtxtOwnerCity.setManagingFocus(false);
				ivjtxtOwnerCity.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerCity.setBounds(30, 273, 181, 20);

				// defect 10188 
				//ivjtxtOwnerCity.setInput(-1); 
				//ivjtxtOwnerCity.setMaxLength(19); 
				ivjtxtOwnerCity.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerCity.setMaxLength(
					CommonConstant.LENGTH_CITY);
				// end defect 10188 

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerCity;
	}

	/**
	 * Return the ivjtxtOwnerState property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOwnerState()
	{
		if (ivjtxtOwnerState == null)
		{
			try
			{
				ivjtxtOwnerState = new RTSInputField();
				ivjtxtOwnerState.setName("ivjtxtOwnerState");
				ivjtxtOwnerState.setManagingFocus(false);
				ivjtxtOwnerState.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerState.setBounds(213, 273, 30, 20);

				// defect 10188 
				//ivjtxtOwnerState.setInput(0);
				//ivjtxtOwnerState.setMaxLength(2); 
				ivjtxtOwnerState.setInput(RTSInputField.ALPHA_ONLY);
				ivjtxtOwnerState.setMaxLength(
					CommonConstant.LENGTH_STATE);
				// end defect 10188

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerState;
	}

	/**
	 * Return the ivjtxtOwnerZpcd property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtOwnerZpcd()
	{
		if (ivjtxtOwnerZpcd == null)
		{
			try
			{
				ivjtxtOwnerZpcd = new RTSInputField();
				ivjtxtOwnerZpcd.setName("ivjtxtOwnerZpcd");
				ivjtxtOwnerZpcd.setManagingFocus(false);
				ivjtxtOwnerZpcd.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerZpcd.setBounds(245, 273, 46, 20);

				// defect 10188 
				//ivjtxtOwnerZpcd.setInput(1);
				//ivjtxtOwnerZpcd.setMaxLength(5);
				ivjtxtOwnerZpcd.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcd.setMaxLength(
					CommonConstant.LENGTH_ZIPCODE);
				// end defect 10188 

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerZpcd;
	}

	/**
	 * Return the ivjtxtOwnerZpcdP4 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOwnerZpcdP4()
	{
		if (ivjtxtOwnerZpcdP4 == null)
		{
			try
			{
				ivjtxtOwnerZpcdP4 = new RTSInputField();
				ivjtxtOwnerZpcdP4.setName("ivjtxtOwnerZpcdP4");
				ivjtxtOwnerZpcdP4.setManagingFocus(false);
				ivjtxtOwnerZpcdP4.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerZpcdP4.setBounds(298, 273, 37, 20);

				// defect 10188 
				//ivjtxtOwnerZpcdP4.setInput(1);
				//ivjtxtOwnerZpcdP4.setMaxLength(4); 
				ivjtxtOwnerZpcdP4.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerZpcdP4.setMaxLength(
					CommonConstant.LENGTH_ZIP_PLUS_4);
				// end defect 10188 

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerZpcdP4;
	}

	/**
	 * Return the txtOwnerId property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOwnerId()
	{
		if (ivjtxtOwnerId == null)
		{
			try
			{
				ivjtxtOwnerId = new RTSInputField();
				ivjtxtOwnerId.setName("txtOwnerId");
				ivjtxtOwnerId.setManagingFocus(false);
				ivjtxtOwnerId.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerId.setBounds(30, 47, 99, 20);

				// defect 10188 
				//ivjtxtOwnerId.setInput(1);
				//ivjtxtOwnerId.setMaxLength(9); 
				ivjtxtOwnerId.setInput(RTSInputField.NUMERIC_ONLY);
				ivjtxtOwnerId.setMaxLength(
					CommonConstant.LENGTH_OWNERID);
				// end defect 10188

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerId;
	}

	/**
	 * Return the txtOwnerName1 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOwnerName1()
	{
		if (ivjtxtOwnerName1 == null)
		{
			try
			{
				ivjtxtOwnerName1 = new RTSInputField();
				ivjtxtOwnerName1.setName("txtOwnerName1");
				ivjtxtOwnerName1.setManagingFocus(false);
				ivjtxtOwnerName1.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName1.setBounds(30, 112, 303, 20);

				// defect 10188 
				//ivjtxtOwnerName1.setInput(-1);
				//ivjtxtOwnerName1.setMaxLength(30);
				ivjtxtOwnerName1.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName1.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// end defect 10188 

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerName1;
	}
	/**
	 * Return the txtOwnerName2 property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtOwnerName2()
	{
		if (ivjtxtOwnerName2 == null)
		{
			try
			{
				ivjtxtOwnerName2 = new RTSInputField();
				ivjtxtOwnerName2.setName("txtOwnerName2");
				ivjtxtOwnerName2.setManagingFocus(false);
				ivjtxtOwnerName2.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtOwnerName2.setBounds(30, 144, 303, 20);

				// defect 10188 
				//ivjtxtOwnerName2.setInput(-1);
				//ivjtxtOwnerName2.setMaxLength(30);
				ivjtxtOwnerName2.setInput(RTSInputField.DEFAULT);
				ivjtxtOwnerName2.setMaxLength(
					CommonConstant.LENGTH_NAME);
				// end defect 10188 

				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjExc);
			}
		}
		return ivjtxtOwnerName2;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7894
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7894
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
			setName("FrmOwnerRegistrationREG032");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(426, 379);
			setTitle(TITLE_REG032);
			setContentPane(getFrmOwnerRegistrationREG032ContentPane1());
		}
		catch (Throwable aeIvjExc)
		{
			handleException(aeIvjExc);
		}
		// user code begin {2}
		// defect 10127 
		caOwnrNameAddrComp =
			new NameAddressComponent(
				gettxtOwnerName1(),
				gettxtOwnerName2(),
				gettxtOwnerStreet1(),
				gettxtOwnerStreet2(),
				gettxtOwnerCity(),
				gettxtOwnerState(),
				gettxtOwnerZpcd(),
				gettxtOwnerZpcdP4(),
				null,
				null,
				null,
				getstcLblDash(),
				ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
				ErrorsConstant.ERR_NUM_INCOMPLETE_OWNR_DATA,
				CommonConstant.TX_DEFAULT_STATE);
		// end defect 10127 
		// user code end
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmOwnerRegistrationREG032 laFrmOwnerRegistrationREG032;
			laFrmOwnerRegistrationREG032 =
				new FrmOwnerRegistrationREG032();
			laFrmOwnerRegistrationREG032.setModal(true);
			laFrmOwnerRegistrationREG032
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmOwnerRegistrationREG032.show();
			java.awt.Insets laInsets =
				laFrmOwnerRegistrationREG032.getInsets();
			laFrmOwnerRegistrationREG032.setSize(
				laFrmOwnerRegistrationREG032.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmOwnerRegistrationREG032.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			// defect 7894
			//aFrmOwnerRegistrationREG032.setVisible(true);
			laFrmOwnerRegistrationREG032.setVisibleRTS(true);
			//end 7894
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * all subclasses must implement this method.It sets the data on the
	 * screen and is how the controller relays information to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// defect 10127 
		caOwnrNameAddrComp.setNameAddressDataToDisplay(new OwnerData());
		// end defect 10127 

		// defect 10130 
		getstcLblOwnerIDNo().setEnabled(SystemProperty.isHQ());
		gettxtOwnerId().setEnabled(SystemProperty.isHQ());
		setDefaultFocusField(gettxtOwnerName1());
		//end defect 10130  
	}

	/**
	 * Validate the input fields on the form.
	 * 
	 * @return boolean true if all fields are valid, false otherwise
	 */
	private boolean validateInput()
	{
		RTSException leRTSEx = new RTSException();

		// defect 10188 
		if (!gettxtOwnerId().isValidOwnerId())
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtOwnerId());
		}
		// end defect 10188 

		// defect 10127
		boolean lbValid = true;
		caOwnrNameAddrComp.validateNameAddressFields(leRTSEx);

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
		// end defect 10127
	}
}
