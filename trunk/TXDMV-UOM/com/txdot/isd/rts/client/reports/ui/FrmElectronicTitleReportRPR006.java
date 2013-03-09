package com.txdot.isd.rts.client.reports.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.ElectronicTitleHistoryUIData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmElectronicTitleReportRPR006.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/26/2009	Created 
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	08/22/2009	Corrected typo in method name 
 * 							  (getstcLblBegintDate)
 * 							"Inherited" from RPR005. 
 * 							  (TODO Standardize/Implement "base" frame.)
 * 							Implement new AdminLogData constructor 
 * 							Implement validateData(), 
 * 							  setDataToDataObject().  Sorted methods.
 * 							add cvReportData  
 * 							add getstcLblBeginDate(), 
 * 							 validateData(), setDataToDataObject() 
 * 							delete getstcLblBegintDate()
 * 							modify actionPerformed(), setDataToDataObject() 
 * 							defect 8628 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * Frame RPR006 prompts for ETitle  Report/Export and county selections.  
 *
 * @version	Defect_POS_F	08/22/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		02/26/2009 
 */
public class FrmElectronicTitleReportRPR006
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener, KeyListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSelectAllCounties = null;
	private JPanel ivjFrmElectronicTitleReportRPR006ContentPane1 = null;
	private JPanel ivjpnlSelectAction = null;
	private JRadioButton ivjradioElectronicTitleExport = null;
	private JRadioButton ivjradioElectronicTitleReport = null;
	private RTSTable ivjScrollPaneTable = null;
	private JLabel ivjstcLblBeginDate = null;
	private JLabel ivjstcLblEndDate = null;
	private JScrollPane ivjtblRegionalCnty = null;
	private RTSDateField ivjtxtBeginDate = null;
	private RTSDateField ivjtxtEndDate = null;
	private JPanel ivjJPanelRadioButtonGroup = null;

	private TMRPR005 caRegionalCntyTableModel = null;

	// String
	private static final String ADMIN_LOG_ENTITY = "Electronic Title";
	private static final String TITLE =
		"Electronic Title Report    RPR006";

	// Vector
	private Vector cvOfcIds = new Vector();

	// defect 8628 
	private Vector cvReportData = new Vector();
	// end defect 8628 

	/**
	 * main used to start class as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(java.lang.String[] args)
	{
		try
		{
			FrmElectronicTitleReportRPR006 laFrame;
			laFrame = new FrmElectronicTitleReportRPR006();
			laFrame.setModal(true);
			laFrame
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrame.show();
			java.awt.Insets insets = laFrame.getInsets();
			laFrame.setSize(
				laFrame.getWidth() + insets.left + insets.right,
				laFrame.getHeight() + insets.top + insets.bottom);
			laFrame.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace();
		}
	}

	/**
	 * FrmElectronicTitleReportRPR006 constructor comment.
	 */
	public FrmElectronicTitleReportRPR006()
	{
		super();
		initialize();
	}

	/**
	 * FrmElectronicTitleReportRPR006 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmElectronicTitleReportRPR006(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmElectronicTitleReportRPR006 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmElectronicTitleReportRPR006(JFrame aaParent)
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
			clearAllColor(this);

			// ENTER 
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 8628 
				if (!validateData())
				{
					return;
				}

				setDataToDataObject();

				getController().processData(
					AbstractViewController.ENTER,
					cvReportData);

				// end defect 8628 
			}

			// SelectAllItem(s) checkbox
			else if (aaAE.getSource() == getchkSelectAllCounties())
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
			// CANCEL 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{

				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			// HELP 
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
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
				ivjButtonPanel1.setBounds(33, 320, 262, 64);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
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
				ivjchkSelectAllCounties.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				ivjchkSelectAllCounties.setText(
					CommonConstant.TXT_SELECT_ALL);
				ivjchkSelectAllCounties.setMaximumSize(
					new java.awt.Dimension(129, 22));
				ivjchkSelectAllCounties.setActionCommand(
					CommonConstant.TXT_SELECT_ALL);
				ivjchkSelectAllCounties.setBounds(15, 106, 123, 22);
				ivjchkSelectAllCounties.setMinimumSize(
					new java.awt.Dimension(129, 22));
				// user code begin {1}
				ivjchkSelectAllCounties.addActionListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSelectAllCounties;
	}

	/**
	 * Return the FrmExemptAuditReportRPR005ContentPane1 property
	 * value.
	 * 
	 * @return JPanel
	 */

	private JPanel getFrmExemptAuditReportRPR005ContentPane1()
	{
		if (ivjFrmElectronicTitleReportRPR006ContentPane1 == null)
		{
			try
			{
				ivjFrmElectronicTitleReportRPR006ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmElectronicTitleReportRPR006ContentPane1.setName(
					"FrmExemptAuditReportRPR005ContentPane1");
				ivjFrmElectronicTitleReportRPR006ContentPane1
					.setLayout(
					null);
				ivjFrmElectronicTitleReportRPR006ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmElectronicTitleReportRPR006ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmElectronicTitleReportRPR006ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				getFrmExemptAuditReportRPR005ContentPane1().add(
					getpnlSelectAction(),
					getpnlSelectAction().getName());
				getFrmExemptAuditReportRPR005ContentPane1().add(
					getchkSelectAllCounties(),
					getchkSelectAllCounties().getName());
				getFrmExemptAuditReportRPR005ContentPane1().add(
					gettblRegionalCnty(),
					gettblRegionalCnty().getName());
				getFrmExemptAuditReportRPR005ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjFrmElectronicTitleReportRPR006ContentPane1;
	}

	/**
	 * This method initializes jPanelRadioButtonGroup
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanelRadioButtonGroup()
	{
		if (ivjJPanelRadioButtonGroup == null)
		{
			ivjJPanelRadioButtonGroup = new javax.swing.JPanel();
			ivjJPanelRadioButtonGroup.setLayout(null);
			ivjJPanelRadioButtonGroup.add(
				getradioElectronicTitleReport(),
				null);
			ivjJPanelRadioButtonGroup.add(
				getradioElectronicTitleExport(),
				null);
			ivjJPanelRadioButtonGroup.setBounds(5, 22, 78, 57);
		}
		return ivjJPanelRadioButtonGroup;
	}

	/** 
	 * 
	 * Panel to Select Action 
	 * 
	 * @return JPanel 
	 */
	private JPanel getpnlSelectAction()
	{
		if (ivjpnlSelectAction == null)
		{
			try
			{
				ivjpnlSelectAction = new JPanel();
				ivjpnlSelectAction.setName(
					CommonConstant.TXT_SELECT_ONE_COLON);
				ivjpnlSelectAction.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						CommonConstant.TXT_SELECT_ONE_COLON));
				ivjpnlSelectAction.setLayout(null);
				ivjpnlSelectAction.setMaximumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectAction.setMinimumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectAction.setBounds(18, 10, 293, 86);
				ivjpnlSelectAction.add(gettxtBeginDate(), null);
				ivjpnlSelectAction.add(gettxtEndDate(), null);
				ivjpnlSelectAction.add(getstcLblEndDate(), null);
				ivjpnlSelectAction.add(getstcLblBeginDate(), null);
				ivjpnlSelectAction.add(
					getJPanelRadioButtonGroup(),
					null);

				RTSButtonGroup laradioGrp = new RTSButtonGroup();
				laradioGrp.add(getradioElectronicTitleExport());
				laradioGrp.add(getradioElectronicTitleReport());
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectAction;
	}

	/**
	 * Return the ivjradioElectronicTitleExport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioElectronicTitleExport()
	{
		if (ivjradioElectronicTitleExport == null)
		{
			try
			{
				ivjradioElectronicTitleExport = new JRadioButton();
				ivjradioElectronicTitleExport.setBounds(4, 28, 62, 22);
				ivjradioElectronicTitleExport.setName(
					"ivjradioElectronicTitleExport");
				ivjradioElectronicTitleExport.setMnemonic(
					java.awt.event.KeyEvent.VK_X);
				ivjradioElectronicTitleExport.setText(
					CommonConstant.TXT_EXPORT);
				ivjradioElectronicTitleExport.setMaximumSize(
					new java.awt.Dimension(154, 22));
				ivjradioElectronicTitleExport.setActionCommand(
					CommonConstant.TXT_EXPORT);
				ivjradioElectronicTitleExport.setMinimumSize(
					new java.awt.Dimension(154, 22));
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioElectronicTitleExport;
	}

	/**
	 * Return the ivjradioElectronicTitleReport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioElectronicTitleReport()
	{
		if (ivjradioElectronicTitleReport == null)
		{
			try
			{
				ivjradioElectronicTitleReport = new JRadioButton();
				ivjradioElectronicTitleReport.setBounds(4, 0, 63, 22);
				ivjradioElectronicTitleReport.setName(
					"ivjradioElectronicTitleExport");
				ivjradioElectronicTitleReport.setMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjradioElectronicTitleReport.setText(
					CommonConstant.TXT_REPORT);
				ivjradioElectronicTitleReport.setMaximumSize(
					new java.awt.Dimension(145, 22));
				ivjradioElectronicTitleReport.setActionCommand(
					CommonConstant.TXT_REPORT);
				ivjradioElectronicTitleReport.setMinimumSize(
					new java.awt.Dimension(145, 22));
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioElectronicTitleReport;
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
				int liColWidthZero = 75, liColWidthOne = 75;
				if (!SystemProperty.isCounty())
				{
					liColWidthZero = 19;
					liColWidthOne = 131;
				}

				ivjScrollPaneTable = new RTSTable();
				ivjScrollPaneTable.setName("ScrollPaneTable");
				gettblRegionalCnty().setColumnHeaderView(
					ivjScrollPaneTable.getTableHeader());
				gettblRegionalCnty().getViewport().setScrollMode(
					JViewport.BLIT_SCROLL_MODE);
				//.BACKINGSTORE_SCROLL_MODE);  jrr - MEMORY
				ivjScrollPaneTable.setModel(new TMRPR005());
				ivjScrollPaneTable.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
				ivjScrollPaneTable.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjScrollPaneTable.setBounds(0, 0, 200, 200);
				// user code begin 
				caRegionalCntyTableModel =
					(TMRPR005) ivjScrollPaneTable.getModel();
				TableColumn laTCa =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(0));
				laTCa.setPreferredWidth(liColWidthZero);

				TableColumn laTCb =
					ivjScrollPaneTable.getColumn(
						ivjScrollPaneTable.getColumnName(1));
				laTCb.setPreferredWidth(liColWidthOne);

				ivjScrollPaneTable.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjScrollPaneTable.init();
				ivjScrollPaneTable.addActionListener(this);
				ivjScrollPaneTable.addMultipleSelectionListener(this);
				// user code end
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjScrollPaneTable;
	}

	/**
	 * Return the ivjstcLblBeginDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBeginDate()
	{
		if (ivjstcLblBeginDate == null)
		{
			try
			{
				ivjstcLblBeginDate = new JLabel();
				ivjstcLblBeginDate.setLabelFor(gettxtBeginDate());
				ivjstcLblBeginDate.setBounds(132, 22, 69, 22);
				ivjstcLblBeginDate.setName("stcLblBeginDate");
				ivjstcLblBeginDate.setText(
					InventoryConstant.TXT_BEGIN_DATE_COLON);
				ivjstcLblBeginDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_B);
				ivjstcLblBeginDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblBeginDate;
	}

	/**
	 * Return the stcLblEndDate property value.
	 * 
	 * @return JLabel
	 */

	private JLabel getstcLblEndDate()
	{
		if (ivjstcLblEndDate == null)
		{
			try
			{
				ivjstcLblEndDate = new JLabel();
				ivjstcLblEndDate.setLabelFor(gettxtEndDate());
				ivjstcLblEndDate.setBounds(141, 50, 60, 22);
				ivjstcLblEndDate.setName("stcLblEndDate");
				ivjstcLblEndDate.setText(
					InventoryConstant.TXT_END_DATE_COLON);
				ivjstcLblEndDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_N);
				ivjstcLblEndDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblEndDate;
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
				ivjtblRegionalCnty.setBounds(18, 139, 293, 164);
				gettblRegionalCnty().setViewportView(
					getScrollPaneTable());
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjtblRegionalCnty;
	}

	/**
	 * Return the txtBeginDate property value.
	 * 
	 * @return RTSDateField
	 */

	private RTSDateField gettxtBeginDate()
	{
		if (ivjtxtBeginDate == null)
		{
			try
			{
				ivjtxtBeginDate = new RTSDateField();
				ivjtxtBeginDate.setBounds(208, 23, 72, 20);
				ivjtxtBeginDate.setName("txtStartDate");
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjtxtBeginDate;
	}

	/**
	 * Return the txtEndDate property value.
	 * 
	 * @return RTSDateField
	 */

	private RTSDateField gettxtEndDate()
	{
		if (ivjtxtEndDate == null)
		{
			try
			{
				ivjtxtEndDate = new RTSDateField();
				ivjtxtEndDate.setBounds(208, 52, 72, 20);
				ivjtxtEndDate.setName("txtEndDate");
			}
			catch (Throwable aeIVJExc)
			{
				handleException(aeIVJExc);
			}
		}
		return ivjtxtEndDate;
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
			setName(TITLE);
			setSize(336, 393);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(TITLE);
			setContentPane(getFrmExemptAuditReportRPR005ContentPane1());
		}
		catch (java.lang.Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		addWindowListener(this);
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

		getradioElectronicTitleReport().requestFocus();
		getradioElectronicTitleReport().setSelected(true);
		gettxtEndDate().setDate(RTSDate.getCurrentDate());
		gettxtBeginDate().setDate(RTSDate.getCurrentDate());
		cvOfcIds =
			(Vector) UtilityMethods.copy(OfficeIdsCache.getOfcIds());
		UtilityMethods.sort(cvOfcIds);
		Vector lvRTSOfcIds = new Vector();
		lvRTSOfcIds.add(
			OfficeIdsCache.getOfcId(
				SystemProperty.getOfficeIssuanceNo()));

		if (!SystemProperty.isCounty())
		{
			lvRTSOfcIds.clear();
			for (int i = 0; i < cvOfcIds.size(); i++)
			{
				if (SystemProperty.isHQ())
				{
					if ((((OfficeIdsData) cvOfcIds.get(i))
						.getOfcIssuanceCd()
						== 2
						|| ((OfficeIdsData) cvOfcIds.get(i))
							.getOfcIssuanceCd()
							== 3
						|| ((OfficeIdsData) cvOfcIds.get(i))
							.getOfcIssuanceNo()
							== 291)
						&& (((OfficeIdsData) cvOfcIds.get(i))
							.getOfcIssuanceNo()
							!= 273)
						&& ((OfficeIdsData) cvOfcIds.get(i))
							.getOfcIssuanceNo()
							!= 257)
					{
						setRegName(i);
						lvRTSOfcIds.add((cvOfcIds.get(i)));
					}
				}
				else
				{
					if (((OfficeIdsData) cvOfcIds.get(i))
						.getRegnlOfcId()
						== SystemProperty.getOfficeIssuanceNo()
						|| ((OfficeIdsData) cvOfcIds.get(i))
							.getOfcIssuanceNo()
							== SystemProperty.getOfficeIssuanceNo())
					{
						setRegName(i);
						lvRTSOfcIds.add((cvOfcIds.get(i)));
					}
				}
			}
		}
		caRegionalCntyTableModel.add(lvRTSOfcIds);

		if (lvRTSOfcIds.size() == 1)
		{
			getchkSelectAllCounties().doClick();
			getchkSelectAllCounties().setEnabled(false);
		}
	}

	/** 
	 * 
	 * Set Data To Data Object
	 * 
	 */
	private void setDataToDataObject()
	{
		// Selected Offices 
		Vector lvSelctdRows =
			new Vector(getScrollPaneTable().getSelectedRowNumbers());

		Vector lvCountyNumber = new Vector();

		for (int i = 0;
			i < getScrollPaneTable().getSelectedRowNumbers().size();
			i++)
		{
			lvCountyNumber.add(
				(
					(TMRPR005) getScrollPaneTable()
						.getModel())
						.getValueAt(
					Integer.parseInt(
						lvSelctdRows.get(i)
							+ CommonConstant.STR_SPACE_EMPTY),
					0));
		}
		UtilityMethods.sort(lvCountyNumber);

		// Begin Date, End Date 
		RTSDate laBeginDate = gettxtBeginDate().getDate();
		RTSDate laEndDate = gettxtEndDate().getDate();

		ElectronicTitleHistoryUIData laSearchParms =
			new ElectronicTitleHistoryUIData();
		laSearchParms.setBeginDate(laBeginDate);
		laSearchParms.setEndDate(laEndDate);
		laSearchParms.setSelectedOffices(lvCountyNumber);

		// defect 8628 
		AdministrationLogData laAdminLogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 

		laAdminLogData.setEntity(ADMIN_LOG_ENTITY);
		laAdminLogData.setEntityValue(
			CommonConstant.STR_OPEN_PARENTHESES
				+ lvSelctdRows.size()
				+ CommonConstant.STR_CLOSE_PARENTHESES
				+ CommonConstant.STR_SPACE_ONE
				+ laBeginDate.toString().substring(0, 6)
				+ laBeginDate.toString().substring(8, 10)
				+ CommonConstant.STR_DASH
				+ laEndDate.toString().substring(0, 6)
				+ laEndDate.toString().substring(8, 10));
				
		cvReportData.add(laSearchParms);

		if (getradioElectronicTitleExport().isSelected())
		{
			laAdminLogData.setAction(CommonConstant.TXT_EXPORT);
		}
		else
		{
			laAdminLogData.setAction(CommonConstant.TXT_REPORT);
		}
		cvReportData.add(laAdminLogData);

	}

	/** 
	 * Called to substring Regional Office Name in cvOfcIds,
	 * removes "AL OFFICE" from string.
	 * 
	 * @param liIndex Reginal Office Id to modify.
	 */
	private void setRegName(int liIndex)
	{
		if (((OfficeIdsData) cvOfcIds.get(liIndex)).getOfcIssuanceCd()
			== 2)
		{
			int liStrLngth =
				(((OfficeIdsData) cvOfcIds.get(liIndex))
					.getOfcName()
					.length()
					- 9);
			((OfficeIdsData) cvOfcIds.get(liIndex)).setOfcName(
				((OfficeIdsData) cvOfcIds.get(liIndex))
					.getOfcName()
					.substring(
					0,
					liStrLngth));
		}
	}

	/**
	 * 
	 * Validate Data
	 * 
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;

		RTSException leEx = new RTSException();
		RTSDate laBeginDate = null;
		RTSDate laValidBeginDate = null;
		RTSDate laEndDate = null;
		RTSDate laValidEndDate = null;

		if (!gettxtBeginDate().isValidDate())
		{
			// 733 
			leEx.addException(
				new RTSException(ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
				gettxtBeginDate());
		}
		else
		{
			laBeginDate = gettxtBeginDate().getDate();
			laValidBeginDate =
				RTSDate.getCurrentDate().add(RTSDate.DATE, -399);
		}
		if (!gettxtEndDate().isValidDate())
		{
			leEx.addException(
				new RTSException(ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
				gettxtEndDate());
		}
		else
		{
			if (laBeginDate != null)
			{
				laEndDate = gettxtEndDate().getDate();
				laValidEndDate = laBeginDate.add(RTSDate.DATE, 30);
			}
		}
		if (laBeginDate != null && laEndDate != null)
		{
			boolean lbBeginDateError = false;
			if (laBeginDate.compareTo(laValidBeginDate) == -1
				|| laBeginDate.compareTo(RTSDate.getCurrentDate()) == 1)
			{
				lbBeginDateError = true;
				// 736 
				leEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_HIST_DATE_INVALID),
					gettxtBeginDate());
			}
			if (!lbBeginDateError)
			{
				if (laEndDate.compareTo(laValidEndDate) == 1)
				{
					// 739
					leEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_DATE_RANGE_INVALID),
						gettxtEndDate());

				}
				if (laEndDate.compareTo(RTSDate.getCurrentDate()) == 1
					|| laEndDate.compareTo(laBeginDate) == -1)
				{
					leEx.addException(
						new RTSException(
							ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
						gettxtEndDate());
				}
			}
		}

		// Verify that at least one row has been selected
		if (getScrollPaneTable().getSelectedRowNumbers().isEmpty())
		{
			leEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_MUST_SELECT_A_COUNTY),
				getScrollPaneTable());
		}

		if (leEx.isValidationError())
		{
			leEx.displayError(this);
			leEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
	}

	/** 
	 * Called whenever the value of the selection changes.
	 * 
	 * @param aaLSE the event that characterizes the change.
	 */
	public void valueChanged(ListSelectionEvent aaLSE)
	{

		if (aaLSE.getValueIsAdjusting())
		{
			return;
		}

		Vector lvSelctdRows =
			new Vector(getScrollPaneTable().getSelectedRowNumbers());

		if (getScrollPaneTable().getRowCount() == 1)
		{
			getchkSelectAllCounties().setSelected(true);
			getScrollPaneTable().setSelectedRow(0);
			return;
		}

		if (lvSelctdRows.size() == getScrollPaneTable().getRowCount())
		{
			getchkSelectAllCounties().setSelected(true);
		}
		else
		{
			getchkSelectAllCounties().setSelected(false);
		}
	}
}
