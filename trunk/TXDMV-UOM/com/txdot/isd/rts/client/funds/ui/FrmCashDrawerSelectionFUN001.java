package com.txdot.isd.rts.client.funds.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.CashWorkstationCloseOutData;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.FundsReportData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 * FrmCashDrawerSelectionFUN001.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * 	MAbs		05/17/2002	Unselected "Select All Rows" a mouse
 *							deselects a row CQU100003981
 * 	Jeff S.		09/22/2003	Handle Shift+tabbing.
 *							Changed: keyPressed()
 *							Defect# 6591 - ver 5.1.6
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							Remove handling arrow keys in button panel.
 * 							Remove unused methods and variables.
 * 							modify getButtonPanel1()
 * 							delete implements KeyListener,
 * 							getLocalColumnModelDefaultListSelectionModel(),
 * 							getScrollPaneTable()
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/28/2005	Comment out keyPressed() method. Code no
 * 							longer necessary due to 8240.
 * 							defect 7886 Ver 5.2.3
 * K Harrell	12/16/2005	Decrease size w/in ScrollPanel for 
 * 							CashDrawer. 
 * 							modify gettblCashDrawers()
 * 							defect 7886 Ver 5.2.3 
 * K Harrell	05/28/2008	Display Error if HQ selects more than one 
 * 							CashDrawer. 
 * 							delete getBuilderData()
 * 							modify actionPerformed(), setData(),
 * 							 valueChanged()
 * 							defect 9653 Ver Defect_POS_A 
 * K Harrell	06/08/2009	Implement FundsConstant.  Additional class
 * 							cleanup. 
 * 							modify gettblCashDrawers(),actionPerformed()
 * 							defect 9943 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/** 
 * Screen presents list of selected cash drawers, and prompts user to
 * verify close out operation.
 *
 * @version	Defect_POS_F	06/08/2009 
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmCashDrawerSelectionFUN001
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{

	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSelectAllCashDrawers = null;
	private JPanel ivjFrmCashDrawerSelectionFUN001ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblCashDrawers = null;

	// Objects 
	private TMFUN001 caCashDrawerTableModel = null;
	private FundsData caFundsData = null;

	// Constants 
	private final static String SELECT_ALL_CASH_DRWRS =
		"Select All Cash Drawers";
	private final static String SELECT_CASH_DRWRS =
		"Select Cash Drawer(s):";
	private final static String TITLE_FUN001 =
		"Cash Drawer Selection    FUN001";

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
			FrmCashDrawerSelectionFUN001 laFrmCashDrawerSelectionFUN001;
			laFrmCashDrawerSelectionFUN001 =
				new FrmCashDrawerSelectionFUN001();
			laFrmCashDrawerSelectionFUN001.setModal(true);
			laFrmCashDrawerSelectionFUN001
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmCashDrawerSelectionFUN001.show();
			java.awt.Insets laInsets =
				laFrmCashDrawerSelectionFUN001.getInsets();
			laFrmCashDrawerSelectionFUN001.setSize(
				laFrmCashDrawerSelectionFUN001.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmCashDrawerSelectionFUN001.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmCashDrawerSelectionFUN001.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of util.JDialogTxDot");
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmCashDrawerSelectionFUN001 constructor comment.
	 */
	public FrmCashDrawerSelectionFUN001()
	{
		super();
		initialize();
	}

	/**
	 * FrmCashDrawerSelectionFUN001 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmCashDrawerSelectionFUN001(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmCashDrawerSelectionFUN001 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCashDrawerSelectionFUN001(JFrame aaParent)
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

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				//Create vectors to store selected rows from table drawers	
				Vector lvSelectedRows =
					new Vector(
						ivjtblCashDrawers.getSelectedRowNumbers());
				Vector lvDrawers = new java.util.Vector();

				//Sort the order of the selected rows in case user
				//selected rows out of order
				UtilityMethods.sort(lvSelectedRows);

				// defect 9653 
				// HQ can only select one row 
				if (SystemProperty.isHQ() && lvSelectedRows.size() > 1)
				{
					// defect 9943
					//RTSException leRTSEx = new RTSException(652);
					RTSException leRTSEx = new RTSException(
					ErrorsConstant.ERR_NUM_ONLY_ONE_SELECTION_CAN_BE_MADE);
					// end defect 9943 
					leRTSEx.displayError(this);
					return;
				}
				// end defect 9653 

				//Add each row from the total list of CashDrawers to the
				//drawers vector
				for (int i = 0; i < lvSelectedRows.size(); i++)
				{
					String lsRow = lvSelectedRows.get(i).toString();
					int liRow2 = Integer.parseInt(lsRow);
					lvDrawers.add(
						(CashWorkstationCloseOutData) caFundsData
							.getCashDrawers()
							.get(
							liRow2));
				}

				//If no drawers are selected, throw msg saying at least
				//one drawer must be selected
				if (lvDrawers.size() == 0)
				{
					// defect 9943
					//RTSException leRTSEx = new RTSException(653);
					RTSException leRTSEx = new RTSException(
						ErrorsConstant.ERR_NUM_ONE_SELECTION_MUST_BE_PICKED);
					// end defect 9943 
					leRTSEx.displayError(this);
					return;
				}

				//If drawers are selected, store in the FundsData object
				caFundsData.setSelectedCashDrawers(lvDrawers);
				//Intitialze that FUN002 has not been displayed for
				//screen navigation purposes
				caFundsData.setDisplayedFUN002(false);

				//If all cash drawers are selected, set indicator to
				//true so SQL will select all drawers at once instead of
				//selecting each drawer at a time
				//Should evaluate if we really need this indicator?
				if (getchkSelectAllCashDrawers().isSelected())
				{
					caFundsData.setAllCashDrawers(true);
				}
				else
				{
					caFundsData.setAllCashDrawers(false);
				}
				//Fire enter command	
				getController().processData(
					AbstractViewController.ENTER,
					caFundsData);
			}

			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caFundsData);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.FUN001);
			}
			//If user selects SelectAllCashDrawers checkbox, select all
			//rows in table
			else if (aaAE.getSource() == getchkSelectAllCashDrawers())
			{
				if (getchkSelectAllCashDrawers().isSelected())
				{
					gettblCashDrawers().selectAllRows(
						gettblCashDrawers().getRowCount());
				}
				else
				{
					gettblCashDrawers().unselectAllRows();
				}
			}

		}

		finally
		{
			doneWorking();
		}
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
				ivjButtonPanel1.setBounds(16, 236, 409, 44);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMaximumSize(
				new java.awt.Dimension(197, 25));
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(197, 25));
				ivjButtonPanel1.setRequestFocusEnabled(false);
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
	 * Return the ivjchkSelectAllCashDrawers property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSelectAllCashDrawers()
	{
		if (ivjchkSelectAllCashDrawers == null)
		{
			try
			{
				ivjchkSelectAllCashDrawers = new JCheckBox();
				ivjchkSelectAllCashDrawers.setBounds(135, 210, 172, 22);
				ivjchkSelectAllCashDrawers.setName(
					"ivjchkSelectAllCashDrawers");
				ivjchkSelectAllCashDrawers.setText(
					SELECT_ALL_CASH_DRWRS);
				ivjchkSelectAllCashDrawers.setMaximumSize(
					new java.awt.Dimension(159, 22));
				ivjchkSelectAllCashDrawers.setActionCommand(
					SELECT_ALL_CASH_DRWRS);
				ivjchkSelectAllCashDrawers.setMinimumSize(
					new java.awt.Dimension(159, 22));
				// user code begin {1}
				ivjchkSelectAllCashDrawers.setMnemonic(KeyEvent.VK_S);				
				ivjchkSelectAllCashDrawers.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSelectAllCashDrawers;
	}

	/**
	 * Return the FrmCashDrawerSelectionFUN001ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmCashDrawerSelectionFUN001ContentPane1()
	{
		if (ivjFrmCashDrawerSelectionFUN001ContentPane1 == null)
		{
			try
			{
				ivjFrmCashDrawerSelectionFUN001ContentPane1 =
					new JPanel();
				ivjFrmCashDrawerSelectionFUN001ContentPane1.setName(
					"ivjFrmCashDrawerSelectionFUN001ContentPane1");
				ivjFrmCashDrawerSelectionFUN001ContentPane1.setLayout(null);
				ivjFrmCashDrawerSelectionFUN001ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmCashDrawerSelectionFUN001ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmCashDrawerSelectionFUN001ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				ivjFrmCashDrawerSelectionFUN001ContentPane1.add(getJPanel1(), null);
				ivjFrmCashDrawerSelectionFUN001ContentPane1.add(getchkSelectAllCashDrawers(), null);
				ivjFrmCashDrawerSelectionFUN001ContentPane1.add(getButtonPanel1(), null);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmCashDrawerSelectionFUN001ContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						SELECT_CASH_DRWRS));
				ivjJPanel1.setLayout(null);

				ivjJPanel1.add(getJScrollPane1(), null);
				ivjJPanel1.setBounds(7, 9, 427, 197);
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
				ivjJScrollPane1.setName("ivjJScrollPane1");
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
				getJScrollPane1().setViewportView(gettblCashDrawers());
				// user code begin {1}
				ivjJScrollPane1.setBounds(12, 26, 402, 161);
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
	 * Return the ivjtblCashDrawers property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblCashDrawers()
	{
		if (ivjtblCashDrawers == null)
		{
			try
			{
				ivjtblCashDrawers =	new RTSTable();
				ivjtblCashDrawers.setName("ivjtblCashDrawers");
				getJScrollPane1().setColumnHeaderView(
					ivjtblCashDrawers.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblCashDrawers.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblCashDrawers.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN001());
				ivjtblCashDrawers.setShowVerticalLines(false);
				ivjtblCashDrawers.setShowHorizontalLines(false);
				ivjtblCashDrawers.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblCashDrawers.setBounds(0, -2, 390, 129);
				ivjtblCashDrawers.setSelectionBackground(
					new java.awt.Color(204, 204, 255));

				// user code begin {1}
				caCashDrawerTableModel =
					(TMFUN001) ivjtblCashDrawers.getModel();

				// defect 9943
				// CashDrawer
				TableColumn laTableColumnA =
					ivjtblCashDrawers.getColumn(
					//ivjtblCashDrawers.getColumnName(0));
				ivjtblCashDrawers.getColumnName(
				FundsConstant.FUN001_CASH_DRAWER));
				laTableColumnA.setPreferredWidth(95);

				// Last Closeout 
				TableColumn laTableColumnB =
					ivjtblCashDrawers.getColumn(
					//ivjtblCashDrawers.getColumnName(1));
				ivjtblCashDrawers.getColumnName(
				FundsConstant.FUN001_LAST_CLOSEOUT));
				laTableColumnB.setPreferredWidth(125);

				// Last Current Status  
				TableColumn laTableColumnC =
					ivjtblCashDrawers.getColumn(
						ivjtblCashDrawers.getColumnName(
							FundsConstant.FUN001_LAST_CURRENT_STATUS));
				//ivjtblCashDrawers.getColumnName(2));
				// end defect 9943
				
				laTableColumnC.setPreferredWidth(125);

				ivjtblCashDrawers.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblCashDrawers.init();
				ivjtblCashDrawers.addActionListener(this);
				ivjtblCashDrawers.addMultipleSelectionListener(this);
				ivjtblCashDrawers.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjtblCashDrawers;
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
			// user code end
			setName("FrmCashDrawerSelectionFUN001");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(450, 311);
			setTitle(TITLE_FUN001);
			setContentPane(
				getFrmCashDrawerSelectionFUN001ContentPane1());
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
	}

	/**
	 * All subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information to the
	 * view.
	 * All cash drawers, their close out, and last current status are
	 * returned in dataObject. This data is then set on the table.
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			if (aaDataObject != null)
			{
				caFundsData = (FundsData) aaDataObject;
				//Add vector of rows in drawers object to Table
				caCashDrawerTableModel.add(
					caFundsData.getCashDrawers());

				//Initalize Date Range for backwards navigation on
				//FUN007
				FundsReportData laFundsReportData =
					(FundsReportData) caFundsData.getFundsReportData();
				if (laFundsReportData != null)
				{
					laFundsReportData.setRange(0);
				}
				// defect 9653
				if (SystemProperty.isHQ())
				{
					getchkSelectAllCashDrawers().setEnabled(false);
				}
				// end defect 9653 
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			leEx.displayError(this);
			leEx = null;
		}
	}

	/** 
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aaLSE javax.swing.event.ListSelectionEvent
	  */
	public void valueChanged(
		javax.swing.event.ListSelectionEvent aaLSE)
	{
		if (aaLSE.getValueIsAdjusting())
		{
			return;
		}
		Vector lvSelectedRows =
			new Vector(ivjtblCashDrawers.getSelectedRowNumbers());
		if (lvSelectedRows.size() == ivjtblCashDrawers.getRowCount())
		{
			// defect 9653
			getchkSelectAllCashDrawers().setSelected(
				!SystemProperty.isHQ());
			// end defect 9653 
		}
		else
		{
			getchkSelectAllCashDrawers().setSelected(false);
		}
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
