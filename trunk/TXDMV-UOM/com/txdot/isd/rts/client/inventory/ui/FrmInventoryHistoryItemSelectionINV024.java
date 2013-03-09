package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.InventoryHistoryUIData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 *
 * FrmInventoryHistoryItemSelectionINV024
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------ -----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames
 *							modal to each other
 * MAbs			05/21/2002	Unselecting "Select All" when mouse clicked
 * Ray Rowehl	06/02/2002	Handle Alt-key for selecting date or receipt
 *							fields. Do not allow switching to Invoice 
 *							Number if the radio button for it is 
 *							disabled..
 *							Also touched the GUI to line up date and 
 *							invoice fields. CQU100004169
 * B Arredondo	03/12/2004	Modifiy visual composition to change
 *							defaultCloseOperation to DO_NOTHING_ON_CLOSE
 *							defect 6897 Ver 5.1.6
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Clean up
 * 							organize imports, format source,
 * 							rename fields.
 * 							modify handleException()
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * K Harrell	05/19/2005	InventoryHistoryUIData field renaming
 * 							defect 7899 Ver 5.2.3 
 * Min Wang		08/01/2005	Remove item code from screen
 * 							modify gettblSelectOneOrMore(), setData()
 * 							defect 8269 Ver 5.2.2 Fix 6 
 * Ray Rowehl	08/09/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Work on constants.
 * 							Remove key processing from button panel.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Work on key processing for radio buttons.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3	
 * T Pederson	10/28/2005	Comment out space bar code in keyPressed() 
 * 							method. Code in valueChanged handles this.
 * 							defect 7890 Ver 5.2.3
 * Jeff S.		12/21/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), keyReleased(), 
 * 								windowClosed(), windowClosing(), 
 * 								windowDeactivated(), windowDeiconified(),
 * 								windowIconified(), windowOpened()
 * 							modify getpnlSelectSearchType(),initialize()
 * 							defect 7890 Ver 5.2.3
 * K Harrell	08/26/2008	Pass AdminLogData to Server for insertion
 * 							add INVOICE, INV_HSTRY_RPT, DEL_PRT, 
 * 							 RCV_RPT
 * 							add getAdminLogData() 
 * 							delete getBuilderData() 
 * 							modify actionPerformed(), valueChanged()
 * 							defect 8940 Ver Defect_POS_B
 * K Harrell	08/29/2008	Use new boolean for Select All Item(s) 
 * 							modify captureInput() 
 * 							defect 9706 Ver Defect_POS_B 
 * K Harrell	08/25/2009	Implement new Contructor of AdminLogData
 * 							Implement ReportSearchData.initForClient()
 * 							modify getAdminLogData(), actionPerformed() 
 * 							defect 8628 Ver Defect_POS_F
 * K Harrell	10/12/2009	Cleanup 
 * 							modify actionPerformed(), captureInput(),
 * 							 getpnlSelectSearchType(),getpnlSelectOneOrMore(), 
 * 							 getstcLblBeginDate(),enableDisableTxtBoxes(), 
 * 							 getstcLblEndDate(), getstcLblBeginDate(),
 *							 gettblSelectOneOrMore(), valueChanged(),
 *							 validateFrame()
 * 							defect 10207 Ver Defect_POS_G  
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV024 prompts for the date range or invoice number and the 
 * inventory item codes for which the Inventory History report should
 * be run.
 *
 * @version Defect_POS_G	10/12/2009
 * @author	Sunil Govindappa
 * <br>Creation date:		08/16/2001 13:49:58
 */

public class FrmInventoryHistoryItemSelectionINV024
	extends RTSDialogBox
	implements ActionListener, KeyListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSelectAllItems = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JPanel ivjpnlSelectOneOrMore = null;
	private JPanel ivjpnlSelectSearchType = null;
	private JRadioButton ivjradioDateRange = null;
	private JRadioButton ivjradioInvoiceNumber = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private JLabel ivjstcLblBeginDate = null;
	private JLabel ivjstcLblEndDate = null;
	private RTSTable ivjtblSelectOneOrMore = null;
	private RTSDateField ivjtxtBeginDate = null;
	private RTSDateField ivjtxtEndDate = null;
	private RTSInputField ivjtxtInvoiceNumber = null;

	private TMINV024 caInventoryTableModel = null;

	// int
	private int ciInvItemsRetrieved = 0;

	// Vector
	Vector cvInvPatrnDesc = null;

	//	Object 
	private InventoryHistoryUIData caInvHisUIData = null;

	// Constant
	private static final String DEL_PRT = "DelRpt";
	private static final int EARLIEST_DATE_SELECTED = 405;
	private static final String INV_HSTRY_RPT = "InvHstryRpt";
	private static final String INVOICE = "Invoice ";
	private static final String RCV_RPT = "RcvRpt";
	private static final int SELECT_DAYS_RANGE = 31;

	/**
	 * main entrypoint - starts the part when it is run as an 
	 * application
	 * 
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmInventoryHistoryItemSelectionINV024 laFrmInventoryHistoryItemSelectionINV024;
			laFrmInventoryHistoryItemSelectionINV024 =
				new FrmInventoryHistoryItemSelectionINV024();
			laFrmInventoryHistoryItemSelectionINV024.setModal(true);
			laFrmInventoryHistoryItemSelectionINV024
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryHistoryItemSelectionINV024.show();
			java.awt.Insets insets =
				laFrmInventoryHistoryItemSelectionINV024.getInsets();
			laFrmInventoryHistoryItemSelectionINV024.setSize(
				laFrmInventoryHistoryItemSelectionINV024.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryHistoryItemSelectionINV024.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryHistoryItemSelectionINV024.setVisibleRTS(
				true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace();
		}
	}

	/**
	 * FrmInventoryHistoryItemSelectionINV024 constructor comment.
	 */
	public FrmInventoryHistoryItemSelectionINV024()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryHistoryItemSelectionINV024 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryHistoryItemSelectionINV024(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryHistoryItemSelectionINV024 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryHistoryItemSelectionINV024(JFrame aaParent)
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
		if (!startWorking())
		{
			return;
		}

		try
		{
			clearAllColor(this);

			// RADIO BUTTON 
			if (aaAE.getSource() instanceof JRadioButton)
			{
				enableDisableTxtBoxes();
			}
			// SELECT ALL ITEMS 
			else if (aaAE.getSource() == getchkSelectAllItems())
			{
				if (getchkSelectAllItems().isSelected())
				{
					gettblSelectOneOrMore().selectAllRows(
						gettblSelectOneOrMore().getRowCount());
				}
				else
				{
					gettblSelectOneOrMore().unselectAllRows();
				}
			}
			// CANCEL
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// ENTER 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10207 
				// Implement single return  
				if (validateFrame())
				{
					captureInput();

					// defect 8628 
					ReportSearchData laRptSearchData =
						new ReportSearchData();
					laRptSearchData.initForClient(
						ReportConstant.ONLINE);
					// end defect 8628 

					// defect 8940	
					// Change vector size from 2 to 3 to add AdminLogData			
					Vector lvDataOut =
						new Vector(CommonConstant.ELEMENT_3);
					lvDataOut.add(caInvHisUIData);
					lvDataOut.add(laRptSearchData);
					lvDataOut.add(getAdminLogData());
					// end defect 8940

					getController().processData(
						AbstractViewController.ENTER,
						lvDataOut);
				}
				// end defect 10207 
			}
			// HELP 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV024);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture the data from the input fields.
	 */
	private void captureInput()
	{
		//Intialize cInvHisUIData data values for INV024 screen
		//This is to refresh the cInvHisUIData object when cancel 
		// button is pressed on reports screen
		caInvHisUIData.setInvItems(new Vector());
		caInvHisUIData.setDateRangeIndi(0);
		caInvHisUIData.setInvoiceIndi(0);
		caInvHisUIData.setBeginDate(null);
		caInvHisUIData.setEndDate(null);
		caInvHisUIData.setInvoiceNo(null);

		Vector lvVecInvItems = caInvHisUIData.getInvItems();
		Vector lvSelctdRows =
			new Vector(gettblSelectOneOrMore().getSelectedRowNumbers());
		for (int i = 0; i < lvSelctdRows.size(); i++)
		{
			String lsIndx = lvSelctdRows.get(i).toString();
			int liIndx = Integer.parseInt(lsIndx);
			lvVecInvItems.add(cvInvPatrnDesc.elementAt(liIndx));
		}

		if (getradioDateRange().isSelected())
		{
			caInvHisUIData.setDateRangeIndi(1);
			caInvHisUIData.setBeginDate(gettxtBeginDate().getDate());
			caInvHisUIData.setEndDate(gettxtEndDate().getDate());
		}
		else if (getradioInvoiceNumber().isSelected())
		{
			caInvHisUIData.setInvoiceIndi(1);
			caInvHisUIData.setInvoiceNo(
				gettxtInvoiceNumber().getText());
		}

		// defect 9706 
		caInvHisUIData.setAllItmsSelected(
			getchkSelectAllItems().isSelected());
		// end defect 9706 

		// defect 10207 
		caInvHisUIData.setRequestOfcIssuanceNo(
			SystemProperty.getOfficeIssuanceNo());
		// end defect 10207 
	}

	/**
	 * Switches user to the appropriate text field when date range or
	 * invoice is selected.
	 */
	private void enableDisableTxtBoxes()
	{
		if (getradioDateRange().isSelected())
		{
			gettxtBeginDate().setEnabled(true);
			gettxtEndDate().setEnabled(true);
			gettxtInvoiceNumber().setEnabled(false);
			// defect 10207 
			gettxtInvoiceNumber().setText(
				CommonConstant.STR_SPACE_EMPTY);
			// end defect 10207 
			gettxtBeginDate().requestFocus();
		}
		else if (getradioInvoiceNumber().isSelected())
		{
			// defect 10207 
			gettxtBeginDate().setText(CommonConstant.STR_SPACE_EMPTY);
			gettxtEndDate().setText(CommonConstant.STR_SPACE_EMPTY);
			// end defect 10207 
			gettxtBeginDate().setEnabled(false);
			gettxtEndDate().setEnabled(false);
			gettxtInvoiceNumber().setEnabled(true);
			gettxtInvoiceNumber().requestFocus();
		}
	}

	/** 
	 * Return populated AdminLogData Object
	 * 
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData()
	{
		// defect 8628 
		AdministrationLogData laLogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 

		laLogData.setEntity(INV_HSTRY_RPT);
		laLogData.setAction(
			caInvHisUIData.getReceiveHisReportIndi() == 1
				? RCV_RPT
				: DEL_PRT);
		int liSiteCount = caInvHisUIData.getSelectedCounties().size();

		String lsEntityValue = "(" + liSiteCount + ") ";

		if (caInvHisUIData.getInvoiceIndi() == 1)
		{
			lsEntityValue =
				lsEntityValue + INVOICE + caInvHisUIData.getInvoiceNo();
		}
		else
		{
			lsEntityValue =
				lsEntityValue
					+ caInvHisUIData.getBeginDate().getMMDDYY()
					+ "-"
					+ caInvHisUIData.getEndDate().getMMDDYY()
					+ " ";
		}

		laLogData.setEntityValue(lsEntityValue);
		return laLogData;

	}

	/**
	 * Return the ivjButtonPanel1 property value.
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
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
	 * Return the ivjchkSelectAllItems property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSelectAllItems()
	{
		if (ivjchkSelectAllItems == null)
		{
			try
			{
				ivjchkSelectAllItems = new JCheckBox();
				ivjchkSelectAllItems.setName("ivjchkSelectAllItems");
				ivjchkSelectAllItems.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjchkSelectAllItems.setText(
					InventoryConstant.TXT_SELECT_ALL_ITEMS);
				// user code begin {1}
				ivjchkSelectAllItems.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSelectAllItems;
	}

	/**
	 * Return the ivjJScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("ivjJScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				getJScrollPane1().setViewportView(
					gettblSelectOneOrMore());
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
	 * Return the ivjpnlSelectOneOrMore property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectOneOrMore()
	{
		if (ivjpnlSelectOneOrMore == null)
		{
			try
			{
				ivjpnlSelectOneOrMore = new JPanel();
				ivjpnlSelectOneOrMore.setName("ivjpnlSelectOneOrMore");

				// defect 10207 
				// Add Colon for consistency
				ivjpnlSelectOneOrMore.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant
							.TXT_SELECT_ONE_OR_MORE_COLON));
				// end defect 10207 

				ivjpnlSelectOneOrMore.setLayout(
					new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 396;
				constraintsJScrollPane1.ipady = 105;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(7, 7, 3, 6);
				getpnlSelectOneOrMore().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
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
		return ivjpnlSelectOneOrMore;
	}

	/**
	 * Return the ivjpnlSelectSearchType property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectSearchType()
	{
		if (ivjpnlSelectSearchType == null)
		{
			try
			{
				ivjpnlSelectSearchType = new JPanel();
				ivjpnlSelectSearchType.setName(
					"ivjpnlSelectSearchType");
					
				// defect 10207 
				// Add colon for consistency
				ivjpnlSelectSearchType.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant
							.TXT_SELECT_SEARCH_TYPE_COLON));
				// end defect 10207 
				
				ivjpnlSelectSearchType.setLayout(
					new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsradioDateRange =
					new java.awt.GridBagConstraints();
				constraintsradioDateRange.gridx = 0;
				constraintsradioDateRange.gridy = 0;
				constraintsradioDateRange.insets =
					new java.awt.Insets(-2, -2, 1, 12);
				getpnlSelectSearchType().add(
					getradioDateRange(),
					constraintsradioDateRange);

				java
					.awt
					.GridBagConstraints constraintsradioInvoiceNumber =
					new java.awt.GridBagConstraints();
				constraintsradioInvoiceNumber.gridx = 0;
				constraintsradioInvoiceNumber.gridy = 6;
				constraintsradioInvoiceNumber.ipadx = 1;
				constraintsradioInvoiceNumber.ipady = 1;
				constraintsradioInvoiceNumber.insets =
					new java.awt.Insets(6, 12, 8, 3);
				getpnlSelectSearchType().add(
					getradioInvoiceNumber(),
					constraintsradioInvoiceNumber);
				// user code begin {1}
				RTSButtonGroup laRadioGrpRcveInto =
					new RTSButtonGroup();
				laRadioGrpRcveInto.add(getradioDateRange());
				laRadioGrpRcveInto.add(getradioInvoiceNumber());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectSearchType;
	}

	/**
	 * Return the ivjradioDateRange property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioDateRange()
	{
		if (ivjradioDateRange == null)
		{
			try
			{
				ivjradioDateRange = new JRadioButton();
				ivjradioDateRange.setName("ivjradioDateRange");
				ivjradioDateRange.setMnemonic(KeyEvent.VK_D);
				ivjradioDateRange.setText(
					InventoryConstant.TXT_DATE_RANGE);
				// user code begin {1}
				ivjradioDateRange.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioDateRange;
	}

	/**
	 * Return the ivjradioInvoiceNumber property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioInvoiceNumber()
	{
		if (ivjradioInvoiceNumber == null)
		{
			try
			{
				ivjradioInvoiceNumber = new JRadioButton();
				ivjradioInvoiceNumber.setName("ivjradioInvoiceNumber");
				ivjradioInvoiceNumber.setMnemonic(KeyEvent.VK_I);
				ivjradioInvoiceNumber.setText(
					InventoryConstant.TXT_INVOICE_NUMBER);
				// user code begin {1}
				ivjradioInvoiceNumber.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjradioInvoiceNumber;
	}

	/**
	 * Return the ivjRTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"ivjRTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(
					new java.awt.GridBagLayout());

				java
					.awt
					.GridBagConstraints constraintspnlSelectSearchType =
					new java.awt.GridBagConstraints();
				constraintspnlSelectSearchType.gridx = 1;
				constraintspnlSelectSearchType.gridy = 1;
				constraintspnlSelectSearchType.gridheight = 2;
				constraintspnlSelectSearchType.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectSearchType.weightx = 1.0;
				constraintspnlSelectSearchType.weighty = 1.0;
				constraintspnlSelectSearchType.ipadx = 3;
				constraintspnlSelectSearchType.ipady = 28;
				constraintspnlSelectSearchType.insets =
					new java.awt.Insets(10, 16, 4, 11);
				getRTSDialogBoxContentPane().add(
					getpnlSelectSearchType(),
					constraintspnlSelectSearchType);

				java.awt.GridBagConstraints constraintsstcLblBeginDate =
					new java.awt.GridBagConstraints();
				constraintsstcLblBeginDate.gridx = 2;
				constraintsstcLblBeginDate.gridy = 1;
				constraintsstcLblBeginDate.ipadx = 5;
				constraintsstcLblBeginDate.insets =
					new java.awt.Insets(30, 1, 7, 2);
				getRTSDialogBoxContentPane().add(
					getstcLblBeginDate(),
					constraintsstcLblBeginDate);

				java.awt.GridBagConstraints constraintsstcLblEndDate =
					new java.awt.GridBagConstraints();
				constraintsstcLblEndDate.gridx = 4;
				constraintsstcLblEndDate.gridy = 1;
				constraintsstcLblEndDate.ipadx = 5;
				constraintsstcLblEndDate.insets =
					new java.awt.Insets(30, 8, 7, 3);
				getRTSDialogBoxContentPane().add(
					getstcLblEndDate(),
					constraintsstcLblEndDate);

				java.awt.GridBagConstraints constraintstxtBeginDate =
					new java.awt.GridBagConstraints();
				constraintstxtBeginDate.gridx = 3;
				constraintstxtBeginDate.gridy = 1;
				constraintstxtBeginDate.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtBeginDate.weightx = 1.0;
				constraintstxtBeginDate.ipadx = 70;
				constraintstxtBeginDate.insets =
					new java.awt.Insets(27, 3, 4, 8);
				getRTSDialogBoxContentPane().add(
					gettxtBeginDate(),
					constraintstxtBeginDate);

				java.awt.GridBagConstraints constraintstxtEndDate =
					new java.awt.GridBagConstraints();
				constraintstxtEndDate.gridx = 5;
				constraintstxtEndDate.gridy = 1;
				constraintstxtEndDate.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtEndDate.weightx = 1.0;
				constraintstxtEndDate.ipadx = 70;
				constraintstxtEndDate.insets =
					new java.awt.Insets(27, 3, 4, 18);
				getRTSDialogBoxContentPane().add(
					gettxtEndDate(),
					constraintstxtEndDate);

				java.awt.GridBagConstraints constraintstxtInvoiceNumber =
					new java.awt.GridBagConstraints();
				constraintstxtInvoiceNumber.gridx = 3;
				constraintstxtInvoiceNumber.gridy = 2;
				constraintstxtInvoiceNumber.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtInvoiceNumber.weightx = 1.0;
				constraintstxtInvoiceNumber.ipadx = 70;
				constraintstxtInvoiceNumber.insets =
					new java.awt.Insets(5, 3, 20, 8);
				getRTSDialogBoxContentPane().add(
					gettxtInvoiceNumber(),
					constraintstxtInvoiceNumber);

				java
					.awt
					.GridBagConstraints constraintschkSelectAllItems =
					new java.awt.GridBagConstraints();
				constraintschkSelectAllItems.gridx = 1;
				constraintschkSelectAllItems.gridy = 3;
				constraintschkSelectAllItems.ipadx = 8;
				constraintschkSelectAllItems.insets =
					new java.awt.Insets(4, 29, 3, 1);
				getRTSDialogBoxContentPane().add(
					getchkSelectAllItems(),
					constraintschkSelectAllItems);

				java
					.awt
					.GridBagConstraints constraintspnlSelectOneOrMore =
					new java.awt.GridBagConstraints();
				constraintspnlSelectOneOrMore.gridx = 1;
				constraintspnlSelectOneOrMore.gridy = 4;
				constraintspnlSelectOneOrMore.gridwidth = 5;
				constraintspnlSelectOneOrMore.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintspnlSelectOneOrMore.weightx = 1.0;
				constraintspnlSelectOneOrMore.weighty = 1.0;
				constraintspnlSelectOneOrMore.insets =
					new java.awt.Insets(4, 24, 4, 25);
				getRTSDialogBoxContentPane().add(
					getpnlSelectOneOrMore(),
					constraintspnlSelectOneOrMore);

				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 5;
				constraintsButtonPanel1.gridwidth = 4;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 34;
				constraintsButtonPanel1.ipady = 28;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(4, 116, 9, 18);
				getRTSDialogBoxContentPane().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
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
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the ivjstcLblBeginDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBeginDate()
	{
		if (ivjstcLblBeginDate == null)
		{
			try
			{
				ivjstcLblBeginDate = new JLabel();
				ivjstcLblBeginDate.setName("ivjstcLblBeginDate");
				ivjstcLblBeginDate.setText(
					InventoryConstant.TXT_BEGIN_DATE_COLON);
				// defect 10207 
				ivjstcLblBeginDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_B);
				ivjstcLblBeginDate.setLabelFor(gettxtBeginDate());
				// end defect 10207 
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
		return ivjstcLblBeginDate;
	}

	/**
	 * Return the ivjstcLblEndDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEndDate()
	{
		if (ivjstcLblEndDate == null)
		{
			try
			{
				ivjstcLblEndDate = new JLabel();
				ivjstcLblEndDate.setName("ivjstcLblEndDate");
				ivjstcLblEndDate.setText(
					InventoryConstant.TXT_END_DATE_COLON);
				// defect 10207
				ivjstcLblEndDate.setLabelFor(gettxtEndDate());
				ivjstcLblEndDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_N);
				// end defect 10207 
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
		return ivjstcLblEndDate;
	}

	/**
	 * Return the ivjtblSelectOneOrMore property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblSelectOneOrMore()
	{
		if (ivjtblSelectOneOrMore == null)
		{
			try
			{
				ivjtblSelectOneOrMore = new RTSTable();
				ivjtblSelectOneOrMore.setName("ivjtblSelectOneOrMore");
				getJScrollPane1().setColumnHeaderView(
					ivjtblSelectOneOrMore.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblSelectOneOrMore.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblSelectOneOrMore.setModel(new TMINV024());
				ivjtblSelectOneOrMore.setShowVerticalLines(false);
				ivjtblSelectOneOrMore.setShowHorizontalLines(false);
				ivjtblSelectOneOrMore.setAutoCreateColumnsFromModel(
					false);
				ivjtblSelectOneOrMore.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblSelectOneOrMore.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caInventoryTableModel =
					(TMINV024) ivjtblSelectOneOrMore.getModel();
				// defect 10207
				// Use Inventory Constants for Column Numbers
				TableColumn laTCa =
					ivjtblSelectOneOrMore.getColumn(
						ivjtblSelectOneOrMore.getColumnName(
							InventoryConstant.INV024_COL_ITEM_DESCR));
				laTCa.setPreferredWidth(450);
				TableColumn laTCb =
					ivjtblSelectOneOrMore.getColumn(
						ivjtblSelectOneOrMore.getColumnName(
							InventoryConstant.INV024_COL_ITEM_YEAR));
				// end defect 10207 
				laTCb.setPreferredWidth(250);
				ivjtblSelectOneOrMore.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblSelectOneOrMore.init();
				ivjtblSelectOneOrMore.addActionListener(this);
				ivjtblSelectOneOrMore.addMultipleSelectionListener(
					this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblSelectOneOrMore;
	}

	/**
	 * Return the ivjtxtBeginDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtBeginDate()
	{
		if (ivjtxtBeginDate == null)
		{
			try
			{
				ivjtxtBeginDate = new RTSDateField();
				ivjtxtBeginDate.setName("ivjtxtBeginDate");
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
		return ivjtxtBeginDate;
	}

	/**
	 * Return the ivjtxtEndDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtEndDate()
	{
		if (ivjtxtEndDate == null)
		{
			try
			{
				ivjtxtEndDate = new RTSDateField();
				ivjtxtEndDate.setName("ivjtxtEndDate");
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
		return ivjtxtEndDate;
	}

	/**
	 * Return the ivjtxtInvoiceNumber property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtInvoiceNumber()
	{
		if (ivjtxtInvoiceNumber == null)
		{
			try
			{
				ivjtxtInvoiceNumber = new RTSInputField();
				ivjtxtInvoiceNumber.setName("ivjtxtInvoiceNumber");
				// user code begin {1}
				ivjtxtInvoiceNumber.setMaxLength(
					InventoryConstant.INVOICE_NUMBER_LENGTH);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtxtInvoiceNumber;
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
			// user code begin {1}
			// focus manager necessary to make form follow tab tag order
			//FocusManager.setCurrentManager(new ContainerOrderFocusManager());
			addWindowListener(this);
			// user code end
			setName(ScreenConstant.INV024_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(480, 350);
			setTitle(ScreenConstant.INV024_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
			// defect 7890
			// This had to be done so that when hot keys are done on the
			// radio buttons focus would be moved to the text fields.
			setRequestFocus(false);
			// end defect 7890
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData instanceof Vector)
		{
			ciInvItemsRetrieved = ((Vector) aaData).size();
			// defect 8269
			cvInvPatrnDesc = (Vector) aaData;
			UtilityMethods.sort(cvInvPatrnDesc);
			caInventoryTableModel.add(cvInvPatrnDesc);
			// end defect 8269
		}
		else if (aaData instanceof InventoryHistoryUIData)
		{
			caInvHisUIData = (InventoryHistoryUIData) aaData;
			if (((InventoryHistoryUIData) aaData)
				.getDeleteHisReportIndi()
				== 1)
			{
				getradioInvoiceNumber().setEnabled(false);
			}
			getradioDateRange().setSelected(true);
			gettxtInvoiceNumber().setEnabled(false);
		}
	}

	/**
	 * Validate the check boxes, dates, and combo box on the screen.
	 * 
	 * @return boolean
	 */
	private boolean validateFrame()
	{
		// defect 10207 
		// Implement single return 
		boolean lbValid = true;

		RTSException leRTSEx = new RTSException();

		// Validate the begin and end dates if the Date Range 
		// radio button is selected.
		if (getradioDateRange().isSelected())
		{
			// Validate the date format.
			RTSDate laBeginDate = gettxtBeginDate().getDate();
			RTSDate laEndDate = gettxtEndDate().getDate();
			RTSDate laCurrenDate = RTSDate.getCurrentDate();

			if (laBeginDate == null)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_HIST_DATE_INVALID),
					gettxtBeginDate());
			}

			if (laEndDate == null)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_HIST_DATE_INVALID),
					gettxtEndDate());
			}

			if (gettxtBeginDate().isValidDate())
			{
				if (gettxtEndDate().isValidDate())
				{
					// Validate that the begin and end dates are within
					// a 31 day range, and that the begin date is <= 
					// the end date.
					if (((laBeginDate
						.add(RTSDate.DATE, SELECT_DAYS_RANGE))
						.compareTo(laEndDate)
						< 0)
						|| (laBeginDate.compareTo(laEndDate) > 0))
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_DATE_RANGE_INVALID),
							gettxtBeginDate());
					}

					// Validate that the begin date is not more than 
					// 405 (13 months) from today, and the begin date 
					// is <= today's date.
					if (((laBeginDate
						.add(RTSDate.DATE, EARLIEST_DATE_SELECTED))
						.compareTo(laCurrenDate)
						<= 0)
						|| (laBeginDate.compareTo(laCurrenDate) > 0))
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_HIST_DATE_INVALID),
							gettxtBeginDate());
					}

					// Validate that the end date is <= today's date.
					if (laEndDate.compareTo(laCurrenDate) > 0)
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_HIST_DATE_INVALID),
							gettxtEndDate());
					}
				}
				else
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						gettxtEndDate());
				}
			}
			else
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtBeginDate());
			}
		}
		// Validate the invoice number if the Invoice Number radio 
		// button is selected.
		else if (getradioInvoiceNumber().isSelected())
		{
			if (!gettxtInvoiceNumber()
				.getText()
				.equals(CommonConstant.STR_SPACE_EMPTY))
			{
				boolean lbValidNo = false;
				String lsAlphabet =
					new String(CommonConstant.VALID_LETTERS);
				String lsIntegers =
					new String(CommonConstant.VALID_INTS);
				String lsInvcNo =
					gettxtInvoiceNumber().getText().toUpperCase();
				if (lsInvcNo.length()
					<= InventoryConstant.INVOICE_NUMBER_LENGTH)
				{
					lbValidNo = true;
					for (int i = 0; i < lsInvcNo.length(); i++)
					{
						String lChar = lsInvcNo.substring(i, i + 1);
						if (i == 0
							&& lsIntegers.indexOf(lChar)
								== CommonConstant.NOT_FOUND
							&& lsAlphabet.indexOf(lChar)
								== CommonConstant.NOT_FOUND)
						{
							lbValidNo = false;
							break;
						}
						else if (
							i != 0
								&& lsIntegers.indexOf(lChar)
									== CommonConstant.NOT_FOUND)
						{
							lbValidNo = false;
							break;
						}
					}
				}
				if (!lbValidNo)
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_INVALID_INVOICE_NUMBER),
						gettxtInvoiceNumber());
				}
			}
			else
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
					gettxtInvoiceNumber());
			}
		}

		// Verify that at least one row has been selected
		Vector lvSelctdRows =
			new Vector(gettblSelectOneOrMore().getSelectedRowNumbers());
		if (lvSelctdRows.size() < 1)
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_NO_ITEMS_SELECTED),
				gettblSelectOneOrMore());
		}

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;
		// end defect 10207 
	}

	/** 
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aaLSE ListSelectionEvent
	  */
	public void valueChanged(ListSelectionEvent aaLSE)
	{
		// defect 10207 
		// Implement single return 
		if (!aaLSE.getValueIsAdjusting())
		{
			Vector lvSelctdRows =
				new Vector(
					gettblSelectOneOrMore().getSelectedRowNumbers());

			getchkSelectAllItems().setSelected(
				lvSelctdRows.size()
					== gettblSelectOneOrMore().getRowCount());
		}
		// end defect 10207 
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowActivated(WindowEvent aaWE)
	{
		if (ciInvItemsRetrieved == 0)
		{
			getController().processData(
				VCInventoryHistoryItemSelectionINV024.GET_INV_ITEMS,
				null);
		}
	}
}
