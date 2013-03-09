package com.txdot.isd.rts.client.funds.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.RTSButton;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.data.FundsReportData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.data.ReportStatusData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.constants.FundsConstant;

/*
 *
 * FrmReportGenerationStatusFUN013.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	03/11/2005	Modify code for move to WSAD from VAJ.
 *							modify for WSAD
 *							defect 7886 Ver 5.2.3
 * B Hargrove	03/30/2005	Comment out setNextFocusableComponent() 
 *							modify getbtnEnter(),
 *							gettblCashDrawerReportStatus(),
 *							gettblEmployeeReportStatus(),setData()
 *							defect 7886 Ver 5.2.3
 * B Hargrove	07/11/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	06/08/2009  Implement FundsConstant. Additional Cleanup. 
 * 							add caReportCashDrawerStatusTableModel,
 *							 caReportEmployeeStatusTableModel,
 *							 caReportNamesTableModel 	 
 * 							delete reportCashDrawerStatusTableModel,
 *							 reportEmployeeStatusTableModel,
 *							 reportNamesTableModel 	
 * 							modify gettblCashDrawerReportStatus(),
 * 							 gettblEmployeeReportStatus() 
 *  						defect 9943 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/**
 * Screen presents the report generation status for each report
 * and cash drawer selected through screen FUN007.
 *
 * @version	Defect_POS_F	06/08/2009 
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/06/2001 13:30:59
 */

public class FrmReportGenerationStatusFUN013
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private RTSButton ivjbtnEnter = null;
	private JPanel ivjFrmReportGenerationStatusFUN013ContentPane1 =
		null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1a = null;
	private JScrollPane ivjJScrollPane1b = null;
	private JScrollPane ivjJScrollPane2 = null;
	private RTSTable ivjtblCashDrawerReportStatus = null;
	private RTSTable ivjtblEmployeeReportStatus = null;
	private RTSTable ivjtblReportNames = null;

	private FundsData laFundsData;

	private TMFUN013b caReportCashDrawerStatusTableModel;
	private TMFUN013c caReportEmployeeStatusTableModel;
	private TMFUN013 caReportNamesTableModel;

	private final static String ENTER = "Enter";

	private final static String EXCEPT_IN_MAIN =
		"Exception occurred in main() of util.JDialogTxDot";
	private final static String SELECTED_RPT = "Select Report:";
	private final static String TITLE_FUN013 =
		"Report Generation Status     FUN013";

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmReportGenerationStatusFUN013 laFrmReportGenerationStatusFUN013;
			laFrmReportGenerationStatusFUN013 =
				new FrmReportGenerationStatusFUN013();
			laFrmReportGenerationStatusFUN013.setModal(true);
			laFrmReportGenerationStatusFUN013
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmReportGenerationStatusFUN013.show();
			java.awt.Insets laInsets =
				laFrmReportGenerationStatusFUN013.getInsets();
			laFrmReportGenerationStatusFUN013.setSize(
				laFrmReportGenerationStatusFUN013.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmReportGenerationStatusFUN013.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmReportGenerationStatusFUN013.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(EXCEPT_IN_MAIN);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmReportGenerationStatusFUN013 constructor comment.
	 */
	public FrmReportGenerationStatusFUN013()
	{
		super();
		initialize();
	}
	
	/**
	 * FrmReportGenerationStatusFUN013 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmReportGenerationStatusFUN013(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	
	/**
	 * FrmReportGenerationStatusFUN013 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmReportGenerationStatusFUN013(JFrame aaParent)
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

			if (aaAE.getSource() == getbtnEnter()
				|| aaAE.getSource() == ivjtblReportNames
				|| aaAE.getSource() == ivjtblCashDrawerReportStatus
				|| aaAE.getSource() == ivjtblEmployeeReportStatus)
			{
				getController().processData(
					AbstractViewController.ENTER,
					laFundsData);
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
				ivjbtnEnter =
					new com.txdot.isd.rts.client.general.ui.RTSButton();
				ivjbtnEnter.setBounds(172, 288, 76, 26);
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
	 * Return the ivjFrmReportGenerationStatusFUN013ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmReportGenerationStatusFUN013ContentPane1()
	{
		if (ivjFrmReportGenerationStatusFUN013ContentPane1 == null)
		{
			try
			{
				ivjFrmReportGenerationStatusFUN013ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmReportGenerationStatusFUN013ContentPane1.setName(
					"ivjFrmReportGenerationStatusFUN013ContentPane1");
				ivjFrmReportGenerationStatusFUN013ContentPane1
					.setLayout(
					null);
				ivjFrmReportGenerationStatusFUN013ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmReportGenerationStatusFUN013ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(437, 326));
				ivjFrmReportGenerationStatusFUN013ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);

				ivjFrmReportGenerationStatusFUN013ContentPane1.add(
					getJPanel1(),
					null);
				ivjFrmReportGenerationStatusFUN013ContentPane1.add(
					getJScrollPane1b(),
					null);
				ivjFrmReportGenerationStatusFUN013ContentPane1.add(
					getJScrollPane1a(),
					null);
				ivjFrmReportGenerationStatusFUN013ContentPane1.add(
					getbtnEnter(),
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
		return ivjFrmReportGenerationStatusFUN013ContentPane1;
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
				ivjJPanel1 = new javax.swing.JPanel();
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setBorder(
					new TitledBorder(new EtchedBorder(), SELECTED_RPT));
				ivjJPanel1.setLayout(new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsJScrollPane2 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane2.gridx = 0;
				constraintsJScrollPane2.gridy = 2;
				constraintsJScrollPane2.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane2.weightx = 1.0;
				constraintsJScrollPane2.weighty = 1.0;
				constraintsJScrollPane2.ipadx = 334;
				constraintsJScrollPane2.ipady = 61;
				constraintsJScrollPane2.insets =
					new java.awt.Insets(4, 4, 4, 4);
				getJPanel1().add(
					getJScrollPane2(),
					constraintsJScrollPane2);
				// user code begin {1}
				ivjJPanel1.setBounds(31, 16, 356, 119);
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
	private JScrollPane getJScrollPane1a()
	{
		if (ivjJScrollPane1a == null)
		{
			try
			{
				ivjJScrollPane1a = new JScrollPane();
				ivjJScrollPane1a.setName("ivjJScrollPane1a");
				ivjJScrollPane1a.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1a.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getJScrollPane1a().setViewportView(
					gettblCashDrawerReportStatus());
				// user code begin {1}
				ivjJScrollPane1a.setBounds(31, 155, 356, 115);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJScrollPane1a;
	}

	/**
	 * Return the JScrollPane11 property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane getJScrollPane1b()
	{
		if (ivjJScrollPane1b == null)
		{
			try
			{
				ivjJScrollPane1b = new javax.swing.JScrollPane();
				ivjJScrollPane1b.setName("ivjJScrollPane1b");
				ivjJScrollPane1b.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1b.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				getJScrollPane1b().setViewportView(
					gettblEmployeeReportStatus());
				// user code begin {1}
				ivjJScrollPane1b.setBounds(31, 156, 358, 116);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJScrollPane1b;
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
				ivjJScrollPane2 = new javax.swing.JScrollPane();
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
				getJScrollPane2().setViewportView(gettblReportNames());
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
	 * Return the ivjtblCashDrawerReportStatus property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblCashDrawerReportStatus()
	{
		if (ivjtblCashDrawerReportStatus == null)
		{
			try
			{
				ivjtblCashDrawerReportStatus =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjtblCashDrawerReportStatus.setName(
					"ivjtblCashDrawerReportStatus");
				getJScrollPane1a().setColumnHeaderView(
					ivjtblCashDrawerReportStatus.getTableHeader());
				getJScrollPane1a().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblCashDrawerReportStatus.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblCashDrawerReportStatus.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN013b());
				ivjtblCashDrawerReportStatus.setShowVerticalLines(
					false);
				ivjtblCashDrawerReportStatus.setShowHorizontalLines(
					false);
				ivjtblCashDrawerReportStatus
					.setAutoCreateColumnsFromModel(
					false);
				ivjtblCashDrawerReportStatus.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblCashDrawerReportStatus.setBounds(10, 10, 298, 60);
				// user code begin {1}
				caReportCashDrawerStatusTableModel =
					(TMFUN013b) ivjtblCashDrawerReportStatus.getModel();

				// defect 9943 
				TableColumn laTableColumnA =
					ivjtblCashDrawerReportStatus
						.getColumn(
							ivjtblCashDrawerReportStatus
							.getColumnName(
							//0));
							FundsConstant.FUN013B_CASH_DRAWER));
							
				laTableColumnA.setPreferredWidth(50);
				TableColumn laTableColumnB =
					ivjtblCashDrawerReportStatus
						.getColumn(
							ivjtblCashDrawerReportStatus
							.getColumnName(
							//1));
							FundsConstant.FUN013B_REPORT_STATUS));
				// end defect 9943 

				laTableColumnB.setPreferredWidth(150);
				ivjtblCashDrawerReportStatus.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblCashDrawerReportStatus.init();
				ivjtblCashDrawerReportStatus.addActionListener(this);
				ivjtblCashDrawerReportStatus.setBackground(Color.white);
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
		return ivjtblCashDrawerReportStatus;
	}
	
	/**
	 * Return the ivjtblEmployeeReportStatus property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblEmployeeReportStatus()
	{
		if (ivjtblEmployeeReportStatus == null)
		{
			try
			{
				ivjtblEmployeeReportStatus =
					new RTSTable();
				ivjtblEmployeeReportStatus.setName(
					"ivjtblEmployeeReportStatus");
				getJScrollPane1b().setColumnHeaderView(
					ivjtblEmployeeReportStatus.getTableHeader());
				getJScrollPane1b().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblEmployeeReportStatus.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN013c());
				ivjtblEmployeeReportStatus.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblEmployeeReportStatus.setShowVerticalLines(false);
				ivjtblEmployeeReportStatus.setShowHorizontalLines(
					false);
				ivjtblEmployeeReportStatus
					.setAutoCreateColumnsFromModel(
					false);
				ivjtblEmployeeReportStatus.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblEmployeeReportStatus.setBounds(10, 10, 298, 60);
				// user code begin {1}
				caReportEmployeeStatusTableModel =
					(TMFUN013c) ivjtblEmployeeReportStatus.getModel();
				// defect 9943 
				TableColumn laTableColumnA =
					ivjtblEmployeeReportStatus
						.getColumn(
							ivjtblEmployeeReportStatus
							.getColumnName(
							// 0));
							FundsConstant.FUN013C_EMPLOYEE_ID));
				laTableColumnA.setPreferredWidth(50);
				
				TableColumn laTableColumnB =
					ivjtblEmployeeReportStatus
						.getColumn(
							ivjtblEmployeeReportStatus
							.getColumnName(
							//1));
							FundsConstant.FUN013C_REPORT_STATUS));
				// end defect 9943 

				laTableColumnB.setPreferredWidth(150);
				ivjtblEmployeeReportStatus.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblEmployeeReportStatus.init();
				ivjtblEmployeeReportStatus.addActionListener(this);
				ivjtblEmployeeReportStatus.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblEmployeeReportStatus;
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
				ivjtblReportNames =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjtblReportNames.setName("tblReportNames");
				getJScrollPane2().setColumnHeaderView(
					ivjtblReportNames.getTableHeader());
				getJScrollPane2().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				// end 7886
				ivjtblReportNames.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblReportNames.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN013());
				ivjtblReportNames.setShowVerticalLines(false);
				ivjtblReportNames.setShowHorizontalLines(false);
				ivjtblReportNames.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblReportNames.setBounds(10, 0, 319, 77);
				// user code begin {1}
				caReportNamesTableModel =
					(TMFUN013) ivjtblReportNames.getModel();
					
				// "No Name" 
				TableColumn laTableColumnA =
					ivjtblReportNames.getColumn(
						ivjtblReportNames.getColumnName(0));
				laTableColumnA.setPreferredWidth(150);
				ivjtblReportNames.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjtblReportNames.init();
				ivjtblReportNames.addActionListener(this);
				ivjtblReportNames
					.getSelectionModel()
					.addListSelectionListener(
					this);
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
			setName("FrmReportGenerationStatusFUN013");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(426, 358);
			setTitle(TITLE_FUN013);
			setContentPane(
				getFrmReportGenerationStatusFUN013ContentPane1());
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
			ReportSearchData laRptSearchData =
				(ReportSearchData) aaDataObject;
			laFundsData = (FundsData) laRptSearchData.getData();
			FundsReportData laFundsReportData =
				(FundsReportData) laFundsData.getFundsReportData();

			Vector lvReports = laFundsReportData.getReportNames();
			Vector lvReportStatus = laFundsData.getReportStatus();
			ReportStatusData laReportStatusData =
				(ReportStatusData) lvReportStatus.get(0);

			if (laFundsData.getCashDrawers() != null
				&& laFundsData.getEmployees() == null)
			{
				ivjtblEmployeeReportStatus.setVisible(false);
				ivjtblEmployeeReportStatus.setEnabled(false);
				ivjJScrollPane1b.setVisible(false);
				ivjJScrollPane1b.setEnabled(false);
				caReportCashDrawerStatusTableModel.add(
					laReportStatusData.getStatusVector());
			}
			else if (
				laFundsData.getCashDrawers() == null
					&& laFundsData.getEmployees() != null)
			{
				ivjtblCashDrawerReportStatus.setVisible(false);
				ivjtblCashDrawerReportStatus.setEnabled(false);
				ivjJScrollPane1a.setVisible(false);
				ivjJScrollPane1a.setEnabled(false);
				caReportEmployeeStatusTableModel.add(
					laReportStatusData.getStatusVector());
			}

			caReportNamesTableModel.add(lvReports);
			gettblReportNames().setRowSelectionInterval(0, 0);

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
	  * @param aaLSE ListSelectionEvent
	  */
	public void valueChanged(
		javax.swing.event.ListSelectionEvent aaLSE)
	{
		int liRow = gettblReportNames().getSelectedRow();
		String lsReportName =
			(String) ivjtblReportNames.getValueAt(liRow, 0);
		Vector lvReports = laFundsData.getReportStatus();

		for (int i = 0; i < lvReports.size(); i++)
		{
			ReportStatusData laStatus =
				(ReportStatusData) lvReports.get(i);
			if (lsReportName.equals(laStatus.getReportName()))
			{
				if (laFundsData.getCashDrawers() != null
					&& laFundsData.getEmployees() == null)
				{
					caReportCashDrawerStatusTableModel.add(
						laStatus.getStatusVector());
				}
				else
				{
					caReportEmployeeStatusTableModel.add(
						laStatus.getStatusVector());
				}
			}
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
