package com.txdot.isd.rts.client.inventory.ui;

import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.InventoryHistoryLogData;
import com.txdot.isd.rts.services.data.InventoryHistoryUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmInventoryHistoryCountyINV015.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * C. Walker	03/26/2002	Added comments and code to make frames
 *							modal to each other
 * MAbs			05/21/2002	Unselecting "Select All" when mouse clicked
 * B Arredondo	12/03/2002	Made changes to user help guide so had to 
 * 							make changes in actionPerformed().
 * B Arredondo	12/16/2002	Made changes for the user help guide so 
 * 							had to make changes
 *							modify actionPerformed().
 *							defect 5147
 * Ray Rowehl	02/19/2005	RTS 5.2.3 Code Clean up
 *							organize imports, format source,
 *							rename fields
 *							removed WindowListener, it was not used.
 *							modify handleException()
 *							defect 7890 Ver 5.2.3
 * Ray Rowehl	03/30/2005	Remove setNextFocusable's
 * 							defect 7890 Ver 5.2.3
 * K Harrell	05/19/2005	InventoryHistoryUIData field renaming
 * 							defect 7899 Ver 5.2.3 
 * Ray Rowehl	08/09/2005	Cleanup pass.
 * 							Add white space between methods.
 * 							Remove key processing from button panel.
 * 							Work on constants.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/13/2005	Move constants to appropriate constants
 * 							classes.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	08/17/2005	Remove select from key processing.
 * 							defect 7890 Ver 5.2.3
 * Ray Rowehl	10/05/2005	Update Mnemonics
 * 							defect 7890 Ver 5.2.3
 * T Pederson	10/28/2005	Comment out space bar code in keyPressed() 
 * 							method. Code in valueChanged handles this.
 * 							defect 7890 Ver 5.2.3
 * Jeff S.		12/20/2005	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), keyReleased(), 
 * 								cbKeyPressed
 * 							modify getpnlSelectReport(), 
 * 								getradioReceiveHistoryReport(),
 * 								getradioDeleteHistoryReport(),
 * 							defect 7890 Ver 5.2.3
 * K Harrell	10/12/2009	Cleanup/Standardize
 * 							delete getBuilderData()   
 * 							modify getchkSelectAllCounties(),
 * 							 getScrollPaneTable(), setData(), 
 * 							 actionPerformed(), valueChanged() 
 * 							defect 10207 Ver Defect_POS_G  
 * ---------------------------------------------------------------------
 */

/**
 * Frame INV015 prompts for what type of history report to run and 
 * for which counties in the Inventory History event.
 *
 * @version	Defect_POS_G 	10/12/2009
 * @author	Sunil Govindappa
 * <br>Creation Date:		06/27/2001 19:04:23
 */

public class FrmInventoryHistoryCountyINV015
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener, KeyListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSelectAllCounties = null;
	private JPanel ivjpnlSelectReport = null;
	private JRadioButton ivjradioDeleteHistoryReport = null;
	private JRadioButton ivjradioReceiveHistoryReport = null;
	private JPanel ivjFrmInventoryHistoryCountyINV015ContentPane1 =
		null;
	private JScrollPane ivjtblRegionalCnty = null;
	private RTSTable ivjScrollPaneTable = null;
	private TMINV015 caRegionalCntyTableModel = null;

	// Object 
	InventoryHistoryLogData caInvHistoryLogData = null;
	InventoryHistoryUIData caInvHisUIData = null;

	// Vector 
	Vector cvInventoryHistoryLogData = null;

	/**
	 * FrmInventoryHistoryCountyINV015 constructor comment.
	 */
	public FrmInventoryHistoryCountyINV015()
	{
		super();
		initialize();
	}

	/**
	 * FrmInventoryHistoryCountyINV015 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInventoryHistoryCountyINV015(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInventoryHistoryCountyINV015 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInventoryHistoryCountyINV015(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * This method is called when a event happens
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
			// Select All Checkbox 
			if (aaAE.getSource() == getchkSelectAllCounties())
			{
				if (getchkSelectAllCounties().isSelected())
				{
					getScrollPaneTable().selectAllRows(
						getScrollPaneTable().getRowCount());
				}
				else
				{
					getScrollPaneTable().unselectAllRows();
				}
			}
			// Cancel 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// Enter 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 10207 
				// Implement single return 
				if (captureUserInput())
				{
					InventoryHistoryUIData laInvHisUIData =
						(InventoryHistoryUIData) UtilityMethods.copy(
							caInvHisUIData);

					getController().processData(
						AbstractViewController.ENTER,
						laInvHisUIData);
				}
				// end defect 10207 
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.INV015);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Capture the User's Input
	 * 
	 * @return boolean 
	 */
	private boolean captureUserInput()
	{
		// defect 10207
		// Implement single return  
		boolean lbValid = true;

		caInvHisUIData = new InventoryHistoryUIData();

		Vector lvSelectedCounties =
			caInvHisUIData.getSelectedCounties();
		Vector lvSelctdRows =
			new Vector(getScrollPaneTable().getSelectedRowNumbers());

		// Verify that at least one row has been selected
		if (lvSelctdRows.size() < CommonConstant.ELEMENT_1)
		{
			RTSException leRTSEx =
				new RTSException(
					ErrorsConstant.ERR_NUM_MUST_SELECT_A_COUNTY);
			leRTSEx.displayError(this);
			ivjScrollPaneTable.requestFocus();
			lbValid = false;
		}
		else
		{
			UtilityMethods.sort(lvSelctdRows);

			for (int i = 0; i < lvSelctdRows.size(); i++)
			{
				int liIndx = ((Integer) lvSelctdRows.get(i)).intValue();
				InventoryHistoryLogData laData =
					(
						InventoryHistoryLogData) cvInventoryHistoryLogData
							.elementAt(
						liIndx);

				lvSelectedCounties.add(
					new Integer(laData.getOfcIssuanceNo()));
			}

			if (getradioDeleteHistoryReport().isSelected())
			{
				caInvHisUIData.setDeleteHisReportIndi(1);
			}
			else
			{
				caInvHisUIData.setReceiveHisReportIndi(1);
			}
		}
		return lbValid;
		// end defect 10207 
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
				ivjButtonPanel1.setBounds(102, 334, 262, 46);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
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
	 * Return the chkSelectAllCounties property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSelectAllCounties()
	{
		if (ivjchkSelectAllCounties == null)
		{
			try
			{
				ivjchkSelectAllCounties = new JCheckBox();
				ivjchkSelectAllCounties.setName("chkSelectAllCounties");

				ivjchkSelectAllCounties.setMnemonic(KeyEvent.VK_S);

				// defect 10207  
				ivjchkSelectAllCounties.setText(
				//InventoryConstant.TXT_SELECT_ALL_COUNTIES);
				CommonConstant.TXT_SELECT_ALL);
				ivjchkSelectAllCounties.setActionCommand(
					CommonConstant.TXT_SELECT_ALL);
				// end defect 10207

				ivjchkSelectAllCounties.setMaximumSize(
					new java.awt.Dimension(129, 22));
				ivjchkSelectAllCounties.setBounds(34, 108, 129, 22);
				ivjchkSelectAllCounties.setMinimumSize(
					new java.awt.Dimension(129, 22));
				// user code begin {1}
				ivjchkSelectAllCounties.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSelectAllCounties;
	}

	/**
	 * Return the FrmInventoryHistoryCountyINV015ContentPane1 property
	 * value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmInventoryHistoryCountyINV015ContentPane1()
	{
		if (ivjFrmInventoryHistoryCountyINV015ContentPane1 == null)
		{
			try
			{
				ivjFrmInventoryHistoryCountyINV015ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmInventoryHistoryCountyINV015ContentPane1.setName(
					"FrmInventoryHistoryCountyINV015ContentPane1");
				ivjFrmInventoryHistoryCountyINV015ContentPane1
					.setLayout(
					null);
				ivjFrmInventoryHistoryCountyINV015ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmInventoryHistoryCountyINV015ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmInventoryHistoryCountyINV015ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				getFrmInventoryHistoryCountyINV015ContentPane1().add(
					getpnlSelectReport(),
					getpnlSelectReport().getName());
				getFrmInventoryHistoryCountyINV015ContentPane1().add(
					getchkSelectAllCounties(),
					getchkSelectAllCounties().getName());
				getFrmInventoryHistoryCountyINV015ContentPane1().add(
					gettblRegionalCnty(),
					gettblRegionalCnty().getName());
				getFrmInventoryHistoryCountyINV015ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
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
		return ivjFrmInventoryHistoryCountyINV015ContentPane1;
	}

	/**
	 * Return the ivjpnlSelectReport property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getpnlSelectReport()
	{
		if (ivjpnlSelectReport == null)
		{
			try
			{
				ivjpnlSelectReport = new JPanel();
				ivjpnlSelectReport.setName("ivjpnlSelectReport");
				ivjpnlSelectReport.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_SELECT_REPORT_COLON));
				ivjpnlSelectReport.setLayout(null);
				ivjpnlSelectReport.setMaximumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setMinimumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setBounds(105, 10, 245, 86);
				getpnlSelectReport().add(
					getradioDeleteHistoryReport(),
					getradioDeleteHistoryReport().getName());
				getpnlSelectReport().add(
					getradioReceiveHistoryReport(),
					getradioReceiveHistoryReport().getName());
				// user code begin {1}
				RTSButtonGroup laRadioGrp = new RTSButtonGroup();
				laRadioGrp.add(getradioReceiveHistoryReport());
				laRadioGrp.add(getradioDeleteHistoryReport());
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectReport;
	}

	/**
	 * Return the ivjradioDeleteHistoryReport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioDeleteHistoryReport()
	{
		if (ivjradioDeleteHistoryReport == null)
		{
			try
			{
				ivjradioDeleteHistoryReport = new JRadioButton();
				ivjradioDeleteHistoryReport.setName(
					"ivjradioDeleteHistoryReport");
				ivjradioDeleteHistoryReport.setMnemonic(KeyEvent.VK_D);
				ivjradioDeleteHistoryReport.setText(
					InventoryConstant.TXT_DELETE_HIST_RPT);
				ivjradioDeleteHistoryReport.setMaximumSize(
					new java.awt.Dimension(145, 22));
				ivjradioDeleteHistoryReport.setActionCommand(
					InventoryConstant.TXT_DELETE_HIST_RPT);
				ivjradioDeleteHistoryReport.setBounds(30, 22, 176, 22);
				ivjradioDeleteHistoryReport.setMinimumSize(
					new java.awt.Dimension(145, 22));
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
		return ivjradioDeleteHistoryReport;
	}

	/**
	 * Return the ivjradioReceiveHistoryReport property value.
	 * 
	 * @return JRadioButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JRadioButton getradioReceiveHistoryReport()
	{
		if (ivjradioReceiveHistoryReport == null)
		{
			try
			{
				ivjradioReceiveHistoryReport = new JRadioButton();
				ivjradioReceiveHistoryReport.setName(
					"ivjradioReceiveHistoryReport");
				ivjradioReceiveHistoryReport.setMnemonic(KeyEvent.VK_R);
				ivjradioReceiveHistoryReport.setText(
					InventoryConstant.TXT_RECEIVE_HIST_RPT);
				ivjradioReceiveHistoryReport.setMaximumSize(
					new java.awt.Dimension(154, 22));
				ivjradioReceiveHistoryReport.setActionCommand(
					InventoryConstant.TXT_RECEIVE_HIST_RPT);
				ivjradioReceiveHistoryReport.setBounds(30, 50, 176, 22);
				ivjradioReceiveHistoryReport.setMinimumSize(
					new java.awt.Dimension(154, 22));
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
		return ivjradioReceiveHistoryReport;
	}

	/**
	 * Return the ScrollPaneTable property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable getScrollPaneTable()
	{
		if (ivjScrollPaneTable == null)
		{
			try
			{
				ivjScrollPaneTable = new RTSTable();
				ivjScrollPaneTable.setName("ScrollPaneTable");
				gettblRegionalCnty().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				gettblRegionalCnty().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjScrollPaneTable.setModel(new TMINV015());
				ivjScrollPaneTable.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
				ivjScrollPaneTable.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjScrollPaneTable.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caRegionalCntyTableModel =
					(TMINV015) ivjScrollPaneTable.getModel();
				// defect 10207
				// Use Inventory Constants for Column Numbers
				TableColumn laTCa =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(
							InventoryConstant.INV015_COL_OFFICE_NO));
				laTCa.setPreferredWidth(45);
				TableColumn laTCb =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(
							InventoryConstant.INV015_COL_OFFICE_NAME));
				laTCb.setPreferredWidth(105);
				TableColumn laTCc =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(
							InventoryConstant
								.INV015_COL_LAST_ACTIVITY));
				laTCc.setPreferredWidth(15);
				// end defect 10207
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjScrollPaneTable.init();
				ivjScrollPaneTable.addActionListener(this);
				ivjScrollPaneTable.addMultipleSelectionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjScrollPaneTable;
	}

	/**
	 * Return the regionalCntyTbl property value.
	 * 
	 * @return JScrollPane
	 */
	private JScrollPane gettblRegionalCnty()
	{
		if (ivjtblRegionalCnty == null)
		{
			try
			{
				ivjtblRegionalCnty = new JScrollPane();
				ivjtblRegionalCnty.setName("tblRegionalCnty");
				ivjtblRegionalCnty.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjtblRegionalCnty.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjtblRegionalCnty.setBounds(34, 139, 380, 182);
				gettblRegionalCnty().setViewportView(
					getScrollPaneTable());
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
		return ivjtblRegionalCnty;
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
			setName(ScreenConstant.INV015_FRAME_NAME);
			setSize(453, 414);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(ScreenConstant.INV015_FRAME_TITLE);
			setContentPane(
				getFrmInventoryHistoryCountyINV015ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		addWindowListener(this);
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
			FrmInventoryHistoryCountyINV015 laFrmInventoryHistoryCountyINV015;
			laFrmInventoryHistoryCountyINV015 =
				new FrmInventoryHistoryCountyINV015();
			laFrmInventoryHistoryCountyINV015.setModal(true);
			laFrmInventoryHistoryCountyINV015
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmInventoryHistoryCountyINV015.show();
			java.awt.Insets insets =
				laFrmInventoryHistoryCountyINV015.getInsets();
			laFrmInventoryHistoryCountyINV015.setSize(
				laFrmInventoryHistoryCountyINV015.getWidth()
					+ insets.left
					+ insets.right,
				laFrmInventoryHistoryCountyINV015.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmInventoryHistoryCountyINV015.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace();
		}
	}

	/**
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		// defect 10207 
		// Consolidate 
		if (aaData != null && aaData instanceof Vector)
		{
			cvInventoryHistoryLogData = (Vector) aaData;
			caRegionalCntyTableModel.add(cvInventoryHistoryLogData);
		}
		// end defect 10207 
	}

	/** 
	 * Called whenever the value of the selection changes.
	 * 
	 * @param aaLSE the event that characterizes the change.
	 */
	public void valueChanged(ListSelectionEvent aaLSE)
	{
		// defect 10207 
		// Consolidate 
		if (!aaLSE.getValueIsAdjusting())
		{
			Vector lvSelctdRows =
				new Vector(
					getScrollPaneTable().getSelectedRowNumbers());

			getchkSelectAllCounties().setSelected(
				lvSelctdRows.size()
					== getScrollPaneTable().getRowCount());
		}
		// end defect 10207 
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aaWE WindowEvent
	 */
	public void windowActivated(WindowEvent aaWE)
	{
		if (caInvHistoryLogData == null)
		{
			ivjradioDeleteHistoryReport.setSelected(true);
			caInvHistoryLogData = new InventoryHistoryLogData();
			int liOfficeIssuanceNo =
				SystemProperty.getOfficeIssuanceNo();
			caInvHistoryLogData.setOfcIssuanceNo(liOfficeIssuanceNo);
			getController().processData(
				VCInventoryHistoryCountyINV015.GET_COUNTIES,
				caInvHistoryLogData);
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
