package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.miscreg.business.MiscellaneousRegClientUtilityMethods;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.DisabledPlacardCustomerIdTypeCache;
import com.txdot.isd.rts.services.cache.DisabledPlacardDeleteReasonCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousRegConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmDeleteDisabledPlacardMRG024.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created.
 * 							Defect 9831 Ver Defect_POS_B
 * K Harrell	07/31/2009	resetAcctItmCd for delete
 * 							modify setData() 
 * 							defect 10133 Ver Defect_POS_F
 * K Harrell	08/07/2009	moved resetAcctItmCd AFTER set description
 * 							on screen. 
 * 							modify setData()
 * 							defect 10133 Ver Defect_POS_F
 * K Harrell	06/15/2010	Issues w/ tabbing from Combo Box.  Add
 * 							   comboBoxHotKeyFix()
 * 							modify setData() 
 *							defect 10498 Ver 6.5.0   
 * ---------------------------------------------------------------------
 */

/**
 * Delete Disabled PersonOld Placard Frame
 *
 * @version	6.5.0 		 	06/15/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		10/21/2008 
 */
public class FrmDeleteDisabledPlacardMRG024
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JComboBox ivjcomboReason = null;
	private JLabel ivjlblAddress1 = null;
	private JLabel ivjlblAddress2 = null;
	private JLabel ivjlblAddress3 = null;
	private JLabel ivjlblCustId = null;
	private JLabel ivjlblCustName = null;
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

	private DisabledPlacardCustomerData caDPCustData = null;
	private DisabledPlacardData caDPData = null;

	private Vector cvReason = new Vector();

	private final static String ERRMSG_DATA_MISS =
		"Data missing for IsNextVCREG029";
	private final static String ERRMSG_ERROR = "ERROR";

	/**
	 * 
	 * FrmDeleteDisabledPlacardMRG024 constructor comment.
	 */
	public FrmDeleteDisabledPlacardMRG024()
	{
		super();
		initialize();
	}

	/**
	 * FrmDeleteDisabledPlacardMRG024 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmDeleteDisabledPlacardMRG024(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmDeleteDisabledPlacardMRG024 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmDeleteDisabledPlacardMRG024(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
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
						"This record will be marked as deleted. "
							+ CommonConstant.TXT_CONTINUE_QUESTION,
						ScreenConstant.CTL001_FRM_TITLE);

				int liRetCode = leRTSEx.displayError(this);
				if (liRetCode == RTSException.YES)
				{
					setDataToDataObject();
					CompleteTransactionData laData =
						MiscellaneousRegClientUtilityMethods
							.getDsabldPlcrdComplTransData(
							caDPCustData);

					getController().processData(
						AbstractViewController.ENTER,
						laData);
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
				ivjButtonPanel1.setSize(216, 36);
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
				ivjButtonPanel1.setLocation(147, 294);
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
	 * This method initializes ivjcomboReason
	 * 
	 * @return JComboBox
	 */
	private JComboBox getcomboReason()
	{
		if (ivjcomboReason == null)
		{
			ivjcomboReason = new JComboBox();
			ivjcomboReason.setSize(200, 25);
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
			jPanel1.add(getlblCustId(), null);
			jPanel1.add(getlblCustName(), null);
			jPanel1.add(getlblAddress1(), null);
			jPanel1.add(getlblAddress2(), null);
			jPanel1.add(getlblAddress3(), null);
			jPanel1.add(getlblIdDesc(), null);
			jPanel1.setBounds(10, 9, 510, 105);
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
	 * This method initializes ivjlblCustId
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCustId()
	{
		if (ivjlblCustId == null)
		{
			ivjlblCustId = new JLabel();
			ivjlblCustId.setBounds(276, 34, 230, 20);
		}
		return ivjlblCustId;
	}

	/**
	 * This method initializes ivjlblCustName
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCustName()
	{
		if (ivjlblCustName == null)
		{
			ivjlblCustName = new JLabel();
			ivjlblCustName.setSize(480, 20);
			ivjlblCustName.setLocation(26, 14);
		}
		return ivjlblCustName;
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
			ivjlblIdDesc.setText("");
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
			ivjlblPlacardNo.setText("");
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
			ivjlblPlacardType.setText("Placard Desc:");
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
			ivjstclblExpirationDate.setText("Expiration Date:");
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
			ivjstclblIssueDate.setText("Issue Date:");
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
			ivjstclblPlacardNo.setText("Placard No:");
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
			ivjstcLblReason.setSize(85, 22);
			ivjstcLblReason.setText("Delete Reason:");
			ivjstcLblReason.setLocation(119, 255);
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
			setName("FrmDeleteDisabledPlacardMRG024");
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			setSize(530, 368);
			setTitle("Delete Disabled Placard      MRG024");
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
					.getDsabldPlcrdDelReasnForDel());

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
				((String) (cvReason.elementAt(i)))
					.substring(1, 30)
					.trim();
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
						return;
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

				getlblCustId().setText(caDPCustData.getCustId());
				int liIdTypeCd = caDPCustData.getCustIdTypeCd();
				DisabledPlacardCustomerIdTypeData laDPAIdTypeData =
					DisabledPlacardCustomerIdTypeCache
						.getDsabldPlcrdCustIdType(
						liIdTypeCd);

				if (laDPAIdTypeData != null)
				{
					lsIdTypeDesc =
						laDPAIdTypeData
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

				getlblCustName().setText(lsName);

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

				AccountCodesData laAcctCdData =
					AccountCodesCache.getAcctCd(
						caDPData.getAcctItmCd(),
						new RTSDate().getYYYYMMDDDate());
						
				getlblPlacardDescription().setText(
					laAcctCdData.getAcctItmCdDesc());

				// defect 10133 
				caDPData.resetAcctItmCdForHB3095();
				lsAcctItmCd = caDPData.getAcctItmCd();
				// "DEL" + AcctItmCd = TransCd for Delete    
				caDPCustData.setTransCd(
					MiscellaneousRegConstant.DELETE_TRANSCD_PREFIX
						+ lsAcctItmCd);
				caDPCustData.setItmCd(lsAcctItmCd);
				// end defect 10133 

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
	private void setDataToDataObject()
	{
		String lsReason =
			(String) cvReason.elementAt(
				getcomboReason().getSelectedIndex());
		lsReason = (lsReason.substring(35)).trim();
		caDPData.setDelReasnCd(Integer.parseInt(lsReason));
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="26,-21"
