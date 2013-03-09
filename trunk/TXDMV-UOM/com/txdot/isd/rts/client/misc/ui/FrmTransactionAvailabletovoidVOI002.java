package com.txdot.isd.rts.client.misc.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.data.TransactionCodesData;
import com.txdot.isd.rts.services.data.VoidTransactionData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 *
 * FrmTransactionAvailabletovoidVOI002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Tulsiani	04/30/2002	Increased table size to accommodate longer 
 *							trans descriptions
 *							defect 3721
 * K Harrell	07/18/2004	Checkbox selected when all transactions
 *							selected
 *							add ListSelectionListener
 *							add valueChanged()
 *							modify gettblTransactions()
 *							defect 7328  Ver 5.2.1
 * J Zwiener	03/04/2005	Java 1.4
 * 							defect 7892 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7899  Ver 5.2.3
 * J Zwiener	06/22/2005	Java 1.4 changes
 * 							Format source, organize imports, rename
 * 							fields, comment out methods.
 * 							defect 7892 Ver 5.2.3           
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	04/05/2007	Modify to check for same special plate
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * ---------------------------------------------------------------------
 */

/**
 * Screen returns the transactions associated with the entered TransId
 * on VOI001
 *
 * @version	Special Plates	04/05/2007
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmTransactionAvailabletovoidVOI002
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private static final String TXT_WANT_TO_VOID =
		"Are you sure you want to void the selected transaction(s)?";
	private static final String TXT_VOID_ALL =
		"Void entire set of transactions";
	private static final String TXT_SELECT_TRANS_TO_VOID =
		"Select transaction(s) to be voided";
	private static final String VOI002_FRM_TITLE =
		"Transaction(s) Available to Void   VOI002";

	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkVoidentireset = null;
	private JLabel ivjstcLblSelectTransaction = null;
	private JPanel ivjFrmTransactionAvailabletovoidVOI002ContentPane1 =
		null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblTransactions = null;
	private TMVOI002 caTransactionTableModel = null;

	// Object
	private VoidTransactionData caCreditCardTrans = null;

	// Vector
	private Vector cvData = null;

	/**
	 * FrmTransactionAvailabletovoidVOI002 constructor comment.
	 */
	public FrmTransactionAvailabletovoidVOI002()
	{
		super();
		initialize();
	}
	/**
	 * FrmTransactionAvailabletovoidVOI002 constructor comment.
	 * 
	 * @param owner java.awt.Dialog
	 */
	public FrmTransactionAvailabletovoidVOI002(Dialog owner)
	{
		super(owner);
		initialize();

	}
	/**
	 * FrmTransactionAvailabletovoidVOI002 constructor comment.
	 */
	public FrmTransactionAvailabletovoidVOI002(JFrame parent)
	{
		super(parent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		//Code to avoid clicking on the button multiple times.
		if (!startWorking())
			return;

		try
		{
			//field validation
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				//Create vectors to store selected transactions to void
				Vector lvTransactions = new java.util.Vector();
				Vector lvSelectedRows =
					new Vector(
						gettblTransactions().getSelectedRowNumbers());

				// Intialize all rows to not selected
				for (int i = 0; i < cvData.size(); i++)
				{
					((VoidTransactionData) cvData.get(i)).setSelected(
						false);
				}

				// Sort user selected rows, if they were selected 
				// out of order
				UtilityMethods.sort(lvSelectedRows);
				//Add each row to vector of transactions	
				for (int i = 0; i < lvSelectedRows.size(); i++)
				{
					String lsRow = lvSelectedRows.get(i).toString();
					int liRow = Integer.parseInt(lsRow);
					lvTransactions.add(cvData.get(liRow));
					//Set row to selected for validation purposes
					(
						(VoidTransactionData) cvData.get(
							liRow)).setSelected(
						true);
				}

				// If no rows are selected, msg that at least one row 
				// must be selected
				if (lvTransactions.size() == 0)
				{
					RTSException leInvalidSelection =
						new RTSException(332);
					leInvalidSelection.displayError(this);
					return;
				}

				//If trans is not voidable present exception message
				for (int i = 0; i < lvTransactions.size(); i++)
				{
					VoidTransactionData aTransaction =
						(VoidTransactionData) lvTransactions.get(i);
					TransactionCodesData lTransactionCodesData =
						TransactionCodesCache.getTransCd(
							aTransaction.getTransCd());
					if (lTransactionCodesData.getVoidableTransIndi()
						== 0)
					{
						RTSException leInvalidDateException =
							new RTSException(194);
						leInvalidDateException.displayError(this);
						return;
					}
				}

				//If trans has already been voided
				for (int i = 0; i < lvTransactions.size(); i++)
				{
					VoidTransactionData laTransaction =
						(VoidTransactionData) lvTransactions.get(i);

					if (laTransaction.getVoidedTransIndi() == 1)
					{
						RTSException leInvalidDateException =
							new RTSException(329);
						leInvalidDateException.displayError(this);
						return;
					}
				}

				// defect 9085 
				// Also check for Same Special Plate 
				// Check voiding all parts if same VIN
				for (int j = 0; j < cvData.size(); j++)
				{
					VoidTransactionData laTransaction =
						(VoidTransactionData) cvData.get(j);
					if (laTransaction.isSelected())
					{
						String lsVIN = laTransaction.getVIN();
						String lsSpclPltRegPltNo =
							laTransaction.getSpclPltRegPltNo();
						if (!lsVIN.equals("")
							|| !lsSpclPltRegPltNo.equals(""))
						{
							for (int k = j + 1; k < cvData.size(); k++)
							{
								VoidTransactionData laTransaction2 =
									(VoidTransactionData) cvData.get(k);
								// If Not Voided 
								// && VINs match || SpclPltRegPltNo match
								// && next transaction not selected 
								if (laTransaction2.getVoidedTransIndi()
									!= 1
									&& (!lsVIN.equals("")
										&& lsVIN.equals(
											laTransaction2.getVIN())
										|| (!lsSpclPltRegPltNo.equals("")
											&& lsSpclPltRegPltNo.equals(
												laTransaction2
													.getSpclPltRegPltNo()))))
								{
									if (!((VoidTransactionData) cvData
										.get(k))
										.isSelected())
									{
										RTSException leInvalidDateException =
											new RTSException(575);
										leInvalidDateException
											.displayError(
											this);
										return;
									}
								}
							}
						}
					}
				}
				// end defect 9085 

				//Confirmation message
				RTSException leRTSExMsg =
					new RTSException(
						RTSException.CTL001,
						TXT_WANT_TO_VOID,
						null);
				int liReturnCd = leRTSExMsg.displayError(this);
				if (liReturnCd == RTSException.YES)
				{
					Vector lvSentInfo = new Vector();
					//Check if we should void the credit card trans fee
					if (caCreditCardTrans != null)
					{
						boolean lbVoidCreditCardTrans = true;
						for (int i = 0; i < cvData.size(); i++)
						{
							VoidTransactionData laTransaction =
								(VoidTransactionData) cvData.get(i);
							//If a trans is not voided, and it is not selected to be voided, then do not void creditcardfee
							if (laTransaction.getVoidedTransIndi() == 0
								&& !laTransaction.isSelected())
								lbVoidCreditCardTrans = false;
						}
						if (lbVoidCreditCardTrans)
						{
							lvTransactions.add(caCreditCardTrans);
							cvData.add(caCreditCardTrans);
						}
					}
					lvSentInfo.addElement(lvTransactions);
					lvSentInfo.addElement(cvData);
					getController().processData(
						AbstractViewController.ENTER,
						lvSentInfo);
				}
				else
				{
					return;
				}

			} //If user presses Cancel
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			//If user presses Help
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.VOI002);
			}
			//If user selects getchkVoidentireset checkbox
			else if (aaAE.getSource() == getchkVoidentireset())
			{
				if (getchkVoidentireset().isSelected())
				{
					gettblTransactions().selectAllRows(
						gettblTransactions().getRowCount());
				}
				else
				{
					gettblTransactions().unselectAllRows();
				}
			}

		}

		catch (RTSException aeRTSEx)
		{
			if (aeRTSEx.getMsgType().equals(RTSException.MF_DOWN))
			{
				RTSException aeMFDownException =
					new RTSException(RTSException.MF_DOWN_MSG);
				aeMFDownException.displayError(this);
				return;
			}
		}

		finally
		{
			doneWorking();
		}
	}
	/**
	 * Return the ButtonPanel1 property value.
	 * 
	 * @return util.ButtonPanel
	 */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
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
	 * Return the chkVoidentireset property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	private javax.swing.JCheckBox getchkVoidentireset()
	{
		if (ivjchkVoidentireset == null)
		{
			try
			{
				ivjchkVoidentireset = new javax.swing.JCheckBox();
				ivjchkVoidentireset.setName("chkVoidentireset");
				ivjchkVoidentireset.setMnemonic(86);
				ivjchkVoidentireset.setText(TXT_VOID_ALL);
				ivjchkVoidentireset.setMaximumSize(
					new java.awt.Dimension(196, 22));
				ivjchkVoidentireset.setActionCommand(TXT_VOID_ALL);
				ivjchkVoidentireset.setMinimumSize(
					new java.awt.Dimension(196, 22));
				// user code begin {1}
				ivjchkVoidentireset.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkVoidentireset;
	}
	/**
	 * Return the FrmTransactionAvailabletovoidVOI002ContentPane1 
	 * property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax
		.swing
		.JPanel getFrmTransactionAvailabletovoidVOI002ContentPane1()
	{
		if (ivjFrmTransactionAvailabletovoidVOI002ContentPane1 == null)
		{
			try
			{
				ivjFrmTransactionAvailabletovoidVOI002ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmTransactionAvailabletovoidVOI002ContentPane1
					.setName(
					"FrmTransactionAvailabletovoidVOI002ContentPane1");
				ivjFrmTransactionAvailabletovoidVOI002ContentPane1
					.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmTransactionAvailabletovoidVOI002ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmTransactionAvailabletovoidVOI002ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(564, 250));
				java
					.awt
					.GridBagConstraints constraintsstcLblSelectTransaction =
					new java.awt.GridBagConstraints();
				constraintsstcLblSelectTransaction.gridx = 1;
				constraintsstcLblSelectTransaction.gridy = 1;
				constraintsstcLblSelectTransaction.ipadx = 35;
				constraintsstcLblSelectTransaction.insets =
					new java.awt.Insets(13, 8, 8, 364);
				getFrmTransactionAvailabletovoidVOI002ContentPane1()
					.add(
					getstcLblSelectTransaction(),
					constraintsstcLblSelectTransaction);
				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 2;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 562;
				constraintsJScrollPane1.ipady = 154;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(8, 8, 9, 8);
				getFrmTransactionAvailabletovoidVOI002ContentPane1()
					.add(
					getJScrollPane1(),
					constraintsJScrollPane1);
				java.awt.GridBagConstraints constraintschkVoidentireset =
					new java.awt.GridBagConstraints();
				constraintschkVoidentireset.gridx = 1;
				constraintschkVoidentireset.gridy = 3;
				constraintschkVoidentireset.ipadx = 21;
				constraintschkVoidentireset.insets =
					new java.awt.Insets(10, 191, 8, 192);
				getFrmTransactionAvailabletovoidVOI002ContentPane1()
					.add(
					getchkVoidentireset(),
					constraintschkVoidentireset);
				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 4;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 188;
				constraintsButtonPanel1.ipady = 24;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(9, 97, 10, 98);
				getFrmTransactionAvailabletovoidVOI002ContentPane1()
					.add(
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
		return ivjFrmTransactionAvailabletovoidVOI002ContentPane1;
	}
	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return javax.swing.JScrollPane
	 */
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
				getJScrollPane1().setViewportView(gettblTransactions());
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
	 * Return the stcLblSelectTransaction property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getstcLblSelectTransaction()
	{
		if (ivjstcLblSelectTransaction == null)
		{
			try
			{
				ivjstcLblSelectTransaction = new javax.swing.JLabel();
				ivjstcLblSelectTransaction.setName(
					"stcLblSelectTransaction");
				ivjstcLblSelectTransaction.setText(
					TXT_SELECT_TRANS_TO_VOID);
				ivjstcLblSelectTransaction.setMaximumSize(
					new java.awt.Dimension(193, 14));
				ivjstcLblSelectTransaction.setMinimumSize(
					new java.awt.Dimension(193, 14));
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
		return ivjstcLblSelectTransaction;
	}
	/**
	 * Return the tblTransactions property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblTransactions()
	{
		if (ivjtblTransactions == null)
		{
			try
			{
				ivjtblTransactions =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjtblTransactions.setName("tblTransactions");
				getJScrollPane1().setColumnHeaderView(
					ivjtblTransactions.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblTransactions.setModel(
					new com.txdot.isd.rts.client.misc.ui.TMVOI002());
				ivjtblTransactions.setAutoResizeMode(0);
				ivjtblTransactions.setShowVerticalLines(false);
				ivjtblTransactions.setShowHorizontalLines(false);
				ivjtblTransactions.setAutoCreateColumnsFromModel(false);
				ivjtblTransactions.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblTransactions.setBounds(0, 0, 727, 200);
				// user code begin {1}
				caTransactionTableModel =
					(TMVOI002) ivjtblTransactions.getModel();
				TableColumn laTblColA =
					ivjtblTransactions.getColumn(
						ivjtblTransactions.getColumnName(0));
				laTblColA.setPreferredWidth(130);
				TableColumn laTblColB =
					ivjtblTransactions.getColumn(
						ivjtblTransactions.getColumnName(1));
				laTblColB.setPreferredWidth(160);
				TableColumn laTblColC =
					ivjtblTransactions.getColumn(
						ivjtblTransactions.getColumnName(2));
				laTblColC.setPreferredWidth(400);
				ivjtblTransactions.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblTransactions.init();
				ivjtblTransactions.addActionListener(this);
				ivjtblTransactions.addMultipleSelectionListener(this);
				ivjtblTransactions.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblTransactions;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// UUncomment the following lines to print uncaught 
		// exceptions to stdout 
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
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
			// user code end
			setName("FrmTransactionAvailabletovoidVOI002");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(625, 350);
			setTitle(VOI002_FRM_TITLE);
			setContentPane(
				getFrmTransactionAvailabletovoidVOI002ContentPane1());
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
	 * @param args String[]
	 */
	public static void main(String[] args)
	{
		try
		{
			FrmTransactionAvailabletovoidVOI002 aFrmTransactionAvailabletovoidVOI002;
			aFrmTransactionAvailabletovoidVOI002 =
				new FrmTransactionAvailabletovoidVOI002();
			aFrmTransactionAvailabletovoidVOI002.setModal(true);
			aFrmTransactionAvailabletovoidVOI002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			aFrmTransactionAvailabletovoidVOI002.show();
			java.awt.Insets insets =
				aFrmTransactionAvailabletovoidVOI002.getInsets();
			aFrmTransactionAvailabletovoidVOI002.setSize(
				aFrmTransactionAvailabletovoidVOI002.getWidth()
					+ insets.left
					+ insets.right,
				aFrmTransactionAvailabletovoidVOI002.getHeight()
					+ insets.top
					+ insets.bottom);
			aFrmTransactionAvailabletovoidVOI002.setVisible(true);
		}
		catch (Throwable exception)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			exception.printStackTrace(System.out);
		}
	}
	/**
	 * all subclasses must implement this method - it sets the data on the screen and is how the controller relays information
	 * to the view
	 */
	public void setData(Object aaData)
	{
		try
		{
			if (aaData != null)
			{
				cvData = (Vector) aaData;
				for (int i = 0; i < cvData.size(); i++)
				{
					String lsTransCd =
						((VoidTransactionData) cvData.get(i))
							.getTransCd();
					if (lsTransCd.equals(TransCdConstant.CCPYMNT))
					{
						caCreditCardTrans =
							(VoidTransactionData) cvData.get(i);
						cvData.removeElementAt(i);
					}
				} //Add vector of rows in drawers object to Table
				caTransactionTableModel.add(cvData);
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
	/** 
	 * Called whenever the value of the selection changes.
	 * 
	 * @param aeLSE the event that characterizes the change.
	 */
	public void valueChanged(
		javax.swing.event.ListSelectionEvent aeLSE)
	{
		if (aeLSE.getValueIsAdjusting())
		{
			return;
		}
		Vector lvSelectedRows =
			new Vector(ivjtblTransactions.getSelectedRowNumbers());
		if (lvSelectedRows.size() == ivjtblTransactions.getRowCount())
		{
			getchkVoidentireset().setSelected(true);
		}
		else
		{
			getchkVoidentireset().setSelected(false);
		}
	}
}
