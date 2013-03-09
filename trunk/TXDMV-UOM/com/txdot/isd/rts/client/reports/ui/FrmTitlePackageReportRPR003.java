package com.txdot.isd.rts.client.reports.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmTitlePackageReportRPR003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * M Rajangam	05/10/2002	Changed errorcode to 641
 * 							ref:CQU100003869
 * J Kwik		05/11/2002	Found defect with entering invalid date when
 *							pressing Enter during CQU100003866 fix.
 * MAbs			05/21/2002	Deselecting "Select All" when mouse clicked
 * K Harrell	01/17/2005	JavaDoc/Formatting/Variable Name Cleanup
 *							Remove references to 
 *							FrmQuickCounterReportREG040
 *							Ver 5.2.3 
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	05/31/2005	removed setNextFocusableComponent
 * 							modify getScrollPaneTable(), setData()
 * 							defect 7896 Ver 5.2.3
 * J Zwiener	06/10/2005	switch order of validation: date before wsid
 * 							table
 * 							modify actionPerformed()
 * 							defect 7658 Ver 5.2.3
 * S Johnston	06/16/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify getButtonPanel1, keyPressed
 * 							defect 8240 Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify actionPerformed(),
 * 							getScrollPaneTable(), handleException(),
 * 							setData()
 *							defect 7896 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * T Pederson	10/31/2005	Comment out keyPressed() method. Code no
 * 							longer necessary due to 8240.
 * 							defect 7896 Ver 5.2.3
 * Jeff S.		11/03/2005	Remove sort of assigned WS ids since it is
 * 							done in the getter of Assigned WS ids.
 * 							modify setData()
 * 							defect 8418 Ver 5.2.3  
 * K Harrell	08/25/2009	Implement ReportSearchData, 
 * 							  AssignedWorkstationsCache.getTtlPkgWsIds(), 
 * 							  startWorking(), doneWorking() 
 * 							delete getBuilderData()  
 * 							modify actionPerformed(), setData() 
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	09/25/2010	Increase size of Table so that does not 
 * 							break across row entry. Automatically 
 * 							select entry, "Select All" if just one row. 
 * 							Remove GridBagLayout to resize  
 * 							Date Entry field, frame 
 * 							modify setData(), valueChanged(),
 * 							 getFrmTitlePackageReportRPR003ContentPane1()   
 * 							defect 10013 Ver 6.6.0 	
 * ---------------------------------------------------------------------
 */
/**
 * Class for RPR003 frame for Title Package Report
 *
 * @version	6.6.0			09/25/2010
 * @author	Administrator
 * <br>Creation Date:		07/27/2001 10:17:40  
 */
public class FrmTitlePackageReportRPR003
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSelectAllWorkstations = null;
	private JLabel ivjstcLblReportDate = null;
	private JLabel ivjstcLblWorkstationNumber = null;
	private JPanel ivjFrmTitlePackageReportRPR003ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private RTSDateField ivjdateField = null;
	private RTSTable ivjScrollPaneTable = null;
	private TMRPR003 caWkIdsTableModel = null;

	// Object 
	private ReportSearchData caRptSearchData = new ReportSearchData();

	// Constants 
	private final static String TITLE =
		"Title Package Report    RPR003";
	private final static String REPORT_DATE = "Report Date:";
	private final static String SEL_ALL_WS = "Select All Workstations";
	private final static String WS_NUM = "Workstation Number";

	/**
	 * FrmTitlePackageReportRPR003 constructor
	 */
	public FrmTitlePackageReportRPR003()
	{
		super();
		initialize();
	}

	/**
	 * FrmTitlePackageReportRPR003 constructor
	 * 
	 * @param aaParent Dialog 
	 */
	public FrmTitlePackageReportRPR003(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmTitlePackageReportRPR003 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmTitlePackageReportRPR003(JFrame aaParent)
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

			// ENTER 
			if (aaAE
				.getActionCommand()
				.equals(CommonConstant.BTN_TXT_ENTER))
			{
				RTSException leRTSEx = new RTSException();
				// defect 7658
				//  switched order of validation: date before wsid table
				//
				// Validate Date entry
				int liAMInputDate = 0;
				if (!getdateField().isValidDate())
				{
					leRTSEx.addException(
						new RTSException(733),
						getdateField());
				}
				else
				{
					RTSDate laRTSDate = getdateField().getDate();
					liAMInputDate = laRTSDate.getAMDate();

					int liAMCurrentDate =
						(RTSDate.getCurrentDate()).getAMDate();

					if ((liAMCurrentDate - liAMInputDate) > 10)
					{
						leRTSEx.addException(
							new RTSException(581),
							getdateField());
					}
					if ((liAMCurrentDate - liAMInputDate) < 0)
					{
						leRTSEx.addException(
							new RTSException(581),
							getdateField());
					}
				}

				// Validate WSIDs selections
				Vector lvWSIDs = new Vector();

				// Check for selected rows
				Vector lvSelectedRows =
					new Vector(
						ivjScrollPaneTable.getSelectedRowNumbers());
				for (int i = lvSelectedRows.size(); i > 0; i--)
				{
					String lsRow = lvSelectedRows.get(i - 1).toString();
					int liRow = Integer.parseInt(lsRow);
					lvWSIDs.add(
						ivjScrollPaneTable.getModel().getValueAt(
							liRow,
							0));
				}
				if (lvWSIDs.size() == 0)
				{
					leRTSEx.addException(
						new RTSException(641),
						getScrollPaneTable());
				}

				// end defect 7658
				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}

				// defect 8628 
				caRptSearchData.initForClient(ReportConstant.ONLINE);
				// end defect 8628 
				caRptSearchData.setVector(lvWSIDs);
				caRptSearchData.setKey2(String.valueOf(liAMInputDate));

				getController().processData(
					AbstractViewController.ENTER,
					caRptSearchData);
			}
			// CANCEL 
			else if (
				aaAE.getActionCommand().equals(
					CommonConstant.BTN_TXT_CANCEL))
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// HELP
			else if (
				aaAE.getActionCommand().equals(
					CommonConstant.BTN_TXT_HELP))
			{
				RTSHelp.displayHelp(RTSHelp.RPR003);
			}
			// SELECT ALL CHECKBOX
			else if (aaAE.getSource() == getchkSelectAllWorkstations())
			{
				if (getchkSelectAllWorkstations().isSelected())
				{
					getScrollPaneTable().selectAllRows(
						getScrollPaneTable().getRowCount());
				}
				else
				{
					getScrollPaneTable().unselectAllRows();
				}
			}
		}
		finally
		{
			// defect 8628 
			doneWorking();
			// end defect 8628 
		}
	}

	/**
	* Return the ButtonPanel1 property value.
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
				ivjButtonPanel1.setName("ButtonPanel1");
				ivjButtonPanel1.setLayout(new GridBagLayout());
				ivjButtonPanel1.setSize(261, 47);
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.setLocation(15, 265);
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the chkSelectAllWorkstations property value.
	 * 
	 * @return JCheckBox
	 */

	private JCheckBox getchkSelectAllWorkstations()
	{
		if (ivjchkSelectAllWorkstations == null)
		{
			try
			{
				ivjchkSelectAllWorkstations = new JCheckBox();
				ivjchkSelectAllWorkstations.setSize(158, 22);
				ivjchkSelectAllWorkstations.setName(
					"chkSelectAllWorkstations");
				ivjchkSelectAllWorkstations.setMnemonic(83);
				ivjchkSelectAllWorkstations.setText(SEL_ALL_WS);
				ivjchkSelectAllWorkstations.setMaximumSize(
					new Dimension(158, 22));
				ivjchkSelectAllWorkstations.setActionCommand(
					SEL_ALL_WS);
				ivjchkSelectAllWorkstations.setMinimumSize(
					new Dimension(158, 22));
				ivjchkSelectAllWorkstations.setHorizontalAlignment(0);
				// user code begin {1}
				ivjchkSelectAllWorkstations.setLocation(62, 59);
				ivjchkSelectAllWorkstations.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjchkSelectAllWorkstations;
	}

	/**
	 * Return the dateField property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField getdateField()
	{
		if (ivjdateField == null)
		{
			try
			{
				ivjdateField = new RTSDateField();
				ivjdateField.setSize(70, 20);
				ivjdateField.setName("dateField");
				// user code begin {1}
				ivjdateField.setLocation(148, 25);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjdateField;
	}

	/**
	 * Return the getFrmTitlePackageReportRPR003ContentPane1 property value
	 * 
	 * @return JPanel
	 */

	private JPanel getFrmTitlePackageReportRPR003ContentPane1()
	{
		if (ivjFrmTitlePackageReportRPR003ContentPane1 == null)
		{
			try
			{
				ivjFrmTitlePackageReportRPR003ContentPane1 =
					new JPanel();
				ivjFrmTitlePackageReportRPR003ContentPane1.setName(
					"FrmTitlePackageReportRPR003ContentPane1");
				ivjFrmTitlePackageReportRPR003ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmTitlePackageReportRPR003ContentPane1
					.setMinimumSize(
					new Dimension(330, 305));
				// defect 10013
				ivjFrmTitlePackageReportRPR003ContentPane1.setLayout(
					null);
				ivjFrmTitlePackageReportRPR003ContentPane1.add(
					getstcLblReportDate(),
					null);
				ivjFrmTitlePackageReportRPR003ContentPane1.add(
					getdateField(),
					null);
				ivjFrmTitlePackageReportRPR003ContentPane1.add(
					getchkSelectAllWorkstations(),
					null);
				ivjFrmTitlePackageReportRPR003ContentPane1.add(
					getstcLblWorkstationNumber(),
					null);
				ivjFrmTitlePackageReportRPR003ContentPane1.add(
					getJScrollPane1(),
					null);
				ivjFrmTitlePackageReportRPR003ContentPane1.add(
					getButtonPanel1(),
					null);
				// end defect 10013 
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjFrmTitlePackageReportRPR003ContentPane1;
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
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				getJScrollPane1().setViewportView(getScrollPaneTable());
				// user code begin {1}
				ivjJScrollPane1.setSize(132, 147);
				ivjJScrollPane1.setLocation(80, 105);
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJScrollPane1;
	}

	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */

	private RTSTable getScrollPaneTable()
	{
		if (ivjScrollPaneTable == null)
		{
			try
			{
				ivjScrollPaneTable = new RTSTable();
				ivjScrollPaneTable.setName("ScrollPaneTable");
				getJScrollPane1().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				// defect 7161
				// removed reference to deprecated method
				// setBackingStroeEnabled(boolean) from JViewport
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				// end defect 7161
				ivjScrollPaneTable.setModel(new TMRPR003());
				ivjScrollPaneTable.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjScrollPaneTable.setBounds(3, 0, 135, 145);
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				// user code begin {1}
				caWkIdsTableModel =
					(TMRPR003) ivjScrollPaneTable.getModel();
				TableColumn laTCa =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(0));
				laTCa.setPreferredWidth(50);
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjScrollPaneTable.init();
				ivjScrollPaneTable.addActionListener(this);
				ivjScrollPaneTable.addMultipleSelectionListener(this);
				ivjScrollPaneTable.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjScrollPaneTable;
	}

	/**
	 * Return the stcLblReportDate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblReportDate()
	{
		if (ivjstcLblReportDate == null)
		{
			try
			{
				ivjstcLblReportDate = new JLabel();
				ivjstcLblReportDate.setSize(71, 20);
				ivjstcLblReportDate.setName("stcLblReportDate");
				ivjstcLblReportDate.setText(REPORT_DATE);
				ivjstcLblReportDate.setMaximumSize(
					new Dimension(70, 14));
				ivjstcLblReportDate.setMinimumSize(
					new Dimension(70, 14));
				// user code begin {1}
				ivjstcLblReportDate.setLocation(67, 25);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblReportDate;
	}

	/**
	 * Return the stcLblWorkstationNumber property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblWorkstationNumber()
	{
		if (ivjstcLblWorkstationNumber == null)
		{
			try
			{
				ivjstcLblWorkstationNumber = new JLabel();
				ivjstcLblWorkstationNumber.setSize(122, 14);
				ivjstcLblWorkstationNumber.setName(
					"stcLblWorkstationNumber");
				ivjstcLblWorkstationNumber.setText(WS_NUM);
				ivjstcLblWorkstationNumber.setMaximumSize(
					new Dimension(118, 14));
				ivjstcLblWorkstationNumber.setMinimumSize(
					new Dimension(118, 14));
				ivjstcLblWorkstationNumber.setHorizontalAlignment(0);
				// user code begin {1}
				ivjstcLblWorkstationNumber.setLocation(82, 88);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblWorkstationNumber;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeThrowable Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		/* Uncomment the following lines to print uncaught exceptions
		 * to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7897
		// Handle GUI exceptions this was just ignored before
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
		leRTSEx.displayError(this);
		// end defect 7897
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
			setName("FrmTitlePackageReportRPR003");
			// defect 6897
			// Do nothing if user clicks 'Close' Icon
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			// end defect 6897
			setSize(295, 350);
			setTitle(TITLE);
			setContentPane(
				getFrmTitlePackageReportRPR003ContentPane1());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmTitlePackageReportRPR003 laFrmTitlePackageReportRPR003;
			laFrmTitlePackageReportRPR003 =
				new FrmTitlePackageReportRPR003();
			laFrmTitlePackageReportRPR003.setModal(true);
			laFrmTitlePackageReportRPR003
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmTitlePackageReportRPR003.show();
			Insets insets = laFrmTitlePackageReportRPR003.getInsets();
			laFrmTitlePackageReportRPR003.setSize(
				laFrmTitlePackageReportRPR003.getWidth()
					+ insets.left
					+ insets.right,
				laFrmTitlePackageReportRPR003.getHeight()
					+ insets.top
					+ insets.bottom);
			// defect 7590
			// changed setVisible to setVisibleRTS
			laFrmTitlePackageReportRPR003.setVisibleRTS(true);
			// end defect 7590
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information to the
	 * view
	 *
	 * @param aaDataObject Object 
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			RTSDate laCurrDate = RTSDate.getCurrentDate();
			getdateField().setDate(laCurrDate.add(RTSDate.DATE, -1));
			getdateField().requestFocus();
			// defect 8628 
			//	Vector lvWkIds =
			//		AssignedWorkstationIdsCache.getAsgndWsIds(
			//			SystemProperty.getOfficeIssuanceNo(),
			//			SystemProperty.getSubStationId());
			Vector lvWkIds =
				AssignedWorkstationIdsCache.getTtlPkgWsIds(
					SystemProperty.getOfficeIssuanceNo(),
					SystemProperty.getSubStationId());
			// end defect 8628 

			caWkIdsTableModel.add(lvWkIds);

			// defect 10013 
			if (lvWkIds.size() == 1)
			{
				getchkSelectAllWorkstations().doClick();
				getchkSelectAllWorkstations().setEnabled(false);
			}
			// end defect 10013 
		}
		catch (Exception aeEx)
		{
			RTSException leRTSEx =
				new RTSException(
					RTSException.JAVA_ERROR,
					(Exception) aeEx);
			leRTSEx.displayError(this);
		}
	}

	/** 
	 * Called whenever the value of the selection changes.
	 *
	 * @param aaLSE ListSelectionEvent
	 */
	public void valueChanged(ListSelectionEvent aaLSE)
	{
		if (aaLSE.getValueIsAdjusting())
		{
			return;
		}
		// defect 10013 
		if (getScrollPaneTable().getRowCount() == 1)
		{
			getchkSelectAllWorkstations().setSelected(true);
			getScrollPaneTable().setSelectedRow(0);
		}
		else
		{
			Vector lvSelectedRows =
				new Vector(ivjScrollPaneTable.getSelectedRowNumbers());

			if (lvSelectedRows.size()
				== ivjScrollPaneTable.getRowCount())
			{
				getchkSelectAllWorkstations().setSelected(true);
			}
			else
			{
				getchkSelectAllWorkstations().setSelected(false);
			}
		}
		// end defect 10013  
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="57,10"