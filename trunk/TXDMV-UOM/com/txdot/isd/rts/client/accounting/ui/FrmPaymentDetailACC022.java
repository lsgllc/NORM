package com.txdot.isd.rts.client.accounting.ui;

import java.awt.Dialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;
import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.cache.PaymentStatusCodesCache;
import com.txdot.isd.rts.services.cache.PaymentTypeCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.AccountingConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmPaymentDetailACC022.java 
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			06/12/2001  Created
 * MAbs			09/07/2001  Added comments
 * MAbs			04/18/2002	Global change for startWorking() and 
 * 							doneWorking()
 * MAbs			05/28/2002	Made trace number label same size as others  
 * 							CQU100004118
 * K Harrell	11/25/2003	Prevent non-Production machines from voiding 
 * 							Funds Remittance transactions. 
 *							modify actionPerformed() 
 * 							Defect 6343.  Ver 5.1.5 Fix 2
 * Min Wang 	03/29/2003	Fix 2 cursors ( one is on help button and 
 *							the other one is on void button) when back 
 *							at the Acc022 screen after completing the 
 *							void the remittance.
 *							modified actionPerformed(), setData()
 *							defect 6982 Ver 5.1.6
 * K Harrell	03/25/2004	JavaDoc Cleanup
 * 							Ver 5.2.0
 * Ray Rowehl	03/21/2005	use getters for controller
 * 							modify actionPerformed()
 * 							defect 7884 Ver 5.2.3
 * K Harrell	06/19/2005	Element renaming in FundsPaymentDataList
 * 							movement of services.cache.*Data to 
 * 							services.data 
 * 							modify setData(),getMatchingRecords()
 * 							defect 7899 Ver 5.2.3
 * K Harrell	07/22/2005	Java 1.4 Work
 * 							remove setNextFocusableComponent... 
 * 							defect 7884 Ver 5.2.3  
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/17/2005	Remove keylistener from buttonPanel
 * 							components. 
 * 							modify getbuttonPanel() 
 * 							defect 8240 Ver 5.2.3 
 * B Hargrove	10/24/2005	While testing this defect, found that Trace 
 * 							No and Payment Date were overlaying the 
 * 							labels. Changed layout from GridBag to Null.
 * 							modify getJPanel2()  
 * 							defect 6522 Ver 5.2.3
 * Ray Rowehl	05/05/2012	Modify the workstation check to use helper
 * 							classes.  This will still block access.
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
 * ACC022 is the payment detail screen in Funds Inquiry
 * 
 * @version 	RTS_700 06/20/2012
 * @author Michael Abernethy
 * @since 		06/12/2001
 */

public class FrmPaymentDetailACC022 extends RTSDialogBox implements
		ActionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JLabel ivjlblAccountSuffix = null;
	private JLabel ivjlblAmount = null;
	private JLabel ivjlblCheckNo = null;
	private JLabel ivjlblEmployeeId = null;
	private JLabel ivjlblPaymentDate = null;
	private JLabel ivjlblPaymentType = null;
	private JLabel ivjlblReceivedDate = null;
	private JLabel ivjlblStatus = null;
	private JLabel ivjlblTraceNo = null;
	private JLabel ivjstcLblAccountSuffix = null;
	private JLabel ivjstcLblAmount = null;
	private JLabel ivjstcLblCheckNo = null;
	private JLabel ivjstcLblEmployeeId = null;
	private JLabel ivjstcLblPaymentDate = null;
	private JLabel ivjstcLblPaymentType = null;
	private JLabel ivjstcLblReceivedDate = null;
	private JLabel ivjstcLblStatus = null;
	private JLabel ivjstcLblTraceNo = null;
	private JPanel ivjJInternalFrameContentPane = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSButton ivjbtnVoid = null;
	private RTSTable ivjtblPaymentDetail = null;
	private TMACC022 caTableModel;

	// boolean
	private boolean cbVoidIndi;

	// int
	private int ciTraceNo;

	// Object
	private FundsPaymentDataList caFundsPaymentDataList;

	// Vector
	private Vector cvTableData;

	private static final String ACCT_SUFFIX = "Account Suffix:";
	private static final String AMOUNT = "Amount:";
	private static final String BLANK_10 = "          ";
	private static final String BLANK_DT = "  /  /    ";
	private static final String CHECK = "CHECK";
	private static final String CHECK_NO = "Check No:";
	private static final String EMP_ID = "Employee Id:";
	private static final String MSG_BREACH = "Breach of secured environment detected.\nRemittance cancelled.";
	private static final String MSG_CANCEL = "Cancellation";
	private static final String MSG_CANCELLED = "Cancelled";
	private static final String MSG_VERIFY = "Verification";
	private static final String MSG_DYRWT = "Are you sure?";
	private static final String NEGATIVE_DOLLAR = "-1.00";
	private static final String PAYMENT_DT = "Payment Date:";
	private static final String PAYMENT_TYPE = " Payment Type:";
	private static final String PAYMENT_VOID = "Payment Voided.";
	private static final String PROD = "P";
	private static final String RECV_DT = "Received Date:";
	private static final String STATUS = "Status:";
	private static final String TITLE_ACC022 = "Payment Detail   ACC022";
	private static final String TRACE_NO = "Trace No:";
	private static final String VOID = "Void";
	private static final String VOID_REQ = "Void Request Cancelled";
	// defect 11320
	// private static final String ZERO = "0";
	// end defect 11320
	private static final String ZERO_DOLLAR = "0.00";

	/**
	 * FrmPaymentDetail constructor comment.
	 */
	public FrmPaymentDetailACC022()
	{
		super();
		initialize();
	}

	/**
	 * Creates a ACC022 with the parent
	 * 
	 * @param aaParent
	 *            Dialog
	 */
	public FrmPaymentDetailACC022(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Creates a ACC022 with the parent
	 * 
	 * @param aaParent
	 *            JFrame
	 */
	public FrmPaymentDetailACC022(JFrame aaParent)
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
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (aaAE.getSource() == getbuttonPanel().getBtnEnter())
			{
				HashMap lhmMap = new HashMap();
				lhmMap.put(AccountingConstant.DATA,
						caFundsPaymentDataList);
				getController().processData(
						AbstractViewController.ENTER, lhmMap);
			}
			else if (aaAE.getSource() == getbuttonPanel()
					.getBtnCancel())
			{
				Map laMap = new HashMap();
				laMap.put(AccountingConstant.DATA,
						caFundsPaymentDataList);
				getController().processData(
						AbstractViewController.CANCEL, laMap);
			}
			else if (aaAE.getSource() == getbuttonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.ACC022);
			}
			else if (aaAE.getSource() == getbtnVoid())
			{
				RTSException leRTSEx1 = new RTSException(
						RTSException.CTL001, MSG_DYRWT, MSG_VERIFY);
				int liChoice = leRTSEx1.displayError(this);
				if (liChoice == RTSException.YES)
				{
					// defect 6343
					// Restrictions on Funds Void
					// Do not allow if Production and HostName don't match
					// OfcIssuanceno + Substaid + WsId

					// defect 11320
					// All of this is not needed. Use the support
					// classes.
					// String lsHostName =
					// InetAddress.getLocalHost().getHostName();
					// int liWSID = SystemProperty.getWorkStationId();
					// String lsWSID =
					// Integer.toString(
					// SystemProperty.getWorkStationId());
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
					// int liSubStaId = SystemProperty.getSubStationId();
					// String lsSubStaId =
					// Integer.toString(
					// SystemProperty.getSubStationId());
					// AssignedWorkstationIdsData laAsgndWsId =
					// AssignedWorkstationIdsCache.getAsgndWsId(
					// liOfcIssuanceNo,
					// liSubStaId,
					// liWSID);
					
					AssignedWorkstationIdsData laAsgndWsId =
						 AssignedWorkstationIdsCache.getAsgndWsId(
								SystemProperty.getOfficeIssuanceNo(),
								SystemProperty.getSubStationId(),
								SystemProperty.getWorkStationId());

					if (!SystemProperty.isDevStatus())
					{
						if (laAsgndWsId.getProdStatusCd().equals(PROD) &&
								(!WorkstationInfo.isRTSWorkstation()
								|| WorkstationInfo
										.getOfficeIssuanceNo() != SystemProperty
										.getOfficeIssuanceNo()
								|| WorkstationInfo.getSubstaId() != SystemProperty
										.getSubStationId()
								|| WorkstationInfo.getWorkstationId() != SystemProperty
										.getWorkStationId()))
						{
							// end defect 11320
							RTSException leRTSEx2 = new RTSException(
									RTSException.WARNING_MESSAGE,
									MSG_BREACH, MSG_CANCEL);
							leRTSEx2.displayError(this);
							return;
						}
					}
					// end defect 6343
					// Create a FundsUpdateData to send to the MF
					cbVoidIndi = true;
					FundsPaymentData laSelectedData = (FundsPaymentData) cvTableData
							.get(gettblPaymentDetail().getSelectedRow());
					FundsUpdateData laUpdateData = new FundsUpdateData();
					laUpdateData.setComptCountyNo(SystemProperty
							.getOfficeIssuanceNo());
					laUpdateData.setFundsPaymentDate(RTSDate
							.getCurrentDate());
					laUpdateData
							.setAccountNoCd(Integer
									.parseInt(laSelectedData
											.getAccountNoCode()));
					laUpdateData
							.setCheckNo(laSelectedData.getCheckNo());
					laUpdateData.setTraceNo(Integer
							.parseInt(laSelectedData.getTraceNo()));
					laUpdateData.setTransEmpId(SystemProperty
							.getCurrentEmpId());
					laUpdateData.setOfcIssuanceNo(SystemProperty
							.getOfficeIssuanceNo());
					laUpdateData.setPaymentTypeCd(Integer
							.parseInt(laSelectedData
									.getPaymentTypeCode()));
					laUpdateData
							.setPaymentStatusCd(PaymentStatusCodesCache.VOIDED);
					Vector lvVector = new Vector();
					for (int i = 0; i < cvTableData.size(); i++)
					{
						FundsPaymentData laPayData = (FundsPaymentData) cvTableData
								.get(i);
						FundsDueData laDueData = new FundsDueData();
						laDueData.setFundsReportDate(laPayData
								.getFundsReportDate());
						laDueData.setReportingDate(laPayData
								.getReportingDate());
						Dollar laDollar = new Dollar(NEGATIVE_DOLLAR);
						laDueData.setRemitAmount(laPayData
								.getTotalPaymentAmount().multiply(
										laDollar));
						laDueData.setFundsCategory(laPayData
								.getFundsCategory());
						lvVector.add(laDueData);
					}
					laUpdateData.setFundsDue(lvVector);
					HashMap lhmMap = new HashMap();
					lhmMap.put(AccountingConstant.UPDATE_DATA,
							laUpdateData);

					// defect 6982
					getbuttonPanel().getBtnEnter().requestFocus();
					// end defect 6982

					getController().processData(
							AbstractViewController.VOID, lhmMap);
				}
				else if (liChoice == RTSException.NO)
				{
					RTSException leRTSEx3 = new RTSException(
							RTSException.WARNING_MESSAGE, VOID_REQ,
							MSG_CANCELLED);
					leRTSEx3.displayError(this);
					return;
				}
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
	 * Return the btnVoid property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnVoid()
	{
		if (ivjbtnVoid == null)
		{
			try
			{
				ivjbtnVoid = new RTSButton();
				ivjbtnVoid.setName("btnVoid");
				ivjbtnVoid.setMnemonic('V');
				ivjbtnVoid.setText(VOID);
				// user code begin {1}
				ivjbtnVoid.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnVoid;
	}

	/**
	 * Return the buttonPanel property value.
	 * 
	 * @return common.ButtonPanel
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
				ivjbuttonPanel.setBounds(37, 331, 257, 59);
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
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
	 * Returns the data object on this frame
	 * 
	 * @return Object
	 */
	public Object getData()
	{
		Map laMap = new HashMap();
		laMap.put(AccountingConstant.DATA, caFundsPaymentDataList);
		return laMap;
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
				ivjJInternalFrameContentPane
						.setName("JInternalFrameContentPane");
				ivjJInternalFrameContentPane.setLayout(null);
				getJInternalFrameContentPane().add(getJScrollPane1(),
						getJScrollPane1().getName());
				getJInternalFrameContentPane().add(getbuttonPanel(),
						getbuttonPanel().getName());
				getJInternalFrameContentPane().add(getJPanel1(),
						getJPanel1().getName());
				getJInternalFrameContentPane().add(getJPanel2(),
						getJPanel2().getName());
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new java.awt.FlowLayout());
				ivjJPanel1.setBounds(359, 331, 204, 59);
				getJPanel1().add(getbtnVoid(), getbtnVoid().getName());
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
		return ivjJPanel1;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				ivjJPanel2 = new javax.swing.JPanel();
				ivjJPanel2.setName("JPanel2");
				ivjJPanel2.setLayout(null);
				ivjJPanel2.add(getstcLblTraceNo(), null);
				ivjJPanel2.add(getlblTraceNo(), null);
				ivjJPanel2.add(getstcLblPaymentDate(), null);
				ivjJPanel2.add(getstcLblAccountSuffix(), null);
				ivjJPanel2.add(getstcLblAmount(), null);
				ivjJPanel2.add(getstcLblEmployeeId(), null);
				ivjJPanel2.add(getlblPaymentDate(), null);
				ivjJPanel2.add(getlblAccountSuffix(), null);
				ivjJPanel2.add(getlblAmount(), null);
				ivjJPanel2.add(getlblEmployeeId(), null);
				ivjJPanel2.add(getstcLblPaymentType(), null);
				ivjJPanel2.add(getstcLblStatus(), null);
				ivjJPanel2.add(getstcLblCheckNo(), null);
				ivjJPanel2.add(getstcLblReceivedDate(), null);
				ivjJPanel2.add(getlblPaymentType(), null);
				ivjJPanel2.add(getlblStatus(), null);
				ivjJPanel2.add(getlblCheckNo(), null);
				ivjJPanel2.add(getlblReceivedDate(), null);
				ivjJPanel2.setBounds(37, 12, 526, 139);

			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel2;
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
				ivjJScrollPane1
						.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1
						.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setBounds(37, 161, 526, 154);
				getJScrollPane1()
						.setViewportView(gettblPaymentDetail());
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
	 * Return the lblAccountSuffix property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblAccountSuffix()
	{
		if (ivjlblAccountSuffix == null)
		{
			try
			{
				ivjlblAccountSuffix = new javax.swing.JLabel();
				ivjlblAccountSuffix.setBounds(146, 60, 94, 16);
				ivjlblAccountSuffix.setName("lblAccountSuffix");
				ivjlblAccountSuffix.setText(BLANK_10);
				ivjlblAccountSuffix
						.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
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
		return ivjlblAccountSuffix;
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
				ivjlblAmount.setBounds(146, 87, 94, 16);
				ivjlblAmount.setName("lblAmount");
				ivjlblAmount.setText(BLANK_10);
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
		return ivjlblAmount;
	}

	/**
	 * Return the lblCheckNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblCheckNo()
	{
		if (ivjlblCheckNo == null)
		{
			try
			{
				ivjlblCheckNo = new javax.swing.JLabel();
				ivjlblCheckNo.setBounds(399, 87, 81, 16);
				ivjlblCheckNo.setName("lblCheckNo");
				ivjlblCheckNo.setText(BLANK_10);
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
		return ivjlblCheckNo;
	}

	/**
	 * Return the lblEmployeeId property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblEmployeeId()
	{
		if (ivjlblEmployeeId == null)
		{
			try
			{
				ivjlblEmployeeId = new javax.swing.JLabel();
				ivjlblEmployeeId.setBounds(146, 114, 94, 16);
				ivjlblEmployeeId.setName("lblEmployeeId");
				ivjlblEmployeeId.setText(BLANK_10);
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
		return ivjlblEmployeeId;
	}

	/**
	 * Return the lblPaymentDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblPaymentDate()
	{
		if (ivjlblPaymentDate == null)
		{
			try
			{
				ivjlblPaymentDate = new javax.swing.JLabel();
				ivjlblPaymentDate.setBounds(146, 33, 94, 16);
				ivjlblPaymentDate.setName("lblPaymentDate");
				ivjlblPaymentDate.setText(BLANK_10);
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
		return ivjlblPaymentDate;
	}

	/**
	 * Return the lblPaymentType property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblPaymentType()
	{
		if (ivjlblPaymentType == null)
		{
			try
			{
				ivjlblPaymentType = new javax.swing.JLabel();
				ivjlblPaymentType.setBounds(399, 33, 81, 16);
				ivjlblPaymentType.setName("lblPaymentType");
				ivjlblPaymentType.setText(BLANK_10);
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
		return ivjlblPaymentType;
	}

	/**
	 * Return the lblReceivedDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblReceivedDate()
	{
		if (ivjlblReceivedDate == null)
		{
			try
			{
				ivjlblReceivedDate = new javax.swing.JLabel();
				ivjlblReceivedDate.setBounds(399, 114, 81, 16);
				ivjlblReceivedDate.setName("lblReceivedDate");
				ivjlblReceivedDate.setText(BLANK_10);
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
		return ivjlblReceivedDate;
	}

	/**
	 * Return the lblStatus property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblStatus()
	{
		if (ivjlblStatus == null)
		{
			try
			{
				ivjlblStatus = new javax.swing.JLabel();
				ivjlblStatus.setBounds(399, 60, 81, 16);
				ivjlblStatus.setName("lblStatus");
				ivjlblStatus.setText(BLANK_10);
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
		return ivjlblStatus;
	}

	/**
	 * Return the lblTraceNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getlblTraceNo()
	{
		if (ivjlblTraceNo == null)
		{
			try
			{
				ivjlblTraceNo = new javax.swing.JLabel();
				ivjlblTraceNo.setBounds(146, 7, 94, 14);
				ivjlblTraceNo.setName("lblTraceNo");
				ivjlblTraceNo
						.setFont(new java.awt.Font("Arial", 1, 12));
				ivjlblTraceNo.setText("");
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
		return ivjlblTraceNo;
	}

	/**
	 * Return Vector of Matching Records
	 * 
	 * @return Vector
	 */
	private Vector getMatchingRecords()
	{
		Vector lvVector = new Vector();
		for (int i = 0; i < caFundsPaymentDataList.getFundsPymnt()
				.size(); i++)
		{
			FundsPaymentData laTempData = (FundsPaymentData) caFundsPaymentDataList
					.getFundsPymnt().get(i);
			if (Integer.parseInt(laTempData.getTraceNo()) == ciTraceNo)
			{
				lvVector.add(laTempData);
			}
		}
		return lvVector;
	}

	/**
	 * Return the stcLblAccountSuffix property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblAccountSuffix()
	{
		if (ivjstcLblAccountSuffix == null)
		{
			try
			{
				ivjstcLblAccountSuffix = new javax.swing.JLabel();
				ivjstcLblAccountSuffix.setBounds(20, 60, 113, 16);
				ivjstcLblAccountSuffix.setName("stcLblAccountSuffix");
				ivjstcLblAccountSuffix.setText(ACCT_SUFFIX);
				ivjstcLblAccountSuffix
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
		return ivjstcLblAccountSuffix;
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
				ivjstcLblAmount.setBounds(46, 87, 87, 16);
				ivjstcLblAmount.setName("stcLblAmount");
				ivjstcLblAmount.setText(AMOUNT);
				ivjstcLblAmount
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
		return ivjstcLblAmount;
	}

	/**
	 * Return the stcLblCheckNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblCheckNo()
	{
		if (ivjstcLblCheckNo == null)
		{
			try
			{
				ivjstcLblCheckNo = new javax.swing.JLabel();
				ivjstcLblCheckNo.setBounds(285, 87, 99, 16);
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
	 * Return the stcLblEmployeeId property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblEmployeeId()
	{
		if (ivjstcLblEmployeeId == null)
		{
			try
			{
				ivjstcLblEmployeeId = new javax.swing.JLabel();
				ivjstcLblEmployeeId.setBounds(52, 114, 81, 16);
				ivjstcLblEmployeeId.setName("stcLblEmployeeId");
				ivjstcLblEmployeeId.setText(EMP_ID);
				ivjstcLblEmployeeId
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
		return ivjstcLblEmployeeId;
	}

	/**
	 * Return the stcLblPaymentDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblPaymentDate()
	{
		if (ivjstcLblPaymentDate == null)
		{
			try
			{
				ivjstcLblPaymentDate = new javax.swing.JLabel();
				ivjstcLblPaymentDate.setBounds(21, 33, 112, 16);
				ivjstcLblPaymentDate.setName("stcLblPaymentDate");
				ivjstcLblPaymentDate.setText(PAYMENT_DT);
				ivjstcLblPaymentDate
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
		return ivjstcLblPaymentDate;
	}

	/**
	 * Return the stcLblPaymentType property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblPaymentType()
	{
		if (ivjstcLblPaymentType == null)
		{
			try
			{
				ivjstcLblPaymentType = new javax.swing.JLabel();
				ivjstcLblPaymentType.setBounds(269, 33, 115, 16);
				ivjstcLblPaymentType.setName("stcLblPaymentType");
				ivjstcLblPaymentType.setText(PAYMENT_TYPE);
				ivjstcLblPaymentType
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
		return ivjstcLblPaymentType;
	}

	/**
	 * Return the stcLblReceivedDate property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblReceivedDate()
	{
		if (ivjstcLblReceivedDate == null)
		{
			try
			{
				ivjstcLblReceivedDate = new javax.swing.JLabel();
				ivjstcLblReceivedDate.setBounds(286, 114, 98, 16);
				ivjstcLblReceivedDate.setName("stcLblReceivedDate");
				ivjstcLblReceivedDate.setText(RECV_DT);
				ivjstcLblReceivedDate
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
		return ivjstcLblReceivedDate;
	}

	/**
	 * Return the stcLblStatus property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblStatus()
	{
		if (ivjstcLblStatus == null)
		{
			try
			{
				ivjstcLblStatus = new javax.swing.JLabel();
				ivjstcLblStatus.setBounds(285, 60, 99, 16);
				ivjstcLblStatus.setName("stcLblStatus");
				ivjstcLblStatus.setText(STATUS);
				ivjstcLblStatus
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
		return ivjstcLblStatus;
	}

	/**
	 * Return the stcLblTraceNo property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JLabel getstcLblTraceNo()
	{
		if (ivjstcLblTraceNo == null)
		{
			try
			{
				ivjstcLblTraceNo = new javax.swing.JLabel();
				ivjstcLblTraceNo.setBounds(14, 7, 119, 15);
				ivjstcLblTraceNo.setName("stcLblTraceNo");
				ivjstcLblTraceNo.setFont(new java.awt.Font("Arial", 1,
						12));
				ivjstcLblTraceNo.setText(TRACE_NO);
				ivjstcLblTraceNo
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
		return ivjstcLblTraceNo;
	}

	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblPaymentDetail()
	{
		if (ivjtblPaymentDetail == null)
		{
			try
			{
				ivjtblPaymentDetail = new RTSTable();
				ivjtblPaymentDetail.setName("tblPaymentDetail");
				getJScrollPane1().setColumnHeaderView(
						ivjtblPaymentDetail.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
						JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblPaymentDetail
						.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblPaymentDetail
						.setModel(new com.txdot.isd.rts.client.accounting.ui.TMACC022());
				ivjtblPaymentDetail.setShowVerticalLines(false);
				ivjtblPaymentDetail.setShowHorizontalLines(false);
				ivjtblPaymentDetail
						.setIntercellSpacing(new java.awt.Dimension(0,
								0));
				ivjtblPaymentDetail.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caTableModel = (TMACC022) ivjtblPaymentDetail
						.getModel();
				TableColumn laTableColumnA = ivjtblPaymentDetail
						.getColumn(ivjtblPaymentDetail.getColumnName(0));
				laTableColumnA.setPreferredWidth(100);
				TableColumn laTableColumnB = ivjtblPaymentDetail
						.getColumn(ivjtblPaymentDetail.getColumnName(1));
				laTableColumnB.setPreferredWidth(100);
				TableColumn laTableColumnC = ivjtblPaymentDetail
						.getColumn(ivjtblPaymentDetail.getColumnName(2));
				laTableColumnC.setPreferredWidth(80);
				TableColumn laTableColumnD = ivjtblPaymentDetail
						.getColumn(ivjtblPaymentDetail.getColumnName(3));
				laTableColumnD.setPreferredWidth(160);
				ivjtblPaymentDetail
						.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				ivjtblPaymentDetail.init();
				laTableColumnA.setCellRenderer(ivjtblPaymentDetail
						.setColumnAlignment(RTSTable.CENTER));
				laTableColumnB.setCellRenderer(ivjtblPaymentDetail
						.setColumnAlignment(RTSTable.CENTER));
				laTableColumnC.setCellRenderer(ivjtblPaymentDetail
						.setColumnAlignment(RTSTable.RIGHT));
				laTableColumnD.setCellRenderer(ivjtblPaymentDetail
						.setColumnAlignment(RTSTable.LEFT));
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblPaymentDetail;
	}

	/**
	 * Return Total Amount
	 * 
	 * @return String
	 */
	private String getTotalAmount()
	{
		Dollar laDollar = new Dollar(ZERO_DOLLAR);
		for (int i = 0; i < cvTableData.size(); i++)
		{
			FundsPaymentData laFundPymntData = (FundsPaymentData) cvTableData
					.get(i);
			laDollar = laDollar.add(laFundPymntData
					.getTotalPaymentAmount());
		}
		return laDollar.toString();
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			setLocation(
					Toolkit.getDefaultToolkit().getScreenSize().width
							/ 2 - getSize().width / 2, Toolkit
							.getDefaultToolkit().getScreenSize().height
							/ 2 - getSize().height / 2);
			// user code end
			setName("FrmPaymentDetail");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(600, 425);
			setModal(true);
			setTitle(TITLE_ACC022);
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
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs
	 *            String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmPaymentDetailACC022 laFrmACC022 = new FrmPaymentDetailACC022();
			laFrmACC022.setVisible(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * sets the account suffix label
	 * 
	 * @param asAccountSuffix
	 *            String
	 */
	private void setAccountSuffix(String asAccountSuffix)
	{
		getlblAccountSuffix().setText(asAccountSuffix);
	}

	/**
	 * sets the amount label
	 * 
	 * @param asAmount
	 *            String
	 */
	private void setAmount(String asAmount)
	{
		getlblAmount().setText(asAmount);
	}

	/**
	 * sets the text in the check no label
	 * 
	 * @param asCheckNo
	 *            String
	 */
	private void setCheckNo(String asCheckNo)
	{
		getlblCheckNo().setText(asCheckNo);
	}

	/**
	 * all subclasses must implement this method - it sets the data on the
	 * screen and is how the controller relays information to the view
	 * 
	 * @param aaDataObject
	 *            Object
	 */
	public void setData(Object aaDataObject)
	{
		// All information will come in a Map
		HashMap lhmMap = (HashMap) aaDataObject;

		// Check if a void was performed on this screen - if yes, get
		// the data returned from the server
		if (cbVoidIndi == false)
		{
			caFundsPaymentDataList = (FundsPaymentDataList) (lhmMap
					.get(AccountingConstant.DATA));
		}
		// If the screen is showing up from the Trace Number selection
		// on KEY021
		if (lhmMap.get(AccountingConstant.TRACE_NO) != null)
		{
			ciTraceNo = ((Integer) lhmMap
					.get(AccountingConstant.TRACE_NO)).intValue();
		}
		else
		{
			ciTraceNo = Integer
					.parseInt(((FundsPaymentData) caFundsPaymentDataList
							.getFundsPymnt().get(0)).getTraceNo());
		}

		// If the MF said that the void was successful, update the data
		// object and change the payment status of the records
		if (lhmMap.get(AccountingConstant.PAYMENT_COMPLETE) != null)
		{
			RTSException leRTSEx = new RTSException(
					RTSException.INFORMATION_MESSAGE, PAYMENT_VOID,
					PAYMENT_VOID);
			leRTSEx.displayError(this);
			for (int i = 0; i < caFundsPaymentDataList.getFundsPymnt()
					.size(); i++)
			{
				FundsPaymentData laTempData = (FundsPaymentData) caFundsPaymentDataList
						.getFundsPymnt().get(i);
				if (Integer.parseInt(laTempData.getTraceNo()) == ciTraceNo)
				{
					laTempData
							.setPaymentStatusCode(PaymentStatusCodesCache.VOIDED);
				}
			}
			setDescFromCode(caFundsPaymentDataList.getFundsPymnt());
		}

		// Paint the screen with the appropriate information
		cvTableData = getMatchingRecords();
		caTableModel.add(cvTableData);

		if (cvTableData.size() > 0)
		{
			gettblPaymentDetail().setRowSelectionInterval(0, 0);
		}

		FundsPaymentData laBaseData = (FundsPaymentData) cvTableData
				.get(0);
		setTraceNo(laBaseData.getTraceNo());
		setPaymentDate(laBaseData.getFundsPaymentDate().toString());
		setAmount(getTotalAmount());
		setEmployeeId(laBaseData.getTransEmpId());

		PaymentTypeData laPymtData = PaymentTypeCache
				.getPymntTypeFromCd(Integer.parseInt(laBaseData
						.getPaymentTypeCode()));
		setPaymentType(laPymtData.getPymntTypeCdDesc());

		PaymentStatusCodesData laStatusData = PaymentStatusCodesCache
				.getPymntStatusCd(laBaseData.getPaymentStatusCode());
		setStatus(laStatusData.getPymntStatusDesc());

		if (getlblPaymentType().getText().trim().toUpperCase().equals(
				CHECK))
		{
			setAccountSuffix("");
		}
		else
		{
			setAccountSuffix(laBaseData.getAccountNoCode());
		}

		setCheckNo(laBaseData.getCheckNo());
		if (laBaseData.getFundsReceivedDate() == null)
		{
			setReceivedDate(BLANK_DT);
		}
		else
		{
			setReceivedDate(laBaseData.getFundsReceivedDate()
					.toString());
		}

		if (laStatusData.getPymntStatusCd().trim().toUpperCase()
				.equals(PaymentStatusCodesCache.REMITTED))
		{
			getbtnVoid().setEnabled(true);
		}
		else
		{
			getbtnVoid().setEnabled(false);
		}
	}

	/**
	 * Get the description from the code
	 * 
	 * @param avVector
	 *            Vector
	 */
	private void setDescFromCode(Vector avVector)
	{
		for (int i = 0; i < avVector.size(); i++)
		{
			FundsPaymentData laTempData = (FundsPaymentData) avVector
					.get(i);
			String lsCode = laTempData.getPaymentStatusCode();

			PaymentStatusCodesData laStatusData = PaymentStatusCodesCache
					.getPymntStatusCd(lsCode);

			laTempData.setPaymentStatusDesc(laStatusData
					.getPymntStatusDesc());
		}
	}

	/**
	 * sets the employee id label
	 * 
	 * @param asEmployeeId
	 *            String
	 */
	private void setEmployeeId(String asEmployeeId)
	{
		getlblEmployeeId().setText(asEmployeeId);
	}

	/**
	 * sets the payment date label
	 * 
	 * @param asPaymentDate
	 *            String
	 */
	private void setPaymentDate(String asPaymentDate)
	{
		getlblPaymentDate().setText(asPaymentDate);
	}

	/**
	 * sets the payment type label
	 * 
	 * @param asPaymentType
	 *            String
	 */
	private void setPaymentType(String asPaymentType)
	{
		getlblPaymentType().setText(asPaymentType);
	}

	/**
	 * sets the text in the received data label
	 * 
	 * @param asReceivedDate
	 *            String
	 */
	private void setReceivedDate(String asReceivedDate)
	{
		getlblReceivedDate().setText(asReceivedDate);
	}

	/**
	 * sets the status label
	 * 
	 * @param asStatus
	 */
	private void setStatus(String asStatus)
	{
		getlblStatus().setText(asStatus);
	}

	/**
	 * sets the trace number label
	 * 
	 * @param asTraceNo
	 *            String
	 */
	private void setTraceNo(String asTraceNo)
	{
		getlblTraceNo().setText(asTraceNo);
	}
}
