package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientUtilityMethods;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.DisabledPlacardCustomerIdTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Point;

/*
 * FrmAddNewDisabledPlacardMRG025.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/09/2008	Do not enable/select Charge Fee for Temp 
 * 							 Placard if HQ.
 * 							modify setData()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	01/05/2009	Applicant w/ Disabled Vet Plate is not 
 * 							restricted to just one placard
 * 							modify setData()
 * 							defect 9884 Ver Defect_POS_D
 * K Harrell	07/26/2009	Implement HB 3095 - No longer consider 
 * 							Mobility. Use only AcctItmCd of PDC / TDC.
 * 							modify setData()  
 * 							defect 10133 Ver Defect_POS_F 
 * K Harrell	05/28/2011	On add TDC, if another exists, match ExpDate
 * 							Show message; Show msg if not default.
 * 							add cbShowSynchTempPlacard, 
 * 							  SYNCH_TEMP_MSG_PREFIX
 * 							add getTmpPlcrdSynchMsg()  
 * 							modify setData(), actionPerformed()  
 * 							defect 10831  Ver 6.8.0
 * K Harrell	10/10/2011	Allow up to 5 Placards if Disabled Vet or 
 * 							Institution.
 * 							add ivjtxtNumPlacards,ivjstclblNumPlacards,
 * 							  get methods
 * 							add validateData() 
 *							modify actionPerformed(), getJPanel(), 
 *							  setData(), setDataToDataObject()  
 * 							defect 11050 Ver 6.9.0  
 * K Harrell	12/05/2011	Add check for Disabled Plate to determine if 
 * 							should enable Two Placard Checkbox 
 * 							modify setData() 
 * 							defect 11163 Ver 6.9.0 
 * ---------------------------------------------------------------------
 */

/**
 * Frame for Add New Disabled Placard.
 * 
 * @version 6.9.0 	12/05/2011	
 * @author Kathy Harrell
 * @since 			10/21/2008
 */

public class FrmAddNewDisabledPlacardMRG025 extends RTSDialogBox
		implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;

	private JCheckBox ivjchkChargeFee = null;

	private JCheckBox ivjchkTwoPlacards = null;

	private JLabel ivjlblAddress1 = null;

	private JLabel ivjlblAddress2 = null;

	private JLabel ivjlblAddress3 = null;

	private JLabel ivjlblApplicantId = null;

	private JLabel ivjlblApplicantName = null;

	private JLabel ivjlblExpirationDate = null;

	private JLabel ivjlblIdDesc = null;

	private JLabel ivjlblIssueDate = null;

	private JLabel ivjlblPlacardDescription = null;

	private JLabel ivjlblPlacardType = null;

	private JLabel ivjstclblExpirationDate = null;

	private JLabel ivjstclblIssueDate = null;

	private JPanel jPanel = null;

	private JPanel jPanel1 = null;

	// boolean
	// defect 10831
	private boolean cbShowSynchTempPlacard = false;

	private static final String SYNCH_TEMP_MSG_PREFIX = "THE TEMPORARY PLACARD EXPIRATION WILL BE ";
	// end defect 10831

	// Object
	private DisabledPlacardCustomerData caDPCustData = null;

	// Constant
	private final static String ERRMSG_DATA_MISS = "Data missing for IsNextVCREG029";

	private final static String ERRMSG_ERROR = "ERROR";

	private final static String FRM_TITLE = "Add New Disabled Placard     MRG025";

	// defect 11050
	private RTSInputField ivjtxtNumPlacards = null;
	private JLabel ivjstclblNumPlacards = null;
	// end defect 11050

	/**
	 * FrmAddNewDisabledPlacardMRG025 constructor comment.
	 */
	public FrmAddNewDisabledPlacardMRG025()
	{
		super();
		initialize();
	}

	/**
	 * FrmAddNewDisabledPlacardMRG025 constructor comment.
	 * 
	 * @param aaOwner
	 *            Dialog
	 */
	public FrmAddNewDisabledPlacardMRG025(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmAddNewDisabledPlacardMRG025 constructor comment.
	 * 
	 * @param aaOwner
	 *            Frame
	 */
	public FrmAddNewDisabledPlacardMRG025(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE
	 *            ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			// defect 11050 
			clearAllColor(this);
			// end defect 11050 
			
			// ENTER
			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				// defect 11050 
				if (validateData())
				{
					setDataToDataObject();

					// defect 10831
					// Show message to denote that Temp Disabled Placard
					// Exp Mo/Yr has been synchronized with the existing
					// Placard vs. issued w/ standard 6 months.
					if (cbShowSynchTempPlacard)
					{
						new RTSException(RTSException.INFORMATION_MESSAGE,
								getTmpPlcrdSynchMsg(), "Information", true)
						.displayError(this);
					}
					// end defect 10831

					CompleteTransactionData laData = MiscellaneousRegClientUtilityMethods
					.getDsabldPlcrdComplTransData(caDPCustData);

					getController().processData(
							AbstractViewController.ENTER, laData);
				}
				// end defect 11050
			}
			// CANCEL
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				getController().processData(
						AbstractViewController.CANCEL, null);
			}
			// HELP
			else if (aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSException leRTSEx = new RTSException(
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
				ivjButtonPanel1.setSize(216, 36);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(new java.awt.Dimension(
						217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
				ivjButtonPanel1.setLocation(147, 276);
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
	 * This method initializes ivjchkChargeFee
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkChargeFee()
	{
		if (ivjchkChargeFee == null)
		{
			ivjchkChargeFee = new JCheckBox();
			ivjchkChargeFee.setSize(89, 20);
			ivjchkChargeFee
					.setText(MiscellaneousRegConstant.CHARGE_FEE);
			ivjchkChargeFee.setLocation(202, 241);
			ivjchkChargeFee.setMnemonic(KeyEvent.VK_F);
		}
		return ivjchkChargeFee;
	}

	/**
	 * This method initializes ivjchkTwoPlacards
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkTwoPlacards()
	{
		if (ivjchkTwoPlacards == null)
		{
			ivjchkTwoPlacards = new JCheckBox();
			ivjchkTwoPlacards.setSize(137, 20);
			ivjchkTwoPlacards.setLocation(202, 219);
			ivjchkTwoPlacards
					.setText(MiscellaneousRegConstant.ISSUE_TWO_PLACARDS);
			ivjchkTwoPlacards.setMnemonic(KeyEvent.VK_I);
		}
		return ivjchkTwoPlacards;
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getlblIssueDate(), null);
			jPanel.add(getlblExpirationDate(), null);
			jPanel.add(getlblPlacardDescription(), null);
			jPanel.add(getchkTwoPlacards(), null);
			jPanel.add(getchkChargeFee(), null);
			jPanel.add(getButtonPanel1(), null);
			jPanel.add(getstclblIssueDate(), null);
			jPanel.add(getstclblExpirationDate(), null);
			jPanel.add(getlblPlacardType(), null);
			jPanel.add(getJPanel1(), null);
			// defect 11050
			jPanel.add(gettxtNumPlacards(), null);
			jPanel.add(getstclblNumPlacards(), null);
			// end defect 11050 
		}
		return jPanel;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel1()
	{
		if (jPanel1 == null)
		{
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.add(getlblApplicantId(), null);
			jPanel1.add(getlblApplicantName(), null);
			jPanel1.add(getlblAddress1(), null);
			jPanel1.add(getlblAddress2(), null);
			jPanel1.add(getlblAddress3(), null);
			jPanel1.add(getlblIdDesc(), null);
			jPanel1.setBounds(10, 9, 505, 105);
			Border b = new TitledBorder(new EtchedBorder(), "");
			jPanel1.setBorder(b);
		}
		return jPanel1;
	}

	/**
	 * This method initializes ivjlblAddress1
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAddress1()
	{
		if (ivjlblAddress1 == null)
		{
			ivjlblAddress1 = new JLabel();
			ivjlblAddress1.setSize(240, 20);
			ivjlblAddress1.setLocation(26, 34);
		}
		return ivjlblAddress1;
	}

	/**
	 * This method initializes ivjlblAddress2
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAddress2()
	{
		if (ivjlblAddress2 == null)
		{
			ivjlblAddress2 = new JLabel();
			ivjlblAddress2.setSize(240, 20);
			ivjlblAddress2.setLocation(26, 54);
		}
		return ivjlblAddress2;
	}

	/**
	 * This method initializes ivjlblAddress3
	 * 
	 * @return JLabel
	 */
	private JLabel getlblAddress3()
	{
		if (ivjlblAddress3 == null)
		{
			ivjlblAddress3 = new JLabel();
			ivjlblAddress3.setSize(240, 20);
			ivjlblAddress3.setLocation(26, 74);
		}
		return ivjlblAddress3;
	}

	/**
	 * This method initializes ivjlblApplicantName
	 * 
	 * @return JLabel
	 */
	private JLabel getlblApplicantId()
	{
		if (ivjlblApplicantId == null)
		{
			ivjlblApplicantId = new JLabel();
			ivjlblApplicantId.setBounds(271, 34, 230, 20);
		}
		return ivjlblApplicantId;
	}

	/**
	 * This method initializes ivjlblApplicantName
	 * 
	 * @return JLabel
	 */
	private JLabel getlblApplicantName()
	{
		if (ivjlblApplicantName == null)
		{
			ivjlblApplicantName = new JLabel();
			ivjlblApplicantName.setSize(473, 20);
			ivjlblApplicantName.setLocation(26, 14);
		}
		return ivjlblApplicantName;
	}

	/**
	 * This method initializes ivjlblExpirationDate
	 * 
	 * @return JLabel
	 */
	private JLabel getlblExpirationDate()
	{
		if (ivjlblExpirationDate == null)
		{
			ivjlblExpirationDate = new JLabel();
			ivjlblExpirationDate.setSize(89, 20);
			ivjlblExpirationDate.setLocation(202, 185);
		}
		return ivjlblExpirationDate;
	}

	/**
	 * This method initializes ivjlblIdDesc
	 * 
	 * @return JLabel
	 */
	private JLabel getlblIdDesc()
	{
		if (ivjlblIdDesc == null)
		{
			ivjlblIdDesc = new JLabel();
			ivjlblIdDesc.setBounds(271, 54, 230, 20);
			ivjlblIdDesc.setText("");
		}
		return ivjlblIdDesc;
	}

	/**
	 * This method initializes ivjlblIssueDate
	 * 
	 * @return JLabel
	 */
	private JLabel getlblIssueDate()
	{
		if (ivjlblIssueDate == null)
		{
			ivjlblIssueDate = new JLabel();
			ivjlblIssueDate.setSize(63, 20);
			ivjlblIssueDate.setLocation(202, 155);
		}
		return ivjlblIssueDate;
	}

	/**
	 * This method initializes ivjlblPlacardDescription
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlacardDescription()
	{
		if (ivjlblPlacardDescription == null)
		{
			ivjlblPlacardDescription = new JLabel();
			ivjlblPlacardDescription.setSize(196, 20);
			ivjlblPlacardDescription.setLocation(202, 127);
		}
		return ivjlblPlacardDescription;
	}

	/**
	 * This method initializes ivjlblPlacardType
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlacardType()
	{
		if (ivjlblPlacardType == null)
		{
			ivjlblPlacardType = new JLabel();
			ivjlblPlacardType.setSize(79, 20);
			ivjlblPlacardType
					.setText(MiscellaneousRegConstant.PLACARD_DESC);
			ivjlblPlacardType.setLocation(113, 127);
		}
		return ivjlblPlacardType;
	}

	/**
	 * This method initializes ivjstclblExpirationDate
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblExpirationDate()
	{
		if (ivjstclblExpirationDate == null)
		{
			ivjstclblExpirationDate = new JLabel();
			ivjstclblExpirationDate.setSize(89, 20);
			ivjstclblExpirationDate
					.setText(MiscellaneousRegConstant.EXPIRATION_DATE);
			ivjstclblExpirationDate.setLocation(103, 185);
		}
		return ivjstclblExpirationDate;
	}

	/**
	 * This method initializes ivjstclblIssueDate
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblIssueDate()
	{
		if (ivjstclblIssueDate == null)
		{
			ivjstclblIssueDate = new JLabel();
			ivjstclblIssueDate.setSize(63, 20);
			ivjstclblIssueDate
					.setText(MiscellaneousRegConstant.ISSUE_DATE);
			ivjstclblIssueDate.setLocation(129, 155);
		}
		return ivjstclblIssueDate;
	}
	
	/**
	 * This method initializes ivjstclblNumPlacards
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblNumPlacards()
	{
		if (ivjstclblNumPlacards == null )
		{
			ivjstclblNumPlacards = new JLabel();
			ivjstclblNumPlacards.setText("Number of Placards:");
			ivjstclblNumPlacards.setLocation(new Point(17, 217));
			ivjstclblNumPlacards.setSize(new Dimension(175, 20));
			ivjstclblNumPlacards
			.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		}
		return ivjstclblNumPlacards;
	}

	/**
	 * Return Synch Message for Temp Disabled Placard
	 * 
	 * @return String
	 */
	private String getTmpPlcrdSynchMsg()
	{
		return SYNCH_TEMP_MSG_PREFIX
				+ "<b><font color=#ff0000>"
				+ RTSDate.getMonthName(caDPCustData.getRTSExpMo() - 1)
						.toUpperCase() + " "
				+ caDPCustData.getRTSExpYr() + "." + "</font>";
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception
	 *            Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		RTSException leRTSEx = new RTSException(
				RTSException.JAVA_ERROR, (Exception) aeThrowable);
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
			this.setContentPane(getJPanel());
			setName("FrmAssignedDisabledPlacardsMRG023");
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setSize(530, 352);
			setTitle(FRM_TITLE);
		}
		catch (Throwable aeThrowable)
		{
			handleException(aeThrowable);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on the
	 * screen and is how the controller relays information to the view
	 * 
	 * @param aaData
	 */
	public void setData(Object aaData)
	{
		if (aaData != null)
		{
			if (aaData instanceof Vector)
			{
				Vector lvIsNextVC = (Vector) aaData;
				if (lvIsNextVC != null)
				{
					if (lvIsNextVC.size() == 2)
					{
						if (lvIsNextVC.get(0) instanceof String)
						{
							getController()
									.processData(
											VCAddNewDisabledPlacardMRG025.REDIRECT,
											lvIsNextVC);
						}
					}
					else
					{
						new RTSException(RTSException.FAILURE_MESSAGE,
								ERRMSG_DATA_MISS, ERRMSG_ERROR)
								.displayError(this);
					}
				}
			}
			else if (aaData instanceof DisabledPlacardCustomerData)
			{
				caDPCustData = (DisabledPlacardCustomerData) UtilityMethods
						.copy(aaData);
				String lsName = "";
				String lsIdTypeDesc = "";

				getlblApplicantId().setText(caDPCustData.getCustId());
				int liIdTypeCd = caDPCustData.getCustIdTypeCd();
				DisabledPlacardCustomerIdTypeData laDPAIdTypeData = DisabledPlacardCustomerIdTypeCache
						.getDsabldPlcrdCustIdType(liIdTypeCd);

				if (laDPAIdTypeData != null)
				{
					lsIdTypeDesc = laDPAIdTypeData.getCustIdTypeDesc()
							.toUpperCase();
				}
				getlblIdDesc().setText(lsIdTypeDesc);

				if (caDPCustData.isInstitution())
				{
					lsName = caDPCustData.getInstName();
				}
				else
				{
					lsName = caDPCustData.getOwnerName(true);
				}

				AddressData laAddrData = (AddressData) caDPCustData
						.getAddressData();

				getlblApplicantName().setText(lsName);

				// Get Vector of String of formatted Address Data
				Vector lvData = laAddrData.getAddressVector();
				getlblAddress1().setText((String) lvData.elementAt(0));
				getlblAddress2().setText((String) lvData.elementAt(1));
				getlblAddress3().setText((String) lvData.elementAt(2));

				// defect 10133
				// AcctItmCd = ItmCd = TransCd
				String lsAcctItmCd = caDPCustData.getAcctItmCd();
				caDPCustData.setTransCd(lsAcctItmCd);
				caDPCustData.setItmCd(lsAcctItmCd);

				AccountCodesData laAcctCdData = AccountCodesCache
						.getAcctCd(lsAcctItmCd, new RTSDate()
								.getYYYYMMDDDate());
				// end defect 10133

				getlblPlacardDescription().setText(
						laAcctCdData.getAcctItmCdDesc());

				// defect 10831
				// Return Hashtable to denote if:
				// - Has Active
				// - Max Active ExpDate if Temp & Has Active

				Hashtable lhtActive = caDPCustData
						.hasActivePlacard(caDPCustData.isTempDsabld());

				boolean lbHasActive = ((Boolean) lhtActive
						.get(MiscellaneousRegConstant.DSABLD_PLCRD_HASACTIVE))
						.booleanValue();

				// CalcDate
				int liNoMonths = laAcctCdData.getPrmtValidtyPeriod() / 30;

				RTSDate laEffDate = new RTSDate();
				caDPCustData.setRTSEffDate(laEffDate.getYYYYMMDDDate());

				RTSDate laExpDate = laEffDate.add(Calendar.MONTH,
						liNoMonths);

				if (caDPCustData.isTempDsabld() & lbHasActive)
				{
					int liMaxExpDate = ((Integer) lhtActive
							.get(MiscellaneousRegConstant.DSABLD_PLCRD_MAXACTIVE_EXPDATE))
							.intValue();

					// Only display message if different
					if (laExpDate.getYear() * 10000
							+ laExpDate.getMonth() * 100 + 1 > liMaxExpDate)
					{
						laExpDate = new RTSDate(RTSDate.YYYYMMDD,
								liMaxExpDate);

						cbShowSynchTempPlacard = true;
					}
				}
				// end defect 10831

				caDPCustData.setRTSExpMo(laExpDate.getMonth());
				caDPCustData.setRTSExpYr(laExpDate.getYear());
				getlblIssueDate().setText(laEffDate.toString());
				getlblExpirationDate().setText(laExpDate.getMMYYYY());

				boolean lbCharge = caDPCustData.isTempDsabld()
						&& !SystemProperty.isHQ();

				getchkChargeFee().setEnabled(lbCharge);
				getchkChargeFee().setSelected(lbCharge);

				// defect 11050
				// Provide input field
				// defect 10831
				// Use boolean lbHasActive vs.
				// caDPCustData.hasActivePlacard()
				// No longer returns boolean

				// defect 10133
				// Implement new hasActivePlacard()
				// defect 9884
				// Applicant w/ Disabled Vet Plate is not restricted to
				// just one placard
				// Use isDsabldPltIndi() vs. isDsabldPltOwnr()
				boolean lbInputPlcrdNo = caDPCustData.isInstitution()
						|| caDPCustData.isDsabldVetPltIndi();

				// defect 11163 
				// Include check for Disabled Plate 
				//boolean lbTwoPlacards = !lbInputPlcrdNo && !lbHasActive;
				boolean lbTwoPlacards = !lbInputPlcrdNo && !lbHasActive && !caDPCustData.isDsabldPltIndi();
				// end defect 11163 
				// end defect 9884
				// end defect 10133
				// end defect 10831
				getchkTwoPlacards().setVisible(!lbInputPlcrdNo);
				getchkTwoPlacards().setEnabled(lbTwoPlacards);
				ivjstclblNumPlacards.setVisible(lbInputPlcrdNo);
				gettxtNumPlacards().setVisible(lbInputPlcrdNo);
				gettxtNumPlacards().setEnabled(lbInputPlcrdNo);
				// end defect 11050 
			}
		}
	}

	/**
	 * Assign values to Disabled Placard Data Object
	 */
	private void setDataToDataObject()
	{
		caDPCustData.setChrgFeeIndi(getchkChargeFee().isSelected() ? 1
				: 0);

		// defect 11050
		// caDPCustData.setIssueTwoPlacards(getchkTwoPlacards()
		//	 .isSelected() ? 1 : 0);

		int liNumPlcrds = 0;

		if (gettxtNumPlacards().isEnabled())
		{
			liNumPlcrds = Integer.parseInt(gettxtNumPlacards()
					.getText());
		}
		else
		{
			liNumPlcrds = 1 + (getchkTwoPlacards().isSelected() ? 1 : 0);
		}
		caDPCustData.setNumPlacardsIssued(liNumPlcrds);
		// end defect 11050 

		Vector lvVector = new Vector();

		for (int i = 0; i < liNumPlcrds; i++)
		{
			DisabledPlacardData laDPData = new DisabledPlacardData(
					caDPCustData);
			laDPData
					.setTransTypeCd(MiscellaneousRegConstant.DP_ADD_TRANS_TYPE_CD);
			lvVector.addElement(laDPData);
		}
		caDPCustData.setDsabldPlcrd(lvVector);
	}

	/**
	 * This method initializes ivjtxtNumPlacards
	 * 
	 * @return javax.swing.JTextField
	 */
	private RTSInputField gettxtNumPlacards()
	{
		if (ivjtxtNumPlacards == null)
		{
			ivjtxtNumPlacards = new RTSInputField();
			ivjtxtNumPlacards
					.setBounds(new Rectangle(202, 217, 22, 20));
			ivjtxtNumPlacards.setText("");
			ivjtxtNumPlacards.setMaxLength(1);
			ivjtxtNumPlacards.setInput(RTSInputField.NUMERIC_ONLY);
		}
		return ivjtxtNumPlacards;
	}
	/** 
	 * Validate data
	 *  
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbValid = true; 
		
		RTSException leRTSEx = new RTSException();
		
		if (gettxtNumPlacards().isEnabled())
		{
			if (gettxtNumPlacards().isEmpty())
			{
				leRTSEx.addException(
						new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtNumPlacards());
			}
			else
			{
				int liNum = Integer.parseInt(gettxtNumPlacards().getText());
				
				int liMax = SystemProperty.getMaxAddDsabldPlcrds(); 
				
				if (liNum <1 || liNum >liMax)
				{
					String lsAppend = ""+ liMax; 

					RTSException leRTSMaxEx =
						new RTSException(
							ErrorsConstant.ERR_NUM_NUM_PLACARDS_OUTSIDE_RANGE,
							new String[] { lsAppend });

					leRTSEx.addException(leRTSMaxEx,gettxtNumPlacards());
				}
			}
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid=false; 
		}
		return lbValid; 
	}
} // @jve:visual-info decl-index=0 visual-constraint="26,-21"
