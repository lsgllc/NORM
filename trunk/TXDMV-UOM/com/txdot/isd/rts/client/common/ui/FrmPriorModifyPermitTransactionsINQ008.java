package com.txdot.isd.rts.client.common.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.ModifyPermitTransactionHistoryData;
import com.txdot.isd.rts.services.data.PermitData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * FrmPriorModifyPermitTransactionsINQ008.java
 *
 * (c) Texas Department of Transportation 2011
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	06/18/2011	Created
 * 							defect 10844 Ver 6.8.0 
 * ---------------------------------------------------------------------
 */

/**
 * Frame for the display of Prior Modify Transactions
 *
 * @version	6.8.0			06/18/2011
 * @author	Kathy Harrell
 * <br>Creation Date:		06/18/2011 14:46:17 
 */
public class FrmPriorModifyPermitTransactionsINQ008
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjbtnPanel = null;
	private JPanel ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1 =
		null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblPrmtIssuanceId = null;
	private JLabel ivjlblPermitNo = null;
	private JLabel ivjstcLblPrmtIssuanceId = null;
	private JLabel ivjstcLblPermitNo = null;
	private JLabel ivjstcLblWarning = null;
	private RTSTable ivjtblPriorModifyTransRecs = null;

	//	String 
	private String csTransCd;

	// Object
	private TMINQ008 caTableModifyRecs;
	private Object caData;

	// Constants 
	private final static String FRM_NAME_INQ008 =
		"FrmPriorModifyPermitTransactionsINQ008";

	private final static String TITLE =
		"Prior Modify Permit Transactions    INQ008";

	private final static String WARNING_1_MODIFY =
		"Modify Permit Transaction for this Permit";

	private final static String WARNING_MULT_MODIFY =
		"Modify Permit Transactions for this Permit";

	/**
	 * FrmPriorModifyPermitTransactionsINQ008 constructor
	 */
	public FrmPriorModifyPermitTransactionsINQ008()
	{
		super();
		initialize();
	}

	/**
	 * FrmPriorModifyPermitTransactionsINQ008 constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmPriorModifyPermitTransactionsINQ008(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmPriorModifyPermitTransactionsINQ008 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmPriorModifyPermitTransactionsINQ008(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Initialize the class
	 */
	private void initialize()
	{
		try
		{
			// user code begin {1}
			// user code end
			setTitle(TITLE);
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			this.setContentPane(
				getFrmPriorModifyPermitTransactionsINQ008ContentPane1());
			setSize(771, 303);
			setName(FRM_NAME_INQ008);
			setContentPane(
				getFrmPriorModifyPermitTransactionsINQ008ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
	}
	/**
	 * Invoked when an action occurs
	 * 
	 * @param aaAE ActionEvent
	 */
	public void actionPerformed(ActionEvent aaAE)
	{
		/**
		 * Code to prevent multiple button clicks
		 */
		if (!startWorking())
		{
			return;
		}
		try
		{
			if (aaAE.getSource() == getbtnPanel().getBtnEnter())
			{
				((PermitData) caData).setPriorModTransList(null);

				getController().processData(
					AbstractViewController.ENTER,
					caData);
			}
			else if (aaAE.getSource() == getbtnPanel().getBtnCancel())
			{
				getController().processData(
					AbstractViewController.CANCEL,
					null);
			}
			else if (aaAE.getSource() == getbtnPanel().getBtnHelp())
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
	 * Return the ivjbtnPanel property value
	 * 
	 * @return ButtonPanel
	 */
	private ButtonPanel getbtnPanel()
	{
		if (ivjbtnPanel == null)
		{
			try
			{
				ivjbtnPanel = new ButtonPanel();
				ivjbtnPanel.setBounds(266, 229, 212, 41);
				ivjbtnPanel.setName("ivjbtnPanel");
				ivjbtnPanel.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjbtnPanel.addActionListener(this);
				ivjbtnPanel.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjbtnPanel;
	}

	/**
	 * Return the ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmPriorModifyPermitTransactionsINQ008ContentPane1()
	{
		if (ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
			== null)
		{
			try
			{
				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1 =
					new JPanel();

				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.setName(
					"ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1");

				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.setLayout(
					null);

				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.setMinimumSize(
					new Dimension(0, 0));

				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.add(
					getJScrollPane1(),
					null);

				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.add(
					getbtnPanel(),
					null);

				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.add(
					getstcLblPermitIssuanceId(),
					null);
				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.add(
					getstcLblPermitNo(),
					null);
				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.add(
					getlblPermitNo(),
					null);
				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.add(
					getlblPrmtIssuanceId(),
					null);
				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.add(
					getstcLblWarning(),
					null);
				ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1
					.setSize(
					756,
					270);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjFrmPriorModifyPermitTransactionsINQ008ContentPane1;
	}

	/**
	 * Return the ivjJScrollPane1 property value
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
				ivjJScrollPane1.setName("ivjJScrollPane1");
				ivjJScrollPane1.setVerticalScrollBarPolicy(
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				ivjJScrollPane1.setHorizontalScrollBarPolicy(
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				getJScrollPane1().setViewportView(
					gettblPriorModifyTransRecs());
				ivjJScrollPane1.setBounds(20, 72, 716, 146);
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
	 * This method initializes ivjlblPrmtIssuanceId
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPrmtIssuanceId()
	{
		if (ivjlblPrmtIssuanceId == null)
		{
			ivjlblPrmtIssuanceId = new JLabel();
			ivjlblPrmtIssuanceId.setSize(160, 20);
			ivjlblPrmtIssuanceId.setLocation(145, 42);
		}
		return ivjlblPrmtIssuanceId;
	}

	/**
	 * This method initializes ivjlblPermitNo
	 * 
	 * @return JLabel
	 */
	private JLabel getlblPermitNo()
	{
		if (ivjlblPermitNo == null)
		{
			ivjlblPermitNo = new JLabel();
			ivjlblPermitNo.setBounds(145, 22, 146, 20);

		}
		return ivjlblPermitNo;
	}

	/**
	 * This method initializes ivjstcLblPrmtIssuanceId
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPermitIssuanceId()
	{
		if (ivjstcLblPrmtIssuanceId == null)
		{
			ivjstcLblPrmtIssuanceId = new JLabel();
			ivjstcLblPrmtIssuanceId.setSize(118, 20);
			ivjstcLblPrmtIssuanceId.setText("Original TransId:");
			ivjstcLblPrmtIssuanceId.setLocation(19, 42);
			ivjstcLblPrmtIssuanceId.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblPrmtIssuanceId;
	}

	/**
	 * This method initializes ivjstcLblPermitNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblPermitNo()
	{
		if (ivjstcLblPermitNo == null)
		{
			ivjstcLblPermitNo = new JLabel();
			ivjstcLblPermitNo.setSize(70, 20);
			ivjstcLblPermitNo.setText("Permit No:");
			ivjstcLblPermitNo.setLocation(67, 22);
			ivjstcLblPermitNo.setHorizontalAlignment(
				SwingConstants.RIGHT);
		}
		return ivjstcLblPermitNo;
	}

	/**
	 * This method initializes ivjstcLblWarning
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblWarning()
	{
		if (ivjstcLblWarning == null)
		{
			ivjstcLblWarning = new JLabel();
			ivjstcLblWarning.setBounds(346, 22, 275, 20);
			ivjstcLblWarning.setText(WARNING_1_MODIFY);
			ivjstcLblWarning.setForeground(java.awt.Color.black);
		}
		return ivjstcLblWarning;
	}

	/**
	 * Return the ivjtblPriorModifyTransRecs property value
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblPriorModifyTransRecs()
	{
		if (ivjtblPriorModifyTransRecs == null)
		{
			try
			{
				ivjtblPriorModifyTransRecs = new RTSTable();
				ivjtblPriorModifyTransRecs.setName(
					"ivjtblPriorModifyTransRecs");
				getJScrollPane1().setColumnHeaderView(
					ivjtblPriorModifyTransRecs.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblPriorModifyTransRecs.setAutoResizeMode(
					JTable.AUTO_RESIZE_OFF);
				ivjtblPriorModifyTransRecs.setShowVerticalLines(false);
				ivjtblPriorModifyTransRecs.setShowHorizontalLines(
					false);
				ivjtblPriorModifyTransRecs.setIntercellSpacing(
					new Dimension(0, 0));
				ivjtblPriorModifyTransRecs.setBounds(0, 0, 200, 200);
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
		return ivjtblPriorModifyTransRecs;
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
	 * all subclasses must implement this method - it sets the data 
	 * on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaData Object
	 */
	public void setData(Object aaData)
	{
		csTransCd = getController().getTransCode();

		if (aaData != null)
		{
			caData = UtilityMethods.copy(aaData);
			Vector lvModifyTrans = new Vector();

			if (caData instanceof PermitData)
			{
				lvModifyTrans =
					((PermitData) caData).getPriorModTransList();
			}

			ModifyPermitTransactionHistoryData laData =
				(
					ModifyPermitTransactionHistoryData) lvModifyTrans
						.elementAt(
					0);
			getlblPermitNo().setText(laData.getPrmtNo());
			getlblPrmtIssuanceId().setText(laData.getPrmtIssuanceId());
			setPriorModifyTransRecs();
			caTableModifyRecs.add(lvModifyTrans);
			if (lvModifyTrans.size() > 1)
			{
				getstcLblWarning().setText(WARNING_MULT_MODIFY);
			}
		}
		gettblPriorModifyTransRecs().setRowSelectionInterval(0, 0);
	}

	/**
	 * This setups the table for In Process Transactions 
	 */
	public void setPriorModifyTransRecs()
	{
		ivjtblPriorModifyTransRecs.setModel(new TMINQ008());
		caTableModifyRecs =
			(TMINQ008) ivjtblPriorModifyTransRecs.getModel();

		// TRANSID
		TableColumn laTCTransId =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_TRANSID));
		laTCTransId.setPreferredWidth(125);

		// DATE
		TableColumn laTCDate =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_DATE));
		laTCDate.setPreferredWidth(68);

		// PERMIT TYPE
		TableColumn laTCPermitType =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_PRMTTYPE));
		laTCPermitType.setPreferredWidth(55);

		// EXP DATE
		TableColumn laTCExpDate =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_EXPMOYR));
		laTCExpDate.setPreferredWidth(70);

		// VEH MODEL YEAR 
		TableColumn laTCModYr =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_MODYR));
		laTCModYr.setPreferredWidth(35);

		// VEH MAKE
		TableColumn laTCMk =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_MAKE));
		laTCMk.setPreferredWidth(40);

		// VIN
		TableColumn laTCVin =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_VIN));
		laTCVin.setPreferredWidth(170);

		// APPLICANT
		TableColumn laTCApplicant =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_APPLICANT));
		laTCApplicant.setPreferredWidth(250);

		// EMPID
		TableColumn laTCEmpId =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_EMPID));
		laTCEmpId.setPreferredWidth(70);

		// OFCNAME 
		TableColumn laTCOfcName =
			ivjtblPriorModifyTransRecs.getColumn(
				ivjtblPriorModifyTransRecs.getColumnName(
					CommonConstant.INQ008_COL_OFCNAME));
		laTCOfcName.setPreferredWidth(255);

		ivjtblPriorModifyTransRecs.setSelectionMode(
			ListSelectionModel.SINGLE_SELECTION);

		ivjtblPriorModifyTransRecs.init();

		laTCTransId.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		laTCDate.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		laTCPermitType.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		laTCExpDate.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		laTCModYr.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		laTCMk.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		laTCVin.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		laTCApplicant.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		laTCEmpId.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		laTCOfcName.setCellRenderer(
			ivjtblPriorModifyTransRecs.setColumnAlignment(
				RTSTable.LEFT));
		getJScrollPane1().setHorizontalScrollBarPolicy(
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getJScrollPane1().setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

	}

}
