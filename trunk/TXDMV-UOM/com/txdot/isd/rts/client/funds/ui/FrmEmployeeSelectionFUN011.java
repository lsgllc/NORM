package com.txdot.isd.rts.client.funds.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.EmployeeData;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.FundsReportData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 * FrmEmployeeSelectionFUN011.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/21/2002	Unselecting "Select All" when mouse clicked
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
 * K Harrell	05/28/2008	Display Error if HQ selects more than one 
 * 							Employee. 
 * 							delete getBuilderData()
 * 							modify actionPerformed(), setData(),
 * 							 valueChanged()
 * 							defect 9653 Ver Defect POS A
 * K Harrell	06/08/2009	Additional class cleanup	
 * 							add ivjJScrollPaneEmployeeSelection(), 
 * 								get method
 *							add CHKBX_TXT_SELECT_EMP, 
 *							 CHKBX_TXT_SELECT_EMPS
 *							delete ivjtblEmployeeSelection, get method
 * 							modify actionPerformed(),gettblEmployees(), 
 * 							  getchkSelectAllEmployees()
 *							defect 9943 Ver Defect_POS_F
 * ---------------------------------------------------------------------
 */

/** 
 * Screen presents list of all employees with transactions, and prompts 
 * user to select those to report on.
 * 
 * @version	Defect_POS_F	06/08/2009 
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmEmployeeSelectionFUN011
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSelectAllEmployees = null;
	private JPanel ivjFrmEmployeeSelectionFUN011ContentPane1 = null;
	private RTSTable ivjtblEmployees = null;

	private TMFUN011 caEmployeesTableModel;
	private FundsData caFundsData;

	// defect 9943
	private JScrollPane ivjJScrollPaneEmployeeSelection = null;

	private final static String CHKBX_TXT_SELECT_ALL_EMPS =
		"Select All Employees";

	private final static String CHKBX_TXT_SELECT_EMP =
		"Select Employee";
	// end defect 9943  

	private final static String TITLE_FUN011 =
		"Employee Selection     FUN011";

	/**
	 * FrmEmployeeSelectionFUN011 constructor comment.
	 */
	public FrmEmployeeSelectionFUN011()
	{
		super();
		initialize();
	}

	/**
	 * FrmEmployeeSelectionFUN011 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmEmployeeSelectionFUN011(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmEmployeeSelectionFUN011 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmEmployeeSelectionFUN011(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
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

			// Enter || Table  
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter()
				|| aaAE.getSource() == ivjtblEmployees)
			{
				//Create vectors to store selected rows from tbl employees	
				Vector lvSelectedRows =
					new Vector(ivjtblEmployees.getSelectedRowNumbers());
				Vector lvEmployees = new Vector();

				//Sort the order of the selected rows in case user
				//selected rows out of order
				UtilityMethods.sort(lvSelectedRows);

				// defect 9653 
				// HQ can only select one row 
				if (SystemProperty.isHQ() && lvSelectedRows.size() > 1)
				{
					// defect 9943 
					//RTSException leRTSEx = new RTSException(652);
					RTSException leRTSEx =
						new RTSException(
							ErrorsConstant
								.ERR_NUM_ONLY_ONE_SELECTION_CAN_BE_MADE);
					// end defect 9943 
					leRTSEx.displayError(this);
					return;
				}
				// end defect 9653 

				//Add each row from the total list of Employees to the
				//employees vector
				for (int i = 0; i < lvSelectedRows.size(); i++)
				{
					// defect 9943 
					String lsRow = lvSelectedRows.get(i).toString();
					int liRow = Integer.parseInt(lsRow);
					EmployeeData laEmployee = new EmployeeData();
					
					laEmployee
						.setEmployeeId(
							(String) ivjtblEmployees
							.getModel()
							.getValueAt(liRow,
							//0));
							FundsConstant.FUN011_EMPLOYEE_ID));
							
					laEmployee
						.setLastName(
							(String) ivjtblEmployees
							.getModel()
							.getValueAt(liRow,
							//1));
							FundsConstant.FUN011_LAST_NAME));

					laEmployee
						.setFirstName(
							(String) ivjtblEmployees
							.getModel()
							.getValueAt(liRow,
							//2));
					FundsConstant.FUN011_FIRST_NAME));
					// end defect 9943 
					
					lvEmployees.add(laEmployee);
				}

				// If no employees are selected, throw exception that
				// at least one must be selected
				if (lvEmployees.size() == 0)
				{
					// defect 9943 
					RTSException leInvalidSelection =
						//new RTSException(653);
					new RTSException(
					ErrorsConstant.ERR_NUM_ONE_SELECTION_MUST_BE_PICKED);
					// end defect 9943 
					leInvalidSelection.displayError(this);
					return;
				}

				//If drawers are selected, store in the FundsData object
				caFundsData.setSelectedEmployees(lvEmployees);
				
				// Intitialze that FUN002 has not been displayed 
				caFundsData.setDisplayedFUN002(false);

				//If all employees are selected, set indicator to true
				//so SQL will select all drawers at once instead of
				//selecting each drawer at a time.
				//Should evaluate if we really need this indicator?
				
				caFundsData.setAllEmployees(
					getchkSelectAllEmployees().isSelected());
					
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
				RTSHelp.displayHelp(RTSHelp.FUN011);
			}
			else if (aaAE.getSource() == getchkSelectAllEmployees())
			{
				if (getchkSelectAllEmployees().isSelected())
				{
					gettblEmployees().selectAllRows(
						gettblEmployees().getRowCount());
				}
				else
				{
					gettblEmployees().unselectAllRows();
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
				ivjButtonPanel1.setBounds(19, 247, 403, 49);
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
	 * Return the ivjchkSelectAllEmployees property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSelectAllEmployees()
	{
		if (ivjchkSelectAllEmployees == null)
		{
			try
			{
				ivjchkSelectAllEmployees = new javax.swing.JCheckBox();
				ivjchkSelectAllEmployees.setBounds(155, 213, 157, 22);
				ivjchkSelectAllEmployees.setName(
					"ivjchkSelectAllEmployees");
				// defect 9943
				//ivjchkSelectAllEmployees.setMnemonic(83);
				ivjchkSelectAllEmployees.setMnemonic(KeyEvent.VK_S);
				ivjchkSelectAllEmployees.setText(
					CHKBX_TXT_SELECT_ALL_EMPS);
				// end defect 9943 
				ivjchkSelectAllEmployees.setMaximumSize(
					new java.awt.Dimension(142, 22));
				ivjchkSelectAllEmployees.setMinimumSize(
					new java.awt.Dimension(142, 22));
				// user code begin {1}
				ivjchkSelectAllEmployees.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSelectAllEmployees;
	}

	/**
	 * Return the ivjFrmEmployeeSelectionFUN011ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmEmployeeSelectionFUN011ContentPane1()
	{
		if (ivjFrmEmployeeSelectionFUN011ContentPane1 == null)
		{
			try
			{
				ivjFrmEmployeeSelectionFUN011ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmEmployeeSelectionFUN011ContentPane1.setName(
					"ivjFrmEmployeeSelectionFUN011ContentPane1");
				ivjFrmEmployeeSelectionFUN011ContentPane1.setLayout(
					null);
				ivjFrmEmployeeSelectionFUN011ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmEmployeeSelectionFUN011ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(425, 250));
				ivjFrmEmployeeSelectionFUN011ContentPane1.add(
					getJScrollPaneEmployeeSelection(),
					null);
				ivjFrmEmployeeSelectionFUN011ContentPane1.add(
					getchkSelectAllEmployees(),
					null);
				ivjFrmEmployeeSelectionFUN011ContentPane1.add(
					getButtonPanel1(),
					null);
				// user code begin {1}
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmEmployeeSelectionFUN011ContentPane1;
	}

	/**
	 * Return the ivjtblEmployees property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblEmployees()
	{
		if (ivjtblEmployees == null)
		{
			try
			{
				ivjtblEmployees = new RTSTable();
				ivjtblEmployees.setName("ivjtblEmployees");
				getJScrollPaneEmployeeSelection().setColumnHeaderView(
					ivjtblEmployees.getTableHeader());
				getJScrollPaneEmployeeSelection()
					.getViewport()
					.setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblEmployees.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblEmployees.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN011());
				ivjtblEmployees.setShowVerticalLines(false);
				ivjtblEmployees.setShowHorizontalLines(false);
				ivjtblEmployees.setAutoCreateColumnsFromModel(false);
				ivjtblEmployees.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblEmployees.setBounds(0, 0, 355, 85);
				// user code begin {1}
				caEmployeesTableModel =
					(TMFUN011) ivjtblEmployees.getModel();
					
				// Employee Id 
				TableColumn laTableColumnA =
					ivjtblEmployees.getColumn(
						//ivjtblEmployees.getColumnName(0));
						ivjtblEmployees.getColumnName(
						FundsConstant.FUN011_EMPLOYEE_ID));
				laTableColumnA.setPreferredWidth(50);
				
				// Last Name  
				TableColumn laTableColumnB =
					ivjtblEmployees.getColumn(
						//ivjtblEmployees.getColumnName(1));
						ivjtblEmployees.getColumnName(
						FundsConstant.FUN011_LAST_NAME));
				laTableColumnB.setPreferredWidth(50);
				
				// First Name 
				TableColumn laTableColumnC =
					ivjtblEmployees.getColumn(
					//ivjtblEmployees.getColumnName(1));
					ivjtblEmployees.getColumnName(
					FundsConstant.FUN011_FIRST_NAME));
				// end defect 9943 
				
				laTableColumnC.setPreferredWidth(50);
				ivjtblEmployees.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblEmployees.init();
				ivjtblEmployees.addActionListener(this);
				ivjtblEmployees.addMultipleSelectionListener(this);
				ivjtblEmployees.setBackground(Color.white);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblEmployees;
	}

	/**
	 * Return the ivjJPaneEmployeeSelection property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPaneEmployeeSelection()
	{
		if (ivjJScrollPaneEmployeeSelection == null)
		{
			try
			{
				ivjJScrollPaneEmployeeSelection =
					new javax.swing.JScrollPane();
				ivjJScrollPaneEmployeeSelection.setName(
					"ivjJPaneEmployeeSelection");
				ivjJScrollPaneEmployeeSelection
					.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPaneEmployeeSelection
					.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPaneEmployeeSelection.setPreferredSize(
					new java.awt.Dimension(453, 419));
				getJScrollPaneEmployeeSelection().setViewportView(
					gettblEmployees());
				// user code begin {1}
				ivjJScrollPaneEmployeeSelection.setBounds(
					20,
					20,
					402,
					170);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJScrollPaneEmployeeSelection;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable aeException)
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("FrmEmployeeSelectionFUN011");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(450, 335);
			setTitle(TITLE_FUN011);
			setContentPane(getFrmEmployeeSelectionFUN011ContentPane1());
		}
		catch (java.lang.Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgsString[]
	 */
	public static void main(java.lang.String[] aarrArgs)
	{
		try
		{
			FrmEmployeeSelectionFUN011 laFrmEmployeeSelectionFUN011;
			laFrmEmployeeSelectionFUN011 =
				new FrmEmployeeSelectionFUN011();
			laFrmEmployeeSelectionFUN011.setModal(true);
			laFrmEmployeeSelectionFUN011
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmEmployeeSelectionFUN011.show();
			java.awt.Insets laInsets =
				laFrmEmployeeSelectionFUN011.getInsets();
			laFrmEmployeeSelectionFUN011.setSize(
				laFrmEmployeeSelectionFUN011.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmEmployeeSelectionFUN011.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmEmployeeSelectionFUN011.setVisible(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of util.JDialogTxDot");
			aeException.printStackTrace(System.out);
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
				caFundsData = (FundsData) aaDataObject;
				//Add vector of rows in drawers object to Table
				caEmployeesTableModel.add(caFundsData.getEmployees());

				//Initalize Date Range for backwards navigation on FUN007
				FundsReportData laFundsReportData =
					(FundsReportData) caFundsData.getFundsReportData();
				if (laFundsReportData != null)
				{
					laFundsReportData.setRange(0);
				}

				// defect 9653
				if (SystemProperty.isHQ())
				{
					getchkSelectAllEmployees().setEnabled(false);
				}
				// end defect 9653 

				if (caFundsData.getEmployees().size() == 1)
				{
					getchkSelectAllEmployees().setText(
						CHKBX_TXT_SELECT_EMP);
					getchkSelectAllEmployees().removeActionListener(
						this);
					getchkSelectAllEmployees().doClick();
					getchkSelectAllEmployees().setEnabled(false);
					gettblEmployees().setSelectedRow(0);
				}
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
			new Vector(ivjtblEmployees.getSelectedRowNumbers());

		if (lvSelectedRows.size() == ivjtblEmployees.getRowCount())
		{
			// defect 9653
			getchkSelectAllEmployees().setSelected(
				!SystemProperty.isHQ());
			// end defect 9653
		}
		else
		{
			getchkSelectAllEmployees().setSelected(false);
		}
	}
}
