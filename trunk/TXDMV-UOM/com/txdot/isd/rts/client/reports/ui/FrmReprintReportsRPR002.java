package com.txdot.isd.rts.client.reports.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.cache.OfficeTimeZoneCache;
import com.txdot.isd.rts.services.cache.ReportCategoryCache;
import com.txdot.isd.rts.services.cache.ReportsCache;
import com.txdot.isd.rts.services.data.ReportCategoryData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.data.ReportsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 * FrmReprintReportsRPR002.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name        	Date        Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/17/2002	OS/2 bug for Daylight Savings CQU100003429
 * Btulsiani   	04/26/2002 	Bug #3671 - Added item listener
 * MAbs			05/02/2002	Deselecting reports after printed 3752
 * MAbs			05/06/2002	Changing default selection of display
 * 							reports	to false 3779
 * N Ting		05/30/2002	Fix CQU100004154.  Make loop to display Exit
 *							button for reprint reports
 * Jeff S.		08/21/2003	XP Defect - When populating Funds reports
 * 							table all comparisons needed to be done in
 * 							uppercase so that all iterations can be
 * 							reprinted.
 *							populateReportsTable() - Defect# 6214 ver.
 *							XP
 * M Wang	   	08/21/2002  Modified populateReportsTable() to correct
 * 							notation.
 *                          Defect 6055. Version 5.1.5
 * Jeff S.		07/12/2004	Subtracted and hour from the last mod
 *							date when tz = M b/c the system returns
 *							a universal time for the last mod date.
 *							modify populateReportsTable()
 *							defect 7325 Ver 5.1.6 Fix 2
 * S Johnston	03/23/2005	changed reference to FrmExitPrintLoopCTL010
 * 							to the renamed type FrmExitPrintLoopCTL012
 * 							modify actionPerformed()
 * 							defect 6962 Ver 5.2.3
 * S Johnston	04/04/2005	Resolved the Null Pointer issue in this
 * 							class when clicking on radio buttons, fixed
 * 							the labels on the JRadioButtons so that they
 * 							show the full text, fixed tabbing and shift
 * 							tabbing issues on the RTSTable
 * 							modify itemStateChanged(), setData(),
 * 							getJRadioButton1(), getJRadioButton2(),
 * 							getJRadioButton3(), getJRadioButton4(),
 * 							getJRadioButton5(), getJRadioButton6()
 * 							defects 8131, 6859, 6398 Ver 5.2.3
 * J Zwiener	06/09/2005	Comment out setRequestFocus(false) since 
 * 							RTSDialogBox now default it to false.
 * 							modify method initialize()
 * 							defect 8215 Ver 5.2.3
 * S Johnston	06/16/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							modify getButtonPanel1, keyPressed
 * 							defect 8240 Ver 5.2.3
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data 
 * 							defect 7884  Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							Made Enter default button for frame
 * 							modify actionPerformed(), handleException(),
 * 							getButtonPanel1()
 *							defect 7896 Ver 5.2.3                 
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	11/01/2005	Adjusted Frame to allow for longer report
 * 							name. 
 * 							modify initialize(),
 * 							getFrmReprintReportsRPR002ContentPane1()
 * 							defect 8379 Ver 5.2.3 
 * Jeff S.		01/06/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.
 * 							remove keyPressed(), carrRadioButton
 * 							modify initialize()
 * 							defect 7896 Ver 5.2.3 
 * Jeff S.		01/20/2006	Rolled back change J Zwiener on 06/09/2005.
 * 							This was to get hot keys to move correctly.
 * 							Made the table the first focus field when 
 * 							the screen in presented.
 * 							modify initialize()
 * 							defect 7896 Ver 5.2.3 
 * Jeff S.		02/09/2006	Added call to RTSTable method that resets
 * 							the last cursor location back to 0.  This
 * 							is b/c in reprint reports we want the cursor
 * 							location to always start at the top when
 * 							changing categories.
 * 							modify populateReportsTable()
 * 							defect 8541 Ver 5.2.3
 * T Pederson	04/10/2007	Added check for Special Plate Application
 * 							report to set to print landscape.
 * 							modify actionPerformed() 
 * 							defect 9123 Ver Special Plates
 * K Harrell	06/20/2007	Report Number changed from 6000 to 6001
 * 							modify actionPerformed()   
 * 							defect 9085 Ver Special Plates
 * K Harrell	06/02/2008	Enlarged screen via Visual Editor to 
 * 							accommodate longer report descriptions for 
 * 							Salvage, Non-Repairable
 * 							defect 9643 Ver POS Defect A 
 * K Harrell	01/05/2009	Center WsId and Report Number 
 * 							modify gettblReports() 
 * 							defect 7124 Ver POS_Defect_D 
 * K Harrell	03/30/2009	Implement call to ReportsCache to determine 
 * 							if Landscape.
 * 							modify actionPerformed()  
 * 							defect 9972 Ver Defect_POS_E  
 * K Harrell	06/15/2009	Additional Screen Cleanup.  Refactor 
 * 							objects, methods to standards. 
 * 							add BATCH, DOWNLOADED, FRM_TITLE_RPR102,
 * 							FUNDS, INVENTORY, OTHER, TITLE,
 * 							EXIT_LOOP_MSG, EXIT_LOOP_MSG_TITLE,
 * 							EXIT_MSEC_WAIT, NO_REPORTS_IN_CAT 
 * 							delete insertSortDate(), insertSortTime(),
 * 							  insertSortWSID(), sortReportElements()
 * 							modify actionPerformed(), gettblReports(),
 * 							 initialize(), populateReportsTable()  
 * 							defect 10086 Ver Defect_POS_F
 * K Harrell	06/15/2009	Implement new Report parameters.
 * 							modify populateReportsTable()
 * 							defect 10011 Ver Defect_POS_F
 * K Harrell	04/03/2010	Implement new OfficeTimeZoneCache 
 * 							add csTimeZone
 * 							modify populateReportsTable(), setData() 
 * 							defect 10427 Ver POS_640  
 * ---------------------------------------------------------------------
 */

/**
 * Frame RPR002 is used for reprinting reports
 *
 * @version	POS_640 		04/03/2010
 * @author	R Duggirala
 * <br>Creation Date:		06/25/2001 11:20:02
 */
public class FrmReprintReportsRPR002
	extends RTSDialogBox
	implements ActionListener, ItemListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkDisRepBforePrint = null;
	private JPanel ivjFrmReprintReportsRPR002ContentPane1 = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JRadioButton ivjradioBatch = null;
	private JRadioButton ivjradioDownloaded = null;
	private JRadioButton ivjradioFunds = null;
	private JRadioButton ivjradioInventory = null;
	private JRadioButton ivjradioOther = null;
	private JRadioButton ivjradioTitle = null;
	private RTSTable ivjtblReports = null;
	private TMRPR002 caReprintRptsTableModel = null;

	private RTSButtonGroup caRadioBtnGrp = new RTSButtonGroup();

	private File[] carrRptFiles = null;

	private File caRptFilesDir = null;

	// boolean
	private boolean cbFocusTblField = false;

	// int	
	private int ciBatchRptCatId = 0;
	private int ciDownloadedRptCatId = 0;
	private int ciFundsRptCatId = 0;
	private int ciInventoryRptCatId = 0;
	private int ciOtherRptCatId = 0;
	private int ciTitleRptCatId = 0;

	// Vector
	private Vector cvReprintReports = null;
	private Vector cvRptCats = null;

	// defect 10427 
	private String csTimeZone = new String();
	// end defect 10427 

	// defect 10086 
	// Constants 
	private final static String BATCH = "Batch";
	private final static String DOWNLOADED = "Downloaded";
	private final static String EXIT_LOOP_MSG =
		"Press the Exit pushbutton to exit"
			+ " the print loop.<br> Any reports"
			+ " remaining will not be printed.<br><br>"
			+ " This screen will disappear in five"
			+ " seconds and report loop will"
			+ " continue.";
	private final static String EXIT_LOOP_MSG_TITLE =
		"Exit Print Loop   CTL012";
	private final static int EXIT_MSEC_WAIT = 5000;
	private final static String FRM_TITLE_RPR102 =
		"Reprint Reports     RPR002";
	private final static String FUNDS = "Funds";
	private final static String INVENTORY = "Inventory";
	private final static String OTHER = "Other";
	private final static String TITLE = "Title";
	private final static String NO_REPORTS_IN_CAT =
		"No reports in category";
	// end defect 10086 

	/**
	 * main entrypoint starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmReprintReportsRPR002 laFrmReprintReportsRPR002;
			laFrmReprintReportsRPR002 = new FrmReprintReportsRPR002();
			laFrmReprintReportsRPR002.setModal(true);
			laFrmReprintReportsRPR002
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmReprintReportsRPR002.show();
			Insets laInsets = laFrmReprintReportsRPR002.getInsets();
			laFrmReprintReportsRPR002.setSize(
				laFrmReprintReportsRPR002.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmReprintReportsRPR002.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmReprintReportsRPR002.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			// empty code block
		}
	}

	/**
	 * FrmReprintReportsRPR002 constructor
	 */
	public FrmReprintReportsRPR002()
	{
		super();
		initialize();
	}

	/**
	 * FrmReprintReportsRPR002 constructor
	 * 
	 * @param aaOwner Frame
	 */
	public FrmReprintReportsRPR002(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmReprintReportsRPR002 constructor
	 * 
	 * @param aaOwner Frame
	 */
	public FrmReprintReportsRPR002(Frame aaOwner)
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
			if (aaAE.getSource() == getButtonPanel1().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnHelp())
			{
				gettblReports().unselectAllRows();
				RTSHelp.displayHelp(RTSHelp.RPR002);
			}
			else if (
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				//Throw a exception when no reports are selected
				if (gettblReports().getSelectedRowNumbers().size()
					== 0)
				{
					// defect 10086 
					RTSException leEx = new RTSException(
						//614);
					ErrorsConstant.ERR_NUM_NO_REPORTS_SELECTED);
					// end defect 10086 
					leEx.displayError(this);
					ivjtblReports.requestFocus();
					return;
				}
				Vector lvSelReprintReports = new Vector();
				Vector lvSelectedRows =
					new Vector(gettblReports().getSelectedRowNumbers());
				for (int i = lvSelectedRows.size(); i > 0; i--)
				{
					String lsRow = lvSelectedRows.get(i - 1).toString();
					int liRow2 = Integer.parseInt(lsRow);
					// defect 10086 
					lvSelReprintReports
						.add(
							gettblReports()
							.getModel()
							.getValueAt(liRow2,
					// 99 
					ReportConstant.RPR002_COL_SAME_OBJECT));
					// end defect 10086 
				}
				Vector lvSelFiles = new Vector();
				ReprintReportsData laReprintRptData = null;
				ReportSearchData laRptSearchData = null;
				for (int i = 0; i < lvSelReprintReports.size(); i++)
				{
					laReprintRptData =
						(
							ReprintReportsData) lvSelReprintReports
								.elementAt(
							i);
					laRptSearchData = new ReportSearchData();
					laRptSearchData.setKey1(
						laReprintRptData.getRptFile().getPath());
					laRptSearchData.setKey3(
						laReprintRptData.getRptDesc());

					// defect 10011
					// Use ReportContant
					//laRptSearchData.setNextScreen("RPR002");
					laRptSearchData.setNextScreen(
						ReportConstant.RPR000_NEXT_SCREEN_CANCEL);
					// end defect 10011 

					// defect 9972 
					if (ReportsCache
						.isLandscape("" + laReprintRptData.getRptNo()))
					{
						laRptSearchData.setIntKey2(
							ReportConstant.LANDSCAPE);
					}
					// end defect 9972 
					lvSelFiles.add(laRptSearchData);
				}
				//Display reports before printing
				if (getchkDisRepBforePrint().isSelected())
				{
					gettblReports().requestFocus();
					getController().processData(
						VCReprintReportsRPR002.DISPLAY_REPORTS,
						lvSelFiles);
					gettblReports().unselectAllRows();
				}
				// Gives a option to exit from printing by pressing on
				// Exit button within five seconds on the dialog box. 
				else if (
					gettblReports().getSelectedRowNumbers().size() > 1
						&& !getchkDisRepBforePrint().isSelected())
				{
					for (int i = 0; i < lvSelFiles.size(); i++)
					{
						// defect 10086 
						// Use constants (w/ HTML) 
						// defect 6962
						// change CTL010 to CTL012
						int liRet =
							new RTSException(
								RTSException.EXIT_LOOP,
								EXIT_LOOP_MSG,
								EXIT_LOOP_MSG_TITLE,
								false,
								EXIT_MSEC_WAIT).displayError(
								this);
						// end defect 6962
						// end defectr 10086 

						Vector lvInput = new Vector();
						Object laObj = lvSelFiles.elementAt(i);
						lvInput.addElement(laObj);
						getController().processData(
							VCReprintReportsRPR002.PRINT_REPORTS,
							lvInput);

						//Check whether exit button pressed 
						if (liRet == RTSException.EXIT)
						{
							//Keep only the first element of selected
							//vector
							gettblReports().unselectAllRows();
							gettblReports().requestFocus();
							break;
						}
						if (i == lvSelFiles.size() - 1)
						{
							//last element
							gettblReports().unselectAllRows();
							gettblReports().requestFocus();
						}
					}
				}
				else
				{
					gettblReports().requestFocus();
					getController().processData(
						VCReprintReportsRPR002.PRINT_REPORTS,
						lvSelFiles);
					gettblReports().unselectAllRows();
				}
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
				ivjButtonPanel1.setName("ivjButtonPanel1");
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.setFocusable(false);
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
	 * Return the ivjchkDisRepBforePrint property value.
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkDisRepBforePrint()
	{
		if (ivjchkDisRepBforePrint == null)
		{
			try
			{
				ivjchkDisRepBforePrint = new JCheckBox();
				ivjchkDisRepBforePrint.setName("chkDisRepBforePrint");
				ivjchkDisRepBforePrint.setMnemonic(KeyEvent.VK_D);
				ivjchkDisRepBforePrint.setText(
					"Display report(s) before printing");
				ivjchkDisRepBforePrint.setMaximumSize(
					new Dimension(205, 22));
				ivjchkDisRepBforePrint.setActionCommand(
					"Display report(s) before printing");
				ivjchkDisRepBforePrint.setSelected(false);
				ivjchkDisRepBforePrint.setMinimumSize(
					new Dimension(205, 22));
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjchkDisRepBforePrint;
	}

	/**
	 * Return the FrmReprintReportsRPR002ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getFrmReprintReportsRPR002ContentPane1()
	{
		if (ivjFrmReprintReportsRPR002ContentPane1 == null)
		{
			try
			{
				ivjFrmReprintReportsRPR002ContentPane1 = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints6 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints5 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints7 =
					new java.awt.GridBagConstraints();
				java.awt.GridBagConstraints consGridBagConstraints8 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints6.insets =
					new java.awt.Insets(7, 176, 4, 150);
				consGridBagConstraints6.ipady = -2;
				consGridBagConstraints6.ipadx = 17;
				consGridBagConstraints6.gridy = 1;
				consGridBagConstraints6.gridx = 0;
				consGridBagConstraints5.insets =
					new java.awt.Insets(6, 22, 6, 15);
				consGridBagConstraints5.ipady = -10;
				consGridBagConstraints5.ipadx = 13;
				consGridBagConstraints5.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints5.weighty = 1.0;
				consGridBagConstraints5.weightx = 1.0;
				consGridBagConstraints5.gridy = 0;
				consGridBagConstraints5.gridx = 0;
				consGridBagConstraints7.insets =
					new java.awt.Insets(5, 22, 3, 16);
				consGridBagConstraints7.ipady = -221;
				consGridBagConstraints7.ipadx = 57;
				consGridBagConstraints7.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints7.weighty = 1.0;
				consGridBagConstraints7.weightx = 1.0;
				consGridBagConstraints7.gridy = 2;
				consGridBagConstraints7.gridx = 0;
				consGridBagConstraints8.insets =
					new java.awt.Insets(3, 59, 3, 50);
				consGridBagConstraints8.ipady = 12;
				consGridBagConstraints8.ipadx = 223;
				consGridBagConstraints8.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints8.weighty = 1.0;
				consGridBagConstraints8.weightx = 1.0;
				consGridBagConstraints8.gridy = 3;
				consGridBagConstraints8.gridx = 0;
				ivjFrmReprintReportsRPR002ContentPane1.setName(
					"FrmReprintReportsRPR002ContentPane1");
				ivjFrmReprintReportsRPR002ContentPane1.setLayout(
					new java.awt.GridBagLayout());

				ivjFrmReprintReportsRPR002ContentPane1.add(
					getJPanel1(),
					consGridBagConstraints5);
				ivjFrmReprintReportsRPR002ContentPane1.add(
					getchkDisRepBforePrint(),
					consGridBagConstraints6);
				ivjFrmReprintReportsRPR002ContentPane1.add(
					getJScrollPane1(),
					consGridBagConstraints7);
				ivjFrmReprintReportsRPR002ContentPane1.add(
					getButtonPanel1(),
					consGridBagConstraints8);
				ivjFrmReprintReportsRPR002ContentPane1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmReprintReportsRPR002ContentPane1.setMinimumSize(
					new Dimension(1012, 402));
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjFrmReprintReportsRPR002ContentPane1;
	}

	/**
	 * Return the JPanel1 property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				GridBagConstraints laGridBagConstraints1 =
					new GridBagConstraints();
				GridBagConstraints laGridBagConstraints2 =
					new GridBagConstraints();
				GridBagConstraints laGridBagConstraints3 =
					new GridBagConstraints();
				GridBagConstraints laGridBagConstraints4 =
					new GridBagConstraints();
				GridBagConstraints laGridBagConstraints5 =
					new GridBagConstraints();
				GridBagConstraints laGridBagConstraints6 =
					new GridBagConstraints();
				laGridBagConstraints1.insets = new Insets(0, 5, 0, 5);
				laGridBagConstraints2.insets = new Insets(0, 5, 0, 5);
				laGridBagConstraints3.insets = new Insets(0, 5, 0, 5);
				laGridBagConstraints4.insets = new Insets(0, 5, 0, 5);
				laGridBagConstraints5.insets = new Insets(0, 5, 0, 5);
				laGridBagConstraints6.insets = new Insets(0, 5, 0, 5);

				ivjJPanel1.setName("JPanel1");
				ivjJPanel1.setLayout(new GridBagLayout());

				ivjJPanel1.add(getradioBatch(), laGridBagConstraints1);
				ivjJPanel1.add(
					getradioDownloaded(),
					laGridBagConstraints2);
				ivjJPanel1.add(getradioFunds(), laGridBagConstraints3);
				ivjJPanel1.add(
					getradioInventory(),
					laGridBagConstraints4);
				ivjJPanel1.add(getradioTitle(), laGridBagConstraints5);
				ivjJPanel1.add(getradioOther(), laGridBagConstraints6);
				// user code begin {1}
				Border laBorder =
					new TitledBorder(
						new EtchedBorder(),
						"Select a Report Category:");
				ivjJPanel1.setBorder(laBorder);
				// user code end
				ivjJPanel1.setPreferredSize(new Dimension(498, 58));
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjJPanel1;
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
				ivjJScrollPane1.setName("scrollPane");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				ivjJScrollPane1.setForeground(Color.white);
				getJScrollPane1().setViewportView(gettblReports());
				// user code begin {1}
				ivjJScrollPane1.setMinimumSize(new Dimension(453, 203));
				ivjJScrollPane1.setPreferredSize(
					new Dimension(453, 403));
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
	 * Return the ivjradioBatch property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioBatch()
	{
		if (ivjradioBatch == null)
		{
			try
			{
				ivjradioBatch = new JRadioButton();
				ivjradioBatch.setName("ivjradioBatch");
				ivjradioBatch.setMnemonic(KeyEvent.VK_B);
				ivjradioBatch.setText(BATCH);
				// user code begin {1}
				ivjradioBatch.addItemListener(this);
				caRadioBtnGrp.add(ivjradioBatch);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjradioBatch;
	}

	/**
	 * Return the ivjradioDownloaded RadioButton property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioDownloaded()
	{
		if (ivjradioDownloaded == null)
		{
			try
			{
				ivjradioDownloaded = new JRadioButton();
				ivjradioDownloaded.setName("ivjradioDownloaded");
				ivjradioDownloaded.setMnemonic(KeyEvent.VK_W);
				ivjradioDownloaded.setText(DOWNLOADED);
				// user code begin {1}
				caRadioBtnGrp.add(ivjradioDownloaded);
				ivjradioDownloaded.addItemListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjradioDownloaded;
	}

	/**
	 * Return ivjradioFunds property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioFunds()
	{
		if (ivjradioFunds == null)
		{
			try
			{
				ivjradioFunds = new JRadioButton();
				ivjradioFunds.setName("ivjradioFunds");
				ivjradioFunds.setMnemonic(KeyEvent.VK_F);
				ivjradioFunds.setText(FUNDS);
				// user code begin {1}
				ivjradioFunds.addItemListener(this);
				caRadioBtnGrp.add(ivjradioFunds);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjradioFunds;
	}

	/**
	 * Return ivjradioInventory property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioInventory()
	{
		if (ivjradioInventory == null)
		{
			try
			{
				ivjradioInventory = new JRadioButton();
				ivjradioInventory.setName("ivjradioInventory");
				ivjradioInventory.setMnemonic(KeyEvent.VK_I);
				ivjradioInventory.setText(INVENTORY);
				// user code begin {1}
				ivjradioInventory.addItemListener(this);
				caRadioBtnGrp.add(ivjradioInventory);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjradioInventory;
	}

	/**
	 * Return the Other JRadioButton property value.
	 * 
	 * @return JRadioButton
	 *
	 **/
	private JRadioButton getradioOther()
	{
		if (ivjradioOther == null)
		{
			try
			{
				ivjradioOther = new JRadioButton();
				ivjradioOther.setName("ivjradioOther");
				ivjradioOther.setMnemonic(KeyEvent.VK_O);
				ivjradioOther.setText(OTHER);
				// user code begin {1}
				ivjradioOther.addItemListener(this);
				caRadioBtnGrp.add(ivjradioOther);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjradioOther;
	}

	/**
	 * Return ivjradioTitle property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioTitle()
	{
		if (ivjradioTitle == null)
		{
			try
			{
				ivjradioTitle = new JRadioButton();
				ivjradioTitle.setName("ivjradioTitle");
				ivjradioTitle.setMnemonic(KeyEvent.VK_T);
				ivjradioTitle.setText(TITLE);
				// user code begin {1}
				ivjradioTitle.addItemListener(this);
				caRadioBtnGrp.add(ivjradioTitle);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjradioTitle;
	}

	/**
	 * Return the ivjtblReports property value.
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblReports()
	{
		if (ivjtblReports == null)
		{
			try
			{
				ivjtblReports = new RTSTable();
				ivjtblReports.setName("tblReports");
				getJScrollPane1().setColumnHeaderView(
					ivjtblReports.getTableHeader());
				ivjtblReports.setModel(new TMRPR002());
				ivjtblReports.setAutoResizeMode(
					JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblReports.setForeground(Color.black);
				ivjtblReports.setShowHorizontalLines(false);
				ivjtblReports.setShowVerticalLines(false);
				ivjtblReports.setAutoCreateColumnsFromModel(false);
				ivjtblReports.setIntercellSpacing(new Dimension(0, 0));
				ivjtblReports.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caReprintRptsTableModel =
					(TMRPR002) ivjtblReports.getModel();

				// defect 10086 
				// Use ReportConstant 
				TableColumn laTCa =
					ivjtblReports.getColumn(
						ivjtblReports.getColumnName(
							ReportConstant.RPR002_COL_RPT_DESC));
				laTCa.setPreferredWidth(300);
				TableColumn laTCb =
					ivjtblReports.getColumn(
						ivjtblReports.getColumnName(
							ReportConstant.RPR002_COL_RPT_NO));
				laTCb.setPreferredWidth(70);
				TableColumn laTCc =
					ivjtblReports.getColumn(
						ivjtblReports.getColumnName(
							ReportConstant.RPR002_COL_RPT_WSID));
				laTCc.setPreferredWidth(70);
				TableColumn laTCd =
					ivjtblReports.getColumn(
						ivjtblReports.getColumnName(
							ReportConstant.RPR002_COL_RPT_DATE));
				laTCd.setPreferredWidth(100);
				TableColumn laTCe =
					ivjtblReports.getColumn(
						ivjtblReports.getColumnName(
							ReportConstant.RPR002_COL_RPT_TIME));
				// end defect 10086 

				laTCe.setPreferredWidth(100);
				ivjtblReports.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblReports.init();
				laTCa.setCellRenderer(
					ivjtblReports.setColumnAlignment(RTSTable.LEFT));
				// defect 7124 
				laTCb.setCellRenderer(
					ivjtblReports.setColumnAlignment(RTSTable.CENTER));
				laTCc.setCellRenderer(
					ivjtblReports.setColumnAlignment(RTSTable.CENTER));
				// end defect 7124 
				laTCd.setCellRenderer(
					ivjtblReports.setColumnAlignment(RTSTable.CENTER));
				laTCe.setCellRenderer(
					ivjtblReports.setColumnAlignment(RTSTable.CENTER));
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtblReports;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeThrowable Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
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
			setDefaultFocusField(gettblReports());
			setRequestFocus(false);
			// Code to get various category number from
			// ReportCategoryCache and set the various indicators
			// accordingly
			cvRptCats = ReportCategoryCache.getRptCats();
			ReportCategoryData laRptCatData = null;
			String lsCatDesc = "";
			for (int i = 0; i < cvRptCats.size(); i++)
			{
				laRptCatData =
					(ReportCategoryData) cvRptCats.elementAt(i);
				lsCatDesc = laRptCatData.getRptCategoryDesc().trim();

				// defect 10086 
				//	StringTokenizer laSt =
				//		new StringTokenizer(lsCatDesc, "~");
				//	lsCatDesc = "";
				//	while (laSt.hasMoreTokens())
				//	{
				//		lsCatDesc += laSt.nextToken();
				//	}
				//	lsCatDesc = lsCatDesc.trim();

				// Use Constants 
				if (lsCatDesc.equals(BATCH))
				{
					ciBatchRptCatId = laRptCatData.getRptCategoryId();
				}
				else if (lsCatDesc.equals(DOWNLOADED))
				{
					ciDownloadedRptCatId =
						laRptCatData.getRptCategoryId();
				}
				else if (lsCatDesc.equals(FUNDS))
				{
					ciFundsRptCatId = laRptCatData.getRptCategoryId();
				}
				else if (lsCatDesc.equals(INVENTORY))
				{
					ciInventoryRptCatId =
						laRptCatData.getRptCategoryId();
				}
				else if (lsCatDesc.equals(TITLE))
				{
					ciTitleRptCatId = laRptCatData.getRptCategoryId();
				}
				else if (lsCatDesc.equals(OTHER))
				{
					ciOtherRptCatId = laRptCatData.getRptCategoryId();
				}
				// end defect 10086 
			}
			// user code end
			setName("FrmReprintReportsRPR002");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(585, 367);
			// defect 10086 
			setTitle(FRM_TITLE_RPR102);
			// end defect 10086  
			setContentPane(getFrmReprintReportsRPR002ContentPane1());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * Invoked when an item has been selected or deselected.
	 * The code written for this method performs the operations
	 * that need to occur when an item is selected (or deselected).
	 * 
	 * @param aaIE ItemEvent
	 */
	public void itemStateChanged(ItemEvent aaIE)
	{
		// defect 8131
		// changed method to prevent null pointer exception in
		// added the guard for only ItemEvent.SELECTED
		// populateReportsTable() 
		if (aaIE.getStateChange() == ItemEvent.SELECTED)
		{
			if (aaIE.getSource() == getradioBatch())
			{
				gettblReports().requestFocus();
				getradioBatch().setSelected(true);
				populateReportsTable();
				cbFocusTblField = false;
			}
			if (aaIE.getSource() == getradioDownloaded())
			{
				gettblReports().requestFocus();
				getradioDownloaded().setSelected(true);
				populateReportsTable();
				cbFocusTblField = false;
			}
			if (aaIE.getSource() == getradioFunds())
			{
				gettblReports().requestFocus();
				getradioFunds().setSelected(true);
				populateReportsTable();
				cbFocusTblField = false;
			}
			if (aaIE.getSource() == getradioInventory())
			{
				gettblReports().requestFocus();
				getradioInventory().setSelected(true);
				populateReportsTable();
				cbFocusTblField = false;
			}
			if (aaIE.getSource() == getradioTitle())
			{
				gettblReports().requestFocus();
				getradioTitle().setSelected(true);
				populateReportsTable();
				cbFocusTblField = false;
			}
			if (aaIE.getSource() == getradioOther())
			{
				gettblReports().requestFocus();
				getradioOther().setSelected(true);
				populateReportsTable();
				cbFocusTblField = false;
			}
		}
		// end defect 8131
	}

	/**
	 * Populates the report table on the screen
	 */
	private void populateReportsTable()
	{
		try
		{
			Vector lvRptData = null;
			ReportsData laRptData = null;
			File laRptFile = null;
			RTSDate laRptDate = null;
			ReprintReportsData laReprintRptData = null;
			cvReprintReports = new Vector();
			boolean lbIsFunds = false;

			//Report Category: Batch
			if (getradioBatch().isSelected())
			{
				lvRptData = ReportsCache.getRpts(ciBatchRptCatId);
			}
			//Report Category: Downloaded
			else if (getradioDownloaded().isSelected())
			{
				lvRptData = ReportsCache.getRpts(ciDownloadedRptCatId);
			}
			//Report Category: Funds
			else if (getradioFunds().isSelected())
			{
				lbIsFunds = true;
				lvRptData = ReportsCache.getRpts(ciFundsRptCatId);
				if (carrRptFiles.length == 0)
				{
					// defect 10086
					throw new RTSException(
					// 389
					ErrorsConstant.ERR_NUM_NO_REPORTS_THAT_CAN_PRINT);
					// end defect 10086 

				}
				Hashtable laFilterFilesHashTable = new Hashtable();
				for (int i = 0; i < lvRptData.size(); i++)
				{
					laRptData = (ReportsData) lvRptData.elementAt(i);
					laFilterFilesHashTable.put(
						laRptData.getRptFileName(),
						new Vector());
				}
				//Get all files which have supplied name as file name
				Vector lvVec = null;
				for (int i = 0; i < carrRptFiles.length; i++)
				{
					String lsFileName = carrRptFiles[i].getName();
					String lsFileNameSubString = null;
					for (Enumeration laEnum =
						laFilterFilesHashTable.keys();
						laEnum.hasMoreElements();
						)
					{
						lsFileNameSubString =
							(String) laEnum.nextElement();
						// XP defect 6214
						// Need to compare in uppercase so that all
						// iterations can be reprinted
						if (lsFileName
							.toUpperCase()
							.endsWith(lsFileNameSubString))
							// end defect 6214
						{
							lvVec =
								(Vector) laFilterFilesHashTable.get(
									lsFileNameSubString);
							lvVec.add(carrRptFiles[i]);
							break;
						}
					}
				}
				for (int i = 0; i < lvRptData.size(); i++)
				{
					laRptData = (ReportsData) lvRptData.elementAt(i);
					lvVec =
						(Vector) laFilterFilesHashTable.get(
							laRptData.getRptFileName());
					if (lvVec.size() != 0)
					{
						for (Enumeration laEnum = lvVec.elements();
							laEnum.hasMoreElements();
							)
						{
							laRptFile = (File) laEnum.nextElement();
							laRptDate =
								new RTSDate(laRptFile.lastModified());

							// defect 10427 
							// 	try
							//	{
							//		AssignedWorkstationIdsData laAssignWksData =
							//			AssignedWorkstationIdsCache
							//				.getAsgndWsId(
							//				SystemProperty
							//					.getOfficeIssuanceNo(),
							//				SystemProperty
							//					.getSubStationId(),
							//				SystemProperty
							//					.getWorkStationId());
							laRptDate.getClientTimeZoneAdjustedDate(
								csTimeZone);
							//laAssignWksData.getTimeZone());

							//}
							//	catch (RTSException aeRTSEx)
							//	{
							//		// empty code block
							//	}
							//	// end defect 7325
							// end defect 10427 

							laReprintRptData = new ReprintReportsData();
							laReprintRptData.setRptFile(laRptFile);
							laReprintRptData.setRptDesc(
								laRptData.getRptDesc());
							laReprintRptData.setRptNo(
								laRptData.getRptNumber());
							String lsDate =
								UtilityMethods.addPadding(
									new String[] {
										laRptDate.getYear() + "/",
										laRptDate.getMonth() + "/",
										laRptDate.getDate() + "" },
									new int[] { 5, 3, 2 },
									"0");
							laReprintRptData.setDate(lsDate);
							String lsTime =
								UtilityMethods.addPadding(
									new String[] {
										laRptDate.getHour() + ":",
										laRptDate.getMinute() + ":",
										laRptDate.getSecond() + "" },
									new int[] { 3, 3, 2 },
									"0");
							laReprintRptData.setTime(lsTime);
							// defect 10086 
							int liIndex =
								laRptData.getRptFileName().indexOf(".");

							// Plan to make table driven  
							if (liIndex == 3 || liIndex == 4)
							{
								int liWsId =
									Integer.parseInt(
										laRptFile.getName().substring(
											0,
											3));
								laReprintRptData.setWsId(liWsId);
							}
							cvReprintReports.add(laReprintRptData);
						}
					}
				}
			}
			//Report Category: Inventory
			else if (getradioInventory().isSelected())
			{
				lvRptData = ReportsCache.getRpts(ciInventoryRptCatId);
			}
			//Report Category: Title
			else if (getradioTitle().isSelected())
			{
				lvRptData = ReportsCache.getRpts(ciTitleRptCatId);
			}
			//Report Category: Other
			else if (getradioOther().isSelected())
			{
				lvRptData = ReportsCache.getRpts(ciOtherRptCatId);
			}
			if (!lbIsFunds)
			{
				for (int i = 0; i < lvRptData.size(); i++)
				{
					laRptData = (ReportsData) lvRptData.elementAt(i);
					laRptFile =
						new File(
							SystemProperty.getReportsDirectory()
								+ laRptData.getRptFileName());
					if (laRptFile.exists())
					{
						laRptDate =
							new RTSDate(laRptFile.lastModified());

						// defect 10427 
						// 	try
						//	{
						//		AssignedWorkstationIdsData laAssignWksData =
						//			AssignedWorkstationIdsCache
						//				.getAsgndWsId(
						//				SystemProperty
						// 				.getOfficeIssuanceNo(),
						//				SystemProperty.getSubStationId(),
						//				SystemProperty.getWorkStationId());
						//laRptDate.getClientTimeZoneAdjustedDate(
						//laAssignWksData.getTimeZone());

						laRptDate.getClientTimeZoneAdjustedDate(csTimeZone);
						
						// 	}
						//	catch (RTSException aeRTSEx)
						//	{
						//		// empty code block
						//	}
						// end defect 10427

						laReprintRptData = new ReprintReportsData();
						laReprintRptData.setRptFile(laRptFile);
						laReprintRptData.setRptDesc(
							laRptData.getRptDesc());
						laReprintRptData.setRptNo(
							laRptData.getRptNumber());
						String lsDate =
							UtilityMethods.addPadding(
								new String[] {
									laRptDate.getYear() + "/",
									laRptDate.getMonth() + "/",
									laRptDate.getDate() + "" },
								new int[] { 5, 3, 2 },
								"0");
						laReprintRptData.setDate(lsDate);
						String lsTime =
							UtilityMethods.addPadding(
								new String[] {
									laRptDate.getHour() + ":",
									laRptDate.getMinute() + ":",
									laRptDate.getSecond() + "" },
								new int[] { 3, 3, 2 },
								"0");
						laReprintRptData.setTime(lsTime);
						cvReprintReports.add(laReprintRptData);
					}
				}
			}
			if (cvReprintReports.size() == 0)
			{
				ReprintReportsData laTempData =
					new ReprintReportsData();
				laTempData.setDate("");
				//defect 6055 / 10086 
				laTempData.setRptDesc(NO_REPORTS_IN_CAT);
				//end defect 6055 / 10086 
				laTempData.setRptNo(-1);
				laTempData.setTime("");
				laTempData.setWsId(-1);
				cvReprintReports.add(laTempData);
				gettblReports().setRowSelectionAllowed(false);
				gettblReports().setSelectedRow(0);
			}
			else
			{
				gettblReports().setRowSelectionAllowed(true);
				gettblReports().setSelectedRow(0);
			}
			// defect 10086 
			//sortReportElements();
			UtilityMethods.sort(cvReprintReports);
			// end defect 10086  
			caReprintRptsTableModel.add(cvReprintReports);
			gettblReports().unselectAllRows();
			gettblReports().setRowSelectionInterval(0, 0);
			// defect 8541
			// Used to force the cursor to the first position and not
			// the last one that was remembered.
			gettblReports().clearLastRowFocus();
			// end defect 8541
			gettblReports().requestFocus();
			ivjJScrollPane1.getViewport().setViewPosition(new Point());
		}
		catch (Exception aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * all subclasses must implement this method it sets the data on the
	 * screen and is how the controller relays information to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject == null)
		{
			// defect 10427 
			csTimeZone =
				OfficeTimeZoneCache.getTimeZone(
					SystemProperty.getOfficeIssuanceNo());
			// end defect 10427  

			caRptFilesDir =
				new File(SystemProperty.getReportsDirectory());
			carrRptFiles = caRptFilesDir.listFiles();
			getradioBatch().setSelected(true);
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="11,-20"
