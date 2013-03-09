package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com
	.txdot
	.isd
	.rts
	.client
	.miscreg
	.business
	.MiscellaneousRegClientUtilityMethods;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com
	.txdot
	.isd
	.rts
	.services
	.cache
	.DisabledPlacardCustomerIdTypeCache;
import com
	.txdot
	.isd
	.rts
	.services
	.cache
	.DisabledPlacardDeleteReasonCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com
	.txdot
	.isd
	.rts
	.services
	.util
	.constants
	.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmReplaceDisabledPlacardMRG026.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created.
 * 							Defect 9831 Ver Defect_POS_B
 * K Harrell	11/09/2008	Do not enable/select Charge Fee for Temp 
 * 							 Placard if HQ
 * 							modify setData()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	03/24/2009	Create copy of caDPCustData for 
 * 							processing in actionPerformed()
 * 							add setDataToDataObject(DisabledPlacardCustomerData)
 * 							delete setDataToDataObject()
 * 							modify actionPerformed() 
 * 							defect 9954 Ver Defect_POS_E
 * K Harrell	07/29/2009	resetAcctItmCd for replacement
 * 							modify setData() 
 * 							defect 10133 Ver Defect_POS_F 
 * K Harrell	04/24/2010	reset ItmCd for insert into RTS_DSABLD_PLCRD
 * 							modify setData()
 * 							defect 10468 Ver POS_640 
 * K Harrell	04/29/2010	set Delete Reason in DisabledPlacardData 
 * 							 in vector in passed Object 
 * 							modify setDataToDataObject()
 * 							defect 10468 Ver POS_640
 * K Harrell	06/15/2010	Issues w/ tabbing from Combo Box.  Add
 * 							   comboBoxHotKeyFix()
 * 							modify setData() 
 *							defect 10498 Ver 6.5.0
 * K Harrell	10/11/2011	modify setDataToDataObject()
 * 							defect 11050 Ver 6.9.0      
 * ---------------------------------------------------------------------
 */

/**
 * Replace Disabled PersonOld Placard Frame
 *
 * @version	6.9.0  			10/11/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		10/21/2008 
 */
public class FrmReplaceDisabledPlacardMRG026
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkChargeFee = null;
	private JComboBox ivjcomboReason = null;
	private JLabel ivjlblAddress1 = null;
	private JLabel ivjlblAddress2 = null;
	private JLabel ivjlblAddress3 = null;
	private JLabel ivjlblApplicantId = null;
	private JLabel ivjlblApplicantName = null;
	private JLabel ivjlblExpirationDate = null;
	private JLabel ivjlblIdDesc = null;
	private JLabel ivjlblIssueDate = null;
	private JLabel ivjlblPlacardDescription = null;
	private JLabel ivjlblPlacardNo = null;
	private JLabel ivjlblPlacardType = null;
	private JLabel ivjstclblExpirationDate = null;
	private JLabel ivjstclblIssueDate = null;
	private JLabel ivjstclblPlacardNo = null;
	private JLabel ivjstcLblReason = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;

	private Vector cvReason = new Vector();

	private DisabledPlacardCustomerData caDPCustData = null;
	private DisabledPlacardData caDPData = null;

	private final static String ERRMSG_DATA_MISS =
		"Data missing for IsNextVCREG029";
	private final static String ERRMSG_ERROR = "ERROR";
	private final static String FRM_TITLE =
		"Replace Disabled Placard      MRG026";
	private final static String REPLACE_MSG =
		"This record will be marked as deleted. "
			+ "New Inventory will be issued with the same "
			+ "Expiration Date. ";

	/**
	 * 
	 * FrmReplaceDisabledPlacardMRG026 constructor comment.
	 */
	public FrmReplaceDisabledPlacardMRG026()
	{
		super();
		initialize();
	}

	/**
	 * FrmReplaceDisabledPlacardMRG026 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmReplaceDisabledPlacardMRG026(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmReplaceDisabledPlacardMRG026 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmReplaceDisabledPlacardMRG026(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
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
			clearAllColor(this);

			if (aaAE.getSource() == ivjButtonPanel1.getBtnEnter())
			{
				if (getcomboReason().getSelectedIndex() == -1)
				{
					RTSException leEx = new RTSException();
					leEx.addException(
						new RTSException(150),
						getcomboReason());
					leEx.displayError(this);
					getcomboReason().requestFocus();
					return;
				}

				RTSException leRTSEx =
					new RTSException(
						RTSException.CTL001,
						REPLACE_MSG
							+ CommonConstant.TXT_CONTINUE_QUESTION,
						ScreenConstant.CTL001_FRM_TITLE);

				int liRetCode = leRTSEx.displayError(this);

				if (liRetCode == RTSException.YES)
				{
					// defect 9954 
					// Create copy to be modified 
					DisabledPlacardCustomerData laCopyDPCustData =
						(
							DisabledPlacardCustomerData) UtilityMethods
								.copy(
							caDPCustData);

					setDataToDataObject(laCopyDPCustData);

					CompleteTransactionData laData =
						MiscellaneousRegClientUtilityMethods
							.getDsabldPlcrdComplTransData(
							laCopyDPCustData);

					getController().processData(
						AbstractViewController.ENTER,
						laData);
					// end defect 9954 
				}
			}

			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
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
				ivjButtonPanel1.setBounds(147, 322, 216, 36);
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
	 * This method initializes ivjchkChargeFee
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkChargeFee()
	{
		if (ivjchkChargeFee == null)
		{
			ivjchkChargeFee = new javax.swing.JCheckBox();
			ivjchkChargeFee.setSize(89, 24);
			ivjchkChargeFee.setText(
				MiscellaneousRegConstant.CHARGE_FEE);
			ivjchkChargeFee.setMnemonic(KeyEvent.VK_F);
			ivjchkChargeFee.setLocation(214, 286);
		}
		return ivjchkChargeFee;
	}

	/**
	 * This method initializes ivjcomboReason
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboReason()
	{
		if (ivjcomboReason == null)
		{
			ivjcomboReason = new JComboBox();
			ivjcomboReason.setSize(196, 25);
			ivjcomboReason.setLocation(214, 252);
		}
		return ivjcomboReason;
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
			jPanel.add(getButtonPanel1(), null);
			jPanel.add(getstclblIssueDate(), null);
			jPanel.add(getstclblExpirationDate(), null);
			jPanel.add(getlblPlacardType(), null);
			jPanel.add(getJPanel1(), null);
			jPanel.add(getstclblPlacardNo(), null);
			jPanel.add(getlblPlacardNo(), null);
			jPanel.add(getcomboReason(), null);
			jPanel.add(getstcLblReason(), null);
			jPanel.add(getchkChargeFee(), null);
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
			jPanel1.setSize(505, 105);
			Border b = new TitledBorder(new EtchedBorder(), "");
			jPanel1.setBorder(b);
			jPanel1.setLocation(10, 9);
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
			ivjlblAddress1.setText("");
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
			ivjlblApplicantId.setBounds(276, 34, 230, 20);
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
			ivjlblExpirationDate.setSize(90, 20);
			ivjlblExpirationDate.setLocation(214, 225);
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
			ivjlblIdDesc.setSize(230, 20);
			ivjlblIdDesc.setLocation(276, 54);
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
			ivjlblIssueDate.setLocation(214, 195);
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
			ivjlblPlacardDescription.setLocation(214, 167);
		}
		return ivjlblPlacardDescription;
	}

	/**
	 * This method initializes ivjlblPlacardNo
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPlacardNo()
	{
		if (ivjlblPlacardNo == null)
		{
			ivjlblPlacardNo = new JLabel();
			ivjlblPlacardNo.setSize(167, 20);
			ivjlblPlacardNo.setLocation(214, 137);
		}
		return ivjlblPlacardNo;
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
			ivjlblPlacardType.setText(
				MiscellaneousRegConstant.PLACARD_DESC);
			ivjlblPlacardType.setLocation(125, 167);
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
			ivjstclblExpirationDate.setText(
				MiscellaneousRegConstant.EXPIRATION_DATE);
			ivjstclblExpirationDate.setLocation(115, 225);
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
			ivjstclblIssueDate.setText(
				MiscellaneousRegConstant.ISSUE_DATE);
			ivjstclblIssueDate.setLocation(141, 195);
		}
		return ivjstclblIssueDate;
	}

	/**
	 * This method initializes ivjstclblPlacardNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblPlacardNo()
	{
		if (ivjstclblPlacardNo == null)
		{
			ivjstclblPlacardNo = new JLabel();
			ivjstclblPlacardNo.setSize(65, 20);
			ivjstclblPlacardNo.setText(
				MiscellaneousRegConstant.PLACARD_NO);
			ivjstclblPlacardNo.setLocation(139, 137);
		}
		return ivjstclblPlacardNo;
	}

	/**
	 * This method initializes ivjstcLblReason
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblReason()
	{
		if (ivjstcLblReason == null)
		{
			ivjstcLblReason = new JLabel();
			ivjstcLblReason.setSize(95, 22);
			ivjstcLblReason.setText(
				MiscellaneousRegConstant.REPLACE_REASON);
			ivjstcLblReason.setLocation(109, 255);
		}
		return ivjstcLblReason;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception Throwable
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
			// user code end
			this.setContentPane(getJPanel());
			setName("FrmReplaceDisabledPlacardMRG024");
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setSize(530, 395);
			setTitle(FRM_TITLE);
		}
		catch (Throwable aeThrowable)
		{
			handleException(aeThrowable);
		}
	}

	/**
	 * Populate Drop Down Reasons 
	 */
	private void populateReasonType()
	{
		getcomboReason().removeActionListener(this);
		getcomboReason().removeAllItems();
		getcomboReason().setSelectedIndex(-1);
		Vector lvReason =
			(Vector) UtilityMethods.copy(
				DisabledPlacardDeleteReasonCache
					.getDsabldPlcrdDelReasnForRepl());

		DisabledPlacardDeleteReasonData laReasonData =
			new DisabledPlacardDeleteReasonData();

		for (int i = 0; i < lvReason.size(); i++)
		{
			laReasonData =
				(DisabledPlacardDeleteReasonData) lvReason.elementAt(i);

			cvReason.add(
				laReasonData.getDelReasnCd()
					+ UtilityMethods.addPaddingRight(
						laReasonData.getDelReasnDesc().toUpperCase(),
						35,
						" ")
					+ laReasonData.getDelReasnCd());
		}
		UtilityMethods.sort(cvReason);

		for (int i = 0; i < cvReason.size(); i++)
		{
			String lsElement =
				((String) (cvReason.elementAt(i))).substring(1, 30);
			getcomboReason().addItem(lsElement);
		}
		getcomboReason().setSelectedIndex(-1);
		getcomboReason().addActionListener(this);
	}

	/**
	 * Set Data 
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
							getController().processData(
								VCAddNewDisabledPlacardMRG025.REDIRECT,
								lvIsNextVC);
						}
					}
					else
					{
						new RTSException(
							RTSException.FAILURE_MESSAGE,
							ERRMSG_DATA_MISS,
							ERRMSG_ERROR).displayError(
							this);
					}
				}
			}
			else if (aaData instanceof DisabledPlacardCustomerData)
			{
				caDPCustData =
					(DisabledPlacardCustomerData) UtilityMethods.copy(
						aaData);
				String lsName = "";
				String lsAcctItmCd = "";
				String lsIdTypeDesc = "";

				getlblApplicantId().setText(caDPCustData.getCustId());
				int liIdTypeCd = caDPCustData.getCustIdTypeCd();

				DisabledPlacardCustomerIdTypeData laDPIdTypeData =
					DisabledPlacardCustomerIdTypeCache
						.getDsabldPlcrdCustIdType(
						liIdTypeCd);

				if (laDPIdTypeData != null)
				{
					lsIdTypeDesc =
						laDPIdTypeData
							.getCustIdTypeDesc()
							.toUpperCase();
				}
				getlblIdDesc().setText(lsIdTypeDesc);
				if (caDPCustData.isInstitution())
				{
					lsName = caDPCustData.getInstName();
				}
				else
				{
					lsName = caDPCustData.getOwnerName();
				}

				AddressData laAddrData =
					(AddressData) caDPCustData.getAddressData();

				getlblApplicantName().setText(lsName);

				// Get Vector of String of formatted Address Data  
				Vector lvData = laAddrData.getAddressVector();
				getlblAddress1().setText((String) lvData.elementAt(0));
				getlblAddress2().setText((String) lvData.elementAt(1));
				getlblAddress3().setText((String) lvData.elementAt(2));

				Vector lvDsabldPlcrd = caDPCustData.getDsabldPlcrd();

				caDPData =
					(DisabledPlacardData) lvDsabldPlcrd.elementAt(0);
				caDPData.setTransTypeCd(
					MiscellaneousRegConstant.DP_DEL_TRANS_TYPE_CD);
				getlblPlacardNo().setText(caDPData.getInvItmNo());

				lsAcctItmCd = caDPData.getAcctItmCd();

				AccountCodesData laAcctCdData =
					AccountCodesCache.getAcctCd(
						lsAcctItmCd,
						new RTSDate().getYYYYMMDDDate());
				getlblPlacardDescription().setText(
					laAcctCdData.getAcctItmCdDesc());

				// defect 10133 
				caDPData.resetAcctItmCdForHB3095();
				lsAcctItmCd = caDPData.getAcctItmCd();
				// "RPL" + AcctItmCd = TransCd for Replace  
				caDPCustData.setTransCd(
					MiscellaneousRegConstant.REPLACE_TRANSCD_PREFIX
						+ lsAcctItmCd);
				caDPCustData.setItmCd(lsAcctItmCd);
				// end defect 10133

				// defect 10468 
				caDPData.setInvItmCd(lsAcctItmCd);
				// end defect 10468  

				RTSDate laEffDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						caDPData.getRTSEffDate());
				int liMonth = caDPData.getRTSExpMo();
				int liYear = caDPData.getRTSExpYr();
				RTSDate laExpDate =
					new RTSDate(
						RTSDate.YYYYMMDD,
						liYear * 10000 + liMonth * 100 + 1);
				caDPCustData.setRTSEffDate(laEffDate.getYYYYMMDDDate());
				caDPCustData.setRTSExpMo(liMonth);
				caDPCustData.setRTSExpYr(liYear);

				getlblIssueDate().setText(laEffDate.toString());
				getlblExpirationDate().setText(laExpDate.getMMYYYY());

				// defect 10133 
				// Use Placard to determine if charge vs. Customer 
				boolean lbCharge =
					!caDPData.isPermanent() && !SystemProperty.isHQ();
				// end defect 10133 

				getchkChargeFee().setEnabled(lbCharge);
				getchkChargeFee().setSelected(lbCharge);

				populateReasonType();
				// defect 10498 
				comboBoxHotKeyFix(getcomboReason());
				// end defect 10498 
			}
		}
	}

	/**
	 *  Set Data to Data Object
	 */
	private void setDataToDataObject(DisabledPlacardCustomerData aaDPCustData)
	{
		aaDPCustData.setChrgFeeIndi(
			getchkChargeFee().isSelected() ? 1 : 0);
		String lsReason =
			(String) cvReason.elementAt(
				getcomboReason().getSelectedIndex());
		lsReason = (lsReason.substring(35)).trim();
		int liRTSEffDate = (new RTSDate()).getYYYYMMDDDate();
		aaDPCustData.setRTSEffDate(liRTSEffDate);
		// defect 11050 
		aaDPCustData.setNumPlacardsIssued(1);
		// end defect 11050

		// defect 10468
		// caDPData is no longer applicable; Use 1st element 
		//    in vector
		//caDPData.setDelReasnCd(Integer.parseInt(lsReason));
		Vector lvDPData = (Vector) aaDPCustData.getDsabldPlcrd();
		DisabledPlacardData laDelDPData =
			(DisabledPlacardData) lvDPData.elementAt(0);
		laDelDPData.setDelReasnCd(Integer.parseInt(lsReason));
		// end 10468 
		

		DisabledPlacardData laAddDPData =
			(DisabledPlacardData) UtilityMethods.copy(laDelDPData);
		laAddDPData.setDelReasnCd(0);
		laAddDPData.setTransTypeCd(
			MiscellaneousRegConstant.DP_ADD_TRANS_TYPE_CD);
		laAddDPData.setRTSEffDate(liRTSEffDate);
		lvDPData.add(laAddDPData);
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="26,-21"
