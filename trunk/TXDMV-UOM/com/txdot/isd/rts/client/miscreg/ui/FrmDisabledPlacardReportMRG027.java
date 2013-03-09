package com.txdot.isd.rts.client.miscreg.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.OfficeIdsCache;
import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.DisabledPlacardUIData;
import com.txdot.isd.rts.services.data.OfficeIdsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.*;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import javax.swing.JCheckBox;
import java.awt.event.KeyEvent;

/*
 * FrmDisabledPlacardReportMRG027.java
 *
 * (c) Texas Department of Transportation 2008
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	10/21/2008	Created.
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	10/30/2008	Added validation of checkboxes on Enter.
 * 							modify actionPerformed()
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	11/09/2008	Change maximum days for Regn/HQ to 15 vs 31 
 * 							modify actionPerformed() 
 * 							defect 9831 Ver Defect_POS_B
 * K Harrell	01/05/2009	Correct handling of "All Types" checkbox. 
 * 							modify selectChkButtons()
 * 							defect 9874 Ver Defect_POS_D
 * K Harrell	08/22/2009	Implement new AdminLogData constructor
 * 							delete KeyListener 
 * 							modify getAdminLogData()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	01/11/2012	add ivjchkRenew,ivjchkReinstate, get methods
 * 							add MAX_COUNTY_DELTA_DAYS,MAX_NON_COUNTY_DELTA_DAYS, 
 * 							  MAX_DAYS_AGO, ALL_TYPES_COUNT
 * 							modify actionPerformed(),checkCountZero(),
 * 							  getJPanel(), selectChkButtons(), setData()  
 * 							defect 11214 Ver 6.10.0 
 * K Harrell	02/23/2012	If HQ, always include OfcNo 290,291 & own 
 * 							 OfcNo. 
 * 							modify setData()
 * 							defect 11214 Ver 6.10.0 
 * ---------------------------------------------------------------------
 */

/**
 * Frame MRG027 prompts for specifications for the Disabled Placard  
 * Report. 
 *
 * @version	6.10.0 			02/23/2012
 * @author	Kathy Harrell	
 * @since					10/21/2008
 */
public class FrmDisabledPlacardReportMRG027
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkAllTypes = null;
	private JCheckBox ivjchkDelete = null;
	private JCheckBox ivjchkReplace = null;
	private JCheckBox ivjchkSelectAllCounties = null;
	private JPanel ivjFrmDisabledPlacardReportMRG027ContentPane1 = null;
	private JPanel ivjpnlSelectReport = null;
	private RTSTable ivjScrollPaneTable = null;
	private JLabel ivjstcLblBeginDate = null;
	private JLabel ivjstcLblEndDate = null;
	private JScrollPane ivjtblRegionalCnty = null;
	private RTSDateField ivjtxtBeginDate = null;
	private RTSDateField ivjtxtEndDate = null;
	private JCheckBox ivjchkAdd = null;
	private JPanel jPanel = null;

	private int ciSelectedCount = 0;

	private TMMRG027 caRegionalCntyTableModel = null;

	// Vector
	private Vector cvOfcIds = new Vector();

	// String
	private static final String ADMIN_LOG_ENTITY = "DsabldPlcrdRpt";

	private static final String TITLE =
		"Disabled Placard Report     MRG027";
	
	// defect 11214 
	private JCheckBox ivjchkRenew = null;
	private JCheckBox ivjchkReinstate = null;
	private static final int ALL_TYPES_COUNT = SystemProperty.isHQ() ? 5 :4; 
	private static final int MAX_COUNTY_DELTA_DAYS = 30; 
	private static final int MAX_NON_COUNTY_DELTA_DAYS = 14;
	private static final int MAX_DAYS_AGO = 399; 
	// end defect 11214 

	/**
	 * This method initializes ivjchkRenew	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getchkRenew()
	{
		if (ivjchkRenew == null)
		{
			ivjchkRenew = new JCheckBox();
			ivjchkRenew.setBounds(new Rectangle(157, 51, 99, 21));
			ivjchkRenew.setMnemonic(KeyEvent.VK_W);
			ivjchkRenew.addActionListener(this);
			ivjchkRenew.setText("Renew");
		}
		return ivjchkRenew;
	}

	/**
	 * This method initializes ivjchkReinstate	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
	private JCheckBox getchkReinstate()
	{
		if (ivjchkReinstate == null)
		{
			ivjchkReinstate = new JCheckBox();
			ivjchkReinstate.setBounds(new Rectangle(157, 74, 99, 21));
			ivjchkReinstate.setText("Reinstate");
			ivjchkReinstate.setMnemonic(KeyEvent.VK_I);
			ivjchkReinstate.addActionListener(this);
		}
		return ivjchkReinstate;
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
			FrmDisabledPlacardReportMRG027 FrmDisabledPlacardReportMRG027;
			FrmDisabledPlacardReportMRG027 =
				new FrmDisabledPlacardReportMRG027();
			FrmDisabledPlacardReportMRG027.setModal(true);
			FrmDisabledPlacardReportMRG027
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			FrmDisabledPlacardReportMRG027.show();
			java.awt.Insets insets =
				FrmDisabledPlacardReportMRG027.getInsets();
			FrmDisabledPlacardReportMRG027.setSize(
				FrmDisabledPlacardReportMRG027.getWidth()
					+ insets.left
					+ insets.right,
				FrmDisabledPlacardReportMRG027.getHeight()
					+ insets.top
					+ insets.bottom);
			FrmDisabledPlacardReportMRG027.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(ScreenConstant.MSG_MAIN_ERR_CATCH);
			aeEx.printStackTrace();
		}
	}

	/**
	 * FrmDisabledPlacardReportMRG027 constructor comment.
	 */
	public FrmDisabledPlacardReportMRG027()
	{
		super();
		initialize();
	}

	/**
	 * FrmDisabledPlacardReportMRG027 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmDisabledPlacardReportMRG027(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmDisabledPlacardReportMRG027 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDisabledPlacardReportMRG027(JFrame aaParent)
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
			int liDelta = 0;
			if (aaAE.getSource() instanceof JCheckBox)
			{
				// If user selects SelectAllItem(s) checkbox
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
					return;
				}
				else if (aaAE.getSource() == getchkAllTypes())
				{
					selectChkButtons(getchkAllTypes().isSelected());
				}
				else if (aaAE.getSource() == getchkAdd())
				{
					liDelta = getchkAdd().isSelected() ? 1 : -1;
					ciSelectedCount = ciSelectedCount + liDelta;
				}
				else if (aaAE.getSource() == getchkDelete())
				{
					liDelta = getchkDelete().isSelected() ? 1 : -1;
					ciSelectedCount = ciSelectedCount + liDelta;
				}
				else if (aaAE.getSource() == getchkReplace())
				{
					liDelta = getchkReplace().isSelected() ? 1 : -1;
					ciSelectedCount = ciSelectedCount + liDelta;
				}
				// defect 11214 
				else if (aaAE.getSource() == getchkRenew())
				{
					liDelta = getchkRenew().isSelected() ? 1 : -1;
					ciSelectedCount = ciSelectedCount + liDelta;
				}
				else if (aaAE.getSource() == getchkReinstate())
				{
					liDelta = getchkReinstate().isSelected() ? 1 : -1;
					ciSelectedCount = ciSelectedCount + liDelta;
				}
				// end defect 11214 
				checkCountZero();
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				// defect 11214
				// Add getchkReinstate(), getchkRenew(), 
				// include in error handling, add constants for errors 
				if (!getchkAdd().isSelected()
					&& !getchkDelete().isSelected()
					&& !getchkReplace().isSelected()
					&& !getchkReinstate().isSelected()
					&& !getchkRenew().isSelected())
				{
					leEx.addException(
						new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						getchkAdd());
					leEx.addException(
						new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						getchkDelete());
					leEx.addException(
						new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
						getchkReplace());
					leEx.addException(
							new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
							getchkRenew());
					if (getchkReinstate().isEnabled())
					{
						leEx.addException(
								new RTSException(ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
								getchkReinstate());
					}
				}

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
					
					// 399 
					laValidBeginDate =
						RTSDate.getCurrentDate().add(
							RTSDate.DATE,
							-MAX_DAYS_AGO);
				}

				if (!gettxtEndDate().isValidDate())
				{
					// 733 
					leEx.addException(
						new RTSException(ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
						gettxtEndDate());
				}
				else
				{
					int liMaxDeltaDays =
						//SystemProperty.isCounty() ? 30 : 14;
						SystemProperty.isCounty() ? MAX_COUNTY_DELTA_DAYS : MAX_NON_COUNTY_DELTA_DAYS;

					if (laBeginDate != null)
					{
						laEndDate = gettxtEndDate().getDate();
						laValidEndDate =
							laBeginDate.add(
								RTSDate.DATE,
								liMaxDeltaDays);
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
						// 736 
						leEx.addException(
							new RTSException(ErrorsConstant.ERR_NUM_HIST_DATE_INVALID),
							gettxtBeginDate());
					}
					if (!lbBeginDateError)
					{
						if (laEndDate.compareTo(laValidEndDate) == 1)
						{
							//	int liErrMsgNo =
							//		SystemProperty.isCounty() ? 739 : 744;
							
							int liErrMsgNo =
								SystemProperty.isCounty() ? 
										ErrorsConstant.ERR_NUM_DATE_RANGE_INVALID : 
											ErrorsConstant.ERR_NUM_DATE_RANGE_CANNOT_EXCEED_15_DAYS; 
							
							leEx.addException(
								new RTSException(liErrMsgNo),
								gettxtEndDate());
						}

						if (laEndDate
							.compareTo(RTSDate.getCurrentDate())
							== 1
							|| laEndDate.compareTo(laBeginDate) == -1)
						{
							// 733 
							leEx.addException(
								//new RTSException(733),
									new RTSException(ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
								gettxtEndDate());
						}
					}
				}
				// end defect 11214 
				
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
							(TMMRG027) getScrollPaneTable()
								.getModel())
								.getValueAt(
							Integer.parseInt(
								lvSelctdRows.get(i)
									+ CommonConstant.STR_SPACE_EMPTY),
							0));
				}
				UtilityMethods.sort(lvCountyNumber);
				DisabledPlacardUIData laSearchParms =
					new DisabledPlacardUIData();
				laSearchParms.setBeginDate(laBeginDate);
				laSearchParms.setEndDate(laEndDate);
				laSearchParms.setSelectedOffices(lvCountyNumber);
				laSearchParms.setAddTrans(getchkAdd().isSelected());
				laSearchParms.setRplTrans(getchkReplace().isSelected());
				laSearchParms.setDelTrans(getchkDelete().isSelected());
				
				// defect 11214 
				laSearchParms.setRenTrans(getchkRenew().isSelected());
				laSearchParms.setReiTrans(getchkReinstate().isSelected());
				// end defect 11214 
				
				Vector lvData = new Vector();
				lvData.add(laSearchParms);
				lvData.add(getAdminLogData());

				getController().processData(
					VCDisabledPlacardReportMRG027.ENTER,
					lvData);
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
	 * Check for SelectedCount field for zero value and 
	 * set the LocalOptions checkbox
	 */
	private void checkCountZero()
	{
		// defect 11214 
		//getchkAllTypes().setSelected(ciSelectedCount == 3);
		getchkAllTypes().setSelected(ciSelectedCount == ALL_TYPES_COUNT);
		// end defect 11214 
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
		laAdminLogData.setAction("Report");
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
				ivjButtonPanel1.setBounds(27, 425, 262, 42);
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
	 * This method initializes ivjchkAllTypes
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkAllTypes()
	{
		if (ivjchkAllTypes == null)
		{
			ivjchkAllTypes = new JCheckBox();
			ivjchkAllTypes.setSize(99, 21);
			ivjchkAllTypes.setText("All Types");
			ivjchkAllTypes.setMnemonic(java.awt.event.KeyEvent.VK_T);
			ivjchkAllTypes.addActionListener(this);
			ivjchkAllTypes.setLocation(32, 27);
		}
		return ivjchkAllTypes;
	}
	/**
	 * This method initializes ivjchkDelete
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkDelete()
	{
		if (ivjchkDelete == null)
		{
			ivjchkDelete = new JCheckBox();
			ivjchkDelete.setSize(99, 21);
			ivjchkDelete.setText("Delete");
			ivjchkDelete.setLocation(157, 27);
			ivjchkDelete.setMnemonic(java.awt.event.KeyEvent.VK_D);
			ivjchkDelete.addActionListener(this);
		}
		return ivjchkDelete;
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
					java.awt.event.KeyEvent.VK_S);
				ivjchkSelectAllCounties.setText(
					CommonConstant.TXT_SELECT_ALL);
				ivjchkSelectAllCounties.setMaximumSize(
					new java.awt.Dimension(129, 22));
				ivjchkSelectAllCounties.setActionCommand(
					CommonConstant.TXT_SELECT_ALL);
				ivjchkSelectAllCounties.setSize(new Dimension(123, 22));
				ivjchkSelectAllCounties.setLocation(new Point(15, 190));
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
		if (ivjFrmDisabledPlacardReportMRG027ContentPane1 == null)
		{
			try
			{
				ivjFrmDisabledPlacardReportMRG027ContentPane1 =
					new JPanel();
				ivjFrmDisabledPlacardReportMRG027ContentPane1.setName(
					"FrmSpecialPlateApplicationReportSPL003ContentPane1");
				ivjFrmDisabledPlacardReportMRG027ContentPane1
					.setLayout(
					null);
				ivjFrmDisabledPlacardReportMRG027ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmDisabledPlacardReportMRG027ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(0, 0));
				ivjFrmDisabledPlacardReportMRG027ContentPane1
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
		return ivjFrmDisabledPlacardReportMRG027ContentPane1;
	}
	/**
	 * This method initializes ivjchkReplace
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkReplace()
	{
		if (ivjchkReplace == null)
		{
			ivjchkReplace = new JCheckBox();
			ivjchkReplace.setSize(99, 21);
			ivjchkReplace.setText("Replace");
			ivjchkReplace.setLocation(32, 74);
			ivjchkReplace.setMnemonic(java.awt.event.KeyEvent.VK_R);
			ivjchkReplace.addActionListener(this);
		}
		return ivjchkReplace;
	}
	/**
	 * This method initializes ivjchkAdd
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkAdd()
	{
		if (ivjchkAdd == null)
		{
			ivjchkAdd = new JCheckBox();
			ivjchkAdd.setSize(99, 21);
			ivjchkAdd.setText("Add ");
			ivjchkAdd.setLocation(32, 51);
			ivjchkAdd.setMnemonic(java.awt.event.KeyEvent.VK_A);
			ivjchkAdd.addActionListener(this);
		}
		return ivjchkAdd;
	}
	/**
	 * This method initializes jPanel
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (jPanel == null)
		{
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.add(getchkAdd(), null);
			jPanel.add(getchkReplace(), null);
			jPanel.add(getchkDelete(), null);
			jPanel.add(getchkAllTypes(), null);
			jPanel.setBounds(10, 9, 293, 108);
			Border laBorder =
				new TitledBorder(
					new EtchedBorder(),
					"Select Transaction Types:");
			jPanel.setBorder(laBorder);
			jPanel.add(getchkRenew(), null);
			jPanel.add(getchkReinstate(), null);
		}
		return jPanel;
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
				//				ivjpnlSelectReport.setBorder(
				//					new TitledBorder(
				//						new EtchedBorder(),
				//						CommonConstant.TXT_SELECT_ONE_COLON));
				ivjpnlSelectReport.setLayout(null);
				ivjpnlSelectReport.setMaximumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setMinimumSize(
					new java.awt.Dimension(243, 64));
				ivjpnlSelectReport.setBounds(7, 3, 309, 184);
				ivjpnlSelectReport.add(gettxtBeginDate(), null);
				ivjpnlSelectReport.add(gettxtEndDate(), null);
				ivjpnlSelectReport.add(getstcLblEndDate(), null);
				ivjpnlSelectReport.add(getstcLblBegintDate(), null);
				ivjpnlSelectReport.add(getJPanel(), null);
			}
			catch (Throwable aeIVJEx)
			{
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectReport;
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
				ivjScrollPaneTable.setModel(new TMMRG027());
				ivjScrollPaneTable.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjScrollPaneTable.setShowVerticalLines(false);
				ivjScrollPaneTable.setShowHorizontalLines(false);
				ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
				ivjScrollPaneTable.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				caRegionalCntyTableModel =
					(TMMRG027) ivjScrollPaneTable.getModel();
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
				ivjstcLblBeginDate.setBounds(69, 129, 69, 22);
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
				ivjstcLblEndDate.setBounds(78, 157, 60, 22);
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
				ivjtblRegionalCnty.setViewportView(getScrollPaneTable());
				ivjtblRegionalCnty.setBounds(16, 218, 293, 195);
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
				ivjtxtBeginDate.setBounds(145, 131, 72, 20);
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
				ivjtxtEndDate.setBounds(145, 159, 72, 20);
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
			setSize(331, 509);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
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
								== 291
								// defect 11214
								|| ((OfficeIdsData) cvOfcIds.get(i))
								.getOfcIssuanceNo()
								== 290
								|| ((OfficeIdsData) cvOfcIds.get(i))
								.getOfcIssuanceNo()
								== SystemProperty.getOfficeIssuanceNo()
								// end defect 11214 
								)
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

			caRegionalCntyTableModel.add(lvRTSOfcIds);
			if (lvRTSOfcIds.size() == 1)
			{
				getchkSelectAllCounties().removeActionListener(this);
				getchkSelectAllCounties().doClick();
				getchkSelectAllCounties().setEnabled(false);
				getScrollPaneTable().setSelectedRow(0);
			}
		}
		// defect 11214 
		getchkReinstate().setEnabled(SystemProperty.isHQ());
		// end defect 11214
		getchkAllTypes().setSelected(true);
		selectChkButtons(true);
	}

	/**
	 * Sets the various check boxes depending on whether 
	 * they are enabled and updates ciSelectedCount
	 * 
	 * @param abVal boolean
	 */
	private void selectChkButtons(boolean abVal)
	{
		if (abVal)
		{
			// defect 11214
			//ciSelectedCount = 3;
			ciSelectedCount = ALL_TYPES_COUNT;
			// end defect 11214 
		}
		else
		{
			if (getchkAdd().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			if (getchkReplace().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			if (getchkDelete().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			// defect 11214 
			if (getchkRenew().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			if (getchkReinstate().isSelected())
			{
				ciSelectedCount = ciSelectedCount - 1;
			}
			// end defect 11214 
		}

		getchkAdd().setSelected(abVal);
		getchkDelete().setSelected(abVal);
		getchkReplace().setSelected(abVal);
		
		// defect 11214
		getchkRenew().setSelected(abVal);
		getchkReinstate().setSelected(abVal && SystemProperty.isHQ());
		// end defect 11214 
		
		checkCountZero();
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
