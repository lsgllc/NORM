package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.ImageTableCellRenderer;
import com.txdot.isd.rts.services.data.InventoryAllocationUIData;
import com.txdot.isd.rts.services.data.MFInventoryAllocationData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmModifyInvoiceINV002.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Kwik 		05/08/2002	Fix defect 3796 - make window and item 
 * 	& M Wang				desc wider.
 * J Kwik		05/15/2002	Fix defect CQU100003931 - enable alt+t to put
 * 							focus in inv items table.
 * J Kwik		05/21/2002	Defect CQU100003931(cont.) - hide the 
 * 							underscore from the letter 't' for the label
 * 							'destination'.
 * M Wang		01/17/2003	Fix defect CQU100005268 - Modified rcveInvc().
 * M Wang		04/02/2004  Modify visual composition to allow all of
 *							Selected item detailed status to appear. the 
 *                          bottom of this message was cut off.
 *							defect 6999 Ver 5.1.6 
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							add handlePrintableInventory(), 
 * 							  handlePrintableDelete()
 *							modify actionPerformed(), setData()
 *	 						Ver 5.2.0
 * K Harrell	06/21/2004	remove check for printableindi from the
 *							client.
 *							deprecate handlePrintableDelete()
 *							deprecate handlePrintableInventory()
 *							modify actionPerformed(), setData()
 *							when remove, also delete
 *							cbContainsPrintableInventory 
 *							defect 7222 Ver 5.2.0
 * K Harrell	11/12/2004	disable editing for inventory status
 *							ivjlblSelctdItmDetailedStatus.setEditable(
 *							false)via Visual Composition
 *							defect 7508 Ver 5.2.2 
 * Ray Rowehl	02/21/2005	RTS 5.2.3 Code Cleanup
 * 							organize imports, format source,
 * 							rename fields
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * K Harrell	06/20/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3 
 * Min Wang		08/01/2005	Remove item code from screen.
 * 							modify gettblInvcItms()
 * 							defect 8269 Ver 5.2.2 Fix 6   
 * Ray Rowehl	08/06/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Organize imports.
 * 							Remove Key actions from button panel buttons.
 * 							Work on constants.
 * 							working on Print option.
 * 							defect 7890 Ver 5.2.3  
 * Ray Rowehl	08/12/2005	Cleanup pass.
 * 							Work on constants.
 * 							Remove key processing from button panel.
 * 							defect 7890 Ver 5.2.3	
 * Ray Rowehl	08/15/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/29/2005	Limit Report Text to 110 characters.
 * 							add LENGTH_INV002_REPORT_TEXT
 * 							modify gettxtRptTxt()
 * 							defect 8335 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3	
 * T. Pederson	12/21/2005	Moved setting default focus to setData.
 * 							delete windowOpened()
 * 							modify setData() 	
 * 							defect 8494 Ver 5.2.3
 * Jeff S		02/08/2006	Fixed problem with default focus that was 
 * 							missed with the previous change.
 * 							modify setData()
 * 							defect 8494 Ver 5.2.3 
 * Min Wang		03/27/2008	Lost focus on INV002 and hot keys not 
 * 							working.
 * 							modify setData()
 * 							defect 8949,9518 Ver Defect_POS_A
 * K Harrell	08/25/2009	Implement ReportSearchData.initForClient()
 * 							modify actionPerformed()
 * 							defect 8628 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * In the Receive Invoice event, frame INV002 displays all the 
 * inventory items on an invoice and allows for invoice items to be 
 * added, modified, and deleted from the invoice.
 *
 * @version	Defect_POS_F	08/25/2009
 * @author	Charlie Walker
 * <br>Creation Date:	08/03/2001 16:09:52 
 */

public class FrmModifyInvoiceINV002
	extends RTSDialogBox
	implements ActionListener, KeyListener, ListSelectionListener
{
	/**
	 * Amount to increase font by for display.
	 */
	private static final int FONT_INCREASE = 2;
	
	// defect 8335
	private static final int LENGTH_INV002_REPORT_TEXT = 110;
	// end defct 8335

	private static final String TEXT_SAMPLE_ORDER_DATE = "11/11/2001";
	private static final String TEXT_SAMPLE_INVCNO = "Z16100";
	private static final String TEXT_SAMPLE_DEST = "MCCLENNAN COUNTY";
	private static final String TEXT_SELCTDITMDETAILEDSTATUS_SAMPLE =
		"ITEM ADDED OK.";

	private RTSButton ivjbtnAdd = null;
	private RTSButton ivjbtnDelete = null;
	private RTSButton ivjbtnModify = null;
	private JPanel ivjFrmModifyInvoiceINV002ContentPane1 = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JPanel ivjpnlBackground = null;
	private TMINV002 caTableModel = null;
	private JCheckBox ivjchkViewInvRcvdRpt = null;
	private JLabel ivjlblDest = null;
	private JLabel ivjlblInvcNo = null;
	private JLabel ivjlblOrderDt = null;
	private JLabel ivjlblRcveInto = null;
	private RTSTextArea ivjlblSelctdItmDetailedStatus = null;
	private JLabel ivjstcLblDest = null;
	private JLabel ivjstcLblInvcNo = null;
	private JLabel ivjstcLblOrderDt = null;
	private JLabel ivjstcLblRcveInto = null;
	private JLabel ivjstcLblRptTxt = null;
	private JLabel ivjstcLblSelctdItmDetailedStatus = null;
	private RTSInputField ivjtxtRptTxt = null;
	private RTSTable ivjtblInvcItms = null;

	/**
	 * InventoryAllocationUIData object used to collect the UI data
	 */
	private MFInventoryAllocationData caMFInvAlloctnData =
		new MFInventoryAllocationData();

	/**
	 * Vector used to store the Substations for the combo box
	 */
	private Vector cvSubstaData = new Vector();

	/**
	 * FrmModifyInvoiceINV002 constructor comment.
	 */
	public FrmModifyInvoiceINV002()
	{
		super();
		initialize();
	}

	/**
	 * FrmModifyInvoiceINV002 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmModifyInvoiceINV002(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmModifyInvoiceINV002 constructor comment.
	 * 
	 * @param parent JFrame
	 */
	public FrmModifyInvoiceINV002(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		// Code to prevent multiple button clicks
		if (!startWorking())
		{
			return;
		}
		try
		{
			// defect 8269
			//if (aaAE.getSource() instanceof RTSTable)
			//{	
			//	//int i = 0;
			//}
			// end defect 8269
			// Determines what actions to take when Allocate, Enter, 
			// Cancel, or Help are pressed.
			if (aaAE.getSource() == getbtnAdd())
			{
				caMFInvAlloctnData.setCalcInv(false);
				Vector lvDataOut = new Vector();
				lvDataOut.addElement(cvSubstaData);
				lvDataOut.addElement(caMFInvAlloctnData);
				getController().processData(
					VCModifyInvoiceINV002.ADD,
					lvDataOut);
			}
			else if (aaAE.getSource() == getbtnModify())
			{
				captureUserInput();
				caMFInvAlloctnData.setCalcInv(false);
				Vector lvDataOut = new Vector();
				lvDataOut.addElement(cvSubstaData);
				lvDataOut.addElement(caMFInvAlloctnData);
				getController().processData(
					VCModifyInvoiceINV002.MODIFY,
					lvDataOut);
			}
			else if (aaAE.getSource() == getbtnDelete())
			{
				captureUserInput();
				caMFInvAlloctnData.setCalcInv(false);

				// Test if the item is already rejected or user deleted
				int liSelctdRowIndx =
					caMFInvAlloctnData.getSelctdRowIndx();
				InventoryAllocationUIData laIAUID =
					(InventoryAllocationUIData) caMFInvAlloctnData
						.getInvAlloctnData()
						.get(
						liSelctdRowIndx);
				if (laIAUID
					.getStatusCd()
					.equals(InventoryConstant.DELETE))
				{
					RTSException leRTSExMsg =
						new RTSException(
							RTSException.WARNING_MESSAGE,
							ErrorsConstant.ERR_MSG_ITEM_REJ_OR_DEL_ALREADY,
							null);
					leRTSExMsg.displayError(this);
					return;
				}
				Vector lvDataOut = new Vector();
				lvDataOut.addElement(cvSubstaData);
				lvDataOut.addElement(caMFInvAlloctnData);
				getController().processData(
					VCModifyInvoiceINV002.DELETE,
					lvDataOut);
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnEnter()
					&& !getchkViewInvRcvdRpt().isSelected())
			{
				int liYesNo = rcveInvc();
				if (liYesNo == RTSException.YES)
				{
					String lsMsgType = new String(RTSException.CTL001);
					String lsMsg =
						new String(
							InventoryConstant.TXT_INVOICE_SPACE
								+ caMFInvAlloctnData
									.getMFInvAckData()
									.getInvcNo()
								+ InventoryConstant
									.TXT_INVOICE_WILL_BE_PUT_IN
								+ caMFInvAlloctnData
									.getMFInvAckData()
									.getDest()
								+ InventoryConstant.TXT_INVOICE_RECV);
					RTSException leRTSExMsg =
						new RTSException(lsMsgType, lsMsg, null);
					liYesNo = leRTSExMsg.displayError(this);
					if (liYesNo == RTSException.YES)
					{
						captureUserInput();
						
						// defect 8628 
						ReportSearchData laRptSearchData = new ReportSearchData();
						laRptSearchData.initForClient(ReportConstant.ONLINE);
						// end defect 8628  

						Vector lvct = new Vector();
						lvct.addElement(cvSubstaData);
						lvct.addElement(caMFInvAlloctnData);
						lvct.addElement(laRptSearchData);
						getController().processData(
							AbstractViewController.ENTER,
							lvct);
					}
					else
					{
						// The user decided to say no to go back to the 
						// screen.
						return;
					}
				}
				else
				{
					// there is an error on the invoice
					// user needs to correct it.
					return;
				}
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnEnter()
					&& getchkViewInvRcvdRpt().isSelected())
			{
				int liYesNo = rcveInvc();
				if (liYesNo == RTSException.YES)
				{
					String lsMsgType = new String(RTSException.CTL001);
					String lsMsg =
						new String(
							InventoryConstant.TXT_INVOICE_SPACE
								+ caMFInvAlloctnData
									.getMFInvAckData()
									.getInvcNo()
								+ InventoryConstant
									.TXT_INVOICE_WILL_BE_PUT_IN
								+ caMFInvAlloctnData
									.getMFInvAckData()
									.getDest()
								+ InventoryConstant.TXT_INVOICE_RECV);
					RTSException leRTSExMsg =
						new RTSException(lsMsgType, lsMsg, null);
					liYesNo = leRTSExMsg.displayError(this);
					if (liYesNo == RTSException.YES)
					{
						captureUserInput();
						ReportSearchData laRSD = new ReportSearchData();
						laRSD.setIntKey1(
							SystemProperty.getOfficeIssuanceNo());
						laRSD.setIntKey2(
							SystemProperty.getSubStationId());
						laRSD.setIntKey3(
							SystemProperty.getWorkStationId());
						laRSD.setKey1(SystemProperty.getCurrentEmpId());
						Vector lvDataOut = new Vector();
						lvDataOut.addElement(cvSubstaData);
						lvDataOut.addElement(caMFInvAlloctnData);
						lvDataOut.addElement(laRSD);
						getController().processData(
							VCModifyInvoiceINV002
								.GENERATE_RECEIVE_REPORT,
							lvDataOut);
					}
					else
					{
						// the user decided to say no to return to the 
						// screen
						return;
					}
				}
				else
				{
					// there is some error.
					// we can not leave the screen yet.
					return;
				}
			}
			else if (
				aaAE.getSource() == ivjButtonPanel1.getBtnCancel())
			{
				Vector lvDataOut = new Vector();
				lvDataOut.addElement(cvSubstaData);
				lvDataOut.addElement(caMFInvAlloctnData);
				getController().processData(
					AbstractViewController.CANCEL,
					lvDataOut);
			}
			else if (aaAE.getSource() == ivjButtonPanel1.getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV002);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture the user input so it can be sent to the next screen.
	 */
	private void captureUserInput()
	{
		caMFInvAlloctnData.setSelctdRowIndx(
			gettblInvcItms().getSelectedRow());
		caMFInvAlloctnData.setRptText(gettxtRptTxt().getText());
	}

	/**
	 * If the invoice does not contain any items, disable the modify 
	 * and delete buttons.
	 */
	private void enableDisableModifyDelete()
	{
		if (caMFInvAlloctnData.getInvAlloctnData().size()
			== CommonConstant.ELEMENT_0)
		{
			getbtnModify().setEnabled(false);
			getbtnDelete().setEnabled(false);
		}
		else
		{
			getbtnModify().setEnabled(true);
			getbtnDelete().setEnabled(true);
		}
	}

	/**
	 * Return the JButton2 property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnAdd()
	{
		if (ivjbtnAdd == null)
		{
			try
			{
				ivjbtnAdd = new RTSButton();
				ivjbtnAdd.setName("btnAdd");
				ivjbtnAdd.setMnemonic(java.awt.event.KeyEvent.VK_A);
				ivjbtnAdd.setText(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setMaximumSize(
					new java.awt.Dimension(57, 25));
				ivjbtnAdd.setActionCommand(CommonConstant.BTN_TXT_ADD);
				ivjbtnAdd.setMinimumSize(
					new java.awt.Dimension(57, 25));
				//ivjbtnAdd.setNextFocusableComponent(getbtnDelete());
				// user code begin {1}
				getbtnAdd().addActionListener(this);
				getbtnAdd().addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnAdd;
	}

	/**
	 * Return the ivjbtnDelete property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnDelete()
	{
		if (ivjbtnDelete == null)
		{
			try
			{
				ivjbtnDelete = new RTSButton();
				ivjbtnDelete.setName("btnDelete");
				ivjbtnDelete.setMnemonic(java.awt.event.KeyEvent.VK_D);
				ivjbtnDelete.setText(CommonConstant.BTN_TXT_DELETE);
				ivjbtnDelete.setMaximumSize(
					new java.awt.Dimension(71, 25));
				ivjbtnDelete.setActionCommand(
					CommonConstant.BTN_TXT_DELETE);
				ivjbtnDelete.setMinimumSize(
					new java.awt.Dimension(71, 25));
				//ivjbtnDelete.setNextFocusableComponent(
				//	getButtonPanel1());
				// user code begin {1}
				getbtnDelete().addActionListener(this);
				getbtnDelete().addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnDelete;
	}

	/**
	 * Return the ivjbtnModify property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnModify()
	{
		if (ivjbtnModify == null)
		{
			try
			{
				ivjbtnModify = new RTSButton();
				ivjbtnModify.setName("btnModify");
				ivjbtnModify.setMnemonic(java.awt.event.KeyEvent.VK_M);
				ivjbtnModify.setText(CommonConstant.BTN_TXT_MODIFY);
				ivjbtnModify.setMaximumSize(
					new java.awt.Dimension(71, 25));
				ivjbtnModify.setActionCommand(
					CommonConstant.BTN_TXT_MODIFY);
				ivjbtnModify.setMinimumSize(
					new java.awt.Dimension(71, 25));
				//ivjbtnModify.setNextFocusableComponent(getbtnAdd());
				// user code begin {1}
				getbtnModify().addActionListener(this);
				getbtnModify().addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnModify;
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
				//ivjButtonPanel1.setNextFocusableComponent(
				//	getchkViewInvRcvdRpt());
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
				// defect 7890 
				//ivjButtonPanel1.getBtnEnter().addKeyListener(this);
				//ivjButtonPanel1.getBtnCancel().addKeyListener(this);
				//ivjButtonPanel1.getBtnHelp().addKeyListener(this);
				// end defect 7890
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
	 * Return the chkViewInventoryReceived property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkViewInvRcvdRpt()
	{
		if (ivjchkViewInvRcvdRpt == null)
		{
			try
			{
				ivjchkViewInvRcvdRpt = new JCheckBox();
				ivjchkViewInvRcvdRpt.setName("chkViewInvRcvdRpt");
				ivjchkViewInvRcvdRpt.setMnemonic(java.awt.event.KeyEvent.VK_V);
				ivjchkViewInvRcvdRpt.setText(
					InventoryConstant.TXT_VIEW_INV_REC_RPT);
				ivjchkViewInvRcvdRpt.setMaximumSize(
					new java.awt.Dimension(204, 22));
				ivjchkViewInvRcvdRpt.setActionCommand(
					InventoryConstant.TXT_VIEW_INV_REC_RPT);
				ivjchkViewInvRcvdRpt.setSelected(true);
				ivjchkViewInvRcvdRpt.setMinimumSize(
					new java.awt.Dimension(204, 22));
				//ivjchkViewInvRcvdRpt.setNextFocusableComponent(
				//	gettblInvcItms());
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
		return ivjchkViewInvRcvdRpt;
	}

	/**
	 * Return the FrmModifyInvoiceINV002ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmModifyInvoiceINV002ContentPane1()
	{
		if (ivjFrmModifyInvoiceINV002ContentPane1 == null)
		{
			try
			{
				ivjFrmModifyInvoiceINV002ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmModifyInvoiceINV002ContentPane1.setName(
					"FrmModifyInvoiceINV002ContentPane1");
				ivjFrmModifyInvoiceINV002ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmModifyInvoiceINV002ContentPane1.setMaximumSize(
					new java.awt.Dimension(630, 400));
				ivjFrmModifyInvoiceINV002ContentPane1.setMinimumSize(
					new java.awt.Dimension(630, 400));
				ivjFrmModifyInvoiceINV002ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				java.awt.GridBagConstraints constraintsbtnModify =
					new java.awt.GridBagConstraints();
				constraintsbtnModify.gridx = 1;
				constraintsbtnModify.gridy = 6;
				constraintsbtnModify.gridwidth = 2;
				constraintsbtnModify.ipadx = 4;
				constraintsbtnModify.ipady = 5;
				constraintsbtnModify.insets =
					new java.awt.Insets(7, 45, 25, 2);
				getFrmModifyInvoiceINV002ContentPane1().add(
					getbtnModify(),
					constraintsbtnModify);

				java.awt.GridBagConstraints constraintsbtnAdd =
					new java.awt.GridBagConstraints();
				constraintsbtnAdd.gridx = 3;
				constraintsbtnAdd.gridy = 6;
				constraintsbtnAdd.ipadx = 18;
				constraintsbtnAdd.ipady = 5;
				constraintsbtnAdd.insets =
					new java.awt.Insets(7, 3, 25, 2);
				getFrmModifyInvoiceINV002ContentPane1().add(
					getbtnAdd(),
					constraintsbtnAdd);

				java.awt.GridBagConstraints constraintsbtnDelete =
					new java.awt.GridBagConstraints();
				constraintsbtnDelete.gridx = 4;
				constraintsbtnDelete.gridy = 6;
				constraintsbtnDelete.ipadx = 4;
				constraintsbtnDelete.ipady = 5;
				constraintsbtnDelete.insets =
					new java.awt.Insets(7, 3, 25, 15);
				getFrmModifyInvoiceINV002ContentPane1().add(
					getbtnDelete(),
					constraintsbtnDelete);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 5;
				constraintsButtonPanel1.gridy = 6;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 26;
				constraintsButtonPanel1.ipady = 7;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(2, 15, 18, 57);
				getFrmModifyInvoiceINV002ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);

				java.awt.GridBagConstraints constraintspnlBackground =
					new java.awt.GridBagConstraints();
				constraintspnlBackground.gridx = 1;
				constraintspnlBackground.gridy = 1;
				constraintspnlBackground.gridwidth = 5;
				constraintspnlBackground.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlBackground.weightx = 1.0;
				constraintspnlBackground.weighty = 1.0;
				constraintspnlBackground.ipadx = -9;
				constraintspnlBackground.ipady = -10;
				constraintspnlBackground.insets =
					new java.awt.Insets(6, 15, 4, 15);
				getFrmModifyInvoiceINV002ContentPane1().add(
					getpnlBackground(),
					constraintspnlBackground);

				java
					.awt
					.GridBagConstraints constraintsstcLblSelctdItmDetailedStatus =
					new java.awt.GridBagConstraints();
				constraintsstcLblSelctdItmDetailedStatus.gridx = 1;
				constraintsstcLblSelctdItmDetailedStatus.gridy = 3;
				constraintsstcLblSelctdItmDetailedStatus.gridwidth = 4;
				constraintsstcLblSelctdItmDetailedStatus.ipadx = 36;
				constraintsstcLblSelctdItmDetailedStatus.insets =
					new java.awt.Insets(3, 15, 1, 74);
				getFrmModifyInvoiceINV002ContentPane1().add(
					getstcLblSelctdItmDetailedStatus(),
					constraintsstcLblSelctdItmDetailedStatus);

				java
					.awt
					.GridBagConstraints constraintslblSelctdItmDetailedStatus =
					new java.awt.GridBagConstraints();
				constraintslblSelctdItmDetailedStatus.gridx = 1;
				constraintslblSelctdItmDetailedStatus.gridy = 4;
				constraintslblSelctdItmDetailedStatus.gridwidth = 5;
				constraintslblSelctdItmDetailedStatus.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintslblSelctdItmDetailedStatus.weightx = 1.0;
				constraintslblSelctdItmDetailedStatus.weighty = 1.0;
				constraintslblSelctdItmDetailedStatus.ipadx = 473;
				constraintslblSelctdItmDetailedStatus.ipady = 23;
				constraintslblSelctdItmDetailedStatus.insets =
					new java.awt.Insets(2, 20, 3, 25);
				getFrmModifyInvoiceINV002ContentPane1().add(
					getlblSelctdItmDetailedStatus(),
					constraintslblSelctdItmDetailedStatus);

				java.awt.GridBagConstraints constraintsstcLblRptTxt =
					new java.awt.GridBagConstraints();
				constraintsstcLblRptTxt.gridx = 1;
				constraintsstcLblRptTxt.gridy = 5;
				constraintsstcLblRptTxt.ipadx = 7;
				constraintsstcLblRptTxt.ipady = 6;
				constraintsstcLblRptTxt.insets =
					new java.awt.Insets(3, 25, 1, 4);
				getFrmModifyInvoiceINV002ContentPane1().add(
					getstcLblRptTxt(),
					constraintsstcLblRptTxt);

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 2;
				constraintsJScrollPane1.gridwidth = 5;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 558;
				constraintsJScrollPane1.ipady = 170;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(4, 15, 3, 15);
				getFrmModifyInvoiceINV002ContentPane1().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				java.awt.GridBagConstraints constraintstxtRptTxt =
					new java.awt.GridBagConstraints();
				constraintstxtRptTxt.gridx = 2;
				constraintstxtRptTxt.gridy = 5;
				constraintstxtRptTxt.gridwidth = 4;
				constraintstxtRptTxt.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtRptTxt.weightx = 1.0;
				constraintstxtRptTxt.ipadx = 471;
				constraintstxtRptTxt.insets =
					new java.awt.Insets(3, 5, 1, 25);
				getFrmModifyInvoiceINV002ContentPane1().add(
					gettxtRptTxt(),
					constraintstxtRptTxt);
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
		return ivjFrmModifyInvoiceINV002ContentPane1;
	}

	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
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
				//ivjJScrollPane1.setNextFocusableComponent(
				//	gettxtRptTxt());
				getJScrollPane1().setViewportView(gettblInvcItms());
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
	 * Return the lblDestination property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblDest()
	{
		if (ivjlblDest == null)
		{
			try
			{
				ivjlblDest = new JLabel();
				ivjlblDest.setName("lblDest");
				ivjlblDest.setText(TEXT_SAMPLE_DEST);
				ivjlblDest.setVerticalAlignment(
					javax.swing.SwingConstants.TOP);
				ivjlblDest.setMaximumSize(
					new java.awt.Dimension(126, 14));
				ivjlblDest.setMinimumSize(
					new java.awt.Dimension(126, 14));
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
		return ivjlblDest;
	}

	/**
	 * Return the lblInvoiceNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblInvcNo()
	{
		if (ivjlblInvcNo == null)
		{
			try
			{
				ivjlblInvcNo = new JLabel();
				ivjlblInvcNo.setName("lblInvcNo");
				ivjlblInvcNo.setOpaque(true);
				ivjlblInvcNo.setText(TEXT_SAMPLE_INVCNO);
				ivjlblInvcNo.setBackground(java.awt.Color.white);
				ivjlblInvcNo.setMaximumSize(
					new java.awt.Dimension(48, 16));
				// setting text to bold
				ivjlblInvcNo.setFont(
					new java.awt.Font(
						UtilityMethods.getDefaultFont(),
						java.awt.Font.BOLD,
						UtilityMethods.getDefaultFontSize()
							+ FONT_INCREASE));
				//new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
				ivjlblInvcNo.setMinimumSize(
					new java.awt.Dimension(48, 16));
				ivjlblInvcNo.setHorizontalAlignment(
					javax.swing.SwingConstants.CENTER);
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
		return ivjlblInvcNo;
	}

	/**
	 * Return the lblOrderDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblOrderDt()
	{
		if (ivjlblOrderDt == null)
		{
			try
			{
				ivjlblOrderDt = new JLabel();
				ivjlblOrderDt.setName("lblOrderDt");
				ivjlblOrderDt.setText(TEXT_SAMPLE_ORDER_DATE);
				ivjlblOrderDt.setMaximumSize(
					new java.awt.Dimension(48, 14));
				ivjlblOrderDt.setMinimumSize(
					new java.awt.Dimension(48, 14));
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
		return ivjlblOrderDt;
	}

	/**
	 * Return the lblReceiveInto property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getlblRcveInto()
	{
		if (ivjlblRcveInto == null)
		{
			try
			{
				ivjlblRcveInto = new JLabel();
				ivjlblRcveInto.setName("lblRcveInto");
				ivjlblRcveInto.setText(InventoryConstant.STR_CENTRAL);
				ivjlblRcveInto.setVerticalAlignment(
					javax.swing.SwingConstants.TOP);
				ivjlblRcveInto.setMaximumSize(
					new java.awt.Dimension(41, 14));
				ivjlblRcveInto.setMinimumSize(
					new java.awt.Dimension(41, 14));
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
		return ivjlblRcveInto;
	}

	/**
	 * Return the lblSelctdItmDetailedStatus property value.
	 * 
	 * @return RTSTextArea
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTextArea getlblSelctdItmDetailedStatus()
	{
		if (ivjlblSelctdItmDetailedStatus == null)
		{
			try
			{
				ivjlblSelctdItmDetailedStatus = new RTSTextArea();
				ivjlblSelctdItmDetailedStatus.setName(
					"lblSelctdItmDetailedStatus");
				ivjlblSelctdItmDetailedStatus.setLineWrap(true);
				ivjlblSelctdItmDetailedStatus.setWrapStyleWord(true);
				ivjlblSelctdItmDetailedStatus.setText(
					TEXT_SELCTDITMDETAILEDSTATUS_SAMPLE);
				ivjlblSelctdItmDetailedStatus.setBackground(
					new java.awt.Color(204, 204, 204));
				ivjlblSelctdItmDetailedStatus.setMaximumSize(
					new java.awt.Dimension(92, 14));
				ivjlblSelctdItmDetailedStatus.setMinimumSize(
					new java.awt.Dimension(92, 14));
				ivjlblSelctdItmDetailedStatus.setEditable(false);
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
		return ivjlblSelctdItmDetailedStatus;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlBackground()
	{
		if (ivjpnlBackground == null)
		{
			try
			{
				ivjpnlBackground = new JPanel();
				ivjpnlBackground.setName("pnlBackground");
				ivjpnlBackground.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlBackground.setMaximumSize(
					new java.awt.Dimension(591, 72));
				ivjpnlBackground.setMinimumSize(
					new java.awt.Dimension(591, 72));

				java.awt.GridBagConstraints constraintsstcLblInvcNo =
					new java.awt.GridBagConstraints();
				constraintsstcLblInvcNo.gridx = 1;
				constraintsstcLblInvcNo.gridy = 1;
				constraintsstcLblInvcNo.ipadx = 8;
				constraintsstcLblInvcNo.insets =
					new java.awt.Insets(17, 10, 8, 3);
				getpnlBackground().add(
					getstcLblInvcNo(),
					constraintsstcLblInvcNo);

				java.awt.GridBagConstraints constraintsstcLblDest =
					new java.awt.GridBagConstraints();
				constraintsstcLblDest.gridx = 1;
				constraintsstcLblDest.gridy = 2;
				constraintsstcLblDest.ipadx = 8;
				constraintsstcLblDest.ipady = 2;
				constraintsstcLblDest.insets =
					new java.awt.Insets(4, 4, 2, 3);
				getpnlBackground().add(
					getstcLblDest(),
					constraintsstcLblDest);

				java.awt.GridBagConstraints constraintslblDest =
					new java.awt.GridBagConstraints();
				constraintslblDest.gridx = 2;
				constraintslblDest.gridy = 2;
				constraintslblDest.gridwidth = 3;
				constraintslblDest.ipadx = 116;
				constraintslblDest.ipady = 2;
				constraintslblDest.insets =
					new java.awt.Insets(4, 8, 2, 7);
				getpnlBackground().add(
					getlblDest(),
					constraintslblDest);

				java.awt.GridBagConstraints constraintslblInvcNo =
					new java.awt.GridBagConstraints();
				constraintslblInvcNo.gridx = 2;
				constraintslblInvcNo.gridy = 1;
				constraintslblInvcNo.ipadx = 8;
				constraintslblInvcNo.ipady = 2;
				constraintslblInvcNo.insets =
					new java.awt.Insets(15, 4, 6, 8);
				getpnlBackground().add(
					getlblInvcNo(),
					constraintslblInvcNo);

				java.awt.GridBagConstraints constraintslblOrderDt =
					new java.awt.GridBagConstraints();
				constraintslblOrderDt.gridx = 4;
				constraintslblOrderDt.gridy = 1;
				constraintslblOrderDt.ipadx = 20;
				constraintslblOrderDt.insets =
					new java.awt.Insets(17, 4, 8, 26);
				getpnlBackground().add(
					getlblOrderDt(),
					constraintslblOrderDt);

				java
					.awt
					.GridBagConstraints constraintschkViewInvRcvdRpt =
					new java.awt.GridBagConstraints();
				constraintschkViewInvRcvdRpt.gridx = 5;
				constraintschkViewInvRcvdRpt.gridy = 1;
				constraintschkViewInvRcvdRpt.gridwidth = 2;
				constraintschkViewInvRcvdRpt.ipadx = 1;
				constraintschkViewInvRcvdRpt.insets =
					new java.awt.Insets(13, 7, 4, 9);
				getpnlBackground().add(
					getchkViewInvRcvdRpt(),
					constraintschkViewInvRcvdRpt);

				java.awt.GridBagConstraints constraintsstcLblRcveInto =
					new java.awt.GridBagConstraints();
				constraintsstcLblRcveInto.gridx = 5;
				constraintsstcLblRcveInto.gridy = 2;
				constraintsstcLblRcveInto.ipadx = 24;
				constraintsstcLblRcveInto.ipady = 2;
				constraintsstcLblRcveInto.insets =
					new java.awt.Insets(4, 9, 2, 6);
				getpnlBackground().add(
					getstcLblRcveInto(),
					constraintsstcLblRcveInto);

				java.awt.GridBagConstraints constraintslblRcveInto =
					new java.awt.GridBagConstraints();
				constraintslblRcveInto.gridx = 6;
				constraintslblRcveInto.gridy = 2;
				constraintslblRcveInto.ipadx = 4;
				constraintslblRcveInto.ipady = 2;
				constraintslblRcveInto.insets =
					new java.awt.Insets(4, 7, 2, 58);
				getpnlBackground().add(
					getlblRcveInto(),
					constraintslblRcveInto);

				java.awt.GridBagConstraints constraintsstcLblOrderDt =
					new java.awt.GridBagConstraints();
				constraintsstcLblOrderDt.gridx = 3;
				constraintsstcLblOrderDt.gridy = 1;
				constraintsstcLblOrderDt.ipadx = 15;
				constraintsstcLblOrderDt.insets =
					new java.awt.Insets(17, 8, 8, 3);
				getpnlBackground().add(
					getstcLblOrderDt(),
					constraintsstcLblOrderDt);
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
		return ivjpnlBackground;
	}

	/**
	 * Return the stcLblDestination property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblDest()
	{
		if (ivjstcLblDest == null)
		{
			try
			{
				ivjstcLblDest = new JLabel();
				ivjstcLblDest.setName("stcLblDest");
				ivjstcLblDest.setDisplayedMnemonic('t');
				ivjstcLblDest.setText(
					InventoryConstant.TXT_DESTINATION_COLON);
				ivjstcLblDest.setMaximumSize(
					new java.awt.Dimension(67, 14));
				ivjstcLblDest.setHorizontalTextPosition(
					javax.swing.SwingConstants.CENTER);
				ivjstcLblDest.setLabelFor(gettblInvcItms());
				ivjstcLblDest.setVerticalAlignment(
					javax.swing.SwingConstants.TOP);
				ivjstcLblDest.setMinimumSize(
					new java.awt.Dimension(67, 14));
				ivjstcLblDest.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblDest;
	}

	/**
	 * Return the stcLblInvoiceNo property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblInvcNo()
	{
		if (ivjstcLblInvcNo == null)
		{
			try
			{
				ivjstcLblInvcNo = new JLabel();
				ivjstcLblInvcNo.setName("stcLblInvcNo");
				ivjstcLblInvcNo.setText(
					InventoryConstant.TXT_INVOICE_NO_COLON);
				ivjstcLblInvcNo.setMaximumSize(
					new java.awt.Dimension(61, 14));
				ivjstcLblInvcNo.setMinimumSize(
					new java.awt.Dimension(61, 14));
				ivjstcLblInvcNo.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblInvcNo;
	}

	/**
	 * Return the stcLblOrderDate property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblOrderDt()
	{
		if (ivjstcLblOrderDt == null)
		{
			try
			{
				ivjstcLblOrderDt = new JLabel();
				ivjstcLblOrderDt.setName("stcLblOrderDt");
				ivjstcLblOrderDt.setText(
					InventoryConstant.TXT_ORDER_DATE_COLON);
				ivjstcLblOrderDt.setMaximumSize(
					new java.awt.Dimension(65, 14));
				ivjstcLblOrderDt.setMinimumSize(
					new java.awt.Dimension(65, 14));
				ivjstcLblOrderDt.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblOrderDt;
	}

	/**
	 * Return the stcLblReceiveInto property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblRcveInto()
	{
		if (ivjstcLblRcveInto == null)
		{
			try
			{
				ivjstcLblRcveInto = new JLabel();
				ivjstcLblRcveInto.setName("stcLblRcveInto");
				ivjstcLblRcveInto.setText(
					InventoryConstant.TXT_RECEIVE_INTO_COLON);
				ivjstcLblRcveInto.setMaximumSize(
					new java.awt.Dimension(72, 14));
				ivjstcLblRcveInto.setVerticalAlignment(
					javax.swing.SwingConstants.TOP);
				ivjstcLblRcveInto.setMinimumSize(
					new java.awt.Dimension(72, 14));
				ivjstcLblRcveInto.setHorizontalAlignment(
					SwingConstants.RIGHT);
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
		return ivjstcLblRcveInto;
	}

	/**
	 * Return the JLabel3 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblRptTxt()
	{
		if (ivjstcLblRptTxt == null)
		{
			try
			{
				ivjstcLblRptTxt = new JLabel();
				ivjstcLblRptTxt.setName("stcLblRptTxt");
				ivjstcLblRptTxt.setDisplayedMnemonic(java.awt.event.KeyEvent.VK_R);
				ivjstcLblRptTxt.setText(
					InventoryConstant.TXT_REPORT_TEXT_COLON);
				ivjstcLblRptTxt.setMaximumSize(
					new java.awt.Dimension(69, 14));
				ivjstcLblRptTxt.setLabelFor(gettxtRptTxt());
				ivjstcLblRptTxt.setMinimumSize(
					new java.awt.Dimension(69, 14));
				ivjstcLblRptTxt.setHorizontalAlignment(
					SwingConstants.RIGHT);
				// user code begin {1}
				//			ivjstcLblRptTxt.labelFor(gettxtRptTxt());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblRptTxt;
	}

	/**
	 * Return the JLabel1 property value.
	 * 
	 * @return JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblSelctdItmDetailedStatus()
	{
		if (ivjstcLblSelctdItmDetailedStatus == null)
		{
			try
			{
				ivjstcLblSelctdItmDetailedStatus = new JLabel();
				ivjstcLblSelctdItmDetailedStatus.setName(
					"stcLblSelctdItmDetailedStatus");
				ivjstcLblSelctdItmDetailedStatus.setText(
					InventoryConstant.TXT_SELCTDITM_DETAILED_STATUS);
				ivjstcLblSelctdItmDetailedStatus.setMaximumSize(
					new java.awt.Dimension(170, 14));
				ivjstcLblSelctdItmDetailedStatus.setMinimumSize(
					new java.awt.Dimension(170, 14));
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
		return ivjstcLblSelctdItmDetailedStatus;
	}

	/**
	 * Return the ivjtblInvcItms property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblInvcItms()
	{
		if (ivjtblInvcItms == null)
		{
			try
			{
				ivjtblInvcItms = new RTSTable();
				ivjtblInvcItms.setName("tblInvcItms");
				getJScrollPane1().setColumnHeaderView(
					ivjtblInvcItms.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblInvcItms.setModel(new TMINV002());
				ivjtblInvcItms.setShowVerticalLines(false);
				ivjtblInvcItms.setShowHorizontalLines(false);
				ivjtblInvcItms.setAutoCreateColumnsFromModel(false);
				ivjtblInvcItms.setBounds(0, 0, 200, 200);
				//ivjtblInvcItms.setNextFocusableComponent(
				//	gettxtRptTxt());
				// user code begin {1}
				caTableModel = (TMINV002) ivjtblInvcItms.getModel();
				TableColumn laTCa =
					ivjtblInvcItms.getColumn(
						ivjtblInvcItms.getColumnName(0));
				laTCa.setPreferredWidth(5);
				// defect 8269
				//TableColumn laTCb =
				//	ivjtblInvcItms.getColumn(
				//		ivjtblInvcItms.getColumnName(1));
				//laTCb.setPreferredWidth(65);
				TableColumn laTCb =
					ivjtblInvcItms.getColumn(
						ivjtblInvcItms.getColumnName(1));
				laTCb.setPreferredWidth(260);
				TableColumn laTCc =
					ivjtblInvcItms.getColumn(
						ivjtblInvcItms.getColumnName(2));
				laTCc.setPreferredWidth(35);
				TableColumn laTCd =
					ivjtblInvcItms.getColumn(
						ivjtblInvcItms.getColumnName(3));
				laTCd.setPreferredWidth(60);
				TableColumn laTCe =
					ivjtblInvcItms.getColumn(
						ivjtblInvcItms.getColumnName(4));
				laTCe.setPreferredWidth(85);
				TableColumn laTCf =
					ivjtblInvcItms.getColumn(
						ivjtblInvcItms.getColumnName(5));
				// end defect 8269
				laTCf.setPreferredWidth(85);
				ivjtblInvcItms.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblInvcItms.init();
				DefaultTableCellRenderer ren =
					new ImageTableCellRenderer();
				laTCa.setCellRenderer(ren);
				// defect 8269
				//laTCb.setCellRenderer(
				//	ivjtblInvcItms.setColumnAlignment(RTSTable.LEFT));
				// end defect 8269
				laTCb.setCellRenderer(
					ivjtblInvcItms.setColumnAlignment(RTSTable.LEFT));
				laTCc.setCellRenderer(
					ivjtblInvcItms.setColumnAlignment(RTSTable.RIGHT));
				laTCd.setCellRenderer(
					ivjtblInvcItms.setColumnAlignment(RTSTable.RIGHT));
				laTCe.setCellRenderer(
					ivjtblInvcItms.setColumnAlignment(RTSTable.RIGHT));
				laTCf.setCellRenderer(
					ivjtblInvcItms.setColumnAlignment(RTSTable.RIGHT));
				ivjtblInvcItms
					.getSelectionModel()
					.addListSelectionListener(
					this);
				ivjtblInvcItms.addActionListener(this);
				//ivjtblInvcItms.setNextFocusableComponent(
				//	gettxtRptTxt());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblInvcItms;
	}

	/**
	 * Return the ivjtxtRptTxt property value.
	 * 
	 * @return RTSInputField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSInputField gettxtRptTxt()
	{
		if (ivjtxtRptTxt == null)
		{
			try
			{
				ivjtxtRptTxt = new RTSInputField();
				ivjtxtRptTxt.setName("txtRptTxt");
				ivjtxtRptTxt.setInput(RTSInputField.DEFAULT);
				ivjtxtRptTxt.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				// defect 8335
				ivjtxtRptTxt.setMaxLength(LENGTH_INV002_REPORT_TEXT);
				// end defect 8335
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
		return ivjtxtRptTxt;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7890
		///* Uncomment the following lines to print uncaught exceptions
		// *  to stdout */
		//System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		//exception.printStackTrace(System.out);
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7890
	}

	// defect 7885
	// these methods were deprecated in 5.2.0
	//	/**
	//	 * Handle Printable Delete.
	//	 * 
	//	 * @deprecated 
	//	 */
	//	private void handlePrintableDelete()
	//	{
	//		int liRow = gettblInvcItms().getSelectedRow();
	//		RTSException leRTSExMsg =
	//			new RTSException(
	//				RTSException.DECISION_VERIFICATION,
	//				"Inventory item will be deleted from invoice - Proceed?",
	//				"Confirmation");
	//		int liResponse = leRTSExMsg.displayError(this);
	//		if (liResponse == RTSException.YES)
	//		{
	//			// remove it from the table
	//			caMFInvAlloctnData.getInvAlloctnData().remove(liRow);
	//			caTableModel.add(caMFInvAlloctnData.getInvAlloctnData());
	//			// check to see if there's any more printable stuff in the vector
	//			for (int i = 0;
	//				i < caMFInvAlloctnData.getInvAlloctnData().size();
	//				i++)
	//			{
	//				InventoryAllocationUIData laIAUID =
	//					(InventoryAllocationUIData) caMFInvAlloctnData
	//						.getInvAlloctnData()
	//						.get(
	//						i);
	//				String laItemCode = laIAUID.getItmCd();
	//				ItemCodesData laItemData =
	//					ItemCodesCache.getItmCd(laItemCode);
	//				// if there's still a printable item, just return
	//				if (laItemData.isPrintable())
	//				{
	//					return;
	//				}
	//			}
	//			cbContainsPrintableInventory = false;
	//			getButtonPanel1().getBtnEnter().setEnabled(true);
	//		}
	//		else
	//		{
	//			return;
	//		}
	//	}
	//	/**
	//	 * Handle Printable Delete.
	//	 * 
	//	 * @param avInventory java.util.Vector
	//	 * @deprecated
	//	 */
	//	private void handlePrintableInventory(Vector avInventory)
	//	{
	//		for (int i = 0; i < avInventory.size(); i++)
	//		{
	//			InventoryAllocationUIData laIAUID =
	//				(InventoryAllocationUIData) avInventory.get(i);
	//			String lsItemCode = laIAUID.getItmCd();
	//			ItemCodesData laItemData =
	//				ItemCodesCache.getItmCd(lsItemCode);
	//			if (laItemData != null && laItemData.isPrintable())
	//			{
	//				RTSException leRTSExMsg =
	//					new RTSException(
	//						RTSException.WARNING_MESSAGE,
	//						"The printable Item Codes cannot be part of "
	//							+ "the invoice",
	//						"Error");
	//				leRTSExMsg.displayError(
	//					getController().getMediator().getParent());
	//				getButtonPanel1().getBtnEnter().setEnabled(false);
	//				cbContainsPrintableInventory = true;
	//				break;
	//			}
	//		}
	//	}
	// end defect 7885

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
			setName(ScreenConstant.INV002_FRAME_NAME);
			setSize(610, 420);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setModal(true);
			setTitle(ScreenConstant.INV002_FRAME_TITLE);
			setContentPane(getFrmModifyInvoiceINV002ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Determines which radio button is currently selected.  
	 * Then depending on which arrow key is pressed, it sets that 
	 * radio button selected and requests focus.
	 *
	 * @param aaKE KeyEvent  
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);
		if (aaKE.getSource() instanceof RTSButton)
		{
			if (aaKE.getKeyCode() == KeyEvent.VK_UP
				|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
			{
				// defect 7885
				// this is handled by button panel
				//if (getButtonPanel1().getBtnEnter().hasFocus())
				//{
				//	getButtonPanel1().getBtnHelp().requestFocus();
				//}
				//else if (getButtonPanel1().getBtnCancel().hasFocus())
				//{
				//	getButtonPanel1().getBtnEnter().requestFocus();
				//}
				//else if (getButtonPanel1().getBtnHelp().hasFocus())
				//{
				//	getButtonPanel1().getBtnCancel().requestFocus();
				//}
				//else if (getbtnModify().hasFocus())
				// end defect 7885
				if (getbtnModify().hasFocus())
				{
					getbtnDelete().requestFocus();
				}
				else if (getbtnDelete().hasFocus())
				{
					getbtnAdd().requestFocus();
				}
				else if (getbtnAdd().hasFocus())
				{
					if (getbtnModify().isEnabled())
					{
						getbtnModify().requestFocus();
					}
					else
					{
						getbtnAdd().requestFocus();
					}
				}
				aaKE.consume();
			}
			else if (
				aaKE.getKeyCode() == KeyEvent.VK_DOWN
					|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				// defect 7885 
				// this is handled from button panel now.
				//if (getButtonPanel1().getBtnEnter().hasFocus())
				//{
				//	getButtonPanel1().getBtnCancel().requestFocus();
				//}
				//else if (getButtonPanel1().getBtnCancel().hasFocus())
				//{
				//	getButtonPanel1().getBtnHelp().requestFocus();
				//}
				//else if (getButtonPanel1().getBtnHelp().hasFocus())
				//{
				//	getButtonPanel1().getBtnEnter().requestFocus();
				//}
				//else if (getbtnModify().hasFocus())
				// end defect 7885
				if (getbtnModify().hasFocus())
				{
					getbtnAdd().requestFocus();
				}
				else if (getbtnDelete().hasFocus())
				{
					getbtnModify().requestFocus();
				}
				else if (getbtnAdd().hasFocus())
				{
					if (getbtnDelete().isEnabled())
					{
						getbtnDelete().requestFocus();
					}
					else
					{
						getbtnAdd().requestFocus();
					}
				}
				aaKE.consume();
			}
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
			FrmModifyInvoiceINV002 laFrmModifyInvoiceINV002;
			laFrmModifyInvoiceINV002 = new FrmModifyInvoiceINV002();
			laFrmModifyInvoiceINV002.setModal(true);
			laFrmModifyInvoiceINV002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmModifyInvoiceINV002.show();
			java.awt.Insets insets =
				laFrmModifyInvoiceINV002.getInsets();
			laFrmModifyInvoiceINV002.setSize(
				laFrmModifyInvoiceINV002.getWidth()
					+ insets.left
					+ insets.right,
				laFrmModifyInvoiceINV002.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmModifyInvoiceINV002.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Call when the user presses the enter button.
	 */
	private int rcveInvc()
	{
		String lsMsgType = new String();
		String lsMsg = new String();
		String lsTitle = new String();
		int liNumItmsFailed = 0;
		int liNumItmsValidated = 0;
		int liYesNo = RTSException.YES;
		try
		{
			// Test if any items failed in validation
			for (int i = 0;
				i < caMFInvAlloctnData.getInvAlloctnData().size();
				i++)
			{
				InventoryAllocationUIData laIAUID =
					(InventoryAllocationUIData) caMFInvAlloctnData
						.getInvAlloctnData()
						.get(
						i);
				if (laIAUID
					.getStatusCd()
					.equals(InventoryConstant.FAILED))
				{
					liNumItmsFailed = liNumItmsFailed + 1;
				}
				if (laIAUID
					.getStatusCd()
					.equals(InventoryConstant.CHECK))
				{
					liNumItmsValidated = liNumItmsValidated + 1;
				}
			}
			if (liNumItmsFailed > 0)
			{
				lsMsgType = RTSException.WARNING_MESSAGE;
				lsMsg = ErrorsConstant.ERR_MSG_ITEMS_FAILED;
				lsTitle = CommonConstant.STR_SPACE_EMPTY;
				RTSException leRTSExMsg =
					new RTSException(lsMsgType, lsMsg, lsTitle);
				throw leRTSExMsg;
			}
			// Test if any items to receive
			if (caMFInvAlloctnData.getInvAlloctnData().size()
				== CommonConstant.ELEMENT_0)
			{
				lsMsgType = RTSException.WARNING_MESSAGE;
				lsMsg =
					ErrorsConstant.ERR_MSG_THERE_IS_NO_ITEM_TO_RECEIVE;
				lsTitle = CommonConstant.STR_SPACE_EMPTY;
				RTSException leRTSExMsg =
					new RTSException(lsMsgType, lsMsg, lsTitle);
				throw leRTSExMsg;
			}
			// Test if any valid items to receive
			if (liNumItmsValidated == 0)
			{
				lsMsgType = RTSException.DECISION_VERIFICATION;
				lsMsg =
					ErrorsConstant.ERR_MSG_NO_VALID_ITEMS_TO_RECEIVE
						+ caMFInvAlloctnData
							.getMFInvAckData()
							.getInvcNo()
						+ InventoryConstant.TXT_COMMA_CONTINUE_QUESTION;
				lsTitle = ScreenConstant.CTL001_FRM_TITLE;
				RTSException leRTSExMsg =
					new RTSException(lsMsgType, lsMsg, lsTitle);
				throw leRTSExMsg;
			}
		}
		catch (RTSException leRTSEx)
		{
			liYesNo = leRTSEx.displayError(this);
			return liYesNo;
		}
		return liYesNo;
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		Vector lvDataIn = (Vector) aaData;
		cvSubstaData =
			(Vector) lvDataIn.elementAt(CommonConstant.ELEMENT_0);
		caMFInvAlloctnData =
			(MFInventoryAllocationData) lvDataIn.get(
				CommonConstant.ELEMENT_1);
		if (!caMFInvAlloctnData.getProcsdInvItm())
		{
			cvSubstaData =
				(Vector) lvDataIn.get(CommonConstant.ELEMENT_0);
			getlblInvcNo().setText(
				caMFInvAlloctnData.getMFInvAckData().getInvcNo());
			getlblDest().setText(
				caMFInvAlloctnData.getMFInvAckData().getDest());
			getlblRcveInto().setText(
				caMFInvAlloctnData.getMFInvAckData().getRcveInto());
			caTableModel.add(caMFInvAlloctnData.getInvAlloctnData());
			if (caMFInvAlloctnData.getInvAlloctnData().size()
				> CommonConstant.ELEMENT_0)
			{
				gettblInvcItms().setRowSelectionInterval(0, 0);
				getlblSelctdItmDetailedStatus().setText(
					((InventoryAllocationUIData) caMFInvAlloctnData
						.getInvAlloctnData()
						.get(0))
						.getStatusDesc());
			}
			else
			{
				getlblSelctdItmDetailedStatus().setText(
					CommonConstant.STR_SPACE_ONE);
			}
			// Convert the date format from YYYYMMDD to MM/DD/YYYY
			String lsInvcDt =
				String.valueOf(
					caMFInvAlloctnData
						.getMFInvAckData()
						.getInvItmOrderDt());
			String lsDispDt =
				lsInvcDt.substring(4, 6)
					+ CommonConstant.STR_SLASH
					+ lsInvcDt.substring(6)
					+ CommonConstant.STR_SLASH
					+ lsInvcDt.substring(0, 4);
			getlblOrderDt().setText(lsDispDt);
			enableDisableModifyDelete();
			gettblInvcItms().requestFocus();
		}
		else if (caMFInvAlloctnData.getProcsdInvItm())
		{
			caTableModel.add(caMFInvAlloctnData.getInvAlloctnData());
			if (caMFInvAlloctnData.getInvAlloctnData().size()
				> CommonConstant.ELEMENT_0)
			{
				int liSelctdRow = caMFInvAlloctnData.getSelctdRowIndx();
				gettblInvcItms().setRowSelectionInterval(
					liSelctdRow,
					liSelctdRow);
				getlblSelctdItmDetailedStatus().setText(
					((InventoryAllocationUIData) caMFInvAlloctnData
						.getInvAlloctnData()
						.get(liSelctdRow))
						.getStatusDesc());
			}
			else
			{
				getlblSelctdItmDetailedStatus().setText(
					CommonConstant.STR_SPACE_ONE);
			}
			enableDisableModifyDelete();
			// defect 8949,9518
			//gettblInvcItms().requestFocus();
			// end defect 8949,9518
		}
		// defect 8494
		// Moved from windowOpened
		if (caMFInvAlloctnData.getInvAlloctnData().size()
			== CommonConstant.ELEMENT_0)
		{
			//getbtnAdd().requestFocus();
			setDefaultFocusField(getbtnAdd());
		}
		else
		{
			//gettblInvcItms().requestFocus();
			setDefaultFocusField(gettblInvcItms());
		}
		// end defect 8494
	}

	/** 
	 * Called whenever the value of the selection changes.
	 * 
	 * @param aaLSE ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent aaLSE)
	{
		int liRowSelctd = gettblInvcItms().getSelectedRow();
		if (liRowSelctd > CommonConstant.NOT_FOUND)
		{
			InventoryAllocationUIData laIAUID =
				(InventoryAllocationUIData) caMFInvAlloctnData
					.getInvAlloctnData()
					.get(
					liRowSelctd);
			getlblSelctdItmDetailedStatus().setText(
				laIAUID.getStatusDesc());
		}
	}

// defect 8494
// Moved to setData
//	/**
//	 * If there are invoice items returned from the mf set focus on 
//	 * the table.  If not put focus on the add button.
//	 * 
//	 * @param aaWE WindowEvent  
//	 */
//	public void windowOpened(java.awt.event.WindowEvent aaWE)
//	{
//		super.windowOpened(aaWE);
//		if (caMFInvAlloctnData.getInvAlloctnData().size()
//			== CommonConstant.ELEMENT_0)
//		{
//			getbtnAdd().requestFocus();
//		}
//		else
//		{
//			gettblInvcItms().requestFocus();
//		}
//	}
// end defect 8494
}
