package com.txdot.isd.rts.client.common.ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSTable;

import com.txdot.isd.rts.services.data.InProcessTransactionData;
import com.txdot.isd.rts.services.data.TitleInProcessData;
import com.txdot.isd.rts.services.data.VehicleInquiryData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * FrmInProcessTransactionsINQ002.java
 *
 * (c) Texas Department of Transportation 2010
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell	09/17/2010	Created
 * 							defect 10598 Ver 6.6.0 
 * K Harrell	10/04/2010	Modify to accommodate TitleInProcessData as
 * 							well as VehicleInquiryData 
 * 							defect 10598 Ver 6.6.0 
 * ---------------------------------------------------------------------
 */

/**
 * Frame for the display of In-Process Transactions
 *
 * @version	6.6.0			10/04/2010
 * @author	Kathy Harrell
 * <br>Creation Date:		09/17/2010 19:47:17
 */
public class FrmInProcessTransactionsINQ002
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjbtnPanel = null;
	private JPanel ivjFrmInProcessTransactionsINQ002ContentPane1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JLabel ivjlblDocNo = null;
	private JLabel ivjlblVIN = null;
	private JLabel ivjstcLblDocNo = null;
	private JLabel ivjstcLblVIN = null;
	private JLabel ivjstcLblWarning = null;
	private RTSTable ivjtblInProcessTransRecs = null;

	//	String 
	private String csTransCd;

	// Object
	private TMINQ002 caTablePndgRecs;
	private boolean cbVehInqData = false;
	private Object caData;

	// Constants 
	private final static String FRM_NAME_INQ002 =
		"FrmInProcessTransactionsINQ002";

	private final static String TITLE =
		"In-Process Transactions    INQ002";

	private final static String WARNING_1_INPROCESS =
		"The transaction listed below is in-process for this vehicle.";

	private final static String WARNING_MULT_INPROCESS =
		"The transactions listed below are in-process for this vehicle.";

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmInProcessTransactionsINQ002 laFrmInProcessTransRecs;
			laFrmInProcessTransRecs =
				new FrmInProcessTransactionsINQ002();
			laFrmInProcessTransRecs.setModal(true);
			laFrmInProcessTransRecs
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmInProcessTransRecs.show();
			Insets laInsets = laFrmInProcessTransRecs.getInsets();
			laFrmInProcessTransRecs.setSize(
				laFrmInProcessTransRecs.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmInProcessTransRecs.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmInProcessTransRecs.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			aeEx.printStackTrace();
		}
	}

	/**
	 * FrmInProcessTransactionsINQ002 constructor
	 */
	public FrmInProcessTransactionsINQ002()
	{
		super();
		initialize();
	}

	/**
	 * FrmInProcessTransactionsINQ002 constructor
	 * 
	 * @param aaParent JDialog
	 */
	public FrmInProcessTransactionsINQ002(JDialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmInProcessTransactionsINQ002 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmInProcessTransactionsINQ002(JFrame aaParent)
	{
		super(aaParent);
		initialize();
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
				if (cbVehInqData)
				{
					(
						(
							VehicleInquiryData) caData)
								.setInProcsTransDataList(
						null);
				}
				else
				{
					(
						(
							TitleInProcessData) caData)
								.setInProcsTransDataList(
						null);
				}
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
	 * Return the ivjFrmInProcessTransactionsINQ002ContentPane1 property value
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmInProcessTransactionsINQ002ContentPane1()
	{
		if (ivjFrmInProcessTransactionsINQ002ContentPane1 == null)
		{
			try
			{
				ivjFrmInProcessTransactionsINQ002ContentPane1 =
					new JPanel();

				ivjFrmInProcessTransactionsINQ002ContentPane1.setName(
					"ivjFrmInProcessTransactionsINQ002ContentPane1");

				ivjFrmInProcessTransactionsINQ002ContentPane1
					.setLayout(
					null);

				ivjFrmInProcessTransactionsINQ002ContentPane1
					.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjFrmInProcessTransactionsINQ002ContentPane1
					.setMinimumSize(
					new Dimension(0, 0));

				ivjFrmInProcessTransactionsINQ002ContentPane1.add(
					getJScrollPane1(),
					null);

				ivjFrmInProcessTransactionsINQ002ContentPane1.add(
					getbtnPanel(),
					null);

				ivjFrmInProcessTransactionsINQ002ContentPane1.add(
					getstcLblDocNo(),
					null);
				ivjFrmInProcessTransactionsINQ002ContentPane1.add(
					getstcLblVIN(),
					null);
				ivjFrmInProcessTransactionsINQ002ContentPane1.add(
					getlblVIN(),
					null);
				ivjFrmInProcessTransactionsINQ002ContentPane1.add(
					getlblDocNo(),
					null);
				ivjFrmInProcessTransactionsINQ002ContentPane1.add(
					getstcLblWarning(),
					null);
				ivjFrmInProcessTransactionsINQ002ContentPane1.setSize(
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
		return ivjFrmInProcessTransactionsINQ002ContentPane1;
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
					gettblInProcessTransRecs());
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
	 * This method initializes ivjlblDocNo
	 * 
	 * @return JLabel
	 */
	private JLabel getlblDocNo()
	{
		if (ivjlblDocNo == null)
		{
			ivjlblDocNo = new JLabel();
			ivjlblDocNo.setSize(160, 20);
			ivjlblDocNo.setLocation(145, 42);
		}
		return ivjlblDocNo;
	}

	/**
	 * This method initializes ivjlblVIN
	 * 
	 * @return JLabel
	 */
	private JLabel getlblVIN()
	{
		if (ivjlblVIN == null)
		{
			ivjlblVIN = new JLabel();
			ivjlblVIN.setBounds(145, 22, 161, 20);

		}
		return ivjlblVIN;
	}

	/**
	 * This method initializes ivjstcLblDocNo
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblDocNo()
	{
		if (ivjstcLblDocNo == null)
		{
			ivjstcLblDocNo = new JLabel();
			ivjstcLblDocNo.setSize(90, 20);
			ivjstcLblDocNo.setText("Document No:");
			ivjstcLblDocNo.setLocation(47, 42);
			ivjstcLblDocNo.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return ivjstcLblDocNo;
	}

	/**
	 * This method initializes ivjstcLblVIN
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblVIN()
	{
		if (ivjstcLblVIN == null)
		{
			ivjstcLblVIN = new JLabel();
			ivjstcLblVIN.setSize(44, 20);
			ivjstcLblVIN.setText("VIN:");
			ivjstcLblVIN.setLocation(93, 22);
			ivjstcLblVIN.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		return ivjstcLblVIN;
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
			ivjstcLblWarning.setBounds(331, 22, 347, 20);
			ivjstcLblWarning.setText(WARNING_1_INPROCESS);
			ivjstcLblWarning.setForeground(java.awt.Color.black);
		}
		return ivjstcLblWarning;
	}

	/**
	 * Return the ivjtblInProcessTransRecs property value
	 * 
	 * @return RTSTable
	 */
	private RTSTable gettblInProcessTransRecs()
	{
		if (ivjtblInProcessTransRecs == null)
		{
			try
			{
				ivjtblInProcessTransRecs = new RTSTable();
				ivjtblInProcessTransRecs.setName(
					"ivjtblInProcessTransRecs");
				getJScrollPane1().setColumnHeaderView(
					ivjtblInProcessTransRecs.getTableHeader());
				getJScrollPane1().getViewport().setScrollMode(
					JViewport.BACKINGSTORE_SCROLL_MODE);
				ivjtblInProcessTransRecs.setAutoResizeMode(
					JTable.AUTO_RESIZE_OFF);
				ivjtblInProcessTransRecs.setShowVerticalLines(false);
				ivjtblInProcessTransRecs.setShowHorizontalLines(false);
				ivjtblInProcessTransRecs.setIntercellSpacing(
					new Dimension(0, 0));
				ivjtblInProcessTransRecs.setBounds(0, 0, 200, 200);
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
		return ivjtblInProcessTransRecs;
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
				getFrmInProcessTransactionsINQ002ContentPane1());
			setSize(771, 303);
			setName(FRM_NAME_INQ002);
			setContentPane(
				getFrmInProcessTransactionsINQ002ContentPane1());
		}
		catch (Throwable aeIVJEx)
		{
			handleException(aeIVJEx);
		}
		// user code begin {2}
		// user code end
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
			caData = aaData;
			Vector lvInProcsTrans = new Vector();
			if (aaData instanceof VehicleInquiryData)
			{
				lvInProcsTrans =
					((VehicleInquiryData) aaData)
						.getInProcsTransDataList();
				cbVehInqData = true;
			}
			else if (aaData instanceof TitleInProcessData)
			{
				lvInProcsTrans =
					((TitleInProcessData) aaData)
						.getInProcsTransDataList();
			}
			UtilityMethods.sort(lvInProcsTrans);
			InProcessTransactionData laData =
				(InProcessTransactionData) lvInProcsTrans.elementAt(0);
			getlblVIN().setText(laData.getVIN());
			getlblDocNo().setText(laData.getDocNo());
			setInProcessTransRecs();
			caTablePndgRecs.add(lvInProcsTrans);
			if (lvInProcsTrans.size() > 1)
			{
				getstcLblWarning().setText(WARNING_MULT_INPROCESS);
			}
		}
		gettblInProcessTransRecs().setRowSelectionInterval(0, 0);
	}

	/**
	 * This setups the table for In Process Transactions 
	 */
	public void setInProcessTransRecs()
	{
		ivjtblInProcessTransRecs.setModel(new TMINQ002());
		caTablePndgRecs =
			(TMINQ002) ivjtblInProcessTransRecs.getModel();

		// TRANSID
		TableColumn laTCTransId =
			ivjtblInProcessTransRecs.getColumn(
				ivjtblInProcessTransRecs.getColumnName(
					CommonConstant.INQ002_COL_TRANSID));
		laTCTransId.setPreferredWidth(125);

		// DATE
		TableColumn laTCDate =
			ivjtblInProcessTransRecs.getColumn(
				ivjtblInProcessTransRecs.getColumnName(
					CommonConstant.INQ002_COL_DATE));
		laTCDate.setPreferredWidth(68);

		// TRANSCD DESC
		TableColumn laTCTransCdDesc =
			ivjtblInProcessTransRecs.getColumn(
				ivjtblInProcessTransRecs.getColumnName(
					CommonConstant.INQ002_COL_TRANSCDDESC));
		laTCTransCdDesc.setPreferredWidth(290);

		// REGPLTNO
		TableColumn laTCRegPltNo =
			ivjtblInProcessTransRecs.getColumn(
				ivjtblInProcessTransRecs.getColumnName(
					CommonConstant.INQ002_COL_REGPLTNO));
		laTCRegPltNo.setPreferredWidth(70);

		// TRANSEMPID 
		TableColumn laTCEmpId =
			ivjtblInProcessTransRecs.getColumn(
				ivjtblInProcessTransRecs.getColumnName(
					CommonConstant.INQ002_COL_EMPID));
		laTCEmpId.setPreferredWidth(70);

		// OFCNAME
		TableColumn laTCOfcName =
			ivjtblInProcessTransRecs.getColumn(
				ivjtblInProcessTransRecs.getColumnName(
					CommonConstant.INQ002_COL_OFCNAME));
		laTCOfcName.setPreferredWidth(255);

		ivjtblInProcessTransRecs.setSelectionMode(
			ListSelectionModel.SINGLE_SELECTION);

		ivjtblInProcessTransRecs.init();

		laTCTransId.setCellRenderer(
			ivjtblInProcessTransRecs.setColumnAlignment(RTSTable.LEFT));
		laTCDate.setCellRenderer(
			ivjtblInProcessTransRecs.setColumnAlignment(RTSTable.LEFT));
		laTCTransCdDesc.setCellRenderer(
			ivjtblInProcessTransRecs.setColumnAlignment(RTSTable.LEFT));
		laTCRegPltNo.setCellRenderer(
			ivjtblInProcessTransRecs.setColumnAlignment(RTSTable.LEFT));
		laTCEmpId.setCellRenderer(
			ivjtblInProcessTransRecs.setColumnAlignment(RTSTable.LEFT));
		laTCOfcName.setCellRenderer(
			ivjtblInProcessTransRecs.setColumnAlignment(RTSTable.LEFT));
		getJScrollPane1().setHorizontalScrollBarPolicy(
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		getJScrollPane1().setVerticalScrollBarPolicy(
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
