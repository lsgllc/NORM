package com.txdot.isd.rts.client.systemcontrolbatch.business;import java.util.Calendar;import java.util.HashMap;import java.util.Map;import java.util.Vector;import com.txdot.isd.rts.services.cache.AssignedWorkstationIdsCache;import com.txdot.isd.rts.services.cache.BatchReportManagementCache;import com.txdot.isd.rts.services.cache.CacheManager;import com.txdot.isd.rts.services.cache.OfficeTimeZoneCache;import com.txdot.isd.rts.services.communication.Comm;import com.txdot.isd.rts.services.data.*;import com.txdot.isd.rts.services.exception.RTSException;import com.txdot.isd.rts.services.util.*;import com.txdot.isd.rts.services.util.constants.*;/* * * BatchProcessor.java * * (c) Texas Department of Transportation 2001 * --------------------------------------------------------------------- * Change History: * Name			Date		Description * ------------	-----------	-------------------------------------------- * Ray Rowehl	03/18/2002	send the OfficeIsuanceNo and the SubstaId in *							FundsData in printSubstationSummary.  This is *							needed to correctly set the office name and  *							substation name in the Substation Summary  *							and CountyWide Reports. *							defect 2755 * Ray Rowehl	03/19/2002	Set the Userid and SummaryEffDate for  *							SubstationSummary. also set the wsid. *							defect 3009 * MAbs			04/23/2002	If FTP printing fails, don't stop  *							application.  Just print error to batchlog  *							defect 3632 * MAbs			05/22/2002	Caught all exceptions so it will always  *							reboot  *							defect 4064 * Ray Rowehl	06/13/2002	Send reports to Tray2. * Nancy Ting				defect 4249 * MAbs			07/24/2002	Added Internet batch report * S Govindappa	10/10/2002	Do not allow batch process run for  * 							substation summary after 7 AM. *							defect 4856  * S Govindappa	10/10/2002	Changed printReports()not to update  * 							LastRunTime if all the batch  *							processes have been skipped. *							defect 4425.  * S Govindappa	10/17/2002	Removed the 7 AM as the	maximum time for  * 							running the substation summary. *							defect 4856.  * S Govindappa	10/22/2002	Batch reports do not fail if  *							the reports cannot print. Made changes to  *							printReports(Vector) to log the exception  *							with printing, without failing the batch  *							reports in the database. *							defect 4926  * Ray Rowehl	01/14/2003	let print redirect if that has been  * 							specified. *							defect 5252   * Ray Rowehl	02/20/2003	redirect countywide when properties are  * 							passed. *							defect 5252   * Jeff S.		07/06/2003	Added barcode to Title Package report and  *							had to handle the printing differently by  *							calling Print method to create temp file *							with the barcode PCL then print and delete  *							the temp file *							printReports(Vector) *							defect 3994 * B Brown		08/20/2003  Added call to Phase 2  *							code printSpecialPlateAddressChangeReport(); *                          if (job.equals(BatchSchedule.SPIADDR)), in  *							method printReports(). *							defect 4885, PCR24   * Jeff S.		02/23/2004	Changed from FTP to LPR printing. Removed *							call to open and close FTP connection. *							Replaced calls to genBarCodeReceiptFile() *							with direct sendToPrinter() calls and let *							the printDocManager handle printing of *							dups and barcode files. (TitlePack rpt) *							Made sure that Integer.MIN_VALUE is not sent *							as a redirect parameter, so that Print will *							know that it is batch and throw exceptions *							back and not halt the batch process. *							modify printReports(Vector) *							defect 6848, 6898 Ver 5.1.6 * Jeff S.		03/05/2004	Created method that updates the *							RTS_WS_Status with the current workstation *							status only if it has changed. *							add updateWorkstationStatus() *							defect 6918 Ver 5.1.6 * Jeff S.		03/11/2004	Added some logging *							modify printReports(Vector) *							defect 6918 Ver 5.1.6 * Jeff S.		05/26/2004	Removed call to  *							Print.getDefaultPrinterProps() b/c it is not *							being used anymore. *							modify saveReports(Object) *							defect 7078 Ver 5.2.0 * Jeff S.		03/03/2005	Get code to standard. Changed a non-static * 							call to a static call. * 							modify printTitlePackageReport() *							defect 7897 Ver 5.2.3 * Ray Rowehl	03/28/2005	Change reference to BatchSceduleData * 							Move status constants into the data class. * 							modify imports * 							defect 7705 Ver 5.2.3 * K Harrell	05/18/2005	BIAR should not run after 7:00. Previously * 							just tested if hour >7. * 							modify printBatchInventoryActionReport() * 							defect 8208 Ver 5.2.3 * K Harrell	06/19/2005	Reorganize imports to address movement  * 							of services.cache.*Data to services.data  * 							defect 7884  Ver 5.2.3    * K Harrell	09/19/2005	Correct typo "Batch processesing completed" * 							modify printReports() * 							defect 7990 Ver 5.2.3        * Jeff S.		10/07/2005	Constant cleanup. *							defect 7897 Ver 5.2.3      * Jeff S.		10/31/2005	Remove sort of assigned WS ids since it is * 							done in the getter of Assigned WS ids. * 							modify printTitlePackageReport() * 							defect 8418 Ver 5.2.3 * K Harrell	11/30/2005	Update name for Completed Set-Aside  * 							Transaction Report for write to batch log. * 							defect 8379 Ver 5.2.3 * K Harrell	05/09/2007	Add logic for Special Plates Report * 							add SPCLPLTS * 							add printSpecialPlatesReport()  * 							modify printReports(), saveReports()       * 							defect 9085 Ver Special Plates * K Harrell	07/14/2007	Special Plates Report was printing in  * 							portrait. * 							modify saveReports() * 							defect 9085 Ver Special Plates   * K Harrell	02/09/2009 	delete SPCLPLTS * 							delete printSpecialPlatesReport() * 							modify printReports()  * 							defect 9941 Ver Defect_POS_D * K Harrell	02/12/2009  add processing for Internet Deposit Recon * 							Report.	 * 							add NO_DAYS_PRIOR_FOR_DEPOSIT_RPT, IDRR * 							add printInternetDepositReconReport() * 							modify printReports()  * 							defect 9935 Ver Defect_POS_D  * K Harrell	03/18/2009	add main() * 							defect 7682 Ver Defect_POS_E  * K Harrell	05/20/2009	Incorrect Workstation Id for Batch  * 							CountyWide, Substation Summary. * 							Additional class cleanup.  * 							modify printSubstationSummary() * 							defect 9899 Ver Defect_POS_F  * K Harrell	08/16/2009  Implement UtilityMethods.addPCLandSaveReport(), * 							ReportSearchData.initForClient()  * 							delete saveReports() * 							modify printBatchInventoryActionReport(),  * 							 printCompleteSetAside(),  * 							 printSubstationSummary(), * 							 printTitlePackageReport(),  * 							 printInternetDepositReconReport()   * 							defect 8628 Ver Defect_POS_F * K Harrell	08/29/2009	Correct BatchSchedule date comparison * 							modify printReports()  * 							defect 10165 Ver Defect_POS_F * K Harrell	03/31/2010	Additional tracking via WS_STATUS * 							add WS_RESTART_TIME_UPDATED  * 							delete WS_NOT_UPDATED * 							modify updateWorkstationStatus()  * 							defect 8087 Ver POS_640  * K Harrell	04/03/2010	Implement OfficeTimeZoneCache * 							modify updateWorkstationStatus()  * 							defect 10427 Ver POS_640  * K Harrell	12/10/2010	delete printInternetDepositReconReport() * 							delete IDRR, NO_DAYS_PRIOR_FOR_DEPOSIT_RPT  * 							modify printReports()  * 							defect 10694 Ver 6.7.0  * K Harrell	01/16/2011	modify to support print on demand. * 							modify printReports(Vector)   * 							defect 10701 Ver 6.7.0  * ---------------------------------------------------------------------                         		 *//** * Process Batch Reports. * * @version	6.7.0 			01/16/2011 * @author	Michael Abernethy * <br>Creation Date:		11/06/2001	14:45:53 *//* &BatchProcessor& */public class BatchProcessor{/* &BatchProcessor'BEGIN_BATCH& */	private static final String BEGIN_BATCH =		"Beginning Batch Processing";/* &BatchProcessor'BIAR& */	private static final String BIAR = "Batch Inventory Action Report";/* &BatchProcessor'BIAR_START_REQ& */	private static final String BIAR_START_REQ =		BIAR + " start request after 7:00 a.m.";/* &BatchProcessor'COMPLETE& */	private static final String COMPLETE = " completed.";/* &BatchProcessor'CSATR& */	private static final String CSATR =		"Completed Set-Aside Transaction Report";/* &BatchProcessor'DATE& */	private static final String DATE = "DATE";/* &BatchProcessor'END_BATCH& */	private static final String END_BATCH =		"Batch processing completed";/* &BatchProcessor'ERROR_GETTING_REDIRECT_INFO& */	private static final String ERROR_GETTING_REDIRECT_INFO =		"Exception retrieving the redirect info from "			+ "SystemProperty. Setting Redirect "			+ "to local SubstaId & WSId";/* &BatchProcessor'ERROR_RUNNING_BATCH& */	private static final String ERROR_RUNNING_BATCH =		"An error occurred while running the batch processes";/* &BatchProcessor'ERROR_RUNNING_WS& */	private static final String ERROR_RUNNING_WS =		"An error occurred while running the "			+ "Workstation Status Update ";/* &BatchProcessor'FAILED& */	private static final String FAILED = " failed.";/* &BatchProcessor'JOB_0_UPD& */	private static final String JOB_0_UPD = "Job Sequence 0 updated";	// XXX will be replaced with the job/* &BatchProcessor'JOB_UPDATED_IN_DB& */	private static final String JOB_UPDATED_IN_DB =		"Job XXX successfully updated in database";	// defect 10694 	//	private static final String IDRR =	//			"Internet Deposit Reconciliation Report";	//	private static final int NO_DAYS_PRIOR_FOR_DEPOSIT_RPT = 2;	// end defect 10694 /* &BatchProcessor'NO_TTP_TO_REPORT& */	private static final String NO_TTP_TO_REPORT =		" - no title packages to report";/* &BatchProcessor'NON_RTS_ERROR_RUNNING_BATCH& */	private static final String NON_RTS_ERROR_RUNNING_BATCH =		"A non-RTS-error occurred while running the batch processes";/* &BatchProcessor'NON_RTS_ERROR_RUNNING_WS& */	private static final String NON_RTS_ERROR_RUNNING_WS =		"A non-RTS error occurred while running the "			+ "Workstation Status Update ";/* &BatchProcessor'PROBLEM_PRINTING_RPT& */	private static final String PROBLEM_PRINTING_RPT =		"Report file XXX could not be printed - Detailed Msg =";/* &BatchProcessor'RETRIEV_BATCH_PROCESS& */	private static final String RETRIEV_BATCH_PROCESS =		"Retrieving batch processes to run";/* &BatchProcessor'SKIPPED& */	private static final String SKIPPED = " skipped.";/* &BatchProcessor'SSR& */	private static final String SSR = "Substation Summary Report";/* &BatchProcessor'SYSTEM& */	private static final String SYSTEM = "SYSTEM";/* &BatchProcessor'TPR& */	private static final String TPR = "Title Package Report";/* &BatchProcessor'WS_COMPLETE& */	private static final String WS_COMPLETE =		"Workstation Status Update Complete";	// defect 8087 	//private static final String WS_NOT_UPDATED =	//		"Workstation Status not updated";/* &BatchProcessor'WS_RESTART_TIME_UPDATED& */	private static final String WS_RESTART_TIME_UPDATED =		"Workstation Restart Timestamp Updated";	// end defect 8087 /* &BatchProcessor'WS_UPDATE& */	private static final String WS_UPDATE =		"Attempting Workstation Status update.";/* &BatchProcessor'XXX& */	private static final String XXX = "XXX";	/**	 * This is the starting point when running from server.	 * 	 * @throws RTSException 	 *//* &BatchProcessor.main& */	public static void main(String[] aarrArgs) throws RTSException	{		int liWorkstationId = SystemProperty.getWorkStationId();		int liOfcIssuanceNo = SystemProperty.getOfficeIssuanceNo();		int liSubstationId = SystemProperty.getSubStationId();		// Reload the cache		try		{			CacheManager.loadCache();		}		catch (RTSException aeRTSEx)		{			System.err.println("Failure to Load Cache");			System.exit(0);		}		AssignedWorkstationIdsData laAssgndWsData =			AssignedWorkstationIdsCache.getAsgndWsId(				liOfcIssuanceNo,				liSubstationId,				liWorkstationId);		if (laAssgndWsData != null			&& laAssgndWsData.getWsCd().equals("S"))		{			BatchProcessor laClientBatch = new BatchProcessor();			laClientBatch.printReports();		}		else		{			System.out.println("Batch must run from server.");		}	}	/**	 * BatchProcessor constructor comment.	 *//* &BatchProcessor.BatchProcessor& */	public BatchProcessor()	{		super();	}	/**	 * This method runs the Batch Inventory Action Report.	 * 	 * @return String	 *//* &BatchProcessor.printBatchInventoryActionReport& */	private String printBatchInventoryActionReport()	{		// BIAR should not run if after 7:00 a.m. 		int liCurrentTime = new RTSDate().get24HrTime();		if (liCurrentTime > 70000)		{			BatchLog.write(BIAR + SKIPPED);			BatchLog.error(BIAR_START_REQ);			return BatchScheduleData.SKIPPED;		}		try		{			ReportSearchData laRptSearchData = new ReportSearchData();			// defect 8628 			laRptSearchData.initForClient(ReportConstant.BATCH);			// end defect 8628  			laRptSearchData.setKey2(				Integer.toString(					RTSDate						.getCurrentDate()						.add(RTSDate.DATE, -1)						.getAMDate()));			laRptSearchData =				(ReportSearchData) Comm.sendToServer(					GeneralConstant.SYSTEMCONTROLBATCH,					SystemControlBatchConstant.GEN_BATCH_REPORT,					laRptSearchData);			// defect 8628 			Vector lvToPrint =				(Vector) UtilityMethods.addPCLandSaveReport(					laRptSearchData);			// end defect 8628			printReports(lvToPrint);			BatchLog.write(BIAR + COMPLETE);			return BatchScheduleData.SUCCESSFUL;		}		catch (RTSException aeEx)		{			BatchLog.write(BIAR + FAILED);			BatchLog.error(				BIAR					+ CommonConstant.SYSTEM_LINE_SEPARATOR					+ aeEx.getDetailMsg());			return BatchScheduleData.UNSUCCESSFUL;		}	}	/**	 * This method runs the Batch Complete SetAside Report.	 * 	 * @return String	 *//* &BatchProcessor.printCompleteSetAside& */	private String printCompleteSetAside()	{		try		{			ReportSearchData laRptSearchData = new ReportSearchData();			// defect 8628 			laRptSearchData.initForClient(ReportConstant.BATCH);			// end defect 8628			laRptSearchData =				(ReportSearchData) Comm.sendToServer(					GeneralConstant.SYSTEMCONTROLBATCH,					SystemControlBatchConstant.GEN_COMPLETE_SETASIDE,					laRptSearchData);			// defect 8628			Vector lvToPrint =				UtilityMethods.addPCLandSaveReport(laRptSearchData);			// end defect 8628			printReports(lvToPrint);			BatchLog.write(CSATR + COMPLETE);			return BatchScheduleData.SUCCESSFUL;		}		catch (RTSException aeEx)		{			BatchLog.write(CSATR + FAILED);			BatchLog.error(				CSATR					+ CommonConstant.SYSTEM_LINE_SEPARATOR					+ aeEx.getDetailMsg());			return BatchScheduleData.UNSUCCESSFUL;		}	}	//	/**	//	 * Prints the Iternet Deposit Recon Report	//	 * 	//	 * @return String	//	 */	//	private String printInternetDepositReconReport()	//	{	//		try	//		{	//			ReportSearchData laRptSearchData = new ReportSearchData();	//	//			RTSDate laRunDate =	//				new RTSDate().add(	//					RTSDate.DATE,	//					-NO_DAYS_PRIOR_FOR_DEPOSIT_RPT);	//	//			// defect 8628 	//			laRptSearchData.initForClient(ReportConstant.BATCH);	//			// end defect 8628	//	//			laRptSearchData.setDate1(laRunDate);	//			laRptSearchData.setDate2(laRunDate);	//	//			laRptSearchData =	//				(ReportSearchData) Comm.sendToServer(	//					GeneralConstant.INTERNET_REG_REN_PROCESSING,	//					RegRenProcessingConstants.GET_DEPOSIT_RECON_RPT,	//					laRptSearchData);	//	//			// defect 8628	//			Vector lvToPrint =	//				UtilityMethods.addPCLandSaveReport(laRptSearchData);	//			// end defect 8628 	//	//			printReports(lvToPrint);	//			BatchLog.write(IDRR + COMPLETE);	//			return BatchScheduleData.SUCCESSFUL;	//		}	//		catch (RTSException aeEx)	//		{	//			BatchLog.write(IDRR + FAILED);	//			BatchLog.error(	//				IDRR	//					+ CommonConstant.SYSTEM_LINE_SEPARATOR	//					+ aeEx.getDetailMsg());	//			return BatchScheduleData.UNSUCCESSFUL;	//		}	//	}	/**	 * This method prints the batch reports.	 *//* &BatchProcessor.printReports& */	public void printReports()	{		try		{			BatchLog.write(BEGIN_BATCH);			HashMap lhmHash = new HashMap();			lhmHash.put(				AccountingConstant.OFC,				new Integer(SystemProperty.getOfficeIssuanceNo()));			lhmHash.put(				AccountingConstant.SUB,				new Integer(SystemProperty.getSubStationId()));			BatchLog.write(RETRIEV_BATCH_PROCESS);			Vector lvProcesses =				(Vector) Comm.sendToServer(					GeneralConstant.SYSTEMCONTROLBATCH,					SystemControlBatchConstant.GET_BATCH_PROCESS,					lhmHash);			BatchScheduleData laJobData = new BatchScheduleData();			// defect 10165 			int liCurrentDate =				RTSDate.getCurrentDate().getYYYYMMDDDate();			// end defect 10165 			boolean lbBatchComplIndi = false;			for (int i = 0; i < lvProcesses.size(); i++)			{				BatchScheduleData laData =					(BatchScheduleData) lvProcesses.get(i);				// defect 10694 				RTSDate laDate = RTSDate.getCurrentDate();				laData.setJobStartTime(laDate.get24HrTime());				// end defect 10694 				if (laData.getJobSeqNo() == 0)				{					laJobData = laData;					laJobData.setBatchStatus(						BatchScheduleData.INCOMPLETE);					continue;				}				String lsJob = laData.getJobName();				// defect 10165				// Do not convert JobRunDate to RTSDate (Avoid Exceptions) 				// Continue if job already run today				//	if (laData.getJobRunDate() > 0)				//	{				//		RTSDate laLastRunDate =				//			new RTSDate(				//				RTSDate.YYYYMMDD,				//				laData.getJobRunDate());				// 				//		if (laLastRunDate.getDate()				//			== laToday.getDate()				//			&& laData.getJobStatus().equals(				//				BatchScheduleData.SUCCESSFUL))				//		{				if (laData.getJobRunDate() == liCurrentDate					&& laData.getJobStatus().equals(						BatchScheduleData.SUCCESSFUL))				{					// end defect 10165 					continue;				}				String lsReturnCode = CommonConstant.STR_SPACE_EMPTY;				if (lsJob.equals(BatchScheduleData.B_INV_RP))				{					lsReturnCode = printBatchInventoryActionReport();				}				else if (lsJob.equals(BatchScheduleData.COMP_SET))				{					lsReturnCode = printCompleteSetAside();				}				else if (lsJob.equals(BatchScheduleData.SUB_SUMM))				{					lsReturnCode = printSubstationSummary();				}				else if (lsJob.equals(BatchScheduleData.TTL_PACK))				{					lsReturnCode = printTitlePackageReport();				}				// defect 10694 				// defect 9935 				//	else if (lsJob.equals(BatchScheduleData.INET_DEPOSIT))				//	{				//		lsReturnCode = printInternetDepositReconReport();				//	}				//	// end defect 9935 				//	else if (lsJob.equals(BatchScheduleData.INTERNT))				//	{				//		lsReturnCode =				//			new RegRenProcessingClientBusiness()				//				.printVendorReport();				//	}				// end defect 10694 				else				{					continue;				}				laData.setJobRunDate(liCurrentDate);				laData.setJobRunTime(laDate.get24HrTime());				laData.setJobStatus(lsReturnCode);				if (!lsReturnCode.equals(BatchScheduleData.SKIPPED))				{					Comm.sendToServer(						GeneralConstant.SYSTEMCONTROLBATCH,						SystemControlBatchConstant.UPDATE_BATCH_RESULTS,						laData);					lbBatchComplIndi = true;				}				BatchLog.write(					JOB_UPDATED_IN_DB.replaceFirst(						XXX,						String.valueOf(laData.getJobSeqNo())));			}			if (lbBatchComplIndi)			{				RTSDate laDate = RTSDate.getCurrentDate();				laJobData.setLastRunDate(laDate.getYYYYMMDDDate());				laJobData.setLastRunTime(laDate.get24HrTime());				laJobData.setBatchStatus(BatchScheduleData.COMPLETE);				Comm.sendToServer(					GeneralConstant.SYSTEMCONTROLBATCH,					SystemControlBatchConstant.UPDATE_BATCH_RESULTS,					laJobData);				BatchLog.write(JOB_0_UPD);				BatchLog.write(END_BATCH);			}		}		catch (RTSException aeRTSEx)		{			BatchLog.write(ERROR_RUNNING_BATCH);			BatchLog.error(				ERROR_RUNNING_BATCH					+ CommonConstant.STR_SPACE_ONE					+ aeRTSEx.getDetailMsg());		}		catch (Exception aeEx)		{			BatchLog.write(NON_RTS_ERROR_RUNNING_BATCH);			BatchLog.error(				NON_RTS_ERROR_RUNNING_BATCH					+ CommonConstant.STR_SPACE_ONE					+ aeEx.getMessage());		}	}	/**	 * This method will print the Vector if reports that are passed.	 *	 * @param  avReports Vector	 * @return Object 	 *//* &BatchProcessor.printReports$1& */	private Object printReports(Vector avReports)	{		Print laPrint = new Print();		ReportSearchData laRptSearchData =			(ReportSearchData) avReports.get(0);		// set up redirect of reports when going across substas		int liReDirectSubstaId = Integer.MIN_VALUE;		int liReDirectWsId = Integer.MIN_VALUE;		try		{			if (avReports.size() > 1)			{				// get the redirect print properties				liReDirectSubstaId =					((Integer) avReports.get(1)).intValue();				liReDirectWsId =					((Integer) avReports.get(2)).intValue();				// defect 6848, 6898				// Checked for Integer.MIN_VALUE to determine if redirect				// is actually intended				if (liReDirectSubstaId == Integer.MIN_VALUE)				{					liReDirectSubstaId =						SystemProperty.getSubStationId();				}				if (liReDirectWsId == Integer.MIN_VALUE)				{					liReDirectWsId = SystemProperty.getWorkStationId();				}			}			// added else b/c actual SubstaId and WSId need to be used			// if redirect values are not passed in the vector.  This			// way Print will know that batch is the calling program			else			{				liReDirectSubstaId = SystemProperty.getSubStationId();				liReDirectWsId = SystemProperty.getWorkStationId();			}		}		catch (Exception aeEx)		{			// Using actual SubstaId and WSId instead of Integer.MIN_VALUE			// reset back to min value so report will still print			liReDirectSubstaId = SystemProperty.getSubStationId();			liReDirectWsId = SystemProperty.getWorkStationId();			BatchLog.error(ERROR_GETTING_REDIRECT_INFO);		}		// end defect 6848, 6898		try		{			if ((laRptSearchData.getKey1() != null)				&& !(laRptSearchData.getKey1()).equals(					CommonConstant.STR_SPACE_EMPTY))			{				String lsFileName = laRptSearchData.getKey1();				// defect 10701 				if (BatchReportManagementCache					.isAutoPrint(						laRptSearchData.getKey2().trim()							+ ReportConstant.RPT_EXTENSION))				{					// end defect 10701 					// defect 3994					// Added barcode to Title Package Report needed to generate PCL					// for the barcode if this report is the Title Package Report					// If the file name's first four character are TTLP then it					// contains a barcode so then the barcode PCL needes to be 					// generated					String lsParsedFilename =						laPrint.parseFileName(lsFileName, 4);					if (lsParsedFilename						.equalsIgnoreCase(TransCdConstant.TTLP))					{						// Now passing the first 4 characters of the filename as						// the trans code for both Tittle Package and Void						// both of these reports are special cases because they						// either contain a barcode or duplicates are needed						laPrint.sendToPrinter(							lsFileName,							lsParsedFilename,							liReDirectSubstaId,							liReDirectWsId);						// end defect 6848, 6898					}					else					{						laPrint.sendToPrinter(							lsFileName,							null,							liReDirectSubstaId,							liReDirectWsId);					}					// end defect 3994				// defect 10701 				}				else				{					BatchLog.write("AutoPrint Off for " + lsFileName);				}				// end defect 10701 			}		}		catch (RTSException aeRTSEx)		{			// defect 6848, 6898			// fixed logging to show detailed message			BatchLog.error(				PROBLEM_PRINTING_RPT.replaceFirst(					XXX,					laRptSearchData.getKey1())					+ aeRTSEx.getDetailMsg());			// end defect 6848, 6898		}		return laRptSearchData;	}	/** 	 * Issue the call to generate the Substation Summary and CountyWide 	 * reports as appropriate.	 * 	 * @return String	 *//* &BatchProcessor.printSubstationSummary& */	private String printSubstationSummary()	{		try		{			Map lhmMap = new HashMap();			lhmMap.put(				AccountingConstant.OFC,				new Integer(SystemProperty.getOfficeIssuanceNo()));			lhmMap.put(				AccountingConstant.SUB,				new Integer(SystemProperty.getSubStationId()));			// Initializes Ofcissuanceno, SubstaId, SummaryEffDate, 			//   Vector of CloseoutHstryData where SummaryEffDate null     			FundsData laFundsData =				(FundsData) Comm.sendToServer(					GeneralConstant.SYSTEMCONTROLBATCH,					SystemControlBatchConstant.GET_SUBSTA_INFO,					lhmMap);			// defect 8628			//  Move init of Ofc, Substaid to above process			// end defect 8628 			// set userid    			laFundsData.setEmployeeId(SYSTEM);			laFundsData.setWorkstationId(				SystemProperty.getWorkStationId());			laFundsData.setBatch(true);			laFundsData.setCountyWide(				(SystemProperty.getSubStationId() == 0));			ReportSearchData laRptSearchData =				(ReportSearchData) Comm.sendToServer(					GeneralConstant.FUNDS,					FundsConstant.GENERATE_SUBSTATION_SUMMARY_REPORT,					laFundsData);			Vector lvToPrint =				UtilityMethods.addPCLandSaveReport(laRptSearchData);			// end defect 8628			// pass redirect parameters			lvToPrint.add(				new Integer(SystemProperty.getRedirectSubStaId()));			lvToPrint.add(				new Integer(SystemProperty.getRedirectWsId()));			printReports(lvToPrint);			BatchLog.write(SSR + COMPLETE);			return BatchScheduleData.SUCCESSFUL;		}		catch (RTSException aeEx)		{			BatchLog.write(SSR + FAILED);			BatchLog.error(				SSR					+ CommonConstant.SYSTEM_LINE_SEPARATOR					+ aeEx.getDetailMsg());			return BatchScheduleData.UNSUCCESSFUL;		}	}	/**	 * Prints the Title Package Report.	 * 	 * @return String	 *//* &BatchProcessor.printTitlePackageReport& */	private String printTitlePackageReport()	{		try		{			Calendar laToday = Calendar.getInstance();			int liDay = laToday.get(Calendar.DAY_OF_WEEK);			if (liDay == Calendar.SUNDAY || liDay == Calendar.MONDAY)			{				Map lhmMap = new HashMap();				lhmMap.put(					AccountingConstant.OFC,					new Integer(SystemProperty.getOfficeIssuanceNo()));				lhmMap.put(					AccountingConstant.SUB,					new Integer(SystemProperty.getSubStationId()));				lhmMap.put(DATE, RTSDate.getCurrentDate());				Integer liReturnValue =					(Integer) Comm.sendToServer(						GeneralConstant.SYSTEMCONTROLBATCH,						SystemControlBatchConstant.GET_TITLE_NUM,						lhmMap);				if (liReturnValue.intValue() < 1)				{					BatchLog.write(TPR + SKIPPED);					BatchLog.error(TPR + NO_TTP_TO_REPORT);					return BatchScheduleData.SKIPPED;				}			}			ReportSearchData laRptSearchData = new ReportSearchData();			// defect 8628 			laRptSearchData.initForClient(ReportConstant.BATCH);			Vector lvWsId =				AssignedWorkstationIdsCache.getTtlPkgWsIds(					SystemProperty.getOfficeIssuanceNo(),					SystemProperty.getSubStationId());			laRptSearchData.setVector(lvWsId);			// end defect 8628 			laRptSearchData.setKey2(				Integer.toString(					RTSDate						.getCurrentDate()						.add(RTSDate.DATE, -1)						.getAMDate()));			laRptSearchData =				(ReportSearchData) Comm.sendToServer(					GeneralConstant.SYSTEMCONTROLBATCH,					SystemControlBatchConstant.GEN_TITLE_PACKAGE,					laRptSearchData);			// defect 8628			Vector lvToPrint =				UtilityMethods.addPCLandSaveReport(laRptSearchData);			// end defect 8628  			printReports(lvToPrint);			BatchLog.write(TPR + COMPLETE);			return BatchScheduleData.SUCCESSFUL;		}		catch (RTSException aeEx)		{			BatchLog.write(TPR + FAILED);			BatchLog.error(				TPR					+ CommonConstant.SYSTEM_LINE_SEPARATOR					+ aeEx.getDetailMsg());			return BatchScheduleData.UNSUCCESSFUL;		}	}	//	/**	//	 * Save reports to hard drive.	//	 *	//	 * @param  aaRptSearchData ReportSearchData	//	 * @return Object	//	 * @throws RTSException	//	 */	//	private Object saveReports(ReportSearchData aaRptSearchData)	//		throws RTSException	//	{	//		UtilityMethods laUtil = new UtilityMethods();	//		Print laPrint = new Print();	//		Vector lvReports = new Vector();	//	//		String lsPageProps = laPrint.getDefaultPageProps();	//	//		// defect 9085 	//		// If Special Plates Batch Report, print in landscape 	//		if (aaRptSearchData	//			.getKey2()	//			.equals(ReportConstant.RPT_6001_BATCH_FILENAME))	//		{	//			lsPageProps =	//				lsPageProps.substring(0, 2)	//					+ Print.getPRINT_LANDSCAPE()	//					+ lsPageProps.substring(2);	//		}	//		// end defect 9085 	//	//		// defect 4249	//		// send reports to tray 3	//		String lsRpt =	//			lsPageProps	//				+ Print.getPRINT_TRAY_2()	//				+ CommonConstant.SYSTEM_LINE_SEPARATOR	//				+ aaRptSearchData.getKey1();	//		// end defect 4249	//	//		String lsFileName =	//			laUtil.saveReport(	//				lsRpt,	//				aaRptSearchData.getKey2(),	//				aaRptSearchData.getIntKey1());	//		aaRptSearchData.setKey1(lsFileName);	//	//		lvReports.add(aaRptSearchData);	//	//		return lvReports;	//	}	/**	 * This method is called from RTSMain after printReports() is 	 * called.  	 * 	 * A Row will be inserted into RTS_WS_STATUS if a row currently does	 * not exist. 	 * 	 * The existing data will be modified if any changes since 	 * last update/insert:	 *  - RTSVersion	 *  - RTSVersionDate	 *  - CPName	 *  - CDSrvrCPName	 *  - CDSrvrIndiu	 *  - JarSize	 *  - ServletHost	 *  - ServletPort	 * 	 * LastRestartTstmp will always be set/updated.	 * 	 * For any insert, change (besides LastRestartTstmp), a row will be 	 * inserted into RTS.RTS_WS_STATUS_LOG with the data prior to 	 * modification. 	 *//* &BatchProcessor.updateWorkstationStatus& */	public void updateWorkstationStatus()	{		// defect 8087 		WorkstationStatusData laWSStatusData =			new WorkstationStatusData();		try		{			BatchLog.write(WS_UPDATE);			laWSStatusData.setOfcIssuanceNo(				SystemProperty.getOfficeIssuanceNo());			laWSStatusData.setSubStaId(				SystemProperty.getSubStationId());			laWSStatusData.setWSid(SystemProperty.getWorkStationId());			// RTSVersion 			laWSStatusData.setRTSVersion(SystemProperty.getVersionNo());			// RTSVersionDate 			laWSStatusData.setRTSVersionDate(				SystemProperty.getVersionDate());			// CPName			String lsCPName = new String();			try			{				lsCPName =					java.net.InetAddress.getLocalHost().getHostName();			}			catch (java.net.UnknownHostException aeUHEx)			{				Log.write(Log.APPLICATION, this, "Can't get CPNAME");			}			laWSStatusData.setCPName(lsCPName);			// Code Server CPName 			laWSStatusData.setCDSrvrCPName(				SystemProperty.getCommServerName());			// Code Server 			laWSStatusData.setCDSrvr(SystemProperty.isCommServer());			// Servlet Host 			laWSStatusData.setServletHost(				SystemProperty.getServletHost());			// Servlet Port			String lsServletPort = SystemProperty.getServletPort();			laWSStatusData.setServletPort(				new Integer(lsServletPort).intValue());			// Jar Size 			long llJarSize = 0;			java.io.File laFile =				new java.io.File(					SystemProperty.getRTSAppDirectory()						+ "RTSClient.jar");			if (laFile.exists())			{				llJarSize = laFile.length();			}			laWSStatusData.setJarSize(llJarSize);			// LastRestartTstmp 			RTSDate laNewRTSDate = new RTSDate();			// defect 10427 			//	try			//	{			//		AssignedWorkstationIdsData laAssgndWsIdsData =			//			AssignedWorkstationIdsCache.getAsgndWsId(			//				laWSStatusData.getOfcIssuanceNo(),			//				laWSStatusData.getSubStaId(),			//				laWSStatusData.getWSid());			//			//		if (laAssgndWsIdsData			//			.getTimeZone()			//			.equalsIgnoreCase("M"))			if (OfficeTimeZoneCache				.isMountainTimeZone(laWSStatusData.getOfcIssuanceNo()))			{				laNewRTSDate.setHour(laNewRTSDate.getHour() - 1);			}			// }			//	catch (RTSException aeRTSEx)			//	{			//	}			// end defect 10427 			laWSStatusData.setLastRestartTstmp(laNewRTSDate);			// Insert/Update table 			Object lbChanged =				Comm.sendToServer(					GeneralConstant.SYSTEMCONTROLBATCH,					SystemControlBatchConstant						.UPDATE_WORKSTATION_STATUS,					laWSStatusData);			if (((Boolean) lbChanged).booleanValue())			{				// DB updated (something besides LastRestartTstmp)  				BatchLog.write(WS_COMPLETE);			}			else			{				// Only Restart Timestmp Updated				BatchLog.write(WS_RESTART_TIME_UPDATED);			}		}		catch (RTSException aeRTSEx)		{			BatchLog.error(ERROR_RUNNING_WS + aeRTSEx.getDetailMsg());		}		catch (Exception aeEx)		{			BatchLog.error(				NON_RTS_ERROR_RUNNING_WS + aeEx.getMessage());		}		// Unreachable code 		//		catch (NumberFormatException aeNFEx)		//		{		//			BatchLog.error(		//				"Number Format Exception " + aeNFEx.getMessage());		//		}		// end defect 8087	}}/* #BatchProcessor# */