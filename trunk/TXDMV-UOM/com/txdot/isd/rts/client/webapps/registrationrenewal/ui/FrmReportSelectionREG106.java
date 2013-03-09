package com.txdot.isd.rts.client.webapps.registrationrenewal.ui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com
	.txdot
	.isd
	.rts
	.services
	.webapps
	.util
	.constants
	.RegRenProcessingConstants;

/*
 *
 * FrmReportSelectionREG106.java
 *
 * (c) Texas Department of Transportation 2001 
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Brown 		10/23/2002  (putting and accessing Phase 2 error msgs in
 * 							rts_err_msgs).
 *                          modify doProcessData(), processData()
 * 							defect 4205
 * B Brown 		11/20/2002  Added hot keys to the report date to and
 * 							from range, by adding the underline
 * 							character in the displaymnemonic box in the
 * 							properties in VCE, and changing the
 * 							setButtonGroup method to add these
 * 							statements whem either Internet trans recon
 * 							or Vendor payment reports are chosen:
 * 							getJLabel1().setLabelFor(getRTSFromDate());
 *                     	 	getJLabel11().setLabelFor(getRTSToDate());
 * 							defect 4542
 * B Brown		11/20/2002	Add these statements when either Internet
 *                          trans recon or Vendor payment reports are
 * 							chosen: getJLabel1().setLabelFor(
 * 							getRTSFromDate()); getJLabel11().setLabelFor
 * 							(getRTSToDate()); so hot keys are enabled
 * 							only when the reports that enable date are
 * 							chosen.
 * 							defect 4542
 * B Brown 		02/20/2003  commented out // com.txdot.isd.rts.services.
 * 							util.UtilityMethods.beep(); in method
 * 							process data so there is not a beep - its
 * 							not an RTS error.
 * 							defect 5371     
 * Jeff S.		02/17/2005	Code Cleanup for Java 1.4.2 upgrade
 * S Johnston				modify actionPerformed(), handleException(),
 * 								main()
 *							defect 7889 ver 5.2.3
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7889 Ver 5.2.3 
 * Jeff S.		07/12/2005	Code Cleanup for java 1.4.2.  Removed inner
 * 							classes and moved code into itemStateChanged
 * 							method. Added ECH to Frame to make it like
 * 							the rest of RTS. Added code to handle arrow
 * 							buttons between radio group.
 * 							modify setButtonGroup()
 * 							add itemStateChanged(), getButtonPanel1(),
 * 								keyPressed()
 * 							deprecate getBuilderData()
 * 							defect 7889 Ver 5.2.3
 * B Hargrove	08/15/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	11/01/2005	Modify from "Vendor Payment Report" to 
 * 							"Vendor Payment" 
 * 							deleted class variables cbCheckPrivilege,
 * 							 caData, replaced with local variables
 * 							modify actionPerformed(),processData()
 * 							modify RADIO_VENDOR_TAG
 * 							defect 8379 Ver 5.2.3 
 * Jeff S.		01/05/2006	Changed ButtonGroup to RTSButtonGroup which
 * 							handles all arrowing.  Added Insurance and
 * 							address btns to an RTSButtonGroup so that
 * 							it could handle the arrowing through these
 * 							buttons.
 * 							remove keyPressed()
 * 							modify setButtonGroup()
 * 							defect 7889 Ver 5.2.3
 * K Harrell	02/19/2007	delete getBuilderData()
 * 							defect 9085 Ver Special Plates  
 * Min Wang  	06/11/2007	modify fields and screen.
 * 							defect 8768 Ver Special Plates  
 * K Harrell	02/09/2009	Add code for Internet Deposit Reconciliation
 * 							Reports.  Restructure class to standards. 
 * 							defect 9935 Ver Defect_POS_D  
 * K Harrell	02/12/2009	Default focus on BeginDate if CIS Branch.
 * 							modify setData()
 * 							defect 9935 Ver Defect_POS_D 
 * K Harrell	02/19/2009	Error if Begin Date prior to 1/14/2009. 
 * 							modify validateData()
 * 							defect 9935 Ver Defect_POS_D
 * K Harrell	06/15/2009	Reset Dates on radio selection.  Additional
 * 							screen cleanup. 
 * 							add ciCurrentAMDate, ciMaxDepositReconDate,
 * 							 ciMaxVendorPaymentDate
 * 							add resetDates()
 * 							delete cbValidDate  
 * 							modify caToday, itemStateChanged(),setData(), 
 * 							 setDataToDataObject(), validateData(), 
 * 							 getradioAddressChange(), 
 * 							 getradioInternetDepositReconReport(),
 * 							 getradioInternetTransactionReport(),
 * 							 getradioVendorPaymentReport()
 * 							defect 10011 Ver Defect_POS_F
 * K Harrell	08/25/2009	Implement new AdminLogData constructor
 * 							Implement ReportSearchData.initForClient()
 * 							modify getAdminLogData(), setDataToDataObject()
 * 							defect 8628 Ver Defect_POS_F 
 * K Harrell	09/21/2009	Do not reassign dates for DepositRecon, 
 * 							  Vendor Payment.(Rollback of 10011)
 * 							delete ciMaxDepositReconAMDate, 
 * 							 ciMaxVendorPaymentAMDate 
 * 							delete resetDates(), itemStateChanged()  
 * 							modify validateDate()  
 * 							defect 10223 Ver Defect_POS_F
 * K Harrell	02/27/2010	Remove Address Change Report 
 * 							delete ivjradioAddressChange, get method
 * 							delete RADIO_ADDRESS_TAG, 
 * 							  ITRNT_ADDR_CHG_ACTION
 * 							modify getJPanel1(), setData(), 
 * 							 validateData()
 * 							defect 10387 Ver POS_640  
 * K Harrell	08/12/2011	Disable Vendor Payment &  
 * 							 Deposit Recon Report radio buttons.
 * 							modify getreadioInternetDepositReconReport(), 
 * 							 getradioVendorPaymentReport()
 * 							defect 10975 Ver 6.8.1  
 * ---------------------------------------------------------------------
 */

/**
 * Internet report selection screen.
 *
 * @version	6.8.1  			08/12/2011
 * @author	George Donoso
 *          K Harrell 
 * <br>Creation Date:		11/26/2001 09:28:14 
 */
public class FrmReportSelectionREG106
	extends RTSDialogBox
	implements ActionListener
{
	private RTSButtonGroup caBtnGroup = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JLabel ivjstclbBeginDate = null;
	private JLabel ivjstclblEndDate = null;
	private JPanel ivjJPanel1 = null;
	private JRadioButton ivjradioInternetTransactionReport = null;
	private JRadioButton ivjradioVendorPayment = null;
	private JRadioButton ivjradioInternetDepositReconReport = null;
	private JPanel ivjJPanel = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private RTSDateField ivjtxtBeginDate = null;
	private RTSDateField ivjtxtEndDate = null;

	// int
	private int ciReportId = 0;

	// String 
	private String csAction = CommonConstant.STR_SPACE_EMPTY;

	// defect 10011
	// private boolean cbValidDate = true; 
	private RTSDate caToday =
		new RTSDate(
			RTSDate.YYYYMMDD,
			RTSDate.getCurrentDate().getYYYYMMDDDate());
	private int ciCurrentAMDate = caToday.getAMDate();
	// end defect 10011
	// defect 10223 
	// private int ciMaxDepositReconAMDate = ciCurrentAMDate - 2;
	// private int ciMaxVendorPaymentAMDate = ciCurrentAMDate - 1;
	// end defect 10223 

	// Constants 
	// int 
	private static final int TRANS_RECON_MAX_DAYS_AGO = 30;
	private static final int DEPOSIT_RECON_MAX_DAYS = 31;

	// String 
	// For Logging to Admin_Log 
	private static final String ADMIN_LOG_ENTITY = "ItrntRpt";
	private static final String ITRNT_DEPOSIT_RECON_ACTION =
		"DepstRecon";
	private static final String ITRNT_TRANS_ACTION = "TransRecon";
	private static final String ITRNT_VENDOR_PAYMENT_ACTION =
		"VndrPymnt";
	private static final String BEGIN_DATE = "Begin Date:";
	private static final String END_DATE = "End Date:";
	private static final String RADIO_TRANS_TAG =
		"Internet Transaction Reconciliation Detail";
	private static final String RADIO_VENDOR_TAG = "Vendor Payment";
	private static final String RADIO_DEPOSIT_RECON_TAG =
		"Internet Deposit Reconciliation";
	private static final String DATE_RANGE_TAG = "Enter Date Range:";

	private static final String FRM_TITLE =
		"Report Selection    REG106";
	private static final String EXCEPTION_IN_MAIN_MSG =
		"Exception occurred in main() of "
			+ "com.txdot.isd.rts.client.webapps.registrationrenewal.ui";

	// defect 10387 
	// private JRadioButton ivjradioAddressChange = null;
	// private static final String RADIO_ADDRESS_TAG =
	//		"Renewal Recipient Address Change";
	// private static final String ITRNT_ADDR_CHG_ACTION = "AddrChng";
	// end defect 10387 

	/**
	 * FrmReportSelectionREG106 constructor
	 */
	public FrmReportSelectionREG106()
	{
		super();
		initialize();
	}

	/**
	 * FrmReportSelectionREG106 constructor
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmReportSelectionREG106(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmReportSelectionREG106 constructor
	 * 
	 * @param aaOwner Frame
	 */
	public FrmReportSelectionREG106(Frame aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * main entrypoint starts this part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmReportSelectionREG106 laFrmReportSelection;
			laFrmReportSelection = new FrmReportSelectionREG106();
			laFrmReportSelection.setModal(true);
			laFrmReportSelection.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent laWE)
				{
					System.exit(0);
				};
			});
			laFrmReportSelection.show();
			Insets laInsets = laFrmReportSelection.getInsets();
			laFrmReportSelection.setSize(
				laFrmReportSelection.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmReportSelection.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmReportSelection.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(EXCEPTION_IN_MAIN_MSG);
			aeEx.printStackTrace(System.out);
		}
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

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (!validateData())
				{
					return;
				}

				getController().processData(
					AbstractViewController.ENTER,
					setDataToDataObject());
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
				RTSHelp.displayHelp(RTSHelp.REG106);
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
	 * @param asAction 
	 * @return AdministrationLogData
	 */
	private AdministrationLogData getAdminLogData()
	{
		// defect 8628 
		AdministrationLogData laAdminLogData =
			new AdministrationLogData(ReportConstant.CLIENT);
		// end defect 8628 
		laAdminLogData.setEntity(ADMIN_LOG_ENTITY);
		laAdminLogData.setAction(csAction);
		laAdminLogData.setEntityValue(
			gettxtBeginDate().getDate().getMMDDYY()
				+ CommonConstant.STR_DASH
				+ gettxtEndDate().getDate().getMMDDYY());
		return laAdminLogData;
	}

	/**
	 * Return the ivjButtonPanel1 property value.
	 * This is the ECH or Enter, Cancel, Help panel.
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
				ivjButtonPanel1.setBounds(84, 267, 217, 35);
				ivjButtonPanel1.setAsDefault(this);
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the ivjstclbBeginDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblBeginDate()
	{
		if (ivjstclbBeginDate == null)
		{
			try
			{
				ivjstclbBeginDate = new JLabel();
				ivjstclbBeginDate.setSize(64, 20);
				ivjstclbBeginDate.setLocation(80, 26);
				// user code begin {1} 
				ivjstclbBeginDate.setDisplayedMnemonic(KeyEvent.VK_B);
				ivjstclbBeginDate.setText(BEGIN_DATE);
				ivjstclbBeginDate.setLabelFor(gettxtBeginDate());
				ivjstclbBeginDate.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstclbBeginDate.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end 
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjstclbBeginDate;
	}

	/**
	 * Return the ivjstclblEndDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstclblEndDate()
	{
		if (ivjstclblEndDate == null)
		{
			try
			{
				ivjstclblEndDate = new JLabel();
				ivjstclblEndDate.setBounds(91, 55, 53, 20);
				// user code begin {1} 
				ivjstclblEndDate.setDisplayedMnemonic(KeyEvent.VK_E);
				ivjstclblEndDate.setText(END_DATE);
				ivjstclblEndDate.setLabelFor(gettxtEndDate());
				ivjstclblEndDate.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				ivjstclblEndDate.setHorizontalTextPosition(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjstclblEndDate;
	}

	/**
	 * Return the ivjJPanel property value
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel()
	{
		if (ivjJPanel == null)
		{
			ivjJPanel = new javax.swing.JPanel();
			ivjJPanel.setLayout(null);
			ivjJPanel.add(getstclblBeginDate(), null);
			ivjJPanel.add(gettxtBeginDate(), null);
			ivjJPanel.add(getstclblEndDate(), null);
			ivjJPanel.add(gettxtEndDate(), null);
			ivjJPanel.setBounds(33, 167, 325, 93);
			getJPanel().setBorder(
				new TitledBorder(new EtchedBorder(), DATE_RANGE_TAG));
		}
		return ivjJPanel;
	}

	/**
	 * Return the ivjJPanel1 property value.
	 * This is the radion button panel.
	 * 
	 * @return JPanel
	 */
	private JPanel getJPanel1()
	{
		if (ivjJPanel1 == null)
		{
			try
			{
				ivjJPanel1 = new JPanel();
				ivjJPanel1.setLayout(null);
				ivjJPanel1.setBounds(33, 18, 325, 127);
				// user code begin {1}
				// defect 10387 
				// getJPanel1().add(
				//	getradioAddressChange(),
				//	getradioAddressChange().getName());
				// end defect 10387 
				getJPanel1().add(
					getradioInternetTransactionReport(),
					getradioInternetTransactionReport().getName());
				getJPanel1().add(
					getradioVendorPaymentReport(),
					getradioVendorPaymentReport().getName());
				getJPanel1().add(
					getradioInternetDepositReconReport(),
					getradioInternetDepositReconReport().getName());
				ivjJPanel1.add(
					getradioInternetDepositReconReport(),
					null);
				getJPanel1().setBorder(
					new TitledBorder(
						new EtchedBorder(),
						InventoryConstant.TXT_SELECT_ONE_COLON));
				setButtonGroup();
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjJPanel1;
	}

	//	/**
	//	 * Return the ivjradioAddressChange property value.
	//	 * 
	//	 * @return JRadioButton
	//	 */
	//	private JRadioButton getradioAddressChange()
	//	{
	//		if (ivjradioAddressChange == null)
	//		{
	//			try
	//			{
	//				ivjradioAddressChange = new JRadioButton();
	//				ivjradioAddressChange.setSize(271, 24);
	//				ivjradioAddressChange.setLocation(56, 29);
	//				// user code begin {1} 
	//				ivjradioAddressChange.setMnemonic(KeyEvent.VK_R);
	//				ivjradioAddressChange.setText(RADIO_ADDRESS_TAG);
	//				// user code end
	//			}
	//			catch (Throwable aeIVJEx)
	//			{
	//				// user code begin {2}
	//				handleException(aeIVJEx);
	//				// user code end
	//			}
	//		}
	//		return ivjradioAddressChange;
	//	}

	/**
	 * Return the ivjradioInternetDepositReconReport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioInternetDepositReconReport()
	{
		if (ivjradioInternetDepositReconReport == null)
		{
			try
			{
				ivjradioInternetDepositReconReport =
					new javax.swing.JRadioButton();
				ivjradioInternetDepositReconReport.setSize(271, 24);
				ivjradioInternetDepositReconReport.setLocation(33, 57);
				// user code begin {1} 
				ivjradioInternetDepositReconReport.setText(
					RADIO_DEPOSIT_RECON_TAG);
				ivjradioInternetDepositReconReport.setMnemonic(
					KeyEvent.VK_D);
				// defect 10975 
				ivjradioInternetDepositReconReport.setEnabled(false);
				// end defect 10975 
				// user code end 
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjradioInternetDepositReconReport;
	}

	/**
	 * Return the ivjradioInternetTransactionReport property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioInternetTransactionReport()
	{
		if (ivjradioInternetTransactionReport == null)
		{
			try
			{
				ivjradioInternetTransactionReport = new JRadioButton();
				ivjradioInternetTransactionReport.setSize(271, 24);
				ivjradioInternetTransactionReport.setLocation(33, 28);
				// user code begin {1} 
				ivjradioInternetTransactionReport.setMnemonic(
					KeyEvent.VK_I);
				ivjradioInternetTransactionReport.setText(
					RADIO_TRANS_TAG);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjradioInternetTransactionReport;
	}

	/**
	 * Return the ivjradioVendorPayment property value.
	 * 
	 * @return JRadioButton
	 */
	private JRadioButton getradioVendorPaymentReport()
	{
		if (ivjradioVendorPayment == null)
		{
			try
			{
				ivjradioVendorPayment = new JRadioButton();
				ivjradioVendorPayment.setSize(271, 24);
				ivjradioVendorPayment.setLocation(33, 86);
				// user code begin {1}
				ivjradioVendorPayment.setMnemonic(KeyEvent.VK_V);
				ivjradioVendorPayment.setText(RADIO_VENDOR_TAG);
				// defect 10975 
				ivjradioVendorPayment.setEnabled(false);
				// end defect 10975 
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjradioVendorPayment;
	}

	/**
	 * Return the RTSDialogBoxContentPane property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getRTSDialogBoxContentPane()
	{
		if (ivjRTSDialogBoxContentPane == null)
		{
			try
			{
				ivjRTSDialogBoxContentPane = new JPanel();
				ivjRTSDialogBoxContentPane.setName(
					"ivjRTSDialogBoxContentPane");
				ivjRTSDialogBoxContentPane.setLayout(null);
				getRTSDialogBoxContentPane().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getRTSDialogBoxContentPane().add(
					getJPanel1(),
					getJPanel1().getName());
				ivjRTSDialogBoxContentPane.add(getJPanel(), null);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the ivjtxtBeginDate property value.
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
				ivjtxtBeginDate.setBounds(151, 26, 74, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjtxtBeginDate;
	}

	/**
	 * Return the ivjtxtEndDate property value.
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
				ivjtxtEndDate.setBounds(151, 55, 74, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				handleException(aeIVJEx);
				// user code end
			}
		}
		return ivjtxtEndDate;
	}

	/**
	 * Called whenever the frame throws an exception whle trying to
	 * initialize.
	 * 
	 * @param aeEx Throwable
	 */
	private void handleException(Throwable aeEx)
	{
		RTSException leRTSEx =
			new RTSException(RTSException.JAVA_ERROR, (Exception) aeEx);
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
			// user code end
			setName("FrmReportSelectionREG106");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(400, 335);
			setTitle(FRM_TITLE);
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
	 * Set Data To Data Object
	 *
	 * @return Vector
	 */
	private Vector setDataToDataObject()
	{
		ReportSearchData laRptSearchData = new ReportSearchData();

		// defect 8628 
		laRptSearchData.initForClient(ReportConstant.ONLINE);
		// end defect 8628 
		RTSDate laBeginDate = gettxtBeginDate().getDate();
		RTSDate laEndDate = gettxtEndDate().getDate();
		laRptSearchData.setKey2(laBeginDate.getYYYYMMDDDate() + "");
		laRptSearchData.setKey3(laEndDate.getYYYYMMDDDate() + "");
		laRptSearchData.setDate1(laBeginDate);
		laRptSearchData.setDate2(laEndDate);
		laRptSearchData.setIntKey4(ciReportId);

		// defect 10011 
		laRptSearchData.setNextScreen(
			ReportConstant.RPR000_NEXT_SCREEN_CANCEL);
		// end defect 10011 

		Vector lvData = new Vector();
		lvData.add(laRptSearchData);
		lvData.add(getAdminLogData());
		return lvData;
	}

	/**
	 * Sets up the radion button group.
	 */
	private void setButtonGroup()
	{
		if (caBtnGroup == null)
		{
			caBtnGroup = new RTSButtonGroup();
		}
		//caBtnGroup.add(getradioAddressChange());
		caBtnGroup.add(getradioInternetTransactionReport());
		caBtnGroup.add(getradioInternetDepositReconReport());
		caBtnGroup.add(getradioVendorPaymentReport());
	}

	/**
	 * All subclasses must implement this method - it sets the data on
	 * the screen and is how the controller relays information to the
	 * view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// defect 10387 
		// Remove Internet Address Change Report
		getradioInternetTransactionReport().setSelected(true);
		gettxtBeginDate().setDate(caToday);
		gettxtEndDate().setDate(caToday);
		// end defect 10387 
	}

	/**
	 * Validate Data
	 *
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;
		RTSException leRTSEx = new RTSException();
		int liBeginAMDate = 0;
		int liEndAMDate = 0;

		// defect 10011/10223 
		// Continue validations after 1st set so all fields in error 
		// displayed in red.

		// Common Date Validations for all Reports 
		// Invalid Begin Date 
		if (!gettxtBeginDate().isValidDate())
		{
			leRTSEx.addException(
				new RTSException(ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
				gettxtBeginDate());
			lbReturn = false;
		}
		else
		{
			liBeginAMDate = gettxtBeginDate().getDate().getAMDate();
			if (liBeginAMDate > ciCurrentAMDate)
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
					gettxtBeginDate());
			}
		}

		// End Date Set to Current Date if Empty 
		if (!gettxtEndDate().isValidDate())
		{
			// OK if Empty; Set to Current Date 
			if (gettxtEndDate().isDateEmpty())
			{
				gettxtEndDate().setDate(new RTSDate());
				liEndAMDate = ciCurrentAMDate;
			}
			else
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
					gettxtEndDate());
				lbReturn = false;
			}
		}
		else
		{
			liEndAMDate = gettxtEndDate().getDate().getAMDate();

			if (liEndAMDate > ciCurrentAMDate
				|| (liBeginAMDate != 0
					&& liBeginAMDate <= ciCurrentAMDate
					&& liBeginAMDate > liEndAMDate))
			{
				leRTSEx.addException(
					new RTSException(
						ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
					gettxtEndDate());
			}
		}

		// Further Validation according to Report Selection
		//     Data Assignment if valid dates for report.    
		if (lbReturn)
		{
			// defect 10387 
			// if (getradioAddressChange().isSelected())
			// {
			//  	ciReportId =
			//		RegRenProcessingConstants.GET_ADDR_CHANGE_RPT;
			//		csAction = ITRNT_ADDR_CHG_ACTION;
			//	}
			//	else 
			// end defect 10387 
			if (getradioInternetTransactionReport().isSelected())
			{
				ciReportId = RegRenProcessingConstants.GET_TRANS_RPT;
				csAction = ITRNT_TRANS_ACTION;

				// Over 30 days ago  
				if (ciCurrentAMDate - liBeginAMDate
					> TRANS_RECON_MAX_DAYS_AGO)
				{
					leRTSEx.addException(
						new RTSException(
							ErrorsConstant
								.ERR_NUM_MORE_THAN_30_DAYS_IN_PAST),
						gettxtBeginDate());
				}
			}
			else
			{
				if (getradioInternetDepositReconReport().isSelected())
				{
					ciReportId =
						RegRenProcessingConstants.GET_DEPOSIT_RECON_RPT;
					csAction = ITRNT_DEPOSIT_RECON_ACTION;

					// BEGIN DATE 
					// Must be  >= 1/14/2009
					if (liBeginAMDate
						< RegRenProcessingConstants
							.MIN_DEPOSIT_RECON_RPT_AMDATE)
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_BEGIN_DEPOSIT_DATE_TOO_EARLY),
							gettxtBeginDate());
					}

					// END DATE 
					// Over 31 Days Max Range 
					if (liEndAMDate - liBeginAMDate + 1
						> DEPOSIT_RECON_MAX_DAYS)
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_DATE_RANGE_INVALID),
							gettxtEndDate());
					}
				}
				else if (getradioVendorPaymentReport().isSelected())
				{
					ciReportId =
						RegRenProcessingConstants.GET_VENDOR_RPT;

					csAction = ITRNT_VENDOR_PAYMENT_ACTION;

					// Begin Date != End Date   
					if (liBeginAMDate != liEndAMDate)
					{
						leRTSEx.addException(
							new RTSException(
								ErrorsConstant
									.ERR_NUM_DATE_RANGE_TOO_LARGE),
							gettxtEndDate());
					}
				}
			}
		}
		// end defect 10011/10223 
		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
	}

} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"