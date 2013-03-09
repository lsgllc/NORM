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
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 *
 * FrmReportSelectionFUN007.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/21/2002  Unselecting "Select All" when mouse clicked
 *
 * MListberger	08/28/2002  set employee and cash drawer tables list box 
 *                          enabled = false to prevent cursor from
 * 							"disappearing"
 *                          Change done in composition editor.  Corrects
 *                          defect 4674.
 * MListberger	10/22/2002  added the following code in method setData
 * 							(object)
 *                          "getchkCloseOutsAfterSubstation().
 * 							setEnabled(false); // defect 4698 fix"
 *                          This prevents the Checkbox field of "Since
 * 							Last Current Status" from losing focus when
 * 							it is the only option available.
 * B Arredondo	12/16/2002  Made changes for the
 * 							user help guide so had to make changes
 *							in actionPerformed().
 *							defect 5147 
 * R Taylor		06/24/2004	Spaced checkboxes evenly in
 * 							 "Select range: panel
 *							defect 6830 Ver 5.2.1
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							delete implements KeyListener
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	08/29/2005	Modify panels to make available space for
 * 							text: "Closeouts after Substation Summary"
 * 							defect 7886 Ver 5.2.3
 * T Pederson	10/28/2005	Comment out space bar code in keyPressed() 
 * 							method. Code in valueChanged handles this.
 * 							defect 7886 Ver 5.2.3
 * K Harrell	11/02/2005	Consistent verbiage for Closeout
 * 							defect 8379 Ver 5.2.3
 * K Harrell	01/03/2006	Screen alignment
 * 							defect 7786 Ver 5.2.3 
 * K Harrell	05/21/2008	For HQ, Report: Only Trans Recon
 * 	 						   Date Option: Only Date Range  
 * 							Prepopulate with current date info 
 * 							modify setData()
 * 							defect 9653 Ver Defect POS A  
 * K Harrell	06/08/2009	Implement RTSDate.getClockTimeNoMs(), 
 * 							additional class cleanup. 
 * 							add CASH_DRAWERS,CASH_DRAWER,DETAIL,
 * 							 BALANCE,EMPLOYEES, EMPLOYEE,
 * 							 SELECT_REPORTS, SELECT_RANGE,
 * 							 SPECIFY_DATE_RANGE
 *							add caCashDrawerTableModel,
 * 							 caEmployeesTableModel, 
 * 							 caReportNamesTableModel			
 * 							delete cashDrawerTableModel,
 * 							 employeesTableModel, reportNamesTableModel 
 * 							modify actionPerformed(), setData(),
 * 							 gettblCashDrawers(), gettblEmployees()  
 * 							defect 9943 Ver Defect_POS_F       
 * ---------------------------------------------------------------------
 */
/**
 * Screen presents reporting critera already selected and prompts
 * user to enter date criteria and report type.
 *
 * @version	Defect_POS_F 	06/08/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/09/2001 13:30:59
 */
public class FrmReportSelectionFUN007
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private JButton ivjbtnSpecifyDateRange = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkCloseOutsAfterSubstation = null;
	private JCheckBox ivjchkDisplayReportsBeforePrint = null;
	private JCheckBox ivjchkLastCloseOut = null;
	private JCheckBox ivjchkSelectAllReports = null;
	private JCheckBox ivjchkSinceLastCloseOut = null;
	private JCheckBox ivjchkSinceLastCurrentStatus = null;
	private JPanel ivjFrmReportSelectionFUN007ContentPane1 = null;
	private JLabel ivjJLabel1 = null;
	private JLabel ivjJLabel2 = null;
	private JLabel ivjJLabel3 = null;
	private JPanel ivjJPanel1 = null;
	private JPanel ivjJPanel2 = null;
	private JPanel ivjJPanel3 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JScrollPane ivjJScrollPane2 = null;
	private JScrollPane ivjJScrollPane3 = null;
	private RTSTable ivjtblCashDrawers = null;
	private RTSTable ivjtblEmployees = null;
	private RTSTable ivjtblReportNames = null;

	// Table Models 		
	// defect 9943
	private TMFUN007b caCashDrawerTableModel = null;
	private TMFUN011 caEmployeesTableModel = null;
	private TMFUN007 caReportNamesTableModel = null;
	// end defect 9943 

	//	Object 
	private FundsData caFundsData = null;

	//Constants
	private final static String CLSOUT_AFTER_SUBSUM =
		"Closeouts after Substation Summary";
	private final static String CURRENT = "CURRENT";
	private final static String DISPLAY_RPT_BEFORE =
		"Display Report(s) before Printing";
	private final static String LAST_CLSOUT = "Last Closeout";
	private final static String SELECT_ALL = "Select All Reports";
	private final static String SINCE_LAST_CLSOUT =
		"Since Last Closeout";
	private final static String SINCE_LAST_CURRSTAT =
		"Since Last Current Status";
	private final static String TITLE_FUN007 =
		"Report Selection     FUN007";

	// defect 9943
	private final static String CASH_DRAWERS =
		"Cash Drawers: ";
	private final static String CASH_DRAWER =
		"Cash Drawer: ";
	private final static String DETAIL = "DETAIL";
	private final static String BALANCE = "BALANCE";
	private final static String EMPLOYEES = "Employees:";
	private final static String EMPLOYEE = "Employee:";
	private final static String SELECT_REPORTS = "Select Reports:";
	private final static String SELECT_RANGE = "Select Range:";
	private final static String SPECIFY_DATE_RANGE =
		"Specify Date Range";
	// end defect 9943  

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmReportSelectionFUN007 laFrmReportSelectionFUN007;
			laFrmReportSelectionFUN007 = new FrmReportSelectionFUN007();
			laFrmReportSelectionFUN007.setModal(true);
			laFrmReportSelectionFUN007
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmReportSelectionFUN007.show();
			java.awt.Insets laInsets =
				laFrmReportSelectionFUN007.getInsets();
			laFrmReportSelectionFUN007.setSize(
				laFrmReportSelectionFUN007.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmReportSelectionFUN007.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmReportSelectionFUN007.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of util.JDialogTxDot");
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmReportSelectionFUN007 constructor comment.
	 */
	public FrmReportSelectionFUN007()
	{
		super();
		initialize();
	}

	/**
	 * FrmReportSelectionFUN007 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmReportSelectionFUN007(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmReportSelectionFUN007 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmReportSelectionFUN007(JFrame aaParent)
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

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				//Set Displays Reports value in FundsReportData	
				if (getchkDisplayReportsBeforePrint().isSelected())
				{
					caFundsData.getFundsReportData().setDisplayReports(
						true);
				}
				else
				{
					caFundsData.getFundsReportData().setDisplayReports(
						false);
				}
				//Set Range in FundsReportData
				if (getchkLastCloseOut().isSelected() == true)
				{
					caFundsData.getFundsReportData().setRange(
						FundsConstant.LAST_CLOSE);
				}
				else if (
					getchkCloseOutsAfterSubstation().isSelected()
						== true)
				{
					caFundsData.getFundsReportData().setRange(
						FundsConstant.AFTER_SUBSTATION);
				}
				else if (
					getchkSinceLastCurrentStatus().isSelected()
						== true)
				{
					caFundsData.getFundsReportData().setRange(
						FundsConstant.SINCE_CURRENT);
				}
				else if (
					getchkSinceLastCloseOut().isSelected() == true)
				{
					caFundsData.getFundsReportData().setRange(
						FundsConstant.SINCE_CLOSE);
				}
				//If Current Status operation, set Range different flag,
				//so will update current status timestamp
				if (getController().getTransCode().equals(CURRENT))
				{
					caFundsData.getFundsReportData().setRange(
						FundsConstant.CURRENT_STATUS);
				}
				//Create vectors to store rows of selected report names	
				Vector lvReports = new java.util.Vector();
				Vector lvSelectedRows =
					new Vector(
						ivjtblReportNames.getSelectedRowNumbers());

				//Sort the order of the selected rows in case user
				//selected rows out of order
				UtilityMethods.sort(lvSelectedRows);
				//Add each row from the total list of reports to
				//the report vector
				for (int i = 0; i < lvSelectedRows.size(); i++)
				{
					String lsRow = lvSelectedRows.get(i).toString();
					int liRow2 = Integer.parseInt(lsRow);
					String lsReport =
						(String) ivjtblReportNames.getValueAt(
							liRow2,
							0);
					lvReports.add(lsReport);
				}

				//If no reports selected, throw exception that at least
				//one must be picked
				if (lvReports.size() == 0)
				{
					// defect 9943
					//new RTSException(614);
					new RTSException(
						ErrorsConstant
							.ERR_NUM_NO_REPORTS_SELECTED)
							.displayError(
						this);
					// end defect 9943 
					return;
				}

				//Set Report names in FundsReportData
				caFundsData.getFundsReportData().setReportNames(
					lvReports);

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
				RTSHelp.displayHelp(RTSHelp.FUN007);
			}

			if (aaAE.getSource() == getbtnSpecifyDateRange())
			{
				caFundsData.getFundsReportData().setRange(
					FundsConstant.DATE_RANGE);
				getController().processData(
					VCReportSelectionFUN007.DATE_RANGE,
					caFundsData);
			}
			//If Substation checkbox, deselect other checkboxes,
			//and hide range
			else if (
				aaAE.getSource() == getchkCloseOutsAfterSubstation())
			{
				ivjchkLastCloseOut.setSelected(false);
				ivjchkSinceLastCurrentStatus.setSelected(false);
				ivjchkSinceLastCloseOut.setSelected(false);
				ivjchkCloseOutsAfterSubstation.setSelected(true);
				ivjJLabel2.setVisible(false);
				ivjJLabel3.setVisible(false);
			}
			//If LastCloseout checkbox, deselect other checkboxes,
			//and hide range
			else if (aaAE.getSource() == getchkLastCloseOut())
			{
				ivjchkCloseOutsAfterSubstation.setSelected(false);
				ivjchkSinceLastCurrentStatus.setSelected(false);
				ivjchkSinceLastCloseOut.setSelected(false);
				ivjchkLastCloseOut.setSelected(true);
				ivjJLabel2.setVisible(false);
				ivjJLabel3.setVisible(false);
			}
			//If LastCurrentStatus checkbox, deselect other checkboxes,
			//and hide range		
			else if (
				aaAE.getSource() == getchkSinceLastCurrentStatus())
			{
				ivjchkLastCloseOut.setSelected(false);
				ivjchkCloseOutsAfterSubstation.setSelected(false);
				ivjchkSinceLastCloseOut.setSelected(false);
				ivjchkSinceLastCurrentStatus.setSelected(true);
				ivjJLabel2.setVisible(false);
				ivjJLabel3.setVisible(false);
			}
			//If SinceLastCloseOut checkbox, deselect other checkboxes,
			//and hide range				
			else if (aaAE.getSource() == getchkSinceLastCloseOut())
			{
				ivjchkLastCloseOut.setSelected(false);
				ivjchkCloseOutsAfterSubstation.setSelected(false);
				ivjchkSinceLastCurrentStatus.setSelected(false);
				ivjchkSinceLastCloseOut.setSelected(true);
				ivjJLabel2.setVisible(false);
				ivjJLabel3.setVisible(false);
			}

			//If SelectallReports checkbox, highlight all reos in table
			else if (aaAE.getSource() == getchkSelectAllReports())
			{
				if (getchkSelectAllReports().isSelected())
				{
					gettblReportNames().selectAllRows(
						gettblReportNames().getRowCount());
				}
				else
				{
					gettblReportNames().unselectAllRows();
				}
				repaint();
			}
		}

		finally
		{
			doneWorking();
		}

	}

	/**
	 * Return the ivjbtnSpecifyDateRange property value.
	 * 
	 * @return JButton
	 */
	private JButton getbtnSpecifyDateRange()
	{
		if (ivjbtnSpecifyDateRange == null)
		{
			try
			{
				ivjbtnSpecifyDateRange = new JButton();
				ivjbtnSpecifyDateRange.setName(
					"ivjbtnSpecifyDateRange");
				ivjbtnSpecifyDateRange.setBounds(35, 108, 145, 25);
				ivjbtnSpecifyDateRange.setText(SPECIFY_DATE_RANGE);
				ivjbtnSpecifyDateRange.setMinimumSize(
					new java.awt.Dimension(145, 25));
				ivjbtnSpecifyDateRange.setMaximumSize(
					new java.awt.Dimension(145, 25));
				ivjbtnSpecifyDateRange.setActionCommand(
					SPECIFY_DATE_RANGE);
				ivjbtnSpecifyDateRange.setFont(
					new java.awt.Font("Arial", 1, 12));
				// user code begin {1}
				ivjbtnSpecifyDateRange.setHorizontalAlignment(
					SwingConstants.LEFT);
				ivjbtnSpecifyDateRange.setMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjbtnSpecifyDateRange.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnSpecifyDateRange;
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
				ivjButtonPanel1.setBounds(127, 348, 365, 60);
				ivjButtonPanel1.setLayout(new java.awt.GridBagLayout());
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
	 * Return the ivjchkCloseOutsAfterSubstation property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkCloseOutsAfterSubstation()
	{
		if (ivjchkCloseOutsAfterSubstation == null)
		{
			try
			{
				ivjchkCloseOutsAfterSubstation = new JCheckBox();
				ivjchkCloseOutsAfterSubstation.setName(
					"ivjchkCloseOutsAfterSubstation");
				ivjchkCloseOutsAfterSubstation.setText(
					CLSOUT_AFTER_SUBSUM);
				ivjchkCloseOutsAfterSubstation.setAlignmentY(
					java.awt.Component.TOP_ALIGNMENT);
				ivjchkCloseOutsAfterSubstation.setBounds(
					21,
					17,
					240,
					22);
				// user code begin {1}
				ivjchkCloseOutsAfterSubstation.setMnemonic(
					KeyEvent.VK_T);
				ivjchkCloseOutsAfterSubstation.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkCloseOutsAfterSubstation;
	}
	/**
	 * Return the ivjchkDisplayReportsBeforePrint property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkDisplayReportsBeforePrint()
	{
		if (ivjchkDisplayReportsBeforePrint == null)
		{
			try
			{
				ivjchkDisplayReportsBeforePrint = new JCheckBox();
				ivjchkDisplayReportsBeforePrint.setName(
					"ivjchkDisplayReportsBeforePrint");
				ivjchkDisplayReportsBeforePrint.setText(
					DISPLAY_RPT_BEFORE);
				ivjchkDisplayReportsBeforePrint.setMinimumSize(
					new java.awt.Dimension(205, 22));
				ivjchkDisplayReportsBeforePrint.setMaximumSize(
					new java.awt.Dimension(205, 22));
				ivjchkDisplayReportsBeforePrint.setActionCommand(
					DISPLAY_RPT_BEFORE);
				ivjchkDisplayReportsBeforePrint.setBounds(300, 271, 213, 22);
				// user code begin {1}
				ivjchkDisplayReportsBeforePrint.setMnemonic(
					java.awt.event.KeyEvent.VK_D);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkDisplayReportsBeforePrint;
	}

	/**
	 * Return the JCheckBox1 property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkLastCloseOut()
	{
		if (ivjchkLastCloseOut == null)
		{
			try
			{
				ivjchkLastCloseOut = new JCheckBox();
				ivjchkLastCloseOut.setName("ivjchkLastCloseOut");
				ivjchkLastCloseOut.setBounds(21, 17, 115, 22);
				ivjchkLastCloseOut.setText(LAST_CLSOUT);
				ivjchkLastCloseOut.setAlignmentY(
					java.awt.Component.TOP_ALIGNMENT);
				ivjchkLastCloseOut.setMaximumSize(
					new java.awt.Dimension(108, 22));
				ivjchkLastCloseOut.setActionCommand(LAST_CLSOUT);
				ivjchkLastCloseOut.setFont(
					new java.awt.Font("Arial", 1, 12));
				ivjchkLastCloseOut.setMinimumSize(
					new java.awt.Dimension(108, 22));
				// user code begin {1}
				ivjchkLastCloseOut.setMnemonic(KeyEvent.VK_U);
				ivjchkLastCloseOut.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkLastCloseOut;
	}

	/**
	 * Return the chkSelectAllReports property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSelectAllReports()
	{
		if (ivjchkSelectAllReports == null)
		{
			try
			{
				ivjchkSelectAllReports = new JCheckBox();
				ivjchkSelectAllReports.setName(
					"ivjchkSelectAllReports");
				ivjchkSelectAllReports.setSize(134, 18);
				ivjchkSelectAllReports.setLocation(165, 3);
				ivjchkSelectAllReports.setText(SELECT_ALL);
				// user code being {1}
				ivjchkSelectAllReports.addActionListener(this);
				ivjchkSelectAllReports.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSelectAllReports;
	}

	/**
	 * Return the ivjchkSinceLastCloseOut property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSinceLastCloseOut()
	{
		if (ivjchkSinceLastCloseOut == null)
		{
			try
			{
				ivjchkSinceLastCloseOut = new JCheckBox();
				ivjchkSinceLastCloseOut.setName(
					"ivjchkSinceLastCloseOut");
				ivjchkSinceLastCloseOut.setText(SINCE_LAST_CLSOUT);
				ivjchkSinceLastCloseOut.setActionCommand(
					SINCE_LAST_CLSOUT);
				ivjchkSinceLastCloseOut.setFont(
					new java.awt.Font("Arial", 1, 12));
				ivjchkSinceLastCloseOut.setBounds(21, 78, 146, 22);
				ivjchkSinceLastCloseOut.setMinimumSize(
					new java.awt.Dimension(143, 22));
				ivjchkSinceLastCloseOut.setMaximumSize(
					new java.awt.Dimension(143, 22));
				// user code begin {1}
				ivjchkSinceLastCloseOut.setMnemonic(
					java.awt.event.KeyEvent.VK_L);
				ivjchkSinceLastCloseOut.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSinceLastCloseOut;
	}

	/**
	 * Return the ivjchkSinceLastCurrentStatus property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSinceLastCurrentStatus()
	{
		if (ivjchkSinceLastCurrentStatus == null)
		{
			try
			{
				ivjchkSinceLastCurrentStatus = new JCheckBox();
				ivjchkSinceLastCurrentStatus.setName(
					"chkSinceLastCurrentStatus");
				ivjchkSinceLastCurrentStatus.setText(
					SINCE_LAST_CURRSTAT);
				ivjchkSinceLastCurrentStatus.setMinimumSize(
					new java.awt.Dimension(171, 22));
				ivjchkSinceLastCurrentStatus.setMaximumSize(
					new java.awt.Dimension(171, 22));
				ivjchkSinceLastCurrentStatus.setActionCommand(
					SINCE_LAST_CURRSTAT);
				ivjchkSinceLastCurrentStatus.setFont(
					new java.awt.Font("Arial", 1, 12));
				ivjchkSinceLastCurrentStatus.setBounds(21, 45, 207, 22);

				// user code begin {1}
				ivjchkSinceLastCurrentStatus.setMnemonic(
					java.awt.event.KeyEvent.VK_N);
				ivjchkSinceLastCurrentStatus.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSinceLastCurrentStatus;
	}

	/**
	 * Return the ivjFrmReportSelectionFUN007ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private javax
		.swing
		.JPanel getFrmReportSelectionFUN007ContentPane1()
	{
		if (ivjFrmReportSelectionFUN007ContentPane1 == null)
		{
			try
			{
				ivjFrmReportSelectionFUN007ContentPane1 = new JPanel();
				ivjFrmReportSelectionFUN007ContentPane1.setName(
					"FrmReportSelectionFUN007ContentPane1");
				ivjFrmReportSelectionFUN007ContentPane1.setLayout(null);
				ivjFrmReportSelectionFUN007ContentPane1.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmReportSelectionFUN007ContentPane1.setMinimumSize(
					new java.awt.Dimension(624, 452));
				getFrmReportSelectionFUN007ContentPane1().add(
					getJPanel2(),
					getJPanel2().getName());
				getFrmReportSelectionFUN007ContentPane1().add(
					getJPanel3(),
					getJPanel3().getName());
				getFrmReportSelectionFUN007ContentPane1().add(
					getchkDisplayReportsBeforePrint(),
					getchkDisplayReportsBeforePrint().getName());
				getFrmReportSelectionFUN007ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmReportSelectionFUN007ContentPane1().add(
					getJPanel1(),
					getJPanel1().getName());
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
		return ivjFrmReportSelectionFUN007ContentPane1;
	}

	/**
	 * Return the ivjJLabel1 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJLabel1()
	{
		if (ivjJLabel1 == null)
		{
			try
			{
				ivjJLabel1 = new JLabel();
				ivjJLabel1.setName("ivjJLabel1");
				ivjJLabel1.setText(SELECT_REPORTS);
				ivjJLabel1.setBounds(11, 1, 92, 23);
				// user code begin {1}
				ivjJLabel1.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjJLabel1.setLabelFor(gettblReportNames());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJLabel1;
	}

	/**
	 * Return the ivjJLabel2 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJLabel2()
	{
		if (ivjJLabel2 == null)
		{
			try
			{
				ivjJLabel2 = new JLabel();
				ivjJLabel2.setName("ivjJLabel2");
				ivjJLabel2.setBounds(35, 141, 155, 14);
				ivjJLabel2.setVisible(true);
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
		return ivjJLabel2;
	}

	/**
	 * Return the JLabel3 property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getJLabel3()
	{
		if (ivjJLabel3 == null)
		{
			try
			{
				ivjJLabel3 = new JLabel();
				ivjJLabel3.setName("JLabel3");
				ivjJLabel3.setBounds(35, 166, 155, 14);
				ivjJLabel3.setVisible(true);
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
		return ivjJLabel3;
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
				ivjJPanel1.setBounds(10, 8, 605, 135);
				ivjJPanel1.setEnabled(false);
				getJPanel1().add(
					getJScrollPane2(),
					getJScrollPane2().getName());
				getJPanel1().add(
					getJScrollPane3(),
					getJScrollPane3().getName());
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
				ivjJPanel2 = new JPanel();
				ivjJPanel2.setName("ivjJPanel2");
				ivjJPanel2.setLayout(null);
				ivjJPanel2.setBounds(13, 147, 272, 193);
				getJPanel2().add(
					getchkLastCloseOut(),
					getchkLastCloseOut().getName());
				getJPanel2().add(
					getchkCloseOutsAfterSubstation(),
					getchkCloseOutsAfterSubstation().getName());
				getJPanel2().add(
					getchkSinceLastCurrentStatus(),
					getchkSinceLastCurrentStatus().getName());
				getJPanel2().add(
					getchkSinceLastCloseOut(),
					getchkSinceLastCloseOut().getName());
				getJPanel2().add(
					getbtnSpecifyDateRange(),
					getbtnSpecifyDateRange().getName());
				getJPanel2().add(getJLabel2(), getJLabel2().getName());
				getJPanel2().add(getJLabel3(), getJLabel3().getName());
				// user code begin {1}
				Border laBorder =
					new TitledBorder(new EtchedBorder(), SELECT_RANGE);
				ivjJPanel2.setBorder(laBorder);
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
	 * Return the ivjJPanel3 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel3()
	{
		if (ivjJPanel3 == null)
		{
			try
			{
				ivjJPanel3 = new JPanel();
				ivjJPanel3.setName("ivjJPanel3");
				ivjJPanel3.setLayout(null);
				ivjJPanel3.setBounds(293, 149, 316, 118);
				getJPanel3().add(getJLabel1(), getJLabel1().getName());
				getJPanel3().add(
					getJScrollPane1(),
					getJScrollPane1().getName());
				getJPanel3().add(
					getchkSelectAllReports(),
					getchkSelectAllReports().getName());
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
		return ivjJPanel3;
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
					JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				ivjJScrollPane1.setBounds(12, 22, 295, 84);
				getJScrollPane1().setViewportView(gettblReportNames());
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
	 * Return the ivjJScrollPane2 property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane2()
	{
		if (ivjJScrollPane2 == null)
		{
			try
			{
				ivjJScrollPane2 = new JScrollPane();
				ivjJScrollPane2.setName("ivjJScrollPane2");
				ivjJScrollPane2.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane2.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane2.setBounds(9, 25, 586, 103);
				ivjJScrollPane2.setEnabled(false);
				getJScrollPane2().setViewportView(gettblEmployees());
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
		return ivjJScrollPane2;
	}

	/**
	 * Return the ivjJScrollPane3 property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane3()
	{
		if (ivjJScrollPane3 == null)
		{
			try
			{
				ivjJScrollPane3 = new JScrollPane();
				ivjJScrollPane3.setName("ivjJScrollPane3");
				ivjJScrollPane3.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane3.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane3.setBounds(9, 25, 586, 101);
				getJScrollPane3().setViewportView(gettblCashDrawers());
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
		return ivjJScrollPane3;
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
				ivjtblCashDrawers = new RTSTable();
				ivjtblCashDrawers.setName("ivjtblCashDrawers");
				getJScrollPane3().setColumnHeaderView(
					ivjtblCashDrawers.getTableHeader());
				getJScrollPane3().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblCashDrawers.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN007b());
				ivjtblCashDrawers.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblCashDrawers.setShowVerticalLines(false);
				ivjtblCashDrawers.setShowHorizontalLines(false);
				ivjtblCashDrawers.setAutoCreateColumnsFromModel(false);
				ivjtblCashDrawers.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblCashDrawers.setBounds(0, 0, 408, 64);
				ivjtblCashDrawers.setRowSelectionAllowed(false);
				ivjtblCashDrawers.setEnabled(false);
				caCashDrawerTableModel =
					(TMFUN007b) ivjtblCashDrawers.getModel();

				// defect 9943 
				// Use FundsConstant 
				TableColumn laTableColumnA =
					ivjtblCashDrawers.getColumn(
					ivjtblCashDrawers.getColumnName(
					//0));
					FundsConstant.FUN007B_ID));
				laTableColumnA.setPreferredWidth(115);
				TableColumn laTableColumnB =
					ivjtblCashDrawers.getColumn(
					ivjtblCashDrawers.getColumnName(
					//1));
					FundsConstant.FUN007B_LAST_CLOSEOUT));
				laTableColumnB.setPreferredWidth(115);
				TableColumn laTableColumnC =
					ivjtblCashDrawers.getColumn(
					ivjtblCashDrawers.getColumnName(
					//2));
					FundsConstant.FUN007B_LAST_CURRENT_STATUS));
				// end defect 9943 					

				laTableColumnC.setPreferredWidth(115);
				ivjtblCashDrawers.setRowSelectionAllowed(false);
				ivjtblCashDrawers.setColumnSelectionAllowed(false);
				ivjtblCashDrawers.init();
				ivjtblCashDrawers.addActionListener(this);
				ivjtblCashDrawers.setBackground(Color.white);
				// user code being {1} 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblCashDrawers;
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
				getJScrollPane2().setColumnHeaderView(
					ivjtblEmployees.getTableHeader());
				getJScrollPane2().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblEmployees.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN011());
				ivjtblEmployees.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblEmployees.setShowVerticalLines(false);
				ivjtblEmployees.setShowHorizontalLines(false);
				ivjtblEmployees.setAutoCreateColumnsFromModel(false);
				ivjtblEmployees.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblEmployees.setBounds(0, 0, 408, 64);
				ivjtblEmployees.setEnabled(false);
				// user code begin {1}
				caEmployeesTableModel =
					(TMFUN011) ivjtblEmployees.getModel();

				// defect 9943 
				// Use FundsConstant 
				TableColumn laTableColumnA = ivjtblEmployees.getColumn(
					ivjtblEmployees.getColumnName(
					//0));
					FundsConstant.FUN011_EMPLOYEE_ID));
				laTableColumnA.setPreferredWidth(115);
				TableColumn laTableColumnB = ivjtblEmployees.getColumn(
					ivjtblEmployees.getColumnName(
					//1));
					FundsConstant.FUN011_LAST_NAME));
				laTableColumnB.setPreferredWidth(115);
				TableColumn laTableColumnC = ivjtblEmployees.getColumn(
					ivjtblEmployees.getColumnName(
					//2));
					FundsConstant.FUN011_FIRST_NAME));
				// end defect 9943	
											
				laTableColumnC.setPreferredWidth(115);
				ivjtblEmployees.setRowSelectionAllowed(false);
				ivjtblEmployees.init();
				ivjtblEmployees.addActionListener(this);
				ivjtblEmployees.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblEmployees;
	}

	/**
	 * Return the ivjtblReportNames property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblReportNames()
	{
		if (ivjtblReportNames == null)
		{
			try
			{
				ivjtblReportNames = new RTSTable();
				ivjtblReportNames.setName("ivjtblReportNames");
				getJScrollPane1().setColumnHeaderView(
					ivjtblReportNames.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblReportNames.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblReportNames.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN007());
				ivjtblReportNames.setShowVerticalLines(false);
				ivjtblReportNames.setShowHorizontalLines(false);
				ivjtblReportNames.setBounds(0, 0, 274, 106);
				// user code begin {1}
				caReportNamesTableModel =
					(TMFUN007) ivjtblReportNames.getModel();
				ivjtblReportNames.setManagingFocus(true);
				ivjtblReportNames.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblReportNames.init();
				ivjtblReportNames.addActionListener(this);
				ivjtblReportNames.addMultipleSelectionListener(this);
				ivjtblReportNames.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblReportNames;
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
			setName("FrmReportSelectionFUN007");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(625, 433);
			setTitle(TITLE_FUN007);
			setContentPane(getFrmReportSelectionFUN007ContentPane1());
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
			|| aaKE.getKeyCode() == KeyEvent.VK_LEFT)
		{

			//Logic to handle focus on checkboxes	   
			if (getchkCloseOutsAfterSubstation().hasFocus())
			{
				getchkSelectAllReports().requestFocus();
			}
			else if (getchkLastCloseOut().hasFocus())
			{
				getchkSelectAllReports().requestFocus();
			}
			else if (getchkSinceLastCurrentStatus().hasFocus())
			{
				if (getchkCloseOutsAfterSubstation().isVisible())
				{
					getchkCloseOutsAfterSubstation().requestFocus();
				}
				else if (getchkLastCloseOut().isEnabled())
				{
					getchkLastCloseOut().requestFocus();
				}
				else
				{
					getchkSelectAllReports().requestFocus();
				}
			}
			else if (getchkSinceLastCloseOut().hasFocus())
			{
				if (getchkSinceLastCurrentStatus().isEnabled())
				{
					getchkSinceLastCurrentStatus().requestFocus();
				}
				else if (getchkLastCloseOut().isEnabled())
				{
					getchkLastCloseOut().requestFocus();
				}
			}
			else if (getchkSelectAllReports().hasFocus())
			{
				if (getchkSinceLastCloseOut().isEnabled())
				{
					getchkSinceLastCloseOut().requestFocus();
				}
				else
				{
					getchkSinceLastCurrentStatus().requestFocus();
				}
			}

		}

		else if (
			aaKE.getKeyCode() == KeyEvent.VK_DOWN
				|| aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			//Logic to handle focus on checkboxes    	   
			if (getchkCloseOutsAfterSubstation().hasFocus())
			{
				if (getchkSinceLastCurrentStatus().isEnabled())
				{
					getchkSinceLastCurrentStatus().requestFocus();
				}
				else if (getchkSinceLastCloseOut().isEnabled())
				{
					getchkSinceLastCloseOut().requestFocus();
				}
			}
			else if (getchkLastCloseOut().hasFocus())
			{
				if (getchkSinceLastCurrentStatus().isEnabled())
				{
					getchkSinceLastCurrentStatus().requestFocus();
				}
				else if (getchkSinceLastCloseOut().isEnabled())
				{
					getchkSinceLastCloseOut().requestFocus();
				}
			}
			else if (getchkSinceLastCurrentStatus().hasFocus())
			{
				if (getchkSinceLastCloseOut().isEnabled())
				{
					getchkSinceLastCloseOut().requestFocus();
				}
				else
				{
					getchkSelectAllReports().requestFocus();
				}
			}
			else if (getchkSinceLastCloseOut().hasFocus())
			{
				getchkSelectAllReports().requestFocus();
			}
			else if (getchkSelectAllReports().hasFocus())
			{
				if (getchkCloseOutsAfterSubstation().isVisible()
					&& getchkCloseOutsAfterSubstation().isEnabled())
				{
					getchkCloseOutsAfterSubstation().requestFocus();
				}
				else if (
					getchkLastCloseOut().isVisible()
						&& getchkLastCloseOut().isEnabled())
				{
					getchkLastCloseOut().requestFocus();
				}
				else
				{
					getchkSinceLastCurrentStatus().requestFocus();
				}
			}
		}

		super.keyPressed(aaKE);
	}

	/**
	 * all subclasses must implement this method - it sets the data
	 * on the screen and is how the controller relays information
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
				//Intialze FUN007 displayed for navigation purposes
				caFundsData = (FundsData) aaDataObject;
				caFundsData.setDisplayedFUN007(true);

				//If Cash Drawer is Entity,add selected drawers to table
				if (caFundsData.getCashDrawers() != null
					&& caFundsData.getEmployees() == null)
				{
					ivjtblEmployees.setVisible(false);
					ivjJScrollPane2.setVisible(false);
					caCashDrawerTableModel.add(
						caFundsData.getSelectedCashDrawers());
				}
				//If Employee is Entity. add selected employees to table
				else if (
					caFundsData.getCashDrawers() == null
						&& caFundsData.getEmployees() != null)
				{
					ivjtblCashDrawers.setVisible(false);
					ivjJScrollPane3.setVisible(false);
					caEmployeesTableModel.add(
						caFundsData.getSelectedEmployees());
				}

				//Intially Hide Date Range values
				if (caFundsData.getFundsReportData().getRange()
					!= FundsConstant.DATE_RANGE)
				{
					ivjJLabel2.setVisible(false);
					ivjJLabel3.setVisible(false);
				}

				//Add border depending on which table is enabled
				Border laBorder =
					new TitledBorder(new EtchedBorder(), "");

				if (caFundsData.getFundsReportData().getEntity()
					== FundsConstant.CASH_DRAWER)
				{
					String lsText =
						caFundsData.getSelectedCashDrawers().size() == 1
							? CASH_DRAWER
							: CASH_DRAWERS;

					laBorder =
						new TitledBorder(new EtchedBorder(), lsText);
				}
				else
				{
					String lsText =
						caFundsData.getSelectedEmployees().size() == 1
							? EMPLOYEE
							: EMPLOYEES;

					laBorder =
						new TitledBorder(new EtchedBorder(), lsText);
				}
				ivjJPanel1.setBorder(laBorder);
				repaint();

			}

			//Set checkboxes if using backward navigation, and range
			//was already selected
			if (caFundsData.getFundsReportData().getRange()
				== FundsConstant.AFTER_SUBSTATION)
			{
				getchkCloseOutsAfterSubstation().setSelected(true);
				getchkLastCloseOut().setSelected(false);
				getchkSinceLastCurrentStatus().setSelected(false);
				getchkSinceLastCloseOut().setSelected(false);
			}
			if (caFundsData.getFundsReportData().getRange()
				== FundsConstant.LAST_CLOSE)
			{
				getchkCloseOutsAfterSubstation().setSelected(false);
				getchkLastCloseOut().setSelected(true);
				getchkSinceLastCurrentStatus().setSelected(false);
				getchkSinceLastCloseOut().setSelected(false);
			}

			if (caFundsData.getFundsReportData().getRange()
				== FundsConstant.SINCE_CURRENT)
			{
				getchkCloseOutsAfterSubstation().setSelected(false);
				getchkLastCloseOut().setSelected(false);
				getchkSinceLastCurrentStatus().setSelected(true);
				getchkSinceLastCloseOut().setSelected(false);
			}
			if (caFundsData.getFundsReportData().getRange()
				== FundsConstant.SINCE_CLOSE)
			{
				getchkCloseOutsAfterSubstation().setSelected(false);
				getchkLastCloseOut().setSelected(false);
				getchkSinceLastCurrentStatus().setSelected(false);
				getchkSinceLastCloseOut().setSelected(true);
			}

			//Enable differet GUI for Current Status event
			if (getController().getTransCode().equals(CURRENT))
			{
				getchkSinceLastCurrentStatus().setSelected(true);
				// put in code to not allow deselect for
				// sincelastcurrentstatus 
				getchkDisplayReportsBeforePrint().setSelected(true);
				getchkCloseOutsAfterSubstation().setVisible(false);
				getchkCloseOutsAfterSubstation().setEnabled(false);
				// defect 4698 fix
				getchkLastCloseOut().setEnabled(false);
				getchkSinceLastCloseOut().setEnabled(false);
				getchkSelectAllReports().setSelected(true);
				getbtnSpecifyDateRange().setEnabled(false);
				//Only Display Payment Report in report names table
				Vector rows = new java.util.Vector();
				rows.add(FundsConstant.PAYMENT_REPORT);
				caReportNamesTableModel.add(rows);
				ivjtblReportNames.setSelectedRow(0);
			}

			//Enable differnt GUI for Detail Reports Event
			if (getController().getTransCode().equals(DETAIL))
			{
				if (caFundsData.getFundsReportData().getRange() == 0)
				{
					getchkLastCloseOut().setSelected(true);
				}
				// put in code to must select one value
				getchkDisplayReportsBeforePrint().setSelected(true);
				getchkCloseOutsAfterSubstation().setVisible(false);
				getchkCloseOutsAfterSubstation().setEnabled(false);
				getchkSinceLastCurrentStatus().setEnabled(false);
				getbtnSpecifyDateRange().setEnabled(false);
				Vector lvRows = new java.util.Vector();
				lvRows.add(FundsConstant.PAYMENT_REPORT);
				//Only disply transrecon report if only 1 cash drawer
				//was selected
				if (caFundsData.getSelectedCashDrawers().size() == 1)
				{
					lvRows.add(FundsConstant.TRANSACTION_REPORT);
				}
				lvRows.add(FundsConstant.FEES_REPORT);
				lvRows.add(FundsConstant.INVENTORYD_REPORT);
				lvRows.add(FundsConstant.INVENTORYS_REPORT);
				caReportNamesTableModel.add(lvRows);
			}

			//Enable different GUI for Balance Reports Event
			if (getController().getTransCode().equals(BALANCE))
			{
				getchkDisplayReportsBeforePrint().setSelected(true);
				//If Entity is employee or more than one cash drawer
				//was selected, display Closeout Subsation checkbox
				if (caFundsData.getFundsReportData().getEntity()
					== FundsConstant.EMPLOYEE
					|| caFundsData.getSelectedCashDrawers().size() > 1)
				{
					getchkLastCloseOut().setVisible(false);
					getchkLastCloseOut().setEnabled(false);
					getchkCloseOutsAfterSubstation().setVisible(true);
					if (caFundsData.getFundsReportData().getRange()
						== 0)
					{
						getchkCloseOutsAfterSubstation().setSelected(
							true);
					}
				}
				//Else display LastCloseOut option
				else
				{
					getchkCloseOutsAfterSubstation().setVisible(false);
					getchkCloseOutsAfterSubstation().setEnabled(false);
					getchkLastCloseOut().setVisible(true);
					if (caFundsData.getFundsReportData().getRange()
						== 0)
					{
						getchkLastCloseOut().setSelected(true);
					}
				}

				//Set report names
				Vector lvRows = new java.util.Vector();
				// defect 9653
				// For HQ 
				//      Reports: Only TransRecon
				// Date Options: Only Date Range  
				if (SystemProperty.isHQ())
				{
					lvRows.add(FundsConstant.TRANSACTION_REPORT);
				}
				else
				{
					//Only add transrecon report if 1 employee or 1
					//cash drawer selected
					lvRows.add(FundsConstant.PAYMENT_REPORT);
					if (caFundsData.getFundsReportData().getEntity()
						== FundsConstant.CASH_DRAWER
						&& caFundsData.getSelectedCashDrawers().size()
							== 1)
					{
						lvRows.add(FundsConstant.TRANSACTION_REPORT);
					}
					if (caFundsData.getFundsReportData().getEntity()
						== FundsConstant.EMPLOYEE
						&& caFundsData.getSelectedEmployees().size() == 1)
					{
						lvRows.add(FundsConstant.TRANSACTION_REPORT);
					}
					lvRows.add(FundsConstant.FEES_REPORT);
					lvRows.add(FundsConstant.INVENTORYD_REPORT);
					lvRows.add(FundsConstant.INVENTORYS_REPORT);
				}
				caReportNamesTableModel.add(lvRows);
				if (SystemProperty.isHQ())
				{
					getchkSelectAllReports().setSelected(true);
					getchkSelectAllReports().setEnabled(false);
					getchkLastCloseOut().setSelected(false);
					getchkCloseOutsAfterSubstation().setEnabled(false);
					getchkLastCloseOut().setEnabled(false);
					getchkSinceLastCloseOut().setEnabled(false);
					getchkSinceLastCurrentStatus().setEnabled(false);
					ivjtblReportNames.setSelectedRow(0);
					caFundsData.getFundsReportData().setRange(
						FundsConstant.DATE_RANGE);
					if (caFundsData.getFundsReportData().getFromRange()
						== null)
					{
						caFundsData.getFundsReportData().setToRange(
							new RTSDate());
						RTSDate laRTSDate = new RTSDate();
						laRTSDate.setTime(0);
						caFundsData.getFundsReportData().setFromRange(
							laRTSDate);
					}
				}
				else
				{
					//If Entity = employee, disable SinceLastCurrentStatus
					if (caFundsData.getFundsReportData().getEntity()
						== FundsConstant.EMPLOYEE)
					{
						getchkSinceLastCurrentStatus().setEnabled(
							false);
					}
				}
				// end defect 9653 
				//If Date Range Selected, display labels and deselect
				//all checkboxes
				if (caFundsData.getFundsReportData().getRange()
					== FundsConstant.DATE_RANGE)
				{
					getchkCloseOutsAfterSubstation().setSelected(false);
					getchkLastCloseOut().setSelected(false);
					getchkSinceLastCurrentStatus().setSelected(false);
					getchkSinceLastCloseOut().setSelected(false);
					ivjJLabel2.setVisible(true);
					ivjJLabel3.setVisible(true);
					RTSDate laFromDate =
						caFundsData.getFundsReportData().getFromRange();

					// defect 9943  
					String laFromTime =
						caFundsData
							.getFundsReportData()
							.getFromRange()
							.getClockTimeNoMs();
					//.getClockTime();

					RTSDate laToDate =
						caFundsData.getFundsReportData().getToRange();
					String laToTime =
						caFundsData
							.getFundsReportData()
							.getToRange()
							.getClockTimeNoMs();
					ivjJLabel2.setText(
						"From:  " + laFromDate + "  " + laFromTime);
					//.substring(0, 8));

					ivjJLabel3.setText(
						"    To:   " + laToDate + "  " + laToTime);
					//.substring(0, 8));
					// end defect 9943 
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
			new Vector(ivjtblReportNames.getSelectedRowNumbers());
		if (lvSelectedRows.size() == ivjtblReportNames.getRowCount())
		{
			getchkSelectAllReports().setSelected(true);
		}
		else
		{
			getchkSelectAllReports().setSelected(false);
		}
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
