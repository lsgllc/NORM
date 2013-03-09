package com.txdot.isd.rts.client.funds.ui;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.CloseOutHistoryData;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 *
 * FrmSubstationSummaryFUN010.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			05/21/2002	Unselecting "Select All" when mouse clicked
 * K Harrell 	02/06/2003  Added date validation 
 * 							defect 5381
 * Jeff S.		06/04/2003	Added confirm box to actionPerformed().
 * 							Rearranged screen to have the date on top.
 *							Coded the tabing for the rearranged screen.
 *							actionPerformed(), keyPressed(),
 *							getButtonPanel1(),
 *							getchkSelectAllCloseoutsDisplayed(),
 *							gettblSubstationSummary() - Defect#6166
 * Jeff S.		08/15/2003	Failed UAT because label was cut off in os2
 *							resized label and tested on os2
 *							Defect#6166
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							delete implements KeyListener
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	10/24/2005	Modified width on table 
 * 							modify gettblSubstationSummary()
 * 							defect 8379 Ver 5.2.3 
 * T Pederson	10/28/2005	Comment out keyPressed() method. Code no
 * 							longer necessary due to 8240.
 * 							defect 7886 Ver 5.2.3
 * K Harrell	12/07/2005	Present Vertical Bar only when needed 
 * 							modify gettblCloseOuts(),
 * 							gettblSubstationSummary()
 * 							defect 7886 Ver 5.2.3 
 * Jeff S.		06/26/2006	Used screen constant for CTL001 Title.
 * 							remove CONFIRM_DATE
 * 							modify actionPerformed()
 * 							defect 8756 Ver 5.2.3
 * K Harrell	09/17/2008	Setup TODAY without time
 * 							delete getBuilderData() 
 * 							modify TODAY 
 * 							defect 9826 Ver Defect_POS_B 
 * K Harrell	01/07/2009	Standardize Fees implementation for date
 * 							handling. 
 * 							refactor TODAY to caToday
 * 							refactor YESTERDAY to caYesterday 
 * 							defect 9826 Ver Defect_POS_D
 * K Harrell	03/01/2010	Create AdminLogData entry for assignment 		
 * 							 when Rerun Substation Summary. 
 * 							add getAdminLogData()
 * 							modify actionPerformed() 
 * 							defect 10168 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/**
 * Screen present list of all closeouts that have been run since the
 * last substation summary and prompts the user to selct those records
 * as well as enter the date to rerun the subsation summary report.
 *
 * @version	POS_640			03/01/2010
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmSubstationSummaryFUN010
	extends RTSDialogBox
	implements ActionListener, ListSelectionListener
{
	private JLabel ivjstcLblSentece1 = null;
	private RTSDateField ivjtxtDate = null;
	private JCheckBox ivjchkSelectAllCloseoutsDisplayed = null;
	private JLabel ivjstcLblEnterSummaryEffectiveDate = null;
	private JPanel ivjFrmSubstationSummaryFUN010ContentPane1 = null;
	private JScrollPane ivjtblCloseOuts = null;
	private RTSTable ivjtblSubstationSummary = null;
	private ButtonPanel ivjButtonPanel1 = null;

	// Objects
	private FundsData caFundsData = null;
	private TMFUN010 caSubstationSummaryTableModel = null;

	// defect 9826 
	private RTSDate caToday =
		new RTSDate(
			RTSDate.YYYYMMDD,
			RTSDate.getCurrentDate().getYYYYMMDDDate());

	private RTSDate caYesterday = caToday.add(RTSDate.DATE, -1);
	// end defect 9826

	// Constants
	private final static String ENTER_DATE =
		"Enter the date you want to run the summary for";
	private final static String IS_THIS_CORRECT =
		"</b><br>Is this correct?";
	private final static String SELECT_ALL_CLSOUTS =
		"Select all closeouts displayed";
	private final static String SELECT_CLSOUTS =
		"Select closeouts to include in substation summary";
	private final static String TITLE_FUN010 =
		"Substation Summary    FUN010";
	private final static String YOU_ARE_RUNNING_FOR =
		"You are rerunning a summary for<br><b>";

	private final static String EXCEPT_IN_MAIN =
		"Exception occurred in main() of util.JDialogTxDot";

	/**
	 * Substation_summary constructor comment.
	 */
	public FrmSubstationSummaryFUN010()
	{
		super();
		initialize();
	}
	/**
	 * Substation_summary constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmSubstationSummaryFUN010(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}
	/**
	 * Substation_summary constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSubstationSummaryFUN010(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}
	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE ActionEvent
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

			RTSException leRTSEx = new RTSException();
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				//Create vectors to store selected rows of drawers	
				Vector lvDrawers = new java.util.Vector();
				Vector lvSelectedRows =
					new Vector(
						ivjtblSubstationSummary
							.getSelectedRowNumbers());

				// Sort the selected rows, incase user selected them
				// out of order
				UtilityMethods.sort(lvSelectedRows);

				//Add each selected row to the vector of drawers
				for (int i = 0; i < lvSelectedRows.size(); i++)
				{
					String lsRow = lvSelectedRows.get(i).toString();
					int liRow2 = Integer.parseInt(lsRow);
					lvDrawers.add(
						(CloseOutHistoryData) caFundsData
							.getCashDrawers()
							.get(
							liRow2));
				}
				// defect 5381
				// Date Validation
				if (!gettxtDate().isValidDate())
				{
					leRTSEx.addException(
						new RTSException(733),
						gettxtDate());
				}
				// Set Valid Date to Today's date minus 10
				else
				{
					RTSDate laSummaryEffDate = gettxtDate().getDate();
					RTSDate laValidDate =
						caToday.add(RTSDate.DATE, -10);
					// Check if Valid Date, present exception if not
					if (laSummaryEffDate.compareTo(laValidDate) == -1
						|| laSummaryEffDate.compareTo(caYesterday) == 1)
					{
						leRTSEx.addException(
							new RTSException(733),
							gettxtDate());
					}
					// Else set date in FundsData object	
					else
					{
						caFundsData.setSummaryEffDate(laSummaryEffDate);
					}
				}
				// end defect 5831 

				if (leRTSEx.isValidationError())
				{
					leRTSEx.displayError(this);
					leRTSEx.getFirstComponent().requestFocus();
					return;
				}
				// If no drawers are selected, throw exception that
				// at least one must be picked
				if (lvDrawers.size() == 0)
				{
					RTSException leRTSExInvalidSelection =
						new RTSException(707);
					leRTSExInvalidSelection.displayError(this);
					return;
				}
				// defect 6166
				// Pop up CTL001 to confirm the date to make re-running
				// substation summary easier for the user.
				// build confirm message
				StringBuffer lsbMsgStr = new StringBuffer();
				lsbMsgStr.append(YOU_ARE_RUNNING_FOR);
				lsbMsgStr.append(gettxtDate().getDate());
				lsbMsgStr.append(IS_THIS_CORRECT);

				RTSException leRTSException =
					new RTSException(
						RTSException.CTL001,
						lsbMsgStr.toString(),
						ScreenConstant.CTL001_FRM_TITLE);

				// set location of the confirm box below the date field
				int liFrmVertPos = (int) this.getLocation().getY();
				leRTSException.setMsgLoc(
					RTSException.CENTER_HORIZONTAL,
					liFrmVertPos + 75);

				// display confirm message
				int liReturnValue = leRTSException.displayError(this);
				if (liReturnValue == RTSException.YES)
				{
					// Add vector of drawers to Funds Data
					caFundsData.setSelectedCashDrawers(lvDrawers);
					// Clear all elements presented in Cash Drawers
					// object to save memory
					caFundsData.getCashDrawers().removeAllElements();
					
					// defect 10168 
					caFundsData.setAdminLogData(getAdminLogData());
					// end defect 10168 
					 
					getController().processData(
						AbstractViewController.ENTER,
						caFundsData);
				}
				else
				{
					return; 
				}
				// end defect 6166
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
				RTSHelp.displayHelp(RTSHelp.FUN010);
			}

			else if (
				aaAE.getSource()
					== getchkSelectAllCloseoutsDisplayed())
			{
				if (getchkSelectAllCloseoutsDisplayed().isSelected())
				{
					gettblSubstationSummary().selectAllRows(
						gettblSubstationSummary().getRowCount());
				}
				else
				{
					gettblSubstationSummary().unselectAllRows();
				}
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
		AdministrationLogData laAdminLogData =
			new AdministrationLogData(ReportConstant.CLIENT);

		laAdminLogData.setEntity(CommonConstant.TXT_ADMIN_LOG_SUBSTA_SUMMARY);
		laAdminLogData.setAction(CommonConstant.TXT_ADMIN_LOG_RERUN_REPORT);
		
		laAdminLogData.setEntityValue(
		CommonConstant.STR_OPEN_PARENTHESES
			+ gettblSubstationSummary().getSelectedRows().length
			+ CommonConstant.STR_CLOSE_PARENTHESES
			+ CommonConstant.STR_SPACE_ONE
			+ gettxtDate().getDate().getMMDDYY()); 

		return laAdminLogData;
	}

	/**
	 * Return the ButtonPanel2 property value.
	 * 
	 * @return ButtonPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private ButtonPanel getButtonPanel1()
	{
		if (ivjButtonPanel1 == null)
		{
			try
			{
				ivjButtonPanel1 = new ButtonPanel();
				ivjButtonPanel1.setName("ButtonPanel1");
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
	 * Return the JCheckBox1 property value.
	 * 
	 * @return javax.swing.JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax.swing.JCheckBox getchkSelectAllCloseoutsDisplayed()
	{
		if (ivjchkSelectAllCloseoutsDisplayed == null)
		{
			try
			{
				ivjchkSelectAllCloseoutsDisplayed =
					new javax.swing.JCheckBox();
				ivjchkSelectAllCloseoutsDisplayed.setName(
					"chkSelectAllCloseoutsDisplayed");
				ivjchkSelectAllCloseoutsDisplayed.setMnemonic(83);
				ivjchkSelectAllCloseoutsDisplayed.setText(
					SELECT_ALL_CLSOUTS);
				ivjchkSelectAllCloseoutsDisplayed.setMaximumSize(
					new java.awt.Dimension(193, 22));
				ivjchkSelectAllCloseoutsDisplayed.setActionCommand(
					SELECT_ALL_CLSOUTS);
				ivjchkSelectAllCloseoutsDisplayed
					.setVerticalTextPosition(
					javax.swing.SwingConstants.CENTER);
				ivjchkSelectAllCloseoutsDisplayed.setVerticalAlignment(
					javax.swing.SwingConstants.CENTER);
				ivjchkSelectAllCloseoutsDisplayed.setMinimumSize(
					new java.awt.Dimension(193, 22));
				// user code begin {1}
				ivjchkSelectAllCloseoutsDisplayed.addActionListener(
					this);
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
		return ivjchkSelectAllCloseoutsDisplayed;
	}

	/**
	 * Return the FrmSubstationSummaryFUN010ContentPane1 property value.
	 * 
	 * @return javax.swing.JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private javax
		.swing
		.JPanel getFrmSubstationSummaryFUN010ContentPane1()
	{
		if (ivjFrmSubstationSummaryFUN010ContentPane1 == null)
		{
			try
			{
				ivjFrmSubstationSummaryFUN010ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmSubstationSummaryFUN010ContentPane1.setName(
					"FrmSubstationSummaryFUN010ContentPane1");
				ivjFrmSubstationSummaryFUN010ContentPane1.setLayout(
					new java.awt.GridBagLayout());
				ivjFrmSubstationSummaryFUN010ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmSubstationSummaryFUN010ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(425, 400));
				java.awt.GridBagConstraints constraintsstcLblSentece1 =
					new java.awt.GridBagConstraints();
				constraintsstcLblSentece1.gridx = 1;
				constraintsstcLblSentece1.gridy = 4;
				constraintsstcLblSentece1.ipadx = 9;
				constraintsstcLblSentece1.insets =
					new java.awt.Insets(5, 40, 4, 40);
				getFrmSubstationSummaryFUN010ContentPane1().add(
					getstcLblSentece1(),
					constraintsstcLblSentece1);
				java.awt.GridBagConstraints constraintstxtDate =
					new java.awt.GridBagConstraints();
				constraintstxtDate.gridx = 1;
				constraintstxtDate.gridy = 2;
				constraintstxtDate.fill =
					java.awt.GridBagConstraints.HORIZONTAL;
				constraintstxtDate.weightx = 1.0;
				constraintstxtDate.ipadx = 84;
				constraintstxtDate.insets =
					new java.awt.Insets(2, 143, 3, 144);
				getFrmSubstationSummaryFUN010ContentPane1().add(
					gettxtDate(),
					constraintstxtDate);
				java
					.awt
					.GridBagConstraints constraintschkSelectAllCloseoutsDisplayed =
					new java.awt.GridBagConstraints();
				constraintschkSelectAllCloseoutsDisplayed.gridx = 1;
				constraintschkSelectAllCloseoutsDisplayed.gridy = 3;
				constraintschkSelectAllCloseoutsDisplayed.ipadx = 9;
				constraintschkSelectAllCloseoutsDisplayed.ipady = 15;
				constraintschkSelectAllCloseoutsDisplayed.insets =
					new java.awt.Insets(4, 86, 4, 87);
				getFrmSubstationSummaryFUN010ContentPane1().add(
					getchkSelectAllCloseoutsDisplayed(),
					constraintschkSelectAllCloseoutsDisplayed);
				java.awt.GridBagConstraints constraintstblCloseOuts =
					new java.awt.GridBagConstraints();
				constraintstblCloseOuts.gridx = 1;
				constraintstblCloseOuts.gridy = 5;
				constraintstblCloseOuts.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintstblCloseOuts.weightx = 1.0;
				constraintstblCloseOuts.weighty = 1.0;
				constraintstblCloseOuts.ipadx = 284;
				constraintstblCloseOuts.ipady = 147;
				constraintstblCloseOuts.insets =
					new java.awt.Insets(5, 34, 5, 35);
				getFrmSubstationSummaryFUN010ContentPane1().add(
					gettblCloseOuts(),
					constraintstblCloseOuts);
				java
					.awt
					.GridBagConstraints constraintsstcLblEnterSummaryEffectiveDate =
					new java.awt.GridBagConstraints();
				constraintsstcLblEnterSummaryEffectiveDate.gridx = 1;
				constraintsstcLblEnterSummaryEffectiveDate.gridy = 1;
				constraintsstcLblEnterSummaryEffectiveDate.ipadx = 121;
				constraintsstcLblEnterSummaryEffectiveDate.insets =
					new java.awt.Insets(7, 58, 2, 29);
				getFrmSubstationSummaryFUN010ContentPane1().add(
					getstcLblEnterSummaryEffectiveDate(),
					constraintsstcLblEnterSummaryEffectiveDate);
				java.awt.GridBagConstraints constraintsButtonPanel1 =
					new java.awt.GridBagConstraints();
				constraintsButtonPanel1.gridx = 1;
				constraintsButtonPanel1.gridy = 6;
				constraintsButtonPanel1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsButtonPanel1.weightx = 1.0;
				constraintsButtonPanel1.weighty = 1.0;
				constraintsButtonPanel1.ipadx = 103;
				constraintsButtonPanel1.ipady = 48;
				constraintsButtonPanel1.insets =
					new java.awt.Insets(6, 42, 6, 33);
				getFrmSubstationSummaryFUN010ContentPane1().add(
					getButtonPanel1(),
					constraintsButtonPanel1);
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
		return ivjFrmSubstationSummaryFUN010ContentPane1;
	}

	/**
	 * Return the JLabel2 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblEnterSummaryEffectiveDate()
	{
		if (ivjstcLblEnterSummaryEffectiveDate == null)
		{
			try
			{
				ivjstcLblEnterSummaryEffectiveDate = new JLabel();
				ivjstcLblEnterSummaryEffectiveDate.setName(
					"stcLblEnterSummaryEffectiveDate");
				ivjstcLblEnterSummaryEffectiveDate.setText(ENTER_DATE);
				ivjstcLblEnterSummaryEffectiveDate.setMaximumSize(
					new java.awt.Dimension(167, 14));
				ivjstcLblEnterSummaryEffectiveDate.setMinimumSize(
					new java.awt.Dimension(167, 14));
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
		return ivjstcLblEnterSummaryEffectiveDate;
	}

	/**
	 * Return the JLabel1 property value.
	 * 
	 * @return javax.swing.JLabel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JLabel getstcLblSentece1()
	{
		if (ivjstcLblSentece1 == null)
		{
			try
			{
				ivjstcLblSentece1 = new javax.swing.JLabel();
				ivjstcLblSentece1.setName("stcLblSentece1");
				ivjstcLblSentece1.setText(SELECT_CLSOUTS);
				ivjstcLblSentece1.setMaximumSize(
					new java.awt.Dimension(286, 14));
				ivjstcLblSentece1.setMinimumSize(
					new java.awt.Dimension(286, 14));
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
		return ivjstcLblSentece1;
	}

	/**
	 * Return the tblCloseOuts property value.
	 * 
	 * @return javax.swing.JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane gettblCloseOuts()
	{
		if (ivjtblCloseOuts == null)
		{
			try
			{
				ivjtblCloseOuts = new javax.swing.JScrollPane();
				ivjtblCloseOuts.setName("tblCloseOuts");
				ivjtblCloseOuts.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);

				ivjtblCloseOuts.setHorizontalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				gettblCloseOuts().setViewportView(
					gettblSubstationSummary());
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
		return ivjtblCloseOuts;
	}

	/**
	 * Return the tblSubstationSummary property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblSubstationSummary()
	{
		if (ivjtblSubstationSummary == null)
		{
			try
			{
				ivjtblSubstationSummary = new RTSTable();
				ivjtblSubstationSummary.setName("tblSubstationSummary");
				gettblCloseOuts().setColumnHeaderView(
					ivjtblSubstationSummary.getTableHeader());
				gettblCloseOuts().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblSubstationSummary.setModel(
					new com.txdot.isd.rts.client.funds.ui.TMFUN010());
				ivjtblSubstationSummary.setShowVerticalLines(false);
				ivjtblSubstationSummary.setShowHorizontalLines(false);
				ivjtblSubstationSummary.setAutoCreateColumnsFromModel(
					false);
				ivjtblSubstationSummary.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblSubstationSummary.setBounds(0, 14, 288, 154);
				// user code begin {1}
				caSubstationSummaryTableModel =
					(TMFUN010) ivjtblSubstationSummary.getModel();
				TableColumn a =
					ivjtblSubstationSummary.getColumn(
						ivjtblSubstationSummary.getColumnName(0));
				a.setPreferredWidth(120);
				TableColumn b =
					ivjtblSubstationSummary.getColumn(
						ivjtblSubstationSummary.getColumnName(1));
				b.setPreferredWidth(160);
				b.setCellRenderer(
					ivjtblSubstationSummary.setColumnAlignment(
						RTSTable.CENTER));
				ivjtblSubstationSummary.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblSubstationSummary.init();
				ivjtblSubstationSummary.addActionListener(this);
				ivjtblSubstationSummary.addMultipleSelectionListener(
					this);
				ivjtblSubstationSummary.setBackground(Color.white);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblSubstationSummary;
	}

	/**
	 * Return the txtDate property value.
	 * 
	 * @return RTSDateField
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSDateField gettxtDate()
	{
		if (ivjtxtDate == null)
		{
			try
			{
				ivjtxtDate = new RTSDateField();
				ivjtxtDate.setName("txtDate");
				ivjtxtDate.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
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
		return ivjtxtDate;
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
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setName("FrmSubstationSummaryFUN010");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(375, 384);
			setTitle(TITLE_FUN010);
			setContentPane(getFrmSubstationSummaryFUN010ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSubstationSummaryFUN010 laFrmSubstationSummaryFUN010;
			laFrmSubstationSummaryFUN010 =
				new FrmSubstationSummaryFUN010();
			laFrmSubstationSummaryFUN010.setModal(true);
			laFrmSubstationSummaryFUN010
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSubstationSummaryFUN010.show();
			java.awt.Insets laInsets =
				laFrmSubstationSummaryFUN010.getInsets();
			laFrmSubstationSummaryFUN010.setSize(
				laFrmSubstationSummaryFUN010.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmSubstationSummaryFUN010.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmSubstationSummaryFUN010.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(EXCEPT_IN_MAIN);
			aeException.printStackTrace(System.out);
		}
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
			if (aaDataObject != null)
			{
				caFundsData = (FundsData) aaDataObject;
				//Add vector of rows in drawers object to Table
				caSubstationSummaryTableModel.add(
					caFundsData.getCashDrawers());
				//Set Date field to Summary Date returned in FundsData 
				gettxtDate().setDate(caFundsData.getSummaryEffDate());
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
			new Vector(ivjtblSubstationSummary.getSelectedRowNumbers());
		if (lvSelectedRows.size()
			== ivjtblSubstationSummary.getRowCount())
		{
			getchkSelectAllCloseoutsDisplayed().setSelected(true);
		}
		else
		{
			getchkSelectAllCloseoutsDisplayed().setSelected(false);
		}
	}
}
