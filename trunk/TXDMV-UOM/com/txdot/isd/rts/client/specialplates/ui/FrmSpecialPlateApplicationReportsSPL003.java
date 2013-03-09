package com.txdot.isd.rts.client.specialplates.ui;
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
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.data.SpecialPlateUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;

/*
 * FrmSpecialPlateApplicationReportsSPL003.java
 *
 * (c) Texas Department of Transportation 2007
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	02/11/2007	Created from FRMRPR005 
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/17/2007	modified for Special Plates
 * 							defect 9085 Ver Special Plates
 * K Harrell	02/27/2007	Renamed to SPL003  
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/18/2007	Added temporary text for Help
 * 							modify actionPerformed()
 * 							defect 9085 Ver Special Plates
 * K Harrell	08/04/2007	Exclude Office 257 from list box
 * 							modify setData()
 * 							defect 9219 Ver Special Plates 	 	 	
 * K Harrell	09/04/2008	Standardize population of AdminLogData
 * 							add PERSONAL_APPL, ALL_APPL
 * 							add getAdminLogData() 
 * 							modify actionPerformed()
 * 							defect 8595 Ver Defect_POS_B
 * K Harrell	08/22/2009	Implement new AdminLogData constructor
 * 							modify getAdminLogData()
 * 							defect 8628  Defect_POS_F
 * ---------------------------------------------------------------------
 */

/**
 * Frame SPL003 prompts for the type of Special Plates Application
 * Report. 
 *
 * @version	Defect_POS_F	08/22/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		02/11/2007 15:08:00
 */
public class FrmSpecialPlateApplicationReportsSPL003
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener, KeyListener
{
	// String
	private static final String ADMIN_LOG_ENTITY = "SpclPltRpt";
	
	// defect 8595 
	private static final String PERSONAL_APPL = "Prsnl Appl"; 
	private static final String ALL_APPL= "All Appl"; 
	// end defect 8595 
	private static final String TITLE =
		"Special Plate Application Report    SPL003";

	// Vector
	private Vector cvOfcIds = new Vector();

	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSelectAllCounties = null;
	private JPanel ivjFrmSpclPltApplicationReportSPL003ContentPane1 =
		null;
	private JPanel ivjpnlSelectReport = null;
	private JRadioButton ivjradioSpclPltApplPersReport = null;
	private JRadioButton ivjradioSpclPltApplAllReport = null;
	private RTSTable ivjScrollPaneTable = null;
	private JLabel ivjstcLblBeginDate = null;
	private JLabel ivjstcLblEndDate = null;
	private JScrollPane ivjtblRegionalCnty = null;
	private RTSDateField ivjtxtBeginDate = null;
	private RTSDateField ivjtxtEndDate = null;
	private javax.swing.JPanel jPanelRadioButtonGroup = null;

	private TMSPL003 caRegionalCntyTableModel = null;

	/**
	 * FrmSpecialPlateApplicationReportsSPL003 constructor comment.
	 */
	public FrmSpecialPlateApplicationReportsSPL003()
	{
		super();
		initialize();
	}

	/**
	 * FrmSpecialPlateApplicationReportsSPL003 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmSpecialPlateApplicationReportsSPL003(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmSpecialPlateApplicationReportsSPL003 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSpecialPlateApplicationReportsSPL003(JFrame aaParent)
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
							(TMSPL003) getScrollPaneTable()
								.getModel())
								.getValueAt(
							Integer.parseInt(
								lvSelctdRows.get(i)
									+ CommonConstant.STR_SPACE_EMPTY),
							0));
				}
				UtilityMethods.sort(lvCountyNumber);
				SpecialPlateUIData laSearchParms =
					new SpecialPlateUIData();
				laSearchParms.setBeginDate(laBeginDate);
				laSearchParms.setEndDate(laEndDate);
				laSearchParms.setSelectedOffices(lvCountyNumber);
				// defect 8595 
				laSearchParms.setPersonalizedOnly(
					getradioSpclPltPersReport().isSelected());
				Vector lvData = new Vector();
				lvData.add(laSearchParms);
				lvData.add(getAdminLogData());
				// end defect 8595 
				getController().processData(
					VCSpecialPlateApplicationReportsSPL003.ENTER,
					lvData);

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
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData()
	{
		// defect 8628 
		AdministrationLogData laAdminLogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 
		
		laAdminLogData.setEntity(ADMIN_LOG_ENTITY);
		laAdminLogData.setAction(
			getradioSpclPltPersReport().isSelected() ? PERSONAL_APPL : ALL_APPL);
		laAdminLogData.setEntityValue(
			CommonConstant.STR_OPEN_PARENTHESES
				+ getScrollPaneTable().getSelectedRows().length
				+ CommonConstant.STR_CLOSE_PARENTHESES
				+ CommonConstant.STR_SPACE_ONE
				+ gettxtBeginDate().getDate().getMMDDYY()
				+ CommonConstant.STR_DASH
				+ gettxtEndDate().getDate().getMMDDYY());
		return laAdminLogData;
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
				ivjButtonPanel1.setBounds(33, 359, 262, 42);
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
				ivjchkSelectAllCounties.setMnemonic(
					java.awt.event.KeyEvent.VK_A);
				ivjchkSelectAllCounties.setText(
					CommonConstant.TXT_SELECT_ALL);
				ivjchkSelectAllCounties.setMaximumSize(
					new java.awt.Dimension(129, 22));
				ivjchkSelectAllCounties.setActionCommand(
					CommonConstant.TXT_SELECT_ALL);
				ivjchkSelectAllCounties.setBounds(18, 158, 123, 22);
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
	 * Return the FrmSpecialPlateApplicationReportSPL003ContentPane1 property
	 * value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmSpecialPlateApplicationReportSPL003ContentPane1()
	{
		if (ivjFrmSpclPltApplicationReportSPL003ContentPane1 == null)
		{
			try
			{
				ivjFrmSpclPltApplicationReportSPL003ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmSpclPltApplicationReportSPL003ContentPane1
					.setName(
					"FrmSpecialPlateApplicationReportSPL003ContentPane1");
				ivjFrmSpclPltApplicationReportSPL003ContentPane1
					.setLayout(
					null);
				ivjFrmSpclPltApplicationReportSPL003ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSpclPltApplicationReportSPL003ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmSpclPltApplicationReportSPL003ContentPane1
					.setBounds(
					0,
					0,
					0,
					0);
				getFrmSpecialPlateApplicationReportSPL003ContentPane1()
					.add(
					getpnlSelectReport(),
					getpnlSelectReport().getName());
				getFrmSpecialPlateApplicationReportSPL003ContentPane1()
					.add(
					getchkSelectAllCounties(),
					getchkSelectAllCounties().getName());
				getFrmSpecialPlateApplicationReportSPL003ContentPane1()
					.add(
					gettblRegionalCnty(),
					gettblRegionalCnty().getName());
				getFrmSpecialPlateApplicationReportSPL003ContentPane1()
					.add(
					getButtonPanel1(),
					getButtonPanel1().getName());
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjFrmSpclPltApplicationReportSPL003ContentPane1;
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
				getradioSpclPltPersReport(),
				null);
			jPanelRadioButtonGroup.add(
				getradioSpclPltApplAllReport(),
				null);
			jPanelRadioButtonGroup.add(
				getradioSpclPltPersReport(),
				null);
			jPanelRadioButtonGroup.setBounds(36, 23, 218, 51);
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
				ivjpnlSelectReport.setBounds(18, 10, 293, 144);
				ivjpnlSelectReport.add(gettxtBeginDate(), null);
				ivjpnlSelectReport.add(gettxtEndDate(), null);
				ivjpnlSelectReport.add(getstcLblEndDate(), null);
				ivjpnlSelectReport.add(getstcLblBegintDate(), null);
				ivjpnlSelectReport.add(
					getJPanelRadioButtonGroup(),
					null);
				RTSButtonGroup laradioGrp = new RTSButtonGroup();
				laradioGrp.add(getradioSpclPltApplAllReport());
				laradioGrp.add(getradioSpclPltPersReport());
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectReport;
	}
	
	/**
	 * Return the radioSpclPltApplAllReport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioSpclPltApplAllReport()
	{
		if (ivjradioSpclPltApplAllReport == null)
		{
			try
			{
				ivjradioSpclPltApplAllReport = new JRadioButton();
				ivjradioSpclPltApplAllReport.setBounds(4, 24, 211, 22);
				ivjradioSpclPltApplAllReport.setName(
					"radioAllSpclPltApplReport");
				ivjradioSpclPltApplAllReport.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjradioSpclPltApplAllReport.setText(
					"All Special Plate Applications");
				// Need Constant
				ivjradioSpclPltApplAllReport.setMaximumSize(
					new java.awt.Dimension(154, 22));
				ivjradioSpclPltApplAllReport.setActionCommand(
					"ALL_APPLICATIONS");
				// Need Constant
				ivjradioSpclPltApplAllReport.setMinimumSize(
					new java.awt.Dimension(154, 22));
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioSpclPltApplAllReport;
	}

	/**
	 * Return the radioDeleteHistoryReport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioSpclPltPersReport()
	{
		if (ivjradioSpclPltApplPersReport == null)
		{
			try
			{
				ivjradioSpclPltApplPersReport = new JRadioButton();
				ivjradioSpclPltApplPersReport.setBounds(4, 0, 212, 22);
				ivjradioSpclPltApplPersReport.setMnemonic(
					java.awt.event.KeyEvent.VK_P);
				ivjradioSpclPltApplPersReport.setText(
					"Personalized Applications Only");
				// Need Constant
				ivjradioSpclPltApplPersReport.setMaximumSize(
					new java.awt.Dimension(145, 22));
				ivjradioSpclPltApplPersReport.setActionCommand(
					CommonConstant.TXT_REPORT);
				// Need Constant
				ivjradioSpclPltApplPersReport.setMinimumSize(
					new java.awt.Dimension(145, 22));
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjradioSpclPltApplPersReport;
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
				ivjScrollPaneTable.setModel(new TMSPL003());
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
					(TMSPL003) ivjScrollPaneTable.getModel();
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
			catch (Throwable aeIVJEx)
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
	private JLabel getstcLblBegintDate()
	{
		if (ivjstcLblBeginDate == null)
		{
			try
			{
				ivjstcLblBeginDate = new JLabel();
				ivjstcLblBeginDate.setLabelFor(gettxtBeginDate());
				ivjstcLblBeginDate.setBounds(54, 79, 69, 22);
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
				ivjstcLblEndDate.setBounds(63, 107, 60, 22);
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
				ivjtblRegionalCnty.setBounds(18, 187, 293, 164);
				gettblRegionalCnty().setViewportView(
					getScrollPaneTable());
			}
			catch (Throwable aeIVJEx)
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
				ivjtxtBeginDate.setBounds(130, 80, 72, 20);
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
				ivjtxtEndDate.setBounds(130, 109, 72, 20);
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
			setSize(336, 443);
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setTitle(TITLE);
			setContentPane(
				getFrmSpecialPlateApplicationReportSPL003ContentPane1());
		}
		catch (Throwable aeIVJEx)
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
		if (aaData == null)
		{
			getradioSpclPltPersReport().requestFocus();
			getradioSpclPltPersReport().setSelected(true);
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
			if (!SystemProperty.isCounty())
			{
				lvRTSOfcIds.clear();
				for (int i = 0; i < cvOfcIds.size(); i++)
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
			// end defect 9219 
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
	public static void main(String[] args)
	{
		try
		{
			FrmSpecialPlateApplicationReportsSPL003 FrmSpecialPlateApplicationReportSPL003;
			FrmSpecialPlateApplicationReportSPL003 =
				new FrmSpecialPlateApplicationReportsSPL003();
			FrmSpecialPlateApplicationReportSPL003.setModal(true);
			FrmSpecialPlateApplicationReportSPL003
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			FrmSpecialPlateApplicationReportSPL003.show();
			java.awt.Insets insets =
				FrmSpecialPlateApplicationReportSPL003.getInsets();
			FrmSpecialPlateApplicationReportSPL003.setSize(
				FrmSpecialPlateApplicationReportSPL003.getWidth()
					+ insets.left
					+ insets.right,
				FrmSpecialPlateApplicationReportSPL003.getHeight()
					+ insets.top
					+ insets.bottom);
			FrmSpecialPlateApplicationReportSPL003.setVisibleRTS(true);
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
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
