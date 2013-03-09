package com.txdot.isd.rts.client.reports.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.*;
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
import com.txdot.isd.rts.services.data.ExemptAuditUIData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmExemptAuditReportRPR005.java
 *
 * (c) Texas Department of Transportation 2006
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * J Ralph		09/29/2006	New Class - Exempts
 * J Ralph		11/20/2006	Changed Date validation messages	
 * 							modify actionPerformed()
 * 							defect 9020 Ver Exempts
 * K Harrell	03/09/2007	Removed try block when using 
 * 							UtilityMethod.isRegion(),isHQ()
 * 							Use SystemProperty.isCounty, isHQ()
 * 							modify setData() 
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/04/2007	Exclude 257 from Combo Box 
 * 							modify setData()
 * 							defect 9219 Ver Special Plates 
 * K Harrell	08/22/2009	Corrected typo in method name 
 * 							  (getstcLblBegintDate)
 * 							Implement new AdministrationLogData 
 * 							constructor 
 * 							modify actionPerformed()
 * 							defect 828 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * Frame RPR005 prompts for what type of Exempt Audit to run and 
 * for which counties in the Export event.
 *
 * @version	Defect_POS_F	08/22/2009
 * @author	John Ralph
 * <br>Creation Date:		09/29/2006 14:05:00
 */

public class FrmExemptAuditReportRPR005
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener, KeyListener
{
	// boolean
	private boolean cbLockDown = true;
	private boolean cbSaved;

	// String
	private static final String ADMIN_LOG_ENTITY = "ExemptAudit";
	private static final String TITLE = "Exempt Audit Report    RPR005";

	// Vector
	private Vector cvOfcIds = new Vector();

	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSelectAllCounties = null;
	private JPanel ivjFrmExemptAuditReportRPR005ContentPane1 = null;
	private JPanel ivjpnlSelectReport = null;
	private JPanel jPanelRadioButtonGroup = null;
	private JRadioButton ivjradioExemptAuditExport = null;
	private JRadioButton ivjradioExemptAuditReport = null;
	private RTSTable ivjScrollPaneTable = null;
	private JLabel ivjstcLblBeginDate = null;
	private JLabel ivjstcLblEndDate = null;
	private JScrollPane ivjtblRegionalCnty = null;
	private RTSDateField ivjtxtBeginDate = null;
	private RTSDateField ivjtxtEndDate = null;

	private TMRPR005 caRegionalCntyTableModel = null;

	/**
	 * FrmExemptAuditReportRPR005 constructor comment.
	 */
	public FrmExemptAuditReportRPR005()
	{
		super();
		initialize();
	}

	/**
	 * FrmExemptAuditReportRPR005 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmExemptAuditReportRPR005(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmExemptAuditReportRPR005 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmExemptAuditReportRPR005(JFrame aaParent)
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
		// Code to prevent multiple button clicks
		if (!startWorking())
		{
			return;
		}
		try
		{
			clearAllColor(this);
			RTSException leEx = new RTSException();
			RTSDate laBeginDate = null;
			RTSDate laValidBeginDate = null;
			RTSDate laEndDate = null;
			RTSDate laValidEndDate = null;
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (!gettxtBeginDate().isValidDate())
				{
					leEx.addException(
						new RTSException(733),
						gettxtBeginDate());
				}
				else
				{
					laBeginDate = gettxtBeginDate().getDate();
					laValidBeginDate =
						RTSDate.getCurrentDate().add(
							RTSDate.DATE,
							-399);
				}
				if (!gettxtEndDate().isValidDate())
				{
					leEx.addException(
						new RTSException(733),
						gettxtEndDate());
				}
				else
				{
					if (laBeginDate != null)
					{
						laEndDate = gettxtEndDate().getDate();
						laValidEndDate =
							laBeginDate.add(RTSDate.DATE, 30);
					}
				}
				if (laBeginDate != null && laEndDate != null)
				{
					// defect 9020
					boolean lbBeginDateError = false;
					if (laBeginDate.compareTo(laValidBeginDate) == -1
						|| laBeginDate.compareTo(RTSDate.getCurrentDate())
							== 1)
					{
						lbBeginDateError = true;
						leEx.addException(
							new RTSException(736),
							gettxtBeginDate());
					}
					if (!lbBeginDateError)
					{
						if (laEndDate.compareTo(laValidEndDate) == 1)
						{
							leEx.addException(
								new RTSException(739),
								gettxtEndDate());

						}
						if (laEndDate
							.compareTo(RTSDate.getCurrentDate())
							== 1
							|| laEndDate.compareTo(laBeginDate) == -1)
						{
							leEx.addException(
								new RTSException(733),
								gettxtEndDate());
						}
					}
					// end defect 9020
				}

				Vector lvSelctdRows =
					new Vector(
						getScrollPaneTable().getSelectedRowNumbers());

				// Verify that at least one row has been selected
				if (lvSelctdRows.size() < CommonConstant.ELEMENT_1)
				{
					leEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_MUST_SELECT_A_COUNTY),
						getScrollPaneTable());
				}
				if (leEx.isValidationError())
				{
					leEx.displayError(this);
					leEx.getFirstComponent().requestFocus();
					return;
				}

				Vector lvCountyNumber = new Vector();
				for (int i = 0; i < lvSelctdRows.size(); i++)
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
				ExemptAuditUIData laSearchParms =
					new ExemptAuditUIData();
				laSearchParms.setBeginDate(laBeginDate);
				laSearchParms.setEndDate(laEndDate);
				laSearchParms.setSelectedOffices(lvCountyNumber);
				Vector lvData = new Vector();

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
				lvData.add(laSearchParms);

				if (getradioExemptAuditExport().isSelected())
				{
					laAdminLogData.setAction(CommonConstant.TXT_EXPORT);
					lvData.add(laAdminLogData);
					getController().processData(
						VCExemptAuditReportRPR005.EXPORT_DATA,
						lvData);
				}
				else
				{
					laAdminLogData.setAction(CommonConstant.TXT_REPORT);
					lvData.add(laAdminLogData);
					getController().processData(
						VCExemptAuditReportRPR005.DISPLAY_REPORT,
						lvData);
				}
				if (cbSaved) // Export saved
				{
					getController().processData(
						AbstractViewController.CANCEL,
						null);
				}
			}
			// If user selects SelectAllItem(s) checkbox
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
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}

			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				RTSHelp.displayHelp(RTSHelp.RPR005);
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
				//ivjButtonPanel1.addKeyListener(this);
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
		if (ivjFrmExemptAuditReportRPR005ContentPane1 == null)
		{
			try
			{
				ivjFrmExemptAuditReportRPR005ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmExemptAuditReportRPR005ContentPane1.setName(
					"FrmExemptAuditReportRPR005ContentPane1");
				ivjFrmExemptAuditReportRPR005ContentPane1.setLayout(
					null);
				ivjFrmExemptAuditReportRPR005ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmExemptAuditReportRPR005ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmExemptAuditReportRPR005ContentPane1.setBounds(
					0,
					0,
					0,
					0);
				getFrmExemptAuditReportRPR005ContentPane1().add(
					getpnlSelectReport(),
					getpnlSelectReport().getName());
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
		return ivjFrmExemptAuditReportRPR005ContentPane1;
	}

	/**
	 * This method initializes jPanelRadioButtonGroup
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanelRadioButtonGroup()
	{
		if (jPanelRadioButtonGroup == null)
		{
			jPanelRadioButtonGroup = new javax.swing.JPanel();
			jPanelRadioButtonGroup.setLayout(null);
			jPanelRadioButtonGroup.add(
				getradioExemptAuditReport(),
				null);
			jPanelRadioButtonGroup.add(
				getradioExemptAuditExport(),
				null);
			jPanelRadioButtonGroup.setBounds(5, 22, 78, 57);
		}
		return jPanelRadioButtonGroup;
	}

	private JPanel getpnlSelectReport()
	{
		if (ivjpnlSelectReport == null)
		{
			try
			{
				ivjpnlSelectReport = new JPanel();
				ivjpnlSelectReport.setName(
					CommonConstant.TXT_SELECT_ONE_COLON);
				ivjpnlSelectReport.setBorder(
					new TitledBorder(
						new EtchedBorder(),
						CommonConstant.TXT_SELECT_ONE_COLON));
				ivjpnlSelectReport.setLayout(null);
				ivjpnlSelectReport.setMaximumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setMinimumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setBounds(18, 10, 293, 86);
				ivjpnlSelectReport.add(gettxtBeginDate(), null);
				ivjpnlSelectReport.add(gettxtEndDate(), null);
				ivjpnlSelectReport.add(getstcLblEndDate(), null);
				ivjpnlSelectReport.add(getstcLblBeginDate(), null);
				ivjpnlSelectReport.add(
					getJPanelRadioButtonGroup(),
					null);

				RTSButtonGroup laradioGrp = new RTSButtonGroup();
				laradioGrp.add(getradioExemptAuditExport());
				laradioGrp.add(getradioExemptAuditReport());
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectReport;
	}
	/**
	 * Return the radioReceiveHistoryReport property value.
	 * 
	 * @return JRadioButton
	 */

	private JRadioButton getradioExemptAuditExport()
	{
		if (ivjradioExemptAuditExport == null)
		{
			try
			{
				ivjradioExemptAuditExport = new JRadioButton();
				ivjradioExemptAuditExport.setBounds(4, 28, 62, 22);
				ivjradioExemptAuditExport.setName(
					"radioExemptAuditExport");
				ivjradioExemptAuditExport.setMnemonic(
					java.awt.event.KeyEvent.VK_X);
				ivjradioExemptAuditExport.setText(
					CommonConstant.TXT_EXPORT);
				// Need Constant
				ivjradioExemptAuditExport.setMaximumSize(
					new java.awt.Dimension(154, 22));
				ivjradioExemptAuditExport.setActionCommand(
					CommonConstant.TXT_EXPORT);
				// Need Constant
				ivjradioExemptAuditExport.setMinimumSize(
					new java.awt.Dimension(154, 22));
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioExemptAuditExport;
	}

	/**
	 * Return the radioDeleteHistoryReport property value.
	 * 
	 * @return JRadioButton
	 */

	private JRadioButton getradioExemptAuditReport()
	{
		if (ivjradioExemptAuditReport == null)
		{
			try
			{
				ivjradioExemptAuditReport = new JRadioButton();
				ivjradioExemptAuditReport.setBounds(4, 0, 63, 22);
				ivjradioExemptAuditReport.setName(
					"radioExemptAuditReport");
				ivjradioExemptAuditReport.setMnemonic(
					java.awt.event.KeyEvent.VK_R);
				ivjradioExemptAuditReport.setText(
					CommonConstant.TXT_REPORT);
				// Need Constant
				ivjradioExemptAuditReport.setMaximumSize(
					new java.awt.Dimension(145, 22));
				ivjradioExemptAuditReport.setActionCommand(
					CommonConstant.TXT_REPORT);
				// Need Constant
				ivjradioExemptAuditReport.setMinimumSize(
					new java.awt.Dimension(145, 22));
			}
			catch (java.lang.Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioExemptAuditReport;
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
				// defect 9085 
				//if (!UtilityMethods.isCounty())
				if (!SystemProperty.isCounty())
				{
					liColWidthZero = 19;
					liColWidthOne = 131;
				}
				// end defect 9085 
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
	 * Return the stcLblStartDate property value.
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
	 * This method saves the results to a file.  If the user cancels
	 * from the save dialog none of the fields are cleared.  If the user
	 * enters a filename without an extension then .sql is automaticly
	 * added as a file extention.
	 * 
	 * If the file already exists then the user is prompted to overwrite
	 * the existing file.  If the user cancels from the over write then
	 * none of the fields are cleared.
	 * @param laData
	 */
	private boolean saveExportToFile(String laData)
	{
		boolean lbReturnValue = false;

		if (SystemProperty.getExportsDirectory() == null)
		{
			cbLockDown = false;
		}

		ExemptAuditSaveDialog chooser =
			new ExemptAuditSaveDialog(cbLockDown);

		if (cbLockDown)
		{
			File exportDir =
				new File(SystemProperty.getExportsDirectory());
			chooser.setCurrentDirectory(exportDir);
			chooser.setDialogTitle(
				CommonConstant.TXT_SAVE_IN_COLON_SPACE
					+ SystemProperty.getExportsDirectory().toUpperCase());
		}

		int liReturnVal = chooser.showSaveDialog(this);

		if (liReturnVal == JFileChooser.APPROVE_OPTION)
		{
			File laFile = chooser.getSelectedFile();
			if (laFile.getName().indexOf(CommonConstant.STR_PERIOD)
				== -1)
			{
				laFile =
					new File(
						laFile.getAbsolutePath()
							+ CommonConstant.TXT_PERIOD_CSV);
			}

			if (laFile.exists())
			{
				int liResponse = 0;
				String lsMsg =
					CommonConstant.TXT_OVERWRITE
						+ laFile.getAbsolutePath().toUpperCase()
						+ CommonConstant.STR_QUESTION_MARK;
				RTSException leRTSExMsg =
					new RTSException(RTSException.CTL001, lsMsg, null);
				liResponse = leRTSExMsg.displayError(this);

				if (liResponse == RTSException.YES)
				{
					writeFile(laFile, laData.toString());
					lbReturnValue = true;
				}
			}
			else
			{
				writeFile(laFile, laData.toString());
				lbReturnValue = true;
			}
		}
		return lbReturnValue;
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
		if (aaData != null)
		{
			if (aaData.toString().length() != 0)
			{
				cbSaved = saveExportToFile(aaData.toString());
			}
			else
			{
				RTSException leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
				leRTSEx.displayError(this);
				ivjScrollPaneTable.requestFocus();
			}
		}
		else
		{
			getradioExemptAuditReport().requestFocus();
			getradioExemptAuditReport().setSelected(true);
			gettxtEndDate().setDate(RTSDate.getCurrentDate());
			gettxtBeginDate().setDate(RTSDate.getCurrentDate());
			cvOfcIds =
				(Vector) UtilityMethods.copy(
					OfficeIdsCache.getOfcIds());
			UtilityMethods.sort(cvOfcIds);
			Vector lvRTSOfcIds = new Vector();
			lvRTSOfcIds.add(
				OfficeIdsCache.getOfcId(
					SystemProperty.getOfficeIssuanceNo()));
			// defect 9219
			// Exclude 257 - POS Test
			// defect 9085
			// Use SystemProperty.isCounty()  
			if (!SystemProperty.isCounty())
			{
				//			if (UtilityMethods.isRegion()
				//				|| UtilityMethods.isHeadquarters())
				//			{
				lvRTSOfcIds.clear();
				for (int i = 0; i < cvOfcIds.size(); i++)
				{
					//if (UtilityMethods.isHeadquarters())
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
			// end defect 9219 
			// end defect 9085 
			caRegionalCntyTableModel.add(lvRTSOfcIds);
			if (lvRTSOfcIds.size() == 1)
			{
				getchkSelectAllCounties().doClick();
				getchkSelectAllCounties().setEnabled(false);
			}
		}
	}

	/**
	 * main used to start class as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(java.lang.String[] args)
	{
		try
		{
			FrmExemptAuditReportRPR005 laFrmExemptAuditReportRPR005;
			laFrmExemptAuditReportRPR005 =
				new FrmExemptAuditReportRPR005();
			laFrmExemptAuditReportRPR005.setModal(true);
			laFrmExemptAuditReportRPR005
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmExemptAuditReportRPR005.show();
			java.awt.Insets insets =
				laFrmExemptAuditReportRPR005.getInsets();
			laFrmExemptAuditReportRPR005.setSize(
				laFrmExemptAuditReportRPR005.getWidth()
					+ insets.left
					+ insets.right,
				laFrmExemptAuditReportRPR005.getHeight()
					+ insets.top
					+ insets.bottom);
			laFrmExemptAuditReportRPR005.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace();
		}
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

	/**
	 * Use a PrintWriter wrapped around a BufferedWriter, which in turn
	 * is wrapped around a FileWriter, to write the string data to the
	 * given file.
	 */
	private boolean writeFile(File file, String dataString)
	{
		try
		{
			PrintWriter out =
				new PrintWriter(
					new BufferedWriter(new FileWriter(file)));
			out.print(dataString);
			out.flush();
			out.close();
		}
		catch (IOException e)
		{
			return false;
		}
		return true;
	}
}
