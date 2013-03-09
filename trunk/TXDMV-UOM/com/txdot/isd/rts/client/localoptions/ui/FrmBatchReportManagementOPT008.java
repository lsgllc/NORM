package com.txdot.isd.rts.client.localoptions.ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.desktop.RTSApplicationController;
import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.BatchReportManagementData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmBatchReportManagementtOPT008.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	01/14/2011	Created
 * 							defect 10701 Ver 6.7.0 
 * ---------------------------------------------------------------------
 */

/**
 * Frame for Management of Batch Report Printing
 *
 * @version	6.7.0			01/14/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		01/14/2011
 */
public class FrmBatchReportManagementOPT008
	extends RTSDialogBox
	implements ActionListener, WindowListener, ListSelectionListener
{

	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnHelp = null;
	private RTSButton ivjbtnRevise = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private RTSTable ivjScrollPaneTable = null;
	private JScrollPane ivjtblBatchRpt = null;
	private TMOPT008 caBatchReportTableModel = null;
	private RTSButtonGroup caRTSButtonGroup = new RTSButtonGroup();

	private Vector cvTblData = null;

	// Constants 
	private final static String YES = "Yes";
	private final static String NO = "No";

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 */
	public FrmBatchReportManagementOPT008()
	{
		super();
		initialize();
	}

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmBatchReportManagementOPT008(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 */
	public FrmBatchReportManagementOPT008(
		Dialog aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmBatchReportManagementOPT008(
		Dialog aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmBatchReportManagementOPT008(
		Dialog aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 * 
	 * @param aaOwner Frame
	 */
	public FrmBatchReportManagementOPT008(Frame aaOwner)
	{
		super(aaOwner);
	}

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 * 
	 * @param aaOwner Frame
	 * @param asTitle String
	 */
	public FrmBatchReportManagementOPT008(
		Frame aaOwner,
		String asTitle)
	{
		super(aaOwner, asTitle);
	}

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param asTitle String
	 * @param abModal boolean
	 */
	public FrmBatchReportManagementOPT008(
		Frame aaOwner,
		String asTitle,
		boolean abModal)
	{
		super(aaOwner, asTitle, abModal);
	}

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 * @param abModal boolean
	 */
	public FrmBatchReportManagementOPT008(
		Frame aaOwner,
		boolean abModal)
	{
		super(aaOwner, abModal);
	}

	/**
	 * FrmBatchReportManagementOPT008 constructor comment.
	 * 
	 * @param aaOwner JFrame
	 */
	public FrmBatchReportManagementOPT008(JFrame aaOwner)
	{
		super(aaOwner);
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
			if (aaAE.getSource() == getbtnRevise())
			{
				int liSel = getScrollPaneTable().getSelectedRow();

				BatchReportManagementData laData =
					(BatchReportManagementData) cvTblData.get(liSel);

				BatchReportManagementData laNewData =
					(BatchReportManagementData) UtilityMethods.copy(
						laData);

				// AutoPrntIndi: 0 -> 1; 1-> 0
				int liNewValue = (laData.getAutoPrntIndi() + 1) % 2;
				RTSDate laRTSDate = new RTSDate();
				laNewData.setAutoPrntIndi(liNewValue);
				laNewData.setChngTimestmp(laRTSDate);
				Vector lvData = new Vector();
				lvData.add(laNewData);
				lvData.add(getAdminLogData(laNewData));
				getController().processData(
					VCBatchReportManagementOPT008.REVISE,
					lvData);

				if (RTSApplicationController.isDBReady())
				{
					laData.setAutoPrntIndi(liNewValue);
					laData.setChngTimestmp(laRTSDate);
				}

				getScrollPaneTable().repaint();

			}
			else if (aaAE.getSource() == getbtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					getController().getData());
			}
			else if (aaAE.getSource() == getbtnHelp())
			{
				
				RTSException leRTSEx =
					new RTSException(
						RTSException.WARNING_MESSAGE,
						ErrorsConstant.ERR_HELP_IS_NOT_AVAILABLE,
						"Information");
				leRTSEx.displayError(this);
			}
		}
		finally
		{
			doneWorking();
		}
	}

	/**
	 * Return populated AdminLogData
	 * 
	 * @param aaData 
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData(BatchReportManagementData aaData)
	{
		AdministrationLogData laAdminlogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		laAdminlogData.setAction("Update");
		laAdminlogData.setEntity("BatchRptMgmt");
		String lsEntityValue =
			""
				+ aaData.getRptNumber()
				+ " "
				+ "AutoPrint: "
				+ (aaData.isAutoPrnt() ? YES : NO);
		laAdminlogData.setEntityValue(lsEntityValue);
		return laAdminlogData;
	}

	/**
	 * Return the btnCancel property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setName("btnCancel");
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setBounds(260, 413, 91, 25);
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the btnHelp property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp = new RTSButton();
				ivjbtnHelp.setName("btnHelp");
				ivjbtnHelp.setMnemonic('H');
				ivjbtnHelp.setText(CommonConstant.BTN_TXT_HELP);
				ivjbtnHelp.setBounds(445, 413, 77, 25);
				// user code begin {1}
				ivjbtnHelp.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnHelp;
	}

	/**
	 * Return the btnRevise property value.
	 * 
	 * @return RTSButton
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSButton getbtnRevise()
	{
		if (ivjbtnRevise == null)
		{
			try
			{
				ivjbtnRevise = new RTSButton();
				ivjbtnRevise.setName("btnRevise");
				ivjbtnRevise.setMnemonic('R');
				ivjbtnRevise.setText(CommonConstant.BTN_TXT_REVISE);
				ivjbtnRevise.setBounds(92, 413, 91, 25);
				// user code begin {1}
				ivjbtnRevise.addActionListener(this);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjbtnRevise;
	}

	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return  JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"RTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				getRTSDialogBoxContentPane().add(
					gettblBatchRpt(),
					gettblBatchRpt().getName());
				getRTSDialogBoxContentPane().add(
					getbtnRevise(),
					getbtnRevise().getName());
				getRTSDialogBoxContentPane().add(
					getbtnCancel(),
					getbtnCancel().getName());
				getRTSDialogBoxContentPane().add(
					getbtnHelp(),
					getbtnHelp().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
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
				ivjScrollPaneTable =
					new com.txdot.isd.rts.client.general.ui.RTSTable();
				ivjScrollPaneTable.setName("ScrollPaneTable");
				gettblBatchRpt().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				gettblBatchRpt().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				//				ivjScrollPaneTable.setAutoResizeMode(
				//					javax.swing.JTable.AUTO_RESIZE_OFF);
				ivjScrollPaneTable.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);

				ivjScrollPaneTable.setModel(new TMOPT008());
				ivjScrollPaneTable.setOpaque(true);
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				ivjScrollPaneTable.setGridColor(java.awt.Color.white);
				ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
				ivjScrollPaneTable.setIntercellSpacing(
					new Dimension(0, 0));
				ivjScrollPaneTable.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caBatchReportTableModel =
					(TMOPT008) ivjScrollPaneTable.getModel();
				TableColumn a =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(0));
				a.setPreferredWidth(100);
				TableColumn b =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(1));
				b.setPreferredWidth(255);
				TableColumn c =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(2));
				c.setPreferredWidth(160);
				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.SINGLE_SELECTION);
				ivjScrollPaneTable.init();
				ivjScrollPaneTable.addActionListener(this);
				ivjScrollPaneTable
					.getSelectionModel()
					.addListSelectionListener(
					this);
				//				a.setCellRenderer(
				//					ivjScrollPaneTable.setColumnAlignment(
				//						RTSTable.CENTER));
				ivjScrollPaneTable.setBackground(Color.white);
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjScrollPaneTable;
	}

	/**
	 * Return the ivjtblBatchRpt property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane gettblBatchRpt()
	{
		if (ivjtblBatchRpt == null)
		{
			try
			{
				ivjtblBatchRpt = new JScrollPane();
				ivjtblBatchRpt.setName("ivjtblBatchRpt");
				ivjtblBatchRpt.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjtblBatchRpt.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				ivjtblBatchRpt.setBackground(java.awt.Color.white);
				ivjtblBatchRpt.setBounds(24, 43, 600, 352);
				gettblBatchRpt().setViewportView(getScrollPaneTable());
				// user code begin {1}
				// user code end
			}
			catch (Throwable leIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(leIVJEx);
			}
		}
		return ivjtblBatchRpt;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		//defect 7891
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
		leRTSEx.displayError(this);
		//end defect 7891
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
			addWindowListener(this);
			// user code end
			setName(ScreenConstant.OPT008_FRAME_NAME);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(640, 480);
			setTitle(ScreenConstant.OPT008_FRAME_TITLE);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable leIVJEx)
		{
			handleException(leIVJEx);
		}
		// user code begin {2}

		caRTSButtonGroup.add(getbtnRevise());
		caRTSButtonGroup.add(getbtnCancel());
		caRTSButtonGroup.add(getbtnHelp());

		getScrollPaneTable().requestFocus();
		// user code end
	}

	/**
	 * All subclasses must implement this method - it sets the data on 
	 * the screen and is how the controller relays information
	 * to the view.
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject != null)
		{
			Vector lvData = (Vector) aaDataObject;
			cvTblData = lvData;
			caBatchReportTableModel.add(cvTblData);
			if (cvTblData != null && cvTblData.size() > 0)
			{
				getScrollPaneTable().setRowSelectionInterval(0, 0);
			}
		}
	}

	/** 
	  * Called whenever the value of the selection changes.
	  * 
	  * @param aeLSE ListSelectionEvent
	  */
	public void valueChanged(ListSelectionEvent aeLSE)
	{
	}

	/**
	 * Invoked when the window is set to be the user's
	 * active window, which means the window (or one of its
	 * subcomponents) will receive keyboard events.
	 * 
	 * @param aeWE WindowEvent  
	 */
	public void windowActivated(WindowEvent aeWE)
	{
		if (cvTblData == null)
		{
			BatchReportManagementData laData =
				new BatchReportManagementData();
			laData.setOfcIssuanceNo(
				SystemProperty.getOfficeIssuanceNo());
			laData.setSubstaId(SystemProperty.getSubStationId());
			getController().processData(
				AbstractViewController.SEARCH,
				laData);
		}
	}

	/**
	 * Invoked when a window has been closed as the result
	 * of calling dispose on the window.
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowClosed(WindowEvent aeWE)
	{
		//empty code block
	}

	/**
	 * Invoked when the user attempts to close the window
	 * from the window's system menu.  If the program does not 
	 * explicitly hide or dispose the window while processing 
	 * this event, the window close operation will be cancelled.
	 * 
		 * @param aeWE WindowEvent 
	 */
	public void windowClosing(java.awt.event.WindowEvent e)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is no longer the user's active
	 * window, which means that keyboard events will no longer
	 * be delivered to the window or its subcomponents.
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowDeactivated(WindowEvent aeWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is changed from a minimized
	 * to a normal state.
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowDeiconified(WindowEvent aeWE)
	{
		//empty code block
	}

	/**
	 * Invoked when a window is changed from a normal to a
	 * minimized state. For many platforms, a minimized window 
	 * is displayed as the icon specified in the window's 
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowIconified(WindowEvent aeWE)
	{
		//empty code block
	}

	/**
	 * Invoked the first time a window is made visible.
	 * 
	 * @param aeWE WindowEvent 
	 */
	public void windowOpened(WindowEvent aeWE)
	{
		//empty code block
	}

}
