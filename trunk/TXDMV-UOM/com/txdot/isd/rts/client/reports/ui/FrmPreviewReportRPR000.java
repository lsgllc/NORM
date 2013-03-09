package com.txdot.isd.rts.client.reports.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.txdot.isd.rts.client.general.ui.*;

import com.txdot.isd.rts.services.data.GeneralSearchData;
import com.txdot.isd.rts.services.data.ReportSearchData;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.util.*;
import com.txdot.isd.rts.services.util.constants.CommonConstant;
import com.txdot.isd.rts.services.util.constants.FundsConstant;
import com.txdot.isd.rts.services.util.constants.ReportConstant;
import com.txdot.isd.rts.services.util.constants.TransCdConstant;

/*
 * FrmPreviewReportRPR000.java
 *
 * (c) Texas Department of Transportation 2002
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * MAbs			04/12/2002	Added mouse support to scroll to
 *							next/previous page in multiple page reports 
 *							defect 3434
 * SGovindappa	05/02/2002  changing initPage and 
 *							added addBlanks(int) to prevent error while 
 *							previewing blank records
 *							defect 3733  
 * BTulsiani	05/06/2002	Code to print a duplicate if user selects 
 *							Void Report 
 *							defect 3782
 * MAbs			05/09/2002	defect 3834
 * MAbs			05/16/2002	Got rid of mysterious beeps 
 * 							defect 3835
 * MML			06/03/2002  Corrected display of Mainframe reports 
 *							(initPage()).  
 *							defect 4174
 * B Arredondo	12/16/2002	Made changes for the 
 *							user help guide so had to make changes
 *							in actionPerformed().
 *							defect 5147. 
 * M Wang		03/06/2003	Modified actionPerformed(). 
 *							defect 5648.
 * Jeff S.		07/06/2003  Added barcode to Tittle Package report and 
 *							had to handle the printing differently by 
 *							calling Print method to create temp file
 *							with the barcode PCL then print and delete 
 *							the temp file
 *							handlePrintBtn()
 *							defect 3994
 * J Rue		07/18/2003	Add conditional to skip print 
 *							linr if first char = ESC
 *							method initPage()
 *							defect 6181 
 * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed
 *							call to open and close FTP connection.
 *							Replaced calls to genBarCodeReceiptFile()
 *							and genTempDuplFile(String,String) with
 *							direct sendToPrinter() calls and let the
 *							printDocManager handle printing of dups and
 *							barcode files.(Void rpt and TittlePack rpt)
 *							modify handlePrintBtn()
 *							defect 6848, 6898 Ver 5.1.6
 * Jeff S.		03/02/2004	Rebuilt the frame to match the frame that
 *							existed before.  This fixed the problem with
 *							the builddata not matching the actual code.
 *							In visual composition I changed the default
 *							close operation to DO_NOTHING_ON_CLOSE.
 *							Refer to comments in getRTSTextPane1() if
 *							have problems compiling code visually.
 *							You must pass dsd into RTSTextPane(dsd).
 *							defect 6897 Ver 5.1.6
 * S Johnston	03/07/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify handleException()
 *							defect 7896 Ver 5.2.3
 * B Hargrove	05/11/2005	Remove reference to Quick Counter.
 *  						modify actionPerfomed() 
 *							defect 7955 Ver 5.2.3
 * S Johnston	06/29/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							modify initPage()
 *							defect 8178 Ver 5.2.3
 * Ray Rowehl	07/06/2005 	ReportConstants cleanup for Inventory.
 * 							defect 7890 Ver 5.2.3
 * K Harrell	11/02/2005	ReportConstants cleanup for Publishing
 * 							Report. 
 * 							modify actionPerformed()
 * 							defect 8379 Ver 5.2.3 
 * Jeff S.		12/12/2005	Removed setting of the focus manager.  This
 * 							code was making the HotKeys and Acceler. on
 * 							the desktop stop working.
 * 							modify initialize() 
 *							defect 7896 Ver 5.2.3
 * Jeff S.		04/02/2007	Removed code that set the size and position
 * 							of this frame.  This caused the message pop
 * 							up to not work correctly.
 * 							modify initialize()
 * 							defect 7768 Ver Broadcast Message
 * Jeff S.		06/27/2007	Left something out in the merge.
 * 							modify initialize()
 * 							defect 7768 Ver Broadcast Message
 * K Harrell	06/15/2009	Update for Print Range Printing;  
 * 							 Additional cleanup. 
 * 							add caButtonGroup, csPrintCtrl, 
 * 							 csPrintFileName,
 * 							 START_MF_RPT_PREFIX, END_MF_RPT_PREFIX,
 * 							 COMMA_DELIMITER, SEMICOLON_DELIMITER,
 * 							 THROUGH_SYMBOL, ERROR_ON_TEXT_INSERT,
 * 							 FRM_TITLE_PREFIX, MF_RPT_PREFIX,
 * 							 REPORT_NO_PREFIX, TMP_PRINT_NAME,
 * 							 TMP_FF_NAME, MF_RPT_AUX_PREFIX
 * 							add addPageNoToPrintVector(), getPageNumbers(),
 * 						  	 createPrintFileforSelection(), 
 * 							 createFFforMFReports(), setMFReport(), 
 * 							 isMFReport(), isCertificate()
 * 							add ivjbtnEnter, ivjbtnHelp, ivjbtnCancel  
 * 							delete getFirstNonDisabledButton(),
 * 							 ivjRTSEnter, ivjRTSHelp, ivjRTSCancel
 * 							modify ciLinesPerPage  
 * 							modify getbtnEnter(), getbtnRTSHelp(), 
 * 							 getbtnCancel(), actionPerformed(), 
 * 							 getJPanel1(), handleEnterBtn(), 
 * 							 handlePrintBtn(), initPage() 
 * 							defect 10086 Ver Defect_POS_F 
 * K Harrell	06/19/2009	Use ReportConstant vs. RegRenProcessingConstants 
 * 							 for Report titles.  
 * 							modify actionPerformed() 
 * 							defect 10023 Ver Defect_POS_F
 * K Harrell	08/27/2009	Inititalize IntKey4 to 0 prior to RPR008; 
 * 							Required when handling view of Multiples
 * 							modify handlePrintBtn() 
 * 							defect 10086 Ver Defect_POS_F 
 * K Harrell	09/14/2010	modify setData() 
 * 							defect 10590 Ver 6.6.0 
 * Min Wang		04/20/2012  resize for the screen
 * 							modify initialize()
 * 							defect 11333 Ver 7.0.0
 * Min Wang		04/23/2012	modify initialize()
 * 							add DEFAULT_SCREEN_HEIGHT, 
 * 								DEFAULT_SCREEN_WIDTH
 * 							defect 11333 Ver 7.0.0
 * Min Wang		05/16/2012	Adjust computed screen height to show scroll 
 * 							bar.
 * 							modify initialize()
 * 							defect 11333 Ver 7.0.0
 * ---------------------------------------------------------------------
 */

/**
 * RPR000 handles displaying all the reports stored on the local 
 * computer
 *
 * @version	7.0.0 				05/16/2012
 * @author	Michael Abernethy
 * <br>Creation Date:			04/12/2002
 */

public class FrmPreviewReportRPR000
	extends RTSDialogBox
	implements ActionListener, AdjustmentListener
{
	private static final int DEFAULT_SCREEN_HEIGHT = 1000;
	private static final int DEFAULT_SCREEN_WIDTH = 1200;
	private RTSButton ivjbtnCancel = null;
	private RTSButton ivjbtnEnter = null;
	private RTSButton ivjbtnFirst = null;
	private RTSButton ivjbtnHelp = null;
	private RTSButton ivjbtnLast = null;
	private RTSButton ivjbtnNext = null;
	private RTSButton ivjbtnPrev = null;
	private RTSButton ivjbtnPrint = null;
	private JPanel ivjJPanel1 = null;
	private JScrollPane ivjJScrollPane1 = null;
	private JPanel ivjRTSDialogBoxContentPane = null;
	private RTSTextPane ivjRTSTextPane1 = null;

	private SimpleAttributeSet caAttr = new SimpleAttributeSet();

	// Object
	private DefaultStyledDocument caDSD;
	private ReportSearchData caRptSearchData = new ReportSearchData();

	// boolean 
	//Flag to indicate not to reset the doc position.
	private boolean cbFlg;

	private boolean cbScrollAtMax;
	private boolean cbScrollAtMin;

	// int 
	private int ciCounter;
	private int ciLineCount = 1;
	private int ciPageNum = 1;
	private int ciPages = 1;

	// String 
	private String csFileName;
	private String csReportName;

	// Vector 
	private Vector cvReport;
	private Vector cvViewReports = new Vector();

	// defect 10086
	private RTSButtonGroup caButtonGroup = new RTSButtonGroup();

	private boolean cbMFReport;

	//private int ciLinesPerPage = 77;
	private int ciLinesPerPage = ReportConstant.LINES_PER_PAGE_PORTRAIT;

	private String csPrintCtrl = new String();
	private String csPrintFileName;

	private final static int START_MF_RPT_PREFIX = 11;
	private final static int END_MF_RPT_PREFIX = 13;

	private final static String COMMA_DELIMITER = ",";
	private final static String SEMICOLON_DELIMITER = ";";
	private final static String THROUGH_SYMBOL = "-";
	private final static String ERROR_ON_TEXT_INSERT =
		"Error on initial text insert";
	private final static String FRM_TITLE_PREFIX =
		"Preview Report RPR000: ";
	private final static String MF_RPT_PREFIX = "MF";
	private final static String MF_RPT_AUX_PREFIX = "AU";
	private final static String REPORT_NO_PREFIX = "RTS.";
	private final static String TMP_PRINT_NAME =
		SystemProperty.getReportsDirectory() + "TMP_RPR.RPT";
	private final static String TMP_FF_NAME =
		SystemProperty.getReportsDirectory() + "TMP_FF.RPT";
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
			FrmPreviewReportRPR000 laFrmPreviewReportRPR000;
			laFrmPreviewReportRPR000 = new FrmPreviewReportRPR000();
			laFrmPreviewReportRPR000.setModal(true);
			laFrmPreviewReportRPR000
				.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReportRPR000.show();
			Insets laInsets = laFrmPreviewReportRPR000.getInsets();
			laFrmPreviewReportRPR000.setSize(
				laFrmPreviewReportRPR000.getWidth()
					+ laInsets.left
					+ laInsets.right,
				laFrmPreviewReportRPR000.getHeight()
					+ laInsets.top
					+ laInsets.bottom);
			laFrmPreviewReportRPR000.setVisibleRTS(true);
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * FrmPreviewReportRPR000 constructor
	 */
	public FrmPreviewReportRPR000()
	{
		super();
		initialize();
	}

	/**
	 * FrmPreviewReportRPR000 constructor
	 * 
	 * @param aaParent Dialog
	 */
	public FrmPreviewReportRPR000(Dialog aaParent)
	{
		super(aaParent);
		initialize();
	}

	/**
	 * FrmPreviewReportRPR000 constructor
	 * 
	 * @param aaParent JFrame
	 */
	public FrmPreviewReportRPR000(JFrame aaParent)
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
		if (!startWorking() || !isVisible())
		{
			return;
		}
		try
		{
			clearAllColor(this);

			// defect 10086 
			// Use CommonConstant for button text 
			if (aaAE
				.getActionCommand()
				.equals(CommonConstant.BTN_TXT_ENTER))
			{
				handleEnterBtn();
			}
			else if (
				aaAE.getActionCommand().equals(
					CommonConstant.BTN_TXT_CANCEL))
			{
				getController().processData(
					AbstractViewController.CANCEL,
					caRptSearchData);
			}
			else if (
				aaAE.getActionCommand().equals(
					CommonConstant.BTN_TXT_HELP))
			{
				// end defect 10086 
				if (csReportName.equals(FundsConstant.PAYMENT_REPORT))
				{
					RTSHelp.displayHelp(RTSHelp.PAYMENT_REPORT);
				}
				else if (
					csReportName.equals(
						FundsConstant.TRANSACTION_REPORT))
				{
					RTSHelp.displayHelp(
						RTSHelp.TRANSACTION_RECONCILIATION_REPORT);
				}
				else if (
					csReportName.equals(FundsConstant.FEES_REPORT))
				{
					RTSHelp.displayHelp(RTSHelp.FEES_REPORT);
				}
				else if (
					csReportName.equals(
						FundsConstant.INVENTORYD_REPORT))
				{
					RTSHelp.displayHelp(
						RTSHelp.INVENTORY_DETAIL_REPORT);
				}
				else if (
					csReportName.equals(
						FundsConstant.INVENTORYS_REPORT))
				{
					RTSHelp.displayHelp(
						RTSHelp.INVENTORY_SUMMARY_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant
							.SALES_TAX_ALLOCATION_REPORT_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.SALES_TAX_ALLOCATION_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant.TITLE_PACKAGE_REPORT_TITLE))
				{
					RTSHelp.displayHelp(RTSHelp.TITLE_PACKAGE_REPORT);
				}
				else if (
					csReportName.equals(ReportConstant.DEALER_REPORT))
				{
					RTSHelp.displayHelp(RTSHelp.DEALER_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant.SUBCONTRACTOR_REPORT))
				{
					RTSHelp.displayHelp(RTSHelp.SUBCONTRACTOR_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant.SUBCONTRACTOR_RENEWAL_REPORT))
				{
					RTSHelp.displayHelp(
						RTSHelp.SUBCONTRACTOR_RENEWAL_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant.LIENHOLDER_REPORT))
				{
					RTSHelp.displayHelp(RTSHelp.LIENHOLDER_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant.EMPLOYEE_SECURITY_REPORT))
				{
					RTSHelp.displayHelp(
						RTSHelp.EMPLOYEE_SECURITY_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant.EVENT_SECURITY_REPORT))
				{
					RTSHelp.displayHelp(RTSHelp.EVENT_SECURITY_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant.RPT_9901_IAR_REPORT_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.INVENTORY_ACTION_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant
							.RPT_3011_INVENTORY_ALLOCATE_REPORT_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.INVENTORY_ALLOCATE_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant
							.RPT_3041_INVENTORY_HOLD_REPORT_TITLE))
				{
					RTSHelp.displayHelp(RTSHelp.INVENTORY_HOLD_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant
							.RPT_3001_INVENTORY_DELETE_REPORT_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.INVENTORY_DELETE_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant
							.RPT_3031_INVENTORY_INQUIRY_REPORT_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.INVENTORY_INQUIRY_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant
							.RPT_3021_INVENTORY_RECEIVE_REPORT_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.INVENTORY_RECEIVE_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant
							.RPT_3071_DELETE_INVENTORY_HISTORY_REPORT_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.DELETE_INVENTORY_HISTORY_REPORT);
				}
				else if (
					csReportName.equals(
						ReportConstant
							.RPT_3072_RECEIVE_INVENTORY_HISTORY_REPORT_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.RECEIVE_INVENTORY_HISTORY_REPORT);
				}
				// defect 8379 
				// Constant name change 
				else if (
					csReportName.equals(
						ReportConstant.PUBLISHING_REPORT_TITLE))
				{
					RTSHelp.displayHelp(RTSHelp.PUBLISHING_REPORT);
				}
				// end defect 8379 
				// defect 10023 
				// Use ReportConstant vs. RegRenProcessingConstants
				else if (
					csReportName.equals(
					//RegRenProcessingConstants.ADDRCHG_TITLE))
				ReportConstant
						.RPT_2001_ADDR_CHNG_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.INET_ADDRESS_CHANGE_REPORT);
				}
				else if (
					csReportName.equals(
					//RegRenProcessingConstants.ITRANS_TITLE))
				ReportConstant
						.RPT_2002_ITRNT_TRANS_RECON_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.INET_RECONCILIATION_REPORT);
				}
				else if (
					csReportName.equals(
					//RegRenProcessingConstants.VENDOR_TRANS_TITLE))
				ReportConstant
						.RPT_2003_VENDOR_PAYMENT_TITLE))
				{
					RTSHelp.displayHelp(
						RTSHelp.INET_VENDOR_PAYMENT_REPORT);
				}
				// end defect 10023 
				else if (
					csReportName.equals(
						ReportConstant.SECURITY_CHANGE_REPORT))
				{
					RTSHelp.displayHelp(RTSHelp.SECURITY_CHANGE_REPORT);
				}
				else
				{
					RTSHelp.displayHelp(RTSHelp.RPR000);
				}
			}
			else
			{
				if (aaAE
					.getActionCommand()
					.equals(CommonConstant.BTN_TXT_PREV))
				{
					handlePrevBtn();
				}
				else if (
					aaAE.getActionCommand().equals(
						CommonConstant.BTN_TXT_NEXT))
				{
					handleNextBtn();
				}
				else if (
					aaAE.getActionCommand().equals(
						CommonConstant.BTN_TXT_FIRST))
				{
					handleFirstBtn();
				}
				else if (
					aaAE.getActionCommand().equals(
						CommonConstant.BTN_TXT_LAST))
				{
					handleLastBtn();
				}
				else if (
					aaAE.getActionCommand().equals(
						CommonConstant.BTN_TXT_PRINT))
				{
					handlePrintBtn();
				}
				getRTSTextPane1().requestFocus();
			}
		}
		finally
		{
			setWorking(false);
		}
	}

	/**
	 * Add the number of specified blanks
	 *
	 * @param aiNoOfBlanks int
	 * @return String with specified blanks
	 */
	public String addBlanks(int aiNoOfBlanks)
	{

		String lsString = "";
		for (int i = 0; i < aiNoOfBlanks; i++)
		{
			lsString = lsString + " ";
		}
		return lsString;
	}

	/**
	 * add Page Number to Print Vector
	 * 
	 * @param aiPageNo
	 * @param avPrint
	 */
	private void addPageNoToPrintVector(int aiPageNo, Vector avPrint)
	{
		if (aiPageNo > 0)
		{
			avPrint.add(new Integer(aiPageNo));
		}
	}

	/**
	 * Invoked when the value of the adjustable on JScrollBar changes
	 * 
	 * @param aaAE AdjustmentEvent
	 */
	public void adjustmentValueChanged(AdjustmentEvent aaAE)
	{
		// Handles moving the ScrollPane to the next page in a report if 
		// the mouse is pressed and the JScrollBar is at its max value
		JScrollBar laVertScroll =
			getJScrollPane1().getVerticalScrollBar();
		if (laVertScroll.getValueIsAdjusting())
		{
			return;
		}
		int liMaxPoss =
			laVertScroll.getMaximum() - laVertScroll.getVisibleAmount();
		if (laVertScroll.getValue() == liMaxPoss
			&& getbtnNext().isEnabled())
		{
			if (cbScrollAtMax)
			{
				cbScrollAtMax = false;
				getbtnNext().doClick();
			}
			else
			{
				cbScrollAtMax = true;
				laVertScroll.setValue(liMaxPoss - 1);
			}
		}
		if (laVertScroll.getValue() == 0 && getbtnPrev().isEnabled())
		{
			if (cbScrollAtMin)
			{
				cbScrollAtMin = false;
				getbtnPrev().doClick();
			}
			else
			{
				cbScrollAtMin = true;
				laVertScroll.setValue(1);
			}
		}
	}

	/**
	 * 
	 * Return the Page Numbers 
	 */
	private Vector getPageNumbers()
	{
		Vector lvPageNo = new Vector();

		StringTokenizer laPageListSemiColon =
			new StringTokenizer(
				caRptSearchData.getKey4(),
				SEMICOLON_DELIMITER);

		while (laPageListSemiColon.hasMoreTokens())
		{
			String lsPartial = laPageListSemiColon.nextToken();

			StringTokenizer laPageListComma =
				new StringTokenizer(lsPartial, COMMA_DELIMITER);

			String lsPages;

			while (laPageListComma.hasMoreTokens())
			{
				lsPages = laPageListComma.nextToken();
				lsPages = lsPages.trim();
				int liPosDash = lsPages.indexOf(THROUGH_SYMBOL);

				if (liPosDash >= 0)
				{
					String lsStart =
						lsPages.substring(0, liPosDash).trim();

					String lsEnd =
						lsPages.substring(liPosDash + 1).trim();
					try
					{
						int liStart = Integer.parseInt(lsStart);
						int liEnd = Integer.parseInt(lsEnd);

						// Adjust if outside max page range; 
						// add PageNoToPrintVector handles 0 page #  
						liStart =
							(liStart > ciPages) ? ciPages : liStart;
						liEnd = (liEnd > ciPages) ? ciPages : liEnd;

						addPageNoToPrintVector(liStart, lvPageNo);

						if (liStart != liEnd)
						{
							int liBy = liStart < liEnd ? 1 : -1;
							liStart = liStart + liBy;

							for (;
								liStart != liEnd;
								liStart = liStart + liBy)
							{
								addPageNoToPrintVector(
									liStart,
									lvPageNo);
							}
							addPageNoToPrintVector(liEnd, lvPageNo);
						}
					}
					catch (NumberFormatException aeNFEx)
					{
						System.out.println(aeNFEx.getMessage());
					}
				}
				else
				{
					try
					{
						int liPageNo = new Integer(lsPages).intValue();
						if (liPageNo <= ciPages)
						{
							addPageNoToPrintVector(liPageNo, lvPageNo);
						}
					}
					catch (NumberFormatException aeNFEx)
					{
						System.out.println(aeNFEx.getMessage());
					}
				}
			}
		}
		return lvPageNo;
	}

	/**
	 * Create Print File for Selected Pages 
	 */
	private boolean createPrintFileforSelection()
	{
		Vector lvPageNo = new Vector();
		try
		{
			FileUtil.deleteFile(TMP_PRINT_NAME);

			lvPageNo = getPageNumbers();

			if (lvPageNo.size() != 0)
			{
				FileOutputStream laFile =
					new FileOutputStream(csPrintFileName, true);
				PrintWriter laPWOut = new PrintWriter(laFile);
				if (isMFReport())
				{
					// Remove extra page if MF Report 
					int liBreak =
						csPrintCtrl.indexOf(ReportConstant.FF);
					if (liBreak != -1)
					{
						csPrintCtrl = csPrintCtrl.substring(0, liBreak);
					}
				}
				laPWOut.print(csPrintCtrl);

				for (int i = 0; i < lvPageNo.size(); i++)
				{
					int liPageNo =
						((Integer) lvPageNo.elementAt(i)).intValue();

					int liRowNoForPage = liPageNo - 1;

					laPWOut.print(CommonConstant.SYSTEM_LINE_SEPARATOR);

					laPWOut.print(
						((StringBuffer) cvReport.get(liRowNoForPage))
							.toString()
							.trim());

					laPWOut.print(ReportConstant.FF);
				}

				laPWOut.flush();
				laPWOut.close();
				laFile.close();
			}
		}
		catch (IOException aeIOEx)
		{
			System.out.println(
				"Got an IOException writing to " + csPrintFileName);
		}
		return lvPageNo.size() > 0;
	}

	/**
	 * Create FF file for MF Report - no selection  
	 */
	private void createFFforMFReports()
	{
		try
		{
			FileUtil.deleteFile(TMP_FF_NAME);
			FileOutputStream laFile =
				new FileOutputStream(TMP_FF_NAME, true);
			PrintWriter laPWOut = new PrintWriter(laFile);
			laPWOut.print(ReportConstant.FF);
			laPWOut.flush();
			laPWOut.close();
			laFile.close();
		}
		catch (IOException aeIOEx)
		{
			System.out.println(
				"Got an IOException writing to " + TMP_FF_NAME);
		}
	}

	/**
	 * Return the ivjRTSCancel property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnCancel()
	{
		if (ivjbtnCancel == null)
		{
			try
			{
				ivjbtnCancel = new RTSButton();
				ivjbtnCancel.setName("ivjbtnCancel");
				ivjbtnCancel.setText(CommonConstant.BTN_TXT_CANCEL);
				ivjbtnCancel.setMaximumSize(new Dimension(61, 25));
				ivjbtnCancel.setPreferredSize(new Dimension(61, 25));
				ivjbtnCancel.setMinimumSize(new Dimension(61, 25));
				// user code begin {1}
				ivjbtnCancel.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnCancel;
	}

	/**
	 * Return the ivjRTSEnter property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnEnter()
	{
		if (ivjbtnEnter == null)
		{
			try
			{
				ivjbtnEnter = new RTSButton();
				ivjbtnEnter.setName("ivjbtnEnter");
				ivjbtnEnter.setText(CommonConstant.BTN_TXT_ENTER);
				ivjbtnEnter.setMaximumSize(new Dimension(61, 25));
				ivjbtnEnter.setPreferredSize(new Dimension(61, 25));
				ivjbtnEnter.setMinimumSize(new Dimension(61, 25));
				// user code begin {1}
				ivjbtnEnter.addActionListener(this);
				this.getRootPane().setDefaultButton(ivjbtnEnter);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnEnter;
	}

	/**
	 * Return the ivjbtnFirst property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnFirst()
	{
		if (ivjbtnFirst == null)
		{
			try
			{
				ivjbtnFirst = new RTSButton();
				ivjbtnFirst.setName("ivjbtnFirst");
				ivjbtnFirst.setMnemonic(KeyEvent.VK_F);
				ivjbtnFirst.setText(CommonConstant.BTN_TXT_FIRST);
				ivjbtnFirst.setMaximumSize(new Dimension(61, 25));
				ivjbtnFirst.setPreferredSize(new Dimension(61, 25));
				ivjbtnFirst.setMinimumSize(new Dimension(61, 25));
				// user code begin {1}
				ivjbtnFirst.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnFirst;
	}

	/**
	 * Return the ivjRTSHelp property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnHelp()
	{
		if (ivjbtnHelp == null)
		{
			try
			{
				ivjbtnHelp = new RTSButton();
				ivjbtnHelp.setName("ivjbtnHelp");
				ivjbtnHelp.setMnemonic(KeyEvent.VK_H);
				ivjbtnHelp.setText(CommonConstant.BTN_TXT_HELP);
				ivjbtnHelp.setMaximumSize(new Dimension(61, 25));
				ivjbtnHelp.setPreferredSize(new Dimension(61, 25));
				ivjbtnHelp.setMinimumSize(new Dimension(61, 25));
				// user code begin {1}
				ivjbtnHelp.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnHelp;
	}

	/**
	 * Return the ivjbtnLast property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnLast()
	{
		if (ivjbtnLast == null)
		{
			try
			{
				ivjbtnLast = new RTSButton();
				ivjbtnLast.setName("ivjbtnLast");
				ivjbtnLast.setMnemonic(KeyEvent.VK_L);
				ivjbtnLast.setText(CommonConstant.BTN_TXT_LAST);
				ivjbtnLast.setMaximumSize(new Dimension(61, 25));
				ivjbtnLast.setPreferredSize(new Dimension(61, 25));
				ivjbtnLast.setMinimumSize(new Dimension(61, 25));
				// user code begin {1}
				ivjbtnLast.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnLast;
	}

	/**
	 * Return the ivjbtnNext property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnNext()
	{
		if (ivjbtnNext == null)
		{
			try
			{
				ivjbtnNext = new RTSButton();
				ivjbtnNext.setName("ivjbtnNext");
				ivjbtnNext.setMnemonic(KeyEvent.VK_N);
				ivjbtnNext.setText(CommonConstant.BTN_TXT_NEXT);
				ivjbtnNext.setMaximumSize(new Dimension(61, 25));
				ivjbtnNext.setPreferredSize(new Dimension(61, 25));
				ivjbtnNext.setMinimumSize(new Dimension(61, 25));
				// user code begin {1}
				ivjbtnNext.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnNext;
	}

	/**
	 * Return the ivjbtnPrev property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnPrev()
	{
		if (ivjbtnPrev == null)
		{
			try
			{
				ivjbtnPrev = new RTSButton();
				ivjbtnPrev.setName("ivjbtnPrev");
				ivjbtnPrev.setMnemonic(KeyEvent.VK_V);
				ivjbtnPrev.setText(CommonConstant.BTN_TXT_PREV);
				ivjbtnPrev.setMaximumSize(new Dimension(61, 25));
				ivjbtnPrev.setPreferredSize(new Dimension(61, 25));
				ivjbtnPrev.setMinimumSize(new Dimension(61, 25));
				// user code begin {1}
				ivjbtnPrev.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnPrev;
	}

	/**
	 * Return the ivjbtnPrint property value.
	 * 
	 * @return RTSButton
	 */
	private RTSButton getbtnPrint()
	{
		if (ivjbtnPrint == null)
		{
			try
			{
				ivjbtnPrint = new RTSButton();
				ivjbtnPrint.setName("ivjbtnPrint");
				ivjbtnPrint.setMnemonic(KeyEvent.VK_P);
				ivjbtnPrint.setText(CommonConstant.BTN_TXT_PRINT);
				ivjbtnPrint.setMaximumSize(new Dimension(61, 25));
				ivjbtnPrint.setPreferredSize(new Dimension(61, 25));
				ivjbtnPrint.setMinimumSize(new Dimension(61, 25));
				ivjbtnPrint.setRequestFocusEnabled(true);
				// user code begin {1}
				ivjbtnPrint.addActionListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjbtnPrint;
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
				ivjJPanel1.setLayout(new GridBagLayout());
				ivjJPanel1.setAlignmentY(Component.CENTER_ALIGNMENT);
				ivjJPanel1.setMaximumSize(
					new Dimension(2147483647, 2147483647));
				ivjJPanel1.setPreferredSize(new Dimension(586, 51));
				ivjJPanel1.setAlignmentX(Component.CENTER_ALIGNMENT);
				ivjJPanel1.setMinimumSize(new Dimension(586, 51));

				GridBagConstraints laConstraintsbtnPrint =
					new GridBagConstraints();
				laConstraintsbtnPrint.gridx = 1;
				laConstraintsbtnPrint.gridy = 1;
				laConstraintsbtnPrint.ipadx = 3;
				laConstraintsbtnPrint.insets =
					new Insets(14, 10, 12, 3);
				getJPanel1().add(getbtnPrint(), laConstraintsbtnPrint);

				GridBagConstraints laConstraintsRTSEnter =
					new GridBagConstraints();
				laConstraintsRTSEnter.gridx = 2;
				laConstraintsRTSEnter.gridy = 1;
				laConstraintsRTSEnter.ipadx = 3;
				laConstraintsRTSEnter.insets = new Insets(14, 3, 12, 3);
				getJPanel1().add(getbtnEnter(), laConstraintsRTSEnter);

				GridBagConstraints laConstraintsRTSCancel =
					new GridBagConstraints();
				laConstraintsRTSCancel.gridx = 3;
				laConstraintsRTSCancel.gridy = 1;
				laConstraintsRTSCancel.ipadx = 12;
				laConstraintsRTSCancel.insets =
					new Insets(14, 3, 12, 3);
				getJPanel1().add(
					getbtnCancel(),
					laConstraintsRTSCancel);

				GridBagConstraints laConstraintsRTSHelp =
					new GridBagConstraints();
				laConstraintsRTSHelp.gridx = 4;
				laConstraintsRTSHelp.gridy = 1;
				laConstraintsRTSHelp.ipadx = 5;
				laConstraintsRTSHelp.insets = new Insets(14, 3, 12, 3);
				getJPanel1().add(getbtnHelp(), laConstraintsRTSHelp);

				GridBagConstraints laConstraintsbtnPrev =
					new GridBagConstraints();
				laConstraintsbtnPrev.gridx = 6;
				laConstraintsbtnPrev.gridy = 1;
				laConstraintsbtnPrev.ipadx = 1;
				laConstraintsbtnPrev.insets = new Insets(14, 3, 12, 3);
				getJPanel1().add(getbtnPrev(), laConstraintsbtnPrev);

				GridBagConstraints laConstraintsbtnNext =
					new GridBagConstraints();
				laConstraintsbtnNext.gridx = 7;
				laConstraintsbtnNext.gridy = 1;
				laConstraintsbtnNext.ipadx = 3;
				laConstraintsbtnNext.insets = new Insets(14, 3, 12, 3);
				getJPanel1().add(getbtnNext(), laConstraintsbtnNext);

				GridBagConstraints laConstraintsbtnFirst =
					new GridBagConstraints();
				laConstraintsbtnFirst.gridx = 5;
				laConstraintsbtnFirst.gridy = 1;
				laConstraintsbtnFirst.ipadx = 5;
				laConstraintsbtnFirst.insets = new Insets(14, 3, 12, 3);
				getJPanel1().add(getbtnFirst(), laConstraintsbtnFirst);

				GridBagConstraints laConstraintsbtnLast =
					new GridBagConstraints();
				laConstraintsbtnLast.gridx = 8;
				laConstraintsbtnLast.gridy = 1;
				laConstraintsbtnLast.ipadx = 5;
				laConstraintsbtnLast.insets = new Insets(14, 3, 12, 10);
				getJPanel1().add(getbtnLast(), laConstraintsbtnLast);

				// user code begin {1}
				// defect 10086 
				caButtonGroup.add(getbtnPrint());
				caButtonGroup.add(getbtnEnter());
				caButtonGroup.add(getbtnCancel());
				caButtonGroup.add(getbtnHelp());
				caButtonGroup.add(getbtnFirst());
				caButtonGroup.add(getbtnPrev());
				caButtonGroup.add(getbtnNext());
				caButtonGroup.add(getbtnLast());
				// end defect 10086 
				// user code end
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
				ivjJScrollPane1.setName("ivjJScrollPane1");
				ivjJScrollPane1.setAutoscrolls(true);
				ivjJScrollPane1.setOpaque(true);
				ivjJScrollPane1.setPreferredSize(new Dimension(14, 26));
				getJScrollPane1().setViewportView(getRTSTextPane1());
				// user code begin {1}
				ivjJScrollPane1
					.getVerticalScrollBar()
					.addAdjustmentListener(
					this);
				// user code end
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
				ivjRTSDialogBoxContentPane.setLayout(
					new BorderLayout());
				getRTSDialogBoxContentPane().add(
					getJScrollPane1(),
					"Center");
				getRTSDialogBoxContentPane().add(getJPanel1(), "South");
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
		return ivjRTSDialogBoxContentPane;
	}

	/**
	 * Return the RTSTextPane1 property value.
	 * You must pass dsd to RTSTextPane if you rebuilf the class
	 * visually. ex. RTSTextPane(dsd);
	 * 
	 * @return RTSTextPane
	 */
	private RTSTextPane getRTSTextPane1()
	{
		if (ivjRTSTextPane1 == null)
		{
			try
			{
				ivjRTSTextPane1 =
					new RTSTextPane(
						caDSD = new DefaultStyledDocument());
				ivjRTSTextPane1.setName("ivjRTSTextPane1");
				ivjRTSTextPane1.setBounds(0, 0, 575, 326);
				ivjRTSTextPane1.setEditable(false);
				ivjRTSTextPane1.setRequestFocusEnabled(false);
				// user code begin {1}
				ivjRTSTextPane1.setRequestFocusEnabled(true);
				ivjRTSTextPane1.addKeyListener(this);
				// user code end
			}
			catch (Throwable aeIVJExc)
			{
				// user code begin {2}
				// user code end
				handleException(aeIVJExc);
			}
		}
		return ivjRTSTextPane1;
	}

	/**
	 * Handle Enter Btn
	 */
	private void handleEnterBtn()
	{
		ciCounter++;
		if (ciCounter < cvViewReports.size())
		{
			GeneralSearchData laGSD =
				(GeneralSearchData) cvViewReports.get(ciCounter);
			csReportName = laGSD.getKey3();
			csFileName = laGSD.getKey1();
			int liOrientation = laGSD.getIntKey2();

			// defect 10086
			setLinesPerPage(liOrientation);
			// end defect 10086 

			initPage();
		}
		else
		{
			getController().processData(
				AbstractViewController.ENTER,
				caRptSearchData);
		}
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
	 * handleFirstBtn
	 */
	public void handleFirstBtn()
	{
		ciPageNum = 1;
		ivjRTSTextPane1.setText("");
		try
		{
			caDSD.insertString(
				caDSD.getLength(),
				cvReport.elementAt(ciPageNum - 1).toString(),
				caAttr);
		}
		catch (BadLocationException aeBLEx)
		{
			System.err.println(ERROR_ON_TEXT_INSERT);
		}
		ivjbtnNext.setEnabled(true);
		ivjbtnLast.setEnabled(true);
		ivjbtnPrev.setEnabled(false);
		ivjbtnFirst.setEnabled(false);
		ivjRTSTextPane1.setCaretPosition(1);
	}

	/**
	 * handleLastBtn
	 */
	public void handleLastBtn()
	{
		ciPageNum = ciPages;
		ivjRTSTextPane1.setText("");
		try
		{
			caDSD.insertString(
				caDSD.getLength(),
				cvReport.elementAt(ciPageNum - 1).toString(),
				caAttr);
		}
		catch (BadLocationException aeBLEx)
		{
			System.err.println(ERROR_ON_TEXT_INSERT);
		}

		ivjbtnNext.setEnabled(false);
		ivjbtnLast.setEnabled(false);
		ivjbtnPrev.setEnabled(true);
		ivjbtnFirst.setEnabled(true);
		ivjRTSTextPane1.setCaretPosition(1);
	}

	/**
	 * handleNextBtn
	 */
	public void handleNextBtn()
	{
		ciPageNum++;
		ivjRTSTextPane1.setText("");
		try
		{
			caDSD.insertString(
				caDSD.getLength(),
				cvReport.elementAt(ciPageNum - 1).toString(),
				caAttr);
		}
		catch (BadLocationException aeBLEx)
		{
			System.err.println(ERROR_ON_TEXT_INSERT);
		}
		if (ciPageNum < ciPages)
		{
			ivjbtnNext.setEnabled(true);
			ivjbtnLast.setEnabled(true);
		}
		else
		{
			ivjbtnNext.setEnabled(false);
			ivjbtnLast.setEnabled(false);
		}
		ivjbtnPrev.setEnabled(true);
		ivjbtnFirst.setEnabled(true);
		ivjRTSTextPane1.setCaretPosition(1);
	}

	/**
	 * handlePrevBtn
	 */
	public void handlePrevBtn()
	{
		ivjRTSTextPane1.setText("");
		ciPageNum--;
		try
		{
			caDSD.insertString(
				caDSD.getLength(),
				cvReport.elementAt(ciPageNum - 1).toString(),
				caAttr);
		}
		catch (BadLocationException aeBLEx)
		{
			System.err.println(ERROR_ON_TEXT_INSERT);
		}
		if (ciPageNum < 2)
		{
			ivjbtnPrev.setEnabled(false);
			ivjbtnFirst.setEnabled(false);
		}
		else
		{
			ivjbtnPrev.setEnabled(true);
			ivjbtnFirst.setEnabled(true);
		}
		ivjbtnNext.setEnabled(true);
		ivjbtnLast.setEnabled(true);
		//flg is set in keypressed for arrow keys.
		if (!cbFlg)
		{
			ivjRTSTextPane1.setCaretPosition(1);
		}
	}

	/**
	 * handlePrintBtn.
	 */
	public void handlePrintBtn()
	{
		// defect 10086 
		// Do not show page options on Certificates,
		//  e.g. COA, Salvage, NRCOT 
		if (isCertificate())
		{
			caRptSearchData.setIntKey4(ReportConstant.PRINT_ALL);
		}
		else
		{
			caRptSearchData.setIntKey4(
				ReportConstant.NO_PRINT_OPTION_SELECTED);

			getController().processData(
				VCPreviewReportRPR000.PRINT_RANGE,
				caRptSearchData);
		}

		// Process Data Returned from RPR008 
		if (caRptSearchData.getIntKey4()
			!= ReportConstant.NO_PRINT_OPTION_SELECTED)
		{
			boolean lbPrint = true;

			Print laPrint = new Print();
			String lsParsedFilename =
				laPrint.parseFileName(csFileName, 4);
			csPrintFileName = csFileName;

			boolean lbSendParsedFileName =
				lsParsedFilename.equalsIgnoreCase(TransCdConstant.TTLP)
					|| lsParsedFilename.equalsIgnoreCase(
						TransCdConstant.VOID);

			if (caRptSearchData.getIntKey4()
				!= ReportConstant.PRINT_ALL)
			{
				csPrintFileName = TMP_PRINT_NAME;

				if (caRptSearchData.getIntKey4()
					== ReportConstant.PRINT_CURRENT)
				{
					caRptSearchData.setKey4("" + ciPageNum);
				}
				lbPrint = createPrintFileforSelection();
			}

			if (lbPrint)
			{
				try
				{

					// defect 3994
					// Added barcode to Title Package Report needed to 
					// generate PCL for the barcode if this report is 
					// the Title Package Report
					// If the file name's first four character are TTLP 
					// then it contains a barcode so then the barcode 
					// PCL needes to be generated

					// defect 6848, 6898
					// added void to the if statement.  Void report duplicate
					// was handled below
					if (lbSendParsedFileName)
					{
						// Now passing the first 4 characters of the filename as
						// the trans code for both Title Package and Void
						// both of these reports are special cased because they
						// either contain a barcode or duplicates are needed
						laPrint.sendToPrinter(
							csPrintFileName,
							lsParsedFilename);
					}
					// end defect 6848, 6898
					else
					{
						laPrint.sendToPrinter(csPrintFileName);

						// MF Reports don't have FF at end.
						if (isMFReport()
							&& caRptSearchData.getIntKey4()
								== ReportConstant.PRINT_ALL)
						{
							createFFforMFReports();
							laPrint.sendToPrinter(TMP_FF_NAME);
						}
					}
					// end defect 3994
				}
				catch (RTSException aeRTSEx)
				{
					aeRTSEx.displayError(this);
				}
			}
			handleEnterBtn();
		}
		// end defect 10086 
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
			setName("FrmPreviewReportRPR000");
			setDefaultCloseOperation(
				WindowConstants.DO_NOTHING_ON_CLOSE);
			setLocation(0, 0);
			// defect 11333
			int liWidth = (int) getToolkit().getScreenSize().getWidth();
			int liHeight = (int) getToolkit().getScreenSize().getHeight() - 30;
			if (liWidth > DEFAULT_SCREEN_WIDTH)
			{
				liWidth = DEFAULT_SCREEN_WIDTH;
			}
			if (liHeight > DEFAULT_SCREEN_HEIGHT)
			{
				liHeight = DEFAULT_SCREEN_HEIGHT;
			}
			setSize(liWidth, liHeight);
			// end defect 11333
			setTitle(FRM_TITLE_PREFIX);
			setContentPane(getRTSDialogBoxContentPane());
		}
		catch (Throwable aeIVJExc)
		{
			handleException(aeIVJExc);
		}
		// defect 11333
		// user code begin {2}
//		try
//		{
			// defect 7768
//			setLocation(0, 0);
//			setSize(
//				(int) getToolkit().getScreenSize().getWidth(),
//				(int) getToolkit().getScreenSize().getHeight() - 1);
//			setSize(800, 600);
//			// end defect 7768
//		}
//		catch (Throwable aeIVJExc)
//		{
//			handleException(aeIVJExc);
//		}
		// user code end
		// end defect 11333
	}

	/**
	 * initPage
	 */
	public void initPage()
	{
		File laFile;
		FileReader laIn = null;
		BufferedReader laBR = null;
		String lsLine = null;
		ivjbtnPrev.setEnabled(false);
		ivjbtnFirst.setEnabled(false);
		ivjbtnNext.setEnabled(false);
		ivjbtnLast.setEnabled(false);
		cvReport = new Vector();
		ciPages = 1;
		ciLineCount = 1;
		ciPageNum = 1;
		// defect 10086
		csPrintCtrl = new String();
		// char lcFormFeedChar = '\f';
		// end defect 10086  
		boolean lbNeedNewPage = false;
		boolean lbFirstFile = true;
		caAttr = new SimpleAttributeSet();
		StyleConstants.setFontFamily(caAttr, "MonoSpaced");
		StyleConstants.setFontSize(caAttr, 12);
		StyleConstants.setForeground(caAttr, Color.black);
		ivjRTSTextPane1.setText("");
		try
		{
			laFile = new File(csFileName); // Create a file object
			laIn = new FileReader(laFile);
			laBR = new BufferedReader(laIn);
			StringBuffer lsReportPage = new StringBuffer();
			boolean lbReadPrintLine = false;
			int liNoOfBlankSpaces = 132;

			// defect 10086 
			setMFReport();
			boolean lbEndCtrl = false;

			while ((lsLine = laBR.readLine()) != null)
			{
				// Save Print Control characters for print page  
				if (!lbEndCtrl)
				{
					int liRptHeader = lsLine.indexOf(REPORT_NO_PREFIX);

					if (liRptHeader == -1)
					{
						csPrintCtrl = csPrintCtrl + lsLine;
					}
					else
					{
						csPrintCtrl =
							csPrintCtrl
								+ lsLine.substring(0, liRptHeader);
						lbEndCtrl = true;
					}
				}
				// end defect 10086

				// if this is a mainframe report and it
				// isn't a blank line	
				if (isMFReport() && lsLine.length() > 0)
				{
					// initialize the lbNeedNewPage to false
					// to prevent too many page breaks.
					lbNeedNewPage = false;

					// grab the first char of the line so we can
					// find out if it is a form feed char
					char lcCheckFormFeedChar = lsLine.charAt(0);

					// defect 10086
					// if it is a form feed char
					// if (lcCheckFormFeedChar == lcFormFeedChar)
					if (lcCheckFormFeedChar == ReportConstant.FF)
					{
						// check the substring for RTS.  Usually on the 
						// first line of the file there is a form feed 
						// character plus printer commands. The first file 
						// has two form feed commands, one on the first line 
						// and one on the second line.  We do not want a 
						// page break for either one of these.  A blank 
						// screen will appear before the data is displayed 
						// if we do a pagebreak for each of these.
						//
						// After the initial form feed + RTS is found, we 
						// will move false to the boolean lbFirstFile.  
						// After that we will want to have a page break for 
						// each new report encountered.

						// Use Trim() as MF Reports not consistent in 
						// number of leading spaces.
						String lsTmpLine = lsLine.trim();

						if (lsTmpLine
							.substring(0, 4)
							.equals(REPORT_NO_PREFIX))
						{
							lbNeedNewPage = !lbFirstFile;
							lbFirstFile = false;
						}
						// end defect 10086 
					}
				}
				// added code for mainframe display.  If it is a mainframe 
				// report and it doesn't require a new page append the line
				// to lsReportPage. If it is not a mainframe report use the 
				// line count to determine page breaks.
				if ((isMFReport() && !lbNeedNewPage)
					|| (!isMFReport() && ciLineCount <= ciLinesPerPage))
				{
					if (lbReadPrintLine)
					{
						// defect 6181
						//	Skip print line if first char = ESC
						if ((lsLine != null && lsLine.equals(""))
							|| !ReportConstant.ESC_CHAR.equals(
								lsLine.substring(0, 1)))
							// end defect 6181
						{
							liNoOfBlankSpaces =
								liNoOfBlankSpaces - lsLine.length();
							// defect 10086
							// Use SYSTEM_LINE_SEPARATOR
							lsReportPage.append(
								lsLine
									+ addBlanks(liNoOfBlankSpaces)
									+ CommonConstant
										.SYSTEM_LINE_SEPARATOR);
							// end defect 10086
							ciLineCount++;
						}
					}
					else
					{
						lbReadPrintLine = true;
					}
				}
				else
				{
					cvReport.addElement(lsReportPage);
					lsReportPage = new StringBuffer();
					// defect 10086
					// Use SYSTEM_LINE_SEPARATOR
					lsReportPage.append(
						lsLine + CommonConstant.SYSTEM_LINE_SEPARATOR);
					// end defect 10086 
					ciPages++;
					ciLineCount = 2;
					ivjbtnNext.setEnabled(true);
					ivjbtnLast.setEnabled(true);
				}
			}
			if (lsReportPage != null && lsReportPage.length() == 0)
			{
				// defect 10086
				// Use SYSTEM_LINE_SEPARATOR
				lsReportPage.append(
					addBlanks(liNoOfBlankSpaces)
						+ CommonConstant.SYSTEM_LINE_SEPARATOR);
				if (isMFReport())
				{
					lsReportPage.append(ReportConstant.FF);
				}
				// end defect 10086
			}

			cvReport.addElement(lsReportPage);
			caDSD.insertString(
				caDSD.getLength(),
				cvReport.elementAt(ciPageNum - 1).toString(),
				caAttr);
			setTitle(FRM_TITLE_PREFIX + csReportName.toUpperCase());

			// Set the window title
			if (isMFReport())
			{
				ivjRTSTextPane1.setCaretPosition(0);
				// Go to start of file
			}
			else if (
				lsReportPage != null && lsReportPage.length() != 0)
			{
				ivjRTSTextPane1.setCaretPosition(1);
				// Go to start of file
			}
			ivjRTSTextPane1.setEditable(false);
			ivjRTSTextPane1.requestFocus();
		}
		// Display messages if something goes wrong
		catch (BadLocationException aeBLE)
		{
			System.err.println(ERROR_ON_TEXT_INSERT);
		}
		catch (IOException aeIOEx)
		{
			ivjRTSTextPane1.setText(
				aeIOEx.getClass().getName()
					+ ": "
					+ aeIOEx.getMessage());
			this.setTitle(
				"FileViewer: " + csFileName + ": I/O Exception");
		}
		// Always be sure to close the input stream!
		finally
		{
			try
			{
				if (laIn != null)
				{
					laIn.close();
				}
			}
			catch (IOException aeIOEx)
			{
				// empty code block
			}
		}
	}

	/**
	 * Return boolean to denote if Certificate
	 */
	private boolean isCertificate()
	{
		return (
			caRptSearchData.getKey1() != null
				&& caRptSearchData.getKey1().indexOf(
					ReportConstant.EXTENSION_CERTS)
					>= 0);
	}

	/**
	 * Return cbMFReport
	 */
	private boolean isMFReport()
	{
		return cbMFReport;
	}

	/**
	 * keyPressed
	 *
	 * @param aaKE KeyEvent
	 */
	public void keyPressed(KeyEvent aaKE)
	{
		super.keyPressed(aaKE);

		// defect 10086
		// Remove code handled by ButtonGroup 
		// end defect 10086

		//****** Code to process up down keys for text pane *******\\
		if (aaKE.getSource() instanceof RTSTextPane)
		{
			RTSTextPane laRTSTP = getRTSTextPane1();
			//units for one row
			int liOneRow =
				laRTSTP.getScrollableUnitIncrement(
					laRTSTP.getVisibleRect(),
					SwingConstants.VERTICAL,
					-1);
			//units for one column
			int liOneCol =
				laRTSTP.getScrollableUnitIncrement(
					laRTSTP.getVisibleRect(),
					SwingConstants.HORIZONTAL,
					-1);

			JScrollBar laSB = getJScrollPane1().getVerticalScrollBar();
			Rectangle laRect = laRTSTP.getVisibleRect();

			if (aaKE.getKeyCode() == KeyEvent.VK_DOWN
				|| aaKE.getKeyCode() == KeyEvent.VK_PAGE_DOWN)
			{
				// If it has reached at the end of the page,
				// show new page
				if (laSB.getHeight() + laSB.getValue()
					== laRTSTP.getHeight())
				{
					if (getbtnNext().isEnabled())
					{
						aaKE.consume();
						getbtnNext().doClick();
						return;
					}
				}
				if (aaKE.getKeyCode() == KeyEvent.VK_DOWN)
				{
					laRect.setLocation(
						(int) (laRect.getX()),
						(int) (laRect.getY()) + liOneRow);
					laRTSTP.scrollRectToVisible(laRect);
					aaKE.consume();
				}
			}
			if (aaKE.getKeyCode() == KeyEvent.VK_UP
				|| aaKE.getKeyCode() == KeyEvent.VK_PAGE_UP)
			{
				// If it has reached at the begining of the page,
				// show new page
				if (laSB.getValue() == 0)
				{
					if (getbtnPrev().isEnabled())
					{
						aaKE.consume();
						// this flag is used to tell doClick method,
						// not to reset viewable pos.
						cbFlg = true;
						getbtnPrev().doClick();
						cbFlg = false;
						laSB.setValue(
							laRTSTP.getDocument().getLength());
						return;
					}
				}
				if (aaKE.getKeyCode() == KeyEvent.VK_UP)
				{
					laRect.setLocation(
						(int) (laRect.getX()),
						(int) (laRect.getY()) - liOneRow);
					laRTSTP.scrollRectToVisible(laRect);
					aaKE.consume();
				}
			}
			if (aaKE.getKeyCode() == KeyEvent.VK_LEFT)
			{
				laRect.setLocation(
					(int) (laRect.getX()) - liOneCol,
					(int) (laRect.getY()));
				laRTSTP.scrollRectToVisible(laRect);
				aaKE.consume();
			}
			if (aaKE.getKeyCode() == KeyEvent.VK_RIGHT)
			{
				laRect.setLocation(
					(int) (laRect.getX()) + liOneCol,
					(int) (laRect.getY()));
				laRTSTP.scrollRectToVisible(laRect);
				aaKE.consume();
			}
		}
		// defect 10086
		// Remove code handled by ButtonGroup 
		// end defect 10086 
	}

	/**
	 * all subclasses must implement this method - it sets the 
	 * data on the screen and is how the controller relays information
	 * to the view
	 * 
	 * @param aaDataObject Object
	 */
	public void setData(Object aaDataObject)
	{
		if (aaDataObject instanceof ReportSearchData)
		{
			caRptSearchData = (ReportSearchData) aaDataObject;
			cvViewReports = (Vector) caRptSearchData.getVector();
		}
		else
		{
			cvViewReports = (Vector) aaDataObject;
			if (cvViewReports.get(0) instanceof ReportSearchData)
			{
				caRptSearchData =
					(ReportSearchData) cvViewReports.get(0);
			}
		}

		csFileName =
			((GeneralSearchData) cvViewReports.get(0)).getKey1();
		csReportName =
			((GeneralSearchData) cvViewReports.get(0)).getKey3();

		// defect 10590  
		if (csFileName.equalsIgnoreCase(ReportConstant.PREVIEW_RCPT_NAME))
		{
			getbtnCancel().setEnabled(false);
			getbtnPrint().setEnabled(false); 	
		}
		// end defect 10590 
			
		// defect 10086
		setLinesPerPage(
			((GeneralSearchData) cvViewReports.get(0)).getIntKey2());
		// end defect 10086 

		initPage();
	}

	/**
	 * Set Lines per page based upon orientation
	 *   
	 * @param aiOrientation
	 */
	private void setLinesPerPage(int aiOrientation)
	{
		if (aiOrientation == ReportConstant.PORTRAIT)
		{
			ciLinesPerPage = ReportConstant.LINES_PER_PAGE_PORTRAIT;
		}
		else
		{
			ciLinesPerPage = ReportConstant.LINES_PER_PAGE_LANDSCAPE;
		}
	}

	/** 
	 * Set cbMFReport according to whether the file begin with "MF" 
	 * 
	 * @return boolean 
	 */
	private void setMFReport()
	{
		cbMFReport =
			csFileName.substring(
				START_MF_RPT_PREFIX,
				END_MF_RPT_PREFIX).equals(
				MF_RPT_PREFIX)
				|| csFileName.substring(
					START_MF_RPT_PREFIX,
					END_MF_RPT_PREFIX).equals(
					MF_RPT_AUX_PREFIX);
	}
}
