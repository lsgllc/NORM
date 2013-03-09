package com.txdot.isd.rts.client.misc.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.TransactionHeaderData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.MiscellaneousConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSelectTransactionCUS001.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * N Ting		05/30/2002	Fix CQU100004127, increase size
 * B Arredondo	12/03/2002	Made changes to user help guide so had 
 *							to make changes in actionPerformed().
 * B Arredondo	12/16/2002	Fixing Defect# 5147. Made changes for the 
 *							user help guide so had to make changes
 *							in actionPerformed().
 * J Zwiener	06/22/2005	Java 1.4 changes
 * 							Format source, organize imports, rename
 * 							fields, comment out methods.
 * 							defect 7892 Ver 5.2.3
 * B Hargrove	08/11/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	01/29/2011	Display Err Msg if WebAgnt Transactions 
 * 							 Exist; Do not allow processing. 
 * 							Class Cleanup.   
 * 							modify setData(), actionPerformed()  
 * 							defect 10734 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Screen presents a list of pending transactions
 * 
 * @version	6.7.0 			01/29/2011
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmSelectTransactionCUS001
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjbuttonPanel = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JPanel ivjFrmSelectTransactionCUS001ContentPane1 = null;
	private RTSTable ivjtblTransactions = null;

	// Table Model 
	private TMCUS001 laTMCUS001;

	// Vector 
	private Vector cvData;

	// Constant 	
	private static final String CUS001_FRM_TITLE =
		"Select Transaction    CUS001";

	/**
	 * FrmSelectTransactionCUS001 constructor comment.
	 */
	public FrmSelectTransactionCUS001()
	{
		super();
		initialize();
	}

	/**
	 * FrmSelectTransactionCUS001 constructor comment.
	 * 
	 * @param owner java.awt.Dialog
	 */
	public FrmSelectTransactionCUS001(Dialog owner)
	{
		super(owner);
		initialize();
	}

	/**
	 * FrmSelectTransactionCUS001 constructor comment.
	 */
	public FrmSelectTransactionCUS001(JFrame parent)
	{
		super(parent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE 
	 */
	public void actionPerformed(java.awt.event.ActionEvent aaAE)
	{
		if (!startWorking())
		{
			return;
		}

		try
		{
			clearAllColor(this);

			// ENTER	
			if (aaAE.getSource() == getButtonPanel().getBtnEnter()
				|| aaAE.getSource() == gettblTransactions())
			{
				TransactionHeaderData laTransHdrData =
					new TransactionHeaderData();

				for (int i = 0;
					i < ivjtblTransactions.getRowCount();
					i++)
				{
					if (ivjtblTransactions.isRowSelected(i) == true)
					{
						laTransHdrData =
							(TransactionHeaderData) cvData.get(i);

						if (laTransHdrData.isWebAgntTrans())
						{
							new RTSException(
								ErrorsConstant
									.ERR_NUM_WEBAGNT_TRANS_NOT_AVAIL)
								.displayError();
							return;
						}
						break;
					}
				}
				getController().processData(
					AbstractViewController.ENTER,
					laTransHdrData);
			}
			// CANCEL
			else if (
				aaAE.getSource() == getButtonPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// HELP
			else if (aaAE.getSource() == getButtonPanel().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.CUS001);
			}
		}

		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ivjbuttonPanel property value.
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getButtonPanel()
	{
		if (ivjbuttonPanel == null)
		{
			try
			{
				ivjbuttonPanel = new ButtonPanel();
				ivjbuttonPanel.setName("ivjbuttonPanel");
				ivjbuttonPanel.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjbuttonPanel.addActionListener(this);
				ivjbuttonPanel.setAsDefault(this);
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjbuttonPanel;
	}

	/**
	 * Return the FrmSelectTransactionCUS001ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmSelectTransactionCUS001ContentPane1()
	{
		if (ivjFrmSelectTransactionCUS001ContentPane1 == null)
		{
			try
			{
				ivjFrmSelectTransactionCUS001ContentPane1 =
					new JPanel();
				ivjFrmSelectTransactionCUS001ContentPane1.setName(
					"FrmSelectTransactionCUS001ContentPane1");
				ivjFrmSelectTransactionCUS001ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmSelectTransactionCUS001ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSelectTransactionCUS001ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(1052, 446));

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 394;
				constraintsJScrollPane1.ipady = 132;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(35, 33, 4, 34);
				getFrmSelectTransactionCUS001ContentPane1().add(
					getJScrollPane1(),
					constraintsJScrollPane1);

				java.awt.GridBagConstraints constraintsbuttonPanel =
					new java.awt.GridBagConstraints();
				constraintsbuttonPanel.gridx = 1;
				constraintsbuttonPanel.gridy = 2;
				constraintsbuttonPanel.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsbuttonPanel.weightx = 1.0;
				constraintsbuttonPanel.weighty = 1.0;
				constraintsbuttonPanel.ipadx = 199;
				constraintsbuttonPanel.ipady = 42;
				constraintsbuttonPanel.insets =
					new java.awt.Insets(4, 34, 22, 33);
				getFrmSelectTransactionCUS001ContentPane1().add(
					getButtonPanel(),
					constraintsbuttonPanel);
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjFrmSelectTransactionCUS001ContentPane1;
	}

	/**
	 * Return the JScrollPane1 property value.
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
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getJScrollPane1().setViewportView(gettblTransactions());
				// user code begin {1}
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the ivjtblTrans property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblTransactions()
	{
		if (ivjtblTransactions == null)
		{
			try
			{
				ivjtblTransactions = new RTSTable();
				ivjtblTransactions.setName("ivjtblTrans");
				getJScrollPane1().setColumnHeaderView(
					ivjtblTransactions.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblTransactions.setModel(
					new com.txdot.isd.rts.client.misc.ui.TMCUS001());
				ivjtblTransactions.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblTransactions.setShowVerticalLines(false);
				ivjtblTransactions.setShowHorizontalLines(false);
				ivjtblTransactions.setAutoCreateColumnsFromModel(false);
				ivjtblTransactions.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblTransactions.setBounds(0, 0, 200, 200);
				// user code begin {1}
				laTMCUS001 = (TMCUS001) ivjtblTransactions.getModel();

				// CUSTSEQNO 
				TableColumn laTC_CustSeqNo =
					ivjtblTransactions.getColumn(
						ivjtblTransactions.getColumnName(
							MiscellaneousConstant
								.CUS001_COL_CUSTSEQNO));
				laTC_CustSeqNo.setPreferredWidth(20);

				// TRANSNAME 
				TableColumn laTC_TransName =
					ivjtblTransactions.getColumn(
						ivjtblTransactions.getColumnName(
							MiscellaneousConstant
								.CUS001_COL_TRANSNAME));
				laTC_TransName.setPreferredWidth(180);

				ivjtblTransactions.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblTransactions.init();
				ivjtblTransactions.setBackground(Color.white);
				// user code end
			}
			catch (Throwable ivjExc)
			{
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjtblTransactions;
	}

	/**
	 * Called whenever the part throws an exception.
	 * @param exception Throwable
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
			// user code end
			setName("FrmSelectTransactionCUS001");
			setSize(483, 300);
			setTitle(CUS001_FRM_TITLE);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setContentPane(getFrmSelectTransactionCUS001ContentPane1());
		}
		catch (Throwable ivjExc)
		{
			handleException(ivjExc);
		}
		// user code begin {2}
		// user code end
	}

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
			FrmSelectTransactionCUS001 laFrmSelectTransactionCUS001;
			laFrmSelectTransactionCUS001 =
				new FrmSelectTransactionCUS001();
			laFrmSelectTransactionCUS001.setModal(true);
			laFrmSelectTransactionCUS001
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(java.awt.event.WindowEvent e)
				{
					System.exit(0);
				};
			});
			laFrmSelectTransactionCUS001.show();
			java.awt.Insets insets =
				laFrmSelectTransactionCUS001.getInsets();
			laFrmSelectTransactionCUS001.setSize(
				laFrmSelectTransactionCUS001.getWidth()
					+ insets.left
					+ insets.right,
				laFrmSelectTransactionCUS001.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmSelectTransactionCUS001.setVisible(true);
		}
		catch (Throwable exception)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			exception.printStackTrace(System.out);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData 
	 */
	public void setData(Object aaData)
	{
		try
		{
			if (aaData != null)
			{
				boolean lbWebAgntTrans = false;

				cvData = (Vector) aaData;

				// defect 10734 
				for (int i = 0; i < cvData.size(); i++)
				{
					TransactionHeaderData laTransHdrData =
						(TransactionHeaderData) cvData.elementAt(i);

					if (laTransHdrData.isWebAgntTrans())
					{
						lbWebAgntTrans = true;
					}
				}
				// end defect 10734 

				// Add vector of rows in drawers object to Table
				laTMCUS001.add(cvData);

				int liRows = ivjtblTransactions.getRowCount();

				// If no transactions exists, disable enter button
				if (liRows == 0)
				{
					getButtonPanel().getBtnEnter().setEnabled(false);
				}
				// Else highlight last row
				else
				{
					ivjtblTransactions.setRowSelectionInterval(
						liRows - 1,
						liRows - 1);
				}
				if (lbWebAgntTrans)
				{
					new RTSException(
						ErrorsConstant.ERR_NUM_WEBAGNT_TRANS_EXIST)
						.displayError();
				}
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException ex =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			ex.displayError(this);
			ex = null;
		}
	}
}
