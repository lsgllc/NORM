package com.txdot.isd.rts.client.funds.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.CashWorkstationIdsData;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 *
 * FrmResetCloseOutIndicatorFUN008.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/21/2002	Unselecting "Select All" when mouse clicked
 * Jeff S.		09/22/2003	Handle Shift+tabbing.
 *							Changed: keyPressed()
 *							defect 6591 Ver 5.1.6
 * K Harrell	06/30/2005	services.cache.*Data objects moved to 
 * 							services.data 
 * 							defect 7899 Ver 5.2.3	 
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							delete implements KeyListener
 * 							defect 7886 Ver 5.2.3
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/28/2005	Comment out keyPressed() method. Code no
 * 							longer necessary due to 8240.
 * 							defect 7886 Ver 5.2.3
 * K Harrell	11/02/2005	Consistent verbiage for "Closeout" for title
 * 							modify TITLE_FUN008
 * 							defect 8379 Ver 5.2.3
 * K Harrell	12/16/2005	Scroll bar as Needed vs. Always
 * 							modify gettblSelectedCashDrawers() 
 * 							defect 7886 Ver 5.2.3  
 * K Harrell	06/08/2009	Use ErrorsConstant, FundsConstant.  Additional
 * 							Cleanup. 
 * 							add BORDER_TXT_SELECT_CASH_DRWR,
 * 							 BORDER_TXT_SELECT_CASH_DRWRS,
 * 							 CHKBX_TXT_SELECT_ALL_CASH_DRAWERS,
 * 							 CHKBX_TXT_SELECT_CASH_DRAWER
 * 							delete SELECT_ALL_CASH_DRWRS, 
 * 						     SELECT_CASH_DRWRS
 * 							modify gettblCloseOutIndicatorDrawers(), 
 * 							 getchkSelectAllCashDrawers(), 
 * 							 getJPanel1(), setData(), validateData()
 * 							defect 9943 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/**
 * Screen presents list of all cash drawers that have the closeout
 * indicator set and their requesting workstation of the closeout.
 *
 * @version	Defect_POS_F	06/08/2009 
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmResetCloseOutIndicatorFUN008
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	 private ButtonPanel ivjButtonPanel1 = null;
	 private JCheckBox ivjchkSelectAllCashDrawers = null;
	 private JPanel ivjFrmResetCloseOutIndicatorFUN008ContentPane1 =
		 null;
	 private JPanel ivjJPanel1 = null;
	 private JScrollPane ivjJScrollPane1 = null;
	 private RTSTable ivjtblCloseOutIndicatorDrawers = null;
	 
	// Table Model 
	 private TMFUN008 caCloseOutIndicatorDrawersTableModel = null;

	 //	Object 
	 private FundsData caFundsData = null;


	// Constants
	// defect 9943 
	private final static String BORDER_TXT_SELECT_CASH_DRWR =
		"Select Cash Drawer: ";

	private final static String BORDER_TXT_SELECT_CASH_DRWRS =
		"Select Cash Drawers: ";

	private final static String CHKBX_TXT_SELECT_ALL_CASH_DRAWERS =
		"Select All Cash Drawers";

	private final static String CHKBX_TXT_SELECT_CASH_DRAWER =
		"Select Cash Drawer";
	// end defect 9943 

	private final static String TITLE_FUN008 =
		"Reset Closeout Indicator    FUN008";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmResetCloseOutIndicatorFUN008 laFrmResetCloseOutIndicatorFUN008;
			laFrmResetCloseOutIndicatorFUN008 =
				new FrmResetCloseOutIndicatorFUN008();
			laFrmResetCloseOutIndicatorFUN008.setModal(true);
			laFrmResetCloseOutIndicatorFUN008
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmResetCloseOutIndicatorFUN008.show();
			java.awt.Insets laInsets =
				laFrmResetCloseOutIndicatorFUN008.getInsets();
			laFrmResetCloseOutIndicatorFUN008.setSize(
				laFrmResetCloseOutIndicatorFUN008.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmResetCloseOutIndicatorFUN008.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmResetCloseOutIndicatorFUN008.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of javax.swing.JDialog");
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmResetCloseOutIndicatorFUN008 constructor comment.
	 */
	public FrmResetCloseOutIndicatorFUN008()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmResetCloseOutIndicatorFUN008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmResetCloseOutIndicatorFUN008(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	
	/**
	 * FrmResetCloseOutIndicatorFUN008 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmResetCloseOutIndicatorFUN008(JFrame aaParent)
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
				if (validateData())
				{
					setDataToDataObject();

					getController().processData(
						AbstractViewController.ENTER,
						caFundsData);
				}
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
				RTSHelp.displayHelp(RTSHelp.FUN008);
			}
			else if (aaAE.getSource() == getchkSelectAllCashDrawers())
			{
				if (getchkSelectAllCashDrawers().isSelected())
				{
					gettblCloseOutIndicatorDrawers().selectAllRows(
						gettblCloseOutIndicatorDrawers().getRowCount());
				}
				else
				{
					gettblCloseOutIndicatorDrawers().unselectAllRows();
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setLayout(new java.awt.GridBagLayout());
				// user code begin {1}
				ivjButtonPanel1.setBounds(29, 298, 284, 42);
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
				ivjchkSelectAllCashDrawers =
					new javax.swing.JCheckBox();
				ivjchkSelectAllCashDrawers.setBounds(87, 272, 173, 22);
				ivjchkSelectAllCashDrawers.setName(
					"ivjchkSelectAllCashDrawers");
				// defect 9943 
				//ivjchkSelectAllCashDrawers.setMnemonic(83);
				ivjchkSelectAllCashDrawers.setMnemonic(KeyEvent.VK_S);
				ivjchkSelectAllCashDrawers.setText(
					CHKBX_TXT_SELECT_ALL_CASH_DRAWERS);
				ivjchkSelectAllCashDrawers.setActionCommand(
									CHKBX_TXT_SELECT_ALL_CASH_DRAWERS);
				// end defect 9943 
				ivjchkSelectAllCashDrawers.setMaximumSize(
					new java.awt.Dimension(159, 22));
				ivjchkSelectAllCashDrawers.setMinimumSize(
					new java.awt.Dimension(159, 22));
				// user code begin {1}
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
	 * Return the ivjFrmResetCloseOutIndicatorFUN008ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmResetCloseOutIndicatorFUN008ContentPane1()
	{
		if (ivjFrmResetCloseOutIndicatorFUN008ContentPane1 == null)
		{
			try
			{
				ivjFrmResetCloseOutIndicatorFUN008ContentPane1 =
					new JPanel();
				ivjFrmResetCloseOutIndicatorFUN008ContentPane1.setName(
					"ivjFrmResetCloseOutIndicatorFUN008ContentPane1");
				ivjFrmResetCloseOutIndicatorFUN008ContentPane1
					.setLayout(
					null);
				ivjFrmResetCloseOutIndicatorFUN008ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmResetCloseOutIndicatorFUN008ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));

				ivjFrmResetCloseOutIndicatorFUN008ContentPane1.add(
					getJPanel1(),
					null);
				ivjFrmResetCloseOutIndicatorFUN008ContentPane1.add(
					getchkSelectAllCashDrawers(),
					null);
				ivjFrmResetCloseOutIndicatorFUN008ContentPane1.add(
					getButtonPanel1(),
					null);
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
		return ivjFrmResetCloseOutIndicatorFUN008ContentPane1;
	}

	/**
	 * Return the ivjJPanel1 property value.
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
				ivjJPanel1.setLayout(null);

				ivjJPanel1.add(getJScrollPane(), null);
				ivjJPanel1.setBounds(25, 15, 292, 248);
				// defect 9943 
				Border laBorder =
					new TitledBorder(
						new EtchedBorder(),
						BORDER_TXT_SELECT_CASH_DRWRS);
				// end defect 9943 
				ivjJPanel1.setBorder(laBorder);
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
	 * Return the tblSelectedCashDrawers property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane()
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
				getJScrollPane().setViewportView(
					gettblCloseOutIndicatorDrawers());
				// user code begin {1}
				ivjJScrollPane1.setBounds(11, 25, 265, 210);
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
	 * Return the ivjtblCloseOutIndicatorDrawers property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblCloseOutIndicatorDrawers()
	{
		if (ivjtblCloseOutIndicatorDrawers == null)
		{
			try
			{
				ivjtblCloseOutIndicatorDrawers = new RTSTable();
				ivjtblCloseOutIndicatorDrawers.setName(
					"ivjtblCloseOutIndicatorDrawers");
				getJScrollPane().setColumnHeaderView(
					ivjtblCloseOutIndicatorDrawers.getTableHeader());
				getJScrollPane().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblCloseOutIndicatorDrawers.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN008());
				ivjtblCloseOutIndicatorDrawers.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblCloseOutIndicatorDrawers.setShowVerticalLines(
					false);
				ivjtblCloseOutIndicatorDrawers.setShowHorizontalLines(
					false);
				ivjtblCloseOutIndicatorDrawers
					.setAutoCreateColumnsFromModel(
					false);
				ivjtblCloseOutIndicatorDrawers.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblCloseOutIndicatorDrawers.setBounds(
					0,
					0,
					200,
					200);
				// user code begin {1}
				caCloseOutIndicatorDrawersTableModel =
					(TMFUN008) ivjtblCloseOutIndicatorDrawers
						.getModel();

				// defect 9943
				TableColumn laTableColumnA =
					ivjtblCloseOutIndicatorDrawers.getColumn(
						ivjtblCloseOutIndicatorDrawers.getColumnName(
						// 0));
						FundsConstant.FUN008_CASH_DRAWER));
				laTableColumnA.setPreferredWidth(50);
				
				TableColumn laTableColumnB =
					ivjtblCloseOutIndicatorDrawers.getColumn(
						ivjtblCloseOutIndicatorDrawers.getColumnName(
						//	1));
						FundsConstant.FUN008_REQUESTED_WORKSTATION));
				// end defect 9943 

				laTableColumnB.setPreferredWidth(75);
				ivjtblCloseOutIndicatorDrawers.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblCloseOutIndicatorDrawers.init();
				ivjtblCloseOutIndicatorDrawers.addActionListener(this);
				ivjtblCloseOutIndicatorDrawers
					.addMultipleSelectionListener(
					this);
				ivjtblCloseOutIndicatorDrawers.setBackground(
					Color.white);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblCloseOutIndicatorDrawers;
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
			setName("FrmResetCloseOutIndicatorFUN008");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(350, 375);
			setTitle(TITLE_FUN008);
			setContentPane(
				getFrmResetCloseOutIndicatorFUN008ContentPane1());
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
				caCloseOutIndicatorDrawersTableModel.add(
					caFundsData.getCashDrawers());
					
				// defect 9943 
				if (caFundsData.getCashDrawers().size() == 1)
				{
					Border laBorder =
						new TitledBorder(
							new EtchedBorder(),
							BORDER_TXT_SELECT_CASH_DRWR);
					ivjJPanel1.setBorder(laBorder);

					getchkSelectAllCashDrawers().setText(
						CHKBX_TXT_SELECT_CASH_DRAWER);
					getchkSelectAllCashDrawers().removeActionListener(
						this);
					getchkSelectAllCashDrawers().doClick();
					getchkSelectAllCashDrawers().setEnabled(false);
					gettblCloseOutIndicatorDrawers().setSelectedRow(0);
				}
				// end defect 9943 
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
	 * Set Data To Data Object
	 */
	private void setDataToDataObject()
	{
		Vector lvSelectedDrawers = new Vector();

		Vector lvSelectedRows =
			new Vector(
				gettblCloseOutIndicatorDrawers()
					.getSelectedRowNumbers());

		//Sort selectedRows incase user selected rows in order
		//different than presented
		UtilityMethods.sort(lvSelectedRows);

		//Add each selected row to vector of drawers
		for (int i = 0; i < lvSelectedRows.size(); i++)
		{
			String lsRow = lvSelectedRows.get(i).toString();
			int liRow2 = Integer.parseInt(lsRow);
			lvSelectedDrawers.add(
				(CashWorkstationIdsData) caFundsData
					.getCashDrawers()
					.get(
					liRow2));
		}

		//Set drawers vector in FundsData
		caFundsData.setSelectedCashDrawers(lvSelectedDrawers);

	}

	/**
	 * Validate Data
	 * 
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;

		//If no drawers are selected, return exception that
		//one atleast one drawer must be selected
		if (gettblCloseOutIndicatorDrawers()
			.getSelectedRowNumbers()
			.size()
			== 0)
		{
			// defect 9943 
			RTSException leRTSExInvalidSelection = new RTSException(
				//653);
				ErrorsConstant.ERR_NUM_ONE_SELECTION_MUST_BE_PICKED);
			// end defect 9943 
			leRTSExInvalidSelection.displayError(this);
			lbReturn = false;
		}

		return lbReturn;
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
			new Vector(
				ivjtblCloseOutIndicatorDrawers.getSelectedRowNumbers());
		if (lvSelectedRows.size()
			== ivjtblCloseOutIndicatorDrawers.getRowCount())
		{
			getchkSelectAllCashDrawers().setSelected(true);
		}
		else
		{
			getchkSelectAllCashDrawers().setSelected(false);
		}
	}
}
