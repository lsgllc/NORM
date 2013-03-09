package com.txdot.isd.rts.client.funds.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.FundsReportData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 * FrmCloseOutConfirmationFUN002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							Remove handling arrow keys in button panel.
 * 							modify getButtonPanel1()
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	10/24/2005	Funds Constants update
 * 							defect 8379 Ver 5.2.3
 * K Harrell	12/16/2005	Focus should not go to list box
 * 							modify gettblSelectedDrawers() 
 * 							defect 7886 Ver 5.2.3
 * K Harrell	03/17/2006	Remove redundant check for !CheckIsServer()
 * 							after CheckIsServer()
 * 							delete getBuilderData()
 * 							modify actionPerformed() 
 * 							defect 8623 Ver 5.2.3  
 * K Harrell	06/08/2009	Implement FundsConstant.  Additional class
 * 							cleanup. 
 * 							add CASH_DRWRS_LISTED, SELECTED_CASH_DRWR
 * 							add caSelectedDrawerTableModel
 * 							add ivjstcLblLine1, ivjstcLblLine2, get
 * 							   methods 
 * 							add setDataToDataObject()
 * 							delete selectedDrawerTableModel, 
 * 								ivjstcLblSentence1,ivjstcLblSentence2, 
 * 								get methods  
 * 							modify IF_YOU_PRESS_ENTER, CASH_DRWR_LISTED,
 * 							 SPLIT_BY_EMP, SELECTED_CASH_DRWRS, 
 * 							 SELECTED_CASH_DRWR
 * 							modify setData(), actionPerformed(),
 * 							 gettblSelectedDrawers() 
 * 							defect 9943 Ver Defect_POS_F
 * K Harrell	08/16/2009	Implement SystemProperty.isClientServer()
 * 							modify actionPerformed() 
 * 							defect 8628 Ver Defect_POS_F  
 * ---------------------------------------------------------------------
 */

/** 
 * Screen presents list of all selected cash drawers from FUN001, and 
 * prompts user to confirm close out transaction. If not working on
 * database server, FUN001 and FUN014 may be skipped, and this screen
 * will present the individual cash workstation for confirmation.
 * 
 * @version	Defect_POS_F 	08/16/2009
 * @author	Bobby Tulsia
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmCloseOutConfirmationFUN002
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSplitReportByEmployee = null;
	private JPanel ivjFrmCloseOutConfirmationFUN002ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	
	// defect 9943 
	private JLabel ivjstcLblLine1 = null;
	private JLabel ivjstcLblLine2 = null;
	// end defect 9943 
	
	private JScrollPane ivjJScrollPane1 = null;
	private RTSTable ivjtblSelectedDrawers = null;

	private FundsData caFundsData = null;
	private FundsReportData caFundsReportData = null;

	// defect 9943
	private TMFUN002 caSelectedDrawerTableModel = null;

	private final static String IF_YOU_PRESS_ENTER =
		"IF YOU PRESS ENTER, YOU WILL CLOSE OUT THE";

	private final static String CASH_DRWR_LISTED =
		"CASH DRAWER LISTED ON THIS SCREEN.";

	private final static String SELECTED_CASH_DRWR =
		"Selected Cash Drawer:";
		
	private final static String SPLIT_BY_EMP =
		"Split Report by Employee";
		
	private final static String SELECTED_CASH_DRWRS =
		"Selected Cash Drawers:";
	// end defect 9943 

	private final static String CASH_DRWRS_LISTED =
		"CASH DRAWERS LISTED ON THIS SCREEN.";

	private final static String TITLE_FUN002 =
		"Close Out Confirmation    FUN002";

	/**
	 * main entrypoint - starts the part when run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmCloseOutConfirmationFUN002 laFrmCloseOutConfirmationFUN002;
			laFrmCloseOutConfirmationFUN002 =
				new FrmCloseOutConfirmationFUN002();
			laFrmCloseOutConfirmationFUN002.setModal(true);
			laFrmCloseOutConfirmationFUN002
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmCloseOutConfirmationFUN002.show();
			java.awt.Insets laInsets =
				laFrmCloseOutConfirmationFUN002.getInsets();
			laFrmCloseOutConfirmationFUN002.setSize(
				laFrmCloseOutConfirmationFUN002.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmCloseOutConfirmationFUN002.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmCloseOutConfirmationFUN002.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of util.JDialogTxDot");
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 *  FrmCloseOutConfirmationFUN002 constructor comment.
	 */
	public FrmCloseOutConfirmationFUN002()
	{
		super();
		initialize();
	}

	/**
	 * FrmCloseOutConfirmationFUN002 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmCloseOutConfirmationFUN002(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmCloseOutConfirmationFUN002 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmCloseOutConfirmationFUN002(JFrame aaParent)
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
				// defect 9943
				setDataToDataObject();
				// end defect 9943 

				getController().processData(
					AbstractViewController.ENTER,
					caFundsData);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				// SERVER: Cancel screen; go back to FUN001
				// defect 8628 
				if (SystemProperty.isClientServer())
				{
				// end defect 8628  
				
					getController().processData(
						AbstractViewController.CANCEL,
						caFundsData);
				}
				// WORKSTATION: Final screen; Return to Desktop
				// defect 8623 		
				// else if (!FundsClientBusiness.checkIsServer())
				// end defect 8623 
				else
				{
					getController().processData(
						AbstractViewController.FINAL,
						caFundsData);
				}
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.FUN002);
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
				ivjButtonPanel1.setBounds(23, 247, 379, 44);
				ivjButtonPanel1.setName("ivjButtonPanel1");
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
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
	 * Return the ivjchkSplitReportByEmployee property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSplitReportByEmployee()
	{
		if (ivjchkSplitReportByEmployee == null)
		{
			try
			{
				ivjchkSplitReportByEmployee = new JCheckBox();
				ivjchkSplitReportByEmployee.setBounds(118, 166, 189, 20);
				ivjchkSplitReportByEmployee.setName(
					"ivjchkSplitReportByEmployee");
				ivjchkSplitReportByEmployee.setMnemonic(83);
				ivjchkSplitReportByEmployee.setText(SPLIT_BY_EMP);
				ivjchkSplitReportByEmployee.setMaximumSize(
					new java.awt.Dimension(162, 22));
				ivjchkSplitReportByEmployee.setActionCommand(
					SPLIT_BY_EMP);
				ivjchkSplitReportByEmployee.setMinimumSize(
					new java.awt.Dimension(162, 22));
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
		return ivjchkSplitReportByEmployee;
	}

	/**
	 * Return the ivjFrmCloseOutConfirmationFUN002ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmCloseOutConfirmationFUN002ContentPane1()
	{
		if (ivjFrmCloseOutConfirmationFUN002ContentPane1 == null)
		{
			try
			{
				ivjFrmCloseOutConfirmationFUN002ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmCloseOutConfirmationFUN002ContentPane1.setName(
					"ivjFrmCloseOutConfirmationFUN002ContentPane1");
				ivjFrmCloseOutConfirmationFUN002ContentPane1.setLayout(null);
				ivjFrmCloseOutConfirmationFUN002ContentPane1.add(getJPanel2(), null);
				ivjFrmCloseOutConfirmationFUN002ContentPane1.add(getchkSplitReportByEmployee(), null);
				ivjFrmCloseOutConfirmationFUN002ContentPane1.add(getButtonPanel1(), null);
				ivjFrmCloseOutConfirmationFUN002ContentPane1.add(getJPanel1(), null);
				ivjFrmCloseOutConfirmationFUN002ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmCloseOutConfirmationFUN002ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(392, 418));
				ivjFrmCloseOutConfirmationFUN002ContentPane1.setBounds(
					0,
					0,
					0,
					0);
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
		return ivjFrmCloseOutConfirmationFUN002ContentPane1;
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
				ivjJPanel1.setLayout(null);
				getJPanel1().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				// user code begin {1}
				ivjJPanel1.setBounds(26, 13, 373, 148);
				Border laBorder =
					new TitledBorder(
						new EtchedBorder(),
						SELECTED_CASH_DRWRS);
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
	 * Return the ivjJPanel2 property value.
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
					new java.awt.Dimension(307, 74));
				java.awt.GridBagConstraints constraintsstcLblSentence1 =
					new java.awt.GridBagConstraints();
				constraintsstcLblSentence1.gridx = 1;
				constraintsstcLblSentence1.gridy = 1;
				constraintsstcLblSentence1.ipadx = 13;
				constraintsstcLblSentence1.insets =
					new java.awt.Insets(24, 43, 0, 62);
				getJPanel2().add(
					getstcLblLine1(),
					constraintsstcLblSentence1);

				java.awt.GridBagConstraints constraintsstcLblSentence2 =
					new java.awt.GridBagConstraints();
				constraintsstcLblSentence2.gridx = 1;
				constraintsstcLblSentence2.gridy = 2;
				constraintsstcLblSentence2.ipadx = 3;
				constraintsstcLblSentence2.insets =
					new java.awt.Insets(1, 43, 24, 98);
				getJPanel2().add(
					getstcLblLine2(),
					constraintsstcLblSentence2);
				// user code begin {1}
				ivjJPanel2.setBounds(21, 194, 382, 44);
				// user code end
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
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setBounds(27, 22, 319, 117);
				getJScrollPane1().setViewportView(
					gettblSelectedDrawers());
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
	 * Return the ivjstcLblLine1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLine1()
	{
		if (ivjstcLblLine1 == null)
		{
			try
			{
				ivjstcLblLine1 = new JLabel();
				ivjstcLblLine1.setName("ivjstcLblLine1");
				ivjstcLblLine1.setText(IF_YOU_PRESS_ENTER);
				ivjstcLblLine1.setMaximumSize(
					new java.awt.Dimension(264, 14));
				ivjstcLblLine1.setMinimumSize(
					new java.awt.Dimension(264, 14));
				ivjstcLblLine1.setForeground(
					new java.awt.Color(255, 0, 0));
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
		return ivjstcLblLine1;
	}

	/**
	 * Return the ivjstcLblLine2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblLine2()
	{
		if (ivjstcLblLine2 == null)
		{
			try
			{
				ivjstcLblLine2 = new JLabel();
				ivjstcLblLine2.setName("ivjstcLblLine2");
				ivjstcLblLine2.setText(CASH_DRWRS_LISTED);
				ivjstcLblLine2.setMaximumSize(
					new java.awt.Dimension(238, 14));
				ivjstcLblLine2.setMinimumSize(
					new java.awt.Dimension(238, 14));
				ivjstcLblLine2.setForeground(
					new java.awt.Color(255, 0, 0));
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
		return ivjstcLblLine2;
	}

	/**
	 * Return the ivjtblSelectedDrawers property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblSelectedDrawers()
	{
		if (ivjtblSelectedDrawers == null)
		{
			try
			{
				ivjtblSelectedDrawers = new RTSTable();
				ivjtblSelectedDrawers.setName("ivjtblSelectedDrawers");
				getJScrollPane1().setColumnHeaderView(
					ivjtblSelectedDrawers.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblSelectedDrawers.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN002());
				ivjtblSelectedDrawers.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblSelectedDrawers.setBounds(0, 0, 200, 200);
				ivjtblSelectedDrawers.setShowVerticalLines(false);
				ivjtblSelectedDrawers.setShowHorizontalLines(false);
				// user code begin {1}
				// defect 9943 
				caSelectedDrawerTableModel =
					(TMFUN002) ivjtblSelectedDrawers.getModel();

				TableColumn laTableColumnA =
					ivjtblSelectedDrawers.getColumn(
						ivjtblSelectedDrawers.getColumnName(
							FundsConstant.FUN002_ID));
				//ivjtblSelectedDrawers.getColumnName(0));
				laTableColumnA.setPreferredWidth(50);
				TableColumn laTableColumnB =
					ivjtblSelectedDrawers.getColumn(
						ivjtblSelectedDrawers.getColumnName(
							FundsConstant.FUN002_LAST_CLOSEOUT));
				//ivjtblSelectedDrawers.getColumnName(1));
				// end defect 9943 
				laTableColumnB.setPreferredWidth(115);
				ivjtblSelectedDrawers.setRowSelectionAllowed(false);
				ivjtblSelectedDrawers.setColumnSelectionAllowed(false);
				ivjtblSelectedDrawers.init();
				ivjtblSelectedDrawers.addActionListener(this);
				ivjtblSelectedDrawers.setBackground(Color.white);
				ivjtblSelectedDrawers.setEnabled(false);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblSelectedDrawers;
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
			setName("FrmCloseOutConfirmationFUN002");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(425, 322);
			setTitle(TITLE_FUN002);
			setContentPane(
				getFrmCloseOutConfirmationFUN002ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Selected rows from the table in FUN001 are passed in the dataObject.
	 * This data, is then posted to the table.  The displayedFUN002  
	 * indicator is set to indicate backwards navigation.
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
				//Initalize FUN002 displayed for navigation purposes
				caFundsData.setDisplayedFUN002(true);
				//Add selected drawers to table	
				caSelectedDrawerTableModel.add(
					caFundsData.getSelectedCashDrawers());

				// defect 9943
				if (caFundsData.getSelectedCashDrawers().size() == 1)
				{
					Border laBorder =
						new TitledBorder(
							new EtchedBorder(),
							SELECTED_CASH_DRWR);
					ivjJPanel1.setBorder(laBorder);
					ivjstcLblLine2.setText(CASH_DRWR_LISTED);
				}
				// end defect 9943 
			}
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException laRTSEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			laRTSEx.displayError(this);
			laRTSEx = null;
		}
	}

	/**
	 * Set Data To Data Object
	 */
	private void setDataToDataObject()
	{
		//Create new FundsReportData object to generate Payment
		// Report - Closeout
		caFundsReportData = new FundsReportData();
		//Set report name
		Vector lvReportNames = new Vector();
		lvReportNames.add(FundsConstant.PAYMENT_REPORT);
		caFundsReportData.setReportNames(lvReportNames);
		//Set entity as cash drawer
		caFundsReportData.setEntity(FundsConstant.CASH_DRAWER);

		//Set primary split if user selected checkbox
		if (getchkSplitReportByEmployee().isSelected())
		{
			caFundsReportData.setPrimarySplit(FundsConstant.EMPLOYEE);
		}
		//Set Range to Closeout, which indicates it is different
		//than the other types of ranges
		caFundsReportData.setRange(FundsConstant.CLOSE_OUT_FOR_DAY);

		//Set Flag to display report as false
		caFundsReportData.setDisplayReports(false);

		//Set FundsReportData object in Funds Data object which
		//holds selected cashdrawers   		
		caFundsData.setFundsReportData(caFundsReportData);
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
