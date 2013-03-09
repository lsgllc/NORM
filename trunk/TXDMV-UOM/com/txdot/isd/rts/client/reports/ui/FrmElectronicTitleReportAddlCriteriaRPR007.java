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

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.cache.CertifiedLienholderCache;
import com.txdot.isd.rts.services.cache.TransactionCodesCache;
import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.ElectronicTitleHistoryUIData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * FrmElectronicTitleReportAddlCriteriaRPR007.java
 *
 * (c) Texas Department of Transportation 2009
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	03/20/2009	Created 
 * 							defect 9972 Ver Defect_POS_E 
 * K Harrell	03/26/2009	Use Final vs. Cancel after save report.
 * 							modify actionPerformed()
 * 							defect 9972 Ver Defect_POS_E
 * K Harrell	08/19/2009	Sort list of Certified Lienholders by row  
 * 							number prior to reort. 
 * 							modify setDataToDataObject() 
 * 							defect 10167 Ver Defect_POS_E' 
 * K Harrell	08/18/2009	Sort Selected Lienholders so that 
 * 							Exception Report in order by Name 
 * 							modify setDataToDataObject()
 * 							defect 10167 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/**
 * Frame RPR007 prompts for selection of TransCd/Certified Lienholders
 *
 * @version	Defect_POS_F	08/18/2009
 * @author	Kathy Harrell
 * <br>Creation Date:		03/20/2009
 */
public class FrmElectronicTitleReportAddlCriteriaRPR007
	extends RTSDialogBox
	implements ActionListener, KeyListener, ListSelectionListener
{
	private AdministrationLogData caAdminLogData = null;

	// boolean
	private boolean cbLockDown = true;
	private boolean cbSaved;

	// Object 
	private ElectronicTitleHistoryUIData caETitleUIData = null;
	private TMRPR007b caETtlCertLienholderTableModel = null;
	private TMRPR007a caTransCdTableModel = null;

	// Vector
	private Vector cvCertLienhldr = null;
	private Vector cvTransCd = null;

	// Constant
	private ButtonPanel ivjButtonPanel1 = null;
	private JCheckBox ivjchkSelectAllETtlCertLienhldr = null;
	private JCheckBox ivjchkSelectAllTransType = null;
	private JScrollPane ivjJScrollPane1 = null;

	private JScrollPane ivjJScrollPane2 = null;
	private JPanel ivjpnlSelectOneOrMore = null;
	private JPanel ivjpnlSelectTransType = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private RTSTable ivjtblSelectETtlCertLienhldr = null;
	private RTSTable ivjtblSelectType = null;

	/**
	 * FrmElectronicTitleReportAddlCriteriaRPR007 constructor comment.
	 */
	public FrmElectronicTitleReportAddlCriteriaRPR007()
	{
		super();
		initialize();
	}

	/**
	 * FrmElectronicTitleReportAddlCriteriaRPR007 constructor comment.
	 * 
	 * @param aaParent JDialog
	 */
	public FrmElectronicTitleReportAddlCriteriaRPR007(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmElectronicTitleReportAddlCriteriaRPR007 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmElectronicTitleReportAddlCriteriaRPR007(JFrame aaParent)
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

			if (aaAE.getSource() == getchkSelectAllETtlCertLienhldr())
			{
				if (getchkSelectAllETtlCertLienhldr().isSelected())
				{
					gettblSelectETtlCertLienhldr().selectAllRows(
						gettblSelectETtlCertLienhldr().getRowCount());
				}
				else
				{
					gettblSelectETtlCertLienhldr().unselectAllRows();
				}
			}
			else if (aaAE.getSource() == getchkSelectAllTransTypes())
			{
				if (getchkSelectAllTransTypes().isSelected())
				{
					gettblSelectTransType().selectAllRows(
						gettblSelectTransType().getRowCount());
				}
				else
				{
					gettblSelectTransType().unselectAllRows();
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
				aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (!setDataToDataObject())
				{
					return;
				}

				Vector lvDataOut = new Vector(CommonConstant.ELEMENT_2);
				lvDataOut.add(caETitleUIData);
				lvDataOut.add(caAdminLogData);
				int liAction =
					caAdminLogData.getAction().equals(
						CommonConstant.TXT_EXPORT)
						? VCElectronicTitleReportAddlCriteriaRPR007
							.EXPORT_DATA
						: VCElectronicTitleReportAddlCriteriaRPR007
							.DISPLAY_REPORT;
				getController().processData(liAction, lvDataOut);
				if (cbSaved) // Export saved
				{
					getController().processData(
						AbstractViewController.FINAL,
						null);
				}
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
	 * Return the ButtonPanel1 property value.
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
				ivjButtonPanel1.setBounds(57, 372, 338, 39);
				ivjButtonPanel1.setName("ButtonPanel1");
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
	 * Return the ivjchkSelectAllETtlCertLienhldr property value.
	 * 
	 * @return JCheckBox
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JCheckBox getchkSelectAllETtlCertLienhldr()
	{
		if (ivjchkSelectAllETtlCertLienhldr == null)
		{
			try
			{
				ivjchkSelectAllETtlCertLienhldr = new JCheckBox();
				ivjchkSelectAllETtlCertLienhldr.setBounds(
					25,
					207,
					186,
					24);
				ivjchkSelectAllETtlCertLienhldr.setName(
					"ivjchkSelectAllETtlCertLienhldr");
				ivjchkSelectAllETtlCertLienhldr.setMnemonic(
					java.awt.event.KeyEvent.VK_S);
				ivjchkSelectAllETtlCertLienhldr.setText(
					"Select All Lienholders");
				// user code begin {1}
				ivjchkSelectAllETtlCertLienhldr.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjchkSelectAllETtlCertLienhldr;
	}

	/**
	 * This method initializes jCheckBox
	 * 
	 * @return JCheckBox
	 */
	private JCheckBox getchkSelectAllTransTypes()
	{
		if (ivjchkSelectAllTransType == null)
		{
			ivjchkSelectAllTransType = new javax.swing.JCheckBox();
			ivjchkSelectAllTransType.setBounds(18, 33, 194, 21);
			ivjchkSelectAllTransType.setText(
				"Select All Transaction Types");
			ivjchkSelectAllTransType.setMnemonic(
				java.awt.event.KeyEvent.VK_A);
			// user code begin {1}
			ivjchkSelectAllTransType.addActionListener(this);
			// user code end
		}
		return ivjchkSelectAllTransType;
	}

	/**
	 * Return the JScrollPane1 property value.
	 * 
	 * @return JScrollPane
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JScrollPane getJScrollPane1()
	{
		if (ivjJScrollPane1 == null)
		{
			try
			{
				ivjJScrollPane1 = new JScrollPane();
				ivjJScrollPane1.setName("JScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				getJScrollPane1().setViewportView(
					gettblSelectETtlCertLienhldr());
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
		return ivjJScrollPane1;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane2()
	{
		if (ivjJScrollPane2 == null)
		{
			try
			{
				ivjJScrollPane2 = new JScrollPane();
				ivjJScrollPane2.setName("JScrollPane2");
				ivjJScrollPane2.setVerticalScrollBarPolicy(
					javax
						.swing
						.JScrollPane
						.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane2.setHorizontalScrollBarPolicy(
					javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				getJScrollPane2().setViewportView(
					gettblSelectTransType());
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
	 * Return the pnlSelectOneOrMore property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlSelectOneOrMore()
	{
		if (ivjpnlSelectOneOrMore == null)
		{
			try
			{
				ivjpnlSelectOneOrMore = new JPanel();
				ivjpnlSelectOneOrMore.setName("pnlSelectOneOrMore");
				ivjpnlSelectOneOrMore.setBorder(
					new TitledBorder(new EtchedBorder(), ""));
				ivjpnlSelectOneOrMore.setLayout(
					new java.awt.GridBagLayout());

				java.awt.GridBagConstraints constraintsJScrollPane1 =
					new java.awt.GridBagConstraints();
				constraintsJScrollPane1.gridx = 1;
				constraintsJScrollPane1.gridy = 1;
				constraintsJScrollPane1.fill =
					java.awt.GridBagConstraints.BOTH;
				constraintsJScrollPane1.weightx = 1.0;
				constraintsJScrollPane1.weighty = 1.0;
				constraintsJScrollPane1.ipadx = 396;
				constraintsJScrollPane1.ipady = 105;
				constraintsJScrollPane1.insets =
					new java.awt.Insets(7, 7, 3, 6);
				getpnlSelectOneOrMore().add(
					getJScrollPane1(),
					constraintsJScrollPane1);
				// user code begin {1}
				ivjpnlSelectOneOrMore.setBounds(19, 236, 441, 125);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectOneOrMore;
	}

	/**
	 * Return the pnlSelectSearchType property value.
	 * 
	 * @return JPanel
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private JPanel getpnlSelectTransType()
	{
		if (ivjpnlSelectTransType == null)
		{
			try
			{
				ivjpnlSelectTransType = new JPanel();
				java.awt.GridBagConstraints consGridBagConstraints4 =
					new java.awt.GridBagConstraints();
				consGridBagConstraints4.fill =
					java.awt.GridBagConstraints.BOTH;
				consGridBagConstraints4.weighty = 1.0;
				consGridBagConstraints4.weightx = 1.0;
				consGridBagConstraints4.gridy = 1;
				consGridBagConstraints4.gridx = 1;
				consGridBagConstraints4.ipadx = 396;
				consGridBagConstraints4.ipady = 105;
				consGridBagConstraints4.insets =
					new java.awt.Insets(7, 7, 3, 6);
				ivjpnlSelectTransType.setName("pnlSelectSearchType");
				ivjpnlSelectTransType.setBorder(
					new TitledBorder(new EtchedBorder(), ""));
				ivjpnlSelectTransType.setLayout(
					new java.awt.GridBagLayout());
				ivjpnlSelectTransType.add(
					getJScrollPane2(),
					consGridBagConstraints4);
				ivjpnlSelectTransType.setBounds(18, 63, 441, 125);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjpnlSelectTransType;
	}

	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
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

				ivjRTSDialogBoxContentPane.add(
					getpnlSelectTransType(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getchkSelectAllETtlCertLienhldr(),
					null);
				ivjRTSDialogBoxContentPane.add(
					getpnlSelectOneOrMore(),
					null);
				ivjRTSDialogBoxContentPane.add(getButtonPanel1(), null);
				// user code end
				ivjRTSDialogBoxContentPane.add(
					getchkSelectAllTransTypes(),
					null);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the ivjtblSelectETtlCertLienholder property value.
	 * 
	 * @return RTSTable
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private RTSTable gettblSelectETtlCertLienhldr()
	{
		if (ivjtblSelectETtlCertLienhldr == null)
		{
			try
			{
				ivjtblSelectETtlCertLienhldr = new RTSTable();
				ivjtblSelectETtlCertLienhldr.setName(
					"ivjtblSelectETtlCertLienholder");
				getJScrollPane1().setColumnHeaderView(
					ivjtblSelectETtlCertLienhldr.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblSelectETtlCertLienhldr.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblSelectETtlCertLienhldr.setModel(new TMRPR007b());
				ivjtblSelectETtlCertLienhldr.setShowVerticalLines(
					false);
				ivjtblSelectETtlCertLienhldr.setShowHorizontalLines(
					false);
				ivjtblSelectETtlCertLienhldr
					.setAutoCreateColumnsFromModel(
					false);
				ivjtblSelectETtlCertLienhldr.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblSelectETtlCertLienhldr.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caETtlCertLienholderTableModel =
					(TMRPR007b) ivjtblSelectETtlCertLienhldr.getModel();
				TableColumn laTCa =
					ivjtblSelectETtlCertLienhldr.getColumn(
						ivjtblSelectETtlCertLienhldr.getColumnName(0));
				laTCa.setPreferredWidth(200);
				TableColumn laTCb =
					ivjtblSelectETtlCertLienhldr.getColumn(
						ivjtblSelectETtlCertLienhldr.getColumnName(1));
				laTCb.setPreferredWidth(500);
				ivjtblSelectETtlCertLienhldr.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblSelectETtlCertLienhldr.init();
				ivjtblSelectETtlCertLienhldr.addActionListener(this);
				ivjtblSelectETtlCertLienhldr
					.addMultipleSelectionListener(
					this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblSelectETtlCertLienhldr;
	}
	private RTSTable gettblSelectTransType()
	{
		if (ivjtblSelectType == null)
		{
			try
			{
				ivjtblSelectType = new RTSTable();
				ivjtblSelectType.setName("tblSelectOneOrMore");
				getJScrollPane2().setColumnHeaderView(
					ivjtblSelectType.getTableHeader());
				getJScrollPane2().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblSelectType.setAutoResizeMode(
					javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN);
				ivjtblSelectType.setModel(new TMRPR007a());
				ivjtblSelectType.setShowVerticalLines(false);
				ivjtblSelectType.setShowHorizontalLines(false);
				ivjtblSelectType.setAutoCreateColumnsFromModel(false);
				ivjtblSelectType.setIntercellSpacing(
					new java.awt.Dimension(0, 0));
				ivjtblSelectType.setBounds(0, 0, 200, 200);
				// user code begin {1}
				caTransCdTableModel =
					(TMRPR007a) ivjtblSelectType.getModel();
				TableColumn laTCa =
					ivjtblSelectType.getColumn(
						ivjtblSelectType.getColumnName(0));
				laTCa.setPreferredWidth(200);
				TableColumn laTCb =
					ivjtblSelectType.getColumn(
						ivjtblSelectType.getColumnName(1));
				laTCb.setPreferredWidth(500);
				ivjtblSelectType.setSelectionMode(
					ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				ivjtblSelectType.init();
				ivjtblSelectType.addActionListener(this);
				ivjtblSelectType.addMultipleSelectionListener(this);

			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjtblSelectType;
	}
	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeException Throwable
	 */
	private void handleException(Throwable aeException)
	{
		// defect 7890
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeException);
		leRTSEx.displayError(this);
		// end defect 7890
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
			setName("FrmElectronicTitleReportAddlCriteriaRPR007");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(480, 448);
			setTitle("Electronic Title Report Additional Criteria  RPR007");
			setContentPane(getRTSDialogBoxContentPane());
			setRequestFocus(false);
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
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		if (aaData != null)
		{
			if (aaData instanceof Vector)
			{
				Vector lvData = (Vector) aaData;
				caETitleUIData =
					(ElectronicTitleHistoryUIData) lvData.elementAt(0);
				caAdminLogData =
					(AdministrationLogData) lvData.elementAt(1);
				cvTransCd =
					(Vector) UtilityMethods.copy(
						TransactionCodesCache.getETitleTransCds());
				UtilityMethods.sort(cvTransCd);

				caTransCdTableModel.add(cvTransCd);
				if (cvTransCd.size() == 1)
				{
					getchkSelectAllTransTypes().setEnabled(false);
					gettblSelectTransType().setEnabled(false);
				}

				getchkSelectAllTransTypes().doClick();

				cvCertLienhldr =
					CertifiedLienholderCache
						.getLastestETtlCertfdLienhldrs();

				UtilityMethods.sort(cvCertLienhldr);

				caETtlCertLienholderTableModel.add(cvCertLienhldr);

				if (cvCertLienhldr.size() == 1)
				{
					getchkSelectAllETtlCertLienhldr().setEnabled(false);
					gettblSelectETtlCertLienhldr().setEnabled(false);
				}
				getchkSelectAllETtlCertLienhldr().doClick();
			}
			else if (aaData.toString().length() != 0)
			{
				cbSaved = saveExportToFile(aaData.toString());
			}
			else
			{
				RTSException leRTSEx =
					new RTSException(
						ErrorsConstant.ERR_NUM_NO_RECORDS_IN_DB);
				leRTSEx.displayError(this);
			}
		}

	}

	/**
	 * Validate the check boxes, dates, and combo box on the screen.
	 * 
	 * @return boolean
	 */
	private boolean setDataToDataObject()
	{
		RTSException leRTSEx = new RTSException();
		boolean lbReturn = false;

		Vector lvSelectedTransRows =
			new Vector(gettblSelectTransType().getSelectedRowNumbers());

		// Verify that at least one row has been selected
		if (lvSelectedTransRows.size() == 0)
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_AT_LEAST_ONE_TRANS_TYPE),
				gettblSelectTransType());
		}

		Vector lvSelectedLienRows =
			new Vector(
				gettblSelectETtlCertLienhldr().getSelectedRowNumbers());

		// Verify that at least one row has been selected
		if (lvSelectedLienRows.size() == 0)
		{
			leRTSEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_AT_LEAST_ONE_LIENHLDR),
				gettblSelectETtlCertLienhldr());
		}

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		else
		{

			Vector lvSelectedTransCd = new Vector();
			Vector lvSelectedCertLienhldr = new Vector();

			for (int i = 0; i < lvSelectedTransRows.size(); i++)
			{
				lvSelectedTransCd.add(
					(
						(TMRPR007a) gettblSelectTransType()
							.getModel())
							.getValueAt(
						Integer.parseInt(
							lvSelectedTransRows.get(i)
								+ CommonConstant.STR_SPACE_EMPTY),
						0));
			}
			
			// defect 10167 
			// Rows in table in order of Name 
			UtilityMethods.sort(lvSelectedLienRows);
			// end defect 10167 
			 
			for (int j = 0; j < lvSelectedLienRows.size(); j++)
			{
				lvSelectedCertLienhldr.add(
					(
						(TMRPR007b) gettblSelectETtlCertLienhldr()
							.getModel())
							.getValueAt(
						Integer.parseInt(
							lvSelectedLienRows.get(j)
								+ CommonConstant.STR_SPACE_EMPTY),
						0));
			}
			caETitleUIData.setSelectedTransCd(lvSelectedTransCd);
			caETitleUIData.setSelectedPermLienhldrId(
				lvSelectedCertLienhldr);
			lbReturn = true;
		}
		return lbReturn;
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
		if (aaLSE.getSource().equals(gettblSelectETtlCertLienhldr()))
		{
			Vector lvSelctdRows =
				new Vector(
					gettblSelectETtlCertLienhldr()
						.getSelectedRowNumbers());

			getchkSelectAllETtlCertLienhldr().setSelected(
				lvSelctdRows.size()
					== gettblSelectETtlCertLienhldr().getRowCount());
		}
		else
		{
			Vector lvSelctdRows =
				new Vector(
					gettblSelectTransType().getSelectedRowNumbers());

			getchkSelectAllTransTypes().setSelected(
				lvSelctdRows.size()
					== gettblSelectTransType().getRowCount());
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
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
