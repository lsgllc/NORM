package com.txdot.isd.rts.client.funds.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDateField;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;

import com.txdot.isd.rts.services.data.AdministrationLogData;
import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;

/*
 *
 * FrmReportDateSelectionFUN012.java
 *
 * (c) Texas Department of Transportation 2003
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * K Harrell 	O2/06/2003  Added date validation 
 * 							defect 5381
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							delete implements KeyListener
 * 							delete keyPressed()
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	09/17/2008	Setup TODAY without time
 * 							delete getBuilderData() 
 * 							modify TODAY 
 * 							defect 9826 Ver Defect_POS_B
 * K Harrell	01/07/2009	Standardize Fees implementation for date
 * 							handling. 
 * 							refactor TODAY to caToday
 * 							refactor YESTERDAY to caYesterday 
 * 							defect 9826 Ver Defect_POS_D 
 * K Harrell	06/08/2009	Use ErrorsConstant. Additional Class Cleanup.
 * 							modify actionPerformed().  
 * 							defect 9943 Ver Defect_POS_F 
 * K Harrell	03/01/2010	Create AdminLogData entry for assignment 		
 * 							 when Rerun Countywide. 
 * 							add getAdminLogData() 
 * 							modify actionPerformed() 
 * 							defect 10168 Ver POS_640 
 * ---------------------------------------------------------------------
 */

/**
 * Screen prompts user to enter date to rerun the county wide report.
 *
 * @version	POS_640			03/01/2010
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */
public class FrmReportDateSelectionFUN012
	extends RTSDialogBox
	implements ActionListener
{
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmReportDateSelectionFUN012ContentPane1 = null;
	private JLabel ivjstcLblEnterTheReportDate = null;
	private RTSDateField ivjtxtDate = null;

	// defect 9826
	private static RTSDate caToday =
		new RTSDate(
			RTSDate.YYYYMMDD,
			RTSDate.getCurrentDate().getYYYYMMDDDate());

	private static RTSDate caYesterday = caToday.add(RTSDate.DATE, -1);
	// end defect 9826

	// Constants 	
	private final static String ENTER_RPT_DT = "Enter the Report Date:";
	private final static String TITLE_FUN012 =
		"Report Date Selection     FUN012";

	/**
	 * main entrypoint, starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmReportDateSelectionFUN012 laFrmReportDateSelectionFUN012;
			laFrmReportDateSelectionFUN012 =
				new FrmReportDateSelectionFUN012();
			laFrmReportDateSelectionFUN012.setModal(true);
			laFrmReportDateSelectionFUN012
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				}
			});
			laFrmReportDateSelectionFUN012.show();
			java.awt.Insets laInsets =
				laFrmReportDateSelectionFUN012.getInsets();
			laFrmReportDateSelectionFUN012.setSize(
				laFrmReportDateSelectionFUN012.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmReportDateSelectionFUN012.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmReportDateSelectionFUN012.setVisibleRTS(true);
		}
		catch (Throwable aeException)
		{
			System.err.println(
				"Exception occurred in main() of util.JDialogTxDot");
			aeException.printStackTrace(System.out);
		}
	}

	/**
	 * FrmReportDateSelectionFUN012 constructor comment.
	 */
	public FrmReportDateSelectionFUN012()
	{
		super();
		initialize();
	}

	/**
	 * FrmReportDateSelectionFUN012 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmReportDateSelectionFUN012(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmReportDateSelectionFUN012 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmReportDateSelectionFUN012(JFrame aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * Invoked when an action occurs.
	 * 
	 * @param aaAE java.awt.event.ActionEvent
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

			RTSException leEx = new RTSException();

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				RTSDate laRerunCountyDate = gettxtDate().getDate();

				// Initalize valid date as 10 days prior to today
				RTSDate laValidDate = caToday.add(RTSDate.DATE, -10);

				// defect 9943 
				// Use ErrorsConstant 
				// defect 5381
				// Date Validation
				if (!gettxtDate().isValidDate())
				{
					leEx.addException(
					//new RTSException(733),
					new RTSException(
						ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
						gettxtDate());
				}
				else
				{
					// Throw exception if date is not within valid range
					if (laRerunCountyDate.compareTo(laValidDate) == -1
						|| laRerunCountyDate.compareTo(caYesterday) == 1)
					{
						leEx.addException(
						//new RTSException(733),
						new RTSException(
							ErrorsConstant.ERR_NUM_DATE_NOT_VALID),
							gettxtDate());
					}
				}
				// end defect 5381
				// end defect 9943 

				if (leEx.isValidationError())
				{
					leEx.displayError(this);
					leEx.getFirstComponent().requestFocus();
					return;
				}

				// Create FundsData object to pass date, office,
				// and substation
				FundsData laFundsData = new FundsData();
				laFundsData.setSummaryEffDate(laRerunCountyDate);

				// defect 10168
				// Unnecessary to assign OfcIssuanceNo, SubstaId 
				// laFundsData.setOfficeIssuanceNo(
				//	SystemProperty.getOfficeIssuanceNo());
				//laFundsData.setSubStationId(
				//	SystemProperty.getSubStationId());

				laFundsData.setAdminLogData(getAdminLogData());
				// end defect 10168 

				getController().processData(
					AbstractViewController.ENTER,
					laFundsData);
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
				RTSHelp.displayHelp(RTSHelp.FUN012);
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

		laAdminLogData.setEntity(
			CommonConstant.TXT_ADMIN_LOG_COUNTY_WIDE);
		laAdminLogData.setAction(
			CommonConstant.TXT_ADMIN_LOG_RERUN_REPORT);

		laAdminLogData.setEntityValue(
			gettxtDate().getDate().getMMDDYY());

		return laAdminLogData;
	}

	/**
	 * Return the ivjButtonPanel1 property value.
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
				ivjButtonPanel1.setLayout(new java.awt.GridBagLayout());
				ivjButtonPanel1.setBounds(36, 108, 287, 64);
				ivjButtonPanel1.setMinimumSize(
					new java.awt.Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.addActionListener(this);
				ivjButtonPanel1.setAsDefault(this);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjButtonPanel1;
	}

	/**
	 * Return the ivjFrmReportDateSelectionFUN012ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmReportDateSelectionFUN012ContentPane1()
	{
		if (ivjFrmReportDateSelectionFUN012ContentPane1 == null)
		{
			try
			{
				ivjFrmReportDateSelectionFUN012ContentPane1 =
					new javax.swing.JPanel();
				ivjFrmReportDateSelectionFUN012ContentPane1.setName(
					"ivjFrmReportDateSelectionFUN012ContentPane1");
				ivjFrmReportDateSelectionFUN012ContentPane1.setLayout(
					null);
				ivjFrmReportDateSelectionFUN012ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(2147483647, 2147483647));
				ivjFrmReportDateSelectionFUN012ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(350, 200));
				getFrmReportDateSelectionFUN012ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmReportDateSelectionFUN012ContentPane1().add(
					getstcLblEnterTheReportDate(),
					getstcLblEnterTheReportDate().getName());
				getFrmReportDateSelectionFUN012ContentPane1().add(
					gettxtDate(),
					gettxtDate().getName());
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjFrmReportDateSelectionFUN012ContentPane1;
	}

	/**
	 * Return the ivjstcLblEnterTheReportDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEnterTheReportDate()
	{
		if (ivjstcLblEnterTheReportDate == null)
		{
			try
			{
				ivjstcLblEnterTheReportDate = new javax.swing.JLabel();
				ivjstcLblEnterTheReportDate.setSize(124, 16);
				ivjstcLblEnterTheReportDate.setName(
					"ivjstcLblEnterTheReportDate");
				ivjstcLblEnterTheReportDate.setText(ENTER_RPT_DT);
				ivjstcLblEnterTheReportDate.setMaximumSize(
					new java.awt.Dimension(121, 14));
				ivjstcLblEnterTheReportDate.setMinimumSize(
					new java.awt.Dimension(121, 14));
				ivjstcLblEnterTheReportDate.setHorizontalAlignment(0);
				ivjstcLblEnterTheReportDate.setLocation(119, 32);
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
			}
		}
		return ivjstcLblEnterTheReportDate;
	}

	/**
	 * Return the ivjtxtDate property value.
	 * 
	 * @return RTSDateField
	 */
	private RTSDateField gettxtDate()
	{
		if (ivjtxtDate == null)
		{
			try
			{
				ivjtxtDate = new RTSDateField();
				ivjtxtDate.setName("ivjtxtDate");
				ivjtxtDate.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtDate.setBounds(143, 71, 75, 20);
				// user code begin {1}
				// user code end
			}
			catch (Throwable aeIvjEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIvjEx);
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
	private void initialize()
	{
		try
		{
			setName("FrmReportDateSelectionFUN012");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(366, 197);
			setTitle(TITLE_FUN012);
			setContentPane(
				getFrmReportDateSelectionFUN012ContentPane1());
			// user code begin {1}
			// user code end
		}
		catch (Throwable aeIvjEx)
		{
			handleException(aeIvjEx);
		}
		// user code begin {2}
		// user code end
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
			//Set Yesterday's Date in Date Text Box	
			gettxtDate().setDate(caYesterday);
		}
		catch (NullPointerException aeNPEx)
		{
			RTSException leEx =
				new RTSException(RTSException.FAILURE_MESSAGE, aeNPEx);
			leEx.displayError(this);
			leEx = null;
		}
	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
