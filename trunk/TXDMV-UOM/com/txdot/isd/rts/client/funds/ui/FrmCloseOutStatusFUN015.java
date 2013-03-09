package com.txdot.isd.rts.client.funds.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 * FrmCloseOutStatusFUN015.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							delete implements KeyListener
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	10/24/2005	Funds Constants update
 * 							defect 8379 Ver 5.2.3
 * K Harrell	12/16/2005	Use Single Interval Selection vs. Multiple
 * 							Arrow keys do not move up/down in list box.
 * 							modify gettblCashDrawerStatus(),
 * 							keyPressed() 
 * 							defect 7886 Ver 5.2.3 
 * K Harrell	06/08/2009	Implement FundsConstant, further cleanup.
 * 							delete getBuilderData()
 * 							modify gettblCashDrawerStatus() 
 * 							defect 9943 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/** 
 * Screen present list of Closeout status for each selected cash drawer. 
 * 
 * @version	Defect_POS_F	06/08/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmCloseOutStatusFUN015
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButton ivjbtnEnter = null;
	private RTSButton ivjbtnHelp = null;
	private JPanel ivjFrmCloseOutStatusFUN015ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjstcLblInstruction = null;
	private RTSTable ivjtblCashDrawerStatus = null;

	//	Objects
	private TMFUN015 caCashDrawerStatusTableModel = null;
	private FundsData caFundsData = null;

	// Constants 
	private final static String ENTER = "Enter";
	private final static String HELP = "Help";
	private final static String HIGHLIGHT_A_LINE =
		"Highlight a line and press F1 for Help";
	private final static String TITLE_FUN015 =
		"Close Out Status    FUN015";

	/**
	 * main entry - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmCloseOutStatusFUN015 laFrmCloseOutStatusFUN015;
			laFrmCloseOutStatusFUN015 = new FrmCloseOutStatusFUN015();
			laFrmCloseOutStatusFUN015.setModal(true);
			laFrmCloseOutStatusFUN015
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmCloseOutStatusFUN015.show();
			java.awt.Insets laInsets =
				laFrmCloseOutStatusFUN015.getInsets();
			laFrmCloseOutStatusFUN015.setSize(
				laFrmCloseOutStatusFUN015.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmCloseOutStatusFUN015.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmCloseOutStatusFUN015.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of util.JDialogTxDot");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmCloseOutStatusFUN015 constructor comment.
	 */
	public FrmCloseOutStatusFUN015()
	{
		super();
		initialize();
	}

	/**
	 * FrmCloseOutStatusFUN015 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmCloseOutStatusFUN015(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmCloseOutStatusFUN015 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCloseOutStatusFUN015(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
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

			if (aaAE.getSource() == getbtnEnter())
			{
				getController().processData(
					AbstractViewController.FINAL,
					getController().getData());
			}
			else if (aaAE.getSource() == getbtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.FUN015);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return the ivjbtnEnter property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnEnter()
	{
		if (ivjbtnEnter == null)
		{
			try
			{
				ivjbtnEnter = new RTSButton();
				ivjbtnEnter.setBounds(127, 215, 83, 25);
				ivjbtnEnter.setName("ivjbtnEnter");
				ivjbtnEnter.setText(ENTER);
				ivjbtnEnter.setMaximumSize(
					new java.awt.Dimension(65, 25));
				ivjbtnEnter.setActionCommand(ENTER);
				ivjbtnEnter.setMinimumSize(
					new java.awt.Dimension(65, 25));
				// user code begin {1}
				ivjbtnEnter.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnEnter;
	}

	/**
	 * Return the ivjbtnHelp property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp = new RTSButton();
				ivjbtnHelp.setBounds(360, 215, 83, 25);
				ivjbtnHelp.setName("ivjbtnHelp");
				ivjbtnHelp.setText(HELP);
				ivjbtnHelp.setMaximumSize(
					new java.awt.Dimension(59, 25));
				ivjbtnHelp.setMinimumSize(
					new java.awt.Dimension(59, 25));
				ivjbtnHelp.setActionCommand(HELP);
				// user code begin {1}
				ivjbtnHelp.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnHelp;
	}

	/**
	 * Return the ivjFrmCloseOutStatusFUN015ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmCloseOutStatusFUN015ContentPane1()
	{
		if (ivjFrmCloseOutStatusFUN015ContentPane1 == null)
		{
			try
			{
				ivjFrmCloseOutStatusFUN015ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmCloseOutStatusFUN015ContentPane1.setName(
					"ivjFrmCloseOutStatusFUN015ContentPane1");
				ivjFrmCloseOutStatusFUN015ContentPane1.setLayout(null);
				ivjFrmCloseOutStatusFUN015ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmCloseOutStatusFUN015ContentPane1.setMinimumSize(
					new java.awt.Dimension(426, 291));
				ivjFrmCloseOutStatusFUN015ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				ivjFrmCloseOutStatusFUN015ContentPane1.add(
					getbtnEnter(),
					null);
				ivjFrmCloseOutStatusFUN015ContentPane1.add(
					getbtnHelp(),
					null);
				ivjFrmCloseOutStatusFUN015ContentPane1.add(
					getstcLblInstruction(),
					null);
				ivjFrmCloseOutStatusFUN015ContentPane1.add(
					getJScrollPane1(),
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
		return ivjFrmCloseOutStatusFUN015ContentPane1;
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
				ivjJScrollPane1 = new javax.swing.JScrollPane();
				ivjJScrollPane1.setName("ivjJScrollPane1");
				ivjJScrollPane1.setBounds(19, 39, 533, 163);
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
				getJScrollPane1().setViewportView(
					gettblCashDrawerStatus());
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
	 * Return the ivjstcLblInstruction property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblInstruction()
	{
		if (ivjstcLblInstruction == null)
		{
			try
			{
				ivjstcLblInstruction = new JLabel();
				ivjstcLblInstruction.setBounds(174, 15, 218, 14);
				ivjstcLblInstruction.setName("ivjstcLblInstruction");
				ivjstcLblInstruction.setText(HIGHLIGHT_A_LINE);
				ivjstcLblInstruction.setMaximumSize(
					new java.awt.Dimension(205, 14));
				ivjstcLblInstruction.setMinimumSize(
					new java.awt.Dimension(205, 14));
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
		return ivjstcLblInstruction;
	}

	/**
	 * Return the ivjtblCashDrawerStatus property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblCashDrawerStatus()
	{
		if (ivjtblCashDrawerStatus == null)
		{
			try
			{
				ivjtblCashDrawerStatus = new RTSTable();
				ivjtblCashDrawerStatus.setName(
					"ivjtblCashDrawerStatus");
				getJScrollPane1().setColumnHeaderView(
					ivjtblCashDrawerStatus.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblCashDrawerStatus.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN015());
				ivjtblCashDrawerStatus.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblCashDrawerStatus.setShowVerticalLines(false);
				ivjtblCashDrawerStatus.setShowHorizontalLines(false);
				ivjtblCashDrawerStatus.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblCashDrawerStatus.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caCashDrawerStatusTableModel =
					(TMFUN015) ivjtblCashDrawerStatus.getModel();
					
				// defect 9943 
				TableColumn laTableColumnA =
					ivjtblCashDrawerStatus.getColumn(
					//ivjtblCashDrawerStatus.getColumnName(0));
					ivjtblCashDrawerStatus.getColumnName(
					FundsConstant.FUN015_CASH_DRAWER));
				laTableColumnA.setPreferredWidth(102);
				TableColumn laTableColumnB =
					ivjtblCashDrawerStatus.getColumn(
					//ivjtblCashDrawerStatus.getColumnName(1));
					ivjtblCashDrawerStatus.getColumnName(FundsConstant.FUN015_STATUS));
				// end defect 9943

				laTableColumnB.setPreferredWidth(415);
				ivjtblCashDrawerStatus.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblCashDrawerStatus.init();
				ivjtblCashDrawerStatus.addActionListener(this);
				ivjtblCashDrawerStatus.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblCashDrawerStatus;
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
			setName("FrmCloseOutStatusFUN015");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(575, 285);
			setTitle(TITLE_FUN015);
			setContentPane(getFrmCloseOutStatusFUN015ContentPane1());
			setDefaultFocusField(getbtnEnter());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Key Pressed 
	 * 
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_UP
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
			|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (getbtnEnter().hasFocus())
			{
				getbtnHelp().requestFocus();
			}
			else if (getbtnHelp().hasFocus())
			{
				getbtnEnter().requestFocus();
			}
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
			if (aaDataObject != null)
			{
				//Extract FundsData object out of ReportSearchData
				ReportSearchData laRptSearchData =
					(ReportSearchData) aaDataObject;
				caFundsData = (FundsData) laRptSearchData.getData();
				
				//Add vector of rows in drawers object to Table
				caCashDrawerStatusTableModel.add(
					caFundsData.getSelectedCashDrawers());
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
}
