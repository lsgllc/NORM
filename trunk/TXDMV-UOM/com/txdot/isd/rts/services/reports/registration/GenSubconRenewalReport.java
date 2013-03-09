package com.txdot.isd.rts.services.reports.registration;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Vector;

import com.txdot.isd.rts.services.cache.SubcontractorCache;
import com.txdot.isd.rts.services.data.*;
import com.txdot.isd.rts.services.exception.RTSException;
import com.txdot.isd.rts.services.reports.ColumnHeader;
import com.txdot.isd.rts.services.reports.FrmPreviewReport;
import com.txdot.isd.rts.services.reports.ReportProperties;
import com.txdot.isd.rts.services.reports.ReportTemplate;
import com.txdot.isd.rts.services.util.Dollar;
import com.txdot.isd.rts.services.util.RTSDate;
import com.txdot.isd.rts.services.util.SystemProperty;
import com.txdot.isd.rts.services.util.UtilityMethods;
import com.txdot.isd.rts.services.util.constants.GeneralConstant;
import com.txdot.isd.rts.services.util.constants.InventoryConstant;

import com.txdot.isd.rts.server.inventory.InventoryServerBusiness;

/*
 * GenSubconRenewalReport.java
 * 
 * (c) Texas Department of Transporation 2001
 * ---------------------------------------------------------------------
 * Change History:
 * Name			Date		Description
 * ------------	-----------	--------------------------------------------
 * S Govindappa	05/09/2002	Changing formatReport
 * 							(SubcontractorRenewalCacheData,boolean) to
 * 							sort transactions by TransID
 * 							defect 3828 
 * B Brown     	09/20/2002	by adding to the formatReport method
 *                          a k++ statement in the print loop to keep 
 * 							track of the "***" print lines when there 
 * 							was a break in inventory sequence.
 * 							defect 4687 
 * K Harrell    11/02/2002  defect 5000  (reversal of defect 3828)
 * K Salvi		03/10/2003	Print blank when scanner/processor id is 
 * 							not applicable.
 * 							defect 5754  
 * K Harrell	01/29/2004	5.2.0 Merge.  See PCR 34 comments.
 * 							Imported from 5.2.0; Modified method calls 
 * 							to match changes to SubcontractorHdrData. 
 * 							Modified variable names to meet standards.
 * 							Ver 5.2.0
 * K Harrell	04/27/2004	Remove double spacing from Subcon Reports
 *							modify formatReport(SubcontractorRenewalCacheData,int)
 *							defect 7041.  Ver 5.2.0
 * K Harrell	07/31/2004	Modify to show seconds on Export Dates
 *							modify formatDateTime(),
 *							formatReport(SubcontractorRenewalCacheData,int)
 *							defect 7412  Ver 5.2.1
 * K Harrell	08/31/2004  Modify to more closely map to RSPS Subcon Rpt
 *							add insertSortProcessDateTime()
 *							modify formatReport(Subcon...,int)
 *							Correct column numbers on total line
 *							defect 7413
 * Jeff S.		09/28/2004	Made room for UserID of 8 and not 7
 *							modify formatReport(,)
 *							defect 7568 Ver 5.2.1
 * K Harrell	10/11/2004	Modify constant to SUBCON_RSPS_REPORT
 *							vs. PRELIMINARY
 *							defect Ver 5.2.1 
 * K Harrell	06/19/2005	Reorganize imports to address movement 
 * 							of services.cache.*Data to services.data
 * 							defect 7884 Ver 5.2.3
 * S Johnston	06/30/2005	Code Cleanup for Java 1.4.2 upgrade
 * 							defect 7896	Ver 5.2.3
 * K Harrell	10/11/2006	Print 0.00 fees 
 * 							modify formatReport(SubcontractorRenewalCacheData,int)
 * 							defect 8900 Ver Exempts                     
 * B Hargrove 	06/01/2009 	Add Flashdrive option to RSPS Subcon. Change
 * 							verbiage from 'diskette' to 'RSPS'. 
 *                   		modify formatReport(),
 * 							rename DISKETTE_SUBCON_REPORT to 
 * 							SUBCON_RSPS_REPORT
 * 							defect 10064 Ver Defect_POS_F  
 * K Harrell	08/15/2009	Implement new generateFooter(boolean) 
 * 							No return required from handlePageBreak()
 * 							modify formatReport(SubcontractorRenewalCacheData,int),
 * 							 handlePageBreak() 
 * 							defect 8628 Ver Defect_POS_F       
 * K Harrell	02/18/2010 	Implement new SubcontractorData
 * 							modify formatReport(SubcontractorRenewalCacheData,int)
 * 							defect 10161 Ver POS_640 
 * ---------------------------------------------------------------------
 */
/**
 * @version	POS_640			02/18/2010
 * @author	Administrator 
 * <br>Creation Date:		08/31/2001 13:52:44
 * 
 * Generates the Subcontractor Renewal Report. 
 *  
 * How the user generates the Subcontractor Renewal Report:
 *
 * 1) User selects Customer option
 * 2) User selects Registration only suboption
 * 3) User selects Subcontractor Renewal suboption
 * 4) Subcontractor ID is entered - enter key pressed
 * 5) List of subcontractor's allocated inventory is displayed 
 *    in lower window, or a  'subcontractor not found' message 
 *    is displayed in a popup. 
 * 6) An issue date is entered in the same window subcontractor
 *    ID was entered in. The issue date helps the system determine
 *    the proper fees.
 * 7) Subcontractor renewal screen pops up, and it brings into the
 *    first input field on the screen line, the first sticker number
 *    from their inventory list. There are also three buttons active 
 *    to press at this time:Plate, Sticker, or Plate and Sticker.	
 * 8) User types in next to the sticker number, the current plate 
 *    number, expiration month, registration class, and the fee.
 *    Enter key is pressed.
 * 9) Registration information is returned in bottom window, with a 
 *	  check mark next to the info line if information is valid.
 * 10)If a registration information line is highlighted, the user can
 *    delete or modify that information.
 * 11)When the listbox button is clicked on, the draft button is 
 *	  highlighted for a draft report. If the listbox button is not 
 *	  highlighted, then the report will be a final.
 * 12)Once enter is pressed at this point, the total fees for that 
 *    subcontractor pops up in a window.
 * 13)Yes is clicked on, then the report window pops up with the report.
 * 
 */
public class GenSubconRenewalReport extends ReportTemplate
{
	private Dollar caTotalAmt = new Dollar(0.00);

	// Issue Date
	private static final int COLUMN1_STARTPT = 1;
	// Plate Number
	private static final int COLUMN2_STARTPT = 14;
	// Prev Plate Number
	private static final int COLUMN3_STARTPT = 23;
	// VIN
	private static final int COLUMN4_STARTPT = 39;
	// Exp Mo/Yr
	private static final int COLUMN5_STARTPT = 56;
	// Stkr
	private static final int COLUMN6_STARTPT = 64;
	// Plt
	private static final int COLUMN7_STARTPT = 71;
	// defect 7568
	// Username is 8 and not 7
	private static final int COLUMN8_STARTPT = 89;
	// end defect 7568
	private static final int COLUMN9_STARTPT = 98;
	private static final int COLUMN10_STARTPT = 108;
	private static final int COLUMN11_STARTPT = 118;
	private static final int COLUMN12_STARTPT = 124;
	private static final int PAYMENT_STARTPT = 109;
	private static final int FOOTER_LINES = 2;
	private static final String DOUBLE_DASH =
		"===================================";
	private static final String SHORT_DOUBLE_DASH = "===========";
	private static final String ISSUEDATE = "ISSUE DATE";
	private static final String PREV = "PREV";
	private static final String PLATE = "PLATE";
	private static final String NUMBER = "NUMBER";
	private static final String EXP = " EXP";
	private static final String MOYR = "MO/YR";
	private static final String VIN = "VIN";
	private static final String STKR = "STKR";
	private static final String PLT = "PLT";
	private static final String CODE = "CODE";
	private static final String FEES = "     FEES";
	private static final String TRANSID = "TRANSACTION ID";
	// defect 7568
	// Add space to constant to make room for 8 instead of 7
	private static final String USERID = "USER ID ";
	// end defect 7568
	private static final String PROCESSED = "PROCESSED";
	private static final String REPRINTED = "REPRINTED";
	private static final String VOIDED = "VOIDED";
	private static final String TOTALS = "TOTALS";
	private String csPreItmNo = "";
	// defect 10064	
    //public static final int DISKETTE_SUBCON_REPORT = 0;
	public static final int SUBCON_RSPS_REPORT = 0;
	// end defect 10064	
	public static final int DRAFT_SUBCON_REPORT = 1;
	public static final int FINAL_SUBCON_REPORT = 2;

	/**
	 * SubconRenewalReport constructor
	 */
	public GenSubconRenewalReport()
	{
		super();
	}

	/**
	 * SubconRenewalReport constructor
	 * 
	 * @param asRptString String
	 * @param asRptProperties ReportProperties
	 */
	public GenSubconRenewalReport(
		String asRptString,
		ReportProperties aaRptProperties)
	{
		super(asRptString, aaRptProperties);
	}

	/**
	 * Add To Total
	 * 
	 * @param aaLineAmt Dollar
	 */
	public void addToTotal(Dollar aaLineAmt)
	{
		caTotalAmt = caTotalAmt.add(aaLineAmt);
	}

	/**
	 * Format date to produce a string MM/DD/YYYY HH:MM:SS
	 * 
	 * @param aaDate RTSDate
	 * @return String
	 */
	private String formatDateTime(RTSDate aaDate)
	{
		return UtilityMethods.addPadding(
			new String[] {
				aaDate.toString() + " ",
				String.valueOf(aaDate.getHour()),
				":",
				String.valueOf(aaDate.getMinute()),
				":",
				String.valueOf(aaDate.getSecond()),
				},
			new int[] { 11, 2, 1, 2, 1, 2 },
			"0");
	}

	/**
	 * Format Report
	 * 
	 * @param aaSRCD SubcontractorRenewalCacheData
	 * @param aiRptId int
	 */
	public void formatReport(
		SubcontractorRenewalCacheData aaSRCD,
		int aiRptId)
	{
		try
		{
			Vector lvResults = null;
			// defect 10064
			//if (aiRptId == DISKETTE_SUBCON_REPORT)
			if (aiRptId == SUBCON_RSPS_REPORT)
			// end defect 10064
			{
				lvResults = aaSRCD.getSubconDiskData();
			}
			else
			{
				lvResults =
					new Vector(aaSRCD.getSubconTransData().values());
			}
			SubcontractorHdrData laSubcontractorHdrData =
				(SubcontractorHdrData) aaSRCD.getSubcontractorHdrData();
			// Adding additional Header information
			Vector lvHeader = new Vector();
			// DecimalFormat threeDigits = new DecimalFormat("000");
			int liSubconId = laSubcontractorHdrData.getSubconId();
			int liOfficeId = laSubcontractorHdrData.getOfcIssuanceNo();
			int liSubStationId = SystemProperty.getSubStationId();
			// SubcontractorCache laSubconCache = new SubcontractorCache();
			SubcontractorData laSubconData =
				(SubcontractorData) SubcontractorCache.getSubcon(
					liOfficeId,
					liSubStationId,
					liSubconId);
					
			// defect 10161 
			String lsSubconDesc = (String) laSubconData.getName1();
			// end defect 10161 
			
			// defect 10064
			//if (aiRptId == DISKETTE_SUBCON_REPORT)
			if (aiRptId == SUBCON_RSPS_REPORT)
			// end defect 10064
			{
				lvHeader.addElement("SUBCONTRACTOR ID");
				lvHeader.addElement(
					UtilityMethods.addPadding("" + liSubconId, 3, "0")
						+ "  "
						+ lsSubconDesc);
				lvHeader.addElement("PREVIOUS EXPORT");
				lvHeader.addElement(
					formatDateTime(
						laSubcontractorHdrData.getLastExportOnDate()));
				lvHeader.addElement("EXPORT");
				lvHeader.addElement(
					formatDateTime(
						laSubcontractorHdrData.getExportOnDate()));
				// defect 10064						
				//lvHeader.addElement("DISK SEQUENCE NO");
				lvHeader.addElement("RSPS SEQUENCE NO");
				// end defect 10064				
				lvHeader.addElement(
					String.valueOf(
						laSubcontractorHdrData.getDiskSeqNo()));
				lvHeader.addElement("RSPS ID");
				//defect 5754
				String lsValue = laSubcontractorHdrData.getProcsId();
				if (lsValue == null)
				{
					lsValue = "";
				}
				lvHeader.addElement(lsValue);
				//end defect 5754
			}
			else
			{
				RTSDate laInputDate =
					new RTSDate(
						2,
						laSubcontractorHdrData.getTransAMDate());
				lvHeader.addElement("SUBCONTRACTOR ID");
				lvHeader.addElement(
					UtilityMethods.addPadding("" + liSubconId, 3, "0")
						+ "  "
						+ lsSubconDesc);
				lvHeader.addElement("INPUT");
				lvHeader.addElement(laInputDate.toString());
				String lsValue = laSubcontractorHdrData.getProcsId();
				// defect 5754 
				if (lsValue != null)
				{
					// defect 10064						
					//lvHeader.addElement("DISK SEQUENCE NO");
					lvHeader.addElement("RSPS SEQUENCE NO");
					// end defect 10064					
					lvHeader.addElement(
						String.valueOf(
							laSubcontractorHdrData.getDiskSeqNo()));
					lvHeader.addElement("RSPS ID");
					lvHeader.addElement(lsValue);
				}
				// end defect 5754
			}
			Vector lvTable = new Vector();
			Vector lvColumn3 = new Vector();
			Vector lvColumn2 = new Vector();
			Vector lvColumn1 = new Vector();
			//First Row of Header
			lvColumn3.addElement(
				new ColumnHeader(PREV, COLUMN3_STARTPT, PREV.length()));
			lvTable.addElement(lvColumn3);
			//Second Row of Header				
			lvColumn2.addElement(
				new ColumnHeader(
					PLATE,
					COLUMN2_STARTPT,
					PLATE.length()));
			lvColumn2.addElement(
				new ColumnHeader(
					PLATE,
					COLUMN3_STARTPT,
					PLATE.length()));
			lvColumn2.addElement(
				new ColumnHeader(EXP, COLUMN5_STARTPT, EXP.length()));
			lvColumn2.addElement(
				new ColumnHeader(STKR, COLUMN6_STARTPT, EXP.length()));
			lvColumn2.addElement(
				new ColumnHeader(PLT, COLUMN7_STARTPT, EXP.length()));
			lvTable.addElement(lvColumn2);
			//Third Row of Header
			lvColumn1.addElement(
				new ColumnHeader(
					ISSUEDATE,
					COLUMN1_STARTPT,
					ISSUEDATE.length()));
			lvColumn1.addElement(
				new ColumnHeader(
					NUMBER,
					COLUMN2_STARTPT,
					NUMBER.length()));
			lvColumn1.addElement(
				new ColumnHeader(
					NUMBER,
					COLUMN3_STARTPT,
					NUMBER.length()));
			lvColumn1.addElement(
				new ColumnHeader(VIN, COLUMN4_STARTPT, VIN.length()));
			lvColumn1.addElement(
				new ColumnHeader(MOYR, COLUMN5_STARTPT, MOYR.length()));
			lvColumn1.addElement(
				new ColumnHeader(CODE, COLUMN6_STARTPT, CODE.length()));
			lvColumn1.addElement(
				new ColumnHeader(CODE, COLUMN7_STARTPT, CODE.length()));
			// defect 10064
			//if (aiRptId == DISKETTE_SUBCON_REPORT)
			if (aiRptId == SUBCON_RSPS_REPORT)
			// end defect 10064
			{
				lvColumn1.addElement(
					new ColumnHeader(
						USERID,
						COLUMN8_STARTPT,
						USERID.length()));
				lvColumn1.addElement(
					new ColumnHeader(
						PROCESSED,
						COLUMN9_STARTPT,
						PROCESSED.length()));
				lvColumn1.addElement(
					new ColumnHeader(
						REPRINTED,
						COLUMN10_STARTPT,
						REPRINTED.length()));
				lvColumn1.addElement(
					new ColumnHeader(
						VOIDED,
						COLUMN11_STARTPT,
						VOIDED.length()));
				lvColumn1.addElement(
					new ColumnHeader(
						FEES,
						COLUMN12_STARTPT,
						FEES.length()));
			}
			else
			{
				if (aiRptId == FINAL_SUBCON_REPORT)
				{
					lvColumn1.addElement(
						new ColumnHeader(
							TRANSID,
							COLUMN9_STARTPT + 5,
							TRANSID.length()));
				}
				lvColumn1.addElement(
					new ColumnHeader(
						FEES,
						COLUMN12_STARTPT,
						FEES.length()));
			}
			lvTable.addElement(lvColumn1);
			//Adding Column Header Information
			int i = 0;
			int liReprinted = 0;
			int liProcessed = 0;
			int liVoided = 0;
			SubcontractorRenewalData laDataline =
				new SubcontractorRenewalData();
			DecimalFormat laTwoDigits = new DecimalFormat("00");

			//sorting the vector
			// defect 10064
			//if (aiRptId == DISKETTE_SUBCON_REPORT)
			if (aiRptId == SUBCON_RSPS_REPORT)
			// end defect 10064
			{
				insertSortProcessDateTime(lvResults);
			}
			else
			{
				if (laSubcontractorHdrData.getProcsId() == null)
				{
					UtilityMethods.sort(lvResults);
				}
				else
				{
					insertTransKey(lvResults);
				}
			}
			
			// I doubt this can happen!  
			if (lvResults == null || lvResults.size() == 0)
			{
				generateHeader(lvHeader, lvTable);
				generateNoRecordFoundMsg();
				// defect 8628
				generateFooter(true);
				// end defect 8628 
			}
			else
			{
				//int length = SOLD_STARTPT - VOIDED_STARTPT + VOIDED.length();
				while (i < lvResults.size()) //Loop through the results
				{
					generateHeader(lvHeader, lvTable);
					int j = getNoOfDetailLines() - FOOTER_LINES;
					//Get Available lines on the page
					for (int k = 0; k <= j; k++)
					{
						if (i < lvResults.size())
						{
							laDataline =
								(
									SubcontractorRenewalData) lvResults
										.elementAt(
									i);
									
							// Unusual assignment at "top" of loop (KPH) 
							i++;
						
							//skip the invalid records for preliminary report
							// defect 10064
							//if (aiRptId == DISKETTE_SUBCON_REPORT)
							if (aiRptId == SUBCON_RSPS_REPORT
							// end defect 10064
								&& laDataline.isInvalidRecord())
							{
								continue;
							}
							String lsRegExpMonth =
								laTwoDigits.format(
									laDataline.getRegExpMo());

							//start printing
							// Issue Date
							caRpt.print(
								new RTSDate(
									1,
									laDataline.getSubconIssueDate())
									.toString(),
								COLUMN1_STARTPT,
								10);
							// Plate Number
							String lsPrevPltNo =
								laDataline.getRegPltNo();
							String lsNewPltNo =
								laDataline.getNewPltNo();
							if (lsNewPltNo == null
								|| lsNewPltNo.trim().equals("")
								|| lsPrevPltNo.equals(lsNewPltNo))
							{
								lsNewPltNo = lsPrevPltNo;
								lsPrevPltNo = " ";
							}

							caRpt.rightAlign(
								lsNewPltNo,
								COLUMN2_STARTPT,
								lsNewPltNo.length());
							// Prev Plate Number  
							caRpt.rightAlign(
								lsPrevPltNo,
								COLUMN3_STARTPT,
								lsPrevPltNo.length());
							// VIN
							caRpt.rightAlign(
								laDataline.getVIN(),
								COLUMN4_STARTPT - 7,
								laDataline.getVIN().length());
							// Exp Mo/Yr
							String lsExpMoYr =
								"" + laDataline.getNewExpYr();
							lsExpMoYr =
								lsRegExpMonth
									+ "/"
									+ lsExpMoYr.substring(2);
							caRpt.print(lsExpMoYr, COLUMN5_STARTPT, 5);
							if (laDataline.getStkrItmCd() != null)
							{
								caRpt.print(
									laDataline.getStkrItmCd(),
									COLUMN6_STARTPT,
									laDataline.getStkrItmCd().length());
							}
							else
							{
								caRpt.print("", COLUMN6_STARTPT, 1);
							}
							// New Plate Description
							if (laDataline.getPltItmCd() != null)
							{
								caRpt.print(
									laDataline.getPltItmCd(),
									COLUMN7_STARTPT,
									laDataline.getPltItmCd().length());
							}
							else
							{
								caRpt.print("", COLUMN7_STARTPT, 1);
							}
							// defect 10064
							//if (aiRptId == DISKETTE_SUBCON_REPORT)
							if (aiRptId == SUBCON_RSPS_REPORT)
							// end defect 10064
							{
								// defect 7568
								// Username can be 8 and not just 7
								//this.cRpt.print(dataline.getEmpId(), COLUMN8_STARTPT, 7);
								caRpt.print(
									laDataline.getEmpId(),
									COLUMN8_STARTPT,
									8);
								// end defect 7568

								switch (laDataline.getTransType())
								{
									//Processed
									case 0 :
										{
											caRpt.print(
												"1",
												COLUMN9_STARTPT + 4,
												1);
											++liProcessed;
											break;
											//Reprinted
										}
									case 1 :
										{
											caRpt.print(
												"1",
												COLUMN10_STARTPT + 4,
												1);
											++liReprinted;
											break;
											//voided
										}
									case -1 :
										{
											caRpt.print(
												"1",
												COLUMN11_STARTPT + 3,
												1);
											++liVoided;
											break;
										}
								}
							}
							else
							{
								if (aiRptId == FINAL_SUBCON_REPORT)
								{
									caRpt.print(
										String.valueOf(
											laDataline.getTransID()),
										COLUMN9_STARTPT + 5,
										17);
								}
							}

							// defect 8900
							// Print 0.00 Fees w/ Exempts  
							//if (laDataline
							//	.getRenwlTotalFees()
							//	.compareTo(new Dollar(0))
							//	!= 0)
							//{
							caRpt.rightAlign(
								laDataline
									.getRenwlTotalFees()
									.printDollar()
									.substring(
									1),
								COLUMN12_STARTPT,
								9);
							//}
							// end defect 8900

							addToTotal(laDataline.getRenwlTotalFees());
							// defect 5869
							handlePageBreak(2, lvHeader, lvTable);
							// end defect 5869
							caRpt.nextLine();
							// i = i + 1;
						}
					}
					if (lvResults.size() == i)
					{
						// defect 5869
						handlePageBreak(2, lvHeader, lvTable);
						// end defect 5869
						String lsTotalFee =
							caTotalAmt.printDollar().substring(1);
						// defect 10064
						//if (aiRptId == DISKETTE_SUBCON_REPORT)
						if (aiRptId == SUBCON_RSPS_REPORT)
						// end defect 10064
						{
							caRpt.rightAlign(
								DOUBLE_DASH,
								98,
								DOUBLE_DASH.length());
							caRpt.nextLine();
							String lsTotalSold = "" + liProcessed;
							String lsTotalReprinted = "" + liReprinted;
							String lsTotalVoided = "" + liVoided;
							caRpt.print(TOTALS, 84, TOTALS.length());
							caRpt.print(
								lsTotalSold,
								COLUMN9_STARTPT
									+ 4
									- lsTotalSold.length()
									+ 1,
								lsTotalSold.length());
							caRpt.print(
								lsTotalReprinted,
								COLUMN10_STARTPT
									+ 4
									- lsTotalReprinted.length()
									+ 1,
								lsTotalReprinted.length());
							caRpt.print(
								lsTotalVoided,
								COLUMN11_STARTPT
									+ 3
									- lsTotalVoided.length()
									+ 1,
								lsTotalVoided.length());
							caRpt.rightAlign(
								lsTotalFee,
								COLUMN12_STARTPT,
								9);
						}
						else
						{
							caRpt.rightAlign(
								SHORT_DOUBLE_DASH,
								122,
								SHORT_DOUBLE_DASH.length());
							caRpt.nextLine();
							caRpt.print(
								"PAYMENT AMT",
								PAYMENT_STARTPT,
								11);
							caRpt.rightAlign(
								lsTotalFee,
								COLUMN12_STARTPT,
								9);
						}
						caRpt.nextLine();
						// defect 8628 
						//generateEndOfReport();
					}
					// Note that this is strange;  i is incremented at
					//  the top of the loop. 
					// generateFooter();
					generateFooter(i == lvResults.size()); 
					// end defect 8628 
				} // end of while
			}
		}
		catch (RTSException aeRTSEx)
		{
			aeRTSEx.printStackTrace();
		}
	}

	/**
	 * Format Report
	 * 
	 * @param avResults Vector
	 */
	public void formatReport(Vector avResults)
	{
		// empty code block
	}

	/**
	 * Generate Attributes
	 */
	public void generateAttributes()
	{
		// empty code block
	}

	/**
	 * Handle Page Break
	 * 
	 * @param aiNumLinesNeeded int
	 * @param avHeader Vector
	 * @param avTable Vector
	 */
	private void handlePageBreak(
		int aiNumLinesNeeded,
		Vector avHeader,
		Vector avTable)
	{
		// defect 8628 
		if (getNoOfDetailLines() < aiNumLinesNeeded)
		{
			generateFooter();
			generateHeader(avHeader, avTable);
		}
		// end defect 8628
	}

	/**
	 * Insertion sort by ProcessDateTime 
	 * 
	 * @param avVector Vector
	 */
	private void insertSortProcessDateTime(Vector avVector)
	{
		if (avVector != null && avVector.size() != 0)
		{
			int k, aiLoc;
			int aiLeft = 0;
			int aiRight = avVector.size() - 1;
			for (k = aiLeft + 1; k <= aiRight; k++)
			{
				SubcontractorRenewalData laSRD =
					(SubcontractorRenewalData) avVector.get(k);
				aiLoc = k;

				while (aiLeft < aiLoc
					&& laSRD.getSubconProcessDateTime().compareTo(
						((SubcontractorRenewalData) avVector
							.get(aiLoc - 1))
							.getSubconProcessDateTime())
						< 0)
				{
					avVector.set(aiLoc, avVector.get(aiLoc - 1));
					aiLoc--;
				}
				avVector.set(aiLoc, laSRD);
			}
		}
	}

	/**
	 * Insertion sort by plate.
	 * 
	 * @param avVector Vector
	 */
	private void insertTransKey(Vector avVector)
	{
		if (avVector != null && avVector.size() != 0)
		{
			int k, aiLoc;
			int aiLeft = 0;
			int aiRight = avVector.size() - 1;
			for (k = aiLeft + 1; k <= aiRight; k++)
			{
				SubcontractorRenewalData laSRD =
					(SubcontractorRenewalData) avVector.get(k);
				aiLoc = k;
				while (aiLeft < aiLoc
					&& laSRD.getTransKeyNumber().compareTo(
						((SubcontractorRenewalData) avVector
							.get(aiLoc - 1))
							.getTransKeyNumber())
						< 0)
				{
					avVector.set(aiLoc, avVector.get(aiLoc - 1));
					aiLoc--;
				}
				avVector.set(aiLoc, laSRD);
			}
		}
	}

	/**
	 * Is Itm Break
	 * 
	 * @param aaDataline SubcontractorRenewalData
	 * @return boolean
	 */
	public boolean isItmBreak(SubcontractorRenewalData aaDataline)
	{
		InventoryServerBusiness laInvServerBusinessData =
			new InventoryServerBusiness();
		InventoryAllocationUIData laInvAllocUIData =
			new InventoryAllocationUIData();
		if (csPreItmNo == null)
		{
			return true;
		}
		if (csPreItmNo != "" && aaDataline.getPltItmCd() != null)
		{
			laInvAllocUIData.setItmCd(aaDataline.getPltItmCd());
			laInvAllocUIData.setInvItmNo(csPreItmNo);
			laInvAllocUIData.setInvItmYr(aaDataline.getNewExpYr());
			try
			{
				laInvAllocUIData =
					(
						InventoryAllocationUIData) laInvServerBusinessData
							.processData(
						GeneralConstant.INVENTORY,
						InventoryConstant.GET_NEXT_INVENTORY_NO,
						laInvAllocUIData);
				if (!aaDataline
					.getNewStkrNo()
					.equals(laInvAllocUIData.getInvItmEndNo())
					&& !aaDataline.getNewStkrNo().equals(csPreItmNo))
				{
					return true;
				}
			}
			catch (RTSException aeRTSEx)
			{
				return false;
			}
		}
		return false;
	}

	/**
	 * Starts the application.
	 * 
	 * @param aarrArgs String[] of command-line arguments
	 */
	public static void main(String[] aarrArgs)
	{
		// Instantiating a new Report Class
		ReportProperties laRptProps =
			new ReportProperties("RTS.POS.2011");
		// need to load "DRAFT" or "FINAL" into srr
		// depending on what buttons were pressed
		String lsStatus = "SUBCONTRACTOR RENEWAL REPORT - " + "DRAFT";
		GenSubconRenewalReport laSRR =
			new GenSubconRenewalReport(lsStatus, laRptProps);
		//
		// the following was sql taken from the AM code for this report
		//	
		//ITEMCOC1 (28) - CURSORSEL	Selects rows from RTS_ITEM_CODES where
		//			InvProcsngCd = 1 or 2, ordered by ItmCd. 1/27/92. 
		//			Used in ACC.AM
		//
		//CURSOR DECLARATION:
		//
		//	DECLARE ITEMCOC1 CURSOR FOR 
		//		SELECT ITMCD, ITMCDDESC, ITMTRCKNGTYPE, INVPROCSNGCD
		//			FROM RTS.RTS_ITEM_CODES
		//		WHERE INVPROCSNGCD = 1
		//			OR INVPROCSNGCD = 2
		//			ORDER BY ITMCD
		//
		//SUBCONS1 (55) - SINGLETON	Gets data for one subcontractor based on subcontractor ID.
		//	LOCAL OPTIONS: Selects a single row with all
		//			columns from the table RTS_SUBCON on SUBCONID. 
		//			Used in P447401.AM, P447290.AM.
		//
		//SELECT SUBCONID, SUBCONNAME, SUBCONST, SUBCONCITY,
		//			SUBCONZPCD, SUBCONZPCDP4 ,
		//			CHANGETSTMP,DELETEINDI
		//			FROM RTS.RTS_SUBCON
		//STATEMENT:
		//
		//	SELECT SUBCONID, SUBCONNAME, SUBCONST, SUBCONCITY,
		//		SUBCONZPCD, SUBCONZPCDP4 , CHANGETSTMP,DELETEINDI
		//	INTO 	:SUBCONID, :SUBCONNAME$, :SUBCONST$, :SUBCONCITY$, 
		//		:SUBCONZPCD, :SUBCONZPCDP4$:I_SUBCONZPCDP4, :CHANGETSTMP$
		//		:I_CHANGETSTMP, :DELETEINDI:I_DELETEINDI
		//	FROM RTS.RTS_SUBCON
		//	WHERE SUBCONID = :SUBCONID
		//		AND DELETEINDI = 0
		// :SUBCONID will be loaded with what was entered in UI
		String lsQuery =
			"SELECT SUBCONID, SUBCONNAME, SUBCONST, SUBCONCITY,"
				+ "SUBCONZPCD, SUBCONZPCDP4 , CHANGETSTMP,DELETEINDI"
				+ "INTO 	:SUBCONID, :SUBCONNAME$, :SUBCONST$, :SUBCONCITY$,"
				+ ":SUBCONZPCD, :SUBCONZPCDP4$:I_SUBCONZPCDP4, :CHANGETSTMP$"
				+ ":I_CHANGETSTMP, :DELETEINDI:I_DELETEINDI"
				+ "FROM RTS.RTS_SUBCON"
				+ "WHERE SUBCONID = :SUBCONID"
				+ "AND DELETEINDI = 0";
		Vector lvQueryResults = laSRR.queryData(lsQuery);
		laSRR.formatReport(lvQueryResults);
		// After the above query takes place, the following actions occur
		// and will be coded for somehow - the first 5 user actions have
		// already taken place (see the class comments)
		//
		/* 
		 * 6) An issue date is entered in the same window subcontractor
		 *    ID was entered in. The issue date helps the system determine
		 *    the proper fees.
		 * 7) Subcontractor renewal screen pops up, and it brings into the
		 *    first input field on the screen line, the first sticker number
		 *    from their inventory list. There are also three buttons active 
		 *    to press at this time:Plate, Sticker, or Plate and Sticker.	
		 * 8) User types in next to the sticker number, the current plate 
		 *    number, expiration month, registration class, and the fee.
		 *    Enter key is pressed.
		 * 9) Registration information is returned in bottom window, with a 
		 *	  check mark next to the info line if information is valid.
		 * 10)If a registration information line is highlighted, the user can
		 *    delete or modify that information.
		 * 11)When the listbox button is clicked on, the draft button is 
		 *	  highlighted for a draft report. If the listbox button is not 
		 *	  highlighted, then the report will be a final.
		 * 12)Once enter is pressed at this point, the total fees for that 
		 *    subcontractor pops up in a window.
		 */
		//Writing the Formatted String onto a local file
		File laOutputFile;
		FileOutputStream lpfsFout;
		PrintStream laPout = null;
		try
		{
			laOutputFile = new File("c:\\VoidTransactionReport.txt");
			lpfsFout = new FileOutputStream(laOutputFile);
			laPout = new PrintStream(lpfsFout);
		}
		catch (IOException aeIOEx)
		{
			// empty code block
		}
		laPout.print(laSRR.caRpt.getReport().toString());
		laPout.close();
		//Display the report
		try
		{
			FrmPreviewReport laFrmPreviewReport;
			laFrmPreviewReport =
				new FrmPreviewReport("c:\\VoidTransactionReport.txt");
			laFrmPreviewReport.setModal(true);
			laFrmPreviewReport.addWindowListener(new WindowAdapter()
			{
				public void windowClosing(WindowEvent aaWE)
				{
					System.exit(0);
				};
			});
			laFrmPreviewReport.show();
			// defect 7590
			// change setVisible to setVisibleRTS
			laFrmPreviewReport.setVisibleRTS(true);
			// end defect 7590
			// One way to Print Report.
			// Process p = Runtime.getRuntime().exec(
			// "cmd.exe /c copy c:\\VoidTransactionReport.txt prn");
		}
		catch (Throwable aeEx)
		{
			System.err.println(
				"Exception occurred in main() of "
					+ "com.txdot.isd.rts.client.general.ui.RTSDialogBox");
			aeEx.printStackTrace(System.out);
		}
	}

	/**
	 * Query Data
	 * 
	 * @param asQuery String
	 * @return Vector
	 */
	public Vector queryData(String asQuery)
	{
		Vector lvResults = new Vector();
		SubcontractorRenewalData laSubconData =
			new SubcontractorRenewalData();
		laSubconData.setSubconIssueDate(12345);
		laSubconData.setRegPltNo("XBV04N");
		laSubconData.setRegExpMo(3);
		laSubconData.setNewExpYr(2002);
		laSubconData.setNewStkrNo("439WC");
		//lSubconData.setStkrDesc("WS-WINDSHIELD STICKER");
		laSubconData.setNewPltNo("439XX");
		//lSubconData.setPltDesc("TEST PLATE");
		laSubconData.setRenwlTotalFees(new Dollar("61.80"));
		laSubconData.setSubconId(2);
		//lSubconData.setSubconDesc("Bob's Test Data");
		for (int i = 0; i < 150; i++)
		{
			lvResults.addElement(laSubconData);
		}
		SubcontractorRenewalData laSubconData1 =
			new SubcontractorRenewalData();
		laSubconData1.setSubconIssueDate(12345);
		laSubconData1.setRegPltNo("XBV04N");
		laSubconData1.setRegExpMo(3);
		laSubconData1.setNewExpYr(2002);
		laSubconData1.setNewStkrNo("439WC");
		//lSubconData1.setStkrDesc("WS-WINDSHIELD STICKER");
		laSubconData1.setNewPltNo("439XX");
		//lSubconData1.setPltDesc("TEST PLATE");
		laSubconData1.setRenwlTotalFees(new Dollar("1461.80"));
		laSubconData1.setSubconId(2);
		//lSubconData1.setSubconDesc("Bob's Test Data");
		lvResults.addElement(laSubconData1);
		/*Generating Demo data to display.
		Vector results = new Vector();
		// issueDate is an graph variable
		cissueDate 		   = "12/18/2000";
		String plateNo     = "XBV04N";
		String expireMonth = "03";
		String expireYear  = "2002";
		String stickerNO   = "439WC";
		String stickerDesc = "WS-WINDSHIELD STICKER";
		String newPlate    = "439XX";
		String plateDesc   = "TEST PLATE";
		String fee		   = "61.80";
		String transID	   = "16100036877154354";
		for (int i = 0; i < 150; i++) {
		    results.addElement(cissueDate);
		    results.addElement(plateNo);
		    results.addElement(expireMonth);
		    results.addElement(expireYear);
		    results.addElement(stickerNO);
		    results.addElement(stickerDesc);
		    results.addElement(newPlate);
		    results.addElement(plateDesc);
		    results.addElement(fee);
		    results.addElement(transID);
		}*/
		return lvResults;
	}
}