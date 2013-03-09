package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.AccountCodesCache;
import com.txdot.isd.rts.services.cache.CacheManager;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmRefundACC006.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments
 * MAbs		    04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * M Listberger 08/09/2002  Changed logic in getTableData to correct the
 *                          reg fee refund usage.  The error was caused 
 * 							by a vehicle that did not have a VIN number.  
 * 							So, changed code to look at reg. refund amount.  
 * 							Defect 4591.
 * K. Harrell   08/16/2002  Added 
 * 							"&& data.getRegData().getRegRefAmt() != null"
 * 							defect 4607 
 * M Listberger 10/24/2002  Moved the line added in 4607 to be before the
 * 							code "&& data.getRegData().getRegRefAmt().
 * 							equals(new Dollar("0.00"))".
 *                          Changed "&& data.getRegData().getRegRefAmt() 
 * 							!= new Dollar ("0.00")"  to 
 * 							"&& data.getRegData().getRegRefAmt().
 * 							equals(new Dollar("0.00"))".
 *                          Added code to check to see if the plate or 
 * 							sticker number entered has been cancelled.  
 * 							If cancelled do not want to display 
 * 							"Registration Refund $0.00" at the top of the 	
 * 							screen.  Modified method getTableData().
 *							Added code   
 *							"&& data.getRegData().getCancPltIndi() == 0 
 *							&& data.getRegData().getCancStkrIndi() == 0)" 
 *                          modify setDataObject().
 * 							defect 4849 
 * S Govindappa	01/27/2003 	Made changes in isValidEntry() not to 
 * 							do validation on amount field when there 
 * 							is a preauthorized refund amount.
 * 							defect 4723
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	07/25/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/17/2005	Remove keylistener from buttonPanel
 * 							components. 
 * 							modify getbuttonPanel(),keyPressed() 
 * 							defect 8240 Ver 5.2.3
 * K Harrell	12/20/2005	restore focusGained(), add'l work on focus
 * 							add cbErrorDisplayed,caPreviousComponent
 * 							modify focusGained(),keyPressed
 * 							defect 7884 Ver 5.2.3  
 * B Hargrove	06/08/2009	Remove all 'Cancelled Sticker' references.
 * 							modify getTableData(), setData()
 * 							delete getBuilderData()
 * 							defect 9953 Ver Defect_POS_F
 * R Pilon		08/08/2012	Comment out logic attempting to request focus in
 * 							  the refund type table when the cash refund
 * 							  checkbox was selected using the <Alt> + <R> hot
 * 							  keys. If the cursor was in the amount input when
 * 							  the hot keys were pressed, the check box would 
 * 							  appear to recieve the command twice causing the 
 * 							  check box to get selected and then immediately 
 * 							  deselected. This problem was introduced with the
 * 							  migration to the 1.7 JRE. In the 1.4 JRE, the
 * 							  request focus failed, but it did not cause the 
 * 							  issue descibed above.
 * 							modify focusGained(FocusEvent)
 *							defect 11428 Ver 7.0.0
 * --------------------------------------------------------------------- 
 */

/**
 * ACC006 is the Refund screen
 * 
 * @version	POS_700			08/08/2012
 * @author	Michael Abernethy
 * <br>Creation Date:		06/12/2001 09:00:33 
 *
 */
public class FrmRefundACC006
	extends RTSDialogBox
	implements ActionListener, FocusListener, KeyListener, ListSelectionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JCheckBox ivjchkCashRefund = null;
	private JLabel ivjlblTotalCredit = null;
	private JLabel ivjlblAmount = null;
	private JLabel ivjstcLblAmount = null;
	private JLabel ivjstcLblEnterAmount = null;
	private JLabel ivjstcLblRegistration = null;
	private JLabel ivjstcLblTotalCredit = null;
	private JPanel ivjJInternalFrameContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSInputField ivjtxtEnterAmount = null;
	private RTSTable ivjtblRefund = null;
	private TMACC006 caTableModel;
	private java.awt.Component caPreviousComponent;

	// boolean
	private boolean cbErrorDisplayed = false;

	// int 
	private int ciSelectedRow;

	// Object 
	private AccountCodesData caRemovedAccountCode;
	private MFVehicleData caMFVehData;

	// Vector 
	private Vector cvTableData;
	private Vector cvAccountData;

	// String
	private final static String AMOUNT = "Amount:";
	private final static String CASH_REFUND = "Cash Refund";
	private final static String DEFLT_AMT = "000000.00";
	private final static String DEFLT_TOTAMT = "00000000.00";
	private final static String ENTER_AMOUNT = "Enter Amount:";
	private final static String ERRMSG_CACHE = "Cache error!! \n";
	private final static String ERRMSG_ERROR = "Error";
	private final static String ERRMSG_IS_CORRECT =
		".  Is this correct?";
	private final static String ERRMSG_NO_ACCT_CDS =
		"No account codes found";
	private final static String ERRMSG_TOTAL_REQ =
		"Total refund requested is $";
	private final static String REFUND_REG_FEE =
		"REFUND - REGISTRATION FEE";
	private final static String TITLE_ACC006 = "Refund   ACC006";
	private final static String TOT_CREDIT_AMT = "Total Credit Amount:";
	private final static String ZERO_DOLLAR = "0.00";

	/**
	 * FrmRefund constructor comment.
	 */
	public FrmRefundACC006()
	{
		super();
		initialize();
	}
	/**
	 * Creates a ACC006 with the parent
	 * 
	 * @param aaParent	Dialog 
	 */
	public FrmRefundACC006(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Creates a ACC006 with the parent
	 * 
	 * @param parent	JFrame 
	 */
	public FrmRefundACC006(JFrame aaParent)
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
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				if (!isValidEntry())
				{
					return;
				}
				RTSException leRTSEx1 = new RTSException();
				if (Double.parseDouble(getlblTotalCredit().getText())
					<= 0)
				{
					leRTSEx1.addException(
						new RTSException(150),
						gettxtEnterAmount());
				}
				if (leRTSEx1.isValidationError())
				{
					leRTSEx1.displayError(this);
					leRTSEx1.getFirstComponent().requestFocus();
					return;
				}
				RTSException leRTSEx2 =
					new RTSException(
						RTSException.CTL001,
						ERRMSG_TOTAL_REQ
							+ getTotalCreditAmount()
							+ ERRMSG_IS_CORRECT,
						"");
				int liResponse = leRTSEx2.displayError(this);
				if (liResponse == RTSException.YES)
				{
					// Create the dataobject with the refund information in it
					MFVehicleData laMFVehData =
						(MFVehicleData) UtilityMethods.copy(
							caMFVehData);
					CompleteTransactionData laCTData =
						new CompleteTransactionData();
					if (isCashRefundSelected())
					{
						laCTData.setTransCode(
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.constants
								.TransCdConstant
								.RFCASH);
					}
					else
					{
						laCTData.setTransCode(
							com
								.txdot
								.isd
								.rts
								.services
								.util
								.constants
								.TransCdConstant
								.REFUND);
					}
					laMFVehData.getRegData().setRegRefAmt(
						new Dollar(ZERO_DOLLAR));
					RegFeesData laRegFeesData = new RegFeesData();
					Vector lvVector = new Vector();
					if (caRemovedAccountCode != null
						&& Double.parseDouble(getlblAmount().getText())
							> 0.0)
					{
						FeesData laFeesData = new FeesData();
						laFeesData.setAcctItmCd(
							caRemovedAccountCode.getAcctItmCd());
						laFeesData.setCrdtAllowedIndi(
							caRemovedAccountCode.getCrdtAllowdIndi());
						laFeesData.setDesc(
							caRemovedAccountCode.getAcctItmCdDesc());
						laFeesData.setItemPrice(
							new Dollar(getlblAmount().getText()));
						laFeesData.setItmQty(1);
						lvVector.add(laFeesData);
					}
					for (int i = 0; i < cvTableData.size(); i++)
					{
						RefundTableData laRefundTableData =
							(RefundTableData) cvTableData.get(i);
						if (Double
							.parseDouble(
								laRefundTableData
									.getAmount()
									.getValue())
							> 0)
						{
							AccountCodesData laTempData =
								(AccountCodesData) cvAccountData.get(i);
							FeesData laFeesData = new FeesData();
							laFeesData.setAcctItmCd(
								laTempData.getAcctItmCd());
							laFeesData.setCrdtAllowedIndi(
								laTempData.getCrdtAllowdIndi());
							laFeesData.setDesc(
								laTempData.getAcctItmCdDesc());
							laFeesData.setItemPrice(
								laRefundTableData.getAmount());
							laFeesData.setItmQty(1);
							lvVector.add(laFeesData);
						}
					}
					laRegFeesData.setVectFees(lvVector);
					laCTData.setRegFeesData(laRegFeesData);
					laCTData.setOrgVehicleInfo(caMFVehData);
					laCTData.setVehicleInfo(laMFVehData);
					laCTData.setOfcIssuanceNo(
						SystemProperty.getOfficeIssuanceNo());
					getController().processData(
						AbstractViewController.ENTER,
						laCTData);
				}
				else
				{
					gettblRefund().requestFocus();
					return;
				}
			}
			else if (
				aaAE.getSource() == getbuttonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.ACC006);
			}
		}
		finally
		{
			doneWorking();
		}
	}
	/**
	 * Create the table data
	 */
	private void convertAccountToTable()
	{
		if (cvTableData == null)
		{
			cvTableData = new Vector();
		}
		cvTableData.removeAllElements();
		for (int i = 0; i < cvAccountData.size(); i++)
		{
			AccountCodesData laTempData =
				(AccountCodesData) cvAccountData.get(i);
			RefundTableData laRefundData = new RefundTableData();
			laRefundData.setType(laTempData.getAcctItmCdDesc());
			laRefundData.setAmount(new Dollar(ZERO_DOLLAR));
			cvTableData.add(laRefundData);
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
				// defect 11428
//				else
//				{
//					gettblRefund().requestFocus();
//				}
				// end defect 11428
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
	public void focusLost(FocusEvent aaFE)
	{
		// This class left intentionally empty
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
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
				// defect 8240 
				//ivjbuttonPanel.getBtnEnter().addKeyListener(this);
				//ivjbuttonPanel.getBtnCancel().addKeyListener(this);
				//ivjbuttonPanel.getBtnHelp().addKeyListener(this);
				//ivjbuttonPanel.getBtnHelp().setNextFocusableComponent(
				//	gettblRefund());
				//ivjbuttonPanel.getBtnCancel().addFocusListener(this);
				//ivjbuttonPanel.getBtnEnter().addFocusListener(this);
				// end defect 8240 
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
				ivjchkCashRefund.setMnemonic('R');
				ivjchkCashRefund.setText(CASH_REFUND);
				ivjchkCashRefund.setFocusPainted(false);
				ivjchkCashRefund.setRequestFocusEnabled(true);
				// user code begin {1}
				ivjchkCashRefund.addFocusListener(this);
				ivjchkCashRefund.setFocusPainted(true);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkCashRefund;
	}
	/**
	 * returns the text in the enter amount text field
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
				ivjJInternalFrameContentPane.setLayout(
					new java.awt.GridBagLayout());
				java
					.awt
					.GridBagConstraints constraintsstcLblTotalCredit =
					new java.awt.GridBagConstraints();
				constraintsstcLblTotalCredit.gridx = 1;
				constraintsstcLblTotalCredit.gridy = 3;
				constraintsstcLblTotalCredit.ipadx = 11;
				constraintsstcLblTotalCredit.insets =
					new java.awt.Insets(6, 201, 4, 10);
				getJInternalFrameContentPane().add(
					getstcLblTotalCredit(),
					constraintsstcLblTotalCredit);
				java.awt.GridBagConstraints constraintslblTotalCredit =
					new java.awt.GridBagConstraints();
				constraintslblTotalCredit.gridx = 2;
				constraintslblTotalCredit.gridy = 3;
				constraintslblTotalCredit.anchor =
					java.awt.GridBagConstraints.EAST;
				constraintslblTotalCredit.ipadx = 6;
				constraintslblTotalCredit.insets =
					new java.awt.Insets(6, 11, 4, 53);
				getJInternalFrameContentPane().add(
					getlblTotalCredit(),
					constraintslblTotalCredit);
				java
					.awt
					.GridBagConstraints constraintsstcLblEnterAmount =
					new java.awt.GridBagConstraints();
				constraintsstcLblEnterAmount.gridx = 1;
				constraintsstcLblEnterAmount.gridy = 4;
				constraintsstcLblEnterAmount.ipadx = 25;
				constraintsstcLblEnterAmount.insets =
					new java.awt.Insets(8, 222, 10, 10);
				getJInternalFrameContentPane().add(
					getstcLblEnterAmount(),
					constraintsstcLblEnterAmount);
				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 2;
				constraintsJScrollPane1.gridwidth = 2;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 413;
				constraintsJScrollPane1.ipady = 164;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(1, 22, 5, 23);
				getJInternalFrameContentPane().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
				java.awt.GridBagConstraints constraintstxtEnterAmount =
					new java.awt.GridBagConstraints();
				constraintstxtEnterAmount.gridx = 2;
				constraintstxtEnterAmount.gridy = 4;
				constraintstxtEnterAmount.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtEnterAmount.weightx = 1.0;
				constraintstxtEnterAmount.ipadx = 75;
				constraintstxtEnterAmount.insets =
					new java.awt.Insets(5, 11, 7, 53);
				getJInternalFrameContentPane().add(
					gettxtEnterAmount(),
					constraintstxtEnterAmount);
				java.awt.GridBagConstraints constraintschkCashRefund =
					new java.awt.GridBagConstraints();
				constraintschkCashRefund.gridx = 1;
				constraintschkCashRefund.gridy = 5;
				constraintschkCashRefund.insets =
					new java.awt.Insets(8, 191, 3, 49);
				getJInternalFrameContentPane().add(
					getchkCashRefund(),
					constraintschkCashRefund);
				java.awt.GridBagConstraints constraintsbuttonPanel =
					new java.awt.GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 6;
				constraintsbuttonPanel.gridwidth = 2;
				constraintsbuttonPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 114;
				constraintsbuttonPanel.ipady = 23;
				constraintsbuttonPanel.insets =
					new java.awt.Insets(4, 74, 15, 75);
				getJInternalFrameContentPane().add(
					getbuttonPanel(),
					constraintsbuttonPanel);
				java.awt.GridBagConstraints constraintsJPanel1 =
					new java.awt.GridBagConstraints();
				constraintsJPanel1.gridx = 1;
				constraintsJPanel1.gridy = 1;
				constraintsJPanel1.gridwidth = 2;
				constraintsJPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJPanel1.weightx = 1.0;
				constraintsJPanel1.weighty = 1.0;
				constraintsJPanel1.ipadx = -14;
				constraintsJPanel1.insets =
					new java.awt.Insets(10, 22, 0, 23);
				getJInternalFrameContentPane().add(
					getJPanel1(),
					constraintsJPanel1);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJInternalFrameContentPane;
	}
	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());
				java
					.awt
					.GridBagConstraints constraintsstcLblRegistration =
					new java.awt.GridBagConstraints();
				constraintsstcLblRegistration.gridx = 1;
				constraintsstcLblRegistration.gridy = 1;
				constraintsstcLblRegistration.ipadx = 13;
				constraintsstcLblRegistration.insets =
					new java.awt.Insets(7, 9, 7, 37);
				getJPanel1().add(
					getstcLblRegistration(),
					constraintsstcLblRegistration);
				java.awt.GridBagConstraints constraintsstcLblAmount =
					new java.awt.GridBagConstraints();
				constraintsstcLblAmount.gridx = 2;
				constraintsstcLblAmount.gridy = 1;
				constraintsstcLblAmount.ipadx = 21;
				constraintsstcLblAmount.insets =
					new java.awt.Insets(7, 38, 7, 13);
				getJPanel1().add(
					getstcLblAmount(),
					constraintsstcLblAmount);
				java.awt.GridBagConstraints constraintslblAmount =
					new java.awt.GridBagConstraints();
				constraintslblAmount.gridx = 3;
				constraintslblAmount.gridy = 1;
				constraintslblAmount.ipadx = 15;
				constraintslblAmount.insets =
					new java.awt.Insets(7, 13, 7, 22);
				getJPanel1().add(getlblAmount(), constraintslblAmount);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel1;
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
				getJScrollPane1().setViewportView(gettblRefund());
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJScrollPane1;
	}
	/**
	 * Return the lblAmount property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblAmount()
	{
		if (ivjlblAmount == null)
		{
			try
			{
				ivjlblAmount = new javax.swing.JLabel();
				ivjlblAmount.setName("lblAmount");
				ivjlblAmount.setText(DEFLT_AMT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblAmount;
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
				ivjlblTotalCredit.setText(DEFLT_TOTAMT);
				ivjlblTotalCredit.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjlblTotalCredit;
	}
	/**
	 * Return the stcLblAmount property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblAmount()
	{
		if (ivjstcLblAmount == null)
		{
			try
			{
				ivjstcLblAmount = new javax.swing.JLabel();
				ivjstcLblAmount.setName("stcLblAmount");
				ivjstcLblAmount.setText(AMOUNT);
				ivjstcLblAmount.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblAmount;
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
				ivjstcLblEnterAmount.setText(ENTER_AMOUNT);
				ivjstcLblEnterAmount.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblEnterAmount;
	}
	/**
	 * Return the stcLblRegistration property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblRegistration()
	{
		if (ivjstcLblRegistration == null)
		{
			try
			{
				ivjstcLblRegistration = new javax.swing.JLabel();
				ivjstcLblRegistration.setName("stcLblRegistration");
				ivjstcLblRegistration.setText(REFUND_REG_FEE);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblRegistration;
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
				ivjstcLblTotalCredit.setText(TOT_CREDIT_AMT);
				ivjstcLblTotalCredit.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblTotalCredit;
	}
	/**
	 * Return vector of Table Data
	 * 
	 * @return Vector
	 */
	private Vector getTableData()
	{
		// get account codes from the cache, excluding those we don't 
		// care about
		Vector lvAccountCodes = new Vector();
		RTSDate laCurrentRTSDate = RTSDate.getCurrentDate();
		try
		{
			lvAccountCodes =
				AccountCodesCache.getAcctCds(
					"",
					laCurrentRTSDate.getYYYYMMDDDate(),
					AccountCodesCache.GET_ALL);
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.displayError(this);
			return new Vector();
		}
		if (lvAccountCodes == null)
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.FAILURE_MESSAGE,
					ERRMSG_NO_ACCT_CDS,
					ERRMSG_ERROR);
			leRTSEx.displayError(this);
			return new java.util.Vector();
		}
		int i = 0;
		while (i < lvAccountCodes.size())
		{
			AccountCodesData laAcctCodesData =
				(AccountCodesData) lvAccountCodes.get(i);
			String lsCompareString =
				laAcctCodesData.getAcctProcsCd().toUpperCase();
			if (!lsCompareString.equals(AccountCodesCache.REFUND))
			{
				lvAccountCodes.remove(i);
				continue;
			}
			i++;
		}
		com.txdot.isd.rts.services.util.UtilityMethods.sort(
			lvAccountCodes);
		// If the record was found, data will be an graph of MFVehicleData and we should get rid of the entry
		// "Refund - Registration Fee" from the table
		// Original
		//	if (data.getVehicleData() != null && data.getVehicleData().getVin() != null && ! data.getVehicleData().getVin().equals(""))
		// defect 4591 - ML
		//	if (data.getVehicleData() != null && data.getRegData().getRegRefAmt() != new Dollar("0.00"))
		// defect 4607 - KPH
		// if (data.getVehicleData() != null
		//    && (data.getRegData().getRegRefAmt()) != new Dollar("0.00")
		//    && data.getRegData().getRegRefAmt() != null)
		// defect 4849 - MML
		if (caMFVehData.getVehicleData() != null
			&& caMFVehData.getRegData().getRegRefAmt() != null
			&& (!caMFVehData
				.getRegData()
				.getRegRefAmt()
				.equals(new Dollar(ZERO_DOLLAR))
				// defect 9953
				//|| caMFVehData.getRegData().getCancPltIndi() == 0
				//&& caMFVehData.getRegData().getCancStkrIndi() == 0))
				|| caMFVehData.getRegData().getCancPltIndi() == 0))
				// end defect 9953
		{
			for (int j = 0; j < lvAccountCodes.size(); j++)
			{
				AccountCodesData laAcctCodesData =
					(AccountCodesData) lvAccountCodes.get(j);
				if (laAcctCodesData
					.getAcctItmCdDesc()
					.trim()
					.toUpperCase()
					.equals(REFUND_REG_FEE))
					caRemovedAccountCode =
						(AccountCodesData) lvAccountCodes.remove(j);
			}
		}
		return lvAccountCodes;
	}
	/**
	 * Return the tblRefund property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblRefund()
	{
		if (ivjtblRefund == null)
		{
			try
			{
				ivjtblRefund = new RTSTable();
				ivjtblRefund.setName("tblRefund");
				getJScrollPane1().setColumnHeaderView(
					ivjtblRefund.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);

				ivjtblRefund.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblRefund.setModel(
					new com
						.txdot
						.isd
						.rts
						.client
						.accounting
						.ui
						.TMACC006());
				ivjtblRefund.setShowVerticalLines(false);
				ivjtblRefund.setShowHorizontalLines(false);
				ivjtblRefund.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblRefund.setBounds(0, 0, 402, 146);
				// user code begin {1}
				caTableModel = (TMACC006) ivjtblRefund.getModel();
				TableColumn laTableColumnA =
					ivjtblRefund.getColumn(
						ivjtblRefund.getColumnName(0));
				laTableColumnA.setPreferredWidth(300);
				TableColumn laTableColumnB =
					ivjtblRefund.getColumn(
						ivjtblRefund.getColumnName(1));
				laTableColumnB.setPreferredWidth(100);
				ivjtblRefund.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblRefund.init();
				laTableColumnA.setCellRenderer(
					ivjtblRefund.setColumnAlignment(RTSTable.LEFT));
				laTableColumnB.setCellRenderer(
					ivjtblRefund.setColumnAlignment(RTSTable.RIGHT));
				ivjtblRefund
					.getSelectionModel()
					.addListSelectionListener(
					this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblRefund;
	}
	/**
	 * Return Total Credit Amount
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
				ivjtxtEnterAmount.setHorizontalAlignment(
					javax.swing.JTextField.RIGHT);
				ivjtxtEnterAmount.setMaxLength(9);
				// user code begin {1}
				ivjtxtEnterAmount.addFocusListener(this);
				ivjtxtEnterAmount.addKeyListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
			setName("FrmRefund");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(480, 400);
			setModal(true);
			setTitle(TITLE_ACC006);
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
	 * returns true if the Cash Refund checkbox is selecte
	 * 
	 * @return boolean
	 */
	private boolean isCashRefundSelected()
	{
		return getchkCashRefund().isSelected();
	}
	/**
	 * Validates the entry before putting it in the table
	 * 
	 * @return boolean
	 */
	private boolean isValidEntry()
	{
		// VALIDATION
		clearAllColor(this);
		RTSException leRTSEx = new RTSException();
		double ldEnteredAmount = 0.0;
		if (!getEnterAmount().equals(""))
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
			if (ldEnteredAmount < 0)
			{
				leRTSEx.addException(
					new RTSException(174),
					gettxtEnterAmount());
			}
			else if (ldEnteredAmount > 999999.99)
			{
				leRTSEx.addException(
					new RTSException(740),
					gettxtEnterAmount());
			}
		}
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			//leRTSEx.getFirstComponent().requestFocus();
			return false;
		}
		// END VALIDATION
		// update the record in the table
		Dollar laDollar1 = new Dollar(ldEnteredAmount);
		ciSelectedRow = gettblRefund().getSelectedRow();
		RefundTableData laSelectedRecord =
			(RefundTableData) cvTableData.get(ciSelectedRow);
		laSelectedRecord.setAmount(laDollar1);
		caTableModel.add(cvTableData);
		gettblRefund().setRowSelectionInterval(
			ciSelectedRow,
			ciSelectedRow);
		ciSelectedRow = -1;
		// update the total amount to reflect the new entry		
		Dollar laDollar2 = new Dollar(getlblAmount().getText());
		for (int i = 0; i < cvTableData.size(); i++)
		{
			RefundTableData laRefundTblData =
				(RefundTableData) cvTableData.get(i);
			laDollar2 = laDollar2.add(laRefundTblData.getAmount());
		}
		setTotalCreditAmount(laDollar2.toString());
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
			FrmRefundACC006 laFrameACC006 = new FrmRefundACC006();
			try
			{
				CacheManager.loadCache();
			}
			catch (RTSException aeRTSEx)
			{
				System.err.println(ERRMSG_CACHE + aeRTSEx.getMessage());
				Log.write(
					Log.START_END,
					laFrameACC006,
					ERRMSG_CACHE + aeRTSEx.getMessage());
			}
			MFVehicleData laMFVehData = new MFVehicleData();
			RegistrationData laRegData = new RegistrationData();
			laRegData.setRegRefAmt(new Dollar(5.50));
			laMFVehData.setRegData(laRegData);
			laFrameACC006.setData(laMFVehData);
			laFrameACC006.setVisible(true);
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
		caMFVehData = (MFVehicleData) UtilityMethods.copy(aaDataObject);
		cvAccountData = getTableData();
		convertAccountToTable();
		caTableModel.add(cvTableData);
		if (cvTableData.size() > 0)
		{
			gettblRefund().setRowSelectionInterval(0, 0);
		}
		// original code
		// if (data.getRegData().getRegRefAmt() != null
		// defect 4849 - ML
		if (caMFVehData.getRegData().getRegRefAmt() != null
			// defect 9953
			//&& caMFVehData.getRegData().getCancPltIndi() == 0
			//&& caMFVehData.getRegData().getCancStkrIndi() == 0)
			&& caMFVehData.getRegData().getCancPltIndi() == 0)
			// end defect 9953
		{
			getlblAmount().setText(
				caMFVehData.getRegData().getRegRefAmt().toString());
			setTotalCreditAmount(
				caMFVehData.getRegData().getRegRefAmt().toString());
			getstcLblAmount().setVisible(true);
			getlblAmount().setVisible(true);
			getstcLblRegistration().setVisible(true);
		}
		else
		{
			setTotalCreditAmount(ZERO_DOLLAR);
			getstcLblAmount().setVisible(false);
			getlblAmount().setVisible(false);
			getstcLblRegistration().setVisible(false);
		}
	}
	/**
	 * sets the text in the enter amount text field
	 * 
	 * @param asAmount	String
	 */
	private void setEnterAmount(String asAmount)
	{
		gettxtEnterAmount().setText(asAmount);
	}
	/**
	 * sets the total credit amount lable
	 * 
	 * @param asCreditAmount	String
	 */
	private void setTotalCreditAmount(String asCreditAmount)
	{
		getlblTotalCredit().setText(asCreditAmount);
	}
	/**
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aaLSE	ListSelectionEvent	 
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
					.get(gettblRefund().getSelectedRow()))
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
			setEnterAmount("");
		}
		else
		{
			setEnterAmount(laTempDollar.toString());
		}
	}
}
