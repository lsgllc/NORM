package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.Collections;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.cache.RegistrationPlateStickerCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmHotCheckACC004.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs			05/13/2002	WS shouldn't have creditindi == 1, so that 
 * 							Credit button doesn't enable in PMT004 
 * 							CQU100003894
 * MAbs			05/29/2002 	Attempted to make checkbox smaller, but was 
 * 							unable to clear CQU100004128
 * MAbs			06/28/2002	Hiding dash
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 * 							make changes in actionPerformed().
 * B Arredondo	12/06/2002	Fixing Defect# 5147. Made changes to user 
 * 							help guide so had to make changes in 
 * 							actionPerformed().
 * K Harrell    02/13/2003  CQU100005242  Check for blank AcctItmCd
 * 							convertAccountToTable()
 * K Harrell	03/25/2004	JavaDoc Cleanup 
 * 							Ver 5.2.0
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed(), 
 * 							convertAccountToTable()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	06/12/2004  ClassToPlate, PlateToSticker Implementation	
 * 							modify convertAccountToTable()
 * 							defect 8218 Ver 5.2.3
 * K Harrell	07/27/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3  
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/17/2005	Remove keylistener from buttonPanel
 * 							components. 
 * 							modify getbuttonPanel(), keyPressed() 
 * 							defect 8240 Ver 5.2.3
 * K Harrell	12/02/2005	restore focusGained(), getchkCashRefund()
 * 							add'l work on focus
 * 							add cbErrorDisplayed,caPreviousComponent
 * 							modify focusGained(),keyPressed(),
 * 							  getJInternalFrameContentPane()
 * 							defect 7884 Ver 5.2.3  
 * B Hargrove 	12/19/2007 	Defect 3894 updated cache (AccountCodesCache)
 * 							in trying to turn off 'credit allowed indi' 
 * 							for windshied stickers (this was done so that 
 * 							credit button would be disabled on PMT004).
 * 							This causes no credit to be given for 
 * 							windshield stickers on receipts for this
 * 							workstation until RTS is re-started.
 * 							Do a 'deep copy' of object so that
 * 							cache is not updated, also do not reset
 * 							credit allowed indi.
 * 							(see also: FrmFeesDuePMT004.setData())
 * 							modify convertAccountToTable()
 * 							defect 6759 Ver Defect POS A
 * ---------------------------------------------------------------------
 */
/**
 * ACC004 is the hot check screen for all 3 hot check events
 * 
 * @version	Defect POS A	12/19/2007  
 * @author Michael Abernethy
 * <br>Creation Date:		06/11/2001 16:29:21	
 * 
 */
public class FrmHotCheckACC004
	extends RTSDialogBox
	implements ActionListener, FocusListener, KeyListener, ListSelectionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JLabel ivjlblTotalCredit = null;
	private JLabel ivjstcLblEnterAmount = null;
	private JLabel ivjstcLblTotalCredit = null;
	private JPanel ivjJInternalFrameContentPane = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSInputField ivjtxtEnterAmount = null;
	private RTSTable ivjtblHotCheck = null;
	private TMACC006 caTableModel;
	private java.awt.Component caPreviousComponent;
	private JCheckBox ivjchkCashRefund = null;

	// boolean
	private boolean cbErrorDisplayed = false;

	// int
	private int ciSelectedRow;

	// Object
	private AccountCodesData caRegAcctData;
	private MFVehicleData caMFVehicleData;

	// Vector 
	private Vector cvAccountCodes;
	private Vector cvTableData;

	private final static String DEFLT_AMT = "00000000.00";
	private final static String ENTER_AMT = "Enter Amount:";
	private final static String ERRMSG_CACHE = "Cache error!! \n";
	private final static String HOTCKREG = "HOTCKREG";
	private final static String HOTCKTTL = "HOTCKTTL";
	private final static String MSG_HOTCHK_ERR =
		"This event will correct a Hot Check Credit error.  Total "
			+ "correction requested is $ ";
	private final static String MSG_TOTAL_REQ =
		"Total credit request is ";
	private final static String MSG_CORRECT = ".  Is this correct?";
	private final static String TITLE_ACC004 = "Hot Check   ACC004";
	private final static String TOTAL_AMT = "Total Credit Amount:";
	private final static String ZERO_DOLLAR = "0.00";

	/**
	 * FrmHotCheck constructor comment.
	 */
	public FrmHotCheckACC004()
	{
		super();
		initialize();
	}
	/**
	 * Creates a ACC004 with the parent
	 * 
	 * @param aaParent	Dialog
	 */
	public FrmHotCheckACC004(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Creates a ACC004 with the parent
	 * 
	 * @param aaParent	JFrame 
	 */
	public FrmHotCheckACC004(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param  aaAE	ActionEvent 
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
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				if (!isValidEntry())
				{
					return;
				}
				// VALIDATION
				RTSException leRTSException = new RTSException();
				if (Double.parseDouble(getTotalCreditAmount()) <= 0)
				{
					leRTSException.addException(
						new RTSException(150),
						gettxtEnterAmount());
				}
				if (leRTSException.isValidationError())
				{
					leRTSException.displayError(this);
					leRTSException.getFirstComponent().requestFocus();
					return;
				}
				// Copy the MFVehData
				MFVehicleData laMFVehicleData =
					(MFVehicleData) UtilityMethods.copy(
						caMFVehicleData);
				boolean lbTitleSelected = false;
				boolean lbRegSelected = false;
				CompleteTransactionData laCompTransData =
					new CompleteTransactionData();
				RegFeesData laRegFeesData = new RegFeesData();
				Vector lvVector = new Vector();
				AccountCodesData laMatchingData = null;
				for (int i = 0; i < cvTableData.size(); i++)
				{
					laMatchingData = null;
					RefundTableData laRefundTableData =
						(RefundTableData) cvTableData.get(i);
					if (Double
						.parseDouble(
							laRefundTableData.getAmount().getValue())
						> 0)
					{
						for (int j = 0; j < cvAccountCodes.size(); j++)
						{
							AccountCodesData laAccountCodesData =
								(AccountCodesData) cvAccountCodes.get(
									j);
							if (laAccountCodesData
								.getAcctItmCdDesc()
								.equals(laRefundTableData.getType()))
							{
								laMatchingData = laAccountCodesData;
								break;
							}
						}
						// the data is from the mfVeh regclasscd lookup
						if (laMatchingData == null)
						{
							laMatchingData = caRegAcctData;
						}
						AccountCodesData laAccountCodesData =
							(AccountCodesData) cvAccountCodes.get(i);
						if (laAccountCodesData
							.getAcctItmCd()
							.trim()
							.toUpperCase()
							.equals(HOTCKREG)
							&& Double.parseDouble(
								laRefundTableData
									.getAmount()
									.getValue())
								> 0.0)
						{
							lbRegSelected = true;
						}
						if (laAccountCodesData
							.getAcctItmCd()
							.trim()
							.toUpperCase()
							.equals(HOTCKTTL)
							&& Double.parseDouble(
								laRefundTableData
									.getAmount()
									.getValue())
								> 0.0)
						{
							lbTitleSelected = true;
						}
						FeesData laFeesData = new FeesData();
						if (getController()
							.getTransCode()
							.equals(
								com
									.txdot
									.isd
									.rts
									.services
									.util
									.constants
									.TransCdConstant
									.CKREDM))
						{
							laFeesData.setAcctItmCd(
								laMatchingData.getRedemdAcctItmCd());
							laFeesData.setDesc(
								laRefundTableData.getRedeemType());
						}
						else
						{
							laFeesData.setAcctItmCd(
								laMatchingData.getAcctItmCd());
							laFeesData.setDesc(
								laMatchingData.getAcctItmCdDesc());
						}
						laFeesData.setCrdtAllowedIndi(
							laMatchingData.getCrdtAllowdIndi());
						laFeesData.setItemPrice(
							laRefundTableData.getAmount());
						laFeesData.setItmQty(1);
						lvVector.add(laFeesData);
					}
				}
				laRegFeesData.setVectFees(lvVector);
				laCompTransData.setRegFeesData(laRegFeesData);
				laCompTransData.setOrgVehicleInfo(caMFVehicleData);
				laCompTransData.setVehicleInfo(laMFVehicleData);
				laCompTransData.setTransCode(
					getController().getTransCode());
				laCompTransData.setOfcIssuanceNo(
					SystemProperty.getOfficeIssuanceNo());
				// Depending on what hot check was selected, do different things
				if (getController()
					.getTransCode()
					.equals(
						com
							.txdot
							.isd
							.rts
							.services
							.util
							.constants
							.TransCdConstant
							.HOTCK))
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.CTL001,
							MSG_TOTAL_REQ
								+ getTotalCreditAmount()
								+ MSG_CORRECT,
							"");
					int liResponse = leRTSEx.displayError(this);
					if (liResponse == RTSException.YES)
					{
						if (lbRegSelected)
						{
							laCompTransData
								.getVehicleInfo()
								.getRegData()
								.setRegHotCkIndi(
								1);
						}
						else
						{
							laCompTransData
								.getVehicleInfo()
								.getRegData()
								.setRegHotCkIndi(
								0);
						}
						if (lbTitleSelected)
						{
							laCompTransData
								.getVehicleInfo()
								.getTitleData()
								.setTtlHotCkIndi(
								1);
						}
						else
						{
							laCompTransData
								.getVehicleInfo()
								.getTitleData()
								.setTtlHotCkIndi(
								0);
						}
						laCompTransData
							.getVehicleInfo()
							.getRegData()
							.setPltSeizedIndi(
							0);
						laCompTransData
							.getVehicleInfo()
							.getRegData()
							.setStkrSeizdIndi(
							0);
						getController().processData(
							AbstractViewController.ENTER,
							laCompTransData);
					}
					else
					{
						gettblHotCheck().requestFocus();
						return;
					}
				}
				else if (
					getController().getTransCode().equals(
						com
							.txdot
							.isd
							.rts
							.services
							.util
							.constants
							.TransCdConstant
							.HOTDED))
				{
					RTSException leRTSEx =
						new RTSException(
							RTSException.CTL001,
							MSG_HOTCHK_ERR
								+ getTotalCreditAmount()
								+ MSG_CORRECT,
							"");
					int liResponse = leRTSEx.displayError(this);
					if (liResponse == RTSException.YES)
					{
						getController().processData(
							AbstractViewController.ENTER,
							laCompTransData);
					}
					else
					{
						gettblHotCheck().requestFocus();
						return;
					}
				}
				else if (
					getController().getTransCode().equals(
						com
							.txdot
							.isd
							.rts
							.services
							.util
							.constants
							.TransCdConstant
							.CKREDM))
				{
					laCompTransData
						.getVehicleInfo()
						.getRegData()
						.setRegHotCkIndi(
						0);
					laCompTransData
						.getVehicleInfo()
						.getTitleData()
						.setTtlHotCkIndi(
						0);
					laCompTransData
						.getVehicleInfo()
						.getRegData()
						.setPltSeizedIndi(
						0);
					laCompTransData
						.getVehicleInfo()
						.getRegData()
						.setStkrSeizdIndi(
						0);
					getController().processData(
						AbstractViewController.ENTER,
						laCompTransData);
				}
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caMFVehicleData);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				String lsTransCd = getController().getTransCode();
				if (lsTransCd.equals(TransCdConstant.HOTCK))
				{
					RTSHelp.displayHelp(RTSHelp.ACC004A);
				}
				if (lsTransCd.equals(TransCdConstant.CKREDM))
				{
					RTSHelp.displayHelp(RTSHelp.ACC004B);
				}
				if (lsTransCd.equals(TransCdConstant.HOTDED))
				{
					RTSHelp.displayHelp(RTSHelp.ACC004C);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Creates the table data
	 */
	private void convertAccountToTable()
	{
		if (cvTableData == null)
		{
			cvTableData = new Vector();
		}
		cvTableData.removeAllElements();
		AccountCodesData laDataFromCache = null;
		// From the accountcodesdata vector, create table entries
		for (int i = 0; i < cvAccountCodes.size(); i++)
		{
			AccountCodesData laAccountCodesData =
				(AccountCodesData) cvAccountCodes.get(i);
			RefundTableData laRefundTableData = new RefundTableData();
			// Since CKREDM shows different descriptions, get this 
			// new description
			if (getController()
				.getTransCode()
				.equals(
					com
						.txdot
						.isd
						.rts
						.services
						.util
						.constants
						.TransCdConstant
						.CKREDM))
			{
				if (laAccountCodesData.getRedemdAcctItmCd() == null
					|| laAccountCodesData.getRedemdAcctItmCd().equals(""))
				{
					continue;
				}
				laDataFromCache =
					AccountCodesCache.getAcctCd(
						laAccountCodesData.getRedemdAcctItmCd(),
						RTSDate.getCurrentDate().getYYYYMMDDDate());
				laRefundTableData.setRedeemType(
					laDataFromCache.getAcctItmCdDesc());
				laRefundTableData.setType(
					laAccountCodesData.getAcctItmCdDesc());
			}
			else
			{
				laRefundTableData.setType(
					laAccountCodesData.getAcctItmCdDesc());
			}
			laRefundTableData.setAmount(new Dollar("0.00"));
			cvTableData.add(laRefundTableData);
		}
		if (getController()
			.getTransCode()
			.equals(
				com
					.txdot
					.isd
					.rts
					.services
					.util
					.constants
					.TransCdConstant
					.CKREDM))
		{
			// defect 8218 
			// Vector will be of PlateToStickerData objects    
			Vector lvPltToStkr =
				RegistrationPlateStickerCache.getPltStkrs(
					caMFVehicleData.getRegData().getRegClassCd(),
					caMFVehicleData.getRegData().getRegPltCd(),
					RTSDate.getCurrentDate().getYYYYMMDDDate(),
					caMFVehicleData.getRegData().getRegStkrCd());

			if (lvPltToStkr != null)
			{
				PlateToStickerData laPltToStkr =
					(PlateToStickerData) lvPltToStkr.get(0);

				// defect 5242  
				// Check for blank AcctItmCd 
				if (laPltToStkr.getAcctItmCd() != null
					&& !laPltToStkr.getAcctItmCd().trim().equals(""))
				{
					// defect 6759
					// Copy object so cache is not updated
					caRegAcctData =
						(AccountCodesData) UtilityMethods.copy(
						AccountCodesCache.getAcctCd(
							laPltToStkr.getAcctItmCd(),
							RTSDate.getCurrentDate().getYYYYMMDDDate()));
					caRegAcctData.setRedemdAcctItmCd(
						caRegAcctData.getAcctItmCd());
					// WS shouldn't have creditindi == 1, so that Credit
					// button doesn't enable in PMT004 CQU100003894
					// Do not turn off credit allowed indi! Will just
					// check for 'CKREDM' in PMT004 and disable credit.
					//caRegAcctData.setCrdtAllowdIndi(0);
					//end defect 6759
				}

				//regAcctData = AccountCodesCache.getAcctCd(regData.getAcctItmCd(), RTSDate.getCurrentDate().getYYYYMMDDDate());
				//regAcctData.setRedemdAcctItmCd(regAcctData.getAcctItmCd());
				// WS shouldn't have creditindi == 1, so that Credit button doesn't enable
				// in PMT004 CQU100003894
				//regAcctData.setCrdtAllowdIndi(0);

				if (caRegAcctData != null)
				{
					RefundTableData laRefundData =
						new RefundTableData();
					laRefundData.setType(
						caRegAcctData.getAcctItmCdDesc());
					laRefundData.setRedeemType(
						caRegAcctData.getAcctItmCdDesc());
					laRefundData.setAmount(new Dollar("0.00"));
					cvTableData.add(laRefundData);
				}
			}
			// end defect 8218 
			Collections.sort(cvTableData);
		}
	}
	/**
	 * Invoked when a component gains the keyboard focus.
	 * 
	 * @param aaFE	FocusEvent
	 */
	public void focusGained(FocusEvent aaFE)
	{

		// From Cancel 
		if (aaFE.getSource() == getbuttonPanel().getBtnCancel())
		{
			caPreviousComponent = getbuttonPanel().getBtnCancel();
		}

		// If From Enter Amount && Error has not been displayed
		if (aaFE.getSource() == gettxtEnterAmount()
			&& !cbErrorDisplayed)
		{
			caPreviousComponent = gettxtEnterAmount();
		}
		// From Cash Refund & Error not displaye
		if (aaFE.getSource() == getchkCashRefund()
			&& !cbErrorDisplayed)
		{
			if (caPreviousComponent == gettxtEnterAmount())
			{
				if (!isValidEntry())
				{
					gettxtEnterAmount().requestFocus();
					cbErrorDisplayed = true;
				}
				else
				{
					gettblHotCheck().requestFocus();
				}
				caPreviousComponent = null;
			}
		}
		else
		{
			cbErrorDisplayed = false;
		}
	}
	/**
	 * Invoked when a component loses the keyboard focus.
	 * 
	 * @param aaFE	FocusEvent
	 */
	public void focusLost(java.awt.event.FocusEvent aaFE)
	{
		// This class left Intentionally Empty
	}
	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getbuttonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("buttonPanel");
				ivjbuttonPanel.setBounds(75, 326, 331, 58);
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// defect 8240 
				// ivjbuttonPanel.getBtnEnter().addKeyListener(this);
				// ivjbuttonPanel.getBtnCancel().addKeyListener(this);
				// ivjbuttonPanel.getBtnHelp().addKeyListener(this);
				// end defect 8240
				//ivjbuttonPanel.getBtnEnter().addFocusListener(this);
				//ivjbuttonPanel.getBtnHelp().setNextFocusableComponent(
				//	gettblHotCheck());
				//ivjbuttonPanel.getBtnCancel().addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbuttonPanel;
	}
	/**
	 * Return the chkCashRefund property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkCashRefund()
	{
		if (ivjchkCashRefund == null)
		{
			try
			{
				ivjchkCashRefund = new javax.swing.JCheckBox();
				ivjchkCashRefund.setName("chkCashRefund");
				ivjchkCashRefund.setText("JCheckBox1");
				ivjchkCashRefund.setBounds(481, 300, 1, 1);
				ivjchkCashRefund.setForeground(
					new java.awt.Color(204, 204, 204));
				// user code begin {1}
				ivjchkCashRefund.addFocusListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkCashRefund;
	}
	/**
	 * Retrieve the Amount Entered
	 * 
	 * @return String
	 */
	private String getEnterAmount()
	{
		return gettxtEnterAmount().getText();
	}
	/**
	 * Return the JInternalFrameContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJInternalFrameContentPane()
	{
		if (ivjJInternalFrameContentPane == null)
		{
			try
			{
				ivjJInternalFrameContentPane = new javax.swing.JPanel();
				ivjJInternalFrameContentPane.setName(
					"JInternalFrameContentPane");
				ivjJInternalFrameContentPane.setLayout(null);
				getJInternalFrameContentPane().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getJInternalFrameContentPane().add(
					getstcLblTotalCredit(),
					getstcLblTotalCredit().getName());
				getJInternalFrameContentPane().add(
					getlblTotalCredit(),
					getlblTotalCredit().getName());
				getJInternalFrameContentPane().add(
					getstcLblEnterAmount(),
					getstcLblEnterAmount().getName());
				getJInternalFrameContentPane().add(
					gettxtEnterAmount(),
					gettxtEnterAmount().getName());
				getJInternalFrameContentPane().add(
					getchkCashRefund(),
					getchkCashRefund().getName());
				getJInternalFrameContentPane().add(
					getbuttonPanel(),
					getbuttonPanel().getName());
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
		return ivjJInternalFrameContentPane;
	}
	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JScrollPane getJScrollPane1()
	{

		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setBounds(23, 28, 435, 190);
				getJScrollPane1().setViewportView(gettblHotCheck());
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
		return ivjJScrollPane1;
	}
	/**
	 * Return the lblTotalCredit property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblTotalCredit()
	{
		if (ivjlblTotalCredit == null)
		{
			try
			{
				ivjlblTotalCredit = new javax.swing.JLabel();
				ivjlblTotalCredit.setName("lblTotalCredit");
				ivjlblTotalCredit.setText(DEFLT_AMT);
				ivjlblTotalCredit.setBounds(349, 241, 78, 14);
				ivjlblTotalCredit.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// ivjlblTotalCredit.setEnabled(false);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblTotalCredit;
	}
	/**
	 * Return the stcLblEnterAmount property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblEnterAmount()
	{
		if (ivjstcLblEnterAmount == null)
		{
			try
			{
				ivjstcLblEnterAmount = new javax.swing.JLabel();
				ivjstcLblEnterAmount.setName("stcLblEnterAmount");
				ivjstcLblEnterAmount.setText(ENTER_AMT);
				ivjstcLblEnterAmount.setBounds(218, 267, 105, 14);
				ivjstcLblEnterAmount.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblEnterAmount;
	}
	/**
	 * Return the stcLblTotalCredit property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblTotalCredit()
	{
		if (ivjstcLblTotalCredit == null)
		{
			try
			{
				ivjstcLblTotalCredit = new javax.swing.JLabel();
				ivjstcLblTotalCredit.setName("stcLblTotalCredit");
				ivjstcLblTotalCredit.setText(TOTAL_AMT);
				ivjstcLblTotalCredit.setBounds(197, 241, 126, 14);
				ivjstcLblTotalCredit.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblTotalCredit;
	}
	/**
	 * Retrieve the Table Data
	 * 
	 * @return Vector
	 */
	private Vector getTableData()
	{
		// get account codes from the cache, excluding those we 
		// don't care about
		cvAccountCodes = new Vector();
		RTSDate laToday = RTSDate.getCurrentDate();
		try
		{
			cvAccountCodes =
				AccountCodesCache.getAcctCds(
					"",
					laToday.getYYYYMMDDDate(),
					AccountCodesCache.GET_ALL);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
		}
		int i = 0;
		while (i < cvAccountCodes.size())
		{
			AccountCodesData laAcctCdsData =
				(AccountCodesData) cvAccountCodes.get(i);
			String lsCompareString =
				laAcctCdsData.getAcctProcsCd().toUpperCase();
			if (!lsCompareString.equals(AccountCodesCache.HOT_CHECK))
			{
				cvAccountCodes.remove(i);
				continue;
			}
			i++;
		}
		com.txdot.isd.rts.services.util.UtilityMethods.sort(
			cvAccountCodes);
		return cvAccountCodes;
	}
	/**
	 * Return the tblHotCheck property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblHotCheck()
	{
		if (ivjtblHotCheck == null)
		{
			try
			{
				ivjtblHotCheck = new RTSTable();
				ivjtblHotCheck.setName("tblHotCheck");
				getJScrollPane1().setColumnHeaderView(
					ivjtblHotCheck.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblHotCheck.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblHotCheck.setModel(
					new com
						.txdot
						.isd
						.rts
						.client
						.accounting
						.ui
						.TMACC006());
				ivjtblHotCheck.setShowVerticalLines(false);
				ivjtblHotCheck.setShowHorizontalLines(false);
				ivjtblHotCheck.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblHotCheck.setBounds(0, 0, 402, 146);
				// user code begin {1}
				caTableModel = (TMACC006) ivjtblHotCheck.getModel();
				TableColumn laTableModelA =
					ivjtblHotCheck.getColumn(
						ivjtblHotCheck.getColumnName(0));
				laTableModelA.setPreferredWidth(300);
				TableColumn laTableModelB =
					ivjtblHotCheck.getColumn(
						ivjtblHotCheck.getColumnName(1));
				laTableModelB.setPreferredWidth(100);
				ivjtblHotCheck.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblHotCheck.init();
				laTableModelA.setCellRenderer(
					ivjtblHotCheck.setColumnAlignment(RTSTable.LEFT));
				laTableModelB.setCellRenderer(
					ivjtblHotCheck.setColumnAlignment(RTSTable.RIGHT));
				ivjtblHotCheck
					.getSelectionModel()
					.addListSelectionListener(
					this);
				//ivjtblHotCheck.setNextFocusableComponent(
				//	gettxtEnterAmount());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblHotCheck;
	}
	/**
	 * Retrieve the Total Credit Amount 
	 * 
	 * @return String
	 */
	private String getTotalCreditAmount()
	{
		return getlblTotalCredit().getText();
	}
	/**
	 * Return the txtEnterAmount property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtEnterAmount()
	{
		if (ivjtxtEnterAmount == null)
		{
			try
			{
				ivjtxtEnterAmount = new RTSInputField();
				ivjtxtEnterAmount.setName("txtEnterAmount");
				ivjtxtEnterAmount.setInput(5);
				ivjtxtEnterAmount.setBounds(349, 264, 78, 20);
				ivjtxtEnterAmount.setMaxLength(8);
				ivjtxtEnterAmount.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				// user code begin {1}
				ivjtxtEnterAmount.addFocusListener(this);
				ivjtxtEnterAmount.addKeyListener(this);
				//ivjtxtEnterAmount.setNextFocusableComponent(getbuttonPanel().getBtnEnter());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtEnterAmount;
	}
	/**
	 * Called whenever the part throws an exception.
	 *  
	 * @param aeException	Throwable	
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
			setLocation(
				Toolkit.getDefaultToolkit().getScreenSize().width / 2
					- getSize().width / 2,
				Toolkit.getDefaultToolkit().getScreenSize().height / 2
					- getSize().height / 2);
			// user code end
			setName("FrmHotCheck");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(481, 400);
			setModal(true);
			setTitle(TITLE_ACC004);
			setContentPane(getJInternalFrameContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}
	/**
	 * Validates the entry to go in the table
	 * 
	 * @return boolean
	 */
	private boolean isValidEntry()
	{
		// VALIDATION
		clearAllColor(this);
		RTSException leRTSEx = new RTSException();
		double ldEnteredAmount = 0.0;
		if (getEnterAmount().equals(""))
		{
			if (gettxtEnterAmount().hasFocus())
			{
				leRTSEx.addException(
					new RTSException(150),
					gettxtEnterAmount());
			}
		}
		else
		{
			try
			{
				ldEnteredAmount = Double.parseDouble(getEnterAmount());
			}
			catch (NumberFormatException aeNFEx)
			{
				leRTSEx.addException(
					new RTSException(150),
					gettxtEnterAmount());
			}
			if (ldEnteredAmount == 0)
			{
				leRTSEx.addException(
					new RTSException(150),
					gettxtEnterAmount());
			}
			else if (ldEnteredAmount > 99999)
			{
				leRTSEx.addException(
					new RTSException(640),
					gettxtEnterAmount());
			}
			else if (ldEnteredAmount < 0)
			{
				leRTSEx.addException(
					new RTSException(174),
					gettxtEnterAmount());
			}
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			return false;
		}
		// update the record in the table
		ciSelectedRow = gettblHotCheck().getSelectedRow();
		RefundTableData laSelectedRecord =
			(RefundTableData) cvTableData.get(ciSelectedRow);
		Dollar laEnteredAmountDollar = new Dollar(ldEnteredAmount);
		laSelectedRecord.setAmount(laEnteredAmountDollar);
		caTableModel.add(cvTableData);
		gettblHotCheck().setRowSelectionInterval(
			ciSelectedRow,
			ciSelectedRow);
		ciSelectedRow = -1;
		// update the total amount to reflect the new entry
		Dollar laTotalCreditAmountDollar = new Dollar(ZERO_DOLLAR);
		for (int i = 0; i < cvTableData.size(); i++)
		{
			RefundTableData laRefundTableData =
				(RefundTableData) cvTableData.get(i);
			laTotalCreditAmountDollar =
				laTotalCreditAmountDollar.add(
					laRefundTableData.getAmount());
		}
		setTotalCreditAmount(laTotalCreditAmountDollar.toString());
		return true;
	}
	/**
	 * Handles the key navigation of the button panel
	 * 
	 * @param aaKE	KeyEvent 
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_ENTER
			&& aaKE.getSource() == gettxtEnterAmount())
		{
			if (isValidEntry())
			{
				getbuttonPanel().getBtnEnter().doClick();
			}
			aaKE.consume();
		}
		else
		{
			super.keyPressed(aaKE);
		}
	}
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
			FrmHotCheckACC004 laFrmACC004 = new FrmHotCheckACC004();
			try
			{
				CacheManager.loadCache();
			}
			catch (RTSException aeRTSEx)
			{
				System.err.println(ERRMSG_CACHE + aeRTSEx.getMessage());
				Log.write(
					Log.START_END,
					laFrmACC004,
					ERRMSG_CACHE + aeRTSEx.getMessage());
			}
			MFVehicleData laMFVehicleData = new MFVehicleData();
			RegistrationData laRegistrationData =
				new RegistrationData();
			laRegistrationData.setRegRefAmt(new Dollar(5.50));
			laMFVehicleData.setRegData(laRegistrationData);
			laFrmACC004.setData(laMFVehicleData);
			laFrmACC004.setVisible(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}
	/**
	 * all subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject	Object
	 */
	public void setData(Object aaDataObject)
	{
		caMFVehicleData =
			(MFVehicleData) UtilityMethods.copy(aaDataObject);
		getTableData();
		convertAccountToTable();
		caTableModel.add(cvTableData);
		setTotalCreditAmount(ZERO_DOLLAR);
		if (cvTableData.size() > 0)
		{
			gettblHotCheck().setRowSelectionInterval(0, 0);
		}
	}
	/**
	 * Sets the amount entered
	 * 
	 * @param asAmount	String
	 */
	//	private void setEnterAmount(String asAmount)
	//	{
	//		gettxtEnterAmount().setText(asAmount);
	//	}
	/**
	 * Sets the total credit amount
	 * 
	 * @param asAmount String
	 */
	private void setTotalCreditAmount(String asAmount)
	{
		getlblTotalCredit().setText(asAmount);
	}
	/** 
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aaLSE	javax.swing.event.ListSelectionEvent	
	  */
	public void valueChanged(
		javax.swing.event.ListSelectionEvent aaLSE)
	{
		clearAllColor(this);
		Dollar laTempDollar = null;
		if (ciSelectedRow == -1)
		{
			laTempDollar =
				((RefundTableData) cvTableData
					.get(gettblHotCheck().getSelectedRow()))
					.getAmount();
		}
		else
		{
			laTempDollar =
				((RefundTableData) cvTableData.get(ciSelectedRow))
					.getAmount();
		}
		if (laTempDollar.toString().equals(ZERO_DOLLAR))
		{
			gettxtEnterAmount().setText("");
		}
		else
		{
			gettxtEnterAmount().setText(laTempDollar.toString());
		}
	}
}
