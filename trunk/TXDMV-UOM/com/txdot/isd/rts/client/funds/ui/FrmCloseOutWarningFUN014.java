package com.txdot.isd.rts.client.funds.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.CashWorkstationCloseOutData;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 * FrmCloseOutWarningFUN014.java
 *
 * (c) Texas Department of Transportation 2001
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
 * K Harrell	11/02/2005	Funds Constant Review
 * 							defect 8379 Ver 5.2.3
 * B Hargrove	12/06/2005	Commented out this whole method to improve
 * 							tabbing. 
 * 							modify keyPressed()  
 * 							defect 7886 Ver 5.2.3 
 * Jeff S.		12/12/2005	Set the table enabled value to false so that
 * 							you would not tab through the table.  You 
 * 							can not select anything anyway.
 * 							modify gettblDrawersClosedToday()
 * 							defect 7886 Ver 5.2.3
 * K Harrell	12/16/2005	Restored keyPressed() for arrow keys
 * 							defect 7886 Ver 5.2.3
 * K Harrell	03/17/2006	Remove redundant check for !isServer()
 * 							after isServer()
 * 							delete getBuilderData()
 * 							modify actionPerformed() 
 * 							defect 8623 Ver 5.2.3   
 * K Harrell	06/08/2009	Implement FundsConstant, additional cleanup.
 * 							add ivjstcLblWarningLine1, ivjstcLblWarningLine2,
 * 							 ivjstcLblWarningLine3, get methods
 * 							add DRWR_ALREADY_CLOSED,SELECTED_CASH_DRWR 
 * 							delete ivjstcLblsentence1,ivjstcLblsentence2,
 * 							 ivjstcLblsentence3, get methods
 * 							modify gettblDrawersClosedToday(), setData(),
 * 							 initialize()
 * 							defect 9943 Ver Defect_POS_F   
 * K Harrell	08/16/2009	Implement SystemProperty.isClientServer()
 * 							modify actionPerformed() 
 * 							defect 8628 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/** 
 * Screen present list of all cash drawers already closed out on
 * the current date, and prompts user to continue.
 * 
 * @version	Defect_POS_F	08/16/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */
public class FrmCloseOutWarningFUN014
	extends RTSDialogBox
	implements ActionListener
{
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private RTSButton ivjbtnNo = null;
	private RTSButton ivjbtnYes = null;
	
	// defect 9943
	private JLabel ivjstcLblWarningLine1 = null;
	private JLabel ivjstcLblWarningLine2 = null;
	private JLabel ivjstcLblWarningLine3 = null;
	// end defect 9943 
	
	private JPanel ivjFrmCloseOutWarningFUN014ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblDrawersClosedToday = null;
	private TMFUN014 caDrawersClosedTodayTableModel;

	// Object 
	private FundsData caFundsData;

	// Constants 
	// defect 9943
	private final static String DRWR_ALREADY_CLOSED =
		"THIS CASH DRAWER HAS ALREADY BEEN CLOSED";
	private final static String SELECTED_CASH_DRWR =
		"Selected Cash Drawer:";
	// end defect 9943 

	private final static String DRWRS_ALREADY_CLOSED =
		"THESE CASH DRAWERS HAVE ALREADY BEEN CLOSED";
	private final static String DYWT_CONTINUE =
		"DO YOU WANT TO CONTINUE?";
	private final static String NO = "No";
	private final static String OUT_TODAY = "OUT TODAY.";
	private final static String SELECTED_CASH_DRWRS =
		"Selected Cash Drawers:";
	private final static String TITLE_FUN014 =
		"Close Out Warning     FUN014";
	private final static String YES = "Yes";
	private final static String EXCEPT_IN_MAIN =
		"Exception occurred in main() of FrmCloseOutWarningFUN014";

	/**
	 * FrmCloseOutWarningFUN014 constructor comment.
	 */
	public FrmCloseOutWarningFUN014()
	{
		super();
		initialize();
	}

	/**
	 * FrmCloseOutWarningFUN014 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmCloseOutWarningFUN014(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmCloseOutWarningFUN014 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCloseOutWarningFUN014(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 *  Invoked when user performs an action.  The user selects may
	 *  press "Yes" button to continue with the close out operation or
	 *  "No" to return to FUN001.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
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

			if (aaAE.getSource() == getbtnYes())
			{
				getController().processData(
					VCCloseOutWarningFUN014.YES,
					caFundsData);
			}
			else if (aaAE.getSource() == getbtnNo())
			{
				// defect 8628 
				// SERVER: Cancel screen; Return to FUN001	
				if (SystemProperty.isClientServer())
				{
					// end defec 10023 
					getController().processData(
						VCCloseOutWarningFUN014.NO,
						caFundsData);
				}
				// WORKSTATION: Final screen; Return to Desktop
				// defect 8623 
				// else if (!FundsClientBusiness.checkIsServer())
				// end defect 8623 
				else
				{
					getController().processData(
						VCCloseOutWarningFUN014.CLOSE_SCREEN,
						caFundsData);
				}
			}
		}
		finally
		{
			doneWorking();
		}
	}
	
	/**
	 * Return the ivjbtnNo property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnNo()
	{
		if (ivjbtnNo == null)
		{
			try
			{
				ivjbtnNo = new RTSButton();
				ivjbtnNo.setBounds(255, 247, 76, 25);
				ivjbtnNo.setName("ivjbtnNo");
				ivjbtnNo.setText(NO);
				ivjbtnNo.setMaximumSize(new java.awt.Dimension(49, 25));
				ivjbtnNo.setMinimumSize(new java.awt.Dimension(49, 25));
				ivjbtnNo.setActionCommand(NO);
				// user code begin {1}
				// defect 9943
				//ivjbtnNo.setMnemonic(78);
				ivjbtnNo.setMnemonic(KeyEvent.VK_N);
				// end defect 9943 
				ivjbtnNo.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjbtnNo;
	}
	
	/**
	 * Return the ivjbtnYes property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnYes()
	{
		if (ivjbtnYes == null)
		{
			try
			{
				ivjbtnYes = new RTSButton();
				ivjbtnYes.setBounds(86, 247, 85, 25);
				ivjbtnYes.setName("ivjbtnYes");
				ivjbtnYes.setText(YES);
				ivjbtnYes.setMaximumSize(
					new java.awt.Dimension(55, 25));
				ivjbtnYes.setActionCommand(YES);
				ivjbtnYes.setMinimumSize(
					new java.awt.Dimension(55, 25));
				// user code begin {1}
				ivjbtnYes.addActionListener(this);
				// defect 9943
				//ivjbtnYes.setMnemonic(89);
				ivjbtnYes.setMnemonic(KeyEvent.VK_Y);
				// end defect 9943 
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjbtnYes;
	}

	/**
	 * Return the FrmCloseOutWarningFUN014ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax
		.swing
		.JPanel getFrmCloseOutWarningFUN014ContentPane1()
	{
		if (ivjFrmCloseOutWarningFUN014ContentPane1 == null)
		{
			try
			{
				ivjFrmCloseOutWarningFUN014ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmCloseOutWarningFUN014ContentPane1.setName(
					"ivjFrmCloseOutWarningFUN014ContentPane1");
				ivjFrmCloseOutWarningFUN014ContentPane1.setLayout(null);
				ivjFrmCloseOutWarningFUN014ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmCloseOutWarningFUN014ContentPane1.setMinimumSize(
					new java.awt.Dimension(438, 338));
				ivjFrmCloseOutWarningFUN014ContentPane1.setBounds(
					0,
					0,
					0,
					0);

				ivjFrmCloseOutWarningFUN014ContentPane1.add(
					getJPanel2(),
					null);
				ivjFrmCloseOutWarningFUN014ContentPane1.add(
					getbtnYes(),
					null);
				ivjFrmCloseOutWarningFUN014ContentPane1.add(
					getbtnNo(),
					null);
				ivjFrmCloseOutWarningFUN014ContentPane1.add(
					getJPanel1(),
					null);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjFrmCloseOutWarningFUN014ContentPane1;
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
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setBounds(20, 5, 378, 130);
				ivjJPanel1.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						SELECTED_CASH_DRWRS));
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 316;
				constraintsJScrollPane1.ipady = 111;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(9, 24, 9, 24);
				getJPanel1().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the JPanel2 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel2()
	{
		if (ivjJPanel2 == null)
		{
			try
			{
				javax.swing.border.TitledBorder ivjLocalBorder;
				ivjLocalBorder =
					new javax.swing.border.TitledBorder(new String());
				ivjLocalBorder.setTitlePosition(2);
				ivjLocalBorder.setBorder(
					new javax.swing.border.EtchedBorder());
				ivjLocalBorder.setTitleJustification(4);
				ivjLocalBorder.setTitle("");
				ivjJPanel2 = new javax.swing.JPanel();
				ivjJPanel2.setName("ivjJPanel2");
				ivjJPanel2.setBorder(ivjLocalBorder);
				ivjJPanel2.setLayout(new java.awt.GridBagLayout());
				ivjJPanel2.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjJPanel2.setMinimumSize(
					new java.awt.Dimension(406, 98));

				java.awt.GridBagConstraints constraintsstcLblsentence1 =
					new java.awt.GridBagConstraints();
				constraintsstcLblsentence1.gridx = 1;
				constraintsstcLblsentence1.gridy = 1;
				constraintsstcLblsentence1.ipadx = 5;
				constraintsstcLblsentence1.ipady = -2;
				constraintsstcLblsentence1.insets =
					new java.awt.Insets(20, 7, 2, 3);
				getJPanel2().add(
					getstcLblWarningLine1(),
					constraintsstcLblsentence1);

				java.awt.GridBagConstraints constraintsstcLblSentence2 =
					new java.awt.GridBagConstraints();
				constraintsstcLblSentence2.gridx = 1;
				constraintsstcLblSentence2.gridy = 2;
				constraintsstcLblSentence2.ipadx = 89;
				constraintsstcLblSentence2.ipady = -2;
				constraintsstcLblSentence2.insets =
					new java.awt.Insets(2, 7, 5, 212);
				getJPanel2().add(
					getstcLblWarningLine2(),
					constraintsstcLblSentence2);

				java.awt.GridBagConstraints constraintsstcLblSentence3 =
					new java.awt.GridBagConstraints();
				constraintsstcLblSentence3.gridx = 1;
				constraintsstcLblSentence3.gridy = 3;
				constraintsstcLblSentence3.ipadx = 104;
				constraintsstcLblSentence3.ipady = -2;
				constraintsstcLblSentence3.insets =
					new java.awt.Insets(6, 7, 18, 70);
				getJPanel2().add(
					getstcLblWarningLine3(),
					constraintsstcLblSentence3);
				// user code begin {1}
				ivjJPanel2.setBounds(18, 150, 381, 82);
				Border b = new TitledBorder(new EtchedBorder(), "");
				ivjJPanel2.setBorder(b);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJPanel2;
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
					gettblDrawersClosedToday());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the ivjstcLblWarningLine1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblWarningLine1()
	{
		if (ivjstcLblWarningLine1 == null)
		{
			try
			{
				ivjstcLblWarningLine1 = new JLabel();
				ivjstcLblWarningLine1.setName("ivjstcLblWarningLine1");
				ivjstcLblWarningLine1.setText(DRWRS_ALREADY_CLOSED);
				ivjstcLblWarningLine1.setMaximumSize(
					new java.awt.Dimension(374, 16));
				ivjstcLblWarningLine1.setForeground(
					new java.awt.Color(255, 0, 0));
				ivjstcLblWarningLine1.setFont(
					new java.awt.Font("Arial", 1, 14));
				ivjstcLblWarningLine1.setMinimumSize(
					new java.awt.Dimension(374, 16));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblWarningLine1;
	}
	
	/**
	 * Return the ivjstcLblWarningLine2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblWarningLine2()
	{
		if (ivjstcLblWarningLine2 == null)
		{
			try
			{
				ivjstcLblWarningLine2 = new javax.swing.JLabel();
				ivjstcLblWarningLine2.setName("ivjstcLblWarningLine2");
				ivjstcLblWarningLine2.setText(OUT_TODAY);
				ivjstcLblWarningLine2.setMaximumSize(
					new java.awt.Dimension(81, 16));
				ivjstcLblWarningLine2.setForeground(
					new java.awt.Color(255, 0, 0));
				ivjstcLblWarningLine2.setFont(
					new java.awt.Font("Arial", 1, 14));
				ivjstcLblWarningLine2.setMinimumSize(
					new java.awt.Dimension(81, 16));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblWarningLine2;
	}

	/**
	 * Return the ivjstcLblWarningLine3 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblWarningLine3()
	{
		if (ivjstcLblWarningLine3 == null)
		{
			try
			{
				ivjstcLblWarningLine3 = new javax.swing.JLabel();
				ivjstcLblWarningLine3.setName("ivjstcLblWarningLine3");
				ivjstcLblWarningLine3.setText(DYWT_CONTINUE);
				ivjstcLblWarningLine3.setMaximumSize(
					new java.awt.Dimension(208, 16));
				ivjstcLblWarningLine3.setForeground(
					new java.awt.Color(255, 0, 0));
				ivjstcLblWarningLine3.setFont(
					new java.awt.Font("Arial", 1, 14));
				ivjstcLblWarningLine3.setMinimumSize(
					new java.awt.Dimension(208, 16));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblWarningLine3;
	}
	
	/**
	 * Return the ivjtblDrawersClosedToday property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblDrawersClosedToday()
	{
		if (ivjtblDrawersClosedToday == null)
		{
			try
			{
				ivjtblDrawersClosedToday =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjtblDrawersClosedToday.setName(
					"ivjtblDrawersClosedToday");
				getJScrollPane1().setColumnHeaderView(
					ivjtblDrawersClosedToday.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblDrawersClosedToday.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblDrawersClosedToday.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN014());
				ivjtblDrawersClosedToday.setShowVerticalLines(false);
				ivjtblDrawersClosedToday.setShowHorizontalLines(false);
				ivjtblDrawersClosedToday.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblDrawersClosedToday.setBounds(0, 0, 322, 64);
				// user code begin {1}
				caDrawersClosedTodayTableModel =
					(TMFUN014) ivjtblDrawersClosedToday.getModel();

				// defect 9943  
				TableColumn laTableColumnA =
					ivjtblDrawersClosedToday.getColumn(
					//ivjtblDrawersClosedToday.getColumnName(0));
					ivjtblDrawersClosedToday.getColumnName(
					FundsConstant.FUN014_ID));
				laTableColumnA.setPreferredWidth(50);

				TableColumn laTableColumnB =
					ivjtblDrawersClosedToday.getColumn(
					//	ivjtblDrawersClosedToday.getColumnName(1));
					ivjtblDrawersClosedToday.getColumnName(
					FundsConstant.FUN014_LAST_CLOSEOUT_REQ_TIME));
				// end defect 9943

				laTableColumnB.setPreferredWidth(150);
				ivjtblDrawersClosedToday.setRowSelectionAllowed(false);
				ivjtblDrawersClosedToday.init();
				ivjtblDrawersClosedToday.addActionListener(this);
				ivjtblDrawersClosedToday.setBackground(Color.white);
				ivjtblDrawersClosedToday.setEnabled(false);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjtblDrawersClosedToday;
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
			setName("FrmCloseOutWarningFUN014");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(425, 317);
			setTitle(TITLE_FUN014);
			setContentPane(getFrmCloseOutWarningFUN014ContentPane1());
			// defect 9943
			setDefaultFocusField(getbtnYes());
			// end defect 9943 
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		cancelButton = getbtnNo();
		// user code end
	}

	/**
	 * Key Pressed
	 * 
	 * @param aeKE KeyEvent
	*/
	public void keyPressed(KeyEvent aaKE)
	{
		if (aaKE.getKeyCode() == KeyEvent.VK_UP
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT
			|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT
			|| aaKE.getKeyCode() == KeyEvent.VK_DOWN)
		{
			if (getbtnYes().hasFocus())
			{
				getbtnNo().requestFocus();
			}
			else if (getbtnNo().hasFocus())
			{
				getbtnYes().requestFocus();
			}
		}
	}

	/**
	 * main entrypoint - starts the part when run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmCloseOutWarningFUN014 laFrmCloseOutWarningFUN014;
			laFrmCloseOutWarningFUN014 = new FrmCloseOutWarningFUN014();
			laFrmCloseOutWarningFUN014.setModal(true);
			laFrmCloseOutWarningFUN014
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmCloseOutWarningFUN014.show();
			java.awt.Insets laInsets =
				laFrmCloseOutWarningFUN014.getInsets();
			laFrmCloseOutWarningFUN014.setSize(
				laFrmCloseOutWarningFUN014.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmCloseOutWarningFUN014.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmCloseOutWarningFUN014.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(EXCEPT_IN_MAIN);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * All subclasses must implement this method. It sets the data on
	 * the screen and is how the controller relays info to the view.
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

				//If FUN001 not displayed, set selected drawers to
				//single drawer
				if (caFundsData.getSelectedCashDrawers() == null)
				{
					caFundsData.setSelectedCashDrawers(
						caFundsData.getCashDrawers());
				}
				// For each cash drawer, determine if closed out today
				Vector lvClosedOutRows = new Vector();
				int liNumCashDrawers =
					caFundsData.getSelectedCashDrawers().size();

				for (int i = 0; i < liNumCashDrawers; i++)
				{
					//create an object to store each selected row
					CashWorkstationCloseOutData laRow =
						(CashWorkstationCloseOutData) caFundsData
							.getSelectedCashDrawers()
							.get(
							i);
					RTSDate laCloseOutDate =
						laRow.getCloseOutEndTstmp();

					if (laCloseOutDate.getDate()
						== RTSDate.getCurrentDate().getDate()
						&& laCloseOutDate.getMonth()
							== RTSDate.getCurrentDate().getMonth()
						&& laCloseOutDate.getYear()
							== RTSDate.getCurrentDate().getYear())
					{
						//add row if closed on current date
						lvClosedOutRows.add(laRow);
					}
				}
				// defect 9943 
				// Adjust text to identify single cashdrawer
				if (lvClosedOutRows.size() == 1)
				{
					getstcLblWarningLine1().setText(
						DRWR_ALREADY_CLOSED);
					getJPanel1().setBorder(
						new TitledBorder(
							new EtchedBorder(),
							SELECTED_CASH_DRWR));
				}
				// end defect 9943 
				caDrawersClosedTodayTableModel.add(lvClosedOutRows);
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
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"