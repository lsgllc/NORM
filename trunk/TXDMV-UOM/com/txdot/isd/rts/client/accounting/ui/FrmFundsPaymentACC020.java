package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.cache.PaymentStatusCodesCache;
import com.txdot.isd.rts.services.cache.PaymentTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmFundsPaymentACC020.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Abernethy	09/07/2001  Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking() 
 * RHicks   	07/29/2002  Add help desk support
 * S Govindappa	08/21/2002	defect 4653
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 * 							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Made changes for the user help guide so had 
 * 							to make changes in actionPerformed().
 *							defect 5147 
 * K Harrell /
 * S Govindappa 03/19/2003 	Restrictions on Funds Remittance 
 * 							defect 5838
 * K Harrell    05/16/2003 	Need to pad WsId, OfcIssuanceNo to 3 
 * 							characters for comparison against Host Name.
 *                         	modify actionPerformed()
 * 							defect 6006 Funds Remittance - 
 * J Zwiener	12/30/2003  Do not allow spaces in Check No field.
 *                          Changed the input property for txtCheckNo 
 * 							bean from a "4" to a "6".
 *                          modify gettxtCheckNo()
 *                         	defect 6678  Ver 5.1.5 Fix 2
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	06/20/2005	renaming of elements within 
 * 							FundsDueDataList Object
 * 							movement of services.cache.*Data to 
 * 							services.data 
 * 							modify actionPerformed(),setData()
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/22/2005	Java 1.4 Work
 * 							defect 7884 Ver 5.2.3   
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/17/2005	Remove keylistener from buttonPanel
 * 							components. 
 * 							modify getbuttonPanel() 
 * 							defect 8240 Ver 5.2.3  
 * Jeff S.		12/14/2005	Added a temp fix for the JComboBox problem.
 * 							modify populateComboBox()
 * 							defect 8479 Ver 5.2.3
 * K Harrell	12/14/2005	Arrow keys were selecting
 * 							modify keyPressed()
 * 							defect 7899 Ver 5.2.3  
 * Jeff S.		12/20/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all of the arrowing.
 * 							removed keyPressed(), focusGained(), 
 * 								focusLost()
 * 							modify getradioEFT(), getradioCheck(), 
 * 								getJPanel1(), setRadioButtons()
 * 							defect 7884 Ver 5.2.3
 * Ray Rowehl	05/05/2012	Use support classes to figure out if
 * 							this is a valid workstation.
 * 							delete PROD, ZERO
 * 							delete getBuilderData()
 * 							modify actionPerformed()
 * 							defect 11320 Ver RTS_700
 * Ray Rowehl	06/20/2012	Go back and add the assigned work station
 * 							ids check of ProdStatusCd.
 * 							add PROD
 * 							modify actionPerformed()
 * 							defect 11320 Ver RTS_700
 * --------------------------------------------------------------------- 
 */

/**
 * ACC020 is the payment screen for Funds Remittance
 * 
 * @version RTS_700 06/20/2012
 * @author Michael Abernethy
 * @since 06/12/2001 10:16:23
 */

public class FrmFundsPaymentACC020 extends RTSDialogBox implements
		ActionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JComboBox ivjcomboEFT = null;
	private JLabel ivjlblAmountToRemit = null;
	private JLabel ivjstclblAmountToRemit = null;
	private JLabel ivjstcLblCheckNo = null;
	private JPanel ivjJInternalFrameContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JRadioButton ivjradioCheck = null;
	private JRadioButton ivjradioEFT = null;
	private RTSInputField ivjtxtCheckNo = null;
	private int ciEFTAccountCode;
	private FundsDueDataList caFundsDueDataList;
	private final static String CHECK = "Check";
	private final static String CHECK_NO = "Check No:";
	private final static String DEFLT_AMT = "0000000000.00";
	private final static String EFT = "EFT";
	private final static String MSG_BREACH = "Breach of secured environment detected.\nRemittance cancelled.";
	private final static String MSG_CANCEL = "Cancellation";
	private final static String MSG_DYRWT = "Are you sure?";
	private final static String MSG_CONFIRM = "Remit Funds Confirmation";
	private final static String PROD = "P";
	private final static String SEL_METHOD = "Select Method of Payment";
	private final static String TITLE_ACC020 = "Funds Payment   ACC020";
	private final static String TOTAL_AMT = "Total Amount to Remit:";

	// defect 11320
	// private final static String ZERO = "0";
	// end defect 11320

	/**
	 * FrmFundsPayment constructor comment.
	 */
	public FrmFundsPaymentACC020()
	{
		super();
		initialize();
	}

	/**
	 * Creates a ACC020 with the parent
	 * 
	 * @param aaParent
	 *            Dialog
	 */
	public FrmFundsPaymentACC020(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Creates a ACC020 with the parent
	 * 
	 * @param aaParent
	 *            JFrame
	 */
	public FrmFundsPaymentACC020(JFrame aaParent)
	{
		super(aaParent);
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
		if (aaAE.getSource() instanceof JRadioButton)
		{
			setRadioButtons();
			return;
		}
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				RTSException leRTSException1 = new RTSException();
				if (!isEFTSelected()
						&& getCheckNo().equals(
								CommonConstant.STR_SPACE_EMPTY))
				{
					leRTSException1.addException(new RTSException(150),
							gettxtCheckNo());
				}
				if (leRTSException1.isValidationError())
				{
					leRTSException1.displayError(this);
					leRTSException1.getFirstComponent().requestFocus();
					return;
				}
				RTSException leRTSException2 = new RTSException(
						RTSException.CTL001, MSG_DYRWT, MSG_CONFIRM);
				int liResponse = leRTSException2.displayError(this);
				if (liResponse != RTSException.YES)
					return;
				// CQU100005838 Restrictions on Funds Remittance 3/19/2003
				// Do not allow if Production and HostName doesn't match
				// OfcIssuanceno + Substaid + WsId

				// defect 11320
				// String lsHostName =
				// InetAddress.getLocalHost().getHostName();
				// int liWSID = SystemProperty.getWorkStationId();
				// defect 6006
				// Need to augment lsWSId, lsOfcIssuanceNo to 3 characters
				// String lsWSID =
				// Integer.toString(SystemProperty.getWorkStationId());
				// while (lsWSID.length() < 3)
				// {
				// lsWSID = ZERO + lsWSID;
				// }
				// int liOfcIssuanceNo =
				// SystemProperty.getOfficeIssuanceNo();
				// String lsOfcIssuanceNo =
				// Integer.toString(
				// SystemProperty.getOfficeIssuanceNo());
				// while (lsOfcIssuanceNo.length() < 3)
				// {
				// lsOfcIssuanceNo = ZERO + lsOfcIssuanceNo;
				// }
				// end defect 6006
				// int liSubStaId = SystemProperty.getSubStationId();
				// String lsSubStaId =
				// Integer.toString(SystemProperty.getSubStationId());
				// AssignedWorkstationIdsData laAsgndWsId =
				// AssignedWorkstationIdsCache.getAsgndWsId(
				// liOfcIssuanceNo,
				// liSubStaId,
				// liWSID);
				// if (laAsgndWsId.getProdStatusCd().equals(PROD))
				// {
				// if ((lsHostName.length() != 8)
				// || !lsHostName.substring(1, 4).equals(
				// lsOfcIssuanceNo)
				// || !lsHostName.substring(4, 5).equals(lsSubStaId)
				// || !lsHostName.substring(5, 8).equals(lsWSID))
				// {
				//

				AssignedWorkstationIdsData laAsgndWsId =
					 AssignedWorkstationIdsCache.getAsgndWsId(
							SystemProperty.getOfficeIssuanceNo(),
							SystemProperty.getSubStationId(),
							SystemProperty.getWorkStationId());
				
				if (!SystemProperty.isDevStatus())
				{
					if (laAsgndWsId.getProdStatusCd().equals(PROD) &&
							(!WorkstationInfo.isRTSWorkstation()
							|| WorkstationInfo.getOfficeIssuanceNo() != SystemProperty
									.getOfficeIssuanceNo()
							|| WorkstationInfo.getSubstaId() != SystemProperty
									.getSubStationId()
							|| WorkstationInfo.getWorkstationId() != SystemProperty
									.getWorkStationId()))
					{
						// end defect 11320
						RTSException leRTSException3 = new RTSException(
								RTSException.WARNING_MESSAGE,
								MSG_BREACH, MSG_CANCEL);
						leRTSException3.displayError(this);
						return;
					}
				}
				FundsUpdateData laUpdateData = new FundsUpdateData();
				laUpdateData.setComptCountyNo(SystemProperty
						.getOfficeIssuanceNo());
				laUpdateData.setFundsPaymentDate(RTSDate
						.getCurrentDate());
				if (isEFTSelected())
				{
					PaymentAccountData laPymntAcctData = (PaymentAccountData) caFundsDueDataList
							.getPaymentAccounts().get(
									getcomboEFT().getSelectedIndex());
					laUpdateData.setAccountNoCd(Integer
							.parseInt(laPymntAcctData.getPymntLocId()));
				}
				else
				{
					laUpdateData.setAccountNoCd(0);
				}
				laUpdateData.setTraceNo(0);
				laUpdateData.setTransEmpId(SystemProperty
						.getCurrentEmpId());
				laUpdateData.setOfcIssuanceNo(SystemProperty
						.getOfficeIssuanceNo());
				if (isEFTSelected())
				{
					laUpdateData.setPaymentTypeCd(PaymentTypeCache.EFT);
				}
				else
				{
					laUpdateData
							.setPaymentTypeCd(PaymentTypeCache.CHECK);
				}
				laUpdateData
						.setPaymentStatusCd(PaymentStatusCodesCache.REMITTED);
				laUpdateData.setCheckNo(getCheckNo().toUpperCase());
				Vector lvVector = new Vector();
				for (int i = 0; i < caFundsDueDataList.getFundsDue()
						.size(); i++)
				{
					FundsDueData laTempData = (FundsDueData) caFundsDueDataList
							.getFundsDue().get(i);
					if (Double.parseDouble(laTempData.getRemitAmount()
							.toString()) != 0.0)
					{
						lvVector.add(laTempData);
					}
				}
				laUpdateData.setFundsDue(lvVector);
				getController().processData(
						AbstractViewController.ENTER, laUpdateData);
			}
			else if (aaAE.getSource() == getbuttonPanel()
					.getBtnCancel())
			{
				getController().processData(
						AbstractViewController.CANCEL,
						caFundsDueDataList);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.ACC020);
			}
		}
		catch (Exception aeExcp)
		{
			aeExcp.printStackTrace();
			return;
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Sets the total amount to remit
	 * 
	 * @return String
	 */
	private String getAmountToRemit()
	{
		Dollar laDollarAmtToRemit = new Dollar("0.00");
		for (int i = 0; i < caFundsDueDataList.getFundsDue().size(); i++)
		{
			FundsDueData laFundsDueData = (FundsDueData) caFundsDueDataList
					.getFundsDue().get(i);
			laDollarAmtToRemit = laDollarAmtToRemit.add(laFundsDueData
					.getRemitAmount());
		}
		return laDollarAmtToRemit.toString();
	}

	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return ButtonPanel
	 */
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
				// ivjbuttonPanel.getBtnEnter().addKeyListener(this);
				// ivjbuttonPanel.getBtnCancel().addKeyListener(this);
				// ivjbuttonPanel.getBtnHelp().addKeyListener(this);
				// end defect 8240
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
	 * returns the text in the check no text field
	 * 
	 * @return String
	 */
	private String getCheckNo()
	{
		return gettxtCheckNo().getText();
	}

	/**
	 * Return the comboEFT property value.
	 * 
	 * @return javax.swing.JComboBox
	 */
	private javax.swing.JComboBox getcomboEFT()
	{
		if (ivjcomboEFT == null)
		{
			try
			{
				ivjcomboEFT = new javax.swing.JComboBox();
				ivjcomboEFT.setName("comboEFT");
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
		return ivjcomboEFT;
	}

	/**
	 * Return the JInternalFrameContentPane property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJInternalFrameContentPane()
	{
		if (ivjJInternalFrameContentPane == null)
		{
			try
			{
				ivjJInternalFrameContentPane = new javax.swing.JPanel();
				ivjJInternalFrameContentPane
						.setName("JInternalFrameContentPane");
				ivjJInternalFrameContentPane
						.setLayout(new java.awt.GridBagLayout());
				java.awt.GridBagConstraints constraintsstclblAmountToRemit = new java.awt.GridBagConstraints();
				constraintsstclblAmountToRemit.gridx = 1;
				constraintsstclblAmountToRemit.gridy = 1;
				constraintsstclblAmountToRemit.ipadx = 3;
				constraintsstclblAmountToRemit.insets = new java.awt.Insets(
						15, 68, 5, 9);
				getJInternalFrameContentPane().add(
						getstclblAmountToRemit(),
						constraintsstclblAmountToRemit);
				java.awt.GridBagConstraints constraintslblAmountToRemit = new java.awt.GridBagConstraints();
				constraintslblAmountToRemit.gridx = 2;
				constraintslblAmountToRemit.gridy = 1;
				constraintslblAmountToRemit.ipadx = 3;
				constraintslblAmountToRemit.insets = new java.awt.Insets(
						15, 9, 5, 93);
				getJInternalFrameContentPane().add(
						getlblAmountToRemit(),
						constraintslblAmountToRemit);
				java.awt.GridBagConstraints constraintsJPanel1 = new java.awt.GridBagConstraints();
				constraintsJPanel1.gridx = 1;
				constraintsJPanel1.gridy = 2;
				constraintsJPanel1.gridwidth = 2;
				constraintsJPanel1.fill = java.awt.GridBagConstraints.BOTH;
				constraintsJPanel1.weightx = 1.0;
				constraintsJPanel1.weighty = 1.0;
				constraintsJPanel1.insets = new java.awt.Insets(6, 19,
						5, 17);
				getJInternalFrameContentPane().add(getJPanel1(),
						constraintsJPanel1);
				java.awt.GridBagConstraints constraintsbuttonPanel = new java.awt.GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 3;
				constraintsbuttonPanel.gridwidth = 2;
				constraintsbuttonPanel.fill = java.awt.GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 147;
				constraintsbuttonPanel.ipady = 26;
				constraintsbuttonPanel.insets = new java.awt.Insets(6,
						19, 4, 17);
				getJInternalFrameContentPane().add(getbuttonPanel(),
						constraintsbuttonPanel);
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
	 * Return the JPanel1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());
				ivjJPanel1.setEnabled(true);
				java.awt.GridBagConstraints constraintsradioEFT = new java.awt.GridBagConstraints();
				constraintsradioEFT.gridx = 1;
				constraintsradioEFT.gridy = 1;
				constraintsradioEFT.ipadx = 9;
				constraintsradioEFT.insets = new java.awt.Insets(21,
						22, 11, 27);
				getJPanel1().add(getradioEFT(), constraintsradioEFT);
				java.awt.GridBagConstraints constraintsradioCheck = new java.awt.GridBagConstraints();
				constraintsradioCheck.gridx = 1;
				constraintsradioCheck.gridy = 2;
				constraintsradioCheck.ipadx = 7;
				constraintsradioCheck.insets = new java.awt.Insets(10,
						22, 23, 13);
				getJPanel1()
						.add(getradioCheck(), constraintsradioCheck);
				java.awt.GridBagConstraints constraintsstcLblCheckNo = new java.awt.GridBagConstraints();
				constraintsstcLblCheckNo.gridx = 2;
				constraintsstcLblCheckNo.gridy = 2;
				constraintsstcLblCheckNo.ipadx = 4;
				constraintsstcLblCheckNo.insets = new java.awt.Insets(
						14, 84, 27, 8);
				getJPanel1().add(getstcLblCheckNo(),
						constraintsstcLblCheckNo);
				java.awt.GridBagConstraints constraintstxtCheckNo = new java.awt.GridBagConstraints();
				constraintstxtCheckNo.gridx = 3;
				constraintstxtCheckNo.gridy = 2;
				constraintstxtCheckNo.fill = java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtCheckNo.weightx = 1.0;
				constraintstxtCheckNo.ipadx = 82;
				constraintstxtCheckNo.insets = new java.awt.Insets(11,
						9, 24, 13);
				getJPanel1()
						.add(gettxtCheckNo(), constraintstxtCheckNo);
				java.awt.GridBagConstraints constraintscomboEFT = new java.awt.GridBagConstraints();
				constraintscomboEFT.gridx = 2;
				constraintscomboEFT.gridy = 1;
				constraintscomboEFT.gridwidth = 2;
				constraintscomboEFT.fill = java.awt.GridBagConstraints.HORIZONTAL;
				constraintscomboEFT.weightx = 1.0;
				constraintscomboEFT.ipadx = 108;
				constraintscomboEFT.insets = new java.awt.Insets(21,
						14, 10, 13);
				getJPanel1().add(getcomboEFT(), constraintscomboEFT);
				// user code begin {1}
				Border b = new TitledBorder(new EtchedBorder(),
						SEL_METHOD);
				getJPanel1().setBorder(b);
				// defect 7884
				// Changed from ButtonGroup to RTSButtonGroup
				RTSButtonGroup laButtonGroup = new RTSButtonGroup();
				laButtonGroup.add(getradioCheck());
				laButtonGroup.add(getradioEFT());
				// end defect 7884
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the JLabel2 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getlblAmountToRemit()
	{
		if (ivjlblAmountToRemit == null)
		{
			try
			{
				ivjlblAmountToRemit = new javax.swing.JLabel();
				ivjlblAmountToRemit.setName("lblAmountToRemit");
				ivjlblAmountToRemit.setText(DEFLT_AMT);
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
		return ivjlblAmountToRemit;
	}

	/**
	 * Return the JRadioButton2 property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getradioCheck()
	{
		if (ivjradioCheck == null)
		{
			try
			{
				ivjradioCheck = new javax.swing.JRadioButton();
				ivjradioCheck.setName("radioCheck");
				ivjradioCheck.setMnemonic(KeyEvent.VK_K);
				ivjradioCheck.setText(CHECK);
				// user code begin {1}
				ivjradioCheck.addActionListener(this);
				// defect 7884
				// ivjradioCheck.addKeyListener(this);
				// ivjradioCheck.addFocusListener(this);
				// end defect 7884

				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioCheck;
	}

	/**
	 * Return the JRadioButton1 property value.
	 * 
	 * @return javax.swing.JRadioButton
	 */
	private javax.swing.JRadioButton getradioEFT()
	{
		if (ivjradioEFT == null)
		{
			try
			{
				ivjradioEFT = new javax.swing.JRadioButton();
				ivjradioEFT.setName("radioEFT");
				ivjradioEFT.setSelected(false);
				ivjradioEFT.setMnemonic(KeyEvent.VK_F);
				ivjradioEFT.setText(EFT);
				// user code begin {1}
				ivjradioEFT.addActionListener(this);
				// defect 7884
				// ivjradioEFT.addKeyListener(this);
				// ivjradioEFT.addFocusListener(this);
				// end defect 7884
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioEFT;
	}

	/**
	 * Return the JLabel1 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstclblAmountToRemit()
	{
		if (ivjstclblAmountToRemit == null)
		{
			try
			{
				ivjstclblAmountToRemit = new javax.swing.JLabel();
				ivjstclblAmountToRemit.setName("stclblAmountToRemit");
				ivjstclblAmountToRemit.setText(TOTAL_AMT);
				ivjstclblAmountToRemit
						.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
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
		return ivjstclblAmountToRemit;
	}

	/**
	 * Return the JLabel4 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblCheckNo()
	{
		if (ivjstcLblCheckNo == null)
		{
			try
			{
				ivjstcLblCheckNo = new javax.swing.JLabel();
				ivjstcLblCheckNo.setName("stcLblCheckNo");
				ivjstcLblCheckNo.setText(CHECK_NO);
				ivjstcLblCheckNo
						.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
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
		return ivjstcLblCheckNo;
	}

	/**
	 * Return the txtCheckNo property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtCheckNo()
	{
		if (ivjtxtCheckNo == null)
		{
			try
			{
				ivjtxtCheckNo = new RTSInputField();
				ivjtxtCheckNo.setName("txtCheckNo");
				ivjtxtCheckNo.setInput(6);
				ivjtxtCheckNo.setText(CommonConstant.STR_SPACE_EMPTY);
				ivjtxtCheckNo.setMaxLength(7);
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
		return ivjtxtCheckNo;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException
	 *            Throwable
	 */
	private void handleException(Throwable aeException)
	{
		RTSException leRTSEx = new RTSException(
				RTSException.JAVA_ERROR, (Exception) aeException);
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
			setName("FrmFundsPayment");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(400, 225);
			setModal(true);
			setTitle(TITLE_ACC020);
			setContentPane(getJInternalFrameContentPane());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		if (ciEFTAccountCode == 0)
			getradioCheck().setSelected(true);
		else
			getradioEFT().setSelected(true);
		setRadioButtons();
		// user code end
	}

	/**
	 * returns true if the EFT radio button is selected
	 * 
	 * @return boolean
	 */
	private boolean isEFTSelected()
	{
		return getradioEFT().isSelected();
	}

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs
	 *            String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmFundsPaymentACC020 aaFrmACC020 = new FrmFundsPaymentACC020();
			aaFrmACC020.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * Populate Combo Box
	 * 
	 * @param avChoices
	 *            Vector
	 */
	private void populateComboBox(Vector avChoices)
	{
		for (int i = 0; i < avChoices.size(); i++)
		{
			PaymentAccountData tempData = (PaymentAccountData) avChoices
					.get(i);
			getcomboEFT().addItem(tempData.getPymntLocDesc());
		}
		// defect 8479
		comboBoxHotKeyFix(getcomboEFT());
		// end defect 8479
	}

	/**
	 * all subclasses must implement this method - it sets the data on the
	 * screen and is how the controller relays information to the view
	 * 
	 * @param aaObject
	 *            Object
	 */
	public void setData(Object aaObject)
	{
		caFundsDueDataList = (FundsDueDataList) aaObject;
		setTotalAmountToRemit(getAmountToRemit());
		if (caFundsDueDataList.getPaymentAccounts().size() > 0)
		{
			populateComboBox(caFundsDueDataList.getPaymentAccounts());
		}
		else
		{
			getradioEFT().setSelected(false);
		}
		int liOfcIssuanceNo = SystemProperty.getOfficeIssuanceNo();
		ciEFTAccountCode = OfficeIdsCache.getOfcId(liOfcIssuanceNo)
				.getEFTAccntCd();
		if (ciEFTAccountCode != 0)
		{
			getradioEFT().setSelected(true);
		}
		else
		{
			getradioEFT().setEnabled(false);
			getcomboEFT().setEnabled(false);
		}
		setRadioButtons();
	}

	/**
	 * Sets the radio button selections.
	 */
	private void setRadioButtons()
	{
		getcomboEFT().setEnabled(getradioEFT().isSelected());
		gettxtCheckNo().setEnabled(getradioCheck().isSelected());
		getstcLblCheckNo().setEnabled(getradioCheck().isSelected());

		// defect 7884
		// Moved from focus gained so that focus listener was not needed
		// anymore
		if (getradioEFT().isSelected())
		{
			gettxtCheckNo().setText(CommonConstant.STR_SPACE_EMPTY);
		}
		// end defect 7884
	}

	/**
	 * sets the text for the total amount label
	 * 
	 * @param asAmount
	 *            String
	 */
	private void setTotalAmountToRemit(String asAmount)
	{
		getlblAmountToRemit().setText(asAmount);
	}
}
