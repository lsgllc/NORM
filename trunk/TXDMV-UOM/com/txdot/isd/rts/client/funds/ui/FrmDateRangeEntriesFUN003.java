package com.txdot.isd.rts.client.funds.ui;

import java.awt.Dialog;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.FundsData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.RTSHelp;
import com.txdot.isd.rts.services.util.constants.ErrorsConstant;

/*
 * FrmDateRangeEntriesFUN003.java
 *
 * (c) Texas Department of Transportation 2001.
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * B Hargrove	07/11/2005	Modify code for move to Java 1.4 (VAJ->WSAD).
 * 							Bring code to standards.
 * 							delete implements KeyListener
 * 							delete keyPressed()
 * 							defect 7886 Ver 5.2.3 
 * B Hargrove	08/10/2005	Modify to do nothing is user clicks the 
 * 							Windows 'Close' icon.
 * 							modify initialize() 
 * 							defect 6897 Ver 5.2.3
 * K Harrell	01/03/2006	TODAY should not be a static variable.
 * 							Screen alignment
 * 							defect 7886 Ver 5.2.3 
 * K Harrell	01/07/2009	Validation of "10 days ago" includes time. 
 * 							refactor/redefine TODAY to caToday 
 * 							delete implements FocusListener 
 * 							delete getBuilderdata(),focustLost(), 
 * 							 focusGained() 
 * 							modify gettxtBeginDate(), gettxtEndDate(),
 * 							 gettxtBeginTime(), gettxtEndTime(), 
 * 							 setData()
 * 							defect 9826 Ver Defect_POS_D 
 * K Harrell	06/08/2009	Implement RTSDate.getClockTimeNoMs(). 
 * 							Additional cleanup. Reduce length of 
 * 							date/time fields. 
 * 							add ENTER_BEGIN_DATE, DEFAULT_BEGIN_TIME
 * 							add validateData() 
 * 							modify setData(), actionPerformed(),
 * 							 getJPanel1() 
 * 							defect 9943 Ver Defect_POS_F 
 * ---------------------------------------------------------------------
 */

/** 
 * Screen is launched by selecting "Specify Date Range" button from
 * FUN007, and prompts user to enter a Begin and End Date/Time.
 * 
 * @version	Defect_POS_F	06/08/2009
 * @author	Bobby Tulsiani
 * <br>Creation Date:		09/05/2001 13:30:59
 */

public class FrmDateRangeEntriesFUN003
	extends RTSDialogBox
	implements ActionListener
{
	 private ButtonPanel ivjButtonPanel1 = null;
	 private JPanel ivjFrmDateRangeEntriesFUN003ContentPane1 = null;
	 private JPanel ivjJPanel1 = null;
	 private JLabel ivjstcLblBeginDate = null;
	 private JLabel ivjstcLblBeginTime = null;
	 private JLabel ivjstcLblEndDate = null;
	 private JLabel ivjstcLblEndTime = null;
	 private RTSDateField ivjtxtBeginDate = null;
	 private RTSTimeField ivjtxtBeginTime = null;
	 private RTSDateField ivjtxtEndDate = null;
	 private RTSTimeField ivjtxtEndTime = null;
	 
	private RTSDate caToday =
		new RTSDate(
			RTSDate.YYYYMMDD,
			RTSDate.getCurrentDate().getYYYYMMDDDate());	 	

	private FundsData caFundsData = null;

	// Constants 
	private final static String BEGIN_DATE = "Begin Date:";
	private final static String BEGIN_TIME = "Begin Time:";
	private final static String END_DATE = "End Date:";
	private final static String END_TIME = "End Time:";
	private final static String TITLE_FUN003 =
		"Date Range Entries     FUN003";
		
	// defect 9943 
	private final static String DEFAULT_BEGIN_TIME = "00:00:00"; 	
	private final static String ENTER_BEGIN_DATE = "Enter Date Range:";
	// end defect 9943		

	/**
	 * main entrypoint - starts the part when it is run as an application
	 * 
	 * @param aarrArgs String[]
	 */
	public static void main(String[] aarrArgs)
	{
		try
		{
			FrmDateRangeEntriesFUN003 laFrmDateRangeEntriesFUN003;
			laFrmDateRangeEntriesFUN003 =
				new FrmDateRangeEntriesFUN003();
			laFrmDateRangeEntriesFUN003.setModal(true);
			laFrmDateRangeEntriesFUN003
				.addWindowListener(new java.awt.event.WindowAdapter()
			{
				public void windowClosing(
					java.awt.event.WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmDateRangeEntriesFUN003.show();
			java.awt.Insets laInsets =
				laFrmDateRangeEntriesFUN003.getInsets();
			laFrmDateRangeEntriesFUN003.setSize(
				laFrmDateRangeEntriesFUN003.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmDateRangeEntriesFUN003.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmDateRangeEntriesFUN003.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of util.JDialogTxDot");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmDateRangeEntriesFUN003 constructor comment.
	 */
	public FrmDateRangeEntriesFUN003()
	{
		super();
		initialize();
	}

	/**
	 * FrmDateRangeEntriesFUN003 constructor comment.
	 * 
	 * @param aaOwner Dialog
	 */
	public FrmDateRangeEntriesFUN003(Dialog aaOwner)
	{
		super(aaOwner);
		initialize();
	}

	/**
	 * FrmDateRangeEntriesFUN003 constructor comment.
	 * 
	 * @param aaParent JFrame
	 */
	public FrmDateRangeEntriesFUN003(JFrame aaParent)
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

			// defect 9943
			if (aaAE.getSource() == getButtonPanel1().getBtnEnter())
			{
				if (validateData())
				{
					getController().processData(
						AbstractViewController.ENTER,
						caFundsData);
				}
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
				RTSHelp.displayHelp(RTSHelp.FUN003);
			}
			// end defect 9943
		}
		finally
		{
			doneWorking();
		}

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
				ivjButtonPanel1.setBounds(27, 172, 217, 43);
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
	 * Return the FrmDateRangeEntriesFUN003ContentPane1 property value.
	 * 
	 * @return JPanel
	 */
	private javax
		.swing
		.JPanel getFrmDateRangeEntriesFUN003ContentPane1()
	{
		if (ivjFrmDateRangeEntriesFUN003ContentPane1 == null)
		{
			try
			{
				ivjFrmDateRangeEntriesFUN003ContentPane1 = new JPanel();
				ivjFrmDateRangeEntriesFUN003ContentPane1.setName(
					"ivjFrmDateRangeEntriesFUN003ContentPane1");
				ivjFrmDateRangeEntriesFUN003ContentPane1.setLayout(null);
				ivjFrmDateRangeEntriesFUN003ContentPane1.add(getJPanel1(), null);
				ivjFrmDateRangeEntriesFUN003ContentPane1.add(getButtonPanel1(), null);
				ivjFrmDateRangeEntriesFUN003ContentPane1
					.setMaximumSize(
					new java.awt.Dimension(362, 276));
				ivjFrmDateRangeEntriesFUN003ContentPane1
					.setMinimumSize(
					new java.awt.Dimension(362, 276));
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
		return ivjFrmDateRangeEntriesFUN003ContentPane1;
	}

	/**
	 * Return the ivjJPanel1 property value.
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
				ivjJPanel1.setName("ivjJPanel1");
				ivjJPanel1.setLayout(null);

				ivjJPanel1.add(getstcLblBeginDate(), null);
				ivjJPanel1.add(getstcLblBeginTime(), null);
				ivjJPanel1.add(getstcLblEndDate(), null);
				ivjJPanel1.add(getstcLblEndTime(), null);
				ivjJPanel1.add(gettxtBeginDate(), null);
				ivjJPanel1.add(gettxtBeginTime(), null);
				ivjJPanel1.add(gettxtEndDate(), null);
				ivjJPanel1.add(gettxtEndTime(), null);
				ivjJPanel1.setBounds(23, 15, 221, 152);
				// defect 9943
				// Use Constant
				Border laBorder =
					new TitledBorder(
						new EtchedBorder(),
						ENTER_BEGIN_DATE);
				// end defect 9943
				ivjJPanel1.setBorder(laBorder);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjJPanel1;
	}

	/**
	 * Return the ivjstcLblBeginDate property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBeginDate()
	{
		if (ivjstcLblBeginDate == null)
		{
			try
			{
				ivjstcLblBeginDate = new JLabel();
				ivjstcLblBeginDate.setName("ivjstcLblBeginDate");
				ivjstcLblBeginDate.setText(BEGIN_DATE);
				ivjstcLblBeginDate.setMinimumSize(
					new java.awt.Dimension(64, 14));
				ivjstcLblBeginDate.setMaximumSize(
					new java.awt.Dimension(64, 14));
				// user code begin {1}
				ivjstcLblBeginDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblBeginDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_B);
				ivjstcLblBeginDate.setLabelFor(gettxtBeginDate());
				// user code end
				ivjstcLblBeginDate.setBounds(32, 30, 69, 18);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblBeginDate;
	}

	/**
	 * Return the ivjstcLblBeginTime property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblBeginTime()
	{
		if (ivjstcLblBeginTime == null)
		{
			try
			{
				ivjstcLblBeginTime = new JLabel();
				ivjstcLblBeginTime.setName("ivjstcLblBeginTime");
				ivjstcLblBeginTime.setText(BEGIN_TIME);
				ivjstcLblBeginTime.setMinimumSize(
					new java.awt.Dimension(66, 14));
				ivjstcLblBeginTime.setMaximumSize(
					new java.awt.Dimension(66, 14));
				// user code begin {1}
				ivjstcLblBeginTime.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblBeginTime.setLabelFor(gettxtBeginTime());
				ivjstcLblBeginTime.setBounds(31, 58, 70, 17);
				ivjstcLblBeginTime.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_T);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblBeginTime;
	}

	/**
	 * Return the ivjstcLblEndDate property value.
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
				ivjstcLblEndDate.setName("ivjstcLblEndDate");
				ivjstcLblEndDate.setText(END_DATE);
				ivjstcLblEndDate.setMaximumSize(
					new java.awt.Dimension(53, 14));
				ivjstcLblEndDate.setMinimumSize(
					new java.awt.Dimension(53, 14));
				// user code begin {1}
				ivjstcLblEndDate.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblEndDate.setLabelFor(gettxtEndDate());
				ivjstcLblEndDate.setBounds(40, 96, 61, 17);
				ivjstcLblEndDate.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_N);
				// user code end
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblEndDate;
	}
	
	/**
	 * Return the ivjstcLblEndTime property value.
	 * 
	 * @return JLabel
	 */
	private JLabel getstcLblEndTime()
	{
		if (ivjstcLblEndTime == null)
		{
			try
			{
				ivjstcLblEndTime = new JLabel();
				ivjstcLblEndTime.setName("ivjstcLblEndTime");
				ivjstcLblEndTime.setText(END_TIME);
				ivjstcLblEndTime.setMinimumSize(
					new java.awt.Dimension(55, 14));
				ivjstcLblEndTime.setMaximumSize(
					new java.awt.Dimension(55, 14));
				// user code begin {1}
				ivjstcLblEndTime.setHorizontalAlignment(
					SwingConstants.RIGHT);
				ivjstcLblEndTime.setDisplayedMnemonic(
					java.awt.event.KeyEvent.VK_M);
				ivjstcLblEndTime.setLabelFor(gettxtEndTime());
				// user code end
				ivjstcLblEndTime.setBounds(40, 124, 61, 17);
			}
			catch (Throwable aeIVJEx)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJEx);
			}
		}
		return ivjstcLblEndTime;
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
				ivjtxtBeginDate.setBounds(115, 28, 75, 20);
				ivjtxtBeginDate.setName("ivjtxtBeginDate");
				ivjtxtBeginDate.setHighlighter(
					new javax
						.swing
						.plaf
						.basic
						.BasicTextUI
						.BasicHighlighter());
				ivjtxtBeginDate.setRequestFocusEnabled(true);
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
		return ivjtxtBeginDate;
	}

	/**
	 * Return the ivjtxtBeginTime property value.
	 * 
	 * @return RTSTimeField
	 */
	private RTSTimeField gettxtBeginTime()
	{
		if (ivjtxtBeginTime == null)
		{
			try
			{
				ivjtxtBeginTime = new RTSTimeField();
				ivjtxtBeginTime.setBounds(115, 55, 75, 20);
				ivjtxtBeginTime.setName("ivjtxtBeginTime");
				ivjtxtBeginTime.setHighlighter(
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
		return ivjtxtBeginTime;
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
				ivjtxtEndDate.setBounds(115, 93, 75, 20);
				ivjtxtEndDate.setName("ivjtxtEndDate");
				ivjtxtEndDate.setHighlighter(
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
		return ivjtxtEndDate;
	}

	/**
	 * Return the ivjtxtEndTime property value.
	 * 
	 * @return RTSTimeField
	 */
	private RTSTimeField gettxtEndTime()
	{
		if (ivjtxtEndTime == null)
		{
			try
			{
				ivjtxtEndTime = new RTSTimeField();
				ivjtxtEndTime.setBounds(115, 121, 75, 20);
				ivjtxtEndTime.setName("ivjtxtEndTime");
				ivjtxtEndTime.setHighlighter(
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
		return ivjtxtEndTime;
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
			// user code begin {1}
			// user code end
			setName("FrmDateRangeEntriesFUN003");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setSize(290, 246);
			setTitle(TITLE_FUN003);
			setContentPane(getFrmDateRangeEntriesFUN003ContentPane1());
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
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		try
		{
			if (aaDataObject != null)
			{
				caFundsData = (FundsData) aaDataObject;
				if (caFundsData.getFundsReportData().getFromRange()
					== null)
				{
					gettxtBeginDate().setDate(caToday);
					gettxtEndDate().setDate(caToday);
					// defect 9826  / 9943
					//gettxtBeginTime().setTime("00:00:00");
					//gettxtEndTime().setTime(caToday.getClockTime());										
					gettxtBeginTime().setTime(DEFAULT_BEGIN_TIME);					
					gettxtEndTime().setTime(
						new RTSDate().getClockTimeNoMs());
					// end defect 9826 / 9943 
				}
				else
				{
					gettxtBeginDate().setDate(
						caFundsData
							.getFundsReportData()
							.getFromRange());
					// defect 9943
					gettxtBeginTime().setTime(
						caFundsData
							.getFundsReportData()
							.getFromRange()
							.getClockTimeNoMs());
							//.getClockTime().substring(0, 8));
					gettxtEndDate().setDate(
						caFundsData.getFundsReportData().getToRange());
					gettxtEndTime().setTime(
						caFundsData
							.getFundsReportData()
							.getToRange()
							.getClockTimeNoMs());
							//.getClockTime().substring(0,8));
					// end defect 9943 

				}
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
	 * Validate Data
	 * 
	 * @return boolean 
	 */
	private boolean validateData()
	{
		boolean lbReturn = true;
		
		// Initalize From Date/Time values
		RTSDate laFromDate = null;
		String lsFromTime = null;
		char[] lchFromTimeArr = null;
		int liFromMonth = 0;
		int liFromDay = 0;
		int liFromYear = 0;
		int liFromHour = 0;
		int liFromMinute = 0;
		int liFromSecond = 0;

		//Initalize To Date/Time values
		RTSDate laToDate = null;
		String lsToTime = null;
		char[] lchToTimeArr = null;
		int liToMonth = 0;
		int liToDay = 0;
		int liToYear = 0;
		int liToHour = 0;
		int liToMinute = 0;
		int liToSecond = 0;

		RTSException laEx = new RTSException();

		//Validate if Begin Date is valid
		if (ivjtxtBeginDate.isValidDate() == false)
		{
			//laEx.addException(new RTSException(150), gettxtBeginDate());
			laEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtBeginDate());
		}
		//Validate if End Time is valid
		if (ivjtxtBeginTime.isValidTime() == false)
		{
			//laEx.addException(new RTSException(150), gettxtBeginTime());
			laEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtBeginTime());
		}
		//Validate if End Date is valid
		if (ivjtxtEndDate.isValidDate() == false)
		{
			//laEx.addException(new RTSException(150), gettxtEndDate());
			laEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtEndDate());
		}
		//Validate if End Time is valid
		if (ivjtxtEndTime.isValidTime() == false)
		{
			//laEx.addException(new RTSException(150), gettxtEndTime());
			laEx.addException(
				new RTSException(
					ErrorsConstant.ERR_NUM_FIELD_ENTRY_INVALID),
				gettxtEndTime());
		}

		if (!laEx.isValidationError())
		{
			//Read Inputted From Date & Time
			laFromDate = gettxtBeginDate().getDate();
			liFromMonth = laFromDate.getMonth();
			liFromDay = laFromDate.getDate();
			liFromYear = laFromDate.getYear();
			lsFromTime = gettxtBeginTime().getText();
			lchFromTimeArr = lsFromTime.toCharArray();
			liFromHour =
				Integer.parseInt(
					new String(
						"" + lchFromTimeArr[0] + lchFromTimeArr[1]));
			liFromMinute =
				Integer.parseInt(
					new String(
						"" + lchFromTimeArr[3] + lchFromTimeArr[4]));
			liFromSecond =
				Integer.parseInt(
					new String(
						"" + lchFromTimeArr[6] + lchFromTimeArr[7]));

			//Read Inputted To Date & Time
			laToDate = gettxtEndDate().getDate();
			liToMonth = laToDate.getMonth();
			liToDay = laToDate.getDate();
			liToYear = laToDate.getYear();
			lsToTime = gettxtEndTime().getText();
			lchToTimeArr = lsToTime.toCharArray();
			liToHour =
				Integer.parseInt(
					new String("" + lchToTimeArr[0] + lchToTimeArr[1]));
			liToMinute =
				Integer.parseInt(
					new String("" + lchToTimeArr[3] + lchToTimeArr[4]));
			liToSecond =
				Integer.parseInt(
					new String("" + lchToTimeArr[6] + lchToTimeArr[7]));

			//Set to Date & Time into FundsDataObject
			laFromDate.set(
				liFromYear,
				liFromMonth,
				liFromDay,
				liFromHour,
				liFromMinute,
				liFromSecond,
				0);
			laToDate.set(
				liToYear,
				liToMonth,
				liToDay,
				liToHour,
				liToMinute,
				liToSecond,
				0);
			caFundsData.getFundsReportData().setFromRange(laFromDate);
			caFundsData.getFundsReportData().setToRange(laToDate);

			if (laFromDate.compareTo(laToDate) > 0)
			{
				laEx.addException(
				//new RTSException(585),
				new RTSException(
					ErrorsConstant.ERR_NUM_BEGIN_DATE_AFTER_END_DATE),
					gettxtBeginDate());
			}

			RTSDate laValidDate = caToday.add(RTSDate.DATE, -10);

			//Check if Valid Date
			if (laFromDate.compareTo(laValidDate) == -1)
			{
				laEx.addException(
				//new RTSException(581),
				new RTSException(
					ErrorsConstant.ERR_NUM_DATE_RANGE_INVALID_11_DAYS),
					gettxtBeginDate());
			}
		}

		if (laEx.isValidationError())
		{
			laEx.displayError(this);
			laEx.getFirstComponent().requestFocus();
			lbReturn = false;
		}
		return lbReturn;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"
