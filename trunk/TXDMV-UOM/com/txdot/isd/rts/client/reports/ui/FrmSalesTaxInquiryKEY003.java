package com.txdot.isd.rts.client.reports.ui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.txdot.isd.rts.client.general.ui.AbstractViewController;
import com.txdot.isd.rts.client.general.ui.ButtonPanel;
import com.txdot.isd.rts.client.general.ui.RTSDialogBox;
import com.txdot.isd.rts.client.general.ui.RTSInputField;

import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.MFLogError;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.ScreenConstant;

/*
 * FrmSalesTaxInquiryKEY003.java
 *
 * (c) Texas Department of Transportation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * Min Wang		11/24/2004	Fix the : mark for the year on the key003
 *							modify via VCE  - resize stcLblReportYear
 *							defect 7526 Ver 5.2.2
 * B Hargrove	04/29/2005	chg '/**' to '/*' to begin prolog.
 * 							defect 7896 Ver 5.2.3 
 * S Johnston	06/16/2005	ButtonPanel now handles the arrow keys
 * 							inside of its keyPressed method
 * 							delete keyPressed
 * 							modify getButtonPanel1
 * 							defect 8240 Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify handleException(), main(), setData(), 
 * 							validYear()
 *							defect 7896 Ver 5.2.3
 * B Hargrove	08/12/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	06/09/2009	Throw Error if Mo/Yr > Current Mo/Yr;
 * 							Constants for Horizontal Alignment, 
 * 							 RTSInputField type.   
 * 							Use RTSDate to determine last day of month
 * 							vs. hardcoding. Additional Class cleanup. 
 * 							add ciMonth, ciYear, caCurrentDate,
 * 							 COUNTY_NO,ENTER_THE_FOLLOWING,
 * 							 REPORT_MONTH, YEAR, LAST_DAY_OF_MONTH
 * 							add validateData(), isValidMonth(),
 * 							 isValidYear(), setDataToDataObject(),
 * 							 getLastDate()
 * 							add ivjlblCountyNo, ivjtxtMonth, ivjtxtYear,
 * 							 ivjstcLblEnterTheFollowing, get methods. 
 * 							delete ivjRTSInputField2, ivjRTSInputField3, 
 * 							 ivjLabel1, ivjstcLblEnterthefollowing,
 * 							 get methods
 * 							delete getBuilderData(), validMonth(), 
 * 							 validYear() 
 * 							modify actionPerformed(), setData(), 
 * 							 initialize()  
 * 							defect 10014 Ver Defect_POS_F
 * K Harrell	08/25/2009	Implement ReportSearchData.initForClient()
 * 							modify setDataToDataObject() 
 * 							defect 8628 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */
/**
 * Frame KEY003 is used for Sales Tax Allocation Report.
 *
 * @version	Defect_POS_F	08/25/2009
 * @author	Administrator
 * <br>Creation Date:		06/21/2001 10:26:42
 */
public class FrmSalesTaxInquiryKEY003
	extends RTSDialogBox
	implements ActionListener
{
	private JLabel ivjstcLblCountyNo = null;
	private JLabel ivjstcLblReportMonth = null;
	private JLabel ivjstcLblReportYear = null;
	private ButtonPanel ivjButtonPanel1 = null;
	private JPanel ivjFrmSalesTaxInquiryKEY003ContentPane1 = null;

	// defect 10014
	private JLabel ivjlblCountyNo = null;
	private JLabel ivjstcLblEnterTheFollowing = null;
	private RTSInputField ivjtxtMonth = null;
	private RTSInputField ivjtxtYear = null;
	// end defect 10014 

	private final int MAX_VALUE_MONTH = 12;
	private final int MIN_VALUE_MONTH = 1;
	private final int MIN_VALUE_YEAR = 1900;

	private ReportSearchData caRptSearchData = new ReportSearchData();

	// defect 10014
	private int ciMonth = 0;
	private int ciYear = 0;

	private static RTSDate caCurrentDate = new RTSDate();

	private final static String COUNTY_NO = "County No:";
	private final static String ENTER_THE_FOLLOWING =
		"Enter the following:";
	private final static String REPORT_MONTH = "Report Month:";
	private final static String YEAR = "Year: ";
	private final static String LAST_DAY_OF_MONTH = "Last Day of Month: "; 
	// end defect 10014

	/**
	 * main entrypoint starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmSalesTaxInquiryKEY003 laFrmSalesTaxInquiryKEY003;
			laFrmSalesTaxInquiryKEY003 = new FrmSalesTaxInquiryKEY003();
			laFrmSalesTaxInquiryKEY003.setModal(true);
			laFrmSalesTaxInquiryKEY003
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmSalesTaxInquiryKEY003.show();
			Insets laInsets = laFrmSalesTaxInquiryKEY003.getInsets();
			laFrmSalesTaxInquiryKEY003.setSize(
				laFrmSalesTaxInquiryKEY003.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmSalesTaxInquiryKEY003.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmSalesTaxInquiryKEY003.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of javax.swing.JDialog");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmSalesTaxInquiryKEY003 constructor
	 */
	public FrmSalesTaxInquiryKEY003()
	{
		super();
		initialize();
	}

	/**
	 * FrmSalesTaxInquiryKEY003 constructor
	 * 
	 * @param aaParent Dialog
	 */
	public FrmSalesTaxInquiryKEY003(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * 
	 * FrmSalesTaxInquiryKEY003 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmSalesTaxInquiryKEY003(JFrame aaParent)
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
		if (isWorking())
		{
			return;
		}
		// defect 10014
		try
		{
			clearAllColor(this);

			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateData())
				{
					setDataToDataObject();

					getController().processData(
						AbstractViewController.ENTER,
						caRptSearchData);
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
				RTSHelp.displayHelp(RTSHelp.KEY003);
			}
		}
		finally
		{
			doneWorking();
		}
		// end defect 10014
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
				ivjButtonPanel1.setBounds(42, 146, 295, 38);
				ivjButtonPanel1.setMaximumSize(new Dimension(217, 35));
				ivjButtonPanel1.setMinimumSize(new Dimension(217, 35));
				// user code begin {1}
				ivjButtonPanel1.setAsDefault(this);
				ivjButtonPanel1.addActionListener(this);
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
	 * Return the FrmSalesTaxInquiryKEY003ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private JPanel getFrmSalesTaxInquiryKEY003ContentPane1()
	{
		if (ivjFrmSalesTaxInquiryKEY003ContentPane1 == null)
		{
			try
			{
				ivjFrmSalesTaxInquiryKEY003ContentPane1 = new JPanel();
				ivjFrmSalesTaxInquiryKEY003ContentPane1.setName(
					"FrmSalesTaxInquiryKEY003ContentPane1");
				ivjFrmSalesTaxInquiryKEY003ContentPane1.setLayout(null);
				ivjFrmSalesTaxInquiryKEY003ContentPane1.setMaximumSize(
					new Dimension(576, 175));
				ivjFrmSalesTaxInquiryKEY003ContentPane1.setMinimumSize(
					new Dimension(576, 175));
				getFrmSalesTaxInquiryKEY003ContentPane1().add(
					gettxtMonth(),
					gettxtMonth().getName());
				getFrmSalesTaxInquiryKEY003ContentPane1().add(
					gettxtYear(),
					gettxtYear().getName());
				getFrmSalesTaxInquiryKEY003ContentPane1().add(
					getButtonPanel1(),
					getButtonPanel1().getName());
				getFrmSalesTaxInquiryKEY003ContentPane1().add(
					getstcLblEnterTheFollowing(),
					getstcLblEnterTheFollowing().getName());
				getFrmSalesTaxInquiryKEY003ContentPane1().add(
					getstcLblCountyNo(),
					getstcLblCountyNo().getName());
				getFrmSalesTaxInquiryKEY003ContentPane1().add(
					getstcLblReportMonth(),
					getstcLblReportMonth().getName());
				getFrmSalesTaxInquiryKEY003ContentPane1().add(
					getstcLblReportYear(),
					getstcLblReportYear().getName());
				getFrmSalesTaxInquiryKEY003ContentPane1().add(
					getlblCountyNo(),
					getlblCountyNo().getName());
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
		return ivjFrmSalesTaxInquiryKEY003ContentPane1;
	}

	/**
	 * Return last day of Month
	 * 
	 * @return RTSDate
	 */
	private RTSDate getLastDay(int aiMonth, int aiYear)
	{
		int liYYYYMMDD = aiYear * 10000 + aiMonth * 100 + 1;
		RTSDate laRTSDate = new RTSDate(RTSDate.YYYYMMDD, liYYYYMMDD);
		laRTSDate = laRTSDate.add(RTSDate.MONTH, 1);
		laRTSDate = laRTSDate.add(RTSDate.DATE, -1);
		if (SystemProperty.getProdStatus()
			!= SystemProperty.APP_PROD_STATUS)
		{
			System.out.println(
				LAST_DAY_OF_MONTH +laRTSDate.toString());
		}
		return laRTSDate;
	}

	/**
	 * Return the ivjlblCountyNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getlblCountyNo()
	{
		if (ivjlblCountyNo == null)
		{
			try
			{
				ivjlblCountyNo = new JLabel();
				ivjlblCountyNo.setName("ivjlblCountyNo");
				ivjlblCountyNo.setBounds(175, 67, 39, 20);
				ivjlblCountyNo.setText("179");
				// user code begin {1}
				ivjlblCountyNo.setHorizontalAlignment(
					javax.swing.SwingConstants.LEFT);
				ivjlblCountyNo.setRequestFocusEnabled(false);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjlblCountyNo;
	}

	/**
	 * Return the ivjstcLblCountyNo property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblCountyNo()
	{
		if (ivjstcLblCountyNo == null)
		{
			try
			{
				ivjstcLblCountyNo = new JLabel();
				ivjstcLblCountyNo.setName("ivjstcLblCountyNo");
				ivjstcLblCountyNo.setLocation(106, 67);
				ivjstcLblCountyNo.setSize(60, 20);
				ivjstcLblCountyNo.setText(COUNTY_NO);
				ivjstcLblCountyNo.setMaximumSize(new Dimension(60, 14));
				ivjstcLblCountyNo.setMinimumSize(new Dimension(60, 14));
				// user code begin {1}
				ivjstcLblCountyNo.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblCountyNo;
	}

	/**
	 * Return the ivjstcLblEnterthefollowing property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEnterTheFollowing()
	{
		if (ivjstcLblEnterTheFollowing == null)
		{
			try
			{
				ivjstcLblEnterTheFollowing = new JLabel();
				ivjstcLblEnterTheFollowing.setLocation(37, 29);
				ivjstcLblEnterTheFollowing.setSize(108, 20);
				ivjstcLblEnterTheFollowing.setName(
					"ivjstcLblEnterthefollowing");
				ivjstcLblEnterTheFollowing.setText(ENTER_THE_FOLLOWING);
				ivjstcLblEnterTheFollowing.setMaximumSize(
					new Dimension(108, 14));
				ivjstcLblEnterTheFollowing.setMinimumSize(
					new Dimension(108, 14));
				// user code begin {1}
				ivjstcLblEnterTheFollowing.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblEnterTheFollowing;
	}

	/**
	 * Return the stcLblReportMonth property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblReportMonth()
	{
		if (ivjstcLblReportMonth == null)
		{
			try
			{
				ivjstcLblReportMonth = new JLabel();
				ivjstcLblReportMonth.setName("ivjstcLblReportMonth");
				ivjstcLblReportMonth.setLocation(87, 97);
				ivjstcLblReportMonth.setSize(79, 20);
				ivjstcLblReportMonth.setText(REPORT_MONTH);
				ivjstcLblReportMonth.setMaximumSize(
					new Dimension(60, 14));
				ivjstcLblReportMonth.setMinimumSize(
					new Dimension(60, 14));

				// user code begin {1}
				ivjstcLblReportMonth.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblReportMonth;
	}

	/**
	 * Return the ivjstcLblReportYear property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblReportYear()
	{
		if (ivjstcLblReportYear == null)
		{
			try
			{
				ivjstcLblReportYear = new JLabel();
				ivjstcLblReportYear.setName("ivjstcLblReportYear");
				ivjstcLblReportYear.setLocation(210, 97);
				ivjstcLblReportYear.setSize(32, 20);
				ivjstcLblReportYear.setText(YEAR);
				ivjstcLblReportYear.setMaximumSize(
					new Dimension(60, 14));
				ivjstcLblReportYear.setMinimumSize(
					new Dimension(60, 14));
				// user code begin {1}
				ivjstcLblReportYear.setHorizontalAlignment(
					javax.swing.SwingConstants.RIGHT);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjstcLblReportYear;
	}

	/**
	 * Return the ivjtxtMonth property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtMonth()
	{
		if (ivjtxtMonth == null)
		{
			try
			{
				ivjtxtMonth = new RTSInputField();
				ivjtxtMonth.setName("ivjtxtMonth");
				ivjtxtMonth.setBounds(175, 97, 26, 20);
				ivjtxtMonth.setMaxLength(2);
				// user code begin {1}
				ivjtxtMonth.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtMonth;
	}

	/**
	 * Return the ivjtxtYear property value.
	 * 
	 * @return RTSInputField
	 */
	private RTSInputField gettxtYear()
	{
		if (ivjtxtYear == null)
		{
			try
			{
				ivjtxtYear = new RTSInputField();
				ivjtxtYear.setName("ivjtxtYear");
				ivjtxtYear.setLocation(250, 97);
				ivjtxtYear.setSize(38, 20);
				// user code begin {1}
				ivjtxtYear.setMaxLength(4);
				ivjtxtYear.setInput(RTSInputField.NUMERIC_ONLY);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjtxtYear;
	}

	/**
	 * Called whenever the part throws an exception.
	 * 
	 * @param aeThrowable Throwable
	 */
	private void handleException(Throwable aeThrowable)
	{

		/* Uncomment the following lines to print uncaught exceptions to stdout */
		// System.out.println("--------- UNCAUGHT EXCEPTION ---------");
		// exception.printStackTrace(System.out);
		// defect 7897
		// Handle GUI exceptions this was just ignored before
		RTSException leRTSEx =
			new RTSException(
				RTSException.JAVA_ERROR,
				(Exception) aeThrowable);
		leRTSEx.displayError(this);
		// end defect 7897
	}

	/**
	 * Initialize the class.
	 */
	/* WARNING: THIS METHOD WILL BE REGENERATED. */
	private void initialize()
	{
		try
		{
			setName("FrmSalesTaxInquiryKEY003");
			setDefaultCloseOperation(
				javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(375, 224);

			// defect 10014 
			setTitle(ScreenConstant.KEY003_FRM_TITLE);
			// end defect 10014

			setContentPane(getFrmSalesTaxInquiryKEY003ContentPane1());
			// user code begin {1}
			// user code end
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// user code begin {2}
		// user code end
	}

	/**
	 * isValidMonth
	 * 
	 * @return boolean
	 */
	private boolean isValidMonth()
	{
		ciMonth = Integer.parseInt(gettxtMonth().getText());
		return (
			ciMonth >= MIN_VALUE_MONTH && ciMonth <= MAX_VALUE_MONTH);
	}

	/**
	 * isValidYear
	 * 
	 * @return boolean
	 */
	private boolean isValidYear()
	{
		ciYear = Integer.parseInt(gettxtYear().getText());
		return (
			ciYear >= MIN_VALUE_YEAR
				&& ciYear <= caCurrentDate.getYear());
	}

	/**
	 * setData
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		// defect 10014 
		// Use class varible for Current Date 
		getlblCountyNo().setText(
			String.valueOf(SystemProperty.getOfficeIssuanceNo()));
		gettxtMonth().setText(String.valueOf(caCurrentDate.getMonth()));
		gettxtYear().setText(String.valueOf(caCurrentDate.getYear()));
		// end defect 10014  
	}

	/**
	 * Set Data to Data Object
	 */
	private void setDataToDataObject()
	{
		// defect 8628 
		caRptSearchData.initForClient(ReportConstant.ONLINE);
		// end defect 8628 

		caRptSearchData.setDate1(getLastDay(ciMonth, ciYear));
		caRptSearchData.setKey2(MFLogError.getErrorString());
	}

	/**
	 * Validate Data; Return boolean to denote validity of data. 
	 *
	 * @return boolean
	 */
	private boolean validateData()
	{
		boolean lbValid = true;

		RTSException leRTSEx = new RTSException();

		if (gettxtMonth().getText().equals("") || !isValidMonth())
		{
			RTSException leMsg =
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			leRTSEx.addException(leMsg, gettxtMonth());
		}

		if ((gettxtYear().getText().equals("") || !isValidYear()))
		{
			RTSException leMsg =
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
			leRTSEx.addException(leMsg, gettxtYear());
		}

		if (!leRTSEx.isValidationError())
		{
			if (ciYear * 12 + ciMonth
				> caCurrentDate.getYear() * 12
					+ caCurrentDate.getMonth())
			{
				RTSException leMsg =
					new RTSException(
						ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID);
				leRTSEx.addException(leMsg, gettxtMonth());
			}
		}

		if (leRTSEx.isValidationError())
		{
			leRTSEx.displayError(this);
			leRTSEx.getFirstComponent().requestFocus();
			lbValid = false;
		}
		return lbValid;

	}
} //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
