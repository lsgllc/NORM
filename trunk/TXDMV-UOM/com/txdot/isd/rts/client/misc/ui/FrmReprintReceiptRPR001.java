package com.txdot.isd.rts.client.misc.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.ReceiptLogData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmReprintReceiptRPR001.java
 *
 * (c) Texas Department of Transportation 2001
 * 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * BTusiani		04/11/2002	Delete Action Listener on Table.
 * 							defect 3418
 * Min Wang   	07/05/2002	Increased table size and Transaction
 *							Type column and decreased Trans No column.
 *							defect 4431
 * Min Wang   	08/01/2002	Fixed The error message shown for file not  
 *                          selected on reprint receipt does not match 
 * 							RTS I. 	
 * 							defect 4544 
 * Min Wang   	08/01/2002	Modified gettblReprintRcpt() to display 18 
 * 							digits. 
 * 							defect 4773 
 * K Harrell	03/19/2004	5.2.0 Merge.  See PCR 34 comments.
 *							add cbShouldShowOverride
 *							modify setData(),actionPerformed()
 * 							Imported from 520. 
 *							Ver 5.2.0
 * K Harrell	04/27/2004	Do not allow reprinting of voided trans
 *							modify actionPerformed()
 *							defect 7019  Ver 5.2.0
 * K Harrell	05/16/2004	Disable print cash register receipt if
 *							all transactions are voided
 *							defect 7019 (cont'd)  Ver 5.2.0 
 * J Zwiener	06/22/2005	Java 1.4 changes
 * 							Format source, organize imports, rename
 * 							fields, comment out methods.
 * 							defect 7892 Ver 5.2.3
 * K Harrell	07/20/2005	Hilight border on Reprint Cash Register 
 * 							Receipt clipped.  Altered screen via 
 * 							Visual Editor.
 * 							defect 8167 Ver 5.2.3 
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * Min Wang		04/10/2006	Reverse the gridbaglayout changes of 8167
 * 							modify getFrmReprintReceiptRPR001ContentPane1()
 * 							defect 8685 Ver 5.2.3
 * K Harrell	09/16/2008	Pass reason text to Supervisor Override 
 * 							Screen. Additional class cleanup.  
 * 							delete TXT_SHOW, TXT_DATA, TXT_CASHRECEIPT
 * 							delete getBuilderData()
 * 							modify actionPerformed(),setData()
 * 							defect 7283 Ver Defect_POS_B	
 * K Harrell	06/19/2011	add TXT_NOT_AVAIL
 * 							modify actionPerformed(), setData() 
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/** 
 * Screen presents the receipt of the last transaction
 *
 * @version	6.8.0 			06/19/2011	
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001
 */

public class FrmReprintReceiptRPR001
	extends RTSDialogBox
	implements ActionListener
{
	private static final String TXT_VOIDED = "** VOIDED **";

	// defect 10844
	private static final String TXT_NOT_AVAIL = "** NOT AVAILABLE **";
	// end defect 10844 

	private static final String TXT_REPRINT_CASH_RCPT =
		"Reprint cash register receipt";
	private static final String RPR001_FRM_TITLE =
		"Reprint Receipt    RPR001";

	private static final String TXT_CASH_RECEIPT = "CASH RECEIPT";

	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkReprintReceipt = null;
	private JPanel ivjFrmReprintReceiptRPR001ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblReprintRcpt = null;
	private TMRPR001 caReceiptsTableModel;

	// boolean 
	private boolean cbShouldShowOverride;

	// Object 
	private ReceiptLogData caCashRcptLogData = new ReceiptLogData();

	// defect 7283
	// Map 
	private Map caMap;
	// end defect 7283

	// Vector 
	private Vector cvReceipts = new Vector();

	/**
	 * FrmReprintReceiptRPR001 constructor comment.
	 */
	public FrmReprintReceiptRPR001()
	{
		super();
		initialize();
	}

	/**
	 * FrmReprintReceiptRPR001 constructor comment.
	 * 
	 * @param aaOwner java.awt.Dialog
	 */
	public FrmReprintReceiptRPR001(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * FrmReprintReceiptRPR001 constructor comment.
	 * 
	 * @param aaParent java.awt.Dialog
	 */
	public FrmReprintReceiptRPR001(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE awt.event.ActionEvent 
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}
		try
		{
			//field validation
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter()
				|| aaAE.getSource() == ivjtblReprintRcpt)
			{
				//Create vectors to store receipts selected to print	
				Vector lvReceiptsToPrint = new Vector();
				Vector lvSelectedRows =
					new Vector(
						ivjtblReprintRcpt.getSelectedRowNumbers());

				UtilityMethods.sort(lvSelectedRows);

				// Verify that all selected are eligible to reprint
				//Add the data from each row into the receipts vector
				for (int i = 0; i < lvSelectedRows.size(); i++)
				{
					String lsRow = lvSelectedRows.get(i).toString();
					int liRow2 = Integer.parseInt(lsRow);

					// defect 7019
					// throw exception if transaction voided
					if (((ReceiptLogData) cvReceipts.get(liRow2))
						.getTransType()
						.startsWith(TXT_VOIDED))
					{
						RTSException leInvalidDateEx =
							new RTSException(329);
						leInvalidDateEx.displayError(this);
						return;
					}
					// end defect 7019 
					// defect 10844
					else if (
						((ReceiptLogData) cvReceipts.get(liRow2))
							.getTransType()
							.startsWith(
							TXT_NOT_AVAIL))
					{
						RTSException leRTSEx =
							new RTSException(
								ErrorsConstant
									.ERR_NUM_NOT_AVAIL_FOR_REPRINT);
						leRTSEx.displayError(this);
						return;
					}
					// end defect 10844	

					// make sure there is a file to print before adding
					if (((ReceiptLogData) cvReceipts.get(liRow2))
						.getRcptFileName()
						.length()
						> 20)
					{
						lvReceiptsToPrint.add(
							(ReceiptLogData) cvReceipts.get(liRow2));
					}
				}
				// If user selects print cash register recipet, add that
				// to vector
				if (getchkReprintReceipt().isSelected())
				{
					lvReceiptsToPrint.add(caCashRcptLogData);
				}
				// If no receipts selected, present message saying at
				// least one must be selected
				if (lvReceiptsToPrint.size() == 0)
				{
					if (lvSelectedRows.size() < 1)
					{
						RTSException leInvalidSelectionEx =
							new RTSException(710);
						leInvalidSelectionEx.displayError(this);
					}
					else
					{
						// this is the case where something was selected
						// but no files to print
						RTSException leInvalidSelectionEx =
							new RTSException(134);
						leInvalidSelectionEx.displayError(this);
					}
					return;
				}
				if (cbShouldShowOverride)
				{
					// defect 7283 
					// Use Miscellaneous Constants
					caMap.put(
						MiscellaneousConstant.MAP_ENTRY_DATA,
						lvReceiptsToPrint);

					getController().processData(
						MiscellaneousConstant.OVERRIDE_NEEDED,
						caMap);
					// end defect 7283
				}
				else
				{
					getController().processData(
						VCReprintReceiptRPR001.ENTER,
						lvReceiptsToPrint);
				}
				// End PCR 34
			}
			//If user presses Cancel
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					VCReprintReceiptRPR001.CANCEL,
					null);
			}
			//If user presses Help
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.RPR001);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ButtonPanel2 property value.
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
	 * Return the chkReprintRceipt property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkReprintReceipt()
	{
		if (ivjchkReprintReceipt == null)
		{
			try
			{
				ivjchkReprintReceipt = new javax.swing.JCheckBox();
				ivjchkReprintReceipt.setName("chkReprintReceipt");
				ivjchkReprintReceipt.setMnemonic(82);
				ivjchkReprintReceipt.setText(TXT_REPRINT_CASH_RCPT);
				ivjchkReprintReceipt.setMaximumSize(
					new java.awt.Dimension(188, 22));
				ivjchkReprintReceipt.setActionCommand(
					"Reprint cash register receipt");
				ivjchkReprintReceipt.setMinimumSize(
					new java.awt.Dimension(188, 22));
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
		return ivjchkReprintReceipt;
	}
	/**
	 * Return the FrmReprintReceiptRPR001ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JPanel getFrmReprintReceiptRPR001ContentPane1()
	{
		if (ivjFrmReprintReceiptRPR001ContentPane1 == null)
		{
			try
			{
				// defect 8685
				ivjFrmReprintReceiptRPR001ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmReprintReceiptRPR001ContentPane1.setName(
					"FrmReprintReceiptRPR001ContentPane1");
				ivjFrmReprintReceiptRPR001ContentPane1.setLayout(
					new java.awt.GridBagLayout());

				ivjFrmReprintReceiptRPR001ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmReprintReceiptRPR001ContentPane1.setMinimumSize(
					new java.awt.Dimension(667, 280));
				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 545;
				constraintsJScrollPane1.ipady = 175;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(19, 17, 10, 16);
				getFrmReprintReceiptRPR001ContentPane1().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
				java
					.awt
					.GridBagConstraints constraintschkReprintReceipt =
					new java.awt.GridBagConstraints();
				constraintschkReprintReceipt.gridx = 1;
				constraintschkReprintReceipt.gridy = 2;
				constraintschkReprintReceipt.ipadx = 13;
				constraintschkReprintReceipt.ipady = -4;
				constraintschkReprintReceipt.insets =
					new java.awt.Insets(10, 199, 4, 200);
				getFrmReprintReceiptRPR001ContentPane1().add(
					getchkReprintReceipt(),
					constraintschkReprintReceipt);
				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 3;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 234;
				constraintsButtonPanel1.ipady = 32;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(4, 74, 17, 75);
				getFrmReprintReceiptRPR001ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
				// user code begin {1}
				// user code end
				// end defect 8685
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmReprintReceiptRPR001ContentPane1;
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
				getJScrollPane1().setViewportView(gettblReprintRcpt());
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
	 * Return the tblReprintRcpt property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblReprintRcpt()
	{
		if (ivjtblReprintRcpt == null)
		{
			try
			{
				ivjtblReprintRcpt = new RTSTable();
				ivjtblReprintRcpt.setName("tblReprintRcpt");
				getJScrollPane1().setColumnHeaderView(
					ivjtblReprintRcpt.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblReprintRcpt.setModel(
					new com.txdot.isd.rts.client.misc.ui.TMRPR001());
				ivjtblReprintRcpt.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblReprintRcpt.setShowVerticalLines(false);
				ivjtblReprintRcpt.setShowHorizontalLines(false);
				ivjtblReprintRcpt.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblReprintRcpt.setBounds(0, 0, 300, 100);
				// user code begin {1}
				caReceiptsTableModel =
					(TMRPR001) ivjtblReprintRcpt.getModel();
				TableColumn a =
					ivjtblReprintRcpt.getColumn(
						ivjtblReprintRcpt.getColumnName(0));
				a.setPreferredWidth(35);
				TableColumn b =
					ivjtblReprintRcpt.getColumn(
						ivjtblReprintRcpt.getColumnName(1));
				b.setPreferredWidth(210);
				TableColumn c =
					ivjtblReprintRcpt.getColumn(
						ivjtblReprintRcpt.getColumnName(2));
				c.setPreferredWidth(75);
				ivjtblReprintRcpt.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblReprintRcpt.init();
				ivjtblReprintRcpt.setBackground(Color.white);
				//		ivjtblReprintRcpt.setNextFocusableComponent(
				//			getchkReprintReceipt());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblReprintRcpt;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param exception Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
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
			// user code end
			setName("FrmReprintReceiptRPR001");
			setSize(665, 347);
			setTitle(RPR001_FRM_TITLE);
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setContentPane(getFrmReprintReceiptRPR001ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}
	// defect 7892
	//		/**
	//		 * keyPressed
	//		 * 
	//		 * @param e java.awt.event.KeyEvent
	//		 */
	//		public void keyPressed(KeyEvent aaKE)
	//		{
	//			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT
	//				|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
	//			{
	//				if (getButtonPanel1().getBtnEnter().hasFocus())
	//					getButtonPanel1().getBtnCancel().requestFocus();
	//				else if (getButtonPanel1().getBtnCancel().hasFocus())
	//					getButtonPanel1().getBtnHelp().requestFocus();
	//				else if (getButtonPanel1().getBtnHelp().hasFocus())
	//					getButtonPanel1().getBtnEnter().requestFocus();
	//			}
	//			else if (
	//				aaKE.getKeyCode() == KeyEvent.VK_LEFT
	//					|| aaKE.getKeyCode() == KeyEvent.VK_UP)
	//			{
	//				if (getButtonPanel1().getBtnCancel().hasFocus())
	//					getButtonPanel1().getBtnEnter().requestFocus();
	//				else if (getButtonPanel1().getBtnHelp().hasFocus())
	//					getButtonPanel1().getBtnCancel().requestFocus();
	//				else if (getButtonPanel1().getBtnEnter().hasFocus())
	//					getButtonPanel1().getBtnHelp().requestFocus();
	//			}
	//			super.keyPressed(aaKE);
	//		}
	// end defect 7892
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
			FrmReprintReceiptRPR001 laFrmReprintReceiptRPR001;
			laFrmReprintReceiptRPR001 = new FrmReprintReceiptRPR001();
			laFrmReprintReceiptRPR001.setModal(true);
			laFrmReprintReceiptRPR001
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWe)
				{
					System.exit(0);
				}
			});
			laFrmReprintReceiptRPR001.show();
			java.awt.Insets laInsets =
				laFrmReprintReceiptRPR001.getInsets();
			laFrmReprintReceiptRPR001.setSize(
				laFrmReprintReceiptRPR001.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmReprintReceiptRPR001.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmReprintReceiptRPR001.setVisible(true);
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
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			caMap = (Map) aaDataObject;

			// defect 7283 
			// Reference Miscellaneous Constants for Map Entry Types  
			cbShouldShowOverride =
				((Boolean) caMap
					.get(MiscellaneousConstant.MAP_ENTRY_SHOW))
					.booleanValue();
			cvReceipts =
				(Vector) caMap.get(
					MiscellaneousConstant.MAP_ENTRY_DATA);
			// defect 7019
			boolean lbEnableCashReceipt =
				((Boolean) caMap
					.get(MiscellaneousConstant.MAP_ENTRY_CASHRECEIPT))
					.booleanValue();
			// end defect 7019
			// end defect 7283 

			ReceiptLogData laReceiptLogData =
				(ReceiptLogData) cvReceipts.get(cvReceipts.size() - 1);

			// If Cash Receipt exists, grab it into object, and enable
			// checkbox
			if (laReceiptLogData
				.getTransType()
				.trim()
				.equals(TXT_CASH_RECEIPT))
			{
				caCashRcptLogData =
					(ReceiptLogData) cvReceipts.get(
						cvReceipts.size() - 1);
				cvReceipts.removeElementAt(cvReceipts.size() - 1);
				// defect 7019
				ivjchkReprintReceipt.setEnabled(lbEnableCashReceipt);
				// end defect 7019
			}
			else
				ivjchkReprintReceipt.setEnabled(false);

			//Add receipts to table	
			if (cvReceipts.size() > 0)
			{
				caReceiptsTableModel.add(cvReceipts);
				ivjtblReprintRcpt.setRowSelectionInterval(0, 0);
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leRTSEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			leRTSEx.displayError(this);
			leRTSEx = null;
		}
	}
}
